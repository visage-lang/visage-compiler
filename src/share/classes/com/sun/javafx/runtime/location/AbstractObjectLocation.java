/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.List;
import java.util.ArrayList;

/**
 * AbstractObjectLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractObjectLocation<T> extends AbstractLocation implements ObjectLocation<T> {
    protected T $value;
    private List<ObjectChangeListener<T>> replaceListeners;

    protected AbstractObjectLocation(boolean valid, T value) {
        super(valid);
        set(value);
    }

    protected AbstractObjectLocation(boolean valid) {
        super(valid);
    }

    public T get() {
        return $value;
    }

    public boolean isNull() {
        return $value == null;
    }

    protected boolean changed(T oldValue, T newValue) {
        if (oldValue == null) {
            return newValue != null;
        } else
            return !oldValue.equals(newValue);
    }

    public T set(T value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(ObjectChangeListener<T> listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<ObjectChangeListener<T>>();
        replaceListeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    protected void notifyListeners(final T oldValue, final T newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (ObjectChangeListener<T> listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

    protected T replaceValue(T newValue) {
        T oldValue = $value;
        if (changed(oldValue, newValue)) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public void fireInitialTriggers() {
        if ($value != null)
            notifyListeners(null, $value);
    }
}
