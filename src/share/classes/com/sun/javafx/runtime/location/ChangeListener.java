package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.*;

/**
 * ChangeListener's onChange methods are called after the value contained within
 * in a location is changed. There are two sets of methods: onChange and
 * onChangeB. The onChangeB methods return boolean flag that tells if this
 * listener has to be kept for future notifications or not. By default, onChangeB
 * methods call onChange version and return true. (i.e., keep the listener for
 * future notifications). A subclass may return false to indicate that this
 * listener may be removed.
 *
 * Compiler generates only onChange overrides for SBECL subclasses. But, compiler
 * generates onChange for on-replace triggers only - which have to be alive till
 * the watched location is alive. But, the runtime library's use of change listener
 * should override onChangeB methods and return appropriate listener aliveness to
 * avoid memory leaks. When overriding onChangeB, you may want to override the
 * correpoding onChange and call onChangeB version from it. This is to support
 * possible older callers of onChange API. Currently, the runtime code itself
 * calls only the onChangeB versions directly.
 *
 * @author Brian Goetz
 */
public class ChangeListener<T> extends AbstractLocationDependency {

    public void onChange(T oldValue, T newValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * return false if you want this listener to be removed.
     */
    public boolean onChangeB(T oldValue, T newValue) {
        onChange(oldValue, newValue);
        return true;
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
    
    /** Listener for when a sequence location changes.
     * @param buffer If non-null, an ArraySequence that was modified in-place.
     * @param oldValue If non-null, the old sequence values.
     * @param startPos The index of the start of the modification.
     * @param endPos The index (exclusive) of the of the modification.
     *   This is one position past the last replaced element in oldValue.
     * @param newElements If non-null, the replacment values.
     * Either buffer must be non-null, or both oldValues and newElements must be non-null.
     * @return false if you want this listener to be removed. return true to keep listening.
     */
    public boolean onChangeB(ArraySequence<T> buffer, Sequence<? extends T> oldValue, int startPos, int endPos, Sequence<? extends T> newElements) {
        onChange(buffer, oldValue, startPos, endPos, newElements);
        return true;
    }

    public void onChange(byte oldValue, byte newValue) {
        onChange((T) (Byte) oldValue, (T) (Byte) newValue);
    }

    public boolean onChangeB(byte oldValue, byte newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(short oldValue, short newValue) {
        onChange((T) (Short) oldValue, (T) (Short) newValue);
    }

    public boolean onChangeB(short oldValue, short newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(int oldValue, int newValue) {
        onChange((T) (Integer) oldValue, (T) (Integer) newValue);
    }

    public boolean onChangeB(int oldValue, int newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(long oldValue, long newValue) {
        onChange((T) (Long) oldValue, (T) (Long) newValue);
    }

    public boolean onChangeB(long oldValue, long newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(float oldValue, float newValue) {
        onChange((T) (Float) oldValue, (T) (Float) newValue);
    }

    public boolean onChangeB(float oldValue, float newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(double oldValue, double newValue) {
        onChange((T) (Double) oldValue, (T) (Double) newValue);
    }

    public boolean onChangeB(double oldValue, double newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(boolean oldValue, boolean newValue) {
        onChange((T) (Boolean) oldValue, (T) (Boolean) newValue);
    }

    public boolean onChangeB(boolean oldValue, boolean newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public void onChange(char oldValue, char newValue) {
        onChange((T) (Character) oldValue, (T) (Character) newValue);
    }

    public boolean onChangeB(char oldValue, char newValue) {
        onChange(oldValue, newValue);
        return true;
    }

    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_TRIGGER;
    }
}
