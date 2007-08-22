/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxBindStatus;
import java.util.HashMap;
import java.util.Map;

public class Javafx2JavaTranslator extends JavafxTreeTranslator {
    protected static final Context.Key<Javafx2JavaTranslator> javafx2JavaTranslatorKey =
            new Context.Key<Javafx2JavaTranslator>();
    
    private final Name.Table names;
    private final JavafxTreeMaker make;
    private final JavafxSymtab syms;
    private final Log log;
    
    private final Name numberTypeName;
    private final Name integerTypeName;
    private final Name booleanTypeName;
    private final Name contextInterfaceName;
    private final Name locationName;
    final Name initializerName;
    final Name initializerBlockName;
    
    private final Name strongName;
    private final Name runtimeName;
    private final Name javafxName;
    private final Name sunName;
    private final Name comName;
    
    private final Name changedName;
    
    private JFXClassDeclaration currentClass = null;
    private JavafxJCClassDecl currentJCClass = null;
    
    private Map<Name, JFXClassDeclHelper> declClasses = null;
    private List<JFXTriggerDeclHelper> declTriggers = null;
    private List<JavafxJCMethodDecl> initBlocks = null;
    private List<JavafxJCMethodDecl> postprocessBlocks = null;
    private int nextNameNumber = 0;
    
    private static String syntheticNamePrfix = "$$synth$$";
    
    public static Javafx2JavaTranslator instance(Context context) {
        Javafx2JavaTranslator instance = context.get(javafx2JavaTranslatorKey);
        if (instance == null)
            instance = new Javafx2JavaTranslator(context);
        return instance;
    }
    
    public Javafx2JavaTranslator(Context context) {
        super();
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        names = Name.Table.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        log = Log.instance(context);
        
        // TODO: hack this should be in a central place. Add it to a Javafx subclass of PredefinedNames.
        numberTypeName  = names.fromString("Number");
        integerTypeName = names.fromString("Integer");
        booleanTypeName = names.fromString("Boolean");
        contextInterfaceName = names.fromString("com.sun.javafx.runtime.Context");
        locationName = names.fromString("com.sun.javafx.runtime.Location");
        initializerName = names.fromString("javafx$init$");
        initializerBlockName = names.fromString("javafx$init$block");
        
        strongName = names.fromString("StrongListener");
        runtimeName = names.fromString("runtime");
        javafxName = names.fromString("javafx");
        sunName = names.fromString("sun");
        comName = names.fromString("com");
        
        changedName = names.fromString("changed");
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        super.visitTopLevel(tree);
        enterTriggers();
        declClasses = null;
        declTriggers = null;
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        super.visitVar(tree);
        
        result = jcVarDeclFromJFXVar(tree);
    }
    
    @Override
    public void visitVarStatement(JFXVarStatement tree) {
        super.visitVarStatement(tree);
        
        result = jcVarDeclFromJFXVar(tree);
    }
    
    @Override
    public void visitVarInit(JFXVarInit tree) {
        super.visitVarInit(tree);
        
        result = make.JavafxVarDef(make.Modifiers(0),
                tree.getName(), JavafxFlags.VARIABLE,
                jcType(tree.getType()), tree.getInitializer(),
                tree.getBindStatus());
    }
    
    /**
     * For the module class
     */
    @Override
    public void visitClassDef(JCClassDecl tree) {
        tree.implementing = List.of(make.Identifier(contextInterfaceName));
        super.visitClassDef(tree);
        ListBuffer<JCTree> defs = ListBuffer.lb();
        for (JCTree def : tree.defs) {
            defs.append(def);
        }
        addEmptyContextMethods(defs);
        tree.defs = defs.toList();
        result = tree;
    }
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXClassDeclaration prevClass = currentClass;
        List<JavafxJCMethodDecl> prevInitBlocks = initBlocks;
        
