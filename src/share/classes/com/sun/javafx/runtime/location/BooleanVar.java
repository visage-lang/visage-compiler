package com.sun.javafx.runtime.location;

/**
 * BooleanVar represents a simple boolean variable as a Location.  New BooleanVars are constructed with the make() factory
 * method.  BooleanVar values are always valid; it is an error to invalidate an BooleanVar.
 *
 * @author Brian Goetz
 */
public class BooleanVar extends AbstractLocation implements BooleanLocation, MutableLocation {
    private boolean value;


    public static BooleanLocation make() {
        return make(false);
    }

    public static BooleanLocation make(boolean value) {
        return new BooleanVar(value);
    }


    private BooleanVar(boolean value) {
        super(true, false);
        this.value = value;
    }


    public boolean get() {
        return value;
    }

    public boolean set(boolean value) {
        if (this.value != value) {
            this.value = value;
            valueChanged();
        }
        return value;
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public ObjectLocation<Boolean> asBooleanLocation() {
        return new BooleanObjectMutableLocation(this);
    }
}