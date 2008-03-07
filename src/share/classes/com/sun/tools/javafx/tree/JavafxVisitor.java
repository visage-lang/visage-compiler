/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/** The visitor interface for JavaFX trees.
 */
public interface JavafxVisitor extends JCVisitor {
    public void visitBindExpression(JFXBindExpression tree);
    public void visitClassDeclaration(JFXClassDeclaration tree);
    public void visitOperationDefinition(JFXFunctionDefinition tree);
    public void visitInitDefinition(JFXInitDefinition tree);
    public void visitPostInitDefinition(JFXPostInitDefinition tree);
    public void visitMemberSelector(JFXMemberSelector tree);
    public void visitDoLater(JFXDoLater tree);
    public void visitStringExpression(JFXStringExpression tree);
    public void visitInstanciate(JFXInstanciate tree);
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized tree);
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree);
    public void visitTypeAny(JFXTypeAny tree);
    public void visitTypeClass(JFXTypeClass tree);
    public void visitTypeFunctional(JFXTypeFunctional tree);
    public void visitTypeUnknown(JFXTypeUnknown tree);
    public void visitVar(JFXVar tree);
    public void visitOnReplace(JFXOnReplace tree);
    public void visitOnReplaceElement(JFXOnReplaceElement tree);
    public void visitOnInsertElement(JFXOnInsertElement tree);
    public void visitOnDeleteElement(JFXOnDeleteElement tree);
    public void visitOnDeleteAll(JFXOnDeleteAll tree);
    public void visitBlockExpression(JFXBlockExpression tree);
    public void visitOperationValue(JFXFunctionValue tree);
    public void visitSequenceEmpty(JFXSequenceEmpty tree);
    public void visitSequenceRange(JFXSequenceRange tree);
    public void visitSequenceExplicit(JFXSequenceExplicit tree);
    public void visitSequenceIndexed(JFXSequenceIndexed tree);
    public void visitSequenceSlice(JFXSequenceSlice tree);
    public void visitSequenceInsert(JFXSequenceInsert tree);
    public void visitSequenceDelete(JFXSequenceDelete tree);
    public void visitForExpression(JFXForExpression tree);
    public void visitForExpressionInClause(JFXForExpressionInClause tree);
    public void visitIndexof(JFXIndexof tree);
    public void visitTimeLiteral(JFXTimeLiteral tree);
    public void visitOverrideAttribute(JFXOverrideAttribute tree);
    public void visitInterpolate(JFXInterpolate tree);
    public void visitInterpolateValue(JFXInterpolateValue tree);
}
