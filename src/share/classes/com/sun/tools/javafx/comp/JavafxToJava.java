/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.lang.model.type.TypeKind;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.code.Type.WildcardType;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedOverrideClassVarInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedVarInfo;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.JavafxClassModel;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

/**
 * Translate JavaFX ASTs into Java ASTs
 *
 * @author Robert Field
 * @author Per Bothner
 * @author Lubo Litchev
 */
public class JavafxToJava extends JavafxTranslationSupport implements JavafxVisitor {
    protected static final Context.Key<JavafxToJava> jfxToJavaKey =
        new Context.Key<JavafxToJava>();

    /*
     * modules imported by context
     */
    private final JavafxToBound toBound;
    private final JavafxInitializationBuilder initBuilder;
    private final JavafxOptimizationStatistics optStat;

    /*
     * Buffers holding definitions waiting to be prepended to the current list of definitions.
     * At class or top-level these are the same.
     * Within a method (or block) prependToStatements is split off.
     * They need to be different because anonymous classes need to be declared in the scope of the method,
     * but interfaces can't be declared here.
     */
    private ListBuffer<JCStatement> prependToDefinitions = null;
    private ListBuffer<JCStatement> prependToStatements = null;

    private ListBuffer<JCExpression> additionalImports = null;

    private Map<Symbol, Name> substitutionMap = new HashMap<Symbol, Name>();

    public enum Wrapped {
        // We need a Location
        InLocation,
        // We need a sequence element or size.
        InIndexed,
        // We need a value
        InNothing
    }

    Wrapped wrap = Wrapped.InNothing;


    // for type morphing
    enum Yield {
        ToExpression,
        ToReturnStatement,
        ToExecStatement
    }

    //TODO: all these should, probably, go into a translation wrap class
    Yield yield = Yield.ToExpression;

    protected JavafxEnv<JavafxAttrContext> attrEnv;
    private Target target;

    abstract static class Translator {

        protected DiagnosticPosition diagPos;
        protected final JavafxToJava toJava;
        protected final JavafxDefs defs;
        protected final JavafxTypes types;
        protected final JavafxSymtab syms;

        Translator(DiagnosticPosition diagPos, JavafxToJava toJava) {
            this.diagPos = diagPos;
            this.toJava = toJava;
            this.defs = toJava.defs;
            this.types = toJava.types;
            this.syms = toJava.syms;
        }

        protected abstract JCTree doit();

        protected TreeMaker m() {
            return toJava.make.at(diagPos);
        }

        protected JavafxTreeMaker fxm() {
            return toJava.fxmake.at(diagPos);
        }

        /**
         * Convert type to JCExpression
         */
        protected JCExpression makeExpression(Type type) {
            return toJava.makeTypeTree(diagPos, type, true);
        }
    }

    /*
     * static information
     */
    private static final String sequencesRangeString = "com.sun.javafx.runtime.sequence.Sequences.range";
    private static final String sequencesRangeExclusiveString = "com.sun.javafx.runtime.sequence.Sequences.rangeExclusive";
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.ArraySequence";
    private static final String boundSequenceBuilderString = "com.sun.javafx.runtime.sequence.BoundSequenceBuilder";
    private static final String noMainExceptionString = "com.sun.javafx.runtime.NoMainException";
    private static final String toSequenceString = "toSequence";
    private static final String methodThrowsString = "java.lang.Throwable";
    private JFXClassDeclaration currentClass;  //TODO: this is redundant with attrEnv.enclClass

    /** Class symbols for classes that need a reference to the outer class. */
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();

