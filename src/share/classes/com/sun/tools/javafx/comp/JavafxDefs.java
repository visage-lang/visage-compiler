/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.Name;
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
    public static final String attributeApplyDefaultsBaseMethodName = "applyDefaults$base$";
    public static final String attributeCountMethodString = "count$";
    public static final String attributeIsInitializedMethodNamePrefix = "isInitialized$";
    public static final String mixinSuffix = "$Mixin";
    public static final String deprecatedInterfaceSuffix = "$Intf";
    public static final String equalsMethodString = "com.sun.javafx.runtime.Checks.equals";
    public static final String isNullMethodString = "com.sun.javafx.runtime.Checks.isNull";
    public static final String startMethodString = "com.sun.javafx.runtime.Entry.start";
    public static final String baseBindingListenerClassString = "com.sun.javafx.runtime.location.SBECL";

    public static final String fxObjectString = "com.sun.javafx.runtime.FXObject";
    public static final String fxMixinString = "com.sun.javafx.runtime.FXMixin";
    public static final String fxBaseString = "com.sun.javafx.runtime.FXBase";
    public static final String typeInfosString = "com.sun.javafx.runtime.TypeInfo";
    public static final String internalRunFunctionNameString = Entry.entryMethodName();
    public static final String receiverNameString = "receiver$";
    public static final String initializeNameString ="initialize$";
    public static final String completeNameString ="complete$";
    public static final String getMethodNameString = "get";
    public static final String setMethodNameString ="set";
    public static final String setDefaultMethodNameString = "setDefault";
    public static final String sizeMethodNameString ="size";
    public static final String addStaticDependentNameString = "addStaticDependent";
    public static final String needDefaultMethodNameString = "needDefault";
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

    public static final String varOffsetString = "VOFF$";
    public static final String varCountString = "VCNT$";
    public static final String varValueString = "$";
    public static final String varFlagActionTest = "is";
    public static final String varFlagActionSet = "set";
    public static final String varFlagActionClear = "clear";
    public static final String varFlagInitialized = "Initialized$";
    public static final String varFlagDefaultsApplied = "DefaultsApplied$";
    public static final String varFlagValid = "ValidValue$";
    public static final String varFlagHasDependents = "Bindee$";
    public static final String varLocationString = "loc$";
    public static final String varMapString = "MAP$";
    public static final String varGetMapString = "GETMAP$";

    public  static final String javaLangPackageNameString = "java.lang";
    public  static final String runtimePackageNameString = "com.sun.javafx.runtime";
    public  static final String annotationPackageNameString = "com.sun.javafx.runtime.annotation";
    public  static final String locationPackageNameString = "com.sun.javafx.runtime.location";
    public  static final String functionsPackageNameString = "com.sun.javafx.functions";
    public  static final String sequencePackageNameString = "com.sun.javafx.runtime.sequence";

    public  static final String cChangeListener = locationPackageNameString + ".ChangeListener";
    public  static final String cSequences = sequencePackageNameString + ".Sequences";
    public  static final String cSequence  = sequencePackageNameString + ".Sequence";
    public  static final String arraySequence  = sequencePackageNameString + ".ArraySequence";
    private static final String cBoundSequences = sequencePackageNameString + ".BoundSequences";
    private static final String cAbstractBoundComprehension = sequencePackageNameString + ".AbstractBoundComprehension";
    private static final String cLocations = locationPackageNameString + ".Locations";
    private static final String cUtil = runtimePackageNameString + ".Util";

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

    final RuntimeMethod Sequences_convertNumberSequence;
    final RuntimeMethod Sequences_forceNonNull;
    final RuntimeMethod Sequences_range;
    final RuntimeMethod Sequences_rangeExclusive;
    final RuntimeMethod Sequences_size;

    final RuntimeMethod Locations_makeBoundSequenceSelect;
    final RuntimeMethod Locations_makeBoundSelect;
    final RuntimeMethod Locations_makeBoundSelectBE;
    final RuntimeMethod Locations_makeBoundIf;
    final RuntimeMethod Locations_makeBoundIfBE;
    final RuntimeMethod Locations_makeBoundOr;
    final RuntimeMethod Locations_makeBoundOrBE;
    final RuntimeMethod Locations_makeBoundAnd;
    final RuntimeMethod Locations_makeBoundAndBE;
    final RuntimeMethod Locations_upcast;

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

    final RuntimeMethod Util_isEqual;

    /**
     * Name definitions
     */
    public final Name fxObjectName;
    public final Name fxMixinName;
    public final Name fxBaseName;
    public final Name mixinSuffixName;
    public final Name deprecatedInterfaceSuffixName;
    final Name durOpAdd;
    final Name durOpSub;
    final Name durOpMul;
    final Name durOpDiv;
    final Name durOpLE;
    final Name durOpLT;
    final Name durOpGE;
    final Name durOpGT;
    final Name userRunFunctionName;
    final Name internalRunFunctionName;
    final Name mainName;
    final Name receiverName;
    final Name initializeName;
    final Name completeName;
    final Name outerAccessorName;
    final Name getMethodName;
    final Name attributeSetMethodParamName;
    final Name getSliceMethodName;
    final Name replaceSliceMethodName;
    final Name setMethodName;
    final Name sizeMethodName;
    final Name defaultingTypeInfoFieldName;
    final Name addStaticDependentName;
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
    final Name varOffsetName;
    final Name varCountName;
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
    final Name attributeCountMethodName;
    final Name isInitializedPrefixName;
    final Name incrementSharingMethodName;
    final Name onReplaceArgNameOld;
    final Name onReplaceArgNameNew;
    final Name onReplaceArgNameBuffer;
    final Name onReplaceArgNameFirstIndex;
    final Name onReplaceArgNameLastIndex;
    final Name onReplaceArgNameNewElements;
    final Name[] locationGetMethodName;
    final Name[] locationSetMethodName;
    final Name locationBindMethodName;
    final Name locationBijectiveBindMethodName;
    final Name computeElementsMethodName;
    final Name cAbstractBoundComprehensionName;

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

        durOpAdd = names.fromString("add");
        durOpSub = names.fromString("sub");
        durOpMul = names.fromString("mul");
        durOpDiv = names.fromString("div");
        durOpLE = names.fromString("le");
        durOpLT = names.fromString("lt");
        durOpGE = names.fromString("ge");
        durOpGT = names.fromString("gt");
        fxObjectName = names.fromString(fxObjectString);
        fxMixinName = names.fromString(fxMixinString);
        fxBaseName = names.fromString(fxBaseString);
        mixinSuffixName = names.fromString(mixinSuffix);
        deprecatedInterfaceSuffixName = names.fromString(deprecatedInterfaceSuffix);
        userRunFunctionName = names.fromString("run");
        internalRunFunctionName = names.fromString(internalRunFunctionNameString);
        mainName = names.fromString("main");
        receiverName = names.fromString(receiverNameString);
        initializeName = names.fromString(initializeNameString);
        completeName = names.fromString(completeNameString);
        outerAccessorName = names.fromString("accessOuter$");
        getMethodName = names.fromString(getMethodNameString);
        attributeSetMethodParamName = names.fromString("value$");
        getSliceMethodName = names.fromString("getSlice");
        replaceSliceMethodName = names.fromString("replaceSlice");
        setMethodName = names.fromString(setMethodNameString);
        sizeMethodName = names.fromString(sizeMethodNameString);
        defaultingTypeInfoFieldName = names.fromString("$TYPE_INFO");
        addStaticDependentName = names.fromString(addStaticDependentNameString);
        needDefaultsMethodName = names.fromString(needDefaultMethodNameString);
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
        varOffsetName = names.fromString(varOffsetString);
        varCountName = names.fromString(varCountString);
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
        incrementSharingMethodName = names.fromString("incrementSharing");
        onReplaceArgNameBuffer = names.fromString("$buffer$");
        onReplaceArgNameOld = names.fromString("$oldValue");
        onReplaceArgNameNew = names.fromString("$newValue");
        onReplaceArgNameFirstIndex = names.fromString("$index$");
        onReplaceArgNameLastIndex = names.fromString("$lastIndex$");
        onReplaceArgNameNewElements = names.fromString("$newElements$");
        implFunctionSuffixName = names.fromString(implFunctionSuffix);
        attributeGetPrefixName = names.fromString(attributeGetMethodNamePrefix);
        attributeSetPrefixName = names.fromString(attributeSetMethodNamePrefix);
        applyDefaultsPrefixName = names.fromString(attributeApplyDefaultsMethodNamePrefix);
        attributeCountMethodName = names.fromString(attributeCountMethodString);
        isInitializedPrefixName = names.fromString(attributeIsInitializedMethodNamePrefix);
        computeElementsMethodName = names.fromString("computeElements$");

		runtimePackageName = names.fromString(runtimePackageNameString);
		annotationPackageName = names.fromString(annotationPackageNameString);
		javaLangPackageName = names.fromString(javaLangPackageNameString);
		locationPackageName = names.fromString(locationPackageNameString);
		sequencePackageName = names.fromString(sequencePackageNameString);
		functionsPackageName = names.fromString(functionsPackageNameString);
        locationGetMethodName = new Name[TYPE_KIND_COUNT];
        locationSetMethodName = new Name[TYPE_KIND_COUNT];
        locationVariableName = new Name[TYPE_KIND_COUNT];
        locationInterfaceName = new Name[TYPE_KIND_COUNT];
        cAbstractBoundComprehensionName = names.fromString(cAbstractBoundComprehension);

        // Initialize RuntimeMethods
        TypeInfo_getTypeInfo = new RuntimeMethod(names, typeInfosString, "getTypeInfo");

        Sequences_convertNumberSequence = new RuntimeMethod(names, cSequences, "convertNumberSequence");
        Sequences_forceNonNull = new RuntimeMethod(names, cSequences, "forceNonNull");
        Sequences_range = new RuntimeMethod(names, cSequences, "range");
        Sequences_rangeExclusive = new RuntimeMethod(names, cSequences, "rangeExclusive");
        Sequences_size = new RuntimeMethod(names, cSequences, "size");

        Locations_makeBoundSequenceSelect = new RuntimeMethod(names, cLocations, "makeBoundSequenceSelect");
        Locations_makeBoundSelect = new RuntimeMethod(names, cLocations, "makeBoundSelect");
        Locations_makeBoundSelectBE = new RuntimeMethod(names, cLocations, "makeBoundSelectBE");
        Locations_makeBoundIf = new RuntimeMethod(names, cLocations, "makeBoundIf");
        Locations_makeBoundIfBE = new RuntimeMethod(names, cLocations, "makeBoundIfBE");
        Locations_makeBoundOr = new RuntimeMethod(names, cLocations, "makeBoundOr");
        Locations_makeBoundOrBE = new RuntimeMethod(names, cLocations, "makeBoundOrBE");
        Locations_makeBoundAnd = new RuntimeMethod(names, cLocations, "makeBoundAnd");
        Locations_makeBoundAndBE = new RuntimeMethod(names, cLocations, "makeBoundAndBE");
        Locations_upcast = new RuntimeMethod(names, cLocations, "upcast");
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
        Util_isEqual = new RuntimeMethod(names, cUtil, "isEqual");

        // Initialize per Kind names and types
        for (int kind = 0; kind < TYPE_KIND_COUNT; kind++) {
            locationGetMethodName[kind] = names.fromString("get" + JavafxVarSymbol.getAccessorSuffix(kind));
            locationSetMethodName[kind] = names.fromString("set" + JavafxVarSymbol.getAccessorSuffix(kind));

            String typePrefix = locationPackageNameString + "." + JavafxVarSymbol.getTypePrefix(kind);
            locationVariableName[kind] = names.fromString(typePrefix + "Variable");
            locationInterfaceName[kind] = names.fromString(typePrefix + "Location");
        }
        locationBindMethodName = names.fromString("bind");
        locationBijectiveBindMethodName = names.fromString("bijectiveBind");

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
