package com.sun.javafx.runtime.sequence;

import java.util.BitSet;

/**
 * SequenceHelper -- static helper methods for constructing derived sequences.  Implements heuristics for reducing
 * time and space overhead, such as flattening complicated sequence trees where appropriate and ignoring null
 * transformations (such as appending an empty sequence).
 *
 * @author Brian Goetz
 */
final class Sequences {

    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Double DOUBLE_ZERO = Double.valueOf(0);

    // Inhibit instantiation
    private Sequences() { }

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

    public static<T> Sequence<T> filter(Sequence<T> seq, BitSet bits) {
        if (bits.cardinality() == seq.size() && bits.nextClearBit(0) == seq.size())
            return seq;
        else if (bits.cardinality() == 0)
            return EmptySequence.get(seq.getElementType());
        else
            return new FilterSequence<T>(seq, bits);
    }

    public static<T> Sequence<T> subsequence(Sequence<T> seq, int start, int end) {
        if (start >= end)
            return EmptySequence.get(seq.getElementType());
        else if (start <= 0 && end >= seq.size())
            return seq;
        else
            return new SubSequence<T>(seq, start, end);
    }

    public static<T> Sequence<T> singleton(Class<T> clazz, T t) {
        return new SingletonSequence<T>(clazz, t);
    }

    public static<T> Sequence<T> emptySequence(Class<T> clazz) {
        return EmptySequence.get(clazz);
    }
}
