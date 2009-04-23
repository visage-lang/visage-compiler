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

    public static <T extends Number> NumericLocation asNumericLocation(ObjectLocation<T> loc) {
        return new ObjectNumericLocation<T>(loc);
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

        public void addDependency(DependencySource... deps) {
            location.addDependency(deps);
        }

        public void addDependency(DependencySource dep) {
            location.addDependency(dep);
        }

        public void addDynamicDependency(DependencySource dep) {
            location.addDynamicDependency(dep);
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

        public void addChangeListener(final ChangeListener<T> listener) {
            location.addChangeListener(new ChangeListener<V>() {
                public void onChange(V oldValue, V newValue) {
                    listener.onChange(oldValue, newValue);
                }
            });
        }
    }
}
