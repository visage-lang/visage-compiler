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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.javafx.runtime.ErrorHandler;
import com.sun.javafx.runtime.util.Linkable;
import com.sun.javafx.runtime.util.Linkables;

/**
 * AbstractLocation is a base class for Location implementations, handling change listener notification and lazy updates.
 *
 * @author Brian Goetz
 */
public abstract class AbstractLocation implements Location, Linkable<LocationDependency> {

    private static final byte INUSE_NOT = 0;
    private static final byte INUSE_UNINFLATED = 1;
    private static final byte INUSE_INFLATED = 2;

    static final int CHILD_KIND_CHANGE_LISTENER = 1;
    static final int CHILD_KIND_WEAK_LOCATION = 2;
    static final int CHILD_KIND_TRIGGER = 4;
    static final int CHILD_KIND_BINDING_EXPRESSION = 8;
    static final int CHILD_KIND_LITERAL_INITIALIZER = 16;
    static final int CHILD_KIND_WEAK_ME_HOLDER = 32;
    static final int CHILD_KIND_VIEW_LOCATION = 64;

    // Space is at a premium; FX classes use a *lot* of locations.
    // We've currently got four byte-size fields here already; we rely on the VM packing byte-size fields
    // together. If we need to add more, we could compress isValid into a bit in state.

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

    /** As we add and remove dependencies, we maintain a mask of the kinds of things on the dependency list, so we can
     * quickly answer questions like "do we have any X's"
     */
    private byte childKindMask;

    // This list contains several kinds of ancillary objects, including dependencies:
    //   Change listeners (really invalidation listeners): called when this location is invalidated
    //   Dependent locations: invalidated when this location is invalidated
    //   Value triggers: invoked when the new value is known (might not happen at invalidation time, as with lazy
    //                   binding.)
    //   WeakMeHolder: holder for weakMe reference, used in dyanamic dependencies.
    //     The implementation of dynamic dependencies exploits the clearability of weak references.  When we register
    //     ourselves as being dynamically dependent on another location for the first time, we create a weak reference
    //     for ourselves that we remember and use for subsequence dynamic dependencies.  When we are asked to clear the
    //     dynamic dependencies (unregister ourselves with any location with which we've registered as a dynamic dependency),
    //     we clear() the weak reference and forget it, which will eventually cause those other locations to forget about us.
    // Elements are differentiated by their dependency kind.
    private LocationDependency children;

    private static final Map<Location, IterationData> iterationData = new HashMap<Location, IterationData>();


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

    protected void recalculateChildMask() {
        int mask = 0;
        for (LocationDependency cur = children; cur != null; cur = cur.getNext())
            mask |= cur.getDependencyKind();
        childKindMask = (byte) mask;
    }

    protected int countChildren(int mask) {
        int count = 0;
        for (LocationDependency cur = children; cur != null; cur = cur.getNext())
            if ((mask & cur.getDependencyKind()) != 0)
                ++count;
        return count;
    }

    protected void orChildMask(int newKinds) {
        childKindMask |= newKinds;
    }

    protected boolean hasChildren(int kindMask) {
        return (childKindMask & kindMask) != 0;
    }

    protected boolean hasDependencies() {
        return hasChildren(CHILD_KIND_WEAK_LOCATION | CHILD_KIND_CHANGE_LISTENER | CHILD_KIND_TRIGGER);
    }

    protected void enqueueChild(LocationDependency dep) {
        Linkables.addAtEnd(this, dep);
        orChildMask(dep.getDependencyKind());
    }

    protected void dequeueChild(LocationDependency dep) {
        if (children != null && Linkables.remove(dep))
            recalculateChildMask();
    }

    protected LocationDependency findChildByKind(int kind) {
        for (LocationDependency cur = children; cur != null; cur = cur.getNext())
            if (cur.getDependencyKind() == kind)
                return cur;
        return null;
    }

    public LocationDependency getNext() {
        return children;
    }

    public Linkable<LocationDependency> getPrev() {
        throw new UnsupportedOperationException();
    }

    public void setNext(LocationDependency next) {
        children = next;
    }

    public void setPrev(Linkable<LocationDependency> prev) {
        throw new UnsupportedOperationException();
    }

