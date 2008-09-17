package com.sun.javafx.runtime;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * TypeInfo
 *
 * @author Brian Goetz
 */
public interface TypeInfo<T> {
    public T getDefaultValue();
    public Sequence<T> getEmptySequence();
}

