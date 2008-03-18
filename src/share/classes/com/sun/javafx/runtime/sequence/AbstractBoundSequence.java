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

import com.sun.javafx.runtime.location.*;

/**
 * AbstractBoundSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractLocation implements SequenceLocation<T> {

    private final SequenceHelper<T> helper;

    // AbstractBoundSequences start out in the invalid state, and go to the valid state exactly once,
    // and thereafter stay in the valid state.  They cannot be lazily bound.

    protected AbstractBoundSequence(Class<T> clazz) {
        helper = new SequenceHelper<T>(clazz) {
            protected void ensureValid() {
                if (!isValid()) {
                    Sequence<T> value = computeValue();
                    initialize();
                    AbstractBoundSequence.this.setValid();
                    helper.replaceValue(value);
                }
            }

            protected boolean isInitialized() {
                return isValid();
            }

            protected void setValid() {
                AbstractBoundSequence.this.setValid();
            }

            protected void valueChanged() {
                AbstractBoundSequence.this.valueChanged();
            }
        };
    }

    /** Called after construction to compute the value of the sequence */
    protected abstract Sequence<T> computeValue();

    /** Called once after construction so that listeners may be registered */
    protected abstract void initialize();

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        Sequence<T> oldValue = helper.getRawValue();
        helper.setRawValue(oldValue.replaceSlice(startPos, endPos, newValues));
        helper.notifyListeners(startPos, endPos, newValues, oldValue, helper.getRawValue());
        valueChanged();
    }

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues, Sequence<T> newSequence) {
        Sequence<T> oldValue = helper.getRawValue();
        helper.setRawValue(newSequence);
        helper.notifyListeners(startPos, endPos, newValues, oldValue, newSequence);
        valueChanged();
    }

    protected Sequence<T> value() {
        return helper.getRawValue();
    }

    protected Class<T> getClazz() {
        return helper.getClazz();
    }

    public Sequence<T> get() {
        return helper.get();
    }

    public T get(int position) {
        return helper.get(position);
    }

    public Sequence<T> getAsSequence() {
        return helper.getAsSequence();
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return helper.getSlice(startPos, endPos);
    }

    public boolean isNull() {
        return helper.isNull();
    }

    public void addChangeListener(SequenceChangeListener<T> listener) {
        helper.addChangeListener(listener);
    }

    public void addChangeListener(ObjectChangeListener<Sequence<T>> listener) {
        helper.addChangeListener(listener);
    }

    public void removeChangeListener(SequenceChangeListener<T> listener) {
        helper.removeChangeListener(listener);
    }

    public Iterator<T> iterator() {
        return helper.iterator();
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
