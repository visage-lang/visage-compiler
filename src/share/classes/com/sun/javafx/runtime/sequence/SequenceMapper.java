package com.sun.javafx.runtime.sequence;

/**
 * SequenceMapper
 *
 * @author Brian Goetz
 */
public interface SequenceMapper<T> {
    public T map(Sequence<T> sequence, int index, T value);
}
