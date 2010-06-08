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
import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.LongValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
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
    // keep this in sync. with com.sun.javafx.runtime.TypeInfo.Types enum.
    public enum Types { INT, FLOAT, OBJECT, DOUBLE, BOOLEAN, LONG, SHORT, BYTE, CHAR, OTHER }

    // element type of this sequence
    private Types elementType;

    public FXSequenceReference(FXVirtualMachine fxvm, ObjectReference underlying) {
        super(fxvm, underlying);
    }

    public Types getElementType()
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        if (elementType == null) {
            Method getElementTypeMethod = virtualMachine().fxSequenceType().getElementTypeMethod();
            Value typeInfo = invokeMethod(virtualMachine().uiThread(), getElementTypeMethod, Collections.EMPTY_LIST, 0);
            elementType = typesFromTypeInfo((ObjectReference)typeInfo);
        }
        return elementType;
    }

    public int size()
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method sizeMethod = virtualMachine().fxSequenceType().sizeMethod();
        Value value = invokeMethod(virtualMachine().uiThread(), sizeMethod, Collections.EMPTY_LIST, 0);
        return ((IntegerValue)value).intValue();
    }

    // synonym for size
    public int length()
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        return size();
    }

    public Value getValue(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Types type = getElementType();
        switch (type) {
            case INT:
                return getValueAsInt(index);
            case FLOAT:
                return getValueAsFloat(index);
            case OBJECT:
                return getValueAsObject(index);
            case DOUBLE:
                return getValueAsDouble(index);
            case BOOLEAN:
                return getValueAsBoolean(index);
            case LONG:
                return getValueAsLong(index);
            case SHORT:
                return getValueAsShort(index);
            case BYTE:
                return getValueAsByte(index);
            case CHAR:
                return getValueAsChar(index);
            case OTHER:
                return getValueAsObject(index);
            default:
                throw new IllegalArgumentException("Invalid sequence element type");
        }
    }

    public FXSequenceReference setValue(int index, Value value)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Types type = getElementType();
        switch (type) {
            case INT:
                return setIntValue(index, (IntegerValue)value);
            case FLOAT:
                return setFloatValue(index, (FloatValue)value);
            case OBJECT:
                return setObjectValue(index, (ObjectReference)value);
            case DOUBLE:
                return setDoubleValue(index, (DoubleValue)value);
            case BOOLEAN:
                return setBooleanValue(index, (BooleanValue)value);
            case LONG:
                return setLongValue(index, (LongValue)value);
            case SHORT:
                return setShortValue(index, (ShortValue)value);
            case BYTE:
                return setByteValue(index, (ByteValue)value);
            case CHAR:
                return setCharValue(index, (CharValue)value);
            case OTHER:
                return setObjectValue(index, (ObjectReference)value);
            default:
                throw new IllegalArgumentException("Invalid sequence element type");
        }
    }

    public BooleanValue getValueAsBoolean(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsBooleanMethod = virtualMachine().fxSequenceType().getAsBooleanMethod();
        return (BooleanValue) getElement(getAsBooleanMethod, index);
    }

    public CharValue getValueAsChar(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsCharMethod = virtualMachine().fxSequenceType().getAsCharMethod();
        return (CharValue) getElement(getAsCharMethod, index);
    }

    public ByteValue getValueAsByte(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsByteMethod = virtualMachine().fxSequenceType().getAsByteMethod();
        return (ByteValue) getElement(getAsByteMethod, index);
    }

    public ShortValue getValueAsShort(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsShortMethod = virtualMachine().fxSequenceType().getAsShortMethod();
        return (ShortValue) getElement(getAsShortMethod, index);
    }

    public IntegerValue getValueAsInt(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsIntMethod = virtualMachine().fxSequenceType().getAsIntMethod();
        return (IntegerValue) getElement(getAsIntMethod, index);
    }
    
    public LongValue getValueAsLong(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsLongMethod = virtualMachine().fxSequenceType().getAsLongMethod();
        return (LongValue) getElement(getAsLongMethod, index);
    }
    
    public FloatValue getValueAsFloat(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsFloatMethod = virtualMachine().fxSequenceType().getAsFloatMethod();
        return (FloatValue) getElement(getAsFloatMethod, index);
    }
    
    public DoubleValue getValueAsDouble(int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getAsDoubleMethod = virtualMachine().fxSequenceType().getAsDoubleMethod();
        return (DoubleValue) getElement(getAsDoubleMethod, index);
    }
    
    public ObjectReference getValueAsObject(int index)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method getMethod = virtualMachine().fxSequenceType().getMethod();
        return (ObjectReference) getElement(getMethod, index);
    }

    public FXSequenceReference setIntValue(int index, IntegerValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setIntElementMethod = virtualMachine().fxSequencesType().setIntElementMethod();
        return setElement(setIntElementMethod, index, value);
    }

    public FXSequenceReference setFloatValue(int index, FloatValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setFloatElementMethod = virtualMachine().fxSequencesType().setFloatElementMethod();
        return setElement(setFloatElementMethod, index, value);
    }

    public FXSequenceReference setObjectValue(int index, ObjectReference value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setObjectElementMethod = virtualMachine().fxSequencesType().setObjectElementMethod();
        return setElement(setObjectElementMethod, index, value);
    }

    public FXSequenceReference setDoubleValue(int index, DoubleValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setDoubleElementMethod = virtualMachine().fxSequencesType().setDoubleElementMethod();
        return setElement(setDoubleElementMethod, index, value);
    }

    public FXSequenceReference setBooleanValue(int index, BooleanValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setBooleanElementMethod = virtualMachine().fxSequencesType().setBooleanElementMethod();
        return setElement(setBooleanElementMethod, index, value);
    }

    public FXSequenceReference setLongValue(int index, LongValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setLongElementMethod = virtualMachine().fxSequencesType().setLongElementMethod();
        return setElement(setLongElementMethod, index, value);
    }

    public FXSequenceReference setShortValue(int index, ShortValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setShortElementMethod = virtualMachine().fxSequencesType().setShortElementMethod();
        return setElement(setShortElementMethod, index, value);
    }

    public FXSequenceReference setByteValue(int index, ByteValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setByteElementMethod = virtualMachine().fxSequencesType().setByteElementMethod();
        return setElement(setByteElementMethod, index, value);
    }

    public FXSequenceReference setCharValue(int index, CharValue value)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Method setCharElementMethod = virtualMachine().fxSequencesType().setCharElementMethod();
        return setElement(setCharElementMethod, index, value);
    }

    // Internals only below this point
    private Value getElement(Method method, int index)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        List<Value> args = new ArrayList<Value>(1);
        args.add(virtualMachine().mirrorOf(index));
        return invokeMethod(virtualMachine().uiThread(), method, args, 0);
    }

    private FXSequenceReference setElement(Method method, int index, Value value)
         throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        List<Value> args = new ArrayList<Value>(3);
        args.add(this);
        args.add(value);
        args.add(virtualMachine().mirrorOf(index));
        return (FXSequenceReference) virtualMachine().fxSequencesType().
            invokeMethod(virtualMachine().uiThread(), method, args, 0);
    }

    private Types typesFromTypeInfo(ObjectReference typeInfo) {
        Field typeField = typeInfo.referenceType().fieldByName("type");
        ObjectReference typeValue = (ObjectReference) typeInfo.getValue(typeField);
        Field nameField = typeValue.referenceType().fieldByName("name");
        String typeName = ((StringReference)typeValue.getValue(nameField)).value();
        return Types.valueOf(typeName);
    }
}
