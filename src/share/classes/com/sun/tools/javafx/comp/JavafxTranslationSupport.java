/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.io.OutputStreamWriter;

import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.BoundKind;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.Type.*;
import com.sun.tools.mjavac.code.TypeTags;
import static com.sun.tools.mjavac.code.Flags.STATIC;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.JCAnnotation;
import com.sun.tools.mjavac.tree.JCTree.JCClassDecl;
import com.sun.tools.mjavac.tree.JCTree.JCExpression;
import com.sun.tools.mjavac.tree.JCTree.JCIdent;
import com.sun.tools.mjavac.tree.JCTree.JCMethodDecl;
import com.sun.tools.mjavac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.mjavac.tree.JCTree.JCModifiers;
import com.sun.tools.mjavac.tree.JCTree.JCStatement;
import com.sun.tools.mjavac.tree.JCTree.JCTypeParameter;
import com.sun.tools.mjavac.tree.JCTree.JCVariableDecl;
import com.sun.tools.mjavac.tree.Pretty;
import com.sun.tools.mjavac.tree.TreeMaker;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.Log;
import com.sun.tools.mjavac.util.Position;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.tree.JCTree.JCBlock;
import com.sun.tools.mjavac.tree.JCTree.JCCatch;
import com.sun.tools.mjavac.tree.TreeInfo;
import com.sun.tools.mjavac.util.Options;
import java.util.Set;
import java.util.HashSet;

/**
 * Common support routines needed for translation
 *
 * @author Robert Field
 * @author Jim Laskey
 */
public abstract class JavafxTranslationSupport {
    protected final JavafxDefs defs;
    protected final Log log;
    protected final JavafxTreeMaker fxmake;
    protected final TreeMaker make; // translation should yield a Java AST, use fxmake when building FX trees
    protected final Name.Table names;
    protected final JavafxResolve rs;
    protected final JavafxSymtab syms;
    protected final JavafxTypes types;
    protected final JavafxTypeMorpher typeMorpher;
    protected final Options options;

    /*
     * other instance information
     */
    private int syntheticNameCounter = 0;

    protected JavafxTranslationSupport(Context context) {
        make = TreeMaker.instance(context);
        fxmake = JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
        options = Options.instance(context);

        syntheticNameCounter = 0;
    }

    public static class NotYetImplementedException extends RuntimeException {
        NotYetImplementedException(String msg) {
            super(msg);
        }
    }

    static JCExpression TODO(String msg) {
        throw new NotYetImplementedException("Not yet implemented: " + msg);
    }

    protected Symbol expressionSymbol(JFXExpression tree) {
        if (tree == null) {
            return null;
        }
        switch (tree.getFXTag()) {
            case IDENT:
                return ((JFXIdent) tree).sym;
            case SELECT:
                return ((JFXSelect) tree).sym;
            case TYPECAST:
                return expressionSymbol(((JFXTypeCast)tree).getExpression());
            default:
                return null;
        }
    }

    protected boolean isValueFromJava(final JFXExpression expr) {
        // The value could come from Java if it is a variable, or a function result.
        Symbol sym = expressionSymbol(expr);
        if (sym != null && !types.isJFXClass(sym.owner)) {
            return true;
        }

        // test for function
        if (expr.getFXTag() == JavafxTag.APPLY) {
            JFXExpression func = ((JFXFunctionInvocation)expr).getMethodSelect();
            if (isValueFromJava(func)) {
                return true;
            }
        }
        return false;
    }

    boolean isImmutable(Symbol sym, Name name) {
        Symbol owner = sym.owner;
        boolean isLocal = owner.kind != Kinds.TYP;
        boolean isKnown = isLocal || (owner instanceof ClassSymbol && types.getFxClass((ClassSymbol) owner) != null); //TODO: consider Java supers
        long flags = sym.flags();
        boolean isWritable = (flags & (Flags.PUBLIC | Flags.PROTECTED | JavafxFlags.PACKAGE_ACCESS)) != 0L;
        boolean isAssignedTo = (flags & JavafxFlags.VARUSE_ASSIGNED_TO) != 0;
        boolean isDef = (flags & JavafxFlags.IS_DEF) != 0;
        boolean isDefinedBound = (flags & JavafxFlags.VARUSE_BOUND_INIT) != 0;
        return
                    name == names._this ||
                    name == names._super ||
                    isKnown && isDef && !isDefinedBound || // unbound def
                    isKnown && !isWritable && !isAssignedTo && !isDefinedBound; // never reassigned
     }

    boolean isImmutable(JFXIdent tree) {
        return isImmutable(tree.sym, tree.getName());
    }

    boolean isImmutable(final JFXSelect tree) {
        return isImmutableSelector(tree) && isImmutable(tree.sym, tree.getIdentifier());
    }

    boolean isImmutableSelector(final JFXSelect tree) {
        if (tree.sym.isStatic()) {
            // If the symbol is static, no matter what the selector is, the selectot is immutable
            return true;
        }
        final JFXExpression selector = tree.getExpression();
        if (selector instanceof JFXIdent) {
            return isImmutable((JFXIdent) selector);
        } else if (selector instanceof JFXSelect) {
            return isImmutable((JFXSelect) selector);
        } else {
            return false;
        }
    }

