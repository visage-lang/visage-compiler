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
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.javafx.comp.JavafxDefs.RuntimeMethod;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

/**
 * Translate bind expressions into code in bind defining methods
 * 
 * @author Robert Field
 */
public class JavafxTranslateBind extends JavafxAbstractTranslation implements JavafxVisitor {

    protected static final Context.Key<JavafxTranslateBind> jfxBoundTranslation =
        new Context.Key<JavafxTranslateBind>();

    // Symbol for the var whose bound expression we are translating.
    private VarSymbol targetSymbol;

    // Is this a bi-diractional bind?
    private boolean isBidiBind;

    // The outer bound expression
    private JFXExpression boundExpression;

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

    static JCExpression TODO(String msg, JFXExpression tree) {
        return TODO(msg + " -- " + tree.getClass());
    }

    /**
     * Entry-point into JavafxTranslateBind.
     *
     * @param expr Bound expression to translate.  Directly held by a var (or bound function?). Not a sub-expression/
     * @param targetSymbol Symbol for the var whose bound expression we are translating.
     * @param isBidiBind Is this a bi-diractional bind?
     * @return
     */
    ExpressionResult translateBoundExpression(JFXExpression expr, VarSymbol targetSymbol, boolean isBidiBind) {
        this.targetSymbol = targetSymbol;
        this.isBidiBind = isBidiBind;
        this.boundExpression = expr;
        // Special case: If the targetSymbol is a bound function result, then 
        // make the expected type to be Pointer or else make it null.
        ExpressionResult res = translateToExpressionResult(expr,
                isBoundFunctionResult(targetSymbol)? syms.javafx_PointerType : null);
        this.targetSymbol = null;
        this.boundExpression = null;
        return res;
    }

/****************************************************************************
 *                     Bound Non-Sequence Translators
 ****************************************************************************/

    /**
     * Translate a bound function call
     */
    private class BoundFunctionCallTranslator extends FunctionCallTranslator {

        BoundFunctionCallTranslator(JFXFunctionInvocation tree) {
            super(tree);
        }
        JCExpression condition = null;

        @Override
        List<JCExpression> determineArgs() {
            ListBuffer<JCExpression> targs = ListBuffer.lb();
            // if this is a super.foo(x) call, "super" will be translated to referenced class,
            // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
            if (superToStatic) {
                targs.append(id(defs.receiverName));
            }

            if (callBound) {
                for (JFXExpression arg : args) {
                    if (arg.getFXTag() == JavafxTag.IDENT) {
                        JFXIdent ident = (JFXIdent)arg;
                        targs.append(getReceiverOrThis(ident.sym));
                        targs.append(Offset(ident.sym));
                    } else {
                        TODO("non-Ident in bound call");
                    }
                }
                return targs.toList();
            } else {
                return super.determineArgs();
            }
        }

        @Override
        JCExpression translateArg(JFXExpression arg, Type formal) {
            if (arg instanceof JFXIdent) {
                Symbol sym = ((JFXIdent) arg).sym;
                JCVariableDecl oldVar = TmpVar("old", formal, Get(sym));
                JCVariableDecl newVar = TmpVar("new", formal, Call(attributeGetterName(sym)));
                addPreface(oldVar);
                addPreface(newVar);
                addBindee((VarSymbol) sym);   //TODO: isn't this redundant?

                // oldArg != newArg
                JCExpression compare = NE(id(oldVar), id(newVar));
                // concatenate with OR --  oldArg1 != newArg1 || oldArg2 != newArg2
                condition = condition == null ? compare : OR(condition, compare);

                return id(newVar);
            } else {
                return super.translateArg(arg, formal);
            }
        }

        @Override
        JCExpression fullExpression(JCExpression mungedToCheckTranslated) {
            if (callBound) {
                // call to a bound function in bind context
                JCExpression tMeth = Select(mungedToCheckTranslated, methodName());
                return m().Apply(translateExprs(typeargs), tMeth, determineArgs());
            } else {
                JCExpression full = super.fullExpression(mungedToCheckTranslated);
                if (condition != null) {
                    // if no args have changed, don't call function, just return previous value
                    //TODO: must call if selector changes
                    full = 
                        If (condition,
                            full,
                            Get(targetSymbol));
                }
                return full;
            }
        }
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
    private class BoundIfExpressionTranslator extends ExpressionTranslator {

        private final JFXIfExpression tree;
        private final JCVariableDecl resVar;
        private final Type type;

        BoundIfExpressionTranslator(JFXIfExpression tree) {
            super(tree.pos());
            this.tree = tree;
            this.type = (targetType != null)? targetType : tree.type;
            this.resVar = TmpVar("res", type, null);
        }

        JCStatement side(JFXExpression expr) {
            ExpressionResult res = translateToExpressionResult(expr, type);
            addBindees(res.bindees());
            addInterClassBindees(res.interClass());
            return Block(res.statements().append(Stmt(m().Assign(id(resVar), res.expr()))));
        }

        protected ExpressionResult doit() {
            JCExpression cond = translateExpr(tree.getCondition(), syms.booleanType);
            addPreface(resVar);
            addPreface(If (cond,
                    side(tree.getTrueExpression()),
                    side(tree.getFalseExpression())));
            return toResult( id(resVar), type );
        }
    }

    /**
     * Bound member select (non-sequence)
     */
    private class BoundSelectTranslator extends SelectTranslator {

        BoundSelectTranslator(JFXSelect tree) {
            super(tree);
        }

