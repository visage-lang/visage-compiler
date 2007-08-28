package com.sun.javafx.runtime.location;

/**
 * ObjectExpression represents an object-value bound expression.  Associated with an ObjectExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  ObjectExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class ObjectExpression<T> extends AbstractLocation implements ObjectLocation<T> {

    private final ObjectBindingExpression<T> expression;
    private T value;

    /** Create an ObjectExpression with the specified expression and dependencies. */
    public static<T> ObjectLocation<T> make(ObjectBindingExpression<T> exp, Location... dependencies) {
        ObjectExpression<T> loc = new ObjectExpression<T>(false, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc);
        return loc;
    }

    /** Create a lazy ObjectExpression with the specified expression and dependencies. */
    public static<T> ObjectLocation<T> makeLazy(ObjectBindingExpression<T> exp, Location... dependencies) {
        ObjectExpression<T> loc = new ObjectExpression<T>(true, exp);
        for (Location dep : dependencies)
            dep.addChangeListener(loc);
        return loc;
    }

    private ObjectExpression(boolean lazy, ObjectBindingExpression<T> expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public T get() {
        if (!isValid())
            update();
        return value;
    }

    public void set(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get();
            setValid();
        }
    }
}