    private void iterateChildren(MutativeIterator<? extends LocationDependency> closure) {
        if (hasChildren(closure.kind))
            Linkables.iterate(children, closure);
    }

    static abstract class MutativeIterator<T extends LocationDependency>
            implements Linkable.MutativeIterationClosure<LocationDependency> {
        private final int kind;

        public MutativeIterator(int kind) {
            this.kind = kind;
        }

        public abstract boolean onAction(T element);

        @SuppressWarnings("unchecked")
        public boolean action(LocationDependency element) {
            if (element.getDependencyKind() == kind)
                return onAction((T) element);
            else
                return true;
        }
    }

    private static MutativeIterator<ChangeListener> CALL_LISTENER_CLOSURE
            = new MutativeIterator<ChangeListener>(CHILD_KIND_CHANGE_LISTENER) {
        public boolean onAction(ChangeListener element) {
            try {
                return element.onChange();
            }
            catch (RuntimeException e) {
                ErrorHandler.triggerException(e);
                return true;
            }
        }
    };
    private static MutativeIterator<WeakLocation> INVALIDATE_DEPENDENCY_CLOSURE
            = new MutativeIterator<WeakLocation>(CHILD_KIND_WEAK_LOCATION) {
        public boolean onAction(WeakLocation element) {
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
        if (hasChildren(CHILD_KIND_CHANGE_LISTENER | CHILD_KIND_WEAK_LOCATION)) {
            beginUpdate();
            try {
                // @@@ We're iterating twice, this is to preserve listener ordering for now
                iterateChildren(CALL_LISTENER_CLOSURE);
                iterateChildren(INVALIDATE_DEPENDENCY_CLOSURE);
            }
            finally {
                endUpdate();
            }
        }
    }

    private static final MutativeIterator<WeakLocation> PURGE_LISTENER_CLOSURE
            = new MutativeIterator<WeakLocation>(CHILD_KIND_WEAK_LOCATION) {
        public boolean onAction(WeakLocation element) {
            return (element.get() != null);
        }
    };

    void purgeDeadDependencies() {
        if (!inUse())
            iterateChildren(PURGE_LISTENER_CLOSURE);
        else
            getInflated().requestPurge();
    }

    protected void addChild(LocationDependency dep) {
        assert(Linkables.isUnused(dep));
        if (!inUse()) {
            StaticDependentLocation.purgeDeadLocations(this);
            enqueueChild(dep);
        }
        else
            getInflated().addDependency(dep);
    }

    protected void removeChild(LocationDependency dep) {
        assert(!Linkables.isUnused(dep));
        if (!inUse())
            dequeueChild(dep);
        else
            getInflated().removeDependency(dep);
    }

    public void addDependentLocation(WeakLocation weakLocation) {
        addChild(weakLocation);
    }

    public void iterateChildren(DependencyIterator<? extends LocationDependency> closure) {
        if (hasChildren(((DependencyIterator<? extends LocationDependency>) closure).kind))
            Linkables.iterate(children, closure);
    }

    public void addChangeListener(ChangeListener listener) {
        addChild(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        removeChild(listener);
    }

    public void addDependency(Location... dependencies) {
        if (dependencies.length > 0) {
            for (Location dep : dependencies)
                addDependency(dep);
        }
    }

    public void addDependency(Location location) {
        if (location != null)
            location.addDependentLocation(new StaticDependentLocation(this));
    }

    public void addDynamicDependency(Location location) {
        WeakMeHolder weakMeHolder = (WeakMeHolder) findChildByKind(CHILD_KIND_WEAK_ME_HOLDER);
        if (weakMeHolder == null) {
            weakMeHolder = new WeakMeHolder(this);
            enqueueChild(weakMeHolder);
        }
        // Good time to clear dead dependencies
        if (location instanceof AbstractLocation)
            ((AbstractLocation) location).purgeDeadDependencies();
        location.addDependentLocation(new DynamicDependentLocation(weakMeHolder.weakMe));
    }

    public void clearDynamicDependencies() {
        WeakMeHolder weakMeHolder = (WeakMeHolder) findChildByKind(CHILD_KIND_WEAK_ME_HOLDER);
        if (weakMeHolder != null) {
            weakMeHolder.weakMe.clear();
            // Hint to poll at reference queue
            StaticDependentLocation.purgeDeadLocations(null);
            dequeueChild(weakMeHolder);
        }
    }

    public <T extends Location> T addDynamicDependent(T dep) {
        addDynamicDependency(dep);
        return dep;
    }

    public <T extends Location> T addStaticDependent(T dep) {
        addDependency(dep);
        return dep;
    }

    public void update() {
    }

    public boolean isViewLocation() {
        return hasChildren(CHILD_KIND_VIEW_LOCATION);
    }

    public Location getUnderlyingLocation() {
        if (isViewLocation()) {
            ViewLocationHolder vlh = (ViewLocationHolder) findChildByKind(CHILD_KIND_VIEW_LOCATION);
            return vlh.underlyingLocation;
        }
        else
            return this;
    }

    protected void setUnderlyingLocation(Location loc) {
        if (isViewLocation()) {
            ViewLocationHolder vlh = (ViewLocationHolder) findChildByKind(CHILD_KIND_VIEW_LOCATION);
            vlh.underlyingLocation = loc;
        }
        else {
            ViewLocationHolder vlh = new ViewLocationHolder(loc);
            enqueueChild(vlh);
        }
    }

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int getListenerCount() {
        return countChildren(CHILD_KIND_WEAK_LOCATION | CHILD_KIND_CHANGE_LISTENER | CHILD_KIND_TRIGGER);
    }

    // For testing -- returns count of listeners plus dependent locations -- the "number of things depending on us"
    int purgeAndGetListenerCount() {
        purgeDeadDependencies();
        return getListenerCount();
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
        public List<LocationDependency> toAdd;
        public List<LocationDependency> toRemove;
        public boolean purge;

        public IterationData(int iterationDepth) {
            this.iterationDepth = iterationDepth;
        }

        public void addDependency(LocationDependency loc) {
            if (toAdd == null)
                toAdd = new LinkedList<LocationDependency>();
            toAdd.add(loc);
        }

        public void removeDependency(LocationDependency loc) {
            if (toRemove == null)
                toRemove = new LinkedList<LocationDependency>();
            toRemove.add(loc);
        }

        private void requestPurge() {
            purge = true;
        }

        public void apply(AbstractLocation target) {
            if (purge)
                target.iterateChildren(PURGE_LISTENER_CLOSURE);
            if (toAdd != null && toAdd.size() > 0) {
                StaticDependentLocation.purgeDeadLocations(target);
                // @@@ Ugh, O(n^2)
                for (LocationDependency loc : toAdd)
                    target.enqueueChild(loc);
            }
            if (toRemove != null && toRemove.size() > 0) {
                for (LocationDependency loc : toRemove)
                    target.dequeueChild(loc);
            }
        }
    }

    private static class WeakMeHolder extends AbstractLocationDependency {
        final WeakReference<Location> weakMe;

        WeakMeHolder(Location loc) {
            this.weakMe = StaticDependentLocation.makeWeakReference(loc);
        }

        public int getDependencyKind() {
            return CHILD_KIND_WEAK_ME_HOLDER;
        }
    }

    private static class ViewLocationHolder extends AbstractLocationDependency {
        Location underlyingLocation;

        private ViewLocationHolder(Location underlyingLocation) {
            this.underlyingLocation = underlyingLocation;
        }

        public int getDependencyKind() {
            return CHILD_KIND_VIEW_LOCATION;
        }
    }
}

abstract class AbstractLocationDependency implements LocationDependency {
    Linkable<LocationDependency> prev;
    LocationDependency next;

    public LocationDependency getNext() {
        return next;
    }

    public Linkable<LocationDependency> getPrev() {
        return prev;
    }

    public void setNext(LocationDependency next) {
        this.next = next;
    }

    public void setPrev(Linkable<LocationDependency> prev) {
        this.prev = prev;
    }
}

abstract class DependencyIterator<T extends LocationDependency> implements Linkable.IterationClosure<LocationDependency> {
    final int kind;

    public DependencyIterator(int kind) {
        this.kind = kind;
    }

    public abstract void onAction(T element);

    @SuppressWarnings("unchecked")
    public void action(LocationDependency element) {
        if (element.getDependencyKind() == kind)
            onAction((T) element);
    }
}

