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
public class VisageSequenceSlice extends VisageExpression implements SequenceSliceTree {
    private final VisageExpression sequence;
    private final VisageExpression firstIndex;
    private final VisageExpression lastIndex;
    private final int endKind;

    public VisageSequenceSlice(VisageExpression sequence, VisageExpression firstIndex,
            VisageExpression lastIndex, int endKind) {
        this.sequence = sequence;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.endKind = endKind;
    }

    public void accept(VisageVisitor v) {
        v.visitSequenceSlice(this);
    }

    public VisageExpression getSequence() {
        return sequence;
    }
    
    public VisageExpression getFirstIndex() {
        return firstIndex;
    }
    
    public VisageExpression getLastIndex() {
        return lastIndex;
    }

    public int getEndKind () {
        return endKind;
    }

    @Override
    public VisageTag getFXTag() {
        return VisageTag.SEQUENCE_SLICE;
    }

    public VisageKind getJavaFXKind() {
        return VisageKind.SEQUENCE_SLICE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceSlice(this, data);
    }
}

