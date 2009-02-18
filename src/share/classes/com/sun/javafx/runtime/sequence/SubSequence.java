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

/**
 * Represents a portion of another sequence.  Subsequences should be created with the Sequences.subsequence() factory
 * method, rather than with the SubSequence constructor.  O(1) space and time construction costs.
 *
 * @author Brian Goetz
 * @author Per Bothner
 */
class SubSequence<T> extends DerivedSequence<T> implements Sequence<T> {

    private final int start;
    private final int step;

    /** Create a slice of a sequence.
     * Uses a default step-size of 1.
     * @param sequence The sequence that we create a subsequence from.
     * @param start The start index.  Caller needs to validate that {@code 0 <= start <= sequence.size()}.
     * @param end The end index, exclusive.  Caller needs to validate that {@code start <= end <= sequence.size()}.
     */
    public SubSequence(Sequence<T> sequence, int start, int end) {
        super(sequence.getElementType(), sequence, end - start);
        this.start = start;
        this.step = 1;
    }

    private SubSequence(Sequence<T> sequence, int size, int start, int step) {
        super(sequence.getElementType(), sequence, size);
        this.start = start;
        this.step = step;
    }

    /** Create a slice of a sequence.
     * Unlike the constructor, this will "collapse" the cases of creating
     * a slice from a SubSequence, or when the size is zero.
     * WARNING The generalization to step!=1, except as used by the Sequence.reverse
     * method, is UNTESTED.  That is why this method is PACKAGE-PRIVATE for now.
     * @param sequence The sequence that we create a subsequence from.
     * @param start The start index.
     *   Caller needs to validate that {@code 0 <= start <= sequence.size()}.
     * @param size Number of elements in the slice.
     *   Caller needs to validate that this is correct: I.e. {@code size >= 0} and
     *   and {@code start >= 0 && start + step*(size-1) < sequence.size()} when {@code step >= 0 && size > 0},
     *   or {@code start < sequence.size() && start + step*(size-1) >= 0} when {@code step < 0 && size > 0},
     *   or {@code 0 <= start <= sequence.size()} when {@code size == 0}.
     * @param step The step size (stride) between selected elements in the base {@code sequence}.
     *
     */
    static <T> Sequence make(Sequence<T> sequence, int size, int start, int step) {
        if (size <= 0)
            return sequence.getElementType().emptySequence;
        if (sequence instanceof SubSequence) {
            SubSequence sseq = (SubSequence) sequence;
            start = sseq.start + sseq.step * start;
            step = sseq.step * step;
            sequence = sseq.sequence;
        }
        return new SubSequence(sequence, size, start, step);
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size)
            return getDefaultValue();
        else
            return sequence.get(start + step * position);
    }
    
    @Override
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || (length > 0 && sourceOffset + length > size()))
            throw new ArrayIndexOutOfBoundsException();
        if (step == 1)
            sequence.toArray(start+sourceOffset, length, dest, destOffset);
        else {
            int j = start + step * sourceOffset;
            for (int i = 0;  i < length;  i++, j += step)
                dest[i + destOffset] = sequence.get(j);
        }
    }
}
