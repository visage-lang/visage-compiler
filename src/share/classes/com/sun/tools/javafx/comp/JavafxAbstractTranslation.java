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
import com.sun.tools.mjavac.tree.TreeMaker;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.jvm.Target;
import com.sun.tools.mjavac.tree.TreeInfo;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.lang.model.type.TypeKind;


/**
 *
 * @author Robert Field
 */
public abstract class JavafxAbstractTranslation<R> 
                             extends JavafxTranslationSupport
                             implements JavafxVisitor {

    /*
     * the result of translating a tree by a visit method
     */
    R result;

    final JavafxOptimizationStatistics optStat;
    final Target target;

    protected JavafxEnv<JavafxAttrContext> attrEnv;

    enum ReceiverContext {
        // In a script function or script var init, implemented as a static method
        ScriptAsStatic,
        // In an instance function or instance var init, implemented as static
        InstanceAsStatic,
        // In an instance function or instance var init, implemented as an instance method
        InstanceAsInstance,
        // Should not see code in this state
        Oops
    }
    ReceiverContext inInstanceContext = ReceiverContext.Oops;

    JavafxToJava toJava; //TODO: this should go away

    protected JavafxAbstractTranslation(Context context, JavafxToJava toJava) {
        super(context);
        this.optStat = JavafxOptimizationStatistics.instance(context);
        this.toJava = toJava==null? (JavafxToJava)this : toJava;  //TODO: temp hack
        this.target = Target.instance(context);
    }

    /** Translate a single expression.
     */
    R translate(JFXTree tree) {
        R ret;

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
        return ret;
    }

    /** Translate a list of expressions.
     */
    <T extends JFXTree> List<R> translate(List<T> trees) {
        ListBuffer<R> translated = ListBuffer.lb();
        if (trees == null) {
            return null;
        }
        for (List<T> l = trees; l.nonEmpty(); l = l.tail) {
            R tree = translate(l.head);
            if (tree != null) {
                translated.append(tree);
            }
        }
        return translated.toList();
    }

    private static final Pattern DATETIME_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT]");

    enum ArgKind { BOUND, DEPENDENT, FREE };

    public enum Locationness {
        // We need a Location
        AsLocation,
        // We need a value
        AsValue
    }

    /**
     * @return the attrEnv
     */
    protected JavafxEnv<JavafxAttrContext> getAttrEnv() {
        return toJava.attrEnv;
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

    /**
     * Determine is a local class is referenced, if so need local generation
     * @param expr Expression to check
     * @return if must generate locally
     */
    boolean mustGenerateInline(JFXTree expr) {
        class InlineChecker extends JavafxTreeScanner {

            boolean mustBeInline = false;

            void checkSymbol(Symbol sym) {
                if (sym instanceof ClassSymbol) {
                    ClassSymbol csym = (ClassSymbol) sym;
                    int ownerKind = csym.owner.kind;
                    if (ownerKind != Kinds.TYP && ownerKind != Kinds.PCK) {
                        // Reference to a local class -- can get to it from script class -- so need inline closure
                        mustBeInline = true;
                    }
                }
            }

            @Override
            public void visitIdent(JFXIdent tree) {
                checkSymbol(tree.sym);
            }
        }
        InlineChecker checker = new InlineChecker();
        checker.scan(expr);
        for (VarSymbol vsym : localVars(expr)) {
            checker.checkSymbol(vsym.type.tsym);
        }
        checker.checkSymbol(getAttrEnv().enclClass.type.tsym);

        return checker.mustBeInline;
    }

    //TODO: this class should go away in favor of Translator
    abstract static class STranslator {

        protected DiagnosticPosition diagPos;
        protected final JavafxToJava toJava;
        protected final JavafxDefs defs;
        protected final JavafxTypes types;
        protected final JavafxSymtab syms;
        protected final Name.Table names;;

        STranslator(DiagnosticPosition diagPos, JavafxToJava toJava) {
            this.diagPos = diagPos;
            this.toJava = toJava;
            this.defs = toJava.defs;
            this.types = toJava.types;
            this.syms = toJava.syms;
            this.names = toJava.names;
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


    protected abstract class Translator {

        protected DiagnosticPosition diagPos;

        protected Translator(DiagnosticPosition diagPos) {
            this.diagPos = diagPos;
        }

        protected abstract JCTree doit();

        protected TreeMaker m() {
            return make.at(diagPos);
        }

        protected JavafxTreeMaker fxm() {
            return fxmake.at(diagPos);
        }

        /**
         * Convert type to JCExpression
         */
        protected JCExpression makeExpression(Type type) {
            return makeTypeTree(diagPos, type, true);
        }
    }

    abstract class StringExpressionTranslator extends Translator {

        private final JFXStringExpression tree;
        StringExpressionTranslator(JFXStringExpression tree) {
            super(tree.pos());
            this.tree = tree;
        }

        protected JCExpression doit() {
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
                    && DATETIME_FORMAT_PATTERN.matcher(format).find()) {
                    containsDateTimeFormat = true;
                }
                parts = parts.tail;
                JFXExpression exp = parts.head;
                JCExpression texp;
                if (exp != null &&
                        types.isSameType(exp.type, syms.javafx_DurationType)) {
                    texp = m().Apply(null,
                            m().Select(translateArg(exp),
                            names.fromString("toMillis")),
                            List.<JCExpression>nil());
                    texp = typeCast(diagPos, syms.javafx_LongType, syms.javafx_DoubleType, texp);
                    sb.append(format.length() == 0 ? "%dms" : format);
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
                        toJava.getAttrEnv().enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
                values.prepend(m().Literal(TypeTags.CLASS, resourceName));
            } else if (containsDateTimeFormat) {
                formatMethod = "com.sun.javafx.runtime.util.FXFormatter.sprintf";
            } else {
                formatMethod = "java.lang.String.format";
            }
            JCExpression formatter = makeQualifiedTree(diagPos, formatMethod);
            return m().Apply(null, formatter, values.toList());
        }

        abstract protected JCExpression translateArg(JFXExpression arg);
    }


    abstract class FunctionCallTranslator extends Translator {

        protected final JFXExpression meth;
        protected final JFXExpression selector;
        private final Name selectorIdName;
        protected final boolean thisCall;
        protected final boolean superCall;
        protected final MethodSymbol msym;
        protected final boolean renameToThis;
        protected final boolean renameToSuper;
        protected final boolean superToStatic;
        protected final List<Type> formals;
        protected final boolean usesVarArgs;
        protected final boolean useInvoke;
        protected final boolean selectorMutable;
        protected final boolean callBound;
        protected final boolean magicIsInitializedFunction;
        protected final Type returnType;

        FunctionCallTranslator(final JFXFunctionInvocation tree) {
            super(tree.pos());
            meth = tree.meth;
            returnType = tree.type;
            JFXSelect fieldAccess = meth.getFXTag() == JavafxTag.SELECT ? (JFXSelect) meth : null;
            selector = fieldAccess != null ? fieldAccess.getExpression() : null;
            Symbol sym = toJava.expressionSymbol(meth);
            msym = (sym instanceof MethodSymbol) ? (MethodSymbol) sym : null;
            selectorIdName = (selector != null && selector.getFXTag() == JavafxTag.IDENT) ? ((JFXIdent) selector).getName() : null;
            thisCall = selectorIdName == toJava.names._this;
            superCall = selectorIdName == toJava.names._super;
            ClassSymbol csym = toJava.getAttrEnv().enclClass.sym;

            useInvoke = meth.type instanceof FunctionType;
            Symbol selectorSym = selector != null? toJava.expressionSymbol(selector) : null;
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
            formals = meth.type.getParameterTypes();
            //TODO: probably move this local to the arg processing
            usesVarArgs = tree.args != null && msym != null &&
                            (msym.flags() & Flags.VARARGS) != 0 &&
                            (formals.size() != tree.args.size() ||
                             types.isConvertible(tree.args.last().type,
                                 types.elemtype(formals.last())));

            selectorMutable = msym != null &&
                    !sym.isStatic() && selector != null &&
                    !namedSuperCall &&
                    !superCall && !renameToSuper &&
                    !thisCall && !renameToThis;
            callBound = msym != null && !useInvoke &&
                  ((msym.flags() & JavafxFlags.BOUND) != 0);

            magicIsInitializedFunction = (msym != null) &&
                    (msym.flags_field & JavafxFlags.FUNC_IS_INITIALIZED) != 0;
       }
    }

    abstract class ClosureTranslator extends Translator {

        // these only used when fields are built
        ListBuffer<JCTree> members = ListBuffer.lb();
        ListBuffer<JCStatement> fieldInits = ListBuffer.lb();
        int dependents = 0;
        ListBuffer<JCExpression> callArgs = ListBuffer.lb();
        int argNum = 0;

        ClosureTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        /**
         * Make a method paramter
         */
        protected JCVariableDecl makeParam(Type type, Name name) {
            return m().VarDef(
                    m().Modifiers(Flags.PARAMETER | Flags.FINAL),
                    name,
                    makeExpression(type),
                    null);
        }

        protected JCTree makeClosureMethod(Name methName, JCExpression expr, List<JCVariableDecl> params, Type returnType, long flags) {
            return toJava.makeMethod(diagPos, methName, List.<JCStatement>of((returnType == syms.voidType) ? m().Exec(expr) : m().Return(expr)), params, returnType, flags);
        }

        /**
         * Build the closure body
         * @return if the closure will be generated in-line, the list of class memebers of the closure, otherwise return null
         */
        protected abstract List<JCTree> makeBody();

        protected abstract JCExpression makeBaseClass();

        protected abstract List<JCExpression> makeConstructorArgs();

        protected JCExpression buildClosure() {
            List<JCTree> body = makeBody();
            JCClassDecl classDecl = body==null? null : m().AnonymousClassDef(m().Modifiers(0L), body);
            List<JCExpression> emptyTypeArgs = List.nil();
            return m().NewClass(null/*encl*/, emptyTypeArgs, makeBaseClass(), makeConstructorArgs(), classDecl);
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
            final ArgKind kind;

            FieldInfo(Type type, ArgKind kind) {
                this((String)null, type, kind);
            }

            FieldInfo(Name descName, Type type, ArgKind kind) {
                this(descName.toString(), type, kind);
            }

            FieldInfo(Name descName, TypeMorphInfo tmi, ArgKind kind) {
                this(descName.toString(), tmi, kind);
            }

            FieldInfo(String desc, Type type, ArgKind kind) {
                this(desc, typeMorpher.typeMorphInfo(type), kind);
            }

            FieldInfo(TypeMorphInfo tmi, ArgKind kind) {
                this((String)null, tmi, kind);
            }

            FieldInfo(String desc, TypeMorphInfo tmi, ArgKind kind) {
                this(desc, tmi, true, kind);
            }

            FieldInfo(String desc, TypeMorphInfo tmi, boolean isLocation, ArgKind kind) {
                this.desc = desc;
                this.num = argNum++;
                this.tmi = tmi;
                this.isLocation = isLocation;
                this.kind = kind;
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

        protected void sendField(JCExpression targ, FieldInfo fieldInfo) {
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
            return buildArgField(arg, new FieldInfo(type, kind));
        }

        protected JCExpression buildArgField(JCExpression arg, FieldInfo fieldInfo) {
            // translate the method arg into a Location field of the BindingExpression
            // XxxLocation arg$0 = ...;
            sendField(arg, fieldInfo);

            // build a list of these args, for use as dependents -- arg$0, arg$1, ...
            if (fieldInfo.kind == ArgKind.BOUND) {
                return makeAccess(fieldInfo);
            } else {
                if (fieldInfo.num > 32) {
                    log.error(diagPos, MsgSym.MESSAGE_BIND_TOO_COMPLEX);
                }
                if (fieldInfo.kind == ArgKind.DEPENDENT) {
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


    /**
     * Translator for for closures that can be expressed as instances of the per-script class.
     * When possible they become instanciations of the per-script class (with support
     * the add the needed support code to the per-script class).
     * When not possible, they are generated as in-line anonymous inner classes.
     */
    abstract class ScriptClosureTranslator extends ClosureTranslator {

        final int id;
        JCStatement resultStatement;
        final ListBuffer<JCExpression> argInits = ListBuffer.lb();
        boolean generateInLine = false;

        ScriptClosureTranslator(DiagnosticPosition diagPos, int id) {
            super(diagPos);
            this.id = id;
        }

        JCCase makeCase() {
            return m().Case(m().Literal(id), List.<JCStatement>of(
                    resultStatement,
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
            // Cast to their XxxLocation type
            return m().TypeCast(makeExpression(fieldInfo.type()), uncast);
        }

        /**
         * The class to instanciate that includes the closure
         */
        protected JCExpression makeBaseClass() {
            return m().Ident(defs.scriptBindingClassName);
        }

        @Override
        protected void sendField(JCExpression targ, FieldInfo fieldInfo) {
            // for use in the constructor args
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
            return args.toList();
        }
    }

    protected abstract class BinaryOperationTranslator extends Translator {

        final JFXBinary tree;
        final Type lhsType;
        final Type rhsType;

        BinaryOperationTranslator(DiagnosticPosition diagPos, final JFXBinary tree) {
            super(diagPos);
            this.tree = tree;
            this.lhsType = tree.lhs.type;
            this.rhsType = tree.rhs.type;
        }

        protected abstract JCExpression translateArg(JFXExpression arg, Type type);

        JCExpression lhs(Type type) {
            return translateArg(tree.lhs, type);
        }

        JCExpression lhs() {
            return lhs(null);
        }

        JCExpression rhs(Type type) {
            return translateArg(tree.rhs, type);
        }

        JCExpression rhs() {
            return rhs(null);
        }

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
            return makeEqEq(arg, defaultValue);
        }

        /**
         * Check if a non-primitive has the default value for its type.
         */
        private JCExpression makeObjectNullCheck(Type argType, JCExpression arg) {
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
            if (tmi.isSequence() || tmi.getRealType() == syms.javafx_StringType) {
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
            return m().Binary(tag, arg1, arg2);
        }

        private JCExpression makeNull() {
            return m().Literal(TypeTags.BOT, null);
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
                    return makeEqEq(lhs(req), rhs(req));
                } else {
                    // lhs is primitive, rhs is non-primitive, use equals(), but switch them
                    JCVariableDecl sl = makeTmpVar(diagPos, req!=null? req : lhsType, lhs(req));  // eval first to keep the order correct
                    return makeBlockExpression(diagPos, List.<JCStatement>of(sl), makeFullCheck(rhs(req), m().Ident(sl.name)));
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
            return callExpression(diagPos, leftSide, methodName, rightSide);
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
        public JCExpression doit() {
            //TODO: handle <>
            if (tree.getFXTag() == JavafxTag.EQ) {
                return translateEqualsEquals();
            } else if (tree.getFXTag() == JavafxTag.NE) {
                return m().Unary(JCTree.NOT, translateEqualsEquals());
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

    JCExpression convertVariableReference(DiagnosticPosition diagPos,
                                                 JCExpression varRef, Symbol sym) {
        JCExpression expr = varRef;

        if (sym instanceof VarSymbol) {
            final VarSymbol vsym = (VarSymbol) sym;
            VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
            boolean isSequence = vmi.isSequence();
            boolean isClassVar = vmi.isFXMemberVariable();

            if (isClassVar) {
                // this is a reference to a JavaFX class variable, use getter
                Name accessName = attributeGetterName(vsym);
                JCExpression accessFunc = switchName(diagPos, varRef, accessName);
                List<JCExpression> emptyArgs = List.nil();
                expr = make.at(diagPos).Apply(null, accessFunc, emptyArgs);
            }
/***
            if (!vmi.useAccessors()) {
                int typeKind = vmi.getTypeKind();
                if (vmi.representation() == AlwaysLocation && wrapper != AsLocation) {
                    // Anything still in the form of a Location (and that isn't what we want), get the value
                    if (isSequence || !isClassVar) {
                        expr = getLocationValue(diagPos, expr, typeKind);
                    }
                } else if (vmi.representation() == NeverLocation && wrapper == AsLocation) {
                    // We are directly accessing a non-Location local, a Location is wanted, wrap it
                    assert !isClassVar;
                    expr = makeUnboundLocation(diagPos, vmi, expr);
                }
            }
 ****/
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
            JCExpression meth = make.Select(makeTypeTree(diagPos, valueOfSym.owner.type), valueOfSym.name);
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

   JCExpression castFromObject (JCExpression arg, Type castType) {
       return make.TypeCast(makeTypeTree(arg.pos(), types.boxedTypeOrType(castType)), arg);
    }

   JCExpression makeFunctionValue (JCExpression meth, JFXFunctionDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        if (def != null) {
            // Translate the definition, maintaining the current inInstanceContext
            members.append( toJava.translateFunction(def, true) );
        }
        JCExpression encl = null;
        int nargs = mtype.argtypes.size();
        Type ftype = syms.javafx_FunctionTypes[nargs];
        JCExpression t = makeQualifiedTree(null, ftype.tsym.getQualifiedName().toString());
        ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
        Type rtype = types.boxedTypeOrType(mtype.restype);
        typeargs.append(makeTypeTree(diagPos, rtype));
        ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
        int i = 0;
        for (List<Type> l = mtype.argtypes;  l.nonEmpty();  l = l.tail) {
            Name pname = make.paramName(i++);
            Type ptype = types.boxedTypeOrType(l.head);
            JCVariableDecl param = make.VarDef(make.Modifiers(0), pname,
                    makeTypeTree(diagPos, ptype), null);
            params.append(param);
            JCExpression marg = make.Ident(pname);
            margs.append(marg);
            typeargs.append(makeTypeTree(diagPos, ptype));
        }

        // The backend's Attr skips SYNTHETIC methods when looking for a matching method.
        long flags = Flags.PUBLIC | Flags.BRIDGE; // | SYNTHETIC;

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

    JCExpression makeReceiver(DiagnosticPosition diagPos, Symbol sym) {
        return makeReceiver(diagPos, sym, false);
    }

    /**
     * Build the AST for accessing the outer member.
     * The accessors might be chained if the member accessed is more than one level up in the outer chain.
     * */
    JCExpression makeReceiver(DiagnosticPosition diagPos, Symbol sym, boolean nullForThis) {
        // !sym.isStatic()
        Symbol siteOwner = getAttrEnv().enclClass.sym;
        // This following cannot be used until anonymous classes like BoundComprehensions are handled
        // JCExpression ret = make.Ident(inInstanceContext == ReceiverContext.InstanceAsStatic ? defs.receiverName : names._this);
        JCExpression thisExpr = make.at(diagPos).Select(makeTypeTree(diagPos, siteOwner.type), names._this);
        JCExpression ret = inInstanceContext == ReceiverContext.InstanceAsStatic ?
            make.at(diagPos).Ident(defs.receiverName) :
            thisExpr;
        ret.type = siteOwner.type;

        // check if it is in the chain
        if (sym != null && siteOwner != null && siteOwner != sym.owner) {
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
                    if (types.isSameType(supType, sym.owner.type)) {
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
                        ret = callExpression(diagPos, ret, defs.outerAccessorName);
                        ret.type = siteCursor.type;
                    }

                    if (siteCursor.kind == Kinds.TYP) {
                        numOfOuters--;
                    }
                    siteCursor = siteCursor.owner;
                }
            }
        }
        return (nullForThis && ret == thisExpr)? null : ret;
    }

    JCExpression translateIdent(JFXIdent tree) {
        DiagnosticPosition diagPos = tree.pos();

        if (tree.name == names._this) {
            // in the static implementation method, "this" becomes "receiver$"
            return makeReceiver(diagPos, tree.sym);
        } else if (tree.name == names._super) {
            if (types.isMixin(tree.type.tsym)) {
                // "super" becomes just the class where the static implementation method is defined
                //  the rest of the implementation is in visitFunctionInvocation
                return make.at(diagPos).Ident(tree.type.tsym.name);
            } else {
                // Just use super.
                return make.at(diagPos).Ident(tree.name);
            }
        }

        int kind = tree.sym.kind;
        if (kind == Kinds.TYP) {
            // This is a class name, replace it with the full name (no generics)
            return makeTypeTree(diagPos, types.erasure(tree.sym.type), false);
        }

       // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
       // and possible outer access methods
        JCExpression convert;
        boolean isStatic = tree.sym.isStatic();
        if (isStatic) {
            // make class-based direct static reference:   Foo.x
            convert = make.at(diagPos).Select(makeTypeTree(diagPos, tree.sym.owner.type, false), tree.name);
        } else {
            if ((kind == Kinds.VAR || kind == Kinds.MTH) &&
                    tree.sym.owner.kind == Kinds.TYP) {
                // it is a non-static attribute or function class member
                // reference it through the receiver
                JCExpression mRec = makeReceiver(diagPos, tree.sym, true);
                convert = (mRec==null)? make.at(diagPos).Ident(tree.name) : make.at(diagPos).Select(mRec, tree.name);
            } else {
                convert = make.at(diagPos).Ident(tree.name);
            }
        }

        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JFXFunctionDefinition def = null; // FIXME
            return makeFunctionValue(convert, def, tree.pos(), mtype);
        }

        return convertVariableReference(diagPos,
                convert,
                tree.sym);
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

}

