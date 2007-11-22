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
 * DoubleExpression represents a double-value bound expression.  Associated with an DoubleExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  DoubleExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class DoubleExpression extends AbstractLocation implements DoubleLocation {

    private final DoubleBindingExpression expression;
    private double value, previousValue;

    /** Create an DoubleExpression with the specified expression and dependencies. */
    public static DoubleLocation make(DoubleBindingExpression exp, Location... dependencies) {
        DoubleExpression loc = new DoubleExpression(false, exp);
        exp.location = loc;
        loc.addDependencies(dependencies);
        return loc;
    }

    /** Create a lazy DoubleExpression with the specified expression and dependencies. */
    public static DoubleLocation makeLazy(DoubleBindingExpression exp, Location... dependencies) {
        DoubleExpression loc = new DoubleExpression(true, exp);
        exp.location = loc;
        loc.addDependencies(dependencies);
        return loc;
    }

    
    private DoubleExpression(boolean lazy, DoubleBindingExpression expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public double get() {
        if (!isValid())
            update();
        return value;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public double set(double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = expression.get();
            setValid(previousValue != value);
        }
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }

    public ObjectLocation<Double> asObjectLocation() {
        return Locations.asObjectLocation(this);
    }
}
