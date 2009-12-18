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

/** The visitor interface for JavaFX trees.
 */
public interface JavafxVisitor {
    public void visitScript(JFXScript tree);
    public void visitImport(JFXImport tree);
    public void visitSkip(JFXSkip tree);
    public void visitWhileLoop(JFXWhileLoop tree);
    public void visitTry(JFXTry tree);
    public void visitCatch(JFXCatch tree);
    public void visitIfExpression(JFXIfExpression tree);
    public void visitBreak(JFXBreak tree);
    public void visitContinue(JFXContinue tree);
    public void visitReturn(JFXReturn tree);
    public void visitThrow(JFXThrow tree);
    public void visitFunctionInvocation(JFXFunctionInvocation tree);
    public void visitParens(JFXParens tree);
    public void visitAssign(JFXAssign tree);
    public void visitAssignop(JFXAssignOp tree);
    public void visitUnary(JFXUnary tree);
    public void visitBinary(JFXBinary tree);
    public void visitTypeCast(JFXTypeCast tree);
    public void visitInstanceOf(JFXInstanceOf tree);
    public void visitSelect(JFXSelect tree);
    public void visitIdent(JFXIdent tree);
    public void visitLiteral(JFXLiteral tree);
    public void visitModifiers(JFXModifiers tree);
    public void visitErroneous(JFXErroneous tree);
    public void visitClassDeclaration(JFXClassDeclaration tree);
    public void visitFunctionDefinition(JFXFunctionDefinition tree);
    public void visitInitDefinition(JFXInitDefinition tree);
    public void visitPostInitDefinition(JFXPostInitDefinition tree);
    public void visitStringExpression(JFXStringExpression tree);
    public void visitInstanciate(JFXInstanciate tree);
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree);
    public void visitTypeAny(JFXTypeAny tree);
    public void visitTypeClass(JFXTypeClass tree);
    public void visitTypeFunctional(JFXTypeFunctional tree);
    public void visitTypeArray(JFXTypeArray tree);
    public void visitTypeUnknown(JFXTypeUnknown tree);
    public void visitVar(JFXVar tree);
    public void visitVarInit(JFXVarInit tree);
    public void visitVarRef(JFXVarRef tree);
    public void visitOnReplace(JFXOnReplace tree);
    public void visitBlockExpression(JFXBlock tree);
    public void visitFunctionValue(JFXFunctionValue tree);
    public void visitSequenceEmpty(JFXSequenceEmpty tree);
    public void visitSequenceRange(JFXSequenceRange tree);
    public void visitSequenceExplicit(JFXSequenceExplicit tree);
    public void visitSequenceIndexed(JFXSequenceIndexed tree);
    public void visitSequenceSlice(JFXSequenceSlice tree);
    public void visitSequenceInsert(JFXSequenceInsert tree);
    public void visitSequenceDelete(JFXSequenceDelete tree);
    public void visitInvalidate(JFXInvalidate tree);
    public void visitForExpression(JFXForExpression tree);
    public void visitForExpressionInClause(JFXForExpressionInClause tree);
    public void visitIndexof(JFXIndexof tree);
    public void visitTimeLiteral(JFXTimeLiteral tree);
    public void visitOverrideClassVar(JFXOverrideClassVar tree);
    public void visitInterpolateValue(JFXInterpolateValue tree);
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree);
}
