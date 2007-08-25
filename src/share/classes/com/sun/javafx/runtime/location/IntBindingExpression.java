package com.sun.javafx.runtime.location;

/**
 * IntBindingExpression is an interface that represents a bound expression.
*
* @author Brian Goetz
*/
public interface IntBindingExpression {
    /** Calculate the current value of the expression */
    public int get();
}
