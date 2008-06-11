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

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.AssignToBoundException;
import com.sun.javafx.runtime.ErrorHandler;

/**
 * IntVariable
 *
 * @author Brian Goetz
 */
public class IntVariable extends AbstractVariable<Integer, IntLocation, IntBindingExpression> implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;
    private List<IntChangeListener> replaceListeners;

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
        this();
        $value = value;
        setValid();
    }

    protected IntVariable(boolean lazy, IntBindingExpression binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    public int getAsInt() {
        if (isBound() && !isValid())
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
        if (oldValue != newValue || !isInitialized() || !isEverValid()) {
            boolean notifyDependencies = isValid() || !isInitialized() || !isEverValid();
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, notifyDependencies);
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
        if (isBound() && $value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
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
            if (isBound() && !isValid())
                replaceValue(binding.computeValue());
        }
        catch (RuntimeException e) {
            ErrorHandler.bindException(e);
            if (isInitialized())
                replaceValue(DEFAULT);
        }
    }

    public void addChangeListener(IntChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<IntChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Integer> listener) {
        addChangeListener(new IntChangeListener() {
            public void onChange(int oldValue, int newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(int oldValue, int newValue, boolean notifyDependencies) {
        if (notifyDependencies)
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
