package com.sun.javafx.runtime.location;

/**
 * ObjectLocation
 *
 * @author Brian Goetz
 */
public interface ObjectLocation<T> {
    T get();

    void set(T value);
}
