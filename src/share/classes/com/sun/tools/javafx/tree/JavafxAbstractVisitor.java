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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.List;

/** The visitor interface for JavaFX trees.
 */
public abstract class JavafxAbstractVisitor extends TreeScanner implements JavafxVisitor {
    private JCTree.Visitor javacVisitor = null;
    public boolean shouldVisitRemoved;
    public boolean shouldVisitSynthetic;
    
    public JavafxAbstractVisitor() {
        this.javacVisitor = null;
    }
    
    public JavafxAbstractVisitor(JCTree.Visitor javacVisitor) {
        this.javacVisitor = javacVisitor;
        this.shouldVisitRemoved = false;
        this.shouldVisitSynthetic = true;
    }
    
    public void visitTopLevel(JCCompilationUnit that) {
        if (javacVisitor != null) {
            javacVisitor.visitTopLevel(that);
        }
        else {
            super.visitTopLevel(that);
        }
    }
    
    public void visitImport(JCImport that) {
        if (javacVisitor != null) {
            javacVisitor.visitImport(that);
        }
        else {
            super.visitImport(that);
        }
    }
    
    public void visitClassDef(JCClassDecl that) {
        if (javacVisitor != null) {
            javacVisitor.visitClassDef(that);
        }
        else {
            super.visitClassDef(that);
        }
    }
    
    public void visitMethodDef(JCMethodDecl that) {
        if (javacVisitor != null) {
            javacVisitor.visitMethodDef(that);
        }
        else {
            super.visitMethodDef(that);
        }
    }
    
    public void visitVarDef(JCVariableDecl that) {
        if (javacVisitor != null) {
            javacVisitor.visitVarDef(that);
        }
        else {
            super.visitVarDef(that);
        }
    }
    
    public void visitSkip(JCSkip that) {
        if (javacVisitor != null) {
            javacVisitor.visitSkip(that);
        }
        else {
            super.visitSkip(that);
        }
    }
    
    public void visitBlock(JCBlock that) {
        if (javacVisitor != null) {
            javacVisitor.visitBlock(that);
        }
        else {
            super.visitBlock(that);
        }
    }
    
    public void visitDoLoop(JCDoWhileLoop that) {
        if (javacVisitor != null) {
            javacVisitor.visitDoLoop(that);
        }
        else {
            super.visitDoLoop(that);
        }
    }
    
    public void visitWhileLoop(JCWhileLoop that) {
        if (javacVisitor != null) {
            javacVisitor.visitWhileLoop(that);
        }
        else {
            super.visitWhileLoop(that);
        }
    }
    
    public void visitForLoop(JCForLoop that) {
        if (javacVisitor != null) {
            javacVisitor.visitForLoop(that);
        }
        else {
            super.visitForLoop(that);
        }
    }
    
    public void visitForeachLoop(JCEnhancedForLoop that) {
        if (javacVisitor != null) {
            javacVisitor.visitForeachLoop(that);
        }
        else {
            super.visitForeachLoop(that);
        }
    }
    
    public void visitLabelled(JCLabeledStatement that) {
        if (javacVisitor != null) {
            javacVisitor.visitLabelled(that);
        }
        else {
            super.visitLabelled(that);
        }
    }
    
    public void visitSwitch(JCSwitch that) {
        if (javacVisitor != null) {
            javacVisitor.visitSwitch(that);
        }
        else {
            super.visitSwitch(that);
        }
    }
    
    public void visitCase(JCCase that) {
        if (javacVisitor != null) {
            javacVisitor.visitCase(that);
        }
        else {
            super.visitCase(that);
        }
    }
    
    public void visitSynchronized(JCSynchronized that) {
        if (javacVisitor != null) {
            javacVisitor.visitSynchronized(that);
        }
        else {
            super.visitSynchronized(that);
        }
    }
    
    public void visitTry(JCTry that) {
        if (javacVisitor != null) {
            javacVisitor.visitTry(that);
        }
        else {
            super.visitTry(that);
        }
    }
    
