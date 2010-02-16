/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.mjavac.util.List;

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

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        scan(tree.mods);
        for (Tree member : tree.getMembers()) {
            scan((JFXTree)member);
        }
    }
    
    public void visitFunctionValue(JFXFunctionValue tree) {
        for (JFXVar param : tree.getParams()) {
            scan(param);
        }
        scan(tree.getBodyExpression());
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        scan(tree.getModifiers());
        scan(tree.getJFXReturnType());
        visitFunctionValue(tree.operation);
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        scan((JFXBlock)tree.getBody());
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        scan((JFXBlock)tree.getBody());
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
    }
    
    public void visitSequenceRange(JFXSequenceRange tree) {
        scan( tree.getLower() );
        scan( tree.getUpper() );
        scan( tree.getStepOrNull() );
    }
    
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        scan( tree.getItems() );
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        scan(tree.getSequence());
        scan(tree.getIndex());
    }
    
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        scan(tree.getSequence());
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
    }
    
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }
    
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }

    public void visitInvalidate(JFXInvalidate tree) {
        scan(tree.getVariable());
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = tree.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            scan(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    public void visitInstanciate(JFXInstanciate tree) {
       scan(tree.getIdentifier());
       scan(tree.getArgs());
       scan(tree.getParts());
       scan(tree.getLocalvars());
       scan(tree.getClassBody());
    }
    
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        scan(tree.getExpression());
    }  
    
    public void visitTypeAny(JFXTypeAny tree) {
    }
    
    public void visitTypeClass(JFXTypeClass tree) {
    }
    
    public void visitTypeFunctional(JFXTypeFunctional tree) {
        for (JFXTree param : (List<JFXType>)tree.getParameters()) {
            scan(param);
        }
        scan((JFXType)tree.getReturnType());
    }
    
    public void visitTypeArray(JFXTypeArray tree) {
        scan((JFXType)tree.getElementType());
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
    }
    
    public void visitVarInit(JFXVarInit tree) {
    }

    public void visitVarRef(JFXVarRef tree) {
    }

    public void visitVar(JFXVar tree) {
        scan(tree.getJFXType());
        scan(tree.mods);
        scan(tree.getInitializer());
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        scan(tree.getId());
        scan(tree.getInitializer());
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    public void visitOnReplace(JFXOnReplace tree) {
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
        scan(tree.getOldValue());
        scan(tree.getNewElements());
        scan(tree.getBody());
    }
    
    
    public void visitForExpression(JFXForExpression tree) {
        for (ForExpressionInClauseTree cl : tree.getInClauses()) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
            scan(clause);
        }
        scan(tree.getBodyExpression());
    }
    
    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        scan(tree.getVar());
        scan(tree.getSequenceExpression());
        scan(tree.getWhereExpression());
    }
    
    public void visitBlockExpression(JFXBlock tree) {
        scan(tree.stats);
        scan(tree.value);
    }
    
    public void visitIndexof(JFXIndexof tree) {
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        scan(tree.attribute);
        scan(tree.value);
        if  (tree.interpolation != null) {
            scan(tree.interpolation);
        }
    }
    
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        scan(tree.start);
        for (JFXExpression value: tree.values)
            scan(value);
        if (tree.trigger != null)
            scan(tree.trigger);
    }

    public void visitTree(JFXTree tree) {
        assert false : "Should not be here!!!";
    }
}
