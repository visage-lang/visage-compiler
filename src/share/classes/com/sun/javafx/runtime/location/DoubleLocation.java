package com.sun.javafx.runtime.location;

/**
 * A double--valued Location.  Provides double-valued get() and set() methods.
 *
 * @author Brian Goetz
 */
public interface DoubleLocation extends Location {
    /** Retrieve the current value of this location, recomputing if necessary */
    double get();

    /** Set the current value of this location, recomputing if necessary */
    void set(double value);

    /** Get a reference to an ObjectLocation<Double> that describes the same location */
    public ObjectLocation<Double> asDoubleLocation();

}
