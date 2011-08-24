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

package org.visage.tools.tree;

import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import java.util.Map;

/**
 * Creates a copy of a tree, using a given TreeMaker.
 * Names, literal values, etc are shared with the original.
 *
 * @author tball
 */
public class VisageTreeCopier implements VisageVisitor {
    protected VisageTreeMaker maker;
    protected VisageTree result;

    public Map<JCTree, Integer> endPositions;

    /** Creates a new instance of TreeCopier */
    public VisageTreeCopier(VisageTreeMaker maker) {
        this.maker = maker;
    }

    @SuppressWarnings("unchecked")
    public <T extends VisageTree> T copy(T tree) {
        if (tree == null)
            return null;
        tree.accept(this);

        // Update the end positions map, we keep the prior object
        // in there in case others have references to them.
        //
        Integer endpos = endPositions.get(tree);
        endPositions.put(result, endpos);

        return (T)result;
    }

    public <T extends VisageTree> List<T> copy(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(copy(tree));
        return lb.toList();
    }

    public void visitScript(VisageScript tree) {
        VisageExpression pid = copy(tree.pid);
        List<VisageTree> defs = copy(tree.defs);
        result = maker.at(tree.pos).Script(pid, defs);
    }

    public void visitImport(VisageImport tree) {
        VisageExpression qualid = copy(tree.qualid);
        result = maker.at(tree.pos).Import(qualid);
    }

    public void visitSkip(VisageSkip tree) {
        result = maker.at(tree.pos).Skip();
    }

