package com.sun.javafx.runtime;

/**
 * Numerics
 *
 * @author Brian Goetz
 */
public class Numerics {
    public static boolean isNumber(Object o) {
        return o instanceof Number;
    }

    public static double toDouble(Object o) {
        return ((Number) o).doubleValue();
    }

    public static int toInt(Object o) {
        return ((Number) o).intValue();
    }
}
