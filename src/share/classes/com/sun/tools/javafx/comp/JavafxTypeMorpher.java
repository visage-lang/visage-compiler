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
import com.sun.tools.javafx.code.JavafxFlags;
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

    private final Name.Table names;
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
            // Don't use accessors for local variables. Without this
            // compiler generates method invoke for local var access!
            return isFXMemberVariable() &&
                    ((sym.flags_field & JavafxFlags.JavafxAllInstanceVarFlags) != JavafxFlags.SCRIPT_PRIVATE ||
                    (sym.flags_field & JavafxFlags.VARUSE_NEED_ACCESSOR) != 0 ||
                    isSequence() ||
                    (sym.owner.flags_field & JavafxFlags.MIXIN) != 0);
        }

        boolean isMemberVariable() {
            return sym.owner.kind == Kinds.TYP && sym.name != names._class;
        }

        boolean isFXMemberVariable() {
            return isMemberVariable() && JavafxTypeMorpher.this.types.isJFXClass(sym.owner);
        }

    }

    public class TypeMorphInfo {
        private final Type realType;
        private final int typeKind;
        private final Type elementType;

        TypeMorphInfo(Type symType) {
            this.realType = symType;
            this.typeKind = types.typeKind(realType);
            this.elementType =
                    symType.isPrimitive() ?
                        null :
                        isSequence() ?
                            types.elementType(symType) :
                            realType;
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

}
