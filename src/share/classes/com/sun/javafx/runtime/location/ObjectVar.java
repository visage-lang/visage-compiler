package com.sun.javafx.runtime.location;

/**
 * ObjectVariableLocation
 *
 * @author Brian Goetz
 */
public class ObjectVar<T> extends AbstractLocation implements ObjectLocation<T> {
    private T value;


    public static<T> ObjectLocation<T> make(T value) {
        return new ObjectVar<T>(value);
    }


    private ObjectVar(T value) {
        super(true, false);
        this.value = value;
    }


    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        valueChanged();
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }
}
