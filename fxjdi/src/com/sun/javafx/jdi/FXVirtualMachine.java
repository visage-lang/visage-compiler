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

import com.sun.javafx.jdi.event.FXEventQueue;
import com.sun.javafx.jdi.request.FXEventRequestManager;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.BooleanType;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteType;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharType;
import com.sun.jdi.CharValue;
import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.ClassObjectReference;
import com.sun.jdi.ClassType;
import com.sun.jdi.DoubleType;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.FloatType;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerType;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InterfaceType;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.LongType;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.MonitorInfo;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VoidType;
import com.sun.jdi.VoidValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class FXVirtualMachine extends FXMirror implements VirtualMachine {
    public FXVirtualMachine(VirtualMachine underlying) {
        super(null, underlying);
    }

    public boolean canForceEarlyReturn() {
        return underlying().canForceEarlyReturn();
    }

    public boolean canGetClassFileVersion() {
        return underlying().canGetClassFileVersion();
    }

    public boolean canGetConstantPool() {
        return underlying().canGetConstantPool();
    }

    public boolean canGetInstanceInfo() {
        return underlying().canGetInstanceInfo();
    }

    public boolean canGetMethodReturnValues() {
        return underlying().canGetMethodReturnValues();
    }

    public boolean canGetMonitorFrameInfo() {
        return underlying().canGetMonitorFrameInfo();
    }

    public boolean canRequestMonitorEvents() {
        return underlying().canRequestMonitorEvents();
    }

    public boolean canUseSourceNameFilters() {
        return underlying().canUseSourceNameFilters();
    }

    public long[] instanceCounts(List<? extends ReferenceType> refTypes) {
        return underlying().instanceCounts(FXWrapper.unwrapReferenceTypes(refTypes));
    }

    public VoidValue mirrorOfVoid() {
        return voidValue();
    }

    @Override
    public FXVirtualMachine virtualMachine() {
        return this;
    }

    public List<ReferenceType> allClasses() {
        return FXWrapper.wrapReferenceTypes(this, underlying().allClasses());
    }

    public List<ThreadReference> allThreads() {
        return FXWrapper.wrapThreads(this, underlying().allThreads());
    }

    public boolean canAddMethod() {
        return underlying().canAddMethod();
    }

    public boolean canBeModified() {
        return underlying().canBeModified();
    }

    public boolean canGetBytecodes() {
        return underlying().canGetBytecodes();
    }

    public boolean canGetCurrentContendedMonitor() {
        return underlying().canGetCurrentContendedMonitor();
    }

    public boolean canGetMonitorInfo() {
        return underlying().canGetMonitorInfo();
    }

    public boolean canGetOwnedMonitorInfo() {
        return underlying().canGetOwnedMonitorInfo();
    }

    public boolean canGetSourceDebugExtension() {
        return underlying().canGetSourceDebugExtension();
    }

    public boolean canGetSyntheticAttribute() {
        return underlying().canGetSyntheticAttribute();
    }

    public boolean canPopFrames() {
        return underlying().canPopFrames();
    }

    public boolean canRedefineClasses() {
        return underlying().canRedefineClasses();
    }

    public boolean canRequestVMDeathEvent() {
        return underlying().canRequestVMDeathEvent();
    }

    public boolean canUnrestrictedlyRedefineClasses() {
        return underlying().canUnrestrictedlyRedefineClasses();
    }

    public boolean canUseInstanceFilters() {
        return underlying().canUseInstanceFilters();
    }

    public boolean canWatchFieldAccess() {
        return underlying().canWatchFieldAccess();
    }

    public boolean canWatchFieldModification() {
        return underlying().canWatchFieldModification();
    }

    public List<ReferenceType> classesByName(String name) {
        List<ReferenceType> refTypes = underlying().classesByName(name);
        if (name.equals(FX_SEQUENCE_TYPE_NAME) && refTypes.size() == 1) {
            List<ReferenceType> result = new ArrayList<ReferenceType>();
            result.add(new FXSequenceType(this, (InterfaceType) refTypes.get(0)));
            return result;
        } else {
            return FXWrapper.wrapReferenceTypes(this, underlying().classesByName(name));
        }
    }

    public String description() {
        return underlying().description();
    }

    public void dispose() {
        underlying().dispose();
    }

    
    private FXEventQueue evtQueue;
    public synchronized FXEventQueue eventQueue() {
        if (evtQueue == null) {
            evtQueue = FXWrapper.wrap(this, underlying().eventQueue());
        }
        return evtQueue;
    }

    private FXEventRequestManager evtManager;
    public synchronized FXEventRequestManager eventRequestManager() {
        if (evtManager == null) {
            evtManager = FXWrapper.wrap(this, underlying().eventRequestManager());
        }
        return evtManager;
    }

    public void exit(int exitCode) {
        underlying().exit(exitCode);
    }

    public String getDefaultStratum() {
        return underlying().getDefaultStratum();
    }

    public FXBooleanValue mirrorOf(boolean value) {
        return new FXBooleanValue(this, underlying().mirrorOf(value));
    }

    public FXByteValue mirrorOf(byte value) {
        return new FXByteValue(this, underlying().mirrorOf(value));
    }

    public FXCharValue mirrorOf(char value) {
        return new FXCharValue(this, underlying().mirrorOf(value));
    }

    public FXShortValue mirrorOf(short value) {
        return new FXShortValue(this, underlying().mirrorOf(value));
    }

    public FXIntegerValue mirrorOf(int value) {
        return new FXIntegerValue(this, underlying().mirrorOf(value));
    }

    public FXLongValue mirrorOf(long value) {
        return new FXLongValue(this, underlying().mirrorOf(value));
    }

    public FXFloatValue mirrorOf(float value) {
        return new FXFloatValue(this, underlying().mirrorOf(value));
    }

    public FXDoubleValue mirrorOf(double value) {
        return new FXDoubleValue(this, underlying().mirrorOf(value));
    }

    public StringReference mirrorOf(String value) {
        return new FXStringReference(this, underlying().mirrorOf(value));
    }

    public String name() {
        return underlying().name();
    }

    public Process process() {
        return underlying().process();
    }

    public void redefineClasses(Map<? extends ReferenceType, byte[]> classBytes) {
        Map<ReferenceType, byte[]> unwrappedClassBytes = new HashMap<ReferenceType, byte[]>();
        for (Map.Entry<? extends ReferenceType, byte[]> entry : classBytes.entrySet()) {
            unwrappedClassBytes.put(FXWrapper.unwrap(entry.getKey()), entry.getValue());
        }
        underlying().redefineClasses(unwrappedClassBytes);
    }

    public void resume() {
        underlying().resume();
    }

    public void setDebugTraceMode(int mode) {
        underlying().setDebugTraceMode(mode);
    }

    public void setDefaultStratum(String stratum) {
        underlying().setDefaultStratum(stratum);
    }

    public void suspend() {
        underlying().suspend();
    }

    public List<ThreadGroupReference> topLevelThreadGroups() {
        return FXWrapper.wrapThreadGroups(this, underlying().topLevelThreadGroups());
    }

    public String version() {
        return underlying().version();
    }

    @Override
    protected VirtualMachine underlying() {
        return (VirtualMachine) super.underlying();
    }

    // JavaFX types
    public static final String FX_OBJECT_TYPE_NAME = "com.sun.javafx.runtime.FXObject";
    private InterfaceType fxObjectType;
    public synchronized InterfaceType fxObjectType() {
        if (fxObjectType == null) {
            List<ReferenceType> refTypes = classesByName(FX_OBJECT_TYPE_NAME);
            fxObjectType = refTypes.isEmpty() ? null : (InterfaceType) refTypes.get(0);
        }
        return fxObjectType;
    }

    public static final String FX_MIXIN_TYPE_NAME = "com.sun.javafx.runtime.FXMixin";
    private InterfaceType fxMixinType;
    public synchronized ReferenceType fxMixinType() {
        if (fxMixinType == null) {
            List<ReferenceType> refTypes = classesByName(FX_MIXIN_TYPE_NAME);
            fxMixinType = refTypes.isEmpty()? null : (InterfaceType) refTypes.get(0);
        }
        return fxMixinType;
    }

    public static final String FX_SEQUENCE_TYPE_NAME = "com.sun.javafx.runtime.sequence.Sequence";
    private FXSequenceType fxSequenceType;
    public synchronized FXSequenceType fxSequenceType() {
        if (fxSequenceType == null) {
            List<ReferenceType> refTypes = classesByName(FX_SEQUENCE_TYPE_NAME);
            fxSequenceType = refTypes.isEmpty() ? null : (FXSequenceType) refTypes.get(0);
        }
        return fxSequenceType;
    }

    private FXVoidValue voidValue;
    private synchronized FXVoidValue voidValue() {
        if (voidValue == null) {
            voidValue = new FXVoidValue(this, underlying().mirrorOfVoid());
        }
        return voidValue;
    }
    // wrapper methods

    // primitive type accessors private FXBooleanType booleanType;
    private FXVoidType voidType;
    protected synchronized FXVoidType voidType(VoidType vt) {
        if (voidType == null) {
            voidType = new FXVoidType(this, vt);
        }
        return voidType;
    }

    private FXBooleanType booleanType;
    protected synchronized FXBooleanType booleanType(BooleanType bt) {
        if (booleanType == null) {
            booleanType = new FXBooleanType(this, bt);
        }
        return booleanType;
    }

    private FXCharType charType;
    protected synchronized FXCharType charType(CharType ct) {
        if (charType == null) {
            charType = new FXCharType(this, ct);
        }
        return charType;
    }

    private FXByteType byteType;
    protected synchronized FXByteType byteType(ByteType bt) {
        if (byteType == null) {
            byteType = new FXByteType(this, bt);
        }
        return byteType;
    }

    private FXShortType shortType;
    protected synchronized FXShortType shortType(ShortType st) {
        if (shortType == null) {
            shortType = new FXShortType(this, st);
        }
        return shortType;
    }

    private FXIntegerType integerType;
    protected synchronized FXIntegerType integerType(IntegerType it) {
        if (integerType == null) {
            integerType = new FXIntegerType(this, it);
        }
        return integerType;
    }

    private FXLongType longType;
    protected synchronized FXLongType longType(LongType lt) {
        if (longType == null) {
            longType = new FXLongType(this, lt);
        }
        return longType;
    }

    private FXFloatType floatType;
    protected synchronized FXFloatType floatType(FloatType ft) {
        if (floatType == null) {
            floatType = new FXFloatType(this, ft);
        }
        return floatType;
    }


    private FXDoubleType doubleType;
    protected synchronized FXDoubleType doubleType(DoubleType dt) {
        if (doubleType == null) {
            doubleType = new FXDoubleType(this, dt);
        }
        return doubleType;
    }

    protected FXLocation location(Location loc) {
        return new FXLocation(this, loc);
    }

    protected FXReferenceType referenceType(ReferenceType rt) {
        return new FXReferenceType(this, rt);
    }

    protected FXClassType classType(ClassType ct) {
        return  new FXClassType(this, ct);
    }

    protected FXInterfaceType interfaceType(InterfaceType it) {
        return new FXInterfaceType(this, it);
    }

    protected FXArrayType arrayType(ArrayType at) {
        return new FXArrayType(this, at);
    }

    protected FXField field(Field field) {
        return new FXField(this, field);
    }

    protected FXMethod method(Method method) {
        return new FXMethod(this, method);
    }

    protected FXLocalVariable localVariable(LocalVariable var) {
        return new FXLocalVariable(this, var);
    }

    protected FXBooleanValue booleanValue(BooleanValue value) {
        return new FXBooleanValue(this, value);
    }

    protected FXCharValue charValue(CharValue value) {
        return new FXCharValue(this, value);
    }

    protected FXByteValue byteValue(ByteValue value) {
        return new FXByteValue(this, value);
    }

    protected FXShortValue shortValue(ShortValue value) {
        return new FXShortValue(this, value);
    }

    protected FXIntegerValue integerValue(IntegerValue value) {
        return new FXIntegerValue(this, value);
    }

    protected FXLongValue longValue(LongValue value) {
        return new FXLongValue(this, value);
    }

    protected FXFloatValue floatValue(FloatValue value) {
        return new FXFloatValue(this, value);
    }

    protected FXDoubleValue doubleValue(DoubleValue value) {
        return new FXDoubleValue(this, value);
    }

    protected FXObjectReference objectReference(ObjectReference ref) {
        ReferenceType rt = ref.referenceType();
        if (rt instanceof ClassType) {
            ClassType ct = (ClassType) rt;
            boolean isSeq =  ct.allInterfaces().contains(FXWrapper.unwrap(fxSequenceType()));
            if (isSeq) {
                return new FXSequenceReference(this, ref);
            }
        }
        return new FXObjectReference(this, ref);
    }

    protected FXThreadReference threadReference(ThreadReference tref) {
        return new FXThreadReference(this, tref);
    }

    protected FXThreadGroupReference threadGroupReference(ThreadGroupReference tgref) {
        return new FXThreadGroupReference(this, tgref);
    }

    protected FXStringReference stringReference(StringReference sref) {
        return new FXStringReference(this, sref);
    }

    protected FXClassLoaderReference classLoaderReference(ClassLoaderReference clref) {
        return new FXClassLoaderReference(this, clref);
    }

    protected FXClassObjectReference classObjectReference(ClassObjectReference coref) {
        return new FXClassObjectReference(this, coref);
    }

    protected FXArrayReference arrayReference(ArrayReference aref) {
        return new FXArrayReference(this, aref);
    }

    protected FXMonitorInfo monitorInfo(MonitorInfo monitorInfo) {
        return new FXMonitorInfo(this, monitorInfo);
    }

    protected FXStackFrame stackFrame(StackFrame frame) {
        return new FXStackFrame(this, frame);
    }
}