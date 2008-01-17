package com.sun.javafx.runtime.location;

/**
 * ObjectChangeListener
 *
 * @author Brian Goetz
 */
public interface ObjectChangeListener<T> {
    /** Notifies the listener that the contents of the location may have changed. */
    public void onChange(T oldValue, T newValue);
}
