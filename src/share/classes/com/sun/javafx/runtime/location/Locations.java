package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;

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

    public static<T> SequenceLocation<T> constant(Sequence<T> value) {
        return SequenceConstant.make(value);
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

    public static <T extends Number> DoubleLocation asDoubleLocation(ObjectLocation<T> loc) {
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

    public static <T> ObjectLocation<T> unmodifiableLocation(ObjectLocation<T> loc) {
        return new UnmodifiableObjectLocation<T>(loc);
    }

    public static <T> SequenceLocation<T> unmodifiableLocation(SequenceLocation<T> loc) {
        return new UnmodifiableSequenceLocation<T>(loc);
    }

    public static<T, V extends T> ObjectLocation<T> upcast(Class<V> clazz, ObjectLocation<V> loc) {
        return new UpcastLocation<T, V>(clazz, loc);
    }


    private static abstract class LocationWrapper implements Location {
        protected abstract Location getLocation();

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

        public void addWeakListener(ChangeListener listener) {
            getLocation().addWeakListener(listener);
        }

        public Collection<ChangeListener> getListeners() {
            return getLocation().getListeners();
        }

        public void addDependencies(Location... location) {
            getLocation().addDependencies(location);
        }

        public void addDynamicDependency(Location location) {
            getLocation().addDynamicDependency(location);
        }

        public void clearDynamicDependencies() {
            getLocation().clearDynamicDependencies();
        }

        public void addDependentLocation(WeakReference<Location> location) {
            getLocation().addDependentLocation(location);
        }
    }

    /**
     * Wrapper class that creates a DoubleLocation view of an IntLocation
     */
    private static class IntDoubleLocation extends LocationWrapper implements DoubleLocation, StaticViewLocation {
        private final IntLocation location;

        protected IntLocation getLocation() {
            return location;
        }

        public IntDoubleLocation(IntLocation location) {
            this.location = location;
        }

        public double getAsDouble() {
            return location.getAsInt();
        }

        public void addChangeListener(IntChangeListener listener) {
            location.addChangeListener(listener);
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

        public int setAsInt(int value) {
            return location.setAsInt(value);
        }

        public int setAsIntFromDefault(int value) {
            return location.setAsIntFromLiteral(value);
        }
    }


    private static class ObjectIntLocation extends LocationWrapper implements IntLocation, StaticViewLocation {
        private final ObjectLocation<Integer> location;

        private ObjectIntLocation(ObjectLocation<Integer> location) {
            this.location = location;
        }

        protected Location getLocation() {
            return location;
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

    private static class ObjectDoubleLocation<T extends Number> extends LocationWrapper implements DoubleLocation, StaticViewLocation {
        private final ObjectLocation<T> location;

        private ObjectDoubleLocation(ObjectLocation<T> location) {
            this.location = location;
        }

        protected Location getLocation() {
            return location;
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

        public T set(T value) {
            return location.set(value);
        }

        public T setFromLiteral(T value) {
            return location.setFromLiteral(value);
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

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    private static class ObjectBooleanLocation extends LocationWrapper implements BooleanLocation, StaticViewLocation {
        private final ObjectLocation<Boolean> location;

        private ObjectBooleanLocation(ObjectLocation<Boolean> location) {
            this.location = location;
        }

        protected Location getLocation() {
            return location;
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
    private static class DoubleIntLocation extends LocationWrapper implements IntLocation, StaticViewLocation {
        private final DoubleLocation location;

        protected DoubleLocation getLocation() {
            return location;
        }

        public DoubleIntLocation(DoubleLocation location) {
            this.location = location;
        }

        public int getAsInt() {
            return (int)location.getAsDouble();
        }

        public void addChangeListener(DoubleChangeListener listener) {
            location.addChangeListener(listener);
        }

        public Integer get() {
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

        public double setAsDouble(double value) {
            return location.setAsDouble(value);
        }

        public double setAsDoubleFromDefault(double value) {
            return location.setAsDoubleFromLiteral(value);
        }
    }

    /**
     * Wrapper class that wraps an IntLocation so it cannot be modified
     */
    private static class UnmodifiableIntLocation extends LocationWrapper implements IntLocation {
        private final IntLocation location;

        protected Location getLocation() {
            return location;
        }

        public UnmodifiableIntLocation(IntLocation location) {
            this.location = location;
        }

        public boolean isMutable() {
            return false;
        }

        public int getAsInt() {
            return location.getAsInt();
        }

        public Integer get() {
            return getAsInt();
        }

        public void addChangeListener(IntChangeListener listener) {
            location.addChangeListener(listener);
        }

        public void addChangeListener(ObjectChangeListener<Integer> listener) {
            location.addChangeListener(listener);
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

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }


    /**
     * Wrapper class that wraps a DoubleLocation so it cannot be modified
     */
    private static class UnmodifiableDoubleLocation extends LocationWrapper implements DoubleLocation {
        private final DoubleLocation location;

        protected Location getLocation() {
            return location;
        }

        public UnmodifiableDoubleLocation(DoubleLocation location) {
            this.location = location;
        }

        public boolean isMutable() {
            return false;
        }

        public double getAsDouble() {
            return location.getAsDouble();
        }

        public Double get() {
            return getAsDouble();
        }

        public void addChangeListener(DoubleChangeListener listener) {
            location.addChangeListener(listener);
        }

        public void addChangeListener(ObjectChangeListener<Double> listener) {
            location.addChangeListener(listener);
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

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }


    /**
     * Wrapper class that wraps a BooleanLocation so it cannot be modified
     */
    private static class UnmodifiableBooleanLocation extends LocationWrapper implements BooleanLocation {
        private final BooleanLocation location;

        protected Location getLocation() {
            return location;
        }

        public UnmodifiableBooleanLocation(BooleanLocation location) {
            this.location = location;
        }

        public boolean isMutable() {
            return false;
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

        public boolean setAsBoolean(boolean value) {
            throw new UnsupportedOperationException();
        }

        public boolean setAsBooleanFromLiteral(boolean value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public Boolean set(Boolean value) {
            throw new UnsupportedOperationException();
        }

        public Boolean setFromLiteral(Boolean value) {
            throw new UnsupportedOperationException();
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Wrapper class that wraps an ObjectLocation so it cannot be modified
     */
    private static class UnmodifiableObjectLocation<T> extends LocationWrapper implements ObjectLocation<T> {
        private final ObjectLocation<T> location;

        public UnmodifiableObjectLocation(ObjectLocation<T> location) {
            this.location = location;
        }

        public ObjectLocation<T> getLocation() {
            return location;
        }

        public boolean isMutable() {
            return false;
        }

        public T get() {
            return location.get();
        }

        public T set(T value) {
            throw new UnsupportedOperationException();
        }

        public T setFromLiteral(T value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public void addChangeListener(ObjectChangeListener<T> listener) {
            location.addChangeListener(listener);
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }

    private static class UpcastLocation<T, V extends T> extends LocationWrapper implements ObjectLocation<T> {
        private final ObjectLocation<V> location;
        private final Class<V> clazz;

        public UpcastLocation(Class<V> clazz, ObjectLocation<V> location) {
            this.location = location;
            this.clazz = clazz;
        }

        protected Location getLocation() {
            return location;
        }

        public V get() {
            return location.get();
        }

        public V set(T value) {
            return location.set(clazz.cast(value));
        }

        public T setFromLiteral(T value) {
            return location.setFromLiteral(clazz.cast(value));
        }

        public void setDefault() {
            location.setDefault();
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
    private static class UnmodifiableSequenceLocation<T> extends LocationWrapper implements SequenceLocation<T> {
        private final SequenceLocation<T> location;

        public UnmodifiableSequenceLocation(SequenceLocation<T> location) {
            this.location = location;
        }

        public SequenceLocation<T> getLocation() {
            return location;
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }

        public boolean isMutable() {
            return false;
        }

        public T get(int position) {
            return location.get(position);
        }

        public Sequence<T> getAsSequence() {
            return location.getAsSequence();
        }

        public Sequence<T> get() {
            return location.get();
        }

        public Sequence<T> set(Sequence<T> value) {
            throw new UnsupportedOperationException();
        }

        public Sequence<T> setFromLiteral(Sequence<T> value) {
            throw new UnsupportedOperationException();
        }

        public void setDefault() {
            throw new UnsupportedOperationException();
        }

        public Iterator<T> iterator() {
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
