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

    // We separate listeners from dependent locations because updating of dependent locations is split into an
    // invalidation phase and an update phase (this is to support lazy locations.)  So there are times when we want
    // to invalidate downstream locations but not yet invoke their change listeners.
    protected List<ChangeListener> listeners;
    protected List<WeakReference<Location>> dependentLocations;

    // The implementation of dynamic dependencies exploits the clearability of weak references.  When we register
    // ourselves as being dynamically dependent on another location for the first time, we create a weak reference
    // for ourselves that we remember and use for subsequence dynamic dependencies.  When we are asked to clear the
    // dynamic dependencies (unregister ourselves with any location with which we've registered as a dynamic dependency),
    // we clear() the weak reference and forget it, which will eventually cause those other locations to forget about us.
    private WeakReference<Location> weakMe;

    // Dynamic dependencies introduce a problem; we register dependent locations from the update() method, while the
    // list of dependencies is being iterated, causing CME.  So we defer adding anything to the list until we're done
    // iterating
    private int iterationDepth;
    private List<WeakReference<Location>> deferredDependencies;

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

    protected void setValid(boolean changed) {
        isValid = true;
        if (changed)
            notifyChangeListeners();
    }

    public void invalidate() {
        isValid = false;
        invalidateDependencies();
        if (!isLazy())
            update();
    }

    public void valueChanged() {
        notifyChangeListeners();
        invalidateDependencies();
    }

    /** For use only by InitHelper; to be overridden by subclasses */
    public void fireInitialTriggers() {
    }

    private void purgeDeadDependencies() {
        if (dependentLocations != null) {
            for (Iterator<WeakReference<Location>> iterator = dependentLocations.iterator(); iterator.hasNext();) {
                WeakReference<Location> locationRef = iterator.next();
                Location loc = locationRef.get();
                if (loc == null)
                    iterator.remove();
            }
        }
    }

    private void invalidateDependencies() {
        if (dependentLocations != null) {
            try {
                ++iterationDepth;
                for (Iterator<WeakReference<Location>> iterator = dependentLocations.iterator(); iterator.hasNext();) {
                    WeakReference<Location> locationRef = iterator.next();
                    Location loc = locationRef.get();
                    if (loc == null)
                        iterator.remove();
                    else
                        loc.invalidate();
                }
            }
            finally {
                --iterationDepth;
                if (iterationDepth == 0 && deferredDependencies != null && deferredDependencies.size() > 0) {
                    dependentLocations.addAll(deferredDependencies);
                    deferredDependencies.clear();
                }
            }
        }
    }

    private void notifyChangeListeners() {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                if (!listener.onChange(this))
                    iterator.remove();
            }
        }
    }

    public void addDependentLocation(WeakReference<Location> location) {
        if (dependentLocations == null)
            dependentLocations = new ArrayList<WeakReference<Location>>();
        if (iterationDepth > 0) {
            if (deferredDependencies == null)
                deferredDependencies = new ArrayList<WeakReference<Location>>();
            deferredDependencies.add(location);
        } else
            dependentLocations.add(location);
    }

    public void addChangeListener(ChangeListener listener) {
        if (listeners == null)
            listeners = new ArrayList<ChangeListener>();
        listeners.add(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        addChangeListener(new WeakListener(listener));
    }

    public void addDependencies(Location... dependencies) {
        if (dependencies.length > 0) {
            WeakReference<Location> wr = new WeakReference<Location>(this);
            for (Location dep : dependencies)
                dep.addDependentLocation(wr);
        }
    }

    public void addDynamicDependency(Location location) {
        if (weakMe == null)
            weakMe = new WeakReference<Location>(this);
        location.addDependentLocation(weakMe);
    }

    public void clearDynamicDependencies() {
        if (weakMe != null) {
            weakMe.clear();
            weakMe = null;
        }
    }

    public<T extends Location> T addDynamicDependent(T dep) {
        addDynamicDependency(dep);
        return dep;
    }

    public<T extends Location> T addStaticDependent(T dep) {
        addDependencies(dep);
        return dep;
    }

    public Collection<ChangeListener> getListeners() {
        if (listeners == null)
            return Collections.emptySet();
        else
            return Collections.unmodifiableCollection(listeners);
    }

    public void update() {
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

    protected static boolean equals(Object a, Object b) {
        return ((a == null) && (b == null)) || ((a != null) && a.equals(b));
    }

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int getListenerCount() {
        purgeDeadDependencies();
        return (listeners == null ? 0 : listeners.size())
                + (dependentLocations == null ? 0 : dependentLocations.size());
    }
}

