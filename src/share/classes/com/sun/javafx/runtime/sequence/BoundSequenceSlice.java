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

import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.IntChangeListener;

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
    private int lower, upper;
    private int size;

    BoundSequenceSlice(Class<T> clazz, SequenceLocation<T> sequenceLoc, IntLocation lowerLoc, IntLocation upperLoc, boolean isExclusive) {
        super(clazz);
        this.sequenceLoc = sequenceLoc;
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
        this.isExclusive = isExclusive;
        setInitialValue(computeValue());
        addTriggers();
    }

    private Sequence<T> computeValue() {
        computeBounds(true, true);
        return sequenceLoc.get().getSlice(lower, upper);
    }
    
    /**
     * adjust for exclusive upper bound
     */
    private int adjusted(int upperValue) {
        return (isExclusive ? -1 : 0) + upperValue;
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
            int range = ((upper >= seqSize)? seqSize-1:upper) - ((lower<0)? 0: lower);
            size = (range >= 0) ? range + 1 : 0;
        }
    }

    protected Sequence<T> computeFull(int lower, int upper) {
        return sequenceLoc.getSlice(lower, upper);
    }
                    
    private void addTriggers() {
        sequenceLoc.addChangeListener(new SequenceChangeListener<T>() {

            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                computeBounds(true, true);

                Sequence<T> newSeq = newValue.getSlice(lower, upper);
                updateSlice(0, size == 0 ? 0 : size - 1, newSeq, newSeq);
            }
        });
        lowerLoc.addChangeListener(new IntChangeListener() {

            public void onChange(int oldValue, int newValue) {
                assert oldValue != newValue;
                int oldSize = size;
                computeBounds(true, false);

                if (sequenceLoc.getAsSequence().size() > 0 && size != oldSize) {
                    if (size > oldSize) {
                        updateSlice(
                                0, 
                                -1, 
                                sequenceLoc.getSlice(newValue, (oldSize == 0) ? upper : (oldValue - 1)));
                    } else {
                        updateSlice(
                                0, 
                                oldSize - size - 1, 
                                sequenceLoc.getAsSequence().getEmptySequence());
                    }
                }
            }
        });
        if (upperLoc != null) {
            upperLoc.addChangeListener(new IntChangeListener() {

                public void onChange(int oldValue, int newValue) {
                    assert oldValue != newValue;
                    int oldSize = size;
                    computeBounds(false, true);

                    if (sequenceLoc.getAsSequence().size() > 0 && size != oldSize) {
                        if (size > oldSize) {
                            int oldUpper = adjusted(oldValue);
                            updateSlice(
                                    oldSize, 
                                    oldSize - 1, 
                                    sequenceLoc.getSlice((oldUpper >= lower) ? oldUpper + 1 : lower, upper));
                        } else {
                            updateSlice(
                                    size, 
                                    oldSize - 1, 
                                    sequenceLoc.getAsSequence().getEmptySequence());
                        }
                    }
                }
            });
        }
    }
}
