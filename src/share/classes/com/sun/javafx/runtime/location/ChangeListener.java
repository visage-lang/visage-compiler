package com.sun.javafx.runtime.location;

/**
 * ChangeListeners are notified when a Location's value may have changed.
 *
 * @see Location
 * @author Brian Goetz
 */
public interface ChangeListener {
    /** Notifies the listener that the contents of the location may have changed.
     *
     * @return a boolean value indicating whether this listener is still valid.  Returning false will cause the
     * listener to be removed from the listener list.  For listeners that do their own weak reference management,
     * they should return false when the relevant weak references have been reported as cleared.  
     */
    public boolean onChange();
}
