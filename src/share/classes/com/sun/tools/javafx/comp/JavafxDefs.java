/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.comp;

import com.sun.javafx.runtime.Entry;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;

/**
 * Shared definitions
 *
 * @author Robert Field
 */
public class JavafxDefs {

    /**
     * static string definitions
     */
    public static final String boundFunctionDollarSuffix = "$$bound$";
    public static final String implFunctionSuffix = "$impl";
    public static final String attributeGetMethodNamePrefix = "get$";
    public static final String attributeApplyDefaultsMethodNamePrefix = "applyDefaults$";
    public static final String interfaceSuffix = "$Intf";
    public static final String equalsMethodString = "com.sun.javafx.runtime.Checks.equals";
    public static final String isNullMethodString = "com.sun.javafx.runtime.Checks.isNull";
    public static final String startMethodString = "com.sun.javafx.runtime.Entry.start";
    
    public static final String fxObjectString = "com.sun.javafx.runtime.FXObject";
    public static final String internalRunFunctionNameString = Entry.entryMethodName();
    public static final String receiverNameString = "receiver$";
    public static final String initializeNameString ="initialize$";
    public static final String getMethodNameString = "get";
    public static final String setMethodNameString ="set";
    public static final String setDefaultMethodNameString = "setDefault";
    public static final String sizeMethodNameString ="size";
    public static final String addStaticDependentNameString = "addStaticDependent";
    public static final String addDynamicDependentNameString = "addDynamicDependent";
    public static final String clearDynamicDependenciesNameString = "clearDynamicDependencies";
    public static final String trySetFromLiteralMethodNameString = "needDefault";
    public static final String makeAttributeMethodNameString = "makeAttribute";
    public static final String makeMethodNameString = "make";
    public static final String makeWithDefaultMethodNameString = "makeWithDefault";
    public static final String makeBijectiveMethodNameString = "makeBijective";
    public static final String invokeNameString = "invoke";
    public static final String lambdaNameString = "lambda";
    public static final String isInitializedNameString = "isInitialized";
    
    public static final String javaLangPackageNameString = "java.lang";
    public static final String runtimePackageNameString = "com.sun.javafx.runtime";
    public static final String annotationPackageNameString = "com.sun.javafx.runtime.annotation";
    public static final String locationPackageNameString = "com.sun.javafx.runtime.location";
    public static final String sequencePackageNameString = "com.sun.javafx.runtime.sequence";
    public static final String functionsPackageNameString = "com.sun.javafx.functions";

    static final String[] milieuNames = { "", "FromLiteral" };
    public static String getMilieuName(int index) { return milieuNames[index]; }
    
    static final int VANILLA_MILIEU = 0;
    static final int FROM_DEFAULT_MILIEU = 0;  // for now, same as vanilla
    static final int FROM_LITERAL_MILIEU = 1;
    static final int MILIEU_COUNT = 2;
    
    public char typeCharToEscape = '.';
    public char escapeTypeChar = '_';
    
    /**
     * Name definitions
     */
    public final Name fxObjectName;
    public final Name interfaceSuffixName;
    final Name userRunFunctionName;
    final Name internalRunFunctionName;
    final Name receiverName;
    final Name initializeName;
    final Name getMethodName;
    final Name getSliceMethodName;
    final Name replaceSliceMethodName;
    final Name setMethodName;
    final Name sizeMethodName;
    final Name addStaticDependentName;
    final Name addDynamicDependentName;
    final Name clearDynamicDependenciesName;
    final Name needDefaultsMethodName;
    final Name makeAttributeMethodName;
    final Name makeMethodName;
    final Name makeWithDefaultMethodName;
    final Name makeBijectiveMethodName;
    final Name invokeName;
    final Name lambdaName;
    final Name isInitializedName;
    final Name computeValueName;
    final Name computeElementName;
    final Name initDefName;
    final Name postInitDefName;
    final Name timeName;
    final Name valuesName;
    final Name valueName;
    final Name targetName;
    final Name interpolateName;
    final Name addTriggersName;
    final Name userInitName;
    final Name postInitName;
    final Name attributeGetPrefixName;
    final Name applyDefaultsPrefixName;
    final Name setDefaultMethodName;
    final Name[] locationGetMethodName;
    final Name[] locationSetMethodName;
    final Name[][] locationSetMilieuMethodName;
    final Name[] locationBindMilieuMethodName;
    final Name[] locationBijectiveBindMilieuMethodName;
    
	public final Name runtimePackageName;
	public final Name annotationPackageName;
	public final Name locationPackageName;
	public final Name sequencePackageName;
	public final Name functionsPackageName;
	public final Name javaLangPackageName;
    public final Name[] locationVariableName;
    public final Name[] locationInterfaceName;
    public final Name implFunctionSuffixName;
    
