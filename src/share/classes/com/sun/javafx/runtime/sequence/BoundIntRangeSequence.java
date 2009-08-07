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

        if (step == 0)
            throw new IllegalArgumentException("Range step of zero");

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
        if (lazy) {
            lowerLoc.addInvalidationListener(new InvalidateMeListener(this));
            upperLoc.addInvalidationListener(new InvalidateMeListener(this));
            stepLoc.addInvalidationListener(new InvalidateMeListener(this));
        } else {
            lowerLoc.addChangeListener(new LowerLimitChangeListener(this));
            upperLoc.addChangeListener(new UpperLimitChangeListener(this));
            stepLoc.addChangeListener(new StepChangeListener(this));
        }
    }

    private static class LowerLimitChangeListener extends WeakMeChangeListener<Integer> {
        LowerLimitChangeListener(BoundIntRangeSequence birs) {
            super(birs);
        }

        @Override
        public void onChange(int oldValue, int newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(int oldValue, int newValue) {
            assert oldValue != newValue;
            BoundIntRangeSequence birs = (BoundIntRangeSequence) get();
            if (birs != null) {
                int oldSize = birs.size;
                birs.computeBounds(newValue, birs.upper, birs.step);
                Sequence<Integer> newElements;

                if (oldSize == 0) {
                    birs.updateSlice(0, 0, birs.computeFull(birs.lower, birs.upper, birs.step));
                }
                else if (oldSize < birs.size) {

                    if (((newValue - oldValue) % birs.step) == 0) {
                        birs.updateSlice(0, 0, Sequences.rangeExclusive(birs.lower, oldValue, birs.step));
                    }
                    else {
                        newElements = birs.computeFull(birs.lower, birs.upper, birs.step);
                        birs.updateSlice(0, oldSize, newElements);
                    }
                }
                else if (oldSize >= birs.size) {
                    if (((newValue - oldValue) % birs.step) == 0) {
                        birs.updateSlice(0, oldSize - birs.size, TypeInfo.Integer.emptySequence);
                    }
                    else {
                        newElements = birs.computeFull(birs.lower, birs.upper, birs.step);
                        birs.updateSlice(0, oldSize, newElements);
                    }
                }
                return true;
            } else { // sequence is null
                return false;
            }
        }
    }

    private static class UpperLimitChangeListener extends WeakMeChangeListener<Integer> {
        UpperLimitChangeListener(BoundIntRangeSequence birs) {
            super(birs);
        }

        @Override
        public void onChange(int oldValue, int newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(int oldValue, int newValue) {
            assert oldValue != newValue;
            BoundIntRangeSequence birs = (BoundIntRangeSequence) get();
            if (birs != null) {
                int oldSize = birs.size;
                birs.computeBounds(birs.lower, newValue, birs.step);

                if (birs.size == oldSize) {
                    return true;
                }
                else if (oldSize == 0) {
                    birs.updateSlice(0, 0, birs.computeFull(birs.lower, birs.upper, birs.step));
                }
                else if (oldSize < birs.size) {
                    int startPos = birs.lower + oldSize * birs.step;
                    birs.updateSlice(oldSize, oldSize, birs.computeFull(startPos, birs.upper, birs.step));
                }
                else if (oldSize > birs.size) {
                    birs.updateSlice(birs.size, oldSize, TypeInfo.Integer.emptySequence);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private static class StepChangeListener extends WeakMeChangeListener<Integer> {
        StepChangeListener(BoundIntRangeSequence birs) {
            super(birs);
        }

        @Override
        public void onChange(int oldValue, int newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(int oldValue, int newValue) {
            assert oldValue != newValue;
            BoundIntRangeSequence birs= (BoundIntRangeSequence) get();
            if (birs != null) {
                int oldSize = birs.size;
                birs.computeBounds(birs.lower, birs.upper, newValue);

                Sequence<Integer> newSeq = birs.computeFull(birs.lower, birs.upper, birs.step);
                birs.updateSlice(0, oldSize, newSeq);
                return true;
            } else {
                return false;
            }
        }
    }
}
