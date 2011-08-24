/*
* Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.tools.comp;

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.TypeTree.Cardinality;
import org.visage.tools.code.FunctionType;
import org.visage.tools.code.VisageClassSymbol;
import org.visage.tools.code.VisageFlags;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.tree.*;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.ClassType;
import static com.sun.tools.mjavac.code.TypeTags.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Options;
import javax.tools.JavaFileObject;

/**
 * Shared support for the pre-translation passes.  Not a pass itself.
 *
 * @author Maurizio Cimadamore
 * @author Robert Field
 */
public class VisagePreTranslationSupport {

    private final VisageTreeMaker visagemake;
    private final VisageDefs defs;
    private final Name.Table names;
    private final VisageCheck chk;
    private final VisageTypes types;
    private final VisageSymtab syms;
    private final VisageOptimizationStatistics optStat;

    private final boolean debugNames;
    private int tmpCount = 0;

    protected static final Context.Key<VisagePreTranslationSupport> preTranslation =
            new Context.Key<VisagePreTranslationSupport>();

    public static VisagePreTranslationSupport instance(Context context) {
        VisagePreTranslationSupport instance = context.get(preTranslation);
        if (instance == null) {
            instance = new VisagePreTranslationSupport(context);
        }
        return instance;
    }

    private VisagePreTranslationSupport(Context context) {
        context.put(preTranslation, this);

        visagemake = VisageTreeMaker.instance(context);
        defs = VisageDefs.instance(context);
        names = Name.Table.instance(context);
        chk = VisageCheck.instance(context);
        types = VisageTypes.instance(context);
        syms = (VisageSymtab)VisageSymtab.instance(context);
        optStat = VisageOptimizationStatistics.instance(context);

        String opt = Options.instance(context).get("debugNames");
        debugNames = opt != null && !opt.startsWith("n");
    }

    // Just adds a counter. prefix is expected to include "$"
    public Name syntheticName(String prefix) {
        return names.fromString(prefix + tmpCount++);
    }

    public VisageExpression defaultValue(Type type) {
        VisageExpression res;
        if (types.isSequence(type)) {
            res = visagemake.EmptySequence();
        } else {
            switch (type.tag) {
                case FLOAT:
                    res = visagemake.Literal(0F);
                    break;
                case DOUBLE:
                    res = visagemake.Literal(0.0);
                    break;
                case CHAR:
                    res = visagemake.Literal((char) 0);
                    break;
                case BYTE:
                    res = visagemake.Literal((byte) 0);
                    break;
                case SHORT:
                    res = visagemake.Literal((short) 0);
                    break;
                case INT:
                    res = visagemake.Literal((int) 0);
                    break;
                case LONG:
                    res = visagemake.Literal(0L);
                    break;
                case BOOLEAN:
                    res = visagemake.Literal(false);
                    break;
                default:
                    res = visagemake.Literal(BOT, null);
            }
        }
        res.type = type;
        return res;
    }

    public Scope getEnclosingScope(Symbol s) {
        if (s.owner.kind == Kinds.TYP) {
            return ((ClassSymbol)s.owner).members();
        }
        else if (s.owner.kind == Kinds.PCK) {
            return ((PackageSymbol)s.owner).members();
        }
        else
            return null;
    }

    public JavaFileObject sourceFile(Symbol owner) {
        for (Symbol currOwner = owner; currOwner != null; currOwner = currOwner.owner) {
            if (currOwner instanceof ClassSymbol) {
                JavaFileObject src = ((ClassSymbol)currOwner).sourcefile;
                if (src != null) {
                    return src;
                }
            }
        }
        return null;
    }

    public VisageClassSymbol makeClassSymbol(Name name, Symbol owner) {
        VisageClassSymbol classSym = new VisageClassSymbol(Flags.SYNTHETIC, name, owner);
        classSym.flatname = chk.localClassName(classSym);
        chk.compiled.put(classSym.flatname, classSym);

        // we may be able to get away without any scope stuff
        //  s.enter(sym);

        // Fill out class fields.
        classSym.completer = null;
        if (classSym.owner instanceof MethodSymbol &&
            (classSym.owner.flags() & VisageFlags.BOUND) != 0L) {
            classSym.flags_field |= VisageFlags.VISAGE_BOUND_FUNCTION_CLASS;
        }
        classSym.sourcefile = sourceFile(owner);
        classSym.members_field = new Scope(classSym);

        ClassType ct = (ClassType) classSym.type;
        // We are seeing a local or inner class.
        // Set outer_field of this class to closest enclosing class
        // which contains this class in a non-static context
        // (its "enclosing instance class"), provided such a class exists.
        Symbol owner1 = owner.enclClass();
        if (owner1.kind == Kinds.TYP) {
            ct.setEnclosingType(owner1.type);
        }

        ct.supertype_field = syms.visage_BaseType;

        return classSym;
    }

