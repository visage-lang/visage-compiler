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
 *
 * @author Robert Field
 */
public class VisageSequenceRange extends VisageAbstractSequenceCreator implements SequenceRangeTree {
    private final VisageExpression lower;
    private final VisageExpression upper;
    private final VisageExpression stepOrNull;
    private final boolean exclusive;

    public VisageVar boundSizeVar;

    public VisageSequenceRange(VisageExpression lower, VisageExpression upper, VisageExpression stepOrNull, boolean exclusive) {
        this.lower = lower;
        this.upper = upper;
        this.stepOrNull = stepOrNull;
        this.exclusive = exclusive;
    }

    public void accept(VisageVisitor v) {
        v.visitSequenceRange(this);
    }

    public VisageExpression getLower() {
        return lower;
    }
    
    public VisageExpression getUpper() {
        return upper;
    }
    
    public VisageExpression getStepOrNull() {
        return stepOrNull;
    }
    
    public boolean isExclusive() {
        return exclusive;
    }
    
    @Override
    public VisageTag getVisageTag() {
        return VisageTag.SEQUENCE_RANGE;
    }

    public VisageKind getVisageKind() {
        return VisageKind.SEQUENCE_RANGE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceRange(this, data);
    }
}
