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

import com.sun.tools.mjavac.util.List;

/**
 * for (name in seqExpr where whereExpr) bodyExpr
 */
public class JFXForExpression extends JFXExpression implements ForExpressionTree {

    private final JavaFXKind fxKind;
    public final List<JFXForExpressionInClause> inClauses;
    public final JFXExpression bodyExpr;

    protected JFXForExpression(
            JavaFXKind fxKind,
            List<JFXForExpressionInClause> inClauses,
            JFXExpression bodyExpr) {
        this.fxKind = fxKind;
        this.inClauses = inClauses;
        this.bodyExpr = bodyExpr;
    }

    public void accept(JavafxVisitor v) {
        v.visitForExpression(this);
    }

    public java.util.List<ForExpressionInClauseTree> getInClauses() {
        return JFXTree.convertList(ForExpressionInClauseTree.class, inClauses);
    }

    public List<JFXForExpressionInClause> getForExpressionInClauses() {
        return inClauses;
    }

    public JFXExpression getBodyExpression() {
        return bodyExpr;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.FOR_EXPRESSION;
    }

    public JavaFXKind getJavaFXKind() {
        return fxKind;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitForExpression(this, data);
    }
}
