package com.sun.javafx.runtime.location;

/**
 * Location
 *
 * @author Brian Goetz
 */
public interface Location {
    public boolean isValid();
    public boolean isLazy();

    public void invalidate();
    public void update();

    public void addChangeListener(ChangeListener listener);
}
