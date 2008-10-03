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
 * BooleanVariable
 *
 * @author Brian Goetz
 */
public class BooleanVariable
        extends AbstractVariable<Boolean, BooleanLocation, BooleanBindingExpression, BooleanChangeListener>
        implements BooleanLocation {

    public static final boolean DEFAULT = false;
    protected boolean $value = DEFAULT;

    public static BooleanVariable make() {
        return new BooleanVariable();
    }

    public static BooleanVariable make(boolean value) {
        return new BooleanVariable(value);
    }

    public static BooleanVariable make(boolean lazy, BooleanBindingExpression binding, Location... dependencies) {
        return new BooleanVariable(lazy, binding, dependencies);
    }

    public static BooleanVariable make(BooleanBindingExpression binding, Location... dependencies) {
        return new BooleanVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static BooleanVariable makeBijective(ObjectVariable<Boolean> other) {
        BooleanVariable me = BooleanVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected BooleanVariable() { }

    protected BooleanVariable(boolean value) {
        super(STATE_UNBOUND);
        $value = value;
        setValid();
    }

    protected BooleanVariable(boolean lazy, BooleanBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependency(dependencies);
    }

    protected boolean replaceValue(boolean newValue) {
        boolean oldValue = $value;
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

    public boolean getAsBoolean() {
        if (isUnidirectionallyBound() && !isValid())
            update();
        return $value;
    }

    protected BooleanBindingExpression makeBindingExpression(final BooleanLocation otherLocation) {
        return new BooleanBindingExpression() {
            public boolean computeValue() {
                return otherLocation.getAsBoolean();
            }
        };
    }

    public boolean setAsBoolean(boolean value) {
        if (isUnidirectionallyBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
        else
            return replaceValue(value);
    }

    public boolean setAsBooleanFromLiteral(final boolean value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsBoolean(value);
            }
        };
        return value;
    }

    public void setDefault() {
        if (state == STATE_INITIAL) {
            $value = DEFAULT;
            state = STATE_UNBOUND_DEFAULT;
            notifyListeners(DEFAULT, DEFAULT, true);
        }
        else
            setAsBoolean(DEFAULT);
    }

    public Boolean set(Boolean value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Boolean");
            setDefault();
        }
        else
            setAsBoolean(value);
        return value;
    }

    @Override
    public void update() {
        try {
            if (isUnidirectionallyBound() && !isValid())
                replaceValue(binding.computeValue());
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(DEFAULT);
        }
    }

    public Boolean get() {
        return getAsBoolean();
    }

    public boolean isNull() {
        return false;
    }

    public void addChangeListener(final ObjectChangeListener<Boolean> listener) {
        addChangeListener(new BooleanChangeListener() {
            public void onChange(boolean oldValue, boolean newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(boolean oldValue, boolean newValue, boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (BooleanChangeListener listener : replaceListeners)
                try {
                    listener.onChange(oldValue, newValue);
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
        }
    }

}
