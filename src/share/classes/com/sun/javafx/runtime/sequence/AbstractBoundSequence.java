package com.sun.javafx.runtime.sequence;

import java.util.Iterator;

import com.sun.javafx.runtime.location.AbstractSequenceLocation;

/**
 * AbstractBoundSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractSequenceLocation<T> {

    protected AbstractBoundSequence(Class<T> clazz, boolean valid, boolean lazy) {
        super(clazz, valid, lazy);
    }

    protected abstract Sequence<T> computeInitial();

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        previousValue = value;
        value = value.replaceSlice(startPos, endPos, newValues);
        notifyListeners(startPos, endPos, newValues, previousValue, value);
        valueChanged();
        previousValue = null;
    }

    private void ensureValid() {
        if (!isValid()) {
            value = computeInitial();
            setValid(true);
        }
    }

    @Override
    public String toString() {
        ensureValid();
        return super.toString();
    }

    @Override
    public Iterator<T> iterator() {
        ensureValid();
        return super.iterator();
    }

    @Override
    public T get(int position) {
        ensureValid();
        return super.get(position);
    }

    @Override
    public Sequence<T> get() {
        ensureValid();
        return super.get();
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }
}
