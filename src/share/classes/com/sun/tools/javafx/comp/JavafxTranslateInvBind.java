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

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;

/**
 * Translate an inversion of bind expressions.
 * 
 * @author Jim Laskey
 */
public class JavafxTranslateInvBind extends JavafxAbstractTranslation implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateInvBind> jfxBoundInvTranslation =
        new Context.Key<JavafxTranslateInvBind>();

    Type targettedType;
    VarSymbol targetSymbol;
    Symbol selectorSymbol;
    VarSymbol selectedVarSymbol;
    

    public static JavafxTranslateInvBind instance(Context context) {
        JavafxTranslateInvBind instance = context.get(jfxBoundInvTranslation);
        if (instance == null) {
            JavafxToJava toJava = JavafxToJava.instance(context);
            instance = new JavafxTranslateInvBind(context, toJava);
        }
        return instance;
    }

    public JavafxTranslateInvBind(Context context, JavafxToJava toJava) {
        super(context, toJava);

        context.put(jfxBoundInvTranslation, this);
    }

    ExpressionResult translate(JFXExpression expr, Type type, Symbol symbol) {
        targettedType = type;
        targetSymbol = (VarSymbol) symbol;
        selectorSymbol = null;
        selectedVarSymbol = null;
        
        final ExpressionResult invertedExpression = translateToExpressionResult(expr, targettedType);

        if (types.isSequence(type)) {
            return new BidirectionalBoundSequenceTranslator(expr.pos()).doit();
        } else {
            return new BidirectionalBoundTranslator(expr.pos(), invertedExpression).doit();
        }
    }

    private class BidirectionalBoundSequenceTranslator extends ExpressionTranslator {

        BidirectionalBoundSequenceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        BoundSequenceResult doit() {
            return new BoundSequenceResult(bindees(), invalidators(), interClass(), makeGetElementBody(), makeSizeBody());
        }

        // ---- Stolen from BoundSequenceTranslator ----
        //TODO: unify

        JCExpression isSequenceDormantSetActive() {
            return NOT(FlagChange(targetSymbol, null, defs.varFlagDEFAULT_APPLIED));
        }

        JCExpression isSequenceActive() {
            return FlagTest(targetSymbol, defs.varFlagDEFAULT_APPLIED, defs.varFlagDEFAULT_APPLIED);
        }

        JCExpression CallSize(Symbol sym) {
            return CallSize(getReceiver(), sym);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            return Call(rcvr, attributeSizeName(sym));
        }


        /**
         * size$ method
         */
        JCStatement makeSizeBody() {
            return
                Block(
                    If(isSequenceDormantSetActive(),
                        Block(
                         )
                    ),
                    Return( Call(Get(targetSymbol), defs.size_SequenceMethodName) )
                );
        }

        /**
         * elem$ method
         */
        JCStatement makeGetElementBody() {
            return
                Block(
                    If(NOT(isSequenceActive()),
                        Stmt(CallSize(targetSymbol))
                    ),
                    Return (Call(Get(targetSymbol), defs.get_SequenceMethodName, posArg()))
                );
        }

        /**
         * Redirecting invalidate
         */
        JCStatement makeInvalidate() {
            return
                Block(
//                    CallSeqInvalidate(targetSymbol, Int(0), id(oldSizeVar), id(newSizeVar))
                );
        }
    }

    private class BidirectionalBoundTranslator extends ExpressionTranslator {

        private final ExpressionResult invertedExpression;

        BidirectionalBoundTranslator(DiagnosticPosition diagPos, ExpressionResult invertedExpression) {
            super(diagPos);
            this.invertedExpression = invertedExpression;
        }

        protected ExpressionResult doit() {
                /*
                  type tmp0 = inv expression(varNewValue$);
                  set$varSym(tmp0);
                  varNewValue$

                  or

                  type tmp0 = inv expression(varNewValue$);
                  seltype tmp1 = get$select();
                  if (tmp1 != null) tmp1.set$varSym(tmp0);
                  varNewValue$
                */
                JCVariableDecl value = TmpVar(targettedType, invertedExpression.expr());
                addPreface(value);

                if (selectorSymbol != null) {
                    JCExpression receiver = null;
                    if (!selectedVarSymbol.isStatic() &&
                            selectorSymbol.kind == Kinds.TYP &&
                            currentClass().sym.isSubClass(selectorSymbol, types)) {
                        receiver = id(names._super);
                    } else if (!(selectorSymbol instanceof VarSymbol)) {  
                        receiver = id(selectorSymbol);
                    } else {
                        JCVariableDecl selector =
                            TmpVar(syms.javafx_FXObjectType,
                                Getter(selectorSymbol));
                        addPreface(selector);
                        receiver = id(selector);
                    }


                    //note: we have to use the set$(int, FXBase) version because
                    //the set$xxx version is not always accessible from the
                    //selector expression (if selector is XXX$Script class)
                    addPreface(
                        If(NEnull(receiver),
                            Block(
                                CallStmt(receiver, defs.set_FXObjectMethodName,
                                    Offset(receiver, selectedVarSymbol),
                                    id(value)   //FIXME: is this mixin safe?
                                )
                            )
                        )
                    );
                } else {
                    addPreface(SetterStmt(selectedVarSymbol, id(value)));
                }

                return toResult(id(defs.varNewValue_ArgName), targettedType);
            }
    }


    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "ibfx$";
    }


/* ***************************************************************************
 * Visitor methods -- implemented (alphabetical order)
 ****************************************************************************/

    
    public void visitIdent(final JFXIdent tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected ExpressionResult doit() {
                selectedVarSymbol = (VarSymbol)tree.sym;
                return toResult(id(defs.varNewValue_ArgName), targettedType);
            }
        }).doit();
    }

    public void visitSelect(final JFXSelect tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected ExpressionResult doit() {
                JFXExpression selectorExpr = tree.getExpression();
                assert selectorExpr instanceof JFXIdent : "should be another var in the same instance.";
                JFXIdent selector = (JFXIdent)selectorExpr;
                selectorSymbol = selector.sym;
                selectedVarSymbol = (VarSymbol)tree.sym;
                return toResult(id(defs.varNewValue_ArgName), targettedType);
            }
        }).doit();
    }


    /***********************************************************************
     *
     * Moot visitors  (alphabetical order)
     *
     */

    private void disallowedInInverseBind() {
        badVisitor("should not be processed as part of a binding with inverse");
    }

    @Override
    public void visitBinary(JFXBinary tree) {
        disallowedInInverseBind();
    }

    public void visitBlockExpression(JFXBlock tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitForExpression(JFXForExpression tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitIndexof(JFXIndexof tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitInterpolateValue(JFXInterpolateValue tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitParens(JFXParens tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitTypeCast(JFXTypeCast tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        disallowedInInverseBind();
    }

}
