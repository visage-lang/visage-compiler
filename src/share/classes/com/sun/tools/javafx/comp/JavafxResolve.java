/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.tree.*;

import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.Symbol.*;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.TypeTags.*;
import javax.lang.model.element.ElementVisitor;

import com.sun.tools.javafx.code.*;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;
import java.util.HashSet;
import java.util.Set;

/** Helper class for name resolution, used mostly by the attribution phase.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxResolve {
    protected static final Context.Key<JavafxResolve> javafxResolveKey =
        new Context.Key<JavafxResolve>();

    Name.Table names;
    Log log;
    Symtab syms;
    JavafxCheck chk;
    Infer infer;
    JavafxClassReader reader;
    JavafxTreeInfo treeinfo;
    JavafxTypes types;
    public final boolean boxingEnabled; // = source.allowBoxing();
    public final boolean varargsEnabled; // = source.allowVarargs();
    private final boolean debugResolve;

    public static JavafxResolve instance(Context context) {
        JavafxResolve instance = context.get(javafxResolveKey);
        if (instance == null)
            instance = new JavafxResolve(context);
        return instance;
    }

    protected JavafxResolve(Context context) {
        context.put(javafxResolveKey, this);
        syms = Symtab.instance(context);

        varNotFound = new
            ResolveError(ABSENT_VAR, syms.errSymbol, "variable not found");
        wrongMethod = new
            ResolveError(WRONG_MTH, syms.errSymbol, "method not found");
        wrongMethods = new
            ResolveError(WRONG_MTHS, syms.errSymbol, "wrong methods");
        methodNotFound = new
            ResolveError(ABSENT_MTH, syms.errSymbol, "method not found");
        typeNotFound = new
            ResolveError(ABSENT_TYP, syms.errSymbol, "type not found");

        names = Name.Table.instance(context);
        log = Log.instance(context);
        chk = (JavafxCheck)JavafxCheck.instance(context);
        infer = Infer.instance(context);
        reader = JavafxClassReader.instance(context);
        treeinfo = JavafxTreeInfo.instance(context);
        types = JavafxTypes.instance(context);
        Source source = Source.instance(context);
        boxingEnabled = source.allowBoxing();
        varargsEnabled = source.allowVarargs();
        Options options = Options.instance(context);
        debugResolve = options.get("debugresolve") != null;
    }

    /** error symbols, which are returned when resolution fails
     */
    final ResolveError varNotFound;
    final ResolveError wrongMethod;
    final ResolveError wrongMethods;
    final ResolveError methodNotFound;
    final ResolveError typeNotFound;

/* ************************************************************************
 * Identifier resolution
 *************************************************************************/

    /** An environment is "static" if its static level is greater than
     *  the one of its outer environment
     */
// JavaFX change
    public
// JavaFX change
    static boolean isStatic(JavafxEnv<JavafxAttrContext> env) {
        return env.info.staticLevel > env.outer.info.staticLevel;
    }

    /** An environment is an "initializer" if it is a constructor or
     *  an instance initializer.
     */
    static boolean isInitializer(JavafxEnv<JavafxAttrContext> env) {
        Symbol owner = env.info.scope.owner;
        return owner.isConstructor() ||
            owner.owner.kind == TYP &&
            (owner.kind == VAR ||
             owner.kind == MTH && (owner.flags() & BLOCK) != 0) &&
            (owner.flags() & STATIC) == 0;
    }

    /** Is class accessible in given evironment?
     *  @param env    The current environment.
     *  @param c      The class whose accessibility is checked.
     */
    public boolean isAccessible(JavafxEnv<JavafxAttrContext> env, TypeSymbol c) {
        switch ((short)(c.flags() & AccessFlags)) {
        case PRIVATE:
            return
                env.enclClass.sym.outermostClass() ==
                c.owner.outermostClass();
        case 0:
            return
                env.toplevel.packge == c.owner // fast special case
                ||
                env.toplevel.packge == c.packge()
                ||
                // Hack: this case is added since synthesized default constructors
                // of anonymous classes should be allowed to access
                // classes which would be inaccessible otherwise.
                env.enclMethod != null &&
                (env.enclMethod.mods.flags & ANONCONSTR) != 0;
        default: // error recovery
        case PUBLIC:
            return true;
        case PROTECTED:
            return
                env.toplevel.packge == c.owner // fast special case
                ||
                env.toplevel.packge == c.packge()
                ||
                isInnerSubClass(env.enclClass.sym, c.owner);
        }
    }
    //where
        /** Is given class a subclass of given base class, or an inner class
         *  of a subclass?
         *  Return null if no such class exists.
         *  @param c     The class which is the subclass or is contained in it.
         *  @param base  The base class
         */
        private boolean isInnerSubClass(ClassSymbol c, Symbol base) {
            while (c != null && !c.isSubClass(base, types)) {
                c = c.owner.enclClass();
            }
            return c != null;
        }

    boolean isAccessible(JavafxEnv<JavafxAttrContext> env, Type t) {
        return (t.tag == ARRAY)
            ? isAccessible(env, types.elemtype(t))
            : isAccessible(env, t.tsym);
    }

    /** Is symbol accessible as a member of given type in given evironment?
     *  @param env    The current environment.
     *  @param site   The type of which the tested symbol is regarded
     *                as a member.
     *  @param sym    The symbol.
     */
    public boolean isAccessible(JavafxEnv<JavafxAttrContext> env, Type site, Symbol sym) {
        if (sym.name == names.init && sym.owner != site.tsym) return false;
        ClassSymbol sub;
        switch ((short)(sym.flags() & AccessFlags)) {
        case PRIVATE:
            return
                (env.enclClass.sym == sym.owner // fast special case
                 ||
                 env.enclClass.sym.outermostClass() ==
                 sym.owner.outermostClass())
                &&
                isInheritedIn(sym, site.tsym, types);
        case 0:
            return
                (env.toplevel.packge == sym.owner.owner // fast special case
                 ||
                 env.toplevel.packge == sym.packge())
                &&
                isAccessible(env, site)
                &&
                isInheritedIn(sym, site.tsym, types);
        case PROTECTED:
            return
                (env.toplevel.packge == sym.owner.owner // fast special case
                 ||
                 env.toplevel.packge == sym.packge()
                 ||
                 isProtectedAccessible(sym, env.enclClass.sym, site)
                 ||
                 // OK to select instance method or field from 'super' or type name
                 // (but type names should be disallowed elsewhere!)
                 env.info.selectSuper && (sym.flags() & STATIC) == 0 && sym.kind != TYP)
                &&
                isAccessible(env, site)
                &&
                // `sym' is accessible only if not overridden by
                // another symbol which is a member of `site'
                // (because, if it is overridden, `sym' is not strictly
                // speaking a member of `site'.)
                (sym.kind != MTH || sym.isConstructor() ||
                 types.implementation((MethodSymbol)sym, site.tsym, true) == sym);
        default: // this case includes erroneous combinations as well
            return isAccessible(env, site);
        }
    }
    //where
        /** Is given protected symbol accessible if it is selected from given site
         *  and the selection takes place in given class?
         *  @param sym     The symbol with protected access
         *  @param c       The class where the access takes place
         *  @site          The type of the qualifier
         */
        private
        boolean isProtectedAccessible(Symbol sym, ClassSymbol c, Type site) {
            while (c != null &&
                   !(c.isSubClass(sym.owner, types) &&
                     (c.flags() & INTERFACE) == 0 &&
                     // In JLS 2e 6.6.2.1, the subclass restriction applies
                     // only to instance fields and methods -- types are excluded
                     // regardless of whether they are declared 'static' or not.
                     ((sym.flags() & STATIC) != 0 || sym.kind == TYP || site.tsym.isSubClass(c, types))))
                c = c.owner.enclClass();
            return c != null;
        }

    /** Try to instantiate the type of a method so that it fits
     *  given type arguments and argument types. If succesful, return
     *  the method's instantiated type, else return null.
     *  The instantiation will take into account an additional leading
     *  formal parameter if the method is an instance method seen as a member
     *  of un underdetermined site In this case, we treat site as an additional
     *  parameter and the parameters of the class containing the method as
     *  additional type variables that get instantiated.
     *
     *  @param env         The current environment
     *  @param m           The method symbol.
     *  @param mt          The expected type.
     *  @param argtypes    The invocation's given value arguments.
     *  @param typeargtypes    The invocation's given type arguments.
     *  @param allowBoxing Allow boxing conversions of arguments.
     *  @param useVarargs Box trailing arguments into an array for varargs.
     */
    Type rawInstantiate(JavafxEnv<JavafxAttrContext> env,
                        Symbol m,
                        Type mt,
                        List<Type> argtypes,
                        List<Type> typeargtypes,
                        boolean allowBoxing,
                        boolean useVarargs,
                        Warner warn)
        throws Infer.NoInstanceException {
        if (useVarargs && (m.flags() & VARARGS) == 0) return null;
        m.complete();

        // tvars is the list of formal type variables for which type arguments
        // need to inferred.
        List<Type> tvars = env.info.tvars;
        if (typeargtypes == null) typeargtypes = List.nil();
        if (mt.tag != FORALL && typeargtypes.nonEmpty()) {
            // This is not a polymorphic method, but typeargs are supplied
            // which is fine, see JLS3 15.12.2.1
        } else if (mt.tag == FORALL && typeargtypes.nonEmpty()) {
            ForAll pmt = (ForAll) mt;
            if (typeargtypes.length() != pmt.tvars.length())
                return null;
            // Check type arguments are within bounds
            List<Type> formals = pmt.tvars;
            List<Type> actuals = typeargtypes;
            while (formals.nonEmpty() && actuals.nonEmpty()) {
                List<Type> bounds = types.subst(types.getBounds((TypeVar)formals.head),
                                                pmt.tvars, typeargtypes);
                for (; bounds.nonEmpty(); bounds = bounds.tail)
                    if (!types.isSubtypeUnchecked(actuals.head, bounds.head, warn))
                        return null;
                formals = formals.tail;
                actuals = actuals.tail;
            }
            mt = types.subst(pmt.qtype, pmt.tvars, typeargtypes);
        } else if (mt.tag == FORALL) {
            ForAll pmt = (ForAll) mt;
            List<Type> tvars1 = types.newInstances(pmt.tvars);
            tvars = tvars.appendList(tvars1);
            mt = types.subst(pmt.qtype, pmt.tvars, tvars1);
        }

        // find out whether we need to go the slow route via infer
        boolean instNeeded = tvars.tail != null/*inlined: tvars.nonEmpty()*/;
        for (List<Type> l = argtypes;
             l.tail != null/*inlined: l.nonEmpty()*/ && !instNeeded;
             l = l.tail) {
            if (l.head.tag == FORALL) instNeeded = true;
        }

        if (instNeeded)
            return
            infer.instantiateMethod(tvars,
                                    (MethodType)mt,
                                    argtypes,
                                    allowBoxing,
                                    useVarargs,
                                    warn);
        return
            argumentsAcceptable(argtypes, mt.getParameterTypes(),
                                allowBoxing, useVarargs, warn)
            ? mt
            : null;
    }

    /** Same but returns null instead throwing a NoInstanceException
     */
    Type instantiate(JavafxEnv<JavafxAttrContext> env,
                     Type site,
                     Symbol m,
                     List<Type> argtypes,
                     List<Type> typeargtypes,
                     boolean allowBoxing,
                     boolean useVarargs,
                     Warner warn) {
        try {
            return rawInstantiate(env, m, types.memberType(site, m), argtypes, typeargtypes,
                                  allowBoxing, useVarargs, warn);
        } catch (Infer.NoInstanceException ex) {
            return null;
        }
    }

    /** Check if a parameter list accepts a list of args.
     */
    boolean argumentsAcceptable(List<Type> argtypes,
                                List<Type> formals,
                                boolean allowBoxing,
                                boolean useVarargs,
                                Warner warn) {
        Type varargsFormal = useVarargs ? formals.last() : null;
        while (argtypes.nonEmpty() && formals.head != varargsFormal) {
            boolean works = allowBoxing
                ? types.isConvertible(argtypes.head, formals.head, warn)
                : types.isSubtypeUnchecked(argtypes.head, formals.head, warn);
            if (!works) return false;
            argtypes = argtypes.tail;
            formals = formals.tail;
        }
        if (formals.head != varargsFormal) return false; // not enough args
        if (!useVarargs)
            return argtypes.isEmpty();
        Type elt = types.elemtype(varargsFormal);
        while (argtypes.nonEmpty()) {
            if (!types.isConvertible(argtypes.head, elt, warn))
                return false;
            argtypes = argtypes.tail;
        }
        return true;
    }

