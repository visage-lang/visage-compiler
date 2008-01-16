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
 * DoubleVar represents a simple double variable as a Location.  New DoubleVars are constructed with the make() factory
 * method.  DoubleVar values are always valid; it is an error to invalidate an DoubleVar.
 *
 * @author Brian Goetz
 */
public class DoubleVar extends AbstractLocation implements DoubleLocation, MutableLocation {
    private double value, previousValue;


    public static DoubleLocation make() {
        return make(0);
    }

    public static DoubleLocation make(double value) {
        return new DoubleVar(value);
    }

    public static DoubleLocation makeUnmodifiable(double value) {
        return Locations.unmodifiableLocation(new DoubleVar(value));
    }


    private DoubleVar(double value) {
        super(true, false);
        this.value = value;
    }


    public double getAsDouble() {
        return value;
    }

    public double getPreviousAsDouble() {
        return previousValue;
    }

    public double setAsDouble(double value) {
        if (this.value != value) {
            previousValue = this.value;
            this.value = value;
            valueChanged();
        }
        return value;
    }

    public void setDefault() {
        setAsDouble(0.0);
    }

    public Double get() {
        return value;
    }

    public Double getPrevious() {
        return previousValue;
    }

    public Double set(Double value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Number");
            setAsDouble(0);
        }
        else
            setAsDouble(value);
        return value;
    }

    public boolean isNull() {
        return false;
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException();
    }

}
