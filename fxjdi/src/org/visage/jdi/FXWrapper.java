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
import com.sun.jdi.PrimitiveType;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VoidType;
import com.sun.jdi.VoidValue;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.EventRequestManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sundar
 */
public class FXWrapper {
    public static FXVirtualMachine wrap(VirtualMachine vm) {
        return (vm == null)? null : new FXVirtualMachine(vm);
    }

    public static List<VirtualMachine> wrapVirtualMachines(List<VirtualMachine> vms) {
        List<VirtualMachine> res = new ArrayList<VirtualMachine>(vms.size());
        for (VirtualMachine vm : vms) {
            res.add(wrap(vm));
        }
        return res;
    }

    public static FXType wrap(FXVirtualMachine fxvm, Type type) {
        if (type == null) {
            return null;
        }
        
        if (type instanceof VoidType) {
            return fxvm.voidType((VoidType)type);
        } else if (type instanceof PrimitiveType) {
            if (type instanceof BooleanType) {
                return fxvm.booleanType((BooleanType)type);
            } else if (type instanceof CharType) {
                return fxvm.charType((CharType)type);
            } else if (type instanceof ByteType) {
                return fxvm.byteType((ByteType)type);
            } else if (type instanceof ShortType) {
                return fxvm.shortType((ShortType)type);
            } else if (type instanceof IntegerType) {
                return fxvm.integerType((IntegerType)type);
            } else if (type instanceof LongType) {
                return fxvm.longType((LongType)type);
            } else if (type instanceof FloatType) {
                return fxvm.floatType((FloatType)type);
            } else if (type instanceof DoubleType) {
                return fxvm.doubleType((DoubleType)type);
            } else {
                throw new IllegalArgumentException("illegal primitive type : " + type);
            }
        } else if (type instanceof ReferenceType) {
            return wrap(fxvm, (ReferenceType)type);
        } else {
            throw new IllegalArgumentException("illegal type: " + type);
        }
    }

    public static List<Type> wrapTypes(FXVirtualMachine fxvm, List<Type> types) {
        if (types == null) {
            return null;
        }
        List<Type> result = new ArrayList<Type>(types.size());
        for (Type type : types) {
            result.add(wrap(fxvm, type));
        }
        return result;
    }

    public static FXReferenceType wrap(FXVirtualMachine fxvm, ReferenceType rt) {
        if (rt == null) {
            return null;
        } else if (rt instanceof ClassType) {
            return fxvm.classType((ClassType)rt);
        } else if (rt instanceof InterfaceType) {
            return fxvm.interfaceType((InterfaceType)rt);
        } else if (rt instanceof ArrayType) {
            return fxvm.arrayType((ArrayType)rt);
        } else {
            return fxvm.referenceType(rt);
        }
    }

    public static FXClassType wrap(FXVirtualMachine fxvm, ClassType ct) {
        return (ct == null)? null : fxvm.classType(ct);
    }

    public static FXInterfaceType wrap(FXVirtualMachine fxvm, InterfaceType it) {
        return (it == null)? null : fxvm.interfaceType(it);
    }

    public static FXArrayType wrap(FXVirtualMachine fxvm, ArrayType at) {
        return (at == null)? null : fxvm.arrayType(at);
    }

    public static List<ReferenceType> wrapReferenceTypes(FXVirtualMachine fxvm, List<ReferenceType> refTypes) {
        // Note that VirtualMachineImpl caches the list, and returns an unmodifiable wrapped list.
        // Classes that get loaded in the future are added to its list by an EventListener on ClassPrepared 
        // events.  If we cache our wrapped list, they we would have to do the same thing, or be able
        // to update our cached list when this method is called again.  So for the time being,
        // we won't cache and thus don't have to return an unmodifiable list.
        if (refTypes == null) {
            return null;
        }
        List<ReferenceType> result = new ArrayList<ReferenceType>(refTypes.size());
        for (ReferenceType rt : refTypes) {
            String className = rt.name();
            // visage generated clases contain $[1-9]Local$ or $ObjLit$
            if (className.indexOf('$') != -1) {
                if (className.indexOf("$ObjLit$") != -1) {
                    continue;
                }
                if (className.matches(".*\\$[0-9]+Local\\$.*")) {
                    continue;
                }
            }
            result.add(FXWrapper.wrap(fxvm, rt));
        }
        return result;
    }

