/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.javac.comp.*;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Kinds.*;
import com.sun.tools.javac.code.Symbol.OperatorSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.WildcardType;
import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javac.code.TypeTags.WILDCARD;
import com.sun.tools.javac.jvm.ByteCodes;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javafx.code.*;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.JFXBlockExpression;
import com.sun.tools.javafx.tree.JavafxTreeInfo;
import com.sun.tools.javafx.tree.*;
import java.util.*;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.tools.JavaFileObject;
import com.sun.tools.javafx.code.FunctionType;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;

import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.code.Type.*;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.util.SimpleTreeVisitor;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;

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
public class JavafxAttr extends JCTree.Visitor implements JavafxVisitor {
    protected static final Context.Key<JavafxAttr> javafxAttrKey =
        new Context.Key<JavafxAttr>();

    /*
     * modules imported by context
     */
    private final Name.Table names;
    private final Log log;
    private final JavafxResolve rs;
    private final JavafxSymtab syms;
    private final JavafxCheck chk;
    private final JavafxMemberEnter memberEnter;
    private final JavafxTreeMaker make;
    private final ConstFold cfolder;
    private final JavafxEnter enter;
    private final Target target;
    private final Types types;
    private final Annotate annotate;
    private JavafxInitializationBuilder initBuilder;

    /*
     * other instance information
     */
    private final Name numberTypeName;
    private final Name integerTypeName;
    private final Name booleanTypeName;
    private final Name stringTypeName;
    private final Name voidTypeName;  // possibly temporary
    private final Name invokeName;

    public static JavafxAttr instance(Context context) {
        JavafxAttr instance = context.get(javafxAttrKey);
        if (instance == null)
            instance = new JavafxAttr(context);
        return instance;
    }

    protected JavafxAttr(Context context) {    
        context.put(javafxAttrKey, this);

        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        names = Name.Table.instance(context);
        log = Log.instance(context);
        rs = JavafxResolve.instance(context);
        chk = JavafxCheck.instance(context);
        memberEnter = JavafxMemberEnter.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        enter = JavafxEnter.instance(context);
        cfolder = ConstFold.instance(context);
        target = Target.instance(context);
        types = Types.instance(context);
        annotate = Annotate.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);

        Options options = Options.instance(context);

        Source source = Source.instance(context);
        allowGenerics = source.allowGenerics();
        allowVarargs = source.allowVarargs();
        allowEnums = source.allowEnums();
        allowBoxing = source.allowBoxing();
        allowCovariantReturns = source.allowCovariantReturns();
        allowAnonOuterThis = source.allowAnonOuterThis();
        relax = (options.get("-retrofit") != null ||
                 options.get("-relax") != null);
        useBeforeDeclarationWarning = options.get("useBeforeDeclarationWarning") != null;
        
        numberTypeName  = names.fromString("Number");
        integerTypeName = names.fromString("Integer");
        booleanTypeName = names.fromString("Boolean");
        stringTypeName = names.fromString("String");
        voidTypeName = names.fromString("Void");
        invokeName = names.fromString("invoke");
    }
    /** Switch: relax some constraints for retrofit mode.
     */
// JavaFX change
    protected
// JavaFX change
    boolean relax;

// Javafx change
    protected
// Javafx change
    /** Switch: support generics?
     */
    boolean allowGenerics;

    /** Switch: allow variable-arity methods.
     */
// Javafx change
    protected
// Javafx change
    boolean allowVarargs;

// Javafx change
    protected
// Javafx change
    /** Switch: support enums?
     */
    boolean allowEnums;

// Javafx change
    protected
// Javafx change
    /** Switch: support boxing and unboxing?
     */
    boolean allowBoxing;

// Javafx change
    protected
// Javafx change
    /** Switch: support covariant result types?
     */
    boolean allowCovariantReturns;

// Javafx change
    protected
