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

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;
import org.visage.tools.code.VisageVarSymbol;

import com.sun.tools.mjavac.util.Name;

/**
 * for (name in seqExpr where whereExpr) bodyExpr
 */
public class VisageForExpressionInClause extends VisageTree implements ForExpressionInClauseTree, VisageBoundMarkable {

    public final VisageVar var;
    public VisageExpression seqExpr;
    private VisageExpression whereExpr;
    private boolean hasWhere = false;
    public Name label;

    private boolean indexUsed;
    private VisageBindStatus bindStatus = VisageBindStatus.UNBOUND;

    public VisageVar boundHelper;
    public VisageVarSymbol indexVarSym;
    public VisageVarSymbol inductionVarSym;
    public VisageVarSymbol boundResultVarSym;

    protected VisageForExpressionInClause() {
        this.var        = null;
        this.seqExpr    = null;
        this.whereExpr  = null;
    }

    protected VisageForExpressionInClause(
            VisageVar var,
            VisageExpression seqExpr,
            VisageExpression whereExpr) {
        this.var = var;
        this.seqExpr = seqExpr;
        this.whereExpr = whereExpr;
        this.hasWhere = whereExpr != null;
    }

    public void accept(VisageVisitor v) {
        v.visitForExpressionInClause(this);
    }

    public VisageVar getVar() {
        return var;
    }

    public VisageVar getVariable() {
        return var;
    }

    public VisageExpression getSequenceExpression() {
        return seqExpr;
    }

    public VisageExpression getWhereExpression() {
        return whereExpr;
    }

    public boolean hasWhereExpression() {
        return hasWhere;
    }

    public void setWhereExpr(VisageExpression whereExpr) {
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
    public VisageTag getVisageTag() {
        return VisageTag.FOR_EXPRESSION_IN_CLAUSE;
    }

    public VisageKind getVisageKind() {
        return VisageKind.FOR_EXPRESSION_IN_CLAUSE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitForExpressionInClause(this, d);
    }

    public void markBound(VisageBindStatus bindStatus) {
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
