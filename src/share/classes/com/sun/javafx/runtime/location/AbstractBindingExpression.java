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
 * AbstractBindingExpression
 *
 * @author Brian Goetz
 */
public class AbstractBindingExpression {
    private Location location;

    public void setLocation(Location location) {
        if (this.location != null)
            throw new IllegalStateException("Cannot reuse binding expressions");
        this.location = location;
        Location[] fixedDependents = getStaticDependents();
        if (fixedDependents != null) {
            location.addDependency(fixedDependents);
        }
    }

    /**
     * Override to provide an array of static dependents
     * @return an array of static dependents, or null
     */
    protected Location[] getStaticDependents() {
        return null;
    }

    protected <T extends Location> T addDynamicDependent(T dep) {
        location.addDynamicDependency(dep);
        return dep;
    }

    protected <T extends Location> T addStaticDependent(T dep) {
        location.addDependency(dep);
        return dep;
    }

    protected void clearDynamicDependencies() {
        location.clearDynamicDependencies();
    }
}
