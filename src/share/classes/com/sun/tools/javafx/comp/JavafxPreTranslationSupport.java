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

package com.sun.tools.javafx.comp;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.ClassType;
import static com.sun.tools.mjavac.code.TypeTags.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;
import javax.tools.JavaFileObject;

/**
 * Shared support for the pre-translation passes.  Not a pass itself.
 *
 * @author Maurizio Cimadamore
 * @author Robert Field
 */
public class JavafxPreTranslationSupport {

    private final JavafxTreeMaker fxmake;
    private final JavafxDefs defs;
    private final Name.Table names;
    private final JavafxCheck chk;
    private final JavafxTypes types;
    private final JavafxSymtab syms;

    private int tmpCount = 0;

    protected static final Context.Key<JavafxPreTranslationSupport> preTranslation =
            new Context.Key<JavafxPreTranslationSupport>();

    public static JavafxPreTranslationSupport instance(Context context) {
        JavafxPreTranslationSupport instance = context.get(preTranslation);
        if (instance == null) {
            instance = new JavafxPreTranslationSupport(context);
        }
        return instance;
    }

    private JavafxPreTranslationSupport(Context context) {
        context.put(preTranslation, this);

        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);
        names = Name.Table.instance(context);
        chk = JavafxCheck.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    // Just adds a counter. prefix is expected to include "$"
    public Name syntheticName(String prefix) {
        return names.fromString(prefix + tmpCount++);
    }

