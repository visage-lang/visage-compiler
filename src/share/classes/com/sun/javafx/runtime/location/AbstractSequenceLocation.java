package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractSequenceLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequenceLocation<T> extends AbstractLocation implements SequenceLocation<T> {
    protected Sequence<T> value, previousValue;
    protected final Class<T> clazz;
    protected List<SequenceChangeListener<? super T>> sequenceListeners;

    public AbstractSequenceLocation(Class<T> clazz, boolean valid, boolean lazy) {
        super(valid, lazy);
        this.clazz = clazz;
    }

    public Sequence<T> getPreviousValue() {
        return previousValue;
    }

    public void addChangeListener(SequenceChangeListener<? super T> listener) {
        if (sequenceListeners == null)
            sequenceListeners = new ArrayList<SequenceChangeListener<? super T>>();
        sequenceListeners.add(listener);
    }

    /**
     * Update the held value, notifying change listeners and generating appropriate delete/insert events as necessary
     */
    protected Sequence<T> replaceValue(Sequence<T> newValue) {
        if (newValue == null)
            throw new NullPointerException();
        Sequence<? extends T> oldValue = value;
        if (!newValue.equals(oldValue)) {
            previousValue = value;
            this.value = newValue;
            valueChanged();
            if (sequenceListeners != null) {
                if (oldValue != null)
                    for (int i = oldValue.size() - 1; i >= 0; i--)
                        onDelete(i, oldValue.get(i));
                for (int i = 0; i < value.size(); i++)
                    onInsert(i, value.get(i));
            }
            previousValue = null;
        }
        return newValue;
    }

    protected void onInsert(int position, T element) {
        if (sequenceListeners != null) {
            for (SequenceChangeListener<? super T> listener : sequenceListeners) {
                listener.onInsert(position, element);
            }
        }
    }

    protected void onReplaceElement(int position, T oldValue, T newValue) {
        if (sequenceListeners != null) {
            for (SequenceChangeListener<? super T> listener : sequenceListeners) {
                listener.onReplace(position, oldValue, newValue);
            }
        }
    }

    protected void onDelete(int position, T element) {
        if (sequenceListeners != null) {
            for (SequenceChangeListener<? super T> listener : sequenceListeners) {
                listener.onDelete(position, element);
            }
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return value.iterator();
    }

    @Override
    public T get(int position) {
        return value.get(position);
    }

    @Override
    public Sequence<T> get() {
        return value;
    }

    @Override
    public Sequence<T> set(Sequence<? extends T> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteValue(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirst(Sequence<? extends T> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
        throw new UnsupportedOperationException();
    }
}
