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

package org.visage.jdi;

import org.visage.jdi.event.FXEventQueue;
import org.visage.jdi.request.FXEventRequestManager;
import com.sun.jdi.Type;
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
import com.sun.jdi.Value;
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
        return FXWrapper.wrapReferenceTypes(this, refTypes);
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

    // default values

    private FXBooleanValue booleanDefaultValue;
    protected synchronized FXBooleanValue booleanDefaultValue() {
        if (booleanDefaultValue == null) {
            booleanDefaultValue = mirrorOf(false);
        }
        return booleanDefaultValue;
    }

    private FXByteValue byteDefaultValue;
    protected synchronized FXByteValue byteDefaultValue() {
        if (byteDefaultValue == null) {
            byteDefaultValue = mirrorOf((byte)0);
        }
        return byteDefaultValue;
    }

    private FXCharValue charDefaultValue;
    protected synchronized FXCharValue charDefaultValue() {
        if (charDefaultValue == null) {
            charDefaultValue = mirrorOf('\u0000');
        }
        return charDefaultValue;
    }

    private FXShortValue shortDefaultValue;
    protected synchronized FXShortValue shortDefaultValue() {
        if (shortDefaultValue == null) {
            shortDefaultValue = mirrorOf((short)0);
        }
        return shortDefaultValue;
    }

    private FXIntegerValue integerDefaultValue;
    protected synchronized FXIntegerValue integerDefaultValue() {
        if (integerDefaultValue == null) {
            integerDefaultValue = mirrorOf(0);
        }
        return integerDefaultValue;
    }

    private FXLongValue longDefaultValue;
    protected synchronized FXLongValue longDefaultValue() {
        if (longDefaultValue == null) {
            longDefaultValue = mirrorOf(0l);
        }
        return longDefaultValue;
    }

    private FXFloatValue floatDefaultValue;
    protected synchronized FXFloatValue floatDefaultValue() {
        if (floatDefaultValue == null) {
            floatDefaultValue = mirrorOf(0.0f);
        }
        return floatDefaultValue;
    }

    private FXDoubleValue doubleDefaultValue;
    protected synchronized FXDoubleValue doubleDefaultValue() {
        if (doubleDefaultValue == null) {
            doubleDefaultValue = mirrorOf(0.0d);
        }
        return doubleDefaultValue;
    }

    //////////

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

    private FXThreadReference cacheUiThread = null;
    /**
     * JDI addition: Return the thread upon which invokeMethods are performed to get/set fields
     *
     * @return the thread upon which invokeMethods are performed by FX-JDI to get/set fields 
     * that have getters/setters
     */
    public FXThreadReference uiThread() {
        if (cacheUiThread == null) {
            FXField uiThreadField = fxEntryType().fieldByName("uiThread");
            cacheUiThread = (FXThreadReference) ((FXReferenceType)fxEntryType()).getValue(uiThreadField);
        }
        return cacheUiThread;
    }

    @Override
    protected VirtualMachine underlying() {
        return (VirtualMachine) super.underlying();
    }

    // JavaFX types
    public static final String FX_ENTRY_TYPE_NAME = "org.visage.runtime.Entry";
    private FXClassType fxEntryType;
    public synchronized FXClassType fxEntryType() {
        if (fxEntryType == null) {
            List<ReferenceType> refTypes = classesByName(FX_ENTRY_TYPE_NAME);
            fxEntryType = refTypes.isEmpty() ? null : (FXClassType) refTypes.get(0);
        }
        return fxEntryType;
    }

    public static final String FX_OBJECT_TYPE_NAME = "org.visage.runtime.FXObject";
    private FXObjectType fxObjectType;
    public synchronized FXObjectType fxObjectType() {
        if (fxObjectType == null) {
            List<ReferenceType> refTypes = classesByName(FX_OBJECT_TYPE_NAME);
            fxObjectType = refTypes.isEmpty() ? null : (FXObjectType) refTypes.get(0);
        }
        return fxObjectType;
    }

    public static final String FX_MIXIN_TYPE_NAME = "org.visage.runtime.FXMixin";
    private FXInterfaceType fxMixinType;
    public synchronized FXReferenceType fxMixinType() {
        if (fxMixinType == null) {
            List<ReferenceType> refTypes = classesByName(FX_MIXIN_TYPE_NAME);
            fxMixinType = refTypes.isEmpty()? null : (FXInterfaceType) refTypes.get(0);
        }
        return fxMixinType;
    }

    public static final String FX_SEQUENCE_TYPE_NAME = "org.visage.runtime.sequence.Sequence";
    private FXSequenceType fxSequenceType;
    public synchronized FXSequenceType fxSequenceType() {
        if (fxSequenceType == null) {
            List<ReferenceType> refTypes = classesByName(FX_SEQUENCE_TYPE_NAME);
            fxSequenceType = refTypes.isEmpty() ? null : (FXSequenceType) refTypes.get(0);
        }
        return fxSequenceType;
    }

    public static final String FX_SEQUENCES_TYPE_NAME = "org.visage.runtime.sequence.Sequences";
    private FXSequencesType fxSequencesType;
    public synchronized FXSequencesType fxSequencesType() {
        if (fxSequencesType == null) {
            List<ReferenceType> refTypes = classesByName(FX_SEQUENCES_TYPE_NAME);
            if (refTypes.isEmpty()) {
                // ensure that the debuggee has loaded and initialized Sequences type
                fxSequencesType = (FXSequencesType) classType(initSequencesType());
            } else {
                fxSequencesType = (FXSequencesType) refTypes.get(0);
            }
        }
        return fxSequencesType;
    }

    private FXVoidValue voidValue;
    protected synchronized FXVoidValue voidValue() {
        if (voidValue == null) {
            voidValue = new FXVoidValue(this, underlying().mirrorOfVoid());
        }
        return voidValue;
    }

    // wrapper methods

    // primitive type accessors
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

    private final Map<ReferenceType, FXReferenceType> refTypesCache =
            new HashMap<ReferenceType, FXReferenceType>();

    protected FXReferenceType referenceType(ReferenceType rt) {
        synchronized (refTypesCache) {
            if (! refTypesCache.containsKey(rt)) {
                refTypesCache.put(rt, new FXReferenceType(this, rt));
            }
            return refTypesCache.get(rt);
        }
    }

    protected FXClassType classType(ClassType ct) {
        synchronized (refTypesCache) {
            if (! refTypesCache.containsKey(ct)) {
                String name = ct.name();
                if (name.equals(FX_SEQUENCES_TYPE_NAME)) {
                    refTypesCache.put(ct, new FXSequencesType(this, ct));
                } else {
                    refTypesCache.put(ct, new FXClassType(this, ct));
                }
            }
            return (FXClassType) refTypesCache.get(ct);
        }
    }

    protected FXInterfaceType interfaceType(InterfaceType it) {
        synchronized (refTypesCache) {
            if (! refTypesCache.containsKey(it)) {
                String name = it.name();
                if (name.equals(FX_OBJECT_TYPE_NAME)) {
                   refTypesCache.put(it, new FXObjectType(this, it));
                } else if (name.equals(FX_SEQUENCE_TYPE_NAME)) {
                   refTypesCache.put(it, new FXSequenceType(this, it));
                } else {
                   refTypesCache.put(it, new FXInterfaceType(this, it));
                }
            }
            return (FXInterfaceType) refTypesCache.get(it);
        }
    }

    protected FXArrayType arrayType(ArrayType at) {
        synchronized (at) {
            if (! refTypesCache.containsKey(at)) {
                refTypesCache.put(at, new FXArrayType(this, at));
            }
            return (FXArrayType) refTypesCache.get(at);
        }
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

    protected Exception lastFieldAccessException = null;
    protected void setLastFieldAccessException(Exception ee) {
        lastFieldAccessException = ee;
    }

    /**
     * JDI addition: Return the exception thrown by an invokeMethod call that was 
     * performed in the most recent setValue, getValue, or getValues method call.
     * 
     * @return the exception thrown by an invokeMethod call that was
     * performed in the most recent setValue, getValue, or getValues method call, or
     * null if no such exception was thrown.
     */
    public Exception lastFieldAccessException() {
        return lastFieldAccessException;
    }

    // cache these masks 
    private int invalidFlagMask = 0;
    private int readOnlyFlagMask = 0;
    private int boundFlagMask = 0;
    private int getFlagMask(String maskName) {
        int flagMask = 0;
        // we only work with underlying JDI objects here
        List<ReferenceType> rtx =  this.underlying().classesByName("org.visage.runtime.FXObject");
        if (rtx.size() != 1) {
            System.out.println("Can't find the ReferenceType for org.visage.runtime.FXObject");
            return 0;
        }
        ReferenceType fxObjectRefType = rtx.get(0);
        Field fieldx = fxObjectRefType.fieldByName(maskName);
        Value flagValue = fxObjectRefType.getValue(fieldx);
        return ((IntegerValue)flagValue).value();
    }

    protected int FXReadOnlyFlagMask() {
        if (readOnlyFlagMask == 0) {
            readOnlyFlagMask = getFlagMask("VFLGS$IS_READONLY");
        }
        return readOnlyFlagMask;
    }

    protected int FXInvalidFlagMask() {
        if (invalidFlagMask == 0) {
            invalidFlagMask = getFlagMask("VFLGS$IS_BOUND_INVALID");
        }
        return invalidFlagMask;
    }

    protected int FXBoundFlagMask() {
        if (boundFlagMask == 0) {
            boundFlagMask = getFlagMask("VFLGS$IS_BOUND");
        }
        return boundFlagMask;
    }
    
    protected Value defaultValue(Type type) {
        if (type instanceof BooleanType) {
            return booleanDefaultValue();
        }
        if (type instanceof ByteType) {
            return byteDefaultValue();
        }
        if (type instanceof CharType) {
            return charDefaultValue();
        }
        if (type instanceof DoubleType) {
            return doubleDefaultValue();
        }
        if (type instanceof FloatType) {
            return floatDefaultValue();
        }
        if (type instanceof IntegerType) {
            return integerDefaultValue();
        }
        if (type instanceof LongType) {
            return longDefaultValue();
        }
        if (type instanceof ShortType) {
            return shortDefaultValue();
        }
        // else it is an object/array/sequence/...
        return null;
    }

    // ensure that the debuggee VM has loaded and initialized Sequences type
    private synchronized ClassType initSequencesType() {
        VirtualMachine vm = underlying();
        ClassType classType = (ClassType) vm.classesByName("java.lang.Class").get(0);
        Method forName = classType.concreteMethodByName("forName",
                "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;");

        // Class.forName(FX_SEQUENCES_TYPE_NAME, true, Entry.class.getClassLoader());
        try {
            List<Value> args = new ArrayList<Value>(3);
            args.add(vm.mirrorOf(FX_SEQUENCES_TYPE_NAME));
            args.add(vm.mirrorOf(true));
            args.add(FXWrapper.unwrap(fxEntryType().classLoader()));
            ClassObjectReference retVal = (ClassObjectReference)classType.invokeMethod(
                                                 FXWrapper.unwrap(uiThread()), forName, args, 0);
            // retVal must be a ClassObjectReference for the Sequences class
            return (ClassType)retVal.reflectedType();
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            // exp.printStackTrace();
            throw new RuntimeException(exp);
        }
    }
}
