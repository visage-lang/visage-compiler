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
public abstract class LegacyDoubleExpression extends AbstractDoubleLocation
        implements DoubleLocation, BindableLocation<DoubleBindingExpression> {

    protected DoubleBindingExpression binding;
    protected boolean isLazy;

    public LegacyDoubleExpression(boolean lazy, Location... dependencies) {
        super(false);
        bind(new DoubleBindingExpression() {
            public double computeValue() {
                return LegacyDoubleExpression.this.computeValue();
            }
        }, lazy);
        addDependencies(dependencies);
    }

    public double getAsDouble() {
        if (!isValid())
            update();
        return super.getAsDouble();
    }

    /** Calculate the current value of the expression */
    protected abstract double computeValue();

    @Override
    public void update() {
        if (!isValid())
            replaceValue(computeValue());
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!isLazy())
            update();
    }

    public void bind(DoubleBindingExpression binding, boolean lazy) {
        if (isBound())
            throw new IllegalStateException("Cannot rebind variable");
        this.binding = binding;
        isLazy = lazy;
    }

    public boolean isBound() {
        return binding != null;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public boolean isMutable() {
        return false;
    }
}
