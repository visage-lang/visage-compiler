package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;
import com.sun.javafx.runtime.sequence.Sequences;
import com.sun.javafx.runtime.sequence.SequenceMutator;

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
    private final SequenceMutator.Listener<T> mutationListener = new MutationListener();

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

    @Override
    public String toString() {
        return sequence.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return sequence.iterator();
    }

    @Override
    public T get(int position) {
        return sequence.get(position);
    }
    
    @Override
    public Sequence<T> get() {
        return sequence;
    }

    @Override
    public void set(Sequence<T> value) {
        Sequence<T> oldValue = sequence;
        if (!oldValue.equals(value)) {
            this.sequence = value;
            valueChanged();
            if (hasSequenceListeners) {
                for (int i=0; i<oldValue.size(); i++)
                    mutationListener.onDelete(i, oldValue.get(i));
                for (int i=0; i<sequence.size(); i++)
                    mutationListener.onInsert(i, sequence.get(i));
            }
        }
    }

    @Override
    public void set(int position, T newValue) {
        SequenceMutator.set(sequence, mutationListener, position, newValue);
    }

    @Override
    public void delete(int position) {
        SequenceMutator.delete(sequence, mutationListener, position);
    }

    @Override
    public void delete(SequencePredicate<T> sequencePredicate) {
        SequenceMutator.delete(sequence, mutationListener, sequencePredicate);
    }

    @Override
    public void deleteAll() {
        Sequence<T> deletedElements = sequence;
        sequence = Sequences.emptySequence(sequence.getElementType());
        for (int i=0; i<deletedElements.size(); i++)
            mutationListener.onDelete(i, deletedElements.get(i));
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
        SequenceMutator.insert(sequence, mutationListener, value);
    }

    @Override
    public void insert(Sequence<T> values) {
        SequenceMutator.insert(sequence, mutationListener, values);
    }

    public void insertFirst(T value) {
        SequenceMutator.insertFirst(sequence, mutationListener, value);
    }

    @Override
    public void insertFirst(Sequence<T> values) {
        SequenceMutator.insertFirst(sequence, mutationListener, values);
    }

    @Override
    public void insertBefore(T value, int position) {
        SequenceMutator.insertBefore(sequence, mutationListener, value, position);
    }

    @Override
    public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
        SequenceMutator.insertBefore(sequence, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertBefore(Sequence<T> values, int position) {
        SequenceMutator.insertBefore(sequence, mutationListener, values, position);
    }

    @Override
    public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        SequenceMutator.insertBefore(sequence, mutationListener, values, sequencePredicate);
    }

    @Override
    public void insertAfter(T value, int position) {
        SequenceMutator.insertAfter(sequence, mutationListener, value, position);
    }

    @Override
    public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
        SequenceMutator.insertAfter(sequence, mutationListener, value, sequencePredicate);
    }

    @Override
    public void insertAfter(Sequence<T> values, int position) {
        SequenceMutator.insertAfter(sequence, mutationListener, values, position);
    }

    @Override
    public void insertAfter(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
        SequenceMutator.insertAfter(sequence, mutationListener, values, sequencePredicate);
    }

    private class MutationListener implements SequenceMutator.Listener<T> {
        public void onReplaceSequence(Sequence<T> newSeq) {
            sequence = newSeq;
            valueChanged();
        }

        public void onInsert(int position, T element) {
            if (listeners != null && hasSequenceListeners) {
                for (ChangeListener listener : listeners) {
                    if (listener instanceof SequenceChangeListener) {
                        ((SequenceChangeListener<T>) listener).onInsert(position, element);
                    }
                }
            }
        }

        public void onDelete(int position, T element) {
            if (listeners != null && hasSequenceListeners) {
                for (ChangeListener listener : listeners) {
                    if (listener instanceof SequenceChangeListener) {
                        ((SequenceChangeListener<T>) listener).onDelete(position, element);
                    }
                }
            }
        }

        public void onReplaceElement(int position, T oldValue, T newValue) {
            if (listeners != null && hasSequenceListeners) {
                for (ChangeListener listener : listeners) {
                    if (listener instanceof SequenceChangeListener) {
                        ((SequenceChangeListener<T>) listener).onReplace(position, oldValue, newValue);
                    }
                }
            }
        }
    }
}
