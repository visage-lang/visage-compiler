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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Type.MethodType;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.Translator;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.comp.JavafxAbstractTranslation.Yield.*;

/**
 * Translate JavaFX ASTs into Java ASTs
 *
 * @author Robert Field
 * @author Per Bothner
 * @author Lubo Litchev
 */
public class JavafxToJava extends JavafxAbstractTranslation {
    protected static final Context.Key<JavafxToJava> jfxToJavaKey =
        new Context.Key<JavafxToJava>();

    /*
     * modules imported by context
     */
    private final JavafxInitializationBuilder initBuilder;
    private final JavafxTranslateBind translateBind;
    private final JavafxTranslateInvBind translateInvBind;
    private final JavafxTranslateDependent translateDependent;

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

    // Stack used to track literal symbols for the current class.
    private Map<Symbol, Name> substitutionMap = new HashMap<Symbol, Name>();

    // Stack used to track literal symbols for the current class.
    private LiteralInitClassMap literalInitClassMap = null;

    /** Class symbols for classes that need a reference to the outer class. */
    private final Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();

    private JavafxEnv<JavafxAttrContext> attrEnv;
    ReceiverContext inInstanceContext = ReceiverContext.Oops;

    /*
     * Sole instance creation
     */
 
    public static JavafxToJava instance(Context context) {
        JavafxToJava instance = context.get(jfxToJavaKey);
        if (instance == null)
            instance = new JavafxToJava(context);
        return instance;
    }

    protected JavafxToJava(Context context) {
        super(context, null);

        context.put(jfxToJavaKey, this);

        this.initBuilder = JavafxInitializationBuilder.instance(context);
        this.translateBind = JavafxTranslateBind.instance(context);
        this.translateInvBind = JavafxTranslateInvBind.instance(context);
        this.translateDependent = JavafxTranslateDependent.instance(context);
    }

    /**
     * Entry point
     */
    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.setAttrEnv(attrEnv);

