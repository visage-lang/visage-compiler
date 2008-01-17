package com.sun.javafx.runtime.location;

/**
 * IntChangeListener
 *
 * @author Brian Goetz
 */
public interface IntChangeListener {
    /** Notifies the listener that the contents of the location may have changed. */
    public void onChange(int oldValue, int newValue);
}
