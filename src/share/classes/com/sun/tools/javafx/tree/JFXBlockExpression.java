/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.Position;

/**
 *
 * @author bothner
 */
public class JFXBlockExpression extends JFXExpression implements BlockExpressionTree {

    public long flags;
    public List<JFXExpression> stats;
    public JFXExpression value;
    /** Position of closing brace, optional. */
    public int endpos = Position.NOPOS;

    protected JFXBlockExpression(long flags, List<JFXExpression> stats, JFXExpression value) {
        this.stats = stats;
        this.flags = flags;
        this.value = value;
    }

    public java.util.List<ExpressionTree> getStatements() {
        return convertList(ExpressionTree.class, stats);
    }

    public List<JFXExpression> getStmts() {
        return stats;
    }

   public JFXExpression getValue() {
        return value;
    }

    public boolean isStatic() {
        return (flags & Flags.STATIC) != 0;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.BLOCK_EXPRESSION;
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitBlockExpression(this, d);
    }

    public void accept(JavafxVisitor v) {
        v.visitBlockExpression(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.BLOCK_EXPRESSION;
    }
}
