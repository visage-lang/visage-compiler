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
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.TypeTags;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.comp.JavafxDefs.RuntimeMethod;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.LiteralInitClassMap;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.LiteralInitVarMap;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.jvm.Target;
import com.sun.tools.mjavac.tree.JCTree.JCFieldAccess;
import com.sun.tools.mjavac.tree.TreeInfo;
import com.sun.tools.mjavac.tree.TreeTranslator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.type.TypeKind;
import static com.sun.tools.javafx.comp.JavafxAbstractTranslation.Yield.*;

/**
 * Common translation mechanism
 *
 * @author Robert Field
 */

public abstract class JavafxAbstractTranslation
                             extends JavafxTranslationSupport
                             implements JavafxVisitor {

    /*
     * the result of translating a tree by a visit method
     */
    Result result;

    final JavafxOptimizationStatistics optStat;
    final Target target;

    Type targetType;
    Yield yieldKind;

    private JavafxToJava toJava; //TODO: this should go away

    protected JavafxAbstractTranslation(Context context, JavafxToJava toJava) {
        super(context);
        this.optStat = JavafxOptimizationStatistics.instance(context);
        this.toJava = toJava; 
        this.target = Target.instance(context);
    }

    /********** translation state tracking types and methods **********/

    protected enum ReceiverContext {
        // In a script function or script var init, implemented as a static method
        ScriptAsStatic,
        // In an instance function or instance var init, implemented as static
        InstanceAsStatic,
        // In an instance function or instance var init, implemented as an instance method
        InstanceAsInstance,
        // Should not see code in this state
        Oops
    }

    enum Yield {
        ToExpression,
        ToStatement
    }

    Yield yield() {
        return yieldKind;
    }

    JFXClassDeclaration currentClass() {
        return getAttrEnv().enclClass;
    }

    void setCurrentClass(JFXClassDeclaration tree) {
        getAttrEnv().enclClass = tree;
    }

    protected JavafxEnv<JavafxAttrContext> getAttrEnv() {
        return toJava.getAttrEnv();
    }

    protected ReceiverContext receiverContext() {
        return toJava.receiverContext();
    }

    protected void setReceiverContext(ReceiverContext rc) {
        toJava.setReceiverContext(rc);
    }

    protected JavafxToJava toJava() {
        return toJava;
    }

    protected JavafxTranslateDependent translateDependent() {
        return toJava.translateDependent();
    }

    /********** Utility routines **********/

    /**
     * @return the substitutionMap
     */
    Map<Symbol, Name> getSubstitutionMap() {
        return toJava.getSubstitutionMap();
    }

    /**
     * Class symbols for classes that need a reference to the outer class.
     */
    Set<ClassSymbol> getHasOuters() {
        return toJava.getHasOuters();
    }

    /**
     * @return the literalInitClassMap
     */
    LiteralInitClassMap getLiteralInitClassMap() {
        return toJava.getLiteralInitClassMap();
    }

    /** Box up a single primitive expression. */
    JCExpression makeBox(DiagnosticPosition diagPos, JCExpression translatedExpr, Type primitiveType) {
        make.at(translatedExpr.pos());
        Type boxedType = types.boxedTypeOrType(primitiveType);
        JCExpression box;
        if (target.boxWithConstructors()) {
            Symbol ctor = lookupConstructor(translatedExpr.pos(),
                    boxedType,
                    List.<Type>nil().prepend(primitiveType));
            box = make.Create(ctor, List.of(translatedExpr));
        } else {
            Symbol valueOfSym = lookupMethod(translatedExpr.pos(),
                    names.valueOf,
                    boxedType,
                    List.<Type>nil().prepend(primitiveType));
//            JCExpression meth =makeIdentifier(valueOfSym.owner.type.toString() + "." + valueOfSym.name.toString());
            JCExpression meth = make.Select(makeType(diagPos, valueOfSym.owner.type), valueOfSym.name);
            TreeInfo.setSymbol(meth, valueOfSym);
            meth.type = valueOfSym.type;
            box = make.App(meth, List.of(translatedExpr));
        }
        return box;
    }
    /** Look up a method in a given scope.
     */
    private MethodSymbol lookupMethod(DiagnosticPosition pos, Name name, Type qual, List<Type> args) {
        return rs.resolveInternalMethod(pos, getAttrEnv(), qual, name, args, null);
    }
    //where
    /** Look up a constructor.
     */
    private MethodSymbol lookupConstructor(DiagnosticPosition pos, Type qual, List<Type> args) {
        return rs.resolveInternalConstructor(pos, getAttrEnv(), qual, args, null);
    }

    ExpressionResult convertTranslated(ExpressionResult res, DiagnosticPosition diagPos, Type targettedType) {
        return (targettedType == null || targettedType == syms.voidType) ?
              res
            : new ExpressionResult(
                diagPos,
                res.statements(),
                new TypeConversionTranslator(diagPos, res.expr(), res.resultType, targettedType).doitExpr(),
                res.invalidators(),
                res.interClass,
                targettedType);
    }

    JCExpression convertTranslated(JCExpression translated, DiagnosticPosition diagPos,
            Type sourceType, Type targettedType) {
        return (targettedType == null || targettedType == syms.voidType) ?
              translated
            : new TypeConversionTranslator(diagPos, translated, sourceType, targettedType).doitExpr();
    }

    /**
     * Special handling for Strings and Durations. If a value assigned to one of these is null,
     * the default value for the type must be substituted.
     * inExpr is the input expression.  outType is the desired result type.
     * expr is the result value to use in the normal case.
     * This doesn't handle the case   var ss: String = if (true) null else "Hi there, sailor"
     * But it does handle nulls coming in from Java method returns, and variables.
     */
    protected JCExpression convertNullability(final DiagnosticPosition diagPos, final JCExpression expr,
            final JFXExpression inExpr, final Type outType) {
        return (new Translator(diagPos) {

            Result doit() {
                throw new IllegalArgumentException();
            }

            JCExpression doitExpr() {
                if (outType != syms.stringType && outType != syms.javafx_DurationType) {
                    return expr;
                }

                final Type inType = inExpr.type;
                if (inType == syms.botType || inExpr.getJavaFXKind() == JavaFXKind.NULL_LITERAL) {
                    return DefaultValue(outType);
                }

                if (!types.isSameType(inType, outType) || isValueFromJava(inExpr)) {
                    JCVariableDecl daVar = TmpVar(outType, expr);
                    JCExpression toTest = id(daVar.name);
                    JCExpression cond = NEnull(toTest);
                    JCExpression ret = 
                        If (cond,
                            id(daVar.name),
                            DefaultValue(outType));
                    return BlockExpression(daVar, ret);
                }
                return expr;
            }
        }).doitExpr();
    }

    /********** Result types **********/

    public static abstract class Result {
        final DiagnosticPosition diagPos;
        Result(DiagnosticPosition diagPos) {
            this.diagPos = diagPos;
        }
        abstract List<JCTree> trees();
    }

    public static abstract class AbstractStatementsResult extends Result {
        private final List<JCStatement> stmts;
        AbstractStatementsResult(DiagnosticPosition diagPos, List<JCStatement> stmts) {
            super(diagPos);
            this.stmts = stmts;
        }
        public List<JCStatement> statements() {
            return stmts;
        }
        List<JCTree> trees() {
            ListBuffer<JCTree> ts = ListBuffer.lb();
            for (JCTree t : stmts) {
                ts.append(t);
            }
            return ts.toList();
        }
    }

    public static class StatementsResult extends AbstractStatementsResult {
        StatementsResult(DiagnosticPosition diagPos, List<JCStatement> stmts) {
            super(diagPos, stmts);
        }
        StatementsResult(DiagnosticPosition diagPos, ListBuffer<JCStatement> buf) {
            super(diagPos, buf.toList());
        }
        StatementsResult(JCStatement stmt) {
            super(stmt.pos(), List.of(stmt));
        }
    }

    public static class DependentPair {

        public final VarSymbol instanceSym;
        public final Symbol referencedSym;

        DependentPair(VarSymbol instanceSym, Symbol referencedSym) {
            this.instanceSym = instanceSym;
            this.referencedSym = referencedSym;
        }
    }

    /**
     * A variable (represented as a Symbol) on which the current variable depends
     * and the, optional, invalidation code to be placed in the variable's
     * invalidation method to corrected invalidate the current variable.
     */
    public static class BindeeInvalidator {

        // Variable symbols on which the current variable depends
        public final VarSymbol bindee;

        // Invalidation code to be placed in bindee.
        // Optional.  If null, use default invalidation of the current variable.
        public final JCStatement invalidator;

        BindeeInvalidator(VarSymbol bindee, JCStatement invalidator) {
            this.bindee = bindee;
            this.invalidator = invalidator;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BindeeInvalidator)) {
                return false;
            }
            BindeeInvalidator other = (BindeeInvalidator)obj;
            return other.bindee == bindee && other.invalidator == invalidator;
        }

        @Override
        public int hashCode() {
            return bindee.hashCode() + (invalidator == null ? 0 : invalidator.hashCode());
        }

        @Override
        public String toString() {
            return "BindeeInvalidator " + bindee.name + " / " + invalidator;
        }
    }

    public interface BoundResult {

        // Has getter expression of bound variable
        boolean hasExpr();

        // Java code for getter expression of bound variable
        JCExpression expr();

        // Java preface code for getter of bound variable
        List<JCStatement> statements();

        // Variable symbols on which this variable depends
        List<VarSymbol> bindees();

        // Invalidators for this variable
        List<BindeeInvalidator> invalidators();

        List<DependentPair> interClass();

        // Java code for getting the element of a bound sequence
        JCStatement getElementMethodBody();

        // Java code for getting the size of a bound sequence
        JCStatement getSizeMethodBody();
    }

    public static class ExpressionResult extends AbstractStatementsResult implements BoundResult {
        private final JCExpression value;
        private final List<BindeeInvalidator> invalidators;
        private final List<DependentPair> interClass;
        private final Type resultType;

        ExpressionResult(DiagnosticPosition diagPos, List<JCStatement> stmts, JCExpression value, List<BindeeInvalidator> invalidators, List<DependentPair> interClass, Type resultType) {
            super(diagPos, stmts);
            this.value = value;
            this.invalidators = invalidators;
            this.interClass = interClass;
            this.resultType = resultType;
        }

        public JCExpression expr() {
            return value;
        }
        public boolean hasExpr() {
            return value != null;
        }
        // Invalidators for this variable
        public List<BindeeInvalidator> invalidators() {
            return invalidators;
        }

        public List<VarSymbol> bindees() {
            ListBuffer<VarSymbol> bindees = ListBuffer.lb();
            for (BindeeInvalidator bi : invalidators) {
                bindees.append(bi.bindee);
            }
            return bindees.toList();
        }
        public List<DependentPair> interClass() {
            return interClass;
        }
        public JCStatement getElementMethodBody() {
            TODO("sequence element getter for: " + value);
            return null;
        }
        public JCStatement getSizeMethodBody() {
            TODO("sequence size getter for: " + value);
            return null;
        }

        @Override
        List<JCTree> trees() {
            List<JCTree> ts = super.trees();
            return value==null? ts : ts.append(value);
        }
    }

    /**
     * Bound sequence get element / size method body pair as Result
     */
    public static class BoundSequenceResult extends ExpressionResult implements BoundResult {
        private final JCStatement getElement;
        private final JCStatement getSize;
        BoundSequenceResult(List<BindeeInvalidator> invalidators, List<DependentPair> interClass, JCStatement getElement, JCStatement getSize) {
            super(getElement.pos(), null, null, invalidators, interClass, null);
            this.getElement = getElement;
            this.getSize = getSize;
        }
        // Java code for getting the element of a bound sequence
        @Override
        public JCStatement getElementMethodBody() {
            return getElement;
        }
        // Java code for getting the size of a bound sequence
        @Override
        public JCStatement getSizeMethodBody() {
            return getSize;
        }
        @Override
        public JCExpression expr() {
            throw new AssertionError("Shouldn't be asking for this");
        }
        @Override
        public List<JCStatement> statements() {
            throw new AssertionError("Shouldn't be asking for this");
        }
        @Override
        public String toString() {
            return "SequenceElementSizeResult- get element: " + getElement.getClass() + " = " + getElement + ", size: " + getSize.getClass() + " = " + getSize;
        }
        @Override
        List<JCTree> trees() {
            return List.<JCTree>of(getElement, getSize);
        }
    }

    public static class SpecialResult extends Result {
        private final JCTree tree;
        SpecialResult(JCTree tree) {
            super(tree.pos());
            this.tree = tree;
        }
        JCTree tree() {
            return tree;
        }
        @Override
        public String toString() {
            return "SpecialResult-" + tree.getClass() + " = " + tree;
        }
        List<JCTree> trees() {
            return tree==null? List.<JCTree>nil() : List.of(tree);
        }
    }

    /********** translation support **********/

    private void translateCore(JFXTree expr, Type targettedType, Yield yield) {
            JFXTree prevWhere = getAttrEnv().where;
            Yield prevYield = yield();
            Type prevTargetType = targetType;
            getAttrEnv().where = expr;
            yieldKind = yield;
            targetType = targettedType;
            expr.accept(this);
            yieldKind = prevYield;
            targetType = prevTargetType;
            getAttrEnv().where = prevWhere;
    }

    ExpressionResult translateToExpressionResult(JFXExpression expr, Type targettedType) {
        if (expr == null) {
            return null;
        } else {
            translateCore(expr, targettedType, ToExpression);
            ExpressionResult ret = (ExpressionResult)this.result;
            this.result = null;
            return ret.hasExpr()?
                  convertTranslated(ret, expr.pos(), targettedType)
                : ret;
        }
    }

    StatementsResult translateToStatementsResult(JFXExpression expr, Type targettedType) {
        if (expr == null) {
            return null;
        } else {
            translateCore(expr, targettedType, ToStatement);
            Result ret = this.result;
            this.result = null;
            if (ret instanceof StatementsResult) {
                return (StatementsResult) ret; // already converted
            } else if (ret instanceof ExpressionResult) {
                return new StatementsResult(expr.pos(), asStatements((ExpressionResult) ret, targettedType));
            } else {
                throw new RuntimeException(ret.toString());
            }
        }
    }

    class JCConverter extends JavaTreeBuilder {

        private final AbstractStatementsResult res;
        private final Type type;

        JCConverter(AbstractStatementsResult res, Type type) {
            super(res.diagPos, currentClass(), receiverContext() == ReceiverContext.ScriptAsStatic);
            this.res = res;
            this.type = type;
        }

        List<JCStatement> asStatements() {
            int typeTag = type.tag; // Blow up if we are passed null as the type of statements
            List<JCStatement> stmts = res.statements();
            if (res instanceof ExpressionResult) {
                ExpressionResult eres = (ExpressionResult) res;
                JCExpression expr = eres.expr();
                if (expr != null) {
                    stmts = stmts.append(Stmt(convertedExpression(eres), type));
                }
            }
            return stmts;
        }

        JCStatement asStatement() {
            List<JCStatement> stmts = asStatements();
            if (stmts.length() == 1) {
                return stmts.head;
            } else {
                return asBlock();
            }
        }

        JCBlock asBlock() {
            return Block(asStatements());
        }

        JCExpression asExpression() {
            if (res instanceof ExpressionResult) {
                ExpressionResult er = (ExpressionResult) res;
                if (er.statements().nonEmpty()) {
                    BlockExprJCBlockExpression bexpr = new BlockExprJCBlockExpression(0L, er.statements(), convertedExpression(er));
                    bexpr.pos = er.expr().pos;
                    return bexpr;
                } else {
                    return convertedExpression(er);
                }
            } else {
                throw new IllegalArgumentException("must be ExpressionResult -- was: " + res);
            }
        }

        private JCExpression convertedExpression(ExpressionResult eres) {
            return convertTranslated(eres.expr(), diagPos, eres.resultType, type);
        }
    }

    JCBlock asBlock(AbstractStatementsResult res, Type targettedType) {
        return new JCConverter(res, targettedType).asBlock();
    }

    JCStatement asStatement(AbstractStatementsResult res, Type targettedType) {
        return new JCConverter(res, targettedType).asStatement();
    }

    List<JCStatement> asStatements(AbstractStatementsResult res, Type targettedType) {
        return new JCConverter(res, targettedType).asStatements();
    }

    JCExpression asExpression(AbstractStatementsResult res, Type targettedType) {
        return new JCConverter(res, targettedType).asExpression();
    }

    JCExpression translateToExpression(JFXExpression expr, Type targettedType) {
        return asExpression(translateToExpressionResult(expr, targettedType), targettedType);
    }

    JCStatement translateToStatement(JFXExpression expr, Type targettedType) {
        return asStatement(translateToStatementsResult(expr, targettedType), targettedType);
    }

    JCBlock translateToBlock(JFXExpression expr, Type targettedType) {
        if (expr == null) {
            return null;
        } else {
            return asBlock(translateToStatementsResult(expr, targettedType), targettedType);
        }
    }

    JCTree translateFunction(JFXFunctionDefinition tree, boolean maintainContext) {
        return new FunctionTranslator(tree, maintainContext).doit().tree();
    }

    /** Translate a single tree.
     */
    SpecialResult translateToSpecialResult(JFXTree tree) {
        SpecialResult ret;

        JFXTree prevWhere = getAttrEnv().where;
        getAttrEnv().where = tree;
        tree.accept(this);
        getAttrEnv().where = prevWhere;
        ret = (SpecialResult) this.result;
        this.result = null;
        return ret;
    }

    static class OnReplaceInfo {
        public OnReplaceInfo outer;
        VarMorphInfo vmi;
        public JFXOnReplace onReplace;
        Symbol newElementsSym;
        Type arraySequenceType;
        Type seqWithExtendsType;
    }

    OnReplaceInfo onReplaceInfo;

    OnReplaceInfo findOnReplaceInfo(Symbol sym) {
        OnReplaceInfo info = onReplaceInfo;
        while (info != null && sym != info.newElementsSym)
            info = info.outer;
        return info;
    }

    /**** utility methods ******/

    private UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType, final int initLength) {
        return new UseSequenceBuilder(diagPos, elemType, null) {

            JCStatement addElement(JFXExpression exprToAdd) {
                JCExpression expr = translateToExpression(exprToAdd, targettedType(exprToAdd));
                return makeAdd(expr);
            }

            List<JCExpression> makeConstructorArgs() {
                ListBuffer<JCExpression> lb = ListBuffer.lb();
                if (initLength != -1) {
                    lb.append(make.at(diagPos).Literal(Integer.valueOf(initLength)));
                }
                if (addTypeInfoArg)
                    lb.append(TypeInfo(diagPos, elemType));
                return lb.toList();
            }

            @Override
            JCExpression makeToSequence() {
                return makeBuilderVarAccess();
            }
        };
    }

    UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType) {
        return useSequenceBuilder(diagPos, elemType, -1);
    }

    abstract class UseSequenceBuilder extends JavaTreeBuilder {
        final Type elemType;
        private final String seqBuilder;
        boolean addTypeInfoArg = true;

        // Sequence builder temp var name "sb"
        private final Name sbName = getSyntheticName("sb");

        private UseSequenceBuilder(DiagnosticPosition diagPos, Type elemType, String seqBuilder) {
            super(diagPos, currentClass(), receiverContext() == ReceiverContext.ScriptAsStatic);
            this.elemType = elemType;
            this.seqBuilder = seqBuilder;
        }

        Type targettedType(JFXExpression exprToAdd) {
            Type exprType = exprToAdd.type;
            if (types.isArray(exprType) || types.isSequence(exprType)) {
                return types.sequenceType(elemType);
            } else {
                Type unboxed = types.unboxedType(elemType);
                return (unboxed.tag != TypeTags.NONE) ? unboxed : elemType;
            }
        }

        JCStatement makeBuilderVar() {
            String localSeqBuilder = this.seqBuilder;
            boolean primitive = false;
            if (localSeqBuilder == null) {
                if (elemType.isPrimitive()) {
                    primitive = true;
                    addTypeInfoArg = false;
                    int kind = types.typeKind(elemType);
                    localSeqBuilder = "com.sun.javafx.runtime.sequence." + JavafxDefs.getTypePrefix(kind) + "ArraySequence"; //TODO: put in defs
                }
                else
                    localSeqBuilder = JavafxDefs.cObjectArraySequence;
            }
            JCExpression builderTypeExpr = QualifiedTree(localSeqBuilder);
            JCExpression builderClassExpr = QualifiedTree(localSeqBuilder);
            if (! primitive) {
                builderTypeExpr = m().TypeApply(builderTypeExpr,
                        List.of(makeType(elemType)));
                // class name -- SequenceBuilder<elemType>
                builderClassExpr = m().TypeApply(builderClassExpr,
                        List.<JCExpression>of(makeType(elemType)));
            }

            // Build "sb" initializing expression -- new SequenceBuilder<T>(clazz)
            JCExpression newExpr = m().NewClass(
                null,                               // enclosing
                List.<JCExpression>nil(),           // type args
                builderClassExpr, // class name -- SequenceBuilder<elemType>
                makeConstructorArgs(),              // args
                null                                // empty body
                );

            // Build the sequence builder variable
            return Var(0L, builderTypeExpr, sbName, newExpr);
        }

        JCIdent makeBuilderVarAccess() {
            return id(sbName);
        }

        abstract JCStatement addElement(JFXExpression expr);

        abstract List<JCExpression> makeConstructorArgs();

        JCStatement makeAdd(JCExpression expr) {
            return CallStmt(makeBuilderVarAccess(), names.fromString("add"), expr);
        }

        abstract JCExpression makeToSequence();
    }

    /****************************** Translators ******************************/

    abstract class Translator extends JavaTreeBuilder {

        Translator(DiagnosticPosition diagPos) {
            super(diagPos, currentClass(), receiverContext() == ReceiverContext.ScriptAsStatic);
        }

        abstract Result doit();

        JavafxTreeMaker fxm() {
            return fxmake.at(diagPos);
        }

        JCVariableDecl convertParam(JFXVar param) {
            return Param(param.type, param.name);
        }

        /**
         * Make a receiver expression that will reference the provided symbol.
         * Return null if no receiver needed.
         */
        JCExpression makeReceiver(Symbol sym) {
            return makeReceiver(sym, true);
        }

        /**
         * Make a receiver expression that will reference the provided symbol.
         * Build the AST for accessing the outer member.
         * The accessors might be chained if the member accessed is more than one level up in the outer chain.
         * */
        JCExpression makeReceiver(Symbol sym, boolean nullForThis) {
            final Symbol owner = sym==null? null : sym.owner;
            final Symbol siteOwner = currentClass().sym;
            final JCExpression thisExpr = Select(makeType(siteOwner.type), names._this);
            JCExpression ret = types.isMixin(owner) ?
                id(defs.receiverName) :   // referenced member is a mixin member, access it through receiver var
                thisExpr;

            // check if it is in the chain
            if (owner != null && siteOwner != owner) {
                Symbol siteCursor = siteOwner;
                boolean foundOwner = false;
                int numOfOuters = 0;
                ownerSearch:
                while (siteCursor.kind != Kinds.PCK) {
                    ListBuffer<Type> supertypes = ListBuffer.lb();
                    Set<Type> superSet = new HashSet<Type>();
                    if (siteCursor.type != null) {
                        supertypes.append(siteCursor.type);
                        superSet.add(siteCursor.type);
                    }
                    if (siteCursor.kind == Kinds.TYP) {
                        types.getSupertypes(siteCursor, supertypes, superSet);
                    }
                    for (Type supType : supertypes) {
                        if (types.isSameType(supType, owner.type)) {
                            foundOwner = true;
                            break ownerSearch;
                        }
                    }
                    if (siteCursor.kind == Kinds.TYP) {
                        numOfOuters++;
                    }
                    siteCursor = siteCursor.owner;
                }

                if (foundOwner) {
                    // site was found up the outer class chain, add the chaining accessors
                    siteCursor = siteOwner;
                    while (numOfOuters > 0) {
                        if (siteCursor.kind == Kinds.TYP) {
                            ret = Call(ret, defs.outerAccessor_MethodName);
                        }
                        if (siteCursor.kind == Kinds.TYP) {
                            numOfOuters--;
                        }
                        siteCursor = siteCursor.owner;
                    }
                }
            }
            return (nullForThis && ret == thisExpr) ? null : ret;
        }
    }

    abstract class ExpressionTranslator extends Translator {

        private final ListBuffer<JCStatement> stmts = ListBuffer.lb();
        private final ListBuffer<BindeeInvalidator> invalidators = ListBuffer.lb();
        private final ListBuffer<DependentPair> interClass = ListBuffer.lb();

        ExpressionTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        JCExpression mergeResults(ExpressionResult res) {
            addPreface(res.statements());
            addBindees(res.bindees());
            addInterClassBindees(res.interClass());
            return res.expr();
        }

        JCExpression translateExpr(JFXExpression expr, Type type) {
            return mergeResults(translateToExpressionResult(expr, type));
        }

        List<JCExpression> translateExprs(List<JFXExpression> list) {
            ListBuffer<JCExpression> trans = ListBuffer.lb();
            for (List<JFXExpression> l = list; l.nonEmpty(); l = l.tail) {
                JCExpression res = translateExpr(l.head, null);
                if (res != null) {
                    trans.append(res);
                }
            }
            return trans.toList();
        }

        void translateStmt(JFXExpression expr, Type targettedType) {
            //TODO: My guess is that statements will need to preserve bindee info for block-expressions
            StatementsResult res = translateToStatementsResult(expr, targettedType);
            addPreface(res.statements());
        }

        void addPreface(JCStatement stmt) {
            stmts.append(stmt);
        }

        void addPreface(List<JCStatement> list) {
            stmts.appendList(list);
        }

        void addInvalidator(VarSymbol sym, JCStatement invStmt) {
            BindeeInvalidator bi = new BindeeInvalidator(sym, invStmt);
            if (!invalidators.contains(bi)) {
                invalidators.append(bi);
            }
        }

        void addBindee(VarSymbol sym) {
            addInvalidator(sym, null);
        }

        void addBindees(List<VarSymbol> syms) {
            for (VarSymbol sym : syms) {
                addBindee(sym);
            }
        }

        void addInterClassBindee(VarSymbol instanceSym, Symbol referencedSym) {
            interClass.append(new DependentPair( instanceSym,  referencedSym));
        }

        void addInterClassBindees(List<DependentPair> pairs) {
            for (DependentPair pair : pairs) {
                interClass.append(pair);
            }
        }

        ExpressionResult toResult(JCExpression translated, Type resultType) {
            return new ExpressionResult(diagPos, statements(), translated, invalidators(), interClass(), resultType);
        }

        StatementsResult toStatementResult(JCExpression translated, Type resultType, Type targettedType) {
            return toStatementResult(
                    (targettedType == null || targettedType == syms.voidType) ?
                          Stmt(translated)
                        : Return(
                            convertTranslated(translated, diagPos, resultType, targettedType)));
        }

        StatementsResult toStatementResult(JCStatement translated) {
            assert invalidators.length() == 0;
            return new StatementsResult(diagPos, stmts.append(translated));
        }

         StatementsResult toStatementResult() {
            assert invalidators.length() == 0;
            return new StatementsResult(diagPos, stmts);
        }

        List<JCStatement> statements() {
            return stmts.toList();
        }

        List<BindeeInvalidator> invalidators() {
            return invalidators.toList();
        }

        List<DependentPair> interClass() {
            return interClass.toList();
        }

        abstract AbstractStatementsResult doit();

        JCExpression staticReference(Symbol sym) {
            Symbol owner = sym.owner;
            Symbol encl = currentClass().sym;
            if (encl.name.endsWith(defs.scriptClassSuffixName) && owner == encl.owner) {
                return null;
            } else {
                //TODO: see init builder getStaticContext() for a better implementation
                Type classType = types.erasure(owner.type);
                return makeType(classType, false);
            }
        }
        
        JCExpression translateSizeof(JFXExpression expr, JCExpression transExpr) {
            if (expr instanceof JFXIdent) {
                JFXIdent varId = (JFXIdent) expr;
                OnReplaceInfo info = findOnReplaceInfo(varId.sym);
                if (info != null) {
                    return id(paramNewElementsLengthName(info.onReplace));
                }
            }
            return Call(defs.Sequences_size, transExpr);
        }

   }

    class LiteralTranslator extends ExpressionTranslator {

        protected final JFXLiteral tree;

        LiteralTranslator(JFXLiteral tree) {
            super(tree.pos());
            this.tree = tree;
        }

        protected ExpressionResult doit() {
            if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
                throw new AssertionError("Should have been converted");
            }
            // Just translate to literal value
            return toResult(m().Literal(tree.typetag, tree.value), tree.type);
        }
    }

    class StringExpressionTranslator extends ExpressionTranslator {

        private final JFXStringExpression tree;
        StringExpressionTranslator(JFXStringExpression tree) {
            super(tree.pos());
            this.tree = tree;
        }

        protected ExpressionResult doit() {
            StringBuffer sb = new StringBuffer();
            List<JFXExpression> parts = tree.getParts();
            ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();

            JFXLiteral lit = (JFXLiteral) (parts.head);            // "...{
            sb.append((String) lit.value);
            parts = parts.tail;
            boolean containsDateTimeFormat = false;

            while (parts.nonEmpty()) {
                lit = (JFXLiteral) (parts.head);                  // optional format (or null)
                String format = (String) lit.value;
                if ((!containsDateTimeFormat) && format.length() > 0
                    && JavafxDefs.DATETIME_FORMAT_PATTERN.matcher(format).find()) {
                    containsDateTimeFormat = true;
                }
                parts = parts.tail;
                JFXExpression exp = parts.head;
                JCExpression texp;
                if (exp != null && types.isSameType(exp.type, syms.javafx_DurationType)) {
                    texp = Call(translateExpr(exp, syms.javafx_DurationType), defs.toMillis_DurationMethodName);
                    texp = typeCast(syms.javafx_LongType, syms.javafx_DoubleType, texp);
                    sb.append(format.length() == 0 ? "%dms" : format);
                } else {
                    texp = translateExpr(exp, null);
                    sb.append(format.length() == 0 ? "%s" : format);
                }
                values.append(texp);
                parts = parts.tail;

                lit = (JFXLiteral) (parts.head);                  // }...{  or  }..."
                String part = (String)lit.value;
                sb.append(part.replace("%", "%%"));              // escape percent signs
                parts = parts.tail;
            }
            values.prepend(String(sb.toString()));
            RuntimeMethod formatMethod;
            if (tree.translationKey != null) {
                formatMethod = defs.StringLocalization_getLocalizedString;
                if (tree.translationKey.length() == 0) {
                    values.prepend(Null());
                } else {
                    values.prepend(String(tree.translationKey));
                }
                String resourceName =
                       currentClass().sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
                values.prepend(String(resourceName));
            } else if (containsDateTimeFormat) {
                formatMethod = defs.FXFormatter_sprintf;
            } else {
                formatMethod = defs.String_format;
            }
            return toResult(
                    Call(formatMethod, values),
                    syms.stringType);
        }
    }

    abstract class MemberReferenceTranslator extends ExpressionTranslator {

        protected MemberReferenceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        JCExpression convertVariableReference(JCExpression varRef, Symbol sym) {
            JCExpression expr = varRef;

            if (sym instanceof VarSymbol) {
                final VarSymbol vsym = (VarSymbol) sym;
                VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                boolean isFXMemberVar = vmi.isFXMemberVariable();

                if (isFXMemberVar) {
                    // this is a reference to a JavaFX class variable, use getter
                    JCExpression instance;
                    // find referenced instance, null for current
                    switch (expr.getTag()) {
                        case JCTree.IDENT:
                            // if we are in a mixin class reference variables through the receiver
                            instance = currentClass().isMixinClass()?
                                  id(defs.receiverName)
                                : null;
                            break;
                        case JCTree.SELECT:
                            instance = ((JCFieldAccess) varRef).getExpression();
                            break;
                        default:
                            throw new AssertionError();
                    }
                    expr = Call(instance, attributeGetterName(vsym));
                }
            }

            return expr;
        }

    }

    abstract class NullCheckTranslator extends MemberReferenceTranslator {

        protected final Symbol refSym;             //
        protected final Type fullType;             // Type, before conversion, of expression
        protected final Type resultType;           // Type of final generated expression
        protected final boolean staticReference;   // Is this a static reference

        NullCheckTranslator(DiagnosticPosition diagPos, Symbol sym, Type fullType) {
            super(diagPos);
            this.refSym = sym;
            this.fullType = fullType;
            this.resultType = targetType==null? fullType : targetType; // use targetType, if any
            this.staticReference = refSym != null && refSym.isStatic();
        }

        abstract JFXExpression getToCheck();

        abstract JCExpression fullExpression(JCExpression mungedToCheckTranslated);

        boolean needNullCheck() {
            return getToCheck() != null && !staticReference && !getToCheck().type.isPrimitive() && possiblyNull(getToCheck());
        }

        boolean canChange() {
            return getToCheck() != null && !getToCheck().type.isPrimitive() && possiblyNull(getToCheck());
        }

        protected JCExpression preserveSideEffects(Type type, JFXExpression expr, JCExpression trans) {
            if (needNullCheck() && expr!=null && hasSideEffects(expr)) {
                // if there is going to be a null check (which thus could keep expr
                // from being evaluated), and expr has side-effects, then eval
                // it first and put it in a temp var.
                return addTempVar(type, trans);
            } else {
                // no side-effects, just pass-through
                return trans;
            }
        }

        protected JCExpression addTempVar(Type varType, JCExpression trans) {
            JCVariableDecl tmpVar = TmpVar("pse", varType, trans);
            addPreface(tmpVar);
            return id(tmpVar);
        }

        /**
         * Translate the 'toCheck' of 'toCheck.name'.
         * Override to specialize the translation.
         * Note: 'toCheck'  may or may not be in a LHS but in either
         * event the selector is a value expression
         */
        JCExpression translateToCheck(JFXExpression expr) {
            if (staticReference) {
                return staticReference(refSym);
            } else if (expr == null) {
                if (refSym != null && refSym.owner.kind == Kinds.TYP) {
                    // it is a non-static attribute or function class member
                    // reference it through the receiver
                    return getReceiver(refSym);
                }
                return null;
            }
            Symbol selectorSym = expressionSymbol(expr);
            // If this is OuterClass.memberName or MixinClass.memberName, then
            // we want to create expression to get the proper receiver.
            if (selectorSym != null && selectorSym.kind == Kinds.TYP) {
                return getReceiver(refSym);
            }
            Type exprType = expr.type;

            // translate normally, preserving side-effects if need be
            JCExpression tExpr = preserveSideEffects(exprType, expr, translateExpr(expr, exprType));

            // if expr is primitve, box it
            // expr.type is null for package symbols.
            if (exprType != null && exprType.isPrimitive()) {
                return makeBox(diagPos, tExpr, exprType);
            }

            return tExpr;
        }

        protected AbstractStatementsResult doit() {
            JCExpression tToCheck = translateToCheck(getToCheck());
            JCExpression full = fullExpression(tToCheck);
            full = convertTranslated(full, diagPos, fullType, resultType);
            if (yield() == ToStatement) {
                // a statement is the desired result of the translation
                return toStatementResult(wrapInNullCheckStatement(full, tToCheck, resultType, fullType));
            } else {
                // an expression is the desired result of the translation, convert it to a conditional expression
                // if it would dereference null, then the full expression instead yields the default value
                return toResult(wrapInNullCheckExpression(full, tToCheck, resultType, fullType), resultType);
            }
        }
        
        protected JCExpression copyOfTranslatedToCheck(JCExpression tToCheck) {
            // Make an expression to use in null test.
            // If translated toCheck is an identifier (tmp var or not), just make a new identifier.
            // Otherwise, retranslate.
            return (tToCheck instanceof JCIdent) ? id(((JCIdent)tToCheck).name) : translateToCheck(getToCheck());
        }

        private JCExpression makeNullCheckCondition(JCExpression tToCheck) {
            return NEnull(copyOfTranslatedToCheck(tToCheck));
        }

        private JCExpression makeDefault(Type theResultType, Type theFullType) {
            JCExpression defaultValue = DefaultValue(theFullType);
            // Return default value for Pointer type to be null
            return defaultValue.type == syms.botType ?
                DefaultValue(theResultType) :
                (types.isSameType(theResultType, syms.javafx_PointerType)) ?
                Null() :
                convertTranslated(defaultValue, diagPos, theFullType, theResultType);
        }

        protected JCExpression wrapInNullCheckExpression(JCExpression full, JCExpression tToCheck, Type theResultType, Type theFullType) {
            if (needNullCheck()) {
                // Do a null check
                // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
                // an expression is the desired result of the translation, convert it to a conditional expression
                // if it would dereference null, then the full expression instead yields the default value
                return 
                    If (makeNullCheckCondition(tToCheck),
                        full,
                        makeDefault(theResultType, theFullType));
            } else {
                return full;
            }
        }

        protected JCStatement wrapInNullCheckStatement(JCExpression full, JCExpression tToCheck, Type theResultType, Type theFullType) {
            if (needNullCheck()) {
                // Do a null check
                // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
                // a statement is the desired result of the translation, return the If-statement
                JCStatement nullAction = null;
                if (theResultType != null && theResultType != syms.voidType) {
                    nullAction = Stmt(makeDefault(theResultType, theFullType), theResultType);
                }
                return If(makeNullCheckCondition(tToCheck), 
                        Stmt(full, theResultType),
                        nullAction);
            } else {
                return Stmt(full, theResultType);
            }
        }

        private boolean possiblyNull(JFXExpression expr) {
            if (expr == null) {
                return true;
            }
            switch (expr.getFXTag()) {
               case ASSIGN:
                   return possiblyNull(((JFXAssign)expr).getExpression());
               case APPLY:
                   return true;
               case BLOCK_EXPRESSION:
                   return possiblyNull(((JFXBlock)expr).getValue());
               case IDENT: {
                   if (((JFXIdent)expr).sym instanceof VarSymbol) {
                       Symbol sym = ((JFXIdent)expr).sym;
                       return sym.name != names._this && sym.name != names._super;
                   } else {
                       return false;
                   }
               }
               case CONDEXPR:
                   return possiblyNull(((JFXIfExpression)expr).getTrueExpression()) || possiblyNull(((JFXIfExpression)expr).getFalseExpression());
               case LITERAL:
                   return expr.getJavaFXKind() == JavaFXKind.NULL_LITERAL;
               case PARENS:
                   return possiblyNull(((JFXParens)expr).getExpression());
               case SELECT:
                   return ((JFXSelect)expr).sym instanceof VarSymbol;
               case SEQUENCE_INDEXED:
                   return true;
               case TYPECAST:
                   return possiblyNull(((JFXTypeCast)expr).getExpression());
               case VAR_DEF:
                   return possiblyNull(((JFXVar)expr).getInitializer());
                default:
                    return false;
            }
        }
    }

    class SelectTranslator extends NullCheckTranslator {

        protected final JFXSelect tree;
        protected final boolean isFunctionReference;
        protected final Name name;

        protected SelectTranslator(JFXSelect tree) {
            super(tree.pos(), tree.sym, tree.type);
            this.tree = tree;
            this.isFunctionReference = tree.type instanceof FunctionType && refSym.type instanceof MethodType;
            this.name = tree.getIdentifier();
        }

        @Override
        JFXExpression getToCheck() {
            return tree.getExpression();
        }

        @Override
        JCExpression fullExpression(JCExpression tToCheck) {
            if (isFunctionReference) {
                MethodType mtype = (MethodType) refSym.type;
                JCExpression translated = Select(tToCheck, name);
                return new FunctionValueTranslator(translated, null, diagPos, mtype, fullType).doitExpr();
            } else {
                JCExpression translated = Select(tToCheck, name);
                return convertVariableReference(translated, refSym);
            }
        }
    }

    class FunctionCallTranslator extends NullCheckTranslator {

        // Function determination
        protected final JFXExpression meth;
        protected final JFXExpression selector;
        protected final boolean thisCall;
        protected final boolean superCall;
        protected final MethodSymbol msym;
        protected final Symbol selectorSym;
        protected final boolean renameToThis;
        protected final boolean renameToSuper;
        protected final boolean superToStatic;
        protected final boolean useInvoke;
        protected final boolean callBound;
        protected final boolean magicIsInitializedFunction;
        protected final boolean magicPointerMakeFunction;

        // Call info
        protected final List<JFXExpression> typeargs;
        protected final List<JFXExpression> args;

        // Null Checking control
        protected final boolean knownNonNull;

        FunctionCallTranslator(final JFXFunctionInvocation tree) {
            // If this is an invoke (later "useInvoke") then the named meth is not the refSym
            // since it will be wrapped with a ".invoke()"
            super(
                    tree.pos(),
                    (tree.meth.type instanceof FunctionType)? null : expressionSymbol(tree.meth),
                    tree.type);

            // Function determination
            meth = tree.meth;
            JFXSelect fieldAccess = meth.getFXTag() == JavafxTag.SELECT ? (JFXSelect) meth : null;
            selector = fieldAccess != null ? fieldAccess.getExpression() : null;
            msym = (refSym instanceof MethodSymbol) ? (MethodSymbol) refSym : null;
            Name selectorIdName = (selector != null && selector.getFXTag() == JavafxTag.IDENT) ? ((JFXIdent) selector).getName() : null;
            thisCall = selectorIdName == names._this;
            superCall = selectorIdName == names._super;
            ClassSymbol csym = currentClass().sym;

            useInvoke = meth.type instanceof FunctionType;
            selectorSym = selector != null? expressionSymbol(selector) : null;
            boolean namedSuperCall =
                    msym != null && !msym.isStatic() &&
                    selectorSym instanceof ClassSymbol &&
                    // FIXME should also allow other enclosing classes:
                    types.isSuperType(selectorSym.type, csym);
            boolean isMixinSuper = namedSuperCall && (selectorSym.flags_field & JavafxFlags.MIXIN) != 0;
            boolean canRename = namedSuperCall && !isMixinSuper;
            renameToThis = canRename && selectorSym == csym;
            renameToSuper = canRename && selectorSym != csym;
            superToStatic = (superCall || namedSuperCall) && isMixinSuper;

            callBound = msym != null && !useInvoke &&
                  ((msym.flags() & JavafxFlags.BOUND) != 0);

            magicIsInitializedFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_IS_INITIALIZED) != 0;
            magicPointerMakeFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_POINTER_MAKE) != 0;
            
            // Call info
            this.typeargs = tree.getTypeArguments();
            this.args = tree.getArguments();

            // Null Checking control
            boolean selectorImmutable =
                    msym == null ||
                    msym.isStatic() ||
                    selector == null ||
                    selector.type.isPrimitive() ||
                    namedSuperCall ||
                    superCall ||
                    thisCall;
            knownNonNull =  selectorImmutable && !useInvoke;
       }

        @Override
        JCExpression translateToCheck(JFXExpression expr) {
            if (renameToSuper || superCall) {
                return id(names._super);
            } else if (renameToThis || thisCall) {
                return id(names._this);
            } else if (superToStatic) {
                return staticReference(msym);
            } else {
                return super.translateToCheck(expr);
            }
        }

        @Override
        JCExpression fullExpression(JCExpression mungedToCheckTranslated) {
            JCExpression tMeth = Select(mungedToCheckTranslated, methodName());
            JCMethodInvocation app = m().Apply(translateExprs(typeargs), tMeth, determineArgs());

            JCExpression full = null;
            if (callBound) {
                // Call to bound functions in bind context is handled elsewhere.

                /*
                 * call Pointer.get() on the return value of bound function to get
                 * computed value of bound function. We need to cast to the right
                 * type as Pointer.get() returns Object type value.
                 */
                full = castFromObject(Call(app, defs.get_PointerMethodName), resultType);
            } else {
                full = app;
                if (useInvoke) {
                    if (resultType != syms.voidType) {
                        full = castFromObject(full, resultType);
                    }
                }
            }
            return full;
        }

        Name methodName() {
            return useInvoke? defs.invoke_MethodName : functionName(msym, superToStatic, callBound);
        }

        @Override
        JFXExpression getToCheck() {
            return useInvoke? meth : selector;
        }

        @Override
        boolean needNullCheck() {
            return !knownNonNull && super.needNullCheck();
        }

        /**
         * Compute the translated arguments.
         */
        List<JCExpression> determineArgs() {
            final List<Type> formals = meth.type.getParameterTypes();
            final boolean usesVarArgs = args != null && msym != null &&
                    (msym.flags() & Flags.VARARGS) != 0 &&
                    (formals.size() != args.size() ||
                    types.isConvertible(args.last().type,
                    types.elemtype(formals.last())));
            ListBuffer<JCExpression> targs = ListBuffer.lb();
            // if this is a super.foo(x) call, "super" will be translated to referenced class,
            // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
            if (superToStatic) {
                targs.append(id(defs.receiverName));
            }

            if (callBound) {
                // This section handles argument expression for bound function calls
                // from non-bind call site.

                /*
                 * For each source bound function argument expression, we create a wrapper
                 * variable of type FXConstant and pass it along with FXConstant.VOFF$value
                 * as the arguments to the bound function.
                 */
                Type formal = null;
                List<Type> t = formals;
                for (JFXExpression arg : args) {
                    formal = t.head;
                    t = t.tail;
                    // pass FXConstant wrapper for argument expression
                    targs.append(Call(defs.FXConstant_make, translateExpr(arg, formal)));

                    // pass FXConstant.VOFF$value as offset value
                    targs.append(Select(makeType(syms.javafx_FXConstantType), defs.varOFF$valueName));
                }
            } else if (magicIsInitializedFunction) {
                //isInitialized has just one argument -- we need to split into
                //an inst/var offset pair so that the builtins function can be called
                //Note: in principle this could be inlined as an FXBase call
                JCExpression receiver = null;
                JCExpression varOffset = null;
                switch (args.head.getFXTag()) {
                    case SELECT: {
                        JFXSelect select = (JFXSelect)args.head;
                        receiver = select.sym.isStatic() ?
                            Call(defs.scriptLevelAccessMethod(names, select.sym.owner)) :
                            translateExpr(select.selected, syms.javafx_FXBaseType);
                        varOffset = Offset(select.sym);
                        break;
                    }
                    case IDENT: {
                        JFXIdent ident = (JFXIdent)args.head;
                        receiver = ident.sym.isStatic() ?
                            Call(defs.scriptLevelAccessMethod(names, ident.sym.owner)) :
                            getReceiverOrThis(ident.sym);

                        varOffset = Offset(ident.sym);
                        break;
                    }
                }
                targs.append(receiver);
                targs.append(varOffset);
            } else if (magicPointerMakeFunction) {
                // Pointer.make has just one argument -- we need to split into
                // three args - an inst, var offset and var type - so that the
                // Pointer.make(FXObject, int, Class) is called.
                switch (args.head.getFXTag()) {
                    case SELECT: {
                        JFXSelect select = (JFXSelect)args.head;
                        targs.append(translateArg(select.selected, syms.javafx_FXBaseType));
                        targs.append(Offset(select.sym));
                        Type type = types.erasure(select.type);
                        JCExpression varType = m().ClassLiteral(type);
                        targs.append(varType);
                        break;
                    }
                    case IDENT: {
                        JFXIdent ident = (JFXIdent)args.head;
                        JCExpression receiver = ident.sym.isStatic() ?
                            Call(defs.scriptLevelAccessMethod(names, ident.sym.owner)) :
                            getReceiverOrThis(ident.sym);
                        targs.append(receiver);
                        targs.append(Offset(ident.sym));
                        Type type = types.erasure(ident.type);
                        JCExpression varType = m().ClassLiteral(type);
                        targs.append(varType);
                        break;
                    }
                }
            }
            else {
                boolean handlingVarargs = false;
                Type formal = null;
                List<Type> t = formals;
                for (List<JFXExpression> l = args; l.nonEmpty(); l = l.tail) {
                    JFXExpression arg = l.head;
                    if (!handlingVarargs) {
                        formal = t.head;
                        t = t.tail;
                        if (usesVarArgs && t.isEmpty()) {
                            formal = types.elemtype(formal);
                            handlingVarargs = true;
                        }
                    }
                    targs.append(translateArg(arg, formal));
                }
            }
            return targs.toList();
        }

        JCExpression translateArg(JFXExpression arg, Type formal) {
            return preserveSideEffects(formal, arg, translateExpr(arg, formal));
        }
    }

    class TimeLiteralTranslator extends ExpressionTranslator {

        JFXExpression value;

        TimeLiteralTranslator(JFXTimeLiteral tree) {
            super(tree.pos());
            this.value = tree.value;
        }

        protected ExpressionResult doit() {
            return toResult(
                    Call(defs.Duration_valueOf, translateExpr(value, syms.doubleType)),
                    syms.javafx_DurationType);
        }
    }

    class FunctionTranslator extends Translator {

        final JFXFunctionDefinition tree;
        final boolean maintainContext;
        final MethodType mtype;
        final MethodSymbol sym;
        final Symbol owner;
        final Name name;
        final boolean isBound;
        final boolean isRunMethod;
        final boolean isAbstract;
        final boolean isStatic;
        final boolean isSynthetic;
        final boolean isInstanceFunction;
        final boolean isInstanceFunctionAsStaticMethod;
        final boolean isMixinClass;

        FunctionTranslator(JFXFunctionDefinition tree, boolean maintainContext) {
            super(tree.pos());
            this.tree = tree;
            this.maintainContext = maintainContext;
            this.mtype = (MethodType) tree.type;
            this.sym = (MethodSymbol) tree.sym;
            this.owner = sym.owner;
            this.name = tree.name;
            this.isBound = (sym.flags() & JavafxFlags.BOUND) != 0;
            this.isRunMethod = syms.isRunMethod(tree.sym);
            this.isMixinClass = currentClass().isMixinClass();
            long originalFlags = tree.mods.flags;
            this.isAbstract = (originalFlags & Flags.ABSTRACT) != 0L;
            this.isSynthetic = (originalFlags & Flags.SYNTHETIC) != 0L;
            this.isStatic = (originalFlags & Flags.STATIC) != 0L;
            this.isInstanceFunction = !isAbstract && !isStatic && !isSynthetic;
            this.isInstanceFunctionAsStaticMethod = isInstanceFunction && isMixinClass;
        }

        private JCBlock makeRunMethodBody(JFXBlock bexpr) {
            final JFXExpression value = bexpr.value;
            JCBlock block;
            if (value == null || value.type == syms.voidType) {
                // the block has no value: translate as simple statement and add a null return
                block = translateToBlock(bexpr, syms.voidType);
                block.stats = block.stats.append(Return(Null()));
            } else {
                // block has a value, return it
                block = translateToBlock(bexpr, value.type);
                final Type valueType = value.type;
                if (valueType != null && valueType.isPrimitive()) {
                    // box up any primitives returns so they return Object -- the return type of the run method
                    new TreeTranslator() {

                        @Override
                        public void visitReturn(JCReturn tree) {
                            tree.expr = makeBox(tree.expr.pos(), tree.expr, valueType);
                            result = tree;
                        }
                        // do not descend into inner classes

                        @Override
                        public void visitClassDef(JCClassDecl tree) {
                            result = tree;
                        }
                    }.translate(block);
                }
            }
            return block;
        }

        private long methodFlags() {
            long methodFlags = tree.mods.flags;
            methodFlags &= ~Flags.PROTECTED;
            methodFlags &= ~Flags.SYNTHETIC;
            methodFlags |= Flags.PUBLIC;
            if (isInstanceFunctionAsStaticMethod) {
                methodFlags |= Flags.STATIC;
            }
            return methodFlags;
        }

        private List<JCVariableDecl> methodParameters() {
            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            if (isInstanceFunctionAsStaticMethod) {
                // if we are converting a standard instance function (to a static method), the first parameter becomes a reference to the receiver
                params.prepend(ReceiverParam(currentClass()));
            }
            if (isBound) {
                for (JFXVar fxVar : tree.getParams()) {
                    params.append(Param(syms.javafx_FXObjectType,
                            boundFunctionObjectParamName(fxVar.name)));
                    params.append(Param(syms.javafx_IntegerType,
                            boundFunctionVarNumParamName(fxVar.name)));
                }
            } else {
                for (JFXVar fxVar : tree.getParams()) {
                    params.append(convertParam(fxVar));
                }
            }
            return params.toList();
        }

        private JCBlock methodBody() {
            // construct the body of the translated function
            JFXBlock bexpr = tree.getBodyExpression();
            JCBlock body;
            if (bexpr == null) {
                body = null; // null if no block expression
            } else if (isRunMethod) {
                // it is a module level run method, do special translation
                body = makeRunMethodBody(bexpr);
            } else {
                // the "normal" case
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                if (! isBound) {
                    for (JFXVar fxVar : tree.getParams()) {
                        if (types.isSequence(fxVar.sym.type)) {
                            setDiagPos(fxVar);
                            stmts.append(CallStmt(id(fxVar.getName()), defs.incrementSharing_SequenceMethodName));
                        }
                    }
                } // else FIXME: what should we do for bound function sequence params?

                setDiagPos(bexpr);
                stmts.appendList(translateToStatementsResult(bexpr, isBound? syms.javafx_PointerType : mtype.getReturnType()).statements());
                body = Block(stmts);
            }

            if (isInstanceFunction && !isMixinClass) {
                //TODO: unfortunately, some generated code still expects a receiver$ to always be present.
                // In the instance as instance case, there is no receiver param, so allow generated code
                // to function by adding:   var receiver = this;
                //TODO: this should go away
                body.stats = body.stats.prepend( m().VarDef(
                        m().Modifiers(Flags.FINAL),
                        defs.receiverName,
                        id(interfaceName(currentClass())),
                        id(names._this)));
            }
            return body;
        }

        private JCMethodDecl makeMethod(long flags, JCBlock body, List<JCVariableDecl> params) {
            JCMethodDecl meth = m().MethodDef(
                    addAccessAnnotationModifiers(diagPos, tree.mods.flags, m().Modifiers(flags)),
                    functionName(sym, isInstanceFunctionAsStaticMethod, isBound),
                    makeReturnTypeTree(diagPos, sym, isBound),
                    m().TypeParams(mtype.getTypeArguments()),
                    params,
                    m().Types(mtype.getThrownTypes()), // makeThrows(diagPos), //
                    body,
                    null);
            meth.sym = sym;
            meth.type = tree.type;
            return meth;
        }

        protected SpecialResult doit() {
            JFXTree prevWhere = getAttrEnv().where;
            Yield prevYield = yield();
            Type prevTargetType = targetType;
            getAttrEnv().where = tree;
            yieldKind = ToStatement;
            targetType = null;

            ReceiverContext prevContext = receiverContext();
            if (!maintainContext) {
                setReceiverContext(isStatic ?
                    ReceiverContext.ScriptAsStatic :
                    isInstanceFunctionAsStaticMethod ?
                        ReceiverContext.InstanceAsStatic :
                        ReceiverContext.InstanceAsInstance);
            }

            try {
                return new SpecialResult(makeMethod(methodFlags(), methodBody(), methodParameters()));
            } finally {
                setReceiverContext(prevContext);
                yieldKind = prevYield;
                targetType = prevTargetType;
                getAttrEnv().where = prevWhere;
            }
        }
    }

    class IdentTranslator extends MemberReferenceTranslator {
        protected final JFXIdent tree;
        protected final Symbol sym;
        IdentTranslator(JFXIdent tree) {
            super(tree.pos());
            this.tree = tree;
            this.sym = tree.sym;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), tree.type);
        }

        protected JCExpression doitExpr() {
            if (tree.getName() == names._this) {
                // in the static implementation method, "this" becomes "receiver$"
                return getReceiverOrThis(sym);
            } else if (tree.getName() == names._super) {
                if (types.isMixin(tree.type.tsym)) {
                    // "super" becomes just the class where the static implementation method is defined
                    //  the rest of the implementation is in visitFunctionInvocation
                    return id(tree.type.tsym.name);
                } else {
                    // Just use super.
                    return id(tree.getName());
                }
            }

            int kind = sym.kind;
            if (kind == Kinds.TYP) {
                // This is a class name, replace it with the full name (no generics)
                return makeType(types.erasure(sym.type), false);
            }

            // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
            // and possible outer access methods
            JCExpression convert;
            boolean isStatic = sym.isStatic();
            if (isStatic) {
                // make class-based direct static reference:   Foo.x
                convert = Select(staticReference(sym),tree.getName());
            } else {
                if ((kind == Kinds.VAR || kind == Kinds.MTH) &&
                        sym.owner.kind == Kinds.TYP) {
                    // it is a non-static attribute or function class member
                    // reference it through the receiver
                    convert = Select(getReceiver(sym),tree.getName());
                } else {
                    convert = id(tree.getName());
                }
            }

            if (tree.type instanceof FunctionType && sym.type instanceof MethodType) {
                MethodType mtype = (MethodType) sym.type;
                JFXFunctionDefinition def = null; // FIXME
                return new FunctionValueTranslator(convert, def, tree.pos(), mtype, tree.type).doitExpr();
            }

            return convertVariableReference(convert, sym);
        }
    }

    /**
     * Bound identifier reference (non-sequence)
     */
    class BoundIdentTranslator extends IdentTranslator {

        BoundIdentTranslator(JFXIdent tree) {
            super(tree);
        }

        @Override
        protected ExpressionResult doit() {
            if (sym instanceof VarSymbol) {
                VarSymbol vsym = (VarSymbol) sym;
                if (currentClass().sym.isSubClass(sym.owner, types)) {
                    // The var is in our class (or a superclass)
                    if ((receiverContext() == ReceiverContext.ScriptAsStatic) == sym.isStatic()) {
                        addBindee(vsym);
                    }
                } else {
                    // The reference is to a presumably outer class
                    //TODO:
                    }

            }
            return super.doit();
        }
    }

    class BoundBlockExpressionTranslator extends ExpressionTranslator {

        private final JFXExpression value;
        private final List<JFXExpression> statements;

        BoundBlockExpressionTranslator(JFXBlock tree) {
            super(tree.pos());
            this.value = tree.value;
            this.statements = tree.getStmts();
        }

        protected ExpressionResult doit() {
            for (JFXExpression expr : statements) {
                translateStmt(expr, syms.voidType);
            }
            JCExpression tvalue = translateExpr(value, targetType);
            return toResult(tvalue, targetType);
        }
    }

    /**
     * Translator for assignment, and other mutating operations
     */
    abstract class AssignTranslator extends NullCheckTranslator {

        protected final JFXExpression ref;
        protected final JFXExpression indexOrNull;
        protected final JFXExpression rhs;
        protected final JFXExpression selector;
        protected final JCExpression rhsTranslated;
        private final JCExpression rhsTranslatedPreserved;
        protected final boolean useSetters;

        /**
         *
         * @param diagPos
         * @param ref Variable being referenced (different from LHS if indexed -- where it is sequence or array)
         * @param indexOrNull The index into the variable reference.  Or null if not indexed.
         * @param fullType The type of the resultant expression
         * @param rhs The expression acting on ref
         */
        AssignTranslator(final DiagnosticPosition diagPos, final JFXExpression ref, final JFXExpression indexOrNull, Type fullType, final JFXExpression rhs) {
            super(diagPos, expressionSymbol(ref), fullType);
            this.ref = ref;
            this.indexOrNull = indexOrNull;
            this.rhs = rhs;
            this.selector = (ref instanceof JFXSelect) ? ((JFXSelect) ref).getExpression() : null;
            this.rhsTranslated = rhs==null? null : convertNullability(diagPos, translateExpr(rhs, rhsType()), rhs, rhsType());
            this.rhsTranslatedPreserved = rhs==null? null : preserveSideEffects(fullType, rhs, rhsTranslated);
            this.useSetters = refSym==null? false : typeMorpher.varMorphInfo(refSym).useAccessors();
        }

        /**
         * Constructor for assignment forms: =, ++, +=, etc
         * @param diagPos
         * @param lhs
         * @param rhs
         */
        AssignTranslator(final DiagnosticPosition diagPos, final JFXExpression lhs, final JFXExpression rhs) {
            this(
                    diagPos,
                    lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED? ((JFXSequenceIndexed)lhs).getSequence() : lhs,
                    lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED? ((JFXSequenceIndexed)lhs).getIndex() : null,
                    lhs.type,
                    rhs);
        }

        JCExpression buildRHS(JCExpression rhsTranslated) {
            return rhsTranslated;
        }

        JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
            throw new AssertionError("should not reach here");
        }

        @Override
        JFXExpression getToCheck() {
            return selector;
        }

        @Override
        boolean needNullCheck() {
            return selector != null && super.needNullCheck();
        }

        JCExpression translateIndex() {
            return indexOrNull==null? null : translateExpr(indexOrNull, syms.intType);
        }

        // Figure out the instance containing the variable
        JCExpression instance(JCExpression tToCheck) {
            if (staticReference) {
                return Call(tToCheck, scriptLevelAccessMethod(refSym.owner));
            } else if (tToCheck == null) {
                return id(names._this);
            } else {
                return tToCheck;
            }
        }

        JCExpression sequencesOp(RuntimeMethod meth, JCExpression tToCheck) {
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            if (refSym.owner.kind != Kinds.TYP) {
                // Local variable sequence -- make a block expression, roughly:
                // { Foo tmp = rhs;
                //   lhs = sequenceAction(lhs, rhs);
                //   tmp;
                // }
                args.append(id(refSym.name));
                JCVariableDecl tv = TmpVar(types.boxedTypeOrType(rhsType()), buildRHS(rhsTranslated));
                args.append(id(tv));
                JCExpression tIndex = translateIndex();
                if (tIndex != null) {
                    args.append(tIndex);
                }
                JCExpression res = Call(meth, args);
                return BlockExpression(
                        tv,
                        Stmt(m().Assign(id(refSym.name), res)),
                        id(tv));
            } else {
                // Instance variable sequence -- roughly:
                // sequenceAction(instance, varNum, rhs);
                args.append(instance(tToCheck));
                args.append(Offset(copyOfTranslatedToCheck(translateToCheck(selector)), refSym));
                args.append(buildRHS(rhsTranslated));
                JCExpression tIndex = translateIndex();
                if (tIndex != null) {
                    args.append(tIndex);
                }
                return Call(meth, args);
            }
        }

        JCExpression makeSliceEndPos(JFXSequenceSlice tree) {
            JCExpression endPos;
            if (tree.getLastIndex() == null) {
                endPos = Call(
                        translateExpr(tree.getSequence(), null),
                        defs.size_SequenceMethodName);
                if (tree.getEndKind() == SequenceSliceTree.END_EXCLUSIVE) {
                    endPos = MINUS(endPos, Int(1));
                }
            } else {
                endPos = translateExpr(tree.getLastIndex(), syms.intType);
                if (tree.getEndKind() == SequenceSliceTree.END_INCLUSIVE) {
                    endPos = PLUS(endPos, Int(1));
                }
            }
            return endPos;
        }

        @Override
        JCExpression fullExpression(JCExpression tToCheck) {
            if (indexOrNull != null) {
                if (ref.type.tag == TypeTags.ARRAY) {
                    // set of an array element --  s[i]=8, set the array element
                    JCExpression tArray = buildGetter(instance(tToCheck));
                    return m().Assign(m().Indexed(tArray, translateIndex()), buildRHS(rhsTranslated));
                } else {
                    // set of a sequence element --  s[i]=8, call the sequence set method
                    return sequencesOp(defs.Sequences_set, tToCheck);
                }
            } else {
                if (useSetters) {
                    return postProcessExpression(buildSetter(tToCheck, buildRHS(rhsTranslatedPreserved)));
                } else {
                    //TODO: possibly should use, or be unified with convertVariableReference
                    JCExpression lhsTranslated = selector != null ?
                        m().Select(tToCheck, refSym.name) :
                        m().Ident(refSym);
                    JCExpression res =  defaultFullExpression(lhsTranslated, rhsTranslatedPreserved);
                    return res;
                }
            }
        }

        /**
         * Override to change the translation type of the right-hand side
         */
        protected Type rhsType() {
            if (indexOrNull != null) {
                // Indexed assignment
                if (types.isArray(rhs.type) || types.isSequence(rhs.type)) {
                    return ref.type;
                } else {
                    return types.elementType(ref.type);
                }
            } else {
                if (refSym == null) {
                    return ref.type;
                } else {
                    // Handle type inferencing not reseting the ident type
                    return refSym.type;
                }
            }
        }

        /**
         * Override to change result in the non-default case.
         */
        protected JCExpression postProcessExpression(JCExpression built) {
            return built;
        }

        JCExpression buildSetter(JCExpression tc, JCExpression rhsComplete) {
            return Call(tc, attributeSetterName(refSym), rhsComplete);
        }

        JCExpression buildGetter(JCExpression tc) {
            return Call(tc, attributeGetterName(refSym));
        }
    }

    class UnaryOperationTranslator extends ExpressionTranslator {

        private final JFXUnary tree;
        private final JFXExpression expr;
        private final JCExpression transExpr;

        UnaryOperationTranslator(JFXUnary tree) {
            super(tree.pos());
            this.tree = tree;
            this.expr = tree.getExpression();
            this.transExpr = translateExpr(expr, expr.type);
        }

        protected AbstractStatementsResult doit() {
            switch (tree.getFXTag()) {
                case SIZEOF:
                    if (expr.type.tag == TypeTags.ARRAY) {
                        return toResult(Select(transExpr, defs.length_ArrayFieldName), syms.intType);
                    }
                    return toResult(translateSizeof(expr, transExpr), syms.intType);
                case REVERSE:
                    if (types.isSequence(expr.type)) {
                        // call runtime reverse of a sequence
                        return toResult(
                             Call(defs.Sequences_reverse, transExpr),
                             expr.type);
                    } else {
                        // this isn't a sequence, just make it a sequence
                        return toResult(convertTranslated(transExpr, diagPos, expr.type, targetType), targetType);
                    }
                case NEG:
                    if (types.isSameType(tree.type, syms.javafx_DurationType)) {
                        return toResult(
                                Call(translateExpr(tree.arg, tree.arg.type), defs.negate_DurationMethodName),
                                syms.javafx_DurationType);
                    }
                default:
                    return toResult(
                            m().Unary(tree.getOperatorTag(), transExpr),
                            tree.type);
            }
        }
    }

    class BinaryOperationTranslator extends ExpressionTranslator {

        final JFXBinary tree;
        final Type lhsType;
        final Type rhsType;

        BinaryOperationTranslator(DiagnosticPosition diagPos, final JFXBinary tree) {
            super(diagPos);
            this.tree = tree;
            this.lhsType = tree.lhs.type;
            this.rhsType = tree.rhs.type;
        }

        JCExpression lhs(Type type) {
            return translateExpr(tree.lhs, type);
        }

        JCExpression lhs() {
            return lhs(null);
        }

        JCExpression rhs(Type type) {
            return translateExpr(tree.rhs, type);
        }

        JCExpression rhs() {
            return rhs(null);
        }

        //TODO: after type system is figured out, this needs to be revisited
        /**
         * Check if a primitive has the default value for its type.
         */
        private JCExpression makePrimitiveNullCheck(Type argType, JCExpression arg) {
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
            JCExpression defaultValue = makeLit(diagPos, tmi.getRealType(), tmi.getDefaultValue());
            return EQ(arg, defaultValue);
        }

        /**
         * Check if a non-primitive has the default value for its type.
         */
        private JCExpression makeObjectNullCheck(Type argType, JCExpression arg) {
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
            if (tmi.isSequence() || tmi.getRealType() == syms.javafx_StringType) {
                return Call(defs.Checks_isNull, arg);
            } else {
                return EQnull(arg);
            }
        }

        /**
         * Make a .equals() comparison with a null check on the receiver
         */
        private JCExpression makeFullCheck(JCExpression lhs, JCExpression rhs) {
            return Call(defs.Checks_equals, lhs, rhs);
        }

        /**
         * Return the translation for a == comparision
         */
        private JCExpression translateEqualsEquals() {
            final boolean reqSeq = types.isSequence(lhsType) ||
                    types.isSequence(rhsType);

            Type expected = tree.operator.type.getParameterTypes().head;
            if (reqSeq) {
                Type left = types.isSequence(lhsType) ? types.elementType(lhsType) : lhsType;
                Type right = types.isSequence(rhsType) ? types.elementType(rhsType) : rhsType;
                if (left.isPrimitive() && right.isPrimitive() && left == right) {
                    expected = left;
                }
            }
            Type req = reqSeq ? types.sequenceType(expected) : null;

            // this is an x == y
            if (lhsType.getKind() == TypeKind.NULL) {
                if (rhsType.getKind() == TypeKind.NULL) {
                    // both are known to be null
                    return Boolean(true);
                } else if (rhsType.isPrimitive()) {
                    // lhs is null, rhs is primitive, do default check
                    return makePrimitiveNullCheck(rhsType, rhs(req));
                } else {
                    // lhs is null, rhs is non-primitive, figure out what check to do
                    return makeObjectNullCheck(rhsType, rhs(req));
                }
            } else if (lhsType.isPrimitive()) {
                if (rhsType.getKind() == TypeKind.NULL) {
                    // lhs is primitive, rhs is null, do default check on lhs
                    return makePrimitiveNullCheck(lhsType, lhs(req));
                } else if (rhsType.isPrimitive()) {
                    // both are primitive, use ==
                    return EQ(lhs(req), rhs(req));
                } else {
                    // lhs is primitive, rhs is non-primitive, use equals(), but switch them
                    JCVariableDecl sl = TmpVar(req!=null? req : lhsType, lhs(req));  // eval first to keep the order correct
                    return BlockExpression(
                            sl,
                            makeFullCheck(rhs(req), id(sl.name)));
                }
            } else {
                if (rhsType.getKind() == TypeKind.NULL) {
                    // lhs is non-primitive, rhs is null, figure out what check to do
                    return makeObjectNullCheck(lhsType, lhs(req));
                } else {
                    //  lhs is non-primitive, use equals()
                    return makeFullCheck(lhs(req), rhs(req));
                }
            }
        }

        JCExpression op(JCExpression leftSide, Name methodName, JCExpression rightSide) {
            return Call(leftSide, methodName, rightSide);
        }

        boolean isDuration(Type type) {
            return types.isSameType(type, syms.javafx_DurationType);
        }

        final Type durationNumericType = syms.javafx_NumberType;

        JCExpression durationOp() {
            switch (tree.getFXTag()) {
                case PLUS:
                    return op(lhs(), defs.add_DurationMethodName, rhs());
                case MINUS:
                    return op(lhs(), defs.sub_DurationMethodName, rhs());
                case DIV:
                    return op(lhs(), defs.div_DurationMethodName, rhs(isDuration(rhsType)? null : durationNumericType));
                case MUL: {
                    // lhs.mul(rhs);
                    JCExpression rcvr;
                    JCExpression arg;
                    if (isDuration(lhsType)) {
                        rcvr = lhs();
                        arg = rhs(durationNumericType);
                    } else {
                        //TODO: This may get side-effects out-of-order.
                        // A simple fix is to use a static Duration.mul(double,Duration).
                        // Another is to use a Block and a temporary.
                        rcvr = rhs();
                        arg = lhs(durationNumericType);
                    }
                    return op(rcvr, defs.mul_DurationMethodName, arg);
                }
                case LT:
                    return op(lhs(), defs.lt_DurationMethodName, rhs());
                case LE:
                    return op(lhs(), defs.le_DurationMethodName, rhs());
                case GT:
                    return op(lhs(), defs.gt_DurationMethodName, rhs());
                case GE:
                    return op(lhs(), defs.ge_DurationMethodName, rhs());
            }
            throw new RuntimeException("Internal Error: bad Duration operation");
        }

        /**
         * Translate a binary expressions
         */
        protected ExpressionResult doit() {
            return toResult(doitExpr(), tree.type);
        }

        JCExpression doitExpr() {
            //TODO: handle <>
            if (tree.getFXTag() == JavafxTag.EQ) {
                return translateEqualsEquals();
            } else if (tree.getFXTag() == JavafxTag.NE) {
                return NOT(translateEqualsEquals());
            } else {
                // anything other than == or !=

                // Duration type operator overloading
                if ((isDuration(lhsType) || isDuration(rhsType)) &&
                        tree.operator == null) { // operator check is to try to get a decent error message by falling through if the Duration method isn't matched
                    return durationOp();
                }
                return m().Binary(tree.getOperatorTag(), lhs(), rhs());
            }
        }
    }

    class TypeConversionTranslator extends ExpressionTranslator {

        final JCExpression translated;
        final Type sourceType;
        final Type targettedType;
        final boolean sourceIsSequence;
        final boolean targetIsSequence;
        final boolean sourceIsArray;
        final boolean targetIsArray;

        TypeConversionTranslator(DiagnosticPosition diagPos, JCExpression translated, Type sourceType, Type targettedType) {
            super(diagPos);
            this.translated = translated;
            this.sourceType = sourceType;
            this.targettedType = targettedType;
            this.sourceIsSequence = types.isSequence(sourceType);
            this.targetIsSequence = types.isSequence(targettedType);
            this.sourceIsArray = types.isArray(sourceType);
            this.targetIsArray = types.isArray(targettedType);
        }

        private JCExpression convertNumericSequence(final DiagnosticPosition diagPos,
                final JCExpression expr, final Type inElementType, final Type targetElementType) {
            JCExpression inTypeInfo = TypeInfo(diagPos, inElementType);
            JCExpression targetTypeInfo = TypeInfo(diagPos, targetElementType);
            return Call(
                    defs.Sequences_convertNumberSequence,
                    targetTypeInfo, inTypeInfo, expr);
        }

        private JCExpression convertNumericToCharSequence(final DiagnosticPosition diagPos,
                final JCExpression expr, final Type inElementType) {
            JCExpression inTypeInfo = TypeInfo(diagPos, inElementType);
            return Call(
                    defs.Sequences_convertNumberToCharSequence,
                    inTypeInfo, expr);
        }

        private JCExpression convertCharToNumericSequence(final DiagnosticPosition diagPos,
                final JCExpression expr, final Type targetElementType) {
            JCExpression targetTypeInfo = TypeInfo(diagPos, targetElementType);
            return Call(
                    defs.Sequences_convertCharToNumberSequence,
                    targetTypeInfo, expr);
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), targettedType);
        }

        JCExpression doitExpr() {
            assert sourceType != null;
            assert targettedType != null;
            if (targettedType.tag == TypeTags.UNKNOWN) {
                //TODO: this is bad attribution
                return translated;
            }
            if (types.isSameType(targettedType, sourceType)) {
                return translated;
            }
            if (targetIsArray) {
                Type elemType = types.elemtype(targettedType);
                if (sourceIsSequence) {
                    if (elemType.isPrimitive()) {
                        return Call(defs.Sequences_toArray[types.typeKind(elemType)], translated);
                    }
                    ListBuffer<JCStatement> stats = ListBuffer.lb();
                    JCVariableDecl tmpVar = TmpVar(sourceType, translated);
                    stats.append(tmpVar);
                    JCVariableDecl sizeVar = TmpVar(syms.intType, Call(id(tmpVar), defs.size_SequenceMethodName));
                    stats.append(sizeVar);
                    JCVariableDecl arrVar = TmpVar("arr", targettedType, m().NewArray(
                            makeType(elemType, true),
                            List.<JCExpression>of(id(sizeVar.name)),
                            null));
                    stats.append(arrVar);
                    stats.append(CallStmt(id(tmpVar.name), defs.toArray_SequenceMethodName, List.of(
                            Int(0),
                            id(sizeVar),
                            id(arrVar),
                            Int(0))));
                    return BlockExpression(stats, id(arrVar));
                } else {
                    //TODO: conversion may be needed here, but this is better than what we had
                    return translated;
                }
            } else if (sourceIsArray && targetIsSequence) {
                Type sourceElemType = types.elemtype(sourceType);
                List<JCExpression> args;
                if (sourceElemType.isPrimitive()) {
                    args = List.of(translated);
                } else {
                    args = List.of(TypeInfo(diagPos, sourceElemType), translated);
                }
                return Call(defs.Sequences_fromArray, args);
            }
            if (targetIsSequence && sourceIsSequence) {
                Type sourceElementType = types.elementType(sourceType);
                Type targetElementType = types.elementType(targettedType);
                if (types.isSameType(sourceElementType, targetElementType))
                    return translated;
                else if (types.isNumeric(sourceElementType) && types.isNumeric(targetElementType)) {
                    return convertNumericSequence(diagPos,
                            translated,
                            sourceElementType,
                            targetElementType);
                }
                else if (types.isNumeric(sourceElementType) &&
                        types.isSameType(targetElementType, syms.charType)) {
                    //numeric seq to char seq
                    return convertNumericToCharSequence(diagPos,
                            translated,
                            sourceElementType);
                }
                else if (types.isNumeric(targetElementType) &&
                        types.isSameType(sourceElementType, syms.charType)) {
                    //char seq to numeric seq
                    return convertCharToNumericSequence(diagPos,
                            translated,
                            targetElementType);
                }
            }

            // Convert primitive/Object types
            if (sourceType.isCompound() || sourceType.isPrimitive()) {
                return make.at(diagPos).TypeCast(makeType(types.erasure(targettedType), true), translated);
            }

            // We should add a cast "when needed".  Then visitTypeCast would just
            // call this function, and not need to call makeTypeCast on the result.
            // However, getting that to work is a pain - giving up for now.  FIXME
            return translated;
        }
    }

    class FunctionValueTranslator extends ExpressionTranslator {

        private final JCExpression meth;
        private final JFXFunctionDefinition def;
        private final MethodType mtype;
        private final Type resultType;

        FunctionValueTranslator(JCExpression meth, JFXFunctionDefinition def, DiagnosticPosition diagPos, MethodType mtype, Type resultType) {
            super(diagPos);
            this.meth = meth;
            this.def = def;
            this.mtype = mtype;
            this.resultType = resultType;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), resultType);
        }

        JCExpression doitExpr() {
            ListBuffer<JCTree> members = new ListBuffer<JCTree>();
            if (def != null) {
                // Translate the definition, maintaining the current inInstanceContext
                members.append(translateFunction(def, true));
            }
            JCExpression encl = null;
            int nargs = mtype.argtypes.size();
            Type ftype = syms.javafx_FunctionTypes[nargs];
            JCExpression t = QualifiedTree(ftype.tsym.getQualifiedName().toString());
            ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
            Type rtype = types.boxedTypeOrType(mtype.restype);
            typeargs.append(makeType(rtype));
            ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
            ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
            int i = 0;
            for (List<Type> l = mtype.argtypes; l.nonEmpty(); l = l.tail) {
                Name pname = make.paramName(i++);
                Type ptype = types.boxedTypeOrType(l.head);
                JCVariableDecl param = Param(ptype, pname);
                params.append(param);
                JCExpression marg = id(pname);
                margs.append(marg);
                typeargs.append(makeType(ptype));
            }

            // The backend's Attr skips SYNTHETIC methods when looking for a matching method.
            long flags = Flags.PUBLIC | Flags.BRIDGE; // | SYNTHETIC;

            JCExpression call = make.Apply(null, meth, margs.toList());

            List<JCStatement> stats;
            if (mtype.restype == syms.voidType) {
                stats = List.of(Stmt(call), Return(Null()));
            } else {
                if (mtype.restype.isPrimitive()) {
                    call = makeBox(diagPos, call, mtype.restype);
                }
                stats = List.<JCStatement>of(Return(call));
            }
            JCMethodDecl bridgeDef = m().MethodDef(
                    m().Modifiers(flags),
                    defs.invoke_MethodName,
                    makeType(rtype),
                    List.<JCTypeParameter>nil(),
                    params.toList(),
                    m().Types(mtype.getThrownTypes()),
                    Block(stats),
                    null);

            members.append(bridgeDef);
            JCClassDecl cl = m().AnonymousClassDef(m().Modifiers(0), members.toList());
            List<JCExpression> nilArgs = List.nil();
            return m().NewClass(encl, nilArgs, make.TypeApply(t, typeargs.toList()), nilArgs, cl);
        }
    }

    abstract class NewInstanceTranslator extends ExpressionTranslator {

        // Statements to set symbols with initial values.
        protected ListBuffer<JCStatement> varInits = ListBuffer.lb();

        // Symbols corresponding to caseStats.
        protected ListBuffer<VarSymbol> varSyms = ListBuffer.lb();

        NewInstanceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        /**
         * Does this instance have any instance initializations?
         */
        protected abstract boolean hasInstanceVariableInits();

        /**
         * Initialize the instance variables of the instance
         * @param instName
         */
        protected abstract void initInstanceVariables(Name instName);

        /**
         * buildInstance calls this just after object is created, but before
         * it's instance variables are initialized. Override to generate
         * statements/expressions just after new object is created.
         */
        protected void postInstanceCreation() {
        }

        /**
         * @return the constructor args -- translating any supplied args
         */
        protected abstract List<JCExpression> completeTranslatedConstructorArgs();

        protected JCExpression translateInstanceVariableInit(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
            ExpressionResult eres;
            switch (bindStatus) {
                case UNBOUND:
                    eres = toJava().translateToExpressionResult(init, vsym.type);
                    break;
                case DEPENDENT:
                    eres = translateDependent().translateToExpressionResult(init, vsym.type);
                    break;
                case UNIDIBIND:
                case BIDIBIND:
                default: // javac is confused
                    throw new AssertionError("Should have been inflated to class");
            }
            mergeResults(eres);
            return convertNullability(init.pos(), eres.expr(), init, vsym.type);
        }

        void setInstanceVariable(DiagnosticPosition diagPos, Name instanceName, JavafxBindStatus bindStatus, VarSymbol vsym, JCExpression transInit) {
            JCExpression tc = instanceName == null ? null : id(instanceName);
            JCStatement def;
            if (types.isSequence(vsym.type))
                def = CallStmt(defs.Sequences_set, tc,
                      Offset(id(instanceName), vsym), transInit);
            else
                def = CallStmt(tc, attributeBeName(vsym), transInit);
            varInits.append(def);
            varSyms.append(vsym);
        }

        void setInstanceVariable(Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JFXExpression init) {
            DiagnosticPosition initPos = init.pos();
            JCExpression transInit = translateInstanceVariableInit(init, bindStatus, vsym);
            setInstanceVariable(initPos, instName, bindStatus, vsym, transInit);
        }

        void makeInitSupportCall(Name methName, Name receiverName) {
            addPreface(CallStmt(id(receiverName), methName));
        }

        JCVariableDecl makeTmpLoopVar(int initValue) {
            return m().VarDef(m().Modifiers(0),
                    getSyntheticName("loop"),
                    makeType(syms.intType),
                    Int(initValue));
        }

        void makeInitApplyDefaults(Type classType, Name receiverName) {
            ClassSymbol classSym = (ClassSymbol)classType.tsym;
            int count = varSyms.size();

            JCVariableDecl loopVar = makeTmpLoopVar(0);
            Name loopName = loopVar.name;
            JCExpression loopLimit = Call(id(receiverName), defs.count_FXObjectMethodName);
            JCVariableDecl loopLimitVar = TmpVar("count", syms.intType, loopLimit);
            addPreface(loopLimitVar);
            JCExpression loopTest = LT(id(loopName), id(loopLimitVar.name));
            List<JCExpressionStatement> loopStep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(loopName), Int(1))));
            JCStatement loopBody;

            JCStatement applyDefaultsExpr =
                    CallStmt(
                        id(receiverName),
                        defs.applyDefaults_FXObjectMethodName,
                        id(loopName));

            if (1 < count) {
                // final short[] jfx$0map = GETMAP$X();
                JCExpression getmapExpr = Call(varGetMapName(classSym));
                JCVariableDecl mapVar = TmpVar("map", syms.javafx_ShortArray, getmapExpr);
                addPreface(mapVar);

                LiteralInitVarMap varMap = getLiteralInitClassMap().getVarMap(classSym);
                int[] tags = new int[count];

                int index = 0;
                for (VarSymbol varSym : varSyms) {
                    tags[index++] = varMap.addVar(varSym);
                }

                ListBuffer<JCCase> cases = ListBuffer.lb();
                index = 0;
                for (JCStatement varInit : varInits) {
                    cases.append(m().Case(Int(tags[index++]), List.<JCStatement>of(varInit, m().Break(null))));
                }

                cases.append(m().Case(null, List.<JCStatement>of(applyDefaultsExpr, m().Break(null))));

                JCExpression mapExpr = m().Indexed(id(mapVar), id(loopName));
                loopBody = m().Switch(mapExpr, cases.toList());
            } else {
                VarSymbol varSym = varSyms.first();
                JCExpression varOffsetExpr = Offset(id(receiverName), varSym);
                JCVariableDecl offsetVar = TmpVar("off", syms.intType, varOffsetExpr);
                addPreface(offsetVar);
                loopBody = If(EQ(id(loopName), id(offsetVar)),
                        varInits.first(),
                        applyDefaultsExpr);
            }

            addPreface(m().ForLoop(List.<JCStatement>of(loopVar), loopTest, loopStep, loopBody));
        }

        void makeSetVarFlags(Name receiverName, Type contextType) {
            for (VarSymbol vsym : varSyms) {
                JCExpression flagsToSet = (vsym.flags() & JavafxFlags.VARUSE_BOUND_INIT) != 0 ?
                    id(defs.varFlagINIT_OBJ_LIT_DEFAULT) :
                    id(defs.varFlagINIT_OBJ_LIT);

                addPreface(CallStmt(
                        id(receiverName),
                        defs.varFlagActionChange,
                        Offset(id(receiverName), vsym),
                        id(defs.varFlagALL_FLAGS),
                        flagsToSet));
            }
        }

        /**
         * Return the instance building expression
         * @param declaredType
         * @param cdef
         * @param isFX
         * @return
         */
        protected ExpressionResult buildInstance(Type declaredType, JFXClassDeclaration cdef, boolean isFX) {
            Type type;

            if (cdef == null) {
                type = declaredType;
            } else {
                translateStmt(cdef, syms.voidType);
                type = cdef.type;
            }
            JCExpression classTypeExpr = makeType(type, false);

            List<JCExpression> newClassArgs = completeTranslatedConstructorArgs();

            Name tmpVarName = getSyntheticName("objlit");
            boolean hasVars = hasInstanceVariableInits();
            JCExpression instExpression;
            if (hasVars || (isFX && newClassArgs.nonEmpty()) || cdef != null) {
                // it is a instanciation of a JavaFX class which has instance variable initializers
                // (or is anonymous, or has an outer class argument)
                //
                //   {
                //       final X jfx$0objlit = new X(true);
                //       final short[] jfx$0map = GETMAP$X();
                //
                //       for (int jfx$0initloop = 0; i < X.$VAR_COUNT; i++) {
                //           if (!isInitialized(jfx$0initloop) {
                //               switch (jfx$0map[jfx$0initloop]) {
                //                   1: jfx$0objlit.set$a(0); break;
                //                   2: jfx$0objlit.set$b(0); break;
                //                   ...
                //                   n: jfx$0objlit.set$z(0); break;
                //                   default: jfx$0objlit.applyDefaults$(jfx$0initloop);
                //               }
                //           }
                //       }
                //
                //       jfx$0objlit.complete$();
                //       jfx$0objlit
                //   }

                // Use the JavaFX constructor by adding a marker argument. The "true" in:
                //       ... new X(true);
                newClassArgs = newClassArgs.append(Boolean(true));

                // Create the new instance, placing it in a temporary variable "jfx$0objlit"
                //       final X jfx$0objlit = new X(true);
                addPreface(Var(
                        type,
                        tmpVarName,
                        m().NewClass(null, null, classTypeExpr, newClassArgs, null)));

                // generate stuff just after new object is created
                postInstanceCreation();

                // now initialize it's instance variables
                initInstanceVariables(tmpVarName);

                // (Re-)initialize the flags for the variables set in object literal
                makeSetVarFlags(tmpVarName, declaredType);

                // Apply defaults to the instance variables
                //
                //       final short[] jfx$0map = GETMAP$X();
                //       for (int jfx$0initloop = 0; i < X.$VAR_COUNT; i++) {
                //           ...
                //       }
                if (varSyms.nonEmpty()) {
                    makeInitApplyDefaults(type, tmpVarName);
                } else {
                    makeInitSupportCall(defs.applyDefaults_FXObjectMethodName, tmpVarName);
                }

                // Call complete$ to do user's init and postinit blocks
                //       jfx$0objlit.complete$();
                makeInitSupportCall(defs.complete_FXObjectMethodName, tmpVarName);

                // Return the instance from the block expressions
                //       jfx$0objlit
                instExpression = id(tmpVarName);

            } else {
                // this is a Java class or has no instance variable initializers, just instanciate it
                instExpression = m().NewClass(null, null, classTypeExpr, newClassArgs, null);
                postInstanceCreation();
            }

            return toResult(instExpression, type);
        }
    }

    /**
     * Translator for object literals
     */
    class InstanciateTranslator extends NewInstanceTranslator {

        protected final JFXInstanciate tree;
        private final ClassSymbol idSym;

        InstanciateTranslator(final JFXInstanciate tree) {
            super(tree.pos());
            this.tree = tree;
            this.idSym = (ClassSymbol)JavafxTreeInfo.symbol(tree.getIdentifier());
        }

        @Override
        protected boolean hasInstanceVariableInits() {
            return tree.getParts().nonEmpty();
        }

        @Override
        protected void initInstanceVariables(Name instName) {
            if (tree.varDefinedByThis != null) {
                getSubstitutionMap().put(tree.varDefinedByThis, instName);
            }
            for (JFXObjectLiteralPart olpart : tree.getParts()) {
                diagPos = olpart.pos(); // overwrite diagPos (must restore)
                JavafxBindStatus bindStatus = olpart.getBindStatus();
                JFXExpression init = olpart.getExpression();
                VarSymbol vsym = (VarSymbol) olpart.sym;
                setInstanceVariable(instName, bindStatus, vsym, init);
            }
            if (tree.varDefinedByThis != null) {
                getSubstitutionMap().remove(tree.varDefinedByThis);
            }
            diagPos = tree.pos();
        }

        protected List<JCExpression> translatedConstructorArgs() {
            List<JFXExpression> args = tree.getArgs();
            Symbol sym = tree.constructor;
            if (sym != null && sym.type != null) {
                ListBuffer<JCExpression> translated = ListBuffer.lb();
                List<Type> formals = sym.type.asMethodType().getParameterTypes();
                boolean usesVarArgs = (sym.flags() & Flags.VARARGS) != 0L &&
                        (formals.size() != args.size() ||
                        types.isConvertible(args.last().type, types.elemtype(formals.last())));
                boolean handlingVarargs = false;
                Type formal = null;
                List<Type> t = formals;
                for (List<JFXExpression> l = args; l.nonEmpty(); l = l.tail) {
                    if (!handlingVarargs) {
                        formal = t.head;
                        t = t.tail;
                        if (usesVarArgs && t.isEmpty()) {
                            formal = types.elemtype(formal);
                            handlingVarargs = true;
                        }
                    }
                    JCExpression targ = translateExpr(l.head, formal);
                    if (targ != null) {
                        translated.append(targ);
                    }
                }
                return translated.toList();
            } else {
                return translateExprs(args);
            }
        }

        @Override
        protected List<JCExpression> completeTranslatedConstructorArgs() {
            List<JCExpression> translated = translatedConstructorArgs();
            if (tree.getClassBody() != null &&
                    tree.getClassBody().sym != null && getHasOuters().contains(tree.getClassBody().sym) ||
                    idSym != null && getHasOuters().contains(idSym)) {
                JCExpression receiver = getReceiverOrThis();
                translated = translated.prepend(receiver);
            }
            return translated;
        }

        protected ExpressionResult doit() {
            return buildInstance(tree.type, tree.getClassBody(), types.isJFXClass(idSym));
        }
    }

    class TypeCastTranslator extends ExpressionTranslator {

        private final JFXExpression expr;
        private final JFXTree clazz;

        TypeCastTranslator(final JFXTypeCast tree) {
            super(tree.pos());
            this.expr = tree.getExpression();
            this.clazz = tree.clazz;
        }

        protected ExpressionResult doit() {
            JCExpression tExpr = translateExpr(expr, clazz.type);
            // The makeTypeCast below is usually redundant, since translateAsValue
            // takes care of most conversions - except in the case of a plain object cast.
            // It would be cleaner to move the makeTypeCast to translateAsValue,
            // but it's painful to get it right.  FIXME.
            JCExpression ret = typeCast(clazz.type, expr.type, tExpr);
            ret = convertNullability(diagPos, ret, expr, clazz.type);
            return toResult(ret, clazz.type);
        }
    }

    class InstanceOfTranslator extends ExpressionTranslator {

        private final Type classType;
        private final JFXExpression expr;

        InstanceOfTranslator(JFXInstanceOf tree) {
            super(tree.pos());
            this.classType = types.boxedTypeOrType(tree.clazz.type);
            this.expr = tree.getExpression();
        }

        protected ExpressionResult doit() {
            JCExpression tExpr = translateExpr(expr, null);
            if (expr.type.isPrimitive()) {
                tExpr = makeBox(expr.pos(), tExpr, expr.type);
            }
            if (types.isSequence(expr.type) && !types.isSequence(classType)) {
                tExpr = Call(defs.Sequences_getSingleValue, tExpr);
            }
            JCTree clazz = makeType(classType);
            return toResult(
                    m().TypeTest(tExpr, clazz),
                    syms.booleanType);
        }
    }

    class SequenceEmptyTranslator extends ExpressionTranslator {

        private final Type type;

        SequenceEmptyTranslator(JFXSequenceEmpty tree) {
            super(tree.pos());
            this.type = tree.type;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), type);
        }

        protected JCExpression doitExpr() {
            if (types.isSequence(type)) {
                Type elemType = types.boxedElementType(type);
                JCExpression expr = accessEmptySequence(diagPos, elemType);
                return castFromObject(expr, syms.javafx_SequenceTypeErasure);
            } else {
                return Null();
            }
        }
    }

    /**
     * Translate if-expression
     */
    class IfTranslator extends ExpressionTranslator {

        private final JFXIfExpression tree;

        IfTranslator(JFXIfExpression tree) {
            super(tree.pos());
            this.tree = tree;
        }

        JCExpression sideExpr(JFXExpression expr) {
            ExpressionResult res = translateToExpressionResult(expr, targetType);
            addBindees(res.bindees());
            addInterClassBindees(res.interClass());
            return asExpression(res, targetType);
        }

        JCStatement sideStmt(JFXExpression expr) {
            if (expr == null) {
                return null;
            } else {
                return translateToStatement(expr, targetType);
            }
        }

        protected AbstractStatementsResult doit() {
            JCExpression cond = translateExpr(tree.getCondition(), syms.booleanType);
            JFXExpression trueSide = tree.getTrueExpression();
            JFXExpression falseSide = tree.getFalseExpression();
            if (yield() == ToExpression) {
                return toResult(
                    If (cond,
                        sideExpr(trueSide),
                        sideExpr(falseSide)),
                    targetType);
            } else {
                 return toStatementResult(
                     If (cond,
                        sideStmt(trueSide),
                        sideStmt(falseSide)));
            }
        }
    }

    class IndexOfTranslator extends ExpressionTranslator {
        final JFXIndexof tree;
        IndexOfTranslator(JFXIndexof tree) {
            super(tree.pos());
            assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
            this.tree = tree;
        }
            protected ExpressionResult doit() {
                return toResult(id(indexVarName(tree.fname)), tree.type);
            }
    }

    class SequenceIndexedTranslator extends ExpressionTranslator {

        private final JFXExpression seq;
        private final JCExpression tSeq;
        private final JCExpression tIndex;
        private final Type resultType;

        SequenceIndexedTranslator(DiagnosticPosition diagPos, JFXExpression seq, JCExpression tSeq, JCExpression tIndex, Type resultType) {
            super(diagPos);
            this.seq = seq;
            this.tSeq = tSeq;
            this.tIndex = tIndex;
            this.resultType = resultType;
        }

        SequenceIndexedTranslator(JFXSequenceIndexed tree) {
            super(tree.pos());
            this.seq = tree.getSequence();
            this.tSeq = translateExpr(seq, null);  //FIXME
            this.tIndex = translateExpr(tree.getIndex(), syms.intType);
            this.resultType = tree.type;
        }

        JCExpression translateSequenceIndexed() {
            int typeKind = types.typeKind(resultType);
            if (seq instanceof JFXIdent) {
                JFXIdent var = (JFXIdent) seq;
                OnReplaceInfo info = findOnReplaceInfo(var.sym);
                if (info != null
                        && (var.sym.flags_field & JavafxFlags.VARUSE_OPT_TRIGGER) != 0) {
                    JFXOnReplace onReplace = info.onReplace;
                    ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                    Symbol sym = info.vmi.getSymbol();
                    args.append(getReceiverOrThis((VarSymbol) sym));
                    args.append(Offset(sym));
                    args.append(id(paramStartPosName(onReplace)));
                    args.append(id(paramNewElementsLengthName(onReplace)));
                    args.append(tIndex);
                    return Call(defs.Sequences_getAsFromNewElements[typeKind], args);
                }
            }
            Name getMethodName = defs.typedGet_SequenceMethodName[typeKind];
            return Call(tSeq, getMethodName, tIndex);
        }

        protected ExpressionResult doit() {
            return toResult(
                    doitExpr(),
                    resultType);
        }

        protected JCExpression doitExpr() {
            if (seq.type.tag == TypeTags.ARRAY) {
                return m().Indexed(tSeq, tIndex);
            } else {
                return translateSequenceIndexed();
            }
        }
    }

    class SequenceSliceTranslator extends ExpressionTranslator {

        private final Type type;
        private final JCExpression seq;
        private final int endKind;
        private final JFXExpression firstIndex;
        private final JFXExpression lastIndex;

        SequenceSliceTranslator(JFXSequenceSlice tree) {
            super(tree.pos());
            this.type = tree.type;
            this.seq = translateExpr(tree.getSequence(), null);  //FIXME
            this.endKind = tree.getEndKind();
            this.firstIndex = tree.getFirstIndex();
            this.lastIndex = tree.getLastIndex();
        }

        JCExpression computeSliceEnd() {
            JCExpression endPos;
            if (lastIndex == null) {
                endPos = Call(seq, defs.size_SequenceMethodName);
                if (endKind == SequenceSliceTree.END_EXCLUSIVE) {
                    endPos = MINUS(endPos, Int(1));
                }
            } else {
                endPos = translateExpr(lastIndex, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE) {
                    endPos = PLUS(endPos, Int(1));
                }
            }
            return endPos;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), type);
        }

        protected JCExpression doitExpr() {
            JCExpression tFirstIndex = translateExpr(firstIndex, syms.intType);
            return Call(seq, defs.getSlice_SequenceMethodName, tFirstIndex, computeSliceEnd());
        }
    }

    class ExplicitSequenceTranslator extends ExpressionTranslator {

        final List<JFXExpression> items;
        final Type elemType;
        final Type resultType;

        ExplicitSequenceTranslator(DiagnosticPosition diagPos, List<JFXExpression> items, Type elemType, Type resultType) {
            super(diagPos);
            this.items = items;
            this.elemType = elemType;
            this.resultType = resultType;
        }

        /***
         * In cases where the components of an explicitly constructed
         * sequence are all singletons, we can revert to this (more
         * optimal) implementation.

        DiagnosticPosition diagPos = tree.pos();
        JCExpression meth = ((JavafxTreeMaker)make).at(diagPos).Identifier(sequencesMakeString);
        Type elemType = tree.type.getTypeArguments().get(0);
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        List<JCExpression> typeArgs = List.<JCExpression>of(makeTypeTree(elemType, diagPos));
        // type name .class
        args.append(makeTypeInfo(diagPos, elemType));
        args.appendList( translate( tree.getItems() ) );
        result = make.at(diagPos).Apply(typeArgs, meth, args.toList());
        */
        protected ExpressionResult doit() {
            UseSequenceBuilder builder = useSequenceBuilder(diagPos, elemType, items.length());
            addPreface(builder.makeBuilderVar());
            for (JFXExpression item : items) {
                if (item.getJavaFXKind() != JavaFXKind.NULL_LITERAL) {
                    // Insert all non-null elements
                    addPreface(builder.addElement(item));
                }
            }
            return toResult(
                    builder.makeToSequence(),
                    resultType);
        }
    }

    class SequenceRangeTranslator extends ExpressionTranslator {

        private final JFXExpression lower;
        private final JFXExpression upper;
        private final JFXExpression step;
        private final boolean hasStep;
        private final boolean exclusive;
        private final Type type;

        SequenceRangeTranslator(JFXSequenceRange tree) {
            super(tree.pos());
            this.lower = tree.getLower();
            this.upper = tree.getUpper();
            this.hasStep = tree.getStepOrNull() != null;
            this.step = tree.getStepOrNull();
            this.exclusive = tree.isExclusive();
            this.type = tree.type;
        }

        protected ExpressionResult doit() {
            RuntimeMethod rm = exclusive ? defs.Sequences_rangeExclusive : defs.Sequences_range;
            Type elemType = syms.javafx_IntegerType;
            int ltag = lower.type.tag;
            int utag = upper.type.tag;
            int stag = hasStep ? step.type.tag : TypeTags.INT;
            if (ltag == TypeTags.FLOAT || ltag == TypeTags.DOUBLE ||
                    utag == TypeTags.FLOAT || utag == TypeTags.DOUBLE ||
                    stag == TypeTags.FLOAT || stag == TypeTags.DOUBLE) {
                elemType = syms.javafx_NumberType;
            }
            ListBuffer<JCExpression> args = ListBuffer.lb();
            args.append(translateExpr(lower, elemType));
            args.append(translateExpr(upper, elemType));
            if (hasStep) {
                args.append(translateExpr(step, elemType));
            }
            return toResult(
                    Call(rm, args),
                    type);
        }
    }

    /**
     * assume seq is a sequence of element type U
     * convert   for (x in seq where cond) { body }
     * into the following block expression
     *
     *   {
     *     SequenceBuilder<T> sb = new SequenceBuilder<T>(clazz);
     *     for (U x : seq) {
     *       if (!cond)
     *         continue;
     *       sb.add( { body } );
     *     }
     *     sb.toSequence()
     *   }
     *
     * **/

    class ForExpressionTranslator extends ExpressionTranslator {

        final JFXForExpression tree;

        ForExpressionTranslator(JFXForExpression tree) {
            super(tree.pos());
            this.tree = tree;
        }

        private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
            JCStatement stmt = coreStmt;
            for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
                JFXForExpressionInClause clause = (JFXForExpressionInClause) tree.getInClauses().get(inx);
                stmt = new InClauseTranslator(clause, stmt).doitStmt();
            }
            return stmt;
        }

        protected AbstractStatementsResult doit() {
            // sub-translation in done inline -- no super.visitForExpression(tree);
            if (yield() == ToStatement && targetType == syms.voidType) {
                return new StatementsResult(wrapWithInClause(tree, translateToStatement(tree.getBodyExpression(), targetType)));
            } else {
                // body has value (non-void)
                assert tree.type != syms.voidType : "should be handled above";
                JCStatement stmt;
                JCExpression value;

                // Compute the element type from the sequence type
                assert tree.type.getTypeArguments().size() == 1;
                Type elemType = types.elementType(tree.type);

                UseSequenceBuilder builder = useSequenceBuilder(diagPos, elemType);
                addPreface(builder.makeBuilderVar());

                // Build innermost loop body
                stmt = builder.addElement(tree.getBodyExpression());

                stmt = wrapWithInClause(tree, stmt);
                addPreface(stmt);

                // Build the result value
                value = builder.makeToSequence();
                if (yield() == ToStatement) {
                    return toStatementResult(value, tree.type, targetType);
                } else {
                    // Build the block expression -- which is what we translate to
                    return toResult(
                            convertTranslated(value, diagPos, tree.type, targetType),
                            targetType);
                }
            }
        }
    }

    /**
     * Translator class for for-expression in/where clauses
     */
    private class InClauseTranslator extends ExpressionTranslator {
        final JFXForExpressionInClause clause;      // in clause being translated
        final JFXVar var;                           // user named JavaFX induction variable
        final Type inductionVarType;                // type of the induction variable
        final JCVariableDecl inductionVar;          // generated induction variable
        JCStatement body;                           // statement being generated by wrapping
        boolean indexedLoop;

        InClauseTranslator(JFXForExpressionInClause clause, JCStatement coreStmt) {
            super(clause);
            this.clause = clause;
            this.var = clause.getVar();
            this.inductionVarType = var.type;
            this.body = coreStmt;
            JFXExpression seq = clause.seqExpr;
            this.indexedLoop =
                    (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE ||
                     (seq.getFXTag() != JavafxTag.SEQUENCE_RANGE &&
                      types.isSequence(seq.type)));
            this.inductionVar = MutableTmpVar("ind", indexedLoop ? syms.intType : inductionVarType, null);
        }

        private JCVariableDecl TmpVar(String root, JCExpression value) {
            return TmpVar(root, inductionVarType, value);
        }

        private JCVariableDecl Var(Name varName, JCExpression value) {
            return Var(inductionVarType, varName, value);
        }

        @Override
        protected JCVariableDecl TmpVar(long flags, String root, Type varType, JCExpression initialValue) {
            Name varName = names.fromString(var.name.toString() + "$" + root);
            return Var(flags, varType, varName, initialValue);
        }

        /**
         * Generate a range sequence conditional test.
         * Result depends on if the range is ascending/descending and if the range is exclusive
         */
        private JCExpression condTest(JFXSequenceRange range, boolean stepNegative, JCVariableDecl upperVar) {
            int op;
            if (stepNegative) {
                if (range.isExclusive()) {
                    op = JCTree.GT;
                } else {
                    op = JCTree.GE;
                }
            } else {
                if (range.isExclusive()) {
                    op = JCTree.LT;
                } else {
                    op = JCTree.LE;
                }
            }
            return m().Binary(op, id(inductionVar), id(upperVar));
        }

        /**
         * Determine if a literal is negative
         */
        private boolean isNegative(JFXExpression expr) {
            JFXLiteral lit = (JFXLiteral) expr;
            Object val = lit.getValue();

            switch (lit.typetag) {
                case TypeTags.INT:
                    return ((int) (Integer) val) < 0;
                case TypeTags.SHORT:
                    return ((short) (Short) val) < 0;
                case TypeTags.BYTE:
                    return ((byte) (Byte) val) < 0;
                case TypeTags.CHAR:
                    return ((char) (Character) val) < 0;
                case TypeTags.LONG:
                    return ((long) (Long) val) < 0L;
                case TypeTags.FLOAT:
                    return ((float) (Float) val) < 0.0f;
                case TypeTags.DOUBLE:
                    return ((double) (Double) val) < 0.0;
                default:
                    throw new AssertionError("unexpected literal kind " + this);
            }
        }

        JCStatement makeForLoop(List<JCStatement> init, JCExpression cond, List<JCExpressionStatement> step, JCStatement body) {
            JCStatement loop = m().ForLoop(init, cond, step, body);
            if (clause.label != null) {
                // Wrap in a labeled stmt if the for has a label (that was created because
                // it translated into nested loops, and the body contained a break or continue.)
                loop = m().Labelled(clause.label, loop);
                }
            return loop;
        }

        /**
         * Generate the loop for a slice sequence.  Loop wraps the current body.
         * For the loop:
         *    for (x in seq[lo..<hi]) body
         * Generate:
         *     for (int $i = max(lo,0);  i++; $i < min(hi,seq.size())) { def x = seq[$i]; body }
         * (We may need to put seq and/or hi in a temporary variable first.)
         */
        void translateSliceInClause(JFXExpression seq, JFXExpression first, JFXExpression last, int endKind, JCVariableDecl seqVar) {
            // Collect all the loop initializing statements (variable declarations)
            ListBuffer<JCStatement> tinits = ListBuffer.lb();
            boolean needSeqVar;
            if (! (seq instanceof JFXIdent))
                needSeqVar = true;
            else  {
                Symbol seqsym = ((JFXIdent) seq).sym;
                OnReplaceInfo info = findOnReplaceInfo(seqsym);
                needSeqVar = info == null ||
                        (seqsym.flags_field & JavafxFlags.VARUSE_OPT_TRIGGER) == 0;
            }
            if (needSeqVar)
                tinits.append(seqVar);
            JCExpression init;
            boolean maxForStartNeeded = true;
            if (first == null)
                init = Int(0);
            else {
                init = translateExpr(first, syms.intType);
                if (first.getFXTag() == JavafxTag.LITERAL && ! isNegative(first))
                    maxForStartNeeded = false;
                // FIXME set maxForStartNeeded false if first is replace-trigger startPos and seq is oldValue
                if (maxForStartNeeded)
                    setDiagPos(first);
                    init = Call(defs.Math_max, init, Int(0));
            }
            setDiagPos(clause);
            inductionVar.init = init;
            tinits.append(inductionVar);
            JCExpression sizeExpr = translateSizeof(seq, id(seqVar));
            //call(diagPos, ident(seqVar), "size");
            JCExpression limitExpr;
            // Compare the logic in makeSliceEndPos.
            if (last == null) {
                limitExpr = sizeExpr;
                if (endKind == SequenceSliceTree.END_EXCLUSIVE)
                    limitExpr = MINUS(limitExpr, Int(1));
            }
            else {
                limitExpr = translateExpr(last, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE)
                    limitExpr = PLUS(limitExpr, Int(1));
                // FIXME can optimize if last is replace-trigger endPos and seq is oldValue
                if (true)
                    setDiagPos(last);
                    limitExpr = Call(defs.Math_min, limitExpr, sizeExpr);
            }
            setDiagPos(clause);
            JCVariableDecl limitVar = TmpVar("limit", syms.intType, limitExpr);
            tinits.append(limitVar);
            // The condition that will be tested each time through the loop
            JCExpression tcond = LT(id(inductionVar), id(limitVar));
            // Generate the step statement as: x += 1
            List<JCExpressionStatement> tstep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(inductionVar), m().Literal(TypeTags.INT, 1))));
            tinits.append(makeForLoop(List.<JCStatement>nil(), tcond, tstep, body));
            body = Block(tinits);
        }

        /**
         * Generate the loop for a range sequence.  Loop wraps the current body.
         * For the loop:
         *    for (x in [lo..hi step st]) body
         * Generate (assuming x is float):
         *    for (float x = lo, final float x$upper = up, final float x$step = st;, final boolean x$negative = x$step < 0.0;
         *        x$negative? x >= x$upper : x <= x$upper;
         *        x += x$step)
         *            body
         * Without a step specified (or a literal step) the form reduces to:
         *    for (float x = lo, final float x$upper = up;
         *        x <= x$upper;
         *        x += 1)
         *            body
         */
        void translateRangeInClause() {
            JFXSequenceRange range = (JFXSequenceRange) clause.seqExpr;
            // Collect all the loop initializing statements (variable declarations)
            ListBuffer<JCStatement> tinits = ListBuffer.lb();
            // Set the initial value of the induction variable to be the low end of the range, and add it the the initializing statements
            inductionVar.init = translateExpr(range.getLower(), inductionVarType);
            tinits.append(inductionVar);
            // Record the upper end of the range in a final variable, and add it the the initializing statements
            JCVariableDecl upperVar = TmpVar("upper", translateExpr(range.getUpper(), inductionVarType));
            tinits.append(upperVar);
            // The expression which will be used in increment the induction variable
            JCExpression tstepIncrExpr;
            // The condition that will be tested each time through the loop
            JCExpression tcond;
            // The user's step expression, or null if none specified
            JFXExpression step = range.getStepOrNull();
            if (step != null) {
                // There is a user specified step expression
                JCExpression stepVal = translateExpr(step, inductionVarType);
                if (step.getFXTag() == JavafxTag.LITERAL) {
                    // The step expression is a literal, no need for a variable to hold it, and we can test if the range is scending at compile time
                    tstepIncrExpr = stepVal;
                    tcond = condTest(range, isNegative(step), upperVar);
                } else {
                    // Arbitrary step expression, do all the madness shown in the method comment
                    JCVariableDecl stepVar = TmpVar("step", stepVal);
                    tinits.append(stepVar);
                    tstepIncrExpr = id(stepVar);
                    JCVariableDecl negativeVar = TmpVar("negative", syms.booleanType, LT(id(stepVar), m().Literal(inductionVarType.tag, 0)));
                    tinits.append(negativeVar);
                    tcond = 
                        If(id(negativeVar),
                            condTest(range, true, upperVar),
                            condTest(range, false, upperVar));
                }
            } else {
                // No step expression, use one as the increment
                tstepIncrExpr = m().Literal(inductionVarType.tag, 1);
                tcond = condTest(range, false, upperVar);
            }
            // Generate the step statement as: x += x$step
            List<JCExpressionStatement> tstep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(inductionVar), tstepIncrExpr)));
            // Finally, build the for loop
            body = makeForLoop(tinits.toList(), tcond, tstep, body);
        }

        /**
         * Core of the in-clause translation.  Given an in-clause and the current body, build the for-loop
         */
        protected StatementsResult doit() {
            return new StatementsResult(doitStmt());
        }
        protected JCStatement doitStmt() {
            // If there is a where expression, make the execution of the body conditional on the where condition
            if (clause.getWhereExpression() != null) {
                body = If(translateExpr(clause.getWhereExpression(), syms.booleanType), body);
            }

            // Because the induction variable may be used in inner contexts, make a final
            // variable inside the loop that holds the current iterations value.
            // Same with the index if used.   That is:
            //   for (x in seq) body
            // Becomes (assume Number sequence):
            //   x$incrindex = 0;
            //   loop over x$ind {
            //     final int x$index = x$incrindex++;
            //     final float x = x$ind;
            //     body;
            //   }
            JCVariableDecl incrementingIndexVar = null;
            JFXExpression seq = clause.seqExpr;
            JCVariableDecl seqVar = null;
            {
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                setDiagPos(var);
                if (clause.getIndexUsed()) {
                    incrementingIndexVar = MutableTmpVar("incrindex", syms.javafx_IntegerType, Int(0));
                    JCVariableDecl finalIndexVar = Var(

                            syms.javafx_IntegerType,
                            indexVarName(clause),m().Unary(JCTree.POSTINC, id(incrementingIndexVar)));
                    stmts.append(finalIndexVar);
                }
                JCExpression varInit;     // Initializer for var.

                if (indexedLoop) {
                    JFXExpression sseq;
                    if (clause.seqExpr instanceof JFXSequenceSlice) {
                        sseq = ((JFXSequenceSlice) clause.seqExpr).getSequence();
                    } else {
                        sseq = clause.seqExpr;
                    }
                    seqVar = TmpVar("seq", seq.type, asExpression(translateToExpressionResult(sseq, seq.type), seq.type));
                    varInit = new SequenceIndexedTranslator(diagPos, sseq, id(seqVar), id(inductionVar), inductionVarType).doitExpr();
                } else {
                    varInit = id(inductionVar);
                }

                stmts.append(Var(var.getName(), varInit));
                stmts.append(body);
                body = Block(stmts);
            }

            // Translate the sequence into the loop
            setDiagPos(seq);
            if (seq.getFXTag() == JavafxTag.SEQUENCE_RANGE) {
                // Iterating over a range sequence
                translateRangeInClause();
            } else if (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
                JFXSequenceSlice slice = (JFXSequenceSlice) clause.seqExpr;
                translateSliceInClause(slice.getSequence(), slice.getFirstIndex(), slice.getLastIndex(),
                        slice.getEndKind(), seqVar);
            } else {
                // We will be using the sequence as a whole, so translate it
                JCExpression tseq = asExpression(translateToExpressionResult(seq, null), null);
                if (types.isSequence(seq.type)) {
                    // Iterating over a non-range sequence, use a foreach loop, but first convert null to an empty sequence
                    tseq = Call(defs.Sequences_forceNonNull,
                            TypeInfo(diagPos, inductionVarType), tseq);
                    translateSliceInClause(seq, null, null, SequenceSliceTree.END_INCLUSIVE, seqVar);
                    //body = m().ForeachLoop(inductionVar, tseq, body);
                } else if (seq.type.tag == TypeTags.ARRAY ||
                             types.asSuper(seq.type, syms.iterableType.tsym) != null) {
                    // Iterating over an array or iterable type, use a foreach loop
                    body = m().ForeachLoop(inductionVar, tseq, body);
                } else {
                    // The "sequence" isn't aactually a sequence, treat it as a singleton.
                    // Compile: { var tmp = seq; if (tmp!=null) body; }
                    if (!inductionVarType.isPrimitive()) {
                        body = If (NEnull(id(inductionVar)),
                                body);
                    }
                    // the "induction" variable will have only one value, set it to that
                    inductionVar.init = tseq;
                    // wrap the induction variable and the body in a block to protect scope
                    body = Block(inductionVar, body);
                }
            }

            if (clause.getIndexUsed()) {
                // indexof is used, define the index counter variable at the top of everything
                body = Block(incrementingIndexVar, body);
            }

            return body;
        }
    }

    /**
     * Translate to a built-in construct
     */
    abstract class NewBuiltInInstanceTranslator extends NewInstanceTranslator {

        protected final Type builtIn;

        NewBuiltInInstanceTranslator(DiagnosticPosition diagPos, Type builtIn) {
            super(diagPos);
            this.builtIn = builtIn;
        }

        /**
         * Arguments for the constructor.
         * There are no user arguments to built-in class constructors.
         * Just generate the default init 'true' flag for JavaFX generated constructors.
         */
        protected List<JCExpression> completeTranslatedConstructorArgs() {
            return List.<JCExpression>nil();
        }

        VarSymbol varSym(Name varName) {
            return (VarSymbol) builtIn.tsym.members().lookup(varName).sym;
        }

        void setInstanceVariable(Name instName, Name varName, JFXExpression init) {
            VarSymbol vsym = varSym(varName);
            setInstanceVariable(instName, JavafxBindStatus.UNBOUND, vsym, init);
        }

        @Override
        protected ExpressionResult doit() {
            return buildInstance(builtIn, null, true);
        }
    }

    class VarInitTranslator extends ExpressionTranslator {

        private final VarSymbol vsym;

        VarInitTranslator(JFXVarInit tree) {
            super(tree.pos());
            this.vsym = tree.getSymbol();
        }

        ExpressionResult doit() {
            JCExpression receiver = vsym.isStatic() ? Call(scriptLevelAccessMethod(vsym.owner)) : null; //TODO: this probably needs to be updated
            return toResult(
                    BlockExpression(
                        CallStmt(receiver, defs.applyDefaults_FXObjectMethodName, Offset(vsym)),
                        Get(vsym)
                    ),
                    vsym.type);
        }
    }

    /***********************************************************************
     * Bad visitor support
     */

    void badVisitor(String msg) {
        throw new AssertionError(msg);
    }

    private void disallowedInBind() {
        badVisitor("should not be processed as part of a binding");
    }

    private void processedInParent() {
        badVisitor("should be processed by parent tree");
    }


    /***********************************************************************
     *
     * Visitors  (alphabetical order)
     * 
     * Disallow constructs disallowed in bind -- override where non-bound contructs are allowed (JavafxToJava)
     * Assume non-bound non-notifying implementations -- override where needed
     */

    public void visitAssign(JFXAssign tree) {
        disallowedInBind();
    }

    public void visitAssignop(JFXAssignOp tree) {
        disallowedInBind();
    }

    public void visitBinary(JFXBinary tree) {
        result = (new BinaryOperationTranslator(tree.pos(), tree)).doit();
    }

    public void visitBreak(JFXBreak tree) {
        disallowedInBind();
    }

    public void visitCatch(JFXCatch tree) {
        processedInParent();
    }

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        // redirected back to JavafxToJava
        toJava.visitClassDeclaration(tree);
        result = toJava.result;
    }

    public void visitContinue(JFXContinue tree) {
        disallowedInBind();
    }

    public void visitErroneous(JFXErroneous tree) {
        badVisitor("erroneous nodes shouldn't have gotten this far");
    }

    public void visitForExpression(JFXForExpression tree) {
        result = (new ForExpressionTranslator(tree)).doit();
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        processedInParent();
    }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        disallowedInBind();
    }

    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        result = new FunctionCallTranslator(tree).doit();
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = new FunctionValueTranslator(make.Ident(defs.lambda_MethodName), def, tree.pos(), (MethodType) def.type, tree.type).doit();
    }

    public abstract void visitIdent(JFXIdent tree);

    public void visitIfExpression(JFXIfExpression tree) {
        result = new IfTranslator(tree).doit();
    }

    public void visitImport(JFXImport tree) {
        processedInParent();
    }

    public void visitIndexof(final JFXIndexof tree) {
        result = new IndexOfTranslator(tree).doit();
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        processedInParent();
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        result = new InstanceOfTranslator(tree).doit();
    }

    public void visitInstanciate(JFXInstanciate tree) {
        result = new InstanciateTranslator(tree).doit();
    }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        throw new AssertionError("KeyFrame should have been lowered");
    }

    public void visitInvalidate(JFXInvalidate tree) {
        disallowedInBind();
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        disallowedInBind();
    }

    public void visitLiteral(JFXLiteral tree) {
         result = new LiteralTranslator(tree).doit();
    }

   public void visitModifiers(JFXModifiers tree) {
        processedInParent();
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        processedInParent();
    }

    public void visitOnReplace(JFXOnReplace tree) {
        processedInParent();
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        processedInParent();
    }

    public void visitParens(JFXParens tree) {
        result = translateToExpressionResult(tree.expr, targetType);
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        processedInParent();
    }

    public void visitReturn(JFXReturn tree) {
        disallowedInBind();
    }

    public void visitScript(JFXScript tree) {
        disallowedInBind();
    }

    public void visitSequenceDelete(JFXSequenceDelete tree) {
        disallowedInBind();
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = new SequenceEmptyTranslator(tree).doit();
    }

    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        result = new ExplicitSequenceTranslator(
                tree.pos(),
                tree.getItems(),
                types.elementType(tree.type),
                tree.type)
             .doit();
    }

    public void visitSequenceIndexed(final JFXSequenceIndexed tree) {
        result = new SequenceIndexedTranslator(tree).doit();
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        disallowedInBind();
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        result = new SequenceRangeTranslator(tree).doit();
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        result = new SequenceSliceTranslator(tree).doit();
    }

    public void visitSkip(JFXSkip tree) {
        disallowedInBind();
    }

    public void visitStringExpression(JFXStringExpression tree) {
        result = new StringExpressionTranslator(tree).doit();
    }

    public void visitThrow(JFXThrow tree) {
        disallowedInBind();
    }

    public void visitTimeLiteral(final JFXTimeLiteral tree) {
        result = new TimeLiteralTranslator(tree).doit();
   }

    public void visitTree(JFXTree that) {
        badVisitor("Should not be here!!!");
    }

    public void visitTry(JFXTry tree) {
        disallowedInBind();
    }

    public void visitTypeAny(JFXTypeAny that) {
        processedInParent();
    }

    public void visitTypeCast(final JFXTypeCast tree) {
        result = new TypeCastTranslator(tree).doit();
    }

    public void visitTypeClass(JFXTypeClass that) {
        processedInParent();
    }

    public void visitTypeFunctional(JFXTypeFunctional that) {
        processedInParent();
    }

    public void visitTypeArray(JFXTypeArray tree) {
        processedInParent();
    }

    public void visitTypeUnknown(JFXTypeUnknown that) {
        processedInParent();
    }

    public void visitUnary(JFXUnary tree) {
        if (tree.getFXTag().isIncDec()) {
            //we shouldn't be here - arithmetic unary expressions should
            //have been lowered to standard binary expressions
            badVisitor("Unexpected unary operator tag: " + tree.getFXTag());
        }
        result = new UnaryOperationTranslator(tree).doit();
    }

    public void visitVar(JFXVar tree) {
        disallowedInBind();
    }

    public void visitVarInit(JFXVarInit tree) {
        result = new VarInitTranslator(tree).doit();
    }

    public void visitWhileLoop(JFXWhileLoop tree) {
        disallowedInBind();
    }
}
