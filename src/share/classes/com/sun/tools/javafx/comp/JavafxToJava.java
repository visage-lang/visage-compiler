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
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.JCAnnotation;
import com.sun.tools.mjavac.tree.JCTree.JCBlock;
import com.sun.tools.mjavac.tree.JCTree.JCCase;
import com.sun.tools.mjavac.tree.JCTree.JCCatch;
import com.sun.tools.mjavac.tree.JCTree.JCClassDecl;
import com.sun.tools.mjavac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.mjavac.tree.JCTree.JCExpression;
import com.sun.tools.mjavac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.mjavac.tree.JCTree.JCFieldAccess;
import com.sun.tools.mjavac.tree.JCTree.JCIdent;
import com.sun.tools.mjavac.tree.JCTree.JCMethodDecl;
import com.sun.tools.mjavac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.mjavac.tree.JCTree.JCModifiers;
import com.sun.tools.mjavac.tree.JCTree.JCReturn;
import com.sun.tools.mjavac.tree.JCTree.JCStatement;
import com.sun.tools.mjavac.tree.JCTree.JCTypeParameter;
import com.sun.tools.mjavac.tree.JCTree.JCVariableDecl;
import com.sun.tools.mjavac.tree.TreeTranslator;
import com.sun.tools.mjavac.util.Context;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.Position;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import static com.sun.tools.javafx.code.JavafxFlags.*;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.Translator;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.comp.JavafxToJava.Yield.*;

/**
 * Translate JavaFX ASTs into Java ASTs
 *
 * @author Robert Field
 * @author Per Bothner
 * @author Lubo Litchev
 */
public class JavafxToJava extends JavafxAbstractTranslation<JCTree> {
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

    private Map<Symbol, Name> substitutionMap = new HashMap<Symbol, Name>();

    enum Yield {
        ToExpression,
        ToStatement
    }

    private static class TranslationState {
        final Yield yield;
        final Type targetType;
        TranslationState(Yield yield, Type targetType) {
            this.yield = yield;
            this.targetType = targetType;
        }
    }

    private JavafxEnv<JavafxAttrContext> attrEnv;

    // Stack used to track literal symbols for the current class.
    LiteralInitClassMap literalInitClassMap = null;

    private TranslationState translationState = null; // should be set before use

    boolean inOverrideInstanceVariableDefinition = false;

    /*
     * static information
     */
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.ObjectArraySequence";
    private static final String noMainExceptionString = "com.sun.javafx.runtime.NoMainException";
    private static final String toSequenceString = "toSequence";
    private static final String methodThrowsString = "java.lang.Throwable";
    JFXClassDeclaration currentClass;  //TODO: this is redundant with attrEnv.enclClass

    /** Class symbols for classes that need a reference to the outer class. */
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();

    JCExpression TODO() {
        throw new RuntimeException("Not yet implemented");
    }

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

    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    private <TFX extends JFXTree, TC extends JCTree> TC translateGeneric(TFX tree) {
        if (tree == null) {
            return null;
        } else {
            TC ret;
            JFXTree prevWhere = getAttrEnv().where;
            attrEnv.where = tree;
            tree.accept(this);
            attrEnv.where = prevWhere;
            ret = (TC) this.result;
            this.result = null;
            return ret;
        }
    }

    private JCMethodDecl translate(JFXFunctionDefinition tree) {
        return translateGeneric(tree);
    }

    private JCVariableDecl translate(JFXVar tree) {
        return translateGeneric(tree);
    }

    private JCCompilationUnit translate(JFXScript tree) {
        return translateGeneric(tree);
    }

    private JCStatement translateClassDef(JFXClassDeclaration tree) {
        return translateGeneric(tree);
    }

    /** Visitor method: translate a list of nodes.
     */
    private <TFX extends JFXTree, TC extends JCTree> List<TC> translateGeneric(List<TFX> trees) {
        ListBuffer<TC> translated = ListBuffer.lb();
        if (trees == null) {
            return null;
        }
        for (List<TFX> l = trees; l.nonEmpty(); l = l.tail) {
            TC tree = this.<TFX, TC>translateGeneric(l.head);
            if (tree != null) {
                translated.append(tree);
            }
        }
        return translated.toList();
    }

    public List<JCExpression> translateExpressions(List<JFXExpression> trees) {
        return translateGeneric(trees);
    }

    private List<JCCatch> translateCatchers(List<JFXCatch> trees) {
        return translateGeneric(trees);
    }

    private JCBlock translateBlockExpressionToBlock(JFXBlock bexpr) {
        JCStatement stmt = translateToStatement(bexpr);
        return stmt==null? null : asBlock(stmt);
    }

    JCExpression translateToExpression(JFXExpression expr, Type targetType) {
        if (expr == null) {
            return null;
        } else {
            JFXTree prevWhere = getAttrEnv().where;
            attrEnv.where = expr;
            TranslationState prevZ = translationState;
            translationState = new TranslationState(ToExpression, targetType);
            expr.accept(this);
            translationState = prevZ;
            attrEnv.where = prevWhere;
            JCExpression ret = (JCExpression)this.result;
            this.result = null;
            return (targetType==null)? ret : convertTranslated(ret, expr.pos(), expr.type, targetType);
        }
    }

    JCExpression translateAsValue(JFXExpression expr, Type targetType) {
        return translateToExpression(expr, targetType);
    }

    JCExpression translateAsUnconvertedValue(JFXExpression expr) {
        return translateToExpression(expr, null);
    }

    JCExpression translateAsSequenceVariable(JFXExpression expr) {
        return translateToExpression(expr, null);
    }

    private JCStatement translateToStatement(JFXExpression expr, Type targetType) {
        if (expr == null) {
            return null;
        } else {
            JFXTree prevWhere = getAttrEnv().where;
            attrEnv.where = expr;
            TranslationState prevZ = translationState;
            translationState = new TranslationState(ToStatement, targetType);
            expr.accept(this);
            translationState = prevZ;
            attrEnv.where = prevWhere;
            JCTree ret = this.result;
            this.result = null;
            if (ret instanceof JCStatement) {
                return (JCStatement) ret; // already converted
            }
            JCExpression translated = (JCExpression) ret;
            DiagnosticPosition diagPos = expr.pos();
            if (targetType == syms.voidType) {
                return make.at(diagPos).Exec(translated);
            } else {
                JFXVar var = null;
                if (expr instanceof JFXVar) {
                    var = (JFXVar)expr;
                } else if (expr instanceof JFXVarScriptInit) {
                    var = ((JFXVarScriptInit)expr).getVar();
                }

                if ((var != null) && var.isBound()) {
                    translated = TODO(); // getLocationValue(diagPos, translated, TYPE_KIND_OBJECT);
                }
                return make.at(diagPos).Return(convertTranslated(translated, diagPos, expr.type, targetType));
            }
        }
    }

    private JCStatement translateToStatement(JFXExpression expr) {
        return translateToStatement(expr, syms.voidType);
    }

    /**
     * For special cases where the expression may not be fully attributed.
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

    private boolean substitute(Symbol sym) {
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

    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.setAttrEnv(attrEnv);

        attrEnv.translatedToplevel = translate(attrEnv.toplevel);
        attrEnv.translatedToplevel.endPositions = attrEnv.toplevel.endPositions;
    }

    static class OnReplaceInfo {
        public OnReplaceInfo outer;
        JFXOnReplace onReplace;
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
    private JCStatement translateOnReplaceAsInline(VarSymbol vsym, JFXOnReplace onReplace) {
        if (onReplace == null) return null;
        return translateToStatement(onReplace.getBody());
    }

    void scriptBegin() {
    }

    /**
     * Add per-script BindingExpression/ChangeListener class, is needed
     * @param diagPos
     * @return
     */
    List<JCTree> scriptComplete(DiagnosticPosition diagPos) {
        return List.nil();
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
        //System.err.println("<translated src="+tree.sourcefile+">"); System.err.println(translated); System.err.println("</translated>");
        translated.docComments = null;
        translated.lineMap = tree.lineMap;
        translated.flags = tree.flags;
        result = translated;
    }

    //@Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXClassDeclaration prevClass = currentClass;
        JFXClassDeclaration prevEnclClass = getAttrEnv().enclClass;
        currentClass = tree;

        if (tree.isScriptClass()) {
            scriptBegin();
        }

