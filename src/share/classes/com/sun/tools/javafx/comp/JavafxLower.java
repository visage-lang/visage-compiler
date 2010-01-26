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

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.JFXExpression;

import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Type;

import com.sun.tools.mjavac.code.TypeTags;
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
        return tree = needSequenceConversion(tree, type) ?
            toSequence(tree, type) :
            preTrans.makeCastIfNeeded(tree, type);
    }

    private boolean needSequenceConversion(JFXExpression tree, Type type) {
        return (types.isSequence(type) &&
            ((!types.isSequence(tree.type) &&
            tree.type != syms.unreachableType &&
            !types.isArray(tree.type)) ||
            isNull(tree)));
    }

    private boolean isNull(JFXTree tree) {
        return (tree.getFXTag() == JavafxTag.LITERAL &&
                ((JFXLiteral)tree).value == null);
    }

    private JFXExpression toSequence(JFXExpression tree, Type type) {
        JFXExpression seqExpr = null;
        if (isNull(tree)) {
            seqExpr = m.at(tree.pos).EmptySequence().setType(type);
        }
        else if (types.isSameType(tree.type, syms.objectType) &&
                types.isSubtypeUnchecked(syms.javafx_SequenceTypeErasure, type)) { //synthetic call
            MethodSymbol msym = (MethodSymbol)rs.findIdentInType(env, syms.javafx_SequencesType, defs.Sequences_convertObjectToSequence.methodName, Kinds.MTH);
            JFXExpression sequencesType = m.at(tree.pos).Type(syms.javafx_SequencesType).setType(syms.javafx_SequencesType);
            JavafxTreeInfo.setSymbol(sequencesType, syms.javafx_SequencesType.tsym);
            JFXIdent convertMeth = m.at(tree.pos).Ident(defs.Sequences_convertObjectToSequence.methodName);
            convertMeth.sym = msym;
            convertMeth.type = msym.type;
            seqExpr = m.at(tree.pos).Apply(List.<JFXExpression>nil(), convertMeth, List.of(tree)).setType(msym.type.getReturnType());

        }
        else {
            seqExpr = m.at(tree.pos).ExplicitSequence(List.of(preTrans.makeCastIfNeeded(tree, types.elementType(type))));
            seqExpr.type = type;
        }
        return seqExpr;
    }

    private Name tempName(String label) {
        return names.fromString("$" + label + "$" + varCount++);
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, String name, JFXExpression init, Type type) {
        return makeVar(diagPos, 0L, name, JavafxBindStatus.UNBOUND, init, type);
    }
    
    private JFXVar makeVar(DiagnosticPosition diagPos, long flags, String name, JFXExpression init, Type type) {
        return makeVar(diagPos, flags, name, JavafxBindStatus.UNBOUND, init, type);
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, String name, JavafxBindStatus bindStatus, JFXExpression init, Type type) {
        return makeVar(diagPos, 0L, name, bindStatus, init, type);
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, long flags, String name, JavafxBindStatus bindStatus, JFXExpression init, Type type) {
        JavafxVarSymbol vsym = new JavafxVarSymbol(types, names, flags, tempName(name), types.normalize(type), preTrans.makeDummyMethodSymbol(currentClass));
        return makeVar(diagPos, vsym, bindStatus, init);
    }

    private JFXVar makeVar(DiagnosticPosition diagPos, JavafxVarSymbol vSym, JavafxBindStatus bindStatus, JFXExpression init) {
        JFXModifiers mod = m.at(diagPos).Modifiers(vSym.flags());
        JFXType fxType = preTrans.makeTypeTree(vSym.type);
        JFXVar v = m.at(diagPos).Var(vSym.name, fxType, mod, init, bindStatus, null, null);
        v.sym = vSym;
        v.type = vSym.type;
        return v;
    }

    public void visitAssign(JFXAssign tree) {
        if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED &&
                types.isSequence(((JFXSequenceIndexed)tree.lhs).getSequence().type) &&
                (types.isSequence(tree.rhs.type) || types.isSameType(tree.rhs.type, syms.objectType))) {
            result = lowerSequenceIndexedAssign(tree.pos(), (JFXSequenceIndexed)tree.lhs, tree.rhs);
        }
        else {
            JFXExpression lhs = lower(tree.lhs);
            JFXExpression rhs = lowerExpr(tree.rhs, tree.lhs.type);
            result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
        }
    }

    JFXExpression lowerSequenceIndexedAssign(DiagnosticPosition pos, JFXSequenceIndexed indexed, JFXExpression val) {
        Type resType = indexed.getSequence().type;
        JFXVar indexVar = makeVar(pos, "pos", indexed.getIndex(), indexed.getIndex().type);
        JFXIdent indexRef = m.at(pos).Ident(indexVar);
        JFXExpression lhs = m.SequenceSlice(indexed.getSequence(), indexRef, indexRef, JFXSequenceSlice.END_INCLUSIVE);
        lhs.setType(resType);
        JFXExpression assign = m.Assign(lhs, val).setType(resType);
        return lower(m.Block(0L, List.<JFXExpression>of(indexVar), assign).setType(resType));
    }

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
            JFXVar varDef = makeVar(tree.pos(), "index", indexed.getIndex(), indexed.getIndex().type);
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
            JFXVar varDef = makeVar(tree.pos(), "expr", selected, selected.type);
            // But, if this select is ClassName.foo, then we don't want
            // to create "var $expr = a;"
            Symbol sym = JavafxTreeInfo.symbolFor(selected);
            if (sym == null || sym.kind != Kinds.TYP) {
                selector = m.at(tree.pos).Ident(varDef.sym);
                selector.sym = varDef.sym;
                selector.type = varDef.type;
                stats.append(varDef);
            }
        }

        JFXExpression varRef = lhs;

        //create a reference to the cached var. The translated expression
        //depends on whether the operand is a select or not:
        //
        //(SELECT)  $expr$.x;
        //(IDENT)   x;

        if (selector != null) {
            JavafxVarSymbol vsym = (JavafxVarSymbol)JavafxTreeInfo.symbol(lhs);
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

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        JFXExpression whereExpr = lower(that.getWhereExpression());
        Type typeToCheck = that.seqExpr.type;
        if  (that.seqExpr.type.tag == TypeTags.BOT ||
                types.isSameType(that.seqExpr.type, syms.javafx_EmptySequenceType)) {
            typeToCheck = types.sequenceType(that.var.type);
        }
        else if (!types.isSequence(that.seqExpr.type) &&
                !types.isArray(that.seqExpr.type) &&
                types.asSuper(that.seqExpr.type, syms.iterableType.tsym) == null) {
            typeToCheck = types.sequenceType(that.seqExpr.type);
        }
        JFXExpression seqExpr = lowerExpr(that.seqExpr, typeToCheck);
        JFXForExpressionInClause res = m.at(that.pos).InClause(that.getVar(), seqExpr, whereExpr);
        res.setIndexUsed(that.getIndexUsed());
        res.indexVarSym = that.indexVarSym;
        forClauseMap.put(that, res);
        result = res.setType(that.type);
    }

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

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression meth = lower(tree.meth);
        List<Type> paramTypes = tree.meth.type.getParameterTypes();
        Symbol sym = JavafxTreeInfo.symbolFor(tree.meth);
        
        List<JFXExpression> args = List.nil();
        boolean pointer_Make = types.isSyntheticPointerFunction(sym);
        boolean builtins_Func = types.isSyntheticBuiltinsFunction(sym);
        if (pointer_Make || builtins_Func) {
                JFXExpression varExpr = lower(tree.args.head);
                ListBuffer<JFXExpression> syntheticArgs = ListBuffer.lb();
                syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, JFXVarRef.RefKind.INST).setType(syms.javafx_FXObjectType));
                syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, JFXVarRef.RefKind.VARNUM).setType(syms.intType));
                
                Symbol msym = builtins_Func ?
                    preTrans.makeSyntheticBuiltinsMethod(sym.name) :
                    preTrans.makeSyntheticPointerMake();
                JavafxTreeInfo.setSymbol(meth, msym);
                meth.type = msym.type;
                args = syntheticArgs.toList();
        }
        else if (sym instanceof MethodSymbol &&
                ((MethodSymbol)sym).isVarArgs()) {
            List<JFXExpression> actuals = tree.args;
            while (paramTypes.tail.nonEmpty()) {
                args = args.append(lowerExpr(actuals.head, paramTypes.head));
                actuals = actuals.tail;
                paramTypes = paramTypes.tail;
            }
            Type varargType = paramTypes.head;
            if (actuals.size() == 1 && 
                    (types.isSequence(actuals.head.type) ||
                    types.isArray(actuals.head.type))) {
                args = args.append(lowerExpr(actuals.head, varargType));
            }
            else if (actuals.size() > 0) {
                while (actuals.nonEmpty()) {
                    args = args.append(lowerExpr(actuals.head, types.elemtype(varargType)));
                    actuals = actuals.tail;
                }
            }
        }
        else {
            args = lowerExprs(tree.args, paramTypes);
        }
         
        result = m.Apply(tree.typeargs, meth, args);
        result.type = tree.type;
    }

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

    public void visitIfExpression(JFXIfExpression tree) {
        if (tree.type.tag != TypeTags.VOID &&
                (tree.truepart.type == syms.unreachableType ||
                (tree.falsepart != null && tree.falsepart.type == syms.unreachableType))) {
            result = lowerUnreachableIfExpression(tree);
        }
        else {
            boolean thenPartSeq = types.isSequence(tree.truepart.type);
            boolean elsePartSeq = tree.falsepart != null ?
                types.isSequence(tree.falsepart.type) :
                thenPartSeq;
            boolean nonSeqExpected = thenPartSeq != elsePartSeq &&
                    !types.isSequence(pt);
            JFXExpression cond = lowerExpr(tree.cond, syms.booleanType);
            JFXExpression truePart = lowerExpr(tree.truepart,
                    !nonSeqExpected || thenPartSeq ? tree.type : types.elementTypeOrType(tree.type));
            JFXExpression falsePart = lowerExpr(tree.falsepart,
                    !nonSeqExpected || elsePartSeq ? tree.type : types.elementTypeOrType(tree.type));
            result = m.Conditional(cond, truePart, falsePart);
            result.setType(nonSeqExpected ? syms.objectType : tree.type);
        }
    }

    public JFXTree lowerUnreachableIfExpression(JFXIfExpression tree) {
        boolean inverted = tree.truepart.type == syms.unreachableType;
        Type treeType = tree.type.tag == TypeTags.BOT ?
            types.isSequence(tree.type) ?
                types.sequenceType(syms.objectType) : syms.objectType :
            tree.type;
        JFXExpression truePart = lowerExpr(tree.truepart, treeType);
        JFXExpression falsePart = lowerExpr(tree.falsepart, treeType);
        JFXVar varDef = makeVar(tree.pos(), "res", null, treeType);
        JFXIdent varRef = m.at(tree.pos).Ident(varDef.sym);
        varRef.sym = varDef.sym;
        varRef.type = varDef.type;

        JFXExpression assign = m.at(tree.pos).Assign(varRef, inverted ? falsePart : truePart).setType(syms.voidType); //we need void here!

        JFXExpression ifExpr = m.at(tree.pos).Conditional(tree.cond,
                inverted ? truePart : assign, inverted ? assign : falsePart).setType(syms.voidType); //we need void here!

        return m.at(tree.pos()).Block(0L, List.of(varDef, ifExpr), varRef).setType(varRef.type);
    }

    public void visitIndexof(JFXIndexof that) {
        JFXIndexof res = m.at(that.pos).Indexof(that.fname);
        res.clause = that.clause;
        result = res.setType(that.type);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = lower(tree.getExpression());
        result = m.at(tree.pos).TypeTest(expr, tree.clazz).setType(tree.type);
    }

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
        JFXObjectLiteralPart part = m.at(pos).ObjectLiteralPart(varName, value, JavafxBindStatus.UNBOUND);
        part.setType(value.type);
        part.sym = rs.findIdentInType(env, site, varName, Kinds.VAR);
        return part;
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
        ListBuffer<JFXTree> parts = ListBuffer.lb();
        JFXExpression keyValues = m.at(that.pos).ExplicitSequence(that.values).setType(types.sequenceType(syms.javafx_KeyValueType));
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyFrameType, defs.time_KeyFrameMethodName, that.start));
        parts.append(makeObjectLiteralPart(that.pos(), syms.javafx_KeyFrameType, defs.values_KeyFrameMethodName, keyValues));
        JFXExpression res = m.at(that.pos).ObjectLiteral(m.at(that.pos).Type(syms.javafx_KeyValueType), parts.toList()).setType(syms.javafx_KeyFrameType);
        result = lower(res);
    }

    public void visitLiteral(JFXLiteral tree) {
        result = tree;
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JFXExpression expr = lowerExpr(tree.getExpression(), tree.sym.type);
        JFXObjectLiteralPart res = m.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getExplicitBindStatus());
        res.markBound(tree.getBindStatus());
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        JFXExpression init = lowerExpr(tree.getInitializer(), tree.getId().sym.type);
        JFXOnReplace onReplace = lower(tree.getOnReplace());
        JFXOnReplace onInvalidate = lower(tree.getOnInvalidate());
        JFXOverrideClassVar res = m.at(tree.pos).OverrideClassVar(tree.name, tree.getJFXType(), tree.mods, tree.getId(), init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitReturn(JFXReturn tree) {
        Type typeToCheck = enclFunc.type != null ?
            enclFunc.type.getReturnType() :
            syms.objectType; //nedded because run function has null type
        JFXExpression retExpr = lowerExpr(tree.getExpression(), typeToCheck);
        result = m.at(tree.pos).Return(retExpr).setType(tree.type);
    }

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

    public void visitSequenceEmpty(JFXSequenceEmpty that) {
        result = that;
    }

    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        ListBuffer<JFXExpression> buf = ListBuffer.lb();
        for (JFXExpression item : that.getItems()) {
            Type typeToCheck = types.isSameType(item.type, syms.objectType) ||
                    types.isArrayOrSequenceType(item.type) ?
                that.type :
                types.elementType(that.type);
            buf.append(lowerExpr(item, typeToCheck));
        }
        result = m.at(that.pos).ExplicitSequence(buf.toList()).setType(that.type);
    }

    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        JFXExpression index = lowerExpr(that.getIndex(), syms.intType);
        JFXExpression seq = lower(that.getSequence());
        result = m.at(that.pos).SequenceIndexed(seq, index).setType(that.type);
    }

    public void visitSequenceInsert(JFXSequenceInsert that) {
        JFXExpression seq = lower(that.getSequence());
        Type typeToCheck = types.isArrayOrSequenceType(that.getElement().type) ||
                types.isSameType(syms.objectType, that.getElement().type) ?
                that.getSequence().type :
                types.elementType(that.getSequence().type);
        JFXExpression el = lowerExpr(that.getElement(), typeToCheck);
        JFXExpression pos = lowerExpr(that.getPosition(), syms.intType);
        result = m.at(that.pos).SequenceInsert(seq, el, pos, that.shouldInsertAfter()).setType(that.type);
    }

    public void visitSequenceRange(JFXSequenceRange that) {
        JFXExpression lower = lowerExpr(that.getLower(), types.elementType(that.type));
        JFXExpression upper = lowerExpr(that.getUpper(), types.elementType(that.type));
        JFXExpression step = lowerExpr(that.getStepOrNull(), types.elementType(that.type));
        JFXSequenceRange res = m.at(that.pos).RangeSequence(lower, upper, step, that.isExclusive());
        res.boundSizeVar = that.boundSizeVar;
        result = res.setType(that.type);
    }

    public void visitSequenceSlice(JFXSequenceSlice that) {
        JFXExpression seq = lower(that.getSequence());
        JFXExpression start = lowerExpr(that.getFirstIndex(), syms.intType);
        JFXExpression end = lowerExpr(that.getLastIndex(), syms.intType);
        result = m.at(that.pos).SequenceSlice(seq, start, end, that.getEndKind()).setType(that.type);
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = lower(tree.parts);
        result = m.at(tree.pos).StringExpression(parts, tree.translationKey).setType(tree.type);
    }

    public void visitUnary(JFXUnary tree) {
        if (tree.getFXTag().isIncDec()) {
            result = visitNumericUnary(tree);
        } else {
            JFXExpression arg = tree.getFXTag() == JavafxTag.REVERSE ?
                lowerExpr(tree.getExpression(), tree.type) :
                tree.getOperator() != null ?
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
            JFXVar varDef = makeVar(tree.pos(), "index", indexed.getIndex(), indexed.getIndex().type);
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
            Symbol sym = JavafxTreeInfo.symbolFor(selected);
            // But, if this select is ClassName.foo, then we don't want
            // to create "var $expr = a;"
            if (sym == null || sym.kind != Kinds.TYP) {
                JFXVar varDef = makeVar(tree.pos(), "expr", selected, selected.type);
                selector = m.at(tree.pos).Ident(varDef.sym);
                selector.sym = varDef.sym;
                selector.type = varDef.type;
                stats.append(varDef);
            }
        }

        JFXExpression varRef = expr;

        if (selector != null) {
            JavafxVarSymbol vsym = (JavafxVarSymbol)JavafxTreeInfo.symbol(expr);
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
        JFXVar oldValDef = makeVar(tree.pos(), "oldVal", varRef, varRef.type);
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

    public void visitVarInit(JFXVarInit tree) {
        result = tree;
    }

    public void visitVarRef(JFXVarRef tree) {
        result = tree;
    }

    public void visitBlockExpression(JFXBlock tree) {
        List<JFXExpression> stats = tree.stats;
        JFXExpression value = tree.value;
        if (value != null) {
            if (JavafxTreeInfo.skipParens(value).getFXTag() == JavafxTag.VAR_DEF) {
                JFXVar varDef = (JFXVar)JavafxTreeInfo.skipParens(value);
                JFXIdent varRef = m.at(tree.value.pos).Ident(varDef.sym);
                varRef.sym = varDef.sym;
                varRef.type = varDef.type;
                value = varRef;
                stats = stats.append(varDef);
            }
            else if (value.type == syms.voidType &&
                    !tree.isVoidValueAllowed) {
                 stats = stats.append(value);
                 value = makeDefaultValue(tree.type);
            }
        }
        List<JFXExpression> loweredStats = lower(stats);
        JFXExpression loweredValue = value != null ?
            lowerExpr(value, pt) :
            null;
        
        result = m.Block(tree.flags, loweredStats, loweredValue);
        result.type = value != null ?
            loweredValue.type :
            tree.type;
    }
    //where
    private JFXExpression makeDefaultValue(Type t) {
        switch (t.tag) {
            case TypeTags.BYTE: return m.Literal(TypeTags.BYTE, 0).setType(syms.byteType);
            case TypeTags.SHORT: return m.Literal(TypeTags.SHORT, 0).setType(syms.shortType);
            case TypeTags.INT: return m.Literal(TypeTags.INT, 0).setType(syms.intType);
            case TypeTags.FLOAT: return m.Literal(TypeTags.FLOAT, 0).setType(syms.floatType);
            case TypeTags.DOUBLE: return m.Literal(TypeTags.DOUBLE, 0).setType(syms.doubleType);
            case TypeTags.BOOLEAN: return m.Literal(TypeTags.BOOLEAN, 0).setType(syms.booleanType);
            case TypeTags.CLASS: {
                if (types.isSequence(t)) {
                    return m.EmptySequence().setType(syms.javafx_EmptySequenceType);
                } else if (types.isSameType(t, syms.javafx_StringType)) {
                    return m.Literal("").setType(syms.javafx_StringType);
                } else if (types.isSameType(t, syms.javafx_DurationType)) {
                    Name zeroName = names.fromString("ZERO");
                    JFXSelect res = (JFXSelect)m.Select(
                            preTrans.makeTypeTree(syms.javafx_DurationType),
                            zeroName).setType(syms.javafx_DurationType);
                    res.sym = rs.findIdentInType(env, syms.javafx_DurationType, zeroName, Kinds.VAR);
                    return res;
                }
            }
            default: return m.Literal(TypeTags.BOT, null);
        }
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
            List<JFXTree> cdefs = lower(tree.getMembers());
            tree.setMembers(cdefs);
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
        patchForLoop(result, tree.getForExpressionInClauses());
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
            Type typeToCheck = types.isSameType(tree.getBodyExpression().type, syms.objectType) ||
                    types.isSequence(tree.getBodyExpression().type) ?
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

    private void patchForLoop(JFXTree forExpr, final List<JFXForExpressionInClause> clausesToPatch) {
        class ForLoopPatcher extends JavafxTreeScanner {

            Name targetLabel;
            int synthNameCount = 0;

            private Name newLabelName() {
                return names.fromString(JavafxDefs.synthForLabelPrefix + forClauseMap.size() + "$" + synthNameCount++);
            }

            @Override
            public void visitBreak(JFXBreak tree) {
                tree.label = tree.label == null ?
                    targetLabel :
                    tree.label;
            }

            @Override
            public void visitContinue(JFXContinue tree) {
                tree.label = tree.label == null ?
                    targetLabel :
                    tree.label;
            }

            @Override
            public void visitIndexof(JFXIndexof tree) {
                tree.clause = clausesToPatch.contains(tree.clause) ?
                    forClauseMap.get(tree.clause) :
                    tree.clause;
            }

            @Override
            public void visitForExpressionInClause(JFXForExpressionInClause tree) {
                tree.label = tree.label == null ?
                    newLabelName() :
                    tree.label;
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
        ListBuffer<JFXExpression> locals = ListBuffer.lb();
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
            for (JFXVar var : tree.getLocalvars()) {
                locals.append(lower(var));
            }
        }

        ListBuffer<JFXTree> newOverrides = ListBuffer.<JFXTree>lb();
        ListBuffer<JFXObjectLiteralPart> unboundParts = ListBuffer.<JFXObjectLiteralPart>lb();
        for (JFXObjectLiteralPart part : tree.getParts()) {
            if (part.isExplicitlyBound()) {
                m.at(part.pos());  // create at part position

                // id for the override
                JFXIdent id = m.Ident(part.name);
                id.sym = part.sym;
                id.type = part.sym.type;

                JFXExpression initExpr;

                if (true) { // enable bound object literal initializer scoping to object literal level.
                    // Shread the expression outside the class, so that the context is correct
                    // The variable should be marked as script private as it shouldn't
                    // be accessible from outside.

                    JFXExpression partExpr = part.getExpression();
                    Symbol initSym = JavafxTreeInfo.symbol(partExpr);
                    // FIXME: revisit this - more opportunities to optimze here.
                    // Check if we can avoid more local class wrapping around object
                    // literal class.
                    if (partExpr.getFXTag() == JavafxTag.LITERAL ||
                        (initSym != null && initSym.isStatic())) {
                        initExpr = partExpr;
                    } else {
                        JFXVar shred = makeVar(
                            part.pos(),
                            JavafxFlags.SCRIPT_PRIVATE,
                            part.name + "$ol",
                            part.getBindStatus(),
                            lowerExpr(partExpr, part.sym.type),
                            part.sym.type);
                        JFXIdent sid = m.Ident(shred.name);
                        sid.sym = shred.sym;
                        sid.type = part.sym.type;
                        locals.append(shred);
                        initExpr = sid;
                    }
                } else {
                    initExpr = part.getExpression(); // lowered with class
                }

                // Turn the part into an override var
                JFXOverrideClassVar ocv =
                        m.OverrideClassVar(
                        part.name,
                        preTrans.makeTypeTree(part.type),
                        m.Modifiers(part.sym.flags_field),
                        id,
                        initExpr,
                        part.getBindStatus(),
                        null,
                        null);
                ocv.sym = (JavafxVarSymbol) part.sym;
                ocv.type = part.sym.type;
                newOverrides.append(ocv);
            } else {
                unboundParts.append(lower(part));
            }
        }

        // Lower the class.  If there are new overrides, fold them into the class first
        JFXClassDeclaration cdecl = tree.getClassBody();
        JFXClassDeclaration lowCdecl;
        if (newOverrides.nonEmpty()) {
            cdecl.setMembers(cdecl.getMembers().appendList(newOverrides));
            lowCdecl = lower(cdecl);
            preTrans.liftTypes(cdecl, cdecl.type, preTrans.makeDummyMethodSymbol(cdecl.sym));
        } else {
            lowCdecl = lower(cdecl);
        }

        // Construct the new instanciate
        JFXInstanciate res = m.at(tree.pos).Instanciate(tree.getJavaFXKind(),
                tree.getIdentifier(),
                lowCdecl,
                lower(tree.getArgs()),
                unboundParts.toList(),
                List.<JFXVar>nil());
        res.sym = tree.sym;
        res.constructor = tree.constructor;
        res.varDefinedByThis = tree.varDefinedByThis;
        res.type = tree.type;

        // If there are locals wrap the whole thing in a block-expression
        if (locals.nonEmpty()) {
            result = m.Block(0L, locals.toList(), res).setType(tree.type);
        } else {
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
        if (tree.sym.isStatic() &&                
                JavafxTreeInfo.symbolFor(tree.selected) != null &&
                JavafxTreeInfo.symbolFor(tree.selected).kind == Kinds.TYP) {
            result = m.at(tree.pos()).Ident(tree.sym);
        }
        else {
            JFXExpression selected = lower(tree.selected);
            JFXSelect res = (JFXSelect)m.Select(selected, tree.sym);
            res.name = tree.name;
            result = res;
        }
        result.setType(tree.type);
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
