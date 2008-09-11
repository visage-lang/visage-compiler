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
import java.util.Iterator;
import java.util.Formattable;
import java.util.Formatter;
import java.util.NoSuchElementException;

/**
 * Abstract base class for sequence classes.  A subclass need only define the size() and get() methods; subclasses
 * may also want to provide optimized versions of some other methods, such as toArray() or getBits().  Subclasses that
 * represent views onto other sequences should also implement the getDepth() method.
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequence<T> implements Sequence<T>, Formattable {
    protected final Class<T> clazz;

    protected AbstractSequence(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract int size();

    public abstract T get(int position);

    public Sequence<T> getSlice(int startPos, int endPos) {
        return Sequences.subsequence(this, startPos, endPos+1);
    }

    public BitSet getBits(SequencePredicate<? super T> predicate) {
        final int length = size();
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

    public void toArray(Object[] array, int destOffset) {
        final int length = size();
        for (int i = 0; i < length; i++)
            array[i + destOffset] = get(i);
    }

    public Sequence<T> get(SequencePredicate<? super T> predicate) {
        return Sequences.filter(this, getBits(predicate));
    }


    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int next = 0;
            final private int length = size();

            public boolean hasNext() {
                return next < length;
            }

            public T next() {
                if (next >= length)
                    throw new NoSuchElementException();
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
        return obj instanceof Sequence && Sequences.isEqual(this, (Sequence<T>) obj);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        final int length = size();
        for (int i = 0; i < length; i++) {
            T val = get(i);
            hash = 31 * hash + (val != null ? val.hashCode() : 0);
        }
        return hash;
    }


    @Override
    public String toString() {
        if (isEmpty())
            return "[ ]";
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        final int length = size();
        for (int i = 0; i < length; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        sb.append(" ]");
        return sb.toString();
    }


    public Sequence<T> flatten() {
        if (getDepth() == 0)
            return this;
        else {
            SequenceBuilder<T> sb = new SequenceBuilder<T>(getElementType(), size());
            sb.add(this);
            return sb.toSequence();
        }
    }


    // Allow sequences to be formatted - toString() is just for debugging
    // i.e
    // var seq = [1, 2];
    // for (i in seq) "{%d i}"
    // should yield: "12"
    // not: "[1, 2]"
    public void formatTo(Formatter formatter,
                         int flags,
                         int width, 
                         int precision) {
        // TBD handle flags, width, and precision
        final int length = size();
        for (int i = 0; i < length; i++) {
            formatter.format("%s", get(i));
        }
    }
}
