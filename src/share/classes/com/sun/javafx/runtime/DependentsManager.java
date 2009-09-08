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

import com.sun.javafx.runtime.util.Linkable;
import com.sun.javafx.runtime.util.Linkables;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Manages dependents of a particular FXObject. Each FXOBject has an instance of
 * dependents manager to which it delegates add/remove/notify/listener-count method
 * calls. The dependents manager implementation maintains {varNum, FXObject(dependent)}
 * pairs for the given source FXObject.
 *
 * @author A. Sundararajan
 */
public abstract class DependentsManager {
    public abstract void addDependent(FXObject src, final int varNum, FXObject dep);
    public abstract void removeDependent(FXObject src, final int varNum, FXObject dep);
    public abstract void notifyDependents(FXObject src, final int varNum);
    public abstract int getListenerCount(FXObject src);

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
    public static DependentsManager get(FXObject src) {
        DependentsManager depMgr = src.getDependentsManager$();
        if (depMgr == null) {
            depMgr = new DependentsManagerImpl();
            src.setDependentsManager$(depMgr);
        }
        return depMgr;
    }

    /*
     * Simple dependents manager implementation. This is straightforward
     * implementation that maintains a single list of listeners for a FXObject
     * (listener multiplexing). Also, this implementation create weak references
     * for each dependent FXObject. If you want to optimize, number of weak
     * references, either modify here or create a different DependentsManager
     * implementation class.
     */
    private static class DependentsManagerImpl extends DependentsManager {
        private static final ReferenceQueue<FXObject> refQ = new ReferenceQueue<FXObject>();
        
        /*
         * This method polls the reference queue and deletes dead listeners.
         * As of now, this is called from add/remove/notify calls. If needed,
         * we can call this method from a timer to remove dead listeners promptly.
         */
        private static void clearDeadDependencies() {
            Reference<? extends FXObject> ref;
            while ((ref = refQ.poll()) != null) {
                if (ref instanceof Dependent) {
                    Linkables.<Dependent>remove((Dependent)ref);
                }
            }
        }

        static final class Dependent extends WeakReference<FXObject> implements Linkable<Dependent> {
            private int varNum;
            private Dependent next;
            private Linkable<Dependent> prev;

            Dependent(int varNum, FXObject dep) {
                super(dep, refQ);
                this.varNum = varNum;
            }

            public Dependent getNext() {
                return next;
            }

            public void setNext(Dependent next) {
                this.next = next;
            }

            public Linkable<Dependent> getPrev() {
                return prev;
            }

            public void setPrev(Linkable<Dependent> prev) {
                this.prev = prev;
            }
        }

        // list of all dependents
        private Dependent dependencies;
        // Are we in the middle of iterating listeners for notification?
        // FIXME: do we need this? Is it possible to handle remove within
        // notification loop properly without this?
        private boolean inIteration;

        @Override
        public void addDependent(FXObject src, final int varNum, FXObject dep) {
            Dependent newDep = new Dependent(varNum, dep);
            if (dependencies == null) {
                dependencies = newDep;
            } else {
                // insert at the start
                newDep.setNext(dependencies);
                dependencies.setPrev(newDep);
                dependencies = newDep;
            }
            // tell "src" that there are listeners for "varNum" field
            src.setBindee$(varNum);
            if (!inIteration) clearDeadDependencies();
        }

        @Override
        public void removeDependent(FXObject src, final int varNum, FXObject dep) {
            Dependent next = dependencies;
            while (next != null) {
                if (varNum == next.varNum && dep == next.get()) {
                    // we just clear the dependent object ref. We will purge
                    // link object later. See below.
                    next.clear();
                }
                next = next.getNext();
            }
            if (!inIteration) clearDeadDependencies();
        }

        @Override
        public void notifyDependents(FXObject src, final int varNum) {
            try {
                inIteration = true;
                Dependent next = dependencies;
                while (next != null) {
                    if (varNum == next.varNum) {
                        FXObject dep = next.get();
                        if (dep != null) dep.update$(src, varNum);
                    }
                    next = next.getNext();
                }
            } finally {
                inIteration = false;
                clearDeadDependencies();
            }
        }

        @Override
        public int getListenerCount(FXObject src) {
            int count = 0;
            Dependent next = dependencies;
            while (next != null) {
                // ignore dead listeners for counting purpose
                FXObject dep = next.get();
                if (dep != null) {
                    count++;
                }
                next = next.getNext();
            }
            return count;
        }
    }
}