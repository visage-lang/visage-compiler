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
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

/**
 * Fill in the synthetic definitions needed in a bound function
 * and bound object literal.
 * 
 * @author A. Sundararajan
 * @author Robert Field
 */
public class JavafxBoundFiller extends JavafxTreeScanner {

    private final JavafxPreTranslationSupport preTrans;
    private final JavafxTreeMaker fxmake;
    private final JavafxDefs defs;
    private final JavafxSymtab syms;
    protected final JavafxTypes types;
    private final Name.Table names;

    protected static final Context.Key<JavafxBoundFiller> boundFuncFill =
            new Context.Key<JavafxBoundFiller>();

    public static JavafxBoundFiller instance(Context context) {
        JavafxBoundFiller instance = context.get(boundFuncFill);
        if (instance == null) {
            instance = new JavafxBoundFiller(context);
        }
        return instance;
    }

    private JavafxBoundFiller(Context context) {
        context.put(boundFuncFill, this);

        preTrans = JavafxPreTranslationSupport.instance(context);
        fxmake = JavafxTreeMaker.instance(context);
        defs = JavafxDefs.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        types = JavafxTypes.instance(context);
        names = Name.Table.instance(context);
    }

    public void fill(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.tree);
    }

    /**
     * Add variables needed when a bound for-expression body is converted into a class:
     * 
     *           var $indexof$x : Integer = $index$;
     *           var x : T;
     *           def result = bind block_value;
     */
    @Override
    public void visitForExpression(JFXForExpression tree) {
        if (tree.isBound()) {
            JFXBlock body = (JFXBlock) tree.getBodyExpression();
            assert tree.getInClauses().size() == 1 : "lower is supposed to flatten to exactly one in-clause";
            JFXForExpressionInClause clause = tree.getForExpressionInClauses().get(0);
            MethodSymbol dummyOwner = preTrans.makeDummyMethodSymbol(clause.var.sym.owner);
            JFXVar idxv = createIndexVar(clause, dummyOwner);
            JFXVar iv = createInductionVar(clause, idxv.sym, dummyOwner);
            JFXVar rv = createResultVar(clause, body, dummyOwner, tree.type);
            body.stats = body.stats.prepend(iv).prepend(idxv).append(rv);
            body.value = preTrans.defaultValue(body.type); // just fill the spot
            scan(clause);
            scan(body);
        } else {
            super.visitForExpression(tree);
        }
    }

    /**
     * Create the '$indexof$x' variable
     */
    private JFXVar createIndexVar(JFXForExpressionInClause clause, Symbol owner) {
        // Create the method parameter
        // $index$
        Name indexParamName = names.fromString(defs.dollarIndexNamePrefix());
        Name indexName = JavafxTranslationSupport.indexVarName(clause.getVar().getName(), names);
        JavafxVarSymbol indexParamSym = new JavafxVarSymbol(types, names,Flags.FINAL | Flags.PARAMETER, indexParamName, syms.intType, owner);

        // Create the index var
        // var $indexof$x = $index$
        JFXVar indexVar = preTrans.LocalVar(clause.pos(), syms.intType, indexName, fxmake.Ident(indexParamSym), owner);
        // Stash the created variable so it can be used when we visit a
        // JFXIndexof, where we convert that to a JFXIdent referencing the indexDecl.
        clause.indexVarSym = indexVar.sym;
        return indexVar;
    }

    /**
     * Create the induction var in the body
     *  var x
     */
    private JFXVar createInductionVar(JFXForExpressionInClause clause, JavafxVarSymbol boundIndexVarSym, Symbol owner) {
        JFXVar param = clause.getVar();
        JFXVar inductionVar =  preTrans.LocalVar(param.pos(), param.type, param.name, null, owner);
        clause.inductionVarSym = inductionVar.sym = param.sym;
        return inductionVar;
    }

    /**
     * Create the bound result:
     *  def result = bind block_value;
     */
    private JFXVar createResultVar(JFXForExpressionInClause clause, JFXBlock body, Symbol owner, Type seqType) {
        JFXExpression value = body.value;
        Type valtype = value.type;
        if (clause.getWhereExpression() != null) {
            // There is a where-clause, convert to an if-expression
            JFXExpression nada;
            if (types.isSequence(valtype)) {
                nada = fxmake.EmptySequence();
            } else {
                // For now, at least, if there is a where clause, we need to be
                // able to return null, so box the type
                nada = fxmake.Literal(TypeTags.BOT, null);
                valtype = types.boxedElementType(seqType);
                value = preTrans.makeCastIfNeeded(value, valtype);
                value.type = valtype;
            }
            nada.type = valtype;
            value = fxmake.Conditional(clause.getWhereExpression(), value, nada);
            value.type = valtype;
            clause.setWhereExpr(null);
        }
        body.type = valtype;
        JFXVar param = clause.getVar();
        Name resName = resultVarName(param.name);
        JFXVar resultVar =  preTrans.BoundLocalVar(clause.pos(), valtype, resName, value, owner);
        resultVar.sym.flags_field |= JavafxFlags.VARUSE_BIND_ACCESS;
        clause.boundResultVarSym = resultVar.sym;
        return resultVar;
    }

    public Name resultVarName(Name name) {
        return names.fromString(defs.resultDollarNamePrefix() + name.toString());
    }

    @Override
    public void visitIndexof(JFXIndexof tree) {
        // Convert
        super.visitIndexof(tree);
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
                            fxmake.Modifiers(fxVar.mods.flags & ~Flags.PARAMETER),
                            preTrans.defaultValue(fxVar.type),
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
                returnVar.type = tree.sym.type.getReturnType();
                returnVar.sym = new JavafxVarSymbol(types, names,0L, defs.boundFunctionResultName, returnVar.type, tree.sym);
                returnVar.sym.flags_field |= JavafxFlags.VARUSE_BIND_ACCESS;
                returnVar.markBound(JavafxBindStatus.UNIDIBIND);
                stmts.append(returnVar);

                // find the symbol of Pointer.make(Object) method.
                // The select expression Pointer.make
                JFXSelect select = fxmake.Select(fxmake.Type(syms.javafx_PointerType), defs.make_PointerMethodName);
                select.sym = preTrans.makeSyntheticPointerMake();
                select.type = select.sym.type;


                // args for Pointer.make(Object)
                JFXIdent ident = fxmake.Ident(returnVar);
                ident.type = returnVar.type;
                ident.sym = returnVar.sym;
                ListBuffer<JFXExpression> pointerMakeArgs = ListBuffer.lb();
                pointerMakeArgs.append(fxmake.VarRef(ident, JFXVarRef.RefKind.INST).setType(syms.javafx_FXObjectType));
                pointerMakeArgs.append(fxmake.VarRef(ident, JFXVarRef.RefKind.VARNUM).setType(syms.intType));

                // call Pointer.make($$bound$result$)
                JFXFunctionInvocation apply = fxmake.Apply(null, select, pointerMakeArgs.toList());
                apply.type = syms.javafx_PointerType;

                blk.stats = stmts.toList();
                blk.value = apply;
                blk.type = apply.type;
            }
        }
    }
}
