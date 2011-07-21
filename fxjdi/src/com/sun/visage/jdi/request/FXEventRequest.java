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

package com.sun.visage.jdi.request;

import com.sun.visage.jdi.FXMirror;
import com.sun.visage.jdi.FXVirtualMachine;
import com.sun.jdi.request.AccessWatchpointRequest;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.ClassUnloadRequest;
import com.sun.jdi.request.EventRequest;
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
import com.sun.jdi.request.WatchpointRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sundar
 */
public class FXEventRequest extends FXMirror implements EventRequest {
    public FXEventRequest(FXVirtualMachine fxvm, EventRequest underlying) {
        super(fxvm, underlying);
    }

    public void addCountFilter(int count) {
        underlying().addCountFilter(count);
    }

    public void disable() {
        underlying().disable();
    }

    public void enable() {
        underlying().enable();
    }

    public Object getProperty(Object arg0) {
        return underlying().getProperty(arg0);
    }

    public boolean isEnabled() {
        return underlying().isEnabled();
    }

    public void putProperty(Object arg0, Object arg1) {
        underlying().putProperty(arg0, arg1);
    }

    public void setEnabled(boolean enabled) {
        underlying().setEnabled(enabled);
    }

    public void setSuspendPolicy(int policy) {
        underlying().setSuspendPolicy(policy);
    }

    public int suspendPolicy() {
        return underlying().suspendPolicy();
    }

    @Override
    protected EventRequest underlying() {
        return (EventRequest) super.underlying();
    }

    // static utils for wrapping/unwrapping event request objects

    public static FXEventRequest wrap(FXVirtualMachine fxvm, EventRequest req) {
        if (req == null) {
            return null;
        }

        if (req instanceof AccessWatchpointRequest) {
            return new FXAccessWatchpointRequest(fxvm, (AccessWatchpointRequest)req);
        } else if (req instanceof BreakpointRequest) {
            return new FXBreakpointRequest(fxvm, (BreakpointRequest)req);
        } else if (req instanceof ClassPrepareRequest) {
            return new FXClassPrepareRequest(fxvm, (ClassPrepareRequest)req);
        } else if (req instanceof ClassUnloadRequest) {
            return new FXClassUnloadRequest(fxvm, (ClassUnloadRequest)req);
        } else if (req instanceof ExceptionRequest) {
            return new FXExceptionRequest(fxvm, (ExceptionRequest)req);
        } else if (req instanceof MethodEntryRequest) {
            return new FXMethodEntryRequest(fxvm, (MethodEntryRequest)req);
        } else if (req instanceof MethodExitRequest) {
            return new FXMethodExitRequest(fxvm, (MethodExitRequest)req);
        } else if (req instanceof ModificationWatchpointRequest) {
            return new FXModificationWatchpointRequest(fxvm, (ModificationWatchpointRequest)req);
        } else if (req instanceof MonitorContendedEnterRequest) {
            return new FXMonitorContendedEnterRequest(fxvm, (MonitorContendedEnterRequest)req);
        } else if (req instanceof MonitorContendedEnteredRequest) {
            return new FXMonitorContendedEnteredRequest(fxvm, (MonitorContendedEnteredRequest)req);
        } else if (req instanceof MonitorWaitRequest) {
            return new FXMonitorWaitRequest(fxvm, (MonitorWaitRequest)req);
        } else if (req instanceof MonitorWaitedRequest) {
            return new FXMonitorWaitedRequest(fxvm, (MonitorWaitedRequest)req);
        } else if (req instanceof StepRequest) {
            return new FXStepRequest(fxvm, (StepRequest)req);
        } else if (req instanceof ThreadDeathRequest) {
            return new FXThreadDeathRequest(fxvm, (ThreadDeathRequest)req);
        } else if (req instanceof ThreadStartRequest) {
            return new FXThreadStartRequest(fxvm, (ThreadStartRequest)req);
        } else if (req instanceof VMDeathRequest) {
            return new FXVMDeathRequest(fxvm, (VMDeathRequest)req);
        } else if (req instanceof WatchpointRequest) {
            return new FXWatchpointRequest(fxvm, (WatchpointRequest)req);
        } else {
            return new FXEventRequest(fxvm, req);
        }
    }
    
