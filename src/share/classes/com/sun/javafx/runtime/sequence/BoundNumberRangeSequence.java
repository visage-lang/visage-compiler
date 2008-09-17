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

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.TypeInfos;

/**
 * BoundNumberRangeSequence
 *
 * @author Zhiqun Chen
 */
public class BoundNumberRangeSequence extends AbstractBoundSequence<Double> implements SequenceLocation<Double> {

    private final DoubleLocation lowerLoc, upperLoc, stepLoc;
    private final boolean exclusive;
    private double lower, upper, step;
    private int size;

    public BoundNumberRangeSequence(DoubleLocation lowerLoc, DoubleLocation upperLoc) {
        this(lowerLoc, upperLoc, DoubleConstant.make(1), false);
    }

    public BoundNumberRangeSequence(DoubleLocation lowerLoc, DoubleLocation upperLoc, DoubleLocation stepLoc) {
        this(lowerLoc, upperLoc, stepLoc, false);
    }

    public BoundNumberRangeSequence(DoubleLocation lowerLoc, DoubleLocation upperLoc, boolean exclusive) {
        this(lowerLoc, upperLoc, DoubleConstant.make(1), exclusive);
    }

    public BoundNumberRangeSequence(DoubleLocation lowerLoc, DoubleLocation upperLoc, DoubleLocation stepLoc, boolean exclusive) {
        super(Double.class);
        this.lowerLoc = lowerLoc;
        this.upperLoc = upperLoc;
        this.stepLoc = stepLoc;
        this.exclusive = exclusive;
        setInitialValue(computeValue());
        addTriggers();
    }

    private Sequence<Double> computeValue() {
        computeBounds(lowerLoc.get(), upperLoc.get(), stepLoc.get());
        return computeFull(lower, upper, step);
    }

    private Sequence<Double> computeFull(double lower, double upper, double step) {
    
        return exclusive ? Sequences.rangeExclusive(lower, upper, step) : Sequences.range(lower, upper, step);
    }

    private void computeBounds(double newLower, double newUpper, double newStep) {       
        lower = newLower;
        upper = newUpper;
        step = newStep;

        // Not all floating point numbers can be exactly represented as a binary (base 2) decimal number (e.g lower = 0.1).
        // The size of the sequence from the following calculation could be off by 1. However, such cases are very rare.
        if (lower == upper) {
            size = exclusive ? 0 : 1;
        }
        else {
            long size = Math.max(0, ((long)((upper - lower) / step)) + 1);

            if (exclusive) {
                boolean tooBig = (step > 0)
                        ? (lower + (size - 1) * step >= upper)
                        : (lower + (size - 1) * step <= upper);
                if (tooBig && size > 0)
                    --size;
            }
            
            if (size > Integer.MAX_VALUE || size < 0)
                throw new IllegalArgumentException("Range sequence too big");
            else
                this.size = (int) size;
        }
    }

    private void addTriggers() {
        lowerLoc.addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                
                assert oldValue != newValue;
                int oldSize = size;
                computeBounds(newValue, upper, step);
                Sequence<Double> newElements;

                if (oldSize == 0) {
                    updateSlice(0, -1, computeFull(lower, upper, step));
                }
                else if (oldSize < size) {

                    if (((newValue - oldValue) % step) == 0) {
                        updateSlice(0, -1, Sequences.rangeExclusive(lower, oldValue, step));
                    }
                    else {
                        newElements = computeFull(lower, upper, step);
                        updateSlice(0, newElements.isEmpty() ? 0 : (size - 1), newElements, newElements);
                    }
                }
                else if (oldSize >= size) {
                    if (((newValue - oldValue) % step) == 0) {
                        
                        updateSlice(0, oldSize - size - 1, TypeInfos.Double.getEmptySequence());
                    }
                    else {
                        newElements = computeFull(lower, upper, step);
                        updateSlice(0, newElements.isEmpty() ? 0 : (size - 1), newElements, newElements);
                    }
                }
            }
        });
        upperLoc.addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                
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
                   updateSlice(oldSize, oldSize - 1, computeFull(lower+oldSize*step, upper, step));
                }
                else if (oldSize > size) {
                    updateSlice(size, oldSize - 1, TypeInfos.Double.getEmptySequence());
                }
            }
        });

        stepLoc.addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                
                assert oldValue != newValue;                                  
                int oldSize = size;
                computeBounds(lower, upper, newValue);

                Sequence<Double> newSeq = computeFull(lower, upper, step);
                updateSlice(0, oldSize - 1, newSeq, newSeq);
            }
        });
    }
}
