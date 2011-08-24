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

/**
 * "if {cond} truePart [else falsePart]" conditional expression
 */
public class VisageIfExpression extends VisageExpression implements ConditionalExpressionTree {

    public final VisageExpression cond;
    public final VisageExpression truepart;
    public final VisageExpression falsepart;

    public VisageVar boundCondVar;
    public VisageVar boundThenVar;
    public VisageVar boundElseVar;
    public VisageVar boundSizeVar;

    protected VisageIfExpression(VisageExpression cond,
            VisageExpression truepart,
            VisageExpression falsepart) {
        this.cond = cond;
        this.truepart = truepart;
        this.falsepart = falsepart;
    }

    @Override
    public void accept(VisageVisitor v) {
        v.visitIfExpression(this);
    }

    public VisageKind getVisageKind() {
        return VisageKind.CONDITIONAL_EXPRESSION;
    }

    public VisageExpression getCondition() {
        return cond;
    }

    public VisageExpression getTrueExpression() {
        return truepart;
    }

    public VisageExpression getFalseExpression() {
        return falsepart;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitConditionalExpression(this, d);
    }

    @Override
    public VisageTag getFXTag() {
        return VisageTag.CONDEXPR;
    }
}

