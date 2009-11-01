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
import com.sun.tools.mjavac.code.Symbol;
import java.util.regex.Pattern;

/**
 * Shared definitions
 *
 * @author Robert Field
 */
public class JavafxDefs {

    /**
     * Method prefix strings for attributes
     *
     * Format: <name>_AttributeMethodPrefix
     */
    public static final String applyDefaults_AttributeMethodPrefix = "applyDefaults$";
    public static final String be_AttributeMethodPrefix = "be$";
    public static final String evaluate_AttributeMethodPrefix = "evaluate$";
    public static final String get_AttributeMethodPrefix = "get$";
    public static final String getMixin_AttributeMethodPrefix = "getMixin$";
    public static final String getVOFF_AttributeMethodPrefix = "getVOFF$";
    public static final String initVarBits_AttributeMethodPrefix = "initVarBits$";
    public static final String invalidate_AttributeMethodPrefix = "invalidate$";
    public static final String onReplace_AttributeMethodPrefix = "onReplace$";
    public static final String set_AttributeMethodPrefix = "set$";
    public static final String setMixin_AttributeMethodPrefix = "setMixin$";
    public static final String size_AttributeMethodPrefix = "size$";
    public static final String getElement_AttributeMethodPrefix = "get$";

    /**
     * Field prefix strings for attributes
     *
     * Format: *_AttributeFieldNamePrefix
     */
    public static final String value_AttributeFieldPrefix = "$";
    public static final String saved_AttributeFieldPrefix = "saved$";
    public static final String offset_AttributeFieldPrefix = "VOFF$";

    /**
     * Prefixes and name strings of generated FXObject fields
     *
     * Format: *_FXObjectFieldNameString
     */
    public static final String count_FXObjectFieldString = "VCNT$";
    public static final String varFlags_FXObjectFieldPrefix = "VFLGS$";
    public static final String varMap_FXObjectFieldPrefix = "MAP$";

    /**
     * Class suffixes
     */
    public static final String mixinClassSuffix = "$Mixin";
    public static final String deprecatedInterfaceSuffix = "$Intf";
    public static final String scriptClassSuffix = "$Script";

    /**
     * Package name strings
     *
     * Format: <name>_PackageNameString
     */
    public static final String javaLang_PackageString = "java.lang";
    public static final String runtime_PackageString = "com.sun.javafx.runtime";
    public static final String annotation_PackageString = "com.sun.javafx.runtime.annotation";
    public static final String functions_PackageString = "com.sun.javafx.functions";
    public static final String sequence_PackageString = "com.sun.javafx.runtime.sequence";
    public static final String fxLang_PackageString = "javafx.lang";

    /**
     * Class name strings
     *
     * Format: c<className>
     */
    // in sequence package
    public static final String cSequences = sequence_PackageString + ".Sequences";
    public static final String cSequence = sequence_PackageString + ".Sequence";
    public static final String cSequenceRef = sequence_PackageString + ".SequenceRef";
    public static final String cArraySequence = sequence_PackageString + ".ArraySequence";
    // in runtime package -- public
    public static final String cFXBase = runtime_PackageString + ".FXBase";
    public static final String cFXObject = runtime_PackageString + ".FXObject";
    public static final String cFXMixin = runtime_PackageString + ".FXMixin";
    public static final String cTypeInfo = runtime_PackageString + ".TypeInfo";
    // in runtime package
    private static final String cUtil = runtime_PackageString + ".Util";
    private static final String cChecks = runtime_PackageString + ".Checks";
    private static final String cFXVariable = runtime_PackageString + ".FXVariable";
    private static final String cPointer = runtime_PackageString + ".Pointer";
    // in java.lang package
    private static final String cMath = javaLang_PackageString + ".Math";
    // in javafx.lang package
    private static final String cDuration = fxLang_PackageString + ".Duration";

