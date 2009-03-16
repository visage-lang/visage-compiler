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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.comp.JavafxToJava.DurationOperationTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.UseSequenceBuilder;
import com.sun.tools.javafx.comp.JavafxToJava.Translator;
import com.sun.tools.javafx.comp.JavafxToJava.FunctionCallTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.InstanciateTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.InterpolateValueTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.StringExpressionTranslator;
import com.sun.tools.javafx.comp.JavafxToJava.Locationness;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;
import java.util.HashSet;
import java.util.Set;
import static com.sun.tools.javac.code.TypeTags.*;

public class JavafxToBound extends JavafxTranslationSupport implements JavafxVisitor {
    protected static final Context.Key<JavafxToBound> jfxToBoundKey =
        new Context.Key<JavafxToBound>();

    static final boolean SEQUENCE_CONDITIONAL_INLINE = true;

    enum ArgKind { BOUND, DEPENDENT, FREE };

    /*
     * modules imported by context
     */
    private final JavafxToJava toJava;
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
        if (instance == null)
            instance = new JavafxToBound(context);
        return instance;
    }

    protected JavafxToBound(Context context) {
        super(context);
        
        context.put(jfxToBoundKey, this);

        toJava = JavafxToJava.instance(context);
        optStat = JavafxOptimizationStatistics.instance(context);
        
        computeElementsName = names.fromString("computeElements$");
    }

    /*** External entry points ***/

    JCExpression translate(JFXExpression tree, JavafxBindStatus bindStatus, TypeMorphInfo tmi) {
        JavafxBindStatus prevBindStatus = this.bindStatus;
        this.bindStatus = bindStatus;
        JCExpression res = translateGeneric(tree, tmi);
        this.bindStatus = prevBindStatus;
        return res;
    }

    JCExpression translate(JFXExpression tree, JavafxBindStatus bindStatus, Type type) {
        JavafxBindStatus prevBindStatus = this.bindStatus;
        this.bindStatus = bindStatus;
        JCExpression res = translateGeneric(tree, type);
        this.bindStatus = prevBindStatus;
        return res;
    }

    JCExpression translateForConditional(JFXExpression tree, Type type) {
        if (SEQUENCE_CONDITIONAL_INLINE && types.isSequence(type)) {
            return translate(tree, type);
        } else {
            return translate(tree, JavafxBindStatus.LAZY_UNIDIBIND, type);
        }
    }

    // only for re-entry use by SequenceBuilder
    JCExpression translate(JFXExpression tree) {
        return translateGeneric(tree);
    }

    private JCExpression translate(JFXExpression tree, TypeMorphInfo tmi) {
        return translateGeneric(tree, tmi);
    }

    JCExpression translate(JFXExpression tree, Type type) {
        return translateGeneric(tree, type);
    }
    
    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    private <TFX extends JFXExpression, TC extends JCTree> TC translateGeneric(TFX tree, TypeMorphInfo tmi) {
        TypeMorphInfo tmiPrevTarget = tmiTarget;
        this.tmiTarget = tmi;
        TC ret;
        if (tree == null) {
            ret = null;
        } else {
            JFXTree prevWhere = toJava.attrEnv.where;
            toJava.attrEnv.where = tree;
            tree.accept(this);
            toJava.attrEnv.where = prevWhere;
            ret = (TC) this.result;
            this.result = null;
        }
        this.tmiTarget = tmiPrevTarget;
        return ret;
    }

    private <TFX extends JFXExpression, TC extends JCTree> TC translateGeneric(TFX tree, Type type) {
        return translateGeneric(tree, typeMorpher.typeMorphInfo(type));
    }

    private <TFX extends JFXExpression, TC extends JCTree> TC translateGeneric(TFX tree) {
        return translateGeneric(tree, (TypeMorphInfo) null);
    }

    private List<JCExpression> translate(List<JFXExpression> trees, Type methType, boolean usesVarArgs) {
        return translateGeneric(trees, methType, usesVarArgs);
    }

    private <TFX extends JFXExpression, TC extends JCExpression> List<TC> translateGeneric(List<TFX> trees, Type methType, boolean usesVarArgs) {
        ListBuffer<TC> translated = ListBuffer.lb();
        boolean handlingVarargs = false;
        Type formal = null;
        List<Type> t = methType.getParameterTypes();
        for (List<TFX> l = trees; l.nonEmpty(); l = l.tail) {
            if (!handlingVarargs) {
                formal = t.head;
                t = t.tail;
                if (usesVarArgs && t.isEmpty()) {
                    formal = types.elemtype(formal);
                    handlingVarargs = true;
                }
            }
            TC tree = translateGeneric(l.head, formal);
            if (tree != null) {
                if (tree.type == null) { // if not set by convert()
                    tree.type = formal; // mark the type to declare the holder of this arg
                }
                translated.append(tree);
            }
        }
        List<TC> args = translated.toList();

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

    private JCExpression convert(Type inType, JCExpression tree, Type targetType) {
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
                            List.of(targetTypeInfo, convert(inType, tree, targetElementType)));
                } else {
                    // this additional test is needed because wildcards compare as different
                    Type sourceElementType = types.elementType(inType);
                    if (!types.isSameType(sourceElementType, targetElementType)) {
                        if (types.isNumeric(sourceElementType) && types.isNumeric(targetElementType)) {
                            tree = convertNumericSequence(diagPos,
                                    true,
                                    tree,
                                    sourceElementType,
                                    targetElementType);
                        } else {
                            JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
                            tree = runtime(diagPos, defs.BoundSequences_upcast, List.of(targetTypeInfo, tree));
                        }
                    }
                }
            } else if (targetType.isPrimitive()) {
                if (inType.isPrimitive()) {
                    tree = runtime(diagPos,
                            numericConvertMethod(targetType),
                            List.of(tree, makeTypeInfo(diagPos, inType)));
                }
                //TODO: boxed inType
            } else {
                List<JCExpression> typeArgs = List.of(makeTypeTree(diagPos, targetType, true),
                        makeTypeTree(diagPos, syms.boxIfNeeded(inType), true));
                Type inRealType = typeMorpher.typeMorphInfo(inType).getRealType();
                JCExpression inClass = makeTypeInfo(diagPos, inRealType);
                tree = runtime(diagPos, defs.Locations_upcast, typeArgs, List.of(inClass, tree));
            }
        }
        tree.type = targetType; // as a way of passing it to methods which needs to know the target type
        return tree;
    }
    //where
    private RuntimeMethod numericConvertMethod(Type type) {
        switch (type.tag) {
            case BYTE:
                return defs.Locations_toByteLocation;
            case SHORT:
                return defs.Locations_toShortLocation;
            case INT:
                return defs.Locations_toIntLocation;
            case LONG:
                return defs.Locations_toLongLocation;
            case FLOAT:
                return defs.Locations_toFloatLocation;
            case DOUBLE:
                return defs.Locations_toDoubleLocation;
            default:
                throw new AssertionError("Should not reach here");
        }
    }
    
    /**
     * Return the list of local variables accessed, but not defined within the FX expression.
     * @param expr
     * @return
     */
    private List<VarSymbol> localVars(JFXExpression expr) {
        final ListBuffer<VarSymbol> lb = ListBuffer.<VarSymbol>lb();
        final Set<VarSymbol> exclude = new HashSet<VarSymbol>();
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

            @Override
            public void visitVar(JFXVar tree) {
                exclude.add(tree.sym);
                super.visitVar(tree);
            }
        }.scan(expr);

        return lb.toList();
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

    private abstract class ClosureTranslator extends Translator {

        protected final TypeMorphInfo tmiResult;
        protected final int typeKindResult;
        protected final Type elementTypeResult;

        // these only used when fields are built
        ListBuffer<JCTree> members = ListBuffer.lb();
        ListBuffer<JCStatement> fieldInits = ListBuffer.lb();
        int dependents = 0;
        ListBuffer<JCExpression> callArgs = ListBuffer.lb();
        int argNum = 0;

        ClosureTranslator(DiagnosticPosition diagPos, Type resultType) {
            this(diagPos, JavafxToBound.this.toJava, (tmiTarget != null) ? tmiTarget : typeMorpher.typeMorphInfo(resultType));
        }

        private ClosureTranslator(DiagnosticPosition diagPos, JavafxToJava toJava, TypeMorphInfo tmiResult) {
            super(diagPos, toJava);
            this.tmiResult = tmiResult;
            typeKindResult = tmiResult.getTypeKind();
            elementTypeResult = boxedElementType(tmiResult.getLocationType()); // want boxed, JavafxTypes version won't work
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

        protected JCTree makeClosureMethod(Name methName, JCExpression expr, List<JCVariableDecl> params, Type returnType, long flags) {
            return toJava.makeMethod(diagPos, methName, List.<JCStatement>of((returnType == syms.voidType) ? m().Exec(expr) : m().Return(expr)), params, returnType, flags);
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
            return typeParams.isEmpty()? clazz : m().TypeApply(clazz, typeParams.toList());
        }
        
        protected abstract List<JCExpression> makeConstructorArgs();

        protected JCExpression buildClosure() {
            List<JCTree> body = makeBody();
            JCClassDecl classDecl = body==null? null : m().AnonymousClassDef(m().Modifiers(0L), body);
            List<JCExpression> typeArgs = List.nil();
            return m().NewClass(null/*encl*/, typeArgs, makeBaseClass(), makeConstructorArgs(), classDecl);
        }

        protected JCExpression doit() {
            return buildClosure();
        }


        // field building support

        protected List<JCTree> completeMembers() {
            members.append(m().Block(0L, fieldInits.toList()));
            return members.toList();
        }

        protected JCExpression makeLocationGet(JCExpression locExpr, int typeKind) {
            Name getMethodName = defs.locationGetMethodName[typeKind];
            JCFieldAccess select = m().Select(locExpr, getMethodName);
            return m().Apply(null, select, List.<JCExpression>nil());
        }
        
        class FieldInfo {
            final String desc;
            final int num;
            final TypeMorphInfo tmi;
            final boolean isLocation;

            FieldInfo(Type type) {
                this((String)null, type);
            }

            FieldInfo(Name descName, Type type) {
                this(descName.toString(), type);
            }

            FieldInfo(Name descName, TypeMorphInfo tmi) {
                this(descName.toString(), tmi);
            }

            FieldInfo(String desc, Type type) {
                this(desc, typeMorpher.typeMorphInfo(type));
            }

            FieldInfo(TypeMorphInfo tmi) {
                this((String)null, tmi);
            }

            FieldInfo(String desc, TypeMorphInfo tmi) {
                this(desc, tmi, true);
            }

            FieldInfo(String desc, TypeMorphInfo tmi, boolean isLocation) {
                this.desc = desc;
                this.num = argNum++;
                this.tmi = tmi;
                this.isLocation = isLocation;
            }

            JCExpression makeGetField() {
                return makeGetField(tmi.getTypeKind());
            }

            JCExpression makeGetField(int typeKind) {
                return isLocation ? makeLocationGet(makeAccess(this), typeKind) : makeAccess(this);
            }

            Type type() {
                return isLocation ? tmi.getLocationType() : tmi.getRealBoxedType();
            }
        }

        private Name argAccessName(FieldInfo fieldInfo) {
            return names.fromString("arg$" + fieldInfo.num);
        }

        JCExpression makeAccess(FieldInfo fieldInfo) {
            return m().Ident(argAccessName(fieldInfo));
        }

        protected void makeLocationField(JCExpression targ, FieldInfo fieldInfo) {
            fieldInits.append( m().Exec( m().Assign(makeAccess(fieldInfo), targ)) );
            members.append(m().VarDef(
                    m().Modifiers(Flags.PRIVATE),
                    argAccessName(fieldInfo),
                    makeExpression(fieldInfo.type()),
                    null));
        }

        protected JCExpression buildArgField(JCExpression arg, Type type) {
            return buildArgField(arg, type, ArgKind.DEPENDENT);
        }

        protected JCExpression buildArgField(JCExpression arg, Type type, ArgKind kind) {
            return buildArgField(arg, new FieldInfo(type), kind);
        }

        protected JCExpression buildArgField(JCExpression arg, FieldInfo fieldInfo) {
            return buildArgField(arg, fieldInfo, ArgKind.DEPENDENT);
        }

        protected JCExpression buildArgField(JCExpression arg, FieldInfo fieldInfo, ArgKind kind) {
            // translate the method arg into a Location field of the BindingExpression
            // XxxLocation arg$0 = ...;
            makeLocationField(arg, fieldInfo);

            // build a list of these args, for use as dependents -- arg$0, arg$1, ...
            if (kind == ArgKind.BOUND) {
                return makeAccess(fieldInfo);
            } else {
                if (fieldInfo.num > 32) {
                    log.error(diagPos, MsgSym.MESSAGE_BIND_TOO_COMPLEX);
                }
                if (kind == ArgKind.DEPENDENT) {
                    dependents |= 1 << fieldInfo.num;
                }

                // set up these arg for the call -- arg$0.getXxx()
                return fieldInfo.makeGetField();
            }
         }

        protected void buildArgFields(List<JCExpression> targs, ArgKind kind) {
            for (JCExpression targ : targs) {
                assert targ.type != null : "caller is supposed to decorate the translated arg with its type";
                callArgs.append( buildArgField(targ, targ.type, kind) );
            }
        }
    }

    void scriptBegin() {
        bects = ListBuffer.lb();
    }

    List<JCTree> scriptComplete(DiagnosticPosition diagPos) {
        ListBuffer<JCTree> trees = ListBuffer.lb();
        // Add _Bindings class
        if (!bects.isEmpty()) {
            ListBuffer<JCCase> cases = ListBuffer.lb();
            for (BindingExpressionClosureTranslator b : bects) {
                if (!b.generateInLine) {
                    cases.append(b.makeBindingCase());
                }
            }

            JCStatement swit = make.at(diagPos).Switch(make.at(diagPos).Ident(defs.bindingIdName), cases.toList());

            JCTree computeMethod = makeMethod(diagPos, defs.computeMethodName, List.of(swit), null, syms.voidType, Flags.PUBLIC);

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

            JCClassDecl bindingClass = make.at(diagPos).ClassDef(
                    make.at(diagPos).Modifiers(Flags.PRIVATE | Flags.STATIC),
                    defs.scriptBindingClassName,
                    List.<JCTypeParameter>nil(),
                    makeQualifiedTree(diagPos, JavafxDefs.scriptBindingExpressionsString),
                    List.<JCExpression>nil(),
                    List.of(computeMethod, constr));
            trees.append(bindingClass);
        }
        return trees.toList();
    }

    private JCExpression wrapInBindingExpression(final DiagnosticPosition diagPos,
            final Type resultType,
            final JCExpression texpr,
            final JFXExpression expr) {
        return new BindingExpressionClosureTranslator(diagPos, resultType) {

            private JCVariableDecl renamingVar(Name name, Name vname, Type type, boolean isLocation) {
                TypeMorphInfo tmi = typeMorpher.typeMorphInfo(type);
                Type varType = isLocation? tmi.getLocationType() : type;
                FieldInfo rcvrField = new FieldInfo(name.toString(), typeMorpher.typeMorphInfo(varType), false);
                return m().VarDef(
                        m().Modifiers(Flags.FINAL),
                        vname,
                        makeTypeTree(diagPos, varType),
                        buildArgField(m().Ident(name), rcvrField, ArgKind.FREE));
            }

            protected JCExpression makePushExpression() {
                ListBuffer<JCStatement> renamings = ListBuffer.<JCStatement>lb();
                switch (toJava.inInstanceContext) {
                    case ScriptAsStatic:
                        // No receiver
                        break;
                    case InstanceAsStatic:
                        renamings.append( renamingVar(defs.receiverName, defs.receiverName, toJava.attrEnv.enclClass.type, false) );
                        break;
                    case InstanceAsInstance:
                        renamings.append( renamingVar(names._this, defs.receiverName, toJava.attrEnv.enclClass.type, false) );
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
                                new FieldInfo(vsym.type),
                                bindStatus.isBound()? ArgKind.BOUND : ArgKind.DEPENDENT);
                        setInstanceVariable(init.pos(), instName, bindStatus, vsym, initRef);
                    }
                }.doit();
            }
        }.doit();
    }

    @Override
    public void visitStringExpression(final JFXStringExpression tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), syms.stringType) {

            protected JCExpression makePushExpression() {
                return new StringExpressionTranslator(tree, toJava) {

                    protected JCExpression translateArg(JFXExpression arg) {
                        return buildArgField(translate(arg), arg.type);
                    }
                }.doit();
            }
        }.doit();
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = makeConstantLocation(tree.pos(), targetType(tree.type), toJava.makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type) );
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
        result = makeBlockExpression(diagPos, //TODO tree.flags lost
                translatedVars.toList(),
                translate(value, tmiTarget) );
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
        result = makeBlockExpression(diagPos,
                List.of(varDecl, setStmt),
                make.at(diagPos).Ident(varDecl.name));
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
            result = convert(tree.type, toJava.translateAsLocation(tree)); //TODO -- for now punt, translate like normal case
            return;
        }
        DiagnosticPosition diagPos = tree.pos();

        Symbol owner = tree.sym.owner;
        if (types.isJFXClass(owner) && typeMorpher.requiresLocation(tree.sym)) {
            if (tree.sym.isStatic()) {
                // if this is a static reference to an attribute, eg.   MyClass.myAttribute
                JCExpression classRef = makeTypeTree( diagPos,types.erasure(tree.sym.owner.type), false);
                result = convert(tree.type, make.at(diagPos).Select(classRef, attributeFieldName(tree.sym)));
            } else {
                // this is a dynamic reference to an attribute
                final JFXExpression expr = tree.getExpression();
                result = makeBoundSelect(diagPos,
                        tree.type,
                        new BindingExpressionClosureTranslator(tree.pos(), typeMorpher.baseLocation.type) {

                            protected JCExpression makePushExpression() {
                                return convert(tree.type, toJava.convertVariableReference(diagPos,
                                        m().Select(
                                            buildArgField(
                                                translate(expr),
                                                new FieldInfo("selector", expr.type)),
                                            tree.getIdentifier()),
                                        tree.sym,
                                        Locationness.AsLocation));
                            }
                        });
            }
        } else {
            if (tree.sym.isStatic()) {
                // This is a static reference to a Java member or elided member e.g. System.out -- do unbound translation, then wrap
                result = this.makeUnboundLocation(diagPos, targetType(tree.type), toJava.translateAsUnconvertedValue(tree));
            } else {
                // This is a dynamic reference to a Java member or elided member
                result = (new BindingExpressionClosureTranslator(diagPos, tree.type) {

                    private JFXExpression selector = tree.getExpression();
                    private TypeMorphInfo tmiSelector = typeMorpher.typeMorphInfo(selector.type);
                    private Name selectorName = getSyntheticName("selector");
                    private FieldInfo selectorField = new FieldInfo(selectorName, tmiSelector);

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
                }).doit();
            }
        }
    }

    @Override
    public void visitIdent(JFXIdent tree)   {  //TODO: don't use toJava
       // assert (tree.sym.flags() & Flags.PARAMETER) != 0 || tree.name == names._this || tree.sym.isStatic() || toJava.requiresLocation(typeMorpher.varMorphInfo(tree.sym)) : "we are bound, so should have been marked to morph: " + tree;
        JCExpression transId = toJava.translateAsLocation(tree);
        result = convert(tree.type, transId );
    }

    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) { //done
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = boxedElementType(targetType(tree.type));
        UseSequenceBuilder builder = toJava.useBoundSequenceBuilder(tree.pos(), elemType, tree.getItems().length());
        stmts.append(builder.makeBuilderVar());
        for (JFXExpression item : tree.getItems()) {
            stmts.append(builder.addElement( item ) );
        }
        result = makeBlockExpression(tree.pos(), stmts, builder.makeToSequence());
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
        args.append( translate( tree.getLower(), tmi ));
        args.append( translate( tree.getUpper(), tmi ));
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull(), tmi ));
        }
        if (tree.isExclusive()) {
            args.append( make.at(diagPos).Literal(TypeTags.BOOLEAN, 1) );
        }
        result = convert(types.sequenceType(elemType), runtime(diagPos, defs.BoundSequences_range, args.toList()));
    }

    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) { //done
        DiagnosticPosition diagPos = tree.pos();
        if (types.isSequence(tree.type)) {
            Type elemType = types.elementType(targetType(tree.type));
            result = runtime(diagPos, defs.BoundSequences_empty, List.of(makeTypeInfo(diagPos, elemType)));
        } else {
            result = makeConstantLocation(diagPos, targetType(tree.type), makeNull(diagPos));
        }
    }

    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {   //done
        DiagnosticPosition diagPos = tree.pos();
        result = convert(tree.type, runtime(diagPos, defs.BoundSequences_element,
                List.of(translate(tree.getSequence()),
                translate(tree.getIndex(), syms.intType))));
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {    //done
        DiagnosticPosition diagPos = tree.pos();
        result = runtime(diagPos, 
                tree.getEndKind()==SequenceSliceTree.END_EXCLUSIVE? defs.BoundSequences_sliceExclusive : defs.BoundSequences_slice,
                List.of(
                    makeTypeInfo(diagPos, types.elementType(targetType(tree.type))),
                    translate(tree.getSequence()),
                    translate(tree.getFirstIndex()),
                    tree.getLastIndex()==null? makeNull(diagPos) : translate(tree.getLastIndex())
                    ));
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
        result = (new Translator( tree.pos(), toJava ) {

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
                    JCExpression where = translate(clause.getWhereExpression());
                    if (where != null) {
                        if (whereTest == null) {
                            whereTest = where;
                        } else {
                            whereTest = runtime(diagPos, defs.BoundOperators_and_bb, List.of(whereTest, where));
                        }
                    }
                }
                if (types.isSequence(body.type)) {
                    // the body is a sequence, desired type is the same as for the for-loop
                    tbody = whereTest!=null? translateForConditional(body, tmiTarget.getRealType()) : translate(body, tmiTarget);
                } else {
                    // the body is not a sequence, desired type is the element tpe need for for-loop
                    Type elemType = types.unboxedTypeOrType(resultElementType);
                    JCExpression single = whereTest!=null? translateForConditional(body, elemType) : translate(body, elemType);
                    List<JCExpression> args = List.of(makeResultClass(), single);
                    tbody = runtime(diagPos, defs.BoundSequences_singleton, args);
                }
                if (whereTest != null) {
                    tbody = makeBoundConditional(diagPos,
                            tree.type,
                            whereTest,
                            tbody,
                            runtime(diagPos, defs.BoundSequences_empty, List.of(makeTypeInfo(diagPos, resultElementType)))
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
                    transSeq = runtime(diagPos, defs.BoundSequences_singleton, List.of(makeResultClass(), transSeq));
                }
                List<JCExpression> constructorArgs = List.of(
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
        }).doit();
    }

    public void visitIndexof(JFXIndexof tree) {
        assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
        JCExpression transIndex = make.at(tree.pos()).Ident(indexVarName(tree.fname));
        VarSymbol vsym = (VarSymbol)tree.clause.getVar().sym;
        if (toJava.requiresLocation(vsym)) {
            // from inside the bind, already a Location
            result = convert(tree.type, transIndex);
        } else {
            // it came from outside of the bind, make it into a Location
            result = makeConstantLocation(tree.pos(), targetType(tree.type), transIndex);
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
        return (new ClosureTranslator(bodyExpr.pos(), resultType) {

            protected List<JCTree> makeBody() {
                return List.<JCTree>of(
                        makeClosureMethod(defs.invokeName, bodyExpr, null, tmiResult.getLocationType(), Flags.PUBLIC));
            }

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
        result = makeBoundConditional(tree.pos(),
                targetType,
                translate(tree.getCondition()) ,
                translateForConditional(tree.getTrueExpression(), targetType),
                translateForConditional(tree.getFalseExpression(), targetType));
    }

    @Override
    public void visitParens(JFXParens tree) { //done
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).Parens(expr);
    }

    @Override
    public void visitInstanceOf(final JFXInstanceOf tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

            protected JCExpression makePushExpression() {
                Type type = tree.clazz.type;
                if (type.isPrimitive())
                    type = types.boxedClass(type).type;
                return m().TypeTest(
                        buildArgField(translate(tree.expr),
                        new FieldInfo(defs.toTestName, tree.expr.type)),
                        makeExpression(type) );
            }
        }.doit();
    }

    @Override
    public void visitTypeCast(final JFXTypeCast tree) {
        result = new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

           protected JCExpression makePushExpression() {
                return makeTypeCast(tree.pos(), tree.clazz.type, tree.expr.type,
                         buildArgField(translate(tree.expr), new FieldInfo(defs.toBeCastName, tree.expr.type)));
            }
        }.doit();
    }

    @Override
    public void visitLiteral(JFXLiteral tree) {
        final DiagnosticPosition diagPos = tree.pos();
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = types.elementType(targetType(tree.type));
            result = runtime(diagPos, defs.BoundSequences_empty, List.of(makeTypeInfo(diagPos, elemType)));
        } else {
            Type targetType = targetType(tree.type);
            JCExpression unbound = toJava.convertTranslated(make.at(diagPos).Literal(tree.typetag, tree.value), diagPos, tree.type, targetType);
            result = makeConstantLocation(diagPos, targetType, unbound);
        }
    }

    /**
     * Translator for Java method and non-bound JavaFX functions.
     */
    private abstract class BindingExpressionClosureTranslator extends ClosureTranslator {

        final Type actualTranslatedType;
        final int id;
        JCStatement pushStatement;
        final ListBuffer<JCExpression> argInits = ListBuffer.lb();
        final ListBuffer<JCStatement> preDecls = ListBuffer.lb();
        boolean generateInLine = false;

        BindingExpressionClosureTranslator(DiagnosticPosition diagPos, Type resultType) {
            super(diagPos, resultType);
            this.id = bects.size();
            this.actualTranslatedType = resultType;
            bects.append(this);
        }

        protected abstract JCExpression makePushExpression();

        protected void buildFields() {
            // by default do this dynamically
        }

        JCCase makeBindingCase() {
            return m().Case(m().Literal(id), List.<JCStatement>of(
                        pushStatement,
                        m().Break(null)));
        }

        @Override
        JCExpression makeAccess(FieldInfo fieldInfo) {
            JCExpression uncast;
            if (fieldInfo.num < 2) {
                // arg$0 and arg$1, use Ident
                uncast = super.makeAccess(fieldInfo);
            } else {
                // moreArgs
                uncast = m().Indexed(m().Ident(defs.moreArgsName), m().Literal(fieldInfo.num - 2));
            }
            // These are just "Location" -- cast to their XxxLocation type
            return m().TypeCast(makeExpression(fieldInfo.type()), uncast);
        }

        protected List<JCTree> makeBody() {
            buildFields();
            // build first since this may add dependencies
            JCExpression resultVal = makePushExpression();
            if (tmiTarget != null && actualTranslatedType != typeMorpher.baseLocation.type) {
                // If we have a target type and this isn't a Location yielding translation (which handles it's own), do any needed type conversion
                resultVal = toJava.convertTranslated(resultVal, diagPos, actualTranslatedType, tmiTarget.getRealType());
            }
            pushStatement = callStatement(diagPos, null, "pushValue", resultVal);

            if (generateInLine) {
                JCTree computeMethod = makeMethod(diagPos, defs.computeMethodName, List.of(pushStatement), null, syms.voidType, Flags.PUBLIC);
                members.append(computeMethod);
                return completeMembers();
            } else {
                return null;
            }
        }

        protected JCExpression makeBaseClass() {
            return m().Ident(defs.scriptBindingClassName);
        }

        @Override
        protected void makeLocationField(JCExpression targ, FieldInfo fieldInfo) {
            // We use the fields in the base class, just store to Location constructions for use in the constructor args
            argInits.append(targ);
        }

        protected List<JCExpression> makeConstructorArgs() {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            // arg: id
            args.append(m().Literal(id));
            List<JCExpression> inits = argInits.toList();
            assert inits.length() == argNum : "Mismatch Args: " + argNum + ", Inits: " + inits.length();
            // arg: arg$0
            if (argNum > 0) {
                args.append(inits.head);
                inits = inits.tail;
            } else {
                args.append(m().Literal(TypeTags.BOT, null));
            }
            // arg: arg$1
            if (argNum > 1) {
                args.append(inits.head);
                inits = inits.tail;
            } else {
                args.append(m().Literal(TypeTags.BOT, null));
            }
            // arg: moreArgs
            if (argNum > 2) {
                args.append(m().NewArray(makeExpression(syms.objectType), List.<JCExpression>nil(), inits));
            } else {
                args.append(m().Literal(TypeTags.BOT, null));
            }
            // arg: dependents
            args.append(m().Literal(TypeTags.INT, dependents));
            
            return args.toList();
        }

        @Override
        protected JCExpression doit() {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            if (tmiResult.getTypeKind() == TYPE_KIND_OBJECT) {
                args.append(makeDefaultValue(diagPos, tmiResult));
            }
            args.append(makeLaziness(diagPos));
            args.append(buildClosure());
            JCExpression varResult = makeLocationLocalVariable(tmiResult, diagPos, args.toList());
            if (preDecls.nonEmpty()) {
                return toJava.makeBlockExpression(diagPos, preDecls, varResult);
            } else {
                return varResult;
            }
        }
    }

    @Override
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        //TODO: painfully in need of refactoring
        result = (new FunctionCallTranslator(tree, toJava) {

            final List<JCExpression> typeArgs = toJava.translateExpressions(tree.typeargs); //TODO: should, I think, be nil list
            final List<JCExpression> targs = translate(tree.args, meth.type, usesVarArgs);

            public JCExpression doit() {
                if (callBound) {
                    if (selectorMutable) {
                        return makeBoundSelect(diagPos,
                                tree.type,
                                new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

                                    protected JCExpression makePushExpression() {
                                        JCExpression transSelect = buildArgField(translate(selector), new FieldInfo("selector", selector.type));
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
                            private FieldInfo selectorField = new FieldInfo(selectorName, tmiSelector);

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
                                } else if (renameToSuper || superCall) {
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
                                    Type rcvrType = toJava.attrEnv.enclClass.sym.type;
                                    rcvrField = new FieldInfo(JavafxDefs.receiverNameString, typeMorpher.typeMorphInfo(rcvrType), false);
                                    return buildArgField(toJava.makeReceiver(diagPos, msym, toJava.attrEnv.enclClass.sym), rcvrField, ArgKind.BOUND);
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
                                } else if (selector == null || thisCall) {
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
                } else if (callBound) {
                    expr = ((JCFieldAccess) transMeth).getExpression();
                }
                
                Name name = functionName(msym, superToStatic, callBound);
                return expr == null ? transMeth : m().Select(expr, name);
            }

        }).doit();
    }

    private class BinaryTranslator {
        final JFXBinary tree;
        final DiagnosticPosition diagPos;
        final JFXExpression l;
        final JFXExpression r;
        final boolean lBoxed;
        final boolean rBoxed;
        final Type lType;
        final Type rType;

        BinaryTranslator(final JFXBinary tree) {
            this.tree = tree;
            this.diagPos = tree.pos();
            this.l = tree.lhs;
            this.r = tree.rhs;
            Type tubl = types.unboxedType(tree.lhs.type);
            lBoxed = tubl.tag != TypeTags.NONE;
            lType = lBoxed? tubl : tree.lhs.type;
            Type tubr = types.unboxedType(tree.rhs.type);
            rBoxed = tubr.tag != TypeTags.NONE;
            rType = rBoxed? tubr : tree.rhs.type;
        }

        RuntimeMethod opMethod(boolean isCompare) {
            if (types.isSameType(rType, syms.doubleType) || types.isSameType(lType, syms.doubleType)) {
                return isCompare? defs.BoundOperators_cmp_double : defs.BoundOperators_op_double;
            }
            if (types.isSameType(rType, syms.floatType) || types.isSameType(lType, syms.floatType)) {
                return isCompare? defs.BoundOperators_cmp_float : defs.BoundOperators_op_float;
            }
            if (types.isSameType(rType, syms.longType) || types.isSameType(lType, syms.longType)) {
                return isCompare? defs.BoundOperators_cmp_long : defs.BoundOperators_op_long;
            }
                return isCompare? defs.BoundOperators_cmp_int : defs.BoundOperators_op_int;
        }

        JCExpression makeBinaryOperator(String op, RuntimeMethod rm) {
            final JCExpression lhs = translate(l);
            final JCExpression rhs = translate(r);
            return runtime(diagPos, rm, List.of(makeLaziness(diagPos), lhs, rhs, makeQualifiedTree(diagPos, op)));
        }

        JCExpression makeBinaryArithmeticOperator(String op) {
            return makeBinaryOperator(op, opMethod(false));
        }

        JCExpression makeBinaryComparisonOperator(String op) {
            return makeBinaryOperator(op, opMethod(true));
        }

        boolean isNumeric(Type opType) {
            switch (opType.tag) {
                case BYTE:
                case CHAR:
                case SHORT:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                    return true;
            }
            return false;
        }

        JCExpression makeBinaryEqualityOperator(String op) {
            if (isNumeric(lType) && isNumeric(rType)) {
                return makeBinaryComparisonOperator(op);
            } else {
                final JCExpression lhs = translate(l);
                final JCExpression rhs = translate(r);
                RuntimeMethod rm = (lType.tag == BOOLEAN && rType.tag == BOOLEAN) ? defs.BoundOperators_op_boolean : defs.BoundOperators_cmp_other;
                return runtime(diagPos, rm, List.of(makeLaziness(diagPos), lhs, rhs, makeQualifiedTree(diagPos, op)));
            }
        }

        JCExpression doit() {
            if ((types.isSameType(lType, syms.javafx_DurationType) ||
                    types.isSameType(rType, syms.javafx_DurationType)) &&
                    (tree.getFXTag() != JavafxTag.EQ && tree.getFXTag() != JavafxTag.NE)) {
                // This is a Duration operation (other than equality).  Use the Duration translator
                return new BindingExpressionClosureTranslator(tree.pos(), tree.type) {

                    protected JCExpression makePushExpression() {
                        return new DurationOperationTranslator(diagPos, tree.getFXTag(),
                                buildArgField(translate(l), lType), buildArgField(translate(r), rType),
                                lType, rType, toJava).doit();
                    }
                }.doit();
            }
            switch (tree.getFXTag()) {
                case PLUS:
                    return makeBinaryArithmeticOperator(opPLUS);
                case MINUS:
                    return makeBinaryArithmeticOperator(opMINUS);
                case DIV:
                    return makeBinaryArithmeticOperator(opDIVIDE);
                case MUL:
                    return makeBinaryArithmeticOperator(opTIMES);
                case MOD:
                    return makeBinaryArithmeticOperator(opMODULO);

                case LT:
                    return makeBinaryComparisonOperator(opLT);
                case LE:
                    return makeBinaryComparisonOperator(opLE);
                case GT:
                    return makeBinaryComparisonOperator(opGT);
                case GE:
                    return makeBinaryComparisonOperator(opGE);

                case EQ:
                    return makeBinaryEqualityOperator(opEQ);
                case NE:
                    return makeBinaryEqualityOperator(opNE);

                case AND:
                    return makeBoundConditional(diagPos,
                            syms.booleanType,
                            translate(l, syms.booleanType),
                            translateForConditional(r, syms.booleanType),
                            makeConstantLocation(diagPos, syms.booleanType, makeLit(diagPos, syms.booleanType, 0))
                        );
                case OR:
                    return makeBoundConditional(diagPos,
                            syms.booleanType,
                            translate(l, syms.booleanType),
                            makeConstantLocation(diagPos, syms.booleanType, makeLit(diagPos, syms.booleanType, 1)),
                            translateForConditional(r, syms.booleanType)
                         );
                default:
                    assert false : "unhandled binary operator";
                    return translate(l);
            }
        }
    }

    @Override
    public void visitBinary(final JFXBinary tree) {
        result = convert(tree.type, new BinaryTranslator(tree).doit());
    }

    @Override
    public void visitUnary(final JFXUnary tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression expr = tree.getExpression();
        JCExpression transExpr = translate(expr);
        JCExpression res;

        switch (tree.getFXTag()) {
            case SIZEOF:
                res = runtime(diagPos, defs.BoundSequences_sizeof, List.of(transExpr) );
                break;
            case REVERSE:
                if (types.isSequence(expr.type)) {
                    // call runtime reverse of a sequence
                    res = runtime(diagPos, defs.BoundSequences_reverse, List.of(transExpr));
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
        result = convert(tree.type, res);
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
                                new FieldInfo(vsym.name, vsym.type),
                                bindStatus.isBound()? ArgKind.BOUND : ArgKind.DEPENDENT);
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
        }.doit();
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