        @Override
        protected ExpressionResult doit() {
            JFXExpression selectorExpr = tree.getExpression();
            if (canChange() && (selectorExpr instanceof JFXIdent)) {
                // cases that need a null check are the same as cases that have changing dependencies
                JFXIdent selector = (JFXIdent) selectorExpr;
                Symbol selectorSym = selector.sym;
                buildDependencies(selectorSym);
                addBindee((VarSymbol) selectorSym);
                addInterClassBindee((VarSymbol) selectorSym, refSym);
            }
            return (ExpressionResult) super.doit();
        }

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
        protected void buildDependencies(Symbol selectorSym) {
                if (types.isJFXClass(selectorSym.owner)) {
                    Type selectorType = selectorSym.type;
                    JCExpression rcvr = getReceiverOrThis(targetSymbol);
 
                    JCVariableDecl oldSelector = TmpVar(selectorType, Get(selectorSym));
                    addPreface(oldSelector);
                    JCVariableDecl newSelector = TmpVar(selectorType, Call(attributeGetterName(selectorSym)));
                    addPreface(newSelector);
                    
                    JCExpression oldOffset;
                    JCExpression newOffset;
                  
                    if (isMixinVar(selectorSym)) {
                        oldOffset =
                            If (EQnull(id(newSelector)),
                                Int(0),
                                Offset(id(oldSelector), tree.sym));
                        
                        newOffset =
                            If (EQnull(id(newSelector)),
                                Int(0),
                                Offset(id(newSelector), tree.sym));
                    } else {
                        JCVariableDecl offsetVar = TmpVar(syms.intType, Offset(tree.sym));
                        addPreface(offsetVar);
                        oldOffset = id(offsetVar);
                        newOffset = id(offsetVar);
                    }

                    if (isBidiBind) {
                        JCVariableDecl selectorOffset = TmpVar(syms.intType, Offset(targetSymbol));
                        addPreface(selectorOffset);
                        
                        addPreface(CallStmt(defs.FXBase_switchBiDiDependence,
                                rcvr,
                                id(selectorOffset),
                                id(oldSelector), oldOffset,
                                id(newSelector), newOffset));
                    } else {
                        addPreface(CallStmt(defs.FXBase_switchDependence,
                                rcvr,
                                id(oldSelector), oldOffset,
                                id(newSelector), newOffset));
                    }
                }
        }
    }

/****************************************************************************
 *                     Bound Sequence Translators
 ****************************************************************************/

    /**
     * Abstract super class of bound sequence Translators.
     *
     * Provides the framework of abstract methods that must be implemented:
     *       makeSizeBody(), makeGetElementBody(), setupInvalidators()
     *
     * And provides common utilities
     */
    private abstract class BoundSequenceTranslator extends ExpressionTranslator {


        abstract JCStatement makeSizeBody();
        abstract JCStatement makeGetElementBody();
        abstract void setupInvalidators();

        BoundSequenceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        BoundSequenceResult doit() {
            setupInvalidators();
            return new BoundSequenceResult(invalidators(), interClass(), makeGetElementBody(), makeSizeBody());
        }

        JCExpression CallSize(Symbol sym) {
            return CallSize(null, sym);
        }

        JCExpression CallSize(JCExpression rcvr, Symbol sym) {
            return Call(rcvr, attributeSizeName(sym));
        }

        JCExpression CallGetElement(Symbol sym, JCExpression pos) {
            return CallGetElement(null, sym, pos);
        }

        JCExpression CallGetElement(JCExpression rcvr, Symbol sym, JCExpression pos) {
            return Call(rcvr, attributeGetElementName(sym), pos);
        }

        JCExpression CallGetter(Symbol sym) {
            return Call(attributeGetterName(sym));
        }

        JCExpression Undefined() {
            return Int(JavafxDefs.UNDEFINED_MARKER_INT);
        }

        JCStatement CallSeqInvalidate() {
            return CallSeqInvalidateUndefined(targetSymbol);
        }

        JCStatement CallSeqInvalidateUndefined(Symbol sym) {
            return CallSeqInvalidate(sym, Int(0), Undefined(), Undefined(), id(defs.varFlagIS_INVALID));
        }

        JCStatement CallSeqInvalidate(JCExpression begin, JCExpression end, JCExpression newLen) {
            return CallSeqInvalidate(targetSymbol, begin, end, newLen);
        }

        JCStatement CallSeqInvalidate(Symbol sym, JCExpression begin, JCExpression end, JCExpression newLen) {
            return CallSeqInvalidate(sym, begin, end, newLen, phaseArg());
        }

        JCStatement CallSeqInvalidate(Symbol sym, JCExpression begin, JCExpression end, JCExpression newLen, JCExpression phase) {
            return CallStmt(attributeInvalidateName(sym), begin, end, newLen, phase);
        }

        private Name activeFlagBit = defs.varFlagDEFAULT_APPLIED;
        VarSymbol flagSymbol = (VarSymbol)targetSymbol;

        JCExpression isSequenceActive() {
            return FlagTest(flagSymbol, activeFlagBit, activeFlagBit);
        }

        JCExpression isSequenceDormantSetActive() {
            return NOT(FlagChange(flagSymbol, null, activeFlagBit));
        }

        JCStatement setSequenceActive() {
            return FlagChangeStmt(flagSymbol, null, activeFlagBit);
        }

        JCExpression IsInvalidatePhase() {
            return EQ(phaseArg(), id(defs.varFlagIS_INVALID));
        }

        JCExpression IsTriggerPhase() {
            return EQ(phaseArg(), id(defs.varFlagNEEDS_TRIGGER));
        }

        JCStatement Assign(JCExpression vid, JCExpression value) {
            return Stmt(m().Assign(vid, value));
        }
        JCStatement Assign(JCVariableDecl var, JCExpression value) {
            return Assign(id(var), value);
        }
    }

    /**
     * Bound identifier Translator for identifiers referencing sequences
     *
     * Just forward the requests for size and elements
     */
    class BoundIdentSequenceTranslator extends BoundSequenceTranslator {
        private final JFXIdent tree;
        private final ExpressionResult exprResult;
        BoundIdentSequenceTranslator(JFXIdent tree, ExpressionResult exprResult) {
            super(tree.pos());
            this.tree = tree;
            this.exprResult = exprResult;
        }

