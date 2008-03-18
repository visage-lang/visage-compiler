package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

/**
 * SequenceConstant
 *
 * @author Brian Goetz
 */
public class SequenceConstant<T> extends AbstractConstantLocation<Sequence<T>> implements SequenceLocation<T> {
    private Sequence<T> $value;

    public static<T> SequenceLocation<T> make(Sequence<T> value) {
        return new SequenceConstant<T>(value);
    }

    protected SequenceConstant(Sequence<T> value) {
        this.$value = value;
    }


    public Sequence<T> getAsSequence() {
        return $value;
    }

    public Sequence<T> get() {
        return $value;
    }

    public Sequence<T> setAsSequence(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public Sequence<T> setAsSequenceFromLiteral(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(SequenceChangeListener<T> listener) { }

    public void removeChangeListener(SequenceChangeListener<T> listener) { }

    public T get(int position) {
        return $value.get(position);
    }

    public Sequence<T> getSlice(int startPos, int endPos) {
        return $value.getSlice(startPos, endPos);
    }

    public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        throw new UnsupportedOperationException();
    }

    public void deleteSlice(int startPos, int endPos) {
        throw new UnsupportedOperationException();
    }

    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    public void delete(SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(T value, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> tSequencePredicate) {
        throw new UnsupportedOperationException();
    }

    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    public T set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    public void insert(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    public void insertFirst(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    public void insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }
}
