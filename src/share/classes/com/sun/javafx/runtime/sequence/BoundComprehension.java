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

import java.util.Iterator;

import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceExpression;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceReplaceListener;
import com.sun.javafx.runtime.location.Location;

/**
 * BoundComprehension -- one-dimensional bound list comprehension, supporting where clauses and variable number of
 * elements per iteration
 *
 * @author Brian Goetz
 */
public abstract class BoundComprehension<T, V> extends AbstractBoundSequence<V> implements SequenceLocation<V> {
    private final SequenceLocation<T> sequenceLocation;
    private final boolean useIndex;
    private Sequence<ComprehensionElement> locations;
    private BoundCompositeSequence<V> underlying;

    public BoundComprehension(Class<V> clazz,
                              SequenceLocation<T> sequenceLocation,
                              boolean useIndex) {
        super(clazz);
        this.sequenceLocation = sequenceLocation;
        this.useIndex = useIndex;
    }

    public interface ComprehensionElement<T, V> extends SequenceLocation<V> {
        public void setElement(T element);
        public void setIndex(int index);
    }

    public static abstract class AbstractComprehensionElement<T, V> extends SequenceExpression<V> implements ComprehensionElement<T, V> {
        protected AbstractComprehensionElement(Class<V> clazz, Location... dependencies) {
            super(clazz, false, dependencies);
        }
    }
    
    protected abstract ComprehensionElement<T, V> processElement$(T element, int index);

    protected Sequence<V> computeValue() {
        Sequence<T> sequence = sequenceLocation.getAsSequence();
        ComprehensionElement<T, V>[] locationsArray = getComprehensionElements(sequence, 0);
        locations = Sequences.make(ComprehensionElement.class, locationsArray);
        underlying = new BoundCompositeSequence<V>(clazz, locationsArray);
        return underlying.getAsSequence();
    }

    private ComprehensionElement<T, V>[] getComprehensionElements(Sequence<? extends T> sequence, int initialIndex) {
        ComprehensionElement<T, V>[] locationsArray = Util.<ComprehensionElement<T, V>>newArray(ComprehensionElement.class, sequence.size());
        int index = initialIndex;
        for (Iterator<? extends T> iterator = sequence.iterator(); iterator.hasNext(); index++) {
            T element = iterator.next();
            locationsArray[index-initialIndex] = processElement$(element, index);
        }
        return locationsArray;
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
                    for (int i=startPos; i<=endPos; i++) {
                        ComprehensionElement<T, V> ce = (ComprehensionElement<T, V>) locations.get(i);
                        ce.setElement(newElements.get(i-startPos));
                        ce.invalidate();
                    }
                }
                else {
                    ComprehensionElement<T, V>[] locationsArray = getComprehensionElements(newElements, startPos);
                    if (useIndex) {
                        for (int i=endPos+1; i<locations.size(); i++) {
                            ComprehensionElement<T, V> ce = (ComprehensionElement<T, V>) locations.get(i);
                            ce.setIndex(i);
                            ce.invalidate();
                        }
                    }
                    underlying.replaceSlice(startPos, endPos, locationsArray);
                    locations = locations.replaceSlice(startPos, endPos, Sequences.make(ComprehensionElement.class, locationsArray));
                }
            }
        });
    }
}
