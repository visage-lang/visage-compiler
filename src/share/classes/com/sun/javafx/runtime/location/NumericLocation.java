package com.sun.javafx.runtime.location;

/**
 * NumberLocation
 *
 * @author Brian Goetz
 */
public interface NumericLocation extends Location {
    public byte getAsByte();
    public short getAsShort();
    public int getAsInt();
    public long getAsLong();
    public float getAsFloat();
    public double getAsDouble();
}