    public MethodSymbol makeDummyMethodSymbol(Symbol owner) {
        return makeDummyMethodSymbol(owner, names.empty);
    }

    public MethodSymbol makeDummyMethodSymbol(Symbol owner, Name name) {
        return new MethodSymbol(Flags.BLOCK, name, null, owner.enclClass());
    }

    VisageType makeTypeTree(Type type) {
        Type elemType = types.elementTypeOrType(type);
        VisageExpression typeExpr = visagemake.Type(elemType).setType(elemType);
        VisageTreeInfo.setSymbol(typeExpr, elemType.tsym);
        return (VisageType)visagemake.TypeClass(typeExpr, types.isSequence(type) ? Cardinality.ANY : Cardinality.SINGLETON, (ClassSymbol)type.tsym).setType(type);
    }

    VisageVar BoundLocalVar(DiagnosticPosition diagPos, Type type, Name name, VisageExpression boundExpr, Symbol owner) {
        return Var(diagPos, VisageFlags.IS_DEF, type, name, VisageBindStatus.UNIDIBIND, boundExpr, owner);
    }

    VisageVar LocalVar(DiagnosticPosition diagPos, Type type, Name name, VisageExpression expr, Symbol owner) {
        return Var(diagPos,0L, type, name, VisageBindStatus.UNBOUND, expr, owner);
    }

    private static final String idChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String suffixGen() {
        final int dig = idChars.length();
        int i = ++tmpCount;
        StringBuffer sb = new StringBuffer();
        while (i > 0) {
            int md = i % dig;
            char ch = idChars.charAt(md);
            sb.append(ch);
            i -= md;
            i = i / dig;
        }
        return sb.toString();
    }

    VisageVar SynthVar(DiagnosticPosition diagPos, VisageVarSymbol vsymParent, String id, VisageExpression initExpr, VisageBindStatus bindStatus, Type type, boolean inScriptLevel, Symbol owner) {
        optStat.recordSynthVar(id);
        String ns = "_$" + suffixGen();
        if (debugNames) {
            ns = (vsymParent==null? "" : vsymParent.toString() + "$") + id + ns;
        }
        Name name = names.fromString(ns);

        long flags = VisageFlags.SCRIPT_PRIVATE | Flags.SYNTHETIC | (inScriptLevel ? Flags.STATIC | VisageFlags.SCRIPT_LEVEL_SYNTH_STATIC : 0L);
        VisageVar var = Var(diagPos, flags, types.normalize(type), name, bindStatus, initExpr, owner);
        owner.members().enter(var.sym);
        return var;
    }

    VisageVar Var(DiagnosticPosition diagPos, long flags, Type type, Name name, VisageBindStatus bindStatus, VisageExpression expr, Symbol owner) {
        VisageVarSymbol vsym = new VisageVarSymbol(
                types,
                names,
                flags,
                name, type, owner);
        VisageVar var = visagemake.at(diagPos).Var(
                name,
                makeTypeTree(vsym.type),
                visagemake.at(diagPos).Modifiers(flags),
                expr,
                bindStatus,
                null, null);
        var.type = vsym.type;
        var.sym = vsym;
        return var;
    }
    
    VisageExpression makeCastIfNeeded(VisageExpression tree, Type type) {
        if (type == Type.noType ||
                type == null ||
                type.isErroneous() ||
                type == syms.voidType ||
                tree.type == syms.voidType ||
                tree.type == syms.unreachableType ||
                type == syms.unreachableType)
            return tree;
        else {
            tree = makeNumericBoxConversionIfNeeded(tree, type);
            return !types.isSameType(tree.type, type) &&
                   (!types.isSubtypeUnchecked(tree.type, type) ||
                   (tree.type.isPrimitive() && type.isPrimitive() ||
                   (types.isSameType(tree.type, syms.visage_EmptySequenceType) &&
                   types.isSequence(type)))) ?
                makeCast(tree, type) :
                tree;
        }
    }

    /**
     * It is necessary to add an extra cast if either source type or target type
     * is a boxed Java type - this is required because we might want to go from
     * java.Lang.Long to int and vice-versa
     */
    private boolean needNumericBoxConversion(VisageExpression tree, Type type) {
        boolean sourceIsPrimitive = tree.type.isPrimitive();
        boolean targetIsPrimitive = type.isPrimitive();
        Type unboxedSource = types.unboxedType(tree.type);
        Type unboxedTarget = types.unboxedType(type);
        return (sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && !types.isSameType(unboxedTarget, tree.type)) ||
                (targetIsPrimitive && !sourceIsPrimitive && unboxedSource != Type.noType && !types.isSameType(unboxedSource, type)) ||
                (!sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && unboxedSource!= Type.noType && !types.isSameType(type, tree.type));
    }

