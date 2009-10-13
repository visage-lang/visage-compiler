/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.Util;
import com.sun.javafx.runtime.FXBase;

/**
 * Sequences -- static helper methods for constructing derived sequences. Implements heuristics for reducing time and
 * space overhead, such as flattening complicated sequence trees where appropriate and ignoring null transformations
 * (such as appending an empty sequence). These methods are generally preferable to the constructors for
 * CompositeSequence, FilterSequence, SubSequence, etc, because they implement heuristics for sensible time-space
 * tradeoffs.
 *
 * @author Brian Goetz
 */
public final class Sequences extends SequenceConversions {

    // Inhibit instantiation
    private Sequences() { }

    @SuppressWarnings("unchecked")
    public static<T> ObjectArraySequence<T> forceNonSharedArraySequence(TypeInfo<T> typeInfo, Sequence<? extends T> value) {
        ObjectArraySequence<T> arr;
        block: {
            if (value instanceof ObjectArraySequence) {
                arr = (ObjectArraySequence) value;
                if (! arr.isShared()) {
                    // FIXME: arr.setElementType(typeInfo);
                    return arr;
                }
                // Special case - we might as well re-use an empty array.
                if (arr.array.length == 0) {
                    arr = new ObjectArraySequence(typeInfo, arr.array, true);
                    break block;
                }
            }
            arr = new ObjectArraySequence(typeInfo, value);
        }
        arr.incrementSharing();
        return arr;
    }

    public static<T> T incrementSharing(T value) {
        if (value instanceof ArraySequence)
            ((ArraySequence) value).incrementSharing();
        return value;
    }

    /***************************************************/
    /* Methods for constructing sequences from scratch */
    /***************************************************/


    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(TypeInfo<T> ti, T... values) {
        if (values == null || values.length == 0)
            return ti.emptySequence;
        else
            return new ObjectArraySequence<T>(ti, values);
    }

    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(TypeInfo<T> ti, T[] values, int size) {
        if (values == null || size <= 0)
            return ti.emptySequence;
        else
            return new ObjectArraySequence<T>(ti, values, 0, size);
    }

    public static<T> Sequence<T> makeViaHandoff(TypeInfo<T> ti, T[] values) {
        return new ObjectArraySequence<T>(ti, values, true);
    }

    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(TypeInfo<T> ti, List<? extends T> values) {
        if (values == null || values.size() == 0)
            return ti.emptySequence;
        else
            return new ObjectArraySequence<T>(ti, values);
    }

    /** Create an Integer range sequence ranging from lower to upper inclusive. */
    public static Sequence<Integer> range(int lower, int upper) {
        return new IntRangeSequence(lower, upper);
    }

    /** Create an Integer range sequence ranging from lower to upper inclusive, incrementing by the specified step. */
    public static Sequence<Integer> range(int lower, int upper, int step) {
        return new IntRangeSequence(lower, upper, step);
    }

    /** Create an Integer range sequence ranging from lower to upper exclusive. */
    public static Sequence<Integer> rangeExclusive(int lower, int upper) {
        return new IntRangeSequence(lower, upper, true);
    }

    /** Create an Integer range sequence ranging from lower to upper exnclusive, incrementing by the specified step. */
    public static Sequence<Integer> rangeExclusive(int lower, int upper, int step) {
        return new IntRangeSequence(lower, upper, step, true);
    }

    /** Create a double range sequence ranging from lower to upper inclusive, incrementing by 1.0 */
    public static Sequence<Float> range(float lower, float upper) {
        return new NumberRangeSequence(lower, upper, 1.0f);
    }

    /** Create a double range sequence ranging from lower to upper inclusive, incrementing by the specified step. */
    public static Sequence<Float> range(float lower, float upper, float step) {
        return new NumberRangeSequence(lower, upper, step);
    }

    /** Create a double range sequence ranging from lower to upper exnclusive */
     public static Sequence<Float> rangeExclusive(float lower, float upper) {
        return new NumberRangeSequence(lower, upper, 1.0f, true);
    }
    /** Create a double range sequence ranging from lower to upper exnclusive, incrementing by the specified step. */
    public static Sequence<Float> rangeExclusive(float lower, float upper, float step) {
        return new NumberRangeSequence(lower, upper, step, true);
    }

