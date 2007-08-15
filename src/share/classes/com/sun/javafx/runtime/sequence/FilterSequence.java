package com.sun.javafx.runtime.sequence;

import java.util.BitSet;

/**
 * FilterSequence
 *
 * @author Brian Goetz
 */
public class FilterSequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final SequenceInternal<T> sequence;
    private final int[] indices;

    public FilterSequence(Sequence<T> sequence, BitSet bits) {
        super(sequence.getElementType());
        this.sequence = (SequenceInternal<T>) sequence;
        indices = new int[bits.cardinality()];
        for (int i = bits.nextSetBit(0), next = 0; i >= 0; i = bits.nextSetBit(i + 1))
            indices[next++] = i;
    }

    @Override
    public int size() {
        return indices.length;
    }


    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }

    @Override
    public T get(int position) {
        return (position < 0 || position >= indices.length)
                ? nullValue
                : sequence.get(indices[position]);
    }
}