    private VisageExpression makeNumericBoxConversionIfNeeded(VisageExpression tree, Type type) {
        if (needNumericBoxConversion(tree, type)) {
           //either tree.type or type is primitive!
           if (tree.type.isPrimitive() && !type.isPrimitive()) {
               return makeCast(tree, types.unboxedType(type));
           }
           else if (type.isPrimitive() && !tree.type.isPrimitive()) {
               return makeCast(tree, types.unboxedType(tree.type));
           }
           else { //both are boxed types
               return makeCast(makeCast(tree, types.unboxedType(tree.type)), types.unboxedType(type));
           }
        }
        else {
            return tree;
        }
    }

    private VisageExpression makeCast(VisageExpression tree, Type type) {
        VisageExpression typeTree = makeTypeTree(type);
        VisageExpression expr = visagemake.at(tree.pos).TypeCast(typeTree, tree);
        expr.type = type;
        return expr;
    }

    void liftTypes(final VisageClassDeclaration cdecl, final Type newEncl, final Symbol newOwner) {
        class NestedClassTypeLifter extends VisageTreeScanner {

            @Override
            public void visitClassDeclaration(VisageClassDeclaration that) {
                super.visitClassDeclaration(that);
                if (that.sym != newEncl.tsym &&
                        (that.type.getEnclosingType() == Type.noType ||
                        that.type.getEnclosingType().tsym == newEncl.getEnclosingType().tsym)) {
                    Scope oldScope = getEnclosingScope(that.sym);
                    if (oldScope != null)
                        oldScope.remove(that.sym);
                    ((ClassType)that.type).setEnclosingType(newEncl);
                    that.sym.owner = newOwner;
                    newEncl.tsym.members().enter(that.sym);
                }
            }
        }
        new NestedClassTypeLifter().scan(cdecl);
    }

    Symbol makeSyntheticBuiltinsMethod(Name name) {
        return new MethodSymbol(
                Flags.PUBLIC | Flags.STATIC | VisageFlags.FUNC_IS_BUILTINS_SYNTH,
                name,
                new Type.MethodType(
                    List.of(syms.visage_ObjectType, syms.intType),
                    syms.booleanType,
                    List.<Type>nil(),
                    syms.methodClass),
                syms.visage_AutoImportRuntimeType.tsym);
    }

    Symbol makeSyntheticPointerMake() {
        return new MethodSymbol(
                Flags.PUBLIC | Flags.STATIC | VisageFlags.FUNC_POINTER_MAKE,
                defs.Pointer_make.methodName,
                new Type.MethodType(
                    List.of(syms.visage_ObjectType, syms.intType, types.erasure(syms.classType)),
                    syms.visage_PointerType,
                    List.<Type>nil(),
                    syms.methodClass),
                syms.visage_PointerType.tsym);
    }

    Name makeUniqueVarNameIn(Name name, Symbol owner) {
        while (owner.members().lookup(name).sym != null) {
            name = name.append('$', name);
        }
        return name;
    }

    boolean isNullable(VisageExpression expr) {
        if (!types.isNullable(expr.type)) {
            return false;
        }
        while (true) {
            switch (expr.getVisageTag()) {
                case OBJECT_LITERAL:
                    return false;
                case PARENS:
                    expr = ((VisageParens)expr).getExpression();
                    break;
                case BLOCK_EXPRESSION:
                    expr = ((VisageBlock)expr).getValue();
                    break;
                case CONDEXPR:
                {
                    VisageIfExpression ife = (VisageIfExpression)expr;
                    return isNullable(ife.getTrueExpression()) || isNullable(ife.getFalseExpression());
                }
                default:
                    return true;
            }
        }
    }

    //TODO: unify with hasSideEffects in TranslationSupport
    boolean hasSideEffectsInBind(VisageExpression expr) {
        class SideEffectScanner extends VisageTreeScanner {

            boolean hse = false;

            private void markSideEffects() {
                hse = true;
            }

            @Override
            public void visitAssign(VisageAssign tree) {
                // In case we add back assignment
                markSideEffects();
            }

            @Override
            public void visitInstanciate(VisageInstanciate tree) {
                markSideEffects();
            }

            @Override
            public void visitFunctionInvocation(VisageFunctionInvocation tree) {
                markSideEffects();
            }
        }
        SideEffectScanner scanner = new SideEffectScanner();
        scanner.scan(expr);
        return scanner.hse;
    }

