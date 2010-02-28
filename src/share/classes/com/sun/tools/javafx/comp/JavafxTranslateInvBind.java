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

import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;

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
    private JavafxVarSymbol targetSymbol;

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
        this.targetSymbol = (JavafxVarSymbol) symbol;
        this.boundExpression = expr;
        
        return translateToExpressionResult(expr, type);
    }

    private class BiBoundSequenceSelectTranslator extends BiBoundSelectTranslator {

        BiBoundSequenceSelectTranslator(JFXSelect tree) {
            super(tree);
        }

        @Override
        protected BoundSequenceResult doit() {
            addInterClassBindee((JavafxVarSymbol) selectorSym, refSym);
            return new BoundSequenceResult(
                    List.<JCStatement>nil(),
                    null,
                    bindees(),
                    invalidators(),
                    interClass(),
                    makeGetElementBody(),
                    makeSizeBody(),
                    targetSymbol.type);
        }

        private JCExpression selector() {
            return refSym.isStatic() ?
                Getter(selectorSym) :
                concreteSelector();
        }

        private JCExpression concreteSelector() {
            return translateToCheck(getToCheck());
        }

        // ---- Stolen from BoundSequenceTranslator ----
        //TODO: unify

        private Name activeFlagBit = defs.varFlagSEQUENCE_LIVE;
        JavafxVarSymbol flagSymbol = (JavafxVarSymbol)targetSymbol;

        JCExpression isSequenceActive() {
            return FlagTest(flagSymbol, activeFlagBit, activeFlagBit);
        }

        JCExpression isSequenceDormant() {
            return FlagTest(flagSymbol, activeFlagBit, null);
        }

        JCStatement setSequenceActive() {
            return FlagChangeStmt(flagSymbol, null, BITOR(id(defs.varFlagSEQUENCE_LIVE), id(defs.varFlagINIT_INITIALIZED_DEFAULT)));
        }

        protected JCExpression getReceiverForCallHack(Symbol sym) {
            if (sym.isStatic()) {
                return makeType(sym.owner.type, false);
            }
            return getReceiver(sym);
        }

        JCExpression CallSize(Symbol sym) {
            return CallSize(getReceiverForCallHack(sym), sym);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            if (((JavafxVarSymbol) sym).useAccessors())
                return Call(rcvr, attributeSizeName(sym));
            else
                return Call(defs.Sequences_size, Getter(rcvr, sym));
        }

        JCExpression CallGetElement(Symbol sym, JCExpression pos) {
            return CallGetElement(getReceiverForCallHack(sym), sym, pos);
        }

        JCExpression CallGetElement(JCExpression rcvr, Symbol sym, JCExpression pos) {
            if (((JavafxVarSymbol) sym).useAccessors())
                return Call(rcvr, attributeGetElementName(sym), pos);
            else
                return Call(Getter(rcvr, sym), defs.get_SequenceMethodName, pos);
        }

        /**
         * size$ method
         */
        JCStatement makeSizeBody() {
            JCVariableDecl vSize = TmpVar("size", syms.intType, CallSize(concreteSelector(), refSym));

            return
                Block(
                    vSize,
                    If (isSequenceDormant(),
                        Block(
                            SetStmt(targetSymbol,
                                m().NewClass(null, null,
                                    makeType(types.erasure(syms.javafx_SequenceProxyType)),
                                    List.<JCExpression>of(
                                        TypeInfo(diagPos, refSym.type),
                                        selector(),
                                        Offset(getReceiver(selectorSym), refSym)),
                                    null)
                            ),
                            setSequenceActive(),
                            CallStmt(defs.FXBase_addDependent,
                                        selector(),
                                        Offset(selector(), refSym),
                                        getReceiverOrThis(selectorSym),
                                        DepNum(getReceiver(selectorSym), selectorSym, refSym)
                            ),
                            CallSeqInvalidateUndefined(targetSymbol),
                            CallSeqTriggerInitial(targetSymbol, id(vSize))
                        )
                    ),
                    Return (id(vSize))
                );
        }

        /**
         * elem$ method
         */
        JCStatement makeGetElementBody() {
            return
                Block(
                    If (isSequenceDormant(),
                        Stmt(CallSize(targetSymbol))
                    ),
                    Return (CallGetElement(concreteSelector(), refSym, posArg()))
                );
        }
    }

    private class BiBoundSequenceIdentTranslator extends BoundIdentTranslator {

        private final JavafxVarSymbol refSym;

        BiBoundSequenceIdentTranslator(JFXIdent tree) {
            super(tree);
            this.refSym = (JavafxVarSymbol) tree.sym;
        }

        @Override
        protected BoundSequenceResult doit() {
            super.doit();
            return new BoundSequenceResult(
                    List.<JCStatement>nil(),
                    null,
                    bindees(),
                    invalidators(),
                    interClass(),
                    makeGetElementBody(),
                    makeSizeBody(),
                    targetSymbol.type);
        }

        // ---- Stolen from BoundSequenceTranslator ----
        //TODO: unify

        private Name activeFlagBit = defs.varFlagSEQUENCE_LIVE;
        JavafxVarSymbol flagSymbol = (JavafxVarSymbol)targetSymbol;

        JCExpression isSequenceActive() {
            return FlagTest(flagSymbol, activeFlagBit, activeFlagBit);
        }

        JCExpression isSequenceDormant() {
            return FlagTest(flagSymbol, activeFlagBit, null);
        }

        JCStatement setSequenceActive() {
            return FlagChangeStmt(flagSymbol, null, BITOR(id(defs.varFlagSEQUENCE_LIVE), id(defs.varFlagINIT_INITIALIZED_DEFAULT)));
        }

        protected JCExpression getReceiverForCallHack(Symbol sym) {
            if (sym.isStatic()) {
                return makeType(sym.owner.type, false);
            }
            return getReceiver(sym);
        }

        JCExpression CallSize(Symbol sym) {
            return CallSize(getReceiverForCallHack(sym), sym);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            if (((JavafxVarSymbol) sym).useAccessors())
                return Call(rcvr, attributeSizeName(sym));
            else
                return Call(defs.Sequences_size, Getter(rcvr, sym));
        }

        JCExpression CallGetElement(Symbol sym, JCExpression pos) {
            return CallGetElement(getReceiverForCallHack(sym), sym, pos);
        }

        JCExpression CallGetElement(JCExpression rcvr, Symbol sym, JCExpression pos) {
            if (((JavafxVarSymbol) sym).useAccessors())
                return Call(rcvr, attributeGetElementName(sym), pos);
            else
                return Call(Getter(rcvr, sym), defs.get_SequenceMethodName, pos);
        }

        /**
         * size$ method
         */
        JCStatement makeSizeBody() {
            JCVariableDecl vSize = TmpVar("size", syms.intType, CallSize(refSym));

            return
                Block(
                    vSize,
                    If (isSequenceDormant(),
                        Block(
                            SetStmt(targetSymbol,
                                m().NewClass(null, null,
                                    makeType(types.erasure(syms.javafx_SequenceProxyType)),
                                    List.<JCExpression>of(
                                        TypeInfo(diagPos, refSym.type),
                                        getReceiverOrThis(refSym),
                                        Offset(refSym)),
                                    null)
                            ),
                            setSequenceActive(),
                            CallSeqInvalidateUndefined(targetSymbol),
                            CallSeqTriggerInitial(targetSymbol, id(vSize))
                        )
                    ),
                    Return (id(vSize))
                );
        }

        /**
         * elem$ method
         */
        JCStatement makeGetElementBody() {
            return
                Block(
                    If (isSequenceDormant(),
                        Stmt(CallSize(targetSymbol))
                    ),
                    Return (CallGetElement(refSym, posArg()))
                );
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