    public void visitCatch(JCCatch that) {
        if (javacVisitor != null) {
            javacVisitor.visitCatch(that);
        }
        else {
            super.visitCatch(that);
        }
    }
    
    public void visitConditional(JCConditional that) {
        if (javacVisitor != null) {
            javacVisitor.visitConditional(that);
        }
        else {
            super.visitConditional(that);
        }
    }
    
    public void visitIf(JCIf that) {
        if (javacVisitor != null) {
            javacVisitor.visitIf(that);
        }
        else {
            super.visitIf(that);
        }
    }
    
    public void visitExec(JCExpressionStatement that) {
        if (javacVisitor != null) {
            javacVisitor.visitExec(that);
        }
        else {
            super.visitExec(that);
        }
    }
    
    public void visitBreak(JCBreak that) {
        if (javacVisitor != null) {
            javacVisitor.visitBreak(that);
        }
        else {
            super.visitBreak(that);
        }
    }
    
    public void visitContinue(JCContinue that) {
        if (javacVisitor != null) {
            javacVisitor.visitContinue(that);
        }
        else {
            super.visitContinue(that);
        }
    }
    
    public void visitReturn(JCReturn that) {
        if (javacVisitor != null) {
            javacVisitor.visitReturn(that);
        }
        else {
            super.visitReturn(that);
        }
    }
    
    public void visitThrow(JCThrow that) {
        if (javacVisitor != null) {
            javacVisitor.visitThrow(that);
        }
        else {
            super.visitThrow(that);
        }
    }
    
    public void visitAssert(JCAssert that) {
        if (javacVisitor != null) {
            javacVisitor.visitAssert(that);
        }
        else {
            super.visitAssert(that);
        }
    }
    
    public void visitApply(JCMethodInvocation that) {
        if (javacVisitor != null) {
            javacVisitor.visitApply(that);
        }
        else {
            super.visitApply(that);
        }
    }
    
    public void visitNewClass(JCNewClass that) {
        if (javacVisitor != null) {
            javacVisitor.visitNewClass(that);
        }
        else {
            super.visitNewClass(that);
        }
    }
    
    public void visitNewArray(JCNewArray that) {
        if (javacVisitor != null) {
            javacVisitor.visitNewArray(that);
        }
        else {
            super.visitNewArray(that);
        }
    }
    
    public void visitParens(JCParens that) {
        if (javacVisitor != null) {
            javacVisitor.visitParens(that);
        }
        else {
            super.visitParens(that);
        }
    }
    
    public void visitAssign(JCAssign that) {
        if (javacVisitor != null) {
            javacVisitor.visitAssign(that);
        }
        else {
            super.visitAssign(that);
        }
    }
    
    public void visitAssignop(JCAssignOp that) {
        if (javacVisitor != null) {
            javacVisitor.visitAssignop(that);
        }
        else {
            super.visitAssignop(that);
        }
    }
    
    public void visitUnary(JCUnary that) {
        if (javacVisitor != null) {
            javacVisitor.visitUnary(that);
        }
        else {
            super.visitUnary(that);
        }
    }
    
    public void visitBinary(JCBinary that) {
        if (javacVisitor != null) {
            javacVisitor.visitBinary(that);
        }
        else {
            super.visitBinary(that);
        }
    }
    