        currentClass = tree;
        JavafxJCClassDecl prevJCClass = currentJCClass;
        initBlocks = null;
        try {
            List<JCTypeParameter> typeParams = List.nil();
            // TODO: Need resolved types so I can verify tree one Java class is extended only... Move the rest to interfaces...
            List<JCExpression> interfaces = List.of(make.Identifier(contextInterfaceName));
            interfaces.appendList(tree.getImplementedInterfaces());
            JCTree extending = null;
            if (tree.supertypes.length() > 0) {
                if (tree.supertypes.length() == 1) {
                    extending = tree.supertypes.head;
                } else {
                    log.error(tree.pos, "compiler.err.javafx.not.yet.implemented",
                              "multiple inheritance");
                }
            }

            ListBuffer<JCTree> defs = ListBuffer.lb();
            for (JCTree decl : tree.declarations) {
                defs.append(translate(decl));
            }
            addEmptyContextMethods(defs);
            JavafxJCClassDecl classDecl = make.JavafxJCClassDef(tree.mods, tree.name, 
                                            extending, interfaces, defs.toList(), tree.initializer);
            
            currentJCClass = classDecl;
            
            currentJCClass.setInitBlocks(initBlocks);
            currentJCClass.setPostprocessBlocks(postprocessBlocks);
            
            if (tree.initializer == null && classDecl != null) {
                List<JCVariableDecl> params = List.nil();

                List<JCStatement> initStats = List.nil();

                JCBlock initBodyBlock = make.Block(0L, initStats);
                JavafxJCMethodDecl jfxDeclInitializer = make.JavafxMethodDef(make.Modifiers(Flags.PUBLIC), 0,
                        initializerName,
                        make.TypeIdent(TypeTags.VOID), params, initBodyBlock);
                tree.initializer = jfxDeclInitializer;
                classDecl.defs = classDecl.defs.prepend(jfxDeclInitializer);
                classDecl.initializer = jfxDeclInitializer;
            }
            
            if (declClasses == null) {
                declClasses = new HashMap<Name, JFXClassDeclHelper>();
            }
            declClasses.put(tree.name, new JFXClassDeclHelper(tree, classDecl));
            result = classDecl;
        } finally {
            currentClass = prevClass;
            currentJCClass = prevJCClass;
            initBlocks = prevInitBlocks;
        }

        // prettyPrint(result);
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        super.visitAttributeDefinition(tree);
        
        JCExpression vartype = jcType(tree.getType());
        
        // Until we can allow access for initialization (including object literals) use workaround
        JCModifiers mods = tree.modifiers;
        if ((mods.flags & Flags.FINAL) != 0) mods = make.Modifiers(mods.flags & ~Flags.FINAL);
        
        JavafxJCVarDecl res = make.JavafxVarDef(mods, tree.name, JavafxFlags.ATTRIBUTE, vartype,
                    tree.getInitializer()==null? null : tree.getInitializer(),
                    tree.getBindStatus()==null? JavafxBindStatus.UNBOUND : tree.getBindStatus());
        
        // TODO: Do the same for javafx vars...
        if (tree.onChange != null) {
            List<JCTypeParameter> typarams = List.nil();
            List<JCExpression> implementing = List.nil();
            
            JCIdent comIdent = make.Ident(comName);
            JCFieldAccess sunSelect = make.Select(comIdent, sunName);
            JCFieldAccess javafxSelect = make.Select(sunSelect, javafxName);
            JCFieldAccess runtimeSelect = make.Select(javafxSelect, runtimeName);
            JCFieldAccess strongSelect = make.Select(runtimeSelect, strongName);
            
            List<JCTree> defs = List.nil();
            
            // changed() method
            defs = defs.append(make.MethodDef(make.Modifiers(Flags.PUBLIC),
                    changedName,
                    make.TypeIdent(TypeTags.VOID),
                    List.<JCTypeParameter>nil(),
                    List.<JCVariableDecl>nil(),
                    List.<JCExpression>nil(),
                    tree.getOnChangeBlock(),
                    null));

            JCClassDecl anonClass = make.ClassDef(make.Modifiers(0L),
                    names.empty, typarams, strongSelect, implementing, defs);
            
            JCIdent comIdent1 = make.Ident(comName);
            JCFieldAccess sunSelect1 = make.Select(comIdent1, sunName);
            JCFieldAccess javafxSelect1 = make.Select(sunSelect1, javafxName);
            JCFieldAccess runtimeSelect1 = make.Select(javafxSelect1, runtimeName);
            JCFieldAccess strongSelect1 = make.Select(runtimeSelect1, strongName);
            
            JCNewClass jcNC = make.NewClass(null,
                    List.<JCExpression>nil(),
                    strongSelect1,
                    List.<JCExpression>nil(),
                    anonClass);
            
            res.setChangeListener(jcNC);
        }
        
