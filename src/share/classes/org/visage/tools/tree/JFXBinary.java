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

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.code.Symbol;

/**
 * A binary operation.
 */
public class VisageBinary extends VisageExpression implements BinaryTree {

    private JavafxTag opcode;
    public VisageExpression lhs;
    public VisageExpression rhs;
    public Symbol operator;

    protected VisageBinary(JavafxTag opcode,
            VisageExpression lhs,
            VisageExpression rhs,
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

    public VisageKind getJavaFXKind() {
        return JavafxTreeInfo.tagToKind(getFXTag());
    }

    public VisageExpression getLeftOperand() {
        return lhs;
    }

    public VisageExpression getRightOperand() {
        return rhs;
    }

    public Symbol getOperator() {
        return operator;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
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
