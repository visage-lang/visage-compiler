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

import com.sun.javafx.runtime.DeferredTrigger;

/**
 * AbstractDoubleLocation
 *
 * @author Brian Goetz
 */
public abstract class AbstractDoubleLocation extends AbstractLocation implements DoubleLocation {
    public static final double DEFAULT = 0.0;

    protected double $value = DEFAULT;
    private List<DoubleChangeListener> replaceListeners;

    protected AbstractDoubleLocation(boolean valid, boolean lazy, double value) {
        super(valid, lazy);
        setAsDouble(value);
    }

    protected AbstractDoubleLocation(boolean valid, boolean lazy) {
        super(valid, lazy);
    }

    public double getAsDouble() {
        return $value;
    }

    public double setAsDouble(double value) {
        throw new UnsupportedOperationException();
    }

    public void setDefault() {
        throw new UnsupportedOperationException();
    }

    public Double get() {
        return getAsDouble();
    }

    public Double set(Double value) {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }

    public void addChangeListener(DoubleChangeListener listener) {
        if (replaceListeners == null)
            replaceListeners = new ArrayList<DoubleChangeListener>();
        replaceListeners.add(listener);
    }

    public void addChangeListener(final ObjectChangeListener<Double> listener) {
        addChangeListener(new DoubleChangeListener() {
            public void onChange(double oldValue, double newValue) {
                listener.onChange(oldValue, newValue);
            }
        });
    }

    protected void notifyListeners(final double oldValue, final double newValue) {
        valueChanged();
        if (replaceListeners != null) {
            if (isTriggersDeferred()) {
                final DoubleChangeListener[] listenerCopy = replaceListeners.toArray(new DoubleChangeListener[replaceListeners.size()]);
                deferTrigger(new DeferredTrigger() {
                    public void run() {
                        for (DoubleChangeListener listener : listenerCopy)
                                listener.onChange(oldValue, newValue);
                    }
                });
            }
            else {
                for (DoubleChangeListener listener : replaceListeners)
                    listener.onChange(oldValue, newValue);
            }
        }
    }

    protected double replaceValue(double newValue) {
        double oldValue = $value;
        if (oldValue != newValue) {
            $value = newValue;
            setValid();
            notifyListeners(oldValue, newValue);
        }
        else
            setValid();
        return newValue;
    }

    public void inherit(AbstractLocation otherLocation) {
        super.inherit(otherLocation);
        if (replaceListeners != null)
            for (DoubleChangeListener listener : ((AbstractDoubleLocation) otherLocation).replaceListeners)
                addChangeListener(listener);
    }


    public void fireInitialTriggers() {
        if ($value != DEFAULT)
            notifyListeners(DEFAULT, $value);
    }
}
