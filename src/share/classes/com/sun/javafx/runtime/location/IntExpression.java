package com.sun.javafx.runtime.location;

/**
 * IntExpressionLocation
 *
 * @author Brian Goetz
 */
public class IntExpression extends AbstractLocation implements IntLocation {

    private final IntBindingExpression expression;
    private int value;

    public static IntLocation make(IntBindingExpression exp, Location... dependencies) {
        IntExpression loc = new IntExpression(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc);
        return loc;
    }

    public static IntLocation makeLazy(IntBindingExpression exp, Location... dependencies) {
        IntExpression loc = new IntExpression(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc);
        return loc;
    }

    private IntExpression(boolean lazy, IntBindingExpression expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public int get() {
        if (!isValid())
            update();
        return value;
    }

    public void set(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        value = expression.get();
        setValid();
    }
}
