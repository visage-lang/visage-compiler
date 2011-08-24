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
import org.visage.tools.code.VisageFlags;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.tree.*;
import org.visage.tools.tree.VisageExpression;
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
public class VisageBoundFiller extends VisageTreeScanner {

    private final VisagePreTranslationSupport preTrans;
    private final VisageTreeMaker fxmake;
    private final VisageDefs defs;
    private final VisageSymtab syms;
    protected final VisageTypes types;
    private final Name.Table names;

    protected static final Context.Key<VisageBoundFiller> boundFuncFill =
            new Context.Key<VisageBoundFiller>();

    public static VisageBoundFiller instance(Context context) {
        VisageBoundFiller instance = context.get(boundFuncFill);
        if (instance == null) {
            instance = new VisageBoundFiller(context);
        }
        return instance;
    }

    private VisageBoundFiller(Context context) {
        context.put(boundFuncFill, this);

        preTrans = VisagePreTranslationSupport.instance(context);
        fxmake = VisageTreeMaker.instance(context);
        defs = VisageDefs.instance(context);
        syms = (VisageSymtab)VisageSymtab.instance(context);
        types = VisageTypes.instance(context);
        names = Name.Table.instance(context);
    }

    public void fill(VisageEnv<VisageAttrContext> attrEnv) {
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
    public void visitForExpression(VisageForExpression tree) {
        if (tree.isBound()) {
            VisageBlock body = (VisageBlock) tree.getBodyExpression();
            assert tree.getInClauses().size() == 1 : "lower is supposed to flatten to exactly one in-clause";
            VisageForExpressionInClause clause = tree.getForExpressionInClauses().get(0);
            MethodSymbol dummyOwner = preTrans.makeDummyMethodSymbol(clause.var.sym.owner);
            VisageVar idxv = createIndexVar(clause, dummyOwner);
            VisageVar iv = createInductionVar(clause, idxv.sym, dummyOwner);
            VisageVar rv = createResultVar(clause, body, dummyOwner, tree.type);
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
    private VisageVar createIndexVar(VisageForExpressionInClause clause, Symbol owner) {
        // Create the method parameter
        // $index$
        Name indexParamName = names.fromString(defs.dollarIndexNamePrefix());
        Name indexName = VisageTranslationSupport.indexVarName(clause.getVar().getName(), names);
        VisageVarSymbol indexParamSym = new VisageVarSymbol(types, names,Flags.FINAL | Flags.PARAMETER, indexParamName, syms.intType, owner);

        // Create the index var
        // var $indexof$x = $index$
        VisageVar indexVar = preTrans.LocalVar(clause.pos(), syms.intType, indexName, fxmake.Ident(indexParamSym), owner);
        // Stash the created variable so it can be used when we visit a
        // VisageIndexof, where we convert that to a VisageIdent referencing the indexDecl.
        clause.indexVarSym = indexVar.sym;
        return indexVar;
    }

    /**
     * Create the induction var in the body
     *  var x
     */
    private VisageVar createInductionVar(VisageForExpressionInClause clause, VisageVarSymbol boundIndexVarSym, Symbol owner) {
        VisageVar param = clause.getVar();
        VisageVar inductionVar =  preTrans.LocalVar(param.pos(), param.type, param.name, null, owner);
        clause.inductionVarSym = inductionVar.sym = param.sym;
        return inductionVar;
    }

    /**
     * Create the bound result:
     *  def result = bind block_value;
     */
    private VisageVar createResultVar(VisageForExpressionInClause clause, VisageBlock body, Symbol owner, Type seqType) {
        VisageExpression value = body.value;
        Type valtype = value.type;
        if (clause.getWhereExpression() != null) {
            // There is a where-clause, convert to an if-expression
            VisageExpression nada;
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
        VisageVar param = clause.getVar();
        Name resName = resultVarName(param.name);
        VisageVar resultVar =  preTrans.BoundLocalVar(clause.pos(), valtype, resName, value, owner);
        resultVar.sym.flags_field |= VisageFlags.VARUSE_BIND_ACCESS;
        clause.boundResultVarSym = resultVar.sym;
        return resultVar;
    }

    public Name resultVarName(Name name) {
        return names.fromString(defs.resultDollarNamePrefix() + name.toString());
    }

    @Override
    public void visitIndexof(VisageIndexof tree) {
        // Convert
        super.visitIndexof(tree);
    }

    @Override
    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        if (tree.isBound()) {
            // Fill out the bound function support vars before
            // local inflation
            boundFunctionFiller(tree);
        }
        super.visitFunctionDefinition(tree);
    }

    private void boundFunctionFiller(VisageFunctionDefinition tree) {
        /*
         * For bound functions, make a synthetic bound variable with
         * initialization expression to be the return expression and return
         * the Pointer of the synthetic variable as the result.
         */
        VisageBlock blk = tree.getBodyExpression();
        if (blk != null) {
            VisageExpression returnExpr = (blk.value instanceof VisageReturn) ? ((VisageReturn) blk.value).getExpression() : blk.value;
            if (returnExpr != null) {
                fxmake.at(blk.value.pos);
                ListBuffer<VisageExpression> stmts = ListBuffer.lb();
                /*
                 * Generate a local variable for each parameter. We will later
                 * transform each param as VisageObject+varNum pair during translation.
                 * These locals will be converted into instance variables of the
                 * local context class.
                 */
                for (VisageVar fxVar : tree.getParams()) {
                    VisageVar localVar = fxmake.Var(
                            fxVar.name,
                            fxVar.getVisageType(),
                            fxmake.Modifiers(fxVar.mods.flags & ~Flags.PARAMETER),
                            preTrans.defaultValue(fxVar.type),
                            VisageBindStatus.UNIDIBIND, null, null);
                    localVar.type = fxVar.type;
                    localVar.sym = fxVar.sym;
                    stmts.append(localVar);
                }

                stmts.appendList(blk.stats);

                // is return expression a variable declaration?
                boolean returnExprIsVar = (returnExpr.getFXTag() == VisageTag.VAR_DEF);
                if (returnExprIsVar) {
                    stmts.append(returnExpr);
                }
                VisageVar returnVar = fxmake.Var(
                        defs.boundFunctionResultName,
                        fxmake.TypeUnknown(),
                        fxmake.Modifiers(0),
                        returnExprIsVar ? fxmake.Ident((VisageVar) returnExpr) : returnExpr,
                        VisageBindStatus.UNIDIBIND, null, null);
                returnVar.type = tree.sym.type.getReturnType();
                returnVar.sym = new VisageVarSymbol(types, names,0L, defs.boundFunctionResultName, returnVar.type, tree.sym);
                returnVar.sym.flags_field |= VisageFlags.VARUSE_BIND_ACCESS;
                returnVar.markBound(VisageBindStatus.UNIDIBIND);
                stmts.append(returnVar);

                // find the symbol of Pointer.make(Object) method.
                // The select expression Pointer.make
                VisageSelect select = fxmake.Select(fxmake.Type(syms.visage_PointerType), defs.make_PointerMethodName, false);
                select.sym = preTrans.makeSyntheticPointerMake();
                select.type = select.sym.type;


                // args for Pointer.make(Object)
                VisageIdent ident = fxmake.Ident(returnVar);
                ident.type = returnVar.type;
                ident.sym = returnVar.sym;
                ListBuffer<VisageExpression> pointerMakeArgs = ListBuffer.lb();
                pointerMakeArgs.append(fxmake.VarRef(ident, VisageVarRef.RefKind.INST).setType(syms.visage_FXObjectType));
                pointerMakeArgs.append(fxmake.VarRef(ident, VisageVarRef.RefKind.VARNUM).setType(syms.intType));

                // call Pointer.make($$bound$result$)
                VisageFunctionInvocation apply = fxmake.Apply(null, select, pointerMakeArgs.toList());
                apply.type = syms.visage_PointerType;

                blk.stats = stmts.toList();
                blk.value = apply;
                blk.type = apply.type;
            }
        }
    }
}
