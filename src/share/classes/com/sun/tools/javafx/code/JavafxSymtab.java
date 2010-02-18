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

package com.sun.tools.javafx.code;

import com.sun.tools.mjavac.code.Symtab;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Type.*;
import static com.sun.tools.mjavac.jvm.ByteCodes.*;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.javafx.comp.JavafxDefs;
import com.sun.tools.mjavac.code.Flags;

/**
 *
 * @author Robert Field
 */
public class JavafxSymtab extends Symtab {

    private static final String anno = JavafxDefs.annotation_PackageString;
    public static final String privateAnnotationClassNameString = anno + ".Private";
    public static final String protectedAnnotationClassNameString = anno + ".Protected";
    public static final String packageAnnotationClassNameString = anno + ".Package";
    public static final String publicAnnotationClassNameString = anno + ".Public";
    public static final String scriptPrivateAnnotationClassNameString = anno + ".ScriptPrivate";
    public static final String publicInitAnnotationClassNameString = anno + ".PublicInitable";
    public static final String publicReadAnnotationClassNameString = anno + ".PublicReadable";
    public static final String bindeesAnnotationClassNameString = anno + ".JavafxBindees";
    public static final String signatureAnnotationClassNameString = anno + ".JavafxSignature";
    public static final String defAnnotationClassNameString = anno + ".Def";
    public static final String staticAnnotationClassNameString = anno + ".Static";
    public static final String inheritedAnnotationClassNameString = anno + ".Inherited";
    public static final String sourceNameAnnotationClassNameString = anno + ".SourceName";

    // Javafx built-in(value) types
    public final Type javafx_BooleanType;
    public final Type javafx_CharacterType;
    public final Type javafx_ByteType;
    public final Type javafx_ShortType;
    public final Type javafx_IntegerType;
    public final Type javafx_LongType;
    public final Type javafx_FloatType;
    public final Type javafx_DoubleType;
    public final Type javafx_NumberType;
    public final Type javafx_StringType;
    public final Type javafx_DurationType;

    // Javafx other types
    public final Type javafx_AnyType;
    public final Type javafx_UnspecifiedType;
    public final Type javafx_AutoImportRuntimeType;
    public final Type javafx_FXRuntimeType;
    public final Type javafx_VoidType;
    public final Type javafx_java_lang_VoidType;
    public final Type javafx_SequenceType;
    public final Type javafx_SequenceRefType;
    public final Type javafx_SequenceProxyType;
    public final Type javafx_ArraySequenceType;
    public final Type javafx_EmptySequenceType;
    public final Type javafx_SequenceTypeErasure;
    public final Type javafx_ShortArray;
    public final Type javafx_ObjectArray;
    static public final int MAX_FIXED_PARAM_LENGTH = 8;
    public final Type[] javafx_FunctionTypes = new Type[MAX_FIXED_PARAM_LENGTH+1];
    public final Type javafx_FXObjectType;
    public final Type javafx_FXMixinType;
    public final Type javafx_FXBaseType;
    public final Type javafx_SequencesType;
    public final Type javafx_KeyValueType;
    public final Type javafx_KeyFrameType;
    public final Type javafx_KeyValueTargetType;
    public final Type javafx_PointerType;
    public final Type javafx_FXConstantType;
    public final Type javafx_BoundForOverSequenceType;
    public final Type javafx_BoundForOverNullableSingletonType;
    public final Type javafx_BoundForOverSingletonType;
    public final Type javafx_FXForPartInterfaceType;
    public final Type javafx_NonLocalReturnExceptionType;
    public final Type javafx_NonLocalBreakExceptionType;
    public final Type javafx_NonLocalContinueExceptionType;

    public final Type javafx_protectedAnnotationType;
    public final Type javafx_packageAnnotationType;
    public final Type javafx_publicAnnotationType;
    public final Type javafx_scriptPrivateAnnotationType;
    public final Type javafx_publicInitAnnotationType;
    public final Type javafx_publicReadAnnotationType;
    public final Type javafx_signatureAnnotationType;
    public final Type javafx_defAnnotationType;
    public final Type javafx_staticAnnotationType;
    public final Type javafx_inheritedAnnotationType;
    public final Type javafx_sourceNameAnnotationType;

    public final Name booleanTypeName;
    public final Name charTypeName;
    public final Name byteTypeName;
    public final Name shortTypeName;
    public final Name integerTypeName;
    public final Name longTypeName;
    public final Name floatTypeName;
    public final Name doubleTypeName;
    public final Name numberTypeName;
    public final Name stringTypeName;
    public final Name voidTypeName;

    public final Name runMethodName;

    /** The type of expressions that never returns a value to its parent.
     * E.g. an expression that always throws an Exception.
     * Likewise, a "return expression" returns from the outer function,
     * which makes any following/surrounding code unreachable.
     */
    public final Type unreachableType;

