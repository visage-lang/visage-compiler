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

package com.sun.tools.example.debug.tty;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;

/**
 * This is a debug event listener interface supplied to "FXJDB" class.
 *
 * @author sundar
 */
public interface FXJDBListener {
    public void breakpointEvent(BreakpointEvent evt);
    public void classPrepareEvent(ClassPrepareEvent evt);
    public void classUnloadEvent(ClassUnloadEvent evt);
    public void exceptionEvent(ExceptionEvent evt);
    public void fieldWatchEvent(WatchpointEvent evt);
    public void methodEntryEvent(MethodEntryEvent evt);
    public boolean methodExitEvent(MethodExitEvent evt);
    public void receivedEvent(Event evt);
    public void stepEvent(StepEvent evt);
    public void threadDeathEvent(ThreadDeathEvent evt);
    public void threadStartEvent(ThreadStartEvent evt);
    public void vmDeathEvent(VMDeathEvent evt);
    public void vmDisconnectEvent(VMDisconnectEvent evt);
    public void vmInterrupted();
    public void vmStartEvent(VMStartEvent evt);
}
