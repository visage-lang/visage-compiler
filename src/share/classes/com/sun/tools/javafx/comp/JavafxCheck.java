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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.Flags.ANNOTATION;
import static com.sun.tools.javac.code.Flags.SYNCHRONIZED;
import static com.sun.tools.javac.code.Kinds.*;
import com.sun.tools.javac.code.Lint.LintCategory;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.ErrorType;
import com.sun.tools.javac.code.Type.ForAll;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.Type.MethodType;
import static com.sun.tools.javac.code.TypeTags.*;
import static com.sun.tools.javac.code.TypeTags.WILDCARD;
import com.sun.tools.javac.comp.Infer;
import com.sun.tools.javac.jvm.ByteCodes;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javafx.comp.JavafxAttr.Sequenceness;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;
import com.sun.javafx.api.JavafxBindStatus;

/** Type checking helper class for the attribution phase.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class JavafxCheck {
    protected static final Context.Key<JavafxCheck> javafxCheckKey =
	new Context.Key<JavafxCheck>();

    private final JavafxDefs defs;
    private final Name.Table names;
    private final Log log;
    private final JavafxSymtab syms;
    private final Infer infer;
    private final Target target;
    private final Source source;
    private final JavafxTypes types;
    private final JavafxTreeInfo treeinfo;
    private final JavafxResolve rs;

    // The set of lint options currently in effect. It is initialized
    // from the context, and then is set/reset as needed by Attr as it 
    // visits all the various parts of the trees during attribution.
    private Lint lint;

    enum WriteKind {
        ASSIGN,
        INIT_NON_BIND,
        INIT_BIND
    }

    public static JavafxCheck instance(Context context) {
	JavafxCheck instance = context.get(javafxCheckKey);
	if (instance == null)
	    instance = new JavafxCheck(context);
	return instance;
    }

    public static void preRegister(final Context context) {
        context.put(javafxCheckKey, new Context.Factory<JavafxCheck>() {
	       public JavafxCheck make() {
		   return new JavafxCheck(context);
	       }
        });
    }

    protected JavafxCheck(Context context) {
	context.put(javafxCheckKey, this);

        defs = JavafxDefs.instance(context);
	names = Name.Table.instance(context);
	log = Log.instance(context);
	syms = (JavafxSymtab) Symtab.instance(context);
	infer = Infer.instance(context);
	this.types = JavafxTypes.instance(context);
	Options options = Options.instance(context);
	target = Target.instance(context);
        source = Source.instance(context);
	lint = Lint.instance(context);
        treeinfo = (JavafxTreeInfo)JavafxTreeInfo.instance(context);

	allowGenerics = source.allowGenerics();
	//allowAnnotations = source.allowAnnotations();
	complexInference = options.get("-complexinference") != null;

	boolean verboseDeprecated = lint.isEnabled(LintCategory.DEPRECATION);
	boolean verboseUnchecked = lint.isEnabled(LintCategory.UNCHECKED);

	deprecationHandler = new MandatoryWarningHandler(log,verboseDeprecated, MsgSym.MESSAGEPREFIX_DEPRECATED);
	uncheckedHandler = new MandatoryWarningHandler(log, verboseUnchecked, MsgSym.MESSAGEPREFIX_UNCHECKED);
        rs = JavafxResolve.instance(context);
    }


    /** Switch: generics enabled?
     */
    boolean allowGenerics;

    /** Switch: annotations enabled?
     */
    //boolean allowAnnotations;

    /** Switch: -complexinference option set?
     */
    boolean complexInference;

    /** A table mapping flat names of all compiled classes in this run to their
     *  symbols; maintained from outside.
     */
    public Map<Name,ClassSymbol> compiled = new HashMap<Name, ClassSymbol>();

    /** A handler for messages about deprecated usage.
     */
    private MandatoryWarningHandler deprecationHandler;

    /** A handler for messages about unchecked or unsafe usage.
     */
    private MandatoryWarningHandler uncheckedHandler;


/* *************************************************************************
 * Errors and Warnings
 **************************************************************************/
    Lint setLint(Lint newLint) {
	Lint prev = lint;
	lint = newLint;
	return prev;
    }

    /** Warn about deprecated symbol.
     *  @param pos        Position to be used for error reporting.
     *  @param sym        The deprecated symbol.
     */ 
    void warnDeprecated(DiagnosticPosition pos, Symbol sym) {
	if (!lint.isSuppressed(LintCategory.DEPRECATION))
	    deprecationHandler.report(pos, MsgSym.MESSAGE_HAS_BEEN_DEPRECATED, sym, sym.location());
    }

    /** Warn about unchecked operation.
     *  @param pos        Position to be used for error reporting.
     *  @param msg        A string describing the problem.
     */
    public void warnUnchecked(DiagnosticPosition pos, String msg, Object... args) {
	if (!lint.isSuppressed(LintCategory.UNCHECKED))
	    uncheckedHandler.report(pos, msg, args);
    }

    /**
     * Report any deferred diagnostics.
     */
    public void reportDeferredDiagnostics() {
	deprecationHandler.reportDeferredDiagnostic();
	uncheckedHandler.reportDeferredDiagnostic();
    }


    /** Report a failure to complete a class.
     *  @param pos        Position to be used for error reporting.
     *  @param ex         The failure to report.
     */
    public Type completionError(DiagnosticPosition pos, CompletionFailure ex) {
	log.error(pos, MsgSym.MESSAGE_CANNOT_ACCESS, ex.sym, ex.errmsg);
	if (ex instanceof ClassReader.BadClassFile) throw new Abort();
	else return syms.errType;
    }

    /** Report a type error.
     *  @param pos        Position to be used for error reporting.
     *  @param problem    A string describing the error.
     *  @param found      The type that was found.
     *  @param req        The type that was required.
     */
    Type typeError(DiagnosticPosition pos, Object problem, Type found, Type req) {
        String foundAsJavaFXType = types.toJavaFXString(found);
        String requiredAsJavaFXType = types.toJavaFXString(req);
	log.error(pos, MsgSym.MESSAGE_PROB_FOUND_REQ, problem, foundAsJavaFXType, requiredAsJavaFXType);
	return syms.errType;
    }

    Type typeError(DiagnosticPosition pos, String problem, Type found, Type req, Object explanation) {
        String foundAsJavaFXType = types.toJavaFXString(found);
        String requiredAsJavaFXType = types.toJavaFXString(req);
	log.error(pos, MsgSym.MESSAGE_PROB_FOUND_REQ_1, problem, foundAsJavaFXType, requiredAsJavaFXType, explanation);
	return syms.errType;
    }

    /** Report an error that wrong type tag was found.
     *  @param pos        Position to be used for error reporting.
     *  @param required   An internationalized string describing the type tag
     *                    required.
     *  @param found      The type that was found.
     */
    Type typeTagError(DiagnosticPosition pos, Object required, Object found) {
        Object requiredAsJavaFXType = required;
        if (required instanceof Type) {
            requiredAsJavaFXType = types.toJavaFXString((Type) requiredAsJavaFXType);
        }
        Object foundAsJavaFXType = found;
        if (foundAsJavaFXType instanceof Type) {
            foundAsJavaFXType = types.toJavaFXString((Type) foundAsJavaFXType);
        }
	log.error(pos, MsgSym.MESSAGE_TYPE_FOUND_REQ, foundAsJavaFXType, requiredAsJavaFXType);
	return syms.errType;
    }

    /** Report an error that symbol cannot be referenced before super
     *  has been called.
     *  @param pos        Position to be used for error reporting.
     *  @param sym        The referenced symbol.
     */
    void earlyRefError(DiagnosticPosition pos, Symbol sym) {
	log.error(pos, MsgSym.MESSAGE_CANNOT_REF_BEFORE_CTOR_CALLED, sym);
    }

    /** Report duplicate declaration error.
     */
    void duplicateError(DiagnosticPosition pos, Symbol sym) {
	if (sym.type == null || !sym.type.isErroneous()) {
	    log.error(pos, MsgSym.MESSAGE_ALREADY_DEFINED, sym, sym.location());
	}
    }

    /** Report array/varargs duplicate declaration 
     */
    void varargsDuplicateError(DiagnosticPosition pos, Symbol sym1, Symbol sym2) {
	if (!sym1.type.isErroneous() && !sym2.type.isErroneous()) {
	    log.error(pos, MsgSym.MESSAGE_ARRAY_AND_VARARGS, sym1, sym2, sym2.location());
	}
    }

