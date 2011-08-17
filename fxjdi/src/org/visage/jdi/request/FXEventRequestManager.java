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

package org.visage.jdi.request;

import org.visage.jdi.FXMirror;
import org.visage.jdi.FXVirtualMachine;
import org.visage.jdi.FXWrapper;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.ClassUnloadRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.MonitorContendedEnterRequest;
import com.sun.jdi.request.MonitorContendedEnteredRequest;
import com.sun.jdi.request.MonitorWaitRequest;
import com.sun.jdi.request.MonitorWaitedRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;
import com.sun.jdi.request.VMDeathRequest;
import java.util.List;

/**
 *
 * @author sundar
 */
public class FXEventRequestManager extends FXMirror implements EventRequestManager {
    public FXEventRequestManager(FXVirtualMachine fxvm, EventRequestManager underlying) {
        super(fxvm, underlying);
    }

    public List<AccessWatchpointRequest> accessWatchpointRequests() {
        return FXEventRequest.wrapAccessWatchpointRequests(virtualMachine(),
                underlying().accessWatchpointRequests());
    }

    public List<BreakpointRequest> breakpointRequests() {
        return FXEventRequest.wrapBreakpointRequests(virtualMachine(),
                underlying().breakpointRequests());
    }

    public List<ClassPrepareRequest> classPrepareRequests() {
        return FXEventRequest.wrapClassPrepareRequests(virtualMachine(),
                underlying().classPrepareRequests());
    }

    public List<ClassUnloadRequest> classUnloadRequests() {
        return FXEventRequest.wrapClassUnloadRequests(virtualMachine(),
                underlying().classUnloadRequests());
    }

    public FXAccessWatchpointRequest createAccessWatchpointRequest(Field arg0) {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createAccessWatchpointRequest(FXWrapper.unwrap(arg0)));
    }

    public FXBreakpointRequest createBreakpointRequest(Location arg0) {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createBreakpointRequest(FXWrapper.unwrap(arg0)));
    }

    public FXClassPrepareRequest createClassPrepareRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createClassPrepareRequest());
    }

    public FXClassUnloadRequest createClassUnloadRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createClassUnloadRequest());
    }

    public FXExceptionRequest createExceptionRequest(ReferenceType arg0, boolean arg1, boolean arg2) {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createExceptionRequest(FXWrapper.unwrap(arg0), arg1, arg2));
    }

    public FXMethodEntryRequest createMethodEntryRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMethodEntryRequest());
    }

    public FXMethodExitRequest createMethodExitRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMethodExitRequest());
    }

    public FXModificationWatchpointRequest createModificationWatchpointRequest(Field arg0) {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createModificationWatchpointRequest(FXWrapper.unwrap(arg0)));
    }

    public FXMonitorContendedEnterRequest createMonitorContendedEnterRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMonitorContendedEnterRequest());
    }

    public FXMonitorContendedEnteredRequest createMonitorContendedEnteredRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMonitorContendedEnteredRequest());
    }

    public FXMonitorWaitRequest createMonitorWaitRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMonitorWaitRequest());
    }

    public FXMonitorWaitedRequest createMonitorWaitedRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createMonitorWaitedRequest());
    }

    public FXStepRequest createStepRequest(ThreadReference arg0, int arg1, int arg2) {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createStepRequest(FXWrapper.unwrap(arg0), arg1, arg2));
    }

    public FXThreadDeathRequest createThreadDeathRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createThreadDeathRequest());
    }

    public FXThreadStartRequest createThreadStartRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createThreadStartRequest());
    }

    public FXVMDeathRequest createVMDeathRequest() {
        return FXEventRequest.wrap(virtualMachine(),
                underlying().createVMDeathRequest());
    }

    public void deleteAllBreakpoints() {
        underlying().deleteAllBreakpoints();
    }

    public void deleteEventRequest(EventRequest arg0) {
        underlying().deleteEventRequest(FXEventRequest.unwrap(arg0));
    }

    public void deleteEventRequests(List<? extends EventRequest> arg0) {
        underlying().deleteEventRequests(FXEventRequest.unwrapEventRequests(arg0));
    }

    public List<ExceptionRequest> exceptionRequests() {
        return FXEventRequest.wrapExceptionRequests(virtualMachine(),
                underlying().exceptionRequests());
    }

    public List<MethodEntryRequest> methodEntryRequests() {
        return FXEventRequest.wrapMethodEntryRequests(virtualMachine(),
                underlying().methodEntryRequests());
    }

    public List<MethodExitRequest> methodExitRequests() {
        return FXEventRequest.wrapMethodExitRequests(virtualMachine(),
                underlying().methodExitRequests());
    }

    public List<ModificationWatchpointRequest> modificationWatchpointRequests() {
        return FXEventRequest.wrapModificationWatchpointRequests(virtualMachine(),
                underlying().modificationWatchpointRequests());
    }

    public List<MonitorContendedEnterRequest> monitorContendedEnterRequests() {
        return FXEventRequest.wrapMonitorContendedEnterRequests(virtualMachine(),
                underlying().monitorContendedEnterRequests());
    }

    public List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests() {
        return FXEventRequest.wrapMonitorContendedEnteredRequests(virtualMachine(),
                underlying().monitorContendedEnteredRequests());
    }

    public List<MonitorWaitRequest> monitorWaitRequests() {
        return FXEventRequest.wrapMonitorWaitRequests(virtualMachine(),
                underlying().monitorWaitRequests());
    }

    public List<MonitorWaitedRequest> monitorWaitedRequests() {
        return FXEventRequest.wrapMonitorWaitedRequests(virtualMachine(),
                underlying().monitorWaitedRequests());
    }

    public List<StepRequest> stepRequests() {
        return FXEventRequest.wrapStepRequests(virtualMachine(),
                underlying().stepRequests());
    }

    public List<ThreadDeathRequest> threadDeathRequests() {
        return FXEventRequest.wrapThreadDeathRequests(virtualMachine(),
                underlying().threadDeathRequests());
    }

    public List<ThreadStartRequest> threadStartRequests() {
        return FXEventRequest.wrapThreadStartRequests(virtualMachine(),
                underlying().threadStartRequests());
    }

    public List<VMDeathRequest> vmDeathRequests() {
        return FXEventRequest.wrapVMDeathRequests(virtualMachine(),
                underlying().vmDeathRequests());
    }

    @Override
    protected EventRequestManager underlying() {
        return (EventRequestManager) super.underlying();
    }
}