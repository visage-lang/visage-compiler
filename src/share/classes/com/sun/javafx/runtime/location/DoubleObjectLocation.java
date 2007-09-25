package com.sun.javafx.runtime.location;

import java.util.Collection;

/**
 * Wrapper class that creates an ObjectLocation<Double> view of a DoubleLocation
 *
 * @author Brian Goetz
 */
class DoubleObjectLocation implements ObjectLocation<Double>, ViewLocation {
    private final DoubleLocation location;

    public DoubleObjectLocation(DoubleLocation location) {
        this.location = location;
    }

    public Double get() {
        return location.get();
    }

    public void set(Double value) {
        location.set(value);
    }

    public boolean isValid() {
        return location.isValid();
    }

    public boolean isLazy() {
        return location.isLazy();
    }

    public void invalidate() {
        location.invalidate();
    }

    public void update() {
        location.update();
    }

    public void addChangeListener(ChangeListener listener) {
        location.addChangeListener(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        location.addWeakListener(listener);
    }

    public ChangeListener getWeakChangeListener() {
        return location.getWeakChangeListener();
    }

    public Collection<ChangeListener> getListeners() {
        return location.getListeners();
    }

    public Location getUnderlyingLocation() {
        return location;
    }
    
    public void valueChanged() {
        location.valueChanged();
    }
}

class DoubleObjectMutableLocation extends DoubleObjectLocation implements MutableLocation {
    public DoubleObjectMutableLocation(DoubleLocation location) {
        super(location);
    }
}