// Javafx change
    /** Switch: allow references to surrounding object from anonymous
     * objects during constructor call?
     */
    boolean allowAnonOuterThis;

    /**
     * Switch: warn about use of variable before declaration?
     * RFE: 6425594
     */
    boolean useBeforeDeclarationWarning;

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
        if (owntype != null && owntype != syms.javafx_UnspecifiedType && owntype.tag != ERROR && pt.tag != METHOD && pt.tag != FORALL) {
//        if (owntype.tag != ERROR && pt.tag != METHOD && pt.tag != FORALL) {
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

    /** Is this symbol a type?
     */
    static boolean isType(Symbol sym) {
        return sym != null && sym.kind == TYP;
    }
    
    public Type boxIfNeeded(Type elemType) {
        if (elemType.isPrimitive())
            return types.boxedClass(elemType).type;
        else
            return elemType;
    }

    /** The current `this' symbol.
     *  @param env    The current environment.
     */
    Symbol thisSym(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env) {
        return rs.resolveSelf(pos, env, env.enclClass.sym, names._this);
    }

    private JCTree breakTree = null;
        
    private static class BreakAttr extends RuntimeException {
        static final long serialVersionUID = -6924771130405446405L;
        private JavafxEnv<JavafxAttrContext> env;
        private BreakAttr(JavafxEnv<JavafxAttrContext> env) {
            this.env = env;
        }
    }
   
/* ************************************************************************
 * Visitor methods
 *************************************************************************/

    /** Visitor argument: the current environment.
     */
    JavafxEnv<JavafxAttrContext> env;

    /** Visitor argument: the currently expected proto-kind.
     */
    int pkind;

    /** Visitor argument: the currently expected proto-type.
     */
    Type pt;

    /** Visitor argument: is a sequence permitted
     */
    boolean pSequencePermitted;

    /** Visitor result: the computed type.
     */
    Type result;
    
    /** Visitor method: attribute a tree, catching any completion failure
     *  exceptions. Return the tree's type.
     *
     *  @param tree    The tree to be visited.
     *  @param env     The environment visitor argument.
     *  @param pkind   The protokind visitor argument.
     *  @param pt      The prototype visitor argument.
     */
    Type attribTree(JCTree tree, JavafxEnv<JavafxAttrContext> env, int pkind, Type pt) {
        return attribTree(tree, env, pkind, pt, pSequencePermitted);
    }
    
    Type attribTree(JCTree tree, JavafxEnv<JavafxAttrContext> env, int pkind, Type pt, boolean pSequencePermitted) {
        JavafxEnv<JavafxAttrContext> prevEnv = this.env;
        int prevPkind = this.pkind;
        Type prevPt = this.pt;
        boolean prevSequencePermitted = this.pSequencePermitted;
        try {
            this.env = env;
            this.pkind = pkind;
            this.pt = pt;
            this.pSequencePermitted = pSequencePermitted;
            tree.accept(this);
            if (tree == breakTree)
                throw new BreakAttr(env);
            return result;
        } catch (CompletionFailure ex) {
            tree.type = syms.errType;
            return chk.completionError(tree.pos(), ex);
        } finally {
            this.env = prevEnv;
            this.pkind = prevPkind;
            this.pt = prevPt;
            this.pSequencePermitted = prevSequencePermitted;
        }
    }

    /** Derived visitor method: attribute an expression tree.
     */
    public Type attribExpr(JCTree tree, JavafxEnv<JavafxAttrContext> env, Type pt, boolean pSequencePermitted) {
        return attribTree(tree, env, VAL, pt.tag != ERROR ? pt : Type.noType, pt.tag != ERROR ? pSequencePermitted : true);
    }

    /** Derived visitor method: attribute an expression tree.  
     *  allow a sequence is no proto-type is specified, the proto-type is a seqeunce,
     *  or the proto-type is an error.
     */
    public Type attribExpr(JCTree tree, JavafxEnv<JavafxAttrContext> env, Type pt) {
        return attribTree(tree, env, VAL, pt.tag != ERROR ? pt : Type.noType, pt.tag == ERROR || pt == Type.noType || isSequence(pt));
    }

    /** Derived visitor method: attribute an expression tree with
     *  no constraints on the computed type.
     */
    Type attribExpr(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
        return attribTree(tree, env, VAL, Type.noType, true);
    }

    /** Derived visitor method: attribute a type tree.
     */
    Type attribType(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
        Type result = attribTree(tree, env, TYP, Type.noType, false);
        return result;
    }

    /** Derived visitor method: attribute a statement or definition tree.
     */
    public Type attribStat(JCTree tree, JavafxEnv<JavafxAttrContext> env) {
        return attribTree(tree, env, NIL, Type.noType, false);
    }
    
    public Type attribVar(JFXVar tree, JavafxEnv<JavafxAttrContext> env) {
        memberEnter.memberEnter(tree, env);
        return attribTree(tree, env, NIL, Type.noType, false);
    }

    /** Attribute a list of expressions, returning a list of types.
     */
    List<Type> attribExprs(List<JCExpression> trees, JavafxEnv<JavafxAttrContext> env, Type pt) {
        ListBuffer<Type> ts = new ListBuffer<Type>();
        for (List<JCExpression> l = trees; l.nonEmpty(); l = l.tail)
            ts.append(attribExpr(l.head, env, pt));
        return ts.toList();
    }
    
    /** Attribute the arguments in a method call, returning a list of types.
     */
    List<Type> attribArgs(List<JCExpression> trees, JavafxEnv<JavafxAttrContext> env) {
        ListBuffer<Type> argtypes = new ListBuffer<Type>();
        for (List<JCExpression> l = trees; l.nonEmpty(); l = l.tail)
            argtypes.append(chk.checkNonVoid(
                l.head.pos(), types.upperBound(attribTree(l.head, env, VAL, Infer.anyPoly))));
        return argtypes.toList();
    }

    /** Does tree represent a static reference to an identifier?
     *  It is assumed that tree is either a SELECT or an IDENT.
     *  We have to weed out selects from non-type names here.
     *  @param tree    The candidate tree.
     */
    boolean isStaticReference(JCTree tree) {
        if (tree.getTag() == JCTree.SELECT) {
            Symbol lsym = TreeInfo.symbol(((JCFieldAccess) tree).selected);
            if (lsym == null || lsym.kind != TYP) {
                return false;
            }
        }
        return true;
    }

    /** Attribute a list of statements, returning nothing.
     */
    <T extends JCTree> void attribStats(List<T> trees, JavafxEnv<JavafxAttrContext> env) {
        for (List<T> l = trees; l.nonEmpty(); l = l.tail)
            attribStat(l.head, env);
    }

// Javafx change
    protected
// Javafx change
    /** Attribute a type argument list, returning a list of types.
     */
    List<Type> attribTypes(List<JCExpression> trees, JavafxEnv<JavafxAttrContext> env) {
        ListBuffer<Type> argtypes = new ListBuffer<Type>();
        for (List<JCExpression> l = trees; l.nonEmpty(); l = l.tail)
            argtypes.append(chk.checkRefType(l.head.pos(), attribType(l.head, env)));
        return argtypes.toList();
    }

    /**
     * Attribute type variables (of generic classes or methods).
     * Compound types are attributed later in attribBounds.
     * @param typarams the type variables to enter
     * @param env      the current environment
     */
    void attribTypeVariables(List<JCTypeParameter> typarams, JavafxEnv<JavafxAttrContext> env) {
        for (JCTypeParameter tvar : typarams) {
            TypeVar a = (TypeVar)tvar.type;
            if (!tvar.bounds.isEmpty()) {
                List<Type> bounds = List.of(attribType(tvar.bounds.head, env));
                for (JCExpression bound : tvar.bounds.tail)
                    bounds = bounds.prepend(attribType(bound, env));
                types.setBounds(a, bounds.reverse());
            } else {
                // if no bounds are given, assume a single bound of
                // java.lang.Object.
                types.setBounds(a, List.of(syms.objectType));
            }
        }
        for (JCTypeParameter tvar : typarams)
            chk.checkNonCyclic(tvar.pos(), (TypeVar)tvar.type);
        attribStats(typarams, env);
    }
    
    void attribBounds(List<JCTypeParameter> typarams) {
        for (JCTypeParameter typaram : typarams) {
            Type bound = typaram.type.getUpperBound();
            if (bound != null && bound.tsym instanceof ClassSymbol) {
                ClassSymbol c = (ClassSymbol)bound.tsym;
                if ((c.flags_field & COMPOUND) != 0) {
                    assert (c.flags_field & UNATTRIBUTED) != 0 : c;
                    attribClass(typaram.pos(), c);
                }
            }
        }
    }

    /**
     * Attribute the type references in a list of annotations.
     */
    void attribAnnotationTypes(List<JCAnnotation> annotations,
                               JavafxEnv<JavafxAttrContext> env) {
        for (List<JCAnnotation> al = annotations; al.nonEmpty(); al = al.tail) {
            JCAnnotation a = al.head;
            attribType(a.annotationType, env);
        }
    }

    /** Attribute type reference in an `extends' or `implements' clause.
     *
     *  @param tree              The tree making up the type reference.
     *  @param env               The environment current at the reference.
     *  @param classExpected     true if only a class is expected here.
     *  @param interfaceExpected true if only an interface is expected here.
     */
    Type attribBase(JCTree tree,
                    JavafxEnv<JavafxAttrContext> env,
                    boolean classExpected,
                    boolean interfaceExpected,
                    boolean checkExtensible) {
        Type t = attribType(tree, env);
        return checkBase(t, tree, env, classExpected, interfaceExpected, checkExtensible);
    }

    Type checkBase(Type t,
                   JCTree tree,
                   JavafxEnv<JavafxAttrContext> env,
                   boolean classExpected,
                   boolean interfaceExpected,
                   boolean checkExtensible) {
        if (t.tag == TYPEVAR && !classExpected && !interfaceExpected) {
            // check that type variable is already visible
            if (t.getUpperBound() == null) {
                log.error(tree.pos(), "illegal.forward.ref");
                return syms.errType;
            }
        } else {
            t = chk.checkClassType(tree.pos(), t, checkExtensible|!allowGenerics);
        }
        if (interfaceExpected && (t.tsym.flags() & INTERFACE) == 0) {
            log.error(tree.pos(), "intf.expected.here");
            // return errType is necessary since otherwise there might
            // be undetected cycles which cause attribution to loop
            return syms.errType;
        } else if (checkExtensible &&
                   classExpected &&
                   (t.tsym.flags() & INTERFACE) != 0) {
            log.error(tree.pos(), "no.intf.expected.here");
            return syms.errType;
        }
        if (checkExtensible &&
            ((t.tsym.flags() & FINAL) != 0)) {
            log.error(tree.pos(),
                      "cant.inherit.from.final", t.tsym);
        }
        chk.checkNonCyclic(tree.pos(), t);
        return t;
    }

    public void visitTypeCast(JCTypeCast tree) {
        Type clazztype = attribType(tree.clazz, env);
        Type exprtype = attribExpr(tree.expr, env, Infer.anyPoly);
        Type owntype = chk.checkCastable(tree.expr.pos(), exprtype, clazztype);
        if (exprtype.constValue() != null)
            owntype = cfolder.coerce(exprtype, owntype);
        result = check(tree, capture(owntype), VAL, pkind, pt);
    }

    public void visitTypeTest(JCInstanceOf tree) {
        Type exprtype = chk.checkNullOrRefType(
            tree.expr.pos(), attribExpr(tree.expr, env));
        Type clazztype = chk.checkReifiableReferenceType(
            tree.clazz.pos(), attribType(tree.clazz, env));
        chk.checkCastable(tree.expr.pos(), exprtype, clazztype);
        result = check(tree, syms.booleanType, VAL, pkind, pt);
    }

    public void visitIndexed(JCArrayAccess tree) {
        Type owntype = syms.errType;
        Type atype = attribExpr(tree.indexed, env);
        attribExpr(tree.index, env, syms.intType);
        if (types.isArray(atype))
            owntype = types.elemtype(atype);
        else if (atype.tag != ERROR)
            log.error(tree.pos(), "array.req.but.found", atype);
        if ((pkind & VAR) == 0) owntype = capture(owntype);
        result = check(tree, owntype, VAR, pkind, pt);
    }

    @Override
    public void visitIdent(JCIdent tree) {
        Symbol sym;
        boolean varArgs = false;

        // Find symbol
        if (tree.sym != null && tree.sym.kind != VAR) {
            sym = tree.sym;
        } else {
            sym = rs.resolveIdent(tree.pos(), env, tree.name, pkind, pt);
        }
        tree.sym = sym;
        sym.complete();

        // (1) Also find the environment current for the class where
        //     sym is defined (`symEnv').
        // Only for pre-tiger versions (1.4 and earlier):
        // (2) Also determine whether we access symbol out of an anonymous
        //     class in a this or super call.  This is illegal for instance
        //     members since such classes don't carry a this$n link.
        //     (`noOuterThisPath').
        JavafxEnv<JavafxAttrContext> symEnv = env;
        boolean noOuterThisPath = false;
        if (env.enclClass.sym.owner.kind != PCK && // we are in an inner class
            (sym.kind & (VAR | MTH | TYP)) != 0 &&
            sym.owner.kind == TYP &&
            tree.name != names._this && tree.name != names._super) {

            // Find environment in which identifier is defined.
            while (symEnv.outer != null &&
                   !sym.isMemberOf(symEnv.enclClass.sym, types)) {
                if ((symEnv.enclClass.sym.flags() & NOOUTERTHIS) != 0)
                    noOuterThisPath = !allowAnonOuterThis;
                symEnv = symEnv.outer;
            }
        }

        // If symbol is a variable, ...
        if (sym.kind == VAR) {
            VarSymbol v = (VarSymbol)sym;

            // ..., evaluate its initializer, if it has one, and check for
            // illegal forward reference.
            checkInit(tree, env, v, false);

            // If symbol is a local variable accessed from an embedded
            // inner class check that it is final.
            if (v.owner.kind == MTH &&
                v.owner != env.info.scope.owner &&
                (v.flags_field & FINAL) == 0) {
                log.error(tree.pos(),
                          "local.var.accessed.from.icls.needs.final",
                          v);
            }

            // If we are expecting a variable (as opposed to a value), check
            // that the variable is assignable in the current environment.
            if (pkind == VAR)
                checkAssignable(tree.pos(), v, null, env);
        }

        // In a constructor body,
        // if symbol is a field or instance method, check that it is
        // not accessed before the supertype constructor is called.
        if ((symEnv.info.isSelfCall || noOuterThisPath) &&
            (sym.kind & (VAR | MTH)) != 0 &&
            sym.owner.kind == TYP &&
            (sym.flags() & STATIC) == 0) {
            chk.earlyRefError(tree.pos(), sym.kind == VAR ? sym : thisSym(tree.pos(), env));
        }
	JavafxEnv<JavafxAttrContext> env1 = env;
	if (sym.kind != ERR && sym.owner != null && sym.owner != env1.enclClass.sym) {
	    // If the found symbol is inaccessible, then it is
	    // accessed through an enclosing instance.  Locate this
	    // enclosing instance:
	    while (env1.outer != null && !rs.isAccessible(env, env1.enclClass.sym.type, sym))
		env1 = env1.outer;
	}
        result = checkId(tree, env1.enclClass.sym.type, sym, env, pkind, pt, varArgs);
    }
    
    public void visitSelect(JCFieldAccess tree) {
        // Determine the expected kind of the qualifier expression.
        int skind = 0;
        if (tree.name == names._this || tree.name == names._super ||
            tree.name == names._class)
        {
            skind = TYP;
        } else {
            if ((pkind & PCK) != 0) skind = skind | PCK;
            if ((pkind & TYP) != 0) skind = skind | TYP | PCK;
            if ((pkind & (VAL | MTH)) != 0) skind = skind | VAL | TYP;
        }

        // Attribute the qualifier expression, and determine its symbol (if any).
        Type site = attribTree(tree.selected, env, skind, Infer.anyPoly);
        if ((pkind & (PCK | TYP)) == 0)
            site = capture(site); // Capture field access

        // don't allow T.class T[].class, etc
        if (skind == TYP) {
            Type elt = site;
            while (elt.tag == ARRAY)
                elt = ((ArrayType)elt).elemtype;
            if (elt.tag == TYPEVAR) {
                log.error(tree.pos(), "type.var.cant.be.deref");
                result = syms.errType;
                return;
            }
        }

        // If qualifier symbol is a type or `super', assert `selectSuper'
        // for the selection. This is relevant for determining whether
        // protected symbols are accessible.
        Symbol sitesym = TreeInfo.symbol(tree.selected);
        boolean selectSuperPrev = env.info.selectSuper;
        env.info.selectSuper =
            sitesym != null &&
            sitesym.name == names._super;

        // If selected expression is polymorphic, strip
        // type parameters and remember in env.info.tvars, so that
        // they can be added later (in Attr.checkId and Infer.instantiateMethod).
        if (tree.selected.type.tag == FORALL) {
            ForAll pstype = (ForAll)tree.selected.type;
            env.info.tvars = pstype.tvars;
            site = tree.selected.type = pstype.qtype;
        }

        // Determine the symbol represented by the selection.
        env.info.varArgs = false;
        Symbol sym = selectSym(tree, site, env, pt, pkind);
        sym.complete();
        if (sym.exists() && !isType(sym) && (pkind & (PCK | TYP)) != 0) {
            site = capture(site);
            sym = selectSym(tree, site, env, pt, pkind);
        }
        boolean varArgs = env.info.varArgs;
        tree.sym = sym;

        if (site.tag == TYPEVAR && !isType(sym) && sym.kind != ERR)
            site = capture(site.getUpperBound());

        // If that symbol is a variable, ...
        if (sym.kind == VAR) {
            VarSymbol v = (VarSymbol)sym;

            // ..., evaluate its initializer, if it has one, and check for
            // illegal forward reference.
            checkInit(tree, env, v, true);

            // If we are expecting a variable (as opposed to a value), check
            // that the variable is assignable in the current environment.
            if (pkind == VAR)
                checkAssignable(tree.pos(), v, tree.selected, env);
        }

        // Disallow selecting a type from an expression
        if (isType(sym) && (sitesym==null || (sitesym.kind&(TYP|PCK)) == 0)) {
            tree.type = check(tree.selected, pt,
                              sitesym == null ? VAL : sitesym.kind, TYP|PCK, pt);
        }

        if (isType(sitesym)) {
            if (sym.name == names._this) {
                // If `C' is the currently compiled class, check that
                // C.this' does not appear in a call to a super(...)
                if (env.info.isSelfCall &&
                    site.tsym == env.enclClass.sym) {
                    chk.earlyRefError(tree.pos(), sym);
                }
            } else {
                // Check if type-qualified fields or methods are static (JLS)
                if ((sym.flags() & STATIC) == 0 &&
                    sym.name != names._super &&
                    (sym.kind == VAR || sym.kind == MTH)) {
                    rs.access(rs.new StaticError(sym),
                              tree.pos(), site, sym.name, true);
                }
            }
        }

        // If we are selecting an instance member via a `super', ...
        if (env.info.selectSuper && (sym.flags() & STATIC) == 0) {

            // Check that super-qualified symbols are not abstract (JLS)
            rs.checkNonAbstract(tree.pos(), sym);

            if (site.isRaw()) {
                // Determine argument types for site.
                Type site1 = types.asSuper(env.enclClass.sym.type, site.tsym);
                if (site1 != null) site = site1;
            }
        }

        env.info.selectSuper = selectSuperPrev;
        result = checkId(tree, site, sym, env, pkind, pt, varArgs);
        env.info.tvars = List.nil();
    }
    //where
        /** Determine symbol referenced by a Select expression,
         *
         *  @param tree   The select tree.
         *  @param site   The type of the selected expression,
         *  @param env    The current environment.
         *  @param pt     The current prototype.
         *  @param pkind  The expected kind(s) of the Select expression.
         */
    @SuppressWarnings("fallthrough")
        private Symbol selectSym(JCFieldAccess tree,
                                 Type site,
                                 JavafxEnv<JavafxAttrContext> env,
                                 Type pt,
                                 int pkind) {
            DiagnosticPosition pos = tree.pos();
            Name name = tree.name;

            switch (site.tag) {
            case PACKAGE:
                return rs.access(
                    rs.findIdentInPackage(env, site.tsym, name, pkind),
                    pos, site, name, true);
            case ARRAY:
            case CLASS:
                if (pt.tag == METHOD || pt.tag == FORALL) {
                    return rs.resolveQualifiedMethod(
                        pos, env, site, name, pt.getParameterTypes(), pt.getTypeArguments());
                } else if (name == names._this || name == names._super) {
                    return rs.resolveSelf(pos, env, site.tsym, name);
                } else if (name == names._class) {
                    // In this case, we have already made sure in
                    // visitSelect that qualifier expression is a type.
                    Type t = syms.classType;
                    List<Type> typeargs = allowGenerics
                        ? List.of(types.erasure(site))
                        : List.<Type>nil();
                    t = new ClassType(t.getEnclosingType(), typeargs, t.tsym);
                    return new VarSymbol(
                        STATIC | PUBLIC | FINAL, names._class, t, site.tsym);
                } else {
                    // We are seeing a plain identifier as selector.
                    Symbol sym = rs.findIdentInType(env, site, name, pkind);
                    if ((pkind & ERRONEOUS) == 0)
                        sym = rs.access(sym, pos, site, name, true);
                    return sym;
                }
            case WILDCARD:
                throw new AssertionError(tree);
            case TYPEVAR:
                // Normally, site.getUpperBound() shouldn't be null.
                // It should only happen during memberEnter/attribBase
                // when determining the super type which *must* be
                // done before attributing the type variables.  In
                // other words, we are seeing this illegal program:
                // class B<T> extends A<T.foo> {}
                Symbol sym = (site.getUpperBound() != null)
                    ? selectSym(tree, capture(site.getUpperBound()), env, pt, pkind)
                    : null;
                if (sym == null || isType(sym)) {
                    log.error(pos, "type.var.cant.be.deref");
                    return syms.errSymbol;
                } else {
                    return sym;
                }
            case ERROR:
                // preserve identifier names through errors
                return new ErrorType(name, site.tsym).tsym;
            case INT:
            case DOUBLE:
            case BOOLEAN:
                if (pt.tag == METHOD || pt.tag == FORALL) {
                    Type boxedSite = types.boxedClass(site).type;
                    return rs.resolveQualifiedMethod(
                        pos, env, boxedSite, name, pt.getParameterTypes(), pt.getTypeArguments());
                } 
                // Fall through to default
            default:
                // The qualifier expression is of a primitive type -- only
                // .class is allowed for these.
                if (name == names._class) {
                    // In this case, we have already made sure in Select that
                    // qualifier expression is a type.
                    Type t = syms.classType;
                    Type arg = types.boxedClass(site).type;
                    t = new ClassType(t.getEnclosingType(), List.of(arg), t.tsym);
                    return new VarSymbol(
                        STATIC | PUBLIC | FINAL, names._class, t, site.tsym);
                } else {
                    log.error(pos, "cant.deref", site);
                    return syms.errSymbol;
                }
            }
        }


    public void visitParens(JCParens tree) {
        Type owntype = attribTree(tree.expr, env, pkind, pt);
        result = check(tree, owntype, pkind, pkind, pt);
        Symbol sym = TreeInfo.symbol(tree);
        if (sym != null && (sym.kind&(TYP|PCK)) != 0)
            log.error(tree.pos(), "illegal.start.of.type");
    }

    @Override
    public void visitAssign(JCAssign tree) {
// Javafx change        Type owntype = attribTree(tree.lhs, env.dup(tree), VAR, Type.noType);
// Javafx change        Type capturedType = capture(owntype);
// Javafx change        attribExpr(tree.rhs, env, owntype);
// Javafx change        result = check(tree, capturedType, VAL, pkind, pt);
        
        Type owntype = null;
        JavafxEnv<JavafxAttrContext> dupEnv = env.dup(tree);
        owntype = attribTree(tree.lhs, dupEnv, VAR, Type.noType);
        boolean hasLhsType = false;
        if (owntype == null || owntype == syms.javafx_UnspecifiedType) {
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
            if (tree.lhs.getTag() == JCTree.SELECT) {
                JCFieldAccess fa = (JCFieldAccess)tree.lhs;
                fa.type = owntype;
                if (fa.sym != null) {
                    fa.sym.type = owntype;
                }
            }
            else if (tree.lhs.getTag() == JCTree.IDENT) {
                JCIdent id = (JCIdent)tree.lhs;
                id.type = owntype;
                if (id.sym != null) {
                    id.sym.type = owntype;
                }
            }
            
            attribTree(tree.lhs, dupEnv, VAR, owntype);
            Symbol lhsSym = JavafxTreeInfo.symbol(tree.lhs);
            lhsSym.type = owntype;
        }
                
        result = check(tree, capturedType, VAL, pkind, pt);
    }
    
    @Override
    public void visitVarDef(JCVariableDecl tree) {
        assert false : "SHOULD NOT REACH HERE";
    }

    @Override
    public void visitVar(JFXVar tree) {
        Symbol sym = tree.sym;
        sym.complete();
    }

    public void finishVar(JFXVar tree, JavafxEnv<JavafxAttrContext> env) {
        VarSymbol v = tree.sym;
        Type declType = attribType(tree.getJFXType(), env);
        if (declType != syms.javafx_UnspecifiedType)
            result = tree.type = v.type = declType;

        // Check that the variable's declared type is well-formed.
//        chk.validate(tree.vartype);

        // The info.lint field in the envs stored in enter.typeEnvs is deliberately uninitialized,
        // because the annotations were not available at the time the env was created. Therefore,
        // we look up the environment chain for the first enclosing environment for which the
        // lint value is set. Typically, this is the parent env, but might be further if there
        // are any envs created as a result of TypeParameter nodes.
        JavafxEnv<JavafxAttrContext> lintEnv = env;
        while (lintEnv.info.lint == null)
            lintEnv = lintEnv.next;
        Lint lint = lintEnv.info.lint.augment(v.attributes_field, v.flags());
        Lint prevLint = chk.setLint(lint);

        try {
            chk.checkDeprecatedAnnotation(tree.pos(), v);

            Type initType;
            if (tree.init != null) {
                    // Attribute initializer in a new environment
                    // with the declared variable as owner.
                    // Check that initializer conforms to variable's declared type.
                    JavafxEnv<JavafxAttrContext> initEnv = memberEnter.initEnv(tree, env);
                    initEnv.info.lint = lint;
                    // In order to catch self-references, we set the variable's
                    // declaration position to maximal possible value, effectively
                    // marking the variable as undefined.
                    v.pos = Position.MAXPOS;
                    initType = attribExpr(tree.init, initEnv, declType);
                    chk.checkType(tree.pos(), initType, declType);
            }
            else
                initType = syms.objectType;  // nothing to go on, so we assume Object
            if (declType == syms.javafx_UnspecifiedType)
                result = tree.type = v.type = initType;
            chk.validateAnnotations(tree.mods.annotations, v);
        }
        finally {
            chk.setLint(prevLint);
        }

        if (tree.getOnChanges() != null) {
            Type elemType = null;

            if (types.erasure(result) == syms.javafx_SequenceTypeErasure) {
                elemType = result.getTypeArguments().head;
            }

            for (JFXAbstractOnChange onc : tree.getOnChanges()) {
                // OK, this is probably a tad hacky, but set the element type for the onChanges
                onc.elementType = elemType;

                if (env.info.scope.owner.kind == TYP) {
                    // var is a static;
                    // let the owner of the environment be a freshly
                    // created BLOCK-method.
                    long flags = tree.getModifiers().flags;
                    JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree, env.info.dup(env.info.scope.dupUnshared()));
                    localEnv.outer = env;
                    localEnv.info.scope.owner = new MethodSymbol(flags | BLOCK, names.empty, null, env.info.scope.owner);
                    if ((flags & STATIC) != 0) {
                        localEnv.info.staticLevel++;
                    }
                    attribStat(onc, localEnv);
                } else {
                    // Create a new local environment with a local scope.
                    JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree, env.info.dup(env.info.scope.dup()));
                    attribStat(onc, localEnv);
                    localEnv.info.scope.leave();
                }
            }
        }
    }
    
    public void visitAbstractOnChange(JFXAbstractOnChange tree) {
	if (tree.getIndex() != null) {
            attribVar(tree.getIndex(), env);
            tree.getIndex().sym.type = syms.intType;
        }
	if (tree.getOldValue() != null) {
            attribVar(tree.getOldValue(), env);  
            tree.getOldValue().sym.type = tree.elementType;
        }
        attribStat(tree.getBody(), env);
    }
    
    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnReplaceElement(JFXOnReplaceElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnInsertElement(JFXOnInsertElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnDeleteElement(JFXOnDeleteElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnDeleteAll(JFXOnDeleteAll tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitForExpression(JFXForExpression tree) {
        JavafxEnv<JavafxAttrContext> forExprEnv =
            env.dup(tree, env.info.dup(env.info.scope.dup()));
        
        for (JFXForExpressionInClause clause : tree.getInClauses()) {
            attribVar(clause.getVar(), forExprEnv);
            Type exprType = types.upperBound(attribExpr(clause.getSequenceExpression(), forExprEnv));
            chk.checkNonVoid(clause.pos(), exprType);
            Type elemtype;
            // must implement Sequence<T>?
            Type base = types.asSuper(exprType, syms.javafx_SequenceType.tsym);
            if (base == null) {
                //TODO: fix error message
                elemtype = syms.errType;
            } else {
                List<Type> iterableParams = base.allparams();
                if (iterableParams.isEmpty()) {
                    elemtype = syms.errType;
                } else {
                    elemtype = types.upperBound(iterableParams.head);
                }
            }
            if (elemtype == syms.errType) {
                log.error(clause.getSequenceExpression().pos(), "foreach.not.applicable.to.type");
            } else {
                // if it is a primitive type, unbox it
                Type unboxed = types.unboxedType(elemtype);
                if (unboxed != Type.noType) {
                    elemtype = unboxed;
                }
            }
            //TODO: this is certainly wrong
            clause.getVar().type = elemtype;
            clause.getVar().sym.type = elemtype;

            chk.checkType(clause.getSequenceExpression().pos(), elemtype, clause.var.sym.type);

            if (clause.getWhereExpression() != null) {
                attribExpr(clause.getWhereExpression(), env, syms.booleanType);
            }
        }

        forExprEnv.tree = tree; // before, we were not in loop!
        attribExpr(tree.getBodyExpression(), forExprEnv);

        Type bodyType = tree.getBodyExpression().type;
        Type owntype = (bodyType == null || bodyType == syms.voidType)? 
            syms.voidType : 
            sequenceType(bodyType);

        forExprEnv.info.scope.leave();
        result = check(tree, owntype, VAL, pkind, pt);
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should not reach here";
    }

    public void visitSkip(JCSkip tree) {
        result = null;
    }

    public void visitBlock(JCBlock tree) {
        if (env.info.scope.owner.kind == TYP) {
            // Block is a static or instance initializer;
            // let the owner of the environment be a freshly
            // created BLOCK-method.
            JavafxEnv<JavafxAttrContext> localEnv =
                env.dup(tree, env.info.dup(env.info.scope.dupUnshared()));
            localEnv.info.scope.owner =
                new MethodSymbol(tree.flags | BLOCK, names.empty, null,
                                 env.info.scope.owner);
            if ((tree.flags & STATIC) != 0) localEnv.info.staticLevel++;
            attribStats(tree.stats, localEnv);
        } else {
            // Create a new local environment with a local scope.
            JavafxEnv<JavafxAttrContext> localEnv =
                env.dup(tree, env.info.dup(env.info.scope.dup()));
            attribStats(tree.stats, localEnv);
            localEnv.info.scope.leave();
        }
        result = null;
    }

    public void visitDoLoop(JCDoWhileLoop tree) {
        attribStat(tree.body, env.dup(tree));
        attribExpr(tree.cond, env, syms.booleanType);
        result = null;
    }

    public void visitWhileLoop(JCWhileLoop tree) {
        attribExpr(tree.cond, env, syms.booleanType);
        attribStat(tree.body, env.dup(tree));
        result = null;
    }

    public void visitForLoop(JCForLoop tree) {
        assert false;
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
        assert false;
    }

    public void visitLabelled(JCLabeledStatement tree) {
        assert false;
    }

    public void visitSwitch(JCSwitch tree) {
        assert false;
    }

    public void visitNewArray(JCNewArray tree) {
        assert false;
    }

    
    public void visitNewClass(JCNewClass tree) {
        assert false : "remove me"; 
    }
    
    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        Type owntype = syms.errType;

        // The local environment of a class creation is
        // a new environment nested in the current one.
        JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree, env.info.dup());

        // The anonymous inner class definition of the new expression,
        // if one is defined by it.
        JFXClassDeclaration cdef = tree.getClassBody();

        // If enclosing class is given, attribute it, and
        // complete class name to be fully qualified
        JCExpression clazz = tree.getIdentifier(); // Class field following new
        JCExpression clazzid =          // Identifier in class field
            (clazz.getTag() == JCTree.TYPEAPPLY)
            ? ((JCTypeApply) clazz).clazz
            : clazz;

        JCExpression clazzid1 = clazzid; // The same in fully qualified form

        // Attribute clazz expression and store
        // symbol + type back into the attributed tree.
        Type clazztype = chk.checkClassType(
            clazz.pos(), attribType(clazz, env), true);
        chk.validate(clazz);
        if (!clazztype.tsym.isInterface() &&
                   clazztype.getEnclosingType().tag == CLASS) {
            // Check for the existence of an apropos outer instance
            rs.resolveImplicitThis(tree.pos(), env, clazztype);
        }

        // Attribute constructor arguments.
        List<Type> argtypes = attribArgs(tree.getArguments(), localEnv);

        // If we have made no mistakes in the class type...
        if (clazztype.tag == CLASS) {
            // Check that class is not abstract
            if (cdef == null &&
                (clazztype.tsym.flags() & (ABSTRACT | INTERFACE)) != 0) {
                log.error(tree.pos(), "abstract.cant.be.instantiated",
                          clazztype.tsym);
            } else if (cdef != null && clazztype.tsym.isInterface()) {
                // Check that no constructor arguments are given to
                // anonymous classes implementing an interface
                if (!argtypes.isEmpty())
                    log.error(tree.getArguments().head.pos(), "anon.class.impl.intf.no.args");


                // Error recovery: pretend no arguments were supplied.
                argtypes = List.nil();
            }

            // Resolve the called constructor under the assumption
            // that we are referring to a superclass instance of the
            // current instance (JLS ???).
            else {
                localEnv.info.selectSuper = cdef != null;
                localEnv.info.varArgs = false;

                /**
                List<Type> emptyTypeargtypes = List.<Type>nil();
                tree.constructor = rs.resolveConstructor(
                    tree.pos(), localEnv, clazztype, argtypes, emptyTypeargtypes);
                Type ctorType = checkMethod(clazztype,
                                            tree.constructor,
                                            localEnv,
                                            tree.getArguments(),
                                            argtypes,
                                            emptyTypeargtypes,
                                            localEnv.info.varArgs);
                if (localEnv.info.varArgs)
                    assert ctorType.isErroneous();
                 * ***/

            }

            if (cdef != null) {
                // We are seeing an anonymous class instance creation.
                // In this case, the class instance creation
                // expression
                //
                //    E.new <typeargs1>C<typargs2>(args) { ... }
                //
                // is represented internally as
                //
                //    E . new <typeargs1>C<typargs2>(args) ( class <empty-name> { ... } )  .
                //
                // This expression is then *transformed* as follows:
                //
                // (1) add a STATIC flag to the class definition
                //     if the current environment is static
                // (2) add an extends or implements clause
                // (3) add a constructor.
                //
                // For instance, if C is a class, and ET is the type of E,
                // the expression
                //
                //    E.new <typeargs1>C<typargs2>(args) { ... }
                //
                // is translated to (where X is a fresh name and typarams is the
                // parameter list of the super constructor):
                //
                //   new <typeargs1>X(<*nullchk*>E, args) where
                //     X extends C<typargs2> {
                //       <typarams> X(ET e, args) {
                //         e.<typeargs1>super(args)
                //       }
                //       ...
                //     }
//               if (JavafxResolve.isStatic(env)) cdef.mods.flags |= STATIC;
                
                // always need to be static, because they will have generated static members
                cdef.mods.flags |= STATIC;

                if (clazztype.tsym.isInterface()) {
                    cdef.implementing = List.of(clazz);
                } else {
                    cdef.extending = clazz;
                }

                attribStat(cdef, localEnv);
                attribClass(cdef.pos(), cdef.sym);

                // Reassign clazztype and recompute constructor.
                clazztype = cdef.sym.type;
                Symbol sym = rs.resolveConstructor(
                    tree.pos(), localEnv, clazztype, argtypes,
                    List.<Type>nil(), true, false);
                
                tree.constructor = sym;
            }

//         if (tree.constructor != null && tree.constructor.kind == MTH)
              owntype = clazz.type;  // this give declared type, where clazztype would give anon type
        }

        //Scope members = owntype.tsym.members();  //TODO: should see new members
        Scope members = clazz.type.tsym.members();
        for (JFXObjectLiteralPart part : tree.getParts()) {
            Symbol memberSym = members.lookup(part.name).sym;
            memberSym = rs.access(memberSym, part.pos(), owntype, part.name, true);
            memberSym.complete();
            attribExpr(part.getExpression(), localEnv, memberSym.type);
            part.type = memberSym.type;
            part.sym = memberSym;
        }

        result = check(tree, owntype, VAL, pkind, pt);
    }

    /** Make an attributed null check tree.
     */
    public JCExpression makeNullCheck(JCExpression arg) {
        // optimization: X.this is never null; skip null check
        Name name = TreeInfo.name(arg);
        if (name == names._this || name == names._super) return arg;

        int optag = JCTree.NULLCHK;
        JCUnary tree = make.at(arg.pos).Unary(optag, arg);
        tree.operator = syms.nullcheck;
        tree.type = arg.type;
        return tree;
    }

    public void visitClassDef(JCClassDecl tree) {
        assert false : "Should never reach here";
    }

    public void visitMethodDef(JCMethodDecl tree) {
        assert false : "Should never reach here";
    }
    
    @Override
    public void visitOperationValue(JFXOperationValue tree) {
        Scope enclScope = enter.enterScope(env);
        JFXOperationDefinition def = new JFXOperationDefinition(make.Modifiers(0), invokeName, tree);
        tree.definition = def;
        MethodSymbol m = new MethodSymbol(0, def.name, null, enclScope.owner);
        // m.flags_field = chk.checkFlags(def.pos(), def.mods.flags, m, def);
        def.sym = m;
        finishOperationDefinition(def, env);
        MethodType mtype = (MethodType) def.type;
        Type rtype = mtype.restype;
        ListBuffer<Type> typarams = new ListBuffer<Type>();
        typarams.append(rtype);
        typarams.appendList(mtype.argtypes);
        result = tree.type = makeFunctionType(typarams, rtype);
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition tree) {
        MethodSymbol m = tree.sym;
        m.complete();
    }

    public void finishOperationDefinition(JFXOperationDefinition tree, JavafxEnv<JavafxAttrContext> env) {
        MethodSymbol m = tree.sym;
        JFXOperationValue opVal = tree.operation;
        JavafxEnv<JavafxAttrContext> localEnv = memberEnter.methodEnv(tree, env);
        Type returnType;
        // Create a new environment with local scope
        // for attributing the method.

        JavafxEnv<JavafxAttrContext> lintEnv = env;
        while (lintEnv.info.lint == null)
            lintEnv = lintEnv.next;

        Lint lint = lintEnv.info.lint.augment(m.attributes_field, m.flags());
        Lint prevLint = chk.setLint(lint);
        try {
            chk.checkDeprecatedAnnotation(tree.pos(), m);

            localEnv.info.lint = lint;

            ClassSymbol owner = env.enclClass.sym;
            if ((owner.flags() & ANNOTATION) != 0 &&
                tree.operation.funParams.nonEmpty())
                log.error(tree.operation.funParams.head.pos(),
                          "intf.annotation.members.cant.have.params");

            // Attribute all value parameters.
            ListBuffer<Type> argbuf = new ListBuffer<Type>();
            for (List<JFXVar> l = tree.getParameters(); l.nonEmpty(); l = l.tail) {
                attribVar(l.head, localEnv);
                argbuf.append(l.head.getJFXType().type);
            }
            returnType = null;
            if (opVal.getJFXReturnType().getTag() != JavafxTag.TYPEUNKNOWN) {
                returnType = attribType(tree.getJFXReturnType(), localEnv);
            }
            MethodType mtype = new MethodType(argbuf.toList(),
                                    returnType, // may be null - i.e. not yet known
                                    List.<Type>nil(),
                                    syms.methodClass);
            m.type = mtype;

            if (tree.getBodyExpression() == null) {
                // Empty bodies are only allowed for
                // abstract, native, or interface methods, or for methods
                // in a retrofit signature class.
                if ((owner.flags() & INTERFACE) == 0 &&
                    (tree.mods.flags & (ABSTRACT | NATIVE)) == 0 &&
                    !relax)
                    log.error(tree.pos(), "missing.meth.body.or.decl.abstract");
                else if (returnType == null)
                    // no body, can't infer, assume Any
                    // FIXME Should this be Void or an error?
                    returnType = syms.javafx_AnyType;
            } else if ((owner.flags() & INTERFACE) != 0) {
                log.error(tree.getBodyExpression().pos(), "intf.meth.cant.have.body");
            } else if ((tree.mods.flags & ABSTRACT) != 0) {
                log.error(tree.pos(), "abstract.meth.cant.have.body");
            } else if ((tree.mods.flags & NATIVE) != 0) {
                log.error(tree.pos(), "native.meth.cant.have.body");
            } else {
                JFXBlockExpression body = opVal.getBodyExpression();
                // Attribute method bodyExpression
                Type bodyType = attribExpr(body, localEnv);
                if (body.value == null) {
                    if (returnType == null)
                        returnType = syms.javafx_VoidType; //TODO: this is wrong if there is a return statement
                } else {
                    if (returnType == null)
                        returnType = bodyType;
                    else if (returnType != syms.javafx_VoidType)
                        chk.checkType(tree.pos(), bodyType, returnType);       
                }
            }
            localEnv.info.scope.leave();

            mtype.restype = returnType;
            result = tree.type = mtype;

            Scope enclScope = enter.enterScope(env);

            // If we override any other methods, check that we do so properly.
            // JLS ???
            if (m.owner instanceof ClassSymbol)
                chk.checkOverride(tree, m);
        }
        finally {
            chk.setLint(prevLint);
        }

        // mark the method varargs, if necessary
        // if (isVarArgs) m.flags_field |= Flags.VARARGS;

        // If types were not set, set them to syms.javafx_AnyType
        // TODO: Can we do some type inference in here?
        if (m != null && m.type != null && ((MethodType)m.type).argtypes != null) {
            for (List<Type> mTypes = ((MethodType)m.type).argtypes; mTypes.nonEmpty(); mTypes = mTypes.tail) {
                if (mTypes.head == null) {
                    mTypes.head = syms.javafx_AnyType;
                } 
            }
        }
    }

    public void visitSynchronized(JCSynchronized tree) {
        chk.checkRefType(tree.pos(), attribExpr(tree.lock, env));
        attribStat(tree.body, env);
        result = null;
    }

    public void visitTry(JCTry tree) {
        // Attribute body
        attribStat(tree.body, env.dup(tree, env.info.dup()));

        // Attribute catch clauses
        for (List<JCCatch> l = tree.catchers; l.nonEmpty(); l = l.tail) {
            JCCatch c = l.head;
            JavafxEnv<JavafxAttrContext> catchEnv =
                env.dup(c, env.info.dup(env.info.scope.dup()));
            Type ctype = attribStat(c.param, catchEnv);
            if (c.param.type.tsym.kind == Kinds.VAR) {
                c.param.sym.setData(ElementKind.EXCEPTION_PARAMETER);
            }
//uses vartype            
//            chk.checkType(c.param.vartype.pos(),
//                          chk.checkClassType(c.param.vartype.pos(), ctype),
//                          syms.throwableType);
            attribStat(c.body, catchEnv);
            catchEnv.info.scope.leave();
        }

        // Attribute finalizer
        if (tree.finalizer != null) attribStat(tree.finalizer, env);
        result = null;
    }

    public void visitConditional(JCConditional tree) {
        attribExpr(tree.cond, env, syms.booleanType);
        attribExpr(tree.truepart, env);
        Type falsepartType;
        if (tree.falsepart == null) {
            falsepartType = tree.truepart.type;
        } else {
            falsepartType = attribExpr(tree.falsepart, env);
        }
        result = check(tree,
                       capture(condType(tree.pos(), tree.cond.type,
                                        tree.truepart.type, falsepartType)),
                       VAL, pkind, pt);
    }
    //where
        /** Compute the type of a conditional expression, after
         *  checking that it exists. See Spec 15.25.
         *
         *  @param pos      The source position to be used for
         *                  error diagnostics.
         *  @param condtype The type of the expression's condition.
         *  @param thentype The type of the expression's then-part.
         *  @param elsetype The type of the expression's else-part.
         */
        private Type condType(DiagnosticPosition pos,
                              Type condtype,
                              Type thentype,
                              Type elsetype) {
            Type ctype = condType1(pos, condtype, thentype, elsetype);

            // If condition and both arms are numeric constants,
            // evaluate at compile-time.
            return ((condtype.constValue() != null) &&
                    (thentype.constValue() != null) &&
                    (elsetype.constValue() != null))
                ? cfolder.coerce(condtype.isTrue()?thentype:elsetype, ctype)
                : ctype;
        }
        /** Compute the type of a conditional expression, after
         *  checking that it exists.  Does not take into
         *  account the special case where condition and both arms
         *  are constants.
         *
         *  @param pos      The source position to be used for error
         *                  diagnostics.
         *  @param condtype The type of the expression's condition.
         *  @param thentype The type of the expression's then-part.
         *  @param elsetype The type of the expression's else-part.
         */
        private Type condType1(DiagnosticPosition pos, Type condtype,
                               Type thentype, Type elsetype) {
            // If same type, that is the result
            if (types.isSameType(thentype, elsetype))
                return thentype.baseType();

            Type thenUnboxed = (!allowBoxing || thentype.isPrimitive())
                ? thentype : types.unboxedType(thentype);
            Type elseUnboxed = (!allowBoxing || elsetype.isPrimitive())
                ? elsetype : types.unboxedType(elsetype);

            // Otherwise, if both arms can be converted to a numeric
            // type, return the least numeric type that fits both arms
            // (i.e. return larger of the two, or return int if one
            // arm is short, the other is char).
            if (thenUnboxed.isPrimitive() && elseUnboxed.isPrimitive()) {
                // If one arm has an integer subrange type (i.e., byte,
                // short, or char), and the other is an integer constant
                // that fits into the subrange, return the subrange type.
                if (thenUnboxed.tag < INT && elseUnboxed.tag == INT &&
                    types.isAssignable(elseUnboxed, thenUnboxed))
                    return thenUnboxed.baseType();
                if (elseUnboxed.tag < INT && thenUnboxed.tag == INT &&
                    types.isAssignable(thenUnboxed, elseUnboxed))
                    return elseUnboxed.baseType();

                for (int i = BYTE; i < VOID; i++) {
                    Type candidate = syms.typeOfTag[i];
                    if (types.isSubtype(thenUnboxed, candidate) &&
                        types.isSubtype(elseUnboxed, candidate))
                        return candidate;
                }
            }

            // Those were all the cases that could result in a primitive
            if (allowBoxing) {
                thentype = boxIfNeeded(thentype);
                elsetype = boxIfNeeded(elsetype);
            }

            if (types.isSubtype(thentype, elsetype))
                return elsetype.baseType();
            if (types.isSubtype(elsetype, thentype))
                return thentype.baseType();

            if (!allowBoxing || thentype.tag == VOID || elsetype.tag == VOID) {
                log.error(pos, "neither.conditional.subtype",
                          thentype, elsetype);
                return thentype.baseType();
            }

            // both are known to be reference types.  The result is
            // lub(thentype,elsetype). This cannot fail, as it will
            // always be possible to infer "Object" if nothing better.
            return types.lub(thentype.baseType(), elsetype.baseType());
        }

    public void visitIf(JCIf tree) {
        attribExpr(tree.cond, env, syms.booleanType);
        attribStat(tree.thenpart, env);
        if (tree.elsepart != null)
            attribStat(tree.elsepart, env);
        chk.checkEmptyIf(tree);
        result = null;
    }

    public void visitExec(JCExpressionStatement tree) {
        attribExpr(tree.expr, env);
        result = null;
    }

    public void visitBreak(JCBreak tree) {
        tree.target = findJumpTarget(tree.pos(), tree.getTag(), tree.label, env);
        result = null;
    }

    public void visitContinue(JCContinue tree) {
        tree.target = findJumpTarget(tree.pos(), tree.getTag(), tree.label, env);
        result = null;
    }
    //where
        /** Return the target of a break or continue statement, if it exists,
         *  report an error if not.
         *  Note: The target of a labelled break or continue is the
         *  (non-labelled) statement tree referred to by the label,
         *  not the tree representing the labelled statement itself.
         *
         *  @param pos     The position to be used for error diagnostics
         *  @param tag     The tag of the jump statement. This is either
         *                 Tree.BREAK or Tree.CONTINUE.
         *  @param label   The label of the jump statement, or null if no
         *                 label is given.
         *  @param env     The environment current at the jump statement.
         */
        private JCTree findJumpTarget(DiagnosticPosition pos,
                                    int tag,
                                    Name label,
                                    JavafxEnv<JavafxAttrContext> env) {
            // Search environments outwards from the point of jump.
            JavafxEnv<JavafxAttrContext> env1 = env;
            LOOP:
            while (env1 != null) {
                switch (env1.tree.getTag()) {
                case JCTree.LABELLED:
                    JCLabeledStatement labelled = (JCLabeledStatement)env1.tree;
                    if (label == labelled.label) {
                        // If jump is a continue, check that target is a loop.
                        if (tag == JCTree.CONTINUE) {
                            if (labelled.body.getTag() != JCTree.DOLOOP &&
                                labelled.body.getTag() != JCTree.WHILELOOP)
                                log.error(pos, "not.loop.label", label);
                            // Found labelled statement target, now go inwards
                            // to next non-labelled tree.
                            return TreeInfo.referencedStatement(labelled);
                        } else {
                            return labelled;
                        }
                    }
                    break;
                case JCTree.WHILELOOP:
                case JavafxTag.FOR_EXPRESSION:
                    if (label == null) return env1.tree;
                    break;
                case JCTree.SWITCH:
                    if (label == null && tag == JCTree.BREAK) return env1.tree;
                    break;
                case JCTree.METHODDEF:
                case JCTree.CLASSDEF:
                    break LOOP;
                default:
                }
                env1 = env1.next;
            }
            if (label != null)
                log.error(pos, "undef.label", label);
            else if (tag == JCTree.CONTINUE)
                log.error(pos, "cont.outside.loop");
            else
                log.error(pos, "break.outside.switch.loop");
            return null;
        }

    @Override
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

    public void visitThrow(JCThrow tree) {
        attribExpr(tree.expr, env, syms.throwableType);
        result = null;
    }

    public void visitAssert(JCAssert tree) {
        attribExpr(tree.cond, env, syms.booleanType);
        if (tree.detail != null) {
            chk.checkNonVoid(tree.detail.pos(), attribExpr(tree.detail, env));
        }
        result = null;
    }

    @Override
    public void visitApply(JCMethodInvocation tree) {
        // The local environment of a method application is
        // a new environment nested in the current one.
        JavafxEnv<JavafxAttrContext> localEnv = env.dup(tree, env.info.dup());

        // The types of the actual method arguments.
        List<Type> argtypes;

        // The types of the actual method type arguments.
        List<Type> typeargtypes = null;

        Name methName = JavafxTreeInfo.name(tree.meth);
        
        {
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
    
    public void visitAssignop(JCAssignOp tree) {
        // Attribute arguments.
        Type owntype = attribTree(tree.lhs, env, VAR, Type.noType);
        Type operand = attribExpr(tree.rhs, env);
        // Find operator.
        Symbol operator = tree.operator = rs.resolveBinaryOperator(
            tree.pos(), tree.getTag() - JCTree.ASGOffset, env,
            owntype, operand);

        if (operator.kind == MTH) {
            chk.checkOperator(tree.pos(),
                              (OperatorSymbol)operator,
                              tree.getTag() - JCTree.ASGOffset,
                              owntype,
                              operand);
            if (types.isSameType(operator.type.getReturnType(), syms.stringType)) {
                // String assignment; make sure the lhs is a string
                chk.checkType(tree.lhs.pos(),
                              owntype,
                              syms.stringType);
            } else {
                chk.checkDivZero(tree.rhs.pos(), operator, operand);
                chk.checkCastable(tree.rhs.pos(),
                                  operator.type.getReturnType(),
                                  owntype);
            }
        }
        result = check(tree, owntype, VAL, pkind, pt);
    }

    public void visitUnary(JCUnary tree) {
        if (tree.getTag() == JavafxTag.SIZEOF) {
             Type argtype = chk.checkNonVoid(tree.arg.pos(), attribExpr(tree.arg, env));
             result = check(tree, syms.javafx_IntegerType, VAL, pkind, pt);
             return;
        }
        // Attribute arguments.
        Type argtype = (JCTree.PREINC <= tree.getTag() && tree.getTag() <= JCTree.POSTDEC)
            ? attribTree(tree.arg, env, VAR, Type.noType)
            : chk.checkNonVoid(tree.arg.pos(), attribExpr(tree.arg, env));

        // Find operator.
        Symbol operator = tree.operator =
            rs.resolveUnaryOperator(tree.pos(), tree.getTag(), env, argtype);

        Type owntype = syms.errType;
        if (operator.kind == MTH) {
            owntype = (JCTree.PREINC <= tree.getTag() && tree.getTag() <= JCTree.POSTDEC)
                ? tree.arg.type
                : operator.type.getReturnType();

        /*** no constants or folding
            int opc = ((OperatorSymbol)operator).opcode;

            // If the argument is constant, fold it.
            if (argtype.constValue() != null) {
                Type ctype = cfolder.fold1(opc, argtype);
                if (ctype != null) {
                    owntype = cfolder.coerce(ctype, owntype);

                    // Remove constant types from arguments to
                    // conserve space. The parser will fold concatenations
                    // of string literals; the code here also
                    // gets rid of intermediate results when some of the
                    // operands are constant identifiers.
                    if (tree.arg.type.tsym == syms.stringType.tsym) {
                        tree.arg.type = syms.stringType;
                    }
                }
            }
         * ****/
        }
        result = check(tree, owntype, VAL, pkind, pt);
    }

    @Override
    public void visitBinary(JCBinary tree) {
        // Attribute arguments.
        Type left = chk.checkNonVoid(tree.lhs.pos(), attribExpr(tree.lhs, env));
        Type right = chk.checkNonVoid(tree.rhs.pos(), attribExpr(tree.rhs, env));
// Javafx change
        if (left == syms.javafx_UnspecifiedType) {
            left = setEffectiveExpressionType(tree.lhs, newTypeFromType(getEffectiveExpressionType(right)));
        }
        else if (right == syms.javafx_UnspecifiedType) {
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
    
    public void visitLiteral(JCLiteral tree) {
        result = check(
            tree, litType(tree.typetag), VAL, pkind, pt);
    }
    //where
    /** Return the type of a literal with given type tag.
     */
    Type litType(int tag) {
        return (tag == TypeTags.CLASS) ? syms.stringType : syms.typeOfTag[tag];
    }

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        result = check(tree, syms.typeOfTag[tree.typetag], TYP, pkind, pt);
    }

    public void visitTypeArray(JCArrayTypeTree tree) {
        Type etype = attribType(tree.elemtype, env);
        Type type = new ArrayType(etype, syms.arrayClass);
        result = check(tree, type, TYP, pkind, pt);
    }

    /** Visitor method for parameterized types.
     *  Bound checking is left until later, since types are attributed
     *  before supertype structure is completely known
     */
    public void visitTypeApply(JCTypeApply tree) {
        Type owntype = syms.errType;

        // Attribute functor part of application and make sure it's a class.
        Type clazztype = chk.checkClassType(tree.clazz.pos(), attribType(tree.clazz, env));

        // Attribute type parameters
        List<Type> actuals = attribTypes(tree.arguments, env);

        if (clazztype.tag == CLASS) {
            List<Type> formals = clazztype.tsym.type.getTypeArguments();

            if (actuals.length() == formals.length()) {
                List<Type> a = actuals;
                List<Type> f = formals;
                while (a.nonEmpty()) {
                    a.head = a.head.withTypeVar(f.head);
                    a = a.tail;
                    f = f.tail;
                }
                // Compute the proper generic outer
                Type clazzOuter = clazztype.getEnclosingType();
                if (clazzOuter.tag == CLASS) {
                    Type site;
                    if (tree.clazz.getTag() == JCTree.IDENT) {
                        site = env.enclClass.sym.type;
                    } else if (tree.clazz.getTag() == JCTree.SELECT) {
                        site = ((JCFieldAccess) tree.clazz).selected.type;
                    } else throw new AssertionError(""+tree);
                    if (clazzOuter.tag == CLASS && site != clazzOuter) {
                        if (site.tag == CLASS)
                            site = types.asOuterSuper(site, clazzOuter.tsym);
                        if (site == null)
                            site = types.erasure(clazzOuter);
                        clazzOuter = site;
                    }
                }
                owntype = new ClassType(clazzOuter, actuals, clazztype.tsym);
            } else {
                if (formals.length() != 0) {
                    log.error(tree.pos(), "wrong.number.type.args",
                              Integer.toString(formals.length()));
                } else {
                    log.error(tree.pos(), "type.doesnt.take.params", clazztype.tsym);
                }
                owntype = syms.errType;
            }
        }
        result = check(tree, owntype, TYP, pkind, pt);
    }

    public void visitTypeParameter(JCTypeParameter tree) {
        assert false;
    }

    public void visitWildcard(JCWildcard tree) {
        assert false;
    }

    public void visitAnnotation(JCAnnotation tree) {
        log.error(tree.pos(), "annotation.not.valid.for.type", pt);
        result = tree.type = syms.errType;
    }

    public void visitErroneous(JCErroneous tree) {
        if (tree.errs != null)
            for (JCTree err : tree.errs)
                attribTree(err, env, ERR, pt);
        result = tree.type = syms.errType;
    }

     @Override
     public void visitTree(JCTree tree) {
         if (tree instanceof JFXBlockExpression)
             visitBlockExpression((JFXBlockExpression) tree);
         else
             super.visitTree(tree);
    }

    /** Main method: attribute class definition associated with given class symbol.
     *  reporting completion failures at the given position.
     *  @param pos The source position at which completion errors are to be
     *             reported.
     *  @param c   The class symbol whose definition will be attributed.
     */
    public void attribClass(DiagnosticPosition pos, ClassSymbol c) {
        try {
            annotate.flush();
            attribClass(c);
        } catch (CompletionFailure ex) {
            chk.completionError(pos, ex);
        }
    }

    /** Attribute class definition associated with given class symbol.
     *  @param c   The class symbol whose definition will be attributed.
     */
    void attribClass(ClassSymbol c) throws CompletionFailure {
        if (c.type.tag == ERROR) return;

        // Check for cycles in the inheritance graph, which can arise from
        // ill-formed class files.
        chk.checkNonCyclic(null, c.type);

        Type st = types.supertype(c.type);
        if ((c.flags_field & Flags.COMPOUND) == 0) {
            // First, attribute superclass.
            if (st.tag == CLASS)
                attribClass((ClassSymbol)st.tsym);

            // Next attribute owner, if it is a class.
            if (c.owner.kind == TYP && c.owner.type.tag == CLASS)
                attribClass((ClassSymbol)c.owner);
        }

        // The previous operations might have attributed the current class
        // if there was a cycle. So we test first whether the class is still
        // UNATTRIBUTED.
        if ((c.flags_field & UNATTRIBUTED) != 0) {
            c.flags_field &= ~UNATTRIBUTED;

            // Get environment current at the point of class definition.
            JavafxEnv<JavafxAttrContext> env = enter.typeEnvs.get(c);

            // The info.lint field in the envs stored in enter.typeEnvs is deliberately uninitialized,
            // because the annotations were not available at the time the env was created. Therefore,
            // we look up the environment chain for the first enclosing environment for which the
            // lint value is set. Typically, this is the parent env, but might be further if there
            // are any envs created as a result of TypeParameter nodes.
            JavafxEnv<JavafxAttrContext> lintEnv = env;
            while (lintEnv.info.lint == null)
                lintEnv = lintEnv.next;

            // Having found the enclosing lint value, we can initialize the lint value for this class
            env.info.lint = lintEnv.info.lint.augment(c.attributes_field, c.flags());

            Lint prevLint = chk.setLint(env.info.lint);
            JavaFileObject prev = log.useSource(c.sourcefile);

            try {
                // java.lang.Enum may not be subclassed by a non-enum
                if (st.tsym == syms.enumSym &&
                    ((c.flags_field & (Flags.ENUM|Flags.COMPOUND)) == 0))
                    log.error(env.tree.pos(), "enum.no.subclassing");

                // Enums may not be extended by source-level classes
                if (st.tsym != null &&
                    ((st.tsym.flags_field & Flags.ENUM) != 0) &&
                    ((c.flags_field & Flags.ENUM) == 0) &&
                    !target.compilerBootstrap(c)) {
                    log.error(env.tree.pos(), "enum.types.not.extensible");
                }
                attribClassBody(env, c);

                chk.checkDeprecatedAnnotation(env.tree.pos(), c);
            } finally {
                log.useSource(prev);
                chk.setLint(prevLint);
            }

        }
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

// Begin JavaFX trees
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        // Local classes have not been entered yet, so we need to do it now:
        if ((env.info.scope.owner.kind & (VAR | MTH)) != 0)
            enter.classEnter(tree, env);

        ClassSymbol c = tree.sym;
        if (c == null) {
            // exit in case something drastic went wrong during enter.
            result = null;
        } else {
            // make sure class has been completed:
            c.complete();

            // If this class appears as an anonymous class
            // in a superclass constructor call where
            // no explicit outer instance is given,
            // disable implicit outer instance from being passed.
            // (This would be an illegal access to "this before super").
            if (env.info.isSelfCall &&
                env.tree.getTag() == JCTree.NEWCLASS &&
                ((JCNewClass) env.tree).encl == null)
            {
                c.flags_field |= NOOUTERTHIS;
            }
            attribClass(tree.pos(), c);

            for (JCExpression superClass : tree.supertypes) {
                attribType(superClass, env);
            }

            result = tree.type = c.type;
        }
        
        initBuilder.addFxClass(c, tree);
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitDoLater(JFXDoLater that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        boolean isSeq = false;
        if (pt.tag != NONE && !(isSeq = isSequence(pt)) && !pSequencePermitted) {
            log.error(tree.pos(), "array.req.but.found", pt); //TODO: msg
            result = syms.errType;
        } else {
            Type owntype = pt.tag == NONE? pt : isSeq? pt : sequenceType(pt);
            result = check(tree, owntype, VAL, pkind, Type.noType);
        }
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        Type lowerType =  attribExpr(tree.getLower(), env);        
        Type upperType = attribExpr(tree.getUpper(), env);
        Type stepType = tree.getStepOrNull() == null? syms.javafx_IntegerType : attribExpr(tree.getStepOrNull(), env);
        boolean allInt = true;
        if (lowerType != syms.javafx_IntegerType) {
            allInt = false;
            if (lowerType != syms.javafx_NumberType) {
                log.error(tree.getLower().pos(), "range.start.int.or.number"); 
            }
        }
        if (upperType != syms.javafx_IntegerType) {
            allInt = false;
            if (upperType != syms.javafx_NumberType) {
                log.error(tree.getLower().pos(), "range.end.int.or.number"); 
            }
        }
        if (stepType != syms.javafx_IntegerType) {
            allInt = false;
            if (stepType != syms.javafx_NumberType) {
                log.error(tree.getUpper().pos(), "range.step.int.or.number"); 
            }
        }
        if (!allInt && tree.getStepOrNull() == null) {
            log.error(tree.pos(), "range.step.required.number"); 
        }
        Type owntype = sequenceType(allInt? syms.javafx_IntegerType : syms.javafx_NumberType);
        Type constraintType;
        if (isSequence(pt)) {
            constraintType = pt;
        } else if (pt.tag == NONE) {
            constraintType = pt;
        } else {
            // pt is a specified type, but not a sequence
            if (pSequencePermitted) {
                constraintType = sequenceType(pt);
            } else {
                // error -- check will generate error
                constraintType = pt;
            }
        }
        result = check(tree, owntype, VAL, pkind, constraintType);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        // atrribute the items, and determine least upper bound of type
        Type elemType = null;
        Type constraintType;
        if (isSequence(pt)) {
            constraintType = pt;
            elemType = elementType(pt);
        } else if (pt.tag == NONE || pt == syms.javafx_UnspecifiedType) {
            constraintType = pt;
            // we don't know what type we are supposed to be, try to infer it
            for (JCExpression expr : tree.getItems()) {
                Type itemType = attribExpr(expr, env);
                if (itemType.tag == NONE || itemType.tag == ERROR) {
                    continue;
                }
                if (isSequence(itemType)) {
                    itemType = elementType(itemType);
                }
                if (elemType == null) {
                    elemType = itemType;
                } else if (!itemType.baseType().equals(elemType.baseType())) {
                    if ((itemType == syms.javafx_IntegerType && elemType == syms.javafx_NumberType) 
                     || (itemType == syms.javafx_NumberType && elemType == syms.javafx_IntegerType)) {
                        elemType = syms.javafx_NumberType; // number and int go to number
                    } else if (itemType.isPrimitive() || elemType.isPrimitive()) {
                        elemType = syms.javafx_AnyType; // only place to go is to Object
                    } else {
                        elemType = syms.javafx_AnyType; //TODO: punt for now
                    }
                }
            }
        } else {
            // pt is a specified type, but not a sequence
            if (pSequencePermitted) {
                constraintType = sequenceType(pt);
                elemType = pt;  
            } else {
                // error
                constraintType = Type.noType;
            }
        }
        if (elemType != null) {
            // attribute the items
           for (JCExpression expr : tree.getItems()) {
                chk.checkNonVoid(expr.pos(), attribExpr(expr, env, elemType, true));
           }
           Type owntype = sequenceType(elemType);
           result = check(tree, owntype, VAL, pkind, constraintType);
        } else {
            // because of nested sequences, we can't test here
            // log.error(tree.pos(), "array.req.but.found", pt); //TODO: msg
            result = syms.errType;
        }
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        Type seqType = attribExpr(tree.getSequence(), env);
        //TODO: check that it is a sequence
        attribExpr(tree.getIndex(), env, syms.javafx_IntegerType);
        Type owntype = seqType.getTypeArguments().head;
        Type unboxed = types.unboxedType(owntype);
        if (unboxed != Type.noType) {
            owntype = unboxed;
        }
        result = check(tree, owntype, VAR, pkind, pt);
    }
    
    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        Type seqType = attribTree(tree.getSequence(), env, VAR, syms.javafx_SequenceTypeErasure); 
        Type elemType = seqType.getTypeArguments().head;
        Type unboxed = types.unboxedType(elemType);
        if (unboxed != Type.noType) {
            elemType = unboxed;
        }
        attribExpr(tree.getElement(), env, elemType);
        result = null;
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        if (tree.getElement() == null) {
            if (tree.getSequence() instanceof JFXSequenceIndexed) { 
                // delete seq[index];
                JFXSequenceIndexed seqInd = (JFXSequenceIndexed)tree.getSequence();
                JCExpression seq = seqInd.getSequence();
                JCExpression index = seqInd.getIndex();
                tree.resetSequenceAndIndex(seq, index);
                attribTree(tree.getSequence(), env, VAR, syms.javafx_SequenceTypeErasure); 
                attribExpr(index, env, syms.javafx_IntegerType);
            } else {
                // delete seq;   // that is, all the elements
                attribTree(tree.getSequence(), env, VAR, syms.javafx_SequenceTypeErasure); 
            }
        } else {
            Type seqType = attribTree(tree.getSequence(), env, VAR, syms.javafx_SequenceTypeErasure); 
            Type elemType = seqType.getTypeArguments().head;
            Type unboxed = types.unboxedType(elemType);
            if (unboxed != Type.noType) {
                elemType = unboxed;
            }
            attribExpr(tree.getElement(), env, elemType);
        }
        result = null;
    }
    
    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        List<JCExpression> parts = tree.getParts();
        attribExpr(parts.head, env, syms.javafx_StringType);
        parts = parts.tail;
        while (parts.nonEmpty()) {
            attribExpr(parts.head, env, syms.javafx_StringType);
            parts = parts.tail;
            attribExpr(parts.head, env, Type.noType);
            parts = parts.tail;
            attribExpr(parts.head, env, syms.javafx_StringType);
            parts = parts.tail;
        }
        result = check(tree, syms.javafx_StringType, VAL, pkind, pt);
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
    }
    
    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should not reach here";
        result = syms.errType;
    }  
    
    @Override
    public void visitTypeAny(JFXTypeAny tree) {
        assert false : "MUST IMPLEMENT";
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass tree) {
        Type type = null;
        JCExpression classNameExpr = ((JFXTypeClass) tree).getClassName();
        if (classNameExpr instanceof JCIdent) {
            Name className = ((JCIdent) classNameExpr).getName();
            if (className == numberTypeName) {
                type = syms.javafx_NumberType;
            } else if (className == integerTypeName) {
                type = syms.javafx_IntegerType;
            } else if (className == booleanTypeName) {
                type = syms.javafx_BooleanType;
            } else if (className == voidTypeName) {
                type = syms.javafx_VoidType;
            } else if (className == stringTypeName) {
                type = syms.javafx_StringType;
            }
        }
        if (type == null) {
            type = attribType(classNameExpr, env);
        }
        type = sequenceType(type, tree.getCardinality());
        tree.type = type;
        result = type;
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional tree) {
        Type restype = attribType(tree.restype, env);
        Type rtype = new WildcardType(boxIfNeeded(restype), BoundKind.EXTENDS, syms.boundClass);

        ListBuffer<Type> typarams = new ListBuffer<Type>();
        typarams.append(rtype);
        int nargs = 0;
        for (JFXType param : tree.params) {
            Type ptype = boxIfNeeded(attribType(param, env));
            ptype = new WildcardType(ptype, BoundKind.SUPER, syms.boundClass);
            typarams.append(ptype);
            nargs++;
        }
        Type type = makeFunctionType(typarams, restype);
        type = sequenceType(type, tree.getCardinality());
        tree.type = type;
        result = type; 
    }
    
    FunctionType makeFunctionType(ListBuffer<Type> typarams, Type restype) {
        int nargs = typarams.size()-1;
        assert nargs <= syms.MAX_FIXED_PARAM_LENGTH
                : "NOT IMPLEMENTED - functions with >"+syms.MAX_FIXED_PARAM_LENGTH+" parameters";
        Type funtype = syms.javafx_FunctionTypes[nargs];
        return new FunctionType(funtype.getEnclosingType(), typarams.toList(), funtype.tsym, restype);
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown tree) {
        result = tree.type = syms.javafx_UnspecifiedType;
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression tree) {
        // Create a new local environment with a local scope.
        JavafxEnv<JavafxAttrContext> localEnv =
                env.dup(tree, env.info.dup(env.info.scope.dup()));
        localEnv.outer = env;
        memberEnter.memberEnter(tree.stats, localEnv);
        for (List<JCStatement> l = tree.stats; l.nonEmpty(); l = l.tail)
            attribStat(l.head, localEnv);
        Type owntype = null;
        if (tree.value != null) {
            owntype = attribExpr(tree.value, localEnv);
        }
        if (owntype == null) {
            owntype = syms.voidType;
        }
        result = check(tree, owntype, VAL, pkind, pt);
        localEnv.info.scope.leave();
    }

    boolean isSequence(Type type) {
        return type != Type.noType && type != null 
                && type.tag != ERROR 
                && type.tag != METHOD && type.tag != FORALL
                && types.erasure(type) == syms.javafx_SequenceTypeErasure;
    }
    
    Type elementType(Type seqType) {
        Type elemType =seqType.getTypeArguments().head;
        Type unboxed = types.unboxedType(elemType);
        if (unboxed.tag != NONE) {
            elemType = unboxed;
        }
        return elemType;
    }
    
    Type sequenceType(Type elemType, int cardinality) {
        return cardinality == JFXType.CARDINALITY_ANY 
                ? sequenceType(elemType)
                : elemType;
    }

    Type sequenceType(Type elemType) {
        if (elemType.isPrimitive()) {
            elemType = types.boxedClass(elemType).type;
        }
        Type seqtype = syms.javafx_SequenceType;
        List<Type> actuals = List.of(elemType);
        Type clazzOuter = seqtype.getEnclosingType();
        return new ClassType(clazzOuter, actuals, seqtype.tsym);
    }
    
    // Javafx change
        protected
// Javafx change
        /** Determine type of identifier or select expression and check that
         *  (1) the referenced symbol is not deprecated
         *  (2) the symbol's type is safe (@see checkSafe)
         *  (3) if symbol is a variable, check that its type and kind are
         *      compatible with the prototype and protokind.
         *  (4) if symbol is an instance field of a raw type,
         *      which is being assigned to, issue an unchecked warning if its
         *      type changes under erasure.
         *  (5) if symbol is an instance method of a raw type, issue an
         *      unchecked warning if its argument types change under erasure.
         *  If checks succeed:
         *    If symbol is a constant, return its constant type
         *    else if symbol is a method, return its result type
         *    otherwise return its type.
         *  Otherwise return errType.
         *
         *  @param tree       The syntax tree representing the identifier
         *  @param site       If this is a select, the type of the selected
         *                    expression, otherwise the type of the current class.
         *  @param sym        The symbol representing the identifier.
         *  @param env        The current environment.
         *  @param pkind      The set of expected kinds.
         *  @param pt         The expected type.
         */
        Type checkId(JCTree tree,
                     Type site,
                     Symbol sym,
                     JavafxEnv<JavafxAttrContext> env,
                     int pkind,
                     Type pt,
                     boolean useVarargs) {
            if (pt.isErroneous()) return syms.errType;
            Type owntype; // The computed type of this identifier occurrence.
            switch (sym.kind) {
            case TYP:
                // For types, the computed type equals the symbol's type,
                // except for two situations:
                owntype = sym.type;
                if (owntype.tag == CLASS) {
                    Type ownOuter = owntype.getEnclosingType();

                    // (a) If the symbol's type is parameterized, erase it
                    // because no type parameters were given.
                    // We recover generic outer type later in visitTypeApply.
                    if (owntype.tsym.type.getTypeArguments().nonEmpty()) {
                        owntype = types.erasure(owntype);
                    }

                    // (b) If the symbol's type is an inner class, then
                    // we have to interpret its outer type as a superclass
                    // of the site type. Example:
                    //
                    // class Tree<A> { class Visitor { ... } }
                    // class PointTree extends Tree<Point> { ... }
                    // ...PointTree.Visitor...
                    //
                    // Then the type of the last expression above is
                    // Tree<Point>.Visitor.
                    else if (ownOuter.tag == CLASS && site != ownOuter) {
                        Type normOuter = site;
                        if (normOuter.tag == CLASS)
                            normOuter = types.asEnclosingSuper(site, ownOuter.tsym);
                        if (normOuter == null) // perhaps from an import
                            normOuter = types.erasure(ownOuter);
                        if (normOuter != ownOuter)
                            owntype = new ClassType(
                                normOuter, List.<Type>nil(), owntype.tsym);
                    }
                }
                break;
            case VAR:
                VarSymbol v = (VarSymbol)sym;
                // Test (4): if symbol is an instance field of a raw type,
                // which is being assigned to, issue an unchecked warning if
                // its type changes under erasure.
                if (allowGenerics &&
                    pkind == VAR &&
                    v.owner.kind == TYP &&
                    (v.flags() & STATIC) == 0 &&
                    (site.tag == CLASS || site.tag == TYPEVAR)) {
                    Type s = types.asOuterSuper(site, v.owner);
                    if (s != null &&
                        s.isRaw() &&
                        !types.isSameType(v.type, v.erasure(types))) {
                        chk.warnUnchecked(tree.pos(),
                                          "unchecked.assign.to.var",
                                          v, s);
                    }
                }
                // The computed type of a variable is the type of the
                // variable symbol, taken as a member of the site type.
                owntype = (sym.owner.kind == TYP &&
                           sym.name != names._this && sym.name != names._super)
                    ? types.memberType(site, sym)
                    : sym.type;

                if (env.info.tvars.nonEmpty()) {
                    Type owntype1 = new ForAll(env.info.tvars, owntype);
                    for (List<Type> l = env.info.tvars; l.nonEmpty(); l = l.tail)
                        if (!owntype.contains(l.head)) {
                            log.error(tree.pos(), "undetermined.type", owntype1);
                            owntype1 = syms.errType;
                        }
                    owntype = owntype1;
                }

                // If the variable is a constant, record constant value in
                // computed type.
                //if (v.getConstValue() != null && isStaticReference(tree))
                //    owntype = owntype.constType(v.getConstValue());

                if (pkind == VAL) {
                    owntype = capture(owntype); // capture "names as expressions"
                }
                break;
            case MTH: {
                JCMethodInvocation app = (JCMethodInvocation)env.tree;
                owntype = checkMethod(site, sym, env, app.args,
                                      pt.getParameterTypes(), pt.getTypeArguments(),
                                      env.info.varArgs);
                break;
            }
            case PCK: case ERR:
                owntype = sym.type;
                break;
            default:
                throw new AssertionError("unexpected kind: " + sym.kind +
                                         " in tree " + tree);
            }

            // Test (1): emit a `deprecation' warning if symbol is deprecated.
            // (for constructors, the error was given when the constructor was
            // resolved)
            if (sym.name != names.init &&
                (sym.flags() & DEPRECATED) != 0 &&
                (env.info.scope.owner.flags() & DEPRECATED) == 0 &&
                sym.outermostClass() != env.info.scope.owner.outermostClass())
                chk.warnDeprecated(tree.pos(), sym);

            if ((sym.flags() & PROPRIETARY) != 0)
                log.strictWarning(tree.pos(), "sun.proprietary", sym);

            // Test (3): if symbol is a variable, check that its type and
            // kind are compatible with the prototype and protokind.
            return check(tree, owntype, sym.kind, pkind, pt);
        }

        /** Check that variable is initialized and evaluate the variable's
         *  initializer, if not yet done. Also check that variable is not
         *  referenced before it is defined.
         *  @param tree    The tree making up the variable reference.
         *  @param env     The current environment.
         *  @param v       The variable's symbol.
         */
        private void checkInit(JCTree tree,
                               JavafxEnv<JavafxAttrContext> env,
                               VarSymbol v,
                               boolean onlyWarning) {
//          System.err.println(v + " " + ((v.flags() & STATIC) != 0) + " " +
//                             tree.pos + " " + v.pos + " " +
//                             Resolve.isStatic(env));//DEBUG

            // A forward reference is diagnosed if the declaration position
            // of the variable is greater than the current tree position
            // and the tree and variable definition occur in the same class
            // definition.  Note that writes don't count as references.
            // This check applies only to class and instance
            // variables.  Local variables follow different scope rules,
            // and are subject to definite assignment checking.
            if (v.pos > tree.pos &&
                v.owner.kind == TYP &&
                canOwnInitializer(env.info.scope.owner) &&
                v.owner == env.info.scope.owner.enclClass() &&
                ((v.flags() & STATIC) != 0) == JavafxResolve.isStatic(env) &&
                (env.tree.getTag() != JCTree.ASSIGN ||
                 TreeInfo.skipParens(((JCAssign) env.tree).lhs) != tree)) {
// Javafx change
                // In JXF is OK to forward reference an attribute even if it's not initialized yet!
//                if (!onlyWarning || isNonStaticEnumField(v)) {
//                    log.error(tree.pos(), "illegal.forward.ref");
//                
//                } else if (useBeforeDeclarationWarning) {
//                    log.warning(tree.pos(), "forward.ref", v);
//                }
// Javafx change end
            }

            v.getConstValue(); // ensure initializer is evaluated

            checkEnumInitializer(tree, env, v);
        }

    /** Is given blank final variable assignable, i.e. in a scope where it
     *  may be assigned to even though it is final?
     *  @param v      The blank final variable.
     *  @param env    The current environment.
     */
    boolean isAssignableAsBlankFinal(VarSymbol v, JavafxEnv<JavafxAttrContext> env) {
        Symbol owner = env.info.scope.owner;
           // owner refers to the innermost variable, method or
           // initializer block declaration at this point.
        return
            v.owner == owner
            ||
            ((owner.name == names.init ||    // i.e. we are in a constructor
              owner.kind == VAR ||           // i.e. we are in a variable initializer
              (owner.flags() & BLOCK) != 0)  // i.e. we are in an initializer block
             &&
             v.owner == owner.owner
             &&
             ((v.flags() & STATIC) != 0) == JavafxResolve.isStatic(env));
    }

    /** Check that variable can be assigned to.
     *  @param pos    The current source code position.
     *  @param v      The assigned varaible
     *  @param base   If the variable is referred to in a Select, the part
     *                to the left of the `.', null otherwise.
     *  @param env    The current environment.
     */
    void checkAssignable(DiagnosticPosition pos, VarSymbol v, JCTree base, JavafxEnv<JavafxAttrContext> env) {
        if ((v.flags() & FINAL) != 0 &&
            ((v.flags() & HASINIT) != 0
             ||
             !((base == null ||
               (base.getTag() == JCTree.IDENT && TreeInfo.name(base) == names._this)) &&
               isAssignableAsBlankFinal(v, env)))) {
            log.error(pos, "cant.assign.val.to.final.var", v);
        }
    }

        /**
         * Check for illegal references to static members of enum.  In
         * an enum type, constructors and initializers may not
         * reference its static members unless they are constant.
         *
         * @param tree    The tree making up the variable reference.
         * @param env     The current environment.
         * @param v       The variable's symbol.
         * @see JLS 3rd Ed. (8.9 Enums)
         */
        private void checkEnumInitializer(JCTree tree, JavafxEnv<JavafxAttrContext> env, VarSymbol v) {
            // JLS 3rd Ed.:
            //
            // "It is a compile-time error to reference a static field
            // of an enum type that is not a compile-time constant
            // (15.28) from constructors, instance initializer blocks,
            // or instance variable initializer expressions of that
            // type. It is a compile-time error for the constructors,
            // instance initializer blocks, or instance variable
            // initializer expressions of an enum constant e to refer
            // to itself or to an enum constant of the same type that
            // is declared to the right of e."
            if (isNonStaticEnumField(v)) {
                ClassSymbol enclClass = env.info.scope.owner.enclClass();

                if (enclClass == null || enclClass.owner == null)
                    return;

                // See if the enclosing class is the enum (or a
                // subclass thereof) declaring v.  If not, this
                // reference is OK.
                if (v.owner != enclClass && !types.isSubtype(enclClass.type, v.owner.type))
                    return;

                // If the reference isn't from an initializer, then
                // the reference is OK.
                if (!JavafxResolve.isInitializer(env))
                    return;

                log.error(tree.pos(), "illegal.enum.static.ref");
            }
        }

        private boolean isNonStaticEnumField(VarSymbol v) {
            return Flags.isEnum(v.owner) && Flags.isStatic(v) && !Flags.isConstant(v);
        }

        /** Can the given symbol be the owner of code which forms part
         *  if class initialization? This is the case if the symbol is
         *  a type or field, or if the symbol is the synthetic method.
         *  owning a block.
         */
        private boolean canOwnInitializer(Symbol sym) {
            return
                (sym.kind & (VAR | TYP)) != 0 ||
                (sym.kind == MTH && (sym.flags() & BLOCK) != 0);
        }

    Warner noteWarner = new Warner();

    /**
     * Check that method arguments conform to its instantation.
     **/
    public Type checkMethod(Type site,
                            Symbol sym,
                            JavafxEnv<JavafxAttrContext> env,
                            final List<JCExpression> argtrees,
                            List<Type> argtypes,
                            List<Type> typeargtypes,
                            boolean useVarargs) {
        // Test (5): if symbol is an instance method of a raw type, issue
        // an unchecked warning if its argument types change under erasure.
        if (allowGenerics &&
            (sym.flags() & STATIC) == 0 &&
            (site.tag == CLASS || site.tag == TYPEVAR)) {
            Type s = types.asOuterSuper(site, sym.owner);
            if (s != null && s.isRaw() &&
                !types.isSameTypes(sym.type.getParameterTypes(),
                                   sym.erasure(types).getParameterTypes())) {
                chk.warnUnchecked(env.tree.pos(),
                                  "unchecked.call.mbr.of.raw.type",
                                  sym, s);
            }
        }

        // Compute the identifier's instantiated type.
        // For methods, we need to compute the instance type by
        // Resolve.instantiate from the symbol's type as well as
        // any type arguments and value arguments.
        noteWarner.warned = false;
        Type owntype = rs.instantiate(env,
                                      site,
                                      sym,
                                      argtypes,
                                      typeargtypes,
                                      true,
                                      useVarargs,
                                      noteWarner);
        boolean warned = noteWarner.warned;

        // If this fails, something went wrong; we should not have
        // found the identifier in the first place.
        if (owntype == null) {
            if (!pt.isErroneous())
                log.error(env.tree.pos(),
                          "internal.error.cant.instantiate",
                          sym, site,
                          Type.toString(pt.getParameterTypes()));
            owntype = syms.errType;
        } else {
            // System.out.println("call   : " + env.tree);
            // System.out.println("method : " + owntype);
            // System.out.println("actuals: " + argtypes);
            List<Type> formals = owntype.getParameterTypes();
            Type last = useVarargs ? formals.last() : null;
            if (sym.name==names.init &&
                sym.owner == syms.enumSym)
                formals = formals.tail.tail;
            List<JCExpression> args = argtrees;
            while (formals.head != last) {
                JCTree arg = args.head;
                Warner warn = chk.convertWarner(arg.pos(), arg.type, formals.head);
                assertConvertible(arg, arg.type, formals.head, warn);
                warned |= warn.warned;
                args = args.tail;
                formals = formals.tail;
            }
            if (useVarargs) {
                Type varArg = types.elemtype(last);
                while (args.tail != null) {
                    JCTree arg = args.head;
                    Warner warn = chk.convertWarner(arg.pos(), arg.type, varArg);
                    assertConvertible(arg, arg.type, varArg, warn);
                    warned |= warn.warned;
                    args = args.tail;
                }
            } else if ((sym.flags() & VARARGS) != 0 && allowVarargs) {
                // non-varargs call to varargs method
                Type varParam = owntype.getParameterTypes().last();
                Type lastArg = argtypes.last();
                if (types.isSubtypeUnchecked(lastArg, types.elemtype(varParam)) &&
                    !types.isSameType(types.erasure(varParam), types.erasure(lastArg)))
                    log.warning(argtrees.last().pos(), "inexact.non-varargs.call",
                                types.elemtype(varParam),
                                varParam);
            }

            if (warned && sym.type.tag == FORALL) {
                String typeargs = "";
                if (typeargtypes != null && typeargtypes.nonEmpty()) {
                    typeargs = "<" + Type.toString(typeargtypes) + ">";
                }
                chk.warnUnchecked(env.tree.pos(),
                                  "unchecked.meth.invocation.applied",
                                  sym,
                                  sym.location(),
                                  typeargs,
                                  Type.toString(argtypes));
                owntype = new MethodType(owntype.getParameterTypes(),
                                         types.erasure(owntype.getReturnType()),
                                         owntype.getThrownTypes(),
                                         syms.methodClass);
            }
            if (useVarargs) {
                JCTree tree = env.tree;
                Type argtype = owntype.getParameterTypes().last();
                if (!types.isReifiable(argtype))
                    chk.warnUnchecked(env.tree.pos(),
                                      "unchecked.generic.array.creation",
                                      argtype);
                Type elemtype = types.elemtype(argtype);
                switch (tree.getTag()) {
                case JCTree.APPLY:
                    ((JCMethodInvocation) tree).varargsElement = elemtype;
                    break;
                case JCTree.NEWCLASS:
                    ((JCNewClass) tree).varargsElement = elemtype;
                    break;
                default:
                    throw new AssertionError(""+tree);
                }
            }
        }
        return owntype;
    }

    private void assertConvertible(JCTree tree, Type actual, Type formal, Warner warn) {
        if (types.isConvertible(actual, formal, warn))
            return;

        if (formal.isCompound()
            && types.isSubtype(actual, types.supertype(formal))
            && types.isSubtypeUnchecked(actual, types.interfaces(formal), warn))
            return;

        if (false) {
            // TODO: make assertConvertible work
            chk.typeError(tree.pos(), JCDiagnostic.fragment("incompatible.types"), actual, formal);
            throw new AssertionError("Tree: " + tree
                                     + " actual:" + actual
                                     + " formal: " + formal);
        }
    }

    // Javafx change
        protected
// Javafx change
        /** Obtain a method type with given argument types.
         */
        Type newMethTemplate(List<Type> argtypes, List<Type> typeargtypes) {
            MethodType mt = new MethodType(argtypes, null, null, syms.methodClass);
            return (typeargtypes == null) ? mt : (Type)new ForAll(typeargtypes, mt);
        }

    public void visitImport(JCImport tree) {
        // nothing to do
    }

    /** Finish the attribution of a class. */
    public void attribClassBody(JavafxEnv<JavafxAttrContext> env, ClassSymbol c) {
        JCClassDecl tree = (JCClassDecl)env.tree;
        assert c == tree.sym;

        // Validate annotations
        chk.validateAnnotations(tree.mods.annotations, c);

        // Validate type parameters, supertype and interfaces.
        attribBounds(tree.typarams);
        chk.validateTypeParams(tree.typarams);
        chk.validate(tree.extending);
        chk.validate(tree.implementing);

        if ((c.flags() & ANNOTATION) != 0) {
            if (tree.implementing.nonEmpty())
                log.error(tree.implementing.head.pos(),
                          "cant.extend.intf.annotation");
            if (tree.typarams.nonEmpty())
                log.error(tree.typarams.head.pos(),
                          "intf.annotation.cant.have.type.params");
        } else {
            // Check that all extended classes and interfaces
            // are compatible (i.e. no two define methods with same arguments
            // yet different return types).  (JLS 8.4.6.3)
            chk.checkCompatibleSupertypes(tree.pos(), c.type);
        }

        // Check that class does not import the same parameterized interface
        // with two different argument lists.
        chk.checkClassBounds(tree.pos(), c.type);

        tree.type = c.type;

        boolean assertsEnabled = false;
        assert assertsEnabled = true;
        if (assertsEnabled) {
            for (List<JCTypeParameter> l = tree.typarams;
                 l.nonEmpty(); l = l.tail)
                assert env.info.scope.lookup(l.head.name).scope != null;
        }

        // Check that a generic class doesn't extend Throwable
        if (!c.type.allparams().isEmpty() && types.isSubtype(c.type, syms.throwableType))
            log.error(tree.extending.pos(), "generic.throwable");

        for (List<JCTree> l = tree.defs; l.nonEmpty(); l = l.tail) {
            // Attribute declaration
            attribStat(l.head, env);
            // Check that declarations in inner classes are not static (JLS 8.1.2)
            // Make an exception for static constants.
            if (c.owner.kind != PCK &&
                ((c.flags() & STATIC) == 0 || c.name == names.empty) &&
                (TreeInfo.flags(l.head) & (STATIC | INTERFACE)) != 0) {
                Symbol sym = null;
                if (l.head.getTag() == JCTree.VARDEF) sym = ((JCVariableDecl) l.head).sym;
                if (sym == null ||
                    sym.kind != VAR ||
                    ((VarSymbol) sym).getConstValue() == null)
                    log.error(l.head.pos(), "icls.cant.have.static.decl");
            }
        }

        // If this is a non-abstract class, check that it has no abstract
        // methods or unimplemented methods of an implemented interface.
        if ((c.flags() & (ABSTRACT | INTERFACE)) == 0) {
            if (!relax)
                chk.checkAllDefined(tree.pos(), c);
        }

        // Check that all methods which implement some
        // method conform to the method they implement.
        chk.checkImplementations(tree);

        Scope enclScope = enter.enterScope(env);
        for (List<JCTree> l = tree.defs; l.nonEmpty(); l = l.tail) {
            if (l.head instanceof JFXOperationDefinition)
                chk.checkUnique(l.head.pos(), ((JFXOperationDefinition) l.head).sym, enclScope);
        }

        // Check for cycles among non-initial constructors.
        chk.checkCyclicConstructors(tree);

        // Check for cycles among annotation elements.
        chk.checkNonCyclicElements(tree);

        // Check for proper use of serialVersionUID
        if (env.info.lint.isEnabled(Lint.LintCategory.SERIAL) &&
            isSerializable(c) &&
            (c.flags() & Flags.ENUM) == 0 &&
            (c.flags() & ABSTRACT) == 0) {
            checkSerialVersionUID(tree, c);
        }
    }
        // where
        /** check if a class is a subtype of Serializable, if that is available. */
        private boolean isSerializable(ClassSymbol c) {
            try {
                syms.serializableType.complete();
            }
            catch (CompletionFailure e) {
                return false;
            }
            return types.isSubtype(c.type, syms.serializableType);
        }

        /** Check that an appropriate serialVersionUID member is defined. */
        private void checkSerialVersionUID(JCClassDecl tree, ClassSymbol c) {

            // check for presence of serialVersionUID
            Scope.Entry e = c.members().lookup(names.serialVersionUID);
            while (e.scope != null && e.sym.kind != VAR) e = e.next();
            if (e.scope == null) {
                log.warning(tree.pos(), "missing.SVUID", c);
                return;
            }

            // check that it is static final
            VarSymbol svuid = (VarSymbol)e.sym;
            if ((svuid.flags() & (STATIC | FINAL)) !=
                (STATIC | FINAL))
                log.warning(TreeInfo.diagnosticPositionFor(svuid, tree), "improper.SVUID", c);

            // check that it is long
            else if (svuid.type.tag != TypeTags.LONG)
                log.warning(TreeInfo.diagnosticPositionFor(svuid, tree), "long.SVUID", c);

            // check constant
            else if (svuid.getConstValue() == null)
                log.warning(TreeInfo.diagnosticPositionFor(svuid, tree), "constant.SVUID", c);
        }

    protected Type capture(Type type) {
        Type ctype = types.capture(type);
        if (type instanceof FunctionType) {
            ctype = new FunctionType(type.getEnclosingType(), ((ClassType) type).typarams_field,
                    type.tsym, type.getReturnType());
        }
        return ctype;
    }
}
