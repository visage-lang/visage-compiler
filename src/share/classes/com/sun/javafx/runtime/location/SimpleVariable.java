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

import com.sun.javafx.runtime.Util;

/**
 * Lower-footprint, object-valued Location implementation.  Supports only dependencies, not triggers.
 *
 * @author Brian Goetz
 */
public class SimpleVariable<T> extends AbstractSimpleVariable implements ObjectLocation<T> {
    private T value;

    public SimpleVariable(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public T set(T value) {
        T oldValue = this.value;
        this.value = value;
        if (dependencies != null && !Util.isEqual(oldValue, value))
            notifyDependencies();
        return value;
    }

    public void setDefault() {
        set(null);
    }

    public boolean isNull() {
        return (value != null);
    }

    public T setFromLiteral(T value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(ObjectChangeListener<T> listener) {
        throw new UnsupportedOperationException();
    }
}
