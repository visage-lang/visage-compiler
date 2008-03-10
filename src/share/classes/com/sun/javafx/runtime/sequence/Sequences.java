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
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.lang.reflect.Array;

import com.sun.javafx.runtime.Util;

/**
 * Sequences -- static helper methods for constructing derived sequences. Implements heuristics for reducing time and
 * space overhead, such as flattening complicated sequence trees where appropriate and ignoring null transformations
 * (such as appending an empty sequence). These methods are generally preferable to the constructors for
 * CompositeSequence, FilterSequence, SubSequence, etc, because they implement heuristics for sensible time-space
 * tradeoffs.
 *
 * @author Brian Goetz
 */
public final class Sequences {

    public static final Integer INTEGER_ZERO = 0;
    public static final Double DOUBLE_ZERO = 0.0;
    public static final Boolean BOOLEAN_ZERO = false;

    // Inhibit instantiation
    private Sequences() { }

    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(Class<T> clazz, T... values) {
        if (values == null || values.length == 0)
            return emptySequence(clazz);
        else
            return new ArraySequence<T>(clazz, values);
    }

    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(Class<T> clazz, T[] values, int size) {
        if (values == null || size <= 0)
            return emptySequence(clazz);
        else
            return new ArraySequence<T>(clazz, values, size);
    }

    /** Factory for simple sequence generation */
    public static<T> Sequence<T> make(Class<T> clazz, List<? extends T> values) {
        if (values == null || values.size() == 0)
            return emptySequence(clazz);
        else
            return new ArraySequence<T>(clazz, values);
    }

    /** Concatenate two sequences into a new sequence.  */
    public static<T> Sequence<T> concatenate(Class<T> clazz, Sequence<? extends T> first, Sequence<? extends T> second) {
        // OPT: for small sequences, just copy the elements
        if (Sequences.size(first) == 0)
            return Sequences.upcast(clazz, second);
        else if (Sequences.size(second) == 0)
            return Sequences.upcast(clazz, first);
        else
            return new CompositeSequence<T>(clazz, first, second);
    }