    boolean hasDependencies(JFXExpression expr, final Set<Symbol> exclusions) {
        class DependencyScanner extends JavafxTreeScanner {

            boolean hasDependencies = false;

            private void markHasDependencies() {
                hasDependencies = true;
            }

            @Override
            public void visitFunctionInvocation(JFXFunctionInvocation tree) {
                Symbol sym = JavafxTreeInfo.symbol(tree.getMethodSelect());
                if (sym == null || (sym.flags() & JavafxFlags.BOUND) != 0L) {
                    markHasDependencies();
                } else {
                    super.visitFunctionInvocation(tree);
                }
            }

            @Override
            public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
                if (!tree.isBound()) {
                    super.visitObjectLiteralPart(tree);
                }
            }

            @Override
            public void visitIdent(JFXIdent tree) {
                if (!exclusions.contains(tree.sym) && !isImmutable(tree)) {
                    markHasDependencies();
                }
            }

            @Override
            public void visitSelect(JFXSelect tree) {
                if (!isImmutable(tree)) {
                    markHasDependencies();
                }
            }

            @Override
            public void visitInterpolateValue(JFXInterpolateValue that) {
                markHasDependencies(); // errr, umm, better safe than sorry
            }

            @Override
            public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
                markHasDependencies(); // errr, umm, better safe than sorry
            }
        }
        DependencyScanner scanner = new DependencyScanner();
        scanner.scan(expr);
        return scanner.hasDependencies;
    }

    boolean hasSideEffects(JFXExpression expr) {
        class SideEffectScanner extends JavafxTreeScanner {

            boolean hse = false;

            private void markSideEffects() {
                hse = true;
            }

            @Override
            public void visitBlockExpression(JFXBlock tree) {
                markSideEffects(); // maybe doesn't but covers all statements
            }

            @Override
            public void visitUnary(JFXUnary tree) {
                markSideEffects();
            }

            @Override
            public void visitAssign(JFXAssign tree) {
                markSideEffects();
            }

            @Override
            public void visitAssignop(JFXAssignOp tree) {
                markSideEffects();
            }

            @Override
            public void visitInstanciate(JFXInstanciate tree) {
                markSideEffects();
            }

            @Override
            public void visitFunctionInvocation(JFXFunctionInvocation tree) {
                markSideEffects();
            }

            @Override
            public void visitSelect(JFXSelect tree) {
                // Doesn't really have side-effects but the dupllicate null checking is aweful
                //TODO: do this in a cleaner way
                markSideEffects();
            }
        }
        SideEffectScanner scanner = new SideEffectScanner();
        scanner.scan(expr);
        return scanner.hse;
    }

    /**
     * Return the generated interface name corresponding to the class
     * */
    protected Name interfaceName(JFXClassDeclaration cDecl) {
        Name name = cDecl.getName();
        if (!cDecl.isMixinClass())
            return name;
        return names.fromString(name.toString() + mixinClassSuffix);
    }

    protected boolean isMixinClass(ClassSymbol sym) {
        return (sym.flags_field & JavafxFlags.MIXIN) != 0;
    }
    
    protected JCExpression makeIdentifier(DiagnosticPosition diagPos, String str) {
        assert str.indexOf('<') < 0 : "attempt to parse a type with 'Identifier'.  Use TypeTree";
        JCExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = names.fromString(part);
            tree = tree == null?
                make.at(diagPos).Ident(partName) :
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }

    protected JCExpression makeQualifiedTree(DiagnosticPosition diagPos, String str) {
        JCExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = names.fromString(part);
            tree = tree == null?
                makeIdentOfPresetKind(diagPos, partName, Kinds.PCK) :
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }

    
    protected JCExpression makeAccessExpression(DiagnosticPosition diagPos, Symbol sym, boolean makeIntf) {
        Symbol owner = sym.owner;
        Name name = sym.name;
        switch (sym.kind) {
            case Kinds.PCK:
                if (name == null || name == name.table.empty) {
                    return null;
                }
                break;
            case Kinds.TYP:
                if (owner.type != null && owner.type.tag == TypeTags.TYPEVAR) {
                    throw new RuntimeException("TYPEVAR: " + owner.type);
                }
                if (makeIntf) {
                    name = names.fromString(name.toString() + mixinClassSuffix);
                }
                break;
            default:
                return null;
        }
        if (owner != null) {
            JCExpression oExpr = makeAccessExpression(diagPos, owner, false);
            if (oExpr != null) {
                return make.at(diagPos).Select(oExpr, name);
            }
        }
        if (sym.kind == Kinds.PCK) {
            return makeIdentOfPresetKind(diagPos, name, Kinds.PCK);
        } else {
            return make.at(diagPos).Ident(name);
        }
    }

    /**
     * Build a Java AST representing the specified type.
     * Convert JavaFX class references to interface references.
     * */
    public JCExpression makeType(DiagnosticPosition diagPos, Type t) {
        return makeType(diagPos, t, true);
    }

    /**
     * Build a Java AST representing the specified type.
     * If "makeIntf" is set, convert JavaFX class references to interface references.
     * */
    public JCExpression makeType(DiagnosticPosition diagPos, Type t, boolean makeIntf) {
        while (t instanceof CapturedType) {
            WildcardType wtype = ((CapturedType) t).wildcard;
            // A kludge for Class.newInstance (and maybe other cases):
            // Applying newinstance of an object of type Class<? extends T>
            // should yield an instance of T rather than a ? extends T,
            // which would confuse the back-end.
            t = wtype.kind == BoundKind.EXTENDS ? wtype.type : wtype;
        }
        JCExpression texp = makeTypeTreeInner(diagPos, t, makeIntf);
        texp.type = t;
        return texp;
    }

    private JCExpression makeTypeTreeInner(DiagnosticPosition diagPos, Type t, boolean makeIntf) {
        while (t instanceof CapturedType)
            t = ((CapturedType) t).wildcard;
        switch (t.tag) {
            case TypeTags.CLASS: {
                JCExpression texp = null;
                boolean isMixin = types.isMixin(t.tsym);

                if (makeIntf && isMixin) {
                    texp = makeAccessExpression(diagPos, t.tsym, true);
                } else {
                    if (t.isCompound()) {
                        t = types.supertype(t);
                    }
                    texp = makeAccessExpression(diagPos, t.tsym, false);
                }

                // Type outer = t.getEnclosingType();
                if (!t.getTypeArguments().isEmpty()) {
                    List<JCExpression> targs = List.nil();
                    for (Type ta : t.getTypeArguments()) {
                        targs = targs.append(makeTypeTreeInner(diagPos, ta, makeIntf));
                    }
                    texp = make.at(diagPos).TypeApply(texp, targs);
                }
                return texp;
            }
            case TypeTags.BOT: { // it is the null type, punt and make it the Object type
                return makeQualifiedTree(diagPos, syms.objectType.tsym.getQualifiedName().toString());
            }
            case TypeTags.WILDCARD: {
                WildcardType wtype = (WildcardType) t;
                return make.at(diagPos).Wildcard(make.TypeBoundKind(wtype.kind),
                        wtype.kind == BoundKind.UNBOUND ? null
                        : makeTypeTreeInner(diagPos,wtype.type, makeIntf));
            }
            case TypeTags.ARRAY: {
                return make.at(diagPos).TypeArray(makeTypeTreeInner(diagPos,types.elemtype(t), makeIntf));
            }
            default: {
                return make.at(diagPos).Type(t);
            }
        }
    }

    /**
     * Build a Java AST representing the return type of a function.
     * Generate the return type as a Location if "isBound" is set.
     * */
    public JCExpression makeReturnTypeTree(DiagnosticPosition diagPos, MethodSymbol mth, boolean isBound) {
        Type returnType = isBound? syms.javafx_PointerType : mth.getReturnType();
        return makeType(diagPos, returnType);
    }

    boolean needsDefaultValue(TypeMorphInfo tmi) {
        return tmi.getTypeKind() == TYPE_KIND_SEQUENCE ||
               tmi.getRealType() == syms.javafx_StringType ||
               tmi.getRealType() == syms.javafx_DurationType;
    }

    JCExpression makeDefaultValue(DiagnosticPosition diagPos, TypeMorphInfo tmi) {
        return tmi.getTypeKind() == TYPE_KIND_SEQUENCE ?
                accessEmptySequence(diagPos, tmi.getElementType()) :
            tmi.getRealType() == syms.javafx_StringType ?
                make.Literal("") :
            tmi.getRealType() == syms.javafx_DurationType ?
                makeQualifiedTree(diagPos, JavafxDefs.zero_DurationFieldName) :
                makeLit(diagPos, tmi.getRealType(), tmi.getDefaultValue());
    }

    /** Make an attributed tree representing a literal. This will be
     *  a Literal node.
     *  @param type       The literal's type.
     *  @param value      The literal's value.
     */
    JCExpression makeLit(DiagnosticPosition diagPos, Type type, Object value) {
        int tag = value==null? TypeTags.BOT : type.tag;
        return make.at(diagPos).Literal(tag, value).setType(
            tag == TypeTags.BOT? syms.botType : type.constType(value)); 
    }

    JCExpression call(DiagnosticPosition diagPos, RuntimeMethod meth, List<JCExpression> typeArgs, List<JCExpression> args) {
        JCExpression select = make.at(diagPos).Select(makeQualifiedTree(diagPos, meth.classString), meth.methodName);
        return make.at(diagPos).Apply(typeArgs, select, args);
    }

    JCMethodInvocation call(DiagnosticPosition diagPos, JCExpression receiver, Name methodName, Object args) {
        JCExpression expr = null;
        if (receiver == null) {
            expr = make.at(diagPos).Ident(methodName);
        } else {
            expr = make.at(diagPos).Select(receiver, methodName);
        }
        return make.at(diagPos).Apply(List.<JCExpression>nil(), expr, (args == null) ? List.<JCExpression>nil() : (args instanceof List) ? (List<JCExpression>) args : (args instanceof ListBuffer) ? ((ListBuffer<JCExpression>) args).toList() : (args instanceof JCExpression) ? List.<JCExpression>of((JCExpression) args) : null);
    }

    Name functionInterfaceName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, isBound);
    }

    Name functionName(MethodSymbol sym) {
        return functionName(sym, false);
    }

    Name functionName(MethodSymbol sym, String full, boolean markAsImpl, boolean isBound) {
        if (markAsImpl) {
            full = full + JavafxDefs.implFunctionSuffix;
        }
        if (isBound) {
            full = full + JavafxDefs.boundFunctionDollarSuffix + getParameterTypeSuffix(sym);
        }
        return names.fromString(full);
    }

    Name functionName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, false, isBound);
    }

    Name functionName(MethodSymbol sym, boolean markAsImpl, boolean isBound) {
        if (!markAsImpl && !isBound) {
            return sym.name;
        }
        return functionName(sym, sym.name.toString(), markAsImpl, isBound);
    }

    Name varMapName(ClassSymbol sym) {
        String className = sym.fullname.toString();
        return names.fromString(varMap_FXObjectFieldPrefix + className.replace('.', '$'));
    }

    Name varGetMapName(ClassSymbol sym) {
        String className = sym.fullname.toString();
        return names.fromString(varGetMapString + className.replace('.', '$'));
    }

    Name attributeOffsetName(Symbol sym) {
        return prefixedAttributeName(sym, offset_AttributeFieldPrefix);
    }
    
    Name attributeValueName(Symbol sym) {
        return prefixedAttributeName(sym, value_AttributeFieldPrefix);
    }

    Name attributeGetterName(Symbol sym) {
        return prefixedAttributeName(sym, get_AttributeMethodPrefix);
    }

    Name attributeSetterName(Symbol sym) {
        return prefixedAttributeName(sym, set_AttributeMethodPrefix);
    }

    Name attributeBeName(Symbol sym) {
        return prefixedAttributeName(sym, be_AttributeMethodPrefix);
    }
    
    Name attributeInvalidateName(Symbol sym) {
        return prefixedAttributeName(sym, invalidate_AttributeMethodPrefix);
    }
    
    Name attributeOnReplaceName(Symbol sym) {
        return prefixedAttributeName(sym, onReplace_AttributeMethodPrefix);
    }
    
    Name attributeGetMixinName(Symbol sym) {
        return prefixedAttributeName(sym, getMixin_AttributeMethodPrefix);
    }
    
    Name attributeGetVOFFName(Symbol sym) {
        return prefixedAttributeName(sym, getVOFF_AttributeMethodPrefix);
    }

    Name attributeSetMixinName(Symbol sym) {
        return prefixedAttributeName(sym, setMixin_AttributeMethodPrefix);
    }
    
    Name attributeGetElementName(Symbol sym) {
        return prefixedAttributeName(sym, getElement_AttributeMethodPrefix);
    }

    Name attributeSizeName(Symbol sym) {
        return prefixedAttributeName(sym, size_AttributeMethodPrefix);
    }
    
    Name attributeSavedName(Symbol sym) {
        return prefixedAttributeName(sym, saved_AttributeFieldPrefix);
    }

    Name attributeInitVarBitsName(Symbol sym) {
        return prefixedAttributeName(sym, initVarBits_AttributeMethodPrefix);
    }
 
    Name attributeApplyDefaultsName(Symbol sym) {
        return prefixedAttributeName(sym, applyDefaults_AttributeMethodPrefix);
    }

    Name boundFunctionObjectParamName(Name suffix) {
        return names.fromString(boundFunctionObjectParamPrefix + suffix);
    }

    Name boundFunctionVarNumParamName(Name suffix) {
        return names.fromString(boundFunctionVarNumParamPrefix + suffix);
    }

    boolean isBoundFunctionResult(Symbol sym) {
        // Is this symbol result var storing bound function's result value?
        // Check if the variable is synthetic, type is Pointer and naming convention
        // is followed for bound function result value.
        return ((sym.flags() & Flags.SYNTHETIC) != 0L) &&
            types.isSameType(syms.javafx_PointerType, sym.type) &&
            sym.name.startsWith(defs.boundFunctionResultName);
    }

    Name paramOldValueName(JFXOnReplace onReplace) {
        return onReplace == null || onReplace.getOldValue() == null ? defs.varOldValue_LocalVarName
                : onReplace.getOldValue().getName();
    }

    Name paramNewValueName(JFXOnReplace onReplace) {
        return onReplace == null || onReplace.getNewElements() == null ? defs.varNewValue_ArgName
                : onReplace.getNewElements().getName();
    }

    Name paramStartPosName(JFXOnReplace onReplace) {
        return onReplace == null || onReplace.getFirstIndex() == null ? defs.startPos_ArgName
                : onReplace.getFirstIndex().getName();
    }

    Name paramEndPosName(JFXOnReplace onReplace) {
        return onReplace == null || onReplace.getLastIndex() == null ||
                      onReplace.getEndKind() != JFXSequenceSlice.END_EXCLUSIVE ? defs.endPos_ArgName
                : onReplace.getLastIndex().getName();
    }

    Name paramNewElementsLengthName(JFXOnReplace onReplace) {
        JFXVar newElements = onReplace == null ? null : onReplace.getNewElements();
        if (newElements == null)
            return defs.newLength_ArgName;
        else
            return newElements.sym.name.append(defs.lengthSuffixName);
    }

    Name scriptLevelAccessMethod(Symbol clazz) {
        return defs.scriptLevelAccessMethod(names, clazz);
    }

    private Name prefixedAttributeName(Symbol sym, String prefix) {
        Symbol owner = sym.owner;
        if (!types.isJFXClass(owner)) {
            return sym.name;
        }
        String sname = sym.name.toString();
        // JFXC-2837 - Mixins: script-private vars no longer hidden -- var with same name as
        // var in subclass, but with different type fails
        long flags = sym.flags();
        boolean isStatic = (flags & STATIC) != 0;
        boolean privateAccess = (flags & JavafxFlags.JavafxInstanceVarFlags &
                                         ~(JavafxFlags.SCRIPT_PRIVATE | Flags.PRIVATE)) == 0L;
        if (!isStatic && privateAccess && types.isJFXClass(owner)) {
            // mangle name to hide it
            sname = owner.toString().replace('.', '$') + '$' + sname;
        }
        return names.fromString( prefix + sname );
    }

    private String getParameterTypeSuffix(MethodSymbol sym) {
        StringBuilder sb = new StringBuilder();
        if (sym != null && sym.type != null) {
            Type mtype = sym.type;
            if (sym.type.tag == TypeTags.FORALL) {
                mtype = ((ForAll) mtype).asMethodType();
            }
            if (mtype.tag == TypeTags.METHOD) {
                List<Type> argtypes = ((MethodType) mtype).getParameterTypes();
                int argtypesCount = argtypes.length();
                int counter = 0;
                for (Type argtype : argtypes) {
                    sb.append(escapeTypeName(types.erasure(argtype)));
                    if (counter < argtypesCount - 1) {
                        // Don't append type separator after the last type in the signature.
                        sb.append(JavafxDefs.escapeTypeChar);
                        // Double separator between type names.
                        sb.append(JavafxDefs.escapeTypeChar);
                    }
                    counter++;
                }
            }
        }
        return sb.toString();
    }

    JCExpression accessEmptySequence(DiagnosticPosition diagPos, Type elemType) {
        return make.at(diagPos).Select(TypeInfo(diagPos, elemType), defs.emptySequence_FieldName);
    }

    private String escapeTypeName(Type type) {
        return type.toString().replace(JavafxDefs.typeCharToEscape, JavafxDefs.escapeTypeChar);
    }

    private JCExpression primitiveTypeInfo(DiagnosticPosition diagPos, Name typeName) {
        return make.at(diagPos).Select(makeQualifiedTree(diagPos, cTypeInfo), typeName);
    }
    
    /**
     * Given type, return an expression whose value is the corresponding TypeInfo.
     * @param diagPos
     * @param type
     * @return expression representing the TypeInfo of the class
     */
    JCExpression TypeInfo(DiagnosticPosition diagPos, Type type) {
        Type ubType = types.unboxedType(type);
        if (ubType.tag != TypeTags.NONE)
            type = ubType;
        if (types.isSameType(type, syms.javafx_BooleanType)) {
            return primitiveTypeInfo(diagPos, syms.booleanTypeName);
        } else if (types.isSameType(type, syms.javafx_CharacterType)) {
            return primitiveTypeInfo(diagPos, syms.charTypeName);
        } else if (types.isSameType(type, syms.javafx_ByteType)) {
            return primitiveTypeInfo(diagPos, syms.byteTypeName);
        } else if (types.isSameType(type, syms.javafx_ShortType)) {
            return primitiveTypeInfo(diagPos, syms.shortTypeName);
        } else if (types.isSameType(type, syms.javafx_IntegerType)) {
            return primitiveTypeInfo(diagPos, syms.integerTypeName);
        } else if (types.isSameType(type, syms.javafx_LongType)) {
            return primitiveTypeInfo(diagPos, syms.longTypeName);
        } else if (types.isSameType(type, syms.javafx_FloatType)) {
            return primitiveTypeInfo(diagPos, syms.floatTypeName);
        } else if (types.isSameType(type, syms.javafx_DoubleType)) {
            return primitiveTypeInfo(diagPos, syms.doubleTypeName);
        } else if (types.isSameType(type, syms.javafx_StringType)) {
            return primitiveTypeInfo(diagPos, syms.stringTypeName);
        } else if (types.isSameType(type, syms.javafx_DurationType)) {
            JCExpression fieldRef = make.at(diagPos).Select(makeType(diagPos, type), defs.defaultingTypeInfo_FieldName);
            // If TYPE_INFO becomes a Location again, ad back this line
            //    fieldRef = getLocationValue(diagPos, fieldRef, TYPE_KIND_OBJECT);
            return fieldRef;
        } else {
            assert !type.isPrimitive();
            List<JCExpression> typeArgs = List.of(makeType(diagPos, type, true));
            return call(diagPos, defs.TypeInfo_getTypeInfo, typeArgs, List.<JCExpression>nil());
        }
    }

    protected Type operationalType(Type srcType) {
        switch (srcType.tag) {
            case TypeTags.BYTE:
            case TypeTags.SHORT:
                return syms.intType;
            default:
                return srcType;
        }
    }

    protected abstract String getSyntheticPrefix();

    Name getSyntheticName(String kind) {
        return names.fromString(getSyntheticPrefix() + syntheticNameCounter++ + kind);
    }

    public Name indexVarName(JFXForExpressionInClause clause) {
        return indexVarName(clause.getVar().getName(), names);
    }
    public Name indexVarName(JFXIdent var) {
        return indexVarName(var.getName(), names);
    }
    public static Name indexVarName(Name name, Name.Table names) {
        return names.fromString("$indexof$" + name.toString());
    }

    JCIdent makeIdentOfPresetKind(DiagnosticPosition diagPos, Name name, int pkind) {
        AugmentedJCIdent id = new AugmentedJCIdent(name, pkind);
        id.pos = (diagPos == null ? Position.NOPOS : diagPos.getStartPosition());
        return id;
    }

    protected JCModifiers addAccessAnnotationModifiers(DiagnosticPosition diagPos, long flags, JCModifiers mods, List<JCAnnotation> annotations) {
        make.at(diagPos);
        JCModifiers ret = mods;
        if ((flags & Flags.PUBLIC) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.publicAnnotationClassNameString), List.<JCExpression>nil()));
        }
        else if ((flags & Flags.PROTECTED) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.protectedAnnotationClassNameString), List.<JCExpression>nil()));
        }
        else if ((flags & JavafxFlags.SCRIPT_PRIVATE) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.scriptPrivateAnnotationClassNameString), List.<JCExpression>nil()));
        }
        else {        // otherwise it is package access
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.packageAnnotationClassNameString), List.<JCExpression>nil()));
        }

        if ((flags & JavafxFlags.PUBLIC_INIT) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.publicInitAnnotationClassNameString), List.<JCExpression>nil()));
        }
        if ((flags & JavafxFlags.PUBLIC_READ) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.publicReadAnnotationClassNameString), List.<JCExpression>nil()));
        }

        if ((flags & JavafxFlags.IS_DEF) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.defAnnotationClassNameString), List.<JCExpression>nil()));
        }

        if ((flags & Flags.STATIC) != 0) {
            annotations = annotations.prepend(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.staticAnnotationClassNameString), List.<JCExpression>nil()));
        }

        if (annotations.nonEmpty()) {
            ret = make.Modifiers(mods.flags, annotations);
        }
        return ret;
    }

    protected JCModifiers addAccessAnnotationModifiers(DiagnosticPosition diagPos, long flags, JCModifiers mods) {
        return addAccessAnnotationModifiers(diagPos, flags, mods, List.<JCAnnotation>nil());
    }

    protected JCModifiers addInheritedAnnotationModifiers(DiagnosticPosition diagPos, long flags, JCModifiers mods) {
        return make.Modifiers(mods.flags, List.of(make.Annotation(makeIdentifier(diagPos, JavafxSymtab.inheritedAnnotationClassNameString), List.<JCExpression>nil())));
    }

    protected void pretty(JCTree tree) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        Pretty pretty = new Pretty(osw, false);
        try {
            pretty.println();
            pretty.print("+++++++++++++++++++++++++++++++++");
            pretty.println();
            pretty.printExpr(tree);
            pretty.println();
            pretty.print("---------------------------------");
            pretty.println();
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Pretty print got: " + ex);
        }
    }

    protected void fxPretty(JFXTree tree) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.println();
            pretty.print("+++++++++++++++++++++++++++++++++");
            pretty.println();
            pretty.printExpr(tree);
            pretty.println();
            pretty.print("---------------------------------");
            pretty.println();
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Pretty print got: " + ex);
        }
    }

    protected JCExpression castFromObject (JCExpression arg, Type castType) {
       return make.TypeCast(makeType(arg.pos(), types.boxedTypeOrType(castType)), arg);
    }

    protected class JavaTreeBuilder {

        protected DiagnosticPosition diagPos;
        private final JFXClassDeclaration enclosingClassDecl;
        private boolean isScript;

        protected JavaTreeBuilder(DiagnosticPosition diagPos, JFXClassDeclaration enclosingClassDecl, boolean isScript) {
            this.diagPos = diagPos;
            this.enclosingClassDecl = enclosingClassDecl;
            this.isScript = isScript;
        }


        //
        // Returns the current class decl.
        //
        public JFXClassDeclaration getCurrentClassDecl() { return enclosingClassDecl; }

        //
        // Returns true if the class (or current class) is a mixin.
        //
        public boolean isMixinClass() {
            return isMixinClass(enclosingClassDecl.sym);
        }

        public boolean isMixinClass(Symbol sym) {
            return (sym.flags() & JavafxFlags.MIXIN) != 0;
        }
        
        public boolean isLocalClass(Symbol sym) {
            return sym.owner.kind == Kinds.MTH;
         }
        
        public boolean isLocalClass() {
            return isLocalClass(enclosingClassDecl.sym);
        }
                
        public boolean isMixinVar(Symbol sym) {
            assert sym instanceof VarSymbol : "Expect a var symbol, got " + sym;
            VarSymbol varSym = (VarSymbol)sym;
            
            return isMixinClass(varSym.owner) && !varSym.isStatic();
        }
        
        public boolean isLocalClassVar(Symbol sym) {
            assert sym instanceof VarSymbol : "Expect a var symbol, got " + sym;
            VarSymbol varSym = (VarSymbol)sym;
            
            return isLocalClass(varSym.owner);
        }
        
        public boolean setIsScript(boolean newState) {
            boolean oldState = isScript;
            isScript = newState;
            return oldState;
        }
        
        public boolean isScript() {
            return isScript;
        }


        protected void setDiagPos(DiagnosticPosition diagPos) {
            this.diagPos = diagPos;
        }

        protected TreeMaker m() {
            return make.at(diagPos);
        }

        /**
         * Make an identifier which references the specified variable declaration
         */
        protected JCIdent id(JCVariableDecl aVar) {
            return id(aVar.name);
        }

        /**
         * Make an identifier of the given name
         */
        protected JCIdent id(Name name) {
            return m().Ident(name);
        }

        /**
         * Make an identifier of the given symbol
         */
        protected JCIdent id(Symbol sym) {
            return m().Ident(sym.name);
        }

        /**
         * Make a member select or an identifier depending on the selector
         */
        protected JCExpression Select(JCExpression selector, Name name) {
            return (selector==null)? id(name) : m().Select(selector, name);
        }

        /**
         * Standard method parameters
         */

        JCIdent makeMethodArg(Name name, Type type) {
            JCIdent id = id(name);
            id.type = type;
            return id;
        }

        JCIdent posArg() {
            return makeMethodArg(defs.pos_ArgName, syms.intType);
        }

        JCIdent startPosArg() {
            return makeMethodArg(defs.startPos_ArgName, syms.intType);
        }

        JCIdent endPosArg() {
            return makeMethodArg(defs.endPos_ArgName, syms.intType);
        }

        JCIdent newLengthArg() {
            return makeMethodArg(defs.newLength_ArgName, syms.intType);
        }

        JCIdent phaseArg() {
            return makeMethodArg(defs.phase_ArgName, syms.intType);
        }

        /**
         * Convert type to JCExpression
         */
        protected JCExpression makeType(Type type, boolean makeIntf) {
            return JavafxTranslationSupport.this.makeType(diagPos, type, makeIntf);
        }

        protected JCExpression makeType(Type type) {
            return makeType(type, true);
        }

        protected JCExpression makeType(Symbol sym) {
            return makeType(sym.type, true);
        }

        // Return a receiver$, scriptLevelAccess$() or null depending on the context.
        //
        protected JCExpression getReceiver() {
            return getReceiverInternal(enclosingClassDecl.sym, true);
        }

        protected JCExpression getReceiverOrThis() {
            return getReceiverInternal(enclosingClassDecl.sym, false);
        }

        protected JCExpression getReceiver(Symbol sym) {
            if (sym.isStatic()) {
                return Call(scriptLevelAccessMethod(sym.owner));
            }
            return getReceiverInternal(sym.owner, true);
        }

        protected JCExpression getReceiverOrThis(Symbol sym) {
            if (sym.isStatic()) {
                return Call(scriptLevelAccessMethod(sym.owner));
            }
            return getReceiverInternal(sym.owner, false);
        }
        
        private JCExpression getReceiverInternal(Symbol sym, boolean nullForThis) {
            return (isMixinClass() && !isScript) ?
                id(defs.receiverName) :
                resolveThis(sym, nullForThis);
        }

        protected JCExpression resolveThis(Symbol owner, boolean nullForThis) {
            JCExpression _this = resolveThisInternal(owner, enclosingClassDecl.sym, false);
            return (nullForThis && _this.getTag() == JCTree.IDENT) ?
                null :
                _this;
        }
        //where
        private JCExpression resolveThisInternal(Symbol ownerThis, Symbol currentThis, boolean rec) {
            JCExpression thisExpr = rec ? 
                Select(makeType(currentThis.type), names._this) :
                id(names._this);
            if (!currentThis.isSubClass(ownerThis, types)) {
                Type encl = currentThis.type.getEnclosingType();
                if (encl == null || encl == Type.noType || types.isMixin(encl.tsym)) {
                    return resolveThisInternal(ownerThis, currentThis, thisExpr);
                }
                return resolveThisInternal(ownerThis, currentThis.type.getEnclosingType().tsym, true);
            }
            else {
                return thisExpr;
            }
        }
        //where
        private JCExpression resolveThisInternal(Symbol ownerThis, Symbol currentThis, JCExpression receiver) {
            if (currentThis == null) {
                throw new AssertionError("Cannot find owner");
            }
            else if (!currentThis.isSubClass(ownerThis, types)) {
                return resolveThisInternal(ownerThis, currentThis.owner.enclClass(), Call(receiver, defs.outerAccessor_MethodName));
            }
            else {
                return receiver;
            }
        }
        
        protected JCExpression resolveSuper(Symbol owner) {
            return resolveSuperInternal(owner, enclosingClassDecl.sym, false);
        }

        private JCExpression resolveSuperInternal(Symbol ownerSym, Symbol currentSym, boolean rec) {
            JCExpression superExpr = rec ?
                Select(makeType(currentSym.type), names._super) :
                id(names._super);
            if (!currentSym.isSubClass(ownerSym, types)) {
                Type encl = currentSym.type.getEnclosingType();
                return resolveSuperInternal(ownerSym, currentSym.type.getEnclosingType().tsym, true);
            }
            else {
                return superExpr;
            }
        }

        //
        // Private support methods for testing/setting/clearing a var flag.
        //

        private JCExpression FlagAction(VarSymbol varSym, Name action, Name clearBits, Name setBits) {
            return FlagAction(varSym, action,
                        clearBits != null ? id(clearBits) : Int(0),
                        setBits != null ? id(setBits) : Int(0));
        }
        private JCExpression FlagAction(VarSymbol varSym, Name action, JCExpression clearBits, JCExpression setBits) {
            return Call(getReceiver(varSym), action, Offset(varSym),
                        clearBits != null ? clearBits : Int(0),
                        setBits != null ? setBits : Int(0));

        }
        private JCExpression FlagAction(JCExpression offset, Name action, Name clearBits, Name setBits) {
            return Call(action, offset,
                        clearBits != null ? id(clearBits) : Int(0),
                        setBits != null ? id(setBits) : Int(0));
        }

        //
        // These methods return an expression for testing/setting/clearing a var flag.
        //

        protected JCExpression FlagTest(VarSymbol varSym, Name clearBits, Name setBits) {
            return FlagAction(varSym, defs.varFlagActionTest, clearBits, setBits);
        }
        protected JCExpression FlagTest(VarSymbol varSym, JCExpression clearBits, JCExpression setBits) {
            return FlagAction(varSym, defs.varFlagActionTest, clearBits, setBits);
        }
        protected JCExpression FlagTest(JCExpression offset, Name clearBits, Name setBits) {
            return FlagAction(offset, defs.varFlagActionTest, clearBits, setBits);
        }

        protected JCExpression FlagChange(VarSymbol varSym, Name clearBits, Name setBits) {
            return FlagAction(varSym, defs.varFlagActionChange, clearBits, setBits);
        }
        protected JCExpression FlagChange(VarSymbol varSym, JCExpression clearBits, JCExpression setBits) {
            return FlagAction(varSym, defs.varFlagActionChange, clearBits, setBits);
        }
        protected JCExpression FlagChange(JCExpression offset, Name clearBits, Name setBits) {
            return FlagAction(offset, defs.varFlagActionChange, clearBits, setBits);
        }


        //
        // These methods returns a statement for setting/clearing a var flag.
        //

        protected JCStatement FlagChangeStmt(VarSymbol varSym, Name clearBits, Name setBits) {
            return Stmt(FlagChange(varSym, clearBits, setBits));
        }

        protected JCStatement FlagChangeStmt(VarSymbol varSym, JCExpression clearBits, JCExpression setBits) {
            return Stmt(FlagChange(varSym, clearBits, setBits));
        }

        protected JCStatement FlagChangeStmt(JCExpression offset, Name clearBits, Name setBits) {
            return Stmt(FlagChange(offset, clearBits, setBits));
        }

        //
        // Methods to generate simple constants.
        //
        protected JCExpression Int(int value)         { return m().Literal(TypeTags.INT, value); }
        protected JCExpression Boolean(boolean value) { return m().Literal(TypeTags.BOOLEAN, value ? 1 : 0); }
        protected JCExpression Null()                 { return m().Literal(TypeTags.BOT, null); }
        protected JCExpression String(String str)     { return m().Literal(TypeTags.CLASS, str); }

        protected JCStatement Stmt(JCExpression expr) {
            return m().Exec(expr);
        }

        protected JCStatement Return(JCExpression expr) {
            return m().Return(expr);
        }

        protected JCStatement Stmt(JCExpression expr, Type returnType) {
            return (returnType==null || returnType==syms.voidType)? 
                  Stmt(expr)
                : Return(expr);
        }

        //
        // Binary and Unary operators
        //

        JCExpression LT(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.LT, v1, v2);
        }
        JCExpression LE(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.LE, v1, v2);
        }
        JCExpression GT(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.GT, v1, v2);
        }
        JCExpression GE(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.GE, v1, v2);
        }
        JCExpression EQ(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.EQ, v1, v2);
        }
        JCExpression NE(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.NE, v1, v2);
        }
        JCExpression AND(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.AND, v1, v2);
        }
        JCExpression OR(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.OR, v1, v2);
        }
        JCExpression PLUS(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.PLUS, v1, v2);
        }
        JCExpression MINUS(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.MINUS, v1, v2);
        }
        JCExpression MUL(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.MUL, v1, v2);
        }
        JCExpression MOD(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.MOD, v1, v2);
        }
        JCExpression DIV(JCExpression v1, JCExpression v2) {
            return m().Binary(JCTree.DIV, v1, v2);
        }
        JCExpression NEG(JCExpression v1) {
            return m().Unary(JCTree.NEG, v1);
        }
        JCExpression NOT(JCExpression v1) {
            return m().Unary(JCTree.NOT, v1);
        }

        /**
         * Compare against null
         */
        protected JCExpression EQnull(JCExpression targ) {
            return EQ(targ, Null());
        }

        protected JCExpression NEnull(JCExpression targ) {
            return NE(targ, Null());
        }

        /**
         * Make a variable -- final by default
         */

        protected JCVariableDecl Var(JCModifiers modifiers, JCExpression varType, Name varName, JCExpression initialValue, VarSymbol varSym) {
            JCVariableDecl varDecl = m().VarDef(
                                            modifiers,
                                            varName,
                                            varType,
                                            initialValue);
            varDecl.sym = varSym;
            return varDecl;
        }

        protected JCVariableDecl Var(long flags, Type varType, Name varName, JCExpression initialValue, VarSymbol varSym) {
            return Var(m().Modifiers(flags), makeType(varType), varName, initialValue, varSym);
        }

        protected JCVariableDecl Var(long flags, JCExpression varType, Name varName, JCExpression initialValue) {
            return Var(m().Modifiers(flags), varType, varName, initialValue, null);
        }

        protected JCVariableDecl Var(long flags, Type varType, Name varName, JCExpression initialValue) {
            return Var(flags, varType, varName, initialValue, null);
        }

        protected JCVariableDecl Var(Type varType, Name varName, JCExpression value) {
            return Var(Flags.FINAL, varType, varName, value);
        }

        protected JCVariableDecl Var(long flags, Type varType, String varName, JCExpression initialValue) {
            return Var(flags, varType, names.fromString(varName), initialValue);
        }
        
        /**
         * Make a method paramter
         */
        protected JCVariableDecl Param(Type varType, Name varName) {
            return Var(Flags.PARAMETER | Flags.FINAL, varType, varName, null);
        }

       /**
        * Make a receiver parameter.
        * Its type is that of the corresponding interface and it is a final parameter.
        * */
        JCVariableDecl ReceiverParam(JFXClassDeclaration cDecl) {
            return m().VarDef(
                    m().Modifiers(Flags.PARAMETER | Flags.FINAL),
                    defs.receiverName,
                    id(interfaceName(cDecl)),
                    null);
        }

        /**
         * Make a variable (synthethic name) -- final by default
         */

        protected JCVariableDecl MutableTmpVar(String root, Type varType, JCExpression initialValue) {
            return TmpVar(0L, root, varType, initialValue);
        }

        protected JCVariableDecl TmpVar(Type type, JCExpression value) {
            return TmpVar("tmp", type, value);
        }

        protected JCVariableDecl TmpVar(String root, Type varType, JCExpression value) {
            return TmpVar(Flags.FINAL, root, varType, value);
        }

        protected JCVariableDecl TmpVar(long flags, String root, Type varType, JCExpression initialValue) {
            return Var(flags, varType, getSyntheticName(root), initialValue);
        }

       /**
         * Block Expressions
         */

        BlockExprJCBlockExpression BlockExpression(List<JCStatement> stmts, JCExpression value) {
            BlockExprJCBlockExpression bexpr = new BlockExprJCBlockExpression(0L, stmts, value);
            bexpr.pos = (diagPos == null ? Position.NOPOS : diagPos.getStartPosition());
            return bexpr;
        }

        BlockExprJCBlockExpression BlockExpression(ListBuffer<JCStatement> stmts, JCExpression value) {
            return BlockExpression(stmts.toList(), value);
        }

        BlockExprJCBlockExpression BlockExpression(JCStatement stmt1, JCExpression value) {
            return BlockExpression(List.of(stmt1), value);
        }

        BlockExprJCBlockExpression BlockExpression(JCStatement stmt1, JCStatement stmt2, JCExpression value) {
            return BlockExpression(List.of(stmt1, stmt2), value);
        }

       /**
         * Block
         */

        JCBlock Block(List<JCStatement> prolog, JCStatement... epilog) {
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            for (JCStatement p : prolog) stmts.append(p);
            for (JCStatement e : epilog) stmts.append(e);
            return Block(stmts);
        }

        JCBlock Block(ListBuffer<JCStatement> prolog, JCStatement... epilog) {
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            for (JCStatement p : prolog) stmts.append(p);
            for (JCStatement e : epilog) stmts.append(e);
            return Block(stmts);
        }

        JCBlock Block(List<JCStatement> stmts) {
            return m().Block(0L, stmts);
        }

        JCBlock Block(ListBuffer<JCStatement> stmts) {
            return Block(stmts.toList());
        }

        JCBlock Block(JCStatement... stmts) {
            return Block(List.from(stmts));
        }
        
        boolean isBlockEmpty(JCBlock block) {
            return block == null || block.getStatements().isEmpty();
        }

        List<JCStatement> Stmts(JCStatement... stmts) {
            return List.from(stmts);
        }

       /**
         * If / Condition
         */

        JCStatement If(JCExpression cond, JCStatement thenStmt, JCStatement elseStmt) {
            return m().If(cond, thenStmt, elseStmt);
        }

        JCStatement If(JCExpression cond, JCStatement thenStmt) {
            return m().If(cond, thenStmt, null);
        }

        JCExpression If(JCExpression cond, JCExpression thenExpr, JCExpression elseExpr) {
            return m().Conditional(cond, thenExpr, elseExpr);
        }
        
        
       /**
         * Optimal If
         */

        JCStatement OptIf(JCExpression cond, JCStatement thenStmt) {
            return OptIf(cond, thenStmt, null);
        }

        JCStatement OptIf(JCExpression cond, JCStatement thenStmt, JCStatement elseStmt) {
            boolean noThen = thenStmt == null || (thenStmt instanceof JCBlock && isBlockEmpty((JCBlock)thenStmt));
            boolean noElse = elseStmt == null || (elseStmt instanceof JCBlock && isBlockEmpty((JCBlock)elseStmt));
            
            if (!noThen) {
                return If(cond, thenStmt, noElse ? null : elseStmt);
            } if (!noElse) {
                return If(NOT(cond), elseStmt, null);
            }
            
            return null;
        }

        /**
         * Try
         */
        JCStatement Try(JCBlock body, JCCatch cat, JCBlock finalizer) {
            ListBuffer<JCCatch> catches = ListBuffer.lb();
            catches.append(cat);
            return m().Try(body, catches.toList(), finalizer);
        }
        JCStatement Try(JCBlock body, JCCatch cat) {
            return Try(body, cat, null);
        }

        // generates catch(RuntimeException re) { ErrorHandler.bindException(re); <onCatchStat> }
        JCCatch ErrorHandler(JCStatement onCatchStat) {
            JCVariableDecl tmpVar = TmpVar(syms.runtimeExceptionType, null);
            JCStatement callErrorHandler = CallStmt(defs.ErrorHandler_bindException, id(tmpVar));
            JCBlock blk = (onCatchStat != null)? Block(callErrorHandler, onCatchStat) : Block(callErrorHandler);
            return m().Catch(tmpVar, blk);
        }

        JCStatement TryWithErrorHandler(JCBlock body, JCStatement onCatchStat) {
            return Try(body, ErrorHandler(onCatchStat));
        }

        JCStatement TryWithErrorHandler(JCStatement tryStat, JCStatement onCatchStat) {
            return TryWithErrorHandler(Block(tryStat), onCatchStat);
        }

        JCStatement TryWithErrorHandler(JCStatement tryStat) {
            return TryWithErrorHandler(tryStat, null);
        }

        /**
         * Make methods
         */

        protected JCMethodDecl Method(long flags, Type returnType, Name methodName, List<JCVariableDecl> params, List<JCStatement> stmts, MethodSymbol methSym) {
            return Method(m().Modifiers(flags), returnType, methodName, params, stmts, methSym);
        }

        protected JCMethodDecl Method(long flags, Type returnType, Name methodName, List<Type> paramTypes, List<JCVariableDecl> params, Symbol owner, List<JCStatement> stmts) {
            MethodSymbol methSym = makeMethodSymbol(flags, returnType, methodName, owner, paramTypes);
            return Method(m().Modifiers(flags), returnType, methodName, params, stmts, methSym);
        }

        protected JCMethodDecl Method(JCModifiers modifiers, Type returnType, Name methodName, List<JCVariableDecl> params, List<JCStatement> stmts, MethodSymbol methSym) {
            JCMethodDecl methDecl = m().MethodDef(
                                        modifiers,
                                        methodName,
                                        makeType(returnType),
                                        List.<JCTypeParameter>nil(),
                                        params != null ? params : List.<JCVariableDecl>nil(),
                                        List.<JCExpression>nil(),
                                        stmts == null ? null : Block(stmts),
                                        null);
            methDecl.sym = methSym;
            return methDecl;
        }

        protected JCExpression QualifiedTree(String str) {
            return JavafxTranslationSupport.this.makeQualifiedTree(diagPos, str);
        }

        /**
         * Var accessors -- returning a JCExpression
         */

        public JCExpression Get(Symbol sym) {
            VarSymbol varSym = (VarSymbol)sym;
            
            if (isMixinVar(varSym)) {
                return Call(attributeGetMixinName(varSym));
            } else if (varSym.isStatic()) {
                return id(attributeValueName(varSym));
            } else {
                return Select(getReceiver(varSym), attributeValueName(varSym));
            }
        }
        public JCExpression Get(JCExpression selector, Symbol sym) {
            VarSymbol varSym = (VarSymbol)sym;
            
            if (isMixinVar(varSym)) {
                return Call(selector, attributeGetMixinName(varSym));
            } else {
                return Select(selector, attributeValueName(varSym));
            }
        }

        public JCExpression Offset(Symbol sym) {
            VarSymbol varSym = (VarSymbol)sym;
            
            if (isMixinVar(varSym)) {
                return Call(getReceiver(), attributeGetVOFFName(varSym));
            } else {
                JCExpression klass = makeType(varSym.owner.type, false);
                
                if (varSym.isStatic()) {
                    klass = Select(klass, TreeInfo.name(klass).append(defs.scriptClassSuffixName));
                }
                
                return Select(klass, attributeOffsetName(varSym));
            }
        }
        public JCExpression Offset(JCExpression selector, Symbol sym) {
            VarSymbol varSym = (VarSymbol)sym;
            
            if (selector != null && isMixinVar(varSym)) {
                return Call(selector, attributeGetVOFFName(varSym));
            }
            
            return Offset(varSym);
        }

        public JCExpression Set(Symbol sym, JCExpression value) {
            assert sym instanceof VarSymbol : "Expect a var symbol, got " + sym;
            VarSymbol varSym = (VarSymbol)sym;
            
            if (isMixinVar(varSym)) {
                return Call(attributeSetMixinName(varSym), value);
            } else if (varSym.isStatic()) {
                return m().Assign(id(attributeValueName(varSym)), value);
            } else {
                return m().Assign(Select(getReceiver(varSym), attributeValueName(varSym)), value);
            }
        }
        public JCExpression Set(JCExpression selector, Symbol sym, JCExpression value) {
            assert sym instanceof VarSymbol : "Expect a var symbol, got " + sym;
            VarSymbol varSym = (VarSymbol)sym;
            
            if (isMixinVar(varSym)) {
                return Call(selector, attributeSetMixinName(varSym), value);
            } else {
                return m().Assign(Select(selector, attributeValueName(varSym)), value);
            }
        }
        public JCStatement SetStmt(Symbol sym, JCExpression value) {
            return Stmt(Set(sym, value));
        }
        public JCStatement SetStmt(JCExpression selector, Symbol sym, JCExpression value) {
            return Stmt(Set(selector, sym, value));
        }
       
        /**
         * Method call support
         */

        private List<JCExpression> callArgs(JCExpression[] args) {
            // Convert args to list.
            ListBuffer<JCExpression> argBuffer = ListBuffer.lb();
            for (JCExpression arg : args) {
                argBuffer.append(arg);
            }

            return argBuffer.toList();
        }

        /**
         * Method calls -- returning a JCExpression
         */

        JCExpression Call(JCExpression receiver, Name methodName, List<JCExpression> args) {
            JCExpression expr = Select(receiver, methodName);
            return m().Apply(List.<JCExpression>nil(), expr, args);
        }

        JCExpression Call(JCExpression receiver, Name methodName, ListBuffer<JCExpression> args) {
            return Call(receiver, methodName, args.toList());
        }

        JCExpression Call(JCExpression receiver, Name methodName, JCExpression... args) {
            return Call(receiver, methodName, callArgs(args));
        }


        JCExpression Call(Name methodName, List<JCExpression> args) {
            return Call(getReceiver(), methodName, args);
        }

        JCExpression Call(Name methodName, ListBuffer<JCExpression> args) {
            return Call(getReceiver(), methodName, args.toList());
        }

        JCExpression Call(Name methodName, JCExpression... args) {
            return Call(getReceiver(), methodName, callArgs(args));
        }


        JCExpression Call(RuntimeMethod meth, List<JCExpression> args) {
            return Call(QualifiedTree(meth.classString), meth.methodName, args);
        }

        JCExpression Call(RuntimeMethod meth, ListBuffer<JCExpression> args) {
            return Call(meth, args.toList());
        }

        JCExpression Call(RuntimeMethod meth, JCExpression... args) {
            return Call(meth, callArgs(args));
        }

        /**
         * Method calls -- returning a JCStatement
         */

        JCStatement CallStmt(JCExpression receiver, Name methodName, List<JCExpression> args) {
            return Stmt(Call(receiver, methodName, args));
        }

        JCStatement CallStmt(JCExpression receiver, Name methodName, ListBuffer<JCExpression> args) {
            return Stmt(Call(receiver, methodName, args.toList()));
        }

        JCStatement CallStmt(JCExpression receiver, Name methodName, JCExpression... args) {
            return Stmt(Call(receiver, methodName, callArgs(args)));
        }


        JCStatement CallStmt(Name methodName, List<JCExpression> args) {
            return Stmt(Call(getReceiver(), methodName, args));
        }

        JCStatement CallStmt(Name methodName, ListBuffer<JCExpression> args) {
            return Stmt(Call(getReceiver(), methodName, args.toList()));
        }

        JCStatement CallStmt(Name methodName, JCExpression... args) {
            return Stmt(Call(getReceiver(), methodName, callArgs(args)));
        }


        JCStatement CallStmt(RuntimeMethod meth, List<JCExpression> args) {
            return Stmt(Call(meth, args));
        }

        JCStatement CallStmt(RuntimeMethod meth, ListBuffer<JCExpression> args) {
            return Stmt(Call(meth, args));
        }

        JCStatement CallStmt(RuntimeMethod meth, JCExpression... args) {
            return Stmt(Call(meth, args));
        }
        
        /**
         * These methods simplify throw statements.
         */
        JCStatement Throw(Type type, String message) {
            if (message != null) {
                return m().Throw(m().NewClass(null, null, makeType(type), List.<JCExpression>of(String(message)), null));
            } else {
                return m().Throw(m().NewClass(null, null, makeType(type), List.<JCExpression>nil(), null));
            }
        }
        JCStatement Throw(Type type) {
            return Throw(type, null);
        }

        JCExpression typeCast(final Type targetType, final Type inType, final JCExpression expr) {
            if (types.typeKind(inType) == TYPE_KIND_OBJECT) {
                // We can't just cast the Object to Float (for example)
                // because if the Object is not Float, we will get a ClassCastException at runtime.
                // And we can't just call java.lang.Number.floatValue() because java.lang.Number
                // doesn't exist on mobile, at least not as of Jan 2009.
                int targetKind = types.typeKind(targetType);
                if (targetKind != TYPE_KIND_OBJECT && targetKind != TYPE_KIND_SEQUENCE) {
                    return Call(defs.Util_objectTo[targetKind], expr);
                }
            }

            // The makeTypeCast below is usually redundant, since translateAsValue
            // takes care of most conversions - except in the case of a plain object cast.
            // It would be cleaner to move the makeTypeCast to translateAsValue,
            // but it's painful to get it right.  FIXME.
            return TypeCast(targetType, inType, expr);
        }

        JCExpression TypeCast(Type clazztype, Type exprtype, JCExpression translatedExpr) {
            if (types.isSameType(clazztype, exprtype)) {
                return translatedExpr;
            } else {
                Type castType = clazztype;
                if (!exprtype.isPrimitive()) {
                    castType = types.boxedTypeOrType(castType);
                }
                if (castType.isPrimitive() && exprtype.isPrimitive()) {
                    JCTree clazz = makeType(exprtype, true);
                    translatedExpr = m().TypeCast(clazz, translatedExpr);
                }
                JCTree clazz = makeType(castType, true);
                return m().TypeCast(clazz, translatedExpr);
            }
        }

        /* Default value per type */
        JCExpression DefaultValue(Type type) {
            return JavafxTranslationSupport.this.makeDefaultValue(diagPos, typeMorpher.typeMorphInfo(type));
        }
        
        /*
         * Construct a symbol and type for a new class.
         */
        protected ClassSymbol makeClassSymbol(long flags, Name name, Symbol owner) {
            ClassSymbol classSym = new ClassSymbol(flags, name, owner);
            ClassType type = new ClassType(Type.noType, List.<Type>nil(), classSym);
            classSym.type = type;
            return classSym;
        }

        /**
         * Create a method symbol.
         */
        public MethodSymbol makeMethodSymbol(long flags, Type returnType, Name methodName, Symbol owner, List<Type> argTypes) {
            MethodType methodType = new MethodType(argTypes, returnType, List.<Type>nil(), syms.methodClass);
            return new MethodSymbol(flags, methodName, methodType, owner);
        }

        /*
         * Copy the members of a newly created JCClassDecl to it's symbol.
         */
        protected void membersToSymbol(JCClassDecl cls) {
            ClassSymbol cSym = cls.sym;
            Scope members = new Scope(cSym);
            
            for (JCTree tree : cls.getMembers()) {
                if (tree instanceof JCVariableDecl) {
                    JCVariableDecl varDecl = (JCVariableDecl)tree;
                    
                    if (varDecl.sym != null) {
                        members.enter(varDecl.sym);
                    }
                } else if (tree instanceof JCMethodDecl) {
                    JCMethodDecl methDecl = (JCMethodDecl)tree;
                    
                    if (methDecl.sym != null) {
                        members.enter(methDecl.sym);
                    }
                } else if (tree instanceof JCClassDecl) {
                    JCClassDecl classDecl = (JCClassDecl)tree;
                    
                    if (classDecl.sym != null) {
                        members.enter(classDecl.sym);
                    }
                }
            }
            
            cSym.members_field = members;
        }
        protected void membersToSymbol(ClassSymbol cSym, List<JCTree> adding) {
            HashSet<Symbol> symbols = new HashSet<Symbol>();
            Scope members = cSym.members();
           
            for (Scope.Entry e = members.elems; e != null && e.sym != null; e = e.sibling) {
                symbols.add(e.sym);
            }

            for (JCTree tree : adding) {
                if (tree instanceof JCVariableDecl) {
                    JCVariableDecl varDecl = (JCVariableDecl)tree;
                    
                    if (varDecl.sym != null && symbols.add(varDecl.sym)) {
                        members.enter(varDecl.sym);
                    }
                } else if (tree instanceof JCMethodDecl) {
                    JCMethodDecl methDecl = (JCMethodDecl)tree;
                    
                    if (methDecl.sym != null && symbols.add(methDecl.sym)) {
                        members.enter(methDecl.sym);
                    }
                } else if (tree instanceof JCClassDecl) {
                    JCClassDecl classDecl = (JCClassDecl)tree;
                    
                    if (classDecl.sym != null && symbols.add(classDecl.sym)) {
                        members.enter(classDecl.sym);
                    }
                }
            }
        }
        
        /* Debugging support */

        JCStatement Debug(String msg, JCExpression obj) {
            return CallStmt(QualifiedTree("java.lang.System.err"), names.fromString("println"),
                    obj==null?
                          String(msg)
                        : PLUS(String(msg + " "), obj));
        }

        List<JCStatement> makeDebugTrace(String msg) {
            return makeDebugTrace(msg, String(""));
        }

        List<JCStatement> makeDebugTrace(String msg, JCExpression obj) {
            String trace = options.get("debugTrace");
            return trace != null ?
                List.<JCStatement>of(Debug(msg, obj))
              : List.<JCStatement>nil();
        }
    }
}
