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

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.*;

/**
 * BoundComprehension -- one-dimensional bound list comprehension, supporting where clauses and variable number of
 * elements per iteration
 *
 * @param T The element type for the input sequence
 * @param V The element type for the resulting sequence
 * @param L The Location type corresponding to the element type T
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundComprehension<T, L extends ObjectLocation<T>, V>
        extends AbstractBoundSequence<V> implements SequenceLocation<V> {

    private final SequenceLocation<T> sequenceLocation;
    protected final boolean useIndex;
    protected final TypeInfo<T> inType;
    private DumbMutableSequence<State<T, L, V>> state;
    private BoundCompositeSequence<V> underlying;

    public AbstractBoundComprehension(TypeInfo<V> typeInfo,
                                      TypeInfo<T> inType,
                                      SequenceLocation<T> sequenceLocation,
                                      boolean useIndex) {
        super(typeInfo);
        this.inType = inType;
        this.sequenceLocation = sequenceLocation;
        this.useIndex = useIndex;
        setInitialValue(computeValue());
        addTriggers();
    }

    public AbstractBoundComprehension(TypeInfo<V> typeInfo,
                                      TypeInfo<T> inType,
                                      SequenceLocation<T> sequenceLocation) {
        this(typeInfo, inType, sequenceLocation, false);
    }

    protected static class State<T, L extends ObjectLocation<T>, V> {
        private SequenceLocation<V> mapped;
        private final L element;
        private final IntLocation index;

        State(L element, IntLocation index, SequenceLocation<V> mapped) {
            this.element = element;
            this.index = index;
            this.mapped = mapped;
        }

        @SuppressWarnings("unchecked")
        private static<T, L extends ObjectLocation<T>, V> State<T, L, V>[] newArray(int size) {
            return (State<T, L, V>[]) new State[size];
        }

    }

    private Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.getAsSequence();
        state = new DumbMutableSequence<State<T, L, V>>(sequence.size());
        SequenceLocation<V>[] locationsArray = Util.newSequenceLocationArray(sequence.size());
        State<T, L, V>[] newStates = State.newArray(sequence.size());
        fillInNewValues(sequence, newStates, locationsArray, 0);
        state.replaceSlice(0, -1, newStates);
        underlying = new BoundCompositeSequence<V>(getElementType(), locationsArray);
        return underlying.getAsSequence();
    }

    private void fillInNewValues(Sequence<? extends T> sequence, State<T, L, V>[] newStates, SequenceLocation<V>[] locationsArray, int offset) {
        int i = 0;
        for (T value : sequence) {
            State<T, L, V> stateElt = makeState(i + offset, value);
            newStates[i] = stateElt;
            locationsArray[i++] = stateElt.mapped;
        }
    }

    protected abstract SequenceLocation<V> computeElements$(L elementLocation, IntLocation indexLocation);

    // This ugly and not typesafe construct eliminates ~10 classes, which add a lot to our static footprint.
    // Such optimizations are ugly but needed for smaller-memory platforms.
    @SuppressWarnings("unchecked")
    protected L makeInductionLocation(T value) {
            switch (inType.type) {
                case BOOLEAN:
                    return (L) BooleanVariable.make((Boolean) value);
                case BYTE:
                    return (L) ByteVariable.make((Byte) value);
                case CHAR:
                    return (L) CharVariable.make((Character) value);
                case DOUBLE:
                    return (L) DoubleVariable.make((Double) value);
                case FLOAT:
                    return (L) FloatVariable.make((Float) value);
                case INT:
                    return (L) IntVariable.make((Integer) value);
                case LONG:
                    return (L) LongVariable.make((Long) value);
                case SHORT:
                    return (L) ShortVariable.make((Short) value);
                default:
                    return (L) ObjectVariable.make((T) value);
            }
    }

    private State<T, L, V> makeState(int index, T value) {
        L elementLocation = makeInductionLocation(value);
        IntVariable indexLocation = useIndex ? IntVariable.make(index) : null;
        SequenceLocation<V> mapped = computeElements$(elementLocation, indexLocation);
        return new State<T, L, V>(elementLocation, indexLocation, mapped);
    }

    private void addTriggers() {
        underlying.addChangeListener(new SequenceChangeListener<V>() {
            public void onChange(int startPos, int endPos, Sequence<? extends V> newElements, Sequence<V> oldValue, Sequence<V> newValue) {
                AbstractBoundComprehension.this.updateSlice(startPos, endPos, newElements);
            }
        });

        sequenceLocation.addChangeListener(new SequenceChangeListener<T>() {
            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int insertedCount = Sequences.size(newElements);
                int deletedCount = endPos - startPos + 1;
                int netAdded = insertedCount - deletedCount;
                if (netAdded == 0) {
                    int i = startPos;
                    for (Iterator<? extends T> it = Sequences.iterator(newElements); it.hasNext(); i++) {
                        state.get(i).element.set(it.next());
                    }
                }
                else {
                    SequenceLocation<V>[] locationsArray = Util.newSequenceLocationArray(newElements.size());
                    State<T, L, V>[] newStates = State.newArray(newElements.size());
                    fillInNewValues(newElements, newStates, locationsArray, startPos);
                    underlying.replaceSlice(startPos, endPos, locationsArray);
                    state.replaceSlice(startPos, endPos, newStates);
                    if (useIndex) {
                        final int length = state.size();
                        for (int i = endPos + 1 + netAdded; i < length; i++)
                            state.get(i).index.set(i);
                    }
                }
            }
        });
    }
}
