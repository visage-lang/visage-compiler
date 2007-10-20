package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.SequencePredicate;

import java.util.Collection;
import java.util.Iterator;

/**
 * Factory methods for wrapping Locations: unmodifiable locations, ObjectLocation-typed views of primitive locations, etc
 *
 * @author Brian Goetz
 */
public class Locations {
    // non-instantiable
    private Locations() {
    }

    public static ObjectLocation<Integer> asObjectLocation(IntLocation loc) {
        return new IntObjectLocation(loc);
    }

    public static ObjectLocation<Integer> asMutableObjectLocation(IntLocation loc) {
        return new IntObjectMutableLocation(loc);
    }

    public static ObjectLocation<Double> asObjectLocation(DoubleLocation loc) {
        return new DoubleObjectLocation(loc);
    }

    public static ObjectLocation<Double> asMutableObjectLocation(DoubleLocation loc) {
        return new DoubleObjectMutableLocation(loc);
    }

    public static ObjectLocation<Boolean> asObjectLocation(BooleanLocation loc) {
        return new BooleanObjectLocation(loc);
    }

    public static ObjectLocation<Boolean> asMutableObjectLocation(BooleanLocation loc) {
        return new BooleanObjectMutableLocation(loc);
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

    public static<T> ObjectLocation<T> unmodifiableLocation(ObjectLocation<T> loc) {
        return new UnmodifiableObjectLocation<T>(loc);
    }

    public static<T> SequenceLocation<T> unmodifiableLocation(SequenceLocation<T> loc) {
        return new UnmodifiableSequenceLocation<T>(loc);
    }


    private static abstract class LocationWrapper implements Location {
        protected abstract Location getLocation();

        public boolean isValid() {
            return getLocation().isValid();
        }

        public boolean isLazy() {
            return getLocation().isLazy();
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

        public void valueChanged() {
            getLocation().valueChanged();
        }

        public void addDependencies(Location... location) {
            getLocation().addDependencies(location);
        }
    }

    /**
     * Wrapper class that creates an ObjectLocation<Integer> view of an IntLocation
     */
    private static class IntObjectLocation extends LocationWrapper implements ObjectLocation<Integer>, ViewLocation {
        private final IntLocation location;

        protected IntLocation getLocation() {
            return location;
        }

        public IntObjectLocation(IntLocation location) {
            this.location = location;
        }

        public Integer get() {
            return location.get();
        }

        public Integer getPreviousValue() {
            return location.getPreviousValue();
        }

        public Integer set(Integer value) {
            return location.set(value);
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    /**
     * Wrapper class that creates an ObjectLocation<Integer> view of a mutable IntLocation
     */
    private static class IntObjectMutableLocation extends IntObjectLocation implements MutableLocation {
        public IntObjectMutableLocation(IntLocation location) {
            super(location);
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

        public int get() {
            return location.get();
        }

        public int getPreviousValue() {
            return location.getPreviousValue();
        }

        public int set(int value) {
            throw new UnsupportedOperationException();
        }

        public ObjectLocation<Integer> asObjectLocation() {
            return new IntObjectLocation(this);
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }


    /**
     * Wrapper class that creates an ObjectLocation<Double> view of a DoubleLocation
     */
    private static class DoubleObjectLocation extends LocationWrapper implements ObjectLocation<Double>, ViewLocation {
        private final DoubleLocation location;

        protected Location getLocation() {
            return location;
        }

        public DoubleObjectLocation(DoubleLocation location) {
            this.location = location;
        }

        public Double get() {
            return location.get();
        }

        public Double getPreviousValue() {
            return location.getPreviousValue();
        }

        public Double set(Double value) {
            return location.set(value);
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    /**
     * Wrapper class that creates an ObjectLocation<Double> view of a mutable DoubleLocation
     */
    private static class DoubleObjectMutableLocation extends DoubleObjectLocation implements MutableLocation {
        public DoubleObjectMutableLocation(DoubleLocation location) {
            super(location);
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

        public double get() {
            return location.get();
        }

        public double getPreviousValue() {
            return location.getPreviousValue();
        }

        public double set(double value) {
            throw new UnsupportedOperationException();
        }

        public ObjectLocation<Double> asObjectLocation() {
            return new DoubleObjectLocation(this);
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
        }
    }


    /**
     * Wrapper class that creates an ObjectLocation<Boolean> view of a BooleanLocation
     */
    private static class BooleanObjectLocation extends LocationWrapper implements ObjectLocation<Boolean>, ViewLocation {
        private final BooleanLocation location;

        protected Location getLocation() {
            return location;
        }

        public BooleanObjectLocation(BooleanLocation location) {
            this.location = location;
        }

        public Boolean get() {
            return location.get();
        }

        public Boolean getPreviousValue() {
            return location.getPreviousValue();
        }

        public Boolean set(Boolean value) {
            return location.set(value);
        }

        public Location getUnderlyingLocation() {
            return location;
        }
    }

    /**
     * Wrapper class that creates an ObjectLocation<Boolean> view of a mutable BooleanLocation
     */
    private static class BooleanObjectMutableLocation extends BooleanObjectLocation implements MutableLocation {
        public BooleanObjectMutableLocation(BooleanLocation location) {
            super(location);
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

        public boolean get() {
            return location.get();
        }

        public boolean getPreviousValue() {
            return location.getPreviousValue();
        }

        public boolean set(boolean value) {
            throw new UnsupportedOperationException();
        }

        public ObjectLocation<Boolean> asObjectLocation() {
            return new BooleanObjectLocation(this);
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

        public T get() {
            return location.get();
        }

        public T getPreviousValue() {
            return location.getPreviousValue();
        }

        public T set(T value) {
            throw new UnsupportedOperationException();
        }

        public void invalidate() {
            throw new UnsupportedOperationException();
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

        public T get(int position) {
            return location.get(position);
        }

        public Sequence<T> get() {
            return location.get();
        }

        public Sequence<T> getPreviousValue() {
            return location.getPreviousValue();
        }

        public Iterator<T> iterator() {
            return location.iterator();
        }

        public Sequence<T> set(Sequence<T> value) {
            throw new UnsupportedOperationException();
        }

        public void set(int position, T value) {
            throw new UnsupportedOperationException();
        }

        public void delete(int position) {
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

        public void insert(Sequence<T> values) {
            throw new UnsupportedOperationException();
        }

        public void insertFirst(T value) {
            throw new UnsupportedOperationException();
        }

        public void insertFirst(Sequence<T> values) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(T value, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(T value, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(Sequence<T> values, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertBefore(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(T value, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(T value, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(Sequence<T> values, int position) {
            throw new UnsupportedOperationException();
        }

        public void insertAfter(Sequence<T> values, SequencePredicate<T> sequencePredicate) {
            throw new UnsupportedOperationException();
        }
    }
}