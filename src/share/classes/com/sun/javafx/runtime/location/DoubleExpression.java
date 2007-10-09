package com.sun.javafx.runtime.location;

/**
 * DoubleExpression represents a double-value bound expression.  Associated with an DoubleExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  DoubleExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class DoubleExpression extends AbstractLocation implements DoubleLocation {

    private final DoubleBindingExpression expression;
    private double value;

    /** Create an DoubleExpression with the specified expression and dependencies. */
    public static DoubleLocation make(DoubleBindingExpression exp, Location... dependencies) {
        DoubleExpression loc = new DoubleExpression(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    /** Create a lazy DoubleExpression with the specified expression and dependencies. */
    public static DoubleLocation makeLazy(DoubleBindingExpression exp, Location... dependencies) {
        DoubleExpression loc = new DoubleExpression(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    private DoubleExpression(boolean lazy, DoubleBindingExpression expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public double get() {
        if (!isValid())
            update();
        return value;
    }

    public double set(double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
    }

    public ObjectLocation<Double> asDoubleLocation() {
        return new DoubleObjectLocation(this);
    }
}
