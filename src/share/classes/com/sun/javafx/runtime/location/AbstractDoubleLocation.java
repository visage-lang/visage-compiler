package com.sun.javafx.runtime.location;

/**
 * AbstractDoubleLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractDoubleLocation extends AbstractLocation implements DoubleLocation {
    public static final double DEFAULT = 0.0;

    protected double $value = DEFAULT;
    protected double $previousValue = DEFAULT;

    protected AbstractDoubleLocation(boolean valid, boolean lazy, double value) {
        super(valid, lazy);
        this.$value = value;
    }

    protected AbstractDoubleLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public double getAsDouble() {
        return $value;
    }

    public double getPreviousAsDouble() {
        return $previousValue;
    }

    public double setAsDouble(double value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public Double get() {
        return getAsDouble();
    }

    public Double getPrevious() {
        return getPreviousAsDouble();
    }

    public Double set(Double value) {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }
}
