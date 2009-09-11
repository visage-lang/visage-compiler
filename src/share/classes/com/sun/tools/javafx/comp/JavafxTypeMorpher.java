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
import com.sun.tools.mjavac.code.Type.ClassType;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.code.JavafxClassSymbol;

import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.locationPackageNameString;
import static com.sun.tools.javafx.code.JavafxFlags.*;


/**
 *
 * @author Robert Field
 */
public class JavafxTypeMorpher {
    protected static final Context.Key<JavafxTypeMorpher> typeMorpherKey =
            new Context.Key<JavafxTypeMorpher>();

    private final JavafxDefs defs;
    private final Name.Table names;
    final JavafxClassReader reader;
    private final JavafxSymtab syms;
    private final JavafxTypes types;

    public final LocationNameSymType[] locationNCT;
    public final LocationNameSymType[] variableNCT;
    public final LocationNameSymType[] constantLocationNCT;
    public final LocationNameSymType   baseLocation;
    public final LocationNameSymType abstractBoundComprehension;

    private final Object[] defaultValueByKind;

    public class LocationNameSymType {
        public final Name name;
        public final ClassSymbol sym;
        public final Type type;
        private LocationNameSymType(Name name) {
            this.name = name;
            sym = reader.jreader.enterClass(name);
            type = sym.type;
        }
        private LocationNameSymType(String which) {
            this(names.fromString(locationPackageNameString + "." + which));
        }
    }

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
        private final Type morphedVariableType;
        private final Type morphedLocationType;
        private int typeKind;
        private Type elementType = null;

        TypeMorphInfo(Type symType) {
            TypeSymbol realTsym = symType.tsym;
            //check if symbol is already a Location, needed for source class
            assert
                (realTsym != variableNCT[TYPE_KIND_OBJECT].sym) &&
                (realTsym != variableNCT[TYPE_KIND_BOOLEAN].sym) &&
                (realTsym != variableNCT[TYPE_KIND_CHAR].sym) &&
                (realTsym != variableNCT[TYPE_KIND_BYTE].sym) &&
                (realTsym != variableNCT[TYPE_KIND_SHORT].sym) &&
                (realTsym != variableNCT[TYPE_KIND_INT].sym) &&
                (realTsym != variableNCT[TYPE_KIND_LONG].sym) &&
                (realTsym != variableNCT[TYPE_KIND_FLOAT].sym) &&
                (realTsym != variableNCT[TYPE_KIND_DOUBLE].sym) &&
                (realTsym != variableNCT[TYPE_KIND_SEQUENCE].sym) : "Locations should have been converted";

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

            // must be called AFTER typeKind and realType are set in vsym
            this.morphedVariableType = symType == syms.voidType ? symType : generifyIfNeeded(variableType(typeKind), this);
            this.morphedLocationType = symType == syms.voidType ? symType : generifyIfNeeded(locationType(typeKind), this);
        }

        protected boolean isSequence() {
            return types.isSequence(realType);
        }

        public Type getRealType() { return realType; }
        public Type getRealBoxedType() { return types.boxedTypeOrType(realType); }
        public Type getRealFXType() { return (realType.isPrimitive() && typeKind==TYPE_KIND_OBJECT)? types.boxedTypeOrType(realType) : realType; }

        public Type getLocationType() { return morphedLocationType; }
        public Type getVariableType() { return morphedVariableType; }
        public Type getConstantLocationType() { return generifyIfNeeded(constantLocationNCT[typeKind].type, this); }
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
        reader = JavafxClassReader.instance(context);

        variableNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        locationNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        constantLocationNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        abstractBoundComprehension = new LocationNameSymType(defs.cAbstractBoundComprehensionName);

        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            variableNCT[kind] = new LocationNameSymType(defs.locationVariableName[kind]);
            locationNCT[kind] = new LocationNameSymType(defs.locationInterfaceName[kind]);
            constantLocationNCT[kind] = new LocationNameSymType(JavafxVarSymbol.getTypePrefix(kind) + "Constant");
        }

        baseLocation = new LocationNameSymType("Location");

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

    Type variableType(int typeKind) {
        return variableNCT[typeKind].type;
    }

    Type locationType(int typeKind) {
        return locationNCT[typeKind].type;
    }

    /** Add type parameters.
     * Returns a bogus hybrid front-end/back-end Type that is only meaningful
     * as an argument to makeTypeTree.
     * FIXME when translation creates attributes trees.
     */
    Type generifyIfNeeded(Type aLocationType, TypeMorphInfo tmi) {
        Type newType;
        Type elemType = tmi.getElementType();
        if ((tmi.getTypeKind() == TYPE_KIND_OBJECT ||
                tmi.getTypeKind() == TYPE_KIND_SEQUENCE) ) {
            if (elemType == null) {
                /* handles library which doesn't have element type */
                elemType = syms.objectType;
            }
            else
                elemType = types.boxedTypeOrType(elemType);
            List<Type> actuals = List.of(elemType);
            Type clazzOuter = variableType(tmi.getTypeKind()).getEnclosingType();

            List<Type> newActuals = List.nil();
            for (Type t : actuals) {
                if ((t.tsym instanceof ClassSymbol) &&
                        (t.tsym.flags_field & MIXIN) != 0) {
                    String str = t.tsym.name.toString().replace("$", ".");
                    ClassSymbol csym = new JavafxClassSymbol(0, names.fromString(str), t.tsym.owner);
                    csym.flags_field |= MIXIN;
                    Type tp = new ClassType(Type.noType, List.<Type>nil(), csym);
                    newActuals = newActuals.append(tp);
                    break;
                }

                newActuals = newActuals.append(t);
            }

            newType = new ClassType(clazzOuter, newActuals, aLocationType.tsym);
        } else {
            newType = aLocationType;
        }
        return newType;
    }
}
