package com.sun.javafx.runtime.location;

/**
 * SequenceBindingExpression is an interface that represents a bound expression.
*
* @author Brian Goetz
*/
public interface SequenceBindingExpression<T> {
    /** Calculate the current value of the expression */
    public SequenceLocation<T> get();
}