        try {
            DiagnosticPosition diagPos = tree.pos();
            if (tree.isScriptClass()) literalInitClassMap = new LiteralInitClassMap();

            attrEnv.enclClass = tree;

            ListBuffer<JCStatement> translatedInitBlocks = ListBuffer.lb();
            ListBuffer<JCStatement> translatedPostInitBlocks = ListBuffer.lb();
            ListBuffer<JCTree> translatedDefs = ListBuffer.lb();
            ListBuffer<TranslatedVarInfo> attrInfo = ListBuffer.lb();
            ListBuffer<TranslatedOverrideClassVarInfo> overrideInfo = ListBuffer.lb();
            boolean isMixinClass = tree.isMixinClass();

           // translate all the definitions that make up the class.
           // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JFXTree def : tree.getMembers()) {
                    switch(def.getFXTag()) {
                        case INIT_DEF: {
                            inInstanceContext = isMixinClass? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            translateAndAppendStaticBlock(((JFXInitDefinition) def).getBody(), translatedInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case POSTINIT_DEF: {
                            inInstanceContext = isMixinClass? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            translateAndAppendStaticBlock(((JFXPostInitDefinition) def).getBody(), translatedPostInitBlocks);
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                        case VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            boolean isScriptClone = (attrDef.getModifiers().flags & SCRIPT_LEVEL_SYNTH_STATIC) != 0;
                            if (!isScriptClone) {
                                boolean isStatic = (attrDef.getModifiers().flags & STATIC) != 0;
                                inInstanceContext = isStatic ? ReceiverContext.ScriptAsStatic : isMixinClass ? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                                JCStatement initStmt = (!isStatic || getAttrEnv().toplevel.isLibrary) ? translateDefinitionalAssignmentToSet(attrDef.pos(),
                                        attrDef.getInitializer(), attrDef.getBindStatus(), attrDef.sym,
                                        (isStatic || !isMixinClass) ? null : defs.receiverName)
                                        : null;
                                attrInfo.append(new TranslatedVarInfo(
                                        attrDef,
                                        typeMorpher.varMorphInfo(attrDef.sym),
                                        initStmt,
                                        attrDef.isBound()? translateBind.translate(attrDef.getInitializer(), attrDef.sym) : null,
                                        attrDef.getOnReplace(),
                                        translateOnReplaceAsInline(attrDef.sym, attrDef.getOnReplace())));
                                inInstanceContext = ReceiverContext.Oops;
                            }
                            break;
                        }
                        case OVERRIDE_ATTRIBUTE_DEF: {
                            JFXOverrideClassVar override = (JFXOverrideClassVar) def;
                            boolean isStatic = (override.sym.flags() & STATIC) != 0;
                            inInstanceContext = isStatic? ReceiverContext.ScriptAsStatic : isMixinClass? ReceiverContext.InstanceAsStatic : ReceiverContext.InstanceAsInstance;
                            JCStatement initStmt;
                            inOverrideInstanceVariableDefinition = true;
                            initStmt = translateDefinitionalAssignmentToSet(override.pos(),
                                    override.getInitializer(), override.getBindStatus(), override.sym,
                                    (isStatic || !isMixinClass) ? null : defs.receiverName);
                            inOverrideInstanceVariableDefinition = false;
                            overrideInfo.append(new TranslatedOverrideClassVarInfo(
                                    override,
                                    typeMorpher.varMorphInfo(override.sym),
                                    initStmt,
                                    override.isBound()? translateBind.translate(override.getInitializer(), override.sym) : null,
                                    override.getOnReplace(),
                                    translateOnReplaceAsInline(override.sym, override.getOnReplace())));
                            inInstanceContext = ReceiverContext.Oops;
                            break;
                        }
                       case FUNCTION_DEF : {
                           JFXFunctionDefinition funcDef = (JFXFunctionDefinition) def;
                           boolean isScriptClone = (funcDef.getModifiers().flags & SCRIPT_LEVEL_SYNTH_STATIC) != 0;
                           if (!isScriptClone) {
                               translatedDefs.append(translate(funcDef));
                           }
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

            JavafxClassModel model = initBuilder.createJFXClassModel(tree, attrInfo.toList(), overrideInfo.toList(), literalInitClassMap);
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

            ClassSymbol superClassSym = model.superClassSym;
            List<ClassSymbol> immediateMixins = model.immediateMixins;
            boolean forceInit = !immediateMixins.isEmpty() || superClassSym == null || isMixinClass;

            if (forceInit || !translatedInitBlocks.isEmpty()) {
                // Add the userInit$ method
                List<JCVariableDecl> receiverVarDeclList = isMixinClass? List.of(makeReceiverParam(tree)) : List.<JCVariableDecl>nil();
                ListBuffer<JCStatement> initStats = ListBuffer.lb();

                // Mixin super calls will be handled when inserted into real classes.
                if (!isMixinClass) {
                    //TODO:
                    // Some implementation code is still generated assuming a receiver parameter.  Until this is fixed
                    //    var receiver = this;
                    initStats.prepend(
                            make.at(diagPos).VarDef(
                            make.at(diagPos).Modifiers(Flags.FINAL),
                            defs.receiverName,
                            make.Ident(initBuilder.interfaceName(currentClass)),
                            make.at(diagPos).Ident(names._this))
                            );

                    if (superClassSym != null) {
                        List<JCExpression> args1 = List.nil();
                        //args1 = args1.append(make.TypeCast(makeTypeTree(diagPos, superClassSym.type, true), make.Ident(names._this)));
                        initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, names._super), defs.userInitName, args1));
                    }

                    for (ClassSymbol mixinClassSym : immediateMixins) {
                        String mixinName = mixinClassSym.fullname.toString();
                        List<JCExpression> args1 = List.nil();
                        args1 = args1.append(make.TypeCast(makeType(diagPos, mixinClassSym.type, true), make.Ident(names._this)));
                        initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, mixinName), defs.userInitName, args1));
                    }
                }

                initStats.appendList(translatedInitBlocks);

