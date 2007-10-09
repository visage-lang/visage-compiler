package com.sun.javafx.runtime.location;

/**
 * IntVar represents a simple Integer variable as a Location.  New IntVars are constructed with the make() factory
 * method.  IntVar values are always valid; it is an error to invalidate an IntVar.
 *
 * @author Brian Goetz
 */
public class IntVar extends AbstractLocation implements IntLocation, MutableLocation {
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

    public int set(int value) {
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

    public ObjectLocation<Integer> asIntegerLocation() {
        return new IntObjectMutableLocation(this);
    }
}
