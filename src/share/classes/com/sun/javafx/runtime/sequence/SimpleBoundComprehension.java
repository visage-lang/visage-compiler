package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceReplaceListener;

/**
 * SimpleBoundComprehension -- special case of a bound list comprehension with one dimension, no where clause
 * and where each iteration produces exactly one result
 *
 * @author Brian Goetz
 */
class SimpleBoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final ElementGenerator<T, V> generator;

    public interface ElementGenerator<T, V> {
        public V getValue(T element, int index);
    }

    public SimpleBoundComprehension(Class<V> clazz,
                                    SequenceLocation<T> sequenceLocation,
                                    ElementGenerator<T, V> generator) {
        super(clazz, false, false);
        this.sequenceLocation = sequenceLocation;
        this.generator = generator;
    }

    protected Sequence<V> computeInitial() {
        Sequence<T> sequence = sequenceLocation.get();
        sequenceLocation.addChangeListener(new MyChangeListener());
        V[] intermediateResults = Util.<V>newObjectArray(sequence.size());
        for (int i = 0; i < intermediateResults.length; i++)
            intermediateResults[i] = generator.getValue(sequence.get(i), i);
        return Sequences.make(clazz, intermediateResults);
    }

    private class MyChangeListener implements SequenceReplaceListener<T> {
        public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
            V[] ourNewElements = Util.<V>newObjectArray(Sequences.size(newElements));
            for (int i = 0; i < ourNewElements.length; i++)
                ourNewElements[i] = generator.getValue(newElements.get(i), i);
            Sequence<V> vSequence = Sequences.make(clazz, ourNewElements);
            updateSlice(startPos, endPos, vSequence);
        }
    }
}
