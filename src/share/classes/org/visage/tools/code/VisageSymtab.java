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

package org.visage.tools.code;

import com.sun.tools.mjavac.code.Symtab;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Type.*;
import static com.sun.tools.mjavac.jvm.ByteCodes.*;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.TypeTags;
import org.visage.tools.comp.VisageDefs;
import com.sun.tools.mjavac.code.Flags;

/**
 *
 * @author Robert Field
 */
public class VisageSymtab extends Symtab {

    private static final String anno = VisageDefs.annotation_PackageString;
    public static final String privateAnnotationClassNameString = anno + ".Private";
    public static final String protectedAnnotationClassNameString = anno + ".Protected";
    public static final String packageAnnotationClassNameString = anno + ".Package";
    public static final String publicAnnotationClassNameString = anno + ".Public";
    public static final String scriptPrivateAnnotationClassNameString = anno + ".ScriptPrivate";
    public static final String defaultAnnotationClassNameString = anno + ".Default";
    public static final String publicInitAnnotationClassNameString = anno + ".PublicInitable";
    public static final String publicReadAnnotationClassNameString = anno + ".PublicReadable";
    public static final String bindeesAnnotationClassNameString = anno + ".VisageBindees";
    public static final String signatureAnnotationClassNameString = anno + ".VisageSignature";
    public static final String defAnnotationClassNameString = anno + ".Def";
    public static final String staticAnnotationClassNameString = anno + ".Static";
    public static final String inheritedAnnotationClassNameString = anno + ".Inherited";
    public static final String sourceNameAnnotationClassNameString = anno + ".SourceName";

    // Visage built-in(value) types
    public final Type visage_BooleanType;
    public final Type visage_CharacterType;
    public final Type visage_ByteType;
    public final Type visage_ShortType;
    public final Type visage_IntegerType;
    public final Type visage_LongType;
    public final Type visage_FloatType;
    public final Type visage_DoubleType;
    public final Type visage_NumberType;
    public final Type visage_StringType;
    public final Type visage_DurationType;
    public final Type visage_LengthType;
    public final Type visage_LengthUnitType;
    public final Type visage_AngleType;
    public final Type visage_AngleUnitType;
    public final Type visage_ColorType;

    // Visage other types
    public final Type visage_AnyType;
    public final Type visage_UnspecifiedType;
    public final Type visage_AutoImportRuntimeType;
    public final Type visage_RuntimeType;
    public final Type visage_VoidType;
    public final Type visage_java_lang_VoidType;
    public final Type visage_SequenceType;
    public final Type visage_SequenceRefType;
    public final Type visage_SequenceProxyType;
    public final Type visage_ArraySequenceType;
    public final Type visage_EmptySequenceType;
    public final Type visage_SequenceTypeErasure;
    public final Type visage_ShortArray;
    public final Type visage_ObjectArray;
    static public final int MAX_FIXED_PARAM_LENGTH = 8;
    public final Type[] visage_FunctionTypes = new Type[MAX_FIXED_PARAM_LENGTH+1];
    public final Type visage_ObjectType;
    public final Type visage_MixinType;
    public final Type visage_BaseType;
    public final Type visage_SequencesType;
    public final Type visage_KeyValueType;
    public final Type visage_KeyFrameType;
    public final Type visage_KeyValueTargetType;
    public final Type visage_PointerType;
    public final Type visage_ConstantType;
    public final Type visage_BoundForOverSequenceType;
    public final Type visage_BoundForOverNullableSingletonType;
    public final Type visage_BoundForOverSingletonType;
    public final Type visage_ForPartInterfaceType;
    public final Type visage_NonLocalReturnExceptionType;
    public final Type visage_NonLocalBreakExceptionType;
    public final Type visage_NonLocalContinueExceptionType;

    public final Type visage_protectedAnnotationType;
    public final Type visage_packageAnnotationType;
    public final Type visage_publicAnnotationType;
    public final Type visage_scriptPrivateAnnotationType;
    public final Type visage_defaultAnnotationType;
    public final Type visage_publicInitAnnotationType;
    public final Type visage_publicReadAnnotationType;
    public final Type visage_signatureAnnotationType;
    public final Type visage_defAnnotationType;
    public final Type visage_staticAnnotationType;
    public final Type visage_inheritedAnnotationType;
    public final Type visage_sourceNameAnnotationType;

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

    private VisageTypes types;

    public static final String functionClassPrefix =
            "org.visage.functions.Function";

    public static void preRegister(final Context context) {
        if (context.get(symtabKey) == null)
            context.put(symtabKey, new Context.Factory<Symtab>() {
                public Symtab make() {
                    return new VisageSymtab(context);
                }
            });
    }

    public static void preRegister(final Context context, Symtab syms) {
        context.put(symtabKey, syms);
    }

