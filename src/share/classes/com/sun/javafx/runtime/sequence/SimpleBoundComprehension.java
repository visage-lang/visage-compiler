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
import com.sun.javafx.runtime.location.SequenceChangeListener;
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
        super(typeInfo);
        this.sequenceLocation = sequenceLocation;
        this.dependsOnIndex = dependsOnIndex;
        setInitialValue(computeValue());
        addTriggers();
    }

    public SimpleBoundComprehension(boolean lazy, TypeInfo<V, ?> typeInfo, SequenceLocation<T> sequenceLocation) {
        this(lazy, typeInfo, sequenceLocation, false);
    }

    protected abstract V computeElement$(T element, int index);

    private Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.getAsSequence();
        V[] intermediateResults = Util.<V>newObjectArray(sequence.size());
        int i = 0;
        for (T val : sequence) {
            intermediateResults[i] = computeElement$(val, i);
            i++;
        }
        return Sequences.make(getElementType(), intermediateResults);
    }

    private void addTriggers() {
        sequenceLocation.addChangeListener(new SequenceChangeListener<T>() {
            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                // IF the closure depends on index, then an insertion or deletion causes recomputation of the whole
                // trailing segment of the comprehension, so not only do we recompute the affected segment, but also
                // the whole rest of the sequence too.

                int directlyAffectedSize = Sequences.size(newElements);
                int elementsAdded = directlyAffectedSize - (endPos - startPos + 1);
                boolean updateTrailingElements = dependsOnIndex
                        && (elementsAdded != 0)
                        && (endPos + 1 < Sequences.size(oldValue));
                int indirectlyAffectedStart=0, indirectlyAffectedEnd=-1, indirectlyAffectedSize=0;
                if (updateTrailingElements) {
                    indirectlyAffectedStart = endPos + 1;
                    indirectlyAffectedEnd = oldValue.size() - 1;
                    indirectlyAffectedSize = indirectlyAffectedEnd - indirectlyAffectedStart + 1;
                }
                V[] ourNewElements = Util.<V>newObjectArray(directlyAffectedSize + indirectlyAffectedSize);
                int i = 0;
                for (Iterator<? extends T> it = Sequences.iterator(newElements); it.hasNext(); i++) {
                    ourNewElements[i] = computeElement$(it.next(), dependsOnIndex ? startPos + i : -1);
                }
                i = 0;
                for (Iterator<? extends T> it = Sequences.iterator(oldValue, indirectlyAffectedStart, indirectlyAffectedEnd); it.hasNext(); i++) {
                    ourNewElements[directlyAffectedSize + i]
                            = computeElement$(it.next(), indirectlyAffectedStart + i + elementsAdded);
                }

                Sequence<V> vSequence = Sequences.make(getElementType(), ourNewElements);
                updateSlice(startPos, updateTrailingElements ? indirectlyAffectedEnd : endPos, vSequence);
            }
        });
    }
}
