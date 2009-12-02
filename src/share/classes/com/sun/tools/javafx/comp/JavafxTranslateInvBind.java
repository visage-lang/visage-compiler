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
import com.sun.tools.mjavac.util.List;

/**
 * Translate an inversion of bind expressions.
 * 
 * @author Jim Laskey
 * @author Robert Field
 */
public class JavafxTranslateInvBind extends JavafxAbstractTranslation implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateInvBind> jfxBoundInvTranslation =
        new Context.Key<JavafxTranslateInvBind>();


    // Symbol for the var whose bound expression we are translating.
    private VarSymbol targetSymbol;

    // The outermost bound expression
    private JFXExpression boundExpression;

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
        this.targetSymbol = (VarSymbol) symbol;
        this.boundExpression = expr;
        
        return translateToExpressionResult(expr, type);
    }

    private class BiBoundSequenceSelectTranslator extends BiBoundSelectTranslator {

        BiBoundSequenceSelectTranslator(JFXSelect tree) {
            super(tree);
        }

        @Override
        protected BoundSequenceResult doit() {
            buildDependencies(selectorSym);
            addInterClassBindee((VarSymbol) selectorSym, refSym);
            return new BoundSequenceResult(
                    List.of(init()),
                    null,
                    bindees(),
                    invalidators(),
                    interClass(),
                    makeGetElementBody(),
                    makeSizeBody(),
                    targetSymbol.type);
        }

        JCStatement init() {
            JCExpression rcvr = refSym.isStatic() ? getReceiver(refSym) : translateToCheck(getToCheck());
            List<JCExpression> args = List.<JCExpression>of(TypeInfo(diagPos, refSym.type), rcvr, Offset(getReceiver(selectorSym), refSym));
            return
                SetStmt(targetSymbol,
                    m().NewClass(null, null, makeType(types.erasure(syms.javafx_SequenceProxyType)), args, null)
                );
        }

        // ---- Stolen from BoundSequenceTranslator ----
        //TODO: unify

        JCExpression CallGetElement(JCExpression rcvr, Symbol sym, JCExpression pos) {
            return Call(rcvr, attributeGetElementName(sym), pos);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            return Call(rcvr, attributeSizeName(sym));
        }

        /**
         * size$ method
         */
        JCStatement makeSizeBody() {
            return
                Return (CallSize(translateToCheck(getToCheck()), refSym) );
        }

        /**
         * elem$ method
         */
        JCStatement makeGetElementBody() {
            return
                Return (CallGetElement(translateToCheck(getToCheck()), refSym, posArg()));
        }
    }

    private class BiBoundSequenceIdentTranslator extends ExpressionTranslator {

        private final VarSymbol refSym;

        BiBoundSequenceIdentTranslator(JFXIdent tree) {
            super(tree.pos());
            this.refSym = (VarSymbol) tree.sym;
        }

        @Override
        BoundSequenceResult doit() {
            addBindee(refSym);
            return new BoundSequenceResult(
                    List.of(init()),
                    null,
                    bindees(),
                    invalidators(),
                    interClass(),
                    makeGetElementBody(),
                    makeSizeBody(),
                    targetSymbol.type);
        }

        JCStatement init() {
            List<JCExpression> args = List.<JCExpression>of(TypeInfo(diagPos, refSym.type), getReceiverOrThis(), Offset(refSym));
            return
                SetStmt(targetSymbol, 
                    m().NewClass(null, null, makeType(types.erasure(syms.javafx_SequenceProxyType)), args, null)
                );
        }

        // ---- Stolen from BoundSequenceTranslator ----
        //TODO: unify

        JCExpression CallSize(Symbol sym) {
            return CallSize(getReceiver(), sym);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            return Call(rcvr, attributeSizeName(sym));
        }

        JCExpression CallGetElement(Symbol sym, JCExpression pos) {
            return CallGetElement(getReceiver(), sym, pos);
        }

        JCExpression CallGetElement(JCExpression rcvr, Symbol sym, JCExpression pos) {
            return Call(rcvr, attributeGetElementName(sym), pos);
        }

        /**
         * size$ method
         */
        JCStatement makeSizeBody() {
            return
                Return (CallSize(refSym) );
        }

        /**
         * elem$ method
         */
        JCStatement makeGetElementBody() {
            return
                Return (CallGetElement(refSym, posArg()));
        }
    }

    private class BiBoundSelectTranslator extends BoundSelectTranslator {

        final Symbol selectorSym;

        BiBoundSelectTranslator(JFXSelect tree) {
            super(tree, targetSymbol);
            JFXExpression selectorExpr = tree.getExpression();
            assert selectorExpr instanceof JFXIdent : "should be another var in the same instance.";
            JFXIdent selector = (JFXIdent) selectorExpr;
            selectorSym = selector.sym;
        }

        @Override
        protected ExpressionResult doit() {
            /*
            type tmp0 = inv expression(varNewValue$);
            seltype tmp1 = get$select();
            if (tmp1 != null) tmp1.set$varSym(tmp0);
            varNewValue$
             */
            JCExpression receiver;
            if (!refSym.isStatic() &&
                    selectorSym.kind == Kinds.TYP &&
                    currentClass().sym.isSubClass(selectorSym, types)) {
                receiver = id(names._super);
            } else if (!(selectorSym instanceof VarSymbol)) {
                receiver = id(selectorSym);
            } else {
                JCVariableDecl selector =
                        TmpVar(syms.javafx_FXObjectType,
                        Getter(selectorSym));
                addSetterPreface(selector);
                receiver = id(selector);
            }

            //note: we have to use the set$(int, FXBase) version because
            //the set$xxx version is not always accessible from the
            //selector expression (if selector is XXX$Script class)
            addSetterPreface(
                    If(NEnull(receiver),
                        Block(
                            CallStmt(receiver, defs.set_FXObjectMethodName,
                                Offset(receiver, refSym),
                                id(defs.varNewValue_ArgName)
                            )
                        )
                    )
            );

            return super.doit();
        }

        @Override
        protected void addSwitchDependence(JCExpression oldSelector, JCExpression newSelector, JCExpression oldOffset, JCExpression newOffset) {
            JCExpression rcvr = getReceiverOrThis(selectResSym);
            JCVariableDecl selectorOffset = TmpVar(syms.intType, Offset(selectResSym));
            addPreface(selectorOffset);

            addPreface(CallStmt(defs.FXBase_switchBiDiDependence,
                    rcvr,
                    id(selectorOffset),
                    oldSelector, oldOffset,
                    newSelector, newOffset));
        }
    }

    private class BiBoundIdentTranslator extends BoundIdentTranslator {

        BiBoundIdentTranslator(JFXIdent tree) {
            super(tree);
        }

        @Override
        protected ExpressionResult doit() {
            /*
            type tmp0 = inv expression(varNewValue$);
            set$varSym(tmp0);
            varNewValue$
             */
            addSetterPreface(SetterStmt(sym, id(defs.varNewValue_ArgName)));

            return super.doit();
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

    private boolean isTargettedToSequence() {
        return types.isSequence(targetSymbol.type);
    }
    
    public void visitIdent(JFXIdent tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            result = new BiBoundSequenceIdentTranslator(tree).doit();
        } else {
            result = new BiBoundIdentTranslator(tree).doit();
        }
    }

    public void visitSelect(JFXSelect tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            result = new BiBoundSequenceSelectTranslator(tree).doit();
        } else {
            result = new BiBoundSelectTranslator(tree).doit();
        }
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
