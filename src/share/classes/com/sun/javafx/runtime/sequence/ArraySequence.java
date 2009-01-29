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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;

/**
 * Sequence implementation class that stores sequence elements in an array.
 *
 * The {@code ArraySequence} can be in one of two modes:
 * <ul>
 * <li>In unshared/writabable mode there is only a single "owner" of the
 *     sequence, so modifying the sequence in-place is OK.
 *     For example inserting into a sequence variable can be implemented
 *     by changing the sequence object itself.
 * <li>In shared/read-only mode there is (or at least may be) multiple
 *     references to this sequence, so modifications are not allowed.
 *     For example inserting into a sequence variable has to be implemented
 *     by assigning a modified sequence.
 * </ul>
 *
 * An {@code ArraySequence} starts out unshared, and can be set to shared.
 * The compiler inserts calls to {@code noteShared} when "reading" a sequence
 * varianble in a way that may cause it to be shared.  Getting a single
 * item from the sequence does not require {@code shared} to be set.
 * We never go back from shared to unshared.
 *
 * We use a <a href="http://en.wikipedia.org/wiki/Buffer_gap">gap buffer</a>
 * as in the Emacs text editor and {@code javax.swing.text.GapContent}.
 * This provies efficient memory usage and locality.  It should perform
 * well in most uses.  The exception is if there are lots of insertions and
 * deletions at widely separated offsets, since that requires "moving" the
 * gap to the insertion or deletion point.
 *
 * @author Brian Goetz
 * @author Per Bothner
 */

