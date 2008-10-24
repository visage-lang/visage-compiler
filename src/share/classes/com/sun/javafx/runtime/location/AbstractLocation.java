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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.util.AbstractLinkable;
import com.sun.javafx.runtime.util.Linkable;

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
    // We've currently got fewer than four byte-size fields here already; we rely on the VM packing byte-size fields
    // together. If we need to add more, we could compress isValid into a bit in state, and/or haveClearedDependencies
    // into inUse.

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

    private static final Map<Location, IterationData> iterationData = new HashMap<Location, IterationData>();

    // What we are calling change listeners are really invalidation listeners.  They are invoked the same time
    // dependent locations are invalidated; the two list heads could also be merged to save space.
    private ChangeListener listeners;
    private WeakLocation dependentLocations;

    // The implementation of dynamic dependencies exploits the clearability of weak references.  When we register
    // ourselves as being dynamically dependent on another location for the first time, we create a weak reference
    // for ourselves that we remember and use for subsequence dynamic dependencies.  When we are asked to clear the
    // dynamic dependencies (unregister ourselves with any location with which we've registered as a dynamic dependency),
    // we clear() the weak reference and forget it, which will eventually cause those other locations to forget about us.
    private WeakMeLocation weakMeHead;

    private static AbstractLinkable.HeadAccessor<ChangeListener, AbstractLocation> changeListenerList
            = new AbstractLinkable.HeadAccessor<ChangeListener, AbstractLocation>() {
        public ChangeListener getHead(AbstractLocation host) {
            return host.listeners;
        }

        public void setHead(AbstractLocation host, ChangeListener newHead) {
            host.listeners = newHead;
        }
    };
    private static AbstractLinkable.HeadAccessor<WeakLocation, AbstractLocation> dependentLocationList
            = new AbstractLinkable.HeadAccessor<WeakLocation, AbstractLocation>() {
        public WeakLocation getHead(AbstractLocation host) {
            return host.dependentLocations;
        }

        public void setHead(AbstractLocation host, WeakLocation newHead) {
            host.dependentLocations = newHead;
        }
    };

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
        return (dependentLocations != null) || (listeners != null);
    }

    private static AbstractLinkable.MutativeIterationClosure<ChangeListener, AbstractLocation> CALL_LISTENER_CLOSURE
            = new AbstractLinkable.MutativeIterationClosure<ChangeListener, AbstractLocation>() {
        public boolean action(ChangeListener element) {
            try {
                return element.onChange();
            }
            catch (RuntimeException e) {
                ErrorHandler.triggerException(e);
                return true;
            }
        }
    };
    private static AbstractLinkable.MutativeIterationClosure<WeakLocation, AbstractLocation> INVALIDATE_DEPENDENCY_CLOSURE
            = new AbstractLinkable.MutativeIterationClosure<WeakLocation, AbstractLocation>() {
        public boolean action(WeakLocation element) {
            Location loc = element.get();
            if (loc == null)
                return false;
            else {
                loc.invalidate();
                // Space optimization: try for early removal of dynamic dependencies, in the case that
                // the dependency is a "weakMe" reference for some object that has been cleared in update()
                if (element.get() == null)
                    return false;
            }
            return true;
        }
    };

    /**
     * Notify change triggers and dependencies that the value has changed. This should be done automatically by mutative
     * methods, and is also used at object initialization time to defer notification of changes until the values
     * provided in the object literal are all set. */
    protected void invalidateDependencies() {
        if (listeners != null) {
            AbstractLinkable.iterate(changeListenerList, this, CALL_LISTENER_CLOSURE);
        }
        if (dependentLocations != null) {
            beginUpdate();
            try {
                AbstractLinkable.iterate(dependentLocationList, this, INVALIDATE_DEPENDENCY_CLOSURE);
            }
            finally {
                endUpdate();
            }
        }
    }

    private static final Linkable.MutativeIterationClosure<WeakLocation,AbstractLocation> PURGE_LISTENER_CLOSURE
            = new Linkable.MutativeIterationClosure<WeakLocation, AbstractLocation>() {
        public boolean action(WeakLocation element) {
            return (element.get() != null);
        }
    };

    void purgeDeadDependencies() {
        AbstractLinkable.iterate(dependentLocationList, this, PURGE_LISTENER_CLOSURE);
    }

    public void addDependentLocation(WeakLocation weakLocation) {
        if (!inUse()) {
            WeakLocation.purgeDeadLocations(this);
            assert(AbstractLinkable.isUnused(weakLocation));
            AbstractLinkable.addAtEnd(dependentLocationList, this, weakLocation);
        }
        else
            getInflated().addDependency(weakLocation);
    }

    public void addChangeListener(ChangeListener listener) {
        // @@@ Would be nice to merge lists for raw listeners with dependent location list
        // @@@ Should also defer adding listeners if the structures are in use
        assert(AbstractLinkable.isUnused(listener));
        AbstractLinkable.addAtEnd(changeListenerList, this, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        // @@@ Should also defer removing listeners if the structures are in use
        if (listeners != null)
            AbstractLinkable.remove(changeListenerList, this, listener);
    }

    public void addDependency(Location... dependencies) {
        if (dependencies.length > 0) {
            for (Location dep : dependencies)
                addDependency(dep);
        }
    }

    public void addDependency(Location location) {
        location.addDependentLocation(new WeakLocation(this));
    }

    public void addDynamicDependency(Location location) {
        WeakMeLocation newWeakMe = new WeakMeLocation(this);
        newWeakMe.nextWeakMe = weakMeHead;
        weakMeHead = newWeakMe;
        location.addDependentLocation(newWeakMe);
    }

    public void clearDynamicDependencies() {
        for (WeakMeLocation wh = weakMeHead; wh != null; wh = wh.nextWeakMe)
            wh.clear();
        // Hint to poll at reference queue
        WeakLocation.purgeDeadLocations(null);
        weakMeHead = null;
    }

    public <T extends Location> T addDynamicDependent(T dep) {
        addDynamicDependency(dep);
        return dep;
    }

    public <T extends Location> T addStaticDependent(T dep) {
        addDependency(dep);
        return dep;
    }

    public ChangeListener getListeners() {
        return listeners;
    }

    public void update() {
    }

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int getListenerCount() {
        purgeDeadDependencies();
        return AbstractLinkable.size(listeners) + AbstractLinkable.size(dependentLocations);
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

    private boolean inUse() {
        return inUse != INUSE_NOT;
    }

    private IterationData getInflated() {
        switch (inUse) {
            case INUSE_UNINFLATED:
                return inflateIterationData(1);

            case INUSE_INFLATED:
                return iterationData.get(this);

            default:
                assert false : "Not inflated";
                return null;
        }
    }

    private void endUpdate() {
        switch (inUse) {
            case INUSE_NOT:
                assert(false);
                break;

            case INUSE_UNINFLATED:
                inUse = INUSE_NOT;
                break;

            case INUSE_INFLATED:
                IterationData id = iterationData.get(this);
                --id.iterationDepth;
                if (id.iterationDepth == 0) {
                    inUse = INUSE_NOT;
                    iterationData.remove(this);
                    id.apply(this);
                }
                break;
        }
    }

    private static class IterationData {
        public int iterationDepth;
        public List<WeakLocation> deferredDependencies;

        public IterationData(int iterationDepth) {
            this.iterationDepth = iterationDepth;
        }

        public void addDependency(WeakLocation loc) {
            if (deferredDependencies == null)
                deferredDependencies = new LinkedList<WeakLocation>();
            deferredDependencies.add(loc);
        }

        public void apply(AbstractLocation target) {
            if (deferredDependencies != null && deferredDependencies.size() > 0) {
                WeakLocation.purgeDeadLocations(target);
                // @@@ Ugh, O(n^2)
                for (WeakLocation loc : deferredDependencies)
                    AbstractLinkable.addAtEnd(dependentLocationList, target, loc);
            }
        }
    }
}

