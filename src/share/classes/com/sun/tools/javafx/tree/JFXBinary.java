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

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.tools.mjavac.code.Symbol;

/**
 * A binary operation.
 */
public class JFXBinary extends JFXExpression implements BinaryTree {

    private JavafxTag opcode;
    public JFXExpression lhs;
    public JFXExpression rhs;
    public Symbol operator;

    protected JFXBinary(JavafxTag opcode,
            JFXExpression lhs,
            JFXExpression rhs,
            Symbol operator) {
        this.opcode = opcode;
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitBinary(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavafxTreeInfo.tagToKind(getFXTag());
    }

    public JFXExpression getLeftOperand() {
        return lhs;
    }

    public JFXExpression getRightOperand() {
        return rhs;
    }

    public Symbol getOperator() {
        return operator;
    }

    //@Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitBinary(this, d);
    }

    @Override
    public JavafxTag getFXTag() {
        return opcode;
    }
    
    public int getOperatorTag() {
        return opcode.asOperatorTag();
    }
}
