package com.sun.javafx.runtime.sequence;

/**
 * Represents a mapping function that will be applied to each element of a sequence, used in list comprehensions.
 *
 * @author Brian Goetz
 */
public interface SequenceMapper<T, V> {
    public V map(Sequence<T> sequence, int index, T value);
}
