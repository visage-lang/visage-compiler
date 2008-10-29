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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * WeakLink
 *
 * @author Brian Goetz
 */
class WeakLocation extends WeakReference<Location> implements LocationDependency {
    /*[*/ static ReferenceQueue<Location> refQ = new ReferenceQueue<Location>(); /*]*/
    LocationDependency next;
    AbstractLocation host;

    WeakLocation(Location referent) {
        super(referent /*[*/ , refQ /*]*/ );
    }

    public LocationDependency getNext() {
        return next;
    }

    public void setNext(LocationDependency next) {
        this.next = next;
    }

    public AbstractLocation getHost() {
        return host;
    }

    public void setHost(AbstractLocation host) {
        this.host = host;
    }

    public int getDependencyKind() {
        return AbstractLocation.DEPENDENCY_KIND_WEAK_LOCATION;
    }

    static void purgeDeadLocations(AbstractLocation fallback) {
        /*[*/
        Reference<? extends Location> loc;
        AbstractLocation lastHost = null;
        while ((loc = refQ.poll()) != null) {
            WeakLocation wl = (WeakLocation) loc;
            AbstractLocation host = wl.host;
            // Minor optimization -- if we just purged a given host, don't do it again
            if (host != null && host != lastHost) {
                host.purgeDeadDependencies();
                lastHost = host;
            }
        }
        /*]*/

        /* [
            // Fallback strategy is an aggressive purge
            if (fallback != null)
                fallback.purgeDeadDependencies();
        ] */
    }
}