        JCStatement makeSizeBody() {
            return Return(CallSize(tree.sym));
        }

        JCStatement makeGetElementBody() {
            return Return(CallGetElement(tree.sym, posArg()));
        }

        /**
         * Simple bindee info from normal translation will do it
         */
        void setupInvalidators() {
            mergeResults(exprResult);
        }
    }

    /**
     * Bound type-cast Translator for type-cast from-and-to sequences
     *
     * Just forward the requests for size and elements, the latter type-converted
     * to its the desired element type.
     */
    class BoundTypeCastSequenceTranslator extends BoundSequenceTranslator {

        private final VarSymbol exprSym;
        private final Type elemType;

        BoundTypeCastSequenceTranslator(JFXTypeCast tree) {
            super(tree.pos());
            assert types.isSequence(tree.type);
            assert tree.getExpression() instanceof JFXIdent; // Decompose should shred
            this.exprSym = (VarSymbol)((JFXIdent)tree.getExpression()).sym;
            assert types.isSequence(tree.getExpression().type);
            this.elemType = types.elementType(tree.type);
        }

        JCStatement makeSizeBody() {
            return Return(CallSize(exprSym));
        }

        JCStatement makeGetElementBody() {
            return Return(m().TypeCast(elemType, CallGetElement(exprSym, posArg())));
        }

        /**
         * Simple bindee info from normal translation will do it
         */
        void setupInvalidators() {
            addBindee(exprSym);
        }
    }

    /**
     * Bound empty sequence Translator
     *
     * Size is always zero, element is always an error (so return default value)
     */
    class BoundEmptySequenceTranslator extends BoundSequenceTranslator {
        private final Type elemType;
        BoundEmptySequenceTranslator(JFXSequenceEmpty tree) {
            super(tree.pos());
            this.elemType = types.elementType(tree.type);
        }

        JCStatement makeSizeBody() {
            return Return(Int(0));
        }

        JCStatement makeGetElementBody() {
            return Return(DefaultValue(elemType));
        }

        void setupInvalidators() {
            // nada
        }
    }

    /**
     * Bound member-select reference to a sequence Translator
     *
     *
     */
    private class BoundSelectSequenceTranslator extends BoundSequenceTranslator {

        private final SelectTranslator strans;
        private final Symbol refSym;
        private final VarSymbol selfSym = (VarSymbol) targetSymbol;
        private final VarSymbol selectorSym;
        private final VarSymbol sizeSym;


        BoundSelectSequenceTranslator(JFXSelect tree) {
            super(tree.pos());
            this.strans = new SelectTranslator(tree);
            this.refSym = strans.refSym;
            this.sizeSym = tree.boundSize.sym;
            JFXExpression selectorExpr = tree.getExpression();
            assert canChange();
            assert (selectorExpr instanceof JFXIdent);
            JFXIdent selector = (JFXIdent) selectorExpr;
            this.selectorSym = (VarSymbol) selector.sym;
        }

        /*** forward to SelectTranslator ***/
        private JFXExpression getToCheck() { return strans.getToCheck(); }
        private JCExpression translateToCheck(JFXExpression expr) { return strans.translateToCheck(expr); }
        private boolean canChange() { return strans.canChange(); }
        private JCExpression wrapInNullCheckExpression(JCExpression full, JCExpression tToCheck, Type theResultType, Type theFullType) {
             return strans.wrapInNullCheckExpression(full, tToCheck, theResultType, theFullType);
        }
        private JCStatement wrapInNullCheckStatement(JCExpression full, JCExpression tToCheck, Type theResultType, Type theFullType) {
             return strans.wrapInNullCheckStatement(full, tToCheck, theResultType, theFullType);
        }

        private JCExpression selector() {
            return Get(selectorSym);
        }

        /**
         * Size accessor
         * (
         *     if ( default-not-set ) {
         *         set-default-flag;
         *         clear selector's trigger flag
         *         invalidate the selector
         *     }
         *     // redirect to the size of the referenced sequence, updating the selector
         *     return get$selector()==null? 0 : get$selector().size$ref();
         * }
         *
         */
        JCStatement makeSizeBody() {
            JCExpression tToCheck = translateToCheck(getToCheck());

            return
                Block(
                    If (isSequenceDormantSetActive(),
                        Block(
                            FlagChangeStmt(selectorSym, defs.varFlagNEEDS_TRIGGER, null),
                            CallStmt(attributeInvalidateName(selectorSym), id(defs.varFlagNEEDS_TRIGGER))
                        )
                    ),
                    buildBody(tToCheck, CallSize(tToCheck, refSym), syms.intType)
                );
        }

        /**
         * Get sequence element
         * {
         *     size$self(); // Access size to make sure we are initialized
         *     return get$selector()==null? <default> : get$selector().get$ref(pos);
         * }
         */
        JCStatement makeGetElementBody() {
            JCExpression tToCheck = translateToCheck(getToCheck());

            return
                Block(
                    Stmt(CallSize(selfSym)),
                    buildBody(tToCheck, CallGetElement(tToCheck, refSym, posArg()), types.elementType(refSym.type))
                );
        }

        protected JCStatement buildBody(JCExpression tToCheck, JCExpression full, Type theResultType) {
            return wrapInNullCheckStatement(full, tToCheck, theResultType, theResultType);
        }

        /**
         *  $selector === null? 0 : $selector.size$ref();
         */
        private JCExpression getSize() {
            if (refSym.isStatic()) {
                return CallSize(refSym);
            } else {
                return
                    If (EQnull(selector()),
                        Int(0),
                        CallSize(selector(), refSym));
            }
        }

