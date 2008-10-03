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
    protected ChangeListener listeners;
    protected List<WeakReference<Location>> dependentLocations;

    // The implementation of dynamic dependencies exploits the clearability of weak references.  When we register
    // ourselves as being dynamically dependent on another location for the first time, we create a weak reference
    // for ourselves that we remember and use for subsequence dynamic dependencies.  When we are asked to clear the
    // dynamic dependencies (unregister ourselves with any location with which we've registered as a dynamic dependency),
    // we clear() the weak reference and forget it, which will eventually cause those other locations to forget about us.
    private WeakReference<Location> weakMe;

    private static AbstractLinkable.HeadAccessor<ChangeListener, AbstractLocation> changeListenerList
            = new AbstractLinkable.HeadAccessor<ChangeListener, AbstractLocation>() {
        public ChangeListener getHead(AbstractLocation host) {
            return host.listeners;
        }

        public void setHead(AbstractLocation host, ChangeListener newHead) {
            host.listeners = newHead;
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
        return (dependentLocations != null && dependentLocations.size() > 0) || (listeners != null);
    }

    private static AbstractLinkable.MutativeIterationClosure<ChangeListener, AbstractLocation> invalidationClosure
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

    /**
     * Notify change triggers and dependencies that the value has changed. This should be done automatically by mutative
     * methods, and is also used at object initialization time to defer notification of changes until the values
     * provided in the object literal are all set. */
    protected void invalidateDependencies() {
        if (listeners != null) {
            AbstractLinkable.iterate(changeListenerList, this, invalidationClosure);
        }
        if (dependentLocations != null) {
            beginUpdate();
            try {
                for (Iterator<WeakReference<Location>> iterator = dependentLocations.iterator(); iterator.hasNext(); ) {
                    WeakReference<Location> locationRef = iterator.next();
                    Location loc = locationRef.get();
                    if (loc == null)
                        iterator.remove();
                    else {
                        loc.invalidate();
                        // Space optimization: try for early removal of dynamic dependencies, in the case that
                        // the dependency is a "weakMe" reference for some object that has been cleared in update()
                        if (locationRef.get() == null)
                            iterator.remove();
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
            dependentLocations = new LinkedList<WeakReference<Location>>();
        switch (inUse) {
            case INUSE_NOT:
                // @@@ Hack: overly aggressive purge
                purgeDeadDependencies();
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
        // @@@ Would be nice to merge lists for raw listeners with dependent location list
        // @@@ Should also defer adding listeners if the structures are in use
        assert(AbstractLinkable.isUnused(listener));
        AbstractLinkable.<ChangeListener, AbstractLocation>addAtEnd(changeListenerList, this, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        // @@@ Should also defer removing listeners if the structures are in use
        if (listeners != null)
            AbstractLinkable.<ChangeListener, AbstractLocation>remove(changeListenerList, this, listener);
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

    public ChangeListener getListeners() {
        return listeners;
    }

    public void update() {
    }

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int getListenerCount() {
        purgeDeadDependencies();
        return (AbstractLinkable.size(listeners)) + (dependentLocations == null ? 0 : dependentLocations.size());
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
        public List<WeakReference<Location>> deferredDependencies;

        public IterationData(int iterationDepth) {
            this.iterationDepth = iterationDepth;
        }

        public void addDependency(WeakReference<Location> loc) {
            if (deferredDependencies == null)
                deferredDependencies = new LinkedList<WeakReference<Location>>();
            deferredDependencies.add(loc);
        }

        public void apply(AbstractLocation target) {
            if (deferredDependencies != null && deferredDependencies.size() > 0) {
                // @@@ Hack: overly aggressive purge
                target.purgeDeadDependencies();
                target.dependentLocations.addAll(deferredDependencies);
            }
        }
    }
}

class WeakLink<T> extends WeakReference<T> implements Linkable<WeakLink<T>, AbstractLocation> {
    WeakLink<T> next;
    AbstractLocation host;

    WeakLink(T referent) {
        super(referent);
    }

    public WeakLink<T> getNext() {
        return next;
    }

    public void setNext(WeakLink<T> next) {
        this.next = next;
    }

    public AbstractLocation getHost() {
        return host;
    }

    public void setHost(AbstractLocation host) {
        this.host = host;
    }
}

class WeakMeLink<T> extends WeakLink<T> {
    WeakMeLink<T> nextWeakMe;

    WeakMeLink(T referent) {
        super(referent);
    }

    public WeakMeLink<T> getNextWeakMe() {
        return nextWeakMe;
    }

    public void setNextWeakMe(WeakMeLink<T> nextWeakMe) {
        this.nextWeakMe = nextWeakMe;
    }
}
