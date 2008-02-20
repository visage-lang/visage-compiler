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

/**
 * Helper classes for building sequences, much like StringBuilder assists in building Strings.  SequenceBuilder
 * stores the sequence building built in an array, which is automatically resized as needed.  It can be converted
 * to a Sequence by calling toSequence().
 *
 * @author Brian Goetz
 */
public class SequenceBuilder<T> {
    private final static int DEFAULT_SIZE = 16;

    private final Class<T> clazz;
    private T[] array;
    private int size;

    /** Create a SequenceBuilder for a Sequence of type T */
    public SequenceBuilder(Class<T> clazz) {
        this(clazz, DEFAULT_SIZE);
    }

    /** Create a SequenceBuilder for a Sequence of type T, ensuring that there is initially room for at least
     * initialSize elements. */
    public SequenceBuilder(Class<T> clazz, int initialSize) {
        this.clazz = clazz;
        array = Util.<T>newObjectArray(Util.powerOfTwo(1, initialSize));
    }

    private void ensureSize(int newSize) {
        if (array.length < newSize) {
            int newCapacity = Util.powerOfTwo(array.length, newSize);
            T[] newArray = Util.<T>newObjectArray(newCapacity);
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    /** Add a single element to the sequence */
    public void add(T element) {
        if (element != null) {
            ensureSize(size + 1);
            array[size++] = element;
        }
    }

    /** Add the contents of an existing sequence to the sequence */
    public void add(Sequence<? extends T> elements) {
        ensureSize(size + elements.size());
        elements.toArray(array, size);
        size += elements.size();
    }

    /** Get the current size of the sequence being constructed */
    public int size() {
        return size;
    }

    /** Get the nth element of the sequence being constructed, returning the null sequence value if n is out of range */
    public T get(int n) {
        if (n < 0 || n >= size)
            throw new IndexOutOfBoundsException(Integer.toString(n));
        else
            return array[n];
    }

    /** Erase the current contents of the SequenceBuilder */
    public void clear() {
        array = Util.<T>newObjectArray(Util.powerOfTwo(1, DEFAULT_SIZE));
        size = 0;
    }

    /** Convert the SequenceBuilder to a Sequence.  The elements will be copied to a new sequence, and will remain
     * in the SequenceBuilder, so it can continue to be used to make more sequences */
    public Sequence<T> toSequence() {
        // OPT: This causes the array to be copied; we can do the same trick as in StringBuilder to transfer
        // ownership of the array instead and avoid the copy
        return Sequences.make(clazz, array, size);
    }
}
