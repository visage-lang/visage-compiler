/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.*;

/**
 * BoundComprehension -- one-dimensional bound list comprehension, supporting where clauses and variable number of
 * elements per iteration
 *
 * @author Brian Goetz
 */
public abstract class BoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final boolean useIndex;
    private DumbMutableSequence<State<T, V>> state;
    private BoundCompositeSequence<V> underlying;

    public BoundComprehension(Class<V> clazz,
                              SequenceLocation<T> sequenceLocation,
                              boolean useIndex) {
        super(clazz);
        this.sequenceLocation = sequenceLocation;
        this.useIndex = useIndex;
    }

    private static class State<T, V> {
        private SequenceLocation<V> mapped;
        private final ObjectLocation<T> element;
        private final IntLocation index;

        private State(ObjectLocation<T> element, IntLocation index) {
            this.element = element;
            this.index = index;
        }

        private State(ObjectLocation<T> element, IntLocation index, SequenceLocation<V> mapped) {
            this.element = element;
            this.index = index;
            this.mapped = mapped;
        }
    }

    protected abstract SequenceLocation<V> getMappedElement$(ObjectLocation<T> elementLocation, IntLocation indexLocation);

    protected Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.getAsSequence();
        state = new DumbMutableSequence<State<T, V>>(sequence.size());
        SequenceLocation<V>[] locationsArray = Util.<SequenceLocation<V>>newArray(SequenceLocation.class, sequence.size());
        State<T, V>[] newStates = (State<T, V>[]) new State[sequence.size()];
        fillInNewValues(sequence, newStates, locationsArray, 0);
        state.replaceSlice(0, -1, newStates);
        underlying = new BoundCompositeSequence<V>(getClazz(), locationsArray);
        return underlying.getAsSequence();
    }

    private void fillInNewValues(Sequence<? extends T> sequence, State<T, V>[] newStates, SequenceLocation<V>[] locationsArray, int offset) {
        int i = 0;
        for (T value : sequence) {
            State<T, V> stateElt = makeState(i + offset, value);
            newStates[i] = stateElt;
            locationsArray[i++] = stateElt.mapped;
        }
    }

    private State<T, V> makeState(int index, T value) {
        SimpleVariable<T> elementLocation = new SimpleVariable<T>(value);
        SimpleIntVariable indexLocation = useIndex ? new SimpleIntVariable(index) : null;
        SequenceLocation<V> mapped = getMappedElement$(elementLocation, indexLocation);
        return new State<T, V>(elementLocation, indexLocation, mapped);
    }

    protected void initialize() {
        underlying.addChangeListener(new SequenceReplaceListener<V>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends V> newElements, Sequence<V> oldValue, Sequence<V> newValue) {
                BoundComprehension.this.updateSlice(startPos, endPos, newElements);
            }
        });

        sequenceLocation.addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int insertedCount = Sequences.size(newElements);
                int deletedCount = endPos - startPos + 1;
                int netAdded = insertedCount - deletedCount;
                if (netAdded == 0) {
                    for (int i = startPos; i <= endPos; i++)
                        state.get(i).element.set(newElements.get(i - startPos));
                }
                else {
                    SequenceLocation<V>[] locationsArray = Util.<SequenceLocation<V>>newArray(SequenceLocation.class, newElements.size());
                    State<T, V>[] newStates = (State<T, V>[]) new State[newElements.size()];
                    fillInNewValues(newElements, newStates, locationsArray, startPos);
                    if (useIndex) {
                        for (int i = endPos + 1; i < state.size(); i++)
                            state.get(i).index.set(i + netAdded);
                    }
                    underlying.replaceSlice(startPos, endPos, locationsArray);
                    state.replaceSlice(startPos, endPos, newStates);
                }
            }
        });
    }
}
