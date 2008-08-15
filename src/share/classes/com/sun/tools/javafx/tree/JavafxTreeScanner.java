/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.javafx.api.tree.Tree;
import com.sun.tools.javac.util.List;

/**
 * An abstract tree walker (visitor) for ASTs ({@code JFXTree}s).
 * Each {@code visitXxx} method calls {@code scan} to visit its child
 * trees.  The {@code scan} method calls the {@code JFXTree}-subclass-specific
 * {@code accept} method.  A sub-class can override a specific {@code visitXxx}
 * method, or the {@code scan method}.
 * 
 * @author Robert Field
 * @author Per Bothner
 */
public class JavafxTreeScanner implements JavafxVisitor {

    public JavafxTreeScanner() {
    }

    /** Visitor method: Scan a single node.
   */
    public void scan(JFXTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
     public void scan(List<? extends JFXTree> trees) {
	if (trees != null)
	for (List<? extends JFXTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(java.util.List<? extends Tree> trees) {
	if (trees != null)
            for (Tree t : trees)
                scan((JFXTree)t);
    }


/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/
    
    public void visitScript(JFXScript tree) {
        scan(tree.pid);
        scan(tree.defs);
    }

    public void visitImport(JFXImport tree) {
        scan(tree.qualid);
    }

    public void visitSkip(JFXSkip tree) {
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        scan(tree.cond);
        scan(tree.body);
    }

    public void visitTry(JFXTry tree) {
        scan(tree.body);
        scan(tree.catchers);
        scan(tree.finalizer);
    }

    public void visitCatch(JFXCatch tree) {
        scan(tree.param);
        scan(tree.body);
    }

    public void visitIfExpression(JFXIfExpression tree) {
        scan(tree.cond);
        scan(tree.truepart);
        scan(tree.falsepart);
    }

    public void visitBreak(JFXBreak tree) {
    }

    public void visitContinue(JFXContinue tree) {
    }

    public void visitReturn(JFXReturn tree) {
        scan(tree.expr);
    }

    public void visitThrow(JFXThrow tree) {
        scan(tree.expr);
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        scan(tree.meth);
        scan(tree.args);
    }

    public void visitParens(JFXParens tree) {
        scan(tree.expr);
    }

    public void visitAssign(JFXAssign tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitAssignop(JFXAssignOp tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitUnary(JFXUnary tree) {
        scan(tree.arg);
    }

    public void visitBinary(JFXBinary tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        scan(tree.clazz);
        scan(tree.expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        scan(tree.expr);
        scan(tree.clazz);
    }

    public void visitSelect(JFXSelect tree) {
        scan(tree.selected);
    }

    public void visitIdent(JFXIdent tree) {
    }

    public void visitLiteral(JFXLiteral tree) {
    }

    public void visitModifiers(JFXModifiers tree) {
    }

    public void visitErroneous(JFXErroneous tree) {
    }

    // Begin JavaFX trees
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        scan(that.mods);
        for (Tree member : that.getMembers()) {
            scan((JFXTree)member);
        }
    }
    
    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        for (JFXVar param : tree.getParams()) {
            scan(param);
        }
        scan(tree.getBodyExpression());
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        scan(tree.getModifiers());
        scan(tree.getJFXReturnType());
        visitFunctionValue(tree.operation);
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        scan((JFXBlock)that.getBody());
    }

    public void visitPostInitDefinition(JFXPostInitDefinition that) {
        scan((JFXBlock)that.getBody());
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        scan( that.getLower() );
        scan( that.getUpper() );
        scan( that.getStepOrNull() );
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        scan( that.getItems() );
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        scan(that.getSequence());
        scan(that.getIndex());
    }
    
    public void visitSequenceSlice(JFXSequenceSlice that) {
        scan(that.getSequence());
        scan(that.getFirstIndex());
        scan(that.getLastIndex());
    }
    
    @Override
    public void visitSequenceInsert(JFXSequenceInsert that) {
        scan(that.getSequence());
        scan(that.getElement());
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete that) {
        scan(that.getSequence());
        scan(that.getElement());
    }

    @Override
    public void visitStringExpression(JFXStringExpression that) {
        List<JFXExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            scan(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    @Override
    public void visitInstanciate(JFXInstanciate tree) {
       scan(tree.getIdentifier());
       scan(tree.getArgs());
       scan(tree.getParts());
       scan(tree.getLocalvars());
       scan(tree.getClassBody());
    }
    
    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        scan(that.getExpression());
    }  
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        for (JFXTree param : (List<JFXType>)that.getParameters()) {
            scan(param);
        }
        scan((JFXType)that.getReturnType());
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
    }
    
    @Override
    public void visitVarScriptInit(JFXVarScriptInit tree) {
    }

    @Override
    public void visitVar(JFXVar tree) {
        scan(tree.getJFXType());
	scan(tree.mods);
	scan(tree.init);
        scan(tree.getOnReplace());
    }
    
    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        scan(tree.getId());
	scan(tree.getInitializer());
        scan(tree.getOnReplace());
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        scan(tree.getFirstIndex());
	scan(tree.getOldValue());  
        scan(tree.getBody());
        scan(tree.getLastIndex());
        scan(tree.getNewElements());
    }
    
    
    @Override
    public void visitForExpression(JFXForExpression that) {
        for (ForExpressionInClauseTree cl : that.getInClauses()) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
            scan(clause);
        }
        scan(that.getBodyExpression());
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        scan(that.getVar());
        scan(that.getSequenceExpression());
        scan(that.getWhereExpression());
    }
    
    @Override
    public void visitBindExpression(JFXBindExpression that) {
        scan(that.expr);
    }
    
    @Override
    public void visitBlockExpression(JFXBlock that) {
        scan(that.stats);
        scan(that.value);
    }
    
    @Override
    public void visitIndexof(JFXIndexof that) {
    }

    public void visitTree(JFXTree that) {
        assert false : "Should not be here!!!";
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
    }

    public void visitInterpolate(JFXInterpolate that) {
        scan(that.var);
        scan(that.values);
    }

    public void visitInterpolateValue(JFXInterpolateValue that) {
        scan(that.attribute);
        scan(that.value);
    }
    
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
        scan(that.start);
        for (JFXExpression value: that.values)
            scan(value);
        if (that.trigger != null)
            scan(that.trigger);
    }
}