    /** Concatenate zero or more sequences into a new sequence.  */
    public static<T> Sequence<T> concatenate(Class<T> clazz, Sequence<? extends T>... seqs) {
        // OPT: for small sequences, just copy the elements
        return new CompositeSequence<T>(clazz, seqs);
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
    public static Sequence<Double> range(double lower, double upper) {
        return new NumberRangeSequence(lower, upper, 1.0);
    }

    /** Create a double range sequence ranging from lower to upper inclusive, incrementing by the specified step. */
    public static Sequence<Double> range(double lower, double upper, double step) {
        return new NumberRangeSequence(lower, upper, step);
    }

    /** Create a double range sequence ranging from lower to upper exnclusive */
     public static Sequence<Double> rangeExclusive(double lower, double upper) {
        return new NumberRangeSequence(lower, upper, 1.0, true);
    }
    /** Create a double range sequence ranging from lower to upper exnclusive, incrementing by the specified step. */
    public static Sequence<Double> rangeExclusive(double lower, double upper, double step) {
        return new NumberRangeSequence(lower, upper, step, true);
    }

    /** Create a filtered sequence.  A filtered sequence contains some, but not necessarily all, of the elements
     * of another sequence, in the same order as that sequence.  If bit n is set in the BitSet, then the element
     * at position n of the original sequence appears in the filtered sequence.  */
    public static<T> Sequence<T> filter(Sequence<T> seq, BitSet bits) {
        // OPT: for small sequences, just copy the elements
        if (bits.cardinality() == seq.size() && bits.nextClearBit(0) == seq.size())
            return seq;
        else if (bits.cardinality() == 0)
            return EmptySequence.get(seq.getElementType());
        else
            return new FilterSequence<T>(seq, bits);
    }

    /** Extract a subsequence from the specified sequence, starting as the specified start position, and up to but
     * not including the specified end position.  If the start position is negative it is assumed to be zero; if the
     * end position is greater than seq.size() it is assumed to be seq.size().  */
    public static<T> Sequence<T> subsequence(Sequence<T> seq, int start, int end) {
        // OPT: for small sequences, just copy the elements
        if (start >= end)
            return EmptySequence.get(seq.getElementType());
        else if (start <= 0 && end >= seq.size())
            return seq;
        else
            return new SubSequence<T>(seq, start, end);
    }

    /** Create a sequence containing a single element, the specified value */
    public static<T> Sequence<T> singleton(Class<T> clazz, T t) {
        if (t == null)
            return emptySequence(clazz);
        else
            return new SingletonSequence<T>(clazz, t);
    }

    /** Create an empty sequence */
    public static<T> Sequence<T> emptySequence(Class<T> clazz) {
        return EmptySequence.get(clazz);
    }

    /** Reverse an existing sequence */
    public static<T> Sequence<T> reverse(Sequence<T> sequence) {
        return new ReverseSequence<T>(sequence);
    }

    /** Create a new sequence that is the result of applying a mapping function to each element */
    public static<T,U> Sequence<U> map(Class<U> clazz, Sequence<T> sequence, SequenceMapper<T, U> mapper) {
        // OPT: for small sequences, do the mapping eagerly
        return new MapSequence<T,U>(clazz, sequence, mapper);
    }

    /** Upcast a sequence of T to a sequence of superclass-of-T */
    @SuppressWarnings("unchecked")
    public static<T> Sequence<T> upcast(Class<T> clazz, Sequence<? extends T> sequence) {
        if (clazz == sequence.getElementType())
            return (Sequence<T>) sequence;
        else if (!clazz.isAssignableFrom(sequence.getElementType()))
            throw new ClassCastException("Cannot upcast Sequence<" + sequence.getElementType().getName()
                    + "> to Sequence<" + clazz.getName() + ">");
        else
            return new UpcastSequence<T>(clazz, sequence);
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

    /** Convert a T[] to a Sequence<T> */
    public static<T> Sequence<T> fromArray(Class<T> clazz, T[] values) {
        if (values == null)
            return Sequences.emptySequence(clazz);
        return new ArraySequence<T>(clazz, values);
    }

    /** Convert a long[] to a Sequence<Long> */
    public static Sequence<Long> fromArray(long[] values) {
        if (values == null)
            return Sequences.emptySequence(Long.class);
        Long[] boxed = new Long[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Long.valueOf(values[i]);
        return new ArraySequence<Long>(Long.class, boxed, values.length);
    }

    /** Convert an int[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(int[] values) {
        if (values == null)
            return Sequences.emptySequence(Integer.class);
        Integer[] boxed = new Integer[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Integer.valueOf(values[i]);
        return new ArraySequence<Integer>(Integer.class, boxed, values.length);
    }

    /** Convert a short[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(short[] values) {
        if (values == null)
            return Sequences.emptySequence(Integer.class);
        Integer[] boxed = new Integer[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Integer.valueOf(values[i]);
        return new ArraySequence<Integer>(Integer.class, boxed, values.length);
    }

    /** Convert a char[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(char[] values) {
        if (values == null)
            return Sequences.emptySequence(Integer.class);
        Integer[] boxed = new Integer[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Integer.valueOf(values[i]);
        return new ArraySequence<Integer>(Integer.class, boxed, values.length);
    }

    /** Convert a byte[] to a Sequence<Integer> */
    public static Sequence<Integer> fromArray(byte[] values) {
        if (values == null)
            return Sequences.emptySequence(Integer.class);
        Integer[] boxed = new Integer[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Integer.valueOf(values[i]);
        return new ArraySequence<Integer>(Integer.class, boxed, values.length);
    }

    /** Convert a double[] to a Sequence<Double> */
    public static Sequence<Double> fromArray(double[] values) {
        if (values == null)
            return Sequences.emptySequence(Double.class);
        Double[] boxed = new Double[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Double.valueOf(values[i]);
        return new ArraySequence<Double>(Double.class, boxed, values.length);
    }

    /** Convert a float[] to a Sequence<Double> */
    public static Sequence<Double> fromArray(float[] values) {
        if (values == null)
            return Sequences.emptySequence(Double.class);
        Double[] boxed = new Double[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Double.valueOf(values[i]);
        return new ArraySequence<Double>(Double.class, boxed, values.length);
    }

    /** Convert a boolean[] to a Sequence<Boolean> */
    public static Sequence<Boolean> fromArray(boolean[] values) {
        if (values == null)
            return Sequences.emptySequence(Boolean.class);
        Boolean[] boxed = new Boolean[values.length];
        for (int i=0; i<values.length; i++)
            boxed[i] = Boolean.valueOf(values[i]);
        return new ArraySequence<Boolean>(Boolean.class, boxed, values.length);
    }

    /** Convert a Sequence<T> to an array */
    public static<T> T[] toArray(Sequence<T> seq) {
        T[] unboxed = Util.<T>newObjectArray(seq.size());
        for (int i=0; i<unboxed.length; i++)
            unboxed[i] = seq.get(i);
        return unboxed;
    }

    /** Convert a Sequence<Long> to an array */
    public static long[] toArray(Sequence<Long> seq) {
        long[] unboxed = new long[seq.size()];
        for (int i=0; i<unboxed.length; i++)
            unboxed[i] = seq.get(i);
        return unboxed;
    }

    /** Convert a Sequence<Integer> to an array */
    public static int[] toArray(Sequence<Integer> seq) {
        int[] unboxed = new int[seq.size()];
        for (int i=0; i<unboxed.length; i++)
            unboxed[i] = seq.get(i);
        return unboxed;
    }

    /** Convert a Sequence<Double> to a double array */
    public static double[] toArray(Sequence<? extends java.lang.Number> seq) {
        double[] unboxed = new double[seq.size()];
        for (int i=0; i<unboxed.length; i++)
            unboxed[i] = seq.get(i).doubleValue();
        return unboxed;
    }

    /** Convert a Sequence<Double> to a float array */
    public static float[] toFloatArray(Sequence<? extends java.lang.Number> seq) {
        float[] unboxed = new float[seq.size()];
        for (int i=0; i<unboxed.length; i++)
          unboxed[i] = seq.get(i).floatValue();
        return unboxed;
    }

    /** Convert a Sequence<Boolean> to an array */
    public static boolean[] toArray(Sequence<Boolean> seq) {
        boolean[] unboxed = new boolean[seq.size()];
        for (int i=0; i<unboxed.length; i++)
            unboxed[i] = seq.get(i);
        return unboxed;
    }

    public static<T> boolean isEqual(Sequence<T> one, Sequence<T> other) {
        int oneSize = size(one);
        if (oneSize == 0)
            return size(other) == 0;
        else if (oneSize != size(other))
            return false;
        else
            for (int i = 0; i < oneSize; i++) {
                if (!one.get(i).equals(other.get(i)))
                    return false;
        }
        return true;
    }

  public static<T> Sequence<? extends T> forceNonNull(Class<T> clazz, Sequence<? extends T> seq) {
    return seq == null ? emptySequence(clazz) : seq;
  }
  
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
    public static <T extends Comparable> T max (Sequence<T> seq) {
        if (seq == null)
            throw new NullPointerException();
        if (seq.isEmpty())
            throw new IllegalArgumentException();
        
        Iterator<T> it = seq.iterator();
        T result = it.next();
        T current;
        while (it.hasNext()) {
            if ((current = it.next()).compareTo(result) > 0)
                result = current;
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
    public static <T> T max (Sequence<T> seq, Comparator<? super T> c) {
        if (seq == null)
            throw new NullPointerException();
        if (seq.isEmpty())
            throw new IllegalArgumentException();
        if (c == null)
            return (T)max((Sequence<Comparable>)seq);
        
        Iterator<T> it = seq.iterator();
        T result = it.next();
        T current;
        while (it.hasNext()) {
            if (c.compare(current = it.next(), result) > 0)
                result = current;
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
    public static <T extends Comparable> T min (Sequence<T> seq) {
        if (seq == null)
            throw new NullPointerException();
        if (seq.isEmpty())
            throw new IllegalArgumentException();
        
        Iterator<T> it = seq.iterator();
        T result = it.next();
        T current;
        while (it.hasNext()) {
            if ((current = it.next()).compareTo(result) < 0)
                result = current;
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
    public static <T> T min (Sequence<T> seq, Comparator<? super T> c) {
        if (seq == null)
            throw new NullPointerException();
        if (seq.isEmpty())
            throw new IllegalArgumentException();
        if (c == null)
            return (T)min((Sequence<Comparable>)seq);
        
        Iterator<T> it = seq.iterator();
        T result = it.next();
        T current;
        while (it.hasNext()) {
            if (c.compare(current = it.next(), result) < 0)
                result = current;
        }
        return result;
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
        if (seq == null || key == null)
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