    /** Create a filtered sequence.  A filtered sequence contains some, but not necessarily all, of the elements
     * of another sequence, in the same order as that sequence.  If bit n is set in the BitSet, then the element
     * at position n of the original sequence appears in the filtered sequence.  */
    public static<T> Sequence<T> filter(Sequence<T> seq, BitSet bits) {
        int cardinality = bits.cardinality();
        if (cardinality == 0)
            return seq.getEmptySequence();
        else if (cardinality == seq.size() && bits.nextClearBit(0) == seq.size())
            return seq;
        else {
            ObjectArraySequence<T> result = new ObjectArraySequence(cardinality, seq.getElementType());
            for (int i = bits.nextSetBit(0), next = 0; i >= 0; i = bits.nextSetBit(i + 1))
                result.add(seq.get(i));
            return result;
        }
    }

    /** Extract a subsequence from the specified sequence, starting as the specified start position, and up to but
     * not including the specified end position.  If the start position is negative it is assumed to be zero; if the
     * end position is greater than seq.size() it is assumed to be seq.size().  */
    public static<T> Sequence<T> subsequence(Sequence<T> seq, int start, int end) {
        // OPT: for small sequences, just copy the elements
        if (start >= end)
            return seq.getEmptySequence();
        else if (start <= 0 && end >= seq.size())
            return seq;
        else {
            start = Math.max(start, 0);
            end = Math.min(end, seq.size());
            return SubSequence.make(seq, end-start, start, 1);
        }
    }