        /**
         * Invalidator for the selector
         *
         * invalidator$selector(int phase) {
         *     if (valid-for-this-phase) {
         *         clear-valid-for-this-phase;
         *         if ( is-invalidate-phase) {
         *             // remove dependent, do it now, so updates from referenced don't interject
         *             removeDependent($selector, VOFF$ref);
         *             // invalidate with undefined newSize, since we can't compute without enter trigger phase
         *             invalidate$self(0, undefined, undefined, phase);
         *         } else {
         *             int oldSize = $selector === null? 0 : $selector.size$ref();
         *             get$selector();  // update selector
         *             int newSize = $selector === null? 0 : $selector.size$ref();
         *             addDependent($selector, VOFF$ref);
         *             invalidate$self(0, oldSize, newSize, phase);
         *         }
         *     }
         * }
         */
        private JCStatement makeInvalidateSelector() {
            JCVariableDecl oldSize = TmpVar(syms.intType, getSize()); 

            return
                If (NOT(FlagChange(selectorSym, null, phaseArg())),
                    Block(
                        If (IsInvalidatePhase(),
                            Block(
                                If (NEnull(selector()),
                                    CallStmt(defs.FXBase_removeDependent,
                                       selector(),
                                       Offset(selector(), refSym),
                                       getReceiverOrThis(selectorSym)
                                    )
                                ),
                                CallSeqInvalidateUndefined(selfSym)
                            ),
                        /*Else (Trigger phase)*/
                            Block(
                                oldSize,
                                CallStmt(attributeGetterName(selectorSym)),
                                If (NEnull(selector()),
                                    CallStmt(defs.FXBase_addDependent,
                                        selector(),
                                        Offset(selector(), refSym),
                                        getReceiverOrThis(selectorSym)
                                    )
                                ),
                                CallSeqInvalidate(selfSym,
                                    Int(0),
                                    id(oldSize),
                                    getSize()
                                )
                            )
                        )
                    )
                );
        }

        /**
         * Addition to the invalidate for this bound select sequence
         *
         * invalidate$self(int start, int end, int newLen, int phase) {
         *     if ( is-trigger-phase ) {
         *         $size = $size + newLen - (end - start);
         *     }
         *     ....
         * }
         */
        private JCStatement makeInvalidateSelf() {
            return
                If (IsTriggerPhase(),
                    SetStmt(sizeSym,
                        PLUS(
                            Get(sizeSym),
                            MINUS(
                                newLengthArg(),
                                MINUS(
                                    endPosArg(),
                                    startPosArg()
                                )
                            )
                        )
                    )
                );
        }

        @Override
        void setupInvalidators() {
                addInvalidator(selectorSym, makeInvalidateSelector());
                addInvalidator(selfSym, makeInvalidateSelf());
                addInterClassBindee(selectorSym, refSym);
        }
    }

    /**
     * Bound explicit sequence Translator ["hi", [2..k], x]
     *
     *
     */
    private class BoundExplicitSequenceTranslator extends BoundSequenceTranslator {
        private final List<JFXVar> vars;
        private final Type elemType;
        private final VarSymbol sizeSymbol;
        private final int length;
        BoundExplicitSequenceTranslator(JFXSequenceExplicit tree) {
            super(tree.pos());
            this.vars = tree.boundItemsVars;
            this.length = vars.length();
            this.elemType = types.elementType(tree.type);
            this.sizeSymbol = tree.boundSizeVar.sym;
        }

        private boolean isNullable(int index) {
            Type type = type(index);
            //TODO: better test that looks at the actual expression (eg. object literal)
            return !type.isPrimitive() &&
                    type != syms.javafx_StringType &&
                    type != syms.javafx_DurationType;
        }

        private boolean isSequence(int index) {
            return types.isSequence(type(index));
        }

        private VarSymbol vsym(int index) {
            return vars.get(index).sym;
        }

        private Type type(int index) {
            return vars.get(index).type;
        }

        private JCExpression CallGetter(int index) {
            return CallGetter(vsym(index));
        }

        private JCExpression computeSize(int index, JCExpression value) {
            if (isSequence(index)) {
                return CallSize(vsym(index));
            } else if (isNullable(index)) {
                return If (EQnull(value), Int(0), Int(1));
            } else {
                return Int(1);
            }
        }

        private JCExpression computeSize(int index) {
            return computeSize(index, Get(vsym(index)));
        }

        private JCExpression cummulativeSize(int index) {
            JCExpression sum = Int(0);
            for (int i = 0; i < index; ++i) {
                sum = PLUS(sum, computeSize(i));
            }
            return sum;
        }

        private JCExpression CallGetElement(int index, JCExpression pos) {
            if (isSequence(index)) {
                return Call(attributeGetElementName(vsym(index)), pos);
            } else {
                return CallGetter(index);
            }
        }

        JCStatement makeSizeBody() {
            return Return(Call(attributeGetterName(sizeSymbol)));
        }

        /**
         * if (pos < 0) {
         *    return null; // default
         * }
         * int start = 0;
         * int next = 0;
         * next += size$s1();
         * if (pos < next) {
         *    return get$s1(pos – start);
         * }
         * start = next;
         * next += size$s2();
         * if (pos < next) {
         *    return get$s2(pos – start);
         * }
         * start = next;
         * next += size$s3();
         * if (pos < next) {
         *    return get$s3(pos – start);
         * )
         * return null; // default
         */
        JCStatement makeGetElementBody() {
            JCVariableDecl vStart = MutableTmpVar("start", syms.intType, Int(0));
            JCVariableDecl vNext = MutableTmpVar("next", syms.intType, Int(0));
            ListBuffer<JCStatement> stmts = ListBuffer.lb();

            stmts.append(If(LT(posArg(), Int(0)), Return(DefaultValue(elemType))));
            stmts.append(vStart);
            stmts.append(vNext);
            for (int index = 0; index < length; ++index) {
                stmts.append(Assign(vNext, PLUS(id(vNext), computeSize(index))));
                stmts.append(If(LT(posArg(), id(vNext)), Return(CallGetElement(index, MINUS(posArg(), id(vStart))))));
                stmts.append(Assign(vStart, id(vNext)));
            }
            stmts.append(Return(DefaultValue(elemType)));
            return Block(stmts);
        }