    public static List<ClassType> wrapClassTypes(FXVirtualMachine fxvm, List<ClassType> classes) {
        if (classes == null) {
            return null;
        }
        List<ClassType> result = new ArrayList<ClassType>(classes.size());
        for (ClassType ct : classes) {
            result.add(FXWrapper.wrap(fxvm, ct));
        }
        return result;
    }

    public static List<InterfaceType> wrapInterfaceTypes(FXVirtualMachine fxvm, List<InterfaceType> interfaces) {
        if (interfaces == null) {
            return null;
        }
        List<InterfaceType> result = new ArrayList<InterfaceType>(interfaces.size());
        for (InterfaceType it : interfaces) {
            result.add(FXWrapper.wrap(fxvm, it));
        }
        return result;
    }

    public static FXLocation wrap(FXVirtualMachine fxvm, Location loc) {
        return (loc == null)? null : fxvm.location(loc);
    }

    public static List<Location> wrapLocations(FXVirtualMachine fxvm, List<Location> locations) {
        if (locations == null) {
            return null;
        }
        List<Location> result = new ArrayList<Location>(locations.size());
        for (Location loc: locations) {
            result.add(wrap(fxvm, loc));
        }
        return result;
    }

    public static FXField wrap(FXVirtualMachine fxvm, Field field) {
        return (field == null)? null : fxvm.field(field);
    }

    /*
     * The fields are JDI Fields.
     * Each field can be a user field of an FX class, an internal field of an FX class,
     * or a field of a Java class.
     */
    public static List<Field> wrapFields(FXVirtualMachine fxvm, List<Field> fields) {
        // Create FXField wrappers for each field that is a valid FX field.
        if (fields == null) {
            return null;
        }
        // We will have far fewer fields than fields.size() due to all the VFLGS etc
        // fields we will discard , so start with some small random amount
        List<Field> result = new ArrayList<Field>(20);

        for (Field fld : fields) {
            String fldName = fld.name();
            int firstDollar = fldName.indexOf('$');
            // java names do not start with $.
            // FX user names start with a $ but so do various internal names
            // mixin vars are mangled with the mixin classname, et,   $MixinClassName$fieldName
            if (firstDollar != -1) {
                if ((fldName.indexOf("_$",1)    != -1) ||
                    (fldName.indexOf("$$")      != -1) ||
                    (fldName.indexOf("$helper$") == 0) ||
                    (fldName.indexOf("$script$") == 0) ||
                    (fldName.indexOf("$ol$")    != -1)) {
                    // $ol$ means it is a shredded name from a bound obj lit (see JavafxLower.java)
                    // _$ means it is a synth var (see JavafxPreTranslationSupport.java)
                    // $helper$ is in JavafxDefs.java
                    continue;
                }
            }

            if (fldName.equals("$assertionsDisabled") && fld.declaringType().name().equals("org.visage.runtime.FXBase")) {
                continue;
            }
            /*
              - mixin fields are named $MixinClassName$fieldName
              - a private script field is java private, and is named with its normal name 
              UNLESS it is referenced in a subclass. In this case it is java public and
              its name is $ClassName$fieldName.  
              This mangling in of the classname is not yet handled.
            */
            if (firstDollar <= 0) {
                result.add(fxvm.field(fld));
            }
        }
        return result;
    }

    public static FXMethod wrap(FXVirtualMachine fxvm, Method method) {
        return (method == null)? null : fxvm.method(method);
    }

    public static List<Method> wrapMethods(FXVirtualMachine fxvm, List<Method> methods) {
        if (methods == null) {
            return null;
        }
        List<Method> result = new ArrayList<Method>(20);
        for (Method mth : methods) {
            FXMethod fxm = fxvm.method(mth);
            if (!fxm.isJavaFXInternalMethod()) {
                result.add(fxm);
            }
        }
        return result;
    }

    public static FXMonitorInfo wrap(FXVirtualMachine fxvm, MonitorInfo monitorInfo) {
        return (monitorInfo == null)? null : fxvm.monitorInfo(monitorInfo);
    }

