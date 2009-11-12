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
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

/**
 * Fill in the synthetic definitions needed in a bound function
 * 
 * @author A. Sundararajan
 */
public class JavafxBoundFunctionFill extends JavafxTreeScanner {

    private final JavafxTreeMaker fxmake;
    private final JavafxDefs defs;
    private final JavafxSymtab syms;

    // assigned lazily on the first usage. This is symbol of
    // Pointer.make(Object) method.
    private MethodSymbol pointerMakeMethodSym;

    protected static final Context.Key<JavafxBoundFunctionFill> boundFuncFill =
            new Context.Key<JavafxBoundFunctionFill>();

    public static JavafxBoundFunctionFill instance(Context context) {
        JavafxBoundFunctionFill instance = context.get(boundFuncFill);
        if (instance == null) {
            instance = new JavafxBoundFunctionFill(context);
        }
        return instance;
    }

    private JavafxBoundFunctionFill(Context context) {
        context.put(boundFuncFill, this);

        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
    }

    public void fill(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        if (tree.isBound()) {
            // Fill out the bound function support vars before
            // local inflation
            boundFunctionFiller(tree);
        }
        super.visitFunctionDefinition(tree);
    }

    private void boundFunctionFiller(JFXFunctionDefinition tree) {
        MethodSymbol oldSym = tree.sym;
        MethodType oldFuncType = oldSym.type.asMethodType();
        MethodType newFuncType = new MethodType(
                oldFuncType.getParameterTypes(), // arg types
                syms.javafx_PointerType, // return type
                oldFuncType.getThrownTypes(), // Throws type
                oldFuncType.tsym);               // TypeSymbol
        tree.sym = new MethodSymbol(oldSym.flags(), oldSym.name, newFuncType, oldSym.owner);

        // Save the parameter names in the MethodSymbol
        {
            ListBuffer<Name> pNames = ListBuffer.lb();
            for (JFXVar fxVar : tree.getParams()) {
                pNames.append(fxVar.getName());
            }
            tree.sym.savedParameterNames = pNames.toList();
        }

        /*
         * For bound functions, make a synthetic bound variable with
         * initialization expression to be the return expression and return
         * the Pointer of the synthetic variable as the result.
         */
        JFXBlock blk = tree.getBodyExpression();
        if (blk != null) {
            JFXExpression returnExpr = (blk.value instanceof JFXReturn) ? ((JFXReturn) blk.value).getExpression() : blk.value;
            if (returnExpr != null) {
                fxmake.at(blk.value.pos);
                ListBuffer<JFXExpression> stmts = ListBuffer.lb();
                /*
                 * Generate a local variable for each parameter. We will later
                 * transform each param as FXObject+varNum pair during translation.
                 * These locals will be converted into instance variables of the
                 * local context class.
                 */
                for (JFXVar fxVar : tree.getParams()) {
                    JFXVar localVar = fxmake.Var(
                            fxVar.name,
                            fxVar.getJFXType(),
                            fxVar.mods,
                            null,
                            JavafxBindStatus.UNIDIBIND, null, null);
                    localVar.type = fxVar.type;
                    localVar.sym = fxVar.sym;
                    stmts.append(localVar);
                }

                stmts.appendList(blk.stats);

                // is return expression a variable declaration?
                boolean returnExprIsVar = (returnExpr.getFXTag() == JavafxTag.VAR_DEF);
                if (returnExprIsVar) {
                    stmts.append(returnExpr);
                }
                JFXVar returnVar = fxmake.Var(
                        defs.boundFunctionResultName,
                        fxmake.TypeUnknown(),
                        fxmake.Modifiers(0),
                        returnExprIsVar ? fxmake.Ident((JFXVar) returnExpr) : returnExpr,
                        JavafxBindStatus.UNIDIBIND, null, null);
                returnVar.type = oldSym.type.getReturnType();
                returnVar.sym = new VarSymbol(0L, defs.boundFunctionResultName, returnVar.type, tree.sym);
                returnVar.markBound(JavafxBindStatus.UNIDIBIND);
                stmts.append(returnVar);

                // find the symbol of Pointer.make(Object) method.
                // The select expression Pointer.make
                Type pointerMakeType = pointerMakeMethodSym().type;
                JFXSelect select = fxmake.Select(fxmake.Type(syms.javafx_PointerType), defs.make_PointerMethodName);
                select.sym = pointerMakeMethodSym();
                select.sym.flags_field |= JavafxFlags.FUNC_POINTER_MAKE;
                select.type = pointerMakeType;


                // args for Pointer.make(Object)
                JFXIdent ident = fxmake.Ident(returnVar);
                ident.type = returnVar.type;
                ident.sym = returnVar.sym;
                ListBuffer<JFXExpression> pointerMakeArgs = ListBuffer.lb();
                pointerMakeArgs.append(ident);

                // call Pointer.make($$bound$result$)
                JFXFunctionInvocation apply = fxmake.Apply(null, select, pointerMakeArgs.toList());
                apply.type = syms.javafx_PointerType;

                blk.stats = stmts.toList();
                blk.value = apply;
                blk.type = apply.type;
            }
        }
    }

    private MethodSymbol pointerMakeMethodSym() {
        if (pointerMakeMethodSym == null) {
            Symbol pointerSym = syms.javafx_PointerType.tsym;
            for (Scope.Entry e1 = pointerSym.members().lookup(defs.make_PointerMethodName);
                e1 != null;
                e1 = e1.sibling) {
                Symbol sym = e1.sym;
                MethodSymbol msym = (MethodSymbol) sym;
                if (msym.params().size() == 1) {
                    pointerMakeMethodSym = msym;
                    break;
                }
            }
        }
        assert pointerMakeMethodSym != null : "can't find Pointer.make(Object) method";
        return pointerMakeMethodSym;
    }
}

