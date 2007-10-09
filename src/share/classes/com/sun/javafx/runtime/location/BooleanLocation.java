package com.sun.javafx.runtime.location;

/**
 * A boolean-valued Location.  Provides boolean-valued get() and set() methods.
 *
 * @author Brian Goetz
 */
public interface BooleanLocation extends Location {
    /** Retrieve the current value of this location, recomputing if necessary */
    boolean get();

    /** Set the current value of this location, recomputing if necessary */
    boolean set(boolean value);

    /** Get a reference to an ObjectLocation<Boolean> that describes the same location */
    public ObjectLocation<Boolean> asBooleanLocation();
}
