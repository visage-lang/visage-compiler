package com.sun.javafx.runtime.sequence;

/**
 * Represents a predicate function that selects elements from a sequence, used in predicate get, delete, and
 * insert operations.
 *
 * @author Brian Goetz
 */
public interface SequencePredicate<T> {
    public boolean matches(Sequence<T> sequence, int index, T value);
}
