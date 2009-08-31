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
 * BoundNumberRangeSequence
 *
 * @author Zhiqun Chen
 */
class BoundNumberRangeSequence extends AbstractBoundSequence<Float> implements SequenceLocation<Float> {

    private final FloatLocation lowerLoc, upperLoc, stepLoc;
    private final boolean exclusive;
    private float lower, upper, step;
    private int size;

    public BoundNumberRangeSequence(boolean lazy, FloatLocation lowerLoc, FloatLocation upperLoc) {
        this(lazy, lowerLoc, upperLoc, FloatConstant.make(1), false);
    }

    public BoundNumberRangeSequence(boolean lazy, FloatLocation lowerLoc, FloatLocation upperLoc, FloatLocation stepLoc) {
        this(lazy, lowerLoc, upperLoc, stepLoc, false);
    }

    public BoundNumberRangeSequence(boolean lazy, FloatLocation lowerLoc, FloatLocation upperLoc, boolean exclusive) {
        this(lazy, lowerLoc, upperLoc, FloatConstant.make(1), exclusive);
    }

    public BoundNumberRangeSequence(boolean lazy, FloatLocation lowerLoc, FloatLocation upperLoc, FloatLocation stepLoc, boolean exclusive) {
        super(lazy, TypeInfo.Float);
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
        this.stepLoc = stepLoc;
        this.exclusive = exclusive;
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    protected Sequence<Float> computeValue() {
        computeBounds(lowerLoc.get(), upperLoc.get(), stepLoc.get());
        return computeFull(lower, upper, step);
    }

    private Sequence<Float> computeFull(float lower, float upper, float step) {
    
        return exclusive ? Sequences.rangeExclusive(lower, upper, step) : Sequences.range(lower, upper, step);
    }

    private void computeBounds(float newLower, float newUpper, float newStep) {
        lower = newLower;
        upper = newUpper;
        step = newStep;

        if (step == 0.0f)
            throw new IllegalArgumentException("Range step of zero");

        // Not all floating point numbers can be exactly represented as a binary (base 2) decimal number (e.g lower = 0.1).
        // The size of the sequence from the following calculation could be off by 1. However, such cases are very rare.
        if (lower == upper) {
            size = exclusive ? 0 : 1;
        }
        else {
            long sz = Math.max(0, ((long)((upper - lower) / step)) + 1);

            if (exclusive) {
                boolean tooBig = (step > 0.0f)
                        ? (lower + (sz - 1) * step >= upper)
                        : (lower + (sz - 1) * step <= upper);
                if (tooBig && sz > 0)
                    --sz;
            }
            
            if (sz > Integer.MAX_VALUE || sz < 0)
                throw new IllegalArgumentException("Range sequence too big");
            else
                size = (int) sz;
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

    private static class LowerLimitChangeListener extends WeakMeChangeListener<Float> {
        LowerLimitChangeListener(BoundNumberRangeSequence bnc) {
            super(bnc);
        }

        @Override
        public void onChange(float oldValue, float newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(float oldValue, float newValue) {
             assert oldValue != newValue;
             BoundNumberRangeSequence bnrs = (BoundNumberRangeSequence) get();
             if (bnrs != null) {
                int oldSize = bnrs.size;
                bnrs.computeBounds(newValue, bnrs.upper, bnrs.step);
                Sequence<Float> newElements;

                if (oldSize == 0) {
                    bnrs.updateSlice(0, 0, bnrs.computeFull(bnrs.lower, bnrs.upper, bnrs.step));
                }
                else if (oldSize < bnrs.size) {

                    if (((newValue - oldValue) % bnrs.step) == 0) {
                        bnrs.updateSlice(0, 0, Sequences.rangeExclusive(bnrs.lower, oldValue, bnrs.step));
                    }
                    else {
                        newElements = bnrs.computeFull(bnrs.lower, bnrs.upper, bnrs.step);
                        bnrs.updateSlice(0, oldSize, newElements);
                    }
                }
                else if (oldSize >= bnrs.size) {
                    if (((newValue - oldValue) % bnrs.step) == 0) {

                        bnrs.updateSlice(0, oldSize - bnrs.size, TypeInfo.Float.emptySequence);
                    }
                    else {
                        newElements = bnrs.computeFull(bnrs.lower, bnrs.upper, bnrs.step);
                        bnrs.updateSlice(0, oldSize, newElements);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private static class UpperLimitChangeListener extends WeakMeChangeListener<Float> {
        UpperLimitChangeListener(BoundNumberRangeSequence bnrs) {
            super(bnrs);
        }

        @Override
        public void onChange(float oldValue, float newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(float oldValue, float newValue) {
            assert oldValue != newValue;
            BoundNumberRangeSequence bnrs = (BoundNumberRangeSequence) get();
            if (bnrs != null) {
                int oldSize = bnrs.size;
                bnrs.computeBounds(bnrs.lower, newValue, bnrs.step);

                if (bnrs.size == oldSize) {
                    return true;
                }
                else if (oldSize == 0) {
                    bnrs.updateSlice(0, 0, bnrs.computeFull(bnrs.lower, bnrs.upper, bnrs.step));
                }
                else if (oldSize < bnrs.size) {
                    bnrs.updateSlice(oldSize, oldSize, bnrs.computeFull(bnrs.lower+oldSize*bnrs.step, bnrs.upper, bnrs.step));
                }
                else if (oldSize > bnrs.size) {
                    bnrs.updateSlice(bnrs.size, oldSize, TypeInfo.Float.emptySequence);
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private static class StepChangeListener extends WeakMeChangeListener<Float> {
        StepChangeListener(BoundNumberRangeSequence bnrs) {
            super(bnrs);
        }

        @Override
        public void onChange(float oldValue, float newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(float oldValue, float newValue) {
            assert oldValue != newValue;
            BoundNumberRangeSequence bnrs = (BoundNumberRangeSequence) get();
            if (bnrs != null) {
                int oldSize = bnrs.size;
                bnrs.computeBounds(bnrs.lower, bnrs.upper, newValue);

                Sequence<Float> newSeq = bnrs.computeFull(bnrs.lower, bnrs.upper, bnrs.step);
                bnrs.updateSlice(0, oldSize, newSeq);
                return true;
            } else {
                return false;
            }
        }
    }
}
