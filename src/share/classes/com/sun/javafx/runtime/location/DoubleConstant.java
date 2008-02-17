package com.sun.javafx.runtime.location;

/**
 * DoubleConstant
 *
 * @author Brian Goetz
 */
public class DoubleConstant extends AbstractConstantLocation<Double> implements DoubleLocation {
    private double $value;

    public static DoubleLocation make(double value) {
        return new DoubleConstant(value);
    }

    protected DoubleConstant(double value) {
        this.$value = value;
    }


    public double getAsDouble() {
        return $value;
    }

    public Double get() {
        return $value;
    }

    public double setAsDouble(double value) {
        throw new UnsupportedOperationException();
    }

    public double setAsDoubleFromLiteral(double value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(DoubleChangeListener listener) { }
}
