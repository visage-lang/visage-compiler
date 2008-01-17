package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractIntLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractIntLocation extends AbstractLocation implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;
    private List<IntChangeListener> replaceListeners;

    protected AbstractIntLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public int getAsInt() {
        return $value;
    }

    public int setAsInt(int value) {
        throw new UnsupportedOperationException();
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer set(Integer value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }

    public DoubleLocation asDoubleLocation() {
        return Locations.asDoubleLocation(this);
    }

    public void addChangeListener(IntChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<IntChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Integer> listener) {
        addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(int oldValue, int newValue) {
        if (replaceListeners != null) {
            for (IntChangeListener listener : replaceListeners) {
                listener.onChange(oldValue, newValue);
            }
        }
    }

    protected int replaceValue(int newValue) {
        int oldValue = $value;
        if (oldValue != newValue) {
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
        if ($value != DEFAULT)
            notifyListeners(DEFAULT, $value);
    }
}