    /**
     * Misc strings
     */
    public static final String boundFunctionDollarSuffix = "$$bound$";
    public static final String boundFunctionResult = "$$bound$result$";
    public static final String boundFunctionObjectParamPrefix = "$$boundInstance$";
    public static final String boundFunctionVarNumParamPrefix = "$$boundVarNum$";
    public static final String implFunctionSuffix = "$impl";
    public static final String internalRunFunctionString = Entry.entryMethodName();
    public static final String varDependentsManagerString = "DependentsManager$internal$";
    public static final String varGetMapString = "GETMAP$";
    public static final String zero_DurationFieldName = "javafx.lang.Duration.$ZERO";

    public static final Pattern DATETIME_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT]");

    public static final char typeCharToEscape = '.';
    public static final char escapeTypeChar = '_';

    /**
     * RuntimeMethod
     */
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
     *
     * Format: <className>_<methodName>
     */

    final RuntimeMethod TypeInfo_getTypeInfo;

    final RuntimeMethod Sequences_calculateIntRangeSize;
    final RuntimeMethod Sequences_calculateFloatRangeSize;
    final RuntimeMethod Sequences_convertCharToNumberSequence;
    final RuntimeMethod Sequences_convertNumberSequence;
    final RuntimeMethod Sequences_convertNumberToCharSequence;
    final RuntimeMethod Sequences_deleteIndexed;
    final RuntimeMethod Sequences_deleteSlice;
    final RuntimeMethod Sequences_deleteValue;
    final RuntimeMethod Sequences_deleteAll;
    final RuntimeMethod Sequences_forceNonNull;
    final RuntimeMethod Sequences_fromArray;
    final RuntimeMethod Sequences_getSingleValue;
    final RuntimeMethod Sequences_getNewElements;
    final RuntimeMethod Sequences_insert;
    final RuntimeMethod Sequences_insertBefore;
    final RuntimeMethod Sequences_range;
    final RuntimeMethod Sequences_rangeExclusive;
    final RuntimeMethod Sequences_replaceSlice;
    final RuntimeMethod Sequences_reverse;
    final RuntimeMethod Sequences_set;
    final RuntimeMethod Sequences_singleton;
    final RuntimeMethod Sequences_size;
    final RuntimeMethod Sequences_sizeOfNewElements;
    final RuntimeMethod Sequences_getAsFromNewElements[];
    final RuntimeMethod Sequences_toArray[];

    final RuntimeMethod SequencesRef_save;

    final RuntimeMethod Util_isEqual; //TODO: replace uses with Checks_equals
    final RuntimeMethod Util_objectTo[];

    final RuntimeMethod Checks_equals;
    final RuntimeMethod Checks_isNull;

    final RuntimeMethod Math_min;
    final RuntimeMethod Math_max;

    final RuntimeMethod FXBase_switchDependence;
    final RuntimeMethod FXBase_switchBiDiDependence;
    final RuntimeMethod FXBase_removeDependent;
    final RuntimeMethod FXBase_addDependent;
    final RuntimeMethod FXBase_makeInitMap;

    final RuntimeMethod FXVariable_make;

    final RuntimeMethod Pointer_make;
    final RuntimeMethod Pointer_get;

    final RuntimeMethod Duration_valueOf;

    /**
     * Classes as Name
     */
    final Name cFXObjectName;
    final Name cFXMixinName;
    final Name cJavaLangThreadName;

    /**
     * FXObject method Names
     */
    final Name applyDefaults_FXObjectMethodName;
    final Name count_FXObjectMethodName;
    final Name get_FXObjectMethodName;
    final Name set_FXObjectMethodName;
    final Name invalidate_FXObjectMethodName;
    final Name notifyDependents_FXObjectMethodName;
    final Name getElement_FXObjectMethodName;
    final Name size_FXObjectMethodName;
    final Name update_FXObjectMethodName;
    final Name complete_FXObjectMethodName;
    final Name initialize_FXObjectMethodName;
    final Name userInit_FXObjectMethodName;
    final Name postInit_FXObjectMethodName;
    final Name initVarBits_FXObjectMethodName;

    /**
     * Duration method Names
     */
    final Name add_DurationMethodName;
    final Name sub_DurationMethodName;
    final Name mul_DurationMethodName;
    final Name div_DurationMethodName;
    final Name le_DurationMethodName;
    final Name lt_DurationMethodName;
    final Name ge_DurationMethodName;
    final Name gt_DurationMethodName;
    final Name toMillis_DurationMethodName;
    final Name negate_DurationMethodName;

    /**
     * Sequence method Names
     */
    final Name get_SequenceMethodName;
    final Name getSlice_SequenceMethodName;
    final Name size_SequenceMethodName;
    final Name toArray_SequenceMethodName;
    final Name incrementSharing_SequenceMethodName;

    /**
     * Interpolate method Names
     */
    final Name interpolate_InterpolateMethodName;
    final Name value_InterpolateMethodName;
    final Name target_InterpolateMethodName;

    /**
     * Misc method Names
     */

    final Name get_PointerMethodName;
    final Name make_PointerMethodName;

    final Name values_KeyFrameMethodName;
    final Name time_KeyFrameMethodName;

    final Name invoke_MethodName;
    final Name lambda_MethodName;

    final Name isInitialized_MethodName;
    final Name initFXBase_MethodName;
    final Name start_ThreadMethodName;
    final Name outerAccessor_MethodName;
    final Name main_MethodName;

    final Name[] typedGet_SequenceMethodName;
    final Name[] typedSet_SequenceMethodName;

    /**
     * Method prefixes for attributes as Names
     * mostly for Name comparison
     */
    final Name applyDefaults_AttributeMethodPrefixName;
    final Name evaluate_AttributeMethodPrefixName;
    final Name getMixin_AttributeMethodPrefixName;
    final Name getVOFF_AttributeMethodPrefixName;
    final Name initVarBitsAttributeMethodPrefixName;
    final Name onReplaceAttributeMethodPrefixName;
    final Name setMixin_AttributeMethodPrefixName;

    /**
     * Field Names
     */

    final Name count_FXObjectFieldName;
    final Name outerAccessor_FXObjectFieldName;
    final Name scriptLevelAccess_FXObjectFieldName;

    final Name length_ArrayFieldName;

    final Name defaultingTypeInfo_FieldName;
    final Name emptySequence_FieldName;

    /**
     * Argument Names
     */
    final Name startPos_ArgName;
    final Name endPos_ArgName;
    final Name newLength_ArgName;
    final Name phase_ArgName;
    final Name varNewValue_ArgName;
    final Name oldValue_ArgName;
    final Name newValue_ArgName;
    final Name pos_ArgName;
    final Name varNum_ArgName;
    final Name updateInstance_ArgName;
    final Name obj_ArgName;

    /**
     * Method prefixes for attributes as Name
     *
     * Format: *_AttributeMethodPrefixName
     */
    final Name get_AttributeMethodPrefixName;
    final Name set_AttributeMethodPrefixName;
    final Name be_AttributeMethodPrefixName;

    /**
     * Field prefixes for attributes as Name
     *
     * Format: *_AttributeFieldPrefixName
     */
    final Name offset_AttributeFieldPrefixName;

    /**
     * Names of flags in FXObject
     */
    final Name varFlagActionTest;
    final Name varFlagActionChange;
    final Name varFlagRestrictSet;
    final Name varFlagIS_INVALID;
    final Name varFlagNEEDS_TRIGGER;
    final Name varFlagIS_BOUND;
    final Name varFlagIS_READONLY;
    final Name varFlagDEFAULT_APPLIED;
    final Name varFlagIS_INITIALIZED;
    final Name varFlagVALIDITY_FLAGS;
    final Name varFlagIS_BOUND_INVALID;
    final Name varFlagIS_BOUND_DEFAULT_APPLIED;
    final Name varFlagINIT_NORMAL;
    final Name varFlagINIT_OBJ_LIT;
    final Name varFlagINIT_OBJ_LIT_DEFAULT;
    final Name varFlagINIT_READONLY;
    final Name varFlagINIT_BOUND;
    final Name varFlagINIT_BOUND_READONLY;
    final Name varFlagALL_FLAGS;

    /**
     * Packages as Name
     */
    public final Name runtime_PackageName;
    public final Name annotation_PackageName;
    public final Name sequence_PackageName;
    public final Name functions_PackageName;
    public final Name javaLang_PackageName;
    public final Name boundFunctionResultName;

    /**
     * Misc Names
     */

    final Name mixinClassSuffixName;
    final Name lengthSuffixName;
    final Name deprecatedInterfaceSuffixName;
    final Name scriptLevelAccess_FXObjectMethodPrefixName;
    final Name userRunFunctionName;
    final Name internalRunFunctionName;
    final Name receiverName;
    final Name typeParameterName;
    final Name scriptClassSuffixName;
    final Name varOldValue_LocalVarName;
    final Name internalSuffixName;

    final Name init_MethodSymbolName;
    final Name postinit_MethodSymbolName;

    final Name varOFF$valueName;

    /**
     * Type Kinds
     */
    public static final int TYPE_KIND_OBJECT = 0;
    public static final int TYPE_KIND_BOOLEAN = 1;
    public static final int TYPE_KIND_CHAR = 2;
    public static final int TYPE_KIND_BYTE = 3;
    public static final int TYPE_KIND_SHORT = 4;
    public static final int TYPE_KIND_INT = 5;
    public static final int TYPE_KIND_LONG = 6;
    public static final int TYPE_KIND_FLOAT = 7;
    public static final int TYPE_KIND_DOUBLE = 8;
    public static final int TYPE_KIND_SEQUENCE = 9;
    public static final int TYPE_KIND_COUNT = 10;

    static final String[] typePrefixes = new String[]{"Object", "Boolean", "Char", "Byte", "Short", "Int", "Long", "Float", "Double", "Sequence"};
    static final String[] accessorSuffixes = new String[]{"", "AsBoolean", "AsChar", "AsByte", "AsShort", "AsInt", "AsLong", "AsFloat", "AsDouble", "AsSequence"};

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
        final JavafxSymtab syms = (JavafxSymtab) (JavafxSymtab.instance(context));

        // Initialize Duration method names
        add_DurationMethodName = names.fromString("add");
        sub_DurationMethodName = names.fromString("sub");
        mul_DurationMethodName = names.fromString("mul");
        div_DurationMethodName = names.fromString("div");
        le_DurationMethodName = names.fromString("le");
        lt_DurationMethodName = names.fromString("lt");
        ge_DurationMethodName = names.fromString("ge");
        gt_DurationMethodName = names.fromString("gt");
        toMillis_DurationMethodName = names.fromString("toMillis");
        negate_DurationMethodName = names.fromString("negate");

        cFXObjectName = names.fromString(cFXObject);
        cFXMixinName = names.fromString(cFXMixin);
        mixinClassSuffixName = names.fromString(mixinClassSuffix);
        lengthSuffixName = names.fromString("$length");
        deprecatedInterfaceSuffixName = names.fromString(deprecatedInterfaceSuffix);
        userRunFunctionName = names.fromString("run");
        internalRunFunctionName = names.fromString(internalRunFunctionString);
        main_MethodName = names.fromString("main");
        receiverName = names.fromString("receiver$");
        initialize_FXObjectMethodName = names.fromString("initialize$");
        notifyDependents_FXObjectMethodName = names.fromString("notifyDependents$");
        complete_FXObjectMethodName = names.fromString("complete$");
        outerAccessor_MethodName = names.fromString("accessOuter$");
        get_PointerMethodName = names.fromString("get");
        get_SequenceMethodName = names.fromString("get");
        getSlice_SequenceMethodName = names.fromString("getSlice");
        size_SequenceMethodName = names.fromString("size");
        defaultingTypeInfo_FieldName = names.fromString("$TYPE_INFO");
        toArray_SequenceMethodName = names.fromString("toArray");
        make_PointerMethodName = names.fromString("make");
        invoke_MethodName = names.fromString("invoke");
        lambda_MethodName = names.fromString("lambda");
        length_ArrayFieldName = names.fromString("length");
        emptySequence_FieldName = names.fromString("emptySequence");
        isInitialized_MethodName = names.fromString("isInitialized");
        offset_AttributeFieldPrefixName = names.fromString(offset_AttributeFieldPrefix);
        count_FXObjectFieldName = names.fromString(count_FXObjectFieldString);
        scriptClassSuffixName = names.fromString(scriptClassSuffix);
        typeParameterName = names.fromString("T");
        init_MethodSymbolName = names.fromString("$init$def$name");
        postinit_MethodSymbolName = names.fromString("$postinit$def$name");
        time_KeyFrameMethodName = names.fromString("time");
        cJavaLangThreadName = names.fromString("java.lang.Thread");
        start_ThreadMethodName = names.fromString("start");
        values_KeyFrameMethodName = names.fromString("values");
        target_InterpolateMethodName = names.fromString("target");
        value_InterpolateMethodName = names.fromString("value");
        interpolate_InterpolateMethodName = names.fromString("interpolate");
        initFXBase_MethodName = names.fromString("initFXBase$");
        userInit_FXObjectMethodName = names.fromString("userInit$");
        postInit_FXObjectMethodName = names.fromString("postInit$");
        incrementSharing_SequenceMethodName = names.fromString("incrementSharing");
        oldValue_ArgName = names.fromString("oldValue$");
        newValue_ArgName = names.fromString("newValue$");
        startPos_ArgName = names.fromString("startPos$");
        endPos_ArgName = names.fromString("endPos$");
        newLength_ArgName = names.fromString("newLength$");
        pos_ArgName = names.fromString("pos$");
        phase_ArgName = names.fromString("phase$");
        internalSuffixName = names.fromString("$internal$");
        boundFunctionResultName = names.fromString(boundFunctionResult);
        get_AttributeMethodPrefixName = names.fromString(get_AttributeMethodPrefix);
        get_FXObjectMethodName = get_AttributeMethodPrefixName;
        set_AttributeMethodPrefixName = names.fromString(set_AttributeMethodPrefix);
        set_FXObjectMethodName = set_AttributeMethodPrefixName;
        be_AttributeMethodPrefixName = names.fromString(be_AttributeMethodPrefix);
        invalidate_FXObjectMethodName = names.fromString(invalidate_AttributeMethodPrefix);
        onReplaceAttributeMethodPrefixName = names.fromString(onReplace_AttributeMethodPrefix);
        evaluate_AttributeMethodPrefixName = names.fromString(evaluate_AttributeMethodPrefix);
        getMixin_AttributeMethodPrefixName = names.fromString(getMixin_AttributeMethodPrefix);
        getVOFF_AttributeMethodPrefixName = names.fromString(getVOFF_AttributeMethodPrefix);
        setMixin_AttributeMethodPrefixName = names.fromString(setMixin_AttributeMethodPrefix);
        initVarBitsAttributeMethodPrefixName = names.fromString(initVarBits_AttributeMethodPrefix);
        initVarBits_FXObjectMethodName = initVarBitsAttributeMethodPrefixName;
        applyDefaults_FXObjectMethodName = names.fromString(applyDefaults_AttributeMethodPrefix);
        applyDefaults_AttributeMethodPrefixName = names.fromString(applyDefaults_AttributeMethodPrefix);
        update_FXObjectMethodName = names.fromString("update$");
        getElement_FXObjectMethodName = names.fromString(getElement_AttributeMethodPrefix);
        size_FXObjectMethodName = names.fromString(size_AttributeMethodPrefix);
        count_FXObjectMethodName = names.fromString("count$");
        varOldValue_LocalVarName = names.fromString("varOldValue$");
        varNewValue_ArgName = names.fromString("varNewValue$");
        scriptLevelAccess_FXObjectFieldName = names.fromString("$scriptLevel$");
        scriptLevelAccess_FXObjectMethodPrefixName = names.fromString("access$scriptLevel$");
        outerAccessor_FXObjectFieldName = names.fromString("accessOuterField$");
        updateInstance_ArgName = names.fromString("instance$");
        obj_ArgName = names.fromString("object$");
        varNum_ArgName = names.fromString("varNum$");

        varFlagActionTest = names.fromString("varTestBits$");
        varFlagActionChange = names.fromString("varChangeBits$");
        varFlagRestrictSet = names.fromString("restrictSet$");

        // Initialize VFLG Names
        varFlagIS_INVALID = names.fromString("VFLGS$IS_INVALID");
        varFlagNEEDS_TRIGGER = names.fromString("VFLGS$NEEDS_TRIGGER");
        varFlagIS_BOUND = names.fromString("VFLGS$IS_BOUND");
        varFlagIS_READONLY = names.fromString("VFLGS$IS_READONLY");
        varFlagDEFAULT_APPLIED = names.fromString("VFLGS$DEFAULT_APPLIED");
        varFlagIS_INITIALIZED = names.fromString("VFLGS$IS_INITIALIZED");
        varFlagVALIDITY_FLAGS = names.fromString("VFLGS$VALIDITY_FLAGS");
        varFlagIS_BOUND_INVALID = names.fromString("VFLGS$IS_BOUND_INVALID");
        varFlagIS_BOUND_DEFAULT_APPLIED = names.fromString("VFLGS$IS_BOUND_DEFAULT_APPLIED");
        varFlagINIT_NORMAL = names.fromString("VFLGS$INIT_NORMAL");
        varFlagINIT_OBJ_LIT = names.fromString("VFLGS$INIT_OBJ_LIT");
        varFlagINIT_OBJ_LIT_DEFAULT = names.fromString("VFLGS$INIT_OBJ_LIT_DEFAULT");
        varFlagINIT_READONLY = names.fromString("VFLGS$INIT_READONLY");
        varFlagINIT_BOUND = names.fromString("VFLGS$INIT_BOUND");
        varFlagINIT_BOUND_READONLY = names.fromString("VFLGS$INIT_BOUND_READONLY");
        varFlagALL_FLAGS = names.fromString("VFLGS$ALL_FLAGS");

        varOFF$valueName = names.fromString("VOFF$value");

        runtime_PackageName = names.fromString(runtime_PackageString);
        annotation_PackageName = names.fromString(annotation_PackageString);
        javaLang_PackageName = names.fromString(javaLang_PackageString);
        sequence_PackageName = names.fromString(sequence_PackageString);
        functions_PackageName = names.fromString(functions_PackageString);

        // Initialize RuntimeMethods
        TypeInfo_getTypeInfo = new RuntimeMethod(names, cTypeInfo, "getTypeInfo");

        Sequences_convertNumberSequence = new RuntimeMethod(names, cSequences, "convertNumberSequence");
        Sequences_convertCharToNumberSequence = new RuntimeMethod(names, cSequences, "convertCharToNumberSequence");
        Sequences_convertNumberToCharSequence = new RuntimeMethod(names, cSequences, "convertNumberToCharSequence");
        Sequences_forceNonNull = new RuntimeMethod(names, cSequences, "forceNonNull");
        Sequences_fromArray = new RuntimeMethod(names, cSequences, "fromArray");
        Sequences_sizeOfNewElements = new RuntimeMethod(names, cSequences, "sizeOfNewElements");
        Sequences_getNewElements = new RuntimeMethod(names, cSequences, "getNewElements");
        Sequences_getSingleValue = new RuntimeMethod(names, cSequences, "getSingleValue");
        Sequences_range = new RuntimeMethod(names, cSequences, "range");
        Sequences_rangeExclusive = new RuntimeMethod(names, cSequences, "rangeExclusive");
        Sequences_singleton = new RuntimeMethod(names, cSequences, "singleton");
        Sequences_size = new RuntimeMethod(names, cSequences, "size");
        Sequences_replaceSlice = new RuntimeMethod(names, cSequences, "replaceSlice");
        Sequences_reverse = new RuntimeMethod(names, cSequences, "reverse");
        Sequences_set = new RuntimeMethod(names, cSequences, "set");
        Sequences_insert = new RuntimeMethod(names, cSequences, "insert");
        Sequences_insertBefore = new RuntimeMethod(names, cSequences, "insertBefore");
        Sequences_deleteIndexed = new RuntimeMethod(names, cSequences, "deleteIndexed");
        Sequences_deleteSlice = new RuntimeMethod(names, cSequences, "deleteSlice");
        Sequences_deleteValue = new RuntimeMethod(names, cSequences, "deleteValue");
        Sequences_deleteAll = new RuntimeMethod(names, cSequences, "deleteAll");
        Sequences_calculateIntRangeSize = new RuntimeMethod(names, cSequences, "calculateIntRangeSize");
        Sequences_calculateFloatRangeSize = new RuntimeMethod(names, cSequences, "calculateFloatRangeSize");

        Sequences_getAsFromNewElements = new RuntimeMethod[TYPE_KIND_COUNT];
        Sequences_toArray = new RuntimeMethod[TYPE_KIND_COUNT];
        for (int kind = 0; kind < TYPE_KIND_COUNT; kind++) {
            Sequences_getAsFromNewElements[kind] = new RuntimeMethod(names, cSequences, "get" + accessorSuffixes[kind] + "FromNewElements");
            Sequences_toArray[kind] = new RuntimeMethod(names, cSequences, "to" + typePrefixes[kind] + "Array");
        }

        SequencesRef_save = new RuntimeMethod(names, cSequenceRef, "save");

        Util_isEqual = new RuntimeMethod(names, cUtil, "isEqual");
        Util_objectTo = new RuntimeMethod[TYPE_KIND_COUNT];
        for (int kind = 0; kind < TYPE_KIND_COUNT; kind++) {
            Util_objectTo[kind] = new RuntimeMethod(names, cUtil, "objectTo" + typePrefixes[kind]);
        }

        Checks_equals = new RuntimeMethod(names, cChecks, "equals"); //TODO: looks like dup
        Checks_isNull = new RuntimeMethod(names, cChecks, "isNull");

        Math_min = new RuntimeMethod(names, cMath, "min");
        Math_max = new RuntimeMethod(names, cMath, "max");

        FXBase_switchDependence = new RuntimeMethod(names, cFXBase, "switchDependence$");
        FXBase_switchBiDiDependence = new RuntimeMethod(names, cFXBase, "switchBiDiDependence$");
        FXBase_removeDependent = new RuntimeMethod(names, cFXBase, "removeDependent$");
        FXBase_addDependent = new RuntimeMethod(names, cFXBase, "addDependent$");
        FXBase_makeInitMap = new RuntimeMethod(names, cFXBase, "makeInitMap$");

        FXVariable_make = new RuntimeMethod(names, cFXVariable, "make");

        Pointer_make = new RuntimeMethod(names, cPointer, "make");
        Pointer_get = new RuntimeMethod(names, cPointer, "get");

        Duration_valueOf = new RuntimeMethod(names, cDuration, "valueOf");

        // Initialize per Kind names and types
        typedGet_SequenceMethodName = new Name[TYPE_KIND_COUNT];
        typedSet_SequenceMethodName = new Name[TYPE_KIND_COUNT];
        for (int kind = 0; kind < TYPE_KIND_COUNT; kind++) {
            typedGet_SequenceMethodName[kind] = names.fromString("get" + accessorSuffixes[kind]);
            typedSet_SequenceMethodName[kind] = names.fromString("set" + accessorSuffixes[kind]);
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

    public static String getTypePrefix(int index) {
        return typePrefixes[index];
    }

    public Name scriptLevelAccessMethod(Name.Table names, Symbol clazz) {
        StringBuilder buf = new StringBuilder();
        buf.append(scriptLevelAccess_FXObjectMethodPrefixName);
        buf.append(clazz.getQualifiedName().toString().replace('.', '$'));
        buf.append('$');
        return names.fromString(buf);
    }
}
