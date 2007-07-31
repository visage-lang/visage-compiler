/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.runtime;

/**
 * A container for a (usually-settable) value.
 * May be part of a dependency network.
 *
 * @author Per Bothner
 */

public abstract class AbstractLocation implements Location {

    protected AbstractLocation() {
    }

    public double asDouble() {
        throw new IllegalArgumentException();
    }

    public Object asObject() {
        throw new IllegalArgumentException();
    }

    public boolean asBoolean() {
        throw new IllegalArgumentException();
    }

    public int asInt() {
        throw new IllegalArgumentException();
    }

    public void setDouble(double value) {
        throw new IllegalArgumentException();
    }

    public void setObject(Object value) {
        throw new IllegalArgumentException();
    }

    public void setBoolean(boolean value) {
        throw new IllegalArgumentException();
    }

    public void setInt(int value) {
        throw new IllegalArgumentException();
    }

    private ChangeListenerEntry listeners;

    public void addWeakChangeListener(ChangeListener listener, Object key) {
        addChangeListener(new WeakListener(listener, key));
    }

    public void addChangeListener(ChangeListenerEntry entry) {
        entry.setNext(listeners);
        listeners = entry;
    }

    protected void notifyChangeListeners() {
        ChangeListenerEntry prev = null;
        ChangeListenerEntry listener = listeners;
        while (listener != null) {
            ChangeListenerEntry next = listener.getNext();
            if (listener instanceof WeakListener) {
                WeakListener wlistener = (WeakListener) listener;
                ChangeListener xlistener = wlistener.get();
                if (xlistener == null) {
                    if (prev != null)
                        prev.setNext(next);
                    else
                        listeners = next;
                } else {
                    prev = listener;
                    xlistener.changed(wlistener.key);
                }
            } else {
                prev = listener;
                listener.changed();
            }
            listener = next;
        }
    }
}
