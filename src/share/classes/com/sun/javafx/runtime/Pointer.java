package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;


/**
 * Pointers
 *
 * @author Brian Goetz
 */
public class Pointer {
    public enum Type {
        INTEGER, DOUBLE, BOOLEAN, SEQUENCE, OBJECT
    }

    private final Location location;
    private final Type type;

    static Pointer make(IntLocation location) {
        return new Pointer(location, Type.INTEGER);
    }

    static Pointer make(DoubleLocation location) {
        return new Pointer(location, Type.DOUBLE);
    }

    static Pointer make(BooleanLocation location) {
        return new Pointer(location, Type.BOOLEAN);
    }

    static Pointer make(SequenceLocation location) {
        return new Pointer(location, Type.SEQUENCE);
    }

    static Pointer make(ObjectLocation location) {
        return new Pointer(location, Type.OBJECT);
    }

    public static boolean equals(Pointer p1, Pointer p2) {
        return p1.equals(p2);
    }

    private Pointer(Location location, Type type) {
        this.location = location;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Pointer unwrap() {
        return this;
    }
    
    public Object get() {
        switch (type) {
            case INTEGER:
                return ((IntLocation) location).getAsInt();
            case DOUBLE:
                return ((DoubleLocation) location).getAsDouble();
            case BOOLEAN:
                return ((BooleanLocation) location).getAsBoolean();
            case SEQUENCE:
                return ((SequenceLocation<?>) location).getAsSequence();
            default:
                return ((ObjectLocation<?>) location).get();
        }
    }

    public void set(Object value) {
        switch (type) {
            case INTEGER:
                ((IntLocation) location).setAsInt(((Number)value).intValue());
                break;
            case DOUBLE:
                ((DoubleLocation) location).setAsDouble(((Number)value).doubleValue());
                break;
            case BOOLEAN:
                ((BooleanLocation) location).setAsBoolean((Boolean) value);
                break;
            case SEQUENCE:
                ((SequenceLocation) location).setAsSequence((Sequence) value);
                break;
            case OBJECT:
                ((ObjectLocation) location).set(value);
                break;
        }
    }

    public Object getValue() {
        return get();
    }

    public void setValue(Object o) {
        set(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        else
            return (o instanceof Pointer)
                    && Locations.getUnderlyingLocation(location) == Locations.getUnderlyingLocation(((Pointer) o).location);
    }

    @Override
    public int hashCode() {
        Location loc = Locations.getUnderlyingLocation(location);
        return loc != null ? loc.hashCode() : 0;
    }
}
