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
package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javac.util.Context;

import com.sun.tools.javac.util.Position;
import java.io.OutputStreamWriter;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javac.code.Flags.*;

/**
 * Common support routines needed for translation
 * 
 * @author Robert Field
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
    
    protected static final String sequencesEmptyString = "com.sun.javafx.runtime.sequence.Sequences.emptySequence";
    
    /*
     * other instance information
     */
    private int syntheticNameCounter = 0;

    /*
     * the result of translating a tree by a visit method
     */
    JCTree result;

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
        
        syntheticNameCounter = 0;
    }

    protected Symbol expressionSymbol(JFXExpression tree) {
        switch (tree.getFXTag()) {
            case IDENT:
                return ((JFXIdent) tree).sym;
            case SELECT:
                return ((JFXSelect) tree).sym;
            default:
                return null;
        }
    }

    protected Type elementType(Type seqType) {
        Type elemType = seqType.getTypeArguments().head;
        if (elemType instanceof CapturedType)
            elemType = ((CapturedType) elemType).wildcard;
        if (elemType instanceof WildcardType)
            elemType = ((WildcardType) elemType).type;
        return elemType;
    }
    
    /**
     * Return the generated interface name corresponding to the class
     * */
    protected Name interfaceName(JFXClassDeclaration cDecl) {
        Name name = cDecl.getName();
        if (cDecl.generateClassOnly())
            return name;
        return names.fromString(name.toString() + interfaceSuffix);
    }

    protected JCExpression makeIdentifier(DiagnosticPosition diagPos, Name aName) {
        String str = aName.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return make.at(diagPos).Ident(aName);
        }
        return makeIdentifier(diagPos, str);
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
            Name partName = Name.fromString(names, part);
            tree = tree == null? 
                make.at(diagPos).Ident(partName) : 
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
    
    /**
     *
     * @param diagPos
     * @return a boolean expression indicating if the bind is lazy
     */
    protected JCExpression makeLaziness(DiagnosticPosition diagPos) {
        return make.at(diagPos).Literal(TypeTags.BOOLEAN, 0);
    }

    /**
     * @param diagPos
     * @return expression representing null
     */
    protected JCExpression makeNull(DiagnosticPosition diagPos) {
        return make.at(diagPos).Literal(TypeTags.BOT, null);
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
            Name partName = Name.fromString(names, part);
            tree = tree == null? 
                make.at(diagPos).Ident(partName) : 
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
    
    /**
     * Build a Java AST representing the specified type.
     * Convert JavaFX class references to interface references.
     * */
    protected JCExpression makeTypeTree(DiagnosticPosition diagPos, Type t) {
        return makeTypeTree(diagPos, t, true);
    }
    
    /**
     * Build a Java AST representing the specified type.
     * If "makeIntf" is set, convert JavaFX class references to interface references.
     * */
    protected JCExpression makeTypeTree(DiagnosticPosition diagPos, Type t, boolean makeIntf) {
        while (t instanceof CapturedType) {
            WildcardType wtype = ((CapturedType) t).wildcard;
            // A kludge for Class.newInstance (and maybe other cases):
            // Applying newinstance of an object of type Class<? extends T>
            // should yield an instance of T rather than a ? extends T,
            // which would confuse the back-end.
            t = wtype.kind == BoundKind.EXTENDS ? wtype.type : wtype;
        }
        return makeTypeTreeInner(diagPos, t, makeIntf);
    }
    private JCExpression makeTypeTreeInner(DiagnosticPosition diagPos, Type t, boolean makeIntf) {
        while (t instanceof CapturedType)
            t = ((CapturedType) t).wildcard;
        switch (t.tag) {
            case TypeTags.CLASS: {
                JCExpression texp = null;
                
                if (makeIntf && types.isCompoundClass(t.tsym)) {
                    texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString() + interfaceSuffix);
                } else {
                    if (t.isCompound()) {
                        t = syms.objectType;
                    }
                    texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString());
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
        Type returnType = mth.getReturnType();
        if (isBound) {
            VarMorphInfo vmi = typeMorpher.varMorphInfo(mth);
            returnType = vmi.getLocationType();
        }
        return makeTypeTree(diagPos, returnType);
    }

    JCExpression makeTypeCast(DiagnosticPosition diagPos, Type clazztype, Type exprtype, JCExpression translatedExpr) {
        if (types.isSameType(clazztype, exprtype)) {
            return translatedExpr;
        } else {
            Type castType = clazztype;
            if (castType.isPrimitive() && !exprtype.isPrimitive()) {
                castType = types.boxedClass(castType).type;
            }
            JCTree clazz = makeTypeTree(diagPos, castType, true);
            return make.at(diagPos).TypeCast(clazz, translatedExpr);
        }
    }
 
   /**
    * Make a receiver parameter. 
    * Its type is that of the corresponding interface and it is a final parameter.
    * */
    JCVariableDecl makeReceiverParam(JFXClassDeclaration cDecl) {
        return make.VarDef(
                make.Modifiers(Flags.FINAL | Flags.PARAMETER), 
                defs.receiverName, 
                make.Ident(interfaceName(cDecl)), 
                null);
    }
    
    JCExpression makeDefaultValue(DiagnosticPosition diagPos, TypeMorphInfo tmi) {
        return tmi.getTypeKind() == TYPE_KIND_SEQUENCE ? 
                makeEmptySequenceCreator(diagPos, tmi.getElementType()) : 
            tmi.getRealType() == syms.javafx_StringType ? 
                make.Literal("") : 
            tmi.getRealType() == syms.javafx_DurationType ?
                makeTimeDefaultValue(diagPos) :
                makeLit(diagPos, tmi.getRealType(), tmi.getDefaultValue());
    }

    JCExpression makeTimeDefaultValue(DiagnosticPosition diagPos) {
        return makeDurationLiteral(diagPos, makeLit(diagPos, syms.javafx_NumberType, 0));
    }

    /** Make an attributed tree representing a literal. This will be
     *  a Literal node.
     *  @param type       The literal's type.
     *  @param value      The literal's value.
     */
    JCExpression makeLit(DiagnosticPosition diagPos, Type type, Object value) {
        int tag = value==null? TypeTags.BOT : type.tag;
        return make.at(diagPos).Literal(tag, value).setType(type.constType(value));
    }

    JCExpression makeLocationLocalVariable(TypeMorphInfo tmi, 
                                  DiagnosticPosition diagPos,
                                  List<JCExpression> makeArgs) {
        return makeLocationVariable(tmi, diagPos, makeArgs, defs.makeMethodName);
    }

    JCExpression makeLocationAttributeVariable(TypeMorphInfo tmi, 
                                  DiagnosticPosition diagPos) {
        return makeLocationVariable(tmi, diagPos, List.<JCExpression>nil(), defs.makeMethodName);
    }

    JCExpression makeLocationVariable(TypeMorphInfo tmi, 
                                  DiagnosticPosition diagPos,
                                  List<JCExpression> makeArgs,
                                  Name makeMethod) {
        Name locName = typeMorpher.variableNCT[tmi.getTypeKind()].name;
        JCExpression locationTypeExp = makeIdentifier(diagPos, locName);
        JCFieldAccess makeSelect = make.at(diagPos).Select(locationTypeExp, makeMethod);
        List<JCExpression> typeArgs = null;
        switch (tmi.getTypeKind()) {
            case TYPE_KIND_OBJECT:
                typeArgs = List.of(makeTypeTree(diagPos, tmi.getRealType(), true));
                break;
            case TYPE_KIND_SEQUENCE:
                typeArgs = List.of(makeTypeTree(diagPos, tmi.getElementType(), true));
                makeArgs = makeArgs.prepend(makeElementClassObject(diagPos, tmi.getElementType()));
                break;
        }
        return make.at(diagPos).Apply(typeArgs, makeSelect, makeArgs);
    }

    JCExpression makeConstantLocation(DiagnosticPosition diagPos, Type type, JCExpression expr) {
        TypeMorphInfo tmi = typeMorpher.typeMorphInfo(type);
        List<JCExpression> makeArgs = List.of(expr);
        JCExpression locationTypeExp = makeTypeTree( diagPos,tmi.getConstantLocationType(), true);
        JCFieldAccess makeSelect = make.at(diagPos).Select(locationTypeExp, defs.makeMethodName);
        List<JCExpression> typeArgs = null;
        //TODO: hmmm, compare this to makeLocationVariable, seems for Object it should be tmi.getRealType()
        if (tmi.getTypeKind() == TYPE_KIND_OBJECT || tmi.getTypeKind() == TYPE_KIND_SEQUENCE) {
            typeArgs = List.of(makeTypeTree( diagPos,tmi.getElementType(), true));
        }
        return make.at(diagPos).Apply(typeArgs, makeSelect, makeArgs);
    }

    JCExpression makeUnboundLocation(DiagnosticPosition diagPos, TypeMorphInfo tmi, JCExpression expr) {
        List<JCExpression> makeArgs = List.of(expr);
        return makeLocationLocalVariable(tmi, diagPos, makeArgs);
    }

    protected JCExpression makeUnboundLocation(DiagnosticPosition diagPos, Type type, JCExpression expr) {
        return makeUnboundLocation(diagPos, typeMorpher.typeMorphInfo(type), expr);
    }

    protected JCExpression runtime(DiagnosticPosition diagPos, String cString, String methString) {
        return runtime(diagPos, cString, methString, null, List.<JCExpression>nil());
    }

    protected JCExpression runtime(DiagnosticPosition diagPos, String cString, String methString, List<JCExpression> args) {
        return runtime(diagPos, cString, methString, null, args);
    }

    protected JCExpression runtime(DiagnosticPosition diagPos, String cString, String methString, List<JCExpression> typeArgs, List<JCExpression> args) {
        JCExpression meth = make.at(diagPos).Select(makeQualifiedTree(diagPos, cString), names.fromString(methString));
        return make.at(diagPos).Apply(typeArgs, meth, args);
    }

    protected JCExpression runtime(DiagnosticPosition diagPos, String cString, String methString, ListBuffer<JCExpression> args) {
        return runtime(diagPos, cString, methString, null, args.toList());
    }

    JCMethodInvocation callExpression(DiagnosticPosition diagPos, JCExpression receiver, Name methodName) {
        return callExpression(diagPos, receiver, methodName, null);
    }

    JCMethodInvocation callExpression(DiagnosticPosition diagPos, JCExpression receiver, Name methodName, Object args) {
        JCExpression expr = null;
        if (receiver == null) {
            expr = make.at(diagPos).Ident(methodName);
        } else {
            expr = make.at(diagPos).Select(receiver, methodName);
        }
        return make.at(diagPos).Apply(List.<JCExpression>nil(), expr, (args == null) ? List.<JCExpression>nil() : (args instanceof List) ? (List<JCExpression>) args : (args instanceof ListBuffer) ? ((ListBuffer<JCExpression>) args).toList() : (args instanceof JCExpression) ? List.<JCExpression>of((JCExpression) args) : null);
    }

    JCMethodInvocation callExpression(DiagnosticPosition diagPos, JCExpression receiver, String method) {
        return callExpression(diagPos, receiver, method, null);
    }

    JCMethodInvocation callExpression(DiagnosticPosition diagPos, JCExpression receiver, String method, Object args) {
        return callExpression(diagPos, receiver, names.fromString(method), args);
    }

    JCStatement callStatement(DiagnosticPosition diagPos, JCExpression receiver, Name methodName) {
        return callStatement(diagPos, receiver, methodName, null);
    }

    JCStatement callStatement(DiagnosticPosition diagPos, JCExpression receiver, Name methodName, Object args) {
        return make.at(diagPos).Exec(callExpression(diagPos, receiver, methodName, args));
    }

    JCStatement callStatement(DiagnosticPosition diagPos, JCExpression receiver, String method) {
        return callStatement(diagPos, receiver, method, null);
    }

    JCStatement callStatement(DiagnosticPosition diagPos, JCExpression receiver, String method, Object args) {
        return make.at(diagPos).Exec(callExpression(diagPos, receiver, method, args));
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
    
    
    Name attributeFieldName(Symbol sym) {
        return names.fromString(attributeNameString(sym, ""));
    }

    Name attributeName(Symbol sym, String prefix) {
        return names.fromString(attributeNameString(sym, prefix));
    }

    String attributeNameString(Symbol sym, String prefix) {
        String sname = sym.name.toString();
        Symbol owner = sym.owner;
        if ((sym.flags() & (PRIVATE|STATIC)) == PRIVATE
                && types.isCompoundClass(owner)) {
             sname = owner.toString().replace('.', '$') + '$' + sname;
        }
        return prefix + sname;
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
                        sb.append(defs.escapeTypeChar);
                        // Double separator between type names.
                        sb.append(defs.escapeTypeChar);
                    }
                    counter++;
                }
            }
        }
        return sb.toString();
    }

    JCExpression makeEmptySequenceCreator(DiagnosticPosition diagPos, Type elemType) {
        JCExpression meth = makeQualifiedTree(diagPos, sequencesEmptyString);
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append(makeElementClassObject(diagPos, elemType));
        List<JCExpression> typeArgs = List.of(makeTypeTree(diagPos, elemType, true));
        return make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }

    private String escapeTypeName(Type type) {
        return type.toString().replace(defs.typeCharToEscape, defs.escapeTypeChar);
    }

    /**
     * Given "Foo" type, return "Foo.class" expression
     * @param diagPos
     * @param elemType
     * @return expression representing the class
     */
    JCExpression makeElementClassObject(DiagnosticPosition diagPos, Type elemType) {
        return make.at(diagPos).Select(makeTypeTree( diagPos,syms.boxIfNeeded(elemType), true), names._class);
    }

    protected abstract String getSyntheticPrefix();
    
    Name getSyntheticName(String kind) {
        return Name.fromString(names, getSyntheticPrefix() + syntheticNameCounter++ + kind);
    }

    public Name indexVarName(JFXForExpressionInClause clause) {
        return indexVarName(clause.getVar().getName());
    }
    public Name indexVarName(JFXIdent var) {
        return indexVarName(var.getName());
    }
    private Name indexVarName(Name name) {
        return names.fromString("$indexof$" + name.toString());
    }

    /**
     * For an attribute "attr" make an access to it via the receiver and getter
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, Symbol attribSym) {
       return callExpression(diagPos,
                make.Ident(defs.receiverName),
                attributeNameString(attribSym, attributeGetMethodNamePrefix));
   }

    BlockExprJCBlockExpression makeBlockExpression(DiagnosticPosition diagPos, List<JCStatement> stmts, JCExpression value) {
        BlockExprJCBlockExpression bexpr = new BlockExprJCBlockExpression(0L, stmts, value);
        bexpr.pos = (diagPos == null ? Position.NOPOS : diagPos.getStartPosition());
        return bexpr;
    }

    BlockExprJCBlockExpression makeBlockExpression(DiagnosticPosition diagPos, ListBuffer<JCStatement> stmts, JCExpression value) {
        return makeBlockExpression(diagPos, stmts.toList(), value);
    }

    JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, String rootName, Type type, JCExpression value) {
        return make.at(diagPos).VarDef(make.at(diagPos).Modifiers(Flags.FINAL), getSyntheticName(rootName), makeTypeTree(diagPos, type), value);
    }

    JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, Type type, JCExpression value) {
        return makeTmpVar(diagPos, "tmp", type, value);
    }

    protected JCModifiers addAccessAnnotationModifiers(DiagnosticPosition diagPos, long flags, JCModifiers mods) {
        make.at(diagPos);
        JCModifiers ret = mods;
        ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
        if ((flags & Flags.PUBLIC) != 0) {
            annotations.append(make.Annotation(makeIdentifier(diagPos, syms.publicAnnotationStr), List.<JCExpression>nil()));
        }
        else if ((flags & Flags.PRIVATE) != 0) {
            annotations.append(make.Annotation(makeIdentifier(diagPos, syms.privateAnnotationStr), List.<JCExpression>nil()));
        }
        else if ((flags & Flags.PROTECTED) != 0) {        
            annotations.append(make.Annotation(makeIdentifier(diagPos, syms.protectedAnnotationStr), List.<JCExpression>nil()));
        }                

        if ((flags & Flags.STATIC) != 0) {
            annotations.append(make.Annotation(makeIdentifier(diagPos, syms.staticAnnotationStr), List.<JCExpression>nil()));
        }

        if (annotations.nonEmpty()) {
            ret = make.Modifiers(mods.flags, annotations.toList());
        }
        return ret;
    }
    
    protected JCModifiers addInheritedAnnotationModifiers(DiagnosticPosition diagPos, long flags, JCModifiers mods) {
        return make.Modifiers(mods.flags, List.of(make.Annotation(makeIdentifier(diagPos, syms.inheritedAnnotationStr), List.<JCExpression>nil())));
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

    // convert time literal to a javafx.lang.Duration object literal
    //TODO: should be eliminated in favor of makeDurationLiteral -- see visitTimeLiteral
    protected JFXFunctionInvocation timeLiteralToDuration(JFXTimeLiteral tree) {
        JFXSelect clsname = (JFXSelect) fxmake.at(tree.pos()).Type(syms.javafx_DurationType);
        clsname.sym = syms.javafx_DurationType.tsym;
        Name valueOf = names.fromString("valueOf");
        JFXSelect meth = fxmake.at(tree.pos).Select(clsname, valueOf);
        meth.sym = syms.javafx_DurationType.tsym.members().lookup(valueOf).sym;
        meth.type = meth.sym.type;
        List<JFXExpression> args = List.<JFXExpression>of(tree.value);
        JFXFunctionInvocation apply = fxmake.at(tree.pos).Apply(List.<JFXExpression>nil(), meth, args);
        apply.type = clsname.type;
        return apply;
    }

    protected JCExpression makeDurationLiteral(DiagnosticPosition diagPos, JCExpression value) {
        JCExpression clsname = makeTypeTree(diagPos, syms.javafx_DurationType);
        Name valueOf = names.fromString("valueOf");
        JCExpression meth = make.at(diagPos).Select(clsname, valueOf);
        List<JCExpression> args = List.of(value);
        JCExpression apply = make.at(diagPos).Apply(List.<JCExpression>nil(), meth, args);
        return apply;
    }
}
