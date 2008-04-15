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
import com.sun.javafx.runtime.location.SequenceLocation;
import com.sun.javafx.runtime.location.SequenceChangeListener;

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

        public void addListener(IndexListener listener) {
            this.listener = listener;
            location.addChangeListener(this.listener);
        }

        public void removeListener() {
            location.removeChangeListener(this.listener);
            this.listener = null;
        }
    }

    public BoundCompositeSequence(Class<T> clazz, SequenceLocation<? extends T>... locations) {
        this(clazz, locations, locations.length);
    }

    public BoundCompositeSequence(Class<T> clazz, SequenceLocation<? extends T>[] locations, int size) {
        super(clazz);
        this.infos = newInfoArray(size);
        for (int i = 0; i < size; i++) {
            infos[i] = new Info<T>(locations[i]);
            Class eClass = locations[i].getAsSequence().getElementType();
            if (!clazz.isAssignableFrom(eClass))
                throw new ClassCastException("cannot cast " + eClass.getName()
                        + " segment to " + clazz.getName() + " sequence");
        }

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
        return Sequences.concatenate(getClazz(), sequences);
    }

    private void addTriggers() {
        for (int i = 0; i < infos.length; i++)
            infos[i].addListener(new MyListener(i));
    }

    public void replaceSlice(int startPos, int endPos, SequenceLocation<? extends T>[] newValues) {
        Info<T>[] newInfos = newInfoArray(newValues.length);
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
        int offset = affectedStart;
        for (int i = 0; i < newInfos.length; i++) {
            newInfos[i] = new Info<T>(newValues[i]);
            sequences[i] = newInfos[i].location.getAsSequence();
            newInfos[i].startPosition = offset;
            newInfos[i].size = sequences[i].size();
            offset += sequences[i].size();
            newInfos[i].addListener(new MyListener(i + startPos));
        }
        Sequence<T> newSlice = Sequences.concatenate(getClazz(), sequences);
        int deltaElements = newSlice.size() - (affectedEnd - affectedStart + 1);
        int deltaLocations = newValues.length - (endPos - startPos + 1);
        for (int i = endPos + 1; i < infos.length; i++) {
            infos[i].startPosition += deltaElements;
            infos[i].listener.setIndex(i + deltaLocations);
        }
        for (int i = startPos; i <= endPos; i++)
            infos[i].removeListener();
        infos = Util.replaceSlice(infos, startPos, endPos, newInfos);
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

    private interface IndexListener<T> extends SequenceChangeListener<T> {
        public void setIndex(int index);
    }

    private class MyListener<V extends T> implements IndexListener<V> {
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
