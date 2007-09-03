package com.sun.javafx.runtime.location;

/**
 * IntExpression represents an integer-value bound expression.  Associated with an IntExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  IntExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class IntExpression extends AbstractLocation implements IntLocation {

    private final IntBindingExpression expression;
    private int value;

    /** Create an IntExpression with the specified expression and dependencies. */
    public static IntLocation make(IntBindingExpression exp, Location... dependencies) {
        IntExpression loc = new IntExpression(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    /** Create a lazy IntExpression with the specified expression and dependencies. */
    public static IntLocation makeLazy(IntBindingExpression exp, Location... dependencies) {
        IntExpression loc = new IntExpression(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
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
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
    }

    public ObjectLocation<Integer> asIntegerLocation() {
        return new IntObjectLocation(this);
    }
}
