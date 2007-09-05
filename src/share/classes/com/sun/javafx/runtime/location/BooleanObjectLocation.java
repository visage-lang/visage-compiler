package com.sun.javafx.runtime.location;

/**
 * Wrapper class that creates an ObjectLocation<Boolean> view of a BooleanLocation
 *
 * @author Brian Goetz
 */
class BooleanObjectLocation implements ObjectLocation<Boolean> {
    private final BooleanLocation location;

    public BooleanObjectLocation(BooleanLocation location) {
        this.location = location;
    }

    public Boolean get() {
        return location.get();
    }

    public void set(Boolean value) {
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
}

class BooleanObjectMutableLocation extends BooleanObjectLocation implements MutableLocation {
    public BooleanObjectMutableLocation(BooleanLocation location) {
        super(location);
    }
}