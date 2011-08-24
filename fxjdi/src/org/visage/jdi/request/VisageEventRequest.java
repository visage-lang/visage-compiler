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
public class VisageEventRequest extends VisageMirror implements EventRequest {
    public VisageEventRequest(VisageVirtualMachine fxvm, EventRequest underlying) {
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

    public static VisageEventRequest wrap(VisageVirtualMachine fxvm, EventRequest req) {
        if (req == null) {
            return null;
        }

        if (req instanceof AccessWatchpointRequest) {
            return new VisageAccessWatchpointRequest(fxvm, (AccessWatchpointRequest)req);
        } else if (req instanceof BreakpointRequest) {
            return new VisageBreakpointRequest(fxvm, (BreakpointRequest)req);
        } else if (req instanceof ClassPrepareRequest) {
            return new VisageClassPrepareRequest(fxvm, (ClassPrepareRequest)req);
        } else if (req instanceof ClassUnloadRequest) {
            return new VisageClassUnloadRequest(fxvm, (ClassUnloadRequest)req);
        } else if (req instanceof ExceptionRequest) {
            return new VisageExceptionRequest(fxvm, (ExceptionRequest)req);
        } else if (req instanceof MethodEntryRequest) {
            return new VisageMethodEntryRequest(fxvm, (MethodEntryRequest)req);
        } else if (req instanceof MethodExitRequest) {
            return new VisageMethodExitRequest(fxvm, (MethodExitRequest)req);
        } else if (req instanceof ModificationWatchpointRequest) {
            return new VisageModificationWatchpointRequest(fxvm, (ModificationWatchpointRequest)req);
        } else if (req instanceof MonitorContendedEnterRequest) {
            return new VisageMonitorContendedEnterRequest(fxvm, (MonitorContendedEnterRequest)req);
        } else if (req instanceof MonitorContendedEnteredRequest) {
            return new VisageMonitorContendedEnteredRequest(fxvm, (MonitorContendedEnteredRequest)req);
        } else if (req instanceof MonitorWaitRequest) {
            return new VisageMonitorWaitRequest(fxvm, (MonitorWaitRequest)req);
        } else if (req instanceof MonitorWaitedRequest) {
            return new VisageMonitorWaitedRequest(fxvm, (MonitorWaitedRequest)req);
        } else if (req instanceof StepRequest) {
            return new VisageStepRequest(fxvm, (StepRequest)req);
        } else if (req instanceof ThreadDeathRequest) {
            return new VisageThreadDeathRequest(fxvm, (ThreadDeathRequest)req);
        } else if (req instanceof ThreadStartRequest) {
            return new VisageThreadStartRequest(fxvm, (ThreadStartRequest)req);
        } else if (req instanceof VMDeathRequest) {
            return new VisageVMDeathRequest(fxvm, (VMDeathRequest)req);
        } else if (req instanceof WatchpointRequest) {
            return new VisageWatchpointRequest(fxvm, (WatchpointRequest)req);
        } else {
            return new VisageEventRequest(fxvm, req);
        }
    }
    
    public static VisageAccessWatchpointRequest wrap(
            VisageVirtualMachine fxvm, AccessWatchpointRequest req) {
        return (req == null)? null : new VisageAccessWatchpointRequest(fxvm, req);
    }

    public static VisageBreakpointRequest wrap(
            VisageVirtualMachine fxvm, BreakpointRequest req) {
        return (req == null)? null : new VisageBreakpointRequest(fxvm, req);
    }

    public static VisageClassPrepareRequest wrap(
            VisageVirtualMachine fxvm, ClassPrepareRequest req) {
        return (req == null)? null : new VisageClassPrepareRequest(fxvm, req);
    }

    public static VisageClassUnloadRequest wrap(
            VisageVirtualMachine fxvm, ClassUnloadRequest req) {
        return (req == null)? null : new VisageClassUnloadRequest(fxvm, req);
    }

    public static VisageExceptionRequest wrap(
            VisageVirtualMachine fxvm, ExceptionRequest req) {
        return (req == null)? null : new VisageExceptionRequest(fxvm, req);
    }

    public static VisageMethodEntryRequest wrap(
            VisageVirtualMachine fxvm, MethodEntryRequest req) {
        return (req == null)? null : new VisageMethodEntryRequest(fxvm, req);
    }

    public static VisageMethodExitRequest wrap(
            VisageVirtualMachine fxvm, MethodExitRequest req) {
        return (req == null)? null : new VisageMethodExitRequest(fxvm, req);
    }

    public static VisageModificationWatchpointRequest wrap(
            VisageVirtualMachine fxvm, ModificationWatchpointRequest req) {
        return (req == null)? null : new VisageModificationWatchpointRequest(fxvm, req);
    }

    public static VisageMonitorContendedEnterRequest wrap(
            VisageVirtualMachine fxvm, MonitorContendedEnterRequest req) {
        return (req == null)? null : new VisageMonitorContendedEnterRequest(fxvm, req);
    }

    public static VisageMonitorContendedEnteredRequest wrap(
            VisageVirtualMachine fxvm, MonitorContendedEnteredRequest req) {
        return (req == null)? null : new VisageMonitorContendedEnteredRequest(fxvm, req);
    }

    public static VisageMonitorWaitRequest wrap(
            VisageVirtualMachine fxvm, MonitorWaitRequest req) {
        return (req == null)? null : new VisageMonitorWaitRequest(fxvm, req);
    }

    public static VisageMonitorWaitedRequest wrap(
            VisageVirtualMachine fxvm, MonitorWaitedRequest req) {
        return (req == null)? null : new VisageMonitorWaitedRequest(fxvm, req);
    }

    public static VisageStepRequest wrap(
            VisageVirtualMachine fxvm, StepRequest req) {
        return (req == null)? null : new VisageStepRequest(fxvm, req);
    }

    public static VisageThreadDeathRequest wrap(
            VisageVirtualMachine fxvm, ThreadDeathRequest req) {
        return (req == null)? null : new VisageThreadDeathRequest(fxvm, req);
    }

    public static VisageThreadStartRequest wrap(
            VisageVirtualMachine fxvm, ThreadStartRequest req) {
        return (req == null)? null : new VisageThreadStartRequest(fxvm, req);
    }

    public static VisageVMDeathRequest wrap(VisageVirtualMachine fxvm, VMDeathRequest req) {
        return (req == null)? null : new VisageVMDeathRequest(fxvm, req);
    }

    public static List<AccessWatchpointRequest> wrapAccessWatchpointRequests(
            VisageVirtualMachine fxvm,  List<AccessWatchpointRequest> reqs) {
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
            VisageVirtualMachine fxvm,  List<BreakpointRequest> reqs) {
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
            VisageVirtualMachine fxvm,  List<ClassPrepareRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<ClassUnloadRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<ExceptionRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MethodEntryRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MethodExitRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<ModificationWatchpointRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MonitorContendedEnterRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MonitorContendedEnteredRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MonitorWaitRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<MonitorWaitedRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<StepRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<ThreadDeathRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<ThreadStartRequest> reqs) {
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
            VisageVirtualMachine fxvm, List<VMDeathRequest> reqs) {
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
        return (req instanceof VisageEventRequest)? ((VisageEventRequest)req).underlying() : req;
    }

    public static <T extends EventRequest> List<T> unwrapEventRequests(List<T> requests) {
        List result = new ArrayList();
        for (EventRequest req : requests) {
            result.add(unwrap(req));
        }
        return result;
    }
}