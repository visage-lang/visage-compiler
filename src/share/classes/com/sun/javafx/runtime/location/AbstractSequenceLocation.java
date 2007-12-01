package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.util.Iterator;

/**
 * AbstractSequenceLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequenceLocation<T> extends AbstractLocation implements SequenceLocation<T> {
    protected Sequence<? extends T> value, previousValue;
    protected boolean hasSequenceListeners;

    public AbstractSequenceLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public Sequence<? extends T> getPreviousValue() {
        return previousValue;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if (listener instanceof SequenceChangeListener)
            hasSequenceListeners = true;
        super.addChangeListener(listener);
    }

    /** Update the held value, notifying change listeners and generating appropriate delete/insert events as necessary */
    protected Sequence<? extends T> replaceValue(Sequence<? extends T> value) {
        if (value == null)
            throw new NullPointerException();
        Sequence<? extends T> oldValue = this.value;
        if (!value.equals(oldValue)) {
            previousValue = this.value;
            this.value = value;
            valueChanged();
            if (hasSequenceListeners) {
                if (oldValue != null)
                    for (int i=oldValue.size() - 1; i >= 0; i--)
                        onDelete(i, oldValue.get(i));
                for (int i=0; i< this.value.size(); i++)
                    onInsert(i, this.value.get(i));
            }
            previousValue = null;
        }
        return value;
    }

    protected void onInsert(int position, T element) {
        if (listeners != null && hasSequenceListeners) {
            for (ChangeListener listener : listeners) {
                if (listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onInsert(position, element);
                }
            }
        }
    }

    protected void onReplaceElement(int position, T oldValue, T newValue) {
        if (listeners != null && hasSequenceListeners) {
            for (ChangeListener listener : listeners) {
                if (listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onReplace(position, oldValue, newValue);
                }
            }
        }
    }

    protected void onDelete(int position, T element) {
        if (listeners != null && hasSequenceListeners) {
            for (ChangeListener listener : listeners) {
                if (listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onDelete(position, element);
                }
            }
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Iterator<? extends T> iterator() {
        return value.iterator();
    }

    @Override
    public T get(int position) {
        return value.get(position);
    }

    @Override
    public Sequence<? extends T> get() {
        return value;
    }

    @Override
    public Sequence<? extends T> set(Sequence<? extends T> value) {
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
