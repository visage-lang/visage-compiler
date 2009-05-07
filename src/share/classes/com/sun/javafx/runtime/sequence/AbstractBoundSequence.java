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

import java.util.LinkedList;
import java.util.List;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.location.*;

/**
 * Abstract base class for bound sequences.  Subclass constructors are expected to compute the initial value, set up
 * any required triggers on dependent objects, and call the setInitialValue() method to provide a value.  (This places
 * limits on designing subclasses for further inheritance.)  The setInitialValue() method must be called exactly once.
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractLocation implements SequenceLocation<T> {
    protected final TypeInfo<T, ?> typeInfo;
    private List<ChangeListener<T>> changeListeners;
    private Sequence<? extends T> $value;
    protected final boolean lazy;

    // Currently, no support for lazy binding.

    protected AbstractBoundSequence(boolean lazy, TypeInfo<T, ?> typeInfo) {
        this.typeInfo = typeInfo;
        this.lazy = lazy;
        this.$value = typeInfo.emptySequence;
    }

    protected abstract Sequence<? extends T> computeValue();

    protected void setInitialValue(Sequence<? extends T> initialValue) {
        if (isValid())
            throw new IllegalStateException("Cannot call setInitialValue more than once");
        Sequence<? extends T> oldValue = $value;
        Sequence<? extends T> newValue = initialValue;
        if (newValue == null)
            newValue = typeInfo.emptySequence;
        $value = newValue;
        setValid();
        if (!Sequences.isEqual(oldValue, newValue)) {
            invalidateDependencies();
            notifyListeners(null, 0, Sequences.size(oldValue), newValue, oldValue, newValue);
        }
    }

    protected void updateSlice(int startPos, int endPos/*exclusive*/, Sequence<? extends T> newValues) {
        assert !lazy;
        ArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, $value);
        arr.replace(startPos, endPos, newValues, 0, newValues.size(), true);
        $value = arr;
        invalidateDependencies();
        notifyListeners(arr, startPos, endPos, newValues, null, $value);
        arr.clearOldValues(endPos-startPos);
    }

    protected void updateSlice(int startPos, int endPos/*exclusive*/, T newValue) {
        assert !lazy;
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, $value);
        if (newValue == null)
            arr.replace(startPos, endPos, typeInfo.emptySequence, 0, 0, true);
        else
            arr.replace(startPos, endPos, newValue, true);
        $value = arr;
        invalidateDependencies();
        notifyListeners(arr, startPos, endPos, null, null, $value);
        arr.clearOldValues(endPos-startPos);
    }

    protected void updateSlice(int startPos, int endPos,
            ArraySequence<T> srcBuffer, int srcStart, Sequence<? extends T> newElements) {
        Sequence<? extends T> srcElements;
        int srcEnd;
        if (newElements != null) {
            srcElements = newElements;
            srcStart = 0;
            srcEnd = newElements.size();
        }
        else {
            srcElements = srcBuffer;
            srcEnd = srcBuffer.gapStart;
        }
        ArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, $value);
        arr.replace(startPos, endPos, srcElements, srcStart, srcEnd, true);
        $value = arr;
        invalidateDependencies();
        notifyListeners(arr, startPos, endPos, newElements, null, $value);
        arr.clearOldValues(endPos-startPos);
    }

    protected Sequence<? extends T> getRawValue() {
        return $value;
    }

    protected void setRawValue(Sequence<? extends T> value) {
        $value = value;
    }

    public Sequence<? extends T> get() {
        return getAsSequence();
    }

    public T get(int position) {
        return getAsSequence().get(position);
    }

    public Sequence<? extends T> getAsSequence() {
        if (lazy)
            update();
        else
            assert(isValid());
        $value.incrementSharing();
        return $value;
    }

    public TypeInfo<T, ?> getElementType() {
        return typeInfo;
    }

    public Sequence<? extends T> getSlice(int startPos, int endPos) {
        return getAsSequence().getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return Sequences.size(getAsSequence()) == 0;
    }

    @Override
    protected boolean hasDependencies() {
        return super.hasDependencies() || changeListeners.size() > 0;
    }

    public void addChangeListener(final ChangeListener<Sequence<? extends T>> listener) {
        addSequenceChangeListener(new ChangeListener<T>() {
            public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
                Sequence<? extends T> newValue = buffer != null ? buffer : newElements;
                listener.onChange(oldValue, newValue);
            }
        });
    }

    public void addSequenceChangeListener(ChangeListener<T> listener) {
        if (changeListeners == null)
            changeListeners = new LinkedList<ChangeListener<T>>();
        changeListeners.add(listener);
    }

    public void removeSequenceChangeListener(ChangeListener<T> listener) {
        if (changeListeners != null)
            changeListeners.remove(listener);
    }

    protected void notifyListeners(ArraySequence<T> buffer, int startPos, int endPos,
                                 Sequence<? extends T> newElements,
                                 Sequence<? extends T> oldValue, Sequence<? extends T> newValue) {
        if (changeListeners != null) {
            for (ChangeListener<T> listener : changeListeners)
                listener.onChange(buffer, oldValue, startPos, endPos, newElements);
        }
    }

    @Override
    public void invalidate() {
        if (lazy)
            super.invalidate();
    }

    @Override
    public void update() {
        if (lazy) {
            Sequence<? extends T> oldValue = $value;
            $value = computeValue();
            setValid();
            if (hasDependencies() && !Sequences.isEqual(oldValue, $value))
              notifyListeners(null, 0, oldValue.size(), $value, oldValue, $value);
        }
    }

    @Override
    public String toString() {
        return getAsSequence().toString();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public T set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> set(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<? extends T> setAsSequence(Sequence<? extends T> value) {
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

    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    protected class InvalidateMeListener extends InvalidationListener {
        @Override
        public boolean onChange() {
            invalidate();
            return true;
        }
    }
}
