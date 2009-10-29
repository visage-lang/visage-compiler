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

import com.sun.javafx.api.tree.TypeTree.Cardinality;

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxSymtab;

import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;

/**
 *
 * @author Maurizio Cimadamore
 */
public class JavafxConvertTypes implements JavafxVisitor {
    protected static final Context.Key<JavafxConvertTypes> convertTypesKey =
            new Context.Key<JavafxConvertTypes>();

    private JavafxTypes types;
    private JavafxSymtab syms;
    private JavafxTreeMaker m;
    private Type pt;
    private JFXTree enclFunc;
    private JFXTree result;

    public static JavafxConvertTypes instance(Context context) {
        JavafxConvertTypes instance = context.get(convertTypesKey);
        if (instance == null)
            instance = new JavafxConvertTypes(context);
        return instance;
    }

    JavafxConvertTypes(Context context) {
        context.put(convertTypesKey, this);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        m = JavafxTreeMaker.instance(context);
    }

    public JFXTree convertTypes(JavafxEnv<JavafxAttrContext> attrEnv) {
        attrEnv.tree.accept(this);
        //System.out.println(result);
        return result;
    }

    public JFXExpression convertExpr(JFXExpression tree, Type pt) {
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
    public <T extends JFXTree> T convert(T tree) {
        if (tree != null) {
            tree.accept(this);
            return (T)result;
        }
        else
            return null;
    }

    public <T extends JFXTree> List<T> convert(List<T> trees) {
        ListBuffer<T> buf = ListBuffer.lb();
        for (T tree : trees) {
            buf.append(convert(tree));
        }
        return buf.toList();
    }

    public List<JFXExpression> convertExprs(List<? extends JFXExpression> trees, List<Type> pts) {
        ListBuffer<JFXExpression> buf = ListBuffer.lb();
        for (JFXExpression tree : trees) {
            buf.append(convertExpr(tree, pts.head));
            pts = pts.tail;
        }
        return buf.toList();
    }

    JFXExpression convertTree(JFXExpression tree, Type type) {
        boolean needSeqConversion = needSequenceConversion(tree, type);
        if (needSeqConversion) {
            type = types.elementType(type);
        }
        tree = makeCastIfNeeded(tree, type);
        return tree = needSeqConversion ?
            toSequence(tree, type) :
            tree;
    }

    private boolean needSequenceConversion(JFXExpression tree, Type type) {
        return (types.isSequence(type) &&
                ((!types.isSequence(tree.type) &&
                !types.isArray(tree.type)) ||
                isNull(tree)));
    }

    private boolean isNull(JFXTree tree) {
        return (tree.getFXTag() == JavafxTag.LITERAL &&
                ((JFXLiteral)tree).value == null);
    }

    private JFXExpression toSequence(JFXExpression tree, Type type) {
        JFXExpression expr = isNull(tree) ? 
            m.at(tree.pos).EmptySequence() :
            m.at(tree.pos).ExplicitSequence(List.of(tree));
        expr.type = types.sequenceType(type);
        return expr;
    }

    private JFXExpression makeCastIfNeeded(JFXExpression tree, Type type) {
        if (type == Type.noType
                || type == null
                || type.isErroneous()
                || type == syms.voidType
                || tree.type == syms.voidType
                || type == syms.unreachableType)
            return tree;
        else
            return (!types.isSubtype(tree.type, type) ||
                (tree.type.isPrimitive() &&
                type.isPrimitive() && !types.isSameType(tree.type, type))) ?
                makeCast(tree, type) :
                tree;
    }

    private JFXExpression makeCast(JFXExpression tree, Type type) {
        JFXExpression typeTree = m.TypeClass(m.Type(types.elementTypeOrType(type)),
                types.isSequence(type) ? Cardinality.ANY : Cardinality.SINGLETON);
        typeTree.type = type;
        JFXExpression expr = m.at(tree.pos).TypeCast(typeTree, tree);
        expr.type = type;
        return expr;
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = convert(tree.lhs);
        JFXExpression rhs = convertExpr(tree.rhs, tree.lhs.type);
        result = m.at(tree.pos).Assign(lhs, rhs).setType(tree.type);
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        JFXExpression lhs = convert(tree.lhs);
        JFXExpression rhs = convertExpr(tree.rhs, tree.lhs.type);
        JFXAssignOp assign = m.at(tree.pos).Assignop(tree.getNormalOperatorFXTag(), lhs, rhs);
        assign.operator = tree.operator;
        result = assign.setType(tree.type);
    }

    @Override
    public void visitBinary(JFXBinary tree) {
        boolean isDurationBinaryExpr = tree.operator == null;
        Type lhsType = isDurationBinaryExpr ?
            syms.javafx_DurationType :
            tree.operator.type.getParameterTypes().head;
        Type rhsType = isDurationBinaryExpr ?
            syms.javafx_DurationType :
            tree.operator.type.getParameterTypes().tail.head;
        JFXExpression lhs = convertExpr(tree.lhs, lhsType);
        JFXExpression rhs = convertExpr(tree.rhs, rhsType);
        JFXBinary res = m.at(tree.pos).Binary(tree.getFXTag(), lhs, rhs);
        res.operator = tree.operator;
        result = res.setType(tree.type);
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        JFXExpression whereExpr = convert(that.whereExpr);
        JFXExpression seqExpr = null;
        if (types.isSequence(that.seqExpr.type)) {
            seqExpr = convertExpr(that.seqExpr, types.sequenceType(that.var.type));
        }
        else {//sure that this is what we want?
            seqExpr = makeCast(that.seqExpr, types.sequenceType(that.var.type)).setType(types.sequenceType(that.var.type));
        }
        result = m.at(that.pos).InClause(that.getVar(), seqExpr, whereExpr).setType(that.type);
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        JFXTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            JFXBlock body  = (JFXBlock)convertExpr(tree.getBodyExpression(), tree.type != null ? tree.type.getReturnType() : Type.noType);
            result = m.FunctionDefinition(tree.mods, tree.name, tree.getJFXReturnType(), tree.getParams(), body);
            result.type = tree.type;
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    @Override
    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression meth = convert(tree.meth);
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
        List<JFXExpression> args = convertExprs(tree.args, paramTypes);
        result = m.Apply(tree.typeargs, meth, args);
        result.type = tree.type;
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXTree prevFunc = enclFunc;
        try {
            enclFunc = tree;
            JFXBlock body = (JFXBlock)convertExpr(tree.bodyExpression, tree.type.getReturnType());
            result = m.at(tree.pos).FunctionValue(tree.rettype, tree.funParams, body).setType(tree.type);
        }
        finally {
            enclFunc = prevFunc;
        }
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        JFXExpression cond = convertExpr(tree.cond, syms.booleanType);
        JFXExpression truePart = convertExpr(tree.truepart, tree.type);
        JFXExpression falsePart = convertExpr(tree.falsepart, tree.type);
        result = m.Conditional(cond, truePart, falsePart).setType(tree.type);
    }

    @Override
    public void visitIndexof(JFXIndexof that) {
        result = that;
    }

    @Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = convert(tree.getExpression());
        result = m.at(tree.pos).TypeTest(expr, tree.clazz).setType(tree.type);
    }

