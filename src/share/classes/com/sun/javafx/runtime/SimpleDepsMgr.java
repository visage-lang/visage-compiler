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

import com.sun.javafx.runtime.refq.RefQ;
import com.sun.javafx.runtime.refq.WeakRef;
import com.sun.javafx.runtime.util.Linkable;
import com.sun.javafx.runtime.util.Linkables;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;


/*
 * Simple dependents manager implementation. This is straightforward
 * implementation that maintains a single list of listeners for a FXObject
 * (listener multiplexing). Also, this implementation create weak references
 * of FXObject for each dependent {FXObject, varNum } pair.
 *
 * @author A. Sundararajan
 */
class SimpleDepsMgr extends DependentsManager implements Linkable<Dependent> {
    // list of all dependents
    private Dependent dependencies;

    public Dependent getNext() {
        return dependencies;
    }

    public void setNext(Dependent next) {
        dependencies = next;
    }

    public Linkable<Dependent> getPrev() {
        throw new UnsupportedOperationException();
    }

    public void setPrev(Linkable<Dependent> prev) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDependent(FXObject bindee, final int varNum, FXObject binder, final int depNum) {
        Dependent newDep = new Dependent(varNum, binder, depNum);
        // insert at the start
        newDep.setPrev(this);
        newDep.setNext(dependencies);
        if (dependencies != null) {
            dependencies.setPrev(newDep);
        }
        dependencies = newDep;
        Dependent.clearDeadDependencies();
    }

    @Override
    public void removeDependent(FXObject bindee, final int varNum, FXObject binder) {
        for (Dependent dep = dependencies; dep != null;) {
            if (varNum == dep.varNum && binder == dep.get()) {
                // we just clear the dependent object ref. We will purge
                // link object later.
                dep.clear();
                if (! Dependent.inIteration) {
                    Linkables.remove(dep);
                }
                return;
            }
            dep = dep.getNext();
        }
    }

    public void notifyDependents(FXObject bindee, final int varNum, int startPos, int endPos, int newLength, final int phase) {
        // TODO - handle phase.
        boolean oldInIteration = Dependent.inIteration;
        try {
            Dependent.inIteration = true;
            for (Dependent dep = dependencies; dep != null;) {
                Dependent next = dep.getNext();
                FXObject binder = dep.get();
                if (binder != null) {
                    if (varNum == dep.varNum) {
                        boolean handled = true;
                        try {
                            handled = binder.update$(bindee, dep.depNum, startPos, endPos, newLength, phase);
                        } catch (RuntimeException re) {
                            ErrorHandler.bindException(re);
                        }
                        if (!handled) {
                             Linkables.remove(dep);
                        }
                    }
                } else {
                    Linkables.remove(dep);
                }
                dep = next;
            }
        } finally {
            Dependent.inIteration = oldInIteration;
        }
    }

    @Override
    public int getListenerCount(FXObject obj) {
        int count = 0;
        for (Dependent dep = dependencies; dep != null;) {
            // ignore dead listeners for counting purpose
            FXObject binder = dep.get();
            if (binder != null) {
                count++;
            }
            dep = dep.getNext();
        }
        return count;
    }

    @Override
    public List<FXObject> getDependents(FXObject obj) {
        List<FXObject> res = new ArrayList<FXObject>();
        for (Dependent dep = dependencies; dep != null;) {
            // ignore dead listeners
            FXObject binder = dep.get();
            if (binder != null) {
                res.add(binder);
            }
            dep = dep.getNext();
        }
        return res;
    }
}
final class Dependent extends WeakRef<FXObject> implements Linkable<Dependent> {
    // Are we in the middle of iterating listeners for notification?
    static volatile boolean inIteration;

    /*
     * This method polls the reference queue and deletes dead listeners.
     * As of now, this is called from add/remove/notify calls. If needed,
     * we can call this method from a timer to remove dead listeners promptly.
     */
    static void clearDeadDependencies() {
        if (Dependent.inIteration) {
            return;
        }
        Reference<? extends FXObject> ref;
        while ((ref = refQ.poll()) != null) {
            if (ref instanceof Dependent) {
                Linkables.<Dependent>remove((Dependent) ref);
            }
        }
    }
    private static final RefQ<FXObject> refQ = new RefQ<FXObject>();
    int varNum;
    int depNum;
    private Dependent next;
    private Linkable<Dependent> prev;

    Dependent(int varNum, FXObject dep, int depNum) {
        super(dep, refQ);
        this.varNum = varNum;
        this.depNum = depNum;
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
