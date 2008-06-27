/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.JavafxBindStatus;

/**
 *
 * @author Per Bothner
 */
public class JFXBindExpression extends JFXExpression implements BindExpressionTree {

    JFXExpression expr;
    private JavafxBindStatus bindStatus;

    protected JFXBindExpression(JFXExpression expr, JavafxBindStatus bindStatus) {
        this.expr = expr;
        this.bindStatus = bindStatus;
    }

    public JFXExpression getExpression() {
        return expr;
    }

    public JavafxBindStatus getBindStatus() {
        return bindStatus;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.BIND_EXPRESSION;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.BIND_EXPRESSION;
    }

    public void accept(JavafxVisitor v) {
        v.visitBindExpression(this);
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitBindExpression(this, data);
    }
}
