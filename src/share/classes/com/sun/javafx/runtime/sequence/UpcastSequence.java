package com.sun.javafx.runtime.sequence;

/**
 * Given S extends T, represent a Sequence of S as a sequence of T.  
 *
 * @author Brian Goetz
 */
public class UpcastSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    private final Sequence<? extends T> sequence;

    public UpcastSequence(Class<T> newClazz, Sequence<? extends T> sequence) {
        super(newClazz);
        this.sequence = sequence;
    }

    @Override
    public int size() {
        return sequence.size();
    }

    @Override
    public T get(int position) {
        return sequence.get(position);
    }

    @Override
    public int getDepth() {
        return sequence.getDepth() + 1;
    }
}
