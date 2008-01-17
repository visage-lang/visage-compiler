package com.sun.javafx.runtime.location;

/**
 * BooleanChangeListener
 *
 * @author Brian Goetz
 */
public interface BooleanChangeListener {
    /** Notifies the listener that the contents of the location may have changed. */
    public void onChange(boolean oldValue, boolean newValue);
}
