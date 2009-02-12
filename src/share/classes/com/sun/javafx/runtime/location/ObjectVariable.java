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
import com.sun.javafx.runtime.Util;

/**
 * ObjectVariable
 *
 * @author Brian Goetz
 */
public class ObjectVariable<T>
        extends AbstractVariable<T, ObjectLocation<T>, ObjectChangeListener<T>>
        implements ObjectLocation<T> {

    protected T $value;
    protected T $default;

    public static<T> ObjectVariable<T> make() {
        return new ObjectVariable<T>();
    }

    public static<T> ObjectVariable<T> makeWithDefault(T deflt) {
        ObjectVariable<T> result = new ObjectVariable<T>();
        result.$default = deflt;
        result.$value = deflt;
        return result;
    }

    public static<T> ObjectVariable<T> make(T value) {
        return new ObjectVariable<T>(value);
    }

    public static<T> ObjectVariable<T> make(boolean lazy, BindingExpression binding, Location... dependencies) {
        return new ObjectVariable<T>(null, lazy, binding, dependencies);
    }

    public static<T> ObjectVariable<T> make(T dflt, boolean lazy, BindingExpression binding, Location... dependencies) {
        return new ObjectVariable<T>(dflt, lazy, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static<T> ObjectVariable<T> makeBijective(ObjectLocation<T> other) {
        ObjectVariable<T> me = ObjectVariable.make();
        me.bijectiveBind(other);
        return me;
    }

    protected ObjectVariable() { }

    protected ObjectVariable(T value) {
        super(STATE_UNBOUND);
        $value = value;
        setValid();
    }

    protected ObjectVariable(T dflt, boolean lazy, BindingExpression binding, Location... dependencies) {
        this();
        $default = dflt;
        bind(lazy, binding);
        addDependency(dependencies);
    }

    protected BindingExpression makeBindingExpression(final ObjectLocation<T> otherLocation) {
        return new BindingExpression() {
            public void compute() { pushValue(otherLocation.get()); }
        };
    }

    public T get() {
        ensureValid();
        return $value;
    }

    protected T replaceValue(T newValue) {
        T oldValue = $value;
        if (preReplace(!Util.isEqual(oldValue, newValue))) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return newValue;
    }

    public T set(T value) {
        if (isUnidirectionallyBound() && !Util.isEqual($value, value))
            throw new AssignToBoundException("Cannot assign to bound variable");
        else
            return replaceValue(value);
    }

    public void setDefault() {
        if (state == STATE_INITIAL) {
            $value = $default;
            state = STATE_UNBOUND_DEFAULT;
            notifyListeners($default, $default, true);
        }
        else
            set($default);
    }

    public void replaceWithDefault() { replaceValue($default); }

    public boolean isNull() {
        return $value == null;
    }

    private void notifyListeners(final T oldValue, final T newValue, boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (hasChildren(CHILD_KIND_TRIGGER))
            iterateChildren(new DependencyIterator<ObjectChangeListener<T>>(CHILD_KIND_TRIGGER) {
                public void onAction(ObjectChangeListener<T> listener) {
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
