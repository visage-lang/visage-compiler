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

package com.sun.tools.javafx.comp;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javafx.tree.JFXBlockExpression;

/**
 * Remove symbol and type information.
 * 
 * Already converted to JCTree nodes
 * 
 * @author Robert Field
 */
public class JavafxPrepForBackEnd extends TreeScanner {
    
    protected static final Context.Key<JavafxPrepForBackEnd> prepForBackEndKey =
        new Context.Key<JavafxPrepForBackEnd>();

    public static JavafxPrepForBackEnd instance(Context context) {
        JavafxPrepForBackEnd instance = context.get(prepForBackEndKey);
        if (instance == null)
            instance = new JavafxPrepForBackEnd(context);
        return instance;
    }

    JavafxPrepForBackEnd(Context context) { 
    }
    
    public void prep(JavafxEnv<JavafxAttrContext> attrEnv) {
        scan(attrEnv.toplevel);
    }

    @Override
    public void visitTopLevel(JCCompilationUnit that) {
        super.visitTopLevel(that);
        that.type = null;
        that.packge = null;
        that.starImportScope = null;
        that.namedImportScope = null;
    }
    
    @Override
    public void visitImport(JCImport that) {
        super.visitImport(that);
        that.type = null;
    }
    
    @Override
    public void visitClassDef(JCClassDecl that) {
        super.visitClassDef(that);
        that.type = null;
        that.sym = null;
    }
    
    @Override
    public void visitMethodDef(JCMethodDecl that) {
        super.visitMethodDef(that);
        that.type = null;
        that.sym = null;
    }
    
    @Override
    public void visitVarDef(JCVariableDecl that) {
        super.visitVarDef(that);
        that.type = null;
        that.sym = null;
    }
    
    @Override
    public void visitSkip(JCSkip that) {
        super.visitSkip(that);
        that.type = null;
    }
    
    @Override
    public void visitBlock(JCBlock that) {
        super.visitBlock(that);
        that.type = null;
    }
    
    @Override
    public void visitDoLoop(JCDoWhileLoop that) {
        super.visitDoLoop(that);
        that.type = null;
    }
    
    @Override
    public void visitWhileLoop(JCWhileLoop that) {
        super.visitWhileLoop(that);
        that.type = null;
    }
    
    @Override
    public void visitForLoop(JCForLoop that) {
        super.visitForLoop(that);
        that.type = null;
    }
    
    @Override
    public void visitForeachLoop(JCEnhancedForLoop that) {
        super.visitForeachLoop(that);
        that.type = null;
    }
    
    @Override
    public void visitLabelled(JCLabeledStatement that) {
        super.visitLabelled(that);
        that.type = null;
    }
    
    @Override
    public void visitSwitch(JCSwitch that) {
        super.visitSwitch(that);
        that.type = null;
    }
    
    @Override
    public void visitCase(JCCase that) {
        super.visitCase(that);
        that.type = null;
    }
    
    @Override
    public void visitSynchronized(JCSynchronized that) {
         super.visitSynchronized(that);
        that.type = null;
    }
    
    @Override
    public void visitTry(JCTry that) {
        super.visitTry(that);
        that.type = null;
    }
    
    @Override
    public void visitCatch(JCCatch that) {
         super.visitCatch(that);
        that.type = null;
    }
    
    @Override
    public void visitConditional(JCConditional that) {
        super.visitConditional(that);
        that.type = null;
    }
    
    @Override
    public void visitIf(JCIf that) {
        super.visitIf(that);
        that.type = null;
    }
    
    @Override
    public void visitExec(JCExpressionStatement that) {
         super.visitExec(that);
        that.type = null;
    }
    
    @Override
    public void visitBreak(JCBreak that) {
         super.visitBreak(that);
        that.type = null;
    }
    
    @Override
    public void visitContinue(JCContinue that) {
        super.visitContinue(that);
        that.type = null;
    }
    
    @Override
    public void visitReturn(JCReturn that) {
        super.visitReturn(that);
        that.type = null;
    }
    
    @Override
    public void visitThrow(JCThrow that) {
        super.visitThrow(that);
        that.type = null;
    }
    
    @Override
    public void visitAssert(JCAssert that) {
        super.visitAssert(that);
        that.type = null;
    }
    
    @Override
    public void visitApply(JCMethodInvocation that) {
        super.visitApply(that);
        that.type = null;
    }
    
    @Override
    public void visitNewClass(JCNewClass that) {
        super.visitNewClass(that);
        that.type = null;
        that.constructor = null;
    }
    
    @Override
    public void visitNewArray(JCNewArray that) {
         super.visitNewArray(that);
        that.type = null;
    }
    
    @Override
    public void visitParens(JCParens that) {
        super.visitParens(that);
        that.type = null;
    }
    
    @Override
    public void visitAssign(JCAssign that) {
        super.visitAssign(that);
        that.type = null;
    }
    
    @Override
    public void visitAssignop(JCAssignOp that) {
        super.visitAssignop(that);
        that.type = null;
        that.operator = null;
    }
    
    @Override
    public void visitUnary(JCUnary that) {
        super.visitUnary(that);
        that.type = null;
        that.operator = null;
    }
    
    @Override
    public void visitBinary(JCBinary that) {
        super.visitBinary(that);
        that.type = null;
        that.operator = null;
    }
    
    @Override
    public void visitTypeCast(JCTypeCast that) {
        super.visitTypeCast(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeTest(JCInstanceOf that) {
        super.visitTypeTest(that);
        that.type = null;
    }
    
    @Override
    public void visitIndexed(JCArrayAccess that) {
         super.visitIndexed(that);
        that.type = null;
    }
    
    @Override
    public void visitSelect(JCFieldAccess that) {
        super.visitSelect(that);
        that.type = null;
        that.sym = null;
    }
    
    @Override
    public void visitIdent(JCIdent that) {
        super.visitIdent(that);
        that.type = null;
        that.sym = null;
    }
    
    @Override
    public void visitLiteral(JCLiteral that) {
        super.visitLiteral(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeIdent(JCPrimitiveTypeTree that) {
        super.visitTypeIdent(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeArray(JCArrayTypeTree that) {
        super.visitTypeArray(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeApply(JCTypeApply that) {
        super.visitTypeApply(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeParameter(JCTypeParameter that) {
        super.visitTypeParameter(that);
        that.type = null;
    }
    
    @Override
    public void visitWildcard(JCWildcard that) {
        super.visitWildcard(that);
        that.type = null;
    }
    
    @Override
    public void visitTypeBoundKind(TypeBoundKind that) {
         super.visitTypeBoundKind(that);
        that.type = null;
    }
    
    @Override
    public void visitAnnotation(JCAnnotation that) {
        super.visitAnnotation(that);
        that.type = null;
    }
    
    @Override
    public void visitModifiers(JCModifiers that) {
        super.visitModifiers(that);
        that.type = null;
    }
    
    @Override
    public void visitErroneous(JCErroneous that) {
        super.visitErroneous(that);
        that.type = null;
    }
    
    @Override
    public void visitLetExpr(LetExpr that) {
        super.visitLetExpr(that);
        that.type = null;
    }

    public void visitBlockExpression(JFXBlockExpression that) {
        scan(that.stats);
        scan(that.value);
    }
}
