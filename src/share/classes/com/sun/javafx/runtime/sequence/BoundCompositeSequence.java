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

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.location.SequenceChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private Info<T>[] infos;

    static class Info<T> {
        private final SequenceLocation<? extends T> location;
        private int startPosition, size;
        private IndexListener listener;

        public Info(SequenceLocation<? extends T> location) {
            this.location = location;
        }

        public void addListener(IndexListener<T> listener) {
            this.listener = listener;
            location.addChangeListener(this.listener);
        }

        public void removeListener() {
            location.removeChangeListener(this.listener);
            this.listener = null;
        }
    }

    public BoundCompositeSequence(TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>... locations) {
        this(typeInfo, locations, locations.length);
    }

    public BoundCompositeSequence(TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>[] locations, int size) {
        super(typeInfo);
        this.infos = newInfoArray(size);
        for (int i = 0; i < size; i++)
            infos[i] = new Info<T>(locations[i]);

        setInitialValue(computeValue());
        addTriggers();
    }

    @SuppressWarnings("unchecked")
    private Info<T>[] newInfoArray(int len) {
        return (Info<T>[]) new Info[len];
    }

    private Sequence<T> computeValue() {
        Sequence<? extends T>[] sequences = Util.newSequenceArray(infos.length);
        for (int i = 0, offset = 0; i < infos.length; i++) {
            sequences[i] = infos[i].location.getAsSequence();
            infos[i].startPosition = offset;
            infos[i].size = sequences[i].size();
            offset += sequences[i].size();
        }
        return Sequences.concatenate(getElementType(), sequences);
    }

    private void addTriggers() {
        for (int i = 0; i < infos.length; i++)
            infos[i].addListener(new MyListener(i));
    }

    public void replaceSlice(int startPos, int endPos, SequenceLocation<? extends T>[] newValues) {
        Sequence<? extends T>[] sequences = Util.newSequenceArray(newValues.length);
        int affectedStart, affectedEnd;
        if (startPos < infos.length) {
            affectedStart = infos[startPos].startPosition;
            affectedEnd = (endPos >= 0) ? (infos[endPos].startPosition + infos[endPos].size - 1) : affectedStart - 1;
        }
        else {
            affectedStart = getRawValue().size();
            affectedEnd = affectedStart - 1;
        }
        for (int i = startPos; i <= endPos; i++)
            infos[i].removeListener();
        int insertedCount = newValues.length;
        int deletedCount = endPos - startPos + 1;
        int deltaLocations = insertedCount - deletedCount;
        if (deltaLocations != 0) {
            @SuppressWarnings("unchecked")
            Info<T>[] temp = (Info<T>[]) new Info[infos.length + deltaLocations];
            System.arraycopy(infos, 0, temp, 0, startPos);
            System.arraycopy(infos, endPos + 1, temp, startPos + insertedCount, infos.length - (endPos + 1));
            infos = temp;
        }
        int offset = affectedStart;
        int newSize = 0;
        for (int i = 0; i < insertedCount; i++) {
            infos[i + startPos] = new Info<T>(newValues[i]);
            sequences[i] = newValues[i].getAsSequence();
            infos[i + startPos].startPosition = offset;
            int sz = sequences[i].size();
            infos[i + startPos].size = sz;
            offset += sz;
            newSize += sz;
            infos[i + startPos].addListener(new MyListener(i + startPos));
        }
        Sequence<T> newSlice = Sequences.concatenate(getElementType(), sequences);
        int deltaElements = newSize - (affectedEnd - affectedStart + 1);
        for (int i = endPos + deltaLocations + 1; i < infos.length; i++) {
            infos[i].startPosition += deltaElements;
            infos[i].listener.setIndex(i);
        }
        updateSlice(affectedStart, affectedEnd, newSlice);
    }

    public void validate() {
        // Uncomment for debugging
//        int offset = 0;
//        for (int i=0; i<infos.length; i++) {
//            Info info = infos[i];
//            Assert.assertEquals(offset, info.startPosition);
//            Assert.assertTrue(info.startPosition >= 0);
//            Assert.assertTrue(info.size >= 0);
//            Assert.assertEquals(info.size, info.location.getAsSequence().size());
//            Assert.assertEquals(value().getSlice(info.startPosition, info.startPosition + info.size - 1), info.location.getAsSequence());
//            Assert.assertEquals(((MyListener) info.listener).index, i);
//            offset += info.size;
//        }
//        Assert.assertEquals(offset, value().size());
    }

    private static abstract class IndexListener<T> extends SequenceChangeListener<T> {
        public abstract void setIndex(int index);
    }

    private class MyListener<V extends T> extends  IndexListener<V> {
        private int index;

        private MyListener(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void onChange(int startPos, int endPos, Sequence<? extends V> newElements,
                              Sequence<V> oldValue, Sequence<V> newValue) {
            int actualStart = infos[index].startPosition + startPos;
            int actualEnd = infos[index].startPosition + endPos;
            infos[index].size = newValue.size();
            int delta = Sequences.size(newElements) - (endPos - startPos + 1);
            if (delta != 0)
                for (int i = index + 1; i < infos.length; i++)
                    infos[i].startPosition += delta;
            updateSlice(actualStart, actualEnd, newElements);
        }
    }
}
