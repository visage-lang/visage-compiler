package com.sun.javafx.runtime.sequence;

/**
 * SingletonSequence
 *
 * @author Brian Goetz
 */
public class SingletonSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    private final T value;

    public SingletonSequence(Class<T> clazz, T value) {
        super(clazz);
        this.value = value;
    }

    public int size() {
        return 1;
    }

    public T get(int position) {
        return (position == 0) ? value : null;
    }
}
