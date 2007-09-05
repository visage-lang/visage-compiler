package com.sun.javafx.runtime.location;

/**
 * BooleanBindingExpression is an interface that represents a bound expression.
 *
 * @author Brian Goetz
 */
public interface BooleanBindingExpression {
    /** Calculate the current value of the expression */
    public boolean get();
}
