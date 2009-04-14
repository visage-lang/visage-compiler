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
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.comp.JavafxToJava.UseSequenceBuilder;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.FunctionCallTranslator;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.Locationness;
import com.sun.tools.javafx.comp.JavafxToJava.InstanciateTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.InterpolateValueTranslator;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javac.code.TypeTags.*;

public class JavafxToBound extends JavafxAbstractTranslation implements JavafxVisitor {
    protected static final Context.Key<JavafxToBound> jfxToBoundKey =
        new Context.Key<JavafxToBound>();

    static final boolean SEQUENCE_CONDITIONAL_INLINE = true;

    /*
     * the result of translating a tree by a visit method
     */
    BoundResult result;

    /*
     * modules imported by context
     */
    private final JavafxOptimizationStatistics optStat;

    /*
     * other instance information
     */
    private final Name computeElementsName;

    /*
     * State
     */
    private ListBuffer<BindingExpressionClosureTranslator> bects;
    private TypeMorphInfo tmiTarget = null;
    private JavafxBindStatus bindStatus = null; // should never be accessed before set

    /*
     * static information
     */
    private static final String cFunction0 = functionsPackageNameString + ".Function0";

    private static final String opPLUS = cOperator + ".PLUS";
    private static final String opMINUS = cOperator + ".MINUS";
    private static final String opTIMES = cOperator + ".TIMES";
    private static final String opDIVIDE = cOperator + ".DIVIDE";
    private static final String opMODULO = cOperator + ".MODULO";

    private static final String opLT = cOperator + ".CMP_LT";
    private static final String opLE = cOperator + ".CMP_LE";
    private static final String opGT = cOperator + ".CMP_GT";
    private static final String opGE = cOperator + ".CMP_GE";

    private static final String opEQ = cOperator + ".CMP_EQ";
    private static final String opNE = cOperator + ".CMP_NE";

    private static final String opNEGATE = cOperator + ".NEGATE";
    private static final String opNOT = cOperator + ".NOT";

    public static JavafxToBound instance(Context context) {
        JavafxToBound instance = context.get(jfxToBoundKey);
        if (instance == null) {
            JavafxToJava toJava = JavafxToJava.instance(context);
            instance = new JavafxToBound(context, toJava);
        }
        return instance;
    }

    protected JavafxToBound(Context context, JavafxToJava toJava) {
        super(context, toJava);
        
        context.put(jfxToBoundKey, this);

        optStat = JavafxOptimizationStatistics.instance(context);
        
        computeElementsName = names.fromString("computeElements$");
    }

    private class BoundResult {
        final JCExpression locationExpression;
        BindingExpressionClosureTranslator translator;
        JCExpression instanciateBE;
        BoundResult(JCExpression locationExpression) {
            this.translator = null;
            this.locationExpression = locationExpression;
        }
        JCExpression asLocation() {
            return locationExpression;
        }
    }

    // External translation entry-point
    JCExpression translateAsLocationOrBE(JFXExpression tree, JavafxBindStatus bindStatus, TypeMorphInfo tmi) {
        BoundResult res = translateAsResult(tree, bindStatus, tmi);
        if (res.translator != null) {
            // There is a binding expression we can return, so do so
            // Mark the BE as un-absorbed since we are using it explicitly
            res.translator.absorbed = false;
            return res.instanciateBE;
        } else
            // no binding expression, return a Location
            return res.asLocation();
    }

    // External translation entry-point
    JCExpression translateAsLocation(JFXExpression tree, JavafxBindStatus bindStatus, TypeMorphInfo tmi) {
        return translateAsResult(tree, bindStatus, tmi).asLocation();
    }

    private JCExpression translate(JFXExpression tree, JavafxBindStatus bindStatus, Type type) {
        return translateAsResult(tree, bindStatus, typeMorpher.typeMorphInfo(type)).asLocation();
    }

    private BoundResult translateAsResult(JFXExpression tree, JavafxBindStatus bindStatus, TypeMorphInfo tmi) {
        JavafxBindStatus prevBindStatus = this.bindStatus;
        this.bindStatus = bindStatus;
        BoundResult res = translate(tree, tmi);
        this.bindStatus = prevBindStatus;
        return res;
    }

    private JCExpression translateForConditional(JFXExpression tree, Type type) {
        if (SEQUENCE_CONDITIONAL_INLINE && types.isSequence(type)) {
            return translate(tree, type);
        } else {
            return translate(tree, JavafxBindStatus.LAZY_UNIDIBIND, type);
        }
    }

    /** Visitor method: Translate a single node.
     */
    private BoundResult translate(JFXExpression tree, TypeMorphInfo tmi) {
        TypeMorphInfo tmiPrevTarget = tmiTarget;
        this.tmiTarget = tmi;
        BoundResult ret;

        if (tree == null) {
            ret = null;
        } else {
            JFXTree prevWhere = getAttrEnv().where;
            toJava.attrEnv.where = tree;
            tree.accept(this);
            toJava.attrEnv.where = prevWhere;
            ret = this.result;
            this.result = null;
        }
        this.tmiTarget = tmiPrevTarget;
        return ret;
    }

    private JCExpression translate(JFXExpression tree) {
        return translate(tree, (TypeMorphInfo)null).asLocation();
    }

    // External translation entry-point: used by SequenceBuilder
    JCExpression translate(JFXExpression tree, Type type) {
        return translate(tree, typeMorpher.typeMorphInfo(type)).asLocation();
    }

    private List<JCExpression> translate(List<JFXExpression> trees, Type methType, boolean usesVarArgs) {
        ListBuffer<JCExpression> translated = ListBuffer.lb();
        boolean handlingVarargs = false;
        Type formal = null;
        List<Type> t = methType.getParameterTypes();
        for (List<JFXExpression> l = trees; l.nonEmpty(); l = l.tail) {
            if (!handlingVarargs) {
                formal = t.head;
                t = t.tail;
                if (usesVarArgs && t.isEmpty()) {
                    formal = types.elemtype(formal);
                    handlingVarargs = true;
                }
            }
            JCExpression tree = translate(l.head, formal);
            if (tree != null) {
                if (tree.type == null) { // if not set by convert()
                    tree.type = formal; // mark the type to declare the holder of this arg
                }
                translated.append(tree);
            }
        }
        List<JCExpression> args = translated.toList();

        return args;
    }

    private JCExpression convert(Type inType, JCExpression tree) {
        if (tmiTarget == null) {
            tree.type = inType;
            return tree;
        }
        Type targetType = tmiTarget.getRealType();
        return convert(inType, tree, targetType);
    }

