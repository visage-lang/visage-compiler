/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
    public static final String attributeSetMethodNamePrefix = "set$";
    public static final String attributeApplyDefaultsMethodNamePrefix = "applyDefaults$";
    public static final String needsDefaultSuffix = "$needs_default$";
    public static final String mixinSuffix = "$Mixin";
    public static final String deprecatedInterfaceSuffix = "$Intf";
    public static final String equalsMethodString = "com.sun.javafx.runtime.Checks.equals";
    public static final String isNullMethodString = "com.sun.javafx.runtime.Checks.isNull";
    public static final String startMethodString = "com.sun.javafx.runtime.Entry.start";
    public static final String baseBindingListenerClassString = "com.sun.javafx.runtime.location.SBECL";
    
    public static final String fxObjectString = "com.sun.javafx.runtime.FXObject";
    public static final String fxMixinString = "com.sun.javafx.runtime.FXMixin";
    public static final String typeInfosString = "com.sun.javafx.runtime.TypeInfo";
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
    public static final String convertNumberSequence = "convertNumberSequence";
    public static final String scriptBindingListenerClassString = "_SBECL";
    public static final String bindingIdString = "id";
    public static final String getStaticDependentsMethodString = "getStaticDependents";
    public static final String computeMethodString = "compute";
    
    public  static final String javaLangPackageNameString = "java.lang";
    public  static final String runtimePackageNameString = "com.sun.javafx.runtime";
    public  static final String annotationPackageNameString = "com.sun.javafx.runtime.annotation";
    public  static final String locationPackageNameString = "com.sun.javafx.runtime.location";
    public  static final String functionsPackageNameString = "com.sun.javafx.functions";
    public  static final String sequencePackageNameString = "com.sun.javafx.runtime.sequence";

    private static final String cLocations = locationPackageNameString + ".Locations";
    public  static final String cChangeListener = locationPackageNameString + ".ChangeListener";
    public  static final String cSequences = sequencePackageNameString + ".Sequences";
    public  static final String cSequence  = sequencePackageNameString + ".Sequence";
    private static final String cBoundSequences = sequencePackageNameString + ".BoundSequences";
    private static final String cBoundOperators = locationPackageNameString + ".BoundOperators";
            static final String cOperator = cBoundOperators + ".Operator";

    static final String[] milieuNames = { "", "FromLiteral" };
    public static String getMilieuName(int index) { return milieuNames[index]; }
    
    static final int VANILLA_MILIEU = 0;
    static final int FROM_DEFAULT_MILIEU = 0;  // for now, same as vanilla
    static final int FROM_LITERAL_MILIEU = 1;
    static final int MILIEU_COUNT = 2;
    
    public char typeCharToEscape = '.';
    public char escapeTypeChar = '_';

    static class RuntimeMethod {
        final String classString;
        final Name methodName;

        private RuntimeMethod(Name.Table names, String classString, String methodString) {
            this.classString = classString;
            this.methodName = names.fromString(methodString);
        }
    }

    /**
     * RuntimeMethod definitions
     */
    final RuntimeMethod TypeInfo_getTypeInfo;

    final RuntimeMethod Locations_upcast;

    final RuntimeMethod Sequences_convertNumberSequence;
    final RuntimeMethod Sequences_forceNonNull;
    final RuntimeMethod Sequences_range;
    final RuntimeMethod Sequences_rangeExclusive;
    final RuntimeMethod Sequences_size;

    final RuntimeMethod BoundOperators_makeBoundSequenceSelect;
    final RuntimeMethod BoundOperators_makeBoundSelect;
    final RuntimeMethod BoundOperators_makeBoundIf;
    final RuntimeMethod BoundOperators_op_boolean;
    final RuntimeMethod BoundOperators_op_double;
    final RuntimeMethod BoundOperators_op_float;
    final RuntimeMethod BoundOperators_op_long;
    final RuntimeMethod BoundOperators_op_int;
    final RuntimeMethod BoundOperators_cmp_double;
    final RuntimeMethod BoundOperators_cmp_float;
    final RuntimeMethod BoundOperators_cmp_long;
    final RuntimeMethod BoundOperators_cmp_int;
    final RuntimeMethod BoundOperators_cmp_other;
    final RuntimeMethod BoundOperators_and_bb;

    final RuntimeMethod BoundSequences_convertNumberSequence;
    final RuntimeMethod BoundSequences_element;
    final RuntimeMethod BoundSequences_empty;
    final RuntimeMethod BoundSequences_range;
    final RuntimeMethod BoundSequences_rangeExclusive;
    final RuntimeMethod BoundSequences_reverse;
    final RuntimeMethod BoundSequences_singleton;
    final RuntimeMethod BoundSequences_sizeof;
    final RuntimeMethod BoundSequences_slice;
    final RuntimeMethod BoundSequences_sliceExclusive;
    final RuntimeMethod BoundSequences_upcast;

    /**
     * Name definitions
     */
    public final Name fxObjectName;
    public final Name fxMixinName;
    public final Name mixinSuffixName;
    public final Name deprecatedInterfaceSuffixName;
    final Name userRunFunctionName;
    final Name internalRunFunctionName;
    final Name mainName;
    final Name receiverName;
    final Name initializeName;
    final Name getMethodName;
    final Name attributeSetMethodParamName;
    final Name getSliceMethodName;
    final Name replaceSliceMethodName;
    final Name setMethodName;
    final Name sizeMethodName;
    final Name defaultingTypeInfoFieldName;
    final Name addStaticDependentName;
    final Name addDynamicDependentName;
    final Name clearDynamicDependenciesName;
    final Name needDefaultsMethodName;
    final Name makeAttributeMethodName;
    final Name makeMethodName;
    final Name makeWithDefaultMethodName;
    final Name makeBijectiveMethodName;
    final Name onChangeMethodName;
    final Name addChangeListenerName;
    final Name addSequenceChangeListenerName;
    final Name locationInitializeName;
    final Name invokeName;
    final Name lambdaName;
    final Name lengthName;
    final Name emptySequenceFieldString;
    final Name isInitializedName;
    final Name scriptBindingClassName;
    final Name bindingIdName;
    final Name getStaticDependentsMethodName;
    final Name computeMethodName;
    final Name toTestName;
    final Name toBeCastName;
    final Name idName;
    final Name arg0Name;
    final Name arg1Name;
    final Name moreArgsName;
    final Name dependentsName;
    final Name typeParamName;
    final Name initDefName;
    final Name postInitDefName;
    final Name javalangThreadName;
    final Name startName;
    final Name timeName;
    final Name valuesName;
    final Name valueName;
    final Name targetName;
    final Name interpolateName;
    final Name addTriggersName;
    final Name userInitName;
    final Name postInitName;
    final Name attributeGetPrefixName;
    final Name attributeSetPrefixName;
    final Name applyDefaultsPrefixName;
    final Name setDefaultMethodName;
    final Name noteSharedMethodName;
    final Name onReplaceArgNameOld;
    final Name onReplaceArgNameNew;
    final Name onReplaceArgNameFirstIndex;
    final Name onReplaceArgNameLastIndex;
    final Name onReplaceArgNameNewElements;
    final Name getAsSequenceRawMethodName;
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
    public final Name needsDefaultSuffixName;
    
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
    public static final Context.Key<JavafxDefs> jfxDefsKey = new Context.Key<JavafxDefs>();

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
        fxMixinName = names.fromString(fxMixinString);
        mixinSuffixName = names.fromString(mixinSuffix);
        deprecatedInterfaceSuffixName = names.fromString(deprecatedInterfaceSuffix);
        userRunFunctionName = names.fromString("run");
        internalRunFunctionName = names.fromString(internalRunFunctionNameString);
        mainName = names.fromString("main");
        receiverName = names.fromString(receiverNameString);
        initializeName = names.fromString(initializeNameString);
        getMethodName = names.fromString(getMethodNameString);
        attributeSetMethodParamName = names.fromString("value");
        getSliceMethodName = names.fromString("getSlice");
        replaceSliceMethodName = names.fromString("replaceSlice");
        getAsSequenceRawMethodName = names.fromString("getAsSequenceRaw");
        setMethodName = names.fromString(setMethodNameString);
        sizeMethodName = names.fromString(sizeMethodNameString);
        defaultingTypeInfoFieldName = names.fromString("$TYPE_INFO");
        addStaticDependentName = names.fromString(addStaticDependentNameString);
        addDynamicDependentName = names.fromString(addDynamicDependentNameString);
        clearDynamicDependenciesName = names.fromString(clearDynamicDependenciesNameString);
        needDefaultsMethodName = names.fromString(trySetFromLiteralMethodNameString);
        makeAttributeMethodName = names.fromString(makeAttributeMethodNameString);
        makeMethodName = names.fromString(makeMethodNameString);
        makeWithDefaultMethodName = names.fromString(makeWithDefaultMethodNameString);
        makeBijectiveMethodName = names.fromString(makeBijectiveMethodNameString);
        onChangeMethodName = names.fromString("onChange");
        addChangeListenerName = names.fromString("addChangeListener");
        addSequenceChangeListenerName = names.fromString("addSequenceChangeListener");
        locationInitializeName = names.fromString("initialize");
        invokeName = names.fromString(invokeNameString);
        lambdaName = names.fromString(lambdaNameString);
        lengthName = names.fromString("length");
        emptySequenceFieldString = names.fromString("emptySequence");
        isInitializedName = names.fromString(isInitializedNameString);
        scriptBindingClassName = names.fromString(scriptBindingListenerClassString);
        bindingIdName = names.fromString(bindingIdString);
        getStaticDependentsMethodName = names.fromString(getStaticDependentsMethodString);
        computeMethodName = names.fromString(computeMethodString);
        toTestName = names.fromString("toTest");
        toBeCastName = names.fromString("toBeCast");
        idName = names.fromString("id");
        arg0Name = names.fromString("arg$0");
        arg1Name = names.fromString("arg$1");
        moreArgsName = names.fromString("moreArgs");
        dependentsName = names.fromString("dependents");
        typeParamName = names.fromString("T");
        initDefName = names.fromString("$init$def$name");
        postInitDefName = names.fromString("$postinit$def$name");
        timeName = names.fromString("time");
        javalangThreadName = names.fromString("java.lang.Thread");
        startName = names.fromString("start");
        valuesName = names.fromString("values");
        targetName = names.fromString("target");
        valueName = names.fromString("value");
        interpolateName = names.fromString("interpolate");
        addTriggersName = names.fromString("addTriggers$");
        userInitName = names.fromString("userInit$");
        postInitName = names.fromString("postInit$");
        noteSharedMethodName = names.fromString("noteShared");
        onReplaceArgNameOld = names.fromString("$oldValue");
        onReplaceArgNameNew = names.fromString("$newValue");
        onReplaceArgNameFirstIndex = names.fromString("$index$");
        onReplaceArgNameLastIndex = names.fromString("$lastIndex$");
        onReplaceArgNameNewElements = names.fromString("$newElements$");
        implFunctionSuffixName = names.fromString(implFunctionSuffix);
        needsDefaultSuffixName = names.fromString(needsDefaultSuffix);
        attributeGetPrefixName = names.fromString(attributeGetMethodNamePrefix);
        attributeSetPrefixName = names.fromString(attributeSetMethodNamePrefix);
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

        // Initialize RuntimeMethods
        TypeInfo_getTypeInfo = new RuntimeMethod(names, typeInfosString, "getTypeInfo");

        Locations_upcast = new RuntimeMethod(names, cLocations, "upcast");

        Sequences_convertNumberSequence = new RuntimeMethod(names, cSequences, "convertNumberSequence");
        Sequences_forceNonNull = new RuntimeMethod(names, cSequences, "forceNonNull");
        Sequences_range = new RuntimeMethod(names, cSequences, "range");
        Sequences_rangeExclusive = new RuntimeMethod(names, cSequences, "rangeExclusive");
        Sequences_size = new RuntimeMethod(names, cSequences, "size");

        BoundOperators_makeBoundSequenceSelect = new RuntimeMethod(names, cBoundOperators, "makeBoundSequenceSelect");
        BoundOperators_makeBoundSelect = new RuntimeMethod(names, cBoundOperators, "makeBoundSelect");
        BoundOperators_makeBoundIf = new RuntimeMethod(names, cBoundOperators, "makeBoundIf");
        BoundOperators_op_boolean = new RuntimeMethod(names, cBoundOperators, "op_boolean");
        BoundOperators_op_double = new RuntimeMethod(names, cBoundOperators, "op_double");
        BoundOperators_op_float = new RuntimeMethod(names, cBoundOperators, "op_float");
        BoundOperators_op_long = new RuntimeMethod(names, cBoundOperators, "op_long");
        BoundOperators_op_int = new RuntimeMethod(names, cBoundOperators, "op_int");
        BoundOperators_cmp_double = new RuntimeMethod(names, cBoundOperators, "cmp_double");
        BoundOperators_cmp_float = new RuntimeMethod(names, cBoundOperators, "cmp_float");
        BoundOperators_cmp_long = new RuntimeMethod(names, cBoundOperators, "cmp_long");
        BoundOperators_cmp_int = new RuntimeMethod(names, cBoundOperators, "cmp_int");
        BoundOperators_cmp_other = new RuntimeMethod(names, cBoundOperators, "cmp_other");
        BoundOperators_and_bb = new RuntimeMethod(names, cBoundOperators, "and_bb");

        BoundSequences_singleton = new RuntimeMethod(names, cBoundSequences, "singleton");
        BoundSequences_range = new RuntimeMethod(names, cBoundSequences, "range");
        BoundSequences_rangeExclusive = new RuntimeMethod(names, cBoundSequences, "rangeExclusive");
        BoundSequences_empty = new RuntimeMethod(names, cBoundSequences, "empty");
        BoundSequences_sizeof = new RuntimeMethod(names, cBoundSequences, "sizeof");
        BoundSequences_reverse = new RuntimeMethod(names, cBoundSequences, "reverse");
        BoundSequences_element = new RuntimeMethod(names, cBoundSequences, "element");
        BoundSequences_slice = new RuntimeMethod(names, cBoundSequences, "slice");
        BoundSequences_sliceExclusive = new RuntimeMethod(names, cBoundSequences, "sliceExclusive");
        BoundSequences_upcast = new RuntimeMethod(names, cBoundSequences, "upcast");
        BoundSequences_convertNumberSequence = new RuntimeMethod(names, cBoundSequences, "convertNumberSequence");

        // Initialize per Kind / Milieu names and types
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
        realTypeByKind[TYPE_KIND_BOOLEAN] = syms.booleanType;
        realTypeByKind[TYPE_KIND_CHAR] = syms.charType;
        realTypeByKind[TYPE_KIND_BYTE] = syms.byteType;
        realTypeByKind[TYPE_KIND_SHORT] = syms.shortType;
        realTypeByKind[TYPE_KIND_INT] = syms.intType;
        realTypeByKind[TYPE_KIND_LONG] = syms.longType;
        realTypeByKind[TYPE_KIND_FLOAT] = syms.floatType;
        realTypeByKind[TYPE_KIND_DOUBLE] = syms.doubleType;
        realTypeByKind[TYPE_KIND_SEQUENCE] = syms.javafx_SequenceType;
    }
}
