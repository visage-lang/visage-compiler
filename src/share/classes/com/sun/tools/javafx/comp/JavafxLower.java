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

import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.TypeSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
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
    private LowerMode mode;
    private Map<JFXForExpressionInClause, JFXForExpressionInClause> forClauseMap; //TODO this should be refactord into a common translation support
    private JavafxEnv<JavafxAttrContext> env;
    private JFXTree enclFunc;
    private JFXTree result;
    private Name.Table names;
    private Symbol currentClass;
    private int varCount;

    enum LowerMode {
        EXPRESSION,
        STATEMENT,
        DECLARATION;
    }

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
        attrEnv.toplevel = lowerDecl(attrEnv.toplevel);
        //System.out.println(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    <T extends JFXTree> T lower(T tree, Type pt, LowerMode mode) {
        Type prevPt = this.pt;
        LowerMode prevMode = this.mode;
        try {
            this.pt = pt;
            this.mode = mode;
            if (tree != null) {
                tree.accept(this);
                return (T)(mode == LowerMode.EXPRESSION ?
                    convertTree((JFXExpression)result, this.pt) :
                    result);
            }
            else
                return null;
        }
        finally {
            this.pt = prevPt;
            this.mode = prevMode;
        }
    }

    
    public <T extends JFXTree> T lowerExpr(T tree) {
        return lower(tree, Type.noType, LowerMode.EXPRESSION);
    }

    public <T extends JFXTree> T lowerExpr(T tree, Type pt) {
        return lower(tree, pt, LowerMode.EXPRESSION);
    }

    public <T extends JFXTree> T lowerStmt(T tree) {
        return lower(tree, Type.noType, LowerMode.STATEMENT);
    }

    public <T extends JFXTree> T lowerDecl(T tree) {
        return lower(tree, Type.noType, LowerMode.DECLARATION);
    }


    <T extends JFXTree> List<T> lower(List<T> trees, LowerMode mode) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(lower(tree, Type.noType, mode));
        }
        return buf.toList();
    }

    public <T extends JFXExpression> List<T> lowerExprs(List<? extends T> trees, List<Type> pts) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(lowerExpr(tree, pts.head));
            pts = pts.tail;
        }
        return buf.toList();
    }

    public <T extends JFXTree> List<T> lowerExprs(List<T> trees) {
        return lower(trees, LowerMode.EXPRESSION);
    }
    
    public <T extends JFXTree> List<T> lowerDecls(List<T> trees) {
        return lower(trees, LowerMode.DECLARATION);
    }

    public <T extends JFXTree> List<T> lowerStats(List<T> trees) {
        return lower(trees, LowerMode.STATEMENT);
    }

    JFXExpression convertTree(JFXExpression tree, Type type) {
        if (type == Type.noType) return tree;
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
            JFXExpression lhs = lowerExpr(tree.lhs);
            JFXExpression rhs = lowerExpr(tree.rhs, tree.lhs.type);
            result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
        }
    }

    JFXExpression lowerSequenceIndexedAssign(DiagnosticPosition pos, JFXSequenceIndexed indexed, JFXExpression val) {
        Type resType = indexed.getSequence().type;
        JFXVar indexVar = makeVar(pos, defs.posNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
        JFXIdent indexRef = m.at(pos).Ident(indexVar);
        JFXExpression lhs = m.SequenceSlice(indexed.getSequence(), indexRef, indexRef, JFXSequenceSlice.END_INCLUSIVE);
        lhs.setType(resType);
        JFXExpression assign = m.Assign(lhs, val).setType(resType);
        return lowerExpr(m.Block(0L, List.<JFXExpression>of(indexVar), assign).setType(resType));
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
            JFXVar varDef = makeVar(tree.pos(), defs.indexNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
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
            JFXVar varDef = makeVar(tree.pos(), defs.exprNamePrefix(), selected, selected.type);
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

        JFXExpression res = stats.nonEmpty() ?
            m.at(tree.pos).Block(0L, stats.toList(), assignOpStat).setType(op.type) :
            assignOpStat;
        return lowerExpr(res, Type.noType);
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
            lowerExpr(tree.lhs) :
            lowerExpr(tree.lhs, lhsType);
        JFXExpression rhs = isEqualExpr && isBoxedOp && !isSequenceOp ?
            lowerExpr(tree.rhs) :
            lowerExpr(tree.rhs, rhsType);
        JFXBinary res = m.at(tree.pos).Binary(tree.getFXTag(), lhs, rhs);
        res.operator = tree.operator;
        result = res.setType(tree.type);
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        JFXExpression whereExpr = lowerExpr(that.getWhereExpression());
        Type typeToCheck = that.seqExpr.type;
        if  (that.seqExpr.type.tag == TypeTags.BOT ||
                types.isSameType(that.seqExpr.type, syms.javafx_EmptySequenceType)) {
            typeToCheck = types.sequenceType(that.var.type);
        }
        else if (that.isBound() &&
                types.isArray(that.seqExpr.type)) {
            // Bound-for is implemented only over sequences, convert the nativearray to a sequence
            typeToCheck = types.sequenceType(types.elemtype(that.seqExpr.type));
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
            JFXFunctionDefinition res = m.at(tree.pos).FunctionDefinition(tree.mods, tree.name, tree.getJFXReturnType(), tree.getParams(), body);
            res.operation.definition = res;
            res.sym = tree.sym;
            result = res.setType(tree.type);
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression meth = lowerFunctionName(tree.meth);
        List<Type> paramTypes = tree.meth.type.getParameterTypes();
        Symbol sym = JavafxTreeInfo.symbolFor(tree.meth);
        
        List<JFXExpression> args = List.nil();
        boolean pointer_Make = types.isSyntheticPointerFunction(sym);
        boolean builtins_Func = types.isSyntheticBuiltinsFunction(sym);
        if (pointer_Make || builtins_Func) {
                JFXExpression varExpr = lowerExpr(tree.args.head);
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
    //where
    private JFXExpression lowerFunctionName(JFXExpression meth) {
        Symbol msym = JavafxTreeInfo.symbolFor(meth);
        if (meth.getFXTag() == JavafxTag.IDENT) {
            return m.at(meth.pos()).Ident(msym).setType(meth.type);
        } else if (meth.getFXTag() == JavafxTag.SELECT) {
            return lowerSelect((JFXSelect)meth);
        } else {
            return lowerExpr(meth);
        }
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
            result = m.at(tree.pos).Conditional(cond, truePart, falsePart);
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
        JFXVar varDef = makeVar(tree.pos(), defs.resNamePrefix(), null, treeType);
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
        JFXExpression expr = lowerExpr(tree.getExpression());
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
        result = lowerExpr(res);
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
        result = lowerExpr(res);
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
        JFXOnReplace onReplace = lowerDecl(tree.getOnReplace());
        JFXOnReplace onInvalidate = lowerDecl(tree.getOnInvalidate());
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
        JFXExpression seq = lowerExpr(that.getSequence());
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
            flatten(lowerExpr(item, typeToCheck), buf);
        }
        result = buf.length() == 1 && types.isSequence(buf.toList().head.type) ?
            buf.toList().head :
            m.at(that.pos).ExplicitSequence(buf.toList()).setType(that.type);
    }
    //where
    private void flatten(JFXExpression item, ListBuffer<JFXExpression> items) {
        if (item.getFXTag() == JavafxTag.SEQUENCE_EXPLICIT) {
            JFXSequenceExplicit nestedSeq = (JFXSequenceExplicit)item;
            for (JFXExpression nestedItem : nestedSeq.getItems()) {
                flatten(nestedItem, items);
            }
        }
        else {
            items.append(item);
        }
    }
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        JFXExpression index = lowerExpr(that.getIndex(), syms.intType);
        JFXExpression seq = lowerExpr(that.getSequence());
        result = m.at(that.pos).SequenceIndexed(seq, index).setType(that.type);
    }

    public void visitSequenceInsert(JFXSequenceInsert that) {
        JFXExpression seq = lowerExpr(that.getSequence());
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
        result = res.setType(that.type);
    }

    public void visitSequenceSlice(JFXSequenceSlice that) {
        JFXExpression seq = lowerExpr(that.getSequence());
        JFXExpression start = lowerExpr(that.getFirstIndex(), syms.intType);
        JFXExpression end = lowerExpr(that.getLastIndex(), syms.intType);
        result = m.at(that.pos).SequenceSlice(seq, start, end, that.getEndKind()).setType(that.type);
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = lowerExprs(tree.parts);
        result = m.at(tree.pos).StringExpression(parts, tree.translationKey).setType(tree.type);
    }

    public void visitUnary(JFXUnary tree) {
        if (tree.getFXTag().isIncDec()) {
            result = lowerNumericUnary(tree);
        } else {
            JFXExpression arg = tree.getFXTag() == JavafxTag.REVERSE ?
                lowerExpr(tree.getExpression(), tree.type) :
                tree.getOperator() != null ?
                    lowerExpr(tree.getExpression(), tree.getOperator().type.getParameterTypes().head) :
                    lowerExpr(tree.getExpression());
            JFXUnary res = m.at(tree.pos).Unary(tree.getFXTag(), arg);
            res.operator = tree.operator;
            res.type = tree.type;
            result = res;
        }
    }

    private JFXExpression lowerNumericUnary(JFXUnary tree) {
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
            JFXVar varDef = makeVar(tree.pos(), defs.indexNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
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
                JFXVar varDef = makeVar(tree.pos(), defs.exprNamePrefix(), selected, selected.type);
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
        JFXExpression oldVal = varRef;
        boolean needOldValue = postFix && (
                pt != Type.noType ||
                mode == LowerMode.EXPRESSION);
        if (needOldValue) {
            JFXVar oldValDef = makeVar(tree.pos(), defs.oldValueNamePrefix(), varRef, varRef.type);
            stats.append(oldValDef);

            oldVal = m.at(tree.pos).Ident(oldValDef.sym);
            ((JFXIdent)oldVal).sym = oldValDef.sym;
        }

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
        if (needOldValue) {
            stats.append(incDecStat);
            blockValue = oldVal;
        }

        JFXExpression res = stats.nonEmpty() ?
            m.at(tree.pos).Block(0L, stats.toList(), blockValue).setType(opType) :
            blockValue;
        return lowerExpr(res, Type.noType);
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
            default: throw new AssertionError("Unexpected unary operator tag: " + tag);
        }
    }

    public void visitVar(JFXVar tree) {
        JFXExpression init = lowerExpr(tree.getInitializer(), tree.type);
        JFXOnReplace onReplace = lowerDecl(tree.getOnReplace());
        JFXOnReplace onInvalidate = lowerDecl(tree.getOnInvalidate());
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
        List<JFXExpression> loweredStats = lowerBlockStatements(stats);
        JFXExpression loweredValue = value != null ?
                lowerExpr(value, pt) :
            null;

        if (value != null && pt == syms.voidType) {
            List<JFXExpression> mergedLoweredValue =
                    mergeLoweredBlockStatements(
                        new ListBuffer<JFXExpression>(),
                        loweredValue,
                        value).toList();
            while (mergedLoweredValue.tail.nonEmpty()) {
                loweredStats = loweredStats.append(mergedLoweredValue.head);
                mergedLoweredValue = mergedLoweredValue.tail;
            }
            loweredValue = mergedLoweredValue.head;
        }

        JFXBlock res = m.at(tree.pos).Block(tree.flags, loweredStats, loweredValue);
        res.endpos = tree.endpos;
        result = res;
        result.type = value != null ?
            loweredValue != null ? loweredValue.type : syms.voidType :
            tree.type;
    }

    private List<JFXExpression> lowerBlockStatements(List<JFXExpression> stats) {
        ListBuffer<JFXExpression> loweredStats = ListBuffer.lb();
        for (JFXExpression stat : stats) {
            mergeLoweredBlockStatements(loweredStats, lowerStmt(stat), stat);
        }
        return loweredStats.toList();
    }

    private ListBuffer<JFXExpression> mergeLoweredBlockStatements(ListBuffer<JFXExpression> loweredStats, JFXExpression loweredStat, JFXExpression stat) {
        loweredStats.append(loweredStat);
        return loweredStats;
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
            default: return m.Literal(TypeTags.BOT, null).setType(syms.botType);
        }
    }

    public void visitBreak(JFXBreak tree) {
        result = tree;
    }

    public void visitCatch(JFXCatch tree) {
        JFXBlock body = lowerExpr(tree.body);
        result = m.at(tree.pos).Catch(tree.param, body).setType(tree.type);
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        Symbol prevClass = currentClass;
        try {
            currentClass = tree.sym;
            List<JFXTree> cdefs = lowerDecls(tree.getMembers());
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
        JFXForExpressionInClause clause = lowerDecl(tree.getForExpressionInClauses().head);
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
            boolean inWhile = false;
            int synthNameCount = 0;

            private Name newLabelName() {
                return names.fromString(JavafxDefs.synthForLabelPrefix + forClauseMap.size() + "$" + synthNameCount++);
            }

            @Override
            public void visitWhileLoop(JFXWhileLoop tree) {
                boolean prevInWhile = inWhile;
                try {
                    inWhile = true;
                    super.visitWhileLoop(tree);
                }
                finally {
                    inWhile = prevInWhile;
                }
            }

            @Override
            public void visitBreak(JFXBreak tree) {
                tree.label = (tree.label == null && !inWhile) ?
                    targetLabel :
                    tree.label;
            }

            @Override
            public void visitContinue(JFXContinue tree) {
                tree.label = (tree.label == null && !inWhile) ?
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
        if (tree.sym.kind == Kinds.MTH) {
            result = toFunctionValue(tree, false);
        }
        else {
            result = tree;
        }
    }

    JFXExpression toFunctionValue(JFXExpression tree, boolean isSelect) {
        boolean needsReceiverVar = isSelect;
        if (isSelect) {
             JFXSelect qualId = (JFXSelect)tree;
             Symbol selectedSym = JavafxTreeInfo.symbolFor(qualId.selected);
             if (selectedSym != null && selectedSym.kind == Kinds.TYP) {
                 needsReceiverVar = false;
             }
        }
        MethodSymbol msym = (MethodSymbol)JavafxTreeInfo.symbolFor(tree);
        Type mtype = msym.type;
        ListBuffer<JFXVar> params = ListBuffer.lb();
        ListBuffer<JFXExpression> args = ListBuffer.lb();
        MethodSymbol lambdaSym = new MethodSymbol(Flags.SYNTHETIC, defs.lambda_MethodName, mtype, currentClass);
        int count = 0;
        for (Type t : mtype.getParameterTypes()) {
            Name paramName = tempName("x"+count);
            JavafxVarSymbol paramSym = new JavafxVarSymbol(types, names, Flags.PARAMETER, paramName, t, lambdaSym);
            JFXVar param = m.at(tree.pos).Param(paramName, preTrans.makeTypeTree(t));
            param.sym = paramSym;
            param.type = t;
            params.append(param);
            JFXIdent arg = m.at(tree.pos).Ident(param);
            arg.type = param.type;
            arg.sym = param.sym;
            args.append(arg);
            count++;
        }
        Type returnType = mtype.getReturnType();
        JFXVar receiverVar = null;
        JFXExpression meth = tree.setType(mtype);
        if (needsReceiverVar) {
            JFXSelect qualId= (JFXSelect)tree;
            receiverVar = makeVar(tree.pos(), "rec", qualId.selected, qualId.selected.type);
            JFXIdent receiverVarRef = (JFXIdent)m.at(tree.pos).Ident(receiverVar.sym).setType(receiverVar.type);
            meth = m.at(tree.pos).Select(receiverVarRef, msym).setType(mtype);
        }
        JFXExpression call = m.at(tree.pos).Apply(List.<JFXExpression>nil(), meth, args.toList()).setType(returnType);
        JFXBlock body = (JFXBlock)m.at(tree.pos).Block(0, List.<JFXExpression>nil(), call).setType(returnType);
        JFXFunctionValue funcValue = m.at(tree.pos).FunctionValue(preTrans.makeTypeTree(returnType),
                params.toList(), body);
        funcValue.type = syms.makeFunctionType((Type.MethodType)mtype);
        funcValue.definition = new JFXFunctionDefinition(
                m.at(tree.pos).Modifiers(lambdaSym.flags_field),
                lambdaSym.name,
                funcValue);
        funcValue.definition.pos = tree.pos;
        funcValue.definition.sym = lambdaSym;
        funcValue.definition.type = lambdaSym.type;
        if (needsReceiverVar) {
            JFXBinary eqNull = (JFXBinary)m.at(tree.pos).Binary(
                    JavafxTag.EQ,
                    m.at(tree.pos).Ident(receiverVar.sym).setType(receiverVar.type),
                    m.at(tree.pos).Literal(TypeTags.BOT, null).setType(syms.botType));
            eqNull.operator = rs.resolveBinaryOperator(tree.pos(), JavafxTag.EQ, env, syms.objectType, syms.objectType);
            eqNull.setType(syms.booleanType);
            JFXExpression blockValue = m.at(tree.pos()).Conditional(
                    eqNull,
                    m.at(tree.pos).Literal(TypeTags.BOT, null).setType(syms.botType),
                    funcValue).setType(funcValue.type);
            return m.at(tree.pos).Block(0,
                    List.<JFXExpression>of(receiverVar),
                    blockValue).setType(funcValue.type);
        }
        else {
            return funcValue;
        }
    }

    public void visitImport(JFXImport tree) {
        result = tree;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        JFXBlock body = lowerExpr(tree.body);
        JFXInitDefinition res = m.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    /*
     * Determine if the expression uses any names that could clash with names in the class
     */
    private boolean hasNameConflicts(final TypeSymbol csym, final JFXExpression expr) {
        class NameClashScanner extends JavafxTreeScanner {

            boolean clashFound = false;

            //TODO: utterly naive -- add visibility testing
            void checkForClash(Name name) {
                Scope.Entry e = csym.members().lookup(name);
                if (e.scope != null) {
                    clashFound = true;
                }
            }

            @Override
            public void visitIdent(JFXIdent tree) {
                checkForClash(tree.getName());
            }
        }
        NameClashScanner ncs = new NameClashScanner();
        ncs.scan(expr);
        boolean clashFound = ncs.clashFound;
        // if (clashFound) System.err.println("Name clash found: " + csym + ", expr: " + expr);
        return clashFound;
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
                locals.append(lowerDecl(var));
            }
        }

        ListBuffer<JFXTree> newOverrides = ListBuffer.<JFXTree>lb();
        ListBuffer<JFXObjectLiteralPart> unboundParts = ListBuffer.<JFXObjectLiteralPart>lb();

        // Determine if there is a mutable non-explicitly bound initializer in a bound object literal,
        // since this could cause the instance to be re-created so binds then need to be external so
        // that they can be re-used (thus, won't, for example, create new objects)
        boolean holdBindsOutsideSubclass = false;
        if (tree.isBound()) {
            for (JFXObjectLiteralPart part : tree.getParts()) {
                if (!part.isExplicitlyBound() && !preTrans.isImmutable(part.getExpression())) {
                    // A bound object literal with non-explicitly bound initializer
                    // requires continuity of binds
                    holdBindsOutsideSubclass = true;
                    break;
                }
            }
        }


        for (JFXObjectLiteralPart part : tree.getParts()) {
            if (part.isExplicitlyBound()) {
                m.at(part.pos());  // create at part position

                // id for the override
                JFXIdent id = m.Ident(part.name);
                id.sym = part.sym;
                id.type = part.sym.type;

                JFXExpression partExpr = part.getExpression();

                JFXExpression initExpr;

                // Determine if bound object literal initializer should be scoped to object literal level.
                if (true || (holdBindsOutsideSubclass && preTrans.hasSideEffectsInBind(partExpr)) || hasNameConflicts(tree.type.tsym, partExpr)) {
                    // Shread the expression outside the class, so that the context is correct
                    // The variable should be marked as script private as it shouldn't
                    // be accessible from outside.

                    JFXVar shred = makeVar(
                            part.pos(),
                            Flags.SYNTHETIC | JavafxFlags.SCRIPT_PRIVATE,
                            part.name + "$ol",
                            part.getBindStatus(),
                            lowerExpr(partExpr, part.sym.type),
                            part.sym.type);
                    JFXIdent sid = m.Ident(shred.name);
                    sid.sym = shred.sym;
                    sid.type = part.sym.type;
                    locals.append(shred);
                    initExpr = sid;
                } else {
                    initExpr = partExpr; // lowered with class
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
                unboundParts.append(lowerExpr(part));
            }
        }

        // Lower the class.  If there are new overrides, fold them into the class first
        JFXClassDeclaration cdecl = tree.getClassBody();
        JFXClassDeclaration lowCdecl;
        if (newOverrides.nonEmpty()) {
            cdecl.setMembers(cdecl.getMembers().appendList(newOverrides));
            lowCdecl = lowerDecl(cdecl);
            preTrans.liftTypes(cdecl, cdecl.type, preTrans.makeDummyMethodSymbol(cdecl.sym));
        } else {
            lowCdecl = lowerDecl(cdecl);
        }

        // Construct the new instanciate
        JFXInstanciate res = m.at(tree.pos).Instanciate(tree.getJavaFXKind(),
                tree.getIdentifier(),
                lowCdecl,
                lowerExprs(tree.getArgs()),
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
        JFXExpression expr = lowerExpr(tree.getVariable());
        result = m.at(tree.pos).Invalidate(expr).setType(tree.type);
    }

    public void visitModifiers(JFXModifiers tree) {
        result = tree;
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXBlock body = lowerExpr(tree.getBody());
        JFXOnReplace res = tree.getTriggerKind() == JFXOnReplace.Kind.ONREPLACE ?
                m.at(tree.pos).OnReplace(tree.getOldValue(), tree.getFirstIndex(), tree.getLastIndex(), tree.getNewElements(), body) :
                m.at(tree.pos).OnInvalidate(body);
        result = res.setType(tree.type);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = lowerExpr(tree.expr);
        result = m.at(tree.pos).Parens(expr).setType(tree.type);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        JFXBlock body = lowerExpr(tree.body);
        JFXPostInitDefinition res = m.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitScript(JFXScript tree) {
        varCount = 0;
        tree.defs = lowerDecls(tree.defs);
        result = tree;
    }

    public void visitSelect(JFXSelect tree) {
        result = (tree.sym.kind == Kinds.MTH) ?
            toFunctionValue(tree, true) :
            lowerSelect(tree);
    }

    private JFXExpression lowerSelect(JFXSelect tree) {
        JFXExpression res = null;
        if (tree.sym.isStatic() &&
                JavafxTreeInfo.symbolFor(tree.selected) != null &&
                JavafxTreeInfo.symbolFor(tree.selected).kind == Kinds.TYP) {
            res = m.at(tree.pos()).Ident(tree.sym);
        }
        else {
            JFXExpression selected = lowerExpr(tree.selected);
            res = (JFXSelect)m.Select(selected, tree.sym);
        }
        return res.setType(tree.type);
    }

    public void visitSkip(JFXSkip tree) {
        result = tree;
    }

    public void visitThrow(JFXThrow tree) {
        JFXExpression expr = lowerExpr(tree.getExpression());
        result = m.at(tree.pos).Throw(expr).setType(tree.type);
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = lowerExpr(tree.getBlock());
        List<JFXCatch> catches = lowerDecls(tree.catchers);
        JFXBlock finallyBlock = lowerExpr(tree.getFinallyBlock());
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
        JFXExpression body = lowerExpr(tree.getBody());
        // Standard form is that the body is a block-expression
        if(!(body instanceof JFXBlock)) {
            body = m.Block(0L, List.<JFXExpression>nil(), body);
        }
        body.setType(syms.voidType);
        result = m.at(tree.pos).WhileLoop(cond, body).setType(syms.voidType);
    }
}
