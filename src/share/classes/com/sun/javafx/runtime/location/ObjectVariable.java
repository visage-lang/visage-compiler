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
import com.sun.javafx.runtime.Util;

/**
 * ObjectVariable
 *
 * @author Brian Goetz
 */
public class ObjectVariable<T>
        extends AbstractVariable<T, ObjectLocation<T>, ObjectBindingExpression<T>>
        implements ObjectLocation<T> {

    protected T $value;
    private List<ObjectChangeListener<T>> replaceListeners;

    public static<T> ObjectVariable<T> make() {
        return new ObjectVariable<T>();
    }

    public static<T> ObjectVariable<T> make(T value) {
        return new ObjectVariable<T>(value);
    }

    public static<T> ObjectVariable<T> make(boolean lazy, ObjectBindingExpression<T> binding, Location... dependencies) {
        return new ObjectVariable<T>(lazy, binding, dependencies);
    }

    public static<T> ObjectVariable<T> make(ObjectBindingExpression<T> binding, Location... dependencies) {
        return new ObjectVariable<T>(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static<T> ObjectVariable<T> makeBijective(ObjectLocation<T> other) {
        ObjectVariable<T> me = ObjectVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected ObjectVariable() { }

    protected ObjectVariable(T value) {
        this();
        $value = value;
        setValid();
    }

    protected ObjectVariable(boolean lazy, ObjectBindingExpression<T> binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependencies(dependencies);
    }

    protected ObjectBindingExpression<T> makeBindingExpression(final ObjectLocation<T> otherLocation) {
        return new ObjectBindingExpression<T>() {
            public T computeValue() {
                return otherLocation.get();
            }
        };
    }

    public T get() {
        if (isBound() && !isValid())
            update();
        return $value;
    }

    protected T replaceValue(T newValue) {
        T oldValue = $value;
        if (!Util.isEqual(oldValue, newValue) || !isInitialized() || !isEverValid()) {
            boolean notifyDependencies = isValid() || !isInitialized() || !isEverValid();
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, notifyDependencies);
        }
        else
            setValid();
        return newValue;
    }

    public T set(T value) {
        if (isBound() && !Util.isEqual($value, value))
            throw new AssignToBoundException("Cannot assign to bound variable");
        return replaceValue(value);
    }

    public void setDefault() {
        set(null);
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
                replaceValue(null);
        }
    }

    public boolean isNull() {
        return $value == null;
    }

    public void addChangeListener(ObjectChangeListener<T> listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<ObjectChangeListener<T>>();
        replaceListeners.add(listener);
    }

    private void notifyListeners(T oldValue, T newValue, boolean notifyDependencies) {
        if (notifyDependencies)
            invalidateDependencies();
        if (replaceListeners != null) {
            for (ObjectChangeListener<T> listener : replaceListeners)
                try {
                    listener.onChange(oldValue, newValue);
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
        }
    }
}
