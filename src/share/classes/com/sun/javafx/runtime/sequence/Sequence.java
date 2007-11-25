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

import java.util.BitSet;
import java.util.Iterator;

/**
 * Sequences are immutable, homogeneous, ordered collections.  A sequence has an element type,
 * a length, and a list of elements.  New sequences can be derived by calling the factory methods
 * (insert, delete, subsequence, etc) or can be constructed with the static factories in Sequences.
 * Sequence types are reified; the sequence references the Class object for the element type.
 *
 * Sequences are stored as trees.  The "leaf" nodes are array-based sequences; intermediate nodes are "views"
 * onto underlying sequences, performing transformations such as adding elements, filtering elements, changing the
 * order of elements, etc.  Do not use the constructors for the various Sequence implementation classes to produce
 * sequences; use the factory methods in the Sequence interface or the static factories in the Sequences class.
 *
 * Sequences with elements of type Integer, Boolean, and Double are special; in these cases, when the
 * get() operation might return a null (because the index is out of range), it will instead return the
 * default value for that type (zero or false).
 *
 * @author Brian Goetz
 */
public interface Sequence<T> extends Iterable<T> {
    /** How large is this sequence?  */
    public int size();

    /** Is this sequence empty? */
    public boolean isEmpty();

    /** What is the element type? */
    public Class<? extends T> getElementType();

    /** Copy the contents of this sequence to an array, at a specified offset within the destination array */
    public void toArray(Object[] array, int destOffset);

    /** Create a new sequence whose elements are the result of applying a mapping function to the elements
     * of the original sequence. */
    public<V> Sequence<V> map(Class<V> clazz, SequenceMapper<T, V> mapper);

    /** Extract the element at the specified position.  If the position is out of range, the default value for
     * the element type is returned; either null, zero for Integer or Double sequences, or false for Boolean
     * sequences.  */
    public T get(int position);

    /** Modify the element at the specified position.  If the position is out of range, the sequence is not
     * modified. */
    public Sequence<? extends T> set(int position, T value);

    /** Select elements from the sequence matching the specified predicate. */
    public Sequence<? extends T> get(SequencePredicate<? super T> predicate);

    /** Return a subsequence starting at the specified start position, up to but not including the specified
     * end position.  If the end position is less than or equal to the start, an empty sequence is returned. */
    public Sequence<? extends T> subsequence(int start, int end);

    /** Reverse the elements of the sequence */
    public Sequence<T> reverse();
    
    /** Is this sequence equal to the specified sequence?  Two sequences are equal if their element types are equal,
     * their sizes are equal, and for each 0 <= n < size(), the nth element of each are equal according to the element's
     * equals() method.  Sequences implement an equals() method that uses isEqual().  
     */
    public boolean isEqual(Sequence<T> other);

    /** Many sequences are represented as trees to reduce copying costs; if the current sequence has depth > 0, 
     * copy the elements into a new sequence of depth == 0.
     */
    public Sequence<? extends T> flatten();

    /**
     * Returns the number of levels of sequence objects between this Sequence object and the deepest data.
     * Leaf classes (e.g., ArraySequence, IntRangeSequence) have a depth of zero; composite classes have a depth
     * one greater than their deepest leaf.
     */
    public int getDepth();

    /**
     * Return a BitSet indicating which elements of the sequence match the given predicate.  AbstractSequence
     * provides a default implementation in terms of get(i); implementations may want to provide an optimized
     * version.
     */
    public BitSet getBits(SequencePredicate<? super T> predicate);

    public Iterator<T> iterator();
}
