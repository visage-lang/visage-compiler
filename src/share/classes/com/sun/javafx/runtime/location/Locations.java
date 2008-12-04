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

package com.sun.javafx.runtime.location;

import java.util.Iterator;

import com.sun.javafx.runtime.Numerics;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

/**
 * Factory methods for wrapping Locations: unmodifiable locations, ObjectLocation-typed views of primitive locations, etc
 *
 * @author Brian Goetz
 */
public class Locations {
    // non-instantiable
    private Locations() {
    }

    public static Location getUnderlyingLocation(Location loc) {
        while (loc instanceof DynamicViewLocation)
            loc = ((DynamicViewLocation) loc).getUnderlyingLocation();
        return loc;
    }

    public static IntLocation constant(int value) {
        return IntConstant.make(value);
    }

    public static DoubleLocation constant(double value) {
        return DoubleConstant.make(value);
    }

    public static BooleanLocation constant(boolean value) {
        return BooleanConstant.make(value);
    }

    public static<T> ObjectLocation<T> constant(T value) {
        return ObjectConstant.make(value);
    }

    public static<T> SequenceLocation<T> constant(TypeInfo<T> typeInfo, Sequence<T> value) {
        return SequenceConstant.make(typeInfo, value);
    }


    public static ObjectLocation<Integer> asObjectLocation(IntLocation loc) {
        return loc;
    }

    public static ObjectLocation<Double> asObjectLocation(DoubleLocation loc) {
        return loc;
    }

    public static ObjectLocation<Boolean> asObjectLocation(BooleanLocation loc) {
        return loc;
    }

    public static BooleanLocation asBooleanLocation(ObjectLocation<Boolean> loc) {
        return new ObjectBooleanLocation(loc);
    }

    public static <T extends Number> NumericLocation asNumericLocation(ObjectLocation<T> loc) {
        return new ObjectNumericLocation(loc);
    }

    // Was <T extends Number>; assumes T is Number
    public static <T> DoubleLocation asDoubleLocation(ObjectLocation<T> loc) {
        return new ObjectDoubleLocation<T>(loc);
    }

    public static DoubleLocation asDoubleLocation(IntLocation loc) {
        return new IntDoubleLocation(loc);
    }

    public static IntLocation asIntLocation(DoubleLocation loc) {
        return new DoubleIntLocation(loc);
    }

    public static IntLocation asIntLocation(ObjectLocation<Integer> loc) {
        return new ObjectIntLocation(loc);
    }

    public static IntLocation unmodifiableLocation(IntLocation loc) {
        return new UnmodifiableIntLocation(loc);
    }

    public static DoubleLocation unmodifiableLocation(DoubleLocation loc) {
        return new UnmodifiableDoubleLocation(loc);
    }

    public static BooleanLocation unmodifiableLocation(BooleanLocation loc) {
        return new UnmodifiableBooleanLocation(loc);
    }

    public static FloatLocation unmodifiableLocation(FloatLocation loc) {
        return new UnmodifiableFloatLocation(loc);
    }

    public static ShortLocation unmodifiableLocation(ShortLocation loc) {
        return new UnmodifiableShortLocation(loc);
    }

    public static ByteLocation unmodifiableLocation(ByteLocation loc) {
        return new UnmodifiableByteLocation(loc);
    }

    public static <T> ObjectLocation<T> unmodifiableLocation(ObjectLocation<T> loc) {
        return new UnmodifiableObjectLocation<T>(loc);
    }

    public static <T> SequenceLocation<T> unmodifiableLocation(SequenceLocation<T> loc) {
        return new UnmodifiableSequenceLocation<T>(loc);
    }

    /**
     * Return a wrapping location that mirrors the underlying Location but upcasts its type
     * @param clazz  Really Class<V> but because of generics limitations this cannot be declared
     * @param loc Location to wrap
     * @return
     */
    public static<T, V extends T> ObjectLocation<T> upcast(TypeInfo<V> typeInfo, ObjectLocation<V> loc) {
        return new UpcastLocation<T, V>(typeInfo, loc);
    }

    private static abstract class LocationWrapper<T extends Location> implements Location {
        protected final T location;

        protected LocationWrapper(T location) {
            this.location = location;
        }

        protected T getLocation() { return location; }

        public boolean isValid() {
            return getLocation().isValid();
        }

        public boolean isNull() {
            return getLocation().isNull();
        }

        public boolean isMutable() {
            return getLocation().isMutable();
        }

