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
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * SequenceHelper is a "mixin" for managing the value of a sequence-valued variable or binding.  It holds the value,
 * and manages change listeners, notification, and mutation.  It was born as AbstractSequenceLocation (hence some of
 * the strange contortions we have to go through).   
 *
 * @author Brian Goetz
 */
public abstract class SequenceHelper<T> {
    private Sequence<T> value;
    private final Class<T> clazz;
    private List<SequenceReplaceListener<T>> replaceListeners;


    public SequenceHelper(Class<T> clazz) {
        this.clazz = clazz;
        value = Sequences.emptySequence(clazz);
    }

    protected abstract void ensureValid();
    protected abstract boolean isInitialized();
    protected abstract void setValid();
    protected abstract void valueChanged();

    public Class<T> getClazz() {
        return clazz;
    }

    public Sequence<T> getRawValue() {
        return value;
    }

    public void setRawValue(Sequence<T> value) {
        this.value = value;
    }

    public Sequence<T> getAsSequence() {
        ensureValid();
        return value;
    }

    public Sequence<T> get() {
        return getAsSequence();
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    public void addChangeListener(final ObjectChangeListener<Sequence<T>> listener) {
        addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
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

    /** Update the held value, notifying change listeners */
    public Sequence<T> replaceValue(Sequence<T> newValue) {
        if (newValue == null)
            newValue = Sequences.emptySequence(clazz);
        return replaceSlice(0, Sequences.size(value)-1, newValue, newValue);
    }

    public Sequence<T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> newValue) {
        Sequence<T> oldValue = value;
        if (!equals(oldValue, newValue) || !isInitialized()) {
            value = newValue;
            setValid();
            if (isInitialized())
                notifyListeners(startPos, endPos, newElements, oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    @SuppressWarnings("unchecked")
    public void notifyListeners(final int startPos, final int endPos,
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
        if (isInitialized())
            notifyListeners(0, -1, value, Sequences.emptySequence(clazz), value);
    }


    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    public Iterator<T> iterator() {
        return getAsSequence().iterator();
    }

    public T get(int position) {
        return getAsSequence().get(position);
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }


    protected static boolean equals(Sequence a, Sequence b) {
        return ((a == null) && (b == null)) || ((a != null) && a.equals(b));
    }
}
