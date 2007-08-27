package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * SequenceVar represents a sequence-valued variable as a Location.
 * New SequenceVars are constructed with the make() factory
 * method.  SequenceVar values are always valid; it is an error to invalidate a SequenceVar.
 *
 * @author Brian Goetz
 */
public class SequenceVar<T> extends AbstractLocation implements SequenceLocation<T> {

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
            for (Iterator<WeakReference<ChangeListener>> iterator = listeners.iterator(); iterator.hasNext();) {
                WeakReference<ChangeListener> ref = iterator.next();
                ChangeListener listener = ref.get();
                if (listener == null) {
                    iterator.remove();
                    continue;
                }
                listener.onChange();
                if (hasSequenceListeners && listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onReplace(position, oldValue, newValue);
                }
            }
        }
    }

    private void elementDeleted(T oldValue) {
        if (listeners != null) {
            for (Iterator<WeakReference<ChangeListener>> iterator = listeners.iterator(); iterator.hasNext();) {
                WeakReference<ChangeListener> ref = iterator.next();
                ChangeListener listener = ref.get();
                if (listener == null) {
                    iterator.remove();
                    continue;
                }
                listener.onChange();
                if (hasSequenceListeners && listener instanceof SequenceChangeListener) {
                    ((SequenceChangeListener<T>) listener).onDelete(oldValue);
                }
            }
        }
    }

    private void elementInserted(T newValue) {
        if (listeners != null) {
            for (Iterator<WeakReference<ChangeListener>> iterator = listeners.iterator(); iterator.hasNext();) {
                WeakReference<ChangeListener> ref = iterator.next();
                ChangeListener listener = ref.get();
                if (listener == null) {
                    iterator.remove();
                    continue;
                }
                listener.onChange();
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

    public Sequence<T> get() {
        return sequence;
    }

    public void set(Sequence<T> value) {
        this.sequence = value;
        valueChanged();
    }

    public void set(int position, T newValue) {
        T oldValue = sequence.get(position);
        sequence = sequence.set(position, newValue);
        elementChanged(position, oldValue, newValue);
    }

    public void delete(int position) {
        int oldSize = sequence.size();
        T oldValue = sequence.get(position);
        sequence = sequence.delete(position);
        if (sequence.size() < oldSize)
            elementDeleted(oldValue);
    }

    public void delete(SequencePredicate<T> sequencePredicate) {
        Sequence<T> deletedElements = sequence.get(sequencePredicate);
        sequence = sequence.delete(sequencePredicate);
        elementDeleted(deletedElements);
    }

    public void insert(T value) {
        sequence = sequence.insert(value);
        elementInserted(value);
    }

    public void insert(Sequence<T> values) {
        sequence = sequence.insert(values);
        elementInserted(values);
    }

    public void insertFirst(T value) {
        sequence = sequence.insertFirst(value);
        elementInserted(value);
    }

    public void insertFirst(Sequence<T> values) {
        sequence = sequence.insertFirst(values);
        elementInserted(values);
    }

    public void insertBefore(T value, int position) {
        sequence = sequence.insertBefore(value, position);
        elementInserted(value);
    }

    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertBefore(value, sequencePredicate);
        int newSize = sequence.size();
        for (int i = 0; i < newSize - oldSize; i++)
            elementInserted(value);
    }

    public void insertBefore(Sequence<T> values, int position) {
        sequence = sequence.insertBefore(values, position);
        elementInserted(values);
    }

    public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertBefore(values, sequencePredicate);
        int newSize = sequence.size();
        if (values.size() > 0) {
            for (int i = 0; i < (newSize - oldSize) / values.size(); i++)
                elementInserted(values);
        }
    }

    public void insertAfter(T value, int position) {
        sequence = sequence.insertAfter(value, position);
        elementInserted(value);
    }

    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        int oldSize = sequence.size();
        sequence = sequence.insertAfter(value, sequencePredicate);
        int newSize = sequence.size();
        for (int i = 0; i < newSize - oldSize; i++)
            elementInserted(value);
    }

    public void insertAfter(Sequence<T> values, int position) {
        sequence = sequence.insertAfter(values, position);
        elementInserted(values);
    }

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
