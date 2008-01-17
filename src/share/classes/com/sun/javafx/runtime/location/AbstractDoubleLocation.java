package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractDoubleLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractDoubleLocation extends AbstractLocation implements DoubleLocation {
    public static final double DEFAULT = 0.0;

    protected double $value = DEFAULT;
    private List<DoubleChangeListener> replaceListeners;

    protected AbstractDoubleLocation(boolean valid, boolean lazy, double value) {
        super(valid, lazy);
        setAsDouble(value);
    }

    protected AbstractDoubleLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public double getAsDouble() {
        return $value;
    }

    public double setAsDouble(double value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public Double get() {
        return getAsDouble();
    }

    public Double set(Double value) {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }

    public void addChangeListener(DoubleChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<DoubleChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Double> listener) {
        addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(double oldValue, double newValue) {
        if (replaceListeners != null) {
            for (DoubleChangeListener listener : replaceListeners) {
                listener.onChange(oldValue, newValue);
            }
        }
    }

    protected double replaceValue(double newValue) {
        double oldValue = $value;
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