    @Override
    public void visitInterpolateValue(JFXInterpolateValue that) {
        JFXExpression funcValue = convert(that.funcValue);
        JFXInterpolateValue res = m.at(that.pos).InterpolateValue(that.attribute, funcValue, that.interpolation);
        res.sym = that.sym;
        result = res.setType(that.type);
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral that) {
        List<JFXExpression> values = convert(that.values);
        JFXExpression trigger = convert(that.trigger);
        result = m.at(that.pos).KeyFrameLiteral(that.start, values, trigger).setType(that.type);
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        result = tree;
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JFXExpression expr = convertExpr(tree.getExpression(), tree.sym.type);
        JFXObjectLiteralPart res = m.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getBindStatus());
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        JFXExpression init = convertExpr(tree.getInitializer(), tree.type);
        JFXOnReplace onReplace = convert(tree.getOnReplace());
        JFXOnReplace onInvalidate = convert(tree.getOnInvalidate());
        JFXOverrideClassVar res = m.at(tree.pos).OverrideClassVar(tree.name, tree.mods, tree.getId(), init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    @Override
    public void visitReturn(JFXReturn tree) {
        Type typeToCheck = enclFunc.type != null ?
            enclFunc.type.getReturnType() :
            syms.objectType; //nedded because run function has null type
        JFXExpression retExpr = convertExpr(tree.getExpression(), typeToCheck);
        result = m.at(tree.pos).Return(retExpr).setType(tree.type);
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete that) {
        JFXExpression seq = convert(that.getSequence());
        JFXExpression el = convertExpr(that.getElement(), types.elementType(that.getSequence().type));
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
            buf.append(convertExpr(item, types.elementType(that.type)));
        }
        result = m.at(that.pos).ExplicitSequence(buf.toList()).setType(that.type);
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        JFXExpression index = convertExpr(that.getIndex(), syms.intType);
        JFXExpression seq = convert(that.getSequence());
        result = m.at(that.pos).SequenceIndexed(seq, index).setType(that.type);
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert that) {
        JFXExpression seq = convert(that.getSequence());
        JFXExpression el = convertExpr(that.getElement(), types.elementType(that.getSequence().type));
        JFXExpression pos = convertExpr(that.getPosition(), syms.intType);
        result = m.at(that.pos).SequenceInsert(seq, el, pos, that.shouldInsertAfter()).setType(that.type);
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange that) {
        JFXExpression lower = convertExpr(that.getLower(), types.elementType(that.type));
        JFXExpression upper = convertExpr(that.getUpper(), types.elementType(that.type));
        JFXExpression step = convertExpr(that.getStepOrNull(), types.elementType(that.type));
        JFXSequenceRange res = m.at(that.pos).RangeSequence(lower, upper, step, that.isExclusive());
        res.boundSizeVar = that.boundSizeVar;
        result = res.setType(that.type);
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice that) {
        JFXExpression seq = convert(that.getSequence());
        JFXExpression start = convertExpr(that.getFirstIndex(), syms.intType);
        JFXExpression end = convertExpr(that.getLastIndex(), syms.intType);
        result = m.at(that.pos).SequenceSlice(seq, start, end, that.getEndKind()).setType(that.type);
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = convert(tree.parts);
        result = m.at(tree.pos).StringExpression(parts, tree.translationKey).setType(tree.type);
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        JFXExpression arg = tree.getFXTag().isIncDec() ?
            convertExpr(tree.getExpression(), tree.getOperator().type.getParameterTypes().head) :
            convert(tree.getExpression());
        JFXUnary res = m.at(tree.pos).Unary(tree.getFXTag(), arg);
        res.operator = tree.operator;
        res.type = tree.type;
        result = res;
    }

