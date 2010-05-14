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
import com.sun.jdi.Value;
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

    public FXValue getValue(Field field) {
        if (!FXUtils.isValid(this, field)) {
            throw new FXInvalidValueException(this, field);
        }
        return FXWrapper.wrap(virtualMachine(), underlying().getValue(FXWrapper.unwrap(field)));
    }

    public Map<Field, Value> getValues(List<? extends Field> fields) {
        Map<Field, Field> fieldMap = new HashMap<Field, Field>();
        List<Field> unwrappedFields = new ArrayList<Field>();
        for (Field field : fields) {
            Field unwrapped = FXWrapper.unwrap(field);
            unwrappedFields.add(unwrapped);
            fieldMap.put(unwrapped, field);
        }
        Map<Field, Value> fieldValues = underlying().getValues(unwrappedFields);
        Map<Field, Value> result = new HashMap<Field, Value>();
        for (Map.Entry<Field, Value> entry: fieldValues.entrySet()) {
            result.put(fieldMap.get(entry.getKey()), entry.getValue());
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

    public void setValue(Field field, Value value) throws InvalidTypeException, ClassNotLoadedException {
        underlying().setValue(FXWrapper.unwrap(field), FXWrapper.unwrap(value));
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
