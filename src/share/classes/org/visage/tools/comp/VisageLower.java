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

package org.visage.tools.comp;

import org.visage.api.VisageBindStatus;

import org.visage.tools.code.VisageFlags;
import org.visage.tools.tree.*;
import org.visage.tools.code.VisageTypes;
import org.visage.tools.code.VisageSymtab;
import org.visage.tools.code.VisageVarSymbol;
import org.visage.tools.tree.VisageExpression;

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
public class VisageLower implements VisageVisitor {
    protected static final Context.Key<VisageLower> convertTypesKey =
            new Context.Key<VisageLower>();

    private VisagePreTranslationSupport preTrans;
    private VisageTypes types;
    private VisageResolve rs;
    private VisageSymtab syms;
    private VisageTreeMaker m;
    private VisageDefs defs;
    private Type pt;
    private LowerMode mode;
    private Map<VisageForExpressionInClause, VisageForExpressionInClause> forClauseMap; //TODO this should be refactord into a common translation support
    private int synthNameCount = 0;
    private VisageEnv<VisageAttrContext> env;
    private VisageTree enclFunc;
    private VisageTree result;
    private Name.Table names;
    private Symbol currentClass;
    private int varCount;

    enum LowerMode {
        EXPRESSION,
        STATEMENT,
        DECLARATION;
    }

    public static VisageLower instance(Context context) {
        VisageLower instance = context.get(convertTypesKey);
        if (instance == null)
            instance = new VisageLower(context);
        return instance;
    }

    VisageLower(Context context) {
        context.put(convertTypesKey, this);
        preTrans = VisagePreTranslationSupport.instance(context);
        types = VisageTypes.instance(context);
        syms = (VisageSymtab)VisageSymtab.instance(context);
        m = VisageTreeMaker.instance(context);
        rs = VisageResolve.instance(context);
        names = Name.Table.instance(context);
        defs = VisageDefs.instance(context);
        forClauseMap = new HashMap<VisageForExpressionInClause, VisageForExpressionInClause>();
    }

    public VisageTree lower(VisageEnv<VisageAttrContext> attrEnv) {
        this.env = attrEnv;
        attrEnv.toplevel = lowerDecl(attrEnv.toplevel);
        //System.out.println(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    <T extends VisageTree> T lower(T tree, Type pt, LowerMode mode) {
        Type prevPt = this.pt;
        LowerMode prevMode = this.mode;
        try {
            this.pt = pt;
            this.mode = mode;
            if (tree != null) {
                tree.accept(this);
                return (T)(mode == LowerMode.EXPRESSION ?
                    convertTree((VisageExpression)result, this.pt) :
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

    
    public <T extends VisageTree> T lowerExpr(T tree) {
        return lower(tree, Type.noType, LowerMode.EXPRESSION);
    }

    public <T extends VisageTree> T lowerExpr(T tree, Type pt) {
        return lower(tree, pt, LowerMode.EXPRESSION);
    }

    public <T extends VisageTree> T lowerStmt(T tree) {
        return lower(tree, Type.noType, LowerMode.STATEMENT);
    }

    public <T extends VisageTree> T lowerDecl(T tree) {
        return lower(tree, Type.noType, LowerMode.DECLARATION);
    }


    <T extends VisageTree> List<T> lower(List<T> trees, LowerMode mode) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(lower(tree, Type.noType, mode));
        }
        return buf.toList();
    }

    public <T extends VisageExpression> List<T> lowerExprs(List<? extends T> trees, List<Type> pts) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(lowerExpr(tree, pts.head));
            pts = pts.tail;
        }
        return buf.toList();
    }

    public <T extends VisageTree> List<T> lowerExprs(List<T> trees) {
        return lower(trees, LowerMode.EXPRESSION);
    }
    
    public <T extends VisageTree> List<T> lowerDecls(List<T> trees) {
        return lower(trees, LowerMode.DECLARATION);
    }

    public <T extends VisageTree> List<T> lowerStats(List<T> trees) {
        return lower(trees, LowerMode.STATEMENT);
    }

    VisageExpression convertTree(VisageExpression tree, Type type) {
        if (type == Type.noType) return tree;
        return tree = needSequenceConversion(tree, type) ?
            toSequence(tree, type) :
            preTrans.makeCastIfNeeded(tree, type);
    }

    private boolean needSequenceConversion(VisageExpression tree, Type type) {
        return (types.isSequence(type) &&
            ((!types.isSequence(tree.type) &&
            tree.type != syms.unreachableType &&
            !types.isArray(tree.type)) ||
            isNull(tree)));
    }

    private boolean isNull(VisageTree tree) {
        return (tree.getVisageTag() == VisageTag.LITERAL &&
                ((VisageLiteral)tree).value == null);
    }

