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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.javafx.code.JavafxVarSymbol;

import com.sun.tools.mjavac.util.Name;

/**
 * for (name in seqExpr where whereExpr) bodyExpr
 */
public class JFXForExpressionInClause extends JFXTree implements ForExpressionInClauseTree, JFXBoundMarkable {

    public final JFXVar var;
    public JFXExpression seqExpr;
    private JFXExpression whereExpr;
    private boolean hasWhere = false;
    public Name label;

    private boolean indexUsed;
    private JavafxBindStatus bindStatus = JavafxBindStatus.UNBOUND;

    public JFXVar boundHelper;
    public JavafxVarSymbol indexVarSym;
    public JavafxVarSymbol inductionVarSym;
    public JavafxVarSymbol boundResultVarSym;

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
        this.hasWhere = whereExpr != null;
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

    public boolean hasWhereExpression() {
        return hasWhere;
    }

    public void setWhereExpr(JFXExpression whereExpr) {
        this.whereExpr = whereExpr;
        if (whereExpr != null) {
            this.hasWhere = true;
        }
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

    public void markBound(JavafxBindStatus bindStatus) {
        this.bindStatus = bindStatus;
    }

    public boolean isBound() {
        return bindStatus.isBound();
    }

    public boolean isUnidiBind() {
        return bindStatus.isUnidiBind();
    }

    public boolean isBidiBind() {
        return bindStatus.isBidiBind();
    }
}
