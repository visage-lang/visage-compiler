package com.sun.javafx.runtime.sequence;

/**
 * SequenceMapper
 *
 * @author Brian Goetz
 */
public interface SequenceMapper<T, V> {
    public V map(Sequence<T> sequence, int index, T value);
}
