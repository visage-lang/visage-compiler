package com.sun.javafx.runtime;

import java.util.Map;
import java.util.HashMap;

import com.sun.javafx.runtime.sequence.AbstractSequence;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * Types
 *
 * @author Brian Goetz
 */
public class TypeInfos {
    // public static final TypeInfo<Number> Number = new AbstractTypeInfo<Number>(0);
    public static final TypeInfo<Long> Long = new AbstractTypeInfo<Long>(0L);
    public static final TypeInfo<Integer> Integer = new AbstractTypeInfo<Integer>(0);
    public static final TypeInfo<Boolean> Boolean = new AbstractTypeInfo<Boolean>(false);
    public static final TypeInfo<Double> Double = new AbstractTypeInfo<Double>(0.0);
    public static final TypeInfo<String> String = new AbstractTypeInfo<String>("");
    public static final TypeInfo<Object> Object = new AbstractTypeInfo<Object>(null);

    private static final Map<Class<?>, TypeInfo<?>> map = new HashMap<Class<?>, TypeInfo<?>>();
    static {
        // map.put(Number.class, Number);
        map.put(Integer.class, Integer);
        map.put(Long.class, Long);
        map.put(Boolean.class, Boolean);
        map.put(Double.class, Double);
        map.put(String.class, String);
    }

    @SuppressWarnings("unchecked")
    public static<T> TypeInfo<T> getTypeInfo() {
        return (TypeInfo<T>) Object;
    }

    @SuppressWarnings("unchecked")
    public static<T> TypeInfo<T> getTypeInfo(Class<T> clazz) {
        TypeInfo<T> ti = (TypeInfo<T>) map.get(clazz);
        if (ti == null)
            ti = TypeInfos.getTypeInfo();
        return ti;
    }

    public static<T> Sequence<T> getEmptySequence() {
        return TypeInfos.<T>getTypeInfo().getEmptySequence();
    }

    public static<T> TypeInfo<T> makeTypeInfo(T defaultValue) {
        return new AbstractTypeInfo<T>(defaultValue);
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(Class clazz, T defaultValue) {
        AbstractTypeInfo<T> ti = new AbstractTypeInfo<T>(defaultValue);
        map.put(clazz, ti);
        return ti;
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(T defaultValue) {
        return makeAndRegisterTypeInfo(defaultValue.getClass(), defaultValue);
    }

    private static class AbstractTypeInfo<T> implements TypeInfo<T> {
        private final T defaultValue;
        private final Sequence<T> emptySequence;

        AbstractTypeInfo(T defaultValue) {
            // This is a fragile pattern; we are passing this to a superclass ctor before this is fully initialized.
            // Relying on the superclass ctor to not do anything other than copy the reference.
            this.defaultValue = defaultValue;
            this.emptySequence = new AbstractSequence<T>(this) {
                public int size() {
                    return 0;
                }

                public T get(int position) {
                    return AbstractTypeInfo.this.defaultValue;
                }
            };
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public Sequence<T> getEmptySequence() {
            return emptySequence;
        }
    }
}

