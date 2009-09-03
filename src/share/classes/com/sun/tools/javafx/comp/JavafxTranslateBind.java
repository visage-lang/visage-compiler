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
import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Context;

/**
 * Translate bind expressions into code in bind defining methods
 * 
 * @author Robert Field
 */
public class JavafxTranslateBind extends JavafxAbstractTranslation<JavafxTranslateBind.Result> implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateBind> jfxBoundTranslation =
        new Context.Key<JavafxTranslateBind>();

    public class Result {
        final List<JCStatement> stmts;
        final JCExpression value;
        Result(List<JCStatement> stmts, JCExpression value) {
            this.stmts = stmts;
            this.value = value;
        }
        Result(ListBuffer<JCStatement> buf, JCExpression value) {
            this(buf.toList(), value);
        }
        Result(JCExpression value) {
            this(List.<JCStatement>nil(), value);
        }
    }

    public static JavafxTranslateBind instance(Context context) {
        JavafxTranslateBind instance = context.get(jfxBoundTranslation);
        if (instance == null) {
            JavafxToJava toJava = JavafxToJava.instance(context);
            instance = new JavafxTranslateBind(context, toJava);
        }
        return instance;
    }

    public JavafxTranslateBind(Context context, JavafxToJava toJava) {
        super(context, toJava);

        context.put(jfxBoundTranslation, this);
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public void visitIfExpression(JFXIfExpression tree) {
        translate(tree.cond);
        translate(tree.truepart);
        translate(tree.falsepart);
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        translate(tree.meth);
        translate(tree.args);
    }

    public void visitParens(JFXParens tree) {
        translate(tree.expr);
    }

    public void visitAssign(JFXAssign tree) {
        translate(tree.lhs);
        translate(tree.rhs);
    }

    public void visitAssignop(JFXAssignOp tree) {
        translate(tree.lhs);
        translate(tree.rhs);
    }

    public void visitUnary(JFXUnary tree) {
        translate(tree.arg);
    }

    public void visitBinary(JFXBinary tree) {
        translate(tree.lhs);
        translate(tree.rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        //(tree.clazz);
        translate(tree.expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        translate(tree.expr);
        //(tree.clazz);
    }

    public void visitSelect(JFXSelect tree) {
        translate(tree.selected);
    }

    public void visitIdent(JFXIdent tree) {
        // Just translate to get
        result = new Result(translateIdent(tree));
    }

    public void visitLiteral(JFXLiteral tree) {
        // Just translate to literal value
        result = new Result(translateLiteral(tree));
    }
    
    //@Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        for (JFXVar param : tree.getParams()) {
            translate(param);
        }
        translate(tree.getBodyExpression());
    }

    //@Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    //@Override
    public void visitSequenceRange(JFXSequenceRange that) {
        translate( that.getLower() );
        translate( that.getUpper() );
        translate( that.getStepOrNull() );
    }
    
    //@Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        translate( that.getItems() );
    }

    //@Override
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        translate(that.getSequence());
        translate(that.getIndex());
    }
    
    public void visitSequenceSlice(JFXSequenceSlice that) {
        translate(that.getSequence());
        translate(that.getFirstIndex());
        translate(that.getLastIndex());
    }


    //@Override
    public void visitStringExpression(JFXStringExpression that) {
        List<JFXExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            translate(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    //@Override
    public void visitInstanciate(JFXInstanciate tree) {
       translate(tree.getIdentifier());
       translate(tree.getArgs());
       translate(tree.getParts());
       translate(tree.getLocalvars());
       translate(tree.getClassBody());
    }
    
    
    //@Override
    public void visitForExpression(JFXForExpression that) {
        for (ForExpressionInClauseTree cl : that.getInClauses()) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
            //(clause);
        }
        translate(that.getBodyExpression());
    }

    //@Override
    public void visitBlockExpression(JFXBlock that) {
        translate(that.stats);
        translate(that.value);
    }
    
    //@Override
    public void visitIndexof(JFXIndexof that) {
    }

    public void visitTree(JFXTree that) {
        assert false : "Should not be here!!!";
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
    }

    public void visitInterpolateValue(JFXInterpolateValue that) {
        translate(that.attribute);
        translate(that.value);
        if  (that.interpolation != null) {
            translate(that.interpolation);
        }
    }


    /***********************************************************************
     *
     * Utilities
     *s
     */

    protected String getSyntheticPrefix() {
        return "bfx$";
    }


    /***********************************************************************
     *
     * Moot visitors
     *
     */

    //@Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }

    //@Override
    public void visitModifiers(JFXModifiers tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitSkip(JFXSkip tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitThrow(JFXThrow tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTry(JFXTry tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitWhileLoop(JFXWhileLoop tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding";
    }


    //@Override
    public void visitScript(JFXScript tree) {
        assert false : "should not be processed as part of a binding";
   }

    //@Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

   //@Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitContinue(JFXContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitReturn(JFXReturn tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitImport(JFXImport tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitBreak(JFXBreak tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitCatch(JFXCatch tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTypeAny(JFXTypeAny that) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTypeClass(JFXTypeClass that) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTypeArray(JFXTypeArray tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitVarScriptInit(JFXVarScriptInit tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitVar(JFXVar tree) {
        // this is handled in translarVar
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        assert false : "should not be processed as part of a binding";
    }

    //@Override
    public void visitErroneous(JFXErroneous tree) {
        assert false : "erroneous nodes shouldn't have gotten this far";
    }

}
