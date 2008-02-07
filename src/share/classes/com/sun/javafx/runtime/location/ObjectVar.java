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

package com.sun.javafx.runtime.location;

/**
 * ObjectVar represents an object-valued variable as a Location.  New ObjectVars are constructed with the make() factory
 * method.  ObjectVar values are always valid; it is an error to invalidate an ObjectVar.
 *
 * @author Brian Goetz
 */
public class ObjectVar<T> extends AbstractObjectLocation<T> implements ObjectLocation<T>, MutableLocation {


    public static<T> ObjectLocation<T> make(T value) {
        return new ObjectVar<T>(value);
    }

    public static <T> ObjectLocation<T> makeUnmodifiable(T value) {
        return Locations.unmodifiableLocation(new ObjectVar<T>(value));
    }


    private ObjectVar(T value) {
        super(true, value);
    }


    public T set(T value) {
        if (changed(this.$value, value))
            replaceValue(value);
        return value;
    }

    public void setDefault() {
        set(null);
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }
}