        /**
         */
        private JCStatement makeItemInvalidate(int index) {
            JCVariableDecl vStart = TmpVar("start", syms.intType, cummulativeSize(index));
            
            if (isSequence(index)) {
                return 
                    If(isSequenceActive(),
                        Block(
                            vStart,
                            If(IsTriggerPhase(),
                                // Update the size by the difference
                                // size = size + newLen - (end - begin)
                                SetStmt(sizeSymbol,
                                    PLUS(
                                        Get(sizeSymbol),
                                        MINUS(
                                            newLengthArg(),
                                            MINUS(endPosArg(), startPosArg())
                                        )
                                    )
                                )
                            ),
                            CallSeqInvalidate(
                                PLUS(id(vStart), startPosArg()),
                                If (EQ(endPosArg(), Undefined()),
                                    Undefined(),
                                    PLUS(id(vStart), endPosArg())
                                ),
                                newLengthArg()
                            )
                        )
                    );
            } else {
                //TODO: this is eager even for fixed length
                JCVariableDecl vOldLen = TmpVar("oldLen", syms.intType, computeSize(index));
                JCVariableDecl vValue = TmpVar("value", type(index), CallGetter(index));
                JCVariableDecl vNewLen = TmpVar("newLen", syms.intType, computeSize(index, id(vValue)));

                return 
                    If(isSequenceActive(),
                        Block(
                            vStart,
                            vOldLen,
                            If(IsInvalidatePhase(),
                                CallSeqInvalidate(
                                    id(vStart),
                                    PLUS(id(vStart), id(vOldLen)),
                                    Undefined()
                                ),
                            /*Else (Trigger phase)*/
                                Block(
                                    // Update the size by the difference
                                    vValue,
                                    vNewLen,
                                    SetStmt(sizeSymbol, PLUS(Get(sizeSymbol), MINUS(id(vNewLen), id(vOldLen)))),
                                    SetStmt(vsym(index), id(vValue)),
                                    CallSeqInvalidate(
                                        id(vStart),
                                        PLUS(id(vStart), id(vOldLen)),
                                        id(vNewLen)
                                    )
                                )
                            )
                        )
                    );
            }
        }

        /**
         * Invalidate (and computation) for synthetic size var.
         */
        private JCStatement makeInvalidateSize() {
            // Initialize the singleton synthetic item vars (during IS_VALID phase)
            // Bound sequences don't have a value
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            for (int i = 0; i < length; ++i) {
                if (!isSequence(i)) {
                    stmts.append(SetStmt(vsym(i), CallGetter(i)));
                }
            }
            JCStatement varInits = Block(stmts);

            return
                Block(
                    If(IsTriggerPhase(),
                        setSequenceActive(),
                        varInits
                    ),
                    SetStmt(sizeSymbol, cummulativeSize(length)),
                    CallSeqInvalidate(Int(0), Int(0), Get(sizeSymbol))
                );
        }

        /**
         * For each item, and for size, set-up the invalidate method
         */
        void setupInvalidators() {
            for (int index = 0; index < length; ++index) {
                addInvalidator(vsym(index), makeItemInvalidate(index));
            }
            addInvalidator(sizeSymbol, makeInvalidateSize());
        }
    }

    /**
     * Bound range sequence Translator [10..100 step 10]
     *
     *
     */
    private class BoundRangeSequenceTranslator extends BoundSequenceTranslator {
        private final JFXVar varLower;
        private final JFXVar varUpper;
        private final JFXVar varStep;
        private final JFXVar varSize;
        private final Type elemType;
        private final boolean exclusive;

        BoundRangeSequenceTranslator(JFXSequenceRange tree) {
            super(tree.pos());
            this.varLower = (JFXVar)tree.getLower();
            this.varUpper = (JFXVar)tree.getUpper();
            this.varStep = (JFXVar)tree.getStepOrNull();
            this.varSize = tree.boundSizeVar;
            if (varLower.type == syms.javafx_IntegerType) {
                this.elemType = syms.javafx_IntegerType;
            } else {
                this.elemType = syms.javafx_NumberType;
            }
            this.exclusive = tree.isExclusive();
        }

        private JCExpression zero() {
            return m().Literal(elemType.tag, 0);
        }

        private JCExpression one() {
            return m().Literal(elemType.tag, 1);
        }

        private JCExpression lower() {
            return Get(varLower.getSymbol());
        }
        private JCExpression upper() {
            return Get(varUpper.getSymbol());
        }
        private JCExpression step() {
            return varStep == null?
                  one()
                : Get(varStep.getSymbol());
        }
        private JCExpression size() {
            return Get(varSize.getSymbol());
        }

        private JCStatement setLower(JCExpression value) {
            return SetStmt(varLower.getSymbol(), value);
        }
        private JCStatement setUpper(JCExpression value) {
            return SetStmt(varUpper.getSymbol(), value);
        }
        private JCStatement setStep(JCExpression value) {
            return SetStmt(varStep.getSymbol(), value);
        }
        private JCStatement setSize(JCExpression value) {
            return SetStmt(varSize.getSymbol(), value);
        }

        private JCExpression CallGetter(JFXVar var) {
            return Call(attributeGetterName(var.getSymbol()));
        }
        private JCExpression CallLower() {
            return CallGetter(varLower);
        }
        private JCExpression CallUpper() {
            return CallGetter(varUpper);
        }
        private JCExpression CallStep() {
            return varStep == null?
                  one()
                : CallGetter(varStep);
        }
        private JCExpression CallSize() {
            return CallGetter(varSize);
        }

