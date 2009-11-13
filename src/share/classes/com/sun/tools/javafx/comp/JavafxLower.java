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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree.Cardinality;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.tree.JFXExpression;

import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;

import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

import java.util.Map;
import java.util.HashMap;

/**
 * Normalize tree before translation. This includes adding explicit type conversions
 * (e.g. for autoboxing, sequence conversions, etc.) and rewriting unary/assignop operations
 * as binary operations.
 * 
 * @author Maurizio Cimadamore
 */
public class JavafxLower implements JavafxVisitor {
    protected static final Context.Key<JavafxLower> convertTypesKey =
            new Context.Key<JavafxLower>();

    private JavafxPreTranslationSupport preTrans;
    private JavafxTypes types;
    private JavafxResolve rs;
    private JavafxSymtab syms;
    private JavafxTreeMaker m;
    private JavafxDefs defs;
    private Type pt;
    private Map<JFXForExpressionInClause, JFXForExpressionInClause> forClauseMap; //TODO this should be refactord into a common translation support
    private JavafxEnv<JavafxAttrContext> env;
    private JFXTree enclFunc;
    private JFXTree result;
    private Name.Table names;
    private Symbol currentClass;
    private int varCount;

    public static JavafxLower instance(Context context) {
        JavafxLower instance = context.get(convertTypesKey);
        if (instance == null)
            instance = new JavafxLower(context);
        return instance;
    }

    JavafxLower(Context context) {
        context.put(convertTypesKey, this);
        preTrans = JavafxPreTranslationSupport.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        m = JavafxTreeMaker.instance(context);
        rs = JavafxResolve.instance(context);
        names = Name.Table.instance(context);
        defs = JavafxDefs.instance(context);
        forClauseMap = new HashMap<JFXForExpressionInClause, JFXForExpressionInClause>();
    }

    public JFXTree lower(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.env = attrEnv;
        attrEnv.toplevel = lower(attrEnv.toplevel);
        //System.out.println(result);
        return result;
    }

    public JFXExpression lowerExpr(JFXExpression tree, Type pt) {
        Type prevPt = this.pt;
        try {
            this.pt = pt;
            if (tree != null) {
                tree.accept(this);
                return convertTree((JFXExpression)result, this.pt);
            }
            else
                return null;
        }
        finally {
            this.pt = prevPt;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends JFXTree> T lower(T tree) {
        Type prevPt = this.pt;
        try {
            this.pt = Type.noType;
            if (tree != null) {
                tree.accept(this);
                return (T)result;
            }
            else
                return null;
        }
        finally {
            this.pt = prevPt;
        }
    }

    public <T extends JFXTree> List<T> lower(List<T> trees) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(lower(tree));
        }
        return buf.toList();
    }

    public List<JFXExpression> lowerExprs(List<? extends JFXExpression> trees, List<Type> pts) {
        ListBuffer<JFXExpression> buf = ListBuffer.lb();
        for (JFXExpression tree : trees) {
            buf.append(lowerExpr(tree, pts.head));
            pts = pts.tail;
        }
        return buf.toList();
    }

    JFXExpression convertTree(JFXExpression tree, Type type) {
        boolean needSeqConversion = needSequenceConversion(tree, type);
        if (needSeqConversion) {
            type = types.elementType(type);
        }
        return tree = needSeqConversion ?
            toSequence(tree, type) :
            makeCastIfNeeded(tree, type);
    }

    private boolean needSequenceConversion(JFXExpression tree, Type type) {
        return (types.isSequence(type) &&
                ((!types.isSequence(tree.type) &&
                !types.isArray(tree.type)) ||
                isNull(tree)));
    }

    /**
     * It is necessary to add an extra cast if either source type or target type
     * is a boxed Java type - this is required because we might want to go from
     * java.Lang.Long to int and vice-versa
     */
    private boolean needNumericBoxConversion(JFXExpression tree, Type type) {
        boolean sourceIsPrimitive = tree.type.isPrimitive();
        boolean targetIsPrimitive = type.isPrimitive();
        Type unboxedSource = types.unboxedType(tree.type);
        Type unboxedTarget = types.unboxedType(type);
        return (sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && !types.isSameType(unboxedTarget, tree.type)) ||
                (targetIsPrimitive && !sourceIsPrimitive && unboxedSource != Type.noType && !types.isSameType(unboxedSource, type)) ||
                (!sourceIsPrimitive && !targetIsPrimitive && unboxedTarget != Type.noType && unboxedSource!= Type.noType && !types.isSameType(type, tree.type));
    }

