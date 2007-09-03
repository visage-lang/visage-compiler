package com.sun.javafx.runtime.location;

/**
 * A Location represents any value on which another variable may express a dependency, including binding dependency
 * and change triggers.  The location is assumed to have a value, whose type is determined by the subinterfaces
 * IntLocation, ObjectLocation, SequenceLocation, etc.
 *
 * The value assocated with a location may be valid or invalid.  If the value is invalid, it will be updated either
 * when the update() method is called, or when the value is retrieved.
 *
 * Change listeners can be registered with a location, and are notified whenever the value associated with the location
 * changes.  Locations may be lazy; this means that change listeners will be notified when the value is invalidated,
 * but the new value will not be recomputed until it is asked for.
 *
 * @author Brian Goetz
 */
public interface Location {
    /** Is the value associated with this location currently valid, or would it have to be recomputed? */
    public boolean isValid();

    /** Is this location lazy? */
    public boolean isLazy();

    /** Invalidate the value associated with this location, and call all registered change listeners.  If the location
     * is lazy, the value is not immediately recomputed, otherwise it is.
     */
    public void invalidate();

    /** Recompute the current value */
    public void update();

    /** Register a change listener that will be notified whenever this location may have changed.  Locations are allowed
     * to notify change listeners spuriously.
     */
    public void addChangeListener(ChangeListener listener);

    /** Register a change listener that will be notified whenever this location may have changed, but use a weak
     * reference for the listener, so that the listener list does not pin the listener in memory after it otherwise
     * could be collected.
     */
    public void addWeakListener(ChangeListener listener);

    /** Return a change listener that holds a weak reference to this Location, so that maintenance of the change
     * listener in a listener list will not pin this object in memory.
     */
    public ChangeListener getWeakChangeListener();
}
