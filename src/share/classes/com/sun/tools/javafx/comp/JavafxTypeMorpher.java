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

import java.util.HashMap;
import java.util.Map;

import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.util.*;

import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import static com.sun.tools.javafx.comp.JavafxDefs.*;


/**
 *
 * @author Robert Field
 */
public class JavafxTypeMorpher {
    protected static final Context.Key<JavafxTypeMorpher> typeMorpherKey =
            new Context.Key<JavafxTypeMorpher>();

    private final JavafxDefs defs;
    private final Name.Table names;
    private final JavafxSymtab syms;
    private final JavafxTypes types;

    private final Object[] defaultValueByKind;

    private Map<Symbol, VarMorphInfo> vmiMap = new HashMap<Symbol, VarMorphInfo>();

    public class VarMorphInfo extends TypeMorphInfo {
        private final Symbol sym;

        VarMorphInfo(Symbol sym) {
            super((sym.kind == Kinds.MTH)? ((MethodType)sym.type).getReturnType() : sym.type);
            this.sym = sym;
        }

        Symbol getSymbol() {
            return sym;
        }

        boolean useAccessors() {
            // FIXME: revisit this. For now, return don't
            // use accessors for local variables. Without this
            // compiler generates method invoke for local var access!
            return !sym.isLocal();
        }

        boolean isMemberVariable() {
            return sym.owner.kind == Kinds.TYP && sym.name != names._class;
        }

        boolean isFXMemberVariable() {
            return isMemberVariable() && JavafxTypeMorpher.this.types.isJFXClass(sym.owner);
        }

    }

    public class TypeMorphInfo {
        private Type realType;
        private int typeKind;
        private Type elementType = null;

        TypeMorphInfo(Type symType) {
            TypeSymbol realTsym = symType.tsym;

            this.realType = symType;

            if (symType.isPrimitive()) {
                typeKind = kindFromPrimitiveType(realTsym);
            } else {
                if (isSequence()) {
                    typeKind = TYPE_KIND_SEQUENCE;
                    elementType = types.elementType(symType);
                } else {
                    typeKind = TYPE_KIND_OBJECT;
                    elementType = realType;
                }
            }
        }

        protected boolean isSequence() {
            return types.isSequence(realType);
        }

        public Type getRealType() { return realType; }
        public Type getRealBoxedType() { return types.boxedTypeOrType(realType); }
        public Type getRealFXType() { return (realType.isPrimitive() && typeKind==TYPE_KIND_OBJECT)? types.boxedTypeOrType(realType) : realType; }

        public Object getDefaultValue() { return defaultValueByKind[typeKind]; }
        public Type getElementType() { return elementType; }

        public int getTypeKind() { return typeKind; }
        }

    VarMorphInfo varMorphInfo(Symbol sym) {
        VarMorphInfo vmi = vmiMap.get(sym);
        if (vmi == null) {
            vmi = new VarMorphInfo(sym);
            vmiMap.put(sym, vmi);
        }
        return vmi;
    }

    TypeMorphInfo typeMorphInfo(Type type) {
        return new TypeMorphInfo(type);
    }

    public static JavafxTypeMorpher instance(Context context) {
        JavafxTypeMorpher instance = context.get(typeMorpherKey);
        if (instance == null)
            instance = new JavafxTypeMorpher(context);
        return instance;
    }

    protected JavafxTypeMorpher(Context context) {
        context.put(typeMorpherKey, this);

        defs = JavafxDefs.instance(context);
        syms = (JavafxSymtab)(JavafxSymtab.instance(context));
        types = JavafxTypes.instance(context);
        names = Name.Table.instance(context);

        defaultValueByKind = new Object[TYPE_KIND_COUNT];
        defaultValueByKind[TYPE_KIND_OBJECT] = null;
        defaultValueByKind[TYPE_KIND_BOOLEAN] = 0;
        defaultValueByKind[TYPE_KIND_CHAR] = 0;
        defaultValueByKind[TYPE_KIND_BYTE] = 0;
        defaultValueByKind[TYPE_KIND_SHORT] = 0;
        defaultValueByKind[TYPE_KIND_INT] = 0;
        defaultValueByKind[TYPE_KIND_LONG] = 0L;
        defaultValueByKind[TYPE_KIND_FLOAT] = (float)0.0;
        defaultValueByKind[TYPE_KIND_DOUBLE] = 0.0;
        defaultValueByKind[TYPE_KIND_SEQUENCE] = null; // Empty sequence done programatically
    }

    public int kindFromPrimitiveType(TypeSymbol tsym) {
        if (tsym == syms.booleanType.tsym) return TYPE_KIND_BOOLEAN;
        if (tsym == syms.charType.tsym) return TYPE_KIND_CHAR;
        if (tsym == syms.byteType.tsym) return TYPE_KIND_BYTE;
        if (tsym == syms.shortType.tsym) return TYPE_KIND_SHORT;
        if (tsym == syms.intType.tsym) return TYPE_KIND_INT;
        if (tsym == syms.longType.tsym) return TYPE_KIND_LONG;
        if (tsym == syms.floatType.tsym) return TYPE_KIND_FLOAT;
        if (tsym == syms.doubleType.tsym) return TYPE_KIND_DOUBLE;
        assert false : "should not reach here";
        return TYPE_KIND_OBJECT;
    }
}