                // Only create method if necessary (rely on FXBase.)
                if (initStats.nonEmpty() || isMixinClass || superClassSym == null) {
                    JCBlock userInitBlock = make.Block(0L, initStats.toList());
                    translatedDefs.append(make.MethodDef(
                            make.Modifiers(!isMixinClass ? Flags.PUBLIC : (Flags.PUBLIC | Flags.STATIC)),
                            defs.userInitName,
                            makeType( null,syms.voidType),
                            List.<JCTypeParameter>nil(),
                            receiverVarDeclList,
                            List.<JCExpression>nil(),
                            userInitBlock,
                            null));
                }
            }

            if (forceInit || !translatedPostInitBlocks.isEmpty()) {
                // Add the userPostInit$ method
                List<JCVariableDecl> receiverVarDeclList = isMixinClass? List.of(makeReceiverParam(tree)) : List.<JCVariableDecl>nil();
                ListBuffer<JCStatement> initStats = ListBuffer.lb();

                // Mixin super calls will be handled when inserted into real classes.
                if (!isMixinClass) {
                    //TODO:
                    // Some implementation code is still generated assuming a receiver parameter.  Until this is fixed
                    //    var receiver = this;
                    initStats.prepend(
                            make.at(diagPos).VarDef(
                            make.at(diagPos).Modifiers(Flags.FINAL),
                            defs.receiverName,
                            make.Ident(initBuilder.interfaceName(currentClass)),
                            make.at(diagPos).Ident(names._this))
                            );

                    if (superClassSym != null) {
                        List<JCExpression> args1 = List.nil();
                        //args1 = args1.append(make.TypeCast(makeTypeTree(diagPos, superClassSym.type, true), make.Ident(names._this)));
                        initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, names._super), defs.postInitName, args1));
                    }

                    for (ClassSymbol mixinClassSym : immediateMixins) {
                        String mixinName = mixinClassSym.fullname.toString();
                        List<JCExpression> args1 = List.nil();
                        args1 = args1.append(make.TypeCast(makeType(diagPos, mixinClassSym.type, true), make.Ident(names._this)));
                        initStats = initStats.append(callStatement(tree.pos(), makeIdentifier(diagPos, mixinName), defs.postInitName, args1));
                    }
                }

                initStats.appendList(translatedPostInitBlocks);

                // Only create method if necessary (rely on FXBase.)
                if (initStats.nonEmpty() || isMixinClass || superClassSym == null) {
                    JCBlock postInitBlock = make.Block(0L, initStats.toList());
                    translatedDefs.append(make.MethodDef(
                            make.Modifiers(!isMixinClass ? Flags.PUBLIC : (Flags.PUBLIC | Flags.STATIC)),
                            defs.postInitName,
                            makeType( null,syms.voidType),
                            List.<JCTypeParameter>nil(),
                            receiverVarDeclList,
                            List.<JCExpression>nil(),
                            postInitBlock,
                            null));
                }
            }

            if (tree.isScriptClass()) {
                if (!isMixinClass) {
                   // JFXC-1888: Do *not* add main method!
                   // com.sun.javafx.runtime.Main has the
                   // "main" that will call Entry.start().
                   // translatedDefs.append(makeMainMethod(diagPos, tree.getName()));
                }

                // Add binding support
                translatedDefs.appendList(scriptComplete(tree.pos()));
            }

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
            JCClassDecl res = make.at(diagPos).ClassDef(
                    classMods,
                    tree.getName(),
                    List.<JCTypeParameter>nil(), // type parameters
                    model.superType==null? null : makeType( null,model.superType, false),
                    implementing,
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
            result = res;
        }
        finally {
            attrEnv.enclClass = prevEnclClass;

            currentClass = prevClass;
        }
    }
    //where
    private void translateAndAppendStaticBlock(JFXBlock block, ListBuffer<JCStatement> translatedBlocks) {
        JCStatement stmt = translateToStatement(block);
        if (stmt != null) {
            translatedBlocks.append(stmt);
        }
    }

    //@Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should be processed by class tree";
    }

    abstract class NewInstanceTranslator extends Translator {

        // Statements for the initialization steps.
        protected ListBuffer<JCStatement> stats = ListBuffer.lb();

        // Statements to set symbols with initial values.
        protected ListBuffer<JCStatement> varInits = ListBuffer.lb();

        // Symbols corresponding to caseStats.
        protected ListBuffer<VarSymbol> varSyms = ListBuffer.lb();

        NewInstanceTranslator(DiagnosticPosition diagPos) {
            super(diagPos);
        }

        /**
         * Initialize the instance variables of the instance
         * @param instName
         */
        protected abstract void initInstanceVariables(Name instName);

        /**
         * @return the constructor args -- translating any supplied args
         */
        protected abstract List<JCExpression> completeTranslatedConstructorArgs();

        protected JCExpression translateInstanceVariableInit(JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
            VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
            return translateDefinitionalAssignmentToValueArg(init.pos(), init, bindStatus, vmi);
        }

        void setInstanceVariable(DiagnosticPosition diagPos, Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JCExpression transInit) {
            //TODO: should not be calling definitionalAssignmentToSetExpression, instead should be translateDefinitionalAssignmentToSetExpression
            varInits.append(m().Exec( definitionalAssignmentToSetExpression(diagPos, transInit, bindStatus, instName,
                                                     typeMorpher.varMorphInfo(vsym)) ) );
            varSyms.append(vsym);
        }

        void setInstanceVariable(DiagnosticPosition diagPos, Name instName, VarSymbol vsym, JCExpression transInit) {
            setInstanceVariable(diagPos, instName, JavafxBindStatus.UNBOUND, vsym, transInit);
        }

        void setInstanceVariable(Name instName, JavafxBindStatus bindStatus, VarSymbol vsym, JFXExpression init) {
            DiagnosticPosition initPos = init.pos();
            JCExpression transInit = translateInstanceVariableInit(init, bindStatus, vsym);
            setInstanceVariable(initPos, instName, bindStatus, vsym, transInit);
        }

        void setInstanceVariable(Name instName, VarSymbol vsym, JFXExpression init) {
            setInstanceVariable(instName, JavafxBindStatus.UNBOUND, vsym, init);
        }

        void makeInitSupportCall(Name methName, Name receiverName) {
            JCExpression receiver = id(receiverName);
            JCStatement callExec = callStatement(receiver, methName, List.<JCExpression>nil());
            stats.append(callExec);
        }

        void makeInitApplyDefaults(Type classType, Name receiverName) {
            ClassSymbol classSym = (ClassSymbol)classType.tsym;
            int count = varSyms.size();

            JCVariableDecl loopVar = makeTmpLoopVar(diagPos, 0);
            Name loopName = loopVar.name;
            JCExpression loopLimit = m().Apply(null, select(id(receiverName), names.fromString(attributeCountMethodString)),
                                               List.<JCExpression>nil());
            JCVariableDecl loopLimitVar = makeTmpVar("count", syms.intType, loopLimit);
            stats.append(loopLimitVar);
            JCExpression loopTest = makeBinary(JCTree.LT, id(loopName), id(loopLimitVar.name));
            List<JCExpressionStatement> loopStep = List.of(m().Exec(m().Assignop(JCTree.PLUS_ASG, id(loopName), m().Literal(TypeTags.INT, 1))));
            JCStatement loopBody;

            List<JCExpression> args = List.<JCExpression>of(id(loopName));
            JCStatement applyDefaultsExpr = callStatement(id(receiverName), defs.applyDefaultsPrefixName, args);

            if (1 < count) {
                // final short[] jfx$0map = GETMAP$X();
                JCExpression getmapExpr = m().Apply(null, id(varGetMapName(classSym)), List.<JCExpression>nil());
                JCVariableDecl mapVar = makeTmpVar("map", syms.javafx_ShortArray, getmapExpr);
                stats.append(mapVar);

                LiteralInitVarMap varMap = literalInitClassMap.getVarMap(classSym);
                int[] tags = new int[count];

                int index = 0;
                for (VarSymbol varSym : varSyms.toList()) {
                    tags[index++] = varMap.addVar(varSym);
                }

                ListBuffer<JCCase> cases = ListBuffer.lb();
                index = 0;
                for (JCStatement varInit : varInits) {
                    cases.append(m().Case(m().Literal(TypeTags.INT, tags[index++]), List.<JCStatement>of(varInit, m().Break(null))));
                }

                cases.append(m().Case(null, List.<JCStatement>of(applyDefaultsExpr, m().Break(null))));

                JCExpression mapExpr = m().Indexed(id(mapVar.name), id(loopName));
                loopBody = m().Switch(mapExpr, cases.toList());
            } else {
                VarSymbol varSym = varSyms.first();
                JCExpression varOffsetExpr = select(makeType(classType, false), attributeOffsetName(varSym));
                JCVariableDecl offsetVar = makeTmpVar("off", syms.intType, varOffsetExpr);
                stats.append(offsetVar);
                JCExpression condition = makeEqual(id(loopName), id(offsetVar.name));
                loopBody = m().If(condition, varInits.first(), applyDefaultsExpr);
            }

            stats.append(m().ForLoop(List.<JCStatement>of(loopVar), loopTest, loopStep, loopBody));
        }

        /**
         * Return the instance building expression
         * @param declaredType
         * @param cdef
         * @param isFX
         * @return
         */
        protected JCExpression buildInstance(Type declaredType, JFXClassDeclaration cdef, boolean isFX) {
            Type type;

            if (cdef == null) {
                type = declaredType;
            } else {
                stats.append(translateClassDef(cdef));
                type = cdef.type;
            }
            JCExpression classTypeExpr = makeType(type, false);

            List<JCExpression> newClassArgs = completeTranslatedConstructorArgs();

            Name tmpVarName = getSyntheticName("objlit");
            initInstanceVariables(tmpVarName);  // Must preceed varSyms.nonEmpty() test

            JCExpression instExpression;
            if (varSyms.nonEmpty() || (isFX && newClassArgs.nonEmpty()) || cdef != null) {
                // it is a instanciation of a JavaFX class which has instance variable initializers
                // (or is anonymous, or has an outer class argument)
                //
                //   {
                //       final X jfx$0objlit = new X(true);
                //       final short[] jfx$0map = GETMAP$X();
                //
                //       for (int jfx$0initloop = 0; i < X.$VAR_COUNT; i++) {
                //           if (!isInitialized(jfx$0initloop) {
                //               switch (jfx$0map[jfx$0initloop]) {
                //                   1: jfx$0objlit.set$a(0); break;
                //                   2: jfx$0objlit.set$b(0); break;
                //                   ...
                //                   n: jfx$0objlit.set$z(0); break;
                //                   default: jfx$0objlit.applyDefaults$(jfx$0initloop);
                //               }
                //           }
                //       }
                //
                //       jfx$0objlit.complete$();
                //       jfx$0objlit
                //   }

                // Use the JavaFX constructor by adding a marker argument. The "true" in:
                //       ... new X(true);
                newClassArgs = newClassArgs.append(m().Literal(TypeTags.BOOLEAN, 1));

                // Create the new instance, placing it in a temporary variable "jfx$0objlit"
                //       final X jfx$0objlit = new X(true);
                stats.append(makeVar(
                        tmpVarName,
                        type,
                        m().NewClass(null, null, classTypeExpr, newClassArgs, null)));

                // Apply defaults to the instance variables
                //
                //       final short[] jfx$0map = GETMAP$X();
                //       for (int jfx$0initloop = 0; i < X.$VAR_COUNT; i++) {
                //           ...
                //       }
                if (varSyms.nonEmpty()) {
                    makeInitApplyDefaults(type, tmpVarName);
                } else {
                    makeInitSupportCall(defs.applyDefaultsPrefixName, tmpVarName);
                }

                // Call complete$ to do user's init and postinit blocks
                //       jfx$0objlit.complete$();
                makeInitSupportCall(defs.completeName, tmpVarName);

                // Return the instance from the block expressions
                //       jfx$0objlit
                JCExpression instValue = id(tmpVarName);

                // Wrap it in a block expression
                instExpression = makeBlockExpression(stats, instValue);

            } else {
                // this is a Java class or has no instance variable initializers, just instanciate it
                instExpression = m().NewClass(null, null, classTypeExpr, newClassArgs, null);
            }

            return instExpression;
        }
    }



    /**
     * Translate to a built-in type
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
            setInstanceVariable(instName, vsym, init);
        }

        @Override
        protected JCExpression doit() {
            return buildInstance(builtIn, null, true);
        }
    }

    /**
     * Translator for object literals
     */
    abstract class InstanciateTranslator extends NewInstanceTranslator {

        protected final JFXInstanciate tree;
        private final Symbol idSym;

        InstanciateTranslator(final JFXInstanciate tree) {
            super(tree.pos());
            this.tree = tree;
            this.idSym = JavafxTreeInfo.symbol(tree.getIdentifier());
        }

        abstract protected void processLocalVar(JFXVar var);

        protected void initInstanceVariables(Name instName) {
            if (tree.varDefinedByThis != null) {
                substitutionMap.put(tree.varDefinedByThis, instName);
            }
            for (JFXObjectLiteralPart olpart : tree.getParts()) {
                diagPos = olpart.pos(); // overwrite diagPos (must restore)
                JavafxBindStatus bindStatus = olpart.getBindStatus();
                JFXExpression init = olpart.getExpression();
                VarSymbol vsym = (VarSymbol) olpart.sym;
                setInstanceVariable(instName, bindStatus, vsym, init);
            }
            if (tree.varDefinedByThis != null) {
                substitutionMap.remove(tree.varDefinedByThis);
            }
            diagPos = tree.pos();
        }

        protected List<JCExpression> translatedConstructorArgs() {
            List<JFXExpression> args = tree.getArgs();
            Symbol sym = tree.constructor;
            if (sym != null && sym.type != null) {
                ListBuffer<JCExpression> translated = ListBuffer.lb();
                List<Type> formals = sym.type.asMethodType().getParameterTypes();
                boolean usesVarArgs = (sym.flags() & VARARGS) != 0L &&
                        (formals.size() != args.size() ||
                        types.isConvertible(args.last().type, types.elemtype(formals.last())));
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
                    JCExpression targ = translateAsValue(l.head, formal);
                    if (targ != null) {
                        translated.append(targ);
                    }
                }
                return translated.toList();
            } else {
                return translateExpressions(args);
            }
        }

        @Override
        protected List<JCExpression> completeTranslatedConstructorArgs() {
            List<JCExpression> translated = translatedConstructorArgs();
            if (tree.getClassBody() != null &&
                    tree.getClassBody().sym != null && hasOuters.contains(tree.getClassBody().sym) ||
                    idSym != null && hasOuters.contains(idSym)) {
                JCIdent thisIdent = id(defs.receiverName);
                translated = translated.prepend(thisIdent);
            }
            return translated;
        }

        protected JCExpression doit() {
            for (JFXVar var : tree.getLocalvars()) {
                // add the variable before the class definition or object litersl assignment
                processLocalVar(var);
            }
            return buildInstance(tree.type, tree.getClassBody(), types.isJFXClass(idSym));
        }
    }

    //@Override
    public void visitInstanciate(JFXInstanciate tree) {
        JFXExpression texp = tree.getIdentifier();
        Type type = texp.type;
        /* MAYBE FUTURE:
        if (tree.getJavaFXKind() == JavaFXKind.INSTANTIATE_NEW &&
                type.tag == TypeTags.ARRAY) {
            JCExpression elemtype = makeTypeTree(texp, ((Type.ArrayType) type).getComponentType());
            JCExpression len = translateAsValue(tree.getArgs().head, syms.intType);
            result = make.at(tree).NewArray(elemtype, List.of(len), null);
            return;
        }
        */

        ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
        prependToStatements = ListBuffer.lb();

        result = new InstanciateTranslator(tree) {
            protected void processLocalVar(JFXVar var) {
                stats.append(translateToStatement(var));
            }
        }.doit();

        if (result instanceof BlockExprJCBlockExpression) {
            BlockExprJCBlockExpression blockExpr = (BlockExprJCBlockExpression) result;
            blockExpr.stats = blockExpr.getStatements().prependList(prependToStatements.toList());
        }
        prependToStatements = prevPrependToStatements;
    }

    //@Override
    public void visitStringExpression(JFXStringExpression tree) {
        result = new StringExpressionTranslator(tree) {
            protected JCExpression translateArg(JFXExpression arg) {
                return translateAsUnconvertedValue(arg);
            }
        }.doit();
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
            JCExpression trans = translateAsValue(init, resultType);
            return convertNullability(diagPos, trans, init, resultType);
        }
    }

    //TODO: get rid of this -- its existance has too many assumptions
    private JCExpression translateDefinitionalAssignmentToValueArg(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarMorphInfo vmi) {
        if (bindStatus.isUnidiBind()) {
            return translateNonBoundInit(diagPos, init, vmi);
            // return TODO(); // toBound.translateAsLocationOrBE(init, bindStatus, vmi);
        } else if (bindStatus.isBidiBind()) {
            // Bi-directional bind translate so it stays in a Location
            return translateNonBoundInit(diagPos, init, vmi);
           //  return TODO();
        } else {
            // normal init -- unbound
            return translateNonBoundInit(diagPos, init, vmi);
        }
    }

    private JCExpression translateDefinitionalAssignmentToValue(DiagnosticPosition diagPos,
            JFXExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        assert !vmi.isMemberVariable();
        if (bindStatus.isUnidiBind()) {
            return TODO(); // toBound.translateAsLocationOrBE(init, bindStatus, vmi);
        } else if (bindStatus.isBidiBind()) {
            // Bi-directional bind translate so it stays in a Location
            return TODO();
        } else {
            return translateNonBoundInit(diagPos, init, vmi);
        }
    }

    //TODO: make this private again
    JCStatement translateDefinitionalAssignmentToSet(DiagnosticPosition diagPos,
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
        JCExpression valueArg = translateDefinitionalAssignmentToValueArg(diagPos, init, bindStatus, vmi);
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
            return callExpression(diagPos, tc, attributeBeName(vsym), nonNullInit);
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

        result = translateDefinitionalAssignmentToSetExpression(diagPos, var.init, var.getBindStatus(), vmi, null);
    }

    //@Override
    public void visitVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXModifiers mods = tree.getModifiers();
        final VarSymbol vsym = tree.getSymbol();
        final VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        assert vsym.owner.kind != Kinds.TYP : "attributes are processed in the class and should never come here";
        final long flags = vsym.flags();
        final boolean isParameter = (flags & Flags.PARAMETER) != 0;
        final boolean hasInnerAccess = (flags & JavafxFlags.VARUSE_INNER_ACCESS) != 0;
        final long modFlags = (mods.flags & ~Flags.FINAL) | ((hasInnerAccess | isParameter)? Flags.FINAL : 0L);
        final JCModifiers tmods = make.at(diagPos).Modifiers(modFlags);
        final Type type = tree.type;
        final JCExpression typeExpression = makeType(diagPos, type, true);

        // for class vars, initialization happens during class init, so set to
        // default Location.  For local vars translate as definitional
        JCExpression init;
        if (isParameter) {
            // parameters aren't initialized
            init = null;
            result = make.at(diagPos).VarDef(tmods, tree.name, typeExpression, init);
        } else {
            // create a blank variable declaration and move the declaration to the beginning of the block
            optStat.recordLocalVar(vsym, tree.getBindStatus().isBound(), false);
            if ((modFlags & Flags.FINAL) != 0) {
                //TODO: this case probably won't be used any more, but it will
                // be again if we optimize the case for initializer which don't reference locals
                init = translateDefinitionalAssignmentToValue(tree.pos(), tree.init,
                        tree.getBindStatus(), vsym);
                JCStatement var = make.at(diagPos).VarDef(tmods, tree.name, typeExpression, init);
                prependToStatements.append(var);
                result = make.at(diagPos).Skip();
                return;
            }
            init = makeDefaultValue(diagPos, vmi);
            prependToStatements.prepend(make.at(Position.NOPOS).VarDef(tmods, tree.name, typeExpression, init));

            result = translateDefinitionalAssignmentToSetExpression(diagPos, tree.init, tree.getBindStatus(), vmi, null);
        }
    }

    //@Override
    public void visitOverrideClassVar(JFXOverrideClassVar tree) {
        assert false : "should be processed by parent tree";
    }

    //@Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should be processed by parent tree";
    }


    //@Override
    public void visitFunctionValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }

   boolean isInnerFunction (MethodSymbol sym) {
       return sym.owner != null && sym.owner.kind != Kinds.TYP
                && (sym.flags() & Flags.SYNTHETIC) == 0;
   }

   @Override
   JCTree translateFunction(JFXFunctionDefinition tree, boolean maintainContext) {
       return new FunctionTranslator(tree, maintainContext).doit();
   }

   class FunctionTranslator extends Translator {

        final JFXFunctionDefinition tree;
        final boolean maintainContext;
        final MethodType mtype;
        final MethodSymbol sym;
        final Symbol owner;
        final Name name;
        final boolean isBound;
        final boolean isRunMethod;
        final boolean isAbstract;
        final boolean isStatic;
        final boolean isSynthetic;
        final boolean isInstanceFunction;
        final boolean isInstanceFunctionAsStaticMethod;
        final boolean isMixinClass;

        FunctionTranslator(JFXFunctionDefinition tree, boolean maintainContext) {
            super(tree.pos());
            this.tree = tree;
            this.maintainContext = maintainContext;
            this.mtype = (MethodType) tree.type;
            this.sym = (MethodSymbol) tree.sym;
            this.owner = sym.owner;
            this.name = tree.name;
            this.isBound = (sym.flags() & JavafxFlags.BOUND) != 0;
            this.isRunMethod = syms.isRunMethod(tree.sym);
            this.isMixinClass = currentClass.isMixinClass();
            long originalFlags = tree.mods.flags;
            this.isAbstract = (originalFlags & Flags.ABSTRACT) != 0L;
            this.isSynthetic = (originalFlags & Flags.SYNTHETIC) != 0L;
            this.isStatic = (originalFlags & Flags.STATIC) != 0L;
            this.isInstanceFunction = !isAbstract && !isStatic && !isSynthetic;
            this.isInstanceFunctionAsStaticMethod = isInstanceFunction && isMixinClass;
        }

        private JCBlock makeRunMethodBody(JFXBlock bexpr) {
            final JFXExpression value = bexpr.value;
            JCBlock block;
            if (value == null || value.type == syms.voidType) {
                // the block has no value: translate as simple statement and add a null return
                block = asBlock(translateToStatement(bexpr));
                block.stats = block.stats.append(make.Return(make.at(diagPos).Literal(TypeTags.BOT, null)));
            } else {
                // block has a value, return it
                block = asBlock(translateToStatement(bexpr, value.type));
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

        private long methodFlags() {
            long methodFlags = tree.mods.flags;
            methodFlags &= ~Flags.PROTECTED;
            methodFlags &= ~Flags.SYNTHETIC;
            methodFlags |= Flags.PUBLIC;
            if (isInstanceFunctionAsStaticMethod) {
                methodFlags |= Flags.STATIC;
            }
            return methodFlags;
        }

        private List<JCVariableDecl> methodParameters() {
            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            if (isInstanceFunctionAsStaticMethod) {
                // if we are converting a standard instance function (to a static method), the first parameter becomes a reference to the receiver
                params.prepend(makeReceiverParam(currentClass));
            }
            for (JFXVar fxVar : tree.getParams()) {
                JCVariableDecl var = (JCVariableDecl) translate(fxVar);
                params.append(var);
            }
            return params.toList();
        }

        private JCBlock methodBody() {
            // construct the body of the translated function
            JFXBlock bexpr = tree.getBodyExpression();
            JCBlock body;
            if (bexpr == null) {
                body = null; // null if no block expression
            } else if (isBound) {
                TODO();
                body = null;
            } else if (isRunMethod) {
                // it is a module level run method, do special translation
                body = makeRunMethodBody(bexpr);
            } else {
                // the "normal" case
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                for (JFXVar fxVar : tree.getParams()) {
                    if (types.isSequence(fxVar.sym.type)) {
                        setDiagPos(fxVar);
                        stmts.append(callStatement(id(fxVar.getName()), defs.incrementSharingMethodName));
                    }
                }
                setDiagPos(bexpr);
                stmts.append(translateToStatement(bexpr, mtype.getReturnType()));
                body = make.at(bexpr).Block(0L, stmts.toList());
            }

            if (isInstanceFunction && !isMixinClass) {
                //TODO: unfortunately, some generated code still expects a receiver$ to always be present.
                // In the instance as instance case, there is no receiver param, so allow generated code
                // to function by adding:   var receiver = this;
                //TODO: this should go away
                body.stats = body.stats.prepend( m().VarDef(
                        m().Modifiers(Flags.FINAL),
                        defs.receiverName,
                        id(interfaceName(currentClass)),
                        id(names._this)));
            }
            return body;
        }

        private JCMethodDecl makeMethod(long flags, JCBlock body, List<JCVariableDecl> params) {
            JCMethodDecl meth = m().MethodDef(
                    addAccessAnnotationModifiers(diagPos, tree.mods.flags, m().Modifiers(flags)),
                    functionName(sym, isInstanceFunctionAsStaticMethod, isBound),
                    makeReturnTypeTree(diagPos, sym, isBound),
                    m().TypeParams(mtype.getTypeArguments()),
                    params,
                    m().Types(mtype.getThrownTypes()), // makeThrows(diagPos), //
                    body,
                    null);
            meth.sym = sym;
            meth.type = tree.type;
            return meth;
        }

        protected JCTree doit() {
            TranslationState prevZ = translationState;
            translationState = null; //should be explicitly set
            ReceiverContext prevContext = inInstanceContext;
            if (!maintainContext) {
                inInstanceContext = isStatic?
                    ReceiverContext.ScriptAsStatic :
                    isInstanceFunctionAsStaticMethod ?
                        ReceiverContext.InstanceAsStatic :
                        ReceiverContext.InstanceAsInstance;
            }

            try {
                return makeMethod(methodFlags(), methodBody(), methodParameters());
            } finally {
                translationState = prevZ;
                inInstanceContext = prevContext;
            }
        }
    }

   /**
    * Translate JavaFX a class definition into a Java static implementation method.
    */
    //@Override
    public void visitFunctionDefinition(JFXFunctionDefinition tree) {
        result = translateFunction(tree, false);
    }

    class BlockExpressionTranslator extends Translator {

        private final JFXExpression value;
        private final List<JFXExpression> statements;
        private final Type type;

        BlockExpressionTranslator(JFXBlock tree) {
            super(tree.pos());
            this.value = tree.value;
            this.statements = tree.getStmts();
            this.type = tree.type;
        }

        JCTree doit() {
            Type targetType = translationState.targetType;
            Yield yield = translationState.yield;

            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            try {
                prependToStatements = ListBuffer.lb();

                ListBuffer<JCStatement> translatedStmts = ListBuffer.lb();
                for (JFXExpression expr : statements) {
                    JCStatement stmt = translateToStatement(expr);
                    if (stmt != null) {
                        translatedStmts.append(stmt);
                    }
                }
                List<JCStatement> localDefs = translatedStmts.toList();

                if (yield == ToExpression) {
                    // make into block expression
                    assert (type != syms.voidType) : "void block expressions should be handled below";

                    //TODO: this may be unneeded, or even wrong
                    JFXExpression rawValue = (value.getFXTag() == JavafxTag.RETURN)?
                         ((JFXReturn) value).getExpression()
                        : value;

                    JCExpression tvalue = translateToExpression(rawValue, null); // must be before prepend
                    localDefs = prependToStatements.appendList(localDefs).toList();
                    return makeBlockExpression(localDefs, tvalue);  //TODO: tree.flags lost
                } else {
                    // make into block
                    prependToStatements.appendList(localDefs);
                    if (value != null) {
                        JCStatement stmt = translateToStatement(value, targetType);
                        if (stmt != null) {
                            prependToStatements.append(stmt);
                        }
                    }
                    localDefs = prependToStatements.toList();
                    return localDefs.size() == 1 ? localDefs.head : make.at(diagPos).Block(0L, localDefs);
                }
            } finally {
                prependToStatements = prevPrependToStatements;
            }
        }
    }

    public void visitBlockExpression(JFXBlock tree) {
        result = (new BlockExpressionTranslator(tree)).doit();
    }

    //@Override
    public void visitAssign(final JFXAssign tree) {
        DiagnosticPosition diagPos = tree.pos();

        if (tree.lhs.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
            TODO();
            // assignment of a sequence slice --  s[i..j]=8, call the sequence set method
            JFXSequenceSlice si = (JFXSequenceSlice)tree.lhs;
            JCExpression rhs = translateAsValue(tree.rhs, si.getSequence().type);
            JCExpression seq = translateAsSequenceVariable(si.getSequence());
            JCExpression startPos = translateAsValue(si.getFirstIndex(), syms.intType);
            JCExpression endPos = makeSliceEndPos(si);
            JCFieldAccess select = make.Select(seq, defs.replaceSliceMethodName);
            List<JCExpression> args = List.of(startPos, endPos, rhs);
            result = make.at(diagPos).Apply(null, select, args);
        } else {
            result = new AssignTranslator(diagPos, tree.lhs, tree.rhs) {

                JCExpression translateExpression(JFXExpression expr, Type type) {
                    return translateToExpression(expr, type);
                }

                JCExpression buildRHS(JCExpression rhsTranslated) {
                    return rhsTranslated;
                }

                JCExpression defaultFullExpression(JCExpression lhsTranslated, JCExpression rhsTranslated) {
                    return m().Assign(lhsTranslated, rhsTranslated);
                }
            }.doit();
        }
    }

    //@Override
    public void visitAssignop(final JFXAssignOp tree) {
        result = new AssignTranslator(tree.pos(), tree.lhs, tree.rhs) {

            JCExpression translateExpression(JFXExpression expr, Type type) {
                return translateToExpression(expr, type);
            }

            private boolean useDurationOperations() {
                return types.isSameType(lhs.type, syms.javafx_DurationType);
            }

            @Override
            JCExpression buildRHS(JCExpression rhsTranslated) {
                final JCExpression lhsTranslated = translateAsUnconvertedValue(lhs);
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

    class SelectTranslator extends NullCheckTranslator {

        protected final Symbol sym;
        protected final boolean isFunctionReference;
        protected final boolean staticReference;
        protected final Name name;

        protected SelectTranslator(JFXSelect tree) {
            super(tree.pos(), tree.getExpression(), tree.type, tree.sym.isStatic());
            sym = tree.sym;
            isFunctionReference = tree.type instanceof FunctionType && sym.type instanceof MethodType;
            staticReference = sym.isStatic();
            name = tree.getIdentifier();
        }

        @Override
        protected JCExpression translateToCheck(JFXExpression expr) {
            // this may or may not be in a LHS but in either
            // event the selector is a value expression
            JCExpression translatedSelected = translateAsUnconvertedValue(expr);

            if (staticReference) {
                translatedSelected = staticReference(sym);
            } else if (expr instanceof JFXIdent) {
                JFXIdent ident = (JFXIdent)expr;
                Symbol identSym = ident.sym;

                if (identSym != null && types.isJFXClass(identSym)) {
                    if ((identSym.flags_field & JavafxFlags.MIXIN) != 0) {
                        translatedSelected = id(defs.receiverName);
                    } else if (identSym == getAttrEnv().enclClass.sym) {
                        translatedSelected = id(names._this);
                    } else {
                        translatedSelected = id(names._super);
                    }
                }
            }

            return translatedSelected;
        }

        @Override
        protected JCExpression fullExpression(JCExpression mungedToCheckTranslated) {
            Symbol selectorSym = (toCheck != null)? expressionSymbol(toCheck) : null;
            // If this is OuterClass.memberName or MixinClass.memberName, then
            // we want to create expression to get the proper receiver.
            if (!staticReference && selectorSym != null && selectorSym.kind == Kinds.TYP) {
                mungedToCheckTranslated = makeReceiver(diagPos, sym);
            }
            if (isFunctionReference) {
                MethodType mtype = (MethodType) sym.type;
                JCExpression tc = staticReference?
                    mungedToCheckTranslated :
                    addTempVar(toCheck.type, mungedToCheckTranslated);
                JCExpression translated = select(tc, name);
                return makeFunctionValue(translated, null, diagPos, mtype);
            } else {
                JCExpression tc = mungedToCheckTranslated;
                if (tc != null && toCheck.type != null && toCheck.type.isPrimitive()) {  // expr.type is null for package symbols.
                    tc = makeBox(diagPos, tc, toCheck.type);
                }
                JCExpression translated = select(tc, name);

                return convertVariableReference(translated, sym);
            }
        }
    }


    public void visitSelect(JFXSelect tree) {
        if (substitute(tree.sym)) {
            return;
        }
        result = new SelectTranslator(tree).doit();
    }

    //@Override
    public void visitIdent(JFXIdent tree) {
        if (substitute(tree.sym)) {
            return;
        }
        result = new IdentTranslator(tree).doit();
    }

    private class ExplicitSequenceTranslator extends Translator {

        final List<JFXExpression> items;
        final Type elemType;

        ExplicitSequenceTranslator(DiagnosticPosition diagPos, List<JFXExpression> items, Type elemType) {
            super(diagPos);
            this.items = items;
            this.elemType = elemType;
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
        protected JCExpression doit() {
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            UseSequenceBuilder builder = useSequenceBuilder(diagPos, elemType, items.length());
            stmts.append(builder.makeBuilderVar());
            for (JFXExpression item : items) {
                if (item.getJavaFXKind() != JavaFXKind.NULL_LITERAL) {
                    // Insert all non-null elements
                    stmts.append(builder.addElement(item));
                }
            }
            return makeBlockExpression(stmts, builder.makeToSequence());
        }
    }

    //@Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
        result = new ExplicitSequenceTranslator(
                tree.pos(),
                tree.getItems(),
                types.elementType(tree.type)
        ).doit();
    }

    //@Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        DiagnosticPosition diagPos = tree.pos();
        RuntimeMethod rm  = tree.isExclusive()?
                    defs.Sequences_rangeExclusive :
                    defs.Sequences_range;
        Type elemType = syms.javafx_IntegerType;
        int ltag = tree.getLower().type.tag;
        int utag = tree.getUpper().type.tag;
        int stag = tree.getStepOrNull() == null? TypeTags.INT : tree.getStepOrNull().type.tag;
        if (ltag == TypeTags.FLOAT || ltag == TypeTags.DOUBLE ||
                utag == TypeTags.FLOAT || utag == TypeTags.DOUBLE ||
                stag == TypeTags.FLOAT || stag == TypeTags.DOUBLE) {
            elemType = syms.javafx_NumberType;
        }
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append( translateAsValue( tree.getLower(), elemType ));
        args.append( translateAsValue( tree.getUpper(), elemType ));
        if (tree.getStepOrNull() != null) {
            args.append( translateAsValue( tree.getStepOrNull(), elemType ));
        }
        result = convertTranslated(
                runtime(diagPos, rm, args.toList()),
                diagPos,
                types.sequenceType(elemType),
                tree.type);
    }

    //@Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        if (types.isSequence(tree.type)) {
            Type elemType = types.boxedElementType(tree.type);
            JCExpression expr = accessEmptySequence(tree.pos(), elemType);
            result =  castFromObject(expr, syms.javafx_SequenceTypeErasure);
        }
        else
            result = make.at(tree.pos).Literal(TypeTags.BOT, null);
    }

    public JCExpression translateSequenceExpression (JFXExpression seq) {
        return translateToExpression(seq, null);
    }

    public JCExpression translateSequenceIndexed(DiagnosticPosition diagPos, JFXExpression seq, JCExpression tseq, JCExpression index, Type elementType) {
        Name getMethodName;
        boolean primitive = elementType.isPrimitive();

        getMethodName = defs.getMethodName;
        if (seq instanceof JFXIdent) {
            JFXIdent var = (JFXIdent) seq;
            OnReplaceInfo info = findOnReplaceInfo(var.sym);
            if (info != null) {
                String mname = getMethodName.toString();
                ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                args.append(make.TypeCast(makeType(diagPos, info.arraySequenceType, true), make.Ident(defs.onReplaceArgNameBuffer)));
                if (var.sym == info.oldValueSym) {
                    mname = mname + "FromOldValue";
                    args.append(make.TypeCast(makeType(diagPos, info.seqWithExtendsType, true), make.Ident(defs.onReplaceArgNameOld)));
                    args.append(make.Ident(defs.onReplaceArgNameFirstIndex));
                    args.append(make.Ident(defs.onReplaceArgNameLastIndex));
                }
                else { // var.sym == info.newElementsSym
                    mname = mname + "FromNewElements";
                    args.append(make.Ident(defs.onReplaceArgNameFirstIndex));
                    args.append(make.TypeCast(makeType(diagPos, info.seqWithExtendsType, true), make.Ident(defs.onReplaceArgNameNewElements)));
                }
                args.append(index);
                return callExpression(diagPos, makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                                      mname, args.toList());
            }
        }
        return callExpression(diagPos, tseq, getMethodName, index);
    }

    //@Override
    public void visitSequenceIndexed(final JFXSequenceIndexed tree) {
        DiagnosticPosition diagPos = tree.pos();
        JFXExpression seq = tree.getSequence();
        JCExpression index = translateAsValue(tree.getIndex(), syms.intType);
        JCExpression tseq = translateToExpression(seq, null);
        if (seq.type.tag == TypeTags.ARRAY) {
            result = make.at(diagPos).Indexed(tseq, index);
        }
        else {
            result = translateSequenceIndexed(diagPos, seq, tseq, index, tree.type);
        }
    }

    //@Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seq = translateAsSequenceVariable(tree.getSequence());
        JCExpression firstIndex = translateAsValue(tree.getFirstIndex(), syms.intType);
        JCExpression lastIndex = makeSliceEndPos(tree);
        JCFieldAccess select = make.at(diagPos).Select(seq, defs.getSliceMethodName);
        List<JCExpression> args = List.of(firstIndex, lastIndex);
        result = make.at(diagPos).Apply(null, select, args);
    }

    //@Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        TODO();
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
        result = callStatement(diagPos, receiver, names.fromString(method), args.toList());
*****/
    }

    //@Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        TODO();
