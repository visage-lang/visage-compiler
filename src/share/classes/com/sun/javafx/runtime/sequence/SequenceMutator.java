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

import java.util.BitSet;

/**
 * Helper methods for modifying sequences and notifying sequence change listeners.  The helper methods only call the
 * onInsert/onDelete/onReplace methods; if the underlying sequence is modified then the caller is responsible for
 * calling the onChanged() method.
 *
 * @author Brian Goetz
 */
public class SequenceMutator {

    public interface Listener<T> {
        public void onReplaceSequence(Sequence<T> newSeq);

        public void onInsert(int position, T element);

        public void onDelete(int position, T element);

        public void onReplaceElement(int position, T oldValue, T newValue);
    }

    // Inhibit instantiation
    private SequenceMutator() {
    }

    /**
     * Modify the element at the specified position.  If the position is out of range, the sequence is not
     * modified.
     */
    public static <T> Sequence<T> set(Sequence<T> target, Listener<T> listener, int position, T value) {
        int size = target.size();
        if (position < 0 || position >= size)
            return target;
        else {
            Sequence<T> newElement = Sequences.singleton(target.getElementType(), value);
            Sequence<T> result;
            if (position == 0)
                result = Sequences.concatenate(target.getElementType(), newElement, target.subsequence(1, size));
            else if (position == size - 1)
                result = Sequences.concatenate(target.getElementType(), target.subsequence(0, size - 1), newElement);
            else
                result = Sequences.concatenate(target.getElementType(),
                        target.subsequence(0, position), newElement, target.subsequence(position + 1, size));
            if (listener != null) {
                listener.onReplaceSequence(result);
                listener.onReplaceElement(position, target.get(position), value);
            }
            return result;
        }
    }

    /**
     * Delete the element at the specified position.  If the position is out of range, the sequence is not modified.
     */
    public static <T> Sequence<T> delete(Sequence<T> target, Listener<T> listener, int position) {
        int size = target.size();
        if (position < 0 || position >= size)
            return target;
        BitSet bits = new BitSet(size);
        bits.set(0, size);
        bits.clear(position);
        Sequence<T> result = Sequences.filter(target, bits);
        if (listener != null) {
            listener.onReplaceSequence(result);
            listener.onDelete(position, target.get(position));
        }
        return result;
    }

    /**
     * Delete the elements matching the specified predicate.
     */
    public static <T> Sequence<T> delete(Sequence<T> target, Listener<T> listener,
                                         SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        bits.flip(0, target.size());
        Sequence<T> result = Sequences.filter(target, bits);
        if (listener != null) {
            listener.onReplaceSequence(result);
            for (int i = target.size() - 1; i >= 0; i--)
                if (!bits.get(i))
                    listener.onDelete(i, target.get(i));
        }
        return result;
    }

    /**
     * Insert the specified value at the end of the sequence
     */
    public static <T> Sequence<T> insert(Sequence<T> target, Listener<T> listener, T value) {
        Class<T> elementType = target.getElementType();
        Sequence<T> result = Sequences.concatenate(elementType, target, Sequences.singleton(elementType, value));
        if (listener != null) {
            listener.onReplaceSequence(result);
            listener.onInsert(target.size(), value);
        }
        return result;
    }

    /**
     * Insert the specified values at the end of the sequence
     */
    public static <T> Sequence<T> insert(Sequence<T> target, Listener<T> listener, Sequence<? extends T> values) {
        Sequence<T> result = Sequences.concatenate(target.getElementType(), target, values);
        if (listener != null) {
            listener.onReplaceSequence(result);
            int size = target.size();
            for (int i = 0; i < values.size(); i++)
                listener.onInsert(size + i, values.get(i));
        }
        return result;
    }

    /**
     * Insert the specified value at the beginning of the sequence
     */
    public static <T> Sequence<T> insertFirst(Sequence<T> target, Listener<T> listener, T value) {
        Class<T> elementType = target.getElementType();
        Sequence<T> result = Sequences.concatenate(elementType, Sequences.singleton(elementType, value), target);
        if (listener != null) {
            listener.onReplaceSequence(result);
            listener.onInsert(0, value);
        }
        return result;
    }

    /**
     * Insert the specified values at the beginning of the sequence
     */
    public static <T> Sequence<T> insertFirst(Sequence<T> target, Listener<T> listener, Sequence<? extends T> values) {
        Sequence<T> result = Sequences.concatenate(target.getElementType(), values, target);
        if (listener != null) {
            listener.onReplaceSequence(result);
            for (int i = 0; i < values.size(); i++)
                listener.onInsert(i, values.get(i));
        }
        return result;
    }

