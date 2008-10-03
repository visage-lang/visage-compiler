/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.BitSet;

import com.sun.javafx.runtime.TypeInfo;

/**
 * Helper methods for modifying sequences and notifying sequence change listeners.  The helper methods only call the
 * sequence trigger methods; if the underlying sequence is modified then the caller is responsible for
 * calling the onChanged() method.
 *
 * @author Brian Goetz
 */
public class SequenceMutator {

    public interface Listener<T> {
        public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue);
        public void onReplaceElement(int startPos, int endPos, T newElement, Sequence<T> oldValue, Sequence<T> newValue);
    }

    // Inhibit instantiation
    private SequenceMutator() {
    }

    /**
     * The core sequence mutation operation is slice replacement.  A slice is represented by (start, end) indexes, which
     * are _inclusive_.  The length of a slice is end-start+1.
     *
     * A one-element slice at position p is represented (p, p).   The empty slice before element p is represented
     * by (p, p-1).  The empty slice after the last element is represented by (len, len-1) where len is the length
     * of the sequence.
     *
     * Inserting elements into the sequence is performed by replacing an empty slice with a non-empty slice.
     * Deleting elements from the sequence is performed by replacing a non-empty slice with an empty slice.
     * Replacing elements in the sequence is performed by replacing a non-empty slice with another non-empty one.
     *
     * @param target The sequence in which the slice is being replaced
     * @param listener A sequence listener which will be notified, may be null
     * @param startPos Starting position of the slice, inclusive, may be 0..size
     * @param endPos Ending position of the slice, inclusive, may be start-1..size
     * @param newValues Values to be inserted, may be null
     * @return The new sequence.
     */
    public static <T> Sequence<T> replaceSlice(Sequence<T> target, Listener<T> listener,
                                               int startPos, int endPos, Sequence<? extends T> newValues) {
        Sequence<T> result;
        TypeInfo<T> elementType = target.getElementType();
        final int size = Sequences.size(target);

        if (startPos > size || startPos < 0)
            return target;

        if (endPos < startPos-1)
            endPos = startPos-1;
        else if (endPos > size)
            endPos = size;

        if (startPos == endPos + 1) {
            // Insertion at startPos
            if (Sequences.size(newValues) == 0)
                result = target;
            else if (startPos == 0)
                result = Sequences.concatenate(elementType, newValues, target);
            else if (startPos == size)
                result = Sequences.concatenate(elementType, target, newValues);
            else
                result = Sequences.replace(target, startPos, startPos, newValues);
        }
        else if (Sequences.size(newValues) == 0) {
            if (newValues == null)
                newValues = target.getEmptySequence();
            // Deletion from startPos to endPos inclusive
            if (endPos == startPos-1)
                result = target;
            else if (endPos >= size-1)
                result = Sequences.subsequence(target, 0, startPos);
            else if (startPos == 0)
                result = Sequences.subsequence(target, endPos+1, size);
            else {
                result = Sequences.replace(target, startPos, endPos+1, elementType.emptySequence);
            }
        }
        else if (startPos <= endPos) {
            // @@@ OPT: Special-case for replacing leading or trailing slices
            result = Sequences.replace(target, startPos, endPos+1, newValues);
        }
        else
            throw new IllegalArgumentException();

        if (result != target && Sequences.shouldFlatten(result))
            result = Sequences.flatten(result);

        if (result != target && listener != null)
            listener.onReplaceSlice(startPos, endPos, newValues, target, result);
        return result;
    }

    /**
     * Optimized version of replaceSlice where sizeof newValues == 1.
     */
    public static <T> Sequence<T> replaceSlice(Sequence<T> target, Listener<T> listener,
                                               int startPos, int endPos, T newValue) {
        final int size = Sequences.size(target);
        if (startPos > size || startPos < 0 || endPos >= size)
            return target;
        if (newValue == null)
            return replaceSlice(target, listener, startPos, endPos, target.getEmptySequence());

        Sequence<T> result;
        if (startPos == endPos) {
            result = Sequences.replace(target, startPos, newValue);
        } else {
            result = Sequences.replace(target, startPos, endPos+1, newValue);
        }
        if (Sequences.shouldFlatten(result))
            result = Sequences.flatten(result);
        if (listener != null) {
            listener.onReplaceElement(startPos, endPos, newValue, target, result);
        }
        return result;
    }

    /**
     * Modify the element at the specified position.  If the position is out of range, the sequence is not
     * modified.
     */
    public static <T> Sequence<T> set(Sequence<T> target, Listener<T> listener, int position, T value) {
        return replaceSlice(target, listener, position, position, value);
    }

    /**
     * Delete the element at the specified position.  If the position is out of range, the sequence is not modified.
     */
    public static <T> Sequence<T> delete(Sequence<T> target, Listener<T> listener, int position) {
        return replaceSlice(target, listener, position, position, target.getEmptySequence());
    }

    /**
     * Insert the specified value at the end of the sequence
     */
    public static <T> Sequence<T> insert(Sequence<T> target, Listener<T> listener, T value) {
        return replaceSlice(target, listener, target.size(), target.size()-1, value);
    }

    /**
     * Insert the specified values at the end of the sequence
     */
    public static <T> Sequence<T> insert(Sequence<T> target, Listener<T> listener, Sequence<? extends T> values) {
        return replaceSlice(target, listener, target.size(), target.size()-1, values);
    }

    /**
     * Insert the specified value at the beginning of the sequence
     */
    public static <T> Sequence<T> insertFirst(Sequence<T> target, Listener<T> listener, T value) {
        return replaceSlice(target, listener, 0, -1, value);
    }

    /**
     * Insert the specified values at the beginning of the sequence
     */
    public static <T> Sequence<T> insertFirst(Sequence<T> target, Listener<T> listener, Sequence<? extends T> values) {
        return replaceSlice(target, listener, 0, -1, values);
    }

    /**
     * Insert the specified value before the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               T value, int position) {
        // Extra validity check needed for insertBefore
        if (position > target.size())
            return target;
        else
            return replaceSlice(target, listener, position, position-1, value);
    }

    /**
     * Insert the specified values before the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<T> insertBefore(Sequence<T> target, Listener<T> listener,
                                               Sequence<? extends T> values, int position) {
        // Extra validity check needed for insertBefore
        if (position > target.size())
            return target;
        else
            return replaceSlice(target, listener, position, position-1, values);
    }

    /**
     * Insert the specified value after the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              T value, int position) {
        if (position < 0 || position >= target.size())
            return target;
        else
            return replaceSlice(target, listener, position+1, position, value);
    }

    /**
     * Insert the specified values after the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<T> insertAfter(Sequence<T> target, Listener<T> listener,
                                              Sequence<? extends T> values, int position) {
        return replaceSlice(target, listener, position+1, position, values);
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
            Sequence<T> lastValue = target;
            BitSet partialBits = new BitSet(target.size());
            partialBits.flip(0, target.size());
            for (int i = target.size() - 1; i >= 0; i--) {
                // @@@ OPT: Collapse adjacent bits into ranges
                if (!bits.get(i)) {
                    partialBits.flip(i);
                    Sequence<T> nextValue = Sequences.filter(target, partialBits);
                    listener.onReplaceSlice(i, i, target.getEmptySequence(), lastValue, nextValue);
                    lastValue = nextValue;
                }
            }
        }
        return result;
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
        Sequence<T> nextValue = target;
        int firstBit = bits.nextSetBit(0);
        int curPos = firstBit > 0 ? firstBit : 0;
        for (int i = firstBit, j = bits.nextSetBit(i + 1); i >= 0; i = j, j = bits.nextSetBit(j + 1)) {
            nextValue = replaceSlice(nextValue, listener, curPos, curPos-1, values);
            curPos += ((j > 0) ? (j - i) : (target.size() - i)) + Sequences.size(values);
        }
        return nextValue;
    }

    /*
     * Precondition: bits.cardinality() > 1
     */
    private static <T> Sequence<T> multiInsertAfter(Sequence<T> target, Listener<T> listener,
                                                    BitSet bits, Sequence<? extends T> values) {
        assert (bits.cardinality() > 1);
        Sequence<T> nextValue = target;
        for (int j = bits.nextSetBit(0), iteration=0; j >= 0; j = bits.nextSetBit(j + 1), iteration++) {
            int curPos = j + (iteration * Sequences.size(values)) + 1;
            nextValue = replaceSlice(nextValue, listener, curPos, curPos-1, values);
        }
        return nextValue;
    }

}
