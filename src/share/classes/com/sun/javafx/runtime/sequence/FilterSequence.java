package com.sun.javafx.runtime.sequence;

import java.util.BitSet;

/**
 * FilterSequence
 *
 * @author Brian Goetz
 */
public class FilterSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final Sequence<T> sequence;
    private final int[] indices;

    public FilterSequence(Sequence<T> sequence, BitSet bits) {
        super(sequence.getElementType());
        this.sequence = sequence;
        indices = new int[bits.cardinality()];
        for (int i=0, next=0; i<sequence.size() && next < indices.length; i++) {
            if (bits.get(i))
                indices[next++] = i;
        }
    }

    public int size() {
        return indices.length;
    }

    public T get(int position) {
        return (position < 0 || position >= indices.length)
                ? null
                : sequence.get(indices[position]);
    }
}
