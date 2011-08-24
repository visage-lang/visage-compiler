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

/** The visitor interface for Visage trees.
 */
public interface JavafxVisitor {
    public void visitScript(VisageScript tree);
    public void visitImport(VisageImport tree);
    public void visitSkip(VisageSkip tree);
    public void visitWhileLoop(VisageWhileLoop tree);
    public void visitTry(VisageTry tree);
    public void visitCatch(VisageCatch tree);
    public void visitIfExpression(VisageIfExpression tree);
    public void visitBreak(VisageBreak tree);
    public void visitContinue(VisageContinue tree);
    public void visitReturn(VisageReturn tree);
    public void visitThrow(VisageThrow tree);
    public void visitFunctionInvocation(VisageFunctionInvocation tree);
    public void visitParens(VisageParens tree);
    public void visitAssign(VisageAssign tree);
    public void visitAssignop(VisageAssignOp tree);
    public void visitUnary(VisageUnary tree);
    public void visitBinary(VisageBinary tree);
    public void visitTypeCast(VisageTypeCast tree);
    public void visitInstanceOf(VisageInstanceOf tree);
    public void visitSelect(VisageSelect tree);
    public void visitIdent(VisageIdent tree);
    public void visitLiteral(VisageLiteral tree);
    public void visitModifiers(VisageModifiers tree);
    public void visitErroneous(VisageErroneous tree);
    public void visitClassDeclaration(VisageClassDeclaration tree);
    public void visitFunctionDefinition(VisageFunctionDefinition tree);
    public void visitInitDefinition(VisageInitDefinition tree);
    public void visitPostInitDefinition(VisagePostInitDefinition tree);
    public void visitStringExpression(VisageStringExpression tree);
    public void visitInstanciate(VisageInstanciate tree);
    public void visitObjectLiteralPart(VisageObjectLiteralPart tree);
    public void visitTypeAny(VisageTypeAny tree);
    public void visitTypeClass(VisageTypeClass tree);
    public void visitTypeFunctional(VisageTypeFunctional tree);
    public void visitTypeArray(VisageTypeArray tree);
    public void visitTypeUnknown(VisageTypeUnknown tree);
    public void visitVar(VisageVar tree);
    public void visitVarInit(VisageVarInit tree);
    public void visitVarRef(VisageVarRef tree);
    public void visitOnReplace(VisageOnReplace tree);
    public void visitBlockExpression(VisageBlock tree);
    public void visitFunctionValue(VisageFunctionValue tree);
    public void visitSequenceEmpty(VisageSequenceEmpty tree);
    public void visitSequenceRange(VisageSequenceRange tree);
    public void visitSequenceExplicit(VisageSequenceExplicit tree);
    public void visitSequenceIndexed(VisageSequenceIndexed tree);
    public void visitSequenceSlice(VisageSequenceSlice tree);
    public void visitSequenceInsert(VisageSequenceInsert tree);
    public void visitSequenceDelete(VisageSequenceDelete tree);
    public void visitInvalidate(VisageInvalidate tree);
    public void visitForExpression(VisageForExpression tree);
    public void visitForExpressionInClause(VisageForExpressionInClause tree);
    public void visitIndexof(VisageIndexof tree);
    public void visitTimeLiteral(VisageTimeLiteral tree);
    public void visitLengthLiteral(VisageLengthLiteral tree);
    public void visitAngleLiteral(VisageAngleLiteral tree);
    public void visitColorLiteral(VisageColorLiteral tree);
    public void visitOverrideClassVar(VisageOverrideClassVar tree);
    public void visitInterpolateValue(VisageInterpolateValue tree);
    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree);
}
