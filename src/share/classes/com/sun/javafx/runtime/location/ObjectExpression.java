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
 * ObjectExpression represents an object-value bound expression.  Associated with an ObjectExpression is an expression
 * that is used to recalculate the value, and a list of dependencies (locations).  If any of the dependencies are
 * changed, the expression is recomputed.  ObjectExpressions are created with the make() and makeLazy() factories; the
 * locations are created in an initially invalid state, so that their evaluation can be deferred until an appropriate
 * time.
 *
 * @author Brian Goetz
 */
public class ObjectExpression<T> extends AbstractLocation implements ObjectLocation<T> {

    private final ObjectBindingExpression<T> expression;
    private T value, previousValue;

    /** Create an ObjectExpression with the specified expression and dependencies. */
    public static<T> ObjectLocation<T> make(ObjectBindingExpression<T> exp, Location... dependencies) {
        ObjectExpression<T> loc = new ObjectExpression<T>(false, exp);
        exp.location = loc;
        loc.addDependencies(dependencies);
        return loc;
    }

    /** Create a lazy ObjectExpression with the specified expression and dependencies. */
    public static<T> ObjectLocation<T> makeLazy(ObjectBindingExpression<T> exp, Location... dependencies) {
        ObjectExpression<T> loc = new ObjectExpression<T>(true, exp);
        exp.location = loc;
        loc.addDependencies(dependencies);
        return loc;
    }

    public ObjectExpression(boolean lazy, Location... dependencies) {
        super(false, lazy);
        addDependencies(dependencies);
        expression = null;
    }

    private ObjectExpression(boolean lazy, ObjectBindingExpression<T> expression) {
        super(false, lazy);
        this.expression = expression;
    }

    public T get() {
        if (!isValid())
            update();
        return value;
    }

    public T getPreviousValue() {
        return previousValue;
    }

    public T set(T value) {
        throw new UnsupportedOperationException();
    }

    /** Calculate the current value of the expression */
    protected T computeValue() {
        return expression.get();
    }

    @Override
    public void update() {
        if (!isValid()) {
            value = computeValue();
            setValid(!equals(value, previousValue));
            previousValue = null;
        }
    }

    @Override
    public void invalidate() {
        if (isValid())
            previousValue = value;
        super.invalidate();
    }
}
