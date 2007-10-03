package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for initializing JavaFX instances from object literals.
 *
 * @author Brian Goetz
 */
public class InitHelper {
    private final Location[] initOrder;
    private int initIndex;

    public InitHelper(int numFields) {
        this.initOrder = new Location[numFields];
    }

    public void add(Location loc) { initOrder[initIndex++] = loc; }

    public void initialize() {
        for (Location loc : initOrder)
            loc.valueChanged();
    }

    public static void assertNonNull(Location location, String name) {
        if (location != null)
            throw new IllegalStateException("Duplicate initialization for attribute: " + name);
    }

}