    public static FXAccessWatchpointRequest wrap(
            FXVirtualMachine fxvm, AccessWatchpointRequest req) {
        return (req == null)? null : new FXAccessWatchpointRequest(fxvm, req);
    }

    public static FXBreakpointRequest wrap(
            FXVirtualMachine fxvm, BreakpointRequest req) {
        return (req == null)? null : new FXBreakpointRequest(fxvm, req);
    }

    public static FXClassPrepareRequest wrap(
            FXVirtualMachine fxvm, ClassPrepareRequest req) {
        return (req == null)? null : new FXClassPrepareRequest(fxvm, req);
    }

    public static FXClassUnloadRequest wrap(
            FXVirtualMachine fxvm, ClassUnloadRequest req) {
        return (req == null)? null : new FXClassUnloadRequest(fxvm, req);
    }

    public static FXExceptionRequest wrap(
            FXVirtualMachine fxvm, ExceptionRequest req) {
        return (req == null)? null : new FXExceptionRequest(fxvm, req);
    }

    public static FXMethodEntryRequest wrap(
            FXVirtualMachine fxvm, MethodEntryRequest req) {
        return (req == null)? null : new FXMethodEntryRequest(fxvm, req);
    }

    public static FXMethodExitRequest wrap(
            FXVirtualMachine fxvm, MethodExitRequest req) {
        return (req == null)? null : new FXMethodExitRequest(fxvm, req);
    }

    public static FXModificationWatchpointRequest wrap(
            FXVirtualMachine fxvm, ModificationWatchpointRequest req) {
        return (req == null)? null : new FXModificationWatchpointRequest(fxvm, req);
    }

    public static FXMonitorContendedEnterRequest wrap(
            FXVirtualMachine fxvm, MonitorContendedEnterRequest req) {
        return (req == null)? null : new FXMonitorContendedEnterRequest(fxvm, req);
    }

    public static FXMonitorContendedEnteredRequest wrap(
            FXVirtualMachine fxvm, MonitorContendedEnteredRequest req) {
        return (req == null)? null : new FXMonitorContendedEnteredRequest(fxvm, req);
    }

    public static FXMonitorWaitRequest wrap(
            FXVirtualMachine fxvm, MonitorWaitRequest req) {
        return (req == null)? null : new FXMonitorWaitRequest(fxvm, req);
    }

    public static FXMonitorWaitedRequest wrap(
            FXVirtualMachine fxvm, MonitorWaitedRequest req) {
        return (req == null)? null : new FXMonitorWaitedRequest(fxvm, req);
    }

    public static FXStepRequest wrap(
            FXVirtualMachine fxvm, StepRequest req) {
        return (req == null)? null : new FXStepRequest(fxvm, req);
    }

    public static FXThreadDeathRequest wrap(
            FXVirtualMachine fxvm, ThreadDeathRequest req) {
        return (req == null)? null : new FXThreadDeathRequest(fxvm, req);
    }

    public static FXThreadStartRequest wrap(
            FXVirtualMachine fxvm, ThreadStartRequest req) {
        return (req == null)? null : new FXThreadStartRequest(fxvm, req);
    }

    public static FXVMDeathRequest wrap(FXVirtualMachine fxvm, VMDeathRequest req) {
        return (req == null)? null : new FXVMDeathRequest(fxvm, req);
    }

    public static List<AccessWatchpointRequest> wrapAccessWatchpointRequests(
            FXVirtualMachine fxvm,  List<AccessWatchpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
         List<AccessWatchpointRequest> result = new ArrayList<AccessWatchpointRequest>();
         for (AccessWatchpointRequest req : reqs) {
             result.add(wrap(fxvm, req));
         }
         return result;
    }