/* ***************************************************************************
 *  Symbol lookup
 *  the following naming conventions for arguments are used
 *
 *       env      is the environment where the symbol was mentioned
 *       site     is the type of which the symbol is a member
 *       name     is the symbol's name
 *                if no arguments are given
 *       argtypes are the value arguments, if we search for a method
 *
 *  If no symbol was found, a ResolveError detailing the problem is returned.
 ****************************************************************************/
    /** Find field. Synthetic fields are always skipped.
     *  @param env     The current environment.
     *  @param site    The original type from where the selection takes place.
     *  @param name    The name of the field.
     *  @param c       The class to search for the field. This is always
     *                 a superclass or implemented interface of site's class.
     */
// Javafx change
    public
// javafx change 
    Symbol findField(JavafxEnv<JavafxAttrContext> env,
                     Type site,
                     Name name,
                     TypeSymbol c) {
        Symbol bestSoFar = varNotFound;
        Symbol sym;
        Scope.Entry e = c.members().lookup(name);
        while (e.scope != null) {
            if ((e.sym.kind & (VAR|MTH)) != 0 && (e.sym.flags_field & SYNTHETIC) == 0) {
                sym = isAccessible(env, site, e.sym)
                    ? e.sym : new AccessError(env, site, e.sym);
                if (bestSoFar.kind < AMBIGUOUS && sym.kind < AMBIGUOUS &&
                    sym.owner != bestSoFar.owner)
                    bestSoFar = new AmbiguityError(bestSoFar, sym);
                else if (sym.kind < bestSoFar.kind)
                    bestSoFar = sym;
            }
            e = e.next();
        }
        if (bestSoFar != varNotFound)
            return bestSoFar;
        Type st = types.supertype(c.type);
        if (st != null && st.tag == CLASS) {
            sym = findField(env, site, name, st.tsym);
            if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        // We failed to find the field in the single Java class supertype of the 
        // Javafx class.
        // Now try to find the filed in all of the Javafx supertypes.
        if (bestSoFar.kind > AMBIGUOUS && c instanceof JavafxClassSymbol) {
            List<Type> supertypes = ((JavafxClassSymbol)c).getSuperTypes();
            for (Type tp : supertypes) {
                if (tp != null && tp.tag == CLASS) {
                    sym = findField(env, site, name, tp.tsym);
                    if (sym.kind < bestSoFar.kind) bestSoFar = sym;
                    if (bestSoFar.kind < AMBIGUOUS) {
                        break;
                    }
                }
            }
        }

        for (List<Type> l = types.interfaces(c.type);
             bestSoFar.kind != AMBIGUOUS && l.nonEmpty();
             l = l.tail) {
            sym = findField(env, site, name, l.head.tsym);
            if (bestSoFar.kind < AMBIGUOUS && sym.kind < AMBIGUOUS &&
                sym.owner != bestSoFar.owner)
                bestSoFar = new AmbiguityError(bestSoFar, sym);
            else if (sym.kind < bestSoFar.kind)
                bestSoFar = sym;
        }
        return bestSoFar;
    }

    /** Resolve a field identifier, throw a fatal error if not found.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the method invocation.
     *  @param site      The type of the qualifying expression, in which
     *                   identifier is searched.
     *  @param name      The identifier's name.
     */
    public VarSymbol resolveInternalField(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                                          Type site, Name name) {
        Symbol sym = findField(env, site, name, site.tsym);
        if (sym.kind == VAR) return (VarSymbol)sym;
        else throw new FatalError(
                 JCDiagnostic.fragment(MsgSym.MESSAGE_FATAL_ERR_CANNOT_LOCATE_FIELD,
                                name));
    }

    /** Find unqualified variable or field with given name.
     *  Synthetic fields always skipped.
     *  @param env     The current environment.
     *  @param name    The name of the variable or field.
     */
    Symbol findVar(JavafxEnv<JavafxAttrContext> env, Name name, int kind, Type expected) {
        Symbol bestSoFar = varNotFound;
        Symbol sym;
        JavafxEnv<JavafxAttrContext> env1 = env;
        boolean staticOnly = false;
        boolean innerAccess = false;
        Type mtype = expected;
        if (mtype instanceof FunctionType)
            mtype = mtype.asMethodType();
        boolean checkArgs = mtype instanceof MethodType || mtype instanceof ForAll;

        while (env1 != null) {
            Scope sc = env1.info.scope;
            Type envClass;
            if (env1.tree instanceof JFXClassDeclaration) {
                JFXClassDeclaration cdecl = (JFXClassDeclaration) env1.tree;
                if (cdecl.runMethod != null &&
                        name != names._this && name != names._super) {
                    envClass = null;
                    sc = cdecl.runBodyScope;
                    innerAccess = true;
                }
                envClass = cdecl.sym.type;
            }
            else
                envClass = null;
            if (envClass != null) {
                sym = findMember(env1, envClass, name,
                        expected,
                        true, false, false);

                if (sym.exists()) {
                    if (staticOnly) {
                        // Note: can't call isStatic with null owner
                        if (sym.owner != null) {
                            if (!sym.isStatic()) {
                                return new StaticError(sym);
                            }
                        }
                    }
                    return sym;
                }
            }
            if (sc != null) {
                
                for (Scope.Entry e = sc.lookup(name); e.scope != null; e = e.next()) {
                    if ((e.sym.flags_field & SYNTHETIC) != 0)
                        continue;
                    if ((e.sym.kind & (MTH|VAR)) != 0) {
                        if (innerAccess) {
                            e.sym.flags_field |= JavafxFlags.INNER_ACCESS;
                            if ((e.sym.flags_field & JavafxFlags.IN_INITIALIZER) != 0)
                                e.sym.flags_field |= JavafxFlags.ASSIGNED_TO;
                        }
                        if (checkArgs) {
                            Type mt = e.sym.type;
                            if (mt instanceof FunctionType)
                                mt = mt.asMethodType();
                            // Better to use selectBest, but that requires some
                            // changes.  FIXME
                            if (! (mt instanceof MethodType) ||
                                    ! argumentsAcceptable(mtype.getParameterTypes(), mt.getParameterTypes(),
                                    true, false, Warner.noWarnings))
                                return wrongMethod.setWrongSym(e.sym);
                        }
                        return e.sym;
                    }
                }
            }

            if (env1.tree instanceof JFXFunctionDefinition)
                innerAccess = true;
            if (env1.outer != null && isStatic(env1)) staticOnly = true;
            env1 = env1.outer;
        }

        Scope.Entry e = env.toplevel.namedImportScope.lookup(name);
        for (; e.scope != null; e = e.next()) {
            sym = e.sym;
            Type origin = e.getOrigin().owner.type;
            if (sym.kind == VAR) {
                if (e.sym.owner.type != origin)
                    sym = sym.clone(e.getOrigin().owner);
                return isAccessible(env, origin, sym)
                    ? sym : new AccessError(env, origin, sym);
            }
        }

        Symbol origin = null;
        e = env.toplevel.starImportScope.lookup(name);
        for (; e.scope != null; e = e.next()) {
            sym = e.sym;
            if (sym.kind != VAR)
                continue;
            // invariant: sym.kind == VAR
            if (bestSoFar.kind < AMBIGUOUS && sym.owner != bestSoFar.owner)
                return new AmbiguityError(bestSoFar, sym);
            else if (bestSoFar.kind >= VAR) {
                origin = e.getOrigin().owner;
                bestSoFar = isAccessible(env, origin.type, sym)
                    ? sym : new AccessError(env, origin.type, sym);
            }
        }
        
        if (name == names.fromString("__DIR__") || name == names.fromString("__FILE__")) {
            Type type = reader.enterClass(names.fromString("java.net.URL")).type;
            return new VarSymbol(Flags.PUBLIC, name, type, env.enclClass.sym);
        }
        
        if (bestSoFar.kind == VAR && bestSoFar.owner.type != origin.type)
            return bestSoFar.clone(origin);
        else
            return bestSoFar;
    }

    Warner noteWarner = new Warner();

    /** Select the best method for a call site among two choices.
     *  @param env              The current environment.
     *  @param site             The original type from where the
     *                          selection takes place.
     *  @param argtypes         The invocation's value arguments,
     *  @param typeargtypes     The invocation's type arguments,
     *  @param sym              Proposed new best match.
     *  @param bestSoFar        Previously found best match.
     *  @param allowBoxing Allow boxing conversions of arguments.
     *  @param useVarargs Box trailing arguments into an array for varargs.
     */
    Symbol selectBest(JavafxEnv<JavafxAttrContext> env,
                      Type site,
                      Type expected,
                      Symbol sym,
                      Symbol bestSoFar,
                      boolean allowBoxing,
                      boolean useVarargs,
                      boolean operator) {
        if (sym.kind == ERR) return bestSoFar;
        if (!isInheritedIn(sym, site.tsym, types)) return bestSoFar;
        assert sym.kind < AMBIGUOUS;
        List<Type> argtypes = expected.getParameterTypes();
        List<Type> typeargtypes = expected.getTypeArguments();
        try {
            if (rawInstantiate(env, sym, types.memberType(site, sym), argtypes, typeargtypes,
                               allowBoxing, useVarargs, Warner.noWarnings) == null) {
                // inapplicable
                switch (bestSoFar.kind) {
                case ABSENT_MTH: return wrongMethod.setWrongSym(sym);
                case WRONG_MTH: return wrongMethods;
                default: return bestSoFar;
                }
            }
        } catch (Infer.NoInstanceException ex) {
            switch (bestSoFar.kind) {
            case ABSENT_MTH:
                return wrongMethod.setWrongSym(sym, ex.getDiagnostic());
            case WRONG_MTH:
                return wrongMethods;
            default:
                return bestSoFar;
            }
        }
        if (!isAccessible(env, site, sym)) {
            return (bestSoFar.kind == ABSENT_MTH)
                ? new AccessError(env, site, sym)
                : bestSoFar;
        }
        return (bestSoFar.kind > AMBIGUOUS)
            ? sym
            : mostSpecific(sym, bestSoFar, env, site,
                           allowBoxing && operator, useVarargs);
    }

    /* Return the most specific of the two methods for a call,
     *  given that both are accessible and applicable.
     *  @param m1               A new candidate for most specific.
     *  @param m2               The previous most specific candidate.
     *  @param env              The current environment.
     *  @param site             The original type from where the selection
     *                          takes place.
     *  @param allowBoxing Allow boxing conversions of arguments.
     *  @param useVarargs Box trailing arguments into an array for varargs.
     */
    Symbol mostSpecific(Symbol m1,
                        Symbol m2,
                        JavafxEnv<JavafxAttrContext> env,
                        Type site,
                        boolean allowBoxing,
                        boolean useVarargs) {
        switch (m2.kind) {
        case MTH:
            if (m1 == m2) return m1;
            Type mt1 = types.memberType(site, m1);
            noteWarner.unchecked = false;
            boolean m1SignatureMoreSpecific =
                (instantiate(env, site, m2, types.lowerBoundArgtypes(mt1), null,
                             allowBoxing, false, noteWarner) != null ||
                 useVarargs && instantiate(env, site, m2, types.lowerBoundArgtypes(mt1), null,
                                           allowBoxing, true, noteWarner) != null) &&
                !noteWarner.unchecked;
            Type mt2 = types.memberType(site, m2);
            noteWarner.unchecked = false;
            boolean m2SignatureMoreSpecific =
                (instantiate(env, site, m1, types.lowerBoundArgtypes(mt2), null,
                             allowBoxing, false, noteWarner) != null ||
                 useVarargs && instantiate(env, site, m1, types.lowerBoundArgtypes(mt2), null,
                                           allowBoxing, true, noteWarner) != null) &&
                !noteWarner.unchecked;
            if (m1SignatureMoreSpecific && m2SignatureMoreSpecific) {
                if (!types.overrideEquivalent(mt1, mt2))
                    return new AmbiguityError(m1, m2);
                // same signature; select (a) the non-bridge method, or
                // (b) the one that overrides the other, or (c) the concrete
                // one, or (d) merge both abstract signatures
                if ((m1.flags() & BRIDGE) != (m2.flags() & BRIDGE)) {
                    return ((m1.flags() & BRIDGE) != 0) ? m2 : m1;
                }
                // if one overrides or hides the other, use it
                TypeSymbol m1Owner = (TypeSymbol)m1.owner;
                TypeSymbol m2Owner = (TypeSymbol)m2.owner;
                if (types.asSuper(m1Owner.type, m2Owner) != null &&
                    ((m1.owner.flags_field & INTERFACE) == 0 ||
                     (m2.owner.flags_field & INTERFACE) != 0) &&
                    m1.overrides(m2, m1Owner, types, false))
                    return m1;
                if (types.asSuper(m2Owner.type, m1Owner) != null &&
                    ((m2.owner.flags_field & INTERFACE) == 0 ||
                     (m1.owner.flags_field & INTERFACE) != 0) &&
                    m2.overrides(m1, m2Owner, types, false))
                    return m2;
                boolean m1Abstract = (m1.flags() & ABSTRACT) != 0;
                boolean m2Abstract = (m2.flags() & ABSTRACT) != 0;
                if (m1Abstract && !m2Abstract) return m2;
                if (m2Abstract && !m1Abstract) return m1;
                // both abstract or both concrete
                if (!m1Abstract && !m2Abstract)
                    return new AmbiguityError(m1, m2);
                // check for same erasure
                if (!types.isSameType(m1.erasure(types), m2.erasure(types)))
                    return new AmbiguityError(m1, m2);
                // both abstract, neither overridden; merge throws clause and result type
                Symbol result;
                Type result2 = mt2.getReturnType();
                if (mt2.tag == FORALL)
                    result2 = types.subst(result2, ((ForAll)mt2).tvars, ((ForAll)mt1).tvars);
                if (types.isSubtype(mt1.getReturnType(), result2)) {
                    result = m1;
                } else if (types.isSubtype(result2, mt1.getReturnType())) {
                    result = m2;
                } else {
                    // Theoretically, this can't happen, but it is possible
                    // due to error recovery or mixing incompatible class files
                    return new AmbiguityError(m1, m2);
                }
                result = result.clone(result.owner);
                result.type = (Type)result.type.clone();
                result.type.setThrown(chk.intersect(mt1.getThrownTypes(),
                                                    mt2.getThrownTypes()));
                return result;
            }
            if (m1SignatureMoreSpecific) return m1;
            if (m2SignatureMoreSpecific) return m2;
            return new AmbiguityError(m1, m2);
        case AMBIGUOUS:
            AmbiguityError e = (AmbiguityError)m2;
            Symbol err1 = mostSpecific(m1, e.sym1, env, site, allowBoxing, useVarargs);
            Symbol err2 = mostSpecific(m1, e.sym2, env, site, allowBoxing, useVarargs);
            if (err1 == err2) return err1;
            if (err1 == e.sym1 && err2 == e.sym2) return m2;
            if (err1 instanceof AmbiguityError &&
                err2 instanceof AmbiguityError &&
                ((AmbiguityError)err1).sym1 == ((AmbiguityError)err2).sym1)
                return new AmbiguityError(m1, m2);
            else
                return new AmbiguityError(err1, err2);
        default:
            throw new AssertionError();
        }
    }

    /** Find best qualified method matching given name, type and value
     *  arguments.
     *  @param env       The current environment.
     *  @param site      The original type from where the selection
     *                   takes place.
     *  @param name      The method's name.
     *  @param argtypes  The method's value arguments.
     *  @param typeargtypes The method's type arguments
     *  @param allowBoxing Allow boxing conversions of arguments.
     *  @param useVarargs Box trailing arguments into an array for varargs.
     */

    Symbol findMethod(JavafxEnv<JavafxAttrContext> env,
                      Type site,
                      Name name,
                      List<Type> argtypes,
                      List<Type> typeargtypes,
                      boolean allowBoxing,
                      boolean useVarargs,
                      boolean operator) {
        return findMember(env,
                          site,
                          name,
                          newMethTemplate(argtypes, typeargtypes),
                          site.tsym.type,
                          methodNotFound,
                          allowBoxing,
                          useVarargs,
                          operator);
    }

    Symbol findMember(JavafxEnv<JavafxAttrContext> env,
                      Type site,
                      Name name,
                      Type expected,
                      boolean allowBoxing,
                      boolean useVarargs,
                      boolean operator) {
        return findMember(env,
                          site,
                          name,
                          expected,
                          site.tsym.type,
                          methodNotFound,
                          allowBoxing,
                          useVarargs,
                          operator);
    }
    // where
             private Symbol findMember(JavafxEnv<JavafxAttrContext> env,
                              Type site,
                              Name name,
                              Type expected,
                              Type intype,
                              Symbol bestSoFar,
                              boolean allowBoxing,
                              boolean useVarargs,
                              boolean operator) {
        Type mtype = expected;
        if (mtype instanceof FunctionType)
            mtype = mtype.asMethodType();
        boolean checkArgs = mtype instanceof MethodType || mtype instanceof ForAll;
        for (Type ct = intype; ct.tag == CLASS; ct = types.supertype(ct)) {
            ClassSymbol c = (ClassSymbol)ct.tsym;
            for (Scope.Entry e = c.members().lookup(name);
                 e.scope != null;
                 e = e.next()) {
                if ((e.sym.kind & (VAR|MTH)) == 0 ||
                        (e.sym.flags_field & SYNTHETIC) != 0)
                    continue;
                e.sym.complete();
                if (! checkArgs) {
                    // No argument list to disambiguate.
                    if (bestSoFar.kind == ABSENT_VAR || bestSoFar.kind == ABSENT_MTH)
                        bestSoFar = e.sym;
                    else
                        bestSoFar = new AmbiguityError(bestSoFar, e.sym);
                }
                else if (e.sym.kind == MTH) {
                    bestSoFar = selectBest(env, site, mtype,
                                           e.sym, bestSoFar,
                                           allowBoxing,
                                           useVarargs,
                                           operator);
                    if (bestSoFar != null && bestSoFar.kind < AMBIGUOUS) {
                        if (isExactMatch(mtype, bestSoFar)) {
                            return bestSoFar;
                        }
                    }
                }
                else if ((e.sym.kind & (VAR|MTH)) != 0 && bestSoFar == methodNotFound) {
                    // FIXME duplicates logic in findVar.
                    Type mt = e.sym.type;
                    if (mt instanceof FunctionType)
                        mt = mt.asMethodType();
                    if (! (mt instanceof MethodType) ||
                            ! argumentsAcceptable(mtype.getParameterTypes(), mt.getParameterTypes(),
                            true, false, Warner.noWarnings))
                        return wrongMethod.setWrongSym(e.sym);
                    return e.sym;
                }
            }
            if (! checkArgs &&
                bestSoFar.kind != ABSENT_VAR && bestSoFar.kind != ABSENT_MTH) {
                return bestSoFar;
            }
            Symbol concrete = methodNotFound;
            if ((bestSoFar.flags() & ABSTRACT) == 0)
                concrete = bestSoFar;
            for (List<Type> l = types.interfaces(c.type);
                 l.nonEmpty();
                 l = l.tail) {
                bestSoFar = findMember(env, site, name, expected,
                                       l.head, bestSoFar,
                                       allowBoxing, useVarargs, operator);
            }
            if (concrete != bestSoFar &&
                concrete.kind < ERR  && bestSoFar.kind < ERR &&
                types.isSubSignature(concrete.type, bestSoFar.type))
                bestSoFar = concrete;
        }

        // We failed to find the field in the single Java class supertype of the 
        // Javafx class.
        // Now try to find the filed in all of the Javafx supertypes.
        if (bestSoFar.kind > AMBIGUOUS && intype.tsym instanceof JavafxClassSymbol) {
            List<Type> supertypes = ((JavafxClassSymbol)intype.tsym).getSuperTypes();
            for (Type tp : supertypes) {
                bestSoFar = findMember(env, site, name, expected, tp,
                        bestSoFar, allowBoxing, useVarargs, operator);                
                if (bestSoFar.kind < AMBIGUOUS) {
                    break;
                }
            }
        }

        return bestSoFar;
    }

    private boolean isExactMatch(Type mtype, Symbol bestSoFar) {
        if (bestSoFar.kind == MTH && (bestSoFar.type instanceof MethodType) &&
                mtype.tag == TypeTags.METHOD ) {
            List<Type> actuals = ((MethodType)mtype).getParameterTypes();
            List<Type> formals = ((MethodType)bestSoFar.type).getParameterTypes();
            if (actuals != null && formals != null) {
                if (actuals.size() == formals.size()) {
                    for (Type actual : actuals) {
                        if (actual != formals.head) {
                            return false;
                        }

                        formals = formals.tail;
                    }
                    return true;
                }
            }
        }
        
        return false;
    }

    Type newMethTemplate(List<Type> argtypes, List<Type> typeargtypes) {
        MethodType mt = new MethodType(argtypes, null, null, syms.methodClass);
        return (typeargtypes == null) ? mt : (Type)new ForAll(typeargtypes, mt);
    }
         
    /** Load toplevel or member class with given fully qualified name and
     *  verify that it is accessible.
     *  @param env       The current environment.
     *  @param name      The fully qualified name of the class to be loaded.
     */
    Symbol loadClass(JavafxEnv<JavafxAttrContext> env, Name name) {
        try {
            ClassSymbol c = reader.loadClass(name);
            return isAccessible(env, c) ? c : new AccessError(c);
        } catch (ClassReader.BadClassFile err) {
            throw err;
        } catch (CompletionFailure ex) {
            return typeNotFound;
        }
    }

    /** Find qualified member type.
     *  @param env       The current environment.
     *  @param site      The original type from where the selection takes
     *                   place.
     *  @param name      The type's name.
     *  @param c         The class to search for the member type. This is
     *                   always a superclass or implemented interface of
     *                   site's class.
     */
// Javafx change
    public
// Javafx change
    Symbol findMemberType(JavafxEnv<JavafxAttrContext> env,
                          Type site,
                          Name name,
                          TypeSymbol c) {
        Symbol bestSoFar = typeNotFound;
        Symbol sym;
        Scope.Entry e = c.members().lookup(name);
        while (e.scope != null) {
            if (e.sym.kind == TYP) {
                return isAccessible(env, site, e.sym)
                    ? e.sym
                    : new AccessError(env, site, e.sym);
            }
            e = e.next();
        }
        Type st = types.supertype(c.type);
        if (st != null && st.tag == CLASS) {
            sym = findMemberType(env, site, name, st.tsym);
            if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        // We failed to find the field in the single Java class supertype of the 
        // Javafx class.
        // Now try to find the filed in all of the Javafx supertypes.
        if (bestSoFar.kind > AMBIGUOUS && c instanceof JavafxClassSymbol) {
            List<Type> supertypes = ((JavafxClassSymbol)c).getSuperTypes();
            for (Type tp : supertypes) {
                if (tp != null && tp.tag == CLASS) {
                    sym = findField(env, site, name, tp.tsym);
                    if (sym.kind < bestSoFar.kind) bestSoFar = sym;
                    if (bestSoFar.kind < AMBIGUOUS) {
                        break;
                    }
                }
            }
        }

        for (List<Type> l = types.interfaces(c.type);
             bestSoFar.kind != AMBIGUOUS && l.nonEmpty();
             l = l.tail) {
            sym = findMemberType(env, site, name, l.head.tsym);
            if (bestSoFar.kind < AMBIGUOUS && sym.kind < AMBIGUOUS &&
                sym.owner != bestSoFar.owner)
                bestSoFar = new AmbiguityError(bestSoFar, sym);
            else if (sym.kind < bestSoFar.kind)
                bestSoFar = sym;
        }
        return bestSoFar;
    }

    /** Find a global type in given scope and load corresponding class.
     *  @param env       The current environment.
     *  @param scope     The scope in which to look for the type.
     *  @param name      The type's name.
     */
    Symbol findGlobalType(JavafxEnv<JavafxAttrContext> env, Scope scope, Name name) {
        Symbol bestSoFar = typeNotFound;
        for (Scope.Entry e = scope.lookup(name); e.scope != null; e = e.next()) {
            Symbol sym = loadClass(env, e.sym.flatName());
            if (bestSoFar.kind == TYP && sym.kind == TYP &&
                bestSoFar != sym)
                return new AmbiguityError(bestSoFar, sym);
            else if (sym.kind < bestSoFar.kind)
                bestSoFar = sym;
        }
        return bestSoFar;
    }

    /** Find an unqualified type symbol.
     *  @param env       The current environment.
     *  @param name      The type's name.
     */
    Symbol findType(JavafxEnv<JavafxAttrContext> env, Name name) {
        Symbol bestSoFar = typeNotFound;
        Symbol sym;
        boolean staticOnly = false;
        for (JavafxEnv<JavafxAttrContext> env1 = env; env1.outer != null; env1 = env1.outer) {
            if (isStatic(env1)) staticOnly = true;
            for (Scope.Entry e = env1.info.scope.lookup(name);
                 e.scope != null;
                 e = e.next()) {
                if (e.sym.kind == TYP) {
                    if (staticOnly &&
                        e.sym.type.tag == TYPEVAR &&
                        e.sym.owner.kind == TYP) return new StaticError(e.sym);
                    return e.sym;
                }
            }

            sym = findMemberType(env1, env1.enclClass.sym.type, name,
                                 env1.enclClass.sym);
            if (staticOnly && sym.kind == TYP &&
                sym.type.tag == CLASS &&
                sym.type.getEnclosingType().tag == CLASS &&
                env1.enclClass.sym.type.isParameterized() &&
                sym.type.getEnclosingType().isParameterized())
                return new StaticError(sym);
            else if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;

            JFXClassDeclaration encl = env1.baseClause ? (JFXClassDeclaration)env1.tree : env1.enclClass;
            if ((encl.sym.flags() & STATIC) != 0)
                staticOnly = true;
        }

        if (env.tree.getFXTag() != JavafxTag.IMPORT) {
            sym = findGlobalType(env, env.toplevel.namedImportScope, name);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;

            sym = findGlobalType(env, env.toplevel.packge.members(), name);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;

            sym = findGlobalType(env, env.toplevel.starImportScope, name);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        return bestSoFar;
    }

    /** Find an unqualified identifier which matches a specified kind set.
     *  @param env       The current environment.
     *  @param name      The indentifier's name.
     *  @param kind      Indicates the possible symbol kinds
     *                   (a subset of VAL, TYP, PCK).
     */
    Symbol findIdent(JavafxEnv<JavafxAttrContext> env, Name name, int kind, Type expected) {
        Symbol bestSoFar = typeNotFound;
        Symbol sym;
        if ((kind & (VAR|MTH)) != 0) {
            sym = findVar(env, name, kind, expected);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        if ((kind & TYP) != 0) {
            sym = findType(env, name);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        if ((kind & PCK) != 0) return reader.enterPackage(name);
        else return bestSoFar;
    }

    /** Find an identifier in a package which matches a specified kind set.
     *  @param env       The current environment.
     *  @param name      The identifier's name.
     *  @param kind      Indicates the possible symbol kinds
     *                   (a nonempty subset of TYP, PCK).
     */
    Symbol findIdentInPackage(JavafxEnv<JavafxAttrContext> env, TypeSymbol pck,
                              Name name, int kind) {
        Name fullname = TypeSymbol.formFullName(name, pck);
        Symbol bestSoFar = typeNotFound;
        PackageSymbol pack = null;
        if ((kind & PCK) != 0) {
            pack = reader.enterPackage(fullname);
            if (pack.exists()) return pack;
        }
        if ((kind & TYP) != 0) {
            Symbol sym = loadClass(env, fullname);
            if (sym.exists()) {
                // don't allow programs to use flatnames
                if (name == sym.name) return sym;
            }
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }
        return (pack != null) ? pack : bestSoFar;
    }

    /** Find an identifier among the members of a given type `site'.
     *  @param env       The current environment.
     *  @param site      The type containing the symbol to be found.
     *  @param name      The identifier's name.
     *  @param kind      Indicates the possible symbol kinds
     *                   (a subset of VAL, TYP).
     */
    Symbol findIdentInType(JavafxEnv<JavafxAttrContext> env, Type site,
                           Name name, int kind) {
        Symbol bestSoFar = typeNotFound;
        Symbol sym;
        if ((kind & (VAR|MTH)) != 0) {
            sym = findField(env, site, name, site.tsym);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }

        if ((kind & TYP) != 0) {
            sym = findMemberType(env, site, name, site.tsym);
            if (sym.exists()) return sym;
            else if (sym.kind < bestSoFar.kind) bestSoFar = sym;
        }
        return bestSoFar;
    }

/* ***************************************************************************
 *  Access checking
 *  The following methods convert ResolveErrors to ErrorSymbols, issuing
 *  an error message in the process
 ****************************************************************************/

    /** If `sym' is a bad symbol: report error and return errSymbol
     *  else pass through unchanged,
     *  additional arguments duplicate what has been used in trying to find the
     *  symbol (--> flyweight pattern). This improves performance since we
     *  expect misses to happen frequently.
     *
     *  @param sym       The symbol that was found, or a ResolveError.
     *  @param pos       The position to use for error reporting.
     *  @param site      The original type from where the selection took place.
     *  @param name      The symbol's name.
     *  @param argtypes  The invocation's value arguments,
     *                   if we looked for a method.
     *  @param typeargtypes  The invocation's type arguments,
     *                   if we looked for a method.
     */
    Symbol access(Symbol sym,
                  DiagnosticPosition pos,
                  Type site,
                  Name name,
                  boolean qualified,
                  List<Type> argtypes,
                  List<Type> typeargtypes) {
        if (sym.kind >= AMBIGUOUS) {
//          printscopes(site.tsym.members());//DEBUG
            if (!site.isErroneous() &&
                !Type.isErroneous(argtypes) &&
                (typeargtypes==null || !Type.isErroneous(typeargtypes))) {
                ((ResolveError)sym).report(log, pos, site, name, argtypes, typeargtypes);
            }
            do {
                sym = ((ResolveError)sym).sym;
            } while (sym.kind >= AMBIGUOUS);
            if (sym == syms.errSymbol // preserve the symbol name through errors
                || ((sym.kind & ERRONEOUS) == 0 // make sure an error symbol is returned
                    && (sym.kind & TYP) != 0))
                sym = new ErrorType(name, qualified?site.tsym:syms.noSymbol).tsym;
        }
        return sym;
    }

    Symbol access(Symbol sym,
                  DiagnosticPosition pos,
                  Type site,
                  Name name,
                  boolean qualified,
                  Type expected) {
        return access(sym, pos, site, name, qualified, expected.getParameterTypes(), expected.getTypeArguments());
    }
    /** Same as above, but without type arguments and arguments.
     */
// Javafx change
    public
// Javafx change
    Symbol access(Symbol sym,
                  DiagnosticPosition pos,
                  Type site,
                  Name name,
                  boolean qualified) {
        if (sym.kind >= AMBIGUOUS)
            return access(sym, pos, site, name, qualified, List.<Type>nil(), null);
        else
            return sym;
    }

    /** Check that sym is not an abstract method.
     */
    void checkNonAbstract(DiagnosticPosition pos, Symbol sym) {
        if ((sym.flags() & ABSTRACT) != 0)
            log.error(pos, MsgSym.MESSAGE_ABSTRACT_CANNOT_BE_ACCESSED_DIRECTLY,
                      kindName(sym), sym, sym.location());
    }

/* ***************************************************************************
 *  Debugging
 ****************************************************************************/

    /** print all scopes starting with scope s and proceeding outwards.
     *  used for debugging.
     */
    public void printscopes(Scope s) {
        while (s != null) {
            if (s.owner != null)
                System.err.print(s.owner + ": ");
            for (Scope.Entry e = s.elems; e != null; e = e.sibling) {
                if ((e.sym.flags() & ABSTRACT) != 0)
                    System.err.print("abstract ");
                System.err.print(e.sym + " ");
            }
            System.err.println();
            s = s.next;
        }
    }

    void printscopes(JavafxEnv<JavafxAttrContext> env) {
        while (env.outer != null) {
            System.err.println("------------------------------");
            printscopes(env.info.scope);
            env = env.outer;
        }
    }

    public void printscopes(Type t) {
        while (t.tag == CLASS) {
            printscopes(t.tsym.members());
            t = types.supertype(t);
        }
    }

/* ***************************************************************************
 *  Name resolution
 *  Naming conventions are as for symbol lookup
 *  Unlike the find... methods these methods will report access errors
 ****************************************************************************/

    /** Resolve an unqualified (non-method) identifier.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the identifier use.
     *  @param name      The identifier's name.
     *  @param kind      The set of admissible symbol kinds for the identifier.
     *  @param pt        The expected type.
     */
    Symbol resolveIdent(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                        Name name, int kind, Type pt) {
        Symbol sym = findIdent(env, name, kind, pt);
        if (sym.kind >= AMBIGUOUS)
            return access(sym, pos, env.enclClass.sym.type, name, false, pt);
        else
            return sym;
    }

    /** Resolve a qualified method identifier
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the method invocation.
     *  @param site      The type of the qualifying expression, in which
     *                   identifier is searched.
     *  @param name      The identifier's name.
     *  @param argtypes  The types of the invocation's value arguments.
     *  @param typeargtypes  The types of the invocation's type arguments.
     */
    Symbol resolveQualifiedMethod(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                                  Type site, Name name, Type expected) {
        Symbol sym = findMember(env, site, name, expected, false,
                                env.info.varArgs=false, false);
        if (varargsEnabled && sym.kind >= WRONG_MTHS) {
            sym = findMember(env, site, name, expected, true,
                             false, false);
            if (sym.kind >= WRONG_MTHS)
                sym = findMember(env, site, name, expected, true,
                                 env.info.varArgs=true, false);
        }
        if (sym.kind >= AMBIGUOUS) {
            sym = access(sym, pos, site, name, true, expected);
        }
        return sym;
    }

    /** Resolve a qualified method identifier, throw a fatal error if not
     *  found.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the method invocation.
     *  @param site      The type of the qualifying expression, in which
     *                   identifier is searched.
     *  @param name      The identifier's name.
     *  @param argtypes  The types of the invocation's value arguments.
     *  @param typeargtypes  The types of the invocation's type arguments.
     */
    public MethodSymbol resolveInternalMethod(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                                        Type site, Name name,
                                        List<Type> argtypes,
                                        List<Type> typeargtypes) {
        Symbol sym = resolveQualifiedMethod(
            pos, env, site, name, newMethTemplate(argtypes, typeargtypes));
        if (sym.kind == MTH) return (MethodSymbol)sym;
        else throw new FatalError(
                 JCDiagnostic.fragment(MsgSym.MESSAGE_FATAL_ERR_CANNOT_LOCATE_METH,
                                name));
    }

    /** Resolve constructor.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the constructor invocation.
     *  @param site      The type of class for which a constructor is searched.
     *  @param argtypes  The types of the constructor invocation's value
     *                   arguments.
     *  @param typeargtypes  The types of the constructor invocation's type
     *                   arguments.
     */
    public // Javafx change
    Symbol resolveConstructor(DiagnosticPosition pos,
                              JavafxEnv<JavafxAttrContext> env,
                              Type site,
                              List<Type> argtypes,
                              List<Type> typeargtypes) {
        Symbol sym = resolveConstructor(pos, env, site, argtypes, typeargtypes, false, env.info.varArgs=false);
        if (varargsEnabled && sym.kind >= WRONG_MTHS) {
            sym = resolveConstructor(pos, env, site, argtypes, typeargtypes, true, false);
            if (sym.kind >= WRONG_MTHS)
                sym = resolveConstructor(pos, env, site, argtypes, typeargtypes, true, env.info.varArgs=true);
        }
        if (sym.kind >= AMBIGUOUS) {
            sym = access(sym, pos, site, names.init, true, argtypes, typeargtypes);
        }
        return sym;
    }

    /** Resolve constructor.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the constructor invocation.
     *  @param site      The type of class for which a constructor is searched.
     *  @param argtypes  The types of the constructor invocation's value
     *                   arguments.
     *  @param typeargtypes  The types of the constructor invocation's type
     *                   arguments.
     *  @param allowBoxing Allow boxing and varargs conversions.
     *  @param useVarargs Box trailing arguments into an array for varargs.
     */
    public // Javafx change
    Symbol resolveConstructor(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                              Type site, List<Type> argtypes,
                              List<Type> typeargtypes,
                              boolean allowBoxing,
                              boolean useVarargs) {
        Symbol sym = findMethod(env, site,
                                names.init, argtypes,
                                typeargtypes, allowBoxing,
                                useVarargs, false);
        if ((sym.flags() & DEPRECATED) != 0 &&
            (env.info.scope.owner.flags() & DEPRECATED) == 0 &&
            env.info.scope.owner.outermostClass() != sym.outermostClass())
            chk.warnDeprecated(pos, sym);
        return sym;
    }

    /** Resolve a constructor, throw a fatal error if not found.
     *  @param pos       The position to use for error reporting.
     *  @param env       The environment current at the method invocation.
     *  @param site      The type to be constructed.
     *  @param argtypes  The types of the invocation's value arguments.
     *  @param typeargtypes  The types of the invocation's type arguments.
     */
    public MethodSymbol resolveInternalConstructor(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env,
                                        Type site,
                                        List<Type> argtypes,
                                        List<Type> typeargtypes) {
        Symbol sym = resolveConstructor(
            pos, env, site, argtypes, typeargtypes);
        if (sym.kind == MTH) return (MethodSymbol)sym;
        else throw new FatalError(
                 JCDiagnostic.fragment(MsgSym.MESSAGE_FATAL_ERR_CANNOT_LOCATE_CTOR, site));
    }

    /** Resolve operator.
     *  @param pos       The position to use for error reporting.
     *  @param optag     The tag of the operation tree.
     *  @param env       The environment current at the operation.
     *  @param argtypes  The types of the operands.
     */
    Symbol resolveOperator(DiagnosticPosition pos, JavafxTag optag,
                           JavafxEnv<JavafxAttrContext> env, List<Type> argtypes) {
        Name name = treeinfo.operatorName(optag);
        Symbol sym = findMethod(env, syms.predefClass.type, name, argtypes,
                                null, false, false, true);
        if (boxingEnabled && sym.kind >= WRONG_MTHS)
            sym = findMethod(env, syms.predefClass.type, name, argtypes,
                             null, true, false, true);
        return access(sym, pos, env.enclClass.sym.type, name,
                      false, argtypes, null);
    }

    /** Resolve operator.
     *  @param pos       The position to use for error reporting.
     *  @param optag     The tag of the operation tree.
     *  @param env       The environment current at the operation.
     *  @param arg       The type of the operand.
     */
    Symbol resolveUnaryOperator(DiagnosticPosition pos, JavafxTag optag, JavafxEnv<JavafxAttrContext> env, Type arg) {
        // check for Duration unary minus
        if (types.isSameType(arg, ((JavafxSymtab)syms).javafx_DurationType)) {
            Symbol res = null;
            switch (optag) {
            case NEG:
                res = resolveMethod(pos,  env,
                                    names.fromString("negate"),
                                    arg, List.<Type>nil());
                break;
            }
            if (res != null && res.kind == MTH) {
                return res;
            }
        }
        return resolveOperator(pos, optag, env, List.of(arg));
    }

    /** Resolve binary operator.
     *  @param pos       The position to use for error reporting.
     *  @param optag     The tag of the operation tree.
     *  @param env       The environment current at the operation.
     *  @param left      The types of the left operand.
     *  @param right     The types of the right operand.
     */
    Symbol resolveBinaryOperator(DiagnosticPosition pos,
                                 JavafxTag optag,
                                 JavafxEnv<JavafxAttrContext> env,
                                 Type left,
                                 Type right) {
        // Duration operator overloading
        if (types.isSameType(left, ((JavafxSymtab)syms).javafx_DurationType) ||
            types.isSameType(right, ((JavafxSymtab)syms).javafx_DurationType)) {
            Type dur = left;
            Symbol res = null;
            switch (optag) {
            case PLUS:
                res = resolveMethod(pos,  env,
                                     names.fromString("add"),
                                     dur, List.of(right));
                break;
            case MINUS:
                res =  resolveMethod(pos,  env,
                                     names.fromString("sub"),
                                     dur, List.of(right));
                break;
            case MUL:
                if (!types.isSameType(left, ((JavafxSymtab)syms).javafx_DurationType)) {
                    left = right;
                    right = dur;
                    dur = left;
                }
                res =  resolveMethod(pos,  env,
                                     names.fromString("mul"),
                                     dur,
                                     List.of(right));
                break;
            case DIV:
                res =  resolveMethod(pos,  env,
                                     names.fromString("div"),
                                     dur, List.of(right));
                break;

            //fix me...inline or move to static helper?
            case LT:
                res =  resolveMethod(pos,  env,
                                     names.fromString("lt"),
                                     dur, List.of(right));
                break;
            case LE:
                res =  resolveMethod(pos,  env,
                                     names.fromString("le"),
                                     dur, List.of(right));
                break;
            case GT:
                res =  resolveMethod(pos,  env,
                                     names.fromString("gt"),
                                     dur, List.of(right));
                break;
            case GE:
                res =  resolveMethod(pos,  env,
                                     names.fromString("ge"),
                                     dur, List.of(right));
                break;
            }
            if (res != null && res.kind == MTH) {
                return res;
            } // else fall through
        }
        return resolveOperator(pos, optag, env, List.of(left, right));
    }

    Symbol resolveMethod(DiagnosticPosition pos,
                         JavafxEnv<JavafxAttrContext> env, 
                         Name name,
                         Type type,
                         List<Type> argtypes) {
        Symbol sym = findMethod(env, type, name, argtypes,
                                null, true, false, false);
        if (sym.kind == MTH) { // skip access if method wasn't found
            return access(sym, pos, env.enclClass.sym.type, name,
                          false, argtypes, null);
        }
        return sym;
    }

    /**
     * Resolve `c.name' where name == this or name == super.
     * @param pos           The position to use for error reporting.
     * @param env           The environment current at the expression.
     * @param c             The qualifier.
     * @param name          The identifier's name.
     */
    public // Javafx change
    Symbol resolveSelf(DiagnosticPosition pos,
                       JavafxEnv<JavafxAttrContext> env,
                       TypeSymbol c,
                       Name name) {
        JavafxEnv<JavafxAttrContext> env1 = env;
        boolean staticOnly = false;
        while (env1.outer != null) {
            if (isStatic(env1)) staticOnly = true;
            if (env1.enclClass.sym == c) {
                Symbol sym = env1.info.scope.lookup(name).sym;
                if (sym != null) {
                    if (staticOnly) sym = new StaticError(sym);
                    return access(sym, pos, env.enclClass.sym.type,
                                  name, true);
                }
            }
            if ((env1.enclClass.sym.flags() & STATIC) != 0) staticOnly = true;
            env1 = env1.outer;
        }
        log.error(pos, MsgSym.MESSAGE_NOT_ENCL_CLASS, c);
        return syms.errSymbol;
    }

    /**
     * Resolve `c.this' for an enclosing class c that contains the
     * named member.
     * @param pos           The position to use for error reporting.
     * @param env           The environment current at the expression.
     * @param member        The member that must be contained in the result.
     */
    Symbol resolveSelfContaining(DiagnosticPosition pos,
                                 JavafxEnv<JavafxAttrContext> env,
                                 Symbol member) {
        Name name = names._this;
        JavafxEnv<JavafxAttrContext> env1 = env;
        boolean staticOnly = false;
        while (env1.outer != null) {
            if (isStatic(env1)) staticOnly = true;
            if (env1.enclClass.sym.isSubClass(member.owner, types) &&
                isAccessible(env, env1.enclClass.sym.type, member)) {
                Symbol sym = env1.info.scope.lookup(name).sym;
                if (sym != null) {
                    if (staticOnly) sym = new StaticError(sym);
                    return access(sym, pos, env.enclClass.sym.type,
                                  name, true);
                }
            }
            if ((env1.enclClass.sym.flags() & STATIC) != 0)
                staticOnly = true;
            env1 = env1.outer;
        }
        log.error(pos, MsgSym.MESSAGE_ENCL_CLASS_REQUIRED, member);
        return syms.errSymbol;
    }

    /**
     * Resolve an appropriate implicit this instance for t's container.
     * JLS2 8.8.5.1 and 15.9.2
     */
    public // Javafx change
    Type resolveImplicitThis(DiagnosticPosition pos, JavafxEnv<JavafxAttrContext> env, Type t) {
        Type thisType = (((t.tsym.owner.kind & (MTH|VAR)) != 0)
                         ? resolveSelf(pos, env, t.getEnclosingType().tsym, names._this)
                         : resolveSelfContaining(pos, env, t.tsym)).type;
        if (env.info.isSelfCall && thisType.tsym == env.enclClass.sym)
            log.error(pos, MsgSym.MESSAGE_CANNOT_REF_BEFORE_CTOR_CALLED, "this");
        return thisType;
    }

/* ***************************************************************************
 *  Methods related to kinds
 ****************************************************************************/

    /** A localized string describing a given kind.
     */
    public // Javafx change
    static JCDiagnostic kindName(int kind) {
        switch (kind) {
        case PCK: return JCDiagnostic.fragment(MsgSym.KINDNAME_PACKAGE);
        case TYP: return JCDiagnostic.fragment(MsgSym.KINDNAME_CLASS);
        case VAR: return JCDiagnostic.fragment(MsgSym.KINDNAME_VARIABLE);
        case VAL: return JCDiagnostic.fragment(MsgSym.KINDNAME_VALUE);
        case MTH: return JCDiagnostic.fragment(MsgSym.KINDNAME_METHOD);
        default : return JCDiagnostic.fragment(MsgSym.KINDNAME,
                                               Integer.toString(kind)); //debug
        }
    }

    static JCDiagnostic kindName(Symbol sym) {
        switch (sym.getKind()) {
        case PACKAGE:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_PACKAGE);

        case ENUM:
        case ANNOTATION_TYPE:
        case INTERFACE:
        case CLASS:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_CLASS);

        case TYPE_PARAMETER:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_TYPE_VARIABLE);

        case ENUM_CONSTANT:
        case FIELD:
        case PARAMETER:
        case LOCAL_VARIABLE:
        case EXCEPTION_PARAMETER:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_VARIABLE);

        case METHOD:
        case CONSTRUCTOR:
        case STATIC_INIT:
        case INSTANCE_INIT:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_METHOD);

        default:
            if (sym.kind == VAL)
                // I don't think this can happen but it can't harm
                // playing it safe --ahe
                return JCDiagnostic.fragment(MsgSym.KINDNAME_VALUE);
            else
                return JCDiagnostic.fragment(MsgSym.KINDNAME, sym.getKind()); // debug
        }
    }

    /** A localized string describing a given set of kinds.
     */
    public // Javafx change
    static JCDiagnostic kindNames(int kind) {
        StringBuffer key = new StringBuffer();
        key.append(MsgSym.KINDNAME);
        if ((kind & VAL) != 0)
            key.append(((kind & VAL) == VAR) ? MsgSym.KINDNAME_KEY_VARIABLE : MsgSym.KINDNAME_KEY_VALUE);
        if ((kind & MTH) != 0) key.append(MsgSym.KINDNAME_KEY_METHOD);
        if ((kind & TYP) != 0) key.append(MsgSym.KINDNAME_KEY_CLASS);
        if ((kind & PCK) != 0) key.append(MsgSym.KINDNAME_KEY_PACKAGE);
        return JCDiagnostic.fragment(key.toString(), kind);
    }

    /** A localized string describing the kind -- either class or interface --
     *  of a given type.
     */
    static JCDiagnostic typeKindName(Type t) {
        if (t.tag == TYPEVAR ||
            t.tag == CLASS && (t.tsym.flags() & COMPOUND) != 0)
            return JCDiagnostic.fragment(MsgSym.KINDNAME_TYPE_VARIABLE_BOUND);
        else if (t.tag == PACKAGE)
            return JCDiagnostic.fragment(MsgSym.KINDNAME_PACKAGE);
        else if ((t.tsym.flags_field & ANNOTATION) != 0)
            return JCDiagnostic.fragment(MsgSym.KINDNAME_ANNOTATION);
        else if ((t.tsym.flags_field & INTERFACE) != 0)
            return JCDiagnostic.fragment(MsgSym.KINDNAME_INTERFACE);
        else
            return JCDiagnostic.fragment(MsgSym.KINDNAME_CLASS);
    }

    /** A localized string describing the kind of a missing symbol, given an
     *  error kind.
     */
    static JCDiagnostic absentKindName(int kind) {
        switch (kind) {
        case ABSENT_VAR:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_VARIABLE);
        case WRONG_MTHS: case WRONG_MTH: case ABSENT_MTH:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_METHOD);
        case ABSENT_TYP:
            return JCDiagnostic.fragment(MsgSym.KINDNAME_CLASS);
        default:
            return JCDiagnostic.fragment(MsgSym.KINDNAME, kind);
        }
    }

