package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;

/**
 * SequenceBindingExpression is an interface that represents a bound expression.
*
* @author Brian Goetz
*/
public interface SequenceBindingExpression<T> {
    /** Calculate the current value of the expression */
    public Sequence<T> get();
}
