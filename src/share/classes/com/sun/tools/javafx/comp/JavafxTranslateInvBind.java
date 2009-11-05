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
import com.sun.tools.mjavac.tree.JCTree.*;
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
                JCVariableDecl value = TmpVar(targettedType, invertedExpression.expr());
                addPreface(value);
                
                if (selectSymbol != null) {
                    JCVariableDecl selector = TmpVar(selectSymbol.type, Call(attributeGetterName(selectSymbol)));
                    addPreface(selector);
                    //note: we have to use the set$(int, FXBase) version because
                    //the set$xxx version is not always accessible from the
                    //selector expression (if selector is XXX$Script class)
                    JCStatement setter = CallStmt(id(selector),
                            defs.set_FXObjectMethodName,
                            Offset(id(selector), selectVarSymbol),
                            id(value)); //FIXME: is this mixin safe?
                    JCExpression conditionExpr = NE(id(selector), Null());
                    addPreface(If(conditionExpr, Block(setter)));
                } else {
                    addPreface(CallStmt(attributeSetterName(selectVarSymbol), id(value)));
                }
                
                return toResult(id(defs.varNewValue_ArgName), targettedType);
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


/* ***************************************************************************
 * Visitor methods -- implemented (alphabetical order)
 ****************************************************************************/

    
    public void visitIdent(final JFXIdent tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected ExpressionResult doit() {
                selectVarSymbol = (VarSymbol)tree.sym;
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
                selectSymbol = (VarSymbol)selector.sym;
                selectVarSymbol = (VarSymbol)tree.sym;
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

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        disallowedInInverseBind();
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        disallowedInInverseBind();
    }

    public void visitParens(JFXParens tree) {
        disallowedInInverseBind();
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        disallowedInInverseBind();
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        disallowedInInverseBind();
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        disallowedInInverseBind();
    }

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
