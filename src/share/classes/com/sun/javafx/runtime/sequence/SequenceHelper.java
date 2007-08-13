package com.sun.javafx.runtime.sequence;

/**
 * SequenceHelper
 *
 * @author Brian Goetz
 */
public final class SequenceHelper {

    // Inhibit instantiation
    private SequenceHelper() { }

    public static<T> Sequence<T> concatenate(Class<T> clazz, Sequence<T> first, Sequence<T> second) {
        if (first.size() == 0)
            return second;
        else if (second.size() == 0)
            return first;
        else
            return new CompositeSequence<T>(clazz, first, second);
    }

    public static<T> Sequence<T> concatenate(Class<T> clazz, Sequence<T>... seqs) {
        return new CompositeSequence<T>(clazz, seqs);
    }

    public static Sequence<Integer> rangeSequence(int lower, int upper) {
        return new IntRangeSequence(lower, upper);
    }

    public static<T> Sequence<T> emptySequence(Class<T> clazz) {
        return EmptySequence.get(clazz);
    }
}
