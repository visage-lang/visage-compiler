package com.sun.javafx.runtime.sequence;

/**
 * SequenceSelector
*
* @author Brian Goetz
*/
public interface SequencePredicate<T> {
    public boolean matches(Sequence<T> sequence, int index, T value);
}