/* ************************************************************************
 * duplicate declaration checking
 *************************************************************************/

    /** Check that variable does not hide variable with same name in
     *	immediately enclosing local scope.
     *	@param pos	     Position for error reporting.
     *	@param v	     The symbol.
     *	@param s	     The scope.
     */
    void checkTransparentVar(DiagnosticPosition pos, VarSymbol v, Scope s) {
	if (s.next != null) {
	    for (Scope.Entry e = s.next.lookup(v.name);
		 e.scope != null && e.sym.owner == v.owner;
		 e = e.next()) {
		if (e.sym.kind == VAR &&
		    (e.sym.owner.kind & (VAR | MTH)) != 0 &&
		    v.name != names.error) {
		    duplicateError(pos, e.sym);
		    return;
		}
	    }
	}
    }

    /** Check that a class or interface does not hide a class or
     *	interface with same name in immediately enclosing local scope.
     *	@param pos	     Position for error reporting.
     *	@param c	     The symbol.
     *	@param s	     The scope.
     */
    void checkTransparentClass(DiagnosticPosition pos, ClassSymbol c, Scope s) {
	if (s.next != null) {
	    for (Scope.Entry e = s.next.lookup(c.name);
		 e.scope != null && e.sym.owner == c.owner;
		 e = e.next()) {
		if (e.sym.kind == TYP &&
		    (e.sym.owner.kind & (VAR | MTH)) != 0 &&
		    c.name != names.error) {
		    duplicateError(pos, e.sym);
		    return;
		}
	    }
	}
    }

    /** Check that class does not have the same name as one of
     *	its enclosing classes, or as a class defined in its enclosing scope.
     *	return true if class is unique in its enclosing scope.
     *	@param pos	     Position for error reporting.
     *	@param name	     The class name.
     *	@param s	     The enclosing scope.
     */
    boolean checkUniqueClassName(DiagnosticPosition pos, Name name, Scope s) {
	for (Scope.Entry e = s.lookup(name); e.scope == s; e = e.next()) {
	    if (e.sym.kind == TYP && e.sym.name != names.error) {
		duplicateError(pos, e.sym);
		return false;
	    }
	}
	for (Symbol sym = s.owner; sym != null; sym = sym.owner) {
	    if (sym.kind == TYP && sym.name == name && sym.name != names.error) {
		duplicateError(pos, sym);
		return true;
	    }
	}
	return true;
    }

/* *************************************************************************
 * Class name generation
 **************************************************************************/

    /** Return name of local class.
     *  This is of the form    <enclClass> $ n <classname>
     *  where
     *    enclClass is the flat name of the enclosing class,
     *    classname is the simple name of the local class
     */
    Name localClassName(ClassSymbol c) {
	for (int i=1; ; i++) {
	    Name flatname = names.
		fromString("" + c.owner.enclClass().flatname +
                           target.syntheticNameChar() + i +
                           c.name);
	    if (compiled.get(flatname) == null) return flatname;
	}
    }

/* *************************************************************************
 * Type Checking
 **************************************************************************/

    private Type deLocationize(Type external) {
	if (external.tag == CLASS) {
            Name flatname = ((ClassSymbol) external.tsym).flatname;
            Type deloc = defs.delocationize(flatname);
            if (deloc != null) {
                throw new AssertionError("At this point we should not have Location(s). This is most likely JavafxReader problem. It should convert all the Location types to the \"real\" types.");
            }
        }
        return external;
    }
    
    /** Check that a given type is assignable to a given proto-type.
     *  If it is, return the type, otherwise return errType.
     *  @param pos        Position to be used for error reporting.
     *  @param found      The type that was found.
     *  @param req        The type that was required.
     */
    Type checkType(DiagnosticPosition pos, Type foundRaw, Type reqRaw, Sequenceness pSequenceness) {
        Type req = deLocationize(reqRaw);
        Type found = deLocationize(foundRaw);
        Type realFound = found;
	if (req.tag == ERROR)
	    return req;
        if (found == syms.unreachableType)
            return found;
	if (found.tag == FORALL) {
            if (req == syms.javafx_UnspecifiedType)
                // Is this the right thing to do?  FIXME
                return types.erasure(found);
            else
	        return instantiatePoly(pos, (ForAll)found, req, convertWarner(pos, found, req));
        }
	if (req.tag == NONE || req == syms.javafx_UnspecifiedType)
	    return found;
        if (types.isSequence(req)) {  
            req = types.elementType(req);
            pSequenceness = Sequenceness.REQUIRED;
        }
        if (types.isSequence(found) || types.isArray(found)) {  
            if (pSequenceness != Sequenceness.DISALLOWED) {
                found = types.isSequence(found) ? types.elementType(found) : types.elemtype(found);
            } else {
                return typeError(pos, JCDiagnostic.fragment(MsgSym.MESSAGE_INCOMPATIBLE_TYPES), found, req);
            }
        }
	if (types.isAssignable(found, req, convertWarner(pos, found, req))) {
            if (req.tag <= LONG && found.tag >= FLOAT && found.tag <= DOUBLE) {
                // FUTURE/FIXME: return typeError(pos, JCDiagnostic.fragment(MsgSym.MESSAGE_INCOMPATIBLE_TYPES), found, req);
                String foundAsJavaFXType = types.toJavaFXString(found);
                String requiredAsJavaFXType = types.toJavaFXString(req);
	        log.warning(pos, MsgSym.MESSAGE_PROB_FOUND_REQ, JCDiagnostic.fragment(MsgSym.MESSAGE_POSSIBLE_LOSS_OF_PRECISION),
                        foundAsJavaFXType, requiredAsJavaFXType);
            }
	    return realFound;
       }

        // use the JavafxClassSymbol's supertypes to see if req is in the supertypes of found.
        ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
        Set<Type> superSet = new HashSet<Type>();
        supertypes.append(found);
        superSet.add(found);

        types.getSupertypes(found.tsym, supertypes, superSet);

        for (Type baseType : supertypes) {
            if (types.isAssignable(baseType, req, convertWarner(pos, found, req)))
                return realFound;
        }

	if (found.tag <= DOUBLE && req.tag <= DOUBLE) {
            String foundAsJavaFXType = types.toJavaFXString(found);
            String requiredAsJavaFXType = types.toJavaFXString(req);
            log.warning(pos.getStartPosition(), MsgSym.MESSAGE_PROB_FOUND_REQ, JCDiagnostic.fragment(MsgSym.MESSAGE_POSSIBLE_LOSS_OF_PRECISION),
                    foundAsJavaFXType, requiredAsJavaFXType);
            return realFound;
        }
	if (found.isSuperBound()) {
	    log.error(pos, MsgSym.MESSAGE_ASSIGNMENT_FROM_SUPER_BOUND, found);
	    return syms.errType;
	}
	if (req.isExtendsBound()) {
	    log.error(pos, MsgSym.MESSAGE_ASSIGNMENT_TO_EXTENDS_BOUND, req);
	    return syms.errType;
	}
	return typeError(pos, JCDiagnostic.fragment(MsgSym.MESSAGE_INCOMPATIBLE_TYPES), found, req);
    }

    /** Instantiate polymorphic type to some prototype, unless
     *  prototype is `anyPoly' in which case polymorphic type
     *  is returned unchanged.
     */
    Type instantiatePoly(DiagnosticPosition pos, ForAll t, Type pt, Warner warn) {
	if (pt == Infer.anyPoly && complexInference) {
	    return t;
	} else if (pt == Infer.anyPoly || pt.tag == NONE) {
	    Type newpt = t.qtype.tag <= VOID ? t.qtype : syms.objectType;
	    return instantiatePoly(pos, t, newpt, warn);
	} else if (pt.tag == ERROR) {
	    return pt;
	} else {
	    try {
		return infer.instantiateExpr(t, pt, warn);
	    } catch (Infer.NoInstanceException ex) {
		if (ex.isAmbiguous) {
		    JCDiagnostic d = ex.getDiagnostic();
		    log.error(pos,
                  d!=null ? MsgSym.MESSAGE_UNDETERMINDED_TYPE_1 : MsgSym.MESSAGE_UNDETERMINDED_TYPE,
                  t, d);
		    return syms.errType;
		} else {
		    JCDiagnostic d = ex.getDiagnostic();
		    return typeError(pos,
                 JCDiagnostic.fragment(d!=null ? MsgSym.MESSAGE_INCOMPATIBLE_TYPES_1 : MsgSym.MESSAGE_INCOMPATIBLE_TYPES, d),
                 t, pt);
		}
	    }
	}
    }

    /** Check that a given type can be cast to a given target type.
     *  Return the result of the cast.
     *  @param pos        Position to be used for error reporting.
     *  @param found      The type that is being cast.
     *  @param req        The target type of the cast.
     */
    Type checkCastable(DiagnosticPosition pos, Type found, Type req) {
	if (found.tag == FORALL && found instanceof ForAll) {
	    instantiatePoly(pos, (ForAll) found, req, castWarner(pos, found, req));
	    return req;
	} else if (types.isCastable(found, req, castWarner(pos, found, req))) {
	    return req;
        }
        // use the JavafxClassSymbol's supertypes to see if req is in the supertypes of found.
        else if (found.tsym != null && found.tsym instanceof JavafxClassSymbol) {
            ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
            Set<Type> superSet = new HashSet<Type>();
            supertypes.append(found);
            superSet.add(found);

            types.getSupertypes(found.tsym, supertypes, superSet);

            for (Type baseType : supertypes) {
                if (types.isCastable(baseType, req, castWarner(pos, found, req)))
                    return req;
            }
        }

        if (req.tsym != null && req.tsym instanceof JavafxClassSymbol) {
            ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
            Set<Type> superSet = new HashSet<Type>();
            supertypes.append(req);
            superSet.add(req);

            types.getSupertypes(req.tsym, supertypes, superSet);
            for (Type baseType : supertypes) {
                if (types.isCastable(baseType, found, castWarner(pos, found, req)))
                    return req;
            }
        }
        
        return typeError(pos,
            JCDiagnostic.fragment(MsgSym.MESSAGE_INCONVERTIBLE_TYPES),
	    found, req);
    }
