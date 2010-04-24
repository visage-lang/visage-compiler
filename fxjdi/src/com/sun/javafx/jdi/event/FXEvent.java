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

import com.sun.javafx.jdi.FXMirror;
import com.sun.javafx.jdi.FXVirtualMachine;
import com.sun.javafx.jdi.request.FXEventRequest;
import com.sun.jdi.event.AccessWatchpointEvent;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.MonitorContendedEnterEvent;
import com.sun.jdi.event.MonitorContendedEnteredEvent;
import com.sun.jdi.event.MonitorWaitEvent;
import com.sun.jdi.event.MonitorWaitedEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sundar
 */
public class FXEvent extends FXMirror implements Event {
    public FXEvent(FXVirtualMachine fxvm, Event underlying) {
        super(fxvm, underlying);
    }

    public FXEventRequest request() {
        return FXEventRequest.wrap(virtualMachine(), underlying().request());
    }

    @Override
    protected Event underlying() {
        return (Event) super.underlying();
    }

    public static Event unwrap(Event evt) {
        return (evt instanceof FXEvent)? ((FXEvent)evt).underlying() : evt;
    }

    public static FXEvent wrap(FXVirtualMachine fxvm, Event evt) {
        if (evt == null) {
            return null;
        }

        if (evt instanceof AccessWatchpointEvent) {
            return new FXAccessWatchpointEvent(fxvm, (AccessWatchpointEvent)evt);
        } else if (evt instanceof BreakpointEvent) {
            return new FXBreakpointEvent(fxvm, (BreakpointEvent)evt);
        } else if (evt instanceof ClassPrepareEvent) {
            return new FXClassPrepareEvent(fxvm, (ClassPrepareEvent)evt);
        } else if (evt instanceof ClassUnloadEvent) {
            return new FXClassUnloadEvent(fxvm, (ClassUnloadEvent)evt);
        } else if (evt instanceof ExceptionEvent) {
            return new FXExceptionEvent(fxvm, (ExceptionEvent)evt);
        } else if (evt instanceof MethodEntryEvent) {
            return new FXMethodEntryEvent(fxvm, (MethodEntryEvent)evt);
        } else if (evt instanceof MethodExitEvent) {
            return new FXMethodExitEvent(fxvm, (MethodExitEvent)evt);
        } else if (evt instanceof ModificationWatchpointEvent) {
            return new FXModificationWatchpointEvent(fxvm, (ModificationWatchpointEvent)evt);
        } else if (evt instanceof MonitorContendedEnterEvent) {
            return new FXMonitorContendedEnterEvent(fxvm, (MonitorContendedEnterEvent)evt);
        } else if (evt instanceof MonitorContendedEnteredEvent) {
            return new FXMonitorContendedEnteredEvent(fxvm, (MonitorContendedEnteredEvent)evt);
        } else if (evt instanceof MonitorWaitEvent) {
            return new FXMonitorWaitEvent(fxvm, (MonitorWaitEvent)evt);
        } else if (evt instanceof MonitorWaitedEvent) {
            return new FXMonitorWaitedEvent(fxvm, (MonitorWaitedEvent)evt);
        } else if (evt instanceof StepEvent) {
            return new FXStepEvent(fxvm, (StepEvent)evt);
        } else if (evt instanceof ThreadDeathEvent) {
            return new FXThreadDeathEvent(fxvm, (ThreadDeathEvent)evt);
        } else if (evt instanceof ThreadStartEvent) {
            return new FXThreadStartEvent(fxvm, (ThreadStartEvent)evt);
        } else if (evt instanceof VMDeathEvent) {
            return new FXVMDeathEvent(fxvm, (VMDeathEvent)evt);
        } else if (evt instanceof VMDisconnectEvent) {
            return new FXVMDisconnectEvent(fxvm, (VMDisconnectEvent)evt);
        } else if (evt instanceof VMStartEvent) {
            return new FXVMStartEvent(fxvm, (VMStartEvent)evt);
        } else if (evt instanceof WatchpointEvent) {
            return new FXWatchpointEvent(fxvm, (WatchpointEvent)evt);
        } else if (evt instanceof LocatableEvent) {
            return new FXLocatableEvent(fxvm, (LocatableEvent)evt);
        } else {
            return new FXEvent(fxvm, evt);
        }
    }

    public static Collection unwrapEvents(Collection events) {
        if (events == null) {
            return null;
        }
        ArrayList result = new ArrayList();
        for (Object obj : events) {
            result.add((obj instanceof Event)? unwrap((Event)obj) : obj);
        }
        return result;
    }
}