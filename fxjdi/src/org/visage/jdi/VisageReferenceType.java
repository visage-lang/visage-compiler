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

import org.visage.jdi.event.VisageEventQueue;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassLoaderReference;
import com.sun.jdi.ClassObjectReference;
import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ClassType;
import com.sun.jdi.Value;
import com.sun.jdi.ShortValue;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author sundar
 */
public class VisageReferenceType extends VisageType implements ReferenceType {
    public VisageReferenceType(VisageVirtualMachine visagevm, ReferenceType underlying) {
        super(visagevm, underlying);
    }

    public byte[] constantPool() {
        return underlying().constantPool();
    }

    public int constantPoolCount() {
        return underlying().constantPoolCount();
    }

    public List<ObjectReference> instances(long count) {
        return VisageWrapper.wrapObjectReferences(virtualMachine(), underlying().instances(count));
    }

    public int majorVersion() {
        return underlying().majorVersion();
    }

    public int minorVersion() {
        return underlying().minorVersion();
    }

    public List<Field> allFields() {
        return VisageWrapper.wrapFields(virtualMachine(), underlying().allFields());
    }

    public List<Location> allLineLocations() throws AbsentInformationException {
        return VisageWrapper.wrapLocations(virtualMachine(), underlying().allLineLocations());
    }

    public List<Location> allLineLocations(String stratum, String sourceName) throws AbsentInformationException {
        return VisageWrapper.wrapLocations(virtualMachine(), underlying().allLineLocations(stratum, sourceName));
    }

    public List<Method> allMethods() {
        return VisageWrapper.wrapMethods(virtualMachine(), underlying().allMethods());
    }

    public List<String> availableStrata() {
        return underlying().availableStrata();
    }

    public ClassLoaderReference classLoader() {
        return VisageWrapper.wrap(virtualMachine(), underlying().classLoader());
    }

    public ClassObjectReference classObject() {
        return VisageWrapper.wrap(virtualMachine(), underlying().classObject());
    }

    public String defaultStratum() {
        return underlying().defaultStratum();
    }

    public boolean failedToInitialize() {
        return underlying().failedToInitialize();
    }

    // return null if there is no field or the name is ambigous. 
    public VisageField fieldByName(String name) {
        // There could be both an Visage field $xxx and a java field xxx
        Field javaField = underlying().fieldByName(name);
        Field visageField = underlying().fieldByName("$" + name);
        if (javaField == null) {
            if (visageField == null ) {
                // an ivar that is a referenced in an outer class can be prefixed with
                //  'classname$'
                return null;
            }
            // we'll return visageField
        } else {
            if (visageField != null) {
                // we found both name and $name
                return null;
            }
            visageField = javaField;
        }
        return VisageWrapper.wrap(virtualMachine(), visageField);
    }

    public List<Field> fields() {
        return VisageWrapper.wrapFields(virtualMachine(), underlying().fields());
    }

    public String genericSignature() {
        return underlying().genericSignature();
    }

    // The RefType for the ....$Script class for this class if there is one
    private ReferenceType scriptType ;

