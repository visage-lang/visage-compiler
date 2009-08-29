/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
 * This invalidation listener maintains a weak location and calls invalidate()
 * on it when  receiving onChange(). This class extends StaticDependentLocation
 * purely for implementation. getDependencyKind is overridden to make sure to
 * return correct dependency kind.
 *
 * @author A. Sundararajan
 */
public class InvalidateMeListener extends StaticDependentLocation implements InvalidationListener {

    public InvalidateMeListener(Location loc) {
        super(loc);
    }

    @Override
    public int getDependencyKind() {
        return AbstractLocation.CHILD_KIND_INVALIDATION_LISTENER;
    }

    //@Override
    public boolean onChange() {
        Location loc = get();
        if (loc != null) {
            loc.invalidate();
            return true;
        } else {
            return false;
        }
    }
}