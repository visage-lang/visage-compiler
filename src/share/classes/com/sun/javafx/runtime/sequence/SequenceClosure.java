package com.sun.javafx.runtime.sequence;

/**
 * SequenceClosure
*
* @author Brian Goetz
*/
public interface SequenceClosure<T> {
    public void call(Sequence<T> sequence, int index, T value);
}
