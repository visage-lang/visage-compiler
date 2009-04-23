/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.Type.*;
import java.util.HashMap;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.code.Symbol.*;
import static com.sun.tools.javac.code.Kinds.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.TypeTags.*;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author bothner
 */
public class JavafxTypes extends Types {
    JavafxSymtab syms;

    private HashMap<ClassSymbol, JFXClassDeclaration> fxClasses;

    public static void preRegister(final Context context) {
        if (context.get(typesKey) == null)
            context.put(typesKey, new Context.Factory<Types>() {
                public Types make() {
                    return new JavafxTypes(context);
                }
            });
    }

    public static void preRegister(final Context context, JavafxTypes types) {
        context.put(typesKey, types);
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
        return sequenceType(elemType, true);
    }
     public Type sequenceType(Type elemType, boolean withExtends) {
        elemType = boxedTypeOrType(elemType);
        if (withExtends)
            elemType = new WildcardType(elemType, BoundKind.EXTENDS, syms.boundClass);
        Type seqtype = syms.javafx_SequenceType;
        List<Type> actuals = List.of(elemType);
        Type clazzOuter = seqtype.getEnclosingType();
        return new ClassType(clazzOuter, actuals, seqtype.tsym);
    }

    public Type boxedElementType(Type seqType) {
        Type elemType = seqType.getTypeArguments().head;
        if (elemType instanceof CapturedType)
            elemType = ((CapturedType) elemType).wildcard;
        if (elemType instanceof WildcardType)
            elemType = ((WildcardType) elemType).type;
        if (elemType == null)
            return syms.javafx_AnyType;
        return elemType;
    }

    public Type elementType(Type seqType) {
        Type elemType = boxedElementType(seqType);
        Type unboxed = unboxedType(elemType);
        if (unboxed.tag != TypeTags.NONE)
            elemType = unboxed;
        return elemType;
    }

    public Type unboxedTypeOrType(Type t) {
        Type ubt = unboxedType(t);
        return ubt==Type.noType? t : ubt;
    }

    public Type boxedTypeOrType(Type t) {
        return (t.isPrimitive() || t == syms.voidType)?
                      boxedClass(t).type
                    : t;
    }

    public Type elementTypeOrType(Type t) {
        return isSequence(t) ? elementType(t) : t;
    }

    public void getSupertypes(Symbol clazz, ListBuffer<Type> supertypes,Set<Type> dupSet) {
        if (clazz != null) {
            Type supType = supertype(clazz.type);
            if (supType != null && supType != Type.noType && !dupSet.contains(supType)) {
                supertypes.append(supType);
                dupSet.add(supType);
                getSupertypes(supType.tsym, supertypes,dupSet);
            }

            if (clazz instanceof JavafxClassSymbol) {
                for (Type superType : ((JavafxClassSymbol)clazz).getSuperTypes()) {
                    if (!dupSet.contains(superType)) {
                        supertypes.append(superType);
                        dupSet.add(superType);
                        getSupertypes(superType.tsym, supertypes,dupSet);
                    }
                }
            }
        }
    }

    public List<Type> supertypes(Symbol clazz) {
        return supertypes(clazz, null);
    }

    public List<Type> supertypes(Symbol clazz, Type includeOrNull) {
        ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
        Set<Type> superSet = new HashSet<Type>();
        if (includeOrNull != null) {
            supertypes.append(includeOrNull);
            superSet.add(includeOrNull);
        }

        getSupertypes(clazz, supertypes, superSet);

        return supertypes.toList();
    }

