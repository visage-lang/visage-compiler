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

    public CompositeSequence(Class<T> clazz, Sequence<? extends T>... sequences) {
        super(clazz);
        this.sequences = sequences.clone();
        this.startPositions = new int[sequences.length];
        int size = 0;
        int depth = 0;
        for (int i = 0, offset = 0; i < sequences.length; i++) {
            if (!sequences[i].getElementType().isAssignableFrom(clazz))
                throw new ClassCastException();
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
        if (position < 0 || position >= size || size == 0)
            return nullValue;
        // Linear search should be good enough for now
        int chunk = 0;
        while (chunk < sequences.length - 1
                && (position >= startPositions[chunk+1] || sequences[chunk].size() == 0))
            ++chunk;
        return sequences[chunk].get(position - startPositions[chunk]);
    }
}
