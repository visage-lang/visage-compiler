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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Context;

/**
 * Decompose bind expressions into easily translated expressions
 *
 * @author Robert Field
 */
public class JavafxDecompose implements JavafxVisitor {
    protected static final Context.Key<JavafxDecompose> decomposeKey =
            new Context.Key<JavafxDecompose>();

    private JFXTree result;
    private boolean inUniBind = false;
    private ListBuffer<? super JFXVar> lbVar;
    private int varCount = 0;

    protected final JavafxTreeMaker fxmake;
    protected final JavafxDefs defs;
    protected final Name.Table names;
    protected final JavafxResolve rs;
    protected final JavafxSymtab syms;
    protected final JavafxTypes types;

    public static JavafxDecompose instance(Context context) {
        JavafxDecompose instance = context.get(decomposeKey);
        if (instance == null)
            instance = new JavafxDecompose(context);
        return instance;
    }

    JavafxDecompose(Context context) {
        context.put(decomposeKey, this);

        fxmake = JavafxTreeMaker.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
    }

    @SuppressWarnings("unchecked")
    private <T extends JFXTree> T decompose(T tree) {
        if (tree == null)
            return null;
        tree.accept(this);
        return (T)result;
    }

    private <T extends JFXTree> List<T> decompose(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }

    private JFXExpression shred(JFXExpression tree) {
        if (tree == null)
            return null;
        JFXExpression pose = decompose(tree);
        Name vName = tempName();
        JFXModifiers mod = fxmake.at(tree.pos).Modifiers(0L);
        JFXType fxType = fxmake.at(tree.pos).TypeAny(Cardinality.ANY);
        JFXVar v = fxmake.at(tree.pos).Var(vName, fxType, mod, pose, JavafxBindStatus.UNIDIBIND, null);
        return fxmake.at(tree.pos).Ident(vName);
    }

    private List<JFXExpression> shred(List<JFXExpression> trees) {
        if (trees == null)
            return null;
        ListBuffer<JFXExpression> lb = new ListBuffer<JFXExpression>();
        for (JFXExpression tree: trees)
            lb.append(decompose(tree));
        return lb.toList();
    }

    private Name tempName() {
        return names.fromString("$t" + varCount++);
    }

    private <T extends JFXTree> List<T> decomposeContainer(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        ListBuffer<? super JFXVar> prevLbVar = lbVar;
        lbVar = (ListBuffer<? super JFXVar>)lb;
        for (T tree: trees)
            lb.append(decompose(tree));
        lbVar = prevLbVar;
        return lb.toList();
    }

    public void visitScript(JFXScript tree) {
        inUniBind = false;
        List<JFXTree> sdef = decomposeContainer(tree.defs);
        result = fxmake.at(tree.pos).Script(tree.pid, sdef);
    }

    public void visitImport(JFXImport tree) {
        result = tree;
    }

    public void visitSkip(JFXSkip tree) {
        result = tree;
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        JFXExpression cond = decompose(tree.cond);
        JFXExpression body = decompose(tree.body);
        result = fxmake.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = decompose(tree.body);
        List<JFXCatch> catchers = decompose(tree.catchers);
        JFXBlock finalizer = decompose(tree.finalizer);
        result = fxmake.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitCatch(JFXCatch tree) {
        JFXVar param = decompose(tree.param);
        JFXBlock body = decompose(tree.body);
        result = fxmake.at(tree.pos).Catch(param, body);
    }

    public void visitIfExpression(JFXIfExpression tree) {
        JFXExpression cond = decompose(tree.cond);
        JFXExpression truepart = decompose(tree.truepart);
        JFXExpression falsepart = decompose(tree.falsepart);
        result = fxmake.at(tree.pos).Conditional(cond, truepart, falsepart);
    }

    public void visitBreak(JFXBreak tree) {
        result = tree;
    }

    public void visitContinue(JFXContinue tree) {
        result = tree;
    }

    public void visitReturn(JFXReturn tree) {
        JFXExpression expr = decompose(tree.expr);
        result = fxmake.at(tree.pos).Return(expr);
    }

    public void visitThrow(JFXThrow tree) {
        result = tree;
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression fn = decompose(tree.meth);
        List<JFXExpression> args = decompose(tree.args);
        result = fxmake.at(tree.pos).Apply(tree.typeargs, fn, args);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = decompose(tree.expr);
        result = fxmake.at(tree.pos).Parens(expr);
    }

    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = decompose(tree.lhs);
        JFXExpression rhs = decompose(tree.rhs);
        result = fxmake.at(tree.pos).Assign(lhs, rhs);
    }

