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

import java.util.LinkedList;
import java.util.List;

import com.sun.javafx.runtime.BindingException;

/**
 * AbstractVariable
 *
 * @author Brian Goetz
 */
public abstract class AbstractVariable<T_VALUE, T_LOCATION extends ObjectLocation<T_VALUE>, T_BINDING extends AbstractBindingExpression, T_LISTENER>
        extends AbstractLocation
        implements ObjectLocation<T_VALUE>, BindableLocation<T_VALUE, T_BINDING, T_LISTENER> {

    protected static final byte STATE_INITIAL = 0;
    protected static final byte STATE_UNBOUND_DEFAULT = 1;
    protected static final byte STATE_UNBOUND = 2;
    protected static final byte STATE_UNI_BOUND = 3;
    protected static final byte STATE_UNI_BOUND_LAZY = 4;
    protected static final byte STATE_BIDI_BOUND = 5;

    protected T_BINDING binding;

    protected DeferredInitializer deferredLiteral;
    protected List<T_LISTENER> replaceListeners;

    protected AbstractVariable() { }

    protected AbstractVariable(byte state) {
        this.state = state;
    }

    protected void resetState(byte newState) {
        if (isValid())
            super.invalidate();
        state = newState;
    }

    public boolean isInitialized() {
        return (state != STATE_INITIAL && state != STATE_UNBOUND_DEFAULT);
    }

    protected boolean isBound() {
        return state >= STATE_UNI_BOUND;
    }

    protected void ensureBindable() {
        if (isBound())
            throw new BindingException("Cannot rebind variable");
    }

    public void bijectiveBind(ObjectLocation<T_VALUE> other) {
        ensureBindable();
        resetState(STATE_BIDI_BOUND);
        Bindings.bijectiveBind(this, other);
    }

    public void bijectiveBindFromLiteral(final ObjectLocation<T_VALUE> other) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bijectiveBind(other);
            }
        };
    }

    protected abstract T_BINDING makeBindingExpression(T_LOCATION location);

    public void bind(T_LOCATION otherLocation) {
        bind(false, makeBindingExpression(otherLocation), otherLocation);
    }

    public void bindFromLiteral(final T_LOCATION otherLocation) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(otherLocation);
            }
        };
    }

    public void bind(boolean lazy, T_BINDING binding, Location... dependencies) {
        ensureBindable();
        resetState(lazy ? STATE_UNI_BOUND_LAZY : STATE_UNI_BOUND);
        this.binding = binding;
        binding.setLocation(this);
        addDependencies(dependencies);
        if (!lazy)
            update();
    }

    public void bindFromLiteral(final boolean lazy, final T_BINDING binding, final Location... dependencies) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                bind(lazy, binding, dependencies);
            }
        };
    }

    public T_VALUE setFromLiteral(final T_VALUE value) {
        deferredLiteral = new DeferredInitializer() {
            public void apply() {
                set(value);
            }
        };
        return value;
    }

    protected boolean isUnidirectionallyBound() {
        return state == STATE_UNI_BOUND || state == STATE_UNI_BOUND_LAZY;
    }

    protected boolean isLazilyBound() {
        return state == STATE_UNI_BOUND_LAZY;
    }

    /** Returns true if this instance needs a default value.  Warning: this method has side effects; when called,
     * it will try and apply any deferred values from the object literal, if there is one.  */
    public boolean needDefault() {
        if (deferredLiteral != null) {
            deferredLiteral.apply();
            deferredLiteral = null;
            return false;
        }
        else
            return !isInitialized();
    }

    public void initialize() {
        // This is where we used to do fireInitialTriggers when we were deferring triggers
        assert(deferredLiteral == null);
        deferredLiteral = null;
        if (isUnidirectionallyBound() && !isLazilyBound())
            update();
    }

    @Override
    public void invalidate() {
        if (isUnidirectionallyBound()) {
            super.invalidate();
            if (!isLazilyBound())
                update();
        }
        else
            throw new BindingException("Cannot invalidate non-bound variable");
    }

    public boolean isMutable() {
        return !isUnidirectionallyBound();
    }

    public void addChangeListener(T_LISTENER listener) {
        if (replaceListeners == null)
            replaceListeners = new LinkedList<T_LISTENER>();
        replaceListeners.add(listener);
    }

    public void removeChangeListener(T_LISTENER listener) {
        if (replaceListeners != null)
            replaceListeners.remove(listener);
    }

    public boolean hasDependencies() {
        return (replaceListeners != null && replaceListeners.size() > 0)
                || super.hasDependencies();
    }

    /** Called from replaceValue(); updates state machine and computes whether triggers should fire */
    protected boolean preReplace(boolean changed) {
        boolean shouldFire = changed;
        switch (state) {
            case STATE_UNBOUND:
                break;

            case STATE_INITIAL:
                state = STATE_UNBOUND;
                shouldFire = true;
                break;

            case STATE_UNBOUND_DEFAULT:
                state = STATE_UNBOUND;
                break;

            default:
                shouldFire = shouldFire || !isValid();
                break;
        }
        return shouldFire;
    }
}

interface DeferredInitializer {
    public void apply();
}
