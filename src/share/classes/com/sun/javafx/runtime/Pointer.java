/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.javafx.runtime;

import javafx.animation.KeyValueTarget;
import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.Sequence;


/**
 * Pointers
 *
 * @author Brian Goetz
 */
public class Pointer implements KeyValueTarget {

    private final Location location;
    private final Type type;

    public static Pointer make(Location location) {
        Type type = location instanceof IntLocation ? Type.INTEGER :
                location instanceof DoubleLocation ? Type.DOUBLE :
                        location instanceof FloatLocation ? Type.FLOAT :
                                location instanceof ByteLocation ? Type.BYTE :
                                        location instanceof LongLocation ? Type.LONG :
                                                location instanceof ShortLocation ? Type.SHORT :
                                                        location instanceof BooleanLocation ? Type.BOOLEAN :
                                                                location instanceof SequenceLocation ? Type.SEQUENCE :
                                                                        Type.OBJECT;
        return new Pointer(location, type);
    }

    static Pointer make(IntLocation location) {
        return new Pointer(location, Type.INTEGER);
    }

    static Pointer make(ShortLocation location) {
        return new Pointer(location, Type.SHORT);
    }

    static Pointer make(ByteLocation location) {
        return new Pointer(location, Type.BYTE);
    }

    static Pointer make(LongLocation location) {
        return new Pointer(location, Type.LONG);
    }

    static Pointer make(DoubleLocation location) {
        return new Pointer(location, Type.DOUBLE);
    }

    static Pointer make(FloatLocation location) {
        return new Pointer(location, Type.FLOAT);
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
        return (p1 == null) ? (p2 == null) : p1.equals(p2);
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
            case BYTE:
                return ((NumericLocation) location).getAsByte();
            case SHORT:
                return ((NumericLocation) location).getAsShort();
            case INTEGER:
                return ((NumericLocation) location).getAsInt();
            case LONG:
                return ((NumericLocation) location).getAsLong();
            case FLOAT:
                return ((NumericLocation) location).getAsFloat();
            case DOUBLE:
                return ((NumericLocation) location).getAsDouble();
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
            case BYTE:
                ((ByteLocation) location).setAsByte(((Number) value).byteValue());
                break;
            case SHORT:
                ((ShortLocation) location).setAsShort(((Number) value).shortValue());
                break;
            case INTEGER:
                ((IntLocation) location).setAsInt(((Number) value).intValue());
                break;
            case LONG:
                ((LongLocation) location).setAsLong(((Number) value).longValue());
                break;
            case FLOAT:
                ((FloatLocation) location).setAsFloat(((Number) value).floatValue());
                break;
            case DOUBLE:
                ((DoubleLocation) location).setAsDouble(((Number) value).doubleValue());
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
