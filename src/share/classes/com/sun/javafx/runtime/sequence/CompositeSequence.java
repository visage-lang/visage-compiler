/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.runtime.Util;

/**
 * Intermediate (view) sequence implementation that represents the concatenation of zero or more other sequences of
 * the same element type.  Concatenating sequences should be done through the Sequences.concatenate() factory,
 * not through the CompositeSequence constructor.  O(nSeq) space and time construction costs.
 *
 * @author Brian Goetz
 */
class CompositeSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<? extends T>[] sequences;
    private final int[] startPositions;
    private final int size, depth;

    public CompositeSequence(TypeInfo<T> ti, Sequence<? extends T>... sequences) {
        // @@@ TODO: Deal with nulls in sequences
        super(ti);
        this.sequences = Util.newSequenceArray(sequences.length);
        System.arraycopy(sequences, 0, this.sequences, 0, sequences.length);
        this.startPositions = new int[sequences.length];
        int size = 0;
        int depth = 0;
        for (int i = 0, offset = 0; i < sequences.length; i++) {
            startPositions[i] = offset;
            size += sequences[i].size();
            offset += sequences[i].size();
            depth = Math.max(depth, sequences[i].getDepth());
        }
        this.size = size;
        this.depth = depth + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size)
            return getDefaultValue();
        // Linear search should be good enough for now
        // @@@ OPT: cache last chunk accessed, use that as predictive starting point for next get
        int chunk = 0;
        while (chunk < sequences.length - 1
                && (position >= startPositions[chunk+1] || sequences[chunk].size() == 0))
            ++chunk;
        return sequences[chunk].get(position - startPositions[chunk]);
    }

    @Override
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || (length > 0 && sourceOffset + length > size))
            throw new ArrayIndexOutOfBoundsException();

        int chunk_length;
        for (int chunk=0; chunk < sequences.length && length > 0; chunk++) {
            if (sourceOffset <= sequences[chunk].size()) {
                chunk_length = Math.min(length, sequences[chunk].size() - sourceOffset); 
                sequences[chunk].toArray(sourceOffset, chunk_length, dest, destOffset);
                length -= chunk_length;
                destOffset += chunk_length;
            }
            sourceOffset -= Math.min(sourceOffset, sequences[chunk].size());
        }
    }

}
