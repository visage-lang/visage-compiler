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
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.sun.javafx.runtime.Util;

/**
 * Sequence implementation class that stores sequence elements in an array.  To create array-based sequences, use
 * the make() factory methods in Sequences instead of the ArraySequence constructor.   O(n) space and time construction
 * costs.  
 *
 * @author Brian Goetz
 */
class ArraySequence<T> extends AbstractSequence<T> implements Sequence<T> {

    private final T[] array;


    public ArraySequence(Class<T> clazz, T... values) {
        super(clazz);
        this.array = Util.<T>newObjectArray(values.length);
        System.arraycopy(values, 0, array, 0, values.length);
        checkForNulls();
    }

    public ArraySequence(Class<T> clazz, T[] values, int size) {
        super(clazz);
        this.array =  Util.<T>newObjectArray(size);
        System.arraycopy(values, 0, array, 0, size);
        checkForNulls();
    }

    @SuppressWarnings("unchecked")
    public ArraySequence(Class<T> clazz, List<? extends T> values) {
        super(clazz);
        this.array = (T[]) values.toArray();
        checkForNulls();
    }

    public ArraySequence(Class<T> clazz, Sequence<? extends T>... sequences) {
        super(clazz);
        int size = 0;
        for (Sequence<? extends T> seq : sequences)
            size += seq.size();
        this.array = Util.<T>newObjectArray(size);
        int next = 0;
        for (Sequence<? extends T> seq : sequences) {
            seq.toArray(array, next);
            next += seq.size();
        }
        checkForNulls();
    }

    private void checkForNulls() {
        for (T v : array)
            if (v == null)
                throw new IllegalArgumentException("cannot create sequence with null values");
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= array.length)
            return Util.defaultValue(getElementType());
        else 
            return array[position];
    }


    // optimized versions
    @Override
    public BitSet getBits(SequencePredicate<? super T> predicate) {
        BitSet bits = new BitSet(array.length);
        for (int i = 0; i < array.length; i++)
            if (predicate.matches(this, i, array[i]))
                bits.set(i);
        return bits;
    }

    @Override
    public void toArray(Object[] dest, int destOffset) {
        System.arraycopy(array, 0, dest, destOffset, array.length);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index;

            public boolean hasNext() {
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
}