    public void visitTypeCast(JCTypeCast that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeCast(that);
        }
        else {
            super.visitTypeCast(that);
        }
    }
    
    public void visitTypeTest(JCInstanceOf that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeTest(that);
        }
        else {
            super.visitTypeTest(that);
        }
    }
    
    public void visitIndexed(JCArrayAccess that) {
        if (javacVisitor != null) {
            javacVisitor.visitIndexed(that);
        }
        else {
            super.visitIndexed(that);
        }
    }
    
    public void visitSelect(JCFieldAccess that) {
        if (javacVisitor != null) {
            javacVisitor.visitSelect(that);
        }
        else {
            super.visitSelect(that);
        }
    }
    
    public void visitIdent(JCIdent that) {
        if (javacVisitor != null) {
            javacVisitor.visitIdent(that);
        }
        else {
            super.visitIdent(that);
        }
    }
    
    public void visitLiteral(JCLiteral that) {
        if (javacVisitor != null) {
            javacVisitor.visitLiteral(that);
        }
        else {
            super.visitLiteral(that);
        }
    }
    
    public void visitTypeIdent(JCPrimitiveTypeTree that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeIdent(that);
        }
        else {
            super.visitTypeIdent(that);
        }
    }
    
    public void visitTypeArray(JCArrayTypeTree that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeArray(that);
        }
        else {
            super.visitTypeArray(that);
        }
    }
    
    public void visitTypeApply(JCTypeApply that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeApply(that);
        }
        else {
            super.visitTypeApply(that);
        }
    }
    
    public void visitTypeParameter(JCTypeParameter that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeParameter(that);
        }
        else {
            super.visitTypeParameter(that);
        }
    }
    
    public void visitWildcard(JCWildcard that) {
        if (javacVisitor != null) {
            javacVisitor.visitWildcard(that);
        }
        else {
            super.visitWildcard(that);
        }
    }
    
    public void visitTypeBoundKind(TypeBoundKind that) {
        if (javacVisitor != null) {
            javacVisitor.visitTypeBoundKind(that);
        }
        else {
            super.visitTypeBoundKind(that);
        }
    }
    
    public void visitAnnotation(JCAnnotation that) {
        if (javacVisitor != null) {
            javacVisitor.visitAnnotation(that);
        }
        else {
            super.visitAnnotation(that);
        }
    }
    
    public void visitModifiers(JCModifiers that) {
        if (javacVisitor != null) {
            javacVisitor.visitModifiers(that);
        }
        else {
            super.visitModifiers(that);
        }
    }
    
    public void visitErroneous(JCErroneous that) {
        if (javacVisitor != null) {
            javacVisitor.visitErroneous(that);
        }
        else {
            super.visitErroneous(that);
        }
    }
    
    public void visitLetExpr(LetExpr that) {
        if (javacVisitor != null) {
            javacVisitor.visitLetExpr(that);
        }
        else {
            super.visitLetExpr(that);
        }
    }
    
    // Begin JavaFX trees
    public void visitClassDeclaration(JFXClassDeclaration that) {
        that.mods.accept(this);
        for (JCTree members : that.getDeclaredMembers()) {
            members.accept(this);
        }
    }
    
    public void visitAttributeDeclaration(JFXRetroAttributeDeclaration that) {
        visitMemberDeclaration(that);
        if (that.getInverse() != null) {
            that.getInverse().accept((JavafxVisitor)this);
        }
        
        if (that.getOrdering() != null) {
            that.getOrdering().accept(this);
        }
    }
    
    public void visitFunctionDeclaration(JFXRetroFunctionMemberDeclaration that) {
        visitFuncOpDeclaration(that);
    }
    
    public void visitOperationDeclaration(JFXRetroOperationMemberDeclaration that) {
        visitFuncOpDeclaration(that);
    }
    
    public void visitFuncOpDeclaration(JFXAbstractFunction that) {
        visitMemberDeclaration(that);
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
    }
    
    public void visitMemberDeclaration(JFXAbstractMember that) {
        that.modifiers.accept(this);
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
    }
    
    public void visitRetroAttributeDefinition(JFXRetroAttributeDefinition that) {
        visitMemberDefinition(that);
        if (that.getInitializer() != null) {
            that.getInitializer().accept(this);
        }
    }
    
    public void visitRetroFunctionDefinition(JFXRetroFunctionMemberDefinition that) {
        visitFuncOpDefinition(that);
    }
    
    public void visitRetroOperationDefinition(JFXRetroOperationMemberDefinition that) {
        visitFuncOpDefinition(that);
    }
    
    public void visitFuncOpDefinition(JFXRetroFuncOpMemberDefinition that) {
        visitMemberDefinition(that);
        
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.body.accept(this);
    }
    
    public void visitMemberDefinition(JFXRetroMemberDefinition that) {
        that.getSelector().accept((JavafxVisitor)this);
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
    }
    
    public void visitRetroOperationLocalDefinition(JFXRetroOperationLocalDefinition that) {
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
        
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getBody().accept(this);
    }
    
    public void visitRetroFunctionLocalDefinition(JFXRetroFunctionLocalDefinition that) {
        if (that.getType() != null) {
            that.getType().accept((JavafxVisitor)this);
        }
        
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getBody().accept(this);
    }
    
    public void visitDoLater(JFXDoLater that) {
        that.getBody().accept(this);
    }

    public void visitTriggerOnInsert(JFXTriggerOnInsert that) {
        that.getSelector().accept((JavafxVisitor)this);
        that.getIdentifier().accept(this);
        that.getBlock().accept(this);        
    }    

    public void visitTriggerOnDelete(JFXTriggerOnDelete that) {
        that.getSelector().accept((JavafxVisitor)this);
        that.getIdentifier().accept(this);
        that.getBlock().accept(this);        
    }
    
    public void visitTriggerOnDeleteElement(JFXTriggerOnDeleteElement that) {
        that.getSelector().accept((JavafxVisitor)this);
        that.getIdentifier().accept(this);
        that.getBlock().accept(this);        
    }
    
    public void visitTriggerOnNew(JFXTriggerOnNew that) {
        that.getClassIdentifier().accept(this);

        if (that.getNewValueIdentifier() != null) {
            that.getNewValueIdentifier().accept(this);
        }
        that.getBlock().accept(this);        
    }
    
    public void visitTriggerOnReplace(JFXTriggerOnReplace that) {
        that.getSelector().accept((JavafxVisitor)this);
        that.getNewValueIdentifier().accept(this);
        that.getBlock().accept(this);        
    }
    
    public void visitTriggerOnReplaceElement(JFXTriggerOnReplaceElement that) {
        that.getSelector().accept((JavafxVisitor)this);
        that.getElementIdentifier().accept(this);
        that.getNewValueIdentifier().accept(this);
        that.getBlock().accept(this);        
    }
    
    public void visitMemberSelector(JFXMemberSelector that) {
    }
    
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
    
    public void visitPureObjectLiteral(JFXPureObjectLiteral that) {
        that.getIdentifier().accept(this);
        for (JFXStatement part : that.getParts()) {
            part.accept((JavafxVisitor)this);
        }
    }
    
    public void visitVarIsObjectBeingInitialized(JFXVarIsObjectBeingInitialized that) {
        visitVar(that);
    }
    
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
    }
    
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.getExpression().accept(this);
    }  
    
    public void visitTypeAny(JFXTypeAny that) {
        visitType(that);
    }
    
    public void visitTypeClass(JFXTypeClass that) {
        visitType(that);
    }
    
    public void visitTypeFunctional(JFXTypeFunctional that) {
        for (JCTree param : that.getParameters()) {
            param.accept(this);
        }
        that.getReturnType().accept((JavafxVisitor)this);
        visitType(that);
    }
    
    public void visitTypeUnknown(JFXTypeUnknown that) {
        visitType(that);
    }
    
    public void visitType(JFXType that) {
    }
    
    public void visitVarInit(JFXVarInit that) {
        visitVarStatement(that);
    }
    
    public void visitVarStatement(JFXVarStatement that) {
        visitVar(that);
    }
    
    public void visitVar(JFXVar that) {
        visitType(that.getType());
    }
    
    public boolean shouldVisitRemoved() {
        return shouldVisitRemoved;
    }
    
    public boolean shouldVisitSynthetic() {
        return shouldVisitSynthetic;
    }
    
    public void visitBlockExpression(JFXBlockExpression that) {
        scan(that.stats);
        scan(that.value);
    }

    public void visitTree(JCTree that) {
        assert false : "Should not be here!!!";
    }
}
