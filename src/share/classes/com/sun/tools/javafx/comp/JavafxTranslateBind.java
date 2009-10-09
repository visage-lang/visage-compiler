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
import com.sun.tools.javafx.code.JavafxClassSymbol;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.Name;

/**
 * Translate bind expressions into code in bind defining methods
 * 
 * @author Robert Field
 */
public class JavafxTranslateBind extends JavafxAbstractTranslation<ExpressionResult> implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateBind> jfxBoundTranslation =
        new Context.Key<JavafxTranslateBind>();

    Symbol targetSymbol;

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

    ExpressionResult translate(JFXExpression expr, Type targettedType, Symbol targetSymbol) {
        this.targetSymbol = targetSymbol;
        return translateToExpressionResult(expr, targettedType);
    }

/* ***************************************************************************
 * Visitor methods -- implemented (alphabetical order)
 ****************************************************************************/

    public void visitBinary(JFXBinary tree) {
        result = (new BinaryOperationTranslator(tree.pos(), tree)).doit();
    }

    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        result = (ExpressionResult) (new FunctionCallTranslator(tree) {
            JCExpression condition = null;

            @Override
            JCExpression translateArg(JFXExpression arg, Type formal) {
                if (arg instanceof JFXIdent) {
                    Symbol sym = ((JFXIdent) arg).sym;
                    JCVariableDecl oldVar = makeTmpVar("old", formal, id(attributeValueName(sym)));
                    JCVariableDecl newVar = makeTmpVar("new", formal, call(attributeGetterName(sym)));
                    addPreface(oldVar);
                    addPreface(newVar);
                    addBindee((VarSymbol) sym);

                    // oldArg != newArg
                    JCExpression compare = makeNotEqual(id(oldVar), id(newVar));
                    // concatenate with OR --  oldArg1 != newArg1 || oldArg2 != newArg2
                    condition = condition == null ? compare : makeBinary(JCTree.OR, condition, compare);

                    return id(newVar);
                } else {
                    return super.translateArg(arg, formal);
                }
            }

            @Override
            JCExpression fullExpression(JCExpression mungedToCheckTranslated) {
                JCExpression full = super.fullExpression(mungedToCheckTranslated);
                if (condition != null) {
                    // if no args have changed, don't call function, just return previous value
                    //TODO: must call if selector changes
                    full = m().Conditional(condition, full, id(attributeValueName(targetSymbol)));
                }
                return full;
            }
        }).doit();
    }

    public void visitIdent(JFXIdent tree) {
        result = new IdentTranslator(tree) {

            @Override
            protected ExpressionResult doit() {
                if (sym instanceof VarSymbol) {
                    VarSymbol vsym = (VarSymbol) sym;
                    if (currentClass().sym.isSubClass(sym.owner, types)) {
                        // The var is in our class (or a superclass)
                        if ((receiverContext() != ReceiverContext.ScriptAsStatic) && sym.isStatic()) {
                            // The reference is to a static, and we are in a non-static context
                            // so we need to add it to the interClass (different instances)
                            Name scriptName = sym.owner.name.append(defs.scriptClassSuffixName);
                            JavafxClassSymbol scriptClass = new JavafxClassSymbol(Flags.STATIC | Flags.PUBLIC | JavafxFlags.FX_CLASS, scriptName, sym.owner);
                            VarSymbol scriptLevel = new VarSymbol(
                                    Flags.STATIC,
                                    defs.scriptLevelAccessField.subName(1, defs.scriptLevelAccessField.length()),
                                    scriptClass.type,
                                    scriptClass);
                             // Prevent duplicates, remove it if it is already there
                            addPreface(callStmt(defs.FXBase_removeDependent,
                                    id(names._this),
                                    makeVarOffset(sym, null),
                                    call(defs.scriptLevelAccessMethod)));
                            // Add dependence on a script-level var
                            addPreface(callStmt(defs.FXBase_addDependent,
                                    id(names._this),
                                    makeVarOffset(sym, null),
                                    call(defs.scriptLevelAccessMethod)));
                            addInterClassBindee(scriptLevel, vsym);
                        } else {
                            addBindee(vsym);
                        }
                    } else {
                        // The reference is to a presumably outer class
                        //TODO:
                    }
                    
                }
                return super.doit();
            }
        }.doit();
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
    private class IfExpressionTranslator extends ExpressionTranslator {

        private final JFXIfExpression tree;
        private final JCVariableDecl resVar;

        IfExpressionTranslator(JFXIfExpression tree) {
            super(tree.pos());
            this.tree = tree;
            this.resVar = makeTmpVar("res", targetType, null);
        }

        JCStatement side(JFXExpression expr) {
            ExpressionResult res = translateToExpressionResult(expr, targetType);
            addBindees(res.bindees());
            addInterClassBindees(res.interClass());
            return m().Block(0L, res.statements().append(makeExec(m().Assign(id(resVar), res.expr()))));
        }

        protected ExpressionResult doit() {
            JCExpression cond = translateExpr(tree.getCondition(), syms.booleanType);
            addPreface(resVar);
            addPreface(m().If(
                    cond,
                    side(tree.getTrueExpression()),
                    side(tree.getFalseExpression())));
            return toResult( id(resVar), targetType );
        }
    }

    public void visitIfExpression(JFXIfExpression tree) {
        result = new IfExpressionTranslator(tree).doit();
    }

    public void visitLiteral(JFXLiteral tree) {
        // Just translate to literal value
        result = new ExpressionResult(translateLiteral(tree), tree.type);
    }

    public void visitParens(JFXParens tree) {
        result = translateToExpressionResult(tree.expr, targetType);
    }

    public void visitSelect(JFXSelect tree) {
        result = (new SelectTranslator(tree) {

            /**
             * Override to handle mutable selector changing dependencies
             *
             *  // def w = bind r.v
             * 	String get$w() {
             *	  if ( ! isValidValue$( VOFF$w ) ) {
             *	    Baz oldSelector = $r;
             *	    Baz newSelector = get$r();
             *	    switchDependence$(VOFF$v, oldSelector, newSelector);
             *	    be$w( (newSelector==null)? "" : newSelector.get$v() );
             *	  }
             *	  return $w;
             *	}
             */
            @Override
            protected ExpressionResult doit() {
                // cases that need a null check are the same as cases that have changing dependencies
                JFXExpression selectorExpr = tree.getExpression();
                if (needNullCheck() && (selectorExpr instanceof JFXIdent)) {
                    JFXIdent selector = (JFXIdent) selectorExpr;
                    Symbol selectorSym = selector.sym;
                    if (types.isJFXClass(selectorSym.owner)) {
                        Type selectorType = selector.type;
                        JCExpression offset = makeVarOffset(tree.sym, selectorSym);
                        JCExpression rcvr = selectorSym.isStatic()? call(defs.scriptLevelAccessMethod) : id(names._this);
                        JCVariableDecl oldSelector = makeTmpVar(selectorType, id(attributeValueName(selectorSym)));
                        JCVariableDecl newSelector = makeTmpVar(selectorType, call(attributeGetterName(selectorSym)));
                        addPreface(oldSelector);
                        addPreface(newSelector);
                        addPreface(callStmt(defs.FXBase_switchDependence,
                                rcvr,
                                offset,
                                id(oldSelector), 
                                id(newSelector)));
                        addPreface(makeDebugTrace("switchDependence", makeString(""+offset)));
                        addPreface(makeDebugTrace("from:", id(oldSelector)));
                        addPreface(makeDebugTrace("to:  ", id(newSelector)));
                    }
                    addBindee((VarSymbol)selectorSym);
                    addInterClassBindee((VarSymbol)selectorSym, refSym);
                }
                return (ExpressionResult) super.doit();
            }
        }).doit();
    }

    public void visitUnary(JFXUnary tree) {
        result = new UnaryOperationTranslator(tree).doit();
    }


/* ***************************************************************************
 * Visitor methods -- NOT implemented yet
 ****************************************************************************/

    JCExpression TODO(JFXTree tree) {
        return TODO("BIND functionality: " + tree.getClass().getSimpleName());
    }

    public void visitAssign(JFXAssign tree) {
        TODO(tree);
        //(tree.lhs);
        //(tree.rhs);
    }

    public void visitTypeCast(JFXTypeCast tree) {
        TODO(tree);
        //(tree.clazz);
        //(tree.expr);
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        TODO(tree);
        //(tree.expr);
        //(tree.clazz);
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        TODO(tree);
        for (JFXVar param : tree.getParams()) {
            //(param);
        }
        //(tree.getBodyExpression());
    }

    //@Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        TODO(tree);
    }
    
    //@Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        TODO(tree);
        //( that.getLower() );
        //( that.getUpper() );
        //( that.getStepOrNull() );
    }
    
    //@Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        TODO(tree);
        //( that.getItems() );
    }

    //@Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        TODO(tree);
        //(that.getSequence());
        //(that.getIndex());
    }
    
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        TODO(tree);
        //(that.getSequence());
        //(that.getFirstIndex());
        //(that.getLastIndex());
    }

    public void visitInvalidate(JFXInvalidate tree) {
        TODO(tree); //FIXME
    }

    public void visitStringExpression(JFXStringExpression tree) {
        TODO(tree);
        List<JFXExpression> parts = tree.getParts();
        parts = parts.tail;
        while (parts.nonEmpty()) {
            parts = parts.tail;
            //(parts.head);
            parts = parts.tail;
            parts = parts.tail;
        }
    }
    
    //@Override
    public void visitInstanciate(JFXInstanciate tree) {
        TODO(tree);
       //(tree.getIdentifier());
       //(tree.getArgs());
       //(tree.getParts());
       //(tree.getLocalvars());
       //(tree.getClassBody());
    }
    
    
    //@Override
    public void visitForExpression(JFXForExpression tree) {
        TODO(tree);
        for (ForExpressionInClauseTree cl : tree.getInClauses()) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)cl;
            //(clause);
        }
        //(that.getBodyExpression());
    }

    //@Override
    public void visitBlockExpression(JFXBlock tree) {
        TODO(tree);
        //(that.stats);
        //(that.value);
    }
    
    //@Override
    public void visitIndexof(JFXIndexof tree) {
        TODO(tree);
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        TODO(tree);
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        TODO(tree);
        //(that.attribute);
        //(that.value);
        if  (tree.interpolation != null) {
            //(that.interpolation);
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

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitContinue(JFXContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
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

    public void visitTry(JFXTry tree) {
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