/* ***************************************************************************
 *  ResolveError classes, indicating error situations when accessing symbols
 ****************************************************************************/

    public void logAccessError(JavafxEnv<JavafxAttrContext> env, JCTree tree, Type type) {
        AccessError error = new AccessError(env, type.getEnclosingType(), type.tsym);
        error.report(log, tree.pos(), type.getEnclosingType(), null, null, null);
    }

    /** Root class for resolve errors.
     *  Instances of this class indicate "Symbol not found".
     *  Instances of subclass indicate other errors.
     */
    private class ResolveError extends Symbol {

        ResolveError(int kind, Symbol sym, String debugName) {
            super(kind, 0, null, null, null);
            this.debugName = debugName;
            this.sym = sym;
        }

        /** The name of the kind of error, for debugging only.
         */
        final String debugName;

        /** The symbol that was determined by resolution, or errSymbol if none
         *  was found.
         */
        final Symbol sym;

        /** The symbol that was a close mismatch, or null if none was found.
         *  wrongSym is currently set if a simgle method with the correct name, but
         *  the wrong parameters was found.
         */
        Symbol wrongSym;

        /** An auxiliary explanation set in case of instantiation errors.
         */
        JCDiagnostic explanation;


        public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            throw new AssertionError();
        }

        /** Print the (debug only) name of the kind of error.
         */
        @Override
        public String toString() {
            return debugName + " wrongSym=" + wrongSym + " explanation=" + explanation;
        }

        /** Update wrongSym and explanation and return this.
         */
        ResolveError setWrongSym(Symbol sym, JCDiagnostic explanation) {
            this.wrongSym = sym;
            this.explanation = explanation;
            return this;
        }

        /** Update wrongSym and return this.
         */
        ResolveError setWrongSym(Symbol sym) {
            this.wrongSym = sym;
            this.explanation = null;
            return this;
        }

        @Override
        public boolean exists() {
            switch (kind) {
            case HIDDEN:
            case ABSENT_VAR:
            case ABSENT_MTH:
            case ABSENT_TYP:
                return false;
            default:
                return true;
            }
        }

        /** Report error.
         *  @param log       The error log to be used for error reporting.
         *  @param pos       The position to be used for error reporting.
         *  @param site      The original type from where the selection took place.
         *  @param name      The name of the symbol to be resolved.
         *  @param argtypes  The invocation's value arguments,
         *                   if we looked for a method.
         *  @param typeargtypes  The invocation's type arguments,
         *                   if we looked for a method.
         */
        void report(Log log, DiagnosticPosition pos, Type site, Name name,
                    List<Type> argtypes, List<Type> typeargtypes) {
            if (name != name.table.error) {
                JCDiagnostic kindname = absentKindName(kind);
                String idname = name.toString();
                String args = "";
                String typeargs = "";
                if (kind >= WRONG_MTHS && kind <= ABSENT_MTH) {
                    if (isOperator(name)) {
                        log.error(pos, MsgSym.MESSAGE_OPERATOR_CANNOT_BE_APPLIED,
                                  name, Type.toString(argtypes));
                        return;
                    }
                    if (name == name.table.init) {
                        kindname = JCDiagnostic.fragment(MsgSym.KINDNAME_CONSTRUCTOR);
                        idname = site.tsym.name.toString();
                    }
                    args = "(" + Type.toString(argtypes) + ")";
                    if (typeargtypes != null && typeargtypes.nonEmpty())
                        typeargs = "<" + Type.toString(typeargtypes) + ">";
                }
                if (kind == WRONG_MTH) {
                    String wrongSymStr;
                    if (wrongSym instanceof MethodSymbol)
                        wrongSymStr =
                                types.toJavaFXString((MethodSymbol) wrongSym.asMemberOf(site, types),
                                    ((MethodSymbol) wrongSym).params);
                    else
                        wrongSymStr = wrongSym.toString();
                    log.error(pos,
                              MsgSym.MESSAGE_CANNOT_APPLY_SYMBOL + (explanation != null ? ".1" : ""),
                              wrongSymStr,
                              types.location(wrongSym, site),
                              typeargs,
                              types.toJavaFXString(argtypes),
                              explanation);
                } else if (site.tsym.name.len != 0) {
                    if (site.tsym.kind == PCK && !site.tsym.exists())
                        log.error(pos, MsgSym.MESSAGE_DOES_NOT_EXIST, site.tsym);
                    else
                        log.error(pos, MsgSym.MESSAGE_CANNOT_RESOLVE_LOCATION,
                                  kindname, idname, args, typeargs,
                                  typeKindName(site), site);
                } else {
                    log.error(pos, MsgSym.MESSAGE_CANNOT_RESOLVE, kindname, idname, args, typeargs);
                }
            }
        }
