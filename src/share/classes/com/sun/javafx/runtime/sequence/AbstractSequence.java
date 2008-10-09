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

import java.util.*;

import com.sun.javafx.runtime.TypeInfo;

/**
 * Abstract base class for sequence classes.  A subclass need only define the size() and get() methods; subclasses
 * may also want to provide optimized versions of some other methods, such as toArray() or getBits().  Subclasses that
 * represent views onto other sequences should also implement the getDepth() method.
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequence<T> implements Sequence<T>, Formattable {
    protected final TypeInfo<T> ti;

    protected AbstractSequence(TypeInfo<T> ti) {
        this.ti = ti;
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

    public TypeInfo<T> getElementType() {
        return ti;
    }

    public T getDefaultValue() {
        return ti.defaultValue;
    }

    public Sequence<T> getEmptySequence() {
        return ti.emptySequence;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public int getDepth() {
        return 0;
    }

    public void toArray(Object[] dest, int destOffset) {
        toArray(0, size(), dest, destOffset);
    }
    
    public void toArray(int sourceOffset, int length, Object[] dest, int destOffset) {
        if (sourceOffset < 0 || (length > 0 && sourceOffset + length > size()))
            throw new ArrayIndexOutOfBoundsException();

        int i=0;
        for (Iterator<T> it = iterator(sourceOffset, sourceOffset+length-1); it.hasNext(); i++) {
            dest[i + destOffset] = it.next();
        }
    }
    
    public Sequence<T> get(SequencePredicate<? super T> predicate) {
        return Sequences.filter(this, getBits(predicate));
    }


    public Iterator<T> iterator() {
        return iterator(0, size()-1);
    }
    
    public Iterator<T> iterator(final int startPos, final int endPos) {
        return new Iterator<T>() {
            private int cur = Math.min(Math.max(0, startPos), size());          // 0 <= cur <= size()
            final private int last = Math.min(Math.max(cur, endPos+1), size()); // cur <= last <= size()

            public boolean hasNext() {
                return cur < last;
            }

            public T next() {
                if (cur >= last)
                    throw new NoSuchElementException();
                else
                    return get(cur++);
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
        for (T val : this) {
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
        for (T val : this) {
            sb.append(val);
            sb.append(", ");
        }
        final int length = sb.length();
        if (length > 1) {
            sb.delete(length-2, length);
        }
        sb.append(" ]");
        return sb.toString();
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
        for (T val : this) {
            formatter.format("%s", val);
        }
    }
}
