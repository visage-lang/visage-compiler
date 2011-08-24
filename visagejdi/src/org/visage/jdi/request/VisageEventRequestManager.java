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

import org.visage.jdi.VisageMirror;
import org.visage.jdi.VisageVirtualMachine;
import org.visage.jdi.VisageWrapper;
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
public class VisageEventRequestManager extends VisageMirror implements EventRequestManager {
    public VisageEventRequestManager(VisageVirtualMachine visagevm, EventRequestManager underlying) {
        super(visagevm, underlying);
    }

    public List<AccessWatchpointRequest> accessWatchpointRequests() {
        return VisageEventRequest.wrapAccessWatchpointRequests(virtualMachine(),
                underlying().accessWatchpointRequests());
    }

    public List<BreakpointRequest> breakpointRequests() {
        return VisageEventRequest.wrapBreakpointRequests(virtualMachine(),
                underlying().breakpointRequests());
    }

    public List<ClassPrepareRequest> classPrepareRequests() {
        return VisageEventRequest.wrapClassPrepareRequests(virtualMachine(),
                underlying().classPrepareRequests());
    }

    public List<ClassUnloadRequest> classUnloadRequests() {
        return VisageEventRequest.wrapClassUnloadRequests(virtualMachine(),
                underlying().classUnloadRequests());
    }

    public VisageAccessWatchpointRequest createAccessWatchpointRequest(Field arg0) {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createAccessWatchpointRequest(VisageWrapper.unwrap(arg0)));
    }

    public VisageBreakpointRequest createBreakpointRequest(Location arg0) {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createBreakpointRequest(VisageWrapper.unwrap(arg0)));
    }

    public VisageClassPrepareRequest createClassPrepareRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createClassPrepareRequest());
    }

    public VisageClassUnloadRequest createClassUnloadRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createClassUnloadRequest());
    }

    public VisageExceptionRequest createExceptionRequest(ReferenceType arg0, boolean arg1, boolean arg2) {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createExceptionRequest(VisageWrapper.unwrap(arg0), arg1, arg2));
    }

    public VisageMethodEntryRequest createMethodEntryRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMethodEntryRequest());
    }

    public VisageMethodExitRequest createMethodExitRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMethodExitRequest());
    }

    public VisageModificationWatchpointRequest createModificationWatchpointRequest(Field arg0) {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createModificationWatchpointRequest(VisageWrapper.unwrap(arg0)));
    }

    public VisageMonitorContendedEnterRequest createMonitorContendedEnterRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMonitorContendedEnterRequest());
    }

    public VisageMonitorContendedEnteredRequest createMonitorContendedEnteredRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMonitorContendedEnteredRequest());
    }

    public VisageMonitorWaitRequest createMonitorWaitRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMonitorWaitRequest());
    }

    public VisageMonitorWaitedRequest createMonitorWaitedRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createMonitorWaitedRequest());
    }

    public VisageStepRequest createStepRequest(ThreadReference arg0, int arg1, int arg2) {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createStepRequest(VisageWrapper.unwrap(arg0), arg1, arg2));
    }

    public VisageThreadDeathRequest createThreadDeathRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createThreadDeathRequest());
    }

    public VisageThreadStartRequest createThreadStartRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createThreadStartRequest());
    }

    public VisageVMDeathRequest createVMDeathRequest() {
        return VisageEventRequest.wrap(virtualMachine(),
                underlying().createVMDeathRequest());
    }

    public void deleteAllBreakpoints() {
        underlying().deleteAllBreakpoints();
    }

    public void deleteEventRequest(EventRequest arg0) {
        underlying().deleteEventRequest(VisageEventRequest.unwrap(arg0));
    }

    public void deleteEventRequests(List<? extends EventRequest> arg0) {
        underlying().deleteEventRequests(VisageEventRequest.unwrapEventRequests(arg0));
    }

    public List<ExceptionRequest> exceptionRequests() {
        return VisageEventRequest.wrapExceptionRequests(virtualMachine(),
                underlying().exceptionRequests());
    }

    public List<MethodEntryRequest> methodEntryRequests() {
        return VisageEventRequest.wrapMethodEntryRequests(virtualMachine(),
                underlying().methodEntryRequests());
    }

    public List<MethodExitRequest> methodExitRequests() {
        return VisageEventRequest.wrapMethodExitRequests(virtualMachine(),
                underlying().methodExitRequests());
    }

    public List<ModificationWatchpointRequest> modificationWatchpointRequests() {
        return VisageEventRequest.wrapModificationWatchpointRequests(virtualMachine(),
                underlying().modificationWatchpointRequests());
    }

    public List<MonitorContendedEnterRequest> monitorContendedEnterRequests() {
        return VisageEventRequest.wrapMonitorContendedEnterRequests(virtualMachine(),
                underlying().monitorContendedEnterRequests());
    }

    public List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests() {
        return VisageEventRequest.wrapMonitorContendedEnteredRequests(virtualMachine(),
                underlying().monitorContendedEnteredRequests());
    }

    public List<MonitorWaitRequest> monitorWaitRequests() {
        return VisageEventRequest.wrapMonitorWaitRequests(virtualMachine(),
                underlying().monitorWaitRequests());
    }

    public List<MonitorWaitedRequest> monitorWaitedRequests() {
        return VisageEventRequest.wrapMonitorWaitedRequests(virtualMachine(),
                underlying().monitorWaitedRequests());
    }

    public List<StepRequest> stepRequests() {
        return VisageEventRequest.wrapStepRequests(virtualMachine(),
                underlying().stepRequests());
    }

    public List<ThreadDeathRequest> threadDeathRequests() {
        return VisageEventRequest.wrapThreadDeathRequests(virtualMachine(),
                underlying().threadDeathRequests());
    }

    public List<ThreadStartRequest> threadStartRequests() {
        return VisageEventRequest.wrapThreadStartRequests(virtualMachine(),
                underlying().threadStartRequests());
    }

    public List<VMDeathRequest> vmDeathRequests() {
        return VisageEventRequest.wrapVMDeathRequests(virtualMachine(),
                underlying().vmDeathRequests());
    }

    @Override
    protected EventRequestManager underlying() {
        return (EventRequestManager) super.underlying();
    }
}