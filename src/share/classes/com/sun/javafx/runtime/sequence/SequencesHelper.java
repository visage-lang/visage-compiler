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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.lang.reflect.Array;

/**
 * SequencesHelper -- static helper methods for javafx.lang.Sequences.
 * The class javafx.lang.Sequences contains various functions for manipulating 
 * sequences (such as sorting and searching).
 *
 * @author Michael Heinrichs
 */
public final class SequencesHelper {

    // Inhibit instantiation
    private SequencesHelper() { }
    
    /**
     * Searches the specified sequence for the specified object using the 
     * binary search algorithm. The sequence must be sorted into ascending 
     * order according to the natural ordering of its elements (as by 
     * the sort(Sequence<T>) method) prior to making this call. 
     * 
     * If it is not sorted, the results are undefined. If the array contains 
     * multiple elements equal to the specified object, there is no guarantee 
     * which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @param key The value to be searched for.
     * @return Index of the search key, if it is contained in the array; 
     *         otherwise, (-(insertion point) - 1). The insertion point is 
     *         defined as the point at which the key would be inserted into the 
     *         array: the index of the first element greater than the key, or
     *         a.length if all elements in the array are less than the specified
     *         key. Note that this guarantees that the return value will be >= 0
     *         if and only if the key is found.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> int binarySearch (Sequence<? extends T> seq, T key) {
        if (seq.isEmpty())
            return -1;
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        return Arrays.binarySearch(array, key);
    }
    
    /**
     * Searches the specified array for the specified object using the 
     * binary search algorithm. The array must be sorted into ascending 
     * order according to the specified comparator (as by the 
     * sort(Sequence<T>, Comparator<? super T>)  method) prior to making 
     * this call. 
     * 
     * If it is not sorted, the results are undefined. If the array contains 
     * multiple elements equal to the specified object, there is no guarantee 
     * which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @param key The value to be searched for.
     * @param c The comparator by which the array is ordered. A null value 
     *          indicates that the elements' natural ordering should be used.
     * @return Index of the search key, if it is contained in the array; 
     *         otherwise, (-(insertion point) - 1). The insertion point is 
     *         defined as the point at which the key would be inserted into the 
     *         array: the index of the first element greater than the key, or
     *         a.length if all elements in the array are less than the specified
     *         key. Note that this guarantees that the return value will be >= 0
     *         if and only if the key is found.
     */
    @SuppressWarnings("unchecked")
    public static <T> int binarySearch(Sequence<? extends T> seq,  T key,  Comparator<? super T> c) {
        if (seq.isEmpty())
            return -1;
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        return Arrays.binarySearch(array, (T)key, c);
    }
    
    /**
     * Searches the specified sequence for the specified object. If the
     * sequence is sorted, binarySearch should be used instead.
     * 
     * If the sequence contains multiple elements equal to the specified object, 
     * the first occurence in the sequence will be returned.
     * 
     * The method nextIndexOf can be used in consecutive calls to iterate
     * through all occurences of a specified object.
     * 
     * @param seq The sequence to be searched.
     * @param key The value to be searched for.
     * @return Index of the search key, if it is contained in the array; 
     *         otherwise -1.
     */
    public static<T> int indexOf(Sequence<? extends T> seq, T key) {
        if (seq == null || key == null)
            throw new NullPointerException();
        
        Iterator<? extends T> it = seq.iterator();
        for (int i=0; it.hasNext(); ++i)
            if (it.next().equals(key))
                return i;
        return -1;
    }
    
    /**
     * Sorts the specified sequence of objects into ascending order, according 
     * to the natural ordering  of its elements. All elements in the sequence
     * must implement the Comparable interface. Furthermore, all elements in 
     * the sequence must be mutually comparable (that is, e1.compareTo(e2) 
     * must not throw a ClassCastException  for any elements e1 and e2 in the 
     * sequence).
     * 
     * This method is immutative, the result is returned in a new sequence,
     * while the original sequence is left untouched.
     * 
     * This sort is guaranteed to be stable: equal elements will not be 
     * reordered as a result of the sort.
     * 
     * The sorting algorithm is a modified mergesort (in which the merge is 
     * omitted if the highest element in the low sublist is less than the 
     * lowest element in the high sublist). This algorithm offers guaranteed 
     * n*log(n) performance. 
     * 
     * @param seq The sequence to be sorted.
     * @return The sorted sequence.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> Sequence<T> sort (Sequence<T> seq) {
        if (seq.isEmpty())
            return Sequences.emptySequence(seq.getElementType());
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        Arrays.sort(array);
        return Sequences.make(seq.getElementType(), array);
    }
    
    /**
     * Sorts the specified sequence of objects according to the order induced 
     * by the specified comparator. All elements in the sequence must be 
     * mutually comparable by the specified comparator (that is, 
     * c.compare(e1, e2) must not throw a ClassCastException  for any elements
     * e1 and e2 in the sequence).
     * 
     * This method is immutative, the result is returned in a new sequence,
     * while the original sequence is left untouched.
     *
     * This sort is guaranteed to be stable: equal elements will not be 
     * reordered as a result of the sort.
     * 
     * The sorting algorithm is a modified mergesort (in which the merge is 
     * omitted if the highest element in the low sublist is less than the 
     * lowest element in the high sublist). This algorithm offers guaranteed 
     * n*log(n) performance. 
     * 
     * @param seq The sequence to be sorted.
     * @param c The comparator to determine the order of the sequence. 
     *          A null value indicates that the elements' natural ordering 
     *          should be used.
     * @return The sorted sequence.
     */
    @SuppressWarnings("unchecked")
    public static <T> Sequence<T> sort (Sequence<T> seq, Comparator<? super T> c) {
        if (seq.isEmpty())
            return Sequences.emptySequence(seq.getElementType());
        T[] array = (T[])Array.newInstance(seq.getElementType(), seq.size());
        seq.toArray(array, 0);
        Arrays.sort(array, c);
        return Sequences.make(seq.getElementType(), array);
    }
}
