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
 * An assignment with "+=", "|=" ...
 */
public class VisageAssignOp extends VisageExpression implements CompoundAssignmentTree {

    private JavafxTag opcode;
    public VisageExpression lhs;
    public VisageExpression rhs;
    public Symbol operator;

    protected VisageAssignOp(JavafxTag opcode, VisageExpression lhs, VisageExpression rhs, Symbol operator) {
        this.opcode = opcode;
        this.lhs = (VisageExpression) lhs;
        this.rhs = (VisageExpression) rhs;
        this.operator = operator;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitAssignop(this);
    }

    public VisageKind getJavaFXKind() {
        return JavafxTreeInfo.tagToKind(getFXTag());
    }

    public VisageExpression getVariable() {
        return lhs;
    }

    public VisageExpression getExpression() {
        return rhs;
    }

    public Symbol getOperator() {
        return operator;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitCompoundAssignment(this, d);
    }

    @Override
    public JavafxTag getFXTag() {
        return opcode;
    }

    public JavafxTag getNormalOperatorFXTag() {
        switch (opcode) {
            case PLUS_ASG:
                return JavafxTag.PLUS;
            case MINUS_ASG:
                return JavafxTag.MINUS;
            case MUL_ASG:
                return JavafxTag.MUL;
            case DIV_ASG:
                return JavafxTag.DIV;
            default:
                throw new RuntimeException("bad assign op tag: " + opcode);
        }
    }

    public int getOperatorTag() {
        return opcode.asOperatorTag();
    }
}