//where
        /** Is type a type variable, or a (possibly multi-dimensional) array of
	 *  type variables?
	 */
	boolean isTypeVar(Type t) {
	    return t.tag == TYPEVAR || t.tag == ARRAY && isTypeVar(types.elemtype(t));
	}

    /** Check that a type is within some bounds.
     *
     *  Used in TypeApply to verify that, e.g., X in V<X> is a valid
     *  type argument.
     *  @param pos           Position to be used for error reporting.
     *  @param a             The type that should be bounded by bs.
     *  @param bs            The bound.
     */
    private void checkExtends(DiagnosticPosition pos, Type a, TypeVar bs) {
	if (a.isUnbound()) {
	    return;
	} else if (a.tag != WILDCARD) {
	    a = types.upperBound(a);
	    for (List<Type> l = types.getBounds(bs); l.nonEmpty(); l = l.tail) {
		if (!types.isSubtype(a, l.head)) {
		    log.error(pos, MsgSym.MESSAGE_NOT_WITHIN_BOUNDS, a);
		    return;
		}
	    }
	} else if (a.isExtendsBound()) {
	    if (!types.isCastable(bs.getUpperBound(), types.upperBound(a), Warner.noWarnings))
		log.error(pos, MsgSym.MESSAGE_NOT_WITHIN_BOUNDS, a);
	} else if (a.isSuperBound()) {
	    if (types.notSoftSubtype(types.lowerBound(a), bs.getUpperBound()))
		log.error(pos, MsgSym.MESSAGE_NOT_WITHIN_BOUNDS, a);
	}
    }

    /** Check that type is different from 'void'.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     */
    Type checkNonVoid(DiagnosticPosition pos, Type t) {
	if (t.tag == VOID) {
	    log.error(pos, MsgSym.MESSAGE_VOID_NOT_ALLOWED_HERE);
	    return syms.errType;
	} else {
	    return t;
	}
    }

    /** Check that type is a class or interface type.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     */
    Type checkClassType(DiagnosticPosition pos, Type t) {
	if (t.tag != CLASS && t.tag != ERROR)
            return typeTagError(pos,
                                JCDiagnostic.fragment(MsgSym.MESSAGE_TYPE_REQ_CLASS),
                                (t.tag == TYPEVAR)
                                ? JCDiagnostic.fragment(MsgSym.MESSAGE_TYPE_PARAMETER, t)
                                : t); 
	else
	    return t;
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
    void checkAssignable(DiagnosticPosition pos, VarSymbol v, JFXTree base, Type site, JavafxEnv<JavafxAttrContext> env, WriteKind writeKind) {
        //TODO: for attributes they are always final -- this should really be checked in JavafxClassReader
        //TODO: rebutal, actual we should just use a different final
        if ((v.flags() & FINAL) != 0 && !types.isJFXClass(v.owner) &&
            ((v.flags() & HASINIT) != 0
             ||
             !((base == null ||
               (base.getFXTag() == JavafxTag.IDENT && JavafxTreeInfo.name(base) == names._this)) &&
               isAssignableAsBlankFinal(v, env)))) {
            log.error(pos, MsgSym.MESSAGE_CANNOT_ASSIGN_VAL_TO_FINAL_VAR, v);
        } else if ((v.flags() & JavafxFlags.IS_DEF) != 0L) {
            log.error(pos, MsgSym.MESSAGE_JAVAFX_CANNOT_ASSIGN_TO_DEF, v);
        } else if ((v.flags() & Flags.PARAMETER) != 0L) {
            log.error(pos, MsgSym.MESSAGE_JAVAFX_CANNOT_ASSIGN_TO_PARAMETER, v);
        } else {
            // now check access permissions for write/init
            switch (writeKind) {
                case INIT_NON_BIND:
                    if ((v.flags() & JavafxFlags.PUBLIC_INIT) != 0L) {
                        // it is an initialization, and init is explicitly allowed
                        return;
                    }
                    break;
            }
            if (!rs.isAccessibleForWrite(env, site, v)) {
                String msg;
                switch (writeKind) {
                    case INIT_BIND:
                        msg = MsgSym.MESSAGE_JAVAFX_REPORT_BIND_ACCESS;
                        break;
                    case INIT_NON_BIND:
                        msg = MsgSym.MESSAGE_JAVAFX_REPORT_INIT_ACCESS;
                        break;
                    default:
                        msg = MsgSym.MESSAGE_JAVAFX_REPORT_WRITE_ACCESS;
                        break;
                }
                log.error(pos, msg, v,
                        JavafxCheck.protectionString(v.flags()),
                        v.location());
            }
        }
    }

    void checkBidiBind(JFXExpression init, JavafxBindStatus bindStatus, JavafxEnv<JavafxAttrContext> env) {
        if (bindStatus.isBidiBind()) {
            Symbol initSym = null;
            JFXTree base = null;
            Type site = null;
            switch (init.getFXTag()) {
                case IDENT: {
                    initSym = ((JFXIdent) init).sym;
                    base = null;
                    site = env.enclClass.sym.type;
                    break;
                }
                case SELECT: {
                    JFXSelect select = (JFXSelect) init;
                    initSym = select.sym;
                    base = select.getExpression();
                    site = select.type;
                    break;
                }
            }
            if (initSym instanceof VarSymbol) {
                checkAssignable(init.pos(), (VarSymbol) initSym, base, site, env, WriteKind.INIT_BIND);
            } else {
                log.error(init.pos(), MsgSym.MESSAGE_JAVAFX_EXPR_UNSUPPORTED_FOR_BIDI_BIND);
            }
        }
    }

    /**
     * Return element type for a sequence type, and report error otherwise.
     */
    public Type checkSequenceElementType (DiagnosticPosition pos, Type t) {
        if (types.isSequence(t))
            return types.elementType(t);
        if (t.tag != ERROR) {
            return typeTagError(pos, types.sequenceType(syms.unknownType), t);
        }
        return syms.errType;
    }

    /** Check that type is a class or interface type.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     *  @param noBounds    True if type bounds are illegal here.
     */
    Type checkClassType(DiagnosticPosition pos, Type t, boolean noBounds) {
	t = checkClassType(pos, t);
	if (noBounds && t.isParameterized()) {
	    List<Type> args = t.getTypeArguments();
	    while (args.nonEmpty()) {
		if (args.head.tag == WILDCARD)
		    return typeTagError(pos,
					Log.getLocalizedString(MsgSym.MESSAGE_TYPE_REQ_EXACT),
					args.head);
		args = args.tail;
	    }
	}
	return t;
    }

    /** Check that type is a reifiable class, interface or array type.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     */
    Type checkReifiableReferenceType(DiagnosticPosition pos, Type t) {
	if (t.tag != CLASS && t.tag != ARRAY && t.tag != ERROR) {
	    return typeTagError(pos,
				JCDiagnostic.fragment(MsgSym.MESSAGE_TYPE_REQ_CLASS_ARRAY),
				t);
	} else if (!types.isReifiable(t)) {
	    log.error(pos, MsgSym.MESSAGE_ILLEGAL_GENERIC_TYPE_FOR_INSTOF);
	    return syms.errType;
	} else {
	    return t;
	}
    }

    /** Check that type is a reference type, i.e. a class, interface or array type
     *  or a type variable.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     */
    Type checkRefType(DiagnosticPosition pos, Type t) {
	switch (t.tag) {
	case CLASS:
	case ARRAY:
	case TYPEVAR:
	case WILDCARD:
	case ERROR:
	    return t;
	default:
	    return typeTagError(pos,
				JCDiagnostic.fragment(MsgSym.MESSAGE_TYPE_REQ_REF),
				t);
	}
    }

    /** Check that type is a null or reference type.
     *  @param pos           Position to be used for error reporting.
     *  @param t             The type to be checked.
     */
    Type checkNullOrRefType(DiagnosticPosition pos, Type t) {
	switch (t.tag) {
	case CLASS:
	case ARRAY:
	case TYPEVAR:
	case WILDCARD:
	case BOT:
	case ERROR:
	    return t;
	default:
	    return typeTagError(pos,
				JCDiagnostic.fragment(MsgSym.MESSAGE_TYPE_REQ_REF),
				t);
	}
    }

    /** Check that flag set does not contain elements of two conflicting sets. s
     *  Return true if it doesn't.
     *  @param pos           Position to be used for error reporting.
     *  @param flags         The set of flags to be checked.
     *  @param set1          Conflicting flags set #1.
     *  @param set2          Conflicting flags set #2.
     */
    boolean checkDisjoint(DiagnosticPosition pos, long flags, long set1, long set2) {
        if ((flags & set1) != 0 && (flags & set2) != 0) {
            log.error(pos,
		      MsgSym.MESSAGE_ILLEGAL_COMBINATION_OF_MODIFIERS,
		      JavafxTreeInfo.flagNames(JavafxTreeInfo.firstFlag(flags & set1)),
		      JavafxTreeInfo.flagNames(JavafxTreeInfo.firstFlag(flags & set2)));
            return false;
        } else
            return true;
    }

    /** Check that given modifiers are legal for given symbol and
     *  return modifiers together with any implicit modififiers for that symbol.
     *  Warning: we can't use flags() here since this method
     *  is called during class enter, when flags() would cause a premature
     *  completion.
     *  @param pos           Position to be used for error reporting.
     *  @param flags         The set of modifiers given in a definition.
     *  @param sym           The defined symbol.
     */
    long checkFlags(DiagnosticPosition pos, long flags, Symbol sym, JFXTree tree) {
	long mask;
	long implicit = 0;
	switch (sym.kind) {
	case VAR:
	    if (sym.owner.kind != TYP)
		mask = LocalVarFlags;
	    else if ((sym.owner.flags_field & INTERFACE) != 0)
		mask = implicit = InterfaceVarFlags;
	    else
		mask = VarFlags;
	    break;
	case MTH:
	    if (sym.name == names.init) {
		if ((sym.owner.flags_field & ENUM) != 0) { 
		    // enum constructors cannot be declared public or
		    // protected and must be implicitly or explicitly
		    // private
		    implicit = PRIVATE;
		    mask = PRIVATE;
		} else
		    mask = ConstructorFlags;
	    }  else if ((sym.owner.flags_field & INTERFACE) != 0)
		mask = implicit = InterfaceMethodFlags;
	    else {
		mask = MethodFlags;
	    }
	    // Imply STRICTFP if owner has STRICTFP set.
	    if (((flags|implicit) & Flags.ABSTRACT) == 0)
	      implicit |= sym.owner.flags_field & STRICTFP;
	    break;
	case TYP:
	    if (sym.isLocal()) {
		mask = LocalClassFlags;
                if (sym.name.len == 0 || true /*allow for all inner classes since they have to be static*/) { // Anonymous class
                    // Anonymous classes in static methods are themselves static;
                    // that's why we admit STATIC here.
                    mask |= STATIC;
                    // JLS: Anonymous classes are final.
                    implicit |= FINAL;
                }
		if ((sym.owner.flags_field & STATIC) == 0 &&
		    (flags & ENUM) != 0)
                    log.error(pos, MsgSym.MESSAGE_ENUMS_MUST_BE_STATIC);
	    } else if (sym.owner.kind == TYP) {
		mask = MemberClassFlags;
		if (sym.owner.owner.kind == PCK ||
                    (sym.owner.flags_field & STATIC) != 0)
                    mask |= STATIC;
                else if ((flags & ENUM) != 0)
                    log.error(pos, MsgSym.MESSAGE_ENUMS_MUST_BE_STATIC);
		// Nested interfaces and enums are always STATIC (Spec ???)
		if ((flags & (INTERFACE | ENUM)) != 0 ) implicit = STATIC;
	    } else {
		mask = ClassFlags;
	    }
	    // Interfaces are always ABSTRACT
	    if ((flags & INTERFACE) != 0) implicit |= ABSTRACT;

	    // Imply STRICTFP if owner has STRICTFP set.
	    implicit |= sym.owner.flags_field & STRICTFP;
	    break;
	default:
	    throw new AssertionError();
	}
	long illegal = flags & StandardFlags & ~mask;
        if (illegal != 0) {
	    if ((illegal & INTERFACE) != 0) {
		log.error(pos, MsgSym.MESSAGE_INTF_NOT_ALLOWED_HERE);
		mask |= INTERFACE;
	    }
	    else {
		log.error(pos,
			  MsgSym.MESSAGE_MOD_NOT_ALLOWED_HERE, JavafxTreeInfo.flagNames(illegal));
	    }
	}
        else if ((sym.kind == TYP ||
		  // ISSUE: Disallowing abstract&private is no longer appropriate
		  // in the presence of inner classes. Should it be deleted here?
		  checkDisjoint(pos, flags,
				ABSTRACT,
				PRIVATE | STATIC))
		 &&
		 checkDisjoint(pos, flags,
			       ABSTRACT | INTERFACE,
			       FINAL | NATIVE | SYNCHRONIZED)
		 &&
                 checkDisjoint(pos, flags,
                               PUBLIC,
                               PRIVATE | PROTECTED | JavafxFlags.PACKAGE_ACCESS | JavafxFlags.SCRIPT_PRIVATE)
		 &&
                 checkDisjoint(pos, flags,
                               PRIVATE,
                               PUBLIC | PROTECTED | JavafxFlags.PACKAGE_ACCESS | JavafxFlags.SCRIPT_PRIVATE)
		 &&
                 checkDisjoint(pos, flags,
                               JavafxFlags.SCRIPT_PRIVATE,
                               PRIVATE | PROTECTED | PUBLIC | JavafxFlags.PACKAGE_ACCESS)
		 &&
                 checkDisjoint(pos, flags,
                               JavafxFlags.PACKAGE_ACCESS,
                               PRIVATE | PROTECTED | PUBLIC | JavafxFlags.SCRIPT_PRIVATE)
		 &&
		 checkDisjoint(pos, flags,
			       FINAL,
			       VOLATILE)
		 &&
		 (sym.kind == TYP ||
		  checkDisjoint(pos, flags,
				ABSTRACT | NATIVE,
				STRICTFP))) {
	    // skip
        }
        return flags & (mask | ~StandardFlags) | implicit;
    }

/* *************************************************************************
 * Type Validation
 **************************************************************************/

    /** Validate a type expression. That is,
     *  check that all type arguments of a parametric type are within
     *  their bounds. This must be done in a second phase after type attributon
     *  since a class might have a subclass as type parameter bound. E.g:
     *
     *  class B<A extends C> { ... }
     *  class C extends B<C> { ... }
     *
     *  and we can't make sure that the bound is already attributed because
     *  of possible cycles.
     */
    private Validator validator = new Validator();

    /** Visitor method: Validate a type expression, if it is not null, catching
     *  and reporting any completion failures.
     */
    void validate(JFXTree tree) {
	try {
	    if (tree != null) tree.accept(validator);
	} catch (CompletionFailure ex) {
	    completionError(tree.pos(), ex);
	}
    }

    /** Visitor method: Validate a list of type expressions.
     */
    void validate(List<? extends JFXTree> trees) {
	for (List<? extends JFXTree> l = trees; l.nonEmpty(); l = l.tail)
	    validate(l.head);
    }

    /** A visitor class for type validation.
     */
    class Validator extends JavafxTreeScanner {

        @Override
        public void visitSelect(JFXSelect tree) {
	    if (tree.type.tag == CLASS) {
                visitSelectInternal(tree);

                // Check that this type is either fully parameterized, or
                // not parameterized at all.
                if (tree.selected.type.isParameterized() && tree.type.tsym.type.getTypeArguments().nonEmpty())
                    log.error(tree.pos(), MsgSym.MESSAGE_IMPROPERLY_FORMED_TYPE_PARAM_MISSING);
            }
	}
        public void visitSelectInternal(JFXSelect tree) {
            if (tree.type.getEnclosingType().tag != CLASS &&
                tree.selected.type.isParameterized()) {
                // The enclosing type is not a class, so we are
                // looking at a static member type.  However, the
                // qualifying expression is parameterized.
                log.error(tree.pos(), MsgSym.MESSAGE_CANNOT_SELECT_STATIC_CLASS_FROM_PARAM_TYPE);
            } else {
                // otherwise validate the rest of the expression
                validate(tree.selected);
            }
        }

	/** Default visitor method: do nothing.
	 */
	@Override
	public void visitTree(JFXTree tree) {
	}
    }

/* *************************************************************************
 * Exception checking
 **************************************************************************/

    /* The following methods treat classes as sets that contain
     * the class itself and all their subclasses
     */

    /** Is given type a subtype of some of the types in given list?
     */
    boolean subset(Type t, List<Type> ts) {
	for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
	    if (types.isSubtype(t, l.head)) return true;
	return false;
    }

    /** Is given type a subtype or supertype of
     *  some of the types in given list?
     */
    boolean intersects(Type t, List<Type> ts) {
	for (List<Type> l = ts; l.nonEmpty(); l = l.tail)
	    if (types.isSubtype(t, l.head) || types.isSubtype(l.head, t)) return true;
	return false;
    }

    /** Add type set to given type list, unless it is a subclass of some class
     *  in the list.
     */
    List<Type> incl(Type t, List<Type> ts) {
	return subset(t, ts) ? ts : excl(t, ts).prepend(t);
    }

    /** Remove type set from type set list.
     */
    List<Type> excl(Type t, List<Type> ts) {
	if (ts.isEmpty()) {
	    return ts;
	} else {
	    List<Type> ts1 = excl(t, ts.tail);
	    if (types.isSubtype(ts.head, t)) return ts1;
	    else if (ts1 == ts.tail) return ts;
	    else return ts1.prepend(ts.head);
	}
    }

    /** Form the union of two type set lists.
     */
    List<Type> union(List<Type> ts1, List<Type> ts2) {
	List<Type> ts = ts1;
	for (List<Type> l = ts2; l.nonEmpty(); l = l.tail)
	    ts = incl(l.head, ts);
	return ts;
    }

    /** Form the difference of two type lists.
     */
    List<Type> diff(List<Type> ts1, List<Type> ts2) {
	List<Type> ts = ts1;
	for (List<Type> l = ts2; l.nonEmpty(); l = l.tail)
	    ts = excl(l.head, ts);
	return ts;
    }

    /** Form the intersection of two type lists.
     */
    public List<Type> intersect(List<Type> ts1, List<Type> ts2) {
	List<Type> ts = List.nil();
	for (List<Type> l = ts1; l.nonEmpty(); l = l.tail)
	    if (subset(l.head, ts2)) ts = incl(l.head, ts);
	for (List<Type> l = ts2; l.nonEmpty(); l = l.tail)
	    if (subset(l.head, ts1)) ts = incl(l.head, ts);
	return ts;
    }

    /** Is exc an exception symbol that need not be declared?
     */
    boolean isUnchecked(ClassSymbol exc) {
	return
	    exc.kind == ERR ||
	    exc.isSubClass(syms.errorType.tsym, types) ||
	    exc.isSubClass(syms.runtimeExceptionType.tsym, types);
    }

    /** Is exc an exception type that need not be declared?
     */
    boolean isUnchecked(Type exc) {
	return
	    (exc.tag == TYPEVAR) ? isUnchecked(types.supertype(exc)) :
	    (exc.tag == CLASS) ? isUnchecked((ClassSymbol)exc.tsym) :
	    exc.tag == BOT;
    }

    /** Same, but handling completion failures.
     */
    boolean isUnchecked(DiagnosticPosition pos, Type exc) {
	try {
	    return isUnchecked(exc);
	} catch (CompletionFailure ex) {
	    completionError(pos, ex);
	    return true;
	}
    }

    /** Is exc handled by given exception list?
     */
    boolean isHandled(Type exc, List<Type> handled) {
	return isUnchecked(exc) || subset(exc, handled);
    }

    /** Return all exceptions in thrown list that are not in handled list.
     *  @param thrown     The list of thrown exceptions.
     *  @param handled    The list of handled exceptions.
     */
    List<Type> unHandled(List<Type> thrown, List<Type> handled) {
	List<Type> unhandled = List.nil();
	for (List<Type> l = thrown; l.nonEmpty(); l = l.tail)
	    if (!isHandled(l.head, handled)) unhandled = unhandled.prepend(l.head);
	return unhandled;
    }

/* *************************************************************************
 * Overriding/Implementation checking
 **************************************************************************/

    /** The level of access protection given by a flag set,
     *  where PRIVATE is highest and PUBLIC is lowest.
     */
    static int protection(long flags) {
        // because the SCRIPT_PRIVATE bit is too high for the switch, test it later
        switch ((short)(flags & Flags.AccessFlags)) {
        case PRIVATE: return 3;
        case PROTECTED: return 1;
        default:
        case PUBLIC: return 0;
        // 'package' vs script-private
        case 0: return ((flags & JavafxFlags.SCRIPT_PRIVATE)==0)? 2 : 3;
        }
    }

    /** A string describing the access permission given by a flag set.
     *  This always returns a space-separated list of Java Keywords.
     */
    public static String protectionString(long flags) {
	long flags1 = flags & JavafxFlags.AccessFlags;
	return JavafxTreeInfo.flagNames(flags1);
    }

    /** A customized "cannot override" error message.
     *  @param m      The overriding method.
     *  @param other  The overridden method.
     *  @return       An internationalized string.
     */
    static Object cannotOverride(MethodSymbol m, MethodSymbol other) {
	String key;
	if ((other.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_CANNOT_OVERRIDE;
	else if ((m.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_CANNOT_IMPLEMENT;
	else
	    key = MsgSym.MESSAGE_CLASHES_WITH;
	return JCDiagnostic.fragment(key, m, m.location(), other, other.location());
    }

    /** A customized "override" warning message.
     *  @param m      The overriding method.
     *  @param other  The overridden method.
     *  @return       An internationalized string.
     */
    static Object uncheckedOverrides(MethodSymbol m, MethodSymbol other) {
	String key;
	if ((other.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_UNCHECKED_OVERRIDE;
	else if ((m.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_UNCHECKED_IMPLEMENT;
	else 
	    key = MsgSym.MESSAGE_UNCHECKED_CLASH_WITH;
	return JCDiagnostic.fragment(key, m, m.location(), other, other.location());
    }

    /** A customized "override" warning message.
     *  @param m      The overriding method.
     *  @param other  The overridden method.
     *  @return       An internationalized string.
     */
    static Object varargsOverrides(MethodSymbol m, MethodSymbol other) {
	String key;
	if ((other.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_VARARGS_OVERRIDE;
	else  if ((m.owner.flags() & INTERFACE) == 0) 
	    key = MsgSym.MESSAGE_VARARGS_IMPLEMENT;
	else
	    key = MsgSym.MESSAGE_VARARGS_CLASH_WITH;
	return JCDiagnostic.fragment(key, m, m.location(), other, other.location());
    }

    /** Check that this method conforms with overridden method 'other'.
     *  where `origin' is the class where checking started.
     *  Complications:
     *  (1) Do not check overriding of synthetic methods
     *      (reason: they might be final).
     *      todo: check whether this is still necessary.
     *  (2) Admit the case where an interface proxy throws fewer exceptions
     *      than the method it implements. Augment the proxy methods with the
     *      undeclared exceptions in this case.
     *  (3) When generics are enabled, admit the case where an interface proxy
     *	    has a result type
     *      extended by the result type of the method it implements.
     *      Change the proxies result type to the smaller type in this case.
     *
     *  @param tree         The tree from which positions
     *			    are extracted for errors.
     *  @param m            The overriding method.
     *  @param other        The overridden method.
     *  @param origin       The class of which the overriding method
     *			    is a member.
     */
    private void checkOverride(JFXTree tree,
		       MethodSymbol m,
		       MethodSymbol other,
		       ClassSymbol origin) {
	// Don't check overriding of synthetic methods or by bridge methods.
	if ((m.flags() & (SYNTHETIC|BRIDGE)) != 0 || (other.flags() & SYNTHETIC) != 0) {
	    return;
	}

	// Error if static method overrides instance method (JLS 8.4.6.2).
	if ((m.flags() & STATIC) != 0 &&
		   (other.flags() & STATIC) == 0) {
	    log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree), MsgSym.MESSAGE_OVERRIDE_STATIC,
		      cannotOverride(m, other));
	    return;
	}

	// Error if instance method overrides static or final
	// method (JLS 8.4.6.1).
	if ((other.flags() & FINAL) != 0 ||
		 (m.flags() & STATIC) == 0 &&
		 (other.flags() & STATIC) != 0) {
	    log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree), MsgSym.MESSAGE_OVERRIDE_METH,
		      cannotOverride(m, other),
		      JavafxTreeInfo.flagNames(other.flags() & (FINAL | STATIC)));
            return;
        }

        // Error if bound function overrides non-bound.
        if ((other.flags() & JavafxFlags.BOUND) == 0 && (m.flags() & JavafxFlags.BOUND) != 0) {
            log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree), MsgSym.MESSAGE_JAVAFX_BOUND_OVERRIDE_METH,
                    cannotOverride(m, other));
            return;
        }

        // Error if non-bound function overrides bound.
        if ((other.flags() & JavafxFlags.BOUND) != 0 && (m.flags() & JavafxFlags.BOUND) == 0) {
            log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree), MsgSym.MESSAGE_JAVAFX_NON_BOUND_OVERRIDE_METH,
                    cannotOverride(m, other));
            return;
        }

        if ((m.owner.flags() & ANNOTATION) != 0) {
                // handled in validateAnnotationMethod
            return;
        }

	// Error if overriding method has weaker access (JLS 8.4.6.3).
/*---------------  Taken out. Javafx doesn't have the JLS 8.4.6.3 rule...
	if ((origin.flags() & INTERFACE) == 0 &&
		 protection(m.flags()) > protection(other.flags())) {
	    log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree), "override.weaker.access",
		      cannotOverride(m, other),
		      protectionString(other.flags()));
	    return;

	}
----------------- */
	Type mt = types.memberType(origin.type, m);
	Type ot = types.memberType(origin.type, other);
	// Error if overriding result type is different
	// (or, in the case of generics mode, not a subtype) of
	// overridden result type. We have to rename any type parameters
	// before comparing types.
	List<Type> mtvars = mt.getTypeArguments();
	List<Type> otvars = ot.getTypeArguments();
	Type mtres = mt.getReturnType();
	Type otres = types.subst(ot.getReturnType(), otvars, mtvars);

	overrideWarner.warned = false;
	boolean resultTypesOK =
	    types.returnTypeSubstitutable(mt, ot, otres, overrideWarner);
	if (!resultTypesOK) {
	    if (!source.allowCovariantReturns() &&
		m.owner != origin &&
		m.owner.isSubClass(other.owner, types)) {
		// allow limited interoperability with covariant returns
	    } else {
		typeError(JavafxTreeInfo.diagnosticPositionFor(m, tree),
			  JCDiagnostic.fragment(MsgSym.MESSAGE_OVERRIDE_INCOMPATIBLE_RET,
					 cannotOverride(m, other)),
			  mtres, otres);
		return;
	    }
	} else if (overrideWarner.warned) {
	    warnUnchecked(JavafxTreeInfo.diagnosticPositionFor(m, tree),
			  MsgSym.MESSAGE_PROB_FOUND_REQ,
			  JCDiagnostic.fragment(MsgSym.MESSAGE_OVERRIDE_UNCHECKED_RET,
					      uncheckedOverrides(m, other)),
			  mtres, otres);
	}
	
	// Error if overriding method throws an exception not reported
	// by overridden method.
	List<Type> otthrown = types.subst(ot.getThrownTypes(), otvars, mtvars);
	List<Type> unhandled = unHandled(mt.getThrownTypes(), otthrown);
	if (unhandled.nonEmpty()) {
	    log.error(JavafxTreeInfo.diagnosticPositionFor(m, tree),
		      MsgSym.MESSAGE_OVERRIDE_METH_DOES_NOT_THROW,
		      cannotOverride(m, other),
		      unhandled.head);
	    return;
	}

	// Optional warning if varargs don't agree 
	if ((((m.flags() ^ other.flags()) & Flags.VARARGS) != 0)
	    && lint.isEnabled(Lint.LintCategory.OVERRIDES)) {
	    log.warning(JavafxTreeInfo.diagnosticPositionFor(m, tree),
			((m.flags() & Flags.VARARGS) != 0)
			? MsgSym.MESSAGE_OVERRIDE_VARARGS_MISSING
			: MsgSym.MESSAGE_OVERRIDE_VARARGS_EXTRA,
			varargsOverrides(m, other));
	} 

	// Warn if instance method overrides bridge method (compiler spec ??)
	if ((other.flags() & BRIDGE) != 0) {
	    log.warning(JavafxTreeInfo.diagnosticPositionFor(m, tree), MsgSym.MESSAGE_OVERRIDE_BRIDGE,
			uncheckedOverrides(m, other));
	}

	// Warn if a deprecated method overridden by a non-deprecated one.
	if ((other.flags() & DEPRECATED) != 0 
	    && (m.flags() & DEPRECATED) == 0 
	    && m.outermostClass() != other.outermostClass()
	    && !isDeprecatedOverrideIgnorable(other, origin)) {
	    warnDeprecated(JavafxTreeInfo.diagnosticPositionFor(m, tree), other);
	}
    }
    // where
	private boolean isDeprecatedOverrideIgnorable(MethodSymbol m, ClassSymbol origin) {
	    // If the method, m, is defined in an interface, then ignore the issue if the method
	    // is only inherited via a supertype and also implemented in the supertype,
	    // because in that case, we will rediscover the issue when examining the method
	    // in the supertype.
	    // If the method, m, is not defined in an interface, then the only time we need to
	    // address the issue is when the method is the supertype implemementation: any other
	    // case, we will have dealt with when examining the supertype classes
	    ClassSymbol mc = m.enclClass();
	    Type st = types.supertype(origin.type);
	    if (st.tag != CLASS)
		return true;
	    MethodSymbol stimpl = types.implementation(m, (ClassSymbol)st.tsym, false);

	    if (mc != null && ((mc.flags() & INTERFACE) != 0)) {
		List<Type> intfs = types.interfaces(origin.type);
		return (intfs.contains(mc.type) ? false : (stimpl != null));
	    }
	    else
		return (stimpl != m);
	}


    // used to check if there were any unchecked conversions
    private Warner overrideWarner = new Warner();

    /** Check that a class does not inherit two concrete methods
     *  with the same signature.
     *  @param pos          Position to be used for error reporting.
     *  @param site         The class type to be checked.
     */
    private void checkCompatibleConcretes(DiagnosticPosition pos, Type site) {
	Type sup = types.supertype(site);
	if (sup.tag != CLASS) return;

	for (Type t1 = sup;
	     t1.tsym.type.isParameterized();
	     t1 = types.supertype(t1)) {
	    for (Scope.Entry e1 = t1.tsym.members().elems;
		 e1 != null;
		 e1 = e1.sibling) {
		Symbol s1 = e1.sym;
		if (s1.kind != MTH ||
		    (s1.flags() & (STATIC|SYNTHETIC|BRIDGE)) != 0 ||
		    !s1.isInheritedIn(site.tsym, types) ||
		    types.implementation((MethodSymbol)s1, site.tsym,
						      true) != s1)
		    continue;
		Type st1 = types.memberType(t1, s1);
		int s1ArgsLength = st1.getParameterTypes().length();
		if (st1 == s1.type) continue;

		for (Type t2 = sup;
		     t2.tag == CLASS;
		     t2 = types.supertype(t2)) {
		    for (Scope.Entry e2 = t1.tsym.members().lookup(s1.name);
			 e2.scope != null;
			 e2 = e2.next()) {
			Symbol s2 = e2.sym;
			if (s2 == s1 ||
			    s2.kind != MTH ||
			    (s2.flags() & (STATIC|SYNTHETIC|BRIDGE)) != 0 ||
			    s2.type.getParameterTypes().length() != s1ArgsLength ||
			    !s2.isInheritedIn(site.tsym, types) ||
			    types.implementation((MethodSymbol)s2, site.tsym,
							      true) != s2)
			    continue;
			Type st2 = types.memberType(t2, s2);
			if (types.overrideEquivalent(st1, st2))
			    log.error(pos, MsgSym.MESSAGE_CONCRETE_INHERITANCE_CONFLICT,
				      s1, t1, s2, t2, sup);
		    }
		}
	    }
	}
    }

    /** Check that classes (or interfaces) do not each define an abstract
     *  method with same name and arguments but incompatible return types.
     *  @param pos          Position to be used for error reporting.
     *  @param t1           The first argument type.
     *  @param t2           The second argument type.
     */
    private boolean checkCompatibleAbstracts(DiagnosticPosition pos,
					    Type t1,
					    Type t2) {
        return checkCompatibleAbstracts(pos, t1, t2,
                                        types.makeCompoundType(t1, t2));
    }

    private boolean checkCompatibleAbstracts(DiagnosticPosition pos,
					    Type t1,
					    Type t2,
					    Type site) {
	Symbol sym = firstIncompatibility(t1, t2, site);
	if (sym != null) {
	    log.error(pos, MsgSym.MESSAGE_TYPES_INCOMPATIBLE_DIFF_RET,
		      t1, t2, sym.name +
		      "(" + types.memberType(t2, sym).getParameterTypes() + ")");
	    return false;
	}
	return true;
    }

    /** Return the first method which is defined with same args
     *  but different return types in two given interfaces, or null if none
     *  exists.
     *  @param t1     The first type.
     *  @param t2     The second type.
     *  @param site   The most derived type.
     *  @returns symbol from t2 that conflicts with one in t1.
     */
    private Symbol firstIncompatibility(Type t1, Type t2, Type site) {
	Map<TypeSymbol,Type> interfaces1 = new HashMap<TypeSymbol,Type>();
	closure(t1, interfaces1);
	Map<TypeSymbol,Type> interfaces2;
	if (t1 == t2)
	    interfaces2 = interfaces1;
	else
	    closure(t2, interfaces1, interfaces2 = new HashMap<TypeSymbol,Type>());

	for (Type t3 : interfaces1.values()) {
	    for (Type t4 : interfaces2.values()) {
		Symbol s = firstDirectIncompatibility(t3, t4, site);
		if (s != null) return s;
	    }
	}
	return null;
    }

    /** Compute all the supertypes of t, indexed by type symbol. */
    private void closure(Type t, Map<TypeSymbol,Type> typeMap) {
	if (t.tag != CLASS) return;
	if (typeMap.put(t.tsym, t) == null) {
	    closure(types.supertype(t), typeMap);
	    for (Type i : types.interfaces(t))
		closure(i, typeMap);
	}
    }

    /** Compute all the supertypes of t, indexed by type symbol (except thise in typesSkip). */
    private void closure(Type t, Map<TypeSymbol,Type> typesSkip, Map<TypeSymbol,Type> typeMap) {
	if (t.tag != CLASS) return;
	if (typesSkip.get(t.tsym) != null) return;
	if (typeMap.put(t.tsym, t) == null) {
	    closure(types.supertype(t), typesSkip, typeMap);
	    for (Type i : types.interfaces(t))
		closure(i, typesSkip, typeMap);
	}
    }

    /** Return the first method in t2 that conflicts with a method from t1. */
    private Symbol firstDirectIncompatibility(Type t1, Type t2, Type site) {
	for (Scope.Entry e1 = t1.tsym.members().elems; e1 != null; e1 = e1.sibling) {
	    Symbol s1 = e1.sym;
	    Type st1 = null;
	    if (s1.kind != MTH || s1.name == defs.internalRunFunctionName ||
                    !s1.isInheritedIn(site.tsym, types)) continue;
            Symbol impl = types.implementation((MethodSymbol)s1, site.tsym, false);
            if (impl != null && (impl.flags() & ABSTRACT) == 0) continue;
	    for (Scope.Entry e2 = t2.tsym.members().lookup(s1.name); e2.scope != null; e2 = e2.next()) {
		Symbol s2 = e2.sym;
		if (s1 == s2) continue;
		if (s2.kind != MTH || !s2.isInheritedIn(site.tsym, types)) continue;
		if (st1 == null) st1 = types.memberType(t1, s1);
		Type st2 = types.memberType(t2, s2);
		if (types.overrideEquivalent(st1, st2)) {
		    List<Type> tvars1 = st1.getTypeArguments();
		    List<Type> tvars2 = st2.getTypeArguments();
		    Type rt1 = st1.getReturnType();
		    Type rt2 = types.subst(st2.getReturnType(), tvars2, tvars1);
		    boolean compat =
			types.isSameType(rt1, rt2) ||
                        rt1.tag >= CLASS && rt2.tag >= CLASS &&
                        (types.covariantReturnType(rt1, rt2, Warner.noWarnings) ||
                         types.covariantReturnType(rt2, rt1, Warner.noWarnings));
		    if (!compat) return s2;
		}
	    }
	}
	return null;
    }

    /** Check that a given method conforms with any method it overrides.
     *  @param tree         The tree from which positions are extracted
     *			    for errors.
     *  @param m            The overriding method.
     */
    void checkOverride(JFXTree tree, MethodSymbol m) {
	ClassSymbol origin = (ClassSymbol)m.owner;
        boolean doesOverride = false;
	if ((origin.flags() & ENUM) != 0 && names.finalize.equals(m.name))
	    if (m.overrides(syms.enumFinalFinalize, origin, types, false)) {
		log.error(tree.pos(), MsgSym.MESSAGE_ENUM_NO_FINALIZE);
		return;
	    }
            ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
            Set<Type> superSet = new HashSet<Type>();
            types.getSupertypes(origin, supertypes, superSet);

        for (Type t : supertypes) {
            if (t.tag == CLASS) {
                TypeSymbol c = t.tsym;
                Scope.Entry e = c.members().lookup(m.name);
                while (e.scope != null) {
                    e.sym.complete();
                    if (m.overrides(e.sym, origin, types, false)) {
                        checkOverride(tree, m, (MethodSymbol)e.sym, origin);
                        doesOverride = true;
                    }
                    e = e.next();
                }
            }
	}
        boolean declaredOverride = (m.flags() & JavafxFlags.OVERRIDE) != 0;
        if (doesOverride) {
            if (!declaredOverride && (m.flags() & (Flags.SYNTHETIC|Flags.STATIC)) == 0) {
                log.warning(tree.pos(), MsgSym.MESSAGE_JAVAFX_SHOULD_BE_DECLARED_OVERRIDE, m);
            }
        } else {
            if (declaredOverride) {
                log.error(tree.pos(), MsgSym.MESSAGE_JAVAFX_DECLARED_OVERRIDE_DOES_NOT, m);
            }
        }
    }

    /** Check that all abstract members of given class have definitions.
     *  @param pos          Position to be used for error reporting.
     *  @param c            The class.
     */
    void checkAllDefined(DiagnosticPosition pos, ClassSymbol c) {
	try {
	    MethodSymbol undef = firstUndef(c, c);
	    if (undef != null) {
                if ((c.flags() & ENUM) != 0 &&
                    types.supertype(c.type).tsym == syms.enumSym &&
                    (c.flags() & FINAL) == 0) {
                    // add the ABSTRACT flag to an enum
                    c.flags_field |= ABSTRACT;
                } else {
                    MethodSymbol undef1 =
                        new MethodSymbol(undef.flags(), undef.name,
                                         types.memberType(c.type, undef), undef.owner);
                    log.error(pos, MsgSym.MESSAGE_DOES_NOT_OVERRIDE_ABSTRACT,
                              c, undef1, undef1.location());
                }
            }
	} catch (CompletionFailure ex) {
	    completionError(pos, ex);
	}
    }
//where
        /** Return first abstract member of class `c' that is not defined
	 *  in `impl', null if there is none.
	 */
	private MethodSymbol firstUndef(ClassSymbol impl, ClassSymbol c) {
	    MethodSymbol undef = null;
	    // Do not bother to search in classes that are not abstract,
	    // since they cannot have abstract members.
	    if (c == impl || (c.flags() & (ABSTRACT | INTERFACE)) != 0) {
		Scope s = c.members();
		for (Scope.Entry e = s.elems;
		     undef == null && e != null;
		     e = e.sibling) {
		    if (e.sym.kind == MTH &&
			(e.sym.flags() & (ABSTRACT|IPROXY)) == ABSTRACT) {
			MethodSymbol absmeth = (MethodSymbol)e.sym;
			MethodSymbol implmeth = types.implementation(absmeth, impl, true);
			if (implmeth == null || implmeth == absmeth) {
                            undef = absmeth;
                        }
		    }
		}
		if (undef == null) {
		    Type st = types.supertype(c.type);
		    if (st.tag == CLASS)
			undef = firstUndef(impl, (ClassSymbol)st.tsym);
		}
		for (List<Type> l = types.interfaces(c.type);
		     undef == null && l.nonEmpty();
		     l = l.tail) {
		    undef = firstUndef(impl, (ClassSymbol)l.head.tsym);
		}
	    }
            return undef;
	}

    /** Check for cyclic references. Issue an error if the
     *  symbol of the type referred to has a LOCKED flag set.
     *
     *  @param pos      Position to be used for error reporting.
     *  @param t        The type referred to.
     */
    void checkNonCyclic(DiagnosticPosition pos, Type t) {
	checkNonCyclicInternal(pos, t);
    }


    void checkNonCyclic(DiagnosticPosition pos, TypeVar t) {
        checkNonCyclic1(pos, t, new HashSet<TypeVar>());
    }

    private void checkNonCyclic1(DiagnosticPosition pos, Type t, Set<TypeVar> seen) {
        final TypeVar tv;
        if (seen.contains(t)) {
            tv = (TypeVar)t;
            tv.bound = new ErrorType();
            log.error(pos, MsgSym.MESSAGE_CYCLIC_INHERITANCE, t);
        } else if (t.tag == TYPEVAR) {
            tv = (TypeVar)t;
            seen.add(tv);
            for (Type b : types.getBounds(tv))
                checkNonCyclic1(pos, b, seen);
        }
    }

    /** Check for cyclic references. Issue an error if the
     *  symbol of the type referred to has a LOCKED flag set.
     *
     *  @param pos      Position to be used for error reporting.
     *  @param t        The type referred to.
     *  @returns        True if the check completed on all attributed classes
     */
    private boolean checkNonCyclicInternal(DiagnosticPosition pos, Type t) {
	boolean complete = true; // was the check complete?
	//- System.err.println("checkNonCyclicInternal("+t+");");//DEBUG
	Symbol c = t.tsym;
	if ((c.flags_field & ACYCLIC) != 0) return true;

	if ((c.flags_field & LOCKED) != 0) {
	    noteCyclic(pos, (ClassSymbol)c);
	} else if (!c.type.isErroneous()) {
	    try {
		c.flags_field |= LOCKED;
		if (c.type.tag == CLASS) {
		    ClassType clazz = (ClassType)c.type;
		    if (clazz.interfaces_field != null)
			for (List<Type> l=clazz.interfaces_field; l.nonEmpty(); l=l.tail)
			    complete &= checkNonCyclicInternal(pos, l.head);
		    if (clazz.supertype_field != null) {
			Type st = clazz.supertype_field;
			if (st != null && st.tag == CLASS)
			    complete &= checkNonCyclicInternal(pos, st);
		    }
		    if (c.owner.kind == TYP)
			complete &= checkNonCyclicInternal(pos, c.owner.type);
		}
	    } finally {
		c.flags_field &= ~LOCKED;
	    }
	}
	if (complete)
	    complete = ((c.flags_field & UNATTRIBUTED) == 0) && c.completer == null;
	if (complete) c.flags_field |= ACYCLIC;
	return complete;
    }

    /** Note that we found an inheritance cycle. */
    private void noteCyclic(DiagnosticPosition pos, ClassSymbol c) {
	log.error(pos, MsgSym.MESSAGE_CYCLIC_INHERITANCE, c);
	for (List<Type> l=types.interfaces(c.type); l.nonEmpty(); l=l.tail)
	    l.head = new ErrorType((ClassSymbol)l.head.tsym);
	Type st = types.supertype(c.type);
	if (st.tag == CLASS)
	    ((ClassType)c.type).supertype_field = new ErrorType((ClassSymbol)st.tsym);
	c.type = new ErrorType(c);
	c.flags_field |= ACYCLIC;
    }

    /** Check that all methods which implement some
     *  method conform to the method they implement.
     *  @param tree         The class definition whose members are checked.
     */
    void checkImplementations(JFXClassDeclaration tree) {
	checkImplementations(tree, tree.sym);
    }
//where
        /** Check that all methods which implement some
	 *  method in `ic' conform to the method they implement.
	 */
	void checkImplementations(JFXClassDeclaration tree, ClassSymbol ic) {
	    ClassSymbol origin = tree.sym;
	    for (List<Type> l = types.closure(ic.type); l.nonEmpty(); l = l.tail) {
		ClassSymbol lc = (ClassSymbol)l.head.tsym;
		if ((allowGenerics || origin != lc) && (lc.flags() & ABSTRACT) != 0) {
		    for (Scope.Entry e=lc.members().elems; e != null; e=e.sibling) {
			if (e.sym.kind == MTH &&
			    (e.sym.flags() & (STATIC|ABSTRACT)) == ABSTRACT) {
			    MethodSymbol absmeth = (MethodSymbol)e.sym;
			    MethodSymbol implmeth = types.implementation(absmeth, origin, false);
			    if (implmeth != null && implmeth != absmeth &&
				(implmeth.owner.flags() & INTERFACE) ==
				(origin.flags() & INTERFACE)) {
				// don't check if implmeth is in a class, yet
				// origin is an interface. This case arises only
				// if implmeth is declared in Object. The reason is
				// that interfaces really don't inherit from
				// Object it's just that the compiler represents
				// things that way.
				checkOverride(tree, implmeth, absmeth, origin);
			    }
			}
		    }
		}
	    }
	}

    /** Check that all abstract methods implemented by a class are
     *  mutually compatible.
     *  @param pos          Position to be used for error reporting.
     *  @param c            The class whose interfaces are checked.
     */
    void checkCompatibleSupertypes(DiagnosticPosition pos, Type c) {
	List<Type> supertypes = types.interfaces(c);
	Type supertype = types.supertype(c);
	if (supertype.tag == CLASS &&
	    (supertype.tsym.flags() & ABSTRACT) != 0)
	    supertypes = supertypes.prepend(supertype);
	for (List<Type> l = supertypes; l.nonEmpty(); l = l.tail) {
	    if (allowGenerics && !l.head.getTypeArguments().isEmpty() &&
		!checkCompatibleAbstracts(pos, l.head, l.head, c))
		return;
	    for (List<Type> m = supertypes; m != l; m = m.tail)
		if (!checkCompatibleAbstracts(pos, l.head, m.head, c))
		    return;
	}
	checkCompatibleConcretes(pos, c);
    }

    /** Check that class c does not implement directly or indirectly
     *  the same parameterized interface with two different argument lists.
     *  @param pos          Position to be used for error reporting.
     *  @param type         The type whose interfaces are checked.
     */
    void checkClassBounds(DiagnosticPosition pos, Type type) {
	checkClassBounds(pos, new HashMap<TypeSymbol,Type>(), type);
    }
//where
        /** Enter all interfaces of type `type' into the hash table `seensofar'
	 *  with their class symbol as key and their type as value. Make
	 *  sure no class is entered with two different types.
	 */
	void checkClassBounds(DiagnosticPosition pos,
			      Map<TypeSymbol,Type> seensofar,
			      Type type) {
	    if (type.isErroneous()) return;
	    for (List<Type> l = types.interfaces(type); l.nonEmpty(); l = l.tail) {
		Type it = l.head;
		Type oldit = seensofar.put(it.tsym, it);
		if (oldit != null) {
		    List<Type> oldparams = oldit.allparams();
		    List<Type> newparams = it.allparams();
		    if (!types.containsTypeEquivalent(oldparams, newparams))
			log.error(pos, MsgSym.MESSAGE_CANNOT_INHERIT_DIFF_ARG,
				  it.tsym, Type.toString(oldparams),
				  Type.toString(newparams));
		}
		checkClassBounds(pos, seensofar, it);
	    }
	    Type st = types.supertype(type);
	    if (st != null) checkClassBounds(pos, seensofar, st);
	}

    /** Enter interface into into set.
     *  If it existed already, issue a "repeated interface" error.
     */
    void checkNotRepeated(DiagnosticPosition pos, Type it, Set<Type> its) {
	if (its.contains(it))
	    log.error(pos, MsgSym.MESSAGE_REPEATED_INTERFACE);
	else {
	    its.add(it);
	}
    }
	
/* *************************************************************************
 * Miscellaneous
 **************************************************************************/
    /**
     * Return the opcode of the operator but emit an error if it is an
     * error.
     * @param pos        position for error reporting.
     * @param operator   an operator
     * @param tag        a tree tag
     * @param left       type of left hand side
     * @param right      type of right hand side
     */
    int checkOperator(DiagnosticPosition pos,
                       OperatorSymbol operator,
                       JavafxTag tag,
                       Type left,
                       Type right) {
        if (operator.opcode == ByteCodes.error) {
            log.error(pos,
                      MsgSym.MESSAGE_OPERATOR_CANNOT_BE_APPLIED,
                      treeinfo.operatorName(tag),
                      left + "," + right);
        }
        return operator.opcode;
    }


    /**
     *  Check for division by integer constant zero
     *	@param pos	     Position for error reporting.
     *	@param operator      The operator for the expression
     *	@param operand       The right hand operand for the expression
     */
    void checkDivZero(DiagnosticPosition pos, Symbol operator, Type operand) {
	if (operand.constValue() != null
	    && lint.isEnabled(Lint.LintCategory.DIVZERO)
	    && operand.tag <= LONG
	    && ((Number) (operand.constValue())).longValue() == 0) {
	    int opc = ((OperatorSymbol)operator).opcode;
	    if (opc == ByteCodes.idiv || opc == ByteCodes.imod 
		|| opc == ByteCodes.ldiv || opc == ByteCodes.lmod) {
		log.warning(pos, MsgSym.MESSAGE_DIV_ZERO);
	    }
	}
    }

    /** Check that symbol is unique in given scope.
     *	@param pos	     Position for error reporting.
     *	@param sym	     The symbol.
     *	@param s	     The scope.
     */
    boolean checkUnique(DiagnosticPosition pos, Symbol sym, Scope s) {
        if (sym.type != null && sym.type.isErroneous())
	    return true;
	if (sym.owner.name == names.any) return false;
	for (Scope.Entry e = s.lookup(sym.name); e.scope == s; e = e.next()) {
            sym.complete();
	    if (sym != e.sym &&
		sym.kind == e.sym.kind &&
		sym.name != names.error &&
                (sym.kind != MTH || types.overrideEquivalent(sym.type, e.sym.type))) {
                    if ((sym.flags() & VARARGS) != (e.sym.flags() & VARARGS))
		    varargsDuplicateError(pos, sym, e.sym);
		else 
		    duplicateError(pos, e.sym);
		return false;
	    }
	}
	return true;
    }

    /** Check that single-type import is not already imported or top-level defined,
     *	but make an exception for two single-type imports which denote the same type.
     *	@param pos	     Position for error reporting.
     *	@param sym	     The symbol.
     *	@param s	     The scope
     */
    boolean checkUniqueImport(DiagnosticPosition pos, Symbol sym, Scope s) {
	return checkUniqueImport(pos, sym, s, false);
    }

    /** Check that static single-type import is not already imported or top-level defined,
     *	but make an exception for two single-type imports which denote the same type.
     *	@param pos	     Position for error reporting.
     *	@param sym	     The symbol.
     *	@param s	     The scope
     *  @param staticImport  Whether or not this was a static import
     */
    boolean checkUniqueStaticImport(DiagnosticPosition pos, Symbol sym, Scope s) {
	return checkUniqueImport(pos, sym, s, true);
    }

    /** Check that single-type import is not already imported or top-level defined,
     *	but make an exception for two single-type imports which denote the same type.
     *	@param pos	     Position for error reporting.
     *	@param sym	     The symbol.
     *	@param s	     The scope.
     *  @param staticImport  Whether or not this was a static import
     */
    private boolean checkUniqueImport(DiagnosticPosition pos, Symbol sym, Scope s, boolean staticImport) {
	for (Scope.Entry e = s.lookup(sym.name); e.scope != null; e = e.next()) {
	    // is encountered class entered via a class declaration?
	    boolean isClassDecl = e.scope == s;
	    if ((isClassDecl || sym != e.sym) &&
		sym.kind == e.sym.kind &&
		sym.name != names.error) {
		if (!e.sym.type.isErroneous()) {
		    String what = e.sym.toString();
		    if (!isClassDecl) {
			if (staticImport)
			    log.error(pos, MsgSym.MESSAGE_ALREADY_DEFINED_STATIC_SINGLE_IMPORT, what);
			else
			    log.error(pos, MsgSym.MESSAGE_ALREADY_DEFINED_SINGLE_IMPORT, what);
		    }
		    else if (sym != e.sym)
			log.error(pos, MsgSym.MESSAGE_ALREADY_DEFINED_THIS_UNIT, what);
		}
		return false;
	    }
	}
	return true;
    }

    /** Check that a qualified name is in canonical form (for import decls).
     */
    public void checkCanonical(JFXTree tree) {
	if (!isCanonical(tree))
	    log.error(tree.pos(), MsgSym.MESSAGE_IMPORT_REQUIRES_CANONICAL,
		      JavafxTreeInfo.symbol(tree));
    }
        // where
	private boolean isCanonical(JFXTree tree) {
	    while (tree.getFXTag() == JavafxTag.SELECT) {
		JFXSelect s = (JFXSelect) tree;
		if (s.sym.owner != JavafxTreeInfo.symbol(s.selected))
		    return false;
		tree = s.selected;
	    }
	    return true;
	}

    private class ConversionWarner extends Warner {
        final String key;
	final Type found;
        final Type expected;
	public ConversionWarner(DiagnosticPosition pos, String key, Type found, Type expected) {
            super(pos);
            this.key = key;
	    this.found = found;
	    this.expected = expected;
	}

	@Override
	public void warnUnchecked() {
            boolean localWarned = this.warned;
            super.warnUnchecked();
            if (localWarned) return; // suppress redundant diagnostics
	    Object problem = JCDiagnostic.fragment(key);
	    JavafxCheck.this.warnUnchecked(pos(), MsgSym.MESSAGE_PROB_FOUND_REQ, problem, found, expected);
	}
    }

    public Warner castWarner(DiagnosticPosition pos, Type found, Type expected) {
	return new ConversionWarner(pos, MsgSym.MESSAGE_UNCHECKED_CAST_TO_TYPE, found, expected);
    }

    public Warner convertWarner(DiagnosticPosition pos, Type found, Type expected) {
	return new ConversionWarner(pos, MsgSym.MESSAGE_UNCHECKED_ASSIGN, found, expected);
    }
	
    public void warnEmptyRangeLiteral(DiagnosticPosition pos, JFXLiteral lower, JFXLiteral upper, JFXLiteral step, boolean isExclusive) {
    double lowerValue = ((Number)lower.getValue()).doubleValue();
    double upperValue = ((Number)upper.getValue()).doubleValue();
    double stepValue = step != null? ((Number)step.getValue()).doubleValue() : 1;
    if ((stepValue > 0 && lowerValue > upperValue)
            || (stepValue < 0 && lowerValue < upperValue)
            || (isExclusive && lowerValue == upperValue)) {
        log.warning(pos, MsgSym.MESSAGE_JAVAFX_RANGE_LITERAL_EMPTY);
            }
    }
        
    public Type checkFunctionType(DiagnosticPosition pos, MethodType m) {
        if (m.argtypes.length() > JavafxSymtab.MAX_FIXED_PARAM_LENGTH) {
            log.error(pos, MsgSym.MESSAGE_TOO_MANY_PARAMETERS);
            return syms.errType;
        } else {
            return syms.makeFunctionType(m);
        }
    }
}
