/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxToJava.UseSequenceBuilder;
import com.sun.tools.javafx.comp.JavafxToJava.Translator;
import com.sun.tools.javafx.comp.JavafxToJava.FunctionCallTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.InstanciateTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.StringExpressionTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.TypeCastTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.Wrapped;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

public class JavafxToBound extends JCTree.Visitor implements JavafxVisitor {
    protected static final Context.Key<JavafxToBound> jfxToBoundKey =
        new Context.Key<JavafxToBound>();

    /*
     * the result of translating a tree by a visit method
     */
    JCExpression result;

    /*
     * modules imported by context
     */
    private final JavafxToJava toJava;
    private final JavafxTreeMaker make;  // should be generating Java AST, explicitly cast when not
    private final Log log;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxSymtab syms;
    private final JavafxTypeMorpher typeMorpher;
    private final JavafxDefs defs;
    private final JavafxResolve rs;
    
    /*
     * other instance information
     */
    private final Symbol doubleObjectTypeSymbol;
    private final Symbol intObjectTypeSymbol;
    private final Symbol booleanObjectTypeSymbol;
    private final Name selectorParamName;


    private JavafxEnv<JavafxAttrContext> attrEnv;
    private Target target;

    /*
     * static information
     */

    static final boolean generateBoundFunctions = true;
    static final boolean generateBoundVoidFunctions = false;
    static final boolean permeateBind = false;
    
    private static final String cBoundSequences = sequencePackageName + "BoundSequences";
    private static final String cBoundOperators = locationPackageName + "BoundOperators";
    
    /** Class symbols for classes that need a reference to the outer class. */
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();
        
    public static JavafxToBound instance(Context context) {
        JavafxToBound instance = context.get(jfxToBoundKey);
        if (instance == null)
            instance = new JavafxToBound(context);
        return instance;
    }

    protected JavafxToBound(Context context) {
        context.put(jfxToBoundKey, this);

        toJava = JavafxToJava.instance(context);
        make = JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        target = Target.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
        
        doubleObjectTypeSymbol = types.boxedClass(syms.doubleType).type.tsym;
        intObjectTypeSymbol = types.boxedClass(syms.intType).type.tsym;
        booleanObjectTypeSymbol = types.boxedClass(syms.booleanType).type.tsym;
        
        selectorParamName = names.fromString("select$");
    }
    
    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends JCExpression> T translate(T tree) {
        T ret;
	if (tree == null) {
	    ret = null;
	} else {
	    tree.accept(this);
	    ret = (T)this.result;
	    this.result = null;
	}
        return ret;
    }
    
    private JCVariableDecl translateVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();

        JCModifiers mods = tree.getModifiers();
        long modFlags = mods == null ? 0L : mods.flags;
        modFlags |= Flags.FINAL;  // Locations are never overwritte
        mods = make.at(diagPos).Modifiers(modFlags);
        
        VarMorphInfo vmi = typeMorpher.varMorphInfo(tree.sym);
        JCExpression typeExpression = toJava.makeTypeTree(vmi.getLocationType(), diagPos, true);

        //TODO: handle array initializers (but, really, shouldn't that be somewhere else?)
        JCExpression init = translate(tree.init);

