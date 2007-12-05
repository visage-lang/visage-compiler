package com.sun.javafx.runtime;

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.scenario.animation.Property;

/**
 * Pointers
 *
 * @author Brian Goetz
 */
public class Pointer implements Property<Object> {
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
                return ((IntLocation) location).get();
            case DOUBLE:
                return ((DoubleLocation) location).get();
            case BOOLEAN:
                return ((BooleanLocation) location).get();
            case SEQUENCE:
                return ((SequenceLocation<?>) location).get();
            default:
                return ((ObjectLocation<?>) location).get();
        }
    }

    public void set(Object value) {
        switch (type) {
            case INTEGER:
                ((IntLocation) location).set((Integer) value);
                break;
            case DOUBLE:
                ((DoubleLocation) location).set((Double) value);
                break;
            case BOOLEAN:
                ((BooleanLocation) location).set((Boolean) value);
                break;
            case SEQUENCE:
                ((SequenceLocation) location).set((Sequence) value);
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pointer pointer = (Pointer) o;

        if (location != null ? !location.equals(pointer.location) : pointer.location != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (location != null ? location.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
