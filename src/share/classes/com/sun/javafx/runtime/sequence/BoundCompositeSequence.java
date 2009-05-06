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
import com.sun.javafx.runtime.location.InvalidationListener;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.SequenceLocation;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private Info<T>[] infos;

    class Info<V extends T> extends ChangeListener<V> {
        private final SequenceLocation<V> location;
        private int startPosition, size, index;

        public Info(int index, SequenceLocation<V> location) {
            this.location = location;
            this.index = index;
        }

        public void addListener() {
            location.addSequenceChangeListener(this);
        }

        public void removeListener() {
            location.removeSequenceChangeListener(this);
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void onChange(ArraySequence<V> buffer, Sequence<? extends V> oldValue,
                int startPos, int endPos, Sequence<? extends V> newElements) {
            int actualStart = startPosition + startPos;
            int actualEnd = startPosition + endPos;
            int insertedSize = Sequences.sizeOfNewElements(buffer, startPos, newElements);
            int oldSize = Sequences.sizeOfOldValue(buffer, oldValue, endPos);
            int delta = insertedSize - (endPos - startPos);
            size = oldSize + delta;
            if (delta != 0) {
                Info<T>[] tinfos = infos;
                int ninfos = tinfos.length;
                for (int i = index + 1; i < ninfos; i++)
                    tinfos[i].startPosition += delta;
            }
            updateSlice(actualStart, actualEnd, (ArraySequence<T>) buffer, startPos, newElements);
        }
    }

    public BoundCompositeSequence(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>... locations) {
        this(lazy, typeInfo, locations, locations.length);
    }

    public BoundCompositeSequence(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>[] locations, int size) {
        super(lazy, typeInfo);
        this.infos = newInfoArray(size);
        for (int i = 0; i < size; i++)
            infos[i] = new Info(i, locations[i]);
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
    }

    @SuppressWarnings("unchecked")
    private Info[] newInfoArray(int len) {
        return (Info[]) new Info[len];
    }

    protected Sequence<? extends T> computeValue() {
       ObjectArraySequence<T> buffer = new ObjectArraySequence(getElementType());
        for (int i = 0, offset = 0; i < infos.length; i++) {
            Sequence<? extends T> seq = infos[i].location.getAsSequence();
            infos[i].startPosition = offset;
            int ssize = seq.size();
            infos[i].size = ssize;
            offset += ssize;
            buffer.add(seq);
        }
        return buffer;
    }

    private void addTriggers() {
        InvalidateMeListener invalidate = null;
        for (int i = 0; i < infos.length; i++) {
            if (lazy) {
                if (invalidate == null)
                     invalidate = new InvalidateMeListener();
            }
            else
                infos[i].addListener();
        }
    }

    public void replaceSlice(int startPos, int endPos, SequenceLocation<? extends T>[] newValues) {
        int insertedCount = newValues.length;
        int affectedStart, affectedEnd;
        if (startPos < infos.length) {
            affectedStart = infos[startPos].startPosition;
            affectedEnd = (endPos > 0) ? (infos[endPos-1].startPosition + infos[endPos-1].size) : 0;
        }
        else {
            affectedStart = getRawValue().size();
            affectedEnd = affectedStart;
        }
        for (int i = startPos; i < endPos; i++)
            infos[i].removeListener();
        int deletedCount = endPos - startPos;
        int deltaLocations = insertedCount - deletedCount;
        if (deltaLocations != 0) {
            @SuppressWarnings("unchecked")
            Info<T>[] temp = (Info<T>[]) new Info[infos.length + deltaLocations];
            System.arraycopy(infos, 0, temp, 0, startPos);
            System.arraycopy(infos, endPos, temp, startPos + insertedCount, infos.length - endPos);
            infos = temp;
        }
        int offset = affectedStart;
        int newSize = 0;
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, getRawValue());
        for (int i = 0; i < insertedCount; i++) {
            int index = i + startPos;
            infos[index] = new Info(index, newValues[i]);
            Sequence<? extends T> seq = newValues[i].getAsSequence();
            infos[index].startPosition = offset;
            int sz = seq.size();
            arr.insert(seq, sz, offset);
            infos[index].size = sz;
            offset += sz;
            newSize += sz;
            infos[index].addListener();
        }
        int deltaElements = newSize - (affectedEnd - affectedStart);
        for (int i = endPos + deltaLocations; i < infos.length; i++) {
            infos[i].startPosition += deltaElements;
            infos[i].setIndex(i);
        }
        arr.replace(affectedStart+newSize, affectedEnd+newSize, typeInfo.emptySequence, 0, 0, true);
        setRawValue(arr);
        invalidateDependencies();
        notifyListeners(arr, affectedStart, affectedEnd, null, null, arr);
        arr.clearOldValues(affectedEnd-affectedStart);
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
}
