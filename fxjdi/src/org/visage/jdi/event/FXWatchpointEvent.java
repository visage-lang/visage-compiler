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

package org.visage.jdi.event;

import org.visage.jdi.FXField;
import org.visage.jdi.FXObjectReference;
import org.visage.jdi.FXValue;
import org.visage.jdi.FXVirtualMachine;
import org.visage.jdi.FXWrapper;
import com.sun.jdi.event.WatchpointEvent;

/**
 *
 * @author sundar
 */
public class FXWatchpointEvent extends FXLocatableEvent implements WatchpointEvent {
    public FXWatchpointEvent(FXVirtualMachine fxvm, WatchpointEvent underlying) {
        super(fxvm, underlying);
    }

    public FXField field() {
        return FXWrapper.wrap(virtualMachine(), underlying().field());
    }

    public FXObjectReference object() {
        return FXWrapper.wrap(virtualMachine(), underlying().object());
    }

    public FXValue valueCurrent() {
        return FXWrapper.wrap(virtualMachine(), underlying().valueCurrent());
    }
    
    @Override
    protected WatchpointEvent underlying() {
        return (WatchpointEvent) super.underlying();
    }
}
