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

    static Pointer make(IntVariable location) {
        return new Pointer(location, Type.INTEGER);
    }

    static Pointer make(DoubleVariable location) {
        return new Pointer(location, Type.DOUBLE);
    }

    static Pointer make(BooleanVariable location) {
        return new Pointer(location, Type.BOOLEAN);
    }

    static Pointer make(SequenceVariable location) {
        return new Pointer(location, Type.SEQUENCE);
    }

    static Pointer make(ObjectVariable location) {
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
                return ((IntVariable) location).getAsInt();
            case DOUBLE:
                return ((DoubleVariable) location).getAsDouble();
            case BOOLEAN:
                return ((BooleanVariable) location).getAsBoolean();
            case SEQUENCE:
                return ((SequenceVariable<?>) location).getAsSequence();
            default:
                return ((ObjectVariable<?>) location).get();
        }
    }

    public void set(Object value) {
        switch (type) {
            case INTEGER:
                ((IntVariable) location).setAsInt(((Number)value).intValue());
                break;
            case DOUBLE:
                ((DoubleVariable) location).setAsDouble(((Number)value).doubleValue());
                break;
            case BOOLEAN:
                ((BooleanVariable) location).setAsBoolean((Boolean) value);
                break;
            case SEQUENCE:
                ((SequenceVariable) location).setAsSequence((Sequence) value);
                break;
            case OBJECT:
                ((ObjectVariable) location).set(value);
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
