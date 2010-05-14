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

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.ClassObjectReference;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sundar
 */
public class FXReferenceType extends FXType implements ReferenceType {
    public FXReferenceType(FXVirtualMachine fxvm, ReferenceType underlying) {
        super(fxvm, underlying);
    }

    public byte[] constantPool() {
        return underlying().constantPool();
    }

    public int constantPoolCount() {
        return underlying().constantPoolCount();
    }

    public List<ObjectReference> instances(long count) {
        return FXWrapper.wrapObjectReferences(virtualMachine(), underlying().instances(count));
    }

    public int majorVersion() {
        return underlying().majorVersion();
    }

    public int minorVersion() {
        return underlying().minorVersion();
    }

    public List<Field> allFields() {
        return FXWrapper.wrapFields(virtualMachine(), underlying().allFields());
    }

    public List<Location> allLineLocations() throws AbsentInformationException {
        return FXWrapper.wrapLocations(virtualMachine(), underlying().allLineLocations());
    }

    public List<Location> allLineLocations(String stratum, String sourceName) throws AbsentInformationException {
        return FXWrapper.wrapLocations(virtualMachine(), underlying().allLineLocations(stratum, sourceName));
    }

    public List<Method> allMethods() {
        return FXWrapper.wrapMethods(virtualMachine(), underlying().allMethods());
    }

    public List<String> availableStrata() {
        return underlying().availableStrata();
    }

    public ClassLoaderReference classLoader() {
        return FXWrapper.wrap(virtualMachine(), underlying().classLoader());
    }

    public ClassObjectReference classObject() {
        return FXWrapper.wrap(virtualMachine(), underlying().classObject());
    }

    public String defaultStratum() {
        return underlying().defaultStratum();
    }

    public boolean failedToInitialize() {
        return underlying().failedToInitialize();
    }

    // return null if there is no field or the name is ambigous. 
    public FXField fieldByName(String name) {
        // There could be both an FX field $xxx and a java field xxx
        Field javaField = underlying().fieldByName(name);
        Field fxField = underlying().fieldByName("$" + name);
        if (javaField == null) {
            if (fxField == null ) {
                return null;
            }
            // we'll return fxField
        } else {
            if (fxField != null) {
                // we found both name and $name
                return null;
            }
            fxField = javaField;
        }
        return FXWrapper.wrap(virtualMachine(), fxField);
    }

    public List<Field> fields() {
        return FXWrapper.wrapFields(virtualMachine(), underlying().fields());
    }

    public String genericSignature() {
        return underlying().genericSignature();
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

    public boolean isAbstract() {
        return underlying().isAbstract();
    }

    public boolean isFinal() {
        return underlying().isFinal();
    }

    public boolean isInitialized() {
        return underlying().isInitialized();
    }

    public boolean isPrepared() {
        return underlying().isPrepared();
    }

    public boolean isStatic() {
        return underlying().isStatic();
    }

    public boolean isVerified() {
        return underlying().isVerified();
    }

    public List<Location> locationsOfLine(int lineNumber) throws AbsentInformationException {
        return FXWrapper.wrapLocations(virtualMachine(), underlying().locationsOfLine(lineNumber));
    }

    public List<Location> locationsOfLine(String stratum, String sourceName, int line) throws AbsentInformationException {
        return FXWrapper.wrapLocations(virtualMachine(), underlying().locationsOfLine(stratum, sourceName, line));
    }

    public List<Method> methods() {
        return FXWrapper.wrapMethods(virtualMachine(), underlying().methods());
    }

    public List<Method> methodsByName(String name) {
        return FXWrapper.wrapMethods(virtualMachine(), underlying().methodsByName(name));
    }

    public List<Method> methodsByName(String name, String signature) {
        return FXWrapper.wrapMethods(virtualMachine(), underlying().methodsByName(name, signature));
    }

    public List<ReferenceType> nestedTypes() {
        return FXWrapper.wrapReferenceTypes(virtualMachine(), underlying().nestedTypes());
    }

    public String sourceDebugExtension() throws AbsentInformationException {
        return underlying().sourceDebugExtension();
    }

    public String sourceName() throws AbsentInformationException {
        return underlying().sourceName();
    }

    public List<String> sourceNames(String stratum) throws AbsentInformationException {
        return underlying().sourceNames(stratum);
    }

    public List<String> sourcePaths(String stratum) throws AbsentInformationException {
        return underlying().sourcePaths(stratum);
    }

    public List<Field> visibleFields() {
        return FXWrapper.wrapFields(virtualMachine(), underlying().visibleFields());
    }

    public List<Method> visibleMethods() {
        return FXWrapper.wrapMethods(virtualMachine(), underlying().visibleMethods());
    }

    public int compareTo(ReferenceType o) {
        return underlying().compareTo(FXWrapper.unwrap(o));
    }

    public boolean isPackagePrivate() {
        return underlying().isPackagePrivate();
    }

    public boolean isPrivate() {
        return underlying().isPrivate();
    }

    public boolean isProtected() {
        return underlying().isProtected();
    }

    public boolean isPublic() {
        return underlying().isPublic();
    }

    public int modifiers() {
        return underlying().modifiers();
    }

    @Override
    protected ReferenceType underlying() {
        return (ReferenceType) super.underlying();
    }

    protected boolean isJavaFXType() {
        return false;
    }
}
