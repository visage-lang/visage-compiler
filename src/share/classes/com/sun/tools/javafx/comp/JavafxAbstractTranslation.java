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
        this.toJava = toJava;  //TODO: temp hack
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

    /********** Utility routines **********/

    public static class NotYetImplementedException extends RuntimeException {
        NotYetImplementedException(String msg) {
            super(msg);
        }
    }

    JCExpression TODO(String msg) {
        throw new NotYetImplementedException("Not yet implemented: " + msg);
    }

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

    /**
     * Return the list of local variables accessed, but not defined within the FX expression.
     * @param expr
     * @return
     */
    List<VarSymbol> localVars(JFXTree expr) {
        final ListBuffer<VarSymbol> lb = ListBuffer.<VarSymbol>lb();
        final Set<VarSymbol> exclude = new HashSet<VarSymbol>();
        new JavafxTreeScanner() {
            @Override
            public void visitVar(JFXVar tree) {
                exclude.add(tree.sym);
                super.visitVar(tree);
            }
        }.scan(expr);

        new JavafxTreeScanner() {
            @Override
            public void visitIdent(JFXIdent tree) {
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol vsym = (VarSymbol) (tree.sym);
                    if (vsym.owner.kind != Kinds.TYP && !exclude.contains(vsym)) {
                        // Local variable we haven't seen before, include it
                        lb.append(vsym);
                        exclude.add(vsym);
                    }
                }
            }
        }.scan(expr);

        return lb.toList();
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
                res.bindees,
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
                    return makeDefaultValue(diagPos, outType);
                }

                if (!types.isSameType(inType, outType) || isValueFromJava(inExpr)) {
                    JCVariableDecl daVar = makeTmpVar(outType, expr);
                    JCExpression toTest = id(daVar.name);
                    JCExpression cond = makeNotNullCheck(toTest);
                    JCExpression ret = m().Conditional(
                            cond,
                            id(daVar.name),
                            makeDefaultValue(diagPos, outType));
                    return makeBlockExpression(List.<JCStatement>of(daVar), ret);
                }
                return expr;
            }
        }).doitExpr();
    }

   JCExpression castFromObject (JCExpression arg, Type castType) {
       return make.TypeCast(makeType(arg.pos(), types.boxedTypeOrType(castType)), arg);
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
        List<JCStatement> statements() {
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

    public static class ExpressionResult extends AbstractStatementsResult {
        public static class DependentPair {
            public final VarSymbol instanceSym;
            public final Symbol referencedSym;
            DependentPair(VarSymbol instanceSym, Symbol referencedSym) {
                this.instanceSym = instanceSym;
                this.referencedSym = referencedSym;
            }
        }
        private final JCExpression value;
        private final List<VarSymbol> bindees;
        private final List<DependentPair> interClass;
        private final Type resultType;

        ExpressionResult(DiagnosticPosition diagPos, List<JCStatement> stmts, JCExpression value, List<VarSymbol> bindees, List<DependentPair> interClass, Type resultType) {
            super(diagPos, stmts);
            this.value = value;
            this.bindees = bindees;
            this.interClass = interClass;
            this.resultType = resultType;
        }
        ExpressionResult(DiagnosticPosition diagPos, ListBuffer<JCStatement> buf, JCExpression value, ListBuffer<VarSymbol> bindees, ListBuffer<DependentPair> interClass, Type resultType) {
            this(diagPos, buf.toList(), value, bindees.toList(), interClass.toList(), resultType);
        }
        ExpressionResult(JCExpression value, List<VarSymbol> bindees, List<DependentPair> interClass, Type resultType) {
            this(value.pos(), List.<JCStatement>nil(), value, bindees, interClass, resultType);
        }
        ExpressionResult(JCExpression value, Type resultType) {
            this(value, List.<VarSymbol>nil(), List.<DependentPair>nil(), resultType);
        }
        JCExpression expr() {
            return value;
        }
        List<VarSymbol> bindees() {
            return bindees;
        }
        List<DependentPair> interClass() {
            return interClass;
        }
        @Override
        List<JCTree> trees() {
            List<JCTree> ts = super.trees();
            return value==null? ts : ts.append(value);
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
            return tree==null? List.<JCTree>nil() : List.<JCTree>of(tree);
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
            return convertTranslated(ret, expr.pos(), targettedType);
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
            super(res.diagPos);
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
                    stmts = stmts.append(makeStatement(convertedExpression(eres), type));
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
            List<JCStatement> stmts = asStatements();
            return m().Block(0L, stmts);
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

    JCExpression translateLiteral(JFXLiteral tree) {
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = types.boxedElementType(tree.type);
            JCExpression expr = accessEmptySequence(tree.pos(), elemType);
            return castFromObject(expr, syms.javafx_SequenceTypeErasure);
        } else {
            return make.at(tree.pos).Literal(tree.typetag, tree.value);
        }
    }

    /** Translate a single tree.
     */
    SpecialResult translateToSpecialResult(JFXTree tree) {
        SpecialResult ret;

        if (tree == null) {
            ret = null;
        } else {
            JFXTree prevWhere = getAttrEnv().where;
            getAttrEnv().where = tree;
            tree.accept(this);
            getAttrEnv().where = prevWhere;
            ret = (SpecialResult)this.result;
            this.result = null;
        }
        return ret;
    }

    /********** Translators **********/

    abstract class Translator extends JavaTreeBuilder {

        Translator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        abstract Result doit();

        JavafxTreeMaker fxm() {
            return fxmake.at(diagPos);
        }

        JCVariableDecl convertParam(JFXVar param) {
            return makeParam(param.type, param.name);
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
            final JCExpression thisExpr = select(makeType(siteOwner.type), names._this);
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
                            ret = call(ret, defs.outerAccessorName);
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
        private final ListBuffer<VarSymbol> bindees = ListBuffer.lb();
        private final ListBuffer<ExpressionResult.DependentPair> interClass = ListBuffer.lb();

        ExpressionTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        JCExpression translateExpr(JFXExpression expr, Type type) {
            ExpressionResult res = translateToExpressionResult(expr, type);
            stmts.appendList(res.statements());
            bindees.appendList(res.bindees());
            interClass.appendList(res.interClass());
            return res.expr();
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
            StatementsResult res = translateToStatementsResult(expr, targettedType);
            stmts.appendList(res.statements());
        }

        void addPreface(JCStatement stmt) {
            stmts.append(stmt);
        }

        void addPreface(List<JCStatement> list) {
            stmts.appendList(list);
        }

        void addBindee(VarSymbol sym) {
            if (!bindees.contains(sym)) {
                bindees.append(sym);
            }
        }

        void addBindees(List<VarSymbol> syms) {
            for (VarSymbol sym : syms) {
                addBindee(sym);
            }
        }

        void addInterClassBindee(VarSymbol instanceSym, Symbol referencedSym) {
            interClass.append(new ExpressionResult.DependentPair( instanceSym,  referencedSym));
        }

        void addInterClassBindees(List<ExpressionResult.DependentPair> pairs) {
            for (ExpressionResult.DependentPair pair : pairs) {
                interClass.append(pair);
            }
        }

        ExpressionResult toResult(JCExpression translated, Type resultType) {
            return new ExpressionResult(diagPos, stmts, translated, bindees, interClass, resultType);
        }

        StatementsResult toStatementResult(JCExpression translated, Type resultType, Type targettedType) {
            return toStatementResult(
                    (targettedType == null || targettedType == syms.voidType) ?
                          makeExec(translated)
                        : makeReturn(
                            convertTranslated(translated, diagPos, resultType, targettedType)));
        }

        StatementsResult toStatementResult(JCStatement translated) {
            assert bindees.length() == 0;
            return new StatementsResult(diagPos, stmts.append(translated));
        }

        List<JCStatement> statements() {
            return stmts.toList();
        }

        List<VarSymbol> bindees() {
            return bindees.toList();
        }

        List<ExpressionResult.DependentPair> interClass() {
            return interClass.toList();
        }

        abstract protected AbstractStatementsResult doit();
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
                    texp = call(translateExpr(exp, syms.javafx_DurationType), names.fromString("toMillis"));
                    texp = typeCast(diagPos, syms.javafx_LongType, syms.javafx_DoubleType, texp);
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
            JCLiteral formatLiteral = m().Literal(TypeTags.CLASS, sb.toString());
            values.prepend(formatLiteral);
            String formatMethod;
            if (tree.translationKey != null) {
                formatMethod = "com.sun.javafx.runtime.util.StringLocalization.getLocalizedString";
                if (tree.translationKey.length() == 0) {
                    values.prepend(makeNull());
                } else {
                    values.prepend(m().Literal(TypeTags.CLASS, tree.translationKey));
                }
                String resourceName =
                       currentClass().sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
                values.prepend(m().Literal(TypeTags.CLASS, resourceName));
            } else if (containsDateTimeFormat) {
                formatMethod = "com.sun.javafx.runtime.util.FXFormatter.sprintf";
            } else {
                formatMethod = "java.lang.String.format";
            }
            JCExpression formatter = makeQualifiedTree(formatMethod);
            return toResult(m().Apply(null, formatter, values.toList()), syms.stringType);
        }
    }

    abstract class MemberReferenceTranslator extends ExpressionTranslator {

        protected MemberReferenceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        JCExpression staticReference(Symbol sym) {
            Symbol owner = sym.owner;
            Symbol encl = currentClass().sym;
            if (encl.name.endsWith(defs.scriptClassSuffixName) && owner == encl.owner) {
                return null;
            } else {
                //TODO: see init builder getStaticContext() for a better implementation
                Type classType = types.erasure(owner.type);
                JCExpression expr = makeType(classType, false);
                if (types.isJFXClass(owner)) {
                    Name simpleName;
                    switch (expr.getTag()) {
                        case JCTree.IDENT:
                            simpleName = ((JCIdent)expr).name;
                            break;
                       case JCTree.SELECT:
                            simpleName = ((JCFieldAccess)expr).name;
                            break;
                        default:
                            throw new RuntimeException("should not get here -- type name should be identifier or select");
                    }

                    // make X.X$Script
                    if (!JavafxInitializationBuilder.SCRIPT_LEVEL_AT_TOP) {
                        expr = select(expr, simpleName.append(defs.scriptClassSuffixName));
                    }
                }
                return expr;
            }
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
                    expr = call(instance, attributeGetterName(vsym));
                }
            }

            return expr;
        }

    }

    abstract class NullCheckTranslator extends MemberReferenceTranslator {

        protected final Symbol refSym;
        protected final Type fullType;
        protected final Type resultType;
        protected final boolean staticReference;

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
            JCVariableDecl tmpVar = makeTmpVar("pse", varType, trans);
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
                    return makeReceiver(refSym);
                }
                return null;
            }
            Symbol selectorSym = expressionSymbol(expr);
            // If this is OuterClass.memberName or MixinClass.memberName, then
            // we want to create expression to get the proper receiver.
            if (selectorSym != null && selectorSym.kind == Kinds.TYP) {
                return makeReceiver(refSym);
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
            if (!needNullCheck()) {
                // no null check needed just return the translation
                return toResult(full, resultType);
            }
            // Make an expression to use in null test.
            // If translated toCheck is an identifier (tmp var or not), just make a new identifier.
            // Otherwise, retranslate.
            JCExpression toTest = (tToCheck instanceof JCIdent) ?
                  id(((JCIdent)tToCheck).name)
                : translateToCheck(getToCheck());

            // Do a null check
            // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
            JCExpression cond = makeNotNullCheck(toTest);
            JCExpression defaultExpr = makeDefaultValue(diagPos, resultType);
            if (yield() == ToStatement) {
                 // a statement is the desired result of the translation, return the If-statement
                JCStatement nullAction = null;
                if (resultType != null && resultType != syms.voidType) {
                    nullAction = makeStatement(defaultExpr, resultType);
                }
                return toStatementResult(
                        m().If(cond, makeStatement(full, resultType), nullAction));
            } else {
                // an expression is the desired result of the translation, convert it to a conditional expression
                // if it would dereference null, then the full expression instead yields the default value
                return toResult(
                        m().Conditional(cond, full, defaultExpr),
                        resultType);
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
                JCExpression translated = select(tToCheck, name);
                return new FunctionValueTranslator(translated, null, diagPos, mtype, fullType).doitExpr();
            } else {
                JCExpression translated = select(tToCheck, name);
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
        protected final boolean magicHasAnInitializerFunction;
        
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
            magicHasAnInitializerFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_HAS_AN_INITIALIZER) != 0;
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
            JCExpression tMeth = select(mungedToCheckTranslated, methodName());
            JCMethodInvocation app = m().Apply(translateExprs(typeargs), tMeth, determineArgs());

            JCExpression full = callBound ? makeBoundCall(app) : app;
            if (useInvoke) {
                if (resultType != syms.voidType) {
                    full = castFromObject(full, resultType);
                }
            }
            return full;
        }

        Name methodName() {
            return useInvoke? defs.invokeName : functionName(msym, superToStatic, callBound);
        }

        // This is for calls from non-bound contexts (code for true bound calls is in JavafxToBound)
        JCExpression makeBoundCall(JCExpression applyExpression) {
            return TODO("makeBoundCall");
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
                //TODO: this code looks completely messed-up
                /**
                 * If this is a bound call, use left-hand side references for arguments consisting
                 * solely of a  var or attribute reference, or function call, otherwise, wrap it
                 * in an expression location
                 */
                List<Type> formal = formals;
                for (JFXExpression arg : args) {
                    switch (arg.getFXTag()) {
                        case IDENT:
                        case SELECT:
                        case APPLY:
                            // This arg expression is one that will translate into a Location;
                            // since that is needed for a this into Location, do so.
                            // However, if the types need to by changed (subclass), this won't
                            // directly work.
                            // Also, if this is a mismatched sequence type, we will need
                            // to do some different
                            //TODO: never actually gets here
                            if (arg.type.equals(formal.head) ||
                                    types.isSequence(formal.head) ||
                                    formal.head == syms.objectType // don't add conversion for parameter type of java.lang.Object: doing so breaks the Pointer trick to obtain the original location (JFC-826)
                                    ) {
                                throw new RuntimeException("bogus bound call code");
                                //break;
                            }
                        //TODO: handle sequence subclasses
                        //TODO: use more efficient mechanism (use currently apears rare)
                        //System.err.println("Not match: " + arg.type + " vs " + formal.head);
                        // Otherwise, fall-through, presumably a conversion will work.
                        default: {
                            targs.append(translateExpr(arg, arg.type));
                        }
                    }
                    formal = formal.tail;
                }
            } else {
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
                    JCExpression targ;
                    if (magicIsInitializedFunction) {
                        //TODO: in theory, this could have side-effects (but only in theory)
                        //TODO: Lombard
                        targ = translateExpr(arg, formal);
                    } else {
                        targ = translateArg(arg, formal);
                    }
                    targs.append(targ);
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
                    makeDurationLiteral(diagPos, translateExpr(value, syms.doubleType)),
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
                block.stats = block.stats.append(makeReturn(makeNull()));
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
                params.prepend(makeReceiverParam(currentClass()));
            }
            for (JFXVar fxVar : tree.getParams()) {
                params.append(convertParam(fxVar));
            }
            return params.toList();
        }

        private JCBlock methodBody() {
            // construct the body of the translated function
            JFXBlock bexpr = tree.getBodyExpression();
            JCBlock body;
            if (bexpr == null) {
                body = null; // null if no block expression
            } else if (isBound) {
                TODO("bound function building"); body = null;
            } else if (isRunMethod) {
                // it is a module level run method, do special translation
                body = makeRunMethodBody(bexpr);
            } else {
                // the "normal" case
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                for (JFXVar fxVar : tree.getParams()) {
                    if (types.isSequence(fxVar.sym.type)) {
                        setDiagPos(fxVar);
                        stmts.append(callStmt(id(fxVar.getName()), defs.incrementSharingMethodName));
                    }
                }
                setDiagPos(bexpr);
                stmts.appendList(translateToStatementsResult(bexpr, mtype.getReturnType()).statements());
                body = m().Block(0L, stmts.toList());
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
                return makeReceiver(sym, false);
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
                convert = select(staticReference(sym),tree.getName());
            } else {
                if ((kind == Kinds.VAR || kind == Kinds.MTH) &&
                        sym.owner.kind == Kinds.TYP) {
                    // it is a non-static attribute or function class member
                    // reference it through the receiver
                    convert = select(makeReceiver(sym),tree.getName());
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
                return call(tToCheck, defs.scriptLevelAccessMethod);
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
                JCVariableDecl tv = makeTmpVar(types.boxedTypeOrType(rhsType()), buildRHS(rhsTranslated));
                args.append(id(tv));
                JCExpression tIndex = translateIndex();
                if (tIndex != null) {
                    args.append(tIndex);
                }
                JCExpression res = call(meth, args);
                return makeBlockExpression(List.<JCStatement>of(tv, makeExec(m().Assign(id(refSym.name), res))), id(tv));
            } else {
                // Instance variable sequence -- roughly:
                // sequenceAction(instance, varNum, rhs);
                args.append(instance(tToCheck));
                args.append(makeVarOffset(refSym, expressionSymbol(selector)));
                args.append(buildRHS(rhsTranslated));
                JCExpression tIndex = translateIndex();
                if (tIndex != null) {
                    args.append(tIndex);
                }
                return call(meth, args);
            }
        }

        JCExpression makeSliceEndPos(JFXSequenceSlice tree) {
            JCExpression endPos;
            if (tree.getLastIndex() == null) {
                endPos = call(
                        translateExpr(tree.getSequence(), null),
                        defs.sizeMethodName);
                if (tree.getEndKind() == SequenceSliceTree.END_EXCLUSIVE) {
                    endPos = make.at(tree).Binary(JCTree.MINUS,
                            endPos, make.Literal(TypeTags.INT, 1));
                }
            } else {
                endPos = translateExpr(tree.getLastIndex(), syms.intType);
                if (tree.getEndKind() == SequenceSliceTree.END_INCLUSIVE) {
                    endPos = makeBinary(JCTree.PLUS, endPos, makeInt(1));
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
                    return defaultFullExpression(translateExpr(ref, null), rhsTranslated);
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
            return call(tc, attributeSetterName(refSym), rhsComplete);
        }

        JCExpression buildGetter(JCExpression tc) {
            return call(tc, attributeGetterName(refSym));
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

        private AbstractStatementsResult doIncDec(final int binaryOp, final boolean postfix) {
            return (AbstractStatementsResult) new AssignTranslator(diagPos, expr, fxm().Literal(1)) {

                private JCExpression castIfNeeded(JCExpression transExpr) {
                    int ttag = expr.type.tag;
                    if (ttag == TypeTags.BYTE || ttag == TypeTags.SHORT) {
                        return m().TypeCast(expr.type, transExpr);
                    }
                    return transExpr;
                }

                @Override
                JCExpression buildRHS(JCExpression rhsTranslated) {
                    return castIfNeeded(makeBinary(binaryOp, transExpr, rhsTranslated));
                }

                @Override
                JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
                    return m().Unary(tree.getOperatorTag(), lhsTranslated);
                }

                @Override
                protected JCExpression postProcessExpression(JCExpression built) {
                    if (postfix) {
                        // this is a postfix operation, undo the value (not the variable) change
                        return castIfNeeded(makeBinary(binaryOp, (JCExpression) built, m().Literal(-1)));
                    } else {
                        // prefix operation
                        return built;
                    }
                }
            }.doit();
        }

        protected AbstractStatementsResult doit() {
            switch (tree.getFXTag()) {
                case SIZEOF:
                    if (expr.type.tag == TypeTags.ARRAY) {
                        return toResult(select(transExpr, defs.lengthName), syms.intType);
                    }
                    return toResult(call(defs.Sequences_size, transExpr), syms.intType);
                case REVERSE:
                    if (types.isSequence(expr.type)) {
                        // call runtime reverse of a sequence
                        return toResult(
                             call(syms.javafx_SequencesType, "reverse", transExpr),
                             expr.type);
                    } else {
                        // this isn't a sequence, just make it a sequence
                        return toResult(convertTranslated(transExpr, diagPos, expr.type, targetType), targetType);
                    }
                case PREINC:
                    return doIncDec(JCTree.PLUS, false);
                case PREDEC:
                    return doIncDec(JCTree.MINUS, false);
                case POSTINC:
                    return doIncDec(JCTree.PLUS, true);
                case POSTDEC:
                    return doIncDec(JCTree.MINUS, true);
                case NEG:
                    if (types.isSameType(tree.type, syms.javafx_DurationType)) {
                        return toResult(
                                call(translateExpr(tree.arg, tree.arg.type), names.fromString("negate")),
                                syms.javafx_DurationType);
                    }
                default:
                    return toResult(
                            makeUnary(tree.getOperatorTag(), transExpr),
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
            return makeEqual(arg, defaultValue);
        }

        /**
         * Check if a non-primitive has the default value for its type.
         */
        private JCExpression makeObjectNullCheck(Type argType, JCExpression arg) {
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
            if (tmi.isSequence() || tmi.getRealType() == syms.javafx_StringType) {
                return call(defs.Checks_isNull, arg);
            } else {
                return makeNullCheck(arg);
            }
        }

        /**
         * Make a .equals() comparison with a null check on the receiver
         */
        private JCExpression makeFullCheck(JCExpression lhs, JCExpression rhs) {
            return call(defs.Checks_equals, lhs, rhs);
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
                    return m().Literal(TypeTags.BOOLEAN, 1);
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
                    return makeEqual(lhs(req), rhs(req));
                } else {
                    // lhs is primitive, rhs is non-primitive, use equals(), but switch them
                    JCVariableDecl sl = makeTmpVar(req!=null? req : lhsType, lhs(req));  // eval first to keep the order correct
                    return makeBlockExpression(List.<JCStatement>of(sl), makeFullCheck(rhs(req), id(sl.name)));
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
            return call(leftSide, methodName, rightSide);
        }

        boolean isDuration(Type type) {
            return types.isSameType(type, syms.javafx_DurationType);
        }

        final Type durationNumericType = syms.javafx_NumberType;

        JCExpression durationOp() {
            switch (tree.getFXTag()) {
                case PLUS:
                    return op(lhs(), defs.durOpAdd, rhs());
                case MINUS:
                    return op(lhs(), defs.durOpSub, rhs());
                case DIV:
                    return op(lhs(), defs.durOpDiv, rhs(isDuration(rhsType)? null : durationNumericType));
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
                    return op(rcvr, defs.durOpMul, arg);
                }
                case LT:
                    return op(lhs(), defs.durOpLT, rhs());
                case LE:
                    return op(lhs(), defs.durOpLE, rhs());
                case GT:
                    return op(lhs(), defs.durOpGT, rhs());
                case GE:
                    return op(lhs(), defs.durOpGE, rhs());
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
                return makeNot(translateEqualsEquals());
            } else {
                // anything other than == or !=

                // Duration type operator overloading
                if ((isDuration(lhsType) || isDuration(rhsType)) &&
                        tree.operator == null) { // operator check is to try to get a decent error message by falling through if the Duration method isn't matched
                    return durationOp();
                }
                return makeBinary(tree.getOperatorTag(), lhs(), rhs());
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
            JCExpression inTypeInfo = makeTypeInfo(diagPos, inElementType);
            JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
            return runtime(diagPos,
                    defs.Sequences_convertNumberSequence,
                    List.of(targetTypeInfo, inTypeInfo, expr));
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
                        String mname;
                        if (elemType == syms.byteType) {
                            mname = "toByteArray";
                        } else if (elemType == syms.shortType) {
                            mname = "toShortArray";
                        } else if (elemType == syms.intType) {
                            mname = "toIntArray";
                        } else if (elemType == syms.longType) {
                            mname = "toLongArray";
                        } else if (elemType == syms.floatType) {
                            mname = "toFloatArray";
                        } else if (elemType == syms.doubleType) {
                            mname = "toDoubleArray";
                        } else if (elemType == syms.booleanType) {
                            mname = "toBooleanArray";
                        } else {
                            mname = "toArray";
                        }
                        return call(makeType(syms.javafx_SequencesType, false),
                                mname, translated);
                    }
                    ListBuffer<JCStatement> stats = ListBuffer.lb();
                    JCVariableDecl tmpVar = makeTmpVar(sourceType, translated);
                    stats.append(tmpVar);
                    JCVariableDecl sizeVar = makeTmpVar(syms.intType, call(id(tmpVar.name), "size"));
                    stats.append(sizeVar);
                    JCVariableDecl arrVar = makeTmpVar("arr", targettedType, make.at(diagPos).NewArray(
                            makeType(elemType, true),
                            List.<JCExpression>of(id(sizeVar.name)),
                            null));
                    stats.append(arrVar);
                    stats.append(callStmt(id(tmpVar.name), "toArray", List.of(
                            makeInt(0),
                            id(sizeVar),
                            id(arrVar),
                            makeInt(0))));
                    return makeBlockExpression(stats, id(arrVar));
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
                    args = List.of(makeTypeInfo(diagPos, sourceElemType), translated);
                }
                JCExpression cSequences = makeType(syms.javafx_SequencesType, false);
                return call(cSequences, "fromArray", args);
            }
            if (targetIsSequence && !sourceIsSequence) {
                //if (sourceType.tag == TypeTags.BOT) {
                //    // it is a null, convert to empty sequence
                //    //TODO: should we leave this null?
                //    Type elemType = types.elemtype(type);
                //    return makeEmptySequenceCreator(diagPos, elemType);
                //}
                Type targetElemType = types.elementType(targettedType);
                JCExpression cSequences = makeType(syms.javafx_SequencesType, false);
                JCExpression expr = convertTranslated(translated, diagPos, sourceType, targetElemType);
                
                // This would be redundant, if convertTranslated did a cast if needed.
                expr = makeTypeCast(diagPos, targetElemType, sourceType, expr);
                return call(
                        cSequences,
                        "singleton",
                        makeTypeInfo(diagPos, targetElemType), expr);
            }
            if (targetIsSequence && sourceIsSequence) {
                Type sourceElementType = types.elementType(sourceType);
                Type targetElementType = types.elementType(targettedType);
                if (!types.isSameType(sourceElementType, targetElementType) &&
                        types.isNumeric(sourceElementType) && types.isNumeric(targetElementType)) {
                    return convertNumericSequence(diagPos,
                            translated,
                            sourceElementType,
                            targetElementType);
                }
            }

            // Convert primitive/Object types
            Type unboxedTargetType = targettedType.isPrimitive() ? targettedType : types.unboxedType(targettedType);
            Type unboxedSourceType = sourceType.isPrimitive() ? sourceType : types.unboxedType(sourceType);
            JCExpression res = translated;
            Type curType = sourceType;
            if (unboxedTargetType != Type.noType && unboxedSourceType != Type.noType) {
                // (boxed or unboxed) primitive to (boxed or unboxed) primitive
                if (!curType.isPrimitive()) {
                    // unboxed source if sourceboxed
                    res = make.at(diagPos).TypeCast(unboxedSourceType, res);
                    curType = unboxedSourceType;
                }
                if (unboxedSourceType != unboxedTargetType) {
                    // convert as primitive types
                    res = make.at(diagPos).TypeCast(unboxedTargetType, make.at(diagPos).TypeCast(unboxedSourceType, res));
                    curType = unboxedTargetType;
                }
                if (!targettedType.isPrimitive()) {
                    // box target if target boxed
                    res = make.at(diagPos).TypeCast(makeType(targettedType, false), res);
                    curType = targettedType;
                }
            } else {
                if (curType.isCompound() || curType.isPrimitive()) {
                    res = make.at(diagPos).TypeCast(makeType(types.erasure(targettedType), true), res);
                }
            }
            // We should add a cast "when needed".  Then visitTypeCast would just
            // call this function, and not need to call makeTypeCast on the result.
            // However, getting that to work is a pain - giving up for now.  FIXME
            return res;
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
            JCExpression t = makeQualifiedTree(ftype.tsym.getQualifiedName().toString());
            ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
            Type rtype = types.boxedTypeOrType(mtype.restype);
            typeargs.append(makeType(rtype));
            ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
            ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
            int i = 0;
            for (List<Type> l = mtype.argtypes; l.nonEmpty(); l = l.tail) {
                Name pname = make.paramName(i++);
                Type ptype = types.boxedTypeOrType(l.head);
                JCVariableDecl param = makeParam(ptype, pname);
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
                stats = List.of(makeExec(call), makeReturn(makeNull()));
            } else {
                if (mtype.restype.isPrimitive()) {
                    call = makeBox(diagPos, call, mtype.restype);
                }
                stats = List.<JCStatement>of(makeReturn(call));
            }
            JCMethodDecl bridgeDef = m().MethodDef(
                    m().Modifiers(flags),
                    defs.invokeName,
                    makeType(rtype),
                    List.<JCTypeParameter>nil(),
                    params.toList(),
                    m().Types(mtype.getThrownTypes()),
                    m().Block(0, stats),
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
         * Initialize the instance variables of the instance
         * @param instName
         */
        protected abstract void initInstanceVariables(Name instName);

        /**
         * @return the constructor args -- translating any supplied args
         */
        protected abstract List<JCExpression> completeTranslatedConstructorArgs();

        protected JCExpression translateInstanceVariableInit(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
            JCExpression trans = translateExpr(init, vsym.type);
            return convertNullability(init.pos(), trans, init, vsym.type);
        }

        void setInstanceVariable(DiagnosticPosition diagPos, Name instanceName, JavafxBindStatus bindStatus, VarSymbol vsym, JCExpression transInit) {
            JCExpression tc = instanceName == null ? null : id(instanceName);
            varInits.append(callStmt(tc, attributeBeName(vsym), transInit));
            varSyms.append(vsym);
        }

        void setInstanceVariable(Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JFXExpression init) {
            DiagnosticPosition initPos = init.pos();
            JCExpression transInit = translateInstanceVariableInit(init, bindStatus, vsym);
            setInstanceVariable(initPos, instName, bindStatus, vsym, transInit);
        }

        void makeInitSupportCall(Name methName, Name receiverName) {
            addPreface(callStmt(id(receiverName), methName));
        }

        void makeInitApplyDefaults(Type classType, Name receiverName) {
            ClassSymbol classSym = (ClassSymbol)classType.tsym;
            int count = varSyms.size();

            JCVariableDecl loopVar = makeTmpLoopVar(diagPos, 0);
            Name loopName = loopVar.name;
            JCExpression loopLimit = call(id(receiverName), defs.attributeCountMethodName);
            JCVariableDecl loopLimitVar = makeTmpVar("count", syms.intType, loopLimit);
            addPreface(loopLimitVar);
            JCExpression loopTest = makeBinary(JCTree.LT, id(loopName), id(loopLimitVar.name));
            List<JCExpressionStatement> loopStep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(loopName), makeInt(1))));
            JCStatement loopBody;

            List<JCExpression> args = List.<JCExpression>of(id(loopName));
            JCStatement applyDefaultsExpr = callStmt(id(receiverName), defs.attributeApplyDefaultsPrefixMethodName, args);

            if (1 < count) {
                // final short[] jfx$0map = GETMAP$X();
                JCExpression getmapExpr = call(varGetMapName(classSym));
                JCVariableDecl mapVar = makeTmpVar("map", syms.javafx_ShortArray, getmapExpr);
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
                    cases.append(m().Case(makeInt(tags[index++]), List.<JCStatement>of(varInit, m().Break(null))));
                }

                cases.append(m().Case(null, List.<JCStatement>of(applyDefaultsExpr, m().Break(null))));

                JCExpression mapExpr = m().Indexed(id(mapVar), id(loopName));
                loopBody = m().Switch(mapExpr, cases.toList());
            } else {
                VarSymbol varSym = varSyms.first();
                JCExpression varOffsetExpr = makeVarOffset(varSym, classSym); //TODO: fill-in selectorSym
                JCVariableDecl offsetVar = makeTmpVar("off", syms.intType, varOffsetExpr);
                addPreface(offsetVar);
                JCExpression condition = makeEqual(id(loopName), id(offsetVar));
                loopBody = m().If(condition, varInits.first(), applyDefaultsExpr);
            }

            addPreface(m().ForLoop(List.<JCStatement>of(loopVar), loopTest, loopStep, loopBody));
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
            initInstanceVariables(tmpVarName);  // Must preceed varSyms.nonEmpty() test

            JCExpression instExpression;
            if (varSyms.nonEmpty() || (isFX && newClassArgs.nonEmpty()) || cdef != null) {
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
                newClassArgs = newClassArgs.append(makeBoolean(true));

                // Create the new instance, placing it in a temporary variable "jfx$0objlit"
                //       final X jfx$0objlit = new X(true);
                addPreface(makeVar(
                        type,
                        tmpVarName,
                        m().NewClass(null, null, classTypeExpr, newClassArgs, null)));

                // Apply defaults to the instance variables
                //
                //       final short[] jfx$0map = GETMAP$X();
                //       for (int jfx$0initloop = 0; i < X.$VAR_COUNT; i++) {
                //           ...
                //       }
                if (varSyms.nonEmpty()) {
                    makeInitApplyDefaults(type, tmpVarName);
                } else {
                    makeInitSupportCall(defs.attributeApplyDefaultsPrefixMethodName, tmpVarName);
                }

                // Call complete$ to do user's init and postinit blocks
                //       jfx$0objlit.complete$();
                makeInitSupportCall(defs.completeName, tmpVarName);

                // Return the instance from the block expressions
                //       jfx$0objlit
                instExpression = id(tmpVarName);

            } else {
                // this is a Java class or has no instance variable initializers, just instanciate it
                instExpression = m().NewClass(null, null, classTypeExpr, newClassArgs, null);
            }

            return toResult(instExpression, type);
        }
    }

    /**
     * Translator for object literals
     */
    abstract class InstanciateTranslator extends NewInstanceTranslator {

        protected final JFXInstanciate tree;
        private final ClassSymbol idSym;

        InstanciateTranslator(final JFXInstanciate tree) {
            super(tree.pos());
            this.tree = tree;
            this.idSym = (ClassSymbol)JavafxTreeInfo.symbol(tree.getIdentifier());
        }

        abstract protected void processLocalVar(JFXVar var);

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
                JCIdent thisIdent = id(defs.receiverName);
                translated = translated.prepend(thisIdent);
            }
            return translated;
        }

        protected ExpressionResult doit() {
            for (JFXVar var : tree.getLocalvars()) {
                // add the variable before the class definition or object litersl assignment
                processLocalVar(var);
            }
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
            JCExpression ret = typeCast(diagPos, clazz.type, expr.type, tExpr);
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
                tExpr = call(syms.javafx_SequencesType, "getSingleValue", tExpr);
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
                return makeNull();
            }
        }
    }

    /********** visitors redirected back to JavafxToJava **********/

    public void visitClassDeclaration(JFXClassDeclaration tree) {
        toJava.visitClassDeclaration(tree);
        result = toJava.result;
    }

    /********** goofy visitors, alpha order -- many of which should go away **********/

    public void visitCatch(JFXCatch tree) {
        assert false : "should be processed by parent tree";
    }

    public void visitErroneous(JFXErroneous tree) {
        assert false : "erroneous nodes shouldn't have gotten this far";
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }

    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    public void visitImport(JFXImport tree) {
        assert false : "should be processed by parent tree";
    }

   public void visitModifiers(JFXModifiers tree) {
        assert false : "should be processed by parent tree";
    }

    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should be processed by parent tree";
    }

    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should be processed by parent tree";
    }

    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should be processed by parent tree";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    public void visitTree(JFXTree that) {
        assert false : "Should not be here!!!";
    }
    public void visitTypeAny(JFXTypeAny that) {
        assert false : "should be processed by parent tree";
    }

    public void visitTypeClass(JFXTypeClass that) {
        assert false : "should be processed by parent tree";
    }

    public void visitTypeFunctional(JFXTypeFunctional that) {
        assert false : "should be processed by parent tree";
    }

    public void visitTypeArray(JFXTypeArray tree) {
        assert false : "should be processed by parent tree";
    }

    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should be processed by parent tree";
    }
}

