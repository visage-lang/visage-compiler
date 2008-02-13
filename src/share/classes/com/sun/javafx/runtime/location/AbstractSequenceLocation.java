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

package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * AbstractSequenceLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequenceLocation<T> extends AbstractLocation implements SequenceLocation<T> {
    protected Sequence<T> $value;
    protected final Class<T> clazz;
    protected List<SequenceReplaceListener<T>> replaceListeners;

    public AbstractSequenceLocation(Class<T> clazz, boolean valid) {
        super(valid);
        this.clazz = clazz;
        $value = Sequences.emptySequence(clazz);
    }

    protected static boolean equals(Sequence a, Sequence b) {
        return ((a == null) && (b == null)) || ((a != null) && a.equals(b));
    }

    public void addChangeListener(SequenceReplaceListener<T> listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<SequenceReplaceListener<T>>();
        replaceListeners.add(listener);
    }

    public void removeChangeListener(SequenceReplaceListener<T> listener) {
        if (replaceListeners != null)
            replaceListeners.remove(listener);
    }

    public void addChangeListener(final SequenceChangeListener<T> listener) {
        addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int newSize = Sequences.size(newElements);
                if (endPos-startPos+1 == newSize && newSize == 1) {
                    for (int i=startPos; i<=endPos; i++)
                        listener.onReplace(i, oldValue.get(i), newValue.get(i));
                }
                else {
                    for (int i=endPos; i >= startPos; i--)
                        listener.onDelete(i, oldValue.get(i));
                    for (int i=0; i<newSize; i++)
                        listener.onInsert(startPos+i, newElements.get(i));
                }

            }
        });
    }

    /**
     * Update the held value, notifying change listeners
     */
    protected Sequence<T> replaceValue(Sequence<T> newValue) {
        if (newValue == null)
            newValue = Sequences.emptySequence(clazz);
        return replaceSlice(0, Sequences.size($value)-1, newValue, newValue);
    }

    protected Sequence<T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> newValue) {
        Sequence<T> oldValue = $value;
        if (!equals(oldValue, newValue)) {
            $value = newValue;
            setValid();
            notifyListeners(startPos, endPos, newElements, oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    @SuppressWarnings("unchecked")
    protected void notifyListeners(final int startPos, final int endPos,
                                   final Sequence<? extends T> newElements,
                                   final Sequence<T> oldValue, final Sequence<T> newValue) {
        if (endPos - startPos + 1 == 0 && Sequences.size(newElements) == 0)
            return;
        valueChanged();
        if (replaceListeners != null) {
            for (SequenceReplaceListener<T> listener : replaceListeners)
                listener.onReplace(startPos, endPos, newElements, oldValue, newValue);
        }
    }

    public void fireInitialTriggers() {
        if (Sequences.size($value) != 0)
            notifyListeners(0, -1, $value, Sequences.emptySequence(clazz), $value);
    }
    
    @Override
    public String toString() {
        return $value.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return $value.iterator();
    }

    @Override
    public T get(int position) {
        return $value.get(position);
    }

    @Override
    public Sequence<T> getAsSequence() {
        return $value;
    }

    @Override
    public Sequence<T> setAsSequence(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sequence<T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    @Override
    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSlice(int startPos, int endPos) {
        replaceSlice(startPos, endPos, null);
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }
}
