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
 * Represents an Visage sequence (instanceof org.visage.runtime.sequence.Sequence).
 * For now, this class provides an ArrayReference-like interface for Sequences - in the future
 * we'll add more operations such as sequence insert/delete/slice etc.
 *
 * @author sundar
 */
public class VisageSequenceReference extends VisageObjectReference {
    
    // keep this in sync. with org.visage.runtime.TypeInfo.Types enum.
    /**
     * The possible types of a sequence element
     */
    public enum Types { INT, FLOAT, OBJECT, DOUBLE, BOOLEAN, LONG, SHORT, BYTE, CHAR, OTHER }

    // element type of this sequence
    private Types elementType;

    public VisageSequenceReference(VisageVirtualMachine fxvm, ObjectReference underlying) {
        super(fxvm, underlying);
    }

    /**
     * Returns the element type of this sequence.
     *
     * @return the type of this sequences's elements
     */
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

    /**
     * Returns the number of elements in this sequence
     *
     * @return the integer count of elements in this sequence.
     */
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

    /**
     * Returns the number of elements in this sequence.
     * (This is a synonym for size().)
     *
     * @return the integer count of elements in this sequence.
     */
    public int length() {
        return size();
    }

    /**
     * Returns the value of a sequence element.
     *
     * @param index the index of the element to retrieve
     * @return the {@link Value} at the given index, or the default value for
     * the sequence's element type 
     * if <CODE><I>index</I></CODE> is outside the range of this sequence,
     * that is, if either of the following are true:
     * <PRE>
     *    <I>index</I> &lt; 0
     *    <I>index</I> &gt;= {@link #length() length()} </PRE>
     */
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

    /**
     * Returns a range of sequence elements.
     *
     * @param index the index of the first element to retrieve
     * @param length the number of elements to retrieve, or -1 to
     * retrieve all elements to the end of this sequence.
     * @return a list of {@link Value} objects, one for each requested
     * sequence element ordered by array index.  When there are
     * no elements in the specified range (e.g.
     * <CODE><I>length</I></CODE> is zero) an empty list is returned
     * Returns the default value for the sequence's element type for indicies in the
     * specified range that are outside the range of the sequence.
     */
    public List<Value> getValues(int index, int length) {
        List<Value> values = new ArrayList<Value>(length);
        for (int i = 0; i < length; i++) {
            values.add(getValue(index + i));
        }
        return values;
    }
    
    /**
     * Replace a sequence element with another value.
     *
     * Object values must be assignment compatible with the element type.
     * (This implies that the component type must be loaded through the
     * declaring class's class loader). Primitive values must be
     * assignment compatible with the component type.
     *
     * @param value the new value
     * @param index the index of the component to set.  If this is beyond the 
     * end of the sequence, the new value is appended to the sequence.
     *
     * @throws InvalidTypeException if the type of <CODE><I>value</I></CODE>
     * is not compatible with the declared type of sequence elements.
     * @throws ClassNotLoadedException if the sequence element type
     * has not yet been loaded through the appropriate class loader.
     * @throws VMCannotBeModifiedException if the VirtualMachine is read-only - see {@link com.sun.jdi.VirtualMachine#canBeModified()}.
     * @return a new sequence with the specified element replaced/added.
     */
    public VisageSequenceReference setValue(int index, Value value) {
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

    /** 
     * Return a sequence which is a copy of this sequence with the first {@link #length()} elements
     * replaced by the elements in <CODE><I>values</I></CODE>
     *
     * @throws InvalidTypeException if the type of an element of <CODE><I>values</I></CODE>
     * is not compatible with the declared type of sequence elements.
     *
     * @return a copy of this sequence with the first {@link #length()} elements replaced by the
     * elements of <CODE><I>values</I></CODE>
     */
    public VisageSequenceReference setValues(List<? extends Value> values) {
        final int len = length();
        VisageSequenceReference result = null;
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

    private VisageSequenceReference setIntValue(int index, IntegerValue value) {
        Method setIntElementMethod = virtualMachine().fxSequencesType().setIntElementMethod();
        return setElement(setIntElementMethod, index, value);
    }

    private VisageSequenceReference setFloatValue(int index, FloatValue value) {
        Method setFloatElementMethod = virtualMachine().fxSequencesType().setFloatElementMethod();
        return setElement(setFloatElementMethod, index, value);
    }

    private VisageSequenceReference setObjectValue(int index, ObjectReference value) {
        Method setObjectElementMethod = virtualMachine().fxSequencesType().setObjectElementMethod();
        return setElement(setObjectElementMethod, index, value);
    }

    private VisageSequenceReference setDoubleValue(int index, DoubleValue value) {
        Method setDoubleElementMethod = virtualMachine().fxSequencesType().setDoubleElementMethod();
        return setElement(setDoubleElementMethod, index, value);
    }

    private VisageSequenceReference setBooleanValue(int index, BooleanValue value) {
        Method setBooleanElementMethod = virtualMachine().fxSequencesType().setBooleanElementMethod();
        return setElement(setBooleanElementMethod, index, value);
    }

    private VisageSequenceReference setLongValue(int index, LongValue value) {
        Method setLongElementMethod = virtualMachine().fxSequencesType().setLongElementMethod();
        return setElement(setLongElementMethod, index, value);
    }

    private VisageSequenceReference setShortValue(int index, ShortValue value) {
        Method setShortElementMethod = virtualMachine().fxSequencesType().setShortElementMethod();
        return setElement(setShortElementMethod, index, value);
    }

    private VisageSequenceReference setByteValue(int index, ByteValue value) {
        Method setByteElementMethod = virtualMachine().fxSequencesType().setByteElementMethod();
        return setElement(setByteElementMethod, index, value);
    }

    private VisageSequenceReference setCharValue(int index, CharValue value) {
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

    private VisageSequenceReference setElement(Method method, int index, Value value) {
        List<Value> args = new ArrayList<Value>(3);
        args.add(this);
        args.add(value);
        args.add(virtualMachine().mirrorOf(index));
        Exception theExc;
        try {
            return (VisageSequenceReference) virtualMachine().fxSequencesType().
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
        VisageVirtualMachine fxvm = virtualMachine();
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
