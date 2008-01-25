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

import java.util.BitSet;

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceReplaceListener;

/**
 * BoundComprehension -- one-dimensional bound list comprehension, supporting where clauses and variable number of
 * elements per iteration
 *
 * @author Brian Goetz
 */
class BoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final SequencePredicate<T> filter;
    private final ElementGenerator<T, V> singleGenerator;
    private final MultiElementGenerator<T, V> multiGenerator;
    private BitSet bits;
    private int[] numElements;

    public interface ElementGenerator<T, V> {
        public V getValue(T element, int index);
    }

    public interface MultiElementGenerator<T, V> {
        public V getValue(T element, int index);
    }

    public BoundComprehension(Class<V> clazz,
                              SequenceLocation<T> sequenceLocation,
                              SequencePredicate<T> filter,
                              ElementGenerator<T, V> generator) {
        super(clazz);
        this.sequenceLocation = sequenceLocation;
        this.filter = filter;
        this.singleGenerator = generator;
        this.multiGenerator = null;
    }

    public BoundComprehension(Class<V> clazz,
                              SequenceLocation<T> sequenceLocation,
                              SequencePredicate<T> filter,
                              MultiElementGenerator<T, V> generator) {
        super(clazz);
        this.sequenceLocation = sequenceLocation;
        this.filter = filter;
        this.singleGenerator = null;
        this.multiGenerator = generator;
    }

    protected Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.getAsSequence();
        bits = new BitSet(sequence.size());
        numElements = new int[sequence.size()];
        SequenceBuilder<V> sb = new SequenceBuilder<V>(clazz, sequence.size());
        for (int i = 0; i < sequence.size(); i++) {
            T value = sequence.get(i);
            if (filter.matches(sequence, i, value)) {
                bits.set(i);
                if (singleGenerator != null) {
                    sb.add(singleGenerator.getValue(value, i));
                    numElements[i] = 1;
                }
                else {
                    V values = multiGenerator.getValue(value, i);
                    sb.add(values);
                    numElements[i] = Sequences.size(values);
                }
            }
        }
        return sb.toSequence();
    }

    protected void initialize() {
        sequenceLocation.addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                V[] ourNewElements = Util.<V>newObjectArray(Sequences.size(newElements));
                for (int i = 0; i < ourNewElements.length; i++)
                    ourNewElements[i] = singleGenerator.getValue(newElements.get(i), i);
                Sequence<V> vSequence = Sequences.make(clazz, ourNewElements);
                updateSlice(startPos, endPos, vSequence);
            }
        });
    }

}
