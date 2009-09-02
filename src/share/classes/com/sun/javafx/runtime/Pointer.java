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

import javafx.animation.KeyValueTarget;


/**
 * Pointers
 *
 * @author Brian Goetz
 *
 * FIXME: yet to be implemented for compiled binds
 */
public class Pointer implements KeyValueTarget {
    private final Type type;
    private final FXObject obj;
    private final int varnum;

    public static Pointer make(FXObject obj, int varnum) {
        // FIXME: yet to be implemented for compiled binds
        return null;
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
        // FIXME: yet to be implemented for compiled binds
        return null;
    }

    public void set(Object value) {
        // FIXME: yet to be implemented for compiled binds
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

