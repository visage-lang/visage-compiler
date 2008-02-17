package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;

/**
 * AbstractConstantLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractConstantLocation<T> implements ObjectLocation<T> {
    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public T set(T value) {
        throw new UnsupportedOperationException();
    }

    public T setFromLiteral(T value) {
        throw new UnsupportedOperationException();
    }

    public boolean isValid() {
        return true;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isMutable() {
        return false;
    }

    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public void update() { }

    public void addChangeListener(ChangeListener listener) { }

    public void addWeakListener(ChangeListener listener) { }

    public void addDependentLocation(WeakReference<Location> location) { }

    public Collection<ChangeListener> getListeners() {
        return Collections.emptyList();
    }

    public void addDependencies(Location... location) { }

    public void addDynamicDependency(Location location) { }

    public void clearDynamicDependencies() { }

    public void addChangeListener(ObjectChangeListener<T> listener) { }
}
