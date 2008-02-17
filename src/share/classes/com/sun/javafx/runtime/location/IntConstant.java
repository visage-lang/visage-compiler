package com.sun.javafx.runtime.location;

/**
 * ConstantIntLocation
 *
 * @author Brian Goetz
 */
public class IntConstant extends AbstractConstantLocation<Integer> implements IntLocation {
    private int $value;

    public static IntLocation make(int value) {
        return new IntConstant(value);
    }

    protected IntConstant(int value) {
        this.$value = value;
    }


    public int getAsInt() {
        return $value;
    }

    public Integer get() {
        return $value;
    }

    public int setAsInt(int value) {
        throw new UnsupportedOperationException();
    }

    public int setAsIntFromLiteral(int value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(IntChangeListener listener) { }
}
