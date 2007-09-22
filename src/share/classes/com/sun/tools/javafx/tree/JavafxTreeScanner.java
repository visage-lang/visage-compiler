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

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;

/**
 *
 * @author Robert Field
 */
public class JavafxTreeScanner extends TreeScanner implements JavafxVisitor {
    public boolean shouldVisitRemoved;
    public boolean shouldVisitSynthetic;

    public JavafxTreeScanner() {
        this.shouldVisitRemoved = false;
        this.shouldVisitSynthetic = true;
    }

    /** Visitor method: Scan a single node.
   */
    public void scan(JCTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(List<? extends JCTree> trees) {
	if (trees != null)
	for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }


/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/
    // Begin JavaFX trees
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration that) {
        that.mods.accept(this);
        for (JCTree member : that.defs) {
            member.accept(this);
        }
    }
    
    @Override
    public void visitAbstractMember(JFXAbstractMember that) {
        that.modifiers.accept(this);
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
    }
    
    @Override
    public void visitAbstractFunction(JFXAbstractFunction that) {
        visitAbstractMember(that);
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition that) {
        visitVarDef(that);
        if (that.getInitializer() != null) {
            that.getInitializer().accept(this);
        }
    }
    
    @Override
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement that) {
        visitOperationDefinition(that.funcDef);
    }

    @Override
    public void visitOperationDefinition(JFXOperationDefinition that) {
        visitMethodDef(that);
        that.getBodyExpression().accept((JavafxVisitor)this);
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitDoLater(JFXDoLater that) {
        that.getBody().accept(this);
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        that.getLower().accept(this);
        that.getUpper().accept(this);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        for (JCExpression expr : that.getItems()) {
            expr.accept(this);
        }
    }

    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        that.getSequence().accept(this);
        that.getIndex().accept(this);
    }
    
    @Override
    public void visitStringExpression(JFXStringExpression that) {
        List<JCExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            parts.head.accept(this);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    @Override
    public void visitPureObjectLiteral(JFXPureObjectLiteral that) {
        that.getIdentifier().accept(this);
        for (JCStatement part : that.getParts()) {
            part.accept(this);
        }
    }
    
    @Override
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that) {
        visitVar(that);
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
    }
    
    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.getExpression().accept(this);
    }  
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
        visitType(that);
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getReturnType().accept((JavafxVisitor)this);
        visitType(that);
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        visitType(that);
    }
    
    @Override
    public void visitType(JFXType that) {
    }
    
    @Override
    public void visitVar(JFXVar that) {
        visitType(that.getJFXType());
    }
    
    @Override
    public void visitForExpression(JFXForExpression that) {
        for (JFXForExpressionInClause clause : that.getInClauses()) {
            clause.accept((JavafxVisitor)this);
        }
        that.getBodyExpression().accept(this);
    }
    
    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        that.getVar().accept((JavafxVisitor)this);
        that.getSequenceExpression().accept(this);
        if (that.getWhereExpression() != null) {
            that.getWhereExpression().accept(this);
        }
    }
    
    @Override
    public void visitInstanciate(JFXInstanciate that) {
        visitNewClass(that);
    }
        
    @Override
    public boolean shouldVisitRemoved() {
        return shouldVisitRemoved;
    }
    
    @Override
    public boolean shouldVisitSynthetic() {
        return shouldVisitSynthetic;
    }
    
    @Override
    public void visitBlockExpression(JFXBlockExpression that) {
        scan(that.stats);
        scan(that.value);
    }

    @Override
    public void visitTree(JCTree that) {
        assert false : "Should not be here!!!";
    }
}