    public Type delocationize(Name flatname) {
        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            if (flatname == locationVariableName[kind] || flatname == locationInterfaceName[kind]) {
                return realTypeByKind[kind];
            }
        }
        return null;
    }

    /**
     * Context set-up
     */
    protected static final Context.Key<JavafxDefs> jfxDefsKey = new Context.Key<JavafxDefs>();

    private final Type[] realTypeByKind;

    public static JavafxDefs instance(Context context) {
        JavafxDefs instance = context.get(jfxDefsKey);
        if (instance == null) {
            instance = new JavafxDefs(context);
        }
        return instance;
    }

    protected JavafxDefs(Context context) {
        context.put(jfxDefsKey, this);
        final Name.Table names = Name.Table.instance(context);
        final JavafxSymtab syms = (JavafxSymtab)(JavafxSymtab.instance(context));

        fxObjectName = names.fromString(fxObjectString);
        interfaceSuffixName = names.fromString(interfaceSuffix);
        userRunFunctionName = names.fromString("run");
        internalRunFunctionName = names.fromString(internalRunFunctionNameString);
        receiverName = names.fromString(receiverNameString);
        initializeName = names.fromString(initializeNameString);
        getMethodName = Name.fromString(names, getMethodNameString);
        getSliceMethodName = names.fromString("getSlice");
        replaceSliceMethodName = names.fromString("replaceSlice");
        setMethodName = Name.fromString(names, setMethodNameString);
        sizeMethodName = Name.fromString(names, sizeMethodNameString);
        addStaticDependentName = names.fromString(addStaticDependentNameString);
        addDynamicDependentName = names.fromString(addDynamicDependentNameString);
        clearDynamicDependenciesName = names.fromString(clearDynamicDependenciesNameString);
        needDefaultsMethodName = names.fromString(trySetFromLiteralMethodNameString);
        makeAttributeMethodName = Name.fromString(names, makeAttributeMethodNameString);
        makeMethodName = Name.fromString(names, makeMethodNameString);
        makeWithDefaultMethodName = Name.fromString(names, makeWithDefaultMethodNameString);
        makeBijectiveMethodName = Name.fromString(names, makeBijectiveMethodNameString);
        invokeName = names.fromString(invokeNameString);
        lambdaName = names.fromString(lambdaNameString);
        isInitializedName = names.fromString(isInitializedNameString);
        computeValueName = names.fromString("computeValue");
        computeElementName = names.fromString("computeElement$");
        initDefName = names.fromString("$init$def$name");
        postInitDefName = names.fromString("$postinit$def$name");
        timeName = names.fromString("time");
        valuesName = names.fromString("values");
        targetName = names.fromString("target");
        valueName = names.fromString("value");
        interpolateName = names.fromString("interpolate");
        addTriggersName = names.fromString("addTriggers$");
        userInitName = names.fromString("userInit$");
        postInitName = names.fromString("postInit$");
        implFunctionSuffixName = names.fromString(implFunctionSuffix);
        attributeGetPrefixName = names.fromString(attributeGetMethodNamePrefix);
        applyDefaultsPrefixName = names.fromString(attributeApplyDefaultsMethodNamePrefix);
		runtimePackageName = names.fromString(runtimePackageNameString);
		annotationPackageName = names.fromString(annotationPackageNameString);
		javaLangPackageName = names.fromString(javaLangPackageNameString);
		locationPackageName = names.fromString(locationPackageNameString);
		sequencePackageName = names.fromString(sequencePackageNameString);
		functionsPackageName = names.fromString(functionsPackageNameString);
        setDefaultMethodName  = names.fromString(setDefaultMethodNameString);
        locationGetMethodName = new Name[TYPE_KIND_COUNT];
        locationSetMethodName = new Name[TYPE_KIND_COUNT];
        locationSetMilieuMethodName = new Name[TYPE_KIND_COUNT][MILIEU_COUNT];
        locationVariableName = new Name[TYPE_KIND_COUNT];
        locationInterfaceName = new Name[TYPE_KIND_COUNT];
        for (int kind = 0; kind < TYPE_KIND_COUNT; kind++) {
            for (int m = 0; m < MILIEU_COUNT; ++m) {
                locationSetMilieuMethodName[kind][m] = names.fromString("set" + JavafxVarSymbol.getAccessorSuffix(kind) + milieuNames[m]);
            }
            locationGetMethodName[kind] = names.fromString("get" + JavafxVarSymbol.getAccessorSuffix(kind));
            locationSetMethodName[kind] = locationSetMilieuMethodName[kind][VANILLA_MILIEU];
            
            String typePrefix = locationPackageNameString + "." + JavafxVarSymbol.getTypePrefix(kind);
            locationVariableName[kind] = names.fromString(typePrefix + "Variable");
            locationInterfaceName[kind] = names.fromString(typePrefix + "Location");
        }
        locationBindMilieuMethodName = new Name[MILIEU_COUNT];
        locationBijectiveBindMilieuMethodName = new Name[MILIEU_COUNT];
        for (int m = 0; m < MILIEU_COUNT; ++m) {
            locationBindMilieuMethodName[m] = names.fromString("bind" + milieuNames[m]);
            locationBijectiveBindMilieuMethodName[m] = names.fromString("bijectiveBind" + milieuNames[m]);
        }
        realTypeByKind = new Type[TYPE_KIND_COUNT];
        realTypeByKind[TYPE_KIND_OBJECT] = syms.objectType;
        realTypeByKind[TYPE_KIND_DOUBLE] = syms.doubleType;
        realTypeByKind[TYPE_KIND_BOOLEAN] = syms.booleanType;
        realTypeByKind[TYPE_KIND_INT] = syms.intType;
        realTypeByKind[TYPE_KIND_SEQUENCE] = syms.javafx_SequenceType;

    }
}
