/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/**
 * Special case implementation for sequences that are ranges of floating point, such as [1.0 .. 2.0 BY .1].
 * Range sequences should be constructed with the Sequences.range() factory method rather than with the
 * NumberRangeSequence constructor. Unlike integer range sequences, the step is required.
 * O(1) space and time construction costs.
 *
 * @author Brian Goetz
 */
class NumberRangeSequence extends AbstractSequence<Double> implements Sequence<Double> {

    private final double start, step;
    private final int size;


    public NumberRangeSequence(double start, double bound, double step, boolean exclusive) {
        super(Double.class);
        this.start = start;
        this.step = step;
        long size;
        if (bound == start) {
            size = exclusive ? 0 : 1;
        }
        else if (bound > start) {
            size = step > 0 ? (((long) ((bound - start) / step)) + 1) : 0;
            if (exclusive && start + (size-1)*step >= bound)
                --size;
        }
        else {
            size = step < 0 ? (((long) ((start - bound) / -step)) + 1) : 0;
            if (exclusive && start + (size-1)*step <= bound)
                --size;
        }
        if (size > Integer.MAX_VALUE || size < 0)
            throw new IllegalArgumentException("Range sequence too big");
        this.size = (int) size;
    }

    public NumberRangeSequence(double start, double bound, double step) {
        this(start, bound, step, false);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Double get(int position) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException(Integer.toString(position));
        else
            return (start + position * step);
    }

    @Override
    public void toArray(Object[] array, int destOffset) {
        int index = destOffset;
        for (double value = start; index < destOffset+size; value += step, index++)
            array[index] = value;
    }
}
