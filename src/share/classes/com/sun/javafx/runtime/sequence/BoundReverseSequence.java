package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceReplaceListener;

/**
 * BoundReverseSequence
 *
 * @author Brian Goetz
 */
public class BoundReverseSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<T> location;

    BoundReverseSequence(SequenceLocation<T> location, boolean lazy) {
        super(location.get().getElementType(), false, lazy);
        this.location = location;
    }

    protected void computeInitial() {
        value = location.get().reverse();
        location.addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int sliceSize = endPos - startPos;
                int actualStart = oldValue.size() - startPos - (sliceSize + 1);
                int actualEnd = actualStart + sliceSize;
                Sequence<? extends T> reverseElements = newElements == null ? null : newElements.reverse();
                Sequence<T> ourNewValue = value.replaceSlice(actualStart, actualEnd, reverseElements);
                notifyListeners(actualStart, actualEnd, reverseElements, value, ourNewValue);
                value = ourNewValue;
            }
        });
        setValid(true);
    }
}
