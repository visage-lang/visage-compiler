/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;

/**
 * An abstract tree walker (visitor) for ASTs ({@code JCTree}s).
 * Each {@code visitXxx} method calls {@code scan} to visit its child
 * trees.  The {@code scan} method calls the {@code JCTree}-subclass-specific
 * {@code accept} method.  A sub-class can override a specific {@code visitXxx}
 * method, or the {@code scan method}.
 * 
 * @author Robert Field
 * @author Per Bothner
 */
public class JavafxTreeScanner extends TreeScanner implements JavafxVisitor {

    public JavafxTreeScanner() {
    }

    /** Visitor method: Scan a single node.
   */
    @Override
    public void scan(JCTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
    @Override
    public void scan(List<? extends JCTree> trees) {
	if (trees != null)
	for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(java.util.List<? extends Tree> trees) {
	if (trees != null)
            for (Tree t : trees)
                scan((JCTree)t);
    }


/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/
    // Begin JavaFX trees
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        scan(that.mods);
        for (Tree member : that.getMembers()) {
            scan((JCTree)member);
        }
    }
    
    @Override
    public void visitOperationValue(JFXFunctionValue tree) {
        for (JFXVar param : tree.getParams()) {
            scan(param);
        }
        scan(tree.getBodyExpression());
    }

    @Override
    public void visitOperationDefinition(JFXFunctionDefinition tree) {
        scan(tree.getModifiers());
        scan(tree.getJFXReturnType());
        visitOperationValue(tree.operation);
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        scan((JCBlock)that.getBody());
    }

    public void visitPostInitDefinition(JFXPostInitDefinition that) {
        scan((JCBlock)that.getBody());
    }

    @Override
    public void visitDoLater(JFXDoLater that) {
        scan(that.getBody());
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
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
        List<JCExpression> parts = that.getParts();
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
       scan(tree.getParts());
       scan(tree.getLocalvars());
       scan(tree.getClassBody());
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
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
        for (JCTree param : (List<JFXType>)that.getParameters()) {
            scan(param);
        }
        scan((JFXType)that.getReturnType());
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        scan(tree.getJFXType());
	scan(tree.mods);
	scan(tree.init);
        scan(tree.getOnReplace());
    }
    
    @Override
    public void visitOverrideAttribute(JFXOverrideAttribute tree) {
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
    public void visitBlockExpression(JFXBlockExpression that) {
        scan(that.stats);
        scan(that.value);
    }
    
    @Override
    public void visitIndexof(JFXIndexof that) {
    }

    @Override
    public void visitTree(JCTree that) {
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
}