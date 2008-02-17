package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.Location;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundSingletonSequence
 *
 * @author Brian Goetz
 */
public class BoundSingletonSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final ObjectLocation<T> location;
    private final Sequence<T> EMPTY;

    public BoundSingletonSequence(Class<T> clazz, ObjectLocation<T> location) {
        super(clazz);
        this.location = location;
        EMPTY = Sequences.emptySequence(clazz);
    }

    protected Sequence<T> computeValue() {
        T element = location.get();
        return (element == null) ? EMPTY : Sequences.singleton(getClazz(), element);
    }

    protected void initialize() {
        location.addChangeListener(new ChangeListener() {
            public boolean onChange(Location t) {
                // @@@ optimize away null update?
                updateSlice(0, value().size() - 1, computeValue());
                return true;
            }
        });
    }
}
