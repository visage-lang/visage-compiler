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

package com.sun.visage.jdi;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidStackFrameException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.MonitorInfo;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import java.util.ArrayList;
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
        underlying().forceEarlyReturn(FXWrapper.unwrap(value));
    }

    public List<MonitorInfo> ownedMonitorsAndFrames() throws IncompatibleThreadStateException {
        return FXWrapper.wrapMonitorInfos(virtualMachine(), underlying().ownedMonitorsAndFrames());
    }

    public FXObjectReference currentContendedMonitor() throws IncompatibleThreadStateException {
        return FXWrapper.wrap(virtualMachine(), underlying().currentContendedMonitor());
    }

    public FXStackFrame frame(int index) throws IncompatibleThreadStateException {
        List<StackFrame> frames = getFilteredFrames();
        if (index < 0 || index >= frames.size()) {
            throw new IndexOutOfBoundsException();
        }
        return (FXStackFrame) frames.get(index);
    }

    public int frameCount() throws IncompatibleThreadStateException {
        return getFilteredFrames().size();
    }

    public List<StackFrame> frames() throws IncompatibleThreadStateException {
        return getFilteredFrames();
    }

    public List<StackFrame> frames(int start, int length) throws IncompatibleThreadStateException {
        List<StackFrame> frames = getFilteredFrames();
        int frameCount = frames.size();
        if (start < 0 || start >= frameCount || length < 0 || (start+length) > frameCount) {
            throw new IndexOutOfBoundsException();
        }
        List<StackFrame> result = new ArrayList<StackFrame>();
        for (int count = start; count < (start+length); count++) {
            result.add(frames.get(count));
        }
        return result;
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

    private List<StackFrame> getFilteredFrames() throws IncompatibleThreadStateException {
        List<StackFrame> frames = FXWrapper.wrapFrames(virtualMachine(), underlying().frames());
        List<StackFrame> filteredFrames = new ArrayList<StackFrame>(frames.size());
        try {
            for (StackFrame fr : frames) {
                FXStackFrame fxfr = (FXStackFrame) fr;
                // don't add JavaFX synthetic frames
                if (fxfr.location().method().isJavaFXInternalMethod()) {
                    continue;
                } else {
                    filteredFrames.add(fxfr);
                }
            }
        } catch (InvalidStackFrameException exp) {
            throw new IncompatibleThreadStateException(exp.getMessage());
        }
        return filteredFrames;
    }    
}
