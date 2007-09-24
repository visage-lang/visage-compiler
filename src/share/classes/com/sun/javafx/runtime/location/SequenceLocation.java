package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

/**
 * A sequence-valued Location.  Exposes analogues of the mutative methods from Sequence, which modify the sequence
 * value and notify the appropriate change listeners.  If the change listeners implement the SequenceChangeListener
 * interface, the additional methods in SequenceChangeListener for insert, delete, and update will be called.
 *
 * @author Brian Goetz
 */
public interface SequenceLocation<T> extends Location, Iterable<T> {
    
    T get(int position);

    SequenceLocation<T> get();
    
    Sequence<T> getSequence();

    void set(Sequence<T> value);

    void set(SequenceLocation<T> value);

    public void set(int position, T value);

    public void delete(int position);

    public void delete(SequencePredicate<T> predicate);

    public void insert(T value);

    public void insert(Sequence<T> values);

    public void insertFirst(T value);

    public void insertFirst(Sequence<T> values);

    public void insertBefore(T value, int position);

    public void insertBefore(T value, SequencePredicate<T> predicate);

    public void insertBefore(Sequence<T> values, int position);

    public void insertBefore(Sequence<T> values, SequencePredicate<T> predicate);

    public void insertAfter(T value, int position);

    public void insertAfter(T value, SequencePredicate<T> predicate);

    public void insertAfter(Sequence<T> values, int position);

    public void insertAfter(Sequence<T> values, SequencePredicate<T> predicate);
}
