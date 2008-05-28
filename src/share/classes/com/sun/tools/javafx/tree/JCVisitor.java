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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

/** The visitor interface that Java JCTrees would have if it were defined.
 * JCTree.Visitor would be defined --
 *     public static abstract class Visitor implements JCVisitor {
 */
public interface JCVisitor {
        public void visitTopLevel(JCCompilationUnit that);
        public void visitImport(JCImport that);
        public void visitClassDef(JCClassDecl that);
        public void visitMethodDef(JCMethodDecl that);
        public void visitVarDef(JCVariableDecl that);
        public void visitSkip(JCSkip that);
        public void visitBlock(JCBlock that);
        public void visitDoLoop(JCDoWhileLoop that);
        public void visitWhileLoop(JCWhileLoop that);
        public void visitForLoop(JCForLoop that);
        public void visitForeachLoop(JCEnhancedForLoop that);
        public void visitLabelled(JCLabeledStatement that);
        public void visitSwitch(JCSwitch that);
        public void visitCase(JCCase that);
        public void visitSynchronized(JCSynchronized that);
        public void visitTry(JCTry that);
        public void visitCatch(JCCatch that);
        public void visitConditional(JCConditional that);
        public void visitIf(JCIf that);
        public void visitExec(JCExpressionStatement that);
        public void visitBreak(JCBreak that);
        public void visitContinue(JCContinue that);
        public void visitReturn(JCReturn that);
        public void visitThrow(JCThrow that);
        public void visitAssert(JCAssert that);
        public void visitApply(JCMethodInvocation that);
        public void visitNewClass(JCNewClass that);
        public void visitNewArray(JCNewArray that);
        public void visitParens(JCParens that);
        public void visitAssign(JCAssign that);
        public void visitAssignop(JCAssignOp that);
        public void visitUnary(JCUnary that);
        public void visitBinary(JCBinary that);
        public void visitTypeCast(JCTypeCast that);
        public void visitTypeTest(JCInstanceOf that);
        public void visitIndexed(JCArrayAccess that);
        public void visitSelect(JCFieldAccess that);
        public void visitIdent(JCIdent that);
        public void visitLiteral(JCLiteral that);
        public void visitTypeIdent(JCPrimitiveTypeTree that);
        public void visitTypeArray(JCArrayTypeTree that);
        public void visitTypeApply(JCTypeApply that);
        public void visitTypeParameter(JCTypeParameter that);
        public void visitWildcard(JCWildcard that);
        public void visitTypeBoundKind(TypeBoundKind that);
        public void visitAnnotation(JCAnnotation that);
        public void visitModifiers(JCModifiers that);
        public void visitErroneous(JCErroneous that);
        public void visitLetExpr(LetExpr that);

        public void visitTree(JCTree that);
    
}
