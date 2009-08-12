/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.javafx.runtime.location.InvalidateMeListener;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.Util;

/**
 * BoundCompositeSequence
 *
 * @author Brian Goetz
 */
public class BoundCompositeSequence<T> extends AbstractBoundSequence<T> implements SequenceLocation<T> {
    private static final int DEFAULT_SIZE = 8;
    private Info<T, ? extends T>[] infos;
    int numinfos;

    private static class Info<T, V extends T> extends WeakMeChangeListener<V> {
        // Exactly one of location or location1 is non-null.
        private final SequenceLocation<V> location;
        // An optimization - instead of using a BoundSingletonSequence.
        private final ObjectLocation<V> location1;
        private int startPosition, size, index;

        public Info(BoundCompositeSequence<T> bcs, int index, SequenceLocation<V> location) {
            super(bcs);
            this.location = location;
            this.location1 = null;
            this.index = index;
        }

        public Info(BoundCompositeSequence<T> bcs, int index, ObjectLocation<V> location) {
            super(bcs);
            this.location1 = location;
            this.location = null;
            this.index = index;
        }

        public void addListener() {
            if (location1 != null)
                location1.addChangeListener(this);
            else
                location.addSequenceChangeListener(this);
        }

        public void removeListener() {
            // Only called from replaceSlice, which is only called from
            // AbstractBoundComprehension - i.e. we know location!=null.
            location.removeSequenceChangeListener(this);
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void onChange(V oldValue, V newValue) {
            onChangeB(oldValue, newValue);
        }

        @Override
        public boolean onChangeB(V oldValue, V newValue) {
            BoundCompositeSequence<T> bcs = (BoundCompositeSequence<T>) get();
            if (bcs != null) {
                int actualStart = startPosition;
                int replacedSize = oldValue == null ? 0 : 1;
                int insertedSize = newValue == null ? 0 : 1;
                int delta = insertedSize - replacedSize;
                size = insertedSize;
                if (delta != 0) {
                    Info<T, ? extends T>[] tinfos = bcs.infos;
                    int ninfos = bcs.numinfos;
                    for (int i = index + 1; i < ninfos; i++)
                        tinfos[i].startPosition += delta;
                }
                bcs.updateSlice(actualStart, actualStart+replacedSize, newValue);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onChange(ArraySequence<V> buffer, Sequence<? extends V> oldValue,
                int startPos, int endPos, Sequence<? extends V> newElements) {
            onChangeB(buffer, oldValue, startPos, endPos, newElements);
        }

        @Override
        public boolean onChangeB(ArraySequence<V> buffer, Sequence<? extends V> oldValue,
                int startPos, int endPos, Sequence<? extends V> newElements) {
            BoundCompositeSequence<T> bcs = (BoundCompositeSequence<T>) get();
            if (bcs != null) {
                int actualStart = startPosition + startPos;
                int actualEnd = startPosition + endPos;

                int insertedSize = Sequences.sizeOfNewElements(buffer, startPos, newElements);
                int oldSize = Sequences.sizeOfOldValue(buffer, oldValue, endPos);
                int delta = insertedSize - (endPos - startPos);
                size = oldSize + delta;
                if (delta != 0) {
                    Info<T, ? extends T>[] tinfos = bcs.infos;
                    int ninfos = bcs.numinfos;
                    for (int i = index + 1; i < ninfos; i++)
                        tinfos[i].startPosition += delta;
                }
                bcs.updateSlice(actualStart, actualEnd, (ArraySequence<T>) buffer, startPos, newElements);
                return true;
            } else {
                return false;
            }
        }
    }

    public BoundCompositeSequence(boolean lazy, TypeInfo<T, ?> typeInfo) {
        this(lazy, DEFAULT_SIZE, typeInfo);
    }

    public BoundCompositeSequence(boolean lazy, int initialSize, TypeInfo<T, ?> typeInfo) {
        super(lazy, typeInfo);
        this.infos = newInfoArray(initialSize);
    }

    public BoundCompositeSequence(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>... locations) {
        this(lazy, typeInfo, locations, locations.length);
    }

    public BoundCompositeSequence(boolean lazy, TypeInfo<T, ?> typeInfo, SequenceLocation<? extends T>[] locations, int size) {
        super(lazy, typeInfo);
        this.infos = newInfoArray(size);
        this.numinfos = size;
        for (int i = 0; i < size; i++)
            infos[i] = new Info(this, i, locations[i]);
        toSequence();
    }

    private void ensureSize(int newSize) {
        if (infos.length < newSize) {
            int newCapacity = Util.powerOfTwo(numinfos, newSize);
            Info<T, ? extends T>[] newArray = newInfoArray(newCapacity);
            System.arraycopy(infos, 0, newArray, 0, numinfos);
            infos = newArray;
        }
    }

    /** Add an existing SequenceLocation, which will be flattened */
    public void add(SequenceLocation<? extends T> seq) {
        int i = numinfos;
        ensureSize(i + 1);
        infos[i] = new Info(this, i, seq);
        numinfos = i+1;
    }

    /** Add an instance location to the sequence */
    public void add(ObjectLocation<? extends T> singleton) {
        int i = numinfos;
        ensureSize(i + 1);
        infos[i] = new Info(this, i, singleton);
        numinfos = i+1;
    }

    public SequenceLocation<T> toSequence() {
        if (!lazy)
            setInitialValue(computeValue());
        addTriggers();
        return this;
    }

    @SuppressWarnings("unchecked")
    private Info[] newInfoArray(int len) {
        return (Info[]) new Info[len];
    }

    protected Sequence<? extends T> computeValue() {
       ObjectArraySequence<T> buffer = new ObjectArraySequence(getElementType());
        for (int i = 0, offset = 0; i < numinfos; i++) {
            Info<T, ? extends T> info = infos[i];
            info.startPosition = offset;
            if (info.location != null) {
                Sequence<? extends T> seq = info.location.getAsSequence();
                int ssize = seq.size();
                info.size = ssize;
                offset += ssize;
                buffer.add(seq);
            }
            else { // Singleton
                T val = info.location1.get();
                int ssize = val == null ? 0 : 1;
                info.size = ssize;
                offset += ssize;
                buffer.add(val);
            }
        }
        return buffer;
    }

    private void addTriggers() {
        InvalidateMeListener invalidate = null;
        for (int i = 0; i < numinfos; i++) {
            if (lazy) {
                if (invalidate == null)
                     invalidate = new InvalidateMeListener(this);
            }
            else
                infos[i].addListener();
        }
    }

    public void replaceSlice(int startPos, int endPos, SequenceLocation<? extends T>[] newValues) {
        int insertedCount = newValues.length;
        int affectedStart, affectedEnd;
        if (startPos < numinfos) {
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
            Info<T, ? extends T>[] temp = (Info<T, ? extends T>[]) new Info[numinfos + deltaLocations];
            System.arraycopy(infos, 0, temp, 0, startPos);
            System.arraycopy(infos, endPos, temp, startPos + insertedCount, numinfos - endPos);
            infos = temp;
            numinfos = numinfos + deltaLocations;
        }
        int offset = affectedStart;
        int newSize = 0;
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, getRawValue());
        for (int i = 0; i < insertedCount; i++) {
            int index = i + startPos;
            infos[index] = new Info(this, index, newValues[i]);
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
        for (int i = endPos + deltaLocations; i < numinfos; i++) {
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
//        for (int i=0; i<numinfos; i++) {
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
