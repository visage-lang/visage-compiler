/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime.sequence;

import java.util.Iterator;        

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * SimpleBoundComprehension -- special case of a bound list comprehension with one dimension, no where clause
 * and where each iteration produces exactly one result
 *
 * @author Brian Goetz
 */
public abstract class SimpleBoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final boolean dependsOnIndex;

    /**
     * Create a bound list comprehension that meets the following criteria: no where clause, one dimension, and each
     * iteration contributes exactly one value.  For each input element, the computeElement() method will be called to
     * calculate the corresponding output element.
     *
     * @param typeInfo
     * @param sequenceLocation The input sequence
     * @param dependsOnIndex Whether or not the computeElement$ makes use of the indexof operator
     */
    public SimpleBoundComprehension(boolean lazy, TypeInfo<V, ?> typeInfo,
                                    SequenceLocation<T> sequenceLocation,
                                    boolean dependsOnIndex) {
        super(lazy, typeInfo);
        this.sequenceLocation = sequenceLocation;
        this.dependsOnIndex = dependsOnIndex;
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    public SimpleBoundComprehension(boolean lazy, TypeInfo<V, ?> typeInfo, SequenceLocation<T> sequenceLocation) {
        this(lazy, typeInfo, sequenceLocation, false);
    }

    protected abstract V computeElement$(T element, int index);

    protected Sequence<? extends V> computeValue() {
        Sequence<? extends T> sequence = sequenceLocation.getAsSequence();
        V[] intermediateResults = Util.<V>newObjectArray(sequence.size());
        int i = 0;
        for (T val : sequence) {
            intermediateResults[i] = computeElement$(val, i);
            i++;
        }
        return Sequences.make(getElementType(), intermediateResults);
    }

    private void addTriggers() {
        if (lazy)
            sequenceLocation.addInvalidationListener(new InvalidateMeListener());
        else
            sequenceLocation.addSequenceChangeListener(new ChangeListener<T>() {
                public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
                    // IF the closure depends on index, then an insertion or deletion causes recomputation of the whole
                    // trailing segment of the comprehension, so not only do we recompute the affected segment, but also
                    // the whole rest of the sequence too.

                    int directlyAffectedSize = Sequences.sizeOfNewElements(buffer, startPos, newElements);
                    int elementsAdded = directlyAffectedSize - (endPos - startPos);
                    Sequence<? extends T> real = Sequences.getNewElements(buffer, startPos, newElements);
                    int oldValueSize = Sequences.sizeOfOldValue(buffer, oldValue, endPos);
                    boolean updateTrailingElements = dependsOnIndex
                            && (elementsAdded != 0)
                            && (endPos < oldValueSize);
                    int indirectlyAffectedStart=0, indirectlyAffectedEnd=0, indirectlyAffectedSize=0;
                    if (updateTrailingElements) {
                        indirectlyAffectedStart = endPos;
                        indirectlyAffectedEnd = oldValueSize;
                        indirectlyAffectedSize = indirectlyAffectedEnd - indirectlyAffectedStart;
                    }
                    V[] ourNewElements = Util.<V>newObjectArray(directlyAffectedSize + indirectlyAffectedSize);
                    for (int i = 0;  i < directlyAffectedSize;  i++) {
                      T newElement = Sequences.getFromNewElements(buffer, endPos, newElements, i);
                      ourNewElements[i] = computeElement$(newElement, dependsOnIndex ? startPos + i : -1);
                    }
                    for (int i = 0;  i < indirectlyAffectedSize;  i++) {
                        T oldElement = Sequences.getFromOldValue(buffer, oldValue, startPos, endPos,
                            indirectlyAffectedStart + i);
                        ourNewElements[directlyAffectedSize + i]

                                = computeElement$(oldElement, startPos + directlyAffectedSize + i);
                    }

                    Sequence<? extends V> vSequence = Sequences.make(getElementType(), ourNewElements);
                    updateSlice(startPos, updateTrailingElements ? indirectlyAffectedEnd : endPos, vSequence);
                }
            });
    }
}
