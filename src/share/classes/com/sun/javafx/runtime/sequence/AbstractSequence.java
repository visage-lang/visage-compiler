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

import com.sun.javafx.runtime.sequence.SequenceMutator.Listener;

import java.util.BitSet;
import java.util.Iterator;

/**
 * Abstract base class for sequence classes.  A subclass need only define the size() and get() methods; subclasses
 * may also want to provide optimized versions of some other methods, such as toArray() or getBits().  Subclasses that
 * represent views onto other sequences should also implement the getDepth() method.
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequence<T> implements Sequence<T> {
    protected final Class<T> clazz;

    protected AbstractSequence(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract int size();

    public abstract T get(int position);

    public BitSet getBits(SequencePredicate<? super T> predicate) {
        int length = size();
        BitSet bits = new BitSet(length);
        for (int i = 0; i < length; i++)
            if (predicate.matches(this, i, get(i)))
                bits.set(i);
        return bits;
    }

    public Class<T> getElementType() {
        return clazz;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public int getDepth() {
        return 0;
    }

    public <V> Sequence<V> map(Class<V> clazz, SequenceMapper<T, V> sequenceMapper) {
        return Sequences.map(clazz, this, sequenceMapper);
    }

    public void toArray(Object[] array, int destOffset) {
        for (int i = 0; i < size(); i++)
            array[i + destOffset] = get(i);
    }

    public Sequence<T> get(SequencePredicate<? super T> predicate) {
        return Sequences.filter(this, getBits(predicate));
    }


    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int next = 0;

            public boolean hasNext() {
                return next < size();
            }

            public T next() {
                if (next >= size())
                    throw new IndexOutOfBoundsException();
                else
                    return get(next++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (!(obj instanceof Sequence))
            return false;
        Sequence other = (Sequence) obj;
        return (other.getElementType().isAssignableFrom(getElementType())) && isEqual((Sequence<T>) other);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < size(); i++) {
            T val = get(i);
            hash = 31 * hash + (val != null ? val.hashCode() : 0);
        }
        return hash;
    }

    public boolean isEqual(Sequence<T> other) {
        if (other == null)
            return false;
        if (size() != other.size())
            return false;
        for (int i = 0; i < size(); i++) {
            T mine = get(i);
            T theirs = other.get(i);
            if (mine == null)
                return (theirs == null);
            else if (!mine.equals(theirs))
                return false;
        }
        return true;
    }


    @Override
    public String toString() {
        if (isEmpty())
            return "[ ]";
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append(" ]");
        return sb.toString();
    }


    public Sequence<T> subsequence(int start, int end) {
        return Sequences.subsequence(this, start, end);
    }

    public Sequence<T> reverse() {
        return Sequences.reverse(this);
    }

    public Sequence<T> flatten() {
        if (getDepth() == 0)
            return this;
        else {
            SequenceBuilder<T> sb = new SequenceBuilder<T>(getElementType());
            for (T t : this)
                sb.add(t);
            return sb.toSequence();
        }
    }

    public final Sequence<T> set(int position, T value) {
        return SequenceMutator.set(this, null, position, value);
    }

    public final Sequence<T> delete(int position) {
        return SequenceMutator.delete(this, null, position);
    }

    public final Sequence<T> delete(SequencePredicate<? super T> predicate) {
        return SequenceMutator.delete(this, (Listener<T>) null, predicate);
    }

    public final Sequence<T> insert(T value) {
        return SequenceMutator.insert(this, (Listener<T>) null, value);
    }

    public final Sequence<T> insert(Sequence<? extends T> values) {
        return SequenceMutator.insert(this, (Listener<T>) null, values);
    }

    public final Sequence<T> insertFirst(T value) {
        return SequenceMutator.insertFirst(this, null, value);
    }

    public final Sequence<T> insertFirst(Sequence<? extends T> values) {
        return SequenceMutator.insertFirst(this, (Listener<T>) null, values);
    }

    public final Sequence<T> insertBefore(T value, int position) {
        return SequenceMutator.insertBefore(this, null, value, position);
    }

    public final Sequence<T> insertBefore(Sequence<? extends T> values, int position) {
        return SequenceMutator.<T>insertBefore(this, null, values, position);
    }

    public final Sequence<T> insertAfter(T value, int position) {
        return SequenceMutator.insertAfter(this, null, value, position);
    }

    public final Sequence<T> insertAfter(Sequence<? extends T> values, int position) {
        return SequenceMutator.<T>insertAfter(this, null, values, position);
    }

    public final Sequence<T> insertBefore(T value, SequencePredicate<? super T> predicate) {
        return SequenceMutator.insertBefore(this, null, value, predicate);
    }

    public final Sequence<T> insertBefore(Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        return SequenceMutator.insertBefore(this, null, values, predicate);
    }

    public final Sequence<T> insertAfter(T value, SequencePredicate<? super T> predicate) {
        return SequenceMutator.insertAfter(this, null, value, predicate);
    }

    public final Sequence<T> insertAfter(Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        return SequenceMutator.insertAfter(this, null, values, predicate);
    }
}
