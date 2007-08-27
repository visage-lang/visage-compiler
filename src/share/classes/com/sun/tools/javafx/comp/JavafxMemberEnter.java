/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;

import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import java.util.Iterator;

import static com.sun.tools.javac.tree.JCTree.*;

/** This is the second phase of Enter, in which classes are completed
 *  by entering their members into the class scope using
 *  MemberEnter.complete().  See Enter for an overview.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("@(#)JavafxMemberEnter.java	1.71 07/05/05")
public class JavafxMemberEnter extends MemberEnter {
    
    private JavafxSymtab syms;
    private boolean isInMethodParamVars;
    private boolean isVarArgs;
    private JCMethodDecl currentMethodDecl = null;
    private List<MethodInferTypeHelper> methodsToInferReturnType;
    private Type methodReturnType;
    private Env<AttrContext> localEnv;
    
    public static MemberEnter instance0(Context context) {
        MemberEnter instance = context.get(memberEnterKey);
        if (instance == null)
            instance = new JavafxMemberEnter(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(memberEnterKey, new Context.Factory<MemberEnter>() {
	       public MemberEnter make() {
		   return new JavafxMemberEnter(context);
	       }
        });
    }

    protected JavafxMemberEnter(Context context) {
        super(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    @Override
    public void visitBlock(JCBlock block) {
        for (JCStatement tree : block.stats) {
            tree.accept(this);
        }
    }
 
    public void visitBlockExpression(JFXBlockExpression tree) {
        for (JCStatement stmt : tree.stats) {
            stmt.accept(this);
        }
        tree.value.accept(this);
    }
    
    @Override
    public void visitTree(JCTree tree) {
        if (tree instanceof JFXBlockExpression)
            visitBlockExpression((JFXBlockExpression) tree);
        else
            super.visitTree(tree);
    }

    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        if (tree.starImportScope.elems != null) {
            // we must have already processed this toplevel
            return;
        }

        // check that no class exists with same fully qualified name as
        // toplevel package
        if (checkClash && tree.pid != null) {
            Symbol p = tree.packge;
            while (p.owner != syms.rootPackage) {
                p.owner.complete(); // enter all class members of p
                if (syms.classes.get(p.getQualifiedName()) != null) {
                    log.error(tree.pos,
                              "pkg.clashes.with.class.of.same.name",
                              p);
                }
                p = p.owner;
            }
        }

        // process package annotations
        annotateLater(tree.packageAnnotations, env, tree.packge);

        // Import-on-demand the JavaFX types 
        importNamed(tree.pos(), syms.javafx_StringType.tsym, env);
        importNamed(tree.pos(), syms.javafx_IntegerType.tsym, env);
        importNamed(tree.pos(), syms.javafx_BooleanType.tsym, env);
        importNamed(tree.pos(), syms.javafx_NumberType.tsym, env);

        // Process all import clauses.
        memberEnter(tree.defs, env);
    }

    @Override
    public void visitImport(JCImport tree) {
        if (!tree.isStatic()) {
            if (tree.qualid.getTag() == SELECT) {
                if (((JCFieldAccess)tree.qualid).name == names.fromString("Integer")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.integer.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("Number")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.number.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("Boolean")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.boolean.primitive.type");
                }
                else if (((JCFieldAccess)tree.qualid).name == names.fromString("String")) { // TODO: use the constant in the new NameTable when available.
                    log.error(tree.pos, "javafx.can.not.import.string.primitive.type");
                }
            }
        }
        
        super.visitImport(tree);
    }
    
    @Override
    protected void finishClass(JCClassDecl tree, Env<AttrContext> env) {
        List<MethodInferTypeHelper> prevMethodsToInferReturnType;
        prevMethodsToInferReturnType = methodsToInferReturnType;
        methodsToInferReturnType = List.nil();
        super.finishClass(tree, env);
        inferMethodReturnTypes();
        methodsToInferReturnType = prevMethodsToInferReturnType;
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        Env<AttrContext> localEnv = env;
        if ((tree.mods.flags & STATIC) != 0 ||
            (env.info.scope.owner.flags() & INTERFACE) != 0) {
            localEnv = env.dup(tree, env.info.dup());
            localEnv.info.staticLevel++;
        }
// Javafx change
        if (tree.vartype == null) {
            tree.vartype = make.TypeIdent(CLASS);
            tree.vartype.type = syms.javafx_AnyType;
        }
        
        if (tree.vartype.type != syms.javafx_AnyType) {
// Javafx change
            attr.attribType(tree.vartype, localEnv);
// Javafx change
        }
// Javafx change
        Scope enclScope = enter.enterScope(env);
        VarSymbol v;
        if (tree instanceof JavafxJCVarDecl) {
            JavafxJCVarDecl decl = (JavafxJCVarDecl)tree;
            v = new JavafxVarSymbol(0, tree.name, decl.getJavafxVarType(), tree.vartype.type, 
                    decl.isBound(), decl.isLazy(), enclScope.owner);
        } else {
            v = new VarSymbol(0, tree.name, tree.vartype.type, enclScope.owner);
        }

        v.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, v, tree);
        tree.sym = v;
        if (tree.init != null) {
            v.flags_field |= HASINIT;
            if ((v.flags_field & FINAL) != 0 && tree.init.getTag() != JCTree.NEWCLASS)
                v.setLazyConstValue(initEnv(tree, env), log, attr, tree.init);
// Javafx change
        if (tree.vartype.type == syms.javafx_AnyType) {
            Env<AttrContext> initEnv = initEnv(tree, env);
            tree.vartype.type = attr.attribExpr(tree.init, initEnv, Type.noType);
            tree.sym.type = tree.vartype.type;
        }
// Javafx change

        }
        if (chk.checkUnique(tree.pos(), v, enclScope)) {
            chk.checkTransparentVar(tree.pos(), v, enclScope);
            enclScope.enter(v);
        }
        annotateLater(tree.mods.annotations, localEnv, v);
        v.pos = tree.pos;
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
        JCMethodDecl prevMethodDecl = currentMethodDecl;
        currentMethodDecl = tree;
        try {
            Scope enclScope = enter.enterScope(env);
            MethodSymbol m = new MethodSymbol(0, tree.name, null, enclScope.owner);
            m.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, m, tree);
            tree.sym = m;
            Env<AttrContext> localEnv = methodEnv(tree, env);

            m.type = attrMethodType(tree, localEnv);
        
// Javafx modification
            // If types were not set, set them to syms.javafx_AnyType
            // TODO: Can we do some type inference in here?
            if (m != null && m.type != null && ((MethodType)m.type).argtypes != null) {
                for (List<Type> mTypes = ((MethodType)m.type).argtypes; mTypes.nonEmpty(); mTypes = mTypes.tail) {
                    if (mTypes.head == null) {
                        mTypes.head = syms.javafx_AnyType;
                    } 
                }
            }
// Javafx modification

            // mark the method varargs, if necessary
            if (isVarArgs)
                m.flags_field |= Flags.VARARGS;

            localEnv.info.scope.leave();
            if (chk.checkUnique(tree.pos(), m, enclScope)) {
                enclScope.enter(m);
            }
            annotateLater(tree.mods.annotations, localEnv, m);
            if (tree.defaultValue != null)
                annotateDefaultValueLater(tree.defaultValue, localEnv, m);
        }
        finally {
            currentMethodDecl = prevMethodDecl;
        }
    }

    @Override
    public void visitReturn(JCReturn tree) {
        super.visitReturn(tree);
        if (localEnv != null) {
            attr.attribStat(tree, localEnv);
            if (tree.expr == null) {
                methodReturnType = Symtab.voidType;
            }
            else {
                methodReturnType = tree.expr.type;
            }
        }
    }
// Javafx modification    
    private List<Type> checkParameterTypes(List<JCTree> defParams, List<JCTree> declParams, JavafxJCMethodDecl methodDecl) {
        isVarArgs = false;
        ListBuffer<Type> ret = new ListBuffer<Type>();
        if (declParams == null || defParams == null || declParams.size() != defParams.size()) {
            return null;
        }
        
        Iterator<JCTree> defParamIter = defParams.iterator();
        Iterator<JCTree> declParamIter = declParams.iterator();
        
        while (defParamIter.hasNext()) {
            JCTree defTree = defParamIter.next();
            JCTree declTree = declParamIter.next();
            
            if (defTree.type == null) {
                defTree.type = syms.javafx_AnyType;
            }
            
            if (declTree.type == null) {
                declTree.type = syms.javafx_AnyType;
            }

            Symbol defTypeSym = defTree.type.tsym;
            Symbol declTypeSym = declTree.type.tsym;
            
            if (defTypeSym == declTypeSym) {
                ret.append(defTree.type);
                continue;
            }
            else if (defTree.type == syms.javafx_AnyType) {
                defTree.type = declTree.type;
                ret.append(defTree.type);
            }
            else if (declTree.type == syms.javafx_AnyType) {
                declTree.type = defTree.type;
                ret.append(declTree.type);
            }
            else {
                log.error(methodDecl.pos, "javafx.different.types.of.parameters.for.definition.and.declaration");
                ret.append(syms.javafx_AnyType);
            }
        }
        
        return ret.toList();
    }
    
    private Type checkReturnTypes(JCTree defRestype, JCTree declRestype, JavafxJCMethodDecl methodDecl) {
        Type res = syms.javafx_AnyType;
        if (defRestype == null || declRestype == null) {
            return syms.javafx_AnyType;
        }
        
        if (defRestype.type == null) {
            defRestype.type = syms.javafx_AnyType;
        }
        
        if (declRestype.type == null) {
            declRestype.type = syms.javafx_AnyType;
        }

        Symbol defTypeSym = defRestype.type.tsym;
        Symbol declTypeSym = declRestype.type.tsym;
        if (defTypeSym == declTypeSym) {
            return defRestype.type;
        }
        else if (defRestype.type == syms.javafx_AnyType) {
            defRestype.type = declRestype.type;
            res = defRestype.type;
        }
        else if (declRestype.type == syms.javafx_AnyType) {
            declRestype.type = defRestype.type;
            res = declRestype.type;
        }
        else {
            log.error(methodDecl.pos, "javafx.different.types.of.result.for.definition.and.declaration");
            res = syms.javafx_AnyType;
        }
        
        return res;
    }

    private Type attrMethodType(JCMethodDecl tree, Env<AttrContext> lEnv) {
        Type res = null;
        List<JCTree> declParams = null;
        JCTree declRestype = null;

        List<JCTree> defParams = null;
        JCTree defRestype = null;

        JavafxJCMethodDecl jfxTree = null;

        if (tree instanceof JavafxJCMethodDecl) {
            // First attribute the definition
            jfxTree = (JavafxJCMethodDecl)tree;

            List<JCTree> params = null;
            JCTree restype = null;
            JFXStatement methodDef = jfxTree.definition;

            if (methodDef != null &&
                    jfxTree.getJavafxMethodType() >= JavafxFlags.OPERATION && 
                    jfxTree.getJavafxMethodType() <= JavafxFlags.LOCAL_FUNCTION) {
                switch (methodDef.getTag()) {
                    case JavafxTag.RETROOPERATIONDEF: {
                        JFXRetroOperationMemberDefinition operationMemberDef = (JFXRetroOperationMemberDefinition)methodDef;
                        params = operationMemberDef.params;
                        restype = operationMemberDef.getType();
                        break;
                    }
                    case JavafxTag.RETROFUNCTIONDEF: {
                        JFXRetroFunctionMemberDefinition functionMemberDef = (JFXRetroFunctionMemberDefinition)methodDef;
                        params = functionMemberDef.params;
                        restype = functionMemberDef.getType();
                        break;
                    }                    case JavafxTag.RETROOPERATIONLOCALDEF: {
                        JFXRetroOperationLocalDefinition operationLocalDef = (JFXRetroOperationLocalDefinition)methodDef;
                        params = operationLocalDef.params;
                        restype = operationLocalDef.restype;
                        break;
                    }

                    case JavafxTag.RETROFUNCTIONLOCALDEF: {
                        JFXRetroFunctionLocalDefinition functionLocalDef = (JFXRetroFunctionLocalDefinition)methodDef;
                        params = functionLocalDef.params;
                        restype = functionLocalDef.restype;
                        break;
                    }

                    default : {
                        throw new AssertionError("Unexpected JFXMethodDecl definition type!");
                    }
                }

                if (params == null) {
                    params = List.nil();
                }

                if (restype == null) {
                    restype = make.TypeIdent(CLASS);
                    restype.type = syms.javafx_AnyType;
                }

                // Create a new environment with local scope
                // for attributing the method.
                Env<AttrContext> localEnv = methodEnv(tree, env);

                // Attribute all value parameters.
                boolean prevIsInMethodLocalVars = isInMethodParamVars;
                isInMethodParamVars = true;
                for (List<JCTree> l = params; l.nonEmpty(); l = l.tail) {
                    attr.attribStat(l.head, localEnv);
                }
                isInMethodParamVars = prevIsInMethodLocalVars;
                defParams = params;

                // Check that result type is well-formed.
                chk.validate(restype);
                restype = restype.getTag() == JavafxTag.TYPECLASS ? make.Ident(((JFXTypeClass)restype).getClassName()) : restype;
                attr.attribType(restype, localEnv);

                defRestype = restype;

                localEnv.info.scope.leave();
            }

            // Then attribute the declaration
            JFXStatement methodDecl = jfxTree.declaration;

            if (methodDecl != null &&
                    jfxTree.getJavafxMethodType() >= JavafxFlags.OPERATION && 
                    jfxTree.getJavafxMethodType() <= JavafxFlags.LOCAL_FUNCTION) {
                switch (methodDecl.getTag()) {
                case JavafxTag.RETROOPERATIONDECL: {
                        JFXRetroOperationMemberDeclaration operationMemberDecl = (JFXRetroOperationMemberDeclaration)methodDecl;
                        params = operationMemberDecl.params;
                        restype = operationMemberDecl.getType();
                        break;
                    }
                case JavafxTag.RETROFUNCTIONDECL: {
                        JFXRetroFunctionMemberDeclaration functionMemberDecl = (JFXRetroFunctionMemberDeclaration)methodDecl;
                        params = functionMemberDecl.params;
                        restype = functionMemberDecl.getType();
                        break;
                    }
                default : {
                        throw new AssertionError("Unexpected JFXMethodDecl declaration type!");
                    }
                }

                if (params == null) {
                    params = List.nil();
                }

                if (restype == null) {
                    restype = make.TypeIdent(CLASS);
                    restype.type = syms.javafx_AnyType;
                }

                // Create a new environment with local scope
                // for attributing the method.
                Env<AttrContext> localEnv = methodEnv(tree, env);

                // Attribute all value parameters.
                boolean prevIsInMethodLocalVars = isInMethodParamVars;
                isInMethodParamVars = true;
                for (List<JCTree> l = params; l.nonEmpty(); l = l.tail) {
                    attr.attribStat(l.head, localEnv);
                }
                isInMethodParamVars = prevIsInMethodLocalVars;

                declParams = params;

                // Check that result type is well-formed.
                chk.validate(restype);
                restype = restype.getTag() == JavafxTag.TYPECLASS ? make.Ident(((JFXTypeClass)restype).getClassName()) : restype;
                attr.attribType(restype, localEnv);

                declRestype = restype;

                localEnv.info.scope.leave();
            }

            if (defParams != null && declParams != null) {
                if (declParams.size() != defParams.size()) {
                    log.error(tree.pos, "javafx.different.number.of.parameters.for.definition.and.declaration");
                }
            }

            List<Type> params1 = null;
            if (jfxTree != null) {
                params1 = checkParameterTypes(defParams, declParams, jfxTree);
                Type temp = checkReturnTypes(defRestype, declRestype, jfxTree);
                if (tree.restype == null || tree.restype.type == Type.noType || tree.restype.type == syms.javafx_AnyType) {
                    if (temp.tag <= TypeTags.VOID) {
                        tree.restype = make.TypeIdent(temp.tag);
                    }
                    else {
                        tree.restype = make.Ident(temp.tsym);
                    }
                }
            }

            if (params1 != null && params.length() != params1.length()) {
                throw new AssertionError("Different number of params!");
            }
            
            if (params != null) {
                for (JCVariableDecl param : tree.params) {
                    param.type = params1 != null && params1.head != null ? params1.head : syms.javafx_AnyType;
                    if (params1 != null) {
                        params1 = params1.tail;
                    }
                }
            }
        }

       res = signature(tree.typarams, tree.params,
                           tree.restype, tree.thrown,
                           lEnv);
       
        if (tree.restype != null && tree.restype.type == syms.javafx_AnyType) {
            methodsToInferReturnType = methodsToInferReturnType.append(new MethodInferTypeHelper(tree, lEnv));
        }
        return res;
    }

    private void inferMethodReturnTypes() {
        if (methodsToInferReturnType != null) { 
            for (MethodInferTypeHelper methodDeclHelper : methodsToInferReturnType) {
                if (methodDeclHelper.method.restype != null &&
                          methodDeclHelper.method.restype.type == syms.javafx_AnyType &&
                          methodDeclHelper.method.body != null) {
                      Type prevMethodReturnType = methodReturnType;
                      methodReturnType = null;
                      Type prevRestype = methodDeclHelper.method.restype.type;
                      methodDeclHelper.method.restype.type = Type.noType;
                      Env<AttrContext> prevLocalEnv = localEnv;
                      localEnv = methodDeclHelper.lEnv;
                      memberEnter(methodDeclHelper.method.body, localEnv);
                      localEnv = prevLocalEnv;
                      if (methodReturnType == null) {
                          methodDeclHelper.method.restype = make.TypeIdent(VOID);
                          methodDeclHelper.method.restype.type = Symtab.voidType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = Symtab.voidType;
                          }
                      }
                      else {
                          methodDeclHelper.method.restype = make.Type(methodReturnType);
                          methodDeclHelper.method.restype.type = methodReturnType;
                          if (methodDeclHelper.method.sym != null && methodDeclHelper.method.sym.type != null &&
                              methodDeclHelper.method.sym.kind == MTH) {
                              ((MethodType)methodDeclHelper.method.sym.type).restype = methodReturnType;
                          }
                      }
                      
                      if (methodDeclHelper.method.restype.type == Type.noType) {
                          methodDeclHelper.method.restype.type = prevRestype;
                      }
  
                      methodReturnType = prevMethodReturnType;
                }
            }
        }
    }

    static class MethodInferTypeHelper {
        JCMethodDecl method;
        Env<AttrContext> lEnv;
        MethodInferTypeHelper(JCMethodDecl method,  Env<AttrContext> lEnv) {
            this.method = method;
            this.lEnv = lEnv;
        }
    }
// Javafx modification
}