        return make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);
    }

    private JCStatement definitionalAssignmentToSet(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            JCExpression instance, int milieu) {
        return make.at(diagPos).Exec( definitionalAssignmentToSetExpression(diagPos,
            init, bindStatus, vsym,
             instance, milieu) );
    }

    private JCExpression definitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            JCExpression instance, int milieu) {
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        JCExpression nonNullInit = (init == null)? toJava.makeDefaultValue(diagPos, vmi) : init;
        List<JCExpression> args = List.<JCExpression>of( nonNullInit );
        JCExpression localAttr;
        if ((vsym.flags() & STATIC) != 0) {
            // statics are accessed directly
            localAttr = make.Ident(vsym);
        } else {
            String attrAccess = attributeGetMethodNamePrefix + vsym;
            localAttr = callExpression(diagPos, instance, attrAccess);
        }
        Name methName;
        if (bindStatus.isUnidiBind()) {
            methName = defs.locationBindMilieuMethodName[milieu];
        } else if (bindStatus.isBidiBind()) {
            methName = defs.locationBijectiveBindMilieuMethodName[milieu];
        } else {
            methName = defs.locationSetMilieuMethodName[vmi.getTypeKind()][milieu];
        }
        return callExpression(diagPos, localAttr, methName, args);
    }

    private abstract class ClosureTranslator extends Translator {
        
        protected final TypeMorphInfo tmiResult;
        protected final int typeKindResult;
        protected final Type elementTypeResult;

        // these only used when fields are built
        ListBuffer<JCTree> members = ListBuffer.lb();
        ListBuffer<JCExpression> dependents = ListBuffer.lb();
        ListBuffer<JCExpression> callArgs = ListBuffer.lb();
        int argNum = 0;

        ClosureTranslator(DiagnosticPosition diagPos, Type resultType) {
            this(diagPos, JavafxToBound.this.toJava, typeMorpher.typeMorphInfo(resultType) );
        }
        
        ClosureTranslator(DiagnosticPosition diagPos, JavafxToJava toJava, TypeMorphInfo tmiResult) {
            super(diagPos, toJava);
            this.tmiResult = tmiResult;
            typeKindResult = tmiResult.getTypeKind();
            elementTypeResult = toJava.elementType(tmiResult.getLocationType()); // want boxed, JavafxTypes version won't work
        }
        
        /**
         * Make a method paramter
         */
        protected JCVariableDecl makeParam(Type type, Name name) {
            return make.at(diagPos).VarDef(
                    m().Modifiers(Flags.PARAMETER | Flags.FINAL),
                    name,
                    makeExpression(type),
                    null);
        }
        
        protected JCExpression makeGet(JCExpression locExpr, TypeMorphInfo tmi) {
            Name getMethodName = defs.locationGetMethodName[tmi.getTypeKind()];
            JCFieldAccess select = m().Select(locExpr, getMethodName);
            return m().Apply(null, select, List.<JCExpression>nil());
        }
        
        protected JCExpression makeGetField(Name fieldName, TypeMorphInfo tmiField) {
            return makeGet(m().Ident(fieldName), tmiField);
        }
        
        protected JCTree makeLocationField(JCExpression targ, Name argName, TypeMorphInfo tmiArg) {
            return m().VarDef(
                    m().Modifiers(Flags.FINAL | Flags.PRIVATE),
                    argName,
                    makeExpression(tmiArg.getLocationType()),
                    targ);
        }

        protected JCTree makeClosureMethod(String methName, JCExpression expr, List<JCVariableDecl> params, Type returnType, long flags) {
            return m().MethodDef(
                    m().Modifiers(flags),
                    names.fromString(methName),
                    makeExpression(returnType),
                    List.<JCTypeParameter>nil(),
                    params==null? List.<JCVariableDecl>nil() : params,
                    List.<JCExpression>nil(),
                    m().Block(0L, List.<JCStatement>of(m().Return(expr))),
                    null);
        }
        
        protected JCTree makeClosureMethod(String methName, JCExpression expr, List<JCVariableDecl> params) {
            return makeClosureMethod(methName, expr, params, tmiResult.getLocationType(), Flags.PROTECTED);
        }
        
        protected abstract List<JCTree> makeBody();
        
        protected abstract JCExpression makeBaseClass();

        protected JCExpression makeBaseClass(Type clazzType, Type additionTypeParamOrNull) {
            JCExpression clazz = makeExpression(types.erasure(clazzType));  // type params added below, so erase formals
            ListBuffer<JCExpression> typeParams = ListBuffer.lb();

            if (typeKindResult == TYPE_KIND_OBJECT || typeKindResult == TYPE_KIND_SEQUENCE) {
                typeParams.append(makeExpression(elementTypeResult));
            }
            if (additionTypeParamOrNull != null) {
                typeParams.append(makeExpression(additionTypeParamOrNull));
            }
            return m().TypeApply(clazz, typeParams.toList());
        }

        protected abstract List<JCExpression> makeConstructorArgs();
        
        protected JCExpression buildClosure() {
            JCClassDecl classDecl = m().AnonymousClassDef(m().Modifiers(0L), makeBody());
            List<JCExpression> typeArgs = List.nil();
            return m().NewClass(null/*encl*/, typeArgs, makeBaseClass(), makeConstructorArgs(), classDecl);
        }

        protected JCExpression doit() {
            return buildClosure();
        }      
        
        
        // field building support
        
        protected JCExpression buildArgField(JCExpression arg) {
            return buildArgField(arg, false);
        }

        protected JCExpression buildArgField(JCExpression arg, boolean isBound) {
            return buildArgField(arg, "arg$" + argNum++, isBound);
        }

        protected JCExpression buildArgField(JCExpression arg, String argLabel) {
            return buildArgField(arg, argLabel, false);
        }

        protected JCExpression buildArgField(JCExpression arg, String argLabel, boolean isBound) {
                TypeMorphInfo tmiArg = typeMorpher.typeMorphInfo(arg.type);
                Name argName = names.fromString(argLabel);

                // translate the method arg into a Location field of the BindingExpression
                // XxxLocation arg$0 = ...;
                members.append(makeLocationField(translate(arg), argName, tmiArg));
             
            // build a list of these args, for use as dependents -- arg$0, arg$1, ...
            if (isBound) {
                return m().Ident(argName);
            } else {
                dependents.append(m().Ident(argName));

                // set up these arg for the call -- arg$0.getXxx()
                return makeGetField(argName, tmiArg);
            }
         }
        
        protected void buildArgFields(List<JCExpression> args, boolean isBound) {
            for (JCExpression arg : args) {
                callArgs.append( buildArgField(arg, isBound) );
            }
        }
        

    }

    @Override
    public void visitInstanciate(final JFXInstanciate tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression resultValue() {
                return new InstanciateTranslator(tree, toJava) {

                    protected JCStatement translateLocalVar(JFXVar var) {
                        return translateVar(var);
                    }

                    protected List<JCExpression> translatedConstructorArgs() {
                        buildArgFields(tree.getArgs(), false);  
                        return callArgs.toList();
                    }

                    protected JCStatement translateAttributeSet(JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, JCExpression instance) {
                        JCExpression initRef = buildArgField(init, vsym.name.toString() + "$attr", bindStatus.isBound());
                        return definitionalAssignmentToSet(diagPos, initRef, bindStatus,
                                vsym, instance, FROM_LITERAL_MILIEU);
                    }
                }.doit();
            }
        }.doit();
    }
    
    @Override
    public void visitStringExpression(final JFXStringExpression tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), syms.stringType) {

            protected JCExpression resultValue() {
                return new StringExpressionTranslator(tree, toJava) {

                    protected JCExpression translateArg(JCExpression arg) {
                        return buildArgField(arg);
                    }
                }.doit();
            }
        }.doit();
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = toJava.makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }
        
    public void visitBlockExpression(JFXBlockExpression tree) {   //done
        assert (tree.type != syms.voidType) : "void block expressions should be not exist in bind expressions";
        DiagnosticPosition diagPos = tree.pos();

        JCExpression value = tree.value;
        ListBuffer<JCStatement> translatedVars = ListBuffer.lb();
        
        for (JCStatement stmt : tree.getStatements()) {
            switch (stmt.getTag()) {
                case JavafxTag.RETURN:
                    assert value == null;
                    value = ((JCReturn) stmt).getExpression();
                    break;
                case JavafxTag.VAR_DEF:
                    translatedVars.append(translateVar((JFXVar) stmt));
                    break;
                default:
                    log.error(diagPos, "javafx.not.allowed.in.bind.context", stmt.toString());
                    break;
            }
        }
        result = ((JavafxTreeMaker) make).at(diagPos).BlockExpression(tree.flags, 
                translatedVars.toList(), 
                translate(value) );
    }
 
    @Override
    public void visitAssign(JCAssign tree) {
        log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "=");
    }

    @Override
    public void visitAssignop(JCAssignOp tree) {
        log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "=");
    }
    
    abstract class SelectClosureTranslator extends ClosureTranslator {

        final TypeMorphInfo tmiSelector;
        final JCExpression selectingExpr;

        SelectClosureTranslator(DiagnosticPosition diagPos, 
                TypeMorphInfo tmiResult,
                TypeMorphInfo tmiSelector,
                JCExpression selectingExpr) {
            super(diagPos, JavafxToBound.this.toJava, tmiResult);
            this.tmiSelector = tmiSelector;
            this.selectingExpr = selectingExpr;
        }
        
        protected JCTree makeComputeSelectMethod(JCExpression expr) {
            List<JCVariableDecl> params = List.of(makeParam(tmiSelector.getRealType(), selectorParamName));
            return makeClosureMethod("computeSelect", expr, params);
        }

        abstract protected List<JCTree> makeBody();

        protected JCExpression makeBaseClass() {
            Type clazzType = tmiResult.getBoundSelectLocationType();
            return makeBaseClass(clazzType, tmiSelector.getRealType());
        }

        protected List<JCExpression> makeConstructorArgs() {
            ListBuffer<JCExpression> constructorArgs = ListBuffer.lb();
            if (typeKindResult == TYPE_KIND_OBJECT || typeKindResult == TYPE_KIND_SEQUENCE) {
                // prepend "Foo.class, "
                constructorArgs.append(makeDotClass(diagPos, elementTypeResult));
            }
            constructorArgs.append(selectingExpr);
            return constructorArgs.toList();
        }
    }

    @Override
    public void visitSelect(final JCFieldAccess tree) {
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            result = toJava.translate(tree, Wrapped.InLocation); //TODO -- for now punt, translate like normal case
            return;
        }
        DiagnosticPosition diagPos = tree.pos();
        JCExpression expr = tree.getExpression();
        JCExpression translatedExpr = translate(expr);

        if (tree.sym.isStatic()) {
            // if this is a static reference, eg.   MyClass.myAttribute
            translatedExpr = makeTypeTree(types.erasure(tree.sym.owner.type), diagPos, false);
            result = make.at(diagPos).Select(translatedExpr, tree.getIdentifier());
        } else {
            result = new SelectClosureTranslator(diagPos,
                    typeMorpher.typeMorphInfo(tree.type),
                    typeMorpher.typeMorphInfo(expr.type),
                    translatedExpr) {

                protected List<JCTree> makeBody() {
                    JCExpression expr = typeMorpher.convertVariableReference(diagPos, //TODO: don't use this
                            m().Select(m().Ident(selectorParamName), tree.getIdentifier()),
                            tree.sym,
                            true,
                            false);
                    return List.<JCTree>of(makeComputeSelectMethod(expr));
                }
            }.doit();
        }
    }
    
    @Override
    public void visitIdent(JCIdent tree)   {  //TODO: this, super, ...
        assert (tree.sym.flags() & Flags.PARAMETER) != 0 || tree.name==names._this || toJava.shouldMorph(typeMorpher.varMorphInfo(tree.sym)) : "we are bound, so should have been marked to morph: " + tree;
        result = toJava.translate(tree, Wrapped.InLocation);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) { //done
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = toJava.elementType(tree.type);
        UseSequenceBuilder builder = toJava.useBoundSequenceBuilder(tree.pos(), elemType);
        stmts.append(builder.makeBuilderVar());
        for (JCExpression item : tree.getItems()) {
            stmts.append(builder.makeAdd( item ) );
        }
        result = toJava.makeBlockExpression(tree.pos(), stmts, builder.makeToSequence());
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange tree) { //done: except for step and exclusive
        DiagnosticPosition diagPos = tree.pos();
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append( translate( tree.getLower() ));
        args.append( translate( tree.getUpper() )); 
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull() ));
        }
        if (tree.isExclusive()) {
            args.append( make.at(diagPos).Literal(TypeTags.BOOLEAN, 1) );
        }
        result = runtime(diagPos, cBoundSequences, "range", args);
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) { //done
        DiagnosticPosition diagPos = tree.pos();
        if (types.isSequence(tree.type)) {
            Type elemType = types.elementType(tree.type);
            result = runtime(diagPos, cBoundSequences, "empty", List.of(makeDotClass(diagPos, elemType)));
        } else {
            JCExpression unbound = make.at(diagPos).Literal(TypeTags.BOT, null); 
            result = makeConstantLocation(diagPos, tree.type, unbound);
        }
    }
        
    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {   //done
        result = runtime(tree.pos(), cBoundSequences, "element",
                List.of(translate(tree.getSequence()),
                translate(tree.getIndex())));
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {    //done
        DiagnosticPosition diagPos = tree.pos();
        result = runtime(diagPos, cBoundSequences, "slice",
                List.of(
                    toJava.makeTypeTree(types.elementType(tree.type), diagPos, true),
                    translate(tree.getSequence()),
                    translate(tree.getFirstIndex()),
                    translate(tree.getLastIndex())
                    ));
        //TODO:   SequenceSliceTree.END_EXCLUSIVE
    }
    
    /**
     * Generate this template, expanding to handle multiple in-clauses
     * 
     *  SequenceLocation<V> derived = new BoundComprehension<T,V>(V.class, IN_SEQUENCE, USE_INDEX) {
            protected SequenceLocation<V> getMappedElement$(final ObjectLocation<T> IVAR_NAME, final IntLocation INDEXOF_IVAR_NAME) {
                return SequenceVariable.make(V.class,
                                             new SequenceBindingExpression<V>() {
                                                 public Sequence<V> computeValue() {
                                                     if (WHERE)
                                                         return BODY with s/indexof IVAR_NAME/INDEXOF_IVAR_NAME/;
                                                     else
                                                        return Sequences.emptySequence(V.class);
                                                 }
                                             }, maybe IVAR_NAME, maybe INDEXOF_IVAR_NAME);
            }
        };
     *
     * **/
    @Override
    public void visitForExpression(final JFXForExpression tree) {
        result = (new Translator( tree.pos(), toJava ) {

            private final TypeMorphInfo tmiResult = typeMorpher.typeMorphInfo(tree.type);
            
            /**
             * V
             */
            private final Type resultElementType = tmiResult.getElementType();
            
            /**
             * SequenceLocation<V>
             */
            private final Type resultSequenceLocationType = typeMorpher.generifyIfNeeded(typeMorpher.locationType(TYPE_KIND_SEQUENCE), tmiResult);
            
            /**
             * Make:  V.class
             */
            private JCExpression makeResultClass() {
                return makeDotClass(diagPos, resultElementType);
            }
            
            /**
             * Make a method paramter
             */
            private JCVariableDecl makeParam(Type type, Name name) {
                return make.at(diagPos).VarDef(
                        make.Modifiers(Flags.PARAMETER | Flags.FINAL),
                        name,
                        makeExpression(type),
                        null);

            }

            /**
             * Starting with the body of the comprehension...
             * Wrap in a singleton sequence, if not a sequence.
             * Wrap in a conditional if there are where-clauses:   whereClause? body : []
             */
            private JCExpression makeCore() {
                JCExpression body = translate(tree.getBodyExpression());
                if (!types.isSequence(tree.getBodyExpression().type)) {
                    List<JCExpression> args = List.of(makeResultClass(), body);
                    body = runtime(diagPos, cBoundSequences, "singleton", args);
                }
                JCExpression whereTest = null;
                for (JFXForExpressionInClause clause : tree.getForExpressionInClauses()) {
                    JCExpression where = translate(clause.getWhereExpression());
                    if (where != null) {
                        if (whereTest == null) {
                            whereTest = where;
                        } else {
                            whereTest = runtime(diagPos, cBoundOperators, "and_bb", List.of(whereTest, where));
                        }
                    }
                }
                if (whereTest != null) {
                    body = makeBoundConditional(diagPos,
                            tmiResult,
                            body,
                            runtime(diagPos, cBoundSequences, "empty", List.of(makeDotClass(diagPos, resultElementType))),
                            whereTest);
                }
                return body;
            }
             
            /**  
             *  protected SequenceLocation<V> getMappedElement$(final ObjectLocation<T> IVAR_NAME, final IntLocation INDEXOF_IVAR_NAME) {
             *     return ...
             *  }
             */
            private JCTree makeGetMappedElementMethod(JFXForExpressionInClause clause, JCExpression inner, TypeMorphInfo tmiInduction) {
                Type objLocType = tmiInduction.getLocationType();
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                Name ivarName = clause.getVar().name;
                stmts.append(m().Return( inner ));
                List<JCVariableDecl> params = List.of( 
                        makeParam(objLocType, ivarName),
                        makeParam(typeMorpher.locationType(TYPE_KIND_INT), indexofMangledName( clause.getVar().sym) )
                        );
                return m().MethodDef(
                        m().Modifiers(Flags.PROTECTED),
                        names.fromString("getMappedElement$"),
                        makeExpression( resultSequenceLocationType ),
                        List.<JCTypeParameter>nil(),
                        params,
                        List.<JCExpression>nil(),
                        m().Block(0L, stmts.toList()),
                        null);
            }

            /**
             * new BoundComprehension<T,V>(V.class, IN_SEQUENCE, USE_INDEX) { ... }
             */
            private JCExpression makeBoundComprehension(JFXForExpressionInClause clause, JCExpression inner) {
                JCExpression seq = clause.getSequenceExpression();
                TypeMorphInfo tmiSeq = typeMorpher.typeMorphInfo(seq.type);
                TypeMorphInfo tmiInduction = typeMorpher.typeMorphInfo(clause.getVar().type);
                Type elementType = tmiSeq.getElementType();
                JCClassDecl classDecl = m().AnonymousClassDef(
                        m().Modifiers(0L), 
                        List.<JCTree>of(makeGetMappedElementMethod(clause, inner, tmiInduction)));
                List<JCExpression> typeArgs = List.nil();
                boolean useIndex = clause.getIndexUsed(); 
                List<JCExpression> constructorArgs = List.of(
                        makeResultClass(),
                        translate( seq ),
                        m().Literal(TypeTags.BOOLEAN, useIndex? 1 : 0) );
                //JCExpression clazz = toJava.makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.BoundComprehension");
                int typeKind = tmiInduction.getTypeKind();
                Type bcType = typeMorpher.boundComprehensionNCT[typeKind].type;
                JCExpression clazz = makeExpression(types.erasure(bcType));  // type params added below, so erase formals
                ListBuffer<JCExpression> typeParams = ListBuffer.lb();
                if (typeKind == TYPE_KIND_OBJECT) {
                    typeParams.append( makeExpression(elementType) );
                }
                typeParams.append( makeExpression(resultElementType) );
                clazz = m().TypeApply(clazz, typeParams.toList());
                return m().NewClass(null, 
                        typeArgs, 
                        clazz, 
                        constructorArgs, 
                        classDecl);
            }
            
            /**
             * Put everything together, handle multiple in clauses -- wrap from the inner-most first
             */
            public JCExpression doit() {
                JCExpression expr = makeCore();
                for (int inx = tree.getForExpressionInClauses().size() - 1; inx >= 0; --inx) {
                    JFXForExpressionInClause clause = tree.getForExpressionInClauses().get(inx);
                    expr = makeBoundComprehension(clause, expr);
                }
                return expr;
            }
        }).doit();
    }
    
    public void visitIndexof(JFXIndexof tree) {
        assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
        result = make.at(tree.pos()).Ident(indexofMangledName(tree.clause.getVar().sym));
    }

    private JCExpression makeBoundConditional(final DiagnosticPosition diagPos,
            final TypeMorphInfo tmi,
            final JCExpression trueExpr,
            final JCExpression falseExpr,
            final JCExpression condExpr) {
        return (new ClosureTranslator(diagPos, toJava, tmi) {

            protected List<JCTree> makeBody() {
                return List.<JCTree>of(
                        makeClosureMethod("computeThenBranch", trueExpr, null),
                        makeClosureMethod("computeElseBranch", falseExpr, null));
            }

            protected JCExpression makeBaseClass() {
                Type clazzType = tmi.getBoundIfLocationType();
                return makeBaseClass(clazzType, null);
            }

            protected List<JCExpression> makeConstructorArgs() {
                ListBuffer<JCExpression> constructorArgs = ListBuffer.lb();
                if (tmiResult.isSequence()) {
                    // prepend "Foo.class, "
                    constructorArgs.append(makeDotClass(diagPos, tmiResult.getElementType()));
                }
                constructorArgs.append(condExpr);
                constructorArgs.append(makeLaziness(diagPos));
                return constructorArgs.toList();
            }
        }).doit();
    }

    @Override
    public void visitConditional(final JCConditional tree) {
        result = makeBoundConditional(tree.pos(), 
                typeMorpher.typeMorphInfo(tree.type),
                translate(tree.getTrueExpression()),
                translate(tree.getFalseExpression()),
                translate(tree.getCondition()) );
    }

    @Override
    public void visitParens(JCParens tree) { //done
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).Parens(expr);
    }

    @Override
    public void visitTypeTest(final JCInstanceOf tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression resultValue() {
                return m().TypeTest( 
                        buildArgField(tree.expr, "toTest"),
                        makeExpression(tree.clazz.type) );
            }
        }.doit();
    }

    @Override
    public void visitTypeCast(final JCTypeCast tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

           protected JCExpression resultValue() {
                return new TypeCastTranslator(tree, toJava) {

                    protected JCExpression translatedExpr() {
                        return buildArgField(tree.expr, "toBeCast");
                    }
                }.doit();
            }
        }.doit();
    }
    
    @Override
    public void visitLiteral(JCLiteral tree) {
        final DiagnosticPosition diagPos = tree.pos();
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = types.elementType(tree.type);
            result = runtime(diagPos, cBoundSequences, "empty", List.of(makeDotClass(diagPos, elemType)));
        } else {
            JCExpression unbound = make.at(diagPos).Literal(tree.typetag, tree.value);
            result = makeConstantLocation(diagPos, tree.type, unbound);
        }
    }
    
    /**
     * Translator for Java method and non-bound JavaFX functions.
     */
    abstract class BindingExpressionClosureTranslator extends ClosureTranslator {

        BindingExpressionClosureTranslator(DiagnosticPosition diagPos, TypeMorphInfo tmiResult) {
            super(diagPos, JavafxToBound.this.toJava, tmiResult);
        }
        
        BindingExpressionClosureTranslator(DiagnosticPosition diagPos, Type resultType) {
            this(diagPos, typeMorpher.typeMorphInfo( resultType ));
        }

        protected abstract JCExpression resultValue();
        
        protected void buildFields() {
            // by default do this dynamically
        }

        protected List<JCTree> makeBody() {
            buildFields();
            JCExpression resultVal = resultValue(); // build first since this may add dependencies

            // create a getStaticDependents method to set the args as static dependents
            Type locationType = typeMorpher.baseLocation.type;
            JCExpression depsArray = make.NewArray(makeExpression(locationType), List.<JCExpression>nil(), dependents.toList());
            Type depsReturnType = new Type.ArrayType(locationType, syms.arrayClass);
            members.append(makeClosureMethod("getStaticDependents", depsArray, null, depsReturnType, Flags.PROTECTED));

            members.append(makeClosureMethod("computeValue", resultVal, null, tmiResult.getRealType(), Flags.PUBLIC));
            return members.toList();
        }

        protected JCExpression makeBaseClass() {
            Type clazzType = typeMorpher.bindingExpressionType(typeKindResult);
            return makeBaseClass(clazzType, null);
        }

        protected List<JCExpression> makeConstructorArgs() {
            return List.<JCExpression>nil();
        }

        @Override
        protected JCExpression doit() {
            return typeMorpher.makeLocationLocalVariable(tmiResult, diagPos, List.of(makeLaziness(diagPos), buildClosure()));
        }      
    }

    @Override
    public void visitApply(final JCMethodInvocation tree) {
        result = (new FunctionCallTranslator(tree, toJava) {

            final TypeMorphInfo tmiResult = typeMorpher.typeMorphInfo(tree.type);
            final List<JCExpression> typeArgs = toJava.translate(tree.typeargs); //TODO: should, I think, be nil list

            public JCExpression doit() {
                if (callBound) {
                    if (selectorMutable) {
                        return new SelectClosureTranslator(diagPos,
                                tmiResult,
                                typeMorpher.typeMorphInfo(selector.type),
                                translate(selector)) {

                            protected List<JCTree> makeBody() {
                                // create a field in the closure for each argument
                                buildArgFields(tree.args, true);

                                // translate the method name -- e.g., foo  to foo$bound
                                Name name = functionName(msym, false, callBound);

                                JCExpression transSelect = makeGet(translate(selector), tmiSelector);
                                JCExpression expr = m().Apply(typeArgs,
                                        m().Select(transSelect, name),
                                        callArgs.toList());
                                members.append(makeComputeSelectMethod(expr));
                                return members.toList();
                            }
                        }.doit();
                    } else {
                        JCExpression fresult = m().Apply(typeArgs, transMeth(), determineArgs());
                        if (useInvoke) {
                            if (tree.type.tag != TypeTags.VOID) {
                                fresult = castFromObject(fresult, tree.type);
                            }
                        }
                        return fresult;
                    }
                } else {
                    // call to Java method or unbound JavaFX function
                    //TODO: varargs
                    if (selectorMutable) {
                        return (new BindingExpressionClosureTranslator(diagPos, tmiResult) {

                            private TypeMorphInfo tmiSelector = typeMorpher.typeMorphInfo(selector.type);
                            private Name selectorName = names.fromString("selector$");

                            protected JCExpression resultValue() {
                                // create two accesses to the value of to selector field -- selector$.getXxx()
                                // one for the method call and one for the nul test
                                JCExpression transSelector = makeGetField(selectorName, tmiSelector);
                                JCExpression toTest = makeGetField(selectorName, tmiSelector);

                                // construct the actuall method invocation
                                Name methName = ((JCFieldAccess) tree.meth).name;
                                JCExpression callMeth = m().Select(transSelector, methName);
                                JCExpression call = m().Apply(typeArgs, callMeth, callArgs.toList());

                                // test the selector for null before attempting to invoke the method 
                                // if it would dereference null, then instead give the default value
                                JCExpression cond = m().Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
                                JCExpression defaultExpr = toJava.makeDefaultValue(diagPos, tmiResult);
                                return m().Conditional(cond, call, defaultExpr);
                            }

                            @Override
                            protected void buildFields() {
                                // create a field in the BindingExpression for each argument
                                buildArgFields(tree.args, false);
                                
                                // translate the method selector into a Location field of the BindingExpression
                                // XxxLocation selector$ = ...;
                                members.append(makeLocationField(translate(selector), selectorName, tmiSelector));

                                // build a list of these selectors, for use as dependents -- selector$0, selector$1, ...
                                dependents.append(m().Ident(selectorName));
                            }
                        }).doit();
                    } else {
                        return (new BindingExpressionClosureTranslator(diagPos, tmiResult) {

                            // construct the actual value computing method (with the method call)
                            protected JCExpression resultValue() {
                                return m().Apply(toJava.translate(tree.typeargs), transMeth(), callArgs.toList());
                            }

                            @Override
                            protected void buildFields() {
                                // create a field in the BindingExpression for each argument
                                buildArgFields(tree.args, false);
                            }
                        }).doit();
                    }
                }
            }

            public JCExpression transMeth() {
                JCExpression transMeth;
                if (renameToSuper) {
                    transMeth = make.at(selector).Select(make.Select(makeTypeTree(attrEnv.enclClass.sym.type, selector, false), names._super), msym);
                } else {
                    transMeth = toJava.translate(meth);
                }

                // translate the method name -- e.g., foo  to foo$bound or foo$impl
                if (superToStatic || (callBound && ! renameToSuper)) {
                    Name name = functionName(msym, superToStatic, callBound);
                    if (transMeth.getTag() == JavafxTag.IDENT) {
                        transMeth = m().Ident(name);
                    } else if (transMeth.getTag() == JavafxTag.SELECT) {
                        JCFieldAccess faccess = (JCFieldAccess) transMeth;
                        transMeth = m().Select(faccess.getExpression(), name);
                    }
                }
                if (useInvoke) {
                    transMeth = make.Select(transMeth, defs.invokeName);
                }
                return transMeth;
            }

            // compute the translated arguments.
            // if this is a bound call, use left-hand side references for arguments consisting
            // solely of a  var or attribute reference, or function call, otherwise, wrap it
            // in an expression location
            private List<JCExpression> determineArgs() {
                List<JCExpression> args;
                {
                    ListBuffer<JCExpression> translated = ListBuffer.lb();
                    boolean handlingVarargs = false;
                    Type formal = null;
                    List<Type> t = formals;
	            for (List<JCExpression> l = tree.args; l.nonEmpty();  l = l.tail) {
                        if (! handlingVarargs) {
                            formal = t.head;
                            t = t.tail;
                            if (usesVarArgs && t.isEmpty()) {
                                formal = types.elemtype(formal);
                                handlingVarargs = true;
                            }
                        }
                        JCExpression tree = translate(l.head);  //was:   translate(l.head, formal);
                        if (tree != null) {
                            translated.append( tree );
                        }
                    }
	            args = translated.toList();
                }

               // if this is a super.foo(x) call, "super" will be translated to referenced class,
                // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
                if (superToStatic) {
                    args = args.prepend(make.Ident(defs.receiverName));
                }

                return args;
            }

        }).doit();
    }

    @Override
    public void visitBinary(final JCBinary tree) {
        DiagnosticPosition diagPos = tree.pos();
        final JCExpression l = tree.lhs;
        final JCExpression r = tree.rhs;
        final JCExpression lhs = translate(l);
        final JCExpression rhs = translate(r);
        final String typeCode = typeCode(l.type) + typeCode(r.type);

        switch (tree.getTag()) {
            case JavafxTag.PLUS:
                result = runtime(diagPos, cBoundOperators, "plus_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.MINUS:
                result = runtime(diagPos, cBoundOperators, "minus_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.DIV:
                result = runtime(diagPos, cBoundOperators, "divide_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.MUL:
                result = runtime(diagPos, cBoundOperators, "times_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.MOD:
                result = runtime(diagPos, cBoundOperators, "modulo_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.EQ:
                result = runtime(diagPos, cBoundOperators, "eq_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.NE:
                result = runtime(diagPos, cBoundOperators, "ne_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.LT:
                result = runtime(diagPos, cBoundOperators, "lt_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.LE:
                result = runtime(diagPos, cBoundOperators, "le_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.GT:
                result = runtime(diagPos, cBoundOperators, "gt_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.GE:
                result = runtime(diagPos, cBoundOperators, "ge_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.AND:
                result = runtime(diagPos, cBoundOperators, "and_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.OR:
                result = runtime(diagPos, cBoundOperators, "or_" + typeCode, List.of(lhs, rhs));
                break;
            default:
                assert false : "unhandled binary operator";
                result = lhs;
                break;
        }
    }

    @Override
    public void visitUnary(final JCUnary tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression expr = tree.getExpression();
        JCExpression transExpr = translate(expr);
        String typeCode = typeCode(expr.type);

        switch (tree.getTag()) {
            case JavafxTag.SIZEOF:
                result = runtime(diagPos, cBoundSequences, "sizeof", List.of(transExpr) );
                break;
            case JavafxTag.REVERSE:
                result = runtime(diagPos, cBoundSequences, "reverse", List.of(transExpr) );
                break;
            case JCTree.NOT:
                result = runtime(diagPos, cBoundOperators, "not_"+typeCode, List.of(transExpr) );
                break;
            case JCTree.NEG:
                if (types.isSameType(tree.type, syms.javafx_DurationType)) {   //TODO
                    result = make.at(diagPos).Apply(null,
                            make.at(diagPos).Select(translate(tree.arg), Name.fromString(names, "negate")), List.<JCExpression>nil());
                } else {
                    result = runtime(diagPos, cBoundOperators, "negate_"+typeCode, List.of(transExpr));
                }
                break;
            case JCTree.PREINC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "++");
                result = transExpr;
                break;
            case JCTree.PREDEC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "--");
                result = transExpr;
                break;
            case JCTree.POSTINC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "++");
                result = transExpr;
                break;
            case JCTree.POSTDEC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "--");
                result = transExpr;
                break;
            default:
                assert false : "unhandled unary operator";
                result = transExpr;
                break;
        }
    }
    
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        // convert time literal to a javafx.lang.Duration object literal
        JCFieldAccess clsname = (JCFieldAccess) makeQualifiedTree(tree.pos(), syms.javafx_DurationType.tsym.toString());
        clsname.type = syms.javafx_DurationType;
        clsname.sym = syms.javafx_DurationType.tsym;
        Name attribute = names.fromString("millis");
        Symbol symMillis = clsname.sym.members().lookup(attribute).sym;
        JavafxTreeMaker fxmake = (JavafxTreeMaker)make;
        JFXObjectLiteralPart objLiteral = fxmake.at(tree.pos()).ObjectLiteralPart(attribute, tree.value, JavafxBindStatus.UNBOUND);
        objLiteral.sym = symMillis;
        JFXInstanciate inst = fxmake.at(tree.pos).Instanciate(clsname, null, List.of((JCTree)objLiteral));
        inst.type = clsname.type;
        
        // now convert that object literal to Java
        visitInstanciate(inst); // sets result
    }

    /***********************************************************************
     *
     * Utilities 
     *
     */
    

    /**
     * For an attribute "attr" make an access to it via the receiver and getter  
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, String attribName) {
       return callExpression(diagPos, 
                make.Ident(defs.receiverName),
                attributeGetMethodNamePrefix + attribName);
   }
   
    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString) {
        return runtime(diagPos,
                cString,
                methString,
                 List.<JCExpression>nil());
    }

    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            ListBuffer<JCExpression> args) {
        return runtime(diagPos,
                cString,
                methString,
                 args.toList());
    }

    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            List<JCExpression> args) {
        JCExpression meth = make.at(diagPos).Select(
                makeQualifiedTree(diagPos, cString),
                names.fromString(methString));
        return make.at(diagPos).Apply(null, meth, args);
    }

    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method) {
        return callExpression(diagPos, receiver, method, null);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            Name methodName) {
        return callExpression(diagPos, receiver, methodName, null);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            Object args) {
        return callExpression(diagPos, receiver, names.fromString(method), args);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
             Name methodName, 
            Object args) {
        JCExpression expr = null;
        if (receiver == null) {
            expr = make.at(diagPos).Ident(methodName);
        } else {
            expr = make.at(diagPos).Select(receiver, methodName);
        }
        return make.at(diagPos).Apply(List.<JCExpression>nil(), expr, 
                (args == null)? List.<JCExpression>nil() :
                (args instanceof List)? (List<JCExpression>)args :
                (args instanceof ListBuffer)? ((ListBuffer<JCExpression>)args).toList() :
                (args instanceof JCExpression)?   List.<JCExpression>of((JCExpression)args) :
                    null /* error */
                );    
                    
    }

    private Name getSyntheticName(String kind) {
        return toJava.getSyntheticName(kind);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, Type type, JCExpression value) {
        return makeTmpVar(diagPos, "tmp", type, value);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, String rootName, Type type, JCExpression value) {
            return toJava.makeTmpVar(diagPos, rootName, type, value);
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
        if (target.boxWithConstructors()) {
            Symbol ctor = lookupConstructor(translatedExpr.pos(),
                                            types.boxedClass(primitiveType).type,
                                            List.<Type>nil()
                                            .prepend(primitiveType));
            return make.Create(ctor, List.of(translatedExpr));
        } else {
            Symbol valueOfSym = lookupMethod(translatedExpr.pos(),
                                             names.valueOf,
                                             types.boxedClass(primitiveType).type,
                                             List.<Type>nil()
                                             .prepend(primitiveType));
//            JCExpression meth =makeIdentifier(valueOfSym.owner.type.toString() + "." + valueOfSym.name.toString());
            JCExpression meth = make.Select(makeTypeTree(valueOfSym.owner.type, diagPos), valueOfSym.name);
            TreeInfo.setSymbol(meth, valueOfSym);
            meth.type = valueOfSym.type;
            return make.App(meth, List.of(translatedExpr));
        }
    }
    
    public JCExpression makeQualifiedTree(DiagnosticPosition diagPos, String str) {
        JCExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = Name.fromString(names, part);
            tree = tree == null? 
                make.at(diagPos).Ident(partName) : 
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
    
    private Name indexofMangledName(VarSymbol vsym) {
        return names.fromString(vsym.getSimpleName().toString() + "$indexof");
    }
    
    /**
     * 
     * @param diagPos
     * @return a boolean expression indicating if the bind is lazy
     */
    private JCExpression makeLaziness(DiagnosticPosition diagPos) {
        return make.at(diagPos).Literal(TypeTags.BOOLEAN, 0); //TODO: eager for now, handle lazy
    }
    
    /**
     * Given "Foo" type, return "Foo.class" expression
     * @param diagPos
     * @param type
     * @return expression representing the class
     */
    private JCExpression makeDotClass(DiagnosticPosition diagPos, Type type) {
        return make.at(diagPos).Select(
                        toJava.makeTypeTree(type, diagPos),
                        names._class);
    }
    
    /**
     * Build a Java AST representing the return type of a function.
     * Generate the return type as a Location if "isBound" is set.
     * */
    public JCExpression makeReturnTypeTree(DiagnosticPosition diagPos, MethodSymbol mth, boolean isBound) {
        Type returnType = mth.getReturnType();
        if (isBound) {
            VarMorphInfo vmi = typeMorpher.varMorphInfo(mth);
            returnType = vmi.getLocationType();
        }
        return makeTypeTree(returnType, diagPos);
    }
 
    /**
     * Build a Java AST representing the specified type.
     * Convert JavaFX class references to interface references.
     * */
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos) {
        return makeTypeTree(t, diagPos, true);
    }
    
    /**
     * Build a Java AST representing the specified type.
     * If "makeIntf" is set, convert JavaFX class references to interface references.
     * */
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos, boolean makeIntf) {
        return toJava.makeTypeTree(t, diagPos, makeIntf);
    }

   JCExpression castFromObject (JCExpression arg, Type castType) {
        if (castType.isPrimitive())
            castType = types.boxedClass(castType).type;
         return make.TypeCast(makeTypeTree(castType, arg.pos()), arg);
    }

    private String typeCode(Type type) {
        Symbol tsym = type.tsym;
                if (type.isPrimitive()) {
                    if (tsym == syms.doubleType.tsym
                            || tsym == syms.floatType.tsym) {
                        return "d";
                    } else if (tsym == syms.intType.tsym
                            || tsym == syms.byteType.tsym
                            || tsym == syms.charType.tsym
                            || tsym == syms.longType.tsym
                            || tsym == syms.shortType.tsym) {
                        return "i";
                    } else if (tsym == syms.booleanType.tsym) {
                        return "b";
                    } else {
                        assert false : "should not reach here";
                        return "X";
                    }
                } else {
                    if (types.isSequence(type) ) {
                        return "s";  //TODO: ?
                    } else if (tsym == this.booleanObjectTypeSymbol) {
                        return "B";
                    } else if (tsym == this.doubleObjectTypeSymbol) {
                        return "D";
                    } else if (tsym == this.intObjectTypeSymbol) {
                        return "I";
                    } else {
                        return "o";
                    }
                }
    }
    
    Symbol expressionSymbol(JCExpression tree) {
        switch (tree.getTag()) {
            case JavafxTag.IDENT:
                return ((JCIdent) tree).sym;
            case JavafxTag.SELECT:
                return ((JCFieldAccess) tree).sym;
            default:
                return null;
        }
    }
    
    JCExpression makeUnboundLocation(DiagnosticPosition diagPos, Type type, JCExpression expr) {
        return toJava.makeUnboundLocation(diagPos, typeMorpher.typeMorphInfo(type), expr);
    }

    JCExpression makeConstantLocation(DiagnosticPosition diagPos, Type type, JCExpression expr) {
        TypeMorphInfo tmi = typeMorpher.typeMorphInfo(type);
        List<JCExpression> makeArgs = List.of(expr);
        JCExpression locationTypeExp = toJava.makeTypeTree(tmi.getConstantLocationType(), diagPos, true);
        JCFieldAccess makeSelect = make.at(diagPos).Select(locationTypeExp, defs.makeMethodName);
        List<JCExpression> typeArgs = null;
        if (tmi.getTypeKind() == TYPE_KIND_OBJECT || tmi.getTypeKind() == TYPE_KIND_SEQUENCE) {
            typeArgs = List.of(toJava.makeTypeTree(tmi.getElementType(), diagPos, true));
        }
        return make.at(diagPos).Apply(typeArgs, makeSelect, makeArgs);
    }

    /**
     * Call Variable "make" to create a bound expression.
     * Use "stmt" which is the translation of the expression into
     * a statement.  The Variable is created with an anonymous binding expression
     * instance and the static dependencies.
     */
    JCExpression makeBoundLocation(DiagnosticPosition diagPos,
            TypeMorphInfo tmi,
            JCStatement stmt,
            boolean isLazy,
            List<JCExpression> staticDependencies) {

        return toJava.makeBoundLocation(diagPos,
            tmi,
            stmt,
            isLazy,
            staticDependencies);
    }

    JCExpression makeIdentifier(String s) {
        return ((JavafxTreeMaker)make).Identifier(s);
    }
    
    JCExpression makeIdentifier(Name name) {
        return ((JavafxTreeMaker)make).Identifier(name);
    }
    
    protected void prettyPrint(JCTree node) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.printExpr(node);
            osw.flush();
        } catch (Exception ex) {
            System.err.println("Error in pretty-printing: " + ex);
        }
    }
    
    Name functionName(MethodSymbol sym) {
        return functionName(sym, false);
    }

    Name functionName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, false, isBound);
    }

    Name functionInterfaceName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, isBound);
    }

    Name functionName(MethodSymbol sym, boolean markAsImpl, boolean isBound) {
        if (!markAsImpl && !isBound) {
            return sym.name;
        }
        return toJava.functionName( sym, sym.name.toString(), markAsImpl, isBound );
    }

    /***********************************************************************
     *
     * Moot visitors
     *
     */
    
    public void visitInterpolate(JFXInterpolate tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }
    
    @Override
    public void visitModifiers(JCModifiers tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitNewArray(JCNewArray tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitNewClass(JCNewClass tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitSkip(JCSkip tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSwitch(JCSwitch tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitSynchronized(JCSynchronized tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitThrow(JCThrow tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTry(JCTry tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitWhileLoop(JCWhileLoop tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitLetExpr(LetExpr tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTree(JCTree tree) {
        assert false : "should not be in JavaFX AST";
    }
    
    @Override
    public void visitOverrideAttribute(JFXOverrideAttribute tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        assert false : "should not be processed as part of a binding";
   }
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }
    
   @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }
   
    public void visitBindExpression(JFXBindExpression tree) {
         throw new AssertionError(tree);
    }

    @Override
    public void visitBlock(JCBlock tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitContinue(JCContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitDoLoop(JCDoWhileLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitReturn(JCReturn tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitExec(JCExpressionStatement tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitForeachLoop(JCEnhancedForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitForLoop(JCForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitIf(JCIf tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitImport(JCImport tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitIndexed(JCArrayAccess tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeArray(JCArrayTypeTree tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitLabelled(JCLabeledStatement tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
         assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeApply(JCTypeApply tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTypeParameter(JCTypeParameter tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitAnnotation(JCAnnotation tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitAssert(JCAssert tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitBreak(JCBreak tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
   public void visitCase(JCCase tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitCatch(JCCatch tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        assert false : "should not be processed as part of a binding";
    }  
        
    @Override
    public void visitVar(JFXVar tree) {
        // this is handled in translarVar
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitErroneous(JCErroneous tree) {
        assert false : "erroneous nodes shouldn't have gotten this far";
    }
   
}
