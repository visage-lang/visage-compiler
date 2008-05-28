/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import  com.sun.tools.migrator.tree.MTTree.*;

/** The visitor interface for JavaFX trees.
 */
public interface MTVisitor {  
    public void visitClassDeclaration(MTClassDeclaration tree);
    public void visitFunctionDefinition(MTOperationDefinition tree);
    public void visitInitDefinition(MTInitDefinition tree);
    public void visitMemberSelector(MTMemberSelector tree);
    public void visitStringExpression(MTStringExpression tree);
    public void visitPureObjectLiteral(MTPureObjectLiteral tree);
    public void visitSetAttributeToObjectBeingInitialized(MTSetAttributeToObjectBeingInitialized tree);
    public void visitObjectLiteralPart(MTObjectLiteralPart tree);
    public void visitTypeAny(MTTypeAny tree);
    public void visitTypeClass(MTTypeClass tree);
    public void visitTypeFunctional(MTTypeFunctional tree);
    public void visitTypeUnknown(MTTypeUnknown tree);
    public void visitVar(MTVar tree);
    public void visitOnReplace(MTOnReplace tree);
    public void visitOnReplaceElement(MTOnReplaceElement tree);
    public void visitOnInsertElement(MTOnInsertElement tree);
    public void visitOnDeleteElement(MTOnDeleteElement tree);
    public void visitBlockExpression(MTBlockExpression tree);
    public void visitFunctionValue(MTOperationValue tree);
    public void visitSequenceEmpty(MTSequenceEmpty tree);
    public void visitSequenceRange(MTSequenceRange tree);
    public void visitSequenceExplicit(MTSequenceExplicit tree);
    public void visitSequenceIndexed(MTSequenceIndexed tree);
    public void visitSequenceInsert(MTSequenceInsert tree);
    public void visitSequenceDelete(MTSequenceDelete tree);
    public void visitForExpression(MTForExpression tree);
    public void visitForExpressionInClause(MTForExpressionInClause tree);
    public void visitInstanciate(MTInstanciate tree);
    public void visitVarIsObjectBeingInitialized(MTVarIsObjectBeingInitialized tree);

        public void visitTopLevel(MTCompilationUnit that);
        public void visitImport(MTImport that);
        public void visitVarDef(MTVariableDecl that);
        public void visitSkip(MTSkip that);
        public void visitBlock(MTBlock that);
        public void visitWhileLoop(MTWhileLoop that);
        public void visitTry(MTTry that);
        public void visitCatch(MTCatch that);
        public void visitConditional(MTConditional that);
        public void visitIf(MTIf that);
        public void visitExec(MTExpressionStatement that);
        public void visitBreak(MTBreak that);
        public void visitContinue(MTContinue that);
        public void visitReturn(MTReturn that);
        public void visitThrow(MTThrow that);
        public void visitAssert(MTAssert that);
        public void visitApply(MTMethodInvocation that);
        public void visitParens(MTParens that);
        public void visitAssign(MTAssign that);
        public void visitAssignop(MTAssignOp that);
        public void visitUnary(MTUnary that);
        public void visitBinary(MTBinary that);
        public void visitTypeCast(MTTypeCast that);
        public void visitTypeTest(MTInstanceOf that);
        public void visitIndexed(MTArrayAccess that);
        public void visitSelect(MTFieldAccess that);
        public void visitIdent(MTIdent that);
        public void visitLiteral(MTLiteral that);
        public void visitTypeIdent(MTPrimitiveTypeTree that);
        public void visitTypeArray(MTArrayTypeTree that);
        public void visitTypeApply(MTTypeApply that);
        public void visitTypeParameter(MTTypeParameter that);
        public void visitWildcard(MTWildcard that);
        public void visitTypeBoundKind(MTTypeBoundKind that);
        public void visitAnnotation(MTAnnotation that);
        public void visitModifiers(MTModifiers that);
        public void visitErroneous(MTErroneous that);
        public void visitLetExpr(MTLetExpr that);

        public void visitTree(MTTree that);


}