    private JavafxTypes types;

    public static final String functionClassPrefix =
            "com.sun.javafx.functions.Function";

    public static void preRegister(final Context context) {
        if (context.get(symtabKey) == null)
            context.put(symtabKey, new Context.Factory<Symtab>() {
                public Symtab make() {
                    return new JavafxSymtab(context);
                }
            });
    }

    public static void preRegister(final Context context, Symtab syms) {
        context.put(symtabKey, syms);
    }

    /** Creates a new instance of JavafxSymtab */
    JavafxSymtab(Context context) {
        super(context);

        // FIXME It would be better to make 'names' in super-class be protected.
        Name.Table names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        Options options = Options.instance(context);
        String numberChoice = options.get("Number");

        // Make the array length var symbol a JavaFX var symbol
        JavafxVarSymbol fxLengthVar = new JavafxVarSymbol(
            types,
            names,
            Flags.PUBLIC | Flags.FINAL ,
            names.length,
            intType,
            arrayClass);
        arrayClass.members().remove(lengthVar);
        arrayClass.members().enter(fxLengthVar);

        javafx_BooleanType = booleanType;
        javafx_CharacterType = charType;
        javafx_ByteType = byteType;
        javafx_ShortType = shortType;
        javafx_IntegerType = intType;
        javafx_LongType = longType;
        javafx_FloatType = floatType;
        javafx_DoubleType = doubleType;
        if (numberChoice == null) {
            //default
            javafx_NumberType = floatType;
        } else if (numberChoice.equals("Float")) {
            javafx_NumberType = floatType;
        } else if (numberChoice.equals("Double")) {
            javafx_NumberType = doubleType;
        } else {
            throw new IllegalArgumentException("Bad argument for Number, must be Float pr Double");
        }
        javafx_StringType = stringType;
        javafx_DurationType = enterClass("javafx.lang.Duration");

        javafx_AnyType = objectType;
        javafx_UnspecifiedType = unknownType;
        javafx_VoidType = voidType;

        javafx_AutoImportRuntimeType = enterClass("javafx.lang.Builtins");
        javafx_FXRuntimeType = enterClass("javafx.lang.FX");
        unreachableType = new Type(TypeTags.VOID, null);
        unreachableType.tsym = new TypeSymbol(0, names.fromString("<unreachable>"), Type.noType, rootPackage);
        javafx_java_lang_VoidType = types.boxedClass(voidType).type;
        javafx_SequenceType = enterClass(JavafxDefs.cSequence);
        javafx_SequenceRefType = enterClass(JavafxDefs.cSequenceRef);
        javafx_SequenceProxyType = enterClass(JavafxDefs.cSequenceProxy);
        javafx_ArraySequenceType = enterClass(JavafxDefs.cArraySequence);
        javafx_SequencesType = enterClass(JavafxDefs.cSequences);
        javafx_EmptySequenceType = types.sequenceType(botType);
        javafx_SequenceTypeErasure = types.erasure(javafx_SequenceType);
        javafx_ShortArray = new ArrayType(shortType, arrayClass);
        javafx_ObjectArray = new ArrayType(objectType, arrayClass);
        javafx_KeyValueType = enterClass("javafx.animation.KeyValue");
        javafx_KeyFrameType = enterClass("javafx.animation.KeyFrame");
        javafx_KeyValueTargetType = enterClass("javafx.animation.KeyValueTarget");
        javafx_PointerType = enterClass("com.sun.javafx.runtime.Pointer");
        javafx_FXConstantType = enterClass("com.sun.javafx.runtime.FXConstant");
        javafx_BoundForOverSequenceType = enterClass(JavafxDefs.cBoundForOverSequence);
        javafx_BoundForOverNullableSingletonType = enterClass(JavafxDefs.cBoundForOverNullableSingleton);
        javafx_BoundForOverSingletonType = enterClass(JavafxDefs.cBoundForOverSingleton);
        javafx_FXForPartInterfaceType = enterClass(JavafxDefs.cBoundForPartI);
        javafx_NonLocalReturnExceptionType = enterClass(JavafxDefs.cNonLocalReturnException);
        javafx_NonLocalBreakExceptionType = enterClass(JavafxDefs.cNonLocalBreakException);
        javafx_NonLocalContinueExceptionType = enterClass(JavafxDefs.cNonLocalContinueException);
        javafx_protectedAnnotationType = enterClass(protectedAnnotationClassNameString);
        javafx_packageAnnotationType = enterClass(packageAnnotationClassNameString);
        javafx_publicAnnotationType = enterClass(publicAnnotationClassNameString);
        javafx_scriptPrivateAnnotationType = enterClass(scriptPrivateAnnotationClassNameString);
        javafx_publicInitAnnotationType = enterClass(publicInitAnnotationClassNameString);
        javafx_publicReadAnnotationType = enterClass(publicReadAnnotationClassNameString);
        javafx_signatureAnnotationType = enterClass(signatureAnnotationClassNameString);
        javafx_defAnnotationType = enterClass(defAnnotationClassNameString);
        javafx_staticAnnotationType = enterClass(staticAnnotationClassNameString);
        javafx_inheritedAnnotationType = enterClass(inheritedAnnotationClassNameString);
        javafx_sourceNameAnnotationType = enterClass(sourceNameAnnotationClassNameString);
        for (int i = MAX_FIXED_PARAM_LENGTH; i >= 0;  i--) {
            javafx_FunctionTypes[i] = enterClass(functionClassPrefix+i);
        }

        booleanTypeName = names.fromString("Boolean");
        charTypeName = names.fromString("Character");
        byteTypeName = names.fromString("Byte");
        shortTypeName = names.fromString("Short");
        integerTypeName = names.fromString("Integer");
        longTypeName = names.fromString("Long");
        floatTypeName = names.fromString("Float");
        doubleTypeName = names.fromString("Double");
        numberTypeName  = names.fromString("Number");
        stringTypeName = names.fromString("String");
        voidTypeName = names.fromString("Void");

        runMethodName = names.fromString(JavafxDefs.internalRunFunctionString);

        javafx_FXObjectType = enterClass(JavafxDefs.cFXObject);
        javafx_FXMixinType = enterClass(JavafxDefs.cFXMixin);
        javafx_FXBaseType = enterClass(JavafxDefs.cFXBase);
        
        enterOperators();
    }