/*****
        JFXExpression seq = tree.getSequence();

        if (tree.getElement() != null) {
            JCExpression seqLoc = translateAsSequenceVariable(seq);
            result = callStatement(tree.pos(), seqLoc, "deleteValue", translateAsUnconvertedValue(tree.getElement())); //TODO: convert to seqence type
        } else {
            if (seq.getFXTag() == JavafxTag.SEQUENCE_INDEXED) {
                // deletion of a sequence element -- delete s[i]
                JFXSequenceIndexed si = (JFXSequenceIndexed) seq;
                JFXExpression seqseq = si.getSequence();
                JCExpression seqLoc = translateAsSequenceVariable(seqseq);
                JCExpression index = translateAsValue(si.getIndex(), syms.intType);
                result = callStatement(tree.pos(), seqLoc, "delete", index);
            } else if (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE) {
                // deletion of a sequence slice --  delete s[i..j]=8
                JFXSequenceSlice slice = (JFXSequenceSlice) seq;
                JFXExpression seqseq = slice.getSequence();
                JCExpression seqLoc = translateAsSequenceVariable(seqseq);
                JCExpression first = translateAsValue(slice.getFirstIndex(), syms.intType);
                JCExpression end = makeSliceEndPos(slice);
                result = callStatement(tree.pos(), seqLoc, "deleteSlice", List.of(first, end));
            } else if (types.isSequence(seq.type)) {
                JCExpression seqLoc = translateAsSequenceVariable(seq);
                result = callStatement(tree.pos(), seqLoc, "deleteAll");
            } else {
                result = make.at(tree.pos()).Exec(
                            make.Assign(
                                translateAsUnconvertedValue(seq), //TODO: does this work?
                                make.Literal(TypeTags.BOT, null)));
            }
        }
 *****/
    }

    /**** utility methods ******/

     JCExpression makeSliceEndPos(JFXSequenceSlice tree) {
        JCExpression endPos;
        if (tree.getLastIndex() == null) {
            endPos = callExpression(tree,
                    translateAsUnconvertedValue(tree.getSequence()),
                    defs.sizeMethodName);
            if (tree.getEndKind() == SequenceSliceTree.END_EXCLUSIVE)
                endPos = make.at(tree).Binary(JCTree.MINUS,
                        endPos, make.Literal(TypeTags.INT, 1));
        }
        else {
            endPos = translateAsValue(tree.getLastIndex(), syms.intType);
            if (tree.getEndKind() == SequenceSliceTree.END_INCLUSIVE)
                endPos = make.at(tree).Binary(JCTree.PLUS,
                        endPos, make.Literal(TypeTags.INT, 1));
        }
        return endPos;
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
                JCExpression expr = translateAsValue(exprToAdd, targetType(exprToAdd));
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

    abstract class UseSequenceBuilder {
        final DiagnosticPosition diagPos;
        final Type elemType;
        final String seqBuilder;

        Name sbName;
        boolean addTypeInfoArg = true;

        private UseSequenceBuilder(DiagnosticPosition diagPos, Type elemType, String seqBuilder) {
            this.diagPos = diagPos;
            this.elemType = elemType;
            this.seqBuilder = seqBuilder;
        }

        Type targetType(JFXExpression exprToAdd) {
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
                    localSeqBuilder = "com.sun.javafx.runtime.sequence." + JavafxVarSymbol.getTypePrefix(kind) + "ArraySequence";
                }
                else
                    localSeqBuilder = sequenceBuilderString;
            }
            JCExpression builderTypeExpr = makeQualifiedTree(diagPos, localSeqBuilder);
            JCExpression builderClassExpr = makeQualifiedTree(diagPos, localSeqBuilder);
            if (! primitive) {
                builderTypeExpr = make.at(diagPos).TypeApply(builderTypeExpr,
                        List.of(makeType(diagPos, elemType)));
                // class name -- SequenceBuilder<elemType>
                builderClassExpr = make.at(diagPos).TypeApply(builderClassExpr,
                        List.<JCExpression>of(makeType(diagPos, elemType)));
            }

            // Sequence builder temp var name "sb"
            sbName = getSyntheticName("sb");

            // Build "sb" initializing expression -- new SequenceBuilder<T>(clazz)
            JCExpression newExpr = make.at(diagPos).NewClass(
                null,                               // enclosing
                List.<JCExpression>nil(),           // type args
                builderClassExpr, // class name -- SequenceBuilder<elemType>
                makeConstructorArgs(),              // args
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

        abstract JCStatement addElement(JFXExpression expr);

        abstract List<JCExpression> makeConstructorArgs();

        JCStatement makeAdd(JCExpression expr) {
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

    //@Override
    public void visitBinary(final JFXBinary tree) {
        result = (new BinaryOperationTranslator(tree.pos(), tree) {

            protected JCExpression translateArg(JFXExpression arg, Type type) {
                return translateAsValue(arg, type);
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

    class ForExpressionTranslator extends Translator {

        final JFXForExpression tree;

        ForExpressionTranslator(JFXForExpression tree) {
            super(tree.pos());
            this.tree = tree;
        }

        private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
            JCStatement stmt = coreStmt;
            for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
                JFXForExpressionInClause clause = (JFXForExpressionInClause) tree.getInClauses().get(inx);
                stmt = new InClauseTranslator(clause, stmt).doit();
            }
            return stmt;
        }

        JCTree doit() {
            Type targetType = translationState.targetType;
            Yield yield = translationState.yield;

            // sub-translation in done inline -- no super.visitForExpression(tree);
            if (yield == ToStatement && targetType == syms.voidType) {
                return wrapWithInClause(tree, translateToStatement(tree.getBodyExpression()));
            } else {
                // body has value (non-void)
                assert tree.type != syms.voidType : "should be handled above";
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
                JCStatement stmt;
                JCExpression value;

                // Compute the element type from the sequence type
                assert tree.type.getTypeArguments().size() == 1;
                Type elemType = types.elementType(tree.type);

                UseSequenceBuilder builder = useSequenceBuilder(diagPos, elemType);
                stmts.append(builder.makeBuilderVar());

                // Build innermost loop body
                stmt = builder.addElement(tree.getBodyExpression());

                // Build the result value
                value = builder.makeToSequence();
                stmt = wrapWithInClause(tree, stmt);
                stmts.append(stmt);

                if (yield == ToStatement) {
                    stmts.append(make.at(tree).Return(value));
                    return make.at(diagPos).Block(0L, stmts.toList());
                } else {
                    // Build the block expression -- which is what we translate to
                    return makeBlockExpression(stmts, value);
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
    private class InClauseTranslator extends Translator {
        final JFXForExpressionInClause clause;      // in clause being translated
        final JFXVar var;                           // user named JavaFX induction variable
        final Type type;                            // type of the induction variable
        final JCVariableDecl inductionVar;          // generated induction variable
        JCStatement body;                           // statement being generated by wrapping
        boolean indexedLoop;

        InClauseTranslator(JFXForExpressionInClause clause, JCStatement coreStmt) {
            super(clause);
            this.clause = clause;
            this.var = clause.getVar();
            this.type = var.type;
            this.body = coreStmt;
            JFXExpression seq = clause.seqExpr;
            this.indexedLoop =
                    (seq.getFXTag() == JavafxTag.SEQUENCE_SLICE ||
                     (seq.getFXTag() != JavafxTag.SEQUENCE_RANGE &&
                      types.isSequence(seq.type)));
            this.inductionVar = makeMutableTmpVar("ind", indexedLoop ? syms.intType : type, null);
        }

        private JCVariableDecl makeTmpVar(String root, JCExpression value) {
            return makeTmpVar(root, type, value);
        }

        private JCVariableDecl makeVar(Name varName, JCExpression value) {
            return makeVar(varName, type, value);
        }

        @Override
        protected JCVariableDecl makeTmpVar(long flags, String root, Type varType, JCExpression initialValue) {
            Name varName = names.fromString(var.name.toString() + "$" + root);
            return makeVar(flags, varName, varType, initialValue);
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
            JCExpression init = translateAsValue(first, syms.intType);
            boolean maxForStartNeeded = true;
            if (first == null)
                init = makeInt(0);
            else {
                if (first.getFXTag() == JavafxTag.LITERAL && ! isNegative(first))
                    maxForStartNeeded = false;
                // FIXME set maxForStartNeeded false if first is replace-trigger startPos and seq is oldValue
                if (maxForStartNeeded)
                    setDiagPos(first);
                    init = callExpression(
                           makeQualifiedTree(first, "java.lang.Math"), "max",
                           List.of(init, makeInt(0)));
            }
            setDiagPos(clause);
            inductionVar.init = init;
            tinits.append(inductionVar);
            JCExpression sizeExpr = translateSizeof(diagPos, seq, id(seqVar));
            //callExpression(diagPos, ident(seqVar), "size");
            JCExpression limitExpr;
            // Compare the logic in makeSliceEndPos.
            if (last == null) {
                limitExpr = sizeExpr;
                if (endKind == SequenceSliceTree.END_EXCLUSIVE)
                    limitExpr = make.at(diagPos).Binary(JCTree.MINUS,
                        limitExpr, makeInt(1));
            }
            else {
                limitExpr = translateAsValue(last, syms.intType);
                if (endKind == SequenceSliceTree.END_INCLUSIVE)
                    limitExpr = makeBinary(JCTree.PLUS, limitExpr, makeInt(1));
                // FIXME can optimize if last is replace-trigger endPos and seq is oldValue
                if (true)
                    setDiagPos(last);
                    limitExpr = callExpression(
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
            inductionVar.init = translateAsValue(range.getLower(), type);
            tinits.append(inductionVar);
            // Record the upper end of the range in a final variable, and add it the the initializing statements
            JCVariableDecl upperVar = makeTmpVar("upper", translateAsValue(range.getUpper(), type));
            tinits.append(upperVar);
            // The expression which will be used in increment the induction variable
            JCExpression tstepIncrExpr;
            // The condition that will be tested each time through the loop
            JCExpression tcond;
            // The user's step expression, or null if none specified
            JFXExpression step = range.getStepOrNull();
            if (step != null) {
                // There is a user specified step expression
                JCExpression stepVal = translateAsValue(step, type);
                if (step.getFXTag() == JavafxTag.LITERAL) {
                    // The step expression is a literal, no need for a variable to hold it, and we can test if the range is scending at compile time
                    tstepIncrExpr = stepVal;
                    tcond = condTest(range, isNegative(step), upperVar);
                } else {
                    // Arbitrary step expression, do all the madness shown in the method comment
                    JCVariableDecl stepVar = makeTmpVar("step", stepVal);
                    tinits.append(stepVar);
                    tstepIncrExpr = id(stepVar);
                    JCVariableDecl negativeVar = makeTmpVar("negative", syms.booleanType, makeBinary(JCTree.LT, id(stepVar), m().Literal(type.tag, 0)));
                    tinits.append(negativeVar);
                    tcond = m().Conditional(id(negativeVar), condTest(range, true, upperVar), condTest(range, false, upperVar));
                }
            } else {
                // No step expression, use one as the increment
                tstepIncrExpr = m().Literal(type.tag, 1);
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
        public JCStatement doit() {
            // If there is a where expression, make the execution of the body conditional on the where condition
            if (clause.getWhereExpression() != null) {
                body = m().If(translateAsUnconvertedValue(clause.getWhereExpression()), body, null);
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
                            indexVarName(clause),
                            syms.javafx_IntegerType,
                            m().Unary(JCTree.POSTINC, id(incrementingIndexVar)));
                    stmts.append(finalIndexVar);
                }
                JCExpression varInit;     // Initializer for var.

                if (indexedLoop) {
                    JFXExpression sseq;
                    if (clause.seqExpr instanceof JFXSequenceSlice)
                        sseq = ((JFXSequenceSlice) clause.seqExpr).getSequence();
                    else
                       sseq = clause.seqExpr;
                    seqVar = makeTmpVar("seq", seq.type, translateAsValue(sseq, seq.type));
                    varInit = translateSequenceIndexed(diagPos, sseq, id(seqVar), id(inductionVar), type);
                }
                else
                    varInit = id(inductionVar);

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
                JCExpression tseq = translateAsUnconvertedValue(seq);
                if (types.isSequence(seq.type)) {
                    // Iterating over a non-range sequence, use a foreach loop, but first convert null to an empty sequence
                    tseq = runtime(diagPos,
                            defs.Sequences_forceNonNull,
                            List.of(makeTypeInfo(diagPos, type), tseq));
                    translateSliceInClause(seq, null, null, SequenceSliceTree.END_INCLUSIVE, seqVar);
                    //body = m().ForeachLoop(inductionVar, tseq, body);
                } else if (seq.type.tag == TypeTags.ARRAY ||
                             types.asSuper(seq.type, syms.iterableType.tsym) != null) {
                    // Iterating over an array or iterable type, use a foreach loop
                    body = m().ForeachLoop(inductionVar, tseq, body);
                } else {
                    // The "sequence" isn't aactually a sequence, treat it as a singleton.
                    // Compile: { var tmp = seq; if (tmp!=null) body; }
                    if (!type.isPrimitive()) {
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
        result = make.at(diagPos).Ident(indexVarName(tree.fname));
    }

    //@Override
    public void visitIfExpression(JFXIfExpression tree) {
        Yield yield = translationState.yield;

        final DiagnosticPosition diagPos = tree.pos();
        JCExpression cond = translateAsUnconvertedValue(tree.getCondition());
        JFXExpression trueSide = tree.getTrueExpression();
        JFXExpression falseSide = tree.getFalseExpression();
        if (yield == ToExpression) {
            Type targetType = tree.type;
            result = make.at(diagPos).Conditional(
                    cond,
                    translateAsValue(trueSide, targetType),
                    translateAsValue(falseSide, targetType));
        } else {
            Type targetType = translationState.targetType;
            result = make.at(diagPos).If(
                    cond,
                    translateToStatement(trueSide, targetType),
                    falseSide == null ? null : translateToStatement(falseSide, targetType));
        }
    }

    //@Override
    public void visitContinue(JFXContinue tree) {
        result = make.at(tree.pos).Continue(tree.label);
    }

    //@Override
    public void visitErroneous(JFXErroneous tree) {
        List<? extends JCTree> errs = translateGeneric(tree.getErrorTrees());
        result = make.at(tree.pos).Erroneous(errs);
    }

    //@Override
    public void visitReturn(JFXReturn tree) {
        JFXExpression exp = tree.getExpression();
        if (exp == null) {
            result = make.at(tree).Return(null);
        } else {
            result = translateToStatement(exp, tree.returnType);
        }
    }

    //@Override
    public void visitParens(JFXParens tree) {
        Type targetType = translationState.targetType;
        Yield yield = translationState.yield;

        if (yield == ToExpression) {
            JCExpression expr = translateAsUnconvertedValue(tree.expr);
            result = make.at(tree.pos).Parens(expr);
        } else {
            result = translateToStatement(tree.expr, targetType);
        }
    }

    //@Override
    public void visitImport(JFXImport tree) {
        //JCTree qualid = straightConvert(tree.qualid);
        //result = make.at(tree.pos).Import(qualid, tree.staticImport);
        assert false : "should be processed by parent tree";
    }

    //@Override
    public void visitInstanceOf(JFXInstanceOf tree) {
        Type type = types.boxedTypeOrType(tree.clazz.type);
        JCTree clazz = this.makeType( tree, type);
        JCExpression expr = translateAsUnconvertedValue(tree.expr);
        if (tree.expr.type.isPrimitive()) {
            expr = this.makeBox(tree.expr.pos(), expr, tree.expr.type);
        }
        if (types.isSequence(tree.expr.type) && ! types.isSequence(type))
            expr = callExpression(tree.expr,
                    makeQualifiedTree(tree.expr, "com.sun.javafx.runtime.sequence.Sequences"),
                    "getSingleValue", expr);
        result = make.at(tree.pos).TypeTest(expr, clazz);
    }

    //@Override
    public void visitTypeCast(final JFXTypeCast tree) {
        final DiagnosticPosition diagPos = tree.pos();

        JCExpression val = translateAsValue(tree.expr, tree.clazz.type);
        // The makeTypeCast below is usually redundant, since translateAsValue
        // takes care of most conversions - except in the case of a plain object cast.
        // It would be cleaner to move the makeTypeCast to translateAsValue,
        // but it's painful to get it right.  FIXME.
        JCExpression ret = typeCast(diagPos, tree.clazz.type, tree.expr.type, val);
        result = convertNullability(diagPos, ret, tree.expr, tree.clazz.type);
    }

    //@Override
    public void visitLiteral(JFXLiteral tree) {
        result = translateLiteral(tree);
    }

    //@Override
    public void visitFunctionInvocation(final JFXFunctionInvocation tree) {
        result = (new FunctionCallTranslator(tree) {
            private Name funcName = null;

            protected JCTree doit() {
                JFXExpression toCheckOrNull;
                boolean knownNonNull;

                if (useInvoke) {
                    // this is a function var call, check the whole expression for null
                    toCheckOrNull = meth;
                    funcName = defs.invokeName;
                    knownNonNull = false;
                } else {
                    Name convName = functionName(msym, superToStatic, callBound);
                    if (selector == null) {
                        // This is not an function var call and not a selector, so we assume it is a simple foo()
                        assert (meth.getFXTag() == JavafxTag.IDENT) : "Should never get here";
                        JFXIdent fr = fxm().Ident(convName);
                        fr.type = meth.type;
                        fr.sym = msym;
                        toCheckOrNull = fr;
                        funcName = null;
                        knownNonNull = true;
                    } else {
                        // Regular selector call  foo.bar() -- so, check the selector not the whole meth
                        toCheckOrNull = selector;
                        funcName = convName;
                        knownNonNull = selector.type.isPrimitive() || !selectorMutable;
                    }
                }

                return new NullCheckTranslator(diagPos, toCheckOrNull, returnType, knownNonNull) {

                    List<JCExpression> args = determineArgs();

                    JCExpression translateToCheck(JFXExpression expr) {
                        JCExpression trans;
                        if (renameToSuper || superCall) {
                           trans = id(names._super);
                        } else if (renameToThis || thisCall) {
                           trans = id(names._this);
                        } else if (superToStatic) {
                            trans = staticReference(msym);
                        } else if (selector != null && !useInvoke && msym != null && msym.isStatic()) {
                            //TODO: clean this up -- handles referencing a static function via an instance
                            trans = staticReference(msym);
                        } else {
                            if (selector != null && msym != null && !msym.isStatic()) {
                                Symbol selectorSym = expressionSymbol(selector);
                                // If this is OuterClass.memberName() then we want to
                                // to create expression to get the proper receiver.
                                if (selectorSym != null && selectorSym.kind == Kinds.TYP) {
                                    trans = makeReceiver(diagPos, msym);
                                    return trans;
                                }
                            }

                            trans = translateAsUnconvertedValue(expr);
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
                            tc = select(tc, funcName);
                        }
                        JCMethodInvocation app =  m().Apply(translateExpressions(tree.typeargs), tc, args);

                        JCExpression full = callBound ? makeBoundCall(app) : app;
                        if (useInvoke) {
                            if (tree.type.tag != TypeTags.VOID) {
                                full = castFromObject(full, tree.type);
                            }
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
                                            TODO();
                                            break;
                                        }
                                    //TODO: handle sequence subclasses
                                    //TODO: use more efficient mechanism (use currently apears rare)
                                    //System.err.println("Not match: " + arg.type + " vs " + formal.head);
                                    // Otherwise, fall-through, presumably a conversion will work.
                                    default: {
                                        targs.append(translateAsValue(arg, arg.type));
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
                                    //TODO: Lombard
                                    targ = translateAsValue(l.head, formal);
                                } else {
                                    targ = preserveSideEffects(formal, l.head, translateAsValue(l.head, formal));
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
                return TODO();
/*****
                JavafxTypeMorpher.TypeMorphInfo tmi = typeMorpher.typeMorphInfo(msym.getReturnType());
                {
                    return callExpression(
                        m().Parens(applyExpression),
                        defs.locationGetMethodName[tmi.getTypeKind()]);
                }
 *****/
            }

        }).doit();
    }

    //@Override
    public void visitModifiers(JFXModifiers tree) {
        result = make.at(tree.pos).Modifiers(tree.flags, List.<JCAnnotation>nil());
    }

    //@Override
    public void visitSkip(JFXSkip tree) {
        result = make.at(tree.pos).Skip();
    }

    //@Override
    public void visitThrow(JFXThrow tree) {
        JCTree expr = translateAsUnconvertedValue(tree.expr);
        result = make.at(tree.pos).Throw(expr);
    }

    //@Override
    public void visitTry(JFXTry tree) {
        JCBlock body = translateBlockExpressionToBlock(tree.body);
        List<JCCatch> catchers = translateCatchers(tree.catchers);
        JCBlock finalizer = translateBlockExpressionToBlock(tree.finalizer);
        result = make.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitUnary(final JFXUnary tree) {
        result = (new UnaryOperationTranslator(tree) {

            JCExpression translateExpression(JFXExpression expr, Type type) {
                return translateToExpression(expr, type);
            }
        }).doit();
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
                return callExpression(diagPos, makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                        mname, args.toList());
            }
        }
        return runtime(diagPos, defs.Sequences_size, List.of(transExpr));
    }

    //@Override
    public void visitWhileLoop(JFXWhileLoop tree) {
        JCStatement body = translateToStatement(tree.body);
        JCExpression cond = translateAsUnconvertedValue(tree.cond);
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

    //@Override
    public void visitTypeArray(JFXTypeArray tree) {
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

    protected String getSyntheticPrefix() {
        return "jfx$";
    }

    JCBlock translatedOnReplaceBody(JFXOnReplace onr) {
        return (onr == null)?  null : translateBlockExpressionToBlock(onr.getBody());
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
                            hasOuters.add(cs);
                        }
                    }
                }
            }
        }

        new FillClassesWithOuters().scan(tree);
    }

    //@Override
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        result = makeDurationLiteral(tree.pos(), translateAsUnconvertedValue(tree.value));
   }

    abstract class InterpolateValueTranslator extends NewBuiltInInstanceTranslator {

        final JFXInterpolateValue tree;

        InterpolateValueTranslator(JFXInterpolateValue tree) {
            super(tree.pos(), syms.javafx_KeyValueType);
            this.tree = tree;
            this.tree.value = tree.funcValue;
        }

        protected abstract JCExpression translateTarget();

        protected void initInstanceVariables(Name instName) {
            // value
            setInstanceVariable(instName, defs.valueName, tree.value);

            // interpolator kind
            if (tree.interpolation != null) {
                VarSymbol vsym = varSym(defs.interpolateName);
                setInstanceVariable(instName, JavafxBindStatus.UNBOUND, vsym, tree.interpolation);
            }

            // target -- convert to Pointer
            setInstanceVariable(tree.attribute.pos(), instName, JavafxBindStatus.UNBOUND, varSym(defs.targetName), translateTarget());
        }
    }

    public void visitInterpolateValue(final JFXInterpolateValue tree) {
        result = new InterpolateValueTranslator(tree) {

            protected JCExpression translateTarget() {
                JCExpression target = translateAsUnconvertedValue(tree.attribute);
                return callExpression(makeType(syms.javafx_PointerType), "make", target);
            }
        }.doit();
    }

    public void visitKeyFrameLiteral(final JFXKeyFrameLiteral tree) {
        result = new NewBuiltInInstanceTranslator(tree.pos(), syms.javafx_KeyFrameType) {

            protected void initInstanceVariables(Name instName) {
                // start time
                setInstanceVariable(instName, defs.timeName, tree.start);

                // key values -- as sequence
                JCExpression values = new ExplicitSequenceTranslator(
                        tree.pos(),
                        tree.getInterpolationValues(),
                        syms.javafx_KeyValueType
                ).doit();
                setInstanceVariable(tree.pos(), instName, varSym(defs.valuesName), values);
            }
        }.doit();
    }
}
