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


import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.tree.JFXExpression;
import com.sun.tools.javafx.tree.JFXFunctionInvocation;
import com.sun.tools.javafx.tree.JFXIdent;
import com.sun.tools.javafx.tree.JFXLiteral;
import com.sun.tools.javafx.tree.JFXSelect;
import com.sun.tools.javafx.tree.JFXStringExpression;
import com.sun.tools.javafx.tree.JavafxTag;
import com.sun.tools.javafx.tree.JavafxTreeMaker;
import com.sun.tools.javafx.util.MsgSym;
import java.util.regex.Pattern;


/**
 *
 * @author Robert Field
 */
public abstract class JavafxAbstractTranslation extends JavafxTranslationSupport {

    JavafxToJava toJava; //TODO: this should go away

    protected JavafxAbstractTranslation(Context context, JavafxToJava toJava) {
        super(context);
        this.toJava = toJava==null? (JavafxToJava)this : toJava;  //TODO: temp hack
    }

    private static final Pattern EXTENDED_FORMAT_PATTERN = Pattern.compile("%[<$0-9]*[tT][guwxGUVWXE]");

    enum ArgKind { BOUND, DEPENDENT, FREE };

    public enum Locationness {
        // We need a Location
        AsLocation,
        // We need a sequence element or size.
        // Normally, when we access (read) a sequence variable, we need to call
        // Sequences.noteShared, which sets the shared bit on the sequence.
        // But for seq[index] or sizeof seq we don't need to do that.
        // The AsShareSafeValue enum is used to supress the noteShared call.
        AsShareSafeValue,
        // We need a value
        AsValue
    }

    /**
     * @return the attrEnv
     */
    protected JavafxEnv<JavafxAttrContext> getAttrEnv() {
        return toJava.attrEnv;
    }

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
                            names.fromString("toDate")),
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
                        toJava.getAttrEnv().enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
                values.prepend(m().Literal(TypeTags.CLASS, resourceName));
            } else if (containsExtendedFormat) {
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

        protected final TypeMorphInfo tmiResult;
        protected final int typeKindResult;
        protected final Type elementTypeResult;

        // these only used when fields are built
        ListBuffer<JCTree> members = ListBuffer.lb();
        ListBuffer<JCStatement> fieldInits = ListBuffer.lb();
        int dependents = 0;
        ListBuffer<JCExpression> callArgs = ListBuffer.lb();
        int argNum = 0;

        ClosureTranslator(DiagnosticPosition diagPos, TypeMorphInfo tmiResult) {
            super(diagPos);
            this.tmiResult = tmiResult;
            typeKindResult = tmiResult.getTypeKind();
            elementTypeResult = types.boxedElementType(tmiResult.getLocationType()); // want boxed, JavafxTypes version won't work
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

        protected abstract List<JCTree> makeBody();

        protected abstract JCExpression makeBaseClass();

        protected JCExpression makeBaseClass(Type clazzType, Type additionTypeParamOrNull) {
            JCExpression clazz = makeExpression(types.erasure(clazzType));  // type params added below, so erase formals
            ListBuffer<JCExpression> typeParams = ListBuffer.lb();

            if (typeKindResult == JavafxVarSymbol.TYPE_KIND_OBJECT || typeKindResult == JavafxVarSymbol.TYPE_KIND_SEQUENCE) {
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
}
