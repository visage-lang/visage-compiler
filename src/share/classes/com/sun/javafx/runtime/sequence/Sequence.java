package com.sun.javafx.runtime.sequence;

/**
 * Sequences are immutable, homogeneous, ordered collections.  A sequence has an element type,
 * a length, and a list of elements.  
 *
 * @author Brian Goetz
 */
public interface Sequence<T> extends Iterable<T> {
    public int size();

    public boolean isEmpty();

    public Class<T> getElementType();

    // @@@ May also need a "copy subsequence to array" method
    public void toArray(Object[] array, int destOffset); 

    public void foreach(SequenceClosure<T> closure);

    public T get(int position);

    public Sequence<T> set(int position, T value);

    public Sequence<T> get(SequencePredicate<T> predicate);

    public Sequence<T> delete(int position);

    public Sequence<T> delete(SequencePredicate<T> predicate);

    public Sequence<T> subsequence(int start, int end);

    public Sequence<T> insert(T value);

    public Sequence<T> insert(Sequence<T> values);

    public Sequence<T> insertFirst(T value);

    public Sequence<T> insertFirst(Sequence<T> values);

    public Sequence<T> insertBefore(T value, int position);

    public Sequence<T> insertBefore(T value, SequencePredicate<T> predicate);

    public Sequence<T> insertBefore(Sequence<T> values, int position);

    public Sequence<T> insertBefore(Sequence<T> values, SequencePredicate<T> predicate);

    public Sequence<T> insertAfter(T value, int position);

    public Sequence<T> insertAfter(T value, SequencePredicate<T> predicate);

    public Sequence<T> insertAfter(Sequence<T> values, int position);

    public Sequence<T> insertAfter(Sequence<T> values, SequencePredicate<T> predicate);

    public boolean isEqual(Sequence<T> other);
}
