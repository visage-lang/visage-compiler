package com.sun.javafx.runtime.sequence;

import java.util.BitSet;
import java.util.List;

/**
 * Sequence implementation class that stores sequence elements in an array.  To create array-based sequences, use
 * the make() factory methods in Sequences instead of the ArraySequence constructor.   O(n) space and time construction
 * costs.  
 *
 * @author Brian Goetz
 */
class ArraySequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final T[] array;


    @SuppressWarnings("unchecked")
    public ArraySequence(Class<T> clazz, T... values) {
        super(clazz);
        this.array =  (T[]) new Object[values.length];
        System.arraycopy(values, 0, array, 0, values.length);
    }

    @SuppressWarnings("unchecked")
    public ArraySequence(Class<T> clazz, T[] values, int size) {
        super(clazz);
        this.array =  (T[]) new Object[size];
        System.arraycopy(values, 0, array, 0, size);
    }

    @SuppressWarnings("unchecked")
    public ArraySequence(Class<T> clazz, List<T> values) {
        super(clazz);
        this.array = (T[]) values.toArray();
    }

    @SuppressWarnings("unchecked")
    public ArraySequence(Class<T> clazz, Sequence<T>... sequences) {
        super(clazz);
        int size = 0;
        for (Sequence<? extends T> seq : sequences)
            size += seq.size();
        this.array = (T[]) new Object[size];
        int next = 0;
        for (Sequence<T> seq : sequences) {
            seq.toArray(array, next);
            next += seq.size();
        }
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public T get(int position) {
        return (position < 0 || position >= array.length) ? nullValue : array[position];
    }


    // optimized versions
    @Override
    public BitSet getBits(SequencePredicate<T> predicate) {
        BitSet bits = new BitSet(array.length);
        for (int i = 0; i < array.length; i++)
            if (predicate.matches(this, i, array[i]))
                bits.set(i);
        return bits;
    }

    @Override
    public void toArray(Object[] dest, int destOffset) {
        System.arraycopy(array, 0, dest, destOffset, array.length);
    }
}
