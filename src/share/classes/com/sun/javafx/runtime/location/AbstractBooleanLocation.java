package com.sun.javafx.runtime.location;

/**
 * AbstractBooleanLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractBooleanLocation extends AbstractLocation implements BooleanLocation {
    public static final boolean DEFAULT = false;

    protected boolean $value = DEFAULT;
    protected boolean $previousValue = DEFAULT;

    protected AbstractBooleanLocation(boolean valid, boolean lazy, boolean value) {
        super(valid, lazy);
        this.$value = value;
    }

    protected AbstractBooleanLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public boolean getAsBoolean() {
        return $value;
    }

    public boolean getPreviousAsBoolean() {
        return $previousValue;
    }

    public Boolean get() {
        return getAsBoolean();
    }

    public Boolean getPrevious() {
        return getPreviousAsBoolean();
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
}
