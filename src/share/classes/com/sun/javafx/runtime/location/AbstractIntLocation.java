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

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractIntLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractIntLocation extends AbstractLocation implements IntLocation {
    public static final int DEFAULT = 0;

    protected int $value = DEFAULT;
    private List<IntChangeListener> replaceListeners;

    protected AbstractIntLocation(boolean valid) {
        super(valid);
    }

    public int getAsInt() {
        return $value;
    }

    public int setAsInt(int value) {
        throw new UnsupportedOperationException();
    }

    public Integer get() {
        return getAsInt();
    }

    public Integer set(Integer value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }

    public DoubleLocation asDoubleLocation() {
        return Locations.asDoubleLocation(this);
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

    protected void notifyListeners(final int oldValue, final int newValue) {
        valueChanged();
        if (replaceListeners != null) {
            for (IntChangeListener listener : replaceListeners)
                listener.onChange(oldValue, newValue);
        }
    }

    protected int replaceValue(int newValue) {
        int oldValue = $value;
        if (oldValue != newValue) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public void fireInitialTriggers() {
        if ($value != DEFAULT)
            notifyListeners(DEFAULT, $value);
    }
}