    public static List<BreakpointRequest> wrapBreakpointRequests(
            FXVirtualMachine fxvm,  List<BreakpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<BreakpointRequest> result = new ArrayList<BreakpointRequest>();
        for (BreakpointRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ClassPrepareRequest> wrapClassPrepareRequests(
            FXVirtualMachine fxvm,  List<ClassPrepareRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ClassPrepareRequest> result = new ArrayList<ClassPrepareRequest>();
        for (ClassPrepareRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ClassUnloadRequest> wrapClassUnloadRequests(
            FXVirtualMachine fxvm, List<ClassUnloadRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ClassUnloadRequest> result = new ArrayList<ClassUnloadRequest>();
        for (ClassUnloadRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ExceptionRequest> wrapExceptionRequests(
            FXVirtualMachine fxvm, List<ExceptionRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ExceptionRequest> result = new ArrayList<ExceptionRequest>();
        for (ExceptionRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MethodEntryRequest> wrapMethodEntryRequests(
            FXVirtualMachine fxvm, List<MethodEntryRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MethodEntryRequest> result = new ArrayList<MethodEntryRequest>();
        for (MethodEntryRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MethodExitRequest> wrapMethodExitRequests(
            FXVirtualMachine fxvm, List<MethodExitRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MethodExitRequest> result = new ArrayList<MethodExitRequest>();
        for (MethodExitRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ModificationWatchpointRequest> wrapModificationWatchpointRequests(
            FXVirtualMachine fxvm, List<ModificationWatchpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ModificationWatchpointRequest> result = new ArrayList<ModificationWatchpointRequest>();
        for (ModificationWatchpointRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MonitorContendedEnterRequest> wrapMonitorContendedEnterRequests(
            FXVirtualMachine fxvm, List<MonitorContendedEnterRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorContendedEnterRequest> result = new ArrayList<MonitorContendedEnterRequest>();
        for (MonitorContendedEnterRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MonitorContendedEnteredRequest> wrapMonitorContendedEnteredRequests(
            FXVirtualMachine fxvm, List<MonitorContendedEnteredRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorContendedEnteredRequest> result = new ArrayList<MonitorContendedEnteredRequest>();
        for (MonitorContendedEnteredRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MonitorWaitRequest> wrapMonitorWaitRequests(
            FXVirtualMachine fxvm, List<MonitorWaitRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorWaitRequest> result = new ArrayList<MonitorWaitRequest>();
        for (MonitorWaitRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<MonitorWaitedRequest> wrapMonitorWaitedRequests(
            FXVirtualMachine fxvm, List<MonitorWaitedRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorWaitedRequest> result = new ArrayList<MonitorWaitedRequest>();
        for (MonitorWaitedRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<StepRequest> wrapStepRequests(
            FXVirtualMachine fxvm, List<StepRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<StepRequest> result = new ArrayList<StepRequest>();
        for (StepRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ThreadDeathRequest> wrapThreadDeathRequests(
            FXVirtualMachine fxvm, List<ThreadDeathRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ThreadDeathRequest> result = new ArrayList<ThreadDeathRequest>();
        for (ThreadDeathRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<ThreadStartRequest> wrapThreadStartRequests(
            FXVirtualMachine fxvm, List<ThreadStartRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ThreadStartRequest> result = new ArrayList<ThreadStartRequest>();
        for (ThreadStartRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }

    public static List<VMDeathRequest> wrapVMDeathRequests(
            FXVirtualMachine fxvm, List<VMDeathRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<VMDeathRequest> result = new ArrayList<VMDeathRequest>();
        for (VMDeathRequest req : reqs) {
            result.add(wrap(fxvm, req));
        }
        return result;
    }
    
    // unwrap methods
    public static EventRequest unwrap(EventRequest req) {
        return (req instanceof FXEventRequest)? ((FXEventRequest)req).underlying() : req;
    }

    public static <T extends EventRequest> List<T> unwrapEventRequests(List<T> requests) {
        List result = new ArrayList();
        for (EventRequest req : requests) {
            result.add(unwrap(req));
        }
        return result;
    }
}