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
    public VisageEventRequest(VisageVirtualMachine visagevm, EventRequest underlying) {
        super(visagevm, underlying);
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

    public static VisageEventRequest wrap(VisageVirtualMachine visagevm, EventRequest req) {
        if (req == null) {
            return null;
        }

        if (req instanceof AccessWatchpointRequest) {
            return new VisageAccessWatchpointRequest(visagevm, (AccessWatchpointRequest)req);
        } else if (req instanceof BreakpointRequest) {
            return new VisageBreakpointRequest(visagevm, (BreakpointRequest)req);
        } else if (req instanceof ClassPrepareRequest) {
            return new VisageClassPrepareRequest(visagevm, (ClassPrepareRequest)req);
        } else if (req instanceof ClassUnloadRequest) {
            return new VisageClassUnloadRequest(visagevm, (ClassUnloadRequest)req);
        } else if (req instanceof ExceptionRequest) {
            return new VisageExceptionRequest(visagevm, (ExceptionRequest)req);
        } else if (req instanceof MethodEntryRequest) {
            return new VisageMethodEntryRequest(visagevm, (MethodEntryRequest)req);
        } else if (req instanceof MethodExitRequest) {
            return new VisageMethodExitRequest(visagevm, (MethodExitRequest)req);
        } else if (req instanceof ModificationWatchpointRequest) {
            return new VisageModificationWatchpointRequest(visagevm, (ModificationWatchpointRequest)req);
        } else if (req instanceof MonitorContendedEnterRequest) {
            return new VisageMonitorContendedEnterRequest(visagevm, (MonitorContendedEnterRequest)req);
        } else if (req instanceof MonitorContendedEnteredRequest) {
            return new VisageMonitorContendedEnteredRequest(visagevm, (MonitorContendedEnteredRequest)req);
        } else if (req instanceof MonitorWaitRequest) {
            return new VisageMonitorWaitRequest(visagevm, (MonitorWaitRequest)req);
        } else if (req instanceof MonitorWaitedRequest) {
            return new VisageMonitorWaitedRequest(visagevm, (MonitorWaitedRequest)req);
        } else if (req instanceof StepRequest) {
            return new VisageStepRequest(visagevm, (StepRequest)req);
        } else if (req instanceof ThreadDeathRequest) {
            return new VisageThreadDeathRequest(visagevm, (ThreadDeathRequest)req);
        } else if (req instanceof ThreadStartRequest) {
            return new VisageThreadStartRequest(visagevm, (ThreadStartRequest)req);
        } else if (req instanceof VMDeathRequest) {
            return new VisageVMDeathRequest(visagevm, (VMDeathRequest)req);
        } else if (req instanceof WatchpointRequest) {
            return new VisageWatchpointRequest(visagevm, (WatchpointRequest)req);
        } else {
            return new VisageEventRequest(visagevm, req);
        }
    }
    
    public static VisageAccessWatchpointRequest wrap(
            VisageVirtualMachine visagevm, AccessWatchpointRequest req) {
        return (req == null)? null : new VisageAccessWatchpointRequest(visagevm, req);
    }

    public static VisageBreakpointRequest wrap(
            VisageVirtualMachine visagevm, BreakpointRequest req) {
        return (req == null)? null : new VisageBreakpointRequest(visagevm, req);
    }

    public static VisageClassPrepareRequest wrap(
            VisageVirtualMachine visagevm, ClassPrepareRequest req) {
        return (req == null)? null : new VisageClassPrepareRequest(visagevm, req);
    }

    public static VisageClassUnloadRequest wrap(
            VisageVirtualMachine visagevm, ClassUnloadRequest req) {
        return (req == null)? null : new VisageClassUnloadRequest(visagevm, req);
    }

    public static VisageExceptionRequest wrap(
            VisageVirtualMachine visagevm, ExceptionRequest req) {
        return (req == null)? null : new VisageExceptionRequest(visagevm, req);
    }

    public static VisageMethodEntryRequest wrap(
            VisageVirtualMachine visagevm, MethodEntryRequest req) {
        return (req == null)? null : new VisageMethodEntryRequest(visagevm, req);
    }

    public static VisageMethodExitRequest wrap(
            VisageVirtualMachine visagevm, MethodExitRequest req) {
        return (req == null)? null : new VisageMethodExitRequest(visagevm, req);
    }

    public static VisageModificationWatchpointRequest wrap(
            VisageVirtualMachine visagevm, ModificationWatchpointRequest req) {
        return (req == null)? null : new VisageModificationWatchpointRequest(visagevm, req);
    }

    public static VisageMonitorContendedEnterRequest wrap(
            VisageVirtualMachine visagevm, MonitorContendedEnterRequest req) {
        return (req == null)? null : new VisageMonitorContendedEnterRequest(visagevm, req);
    }

    public static VisageMonitorContendedEnteredRequest wrap(
            VisageVirtualMachine visagevm, MonitorContendedEnteredRequest req) {
        return (req == null)? null : new VisageMonitorContendedEnteredRequest(visagevm, req);
    }

    public static VisageMonitorWaitRequest wrap(
            VisageVirtualMachine visagevm, MonitorWaitRequest req) {
        return (req == null)? null : new VisageMonitorWaitRequest(visagevm, req);
    }

    public static VisageMonitorWaitedRequest wrap(
            VisageVirtualMachine visagevm, MonitorWaitedRequest req) {
        return (req == null)? null : new VisageMonitorWaitedRequest(visagevm, req);
    }

    public static VisageStepRequest wrap(
            VisageVirtualMachine visagevm, StepRequest req) {
        return (req == null)? null : new VisageStepRequest(visagevm, req);
    }

    public static VisageThreadDeathRequest wrap(
            VisageVirtualMachine visagevm, ThreadDeathRequest req) {
        return (req == null)? null : new VisageThreadDeathRequest(visagevm, req);
    }

    public static VisageThreadStartRequest wrap(
            VisageVirtualMachine visagevm, ThreadStartRequest req) {
        return (req == null)? null : new VisageThreadStartRequest(visagevm, req);
    }

    public static VisageVMDeathRequest wrap(VisageVirtualMachine visagevm, VMDeathRequest req) {
        return (req == null)? null : new VisageVMDeathRequest(visagevm, req);
    }

    public static List<AccessWatchpointRequest> wrapAccessWatchpointRequests(
            VisageVirtualMachine visagevm,  List<AccessWatchpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
         List<AccessWatchpointRequest> result = new ArrayList<AccessWatchpointRequest>();
         for (AccessWatchpointRequest req : reqs) {
             result.add(wrap(visagevm, req));
         }
         return result;
    }

    public static List<BreakpointRequest> wrapBreakpointRequests(
            VisageVirtualMachine visagevm,  List<BreakpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<BreakpointRequest> result = new ArrayList<BreakpointRequest>();
        for (BreakpointRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ClassPrepareRequest> wrapClassPrepareRequests(
            VisageVirtualMachine visagevm,  List<ClassPrepareRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ClassPrepareRequest> result = new ArrayList<ClassPrepareRequest>();
        for (ClassPrepareRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ClassUnloadRequest> wrapClassUnloadRequests(
            VisageVirtualMachine visagevm, List<ClassUnloadRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ClassUnloadRequest> result = new ArrayList<ClassUnloadRequest>();
        for (ClassUnloadRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ExceptionRequest> wrapExceptionRequests(
            VisageVirtualMachine visagevm, List<ExceptionRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ExceptionRequest> result = new ArrayList<ExceptionRequest>();
        for (ExceptionRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MethodEntryRequest> wrapMethodEntryRequests(
            VisageVirtualMachine visagevm, List<MethodEntryRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MethodEntryRequest> result = new ArrayList<MethodEntryRequest>();
        for (MethodEntryRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MethodExitRequest> wrapMethodExitRequests(
            VisageVirtualMachine visagevm, List<MethodExitRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MethodExitRequest> result = new ArrayList<MethodExitRequest>();
        for (MethodExitRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ModificationWatchpointRequest> wrapModificationWatchpointRequests(
            VisageVirtualMachine visagevm, List<ModificationWatchpointRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ModificationWatchpointRequest> result = new ArrayList<ModificationWatchpointRequest>();
        for (ModificationWatchpointRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MonitorContendedEnterRequest> wrapMonitorContendedEnterRequests(
            VisageVirtualMachine visagevm, List<MonitorContendedEnterRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorContendedEnterRequest> result = new ArrayList<MonitorContendedEnterRequest>();
        for (MonitorContendedEnterRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MonitorContendedEnteredRequest> wrapMonitorContendedEnteredRequests(
            VisageVirtualMachine visagevm, List<MonitorContendedEnteredRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorContendedEnteredRequest> result = new ArrayList<MonitorContendedEnteredRequest>();
        for (MonitorContendedEnteredRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MonitorWaitRequest> wrapMonitorWaitRequests(
            VisageVirtualMachine visagevm, List<MonitorWaitRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorWaitRequest> result = new ArrayList<MonitorWaitRequest>();
        for (MonitorWaitRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<MonitorWaitedRequest> wrapMonitorWaitedRequests(
            VisageVirtualMachine visagevm, List<MonitorWaitedRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<MonitorWaitedRequest> result = new ArrayList<MonitorWaitedRequest>();
        for (MonitorWaitedRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<StepRequest> wrapStepRequests(
            VisageVirtualMachine visagevm, List<StepRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<StepRequest> result = new ArrayList<StepRequest>();
        for (StepRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ThreadDeathRequest> wrapThreadDeathRequests(
            VisageVirtualMachine visagevm, List<ThreadDeathRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ThreadDeathRequest> result = new ArrayList<ThreadDeathRequest>();
        for (ThreadDeathRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<ThreadStartRequest> wrapThreadStartRequests(
            VisageVirtualMachine visagevm, List<ThreadStartRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<ThreadStartRequest> result = new ArrayList<ThreadStartRequest>();
        for (ThreadStartRequest req : reqs) {
            result.add(wrap(visagevm, req));
        }
        return result;
    }

    public static List<VMDeathRequest> wrapVMDeathRequests(
            VisageVirtualMachine visagevm, List<VMDeathRequest> reqs) {
        if (reqs == null) {
            return null;
        }
        List<VMDeathRequest> result = new ArrayList<VMDeathRequest>();
        for (VMDeathRequest req : reqs) {
            result.add(wrap(visagevm, req));
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