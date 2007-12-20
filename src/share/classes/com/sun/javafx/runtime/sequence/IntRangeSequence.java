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
 * Special case implementation for sequences that are ranges of integers, such as [1..10].  Range sequences should
 * be constructed with the Sequences.range() factory method rather than with the IntRangeSequence constructor.  An
 * optional "step" allows sequences to vary by more than one; [ 1..5 STEP 2 ] is [ 1, 3, 5 ].  
 * O(1) space and time construction costs.
 *
 * @author Brian Goetz
 */
class IntRangeSequence extends AbstractSequence<Integer> implements Sequence<Integer> {

    private final int start, step, size;


    public IntRangeSequence(int start, int bound, int step, boolean exclusive) {
        super(Integer.class);
        if (Math.abs((long) bound - (long) start) > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range sequence too big");
        this.start = start;
        this.step = step;
        int size;
        if (bound == start) {
            size = exclusive ? 0 : 1;
        }
        else if (bound > start) {
            size = step > 0 ? (((bound - start) / step) + 1) : 0;
            if (exclusive && start + (size-1)*step >= bound)
                --size;
        }
        else {
            size = step < 0 ? (((start - bound) / -step) + 1) : 0;
            if (exclusive && start + (size-1)*step <= bound)
                --size;
        }
        this.size = size;
    }

    public IntRangeSequence(int start, int bound, int step) {
        this(start, bound, step, false);
    }

    public IntRangeSequence(int start, int bound) {
        this(start, bound, (start < bound) ? 1 : -1, false);
    }

    public IntRangeSequence(int start, int bound, boolean exclusive) {
        this(start, bound, (start < bound) ? 1 : -1, exclusive);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Integer get(int position) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException(Integer.toString(position));
        else 
            return (start + position * step);
    }

    @Override
    public void toArray(Object[] array, int destOffset) {
        for (int value = start, index = destOffset; index < destOffset+size; value += step, index++)
            array[index] = value;
    }
}