//where
            /** A name designates an operator if it consists
             *  of a non-empty sequence of operator symbols +-~!/*%&|^<>=
             */
            boolean isOperator(Name name) {
                int i = 0;
                while (i < name.len &&
                       "+-~!*/%&|^<>=".indexOf(name.byteAt(i)) >= 0) i++;
                return i > 0 && i == name.len;
            }
    }

    /** Resolve error class indicating that a symbol is not accessible.
     */
    class AccessError extends ResolveError {

        AccessError(Symbol sym) {
            this(null, null, sym);
        }

        AccessError(JavafxEnv<JavafxAttrContext> env, Type site, Symbol sym) {
            super(HIDDEN, sym, "access error");
            this.env = env;
            this.site = site;
            if (debugResolve)
                log.error(MsgSym.MESSAGE_PROC_MESSAGER, sym + " @ " + site + " is inaccessible.");
        }

        private JavafxEnv<JavafxAttrContext> env;
        private Type site;

        /** Report error.
         *  @param log       The error log to be used for error reporting.
         *  @param pos       The position to be used for error reporting.
         *  @param site      The original type from where the selection took place.
         *  @param name      The name of the symbol to be resolved.
         *  @param argtypes  The invocation's value arguments,
         *                   if we looked for a method.
         *  @param typeargtypes  The invocation's type arguments,
         *                   if we looked for a method.
         */
        @Override
        void report(Log log, DiagnosticPosition pos, Type site, Name name,
                    List<Type> argtypes, List<Type> typeargtypes) {
            if (sym.owner.type.tag != ERROR) {
                if (sym.name == sym.name.table.init && sym.owner != site.tsym)
                    new ResolveError(ABSENT_MTH, sym.owner, "absent method " + sym).report(
                        log, pos, site, name, argtypes, typeargtypes);
                if ((sym.flags() & PUBLIC) != 0
                    || (env != null && this.site != null
                        && !isAccessible(env, this.site)))
                    log.error(pos, MsgSym.MESSAGE_NOT_DEF_ACCESS_CLASS_INTF_CANNOT_ACCESS,
                        sym, sym.location());
                else if ((sym.flags() & (PRIVATE | PROTECTED)) != 0)
                    log.error(pos, MsgSym.MESSAGE_REPORT_ACCESS, sym,
                              JavafxTreeInfo.flagNames(sym.flags() & (PRIVATE | PROTECTED)),
                              sym.location());
                else
                    log.error(pos, MsgSym.MESSAGE_NOT_DEF_PUBLIC_CANNOT_ACCESS,
                              sym, sym.location());
            }
        }
    }

    /** Resolve error class indicating that an instance member was accessed
     *  from a static context.
     */
    class StaticError extends ResolveError {
        StaticError(Symbol sym) {
            super(STATICERR, sym, "static error");
        }

        /** Report error.
         *  @param log       The error log to be used for error reporting.
         *  @param pos       The position to be used for error reporting.
         *  @param site      The original type from where the selection took place.
         *  @param name      The name of the symbol to be resolved.
         *  @param argtypes  The invocation's value arguments,
         *                   if we looked for a method.
         *  @param typeargtypes  The invocation's type arguments,
         *                   if we looked for a method.
         */
        @Override
        void report(Log log,
                    DiagnosticPosition pos,
                    Type site,
                    Name name,
                    List<Type> argtypes,
                    List<Type> typeargtypes) {
            String symstr = ((sym.kind == TYP && sym.type.tag == CLASS)
                ? types.erasure(sym.type)
                : sym).toString();
            log.error(pos, MsgSym.MESSAGE_NON_STATIC_CANNOT_BE_REF,
                      kindName(sym), symstr);
        }
    }

    /** Resolve error class indicating an ambiguous reference.
     */
    class AmbiguityError extends ResolveError {
        Symbol sym1;
        Symbol sym2;

        AmbiguityError(Symbol sym1, Symbol sym2) {
            super(AMBIGUOUS, sym1, "ambiguity error");
            this.sym1 = sym1;
            this.sym2 = sym2;
        }

        /** Report error.
         *  @param log       The error log to be used for error reporting.
         *  @param pos       The position to be used for error reporting.
         *  @param site      The original type from where the selection took place.
         *  @param name      The name of the symbol to be resolved.
         *  @param argtypes  The invocation's value arguments,
         *                   if we looked for a method.
         *  @param typeargtypes  The invocation's type arguments,
         *                   if we looked for a method.
         */
        @Override
        void report(Log log, DiagnosticPosition pos, Type site, Name name,
                    List<Type> argtypes, List<Type> typeargtypes) {
            AmbiguityError pair = this;
            while (true) {
                if (pair.sym1.kind == AMBIGUOUS)
                    pair = (AmbiguityError)pair.sym1;
                else if (pair.sym2.kind == AMBIGUOUS)
                    pair = (AmbiguityError)pair.sym2;
                else break;
            }
            Name sname = pair.sym1.name;
            if (sname == sname.table.init) sname = pair.sym1.owner.name;
            log.error(pos, MsgSym.MESSAGE_REF_AMBIGUOUS, sname,
                      kindName(pair.sym1),
                      pair.sym1,
                      types.location(pair.sym1, site),
                      kindName(pair.sym2),
                      pair.sym2,
                      types.location(pair.sym2, site));
        }
    }

    public boolean isInheritedIn(Symbol sym, Symbol clazz, JavafxTypes types) {
        switch ((int)(sym.flags_field & Flags.AccessFlags)) {
        default: // error recovery
        case PUBLIC:
            return true;
        case PRIVATE:
            return sym.owner == clazz;
        case PROTECTED:
            // we model interfaces as extending Object
            return (clazz.flags() & INTERFACE) == 0;
        case 0:
            ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
            Set superSet = new HashSet<Type>();
            if (clazz.type != null) {
                supertypes.append(clazz.type);
                superSet.add(clazz.type);
            }
            types.getSupertypes(clazz, supertypes, superSet);

            boolean foundInherited = false;
            for (Type supType : supertypes.toList()) {
                if (supType.tsym == sym.owner) {
                    foundInherited = true;
                    break;
                }
                else if (supType.isErroneous()) {
                    return true; // Error recovery
                }
                else if (supType.tsym != null && (supType.tsym.flags() & COMPOUND) != 0) {
                    continue;
                }
            }
            return foundInherited && (clazz.flags() & INTERFACE) == 0;
        }
    }
}
