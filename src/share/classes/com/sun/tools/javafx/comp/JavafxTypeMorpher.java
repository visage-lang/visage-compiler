/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.util.*;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.code.JavafxClassSymbol;

import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.locationPackageNameString;
import static com.sun.tools.javafx.comp.JavafxDefs.sequencePackageNameString;
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
    private final JavafxToJava toJava;  //TODO: this dependency should go away
    private final JavafxTypes types;

    public final LocationNameSymType[] bindingNCT;
    public final LocationNameSymType[] locationNCT;
    public final LocationNameSymType[] variableNCT;
    public final LocationNameSymType[] boundComprehensionNCT;
    public final LocationNameSymType[] constantLocationNCT;
    public final LocationNameSymType   baseLocation;

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
            this(locationPackageNameString, which);
        }
        private LocationNameSymType(String pkg, String which) {
            this(Name.fromString(names, pkg + "." + which));
        }
    }

    private Map<Symbol, VarMorphInfo> vmiMap = new HashMap<Symbol, VarMorphInfo>();

    public class VarMorphInfo extends TypeMorphInfo {
        private final Symbol sym;

        VarMorphInfo(Symbol sym) {
            super((sym.kind == Kinds.MTH)? ((MethodType)sym.type).getReturnType() : sym.type);
            this.sym = sym;
        }

        public Symbol getSymbol() {
            return sym;
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
                (realTsym != variableNCT[TYPE_KIND_SEQUENCE].sym) &&
                (realTsym != variableNCT[TYPE_KIND_BOOLEAN].sym) &&
                (realTsym != variableNCT[TYPE_KIND_DOUBLE].sym) &&
                (realTsym != variableNCT[TYPE_KIND_INT].sym) : "Locations should have been converted";
            
            this.realType = symType;

            if (symType.isPrimitive()) {
                if (realTsym == syms.doubleType.tsym //  || realTsym == syms.floatType.tsym
                        ) {
                    typeKind = TYPE_KIND_DOUBLE;
                } else if (realTsym == syms.intType.tsym) {
                    typeKind = TYPE_KIND_INT;
                } else if (realTsym == syms.booleanType.tsym) {
                    typeKind = TYPE_KIND_BOOLEAN;
                } else {
                    //assert false : "should not reach here";
                    this.realType = types.boxedClass(realType).type; //TODO: maybe the real type should be kept separate?
                    elementType = realType;
                    typeKind = TYPE_KIND_OBJECT;
                }
            } else {
                if (isSequence()) {
                    typeKind = TYPE_KIND_SEQUENCE;
                    elementType = toJava.elementType(symType);
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
        public Type getRealBoxedType() { return (realType.isPrimitive())? types.boxedClass(realType).type : realType; }
        public Type getRealFXType() { return (realType.isPrimitive() && typeKind==TYPE_KIND_OBJECT)? types.boxedClass(realType).type : realType; }

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
        toJava = JavafxToJava.instance(context);

        variableNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        locationNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        bindingNCT = new LocationNameSymType[TYPE_KIND_COUNT];  
        boundComprehensionNCT = new LocationNameSymType[TYPE_KIND_COUNT];
        constantLocationNCT = new LocationNameSymType[TYPE_KIND_COUNT];

        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            variableNCT[kind] = new LocationNameSymType(defs.locationVariableName[kind]);
            locationNCT[kind] = new LocationNameSymType(defs.locationInterfaceName[kind]);
            bindingNCT[kind] = new LocationNameSymType(JavafxVarSymbol.getTypePrefix(kind) + "BindingExpression");
            boundComprehensionNCT[kind] = new LocationNameSymType(sequencePackageNameString, JavafxVarSymbol.getTypePrefix(kind) + "BoundComprehension");
            constantLocationNCT[kind] = new LocationNameSymType(JavafxVarSymbol.getTypePrefix(kind) + "Constant");
        }

        baseLocation = new LocationNameSymType("Location");

        defaultValueByKind = new Object[TYPE_KIND_COUNT];
        defaultValueByKind[TYPE_KIND_OBJECT] = null;
        defaultValueByKind[TYPE_KIND_DOUBLE] = 0.0;
        defaultValueByKind[TYPE_KIND_BOOLEAN] = 0;
        defaultValueByKind[TYPE_KIND_INT] = 0;
        defaultValueByKind[TYPE_KIND_SEQUENCE] = null; //TODO: empty sequence
    }

    private boolean computeRequiresLocation(Symbol sym) {
        if (sym.kind == Kinds.VAR) {
            final Symbol owner = sym.owner;
            final long flags = sym.flags();
            final boolean isClassVar = owner.kind == Kinds.TYP;
            final boolean isAssignedTo = (flags & (VARUSE_INIT_ASSIGNED_TO | VARUSE_ASSIGNED_TO)) != 0;

            if (sym.flatName() == names._super || sym.flatName() == names._this) {
                // 'this' and 'super' can't be made into Locations
                return false;
            }
            if (isClassVar && !types.isJFXClass(owner)) {
                //TODO: should be handled by ClassReader setting *NEED_LOCATION* bits
                return false;
            }
            if (isAssignedTo && !isClassVar && (flags & VARUSE_INNER_ACCESS) != 0) {
                // Local variables must be Locations if they are accessed within an inner class and have been assigned to
                return true;
            }
            if ((flags & Flags.PARAMETER) != 0) {
                // Otherwise parameters are never Locations
                return false;
            }
            if( (flags & (VARUSE_BOUND_INIT | VARUSE_HAS_ON_REPLACE | VARUSE_USED_IN_BIND | VARUSE_SELF_REFERENCE)) != 0 ) {
                // vars which are defined by a bind or have an 'on replace' must be Locations
                // vars which reference themselves in their init expression must be Locactions // TODO: maybe not needed for class vars
                // vars which are used in a bind should be Locations (even if never changed) since otherwise they will dynamically be turned to Locations
                return true;
            }
            if (types.isSequence(sym.type)) {
                // for a sequence to be modified it must be a Location
                //TODO: check for sequence variables which are never modifier (no insert, delete, assignment, etc)
                return true;
            }
            if (isClassVar) {  // class or script var
                return true;  //TODO: conditionally elide
            }
        }
        return false;
    }

    public boolean requiresLocation(Symbol sym) {
        if ((sym.flags_field & VARUSE_NEED_LOCATION_DETERMINED) == 0) {
            if (computeRequiresLocation(sym)) {
                sym.flags_field |= VARUSE_NEED_LOCATION;
            }
            sym.flags_field |= VARUSE_NEED_LOCATION_DETERMINED;
        }
        return (sym.flags_field & VARUSE_NEED_LOCATION) != 0;
    }

    Type variableType(int typeKind) {
        return variableNCT[typeKind].type;
    }

    Type locationType(int typeKind) {
        return locationNCT[typeKind].type;
    }

    Type bindingExpressionType(int typeKind) {
        return bindingNCT[typeKind].type;
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
            List<Type> actuals = List.of(elemType);
            Type clazzOuter = variableType(tmi.getTypeKind()).getEnclosingType();

            List<Type> newActuals = List.nil();
            for (Type t : actuals) {
                if ((t.tsym instanceof ClassSymbol) &&
                        (t.tsym.flags_field & COMPOUND_CLASS) != 0) {
                    String str = t.tsym.name.toString().replace("$", ".");
                    ClassSymbol csym = new JavafxClassSymbol(0, names.fromString(str), t.tsym.owner);
                    csym.flags_field |= COMPOUND_CLASS;
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
