package com.sun.javafx.runtime.location;

/**
 * BooleanConsatnt
 *
 * @author Brian Goetz
 */
public class BooleanConstant extends AbstractConstantLocation<Boolean> implements BooleanLocation {
    private boolean $value;

    public static BooleanLocation make(boolean value) {
        return new BooleanConstant(value);
    }

    protected BooleanConstant(boolean value) {
        this.$value = value;
    }


    public boolean getAsBoolean() {
        return $value;
    }

    public Boolean get() {
        return $value;
    }

    public boolean setAsBoolean(boolean value) {
        throw new UnsupportedOperationException();
    }

    public boolean setAsBooleanFromLiteral(boolean value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(BooleanChangeListener listener) { }
}