    /**
     * Insert the specified value before the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               T value, int position) {
        if (position <= 0)
            return insertFirst(target, listener, value);
        else if (position >= target.size())
            return insert(target, listener, value);
        else
            return insertBefore(target, listener, Sequences.singleton(target.getElementType(), value), position);
    }

    /**
     * Insert the specified value before the position(s) matching the specified predicate.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               T value, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertBefore(target, listener, value, bits.nextSetBit(0));
        else
            return multiInsertBefore(target, listener, bits, Sequences.singleton(target.getElementType(), value));
    }

    /**
     * Insert the specified values before the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               Sequence<? extends T> values, int position) {
        if (position <= 0)
            return insertFirst(target, listener, values);
        else if (position >= target.size())
            return insert(target, listener, values);
        else {
            Sequence<T> result = Sequences.concatenate(target.getElementType(),
                    target.subsequence(0, position), values, target.subsequence(position, target.size()));
            if (listener != null) {
                listener.onReplaceSequence(result);
                for (int i = 0; i < values.size(); i++)
                    listener.onInsert(position + i, values.get(i));
            }
            return result;
        }
    }

    /**
     * Insert the specified values before the position(s) matching the specified predicate.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertBefore(target, listener, values, bits.nextSetBit(0));
        else
            return multiInsertBefore(target, listener, bits, values);
    }

    /**
     * Insert the specified value after the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              T value, int position) {
        if (position >= target.size())
            return insert(target, listener, value);
        else if (position < 0)
            return insertFirst(target, listener, value);
        else
            return insertAfter(target, listener, Sequences.singleton(target.getElementType(), value), position);
    }

    /**
     * Insert the specified value after the position(s) matching the specified predicate.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              T value, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertAfter(target, listener, value, bits.nextSetBit(0));
        else
            return multiInsertAfter(target, listener, bits, Sequences.singleton(target.getElementType(), value));
    }

    /**
     * Insert the specified values after the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              Sequence<? extends T> values, int position) {
        double size = target.size();
        if (position >= size - 1)
            return insert(target, listener, values);
        else if (position < 0)
            return insertFirst(target, listener, values);
        else {
            Sequence<T> result = Sequences.concatenate(target.getElementType(),
                    target.subsequence(0, position + 1), values, target.subsequence(position + 1, target.size()));
            if (listener != null) {
                listener.onReplaceSequence(result);
                for (int i = 0; i < values.size(); i++)
                    listener.onInsert(position + 1 + i, values.get(i));
            }
            return result;
        }
    }

    /**
     * Insert the specified values after the position(s) matching the specified predicate.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertAfter(target, listener, values, bits.nextSetBit(0));
        else
            return multiInsertAfter(target, listener, bits, values);
    }

    /*
    * Precondition: bits.cardinality() > 1
    */
    private static <T> Sequence<T> multiInsertBefore(Sequence<T> target, Listener<T> listener,
                                                     BitSet bits, Sequence<? extends T> values) {
        assert (bits.cardinality() > 1);
        int firstBit = bits.nextSetBit(0);
        int count = 2 * bits.cardinality() + (firstBit > 0 ? 1 : 0);
        Sequence<? extends T>[] segments = Util.newSequenceArray(count);
        int n = 0;
        if (firstBit > 0)
            segments[n++] = target.subsequence(0, firstBit);
        for (int i = firstBit, j = bits.nextSetBit(i + 1); i >= 0; i = j, j = bits.nextSetBit(j + 1)) {
            segments[n++] = values;
            segments[n++] = target.subsequence(i, (j > 0) ? j : target.size());
        }
        Sequence<T> result = Sequences.concatenate(target.getElementType(), segments);
        if (listener != null) {
            listener.onReplaceSequence(result);
            int curPos = firstBit > 0 ? firstBit : 0;
            for (int i = firstBit, j = bits.nextSetBit(i + 1); i >= 0; i = j, j = bits.nextSetBit(j + 1)) {
                for (int k = 0; k < values.size(); k++)
                    listener.onInsert(curPos + k, values.get(k));
                curPos += ((j > 0) ? (j - i) : (target.size() - i)) + values.size();
            }
        }
        return result;
    }

    /*
     * Precondition: bits.cardinality() > 1
     */
    private static <T> Sequence<T> multiInsertAfter(Sequence<T> target, Listener<T> listener,
                                                    BitSet bits, Sequence<? extends T> values) {
        assert (bits.cardinality() > 1);
        int firstBit = bits.nextSetBit(0);
        int lastBit = firstBit;
        for (int i = firstBit; i >= 0; i = bits.nextSetBit(i + 1))
            lastBit = i;
        int size = target.size();
        int count = 2 * bits.cardinality() + (lastBit < size - 1 ? 1 : 0);
        Sequence<? extends T>[] segments = Util.newSequenceArray(count);
        int lastWritten = -1, n = 0;
        for (int j = firstBit; j >= 0; j = bits.nextSetBit(j + 1)) {
            segments[n++] = target.subsequence(lastWritten + 1, j + 1);
            segments[n++] = values;
            lastWritten = j;
        }
        if (lastBit < size - 1)
            segments[n++] = target.subsequence(lastBit + 1, size);
        Sequence<T> result = Sequences.concatenate(target.getElementType(), segments);
        if (listener != null) {
            listener.onReplaceSequence(result);
            int nInserted = 0;
            for (int j = firstBit; j >= 0; j = bits.nextSetBit(j + 1)) {
                for (int k = 0; k < values.size(); k++)
                    listener.onInsert(j + 1 + nInserted + k, values.get(k));
                nInserted += values.size();
            }
        }
        return result;
    }
}
