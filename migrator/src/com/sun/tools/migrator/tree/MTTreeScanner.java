/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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

package com.sun.tools.migrator.tree;

import com.sun.tools.javac.util.*;
import com.sun.tools.migrator.tree.MTTree.*;

/**
 *
 * @author Robert Field
 */
public class MTTreeScanner implements MTVisitor {

    public MTTreeScanner() {
    }

    /** Visitor method: Scan a single node.
   */
    public void scan(MTTree tree) {
	if(tree!=null) tree.accept(this);
    }

    /** Visitor method: scan a list of nodes.
     */
    public void scan(List<? extends MTTree> trees) {
	if (trees != null)
	for (List<? extends MTTree> l = trees; l.nonEmpty(); l = l.tail)
	    scan(l.head);
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/

    public void visitTopLevel(MTCompilationUnit tree) {
	scan(tree.pid);
	scan(tree.defs);
    }

    public void visitImport(MTImport tree) {
	scan(tree.qualid);
    }

    public void visitVarDef(MTVariableDecl tree) {
	scan(tree.mods);
	scan(tree.vartype);
	scan(tree.init);
    }
	
    public void visitSkip(MTSkip tree) {
    }

    public void visitBlock(MTBlock tree) {
	scan(tree.stats);
    }

    public void visitWhileLoop(MTWhileLoop tree) {
	scan(tree.cond);
	scan(tree.body);
    }

    public void visitTry(MTTry tree) {
	scan(tree.body);
	scan(tree.catchers);
	scan(tree.finalizer);
    }

    public void visitCatch(MTCatch tree) {
	scan(tree.param);
	scan(tree.body);
    }

    public void visitConditional(MTConditional tree) {
	scan(tree.cond);
	scan(tree.truepart);
	scan(tree.falsepart);
    }

    public void visitIf(MTIf tree) {
	scan(tree.cond);
	scan(tree.thenpart);
	scan(tree.elsepart);
    }

    public void visitExec(MTExpressionStatement tree) {
	scan(tree.expr);
    }

    public void visitBreak(MTBreak tree) {
    }

    public void visitContinue(MTContinue tree) {
    }

    public void visitReturn(MTReturn tree) {
	scan(tree.expr);
    }

    public void visitThrow(MTThrow tree) {
	scan(tree.expr);
    }

    public void visitAssert(MTAssert tree) {
	scan(tree.cond);
	scan(tree.detail);
    }

    public void visitApply(MTMethodInvocation tree) {
	scan(tree.meth);
	scan(tree.args);
    }

    public void visitParens(MTParens tree) {
	scan(tree.expr);
    }

    public void visitAssign(MTAssign tree) {
	scan(tree.lhs);
	scan(tree.rhs);
    }

    public void visitAssignop(MTAssignOp tree) {
	scan(tree.lhs);
	scan(tree.rhs);
    }

    public void visitUnary(MTUnary tree) {
	scan(tree.arg);
    }

    public void visitBinary(MTBinary tree) {
	scan(tree.lhs);
	scan(tree.rhs);
    }

    public void visitTypeCast(MTTypeCast tree) {
	scan(tree.clazz);
	scan(tree.expr);
    }

    public void visitTypeTest(MTInstanceOf tree) {
	scan(tree.expr);
	scan(tree.clazz);
    }

    public void visitIndexed(MTArrayAccess tree) {
	scan(tree.indexed);
	scan(tree.index);
    }

    public void visitSelect(MTFieldAccess tree) {
	scan(tree.selected);
    }

    public void visitIdent(MTIdent tree) {
    }

    public void visitLiteral(MTLiteral tree) {
    }

    public void visitTypeIdent(MTPrimitiveTypeTree tree) {
    }

    public void visitTypeArray(MTArrayTypeTree tree) {
	scan(tree.elemtype);
    }

    public void visitTypeApply(MTTypeApply tree) {
	scan(tree.clazz);
	scan(tree.arguments);
    }

    public void visitTypeParameter(MTTypeParameter tree) {
	scan(tree.bounds);
    }

    @Override
    public void visitWildcard(MTWildcard tree) {
        scan(tree.kind);
        if (tree.inner != null)
            scan(tree.inner);
    }

    @Override
    public void visitTypeBoundKind(MTTypeBoundKind tree) {
    }

    public void visitModifiers(MTModifiers tree) {
	scan(tree.annotations);
    }

    public void visitAnnotation(MTAnnotation tree) {
	scan(tree.annotationType);
	scan(tree.args);
    }

    public void visitErroneous(MTErroneous tree) {
    }

    public void visitLetExpr(MTLetExpr tree) {
	scan(tree.defs);
	scan(tree.expr);
    }


    @Override
    public void visitTree(MTTree tree) {
        assert false : "Should not be here!!!";
    }

/* ***************************************************************************
 * Visitor methods
 ****************************************************************************/
    // Begin JavaFX trees
    
    @Override
    public void visitClassDeclaration(MTClassDeclaration tree) {
	scan(tree.mods);
	scan(tree.getSupertypes());
	scan(tree.defs);
    }
    
    @Override
    public void visitFunctionValue(MTOperationValue tree) {
        for (MTVar param : tree.getParameters()) {
            param.accept((MTVisitor)this);
        }
        if (tree.getBodyExpression() != null) {
            tree.getBodyExpression().accept((MTVisitor)this);
        }
    }

    @Override
    public void visitFunctionDefinition(MTOperationDefinition tree) {
        tree.getModifiers().accept(this);
        tree.getJFXReturnType().accept((MTVisitor)this);
        visitFunctionValue(tree.operation);
    }

    @Override
    public void visitInitDefinition(MTInitDefinition tree) {
        tree.getBody().accept(this);
    }

    @Override
    public void visitMemberSelector(MTMemberSelector tree) {
    }
    
    @Override
    public void visitSequenceEmpty(MTSequenceEmpty tree) {
    }
    
    @Override
    public void visitSequenceRange(MTSequenceRange tree) {
        tree.getLower().accept(this);
        tree.getUpper().accept(this);
    }
    
    @Override
    public void visitSequenceExplicit(MTSequenceExplicit tree) {
        for (MTExpression expr : tree.getItems()) {
            expr.accept(this);
        }
    }

    @Override
    public void visitSequenceIndexed(MTSequenceIndexed tree) {
        tree.getSequence().accept(this);
        tree.getIndex().accept(this);
    }
    
    @Override
    public void visitSequenceInsert(MTSequenceInsert tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }
    
    @Override
    public void visitSequenceDelete(MTSequenceDelete tree) {
        scan(tree.getSequence());
        scan(tree.getElement());
    }

    @Override
    public void visitStringExpression(MTStringExpression tree) {
        List<MTExpression> parts = tree.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            parts.head.accept(this);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    @Override
    public void visitPureObjectLiteral(MTPureObjectLiteral tree) {
        tree.getIdentifier().accept(this);
        for (MTStatement part : tree.getParts()) {
            if (part != null) {
                part.accept(this);
            }
        }
    }
    
    public void visitVarIsObjectBeingInitialized(MTVarIsObjectBeingInitialized tree) {
        assert false : "not yet implemented";
    }
    
    @Override
    public void visitSetAttributeToObjectBeingInitialized(MTSetAttributeToObjectBeingInitialized tree) {
        assert false : "not yet implemented";
    }
    
    @Override
    public void visitObjectLiteralPart(MTObjectLiteralPart tree) {
        tree.getExpression().accept(this);
    }  
    
    @Override
    public void visitTypeAny(MTTypeAny tree) {
    }
    
    @Override
    public void visitTypeClass(MTTypeClass tree) {
    }
    
    @Override
    public void visitTypeFunctional(MTTypeFunctional tree) {
        for (MTTree param : tree.getParameters()) {
            param.accept(this);
        }
        tree.getReturnType().accept((MTVisitor)this);
    }
    
    @Override
    public void visitTypeUnknown(MTTypeUnknown tree) {
    }
    
    @Override
    public void visitVar(MTVar tree) {
        scan(tree.getJFXType());
	scan(tree.mods);
	scan(tree.init);
    }
    
    public void visitAbstractOnChange(MTAbstractOnChange tree) {
	scan(tree.getIndex());
	scan(tree.getOldValue());  
        scan(tree.getBody());
    }
    
    @Override
    public void visitOnReplace(MTOnReplace tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnReplaceElement(MTOnReplaceElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnInsertElement(MTOnInsertElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitOnDeleteElement(MTOnDeleteElement tree) {
        visitAbstractOnChange(tree);
    }
    
    @Override
    public void visitForExpression(MTForExpression tree) {
        for (MTForExpressionInClause clause : tree.getInClauses()) {
            clause.accept((MTVisitor)this);
        }
        tree.getBodyExpression().accept(this);
    }
    
    @Override
    public void visitForExpressionInClause(MTForExpressionInClause tree) {
        tree.getVar().accept((MTVisitor)this);
        tree.getSequenceExpression().accept(this);
        if (tree.getWhereExpression() != null) {
            tree.getWhereExpression().accept(this);
        }
    }
    
    @Override
    public void visitInstanciate(MTInstanciate tree) {
	scan(tree.encl);
	scan(tree.clazz);
	scan(tree.args);
	scan(tree.def);
    }
        
    @Override
    public void visitBlockExpression(MTBlockExpression tree) {
        scan(tree.stats);
        scan(tree.value);
    }
}