    /** Creates a new instance of VisageSymtab */
    VisageSymtab(Context context) {
        super(context);

        // FIXME It would be better to make 'names' in super-class be protected.
        Name.Table names = Name.Table.instance(context);
        types = VisageTypes.instance(context);
        Options options = Options.instance(context);
        String numberChoice = options.get("Number");

        // Make the array length var symbol a Visage var symbol
        VisageVarSymbol visageLengthVar = new VisageVarSymbol(
            types,
            names,
            Flags.PUBLIC | Flags.FINAL ,
            names.length,
            intType,
            arrayClass);
        arrayClass.members().remove(lengthVar);
        arrayClass.members().enter(visageLengthVar);

        visage_BooleanType = booleanType;
        visage_CharacterType = charType;
        visage_ByteType = byteType;
        visage_ShortType = shortType;
        visage_IntegerType = intType;
        visage_LongType = longType;
        visage_FloatType = floatType;
        visage_DoubleType = doubleType;
        if (numberChoice == null) {
            //default
            visage_NumberType = floatType;
        } else if (numberChoice.equals("Float")) {
            visage_NumberType = floatType;
        } else if (numberChoice.equals("Double")) {
            visage_NumberType = doubleType;
        } else {
            throw new IllegalArgumentException("Bad argument for Number, must be Float pr Double");
        }
        visage_StringType = stringType;
        visage_DurationType = enterClass("visage.lang.Duration");
        visage_LengthType = enterClass("visage.lang.Length");
        visage_LengthUnitType = enterClass("visage.lang.LengthUnit");
        visage_AngleType = enterClass("visage.lang.Angle");
        visage_AngleUnitType = enterClass("visage.lang.AngleUnit");
        visage_ColorType = enterClass("visage.lang.Color");

        visage_AnyType = objectType;
        visage_UnspecifiedType = unknownType;
        visage_VoidType = voidType;

        visage_AutoImportRuntimeType = enterClass("visage.lang.Builtins");
        visage_RuntimeType = enterClass("visage.lang.Visage");
        unreachableType = new Type(TypeTags.VOID, null);
        unreachableType.tsym = new TypeSymbol(0, names.fromString("<unreachable>"), Type.noType, rootPackage);
        visage_java_lang_VoidType = types.boxedClass(voidType).type;
        visage_SequenceType = enterClass(VisageDefs.cSequence);
        visage_SequenceRefType = enterClass(VisageDefs.cSequenceRef);
        visage_SequenceProxyType = enterClass(VisageDefs.cSequenceProxy);
        visage_ArraySequenceType = enterClass(VisageDefs.cArraySequence);
        visage_SequencesType = enterClass(VisageDefs.cSequences);
        visage_EmptySequenceType = types.sequenceType(botType);
        visage_SequenceTypeErasure = types.erasure(visage_SequenceType);
        visage_ShortArray = new ArrayType(shortType, arrayClass);
        visage_ObjectArray = new ArrayType(objectType, arrayClass);
        visage_KeyValueType = enterClass("visage.animation.KeyValue");
        visage_KeyFrameType = enterClass("visage.animation.KeyFrame");
        visage_KeyValueTargetType = enterClass("visage.animation.KeyValueTarget");
        visage_PointerType = enterClass("org.visage.runtime.Pointer");
        visage_ConstantType = enterClass("org.visage.runtime.VisageConstant");
        visage_BoundForOverSequenceType = enterClass(VisageDefs.cBoundForOverSequence);
        visage_BoundForOverNullableSingletonType = enterClass(VisageDefs.cBoundForOverNullableSingleton);
        visage_BoundForOverSingletonType = enterClass(VisageDefs.cBoundForOverSingleton);
        visage_ForPartInterfaceType = enterClass(VisageDefs.cBoundForPartI);
        visage_NonLocalReturnExceptionType = enterClass(VisageDefs.cNonLocalReturnException);
        visage_NonLocalBreakExceptionType = enterClass(VisageDefs.cNonLocalBreakException);
        visage_NonLocalContinueExceptionType = enterClass(VisageDefs.cNonLocalContinueException);
        visage_protectedAnnotationType = enterClass(protectedAnnotationClassNameString);
        visage_packageAnnotationType = enterClass(packageAnnotationClassNameString);
        visage_publicAnnotationType = enterClass(publicAnnotationClassNameString);
        visage_scriptPrivateAnnotationType = enterClass(scriptPrivateAnnotationClassNameString);
        visage_defaultAnnotationType = enterClass(defaultAnnotationClassNameString);
        visage_publicInitAnnotationType = enterClass(publicInitAnnotationClassNameString);
        visage_publicReadAnnotationType = enterClass(publicReadAnnotationClassNameString);
        visage_signatureAnnotationType = enterClass(signatureAnnotationClassNameString);
        visage_defAnnotationType = enterClass(defAnnotationClassNameString);
        visage_staticAnnotationType = enterClass(staticAnnotationClassNameString);
        visage_inheritedAnnotationType = enterClass(inheritedAnnotationClassNameString);
        visage_sourceNameAnnotationType = enterClass(sourceNameAnnotationClassNameString);
        for (int i = MAX_FIXED_PARAM_LENGTH; i >= 0;  i--) {
            visage_FunctionTypes[i] = enterClass(functionClassPrefix+i);
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

        runMethodName = names.fromString(VisageDefs.internalRunFunctionString);

        visage_ObjectType = enterClass(VisageDefs.cObject);
        visage_MixinType = enterClass(VisageDefs.cMixin);
        visage_BaseType = enterClass(VisageDefs.cBase);
        
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

        // Enter Visage operators.
        enterUnop("sizeof", visage_SequenceType, visage_IntegerType, 0);

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
                if (a.tsym.name == visage_java_lang_VoidType.tsym.name) {
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
        Type funtype = visage_FunctionTypes[nargs];
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
