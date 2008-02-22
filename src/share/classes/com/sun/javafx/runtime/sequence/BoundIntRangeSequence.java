/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.IntChangeListener;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundIntRangeSequence
 *
 * @author Brian Goetz
 */
public class BoundIntRangeSequence extends AbstractBoundSequence<Integer> implements SequenceLocation<Integer> {

    private final IntLocation lowerLoc, upperLoc;
    private int lower, upper;
    private int size;

    public BoundIntRangeSequence(IntLocation lowerLoc, IntLocation upperLoc) {
        super(Integer.class);
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
    }

    protected Sequence<Integer> computeValue() {
        computeBounds(lowerLoc.get(), upperLoc.get());
        return Sequences.range(lower, upper);
    }

    private void computeBounds(int newLower, int newUpper) {
        lower = newLower;
        upper = newUpper;
        size = (upper >= lower) ? upper - lower + 1 : 0;
    }

    protected void initialize() {
        lowerLoc.addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                int oldSize = size;
                computeBounds(newValue, upper);
                if (oldSize == 0)
                    updateSlice(0, -1, Sequences.range(lower, upper));
                else if (oldSize < size)
                    updateSlice(0, -1, Sequences.range(lower, lower+size-oldSize-1));
                else if (oldSize > size)
                    updateSlice(0, oldSize-size-1, Sequences.emptySequence(Integer.class));
            }
        });
        upperLoc.addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                int oldSize = size;
                computeBounds(lower, newValue);
                if (oldSize == 0)
                    updateSlice(0, -1, Sequences.range(lower, upper));
                else if (oldSize < size)
                    updateSlice(oldSize, oldSize-1, Sequences.range(upper-(size-oldSize-1), upper));
                else if (oldSize > size)
                    updateSlice(size, oldSize-1, Sequences.emptySequence(Integer.class));
            }
        });
    }
}
