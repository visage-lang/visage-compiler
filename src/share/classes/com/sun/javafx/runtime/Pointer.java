/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.runtime.sequence.Sequence;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyValueTarget;


/**
 * Pointers
 *
 * @author Brian Goetz
 * @author A. Sundararajan
 */
public class Pointer implements KeyValueTarget {
    private final Type type;
    private final FXObject obj;
    private final int varnum;

    private static Map<Class, Type> classToType;
    static {
        classToType = new HashMap<Class, Type>();
        classToType.put(Byte.TYPE, Type.BYTE);
        classToType.put(Short.TYPE, Type.SHORT);
        classToType.put(Integer.TYPE, Type.INTEGER);
        classToType.put(Long.TYPE, Type.LONG);
        classToType.put(Float.TYPE, Type.FLOAT);
        classToType.put(Double.TYPE, Type.DOUBLE);
        classToType.put(Boolean.TYPE, Type.BOOLEAN);
        classToType.put(Character.TYPE, Type.INTEGER);
    }

    public static Pointer make(FXObject obj, int varnum) {
        Class clazz = obj.getType$(varnum);
        Type type = classToType.get(clazz);
        if (type == null) {
            if (Sequence.class.isAssignableFrom(clazz)) {
                type = Type.SEQUENCE;
            } else {
                type = Type.OBJECT;
            }
        }
        return new Pointer(type, obj, varnum);
    }

    // make a Pointer for a script-level variable of given Class
    public static Pointer make(Class clazz, String varName) {
        try {
            // FIXME: dependency on generated members!
            StringBuilder buf = new StringBuilder();
            buf.append("access$scriptLevel$");
            buf.append(clazz.getName().replace('.', '$'));
            buf.append('$');
            Method statics = clazz.getMethod(buf.toString(), (Class[])null);
            FXObject obj = (FXObject) statics.invoke(null, (Object[])null);
            return make(obj, FXBase.getVarNum$(obj, varName));
        } catch (RuntimeException re) {
            throw (RuntimeException)re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    // make a Pointer for an instance variable of a given object
    public static Pointer make(FXBase obj, String varName) {
        return make((FXObject)obj, varName);
    }

    public static Pointer make(FXObject obj, String varName) {
        return make(obj, FXBase.getVarNum$(obj, varName));
    }

    public static boolean equals(Pointer p1, Pointer p2) {
        return (p1 == null) ? (p2 == null) : p1.equals(p2);
    }

    private Pointer(Type type, FXObject obj, int varnum) {
        this.type = type;
        this.obj = obj;
        this.varnum = varnum;
    }

    public Type getType() {
        return type;
    }

    public Pointer unwrap() {
        return this;
    }
    
    public Object get() {
        return obj.get$(varnum);
    }

    public void set(Object value) {
        obj.set$(varnum, value);
    }

    public Object getValue() {
        return get();
    }

    public void setValue(Object o) {
        set(o);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pointer) {
            Pointer other = (Pointer)o;
            return obj == other.obj && varnum == other.varnum;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(obj) ^ varnum;
    }
}