    private boolean isNull(JFXTree tree) {
        return (tree.getFXTag() == JavafxTag.LITERAL &&
                ((JFXLiteral)tree).value == null);
    }

    private JFXExpression toSequence(JFXExpression tree, Type type) {
        JFXExpression expr = isNull(tree) ? 
            m.at(tree.pos).EmptySequence() :
            m.at(tree.pos).ExplicitSequence(List.of(makeCastIfNeeded(tree, type)));
        expr.type = types.sequenceType(type);
        return expr;
    }

    private JFXExpression makeCastIfNeeded(JFXExpression tree, Type type) {
        if (type == Type.noType ||
                type == null ||
                type.isErroneous() ||
                type == syms.voidType ||
                tree.type == syms.voidType ||
                tree.type == syms.unreachableType ||
                type == syms.unreachableType)
            return tree;
        else {
            tree = makeNumericBoxConversionIfNeeded(tree, type);
            return (!types.isSubtypeUnchecked(tree.type, type) ||
                    types.isSameType(tree.type, syms.javafx_EmptySequenceType) ||
                    (tree.type.isPrimitive() &&
                    type.isPrimitive() && !types.isSameType(tree.type, type))) ?
                makeCast(tree, type) :
                tree;
        }
    }

    private JFXExpression makeNumericBoxConversionIfNeeded(JFXExpression tree, Type type) {
        if (needNumericBoxConversion(tree, type)) {
           //either tree.type or type is primitive!
           if (tree.type.isPrimitive() && !type.isPrimitive()) {
               return makeCast(tree, types.unboxedType(type));
           }
           else if (type.isPrimitive() && !tree.type.isPrimitive()) {
               return makeCast(tree, types.unboxedType(tree.type));
           }
           else { //both are boxed types
               return makeCast(makeCast(tree, types.unboxedType(tree.type)), types.unboxedType(type));
           }
        }
        else {
            return tree;
        }
    }

    private JFXExpression makeCast(JFXExpression tree, Type type) {
        JFXExpression typeTree = m.TypeClass(m.Type(types.elementTypeOrType(type)),
                types.isSequence(type) ? Cardinality.ANY : Cardinality.SINGLETON);
        typeTree.type = type;
        JFXExpression expr = m.at(tree.pos).TypeCast(typeTree, tree);
        expr.type = type;
        return expr;
    }

    private Name tempName(String label) {
        return names.fromString("$" + label + "$" + varCount++);
    }

    private JFXVar makeTmpVar(DiagnosticPosition diagPos, String name, JFXExpression init, Type type) {
        VarSymbol vsym = new VarSymbol(0L, tempName(name), type,         preTrans.makeDummyMethodSymbol(currentClass));
        return makeTmpVar(diagPos, vsym, init);
    }

    private JFXVar makeTmpVar(DiagnosticPosition diagPos, VarSymbol vSym, JFXExpression init) {
        JFXModifiers mod = m.at(diagPos).Modifiers(vSym.flags());
        JFXType fxType = m.at(diagPos).TypeClass(m.at(diagPos).Type(vSym.type), Cardinality.SINGLETON);
        JFXVar v = m.at(diagPos).Var(vSym.name, fxType, mod, init, JavafxBindStatus.UNBOUND, null, null);
        v.sym = vSym;
        v.type = vSym.type;
        return v;
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = lower(tree.lhs);
        JFXExpression rhs = lowerExpr(tree.rhs, tree.lhs.type);
        result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        result = visitNumericAssignop(tree, types.isSameType(tree.lhs.type, syms.javafx_DurationType));
    }
    //where
    private JFXExpression visitNumericAssignop(JFXAssignOp tree, boolean isDurationOperation) {

        JavafxTag opTag = tree.getNormalOperatorFXTag();
        ListBuffer<JFXExpression> stats = ListBuffer.lb();

        //if the assignop operand is an indexed expression of the kind a.x[i]
        //then we need to cache the index value (not to recumpute it twice).

        JFXExpression lhs = tree.lhs;
        JFXIdent index = null;
        
        if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
            JFXSequenceIndexed indexed = (JFXSequenceIndexed)tree.lhs;
            JFXVar varDef = makeTmpVar(tree.pos(), "index", indexed.getIndex(), indexed.getIndex().type);
            index = m.at(tree.pos).Ident(varDef.sym);
            index.sym = varDef.sym;
            index.type = varDef.type;
            stats.append(varDef);
            lhs = indexed.getSequence();
        }

        //if the assignop operand is a select of the kind a.x
        //then we need to cache the selected part (a), so that
        //it won't be recomputed twice.
        //
        // var $expr$ = a;

        JFXIdent selector = null;

