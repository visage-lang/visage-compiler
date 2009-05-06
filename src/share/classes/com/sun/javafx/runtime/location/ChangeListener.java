package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.*;

/**
 * ChangeListener
 *
 * @author Brian Goetz
 */
public class ChangeListener<T> extends AbstractLocationDependency {

    public void onChange(T oldValue, T newValue) {
        throw new UnsupportedOperationException();
    }

    /** Listener for when a sequence location changes.
     * @param buffer If non-null, an ArraySequence that was modified in-place.
     * @param oldValue If non-null, the old sequence values.
     * @param startPos The index of the start of the modification.
     * @param endPos The index (exclusive) of the of the modification.
     *   This is one position past the last replaced element in oldValue.
     * @param newElements If non-null, the replacment values.
     * Either buffer must be non-null, or both oldValues and newElements must be non-null.
     */
    public void onChange(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
        throw new UnsupportedOperationException();
    }
    
    public void onChange(byte oldValue, byte newValue) {
        onChange((T) (Byte) oldValue, (T) (Byte) newValue);
    }

    public void onChange(short oldValue, short newValue) {
        onChange((T) (Short) oldValue, (T) (Short) newValue);
    }

    public void onChange(int oldValue, int newValue) {
        onChange((T) (Integer) oldValue, (T) (Integer) newValue);
    }

    public void onChange(long oldValue, long newValue) {
        onChange((T) (Long) oldValue, (T) (Long) newValue);
    }

    public void onChange(float oldValue, float newValue) {
        onChange((T) (Float) oldValue, (T) (Float) newValue);
    }

    public void onChange(double oldValue, double newValue) {
        onChange((T) (Double) oldValue, (T) (Double) newValue);
    }

    public void onChange(boolean oldValue, boolean newValue) {
        onChange((T) (Boolean) oldValue, (T) (Boolean) newValue);
    }

    public void onChange(char oldValue, char newValue) {
        onChange((T) (Character) oldValue, (T) (Character) newValue);
    }

    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_TRIGGER;
    }
}