        attrEnv.translatedToplevel = (JCCompilationUnit)((SpecialResult)translateToSpecialResult(attrEnv.toplevel)).tree();
        attrEnv.translatedToplevel.endPositions = attrEnv.toplevel.endPositions;
    }

    /**
     * For special cases where the expression may not be fully attributed.
     * Specifically: package and import names.
     * Do a dumb simple conversion.
     */
    private JCExpression straightConvert(JFXExpression tree) {
        if (tree == null) {
            return null;
        }
        DiagnosticPosition diagPos = tree.pos();
        switch (tree.getFXTag()) {
            case IDENT: {
                JFXIdent id = (JFXIdent) tree;
                return make.at(diagPos).Ident(id.getName());
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

    private boolean substitute(DiagnosticPosition diagPos, final Symbol sym) {
        final Name name = getSubstitutionMap().get(sym);
        if (name == null) {
            return false;
        } else {
            result = new ExpressionTranslator(diagPos) {
                protected ExpressionResult doit() {
                    return toResult(id(name), sym.type);
                }
            }.doit();
            return true;
        }
    }

    private void setSubstitution(JFXTree target, Symbol sym) {
        if (target instanceof JFXInstanciate) {
            // Set up to substitute a reference to the this var within its definition
            ((JFXInstanciate) target).varDefinedByThis = sym;
        }
    }

    /**
     * @return the attrEnv
     */
    @Override
    protected JavafxEnv<JavafxAttrContext> getAttrEnv() {
        return attrEnv;
    }

    @Override
    protected ReceiverContext receiverContext() {
        return inInstanceContext;
    }

    @Override
    protected void setReceiverContext(ReceiverContext rc) {
        inInstanceContext = rc;
    }

    @Override
    protected JavafxToJava toJava() {
        return this;
    }

    @Override
    protected JavafxTranslateDependent translateDependent() {
        return translateDependent;
    }

    /**
     * @param attrEnv the attrEnv to set
     */
    public void setAttrEnv(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;
    }

    /**
     * @return the substitutionMap
     */
    @Override
    Map<Symbol, Name> getSubstitutionMap() {
        return substitutionMap;
    }

    /**
     * Class symbols for classes that need a reference to the outer class.
     */
    @Override
    Set<ClassSymbol> getHasOuters() {
        return hasOuters;
    }

    /**
     * @return the literalInitClassMap
     */
    @Override
    LiteralInitClassMap getLiteralInitClassMap() {
        return literalInitClassMap;
    }

    /**
     * @param literalInitClassMap the literalInitClassMap to set
     */
    private void setLiteralInitClassMap(LiteralInitClassMap literalInitClassMap) {
        this.literalInitClassMap = literalInitClassMap;
    }

    /**
     * Make a version of the on-replace to be used in inline in a setter.
     */
    private JCStatement translateTriggerAsInline(VarMorphInfo vmi, JFXOnReplace onReplace) {
        if (onReplace == null) return null;
        boolean isSequence = vmi.isSequence();
        if (isSequence) {
            OnReplaceInfo info = new OnReplaceInfo();
            info.onReplace = onReplace;
            info.vmi = vmi;
            info.outer = onReplaceInfo;
            if (onReplace.getNewElements() != null)
                info.newElementsSym = onReplace.getNewElements().sym;
            onReplaceInfo = info;
        }
        JCStatement ret = translateToStatement(onReplace.getBody(), syms.voidType);
        if (isSequence)
            onReplaceInfo = onReplaceInfo.outer;
        return ret;
    }

    void scriptBegin() {
    }

    private class ClassDeclarationTranslator extends Translator {

        private final JFXClassDeclaration tree;
        private final boolean isMixinClass;
        private final ListBuffer<JCTree> translatedDefs = ListBuffer.lb();

        ClassDeclarationTranslator(JFXClassDeclaration tree) {
            super(tree.pos());
            this.tree = tree;
            isMixinClass = tree.isMixinClass();
        }

        protected StatementsResult doit() {
            final ListBuffer<JCStatement> translatedInitBlocks = ListBuffer.lb();
            final ListBuffer<JCStatement> translatedPostInitBlocks = ListBuffer.lb();
            
            ListBuffer<JFXClassDeclaration> deferredClasses = ListBuffer.lb();

            ListBuffer<TranslatedVarInfo> attrInfo = ListBuffer.lb();
            ListBuffer<TranslatedOverrideClassVarInfo> overrideInfo = ListBuffer.lb();
            ListBuffer<TranslatedFuncInfo> funcInfo = ListBuffer.lb();

            ReceiverContext prevReceiverContext = receiverContext();

            // translate all the definitions that make up the class.
            // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JFXTree def : tree.getMembers()) {
                    switch (def.getFXTag()) {
                        case INIT_DEF: {
                            setContext(false);
                            translateAndAppendStaticBlock(((JFXInitDefinition) def).getBody(), translatedInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case POSTINIT_DEF: {
                            setContext(false);
                            translateAndAppendStaticBlock(((JFXPostInitDefinition) def).getBody(), translatedPostInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            setContext(attrDef.isStatic());
                            VarMorphInfo vmi = typeMorpher.varMorphInfo(attrDef.sym);
                            JFXExpression initializer = attrDef.getInitializer();
                            boolean initWithBoundFuncResult = 
                                (initializer instanceof JFXIdent) &&
                                isBoundFunctionResult(((JFXIdent)initializer).sym);
                            TranslatedVarInfo ai = new TranslatedVarInfo(
                                    attrDef,
                                    vmi,
                                    translateVarInit(attrDef),
                                    initWithBoundFuncResult? ((JFXIdent)initializer).sym : null,
                                    attrDef.isBound() ? translateBind.translateBoundExpression(initializer, attrDef.sym, attrDef.isBidiBind()) : null,
                                    attrDef.isBidiBind() ? translateInvBind.translate(initializer, attrDef.type, attrDef.sym) : null,
                                    attrDef.getOnReplace(),
                                    translateTriggerAsInline(vmi, attrDef.getOnReplace()),
                                    attrDef.getOnInvalidate(),
                                    translateTriggerAsInline(vmi, attrDef.getOnInvalidate()));
                            attrInfo.append(ai);
                            break;
                        }
                        case OVERRIDE_ATTRIBUTE_DEF: {
                            JFXOverrideClassVar override = (JFXOverrideClassVar) def;
                            setContext(override.isStatic());
                            VarMorphInfo vmi = typeMorpher.varMorphInfo(override.sym);
                            TranslatedOverrideClassVarInfo ai = new TranslatedOverrideClassVarInfo(
                                    override,
                                    vmi,
                                    translateVarInit(override),
                                    override.isBound() ? translateBind.translateBoundExpression(override.getInitializer(), override.sym, override.isBidiBind()) : null,
                                    override.isBidiBind() ? translateInvBind.translate(override.getInitializer(), override.type, override.sym) : null,
                                    override.getOnReplace(),
                                    translateTriggerAsInline(vmi, override.getOnReplace()),
                                    override.getOnInvalidate(),
                                    translateTriggerAsInline(vmi, override.getOnInvalidate()));
                            overrideInfo.append(ai);
                            break;
                        }
                        case FUNCTION_DEF: {
                            JFXFunctionDefinition funcDef = (JFXFunctionDefinition) def;
                            setContext(funcDef.isStatic());
                            funcInfo.append(new TranslatedFuncInfo(funcDef, translateToSpecialResult(funcDef).trees()));
                            break;
                        }
                        case CLASS_DEF: {
                            // Handle other classes.
                            if (isMixinClass) {
                                deferredClasses.append((JFXClassDeclaration)def);
                            } else {
                                inInstanceContext = ReceiverContext.Oops;
                                translatedDefs.appendList(translateToStatementsResult((JFXClassDeclaration)def, syms.voidType).trees());
                            }
                            break;
                        }
                        default: {
                            assert false : "Unexpected class member: " + def;
                            inInstanceContext = ReceiverContext.Oops;
                            translatedDefs.appendList(translateToSpecialResult(def).trees());
                            break;
                        }
                    }
                }
            }
            
            inInstanceContext = ReceiverContext.Oops;

            // the translated defs have prepends in front
            for (JCTree prepend : prependToDefinitions) {
                translatedDefs.prepend(prepend);
            }

            inInstanceContext = ReceiverContext.Oops;

            prependToDefinitions = prevPrependToDefinitions;
            prependToStatements = prevPrependToStatements;
            // WARNING: translate can't be called directly or indirectly after this point in the method, or the prepends won't be included

            JavafxClassModel model = initBuilder.createJFXClassModel(tree, 
                    attrInfo.toList(),
                    overrideInfo.toList(),
                    funcInfo.toList(),
                    getLiteralInitClassMap(),
                    translatedInitBlocks,
                    translatedPostInitBlocks);
            additionalImports.appendList(model.additionalImports);

            translatedDefs.appendList(model.additionalClassMembers);

            if (isMixinClass && deferredClasses.nonEmpty()) {
                ListBuffer<JCStatement> savedPrependToDefinitions = prependToDefinitions;
                ListBuffer<JCStatement> savedPrependToStatements = prependToStatements;
                prependToStatements = prependToDefinitions = ListBuffer.lb();
                
                // Add partial list of member symbols to mixin class symbol so that
                // nested classes will "see" synthesized mixin methods.
                membersToSymbol((ClassSymbol)tree.sym, translatedDefs.toList());
                
                for (JFXClassDeclaration deferredClass : deferredClasses) {
                    inInstanceContext = ReceiverContext.Oops;
                    translatedDefs.appendList(translateToStatementsResult(deferredClass, syms.voidType).trees());
                }
                
                assert prependToDefinitions.isEmpty() : "prependToDefinitions is non-empty";
                assert prependToStatements.isEmpty() : "prependToStatements is non-empty";
                
                prependToDefinitions = savedPrependToDefinitions;
                prependToStatements = savedPrependToStatements;
            }
            
            // build the list of implemented interfaces
            List<JCExpression> implementing = model.interfaces;

            // include the interface only once
            if (!tree.hasBeenTranslated) {
                if (isMixinClass) {
                    JCModifiers mods = m().Modifiers(Flags.PUBLIC | Flags.INTERFACE);
                    mods = addAccessAnnotationModifiers(diagPos, tree.mods.flags, mods);

                    JCClassDecl cInterface = m().ClassDef(mods,
                            model.interfaceName, List.<JCTypeParameter>nil(), null,
                            implementing, model.iDefinitions);
        
                    cInterface.sym = makeClassSymbol(mods.flags, cInterface.name, tree.sym.owner);
                    
                    membersToSymbol(cInterface);
                    
                    prependToDefinitions.append(cInterface); // prepend to the enclosing class or top-level
                }
                tree.hasBeenTranslated = true;
            }

            // Class must be visible for reflection.
            long flags = tree.mods.flags & (Flags.FINAL | Flags.ABSTRACT | Flags.SYNTHETIC);
            if ((flags & Flags.SYNTHETIC) == 0) {
                flags |= Flags.PUBLIC;
            }
            if (tree.sym.owner.kind == Kinds.TYP) {
                flags |= Flags.STATIC;
            }
            // JFXC-2831 - Mixins should be abstract.
            if (tree.sym.kind == Kinds.TYP && isMixinClass) {
                flags |= Flags.ABSTRACT;
            }

            JCModifiers classMods = make.at(diagPos).Modifiers(flags);
            classMods = addAccessAnnotationModifiers(diagPos, tree.mods.flags, classMods);

            // make the Java class corresponding to this FX class, and return it
            JCClassDecl res = m().ClassDef(
                    classMods,
                    tree.getName(),
                    List.<JCTypeParameter>nil(), // type parameters
                    model.superType == null ? null : makeType(model.superType, false),
                    implementing,
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
        
            membersToSymbol(res);

            setReceiverContext(prevReceiverContext);

            return new StatementsResult(res);
        }

        private void setContext(boolean isStatic) {
            setReceiverContext( isStatic ?
                  ReceiverContext.ScriptAsStatic
                : isMixinClass ?
                          ReceiverContext.InstanceAsStatic
                        : ReceiverContext.InstanceAsInstance );

        }

        private JCStatement translateVarInit(JFXAbstractVar var) {
            if (var.getInitializer()==null || var.isBound()) {
                // no init, or init handled by bind or JavafxVarInit
                return null;
            }
            Name instanceName = (var.isStatic() || !isMixinClass) ? null : defs.receiverName;
            return Stmt(translateDefinitionalAssignmentToSetExpression(
                    var.pos(),
                    var.getInitializer(),
                    typeMorpher.varMorphInfo(var.sym),
                    instanceName
                 ).expr());
        }

        private void translateAndAppendStaticBlock(JFXBlock block, ListBuffer<JCStatement> translatedBlocks) {
            JCStatement stmt = translateToStatement(block, syms.voidType);
            if (stmt != null) {
                translatedBlocks.append(stmt);
            }
        }
    }

    private JCExpression translateNonBoundInit(DiagnosticPosition diagPos,
            JFXExpression init, VarMorphInfo vmi) {
        // normal init -- unbound
        if (init == null) {
            // no initializing expression determine a default value from the type
            return makeDefaultValue(diagPos, vmi);
        } else {
            // do a vanilla translation of the expression
            Type resultType = vmi.getSymbol().type;
            JCExpression trans = translateToExpression(init, resultType);
            return convertNullability(diagPos, trans, init, resultType);
        }
    }

    private ExpressionResult translateDefinitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            final JFXExpression init,
            final VarMorphInfo vmi,
            final Name instanceName) {

        return new ExpressionTranslator(diagPos) {
            protected ExpressionResult doit() {
                Symbol vsym = vmi.getSymbol();
                assert ((vsym.flags() & Flags.PARAMETER) == 0L) : "Parameters are not initialized";
                setSubstitution(init, vsym);
                final JCExpression nonNullInit = translateNonBoundInit(diagPos, init, vmi);
                final boolean isLocal = !vmi.isMemberVariable();
                assert !isLocal || instanceName == null;
                JCExpression res;
                if (vmi.isMemberVariable() && vmi.isSequence()) {
                    JCExpression tc = 
                            instanceName == null ?
                                vsym.isStatic()?
                                      Call(makeType(vsym.owner.type), scriptLevelAccessMethod(vsym.owner))
                                    : id(names._this)
                               : id(instanceName);
                    res = Call(defs.Sequences_set, tc, Offset(vsym), nonNullInit);
                } else if (vmi.useAccessors()) {
                    JCExpression tc = instanceName == null ? null : id(instanceName);
                    res = Call(tc, attributeBeName(vsym), nonNullInit);
                } else {
                    // TODO: Java inherited.
                    final JCExpression varRef = id(vsym);
                    res = m().Assign(varRef, nonNullInit);
                }
                return toResult(res, vmi.getRealType());
            }
        }.doit();
    }

    /**
     * Translate a local variable
     */
    private class VarTranslator extends ExpressionTranslator {

        final JFXVar tree;
        final VarSymbol vsym;
        final VarMorphInfo vmi;
        final boolean hasForwardReference;
        final boolean isAssignedTo;
        final long modFlags;

        VarTranslator(JFXVar tree) {
            super(tree.pos());
            this.tree = tree;
            JFXModifiers mods = tree.getModifiers();
            vsym = tree.getSymbol();
            vmi = typeMorpher.varMorphInfo(vsym);
            long flags = vsym.flags();
            assert vsym.owner.kind != Kinds.TYP : "attributes are processed in the class and should never come here: " + tree.name;
            assert (flags & Flags.PARAMETER) == 0L : "we should not see parameters here" + tree.name;
            hasForwardReference = (flags & JavafxFlags.VARUSE_FORWARD_REFERENCE) != 0L;
            isAssignedTo = (flags & JavafxFlags.VARUSE_ASSIGNED_TO) != 0L;
            modFlags = (mods.flags & ~Flags.FINAL) | ((hasForwardReference || isAssignedTo) ? 0L : Flags.FINAL);
        }

        protected AbstractStatementsResult doit() {
            optStat.recordLocalVar(vsym, tree.getBindStatus().isBound(), false);

            if (hasForwardReference) {
                // create a blank variable declaration and move the declaration to the beginning of the block
                JCExpression init = JavafxToJava.this.makeDefaultValue(diagPos, vmi);
                prependToStatements.prepend(Var(modFlags, tree.type, tree.name, init));
                return translateDefinitionalAssignmentToSetExpression(diagPos, tree.getInitializer(), vmi, null);
            } else {
                // Translate in-place
                JCExpression init = translateNonBoundInit(diagPos, tree.getInitializer(), typeMorpher.varMorphInfo(vsym));
                JCStatement var = Var(modFlags, tree.type, tree.name, init);
                return new StatementsResult(var);
            }
        }
    }

    boolean isInnerFunction(MethodSymbol sym) {
        return sym.owner != null && sym.owner.kind != Kinds.TYP && (sym.flags() & Flags.SYNTHETIC) == 0;
    }

    private class BlockExpressionTranslator extends ExpressionTranslator {

        private final JFXExpression value;
        private final List<JFXExpression> statements;

        BlockExpressionTranslator(JFXBlock tree) {
            super(tree.pos());
            this.value = tree.value;
            this.statements = tree.getStmts();
        }

        protected AbstractStatementsResult doit() {
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            try {
                prependToStatements = ListBuffer.lb();

                for (JFXExpression expr : statements) {
                    JCStatement stmt = translateToStatement(expr, syms.voidType);
                    if (stmt != null) {
                        addPreface(stmt);
                    }
                }

                if (yield() == ToExpression) {
                    // make into block expression
                    assert (targetType != syms.voidType) : "void block expressions should be handled below";

                    //TODO: this may be unneeded, or even wrong
                    JFXExpression rawValue = (value.getFXTag() == JavafxTag.RETURN)?
                         ((JFXReturn) value).getExpression()
                        : value;

                    JCExpression tvalue = translateExpr(rawValue, targetType); // must be before prepend
                    List<JCStatement> localDefs = prependToStatements.appendList(statements()).toList();
                    return new ExpressionResult(
                            diagPos,
                            List.<JCStatement>nil(), //TODO: statements rolled into expression (below) is this needed?
                            localDefs.size() == 0 ? tvalue : BlockExpression(localDefs, tvalue),
                            bindees(),
                            invalidators(),
                            interClass(),
                            targetType);
                } else {
                    // make into block
                    if (value != null) {
                        if (value.getFXTag() == JavafxTag.VAR_SCRIPT_INIT && targetType != syms.voidType) {
                            translateStmt(value, syms.voidType);
                            addPreface(Stmt(Get(((JFXVarInit) value).getSymbol()), targetType));
                        } else {
                            translateStmt(value, targetType);
                        }
                    }
                    List<JCStatement> localDefs = prependToStatements.appendList(statements()).toList();
                    return new StatementsResult(localDefs.size() == 1 ? localDefs.head : Block(localDefs));
                }
            } finally {
                prependToStatements = prevPrependToStatements;
            }
        }
    }

    class SequenceActionTranslator extends AssignTranslator {

        final RuntimeMethod meth;

        /**
         *
         * @param diagPos
         * @param ref Variable being referenced (different from LHS if indexed -- where it is sequence or array)
         * @param indexOrNull The index into the variable reference.  Or null if not indexed.
         * @param rhs The expression acting on ref or null
         */
        SequenceActionTranslator(DiagnosticPosition diagPos, JFXExpression ref, RuntimeMethod meth, JFXExpression indexOrNull, JFXExpression rhs) {
            this(diagPos, ref, meth, indexOrNull, syms.voidType, rhs);
        }

        SequenceActionTranslator(DiagnosticPosition diagPos, JFXExpression ref, RuntimeMethod meth, JFXExpression indexOrNull, Type fullType, JFXExpression rhs) {
            super(diagPos, ref, indexOrNull, fullType, rhs);
            this.meth = meth;
        }

        SequenceActionTranslator(DiagnosticPosition diagPos, JFXExpression ref, RuntimeMethod meth, JFXExpression indexOrNull) {
            this(diagPos, ref, meth, indexOrNull, null);
        }

        @Override
        JCExpression fullExpression(JCExpression tToCheck) {
            return sequencesOp(meth, tToCheck);
        }

        @Override
        JCExpression sequencesOp(RuntimeMethod meth, JCExpression tToCheck) {
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            if (refSym.owner.kind != Kinds.TYP) {
                // Local variable sequence -- roughly:
                // lhs = sequenceAction(lhs, rhs);
                args.append(id(refSym.name));
            } else {
                // Instance variable sequence -- roughly:
                // sequenceAction(instance, varNum, rhs);
                args.append(instance(tToCheck));
                args.append(Offset(copyOfTranslatedToCheck(translateToCheck(selector)), refSym));
            }
            JCExpression tRHS = buildRHS(rhsTranslated);
            if (tRHS != null) {
                args.append(tRHS);
            }
            JCExpression tIndex = translateIndex();
            if (tIndex != null) {
                args.append(tIndex);
                JCExpression tIndex2 = translateIndex2();
                if (tIndex2 != null) {
                    args.append(tIndex2);
                }
            }
            JCExpression res = Call(meth, args);
            if (refSym.owner.kind != Kinds.TYP) {
                // It is a local variable sequence, assign the result
                res = m().Assign(id(refSym.name), res);
            }
            return res;
        }

        JCExpression translateIndex2() {
            return null;
        }

        /**
         * If we are operating on an array or sequence, convert to the sequence type.
         * Otherwise, to the element type.
         */
        @Override
        protected Type rhsType() {
            if (types.isArray(rhs.type) || types.isSequence(rhs.type))
                return ref.type;
            else
                return types.elementType(ref.type);
        }
    }

    class SequenceSliceActionTranslator extends SequenceActionTranslator {

        private final JFXSequenceSlice slice;

        SequenceSliceActionTranslator(JFXSequenceSlice slice, RuntimeMethod meth, Type fullType, JFXExpression rhs) {
            super(slice.pos(), slice.getSequence(), meth, slice.getFirstIndex(), fullType, rhs);
            this.slice = slice;
        }

        @Override
        JCExpression translateIndex2() {
            return makeSliceEndPos(slice);
        }
    }

    class SequenceInsertTranslator extends SequenceActionTranslator {
        JFXSequenceInsert tree;

        public SequenceInsertTranslator(JFXSequenceInsert tree) {
            super(
                    tree.pos(),
                    tree.getSequence(),
                    (tree.getPosition() == null)? defs.Sequences_insert : defs.Sequences_insertBefore,
                    tree.getPosition(),
                    tree.getElement());
            this.tree = tree;
        }

        @Override
        JCExpression translateIndex() {
            if (indexOrNull == null) {
                return null;
            }
            JCExpression position = translateExpr(indexOrNull, syms.intType);
            if (tree.shouldInsertAfter()) {
                position = PLUS(position, Int(1));
            }
            return position;
        }
    }

    private class TryTranslator extends ExpressionTranslator {

        private final JFXTry tree;

        TryTranslator(JFXTry tree) {
            super(tree.pos());
            this.tree = tree;
        }

        protected StatementsResult doit() {
            ListBuffer<JCCatch> tCatchers = ListBuffer.lb();
            for (List<JFXCatch> l = tree.catchers; l.nonEmpty(); l = l.tail) {
                JFXCatch cat = l.head;
                JCVariableDecl param = convertParam(cat.param);
                JCBlock tCatBody = translateToBlock(cat.body, syms.voidType);
                tCatchers.append(m().Catch(param, tCatBody));
            }
            JCBlock body = translateToBlock(tree.body, syms.voidType);
            JCBlock finalizer = translateToBlock(tree.finalizer, syms.voidType);
            return new StatementsResult(m().Try(body, tCatchers.toList(), finalizer));
        }
    }

    private class WhileTranslator extends ExpressionTranslator {

        private final JFXWhileLoop tree;

        WhileTranslator(JFXWhileLoop tree) {
            super(tree);
            this.tree = tree;
        }

        protected StatementsResult doit() {
            JCExpression cond = translateExpr(tree.cond, syms.booleanType);

            JCStatement body = translateToStatement(tree.body, syms.voidType);

            return toStatementResult(m().WhileLoop(cond, body));
        }
    }

    class ScriptTranslator extends Translator {

        final JFXScript tree;

        ScriptTranslator(JFXScript tree) {
            super(tree.pos());
            this.tree = tree;
        }

        SpecialResult doit() {
            // add to the hasOuters set the class symbols for classes that need a reference to the outer class
            fillClassesWithOuters(tree);

            ListBuffer<JCTree> translatedDefinitions = ListBuffer.lb();
            ListBuffer<JCTree> imports = ListBuffer.lb();
            additionalImports = ListBuffer.lb();
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            for (JFXTree def : tree.defs) {
                switch (def.getFXTag()) {
                    case IMPORT:
                        // ignore import
                        break;
                    case CLASS_DEF:
                        translatedDefinitions.appendList(translateToStatementsResult((JFXClassDeclaration) def, syms.voidType).trees());
                        break;
                    default:
                        assert false : "something wierd in the script: " + def;
                        break;
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
                translatedDefinitions.append(m().Import(prepend, false));
            }

            prependToStatements = prependToDefinitions = null; // shouldn't be used again until the next top level

            JCExpression pid = straightConvert(tree.pid);
            JCCompilationUnit translated = m().TopLevel(List.<JCAnnotation>nil(), pid, translatedDefinitions.toList());
            translated.sourcefile = tree.sourcefile;
            // System.err.println("<translated src="+tree.sourcefile+">"); System.err.println(translated); System.err.println("</translated>");
            translated.docComments = null;
            translated.lineMap = tree.lineMap;
            translated.flags = tree.flags;
            return new SpecialResult(translated);
        }
    }

    class InvalidateTranslator extends ExpressionTranslator {

        private final JFXExpression varRef;
        private final Symbol vsym;
        private JCVariableDecl invalVar = null;
        private JCVariableDecl newLenVar = null;

        InvalidateTranslator(JFXInvalidate tree) {
            super(tree.pos());
            this.varRef = tree.getVariable();
            this.vsym = JavafxTreeInfo.symbol(varRef);
        }

        private JCExpression receiver() {
            return invalVar == null ? null : id(invalVar);
        }

        private void callInvalidate(Name phase) {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            if (types.isSequence(vsym.type)) {
                addPreface(newLenVar);
                args.append(Int(0));
                args.append(MINUS(id(newLenVar), Int(1)));
                args.append(id(newLenVar));
            }

            args.append(id(phase));

            addPreface(CallStmt(receiver(), attributeInvalidateName(vsym), args.toList()));
        }

        protected AbstractStatementsResult doit() {
            if (varRef.getFXTag() == JavafxTag.SELECT) {
                JFXSelect sel = (JFXSelect) varRef;
                JCExpression selected = translateToExpression(sel.selected, sel.selected.type);
                if (selected != null) {
                    invalVar = TmpVar("inval", sel.selected.type, selected);
                    addPreface(invalVar);
                }
            }

            if (types.isSequence(vsym.type)) {
                newLenVar = TmpVar("newLen", syms.intType, Call(receiver(), attributeSizeName(vsym)));
                addPreface(newLenVar);
            }

            callInvalidate(defs.varFlagIS_INVALID);
            callInvalidate(defs.varFlagNEEDS_TRIGGER);

            return toStatementResult();
        }
    }

    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "jfx$";
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

            @Override
            public void visitSelect(JFXSelect tree) {
                super.visitSelect(tree);
                Symbol sym = expressionSymbol(tree.selected);
                if (currentClass != null && sym != null && sym.kind == Kinds.TYP) {
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
                            getHasOuters().add(cs);
                        }
                    }
                }
            }
        }

        new FillClassesWithOuters().scan(tree);
    }

    /***********************************************************************
     *
     * Visitors  (alphabetical order)
     *
     * Overrides to add contructs allowed in non-bound contexts.
     */

    @Override
    public void visitAssign(final JFXAssign tree) {
        if (types.isSequence(tree.type)) {
            if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
                result = new SequenceSliceActionTranslator((JFXSequenceSlice) tree.lhs, defs.Sequences_replaceSlice, tree.type, tree.rhs).doit();
            } else {
                result = new SequenceActionTranslator(tree.pos(), tree.lhs, defs.Sequences_set, null, tree.type, tree.rhs) {

                    @Override
                    protected Type rhsType() {
                        return tree.type;
                    }
                }.doit();
            }
        } else {
            result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

                @Override
                JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
                    return m().Assign(lhsTranslated, rhsTranslated);
                }
            }.doit();
        }
    }

    @Override
    public void visitAssignop(final JFXAssignOp tree) {
        badVisitor("Assignop should have been lowered");
    }

    public void visitBlockExpression(JFXBlock tree) {
        result = new BlockExpressionTranslator(tree).doit();
    }

    @Override
    public void visitBreak(JFXBreak tree) {
        result = new StatementsResult(make.at(tree.pos).Break(tree.label));
    }

    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXClassDeclaration prevClass = currentClass();
        setCurrentClass(tree);

        if (tree.isScriptClass()) {
            scriptBegin();
        }

        try {
            if (tree.isScriptClass()) {
                setLiteralInitClassMap(new LiteralInitClassMap());
            }

            result = new ClassDeclarationTranslator(tree).doit();

        } finally {
            setCurrentClass(prevClass);
        }
    }

    @Override
    public void visitContinue(JFXContinue tree) {
        result = new StatementsResult(make.at(tree.pos).Continue(tree.label));
    }

    @Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        JFXFunctionDefinition prevFunction = currentFunction();
        try {
            setCurrentFunction(tree);
            result = new FunctionTranslator(tree, false).doit();
        }
        finally {
            setCurrentFunction(prevFunction);
        }
    }

    @Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = new FunctionValueTranslator(make.Ident(defs.lambda_MethodName), def, tree.pos(), (MethodType) def.type, tree.type).doit();
    }

    public void visitIdent(JFXIdent tree) {
        if (substitute(tree.pos(),tree.sym)) {
            return;
        }
        result = new IdentTranslator(tree).doit();
    }

    @Override
    public void visitInvalidate(final JFXInvalidate tree) {
        result = new InvalidateTranslator(tree).doit();
    }

    @Override
    public void visitParens(JFXParens tree) {
        if (yield() == ToExpression) {
            result = translateToExpressionResult(tree.expr, targetType);
        } else {
            result = translateToStatementsResult(tree.expr, targetType);
        }
    }

    @Override
    public void visitReturn(JFXReturn tree) {
        JFXExpression exp = tree.getExpression();
        if (exp == null) {
            result = new StatementsResult(make.at(tree).Return(null));
        } else {
            result = translateToStatementsResult(exp, exp.type);
        }
    }

    @Override
    public void visitScript(JFXScript tree) {
        result = new ScriptTranslator(tree).doit();
    }

    public void visitSelect(JFXSelect tree) {
        if (substitute(tree.pos(),tree.sym)) {
            return;
        }
        result = new SelectTranslator(tree).doit();
    }

    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression seq = tree.getSequence();
        SequenceActionTranslator trans;
        if (tree.getElement() != null) {
            trans = new SequenceActionTranslator(diagPos, seq, defs.Sequences_deleteValue, null, tree.getElement());
        } else {
            switch (seq.getFXTag()) {
                case SEQUENCE_INDEXED:
                    JFXSequenceIndexed si = (JFXSequenceIndexed) seq;
                    trans = new SequenceActionTranslator(diagPos, si.getSequence(), defs.Sequences_deleteIndexed, si.getIndex());
                    break;
                case SEQUENCE_SLICE:
                    final JFXSequenceSlice ss = (JFXSequenceSlice) seq;
                    trans = new SequenceSliceActionTranslator((JFXSequenceSlice) seq, defs.Sequences_deleteSlice, syms.voidType, null);
                    break;
                default:
                    if (types.isSequence(seq.type)) {
                        trans = new SequenceActionTranslator(diagPos, seq, defs.Sequences_deleteAll, null);
                    } else {
                        TODO("delete non-sequence");
                        trans = null; //shut-up
                    }
            }
        }
        result = trans.doit();
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        result = new SequenceInsertTranslator(tree).doit();
    }

    @Override
    public void visitSkip(JFXSkip tree) {
        result = new StatementsResult(make.at(tree.pos).Skip());
    }

    @Override
    public void visitThrow(JFXThrow tree) {
        JCTree expr = translateToExpression(tree.expr, tree.type);
        result = new StatementsResult(make.at(tree.pos).Throw(expr));
    }

    @Override
    public void visitTry(JFXTry tree) {
        result = new TryTranslator(tree).doit();
    }

    @Override
    public void visitVar(JFXVar tree) {
        result = new VarTranslator(tree).doit();
    }

    @Override
    public void visitWhileLoop(JFXWhileLoop tree) {
        result = new WhileTranslator(tree).doit();
    }

}
