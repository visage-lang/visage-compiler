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

package org.visage.tools.tree;

import org.visage.api.tree.ForExpressionInClauseTree;
import org.visage.api.tree.Tree;
import com.sun.tools.mjavac.util.List;

/**
 * An abstract tree walker (visitor) for ASTs ({@code VisageTree}s).
 * Each {@code visitXxx} method calls {@code scan} to visit its child
 * trees.  The {@code scan} method calls the {@code VisageTree}-subclass-specific
 * {@code accept} method.  A sub-class can override a specific {@code visitXxx}
 * method, or the {@code scan method}.
 * 
 * @author Robert Field
 * @author Per Bothner
 */
public class VisageTreeScanner implements VisageVisitor {

    public VisageTreeScanner() {
    }

    /** Visitor method: Scan a single node.
   */
    public void scan(VisageTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
     public void scan(List<? extends VisageTree> trees) {
	if (trees != null)
	for (List<? extends VisageTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(java.util.List<? extends Tree> trees) {
	if (trees != null)
            for (Tree t : trees)
                scan((VisageTree)t);
    }


/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/
    
    public void visitScript(VisageScript tree) {
        scan(tree.pid);
        scan(tree.defs);
    }

    public void visitImport(VisageImport tree) {
        scan(tree.qualid);
    }

    public void visitSkip(VisageSkip tree) {
    }

    public void visitWhileLoop(VisageWhileLoop tree) {
        scan(tree.cond);
        scan(tree.body);
    }

    public void visitTry(VisageTry tree) {
        scan(tree.body);
        scan(tree.catchers);
        scan(tree.finalizer);
    }

    public void visitCatch(VisageCatch tree) {
        scan(tree.param);
        scan(tree.body);
    }

    public void visitIfExpression(VisageIfExpression tree) {
        scan(tree.cond);
        scan(tree.truepart);
        scan(tree.falsepart);
    }

    public void visitBreak(VisageBreak tree) {
    }

    public void visitContinue(VisageContinue tree) {
    }

    public void visitReturn(VisageReturn tree) {
        scan(tree.expr);
    }

    public void visitThrow(VisageThrow tree) {
        scan(tree.expr);
    }

    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        scan(tree.meth);
        scan(tree.args);
    }

    public void visitParens(VisageParens tree) {
        scan(tree.expr);
    }

    public void visitAssign(VisageAssign tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitAssignop(VisageAssignOp tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitUnary(VisageUnary tree) {
        scan(tree.arg);
    }

    public void visitBinary(VisageBinary tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitTypeCast(VisageTypeCast tree) {
        scan(tree.clazz);
        scan(tree.expr);
    }

    public void visitInstanceOf(VisageInstanceOf tree) {
        scan(tree.expr);
        scan(tree.clazz);
    }

    public void visitSelect(VisageSelect tree) {
        scan(tree.selected);
    }

    public void visitIdent(VisageIdent tree) {
    }

    public void visitLiteral(VisageLiteral tree) {
    }

    public void visitModifiers(VisageModifiers tree) {
    }

    public void visitErroneous(VisageErroneous tree) {
    }

    public void visitClassDeclaration(VisageClassDeclaration tree) {
        scan(tree.mods);
        for (Tree member : tree.getMembers()) {
            scan((VisageTree)member);
        }
    }
    
    public void visitFunctionValue(VisageFunctionValue tree) {
        for (VisageVar param : tree.getParams()) {
            scan(param);
        }
        scan(tree.getBodyExpression());
    }

    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        scan(tree.getModifiers());
        scan(tree.getJFXReturnType());
        visitFunctionValue(tree.operation);
    }

    public void visitInitDefinition(VisageInitDefinition tree) {
        scan((VisageBlock)tree.getBody());
    }

    public void visitPostInitDefinition(VisagePostInitDefinition tree) {
        scan((VisageBlock)tree.getBody());
    }

    public void visitSequenceEmpty(VisageSequenceEmpty tree) {
    }
    
    public void visitSequenceRange(VisageSequenceRange tree) {
        scan( tree.getLower() );
        scan( tree.getUpper() );
        scan( tree.getStepOrNull() );
    }
    
    public void visitSequenceExplicit(VisageSequenceExplicit tree) {
        scan( tree.getItems() );
    }

    public void visitSequenceIndexed(VisageSequenceIndexed tree) {
        scan(tree.getSequence());
        scan(tree.getIndex());
    }
    
    public void visitSequenceSlice(VisageSequenceSlice tree) {
        scan(tree.getSequence());
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
    }
    
    public void visitSequenceInsert(VisageSequenceInsert tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }
    
    public void visitSequenceDelete(VisageSequenceDelete tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }

    public void visitInvalidate(VisageInvalidate tree) {
        scan(tree.getVariable());
    }

    public void visitStringExpression(VisageStringExpression tree) {
        List<VisageExpression> parts = tree.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            scan(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    public void visitInstanciate(VisageInstanciate tree) {
       scan(tree.getIdentifier());
       scan(tree.getArgs());
       scan(tree.getParts());
       scan(tree.getLocalvars());
       scan(tree.getClassBody());
    }
    
    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        scan(tree.getExpression());
    }  
    
    public void visitTypeAny(VisageTypeAny tree) {
    }
    
    public void visitTypeClass(VisageTypeClass tree) {
    }
    
    public void visitTypeFunctional(VisageTypeFunctional tree) {
        for (VisageTree param : (List<VisageType>)tree.getParameters()) {
            scan(param);
        }
        scan((VisageType)tree.getReturnType());
    }
    
    public void visitTypeArray(VisageTypeArray tree) {
        scan((VisageType)tree.getElementType());
    }

    public void visitTypeUnknown(VisageTypeUnknown tree) {
    }
    
    public void visitVarInit(VisageVarInit tree) {
    }

    public void visitVarRef(VisageVarRef tree) {
    }

    public void visitVar(VisageVar tree) {
        scan(tree.getJFXType());
        scan(tree.mods);
        scan(tree.getInitializer());
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        scan(tree.getId());
        scan(tree.getInitializer());
        scan(tree.getOnReplace());
        scan(tree.getOnInvalidate());
    }

    public void visitOnReplace(VisageOnReplace tree) {
        scan(tree.getFirstIndex());
        scan(tree.getLastIndex());
        scan(tree.getOldValue());
        scan(tree.getNewElements());
        scan(tree.getBody());
    }
    
    
    public void visitForExpression(VisageForExpression tree) {
        for (ForExpressionInClauseTree cl : tree.getInClauses()) {
            VisageForExpressionInClause clause = (VisageForExpressionInClause)cl;
            scan(clause);
        }
        scan(tree.getBodyExpression());
    }
    
    public void visitForExpressionInClause(VisageForExpressionInClause tree) {
        scan(tree.getVar());
        scan(tree.getSequenceExpression());
        scan(tree.getWhereExpression());
    }
    
    public void visitBlockExpression(VisageBlock tree) {
        scan(tree.stats);
        scan(tree.value);
    }
    
    public void visitIndexof(VisageIndexof tree) {
    }

    public void visitTimeLiteral(VisageTimeLiteral tree) {
    }

    public void visitLengthLiteral(VisageLengthLiteral tree) {
    }

    public void visitAngleLiteral(VisageAngleLiteral tree) {
    }

    public void visitColorLiteral(VisageColorLiteral tree) {
    }

    public void visitInterpolateValue(VisageInterpolateValue tree) {
        scan(tree.attribute);
        scan(tree.value);
        if  (tree.interpolation != null) {
            scan(tree.interpolation);
        }
    }
    
    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree) {
        scan(tree.start);
        for (VisageExpression value: tree.values)
            scan(value);
        if (tree.trigger != null)
            scan(tree.trigger);
    }

    public void visitTree(VisageTree tree) {
        assert false : "Should not be here!!!";
    }
}
