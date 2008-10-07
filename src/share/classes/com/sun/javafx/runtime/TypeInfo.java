package com.sun.javafx.runtime;

import java.util.Map;
import java.util.HashMap;

import com.sun.javafx.runtime.sequence.Sequence;
import com.sun.javafx.runtime.sequence.AbstractSequence;

/**
 * TypeInfo
 *
 * @author Brian Goetz
 */
public class TypeInfo<T> {
    public final T defaultValue;
    public final Sequence<T> emptySequence;

    private TypeInfo(T defaultValue) {
        // This is a fragile pattern; we are passing this to a superclass ctor before this is fully initialized.
        // Relying on the superclass ctor to not do anything other than copy the reference.
        this.defaultValue = defaultValue;
        this.emptySequence = new AbstractSequence<T>(this) {
            public int size() {
                return 0;
            }

            public T get(int position) {
                return TypeInfo.this.defaultValue;
            }
        };
    }

    public static final TypeInfo<Long> Long = new TypeInfo<Long>(0L);
    public static final TypeInfo<Integer> Integer = new TypeInfo<Integer>(0);
    public static final TypeInfo<Boolean> Boolean = new TypeInfo<Boolean>(false);
    public static final TypeInfo<Double> Double = new TypeInfo<Double>(0.0);
    public static final TypeInfo<Float> Float = new TypeInfo<Float>(0.0f);
    public static final TypeInfo<String> String = new TypeInfo<String>("");
    public static final TypeInfo<Object> Object = new TypeInfo<Object>(null);

    private static final Map<Class<?>, TypeInfo<?>> map = new HashMap<Class<?>, TypeInfo<?>>();
    static {
        // map.put(Number.class, Number);
        map.put(Integer.class, Integer);
        map.put(Long.class, Long);
        map.put(Boolean.class, Boolean);
        map.put(Double.class, Double);
        map.put(Float.class, Float);
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
            ti = TypeInfo.getTypeInfo();
        return ti;
    }

    public static<T> TypeInfo<T> makeTypeInfo(T defaultValue) {
        return new TypeInfo<T>(defaultValue);
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(Class clazz, T defaultValue) {
        TypeInfo<T> ti = new TypeInfo<T>(defaultValue);
        map.put(clazz, ti);
        return ti;
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(T defaultValue) {
        return makeAndRegisterTypeInfo(defaultValue.getClass(), defaultValue);
    }
}


