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
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import com.sun.jdi.ShortValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class FXObjectReference extends FXValue implements ObjectReference {
    public FXObjectReference(FXVirtualMachine fxvm, ObjectReference underlying) {
        super(fxvm, underlying);
    }

    public List<ObjectReference> referringObjects(long count) {
        return FXWrapper.wrapObjectReferences(virtualMachine(), underlying().referringObjects(count));
    }

    public void disableCollection() {
        underlying().disableCollection();
    }

    public void enableCollection() {
        underlying().enableCollection();
    }

    public int entryCount() throws IncompatibleThreadStateException {
        return underlying().entryCount();
    }

    public int getFlagWord(Field field) {
        FXReferenceType clazz = (FXReferenceType)referenceType();
        // could this be a java field inherited by an fx class??
        if (!clazz.isJavaFXType()) {
            return 0;
        }
        Field jdiField = FXWrapper.unwrap(field); 
        String jdiFieldName = jdiField.name();
        String vflgFieldName = "VFLG" + jdiFieldName;

        Field  vflgField = clazz.fieldByName(vflgFieldName);
        if (vflgField == null) {
            // not all fields have a VFLG, eg, a private field that isn't accessed
            return 0;
        }
        Value vflgValue = underlying().getValue(FXWrapper.unwrap(vflgField));
        return((ShortValue)vflgValue).value();
    }

    private boolean areFlagBitsSet(Field field, int mask) {
        return (getFlagWord(field) & mask) == mask;
    }

    private Map<Field, Value> getValuesCommon(List<? extends Field> wrappedFields, boolean doInvokes) throws
        InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        // We will find fields which have no getters, and call the underlying
        // getValues to get values for all of them in one fell swoop.
        Map<Field, Field> unwrappedToWrappedMap = new HashMap<Field, Field>();
        List<Field> noGetterUnwrappedFields = new ArrayList<Field>();    // fields that don't have getters

        // For fields that do have getters, we will just return VoidValue for them if
        // or we will call FXGetValue for each, depending on doInvokes
        Map<Field, Value> result = new HashMap<Field, Value>();
        FXReferenceType wrappedClass = (FXReferenceType)referenceType();
        ReferenceType unwrappedClass = FXWrapper.unwrap(wrappedClass);

        // Create the above Maps and lists
        for (Field wrappedField : wrappedFields) {
            Field unwrapped = FXWrapper.unwrap(wrappedField);
            if (wrappedClass.isJavaFXType()) {
                List<Method> mth = unwrappedClass.methodsByName("get" + unwrapped.name());
                if (mth.size() == 0) {
                    // no getter
                    unwrappedToWrappedMap.put(unwrapped, wrappedField);
                    noGetterUnwrappedFields.add(unwrapped);
                } else {
                    // field has a getter
                    if (doInvokes) {
                        result.put(wrappedField, FXGetValue(wrappedField));
                    } else {
                        result.put(wrappedField, virtualMachine().voidValue());
                    }
                }
            } else {
                unwrappedToWrappedMap.put(unwrapped, wrappedField);
                noGetterUnwrappedFields.add(unwrapped);
            }                
        }

        // Get values for all the noGetter fields.  Note that this gets them in a single JDWP trip
        Map<Field, Value> unwrappedFieldValues = underlying().getValues(noGetterUnwrappedFields);

        // for each input Field, create a result map entry with that field as the
        // key, and the value returned by getValues, or null if the field is invalid.

        // Make a pass over the unwrapped no getter fields and for each, put its
        // wrapped version, and wrapped value into the result Map.
        for (Map.Entry<Field, Field> unwrappedEntry: unwrappedToWrappedMap.entrySet()) {
            Field wrappedField = unwrappedEntry.getValue();
            Value resultValue = FXWrapper.wrap(virtualMachine(), 
                                             unwrappedFieldValues.get(unwrappedEntry.getKey()));
            result.put(wrappedField, resultValue);
        }
        return result;
    }

    /**
     * Extension to JDI
     */
    public boolean isReadOnly(Field field) {
        return areFlagBitsSet(field, virtualMachine().FXReadOnlyFlagMask());
    }

    /**
     * Extension to JDI
     */
    public boolean isInvalid(Field field) {
        return areFlagBitsSet(field, virtualMachine().FXInvalidFlagMask());
    }

    /**
     * Extension to JDI
     */
    public boolean isBound(Field field) {
        return areFlagBitsSet(field, virtualMachine().FXBoundFlagMask());
    }

    /**
     * JDI Extension:  This will call the getter if one exists
     */
    public FXValue FXGetValue(Field field) throws
        InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Field jdiField = FXWrapper.unwrap(field);
        FXReferenceType clazz = (FXReferenceType)referenceType();
        if (!clazz.isJavaFXType()) {
            return FXWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }

        //get$xxxx methods exist for fields except private fields which have no binders
        List<Method> mth = FXWrapper.unwrap(clazz).methodsByName("get" + jdiField.name());
        if (mth.size() == 0) {
            return FXWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }
        return invokeMethod(virtualMachine().uiThread(), mth.get(0), new ArrayList<Value>(0), 0);
    }

    /**
     * Extension to JDI:  This will call the getter for a field if there is one
     */
    public Map<Field, Value> FXGetValues(List<? extends Field> wrappedFields) throws
        InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {

        return getValuesCommon(wrappedFields, true);
    }

    /**
     * Extension to JDI:  Return VoidValue if the field has a setter
     */
    public FXValue getValue(Field field) {
        Field jdiField = FXWrapper.unwrap(field);
        FXReferenceType wrappedClass = (FXReferenceType)referenceType();
        if (!wrappedClass.isJavaFXType()) {
            return FXWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }

        //get$xxxx methods exist for fields except private fields which have no binders
        ReferenceType unwrappedClass = FXWrapper.unwrap(referenceType());
        List<Method> mth = unwrappedClass.methodsByName("get" + jdiField.name());

        if (mth.size() == 0) {
            return FXWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }
        return virtualMachine().voidValue();
    }

    /**
     * JDI Extension: This will return VoidValue for a field that has a getter
     */
    public Map<Field, Value> getValues(List<? extends Field> wrappedFields) {
        
        Map<Field,Value> result = null;
        try {
            // this call does no invoke, so none of the exceptions are thrown
            result = getValuesCommon(wrappedFields, false);
        } catch(InvalidTypeException ee) {
        } catch(ClassNotLoadedException ee) {
        } catch(IncompatibleThreadStateException ee) {
        } catch(InvocationException ee) {
        }
        return result;
    }

    public FXValue invokeMethod(ThreadReference thread, Method method, List<? extends Value> values, int options)
            throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Value value =
                underlying().invokeMethod(
                    FXWrapper.unwrap(thread), FXWrapper.unwrap(method),
                    FXWrapper.unwrapValues(values), options);
        return FXWrapper.wrap(virtualMachine(), value);
    }

    public boolean isCollected() {
        return underlying().isCollected();
    }

    public FXThreadReference owningThread() throws IncompatibleThreadStateException {
        return FXWrapper.wrap(virtualMachine(), underlying().owningThread());
    }

    public FXReferenceType referenceType() {
        return FXWrapper.wrap(virtualMachine(), underlying().referenceType());
    }

    /**
     * JDI extension:  This throws IllegalArgumentException if field has a setter method
     */
    private void setValueCommon(Field field, Value value, boolean invokeAllowed)
        throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        Field jdiField = FXWrapper.unwrap(field);
        Value jdiValue = FXWrapper.unwrap(value);
        FXReferenceType clazz = (FXReferenceType)referenceType();
        if (!clazz.isJavaFXType()) {
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
        List<Method> mth = FXWrapper.unwrap(clazz).methodsByName("set" + jdiField.name());
        if (mth.size() == 0) {
            // there is no setter
            underlying().setValue(jdiField, jdiValue);
            return;
        }
        // there is a setter
        if (!invokeAllowed) {
            throw new IllegalArgumentException("Error: FX field " + field + " has a setter; call FXSetValue instead of setValue");
        }
        ArrayList<Value> args = new ArrayList<Value>(1);
        args.add(jdiValue);
        invokeMethod(virtualMachine().uiThread(), mth.get(0), args, 0);
    }

    /**
     * JDI extension:  This will call a setter if one exists.  It uses invokeMethod to do this
     * so it can throw the same exceptions as does invokeMethod.
     */
    public void FXSetValue(Field field, Value value) throws 
        InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException {
        setValueCommon(field, value, true);
    }

    /**
     * JDI extension:  This throws IllegalArgumentException if field is read only or has a setter method
     */
    public void setValue(Field field, Value value) throws InvalidTypeException, ClassNotLoadedException {
        try {
            setValueCommon(field, value, false);
        } catch (IncompatibleThreadStateException ee) {
            // can't happen
        } catch (InvocationException ee) {
            // can't happen
        }
    }

    public long uniqueID() {
        return underlying().uniqueID();
    }

    public List<ThreadReference> waitingThreads() throws IncompatibleThreadStateException {
        return FXWrapper.wrapThreads(virtualMachine(), underlying().waitingThreads());
    }

    @Override
    protected ObjectReference underlying() {
        return (ObjectReference) super.underlying();
    }
}
