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

// For pretty print debugging
import java.io.OutputStreamWriter;


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
    
    private JFXClassDeclaration currentClass = null;
    
    private Map<Name, JFXClassDeclHelper> declClasses = null;
    private List<JFXTriggerDeclHelper> declTriggers = null;
    private int nextNameNumber = 0;
    
    private static String syntheticNamePrfix = "$$synth$$";
    
    private ListBuffer<JCStatement> prependInFrontOfStatement = null;
    
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
                tree.getBindStatus(), null, tree);
    }
    
    /**
     * For the module class
     */
    @Override
    public void visitClassDef(JCClassDecl tree) {
        tree.implementing = List.<JCExpression>of(make.Identifier(contextInterfaceName));
        super.visitClassDef(tree);
        ListBuffer<JCTree> defs = ListBuffer.<JCTree>lb();
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
        currentClass = tree;
        try {
            List<JCTypeParameter> typeParams = List.nil();
            // TODO: Need resolved types so I can verify tree one Java class is extended only... Move the rest to interfaces...
            // The supertypes should not be names, but trees.
            List<JCExpression> interfaces = List.<JCExpression>of(make.Identifier(contextInterfaceName));
            for (Name name : tree.supertypes) {
                interfaces = interfaces.append(make.Ident(name));
            }
            
            ListBuffer<JCTree> defs = ListBuffer.<JCTree>lb();
            for (JFXMemberDeclaration decl : tree.declarations) {
                defs.append(translate(decl));
            }
            addEmptyContextMethods(defs);
            JCClassDecl classDecl = make.ClassDef(tree.mods, tree.name, typeParams, null, interfaces, defs.toList());
            if (declClasses == null) {
                declClasses = new HashMap<Name, JFXClassDeclHelper>();
            }
            declClasses.put(tree.name, new JFXClassDeclHelper(tree, classDecl));
            result = classDecl;
        } finally {
            currentClass = prevClass;
        }
        /*****
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.printExpr(result);
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Pretty print got: " + ex);
        }
        ******/
    }
    
    @Override
    public void visitAttributeDeclaration(JFXAttributeDeclaration tree) {
        super.visitAttributeDeclaration(tree);
        
        JCExpression vartype = jcType(tree.getType());
        JFXAttributeDefinition definition = (JFXAttributeDefinition)tree.definition;
        if (definition == null) {
            result = make.JavafxVarDef(make.Modifiers(0), tree.getName(), JavafxFlags.ATTRIBUTE, vartype,
                    null, JavafxBindStatus.UNBOUND, definition, tree);
        } else {
            result = make.JavafxVarDef(make.Modifiers(0),
                    tree.getName(), JavafxFlags.ATTRIBUTE, vartype,
                    translate(definition.getInitializer()),
                    definition.getBindStatus(), definition, tree);
        }
    }
    
    @Override
    public void visitFunctionDeclaration(JFXFunctionMemberDeclaration tree) {
        super.visitFunctionDeclaration(tree);
        
        JCExpression restype = jcType(tree.getType());
        List<JFXExpression> capturedOuters = List.nil();
        
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : tree.params) {
            if (var.tag == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.tag == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXFunctionMemberDeclaration parameter list.");
            }
        }
        
        JFXFunctionMemberDefinition definition = (JFXFunctionMemberDefinition)tree.definition;
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, definition == null ? null : definition.body, null,
                capturedOuters, definition, tree);
    }
    
    @Override
    public void visitOperationDeclaration(JFXOperationMemberDeclaration tree) {
        super.visitOperationDeclaration(tree);
        
        JCExpression restype = jcType(tree.getType());
        List<JFXExpression> capturedOuters = List.nil();
        
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : tree.params) {
            if (var.tag == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.tag == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXOperationMemberDeclaration parameter list.");
            }
        }
        
        JFXOperationMemberDefinition definition = (JFXOperationMemberDefinition)tree.definition;
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, definition == null ? null : definition.body, null,
                capturedOuters, definition, tree);
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        super.visitAttributeDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitFunctionDefinition(JFXFunctionMemberDefinition tree) {
        super.visitFunctionDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationMemberDefinition tree) {
        super.visitOperationDefinition(tree);
        result = null;
    }
    
    @Override
    public void visitOperationLocalDefinition(JFXOperationLocalDefinition tree) {
        super.visitOperationLocalDefinition(tree);
        
        JCExpression restype = jcType(tree.getType());
        
        List<JFXExpression> boundFrom = List.nil();
        List<JFXExpression> boundTo = List.nil();
        List<JFXExpression> capturedOuters = List.nil();
        
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : tree.params) {
            if (var.tag == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.tag == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXOperationLocalDefinition parameter list.");
            }
        }
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, tree.getBody(), null, capturedOuters, tree, null);
    }
    
    @Override
    public void visitFunctionLocalDefinition(JFXFunctionLocalDefinition tree) {
        super.visitFunctionLocalDefinition(tree);
        
        JCExpression restype = jcType(tree.getType());
        
        List<JFXExpression> boundFrom = List.nil();
        List<JFXExpression> boundTo = List.nil();
        List<JFXExpression> capturedOuters = List.nil();
        
        List<JCVariableDecl> params = List.nil();
        for (JCTree var : tree.params) {
            if (var.tag == JavafxTag.VARDECL) {
                params = params.append(jcVarDeclFromJFXVar((JFXVar)var));
            } else if (var.tag == JCTree.VARDEF) {
                params = params.append((JCVariableDecl)var);
            } else {
                throw new AssertionError("Unexpected tree in the JFXFunctionLocalDefinition parameter list.");
            }
        }
        
        result = make.JavafxMethodDef(make.Modifiers(0), JavafxFlags.FUNCTION, tree.getName(),
                restype, params, tree.getBody(), null, capturedOuters, tree, null);
    }
    
    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        super.visitStringExpression(tree);
        StringBuffer sb = new StringBuffer();
        List<JCExpression> parts = tree.getParts();
        ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();
        JCLiteral lit = (JCLiteral)(parts.head);
        sb.append((String)lit.value);
        parts = parts.tail;
        while (parts.nonEmpty()) {
            // TODO: fix me -- format part missing
            lit = null; //lit = (JCLiteral)(parts.head);
                        //parts = parts.tail;
            sb.append(lit==null? "%s" : (String)lit.value);
            values.append(translate(parts.head));
            parts = parts.tail;
            lit = (JCLiteral)(parts.head);
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
        if (prependInFrontOfStatement == null) {
            prependInFrontOfStatement = new ListBuffer<JCStatement>();
        }
        prependInFrontOfStatement.append(make.VarDef(make.Modifiers(0), tmpName, clazz,
                make.NewClass(null, null, clazz, com.sun.tools.javac.util.List.<com.sun.tools.javac.tree.JCTree.JCExpression>nil(), null)));
        for (JFXStatement part : tree.getParts()) {
            if (part instanceof JFXObjectLiteralPart) {
                JFXObjectLiteralPart olpart = (JFXObjectLiteralPart)part;
                JCFieldAccess attr = make.Select(
                        make.Ident(tmpName),
                        olpart.getName());
                prependInFrontOfStatement.append( make.Exec( make.JavafxAssign(
                        attr,
                        translate(olpart.getExpression()),
                        olpart.getBindStatus())));
                
            } else {
                log.error(tree.pos, "compiler.err.javafx.not.yet.implemented",
                        part.getClass().getName() + " in object literal");
            }
        }
        result = make.Ident(tmpName);
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
                    make.TypeIdent(TypeTags.VOID), params, tree.getBlock(), null,
                    null, tree, null);
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
    public void visitBlock(JCBlock tree) {
        List<JCStatement> stats = tree.stats;
        if (stats != null)  {
            List<JCStatement> prev = null;
            for (List<JCStatement> l = stats; l.nonEmpty(); l = l.tail) {
                // translate must occur immediately before prependInFrontOfStatement check
                JCStatement trans = translate(l.head);
                if (trans == null) {
                    // This statement has translated to nothing, remove it from the list
                    prev.tail = l.tail;
                    l = prev;
                    continue;
                }
                if (prependInFrontOfStatement != null) {
                    List<JCStatement> pl = prependInFrontOfStatement.toList();
                    List<JCStatement> last = prependInFrontOfStatement.last;
                    // attach remainder of list to the prepended statements
                    for (List<JCStatement> al = pl; ; al = al.tail) {
                        if (al.tail == last) {
                            al.tail = l;
                            break;
                        }
                    }
                    // attach prepended statement to previous part of list
                    if (prev == null) {
                        stats = pl;
                    } else {
                        prev.tail = pl;
                    }
                    prependInFrontOfStatement = null;
                }
                l.head = trans;
                prev = l;
            }
            tree.stats = stats;
        }
        result = tree;
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
        return make.JavafxVarDef(make.Modifiers(0),  tree.getName(),
                JavafxFlags.VARIABLE, jcType(tree.getType()),
                null, JavafxBindStatus.UNBOUND, null, tree);
    }
    
    
    private void enterTriggers() {
        if (declTriggers != null) {
            for (JFXTriggerDeclHelper triggerDecl : declTriggers) {
                if (triggerDecl.jfxDecl.tag == JavafxTag.TRIGGERONNEW) {
                    if (triggerDecl.javafxDecl.getJavafxMethodType() == JavafxFlags.TRIGGERNEW) {
                        JFXTriggerOnNew trigger = (JFXTriggerOnNew)triggerDecl.jfxDecl;
                        JCTree classIdent = trigger.getClassIdentifier();
                        while (classIdent != null && classIdent.tag != JCTree.IDENT) {
                            if (classIdent.tag == JCTree.SELECT) {
                                classIdent = ((JCFieldAccess)classIdent).selected;
                            } else {
                                throw new Error("Unexpected tree type in Trigged On"); // TODO: Remove this when figure out what can be in here
                            }
                        }
                        
                        if (classIdent == null || classIdent.tag != JCTree.IDENT) {
                            throw new Error("Have to have a valid ident in here!"); // TODO: Remove when cleaned out...
                        }
                        JCIdent classNameIdent = (JCIdent)classIdent;
                        JFXClassDeclHelper classHelper = declClasses.get(classNameIdent.name);
                        
                        if (classHelper == null) {
                            log.error(classNameIdent.pos, "javafx.trigger.with.no.owner", classNameIdent.name.toString());
                            return;
                        }
                        
                        JCBlock ctorBodyBlock = null;
                        if (classHelper.jfxDecl.constructor == null) {
                            List<JCVariableDecl> params = List.nil();
                            
                            List<JCStatement> ctorStats = List.nil();
                            
                            ctorBodyBlock = make.Block(0L, ctorStats);
                            JavafxJCMethodDecl jfxDeclConstructor = make.JavafxMethodDef(make.Modifiers(Flags.PUBLIC), 0,
                                    names.init,
                                    make.TypeIdent(TypeTags.VOID), params, ctorBodyBlock, null, null, null, null);
                            classHelper.jfxDecl.constructor = jfxDeclConstructor;
                            classHelper.jcDecl.defs = classHelper.jcDecl.defs.prepend(jfxDeclConstructor);
                        } else {
                            ctorBodyBlock = classHelper.jfxDecl.constructor.body;
                        }
                        
                        assert ctorBodyBlock != null : "ctorBodyBlock must not be null!";
                        
                        List<JCExpression> typeArgs = List.nil();
                        List<JCExpression> args = List.nil();
                        ctorBodyBlock.stats = ctorBodyBlock.stats.append(make.Exec(
                                make.Apply(typeArgs, make.Ident(triggerDecl.javafxDecl.name), args)));
                        classHelper.jcDecl.defs = classHelper.jcDecl.defs.append(triggerDecl.javafxDecl);
                    }
                    // TODO: More triggers functionality here...
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
