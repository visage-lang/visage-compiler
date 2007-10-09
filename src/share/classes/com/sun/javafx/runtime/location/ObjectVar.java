package com.sun.javafx.runtime.location;

/**
 * ObjectVar represents an object-valued variable as a Location.  New ObjectVars are constructed with the make() factory
 * method.  ObjectVar values are always valid; it is an error to invalidate an ObjectVar.
 *
 * @author Brian Goetz
 */
public class ObjectVar<T> extends AbstractLocation implements ObjectLocation<T>, MutableLocation {
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

    public T set(T value) {
        if (this.value != value) {
            this.value = value;
            valueChanged();
        }
        return value;
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }
}
