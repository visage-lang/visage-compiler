package com.sun.javafx.runtime.location;

/**
 * ChangeListeners are notified when a Location's value may have changed.
 *
 * @see Location
 * @author Brian Goetz
 */
public interface ChangeListener {
    public void onChange();
}