        if (lhs.getFXTag() == JavafxTag.SELECT) {
            JFXExpression selected = ((JFXSelect)lhs).selected;
            JFXVar varDef = makeTmpVar(tree.pos(), "expr", selected, selected.type);
            selector = m.at(tree.pos).Ident(varDef.sym);
            selector.sym = varDef.sym;
            selector.type = varDef.type;
            stats.append(varDef);
        }

        JFXExpression varRef = lhs;

        //create a reference to the cached var. The translated expression
        //depends on whether the operand is a select or not:
        //
        //(SELECT)  $expr$.x;
        //(IDENT)   x;

        if (selector != null) {
            VarSymbol vsym = (VarSymbol)JavafxTreeInfo.symbol(lhs);
            varRef = m.at(tree.pos).Select(selector, vsym);
            ((JFXSelect)varRef).sym = vsym;
        }

        if (index != null) {
            varRef = m.at(tree.pos).SequenceIndexed(varRef, index).setType(tree.lhs.type);
        }

        //Generate the binary expression this assignop translates to
        JFXExpression op = null;

        if (isDurationOperation) {
            //duration assignop
            //
            //(SELECT) $expr$.x = $expr$.x.[add/sub/mul/div](lhs);
            //(IDENT)  x = x.[add/sub/mul/div](lhs);
            JFXSelect meth = (JFXSelect)m.at(tree.pos).Select(varRef, tree.operator.name);
            meth.setType(tree.operator.type);
            meth.sym = tree.operator;
            op = m.at(tree.pos).Apply(List.<JFXExpression>nil(), meth, List.of(tree.rhs));
            op.setType(tree.type);
        }
        else {
            //numeric assignop
            //
            //(SELECT) $expr$.x = $expr$.x [+|-|*|/] lhs;
            //(IDENT)  x = $expr$.x [+|-|*|/] lhs;
            op = m.at(tree.pos).Binary(opTag, varRef, tree.rhs);
            ((JFXBinary)op).operator = tree.operator;
            op.setType(tree.operator.type.asMethodType().getReturnType());
        }
        JFXExpression assignOpStat = (JFXExpression)m.at(tree.pos).Assign(varRef, op).setType(op.type);

