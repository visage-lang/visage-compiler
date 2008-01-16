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
 * IntExpression represents an integer-value bound expression.  Associated with an IntExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  IntExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public abstract class IntExpression extends AbstractLocation implements IntLocation {
    private int value, previousValue;

    public IntExpression(boolean lazy, Location... dependencies) {
        super(false, lazy);
        addDependencies(dependencies);
    }

    public int getAsInt() {
        if (!isValid())
            update();
        return value;
    }

    public int getPreviousAsInt() {
        return previousValue;
    }

    public int setAsInt(int value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer getPrevious() {
        return getPreviousAsInt();
    }

    public Integer set(Integer value) {
        return setAsInt(value);
    }

    /** Calculate the current value of the expression */
    protected abstract int computeValue();

    @Override
    public void update() {
        if (!isValid()) {
            value = computeValue();
            // @@@ Should this be .equals() ?
            setValid(previousValue != value);
        }
    }

    public boolean isNull() {
        return false;
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }

}
