package com.sun.javafx.runtime.location;

/**
 * When computing dependency graphs of locations, some locations are really just views on another.  A ViewLocation
 * provides a means of finding out the underlying location.
 *
 * @author Brian Goetz
 */
public interface ViewLocation extends Location {

    /** Get the Location that this location "really" represents */
    public Location getUnderlyingLocation();
}
