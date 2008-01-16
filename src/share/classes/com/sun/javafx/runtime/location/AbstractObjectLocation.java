package com.sun.javafx.runtime.location;

/**
 * AbstractObjectLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractObjectLocation<T> extends AbstractLocation {
    protected T value;
    protected T previousValue;

    public AbstractObjectLocation(boolean valid, boolean lazy, T value) {
        super(valid, lazy);
        this.value = value;
    }

    public AbstractObjectLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public T get() {
        return value;
    }

    public T getPrevious() {
        return previousValue;
    }

    public boolean isNull() {
        return value == null;
    }

    protected boolean changed(T oldValue, T newValue) {
        if (oldValue == null) {
            return newValue != null;
        } else
            return !oldValue.equals(newValue);
    }

    public T set(T value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }
}
