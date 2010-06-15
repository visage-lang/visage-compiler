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
import com.sun.jdi.ClassType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a FX sequence (instanceof com.sun.javafx.runtime.sequence.Sequence).
 * For now, class provides ArrayReference-like interface for Sequences - in future
 * we'll add more operations like sequence insert/delete/slice etc.
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

    public Types getElementType() {
        if (elementType == null) {
            Method getElementTypeMethod = virtualMachine().fxSequenceType().getElementTypeMethod();
            Exception theExc = null;
            try {
                Value typeInfo = invokeMethod(virtualMachine().uiThread(), getElementTypeMethod, Collections.EMPTY_LIST, ClassType.INVOKE_SINGLE_THREADED);
                elementType = typesFromTypeInfo((ObjectReference)typeInfo);
            } catch(InvalidTypeException ee) {
                theExc = ee;
            } catch(ClassNotLoadedException ee) {
                theExc = ee;
            } catch(IncompatibleThreadStateException ee) {
                theExc = ee;
            } catch(InvocationException ee) {
                theExc = ee;
            }
            if (theExc != null) {
                virtualMachine().setLastFieldAccessException(theExc);
            }
        }
        return elementType;
    }

    public int size() {
        Method sizeMethod = virtualMachine().fxSequenceType().sizeMethod();
        Exception theExc = null;
        try {
            Value value = invokeMethod(virtualMachine().uiThread(), sizeMethod, Collections.EMPTY_LIST, ClassType.INVOKE_SINGLE_THREADED);
            return ((IntegerValue)value).intValue();
        } catch(InvalidTypeException ee) {
            theExc = ee;
        } catch(ClassNotLoadedException ee) {
            theExc = ee;
        } catch(IncompatibleThreadStateException ee) {
            theExc = ee;
        } catch(InvocationException ee) {
            theExc = ee;
        }
        virtualMachine().setLastFieldAccessException(theExc);
        return 0;
    }

    // synonym for size
    public int length() {
        return size();
    }

    public Value getValue(int index) {
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

    public List<Value> getValues(int index, int length) {
        List<Value> values = new ArrayList<Value>(length);
        for (int i = 0; i < length; i++) {
            values.add(getValue(index + i));
        }
        return values;
    }

    public FXSequenceReference setValue(int index, Value value) {
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

    public FXSequenceReference setValues(List<? extends Value> values) {
        final int len = length();
        FXSequenceReference result = null;
        Iterator<? extends Value> valuesItr = values.iterator();
        for (int i = 0; i < len; i++) {
            if (! valuesItr.hasNext()) {
                break;
            }
            result = setValue(i, valuesItr.next());
        }
        return result;
    }

    // Internals only below this point
    private BooleanValue getValueAsBoolean(int index) {
        Method getAsBooleanMethod = virtualMachine().fxSequenceType().getAsBooleanMethod();
        return (BooleanValue) getElement(getAsBooleanMethod, index);
    }

    private CharValue getValueAsChar(int index) {
        Method getAsCharMethod = virtualMachine().fxSequenceType().getAsCharMethod();
        return (CharValue) getElement(getAsCharMethod, index);
    }

    private ByteValue getValueAsByte(int index) {
        Method getAsByteMethod = virtualMachine().fxSequenceType().getAsByteMethod();
        return (ByteValue) getElement(getAsByteMethod, index);
    }

    private ShortValue getValueAsShort(int index) {
        Method getAsShortMethod = virtualMachine().fxSequenceType().getAsShortMethod();
        return (ShortValue) getElement(getAsShortMethod, index);
    }

    private IntegerValue getValueAsInt(int index) {
        Method getAsIntMethod = virtualMachine().fxSequenceType().getAsIntMethod();
        return (IntegerValue) getElement(getAsIntMethod, index);
    }
    
    private LongValue getValueAsLong(int index) {
        Method getAsLongMethod = virtualMachine().fxSequenceType().getAsLongMethod();
        return (LongValue) getElement(getAsLongMethod, index);
    }
    
    private FloatValue getValueAsFloat(int index) {
        Method getAsFloatMethod = virtualMachine().fxSequenceType().getAsFloatMethod();
        return (FloatValue) getElement(getAsFloatMethod, index);
    }
    
    private DoubleValue getValueAsDouble(int index) {
        Method getAsDoubleMethod = virtualMachine().fxSequenceType().getAsDoubleMethod();
        return (DoubleValue) getElement(getAsDoubleMethod, index);
    }
    
    private ObjectReference getValueAsObject(int index) {
        Method getMethod = virtualMachine().fxSequenceType().getMethod();
        return (ObjectReference) getElement(getMethod, index);
    }

    private FXSequenceReference setIntValue(int index, IntegerValue value) {
        Method setIntElementMethod = virtualMachine().fxSequencesType().setIntElementMethod();
        return setElement(setIntElementMethod, index, value);
    }

    private FXSequenceReference setFloatValue(int index, FloatValue value) {
        Method setFloatElementMethod = virtualMachine().fxSequencesType().setFloatElementMethod();
        return setElement(setFloatElementMethod, index, value);
    }

    private FXSequenceReference setObjectValue(int index, ObjectReference value) {
        Method setObjectElementMethod = virtualMachine().fxSequencesType().setObjectElementMethod();
        return setElement(setObjectElementMethod, index, value);
    }

    private FXSequenceReference setDoubleValue(int index, DoubleValue value) {
        Method setDoubleElementMethod = virtualMachine().fxSequencesType().setDoubleElementMethod();
        return setElement(setDoubleElementMethod, index, value);
    }

    private FXSequenceReference setBooleanValue(int index, BooleanValue value) {
        Method setBooleanElementMethod = virtualMachine().fxSequencesType().setBooleanElementMethod();
        return setElement(setBooleanElementMethod, index, value);
    }

    private FXSequenceReference setLongValue(int index, LongValue value) {
        Method setLongElementMethod = virtualMachine().fxSequencesType().setLongElementMethod();
        return setElement(setLongElementMethod, index, value);
    }

    private FXSequenceReference setShortValue(int index, ShortValue value) {
        Method setShortElementMethod = virtualMachine().fxSequencesType().setShortElementMethod();
        return setElement(setShortElementMethod, index, value);
    }

    private FXSequenceReference setByteValue(int index, ByteValue value) {
        Method setByteElementMethod = virtualMachine().fxSequencesType().setByteElementMethod();
        return setElement(setByteElementMethod, index, value);
    }

    private FXSequenceReference setCharValue(int index, CharValue value) {
        Method setCharElementMethod = virtualMachine().fxSequencesType().setCharElementMethod();
        return setElement(setCharElementMethod, index, value);
    }

    // Internals only below this point
    private Value getElement(Method method, int index) {
        List<Value> args = new ArrayList<Value>(1);
        args.add(virtualMachine().mirrorOf(index));
        Exception theExc;
        try {
            return invokeMethod(virtualMachine().uiThread(), method, args, ClassType.INVOKE_SINGLE_THREADED);
        } catch(InvalidTypeException ee) {
            theExc = ee;
        } catch(ClassNotLoadedException ee) {
            theExc = ee;
        } catch(IncompatibleThreadStateException ee) {
            theExc = ee;
        } catch(InvocationException ee) {
            theExc = ee;
        }

        virtualMachine().setLastFieldAccessException(theExc);
        return defaultValue(getElementType());
    }

    private FXSequenceReference setElement(Method method, int index, Value value) {
        List<Value> args = new ArrayList<Value>(3);
        args.add(this);
        args.add(value);
        args.add(virtualMachine().mirrorOf(index));
        Exception theExc;
        try {
            return (FXSequenceReference) virtualMachine().fxSequencesType().
                invokeMethod(virtualMachine().uiThread(), method, args, ClassType.INVOKE_SINGLE_THREADED);
        } catch(InvalidTypeException ee) {
            theExc = ee;
        } catch(ClassNotLoadedException ee) {
            theExc = ee;
        } catch(IncompatibleThreadStateException ee) {
            theExc = ee;
        } catch(InvocationException ee) {
            theExc = ee;
        }

        virtualMachine().setLastFieldAccessException(theExc);
        return this;
    }

    private Types typesFromTypeInfo(ObjectReference typeInfo) {
        Field typeField = typeInfo.referenceType().fieldByName("type");
        ObjectReference typeValue = (ObjectReference) typeInfo.getValue(typeField);
        Field nameField = typeValue.referenceType().fieldByName("name");
        String typeName = ((StringReference)typeValue.getValue(nameField)).value();
        return Types.valueOf(typeName);
    }

    private Value defaultValue(Types type) {
        FXVirtualMachine fxvm = virtualMachine();
        switch (type) {
            case BOOLEAN:
                return fxvm.booleanDefaultValue();
            case BYTE:
                return fxvm.byteDefaultValue();
            case CHAR:
                return fxvm.charDefaultValue();
            case DOUBLE:
                return fxvm.doubleDefaultValue();
            case FLOAT:
                return fxvm.floatDefaultValue();
            case INT:
                return fxvm.integerDefaultValue();
            case LONG:
                return fxvm.longDefaultValue();
            case SHORT:
                return fxvm.shortDefaultValue();
            default:
                return null;
        }
    }
}
