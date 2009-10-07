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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.mjavac.code.*;
import static com.sun.tools.mjavac.code.Flags.*;
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
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.Result;
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
public class JavafxToJava extends JavafxAbstractTranslation<Result> {
    protected static final Context.Key<JavafxToJava> jfxToJavaKey =
        new Context.Key<JavafxToJava>();

    /*
     * modules imported by context
     */
    private final JavafxInitializationBuilder initBuilder;
    private final JavafxTranslateBind translateBind;

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

    /*
     * static information
     */
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.ObjectArraySequence";
    private static final String noMainExceptionString = "com.sun.javafx.runtime.NoMainException";
    private static final String toSequenceString = "toSequence";
    private static final String methodThrowsString = "java.lang.Throwable";

    public static JavafxToJava instance(Context context) {
        JavafxToJava instance = context.get(jfxToJavaKey);
        if (instance == null)
            instance = new JavafxToJava(context);
        return instance;
    }

    protected JavafxToJava(Context context) {
        super(context, null);

        context.put(jfxToJavaKey, this);

        initBuilder = JavafxInitializationBuilder.instance(context);
        translateBind = JavafxTranslateBind.instance(context);
    }

    /**
     * Entry point
     */
    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.setAttrEnv(attrEnv);

        attrEnv.translatedToplevel = (JCCompilationUnit)((SpecialResult)translateToSpecialResult(attrEnv.toplevel)).tree();
        attrEnv.translatedToplevel.endPositions = attrEnv.toplevel.endPositions;
    }

    JCExpression TODO(JFXTree tree) {
        return TODO("non-bound " + tree.getClass().getSimpleName());
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

    private boolean substitute(Symbol sym) {
        Name name = getSubstitutionMap().get(sym);
        if (name == null) {
            return false;
        } else {
            result = new ExpressionResult(make.Ident(name), sym.type);
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

    static class OnReplaceInfo {
        public OnReplaceInfo outer;
        Symbol newElementsSym;
        Symbol oldValueSym;
        Type arraySequenceType;
        Type seqWithExtendsType;
    }

    OnReplaceInfo onReplaceInfo;

    OnReplaceInfo findOnReplaceInfo(Symbol sym) {
        OnReplaceInfo info = onReplaceInfo;
        while (info != null && sym != info.newElementsSym && sym != info.oldValueSym)
            info = info.outer;
        return info;
    }

    /**
     * Make a version of the on-replace to be used in inline in a setter.
     */
    private JCStatement translateTriggerAsInline(VarSymbol vsym, JFXOnReplace onReplace) {
        if (onReplace == null) return null;
        return translateToStatement(onReplace.getBody(), syms.voidType);
    }

    void scriptBegin() {
    }

    //@Override
    public void visitScript(JFXScript tree) {
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
            translatedDefinitions.append(make.Import(prepend, false));
        }

        prependToStatements = prependToDefinitions = null; // shouldn't be used again until the next top level

        JCExpression pid = straightConvert(tree.pid);
        JCCompilationUnit translated = make.at(tree.pos).TopLevel(List.<JCAnnotation>nil(), pid, translatedDefinitions.toList());
        translated.sourcefile = tree.sourcefile;
        // System.err.println("<translated src="+tree.sourcefile+">"); System.err.println(translated); System.err.println("</translated>");
        translated.docComments = null;
        translated.lineMap = tree.lineMap;
        translated.flags = tree.flags;
        result = new SpecialResult(translated);
    }

    class ClassDeclarationTranslator extends Translator {

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

            ListBuffer<TranslatedVarInfo> attrInfo = ListBuffer.lb();
            ListBuffer<TranslatedOverrideClassVarInfo> overrideInfo = ListBuffer.lb();
            ListBuffer<TranslatedFuncInfo> funcInfo = ListBuffer.lb();

            // translate all the definitions that make up the class.
            // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JFXTree def : tree.getMembers()) {
                    switch (def.getFXTag()) {
                        case INIT_DEF: {
                            inInstanceContext = isMixinClass ? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            translateAndAppendStaticBlock(((JFXInitDefinition) def).getBody(), translatedInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case POSTINIT_DEF: {
                            inInstanceContext = isMixinClass ? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            translateAndAppendStaticBlock(((JFXPostInitDefinition) def).getBody(), translatedPostInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            boolean isStatic = (attrDef.getModifiers().flags & STATIC) != 0;
                            inInstanceContext = isStatic ? ReceiverContext.ScriptAsStatic : isMixinClass ? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            JCStatement initStmt = (attrDef.isBound() || attrDef.deferInit()) ?
                                  null // init handled by bind or JavafxVarScriptInit
                                : translateDefinitionalAssignmentToSet(
                                    attrDef.pos(),
                                    attrDef.getInitializer(), 
                                    attrDef.getBindStatus(),
                                    attrDef.sym,
                                    (isStatic || !isMixinClass) ? null : defs.receiverName);
                            attrInfo.append(new TranslatedVarInfo(
                                    attrDef,
                                    typeMorpher.varMorphInfo(attrDef.sym),
                                    initStmt,
                                    attrDef.isBound() ? translateBind.translate(attrDef.getInitializer(), attrDef.type, attrDef.sym) : null,
                                    attrDef.getOnReplace(),
                                    translateTriggerAsInline(attrDef.sym, attrDef.getOnReplace()),
                                    attrDef.getOnInvalidate(),
                                    translateTriggerAsInline(attrDef.sym, attrDef.getOnInvalidate())));
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case OVERRIDE_ATTRIBUTE_DEF: {
                            JFXOverrideClassVar override = (JFXOverrideClassVar) def;
                            boolean isStatic = (override.sym.flags() & STATIC) != 0;
                            inInstanceContext = isStatic ? ReceiverContext.ScriptAsStatic : isMixinClass ? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            JCStatement initStmt;
                            initStmt = override.isBound() ?
                                  null // init handled by bind
                                : translateDefinitionalAssignmentToSet(
                                    override.pos(),
                                    override.getInitializer(), 
                                    override.getBindStatus(),
                                    override.sym,
                                    (isStatic || !isMixinClass) ? null : defs.receiverName);
                            overrideInfo.append(new TranslatedOverrideClassVarInfo(
                                    override,
                                    typeMorpher.varMorphInfo(override.sym),
                                    initStmt,
                                    override.isBound() ? translateBind.translate(override.getInitializer(), override.type, override.sym) : null,
                                    override.getOnReplace(),
                                    translateTriggerAsInline(override.sym, override.getOnReplace()),
                                    override.getOnInvalidate(),
                                    translateTriggerAsInline(override.sym, override.getOnInvalidate())));
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case FUNCTION_DEF: {
                            JFXFunctionDefinition funcDef = (JFXFunctionDefinition) def;
                            funcInfo.append(new TranslatedFuncInfo(funcDef, translateToSpecialResult(funcDef).trees()));
                            break;
                        }
                        case CLASS_DEF: {
                            // Handle other classes.
                            translatedDefs.appendList(translateToStatementsResult((JFXClassDeclaration)def, syms.voidType).trees());
                            break;
                        }
                        default: {
                            assert false : "Unexpected class member: " + def;
                            translatedDefs.appendList(translateToSpecialResult(def).trees());
                            break;
                        }
                    }
                }
            }
            // the translated defs have prepends in front
            for (JCTree prepend : prependToDefinitions) {
                translatedDefs.prepend(prepend);
            }
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

            // include the interface only once
            if (!tree.hasBeenTranslated) {
                if (isMixinClass) {
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

            // build the list of implemented interfaces
            List<JCExpression> implementing = model.interfaces;

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
            return new StatementsResult(res);
        }

        private void translateAndAppendStaticBlock(JFXBlock block, ListBuffer<JCStatement> translatedBlocks) {
            JCStatement stmt = translateToStatement(block, syms.voidType);
            if (stmt != null) {
                translatedBlocks.append(stmt);
            }
        }
    }

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

    public void visitInstanciate(JFXInstanciate tree) {
        result = new InstanciateTranslator(tree) {
            protected void processLocalVar(JFXVar var) {
                translateStmt(var, syms.voidType);
            }
        }.doit();
    }

    public void visitStringExpression(JFXStringExpression tree) {
        result = new StringExpressionTranslator(tree).doit();
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

    private JCStatement translateDefinitionalAssignmentToSet(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            Name instanceName) {
        if (init == null) {
            // Nothing to set, no init statement
            return null;
        }
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        return make.at(diagPos).Exec( translateDefinitionalAssignmentToSetExpression(diagPos,
            init, bindStatus, vmi, instanceName) );
    }

    private JCExpression translateDefinitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarMorphInfo vmi,
            Name instanceName) {
        Symbol vsym = vmi.getSymbol();
        assert( (vsym.flags() & Flags.PARAMETER) == 0L): "Parameters are not initialized";
        setSubstitution(init, vsym);
        JCExpression valueArg = translateNonBoundInit(diagPos, init, vmi);
        return definitionalAssignmentToSetExpression(diagPos, valueArg, bindStatus, instanceName, vmi);
    }

    //TODO: should go away and be folded with above
    private JCExpression definitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus,
            Name instanceName, VarMorphInfo vmi) {
        Symbol vsym = vmi.getSymbol();
        final boolean isLocal = !vmi.isMemberVariable();
        assert !isLocal || instanceName == null;
        final JCExpression nonNullInit = (init == null) ? makeDefaultValue(diagPos, vmi) : init;  //TODO: is this needed?

        if (vmi.useAccessors()) {
            JCExpression tc = instanceName == null ? null : make.at(diagPos).Ident(instanceName);
            return call(diagPos, tc, attributeBeName(vsym), nonNullInit);
        }
        // TODO: Java inherited.
        final JCExpression varRef = //TODO: fix me
                  make.at(diagPos).Ident(vsym) // It is a local variable
                ;
        return make.at(diagPos).Assign(varRef, nonNullInit);
    }

    public void visitVarScriptInit(JFXVarScriptInit tree) {
        DiagnosticPosition diagPos = tree.pos();
        VarSymbol vsym = tree.getSymbol();
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        JFXVar var = tree.getVar();
        assert !attrEnv.toplevel.isLibrary;

        result = new ExpressionResult(
                translateDefinitionalAssignmentToSetExpression(diagPos, var.getInitializer(),var.getBindStatus(), vmi, null),
                tree.type);
    }

    class VarTranslator extends ExpressionTranslator {

        final JFXVar tree;
        final JFXModifiers mods;
        final VarSymbol vsym;
        final VarMorphInfo vmi;
        final long flags;
        final boolean isParameter;
        final boolean hasInnerAccess;
        final long modFlags;

        VarTranslator(JFXVar tree) {
            super(tree.pos());
            this.tree = tree;
            mods = tree.getModifiers();
            vsym = tree.getSymbol();
            vmi = typeMorpher.varMorphInfo(vsym);
            assert vsym.owner.kind != Kinds.TYP : "attributes are processed in the class and should never come here: " + tree.name;
            flags = vsym.flags();
            isParameter = (flags & Flags.PARAMETER) != 0;
            hasInnerAccess = (flags & JavafxFlags.VARUSE_INNER_ACCESS) != 0;
            modFlags = (mods.flags & ~Flags.FINAL) | ((hasInnerAccess | isParameter) ? Flags.FINAL : 0L);
        }

        protected AbstractStatementsResult doit() {
            // for class vars, initialization happens during class init, so set to
            // default Location.  For local vars translate as definitional
            assert !isParameter;
            JCExpression init;
            // create a blank variable declaration and move the declaration to the beginning of the block
            optStat.recordLocalVar(vsym, tree.getBindStatus().isBound(), false);
            if ((modFlags & Flags.FINAL) != 0) {
                //TODO: this case probably won't be used any more, but it will
                // be again if we optimize the case for initializer which don't reference locals
                init = translateNonBoundInit(diagPos, tree.getInitializer(),typeMorpher.varMorphInfo(vsym));
                JCStatement var = makeVar(modFlags, tree.type, tree.name, init);
                prependToStatements.append(var);
                return new StatementsResult(diagPos, List.<JCStatement>nil());
            }
            init = makeDefaultValue(diagPos, vmi);
            prependToStatements.prepend(makeVar(modFlags, tree.type, tree.name, init));

            return toResult(
                    translateDefinitionalAssignmentToSetExpression(diagPos, tree.getInitializer(),tree.getBindStatus(), vmi, null),
                    tree.type);
        }
    }

    public void visitVar(JFXVar tree) {
        result = new VarTranslator(tree).doit();
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = new FunctionValueTranslator(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type, tree.type).doit();
    }

   boolean isInnerFunction (MethodSymbol sym) {
       return sym.owner != null && sym.owner.kind != Kinds.TYP
                && (sym.flags() & Flags.SYNTHETIC) == 0;
   }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        result = new FunctionTranslator(tree, false).doit();
    }

    class BlockExpressionTranslator extends ExpressionTranslator {

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
                            localDefs.size() == 0 ? tvalue : makeBlockExpression(localDefs, tvalue),
                            bindees(),
                            interClass(),
                            targetType);
                } else {
                    // make into block
                    if (value != null) {
                        translateStmt(value, targetType);
                    }
                    List<JCStatement> localDefs = prependToStatements.appendList(statements()).toList();
                    return new StatementsResult(localDefs.size() == 1 ? localDefs.head : m().Block(0L, localDefs));
                }
            } finally {
                prependToStatements = prevPrependToStatements;
            }
        }
    }

    public void visitBlockExpression(JFXBlock tree) {
        result = (new BlockExpressionTranslator(tree)).doit();
    }

    public void visitAssign(final JFXAssign tree) {
        if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
            TODO("visitAssign SEQUENCE_SLICE");
            // assignment of a sequence slice --  s[i..j]=8, call the sequence set method
            /***
            JFXSequenceSlice si = (JFXSequenceSlice)tree.lhs;
            JCExpression rhs = translateAsValue(tree.rhs, si.getSequence().type);
            JCExpression seq = translateAsSequenceVariable(si.getSequence());
            JCExpression startPos = translateAsValue(si.getFirstIndex(), syms.intType);
            JCExpression endPos = makeSliceEndPos(si);
            JCFieldAccess select = make.Select(seq, defs.replaceSliceMethodName);
            List<JCExpression> args = List.of(startPos, endPos, rhs);
            result = make.at(diagPos).Apply(null, select, args);
            **/
        } else {
            result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

                JCExpression buildRHS(JCExpression rhsTranslated) {
                    return rhsTranslated;
                }

                JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
                    return m().Assign(lhsTranslated, rhsTranslated);
                }
            }.doit();
        }
    }

    public void visitAssignop(final JFXAssignOp tree) {
        result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

            private boolean useDurationOperations() {
                return types.isSameType(lhs.type, syms.javafx_DurationType);
            }

            @Override
            JCExpression buildRHS(JCExpression rhsTranslated) {
                final JCExpression lhsTranslated = translateExpr(lhs, null);
                if (useDurationOperations()) {
                    JCExpression method = select(lhsTranslated, tree.operator.name);
                    return m().Apply(null, method, List.<JCExpression>of(rhsTranslated));
                } else {
                    JCExpression ret = makeBinary(getBinaryOp(), lhsTranslated, rhsTranslated);
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
                        return operationalType(useDurationOperations()? syms.javafx_NumberType : rhs.type);
                    default:
                        return operationalType(lhs.type);
                }
            }

            @Override
            JCExpression defaultFullExpression( JCExpression lhsTranslated, JCExpression rhsTranslated) {
                return useDurationOperations()?
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

    public void visitSelect(JFXSelect tree) {
        if (substitute(tree.sym)) {
            return;
        }
        result = new SelectTranslator(tree).doit();
    }

    public void visitIdent(JFXIdent tree) {
        if (substitute(tree.sym)) {
            return;
        }
        result = new IdentTranslator(tree).doit();
    }

    private class ExplicitSequenceTranslator extends ExpressionTranslator {

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

    //@Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        result = new ExplicitSequenceTranslator(
                tree.pos(),
                tree.getItems(),
                types.elementType(tree.type),
                tree.type)
             .doit();
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
                    runtime(diagPos, rm, args.toList()),
                    type);
        }
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        result = new SequenceRangeTranslator(tree).doit();
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

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = new SequenceEmptyTranslator(tree).doit();
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
            Name getMethodName = defs.getMethodName;
            if (seq instanceof JFXIdent) {
                JFXIdent var = (JFXIdent) seq;
                OnReplaceInfo info = findOnReplaceInfo(var.sym);
                if (info != null) {
                    String mname = getMethodName.toString();
                    ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                    args.append(make.TypeCast(makeType(info.arraySequenceType, true), make.Ident(defs.onReplaceArgNameBuffer)));
                    if (var.sym == info.oldValueSym) {
                        mname = mname + "FromOldValue";
                        args.append(make.TypeCast(makeType(info.seqWithExtendsType, true), make.Ident(defs.onReplaceArgNameOld)));
                        args.append(make.Ident(defs.onReplaceArgNameFirstIndex));
                        args.append(make.Ident(defs.onReplaceArgNameLastIndex));
                    } else { // var.sym == info.newElementsSym
                        mname = mname + "FromNewElements";
                        args.append(make.Ident(defs.onReplaceArgNameFirstIndex));
                        args.append(make.TypeCast(makeType(info.seqWithExtendsType, true), make.Ident(defs.onReplaceArgNameNewElements)));
                    }
                    args.append(tIndex);
                    return call(makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                            mname, args.toList());
                }
            }
            return call(tSeq, getMethodName, tIndex);
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

    public void visitSequenceIndexed(final JFXSequenceIndexed tree) {
        result = new SequenceIndexedTranslator(tree).doit();
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
                endPos = call(seq, defs.sizeMethodName);
                if (endKind == SequenceSliceTree.END_EXCLUSIVE) {
                    endPos = m().Binary(JCTree.MINUS, endPos, makeInt(1));
                }
            } else {
                endPos = translateExpr(lastIndex, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE) {
                    endPos = makeBinary(JCTree.PLUS, endPos, makeInt(1));
                }
            }
            return endPos;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), type);
        }

        protected JCExpression doitExpr() {
            JCExpression tFirstIndex = translateExpr(firstIndex, syms.intType);
            return call(seq, defs.getSliceMethodName, tFirstIndex, computeSliceEnd());
        }
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        result = new SequenceSliceTranslator(tree).doit();
    }

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        TODO(tree);
/*****
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seqLoc = translateAsSequenceVariable(tree.getSequence());
        JCExpression elem = translateAsUnconvertedValue( tree.getElement() );
        Type elemType = tree.getElement().type;
        if (types.isArray(elemType) || types.isSequence(elemType))
            elem = convertTranslated(elem, diagPos, elemType, tree.getSequence().type);
        else
            elem = convertTranslated(elem, diagPos, elemType, types.elementType(tree.getSequence().type));
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
        JCExpression receiver;
        if (elemType.isPrimitive()) {
            // In this case we call a static method.  Kind of ugly ...
            receiver = makeQualifiedTree(diagPos, JavafxDefs.locationPackageNameString+".SequenceVariable");
            args.append(seqLoc);
        }
        else
            receiver = seqLoc;
        args.append(elem);
        String method;
        if (tree.getPosition() == null) {
            method = "insert";
        } else {
            JCExpression position = translateAsValue(tree.getPosition(), syms.intType);
            if (tree.shouldInsertAfter())
                position = make.Binary(JCTree.PLUS, position, make.Literal(Integer.valueOf(1)));
            method = "insertBefore";
            args.append(position);
        }
        result = callStmt(diagPos, receiver, names.fromString(method), args.toList());
*****/
    }

    //@Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        TODO(tree);;
