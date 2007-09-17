/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.*;
import java.util.HashMap;
import java.util.Map;

public class JavafxInitializationBuilder extends JavafxTreeScanner {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private JCClassDecl currentClassDef;
    private JavafxTreeMaker make;
    private JavafxResolve rs;
    private Name.Table names;
    private JavafxEnv<JavafxAttrContext> env;
    
    private Name addChangeListenerName;
    private Name changeListenerInterfaceName;
    
    final Name initializerName;
    private final String initSyntheticName = "$init$synth$";
    
    private List<JCMethodDecl> currentInitBlocks = null;
    private int currentInitCounter = 0;
    
    private JavafxSymtab syms = null;
    
    private final Name changedName;
    
    private Map<JFXAttributeDefinition, ChangeProcessingHelper> currentChangeHelpers;

    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        rs = JavafxResolve.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        
        names = Name.Table.instance(context);
        addChangeListenerName = names.fromString("addChangeListener");
        changeListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "ChangeListener");
        
        initializerName = names.fromString("javafx$init$");

        changedName = names.fromString("onChange");
    }
    
    public void visitTopLevel(JCCompilationUnit cu, JavafxEnv<JavafxAttrContext> env) {
        this.env = env;
        visitTopLevel(cu);
        this.env = null;
    }
    
    @Override
    public void visitClassDef(JCClassDecl tree) {
        handleClassDef(tree, null);
    }
    
    private void handleClassDef(JCClassDecl tree, JFXClassDeclaration jfxClassDecl) {
        JCClassDecl prevClassDef = currentClassDef;
        List<JCMethodDecl> prevInitBlocks = currentInitBlocks;
        int prevInitCounter = currentInitCounter;
        try {
            currentInitBlocks = List.<JCMethodDecl>nil();
            currentClassDef = tree;
            prevInitCounter = 0;
            super.visitClassDef(tree);
            JCMethodDecl initializer = null;
            if (jfxClassDecl != null) {
                initializer = this.createInitializerMethod(jfxClassDecl);
            }
            
            handleInitializerMethod(initializer);
        }
        finally {
            currentClassDef = prevClassDef;
            currentInitBlocks = prevInitBlocks;
            currentInitCounter = prevInitCounter;
        }
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        Map<JFXAttributeDefinition, ChangeProcessingHelper>prevChangeHelpers = currentChangeHelpers;
        try {
            currentChangeHelpers = new HashMap<JFXAttributeDefinition, ChangeProcessingHelper>();
            handleClassDef(tree, tree);
        }
        finally {
            currentChangeHelpers = prevChangeHelpers;
        }
    }
    
    private void handleInitializerMethod(JCMethodDecl initializer) {
        JCBlock initializerBlock = null;
        if (currentClassDef != null && 
                currentClassDef.defs != null &&
                !currentClassDef.defs.isEmpty() &&
                initializer != null) {
            // Initialize the default values for the atttributes.
            for (JCTree tree : currentClassDef.defs) {
                if (tree.getTag() == JavafxTag.ATTRIBUTEDEF) {
                    JCVariableDecl jfxVarDecl = (JCVariableDecl)tree;
                    JCLiteral jcLiteral = getEmptyLiteral(jfxVarDecl.type);
                    if (jcLiteral == null) {
                        continue;
                    }
                    
                    // If not initialized, skip it.
                    if (jfxVarDecl.init == null) {
                        continue;
                    }
                    
                    if (initializerBlock == null) {
                        initializerBlock = initializer.body;
                    }
                    
                    // Assert that there is an initializerBlock
                    assert initializerBlock != null : "initializerBlock must not be null!";
                    
                    JCIdent lhsIdent = make.Ident(jfxVarDecl.name);
                    lhsIdent.sym = jfxVarDecl.sym;
                    lhsIdent.type = jfxVarDecl.type;
                    JCBinary cond = make.Binary(JCTree.EQ, lhsIdent, jcLiteral);
                    cond.type = JavafxSymtab.booleanType;
                    cond.operator = rs.resolveBinaryOperator(cond.pos(), cond.getTag(), env, lhsIdent.type, jcLiteral.type);

                    JCIdent lhsAssignIdent = make.Ident(jfxVarDecl.name);
                    lhsAssignIdent.sym = jfxVarDecl.sym;
                    lhsAssignIdent.type = jfxVarDecl.type;
                    JCAssign defValAssign = make.Assign(lhsAssignIdent, jfxVarDecl.init);
                    defValAssign.type = lhsAssignIdent.type;
                    
                    JCExpressionStatement defAttrValue = make.Exec(defValAssign);
                    defAttrValue.type = null;
                    
                    JCIf jcIf = make.If(cond, defAttrValue, null);
                    jcIf.type = null;
                    
                    initializerBlock.stats = initializerBlock.stats.append(jcIf);
                    jfxVarDecl.init = null;
                }
            }
            
            // Call the init blocks and postprocess blocks if there are any
            if (initializerBlock == null) {
                    initializerBlock = initializer.body;
            }
            
            if (currentInitBlocks != null) {
                for (JCMethodDecl init : currentInitBlocks) {
                    List<JCExpression> typeargs = List.nil();
                    List<JCExpression> args = List.nil();
                    JCIdent initIdent = make.Ident(init.name);
                    initIdent.sym = init.sym;
                    initIdent.type = init.type;

                    JCMethodInvocation tmpApply = make.Apply(typeargs, initIdent, args);

                    JCExpressionStatement initCall = make.Exec(tmpApply);
                    initializerBlock.stats = initializerBlock.stats.append(initCall);
                }
            }

            // Now do the on change blocks of the JavafxJCVarDecl
            for (JCTree tree : currentClassDef.defs) {
                if (tree.getTag() == JavafxTag.ATTRIBUTEDEF) {
                    JFXAttributeDefinition jfxAttributeDefinition = (JFXAttributeDefinition)tree;

                    ChangeProcessingHelper changeProcHelper = currentChangeHelpers.get(jfxAttributeDefinition);
                    
                    if (changeProcHelper == null) {
                        continue;
                    }
                    
                    JCNewClass anonymousChangeListener = changeProcHelper.newClass;
                    
                    // No change listener, skip it.
                    if (anonymousChangeListener == null) {
                        continue;
                    }
                    
                    if (initializerBlock == null) {
                        initializerBlock = initializer.body;
                    }
                    
                    // Assert that there is an initializerBlock
                    assert initializerBlock != null : "initializerBlock must not be null!";

                    JCIdent varIdent = make.Ident(jfxAttributeDefinition.name);
                    varIdent.type = jfxAttributeDefinition.type;
                    varIdent.sym = jfxAttributeDefinition.sym;
                    
                    JCFieldAccess tmpSelect = make.Select(varIdent, addChangeListenerName);
/***                    
                    // Get the symbol for addChangeListener(ChangeListener listener) method.
                    Scope.Entry entry = null;
                    TypeSymbol lookIn = varIdent.type.tag == TypeTags.CLASS ? (ClassSymbol)varIdent.type.tsym : null;
                    while (lookIn != null && (entry == null || entry.sym == null)) {
                        entry = lookIn.members().lookup(addChangeListenerName);
                        while (entry != null && entry.sym != null) {
                            if (entry.sym.kind == Kinds.MTH) {
                                MethodSymbol msym = (MethodSymbol)entry.sym;
                                MethodType mtype = (MethodType)msym.type;
                                if(mtype != null && mtype.argtypes.length() == 1) {
                                    if (mtype.argtypes.head.tsym.flatName() == addChangeListenerFirstParamName) {
                                        tmpSelect.sym = msym;
                                        tmpSelect.type = mtype;
                                        break;
                                    }
                                }
                            }
                            
                            if (entry != null && entry.sym != null) {
                                entry = entry.sibling;
                            }
                        }
                        
                        lookIn = ((ClassSymbol)lookIn.type.tsym).getSuperclass().tsym;
                    }
***/

                    List<JCExpression> typeargs = List.nil();
                    List<JCExpression> args = List.nil();
                    args = args.append(anonymousChangeListener);
                    JCMethodInvocation tmpApply = make.Apply(typeargs, tmpSelect, args);

                    JCExpressionStatement addChangeListenerCall = make.Exec(tmpApply);
                    initializerBlock.stats = initializerBlock.stats.append(addChangeListenerCall);
                }
            }
        }
    }
    
    // Not used for now. We will use primitive fields if there are no bind for private fields.
    private JCLiteral getEmptyLiteral(Type type) {
        JCLiteral ret = null;
        if (type == null) {
            return ret;
        }
        
        if (type.isPrimitive()) {
            if (type.tag == TypeTags.BOOLEAN) {
                ret = make.Literal(TypeTags.BOOLEAN, Boolean.FALSE);
                ret.type = JavafxSymtab.booleanType;
                return ret;
            }
            else if (type.tag == TypeTags.BYTE) {
                ret = make.Literal(TypeTags.BYTE, Byte.valueOf((byte)0));
                ret.type = JavafxSymtab.byteType;
                return ret;
            }
            else if (type.tag == TypeTags.CHAR) {
                ret = make.Literal(TypeTags.CHAR, Character.valueOf((char)0));
                ret.type = JavafxSymtab.charType;
                return ret;
            }
            else if (type.tag == TypeTags.DOUBLE) {
                ret = make.Literal(TypeTags.DOUBLE, Double.valueOf(0.0));
                ret.type = JavafxSymtab.doubleType;
                return ret;
            }
            else if (type.tag == TypeTags.FLOAT) {
                ret = make.Literal(TypeTags.FLOAT, Float.valueOf(0.0f));
                ret.type = JavafxSymtab.floatType;
                return ret;
            }
            else if (type.tag == TypeTags.INT) {
                ret = make.Literal(TypeTags.INT, Integer.valueOf(0));
                ret.type = JavafxSymtab.intType;
                return ret;
            }
            else if (type.tag == TypeTags.LONG) {
                ret = make.Literal(TypeTags.LONG, Long.valueOf(0L));
                ret.type = JavafxSymtab.longType;
                return ret;
            }
            else if (type.tag == TypeTags.SHORT) {
                ret = make.Literal(TypeTags.SHORT, Short.valueOf((short)0));
                ret.type = JavafxSymtab.shortType;
                return ret;
            }
            else 
                return ret;
        }
        
        ret =  make.Literal(TypeTags.BOT, null);
        ret.type = type;
        return ret;
    }
 
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        super.visitInitDefinition(tree);
        
        List<JCVariableDecl> params = List.nil();
        // Removed Flags.SYNTHETIC
        JavafxJCMethodDecl res = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.TRIGGERNEW,
                     getInitSyntheticName(),
                     make.TypeIdent(TypeTags.VOID), params, tree.getBody());
         
        Type mtype = new MethodType(List.<Type>nil(),
                           syms.voidType,
                           List.<Type>nil(),
                           currentClassDef.sym);
        
        res.type = mtype;
        res.sym = new MethodSymbol(res.mods.flags, res.name, mtype, currentClassDef.sym);
        
        currentClassDef.defs = currentClassDef.defs.append(res);
        currentClassDef.sym.members().enterIfAbsent(res.sym);
        
        currentInitBlocks = currentInitBlocks.append(res);
   }
   
    private JCMethodDecl createInitializerMethod(JFXClassDeclaration classDecl) {
        if (classDecl != null) {
            List<JCVariableDecl> params = List.nil();

            List<JCStatement> initStats = List.nil();

            JCBlock initBodyBlock = make.Block(0L, initStats);
            JavafxJCMethodDecl jfxDeclInitializer = make.JavafxMethodDef(make.Modifiers(Flags.PUBLIC), 0,
                    initializerName,
                    make.TypeIdent(TypeTags.VOID), params, initBodyBlock);
            
            Type mtype = new MethodType(List.<Type>nil(),
                           syms.voidType,
                           List.<Type>nil(),
                           currentClassDef.sym);
        
            jfxDeclInitializer.type = mtype;
            jfxDeclInitializer.sym = new MethodSymbol(jfxDeclInitializer.mods.flags, jfxDeclInitializer.name, mtype, currentClassDef.sym);
            classDecl.sym.members().enterIfAbsent(jfxDeclInitializer.sym);

            classDecl.defs = classDecl.defs.prepend(jfxDeclInitializer);
            classDecl.initializer = jfxDeclInitializer;
            return jfxDeclInitializer;
        }
        
        return null;
    }
    
    private Name getInitSyntheticName() {
        return Name.fromString(names, initSyntheticName + currentInitCounter);
    }

    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        super.visitAttributeDefinition(tree);

        // Until we can allow access for initialization (including object literals) use workaround
        JCModifiers mods = tree.mods;
        if ((mods.flags & Flags.FINAL) != 0) mods = make.Modifiers(mods.flags & ~Flags.FINAL);
        
        // TODO: Do the same for javafx vars...
        if (tree.onChange != null) {
            List<JCTypeParameter> typarams = List.nil();
            List<JCTree> defs = List.nil();
            
            JCBlock ocBlock = tree.getOnChangeBlock();
            ocBlock.stats = ocBlock.stats.append(
                    make.at(tree.pos).Return(make.Literal(TypeTags.BOOLEAN, 1)));
            
            // changed() method
            defs = defs.append(make.MethodDef(make.Modifiers(Flags.PUBLIC),
                    changedName,
                    make.TypeIdent(TypeTags.BOOLEAN),
                    List.<JCTypeParameter>nil(),
                    List.<JCVariableDecl>nil(),
                    List.<JCExpression>nil(),
                    ocBlock,
                    null));

            JCNewClass jcNC = make.NewClass(null,
                    List.<JCExpression>nil(),
                    make.at(tree.pos).Identifier(changeListenerInterfaceName), 
                    List.<JCExpression>nil(),
                    make.at(tree.pos).AnonymousClassDef(make.Modifiers(0L), defs));

            currentChangeHelpers.put(tree, new ChangeProcessingHelper(jcNC));
        }
    }
    
    class ChangeProcessingHelper {
        JCNewClass newClass;
        
        ChangeProcessingHelper(JCNewClass newClass) {
            this.newClass = newClass;
        }
    }
}