package com.sun.javafx.runtime.location;

/**
 * DoubleVar represents a simple double variable as a Location.  New DoubleVars are constructed with the make() factory
 * method.  DoubleVar values are always valid; it is an error to invalidate an DoubleVar.
 *
 * @author Brian Goetz
 */
public class DoubleVar extends AbstractLocation implements DoubleLocation, MutableLocation {
    private double value;


    public static DoubleLocation make() {
        return make(0);
    }

    public static DoubleLocation make(double value) {
        return new DoubleVar(value);
    }


    private DoubleVar(double value) {
        super(true, false);
        this.value = value;
    }


    public double get() {
        return value;
    }

    public void set(double value) {
        this.value = value;
        valueChanged();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Double> asDoubleLocation() {
        return new DoubleObjectMutableLocation(this);
    }
}
