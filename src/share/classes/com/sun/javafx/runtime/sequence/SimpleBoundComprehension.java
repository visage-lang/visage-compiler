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
abstract class SimpleBoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final boolean dependsOnIndex;

    /**
     * Create a bound list comprehension that meets the following criteria: no where clause, one dimension, and each
     * iteration contributes exactly one value.  For each input element, the computeElement() method will be called to
     * calculate the corresponding output element.
     *
     * @param clazz The Class of the resulting sequence element type
     * @param sequenceLocation The input sequence
     * @param dependsOnIndex Whether or not the computeElement makes use of the indexof operator
     */
    public SimpleBoundComprehension(Class<V> clazz,
                                    SequenceLocation<T> sequenceLocation,
                                    boolean dependsOnIndex) {
        super(clazz);
        this.sequenceLocation = sequenceLocation;
        this.dependsOnIndex = dependsOnIndex;
    }

    public SimpleBoundComprehension(Class<V> clazz,
                                    SequenceLocation<T> sequenceLocation) {
        this(clazz, sequenceLocation, false);
    }

    abstract V computeElement(T element, int index);

    protected Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.get();
        V[] intermediateResults = Util.<V>newObjectArray(sequence.size());
        for (int i = 0; i < intermediateResults.length; i++)
            intermediateResults[i] = computeElement(sequence.get(i), i);
        return Sequences.make(clazz, intermediateResults);
    }

    protected void initialize() {
        sequenceLocation.addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                // IF the closure depends on index, then an insertion or deletion causes recomputation of the whole
                // trailing segment of the comprehension, so not only do we recompute the affected segment, but also
                // the whole rest of the sequence too.

                int directlyAffectedSize = Sequences.size(newElements);
                int elementsAdded = directlyAffectedSize - (endPos - startPos + 1);
                boolean updateTrailingElements = dependsOnIndex
                        && (elementsAdded != 0)
                        && (endPos + 1 < Sequences.size(oldValue));
                int indirectlyAffectedStart=0, indirectlyAffectedEnd=0, indirectlyAffectedSize=0;
                if (updateTrailingElements) {
                    indirectlyAffectedStart = endPos + 1;
                    indirectlyAffectedEnd = oldValue.size() - 1;
                    indirectlyAffectedSize = indirectlyAffectedEnd - indirectlyAffectedStart + 1;
                }
                V[] ourNewElements = Util.<V>newObjectArray(directlyAffectedSize + indirectlyAffectedSize);
                for (int i = 0; i < directlyAffectedSize; i++)
                    ourNewElements[i] = computeElement(newElements.get(i), dependsOnIndex ? startPos + i : -1);
                for (int i = 0; i < indirectlyAffectedSize; i++)
                    ourNewElements[directlyAffectedSize + i]
                            = computeElement(oldValue.get(indirectlyAffectedStart + i), indirectlyAffectedStart + i + elementsAdded);

                Sequence<V> vSequence = Sequences.make(clazz, ourNewElements);
                updateSlice(startPos, updateTrailingElements ? indirectlyAffectedEnd : endPos, vSequence);
            }
        });
    }

}