        JFXBlock block = (JFXBlock)m.at(tree.pos).Block(0L, stats.toList(), assignOpStat).setType(op.type);
        return lowerExpr(block, Type.noType);
    }

    @Override
    public void visitBinary(JFXBinary tree) {
        boolean isDurationBinaryExpr = tree.operator == null;
        boolean isEqualExpr = (tree.getFXTag() == JavafxTag.EQ ||
                tree.getFXTag() == JavafxTag.NE);
        boolean isSequenceOp = types.isSequence(tree.lhs.type) ||
                types.isSequence(tree.rhs.type);
        boolean isBoxedOp = (tree.lhs.type.isPrimitive() && !tree.rhs.type.isPrimitive()) ||
                (tree.rhs.type.isPrimitive() && !tree.lhs.type.isPrimitive());
        Type lhsType = tree.lhs.type;
        Type rhsType = tree.rhs.type;
        if (!isDurationBinaryExpr) {
            lhsType = isSequenceOp && isEqualExpr ?
                types.sequenceType(tree.operator.type.getParameterTypes().head) :
                tree.operator.type.getParameterTypes().head;
            rhsType = isSequenceOp && isEqualExpr ?
                types.sequenceType(tree.operator.type.getParameterTypes().tail.head) :
                tree.operator.type.getParameterTypes().tail.head;
        }
        else {
            if (!types.isSameType(tree.lhs.type, syms.javafx_DurationType) &&
                    tree.getFXTag() == JavafxTag.MUL) {
                rhsType = syms.javafx_DurationType;
            }
            else {
                lhsType = syms.javafx_DurationType;
            }
        }
        JFXExpression lhs = isEqualExpr && isBoxedOp && !isSequenceOp ?
            lower(tree.lhs) :
            lowerExpr(tree.lhs, lhsType);
        JFXExpression rhs = isEqualExpr && isBoxedOp && !isSequenceOp ?
            lower(tree.rhs) :
            lowerExpr(tree.rhs, rhsType);
        JFXBinary res = m.at(tree.pos).Binary(tree.getFXTag(), lhs, rhs);
        res.operator = tree.operator;
        result = res.setType(tree.type);
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        JFXExpression whereExpr = lower(that.whereExpr);
        JFXExpression seqExpr = null;
        if (types.isSequence(that.seqExpr.type)) {
            seqExpr = lowerExpr(that.seqExpr, types.sequenceType(that.var.type));
        }
        else {//sure that this is what we want?
            seqExpr = lower(that.seqExpr);
        }
        JFXForExpressionInClause res = m.at(that.pos).InClause(that.getVar(), seqExpr, whereExpr);
        res.setIndexUsed(that.getIndexUsed());
        forClauseMap.put(that, res);
        result = res.setType(that.type);
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        JFXTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            JFXBlock body  = (JFXBlock)lowerExpr(tree.getBodyExpression(), tree.type != null ? tree.type.getReturnType() : Type.noType);
            JFXFunctionDefinition res = m.FunctionDefinition(tree.mods, tree.name, tree.getJFXReturnType(), tree.getParams(), body);
            res.operation.definition = res;
            res.sym = tree.sym;
            result = res.setType(tree.type);
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression meth = lower(tree.meth);
        List<Type> paramTypes = tree.meth.type.getParameterTypes();
        Symbol sym = JavafxTreeInfo.symbolFor(tree.meth);
        boolean pointer_Make = (sym.flags_field & JavafxFlags.FUNC_POINTER_MAKE) != 0;
        boolean builtins_isInitialized = (sym.flags_field & JavafxFlags.FUNC_IS_INITIALIZED) != 0;
        if (sym instanceof MethodSymbol &&
                ((MethodSymbol)sym).isVarArgs()) {
            Type varargType = paramTypes.reverse().head;
            paramTypes = paramTypes.reverse().tail.reverse(); //remove last formal
            while (paramTypes.size() < tree.args.size()) {
                paramTypes = paramTypes.append(types.elemtype(varargType));
            }
        }
        List<JFXExpression> args = null;
        if (pointer_Make || builtins_isInitialized) {
            JFXExpression varExpr = lower(tree.args.head);
            ListBuffer<JFXExpression> syntheticArgs = ListBuffer.lb();
            List<Type> argTypes = List.of(syms.javafx_FXObjectType, syms.intType);
            syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, JFXVarRef.RefKind.INST).setType(syms.javafx_FXObjectType));
            syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, JFXVarRef.RefKind.VARNUM).setType(syms.intType));
            if (pointer_Make) {
                argTypes = argTypes.append(syms.classType);
            }

            Symbol methSym = rs.resolveQualifiedMethod(tree.pos(),
                    env,
                    pointer_Make ? syms.javafx_PointerType : syms.javafx_AutoImportRuntimeType,
                    pointer_Make ? defs.make_PointerMethodName : defs.isInitialized_MethodName,
                    rs.newMethTemplate(argTypes, List.<Type>nil()));
            methSym.flags_field = sym.flags();
            JavafxTreeInfo.setSymbol(meth, methSym);
            meth.type = methSym.type;
            args = syntheticArgs.toList();
        }
        else {
            args = lowerExprs(tree.args, paramTypes);
        }
         
        result = m.Apply(tree.typeargs, meth, args);
        result.type = tree.type;
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            tree.bodyExpression = (JFXBlock)lowerExpr(tree.bodyExpression, tree.type.getReturnType());
            result = tree;
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        JFXExpression cond = lowerExpr(tree.cond, syms.booleanType);
        JFXExpression truePart = lowerExpr(tree.truepart, tree.type);
        JFXExpression falsePart = lowerExpr(tree.falsepart, tree.type);
        result = m.Conditional(cond, truePart, falsePart).setType(tree.type);
    }

    @Override
    public void visitIndexof(JFXIndexof that) {
        JFXIndexof res = m.at(that.pos).Indexof(that.fname);
        res.clause = that.clause;
        result = res.setType(that.type);
    }

    @Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        Type typeToCheck = needSequenceConversion(tree.getExpression(), tree.clazz.type) ?
            types.sequenceType(tree.getExpression().type) :
            tree.getExpression().type;
        JFXExpression expr = lowerExpr(tree.getExpression(), typeToCheck);
        result = m.at(tree.pos).TypeTest(expr, tree.clazz).setType(tree.type);
    }

    @Override
    public void visitInterpolateValue(JFXInterpolateValue that) {
        JFXExpression pointerType = m.at(that.pos).Type(syms.javafx_PointerType).setType(syms.javafx_PointerType);
        Symbol pointerMakeSym = rs.resolveQualifiedMethod(that.pos(),
                env, syms.javafx_PointerType,
                defs.Pointer_make.methodName,
                rs.newMethTemplate(List.of(syms.objectType),
                List.<Type>nil()));
        pointerMakeSym.flags_field |= JavafxFlags.FUNC_POINTER_MAKE;
        JFXSelect pointerMake = (JFXSelect)m.at(that.pos).Select(pointerType, pointerMakeSym);
        pointerMake.sym = pointerMakeSym;
        JFXExpression pointerCall = m.at(that.pos).Apply(List.<JFXExpression>nil(),
                pointerMake,
                List.of(that.attribute)).setType(pointerMakeSym.type.getReturnType());
        ListBuffer<JFXTree> parts = ListBuffer.lb();
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyValueType, defs.value_InterpolateMethodName, that.funcValue));
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyValueType, defs.target_InterpolateMethodName, pointerCall));
        if (that.interpolation != null) {
            parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyValueType, defs.interpolate_InterpolateMethodName, that.interpolation));
        }
        JFXExpression res = m.at(that.pos).ObjectLiteral(m.at(that.pos).Type(syms.javafx_KeyValueType), parts.toList()).setType(syms.javafx_KeyValueType);
        result = lower(res);
    }
    //where
    private JFXObjectLiteralPart makeObjectLiteralPart(DiagnosticPosition pos, Type site, Name varName, JFXExpression value) {
        JFXObjectLiteralPart part = m.at(pos).ObjectLiteralPart(defs.value_InterpolateMethodName, value, JavafxBindStatus.UNBOUND);
        part.setType(value.type);
        part.sym = rs.findIdentInType(env, site, varName, Kinds.VAR);
        return part;
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
        ListBuffer<JFXTree> parts = ListBuffer.lb();
        JFXExpression keyValues = m.at(that.pos).ExplicitSequence(that.values).setType(types.sequenceType(syms.javafx_KeyValueType));
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyFrameType, defs.time_KeyFrameMethodName, that.start));
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyFrameType, defs.values_KeyFrameMethodName, keyValues));
        JFXExpression res = m.at(that.pos).ObjectLiteral(m.at(that.pos).Type(syms.javafx_KeyValueType), parts.toList()).setType(syms.javafx_KeyFrameType);
        result = lower(res);
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        result = tree;
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JFXExpression expr = lowerExpr(tree.getExpression(), tree.sym.type);
        JFXObjectLiteralPart res = m.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getBindStatus());
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        JFXExpression init = lowerExpr(tree.getInitializer(), tree.type);
        JFXOnReplace onReplace = lower(tree.getOnReplace());
        JFXOnReplace onInvalidate = lower(tree.getOnInvalidate());
        JFXOverrideClassVar res = m.at(tree.pos).OverrideClassVar(tree.name, tree.mods, tree.getId(), init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    @Override
    public void visitReturn(JFXReturn tree) {
        Type typeToCheck = enclFunc.type != null ?
            enclFunc.type.getReturnType() :
            syms.objectType; //nedded because run function has null type
        JFXExpression retExpr = lowerExpr(tree.getExpression(), typeToCheck);
        result = m.at(tree.pos).Return(retExpr).setType(tree.type);
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete that) {
        JFXExpression seq = lower(that.getSequence());
        JFXExpression el = that.getElement();
        if (that.getElement() != null) {
            Type typeToCheck = types.isArrayOrSequenceType(that.getElement().type) ?
                    that.getSequence().type :
                    types.elementType(that.getSequence().type);
            el = lowerExpr(that.getElement(), typeToCheck);
        }
        result = m.at(that.pos).SequenceDelete(seq, el).setType(that.type);
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
        result = that;
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        ListBuffer<JFXExpression> buf = ListBuffer.lb();
        for (JFXExpression item : that.getItems()) {
            Type typeToCheck = types.isArrayOrSequenceType(item.type) ?
                that.type :
                types.elementType(that.type);
            buf.append(lowerExpr(item, typeToCheck));
        }
        result = m.at(that.pos).ExplicitSequence(buf.toList()).setType(that.type);
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        JFXExpression index = lowerExpr(that.getIndex(), syms.intType);
        JFXExpression seq = lower(that.getSequence());
        result = m.at(that.pos).SequenceIndexed(seq, index).setType(that.type);
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert that) {
        JFXExpression seq = lower(that.getSequence());
        Type typeToCheck = types.isArrayOrSequenceType(that.getElement().type) ?
                that.getSequence().type :
                types.elementType(that.getSequence().type);
        JFXExpression el = lowerExpr(that.getElement(), typeToCheck);
        JFXExpression pos = lowerExpr(that.getPosition(), syms.intType);
        result = m.at(that.pos).SequenceInsert(seq, el, pos, that.shouldInsertAfter()).setType(that.type);
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        JFXExpression lower = lowerExpr(that.getLower(), types.elementType(that.type));
        JFXExpression upper = lowerExpr(that.getUpper(), types.elementType(that.type));
        JFXExpression step = lowerExpr(that.getStepOrNull(), types.elementType(that.type));
        JFXSequenceRange res = m.at(that.pos).RangeSequence(lower, upper, step, that.isExclusive());
        res.boundSizeVar = that.boundSizeVar;
        result = res.setType(that.type);
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice that) {
        JFXExpression seq = lower(that.getSequence());
        JFXExpression start = lowerExpr(that.getFirstIndex(), syms.intType);
        JFXExpression end = lowerExpr(that.getLastIndex(), syms.intType);
        result = m.at(that.pos).SequenceSlice(seq, start, end, that.getEndKind()).setType(that.type);
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = lower(tree.parts);
        result = m.at(tree.pos).StringExpression(parts, tree.translationKey).setType(tree.type);
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        if (tree.getFXTag().isIncDec()) {
            result = visitNumericUnary(tree);
        }
        else {
            JFXExpression arg = tree.getFXTag().isIncDec() ?
                lowerExpr(tree.getExpression(), tree.getOperator().type.getParameterTypes().head) :
                lower(tree.getExpression());
            JFXUnary res = m.at(tree.pos).Unary(tree.getFXTag(), arg);
            res.operator = tree.operator;
            res.type = tree.type;
            result = res;
        }
    }

    private JFXExpression visitNumericUnary(JFXUnary tree) {
        boolean postFix = isPostfix(tree.getFXTag());
        JavafxTag opTag = unaryToBinaryOpTag(tree.getFXTag());
        Type opType = types.unboxedTypeOrType(tree.getExpression().type);
        if (types.isSameType(opType, syms.charType)) {
            opType = syms.intType;
        }
        ListBuffer<JFXExpression> stats = ListBuffer.lb();

        //if the unary operand is an indexed expression of the kind a.x[i]
        //then we need to cache the index value (not to recumpute it twice).

        JFXExpression expr = tree.getExpression();
        JFXIdent index = null;

        if (tree.getExpression().getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
            JFXSequenceIndexed indexed = (JFXSequenceIndexed)tree.getExpression();
            JFXVar varDef = makeTmpVar(tree.pos(), "index", indexed.getIndex(), indexed.getIndex().type);
            index = m.at(tree.pos).Ident(varDef.sym);
            index.sym = varDef.sym;
            index.type = varDef.type;
            stats.append(varDef);
            expr = indexed.getSequence();
        }

        //if the unary operand is a select of the kind a.x
        //then we need to cache the selected part (a), so that
        //it won't be recomputed twice.
        //
        // var $expr$ = a;
        JFXIdent selector = null;

        if (expr.getFXTag() == JavafxTag.SELECT) {
            JFXExpression selected = ((JFXSelect)expr).selected;
            JFXVar varDef = makeTmpVar(tree.pos(), "expr", selected, selected.type);
            selector = m.at(tree.pos).Ident(varDef.sym);
            selector.sym = varDef.sym;
            selector.type = varDef.type;
            stats.append(varDef);
        }

        JFXExpression varRef = expr;

        if (selector != null) {
            VarSymbol vsym = (VarSymbol)JavafxTreeInfo.symbol(expr);
            varRef = m.at(tree.pos).Select(selector, vsym);
            ((JFXSelect)varRef).sym = vsym;
        }

        if (index != null) {
            varRef = m.at(tree.pos).SequenceIndexed(varRef, index).setType(tree.getExpression().type);
        }

        //cache the old value of the unary operand. The translated expression
        //depends on whether the operand is a select or not:
        //
        //(SELECT) var $oldVal$ = $expr$.x;
        //(IDENT)  var $oldVal$ = x;
        JFXVar oldValDef = makeTmpVar(tree.pos(), "oldVal", varRef, varRef.type);
        stats.append(oldValDef);

        JFXIdent oldVal = m.at(tree.pos).Ident(oldValDef.sym);
        oldVal.sym = oldValDef.sym;

        //Generate the binary expression this unary translates to
        //
        //(SELECT) $expr$.x = $oldVal [+/-] 1;
        //(IDENT)  x = $oldVal [+/-] 1;
        JFXBinary binary = (JFXBinary)m.at(tree.pos).Binary(opTag, oldVal, m.at(tree.pos).Literal(opType.tag, 1).setType(opType));
        binary.operator = rs.resolveBinaryOperator(tree.pos(), opTag, env, opType, opType);
        binary.setType(binary.operator.type.asMethodType().getReturnType());
        JFXExpression incDecStat = (JFXExpression)m.at(tree.pos).Assign(varRef, binary).setType(opType);

        //If this is a postfix unary expression, the old value is returned
        JFXExpression blockValue = incDecStat;
        if (postFix) {
            stats.append(incDecStat);
            blockValue = oldVal;
        }

        JFXBlock block = (JFXBlock)m.at(tree.pos).Block(0L, stats.toList(), blockValue).setType(opType);
        return lowerExpr(block, Type.noType);
    }
    //where
    private JavafxTag unaryToBinaryOpTag(JavafxTag tag) {
        switch (tag) {
            case POSTINC:
            case PREINC: return JavafxTag.PLUS;
            case POSTDEC:
            case PREDEC: return JavafxTag.MINUS;
            default: throw new AssertionError("Unexpecetd unary operator tag: " + tag);
        }
    }
    //where
    private boolean isPostfix(JavafxTag tag) {
        switch (tag) {
            case POSTINC:
            case POSTDEC: return true;
            case PREINC:
            case PREDEC: return false;
            default: throw new AssertionError("Unexpecetd unary operator tag: " + tag);
        }
    }

    @Override
    public void visitVar(JFXVar tree) {
        JFXExpression init = lowerExpr(tree.getInitializer(), tree.type);
        JFXOnReplace onReplace = lower(tree.getOnReplace());
        JFXOnReplace onInvalidate = lower(tree.getOnInvalidate());
        JFXVar res = m.at(tree.pos).Var(tree.name, tree.getJFXType(), tree.mods, init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
        JFXVarInit vsi = tree.getVarInit();
        if (vsi != null) {
            // update the var in the var-init
            vsi.resetVar(res);
        }
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
        result = tree;
    }

    @Override
    public void visitVarRef(JFXVarRef tree) {
        result = tree;
    }

    public void visitBlockExpression(JFXBlock tree) {
        List<JFXExpression> stats = lower(tree.stats);
        JFXExpression value = tree.value != null ?
            lowerExpr(tree.value, pt) :
            null;
        result = m.Block(tree.flags, stats, value);
        result.type = value != null ?
            value.type :
            tree.type;
    }

    public void visitBreak(JFXBreak tree) {
        result = tree;
    }

    public void visitCatch(JFXCatch tree) {
        JFXBlock body = lower(tree.body);
        result = m.at(tree.pos).Catch(tree.param, body).setType(tree.type);
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        Symbol prevClass = currentClass;
        try {
            currentClass = tree.sym;
            List<JFXTree> defs = lower(tree.getMembers());
            tree.setMembers(defs);
            result = tree;
        }
        finally {
            currentClass = prevClass;
        }
    }

    public void visitContinue(JFXContinue tree) {
        result = tree;
    }

    public void visitErroneous(JFXErroneous tree) {
        result = tree;
    }

    public void visitForExpression(JFXForExpression tree) {
        result = lowerForExpression(tree);
        patchForLoop(result);
        for (JFXForExpressionInClause clause : tree.getForExpressionInClauses()) {
            forClauseMap.remove(clause);
        }
    }

    public JFXExpression lowerForExpression(JFXForExpression tree) {
        JFXForExpressionInClause clause = lower(tree.getForExpressionInClauses().head);
        JFXExpression body = tree.getBodyExpression();
        if (tree.getForExpressionInClauses().size() > 1) {
            // for (INCLAUSE(1), INCLAUSE(2), ... INCLAUSE(n)) BODY
            // (n>1) is lowered to:
            // for (INCLAUSE(1) Lower(for (INCLAUSE(2) (... for (INCLAUSE(n)) ... )) BODY
            JFXForExpression nestedFor = (JFXForExpression)m.ForExpression(tree.getForExpressionInClauses().tail, tree.bodyExpr).setType(tree.type);
            body = lowerForExpression(nestedFor);
        }
        else {
            //single clause for expression - standard lowering
            Type typeToCheck = types.isSequence(tree.getBodyExpression().type) ?
                tree.type :
                types.elementType(tree.type);
            body = lowerExpr(tree.bodyExpr, typeToCheck);
        }
        // Standard form is that the body is a block-expression
        if(!(body instanceof JFXBlock)) {
            body = m.Block(0L, List.<JFXExpression>nil(), body).setType(body.type);
        }
        JFXForExpression res = m.at(tree.pos).ForExpression(List.of(clause), body);
        return (JFXForExpression)res.setType(tree.type);
    }

    private void patchForLoop(JFXTree forExpr) {
        class ForLoopPatcher extends JavafxTreeScanner {
            
            Name targetLabel;
            int synthNameCount = 0;

            private Name newLabelName() {
                return names.fromString(JavafxDefs.synthForLabelPrefix + synthNameCount++);
            }

            @Override
            public void visitBreak(JFXBreak tree) {
                tree.label = targetLabel;
            }

            @Override
            public void visitContinue(JFXContinue tree) {
                tree.label = targetLabel;
            }

            @Override
            public void visitIndexof(JFXIndexof tree) {
                tree.clause = forClauseMap.get(tree.clause);
            }

            @Override
            public void visitForExpressionInClause(JFXForExpressionInClause tree) {
                tree.label = newLabelName();
                if (targetLabel == null) {
                    targetLabel = tree.label;
                }
                super.visitForExpressionInClause(tree);
            }
        }
        new ForLoopPatcher().scan(forExpr);
    }

    public void visitIdent(JFXIdent tree) {
        result = tree;
    }

    public void visitImport(JFXImport tree) {
        result = tree;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        JFXBlock body = lower(tree.body);
        JFXInitDefinition res = m.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitInstanciate(JFXInstanciate tree) {
       List<JFXObjectLiteralPart> parts = lower(tree.getParts());
       JFXClassDeclaration cdecl = lower(tree.getClassBody());
       List<JFXExpression> args = lower(tree.getArgs());
       JFXInstanciate res = m.at(tree.pos).Instanciate(tree.getJavaFXKind(), tree.getIdentifier(), cdecl, args, parts, tree.getLocalvars());
       res.sym = tree.sym;
       res.constructor = tree.constructor;
       res.varDefinedByThis = tree.varDefinedByThis;
       res.type = tree.type;
       if (tree.getLocalvars().nonEmpty()) {
            //ObjLit {
            //  local var 1;
            //  local var 2;
            //  ...
            //  local var n;
            //}
            //
            //is equivalent to:
            //
            //{
            //
            //  local var 1;
            //  local var 2;
            //  ...
            //  local var n;
            //
            //  ObjLit {
            //    ...
            //  }
            //}
           result = m.Block(0L, List.convert(JFXExpression.class, tree.getLocalvars()), res).setType(tree.type);
           res.clearLocalVars();
       }
       else {
           result = res;
       }
    }

    public void visitInvalidate(JFXInvalidate tree) {
        JFXExpression expr = lower(tree.getVariable());
        result = m.at(tree.pos).Invalidate(expr).setType(tree.type);
    }

    public void visitModifiers(JFXModifiers tree) {
        result = tree;
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXBlock body = lower(tree.getBody());
        JFXOnReplace res = tree.getTriggerKind() == JFXOnReplace.Kind.ONREPLACE ?
                m.at(tree.pos).OnReplace(tree.getOldValue(), tree.getFirstIndex(), tree.getLastIndex(), tree.getNewElements(), body) :
                m.at(tree.pos).OnInvalidate(body);
        result = res.setType(tree.type);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = lower(tree.expr);
        result = m.at(tree.pos).Parens(expr).setType(tree.type);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        JFXBlock body = lower(tree.body);
        JFXPostInitDefinition res = m.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitScript(JFXScript tree) {
        varCount = 0;
        tree.defs = lower(tree.defs);
        result = tree;
    }

    public void visitSelect(JFXSelect tree) {
        JFXExpression selected = lower(tree.selected);
        JFXSelect res = (JFXSelect)m.Select(selected, tree.sym);
        res.name = tree.name;
        result = res.setType(tree.type);
    }

    public void visitSkip(JFXSkip tree) {
        result = tree;
    }

    public void visitThrow(JFXThrow tree) {
        JFXExpression expr = lower(tree.getExpression());
        result = m.at(tree.pos).Throw(expr).setType(tree.type);
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = lower(tree.getBlock());
        List<JFXCatch> catches = lower(tree.catchers);
        JFXBlock finallyBlock = lower(tree.getFinallyBlock());
        result = m.at(tree.pos).Try(body, catches, finallyBlock).setType(tree.type);
    }

    public void visitTypeAny(JFXTypeAny tree) {
        result = tree;
    }

    public void visitTypeArray(JFXTypeArray tree) {
        result = tree;
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXExpression expr = lowerExpr(tree.getExpression(), tree.clazz.type);
        result = m.at(tree.pos).TypeCast(tree.clazz, expr).setType(tree.type);
    }

    public void visitTypeClass(JFXTypeClass tree) {
        result = tree;
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        result = tree;
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
        result = tree;
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        JFXExpression cond = lowerExpr(tree.getCondition(), syms.booleanType);
        JFXExpression body = lower(tree.getBody());
        // Standard form is that the body is a block-expression
        if(!(body instanceof JFXBlock)) {
            body = m.Block(0L, List.<JFXExpression>nil(), body);
        }
        body.setType(syms.voidType);
        result = m.at(tree.pos).WhileLoop(cond, body).setType(syms.voidType);
    }
}
