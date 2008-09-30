/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.lang.ref.WeakReference;
import java.util.*;

import com.sun.javafx.runtime.ErrorHandler;

/**
 * AbstractLocation is a base class for Location implementations, handling change listener notification and lazy updates.
 *
 * @author Brian Goetz
 */
public abstract class AbstractLocation implements Location {

    private static final byte INUSE_NOT = 0;
    private static final byte INUSE_UNINFLATED = 1;
    private static final byte INUSE_INFLATED = 2;

    // Space is at a premium; FX classes use a *lot* of locations.
    // We've got four byte-size fields here already; we rely on the VM packing byte-size fields together.  If we need
    // to add more, we could compress isValid into a bit in state, and/or haveClearedDependencies into inUse.

    /** The isValid flag means that the location currently has an up-to-date value. This would be true if the value is
    already known, or if the location has a binding and the binding does not need recomputation. */
    private boolean isValid;

    /** For use by subclasses */
    protected byte state;

    /** The inUse flag indicates that we are currently traversing our listener lists, and therefore need to defer
     * modifying the lists until we're done traversing.  It serves as a two-bit reference count; if we "enter" the list
     * more than once, we'll observe that the list is already in use, and lazily inflate a structure to hold the count
     * and any pending modifications */
    private byte inUse;

    /** A bit that indicates that there exist one or more dependent locations that are cleared and that
     * should be purged after inUse goes to _NOT */
    private static boolean haveClearedDependencies;

    private static final Map<Location, IterationData> iterationData = new HashMap<Location, IterationData>();

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

    public boolean isValid() {
        return isValid;
    }

    public boolean isMutable() {
        return true;
    }

    protected void setValid() {
        isValid = true;
    }

    public void invalidate() {
        boolean wasValid = isValid;
        isValid = false;
        if (wasValid)
            invalidateDependencies();
    }

    public boolean hasDependencies() {
        return (dependentLocations != null && dependentLocations.size() > 0)
                || (listeners != null && listeners.size() > 0);
    }
    
    /**
     * Notify change triggers and dependencies that the value has changed. This should be done automatically by mutative
     * methods, and is also used at object initialization time to defer notification of changes until the values
     * provided in the object literal are all set. */
    protected void invalidateDependencies() {
        if (listeners != null) {
            for (Iterator<ChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
                ChangeListener listener = iterator.next();
                try {
                    if (!listener.onChange())
                        iterator.remove();
                }
                catch (RuntimeException e) {
                    ErrorHandler.triggerException(e);
                }
            }
        }
        if (dependentLocations != null) {
            beginUpdate();
            try {
                for (WeakReference<Location> locationRef : dependentLocations) {
                    Location loc = locationRef.get();
                    if (loc == null)
                        haveClearedDependencies = true;
                    else {
                        loc.invalidate();
                        // Space optimization: try for early removal of dynamic dependencies, in the case that
                        // the dependency is a "weakMe" reference for some object that has been cleared in update()
                        if (locationRef.get() == null)
                            haveClearedDependencies = true;
                    }
                }
            }
            finally {
                endUpdate();
            }
        }
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

    public void addDependentLocation(WeakReference<Location> locationRef) {
        if (dependentLocations == null)
            dependentLocations = new ArrayList<WeakReference<Location>>();
        switch (inUse) {
            case INUSE_NOT:
                // @@@ This is where we used to do the overly aggressive purge
                dependentLocations.add(locationRef);
                break;

            case INUSE_UNINFLATED:
                IterationData id = inflateIterationData(1);
                id.addDependency(locationRef);
                break;

            case INUSE_INFLATED:
                id = iterationData.get(this);
                id.addDependency(locationRef);
                break;
        }
    }

    public void addChangeListener(ChangeListener listener) {
        // @@@ Would be nice to get rid of raw change listeners, and stick with the type-specific versions (e.g.,
        // ObjectChangeListener), but for now Bijection relies on access to the raw listener because it needs to
        // be able to unregister itself when the weak references are cleared
        // @@@ Should also defer adding listeners if the structures are in use
        if (listeners == null)
            listeners = new ArrayList<ChangeListener>();
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        // @@@ Should also defer removing listeners if the structures are in use
        if (listeners != null)
            listeners.remove(listener);
    }

    public void addWeakListener(ChangeListener listener) {
        // @@@ Should also defer adding listeners if the structures are in use
        addChangeListener(new WeakListener(listener));
    }

    public void addDependencies(Location... dependencies) {
        // @@@ Provide a one-arg version so autoboxing is not always used
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

    public <T extends Location> T addDynamicDependent(T dep) {
        addDynamicDependency(dep);
        return dep;
    }

    public <T extends Location> T addStaticDependent(T dep) {
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

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int getListenerCount() {
        purgeDeadDependencies();
        return (listeners == null ? 0 : listeners.size())
                + (dependentLocations == null ? 0 : dependentLocations.size());
    }

    private IterationData inflateIterationData(int count) {
        inUse = INUSE_INFLATED;
        IterationData id = new IterationData(count);
        iterationData.put(this, id);
        return id;
    }
    
    private void beginUpdate() {
        switch (inUse) {
            case INUSE_NOT:
                inUse = INUSE_UNINFLATED;
                break;

            case INUSE_UNINFLATED:
                inflateIterationData(2);
                break;

            case INUSE_INFLATED:
                IterationData id = iterationData.get(this);
                ++id.iterationDepth;
                break;
        }
    }

    private void endUpdate() {
        switch (inUse) {
            case INUSE_NOT:
                assert(false);
                break;

            case INUSE_UNINFLATED:
                inUse = INUSE_NOT;
                if (haveClearedDependencies) {
                    purgeDeadDependencies();
                    haveClearedDependencies = false;
                }
                break;

            case INUSE_INFLATED:
                IterationData id = iterationData.get(this);
                --id.iterationDepth;
                if (id.iterationDepth == 0) {
                    inUse = INUSE_NOT;
                    iterationData.remove(this);
                    id.apply(this);
                    if (haveClearedDependencies) {
                        purgeDeadDependencies();
                        haveClearedDependencies = false;
                    }
                }
                break;
        }
    }

    private static class IterationData {
        public int iterationDepth;
        public List<WeakReference<Location>> deferredDependencies;

        public IterationData(int iterationDepth) {
            this.iterationDepth = iterationDepth;
        }

        public void addDependency(WeakReference<Location> loc) {
            if (deferredDependencies == null)
                deferredDependencies = new ArrayList<WeakReference<Location>>();
            deferredDependencies.add(loc);
        }

        public void apply(AbstractLocation target) {
            if (deferredDependencies != null && deferredDependencies.size() > 0) {
                // @@@ This is where we used to do the overly aggressive purge
                target.dependentLocations.addAll(deferredDependencies);
            }
        }
    }
}

class WeakListener extends WeakReference<ChangeListener> implements ChangeListener {
    public WeakListener(ChangeListener referent) {
        super(referent);
    }

    public boolean onChange() {
        ChangeListener listener = get();
        return listener == null ? false : listener.onChange();
    }
}

