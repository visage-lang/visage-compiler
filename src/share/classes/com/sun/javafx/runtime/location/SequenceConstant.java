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

package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.TypeInfo;

/**
 * SequenceConstant
 *
 * @author Brian Goetz
 */
public class SequenceConstant<T> extends AbstractConstantLocation<Sequence<T>> implements SequenceLocation<T> {
    private final TypeInfo<T> typeInfo;
    private Sequence<T> $value;

    public static<T> SequenceLocation<T> make(TypeInfo<T> typeInfo, Sequence<T> value) {
        return new SequenceConstant<T>(typeInfo, value);
    }

    protected SequenceConstant(TypeInfo<T> typeInfo, Sequence<T> value) {
        this.typeInfo = typeInfo;
        this.$value = value;
    }


    public Sequence<T> getAsSequence() {
        return $value;
    }

    public TypeInfo<T> getElementType() {
        return typeInfo;
    }

    public Sequence<T> get() {
        return $value;
    }

    public Sequence<T> setAsSequence(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> setAsSequenceFromLiteral(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(SequenceChangeListener<T> listener) { }

    public void removeChangeListener(SequenceChangeListener<T> listener) { }

    public T get(int position) {
        return $value.get(position);
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return $value.getSlice(startPos, endPos);
    }

    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
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

    public void delete(SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(T value, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    public T set(int position, T value) {
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

    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }
}
