package com.sun.javafx.runtime.sequence;

import java.util.BitSet;

/**
 * Sequences are immutable, homogeneous, ordered collections.  A sequence has an element type,
 * a length, and a list of elements.  New sequences can be derived by calling the factory methods
 * (insert, delete, subsequence, etc) or can be constructed with the static factories in Sequences.
 * Sequence types are reified; the sequence references the Class object for the element type.
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
    public Class<T> getElementType();

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
    public Sequence<T> set(int position, T value);

    /** Select elements from the sequence matching the specified predicate. */
    public Sequence<T> get(SequencePredicate<T> predicate);

    /** Delete the element at the specified position.  If the position is out of range, the sequence is not modified. */
    public Sequence<T> delete(int position);

    /** Delete the elements matching the specified predicate.  */
    public Sequence<T> delete(SequencePredicate<T> predicate);

    /** Return a subsequence starting at the specified start position, up to but not including the specified
     * end position.  If the end position is less than or equal to the start, an empty sequence is returned. */
    public Sequence<T> subsequence(int start, int end);

    /** Insert the specified value at the end of the sequence */
    public Sequence<T> insert(T value);

    /** Insert the specified values at the end of the sequence */
    public Sequence<T> insert(Sequence<T> values);

    /** Insert the specified value at the beginning of the sequence */
    public Sequence<T> insertFirst(T value);

    /** Insert the specified values at the beginning of the sequence */
    public Sequence<T> insertFirst(Sequence<T> values);

    /** Insert the specified value before the specified position.  If the position is negative, it is inserted before
     *  element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.  */
    public Sequence<T> insertBefore(T value, int position);

    /** Insert the specified value before the position(s) matching the specified predicate.  */
    public Sequence<T> insertBefore(T value, SequencePredicate<T> predicate);

    /** Insert the specified values before the specified position.  If the position is negative, they are inserted before
     *  element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.  */
    public Sequence<T> insertBefore(Sequence<T> values, int position);

    /** Insert the specified values before the position(s) matchign the specified predicate.  */
    public Sequence<T> insertBefore(Sequence<T> values, SequencePredicate<T> predicate);

    /** Insert the specified value after the specified position.  If the position is negative, it is inserted before
     *  element zero; if it is greater than or equal to the size of the sequence, it is inserted at the end.  */
    public Sequence<T> insertAfter(T value, int position);

    /** Insert the specified value after the position(s) matching the specified predicate.  */
    public Sequence<T> insertAfter(T value, SequencePredicate<T> predicate);

    /** Insert the specified values after the specified position.  If the position is negative, they are inserted before
     *  element zero; if it is greater than or equal to the size of the sequence, they are inserted at the end.  */
    public Sequence<T> insertAfter(Sequence<T> values, int position);

    /** Insert the specified values after the position(s) matchign the specified predicate.  */
    public Sequence<T> insertAfter(Sequence<T> values, SequencePredicate<T> predicate);

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
    public Sequence<T> flatten();

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
    public BitSet getBits(SequencePredicate<T> predicate);

}
