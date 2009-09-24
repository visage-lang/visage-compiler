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
import java.util.regex.Pattern;
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
    public static final String attributeBeMethodNamePrefix = "be$";
    public static final String attributeInvalidateMethodNamePrefix = "invalidate$";
    public static final String attributeOnReplaceMethodNamePrefix = "onReplace$";
    public static final String attributeEvaluateMethodNamePrefix = "evaluate$";
    public static final String attributeNotifyDependentsNameString = "notifyDependents$";
    public static final String attributeApplyDefaultsMethodNamePrefix = "applyDefaults$";
    public static final String attributeApplyDefaultsBaseMethodName = "applyDefaults$base$";
    public static final String attributeCountMethodString = "count$";
    public static final String attributeIsInitializedMethodNamePrefix = "isInitialized$";
    public static final String mixinSuffix = "$Mixin";
    public static final String deprecatedInterfaceSuffix = "$Intf";
    public static final String scriptClassSuffix = "$Script";
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
    public static final String varFlagsString = "VFLGS$";
    public static final String varDependentsManagerString = "DependentsManager$";
    public static final String varValueString = "$";
    public static final String varFlagActionTest = "is";
    public static final String varFlagActionSet = "set";
    public static final String varFlagActionClear = "clear";
    public static final String varFlagInitialized = "Initialized$";
    public static final String varFlagDefaultsApplied = "DefaultsApplied$";
    public static final String varFlagValid = "ValidValue$";
    public static final String varFlagHasDependents = "Bindee$";
    public static final String varMapString = "MAP$";
    public static final String varGetMapString = "GETMAP$";

    public  static final String javaLangPackageNameString = "java.lang";
    public  static final String runtimePackageNameString = "com.sun.javafx.runtime";
    public  static final String annotationPackageNameString = "com.sun.javafx.runtime.annotation";
    public  static final String functionsPackageNameString = "com.sun.javafx.functions";
    public  static final String sequencePackageNameString = "com.sun.javafx.runtime.sequence";

    public  static final String cSequences = sequencePackageNameString + ".Sequences";
    public  static final String cSequence  = sequencePackageNameString + ".Sequence";
    public  static final String arraySequence  = sequencePackageNameString + ".ArraySequence";
    private static final String cUtil = runtimePackageNameString + ".Util";
    private static final String cFXBase = runtimePackageNameString + ".FXBase";

    public  static final Pattern DATETIME_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT]");

    public  static final char typeCharToEscape = '.';
    public  static final char escapeTypeChar = '_';

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

    final RuntimeMethod Util_isEqual;

    final RuntimeMethod FXBase_switchDependence;

    /**
     * Name definitions
     */
    public final Name fxObjectName;
    public final Name fxMixinName;
    public final Name fxBaseName;
    public final Name mixinSuffixName;
    public final Name deprecatedInterfaceSuffixName;
    final Name scriptLevelAccessField;
    final Name scriptLevelAccessMethod;
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
    final Name attributeNotifyDependentsName;
    final Name completeName;
    final Name outerAccessorName;
    final Name getMethodName;
    final Name attributeSetMethodParamName;
    final Name getSliceMethodName;
    final Name replaceSliceMethodName;
    final Name setMethodName;
    final Name sizeMethodName;
    final Name defaultingTypeInfoFieldName;
    final Name needDefaultsMethodName;
    final Name makeAttributeMethodName;
    final Name makeMethodName;
    final Name invokeName;
    final Name lambdaName;
    final Name lengthName;
    final Name emptySequenceFieldString;
    final Name isInitializedName;
    final Name scriptBindingClassName;
    final Name bindingIdName;
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
    final Name scriptClassSuffixName;
    final Name timeName;
    final Name valueOfName;
    final Name valuesName;
    final Name valueName;
    final Name targetName;
    final Name interpolateName;
    final Name initFXBaseName;
    final Name userInitName;
    final Name postInitName;
    final Name attributeGetPrefixName;
    final Name attributeSetPrefixName;
    final Name attributeBePrefixName;
    final Name attributeInvalidatePrefixMethodName;
    final Name attributeOnReplacePrefixMethodName;
    final Name attributeEvaluatePrefixMethodName;
    final Name attributeApplyDefaultsPrefixMethodName;
    final Name attributeCountMethodName;
    final Name isInitializedPrefixName;
    final Name incrementSharingMethodName;
    final Name onReplaceArgNameOld;
    final Name onReplaceArgNameNew;
    final Name onReplaceArgNameBuffer;
    final Name onReplaceArgNameFirstIndex;
    final Name onReplaceArgNameLastIndex;
    final Name onReplaceArgNameNewElements;

	public final Name runtimePackageName;
	public final Name annotationPackageName;
	public final Name sequencePackageName;
	public final Name functionsPackageName;
	public final Name javaLangPackageName;
    public final Name implFunctionSuffixName;

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
        attributeNotifyDependentsName = names.fromString(attributeNotifyDependentsNameString);
        completeName = names.fromString(completeNameString);
        outerAccessorName = names.fromString("accessOuter$");
        getMethodName = names.fromString(getMethodNameString);
        attributeSetMethodParamName = names.fromString("value$");
        getSliceMethodName = names.fromString("getSlice");
        replaceSliceMethodName = names.fromString("replaceSlice");
        setMethodName = names.fromString(setMethodNameString);
        sizeMethodName = names.fromString(sizeMethodNameString);
        defaultingTypeInfoFieldName = names.fromString("$TYPE_INFO");
        needDefaultsMethodName = names.fromString(needDefaultMethodNameString);
        makeAttributeMethodName = names.fromString(makeAttributeMethodNameString);
        makeMethodName = names.fromString(makeMethodNameString);
        invokeName = names.fromString(invokeNameString);
        lambdaName = names.fromString(lambdaNameString);
        lengthName = names.fromString("length");
        emptySequenceFieldString = names.fromString("emptySequence");
        isInitializedName = names.fromString(isInitializedNameString);
        scriptBindingClassName = names.fromString(scriptBindingListenerClassString);
        bindingIdName = names.fromString(bindingIdString);
        varOffsetName = names.fromString(varOffsetString);
        varCountName = names.fromString(varCountString);
        scriptClassSuffixName = names.fromString(scriptClassSuffix);
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
        valueOfName = names.fromString("valueOf");
        valuesName = names.fromString("values");
        targetName = names.fromString("target");
        valueName = names.fromString("value");
        interpolateName = names.fromString("interpolate");
        initFXBaseName = names.fromString("initFXBase$");
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
        attributeBePrefixName = names.fromString(attributeBeMethodNamePrefix);
        attributeInvalidatePrefixMethodName = names.fromString(attributeInvalidateMethodNamePrefix);
        attributeOnReplacePrefixMethodName = names.fromString(attributeOnReplaceMethodNamePrefix);
        attributeEvaluatePrefixMethodName = names.fromString(attributeEvaluateMethodNamePrefix);
        attributeApplyDefaultsPrefixMethodName = names.fromString(attributeApplyDefaultsMethodNamePrefix);
        attributeCountMethodName = names.fromString(attributeCountMethodString);
        isInitializedPrefixName = names.fromString(attributeIsInitializedMethodNamePrefix);
        scriptLevelAccessField = names.fromString("$scriptLevel$");
        scriptLevelAccessMethod = names.fromString("access$scriptLevel$");

        runtimePackageName = names.fromString(runtimePackageNameString);
        annotationPackageName = names.fromString(annotationPackageNameString);
        javaLangPackageName = names.fromString(javaLangPackageNameString);
        sequencePackageName = names.fromString(sequencePackageNameString);
        functionsPackageName = names.fromString(functionsPackageNameString);

        // Initialize RuntimeMethods
        TypeInfo_getTypeInfo = new RuntimeMethod(names, typeInfosString, "getTypeInfo");

        Sequences_convertNumberSequence = new RuntimeMethod(names, cSequences, "convertNumberSequence");
        Sequences_forceNonNull = new RuntimeMethod(names, cSequences, "forceNonNull");
        Sequences_range = new RuntimeMethod(names, cSequences, "range");
        Sequences_rangeExclusive = new RuntimeMethod(names, cSequences, "rangeExclusive");
        Sequences_size = new RuntimeMethod(names, cSequences, "size");

        Util_isEqual = new RuntimeMethod(names, cUtil, "isEqual");

        FXBase_switchDependence = new RuntimeMethod(names, cFXBase, "switchDependence$");

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
