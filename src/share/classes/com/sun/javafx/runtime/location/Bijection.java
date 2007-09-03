package com.sun.javafx.runtime.location;

/**
 * BijectiveObjectBindingExpression
*
* @author Brian Goetz
*/
public interface Bijection<T, U> {
    public U mapForwards(T a);
    public T mapBackwards(U b);
}
