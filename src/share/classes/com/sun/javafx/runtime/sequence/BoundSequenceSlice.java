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
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.InvalidateMeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundSequenceSlice
 *
 * @author Brian Goetz
 * @author Zhiqun Chen
 */
class BoundSequenceSlice<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<T> sequenceLoc;
    private final IntLocation lowerLoc;
    private final IntLocation upperLoc;
    private final boolean isExclusive;
    private int lower, upper/*exclusive*/;
    private int size;

    BoundSequenceSlice(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<T> sequenceLoc, IntLocation lowerLoc, IntLocation upperLoc, boolean isExclusive) {
        super(lazy, typeInfo);
        this.sequenceLoc = sequenceLoc;
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
        this.isExclusive = isExclusive;
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    protected Sequence<? extends T> computeValue() {
        computeBounds(true, true);
        return sequenceLoc.get().getSlice(lower, upper);
    }
    
    /**
     * adjust for exclusive upper bound
     */
    private int adjusted(int upperValue) {
        return (isExclusive ? 0 : 1) + upperValue;
    }

    private void computeBounds(boolean updateLower, boolean updateUpper) {
        int seqSize = sequenceLoc.getAsSequence().size();
        
        if (updateLower) {
            lower = lowerLoc.get();
        }
        if (updateUpper) {
            upper = adjusted(upperLoc == null ? seqSize-1 : upperLoc.get());
        }
        
        if (seqSize == 0) {
            size = 0;
        } else {
            int range = ((upper > seqSize)? seqSize:upper) - ((lower<0)? 0: lower);
            size = (range >= 0) ? range : 0;
        }
    }
          
    private void addTriggers() {
        if (lazy) {
            sequenceLoc.addInvalidationListener(new InvalidateMeListener(this));
            lowerLoc.addInvalidationListener(new InvalidateMeListener(this));
            if (upperLoc != null)
                upperLoc.addInvalidationListener(new InvalidateMeListener(this));
        }
        else {
            sequenceLoc.addSequenceChangeListener(new UnderlyingSequenceChangeListener<T>(this));
            lowerLoc.addChangeListener(new LowerLimitChangeListener<T>(this));
            if (upperLoc != null) {
                upperLoc.addChangeListener(new UpperLimitChangeListener<T>(this));
            }
        }
    }

    private static class UnderlyingSequenceChangeListener<T> extends WeakMeChangeListener<T> {
        UnderlyingSequenceChangeListener(BoundSequenceSlice<T> bss) {
            super(bss);
        }

        @Override
        public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            onChangeB(buffer, oldValue, startPos, endPos, newElements);
        }

        @Override
        public boolean onChangeB(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
            BoundSequenceSlice<T> bss = (BoundSequenceSlice<T>) get();
            if (bss != null) {
                int oldSize = bss.size;
                bss.computeBounds(true, true);

                Sequence<? extends T> newValue = buffer != null ? buffer : newElements;
                Sequence<? extends T> newSeq = newValue.getSlice(bss.lower, bss.upper);
                bss.updateSlice(0, oldSize, newSeq); // FIXME inefficient for whole-sequence replacement
                return true;
            } else {
                return false;
            }
        }
    }

    private static class LowerLimitChangeListener<T> extends WeakMeChangeListener<Integer> {
        LowerLimitChangeListener(BoundSequenceSlice<T> bss) {
            super(bss);
        }

        @Override
        public void onChange(int oldValue, int newValue) {
            onChangeB(oldValue, newValue);
        }


        @Override
        public boolean onChangeB(int oldValue, int newValue) {
            BoundSequenceSlice<T> bss = (BoundSequenceSlice<T>) get();
            if (bss != null) {
                assert oldValue != newValue;
                int oldSize = bss.size;
                bss.computeBounds(true, false);

                if (bss.sequenceLoc.getAsSequence().size() > 0 && bss.size != oldSize) {
                    if (bss.size > oldSize) {
                        bss.updateSlice(
                                0,
                                0,
                                bss.sequenceLoc.getSlice(newValue, (oldSize == 0) ? bss.upper : oldValue));
                    } else {
                        bss.updateSlice(
                                0,
                                oldSize - bss.size,
                                bss.sequenceLoc.getAsSequence().getEmptySequence());
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private static class UpperLimitChangeListener<T> extends WeakMeChangeListener<Integer> {
        UpperLimitChangeListener(BoundSequenceSlice<T> bss) {
            super(bss);
        }

        @Override
        public void onChange(int oldValue, int newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(int oldValue, int newValue) {
            assert oldValue != newValue;
            BoundSequenceSlice<T> bss = (BoundSequenceSlice<T>) get();
            if (bss != null) {
                int oldSize = bss.size;
                bss.computeBounds(false, true);

                if (bss.sequenceLoc.getAsSequence().size() > 0 && bss.size != oldSize) {
                    if (bss.size > oldSize) {
                        int oldUpper = bss.adjusted(oldValue);
                        bss.updateSlice(
                            oldSize,
                            oldSize,
                            bss.sequenceLoc.getSlice((oldUpper >= bss.lower) ? oldUpper : bss.lower, bss.upper));
                    } else {
                        bss.updateSlice(
                            bss.size,
                            oldSize,
                            bss.sequenceLoc.getAsSequence().getEmptySequence());
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