        private JCExpression DIVstep(JCExpression v1) {
            return varStep == null?
                  v1
                : DIV(v1, step());
        }
        private JCExpression MULstep(JCExpression v1) {
            return varStep == null?
                  v1
                : MUL(v1, step());
        }
        private JCExpression exclusive() {
            return Boolean(exclusive);
        }

        private JCExpression calculateSize(JCExpression vl, JCExpression vu, JCExpression vs) {
            RuntimeMethod rm =
                    (elemType == syms.javafx_NumberType)?
                          defs.Sequences_calculateFloatRangeSize
                        : defs.Sequences_calculateIntRangeSize;
            return Call(rm, vl, vu, vs, exclusive());
        }

        /**
         * int size$range() {
         *     return getSize();
         * }
         */
        JCStatement makeSizeBody() {
            return Return(CallSize());
        }

        /**
         * float get$range(int pos) {
         *    return (pos >= 0 && pos < getSize())?
         *              pos * step + lower
         *            : 0.0f;
         * }
         */
        JCStatement makeGetElementBody() {
            JCExpression cond = AND(
                    GE(posArg(), Int(0)),
                    LT(posArg(), CallSize())
                    );
            JCExpression value = PLUS(MULstep(posArg()), lower());
            JCExpression res = If (cond, value, zero());
            return Return(res);
        }

        /**
         * float newLower = getLower();
         * if (step != 0 && lower != newLower) {
         *    int newSize = Sequences.calculateFloatRangeSize(newLower, upper, step, false);
         *    int loss = 0;
         *    int gain = 0;
         *    float delta = newLower - lower;
         *    if (size == 0 || ((delta % step) != 0)) {
         *      // invalidate everything - new or start point different
         * 	loss = size;
         * 	gain = newSize;
         *    } else if (newLower > lower) {
         *      // shrink -- chop off the front
         * 	loss = (int) delta / step;
         * 	if (loss > size)
         * 	    loss = size
         *    } else {
         *      // grow -- add to the beginning
         * 	gain = (int) -delta / step;
         *    }
         *    if (phase == TRIGGER_PHASE) {
         * 	lower = newLower;
         * 	size = newSize;
         *    }
         *    invalidate$range(0, loss, gain, phase);
         * }
         */
        private JCStatement makeInvalidateLower() {
            JCVariableDecl vNewLower = TmpVar("newLower", elemType, CallLower());
            JCVariableDecl vNewSize = TmpVar("newSize", syms.intType,
                                        calculateSize(id(vNewLower), upper(), step()));
            JCVariableDecl vLoss = MutableTmpVar("loss", syms.intType, Int(0));
            JCVariableDecl vGain = MutableTmpVar("gain", syms.intType, Int(0));
            JCVariableDecl vDelta = TmpVar("delta", elemType, MINUS(id(vNewLower), lower()));

            return
                If (isSequenceActive(),
                  If (IsInvalidatePhase(),
                      CallSeqInvalidate(),
                  /*Else (Trigger phase)*/
                      Block(
                        vNewLower,
                        If (AND(NE(step(), zero()), NE(lower(), id(vNewLower))),
                          Block(
                              vNewSize,
                              vLoss,
                              vGain,
                              vDelta,
                              If (OR(EQ(size(), Int(0)), NE(MOD(id(vDelta), step()), zero())),
                                  Block(
                                      Assign(vLoss, size()),
                                      Assign(vGain, id(vNewSize))),
                              If (GT(id(vNewLower), lower()),
                                  Block(
                                      Assign(vLoss, m().TypeCast(syms.intType, DIVstep(id(vDelta)))),
                                      If (GT(id(vLoss), size()),
                                         Assign(vLoss, size()))
                                      ),
                                  Assign(vGain, m().TypeCast(syms.intType, DIVstep(NEG(id(vDelta)))))
                              )),
                              setLower(id(vNewLower)),
                              setSize(id(vNewSize)),
                              CallSeqInvalidate(Int(0), id(vLoss), id(vGain))
                          )
                        )
                      )
                    )
                );
        }

        /**
         * float newUpper = getUpper();
         * if (step != 0 && upper != newUpper) {
         *    int newSize = Sequences.calculateFloatRangeSize(lower, newUpper, step, false);
         *    int oldSize = size();
         *    if (phase == TRIGGER_PHASE) {
         *       upper = newUpper;
         *       size = newSize;
         *    }
         *    if (newSize >= oldSize)
         *       // grow
         *       invalidate$range(oldSize, oldSize, newSize-oldSize, phase);
         *    else
         *       // shrink
         *       invalidate$range(newSize, oldSize, 0, phase);
         * }
         */
        private JCStatement makeInvalidateUpper() {
            JCVariableDecl vNewUpper = TmpVar("newUpper", elemType, CallUpper());
            JCVariableDecl vOldSize = TmpVar("oldSize", syms.intType, size());
            JCVariableDecl vNewSize = TmpVar("newSize", syms.intType,
                                        calculateSize(lower(), id(vNewUpper), step()));

            return
                If (isSequenceActive(),
                  If (IsInvalidatePhase(),
                      CallSeqInvalidate(),
                  /*Else (Trigger phase)*/
                      Block(
                        vNewUpper,
                        If (AND(NE(step(), zero()), NE(upper(), id(vNewUpper))),
                            Block(
                                vNewSize,
                                vOldSize,
                                setUpper(id(vNewUpper)),
                                setSize(id(vNewSize)),
                                If (GE(id(vNewSize), id(vOldSize)),
                                    CallSeqInvalidate(id(vOldSize), id(vOldSize), MINUS(id(vNewSize), id(vOldSize))),
                                    CallSeqInvalidate(id(vNewSize), id(vOldSize), Int(0))
                                )
                            )
                        )
                      )
                  )
                );
        }

