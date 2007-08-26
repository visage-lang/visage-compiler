package com.sun.javafx.runtime.location;

/**
 * DoubleBindingExpression is an interface that represents a bound expression.
 *
 * @author Brian Goetz
 */
public interface DoubleBindingExpression {
    /** Calculate the current value of the expression */
    public double get();
}