        public void invalidate() {
            getLocation().invalidate();
        }

        public void update() {
            getLocation().update();
        }

        public void addChangeListener(ChangeListener listener) {
            getLocation().addChangeListener(listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            getLocation().removeChangeListener(listener);
        }

        public void iterateChildren(DependencyIterator<? extends LocationDependency> closure) {
            getLocation().iterateChildren(closure);
        }

        public void addDependency(Location... location) {
            getLocation().addDependency(location);
        }

        public void addDependency(Location location) {
            getLocation().addDependency(location);
        }

        public void addDynamicDependency(Location location) {
            getLocation().addDynamicDependency(location);
        }

        public void clearDynamicDependencies() {
            getLocation().clearDynamicDependencies();
        }

        public void addDependentLocation(WeakLocation location) {
            getLocation().addDependentLocation(location);
        }
    }

    /**
     * Wrapper class that creates a DoubleLocation view of an IntLocation
     * @@@ May no longer be needed
     */
    private static class IntDoubleLocation extends LocationWrapper<IntLocation> implements DoubleLocation, StaticViewLocation {
        public IntDoubleLocation(IntLocation location) {
            super(location);
        }

        public double getAsDouble() {
            return location.getAsInt();
        }

        public Double get() {
            return getAsDouble();
        }

        public double setAsDouble(double value) {
            throw new UnsupportedOperationException();
        }