    boolean isImmutable(List<VisageExpression> trees) {
        for (VisageExpression item : trees) {
            if (!isImmutable(item)) {
                return false;
            }
        }
        return true;
    }

    boolean isImmutable(VisageExpression tree) {
//        boolean im = isImmutableReal(tree);
//        System.err.println((im? "IMM: " : "MUT: ") + tree);
//        return im;
//    }
//    boolean isImmutableReal(VisageExpression tree) {
        //TODO: add for-loop, sequence indexed, string expression
        switch (tree.getVisageTag()) {
            case IDENT: {
                VisageIdent id = (VisageIdent) tree;
                return isImmutable(id.sym, id.getName());
            }
            case SELECT: {
                VisageSelect sel = (VisageSelect) tree;
                return (sel.sym.isStatic() || isImmutable(sel.getExpression())) && isImmutable(sel.sym, sel.getIdentifier());
            }
            case LITERAL:
            case TIME_LITERAL:
            case SEQUENCE_EMPTY:
                return true;
            case PARENS:
                return isImmutable(((VisageParens)tree).getExpression());
            case BLOCK_EXPRESSION: {
                VisageBlock be = (VisageBlock) tree;
                for (VisageExpression stmt : be.getStmts()) {
                    //TODO: OPT probably many false positive cases
                    if (stmt instanceof VisageVar) {
                        VisageVar var = (VisageVar) stmt;
                        if (!isImmutable(var.getInitializer())) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                return isImmutable(be.getValue());
            }
            case FOR_EXPRESSION: {
                VisageForExpression fe = (VisageForExpression) tree;
                for (VisageForExpressionInClause clause : fe.getForExpressionInClauses()) {
                    if (!isImmutable(clause.getSequenceExpression())) {
                        return false;
                    }
                    if (clause.getWhereExpression() != null && !isImmutable(clause.getWhereExpression())) {
                        return false;
                    }
                }
                return isImmutable(fe.getBodyExpression());
            }
            case APPLY: {
                VisageFunctionInvocation finv = (VisageFunctionInvocation) tree;
                VisageExpression meth = finv.meth;
                Symbol refSym = VisageTreeInfo.symbol(meth);
                return
                        isImmutable(meth) &&                       // method being called won't change
                        isImmutable(finv.getArguments()) &&        // arguments won't change
                        !(meth.type instanceof FunctionType) &&    // not a function value call -- over-cautious
                        (refSym instanceof MethodSymbol) &&        // call to a method, protects the next check -- over-cautious
                        (refSym.flags() & VisageFlags.BOUND) == 0; // and isn't a call to a bound function
            }
            case CONDEXPR: {
                VisageIfExpression ife = (VisageIfExpression) tree;
                return isImmutable(ife.getCondition()) && isImmutable(ife.getTrueExpression()) && isImmutable(ife.getFalseExpression());
            }
            case SEQUENCE_RANGE: {
                VisageSequenceRange rng = (VisageSequenceRange) tree;
                return isImmutable(rng.getLower()) && isImmutable(rng.getUpper()) && (rng.getStepOrNull()==null || isImmutable(rng.getStepOrNull()));
            }
            case SEQUENCE_EXPLICIT: {
                VisageSequenceExplicit se = (VisageSequenceExplicit) tree;
                for (VisageExpression item : se.getItems()) {
                    if (!isImmutable(item)) {
                        return false;
                    }
                }
                return true;
            }
            case TYPECAST: {
                VisageTypeCast tc = (VisageTypeCast) tree;
                return isImmutable(tc.getExpression());
            }
            default:
                if (tree instanceof VisageUnary) {
                    if (tree.getVisageTag().isIncDec()) {
                        return false;
                    } else {
                        return isImmutable(((VisageUnary) tree).getExpression());
                    }
                } else if (tree instanceof VisageBinary) {
                    VisageBinary b = (VisageBinary) tree;
                    return isImmutable(b.lhs) && isImmutable(b.rhs);
                } else {
                    return false;
                }
        }
    }

    private boolean isImmutable(Symbol sym, Name name) {
        if (sym.kind != Kinds.VAR) {
            return true;
        }
        VisageVarSymbol vsym = (VisageVarSymbol) sym;
        Symbol owner = sym.owner;
        return
                    name == names._this ||
                    name == names._super ||
                    (owner instanceof VisageClassSymbol && name == visagemake.ScriptAccessSymbol(owner).name) ||
                    !vsym.canChange();
     }
}

