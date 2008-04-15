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
import java.util.List;
import java.util.ArrayList;

import com.sun.javafx.runtime.location.*;

/**
 * Abstract base class for bound sequences.  Because bound sequences must be constructed in a valid
 * state, the constructor will call the abstract construct() method.  This means that construct() cannot
 * depend on any state initialized by the subclass constructor!  
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractLocation implements SequenceLocation<T> {
    private final Class<T> clazz;
    private List<SequenceChangeListener<T>> changeListeners;
    private Sequence<T> value;

    // AbstractBoundSequences start out in the invalid state, and go to the valid state exactly once,
    // and thereafter stay in the valid state.  They cannot be lazily bound.

    protected AbstractBoundSequence(Class<T> clazz) {
        this.clazz = clazz;
        this.value = Sequences.emptySequence(clazz);
    }

    /** Called after construction to compute the value of the sequence */
    protected abstract Sequence<T> computeValue();

    /** Called once after construction so that listeners may be registered */
    protected abstract void initialize();

    protected void ensureValid() {
        if (!isValid()) {
            Sequence<T> oldValue = value;
            Sequence<T> newValue = computeValue();
            if (newValue == null)
                newValue = Sequences.emptySequence(clazz);
            initialize();
            setValid();
            value = newValue;
            if (!Sequences.isEqual(oldValue, newValue)) {
                invalidateDependencies();
                notifyListeners(0, Sequences.size(oldValue)-1, newValue, oldValue, newValue);
            }
        }
    }

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        Sequence<T> oldValue = value;
        value = oldValue.replaceSlice(startPos, endPos, newValues);
        invalidateDependencies();
        notifyListeners(startPos, endPos, newValues, oldValue, value);
    }

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues, Sequence<T> newSequence) {
        Sequence<T> oldValue = value;
        value = newSequence;
        invalidateDependencies();
        notifyListeners(startPos, endPos, newValues, oldValue, newSequence);
    }

    protected Sequence<T> getRawValue() {
        return value;
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    public Sequence<T> get() {
        return getAsSequence();
    }

    public T get(int position) {
        return getAsSequence().get(position);
    }

    public Sequence<T> getAsSequence() {
        ensureValid();
        return value;
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    public void addChangeListener(final ObjectChangeListener<Sequence<T>> listener) {
        addChangeListener(new SequenceChangeListener<T>() {
            public void onChange(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    public void addChangeListener(SequenceChangeListener<T> listener) {
        if (changeListeners == null)
            changeListeners = new ArrayList<SequenceChangeListener<T>>();
        changeListeners.add(listener);
    }

    public void removeChangeListener(SequenceChangeListener<T> listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }

    private void notifyListeners(final int startPos, final int endPos,
                                 final Sequence<? extends T> newElements,
                                 final Sequence<T> oldValue, final Sequence<T> newValue) {
        if (changeListeners != null) {
            for (SequenceChangeListener<T> listener : changeListeners)
                listener.onChange(startPos, endPos, newElements, oldValue, newValue);
        }
    }

    public Iterator<T> iterator() {
        return getAsSequence().iterator();
    }

    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public T set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> set(Sequence<T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> setFromLiteral(Sequence<T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> setAsSequence(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> setAsSequenceFromLiteral(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        throw new UnsupportedOperationException();
    }

    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    public void deleteSlice(int startPos, int endPos) {
        throw new UnsupportedOperationException();
    }

    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    public void delete(SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    public void insert(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    public void insertFirst(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }
}
