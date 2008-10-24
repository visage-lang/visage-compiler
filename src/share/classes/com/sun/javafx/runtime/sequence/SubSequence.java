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
 */
class SubSequence<T> extends DerivedSequence<T> implements Sequence<T> {

    private final int start;
    private final int end;

    public SubSequence(Sequence<T> sequence, int start, int end) {
        super(sequence.getElementType(), sequence, (start <= end) ? end - start : 0);
        this.start = start;
        this.end = end;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position + start >= end)
            return getDefaultValue();
        else
            return sequence.get(position + start);
    }
    
    @Override
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || (length > 0 && sourceOffset + length > size()))
            throw new ArrayIndexOutOfBoundsException();

        sequence.toArray(start+sourceOffset, length, dest, destOffset);
    }
}
