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
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.javafx.code.JavafxTypeRepresentation;
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
    public static final String seq_AttributeMethodPrefix = "seq$";
    public static final String get_AttributeMethodPrefix = "get$";
    public static final String getMixin_AttributeMethodPrefix = "getMixin$";
    public static final String getVOFF_AttributeMethodPrefix = "getVOFF$";
    public static final String initVars_AttributeMethodPrefix = "initVars$";
    public static final String invalidate_AttributeMethodPrefix = "invalidate$";
    public static final String getFlags_AttributeMethodPrefix = "getFlags$";
    public static final String setFlags_AttributeMethodPrefix = "setFlags$";
    public static final String onReplace_AttributeMethodPrefix = "onReplace$";
    public static final String set_AttributeMethodPrefix = "set$";
    public static final String setMixin_AttributeMethodPrefix = "setMixin$";
    public static final String size_AttributeMethodPrefix = "size$";
    public static final String getElement_AttributeMethodPrefix = "elem$";
    public static final String makeForPart_AttributeMethodPrefix = "makeForPart$";

    /**
     * Field prefix strings for attributes
     *
     * Format: *_AttributeFieldNamePrefix
     */
    public static final String value_AttributeFieldPrefix = "$";
    public static final String saved_AttributeFieldPrefix = "saved$";
    public static final String offset_AttributeFieldPrefix = "VOFF$";
    public static final String flags_AttributeFieldPrefix = "VFLG$";

    /**
     * Prefixes and name strings of generated FXObject fields
     *
     * Format: *_FXObjectFieldNameString
     */
    public static final String count_FXObjectFieldString = "VCNT$";
    public static final String varFlags_FXObjectFieldPrefix = "VFLGS$";
    public static final String varMap_FXObjectFieldPrefix = "MAP$";
    public static final String depCount_FXObjectFieldString = "DCNT$";
    public static final String dep_FXObjectFieldString = "DEP$";
    public static final String funcCount_FXObjectFieldString = "FCNT$";
    public static final String scriptLevelAccess_FXObjectFieldString = "$script$";

    /**
     * Class suffixes
     */
    public static final String mixinClassSuffix = "$Mixin";
    public static final String deprecatedInterfaceSuffix = "$Intf";
    public static final String scriptClassSuffix = "$Script";

    /**
     * Class prefixes
     */
    public static final String boundFunctionClassPrefix = "BFunc$";
    public static final String boundForPartClassPrefix = "ForPart$";
    public static final String localContextClassPrefix = "Local$";
    public static final String anonFunctionClassPrefix = "AnonFunc$";
    
    /**
     * Class name infixes
     */
    public static final String objectLiteralClassInfix = "$ObjLit$";
    

    /**
     * Package name strings
     *
     * Format: <name>_PackageNameString
     */
    public static final String javaLang_PackageString = "java.lang";
    public static final String runtime_PackageString = "com.sun.javafx.runtime";
    public static final String runtimeUtil_PackageString = "com.sun.javafx.runtime.util";
    public static final String annotation_PackageString = "com.sun.javafx.runtime.annotation";
    public static final String functions_PackageString = "com.sun.javafx.functions";
    public static final String sequence_PackageString = "com.sun.javafx.runtime.sequence";
    public static final String fxLang_PackageString = "javafx.lang";
    public static final String fxAnimation_PackageString = "javafx.animation";

    /**
     * Class name strings
     *
     * Format: c<className>
     */
    // in sequence package
    public static final String cSequences = sequence_PackageString + ".Sequences";
    public static final String cSequence = sequence_PackageString + ".Sequence";
    public static final String cSequenceRef = sequence_PackageString + ".SequenceRef";
    public static final String cSequenceProxy = sequence_PackageString + ".SequenceProxy";
    public static final String cArraySequence = sequence_PackageString + ".ArraySequence";
    public static final String cBoundForOverSequence = sequence_PackageString + ".BoundForOverSequence";
    public static final String cBoundForOverNullableSingleton = sequence_PackageString + ".BoundForOverNullableSingleton";
    public static final String cBoundForOverSingleton = sequence_PackageString + ".BoundForOverSingleton";
    public static final String cBoundForPartI = sequence_PackageString + ".BoundFor$FXForPart";
    public static final String cObjectArraySequence = sequence_PackageString + ".ObjectArraySequence";

    // in runtime package -- public
    public static final String cFXBase = runtime_PackageString + ".FXBase";
    public static final String cFXObject = runtime_PackageString + ".FXObject";
    public static final String cErrorHandler = runtime_PackageString + ".ErrorHandler";
    public static final String cFXMixin = runtime_PackageString + ".FXMixin";
    public static final String cTypeInfo = runtime_PackageString + ".TypeInfo";
    public static final String cNonLocalReturnException = runtime_PackageString + ".NonLocalReturnException";
    public static final String cNonLocalBreakException = runtime_PackageString + ".NonLocalBreakException";
    public static final String cNonLocalContinueException = runtime_PackageString + ".NonLocalContinueException";
    // in runtime package
    private static final String cUtil = runtime_PackageString + ".Util";
    private static final String cChecks = runtime_PackageString + ".Checks";
    private static final String cFXConstant = runtime_PackageString + ".FXConstant";
    private static final String cPointer = runtime_PackageString + ".Pointer";
    // in runtime.util package
    private static final String cStringLocalization = runtimeUtil_PackageString + ".StringLocalization";
    private static final String cFXFormatter = runtimeUtil_PackageString + ".FXFormatter";
    // in java.lang package
    private static final String cMath = javaLang_PackageString + ".Math";
    private static final String cString = javaLang_PackageString + ".String";
    // in javafx.lang package
    private static final String cDuration = fxLang_PackageString + ".Duration";
    // in javafx.animation package
    public static final String cKeyValueTargetType = fxAnimation_PackageString + ".KeyValueTarget.Type";

    /**
     * Misc strings
     */
    public static final String internalString = "\u03A7"; // (Chi = X)
    public static final String boundFunctionDollarSuffix = "$bFunc$";
    public static final String boundFunctionResult = "$bFuncRes$";
    public static final String boundFunctionObjectParamPrefix = "$bInst$";
    public static final String boundFunctionVarNumParamPrefix = "$bVarNum$";
    public static final String implFunctionSuffix = "$impl";
    public static final String internalRunFunctionString = Entry.entryMethodName();
    public static final String varGetMapString = "GETMAP$";
    public static final String zero_DurationFieldName = "javafx.lang.Duration.$ZERO";
    public static final String synthForLabelPrefix = "synth_for$";

    public static final Pattern DATETIME_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT]");

    public static final char typeCharToEscape = '.';
    public static final char escapeTypeChar = '_';

    // Must match com.sun.tools.runtime.sequence.Sequences.UNDEFINED_MARKER_INT
    public static final int UNDEFINED_MARKER_INT = -1000;


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
    final RuntimeMethod Sequences_convertObjectToSequence;
    final RuntimeMethod Sequences_copy;
    final RuntimeMethod Sequences_deleteIndexed;
    final RuntimeMethod Sequences_deleteSlice;
    final RuntimeMethod Sequences_deleteValue;
    final RuntimeMethod Sequences_deleteAll;
    final RuntimeMethod Sequences_forceNonNull;
    final RuntimeMethod Sequences_fromArray;
    final RuntimeMethod Sequences_getSingleValue;
    final RuntimeMethod Sequences_getNewElements;
    final RuntimeMethod Sequences_incrementSharing;
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

    final RuntimeMethod Util_objectTo[];

    final RuntimeMethod Checks_equals;
    final RuntimeMethod Checks_isNull;

    final RuntimeMethod Math_min;
    final RuntimeMethod Math_max;

    final RuntimeMethod ErrorHandler_bindException;

    final RuntimeMethod FXBase_switchDependence;
    final RuntimeMethod FXBase_removeDependent;
    final RuntimeMethod FXBase_addDependent;
    final RuntimeMethod FXBase_makeInitMap;

    final RuntimeMethod FXConstant_make;

    final RuntimeMethod Pointer_make;
    final RuntimeMethod Pointer_switchDependence;

    final RuntimeMethod Duration_valueOf;

    final RuntimeMethod StringLocalization_getLocalizedString;
    final RuntimeMethod FXFormatter_sprintf;
    final RuntimeMethod String_format;

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
    final Name invoke_FXObjectMethodName;
    final Name count_FXObjectMethodName;
    final Name get_FXObjectMethodName;
    final Name set_FXObjectMethodName;
    final Name invalidate_FXObjectMethodName;
    final Name getFlags_FXObjectMethodName;
    final Name setFlags_FXObjectMethodName;
    final Name notifyDependents_FXObjectMethodName;
    final Name getElement_FXObjectMethodName;
    final Name size_FXObjectMethodName;
    final Name update_FXObjectMethodName;
    final Name complete_FXObjectMethodName;
    final Name initialize_FXObjectMethodName;
    final Name userInit_FXObjectMethodName;
    final Name postInit_FXObjectMethodName;
    final Name initVars_FXObjectMethodName;
    final Name restrictSet_FXObjectMethodName;
    Name getAs_FXObjectMethodName[];

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

    final Name replaceParts_BoundForMethodName;

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
    final Name getVarNum_PointerMethodName;
    final Name getFXObject_PointerMethodName;
    final Name size_PointerMethodName;

    final Name values_KeyFrameMethodName;
    final Name time_KeyFrameMethodName;

    final Name getIndex_BoundForPartMethodName;
    final Name adjustIndex_BoundForPartMethodName;
    final Name setInductionVar_BoundForPartMethodName;

    final Name lambda_MethodName;

    final Name isInitialized_MethodName;
    final Name isReadOnly_MethodName;
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
    final Name getMixin_AttributeMethodPrefixName;
    final Name getVOFF_AttributeMethodPrefixName;
    final Name initVarsAttributeMethodPrefixName;
    final Name onReplaceAttributeMethodPrefixName;
    final Name setMixin_AttributeMethodPrefixName;

    /**
     * Field Names
     */

    final Name count_FXObjectFieldName;
    final Name depCount_FXObjectFieldName;
    final Name funcCount_FXObjectFieldName;
    final Name outerAccessor_FXObjectFieldName;
    final Name scriptLevelAccess_FXObjectFieldName;

    final Name value_NonLocalReturnExceptionFieldName;

    final Name length_ArrayFieldName;

    final Name defaultingTypeInfo_FieldName;
    final Name emptySequence_FieldName;

    final Name partResultVarNum_BoundForHelper;
    
    // fields of the enum javafx.animation.KeyValueTarget.Type
    final Name BYTE_KeyValueTargetTypeFieldName;
    final Name SHORT_KeyValueTargetTypeFieldName;
    final Name INTEGER_KeyValueTargetTypeFieldName;
    final Name LONG_KeyValueTargetTypeFieldName;
    final Name FLOAT_KeyValueTargetTypeFieldName;
    final Name DOUBLE_KeyValueTargetTypeFieldName;
    final Name BOOLEAN_KeyValueTargetTypeFieldName;
    final Name SEQUENCE_KeyValueTargetTypeFieldName;
    final Name OBJECT_KeyValueTargetTypeFieldName;

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
    final Name depNum_ArgName;
    final Name clearBits_ArgName;
    final Name setBits_ArgName;
    final Name updateInstance_ArgName;
    final Name obj_ArgName;
    final Name value_ArgName;
    final Name number_ArgName;
    final Name arg1_ArgName;
    final Name arg2_ArgName;
    final Name args_ArgName;

    /**
     * Method prefixes for attributes as Name
     *
     * Format: *_AttributeMethodPrefixName
     */
    final Name get_AttributeMethodPrefixName;
    final Name set_AttributeMethodPrefixName;
    final Name seq_AttributeMethodPrefixName;

    /**
     * Field prefixes for attributes as Name
     *
     * Format: *_AttributeFieldPrefixName
     */
    final Name offset_AttributeFieldPrefixName;
    final Name flags_AttributeFieldPrefixName;

    /**
     * Names of flags in FXObject
     */
    final Name varFlagActionTest;
    final Name varFlagActionChange;

    final Name varFlagRESTING_STATE_BIT;
    final Name varFlagBE_STATE_BIT;
    final Name varFlagINVALID_STATE_BIT;
    final Name varFlagDEFAULT_STATE_BIT;
    final Name varFlagINITIALIZED_STATE_BIT;
    final Name varFlagIS_EAGER;
    final Name varFlagSEQUENCE_LIVE;
    final Name varFlagIS_BOUND;
    final Name varFlagIS_READONLY;
    final Name varFlagFORWARD_ACCESS;

    final Name varFlagSTATE_MASK;

    final Name varFlagSTATE_VALID;
    final Name varFlagSTATE_CASCADE_INVALID;
    final Name varFlagSTATE_BE_INVALID;
    final Name varFlagSTATE_TRIGGERED;
    
    final Name varFlagINIT_MASK;
    final Name varFlagINIT_WITH_AWAIT_MASK;
    
    final Name varFlagINIT_PENDING;
    final Name varFlagINIT_AWAIT_VARINIT;
    final Name varFlagINIT_READY;
    final Name varFlagINIT_INITIALIZED;
    final Name varFlagINIT_INITIALIZED_DEFAULT;
    
    final Name varFlagINIT_BOUND_MASK;
    final Name varFlagINIT_STATE_MASK;
    
    final Name varFlagIS_BOUND_READONLY;
    final Name varFlagIS_BOUND_INVALID;
    final Name varFlagVALID_DEFAULT_APPLIED;
    final Name varFlagINIT_INITIALIZED_DEFAULT_READONLY;
    final Name varFlagINIT_OBJ_LIT;
    final Name varFlagINIT_OBJ_LIT_SEQUENCE;
    
    final Name varFlagALL_FLAGS;

    /**
     * Names of phase transitions
     */
    final Name phaseTransitionBE_INVALIDATE;
    final Name phaseTransitionBE_TRIGGER;
    final Name phaseTransitionCASCADE_INVALIDATE;
    final Name phaseTransitionCASCADE_TRIGGER;

    final Name phaseTransitionCLEAR_BE;
    final Name phaseTransitionNEXT_STATE_SHIFT;

    final Name phaseTransitionPHASE;
    final Name phaseINVALIDATE;
    final Name phaseTRIGGER;

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

    public final Name scriptClassSuffixName;
    final Name mixinClassSuffixName;
    final Name lengthSuffixName;
    final Name deprecatedInterfaceSuffixName;
    final Name userRunFunctionName;
    final Name internalRunFunctionName;
    final Name receiverName;
    final Name typeParameterName;
    final Name varState_LocalVarName;
    final Name varOldValue_LocalVarName;
    final Name varFlags_LocalVarName;
    final Name wasInvalid_LocalVarName;
    final Name internalSuffixName;
    final Name internalNameMarker;

    final Name init_MethodSymbolName;
    final Name postinit_MethodSymbolName;

    final Name varOFF$valueName;

    final Name boundForPartName;

    static final String[] typePrefixes = new String[]{"Object", "Boolean", "Char", "Byte", "Short", "Int", "Long", "Float", "Double", "Sequence"};
    static final String[] accessorSuffixes = new String[]{"", "AsBoolean", "AsChar", "AsByte", "AsShort", "AsInt", "AsLong", "AsFloat", "AsDouble", "AsSequence"};
 
    /**
     * Accessor prefixes.
     */
    public final Name[] accessorPrefixes;

    /**
     * Context set-up
     */

    public static final Context.Key<JavafxDefs> jfxDefsKey = new Context.Key<JavafxDefs>();

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
        getIndex_BoundForPartMethodName = names.fromString("getIndex$");
        adjustIndex_BoundForPartMethodName = names.fromString("adjustIndex$");
        setInductionVar_BoundForPartMethodName = names.fromString("setInductionVar$");
        get_PointerMethodName = names.fromString("get");
        get_SequenceMethodName = names.fromString("get");
        getSlice_SequenceMethodName = names.fromString("getSlice");
        size_SequenceMethodName = names.fromString("size");
        defaultingTypeInfo_FieldName = names.fromString("$TYPE_INFO");
        toArray_SequenceMethodName = names.fromString("toArray");
        make_PointerMethodName = names.fromString("make");
        getVarNum_PointerMethodName = names.fromString("getVarNum");
        getFXObject_PointerMethodName = names.fromString("getFXObject");
        size_PointerMethodName = size_SequenceMethodName;
        lambda_MethodName = names.fromString("lambda");
        length_ArrayFieldName = names.fromString("length");
        emptySequence_FieldName = names.fromString("emptySequence");
        partResultVarNum_BoundForHelper = names.fromString("partResultVarNum");
        isInitialized_MethodName = names.fromString("isInitialized");
        isReadOnly_MethodName = names.fromString("isReadOnly");
        offset_AttributeFieldPrefixName = names.fromString(offset_AttributeFieldPrefix);
        flags_AttributeFieldPrefixName = names.fromString(flags_AttributeFieldPrefix);
        count_FXObjectFieldName = names.fromString(count_FXObjectFieldString);
        depCount_FXObjectFieldName = names.fromString(depCount_FXObjectFieldString);
        funcCount_FXObjectFieldName = names.fromString(funcCount_FXObjectFieldString);
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
        internalNameMarker = names.fromString(internalString);
        boundFunctionResultName = names.fromString(boundFunctionResult);
        get_AttributeMethodPrefixName = names.fromString(get_AttributeMethodPrefix);
        get_FXObjectMethodName = get_AttributeMethodPrefixName;
        set_AttributeMethodPrefixName = names.fromString(set_AttributeMethodPrefix);
        set_FXObjectMethodName = set_AttributeMethodPrefixName;
        seq_AttributeMethodPrefixName = names.fromString(seq_AttributeMethodPrefix);
        replaceParts_BoundForMethodName = names.fromString("replaceParts");
        invalidate_FXObjectMethodName = names.fromString(invalidate_AttributeMethodPrefix);
        getFlags_FXObjectMethodName = names.fromString(getFlags_AttributeMethodPrefix);
        setFlags_FXObjectMethodName = names.fromString(setFlags_AttributeMethodPrefix);
        onReplaceAttributeMethodPrefixName = names.fromString(onReplace_AttributeMethodPrefix);
        getMixin_AttributeMethodPrefixName = names.fromString(getMixin_AttributeMethodPrefix);
        getVOFF_AttributeMethodPrefixName = names.fromString(getVOFF_AttributeMethodPrefix);
        setMixin_AttributeMethodPrefixName = names.fromString(setMixin_AttributeMethodPrefix);
        initVarsAttributeMethodPrefixName = names.fromString(initVars_AttributeMethodPrefix);
        initVars_FXObjectMethodName = initVarsAttributeMethodPrefixName;
        applyDefaults_FXObjectMethodName = names.fromString(applyDefaults_AttributeMethodPrefix);
        invoke_FXObjectMethodName = names.fromString("invoke$");
        applyDefaults_AttributeMethodPrefixName = names.fromString(applyDefaults_AttributeMethodPrefix);
        update_FXObjectMethodName = names.fromString("update$");
        getElement_FXObjectMethodName = names.fromString(getElement_AttributeMethodPrefix);
        size_FXObjectMethodName = names.fromString(size_AttributeMethodPrefix);
        count_FXObjectMethodName = names.fromString("count$");
        restrictSet_FXObjectMethodName = names.fromString("restrictSet$");
        varState_LocalVarName = names.fromString("varState$");
        varOldValue_LocalVarName = names.fromString("varOldValue$");
        varFlags_LocalVarName = names.fromString("varFlags$");
        wasInvalid_LocalVarName = names.fromString("wasInvalid$");
        varNewValue_ArgName = names.fromString("varNewValue$");
        value_NonLocalReturnExceptionFieldName = names.fromString("value");
        outerAccessor_FXObjectFieldName = names.fromString("accessOuterField$");
        updateInstance_ArgName = names.fromString("instance$");
        obj_ArgName = names.fromString("object$");
        value_ArgName = names.fromString("value$");
        number_ArgName = names.fromString("number$");
        arg1_ArgName = names.fromString("arg1$");
        arg2_ArgName = names.fromString("arg2$");
        args_ArgName = names.fromString("args$");
        varNum_ArgName = names.fromString("varNum$");
        depNum_ArgName = names.fromString("depNum$");
        clearBits_ArgName = names.fromString("clearBits$");
        setBits_ArgName = names.fromString("setBits$");
        scriptClassSuffixName = names.fromString(scriptClassSuffix);
        scriptLevelAccess_FXObjectFieldName = names.fromString(scriptLevelAccess_FXObjectFieldString);

        // KeyValueTarget.Type field names
        BYTE_KeyValueTargetTypeFieldName = names.fromString("BYTE");
        SHORT_KeyValueTargetTypeFieldName = names.fromString("SHORT");
        INTEGER_KeyValueTargetTypeFieldName = names.fromString("INTEGER");
        LONG_KeyValueTargetTypeFieldName = names.fromString("LONG");
        FLOAT_KeyValueTargetTypeFieldName = names.fromString("FLOAT");
        DOUBLE_KeyValueTargetTypeFieldName = names.fromString("DOUBLE");
        BOOLEAN_KeyValueTargetTypeFieldName = names.fromString("BOOLEAN");
        SEQUENCE_KeyValueTargetTypeFieldName = names.fromString("SEQUENCE");
        OBJECT_KeyValueTargetTypeFieldName = names.fromString("OBJECT");


        varFlagActionTest = names.fromString("varTestBits$");
        varFlagActionChange = names.fromString("varChangeBits$");
        
        // Initialize VFLG Names
        varFlagRESTING_STATE_BIT                 = names.fromString("VFLGS$RESTING_STATE_BIT");
        varFlagBE_STATE_BIT                      = names.fromString("VFLGS$BE_STATE_BIT");
        varFlagINVALID_STATE_BIT                 = names.fromString("VFLGS$INVALID_STATE_BIT");
        varFlagDEFAULT_STATE_BIT                 = names.fromString("VFLGS$DEFAULT_STATE_BIT");
        varFlagINITIALIZED_STATE_BIT             = names.fromString("VFLGS$INITIALIZED_STATE_BIT");
        varFlagIS_EAGER                          = names.fromString("VFLGS$IS_EAGER");
        varFlagSEQUENCE_LIVE                     = names.fromString("VFLGS$SEQUENCE_LIVE");
        varFlagIS_BOUND                          = names.fromString("VFLGS$IS_BOUND");
        varFlagIS_READONLY                       = names.fromString("VFLGS$IS_READONLY");
        varFlagFORWARD_ACCESS                    = names.fromString("VFLGS$FORWARD_ACCESS");
        
        varFlagSTATE_MASK                        = names.fromString("VFLGS$STATE_MASK");
        
        varFlagSTATE_VALID                       = names.fromString("VFLGS$STATE$VALID");
        varFlagSTATE_CASCADE_INVALID             = names.fromString("VFLGS$STATE$CASCADE_INVALID");
        varFlagSTATE_BE_INVALID                  = names.fromString("VFLGS$STATE$BE_INVALID");
        varFlagSTATE_TRIGGERED                   = names.fromString("VFLGS$STATE$TRIGGERED");
        
        varFlagINIT_MASK                         = names.fromString("VFLGS$INIT$MASK");
        varFlagINIT_WITH_AWAIT_MASK              = names.fromString("VFLGS$INIT_WITH_AWAIT$MASK");
        
        varFlagINIT_PENDING                      = names.fromString("VFLGS$INIT$PENDING");
        varFlagINIT_AWAIT_VARINIT                = names.fromString("VFLGS$INIT$AWAIT_VARINIT");
        varFlagINIT_READY                        = names.fromString("VFLGS$INIT$READY");
        varFlagINIT_INITIALIZED                  = names.fromString("VFLGS$INIT$INITIALIZED");
        varFlagINIT_INITIALIZED_DEFAULT          = names.fromString("VFLGS$INIT$INITIALIZED_DEFAULT");
        
        varFlagINIT_BOUND_MASK                   = names.fromString("VFLGS$INIT$BOUND_MASK");
        varFlagINIT_STATE_MASK                   = names.fromString("VFLGS$INIT$STATE$MASK");
        
        varFlagIS_BOUND_READONLY                 = names.fromString("VFLGS$IS_BOUND_READONLY");
        varFlagIS_BOUND_INVALID                  = names.fromString("VFLGS$IS_BOUND_INVALID");
        varFlagVALID_DEFAULT_APPLIED             = names.fromString("VFLGS$VALID_DEFAULT_APPLIED");
        varFlagINIT_INITIALIZED_DEFAULT_READONLY = names.fromString("VFLGS$INIT$INITIALIZED_DEFAULT_READONLY");
        varFlagINIT_OBJ_LIT                      = names.fromString("VFLGS$INIT_OBJ_LIT");
        varFlagINIT_OBJ_LIT_SEQUENCE             = names.fromString("VFLGS$INIT_OBJ_LIT_SEQUENCE");

        
        varFlagALL_FLAGS                         = names.fromString("VFLGS$ALL_FLAGS");


        phaseTransitionBE_INVALIDATE = names.fromString("PHASE_TRANS$BE_INVALIDATE");
        phaseTransitionBE_TRIGGER = names.fromString("PHASE_TRANS$BE_TRIGGER");
        phaseTransitionCASCADE_INVALIDATE = names.fromString("PHASE_TRANS$CASCADE_INVALIDATE");
        phaseTransitionCASCADE_TRIGGER = names.fromString("PHASE_TRANS$CASCADE_TRIGGER");
        phaseTransitionCLEAR_BE = names.fromString("PHASE_TRANS$CLEAR_BE");
        phaseTransitionNEXT_STATE_SHIFT = names.fromString("PHASE_TRANS$NEXT_STATE_SHIFT");
        phaseTransitionPHASE = names.fromString("PHASE_TRANS$PHASE");
        phaseINVALIDATE = names.fromString("PHASE$INVALIDATE");
        phaseTRIGGER = names.fromString("PHASE$TRIGGER");

        varOFF$valueName = names.fromString("VOFF$value");

        boundForPartName = names.fromString(boundForPartClassPrefix);
        
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
        Sequences_convertObjectToSequence = new RuntimeMethod(names, cSequences, "convertObjectToSequence");
        Sequences_copy = new RuntimeMethod(names, cSequences, "copy");
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
        Sequences_incrementSharing = new RuntimeMethod(names, cSequences, "incrementSharing");
        Sequences_insert = new RuntimeMethod(names, cSequences, "insert");
        Sequences_insertBefore = new RuntimeMethod(names, cSequences, "insertBefore");
        Sequences_deleteIndexed = new RuntimeMethod(names, cSequences, "deleteIndexed");
        Sequences_deleteSlice = new RuntimeMethod(names, cSequences, "deleteSlice");
        Sequences_deleteValue = new RuntimeMethod(names, cSequences, "deleteValue");
        Sequences_deleteAll = new RuntimeMethod(names, cSequences, "deleteAll");
        Sequences_calculateIntRangeSize = new RuntimeMethod(names, cSequences, "calculateIntRangeSize");
        Sequences_calculateFloatRangeSize = new RuntimeMethod(names, cSequences, "calculateFloatRangeSize");

        int typeRepCnt = JavafxTypeRepresentation.values().length;
        Sequences_getAsFromNewElements = new RuntimeMethod[typeRepCnt];
        Sequences_toArray = new RuntimeMethod[typeRepCnt];
        getAs_FXObjectMethodName = new Name[typeRepCnt];
        for (int kind = 0; kind < typeRepCnt; kind++) {
            Sequences_getAsFromNewElements[kind] = new RuntimeMethod(names, cSequences, "get" + accessorSuffixes[kind] + "FromNewElements");
            Sequences_toArray[kind] = new RuntimeMethod(names, cSequences, "to" + typePrefixes[kind] + "Array");
            getAs_FXObjectMethodName[kind] = names.fromString("get" + accessorSuffixes[kind] + "$");
        }

        Util_objectTo = new RuntimeMethod[typeRepCnt];
        for (int kind = 0; kind < typeRepCnt; kind++) {
            Util_objectTo[kind] = new RuntimeMethod(names, cUtil, "objectTo" + typePrefixes[kind]);
        }

        Checks_equals = new RuntimeMethod(names, cChecks, "equals"); 
        Checks_isNull = new RuntimeMethod(names, cChecks, "isNull");

        Math_min = new RuntimeMethod(names, cMath, "min");
        Math_max = new RuntimeMethod(names, cMath, "max");


        ErrorHandler_bindException = new RuntimeMethod(names, cErrorHandler, "bindException");

        FXBase_switchDependence = new RuntimeMethod(names, cFXBase, "switchDependence$");
        FXBase_removeDependent = new RuntimeMethod(names, cFXBase, "removeDependent$");
        FXBase_addDependent = new RuntimeMethod(names, cFXBase, "addDependent$");
        FXBase_makeInitMap = new RuntimeMethod(names, cFXBase, "makeInitMap$");

        FXConstant_make = new RuntimeMethod(names, cFXConstant, "make");

        Pointer_make = new RuntimeMethod(names, cPointer, "make");
        Pointer_switchDependence = new RuntimeMethod(names, cPointer, "switchDependence");

        Duration_valueOf = new RuntimeMethod(names, cDuration, "valueOf");

        StringLocalization_getLocalizedString = new RuntimeMethod(names, cStringLocalization, "getLocalizedString");
        FXFormatter_sprintf = new RuntimeMethod(names, cFXFormatter, "sprintf");
        String_format = new RuntimeMethod(names, cString, "format");

        accessorPrefixes = new Name[] {
                get_AttributeMethodPrefixName,
                set_AttributeMethodPrefixName,
                seq_AttributeMethodPrefixName,
                invalidate_FXObjectMethodName,
                getFlags_FXObjectMethodName,
                setFlags_FXObjectMethodName,
                onReplaceAttributeMethodPrefixName,
                getElement_FXObjectMethodName,
                size_FXObjectMethodName,
                count_FXObjectFieldName,
                depCount_FXObjectFieldName,
                funcCount_FXObjectFieldName
             };
     
        // Initialize per Kind names and types
        typedGet_SequenceMethodName = new Name[typeRepCnt];
        typedSet_SequenceMethodName = new Name[typeRepCnt];
        for (int kind = 0; kind < typeRepCnt; kind++) {
            typedGet_SequenceMethodName[kind] = names.fromString("get" + accessorSuffixes[kind]);
            typedSet_SequenceMethodName[kind] = names.fromString("set" + accessorSuffixes[kind]);
        }
    }

    public static String getTypePrefix(int index) {
        return typePrefixes[index];
    }

    public Name scriptLevelAccessField(Name.Table names, Symbol clazz) {
        return names.fromString(scriptLevelAccess_FXObjectFieldString + mangleClassName(clazz, true) + "$");
    }
    
    public String mangleClassName(Symbol clazz, boolean useFull) {
        if (useFull) {
            return clazz.getQualifiedName().toString().replace('.', '$');
        } else {
            return clazz.name.toString();
        }
    }

    // Name prefixes used for synthetic variable names.

    public String itemNamePrefix() {
        return "item";
    }

    public String valueNamePrefix() {
        return "value";
    }

    public String ignoreNamePrefix() {
        return "ignore";
    }

    public String saveNamePrefix() {
        return "save";
    }

    public String sizeNamePrefix() {
        return "size";
    }

    public String lengthNamePrefix() {
        return "length";
    }

    public String lowNamePrefix() {
        return "lowestInvalid";
    }

    public String highNamePrefix() {
        return "highestInvalid";
    }

    public String pendingNamePrefix() {
        return "pending";
    }

    public String deltaNamePrefix() {
        return "delta";
    }

    public String cngStartNamePrefix() {
        return "changeStart";
    }

    public String cngEndNamePrefix() {
        return "changeEnd";
    }

    public String lowerNamePrefix() {
        return "lower";
    }

    public String upperNamePrefix() {
        return "upper";
    }

    public String stepNamePrefix() {
        return "step";
    }

    public String functionResultNamePrefix() {
        return "functionResult";
    }

    public String helperDollarNamePrefix() {
        return "helper$";
    }

    public String condNamePrefix() {
        return "cond";
    }

    public String thenNamePrefix() {
        return "then";
    }

    public String elseNamePrefix() {
        return "else";
    }

    public String doitDollarNamePrefix() {
        return "doit$$";
    }

    public String resDollarNamePrefix() {
        return "res$";
    }

    public String exceptionDollarNamePrefix() {
        return "expt$";
    }

    public String dollarIndexNamePrefix() {
        return "$index$";
    }

    public String resultDollarNamePrefix() {
        return "$result$";
    }

    public String posNamePrefix() {
        return "pos";
    }

    public String indexNamePrefix() {
        return "index";
    }

    public String exprNamePrefix() {
        return "expr";
    }

    public String oldValueNamePrefix() {
        return "oldValue";
    }

    public String resNamePrefix() {
        return "res";
    }
}