    private VisageExpression toSequence(VisageExpression tree, Type type) {
        VisageExpression seqExpr = null;
        if (isNull(tree)) {
            seqExpr = m.at(tree.pos).EmptySequence().setType(type);
        }
        else if (types.isSameType(tree.type, syms.objectType) &&
                types.isSubtypeUnchecked(syms.visage_SequenceTypeErasure, type)) { //synthetic call
            MethodSymbol msym = (MethodSymbol)rs.findIdentInType(env, syms.visage_SequencesType, defs.Sequences_convertObjectToSequence.methodName, Kinds.MTH);
            VisageExpression sequencesType = m.at(tree.pos).Type(syms.visage_SequencesType).setType(syms.visage_SequencesType);
            VisageTreeInfo.setSymbol(sequencesType, syms.visage_SequencesType.tsym);
            VisageIdent convertMeth = m.at(tree.pos).Ident(defs.Sequences_convertObjectToSequence.methodName);
            convertMeth.sym = msym;
            convertMeth.type = msym.type;
            seqExpr = m.at(tree.pos).Apply(List.<VisageExpression>nil(), convertMeth, List.of(tree)).setType(msym.type.getReturnType());

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

    private VisageVar makeVar(DiagnosticPosition diagPos, String name, VisageExpression init, Type type) {
        return makeVar(diagPos, 0L, name, VisageBindStatus.UNBOUND, init, type);
    }
    
    private VisageVar makeVar(DiagnosticPosition diagPos, long flags, String name, VisageBindStatus bindStatus, VisageExpression init, Type type) {
        VisageVarSymbol vsym = new VisageVarSymbol(types, names, flags, tempName(name), types.normalize(type), preTrans.makeDummyMethodSymbol(currentClass));
        return makeVar(diagPos, vsym, bindStatus, init);
    }

    private VisageVar makeVar(DiagnosticPosition diagPos, VisageVarSymbol vSym, VisageBindStatus bindStatus, VisageExpression init) {
        VisageModifiers mod = m.at(diagPos).Modifiers(vSym.flags());
        VisageType visageType = preTrans.makeTypeTree(vSym.type);
        VisageVar v = m.at(diagPos).Var(vSym.name, visageType, mod, init, bindStatus, null, null);
        v.sym = vSym;
        v.type = vSym.type;
        return v;
    }

    public void visitAssign(VisageAssign tree) {
        if (tree.lhs.getVisageTag() == VisageTag.SEQUENCE_INDEXED &&
                types.isSequence(((VisageSequenceIndexed)tree.lhs).getSequence().type) &&
                (types.isSequence(tree.rhs.type) || types.isSameType(tree.rhs.type, syms.objectType))) {
            result = lowerSequenceIndexedAssign(tree.pos(), (VisageSequenceIndexed)tree.lhs, tree.rhs);
        }
        else {
            VisageExpression lhs = lowerExpr(tree.lhs);
            VisageExpression rhs = lowerExpr(tree.rhs, tree.lhs.type);
            result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
        }
    }

    VisageExpression lowerSequenceIndexedAssign(DiagnosticPosition pos, VisageSequenceIndexed indexed, VisageExpression val) {
        Type resType = indexed.getSequence().type;
        VisageVar indexVar = makeVar(pos, defs.posNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
        VisageIdent indexRef = m.at(pos).Ident(indexVar);
        VisageExpression lhs = m.SequenceSlice(indexed.getSequence(), indexRef, indexRef, VisageSequenceSlice.END_INCLUSIVE);
        lhs.setType(resType);
        VisageExpression assign = m.Assign(lhs, val).setType(resType);
        return lowerExpr(m.Block(0L, List.<VisageExpression>of(indexVar), assign).setType(resType));
    }

    public void visitAssignop(VisageAssignOp tree) {
        result = visitNumericAssignop(tree, types.isSameType(tree.lhs.type, syms.visage_DurationType)
                || types.isSameType(tree.lhs.type, syms.visage_LengthType)
                || types.isSameType(tree.lhs.type, syms.visage_AngleType)
                || types.isSameType(tree.lhs.type, syms.visage_ColorType));
    }
    //where
    private VisageExpression visitNumericAssignop(VisageAssignOp tree, boolean isSpecialLiteralOperation) {

        VisageTag opTag = tree.getNormalOperatorVisageTag();
        ListBuffer<VisageExpression> stats = ListBuffer.lb();

        //if the assignop operand is an indexed expression of the kind a.x[i]
        //then we need to cache the index value (not to recompute it twice).

        VisageExpression lhs = tree.lhs;
        VisageIdent index = null;
        
        if (tree.lhs.getVisageTag() == VisageTag.SEQUENCE_INDEXED) {
            VisageSequenceIndexed indexed = (VisageSequenceIndexed)tree.lhs;
            VisageVar varDef = makeVar(tree.pos(), defs.indexNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
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

        VisageIdent selector = null;

        if (lhs.getVisageTag() == VisageTag.SELECT) {
            VisageExpression selected = ((VisageSelect)lhs).selected;
            // But, if this select is ClassName.foo, then we don't want
            // to create "var $expr = a;"
            Symbol sym = VisageTreeInfo.symbolFor(selected);
            if (sym == null || sym.kind != Kinds.TYP) {
                VisageVar varDef = makeVar(tree.pos(), defs.exprNamePrefix(), selected, selected.type);
                selector = m.at(tree.pos).Ident(varDef.sym);
                selector.sym = varDef.sym;
                selector.type = varDef.type;
                stats.append(varDef);
            }
        }

        VisageExpression varRef = lhs;

        //create a reference to the cached var. The translated expression
        //depends on whether the operand is a select or not:
        //
        //(SELECT)  $expr$.x;
        //(IDENT)   x;

        if (selector != null) {
            VisageVarSymbol vsym = (VisageVarSymbol)VisageTreeInfo.symbol(lhs);
            varRef = m.at(tree.pos).Select(selector, vsym, false);
            ((VisageSelect)varRef).sym = vsym;
        }

        if (index != null) {
            varRef = m.at(tree.pos).SequenceIndexed(varRef, index).setType(tree.lhs.type);
        }

        //Generate the binary expression this assignop translates to
        VisageExpression op = null;

        if (isSpecialLiteralOperation) {
            //special literal assignop (duration, length, angle, or color)
            //
            //(SELECT) $expr$.x = $expr$.x.[add/sub/mul/div](lhs);
            //(IDENT)  x = x.[add/sub/mul/div](lhs);
            VisageSelect meth = (VisageSelect)m.at(tree.pos).Select(varRef, tree.operator.name, false);
            meth.setType(tree.operator.type);
            meth.sym = tree.operator;
            op = m.at(tree.pos).Apply(List.<VisageExpression>nil(), meth, List.of(tree.rhs));
            op.setType(tree.type);
        } else {
            //numeric assignop
            //
            //(SELECT) $expr$.x = $expr$.x [+|-|*|/] lhs;
            //(IDENT)  x = $expr$.x [+|-|*|/] lhs;
            op = m.at(tree.pos).Binary(opTag, varRef, tree.rhs);
            ((VisageBinary)op).operator = tree.operator;
            op.setType(tree.operator.type.asMethodType().getReturnType());
        }
        VisageExpression assignOpStat = (VisageExpression)m.at(tree.pos).Assign(varRef, op).setType(op.type);

        VisageExpression res = stats.nonEmpty() ?
            m.at(tree.pos).Block(0L, stats.toList(), assignOpStat).setType(op.type) :
            assignOpStat;
        return lowerExpr(res, Type.noType);
    }

    public void visitBinary(VisageBinary tree) {
        boolean isSpecialLiteralBinaryExpr = tree.operator == null;
        boolean isEqualExpr = (tree.getVisageTag() == VisageTag.EQ ||
                tree.getVisageTag() == VisageTag.NE);
        boolean isSequenceOp = types.isSequence(tree.lhs.type) ||
                types.isSequence(tree.rhs.type);
        boolean isBoxedOp = (tree.lhs.type.isPrimitive() && !tree.rhs.type.isPrimitive()) ||
                (tree.rhs.type.isPrimitive() && !tree.lhs.type.isPrimitive());
        Type lhsType = tree.lhs.type;
        Type rhsType = tree.rhs.type;
        if (!isSpecialLiteralBinaryExpr) {
            lhsType = isSequenceOp && isEqualExpr ?
                types.sequenceType(tree.operator.type.getParameterTypes().head) :
                tree.operator.type.getParameterTypes().head;
            rhsType = isSequenceOp && isEqualExpr ?
                types.sequenceType(tree.operator.type.getParameterTypes().tail.head) :
                tree.operator.type.getParameterTypes().tail.head;
        }
        VisageExpression lhs = isEqualExpr && isBoxedOp && !isSequenceOp ?
            lowerExpr(tree.lhs) :
            lowerExpr(tree.lhs, lhsType);
        VisageExpression rhs = isEqualExpr && isBoxedOp && !isSequenceOp ?
            lowerExpr(tree.rhs) :
            lowerExpr(tree.rhs, rhsType);
        VisageBinary res = m.at(tree.pos).Binary(tree.getVisageTag(), lhs, rhs);
        res.operator = tree.operator;
        result = res.setType(tree.type);
    }

    public void visitForExpressionInClause(VisageForExpressionInClause that) {
        VisageExpression whereExpr = lowerExpr(that.getWhereExpression());
        Type typeToCheck = that.seqExpr.type;
        if  (that.seqExpr.type.tag == TypeTags.BOT ||
                types.isSameType(that.seqExpr.type, syms.visage_EmptySequenceType)) {
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
        VisageExpression seqExpr = lowerExpr(that.seqExpr, typeToCheck);
        VisageForExpressionInClause res = m.at(that.pos).InClause(that.getVar(), seqExpr, whereExpr);
        res.setIndexUsed(that.getIndexUsed());
        res.indexVarSym = that.indexVarSym;
        forClauseMap.put(that, res);
        result = res.setType(that.type);
    }

    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        VisageTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            VisageBlock body  = (VisageBlock)lowerExpr(tree.getBodyExpression(), tree.type != null ? tree.type.getReturnType() : Type.noType);
            VisageFunctionDefinition res = m.at(tree.pos).FunctionDefinition(tree.mods, tree.name, tree.getVisageReturnType(), tree.getParams(), body);
            res.operation.definition = res;
            res.sym = tree.sym;
            result = res.setType(tree.type);
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        VisageExpression meth = lowerFunctionName(tree.meth);
        List<Type> paramTypes = tree.meth.type.getParameterTypes();
        Symbol sym = VisageTreeInfo.symbolFor(tree.meth);
        
        List<VisageExpression> args = List.nil();
        boolean pointer_Make = types.isSyntheticPointerFunction(sym);
        boolean builtins_Func = types.isSyntheticBuiltinsFunction(sym);
        if (pointer_Make || builtins_Func) {
                VisageExpression varExpr = lowerExpr(tree.args.head);
                ListBuffer<VisageExpression> syntheticArgs = ListBuffer.lb();
                syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, VisageVarRef.RefKind.INST).setType(syms.visage_ObjectType));
                
                if (varExpr.getVisageTag() == VisageTag.IDENT && ((VisageIdent)varExpr).getName().equals(names._this)) {
                    syntheticArgs.append(m.at(tree.pos).LiteralInteger("-1", 10).setType(syms.intType));
                } else {
                    syntheticArgs.append(m.at(tree.pos).VarRef(varExpr, VisageVarRef.RefKind.VARNUM).setType(syms.intType));
                }
                
                Symbol msym = builtins_Func ?
                    preTrans.makeSyntheticBuiltinsMethod(sym.name) :
                    preTrans.makeSyntheticPointerMake();
                VisageTreeInfo.setSymbol(meth, msym);
                meth.type = msym.type;
                args = syntheticArgs.toList();
        }
        else if (sym instanceof MethodSymbol &&
                ((MethodSymbol)sym).isVarArgs()) {
            List<VisageExpression> actuals = tree.args;
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
    private VisageExpression lowerFunctionName(VisageExpression meth) {
        Symbol msym = VisageTreeInfo.symbolFor(meth);
        if (meth.getVisageTag() == VisageTag.IDENT) {
            return m.at(meth.pos()).Ident(msym).setType(meth.type);
        } else if (meth.getVisageTag() == VisageTag.SELECT) {
            return lowerSelect((VisageSelect)meth);
        } else {
            return lowerExpr(meth);
        }
    }

    public void visitFunctionValue(VisageFunctionValue tree) {
        VisageTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            tree.bodyExpression = (VisageBlock)lowerExpr(tree.bodyExpression, tree.type.getReturnType());
            result = tree;
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    public void visitIfExpression(VisageIfExpression tree) {
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
            VisageExpression cond = lowerExpr(tree.cond, syms.booleanType);
            VisageExpression truePart = lowerExpr(tree.truepart,
                    !nonSeqExpected || thenPartSeq ? tree.type : types.elementTypeOrType(tree.type));
            VisageExpression falsePart = lowerExpr(tree.falsepart,
                    !nonSeqExpected || elsePartSeq ? tree.type : types.elementTypeOrType(tree.type));
            result = m.at(tree.pos).Conditional(cond, truePart, falsePart);
            result.setType(nonSeqExpected ? syms.objectType : tree.type);
        }
    }

    public VisageTree lowerUnreachableIfExpression(VisageIfExpression tree) {
        boolean inverted = tree.truepart.type == syms.unreachableType;
        Type treeType = tree.type.tag == TypeTags.BOT ?
            types.isSequence(tree.type) ?
                types.sequenceType(syms.objectType) : syms.objectType :
            tree.type;
        VisageExpression truePart = lowerExpr(tree.truepart, treeType);
        VisageExpression falsePart = lowerExpr(tree.falsepart, treeType);
        VisageVar varDef = makeVar(tree.pos(), defs.resNamePrefix(), null, treeType);
        VisageIdent varRef = m.at(tree.pos).Ident(varDef.sym);
        varRef.sym = varDef.sym;
        varRef.type = varDef.type;

        VisageExpression assign = m.at(tree.pos).Assign(varRef, inverted ? falsePart : truePart).setType(syms.voidType); //we need void here!

        VisageExpression ifExpr = m.at(tree.pos).Conditional(tree.cond,
                inverted ? truePart : assign, inverted ? assign : falsePart).setType(syms.voidType); //we need void here!

        return m.at(tree.pos()).Block(0L, List.of(varDef, ifExpr), varRef).setType(varRef.type);
    }

    public void visitIndexof(VisageIndexof that) {
        VisageIndexof res = m.at(that.pos).Indexof(that.fname);
        res.clause = that.clause;
        result = res.setType(that.type);
    }

    public void visitInstanceOf(VisageInstanceOf tree) {
        VisageExpression expr = lowerExpr(tree.getExpression());
        result = m.at(tree.pos).TypeTest(expr, tree.clazz).setType(tree.type);
    }

    public void visitInterpolateValue(VisageInterpolateValue that) {
        VisageExpression pointerType = m.at(that.pos).Type(syms.visage_PointerType).setType(syms.visage_PointerType);
        Symbol pointerMakeSym = rs.resolveQualifiedMethod(that.pos(),
                env, syms.visage_PointerType,
                defs.Pointer_make.methodName,
                rs.newMethTemplate(List.of(syms.objectType),
                List.<Type>nil()));
        pointerMakeSym.flags_field |= VisageFlags.FUNC_POINTER_MAKE;
        VisageSelect pointerMake = (VisageSelect)m.at(that.pos).Select(pointerType, pointerMakeSym, false);
        pointerMake.sym = pointerMakeSym;
        VisageExpression pointerCall = m.at(that.pos).Apply(List.<VisageExpression>nil(),
                pointerMake,
                List.of(that.attribute)).setType(pointerMakeSym.type.getReturnType());
        ListBuffer<VisageTree> parts = ListBuffer.lb();
        parts.append(makeObjectLiteralPart(that.pos(), syms.visage_KeyValueType, defs.value_InterpolateMethodName, that.funcValue));
        parts.append(makeObjectLiteralPart(that.pos(), syms.visage_KeyValueType, defs.target_InterpolateMethodName, pointerCall));
        if (that.interpolation != null) {
            parts.append(makeObjectLiteralPart(that.pos(), syms.visage_KeyValueType, defs.interpolate_InterpolateMethodName, that.interpolation));
        }
        VisageExpression res = m.at(that.pos).ObjectLiteral(m.at(that.pos).Type(syms.visage_KeyValueType), parts.toList()).setType(syms.visage_KeyValueType);
        result = lowerExpr(res);
    }
    //where
    private VisageObjectLiteralPart makeObjectLiteralPart(DiagnosticPosition pos, Type site, Name varName, VisageExpression value) {
        VisageObjectLiteralPart part = m.at(pos).ObjectLiteralPart(varName, value, VisageBindStatus.UNBOUND);
        part.setType(value.type);
        part.sym = rs.findIdentInType(env, site, varName, Kinds.VAR);
        return part;
    }

    public void visitKeyFrameLiteral(VisageKeyFrameLiteral that) {
        ListBuffer<VisageTree> parts = ListBuffer.lb();
        VisageExpression keyValues = m.at(that.pos).ExplicitSequence(that.values).setType(types.sequenceType(syms.visage_KeyValueType));
        parts.append(makeObjectLiteralPart(that.pos(), syms.visage_KeyFrameType, defs.time_KeyFrameMethodName, that.start));
        parts.append(makeObjectLiteralPart(that.pos(), syms.visage_KeyFrameType, defs.values_KeyFrameMethodName, keyValues));
        VisageExpression res = m.at(that.pos).ObjectLiteral(m.at(that.pos).Type(syms.visage_KeyValueType), parts.toList()).setType(syms.visage_KeyFrameType);
        result = lowerExpr(res);
    }

    public void visitLiteral(VisageLiteral tree) {
        result = tree;
    }

    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        VisageExpression expr = lowerExpr(tree.getExpression(), tree.sym.type);
        VisageObjectLiteralPart res = m.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getExplicitBindStatus());
        res.markBound(tree.getBindStatus());
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        VisageExpression init = lowerExpr(tree.getInitializer(), tree.getId().sym.type);
        VisageOnReplace onReplace = lowerDecl(tree.getOnReplace());
        VisageOnReplace onInvalidate = lowerDecl(tree.getOnInvalidate());
        VisageOverrideClassVar res = m.at(tree.pos).OverrideClassVar(tree.name, tree.getVisageType(), tree.mods, tree.getId(), init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitReturn(VisageReturn tree) {
        Type typeToCheck = enclFunc.type != null ?
            enclFunc.type.getReturnType() :
            syms.objectType; //nedded because run function has null type
        VisageExpression retExpr = lowerExpr(tree.getExpression(), typeToCheck);
        result = m.at(tree.pos).Return(retExpr).setType(tree.type);
    }

    public void visitSequenceDelete(VisageSequenceDelete that) {
        VisageExpression seq = lowerExpr(that.getSequence());
        VisageExpression el = that.getElement();
        if (that.getElement() != null) {
            Type typeToCheck = types.isArrayOrSequenceType(that.getElement().type) ?
                    that.getSequence().type :
                    types.elementType(that.getSequence().type);
            el = lowerExpr(that.getElement(), typeToCheck);
        }
        result = m.at(that.pos).SequenceDelete(seq, el).setType(that.type);
    }

    public void visitSequenceEmpty(VisageSequenceEmpty that) {
        result = that;
    }

    public void visitSequenceExplicit(VisageSequenceExplicit that) {
        ListBuffer<VisageExpression> buf = ListBuffer.lb();
        for (VisageExpression item : that.getItems()) {
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
    private void flatten(VisageExpression item, ListBuffer<VisageExpression> items) {
        if (item.getVisageTag() == VisageTag.SEQUENCE_EXPLICIT) {
            VisageSequenceExplicit nestedSeq = (VisageSequenceExplicit)item;
            for (VisageExpression nestedItem : nestedSeq.getItems()) {
                flatten(nestedItem, items);
            }
        }
        else {
            items.append(item);
        }
    }
    public void visitSequenceIndexed(VisageSequenceIndexed that) {
        VisageExpression index = lowerExpr(that.getIndex(), syms.intType);
        VisageExpression seq = lowerExpr(that.getSequence());
        result = m.at(that.pos).SequenceIndexed(seq, index).setType(that.type);
    }

    public void visitSequenceInsert(VisageSequenceInsert that) {
        VisageExpression seq = lowerExpr(that.getSequence());
        Type typeToCheck = types.isArrayOrSequenceType(that.getElement().type) ||
                types.isSameType(syms.objectType, that.getElement().type) ?
                that.getSequence().type :
                types.elementType(that.getSequence().type);
        VisageExpression el = lowerExpr(that.getElement(), typeToCheck);
        VisageExpression pos = lowerExpr(that.getPosition(), syms.intType);
        result = m.at(that.pos).SequenceInsert(seq, el, pos, that.shouldInsertAfter()).setType(that.type);
    }

    public void visitSequenceRange(VisageSequenceRange that) {
        VisageExpression lower = lowerExpr(that.getLower(), types.elementType(that.type));
        VisageExpression upper = lowerExpr(that.getUpper(), types.elementType(that.type));
        VisageExpression step = lowerExpr(that.getStepOrNull(), types.elementType(that.type));
        VisageSequenceRange res = m.at(that.pos).RangeSequence(lower, upper, step, that.isExclusive());
        result = res.setType(that.type);
    }

    public void visitSequenceSlice(VisageSequenceSlice that) {
        VisageExpression seq = lowerExpr(that.getSequence());
        VisageExpression start = lowerExpr(that.getFirstIndex(), syms.intType);
        VisageExpression end = lowerExpr(that.getLastIndex(), syms.intType);
        result = m.at(that.pos).SequenceSlice(seq, start, end, that.getEndKind()).setType(that.type);
    }

    public void visitStringExpression(VisageStringExpression tree) {
        List<VisageExpression> parts = lowerExprs(tree.parts);
        result = m.at(tree.pos).StringExpression(parts, tree.translationKey).setType(tree.type);
    }

    public void visitUnary(VisageUnary tree) {
        if (tree.getVisageTag().isIncDec()) {
            result = lowerNumericUnary(tree);
        } else {
            VisageExpression arg = tree.getVisageTag() == VisageTag.REVERSE ?
                lowerExpr(tree.getExpression(), tree.type) :
                tree.getOperator() != null ?
                    lowerExpr(tree.getExpression(), tree.getOperator().type.getParameterTypes().head) :
                    lowerExpr(tree.getExpression());
            VisageUnary res = m.at(tree.pos).Unary(tree.getVisageTag(), arg);
            res.operator = tree.operator;
            res.type = tree.type;
            result = res;
        }
    }

    private VisageExpression lowerNumericUnary(VisageUnary tree) {
        boolean postFix = isPostfix(tree.getVisageTag());
        VisageTag opTag = unaryToBinaryOpTag(tree.getVisageTag());
        Type opType = types.unboxedTypeOrType(tree.getExpression().type);
        if (types.isSameType(opType, syms.charType)) {
            opType = syms.intType;
        }
        ListBuffer<VisageExpression> stats = ListBuffer.lb();

        //if the unary operand is an indexed expression of the kind a.x[i]
        //then we need to cache the index value (not to recumpute it twice).

        VisageExpression expr = tree.getExpression();
        VisageIdent index = null;

        if (tree.getExpression().getVisageTag() == VisageTag.SEQUENCE_INDEXED) {
            VisageSequenceIndexed indexed = (VisageSequenceIndexed)tree.getExpression();
            VisageVar varDef = makeVar(tree.pos(), defs.indexNamePrefix(), indexed.getIndex(), indexed.getIndex().type);
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
        VisageIdent selector = null;

        if (expr.getVisageTag() == VisageTag.SELECT) {
            VisageExpression selected = ((VisageSelect)expr).selected;
            Symbol sym = VisageTreeInfo.symbolFor(selected);
            // But, if this select is ClassName.foo, then we don't want
            // to create "var $expr = a;"
            if (sym == null || sym.kind != Kinds.TYP) {
                VisageVar varDef = makeVar(tree.pos(), defs.exprNamePrefix(), selected, selected.type);
                selector = m.at(tree.pos).Ident(varDef.sym);
                selector.sym = varDef.sym;
                selector.type = varDef.type;
                stats.append(varDef);
            }
        }

        VisageExpression varRef = expr;

        if (selector != null) {
            VisageVarSymbol vsym = (VisageVarSymbol)VisageTreeInfo.symbol(expr);
            varRef = m.at(tree.pos).Select(selector, vsym, false);
            ((VisageSelect)varRef).sym = vsym;
        }

        if (index != null) {
            varRef = m.at(tree.pos).SequenceIndexed(varRef, index).setType(tree.getExpression().type);
        }

        //cache the old value of the unary operand. The translated expression
        //depends on whether the operand is a select or not:
        //
        //(SELECT) var $oldVal$ = $expr$.x;
        //(IDENT)  var $oldVal$ = x;
        VisageExpression oldVal = varRef;
        boolean needOldValue = postFix && (
                pt != Type.noType ||
                mode == LowerMode.EXPRESSION);
        if (needOldValue) {
            VisageVar oldValDef = makeVar(tree.pos(), defs.oldValueNamePrefix(), varRef, varRef.type);
            stats.append(oldValDef);

            oldVal = m.at(tree.pos).Ident(oldValDef.sym);
            ((VisageIdent)oldVal).sym = oldValDef.sym;
        }

            //Generate the binary expression this unary translates to
            //
            //(SELECT) $expr$.x = $oldVal [+/-] 1;
            //(IDENT)  x = $oldVal [+/-] 1;

        VisageBinary binary = (VisageBinary)m.at(tree.pos).Binary(opTag, oldVal, m.at(tree.pos).Literal(opType.tag, 1).setType(opType));
        binary.operator = rs.resolveBinaryOperator(tree.pos(), opTag, env, opType, opType);
        binary.setType(binary.operator.type.asMethodType().getReturnType());
        VisageExpression incDecStat = (VisageExpression)m.at(tree.pos).Assign(varRef, binary).setType(opType);

        //If this is a postfix unary expression, the old value is returned
        VisageExpression blockValue = incDecStat;
        if (needOldValue) {
            stats.append(incDecStat);
            blockValue = oldVal;
        }

        VisageExpression res = stats.nonEmpty() ?
            m.at(tree.pos).Block(0L, stats.toList(), blockValue).setType(opType) :
            blockValue;
        return lowerExpr(res, Type.noType);
    }
    //where
    private VisageTag unaryToBinaryOpTag(VisageTag tag) {
        switch (tag) {
            case POSTINC:
            case PREINC: return VisageTag.PLUS;
            case POSTDEC:
            case PREDEC: return VisageTag.MINUS;
            default: throw new AssertionError("Unexpecetd unary operator tag: " + tag);
        }
    }
    //where
    private boolean isPostfix(VisageTag tag) {
        switch (tag) {
            case POSTINC:
            case POSTDEC: return true;
            case PREINC:
            case PREDEC: return false;
            default: throw new AssertionError("Unexpected unary operator tag: " + tag);
        }
    }

    public void visitVar(VisageVar tree) {
        VisageExpression init = lowerExpr(tree.getInitializer(), tree.type);
        VisageOnReplace onReplace = lowerDecl(tree.getOnReplace());
        VisageOnReplace onInvalidate = lowerDecl(tree.getOnInvalidate());
        VisageVar res = m.at(tree.pos).Var(tree.name, tree.getVisageType(), tree.mods, init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
        VisageVarInit vsi = tree.getVarInit();
        if (vsi != null) {
            // update the var in the var-init
            vsi.resetVar(res);
        }
    }

    public void visitVarInit(VisageVarInit tree) {
        result = tree;
    }

    public void visitVarRef(VisageVarRef tree) {
        result = tree;
    }

    public void visitBlockExpression(VisageBlock tree) {
        List<VisageExpression> stats = tree.stats;
        VisageExpression value = tree.value;
        if (value != null) {
            if (VisageTreeInfo.skipParens(value).getVisageTag() == VisageTag.VAR_DEF) {
                VisageVar varDef = (VisageVar)VisageTreeInfo.skipParens(value);
                VisageIdent varRef = m.at(tree.value.pos).Ident(varDef.sym);
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
        List<VisageExpression> loweredStats = lowerBlockStatements(stats);
        VisageExpression loweredValue = value != null ?
                lowerExpr(value, pt) :
            null;

        if (value != null && pt == syms.voidType) {
            List<VisageExpression> mergedLoweredValue =
                    mergeLoweredBlockStatements(
                        new ListBuffer<VisageExpression>(),
                        loweredValue,
                        value).toList();
            while (mergedLoweredValue.tail.nonEmpty()) {
                loweredStats = loweredStats.append(mergedLoweredValue.head);
                mergedLoweredValue = mergedLoweredValue.tail;
            }
            loweredValue = mergedLoweredValue.head;
        }

        VisageBlock res = m.at(tree.pos).Block(tree.flags, loweredStats, loweredValue);
        res.endpos = tree.endpos;
        result = res;
        result.type = value != null ?
            loweredValue != null ? loweredValue.type : syms.voidType :
            tree.type;
    }

    private List<VisageExpression> lowerBlockStatements(List<VisageExpression> stats) {
        ListBuffer<VisageExpression> loweredStats = ListBuffer.lb();
        for (VisageExpression stat : stats) {
            mergeLoweredBlockStatements(loweredStats, lowerStmt(stat), stat);
        }
        return loweredStats.toList();
    }

    private ListBuffer<VisageExpression> mergeLoweredBlockStatements(ListBuffer<VisageExpression> loweredStats, VisageExpression loweredStat, VisageExpression stat) {
        loweredStats.append(loweredStat);
        return loweredStats;
    }
    //where
    private VisageExpression makeDefaultValue(Type t) {
        switch (t.tag) {
            case TypeTags.BYTE: return m.Literal(TypeTags.BYTE, 0).setType(syms.byteType);
            case TypeTags.SHORT: return m.Literal(TypeTags.SHORT, 0).setType(syms.shortType);
            case TypeTags.INT: return m.Literal(TypeTags.INT, 0).setType(syms.intType);
            case TypeTags.FLOAT: return m.Literal(TypeTags.FLOAT, 0).setType(syms.floatType);
            case TypeTags.DOUBLE: return m.Literal(TypeTags.DOUBLE, 0).setType(syms.doubleType);
            case TypeTags.BOOLEAN: return m.Literal(TypeTags.BOOLEAN, 0).setType(syms.booleanType);
            case TypeTags.CLASS: {
                if (types.isSequence(t)) {
                    return m.EmptySequence().setType(syms.visage_EmptySequenceType);
                } else if (types.isSameType(t, syms.visage_StringType)) {
                    return m.Literal("").setType(syms.visage_StringType);
                } else if (types.isSameType(t, syms.visage_DurationType)) {
                    Name zeroName = names.fromString("ZERO");
                    VisageSelect res = (VisageSelect)m.Select(
                            preTrans.makeTypeTree(syms.visage_DurationType),
                            zeroName, false).setType(syms.visage_DurationType);
                    res.sym = rs.findIdentInType(env, syms.visage_DurationType, zeroName, Kinds.VAR);
                    return res;
                } else if (types.isSameType(t, syms.visage_LengthType)) {
                    Name zeroName = names.fromString("ZERO");
                    VisageSelect res = (VisageSelect)m.Select(
                            preTrans.makeTypeTree(syms.visage_LengthType),
                            zeroName, false).setType(syms.visage_LengthType);
                    res.sym = rs.findIdentInType(env, syms.visage_LengthType, zeroName, Kinds.VAR);
                    return res;
                } else if (types.isSameType(t, syms.visage_AngleType)) {
                    Name zeroName = names.fromString("ZERO");
                    VisageSelect res = (VisageSelect)m.Select(
                            preTrans.makeTypeTree(syms.visage_AngleType),
                            zeroName, false).setType(syms.visage_AngleType);
                    res.sym = rs.findIdentInType(env, syms.visage_AngleType, zeroName, Kinds.VAR);
                    return res;
                } else if (types.isSameType(t, syms.visage_ColorType)) {
                    Name blackName = names.fromString("BLACK");
                    VisageSelect res = (VisageSelect)m.Select(
                            preTrans.makeTypeTree(syms.visage_ColorType),
                            blackName, false).setType(syms.visage_ColorType);
                    res.sym = rs.findIdentInType(env, syms.visage_ColorType, blackName, Kinds.VAR);
                    return res;
                }
            }
            default: return m.Literal(TypeTags.BOT, null).setType(syms.botType);
        }
    }

    public void visitBreak(VisageBreak tree) {
        result = tree;
    }

    public void visitCatch(VisageCatch tree) {
        VisageBlock body = lowerExpr(tree.body);
        result = m.at(tree.pos).Catch(tree.param, body).setType(tree.type);
    }

    public void visitClassDeclaration(VisageClassDeclaration tree) {
        Symbol prevClass = currentClass;
        try {
            currentClass = tree.sym;
            List<VisageTree> cdefs = lowerDecls(tree.getMembers());
            tree.setMembers(cdefs);
            result = tree;
        }
        finally {
            currentClass = prevClass;
        }
    }

    public void visitContinue(VisageContinue tree) {
        result = tree;
    }

    public void visitErroneous(VisageErroneous tree) {
        result = tree;
    }

    public void visitForExpression(VisageForExpression tree) {
        result = lowerForExpression(tree);
        patchForLoop(result, tree.getForExpressionInClauses());
        for (VisageForExpressionInClause clause : tree.getForExpressionInClauses()) {
            forClauseMap.remove(clause);
        }
    }

    public VisageExpression lowerForExpression(VisageForExpression tree) {
        VisageForExpressionInClause clause = lowerDecl(tree.getForExpressionInClauses().head);
        VisageExpression body = tree.getBodyExpression();
        if (tree.getForExpressionInClauses().size() > 1) {
            // for (INCLAUSE(1), INCLAUSE(2), ... INCLAUSE(n)) BODY
            // (n>1) is lowered to:
            // for (INCLAUSE(1) Lower(for (INCLAUSE(2) (... for (INCLAUSE(n)) ... )) BODY
            VisageForExpression nestedFor = (VisageForExpression)m.ForExpression(tree.getForExpressionInClauses().tail, tree.bodyExpr).setType(tree.type);
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
        if(!(body instanceof VisageBlock)) {
            body = m.Block(0L, List.<VisageExpression>nil(), body).setType(body.type);
        }
        VisageForExpression res = m.at(tree.pos).ForExpression(List.of(clause), body);
        return (VisageForExpression)res.setType(tree.type);
    }

    private void patchForLoop(VisageTree forExpr, final List<VisageForExpressionInClause> clausesToPatch) {
        class ForLoopPatcher extends VisageTreeScanner {

            Name targetLabel;
            boolean inWhile = false;

            private Name newLabelName() {
                return names.fromString(VisageDefs.synthForLabelPrefix + forClauseMap.size() + "$" + synthNameCount++);
            }

            @Override
            public void visitWhileLoop(VisageWhileLoop tree) {
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
            public void visitBreak(VisageBreak tree) {
                tree.label = (tree.label == null && !inWhile) ?
                    targetLabel :
                    tree.label;
            }

            @Override
            public void visitContinue(VisageContinue tree) {
                tree.label = (tree.label == null && !inWhile) ?
                    targetLabel :
                    tree.label;
            }

            @Override
            public void visitIndexof(VisageIndexof tree) {
                tree.clause = clausesToPatch.contains(tree.clause) ?
                    forClauseMap.get(tree.clause) :
                    tree.clause;
            }

            @Override
            public void visitForExpressionInClause(VisageForExpressionInClause tree) {
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

    public void visitIdent(VisageIdent tree) {
        if (tree.sym.kind == Kinds.MTH) {
            result = toFunctionValue(tree, false);
        }
        else {
            result = tree;
        }
    }

    VisageExpression toFunctionValue(VisageExpression tree, boolean isSelect) {
        boolean needsReceiverVar = isSelect;
        if (isSelect) {
             VisageSelect qualId = (VisageSelect)tree;
             Symbol selectedSym = VisageTreeInfo.symbolFor(qualId.selected);
             if (selectedSym != null && selectedSym.kind == Kinds.TYP) {
                 needsReceiverVar = false;
             }
        }
        MethodSymbol msym = (MethodSymbol)VisageTreeInfo.symbolFor(tree);
        Type mtype = msym.type;
        ListBuffer<VisageVar> params = ListBuffer.lb();
        ListBuffer<VisageExpression> args = ListBuffer.lb();
        MethodSymbol lambdaSym = new MethodSymbol(Flags.SYNTHETIC, defs.lambda_MethodName, mtype, currentClass);
        int count = 0;
        for (Type t : mtype.getParameterTypes()) {
            Name paramName = tempName("x"+count);
            VisageVarSymbol paramSym = new VisageVarSymbol(types, names, Flags.PARAMETER, paramName, t, lambdaSym);
            VisageVar param = m.at(tree.pos).Param(paramName, preTrans.makeTypeTree(t));
            param.sym = paramSym;
            param.type = t;
            params.append(param);
            VisageIdent arg = m.at(tree.pos).Ident(param);
            arg.type = param.type;
            arg.sym = param.sym;
            args.append(arg);
            count++;
        }
        Type returnType = mtype.getReturnType();
        VisageVar receiverVar = null;
        VisageExpression meth = tree.setType(mtype);
        if (needsReceiverVar) {
            VisageSelect qualId= (VisageSelect)tree;
            receiverVar = makeVar(tree.pos(), "rec", qualId.selected, qualId.selected.type);
            VisageIdent receiverVarRef = (VisageIdent)m.at(tree.pos).Ident(receiverVar.sym).setType(receiverVar.type);
            meth = m.at(tree.pos).Select(receiverVarRef, msym, false).setType(mtype);
        }
        VisageExpression call = m.at(tree.pos).Apply(List.<VisageExpression>nil(), meth, args.toList()).setType(returnType);
        VisageBlock body = (VisageBlock)m.at(tree.pos).Block(0, List.<VisageExpression>nil(), call).setType(returnType);
        VisageFunctionValue funcValue = m.at(tree.pos).FunctionValue(preTrans.makeTypeTree(returnType),
                params.toList(), body);
        funcValue.type = syms.makeFunctionType((Type.MethodType)mtype);
        funcValue.definition = new VisageFunctionDefinition(
                m.at(tree.pos).Modifiers(lambdaSym.flags_field),
                lambdaSym.name,
                funcValue);
        funcValue.definition.pos = tree.pos;
        funcValue.definition.sym = lambdaSym;
        funcValue.definition.type = lambdaSym.type;
        if (needsReceiverVar) {
            VisageBinary eqNull = (VisageBinary)m.at(tree.pos).Binary(
                    VisageTag.EQ,
                    m.at(tree.pos).Ident(receiverVar.sym).setType(receiverVar.type),
                    m.at(tree.pos).Literal(TypeTags.BOT, null).setType(syms.botType));
            eqNull.operator = rs.resolveBinaryOperator(tree.pos(), VisageTag.EQ, env, syms.objectType, syms.objectType);
            eqNull.setType(syms.booleanType);
            VisageExpression blockValue = m.at(tree.pos()).Conditional(
                    eqNull,
                    m.at(tree.pos).Literal(TypeTags.BOT, null).setType(syms.botType),
                    funcValue).setType(funcValue.type);
            return m.at(tree.pos).Block(0,
                    List.<VisageExpression>of(receiverVar),
                    blockValue).setType(funcValue.type);
        }
        else {
            return funcValue;
        }
    }

    public void visitImport(VisageImport tree) {
        result = tree;
    }

    public void visitInitDefinition(VisageInitDefinition tree) {
        VisageBlock body = lowerExpr(tree.body);
        VisageInitDefinition res = m.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    /*
     * Determine if the expression uses any names that could clash with names in the class
     */
    private boolean hasNameConflicts(final TypeSymbol csym, final VisageExpression expr) {
        class NameClashScanner extends VisageTreeScanner {

            boolean clashFound = false;

            //TODO: utterly naive -- add visibility testing
            void checkForClash(Name name) {
                Scope.Entry e = csym.members().lookup(name);
                if (e.scope != null) {
                    clashFound = true;
                }
            }

            @Override
            public void visitIdent(VisageIdent tree) {
                checkForClash(tree.getName());
            }
        }
        NameClashScanner ncs = new NameClashScanner();
        ncs.scan(expr);
        boolean clashFound = ncs.clashFound;
        // if (clashFound) System.err.println("Name clash found: " + csym + ", expr: " + expr);
        return clashFound;
    }

    public void visitInstanciate(VisageInstanciate tree) {
        ListBuffer<VisageExpression> locals = ListBuffer.lb();
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
            for (VisageVar var : tree.getLocalvars()) {
                locals.append(lowerDecl(var));
            }
        }

        ListBuffer<VisageTree> newOverrides = ListBuffer.<VisageTree>lb();
        ListBuffer<VisageObjectLiteralPart> unboundParts = ListBuffer.<VisageObjectLiteralPart>lb();

        // Determine if there is a mutable non-explicitly bound initializer in a bound object literal,
        // since this could cause the instance to be re-created so binds then need to be external so
        // that they can be re-used (thus, won't, for example, create new objects)
        boolean holdBindsOutsideSubclass = false;
        if (tree.isBound()) {
            for (VisageObjectLiteralPart part : tree.getParts()) {
                if (!part.isExplicitlyBound() && !preTrans.isImmutable(part.getExpression())) {
                    // A bound object literal with non-explicitly bound initializer
                    // requires continuity of binds
                    holdBindsOutsideSubclass = true;
                    break;
                }
            }
        }


        for (VisageObjectLiteralPart part : tree.getParts()) {
            if (part.isExplicitlyBound()) {
                m.at(part.pos());  // create at part position

                // id for the override
                VisageIdent id = m.Ident(part.name);
                id.sym = part.sym;
                id.type = part.sym.type;

                VisageExpression partExpr = part.getExpression();

                VisageExpression initExpr;

                // Determine if bound object literal initializer should be scoped to object literal level.
                if ((holdBindsOutsideSubclass && preTrans.hasSideEffectsInBind(partExpr)) || hasNameConflicts(tree.type.tsym, partExpr)) {
                    // Shread the expression outside the class, so that the context is correct
                    // The variable should be marked as script private as it shouldn't
                    // be accessible from outside.

                    VisageVar shred = makeVar(
                            part.pos(),
                            Flags.SYNTHETIC | VisageFlags.SCRIPT_PRIVATE,
                            part.name + "$ol",
                            part.getBindStatus(),
                            lowerExpr(partExpr, part.sym.type),
                            part.sym.type);
                    VisageIdent sid = m.Ident(shred.name);
                    sid.sym = shred.sym;
                    sid.type = part.sym.type;
                    locals.append(shred);
                    initExpr = sid;
                } else {
                    initExpr = partExpr; // lowered with class
                }

                // Turn the part into an override var
                VisageOverrideClassVar ocv =
                        m.OverrideClassVar(
                        part.name,
                        preTrans.makeTypeTree(part.type),
                        m.Modifiers(part.sym.flags_field),
                        id,
                        initExpr,
                        part.getBindStatus(),
                        null,
                        null);
                ocv.sym = (VisageVarSymbol) part.sym;
                ocv.type = part.sym.type;
                newOverrides.append(ocv);
            } else {
                unboundParts.append(lowerExpr(part));
            }
        }

        // Lower the class.  If there are new overrides, fold them into the class first
        VisageClassDeclaration cdecl = tree.getClassBody();
        VisageClassDeclaration lowCdecl;
        if (newOverrides.nonEmpty()) {
            cdecl.setMembers(cdecl.getMembers().appendList(newOverrides));
            lowCdecl = lowerDecl(cdecl);
            preTrans.liftTypes(cdecl, cdecl.type, preTrans.makeDummyMethodSymbol(cdecl.sym));
        } else {
            lowCdecl = lowerDecl(cdecl);
        }

        // Construct the new instanciate
        VisageInstanciate res = m.at(tree.pos).Instanciate(tree.getVisageKind(),
                tree.getIdentifier(),
                lowCdecl,
                lowerExprs(tree.getArgs()),
                unboundParts.toList(),
                List.<VisageVar>nil());
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

    public void visitInvalidate(VisageInvalidate tree) {
        VisageExpression expr = lowerExpr(tree.getVariable());
        result = m.at(tree.pos).Invalidate(expr).setType(tree.type);
    }

    public void visitModifiers(VisageModifiers tree) {
        result = tree;
    }

    public void visitOnReplace(VisageOnReplace tree) {
        VisageBlock body = lowerExpr(tree.getBody());
        VisageOnReplace res = tree.getTriggerKind() == VisageOnReplace.Kind.ONREPLACE ?
                m.at(tree.pos).OnReplace(tree.getOldValue(), tree.getFirstIndex(), tree.getLastIndex(), tree.getNewElements(), body) :
                m.at(tree.pos).OnInvalidate(body);
        result = res.setType(tree.type);
    }

    public void visitParens(VisageParens tree) {
        VisageExpression expr = lowerExpr(tree.expr);
        result = m.at(tree.pos).Parens(expr).setType(tree.type);
    }

    public void visitPostInitDefinition(VisagePostInitDefinition tree) {
        VisageBlock body = lowerExpr(tree.body);
        VisagePostInitDefinition res = m.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitScript(VisageScript tree) {
        varCount = 0;
        tree.defs = lowerDecls(tree.defs);
        result = tree;
    }

    public void visitSelect(VisageSelect tree) {
        result = (tree.sym.kind == Kinds.MTH) ?
            toFunctionValue(tree, true) :
            lowerSelect(tree);
    }

    private VisageExpression lowerSelect(VisageSelect tree) {
        VisageExpression res = null;
        if (tree.sym.isStatic() &&
                VisageTreeInfo.symbolFor(tree.selected) != null &&
                VisageTreeInfo.symbolFor(tree.selected).kind == Kinds.TYP) {
            res = m.at(tree.pos()).Ident(tree.sym);
        }
        else {
            VisageExpression selected = lowerExpr(tree.selected);
            res = (VisageSelect)m.Select(selected, tree.sym, tree.nullCheck);
        }
        return res.setType(tree.type);
    }

    public void visitSkip(VisageSkip tree) {
        result = tree;
    }

    public void visitThrow(VisageThrow tree) {
        VisageExpression expr = lowerExpr(tree.getExpression());
        result = m.at(tree.pos).Throw(expr).setType(tree.type);
    }

    public void visitTimeLiteral(VisageTimeLiteral tree) {
        result = tree;
    }

    public void visitLengthLiteral(VisageLengthLiteral tree) {
        result = tree;
    }

    public void visitAngleLiteral(VisageAngleLiteral tree) {
        result = tree;
    }

    public void visitColorLiteral(VisageColorLiteral tree) {
        result = tree;
    }

    public void visitTry(VisageTry tree) {
        VisageBlock body = lowerExpr(tree.getBlock());
        List<VisageCatch> catches = lowerDecls(tree.catchers);
        VisageBlock finallyBlock = lowerExpr(tree.getFinallyBlock());
        result = m.at(tree.pos).Try(body, catches, finallyBlock).setType(tree.type);
    }

    public void visitTypeAny(VisageTypeAny tree) {
        result = tree;
    }

    public void visitTypeArray(VisageTypeArray tree) {
        result = tree;
    }

    public void visitTypeCast(VisageTypeCast tree) {
        VisageExpression expr = lowerExpr(tree.getExpression(), tree.clazz.type);
        result = m.at(tree.pos).TypeCast(tree.clazz, expr).setType(tree.type);
    }

    public void visitTypeClass(VisageTypeClass tree) {
        result = tree;
    }

    public void visitTypeFunctional(VisageTypeFunctional tree) {
        result = tree;
    }

    public void visitTypeUnknown(VisageTypeUnknown tree) {
        result = tree;
    }

    public void visitWhileLoop(VisageWhileLoop tree) {
        VisageExpression cond = lowerExpr(tree.getCondition(), syms.booleanType);
        VisageExpression body = lowerExpr(tree.getBody());
        // Standard form is that the body is a block-expression
        if(!(body instanceof VisageBlock)) {
            body = m.Block(0L, List.<VisageExpression>nil(), body);
        }
        body.setType(syms.voidType);
        result = m.at(tree.pos).WhileLoop(cond, body).setType(syms.voidType);
    }
}
