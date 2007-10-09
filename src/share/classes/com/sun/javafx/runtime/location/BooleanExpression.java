package com.sun.javafx.runtime.location;

/**
 * BooleanExpression represents a boolean-value bound expression.  Associated with an BooleanExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  BooleanExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class BooleanExpression extends AbstractLocation implements BooleanLocation {

    private final BooleanBindingExpression expression;
    private boolean value;

    /** Create an BooleanExpression with the specified expression and dependencies. */
    public static BooleanExpression make(BooleanBindingExpression exp, Location... dependencies) {
        BooleanExpression loc = new BooleanExpression(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    /** Create a lazy BooleanExpression with the specified expression and dependencies. */
    public static BooleanExpression makeLazy(BooleanBindingExpression exp, Location... dependencies) {
        BooleanExpression loc = new BooleanExpression(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc.getWeakChangeListener());
        return loc;
    }

    private BooleanExpression(boolean lazy, BooleanBindingExpression expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public boolean get() {
        if (!isValid())
            update();
        return value;
    }

    public boolean set(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
    }

    public ObjectLocation<Boolean> asBooleanLocation() {
        return new BooleanObjectLocation(this);
    }
}
