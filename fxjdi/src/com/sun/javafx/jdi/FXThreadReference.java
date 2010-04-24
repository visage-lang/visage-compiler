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

package com.sun.javafx.jdi;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.MonitorInfo;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import java.util.List;

/**
 *
 * @author sundar
 */
public class FXThreadReference extends FXObjectReference implements ThreadReference {
    public FXThreadReference(FXVirtualMachine fxvm, ThreadReference underlying) {
        super(fxvm, underlying);
    }

    public void forceEarlyReturn(Value value) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException {
        underlying().forceEarlyReturn(value);
    }

    public List<MonitorInfo> ownedMonitorsAndFrames() throws IncompatibleThreadStateException {
        return FXWrapper.wrapMonitorInfos(virtualMachine(), underlying().ownedMonitorsAndFrames());
    }

    public FXObjectReference currentContendedMonitor() throws IncompatibleThreadStateException {
        return FXWrapper.wrap(virtualMachine(), underlying().currentContendedMonitor());
    }

    public FXStackFrame frame(int index) throws IncompatibleThreadStateException {
        return FXWrapper.wrap(virtualMachine(), underlying().frame(index));
    }

    public int frameCount() throws IncompatibleThreadStateException {
        return underlying().frameCount();
    }

    public List<StackFrame> frames() throws IncompatibleThreadStateException {
        return FXWrapper.wrapFrames(virtualMachine(), underlying().frames());
    }

    public List<StackFrame> frames(int start, int length) throws IncompatibleThreadStateException {
        return FXWrapper.wrapFrames(virtualMachine(), underlying().frames(start, length));
    }

    public void interrupt() {
        underlying().interrupt();
    }

    public boolean isAtBreakpoint() {
        return underlying().isAtBreakpoint();
    }

    public boolean isSuspended() {
        return underlying().isSuspended();
    }

    public String name() {
        return underlying().name();
    }

    public List<ObjectReference> ownedMonitors() throws IncompatibleThreadStateException {
        return FXWrapper.wrapObjectReferences(virtualMachine(), underlying().ownedMonitors());
    }

    public void popFrames(StackFrame frame) throws IncompatibleThreadStateException {
        underlying().popFrames(FXWrapper.unwrap(frame));
    }

    public void resume() {
        underlying().resume();
    }

    public int status() {
        return underlying().status();
    }

    public void stop(ObjectReference exception) throws InvalidTypeException {
        underlying().stop(FXWrapper.unwrap(exception));
    }

    public void suspend() {
        underlying().suspend();
    }

    public int suspendCount() {
        return underlying().suspendCount();
    }

    public FXThreadGroupReference threadGroup() {
        return FXWrapper.wrap(virtualMachine(), underlying().threadGroup());
    }

    @Override
    protected ThreadReference underlying() {
        return (ThreadReference) super.underlying();
    }
}
