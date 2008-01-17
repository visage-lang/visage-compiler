package com.sun.javafx.runtime.location;

import java.util.List;
import java.util.ArrayList;

/**
 * AbstractObjectLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractObjectLocation<T> extends AbstractLocation implements ObjectLocation<T> {
    protected T $value;
    private List<ObjectChangeListener<T>> replaceListeners;

    protected AbstractObjectLocation(boolean valid, boolean lazy, T value) {
        super(valid, lazy);
        set(value);
    }

    protected AbstractObjectLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public T get() {
        return $value;
    }

    public boolean isNull() {
        return $value == null;
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

    public void addChangeListener(ObjectChangeListener<T> listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<ObjectChangeListener<T>>();
        replaceListeners.add(listener);
    }

    protected void notifyListeners(T oldValue, T newValue) {
        if (replaceListeners != null) {
            for (ObjectChangeListener<T> listener : replaceListeners) {
                listener.onChange(oldValue, newValue);
            }
        }
    }

    protected T replaceValue(T newValue) {
        T oldValue = $value;
        if (changed(oldValue, newValue)) {
            $value = newValue;
            valueChanged();
            if (replaceListeners != null)
                notifyListeners(oldValue, newValue);
        }
        if (!isValid())
            setValid(false);
        return newValue;
    }

    public void fireInitialTriggers() {
        if ($value != null)
            notifyListeners(null, $value);
    }
}
