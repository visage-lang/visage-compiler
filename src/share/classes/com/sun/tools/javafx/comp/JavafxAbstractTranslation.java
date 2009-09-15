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

    private JavafxToJava toJava; //TODO: this should go away

    protected JavafxAbstractTranslation(Context context, JavafxToJava toJava) {
        super(context);
        this.optStat = JavafxOptimizationStatistics.instance(context);
        this.toJava = toJava;  //TODO: temp hack
        this.target = Target.instance(context);
    }

    /** Translate a single tree.
     */
    R translate(JFXTree tree) {
        R ret;

        if (tree == null) {
            ret = null;
        } else {
            JFXTree prevWhere = getAttrEnv().where;
            getAttrEnv().where = tree;
            tree.accept(this);
            getAttrEnv().where = prevWhere;
            ret = this.result;
            this.result = null;
        }
        return ret;
    }

    /** Translate a single expression.
     */
    R translate(JFXExpression expr, Type type) {
        return translate(expr);
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

    /**
     * @return the attrEnv
     */
    protected JavafxEnv<JavafxAttrContext> getAttrEnv() {
        return toJava.getAttrEnv();
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

    abstract class Translator extends JavaTreeBuilder {

        Translator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        abstract JCTree doit();

        JavafxTreeMaker fxm() {
            return fxmake.at(diagPos);
        }
    }

    abstract class MemberReferenceTranslator extends Translator {

        protected MemberReferenceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        JCExpression staticReference(Symbol sym) {
            Symbol owner = sym.owner;
            Symbol encl = getAttrEnv().enclClass.sym;
            if (encl.name.endsWith(defs.scriptClassSuffixName) && owner == encl.owner) {
                return null;
            } else {
                JCExpression expr = makeType(types.erasure(owner.type), false);
                if (types.isJFXClass(owner)) {
                    // script-level get to instance through script-level accessor
                    expr = callExpression(expr, defs.scriptLevelAccessMethod);
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
                            instance = null;
                            break;
                        case JCTree.SELECT:
                            instance = ((JCFieldAccess) varRef).getExpression();
                            break;
                        default:
                            throw new AssertionError();
                    }
                    expr = callExpression(instance, attributeGetterName(vsym));
                }
            }

            return expr;
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
                            select(translateArg(exp),
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
                    values.prepend(makeNull());
                } else {
                    values.prepend(m().Literal(TypeTags.CLASS, tree.translationKey));
                }
                String resourceName =
                        getAttrEnv().enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
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
            Symbol sym = expressionSymbol(meth);
            msym = (sym instanceof MethodSymbol) ? (MethodSymbol) sym : null;
            Name selectorIdName = (selector != null && selector.getFXTag() == JavafxTag.IDENT) ? ((JFXIdent) selector).getName() : null;
            thisCall = selectorIdName == names._this;
            superCall = selectorIdName == names._super;
            ClassSymbol csym = getAttrEnv().enclClass.sym;

            useInvoke = meth.type instanceof FunctionType;
            Symbol selectorSym = selector != null? expressionSymbol(selector) : null;
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

        protected JCTree makeClosureMethod(Name methName, JCExpression expr, List<JCVariableDecl> params, Type returnType, long flags) {
            return makeMethod(diagPos, methName, List.<JCStatement>of((returnType == syms.voidType) ? m().Exec(expr) : m().Return(expr)), params, returnType, flags);
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

        class FieldInfo {
            final String desc;
            final int num;
            final TypeMorphInfo tmi;
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
                this.desc = desc;
                this.num = argNum++;
                this.tmi = tmi;
                this.kind = kind;
            }

            JCExpression makeGetField() {
                return makeGetField(tmi.getTypeKind());
            }

            JCExpression makeGetField(int typeKind) {
                return makeAccess(this);
            }

            Type type() {
                return tmi.getRealBoxedType();
            }
        }

        private Name argAccessName(FieldInfo fieldInfo) {
            return names.fromString("arg$" + fieldInfo.num);
        }

        JCExpression makeAccess(FieldInfo fieldInfo) {
            return id(argAccessName(fieldInfo));
        }

        protected void sendField(JCExpression targ, FieldInfo fieldInfo) {
            fieldInits.append( m().Exec( m().Assign(makeAccess(fieldInfo), targ)) );
            members.append(m().VarDef(
                    m().Modifiers(Flags.PRIVATE),
                    argAccessName(fieldInfo),
                    makeType(fieldInfo.type()),
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
                uncast = m().Indexed(id(defs.moreArgsName), m().Literal(fieldInfo.num - 2));
            }
            // Cast to their XxxLocation type
            return m().TypeCast(makeType(fieldInfo.type()), uncast);
        }

        /**
         * The class to instanciate that includes the closure
         */
        protected JCExpression makeBaseClass() {
            return id(defs.scriptBindingClassName);
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
                args.append(makeNull());
            }
            // arg: arg$1
            if (argNum > 1) {
                args.append(inits.head);
                inits = inits.tail;
            } else {
                args.append(makeNull());
            }
            // arg: moreArgs
            if (argNum > 2) {
                args.append(m().NewArray(makeType(syms.objectType), List.<JCExpression>nil(), inits));
            } else {
                args.append(makeNull());
            }
            return args.toList();
        }
    }

    //TODO: this needs to be refactored so it makes sense as a MemberReferenceTranslator
    abstract class NullCheckTranslator extends MemberReferenceTranslator {

        protected final JFXExpression toCheck;
        protected final Type resultType;
        private final boolean needNullCheck;
        private boolean hasSideEffects;
        private ListBuffer<JCStatement> tmpVarList;

        NullCheckTranslator(DiagnosticPosition diagPos, JFXExpression toCheck, Type resultType, boolean knownNonNull) {
            super(diagPos);
            this.toCheck = toCheck;
            this.resultType = resultType;
            this.needNullCheck = !knownNonNull && !toCheck.type.isPrimitive() && possiblyNull(toCheck);
            this.hasSideEffects = needNullCheck && hasSideEffects(toCheck);
            this.tmpVarList = ListBuffer.<JCStatement>lb();
        }

        abstract JCExpression fullExpression(JCExpression mungedToCheckTranslated);

        abstract JCExpression translateToCheck(JFXExpression expr);

        protected JCExpression preserveSideEffects(Type type, JFXExpression expr, JCExpression trans) {
            if (needNullCheck && expr!=null && hasSideEffects(expr)) {
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
            tmpVarList.append(tmpVar);
            return id(tmpVar.name);
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

        protected JCTree doit() {
            JCExpression mungedToCheckTranslated = translateToCheck(toCheck);
            JCVariableDecl tmpVar = null;
            if (hasSideEffects) {
                // if the toCheck sub-expression has side-effects, compute it and stash in a
                // temp var so we don't invoke it twice.
                tmpVar = makeTmpVar("toCheck", toCheck.type, mungedToCheckTranslated);
                tmpVarList.append(tmpVar);
                mungedToCheckTranslated = id(tmpVar.name);
            }
            JCExpression full = fullExpression(mungedToCheckTranslated);
            if (!needNullCheck && tmpVarList.isEmpty()) {
                return full;
            }
            // Do a null check
            JCExpression toTest = hasSideEffects ? id(tmpVar.name) : translateToCheck(toCheck);
            // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
            JCExpression cond = makeNotNullCheck(toTest);
            if (resultType == syms.voidType) {
                // if this is a void expression, check it with an If-statement
                JCStatement stmt = m().Exec(full);
                if (needNullCheck) {
                    stmt = m().If(cond, stmt, null);
                }
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
                    full = m().Conditional(cond, full, defaultExpr);
                }
                if (tmpVarList.nonEmpty()) {
                    // if the selector has side-effects, we created a temp var to hold it
                    // so we need to make a block-expression to include the temp var
                    full = makeBlockExpression(tmpVarList.toList(), full);
                }
                return full;
            }
        }
    }

    abstract class AssignTranslator extends Translator {

        protected final JFXExpression lhs;
        protected final JFXExpression rhs;
        protected final Symbol sym;
        protected final VarMorphInfo vmi;
        protected final JCExpression rhsTranslated;

        AssignTranslator(final DiagnosticPosition diagPos, final JFXExpression lhs, final JFXExpression rhs) {
            super(diagPos);
            this.lhs = lhs;
            this.rhs = rhs;
            this.sym = expressionSymbol(lhs);
            this.vmi = sym==null? null : typeMorpher.varMorphInfo(sym);
            this.rhsTranslated = convertNullability(diagPos, translateExpression(rhs, rhsType()), rhs, rhsType());
        }

        abstract JCExpression translateExpression(JFXExpression expr, Type type);

        JCExpression translateExpression(JFXExpression expr) {
            return translateExpression(expr, expr.type);
        }

        abstract JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated);

        abstract JCExpression buildRHS(JCExpression rhsTranslated);

        /**
         * Override to change the translation type of the right-hand side
         */
        protected Type rhsType() {
            return sym==null? lhs.type : sym.type; // Handle type inferencing not reseting the ident type
        }

        /**
         * Override to change result in the non-default case.
          */
        protected JCExpression postProcess(JCExpression built) {
            return built;
        }

        private JCExpression buildSetter(JCExpression tc, JCExpression rhsComplete) {
            final Name setter = attributeSetterName(sym);
            JCExpression toApply = (tc==null)? id(setter) : select(tc, setter);
            return m().Apply(null, toApply, List.of(rhsComplete));
        }

        protected JCTree doit() {
            if (lhs.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
                // set of a sequence element --  s[i]=8, call the sequence set method
                JFXSequenceIndexed si = (JFXSequenceIndexed) lhs;
                JFXExpression seq = si.getSequence();
                JCExpression index = translateExpression(si.getIndex(), syms.intType);
                if (seq.type.tag == TypeTags.ARRAY) {
                    JCExpression tseq = translateExpression(seq);
                    return postProcess(m().Assign(m().Indexed(tseq, index), buildRHS(rhsTranslated)));
                }
                else {
                    JCExpression tseq = translateExpression(seq); //TODO
                    List<JCExpression> args = List.of(index, buildRHS(rhsTranslated));
                    JCExpression select = select(tseq, defs.setMethodName);
                    return postProcess(m().Apply(null, select, args));
                }
            } else {
                final boolean useSetters = vmi.useAccessors();
                //TODO: ??? If sequence we need to call incrementShared.  Thus:
                // assert ! types.isSequence(lhs.type);
                if (lhs.getFXTag() == JavafxTag.SELECT) {
                    final JFXSelect select = (JFXSelect) lhs;
                    return new NullCheckTranslator(diagPos, select.getExpression(), lhs.type, false) { 

                        private final JCExpression rhsTranslatedPreserved = preserveSideEffects(lhs.type, rhs, rhsTranslated);

                        @Override
                        JCExpression translateToCheck( JFXExpression expr) {
                            return translateExpression(expr);
                        }

                        @Override
                        JCExpression fullExpression( JCExpression mungedToCheckTranslated) {
                            Symbol selectorSym = expressionSymbol(select.getExpression());
                            // If LHS is OuterClass.memberName or MixinClass.memberName, then
                            // we want to create expression to get the proper receiver.
                            if (!sym.isStatic() && selectorSym != null && selectorSym.kind == Kinds.TYP) {
                                mungedToCheckTranslated = makeReceiver(diagPos, sym);
                            }
                            if (useSetters) {
                                return postProcess(buildSetter(mungedToCheckTranslated, buildRHS(rhsTranslatedPreserved)));
                            } else {
                                //TODO: possibly should use, or be unified with convertVariableReference
                                JCExpression fa = select(mungedToCheckTranslated, attributeValueName(select.sym));
                                return defaultFullExpression(fa, rhsTranslatedPreserved);
                            }
                        }
                    }.doit();
                } else {
                    // not SELECT
                    if (useSetters) {
                        JCExpression recv = sym.isStatic()?
                            makeType(sym.owner.type, false) :
                            makeReceiver(diagPos, sym, true);
                        return postProcess(buildSetter(recv, buildRHS(rhsTranslated)));
                    } else {
                        return defaultFullExpression(translateExpression(lhs), rhsTranslated);
                    }
                }
            }
        }
    }

    abstract class UnaryOperationTranslator extends Translator {

        private final JFXUnary tree;
        private final JFXExpression expr;
        private final JCExpression transExpr;

        UnaryOperationTranslator(JFXUnary tree) {
            super(tree.pos());
            this.tree = tree;
            this.expr = tree.getExpression();
            this.transExpr =
                    tree.getFXTag() == JavafxTag.SIZEOF &&
                    (expr instanceof JFXIdent || expr instanceof JFXSelect) ? translateForSizeof(expr)
                    : translateExpression(expr, expr.type);
        }

        abstract JCExpression translateExpression(JFXExpression expr, Type type);

        private JCExpression translateForSizeof(JFXExpression expr) {
            return translateExpression(expr, expr.type);
        }

        JCExpression translateSizeof(JFXExpression expr, JCExpression transExpr) {
            return runtime(diagPos, defs.Sequences_size, List.of(transExpr));
        }

        private JCExpression doIncDec(final int binaryOp, final boolean postfix) {
            return (JCExpression) new AssignTranslator(diagPos, expr, fxm().Literal(1)) {

                JCExpression translateExpression(JFXExpression expr, Type type) {
                    return UnaryOperationTranslator.this.translateExpression(expr, type);
                }

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
                protected JCExpression postProcess(JCExpression built) {
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

        JCExpression doit() {
            switch (tree.getFXTag()) {
                case SIZEOF:
                    if (expr.type.tag == TypeTags.ARRAY) {
                        return select(transExpr, defs.lengthName);
                    }
                    return translateSizeof(expr, transExpr);
                case REVERSE:
                    if (types.isSequence(expr.type)) {
                        // call runtime reverse of a sequence
                        return callExpression(
                                makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                                "reverse", transExpr);
                    } else {
                        // this isn't a sequence, just make it a sequence
                        return convertTranslated(transExpr, diagPos, expr.type, tree.type);
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
                        return m().Apply(null, select(translateExpression(tree.arg, tree.arg.type), names.fromString("negate")), List.<JCExpression>nil());
                    }
                default:
                    return m().Unary(tree.getOperatorTag(), transExpr);
            }
        }
    }

    abstract class BinaryOperationTranslator extends Translator {

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
                return callRuntime(JavafxDefs.isNullMethodString, List.of(arg));
            } else {
                return makeNullCheck(arg);
            }
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
            return callExpression(leftSide, methodName, rightSide);
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
        JCExpression doit() {
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

    class TypeConversionTranslator extends Translator {

        final JCExpression translated;
        final Type sourceType;
        final Type targetType;
        final boolean sourceIsSequence;
        final boolean targetIsSequence;
        final boolean sourceIsArray;
        final boolean targetIsArray;

        TypeConversionTranslator(DiagnosticPosition diagPos, JCExpression translated, Type sourceType, Type targetType) {
            super(diagPos);
            this.translated = translated;
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.sourceIsSequence = types.isSequence(sourceType);
            this.targetIsSequence = types.isSequence(targetType);
            this.sourceIsArray = types.isArray(sourceType);
            this.targetIsArray = types.isArray(targetType);
        }

        private JCExpression convertNumericSequence(final DiagnosticPosition diagPos,
                final JCExpression expr, final Type inElementType, final Type targetElementType) {
            JCExpression inTypeInfo = makeTypeInfo(diagPos, inElementType);
            JCExpression targetTypeInfo = makeTypeInfo(diagPos, targetElementType);
            return runtime(diagPos,
                    defs.Sequences_convertNumberSequence,
                    List.of(targetTypeInfo, inTypeInfo, expr));
        }

        JCExpression doit() {
            if (targetIsArray) {
                Type elemType = types.elemtype(targetType);
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
                        return callExpression(makeType(syms.javafx_SequencesType, false),
                                mname, translated);
                    }
                    ListBuffer<JCStatement> stats = ListBuffer.lb();
                    JCVariableDecl tmpVar = makeTmpVar(sourceType, translated);
                    stats.append(tmpVar);
                    JCVariableDecl sizeVar = makeTmpVar(syms.intType, callExpression(id(tmpVar.name), "size"));
                    stats.append(sizeVar);
                    JCVariableDecl arrVar = makeTmpVar("arr", targetType, make.at(diagPos).NewArray(
                            makeType(elemType, true),
                            List.<JCExpression>of(id(sizeVar.name)),
                            null));
                    stats.append(arrVar);
                    stats.append(callStatement(id(tmpVar.name), "toArray", List.of(
                            makeInt(0),
                            id(sizeVar),
                            id(arrVar),
                            makeInt(0))));
                    return makeBlockExpression(stats, id(arrVar));
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
                JCExpression cSequences = makeType(syms.javafx_SequencesType, false);
                return callExpression(cSequences, "fromArray", args);
            }
            if (targetIsSequence && !sourceIsSequence) {
                //if (sourceType.tag == TypeTags.BOT) {
                //    // it is a null, convert to empty sequence
                //    //TODO: should we leave this null?
                //    Type elemType = types.elemtype(type);
                //    return makeEmptySequenceCreator(diagPos, elemType);
                //}
                Type targetElemType = types.elementType(targetType);
                JCExpression cSequences = makeType(syms.javafx_SequencesType, false);
                JCExpression res = convertTranslated(translated, diagPos, sourceType, targetElemType);
                // This would be redundant, if convertTranslated did a cast if needed.
                res = makeTypeCast(diagPos, targetElemType, sourceType, res);
                return callExpression(
                        cSequences,
                        "singleton",
                        List.of(makeTypeInfo(diagPos, targetElemType), res));
            }
            if (targetIsSequence && sourceIsSequence) {
                Type sourceElementType = types.elementType(sourceType);
                Type targetElementType = types.elementType(targetType);
                if (!types.isSameType(sourceElementType, targetElementType) &&
                        types.isNumeric(sourceElementType) && types.isNumeric(targetElementType)) {
                    return convertNumericSequence(diagPos,
                            translated,
                            sourceElementType,
                            targetElementType);
                }
            }

            // Convert primitive/Object types
            Type unboxedTargetType = targetType.isPrimitive() ? targetType : types.unboxedType(targetType);
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
                    res = make.at(diagPos).TypeCast(unboxedTargetType, res);
                    curType = unboxedTargetType;
                }
                if (!targetType.isPrimitive()) {
                    // box target if target boxed
                    res = make.at(diagPos).TypeCast(makeType(targetType, false), res);
                    curType = targetType;
                }
            } else {
                if (curType.isCompound() || curType.isPrimitive()) {
                    res = make.at(diagPos).TypeCast(makeType(types.erasure(targetType), true), res);
                }
            }
            // We should add a cast "when needed".  Then visitTypeCast would just
            // call this function, and not need to call makeTypeCast on the result.
            // However, getting that to work is a pain - giving up for now.  FIXME
            return res;
        }
    }

    JCExpression convertTranslated(JCExpression translated, DiagnosticPosition diagPos,
            Type sourceType, Type targetType) {
        assert sourceType != null;
        assert targetType != null;
        if (targetType.tag == TypeTags.UNKNOWN) {
            //TODO: this is bad attribution
            return translated;
        }
        if (types.isSameType(targetType, sourceType)) {
            return translated;
        }
        return (new TypeConversionTranslator(diagPos, translated, sourceType, targetType)).doit();
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

            JCExpression doit() {
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
        }).doit();
    }

   JCExpression castFromObject (JCExpression arg, Type castType) {
       return make.TypeCast(makeType(arg.pos(), types.boxedTypeOrType(castType)), arg);
    }

   //TODO: hack -- move FunctionTranslator up
   JCTree translateFunction(JFXFunctionDefinition tree, boolean maintainContext) {
       return toJava.translateFunction(tree, maintainContext);
   }

   JCExpression makeFunctionValue (JCExpression meth, JFXFunctionDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        if (def != null) {
            // Translate the definition, maintaining the current inInstanceContext
            members.append( translateFunction(def, true) );
        }
        JCExpression encl = null;
        int nargs = mtype.argtypes.size();
        Type ftype = syms.javafx_FunctionTypes[nargs];
        JCExpression t = makeQualifiedTree(null, ftype.tsym.getQualifiedName().toString());
        ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
        Type rtype = types.boxedTypeOrType(mtype.restype);
        typeargs.append(makeType(diagPos, rtype));
        ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
        int i = 0;
        for (List<Type> l = mtype.argtypes;  l.nonEmpty();  l = l.tail) {
            Name pname = make.paramName(i++);
            Type ptype = types.boxedTypeOrType(l.head);
            JCVariableDecl param = make.VarDef(make.Modifiers(0), pname,
                    makeType(diagPos, ptype), null);
            params.append(param);
            JCExpression marg = make.Ident(pname);
            margs.append(marg);
            typeargs.append(makeType(diagPos, ptype));
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
                makeType(diagPos, rtype),
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
        JCExpression thisExpr = make.at(diagPos).Select(makeType(diagPos, siteOwner.type), names._this);
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

    class IdentTranslator extends MemberReferenceTranslator {
        JFXIdent tree;
        IdentTranslator(JFXIdent tree) {
            super(tree.pos());
            this.tree = tree;
        }

        protected JCExpression doit() {
            if (tree.name == names._this) {
                // in the static implementation method, "this" becomes "receiver$"
                return makeReceiver(diagPos, tree.sym);
            } else if (tree.name == names._super) {
                if (types.isMixin(tree.type.tsym)) {
                    // "super" becomes just the class where the static implementation method is defined
                    //  the rest of the implementation is in visitFunctionInvocation
                    return id(tree.type.tsym.name);
                } else {
                    // Just use super.
                    return id(tree.name);
                }
            }

            int kind = tree.sym.kind;
            if (kind == Kinds.TYP) {
                // This is a class name, replace it with the full name (no generics)
                return makeType(types.erasure(tree.sym.type), false);
            }

            // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
            // and possible outer access methods
            JCExpression convert;
            boolean isStatic = tree.sym.isStatic();
            if (isStatic) {
                // make class-based direct static reference:   Foo.x
                convert = select(staticReference(tree.sym), tree.name);
            } else {
                if ((kind == Kinds.VAR || kind == Kinds.MTH) &&
                        tree.sym.owner.kind == Kinds.TYP) {
                    // it is a non-static attribute or function class member
                    // reference it through the receiver
                    JCExpression mRec = makeReceiver(diagPos, tree.sym, true);
                    convert = select(mRec, tree.name);
                } else {
                    convert = id(tree.name);
                }
            }

            if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
                MethodType mtype = (MethodType) tree.sym.type;
                JFXFunctionDefinition def = null; // FIXME
                return makeFunctionValue(convert, def, tree.pos(), mtype);
            }

            return convertVariableReference(convert, tree.sym);
        }
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

