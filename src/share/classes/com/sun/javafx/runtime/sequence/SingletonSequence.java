package com.sun.javafx.runtime.sequence;

/**
 * Represents a sequence with a single element.  Singleton sequences should be created with the Sequences.singleton()
 * factory, not the SingletonSequence constructor.  O(1) space and time construction costs.
 *
 * @author Brian Goetz
 */
class SingletonSequence<T> extends AbstractSequence<T> implements Sequence<T> {
    private final T value;

    public SingletonSequence(Class<T> clazz, T value) {
        super(clazz);
        this.value = value;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public T get(int position) {
        return (position == 0) ? value : nullValue;
    }
}
