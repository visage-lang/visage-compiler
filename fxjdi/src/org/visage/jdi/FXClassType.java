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
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InterfaceType;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author sundar
 */
public class FXClassType extends FXReferenceType implements ClassType {
    private boolean isIsFxTypeSet = false;
    private boolean isFXType = false;

    public FXClassType(FXVirtualMachine fxvm, ClassType underlying) {
        super(fxvm, underlying);
    }

    public List<InterfaceType> allInterfaces() {
        return FXWrapper.wrapInterfaceTypes(virtualMachine(), underlying().allInterfaces());
    }

    public FXMethod concreteMethodByName(String name, String signature) {
        return FXWrapper.wrap(virtualMachine(), underlying().concreteMethodByName(name, signature));
    }

    public List<InterfaceType> interfaces() {
        return FXWrapper.wrapInterfaceTypes(virtualMachine(), underlying().interfaces());
    }

    public FXValue invokeMethod(ThreadReference thread, Method method, List<? extends Value> values, int options)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Value value =
                underlying().invokeMethod(
                    FXWrapper.unwrap(thread), FXWrapper.unwrap(method),
                    FXWrapper.unwrapValues(values), options);
        return FXWrapper.wrap(virtualMachine(), value);
    }

    public boolean isEnum() {
        return underlying().isEnum();
    }

    public FXObjectReference newInstance(ThreadReference thread, Method method, List<? extends Value> values, int options)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
         ObjectReference result =
                 underlying().newInstance(
                      FXWrapper.unwrap(thread), FXWrapper.unwrap(method),
                      FXWrapper.unwrapValues(values), options);
         return FXWrapper.wrap(virtualMachine(), result);
    }

    /**
     * JDI extension:  This will call the set function if one exists via invokeMethod.
     * The call to invokeMethod is preceded by a call to {@link FXEventQueue#setEventControl(boolean)} passing true
     * and is followed by a call to {@link FXEventQueue#setEventControl(boolean)} passing false.
     *
     * If an invokeMethod Exception occurs, it is saved and can be accessed by calling 
     * {@link FXVirtualMachine#lastFieldAccessException()}.
     */
    public void setValue(Field field, Value value) throws 
        InvalidTypeException, ClassNotLoadedException {

        virtualMachine().setLastFieldAccessException(null);
        Field jdiField = FXWrapper.unwrap(field);
        Value jdiValue = FXWrapper.unwrap(value);
        if (!isJavaFXType()) {
            underlying().setValue(jdiField, jdiValue);
            return;
        }
        if (isReadOnly(field)) {
            throw new IllegalArgumentException("Error: Cannot set value of a read-only field: " + field);
        } 
        if (isBound(field)) {
            throw new IllegalArgumentException("Error: Cannot set value of a bound field: " + field);
        }

        //get$xxxx methods exist for fields except private fields which have no binders
        List<Method> mth = underlying().methodsByName("set" + jdiField.name());
        if (mth.size() == 0) {
            // there is no setter
            underlying().setValue(jdiField, jdiValue);
            return;
        }
        // there is a setter
        ArrayList<Value> args = new ArrayList<Value>(1);
        args.add(jdiValue);
        Exception theExc = null;
        FXEventQueue eq = virtualMachine().eventQueue();
        try {
            eq.setEventControl(true);
            invokeMethod(virtualMachine().uiThread(), mth.get(0), args, ClassType.INVOKE_SINGLE_THREADED);
        } catch(InvalidTypeException ee) {
            theExc = ee;
        } catch(ClassNotLoadedException ee) {
            theExc = ee;
        } catch(IncompatibleThreadStateException ee) {
            theExc = ee;
        } catch(InvocationException ee) {
            theExc = ee;
        } finally {
            eq.setEventControl(false);
        }
        // We don't have to catch IllegalArgumentException.  It is an unchecked exception for invokeMethod
        // and for getValue

        virtualMachine().setLastFieldAccessException(theExc);
    }

    public List<ClassType> subclasses() {
        return FXWrapper.wrapClassTypes(virtualMachine(), underlying().subclasses());
    }

    public FXClassType superclass() {
        return FXWrapper.wrap(virtualMachine(), underlying().superclass());
    }

    @Override
    protected ClassType underlying() {
        return (ClassType) super.underlying();
    }

    /**
     * JDI addition: Determines if this is a JavaFX class.
     *
     * @return <code>true</code> if this is a JavaFX class; false otherwise.
     */
    @Override
    public boolean isJavaFXType() {
        if (!isIsFxTypeSet) {
            isIsFxTypeSet = true;
            FXVirtualMachine fxvm = virtualMachine();
            InterfaceType fxObjType = (InterfaceType) FXWrapper.unwrap(fxvm.fxObjectType());
            if (fxObjType != null) {
                ClassType thisType = underlying();
                List<InterfaceType> allIfaces = thisType.allInterfaces();
                for (InterfaceType iface : allIfaces) {
                    if (iface.equals(fxObjType)) {
                        isFXType = true;
                        break;
                    }
                }
            }
        }
        return isFXType;
    }
}