        result = res;
    }
    
    
    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        super.visitFunctionDefinition(tree);
        
        JCExpression restype = jcType(tree.getType());
        List<JCVariableDecl> params = buildParams(tree.params);
        JFXBlockExpression bodyExpression = tree.getBodyExpression();
        List<JCStatement> statements = bodyExpression.getStatements();
        if (bodyExpression.value != null) {
            // if the block expression has a value, convert it to a return statement
            ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
            stats.appendList(statements);
            stats.append(make.Return(bodyExpression.value));
            statements = stats.toList();
        }
        JCBlock block = make.Block(0L, statements);
         
        result = make.JavafxMethodDef(tree.modifiers, JavafxFlags.FUNCTION, tree.name,
                restype, params, block);
   }
    
    // TODO: In Attr need to enforce not to be able to do apply on this or pass this to apply.
    // This is according to initialization spec...
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        super.visitInitDefinition(tree);
        
        List<JCVariableDecl> params = List.nil();
        JavafxJCMethodDecl res = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.TRIGGERNEW,
                    getSyntheticName("init"),
                    make.TypeIdent(TypeTags.VOID), params, tree.getBody());
        
        if (initBlocks == null) {
            initBlocks = List.nil();
        }
        
        initBlocks = initBlocks.append(res);
        
        result = res;
   }
    
    // TODO: Parser to create JFXPostprocessDefinition... Processing very similar as above.
    
    @Override
    public void visitRetroAttributeDeclaration(JFXRetroAttributeDeclaration tree) {
        super.visitRetroAttributeDeclaration(tree);
        
        JCExpression vartype = jcType(tree.getType());
        JFXRetroAttributeDefinition definition = tree.retroDefinition;
        if (definition == null) {
            result = make.JavafxVarDef(make.Modifiers(0), tree.name, JavafxFlags.ATTRIBUTE, vartype,
                    null, JavafxBindStatus.UNBOUND);
        } else {
            result = make.JavafxVarDef(make.Modifiers(0),
                    tree.name, JavafxFlags.ATTRIBUTE, vartype,
                    definition.getInitializer()==null? null : translate(definition.getInitializer()),
                    definition.getBindStatus()==null? JavafxBindStatus.UNBOUND : definition.getBindStatus());
        }
    }
    
    @Override
    public void visitRetroFunctionDeclaration(JFXRetroFunctionMemberDeclaration tree) {
        super.visitRetroFunctionDeclaration(tree);
        
        JCExpression restype = jcType(tree.getType());
        List<JCVariableDecl> params = buildParams(tree.params);
        JFXRetroFunctionMemberDefinition definition = tree.retroDefinition;
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.name,
                restype, params, definition == null ? null : definition.body, 
                definition, tree);
    }
    
    @Override
    public void visitRetroOperationDeclaration(JFXRetroOperationMemberDeclaration tree) {
        super.visitRetroOperationDeclaration(tree);
        
        JCExpression restype = jcType(tree.getType());
        List<JCVariableDecl> params = buildParams(tree.params);
        JFXRetroOperationMemberDefinition definition = tree.retroDefinition;
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.name,
                restype, params, definition == null ? null : definition.body, 
                definition, tree);
    }
    
    @Override
    public void visitRetroAttributeDefinition(JFXRetroAttributeDefinition tree) {
        super.visitRetroAttributeDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitRetroFunctionDefinition(JFXRetroFunctionMemberDefinition tree) {
        super.visitRetroFunctionDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitRetroOperationDefinition(JFXRetroOperationMemberDefinition tree) {
        super.visitRetroOperationDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitRetroOperationLocalDefinition(JFXRetroOperationLocalDefinition tree) {
        super.visitRetroOperationLocalDefinition(tree);
        
        JCExpression restype = jcType(tree.getType());
        
        List<JCVariableDecl> params = buildParams(tree.params);
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, tree.getBody(), tree, null);
    }
    
    @Override
    public void visitRetroFunctionLocalDefinition(JFXRetroFunctionLocalDefinition tree) {
        super.visitRetroFunctionLocalDefinition(tree);
        
        JCExpression restype = jcType(tree.getType());
        
        List<JCVariableDecl> params = buildParams(tree.params);
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, tree.getBody(), tree, null);
    }
    
    @Override
    public void visitStringExpression(JFXStringExpression tree) {
       StringBuffer sb = new StringBuffer();
        List<JCExpression> parts = tree.getParts();
        ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();
        
        JCLiteral lit = (JCLiteral)(parts.head);            // "...{
        sb.append((String)lit.value);            
        parts = parts.tail;
        
        while (parts.nonEmpty()) {

            lit = (JCLiteral)(parts.head);                  // optional format (or null)
            sb.append(lit==null? "%s" : (String)lit.value);
            parts = parts.tail;
            
            values.append(translate(parts.head));           // expression
            parts = parts.tail;
            
            lit = (JCLiteral)(parts.head);                  // }...{  or  }..."
            sb.append((String)lit.value);
            parts = parts.tail;
        }
        JCLiteral formatLiteral = make.at(tree.pos).Literal(TypeTags.CLASS, sb.toString());
        JCExpression formatter = make.Ident(Name.fromString(names, "java"));
        for (String s : new String[] {"lang", "String", "format"}) {
            formatter = make.Select(formatter, Name.fromString(names, s));
        }
        values.prepend(formatLiteral);
        result = make.Apply(null, formatter, values.toList());
    }
    
    @Override
    public void visitPureObjectLiteral(JFXPureObjectLiteral tree) {
        Name tmpName = getSyntheticName("objlit");
        JCExpression clazz = translate(tree.getIdentifier());
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
        JavafxJCNewClassObjectLiteral javafxNewClassObjLit = 
                make.NewClassObjectLiteral(null, null, clazz, List.<JCExpression>nil(), null);
        JCVariableDecl tmpVar = make.VarDef(make.Modifiers(0), tmpName, clazz, javafxNewClassObjLit);
        stats.append(tmpVar);
        JCStatement lastStatement = null;
        for (JFXStatement part : tree.getParts()) {
            if (part instanceof JFXObjectLiteralPart) {
                JFXObjectLiteralPart olpart = (JFXObjectLiteralPart)part;
                JCFieldAccess attr = make.Select(
                        make.Ident(tmpName),
                        olpart.getName());
                lastStatement = make.Exec( make.JavafxAssign(
                        attr,
                        translate(olpart.getExpression()),
                        olpart.getBindStatus()));
                stats.append(lastStatement);
                
            } else {
                log.error(tree.pos, "compiler.err.javafx.not.yet.implemented",
                        part.getClass().getName() + " in object literal");
            }
        }
        
        List<JCExpression> typeargs = List.nil();
        List<JCExpression> args = List.nil();
        
        stats.append(make.Exec(make.Apply(typeargs, 
                make.Select(make.Ident(tmpName), initializerName), args)));
         
        result = make.BlockExpression(0, stats.toList(), make.Ident(tmpName));
    }
    
    @Override
    public void visitTriggerOnNew(JFXTriggerOnNew tree) {
        super.visitTriggerOnNew(tree);
        
        if (declTriggers == null) {
            declTriggers = List.nil();
        }
        
        if (tree.getBlock() == null) {
            log.error(tree.pos, "javafx.trigger.with.no.body", tree.getNewValueIdentifier().toString());
        } else {
            List<JCVariableDecl> params = List.nil();
            JavafxJCMethodDecl triggerDecl = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.TRIGGERNEW,
                    getSyntheticName(tree.getClassIdentifier().toString()),
                    make.TypeIdent(TypeTags.VOID), params, tree.getBlock(),
                    tree, null);
            if (declTriggers == null) {
                declTriggers = List.nil();
            }
            declTriggers = declTriggers.append(new JFXTriggerDeclHelper(tree, triggerDecl));
            result = null;
        }
    }
    
    @Override
    public void visitTriggerOnReplace(JFXTriggerOnReplace tree) {
        super.visitTriggerOnReplace(tree);
        
        if (declTriggers == null) {
            declTriggers = List.nil();
        }
        
        if (tree.getBlock() == null) {
            log.error(tree.pos, "javafx.trigger.with.no.body", tree.getNewValueIdentifier().toString());
        } else {
            List<JCVariableDecl> params = List.nil();
            JavafxJCMethodDecl triggerDecl = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.TRIGGERREPLACE,
                    getSyntheticName(tree.selector.getClassName().toString()),
                    make.TypeIdent(TypeTags.VOID), params, tree.getBlock(),
                    tree, null);
            if (declTriggers == null) {
                declTriggers = List.nil();
            }
            declTriggers = declTriggers.append(new JFXTriggerDeclHelper(tree, triggerDecl));
            result = null;
        }
    }
    
    /**
     * Allow prepending of statements and/or deletin by translation to null
     */
    @Override
    public void visitBlock(JCBlock tree) {
        List<JCStatement> stats = tree.stats;
        if (stats != null)  {
            List<JCStatement> prev = null;
            for (List<JCStatement> l = stats; l.nonEmpty(); l = l.tail) {
                JCStatement trans = translate(l.head);
                if (trans == null) {
                    // This statement has translated to nothing, remove it from the list
                    prev.tail = l.tail;
                    l = prev;
                    continue;
                }
                l.head = trans;
                prev = l;
            }
            tree.stats = stats;
        }
        result = tree;
    }

    private List<JCVariableDecl> buildParams(List<JCTree> fxparams) {
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : fxparams) {
            if (var.getTag() == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.getTag() == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXFunctionMemberDeclaration parameter list.");
            }
        }
        return params;
    }
    
    private void addEmptyContextMethods(ListBuffer<JCTree> defs) {
        List<JCVariableDecl> params;
        params = List.of(
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("bound"), make.Identifier(locationName), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("exprNum"), make.TypeIdent(TypeTags.INT), null)
                );
        defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                names.fromString("apply0"),
                make.TypeIdent(TypeTags.BOOLEAN),
                List.<JCTypeParameter>nil(),
                params,
                List.<JCExpression>nil(),
                make.Block(0, List.<JCStatement>of(make.Return(make.Literal(TypeTags.BOOLEAN, 0)))),
                null));
        params = List.of(
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("bound"), make.Identifier(locationName), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("exprNum"), make.TypeIdent(TypeTags.INT), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("arg1"), make.Identifier(locationName), null)
                );
        defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                names.fromString("apply1"),
                make.TypeIdent(TypeTags.BOOLEAN),
                List.<JCTypeParameter>nil(),
                params,
                List.<JCExpression>nil(),
                make.Block(0, List.<JCStatement>of(make.Return(make.Literal(TypeTags.BOOLEAN, 0)))),
                null));
        params = List.of(
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("bound"), make.Identifier(locationName), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("exprNum"), make.TypeIdent(TypeTags.INT), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("arg1"), make.Identifier(locationName), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("arg2"), make.Identifier(locationName), null)
                );
        defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                names.fromString("apply2"),
                make.TypeIdent(TypeTags.BOOLEAN),
                List.<JCTypeParameter>nil(),
                params,
                List.<JCExpression>nil(),
                make.Block(0, List.<JCStatement>of(make.Return(make.Literal(TypeTags.BOOLEAN, 0)))),
                null));
        params = List.of(
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("bound"), make.Identifier(locationName), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("exprNum"), make.TypeIdent(TypeTags.INT), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("args"), make.TypeArray(make.Identifier(locationName)), null)
                );
        defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                names.fromString("applyN"),
                make.TypeIdent(TypeTags.BOOLEAN),
                List.<JCTypeParameter>nil(),
                params,
                List.<JCExpression>nil(),
                make.Block(0, List.<JCStatement>of(make.Return(make.Literal(TypeTags.BOOLEAN, 0)))),
                null));
    }
    
    private JCExpression jcType(JFXType jfxType) {
        JCExpression type = null;
        
        if (jfxType != null) {
            if (jfxType instanceof JFXTypeClass) {
                Name className = ((JFXTypeClass)jfxType).getClassName();
                if (className == numberTypeName) {
                    type = make.TypeIdent(TypeTags.DOUBLE);
                    type.type = syms.javafx_NumberType;
                } else if (className == integerTypeName) {
                    type = make.TypeIdent(TypeTags.INT);
                    type.type = syms.javafx_IntegerType;
                } else if (className == booleanTypeName) {
                    type = make.TypeIdent(TypeTags.BOOLEAN);
                    type.type = syms.javafx_BooleanType;
                } else {
                    type = make.Ident(className);
                }
            } else {
                // TODO: Figure out what this could be???
                throw new Error("Unexpected instanceof for JFXVar.getType()!!!");
            }
        } else {
            type = make.TypeIdent(TypeTags.NONE);
            type.type = syms.javafx_AnyType;
        }
        
        return type;
    }
    
    private JavafxJCVarDecl jcVarDeclFromJFXVar(JFXVar tree) {
        JCModifiers mods = tree.getModifiers();
        if (mods == null) {
            mods = make.Modifiers(0);
        }
        return make.JavafxVarDef(mods, tree.getName(),
                JavafxFlags.VARIABLE, jcType(tree.getType()),
                null, JavafxBindStatus.UNBOUND);
    }
    
    
    private void enterTriggers() {
        if (declTriggers != null) {
            for (JFXTriggerDeclHelper triggerDecl : declTriggers) {
                if (triggerDecl.jfxDecl.getTag() == JavafxTag.TRIGGERONNEW) {
                    if (triggerDecl.javafxDecl.getJavafxMethodType() == JavafxFlags.TRIGGERNEW) {
                        JFXTriggerOnNew trigger = (JFXTriggerOnNew)triggerDecl.jfxDecl;
                        JCTree classIdent = trigger.getClassIdentifier();
                        while (classIdent != null && classIdent.getTag() != JCTree.IDENT) {
                            if (classIdent.getTag() == JCTree.SELECT) {
                                classIdent = ((JCFieldAccess)classIdent).selected;
                            } else {
                                throw new Error("Unexpected tree type in Trigged On"); // TODO: Remove this when figure out what can be in here
                            }
                        }
                        
                        if (classIdent == null || classIdent.getTag() != JCTree.IDENT) {
                            throw new Error("Have to have a valid ident in here!"); // TODO: Remove when cleaned out...
                        }
                        JCIdent classNameIdent = (JCIdent)classIdent;
                        JFXClassDeclHelper classHelper = declClasses.get(classNameIdent.name);
                        
                        if (classHelper == null) {
                            log.error(classNameIdent.pos, "javafx.trigger.with.no.owner", classNameIdent.name.toString());
                            return;
                        }
                        
                        JCBlock initBodyBlock = null;
                        
                        assert classHelper.jfxDecl.initializer != null : "initializer1 must be set!";
                        initBodyBlock = classHelper.jfxDecl.initializer.body;
                        
                        assert initBodyBlock != null : "initBodyBlock must not be null!";
                        
                        List<JCExpression> typeArgs = List.nil();
                        List<JCExpression> args = List.nil();
                        initBodyBlock.stats = initBodyBlock.stats.append(make.Exec(
                                make.Apply(typeArgs, make.Ident(triggerDecl.javafxDecl.name), args)));
                        classHelper.jcDecl.defs = classHelper.jcDecl.defs.append(triggerDecl.javafxDecl);
                    }
                    // TODO: More triggers functionality here...
                }
                // ReplaceTrigger
                else if (triggerDecl.jfxDecl.getTag() == JavafxTag.TRIGGERONREPLACE) {
                    if (triggerDecl.javafxDecl.getJavafxMethodType() == JavafxFlags.TRIGGERREPLACE) {
                        JFXTriggerOnReplace trigger = (JFXTriggerOnReplace)triggerDecl.jfxDecl;
                        JCTree classIdent = trigger.selector;
                        if (classIdent != null && classIdent.getTag() != JavafxTag.MEMBERSELECTOR) {
                            throw new Error("Unexpected tree type in Trigged On"); // TODO: Remove this when figure out what can be in here
                        }
                        
                        if (classIdent == null || classIdent.getTag() != JavafxTag.MEMBERSELECTOR) {
                            throw new Error("Have to have a valid ident in here!"); // TODO: Remove when cleaned out...
                        }
                        Name className = ((JFXMemberSelector)classIdent).getClassName();
                        JFXClassDeclHelper classHelper = declClasses.get(className);
                        
                        if (classHelper == null) {
                            log.error(classIdent.pos, "javafx.trigger.with.no.owner", className.toString());
                            return;
                        }
                        
                        // TODO: check the attribute... Note this should be done in attr... We need to know the type of the attribute, so we can generate the correct method
                        JCBlock initBodyBlock = null;
                        assert classHelper.jfxDecl.initializer != null : "initializer1 must be set!";
                        initBodyBlock = classHelper.jfxDecl.initializer.body;
                        
                        assert initBodyBlock != null : "initBodyBlock must not be null!";
                        
                        List<JCExpression> typeArgs = List.nil();
                        List<JCExpression> args = List.nil();
                        initBodyBlock.stats = initBodyBlock.stats.append(make.Exec(
                                make.Apply(typeArgs, make.Ident(triggerDecl.javafxDecl.name), args))); // Move from here into the initializer.
                        classHelper.jcDecl.defs = classHelper.jcDecl.defs.append(triggerDecl.javafxDecl);
                    }
                }
                // TODO: More triggers functionality here...
            }
        }
    }
    
    private Name getSyntheticName(String str) {
        return Name.fromString(names, Javafx2JavaTranslator.syntheticNamePrfix + str + nextNameNumber ++);
    }
    
    static class JFXClassDeclHelper {
        JFXClassDeclaration jfxDecl;
        JCClassDecl jcDecl;
        JFXClassDeclHelper(JFXClassDeclaration jfxDecl, JCClassDecl jcDecl) {
            this.jfxDecl = jfxDecl;
            this.jcDecl = jcDecl;
        }
    }
    
    static class JFXTriggerDeclHelper {
        JFXAbstractTriggerOn jfxDecl;
        JavafxJCMethodDecl javafxDecl;
        JFXTriggerDeclHelper(JFXAbstractTriggerOn jfxDecl,JavafxJCMethodDecl javafxDecl) {
            this.jfxDecl = jfxDecl;
            this.javafxDecl = javafxDecl;
        }
    }
}