    public void visitAssignop(JFXAssignOp tree) {
        JFXExpression lhs = decompose(tree.lhs);
        JFXExpression rhs = decompose(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        result = fxmake.at(tree.pos).Assignop(tag, lhs, rhs);
    }

    public void visitUnary(JFXUnary tree) {
        JFXExpression arg = decompose(tree.arg);
        JavafxTag tag = tree.getFXTag();
        result = fxmake.at(tree.pos).Unary(tag, arg);
    }

    public void visitBinary(JFXBinary tree) {
        JFXExpression lhs = decompose(tree.lhs);
        JFXExpression rhs = decompose(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        result = fxmake.at(tree.pos).Binary(tag, lhs, rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXTree clazz = decompose(tree.clazz);
        JFXExpression expr = decompose(tree.expr);
        result = fxmake.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = decompose(tree.expr);
        JFXTree clazz = decompose(tree.clazz);
        result = fxmake.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(JFXSelect tree) {
        JFXExpression selected = decompose(tree.selected);
        result = fxmake.at(tree.pos).Select(selected, tree.name);
    }

    public void visitIdent(JFXIdent tree) {
        result = fxmake.at(tree.pos).Ident(tree.name);
    }

    public void visitLiteral(JFXLiteral tree) {
        result = tree;
    }

    public void visitModifiers(JFXModifiers tree) {
        result = tree;
    }

    public void visitErroneous(JFXErroneous tree) {
        result = tree;
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = false;
        Name name = tree.getName();
        List<JFXTree> defs = decomposeContainer(tree.getMembers());
        result = fxmake.at(tree.pos).ClassDeclaration(tree.mods, name, tree.getSupertypes(), defs);
        inUniBind = wasInUniBind;
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = tree.isBound();
        JFXModifiers mods = tree.mods;
        Name name = tree.getName();
        JFXType restype = decompose(tree.getJFXReturnType());
        List<JFXVar> params = decompose(tree.getParams());
        JFXBlock bodyExpression = decompose(tree.getBodyExpression());
        result = fxmake.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
        inUniBind = wasInUniBind;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        JFXBlock body = decompose(tree.body);
        result = fxmake.at(tree.pos).InitDefinition(body);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        JFXBlock body = decompose(tree.body);
        result = fxmake.at(tree.pos).PostInitDefinition(body);
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = decompose(tree.parts);
        result = fxmake.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

    public void visitInstanciate(JFXInstanciate tree) {
        JFXExpression clazz = tree.getIdentifier();
        JFXClassDeclaration def = decompose(tree.getClassBody());
        List<JFXExpression> args = decompose(tree.getArgs());
        List<JFXObjectLiteralPart> parts = decompose(tree.getParts());
        List<JFXVar> localVars = decomposeContainer(tree.getLocalvars());
        result = fxmake.at(tree.pos).Instanciate(tree.getJavaFXKind(), clazz, def, args, parts, localVars);
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind |= tree.isUnidiBind();  //TODO: this may not be right
        JFXExpression expr = decompose(tree.getExpression());
        result = fxmake.at(tree.pos).ObjectLiteralPart(tree.name, expr, null);
        inUniBind = wasInUniBind;
    }

    public void visitTypeAny(JFXTypeAny tree) {
        result = tree;
    }

    public void visitTypeClass(JFXTypeClass tree) {
        result = tree;
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        result = tree;
    }

    public void visitTypeArray(JFXTypeArray tree) {
        result = tree;
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
        result = tree;
    }

    public void visitVarScriptInit(JFXVarScriptInit tree) {
        result = tree;
    }

    public void visitVar(JFXVar tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind |= tree.isUnidiBind();
        Name name = tree.name;
        JFXType type = tree.getJFXType();
        JFXModifiers mods = tree.getModifiers();
        JFXExpression init = decompose(tree.getInitializer());
        JFXOnReplace onReplace = decompose(tree.getOnReplace());
        result = fxmake.at(tree.pos).Var(name, type, mods,
                                        init, tree.getBindStatus(), onReplace);
        inUniBind = wasInUniBind;
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXVar oldValue = tree.getOldValue();
        JFXVar firstIndex = tree.getFirstIndex();
        JFXVar lastIndex = tree.getLastIndex();
        JFXVar newElements = tree.getNewElements();
        JFXBlock body = decompose(tree.getBody());
        result = fxmake.at(tree.pos).OnReplace(oldValue, firstIndex, lastIndex, tree.getEndKind(), newElements, body);
    }

    public void visitBlockExpression(JFXBlock tree) {
        List<JFXExpression> stats = decomposeContainer(tree.stats);
        JFXExpression value = decompose(tree.value);
        result = fxmake.at(tree.pos).Block(tree.flags, stats, value);
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = false;
        JFXType restype = tree.rettype;
        List<JFXVar> params = tree.getParams();
        JFXBlock bodyExpression = decompose(tree.bodyExpression);
        result = fxmake.at(tree.pos).FunctionValue(restype, params, bodyExpression);
        inUniBind = wasInUniBind;
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = tree;
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        JFXExpression lower = decompose(tree.getLower());
        JFXExpression upper = decompose(tree.getUpper());
        JFXExpression stepOrNull = decompose(tree.getStepOrNull());
        result = fxmake.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        List<JFXExpression> items = decompose(tree.getItems());
        result = fxmake.at(tree.pos).ExplicitSequence(items);
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression index = decompose(tree.getIndex());
        result = fxmake.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression firstIndex = decompose(tree.getFirstIndex());
        JFXExpression lastIndex = decompose(tree.getLastIndex());
        result = fxmake.at(tree.pos).SequenceSlice(sequence, firstIndex, lastIndex, tree.getEndKind());
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression element = decompose(tree.getElement());
        JFXExpression position = decompose(tree.getPosition());
        result = fxmake.at(tree.pos).SequenceInsert(sequence, element, position, tree.shouldInsertAfter());
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JFXExpression sequence = decompose(tree.getSequence());
        JFXExpression element = decompose(tree.getElement());
        result = fxmake.at(tree.pos).SequenceDelete(sequence, element);
    }

    public void visitForExpression(JFXForExpression tree) {
        List<JFXForExpressionInClause> inClauses = decompose(tree.inClauses);
        JFXExpression bodyExpr = decompose(tree.bodyExpr);
        result = fxmake.at(tree.pos).ForExpression(inClauses, bodyExpr);
    }

    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        JFXVar var = tree.var;
        JFXExpression seqExpr = decompose(tree.seqExpr);
        JFXExpression whereExpr = decompose(tree.whereExpr);
        result = fxmake.at(tree.pos).InClause(var, seqExpr, whereExpr);
    }

    public void visitIndexof(JFXIndexof tree) {
        result = tree;
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind |= tree.isUnidiBind();
        JFXIdent expr = decompose(tree.getId());
        JFXExpression initializer = decompose(tree.getInitializer());
        JFXOnReplace onr = decompose(tree.getOnReplace());
        result = fxmake.at(tree.pos).OverrideClassVar(expr, initializer, tree.getBindStatus(), onr);
        inUniBind = wasInUniBind;
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = true;
        JFXExpression attr = decompose(tree.attribute);
        JFXExpression value = decompose(tree.value);
        JFXExpression interpolation = decompose(tree.interpolation);
        result = fxmake.at(tree.pos).InterpolateValue(attr, value, interpolation);
        inUniBind = wasInUniBind;
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        JFXExpression start = decompose(tree.start);
        List<JFXExpression> values = decompose(tree.values);
        JFXExpression trigger = decompose(tree.trigger);
        result = fxmake.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }

}
