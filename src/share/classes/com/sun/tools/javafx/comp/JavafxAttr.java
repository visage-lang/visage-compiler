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
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import java.util.Iterator;

/** This is the main context-dependent analysis phase in GJC. It
 *  encompasses name resolution, type checking and constant folding as
 *  subtasks. Some subtasks involve auxiliary classes.
 *  @see Check
 *  @see Resolve
 *  @see ConstFold
 *  @see Infer
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("@(#)JavafxAttr.java	1.228 07/05/05")
public class JavafxAttr extends Attr {
    private JCMethodDecl currentMethod = null;
    private boolean attribDefDeclParams = false;
    
    private JavafxSymtab syms;
    
    public static Attr instance0(Context context) {
        Attr instance = context.get(attrKey);
        if (instance == null)
            instance = new JavafxAttr(context);
        return instance;
    }

    public static void preRegister(final Context context) {
        context.put(attrKey, new Context.Factory<Attr>() {
	       public Attr make() {
		   return new JavafxAttr(context);
	       }
        });
    }

    protected JavafxAttr(Context context) {
        super(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    /** Check kind and type of given tree against protokind and prototype.
     *  If check succeeds, store type in tree and return it.
     *  If check fails, store errType in tree and return it.
     *  No checks are performed if the prototype is a method type.
     *  Its not necessary in this case since we know that kind and type
     *  are correct.
     *
     *  @param tree     The tree whose kind and type is checked
     *  @param owntype  The computed type of the tree
     *  @param ownkind  The computed kind of the tree
     *  @param pkind    The expected kind (or: protokind) of the tree
     *  @param pt       The expected type (or: prototype) of the tree
     */
    protected
    Type check(JCTree tree, Type owntype, int ownkind, int pkind, Type pt) {
// Javafx change
        if (owntype != null && owntype.tag != ERROR && pt.tag != METHOD && pt.tag != FORALL) {
//        if (owntype.tag != ERROR && pt.tag != METHOD && pt.tag != FORALL) {
// Javafx change
            if ((ownkind & ~pkind) == 0) {
                owntype = chk.checkType(tree.pos(), owntype, pt);
            } else {
                log.error(tree.pos(), "unexpected.type",
                          Resolve.kindNames(pkind),
                          Resolve.kindName(ownkind));
                owntype = syms.errType;
            }
        }
        tree.type = owntype;
        return owntype;
    }

    public void visitIdent(JCIdent tree) {
        // TODO: Fix this when the new named table is available
        if (tree.name == names.fromString("Integer")) {
            tree.type = syms.javafx_IntegerType;
            tree.sym = syms.javafx_IntegerType.tsym;
            result = tree.type;
        }
        else if (tree.name == names.fromString("Boolean")) {
            tree.type = syms.javafx_BooleanType;
            tree.sym = syms.javafx_BooleanType.tsym;
            result = tree.type;
        }
        else if (tree.name == names.fromString("Number")) {
            tree.type = syms.javafx_NumberType;
            tree.sym = syms.javafx_NumberType.tsym;
            result = tree.type;
        }
        else if (tree.name == names.fromString("String")) {
            tree.type = syms.javafx_StringType;
            tree.sym = syms.javafx_StringType.tsym;
            result = tree.type;
        }
        else {
            super.visitIdent(tree);
        }
    }

    public void visitAssign(JCAssign tree) {
// Javafx change        Type owntype = attribTree(tree.lhs, env.dup(tree), VAR, Type.noType);
// Javafx change        Type capturedType = capture(owntype);
// Javafx change        attribExpr(tree.rhs, env, owntype);
// Javafx change        result = check(tree, capturedType, VAL, pkind, pt);
        
        Type owntype = null;
        Env<AttrContext> dupEnv = env.dup(tree);
        owntype = attribTree(tree.lhs, dupEnv, VAR, Type.noType);
        boolean hasLhsType = false;
        if (owntype == null || owntype == syms.javafx_AnyType) {
            owntype = attribExpr(tree.rhs, env, Type.noType);
            hasLhsType = false;
        }
        else {
            hasLhsType = true;
        }
        
        Type capturedType = capture(owntype);
        
        if (hasLhsType) {
            attribExpr(tree.rhs, dupEnv, owntype);
        }
        else {
            attribTree(tree.lhs, dupEnv, VAR, owntype);
            Symbol lhsSym = JavafxTreeInfo.symbol(tree.lhs);
            lhsSym.type = owntype;
        }
                
        result = check(tree, capturedType, VAL, pkind, pt);
    }


    public void visitVarDef(JCVariableDecl tree) {
        // Local variables have not been entered yet, so we need to do it now:
        if (env.info.scope.owner.kind == MTH) {
            if (tree.sym != null) {
                // parameters have already been entered
                env.info.scope.enter(tree.sym);
            } else {
                memberEnter.memberEnter(tree, env);
                annotate.flush();
            }
        }

        // Check that the variable's declared type is well-formed.
        chk.validate(tree.vartype);

        VarSymbol v = tree.sym;

        // The info.lint field in the envs stored in enter.typeEnvs is deliberately uninitialized,
        // because the annotations were not available at the time the env was created. Therefore,
        // we look up the environment chain for the first enclosing environment for which the
        // lint value is set. Typically, this is the parent env, but might be further if there
        // are any envs created as a result of TypeParameter nodes.
        Env<AttrContext> lintEnv = env;
        while (lintEnv.info.lint == null)
            lintEnv = lintEnv.next;


        Lint lint = lintEnv.info.lint.augment(v.attributes_field, v.flags());
        Lint prevLint = chk.setLint(lint);

        try {
            chk.checkDeprecatedAnnotation(tree.pos(), v);

            if (tree.init != null) {
                if ((v.flags_field & FINAL) != 0 && tree.init.getTag() != JCTree.NEWCLASS) {
                    // In this case, `v' is final.  Ensure that it's initializer is
                    // evaluated.
                    v.getConstValue(); // ensure initializer is evaluated
                } else {
                    // Attribute initializer in a new environment
                    // with the declared variable as owner.
                    // Check that initializer conforms to variable's declared type.
                    Env<AttrContext> initEnv = memberEnter.initEnv(tree, env);
                    initEnv.info.lint = lint;
                    // In order to catch self-references, we set the variable's
                    // declaration position to maximal possible value, effectively
                    // marking the variable as undefined.
                    v.pos = Position.MAXPOS;
                    attribExpr(tree.init, initEnv, v.type);
                    v.pos = tree.pos;
                }
            }
            result = tree.type = v.type;
            chk.validateAnnotations(tree.mods.annotations, v);
        }
        finally {
            chk.setLint(prevLint);
        }
    }

    public void visitMethodDef(JCMethodDecl tree) {
// Javafx change
        JCMethodDecl prev = currentMethod;
        try {
            currentMethod = tree;
            super.visitMethodDef(tree);
        }
        finally {
            currentMethod = prev;
        }
// Javafx change
    }

    public void visitReturn(JCReturn tree) {
        // Check that there is an enclosing method which is
        // nested within than the enclosing class.
        if (env.enclMethod == null ||
            env.enclMethod.sym.owner != env.enclClass.sym) {
            log.error(tree.pos(), "ret.outside.meth");

        } else {
            // Attribute return expression, if it exists, and check that
            // it conforms to result type of enclosing method.
            Symbol m = env.enclMethod.sym;
                if (m.type.getReturnType().tag == VOID) {
                    if (tree.expr != null)
                        log.error(tree.expr.pos(),
                                  "cant.ret.val.from.meth.decl.void");
                } else if (tree.expr == null) {
                    log.error(tree.pos(), "missing.ret.val");
                } else {
                    attribExpr(tree.expr, env, m.type.getReturnType());
                }
        }

        result = null;
    }

    public void visitApply(JCMethodInvocation tree) {
        // The local environment of a method application is
        // a new environment nested in the current one.
        Env<AttrContext> localEnv = env.dup(tree, env.info.dup());

        // The types of the actual method arguments.
        List<Type> argtypes;

        // The types of the actual method type arguments.
        List<Type> typeargtypes = null;

        Name methName = JavafxTreeInfo.name(tree.meth);

        boolean isConstructorCall =
            methName == names._this || methName == names._super;

        if (isConstructorCall) {
            // We are seeing a ...this(...) or ...super(...) call.
            // Check that this is the first statement in a constructor.
            if (checkFirstConstructorStat(tree, env)) {

                // Record the fact
                // that this is a constructor call (using isSelfCall).
                localEnv.info.isSelfCall = true;

                // Attribute arguments, yielding list of argument types.
                argtypes = attribArgs(tree.args, localEnv);
                typeargtypes = attribTypes(tree.typeargs, localEnv);

                // Variable `site' points to the class in which the called
                // constructor is defined.
                Type site = env.enclClass.sym.type;
                if (methName == names._super) {
                    if (site == syms.objectType || site == syms.javafx_AnyType) {
                        log.error(tree.meth.pos(), "no.superclass", site);
                        site = syms.errType;
                    } else {
                        site = types.supertype(site);
                    }
                }

                if (site.tag == CLASS) {
                    if (site.getEnclosingType().tag == CLASS) {
                        // we are calling a nested class

                        if (tree.meth.getTag() == JCTree.SELECT) {
                            JCTree qualifier = ((JCFieldAccess) tree.meth).selected;

                            // We are seeing a prefixed call, of the form
                            //     <expr>.super(...).
                            // Check that the prefix expression conforms
                            // to the outer instance type of the class.
                            chk.checkRefType(qualifier.pos(),
                                             attribExpr(qualifier, localEnv,
                                                        site.getEnclosingType()));
                        } else if (methName == names._super) {
                            // qualifier omitted; check for existence
                            // of an appropriate implicit qualifier.
                            rs.resolveImplicitThis(tree.meth.pos(),
                                                   localEnv, site);
                        }
                    } else if (tree.meth.getTag() == JCTree.SELECT) {
                        log.error(tree.meth.pos(), "illegal.qual.not.icls",
                                  site.tsym);
                    }

                    // if we're calling a java.lang.Enum constructor,
                    // prefix the implicit String and int parameters
                    if (site.tsym == syms.enumSym && allowEnums)
                        argtypes = argtypes.prepend(syms.intType).prepend(syms.stringType);

                    // Resolve the called constructor under the assumption
                    // that we are referring to a superclass instance of the
                    // current instance (JLS ???).
                    boolean selectSuperPrev = localEnv.info.selectSuper;
                    localEnv.info.selectSuper = true;
                    localEnv.info.varArgs = false;
                    Symbol sym = rs.resolveConstructor(
                        tree.meth.pos(), localEnv, site, argtypes, typeargtypes);
                    localEnv.info.selectSuper = selectSuperPrev;

                    // Set method symbol to resolved constructor...
                    JavafxTreeInfo.setSymbol(tree.meth, sym);

                    // ...and check that it is legal in the current context.
                    // (this will also set the tree's type)
                    Type mpt = newMethTemplate(argtypes, typeargtypes);
                    checkId(tree.meth, site, sym, localEnv, MTH,
                            mpt, tree.varargsElement != null);
                }
                // Otherwise, `site' is an error type and we do nothing
            }
            result = tree.type = syms.voidType;
        } else {
            // Otherwise, we are seeing a regular method call.
            // Attribute the arguments, yielding list of argument types, ...
            argtypes = attribArgs(tree.args, localEnv);
            typeargtypes = attribTypes(tree.typeargs, localEnv);

            // ... and attribute the method using as a prototype a methodtype
            // whose formal argument types is exactly the list of actual
            // arguments (this will also set the method symbol).
            Type mpt = newMethTemplate(argtypes, typeargtypes);
            localEnv.info.varArgs = false;
            Type mtype = attribExpr(tree.meth, localEnv, mpt);
            if (localEnv.info.varArgs)
                assert mtype.isErroneous() || tree.varargsElement != null;

            // Compute the result type.
            Type restype = mtype.getReturnType();
            assert restype.tag != WILDCARD : mtype;

            // as a special case, array.clone() has a result that is
            // the same as static type of the array being cloned
            if (tree.meth.getTag() == JCTree.SELECT &&
                allowCovariantReturns &&
                methName == names.clone &&
                types.isArray(((JCFieldAccess) tree.meth).selected.type))
                restype = ((JCFieldAccess) tree.meth).selected.type;

            // as a special case, x.getClass() has type Class<? extends |X|>
            if (allowGenerics &&
                methName == names.getClass && tree.args.isEmpty()) {
                Type qualifier = (tree.meth.getTag() == JCTree.SELECT)
                    ? ((JCFieldAccess) tree.meth).selected.type
                    : env.enclClass.sym.type;
                restype = new
                    ClassType(restype.getEnclosingType(),
                              List.<Type>of(new WildcardType(types.erasure(qualifier),
                                                               BoundKind.EXTENDS,
                                                               syms.boundClass)),
                              restype.tsym);
            }

// Javafx change
            if (restype == null) {
                tree.type = pt;
                result = pt;
            }
            else {
                
// Javafx change
            // Check that value of resulting type is admissible in the
            // current context.  Also, capture the return type
                result = check(tree, capture(restype), VAL, pkind, pt);
            }
        }
        chk.validate(tree.typeargs);
    }
    
    public void visitBlockExpression(JFXBlockExpression tree) {
        // Create a new local environment with a local scope.
        Env<AttrContext> localEnv =
                env.dup(tree,
                env.info.dup(env.info.scope.dup()));
        for (List<JCStatement> l = tree.stats; l.nonEmpty(); l = l.tail)
            attribStat(l.head, localEnv);
        if (tree.value != null) {
            Type valtype = attribExpr(tree.value, localEnv);
             result = check(tree, valtype, VAL, pkind, pt);
        }
        localEnv.info.scope.leave();
    }

    public void visitBinary(JCBinary tree) {
        // Attribute arguments.
        Type left = chk.checkNonVoid(tree.lhs.pos(), attribExpr(tree.lhs, env));
        Type right = chk.checkNonVoid(tree.lhs.pos(), attribExpr(tree.rhs, env));
// Javafx change
        if (left == syms.javafx_AnyType) {
            left = setEffectiveExpressionType(tree.lhs, newTypeFromType(getEffectiveExpressionType(right)));
        }
        else if (right == syms.javafx_AnyType) {
            right = setEffectiveExpressionType(tree.rhs, newTypeFromType(getEffectiveExpressionType(left)));
        }
 // Javafx change
        // Find operator.
        Symbol operator = tree.operator =
            rs.resolveBinaryOperator(tree.pos(), tree.getTag(), env, left, right);

        Type owntype = syms.errType;
        if (operator.kind == MTH) {
            owntype = operator.type.getReturnType();
            int opc = chk.checkOperator(tree.lhs.pos(),
                                        (OperatorSymbol)operator,
                                        tree.getTag(),
                                        left,
                                        right);

            // If both arguments are constants, fold them.
            if (left.constValue() != null && right.constValue() != null) {
                Type ctype = cfolder.fold2(opc, left, right);
                if (ctype != null) {
                    owntype = cfolder.coerce(ctype, owntype);

                    // Remove constant types from arguments to
                    // conserve space. The parser will fold concatenations
                    // of string literals; the code here also
                    // gets rid of intermediate results when some of the
                    // operands are constant identifiers.
                    if (tree.lhs.type.tsym == syms.stringType.tsym) {
                        tree.lhs.type = syms.stringType;
                    }
                    if (tree.rhs.type.tsym == syms.stringType.tsym) {
                        tree.rhs.type = syms.stringType;
                    }
                }
            }

            // Check that argument types of a reference ==, != are
            // castable to each other, (JLS???).
            if ((opc == ByteCodes.if_acmpeq || opc == ByteCodes.if_acmpne)) {
                if (!types.isCastable(left, right, new Warner(tree.pos()))) {
                    log.error(tree.pos(), "incomparable.types", left, right);
                }
            }

            chk.checkDivZero(tree.rhs.pos(), operator, right);
        }
        result = check(tree, owntype, VAL, pkind, pt);
    }
    
     public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
    }

    /** Clones a type without copiyng constant values
     * @param t the type that needs to be cloned.
     * @return  the cloned type with no cloned constants.
     */
    public Type newTypeFromType(Type t) {
        if (t == null) return null;
        switch (t.tag) {
            case BYTE:
                return syms.byteType;
            case CHAR:
                return syms.charType;
            case SHORT:
                return syms.shortType;
            case INT:
                return syms.intType;
            case LONG:
                return syms.longType;
            case FLOAT:
                return syms.floatType;
            case DOUBLE:
                return syms.doubleType;
            case BOOLEAN:
                return syms.booleanType;
            case VOID:
                return syms.voidType;
            default:
                return t;
        }
    }
    
    /**
     * Gets the effective type of a type. If MethodType - the return type,
     * otherwise the passed in type.
     */
    private Type getEffectiveExpressionType(Type type) {
        if (type.tag == TypeTags.METHOD) {
            return type.getReturnType();
        }
        
        return type;
    }

    /**
     * Sets the effective type of an expression. If MethodType - the return type,
     * otherwise the whole type of the expression is set.
     */
    private Type setEffectiveExpressionType(JCExpression expression, Type type) {
        if (expression.type.tag == TypeTags.METHOD) {
            ((MethodType)expression.type).restype = type;
        }
        else {
            expression.type = type;
        }

        return expression.type;
    }
}
