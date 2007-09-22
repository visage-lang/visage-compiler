/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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
    public boolean shouldVisitRemoved();
    public boolean shouldVisitSynthetic();
    
    public void visitClassDeclaration(JFXClassDeclaration that);
    public void visitAbstractMember(JFXAbstractMember that);
    public void visitAbstractFunction(JFXAbstractFunction that);
    public void visitAttributeDefinition(JFXAttributeDefinition that);
    public void visitOperationDefinition(JFXOperationDefinition that);
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement that);
    public void visitInitDefinition(JFXInitDefinition that);
    public void visitMemberSelector(JFXMemberSelector that);
    public void visitDoLater(JFXDoLater that);
    public void visitStringExpression(JFXStringExpression that);
    public void visitPureObjectLiteral(JFXPureObjectLiteral that);
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that);
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that);
    public void visitObjectLiteralPart(JFXObjectLiteralPart that);
    public void visitTypeAny(JFXTypeAny that);
    public void visitTypeClass(JFXTypeClass that);
    public void visitTypeFunctional(JFXTypeFunctional that);
    public void visitTypeUnknown(JFXTypeUnknown that);
    public void visitType(JFXType that);
    public void visitVar(JFXVar that);
    public void visitBlockExpression(JFXBlockExpression that);
    public void visitSequenceEmpty(JFXSequenceEmpty that);
    public void visitSequenceRange(JFXSequenceRange that);
    public void visitSequenceExplicit(JFXSequenceExplicit that);
    public void visitSequenceIndexed(JFXSequenceIndexed that);
    public void visitForExpression(JFXForExpression that);
    public void visitForExpressionInClause(JFXForExpressionInClause that);
    public void visitInstanciate(JFXInstanciate that);
}
