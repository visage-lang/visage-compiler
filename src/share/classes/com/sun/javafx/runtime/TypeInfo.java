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
    public final Sequence<T> emptySequence;

    static Iterator<?> emptyIterator = new Iterator() {
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

            @SuppressWarnings("unchecked")
            public Iterator<T> iterator() {
                return (Iterator<T>) emptyIterator;
            }
        };
    }

    public static final TypeInfo<Object> Object = new TypeInfo<Object>(null);
    public static final TypeInfo<Boolean> Boolean = new TypeInfo<Boolean>(false);
    public static final TypeInfo<Character> Character = new TypeInfo<Character>('\0');
    public static final TypeInfo<Byte> Byte = new TypeInfo<Byte>((byte)0);
    public static final TypeInfo<Short> Short = new TypeInfo<Short>((short)0);
    public static final TypeInfo<Integer> Integer = new TypeInfo<Integer>(0);
    public static final TypeInfo<Long> Long = new TypeInfo<Long>(0L);
    public static final TypeInfo<Float> Float = new TypeInfo<Float>(0.0f);
    public static final TypeInfo<Double> Double = new TypeInfo<Double>(0.0);
    public static final TypeInfo<String> String = new TypeInfo<String>("");

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


