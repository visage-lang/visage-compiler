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
 * A Location represents any value on which another variable may express a dependency, including binding dependency
 * and change triggers.  The location is assumed to have a value, whose type is determined by the subinterfaces
 * IntLocation, ObjectLocation, SequenceLocation, etc.
 *
 * The value assocated with a location may be valid or invalid.  If the value is invalid, it will be updated either
 * when the update() method is called, or when the value is retrieved.
 *
 * Change listeners can be registered with a location, and are notified whenever the value associated with the location
 * changes.  Locations may be lazy; this means that change listeners will be notified when the value is invalidated,
 * but the new value will not be recomputed until it is asked for.
 *
 * @author Brian Goetz
 */
public interface Location {
    /** Is the value associated with this location currently valid, or would it have to be recomputed? */
    public boolean isValid();

    /** Is the value assicated with this location null? */
    public boolean isNull();

    /** Can the value held by this location be changed by calling its mutative methods?  */
    public boolean isMutable();

    /** Invalidate the value associated with this location, and call all registered change listeners.  If the location
     * is lazy, the value is not immediately recomputed, otherwise it is.
     */
    public void invalidate();

    /** Recompute the current value */
    public void update();

    /** Register a change listener that will be notified whenever this location may have changed.  Locations are allowed
     * to notify change listeners spuriously.
     */
    public void addInvalidationListener(InvalidationListener listener);

    /** Remove a previously registered change listener.
     */
    public void removeInvalidationListener(InvalidationListener listener);

    /** Record a location as depending on this location -- for internal use only!  */
    public void addDependentLocation(WeakLocation location);

    /** Iterate over the dependent objects, including change listeners, dependencies, triggers, etc */
    public void iterateChildren(DependencyIterator<? extends LocationDependency> closure);

    /** Add this location as a dependency of zero or more other Locations */
    public void addDependency(Location... location);

    /** Add this location as a dependency of another Location */
    public void addDependency(Location location);

    /** Add this location as a dynamic dependency of zero or more other Locations */
    public void addDynamicDependency(Location location);

    /** Remove this location as a dynamic dependency of any Location it was previously registered with */
    public void clearDynamicDependencies();

    /** Does this location represent a "view" of another location? */
    public boolean isViewLocation();

    /** Get the location for which this location represents a view */
    public Location getUnderlyingLocation();
}
