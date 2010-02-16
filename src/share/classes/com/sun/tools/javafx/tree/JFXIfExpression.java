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

/**
 * "if {cond} truePart [else falsePart]" conditional expression
 */
public class JFXIfExpression extends JFXExpression implements ConditionalExpressionTree {

    public final JFXExpression cond;
    public final JFXExpression truepart;
    public final JFXExpression falsepart;

    public JFXVar boundCondVar;
    public JFXVar boundThenVar;
    public JFXVar boundElseVar;
    public JFXVar boundSizeVar;

    protected JFXIfExpression(JFXExpression cond,
            JFXExpression truepart,
            JFXExpression falsepart) {
        this.cond = cond;
        this.truepart = truepart;
        this.falsepart = falsepart;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitIfExpression(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.CONDITIONAL_EXPRESSION;
    }

    public JFXExpression getCondition() {
        return cond;
    }

    public JFXExpression getTrueExpression() {
        return truepart;
    }

    public JFXExpression getFalseExpression() {
        return falsepart;
    }

    //@Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitConditionalExpression(this, d);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.CONDEXPR;
    }
}

