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

    public Name syntheticName(String prefix) {
        return names.fromString(prefix + "$" + tmpCount++);
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
        classSym.addSuperType(syms.javafx_FXBaseType);

        return classSym;
    }

    public MethodSymbol makeDummyMethodSymbol(Symbol owner) {
        return new MethodSymbol(Flags.BLOCK, names.empty, null, owner);
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
        var.sym = new VarSymbol(
                flags,
                name,
                type,
                owner);
        return var;
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
}

