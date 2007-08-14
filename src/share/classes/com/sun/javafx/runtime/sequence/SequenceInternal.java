package com.sun.javafx.runtime.sequence;

import java.util.BitSet;

/**
 * SequenceInternal -- Sequence methods that are not part of the public interface
 *
 * @author Brian Goetz
 */
public interface SequenceInternal<T> extends Sequence<T> {
    /**
     * Returns the number of levels of sequence objects between this Sequence object and the deepest data.
     * Leaf classes (e.g., ArraySequence, IntRangeSequence) have a depth of zero; composite classes have a depth
     * one greater than their deepest leaf.
     */
    int getDepth();

    /**
     * Return a BitSet indicating which elements of the sequence match the given predicate.  AbstractSequence
     * provides a default implementation in terms of get(i); implementations may want to provide an optimized
     * version.  
     */
    BitSet getBits(SequencePredicate<T> predicate);
}
