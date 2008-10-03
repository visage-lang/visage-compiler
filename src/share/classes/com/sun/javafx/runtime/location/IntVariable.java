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
 * IntVariable
 *
 * @author Brian Goetz
 */
public class IntVariable extends AbstractVariable<Integer, IntLocation, IntBindingExpression, IntChangeListener> implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;

    public static IntVariable make() {
        return new IntVariable();
    }

    public static IntVariable make(int value) {
        return new IntVariable(value);
    }

    public static IntVariable make(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        return new IntVariable(lazy, binding, dependencies);
    }

    public static IntVariable make(IntBindingExpression binding, Location... dependencies) {
        return new IntVariable(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static IntVariable makeBijective(ObjectVariable<Integer> other) {
        IntVariable me = IntVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected IntVariable() { }

    protected IntVariable(int value) {
        super(STATE_UNBOUND);
        $value = value;
        setValid();
    }

    protected IntVariable(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependency(dependencies);
    }

    public int getAsInt() {
        if (isUnidirectionallyBound() && !isValid())
            update();
        return $value;
    }

    public Integer get() {
        return getAsInt();
    }

    public boolean isNull() {
        return false;
    }

    protected int replaceValue(int newValue) {
        int oldValue = $value;
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

    protected IntBindingExpression makeBindingExpression(final IntLocation otherLocation) {
        return new IntBindingExpression() {
            public int computeValue() {
                return otherLocation.getAsInt();
            }
        };
    }

    public int setAsInt(int value) {
        if (isUnidirectionallyBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
        else
            return replaceValue(value);
    }

    public int setAsIntFromLiteral(final int value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                setAsInt(value);
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
            setAsInt(DEFAULT);
    }

    public Integer set(Integer value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("Integer");
            setDefault();
        }
        else
            setAsInt(value);
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

    public void addChangeListener(final ObjectChangeListener<Integer> listener) {
        addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(int oldValue, int newValue, boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (IntChangeListener listener : replaceListeners) {
                try {
                    listener.onChange(oldValue, newValue);
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
            }
        }
    }
}
