package com.sun.javafx.runtime.location;

/**
 * An integer-valued Location.  Provides int-valued get() and set() methods.
 *
 * @author Brian Goetz
 */
public interface IntLocation extends Location {
    /** Retrieve the current value of this location, recomputing if necessary */
    public int get();

    /** Set the current value of this location, recomputing if necessary */
    public void set(int value);

    /** Get a reference to an ObjectLocation<Integer> that describes the same location */
    public ObjectLocation<Integer> asIntegerLocation();
}
