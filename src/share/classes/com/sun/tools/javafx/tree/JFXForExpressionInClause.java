/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
 * for (name in seqExpr where whereExpr) bodyExpr
 */
public class JFXForExpressionInClause extends JFXTree implements ForExpressionInClauseTree {

    public final JFXVar var;
    public final JFXExpression seqExpr;
    public final JFXExpression whereExpr;
    private boolean indexUsed;

    protected JFXForExpressionInClause() {
        this.var        = null;
        this.seqExpr    = null;
        this.whereExpr  = null;
    }

    protected JFXForExpressionInClause(
            JFXVar var,
            JFXExpression seqExpr,
            JFXExpression whereExpr) {
        this.var = var;
        this.seqExpr = seqExpr;
        this.whereExpr = whereExpr;
    }

    public void accept(JavafxVisitor v) {
        v.visitForExpressionInClause(this);
    }

    public JFXVar getVar() {
        return var;
    }

    public JFXVar getVariable() {
        return var;
    }

    public JFXExpression getSequenceExpression() {
        return seqExpr;
    }

    public JFXExpression getWhereExpression() {
        return whereExpr;
    }

    public boolean getIndexUsed() {
        return indexUsed;
    }

    public void setIndexUsed(boolean indexUsed) {
        this.indexUsed = indexUsed;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.FOR_EXPRESSION_IN_CLAUSE;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.FOR_EXPRESSION_IN_CLAUSE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitForExpressionInClause(this, d);
    }
}
