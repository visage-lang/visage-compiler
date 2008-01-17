package com.sun.javafx.runtime.location;

/**
 * AbstractIntLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractIntLocation extends AbstractLocation implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;
    protected int $previousValue = DEFAULT;

    protected AbstractIntLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public int getAsInt() {
        return $value;
    }

    public int getPreviousAsInt() {
        return $previousValue;
    }

    public int setAsInt(int value) {
        throw new UnsupportedOperationException();
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer getPrevious() {
        return getPreviousAsInt();
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
}
