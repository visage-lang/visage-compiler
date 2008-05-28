/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import  com.sun.tools.migrator.tree.MTTree.*;

/**
 * for (name in seqExpr where whereExpr) bodyExpr
 */
public class MTForExpressionInClause extends MTTree {
    public MTVar var; 
    public MTExpression seqExpr;
    public MTExpression whereExpr;

    protected MTForExpressionInClause(
            MTVar var, 
            MTExpression seqExpr,
            MTExpression whereExpr) {
        this.var = var;
        this.seqExpr = seqExpr;
        this.whereExpr = whereExpr;
    }
    public void accept(MTVisitor v) { v.visitForExpressionInClause(this); }
    
    public MTVar getVar() { return var; }
    public MTExpression getSequenceExpression() { return seqExpr; }
    public MTExpression getWhereExpression() { return whereExpr; }

    @Override
    public int getTag() {
        return MTTag.FOR_EXPRESSION_IN_CLAUSE;
    }
}
