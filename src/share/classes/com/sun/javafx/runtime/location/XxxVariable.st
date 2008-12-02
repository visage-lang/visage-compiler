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

/*
 * WARNING: Automatically generated from the template $TEMPLATE_NAME$; do not edit!
 */

/**
 * $T.Variable$
 *
 * @author Brian Goetz
 */
public class $T.Variable$ extends AbstractVariable<$T.boxed$, $T.Location$, $T.BindingExpression$, $T.ChangeListener$> implements $T.Location$ {
    public static final $T.prim$ DEFAULT = $V.default$;

    protected $T.prim$ \$value = DEFAULT;

    public static $T.Variable$ make() {
        return new $T.Variable$();
    }

    public static $T.Variable$ make($T.prim$ value) {
        return new $T.Variable$(value);
    }

    public static $T.Variable$ make(boolean lazy, $T.BindingExpression$ binding, Location... dependencies) {
        return new $T.Variable$(lazy, binding, dependencies);
    }

    public static $T.Variable$ make($T.BindingExpression$ binding, Location... dependencies) {
        return new $T.Variable$(false, binding, dependencies);
    }

    /** Create a bijectively bound variable */
    public static $T.Variable$ makeBijective(ObjectVariable<$T.boxed$> other) {
        $T.Variable$ me = $T.Variable$.make();
        me.bijectiveBind(other);
        return me;
    }

    protected $T.Variable$() { }

    protected $T.Variable$($T.prim$ value) {
        super(STATE_UNBOUND);
        \$value = value;
        setValid();
    }

    protected $T.Variable$(boolean lazy, $T.BindingExpression$ binding, Location... dependencies) {
        this();
        bind(lazy, binding);
        addDependency(dependencies);
    }

    public $T.prim$ getAs$PREFIX$() {
        if (isUnidirectionallyBound() && !isValid())
            update();
        return \$value;
    }

    public $T.boxed$ get() {
        return getAs$PREFIX$();
    }

    public boolean isNull() {
        return false;
    }

    protected $T.prim$ replaceValue($T.prim$ newValue) {
        $T.prim$ oldValue = \$value;
        if (preReplace(oldValue != newValue)) {
            boolean invalidateDependencies = isValid() || state == STATE_UNBOUND;
            \$value = newValue;
            setValid();
            notifyListeners(oldValue, newValue, invalidateDependencies);
        }
        else
            setValid();
        return newValue;
    }

    protected $T.BindingExpression$ makeBindingExpression(final $T.Location$ otherLocation) {
        return new $T.BindingExpression$() {
            public $T.prim$ computeValue() {
                return otherLocation.getAs$PREFIX$();
            }
        };
    }

    public $T.prim$ setAs$PREFIX$($T.prim$ value) {
        if (isUnidirectionallyBound() && \$value != value)
            throw new AssignToBoundException("Cannot assign to bound variable");
        else
            return replaceValue(value);
    }

    public $T.prim$ setAs$PREFIX$FromLiteral(final $T.prim$ value) {
        setDeferredLiteral(new DeferredInitializer() {
            public void apply() {
                setAs$PREFIX$(value);
            }
        });
        return value;
    }

    public void setDefault() {
        if (state == STATE_INITIAL) {
            \$value = DEFAULT;
            state = STATE_UNBOUND_DEFAULT;
            notifyListeners(DEFAULT, DEFAULT, true);
        }
        else
            setAs$PREFIX$(DEFAULT);
    }

    public $T.boxed$ set($T.boxed$ value) {
        if (value == null) {
            ErrorHandler.nullToPrimitiveCoercion("$T.boxed$");
            setDefault();
        }
        else
            setAs$PREFIX$(value);
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

    public void addChangeListener(final ObjectChangeListener<$T.boxed$> listener) {
        addChangeListener(new $T.ChangeListener$() {
            public void onChange($T.prim$ oldValue, $T.prim$ newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    private void notifyListeners(final $T.prim$ oldValue, final $T.prim$ newValue, boolean invalidateDependencies) {
        if (invalidateDependencies)
            invalidateDependencies();
        if (hasChildren(CHILD_KIND_TRIGGER))
            iterateChildren(new DependencyIterator<$T.ChangeListener$>(CHILD_KIND_TRIGGER) {
                public void onAction($T.ChangeListener$ listener) {
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
