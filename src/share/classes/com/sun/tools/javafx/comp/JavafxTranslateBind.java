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

import com.sun.tools.javafx.tree.*;
import com.sun.javafx.api.tree.ForExpressionInClauseTree;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.Name;

/**
 * Translate bind expressions into code in bind defining methods
 * 
 * @author Robert Field
 */
public class JavafxTranslateBind extends JavafxAbstractTranslation<JavafxTranslateBind.Result> implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateBind> jfxBoundTranslation =
        new Context.Key<JavafxTranslateBind>();

    Symbol targetSymbol;

    public class Result {
        final List<JCStatement> stmts;
        final JCExpression value;
        final List<VarSymbol> bindees;
        Result(List<JCStatement> stmts, JCExpression value, List<VarSymbol> bindees) {
            this.stmts = stmts;
            this.value = value;
            this.bindees = bindees;
        }
        Result(ListBuffer<JCStatement> buf, JCExpression value, ListBuffer<VarSymbol> bindees) {
            this(buf.toList(), value, bindees.toList());
        }
        Result(JCExpression value) {
            this(List.<JCStatement>nil(), value, List.<VarSymbol>nil());
        }
    }

    public static JavafxTranslateBind instance(Context context) {
        JavafxTranslateBind instance = context.get(jfxBoundTranslation);
        if (instance == null) {
            JavafxToJava toJava = JavafxToJava.instance(context);
            instance = new JavafxTranslateBind(context, toJava);
        }
        return instance;
    }

    public JavafxTranslateBind(Context context, JavafxToJava toJava) {
        super(context, toJava);

        context.put(jfxBoundTranslation, this);
    }

    Result translate(JFXExpression expr, Symbol targetSymbol) {
        this.targetSymbol = targetSymbol;
        return translate(expr);
    }