    public static List<MonitorInfo> wrapMonitorInfos(FXVirtualMachine fxvm, List<MonitorInfo> monInfos) {
        if (monInfos == null) {
            return null;
        }
        List<MonitorInfo> result = new ArrayList<MonitorInfo>(monInfos.size());
        for (MonitorInfo mi : monInfos) {
            result.add(wrap(fxvm, mi));
        }
        return result;
    }

    public static FXStackFrame wrap(FXVirtualMachine fxvm, StackFrame frame) {
        return (frame == null)? null : fxvm.stackFrame(frame);
    }

    public static List<StackFrame> wrapFrames(FXVirtualMachine fxvm, List<StackFrame> frames) {
        if (frames == null) {
            return null;
        }
        List<StackFrame> result = new ArrayList<StackFrame>(frames.size());
        for (StackFrame fr : frames) {
            result.add(wrap(fxvm, fr));
        }
        return result;
    }

    public static FXLocalVariable wrap(FXVirtualMachine fxvm, LocalVariable var) {
        return (var == null)? null : fxvm.localVariable(var);
    }

    public static List<LocalVariable> wrapLocalVariables(FXVirtualMachine fxvm, List<LocalVariable> locals) {
        if (locals == null) {
            return null;
        }
        List<LocalVariable> result = new ArrayList<LocalVariable>(locals.size());
        for (LocalVariable var: locals) {
            result.add(wrap(fxvm, var));
        }
        return result;
    }

    public static FXValue wrap(FXVirtualMachine fxvm, Value value) {
        if (value == null) {
            return null;
        }

        if (value instanceof PrimitiveValue) {
            if (value instanceof BooleanValue) {
                return fxvm.booleanValue((BooleanValue)value);
            } else if (value instanceof CharValue) {
                return fxvm.charValue((CharValue)value);
            } else if (value instanceof ByteValue) {
                return fxvm.byteValue((ByteValue)value);
            } else if (value instanceof ShortValue) {
                return fxvm.shortValue((ShortValue)value);
            } else if (value instanceof IntegerValue) {
                return fxvm.integerValue((IntegerValue)value);
            } else if (value instanceof LongValue) {
                return fxvm.longValue((LongValue)value);
            } else if (value instanceof FloatValue) {
                return fxvm.floatValue((FloatValue)value);
            } else if (value instanceof DoubleValue) {
                return fxvm.doubleValue((DoubleValue)value);
            } else {
                throw new IllegalArgumentException("illegal primitive value : " + value);
            }
        } else if (value instanceof VoidValue) {
            return fxvm.voidValue();
        } else if (value instanceof ObjectReference) {
            return  wrap(fxvm, (ObjectReference)value);
        } else {
            throw new IllegalArgumentException("illegal value: " + value);
        }
    }

    public static List<ObjectReference> wrapObjectReferences(FXVirtualMachine fxvm, List<ObjectReference> refs) {
        if (refs == null) {
            return null;
        }
        List<ObjectReference> result = new ArrayList<ObjectReference>(refs.size());
        for (ObjectReference ref : refs) {
            result.add(wrap(fxvm, ref));
        }
        return result;
    }


    public static FXObjectReference wrap(FXVirtualMachine fxvm, ObjectReference ref) {
        if (ref == null) {
            return null;
        } else if (ref instanceof ArrayReference) {
            return fxvm.arrayReference((ArrayReference)ref);
        } else if (ref instanceof StringReference) {
            return fxvm.stringReference((StringReference)ref);
        } else if (ref instanceof ThreadReference) {
            return fxvm.threadReference((ThreadReference)ref);
        } else if (ref instanceof ThreadGroupReference) {
            return fxvm.threadGroupReference((ThreadGroupReference)ref);
        } else if (ref instanceof ClassLoaderReference) {
            return fxvm.classLoaderReference((ClassLoaderReference)ref);
        } else if (ref instanceof ClassObjectReference) {
            return fxvm.classObjectReference((ClassObjectReference)ref);
        } else {
            return fxvm.objectReference(ref);
        }
    }

    public static FXArrayReference wrap(FXVirtualMachine fxvm, ArrayReference ref) {
        return (ref == null)? null : fxvm.arrayReference(ref);
    }

