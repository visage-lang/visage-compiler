package com.sun.javafx.runtime;

/**
 * Location
 *
 * @author Brian Goetz
 */
public interface Location {
    public double asDouble();
    public Object asObject();
    public boolean asBoolean();
    public int asInt();

    public void setDouble(double value);
    public void setObject(Object value);
    public void setBoolean(boolean value);
    public void setInt(int value);

    public void addWeakChangeListener(ChangeListener listener, Object key);
    public void addChangeListener(ChangeListenerEntry entry);
}