    public static int calculateSize(int start, int bound, int step, boolean exclusive) {
        if (Math.abs((long) start - (long) bound) + ((long) (exclusive ? 0 : 1)) > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range sequence too big");
        if (bound == start) {
            return exclusive ? 0 : 1;
        }
        else {
            int size = Math.max(0, ((bound - start) / step) + 1);
            if (exclusive) {
                boolean tooBig = (step > 0)
                        ? (start + (size-1)*step >= bound)
                        : (start + (size-1)*step <= bound);
                if (tooBig && size > 0)
                    --size;
            }
            return (int) size;
        }
    }

    /* NOTE Possible future functionality, to allow a step in a slice expression.
     * This is a sketch, which needs some tweaking to handle corner cases,
     * plus some compiler work.
     * NOTE The generalization to step!=1, except as used by the reverse
     * function, is UNTESTED.
    public static<T> Sequence<T> subsequence(Sequence<T> seq, int start, int bound, int step, boolean exclusive) {
        // FIXME canonicalize start (if out of range)
        int size = calculateSize(start, bound, step, exclusive);
        return SubSequence.make(seq, start, size, step);
    }
    */

    /** Create a sequence containing a single element, the specified value */
    public static<T> Sequence<T> singleton(TypeInfo<T> ti, T t) {
        if (t == null)
            return ti.emptySequence;
        else
            return new SingletonSequence<T>(ti, t);
    }

    /** Create an empty sequence */
    public static<T> Sequence<T> emptySequence(Class<T> clazz) {
        return TypeInfo.getTypeInfo(clazz).emptySequence;
    }

    /** Reverse an existing sequence */
    public static<T> Sequence<T> reverse(Sequence<T> sequence) {
        int ssize = sequence.size();
        return SubSequence.make(sequence, ssize, ssize-1, -1);
    }

    /** Convert a Collection<T> to a Sequence<T> */
    @SuppressWarnings("unchecked")
    public static<T> Sequence<T> fromCollection(TypeInfo<T> ti, Collection<T> values) {
        if (values == null)
            return ti.emptySequence;
        // OPT: Use handoff, pre-size array
        return new ObjectArraySequence<T>(ti, (T[]) values.toArray());
    }

    /**********************************************/
    /* Utility methods for dealing with sequences */
    /**********************************************/


    /** Upcast a sequence of T to a sequence of superclass-of-T */
    @SuppressWarnings("unchecked")
    public static<T> Sequence<T> upcast(Sequence<? extends T> sequence) {
        return (Sequence<T>) sequence;
    }

    /** Convert any numeric sequence to any other numeric sequence */
    public static<T extends Number, V extends Number>
    Sequence<T> convertNumberSequence(NumericTypeInfo<T> toType, NumericTypeInfo<V> fromType, Sequence<? extends V> seq) {
        if (Sequences.size(seq) == 0)
            return toType.emptySequence;

        int length = seq.size();
        T[] toArray = toType.makeArray(length);
        int i=0;
        for (V val : seq) {
            toArray[i++] = toType.asPreferred(fromType, val);
        }
        return new ObjectArraySequence<T>(toType, toArray, 0, length);
    }

    /** How large is this sequence?  Can be applied to any object.  */
    public static int size(Object seq) {
        if (seq instanceof Sequence)
            return ((Sequence) seq).size();
        else
            return seq == null ? 0 : 1;
    }

    /** How large is this sequence?  */
    public static int size(Sequence seq) {
        return (seq == null) ? 0 : seq.size();
    }
    
    @SuppressWarnings("unchecked")
    public static<T> Iterator<T> iterator(Sequence<T> seq) {
        return (seq == null)? (Iterator<T>) TypeInfo.Object.emptySequence.iterator() : seq.iterator();
    }

    @SuppressWarnings("unchecked")
    public static<T> Iterator<T> iterator(Sequence<T> seq, int startPos, int endPos) {
        return (seq == null)? (Iterator<T>) TypeInfo.Object.emptySequence.iterator() : seq.iterator(startPos, endPos);
    }

    public static<T> boolean isEqual(Sequence<?> one, Sequence<?> other) {
        int oneSize = size(one);
        int otherSize = size(other);
        if (oneSize == 0)
            return (otherSize == 0);
        else if (oneSize != otherSize)
            return false;
        else {
            Iterator<?> it1 = one.iterator();
            Iterator<?> it2 = other.iterator();
            while (it1.hasNext()) {
                if (! it1.next().equals(it2.next()))
                    return false;
            }
            return true;
        }
    }

    public static<T> boolean isEqualByContentIdentity(Sequence<? extends T> one, Sequence<? extends T> other) {
        int oneSize = size(one);
        if (oneSize == 0)
            return size(other) == 0;
        else if (oneSize != size(other))
            return false;
        else {
            Iterator<? extends T> it1 = one.iterator();
            Iterator<? extends T> it2 = other.iterator();
            while (it1.hasNext()) {
                if (it1.next() != it2.next())
                    return false;
            }
            return true;
        }
    }

    public static<T> boolean sliceEqual(Sequence<? extends T> seq, int startPos, int endPos/*exclusive*/, Sequence<? extends T> slice) {
        int size = size(slice);
        if (endPos - startPos != size)
            return false;
        /* For most (but not all) Sequences types, indexing is faster.
        Iterator<? extends T> seqIterator = iterator(seq, startPos, endPos-1);
        for (Iterator<? extends T> sliceIterator = iterator(slice); sliceIterator.hasNext(); ) {
            if (!seqIterator.next().equals(sliceIterator.next())) {
                return false;
            }
        }
        */
        for (int i = 0;  i < size;  i++) {
            if (!seq.get(startPos+i).equals(slice.get(i)))
                return false;
        }
        return true;
    }

    public static<T> Sequence<? extends T> forceNonNull(TypeInfo<T> typeInfo, Sequence<? extends T> seq) {
        return seq == null ? typeInfo.emptySequence : seq;
    }
    
    /**
     * Return the single value of a sequence.
     * Return null if the sequence zero zero or more than 1 elements.
     * Thid is used to implement 'seq instanceof T'.
     */
    public static <T> T getSingleValue (Sequence<T> seq) {
        if (seq == null || seq.size() != 1)
            return null;
        return seq.get(0);
    }


    /*************************/
    /* Sorting and searching */
    /*************************/

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
    public static <T extends Comparable> int binarySearch (Sequence<? extends T> seq, T key) {
        if (seq.isEmpty())
            return -1;
        final int length = seq.size();
        T[] array = Util.<T>newComparableArray(length);
        seq.toArray(0, length, array, 0);
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
    public static <T> int binarySearch(Sequence<? extends T> seq,  T key,  Comparator<? super T> c) {
        if (seq.isEmpty())
            return -1;
        final int length = seq.size();
        T[] array = Util.<T>newObjectArray(length);
        seq.toArray(0, length, array, 0);
        return Arrays.binarySearch(array, (T)key, c);
    }
    
    /**
     * Searches the specified sequence for the specified object.
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
    public static<T> int indexByIdentity(Sequence<? extends T> seq, T key) {
        return nextIndexByIdentity(seq, key, 0);
    }
    
    /**
     * Searches the specified sequence for an object with the same value. The
     * objects are compared using the method equals(). If the sequence is sorted, 
     * binarySearch should be used instead.
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
        return nextIndexOf(seq, key, 0);
    }
    
    /**
     * Returns the element with the maximum value in the specified sequence, 
     * according to the natural ordering  of its elements. All elements in the 
     * sequence must implement the Comparable interface. Furthermore, all 
     * elements in the sequence must be mutually comparable (that is, 
     * e1.compareTo(e2) must not throw a ClassCastException  for any elements 
     * e1 and e2 in the sequence).
     * 
     * If the sequence contains multiple elements with the maximum value, 
     * there is no guarantee which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @return The element with the maximum value.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> T max (Sequence<T> seq) {
        if (seq == null || seq.isEmpty())
            throw new IllegalArgumentException("empty sequence passed to Sequences.max");
        
        T result = seq.get(0);
        for (T val : seq) {
            if (result.compareTo(val) < 0) {
                result = val;
            }
        }
        return result;
    }
    
    /**
     * Returns the element with the maximum value in the specified sequence, 
     * according to the specified comparator. All elements in the sequence must 
     * be mutually comparable by the specified comparator (that is, 
     * c.compare(e1, e2) must not throw a ClassCastException  for any elements
     * e1 and e2 in the sequence).
     * 
     * If the sequence contains multiple elements with the maximum value, 
     * there is no guarantee which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @param c The comparator to determine the order of the sequence. 
     *          A null value indicates that the elements' natural ordering 
     *          should be used.
     * @return The element with the maximum value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T max (Sequence<T> seq, Comparator<? super T> c) {
        if (seq == null || seq.isEmpty())
            throw new IllegalArgumentException("empty sequence passed to Sequences.max");
        if (c == null)
            return (T)max((Sequence<Comparable>)seq);
        
        T result = seq.get(0);
        for (T val : seq) {
            if (c.compare(result, val) < 0) {
                result = val;
            }
        }
        return result;
    }
    
    /**
     * Returns the element with the minimum value in the specified sequence, 
     * according to the natural ordering  of its elements. All elements in the 
     * sequence must implement the Comparable interface. Furthermore, all 
     * elements in the sequence must be mutually comparable (that is, 
     * e1.compareTo(e2) must not throw a ClassCastException  for any elements 
     * e1 and e2 in the sequence).
     * 
     * If the sequence contains multiple elements with the minimum value, 
     * there is no guarantee which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @return The element with the maximum value.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> T min (Sequence<T> seq) {
        if (seq == null || seq.isEmpty())
            throw new IllegalArgumentException("empty sequence passed to Sequences.min");
        
        T result = seq.get(0);
        for (T val : seq) {
            if (result.compareTo(val) > 0) {
                result = val;
            }
        }
        return result;
    }
    
    /**
     * Returns the element with the minimum value in the specified sequence, 
     * according to the specified comparator. All elements in the sequence must 
     * be mutually comparable by the specified comparator (that is, 
     * c.compare(e1, e2) must not throw a ClassCastException  for any elements
     * e1 and e2 in the sequence).
     * 
     * If the sequence contains multiple elements with the minimum value, 
     * there is no guarantee which one will be found.
     * 
     * @param seq The sequence to be searched.
     * @param c The comparator to determine the order of the sequence. 
     *          A null value indicates that the elements' natural ordering 
     *          should be used.
     * @return The element with the minimum value.
     */
    @SuppressWarnings("unchecked")
    public static <T> T min (Sequence<T> seq, Comparator<? super T> c) {
        if (seq == null || seq.isEmpty())
            throw new IllegalArgumentException("empty sequence passed to Sequences.min");
        if (c == null)
            return (T)min((Sequence<Comparable>)seq);
        
        T result = seq.get(0);
        for (T val : seq) {
            if (c.compare(result, val) > 0)
                result = val;
        }
        return result;
    }
            
    /**
     * Searches the specified sequence for an object with the same value,
     * starting the search at the specified position. The objects are compared 
     * using the method equals().
     * 
     * If the sequence contains multiple elements equal to the specified object, 
     * the first occurence in the subsequence will be returned.
     * 
     * @param seq The sequence to be searched.
     * @param key The value to be searched for.
     * @param pos The position in the sequence to start the search. If pos is
     *            negative or 0 the whole sequence will be searched.
     * @return Index of the search key, if it is contained in the array; 
     *         otherwise -1.
     */
    public static<T> int nextIndexByIdentity(Sequence<? extends T> seq, T key, int pos) {
        if (seq == null)
            return -1;
        if (key == null)
            throw new NullPointerException();
        
        Iterator<? extends T> it = seq.iterator();
        int i;
        for (i=0; i<pos && it.hasNext(); ++i)
            it.next();
        for (; it.hasNext(); ++i)
            if (it.next() ==  key)
                return i;
        return -1;
    }
    
    /**
     * Searches the specified sequence for the specified object, starting the
     * search at the specified position. 
     * 
     * If the sequence contains multiple elements equal to the specified object, 
     * the first occurence in the subsequence will be returned.
     * 
     * @param seq The sequence to be searched.
     * @param key The value to be searched for.
     * @param pos The position in the sequence to start the search. If pos is
     *            negative or 0 the whole sequence will be searched.
     * @return Index of the search key, if it is contained in the array; 
     *         otherwise -1.
     */
    public static<T> int nextIndexOf(Sequence<? extends T> seq, T key, int pos) {
        if (seq == null)
            return -1;
        if (key == null)
            throw new NullPointerException();
        
        Iterator<? extends T> it = seq.iterator();
        int i;
        for (i=0; i<pos && it.hasNext(); ++i)
            it.next();
        for (; it.hasNext(); ++i)
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
    public static <T extends Comparable> Sequence<? extends T> sort (Sequence<T> seq) {
        if (seq.isEmpty())
            return seq.getEmptySequence();
        final int length = seq.size();
        T[] array = Util.<T>newComparableArray(length);
        seq.toArray(0, length, array, 0);
        Arrays.sort(array);
        return Sequences.<T>make(seq.getElementType(), array);
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
    public static <T> Sequence<? extends T> sort (Sequence<T> seq, Comparator<? super T> c) {
        if (seq.isEmpty())
            return seq.getEmptySequence();
        final int length = seq.size();
        T[] array = Util.<T>newObjectArray(length);
        seq.toArray(0, length, array, 0);
        Arrays.sort(array, c);
        return Sequences.<T>make(seq.getElementType(), array);
    }

    /* Only used by the testing framework - should be moved.  FIXME */
    public static<T> Sequence<? extends T> insert(TypeInfo<T> typeInfo, Sequence<? extends T> sequence, T value) {
        ObjectArraySequence<T> arr = forceNonSharedArraySequence(typeInfo, sequence);
        arr.add(value);
        return arr;
    }

    /** Returns a new sequence containing the randomly shuffled
     * contents of the existing sequence
     * */
    public static <T> Sequence<T> shuffle (Sequence<T> seq) {
        T[] array = Sequences.toArray(seq);
        List<? extends T> list = Arrays.asList(array);
        Collections.shuffle(list);
        return Sequences.make(seq.getElementType(), list);
    }

    public static <T> int sizeOfOldValue(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int hiIndex/*exclusive*/) {
        if (oldValue != null)
            return oldValue.size();
        return buffer.getRawArrayLength() - buffer.gapEnd + hiIndex;
    }

    public static <T> int sizeOfNewElements(ArraySequence<T> buffer, int loIndex, Sequence<? extends T> newElements) {
        if (newElements != null)
            return newElements.size();
        return buffer.gapStart - loIndex;
    }

    public static <T> T getFromOldValue(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, int k) {
        if (oldValue != null)
            return oldValue.get(k);
        if (k >= 0) {
            if (k >= startPos)
                k += buffer.gapEnd - endPos;
            int alen = buffer.getRawArrayLength();
            if (k < alen) {
                return buffer.getRawArrayElementAsObject(k);
            }
        }
        return buffer.getDefaultValue();
    }

    public static <T> T getFromNewElements(ArraySequence<T> buffer, int loIndex, Sequence<? extends T> newElements, int k) {
        if (newElements != null)
            return newElements.get(k);
        if (k >= 0) {
            k = k + loIndex;
            if (k < buffer.gapStart)
                return buffer.get(k);
        }
        return buffer.getDefaultValue();
    }

$PREFIX:{T|
    public static $PRIM_MAP.(T)$ getAs$T$FromOldValue(ArraySequence<$BOXED_MAP.(T)$> buffer, Sequence<? extends $BOXED_MAP.(T)$> oldValue, int startPos, int endPos, int k) {
        if (oldValue != null)
            return oldValue.get(k);
        if (k >= 0) {
            if (k >= startPos)
                k += buffer.gapEnd - endPos;
            int alen = buffer.getRawArrayLength();
            if (k < alen) {
                return buffer.getRawArrayElementAsObject(k);
            }
        }
        return buffer.getDefaultValue();
    }

    public static $PRIM_MAP.(T)$ getAs$T$FromNewElements(ArraySequence<$BOXED_MAP.(T)$> buffer, int loIndex, Sequence<? extends $BOXED_MAP.(T)$> newElements, int k) {
        if (newElements != null)
            return newElements.get(k);
        if (k >= 0) {
            k = k + loIndex;
            if (k < buffer.gapStart)
                return buffer.get(k);
        }
        return buffer.getDefaultValue();
    }
}$

    public static <T> Sequence<? extends T> getOldValue(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int loIndex, int hiIndex/*exclusive*/) {
        if (oldValue != null)
            return oldValue;
        return buffer.extractOldValue(loIndex, hiIndex);
    }

    public static <T> Sequence<? extends T> getNewElements(ArraySequence<T> buffer, int startPos, Sequence<? extends T> newElements) {
        if (newElements != null)
            return newElements;
        buffer.incrementSharing();
        return Sequences.subsequence(buffer, startPos, buffer.gapStart);
    }

    public static <T> Sequence<? extends T> replaceSlice(Sequence<? extends T> oldValue, int startPos, int endPos/*exclusive*/, T newValue) {
        if (newValue != null ? (endPos == startPos+1 && newValue.equals(oldValue.get( startPos))) : endPos == startPos) {
            // FIXME set valid??
            return oldValue;
        }

        int oldSize = oldValue.size();
        if (startPos < 0)
            startPos = 0;
        else if (startPos > oldSize)
            startPos = oldSize;
        if (endPos > oldSize)
            endPos = oldSize;
        else if (endPos < startPos)
            endPos = startPos;
        ObjectArraySequence<T> arr = forceNonSharedArraySequence((TypeInfo<T>) oldValue.getElementType(), oldValue);
        arr.replace(startPos, endPos, (T) newValue, true);
        arr.clearOldValues(endPos-startPos);
        return arr;
    }

    public static <T> void replaceSlice(FXBase instance, int varNum, int startPos, int endPos/*exclusive*/, T newValue) {
        Sequence<? extends T> oldValue = (Sequence<? extends T>) instance.get\$(varNum);
        Sequence<? extends T> arr = replaceSlice(oldValue, startPos, endPos, newValue);
        // FIXME var.setValid();
        // TODO: invalidate(varNum, startPos, endPos, newValue==null?0:1);
        instance.set\$(varNum, arr);
    }

    public static <T> Sequence<? extends T> replaceSlice(Sequence<? extends T> oldValue, int startPos, int endPos/*exclusive*/, Sequence<? extends T> newValues) {
        if (sliceEqual(oldValue, startPos, endPos, newValues)) {
            // FIXME set valid??
            return oldValue;
        }
        int inserted = newValues.size();
        int oldSize = oldValue.size();
        if (startPos < 0)
            startPos = 0;
        else if (startPos > oldSize)
            startPos = oldSize;
        if (endPos > oldSize)
            endPos = oldSize;
        else if (endPos < startPos)
            endPos = startPos;
        ObjectArraySequence<T> arr = forceNonSharedArraySequence((TypeInfo<T>) oldValue.getElementType(), oldValue);
        arr.replace(startPos, endPos, newValues, 0, inserted, true);
        arr.clearOldValues(endPos-startPos);
        return arr;
    }

    public static <T> void replaceSlice(FXBase instance, int varNum, int startPos, int endPos/*exclusive*/, Sequence<? extends T> newValues) {
        Sequence<? extends T> oldValue = (Sequence<? extends T>) instance.get\$(varNum);
        Sequence<? extends T> arr = replaceSlice(oldValue, startPos, endPos, newValues);
        instance.set\$(varNum, arr);
    }

    public static <T> Sequence<? extends T> set(Sequence<? extends T> oldValue, T newValue, int index) {
        return replaceSlice(oldValue, index, index+1, newValue);
    }

    public static <T> T set(FXBase instance, int varNum, T newValue, int index) {
        replaceSlice(instance, varNum, index, index+1, newValue);
        return newValue;
    }

    public static <T> Sequence<? extends T> insert(Sequence<? extends T> oldValue, T newValue) {
        if (newValue == null)
            return oldValue;
        int oldSize = oldValue.size();
        ObjectArraySequence<T> arr = forceNonSharedArraySequence((TypeInfo<T>) oldValue.getElementType(), oldValue);
        arr.replace(oldSize, oldSize, (T) newValue, true);
        return arr;
    }

    public static <T> void insert(FXBase instance, int varNum, T newValue) {
        if (newValue == null)
            return;
        Sequence<? extends T> oldValue = (Sequence<? extends T>) instance.get\$(varNum);
        int oldSize = oldValue.size();
        Sequence<? extends T> arr = insert(oldValue, newValue);
        // FIXME var.setValid();
        // TODO: invalidate(varNum, startPos, endPos, newValue==null?0:1);
        instance.set\$(varNum, arr);
    }

    public static <T> Sequence<? extends T> insert(Sequence<? extends T> oldValue, Sequence<? extends T> values) {
        int inserted = values.size();
        if (inserted == 0)
            return oldValue;
        int oldSize = oldValue.size();
        ObjectArraySequence<T> arr = forceNonSharedArraySequence((TypeInfo<T>) oldValue.getElementType(), oldValue);
        arr.replace(oldSize, oldSize, values, 0, inserted, true);
        return arr;
    }

    public static <T> void insert(FXBase instance, int varNum, Sequence<? extends T> values) {
        int inserted = values.size();
        if (inserted == 0)
            return;
        Sequence<? extends T> oldValue = (Sequence<? extends T>) instance.get\$(varNum);
        Sequence<? extends T> arr = insert(oldValue, values);
        // FIXME var.setValid();
        // TODO: invalidate(varNum, startPos, endPos, ...);
        instance.set\$(varNum, arr);
    }

    public static <T> void insertBefore(FXBase instance, int varNum, T value, int position) {
        replaceSlice(instance, varNum, position, position, value);
    }

    public static <T> void insertBefore(FXBase instance, int varNum, Sequence<? extends T> values, int position) {
        replaceSlice(instance, varNum, position, position, values);
    }

    public static <T> Sequence<? extends T> insertBefore(Sequence<? extends T> oldValue, T value, int position) {
        return replaceSlice(oldValue, position, position, value);
    }

    public static <T> Sequence<? extends T> insertBefore(Sequence<? extends T> oldValue, Sequence<? extends T> values, int position) {
        return replaceSlice(oldValue, position, position, values);
    }
}
