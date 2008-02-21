package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.ObjectChangeListener;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundSingletonSequence
 *
 * @author Brian Goetz
 */
public class BoundSingletonSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final ObjectLocation<T> location;

    public BoundSingletonSequence(Class<T> clazz, ObjectLocation<T> location) {
        super(clazz);
        this.location = location;
    }

    protected Sequence<T> computeValue() {
        return Sequences.singleton(getClazz(), location.get());
    }

    protected void initialize() {
        location.addChangeListener(new ObjectChangeListener<T>() {
            public void onChange(T oldValue, T newValue) {
                updateSlice(0, value().size() - 1, Sequences.singleton(getClazz(), newValue));
            }
        });
    }
}
