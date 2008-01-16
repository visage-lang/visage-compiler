package com.sun.javafx.runtime;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * Utility class for various static utility methods, such as methods that launder generic type errors that are
 * known to be safe.
 *
 * @author Brian Goetz
 */
public class Util {
    @SuppressWarnings("unchecked")
    public static<T> T[] newObjectArray(int size) {
        return (T[]) new Object[size];
    }

    @SuppressWarnings("unchecked")
    public static<T> Sequence<T>[] newSequenceArray(int size) {
        return (Sequence<T>[]) new Sequence[size];

    }

    public static int powerOfTwo(int current, int desired) {
        int capacity = current;
        while (capacity < desired)
            capacity <<= 1;
        return capacity;
    }

    public static<T> T defaultValue(Class<T> clazz) {
        if (clazz == Integer.class)
            return (T) Integer.valueOf(0);
        else if (clazz == Double.class)
            return (T) Double.valueOf(0.0);
        else if (clazz == Boolean.class)
            return (T) Boolean.FALSE;
        else
            return null;
    }
}
