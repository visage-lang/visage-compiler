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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.sun.javafx.runtime.sequence.AbstractSequence;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * TypeInfo
 *
 * @author Brian Goetz
 */
public class TypeInfo<T> {
    public final T defaultValue;
    public final Types type;
    public final Sequence<T> emptySequence;

    public enum Types { INT, FLOAT, OBJECT, DOUBLE, BOOLEAN, LONG, SHORT, BYTE, CHAR, OTHER }

    private static Iterator<?> emptyIterator = new Iterator() {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
    
    protected TypeInfo(T defaultValue, Types type) {
        // This is a fragile pattern; we are passing this to a superclass ctor before this is fully initialized.
        // Relying on the superclass ctor to not do anything other than copy the reference.
        this.defaultValue = defaultValue;
        this.type = type;
        this.emptySequence = new AbstractSequence<T>(this) {
            public int size() {
                return 0;
            }

            public T get(int position) {
                return TypeInfo.this.defaultValue;
            }

            @SuppressWarnings("unchecked")
            public Iterator<T> iterator() {
                return (Iterator<T>) emptyIterator;
            }
        };
    }

    public boolean isNumeric() {
        return false;
    }

    public static final TypeInfo<Object> Object = new TypeInfo<Object>(null, Types.OBJECT);
    public static final TypeInfo<Boolean> Boolean = new TypeInfo<Boolean>(false, Types.BOOLEAN);
    public static final TypeInfo<Character> Character = new TypeInfo<Character>('\0', Types.CHAR);
    public static final TypeInfo<String> String = new TypeInfo<String>("", Types.OTHER);
    public static final NumericTypeInfo<Byte> Byte = new NumericTypeInfo<Byte>((byte)0, Types.BYTE) {
        public <V extends Number> Byte asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.byteValue(otherValue);
        }
    };
    public static final NumericTypeInfo<Short> Short = new NumericTypeInfo<Short>((short)0, Types.SHORT) {
        public <V extends Number> Short asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.shortValue(otherValue);
        }
    };
    public static final NumericTypeInfo<Integer> Integer = new NumericTypeInfo<Integer>(0, Types.INT) {
        public <V extends Number> Integer asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.intValue(otherValue);
        }
    };
    public static final NumericTypeInfo<Long> Long = new NumericTypeInfo<Long>(0L, Types.LONG) {
        public <V extends Number> Long asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.longValue(otherValue);
        }
    };
    public static final NumericTypeInfo<Float> Float = new NumericTypeInfo<Float>(0.0f, Types.FLOAT) {
        public <V extends Number> Float asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.floatValue(otherValue);
        }
    };
    public static final NumericTypeInfo<Double> Double = new NumericTypeInfo<Double>(0.0, Types.DOUBLE) {
        public <V extends Number> Double asPreferred(NumericTypeInfo<V> otherType, V otherValue) {
            return otherType.doubleValue(otherValue);
        }
    };

    private static final Map<Class<?>, TypeInfo<?>> map = new HashMap<Class<?>, TypeInfo<?>>();
    static {
        // map.put(Number.class, Number);
        map.put(Byte.class, Byte);
        map.put(Short.class, Short);
        map.put(Integer.class, Integer);
        map.put(Long.class, Long);
        map.put(Float.class, Float);
        map.put(Double.class, Double);
        map.put(Boolean.class, Boolean);
        map.put(Character.class, Character);
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
        return new TypeInfo<T>(defaultValue, Types.OTHER);
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(Class clazz, T defaultValue) {
        TypeInfo<T> ti = new TypeInfo<T>(defaultValue, Types.OTHER);
        map.put(clazz, ti);
        return ti;
    }

    public static<T> TypeInfo<T> makeAndRegisterTypeInfo(T defaultValue) {
        return makeAndRegisterTypeInfo(defaultValue.getClass(), defaultValue);
    }
}


