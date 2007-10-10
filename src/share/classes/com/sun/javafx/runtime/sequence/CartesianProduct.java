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
 * Special case for n-dimensional foreach comprehension when there are no where clauses on any list and
 * the foreach body always returns a single instance. The results are computed as needed rather than
 * precomputed, to save space.
 *
 * @author Brian Goetz
 */
public class CartesianProduct<T> extends AbstractSequence<T> implements Sequence<T> {

    public interface Mapper<T> {
        public T map(int[] indexes, Object[] values);
    }

    private final Sequence<?>[] sequences;
    private final Mapper<T> mapper;
    private final int size;
    private final int[] sizes;

    public CartesianProduct(Class<T> clazz, Mapper<T> mapper, Sequence<?>... sequences) {
        super(clazz);
        this.sequences = sequences;
        this.mapper = mapper;
        if (sequences.length == 0)
            size = 0;
        else {
            int depth = 1;
            for (Sequence<?> seq : sequences)
                depth = depth * seq.size();
            size = depth;
        }
        sizes = new int[sequences.length];
        for (int i=0; i<sequences.length; i++) {
            int cur = 1;
            for (int j=i+1; j<sequences.length; j++)
                cur *= sequences[j].size();
            sizes[i] = cur;
        }
    }

    public int getDepth() {
        int depth = 0;
        for (Sequence<?> seq : sequences)
            depth = Math.max(depth, seq.getDepth());
        return depth + 1;
    }

    public int size() {
        return size;
    }

    public T get(int position) {
        int[] indices = new int[sequences.length];
        Object[] values = new Object[sequences.length];
        int last = sequences.length-1;
        for (int i=0; i<last; i++) {
            indices[i] = position / sizes[i];
            values[i] = sequences[i].get(indices[i]);
            position -= indices[i]*sizes[i];
        }
        indices[last] = position;
        values[last] = sequences[last].get(indices[last]);
        return mapper.map(indices, values);
    }
}
