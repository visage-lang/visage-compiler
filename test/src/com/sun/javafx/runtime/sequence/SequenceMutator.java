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
        public void onReplaceSlice(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<? extends T> oldValue, Sequence<? extends T> newValue);
        public void onReplaceElement(int startPos, int endPos, T newElement, Sequence<? extends T> oldValue, Sequence<? extends T> newValue);
    }

    // Inhibit instantiation
    private SequenceMutator() {
    }

    @SuppressWarnings("unchecked")
    public static<T> ObjectArraySequence<T> asNonSharedArraySequence(TypeInfo<T,?> typeInfo, Sequence<? extends T> value) {
        if (value instanceof ObjectArraySequence) {
            ObjectArraySequence<T> arr = (ObjectArraySequence) value;
            if (! arr.isShared()) {
                // FIXME: arr.setElementType(typeInfo);
                return arr;
            }
        }
        return null;
    }

    public static <T> Sequence<? extends T> concatenate(TypeInfo<T, ?> ti, Sequence<? extends T>... sequences) {
        int size = 0;
        for (Sequence<? extends T> seq : sequences)
            size += seq.size();
        ObjectArraySequence<T> arr = new ObjectArraySequence(size, ti);
        for (Sequence<? extends T> seq : sequences) {
            arr.add(seq);
        }
        return arr;
    }

   /**
     * Create a sequences by replacing a slice.
     * @param sequence The sequence in which the slice is being replaced
     * @param startPos Starting position of the slice, inclusive, may be 0..size
     * @param endPos Ending position of the slice, exclusive, may be startPos..size
     * @param value The single replement value - must be non-null
     * @return The new sequence.
     */
    public static<T> Sequence<? extends T> replace(TypeInfo<T,?> typeInfo, Sequence<? extends T> sequence, int startPos, int endPos, T value) {
        final int seqSize = Sequences.size(sequence);
        startPos = Math.min (Math.max (0, startPos), seqSize);       // 0 <= startPos <= size
        endPos = Math.min (Math.max (startPos, endPos), seqSize);    // startPos <= endPos <= size
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, sequence);
        arr.replace(startPos, endPos, value, false);
        return arr;
    }

    public static<T> Sequence<? extends T> replace(TypeInfo<T,?> typeInfo, Sequence<? extends T> sequence, int startPos, T value) {
        ObjectArraySequence<T> arr = Sequences.forceNonSharedArraySequence(typeInfo, sequence);
        arr.replace(startPos, value);
        return arr;
    }

   public static<T> Sequence<? extends T> replace(TypeInfo<T,?> typeInfo, Sequence<? extends T> sequence, int startPos, int endPos, Sequence<? extends T> replacement) {
        final int seqSize = Sequences.size(sequence);
        startPos = Math.min (Math.max (0, startPos), seqSize);       // 0 <= startPos <= size
        endPos = Math.min (Math.max (startPos, endPos), seqSize);    // startPos <= endPos <= size
        ObjectArraySequence<T> arr = asNonSharedArraySequence(typeInfo, sequence);
        if (arr == null) {
            /* Possible OPT
            arr = asNonSharedArraySequence(typeInfo, replacement);
            if (arr != null) {
                ....
                return arr;
            }
            */
            arr = new ObjectArraySequence(typeInfo, sequence);
        }
        arr.replace(startPos, endPos, replacement, 0, replacement.size(), false);
        return arr;
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
    public static <T> Sequence<? extends T> replaceSlice(TypeInfo<T,?> elementType, Sequence<? extends T> target, Listener<T> listener,
                                               int startPos, int endPos, Sequence<? extends T> newValues) {
        Sequence<? extends T> result;
        final int size = Sequences.size(target);

        if (startPos > size || startPos < 0)
            return target;

        if (endPos < startPos-1)
            endPos = startPos-1;
        else if (endPos > size)
            endPos = size;

        if (startPos == 0 && endPos == size-1) {
            result = (newValues != null) ? newValues : target.getEmptySequence();
        }
        else if (startPos == endPos + 1) {
            // Insertion at startPos
            if (Sequences.size(newValues) == 0)
                result = target;
            else if (startPos == 0)
                result = SequenceMutator.concatenate(elementType, newValues, target);
            else if (startPos == size)
                result = SequenceMutator.concatenate(elementType, target, newValues);
            else
                result = SequenceMutator.replace(elementType, target, startPos, startPos, newValues);
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
                result = SequenceMutator.replace(elementType, target, startPos, endPos+1, elementType.emptySequence);
            }
        }
        else if (startPos <= endPos) {
            // @@@ OPT: Special-case for replacing leading or trailing slices
            result = SequenceMutator.replace(elementType, target, startPos, endPos+1, newValues);
        }
        else
            throw new IllegalArgumentException();

        if (result != target && listener != null)
            listener.onReplaceSlice(startPos, endPos, newValues, target, result);
        return result;
    }

    /**
     * Optimized version of replaceSlice where sizeof newValues == 1.
     * @param target The sequence in which the slice is being replaced
     * @param listener A sequence listener which will be notified, may be null
     * @param startPos Starting position of the slice, inclusive, may be 0..size
     * @param endPos Ending position of the slice, inclusive, may be start-1..size-1
     * @param newValue The single replement value - null is treated like deletion
     * @return The new sequence.
       */
    public static <T> Sequence<? extends T> replaceSlice(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                               int startPos, int endPos, T newValue) {
        final int size = Sequences.size(target);
        if (startPos > size || startPos < 0 || endPos >= size)
            return target;
        if (newValue == null)
            return replaceSlice(typeInfo, target, listener, startPos, endPos, target.getEmptySequence());

        Sequence<? extends T> result;
        if (startPos == endPos) {
            result = SequenceMutator.replace(typeInfo, target, startPos, newValue);
        } else {
            result = SequenceMutator.replace(typeInfo, target, startPos, endPos+1, newValue);
        }
        if (listener != null) {
            listener.onReplaceElement(startPos, endPos, newValue, target, result);
        }
        return result;
    }

    /**
     * Modify the element at the specified position.  If the position is out of range, the sequence is not
     * modified.
     */
    public static <T> Sequence<? extends T> set(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, int position, T value) {
        return replaceSlice(typeInfo, target, listener, position, position, value);
    }

    /**
     * Delete the element at the specified position.  If the position is out of range, the sequence is not modified.
     */
    public static <T> Sequence<? extends T> delete(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, int position) {
        return replaceSlice(typeInfo, target, listener, position, position, target.getEmptySequence());
    }

    /**
     * Insert the specified value at the end of the sequence
     */
    public static <T> Sequence<? extends T> insert(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, T value) {
        int tsize = target.size();
        return replaceSlice(typeInfo, target, listener, tsize, tsize-1, value);
    }

    /**
     * Insert the specified values at the end of the sequence
     */
    public static <T> Sequence<? extends T> insert(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, Sequence<? extends T> values) {
        int tsize = target.size();
        return replaceSlice(typeInfo, target, listener, tsize, tsize-1, values);
    }

    /**
     * Insert the specified value at the beginning of the sequence
     */
    public static <T> Sequence<? extends T> insertFirst(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, T value) {
        return replaceSlice(typeInfo, target, listener, 0, -1, value);
    }

    /**
     * Insert the specified values at the beginning of the sequence
     */
    public static <T> Sequence<? extends T> insertFirst(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener, Sequence<? extends T> values) {
        return replaceSlice(typeInfo, target, listener, 0, -1, values);
    }

    /**
     * Insert the specified value before the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<? extends T> insertBefore(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                               T value, int position) {
        if (position < 0)
            position = 0;
        else {
            int size = target.size();
            if (position > size)
                position = size;
        }
        return replaceSlice(typeInfo, target, listener, position, position-1, value);
    }

    /**
     * Insert the specified values before the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<? extends T> insertBefore(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                               Sequence<? extends T> values, int position) {
        if (position < 0)
            position = 0;
        else {
            int size = target.size();
            if (position > size)
                position = size;
        }
        return replaceSlice(typeInfo, target, listener, position, position-1, values);
    }

    /**
     * Insert the specified value after the specified position.  If the position is negative, it is inserted before
     * element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.
     */
    public static <T> Sequence<? extends T> insertAfter(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                              T value, int position) {
        return insertBefore(typeInfo, target, listener, value, position+1);
    }

    /**
     * Insert the specified values after the specified position.  If the position is negative, they are inserted before
     * element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.
     */
    public static <T> Sequence<? extends T> insertAfter(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                              Sequence<? extends T> values, int position) {
        return insertBefore(typeInfo, target, listener, values, position+1);
    }

    /**
     * Delete the elements matching the specified predicate.
     */
    public static <T> Sequence<? extends T> delete(Sequence<? extends T> target, Listener<T> listener,
                                         SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        bits.flip(0, target.size());
        Sequence<? extends T> result = Sequences.filter(target, bits);
        if (listener != null) {
            Sequence<? extends T> lastValue = target;
            BitSet partialBits = new BitSet(target.size());
            partialBits.flip(0, target.size());
            for (int i = target.size() - 1; i >= 0; i--) {
                // @@@ OPT: Collapse adjacent bits into ranges
                if (!bits.get(i)) {
                    partialBits.flip(i);
                    Sequence<? extends T> nextValue = Sequences.filter(target, partialBits);
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
    public static <T> Sequence<? extends T> insertBefore(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                               T value, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertBefore(typeInfo, target, listener, value, bits.nextSetBit(0));
        else
            return multiInsertBefore(typeInfo, target, listener, bits, Sequences.singleton(typeInfo, value));
    }

    /**
     * Insert the specified values before the position(s) matching the specified predicate.
     */
    public static <T> Sequence<? extends T> insertBefore(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                               Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertBefore(typeInfo, target, listener, values, bits.nextSetBit(0));
        else
            return multiInsertBefore(typeInfo, target, listener, bits, values);
    }

    /**
     * Insert the specified value after the position(s) matching the specified predicate.
     */
    public static <T> Sequence<? extends T> insertAfter(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                              T value, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertAfter(typeInfo, target, listener, value, bits.nextSetBit(0));
        else {
            Sequence<? extends T> single = Sequences.singleton(typeInfo, value);
            return multiInsertAfter(typeInfo, target, listener, bits, single);
        }
    }

    /**
     * Insert the specified values after the position(s) matching the specified predicate.
     */
    public static <T> Sequence<? extends T> insertAfter(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                              Sequence<? extends T> values, SequencePredicate<? super T> predicate) {
        BitSet bits = target.getBits(predicate);
        if (bits.cardinality() == 0)
            return target;
        else if (bits.cardinality() == 1)
            return insertAfter(typeInfo, target, listener, values, bits.nextSetBit(0));
        else
            return multiInsertAfter(typeInfo, target, listener, bits, values);
    }

    /*
    * Precondition: bits.cardinality() > 1
    */
    private static <T> Sequence<? extends T> multiInsertBefore(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                                     BitSet bits, Sequence<? extends T> values) {
        assert (bits.cardinality() > 1);
        Sequence<? extends T> nextValue = target;
        int firstBit = bits.nextSetBit(0);
        int curPos = firstBit > 0 ? firstBit : 0;
        for (int i = firstBit, j = bits.nextSetBit(i + 1); i >= 0; i = j, j = bits.nextSetBit(j + 1)) {
            nextValue = replaceSlice(typeInfo, nextValue, listener, curPos, curPos-1, values);
            curPos += ((j > 0) ? (j - i) : (target.size() - i)) + Sequences.size(values);
        }
        return nextValue;
    }

    /*
     * Precondition: bits.cardinality() > 1
     */
    private static <T> Sequence<? extends T> multiInsertAfter(TypeInfo<T,?> typeInfo, Sequence<? extends T> target, Listener<T> listener,
                                                    BitSet bits, Sequence<? extends T> values) {
        assert (bits.cardinality() > 1);
        Sequence<? extends T> nextValue = target;
        for (int j = bits.nextSetBit(0), iteration=0; j >= 0; j = bits.nextSetBit(j + 1), iteration++) {
            int curPos = j + (iteration * Sequences.size(values)) + 1;
            nextValue = replaceSlice(typeInfo, nextValue, listener, curPos, curPos-1, values);
        }
        return nextValue;
    }

}
