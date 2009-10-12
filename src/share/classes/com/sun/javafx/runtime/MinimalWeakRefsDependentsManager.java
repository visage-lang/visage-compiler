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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


/**
 * A DepenendentsManager implementation that minimizes number of WeakReference
 * objects created.
 *
 * @author Per Bother
 * @author A. Sundararajan (integrated with DependentsManager interface)
 */
class MinimalWeakRefsDependentsManager extends DependentsManager implements BinderLinkable {
    private WeakBinderRef thisRef;
    Dep dependencies;

    WeakBinderRef getThisRef(FXObject self) {
       if (thisRef == null) {
           thisRef = new WeakBinderRef(self);
       }
       return thisRef;
    }

    public void setNextBinder(Dep next) {
        dependencies = next;
    }

    public void addDependent(FXObject bindee, final int varNum, FXObject binder) {
        Dep dep = Dep.newDependency(binder);
        dep.linkToBindee(bindee, varNum);
        // FIXME: revisit this - is this a good time to call cleanup?
        WeakBinderRef.checkForCleanups();
    }

    public void removeDependent(FXObject bindee, final int varNum, FXObject binder) {
        for (Dep dep = dependencies; dep != null;) {
            WeakBinderRef binderRef = dep.binderRef;
            if (binderRef != null) {
                if (varNum == dep.bindeeVarNum && binder == binderRef.get()) {
                    dep.binderRef = null;
                    if (! Dep.inIteration) {
                        dep.unlinkFromBindee();
                    }
                    return;
                }
            }
            dep = dep.nextInBinders;
        }
    }

    public void notifyDependents(FXObject bindee, final int varNum, final int phase) {
        // TODO - handle phase.
        boolean oldInIteration = Dep.inIteration;
        try {
            Dep.inIteration = true;
            for (Dep dep = dependencies; dep != null;) {
                Dep next = dep.nextInBinders;
                WeakBinderRef binderRef = dep.binderRef;
                if (binderRef != null) {
                    if (varNum == dep.bindeeVarNum) {
                        FXObject binder = binderRef.get();
                        if (binder == null) {
                            dep.binderRef = null;
                            binderRef.cleanup();
                        } else {
                            binder.update$(bindee, varNum, phase);
                        }
                    }
                } else {
                    dep.unlinkFromBindee();
                }
                dep = next;
            }
        } finally {
            Dep.inIteration = oldInIteration;
        }
    }

    public int getListenerCount(FXObject bindee) {
        int count = 0;
        for (Dep dep = dependencies; dep != null;) {
            WeakBinderRef binderRef = dep.binderRef;
            if (binderRef != null) {
                if (binderRef.get() != null) {
                    count++;
                }
            }
            dep = dep.nextInBinders;
        }
        return count;
    }
}

interface BinderLinkable {
    void setNextBinder(Dep next);
};

class WeakBinderRef extends WeakReference<FXObject> {
    private static ReferenceQueue<FXObject> refQ = new ReferenceQueue<FXObject>();
    /** Chain of Dep instances whose binderRef point back here. */
    Dep bindees;
    /** Increment this to disable checkForCleanups.
     * (I don't know if/when that is needed ...) */
    static volatile int unsafeToCleanup;

    static void checkForCleanups() {
        if (unsafeToCleanup > 0 || Dep.inIteration) {
            return;
        }
        Reference<? extends FXObject> ref;
        while ((ref = refQ.poll()) != null) {
            if (ref instanceof WeakBinderRef) {
                ((WeakBinderRef) ref).cleanup();
            }
        }
    }

    WeakBinderRef(FXObject obj) {
        super(obj, refQ);
    }

    void cleanup() {
        for (Dep dep = bindees; dep != null;) {
            Dep next = dep.nextInBindees;
            dep.unlinkFromBindee();
            dep = next;
        }
        bindees = null;
    }
}

class Dep implements BinderLinkable {
    // See comment for the method Dep.unlinkFromBindee().
    static volatile boolean inIteration;
    WeakBinderRef binderRef;
    int bindeeVarNum;
    Dep nextInBinders;

    public void setNextBinder(Dep next) {
        nextInBinders = next;
    }

    /** Back-pointer corresponding to nextInBinders.
     * Either the previous Dep such that
     * {@code ((Dep) prevInBinders).nextInBinders==this},
     * or (if this is the first dep) the FXBase list head such that
     * {@code ((FXBase) prevInBinders).dependencies==this}.
     */
    BinderLinkable prevInBinders;
    Dep nextInBindees;

    static Dep newDependency(FXObject binder) {
        Dep dep = new Dep();
        MinimalWeakRefsDependentsManager binderDepMgr =
                (MinimalWeakRefsDependentsManager) DependentsManager.get(binder);
        WeakBinderRef binderRef = binderDepMgr.getThisRef(binder);
        dep.binderRef = binderRef;
        // Link into bindee chain of binderRef
        Dep firstBindee = binderRef.bindees;
        dep.nextInBindees = firstBindee;
        binderRef.bindees = dep;
        return dep;
    }

    void linkToBindee(FXObject bindee, int bindeeVarNum) {
        this.bindeeVarNum = bindeeVarNum;
        MinimalWeakRefsDependentsManager depMgr =
                (MinimalWeakRefsDependentsManager) DependentsManager.get(bindee);
        // Link into binder chain of bindee
        Dep firstBinder = depMgr.dependencies;
        nextInBinders = firstBinder;
        if (firstBinder != null) {
            firstBinder.prevInBinders = this;
        }
        prevInBinders = depMgr;
        depMgr.dependencies = this;
    }

    /**
     * Unlink from dependency chain of bindee.
     *
     * This can be used for a dynamic dependency we want to re-use.
     * WARNING: This needs some care, since if we call unlinkFromBindee
     * on a Dep that is on a list being processed by notifyDependents$ then we
     * risk confusion, especially if we re-use the Dep for a different bindee.
     *
     * The problem seems restricted to either the current or the next dep
     * (in the notifyDependents loop), depending on whether we get the next
     * pointer before or after we call update. Using an "inIteration" flag seems
     * like a solution that would work.
     */
    void unlinkFromBindee() {
        Dep next = nextInBinders;
        BinderLinkable prevBinder = prevInBinders;
        prevBinder.setNextBinder(next);
        if (next != null) {
            next.prevInBinders = prevBinder;
        }
    }
}