package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;

/**
 * AbstractSequenceLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractSequenceLocation<T> extends AbstractLocation implements SequenceLocation<T> {
    protected Sequence<T> value, previousValue;
    protected final Class<T> clazz;
    protected List<SequenceReplaceListener<T>> sequenceListeners;

    public AbstractSequenceLocation(Class<T> clazz, boolean valid, boolean lazy) {
        super(valid, lazy);
        this.clazz = clazz;
    }

    public Sequence<T> getPreviousValue() {
        return previousValue;
    }

    public void addChangeListener(SequenceReplaceListener<T> listener) {
        if (sequenceListeners == null)
            sequenceListeners = new ArrayList<SequenceReplaceListener<T>>();
        sequenceListeners.add(listener);
    }

    public void addChangeListener(final SequenceChangeListener<T> listener) {
        addChangeListener(new SequenceReplaceListener<T>() {
            public void onReplace(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
                int newSize = Sequences.size(newElements);
                if (endPos-startPos+1 == newSize && newSize == 1) {
                    for (int i=startPos; i<=endPos; i++)
                        listener.onReplace(i, oldValue.get(i), newValue.get(i));
                }
                else {
                    for (int i=endPos; i >= startPos; i--)
                        listener.onDelete(i, oldValue.get(i));
                    for (int i=0; i<newSize; i++)
                        listener.onInsert(startPos+i, newElements.get(i));
                }

            }
        });
    }

    /**
     * Update the held value, notifying change listeners and generating appropriate delete/insert events as necessary
     */
    protected Sequence<T> replaceValue(Sequence<T> newValue) {
        if (newValue == null)
            throw new NullPointerException();
        Sequence<T> oldValue = value;
        if (!equals(oldValue, newValue)) {
            previousValue = value;
            this.value = newValue;
            valueChanged();
            if (sequenceListeners != null)
                notifyListeners(0, Sequences.size(oldValue)-1, newValue, oldValue, newValue);
            previousValue = null;
        }
        return newValue;
    }

    protected void notifyListeners(int startPos, int endPos, Sequence<? extends T> newElements, Sequence<T> oldValue, Sequence<T> newValue) {
        if (sequenceListeners != null) {
            for (SequenceReplaceListener<T> listener : sequenceListeners) {
                listener.onReplace(startPos, endPos, newElements, oldValue, newValue);
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
    public T set(int position, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceSlice(int startPos, int endPos, Sequence<T> newValues) {
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
