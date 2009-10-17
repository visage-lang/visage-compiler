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
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Context;

/**
 * Translate an inversion of bind expressions.
 * 
 * @author Jim Laskey
 */
public class JavafxTranslateInvBind extends JavafxAbstractTranslation implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateInvBind> jfxBoundInvTranslation =
        new Context.Key<JavafxTranslateInvBind>();

    Type targettedType;
    Symbol targetSymbol;
    VarSymbol selectSymbol;
    VarSymbol selectVarSymbol;
    

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
        targetSymbol = symbol;
        selectSymbol = null;
        selectVarSymbol = null;
        
        final ExpressionResult invertedExpression = translateToExpressionResult(expr, targettedType);

        return (new ExpressionTranslator(expr.pos()) {
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
                JCExpression expr;

                JCVariableDecl value = makeTmpVar(targettedType, invertedExpression.expr());
                addPreface(value);
                
                if (selectSymbol != null) {
                    JCVariableDecl selector = makeTmpVar(selectSymbol.type, call(attributeGetterName(selectSymbol)));
                    addPreface(selector);
                    JCStatement setter = callStmt(id(selector), attributeSetterName(selectVarSymbol), id(value));
                    JCExpression conditionExpr = makeBinary(JCTree.NE, id(selector), makeNull());
                    addPreface(m().If(conditionExpr, m().Block(0L, List.<JCStatement>of(setter)), null));
                } else {
                    addPreface(callStmt(attributeSetterName(selectVarSymbol), id(value)));
                }
                
                return toResult(id(defs.attributeNewValueName), targettedType);
            }
        }).doit();
    }

/* ***************************************************************************
 * Visitor methods -- implemented (alphabetical order)
 ****************************************************************************/

    
    public void visitIdent(final JFXIdent tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected ExpressionResult doit() {
                selectVarSymbol = (VarSymbol)tree.sym;
                return toResult(id(defs.attributeNewValueName), targettedType);
            }
        }).doit();
    }

    public void visitSelect(final JFXSelect tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected ExpressionResult doit() {
                JFXExpression selectorExpr = tree.getExpression();
                assert selectorExpr instanceof JFXIdent : "should be another var in the same instance.";
                JFXIdent selector = (JFXIdent)selectorExpr;
                selectSymbol = (VarSymbol)selector.sym;
                selectVarSymbol = (VarSymbol)tree.sym;
                return toResult(id(defs.attributeNewValueName), targettedType);
            }
        }).doit();
    }


    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "ibfx$";
    }


    /***********************************************************************
     *
     * Moot visitors  (alphabetical order)
     *
     */

    public void visitAssign(JFXAssign tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitAssignop(JFXAssignOp tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitBinary(JFXBinary tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitBlockExpression(JFXBlock tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitBreak(JFXBreak tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitCatch(JFXCatch tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitContinue(JFXContinue tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitErroneous(JFXErroneous tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitForExpression(JFXForExpression tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitIfExpression(JFXIfExpression tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitImport(JFXImport tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitIndexof(JFXIndexof tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitInstanciate(JFXInstanciate tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitInvalidate(JFXInvalidate tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitLiteral(JFXLiteral tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitModifiers(JFXModifiers tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitParens(JFXParens tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitReturn(JFXReturn tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitScript(JFXScript tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitSkip(JFXSkip tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitStringExpression(JFXStringExpression tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitThrow(JFXThrow tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTry(JFXTry tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeAny(JFXTypeAny tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeArray(JFXTypeArray tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeCast(JFXTypeCast tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeClass(JFXTypeClass tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitUnary(JFXUnary tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitVar(JFXVar tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitVarInit(JFXVarInit tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        assert false : "should not be processed as part of a binding with inverse";
    }

}