    @Override
    public void enterOperators() {
        super.enterOperators();

        enterBinop("<>", objectType, objectType, booleanType, if_acmpne);
        enterBinop("<>", booleanType, booleanType, booleanType, if_icmpne);
        enterBinop("<>", doubleType, doubleType, booleanType, dcmpl, ifne);
        enterBinop("<>", floatType, floatType, booleanType, fcmpl, ifne);
        enterBinop("<>", longType, longType, booleanType, lcmp, ifne);
        enterBinop("<>", intType, intType, booleanType, if_icmpne);

        enterBinop("and", booleanType, booleanType, booleanType, bool_and);
        enterBinop("or", booleanType, booleanType, booleanType, bool_or);

        // Enter JavaFX operators.
        enterUnop("sizeof", javafx_SequenceType, javafx_IntegerType, 0);

        //TODO: I think these are ancient garbage, needs verification
        enterUnop("lazy", doubleType, doubleType, 0);
        enterUnop("lazy", intType, intType, 0);
        enterUnop("lazy", booleanType, booleanType, 0);
        enterUnop("lazy", objectType, objectType, 0);

        enterUnop("bind", doubleType, doubleType, 0);
        enterUnop("bind", intType, intType, 0);
        enterUnop("bind", booleanType, booleanType, 0);
        enterUnop("bind", objectType, objectType, 0);
    }

    public boolean isRunMethod(Symbol sym) {
        return sym.name == runMethodName;
    }

    private Type boxedTypeOrType(Type elemType) {
        if (elemType.isPrimitive() || elemType == voidType)
            return types.boxedClass(elemType).type;
        else
            return elemType;
    }

    public FunctionType makeFunctionType(List<Type> typarams) {
        ListBuffer<Type> argtypes = new ListBuffer<Type>();
        Type restype = null;
        for (List<Type> l = typarams; l.nonEmpty();  l = l.tail) {
            Type a = l.head;
            if (a instanceof WildcardType)
                a = ((WildcardType) a).type;
            if (restype == null) {
                if (a.tsym.name == javafx_java_lang_VoidType.tsym.name) {
                    a = voidType;
                }
                restype = a;
            }
            else
                argtypes.append(a);
        }
        MethodType mtype = new MethodType(argtypes.toList(), restype, null, methodClass);
        return makeFunctionType(typarams, mtype);
    }

    public FunctionType makeFunctionType(List<Type> typarams, MethodType mtype) {
        int nargs = typarams.size()-1;
        assert (nargs <= MAX_FIXED_PARAM_LENGTH);
        Type funtype = javafx_FunctionTypes[nargs];
        return new FunctionType(funtype.getEnclosingType(), typarams, funtype.tsym, mtype);
    }

    /** Given a MethodType, create the corresponding FunctionType.
     */
    public FunctionType makeFunctionType(MethodType mtype) {
        Type rtype = mtype.restype;
        ListBuffer<Type> typarams = new ListBuffer<Type>();
        typarams.append(boxedTypeOrType(rtype));
        for (List<Type> l = mtype.argtypes; l.nonEmpty(); l = l.tail) {
            typarams.append(boxedTypeOrType(l.head));
        }
        return makeFunctionType(typarams.toList(), mtype);
    }

    /** Make public. */
    @Override
    public Type enterClass(String name) {
        return super.enterClass(name);
    }
}