    public static FXThreadReference wrap(FXVirtualMachine fxvm, ThreadReference ref) {
        return (ref == null)? null : fxvm.threadReference(ref);
    }


    public static FXThreadGroupReference wrap(FXVirtualMachine fxvm, ThreadGroupReference ref) {
        return (ref == null)? null : fxvm.threadGroupReference(ref);
    }

    public static List<ThreadReference> wrapThreads(FXVirtualMachine fxvm, List<ThreadReference> threads) {
        if (threads == null) {
            return null;
        }
        List<ThreadReference> result = new ArrayList<ThreadReference>(threads.size());
        for (ThreadReference tref : threads) {
            result.add(wrap(fxvm, tref));
        }
        return result;
    }

    public static List<ThreadGroupReference> wrapThreadGroups(FXVirtualMachine fxvm, List<ThreadGroupReference> threadGroups) {
        if (threadGroups == null) {
            return null;
        }
        List<ThreadGroupReference> result = new ArrayList<ThreadGroupReference>(threadGroups.size());
        for (ThreadGroupReference tref : threadGroups) {
            result.add(wrap(fxvm, tref));
        }
        return result;
    }

    public static FXClassLoaderReference wrap(FXVirtualMachine fxvm, ClassLoaderReference ref) {
        return (ref == null)? null : fxvm.classLoaderReference(ref);
    }

    public static FXClassObjectReference wrap(FXVirtualMachine fxvm, ClassObjectReference ref) {
        return (ref == null)? null : fxvm.classObjectReference(ref);
    }

    public static List<Value> wrapValues(FXVirtualMachine fxvm, List<Value> values) {
        if (values == null) {
            return null;
        }
        List<Value> result = new ArrayList<Value>(values.size());
        for (Value v : values) {
            result.add(wrap(fxvm, v));
        }
        return result;
    }

    public static Location unwrap(Location loc) {
        return (loc instanceof FXLocation)? ((FXLocation)loc).underlying() : loc;
    }

    public static StackFrame unwrap(StackFrame frame) {
        return (frame instanceof FXStackFrame)? ((FXStackFrame)frame).underlying() : frame;
    }

    public static LocalVariable unwrap(LocalVariable var) {
        return (var instanceof FXLocalVariable)? ((FXLocalVariable)var).underlying() : var;
    }
    
    public static Value unwrap(Value value) {
        return (value instanceof FXValue)? ((FXValue)value).underlying() : value;
    }

    public static List<? extends Value> unwrapValues(List<? extends Value> values) {
        if (values == null) {
            return null;
        }
        List<Value> result = new ArrayList<Value>(values.size());
        for (Value v : values) {
            result.add(unwrap(v));
        }
        return result;
    }

    public static Field unwrap(Field field) {
        return (field instanceof FXField)? ((FXField)field).underlying() : field;
    }

    public static Method unwrap(Method method) {
        return (method instanceof FXMethod)? ((FXMethod)method).underlying() : method;
    }

    public static ObjectReference unwrap(ObjectReference ref) {
        return (ref instanceof FXObjectReference)? ((FXObjectReference)ref).underlying() : ref;
    }

    public static ThreadReference unwrap(ThreadReference ref) {
        return (ref instanceof FXThreadReference)? ((FXThreadReference)ref).underlying() : ref;
    }

    public static ReferenceType unwrap(ReferenceType rt) {
        return (rt instanceof FXReferenceType)? ((FXReferenceType)rt).underlying() : rt;
    }

    public static List<? extends ReferenceType> unwrapReferenceTypes(List<? extends ReferenceType> refTypes) {
        if (refTypes == null) {
            return null;
        }
        List<ReferenceType> result = new ArrayList<ReferenceType>(refTypes.size());
        for (ReferenceType rt : refTypes) {
            result.add(unwrap(rt));
        }
        return result;
    }

    // event requests
    public static FXEventRequestManager wrap(FXVirtualMachine fxvm, EventRequestManager man) {
        return (man == null)? null : new FXEventRequestManager(fxvm, man);
    }

    // event queue
    public static FXEventQueue wrap(FXVirtualMachine fxvm, EventQueue evtQueue) {
        return (evtQueue == null)? null : new FXEventQueue(fxvm, evtQueue);
    }
}
