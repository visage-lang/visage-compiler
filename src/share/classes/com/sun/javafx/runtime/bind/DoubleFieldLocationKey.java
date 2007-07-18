/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.javafx.runtime.bind;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * DoubleFieldLocationKey
 *
 * @author Brian Goetz
 */
public class DoubleFieldLocationKey implements FieldLocationKey {
    private final static Unsafe unsafe = UnsafeUtil.getUnsafe();
    private final Field field;                           // @@@ OPT: Needed only for assertions
    protected final long offset;
    protected final int sequence;

    public DoubleFieldLocationKey(Field field, int sequence) {
        offset = unsafe.objectFieldOffset(field);
        this.field = field;
        this.sequence = sequence;
    }

    public int getInt(Object base) {
        throw new UnsupportedOperationException();
    }

    public void setInt(Object base, int value) {
        throw new UnsupportedOperationException();
    }

    public double getDouble(Object base) {
        assert field.getType() == Double.TYPE;
        assert field.getDeclaringClass().isAssignableFrom(base.getClass());
        return unsafe.getDouble(base, offset);
    }

    public void setDouble(Object base, double value) {
        assert field.getType() == Double.TYPE;
        assert field.getDeclaringClass().isAssignableFrom(base.getClass());
        unsafe.putDouble(base, offset, value);
    }

    public void setReference(Object base, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object getReference(Object base) {
        throw new UnsupportedOperationException();
    }

    public void update(Object base, BindingClosure closure) {
        setDouble(base, closure.asDouble());
    }


    public int getSequence() {
        return sequence;
    }

}
