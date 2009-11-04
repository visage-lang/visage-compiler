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
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

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

    private JavafxTypes types;
    private JavafxResolve rs;
    private JavafxSymtab syms;
    private JavafxTreeMaker m;
    private Type pt;
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
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        m = JavafxTreeMaker.instance(context);
        rs = JavafxResolve.instance(context);
        names = Name.Table.instance(context);
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
        if (type == Type.noType
                || type == null
                || type.isErroneous()
                || type == syms.voidType
                || tree.type == syms.voidType
                || tree.type == syms.unreachableType
                || type == syms.unreachableType)
            return tree;
        else {
            tree = makeNumericBoxConversionIfNeeded(tree, type);
            return (!types.isSubtypeUnchecked(tree.type, type) ||
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
        VarSymbol vsym = new VarSymbol(0L, tempName(name), type, makeDummyMethodSymbol(currentClass));
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

    private MethodSymbol makeDummyMethodSymbol(Symbol owner) {
        return new MethodSymbol(Flags.BLOCK, names.empty, null, owner);
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = lower(tree.lhs);
        JFXExpression rhs = lowerExpr(tree.rhs, tree.lhs.type);
        result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        JFXExpression lhs = lower(tree.lhs);
        JFXExpression rhs = lowerExpr(tree.rhs, tree.operator.type.getParameterTypes().head);
        JFXAssignOp assign = m.at(tree.pos).Assignop(tree.getFXTag(), lhs, rhs);
        assign.operator = tree.operator;
        result = assign.setType(tree.type);
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
        if (sym instanceof MethodSymbol &&
                ((MethodSymbol)sym).isVarArgs()) {
            Type varargType = paramTypes.reverse().head;
            paramTypes = paramTypes.reverse().tail.reverse(); //remove last formal
            while (paramTypes.size() < tree.args.size()) {
                paramTypes = paramTypes.append(types.elemtype(varargType));
            }
        }
        List<JFXExpression> args = null;
        if (!(sym instanceof MethodSymbol) ||
                ((sym.flags_field & JavafxFlags.FUNC_POINTER_MAKE) == 0 &&
                (sym.flags_field & JavafxFlags.FUNC_IS_INITIALIZED) == 0)) {
            args = lowerExprs(tree.args, paramTypes);
        }
        else {
            args = lower(tree.args);
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
        result = that;
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
        JFXExpression funcValue = lower(that.funcValue);
        JFXInterpolateValue res = m.at(that.pos).InterpolateValue(that.attribute, funcValue, that.interpolation);
        res.funcValue = funcValue; //to make Decompose happy
        res.sym = that.sym;
        result = res.setType(that.type);
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
        List<JFXExpression> values = lower(that.values);
        JFXExpression trigger = lower(that.trigger);
        result = m.at(that.pos).KeyFrameLiteral(that.start, values, trigger).setType(that.type);
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
            result = lowerArithmeticUnary(tree);
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

    private JFXExpression lowerArithmeticUnary(JFXUnary tree) {
        boolean postFix = isPostfix(tree.getFXTag());
        JavafxTag opTag = unaryToBinaryOpTag(tree.getFXTag());
        Type opType = types.unboxedTypeOrType(tree.getExpression().type);
        if (types.isSameType(opType, syms.charType)) {
            opType = syms.intType;
        }
        ListBuffer<JFXExpression> stats = ListBuffer.lb();

        //if the unary operand is a select of the kind a.x
        //then we need to cache the selected part (a), so that
        //it won't be recomputed twice.
        //
        // var $expr$ = a;
        JFXIdent selector = null;

        if (tree.getExpression().getFXTag() == JavafxTag.SELECT) {
            JFXExpression selected = ((JFXSelect)tree.getExpression()).selected;
            JFXVar varDef = makeTmpVar(tree.pos(), "expr", selected, selected.type);
            selector = m.at(tree.pos).Ident(varDef.sym);
            selector.sym = varDef.sym;
            selector.type = varDef.type;
            stats.append(varDef);
        }

        JFXExpression varRef = tree.getExpression();

        if (selector != null) {
            VarSymbol vsym = (VarSymbol)JavafxTreeInfo.symbol(tree.getExpression());
            varRef = m.at(tree.pos).Select(selector, vsym);
            ((JFXSelect)varRef).sym = vsym;
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
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
        JFXVar var = lower(tree.getVar());
        result = m.VarInit(var).setType(tree.type); //avoid cast
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
        Type typeToCheck = types.isSequence(tree.getBodyExpression().type) ?
                tree.type :
                types.elementType(tree.type);
        JFXExpression body = lowerExpr(tree.bodyExpr, typeToCheck);
        List<JFXForExpressionInClause> clauses = lower(tree.getForExpressionInClauses());
        result = m.at(tree.pos).ForExpression(clauses, body).setType(tree.type);
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
       result = res.setType(tree.type);
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
        JFXExpression body = lower(tree.getStatement());
        result = m.at(tree.pos).WhileLoop(cond, body).setType(tree.type);
    }
}
