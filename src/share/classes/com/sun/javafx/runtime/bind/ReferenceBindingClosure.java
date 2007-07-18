package com.sun.javafx.runtime.bind;

/**
 * ReferenceBindingClosure
 *
 * @author Brian Goetz
 */
public abstract class ReferenceBindingClosure implements BindingClosure {
    public int asInt() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }
}
