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
    public static<T> T[] newArray(int size) {
        return (T[]) new Object[size];
    }

    @SuppressWarnings("unchecked")
    public static<T> Sequence<? extends T>[] newSequenceArray(int size) {
        return (Sequence<? extends T>[]) new Sequence[size];

    }
}