        /**
         * float newStep = getStep();
         * if (step != newStep) {
         *    int newSize = Sequences.calculateFloatRangeSize(lower, upper, newStep, false);
         *    int oldSize = size();
         *    if (phase == TRIGGER_PHASE) {
         *       step = newStep;
         *       size = newSize;
         *    }
         *    // Invalidate everything
         *    invalidate$range(0, oldSize, newSize, phase);
         * }
         */
        private JCStatement makeInvalidateStep() {
            JCVariableDecl vNewStep = TmpVar("newStep", elemType, CallStep());
            JCVariableDecl vOldSize = TmpVar("oldSize", syms.intType, size());
            JCVariableDecl vNewSize = TmpVar("newSize", syms.intType,
                                        calculateSize(lower(), upper(), id(vNewStep)));

            return
                If (isSequenceActive(),
                  If (IsInvalidatePhase(),
                      CallSeqInvalidate(),
                  /*Else (Trigger phase)*/
                      Block(
                        vNewStep,
                        If (NE(step(), id(vNewStep)),
                          Block(
                              vNewSize,
                              vOldSize,
                              setStep(id(vNewStep)),
                              setSize(id(vNewSize)),
                              CallSeqInvalidate(Int(0), id(vOldSize), id(vNewSize))
                          )
                        )
                      )
                    )
                );
        }

        /**
         * float newLower = getLower();
         * float newUpper = getUpper();
         * float newStep = getStep();
         * int newSize = Sequences.calculateFloatRangeSize(newLower, newUpper, newStep, false);
         * if (phase == TRIGGER_PHASE) {
         *      lower = newLower;
         *      upper = newUpper ;
         *      step = newStep ;
         *      size = newSize;
         *      setSequenceValid();
         * }
         * // Invalidate: empty -> filled out range
         * invalidate$range(0, 0, newSize, phase);
         */
        private JCStatement makeInvalidateSize() {
            JCVariableDecl vNewLower = TmpVar("newLower", elemType, CallLower());
            JCVariableDecl vNewUpper = TmpVar("newUpper", elemType, CallUpper());
            JCVariableDecl vNewStep = TmpVar("newStep", elemType, CallStep());
            JCVariableDecl vNewSize = TmpVar("newSize", syms.intType,
                                        calculateSize(id(vNewLower), id(vNewUpper), id(vNewStep)));
            ListBuffer<JCStatement> inits = ListBuffer.lb();
            inits.append(vNewLower);
            inits.append(vNewUpper);
            inits.append(vNewStep);
            inits.append(vNewSize);
            inits.append(setLower(id(vNewLower)));
            inits.append(setUpper(id(vNewUpper)));
            if (varStep != null) {
                inits.append(setStep(id(vNewStep)));
            }
            inits.append(setSize(id(vNewSize)));
            inits.append(setSequenceActive());
            inits.append(CallSeqInvalidate(Int(0), Int(0), id(vNewSize)));

            return
                If (IsInvalidatePhase(),
                    CallSeqInvalidate(),
                /*Else (Trigger phase)*/
                    Block(inits)
                );
        }

        /**
         * Set invalidators for the synthetic support variables
         */
        void setupInvalidators() {
            addInvalidator(varLower.sym, makeInvalidateLower());
            addInvalidator(varUpper.sym, makeInvalidateUpper());
            if (varStep != null) {
                addInvalidator(varStep.sym, makeInvalidateStep());
            }
            addInvalidator(varSize.sym, makeInvalidateSize());
        }
    }


    /**
     * Bound if-expression over sequences.
     *
     * Assumptions:
     *   No eager compution and no invalate calls until sequence is active.
     *   Sequence is made active by a call to size.
     *   Once the sequence is active, 
     *     the cond field is kept up-to-date (by condition invalidator);
     *     the size field is kept up-to-date (by the condition and arm invalidators
     */
    private class BoundIfSequenceTranslator extends BoundSequenceTranslator {

        private final VarSymbol condSym;
        private final VarSymbol thenSym;
        private final VarSymbol elseSym;
        private final VarSymbol sizeSym;

        BoundIfSequenceTranslator(JFXIfExpression tree) {
            super(tree.pos());
            this.condSym = tree.boundCondVar.sym;
            this.thenSym = tree.boundThenVar.sym;
            this.elseSym = tree.boundElseVar.sym;
            this.sizeSym = tree.boundSizeVar.sym;
        }

        JCExpression CallGetCond() {
            return CallGetter(condSym);
        }

        /**
         * Body of the sequence size method.
         *
         * If the sequence is dormant
         *   Set it active.
         *   Eagerly calculate the if-condition.
         *   Use that to determine (and set) the size from appropriate branch arm.
         *   Send initial update nodification.
         * Else (already active)
         *   If the size has been marked invalid,
         *     determine (and set) the size
         * In either case, return the (possibly updated) size field
         */
        JCStatement makeSizeBody() {
            return
                Block(
                    If (isSequenceDormantSetActive(),
                        Block(
                            SetStmt(condSym, CallGetCond()),
                            SetStmt(sizeSym,
                                If (Get(condSym),
                                    CallSize(thenSym),
                                    CallSize(elseSym)
                                )
                            ),
                            CallSeqInvalidateUndefined(flagSymbol),
                            CallSeqInvalidate(flagSymbol, Int(0), Int(0), Get(sizeSym), id(defs.varFlagNEEDS_TRIGGER))
                        ),
                    /*Else (already active)*/
                        Block(
                            If (LT(Get(sizeSym), Int(0)),
                                SetStmt(sizeSym,
                                    If (Get(condSym),
                                        CallSize(thenSym),
                                        CallSize(elseSym)
                                    )
                                )
                            )
                        )
                    ),
                    Return(Get(sizeSym))
                );
        }

