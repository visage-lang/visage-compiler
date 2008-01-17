package com.sun.javafx.runtime.location;

/**
 * DoubleChangeListener
 *
 * @author Brian Goetz
 */
public interface DoubleChangeListener {
    /** Notifies the listener that the contents of the location may have changed. */
    public void onChange(double oldValue, double newValue);
}
