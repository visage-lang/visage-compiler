package com.sun.javafx.runtime.sequence;

/**
 * DerivedSequence
 *
 * @author Brian Goetz
 */
abstract class DerivedSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    protected final Sequence<? extends T> sequence;
    protected final int size, depth;

    public DerivedSequence(Class<T> clazz, Sequence<? extends T> sequence, int size, int depth) {
        super(clazz);
        this.sequence = sequence;
        this.size = size;
        this.depth = depth;
    }

    protected DerivedSequence(Class<T> clazz, Sequence<? extends T> sequence, int size) {
        this(clazz, sequence, size, sequence.getDepth() + 1);
    }

    protected DerivedSequence(Class<T> clazz, Sequence<? extends T> sequence) {
        this(clazz, sequence, sequence.size(), sequence.getDepth() + 1);
    }

    public int size() {
        return size;
    }

    public int getDepth() {
        return depth;
    }
}
