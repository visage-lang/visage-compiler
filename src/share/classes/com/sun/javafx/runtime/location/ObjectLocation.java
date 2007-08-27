package com.sun.javafx.runtime.location;

/**
 * An object-valued Location.  Provides object-valued get() and set() methods.
 *
 * @author Brian Goetz
 */
public interface ObjectLocation<T> extends Location {
    /** Retrieve the current value associated with this Location, recomputing if necessary */
    T get();

    /** Modify the value associated with this Location */
    void set(T value);
}