public class ArraySequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private T[] array;
    int gapStart, gapEnd;
    
    private final static int DEFAULT_SIZE = 16;

    /** A potentially useful debugging/testing hook. */
    public static boolean disableInplaceArrayUpdates;

    /** Basically a one-bit reference count.
     * I.e. false means a single reference, and true means more than one.
     * If shared is false, there are no "other" references, so operations like
     * insertion and concatenation can re-use this object, modifying the
     * object in-place.  This leads to major performance improvements - but
     * the compiler and library must be careful to set shared to true whenever
     * sharing happens or can happen.  This is done with {@code #noteShared}.
     */
    private boolean shared = disableInplaceArrayUpdates;

    public ArraySequence(int initialSize, TypeInfo<T, ?> ti) {
        super(ti);
        this.array =  Util.<T>newObjectArray(initialSize);
        gapStart = 0;
        gapEnd = initialSize;
    }

    public ArraySequence(TypeInfo<T, ?> ti) {
        this(DEFAULT_SIZE, ti);
    }

    public ArraySequence(TypeInfo<T, ?> ti, T... values) {
        this(ti, values, false);
    }

    public ArraySequence(TypeInfo<T, ?> ti, T[] values, boolean handoff) {
        super(ti);
        if (handoff) {
            this.array = values;
        }
        else {
            this.array = Util.<T>newObjectArray(values.length);
            System.arraycopy(values, 0, array, 0, values.length);
        }
        gapStart = gapEnd = values.length;
        checkForNulls();
    }

    public ArraySequence(TypeInfo<T, ?> ti, T[] values, int size) {
        super(ti);
        this.array =  Util.<T>newObjectArray(size);
        System.arraycopy(values, 0, array, 0, size);
        gapStart = gapEnd = size;
        checkForNulls();
    }

    @SuppressWarnings("unchecked")
    public ArraySequence(TypeInfo<T, ?> ti, List<? extends T> values) {
        super(ti);
        this.array = (T[]) values.toArray();
        gapStart = gapEnd = array.length;
        checkForNulls();
    }

    public ArraySequence(TypeInfo<T, ?> ti, Sequence<? extends T>... sequences) {
        super(ti);
        int size = 0;
        for (Sequence<? extends T> seq : sequences)
            size += seq.size();
        this.array = Util.<T>newObjectArray(size);
        int next = 0;
        for (Sequence<? extends T> seq : sequences) {
            final int l = seq.size();
            seq.toArray(0, l, array, next);
            next += l;
        }
        gapStart = gapEnd = size;
    }

    public ArraySequence<T> noteShared() {
        shared = true;
        return this;
    }

    public boolean isShared() {
        return shared;
    }

    private void checkForNulls() {
        int limit = gapStart;
        for (int i = 0; ; i++) {
            // limit is either gapStart or array.length.
            if (i == limit) {
                if (limit != gapStart)
                    break;
                i = gapEnd;
                limit = array.length;
                if (i == limit)
                    break;
            }
            if (array[i] == null)
                throw new IllegalArgumentException("cannot create sequence with null values");
        }
    }

    @Override
    public int size() {
        return array.length - (gapEnd - gapStart);

    }

    @Override
    public T get(int position) {
        if (position >= gapStart)
            position += gapEnd - gapStart;
        if (position < 0 || position >= array.length)
            return getDefaultValue();
        else 
            return array[position];
    }


    // optimized versions
    @Override
    public BitSet getBits(SequencePredicate<? super T> predicate) {
        int sz = size();
        BitSet bits = new BitSet(sz);
        for (int i = 0; i < sz; i++) {
            int j = i;
            if (j >= gapStart)
                j += gapEnd - gapStart;
            if (predicate.matches(this, i, array[j]))
                bits.set(i);
        }
        return bits;
    }

    @Override
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || length < 0 || sourceOffset + length > size())
            throw new ArrayIndexOutOfBoundsException();
        int len0 = length;
        int sO = sourceOffset, dO = destOffset;
        int beforeGap = gapStart - sourceOffset;
        if (beforeGap >= 0) {
            if (length <= beforeGap)
                beforeGap = length;
            System.arraycopy(array, sourceOffset, dest, destOffset, beforeGap);
            length -= beforeGap;
            destOffset += beforeGap;
            sourceOffset = gapEnd;
        }
        else
            sourceOffset += gapEnd - gapStart;
        System.arraycopy(array, sourceOffset, dest, destOffset, length);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index;

            public boolean hasNext() {
                if (index == gapStart)
                    index = gapEnd;
                return index < array.length;
            }

            public T next() {
                if (hasNext())
                    return array[index++];
                else
                    throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    protected void shiftGap(int newGapStart) {
        int delta = newGapStart - gapStart;
        if (delta > 0)
            System.arraycopy(array, gapEnd, array, gapStart, delta);
        else if (delta < 0)
            System.arraycopy(array, newGapStart, array, gapEnd + delta, - delta);
        gapEnd += delta;
        gapStart = newGapStart;
    }

    /** Make sure gap is at least 'needed' elements long. */
    protected void gapReserve(int where, int needed) {
        if (needed > gapEnd - gapStart) {
            int oldLength = array.length;
            int newLength = oldLength < 16 ? 16 : 2 * oldLength;
            int minLength = oldLength - (gapEnd - gapStart) + needed;
            if (newLength < minLength)
                newLength = minLength;
            T[] newArray = Util.<T>newObjectArray(newLength);
            int oldGapSize = gapEnd - gapStart;
            int size = oldLength-oldGapSize;
            int newGapEnd = newLength - size + where;
            int gapDelta = gapStart - where;
            int startLength;
            if (gapDelta >= 0) {
                startLength = where;
                int endLength = oldLength - gapEnd;
                System.arraycopy(array, gapEnd, newArray, newLength - endLength, endLength);
                if (gapDelta > 0)
                    System.arraycopy(array, where, newArray, newGapEnd, gapDelta);
                }
            else {
                startLength = gapStart;
                int endLength = newLength - newGapEnd;
                System.arraycopy(array, oldLength-endLength, newArray, newGapEnd, endLength);
                System.arraycopy(array, gapEnd, newArray, gapStart, -gapDelta);
            }
            System.arraycopy(array, 0, newArray, 0, startLength);
            array = newArray;
            gapStart = where;
            gapEnd = newGapEnd;
        }
        else if (where != gapStart)
            shiftGap(where);
    }
    /** Add a single element to the sequence, modifying it.
     * This must only be called when the sequence is unshared. */
    public void add(T element) {
        assert ! shared || disableInplaceArrayUpdates;
        if (element != null) {
            gapReserve(size(), 1);
            array[gapStart++] = element;
        }
    }
    /** Add the contents of an existing sequence to the sequence.
     * This must only be called when the sequence is unshared. */
    public void add(Sequence<? extends T> elements) {
        assert ! shared || disableInplaceArrayUpdates;
        final int length = Sequences.size(elements);
        if (length > 0) {
            int size = size();
            gapReserve(size, length);
            elements.toArray(0, length, array, size);
            gapStart += length;
        }
    }

    /** Internal method to replace a value. */
    protected void replace (int position, T value) {
        assert ! shared || disableInplaceArrayUpdates;
        if (position >= gapStart)
            position += gapEnd - gapStart;
        if (position < 0 || position >= array.length)
            return; // Sigh - we really should throw an exception.
        array[position] = value;
    }

    /** Replace a slice of this array.
     * @param startPos Starting position of the slice, inclusive, may be 0..size.
     * @param endPos Ending position of the slice, exclusive, may be startPos..size.
     * @param value The single replement value - must be non-null
     * @return The new sequence.
     */
    protected void replace (int startPos, int endPos, T value) {
        assert ! shared || disableInplaceArrayUpdates;
        if (endPos == startPos+1) {
            replace(startPos, value);
            return;
        }
        int size = size();
        int removed = endPos-startPos;
        gapReserve(startPos, removed == 0 ? 1 : 0);
        gapEnd = startPos + array.length - size + removed;
        array[startPos++] = value;
        gapStart=startPos;
    }

    /** Internal method to insert values.
     * Does not check shared flag, and does not do any notifications.
     */
    protected <T> void insert (Sequence<T> values, int vsize, int where) {
        assert ! shared || disableInplaceArrayUpdates;
        gapReserve(where, vsize);
        values.toArray(array, where);
        gapStart += vsize;
    }

    /* DEBUGGING code:
    int id=++counter;
    static int counter;
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("[#"+id+"(shared:"+shared+" gap:"+gapStart+"..<"+gapEnd+" alen:"+array.length+")");
        int sz = size();
        for (int i = 0; i < sz; i++)
            sbuf.append(" "+get(i));
        sbuf.append(']');
        return sbuf.toString();
    }
    */
}
