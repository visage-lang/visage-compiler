package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceChangeListener;

/**
 * BoundReverseSequence
 *
 * @author Brian Goetz
 */
class BoundReverseSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<T> location;

    BoundReverseSequence(SequenceLocation<T> location) {
        super(location.getAsSequence().getElementType());
        this.location = location;
    }

    protected Sequence<T> computeValue() {
        return location.getAsSequence().reverse();
    }

    protected void initialize() {
        location.addChangeListener(new SequenceChangeListener<T>() {
            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int sliceSize = endPos - startPos;
                int actualStart = oldValue.size() - startPos - (sliceSize + 1);
                int actualEnd = actualStart + sliceSize;
                Sequence<? extends T> reverseElements = newElements == null ? null : newElements.reverse();
                updateSlice(actualStart, actualEnd, reverseElements);
            }
        });
    }
}