    private JCExpression convert(final Type inType, JCExpression tree, final Type targetType) {
        DiagnosticPosition diagPos = tree.pos();
        if (!types.isSameType(inType, targetType)) {
            if (types.isSequence(targetType)) {
                Type targetElementType = types.elementType(targetType);
                if (targetElementType == null) {  // runtime classes written in Java do this
                    tree.type = inType;
                    return tree;
                }
                if (!types.isSequence(inType)) {
                    JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
                    tree = runtime(diagPos, 
                            defs.BoundSequences_singleton,
                            List.of(
                                makeLaziness(diagPos),
                                targetTypeInfo,
                                convert(inType, tree, targetElementType)));
                } else {
                    // this additional test is needed because wildcards compare as different
                    Type sourceElementType = types.elementType(inType);
                    if (!types.isSameType(sourceElementType, targetElementType)) {
                        if (types.isNumeric(sourceElementType) && types.isNumeric(targetElementType)) {
                            tree = convertNumericSequence(diagPos,
                                    tree,
                                    sourceElementType,
                                    targetElementType);
                        } else {
                            JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
                            tree = runtime(diagPos, defs.BoundSequences_upcast, List.of(makeLaziness(diagPos), targetTypeInfo, tree));
                        }
                    }
                }
            } else if (targetType.isPrimitive()) {
                final JCExpression prevTree = tree;
                TypeMorphInfo tmiPrevTarget = tmiTarget;
                tmiTarget = null;  // ignore translation-time type targets


                tree = new BindingExpressionClosureTranslator(diagPos, targetType) {

                    protected JCExpression makePushExpression() {
                        return typeCast(diagPos, targetType, inType, buildArgField(prevTree, inType));
                    }
                }.doit();
                tmiTarget = tmiPrevTarget;
            } else {
                List<JCExpression> typeArgs = List.of(makeTypeTree(diagPos, targetType, true),
                        makeTypeTree(diagPos, types.boxedTypeOrType(inType), true));
                Type inRealType = typeMorpher.typeMorphInfo(inType).getRealType();
                JCExpression inClass = makeTypeInfo(diagPos, inRealType);
                tree = runtime(diagPos, defs.Locations_upcast, typeArgs, List.of(inClass, tree));
            }
        }
        tree.type = targetType; // as a way of passing it to methods which needs to know the target type
        return tree;
    }

    
    private JCExpression convertNumericSequence(final DiagnosticPosition diagPos,
            final JCExpression expr, final Type inElementType, final Type targetElementType) {
        JCExpression inTypeInfo = makeTypeInfo(diagPos, inElementType);
        JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
        return runtime(diagPos,
                defs.BoundSequences_convertNumberSequence,
                List.of(makeLaziness(diagPos), targetTypeInfo, inTypeInfo, expr));
    }

    /**
     *
     * @param diagPos
     * @return a boolean expression indicating if the bind is lazy
     */
    private JCExpression makeLaziness(DiagnosticPosition diagPos) {
        return makeLaziness(diagPos, bindStatus);
    }

    private Type targetType(Type type) {
        return tmiTarget!=null? tmiTarget.getRealType() : type;
    }

    private JCVariableDecl translateVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();

        JFXModifiers mods = tree.getModifiers();
        long modFlags = mods == null ? 0L : mods.flags;
        modFlags |= Flags.FINAL;  // Locations are never overwritten
        JCModifiers tmods = make.at(diagPos).Modifiers(modFlags);

        VarMorphInfo vmi = typeMorpher.varMorphInfo(tree.sym);
        JCExpression typeExpression = makeTypeTree( diagPos,vmi.getLocationType(), true);

