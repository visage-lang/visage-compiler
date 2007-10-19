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

package com.sun.javafx.runtime.location;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * AbstractLocation is a base class for Location implementations, handling change listener notification and lazy updates.
 *
 * @author Brian Goetz
 */
public abstract class AbstractLocation implements Location {
    private boolean isValid;
    private final boolean isLazy;
    protected List<ChangeListener> listeners;

    protected AbstractLocation(boolean valid, boolean lazy) {
        isValid = valid;
        isLazy = lazy;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isLazy() {
        return isLazy;
    }

    protected void setValid() {
        isValid = true;
    }

    public void invalidate() {
        isValid = false;
        if (!isLazy())
            update();
        valueChanged();
    }

    public void valueChanged() {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange(this))
                    iterator.remove();
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        if (listeners == null)
            listeners = new LinkedList<ChangeListener>();
        listeners.add(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        addChangeListener(new WeakListener(listener));
    }

    public ChangeListener getWeakChangeListener() {
        return new WeakLocationListener(this);
    }

    public Collection<ChangeListener> getListeners() {
        if (listeners == null)
            return Collections.emptySet();
        else
            return Collections.unmodifiableCollection(listeners);
    }

    public void update() {
    }

    private static class WeakLocationListener extends WeakReference<Location> implements ChangeListener {
        public WeakLocationListener(Location referent) {
            super(referent);
        }

        public boolean onChange(Location location) {
            Location loc = get();
            if (loc == null)
                return false;
            else {
                loc.invalidate();
                return true;
            }
        }
    }

    private static class WeakListener extends WeakReference<ChangeListener> implements ChangeListener {

        public WeakListener(ChangeListener referent) {
            super(referent);
        }

        public boolean onChange(Location location) {
            ChangeListener listener = get();
            return listener == null ? false : listener.onChange(location);
        }
    }

    // For testing
    int getListenerCount() {
        return listeners == null ? 0 : listeners.size();
    }

    public void addDependencies(Location... dependencies) {
        for (Location dep : dependencies)
            dep.addChangeListener(getWeakChangeListener());
    }
}

