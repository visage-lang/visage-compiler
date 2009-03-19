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

package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.*;

/**
 * BoundIntRangeSequence
 *
 * @author Brian Goetz
 */
class BoundIntRangeSequence extends AbstractBoundSequence<Integer> implements SequenceLocation<Integer> {

    private final IntLocation lowerLoc, upperLoc, stepLoc;
    private final boolean exclusive;
    private int lower, upper, size, step;

    public BoundIntRangeSequence(boolean lazy, IntLocation lowerLoc, IntLocation upperLoc) {
        this(lazy, lowerLoc, upperLoc, IntConstant.make(1), false);
    }

    public BoundIntRangeSequence(boolean lazy, IntLocation lowerLoc, IntLocation upperLoc, IntLocation stepLoc) {
        this(lazy, lowerLoc, upperLoc, stepLoc, false);
    }

    public BoundIntRangeSequence(boolean lazy, IntLocation lowerLoc, IntLocation upperLoc, boolean exclusive) {
        this(lazy, lowerLoc, upperLoc, IntConstant.make(1), exclusive);
    }

    public BoundIntRangeSequence(boolean lazy, IntLocation lowerLoc, IntLocation upperLoc, IntLocation stepLoc, boolean exclusive) {
        super(lazy, TypeInfo.Integer);
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
        this.stepLoc = stepLoc;
        this.exclusive = exclusive;
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    protected Sequence<Integer> computeValue() {
        computeBounds(lowerLoc.get(), upperLoc.get(), stepLoc.get());
        return computeFull(lower, upper, step);
    }

    private Sequence<Integer> computeFull(int lower, int upper, int step) {
        return exclusive ? Sequences.rangeExclusive(lower, upper, step) : Sequences.range(lower, upper, step);
    }

    private void computeBounds(int newLower, int newUpper, int newStep) {
        lower = newLower;
        upper = newUpper;
        step = newStep;

        if (Math.abs((long) newLower - (long) newUpper) + ((long) (exclusive ? 0 : 1) / step) > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range sequence too big");

        if (lower == upper) {
            size = exclusive ? 0 : 1;
        }
        else {
            size = Math.max(0, ((upper - lower) / step) + 1);

            if (exclusive) {
                boolean tooBig = (step > 0)
                        ? (lower + (size - 1) * step >= upper)
                        : (lower + (size - 1) * step <= upper);
                if (tooBig && size > 0)
                    --size;
            }
        }
    }

    private void addTriggers() {
        if (!lazy) {
            lowerLoc.addInvalidationListener(new InvalidateMeListener());
            upperLoc.addInvalidationListener(new InvalidateMeListener());
            stepLoc.addInvalidationListener(new InvalidateMeListener());
        }
        lowerLoc.addChangeListener(new PrimitiveChangeListener<Integer>() {
            public void onChange(int oldValue, int newValue) {
                
                assert oldValue != newValue;
                
                int oldSize = size;
                computeBounds(newValue, upper, step);
                Sequence<Integer> newElements;

                if (oldSize == 0) {
                    updateSlice(0, -1, computeFull(lower, upper, step));
                }
                else if (oldSize < size) {

                    if (((newValue - oldValue) % step) == 0) {
                        updateSlice(0, -1, Sequences.rangeExclusive(lower, oldValue, step));
                    }
                    else {
                        newElements = computeFull(lower, upper, step);
                        updateSlice(0, oldSize - 1, newElements, newElements);
                    }
                }
                else if (oldSize >= size) {
                    if (((newValue - oldValue) % step) == 0) {
                        updateSlice(0, oldSize - size - 1, TypeInfo.Integer.emptySequence);
                    }
                    else {
                        newElements = computeFull(lower, upper, step);
                        updateSlice(0, oldSize - 1, newElements, newElements);
                    }
                }
            }
        });
        upperLoc.addChangeListener(new PrimitiveChangeListener<Integer>() {
            public void onChange(int oldValue, int newValue) {
                
                assert oldValue != newValue;
                
                int oldSize = size;
                computeBounds(lower, newValue, step);

                if (size == oldSize) {
                    return;
                }
                else if (oldSize == 0) {
                    updateSlice(0, -1, computeFull(lower, upper, step));
                }
                else if (oldSize < size) {
                    int startPos = lower + oldSize * step;
                    updateSlice(oldSize, oldSize - 1, computeFull(startPos, upper, step));
                }
                else if (oldSize > size) {
                    updateSlice(size, oldSize - 1, TypeInfo.Integer.emptySequence);
                }
            }
        });

        stepLoc.addChangeListener(new PrimitiveChangeListener<Integer>() {
            public void onChange(int oldValue, int newValue) {
                
                assert oldValue != newValue;
                
                int oldSize = size;
                computeBounds(lower, upper, newValue);

                Sequence<Integer> newSeq = computeFull(lower, upper, step);
                updateSlice(0, oldSize - 1, newSeq, newSeq);
            }
        });
    }
}
