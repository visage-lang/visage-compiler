/*
 * Copyright 2010 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.javafx.jdi.event;

import com.sun.javafx.jdi.FXVirtualMachine;
import com.sun.jdi.event.EventIterator;

/**
 *
 * @author sundar
 */
public class FXEventIterator implements EventIterator {
    private final FXVirtualMachine fxvm;
    private final EventIterator underlying;

    public FXEventIterator(FXVirtualMachine fxvm, EventIterator underlying) {
        this.fxvm = fxvm;
        this.underlying = underlying;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FXEventIterator) {
            obj = ((FXEventIterator)obj).underlying();
        }
        return underlying().equals(obj);
    }

    @Override
    public int hashCode() {
        return underlying().hashCode();
    }

    @Override
    public String toString() {
        return underlying().toString();
    }

    public FXEvent nextEvent() {
        return FXEvent.wrap(virtualMachine(), underlying().nextEvent());
    }

    public boolean hasNext() {
        return underlying().hasNext();
    }

    public FXEvent next() {
        return FXEvent.wrap(virtualMachine(), underlying().next());
    }

    public void remove() {
        underlying().remove();
    }

    protected FXVirtualMachine virtualMachine() {
        return fxvm;
    }

    protected EventIterator underlying() {
        return underlying;
    }

    public static FXEventIterator wrap(FXVirtualMachine fxvm, EventIterator evtItr) {
        return (evtItr == null)? null : new FXEventIterator(fxvm, evtItr);
    }
}