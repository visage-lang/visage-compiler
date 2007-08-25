package com.sun.javafx.runtime.location;

/**
 * An integer-valued Location.  Provides int-valued get() and set() methods.
 *
 * @author Brian Goetz
 */
public interface IntLocation extends Location {
    /** Retrieve the current value of this location, recomputing if necessary */
    int get();

    /** Set the current value of this location, recomputing if necessary */
    void set(int value);
}
