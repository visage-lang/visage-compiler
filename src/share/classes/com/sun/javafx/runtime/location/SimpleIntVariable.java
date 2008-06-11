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

/**
 * SimpleIntVariable
 *
 * @author Brian Goetz
 */
public class SimpleIntVariable extends AbstractSimpleVariable implements IntLocation {
    private int value;

    public SimpleIntVariable(int value) {
        this.value = value;
    }

    public int getAsInt() {
        return value;
    }

    public int setAsInt(int value) {
        int oldValue = this.value;
        this.value = value;
        if (dependencies != null && oldValue != value)
            notifyDependencies();
        return value;
    }

    public void setDefault() {
        setAsInt(0);
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer set(Integer value) {
        return setAsInt(value);
    }

    public void addChangeListener(IntChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public int setAsIntFromLiteral(int value) {
        throw new UnsupportedOperationException();
    }

    public Integer setFromLiteral(Integer value) {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(ObjectChangeListener<Integer> listener) {
        throw new UnsupportedOperationException();
    }
}
