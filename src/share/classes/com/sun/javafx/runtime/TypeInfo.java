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

import com.sun.javafx.runtime.location.*;
import com.sun.javafx.runtime.sequence.AbstractSequence;
import com.sun.javafx.runtime.sequence.Sequence;

/**
 * TypeInfo
 *
 * @author Brian Goetz
 */
public class TypeInfo<T, L extends ObjectLocation<T>> {
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

    // This ugly and not typesafe construct eliminates lots of small classes, which add a lot to our static footprint.
    // Such optimizations are ugly but needed for smaller-memory platforms.
    @SuppressWarnings("unchecked")
    public L makeLocation() {
        switch (type) {
            case CHAR: return (L) CharVariable.make();
            case BOOLEAN: return (L) BooleanVariable.make();
            case OBJECT:
            case OTHER:
                return (L) ObjectVariable.<T>make(defaultValue);
            default:
                throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    public L makeDefaultConstant() {
        switch (type) {
            case CHAR: return (L) CharConstant.make(CharVariable.DEFAULT);
            case BOOLEAN: return (L) BooleanConstant.make(BooleanVariable.DEFAULT);
            case OBJECT:
            case OTHER:
                return (L) ObjectConstant.<T>make(defaultValue);
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isNumeric() {
        return false;
    }

    public static final TypeInfo<Object, ObjectLocation<Object>> Object = new TypeInfo<Object, ObjectLocation<Object>>(null, Types.OBJECT);
    public static final TypeInfo<Boolean, BooleanLocation> Boolean = new TypeInfo<Boolean, BooleanLocation>(false, Types.BOOLEAN);
    public static final TypeInfo<Character, CharLocation> Character = new TypeInfo<Character, CharLocation>('\0', Types.CHAR);
    public static final TypeInfo<String, ObjectLocation<String>> String = new TypeInfo<String, ObjectLocation<String>>("", Types.OTHER);
    public static final NumericTypeInfo<Byte, ByteLocation> Byte = new NumericTypeInfo<Byte, ByteLocation>((byte)0, Types.BYTE);
    public static final NumericTypeInfo<Short, ShortLocation> Short = new NumericTypeInfo<Short, ShortLocation>((short)0, Types.SHORT);
    public static final NumericTypeInfo<Integer, IntLocation> Integer = new NumericTypeInfo<Integer, IntLocation>(0, Types.INT);
    public static final NumericTypeInfo<Long, LongLocation> Long = new NumericTypeInfo<Long, LongLocation>(0L, Types.LONG);
    public static final NumericTypeInfo<Float, FloatLocation> Float = new NumericTypeInfo<Float, FloatLocation>(0.0f, Types.FLOAT);
    public static final NumericTypeInfo<Double, DoubleLocation> Double = new NumericTypeInfo<Double, DoubleLocation>(0.0, Types.DOUBLE);

    private static final Map<Class<?>, TypeInfo<?, ?>> map = new HashMap<Class<?>, TypeInfo<?, ?>>();
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
    public static<T> TypeInfo<T, ObjectLocation<T>> getTypeInfo() {
        return (TypeInfo<T, ObjectLocation<T>>) (TypeInfo<T, ?>) (TypeInfo<?, ?>) Object;
    }

    @SuppressWarnings("unchecked")
    public static<T> TypeInfo<T, ?> getTypeInfo(Class<T> clazz) {
        TypeInfo<T, ?> ti = (TypeInfo<T, ?>) map.get(clazz);
        if (ti == null)
            ti = TypeInfo.getTypeInfo();
        return ti;
    }

    public static<T> TypeInfo<T, ObjectLocation<T>> makeTypeInfo(T defaultValue) {
        return new TypeInfo<T, ObjectLocation<T>>(defaultValue, Types.OTHER);
    }

    public static<T> TypeInfo<T, ObjectLocation<T>> makeAndRegisterTypeInfo(Class clazz, T defaultValue) {
        TypeInfo<T, ObjectLocation<T>> ti = new TypeInfo<T, ObjectLocation<T>>(defaultValue, Types.OTHER);
        map.put(clazz, ti);
        return ti;
    }

    public static<T> TypeInfo<T, ?> makeAndRegisterTypeInfo(T defaultValue) {
        return makeAndRegisterTypeInfo(defaultValue.getClass(), defaultValue);
    }
}


