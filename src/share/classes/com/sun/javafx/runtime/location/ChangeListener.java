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

import com.sun.javafx.runtime.util.AbstractLinkable;

/**
 * ChangeListeners are notified when a Location's value may have changed.
 *
 * @see Location
 * @author Brian Goetz
 */
public abstract class ChangeListener extends AbstractLinkable<ChangeListener, AbstractLocation> {
    ChangeListener next;
    AbstractLocation host;

    public ChangeListener getNext() {
        return next;
    }

    public void setNext(ChangeListener next) {
        this.next = next;
    }

    public AbstractLocation getHost() {
        return host;
    }

    public void setHost(AbstractLocation host) {
        this.host = host;
    }

    /** Notifies the listener that the contents of the location may have changed.
     *
     * @return a boolean value indicating whether this listener is still valid.  Returning false will cause the
     * listener to be removed from the listener list.  For listeners that do their own weak reference management,
     * they should return false when the relevant weak references have been reported as cleared.
     */
    public abstract boolean onChange();
}
