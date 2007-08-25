package com.sun.javafx.runtime.location;

/**
 * IntLocation
 *
 * @author Brian Goetz
 */
public interface IntLocation extends Location {
    int get();

    void set(int value);
}
