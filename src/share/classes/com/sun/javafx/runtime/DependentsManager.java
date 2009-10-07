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

package com.sun.javafx.runtime;

/**
 * Manages dependents of a particular FXObject. Each FXOBject has an instance of
 * dependents manager to which it delegates add/remove/notify/listener-count method
 * calls. The dependents manager implementation maintains {varNum, FXObject(dependent)}
 * pairs for the given source FXObject.
 *
 * @author A. Sundararajan
 */
public abstract class DependentsManager {
    public abstract void addDependent(FXObject bindee, final int varNum, FXObject binder);
    public abstract void removeDependent(FXObject bindee, final int varNum, FXObject binder);
    public void switchDependence(FXObject binder, final int varNum, FXObject oldBindee, FXObject newBindee) {
        if (oldBindee != null) {
            oldBindee.removeDependent$(varNum, binder);
        }
        if (newBindee != null) {
            newBindee.addDependent$(varNum, binder);
        }
    }
    public abstract void notifyDependents(FXObject bindee, final int varNum, final int phase);
    public abstract int getListenerCount(FXObject bindee);

    /**
     * Returns the dependents manager of a given FXObject. If needed, this method
     * creates a new dependents manager. It is possible to return different
     * dependents manager implementation for different FXObjects. For example,
     * if there are too many fields in an object and only few fields are updated
     * often, we may avoid listener multiplexing done by the simple implementation
     * below. An implementation may keep separate listener lists for frequently
     * updated fields.
     *
     * @param src FXObject for which dependents manager is returned
     */
    public static DependentsManager get(FXObject obj) {
        DependentsManager depMgr = obj.getDependentsManager$internal$();
        if (depMgr == null) {
            depMgr = new MinimalWeakRefsDependentsManager();
            obj.setDependentsManager$internal$(depMgr);
        }
        return depMgr;
    }
}