    private static final Pattern EXTENDED_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT][guwxGUVWXE]");

    abstract class NullCheckTranslator extends Translator {

        protected final JFXExpression toCheck;
        protected final Type resultType;
        private final boolean needNullCheck;
        private boolean hasSideEffects;
        private boolean hse;
        private ListBuffer<JCStatement> tmpVarList;

        NullCheckTranslator(DiagnosticPosition diagPos, JFXExpression toCheck, Type resultType, boolean knownNonNull) {
            super(diagPos, JavafxToJava.this);
            this.toCheck = toCheck;
            this.resultType = resultType;
            this.needNullCheck = !knownNonNull && !toCheck.type.isPrimitive() && possiblyNull(toCheck);
            this.hasSideEffects = needNullCheck && computeHasSideEffects(toCheck);
            this.tmpVarList = ListBuffer.<JCStatement>lb();
        }

        abstract JCExpression fullExpression(JCExpression mungedToCheckTranslated);

        abstract JCExpression translateToCheck(JFXExpression expr);

        protected JCExpression preserveSideEffects(Type type, JFXExpression expr, JCExpression trans) {
            if (needNullCheck && expr!=null && computeHasSideEffects(expr)) {
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
            JCVariableDecl tmpVar = makeTmpVar(diagPos, "pse", varType, trans);
            tmpVarList.append(tmpVar);
            return m().Ident(tmpVar.name);
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
               case IDENT:
                   return ((JFXIdent)expr).sym instanceof VarSymbol;
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

        private boolean computeHasSideEffects(JFXExpression expr) {
            hse = false;
            new JavafxTreeScanner() {

                private void markSideEffects() {
                    hse = true;
                }

                @Override
                public void visitBlockExpression(JFXBlock tree) {
                    markSideEffects(); // maybe doesn't but covers all statements
                }

                @Override
                public void visitUnary(JFXUnary tree) {
                    markSideEffects();
                }

                @Override
                public void visitAssign(JFXAssign tree) {
                    markSideEffects();
                }

                @Override
                public void visitAssignop(JFXAssignOp tree) {
                    markSideEffects();
                }

                @Override
                public void visitInstanciate(JFXInstanciate tree) {
                    markSideEffects();
                }

                @Override
                public void visitFunctionInvocation(JFXFunctionInvocation tree) {
                    markSideEffects();
                }

                @Override
                public void visitSelect(JFXSelect tree) {
                    // Doesn't really have side-effects but the dupllicate null checking is aweful
                    //TODO: do this in a cleaner way
                    markSideEffects();
                }
            }.scan(expr);
            return hse;
        }

        protected JCTree doit() {
            JCExpression mungedToCheckTranslated = translateToCheck(toCheck);
            JCVariableDecl tmpVar = null;
            if (hasSideEffects) {
                // if the toCheck sub-expression has side-effects, compute it and stash in a
                // temp var so we don't invoke it twice.
                tmpVar = makeTmpVar(diagPos, "toCheck", toCheck.type, mungedToCheckTranslated);
                tmpVarList.append(tmpVar);
                mungedToCheckTranslated = m().Ident(tmpVar.name);
            }
            JCExpression full = fullExpression(mungedToCheckTranslated);
            if (!needNullCheck && tmpVarList.isEmpty()) {
                return full;
            }
            // Do a null check
            JCExpression toTest = hasSideEffects ? m().Ident(tmpVar.name) : translateToCheck(toCheck);
            // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
            JCExpression cond = m().Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
            if (resultType == syms.voidType) {
                // if this is a void expression, check it with an If-statement
                JCStatement stmt = m().Exec(full);
                if (needNullCheck) {
                    stmt = m().If(cond, stmt, null);
                }
                assert yield == Yield.ToExecStatement : "Yield from a void call should always be a statement";
                // a statement is the desired result of the translation, return the If-statement
                if (tmpVarList.nonEmpty()) {
                    // if the selector has side-effects, we created a temp var to hold it
                    // so we need to make a block to include the temp var
                    return m().Block(0L, tmpVarList.toList().append(stmt));
                } else {
                    return stmt;
                }
            } else {
                if (needNullCheck) {
                    // it has a non-void return type, convert it to a conditional expression
                    // if it would dereference null, then the full expression instead yields the default value
                    TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(resultType);
                    JCExpression defaultExpr = makeDefaultValue(diagPos, returnTypeInfo);
                    if (wrap == Wrapped.InLocation) {
                        defaultExpr = makeUnboundLocation(diagPos, returnTypeInfo, defaultExpr);
                    }
                    full = m().Conditional(cond, full, defaultExpr);
                }
                if (tmpVarList.nonEmpty()) {
                    // if the selector has side-effects, we created a temp var to hold it
                    // so we need to make a block-expression to include the temp var
                    full = makeBlockExpression(diagPos, tmpVarList.toList(), full);
                }
                return full;
            }
        }
    }

    public static JavafxToJava instance(Context context) {
        JavafxToJava instance = context.get(jfxToJavaKey);
        if (instance == null)
            instance = new JavafxToJava(context);
        return instance;
    }

    protected JavafxToJava(Context context) {
        super(context);

        context.put(jfxToJavaKey, this);

        toBound = JavafxToBound.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);
        optStat = JavafxOptimizationStatistics.instance(context);
        target = Target.instance(context);
    }

    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    private <TFX extends JFXTree, TC extends JCTree> TC translateGeneric(TFX tree) {
        TC ret;
        Yield prevYield = yield;
        yield = Yield.ToExpression; // reset to default
        if (tree == null) {
            ret = null;
        } else {
            JFXTree prevWhere = attrEnv.where;
            attrEnv.where = tree;
            tree.accept(this);
            attrEnv.where = prevWhere;
            ret = (TC) this.result;
            this.result = null;
        }
        yield = prevYield;
        return ret;
    }

    public JCExpression translate(JFXExpression tree) {
        return translateGeneric(tree);
    }

    public JCMethodDecl translate(JFXFunctionDefinition tree) {
        return translateGeneric(tree);
    }

    public JCVariableDecl translate(JFXVar tree) {
        return translateGeneric(tree);
    }

    public JCCompilationUnit translate(JFXScript tree) {
        return translateGeneric(tree);
    }

    public JCTree translate(JFXTree tree) {
        return translateGeneric(tree);
    }

    public JCStatement translateClassDef(JFXClassDeclaration tree) {
        return translateGeneric(tree);
    }

    public JFXOnReplace translate(JFXOnReplace tree) {
        JFXOnReplace ret;
        Yield prevYield = yield;
        yield = Yield.ToExpression; // reset to default
	if (tree == null) {
	    ret = null;
	} else {
            JFXTree prevWhere = attrEnv.where;
            attrEnv.where = tree;
	    tree.accept(this);
            attrEnv.where = prevWhere;
	    ret = (JFXOnReplace)this.result;
	    this.result = null;
	}
        yield = prevYield;
        return ret;
    }

    public JCExpression translate(JFXExpression tree, Type type) {
        if (tree == null)
            return null;
        if (tree instanceof JFXBlock && yield == Yield.ToExpression) {
            visitBlockExpression((JFXBlock) tree, type);
            return (JCExpression) result;
        }
        return convertTranslated(translate(tree), tree.pos(), tree.type, type);
    }

    public JCExpression convertTranslated(JCExpression translated, DiagnosticPosition diagPos,
            Type sourceType, Type targetType) {
        boolean sourceIsSequence = types.isSequence(sourceType);
        boolean targetIsSequence = types.isSequence(targetType);
        boolean sourceIsArray = sourceType==null? false : types.isArray(sourceType);
        boolean targetIsArray = targetType==null? false : types.isArray(targetType);
        if (targetIsArray) {
            Type elemType = types.elemtype(targetType);
            if (sourceIsSequence) {
                if (elemType.isPrimitive()) {
                    String mname = "toArray";
                    if (elemType == syms.floatType) {
                        mname = "toFloatArray";
                    } else if (elemType == syms.doubleType) {
                        mname = "toDoubleArray";
                    }
                    return callExpression(diagPos, makeTypeTree(diagPos, syms.javafx_SequencesType, false),
                            mname, translated);
                }
                ListBuffer<JCStatement> stats = ListBuffer.lb();
                JCVariableDecl tmpVar = makeTmpVar(diagPos, sourceType, translated);
                stats.append(tmpVar);
                JCVariableDecl sizeVar = makeTmpVar(diagPos, syms.intType, callExpression(diagPos, make.at(diagPos).Ident(tmpVar.name), "size"));
                stats.append(sizeVar);
                JCVariableDecl arrVar = makeTmpVar(diagPos, "arr", targetType, make.at(diagPos).NewArray(
                        makeTypeTree(diagPos, elemType, true),
                        List.<JCExpression>of(make.at(diagPos).Ident(sizeVar.name)),
                        null));
                stats.append(arrVar);
                stats.append(callStatement(diagPos, make.at(diagPos).Ident(tmpVar.name), "toArray", List.of(
                        make.Literal(TypeTags.INT, 0),
                        make.at(diagPos).Ident(sizeVar.name),
                        make.at(diagPos).Ident(arrVar.name),
                        make.at(diagPos).Literal(TypeTags.INT, 0))));
                JCIdent ident2 = make.at(diagPos).Ident(arrVar.name);
                return makeBlockExpression(diagPos, stats, ident2);
            } else {
                //TODO: conversion may be needed here, but this is better than what we had
                return translated;
            }
        }
        if (sourceIsArray && targetIsSequence) {
            Type sourceElemType = types.elemtype(sourceType);
            List<JCExpression> args;
            if (sourceElemType.isPrimitive()) {
                args = List.of(translated);
            } else {
                args = List.of(makeTypeInfo(diagPos, sourceElemType), translated);
            }
            JCExpression cSequences = makeTypeTree(diagPos, syms.javafx_SequencesType, false);
            return callExpression(diagPos, cSequences, "fromArray", args);
        }
        if (targetIsSequence && ! sourceIsSequence) {
            //if (sourceType.tag == TypeTags.BOT) {
            //    // it is a null, convert to empty sequence
            //    //TODO: should we leave this null?
            //    Type elemType = types.elemtype(type);
            //    return makeEmptySequenceCreator(diagPos, elemType);
            //}
            Type targetElemType = types.elementType(targetType);
            JCExpression cSequences = makeTypeTree(diagPos, syms.javafx_SequencesType, false);
            if (sourceType.isPrimitive())
                translated = convertTranslated(translated, diagPos, sourceType, targetElemType);
            return callExpression(diagPos,
                    cSequences,
                    "singleton",
                    List.of(makeTypeInfo(diagPos, targetElemType), translated));
        }
        if (targetIsSequence && sourceIsSequence) {
            Type sourceElemType = types.elementType(sourceType);
            Type targetElemType = types.elementType(targetType);
            //TODO:NUMERIC
            if (types.isSameType(sourceElemType, syms.javafx_IntegerType) && types.isSameType(targetElemType, syms.javafx_DoubleType)) {
                JCExpression cSequences = makeTypeTree(diagPos, syms.javafx_SequencesType, false);
                return callExpression(diagPos,
                        cSequences,
                        "integerSequenceToNumberSequence",
                        List.of(translated));
            }
        }

        Type unboxedTargetType = targetType.isPrimitive() ? targetType :
            types.unboxedType(targetType);
        if (unboxedTargetType != Type.noType) {
            Type unboxedSourceType = sourceType.isPrimitive() ? sourceType
                    : types.unboxedType(sourceType);
            if (unboxedSourceType != Type.noType &&
                    unboxedSourceType != unboxedTargetType) {
                 translated = make.at(diagPos).TypeCast(unboxedTargetType, translated);
            }
        }

        if (sourceType.isCompound()) {
            translated = make.at(diagPos).TypeCast(makeTypeTree(diagPos, types.erasure(targetType), true), translated);
        }
        return translated;
    }

    /** Visitor method: translate a list of nodes.
     */
    private <TFX extends JFXTree, TC extends JCTree> List<TC> translateGeneric(List<TFX> trees) {
        ListBuffer<TC> translated = ListBuffer.lb();
        if (trees == null) {
            return null;
        }
        for (List<TFX> l = trees; l.nonEmpty(); l = l.tail) {
            TC tree = translateGeneric(l.head);
            if (tree != null) {
                translated.append(tree);
            }
        }
        return translated.toList();
    }

    public List<JCExpression> translateExpressions(List<JFXExpression> trees) {
        return translateGeneric(trees);
    }

    public List<JCCatch> translateCatchers(List<JFXCatch> trees) {
        return translateGeneric(trees);
    }

    //// Translation with wrap transition

    public JCExpression translate(JFXExpression tree, Wrapped newState) {
        Wrapped prevWrap = wrap;
        wrap = newState;
        JCExpression ret = translate(tree);
        wrap = prevWrap;
        return ret;
    }

    JCBlock translateBlockExpressionToBlock(JFXBlock bexpr) {
        JCStatement stmt = translateExpression(bexpr, Wrapped.InNothing, syms.voidType);
        return stmt==null? null : asBlock(stmt);
    }

    JCStatement translateExpressionToYield(JFXExpression expr) {
        assert yield != Yield.ToExpression;
        if (expr == null) {
            return null;
        } else {
            JFXTree prevWhere = attrEnv.where;
            attrEnv.where = expr;
            expr.accept(this);
            attrEnv.where = prevWhere;
            JCTree ret = this.result;
            this.result = null;
            return ret instanceof JCStatement ? (JCStatement) ret // already converted
                    : toCorrectReturn(expr, (JCExpression) ret);
        }
    }

    JCStatement translateExpressionToReturn(JFXExpression expr) {
        Yield prevYield = yield;
        yield = Yield.ToReturnStatement;
        JCStatement ret = translateExpressionToYield(expr);

        yield = prevYield;
        return ret;
    }

    JCStatement translateExpressionToStatement(JFXExpression expr) {
        Yield prevYield = yield;
        yield = Yield.ToExecStatement;
        JCStatement ret = translateExpressionToYield(expr);

        yield = prevYield;
        return ret;
    }

    JCStatement translateExpression(JFXExpression expr, Wrapped newState, Type targetType) {
        Wrapped prevWrap = wrap;
        Yield prevYield = yield;
        wrap = newState;
        JCStatement ret;
        if (targetType==syms.voidType || targetType == expr.type
                || targetType == null || expr.type == syms.unreachableType) {
            yield = targetType==syms.voidType? Yield.ToExecStatement : Yield.ToReturnStatement;
            ret = translateExpressionToYield(expr);
        } else {
            yield = Yield.ToExpression;
            ret = make.at(expr).Return(translate(expr, targetType));
        }
        yield = prevYield;
        wrap = prevWrap;
        return ret;
    }

    JCStatement translateExpression(JFXExpression expr, Type targetType) {
        return translateExpression(expr, wrap,  targetType);
    }

    /**
     * For special cases where the expression my not be fully attributed.
     * Specifically: package and import names.
     * Do a dumb simple conversion.
     *
     * @param tree
     * @return
     */
    private JCExpression straightConvert(JFXExpression tree) {
        if (tree == null) {
            return null;
        }
        DiagnosticPosition diagPos = tree.pos();
        switch (tree.getFXTag()) {
            case IDENT: {
                JFXIdent id = (JFXIdent) tree;
                return make.at(diagPos).Ident(id.name);
            }
            case SELECT: {
                JFXSelect sel = (JFXSelect) tree;
                return make.at(diagPos).Select(
                        straightConvert(sel.getExpression()),
                        sel.getIdentifier());
            }
            default:
                throw new RuntimeException("bad conversion");
        }
    }

    JCBlock asBlock(JCStatement stmt) {
        if (stmt.getTag() == JCTree.BLOCK) {
            return (JCBlock)stmt;
        } else {
            return make.at(stmt).Block(0L, List.of(stmt));
        }
    }

    JCStatement toCorrectReturn(JFXExpression expr, JCExpression translated) {
        switch (yield) {
            case ToExecStatement:
                return make.at(expr).Exec( translated );
            case ToReturnStatement:
                return make.at(expr).Return( translated );
            case ToExpression:
            default:
                assert false : "this method should not be called";
                return null;
        }
    }

    private boolean substitute(Symbol sym) {
        if (wrap == Wrapped.InLocation) {
            return false;
        }
        Name name = substitutionMap.get(sym);
        if (name == null) {
            return false;
        } else {
            result = make.Ident(name);
            return true;
        }
    }

    private void setSubstitution(JFXTree target, Symbol sym) {
        if (target instanceof JFXInstanciate) {
            // Set up to substitute a reference to the this var within its definition
            ((JFXInstanciate) target).varDefinedByThis = sym;
        }
    }

    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;

        attrEnv.translatedToplevel = translate(attrEnv.toplevel);
        attrEnv.translatedToplevel.endPositions = attrEnv.toplevel.endPositions;
    }

    @Override
    public void visitScript(JFXScript tree) {
        // add to the hasOuters set the clas symbols for classes that need a reference to the outer class
        fillClassesWithOuters(tree);

        ListBuffer<JCTree> translatedDefinitions = ListBuffer.lb();
        ListBuffer<JCTree> imports = ListBuffer.lb();
        additionalImports = ListBuffer.lb();
        prependToStatements = prependToDefinitions = ListBuffer.lb();
        for (JFXTree def : tree.defs) {
            if (def.getFXTag() != JavafxTag.IMPORT) {
                // anything but an import
                translatedDefinitions.append(translate(def));
            }
        }

       // order is imports, any prepends, then the translated non-imports
        for (JCTree prepend : prependToDefinitions) {
            translatedDefinitions.prepend(prepend);
        }

        for (JCTree prepend : imports) {
            translatedDefinitions.prepend(prepend);
        }

        for (JCExpression prepend : additionalImports) {
            translatedDefinitions.append(make.Import(prepend, false));
        }

        prependToStatements = prependToDefinitions = null; // shouldn't be used again until the next top level

 	JCExpression pid = straightConvert(tree.pid);
        JCCompilationUnit translated = make.at(tree.pos).TopLevel(List.<JCAnnotation>nil(), pid, translatedDefinitions.toList());
        translated.sourcefile = tree.sourcefile;
        translated.docComments = null;
        translated.lineMap = tree.lineMap;
        translated.flags = tree.flags;
        result = translated;
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXClassDeclaration prevClass = currentClass;
        JFXClassDeclaration prevEnclClass = attrEnv.enclClass;
        Wrapped prevWrap = wrap;
        wrap = Wrapped.InNothing;
        currentClass = tree;

        try {
            DiagnosticPosition diagPos = tree.pos();

            attrEnv.enclClass = tree;

            ListBuffer<JCStatement> translatedInitBlocks = ListBuffer.lb();
            ListBuffer<JCStatement> translatedPostInitBlocks = ListBuffer.lb();
            ListBuffer<JCTree> translatedDefs = ListBuffer.lb();
            ListBuffer<TranslatedVarInfo> attrInfo = ListBuffer.lb();
            ListBuffer<TranslatedOverrideClassVarInfo> overrideInfo = ListBuffer.lb();

           // translate all the definitions that make up the class.
           // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JFXTree def : tree.getMembers()) {
                    switch(def.getFXTag()) {
                        case INIT_DEF: {
                            translateAndAppendStaticBlock(((JFXInitDefinition) def).getBody(), translatedInitBlocks);
                            break;
                        }
                        case POSTINIT_DEF: {
                            translateAndAppendStaticBlock(((JFXPostInitDefinition) def).getBody(), translatedPostInitBlocks);
                            break;
                        }
                        case VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            boolean isStatic = (attrDef.getModifiers().flags & STATIC) != 0;
                            JCStatement init =
                                translateDefinitionalAssignmentToSet(attrDef.pos(),
                                attrDef.getInitializer(), attrDef.getBindStatus(), attrDef.sym,
                                isStatic? null : defs.receiverName, FROM_DEFAULT_MILIEU);
                            attrInfo.append(new TranslatedVarInfo(
                                    attrDef,
                                    typeMorpher.varMorphInfo(attrDef.sym),
                                    init,
                                    attrDef.getOnReplace(),
                                    translatedOnReplaceBody(attrDef.getOnReplace())));
                            //translatedDefs.append(trans);
                            break;
                        }
                        case OVERRIDE_ATTRIBUTE_DEF: {
                            JFXOverrideClassVar override = (JFXOverrideClassVar) def;
                            boolean isStatic = (override.sym.flags() & STATIC) != 0;
                            JCStatement init;
                            if (override.getInitializer() == null) {
                                init = null;
                            } else {
                                init = translateDefinitionalAssignmentToSet(override.pos(),
                                    override.getInitializer(), override.getBindStatus(), override.sym,
                                    isStatic? null : defs.receiverName,
                                    FROM_DEFAULT_MILIEU);
                            }
                            overrideInfo.append(new TranslatedOverrideClassVarInfo(
                                    override,
                                    typeMorpher.varMorphInfo(override.sym),
                                    init,
                                    override.getOnReplace(),
                                    translatedOnReplaceBody(override.getOnReplace())));
                            break;
                        }
                       case FUNCTION_DEF : {
                           JFXFunctionDefinition funcDef = (JFXFunctionDefinition) def;
                           translatedDefs.append(translate(funcDef));
                           break;
                        }
                        default: {
                            JCTree tdef =  translate(def);
                            if ( tdef != null ) {
                                translatedDefs.append( tdef );
                            }
                            break;
                        }
                    }
                }
            }
            // the translated defs have prepends in front
            for (JCTree prepend : prependToDefinitions) {
                translatedDefs.prepend(prepend);
            }
            prependToDefinitions = prevPrependToDefinitions ;
            prependToStatements = prevPrependToStatements;
            // WARNING: translate can't be called directly or indirectly after this point in the method, or the prepends won't be included

            boolean classOnly = tree.generateClassOnly();
            JavafxClassModel model = initBuilder.createJFXClassModel(tree, attrInfo.toList(), overrideInfo.toList());
            additionalImports.appendList(model.additionalImports);

            boolean classIsFinal = (tree.getModifiers().flags & Flags.FINAL) != 0;

            // include the interface only once
            if (!tree.hasBeenTranslated) {
                if (! classOnly) {
                    JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.INTERFACE);
                    mods = addAccessAnnotationModifiers(diagPos, tree.mods.flags, mods);

                    JCClassDecl cInterface = make.ClassDef(mods,
                            model.interfaceName, List.<JCTypeParameter>nil(), null,
                            model.interfaces, model.iDefinitions);

                    prependToDefinitions.append(cInterface); // prepend to the enclosing class or top-level
                }
                tree.hasBeenTranslated = true;
            }

            translatedDefs.appendList(model.additionalClassMembers);

            {
                // Add the userInit$ method
                List<JCVariableDecl> receiverVarDeclList = List.of(makeReceiverParam(tree));
                ListBuffer<JCStatement> initStats = ListBuffer.lb();
                // call the superclasses userInit$
                Set<String> dupSet = new HashSet<String>();
                for (JFXExpression parent : tree.getExtending()) {
                    if (! (parent instanceof JFXIdent))
                        continue;
                    Symbol symbol = expressionSymbol(parent);
                    if (types.isJFXClass(symbol)) {
                        ClassSymbol cSym = (ClassSymbol) symbol;
                        String className = cSym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree( diagPos,cSym.type, true), make.Ident(defs.receiverName)));
                            initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, className), defs.userInitName, args1));
                        }
                    }
                }
                initStats.appendList(translatedInitBlocks);

                JCBlock userInitBlock = make.Block(0L, initStats.toList());
                translatedDefs.append(make.MethodDef(
                        make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC),
                        defs.userInitName,
                        makeTypeTree( null,syms.voidType),
                        List.<JCTypeParameter>nil(),
                        receiverVarDeclList,
                        List.<JCExpression>nil(),
                        userInitBlock,
                        null));
            }
            {
                // Add the userPostInit$ method
                List<JCVariableDecl> receiverVarDeclList = List.of(makeReceiverParam(tree));
                ListBuffer<JCStatement> initStats = ListBuffer.lb();
                // call the superclasses postInit$
                Set<String> dupSet = new HashSet<String>();
                for (JFXExpression parent : tree.getExtending()) {
                    if (! (parent instanceof JFXIdent))
                        continue;
                    Symbol symbol = expressionSymbol(parent);
                    if (types.isJFXClass(symbol)) {
                        ClassSymbol cSym = (ClassSymbol) symbol;
                        String className = cSym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree( diagPos,cSym.type, true), make.Ident(defs.receiverName)));
                            initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, className), defs.postInitName, args1));
                        }
                    }
                }
                initStats.appendList(translatedPostInitBlocks);

                JCBlock postInitBlock = make.Block(0L, initStats.toList());
                translatedDefs.append(make.MethodDef(
                        make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC),
                        defs.postInitName,
                        makeTypeTree( null,syms.voidType),
                        List.<JCTypeParameter>nil(),
                        receiverVarDeclList,
                        List.<JCExpression>nil(),
                        postInitBlock,
                        null));
            }

            if (tree.isModuleClass) {
                // Add main method...
                translatedDefs.append(makeMainMethod(diagPos, tree.getName()));
            }

            // build the list of implemented interfaces
            List<JCExpression> implementing;
            if (classOnly) {
                implementing = model.interfaces;
            }
            else {
                implementing = List.of(make.Ident(model.interfaceName), makeIdentifier(diagPos, fxObjectString));
            }

            long flags = tree.mods.flags & (Flags.PUBLIC | Flags.PRIVATE | Flags.PROTECTED | Flags.FINAL | Flags.ABSTRACT);
            if (tree.sym.owner.kind == Kinds.TYP) {
                flags |= Flags.STATIC;
            }
            JCModifiers classMods = make.at(diagPos).Modifiers(flags);
            classMods = addAccessAnnotationModifiers(diagPos, tree.mods.flags, classMods);

            // make the Java class corresponding to this FX class, and return it
            JCClassDecl res = make.at(diagPos).ClassDef(
                    classMods,
                    tree.getName(),
                    List.<JCTypeParameter>nil(), // type parameters
                    model.superType==null? null : makeTypeTree( null,model.superType, false),
                    implementing,
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
            result = res;
        }
        finally {
            attrEnv.enclClass = prevEnclClass;
            wrap = prevWrap;

            currentClass = prevClass;
        }
    }
    //where
    private void translateAndAppendStaticBlock(JFXBlock block, ListBuffer<JCStatement> translatedBlocks) {
        JCStatement stmt = translateExpressionToStatement(block);
        if (stmt != null) {
            translatedBlocks.append(stmt);
        }
    }

    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    abstract static class InstanciateTranslator extends Translator {

        protected final JFXInstanciate tree;
        protected ListBuffer<JCStatement> stats = ListBuffer.lb();

        InstanciateTranslator(final JFXInstanciate tree, JavafxToJava toJava) {
            super(tree.pos(), toJava);
            this.tree = tree;
        }

        abstract protected void processLocalVar(JFXVar var);

        protected List<JCExpression> translatedConstructorArgs() {
            List<JFXExpression> args = tree.getArgs();
            if (args == null) {
                return null;
            }
            Symbol sym = tree.constructor;
            if (sym != null && sym.type != null) {
                List<Type> formals = sym.type.asMethodType().getParameterTypes();
                boolean usesVarArgs = (sym.flags() & VARARGS) != 0L &&
                        (formals.size() != args.size() ||
                        types.isConvertible(args.last().type, types.elemtype(formals.last())));
                ListBuffer<JCExpression> translated = ListBuffer.lb();
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
                    JCExpression targ = toJava.translate(l.head, formal);
                    if (targ != null) {
                        translated.append(targ);
                    }
                }
                return translated.toList();
            } else {
                return toJava.translateExpressions(args);
            }
        }

        abstract protected JCStatement translateAttributeSet(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            Name attrName);

        protected JCExpression doit() {
            Type type;

            for (JFXVar var : tree.getLocalvars()) {
                // add the variable before the class definition or object litersl assignment
                processLocalVar(var);
            }
            if (tree.getClassBody() == null) {
                type = tree.type;
            } else {
                JFXClassDeclaration cdef = tree.getClassBody();
                stats.append(toJava.translateClassDef(cdef));
                type = cdef.type;
            }
            JCExpression classTypeExpr = toJava.makeTypeTree(tree, type, false);
            Symbol sym = JavafxTreeInfo.symbol(tree.getIdentifier());

            List<JCExpression> newClassArgs = translatedConstructorArgs();
            if (tree.getClassBody() != null || types.isJFXClass(sym)) {
                assert newClassArgs.size() == 0 : "should not be args for JavaFX class constructors";
                newClassArgs = newClassArgs.append(m().Literal(TypeTags.BOOLEAN, 1));
            }
            if (tree.getClassBody() != null &&
                    tree.getClassBody().sym != null && toJava.hasOuters.contains(tree.getClassBody().sym) ||
                 sym != null && toJava.hasOuters.contains(sym)) {
                JCIdent thisIdent = m().Ident(defs.receiverName);
                newClassArgs = newClassArgs.prepend(thisIdent);
            }

            JCNewClass newClass =
                    m().NewClass(null, null, classTypeExpr,
                    newClassArgs,
                    null);

            JCExpression instExpression;
            {
                if (sym != null &&
                        sym.kind == Kinds.TYP && (sym instanceof ClassSymbol) &&
                        (types.isJFXClass((ClassSymbol) sym) ||
                        tree.getClassBody() != null)) {
                    // it is a JavaFX class, initializa it properly
                    JCVariableDecl tmpVar = toJava.makeTmpVar(diagPos, "objlit", type, newClass);
                    stats.append(tmpVar);
                    if (tree.varDefinedByThis != null) {
                        toJava.substitutionMap.put(tree.varDefinedByThis, tmpVar.name);
                    }
                    for (JFXObjectLiteralPart olpart : tree.getParts()) {
                        diagPos = olpart.pos(); // overwrite diagPos (must restore)
                        JavafxBindStatus bindStatus = olpart.getBindStatus();
                        JFXExpression init = olpart.getExpression();
                        VarSymbol vsym = (VarSymbol) olpart.sym;

                        // Lift JFXObjectLiteralPart if needed
                        if (types.isSequence(olpart.type)) {
                            JFXExpression olexpr = olpart.getExpression();
                            if (!types.isSequence(olexpr.type)) {
                                init = (fxm()).ExplicitSequence(List.<JFXExpression>of(olexpr));
                                WildcardType tpType = new WildcardType(olexpr.type, BoundKind.EXTENDS, olexpr.type.tsym);
                                init.type = new ClassType(((JavafxSymtab) syms).javafx_SequenceType, List.<Type>of(tpType), ((JavafxSymtab) syms).javafx_SequenceType.tsym);
                            }
                        }

                        stats.append( translateAttributeSet(init, bindStatus, vsym, tmpVar.name) );
                    }
                    if (tree.varDefinedByThis != null) {
                        toJava.substitutionMap.remove(tree.varDefinedByThis);
                    }
                    diagPos = tree.pos();
                    JCIdent ident3 = m().Ident(tmpVar.name);
                    JCStatement applyExec = toJava.callStatement(diagPos, ident3, defs.initializeName);
                    stats.append(applyExec);

                    JCIdent ident2 = m().Ident(tmpVar.name);
                    instExpression = toJava.makeBlockExpression(diagPos, stats, ident2);
                } else {
                    // this is a Java class, just instanciate it
                    instExpression = newClass;
                }
            }
            if (toJava.wrap == Wrapped.InLocation) {
                 instExpression = toJava.makeConstantLocation(diagPos, tree.type, instExpression);
            }
            return instExpression;
        }
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {

        ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
        prependToStatements = ListBuffer.lb();

        result = new InstanciateTranslator(tree, this) {

            protected void processLocalVar(JFXVar var) {
                stats.append(translateExpressionToStatement(var));
            }

            protected JCStatement translateAttributeSet(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, Name instanceName) {
                return toJava.translateDefinitionalAssignmentToSet(diagPos, init, bindStatus,
                        vsym, instanceName, FROM_LITERAL_MILIEU);
            }
        }.doit();

        if (result instanceof BlockExprJCBlockExpression) {
            BlockExprJCBlockExpression blockExpr = (BlockExprJCBlockExpression) result;
            blockExpr.stats = blockExpr.getStatements().prependList(prependToStatements.toList());
        }
        prependToStatements = prevPrependToStatements;
    }

    abstract static class StringExpressionTranslator extends Translator {

        private final JFXStringExpression tree;
        StringExpressionTranslator(JFXStringExpression tree, JavafxToJava toJava) {
            super(tree.pos(), toJava);
            this.tree = tree;
        }

        protected JCExpression doit() {
            StringBuffer sb = new StringBuffer();
            List<JFXExpression> parts = tree.getParts();
            ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();

            JFXLiteral lit = (JFXLiteral) (parts.head);            // "...{
            sb.append((String) lit.value);
            parts = parts.tail;
            boolean containsExtendedFormat = false;

            while (parts.nonEmpty()) {
                lit = (JFXLiteral) (parts.head);                  // optional format (or null)
                String format = (String) lit.value;
                if ((!containsExtendedFormat) && format.length() > 0
                    && EXTENDED_FORMAT_PATTERN.matcher(format).find()) {
                    containsExtendedFormat = true;
                }
                parts = parts.tail;
                JFXExpression exp = parts.head;
                JCExpression texp;
                if (exp != null &&
                        types.isSameType(exp.type, syms.javafx_DurationType)) {
                    texp = m().Apply(null,
                            m().Select(translateArg(exp),
                            toJava.names.fromString("toDate")),
                            List.<JCExpression>nil());
                    sb.append(format.length() == 0 ? "%tQms" : format);
                } else {
                    texp = translateArg(exp);
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
                    values.prepend(m().Literal(TypeTags.BOT, null));
                } else {
                    values.prepend(m().Literal(TypeTags.CLASS, tree.translationKey));
                }
                String resourceName =
                        toJava.attrEnv.enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
                values.prepend(m().Literal(TypeTags.CLASS, resourceName));
            } else if (containsExtendedFormat) {
                formatMethod = "com.sun.javafx.runtime.util.FXFormatter.sprintf";
            } else {
                formatMethod = "java.lang.String.format";
            }
            JCExpression formatter = toJava.makeQualifiedTree(diagPos, formatMethod);
            return m().Apply(null, formatter, values.toList());
        }

        abstract protected JCExpression translateArg(JFXExpression arg);

    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        result = new StringExpressionTranslator(tree, this) {
            protected JCExpression translateArg(JFXExpression arg) {
                return translate(arg);
            }
        }.doit();
    }

    private JCExpression translateDefinitionalAssignmentToValueArg(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarMorphInfo vmi) {
        if (bindStatus.isUnidiBind()) {
            return toBound.translate(init, vmi.getRealType());
        } else if (bindStatus.isBidiBind()) {
            assert requiresLocation(vmi);
            // Bi-directional bind translate so it stays in a Location
            return translate(init, Wrapped.InLocation);
        } else {
            // normal init -- unbound
            if (init == null) {
                // no initializing expression determine a default value from the type
                return makeDefaultValue(diagPos, vmi);
            } else {
                // do a vanilla translation of the expression
                Type resultType = vmi.getSymbol().type;
                JCExpression trans = translate(init, resultType);
                return convertNullability(diagPos, trans, init, resultType);
            }
        }
    }

    private JCExpression translateDefinitionalAssignmentToValue(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        JCExpression valueArg = translateDefinitionalAssignmentToValueArg(diagPos, init, bindStatus, vmi);
        if (bindStatus.isUnidiBind()) {
            return valueArg;
        } else if (requiresLocation(vmi)) {
            Name makeName = defs.makeMethodName;
            if (bindStatus.isBidiBind()) {
                makeName = defs.makeBijectiveMethodName;
            }
            return makeLocationVariable(vmi, diagPos, List.of(valueArg), makeName);
        } else {
            return valueArg;
        }
    }

    private JCStatement translateDefinitionalAssignmentToSet(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            Name instanceName, int milieu) {
        if (init == null && vsym.owner.kind == Kinds.TYP && requiresLocation(vsym)) {
            // this is a class variable with no explicit initializer,
            // use setDefault() so that it is flagged as a default
            assert !bindStatus.isBound() : "cannot be bound and have no init expression";
            JCExpression localAttr = makeAttributeAccess(diagPos, vsym, instanceName);
            Name methName = defs.setDefaultMethodName;
            return callStatement(diagPos, localAttr, methName);
        }
        return make.at(diagPos).Exec( translateDefinitionalAssignmentToSetExpression(diagPos,
            init, bindStatus, vsym,
             instanceName, milieu) );
    }

    private JCExpression translateDefinitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            Name instanceName, int milieu) {
        assert( (vsym.flags() & Flags.PARAMETER) == 0L): "Parameters are not initialized";
        setSubstitution(init, vsym);
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        JCExpression valueArg = translateDefinitionalAssignmentToValueArg(diagPos, init, bindStatus, vmi);
        return definitionalAssignmentToSetExpression(diagPos, valueArg, bindStatus, vsym, instanceName,
                                                     vmi.getTypeKind(), milieu);
    }

    JCExpression definitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JCExpression valueArg, JavafxBindStatus bindStatus, VarSymbol vsym,
            Name instanceName, int typeKind, int milieu) {
        JCExpression varRef;

        if (!requiresLocation(vsym)) {
            if (vsym.owner.kind == Kinds.TYP) {
                // It is a member variable
                if (instanceName == null) {
                    varRef = make.at(diagPos).Ident(attributeFieldName(vsym));
                } else {
                    JCExpression tc = make.at(diagPos).Ident(instanceName);
                    final Name setter = attributeSetterName(vsym);
                    JCExpression toApply = make.at(diagPos).Select(tc, setter);
                    return make.at(diagPos).Apply(null, toApply, List.of(valueArg));
                }
            } else {
                // It is a local variable
                assert instanceName == null;
                varRef = make.at(diagPos).Ident(vsym);
            }
            return make.at(diagPos).Assign(varRef, valueArg);
        }

        if (vsym.owner.kind == Kinds.TYP) {
            // It is a member variable
            varRef = makeAttributeAccess(diagPos, vsym, instanceName);
        } else {
            // It is a local variable
            varRef = make.at(diagPos).Ident(vsym);
        }

        Name methName;
        if (bindStatus.isUnidiBind()) {
            methName = defs.locationBindMilieuMethodName[milieu];
        } else if (bindStatus.isBidiBind()) {
            methName = defs.locationBijectiveBindMilieuMethodName[milieu];
        } else {
            methName = defs.locationSetMilieuMethodName[typeKind][milieu];
        }
        return callExpression(diagPos, varRef, methName, valueArg);
    }

    @Override
    public void visitVarScriptInit(JFXVarScriptInit tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXModifiers mods = tree.getModifiers();
        long modFlags = mods.flags | Flags.FINAL;
        VarSymbol vsym = tree.getSymbol();
        JFXVar var = tree.getVar();
        assert vsym.owner.kind == Kinds.TYP : "this should just be for script-level variables";
        assert (modFlags & Flags.STATIC) != 0;
        assert (modFlags & JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC) != 0;
        assert !attrEnv.toplevel.isLibrary;

        result = translateDefinitionalAssignmentToSetExpression(diagPos, var.init, var.getBindStatus(), vsym, null, 0);
    }

    @Override
    public void visitVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXModifiers mods = tree.getModifiers();
        final VarSymbol vsym = tree.getSymbol();
        final VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        assert vsym.owner.kind != Kinds.TYP : "attributes are processed in the class and should never come here";
        final long flags = vsym.flags();
        final boolean requiresLocation = requiresLocation(vsym);
        final boolean isParameter = (flags & Flags.PARAMETER) != 0;
        final boolean hasInnerAccess = (flags & JavafxFlags.VARUSE_INNER_ACCESS) != 0;
        final long modFlags = (mods.flags & ~Flags.FINAL) | ((hasInnerAccess | requiresLocation)? Flags.FINAL : 0L);
        final JCModifiers tmods = make.at(diagPos).Modifiers(modFlags);
        final Type type =
                requiresLocation?
                    (isParameter?
                        vmi.getLocationType() :
                        vmi.getVariableType()) :
                    tree.type;
        final JCExpression typeExpression = makeTypeTree(diagPos, type, true);

        // for class vars, initialization happens during class init, so set to
        // default Location.  For local vars translate as definitional
        JCExpression init;
        if (isParameter) {
            // parameters aren't initialized
            init = null;
            result = make.at(diagPos).VarDef(tmods, tree.name, typeExpression, init);
        } else {
            // create a blank variable declaration and move the declaration to the beginning of the block
            if (requiresLocation) {
                // location types: XXXVariable.make()
                optStat.recordLocalVar(vsym, tree.getBindStatus().isBound(), true);
                init = makeLocationAttributeVariable(vmi, diagPos);
            } else {
                optStat.recordLocalVar(vsym, tree.getBindStatus().isBound(), false);
                if ((modFlags & Flags.FINAL) != 0) {
                    init = translateDefinitionalAssignmentToValue(tree.pos(), tree.init,
                            tree.getBindStatus(), vsym);
                    result = make.at(diagPos).VarDef(tmods, tree.name, typeExpression, init);
                    return;
                }
                // non location types:
                init = makeDefaultValue(diagPos, vmi);
            }
            prependToStatements.append(make.at(Position.NOPOS).VarDef(tmods, tree.name, typeExpression, init));

            // create change Listener and append it to the beginning of the block after the blank variable declaration
            JFXOnReplace onReplace = tree.getOnReplace();
            if ( onReplace != null ) {

                TranslatedVarInfo varInfo = new TranslatedVarInfo(
                                                            tree,
                                                            vmi,
                                                            null,
                                                            tree.getOnReplace(),
                                                            translatedOnReplaceBody(tree.getOnReplace()));
                JCStatement changeListener = initBuilder.makeChangeListenerCall(varInfo);
                prependToStatements.append(changeListener);
            }

            result = translateDefinitionalAssignmentToSetExpression(diagPos, tree.init, tree.getBindStatus(), tree.sym, null, 0);
        }
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should be processed by parent tree";
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should be processed by parent tree";
    }


    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }

   JCExpression makeFunctionValue (JCExpression meth, JFXFunctionDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        if (def != null)
            members.append(translate(def));
        JCExpression encl = null;
        int nargs = mtype.argtypes.size();
        Type ftype = syms.javafx_FunctionTypes[nargs];
        JCExpression t = makeQualifiedTree(null, ftype.tsym.getQualifiedName().toString());
        ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
        Type rtype = syms.boxIfNeeded(mtype.restype);
        typeargs.append(makeTypeTree(diagPos, rtype));
        ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
        int i = 0;
        for (List<Type> l = mtype.argtypes;  l.nonEmpty();  l = l.tail) {
            Name pname = make.paramName(i++);
            Type ptype = syms.boxIfNeeded(l.head);
            JCVariableDecl param = make.VarDef(make.Modifiers(0), pname,
                    makeTypeTree(diagPos, ptype), null);
            params.append(param);
            JCExpression marg = make.Ident(pname);
            margs.append(marg);
            typeargs.append(makeTypeTree(diagPos, ptype));
        }

        // The backend's Attr skips SYNTHETIC methods when looking for a matching method.
        long flags = PUBLIC | BRIDGE; // | SYNTHETIC;

        JCExpression call = make.Apply(null, meth, margs.toList());

        List<JCStatement> stats;
        if (mtype.restype == syms.voidType)
            stats = List.of(make.Exec(call), make.Return(make.Literal(TypeTags.BOT, null)));
        else {
            if (mtype.restype.isPrimitive())
                call = makeBox(diagPos, call, mtype.restype);
            stats = List.<JCStatement>of(make.Return(call));
        }
       JCMethodDecl bridgeDef = make.at(diagPos).MethodDef(
                make.Modifiers(flags),
                defs.invokeName,
                makeTypeTree(diagPos, rtype),
                List.<JCTypeParameter>nil(),
                params.toList(),
                make.at(diagPos).Types(mtype.getThrownTypes()),
                make.Block(0, stats),
                null);

        members.append(bridgeDef);
        JCClassDecl cl = make.AnonymousClassDef(make.Modifiers(0), members.toList());
        List<JCExpression> nilArgs = List.nil();
        return make.NewClass(encl, nilArgs, make.TypeApply(t, typeargs.toList()), nilArgs, cl);
    }

   boolean isInnerFunction (MethodSymbol sym) {
       return sym.owner != null && sym.owner.kind != Kinds.TYP
                && (sym.flags() & Flags.SYNTHETIC) == 0;
   }

   /**
    * Translate JavaFX a class definition into a Java static implementation method.
    */
   @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        if (isInnerFunction(tree.sym)) {
            // If tree's context is not a class, then translate:
            //   function foo(args) { body }
            // to: final var foo = function(args) { body };
            // A probably cleaner and possibly more efficient solution would
            // be to create an anonymous class containing the function as
            // a method; closed-over local variables become fields.
            // That would have the advantage that calling the function directly
            // translates into a direct method call, rather than going via
            // Function's invoke method, which implies extra casts, possibly
            // boxing, etc.  FIXME
            DiagnosticPosition diagPos = tree.pos();
            MethodType mtype = (MethodType) tree.type;
            JCExpression typeExpression = makeTypeTree( diagPos,syms.makeFunctionType(mtype), true);
            JFXFunctionDefinition def = new JFXFunctionDefinition(fxmake.Modifiers(Flags.SYNTHETIC), tree.name, tree.operation);
            def.type = mtype;
            def.sym = new MethodSymbol(0, def.name, mtype, tree.sym.owner.owner);
            JCExpression init =
               makeFunctionValue(make.Ident(tree.name), def, tree.pos(), mtype);
            JCModifiers mods = make.at(diagPos).Modifiers(Flags.FINAL);
            result = make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);
            return;
        }

        boolean isBound = (tree.sym.flags() & JavafxFlags.BOUND) != 0;
        Wrapped prevWrap = wrap;
        wrap = Wrapped.InNothing;

        try {
            boolean classOnly = currentClass.generateClassOnly();
            // Made all the operations public. Per Brian's spec.
            // If they are left package level it interfere with Multiple Inheritance
            // The interface methods cannot be package level and an error is reported.
            long flags = tree.mods.flags;
            long originalFlags = flags;
            flags &= ~Flags.PROTECTED;
            if ((tree.mods.flags & Flags.PRIVATE) == 0)
                flags |=  Flags.PUBLIC;
            if (((flags & (Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) && !classOnly) {
                flags |= Flags.STATIC;
            }
            flags &= ~Flags.SYNTHETIC;
            JCModifiers mods = make.Modifiers(flags);
            final boolean isRunMethod = syms.isRunMethod(tree.sym);
            final boolean isImplMethod = (originalFlags & (Flags.STATIC | Flags.ABSTRACT | Flags.SYNTHETIC)) == 0L && !isRunMethod && !classOnly;

            DiagnosticPosition diagPos = tree.pos();
            MethodType mtype = (MethodType)tree.type;

            // construct the body of the translated function
            JFXBlock bexpr = tree.getBodyExpression();
            JCBlock body;
            if (bexpr == null) {
                body = null; // null if no block expression
            } else if (isBound) {
                // Build the body of a bound function by treating it as a bound expression
                // TODO: Remove entry in findbugs-exclude.xml if permeateBind is implemented -- it is, so it should be
                JCExpression expr = toBound.translate(bexpr, typeMorpher.varMorphInfo(tree.sym));
                body = asBlock(make.at(diagPos).Return(expr));
            } else if (isRunMethod) {
                // it is a module level run method, do special translation
                body = makeRunMethodBody(bexpr);
            } else {
                // the "normal" case
                body = asBlock(translateExpression(bexpr, mtype.getReturnType()));
            }

            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            if ((originalFlags & (Flags.STATIC | Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) {
                if (classOnly) {
                    // all implementation code is generated assuming a receiver parameter.
                    // in the final class case, there is no receiver param, so allow generated code
                    // to function by adding:   var receiver = this;
                    body.stats = body.stats.prepend(
                            make.at(diagPos).VarDef(
                            make.at(diagPos).Modifiers(Flags.FINAL),
                            defs.receiverName,
                            make.Ident(initBuilder.interfaceName(currentClass)),
                            make.at(diagPos).Ident(names._this))
                            );
                } else {
                    // if we are converting a standard instance function (to a static method), the first parameter becomes a reference to the receiver
                    params.prepend(makeReceiverParam(currentClass));
                }
            }
            for (JFXVar fxVar : tree.getParams()) {
                JCVariableDecl var = (JCVariableDecl)translate(fxVar);
                params.append(var);
            }

            mods = addAccessAnnotationModifiers(diagPos, tree.mods.flags, mods);

            result = make.at(diagPos).MethodDef(
                    mods,
                    functionName(tree.sym, isImplMethod,  isBound),
                    makeReturnTypeTree(diagPos, tree.sym, isBound),
                    make.at(diagPos).TypeParams(mtype.getTypeArguments()),
                    params.toList(),
                    make.at(diagPos).Types(mtype.getThrownTypes()),  // makeThrows(diagPos), //
                    body,
                    null);
            ((JCMethodDecl)result).sym = tree.sym;
            result.type = tree.type;
        }
        finally {
            wrap = prevWrap;
        }
    }

    public void visitBlockExpression(JFXBlock tree) {
        visitBlockExpression(tree, tree.type);
    }

    public void visitBlockExpression(JFXBlock tree, Type pt) {
        DiagnosticPosition diagPos = tree.pos();
        ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
        prependToStatements = ListBuffer.lb();

        JFXExpression value = tree.value;
        ListBuffer<JCStatement> translatedStmts = ListBuffer.lb();
        for(JFXExpression expr : tree.getStmts()) {
                JCStatement stmt = translateExpressionToStatement(expr);
                if (stmt != null) {
                    translatedStmts.append(stmt);
                }
        }
        List<JCStatement> localDefs = translatedStmts.toList();

        if (yield == Yield.ToExpression) {
            // make into block expression
            assert (tree.type != syms.voidType) : "void block expressions should be handled below";
            //TODO: this may be unneeded, or even wrong
            if (value.getFXTag() == JavafxTag.RETURN) {
                value = ((JFXReturn) value).getExpression();
            }
            JCExpression tvalue = translate(value, pt); // must be before prepend
            localDefs = prependToStatements.appendList(localDefs).toList();
            result = makeBlockExpression(tree.pos(), localDefs, tvalue);  //TODO: tree.flags lost
        } else {
            // make into block
            prependToStatements.appendList(localDefs);
            if (value != null) {
                JCStatement stmt = translateExpressionToYield(value);
                if (stmt != null) {
                    prependToStatements.append(stmt);
                }
            }
            localDefs = prependToStatements.toList();
            result = localDefs.size() == 1? localDefs.head : make.at(diagPos).Block(0L, localDefs);
        }
        prependToStatements = prevPrependToStatements;
    }

    abstract class AssignTranslator extends Translator {

        protected final JFXExpression lhs;
        protected final JFXExpression rhs;
        protected final Symbol sym;
        protected final JCExpression rhsTranslated;

        AssignTranslator(final DiagnosticPosition diagPos, final JFXExpression lhs, final JFXExpression rhs) {
            super(diagPos, JavafxToJava.this);
            this.lhs = lhs;
            this.rhs = rhs;
            this.rhsTranslated = convertNullability(diagPos, translate(rhs, rhsType()), rhs, rhsType());
            this.sym = expressionSymbol(lhs);
        }

        abstract JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated);

        abstract JCExpression buildRHS(JCExpression rhsTranslated);

        /**
         * Override to change the translation type of the right-hand side
         */
        protected Type rhsType() {
            return lhs.type;
        }

        /**
         * Override to change result in the non-default case.
          */
        protected JCExpression postProcess(JCExpression built) {
            return built;
        }

        private JCExpression buildSetter(JCExpression tc, JCExpression rhsComplete) {
            final Name setter = attributeSetterName(sym);
            JCExpression toApply = tc==null? m().Ident(setter) : m().Select(tc, setter);
            return m().Apply(null, toApply, List.of(rhsComplete));
        }

        protected JCTree doit() {
            if (lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
                // set of a sequence element --  s[i]=8, call the sequence set method
                JFXSequenceIndexed si = (JFXSequenceIndexed) lhs;
                JCExpression seq = translate(si.getSequence(), Wrapped.InLocation);
                JCExpression index = translate(si.getIndex());
                JCFieldAccess select = m().Select(seq, defs.setMethodName);
                List<JCExpression> args = List.of(index, buildRHS(rhsTranslated));
                return postProcess(m().Apply(null, select, args));
            } else if (requiresLocation(sym)) {
                // we are setting a var Location, call the set method
                JCExpression lhsTranslated = translate(lhs, Wrapped.InLocation);
                JCFieldAccess setSelect = m().Select(lhsTranslated, defs.locationSetMethodName[typeMorpher.typeMorphInfo(lhs.type).getTypeKind()]);
                List<JCExpression> setArgs = List.of(buildRHS(rhsTranslated));
                return postProcess(m().Apply(null, setSelect, setArgs));
            } else {
                final boolean useSetters = sym.owner.kind == Kinds.TYP && !sym.isStatic() && types.isJFXClass(sym.owner);

                if (lhs.getFXTag() == JavafxTag.SELECT) {
                    final JFXSelect select = (JFXSelect) lhs;
                    return new NullCheckTranslator(diagPos, select.getExpression(), lhs.type, false) {

                        private final JCExpression rhsTranslatedPreserved = preserveSideEffects(lhs.type, rhs, rhsTranslated);

                        @Override
                        JCExpression translateToCheck( JFXExpression expr) {
                            return translate(expr, Wrapped.InNothing);
                        }

                        @Override
                        JCExpression fullExpression( JCExpression mungedToCheckTranslated) {
                            if (useSetters) {
                                return postProcess(buildSetter(mungedToCheckTranslated, buildRHS(rhsTranslatedPreserved)));
                            } else {
                                //TODO: possibly should use, or be unified with convertVariableReference
                                JCFieldAccess fa = m().Select(mungedToCheckTranslated, attributeFieldName(select.sym));
                                return defaultFullExpression(fa, rhsTranslatedPreserved);
                            }
                        }
                    }.doit();
                } else {
                    // not SELECT
                    if (useSetters) {
                        JCExpression recv = makeReceiver(diagPos, sym, attrEnv.enclClass.sym);
                        return postProcess(buildSetter(recv, buildRHS(rhsTranslated)));
                    } else {
                        Wrapped w = wrap;
                        if (w == Wrapped.InNothing)
                            w = Wrapped.InIndexed;
                        return defaultFullExpression(translate(lhs, w), rhsTranslated);
                    }
                }
            }
        }
    }

    @Override
    public void visitAssign(final JFXAssign tree) {
        DiagnosticPosition diagPos = tree.pos();

        if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
            // assignment of a sequence slice --  s[i..j]=8, call the sequence set method
            JFXSequenceSlice si = (JFXSequenceSlice)tree.lhs;
            JCExpression rhs = translate(tree.rhs, si.getSequence().type);
            JCExpression seq = translate(si.getSequence(), Wrapped.InLocation);
            JCExpression firstIndex = translate(si.getFirstIndex());
            JCExpression lastIndex = makeSliceLastIndex(si);
            JCFieldAccess select = make.Select(seq, defs.replaceSliceMethodName);
            List<JCExpression> args = List.of(firstIndex, lastIndex, rhs);
            result = make.at(diagPos).Apply(null, select, args);
        } else {
            result = new AssignTranslator(diagPos, tree.lhs, tree.rhs) {

                JCExpression buildRHS(JCExpression rhsTranslated) {
                    return rhsTranslated;
                }

                JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
                    return m().Assign(lhsTranslated, rhsTranslated);
                }
            }.doit();
        }
    }

    @Override
    public void visitAssignop(final JFXAssignOp tree) {
        result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

            boolean useDurationOperations = types.isSameType(lhs.type, syms.javafx_DurationType);

            @Override
            JCExpression buildRHS(JCExpression rhsTranslated) {
                final JCExpression lhsTranslated = translate(lhs);
                if (useDurationOperations) {
                    JCExpression method = m().Select(lhsTranslated, tree.operator);
                    return m().Apply(null, method, List.<JCExpression>of(rhsTranslated));
                } else {
                    JCExpression ret = m().Binary(getBinaryOp(), lhsTranslated, rhsTranslated);
                    if (!types.isSameType(rhsType(), lhs.type)) {
                        // Because the RHS is a different type than the LHS, a cast may be needed
                        ret = m().TypeCast(lhs.type, ret);
                    }
                    return ret;
                }
            }

            @Override
            protected Type rhsType() {
                switch (tree.getFXTag()) {
                    case MUL_ASG:
                    case DIV_ASG:
                        // Allow for cases like 'k *= 0.5' where k is an Integer or Duration
                        return rhs.type;
                    default:
                        return lhs.type;
                }
            }

            @Override
            JCExpression defaultFullExpression( JCExpression lhsTranslated, JCExpression rhsTranslated) {
                return useDurationOperations?
                    m().Assign(lhsTranslated, buildRHS(rhsTranslated)) :
                    m().Assignop(tree.getOperatorTag(), lhsTranslated, rhsTranslated);
            }

            private int getBinaryOp() {
                switch (tree.getFXTag()) {
                    case PLUS_ASG:
                        return JCTree.PLUS;
                    case MINUS_ASG:
                        return JCTree.MINUS;
                    case MUL_ASG:
                        return JCTree.MUL;
                    case DIV_ASG:
                        return JCTree.DIV;
                    default:
                        assert false : "unexpected assign op kind";
                        return JCTree.PLUS;
                }
            }
        }.doit();
    }

    class SelectTranslator extends NullCheckTranslator {
        protected final Symbol sym;
        protected final boolean isFunctionReference;
        protected final boolean staticReference;
        protected final Name name;

        protected SelectTranslator(JFXSelect tree) {
            super(tree.pos(), tree.getExpression(), tree.type, tree.sym.isStatic());
            sym = tree.sym;
            isFunctionReference = tree.type instanceof FunctionType && tree.sym.type instanceof MethodType;
            staticReference = tree.sym.isStatic();
            name = tree.getIdentifier();
        }

        @Override
        protected JCExpression translateToCheck(JFXExpression expr) {
            // this may or may not be in a LHS but in either
            // event the selector is a value expression
            JCExpression translatedSelected = translate(expr, Wrapped.InNothing);

            if (staticReference) {
                translatedSelected = makeTypeTree(diagPos, types.erasure(sym.owner.type), false);
            }

            return translatedSelected;
        }

        @Override
        protected JCExpression fullExpression(JCExpression mungedToCheckTranslated) {
            if (isFunctionReference) {
                MethodType mtype = (MethodType) sym.type;
                JCExpression tc = staticReference?
                    mungedToCheckTranslated :
                    addTempVar(toCheck.type, mungedToCheckTranslated);
                JCExpression translated = m().Select(tc, name);
                return makeFunctionValue(translated, null, diagPos, mtype);
            } else {
                JCExpression tc = mungedToCheckTranslated;
                if (toCheck.type != null && toCheck.type.isPrimitive()) {  // expr.type is null for package symbols.
                    tc = makeBox(diagPos, tc, toCheck.type);
                }
                JCFieldAccess translated = make.at(diagPos).Select(tc, name);

                return convertVariableReference(
                        diagPos,
                        translated,
                        sym,
                        (wrap == Wrapped.InLocation));
            }
        }
    }


    public void visitSelect(JFXSelect tree) {
        if (substitute(tree.sym)) {
            return;
        }
        result = new SelectTranslator(tree).doit();
    }

    @Override
    public void visitIdent(JFXIdent tree) {
        DiagnosticPosition diagPos = tree.pos();
        if (substitute(tree.sym)) {
            return;
        }

        if (tree.name == names._this) {
            // in the static implementation method, "this" becomes "receiver$"
            JCExpression rcvr = make.at(diagPos).Ident(defs.receiverName);
            if (wrap == Wrapped.InLocation) {
                rcvr = makeConstantLocation(diagPos, tree.type, rcvr);
            }
            result = rcvr;
            return;
        } else if (tree.name == names._super) {
            if (types.isCompoundClass(tree.type.tsym)) {
                // "super" become just the class where the static implementation method is defined
                //  the rest of the implementation is in visitFunctionInvocation
                result = make.at(diagPos).Ident(tree.type.tsym.name);
            }
            else {
               JCFieldAccess superSelect = make.at(diagPos).Select(make.at(diagPos).Ident(defs.receiverName), tree.name);
                result = superSelect;
            }
            return;
        }

        int kind = tree.sym.kind;
        if (kind == Kinds.TYP) {
            // This is a class name, replace it with the full name (no generics)
            result = makeTypeTree(diagPos, types.erasure(tree.sym.type), false);
            return;
        }

       // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
       // and possible outer access methods
        JCExpression convert;
        boolean isStatic = tree.sym.isStatic();
        if (isStatic) {
            // make class-based direct static reference:   Foo.x
            convert = make.at(diagPos).Select(makeTypeTree(diagPos, tree.sym.owner.type, false), tree.name);
        } else {
            if ((kind == Kinds.VAR || kind == Kinds.MTH) && tree.sym.owner.kind == Kinds.TYP) {
                // it is a non-static attribute or function class member
                // reference it through the receiver
                JCExpression mRec = makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym);
                convert = make.at(diagPos).Select(mRec, tree.name);
            } else {
                convert = make.at(diagPos).Ident(tree.name);
            }
        }

        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JFXFunctionDefinition def = null; // FIXME
            result = makeFunctionValue(convert, def, tree.pos(), mtype);
            return;
        }

        result = convertVariableReference(diagPos,
                convert,
                tree.sym,
                (wrap == Wrapped.InLocation));
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
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
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = elementType(tree.type);
        UseSequenceBuilder builder = useSequenceBuilder(tree.pos(), elemType, tree.getItems().length());
        stmts.append(builder.makeBuilderVar());
        for (JFXExpression item : tree.getItems()) {
            if (item.getJavaFXKind() != JavaFXKind.NULL_LITERAL) {
                // Insert all non-null elements
                stmts.append(builder.makeAdd(item));
            }
        }
        result = makeBlockExpression(tree.pos(), stmts, builder.makeToSequence());
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression meth = makeQualifiedTree(
                diagPos, tree.isExclusive()?
                    sequencesRangeExclusiveString :
                    sequencesRangeString);
        ListBuffer<JCExpression> args = ListBuffer.lb();
        List<JCExpression> typeArgs = List.nil();
        args.append( translate( tree.getLower() ));
        args.append( translate( tree.getUpper() ));
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull() ));
        }
        result = make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        if (types.isSequence(tree.type)) {
            Type elemType = elementType(tree.type);
            result = accessEmptySequence(tree.pos(), elemType);
        }
        else
            result = make.at(tree.pos).Literal(TypeTags.BOT, null);
    }

    public JCExpression translateSequenceExpression (JFXExpression seq) {
        Wrapped w;
        if (types.isSequence(seq.type) &&
                (seq instanceof JFXIdent || seq instanceof JFXSelect)) {
            w = Wrapped.InIndexed;
        } else
            w = Wrapped.InNothing;
        return translate(seq, w);
    }

    @Override
    public void visitSequenceIndexed(final JFXSequenceIndexed tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression seq = tree.getSequence();
        JCExpression index = makeTypeCast(diagPos, syms.intType, tree.getIndex().type, translate(tree.getIndex()));
        Wrapped w;
        if (types.isSequence(seq.type) &&
                (seq instanceof JFXIdent || seq instanceof JFXSelect)) {
            Symbol sym = JavafxTreeInfo.symbol(seq);
            // An optimization of translateSequenceExpression - instead of:
            // v.getAsSequenceRaw().get(index) we should generate v.get(index)
            if (sym instanceof VarSymbol && requiresLocation(sym))
                w = Wrapped.InLocation;
            else
                w = Wrapped.InIndexed;
        } else
            w = Wrapped.InNothing;
        JCExpression tseq = translate(seq, w);
        if (seq.type.tag == TypeTags.ARRAY) {
            result = make.at(diagPos).Indexed(tseq, index);
        }
        else {
            JCFieldAccess select = make.at(diagPos).Select(tseq, defs.getMethodName);
            List<JCExpression> args = List.of(index);
            result = make.at(diagPos).Apply(null, select, args);
        }
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seq = translate(tree.getSequence(), Wrapped.InLocation);
        JCExpression firstIndex = translate(tree.getFirstIndex(), syms.intType);
        JCExpression lastIndex = makeSliceLastIndex(tree);
        JCFieldAccess select = make.at(diagPos).Select(seq, defs.getSliceMethodName);
        List<JCExpression> args = List.of(firstIndex, lastIndex);
        result = make.at(diagPos).Apply(null, select, args);
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seqLoc = translate(tree.getSequence(), Wrapped.InLocation);
        JCExpression elem = translate( tree.getElement() );
        Type elemType = tree.getElement().type;
        if (types.isArray(elemType) || types.isSequence(elemType))
            elem = convertTranslated(elem, diagPos, elemType, tree.getSequence().type);
        else
            elem = convertTranslated(elem, diagPos, elemType, elementType(tree.getSequence().type));
        if (tree.getPosition() == null) {
            result = callStatement(diagPos, seqLoc, "insert", elem);
        } else {
            String meth = tree.shouldInsertAfter()? "insertAfter" : "insertBefore";
            result = callStatement(diagPos, seqLoc, meth,
                    List.of(elem, translate(tree.getPosition())));
        }
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JFXExpression seq = tree.getSequence();

        if (tree.getElement() != null) {
            JCExpression seqLoc = translate(seq, Wrapped.InLocation);
            result = callStatement(tree.pos(), seqLoc, "deleteValue", translate(tree.getElement()));
        } else {
            if (seq.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
                // deletion of a sequence element -- delete s[i]
                JFXSequenceIndexed si = (JFXSequenceIndexed) seq;
                JFXExpression seqseq = si.getSequence();
                JCExpression seqLoc = translate(seqseq, Wrapped.InLocation);
                JCExpression index = translate(si.getIndex());
                result = callStatement(tree.pos(), seqLoc, "delete", index);
            } else if (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
                // deletion of a sequence slice --  delete s[i..j]=8
                JFXSequenceSlice slice = (JFXSequenceSlice) seq;
                JFXExpression seqseq = slice.getSequence();
                JCExpression seqLoc = translate(seqseq, Wrapped.InLocation);
                JCExpression first = translate(slice.getFirstIndex());
                JCExpression last = makeSliceLastIndex(slice);
                result = callStatement(tree.pos(), seqLoc, "deleteSlice", List.of(first, last));
            } else if (types.isSequence(seq.type)) {
                JCExpression seqLoc = translate(seq, Wrapped.InLocation);
                result = callStatement(tree.pos(), seqLoc, "deleteAll");
            } else {
                result = make.at(tree.pos()).Exec(
                            make.Assign(
                                translate(seq),
                                make.Literal(TypeTags.BOT, null)));
            }
        }
    }

    /**** utility methods ******/

    JCExpression makeSliceLastIndex(JFXSequenceSlice tree) {
        JCExpression lastIndex = tree.getLastIndex() == null ?
                callExpression(tree,
                    translate(tree.getSequence()),
                    defs.sizeMethodName) :
                translate(tree.getLastIndex(), syms.intType);
        int decr =
                (tree.getEndKind() == SequenceSliceTree.END_EXCLUSIVE ? 1 : 0) +
                (tree.getLastIndex() == null ? 1 : 0);
        if (decr > 0) {
            lastIndex = make.at(tree).Binary(JCTree.MINUS,
                    lastIndex, make.Literal(TypeTags.INT, decr));
        }
        return lastIndex;
    }

    JCMethodDecl makeMainMethod(DiagnosticPosition diagPos, Name className) {
        List<JCStatement> mainStats;
        if (!attrEnv.toplevel.isRunnable) {
            List<JCExpression> newClassArgs = List.<JCExpression>of(make.at(diagPos).Literal(TypeTags.CLASS, className.toString()));
            mainStats = List.<JCStatement>of(make.at(diagPos).Throw(make.at(diagPos).NewClass(null, null, makeIdentifier(diagPos, noMainExceptionString), newClassArgs, null)));
        } else {
            List<JCExpression> emptyExpressionList = List.nil();
            JCExpression classIdent = make.at(diagPos).Ident(className);
            JCExpression classConstant = make.at(diagPos).Select(classIdent, names._class);
            JCExpression commandLineArgs = makeIdentifier(diagPos, "args");
            JCExpression startIdent = makeQualifiedTree(diagPos, JavafxDefs.startMethodString);
            ListBuffer<JCExpression>args = new ListBuffer<JCExpression>();
            args.append(classConstant);
            args.append(commandLineArgs);
            JCMethodInvocation runCall = make.at(diagPos).Apply(emptyExpressionList, startIdent, args.toList());
            mainStats = List.<JCStatement>of(make.at(diagPos).Exec(runCall));
        }
            List<JCVariableDecl> paramList = List.nil();
            paramList = paramList.append(make.at(diagPos).VarDef(make.Modifiers(0),
                    names.fromString("args"),
                    make.at(diagPos).TypeArray(make.Ident(names.fromString("String"))),
                    null));
            JCBlock body = make.Block(0, mainStats);
            return make.at(diagPos).MethodDef(make.Modifiers(Flags.PUBLIC | Flags.STATIC),
                    names.fromString("main"),
                    make.at(diagPos).TypeIdent(TypeTags.VOID),
                    List.<JCTypeParameter>nil(),
                    paramList,
                    makeThrows(diagPos),
                    body,
                    null);
    }

    /** Equivalent to make.at(pos.getStartPosition()) with side effect of caching
     *  pos as make_pos, for use in diagnostics.
     **/
    TreeMaker make_at(DiagnosticPosition pos) {
        return make.at(pos);
    }

    /** Look up a method in a given scope.
     */
    private MethodSymbol lookupMethod(DiagnosticPosition pos, Name name, Type qual, List<Type> args) {
	return rs.resolveInternalMethod(pos, attrEnv, qual, name, args, null);
    }

    /** Look up a constructor.
     */
    private MethodSymbol lookupConstructor(DiagnosticPosition pos, Type qual, List<Type> args) {
	return rs.resolveInternalConstructor(pos, attrEnv, qual, args, null);
    }

    /** Box up a single primitive expression. */
    JCExpression makeBox(DiagnosticPosition diagPos, JCExpression translatedExpr, Type primitiveType) {
	make_at(translatedExpr.pos());
        Type boxedType = types.boxedClass(primitiveType).type;
        JCExpression box;
        if (target.boxWithConstructors()) {
            Symbol ctor = lookupConstructor(translatedExpr.pos(),
                                            boxedType,
                                            List.<Type>nil()
                                            .prepend(primitiveType));
            box = make.Create(ctor, List.of(translatedExpr));
        } else {
            Symbol valueOfSym = lookupMethod(translatedExpr.pos(),
                                             names.valueOf,
                                             boxedType,
                                             List.<Type>nil()
                                             .prepend(primitiveType));
//            JCExpression meth =makeIdentifier(valueOfSym.owner.type.toString() + "." + valueOfSym.name.toString());
            JCExpression meth = make.Select(makeTypeTree( diagPos,valueOfSym.owner.type), valueOfSym.name);
            TreeInfo.setSymbol(meth, valueOfSym);
            meth.type = valueOfSym.type;
            box = make.App(meth, List.of(translatedExpr));
        }
        return box;
    }

    public List<JCExpression> makeThrows(DiagnosticPosition diagPos) {
        return List.of(makeQualifiedTree(diagPos, methodThrowsString));
    }

    UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType, final int initLength) {

        return new UseSequenceBuilder(diagPos, elemType, sequenceBuilderString) {

            JCStatement makeAdd(JFXExpression exprToAdd) {
                JCExpression expr = translate(exprToAdd);
                Type exprType = exprToAdd.type;
                return makeAdd(expr, exprType);
            }

            JCExpression makeConstructorArg() {
                return makeTypeInfo(diagPos, elemType);
            }
            
            JCExpression makeInitialLengthArg() {
                return (initLength != -1)? make.at(diagPos).Literal(Integer.valueOf(initLength)) : null;
            }

            JCExpression makeToSequence() {
                return makeBuilderVarAccess();
            }
        };
    }
    
    UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType) {
        return useSequenceBuilder(diagPos, elemType, -1);
    }


    UseSequenceBuilder useBoundSequenceBuilder(DiagnosticPosition diagPos, Type elemType, final int initLength) {
        return new UseSequenceBuilder(diagPos, elemType, boundSequenceBuilderString) {

            JCStatement makeAdd(JFXExpression exprToAdd) {
                JCExpression expr = toBound.translate(exprToAdd);
                Type exprType = exprToAdd.type;
                return makeAdd(expr, exprType);
            }

            JCExpression makeConstructorArg() {
                return makeTypeInfo(diagPos, elemType);
            }
            
            JCExpression makeInitialLengthArg() {
                return (initLength != -1)? make.at(diagPos).Literal(Integer.valueOf(initLength)) : null;
            }
        };
    }
    
    UseSequenceBuilder useBoundSequenceBuilder(DiagnosticPosition diagPos, Type elemType) {
        return useBoundSequenceBuilder(diagPos, elemType, -1);
    }
    
    abstract class UseSequenceBuilder {
        final DiagnosticPosition diagPos;
        final Type elemType;
        final String seqBuilder;

        Name sbName;

        private UseSequenceBuilder(DiagnosticPosition diagPos, Type elemType, String seqBuilder) {
            this.diagPos = diagPos;
            this.elemType = elemType;
            this.seqBuilder = seqBuilder;
        }

        JCStatement makeBuilderVar() {
            JCExpression builderTypeExpr = makeQualifiedTree(diagPos, seqBuilder);
            List<JCExpression> btargs = List.of(makeTypeTree(diagPos, elemType));
            builderTypeExpr = make.at(diagPos).TypeApply(builderTypeExpr, btargs);

            // Sequence builder temp var name "sb"
            sbName = getSyntheticName("sb");

            JCExpression initialLengthArg = makeInitialLengthArg();
            // Build "sb" initializing expression -- new SequenceBuilder<T>(clazz)
            JCExpression newExpr = make.at(diagPos).NewClass(
                null,                               // enclosing
                List.<JCExpression>nil(),           // type args
                make.at(diagPos).TypeApply(         // class name -- SequenceBuilder<elemType>
                     makeQualifiedTree(diagPos, seqBuilder),
                     List.<JCExpression>of(makeTypeTree(diagPos, elemType))),
                     initialLengthArg == null?
                         List.<JCExpression>of(makeConstructorArg()) :
                         List.<JCExpression>of(initialLengthArg, makeConstructorArg()),  // args
                null                                // empty body
                );

            // Build the sequence builder variable
            return make.at(diagPos).VarDef(
                make.at(diagPos).Modifiers(0L),
                sbName, builderTypeExpr, newExpr);
        }

        JCIdent makeBuilderVarAccess() {
            return make.Ident(sbName);
        }

        abstract JCStatement makeAdd(JFXExpression expr);

        abstract JCExpression makeConstructorArg();
        
        JCExpression makeInitialLengthArg() {
            return null;
        }

        JCStatement makeAdd(JCExpression expr, Type exprType) {
            if (exprType != elemType) {
                if (types.isArray(exprType) || types.isSequence(exprType)) {
                    expr = convertTranslated(expr, diagPos, exprType, types.sequenceType(elemType));
                }
                else {
                    expr = convertTranslated(expr, diagPos, exprType, elemType);
                }
             }
            JCMethodInvocation addCall = make.Apply(
                    List.<JCExpression>nil(),
                    make.at(diagPos).Select(
                        makeBuilderVarAccess(),
                        names.fromString("add")),
                    List.<JCExpression>of(expr));
            return make.at(diagPos).Exec(addCall);
        }

        JCExpression makeToSequence() {
            return make.Apply(
                List.<JCExpression>nil(), // type arguments
                make.at(diagPos).Select(
                    makeBuilderVarAccess(),
                    names.fromString(toSequenceString)),
                List.<JCExpression>nil() // arguments
                );
        }
    }

   JCExpression castFromObject (JCExpression arg, Type castType) {
        if (castType.isPrimitive())
            castType = types.boxedClass(castType).type;
         return make.TypeCast(makeTypeTree( arg.pos(),castType), arg);
    }

    /**
     * JCTrees which can just be copied and trees which sjould not occur
     * */

    @Override
    public void visitBinary(final JFXBinary tree) {
        result = (new Translator( tree.pos(), this ) {

            /**
             * Compare against null
             */
            private JCExpression makeNullCheck(JCExpression targ) {
                return makeEqEq(targ, makeNull());
            }

            //TODO: after type system is figured out, this needs to be revisited
            /**
             * Check if a primitive has the default value for its type.
             */
            private JCExpression makePrimitiveNullCheck(Type argType, JCExpression arg) {
                TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
                JCExpression defaultValue = makeLit(diagPos, tmi.getRealType(), tmi.getDefaultValue());
                return makeEqEq( arg, defaultValue);
            }

            /**
             * Check if a non-primitive has the default value for its type.
             */
            private JCExpression makeObjectNullCheck(Type argType, JCExpression arg) {
                TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
                if (tmi.getTypeKind() == TYPE_KIND_SEQUENCE || tmi.getRealType() == syms.javafx_StringType) {
                    return callRuntime(JavafxDefs.isNullMethodString, List.of(arg));
                } else {
                    return makeNullCheck(arg);
                }
            }

            /*
             * Do a == compare
             */
            private JCExpression makeEqEq(JCExpression arg1, JCExpression arg2) {
                return makeBinary(JCTree.EQ, arg1, arg2);
            }

            private JCExpression makeBinary(int tag, JCExpression arg1, JCExpression arg2) {
                return make.at(diagPos).Binary(tag, arg1, arg2);
            }

            private JCExpression makeNull() {
                return make.at(diagPos).Literal(TypeTags.BOT, null);
            }

            private JCExpression callRuntime(String methNameString, List<JCExpression> args) {
                JCExpression meth = makeQualifiedTree(diagPos, methNameString);
                List<JCExpression> typeArgs = List.nil();
                return m().Apply(typeArgs, meth, args);
            }

            /**
             * Make a .equals() comparison with a null check on the receiver
             */
            private JCExpression makeFullCheck(JCExpression lhs, JCExpression rhs) {
                return callRuntime(JavafxDefs.equalsMethodString, List.of(lhs, rhs));
            }

            /**
             * Return the translation for a == comparision
             */
            private JCExpression translateEqualsEquals() {
                final JCExpression lhs = translate( tree.lhs );
                final JCExpression rhs = translate( tree.rhs );
                final Type lhsType = tree.lhs.type;
                final Type rhsType = tree.rhs.type;

                    // this is an x == y
                    if (lhsType.getKind() == TypeKind.NULL) {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // both are known to be null
                            return make.at(diagPos).Literal(TypeTags.BOOLEAN, 1);
                        } else if (rhsType.isPrimitive()) {
                            // lhs is null, rhs is primitive, do default check
                            return makePrimitiveNullCheck(rhsType, rhs);
                        } else {
                            // lhs is null, rhs is non-primitive, figure out what check to do
                            return makeObjectNullCheck(rhsType, rhs);
                        }
                    } else if (lhsType.isPrimitive()) {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // lhs is primitive, rhs is null, do default check on lhs
                            return makePrimitiveNullCheck(lhsType, lhs);
                        } else if (rhsType.isPrimitive()) {
                            // both are primitive, use ==
                            return makeEqEq(lhs, rhs);
                        } else {
                            // lhs is primitive, rhs is non-primitive, use equals(), but switch them
                            return makeFullCheck(rhs, lhs);
                        }
                    } else {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // lhs is non-primitive, rhs is null, figure out what check to do
                            return makeObjectNullCheck(lhsType, lhs);
                        } else {
                            //  lhs is non-primitive, use equals()
                            return makeFullCheck(lhs, rhs);
                        }
                    }
            }

            /**
             * Translate a binary expressions
             */
            public JCTree doit() {
                //TODO: handle <>
                if (tree.getFXTag() == JavafxTag.EQ) {
                    return translateEqualsEquals();
                } else if (tree.getFXTag() == JavafxTag.NE) {
                    return make.at(diagPos).Unary(JCTree.NOT, translateEqualsEquals());
                }  else {
                    // anything other than == or <>

                    // Duration type operator overloading
                    if ((types.isSameType(tree.lhs.type, syms.javafx_DurationType) ||
                         types.isSameType(tree.rhs.type, syms.javafx_DurationType)) &&
                        tree.operator == null) { // operator check is to try to get a decent error message by falling through if the Duration method isn't matched
                        JFXExpression l = tree.lhs;
                        JFXExpression r = tree.rhs;
                        switch (tree.getFXTag()) {
                        case PLUS:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("add")), List.<JCExpression>of(translate(r)));
                            // lhs.add(rhs);
                        case MINUS:
                            // lhs.sub(rhs);
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("sub")), List.<JCExpression>of(translate(r)));
                        case DIV:
                            // lhs.div(rhs);
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("div")), List.<JCExpression>of(translate(r)));
                        case MUL:
                            // lhs.mul(rhs);
                            if (!types.isSameType(l.type, syms.javafx_DurationType)) {
                                // FIXME This may get side-effects out-of-order.
                                // A simple fix is to use a static Duration.mul(double,Duration).
                                // Another is to use a Block and a temporary.
                                r = l;
                                l = tree.rhs;
                            }
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("mul")), List.<JCExpression>of(translate(r)));
                        case LT:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("lt")), List.<JCExpression>of(translate(r)));
                        case LE:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("le")), List.<JCExpression>of(translate(r)));
                        case GT:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("gt")), List.<JCExpression>of(translate(r)));
                        case GE:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), names.fromString("ge")), List.<JCExpression>of(translate(r)));
                        }
                    }
                    final JCExpression lhs = translate(tree.lhs);
                    final JCExpression rhs = translate(tree.rhs);
                    return makeBinary(tree.getOperatorTag(), lhs, rhs);
                }
            }

        }).doit();
    }

    public void visitBreak(JFXBreak tree) {
        result = make.at(tree.pos).Break(tree.label);
    }

    public void visitCatch(JFXCatch tree) {
        JCVariableDecl param = translate(tree.param);
        JCBlock body = translateBlockExpressionToBlock(tree.body);
        result = make.at(tree.pos).Catch(param, body);
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
    @Override
    public void visitForExpression(JFXForExpression tree) {
        // sub-translation in done inline -- no super.visitForExpression(tree);
        if (yield == Yield.ToExecStatement) {
             result = wrapWithInClause(tree, translateExpressionToStatement(tree.getBodyExpression()));
        } else {
            // body has value (non-void)
            assert tree.type != syms.voidType : "should be handled above";
            DiagnosticPosition diagPos = tree.pos();
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            JCStatement stmt;
            JCExpression value;

            // Compute the element type from the sequence type
            assert tree.type.getTypeArguments().size() == 1;
            Type elemType = elementType(tree.type);

            UseSequenceBuilder builder = useSequenceBuilder(diagPos, elemType);
            stmts.append(builder.makeBuilderVar());

            // Build innermost loop body
            stmt = builder.makeAdd( tree.getBodyExpression() );

            // Build the result value
            value = builder.makeToSequence();
            stmt = wrapWithInClause(tree, stmt);
            stmts.append(stmt);

            if (yield == Yield.ToExpression) {
                    // Build the block expression -- which is what we translate to
                    result = makeBlockExpression(diagPos, stmts, value);
            } else {
                    stmts.append( make.at(tree).Return( value ) );
                    result = make.at(diagPos).Block(0L, stmts.toList());
            }
        }
    }

    //where
    private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
        JCStatement stmt = coreStmt;
        for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)tree.getInClauses().get(inx);
            if (clause.getWhereExpression() != null) {
                stmt = make.at(clause).If( translate( clause.getWhereExpression() ), stmt, null);
            }

            // Build the loop
            //TODO:  below is the simpler version of the loop. Ideally, this should be used in
            // cases where the loop variable does not need to be final.
            /*
            stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    boundTranslate(clause.getVar(), JavafxBindStatus.UNBOUND),
                    translate(clause.getSequenceExpression()),
                    stmt);
             */
            JFXVar var = clause.getVar();
            Name tmpVarName = getSyntheticName(var.getName().toString());
            JCVariableDecl finalVar = make.VarDef(
                    make.Modifiers(Flags.FINAL),
                    var.getName(),
                    makeTypeTree( var,var.type),
                    make.Ident(tmpVarName));
            Name tmpIndexVarName;
            if (clause.getIndexUsed()) {
                Name indexVarName = indexVarName(clause);
                tmpIndexVarName = getSyntheticName(indexVarName.toString());
                JCVariableDecl finalIndexVar = make.VarDef(
                    make.Modifiers(Flags.FINAL),
                    indexVarName,
                    makeTypeTree( var,syms.javafx_IntegerType),
                    make.Unary(JCTree.POSTINC, make.Ident(tmpIndexVarName)));
                stmt = make.Block(0L, List.of(finalIndexVar, finalVar, stmt));
            }
            else {
                tmpIndexVarName = null;
                stmt = make.Block(0L, List.of(finalVar, stmt));
            }

            DiagnosticPosition diagPos = clause.seqExpr;
            if (types.isSequence(clause.seqExpr.type)) {
                // It would be more efficient to move the Iterable.iterator call
                // to a static method, which can also check for null.
                // But that requires expanding the ForeachLoop by hand.  Later.
                JCExpression seq = callExpression(diagPos,
                    makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                    "forceNonNull",
                    List.of(makeTypeInfo(diagPos, var.type),
                        translate(clause.seqExpr)));
                stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    make.VarDef(
                        make.Modifiers(0L),
                        tmpVarName,
                        makeTypeTree( var,var.type, true),
                        null),
                    seq,
                    stmt);
            } else if (types.asSuper(clause.seqExpr.type, syms.iterableType.tsym) != null) {
                stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    make.VarDef(
                        make.Modifiers(0L),
                        tmpVarName,
                        makeTypeTree(var,var.type, true),
                        null),
                    translate(clause.seqExpr),
                    stmt);
            } else {
                // The "sequence" isn't a Sequence.
                // Compile: { var tmp = seq; if (tmp!=null) stmt; }
                if (! var.type.isPrimitive())
                    stmt = make.If(make.Binary(JCTree.NE, make.Ident(tmpVarName),
                                make.Literal(TypeTags.BOT, null)),
                            stmt, null);
                stmt = make.at(diagPos).Block(0,
                    List.of(make.at(diagPos).VarDef(
                        make.Modifiers(0L),
                        tmpVarName,
                        makeTypeTree( var,var.type, true),
                        translate(clause.seqExpr)),
                        stmt));
            }
            if (clause.getIndexUsed()) {
                JCVariableDecl tmpIndexVar =
                        make.VarDef(
                        make.Modifiers(0L),
                        tmpIndexVarName,
                        makeTypeTree( var,syms.javafx_IntegerType),
                        make.Literal(Integer.valueOf(0)));
                stmt = make.Block(0L, List.of(tmpIndexVar, stmt));
            }
        }
        return stmt;
    }

    public void visitIndexof(JFXIndexof tree) {
        final DiagnosticPosition diagPos = tree.pos();
        assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
        JCExpression transIndex = make.at(diagPos).Ident(indexVarName(tree.fname));
        VarSymbol vsym = (VarSymbol)tree.clause.getVar().sym;
        if (requiresLocation(vsym)) {
            // from inside the bind, its a Location, convert to value
            result = getLocationValue(diagPos, transIndex, TYPE_KIND_INT);
        } else {
            // it came from outside of the bind, not a Location
            result = transIndex;
        }
    }

    @Override
    public void visitIfExpression(JFXIfExpression tree) {
        final DiagnosticPosition diagPos = tree.pos();
        JCExpression cond = translate(tree.getCondition());
        JFXExpression trueSide = tree.getTrueExpression();
        JFXExpression falseSide = tree.getFalseExpression();
        if (yield == Yield.ToExpression) {
            JCExpression translatedFalseSide;
            if (falseSide == null) {
                Type trueSideType = tree.getTrueExpression().type;
                switch (trueSideType.tag) {
                    case TypeTags.BOOLEAN:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOOLEAN, 0);
                        break;
                    case TypeTags.INT:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.INT, 0);
                        break;
                    case TypeTags.DOUBLE:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.DOUBLE, 0.0);
                        break;
                    case TypeTags.BOT:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                    case TypeTags.VOID:
                        assert false : "should have been translated";
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                    case TypeTags.CLASS:
                        if (trueSideType == syms.stringType) {
                            translatedFalseSide = make.at(diagPos).Literal(TypeTags.CLASS, "");
                        } else {
                            translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        }
                        break;
                    default:
                        assert false : "what is this type doing here? " + trueSideType;
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                }
            } else {
                translatedFalseSide = translate(falseSide);
            }
            JCExpression translatedTrueSide = convertTranslated(translate(trueSide), trueSide, trueSide.type, tree.type);
            if (falseSide != null) {
                translatedFalseSide = convertTranslated(translatedFalseSide, falseSide, falseSide.type, tree.type);
            } else {
                translatedFalseSide = convertTranslated(translatedFalseSide, null, trueSide.type, tree.type);
            }
            result = make.at(diagPos).Conditional(cond, translatedTrueSide, translatedFalseSide);
        } else {
            result = make.at(diagPos).If(cond,
                    translateExpressionToYield(trueSide),
                    falseSide == null ? null : translateExpressionToYield(falseSide));
        }
    }

    @Override
    public void visitContinue(JFXContinue tree) {
        result = make.at(tree.pos).Continue(tree.label);
    }

    @Override
    public void visitErroneous(JFXErroneous tree) {
        List<? extends JCTree> errs = translateGeneric(tree.getErrorTrees());
        result = make.at(tree.pos).Erroneous(errs);
    }

    @Override
    public void visitReturn(JFXReturn tree) {
        JFXExpression exp = tree.getExpression();
        if (exp == null) {
            result = make.at(tree).Return(null);
        } else {
            result = translateExpression(exp, tree.type);
        }
    }

    @Override
    public void visitParens(JFXParens tree) {
        if (yield == Yield.ToExpression) {
            JCExpression expr = translate(tree.expr);
            result = make.at(tree.pos).Parens(expr);
        } else {
            result = translateExpressionToYield(tree.expr);
        }
    }

    @Override
    public void visitImport(JFXImport tree) {
        //JCTree qualid = straightConvert(tree.qualid);
        //result = make.at(tree.pos).Import(qualid, tree.staticImport);
        assert false : "should be processed by parent tree";
    }

    @Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        JCTree clazz = this.makeTypeTree( tree,tree.clazz.type);
        JCExpression expr = translate(tree.expr);
        if (tree.expr.type.isPrimitive()) {
            expr = this.makeBox(tree.expr.pos(), expr, tree.expr.type);
        }
        if (types.isSequence(tree.expr.type) && ! types.isSequence(tree.clazz.type))
            expr = callExpression(tree.expr,
                    makeQualifiedTree(tree.expr, "com.sun.javafx.runtime.sequence.Sequences"),
                    "getSingleValue", expr);
        result = make.at(tree.pos).TypeTest(expr, clazz);
    }

    @Override
    public void visitTypeCast(final JFXTypeCast tree) {
        final DiagnosticPosition diagPos = tree.pos();
        JCExpression ret = makeTypeCast(diagPos, tree.clazz.type, tree.expr.type, translate(tree.expr));
        result = convertNullability(diagPos, ret, tree.expr, tree.clazz.type);
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = elementType(tree.type);
            result = accessEmptySequence(tree.pos(), elemType);
        } else {
            result = make.at(tree.pos).Literal(tree.typetag, tree.value);
        }
    }

    abstract static class FunctionCallTranslator extends Translator {

        protected final JFXExpression meth;
        protected final JFXExpression selector;
        protected final MethodSymbol msym;
        protected final boolean renameToSuper;
        protected final boolean superToStatic;
        protected final List<Type> formals;
        protected final boolean usesVarArgs;
        protected final boolean useInvoke;
        protected final boolean selectorMutable;
        protected final boolean callBound;
        protected final boolean magicIsInitializedFunction;
        protected final Type returnType;

        FunctionCallTranslator(final JFXFunctionInvocation tree, JavafxToJava toJava) {
            super(tree.pos(), toJava);
            meth = tree.meth;
            returnType = tree.type;
            JFXSelect fieldAccess = meth.getFXTag() == JavafxTag.SELECT ? (JFXSelect) meth : null;
            selector = fieldAccess != null ? fieldAccess.getExpression() : null;
            Symbol sym = toJava.expressionSymbol(meth);
            msym = (sym instanceof MethodSymbol) ? (MethodSymbol) sym : null;
            Name selectorIdName = (selector != null && selector.getFXTag() == JavafxTag.IDENT) ? ((JFXIdent) selector).getName() : null;
            boolean thisCall = selectorIdName == toJava.names._this;
            boolean superCall = selectorIdName == toJava.names._super;
            ClassSymbol csym = toJava.attrEnv.enclClass.sym;

            useInvoke = meth.type instanceof FunctionType;
            Symbol selectorSym = selector != null? toJava.expressionSymbol(selector) : null;
            boolean namedSuperCall =
                    msym != null && !msym.isStatic() &&
                    selectorSym instanceof ClassSymbol &&
                    // FIXME should also allow other enclosing classes:
                    types.isSuperType(selectorSym.type, csym);
            renameToSuper = namedSuperCall && !types.isCompoundClass(csym);
            superToStatic = (superCall || namedSuperCall) && !renameToSuper;
            formals = meth.type.getParameterTypes();
            //TODO: probably move this local to the arg processing
            usesVarArgs = tree.args != null && msym != null &&
                            (msym.flags() & VARARGS) != 0 &&
                            (formals.size() != tree.args.size() ||
                             types.isConvertible(tree.args.last().type,
                                 types.elemtype(formals.last())));

            selectorMutable = msym != null &&
                    !sym.isStatic() && selector != null && !superCall && !namedSuperCall &&
                    !thisCall && !renameToSuper;
            callBound = msym != null && !useInvoke &&
                  ((msym.flags() & JavafxFlags.BOUND) != 0);

            magicIsInitializedFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_IS_INITIALIZED) != 0;
        }
    }

    @Override
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        result = (new FunctionCallTranslator( tree, this ) {

            private Name funcName = null;

            protected JCTree doit() {
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

                return new NullCheckTranslator(diagPos, toCheckOrNull, returnType, knownNonNull) {

                    List<JCExpression> args = determineArgs();

                    JCExpression translateToCheck(JFXExpression expr) {
                        JCExpression trans;
                        if (renameToSuper) {
                            trans = m().Select(makeTypeTree(diagPos, currentClass.sym.type, false), names._super);
                        } else if (superToStatic) {
                            trans = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                        } else if (selector != null && !useInvoke && msym != null && msym.isStatic()) {
                            //TODO: clean this up -- handles referencing a static function via an instance
                            trans = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                        } else {
                            trans = translate(expr);
                            if (expr.type.isPrimitive()) {
                                // Java doesn't allow calls directly on a primitive, wrap it
                                trans = makeBox(diagPos, trans, expr.type);
                            }
                        }
                        return trans;
                    }

                    @Override
                    JCExpression fullExpression( JCExpression mungedToCheckTranslated) {
                        JCExpression tc = mungedToCheckTranslated;
                        if (funcName != null) {
                            // add the selector name back
                            tc = m().Select(tc, funcName);
                        }
                        JCMethodInvocation app =  m().Apply(translateExpressions(tree.typeargs), tc, args);

                        JCExpression full = callBound ? makeBoundCall(app) : app;
                        if (useInvoke) {
                            if (tree.type.tag != TypeTags.VOID) {
                                full = castFromObject(full, tree.type);
                            }
                        }
                        // If we are to yield a Location, and this isn't going to happen as
                        // a return of using a bound call (for example, if this is a Java call)
                        // then convert into a Location
                        if (wrap == Wrapped.InLocation && !callBound && msym != null) {
                            TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(returnType);
                            full = makeUnboundLocation(diagPos, returnTypeInfo, full);
                        }
                        return full;
                    }

                    /**
                     * Compute the translated arguments.
                     */
                    List<JCExpression> determineArgs() {
                        ListBuffer<JCExpression> targs = ListBuffer.lb();
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
                            List<Type> formal = formals;
                            for (JFXExpression arg : tree.args) {
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
                                            targs.append(translate(arg, Wrapped.InLocation));
                                            break;
                                        }
                                    //TODO: handle sequence subclasses
                                    //TODO: use more efficient mechanism (use currently apears rare)
                                    //System.err.println("Not match: " + arg.type + " vs " + formal.head);
                                    // Otherwise, fall-through, presumably a conversion will work.
                                    default: {
                                        targs.append(makeUnboundLocation(
                                                arg.pos(),
                                                typeMorpher.typeMorphInfo(formal.head),
                                                translate(arg, arg.type)));
                                    }
                                }
                                formal = formal.tail;
                            }
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
                                JCExpression targ;
                                if (magicIsInitializedFunction) {
                                    //TODO: in theory, this could have side-effects (but only in theory)
                                    targ = translate(l.head, Wrapped.InLocation);
                                } else {
                                    targ = preserveSideEffects(formal, l.head, translate(l.head, formal));
                                }
                                targs.append(targ);
                            }
                        }
                        return targs.toList();
                    }
                }.doit();
            }


            // This is for calls from non-bound contexts (code for true bound calls is in JavafxToBound)
            JCExpression makeBoundCall(JCExpression applyExpression) {
                JavafxTypeMorpher.TypeMorphInfo tmi = typeMorpher.typeMorphInfo(msym.getReturnType());
                return wrap == Wrapped.InLocation ? applyExpression : callExpression(diagPos,
                        m().Parens(applyExpression),
                        defs.locationGetMethodName[tmi.getTypeKind()]);
            }

        }).doit();
    }

    @Override
    public void visitModifiers(JFXModifiers tree) {
        result = make.at(tree.pos).Modifiers(tree.flags, List.<JCAnnotation>nil());
    }

    @Override
    public void visitSkip(JFXSkip tree) {
        result = make.at(tree.pos).Skip();
    }

    @Override
    public void visitThrow(JFXThrow tree) {
        JCTree expr = translate(tree.expr);
        result = make.at(tree.pos).Throw(expr);
    }

    @Override
    public void visitTry(JFXTry tree) {
        JCBlock body = translateBlockExpressionToBlock(tree.body);
        List<JCCatch> catchers = translateCatchers(tree.catchers);
        JCBlock finalizer = translateBlockExpressionToBlock(tree.finalizer);
        result = make.at(tree.pos).Try(body, catchers, finalizer);
    }

    @Override
    public void visitUnary(final JFXUnary tree) {
        result = (new Translator( tree.pos(), this ) {
            private final JFXExpression expr = tree.getExpression();

            private JCExpression translateForSizeof(JFXExpression expr) {
                return translateSequenceExpression(expr);
            }
            private final JCExpression transExpr =
                    tree.getFXTag() == JavafxTag.SIZEOF && wrap == Wrapped.InNothing &&
                (expr instanceof JFXIdent || expr instanceof JFXSelect) ? translateForSizeof(expr)
                : translate(expr);

            private JCExpression doIncDec(final int binaryOp, final boolean postfix) {
                return (JCExpression) new AssignTranslator(diagPos, expr, fxm().Literal(1)) {

                    @Override
                    JCExpression buildRHS(JCExpression rhsTranslated) {
                        return m().Binary(binaryOp, transExpr, rhsTranslated);
                    }

                    @Override
                    JCExpression defaultFullExpression( JCExpression lhsTranslated, JCExpression rhsTranslated) {
                        return m().Unary(tree.getOperatorTag(), lhsTranslated);
                    }

                    @Override
                    protected JCExpression postProcess(JCExpression built) {
                        if (postfix) {
                            // this is a postfix operation, undo the value (not the variable) change
                            return m().Binary(binaryOp, (JCExpression) built, m().Literal(-1));
                        } else {
                            // prefix operation
                            return built;
                        }
                    }
                }.doit();
            }

            public JCTree doit() {
                switch (tree.getFXTag()) {
                    case SIZEOF:
                        return callExpression(diagPos,
                                makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                                defs.sizeMethodName, transExpr);
                    case REVERSE:
                        return callExpression(diagPos,
                                makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                                "reverse", transExpr);
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
                            return m().Apply(null, m().Select(translate(tree.arg), names.fromString("negate")), List.<JCExpression>nil());
                        }
                    default:
                        return m().Unary(tree.getOperatorTag(), transExpr);
                }
            }
        }).doit();
    }


    @Override
    public void visitWhileLoop(JFXWhileLoop tree) {
        JCStatement body = translateExpressionToStatement(tree.body);
        JCExpression cond = translate(tree.cond);
        result = make.at(tree.pos).WhileLoop(cond, body);
    }

    /******** goofy visitors, most of which should go away ******/

    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should be processed by parent tree";
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

    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should be processed by parent tree";
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }

    /***********************************************************************
     *
     * Utilities
     *
     */

    private JCBlock makeRunMethodBody(JFXBlock bexpr) {
        DiagnosticPosition diagPos = bexpr.pos();
        final JFXExpression value = bexpr.value;
        JCBlock block;
        if (value == null || value.type == syms.voidType) {
            // the block has no value: translate as simple statement and add a null return
            block = asBlock(translateExpressionToStatement(bexpr));
            block.stats = block.stats.append(make.Return(make.at(diagPos).Literal(TypeTags.BOT, null)));
        } else {
            // block has a value, return it
            block = asBlock(translateExpressionToReturn(bexpr));
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

    protected String getSyntheticPrefix() {
        return "jfx$";
    }

    boolean requiresLocation(Symbol sym) {
        if (sym == null) {
            return false;
        }
        return typeMorpher.requiresLocation(sym);
        }

    boolean requiresLocation(VarMorphInfo vmi) {
        return requiresLocation(vmi.getSymbol());
    }

    JCBlock translatedOnReplaceBody(JFXOnReplace onr) {
        return (onr == null)?  null : translateBlockExpressionToBlock(onr.getBody());
        }

    JCExpression convertVariableReference(DiagnosticPosition diagPos,
                                                 JCExpression varRef, Symbol sym,
                                                 boolean wantLocation) {

        JCExpression expr = varRef;

        boolean staticReference = sym.isStatic();
        if (sym instanceof VarSymbol) {
            final VarSymbol vsym = (VarSymbol) sym;
            boolean doNoteShared = false;
            if (wrap == Wrapped.InNothing) {
                Type type = vsym.type;
                if (types.isSequence(type) || type == syms.objectType || type == syms.unknownType)
                    doNoteShared = true;
            }
 
            if (sym.owner.kind == Kinds.TYP && types.isJFXClass(sym.owner)) {
                // this is a reference to a JavaFX class variable
                if (staticReference) {
                    // a script-level (static) variable, direct access with prefix
                    expr = switchName(diagPos, varRef, attributeFieldName(vsym));
                } else {
                    // an instance variable, use get$
                    JCExpression accessFunc = switchName(diagPos, varRef, attributeGetterName(vsym));
                    List<JCExpression> emptyArgs = List.nil();
                    expr = make.at(diagPos).Apply(null, accessFunc, emptyArgs);
                }
            }
            VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
            if (requiresLocation(vsym)) {
                if (wantLocation) {
                    // already in correct form-- leave it
                } else {
                    // non-bind context -- want v1.get()
                    int typeKind = vmi.getTypeKind();
                    Name getMethodName = defs.locationGetMethodName[typeKind];
                    if (typeKind == JavafxVarSymbol.TYPE_KIND_SEQUENCE) {
                        if (doNoteShared)
                            doNoteShared = false;
                        else
                            getMethodName = defs.getAsSequenceRawMethodName;
                    }
                    expr = getLocationValue(diagPos, expr, getMethodName);
                }
            } else {
                // not morphed
                if (wantLocation) {
                    expr = makeUnboundLocation(diagPos, vmi, expr);
                }
            }
            if (doNoteShared) {
                // typeArgs = List.of(makeTypeTree(diagPos, tmi.getRealType(), true));
                JCExpression st = makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences");
                JCExpression fn = make.at(diagPos).Select(st, defs.noteSharedMethodName);
                expr = make.at(diagPos).Apply(null/*typeArgs*/, fn, List.of(expr));
            }
        }
        return expr;
    }
    //where
    private JCExpression switchName(DiagnosticPosition diagPos, JCExpression identOrSelect, Name name) {
        switch (identOrSelect.getTag()) {
            case JCTree.IDENT:
                return make.at(diagPos).Ident(name);
            case JCTree.SELECT:
                return make.at(diagPos).Select(((JCFieldAccess)identOrSelect).getExpression(), name);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Build the AST for accessing the outer member.
     * The accessors might be chained if the member accessed is more than one level up in the outer chain.
     * */
    private JCExpression makeReceiver(DiagnosticPosition pos, Symbol treeSym, Symbol siteOwner) {
        JCExpression ret = null;
        if (treeSym != null && siteOwner != null) {

            ret = make.Ident(defs.receiverName);
            ret.type = siteOwner.type;

            // check if it is in the chain
            if (siteOwner != treeSym.owner) {
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
                        if (types.isSameType(supType, treeSym.owner.type)) {
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
                            ret = callExpression(pos, ret, initBuilder.outerAccessorName);
                            ret.type = siteCursor.type;
                        }

                        if (siteCursor.kind == Kinds.TYP) {
                            numOfOuters--;
                        }
                        siteCursor = siteCursor.owner;
                    }
                }
            }
        }
        return ret;
    }

    private void fillClassesWithOuters(JFXScript tree) {
        class FillClassesWithOuters extends JavafxTreeScanner {
            JFXClassDeclaration currentClass;

            @Override
            public void visitClassDeclaration(JFXClassDeclaration tree) {
                JFXClassDeclaration prevClass = currentClass;
                try {
                    currentClass = tree;
                    super.visitClassDeclaration(tree);
                }
                finally {
                    currentClass = prevClass;
                }
            }

            @Override
            public void visitIdent(JFXIdent tree) {
                super.visitIdent(tree);
                if (currentClass != null && tree.sym.kind != Kinds.TYP) {
                    addOutersForOuterAccess(tree.sym, currentClass.sym);
                }
            }

            @Override // Need this because JavafxTreeScanner is not visiting the args of the JFXInstanciate tree. Starting to visit them generate tons of errors.
            public void visitInstanciate(JFXInstanciate tree) {
                super.visitInstanciate(tree);
                super.scan(tree.getArgs());
            }

            private void addOutersForOuterAccess(Symbol sym, Symbol currentClass) {
                if (sym != null && sym.owner != null && sym.owner.type != null
                        && !sym.isStatic() && currentClass != null) {
                    Symbol outerSym = currentClass;
                    ListBuffer<ClassSymbol> potentialOuters = new ListBuffer<ClassSymbol>();
                    boolean foundOuterOwner = false;
                    while (outerSym != null) {
                        if (outerSym.kind == Kinds.TYP) {
                            ClassSymbol outerCSym = (ClassSymbol) outerSym;
                            if (types.isSuperType(sym.owner.type, outerCSym)) {
                                foundOuterOwner = true;
                                break;
                             }
                            potentialOuters.append(outerCSym);
                        }
                        else if (sym.owner == outerSym)
                            break;
                        outerSym = outerSym.owner;
                    }

                    if (foundOuterOwner) {
                        for (ClassSymbol cs : potentialOuters) {
                            hasOuters.add(cs);
                        }
                    }
                }
            }
        }

        new FillClassesWithOuters().scan(tree);
    }

    @Override
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        //TODO: code should be something like the below, but this requires a similar change to visitInterpolateValue
        /***
           result = makeDurationLiteral(tree.pos(), translate(tree.value));
         */

        // convert this time literal to a javafx.lang.Duration.valueOf() invocation
        JFXFunctionInvocation duration = timeLiteralToDuration(tree); // sets result

        // now convert that FX invocation to Java
        visitFunctionInvocation(duration); // sets result
   }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression clsname = fxmake.at(diagPos).Type(syms.javafx_KeyValueType);
        ((JFXSelect) clsname).sym = syms.javafx_KeyValueType.tsym;
        final Symbol targetSymbol = syms.javafx_KeyValueType.tsym.members().lookup(defs.targetName).sym;
        JFXObjectLiteralPart targetLiteral = fxmake.at(tree.pos()).ObjectLiteralPart(defs.targetName, tree.attribute);
        targetLiteral.sym = targetSymbol;
        JFXObjectLiteralPart valueLiteral =
                fxmake.at(tree.pos()).ObjectLiteralPart(defs.valueName,
                                                        tree.value,
                                                        JavafxBindStatus.UNBOUND);
        valueLiteral.sym = syms.javafx_KeyValueType.tsym.members().lookup(defs.valueName).sym;
        List<JFXTree> parts;
        if (tree.interpolation == null)
            parts = List.<JFXTree>of(targetLiteral, valueLiteral);
        else {
            JFXObjectLiteralPart interpolateLiteral =
                    fxmake.at(tree.pos()).ObjectLiteralPart(defs.interpolateName, tree.interpolation, JavafxBindStatus.UNBOUND);
            interpolateLiteral.sym = syms.javafx_KeyValueType.tsym.members().lookup(defs.interpolateName).sym;
            parts = List.<JFXTree>of(targetLiteral, valueLiteral, interpolateLiteral);
        }

        JFXInstanciate inst = fxmake.at(diagPos).Instanciate(JavaFXKind.INSTANTIATE_OBJECT_LITERAL, clsname, null, parts);
        inst.type = clsname.type;
        result = new InstanciateTranslator(inst, this) {
            protected void processLocalVar(JFXVar var) {
            }

            protected JCStatement translateAttributeSet(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, Name instanceName) {
                if (targetSymbol==vsym) {
                    JCExpression target = translate(init, Wrapped.InLocation);
 //                   target = callExpression(diagPos, make.Type(syms.javafx_PointerType), "make", target);
                    target = callExpression(diagPos, makeExpression(syms.javafx_PointerType), "make", target);
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                    JCExpression localAttr;

                    // if it is an attribute
                    if (vsym.owner.kind == Kinds.TYP) {
                        localAttr = makeAttributeAccess(diagPos, vsym, instanceName);
                    } else {
                        // if it is a local variable
                        assert( (vsym.flags() & Flags.PARAMETER) == 0): "Parameters are not initialized";
                        localAttr = make.at(diagPos).Ident(vsym);
                   }
                   return make.at(diagPos).Exec(callExpression(diagPos, localAttr, defs.locationSetMilieuMethodName[vmi.getTypeKind()][FROM_LITERAL_MILIEU], target));
               } else {
                    return toJava.translateDefinitionalAssignmentToSet(diagPos, init, bindStatus,
                        vsym, instanceName, FROM_LITERAL_MILIEU);
                }
            }
        }.doit();
    }

    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression clsname = fxmake.at(diagPos).Type(syms.javafx_KeyFrameType);
        ((JFXSelect) clsname).sym = syms.javafx_KeyFrameType.tsym;

        final Symbol timeSymbol = syms.javafx_KeyFrameType.tsym.members().lookup(defs.timeName).sym;
        JFXObjectLiteralPart timeLiteral = fxmake.at(diagPos).ObjectLiteralPart(defs.timeName, tree.start);
        timeLiteral.sym = timeSymbol;

        JFXSequenceExplicit values = fxmake.at(diagPos).ExplicitSequence(tree.values);
        values.type = types.sequenceType( fxmake.at(diagPos).Type(syms.javafx_KeyValueType).type);

        JFXObjectLiteralPart valuesLiteral =
                fxmake.at(diagPos).ObjectLiteralPart(defs.valuesName, values);
        valuesLiteral.sym = syms.javafx_KeyFrameType.tsym.members().lookup(defs.valuesName).sym;

        List<JFXTree> parts = List.<JFXTree>of(timeLiteral, valuesLiteral);

        JFXInstanciate inst = fxmake.at(diagPos).Instanciate(JavaFXKind.INSTANTIATE_OBJECT_LITERAL, clsname, null, parts);

        inst.type = clsname.type;

        result = new InstanciateTranslator(inst, this) {
            protected void processLocalVar(JFXVar var) {
            }

            protected JCStatement translateAttributeSet(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, Name instanceName) {
                return toJava.translateDefinitionalAssignmentToSet(diagPos, init, bindStatus,
                        vsym, instanceName, FROM_LITERAL_MILIEU);
            }
        }.doit();
    }
}
