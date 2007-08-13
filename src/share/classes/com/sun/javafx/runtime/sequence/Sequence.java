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
    public void toArray(T[] array, int destOffset); 

    public T get(int position);

    public Sequence<T> get(int position, int length);

    public Sequence<T> get(SequenceSelector<T> predicate);

    public void forEach(SequenceClosure<T> closure);

    public Sequence<T> delete(int position);

    public Sequence<T> delete(SequenceSelector<T> predicate);

    public Sequence<T> insert(T value);

    public Sequence<T> insert(Sequence<? extends T> values);

    public Sequence<T> insertFirst(T value);

    public Sequence<T> insertFirst(Sequence<? extends T> values);

    public Sequence<T> insertBefore(T value, int position);

    public Sequence<T> insertBefore(T value, SequenceSelector<T> predicate);

    public Sequence<T> insertBefore(Sequence<? extends T> values, int position);

    public Sequence<T> insertBefore(Sequence<? extends T> values, SequenceSelector<T> predicate);

    public Sequence<T> insertAfter(T value, int position);

    public Sequence<T> insertAfter(T value, SequenceSelector<T> predicate);

    public Sequence<T> insertAfter(Sequence<? extends T> values, int position);

    public Sequence<T> insertAfter(Sequence<? extends T> values, SequenceSelector<T> predicate);

    public boolean isEqual(Sequence<T> other);
}
