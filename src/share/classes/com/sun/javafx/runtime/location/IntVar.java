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

import com.sun.javafx.runtime.ErrorHandler;

/**
 * IntVar represents a simple Integer variable as a Location.  New IntVars are constructed with the make() factory
 * method.  IntVar values are always valid; it is an error to invalidate an IntVar.
 *
 * @author Brian Goetz
 */
public class IntVar extends AbstractLocation implements IntLocation, MutableLocation {
    private int value, previousValue;


    public static IntLocation make() {
        return make(0);
    }

    public static IntLocation make(int value) {
        return new IntVar(value);
    }

    public static IntLocation makeUnmodifiable(int value) {
        return Locations.unmodifiableLocation((IntLocation) new IntVar(value));
    }


    private IntVar(int value) {
        super(true, false);
        this.value = value;
    }


    public int getAsInt() {
        return value;
    }

    public int getPreviousAsInt() {
        return previousValue;
    }

    public int setAsInt(int value) {
        if (this.value != value) {
            previousValue = this.value;
            this.value = value;
            valueChanged();
        }
        return value;
    }

    public Integer get() {
        return value;
    }

    public Integer getPrevious() {
        return previousValue;
    }

    public Integer set(Integer value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Integer");
            setAsInt(0);
        }
        else
            setAsInt(value);
        return value;
    }

    public boolean isNull() {
        return false;
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public DoubleLocation asDoubleLocation() {
        return Locations.asDoubleLocation(this);
    }
}
