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
import java.lang.ref.Reference;

public class WeakBinderRef extends WeakRef<FXObject> {
    private static RefQ<FXObject> refQ = new RefQ<FXObject>();
    /** Chain of Dep instances whose binderRef point back here. */
    Dep bindees;
    /** Increment this to disable checkForCleanups.
     * (I don't know if/when that is needed ...) */
    static volatile int unsafeToCleanup;

    public static WeakBinderRef instance(FXObject bindee) {
        WeakBinderRef bref = bindee.getThisRef$internal$();
        if (bref == null) {
            bref = new WeakBinderRef(bindee);
            bindee.setThisRef$internal$(bref);
        }
        return bref;
    }

    static void checkForCleanups() {
        if (unsafeToCleanup > 0) {
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