    public JFXExpression defaultValue(Type type) {
        JFXExpression res;
        if (types.isSequence(type)) {
            res = fxmake.EmptySequence();
        } else {
            switch (type.tag) {
                case FLOAT:
                    res = fxmake.Literal(0F);
                    break;
                case DOUBLE:
                    res = fxmake.Literal(0.0);
                    break;
                case CHAR:
                    res = fxmake.Literal((char) 0);
                    break;
                case BYTE:
                    res = fxmake.Literal((byte) 0);
                    break;
                case SHORT:
                    res = fxmake.Literal((short) 0);
                    break;
                case INT:
                    res = fxmake.Literal((int) 0);
                    break;
                case LONG:
                    res = fxmake.Literal(0L);
                    break;
                case BOOLEAN:
                    res = fxmake.Literal(false);
                    break;
                default:
                    res = fxmake.Literal(BOT, null);
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

    public JavafxClassSymbol makeClassSymbol(Name name, Symbol owner) {
        JavafxClassSymbol classSym = new JavafxClassSymbol(Flags.SYNTHETIC, name, owner);
        classSym.flatname = chk.localClassName(classSym);
        chk.compiled.put(classSym.flatname, classSym);

        // we may be able to get away without any scope stuff
        //  s.enter(sym);

        // Fill out class fields.
        classSym.completer = null;
        if (classSym.owner instanceof MethodSymbol &&
            (classSym.owner.flags() & JavafxFlags.BOUND) != 0L) {
            classSym.flags_field |= JavafxFlags.FX_BOUND_FUNCTION_CLASS;
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

        ct.supertype_field = syms.javafx_FXBaseType;

        return classSym;
    }

    public MethodSymbol makeDummyMethodSymbol(Symbol owner) {
        return makeDummyMethodSymbol(owner, names.empty);
    }

    public MethodSymbol makeDummyMethodSymbol(Symbol owner, Name name) {
        return new MethodSymbol(Flags.BLOCK, name, null, owner);
    }

    JFXType makeTypeTree(Type type) {
        Type elemType = types.elementTypeOrType(type);
        JFXExpression typeExpr = fxmake.Type(elemType).setType(elemType);
        JavafxTreeInfo.setSymbol(typeExpr, elemType.tsym);
        return (JFXType)fxmake.TypeClass(typeExpr, types.isSequence(type) ? Cardinality.ANY : Cardinality.SINGLETON, (ClassSymbol)type.tsym).setType(type);
    }

    JFXVar BoundLocalVar(Type type, Name name, JFXExpression boundExpr, Symbol owner) {
        return Var(JavafxFlags.IS_DEF, type, name, JavafxBindStatus.UNIDIBIND, boundExpr, owner);
    }

    JFXVar LocalVar(Type type, Name name, JFXExpression expr, Symbol owner) {
        return Var(0L, type, name, JavafxBindStatus.UNBOUND, expr, owner);
    }

    JFXVar Var(long flags, Type type, Name name, JavafxBindStatus bindStatus, JFXExpression expr, Symbol owner) {
        JFXVar var = fxmake.Var(
                name,
                makeTypeTree(type),
                fxmake.Modifiers(flags),
                expr,
                bindStatus,
                null, null);
        var.type = type;
        var.sym = new JavafxVarSymbol(
                types,
                names,
                flags,
                name, type, owner);
        return var;
    }
    
    JFXExpression makeCastIfNeeded(JFXExpression tree, Type type) {
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
                   (types.isSameType(tree.type, syms.javafx_EmptySequenceType) &&
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
    private boolean needNumericBoxConversion(JFXExpression tree, Type type) {
        boolean sourceIsPrimitive = tree.type.isPrimitive();
        boolean targetIsPrimitive = type.isPrimitive();
        Type unboxedSource = types.unboxedType(tree.type);
        Type unboxedTarget = types.unboxedType(type);
        return (sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && !types.isSameType(unboxedTarget, tree.type)) ||
                (targetIsPrimitive && !sourceIsPrimitive && unboxedSource != Type.noType && !types.isSameType(unboxedSource, type)) ||
                (!sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && unboxedSource!= Type.noType && !types.isSameType(type, tree.type));
    }

    private JFXExpression makeNumericBoxConversionIfNeeded(JFXExpression tree, Type type) {
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

    private JFXExpression makeCast(JFXExpression tree, Type type) {
        JFXExpression typeTree = makeTypeTree(type);
        JFXExpression expr = fxmake.at(tree.pos).TypeCast(typeTree, tree);
        expr.type = type;
        return expr;
    }

    void liftTypes(final JFXClassDeclaration cdecl, final Type newEncl, final Symbol newOwner) {
        class NestedClassTypeLifter extends JavafxTreeScanner {

            @Override
            public void visitClassDeclaration(JFXClassDeclaration that) {
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
                Flags.PUBLIC | Flags.STATIC | JavafxFlags.FUNC_IS_BUILTINS_SYNTH,
                name,
                new Type.MethodType(
                    List.of(syms.javafx_FXObjectType, syms.intType),
                    syms.booleanType,
                    List.<Type>nil(),
                    syms.methodClass),
                syms.javafx_AutoImportRuntimeType.tsym);
    }

    Symbol makeSyntheticPointerMake() {
        return new MethodSymbol(
                Flags.PUBLIC | Flags.STATIC | JavafxFlags.FUNC_POINTER_MAKE,
                defs.Pointer_make.methodName,
                new Type.MethodType(
                    List.of(syms.javafx_FXObjectType, syms.intType, types.erasure(syms.classType)),
                    syms.javafx_PointerType,
                    List.<Type>nil(),
                    syms.methodClass),
                syms.javafx_PointerType.tsym);
    }

    Name makeUniqueVarNameIn(Name name, Symbol owner) {
        while (owner.members().lookup(name).sym != null) {
            name = name.append('$', name);
        }
        return name;
    }

    boolean isNullable(JFXExpression expr) {
        if (!types.isNullable(expr.type)) {
            return false;
        }
        while (true) {
            switch (expr.getFXTag()) {
                case OBJECT_LITERAL:
                    return false;
                case PARENS:
                    expr = ((JFXParens)expr).getExpression();
                    break;
                case BLOCK_EXPRESSION:
                    expr = ((JFXBlock)expr).getValue();
                    break;
                case CONDEXPR:
                {
                    JFXIfExpression ife = (JFXIfExpression)expr;
                    return isNullable(ife.getTrueExpression()) || isNullable(ife.getFalseExpression());
                }
                default:
                    return true;
            }
        }
    }

    //TODO: unify with hasSideEffects in TranslationSupport
    boolean hasSideEffectsInBind(JFXExpression expr) {
        class SideEffectScanner extends JavafxTreeScanner {

            boolean hse = false;

            private void markSideEffects() {
                hse = true;
            }

            @Override
            public void visitAssign(JFXAssign tree) {
                // In case we add back assignment
                markSideEffects();
            }

            @Override
            public void visitInstanciate(JFXInstanciate tree) {
                markSideEffects();
            }

            @Override
            public void visitFunctionInvocation(JFXFunctionInvocation tree) {
                markSideEffects();
            }
        }
        SideEffectScanner scanner = new SideEffectScanner();
        scanner.scan(expr);
        return scanner.hse;
    }

    boolean isImmutable(JFXExpression tree) {
        switch (tree.getFXTag()) {
            case IDENT: {
                JFXIdent id = (JFXIdent) tree;
                return isImmutable(id.sym, id.getName());
            }
            case SELECT: {
                JFXSelect sel = (JFXSelect) tree;
                return isImmutableSelector(sel) && isImmutable(sel.sym, sel.getIdentifier());
            }
            case LITERAL:
            case TIME_LITERAL:
                return true;
            case PARENS:
                return isImmutable(((JFXParens)tree).getExpression());
            case SEQUENCE_RANGE: {
                JFXSequenceRange rng = (JFXSequenceRange) tree;
                return isImmutable(rng.getLower()) && isImmutable(rng.getUpper()) && (rng.getStepOrNull()==null || isImmutable(rng.getStepOrNull()));
            }
            case SEQUENCE_EXPLICIT: {
                JFXSequenceExplicit se = (JFXSequenceExplicit) tree;
                for (JFXExpression item : se.getItems()) {
                    if (!isImmutable(item)) {
                        return false;
                    }
                }
                return true;
            }
            default:
                if (tree instanceof JFXUnary) {
                    if (tree.getFXTag().isIncDec()) {
                        return false;
                    } else {
                        return isImmutable(((JFXUnary)tree).getExpression());
                    }
                }
                return false;
        }
    }

    private boolean isImmutable(Symbol sym, Name name) {
        if (sym.kind != Kinds.VAR) {
            return true;
        }
        JavafxVarSymbol vsym = (JavafxVarSymbol) sym;
        Symbol owner = sym.owner;
        boolean isKnown = vsym.isLocal() || (owner instanceof ClassSymbol && types.getFxClass((ClassSymbol) owner) != null); //TODO: consider Java supers
        boolean isAssignedTo = vsym.isAssignedTo();
        boolean isDefinedBound = vsym.isDefinedBound();
        return
                    name == names._this ||
                    name == names._super ||
                    (owner instanceof JavafxClassSymbol && name == fxmake.ScriptAccessSymbol(owner).name) ||
                    isKnown && vsym.isDef() && !isDefinedBound || // unbound def
                    isKnown && !vsym.isWritableOutsideScript() && !isAssignedTo && !isDefinedBound; // never reassigned
     }

    private boolean isImmutableSelector(final JFXSelect tree) {
        if (tree.sym.isStatic()) {
            // If the symbol is static, no matter what the selector is, the selectot is immutable
            return true;
        }
        final JFXExpression selector = tree.getExpression();
        if (selector instanceof JFXIdent) {
            return isImmutable((JFXIdent) selector);
        } else if (selector instanceof JFXSelect) {
            return isImmutable((JFXSelect) selector);
        } else {
            return false;
        }
    }
}