        //TODO: handle array initializers (but, really, shouldn't that be somewhere else?)
        JCExpression init;
        if (tree.init == null) {
            init = makeLocationAttributeVariable(vmi, diagPos);
        } else {
            init = translate(tree.init, vmi.getRealFXType());
        }
        return make.at(diagPos).VarDef(tmods, tree.name, typeExpression, init);
    }

    /**
     * Make BindingExpression compute() method
     * @param diagPos
     * @param stmts
     * @return
     */
    private JCTree makeComputeMethod(DiagnosticPosition diagPos, List<JCStatement> stmts) {
        return makeMethod(diagPos, defs.computeMethodName, stmts, null, syms.voidType, Flags.PUBLIC);
    }

    void scriptBeginBinding() {
        bects = ListBuffer.lb();
    }

    boolean hasScriptBinding() {
        for (BindingExpressionClosureTranslator b : bects) {
            if (b.shouldBuildIntoScriptClass()) {
                return true;
            }
        }
        return false;
    }

    List<JCTree> scriptCompleteBinding(DiagnosticPosition diagPos) {
        if (bects.isEmpty()) {
            // Make empty BindingExpression compute() method -- To make interface happy
            return List.<JCTree>of(makeComputeMethod(diagPos, List.<JCStatement>nil()));
        } else {
            ListBuffer<JCCase> cases = ListBuffer.lb();
            for (BindingExpressionClosureTranslator b : bects) {
                if (b.shouldBuildIntoScriptClass()) {
                    cases.append(b.makeCase());
                }
            }

            JCStatement swit = make.at(diagPos).Switch(make.at(diagPos).Ident(defs.bindingIdName), cases.toList());

            JCTree computeMethod = makeComputeMethod(diagPos, List.of(swit));

            Type objectArrayType = new Type.ArrayType(syms.objectType, syms.arrayClass);
            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            params.append(makeParam(diagPos, defs.idName, syms.intType));
            params.append(makeParam(diagPos, defs.arg0Name, syms.objectType));
            params.append(makeParam(diagPos, defs.arg1Name, syms.objectType));
            params.append(makeParam(diagPos, defs.moreArgsName, objectArrayType));
            params.append(makeParam(diagPos, defs.dependentsName, syms.intType));
            JCStatement cbody = make.Exec(make.Apply(null, make.Ident(names._super), List.<JCExpression>of(
                    make.Ident(defs.idName),
                    make.Ident(defs.arg0Name),
                    make.Ident(defs.arg1Name),
                    make.Ident(defs.moreArgsName),
                    make.Ident(defs.dependentsName)
                    )));
            JCTree constr = makeMethod(diagPos, names.init, List.of(cbody), params.toList(), syms.voidType, Flags.PRIVATE);

            return List.of(computeMethod, constr);
        }
    }

    private JCExpression wrapInBindingExpression(final DiagnosticPosition diagPos,
            final Type resultType,
            final JCExpression texpr,
            final JFXExpression expr) {
        return new BindingExpressionClosureTranslator(diagPos, resultType) {

            private JCVariableDecl renamingVar(Name name, Name vname, Type type, boolean isLocation) {
                TypeMorphInfo tmi = typeMorpher.typeMorphInfo(type);
                Type varType = isLocation? tmi.getLocationType() : type;
                FieldInfo rcvrField = new FieldInfo(name.toString(), typeMorpher.typeMorphInfo(varType), false, ArgKind.FREE);
                return m().VarDef(
                        m().Modifiers(Flags.FINAL),
                        vname,
                        makeTypeTree(diagPos, varType),
                        buildArgField(m().Ident(name), rcvrField));
            }

            protected JCExpression makePushExpression() {
                ListBuffer<JCStatement> renamings = ListBuffer.<JCStatement>lb();
                switch (toJava.inInstanceContext) {
                    case ScriptAsStatic:
                        // No receiver
                        break;
                    case InstanceAsStatic:
                        renamings.append( renamingVar(defs.receiverName, defs.receiverName, getAttrEnv().enclClass.type, false) );
                        break;
                    case InstanceAsInstance:
                        renamings.append( renamingVar(names._this, defs.receiverName, getAttrEnv().enclClass.type, false) );
                        break;
                    default:
                        assert false : "Bad receiver context";
                        break;
                }
                if (expr != null) {
                    for (VarSymbol vsym : localVars(expr)) {
                        renamings.append( renamingVar(vsym.name, vsym.name, vsym.type, true) );
                    }
                }
                return makeBlockExpression(diagPos, renamings.toList(), texpr);
            }
        }.buildClosure();
    }

    @Override
    public void visitInstanciate(final JFXInstanciate tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression makePushExpression() {
                return new InstanciateTranslator(tree, toJava) {

                    protected void processLocalVar(JFXVar var) {
                        preDecls.append(translateVar(var));
                    }

                    @Override
                    protected List<JCExpression> translatedConstructorArgs() {
                        List<JFXExpression> args = tree.getArgs();
                        if (args != null && args.size() > 0) {
                            assert tree.constructor != null : "args passed on instanciation of class without constructor";
                            boolean usesVarArgs = (tree.constructor.flags() & Flags.VARARGS) != 0L;
                            buildArgFields(translate(args, tree.constructor.type, usesVarArgs), ArgKind.DEPENDENT);
                            return callArgs.toList();
                        } else {
                            return List.<JCExpression>nil();
                        }
                    }

                    @Override
                    void setInstanceVariable(Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JFXExpression init) {
                        // bind staus to use for translation needs to propagate laziness if this isn't a bound init
                        JavafxBindStatus translationBindStatus = bindStatus.isBound()?
                            bindStatus :
                            JavafxToBound.this.bindStatus.isLazy()?
                                JavafxBindStatus.LAZY_UNBOUND :
                                JavafxBindStatus.UNBOUND;
                        JCExpression initRef = buildArgField(
                                translate(init, translationBindStatus, vsym.type),
                                new FieldInfo(vsym.type, bindStatus.isBound()? ArgKind.BOUND : ArgKind.DEPENDENT)
                                );
                        setInstanceVariable(init.pos(), instName, bindStatus, vsym, initRef);
                    }
                }.doit();
            }
        }.result();
    }

    @Override
    public void visitStringExpression(final JFXStringExpression tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), syms.stringType) {

            protected JCExpression makePushExpression() {
                return new StringExpressionTranslator(tree) {

                    protected JCExpression translateArg(JFXExpression arg) {
                        return buildArgField(translate(arg), arg.type);
                    }
                }.doit();
            }
        }.result();
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = new BoundResult(makeConstantLocation(
                tree.pos(),
                targetType(tree.type),
                toJava.makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type))
                );
    }

    public void visitBlockExpression(JFXBlock tree) {   //done
        assert (tree.type != syms.voidType) : "void block expressions should be not exist in bind expressions";
        DiagnosticPosition diagPos = tree.pos();

        JFXExpression value = tree.value;
        ListBuffer<JCStatement> translatedVars = ListBuffer.lb();

        for (JFXExpression stmt : tree.getStmts()) {
            if (stmt.getFXTag() == JavafxTag.VAR_DEF) {
                JFXVar var = (JFXVar) stmt;
                translatedVars.append(translateVar(var));
                optStat.recordLocalVar(var.sym, true, true);
            } else {
                assert false : "non VAR_DEF in block expression in bind context";
            }
        }
        while (value.getFXTag() == JavafxTag.VAR_DEF) {
            // for now, at least, ignore the declaration part of a terminal var decl.
            //TODO: when vars can be referenced before decl (say in "var: self" replacement)
            // this will need to be changed.
            value = ((JFXVar)value).getInitializer();
        }
        assert value.getFXTag() != JavafxTag.RETURN;
        result = new BoundResult(makeBlockExpression(diagPos, //TODO tree.flags lost
                translatedVars.toList(),
                translate(value, tmiTarget).asLocation() ));
    }

    @Override
    public void visitAssign(JFXAssign tree) {
        //TODO: this should probably not be allowed
        // log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "=");
        DiagnosticPosition diagPos = tree.pos();
        TypeMorphInfo tmi = typeMorpher.typeMorphInfo(tree.type);
        int typeKind = tmi.getTypeKind();
        // create a temp var to hold the RHS
        JCVariableDecl varDecl = makeTmpVar(diagPos, tmi.getLocationType(), translate(tree.rhs));
        // call the set method
        JCStatement setStmt = callStatement(diagPos,
                translate(tree.lhs),
                defs.locationSetMethodName[typeKind],
                callExpression(diagPos,
                    make.at(diagPos).Ident(varDecl.name),
                    defs.locationGetMethodName[typeKind]));
        // bundle it all into a block-expression that looks like --
        // { ObjectLocation tmp = rhs; lhs.set(tmp.get()); tmp }
        result = new BoundResult(makeBlockExpression(diagPos,
                List.of(varDecl, setStmt),
                make.at(diagPos).Ident(varDecl.name)));
    }

    @Override
    public void visitAssignop(JFXAssignOp tree) {
        // should have caught this in attribution
        assert false : "Assignment operator in bind context";
    }

    private JCExpression makeBoundSelect(final DiagnosticPosition diagPos,
            final Type resultType,
            final BindingExpressionClosureTranslator translator) {
        TypeMorphInfo tmi = (tmiTarget != null) ? tmiTarget : typeMorpher.typeMorphInfo(resultType);
        JCExpression bindingExpression = translator.buildClosure();
        List<JCExpression> args = List.of(
                makeTypeInfo(diagPos, tmi.isSequence()? tmi.getElementType() : tmi.getRealType()),
                makeLaziness(diagPos),
                bindingExpression);
        return runtime(
                diagPos,
                tmi.isSequence()? defs.BoundOperators_makeBoundSequenceSelect : defs.BoundOperators_makeBoundSelect,
                args);
    }

    @Override
    public void visitSelect(final JFXSelect tree) {
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            result = new BoundResult(convert(tree.type, toJava.translateAsLocation(tree))); //TODO -- for now punt, translate like normal case
            return;
        }
        DiagnosticPosition diagPos = tree.pos();

        Symbol owner = tree.sym.owner;
        if (types.isJFXClass(owner) && typeMorpher.requiresLocation(tree.sym)) {
            if (tree.sym.isStatic()) {
                // if this is a static reference to an attribute, eg.   MyClass.myAttribute
                JCExpression classRef = makeTypeTree( diagPos,types.erasure(tree.sym.owner.type), false);
                result = new BoundResult(convert(tree.type, make.at(diagPos).Select(classRef, attributeFieldName(tree.sym))));
            } else {
                // this is a dynamic reference to an attribute
                final JFXExpression expr = tree.getExpression();
                result = new BoundResult(makeBoundSelect(diagPos,
                        tree.type,
                        new BindingExpressionClosureTranslator(tree.pos(), typeMorpher.baseLocation.type) {

                            protected JCExpression makePushExpression() {
                                return convert(tree.type, toJava.convertVariableReference(diagPos,
                                        m().Select(
                                            buildArgField(
                                                translate(expr),
                                                new FieldInfo("selector", expr.type, ArgKind.DEPENDENT)),
                                            tree.getIdentifier()),
                                        tree.sym,
                                        Locationness.AsLocation));
                            }
                        }));
            }
        } else {
            if (tree.sym.isStatic()) {
                // This is a static reference to a Java member or elided member e.g. System.out -- do unbound translation, then wrap
                result = new BoundResult(makeUnboundLocation(diagPos, targetType(tree.type), toJava.translateAsUnconvertedValue(tree)));
            } else {
                // This is a dynamic reference to a Java member or elided member
                result = new BindingExpressionClosureTranslator(diagPos, tree.type) {

                    private JFXExpression selector = tree.getExpression();
                    private TypeMorphInfo tmiSelector = typeMorpher.typeMorphInfo(selector.type);
                    private Name selectorName = getSyntheticName("selector");
                    private FieldInfo selectorField = new FieldInfo(selectorName, tmiSelector, ArgKind.DEPENDENT);

                    protected JCExpression makePushExpression() {
                        // create two accesses to the value of to selector field -- selector$.blip
                        // one for the method call and one for the nul test
                        JCExpression transSelector = selectorField.makeGetField();
                        JCExpression toTest = selectorField.makeGetField();

                        // construct the actual select
                        JCExpression selectExpr = toJava.convertVariableReference(diagPos,
                                        m().Select(transSelector, tree.getIdentifier()),
                                        tree.sym,
                                        Locationness.AsValue);

                        // test the selector for null before attempting to select the field
                        // if it would dereference null, then instead give the default value
                        JCExpression cond = m().Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
                        JCExpression defaultExpr = makeDefaultValue(diagPos, actualTranslatedType);
                        return m().Conditional(cond, selectExpr, defaultExpr);
                    }

                    @Override
                    protected void buildFields() {
                        // translate the selector into a Location field of the BindingExpression
                        // XxxLocation selector$ = ...;
                        buildArgField(translate(selector), selectorField);
                    }
                }.result();
            }
        }
    }

    @Override
    public void visitIdent(JFXIdent tree)   {  //TODO: don't use toJava
       // assert (tree.sym.flags() & Flags.PARAMETER) != 0 || tree.name == names._this || tree.sym.isStatic() || toJava.requiresLocation(typeMorpher.varMorphInfo(tree.sym)) : "we are bound, so should have been marked to morph: " + tree;
        JCExpression transId = toJava.translateAsLocation(tree);
        result = new BoundResult(convert(tree.type, transId ));
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) { //done
        DiagnosticPosition diagPos = tree.pos();
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = types.boxedElementType(targetType(tree.type));
        UseSequenceBuilder builder = toJava.useBoundSequenceBuilder(diagPos, elemType, makeLaziness(diagPos), tree.getItems().length());
        stmts.append(builder.makeBuilderVar());
        for (JFXExpression item : tree.getItems()) {
            stmts.append(builder.addElement( item ) );
        }
        result = new BoundResult(makeBlockExpression(diagPos, stmts, builder.makeToSequence()));
    }

    @Override
    public void visitSequenceRange(JFXSequenceRange tree) { //done: except for step and exclusive
        DiagnosticPosition diagPos = tree.pos();
        Type elemType = syms.javafx_IntegerType;
        int ltag = tree.getLower().type.tag;
        int utag = tree.getUpper().type.tag;
        int stag = tree.getStepOrNull() == null? TypeTags.INT : tree.getStepOrNull().type.tag;
        if (ltag == TypeTags.FLOAT || ltag == TypeTags.DOUBLE ||
                utag == TypeTags.FLOAT || utag == TypeTags.DOUBLE ||
                stag == TypeTags.FLOAT || stag == TypeTags.DOUBLE) {
            elemType = syms.javafx_NumberType;
        }
        TypeMorphInfo tmi = typeMorpher.typeMorphInfo(elemType);
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append( makeLaziness(diagPos) );
        args.append( translate( tree.getLower(), tmi ).asLocation());
        args.append( translate( tree.getUpper(), tmi ).asLocation());
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull(), tmi ).asLocation());
        }
        RuntimeMethod rm = tree.isExclusive()?
            defs.BoundSequences_rangeExclusive :
            defs.BoundSequences_range;
        result = new BoundResult(convert(types.sequenceType(elemType), runtime(diagPos, rm, args.toList())));
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) { //done
        DiagnosticPosition diagPos = tree.pos();
        if (types.isSequence(tree.type)) {
            Type elemType = types.elementType(targetType(tree.type));
            result = new BoundResult(runtime(diagPos, defs.BoundSequences_empty, List.of(makeLaziness(diagPos), makeTypeInfo(diagPos, elemType))));
        } else {
            result = new BoundResult(makeConstantLocation(diagPos, targetType(tree.type), makeNull(diagPos)));
        }
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {   //done
        DiagnosticPosition diagPos = tree.pos();
        result = new BoundResult(convert(
                types.boxedTypeOrType(tree.type),
                runtime(diagPos, defs.BoundSequences_element,
                  List.of(
                    makeLaziness(diagPos),
                    translate(tree.getSequence()),
                    translate(tree.getIndex(), syms.intType)
                  )
                ),
                (tmiTarget == null)? tree.type : tmiTarget.getRealType()
              ));
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {    //done
        DiagnosticPosition diagPos = tree.pos();
        result = new BoundResult(runtime(diagPos,
                tree.getEndKind()==SequenceSliceTree.END_EXCLUSIVE? defs.BoundSequences_sliceExclusive : defs.BoundSequences_slice,
                List.of(
                    makeLaziness(diagPos),
                    makeTypeInfo(diagPos, types.elementType(targetType(tree.type))),
                    translate(tree.getSequence()),
                    translate(tree.getFirstIndex()),
                    tree.getLastIndex()==null? makeNull(diagPos) : translate(tree.getLastIndex())
                    )));
    }

    /**
     * Generate this template, expanding to handle multiple in-clauses
     *
     *  SequenceLocation<V> derived = new BoundComprehension<T,V>(..., IN_SEQUENCE, USE_INDEX) {
            protected SequenceLocation<V> getMappedElement$(final ObjectLocation<T> IVAR_NAME, final IntLocation INDEXOF_IVAR_NAME) {
                return SequenceVariable.make(...,
                                             new SequenceBindingExpression<V>() {
                                                 public Sequence<V> computeValue() {
                                                     if (WHERE)
                                                         return BODY with s/indexof IVAR_NAME/INDEXOF_IVAR_NAME/;
                                                     else
                                                        return ....emptySequence;
                                                 }
                                             }, maybe IVAR_NAME, maybe INDEXOF_IVAR_NAME);
            }
        };
     *
     * **/
    @Override
    public void visitForExpression(final JFXForExpression tree) {
        result = new BoundResult((new Translator( tree.pos() ) {

            private final TypeMorphInfo tmiResult = typeMorpher.typeMorphInfo(targetType(tree.type));

            /**
             * V
             */
            private final Type resultElementType = tmiResult.getElementType();

            /**
             * SequenceLocation<V>
             */
            private final Type resultSequenceLocationType = typeMorpher.generifyIfNeeded(typeMorpher.locationType(TYPE_KIND_SEQUENCE), tmiResult);
            
            /**
             * isSimple -- true if the for-loop is simple enough that it can use SimpleBoundComprehension
             */
            private final boolean isSimple = false;  //TODO

            /**
             * Make:  V.class
             */
            private JCExpression makeResultClass() {
                return makeTypeInfo(diagPos, resultElementType);
            }

            /**
             * Make a method parameter
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
                JFXExpression body = tree.getBodyExpression();
                JCExpression tbody;
                JCExpression whereTest = null;
                for (JFXForExpressionInClause clause : tree.getForExpressionInClauses()) {
                    JFXExpression origWhere = clause.getWhereExpression();
                    if (origWhere != null) {
                        JCExpression where = translate(origWhere);
                        if (whereTest == null) {
                            whereTest = where;
                        } else {
                            whereTest = runtime(diagPos, defs.BoundOperators_and_bb, List.of(whereTest, where));
                        }
                    }
                }
                if (types.isSequence(body.type)) {
                    // the body is a sequence, desired type is the same as for the for-loop
                    tbody = whereTest!=null? translateForConditional(body, tmiTarget.getRealType()) : translate(body, tmiTarget).asLocation();
                } else {
                    // the body is not a sequence, desired type is the element tpe need for for-loop
                    Type elemType = types.unboxedTypeOrType(resultElementType);
                    JCExpression single = whereTest!=null? translateForConditional(body, elemType) : translate(body, elemType);
                    List<JCExpression> args = List.of(makeLaziness(diagPos),makeResultClass(), single);
                    tbody = runtime(diagPos, defs.BoundSequences_singleton, args);
                }
                if (whereTest != null) {
                    tbody = makeBoundConditional(diagPos,
                            tree.type,
                            whereTest,
                            tbody,
                            runtime(diagPos, defs.BoundSequences_empty, List.of(makeLaziness(diagPos), makeTypeInfo(diagPos, resultElementType)))
                          );
                }
                return tbody;
            }

            /**
             *  protected SequenceLocation<V> computeElements$(final ObjectLocation<T> IVAR_NAME, final IntLocation INDEXOF_IVAR_NAME) {
             *     return ...
             *  }
             */
            private JCTree makeComputeElementsMethod(JFXForExpressionInClause clause, JCExpression inner, TypeMorphInfo tmiInduction) {
                Type iVarType;
                Type idxVarType;
                Name computeName;
                if  (isSimple) {
                   iVarType = tmiInduction.getRealFXType();
                   idxVarType = syms.intType;
                   computeName = computeElementsName;
                } else {
                   iVarType = tmiInduction.getLocationType();
                   idxVarType = typeMorpher.locationType(TYPE_KIND_INT);
                   computeName = computeElementsName;
                }
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                Name ivarName = clause.getVar().name;
                stmts.append(m().Return( inner ));
                List<JCVariableDecl> params = List.of(
                        makeParam(iVarType, ivarName),
                        makeParam(idxVarType, indexVarName(clause) )
                        );
                return m().MethodDef(
                        m().Modifiers(Flags.PROTECTED),
                        computeName,
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
                JFXExpression seq = clause.getSequenceExpression();
                TypeMorphInfo tmiSeq = typeMorpher.typeMorphInfo(seq.type);
                TypeMorphInfo tmiInduction = typeMorpher.typeMorphInfo(clause.getVar().type);
                JCClassDecl classDecl = m().AnonymousClassDef(
                        m().Modifiers(0L),
                        List.<JCTree>of(makeComputeElementsMethod(clause, inner, tmiInduction)));
                List<JCExpression> typeArgs = List.nil();
                boolean useIndex = clause.getIndexUsed();
                JCExpression transSeq = translate( seq );
                if (!tmiSeq.isSequence()) {
                    transSeq = runtime(diagPos, defs.BoundSequences_singleton, List.of(makeLaziness(diagPos),makeResultClass(), transSeq));
                }
                List<JCExpression> constructorArgs = List.of(
                        makeLaziness(diagPos),
                        makeResultClass(),
                        makeTypeInfo(diagPos, tmiInduction.getRealBoxedType()),
                        transSeq,
                        m().Literal(TypeTags.BOOLEAN, useIndex? 1 : 0) );
                Type bcType = typeMorpher.abstractBoundComprehension.type;
                JCExpression clazz = makeExpression(types.erasure(bcType));  // type params added below, so erase formals
                ListBuffer<JCExpression> typeParams = ListBuffer.lb();
                typeParams.append( makeExpression(tmiInduction.getRealBoxedType()) );
                typeParams.append( makeExpression(tmiInduction.getLocationType()) );
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
                List<JFXForExpressionInClause> clauses = tree.getForExpressionInClauses();
                // make the body of loop
                JCExpression expr = makeCore();
                // then wrap it in the looping constructs
                for (int inx = clauses.size() - 1; inx >= 0; --inx) {
                    JFXForExpressionInClause clause = clauses.get(inx);
                    expr = makeBoundComprehension(clause, expr);
                }
                return expr;
            }
        }).doit());
    }

    public void visitIndexof(JFXIndexof tree) {
        assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
        JCExpression transIndex = make.at(tree.pos()).Ident(indexVarName(tree.fname));
        VarSymbol vsym = (VarSymbol)tree.clause.getVar().sym;
        if (toJava.requiresLocation(vsym)) {
            // from inside the bind, already a Location
            result = new BoundResult(convert(tree.type, transIndex));
        } else {
            // it came from outside of the bind, make it into a Location
            result = new BoundResult(makeConstantLocation(tree.pos(), targetType(tree.type), transIndex));
        }
    }

    /**
     * Build a tree for a conditional.
     * This is the old syle approach, now used only for sequences
     * @param diagPos
     * @param resultType
     * @param trueExpr then branch, already translated
     * @param falseExpr else  branch, already translated
     * @param condExpr conditional expression  branch, already translated
     * @return
     */
    private JCExpression makeBoundSequenceConditional(final DiagnosticPosition diagPos,
            final Type resultType,
            final TypeMorphInfo tmiResult,
            final JCExpression condExpr,
            final JCExpression trueExpr,
            final JCExpression falseExpr) {
        List<JCExpression> args = List.of(
                makeTypeInfo(diagPos, tmiResult.getElementType()),
                makeLaziness(diagPos),
                condExpr,
                makeFunction0(resultType, trueExpr),
                makeFunction0(resultType, falseExpr));
        return runtime(diagPos, defs.BoundOperators_makeBoundIf, args);
    }

    private JCExpression makeFunction0(
            final Type resultType,
            final JCExpression bodyExpr) {
        return (new ClosureTranslator(bodyExpr.pos()) {

            TypeMorphInfo tmiResult = (tmiTarget != null) ? tmiTarget : typeMorpher.typeMorphInfo(resultType);

            /**
             * Build the closure body
             * @return if the closure will be generated in-line, the list of class memebers of the closure, otherwise return null
             */
            protected List<JCTree> makeBody() {
                return List.<JCTree>of(
                        makeClosureMethod(defs.invokeName, bodyExpr, null, tmiResult.getLocationType(), Flags.PUBLIC));
            }

            /**
             * The class to instanciate that includes the closure
             */
            protected JCExpression makeBaseClass() {
                JCExpression objFactory = makeQualifiedTree(diagPos, cFunction0);
                Type clazzType = tmiResult.getLocationType();
                JCExpression clazz = makeExpression(clazzType);
                return m().TypeApply(objFactory, List.of(clazz));
            }

            protected List<JCExpression> makeConstructorArgs() {
                return List.<JCExpression>nil();
            }
        }).doit();
    }

    private JCExpression makeBoundConditional(final DiagnosticPosition diagPos,
            final Type resultType,
            final JCExpression condExpr,
            final JCExpression trueExpr,
            final JCExpression falseExpr) {
        TypeMorphInfo tmiResult = typeMorpher.typeMorphInfo(resultType);
        if (tmiResult.isSequence()) {
            // the lazy approach won't work for sequences
            return makeBoundSequenceConditional(diagPos,
                    resultType,
                    tmiResult,
                    condExpr,
                    trueExpr,
                    falseExpr);
        }
        List<JCExpression> args = List.of(
                makeTypeInfo(diagPos, resultType),
                makeLaziness(diagPos),
                condExpr,
                trueExpr,
                falseExpr);
        return runtime(diagPos, defs.BoundOperators_makeBoundIf, args);
    }

    @Override
    public void visitIfExpression(final JFXIfExpression tree) {
        Type targetType = targetType(tree.type);
        result = new BoundResult(makeBoundConditional(tree.pos(),
                targetType,
                translate(tree.getCondition()) ,
                translateForConditional(tree.getTrueExpression(), targetType),
                translateForConditional(tree.getFalseExpression(), targetType)));
    }

    @Override
    public void visitParens(JFXParens tree) { //done
        JCExpression expr = translate(tree.expr);
        result = new BoundResult(make.at(tree.pos).Parens(expr));
    }

    @Override
    public void visitInstanceOf(final JFXInstanceOf tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression makePushExpression() {
                Type type = tree.clazz.type;
                type = types.boxedTypeOrType(type);
                return m().TypeTest(
                        buildArgField(translate(tree.expr),
                        new FieldInfo(defs.toTestName, tree.expr.type, ArgKind.DEPENDENT)),
                        makeExpression(type) );
            }
        }.result();
    }

    @Override
    public void visitTypeCast(final JFXTypeCast tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

           protected JCExpression makePushExpression() {
                return makeTypeCast(tree.pos(), tree.clazz.type, tree.expr.type,
                         buildArgField(translate(tree.expr), new FieldInfo(defs.toBeCastName, tree.expr.type, ArgKind.DEPENDENT)));
            }
        }.result();
    }

    @Override
    public void visitLiteral(final JFXLiteral tree) {
        final DiagnosticPosition diagPos = tree.pos();
        final Type targetType = targetType(tree.type);
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = types.elementType(targetType);
            result = new BoundResult(runtime(diagPos, defs.BoundSequences_empty, List.of(makeLaziness(diagPos), makeTypeInfo(diagPos, elemType))));
        } else {
            final JCExpression lit = make.at(diagPos).Literal(tree.typetag, tree.value);
            final JCExpression unbound = tree.typetag == TypeTags.BOT?
                    make.at(diagPos).TypeCast(makeTypeTree(diagPos, targetType.tag != TypeTags.BOT? targetType : syms.objectType, true), lit) :
                    toJava.convertTranslated(lit, diagPos, tree.type, targetType);
            BindingExpressionClosureTranslator bet = new BindingExpressionClosureTranslator(diagPos, targetType) {

                protected JCExpression makePushExpression() {
                    return unbound;
                }

                @Override
                protected JCExpression makeLocation(JCExpression instanciateBE) {
                    return makeConstantLocation(diagPos, targetType, unbound);
                }
            };
            // the Location has absorbed (in this case ignored) the BindingExpression
            // may be un-absorbed if explicitly used
            bet.absorbed = true;
            result = bet.result();
        }
    }

    /**
     * Translator for bound expressions which become BindingExpressions
     */
    private abstract class BindingExpressionClosureTranslator extends ScriptClosureTranslator {

        final TypeMorphInfo tmiResult;
        final Type actualTranslatedType;
        final ListBuffer<JCStatement> preDecls = ListBuffer.lb();
        boolean absorbed = false;

        BindingExpressionClosureTranslator(DiagnosticPosition diagPos, Type resultType) {
            super(diagPos, bects.size());
            this.tmiResult = (tmiTarget != null) ? tmiTarget : typeMorpher.typeMorphInfo(resultType);
            this.actualTranslatedType = resultType;
            bects.append(this);
        }

        abstract JCExpression makePushExpression();

        protected boolean shouldBuildIntoScriptClass() {
            return !generateInLine && !absorbed;
        }

        protected void buildFields() {
            // by default do this dynamically
        }

        /**
         * Build the closure body
         * @return if the closure will be generated in-line, the list of class memebers of the closure, otherwise return null
         */
        protected List<JCTree> makeBody() {
            buildFields();
            // build first since this may add dependencies
            JCExpression resultVal = makePushExpression();
            if (tmiTarget != null && actualTranslatedType != typeMorpher.baseLocation.type) {
                // If we have a target type and this isn't a Location yielding translation (which handles it's own), do any needed type conversion
                resultVal = toJava.convertTranslated(resultVal, diagPos, actualTranslatedType, tmiTarget.getRealType());
            }
            resultStatement = callStatement(diagPos, null, "pushValue", resultVal);

            if (generateInLine) {
                JCTree computeMethod = makeComputeMethod(diagPos, List.of(resultStatement));
                members.append(computeMethod);
                return completeMembers();
            } else {
                // Add to per-script class
                return null;
            }
        }

        @Override
        protected List<JCExpression> makeConstructorArgs() {
            return super.makeConstructorArgs().append(m().Literal(TypeTags.INT, dependents));
        }

        protected JCExpression makeLocation(JCExpression instanciateBE) {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            if (tmiResult.getTypeKind() == TYPE_KIND_OBJECT) {
                args.append(makeDefaultValue(diagPos, tmiResult));
            }
            args.append(makeLaziness(diagPos));
            args.append(instanciateBE);
            return makeLocationLocalVariable(tmiResult, diagPos, args.toList());
        }

        @Override
        protected JCExpression doit() {
            JCExpression varResult = makeLocation(buildClosure());
            if (preDecls.nonEmpty()) {
                return toJava.makeBlockExpression(diagPos, preDecls, varResult);
            } else {
                return varResult;
            }
        }

        BoundResult result() {
            JCExpression instanciateBE = buildClosure();
            JCExpression varResult = makeLocation(instanciateBE);
            if (preDecls.nonEmpty()) {
                varResult = toJava.makeBlockExpression(diagPos, preDecls, varResult);
            }
            BoundResult res = new BoundResult(varResult);
            if (!generateInLine && preDecls.isEmpty()) {
                res.translator = this; // allow the absortion of this BE
                res.instanciateBE = instanciateBE; // allow as raw BindingExpression
            }
            return res;
        }
    }

    @Override
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        //TODO: painfully in need of refactoring
        result = new BoundResult((new FunctionCallTranslator(tree) {

            final List<JCExpression> typeArgs = toJava.translateExpressions(tree.typeargs); //TODO: should, I think, be nil list
            final List<JCExpression> targs = translate(tree.args, meth.type, usesVarArgs);

            public JCExpression doit() {
                if (callBound) {
                    if (selectorMutable) {
                        return makeBoundSelect(diagPos,
                                tree.type,
                                new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

                                    protected JCExpression makePushExpression() {
                                        JCExpression transSelect = buildArgField(translate(selector), new FieldInfo("selector", selector.type, ArgKind.DEPENDENT));
                                        // create a field in the closure for each argument
                                        buildArgFields(targs, ArgKind.BOUND);

                                        // translate the method name -- e.g., foo  to foo$bound
                                        Name name = functionName(msym, false, callBound);

                                        // selectors are always Objects
                                        JCExpression expr = m().Apply(typeArgs,
                                                m().Select(transSelect, name),
                                                callArgs.toList());
                                        return convert(tree.type, expr); // convert type, if needed
                                    }
                                });
                    } else {
                        List<JCExpression> callArgs = targs;
                        if (superToStatic) {  //TODO: should this be higher?
                            // This is a super call, add the receiver so that the impl is called directly
                            callArgs = callArgs.prepend(make.Ident(defs.receiverName));
                        }
                        
                        return convert(tree.type, m().Apply(typeArgs, transMeth(), callArgs));
                    }
                } else {
                    // call to Java method or unbound JavaFX function
                    //TODO: varargs
                    if (selectorMutable || useInvoke) {
                        return (new BindingExpressionClosureTranslator(diagPos, tree.type) {

                            private JFXExpression check = useInvoke? meth : selector;
                            private TypeMorphInfo tmiSelector = typeMorpher.typeMorphInfo(check.type);
                            private Name selectorName = getSyntheticName("selector");
                            private FieldInfo selectorField = new FieldInfo(selectorName, tmiSelector, ArgKind.DEPENDENT);

                            protected JCExpression makePushExpression() {
                                // access the selector field for the method call-- selector$.get()
                                // selectors are always Objects
                                JCExpression transSelector = selectorField.makeGetField(TYPE_KIND_OBJECT);

                                // construct the actual method invocation
                                Name methName = useInvoke? defs.invokeName : ((JFXSelect) tree.meth).name;
                                JCExpression callMeth = m().Select(transSelector, methName);
                                JCExpression call = m().Apply(typeArgs, callMeth, callArgs.toList());

                                if (tmiSelector.getTypeKind() == TYPE_KIND_OBJECT) {
                                    // create another access to the selector field for the null test (below)
                                    JCExpression toTest = selectorField.makeGetField(TYPE_KIND_OBJECT);
                                    // test the selector for null before attempting to invoke the method
                                    // if it would dereference null, then instead give the default value
                                    JCExpression cond = m().Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
                                    JCExpression defaultExpr = makeDefaultValue(diagPos, actualTranslatedType);
                                    return m().Conditional(cond, call, defaultExpr);
                                } else {
                                    return call;
                                }
                            }

                            @Override
                            protected void buildFields() {
                                // translate the method selector into a Location field of the BindingExpression
                                // XxxLocation selector$ = ...;
                                // Must be first, because of pre-definition of selectorField
                                buildArgField(translate(check), selectorField);

                                // create a field in the BindingExpression for each argument
                                buildArgFields(targs, ArgKind.DEPENDENT);
                            }
                        }).doit();
                    } else {
                        return (new BindingExpressionClosureTranslator(diagPos, tree.type) {

                            FieldInfo rcvrField = null;

                            // construct the actual value computing method (with the method call)
                            protected JCExpression makePushExpression() {
                                if (superToStatic) {  //TODO: should this be higher?
                                    // This is a super call, add the receiver so that the impl is called directly
                                    callArgs.prepend( receiver() );
                                } else if (renameToSuper || renameToThis || superCall || thisCall) {
                                    generateInLine = true;
                                }
                                // result is a block expression that has the definition of receiver$ at the beginning
                                return m().Apply(null, translatedImmutableMethodReference(), callArgs.toList());
                            }

                            @Override
                            protected void buildFields() {
                                // create a field in the BindingExpression for each argument
                                buildArgFields(targs, ArgKind.DEPENDENT);
                            }

                            JCExpression receiver() {
                                if (rcvrField == null) {
                                    Type rcvrType = getAttrEnv().enclClass.sym.type;
                                    rcvrField = new FieldInfo(JavafxDefs.receiverNameString, typeMorpher.typeMorphInfo(rcvrType), false, ArgKind.BOUND);
                                    return buildArgField(toJava.makeReceiver(diagPos, msym, getAttrEnv().enclClass.sym), rcvrField);
                                } else {
                                    return rcvrField.makeGetField();
                                }
                            }

                            JCExpression translatedImmutableMethodReference() {
                                Name name = functionName(msym, superToStatic, callBound);
                                JCExpression stor;
                                
                                 if (superToStatic || msym.isStatic()) {
                                    stor = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                                } else if (renameToSuper || superCall) {
                                    stor = m().Select(makeTypeTree(diagPos, toJava.currentClass.sym.type, false), names._super);
                                } else if (selector == null || thisCall || renameToThis) {
                                    stor = receiver();
                                } else {
                                    stor = makeTypeTree(diagPos, types.erasure(msym.owner.type), false);
                                }
                                return m().Select(stor, name);
                            }
                        }).doit();
                    }
                }
            }

            public JCExpression transMeth() {
                assert !useInvoke;
                JCExpression transMeth = toJava.translateAsUnconvertedValue(meth);
                JCExpression expr = null;
                
                if (superToStatic || msym.isStatic()) {
                    expr = makeTypeTree(diagPos, msym.owner.type, false);
                } else if (renameToSuper || superCall) {
                    expr = m().Ident(names._super);
                } else if (renameToThis || thisCall) {
                    expr = m().Ident(names._this);
                } else if (callBound) {
                    expr = ((JCFieldAccess) transMeth).getExpression();
                }
                
                Name name = functionName(msym, superToStatic, callBound);
                return expr == null ? transMeth : m().Select(expr, name);
            }

        }).doit());
    }

    @Override
    public void visitBinary(final JFXBinary tree) {
        DiagnosticPosition diagPos = tree.pos();
        switch (tree.getFXTag()) {
            case AND:
                result = new BoundResult(makeBoundConditional(diagPos,
                        syms.booleanType,
                        translate(tree.lhs, syms.booleanType),
                        translateForConditional(tree.rhs, syms.booleanType),
                        makeConstantLocation(diagPos, syms.booleanType, makeLit(diagPos, syms.booleanType, 0))));
                break;
            case OR:
                result = new BoundResult(makeBoundConditional(diagPos,
                        syms.booleanType,
                        translate(tree.lhs, syms.booleanType),
                        makeConstantLocation(diagPos, syms.booleanType, makeLit(diagPos, syms.booleanType, 1)),
                        translateForConditional(tree.rhs, syms.booleanType)));
                break;
            default:
                result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

                    protected JCExpression makePushExpression() {
                        return (new BinaryOperationTranslator(tree.pos(), tree) {

                            protected JCExpression translateArg(JFXExpression arg, Type type) {
                                Type transType = type == null ? arg.type : type;
                                return buildArgField(translate(arg, transType), transType);
                            }
                        }).doit();
                    }
                }.result();
                break;
        }
    }

    @Override
    public void visitUnary(final JFXUnary tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression expr = tree.getExpression();
        JCExpression transExpr = translate(expr);
        JCExpression res;

        switch (tree.getFXTag()) {
            case SIZEOF:
                res = runtime(diagPos, defs.BoundSequences_sizeof, List.of(makeLaziness(diagPos), transExpr) );
                break;
            case REVERSE:
                if (types.isSequence(expr.type)) {
                    // call runtime reverse of a sequence
                    res = runtime(diagPos, defs.BoundSequences_reverse, List.of(makeLaziness(diagPos), transExpr));
                } else {
                    // this isn't a sequence, just make it into a sequence
                    res = convert(expr.type, transExpr, tree.type);
                }
                break;
            case NOT:
                res = runtime(diagPos, defs.BoundOperators_op_boolean, List.of(makeLaziness(diagPos), transExpr, make.Literal(TypeTags.BOT, null), makeQualifiedTree(diagPos, opNOT)));
                break;
            case NEG:
                if (types.isSameType(tree.type, syms.javafx_DurationType)) {   
                    //TODO: totally wrong
                    res = make.at(diagPos).Apply(null,
                            make.at(diagPos).Select(translate(tree.arg), names.fromString("negate")), List.<JCExpression>nil());
                } else {
                    Type t = expr.type;
                    Type tub = types.unboxedType(t);
                    if (tub.tag != TypeTags.NONE) {
                        t = tub;
                    }
                    RuntimeMethod rm = (types.isSameType(t, syms.doubleType)) ? defs.BoundOperators_op_double
                            : (types.isSameType(t, syms.floatType)) ? defs.BoundOperators_op_float
                            : (types.isSameType(t, syms.longType)) ? defs.BoundOperators_op_long
                            : defs.BoundOperators_op_int;
                    res = runtime(diagPos, rm, List.of(makeLaziness(diagPos), transExpr, make.Literal(TypeTags.BOT, null), makeQualifiedTree(diagPos, opNEGATE)));
                }
                break;
            case PREINC:
            case PREDEC:
            case POSTINC:
            case POSTDEC:
                // should have caught this in attribution
                assert false : "++/-- in bind context f";
                res = transExpr;
                break;
            default:
                assert false : "unhandled unary operator";
                res = transExpr;
                break;
        }
        result = new BoundResult(convert(tree.type, res));
    }

    @Override
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        //TODO: code should be something like the below, but this requires a similar change to visitInterpolateValue
        /***
           DiagnosticPosition diagPos = tree.pos();
           JCExpression unbound = toJava.translate(tree, Wrapped.InNothing);
           result = makeConstantLocation(diagPos, targetType(tree.type), unbound);
         */

        // convert this time literal to a javafx.lang.Duration.valueOf() invocation
        JFXFunctionInvocation duration = timeLiteralToDuration(tree);

        // now convert that FX invocation to Java
        visitFunctionInvocation(duration); // sets result
    }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression makePushExpression() {
                return new InterpolateValueTranslator(tree, toJava) {

                    @Override
                    void setInstanceVariable(DiagnosticPosition diagPos, Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JCExpression transInit) {
                        JCExpression initRef = buildArgField(
                                transInit,
                                new FieldInfo(vsym.name, vsym.type, bindStatus.isBound()? ArgKind.BOUND : ArgKind.DEPENDENT)
                                );
                        super.setInstanceVariable(diagPos, instName, bindStatus, vsym, initRef);
                    }

                    @Override
                    protected JCExpression translateInstanceVariableInit(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
                        return translate(init, bindStatus, vsym.type);
                    }

                    protected JCExpression translateTarget() {
                        JCExpression target = translate(tree.attribute);
                        JCExpression val = toJava.callExpression(diagPos, makeExpression(syms.javafx_PointerType), "make", target);
                        return makeConstantLocation(diagPos, syms.javafx_KeyValueTargetType, val);
                    }
                }.doit();
            }
        }.result();
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
     * Moot visitors
     *
     */

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }

    @Override
    public void visitModifiers(JFXModifiers tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSkip(JFXSkip tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitThrow(JFXThrow tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTry(JFXTry tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitWhileLoop(JFXWhileLoop tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding";
    }


    @Override
    public void visitScript(JFXScript tree) {
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

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitContinue(JFXContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitReturn(JFXReturn tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitImport(JFXImport tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitBreak(JFXBreak tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitCatch(JFXCatch tree) {
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
    public void visitTypeArray(JFXTypeArray tree) {
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
    public void visitVarScriptInit(JFXVarScriptInit tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitVar(JFXVar tree) {
        // this is handled in translarVar
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitKeyFrameLiteral(JFXKeyFrameLiteral tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitErroneous(JFXErroneous tree) {
        assert false : "erroneous nodes shouldn't have gotten this far";
    }
}
