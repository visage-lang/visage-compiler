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

import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ShortValue;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Represents a FX sequence (instanceof com.sun.javafx.runtime.sequence.Sequence).
 *
 * @author sundar
 */
public class FXSequenceReference extends FXObjectReference {
    public FXSequenceReference(FXVirtualMachine fxvm, ObjectReference underlying) {
        super(fxvm, underlying);
    }

    public int size()
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method sizeMethod = virtualMachine().fxSequenceType().sizeMethod();
        Value value = invokeMethod(virtualMachine().uiThread(), sizeMethod, Collections.EMPTY_LIST, 0);
        return ((IntegerValue)value).intValue();
    }

    public Value get(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getMethod = virtualMachine().fxSequenceType().getMethod();
        return getElement(getMethod, index);
    }

    public BooleanValue getAsBoolean(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsBooleanMethod = virtualMachine().fxSequenceType().getAsBooleanMethod();
        return (BooleanValue) getElement(getAsBooleanMethod, index);
    }

    public CharValue getAsChar(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsCharMethod = virtualMachine().fxSequenceType().getAsBooleanMethod();
        return (CharValue) getElement(getAsCharMethod, index);
    }

    public ByteValue getAsByte(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsByteMethod = virtualMachine().fxSequenceType().getAsByteMethod();
        return (ByteValue) getElement(getAsByteMethod, index);
    }

    public ShortValue getAsShort(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsShortMethod = virtualMachine().fxSequenceType().getAsShortMethod();
        return (ShortValue) getElement(getAsShortMethod, index);
    }

    public IntegerValue getAsInt(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsIntMethod = virtualMachine().fxSequenceType().getAsIntMethod();
        return (IntegerValue) getElement(getAsIntMethod, index);
    }
    
    public LongValue getAsLong(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsLongMethod = virtualMachine().fxSequenceType().getAsLongMethod();
        return (LongValue) getElement(getAsLongMethod, index);
    }
    
    public FloatValue getAsFloat(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsFloatMethod = virtualMachine().fxSequenceType().getAsFloatMethod();
        return (FloatValue) getElement(getAsFloatMethod, index);
    }
    
    public DoubleValue getAsDouble(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsDoubleMethod = virtualMachine().fxSequenceType().getAsDoubleMethod();
        return (DoubleValue) getElement(getAsDoubleMethod, index);
    }

    public Value getElementType()
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getElementTypeMethod = virtualMachine().fxSequenceType().getElementTypeMethod();
        return invokeMethod(virtualMachine().uiThread(), getElementTypeMethod, Collections.EMPTY_LIST, 0);
    }

    private Value getElement(Method method, int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        List<Value> args = new ArrayList<Value>(1);
        args.add(virtualMachine().mirrorOf(index));
        return invokeMethod(virtualMachine().uiThread(), method, args, 0);
    }
}