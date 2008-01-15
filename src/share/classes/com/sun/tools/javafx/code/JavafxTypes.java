/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Type.*;
import java.util.HashMap;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
/**
 *
 * @author bothner
 */
public class JavafxTypes extends Types {
    JavafxSymtab syms;

    private HashMap<ClassSymbol, JFXClassDeclaration> fxClasses;

    public static void preRegister(final Context context) {
        context.put(typesKey, new Context.Factory<Types>() {
            public Types make() {
                return new JavafxTypes(context);
            }
        });
    }

    public static JavafxTypes instance(Context context) {
        JavafxTypes instance = (JavafxTypes) context.get(typesKey);
        if (instance == null)
            instance = new JavafxTypes(context);
        return instance;
    }

    protected JavafxTypes(Context context) {
        super(context);
        syms = (JavafxSymtab) JavafxSymtab.instance(context);
    }

   public boolean isSequence(Type type) {
       return type != Type.noType && type != null 
                && type.tag != TypeTags.ERROR 
                && type.tag != TypeTags.METHOD && type.tag != TypeTags.FORALL
                && erasure(type) == syms.javafx_SequenceTypeErasure;
    }

    public Type sequenceType(Type elemType) {
        if (elemType.isPrimitive())
            elemType = boxedClass(elemType).type;
        elemType = new WildcardType(elemType, BoundKind.EXTENDS, syms.boundClass);
        Type seqtype = syms.javafx_SequenceType;
        List<Type> actuals = List.of(elemType);
        Type clazzOuter = seqtype.getEnclosingType();
        return new ClassType(clazzOuter, actuals, seqtype.tsym);
    }

    public Type elementType(Type seqType) {
        Type elemType = seqType.getTypeArguments().head;
        if (elemType instanceof CapturedType)
            elemType = ((CapturedType) elemType).wildcard;
        if (elemType instanceof WildcardType)
            elemType = ((WildcardType) elemType).type;
        if (elemType == null)
            return syms.errType;
        Type unboxed = unboxedType(elemType);
        if (unboxed.tag != TypeTags.NONE)
            elemType = unboxed;
        return elemType;
    }
 
    public boolean isConvertible (Type t, Type s, Warner warn) {
        if (super.isConvertible(t, s, warn))
            return true;
        if (isSequence(t) && isArray(s))
            return isConvertible(elementType(t), elemtype(s), warn);
        
        // Allow lessening precision conversions.
        if (t == syms.javafx_NumberType) {
            if (s == syms.javafx_IntegerType ||
                    s == syms.intType ||
                    s == syms.floatType ||
                    s == syms.shortType ||
                    s == syms.charType ||
                    s == syms.byteType ||
                    s == syms.longType) {
                return true;
            }
        }
        else if (t == syms.javafx_IntegerType) {
            if (s == syms.javafx_NumberType ||
                    s == syms.intType ||
                    s == syms.floatType ||
                    s == syms.shortType ||
                    s == syms.charType ||
                    s == syms.byteType ||
                    s == syms.longType) {
                return true;
            }
        }

        return false;
    }
    
    public boolean isCompoundClass(Symbol sym) {
        return sym instanceof ClassSymbol &&
                (sym.flags_field & JavafxFlags.COMPOUND_CLASS) != 0;
    }

    public boolean isJFXClass(Symbol sym) {
        if (!(sym instanceof ClassSymbol)) {
            return false;
        }
        
        ClassSymbol cSym = (ClassSymbol)sym;
        if ((cSym.flags_field & Flags.INTERFACE) != 0) {
            for (List<Type> intfs = cSym.getInterfaces(); intfs.nonEmpty(); intfs = intfs.tail) {
                if (intfs.head.tsym.type == syms.javafx_FXObjectType) {
                    return true;
                }
            }
        }
        else {
            if (fxClasses != null) {
                if (fxClasses.containsKey(cSym)) {
                    return true;
                }
                
                for (List<Type> intfs = cSym.getInterfaces(); intfs.nonEmpty(); intfs = intfs.tail) {
                    if (intfs.head.tsym.type == syms.javafx_FXObjectType) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void addFxClass(ClassSymbol csym, JFXClassDeclaration cdecl) {
        if (fxClasses == null) {
            fxClasses = new HashMap<ClassSymbol, JFXClassDeclaration>();
        }
        
        fxClasses.put(csym, cdecl);
    }
    
    public JFXClassDeclaration getFxClass (ClassSymbol csym) {
       return fxClasses.get(csym);
    }

    public void clearCaches() {
        fxClasses = null;
    }
}
