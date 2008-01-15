/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.runtime.location.SequenceReplaceListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private final SequenceLocation<? extends T>[] locations;
    private final int[] startPositions;

    BoundCompositeSequence(Class<T> clazz, SequenceLocation<? extends T>... locations) {
        super(clazz);
        this.locations = locations;
        this.startPositions = new int[locations.length];
    }

    protected Sequence<T> computeValue() {
        Sequence<? extends T>[] sequences = Util.newSequenceArray(locations.length);
        for (int i = 0, offset = 0; i < locations.length; i++) {
            sequences[i] = locations[i].getAsSequence();
            Class eClass = locations[i].getAsSequence().getElementType();
            if (!clazz.isAssignableFrom(eClass))
                throw new ClassCastException("cannot cast " + eClass.getName()
                        + " segment to " + clazz.getName() + " sequence");
            startPositions[i] = offset;
            offset += sequences[i].size();
        }
        return Sequences.concatenate(clazz, sequences);
    }

    protected void initialize() {
        class MyListener<V extends T> implements SequenceReplaceListener<V> {
            private final int index;

            private MyListener(int index) {
                this.index = index;
            }

            public void onReplace(int startPos, int endPos, Sequence<? extends V> newElements,
                                  Sequence<V> oldValue, Sequence<V> newValue) {
                int actualStart = startPositions[index] + startPos;
                int actualEnd = startPositions[index] + endPos;
                int delta = Sequences.size(newElements) - (endPos - startPos + 1);
                if (delta != 0)
                    for (int i = index + 1; i < startPositions.length; i++)
                        startPositions[i] += delta;
                updateSlice(actualStart, actualEnd, newElements);
            }
        }

        for (int i = 0; i < locations.length; i++)
            locations[i].addChangeListener(new MyListener(i));
    }
}
