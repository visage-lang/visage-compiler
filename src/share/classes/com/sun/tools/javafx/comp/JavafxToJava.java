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
     * static information
     */
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.ObjectArraySequence";
    private static final String toSequenceString = "toSequence";

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
        translateInvBind = JavafxTranslateInvBind.instance(context);
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
                                attrDef.isBound() && (initializer instanceof JFXIdent) &&
                                isBoundFunctionResult(((JFXIdent)initializer).sym);
                            TranslatedVarInfo ai = new TranslatedVarInfo(
                                    attrDef,
                                    vmi,
                                    translateVarInit(attrDef),
                                    initWithBoundFuncResult,
                                    attrDef.isBound() ? translateBind.translateBoundExpression(initializer, attrDef.type, attrDef.sym, attrDef.isBidiBind()) : null,
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
                                    override.isBound() ? translateBind.translateBoundExpression(override.getInitializer(), override.type, override.sym, override.isBidiBind()) : null,
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
                            inInstanceContext = ReceiverContext.Oops;
                            translatedDefs.appendList(translateToStatementsResult((JFXClassDeclaration)def, syms.voidType).trees());
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
        
                    cInterface.sym = makeClassSymbol(mods.flags, cInterface.name, tree.sym.owner);
                    
                    membersToSymbol(cInterface);
                    
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

    public void visitInstanciate(JFXInstanciate tree) {
        result = new InstanciateTranslator(tree).doit();
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

    public void visitVarInit(JFXVarInit tree) {
        final VarSymbol vsym = tree.getSymbol();

        result = new ExpressionTranslator(tree.pos()) {
            ExpressionResult doit() {
                JCExpression receiver = vsym.isStatic() ? Call(scriptLevelAccessMethod(vsym.owner)) : null;
                JCStatement applyDefaultCall = CallStmt(receiver, defs.applyDefaults_FXObjectMethodName, Offset(vsym));
                return toResult(
                        BlockExpression(
                            applyDefaultCall,
                            id(attributeValueName(vsym))),
                        vsym.type);
        }}.doit();
    }

    private class VarTranslator extends ExpressionTranslator {

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
                JCStatement var = Var(modFlags, tree.type, tree.name, init);
                prependToStatements.append(var);
                return new StatementsResult(diagPos, List.<JCStatement>nil());
            }
            init = JavafxToJava.this.makeDefaultValue(diagPos, vmi);
            prependToStatements.prepend(Var(modFlags, tree.type, tree.name, init));

            return translateDefinitionalAssignmentToSetExpression(diagPos, tree.getInitializer(), vmi, null);
        }
    }

    public void visitVar(JFXVar tree) {
        result = new VarTranslator(tree).doit();
    }

    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = new FunctionValueTranslator(make.Ident(defs.lambda_MethodName), def, tree.pos(), (MethodType) def.type, tree.type).doit();
    }

   boolean isInnerFunction (MethodSymbol sym) {
       return sym.owner != null && sym.owner.kind != Kinds.TYP
                && (sym.flags() & Flags.SYNTHETIC) == 0;
   }

    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        result = new FunctionTranslator(tree, false).doit();
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
                            localDefs.size() == 0 ? tvalue : BlockExpression(localDefs, tvalue),
                            invalidators(),
                            interClass(),
                            targetType);
                } else {
                    // make into block
                    if (value != null) {
                        if (value.getFXTag() == JavafxTag.VAR_SCRIPT_INIT && targetType != syms.voidType) {
                            translateStmt(value, syms.voidType);
                            addPreface(Stmt(id(attributeValueName(((JFXVarInit) value).getSymbol())), targetType));
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

    public void visitBlockExpression(JFXBlock tree) {
        result = (new BlockExpressionTranslator(tree)).doit();
    }

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

    public void visitAssignop(final JFXAssignOp tree) {
        result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

            private boolean useDurationOperations() {
                return types.isSameType(ref.type, syms.javafx_DurationType);
            }

            @Override
            JCExpression buildRHS(JCExpression rhsTranslated) {
                final JCExpression lhsTranslated = translateExpr(ref, null);
                if (useDurationOperations()) {
                    return Call(
                            lhsTranslated,
                            tree.operator.name,
                            rhsTranslated);
                } else {
                    JCExpression ret = m().Binary(getBinaryOp(), lhsTranslated, rhsTranslated);
                    if (!types.isSameType(rhsType(), ref.type)) {
                        // Because the RHS is a different type than the LHS, a cast may be needed
                        ret = m().TypeCast(ref.type, ret);
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
                        return operationalType(ref.type);
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

    private class SequenceRangeTranslator extends ExpressionTranslator {

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
                    Call(rm, args),
                    type);
        }
    }

    public void visitSequenceRange(JFXSequenceRange tree) {
        result = new SequenceRangeTranslator(tree).doit();
    }

    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        result = new SequenceEmptyTranslator(tree).doit();
    }

    private class SequenceIndexedTranslator extends ExpressionTranslator {

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
            int typeKind = types.typeKind(resultType);
            if (seq instanceof JFXIdent) {
                JFXIdent var = (JFXIdent) seq;
                OnReplaceInfo info = findOnReplaceInfo(var.sym);
                if (info != null
                        && (var.sym.flags_field & JavafxFlags.VARUSE_OPT_TRIGGER) != 0) {
                    JFXOnReplace onReplace = info.onReplace;
                    ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                    Symbol sym = info.vmi.getSymbol();
                    args.append(getReceiverOrThis((VarSymbol) sym));
                    args.append(Offset(sym));
                    args.append(make.Ident(paramStartPosName(onReplace)));
                    args.append(make.Ident(paramNewElementsLengthName(onReplace)));
                    args.append(tIndex);
                    return Call(defs.Sequences_getAsFromNewElements[typeKind], args);
                }
            }
            Name getMethodName = defs.typedGet_SequenceMethodName[typeKind];
            return Call(tSeq, getMethodName, tIndex);
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

    private class SequenceSliceTranslator extends ExpressionTranslator {

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
                endPos = Call(seq, defs.size_SequenceMethodName);
                if (endKind == SequenceSliceTree.END_EXCLUSIVE) {
                    endPos = MINUS(endPos, Int(1));
                }
            } else {
                endPos = translateExpr(lastIndex, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE) {
                    endPos = PLUS(endPos, Int(1));
                }
            }
            return endPos;
        }

        protected ExpressionResult doit() {
            return toResult(doitExpr(), type);
        }

        protected JCExpression doitExpr() {
            JCExpression tFirstIndex = translateExpr(firstIndex, syms.intType);
            return Call(seq, defs.getSlice_SequenceMethodName, tFirstIndex, computeSliceEnd());
        }
    }

    public void visitSequenceSlice(JFXSequenceSlice tree) {
        result = new SequenceSliceTranslator(tree).doit();
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

    public void visitSequenceInsert(JFXSequenceInsert tree) {
        result = new SequenceInsertTranslator(tree).doit();
    }

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

    public void visitInvalidate(final JFXInvalidate tree) {
        result = (new ExpressionTranslator(tree.pos()) {
            protected AbstractStatementsResult doit() {
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                JCExpression receiver0 = null;
                JCExpression receiver1 = null;
                
                Symbol vsym = JavafxTreeInfo.symbol(tree.getVariable());
                if (tree.getVariable().getFXTag() == JavafxTag.SELECT) {
                    JFXSelect sel = (JFXSelect)tree.getVariable();
                    JCExpression receiver = translateToExpression(sel.selected, sel.selected.type);
                    if (receiver != null) {
                        JCVariableDecl invalVar = TmpVar("inval", sel.selected.type, receiver);
                        stmts.append(invalVar);
                        receiver0 = id(invalVar.name);
                        receiver1 = id(invalVar.name);
                    }
                }
                
                stmts.append(CallStmt(receiver0, attributeInvalidateName(vsym), id(defs.varFlagIS_INVALID)));
                stmts.append(CallStmt(receiver1, attributeInvalidateName(vsym), id(defs.varFlagNEEDS_TRIGGER)));
                return toStatementResult(Block(stmts));
            }
        }).doit();
    }

    /**** utility methods ******/

    private UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType, final int initLength) {
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
                    lb.append(TypeInfo(diagPos, elemType));
                return lb.toList();
            }

            @Override
            JCExpression makeToSequence() {
                return makeBuilderVarAccess();
            }
        };
    }

    private UseSequenceBuilder useSequenceBuilder(DiagnosticPosition diagPos, Type elemType) {
        return useSequenceBuilder(diagPos, elemType, -1);
    }

    private abstract class UseSequenceBuilder extends JavaTreeBuilder {
        final Type elemType;
        private final String seqBuilder;
        boolean addTypeInfoArg = true;

        // Sequence builder temp var name "sb"
        private final Name sbName = getSyntheticName("sb");

        private UseSequenceBuilder(DiagnosticPosition diagPos, Type elemType, String seqBuilder) {
            super(diagPos, currentClass(), receiverContext() == ReceiverContext.ScriptAsStatic);
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
                    int kind = types.typeKind(elemType);
                    localSeqBuilder = "com.sun.javafx.runtime.sequence." + JavafxDefs.getTypePrefix(kind) + "ArraySequence";
                }
                else
                    localSeqBuilder = sequenceBuilderString;
            }
            JCExpression builderTypeExpr = QualifiedTree(localSeqBuilder);
            JCExpression builderClassExpr = QualifiedTree(localSeqBuilder);
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
            return Var(0L, builderTypeExpr, sbName, newExpr);
        }

        JCIdent makeBuilderVarAccess() {
            return id(sbName);
        }

        abstract JCStatement addElement(JFXExpression expr);

        abstract List<JCExpression> makeConstructorArgs();

        JCStatement makeAdd(JCExpression expr) {
            return CallStmt(makeBuilderVarAccess(), names.fromString("add"), expr);
        }

        abstract JCExpression makeToSequence();
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
            this.inductionVar = MutableTmpVar("ind", indexedLoop ? syms.intType : inductionVarType, null);
        }

        private JCVariableDecl TmpVar(String root, JCExpression value) {
            return TmpVar(root, inductionVarType, value);
        }

        private JCVariableDecl Var(Name varName, JCExpression value) {
            return Var(inductionVarType, varName, value);
        }

        @Override
        protected JCVariableDecl TmpVar(long flags, String root, Type varType, JCExpression initialValue) {
            Name varName = names.fromString(var.name.toString() + "$" + root);
            return Var(flags, varType, varName, initialValue);
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
            return m().Binary(op, id(inductionVar), id(upperVar));
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
            boolean needSeqVar;
            if (! (seq instanceof JFXIdent))
                needSeqVar = true;
            else  {
                Symbol seqsym = ((JFXIdent) seq).sym;
                OnReplaceInfo info = findOnReplaceInfo(seqsym);
                needSeqVar = info == null ||
                        (seqsym.flags_field & JavafxFlags.VARUSE_OPT_TRIGGER) == 0;
            }
            if (needSeqVar)
                tinits.append(seqVar);
            JCExpression init;
            boolean maxForStartNeeded = true;
            if (first == null)
                init = Int(0);
            else {
                init = translateExpr(first, syms.intType);
                if (first.getFXTag() == JavafxTag.LITERAL && ! isNegative(first))
                    maxForStartNeeded = false;
                // FIXME set maxForStartNeeded false if first is replace-trigger startPos and seq is oldValue
                if (maxForStartNeeded)
                    setDiagPos(first);
                    init = Call(defs.Math_max, init, Int(0));
            }
            setDiagPos(clause);
            inductionVar.init = init;
            tinits.append(inductionVar);
            JCExpression sizeExpr = translateSizeof(seq, id(seqVar));
            //call(diagPos, ident(seqVar), "size");
            JCExpression limitExpr;
            // Compare the logic in makeSliceEndPos.
            if (last == null) {
                limitExpr = sizeExpr;
                if (endKind == SequenceSliceTree.END_EXCLUSIVE)
                    limitExpr = MINUS(limitExpr, Int(1));
            }
            else {
                limitExpr = translateExpr(last, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE)
                    limitExpr = PLUS(limitExpr, Int(1));
                // FIXME can optimize if last is replace-trigger endPos and seq is oldValue
                if (true)
                    setDiagPos(last);
                    limitExpr = Call(defs.Math_min, limitExpr, sizeExpr);
            }
            setDiagPos(clause);
            JCVariableDecl limitVar = TmpVar("limit", syms.intType, limitExpr);
            tinits.append(limitVar);
            // The condition that will be tested each time through the loop
            JCExpression tcond = LT(id(inductionVar), id(limitVar));
            // Generate the step statement as: x += 1
            List<JCExpressionStatement> tstep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(inductionVar), m().Literal(TypeTags.INT, 1))));
            tinits.append(m().ForLoop(List.<JCStatement>nil(), tcond, tstep, body));
            body = Block(tinits);
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
            JCVariableDecl upperVar = TmpVar("upper", translateExpr(range.getUpper(), inductionVarType));
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
                    JCVariableDecl stepVar = TmpVar("step", stepVal);
                    tinits.append(stepVar);
                    tstepIncrExpr = id(stepVar);
                    JCVariableDecl negativeVar = TmpVar("negative", syms.booleanType, LT(id(stepVar), m().Literal(inductionVarType.tag, 0)));
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
                body = If(translateExpr(clause.getWhereExpression(), syms.booleanType), body);
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
                    incrementingIndexVar = MutableTmpVar("incrindex", syms.javafx_IntegerType, Int(0));
                    JCVariableDecl finalIndexVar = Var(

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
                    seqVar = TmpVar("seq", seq.type, translateExpr(sseq, seq.type));
                    varInit = new SequenceIndexedTranslator(diagPos, sseq, id(seqVar), id(inductionVar), inductionVarType).doitExpr();
                } else {
                    varInit = id(inductionVar);
                }

                stmts.append(Var(var.getName(), varInit));
                stmts.append(body);
                body = Block(stmts);
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
                JCExpression tseq = asExpression(translateToExpressionResult(seq, null), null);
                if (types.isSequence(seq.type)) {
                    // Iterating over a non-range sequence, use a foreach loop, but first convert null to an empty sequence
                    tseq = Call(defs.Sequences_forceNonNull,
                            TypeInfo(diagPos, inductionVarType), tseq);
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
                        body = If (NEnull(id(inductionVar)),
                                body);
                    }
                    // the "induction" variable will have only one value, set it to that
                    inductionVar.init = tseq;
                    // wrap the induction variable and the body in a block to protect scope
                    body = Block(inductionVar, body);
                }
            }

            if (clause.getIndexUsed()) {
                // indexof is used, define the index counter variable at the top of everything
                body = Block(incrementingIndexVar, body);
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
                 return toStatementResult(If(
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

    public void visitInstanceOf(JFXInstanceOf tree) {
        result = new InstanceOfTranslator(tree).doit();
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

    public void visitTry(JFXTry tree) {
        result = new TryTranslator(tree).doit();
    }

    public void visitUnary(final JFXUnary tree) {
        result = new UnaryOperationTranslator(tree).doit();
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

    private class InterpolateValueTranslator extends NewBuiltInInstanceTranslator {

        final JFXInterpolateValue tree;

        InterpolateValueTranslator(JFXInterpolateValue tree) {
            super(tree.pos(), syms.javafx_KeyValueType);
            this.tree = tree;
        }

        protected JCExpression translateTarget() {
            JavafxTag tag = tree.attribute.getFXTag();
            Symbol sym = JavafxTreeInfo.symbol(tree.attribute);
            JCExpression receiver;
            if (tag == JavafxTag.IDENT) {
                if (sym.isStatic()) {
                    receiver = Call(staticReference(sym), scriptLevelAccessMethod(sym.owner));
                } else {
                    receiver = makeReceiver(sym, false);
                }
            } else if (tag == JavafxTag.SELECT) {
                receiver = translateExpr(((JFXSelect)tree.attribute).selected, null);
            } else {
                // FIXME: JavafxAttr enforces "attribute" of JFXInterpolateValue
                // to be either a select or an identifier. Do I need TODO here?
                TODO("JFXInterpolateValue.attribute should be either a select or an identifier");
                // should not reach here. TODO always throws exception.
                // This is just to satisfy the compiler for calls below.
                receiver = null;
            }
            
            JCExpression varOffsetExpr = Offset(receiver, sym);
            Type type = types.erasure(tree.attribute.type);
            JCExpression varType = m().ClassLiteral(type);
            return Call(defs.Pointer_make, receiver, varOffsetExpr, varType);
        }

        @Override
        protected boolean hasInstanceVariableInits() {
            return true;
        }

        @Override
        protected void initInstanceVariables(Name instName) {
            // value
            setInstanceVariable(instName, defs.value_InterpolateMethodName, tree.value);

            // interpolator kind
            if (tree.interpolation != null) {
                setInstanceVariable(instName, defs.interpolate_InterpolateMethodName, tree.interpolation);
            }

            // target -- convert to Pointer
            setInstanceVariable(tree.attribute.pos(), instName, JavafxBindStatus.UNBOUND, varSym(defs.target_InterpolateMethodName), translateTarget());
        }
    }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        result = new InterpolateValueTranslator(tree).doit();
    }

    public void visitKeyFrameLiteral(final JFXKeyFrameLiteral tree) {
        result = new NewBuiltInInstanceTranslator(tree.pos(), syms.javafx_KeyFrameType) {

            @Override
            protected boolean hasInstanceVariableInits() {
                return true;
            }

            @Override
            protected void initInstanceVariables(Name instName) {
                // start time
                setInstanceVariable(instName, defs.time_KeyFrameMethodName, tree.start);

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
                setInstanceVariable(tree.pos(), instName, JavafxBindStatus.UNBOUND, varSym(defs.values_KeyFrameMethodName), values);
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
