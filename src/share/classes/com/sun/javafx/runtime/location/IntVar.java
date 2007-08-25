package com.sun.javafx.runtime.location;

/**
 * IntLocation
 *
 * @author Brian Goetz
 */
public class IntVar extends AbstractLocation implements IntLocation {
    private int value;


    public static IntLocation make() {
        return make(0);
    }

    public static IntLocation make(int value) {
        return new IntVar(value);
    }


    private IntVar(int value) {
        super(true, false);
        this.value = value;
    }


    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
        valueChanged();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }
}
