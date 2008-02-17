package com.sun.javafx.runtime.location;

/**
 * ObjectConstant
 *
 * @author Brian Goetz
 */
public class ObjectConstant<T> extends AbstractConstantLocation<T> implements ObjectLocation<T> {
    private T $value;

    public static<T> ObjectLocation<T> make(T value) {
        return new ObjectConstant<T>(value);
    }

    protected ObjectConstant(T value) {
        this.$value = value;
    }

    public T get() {
        return $value;
    }

    public void addChangeListener(ObjectChangeListener<T> listener) { }
}