        public double setAsDoubleFromLiteral(double value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public Double set(Double value) {
            throw new UnsupportedOperationException();
        }

        public Double setFromLiteral(Double value) {
            throw new UnsupportedOperationException();
        }

        public Location getUnderlyingLocation() {
            return location;
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            location.addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange(oldValue, newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            location.addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }

        public int getAsInt() {
            return location.getAsInt();
        }

        public byte getAsByte() {
            return (byte) getAsDouble();
        }

        public short getAsShort() {
            return (short) getAsDouble();
        }

        public long getAsLong() {
            return (long) getAsDouble();
        }

        public float getAsFloat() {
            return (float) getAsDouble();
        }
    }

    private static class ObjectNumericLocation extends LocationWrapper<ObjectLocation<? extends Number>> implements NumericLocation, StaticViewLocation {
        private ObjectNumericLocation(ObjectLocation<? extends Number> location) {
            super(location);
        }

        public int getAsInt() {
            Integer val = Numerics.toInt(location.get());
            return val==null? 0 : val;
        }

        public byte getAsByte() {
            return (byte) getAsInt();
        }

        public short getAsShort() {
            return (short) getAsInt();
        }

        public long getAsLong() {
            return getAsInt();
        }

        public float getAsFloat() {
            return getAsInt();
        }

        public double getAsDouble() {
            return getAsInt();
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    // @@@ May no longer be needed
    private static class ObjectIntLocation extends LocationWrapper<ObjectLocation<Integer>> implements IntLocation, StaticViewLocation {
        private ObjectIntLocation(ObjectLocation<Integer> location) {
            super(location);
        }

        public int getAsInt() {
            Integer val = location.get();
            return val==null? 0 : val;
        }

        public int setAsInt(int value) {
            return location.set(value);
        }

        public int setAsIntFromLiteral(int value) {
            return location.setFromLiteral(value);
        }

        public void setDefault() {
            location.setDefault();
        }

        public void addChangeListener(final IntChangeListener listener) {
            location.addChangeListener(new ObjectChangeListener<Integer>() {
                public void onChange(Integer oldValue, Integer newValue) {
                    listener.onChange(oldValue, newValue);
                }
            });
        }

        public Integer get() {
            return location.get();
        }

        public byte getAsByte() {
            return (byte) getAsInt();
        }

        public short getAsShort() {
            return (short) getAsInt();
        }

        public long getAsLong() {
            return getAsInt();
        }

        public float getAsFloat() {
            return getAsInt();
        }

        public double getAsDouble() {
            return getAsInt();
        }

        public Integer set(Integer value) {
            return location.set(value);
        }

        public Integer setFromLiteral(Integer value) {
            return location.setFromLiteral(value);
        }

        public void addChangeListener(ObjectChangeListener<Integer> listener) {
            location.addChangeListener(listener);
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    // @@@ May no longer be needed
    // Was <T extends Number>; assumes T is Number
    private static class ObjectDoubleLocation<T> extends LocationWrapper<ObjectLocation<T>> implements DoubleLocation, StaticViewLocation {
        private ObjectDoubleLocation(ObjectLocation<T> location) {
            super(location);
        }

        public double getAsDouble() {
            T val = location.get();
            return val==null? 0.0 : Numerics.toDouble(val);
        }

        public double setAsDouble(double value) {
            // return location.set(value);
            throw new UnsupportedOperationException();
        }

        public double setAsDoubleFromLiteral(double value) {
            // return location.setFromLiteral(value);
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            location.setDefault();
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            location.addChangeListener(new ObjectChangeListener<T>() {
                public void onChange(T oldValue, T newValue) {
                    listener.onChange(Numerics.toDouble(oldValue), Numerics.toDouble(newValue));
                }
            });
        }

        public Double get() {
            return getAsDouble();
        }

        public Double set(Double value) {
            // return location.set(value);
            throw new UnsupportedOperationException();
        }

        public Double setFromLiteral(Double value) {
            // return location.setFromLiteral(value);
            throw new UnsupportedOperationException();
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            location.addChangeListener(new ObjectChangeListener<T>() {
                public void onChange(T oldValue, T newValue) {
                    listener.onChange(Numerics.toDouble(oldValue), Numerics.toDouble(newValue));
                }
            });
        }

        public byte getAsByte() {
            return (byte) getAsDouble();
        }

        public short getAsShort() {
            return (short) getAsDouble();
        }

        public int getAsInt() {
            return (int) getAsDouble();
        }

        public long getAsLong() {
            return (long) getAsDouble();
        }

        public float getAsFloat() {
            return (float) getAsDouble();
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    private static class ObjectBooleanLocation extends LocationWrapper<ObjectLocation<Boolean>> implements BooleanLocation, StaticViewLocation {
        private ObjectBooleanLocation(ObjectLocation<Boolean> location) {
            super(location);
        }

        public boolean getAsBoolean() {
            Boolean val = location.get();
            return val==null? false : val;
        }

        public boolean setAsBoolean(boolean value) {
            return location.set(value);
        }

        public boolean setAsBooleanFromLiteral(boolean value) {
            return location.setFromLiteral(value);
        }

        public void setDefault() {
            location.setDefault();
        }

        public void addChangeListener(final BooleanChangeListener listener) {
            location.addChangeListener(new ObjectChangeListener<Boolean>() {
                public void onChange(Boolean oldValue, Boolean newValue) {
                    listener.onChange(oldValue, newValue);
                }
            });
        }

        public Boolean get() {
            return location.get();
        }

        public Boolean set(Boolean value) {
            return location.set(value);
        }

        public Boolean setFromLiteral(Boolean value) {
            return location.setFromLiteral(value);
        }

        public void addChangeListener(ObjectChangeListener<Boolean> listener) {
            location.addChangeListener(listener);
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }


    /**
     * Wrapper class that creates an IntLocation view of a DoubleLocation
     */
    // @@@ May no longer be needed
    private static class DoubleIntLocation extends LocationWrapper<DoubleLocation> implements IntLocation, StaticViewLocation {
        public DoubleIntLocation(DoubleLocation location) {
            super(location);
        }

        public int getAsInt() {
            return (int)location.getAsDouble();
        }

        public Integer get() {
            return getAsInt();
        }

        public byte getAsByte() {
            return (byte) getAsInt();
        }

        public short getAsShort() {
            return (short) getAsInt();
        }

        public long getAsLong() {
            return getAsInt();
        }

        public float getAsFloat() {
            return getAsInt();
        }

        public int setAsInt(int value) {
            throw new UnsupportedOperationException();
        }

        public int setAsIntFromLiteral(int value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public Integer set(Integer value) {
            throw new UnsupportedOperationException();
        }

        public Integer setFromLiteral(Integer value) {
            throw new UnsupportedOperationException();
        }

        public Location getUnderlyingLocation() {
            return location;
        }

        public void addChangeListener(final IntChangeListener listener) {
            location.addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            location.addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }

        public double getAsDouble() {
            return location.getAsDouble();
        }
    }

    private abstract static class UnmodifiableLocationWrapper<T_VALUE, T_LOC extends Location> extends LocationWrapper<T_LOC> {
        protected UnmodifiableLocationWrapper(T_LOC location) {
            super(location);
        }

        public boolean isMutable() {
            return false;
        }

        public int setAsInt(int value) {
            throw new UnsupportedOperationException();
        }

        public int setAsIntFromLiteral(int value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public T_VALUE set(T_VALUE value) {
            throw new UnsupportedOperationException();
        }

        public T_VALUE setFromLiteral(T_VALUE value) {
            throw new UnsupportedOperationException();
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }

        public double setAsDouble(double value) {
            throw new UnsupportedOperationException();
        }

        public double setAsDoubleFromLiteral(double value) {
            throw new UnsupportedOperationException();
        }

        public float setAsFloat(float value) {
            throw new UnsupportedOperationException();
        }

        public float setAsFloatFromLiteral(float value) {
            throw new UnsupportedOperationException();
        }

        public short setAsShort(short value) {
            throw new UnsupportedOperationException();
        }

        public short setAsShortFromLiteral(short value) {
            throw new UnsupportedOperationException();
        }

        public byte setAsByte(byte value) {
            throw new UnsupportedOperationException();
        }

        public byte setAsByteFromLiteral(byte value) {
            throw new UnsupportedOperationException();
        }

        public boolean setAsBoolean(boolean value) {
            throw new UnsupportedOperationException();
        }

        public boolean setAsBooleanFromLiteral(boolean value) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableNumericLocationWrapper<T_LOC extends NumericLocation & ObjectLocation<T_VALUE>, T_VALUE> extends UnmodifiableLocationWrapper<T_VALUE, T_LOC> {
        private UnmodifiableNumericLocationWrapper(T_LOC location) {
            super(location);
        }

        protected T_LOC getLocation() {
            return location;
        }

        public int getAsInt() {
            return location.getAsInt();
        }

        public byte getAsByte() {
            return location.getAsByte();
        }

        public short getAsShort() {
            return location.getAsShort();
        }

        public long getAsLong() {
            return location.getAsLong();
        }

        public float getAsFloat() {
            return location.getAsFloat();
        }

        public double getAsDouble() {
            return location.getAsDouble();
        }

        public void addChangeListener(ObjectChangeListener<T_VALUE> listener) {
            location.addChangeListener(listener);
        }
    }

    /**
     * Wrapper class that wraps an IntLocation so it cannot be modified
     */
    private static class UnmodifiableIntLocation extends UnmodifiableNumericLocationWrapper<IntLocation, Integer> implements IntLocation {
        public UnmodifiableIntLocation(IntLocation location) {
            super(location);
        }

        public Integer get() {
            return getAsInt();
        }

        public void addChangeListener(IntChangeListener listener) {
            location.addChangeListener(listener);
        }
    }


    /**
     * Wrapper class that wraps a DoubleLocation so it cannot be modified
     */
    private static class UnmodifiableDoubleLocation extends UnmodifiableNumericLocationWrapper<DoubleLocation, Double> implements DoubleLocation {
        public UnmodifiableDoubleLocation(DoubleLocation location) {
            super(location);
        }

        public Double get() {
            return getAsDouble();
        }

        public void addChangeListener(DoubleChangeListener listener) {
            location.addChangeListener(listener);
        }
    }

    /**
     * Wrapper class that wraps a FloatLocation so it cannot be modified
     */
    private static class UnmodifiableFloatLocation extends UnmodifiableNumericLocationWrapper<FloatLocation, Float> implements FloatLocation {
        public UnmodifiableFloatLocation(FloatLocation location) {
            super(location);
        }

        public Float get() {
            return getAsFloat();
        }

        public void addChangeListener(FloatChangeListener listener) {
            location.addChangeListener(listener);
        }
    }

    /**
     * Wrapper class that wraps a ShortLocation so it cannot be modified
     */
    private static class UnmodifiableShortLocation extends UnmodifiableNumericLocationWrapper<ShortLocation, Short> implements ShortLocation {
        public UnmodifiableShortLocation(ShortLocation location) {
            super(location);
        }

        public Short get() {
            return getAsShort();
        }

        public void addChangeListener(ShortChangeListener listener) {
            location.addChangeListener(listener);
        }
    }

    /**
     * Wrapper class that wraps a ByteLocation so it cannot be modified
     */
    private static class UnmodifiableByteLocation extends UnmodifiableNumericLocationWrapper<ByteLocation, Byte> implements ByteLocation {
        public UnmodifiableByteLocation(ByteLocation location) {
            super(location);
        }

        public Byte get() {
            return getAsByte();
        }

        public void addChangeListener(ByteChangeListener listener) {
            location.addChangeListener(listener);
        }
    }


    /**
     * Wrapper class that wraps a BooleanLocation so it cannot be modified
     */
    private static class UnmodifiableBooleanLocation extends UnmodifiableLocationWrapper<Boolean, BooleanLocation> implements BooleanLocation {
        public UnmodifiableBooleanLocation(BooleanLocation location) {
            super(location);
        }

        public boolean getAsBoolean() {
            return location.getAsBoolean();
        }

        public Boolean get() {
            return getAsBoolean();
        }

        public void addChangeListener(BooleanChangeListener listener) {
            location.addChangeListener(listener);
        }

        public void addChangeListener(ObjectChangeListener<Boolean> listener) {
            location.addChangeListener(listener);
        }
    }

    /**
     * Wrapper class that wraps an ObjectLocation so it cannot be modified
     */
    private static class UnmodifiableObjectLocation<T> extends UnmodifiableLocationWrapper<T, ObjectLocation<T>> implements ObjectLocation<T> {
        public UnmodifiableObjectLocation(ObjectLocation<T> location) {
            super(location);
        }

        public T get() {
            return location.get();
        }

        public void addChangeListener(ObjectChangeListener<T> listener) {
            location.addChangeListener(listener);
        }
    }

    private static class UpcastLocation<T, V extends T> extends LocationWrapper<ObjectLocation<V>> implements ObjectLocation<T> {

        public UpcastLocation(TypeInfo<V> typeInfo, ObjectLocation<V> location) {
            super(location);
        }

        public V get() {
            return location.get();
        }

        public V set(T value) {
            throw new AssignToBoundException("Cannot assign to bound variable");
            // return location.set((V)value);
        }

        public T setFromLiteral(T value) {
            throw new AssignToBoundException("Cannot assign to bound variable");
            // return location.setFromLiteral((V)value);
        }

        public void setDefault() {
            throw new AssignToBoundException("Cannot assign to bound variable");
            // location.setDefault();
        }

        public void addChangeListener(final ObjectChangeListener<T> listener) {
            location.addChangeListener(new ObjectChangeListener<V>() {
                public void onChange(V oldValue, V newValue) {
                    listener.onChange(oldValue, newValue);
                }
            });
        }
    }

    /**
     * Wrapper class that wraps a SequenceLocation so it cannot be modified
     */
    private static class UnmodifiableSequenceLocation<T> extends UnmodifiableLocationWrapper<Sequence<T>, SequenceLocation<T>> implements SequenceLocation<T> {
        public UnmodifiableSequenceLocation(SequenceLocation<T> location) {
            super(location);
        }

        public T get(int position) {
            return location.get(position);
        }

        public TypeInfo<T> getElementType() {
            return location.getElementType();
        }

        public Sequence<T> getAsSequence() {
            return location.getAsSequence();
        }

        public Sequence<T> get() {
            return location.get();
        }

        public Iterator<T> iterator() {
            // @@@ Wrap iterator with unmodifiable iterator
            return location.iterator();
        }

        public Sequence<T> setAsSequence(Sequence<? extends T> value) {
            throw new UnsupportedOperationException();
        }

        public Sequence<T> setAsSequenceFromLiteral(Sequence<? extends T> value) {
            throw new UnsupportedOperationException();
        }

        public void addChangeListener(ObjectChangeListener<Sequence<T>> listener) {
            location.addChangeListener(listener);
        }

        public void addChangeListener(SequenceChangeListener<T> sequenceChangeListener) {
            location.addChangeListener(sequenceChangeListener);
        }

        public void removeChangeListener(SequenceChangeListener<T> sequenceChangeListener) {
            location.removeChangeListener(sequenceChangeListener);
        }

        public T set(int position, T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Sequence<T> getSlice(int startPos, int endPos) {
            return getAsSequence().getSlice(startPos, endPos);
        }

        public Sequence<? extends T> replaceSlice(int startPos, int endPos, Sequence<? extends T> newValues) {
            throw new UnsupportedOperationException();
        }

        public void delete(int position) {
            throw new UnsupportedOperationException();
        }

        public void deleteSlice(int startPos, int endPos) {
            throw new UnsupportedOperationException();
        }

        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        public void deleteValue(T value) {
            throw new UnsupportedOperationException();
        }

        public void delete(SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insert(T value) {
            throw new UnsupportedOperationException();
        }

        public void insert(Sequence<? extends T> values) {
            throw new UnsupportedOperationException();
        }

        public void insertFirst(T value) {
            throw new UnsupportedOperationException();
        }

        public void insertFirst(Sequence<? extends T> values) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(T value, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(Sequence<? extends T> values, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(T value, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(Sequence<? extends T> values, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(Sequence<? extends T> values, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }
    }
}
