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
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.jvm.ClassReader;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.Context;
import java.util.HashMap;
import java.util.Map;

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
    private Symbol owner = null;
    private boolean inScriptLevel = true;
    private Map<JFXVar, JFXVar> varOldToNew = new HashMap<JFXVar, JFXVar>();
    private Map<JFXVar, JFXVarScriptInit> varOldToScriptInit = new HashMap<JFXVar, JFXVarScriptInit>();

    protected final JavafxTreeMaker fxmake;
    protected final JavafxDefs defs;
    protected final Name.Table names;
    protected final JavafxResolve rs;
    protected final JavafxSymtab syms;
    protected final JavafxTypes types;
    protected final ClassReader reader;

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
        reader = ClassReader.instance(context);
    }

    /**
     * External access: overwrite the top-level tree with the translated tree
     */
    public void decompose(JavafxEnv<JavafxAttrContext> attrEnv) {
        inUniBind = false;
        owner = null;
        lbVar = null;
        attrEnv.toplevel = decompose(attrEnv.toplevel);
        lbVar = null;
    }

    @SuppressWarnings("unchecked")
    private <T extends JFXTree> T decompose(T tree) {
        if (tree == null)
            return null;
        tree.accept(this);
        result.type = tree.type;
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
    
    private boolean requiresShred(JFXExpression tree) {
        if (tree==null) {
            return false;
        }
        return tree.getFXTag() == JavafxTag.APPLY;
    }

    private JFXExpression decomposeComponent(JFXExpression tree) {
        if (requiresShred(tree))
            return shred(tree);
        else
            return decompose(tree);
    }

    private List<JFXExpression> decomposeComponents(List<JFXExpression> trees) {
        if (trees == null)
            return null;
        ListBuffer<JFXExpression> lb = new ListBuffer<JFXExpression>();
        for (JFXExpression tree: trees)
            lb.append(decomposeComponent(tree));
        return lb.toList();
    }

    /**
     * If we are in a bound expression, break this expression out into a separate synthetic bound variable.
     */
    private JFXExpression shred(JFXExpression tree) {
        if (tree == null)
            return null;
        JFXExpression pose = decompose(tree);
        if (inUniBind) {
            Name vName = tempName();
            long flags = inScriptLevel? Flags.STATIC | JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC : 0L;
            JFXModifiers mod = fxmake.at(tree.pos).Modifiers(flags);
            JFXType fxType = fxmake.at(tree.pos).TypeAny(Cardinality.ANY);
            JFXVar v = fxmake.at(tree.pos).Var(vName, fxType, mod, pose, JavafxBindStatus.UNIDIBIND, null, null);
            VarSymbol sym = new VarSymbol(flags, vName, tree.type, owner);
            v.sym = sym;
            v.type = tree.type;
            lbVar.append(v);
            JFXIdent id = fxmake.at(tree.pos).Ident(vName);
            id.sym = sym;
            id.type = tree.type;
            return id;
        }
        return pose;
    }

    private List<JFXExpression> shred(List<JFXExpression> trees) {
        if (trees == null)
            return null;
        ListBuffer<JFXExpression> lb = new ListBuffer<JFXExpression>();
        for (JFXExpression tree: trees)
            lb.append(shred(tree));
        return lb.toList();
    }

    private Name tempName() {
        return names.fromString("$t" + varCount++);
    }

    Name syntheticClassName(Name superclass) {
        return names.fromString(superclass.toString() + "$anonD" + ++varCount);
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
        tree.defs = decomposeContainer(tree.defs);
        result = tree;
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
        JFXExpression cond = decomposeComponent(tree.cond);
        JFXExpression truepart = decomposeComponent(tree.truepart);
        JFXExpression falsepart = decomposeComponent(tree.falsepart);
        result = fxmake.at(tree.pos).Conditional(cond, truepart, falsepart);
    }

    public void visitBreak(JFXBreak tree) {
        result = tree;
    }

    public void visitContinue(JFXContinue tree) {
        result = tree;
    }

    public void visitReturn(JFXReturn tree) {
        tree.expr = decompose(tree.expr);
        result = tree;
    }

    public void visitThrow(JFXThrow tree) {
        result = tree;
    }

    public void visitFunctionInvocation(JFXFunctionInvocation tree) {
        JFXExpression fn = decompose(tree.meth);
        List<JFXExpression> args = shred(tree.args);
        result = fxmake.at(tree.pos).Apply(tree.typeargs, fn, args);
    }

    public void visitParens(JFXParens tree) {
        JFXExpression expr = decomposeComponent(tree.expr);
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
        JFXAssignOp res = fxmake.at(tree.pos).Assignop(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitUnary(JFXUnary tree) {
        JFXExpression arg = decomposeComponent(tree.arg);
        JavafxTag tag = tree.getFXTag();
        JFXUnary res = fxmake.at(tree.pos).Unary(tag, arg);
        res.operator = tree.operator;
        result = res;
    }

    public void visitBinary(JFXBinary tree) {
        JFXExpression lhs = decomposeComponent(tree.lhs);
        JFXExpression rhs = decomposeComponent(tree.rhs);
        JavafxTag tag = tree.getFXTag();
        JFXBinary res = fxmake.at(tree.pos).Binary(tag, lhs, rhs);
        res.operator = tree.operator;
        result = res;
    }

    public void visitTypeCast(JFXTypeCast tree) {
        JFXTree clazz = decompose(tree.clazz);
        JFXExpression expr = decomposeComponent(tree.expr);
        result = fxmake.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        JFXExpression expr = decomposeComponent(tree.expr);
        JFXTree clazz = decompose(tree.clazz);
        result = fxmake.at(tree.pos).TypeTest(expr, clazz);
    }

    public void visitSelect(JFXSelect tree) {
        JFXExpression selected;
        if (tree.sym.isStatic() || !types.isJFXClass(tree.sym.owner)) {
            // Referenced is static, then selected is a class reference
            selected = decompose(tree.selected);
        } else {
            selected = shred(tree.selected);
        }
        JFXSelect res = fxmake.at(tree.pos).Select(selected, tree.name);
        res.sym = tree.sym;
        result = res;
    }

    public void visitIdent(JFXIdent tree) {
        JFXIdent res = fxmake.at(tree.pos).Ident(tree.name);
        res.sym = tree.sym;
        result = res;
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
        tree.setMembers(decomposeContainer(tree.getMembers()));
        result = tree;
        inUniBind = wasInUniBind;
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        boolean wasInUniBind = inUniBind;
        inUniBind = tree.isBound();
        inScriptLevel = tree.sym.isStatic();
        Symbol prevOwner = owner;
        owner = tree.sym.owner;
        JFXModifiers mods = tree.mods;
        Name name = tree.getName();
        JFXType restype = tree.getJFXReturnType();
        List<JFXVar> params = decompose(tree.getParams());
        JFXBlock bodyExpression = decompose(tree.getBodyExpression());
        JFXFunctionDefinition res = fxmake.at(tree.pos).FunctionDefinition(mods, name, restype, params, bodyExpression);
        res.sym = tree.sym;
        result = res;
        inUniBind = wasInUniBind;
        inScriptLevel = wasInScriptLevel;
        owner = prevOwner;
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        Symbol prevOwner = owner;
        owner = tree.sym.owner;
        JFXBlock body = decompose(tree.body);
        JFXInitDefinition res = fxmake.at(tree.pos).InitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
        owner = prevOwner;
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        Symbol prevOwner = owner;
        owner = tree.sym.owner;
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        JFXBlock body = decompose(tree.body);
        JFXPostInitDefinition res = fxmake.at(tree.pos).PostInitDefinition(body);
        res.sym = tree.sym;
        result = res;
        inScriptLevel = wasInScriptLevel;
        owner = prevOwner;
    }

    public void visitStringExpression(JFXStringExpression tree) {
        List<JFXExpression> parts = decomposeComponents(tree.parts);
        result = fxmake.at(tree.pos).StringExpression(parts, tree.translationKey);
    }

    public void visitInstanciate(JFXInstanciate tree) {
        JFXExpression clazz = tree.getIdentifier();
        JFXClassDeclaration def = decompose(tree.getClassBody());
        List<JFXExpression> args = decomposeComponents(tree.getArgs());
        List<JFXObjectLiteralPart> parts = decompose(tree.getParts());
        List<JFXVar> localVars = decomposeContainer(tree.getLocalvars());
        JFXInstanciate res = fxmake.at(tree.pos).Instanciate(tree.getJavaFXKind(), clazz, def, args, parts, localVars);
        res.sym = tree.sym;
        res.constructor = tree.constructor;
        res.varDefinedByThis = tree.varDefinedByThis;
        result = res;
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind |= tree.isUnidiBind();  //TODO: this may not be right
        JFXExpression expr = decompose(tree.getExpression());
        JFXObjectLiteralPart res = fxmake.at(tree.pos).ObjectLiteralPart(tree.name, expr, null);
        res.sym = tree.sym;
        result = res;
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
        JFXVar ovar = tree.getVar();
        JFXVar var = varOldToNew.get(ovar);
        if (var == null) {
            // we don't have a mapping to decomposed var, add to map
            // requesting update.  And leave it untouched, for now.
            varOldToScriptInit.put(ovar, tree);
            result = tree;
        } else {
            result = fxmake.at(tree.pos).VarScriptInit(var);
        }
    }

    public void visitVar(JFXVar tree) {
        JFXVar already = varOldToNew.get(tree);
        if (already != null) {
            result = already;
        } else {
            Symbol prevOwner = this.owner;
            this.owner = tree.sym.owner;
            boolean wasInScriptLevel = inScriptLevel;
            inScriptLevel = tree.sym.isStatic();
            boolean wasInUniBind = inUniBind;
            // for on-replace, decompose as unbound 
            inUniBind = false;
            JFXOnReplace onReplace = decompose(tree.getOnReplace());
            JFXOnReplace onInvalidate = decompose(tree.getOnInvalidate());
            // bound if was bind context or is bound variable
            inUniBind = wasInUniBind | tree.isUnidiBind();

            JFXVar res = fxmake.at(tree.pos).Var(
                    tree.name,
                    tree.getJFXType(),
                    tree.getModifiers(),
                    decompose(tree.getInitializer()),
                    tree.getBindStatus(),
                    onReplace,
                    onInvalidate);
            res.sym = tree.sym;
            res.type = tree.type;
            varOldToNew.put(tree, res);
            JFXVarScriptInit vsi = varOldToScriptInit.get(tree);
            if (vsi != null) {
                // we have already seen this var in a VarScriptInit, update
                // the var
                vsi.resetVar(res);
            }
            
            inUniBind = wasInUniBind;
            this.owner = prevOwner;
            inScriptLevel = wasInScriptLevel;
            result = res;
        }
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
        tree.stats = decomposeContainer(tree.stats);
        tree.value = decompose(tree.value);
        result = tree;
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = false;
        tree.bodyExpression = decompose(tree.bodyExpression);
        result = tree;
        inUniBind = wasInUniBind;
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = tree;
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        JFXExpression lower = decomposeComponent(tree.getLower());
        JFXExpression upper = decomposeComponent(tree.getUpper());
        JFXExpression stepOrNull = decomposeComponent(tree.getStepOrNull());
        result = fxmake.at(tree.pos).RangeSequence(lower, upper, stepOrNull, tree.isExclusive());
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        List<JFXExpression> items = decomposeComponents(tree.getItems());
        result = fxmake.at(tree.pos).ExplicitSequence(items);
    }

    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        JFXExpression sequence = decomposeComponent(tree.getSequence());
        JFXExpression index = decomposeComponent(tree.getIndex());
        result = fxmake.at(tree.pos).SequenceIndexed(sequence, index);
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        JFXExpression sequence = decomposeComponent(tree.getSequence());
        JFXExpression firstIndex = decomposeComponent(tree.getFirstIndex());
        JFXExpression lastIndex = decomposeComponent(tree.getLastIndex());
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

    public void visitInvalidate(JFXInvalidate tree) {
        throw new AssertionError("Invalidate statement not supported yet"); //FIXME
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
        JFXForExpressionInClause res = fxmake.at(tree.pos).InClause(var, seqExpr, whereExpr);
        res.setIndexUsed(tree.getIndexUsed());
        result = res;
    }

    public void visitIndexof(JFXIndexof tree) {
        result = tree;
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = tree;
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        Symbol prevOwner = owner;
        owner = tree.sym.owner;
        boolean wasInScriptLevel = inScriptLevel;
        inScriptLevel = tree.sym.isStatic();
        boolean wasInUniBind = inUniBind;
        // on-replace is always unbound
        inUniBind = false;
        JFXOnReplace onReplace = decompose(tree.getOnReplace());
        // bound if was bind context or is bound variable
        inUniBind = wasInUniBind | tree.isUnidiBind();
        JFXExpression initializer = decompose(tree.getInitializer());
        JFXOverrideClassVar res = fxmake.at(tree.pos).OverrideClassVar(tree.getId(), initializer, tree.getBindStatus(), onReplace, null); //FIXME: on invalidate not supported
        res.sym = tree.sym;
        inUniBind = wasInUniBind;
        inScriptLevel = wasInScriptLevel;
        owner = prevOwner;
        result = res;
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        boolean wasInUniBind = inUniBind;
        inUniBind = true;
        JFXExpression attr = decompose(tree.attribute);
        JFXExpression funcValue = decompose(tree.funcValue);
        JFXExpression interpolation = decompose(tree.interpolation);
        // Note: funcValue takes the place of value
        JFXInterpolateValue res = fxmake.at(tree.pos).InterpolateValue(attr, funcValue, interpolation);
        res.sym = tree.sym;
        result = res;
        inUniBind = wasInUniBind;
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        JFXExpression start = decompose(tree.start);
        List<JFXExpression> values = decomposeComponents(tree.values);
        JFXExpression trigger = decompose(tree.trigger);
        result = fxmake.at(tree.pos).KeyFrameLiteral(start, values, trigger);
    }
}
