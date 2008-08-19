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
import java.util.*;

/**
 * Base class for lower-footprint Location implementations that have support only for dependencies, not triggers
 *
 * @author Brian Goetz
 */
public abstract class AbstractSimpleVariable implements Location {
    protected List<WeakReference<Location>> dependencies;

    protected void notifyDependencies() {
        if (dependencies != null) {
            for (Iterator<WeakReference<Location>> iterator = dependencies.iterator(); iterator.hasNext();) {
                WeakReference<Location> lr = iterator.next();
                Location loc = lr.get();
                if (loc == null)
                    iterator.remove();
                else
                    loc.invalidate();
            }
        }
    }

    public boolean isValid() {
        return true;
    }

    public boolean isMutable() {
        return true;
    }

    public boolean isNull() {
        return false;
    }

    public void addDependentLocation(final WeakReference<Location> location) {
        if (dependencies == null)
            dependencies = new ArrayList<WeakReference<Location>>();
        dependencies.add(location);
    }

    public void invalidate() {
        throw new UnsupportedOperationException();
    }

    public void update() {
        throw new UnsupportedOperationException();
    }

    public void addChangeListener(ChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removeChangeListener(ChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addWeakListener(ChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public Collection<ChangeListener> getListeners() {
        return Collections.emptyList();
    }

    public void addDependencies(Location... location) {
        throw new UnsupportedOperationException();
    }

    public void addDynamicDependency(Location location) {
        throw new UnsupportedOperationException();
    }

    public void clearDynamicDependencies() {
        throw new UnsupportedOperationException();
    }
}
