/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.code.Type.*;
import static com.sun.tools.javac.jvm.ByteCodes.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.TypeTags;

/**
 *
 * @author Robert Field
 */
public class JavafxSymtab extends Symtab {
    
    // Javafx types
    public final Type javafx_IntegerType;
    public final Type javafx_NumberType;
    public final Type javafx_AnyType;
    public final Type javafx_UnspecifiedType;
    public final Type javafx_StringType;
    public final Type javafx_BooleanType;
    public final Type javafx_VoidType;
    public final Type javafx_java_lang_VoidType;
    public final Type javafx_SequenceType;
    public final Type javafx_SequenceTypeErasure;
    static public final int MAX_FIXED_PARAM_LENGTH = 8;
    public final Type[] javafx_FunctionTypes = new Type[MAX_FIXED_PARAM_LENGTH+1];
    public final Type javafx_FXObjectType;
    public final Type javafx_SequencesType;
    
    public final Type javafx_privateAnnotationType;
    public final Type javafx_protectedAnnotationType;
    public final Type javafx_publicAnnotationType;
    
    /** The type of expressions that never returns a value to its parent.
     * E.g. an expression that always throws an Exception.
     * Likewise, a "return expression" returns from the outer function,
     * which makes any following/surrounding code unreachable.
     */
    public final Type unreachableType;

    private Types types;

    public static final String functionClassPrefix =
            "com.sun.javafx.functions.Function";

    public static void preRegister(final Context context) {
        context.put(symtabKey, new Context.Factory<Symtab>() {
            public Symtab make() {
                return new JavafxSymtab(context);
            }
        });
    }
    
    /** Creates a new instance of JavafxSymtab */
    JavafxSymtab(Context context) {
        super(context);

        // FIXME It would be better to make 'names' in super-class be protected.
        Name.Table names = Name.Table.instance(context);
        types = Types.instance(context);
        
        javafx_IntegerType = intType;
        javafx_NumberType = doubleType;
        javafx_AnyType = objectType;
        javafx_UnspecifiedType = unknownType;
        javafx_StringType = stringType;
        javafx_BooleanType = booleanType;
        javafx_VoidType = voidType;
        unreachableType = new Type(TypeTags.VOID, null);
        unreachableType.tsym = new TypeSymbol(0, names.fromString("<unreachable>"), Type.noType, rootPackage);
        javafx_java_lang_VoidType = types.boxedClass(voidType).type;
        javafx_SequenceType = enterClass("com.sun.javafx.runtime.sequence.Sequence");
        javafx_SequencesType = enterClass("com.sun.javafx.runtime.sequence.Sequences");
        javafx_SequenceTypeErasure = types.erasure(javafx_SequenceType);
        javafx_privateAnnotationType = enterClass("com.sun.javafx.runtime.Private");
        javafx_protectedAnnotationType = enterClass("com.sun.javafx.runtime.Protected");
        javafx_publicAnnotationType = enterClass("com.sun.javafx.runtime.Public");
        for (int i = MAX_FIXED_PARAM_LENGTH; --i >= 0;  ) {
            javafx_FunctionTypes[i] = enterClass(functionClassPrefix+i);
        }
        
        javafx_FXObjectType = enterClass("com.sun.javafx.runtime.FXObject");
        enterOperators();
    }
    
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
        
        enterUnop("lazy", doubleType, doubleType, 0);
        enterUnop("lazy", intType, intType, 0);
        enterUnop("lazy", booleanType, booleanType, 0);
        enterUnop("lazy", objectType, objectType, 0);

        enterUnop("bind", doubleType, doubleType, 0);
        enterUnop("bind", intType, intType, 0);
        enterUnop("bind", booleanType, booleanType, 0);
        enterUnop("bind", objectType, objectType, 0);
    }

    public Type boxIfNeeded(Type elemType) {
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
        assert nargs <= MAX_FIXED_PARAM_LENGTH
                : "NOT IMPLEMENTED - functions with >"+MAX_FIXED_PARAM_LENGTH+" parameters";
        Type funtype = javafx_FunctionTypes[nargs];
        return new FunctionType(funtype.getEnclosingType(), typarams, funtype.tsym, mtype);
    }

    /** Given a MethodType, create the corresponding FunctionType.
     */
    public FunctionType makeFunctionType(MethodType mtype) {
        Type rtype = mtype.restype;
        ListBuffer<Type> typarams = new ListBuffer<Type>();
        typarams.append(boxIfNeeded(rtype));
        for (List<Type> l = mtype.argtypes; l.nonEmpty(); l = l.tail) {
            typarams.append(boxIfNeeded(l.head));
        }
        return makeFunctionType(typarams.toList(), mtype);
    }
}