    public boolean isSuperType (Type maybeSuper, ClassSymbol sym) {
        ListBuffer<Type> supertypes = ListBuffer.<Type>lb();
        Set superSet = new HashSet<Type>();
        supertypes.append(sym.type);
        superSet.add(sym.type);
        getSupertypes(sym, supertypes, superSet);
        for (Type t : supertypes) {
            if (isSameType(t,maybeSuper)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSubtype(Type t, Type s, boolean capture) {
        boolean b = super.isSubtype(t, s, capture);
        if (!b && s.isCompound()) {
            for (Type s2 : interfaces(s).prepend(supertype(s))) {
                if (!isSubtype(t, s2, capture))
                    return false;
            }
            return true;
        }
        else
            return b;
    }

    @Override
    public Type asSuper(Type t, Symbol sym) {
        if (isMixin(sym)) {
            JavafxClassSymbol tsym = (JavafxClassSymbol) t.tsym;
            List<Type> supers = tsym.getSuperTypes();
            for (List<Type> l = supers; l.nonEmpty(); l = l.tail) {
                Type x = asSuper(l.head, sym);
                if (x != null)
                    return x;
            }
        }
        return super.asSuper(t, sym);
    }
    
    public Type superType(JFXClassDeclaration cDecl) {
        // JFXC-2868 - Mixins: JavafxTypes.superType is complex and likely wrong.
        if (! (cDecl.type instanceof ClassType))
            return null;
        ClassType cType = (ClassType) cDecl.type;
        Type superType = cType.supertype_field;
        return superType;
    }

    @Override
    public boolean isConvertible (Type t, Type s, Warner warn) {
        if (super.isConvertible(t, s, warn))
            return true;
        if (isSequence(t) && isArray(s))
            return isConvertible(elementType(t), elemtype(s), warn);
        if (isArray(t) && isSequence(s))
            return isConvertible(elemtype(t), elementType(s), warn);
        if (isSequence(t) && isSequence(s))
            return isConvertible(elementType(t), elementType(s), warn);
        //sequence promotion conversion
        if (isSequence(s) && !isSequence(t)) {
            return isConvertible(sequenceType(t), s, warn);
        }
        // Allow all numeric conversion, for now (some should warn)
        if (isNumeric(t) && isNumeric(s)) {
            return true;
        }
        if (t == syms.intType && s == syms.charType)
            return true;
        return false;
    }

    @Override
    public boolean isCastable(Type t, Type s, Warner warn) {
        //if source is a sequence and target is neither a sequence nor Object return false
        if (isSequence(t) &&
                !(isSequence(s) || s.tag == TypeTags.ARRAY) &&
                s != syms.objectType &&
                s != syms.botType) {
            return false;
        }

        //cannot cast from null to a value type (non-null by default) and vice-versa
        if ((s == syms.botType && t.isPrimitive()) ||
                (t == syms.botType && s.isPrimitive())) {
            return false;
        }

        Type target = isSequence(s) ? elementType(s) : s.tag == TypeTags.ARRAY ? ((ArrayType) s).elemtype : s;
        Type source = isSequence(t) ? elementType(t) : t.tag == TypeTags.ARRAY ? ((ArrayType) t).elemtype : t;
        if (!source.isPrimitive())
            target = boxedTypeOrType(target);
        if (!target.isPrimitive())
            source = boxedTypeOrType(source);

        if (source == syms.botType ||
            target == syms.botType)
            return true;

        boolean isSourceFinal = (source.tsym.flags() & FINAL) != 0;
        boolean isTargetFinal = (target.tsym.flags() & FINAL) != 0;
        if (isJFXClass(source.tsym) && isJFXClass(target.tsym))
            return true;
        else if (isJFXClass(source.tsym) &&
            !isTargetFinal || 
            target.isInterface())
            return true;
        else if (isJFXClass(target.tsym) &&
            !isSourceFinal ||
            target.isInterface())
            return true;
        else //conversion between two primitives/Java classes
            return super.isCastable(source, target, warn);
    }
    
    public boolean isMixin(Symbol sym) {
        if (! (sym instanceof JavafxClassSymbol))
            return false;
        sym.complete();
        return (sym.flags_field & JavafxFlags.MIXIN) != 0;
    }

    public boolean isJFXClass(Symbol sym) {
        if (! (sym instanceof JavafxClassSymbol))
            return false;
        sym.complete();
        return (sym.flags_field & JavafxFlags.FX_CLASS) != 0;
    }
    
    public void addFxClass(ClassSymbol csym, JFXClassDeclaration cdecl) {
        if (fxClasses == null) {
            fxClasses = new HashMap<ClassSymbol, JFXClassDeclaration>();
        }
        csym.flags_field |= JavafxFlags.FX_CLASS;
        fxClasses.put(csym, cdecl);
    }
    
    public JFXClassDeclaration getFxClass (ClassSymbol csym) {
       return fxClasses.get(csym);
    }
    
    /** The implementation of this (abstract) symbol in class origin;
     *  null if none exists. Synthetic methods are not considered
     *  as possible implementations.
     *  Based on the Javac implementation method in MethodSymbol,
     *  but modified to handle multiple inheritance.
     */
    public MethodSymbol implementation(MethodSymbol msym, TypeSymbol origin, boolean checkResult) {
        msym.complete();
        if (origin instanceof JavafxClassSymbol) {
            JavafxClassSymbol c = (JavafxClassSymbol) origin;
            for (Scope.Entry e = c.members().lookup(msym.name);
                     e.scope != null;
                     e = e.next()) {
                if (e.sym.kind == MTH) {
                        MethodSymbol m = (MethodSymbol) e.sym;
                        m.complete();
                        if (m.overrides(msym, origin, this, checkResult) &&
                            (m.flags() & SYNTHETIC) == 0)
                            return m;
                }
            }
            List<Type> supers = c.getSuperTypes();
            for (List<Type> l = supers; l.nonEmpty(); l = l.tail) {
                MethodSymbol m = implementation(msym, l.head.tsym, checkResult);
                if (m != null)
                    return m;
            }
            return null;
        }
        else
            return msym.implementation(origin, this, checkResult);
    }

    /** A replacement for MethodSymbol.overrides. */
    public boolean overrides(Symbol sym, Symbol _other, TypeSymbol origin, boolean checkResult) {
        if (sym.isConstructor() || _other.kind != MTH) return false;

        if (sym == _other) return true;
        MethodSymbol other = (MethodSymbol)_other;

        // assert types.asSuper(origin.type, other.owner) != null;
        Type mt = this.memberType(origin.type, sym);
        Type ot = this.memberType(origin.type, other);
        return
            this.isSubSignature(mt, ot) &&
            (!checkResult || this.resultSubtype(mt, ot, Warner.noWarnings));
    }

    public void clearCaches() {
        fxClasses = null;
    }

    public boolean isNumeric(Type type) {
        return (isSameType(type, syms.javafx_ByteType) ||
                isSameType(type, syms.javafx_ShortType) ||
                isSameType(type, syms.javafx_IntegerType) ||
                isSameType(type, syms.javafx_LongType) ||
                isSameType(type, syms.javafx_FloatType) ||
                isSameType(type, syms.javafx_DoubleType));
    }

    public List<String> toJavaFXString(List<Type> ts) {
        List<String> buf = List.nil();
        for (Type t : ts) {
            buf = buf.prepend(toJavaFXString(t));
        }
        return buf.reverse();
    }

    public String toJavaFXString(Type type) {
        StringBuilder buffer = new StringBuilder();
        typePrinter.visit(type, buffer);
        return buffer.toString();
    }

    SimpleVisitor typePrinter = new SimpleVisitor<Void, StringBuilder>() {

        public Void visitType(Type t, StringBuilder buffer) {
            String s = null;
            switch (t.tag) {
                case NONE: s = "<unknown>"; break;
                case UNKNOWN: s = "Object"; break;
                case BYTE: s = "Byte"; break;
                case SHORT: s = "Short"; break;
                case INT: s = "Integer"; break;
                case LONG: s = "Long"; break;
                case FLOAT: s = "Number"; break;
                case DOUBLE: s = "Double"; break;
                case CHAR: s = "Character"; break;
                case BOOLEAN: s = "Boolean"; break;
                default: s = t.toString(); break;
            }
            buffer.append(s);
            return null;
        }

        @Override
        public Void visitMethodType(MethodType t, StringBuilder buffer) {
            if (t.getReturnType() == null) {
                buffer.append("function(?):?");
                return null;
            }
            buffer.append("function(");
            List<Type> args = t.getParameterTypes();
            for (List<Type> l = args; l.nonEmpty(); l = l.tail) {
                if (l != args) {
                    buffer.append(",");
                }
                buffer.append(":");
                visit(l.head, buffer);
            }
            buffer.append("):");
            visit(t.getReturnType(), buffer);
            return null;
        }

        @Override
        public Void visitArrayType(ArrayType t, StringBuilder buffer) {
            buffer.append("nativearray of ");
            visit(elemtype(t), buffer);
            return null;
        }

        @Override
        public Void visitClassType(ClassType t, StringBuilder buffer) {
            if (isSameType(t, syms.stringType))
                buffer.append("String");
            else if (isSameType(t, syms.objectType))
                buffer.append("Object");
            else if (isSequence(t)) {
                if (t != syms.javafx_EmptySequenceType) {
                    visit(elementType(t), buffer);
                }
                buffer.append("[]");
            }
            else if (t instanceof FunctionType) {
                visitMethodType(t.asMethodType(), buffer);
            }
            else if (t.isCompound()) {
                visit(supertype(t), buffer);
            }
            else
                buffer.append(t.toString());
            return null;
        }
    };

    public String toJavaFXString(MethodSymbol sym, List<VarSymbol> params) {
        StringBuilder buffer = new StringBuilder();
        if ((sym.flags() & BLOCK) != 0)
            buffer.append(sym.owner.name);
        else {
            buffer.append(sym.name == sym.name.table.init ? sym.owner.name : sym.name);
            if (sym.type != null) {
                buffer.append('(');
                // FUTURE: check (flags() & VARARGS) != 0
                List<Type> args = sym.type.getParameterTypes();
                for (List<Type> l = args; l.nonEmpty(); l = l.tail) {
                    if (l != args)
                        buffer.append(",");
                    if (params != null && params.nonEmpty()) {
                        VarSymbol param = params.head;
                        if (param != null)
                            buffer.append(param.name);
                        params = params.tail;
                    }
                    buffer.append(":");
                    buffer.append(toJavaFXString(l.head));
                }
                buffer.append(')');
            }
        }
        return buffer.toString();
    }

    public String location (Symbol sym, Type site) {
        while ((sym.owner.flags() & BLOCK) != 0 ||
                syms.isRunMethod(sym.owner))
            sym = sym.owner;
        return sym.location(site, this);
    }

    public String location (Symbol sym) {
        while ((sym.owner.flags() & BLOCK) != 0 ||
                syms.isRunMethod(sym.owner))
            sym = sym.owner;
        return sym.location();
    }
}
