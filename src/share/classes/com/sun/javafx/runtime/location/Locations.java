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

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.NumericTypeInfo;
import com.sun.javafx.runtime.TypeInfo;
import com.sun.javafx.runtime.sequence.Sequence;

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
        while (loc.isViewLocation())
            loc = loc.getUnderlyingLocation();
        return loc;
    }

    public static boolean hasDependencies(Location loc) {
        // May also want these lines, eventually:
        // while (loc.isViewLocation())
        //     loc = loc.getUnderlyingLocation();
        return ((AbstractVariable) loc).hasDependencies();
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

    public static<T> SequenceLocation<T> constant(TypeInfo<T, ?> typeInfo, Sequence<T> value) {
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
        return new ObjectNumericLocation<T>(loc);
    }

    public static <T extends Number> FloatLocation asFloatLocation(ObjectLocation<T> loc) {
        return new ObjectFloatLocation<T>(loc);
    }

    public static IntLocation asIntLocation(ObjectLocation<Integer> loc) {
        return new ObjectIntLocation(loc);
    }

    /**
     * Return a wrapping location that mirrors the underlying Location but upcasts its type
     * @param clazz  Really Class<V> but because of generics limitations this cannot be declared
     * @param loc Location to wrap
     * @return
     */
    public static<T, V extends T> ObjectLocation<T> upcast(TypeInfo<V, ?> typeInfo, ObjectLocation<V> loc) {
        return new UpcastLocation<T, V>(typeInfo, loc);
    }

    private static abstract class LocationWrapper<T extends Location> implements Location {
        protected final T location;

        protected LocationWrapper(T location) {
            this.location = location;
        }

        public boolean isValid() {
            return location.isValid();
        }

        public boolean isNull() {
            return location.isNull();
        }

        public boolean isMutable() {
            return location.isMutable();
        }

        public void invalidate() {
            location.invalidate();
        }

        public void update() {
            location.update();
        }

        public void addInvalidationListener(InvalidationListener listener) {
            location.addInvalidationListener(listener);
        }

        public void removeInvalidationListener(InvalidationListener listener) {
            location.removeInvalidationListener(listener);
        }

        public void iterateChildren(DependencyIterator<? extends LocationDependency> closure) {
            location.iterateChildren(closure);
        }

        public void addDependency(Location... locations) {
            location.addDependency(locations);
        }

        public void addDependency(Location location) {
            location.addDependency(location);
        }

        public void addDynamicDependency(Location location) {
            location.addDynamicDependency(location);
        }

        public void clearDynamicDependencies() {
            location.clearDynamicDependencies();
        }

        public void addDependentLocation(WeakLocation weakLocation) {
            location.addDependentLocation(weakLocation);
        }

        public boolean isViewLocation() {
            return false;
        }

        public Location getUnderlyingLocation() {
            return this;
        }
    }

    private static class ObjectNumericLocation<T extends Number> extends LocationWrapper<ObjectLocation<T>> implements NumericLocation {
        private ObjectNumericLocation(ObjectLocation<T> location) {
            super(location);
        }

        public int getAsInt() {
            Number val = location.get();
            return (val == null) ? 0 : val.intValue();
        }

        public byte getAsByte() {
            Number val = location.get();
            return (val == null) ? 0 : val.byteValue();
        }

        public short getAsShort() {
            Number val = location.get();
            return (val == null) ? 0 : val.shortValue();
        }

        public long getAsLong() {
            Number val = location.get();
            return (val == null) ? 0 : val.longValue();
        }

        public float getAsFloat() {
            Number val = location.get();
            return (val == null) ? 0 : val.floatValue();
        }

        public double getAsDouble() {
            Number val = location.get();
            return (val == null) ? 0 : val.doubleValue();
        }

        @Override
        public boolean isViewLocation() {
            return true;
        }

        @Override
        public Location getUnderlyingLocation() {
            return location;
        }
    }

    // @@@ May no longer be needed
    private static class ObjectIntLocation extends ObjectNumericLocation<Integer> implements IntLocation {
        private ObjectIntLocation(ObjectLocation<Integer> location) {
            super(location);
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

        public void addChangeListener(final PrimitiveChangeListener<Integer> listener) {
            location.addChangeListener(listener.asObjectListener(TypeInfo.Integer));
        }

        public Integer get() {
            return location.get();
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
    }

    // @@@ May no longer be needed  -- errr, well, it is used
    private static class ObjectFloatLocation<T extends Number> extends ObjectNumericLocation<T> implements FloatLocation {
        private ObjectFloatLocation(ObjectLocation<T> location) {
            super(location);
        }

        public float setAsFloat(float value) {
            // return location.set(value);
            throw new UnsupportedOperationException();
        }

        public float setAsFloatFromLiteral(float value) {
            // return location.setFromLiteral(value);
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            location.setDefault();
        }

        public void addChangeListener(final PrimitiveChangeListener<Float> listener) {
            location.addChangeListener(new ObjectChangeListener<T>() {
                public void onChange(T oldValue, T newValue) {
                    listener.onChange(oldValue.floatValue(), newValue.floatValue());
                }
            });
        }

        public Float get() {
            return getAsFloat();
        }

        public Float set(Float value) {
            // return location.set(value);
            throw new UnsupportedOperationException();
        }

        public Float setFromLiteral(Float value) {
            // return location.setFromLiteral(value);
            throw new UnsupportedOperationException();
        }

        public void addChangeListener(final ObjectChangeListener<Float> listener) {
            location.addChangeListener(new ObjectChangeListener<T>() {
                public void onChange(T oldValue, T newValue) {
                    listener.onChange(oldValue.floatValue(), newValue.floatValue());
                }
            });
        }
    }

    private static class ObjectBooleanLocation extends LocationWrapper<ObjectLocation<Boolean>> implements BooleanLocation {
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

        public void addChangeListener(final PrimitiveChangeListener<Boolean> listener) {
            location.addChangeListener(listener.asObjectListener(TypeInfo.Boolean));
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

        @Override
        public boolean isViewLocation() {
            return true;
        }

        @Override
        public Location getUnderlyingLocation() {
            return location;
        }
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> ByteLocation
            toByteLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToByteLocationConversionWrapper(location, inType);
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> ShortLocation
            toShortLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToShortLocationConversionWrapper(location, inType);
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> IntLocation
            toIntLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToIntLocationConversionWrapper(location, inType);
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> LongLocation
            toLongLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToLongLocationConversionWrapper(location, inType);
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> FloatLocation
            toFloatLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToFloatLocationConversionWrapper(location, inType);
    }

    public static <T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number> DoubleLocation
            toDoubleLocation(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
        return new NumericToDoubleLocationConversionWrapper(location, inType);
    }

    static class NumericToByteLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, ByteLocation, Byte, PrimitiveChangeListener<Byte>> implements ByteLocation {

        public NumericToByteLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Byte);
        }

        public Byte get() {
            return getAsByte();
        }

        public byte setAsByte(byte value) {
            return set(value);
        }

        public byte setAsByteFromLiteral(byte value) {
            return setFromLiteral(value);
        }
    }

    static class NumericToShortLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, ShortLocation, Short, PrimitiveChangeListener<Short>> implements ShortLocation {

        public NumericToShortLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Short);
        }

        public Short get() {
            return getAsShort();
        }

        public short setAsShort(short value) {
            return set(value);
        }

        public short setAsShortFromLiteral(short value) {
            return setFromLiteral(value);
        }
    }

    static class NumericToIntLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, IntLocation, Integer, PrimitiveChangeListener<Integer>> implements IntLocation {

        public NumericToIntLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Integer);
        }

        public Integer get() {
            return getAsInt();
        }

        public int setAsInt(int value) {
            return set(value);
        }

        public int setAsIntFromLiteral(int value) {
            return setFromLiteral(value);
        }
    }

    static class NumericToLongLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, LongLocation, Long, PrimitiveChangeListener<Long>> implements LongLocation {

        public NumericToLongLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Long);
        }

        public Long get() {
            return getAsLong();
        }

        public long setAsLong(long value) {
            return set(value);
        }

        public long setAsLongFromLiteral(long value) {
            return setFromLiteral(value);
        }
    }

    static class NumericToFloatLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, FloatLocation, Float, PrimitiveChangeListener<Float>> implements FloatLocation {

        public NumericToFloatLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Float);
        }

        public Float get() {
            return getAsFloat();
        }

        public float setAsFloat(float value) {
            return set(value);
        }

        public float setAsFloatFromLiteral(float value) {
            return setFromLiteral(value);
        }
    }

    static class NumericToDoubleLocationConversionWrapper<T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>, T_VALUE_IN extends Number>
            extends NumericLocationConversionWrapper<T_LOC_IN, T_VALUE_IN, DoubleLocation, Double, PrimitiveChangeListener<Double>> implements DoubleLocation {

        public NumericToDoubleLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType) {
            super(location, inType, TypeInfo.Double);
        }

        public Double get() {
            return getAsDouble();
        }

        public double setAsDouble(double value) {
            return set(value);
        }

        public double setAsDoubleFromLiteral(double value) {
            return setFromLiteral(value);
        }
    }

    private static abstract class NumericLocationConversionWrapper<
            T_LOC_IN extends NumericLocation & ObjectLocation<T_VALUE_IN>,
            T_VALUE_IN extends Number,
            T_LOC_OUT extends NumericLocation & ObjectLocation<T_VALUE_OUT>,
            T_VALUE_OUT extends Number,
            T_LISTENER_OUT extends PrimitiveChangeListener<T_VALUE_OUT>>
                 extends LocationWrapper<T_LOC_IN> {

        protected final NumericTypeInfo<T_VALUE_IN, ?> inType;
        protected final NumericTypeInfo<T_VALUE_OUT, ?> outType;

        protected NumericLocationConversionWrapper(T_LOC_IN location, NumericTypeInfo<T_VALUE_IN, ?> inType, NumericTypeInfo<T_VALUE_OUT, ?> outType) {
            super(location);
            this.inType = inType;
            this.outType = outType;
        }

        public void setDefault() {
            location.setDefault();
        }

        public T_VALUE_OUT set(T_VALUE_OUT value) {
            return outType.asPreferred(inType, location.set(inType.asPreferred(outType, value)));
        }

        public T_VALUE_OUT setFromLiteral(T_VALUE_OUT value) {
            return outType.asPreferred(inType, location.setFromLiteral(inType.asPreferred(outType, value)));
        }

        /*** Numeric conversion ***/

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

        public void addChangeListener(final ObjectChangeListener<T_VALUE_OUT> listener) {
            location.addChangeListener(new ObjectChangeListener<T_VALUE_IN>() {
                public void onChange(T_VALUE_IN oldValue, T_VALUE_IN newValue) {
                    listener.onChange(outType.asPreferred(inType, oldValue), outType.asPreferred(inType, newValue));
                }
            });
        }

        public void addChangeListener(final T_LISTENER_OUT listener) {
            addChangeListener(listener.asObjectListener(outType));
        }
    }

    private static class UpcastLocation<T, V extends T> extends LocationWrapper<ObjectLocation<V>> implements ObjectLocation<T> {

        public UpcastLocation(TypeInfo<V, ?> typeInfo, ObjectLocation<V> location) {
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
}
