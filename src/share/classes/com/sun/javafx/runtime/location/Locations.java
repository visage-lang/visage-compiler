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

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.TypeInfo;
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

    public static <T extends Number> DoubleLocation asDoubleLocation(ObjectLocation<T> loc) {
        return new ObjectDoubleLocation<T>(loc);
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

    private static class ObjectNumericLocation extends LocationWrapper<ObjectLocation<? extends Number>> implements NumericLocation, StaticViewLocation {
        private ObjectNumericLocation(ObjectLocation<? extends Number> location) {
            super(location);
        }

        public int getAsInt() {
            Integer val = location.get().intValue();
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
    private static class ObjectDoubleLocation<T extends Number> extends LocationWrapper<ObjectLocation<T>> implements DoubleLocation, StaticViewLocation {
        private ObjectDoubleLocation(ObjectLocation<T> location) {
            super(location);
        }

        public double getAsDouble() {
            T val = location.get();
            return val==null? 0.0 : val.doubleValue();
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
                    listener.onChange(oldValue.doubleValue(), newValue.doubleValue());
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
                    listener.onChange(oldValue.doubleValue(), newValue.doubleValue());
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

    private abstract static class UnmodifiableLocationWrapper<T_VALUE, T_LOC extends ObjectLocation<T_VALUE>> extends LocationWrapper<T_LOC> {
        protected UnmodifiableLocationWrapper(T_LOC location) {
            super(location);
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        public T_VALUE get() {
            return location.get();
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

        @Override
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

    public static class ShortToByteLocationConversionWrapper extends NumericToByteLocationConversionWrapper<ShortLocation, Short> {

        public ShortToByteLocationConversionWrapper(ShortLocation location) {
            super(location);
        }

        public void addChangeListener(final ByteChangeListener listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((byte)oldValue, (byte)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Byte> listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((byte) oldValue, (byte) newValue);
                }
            });
        }
    }

    public static class IntToByteLocationConversionWrapper extends NumericToByteLocationConversionWrapper<IntLocation, Integer> {

        public IntToByteLocationConversionWrapper(IntLocation location) {
            super(location);
        }

        public void addChangeListener(final ByteChangeListener listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((byte)oldValue, (byte)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Byte> listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((byte) oldValue, (byte) newValue);
                }
            });
        }
    }

    public static class LongToByteLocationConversionWrapper extends NumericToByteLocationConversionWrapper<LongLocation, Long> {

        public LongToByteLocationConversionWrapper(LongLocation location) {
            super(location);
        }

        public void addChangeListener(final ByteChangeListener listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((byte)oldValue, (byte)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Byte> listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((byte) oldValue, (byte) newValue);
                }
            });
        }
    }

    public static class FloatToByteLocationConversionWrapper extends NumericToByteLocationConversionWrapper<FloatLocation, Float> {

        public FloatToByteLocationConversionWrapper(FloatLocation location) {
            super(location);
        }

        public void addChangeListener(final ByteChangeListener listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((byte)oldValue, (byte)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Byte> listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((byte) oldValue, (byte) newValue);
                }
            });
        }
    }

    public static class DoubleToByteLocationConversionWrapper extends NumericToByteLocationConversionWrapper<DoubleLocation, Double> {

        public DoubleToByteLocationConversionWrapper(DoubleLocation location) {
            super(location);
        }

        public void addChangeListener(final ByteChangeListener listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((byte)oldValue, (byte)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Byte> listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((byte) oldValue, (byte) newValue);
                }
            });
        }
    }

    public static class ByteToShortLocationConversionWrapper extends NumericToShortLocationConversionWrapper<ByteLocation, Byte> {

        public ByteToShortLocationConversionWrapper(ByteLocation location) {
            super(location);
        }

        public void addChangeListener(final ShortChangeListener listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((short)oldValue, (short)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Short> listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((short) oldValue, (short) newValue);
                }
            });
        }
    }

    public static class IntToShortLocationConversionWrapper extends NumericToShortLocationConversionWrapper<IntLocation, Integer> {

        public IntToShortLocationConversionWrapper(IntLocation location) {
            super(location);
        }

        public void addChangeListener(final ShortChangeListener listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((short)oldValue, (short)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Short> listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((short) oldValue, (short) newValue);
                }
            });
        }
    }

    public static class LongToShortLocationConversionWrapper extends NumericToShortLocationConversionWrapper<LongLocation, Long> {

        public LongToShortLocationConversionWrapper(LongLocation location) {
            super(location);
        }

        public void addChangeListener(final ShortChangeListener listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((short)oldValue, (short)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Short> listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((short) oldValue, (short) newValue);
                }
            });
        }
    }

    public static class FloatToShortLocationConversionWrapper extends NumericToShortLocationConversionWrapper<FloatLocation, Float> {

        public FloatToShortLocationConversionWrapper(FloatLocation location) {
            super(location);
        }

        public void addChangeListener(final ShortChangeListener listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((short)oldValue, (short)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Short> listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((short) oldValue, (short) newValue);
                }
            });
        }
    }

    public static class DoubleToShortLocationConversionWrapper extends NumericToShortLocationConversionWrapper<DoubleLocation, Double> {

        public DoubleToShortLocationConversionWrapper(DoubleLocation location) {
            super(location);
        }

        public void addChangeListener(final ShortChangeListener listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((short)oldValue, (short)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Short> listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((short) oldValue, (short) newValue);
                }
            });
        }
    }

    public static class ByteToIntLocationConversionWrapper extends NumericToIntLocationConversionWrapper<ByteLocation, Byte> {

        public ByteToIntLocationConversionWrapper(ByteLocation location) {
            super(location);
        }

        public void addChangeListener(final IntChangeListener listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }
    }

    public static class ShortToIntLocationConversionWrapper extends NumericToIntLocationConversionWrapper<ShortLocation, Short> {

        public ShortToIntLocationConversionWrapper(ShortLocation location) {
            super(location);
        }

        public void addChangeListener(final IntChangeListener listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }
    }

    public static class LongToIntLocationConversionWrapper extends NumericToIntLocationConversionWrapper<LongLocation, Long> {

        public LongToIntLocationConversionWrapper(LongLocation location) {
            super(location);
        }

        public void addChangeListener(final IntChangeListener listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }
    }

    public static class FloatToIntLocationConversionWrapper extends NumericToIntLocationConversionWrapper<FloatLocation, Float> {

        public FloatToIntLocationConversionWrapper(FloatLocation location) {
            super(location);
        }

        public void addChangeListener(final IntChangeListener listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }
    }

    public static class DoubleToIntLocationConversionWrapper extends NumericToIntLocationConversionWrapper<DoubleLocation, Double> {

        public DoubleToIntLocationConversionWrapper(DoubleLocation location) {
            super(location);
        }

        public void addChangeListener(final IntChangeListener listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((int)oldValue, (int)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Integer> listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((int) oldValue, (int) newValue);
                }
            });
        }
    }

    public static class ByteToLongLocationConversionWrapper extends NumericToLongLocationConversionWrapper<ByteLocation, Byte> {

        public ByteToLongLocationConversionWrapper(ByteLocation location) {
            super(location);
        }

        public void addChangeListener(final LongChangeListener listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((long)oldValue, (long)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Long> listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((long) oldValue, (long) newValue);
                }
            });
        }
    }

    public static class ShortToLongLocationConversionWrapper extends NumericToLongLocationConversionWrapper<ShortLocation, Short> {

        public ShortToLongLocationConversionWrapper(ShortLocation location) {
            super(location);
        }

        public void addChangeListener(final LongChangeListener listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((long)oldValue, (long)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Long> listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((long) oldValue, (long) newValue);
                }
            });
        }
    }

    public static class IntToLongLocationConversionWrapper extends NumericToLongLocationConversionWrapper<IntLocation, Integer> {

        public IntToLongLocationConversionWrapper(IntLocation location) {
            super(location);
        }

        public void addChangeListener(final LongChangeListener listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((long)oldValue, (long)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Long> listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((long) oldValue, (long) newValue);
                }
            });
        }
    }

    public static class FloatToLongLocationConversionWrapper extends NumericToLongLocationConversionWrapper<FloatLocation, Float> {

        public FloatToLongLocationConversionWrapper(FloatLocation location) {
            super(location);
        }

        public void addChangeListener(final LongChangeListener listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((long)oldValue, (long)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Long> listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((long) oldValue, (long) newValue);
                }
            });
        }
    }

    public static class DoubleToLongLocationConversionWrapper extends NumericToLongLocationConversionWrapper<DoubleLocation, Double> {

        public DoubleToLongLocationConversionWrapper(DoubleLocation location) {
            super(location);
        }

        public void addChangeListener(final LongChangeListener listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((long)oldValue, (long)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Long> listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((long) oldValue, (long) newValue);
                }
            });
        }
    }

    public static class ByteToFloatLocationConversionWrapper extends NumericToFloatLocationConversionWrapper<ByteLocation, Byte> {

        public ByteToFloatLocationConversionWrapper(ByteLocation location) {
            super(location);
        }

        public void addChangeListener(final FloatChangeListener listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((float)oldValue, (float)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((float) oldValue, (float) newValue);
                }
            });
        }
    }

    public static class ShortToFloatLocationConversionWrapper extends NumericToFloatLocationConversionWrapper<ShortLocation, Short> {

        public ShortToFloatLocationConversionWrapper(ShortLocation location) {
            super(location);
        }

        public void addChangeListener(final FloatChangeListener listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((float)oldValue, (float)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((float) oldValue, (float) newValue);
                }
            });
        }
    }

    public static class IntToFloatLocationConversionWrapper extends NumericToFloatLocationConversionWrapper<IntLocation, Integer> {

        public IntToFloatLocationConversionWrapper(IntLocation location) {
            super(location);
        }

        public void addChangeListener(final FloatChangeListener listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((float)oldValue, (float)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((float) oldValue, (float) newValue);
                }
            });
        }
    }

    public static class LongToFloatLocationConversionWrapper extends NumericToFloatLocationConversionWrapper<LongLocation, Long> {

        public LongToFloatLocationConversionWrapper(LongLocation location) {
            super(location);
        }

        public void addChangeListener(final FloatChangeListener listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((float)oldValue, (float)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((float) oldValue, (float) newValue);
                }
            });
        }
    }

    public static class DoubleToFloatLocationConversionWrapper extends NumericToFloatLocationConversionWrapper<DoubleLocation, Double> {

        public DoubleToFloatLocationConversionWrapper(DoubleLocation location) {
            super(location);
        }

        public void addChangeListener(final FloatChangeListener listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((float)oldValue, (float)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            getLocation().addChangeListener(new DoubleChangeListener() {
                public void onChange(double oldValue, double newValue) {
                    listener.onChange((float) oldValue, (float) newValue);
                }
            });
        }
    }

    public static class ByteToDoubleLocationConversionWrapper extends NumericToDoubleLocationConversionWrapper<ByteLocation, Byte> {

        public ByteToDoubleLocationConversionWrapper(ByteLocation location) {
            super(location);
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((double)oldValue, (double)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            getLocation().addChangeListener(new ByteChangeListener() {
                public void onChange(byte oldValue, byte newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }
    }

    public static class ShortToDoubleLocationConversionWrapper extends NumericToDoubleLocationConversionWrapper<ShortLocation, Short> {

        public ShortToDoubleLocationConversionWrapper(ShortLocation location) {
            super(location);
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((double)oldValue, (double)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            getLocation().addChangeListener(new ShortChangeListener() {
                public void onChange(short oldValue, short newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }
    }

    public static class IntToDoubleLocationConversionWrapper extends NumericToDoubleLocationConversionWrapper<IntLocation, Integer> {

        public IntToDoubleLocationConversionWrapper(IntLocation location) {
            super(location);
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((double)oldValue, (double)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            getLocation().addChangeListener(new IntChangeListener() {
                public void onChange(int oldValue, int newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }
    }

    public static class LongToDoubleLocationConversionWrapper extends NumericToDoubleLocationConversionWrapper<LongLocation, Long> {

        public LongToDoubleLocationConversionWrapper(LongLocation location) {
            super(location);
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((double)oldValue, (double)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            getLocation().addChangeListener(new LongChangeListener() {
                public void onChange(long oldValue, long newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }
    }

    public static class FloatToDoubleLocationConversionWrapper extends NumericToDoubleLocationConversionWrapper<FloatLocation, Float> {

        public FloatToDoubleLocationConversionWrapper(FloatLocation location) {
            super(location);
        }

        public void addChangeListener(final DoubleChangeListener listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((double)oldValue, (double)newValue);
                }
            });
        }

        public void addChangeListener(final ObjectChangeListener<Double> listener) {
            getLocation().addChangeListener(new FloatChangeListener() {
                public void onChange(float oldValue, float newValue) {
                    listener.onChange((double) oldValue, (double) newValue);
                }
            });
        }
    }

    public static abstract class NumericToByteLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, ByteLocation, Byte> implements ByteLocation {

        public NumericToByteLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Byte get() {
            return getAsByte();
        }

        public byte setAsByte(byte value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public byte setAsByteFromLiteral(byte value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Byte set(Byte value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Byte setFromLiteral(Byte value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static abstract class NumericToShortLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, ShortLocation, Short> implements ShortLocation {

        public NumericToShortLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Short get() {
            return getAsShort();
        }

        public short setAsShort(short value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public short setAsShortFromLiteral(short value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Short set(Short value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Short setFromLiteral(Short value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static abstract class NumericToIntLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, IntLocation, Integer> implements IntLocation {

        public NumericToIntLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Integer get() {
            return getAsInt();
        }

        public int setAsInt(int value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int setAsIntFromLiteral(int value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Integer set(Integer value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Integer setFromLiteral(Integer value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static abstract class NumericToLongLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, LongLocation, Long> implements LongLocation {

        public NumericToLongLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Long get() {
            return getAsLong();
        }

        public long setAsLong(long value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public long setAsLongFromLiteral(long value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Long set(Long value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Long setFromLiteral(Long value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static abstract class NumericToFloatLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, FloatLocation, Float> implements FloatLocation {

        public NumericToFloatLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Float get() {
            return getAsFloat();
        }

        public float setAsFloat(float value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public float setAsFloatFromLiteral(float value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Float set(Float value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Float setFromLiteral(Float value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static abstract class NumericToDoubleLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN>
            extends NumericToNumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, DoubleLocation, Double> implements DoubleLocation {

        public NumericToDoubleLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        public Double get() {
            return getAsDouble();
        }

        public double setAsDouble(double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public double setAsDoubleFromLiteral(double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Double set(Double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Double setFromLiteral(Double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static abstract class NumericToNumericLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN, T_LOC_OUT extends NumericLocation & ObjectLocation<T_VALUE_OUT>, T_VALUE_OUT>
                 extends LocationWrapper<T_LOC_IN> {

        private NumericToNumericLocationConversionWrapper(T_LOC_IN location) {
            super(location);
        }

        /*** Sets not allowed ***/

        public void setDefault() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /*** Numeric conversion ***/

        public int getAsInt() {
            return getLocation().getAsInt();
        }

        public byte getAsByte() {
            return getLocation().getAsByte();
        }

        public short getAsShort() {
            return getLocation().getAsShort();
        }

        public long getAsLong() {
            return getLocation().getAsLong();
        }

        public float getAsFloat() {
            return getLocation().getAsFloat();
        }

        public double getAsDouble() {
            return getLocation().getAsDouble();
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
