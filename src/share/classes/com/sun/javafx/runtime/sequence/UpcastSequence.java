package com.sun.javafx.runtime.sequence;

/**
 * Given S extends T, represent a Sequence of S as a sequence of T.  
 *
 * @author Brian Goetz
 */
class UpcastSequence<T> extends DerivedSequence<T> implements Sequence<T> {

    public UpcastSequence(Class<T> newClazz, Sequence<? extends T> sequence) {
        super(newClazz, sequence);
    }

    @Override
    public T get(int position) {
        return sequence.get(position);
    }
}
