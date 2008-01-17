package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractBooleanLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractBooleanLocation extends AbstractLocation implements BooleanLocation {
    public static final boolean DEFAULT = false;

    protected boolean $value = DEFAULT;
    private List<BooleanChangeListener> replaceListeners;

    protected AbstractBooleanLocation(boolean valid, boolean lazy, boolean value) {
        super(valid, lazy);
        setAsBoolean(value);
    }

    protected AbstractBooleanLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public boolean getAsBoolean() {
        return $value;
    }

    public Boolean get() {
        return getAsBoolean();
    }

    public boolean isNull() {
        return false;
    }

    public boolean setAsBoolean(boolean value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public Boolean set(Boolean value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(BooleanChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<BooleanChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Boolean> listener) {
        addChangeListener(new BooleanChangeListener() {
            public void onChange(boolean oldValue, boolean newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(boolean oldValue, boolean newValue) {
        if (replaceListeners != null) {
            for (BooleanChangeListener listener : replaceListeners) {
                listener.onChange(oldValue, newValue);
            }
        }
    }

    protected boolean replaceValue(boolean newValue) {
        boolean oldValue = $value;
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
