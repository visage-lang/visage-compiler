package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;

import java.util.Iterator;

/**
 * SequenceVar represents a sequence-valued variable as a Location.
 * New SequenceVars are constructed with the make() factory
 * method.  SequenceVar values are always valid; it is an error to invalidate a SequenceVar.
 *
 * @author Brian Goetz
 */
public class SequenceVar<T> extends AbstractLocation implements SequenceLocation<T>, MutableLocation {

    private Sequence<T> sequence;
    private boolean hasSequenceListeners;

    public static <T> SequenceVar<T> make(Sequence<T> value) {
        return new SequenceVar<T>(value);
    }

    private SequenceVar(Sequence<T> value) {
        super(true, false);
        this.sequence = value;
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void addChangeListener(ChangeListener listener) {
        if (listener instanceof SequenceChangeListener)
            hasSequenceListeners = true;
        super.addChangeListener(listener);
    }

    private void elementChanged(int position, T oldValue, T newValue) {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange()) {
                    iterator.remove();
                    continue;
                }
                if (hasSequenceListeners && listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onReplace(position, oldValue, newValue);
                }
            }
        }
    }

    private void elementDeleted(T oldValue) {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange()) {
                    iterator.remove();
                    continue;
                }
                if (hasSequenceListeners && listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onDelete(oldValue);
                }
            }
        }
    }

    private void elementInserted(T newValue) {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange()) {
                    iterator.remove();
                    continue;
                }
                if (hasSequenceListeners && listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onInsert(newValue);
                }
            }
        }
    }

    private void elementInserted(Sequence<T> values) {
        for (T value : values)
            elementInserted(value);
    }

    private void elementDeleted(Sequence<T> values) {
        for (T value : values)
            elementDeleted(value);
    }

    @Override
    public String toString() {
        return getSequence().toString(); 
    }

    @Override
    public Iterator<T> iterator() {
        return getSequence().iterator(); 
    }

    @Override
    public T get(int position) {
        return getSequence().get(position);
    }
    
    @Override
    public SequenceLocation<T> get() {
        return this;
    }

    @Override
    public void set(SequenceLocation<T> value) {
        set(value.getSequence());
    }

    @Override
    public Sequence<T> getSequence() {
        return sequence;
    }

    @Override
    public void set(Sequence<T> value) {
        this.sequence = value;
        valueChanged();
    }

    @Override
    public void set(int position, T newValue) {
        T oldValue = sequence.get(position);
        sequence = sequence.set(position, newValue);
        elementChanged(position, oldValue, newValue);
    }

    @Override
    public void delete(int position) {
        int oldSize = sequence.size();
        T oldValue = sequence.get(position);
        sequence = sequence.delete(position);
        if (sequence.size() < oldSize)
            elementDeleted(oldValue);
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        Sequence<T> deletedElements = sequence.get(sequencePredicate);
        sequence = sequence.delete(sequencePredicate);
        elementDeleted(deletedElements);
    }

    @Override
    public void deleteAll() {
        Sequence<T> deletedElements = sequence;
        sequence = Sequences.emptySequence(sequence.getElementType());
        elementDeleted(deletedElements);
    }

    @Override
    public void deleteValue(final T targetValue) {
        delete(new SequencePredicate<T>() {
            public boolean matches(Sequence<T> sequence, int index, T value) {
                return ((value == null && targetValue == null || value.equals(targetValue)));
            }
        });
    }

    @Override
    public void insert(T value) {
        sequence = sequence.insert(value);
        elementInserted(value);
    }

    @Override
    public void insert(Sequence<T> values) {
        sequence = sequence.insert(values);
        elementInserted(values);
    }

    public void insertFirst(T value) {
        sequence = sequence.insertFirst(value);
        elementInserted(value);
    }

    @Override
    public void insertFirst(Sequence<T> values) {
        sequence = sequence.insertFirst(values);
        elementInserted(values);
    }

    @Override
    public void insertBefore(T value, int position) {
        sequence = sequence.insertBefore(value, position);
        elementInserted(value);
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertBefore(value, sequencePredicate);
        int newSize = sequence.size();
        for (int i = 0; i < newSize - oldSize; i++)
            elementInserted(value);
    }

    @Override
    public void insertBefore(Sequence<T> values, int position) {
        sequence = sequence.insertBefore(values, position);
        elementInserted(values);
    }

    @Override
    public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertBefore(values, sequencePredicate);
        int newSize = sequence.size();
        if (values.size() > 0) {
            for (int i = 0; i < (newSize - oldSize) / values.size(); i++)
                elementInserted(values);
        }
    }

    @Override
    public void insertAfter(T value, int position) {
        sequence = sequence.insertAfter(value, position);
        elementInserted(value);
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertAfter(value, sequencePredicate);
        int newSize = sequence.size();
        for (int i = 0; i < newSize - oldSize; i++)
            elementInserted(value);
    }

    @Override
    public void insertAfter(Sequence<T> values, int position) {
        sequence = sequence.insertAfter(values, position);
        elementInserted(values);
    }

    @Override
    public void insertAfter(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertAfter(values, sequencePredicate);
        int newSize = sequence.size();
        if (values.size() > 0) {
            for (int i = 0; i < (newSize - oldSize) / values.size(); i++)
                elementInserted(values);
        }
    }
}
