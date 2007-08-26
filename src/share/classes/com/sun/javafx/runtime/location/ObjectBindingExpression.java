package com.sun.javafx.runtime.location;

/**
 * ObjectBindingExpression is an interface that represents a bound expression.
*
* @author Brian Goetz
*/
public interface ObjectBindingExpression<T> {
    /** Calculate the current value of the expression */
    public T get();
}
