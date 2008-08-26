/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Creates a copy of a tree, using a given TreeMaker.
 * Names, literal values, etc are shared with the original.
 *
 * @author tball
 */
public class JavafxTreeCopier implements JavafxVisitor {
    private JavafxTreeMaker maker;
    private JFXTree result;

    /** Creates a new instance of TreeCopier */
    public JavafxTreeCopier(JavafxTreeMaker maker) {
        this.maker = maker;
    }

    @SuppressWarnings("unchecked")
    public <T extends JFXTree> T copy(T tree) {
        if (tree == null)
            return null;
        tree.accept(this);
        return (T)result;
    }

    public <T extends JFXTree> List<T> copy(List<T> trees) {
        if (trees == null)
            return null;
        ListBuffer<T> lb = new ListBuffer<T>();
        for (T tree: trees)
            lb.append(copy(tree));
        return lb.toList();
    }

    public void visitScript(JFXScript tree) {
        JFXExpression pid = copy(tree.pid);
        List<JFXTree> defs = copy(tree.defs);
        result = maker.at(tree.pos).Script(pid, defs);
    }

    public void visitImport(JFXImport tree) {
        JFXExpression qualid = copy(tree.qualid);
        result = maker.at(tree.pos).Import(qualid);
    }

    public void visitSkip(JFXSkip tree) {
        result = maker.at(tree.pos).Skip();
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        JFXExpression cond = copy(tree.cond);
        JFXExpression body = copy(tree.body);
        result = maker.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitTry(JFXTry tree) {
        JFXBlock body = copy(tree.body);
        List<JFXCatch> catchers = copy(tree.catchers);
        JFXBlock finalizer = copy(tree.finalizer);
        result = maker.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitCatch(JFXCatch tree) {
        JFXVar param = copy(tree.param);
        JFXBlock body = copy(tree.body);
        result = maker.at(tree.pos).Catch(param, body);
    }

    public void visitIfExpression(JFXIfExpression tree) {
        JFXExpression cond = copy(tree.cond);
        JFXExpression truepart = copy(tree.truepart);
        JFXExpression falsepart = copy(tree.falsepart);
        result = maker.at(tree.pos).Conditional(cond, truepart, falsepart);
    }

    public void visitBreak(JFXBreak tree) {
        result = maker.at(tree.pos).Break(tree.label);
    }

    public void visitContinue(JFXContinue tree) {
        result = maker.at(tree.pos).Continue(tree.label);
    }

    public void visitReturn(JFXReturn tree) {
        JFXExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Return(expr);
    }

    public void visitThrow(JFXThrow tree) {
        JFXExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Throw(expr);
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        List<JFXExpression> typeargs = copy(tree.typeargs);
        JFXExpression fn = copy(tree.meth);
        List<JFXExpression> args = copy(tree.args);
        result = maker.at(tree.pos).Apply(typeargs, fn, args);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).Parens(expr);
    }

    public void visitAssign(JFXAssign tree) {
        JFXExpression lhs = copy(tree.lhs);
        JFXExpression rhs = copy(tree.rhs);
        result = maker.at(tree.pos).Assign(lhs, rhs);
    }

    public void visitAssignop(JFXAssignOp tree) {
        JFXExpression lhs = copy(tree.lhs);
        JFXExpression rhs = copy(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Assignop(tag, lhs, rhs);
    }

    public void visitUnary(JFXUnary tree) {
        JFXExpression arg = copy(tree.arg);
        JavafxTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Unary(tag, arg);
    }

    public void visitBinary(JFXBinary tree) {
        JFXExpression lhs = copy(tree.lhs);
        JFXExpression rhs = copy(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        result = maker.at(tree.pos).Binary(tag, lhs, rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXTree clazz = copy(tree.clazz);
        JFXExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = copy(tree.expr);
        JFXTree clazz = copy(tree.clazz);
        result = maker.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(JFXSelect tree) {
        JFXExpression selected = copy(tree.selected);
        result = maker.at(tree.pos).Select(selected, tree.name);
    }

    public void visitIdent(JFXIdent tree) {
        result = maker.at(tree.pos).Ident(tree.name);
    }

    public void visitLiteral(JFXLiteral tree) {
        result = maker.at(tree.pos).Literal(tree.getTag(), tree.value);
    }

    public void visitModifiers(JFXModifiers tree) {
        result = maker.at(tree.pos).Modifiers(tree.flags);
    }

    public void visitErroneous(JFXErroneous tree) {
        List<? extends JFXTree> errs = copy(tree.errs);
        result = maker.at(tree.pos).Erroneous(errs);
    }

    public void visitBindExpression(JFXBindExpression tree) {
        JFXExpression expr = copy(tree.expr);
        result = maker.at(tree.pos).BindExpression(expr, tree.getBindStatus());
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXModifiers mods = copy(tree.mods);
        Name name = tree.getName();
        List<JFXExpression> supertypes = copy(tree.getSupertypes());
        List<JFXTree> defs = copy(tree.getMembers());
        result = maker.at(tree.pos).ClassDeclaration(mods, name, supertypes, defs);
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        JFXModifiers mods = copy(tree.mods);
        Name name = tree.getName();
        JFXType restype = copy(tree.getJFXReturnType());
        List<JFXVar> params = copy(tree.getParameters());
        JFXBlock bodyExpression = copy(tree.getBodyExpression());
        result = maker.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        JFXBlock body = tree.body;
        result = maker.at(tree.pos).InitDefinition(body);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        JFXBlock body = tree.body;
        result = maker.at(tree.pos).PostInitDefinition(body);
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = copy(tree.parts);
        result = maker.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

    public void visitInstanciate(JFXInstanciate tree) {
        JFXExpression clazz = copy(tree.getIdentifier());
        JFXClassDeclaration def = copy(tree.getClassBody());
        List<JFXExpression> args = copy(tree.getArgs());
        List<JFXObjectLiteralPart> parts = copy(tree.getParts());
        List<JFXVar> localVars = copy(tree.getLocalvars());
        result = maker.at(tree.pos).Instanciate(tree.getJavaFXKind(), clazz, def, args, parts, localVars);
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        JFXExpression expr = copy(tree.getExpression());
        result = maker.at(tree.pos).ObjectLiteralPart(tree.name, expr, null);
    }

    public void visitTypeAny(JFXTypeAny tree) {
        result = maker.at(tree.pos).TypeAny(tree.getCardinality());
    }

    public void visitTypeClass(JFXTypeClass tree) {
        JFXExpression clazz = copy(tree.getClassName());
        result = maker.at(tree.pos).TypeClass(clazz, tree.getCardinality());
    }

    public void visitTypeFunctional(JFXTypeFunctional tree) {
        List<JFXType> params = copy(tree.getParams());
        JFXType restype = copy(tree.restype);
        result = maker.at(tree.pos).TypeFunctional(params, restype, tree.getCardinality());
    }

    public void visitTypeUnknown(JFXTypeUnknown tree) {
        result = maker.at(tree.pos).TypeUnknown();
    }

    @Override
    public void visitVarScriptInit(JFXVarScriptInit tree) {
    }

    public void visitVar(JFXVar tree) {
        Name name = tree.name;
        JFXType type = copy(tree.getJFXType());
        JFXModifiers mods = copy(tree.getModifiers());
        JFXExpression init = copy(tree.getInitializer());
        JFXOnReplace onReplace = copy(tree.getOnReplace());
        result = maker.at(tree.pos).Var(name, type, mods, 
                                        init, tree.getBindStatus(), onReplace);
    }

    public void visitOnReplace(JFXOnReplace tree) {
        JFXVar oldValue = copy(tree.getOldValue());
        JFXVar firstIndex = copy(tree.getFirstIndex());
        JFXVar lastIndex = copy(tree.getLastIndex());
        JFXVar newElements = copy(tree.getNewElements());
        JFXBlock body = copy(tree.getBody());
        result = maker.at(tree.pos).OnReplace(oldValue, firstIndex, lastIndex, tree.getEndKind(), newElements, body);
    }

    public void visitBlockExpression(JFXBlock tree) {
        List<JFXExpression> stats = copy(tree.stats);
        JFXExpression value = copy(tree.value);
        result = maker.at(tree.pos).Block(tree.flags, stats, value);
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXType restype = copy(tree.rettype);
        List<JFXVar> params = copy(tree.getParams());
        JFXBlock bodyExpression = copy(tree.bodyExpression);
        result = maker.at(tree.pos).FunctionValue(restype, params, bodyExpression);
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = maker.at(tree.pos).EmptySequence();
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        JFXExpression lower = copy(tree.getLower());
        JFXExpression upper = copy(tree.getUpper());
        JFXExpression stepOrNull = copy(tree.getStepOrNull());
        result = maker.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        List<JFXExpression> items = copy(tree.getItems());
        result = maker.at(tree.pos).ExplicitSequence(items);
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        JFXExpression sequence = copy(tree.getSequence());
        JFXExpression index = copy(tree.getIndex());
        result = maker.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        JFXExpression sequence = copy(tree.getSequence());
        JFXExpression firstIndex = copy(tree.getFirstIndex());
        JFXExpression lastIndex = copy(tree.getLastIndex());
        result = maker.at(tree.pos).SequenceSlice(sequence, firstIndex, lastIndex, tree.getEndKind());
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        JFXExpression sequence = copy(tree.getSequence());
        JFXExpression element = copy(tree.getElement());
        JFXExpression position = copy(tree.getPosition());
        result = maker.at(tree.pos).SequenceInsert(sequence, element, position, tree.shouldInsertAfter());
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JFXExpression sequence = copy(tree.getSequence());
        JFXExpression element = copy(tree.getElement());
        result = maker.at(tree.pos).SequenceDelete(sequence, element);
    }

    public void visitForExpression(JFXForExpression tree) {
        List<JFXForExpressionInClause> inClauses = copy(tree.inClauses);
        JFXExpression bodyExpr = copy(tree.bodyExpr);
        result = maker.at(tree.pos).ForExpression(inClauses, bodyExpr);
    }

    public void visitForExpressionInClause(JFXForExpressionInClause tree) {
        JFXVar var = copy(tree.var);
        JFXExpression seqExpr = copy(tree.seqExpr);
        JFXExpression whereExpr = copy(tree.whereExpr);
        result = maker.at(tree.pos).InClause(var, seqExpr, whereExpr);
    }

    public void visitIndexof(JFXIndexof tree) {
        result = maker.at(tree.pos).Indexof(tree.fname);
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        JFXLiteral literal = copy(tree.value);
        result = maker.at(tree.pos).TimeLiteral(literal, tree.duration);
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        JFXIdent expr = copy(tree.getId());
        JFXExpression initializer = copy(tree.getInitializer());
        JFXOnReplace onr = copy(tree.getOnReplace());
        result = maker.at(tree.pos).OverrideClassVar(expr, initializer, tree.getBindStatus(), onr);
    }

    public void visitInterpolate(JFXInterpolate tree) {
        JFXExpression var = copy(tree.var);
        List<JFXInterpolateValue> values = copy(tree.values);
        result = maker.at(tree.pos).Interpolate(var, values);
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        JFXExpression attr = copy(tree.attribute);
        JFXExpression value = copy(tree.value);
        JFXExpression interpolation = copy(tree.interpolation);
        result = maker.at(tree.pos).InterpolateValue(attr, value, interpolation);
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        JFXExpression start = copy(tree.start);
        List<JFXExpression> values = copy(tree.values);
        JFXExpression trigger = copy(tree.trigger);
        result = maker.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }

}
