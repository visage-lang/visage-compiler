package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundUpcastSequence
 *
 * @author Brian Goetz
 */
public class BoundUpcastSequence<T, V extends T> extends AbstractBoundSequence<T> {

    private final SequenceLocation<V> sequence;
    private final Class<V> vClazz;

    public BoundUpcastSequence(Class<T> clazz, SequenceLocation<V> sequence) {
        super(clazz);
        this.sequence = sequence;
        this.vClazz = sequence.get().getElementType();
    }

    protected Sequence<T> computeValue() {
        return Sequences.upcast(getClazz(), sequence.get());
    }

    protected void initialize() {
        sequence.addChangeListener(new SequenceChangeListener<V>() {
            public void onChange(int startPos, int endPos, Sequence<? extends V> newElements, Sequence<V> oldValue, Sequence<V> newValue) {
                updateSlice(startPos, endPos, Sequences.upcast(vClazz, newElements));
            }
        });
    }
}