/* ***************************************************************************
 * Visitor methods -- implemented (alphabetical order)
 ****************************************************************************/

    public void visitBinary(JFXBinary tree) {
        final ListBuffer<JCStatement> preface = ListBuffer.lb();
        final ListBuffer<VarSymbol> bindees = ListBuffer.lb();
        JCExpression value = (new BinaryOperationTranslator(tree.pos(), tree) {

            protected JCExpression translateArg(JFXExpression arg, Type type) {
                Result res = translate(arg);
                //TODO: convert type
                preface.appendList(res.stmts);
                bindees.appendList(res.bindees);
                return res.value;
            }
        }).doit();
        result = new Result(preface, value, bindees);
    }

    //TODO: merge with JavafxToJava version
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        final ListBuffer<JCStatement> preface = ListBuffer.lb();
        final ListBuffer<VarSymbol> bindees = ListBuffer.lb();
        JCExpression value = (new FunctionCallTranslator(tree) {
            private Name funcName = null;

            JCExpression translateExpression(JFXExpression expr) {
                Result res = translate(expr);
                //TODO: convert type
                preface.appendList(res.stmts);
                return res.value;
            }

            JCExpression translateExpression(JFXExpression expr, Type type) {
                return translateExpression(expr);
            }

            List<JCExpression> translateExpressions(List<JFXExpression> trees) {
                ListBuffer<JCExpression> list = ListBuffer.lb();
                for (JFXExpression expr : trees) {
                    list.append(translateExpression(expr));
                }
                return list.toList();
            }

            protected JCExpression doit() {
                JFXExpression toCheckOrNull;
                boolean knownNonNull;

                if (useInvoke) {
                    // this is a function var call, check the whole expression for null
                    toCheckOrNull = meth;
                    funcName = defs.invokeName;
                    knownNonNull = false;
                } else if (selector == null) {
                    // This is not an function var call and not a selector, so we assume it is a simple foo()
                    if (meth.getFXTag() == JavafxTag.IDENT) {
                        JFXIdent fr = fxm().Ident(functionName(msym, superToStatic, callBound));
                        fr.type = meth.type;
                        fr.sym = msym;
                        toCheckOrNull = fr;
                        funcName = null;
                        knownNonNull = true;
                    } else {
                        // Should never get here
                        assert false : meth;
                        toCheckOrNull = meth;
                        funcName = null;
                        knownNonNull = true;
                    }
                } else {
                    // Regular selector call  foo.bar() -- so, check the selector not the whole meth
                    toCheckOrNull = selector;
                    funcName = functionName(msym, superToStatic, callBound);
                    knownNonNull =  selector.type.isPrimitive() || !selectorMutable;
                }

                return (JCExpression) new NullCheckTranslator(diagPos, toCheckOrNull, returnType, knownNonNull) {

                    JCExpression translateToCheck(JFXExpression expr) {
                        JCExpression trans;
                        if (renameToSuper || superCall) {
                           trans = m().Ident(names._super);
                        } else if (renameToThis || thisCall) {
                           trans = m().Ident(names._this);
                        } else if (superToStatic) {
                            trans = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                        } else if (selector != null && !useInvoke && msym != null && msym.isStatic()) {
                            //TODO: clean this up -- handles referencing a static function via an instance
                            trans = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                        } else {
                            if (selector != null && msym != null && !msym.isStatic()) {
                                Symbol selectorSym = expressionSymbol(selector);
                                // If this is OuterClass.memberName() then we want to
                                // to create expression to get the proper receiver.
                                if (selectorSym != null && selectorSym.kind == Kinds.TYP) {
                                    trans = makeReceiver(diagPos, msym);
                                    return trans;
                                }
                            }

                            trans = translateExpression(expr);
                            if (expr.type.isPrimitive()) {
                                // Java doesn't allow calls directly on a primitive, wrap it
                                trans = makeBox(diagPos, trans, expr.type);
                            }
                        }
                        return trans;
                    }

                    @Override
                    JCExpression fullExpression( JCExpression mungedToCheckTranslated) {
                        ListBuffer<JCExpression> targs = ListBuffer.lb();
                        JCExpression condition = null;

                        // if this is a super.foo(x) call, "super" will be translated to referenced class,
                        // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
                        if (superToStatic) {
                            targs.append(make.Ident(defs.receiverName));
                        }

                        if (callBound) {
                            //TODO: this code looks completely messed-up
                            /**
                             * If this is a bound call, use left-hand side references for arguments consisting
                             * solely of a  var or attribute reference, or function call, otherwise, wrap it
                             * in an expression location
                             */
                            //TODO
                        } else {
                            boolean handlingVarargs = false;
                            Type formal = null;
                            List<Type> t = formals;
                            for (List<JFXExpression> l = tree.args; l.nonEmpty(); l = l.tail) {
                                if (!handlingVarargs) {
                                    formal = t.head;
                                    t = t.tail;
                                    if (usesVarArgs && t.isEmpty()) {
                                        formal = types.elemtype(formal);
                                        handlingVarargs = true;
                                    }
                                }
                                JFXExpression arg = l.head;
                                JCExpression targ;
                                if (magicIsInitializedFunction) {
                                    //TODO: in theory, this could have side-effects (but only in theory)
                                    //TODO: Lombard
                                    targ = translateExpression(l.head, formal);
                                } else {
                                    if (arg instanceof JFXIdent) {
                                        Symbol sym = ((JFXIdent) arg).sym;
                                        JCVariableDecl oldVar = makeTmpVar(diagPos, getSyntheticName("old"), formal, id(attributeValueName(sym)));
                                        JCVariableDecl newVar = makeTmpVar(diagPos, getSyntheticName("new"), formal, callExpression(diagPos, null, attributeGetterName(sym)));
                                        preface.append(oldVar);
                                        preface.append(newVar);
                                        bindees.append((VarSymbol)sym);

                                        // oldArg != newArg
                                        JCExpression compare = m().Binary(JCTree.NE, id(oldVar), id(newVar));
                                        // concatenate with OR --  oldArg1 != newArg1 || oldArg2 != newArg2
                                        condition = condition == null ? compare : m().Binary(JCTree.OR, condition, compare);

                                        targ = id(newVar);
                                    } else {
                                        targ = preserveSideEffects(formal, l.head, translateExpression(arg, formal));
                                    }
                                }
                                targs.append(targ);
                            }
                        }
                        JCExpression tc = mungedToCheckTranslated;
                        if (funcName != null) {
                            // add the selector name back
                            tc = m().Select(tc, funcName);
                        }
                        JCMethodInvocation app =  m().Apply(translateExpressions(tree.typeargs), tc, targs.toList());

                        JCExpression full = callBound ?
                              null //TODO
                            : app;
                        if (useInvoke) {
                            if (tree.type.tag != TypeTags.VOID) {
                                full = castFromObject(full, tree.type);
                            }
                        }
                        if (condition != null) {
                            full = m().Conditional(condition, full, id(attributeValueName(targetSymbol)));
                        }
                        return full;
                    }
                }.doit();
            }

        }).doit();
        result = new Result(preface, value, bindees);
    }

    public void visitIdent(JFXIdent tree) {
        // Just translate to get
        result = new Result(List.<JCStatement>nil(), translateIdent(tree), (tree.sym instanceof VarSymbol)? List.<VarSymbol>of((VarSymbol)tree.sym) : List.<VarSymbol>nil());
    }

    /**
     * Translate if-expression
     *
     * bind if (cond) foo else bar
     *
     * becomes preface statements:
     *
     *   T res;
     *   cond.preface;
     *   if (cond) {
     *     foo.preface;
     *     res = foo;
     *   } else {
     *     bar.preface;
     *     res = bar;
     *   }
     *
     * result value:
     *
     *   res
     *
     */
    private class IfExpressionTranslator extends Translator {

        private final JFXIfExpression tree;
        private final Type targetType;
        private final JCVariableDecl resVar;
        private final ListBuffer<VarSymbol> bindees = ListBuffer.lb();

        IfExpressionTranslator(JFXIfExpression tree) {
            super(tree.pos());
            this.tree = tree;
            this.targetType = tree.type;
            this.resVar = makeTmpVar(diagPos, getSyntheticName("res"), targetType, null);
        }

        JCStatement side(JFXExpression expr) {
            Result res = translate(expr, targetType);
            bindees.appendList(res.bindees);
            return m().Block(0L, res.stmts.append(m().Exec(m().Assign(id(resVar), res.value))));
        }

        protected JCStatement doit() {
            assert false : "should not reach here";
            return null;
        }

        Result result() {
            Result cond = translate(tree.getCondition());
            bindees.appendList(cond.bindees);
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            stmts.append(resVar);
            stmts.appendList(cond.stmts);
            stmts.append(make.at(diagPos).If(
                    cond.value,
                    side(tree.getTrueExpression()),
                    side(tree.getFalseExpression())));
            return new Result(stmts, id(resVar), bindees);
        }
    }

    public void visitIfExpression(JFXIfExpression tree) {
        result = new IfExpressionTranslator(tree).result();
    }

    public void visitLiteral(JFXLiteral tree) {
        // Just translate to literal value
        result = new Result(translateLiteral(tree));
    }

    public void visitParens(JFXParens tree) {
        result = translate(tree.expr);
    }

    public void visitUnary(JFXUnary tree) {
        final ListBuffer<JCStatement> preface = ListBuffer.lb();
        final ListBuffer<VarSymbol> bindees = ListBuffer.lb();
        JCExpression value = (new UnaryOperationTranslator(tree) {

            JCExpression translateExpression(JFXExpression expr, Type type) {
                Result res = translate(expr);
                //TODO: convert type
                preface.appendList(res.stmts);
                bindees.appendList(res.bindees);
                return res.value;
            }
        }).doit();
        result = new Result(preface, value, bindees);
    }


