package com.sun.javafx.runtime.sequence;

import java.util.Iterator;

import com.sun.javafx.runtime.location.AbstractSequenceLocation;

/**
 * AbstractBoundSequence
 *
 * @author Brian Goetz
 */
public abstract class AbstractBoundSequence<T> extends AbstractSequenceLocation<T> {
    private boolean initialized = false;

    // AbstractBoundSequences start out in the invalid state, and go to the valid state exactly once,
    // and thereafter stay in the valid state.  They cannot be lazily bound.

    protected AbstractBoundSequence(Class<T> clazz) {
        super(clazz, false, false);
    }

    /** Called after construction to compute the value of the sequence */
    protected abstract Sequence<T> computeValue();

    /** Called once after construction so that listeners may be registered */
    protected abstract void initialize();

    protected void updateSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
        Sequence<T> oldValue = $value;
        $value = $value.replaceSlice(startPos, endPos, newValues);
        notifyListeners(startPos, endPos, newValues, oldValue, $value);
        valueChanged();
    }

    private void ensureValid() {
        if (!isValid()) {
            Sequence<T> value = computeValue();
            if (!initialized)
                initialize();
            replaceValue(value);
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
    public Sequence<T> getAsSequence() {
        ensureValid();
        return super.getAsSequence();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }
}
