package com.sun.javafx.runtime.sequence;

import com.sun.javafx.runtime.location.AbstractSequenceLocation;

import java.util.Iterator;

/**
 * AbstractBoundSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractSequenceLocation<T> {

    protected AbstractBoundSequence(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    protected abstract void computeInitial();

    private void ensureValid() {
        if (!isValid())
            computeInitial();
    }

    @Override
    public String toString() {
        ensureValid();
        return super.toString();
    }

    @Override
    public Iterator<? extends T> iterator() {
        ensureValid();
        return super.iterator();
    }

    @Override
    public T get(int position) {
        ensureValid();
        return super.get(position);
    }

    @Override
    public Sequence<? extends T> get() {
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