/* ***************************************************************************
 * Visitor methods -- NOT implemented yet
 ****************************************************************************/

    public void visitAssign(JFXAssign tree) {
        translate(tree.lhs);
        translate(tree.rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        //(tree.clazz);
        translate(tree.expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        translate(tree.expr);
        //(tree.clazz);
    }

    public void visitSelect(JFXSelect tree) {
        translate(tree.selected);
    }

    //@Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        for (JFXVar param : tree.getParams()) {
            translate(param);
        }
        translate(tree.getBodyExpression());
    }

    //@Override
    public void visitSequenceEmpty(JFXSequenceEmpty that) {
    }
    
    //@Override
    public void visitSequenceRange(JFXSequenceRange that) {
        translate( that.getLower() );
        translate( that.getUpper() );
        translate( that.getStepOrNull() );
    }
    
    //@Override
    public void visitSequenceExplicit(JFXSequenceExplicit that) {
        translate( that.getItems() );
    }

    //@Override
    public void visitSequenceIndexed(JFXSequenceIndexed that) {
        translate(that.getSequence());
        translate(that.getIndex());
    }
    
    public void visitSequenceSlice(JFXSequenceSlice that) {
        translate(that.getSequence());
        translate(that.getFirstIndex());
        translate(that.getLastIndex());
    }

    public void visitStringExpression(JFXStringExpression that) {
        List<JFXExpression> parts = that.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            translate(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    //@Override
    public void visitInstanciate(JFXInstanciate tree) {
       translate(tree.getIdentifier());
       translate(tree.getArgs());
       translate(tree.getParts());
       translate(tree.getLocalvars());
       translate(tree.getClassBody());
    }
    
    
    //@Override
    public void visitForExpression(JFXForExpression that) {
        for (ForExpressionInClauseTree cl : that.getInClauses()) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
            //(clause);
        }
        translate(that.getBodyExpression());
    }

    //@Override
    public void visitBlockExpression(JFXBlock that) {
        translate(that.stats);
        translate(that.value);
    }
    
    //@Override
    public void visitIndexof(JFXIndexof that) {
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
    }

    public void visitInterpolateValue(JFXInterpolateValue that) {
        translate(that.attribute);
        translate(that.value);
        if  (that.interpolation != null) {
            translate(that.interpolation);
        }
    }


    /***********************************************************************
     *
     * Utilities
     *s
     */

    protected String getSyntheticPrefix() {
        return "bfx$";
    }


    /***********************************************************************
     *
     * Moot visitors  (alphabetical order)
     *
     */

    public void visitAssignop(JFXAssignOp tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitBreak(JFXBreak tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitCatch(JFXCatch tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitContinue(JFXContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitErroneous(JFXErroneous tree) {
        assert false : "erroneous nodes shouldn't have gotten this far";
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitImport(JFXImport tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitModifiers(JFXModifiers tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitReturn(JFXReturn tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitScript(JFXScript tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitSkip(JFXSkip tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitThrow(JFXThrow tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTree(JFXTree that) {
        assert false : "Should not be here!!!";
    }

    public void visitTry(JFXTry tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTypeAny(JFXTypeAny that) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTypeClass(JFXTypeClass that) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTypeFunctional(JFXTypeFunctional that) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTypeArray(JFXTypeArray tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitVar(JFXVar tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitVarScriptInit(JFXVarScriptInit tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        assert false : "should not be processed as part of a binding";
    }
}