    public void visitWhileLoop(VisageWhileLoop tree) {
        VisageExpression cond = copy(tree.cond);
        VisageExpression body = copy(tree.body);
        result = maker.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitTry(VisageTry tree) {
        VisageBlock body = copy(tree.body);
        List<VisageCatch> catchers = copy(tree.catchers);
        VisageBlock finalizer = copy(tree.finalizer);
        result = maker.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitCatch(VisageCatch tree) {
        VisageVar param = copy(tree.param);
        VisageBlock body = copy(tree.body);
        result = maker.at(tree.pos).Catch(param, body);
    }

    public void visitIfExpression(VisageIfExpression tree) {
        VisageExpression cond = copy(tree.cond);
        VisageExpression truepart = copy(tree.truepart);
        VisageExpression falsepart = copy(tree.falsepart);
        result = maker.at(tree.pos).Conditional(cond, truepart, falsepart);
    }

    public void visitBreak(VisageBreak tree) {
        result = maker.at(tree.pos).Break(tree.label);
    }

    public void visitContinue(VisageContinue tree) {
        result = maker.at(tree.pos).Continue(tree.label);
    }

    public void visitReturn(VisageReturn tree) {
        VisageExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Return(expr);
    }

    public void visitThrow(VisageThrow tree) {
        VisageExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Throw(expr);
    }

    public void visitFunctionInvocation(VisageFunctionInvocation tree) {
        List<VisageExpression> typeargs = copy(tree.typeargs);
        VisageExpression fn = copy(tree.meth);
        List<VisageExpression> args = copy(tree.args);
        result = maker.at(tree.pos).Apply(typeargs, fn, args);
    }

    public void visitParens(VisageParens tree) {
        VisageExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Parens(expr);
    }

    public void visitAssign(VisageAssign tree) {
        VisageExpression lhs = copy(tree.lhs);
        VisageExpression rhs = copy(tree.rhs);
        result = maker.at(tree.pos).Assign(lhs, rhs);
    }

    public void visitAssignop(VisageAssignOp tree) {
        VisageExpression lhs = copy(tree.lhs);
        VisageExpression rhs = copy(tree.rhs);
        VisageTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Assignop(tag, lhs, rhs);
    }

    public void visitUnary(VisageUnary tree) {
        VisageExpression arg = copy(tree.arg);
        VisageTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Unary(tag, arg);
    }

    public void visitBinary(VisageBinary tree) {
        VisageExpression lhs = copy(tree.lhs);
        VisageExpression rhs = copy(tree.rhs);
        VisageTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Binary(tag, lhs, rhs);
    }

    public void visitTypeCast(VisageTypeCast tree) {
        VisageTree clazz = copy(tree.clazz);
        VisageExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitInstanceOf(VisageInstanceOf tree) {
        VisageExpression expr = copy(tree.expr);
        VisageTree clazz = copy(tree.clazz);
        result = maker.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(VisageSelect tree) {
        VisageExpression selected = copy(tree.selected);
        result = maker.at(tree.pos).Select(selected, tree.name, tree.nullCheck);
    }

    public void visitIdent(VisageIdent tree) {
        result = maker.at(tree.pos).Ident(tree.getName());
    }

    public void visitLiteral(VisageLiteral tree) {
        result = maker.at(tree.pos).Literal(tree.typetag, tree.value);
    }

    public void visitModifiers(VisageModifiers tree) {
        result = maker.at(tree.pos).Modifiers(tree.flags);
    }

    public void visitErroneous(VisageErroneous tree) {
        List<? extends VisageTree> errs = copy(tree.getErrorTrees());
        result = maker.at(tree.pos).Erroneous(errs);
    }

    public void visitClassDeclaration(VisageClassDeclaration tree) {
        VisageModifiers mods = copy(tree.mods);
        Name name = tree.getName();
        List<VisageExpression> supertypes = copy(tree.getSupertypes());
        List<VisageTree> defs = copy(tree.getMembers());
        result = maker.at(tree.pos).ClassDeclaration(mods, name, supertypes, defs);
    }

    public void visitFunctionDefinition(VisageFunctionDefinition tree) {
        VisageModifiers mods = copy(tree.mods);
        Name name = tree.getName();
        VisageType restype = copy(tree.getJFXReturnType());
        List<VisageVar> params = copy(tree.getParams());
        VisageBlock bodyExpression = copy(tree.getBodyExpression());
        result = maker.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
    }

    public void visitInitDefinition(VisageInitDefinition tree) {
        VisageBlock body = tree.body;
        result = maker.at(tree.pos).InitDefinition(body);
    }

    public void visitPostInitDefinition(VisagePostInitDefinition tree) {
        VisageBlock body = tree.body;
        result = maker.at(tree.pos).PostInitDefinition(body);
    }

    public void visitStringExpression(VisageStringExpression tree) {
        List<VisageExpression> parts = copy(tree.parts);
        result = maker.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

    public void visitInstanciate(VisageInstanciate tree) {
        VisageExpression clazz = copy(tree.getIdentifier());
        VisageClassDeclaration def = copy(tree.getClassBody());
        List<VisageExpression> args = copy(tree.getArgs());
        List<VisageObjectLiteralPart> parts = copy(tree.getParts());
        List<VisageVar> localVars = copy(tree.getLocalvars());
        result = maker.at(tree.pos).Instanciate(tree.getVisageKind(), clazz, def, args, parts, localVars);
    }

    public void visitObjectLiteralPart(VisageObjectLiteralPart tree) {
        VisageExpression expr = copy(tree.getExpression());
        VisageObjectLiteralPart res = maker.at(tree.pos).ObjectLiteralPart(tree.name, expr, tree.getExplicitBindStatus());
        res.markBound(tree.getBindStatus());
        result = res;
    }

    public void visitTypeAny(VisageTypeAny tree) {
        result = maker.at(tree.pos).TypeAny(tree.getCardinality());
    }

    public void visitTypeClass(VisageTypeClass tree) {
        VisageExpression clazz = copy(tree.getClassName());
        result = maker.at(tree.pos).TypeClass(clazz, tree.getCardinality());
    }

    public void visitTypeFunctional(VisageTypeFunctional tree) {
        List<VisageType> params = copy(tree.getParams());
        VisageType restype = copy(tree.restype);
        result = maker.at(tree.pos).TypeFunctional(params, restype, tree.getCardinality());
    }

    //@Override
    public void visitTypeArray(VisageTypeArray tree) {
        VisageType elementType = copy(tree.getElementType());
        result = maker.at(tree.pos).TypeArray(elementType);
    }

    public void visitTypeUnknown(VisageTypeUnknown tree) {
        result = maker.at(tree.pos).TypeUnknown();
    }

    //@Override
    public void visitVarInit(VisageVarInit tree) {
    }

    //@Override
    public void visitVarRef(VisageVarRef tree) {
        result = maker.at(tree.pos).VarRef(tree.getExpression(), tree.getVarRefKind());
    }

    public void visitVar(VisageVar tree) {
        Name name = tree.name;
        VisageType type = copy(tree.getJFXType());
        VisageModifiers mods = copy(tree.getModifiers());
        VisageExpression init = copy(tree.getInitializer());
        VisageOnReplace onReplace = copy(tree.getOnReplace());
        VisageOnReplace onInvalidate = copy(tree.getOnInvalidate());
        result = maker.at(tree.pos).Var(name, type, mods, 
                                        init, tree.getBindStatus(), onReplace, onInvalidate);
    }

    public void visitOnReplace(VisageOnReplace tree) {
        VisageVar oldValue = copy(tree.getOldValue());
        VisageVar firstIndex = copy(tree.getFirstIndex());
        VisageVar lastIndex = copy(tree.getLastIndex());
        VisageVar newElements = copy(tree.getNewElements());
        VisageBlock body = copy(tree.getBody());
        result = maker.at(tree.pos).OnReplace(oldValue, firstIndex, lastIndex, tree.getEndKind(), newElements, body);
    }

    public void visitBlockExpression(VisageBlock tree) {
        List<VisageExpression> stats = copy(tree.stats);
        VisageExpression value = copy(tree.value);
        result = maker.at(tree.pos).Block(tree.flags, stats, value);

    }

    public void visitFunctionValue(VisageFunctionValue tree) {
        VisageType restype = copy(tree.rettype);
        List<VisageVar> params = copy(tree.getParams());
        VisageBlock bodyExpression = copy(tree.bodyExpression);
        result = maker.at(tree.pos).FunctionValue(restype, params, bodyExpression);
    }

    public void visitSequenceEmpty(VisageSequenceEmpty tree) {
        result = maker.at(tree.pos).EmptySequence();
    }

    public void visitSequenceRange(VisageSequenceRange tree) {
        VisageExpression lower = copy(tree.getLower());
        VisageExpression upper = copy(tree.getUpper());
        VisageExpression stepOrNull = copy(tree.getStepOrNull());
        result = maker.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
    }

    public void visitSequenceExplicit(VisageSequenceExplicit tree) {
        List<VisageExpression> items = copy(tree.getItems());
        result = maker.at(tree.pos).ExplicitSequence(items);
    }

    public void visitSequenceIndexed(VisageSequenceIndexed tree) {
        VisageExpression sequence = copy(tree.getSequence());
        VisageExpression index = copy(tree.getIndex());
        result = maker.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(VisageSequenceSlice tree) {
        VisageExpression sequence = copy(tree.getSequence());
        VisageExpression firstIndex = copy(tree.getFirstIndex());
        VisageExpression lastIndex = copy(tree.getLastIndex());
        result = maker.at(tree.pos).SequenceSlice(sequence, firstIndex, lastIndex, tree.getEndKind());
    }

    public void visitSequenceInsert(VisageSequenceInsert tree) {
        VisageExpression sequence = copy(tree.getSequence());
        VisageExpression element = copy(tree.getElement());
        VisageExpression position = copy(tree.getPosition());
        result = maker.at(tree.pos).SequenceInsert(sequence, element, position, tree.shouldInsertAfter());
    }

    public void visitSequenceDelete(VisageSequenceDelete tree) {
        VisageExpression sequence = copy(tree.getSequence());
        VisageExpression element = copy(tree.getElement());
        result = maker.at(tree.pos).SequenceDelete(sequence, element);
    }

    public void visitInvalidate(VisageInvalidate tree) {
       VisageExpression variable = copy(tree.getVariable());
       result = maker.at(tree.pos).Invalidate(variable);
    }

    public void visitForExpression(VisageForExpression tree) {
        List<VisageForExpressionInClause> inClauses = copy(tree.inClauses);
        VisageExpression bodyExpr = copy(tree.bodyExpr);
        result = maker.at(tree.pos).ForExpression(inClauses, bodyExpr);
    }

    public void visitForExpressionInClause(VisageForExpressionInClause tree) {
        tree.seqExpr = copy(tree.seqExpr);
        tree.setWhereExpr(copy(tree.getWhereExpression()));
        result = tree;
    }

    public void visitIndexof(VisageIndexof tree) {
        result = maker.at(tree.pos).Indexof(tree.fname);
    }

    public void visitTimeLiteral(VisageTimeLiteral tree) {
        VisageLiteral literal = copy(tree.value);
        result = maker.at(tree.pos).TimeLiteral(literal, tree.duration);
    }

    public void visitLengthLiteral(VisageLengthLiteral tree) {
        VisageLiteral literal = copy(tree.value);
        result = maker.at(tree.pos).LengthLiteral(literal, tree.units);
    }

    public void visitAngleLiteral(VisageAngleLiteral tree) {
        VisageLiteral literal = copy(tree.value);
        result = maker.at(tree.pos).AngleLiteral(literal, tree.units);
    }

    public void visitColorLiteral(VisageColorLiteral tree) {
        VisageLiteral literal = copy(tree.value);
        result = maker.at(tree.pos).ColorLiteral(literal);
    }

    public void visitOverrideClassVar(VisageOverrideClassVar tree) {
        Name name = tree.getName();
        VisageModifiers mods = copy(tree.getModifiers());
        VisageIdent expr = copy(tree.getId());
        VisageType jfxtype = copy(tree.getJFXType());
        VisageExpression initializer = copy(tree.getInitializer());
        VisageOnReplace onReplace = copy(tree.getOnReplace());
        VisageOnReplace onInvalidate = copy(tree.getOnInvalidate());
        result = maker.at(tree.pos).OverrideClassVar(name, jfxtype, mods, expr, initializer, tree.getBindStatus(), onReplace, onInvalidate);
    }

    public void visitInterpolateValue(VisageInterpolateValue tree) {
        VisageExpression attr = copy(tree.attribute);
        VisageExpression value = copy(tree.value);
        VisageExpression interpolation = copy(tree.interpolation);
        result = maker.at(tree.pos).InterpolateValue(attr, value, interpolation);
    }

    public void visitKeyFrameLiteral(VisageKeyFrameLiteral tree) {
        VisageExpression start = copy(tree.start);
        List<VisageExpression> values = copy(tree.values);
        VisageExpression trigger = copy(tree.trigger);
        result = maker.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }

}