    @Override
    public void visitVar(JFXVar tree) {
        JFXExpression init = convertExpr(tree.getInitializer(), tree.type);
        JFXOnReplace onReplace = convert(tree.getOnReplace());
        JFXOnReplace onInvalidate = convert(tree.getOnInvalidate());
        JFXVar res = m.at(tree.pos).Var(tree.name, tree.getJFXType(), tree.mods, init, tree.getBindStatus(), onReplace, onInvalidate);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    @Override
    public void visitVarInit(JFXVarInit tree) {
        JFXVar var = convert(tree.getVar());
        result = m.VarInit(var).setType(tree.type);
    }

    public void visitBlockExpression(JFXBlock tree) {
        List<JFXExpression> stats = convert(tree.stats);
        JFXExpression value = tree.value != null ?
            convertExpr(tree.value, pt) :
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
        JFXBlock body = convert(tree.body);
        result = m.at(tree.pos).Catch(tree.param, body).setType(tree.type);
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        List<JFXTree> defs = convert(tree.getMembers());
        JFXFunctionDefinition runMethod = convert(tree.runMethod);
        JFXClassDeclaration res = m.at(tree.pos).ClassDeclaration(tree.mods, tree.getName(), tree.getSupertypes(), defs);
        res.hasBeenTranslated = tree.hasBeenTranslated;
        res.isScriptClass = tree.isScriptClass;
        res.runBodyScope = tree.runBodyScope;
        res.runMethod = runMethod;
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitContinue(JFXContinue tree) {
        result = tree;
    }

    public void visitErroneous(JFXErroneous tree) {
        result = tree;
    }

    public void visitForExpression(JFXForExpression tree) {
        JFXExpression body = convert(tree.bodyExpr);
        List<JFXForExpressionInClause> clauses = convert(tree.getForExpressionInClauses());
        result = m.at(tree.pos).ForExpression(clauses, body).setType(tree.type);
    }

    public void visitIdent(JFXIdent tree) {
        result = tree;
    }

    public void visitImport(JFXImport tree) {
        result = tree;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        JFXBlock body = convert(tree.body);
        JFXInitDefinition res = m.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res.setType(tree.type);
    }

    public void visitInstanciate(JFXInstanciate tree) {
       List<JFXObjectLiteralPart> parts = convert(tree.getParts());
       JFXClassDeclaration cdecl = convert(tree.getClassBody());
       List<JFXExpression> args = convert(tree.getArgs());
       JFXInstanciate res = m.at(tree.pos).Instanciate(tree.getJavaFXKind(), tree.getIdentifier(), cdecl, args, parts, tree.getLocalvars());
       res.sym = tree.sym;
       res.constructor = tree.constructor;
       res.varDefinedByThis = tree.varDefinedByThis;
       result = res.setType(tree.type);
    }

    public void visitInvalidate(JFXInvalidate tree) {
        JFXExpression expr = convert(tree.getVariable());
        result = m.at(tree.pos).Invalidate(expr).setType(tree.type);
    }

    public void visitModifiers(JFXModifiers tree) {
        result = tree;
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXBlock body = convert(tree.getBody());
        JFXOnReplace res = tree.getTriggerKind() == JFXOnReplace.Kind.ONREPLACE ?
                m.at(tree.pos).OnReplace(tree.getOldValue(), tree.getFirstIndex(), tree.getLastIndex(), tree.getNewElements(), body) :
                m.at(tree.pos).OnInvalidate(body);
        result = res.setType(tree.type);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = convert(tree.expr);
        result = m.at(tree.pos).Parens(expr).setType(tree.type);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        JFXBlock body = convert(tree.body);
        JFXPostInitDefinition res = m.at(tree.pos).PostInitDefinition(body);
        result = res.setType(tree.type);
    }

    public void visitScript(JFXScript tree) {
        JFXExpression pid = tree.getPackageName();
        List<JFXTree> defs = convert(tree.defs);
        result = m.Script(pid, defs);
    }

    public void visitSelect(JFXSelect tree) {
        result = tree;
    }

    public void visitSkip(JFXSkip tree) {
        result = tree;
    }

    public void visitThrow(JFXThrow tree) {
        JFXExpression expr = convert(tree.getExpression());
        result = m.at(tree.pos).Throw(expr).setType(tree.type);
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = convert(tree.getBlock());
        List<JFXCatch> catches = convert(tree.catchers);
        JFXBlock finallyBlock = convert(tree.getFinallyBlock());
        result = m.at(tree.pos).Try(body, catches, finallyBlock).setType(tree.type);
    }

    public void visitTypeAny(JFXTypeAny tree) {
        result = tree;
    }

    public void visitTypeArray(JFXTypeArray tree) {
        result = tree;
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXExpression expr = convert(tree.getExpression());
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
        JFXExpression cond = convertExpr(tree.getCondition(), syms.booleanType);
        JFXExpression body = convert(tree.getStatement());
        result = m.at(tree.pos).WhileLoop(cond, body).setType(tree.type);
    }
}
