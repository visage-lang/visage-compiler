/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime.location;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * DoubleVariable
 *
 * @author Brian Goetz
 */
public class DoubleVariable
        extends AbstractVariable<Double, DoubleLocation, DoubleBindingExpression, DoubleChangeListener>
        implements DoubleLocation {

    public static final double DEFAULT = 0.0;

    protected double $value = DEFAULT;


    public static DoubleVariable make() {
        return new DoubleVariable();
    }

    public static DoubleVariable make(double value) {
        return new DoubleVariable(value);
    }

    public static DoubleVariable make(boolean lazy, DoubleBindingExpression binding, Location... dependencies) {
        return new DoubleVariable(lazy, binding, dependencies);
    }

    public static DoubleVariable make(DoubleBindingExpression binding, Location... dependencies) {
        return new DoubleVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static DoubleVariable makeBijective(ObjectVariable<Double> other) {
        DoubleVariable me = DoubleVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected DoubleVariable() { }

    protected DoubleVariable(double value) {
        super(STATE_UNBOUND);
        $value = value;
        setValid();
    }

    protected DoubleVariable(boolean lazy, DoubleBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependency(dependencies);
    }


    public double getAsDouble() {
        if (isUnidirectionallyBound() && !isValid())
            update();
        return $value;
    }

    public Double get() {
        return getAsDouble();
    }

    public boolean isNull() {
        return false;
    }

    protected double replaceValue(double newValue) {
        double oldValue = $value;
        if (preReplace(oldValue != newValue)) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return newValue;
    }

    protected DoubleBindingExpression makeBindingExpression(final DoubleLocation otherLocation) {
        return new DoubleBindingExpression() {
            public double computeValue() {
                return otherLocation.getAsDouble();
            }
        };
    }

    public double setAsDouble(double value) {
        if (isUnidirectionallyBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
        else
            return replaceValue(value);
    }

    public double setAsDoubleFromLiteral(final double value) {
        setDeferredLiteral(new DeferredInitializer() {
            public void apply() {
                setAsDouble(value);
            }
        });
        return value;
    }

    public void setDefault() {
        if (state == STATE_INITIAL) {
            $value = DEFAULT;
            state = STATE_UNBOUND_DEFAULT;
            notifyListeners(DEFAULT, DEFAULT, true);
        }
        else
            setAsDouble(DEFAULT);
    }

    public Double set(Double value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Double");
            setDefault();
        }
        else
            setAsDouble(value);
        return value;
    }

    @Override
    public void update() {
        try {
            if (isUnidirectionallyBound() && !isValid())
                replaceValue(getBindingExpression().computeValue());
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(DEFAULT);
        }
    }

    public void addChangeListener(final ObjectChangeListener<Double> listener) {
        addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(final double oldValue, final double newValue, boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (hasDependencies(DEPENDENCY_KIND_TRIGGER))
            iterateChangeListeners(new DependencyIterator<DoubleChangeListener>(DEPENDENCY_KIND_TRIGGER) {
                public void onAction(DoubleChangeListener listener) {
                    try {
                        listener.onChange(oldValue, newValue);
                    }
                    catch (RuntimeException e) {
                        ErrorHandler.triggerException(e);
                    }
                }
            });
    }

}
