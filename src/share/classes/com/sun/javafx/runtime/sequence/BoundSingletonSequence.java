package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.ObjectChangeListener;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundSingletonSequence
 *
 * @author Brian Goetz
 */
class BoundSingletonSequence<T, V extends T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final ObjectLocation<V> location;

    public BoundSingletonSequence(Class<T> clazz, ObjectLocation<V> location) {
        super(clazz);
        this.location = location;
    }

    protected Sequence<T> computeValue() {
        return Sequences.singleton(getClazz(), location.get());
    }

    protected void initialize() {
        location.addChangeListener(new ObjectChangeListener<V>() {
            public void onChange(V oldValue, V newValue) {
                updateSlice(0, value().size() - 1, Sequences.singleton(getClazz(), newValue));
            }
        });
    }
}