/*****
        JFXExpression seq = tree.getSequence();

        if (tree.getElement() != null) {
            JCExpression seqLoc = translateAsSequenceVariable(seq);
            result = callStmt(tree.pos(), seqLoc, "deleteValue", translateAsUnconvertedValue(tree.getElement())); //TODO: convert to seqence type
        } else {
            if (seq.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
                // deletion of a sequence element -- delete s[i]
                JFXSequenceIndexed si = (JFXSequenceIndexed) seq;
                JFXExpression seqseq = si.getSequence();
                JCExpression seqLoc = translateAsSequenceVariable(seqseq);
                JCExpression index = translateAsValue(si.getIndex(), syms.intType);
                result = callStmt(tree.pos(), seqLoc, "delete", index);
            } else if (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
                // deletion of a sequence slice --  delete s[i..j]=8
                JFXSequenceSlice slice = (JFXSequenceSlice) seq;
                JFXExpression seqseq = slice.getSequence();
                JCExpression seqLoc = translateAsSequenceVariable(seqseq);
                JCExpression first = translateAsValue(slice.getFirstIndex(), syms.intType);
                JCExpression end = makeSliceEndPos(slice);
                result = callStmt(tree.pos(), seqLoc, "deleteSlice", List.of(first, end));
            } else if (types.isSequence(seq.type)) {
                JCExpression seqLoc = translateAsSequenceVariable(seq);
                result = callStmt(tree.pos(), seqLoc, "deleteAll");
            } else {
                result = make.at(tree.pos()).Exec(
                            make.Assign(
                                translateAsUnconvertedValue(seq), //TODO: does this work?
                                make.Literal(TypeTags.BOT, null)));
            }
        }
 *****/
    }

    public void visitInvalidate(JFXInvalidate tree) {
        JCTree receiver = null;
        Symbol vsym = JavafxTreeInfo.symbol(tree.getVariable());
        if (tree.getVariable().getFXTag() == JavafxTag.SELECT) {
            JFXSelect sel = (JFXSelect)tree.getVariable();
            receiver = translateToExpression(sel.selected, sel.selected.type);
        }
        result = new StatementsResult(make.at(tree.pos()).Exec(call(tree.pos(), (JCExpression)receiver, attributeInvalidateName(vsym))));
    }

    /**** utility methods ******/

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
                    defs.mainName,
                    make.at(diagPos).TypeIdent(TypeTags.VOID),
                    List.<JCTypeParameter>nil(),
                    paramList,
                    makeThrows(diagPos),
                    body,
                    null);
    }

    public List<JCExpression> makeThrows(DiagnosticPosition diagPos) {
        return List.of(makeQualifiedTree(diagPos, methodThrowsString));
    }

    UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType, final int initLength) {
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
                    lb.append(makeTypeInfo(diagPos, elemType));
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
            super(diagPos);
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
                    int kind = typeMorpher.kindFromPrimitiveType(elemType.tsym);
                    localSeqBuilder = "com.sun.javafx.runtime.sequence." + JavafxDefs.getTypePrefix(kind) + "ArraySequence";
                }
                else
                    localSeqBuilder = sequenceBuilderString;
            }
            JCExpression builderTypeExpr = makeQualifiedTree(diagPos, localSeqBuilder);
            JCExpression builderClassExpr = makeQualifiedTree(diagPos, localSeqBuilder);
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
            return makeVar(0L, builderTypeExpr, sbName, newExpr);
        }

        JCIdent makeBuilderVarAccess() {
            return id(sbName);
        }

        abstract JCStatement addElement(JFXExpression expr);

        abstract List<JCExpression> makeConstructorArgs();

        JCStatement makeAdd(JCExpression expr) {
            return callStmt(makeBuilderVarAccess(), names.fromString("add"), expr);
        }

        JCExpression makeToSequence() {
            return call(makeBuilderVarAccess(), names.fromString(toSequenceString));
        }
    }

    //@Override
    public void visitBinary(final JFXBinary tree) {
        result = new BinaryOperationTranslator(tree.pos(), tree).doit();
    }

    public void visitBreak(JFXBreak tree) {
        result = new StatementsResult(make.at(tree.pos).Break(tree.label));
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
    
    public void visitForExpression(JFXForExpression tree) {
        result = (new ForExpressionTranslator(tree)).doit();
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
            this.inductionVar = makeMutableTmpVar("ind", indexedLoop ? syms.intType : inductionVarType, null);
        }

        private JCVariableDecl makeTmpVar(String root, JCExpression value) {
            return makeTmpVar(root, inductionVarType, value);
        }

        private JCVariableDecl makeVar(Name varName, JCExpression value) {
            return makeVar(inductionVarType, varName, value);
        }

        @Override
        protected JCVariableDecl makeTmpVar(long flags, String root, Type varType, JCExpression initialValue) {
            Name varName = names.fromString(var.name.toString() + "$" + root);
            return makeVar(flags, varType, varName, initialValue);
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
            return makeBinary(op, id(inductionVar), id(upperVar));
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
            if (! (seq instanceof JFXIdent) || findOnReplaceInfo(((JFXIdent) seq).sym) == null)
                tinits.append(seqVar);
            JCExpression init;
            boolean maxForStartNeeded = true;
            if (first == null)
                init = makeInt(0);
            else {
                init = translateExpr(first, syms.intType);
                if (first.getFXTag() == JavafxTag.LITERAL && ! isNegative(first))
                    maxForStartNeeded = false;
                // FIXME set maxForStartNeeded false if first is replace-trigger startPos and seq is oldValue
                if (maxForStartNeeded)
                    setDiagPos(first);
                    init = call(
                           makeQualifiedTree(first, "java.lang.Math"),
                           "max",
                           init, makeInt(0));
            }
            setDiagPos(clause);
            inductionVar.init = init;
            tinits.append(inductionVar);
            JCExpression sizeExpr = translateSizeof(diagPos, seq, id(seqVar));
            //call(diagPos, ident(seqVar), "size");
            JCExpression limitExpr;
            // Compare the logic in makeSliceEndPos.
            if (last == null) {
                limitExpr = sizeExpr;
                if (endKind == SequenceSliceTree.END_EXCLUSIVE)
                    limitExpr = makeBinary(JCTree.MINUS, limitExpr, makeInt(1));
            }
            else {
                limitExpr = translateExpr(last, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE)
                    limitExpr = makeBinary(JCTree.PLUS, limitExpr, makeInt(1));
                // FIXME can optimize if last is replace-trigger endPos and seq is oldValue
                if (true)
                    setDiagPos(last);
                    limitExpr = call(
                           makeQualifiedTree(last, "java.lang.Math"), "min",
                           List.of(limitExpr, sizeExpr));
            }
            setDiagPos(clause);
            JCVariableDecl limitVar = makeTmpVar("limit", syms.intType, limitExpr);
            tinits.append(limitVar);
            // The condition that will be tested each time through the loop
            JCExpression tcond = makeBinary(JCTree.LT, id(inductionVar), id(limitVar));
            // Generate the step statement as: x += 1
            List<JCExpressionStatement> tstep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(inductionVar), m().Literal(TypeTags.INT, 1))));
            tinits.append(m().ForLoop(List.<JCStatement>nil(), tcond, tstep, body));
            body = m().Block(0L, tinits.toList());
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
            JCVariableDecl upperVar = makeTmpVar("upper", translateExpr(range.getUpper(), inductionVarType));
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
                    JCVariableDecl stepVar = makeTmpVar("step", stepVal);
                    tinits.append(stepVar);
                    tstepIncrExpr = id(stepVar);
                    JCVariableDecl negativeVar = makeTmpVar("negative", syms.booleanType, makeBinary(JCTree.LT, id(stepVar), m().Literal(inductionVarType.tag, 0)));
                    tinits.append(negativeVar);
                    tcond = m().Conditional(id(negativeVar), condTest(range, true, upperVar), condTest(range, false, upperVar));
                }
            } else {
                // No step expression, use one as the increment
                tstepIncrExpr = m().Literal(inductionVarType.tag, 1);
                tcond = condTest(range, false, upperVar);
            }
            // Generate the step statement as: x += x$step
            List<JCExpressionStatement> tstep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(inductionVar), tstepIncrExpr)));
            // Finally, build the for loop
            body = m().ForLoop(tinits.toList(), tcond, tstep, body);
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
                body = m().If(translateExpr(clause.getWhereExpression(), syms.booleanType), body, null);
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
                    incrementingIndexVar = makeMutableTmpVar("incrindex", syms.javafx_IntegerType, makeInt(0));
                    JCVariableDecl finalIndexVar = makeVar(

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
                    seqVar = makeTmpVar("seq", seq.type, translateExpr(sseq, seq.type));
                    varInit = new SequenceIndexedTranslator(diagPos, sseq, id(seqVar), id(inductionVar), inductionVarType).doitExpr();
                } else {
                    varInit = id(inductionVar);
                }

                stmts.append(makeVar(var.getName(), varInit));
                stmts.append(body);
                body = m().Block(0L, stmts.toList());
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
                JCExpression tseq = translateExpr(seq, null); //FIXME
                if (types.isSequence(seq.type)) {
                    // Iterating over a non-range sequence, use a foreach loop, but first convert null to an empty sequence
                    tseq = runtime(diagPos,
                            defs.Sequences_forceNonNull,
                            List.of(makeTypeInfo(diagPos, inductionVarType), tseq));
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
                        body = m().If(
                                makeNotNullCheck(id(inductionVar)),
                                body,
                                null);
                    }
                    // the "induction" variable will have only one value, set it to that
                    inductionVar.init = tseq;
                    // wrap the induction variable and the body in a block to protect scope
                    body = m().Block(0L, List.of(inductionVar, body));
                }
            }

            if (clause.getIndexUsed()) {
                // indexof is used, define the index counter variable at the top of everything
                body = m().Block(0L, List.of(incrementingIndexVar, body));
            }

            return body;
        }
    }

    public void visitIndexof(JFXIndexof tree) {
        final DiagnosticPosition diagPos = tree.pos();
        assert tree.clause.getIndexUsed() : "assert that index used is set correctly";
        result = new ExpressionResult(
                make.at(diagPos).Ident(indexVarName(tree.fname)),
                tree.type);
    }


    /**
     * Translate if-expression
     */
    private class IfTranslator extends ExpressionTranslator {

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
                    m().Conditional(
                        cond,
                        sideExpr(trueSide),
                        sideExpr(falseSide)),
                    targetType);
            } else {
                 return toStatementResult(m().If(
                        cond,
                        sideStmt(trueSide),
                        sideStmt(falseSide)));
            }
        }
    }

    public void visitIfExpression(JFXIfExpression tree) {
        result = new IfTranslator(tree).doit();
    }

    public void visitContinue(JFXContinue tree) {
        result = new StatementsResult(make.at(tree.pos).Continue(tree.label));
    }

    public void visitReturn(JFXReturn tree) {
        JFXExpression exp = tree.getExpression();
        if (exp == null) {
            result = new StatementsResult(make.at(tree).Return(null));
        } else {
            result = translateToStatementsResult(exp, tree.returnType);
        }
    }

    public void visitParens(JFXParens tree) {
        if (yield() == ToExpression) {
            result = translateToExpressionResult(tree.expr, targetType);
        } else {
            result = translateToStatementsResult(tree.expr, targetType);
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
                tExpr = call(
                        makeQualifiedTree(expr, "com.sun.javafx.runtime.sequence.Sequences"),
                        "getSingleValue", tExpr);
            }
            JCTree clazz = makeType(classType);
            return toResult(
                    m().TypeTest(tExpr, clazz),
                    syms.booleanType);
        }
    }

    public void visitInstanceOf(JFXInstanceOf tree) {
        result = new InstanceOfTranslator(tree).doit();
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

    public void visitTypeCast(final JFXTypeCast tree) {
        result = new TypeCastTranslator(tree).doit();
    }

    public void visitLiteral(JFXLiteral tree) {
        result = new ExpressionResult(
                translateLiteral(tree),
                tree.type);
    }

    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        result = new FunctionCallTranslator(tree).doit();
    }

    public void visitSkip(JFXSkip tree) {
        result = new StatementsResult(make.at(tree.pos).Skip());
    }

    public void visitThrow(JFXThrow tree) {
        JCTree expr = translateToExpression(tree.expr, tree.type);
        result = new StatementsResult(make.at(tree.pos).Throw(expr));
    }

    class TryTranslator extends ExpressionTranslator {

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

    public void visitTry(JFXTry tree) {
        result = new TryTranslator(tree).doit();
    }

    public void visitUnary(final JFXUnary tree) {
        result = new UnaryOperationTranslator(tree).doit();
    }

    JCExpression translateSizeof(DiagnosticPosition diagPos, JFXExpression expr, JCExpression transExpr) {
        if (expr instanceof JFXIdent) {
            JFXIdent var = (JFXIdent) expr;
            OnReplaceInfo info = findOnReplaceInfo(var.sym);
            if (info != null) {
                String mname;
                ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                args.append(make.Ident(defs.onReplaceArgNameBuffer));
                if (var.sym == info.oldValueSym) {
                    mname = "sizeOfOldValue";
                    args.append(make.Ident(defs.onReplaceArgNameOld));
                    args.append(make.Ident(defs.onReplaceArgNameLastIndex));
                }
                else { // var.sym == info.newElementsSym
                    mname = "sizeOfNewElements";
                    args.append(make.Ident(defs.onReplaceArgNameFirstIndex));
                    args.append(make.Ident(defs.onReplaceArgNameNewElements));
                }
                return call(diagPos, makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                        mname, args.toList());
            }
        }
        return runtime(diagPos, defs.Sequences_size, List.of(transExpr));
    }

    class WhileTranslator extends ExpressionTranslator {

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

    public void visitWhileLoop(JFXWhileLoop tree) {
        result = new WhileTranslator(tree).doit();
    }

    public void visitTimeLiteral(final JFXTimeLiteral tree) {
        result = new TimeLiteralTranslator(tree).doit();
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

    class InterpolateValueTranslator extends NewBuiltInInstanceTranslator {

        final JFXInterpolateValue tree;

        InterpolateValueTranslator(JFXInterpolateValue tree) {
            super(tree.pos(), syms.javafx_KeyValueType);
            this.tree = tree;
        }

        protected JCExpression translateTarget() {
            JCExpression target = translateExpr(tree.attribute, null); //FIXME
            return call(makeType(syms.javafx_PointerType), "make", target);
        }

        protected void initInstanceVariables(Name instName) {
            // value
            setInstanceVariable(instName, defs.valueName, tree.value);

            // interpolator kind
            if (tree.interpolation != null) {
                setInstanceVariable(instName, defs.interpolateName, tree.interpolation);
            }

            // target -- convert to Pointer
            setInstanceVariable(tree.attribute.pos(), instName, JavafxBindStatus.UNBOUND, varSym(defs.targetName), translateTarget());
        }
    }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        result = new InterpolateValueTranslator(tree).doit();
    }

    public void visitKeyFrameLiteral(final JFXKeyFrameLiteral tree) {
        result = new NewBuiltInInstanceTranslator(tree.pos(), syms.javafx_KeyFrameType) {

            protected void initInstanceVariables(Name instName) {
                // start time
                setInstanceVariable(instName, defs.timeName, tree.start);

                // key values -- as sequence
                JCExpression values = asExpression(
                        new ExplicitSequenceTranslator(
                           tree.pos(),
                           tree.getInterpolationValues(),
                           syms.javafx_KeyValueType,
                           types.sequenceType(syms.javafx_KeyValueType)
                        ).doit(),
                        null //FIXME
                );
                setInstanceVariable(tree.pos(), instName, JavafxBindStatus.UNBOUND, varSym(defs.valuesName), values);
            }
        }.doit();
    }

    /***********************************************************************
     *
     * Utilities
     *
     */

    protected String getSyntheticPrefix() {
        return "jfx$";
    }

    JCBlock translatedOnReplaceBody(JFXOnReplace onr) {
        return (onr == null)?  null : translateToBlock(onr.getBody(), syms.voidType);
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

}