        /**
         * Body of the sequence get element method.
         *
         * Make sure the sequence is initialized, by calling the size method.
         * Redirect to the arm sequence to get the element.
         */
        JCStatement makeGetElementBody() {
            return
                Block(
                    Stmt(CallSize(targetSymbol)),  // Assure initialized
                    If (Get(condSym),
                        Return(CallGetElement(thenSym, posArg())),
                        Return(CallGetElement(elseSym, posArg()))
                    )
                );
        }

        /**
         * Body of a invalidate$ method for the synthetic condition boolean.
         *
         * Do nothing if the sequence is dormant.
         * If this is invalidation phase, send a blanket invalidation of the sequence.
         * If this is trigger phase and the condition has changed,
         *   update the condition field
         *   mark the size field as invalid
         *   call invalidate as a whole sequence replacement
         */
        private JCStatement makeInvalidateCond() {
            JCVariableDecl oldCondVar = TmpVar("oldCond", syms.booleanType, Get(condSym));
            JCVariableDecl newCondVar = TmpVar("newCond", syms.booleanType, CallGetCond());
            JCVariableDecl oldSizeVar = TmpVar("oldSize", syms.intType, Get(sizeSym));

            return
                If(isSequenceActive(),
                    If(IsInvalidatePhase(),
                        CallSeqInvalidateUndefined(targetSymbol),
                        Block(
                            oldCondVar,
                            newCondVar,
                            If (NE(id(newCondVar), id(oldCondVar)),
                                Block(
                                    oldSizeVar,
                                    SetStmt(condSym, id(newCondVar)),
                                    SetStmt(sizeSym, Int(JavafxDefs.UNDEFINED_MARKER_INT)),
                                    CallSeqInvalidate(targetSymbol, Int(0), id(oldSizeVar), CallSize(targetSymbol))
                                )
                            )
                        )
                    )
                );
        }

        /**
         * Body of a invalidate$ method for the synthetic bound sequences
         * that is a branch arm.
         *
         * Do nothing if the sequence is dormant.
         * If this is invalidation phase, send a blanket invalidation of the sequence.
         * If this is trigger phase and we are the active arm, update the sequence size,
         * and just pass the invalidation through.
         */
        private JCStatement makeInvalidateArm(VarSymbol armSym, boolean take) {
            return
                If(isSequenceActive(),
                    If(IsInvalidatePhase(),
                        CallSeqInvalidateUndefined(targetSymbol),
                        Block(
                            If (EQ(Get(condSym), Boolean(take)),
                                Block(
                                    SetStmt(sizeSym, CallSize(armSym)), // update the size
                                    CallSeqInvalidate(targetSymbol, startPosArg(), endPosArg(), newLengthArg())
                                )
                            )
                        )
                    )
                );
        }

        /**
         * Set-up the condition and branch arm invalidators.
         */
        void setupInvalidators() {
            addInvalidator(condSym, makeInvalidateCond());
            addInvalidator(thenSym, makeInvalidateArm(thenSym, true));
            addInvalidator(elseSym, makeInvalidateArm(elseSym, false));
        }
    }

    /***********************************************************************
     *
     * Visitors  (alphabetical order)
     *
     * Override those that need special bind handling
     */

    JCExpression TODO(JFXTree tree) {
        return TODO("BIND functionality: " + tree.getClass().getSimpleName());
    }

    private boolean isTargettedToSequence() {
        return types.isSequence(targetSymbol.type);
    }

    private void checkForSequenceVersionUnimplemented(JFXExpression tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            // We want to translate to a bound sequence
            TODO("bound sequence", tree);
        }
    }

    public void visitBlockExpression(JFXBlock tree) {
        checkForSequenceVersionUnimplemented(tree);
        result = new BoundBlockExpressionTranslator(tree).doit();
    }

    @Override
    public void visitForExpression(JFXForExpression tree) {
        TODO(tree);
    }

    @Override
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        checkForSequenceVersionUnimplemented(tree);
        result = (ExpressionResult) (new BoundFunctionCallTranslator(tree)).doit();
    }
    
    @Override
    public void visitIdent(JFXIdent tree) {
        final ExpressionResult exprResult = new BoundIdentTranslator(tree).doit();
        if (tree == boundExpression && isTargettedToSequence()) {
            // We are translating to a bound sequence
            result = new BoundIdentSequenceTranslator(tree, exprResult).doit();
        } else {
            result = exprResult;
        }
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            // We are translating to a bound sequence
            result = new BoundIfSequenceTranslator(tree).doit();
        } else {
            result = new BoundIfExpressionTranslator(tree).doit();
        }

    }

    @Override
    public void visitIndexof(JFXIndexof tree) {
        TODO(tree);
    }

    @Override
    public void visitParens(JFXParens tree) {
        result = translateBoundExpression(tree.expr, targetSymbol, isBidiBind);
    }

    @Override
    public void visitSelect(JFXSelect tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            // We want to translate to a bound sequence
            result = new BoundSelectSequenceTranslator(tree).doit();
        } else {
            result = new BoundSelectTranslator(tree).doit();
        }
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            // We want to translate to a bound sequence
            result = new BoundEmptySequenceTranslator(tree).doit();
        } else {
            super.visitSequenceEmpty(tree);
        }
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        result = new BoundExplicitSequenceTranslator(tree).doit();
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        result = new BoundRangeSequenceTranslator(tree).doit();
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        TODO(tree);
    }

    @Override
    public void visitTypeCast(final JFXTypeCast tree) {
        if (tree == boundExpression && isTargettedToSequence()) {
            // We want to translate to a bound sequence
            result = new BoundTypeCastSequenceTranslator(tree).doit();
        } else {
            super.visitTypeCast(tree);
        }
    }

    @Override
    public void visitUnary(JFXUnary tree) {
        checkForSequenceVersionUnimplemented(tree);
        super.visitUnary(tree);
    }


    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "bfx$";
    }

}