    public int getFlagWord(Field field) {
        // could this be a java field inherited by an visage class??
        if (!isVisageType()) {
            return 0;
        }
        if (scriptType == null) {
            ReferenceType jdiRefType = underlying();
            String jdiRefTypeName = jdiRefType.name();
            String scriptClassName = jdiRefTypeName;
            int lastDot = scriptClassName.lastIndexOf('.');
            if (lastDot != -1) {
                scriptClassName = scriptClassName.substring(lastDot + 1);
            }
            scriptClassName = jdiRefTypeName + "$" + scriptClassName + "$Script";
            List<ReferenceType> rtx =  virtualMachine().classesByName(scriptClassName);
            if (rtx.size() != 1) {
                System.out.println("--VisageJDI Error: Can't find the class: " + scriptClassName);
                return 0;
            }
            scriptType = rtx.get(0);
        }
        Field jdiField = VisageWrapper.unwrap(field); 
        String jdiFieldName = jdiField.name();
        String vflgFieldName = "VFLG" + jdiFieldName;

        Field  vflgField = scriptType.fieldByName(vflgFieldName);
        if (vflgField == null) {
            // not all fields have a VFLG, eg, a private field that isn't accessed
            return 0;
        }
        Value vflgValue = VisageWrapper.unwrap(scriptType).getValue(VisageWrapper.unwrap(vflgField));
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
        // or we will call VisageGetValue for each, depending on doInvokes
        Map<Field, Value> result = new HashMap<Field, Value>();

        // Create the above Maps and lists
        for (Field wrappedField : wrappedFields) {
            Field unwrapped = VisageWrapper.unwrap(wrappedField);
            if (isVisageType()) {
                List<Method> mth = underlying().methodsByName("get" + unwrapped.name());
                if (mth.size() == 0) {
                    // No getter
                    unwrappedToWrappedMap.put(unwrapped, wrappedField);
                    noGetterUnwrappedFields.add(unwrapped);
                } else {
                    // Field has a getter
                    if (doInvokes) {
                        result.put(wrappedField, getValue(wrappedField));
                    } else {
                        result.put(wrappedField, virtualMachine().voidValue());
                    }
                }
            } else {
                // Java type
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
            Value resultValue = VisageWrapper.wrap(virtualMachine(), 
                                             unwrappedFieldValues.get(unwrappedEntry.getKey()));
            result.put(wrappedField, resultValue);
        }
        return result;
    }

    /**
     * JDI addition: Determines if the value of a field of this reference type is invalid.  A value
     * is invalid if a new value has been specified for the field, but not yet
     * stored into the field, for example, because the field is lazily bound.
     *
     * @return <code>true</code> if the value of the specified field is invalid; false otherwise.
     */
    public boolean isInvalid(Field field) {
        return areFlagBitsSet(field, virtualMachine().VisageInvalidFlagMask());
    }

    /**
     * JDI addition: Determines if a field of this reference type can be modified.  For example,
     * an field declared with a bind cannot be modified.
     *
     * @return <code>true</code> if the specified field is read only; false otherwise.
     */
    public boolean isReadOnly(Field field) {
        return areFlagBitsSet(field, virtualMachine().VisageReadOnlyFlagMask());
    }

    /**
     * JDI addition: Determines if a field was declared with a bind clause.
     *
     * @return <code>true</code> if the specified field was declared with a bind clause; false otherwise.
     */
    public boolean isBound(Field field) {
        return areFlagBitsSet(field, virtualMachine().VisageBoundFlagMask());
    }

    /**
     * JDI addition: Determines if this is a Visage class.
     *
     * @return <code>true</code> if this is a Visage class; false otherwise.
     */
    public boolean isVisageType() {
        return false;
    }

    // Each internal class name is the name of its containing class followed by $digit.
    // (Except for the $Script class)
    private Pattern visagePat1 = Pattern.compile("\\$[0-9].*");
    private boolean isUserClassSet = false;
    private VisageClassType userClass = null;

    /**
     * JDI addition: Return the Visage user class associated with this reference type.
     *
     * The Visage compiler can generate several classes for a given class defined by the user.
     * Given one of these internal classes, this method will return the ReferenceType for the
     * associated user class.
     *
     * @return the Visage user class associated with this reference type if there is one, else null.
     */
    public VisageClassType visageUserClass() {
        if (isUserClassSet) {
            return userClass;
        }

        isUserClassSet = true;
        if (!isVisageType()) {
            return null;
        }
        
        String className = name();
        int firstDollar = className.indexOf('$');
        if (firstDollar == -1) {
            return null;
        }

        String[] hit = visagePat1.split(className, 0);
        if (hit.length != 1) {
            return null;
        }
        if (!hit[0].equals(className)) {
            List<ReferenceType> userClasses = virtualMachine().classesByName(hit[0]);
            if (userClasses.size() != 1) {
                // can't happen
                return null;
            }
            userClass = (VisageClassType)userClasses.get(0);
            return userClass;
        }

        if (className.indexOf("$Script") == -1) {
            return null;
        }
        
        // This is a $Script class, so we want the scriptClass
        userClass = scriptClass();
        return userClass;
    }

    private boolean isTopClassSet = false;
    private VisageClassType topClass = null;
    /**
     * JDI addition: Return the script class associated with this reference type.
     *
     * The Visage compiler can generate several classes for a given Visage file.  
     * Given one of these classes, this method will return the associated ReferenceType 
     * for the containing script class.
     *
     * @return the Visage class that contains this class if there is one, else null.
     */
    public VisageClassType scriptClass() {
        if (isTopClassSet) {
            return topClass;
        }

        isTopClassSet = true;
        if (!isVisageType()) {
            return null;
        }
        if (!(this instanceof VisageClassType)) {
            return null;
        }

        // Get the name of the top class.  First choice is the filename.  2nd choice
        // is the part of this name before the first $.
        String topClassName ;
        String thisName = name();
        int firstDollar = thisName.indexOf('$');
        if (firstDollar == -1) {
            topClass = (VisageClassType)this;
            return topClass;
        }
        topClassName = thisName.substring(0, firstDollar);

        List<ReferenceType> xx = virtualMachine().classesByName(topClassName);
        if (xx.size() != 1) {
            //  shouldn't happen
            return null;
        }

        if (!(xx.get(0) instanceof VisageClassType)) {
            // shouldn't happen
            return null;
        }
        
        topClass = (VisageClassType)xx.get(0);
        return topClass;
    }

    /**
     * JDI extension: This will call the get function for the field if one exists via invokeMethod.
     * The call to invokeMethod is preceded by a call to {@link VisageEventQueue#setEventControl(boolean)} passing true
     * and is followed by a call to {@link VisageEventQueue#setEventControl(boolean)} passing false.
     *
     * If an invokeMethod Exception occurs, it is saved and can be accessed by calling 
     * {@link VisageVirtualMachine#lastFieldAccessException()}. In this case,
     * the default value for the type of the field is returned for a PrimitiveType,
     * while null is returned for a non PrimitiveType.
     */
    public Value getValue(Field field) {
        virtualMachine().setLastFieldAccessException(null);
        Field jdiField = VisageWrapper.unwrap(field);
        if (!isVisageType()) {
            return VisageWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }

        //get$xxxx methods exist for fields except private fields which have no binders

        List<Method> mth = underlying().methodsByName("get" + jdiField.name());
        if (mth.size() == 0) {
            return VisageWrapper.wrap(virtualMachine(), underlying().getValue(jdiField));
        }
        Exception theExc = null;
        VisageEventQueue eq = virtualMachine().eventQueue();
        try {
            eq.setEventControl(true);
            return ((VisageClassType)this).invokeMethod(virtualMachine().uiThread(), mth.get(0), new ArrayList<Value>(0), ClassType.INVOKE_SINGLE_THREADED);
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
        try {
            return virtualMachine().defaultValue(field.type());
        } catch(ClassNotLoadedException ee) {
            // The type has to be a ReferenceType for which we return null;
            return null;
        }
    }

    /**
     * JDI extension: This will call the get function for a field if one exists via invokeMethod.
     * The call to invokeMethod is preceded by a call to {@link VisageEventQueue#setEventControl(boolean)}
     * passing true and is followed by a call to {@link VisageEventQueue#setEventControl(boolean)} passing false.
     *
     * If an invokeMethod Exception occurs, it is saved and can be accessed by calling 
     * {@link VisageVirtualMachine#lastFieldAccessException()}. In this case,
     * the default value for the type of the field is returned for a PrimitiveType,
     * while null is returned for a non PrimitiveType.
     */
    public Map<Field, Value> getValues(List<? extends Field> wrappedFields) {
        virtualMachine().setLastFieldAccessException(null);

        // We will find fields which have no getters, and call the underlying
        // getValues to get values for all of them in one fell swoop.
        Map<Field, Field> unwrappedToWrappedMap = new HashMap<Field, Field>();
        List<Field> noGetterUnwrappedFields = new ArrayList<Field>();    // fields that don't have getters

        // But first, for fields that do have getters, call invokeMethod
        // or we will call VisageGetValue for each, depending on doInvokes
        Map<Field, Value> result = new HashMap<Field, Value>();

        // Create the above Maps and lists
        for (Field wrappedField : wrappedFields) {
            Field unwrapped = VisageWrapper.unwrap(wrappedField);
            if (isVisageType()) {
                List<Method> mth = underlying().methodsByName("get" + unwrapped.name());
                if (mth.size() == 0) {
                    // No getter
                    unwrappedToWrappedMap.put(unwrapped, wrappedField);
                    noGetterUnwrappedFields.add(unwrapped);
                } else {
                    // Field has a getter
                    result.put(wrappedField, getValue(wrappedField));
                }
            } else {
                // Java type
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
            Value resultValue = VisageWrapper.wrap(virtualMachine(), 
                                             unwrappedFieldValues.get(unwrappedEntry.getKey()));
            result.put(wrappedField, resultValue);
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
        return VisageWrapper.wrapLocations(virtualMachine(), underlying().locationsOfLine(lineNumber));
    }

    public List<Location> locationsOfLine(String stratum, String sourceName, int line) throws AbsentInformationException {
        return VisageWrapper.wrapLocations(virtualMachine(), underlying().locationsOfLine(stratum, sourceName, line));
    }

    public List<Method> methods() {
        return VisageWrapper.wrapMethods(virtualMachine(), underlying().methods());
    }

    public List<Method> methodsByName(String name) {
        return VisageWrapper.wrapMethods(virtualMachine(), underlying().methodsByName(name));
    }

    public List<Method> methodsByName(String name, String signature) {
        return VisageWrapper.wrapMethods(virtualMachine(), underlying().methodsByName(name, signature));
    }

    public List<ReferenceType> nestedTypes() {
        return VisageWrapper.wrapReferenceTypes(virtualMachine(), underlying().nestedTypes());
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
        return VisageWrapper.wrapFields(virtualMachine(), underlying().visibleFields());
    }

    public List<Method> visibleMethods() {
        return VisageWrapper.wrapMethods(virtualMachine(), underlying().visibleMethods());
    }

    public int compareTo(ReferenceType o) {
        return underlying().compareTo(VisageWrapper.unwrap(o));
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
    
    public ReferenceType _underlying() {
        return (ReferenceType)super.underlying();
    }

    private boolean isInternalJavaTypeSet = false;
    private boolean internalJavaType = false;
    public boolean isInternalJavaType() {
        if (!isInternalJavaTypeSet) {
            String myName = name();
            if ("org.visage.runtime.VisageBase".equals(myName) ||
                "org.visage.runtime.VisageObject".equals(myName) ||
                myName.startsWith("org.visage.functions.Function")) {
                internalJavaType = true;
                isInternalJavaTypeSet = true;
            }
        }
        return internalJavaType;
    }
}
