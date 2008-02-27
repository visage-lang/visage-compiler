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
import javax.lang.model.type.TypeKind;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
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
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.JavafxClassModel;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedAttributeInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedOverrideAttributeInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.BindAnalysis;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

public class JavafxToJava extends JCTree.Visitor implements JavafxVisitor {
    protected static final Context.Key<JavafxToJava> jfxToJavaKey =
        new Context.Key<JavafxToJava>();

    /*
     * the result of translating a tree by a visit method
     */
    JCTree result;

    /*
     * modules imported by context
     */
    private final JavafxTreeMaker make;  // should be generating Java AST, explicitly cast when not
    private final Log log;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxSymtab syms;
    private final JavafxInitializationBuilder initBuilder;
    final JavafxTypeMorpher typeMorpher;
    private final JavafxDefs defs;
    final private JavafxResolve rs;
    
    static final private String privateAnnotationStr = "com.sun.javafx.runtime.Private";
    static final private String protectedAnnotationStr = "com.sun.javafx.runtime.Protected";
    static final private String publicAnnotationStr = "com.sun.javafx.runtime.Public";
    static final private String staticAnnotationStr = "com.sun.javafx.runtime.Static";

    /*
     * other instance information
     */
    private int syntheticNameCounter = 0;
    
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
    
    public enum Wrapped {
        InLocation,
        InNothing
    }

    public enum Convert {
        ToBound,
        Normal
    }

    public static class State {

        Wrapped wrap;
        Convert convert;

        State(Wrapped wrap, Convert convert) {
            this.wrap = wrap;
            this.convert = convert;
        }

        /**
         * Base state
         */
        State() {
            this(Wrapped.InNothing, Convert.Normal);
        }
        
        boolean wantLocation() {
            return wrap == Wrapped.InLocation;
        }
        
        boolean isBound() {
            return convert == Convert.ToBound;
        }
    }

    State state = new State();

    
    // for type morphing
    enum Yield {
        ToExpression,
        ToReturnStatement,
        ToExecStatement
    }

    //TODO: all these should, probably, go into a translation state class
    Yield yield = Yield.ToExpression;
    ListBuffer<JCTree> bindingExpressionDefs = null;
    
    private JavafxEnv<JavafxAttrContext> attrEnv;
    private Target target;

    /*
     * static information
     */
    static final boolean generateBoundFunctions = true;
    static final boolean generateBoundVoidFunctions = false;
    static final boolean permeateBind = false;
    static final boolean generateNullChecks = true;
    
    private static final String sequencesRangeString = "com.sun.javafx.runtime.sequence.Sequences.range";
    private static final String sequencesRangeExclusiveString = "com.sun.javafx.runtime.sequence.Sequences.rangeExclusive";
    private static final String sequencesEmptyString = "com.sun.javafx.runtime.sequence.Sequences.emptySequence";
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.SequenceBuilder";
    private static final String toSequenceString = "toSequence";
    private static final String methodThrowsString = "java.lang.Throwable";
    private static final String syntheticNamePrefix = "jfx$$";
    private JFXClassDeclaration currentClass;  //TODO: this is redundant with attrEnv.enclClass
    /** Class symbols for classes that need a reference to the outer class. */
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();
        
    private Set<VarSymbol> locallyBound = null;
    
    private boolean inOperationDef = false;

    public static JavafxToJava instance(Context context) {
        JavafxToJava instance = context.get(jfxToJavaKey);
        if (instance == null)
            instance = new JavafxToJava(context);
        return instance;
    }

    protected JavafxToJava(Context context) {
        context.put(jfxToJavaKey, this);

        make = JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);
        target = Target.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
    }
    
    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends JCTree> T translate(T tree) {
        T ret;
        Yield prevYield = yield;
        yield = Yield.ToExpression; // reset to default
	if (tree == null) {
	    ret = null;
	} else {
	    tree.accept(this);
	    ret = (T)this.result;
	    this.result = null;
	}
        yield = prevYield;
        return ret;
    }
    
    public JCExpression translate(JCExpression tree, Type type) {
        if (tree == null)
            return null;
        if (types.isSequence(tree.type) && types.isArray(type)) {
             ListBuffer<JCStatement> stats = ListBuffer.lb();
            DiagnosticPosition diagPos = tree.pos();
            JCExpression init = (JCExpression) translate(tree);
            Type elemType = types.elemtype(type);
            if (elemType.isPrimitive()) {
                String mname = "toArray";
                if (elemType == syms.floatType)
                    mname = "toFloatArray";
                return callExpression(diagPos, makeTypeTree(syms.javafx_SequencesType, diagPos, false),
                       mname, init);
            }
            JCVariableDecl tmpVar = makeTmpVar(diagPos, tree.type, init);
            stats.append(tmpVar);
            JCVariableDecl arrVar = makeTmpVar(diagPos, "arr", type, 
                    make.NewArray(makeTypeTree(elemType, diagPos, true),
                        List.<JCExpression>of(callExpression(diagPos, make.Ident(tmpVar.name), "size")), null));
            stats.append(arrVar);
            stats.append(callStatement(diagPos, make.Ident(tmpVar.name), "toArray",
                            List.of(make.Ident(arrVar.name), make.Literal(TypeTags.INT, 0))));
            JCIdent ident2 = make.Ident(arrVar.name);
            return makeBlockExpression(diagPos, stats, ident2);
            
        }
        
        JCExpression ret = translate(tree);

        Type paramType = tree.type;
        if (paramType == syms.javafx_IntegerType ||
                type == syms.javafx_IntegerType) {
            if (paramType != type && type.isPrimitive()) {
                ret = make.at(tree).TypeCast(type, ret);
            }
        }

        return ret;
    }

    /** Visitor method: translate a list of nodes.
     */
    public <T extends JCTree> List<T> translate(List<T> trees) {
        ListBuffer<T> translated = ListBuffer.lb();
	if (trees == null) return null;
	for (List<T> l = trees; l.nonEmpty();  l = l.tail) {
            T tree = translate(l.head);
            if (tree != null) {
                translated.append( tree );
            }
        }
	return translated.toList();
    }
    
     public List<JCExpression> translate(List<JCExpression> trees, List<Type> types) {
        ListBuffer<JCExpression> translated = ListBuffer.lb();
	if (trees == null) return null;
        List<Type> t = types;
	for (List<JCExpression> l = trees; l.nonEmpty();  l = l.tail, t = t.tail) {
            JCExpression tree = translate(l.head, t.head);
            if (tree != null) {
                translated.append( tree );
            }
        }
	return translated.toList();
    }
     
     /**
     * Translate a list of statements, 
     * The purpose of this method is to prepend an anonymous class definition in the correct
     * scope.  However, that is currently disabled since generated classes define static members
     * and that isn't allowed by an (unmodified) javac back-end.
     * */
    private List<JCStatement> translateStatements(List<JCStatement> stmts) {
        ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
        prependToStatements = ListBuffer.lb();
        List<JCStatement> translatedStats = translate(stmts);
        translatedStats = translatedStats.prependList(prependToStatements.toList());
        prependToStatements = prevPrependToStatements;
        return translatedStats;
    }
    
    //// Translation with state transition
    
    public <T extends JCTree> T translate(T tree, State newState) {
        State prevState = state;
        state = newState;
        T ret = translate(tree);
        state = prevState;
        return ret;
    }

    public <T extends JCTree> List<T> translate(List<T> trees, State newState) {
        State prevState = state;
        state = newState;
        List<T> ret = translate(trees);
        state = prevState;
        return ret;
    }

    JCStatement translateExpressionToStatement(JCExpression expr) {
        assert yield != Yield.ToExpression;
	if (expr == null) {
	    return null;
	} else {
	    expr.accept(this);
	    JCTree ret = this.result;
	    this.result = null;
	    return ret instanceof JCStatement? 
                (JCStatement)ret  // already converted
                : toCorrectReturn(expr, (JCExpression)ret );
	}
    }

    JCStatement translateExpressionToStatement(JCExpression expr, State newState, Type targetType) {
        State prevState = state;
       Yield prevYield = yield;
        yield = targetType==syms.voidType? Yield.ToExecStatement : Yield.ToReturnStatement;
        state = newState;
       JCStatement ret = translateExpressionToStatement(expr);
        yield = prevYield;
        state = prevState;
        return ret;
    }

    JCStatement translateExpressionToStatement(JCExpression expr, Type targetType) {
        return translateExpressionToStatement(expr, state,  targetType);
    }

   private  <T extends JCTree> T translate(T tree, Wrapped wrap, Convert convert) {
        return translate(tree, new State(wrap, convert));
    }

    private JCStatement translateExpressionToStatement(JCExpression expr, Wrapped wrap, Convert convert, Type asType) {
        return translateExpressionToStatement(expr, new State(wrap, convert), asType);
    }

    public  <T extends JCTree> T translate(T tree, Wrapped wrap) {
        return translate(tree, wrap, state.convert);
    }

    public <T extends JCTree> T translate(T tree, Convert convert) {
        return translate(tree, state.wrap, convert);
    }

    JCBlock asBlock(JCStatement stmt) {
        if (stmt.getTag() == JavafxTag.BLOCK) {
            return (JCBlock)stmt;
        } else {
            return make.at(stmt).Block(0L, List.of(stmt));
        }
    }
    
    JCStatement toCorrectReturn(JCExpression expr, JCExpression translated) {
        switch (yield) {
            case ToExecStatement:
                return make.at(expr).Exec( translated );
            case ToReturnStatement:
                return make.at(expr).Return( translated );
            case ToExpression:
            default:
                assert false : "this method should not be called";
                return null;
        }
    }
    
    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;
        syntheticNameCounter = 0;
        
        attrEnv.toplevel = translate(attrEnv.toplevel);
    }

    interface Translator {
        JCTree doit();
    }

    /**
     * Fixup up block before translation.
     * For now this is only done for the run methods's block, and all we do is
     * convert to static variables that are accessed by other methods or classes.
     * In the future, I also want to:
     * (a) pre-allocate variables that need morphing ta the top of the block,
     * so we can do forward references.
     * (b) If a variable is final and initialized by an object literal,
     * invoke the Java constructor first, so we can make cyclic data structures. 
     * 
     * @param body The block to mogrify.
     * @param module If non-null, the module class whose body this is.
     */
    public void fixupBlockExpression (JFXBlockExpression body, JFXClassDeclaration module) {
        boolean changed = false;
        ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();

        for (JCStatement stat : body.stats) {
            if (stat instanceof JFXVar) {
                JFXVar decl = (JFXVar) stat;
                if ((decl.sym.flags_field & JavafxFlags.INNER_ACCESS) != 0) {
                   decl.sym.flags_field |= STATIC;
                   changed = true;
                   JCExpression init = decl.init;
                   if (init != null) {
                        Name name = decl.name;
                        make.at(decl);
                        JavafxBindStatus bindStatus = decl.getBindStatus();
                        if (bindStatus != JavafxBindStatus.UNBOUND)
                            init = make.BindExpression(init, bindStatus);
                        JCIdent lhs = make.Ident(name);
                        decl.sym.owner = module.type.tsym;
                        lhs.sym = decl.sym;
                        stats.append(make.Exec(make.Assign(lhs, init)));
                        decl.init = null;
                    }
                   module.setMembers(module.getMembers().prepend(decl));
                   continue;
                }
            }
            stats.append(stat);
        }
        if (changed) {
            body.stats = stats.toList();
        }
    }

    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        for (JCTree def : tree.defs) {
            if (def instanceof JFXClassDeclaration) {
                JFXClassDeclaration cdecl = (JFXClassDeclaration) def;
                JFXFunctionDefinition runMethod = cdecl.runMethod;
                if (runMethod != null) {
                    fixupBlockExpression(runMethod.getBodyExpression(), cdecl);
                }
            }
        }
        // add to the hasOuters set the clas symbols for classes that need a reference to the outer class
        fillClassesWithOuters(tree);
        
       ListBuffer<JCTree> translatedDefinitions= ListBuffer.lb();
       ListBuffer<JCTree> imports= ListBuffer.lb();
       additionalImports = ListBuffer.lb();
       prependToStatements = prependToDefinitions = ListBuffer.lb();
       for (JCTree def : tree.defs) {
            if (def.getTag() == JCTree.IMPORT) {
                imports.append(def);
                 if (!((JCImport)def).isStatic()) {
                    if (((JCImport)def).getQualifiedIdentifier().getTag() == JCTree.SELECT) {
                        JCFieldAccess select = (JCFieldAccess)((JCImport)def).getQualifiedIdentifier();
                        if (select.name != names.asterisk &&
                                types.isCompoundClass(select.sym)) {
                           imports.append(make.Import(make.Select(
                                        translate( select.selected ), 
                                        names.fromString(select.name.toString() + interfaceSuffix)), 
                                   false));
                        }
                    }  else if (((JCImport)def).getQualifiedIdentifier().getTag() == JCTree.IDENT) {
                        JCIdent ident = (JCIdent)((JCImport)def).getQualifiedIdentifier();
                        if (ident.name != names.asterisk &&
                                types.isCompoundClass(ident.sym)) {
                            imports.append(make.Import(make.Ident(names.fromString(ident.name.toString() + interfaceSuffix)), false));
                        }
                    }
                }
            } else {
                // anything but an import
                translatedDefinitions.append( translate(def) );
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

 	JCExpression pid = tree.pid;  //translate(tree.pid);
        JCCompilationUnit translated = make.at(tree.pos).TopLevel(List.<JCAnnotation>nil(), pid, translatedDefinitions.toList());
        translated.sourcefile = tree.sourcefile;
        translated.docComments = tree.docComments;
        translated.lineMap = tree.lineMap;
        translated.flags = tree.flags;
        result = translated;
   }
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        JFXClassDeclaration prevClass = currentClass;
        JFXClassDeclaration prevEnclClass = attrEnv.enclClass;
        State prevState = state;
        state = new State();
        currentClass = tree;

        new ForEachInClauseOwnerFixer().scan(tree);

        boolean prevInOper = inOperationDef;
        try {
            inOperationDef = false;
            DiagnosticPosition diagPos = tree.pos();

            attrEnv.enclClass = tree;

            ListBuffer<JCStatement> translatedInitBlocks = ListBuffer.lb();
            ListBuffer<JCStatement> translatedPostInitBlocks = ListBuffer.lb();
            ListBuffer<JCTree> translatedDefs = ListBuffer.lb();
            ListBuffer<TranslatedAttributeInfo> attrInfo = ListBuffer.lb();
            ListBuffer<TranslatedOverrideAttributeInfo> overrideInfo = ListBuffer.lb();

           // translate all the definitions that make up the class.
           // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JCTree def : tree.getMembers()) {
                    switch(def.getTag()) {
                        case JavafxTag.INIT_DEF: {
                            inOperationDef = true;
                            translatedInitBlocks.append(translate((JCBlock) ((JFXInitDefinition) def).getBody()));
                            inOperationDef = false;
                            break;
                        }
                        case JavafxTag.POSTINIT_DEF: {
                            inOperationDef = true;
                            translatedPostInitBlocks.append(translate((JCBlock) ((JFXPostInitDefinition) def).getBody()));
                            inOperationDef = false;
                            break;
                        }
                        case JavafxTag.VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            boolean isStatic = (attrDef.getModifiers().flags & STATIC) != 0;
                            JCStatement init = translateDefinitionalAssignmentToSet(attrDef.pos(), 
                                attrDef.init, attrDef.getBindStatus(), attrDef.sym,
                                isStatic? null : make.Ident(defs.receiverName), FROM_DEFAULT_MILIEU);
                            attrInfo.append(new TranslatedAttributeInfo(
                                attrDef,
                                typeMorpher.varMorphInfo(attrDef.sym),
                                init,
                                translate(attrDef.getOnChanges())));
                            //translatedDefs.append(trans);
                            break;
                        }
                        case JavafxTag.OVERRIDE_ATTRIBUTE_DEF: {
                            JFXOverrideAttribute override = (JFXOverrideAttribute) def;
                            boolean isStatic = (override.sym.flags() & STATIC) != 0;
                            JCStatement init;
                            if (override.getInitializer() == null) {
                                init = null;
                            } else {
                                init = translateDefinitionalAssignmentToSet(override.pos(),
                                    override.getInitializer(), override.getBindStatus(), override.sym,
                                    isStatic? null : make.Ident(defs.receiverName), 
                                    FROM_DEFAULT_MILIEU);
                            }
                            overrideInfo.append(new TranslatedOverrideAttributeInfo(
                                    override,
                                    typeMorpher.varMorphInfo(override.sym),
                                    init,
                                    translate(override.getOnReplace())));
                            break;
                        }
                       case JavafxTag.FUNCTION_DEF : {
                           JFXFunctionDefinition funcDef = (JFXFunctionDefinition)def;
                            translatedDefs.append(  translate(funcDef) );
                            if (generateBoundFunctions  && (generateBoundVoidFunctions || funcDef.type.getReturnType() != syms.voidType)) {
                                if ((funcDef.sym.flags() & Flags.SYNTHETIC) == 0) {
                                    translatedDefs.append(translate(funcDef, Convert.ToBound));
                                }
                            }
                            break;
                        }
                       case JCTree.METHODDEF : {
                            assert false : "translated method should never appear in an FX class";
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

            boolean classOnly = tree.generateClassOnly();
            JavafxClassModel model = initBuilder.createJFXClassModel(tree, attrInfo.toList(), overrideInfo.toList());
            additionalImports.appendList(model.additionalImports);
       
            boolean classIsFinal = (tree.getModifiers().flags & Flags.FINAL) != 0;
            
            // include the interface only once
            if (!tree.hasBeenTranslated) {
                if (! classOnly) {
                    JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.INTERFACE);
                    mods = addAccessAnnotationModifiers(tree.mods.flags, mods, (JavafxTreeMaker)make);

                    JCClassDecl cInterface = make.ClassDef(mods,
                            model.interfaceName, List.<JCTypeParameter>nil(), null,
                            model.interfaces, model.iDefinitions);

                    prependToDefinitions.append(cInterface); // prepend to the enclosing class or top-level
                }
                tree.hasBeenTranslated = true;
            }
            
            translatedDefs.appendList(model.additionalClassMembers);

            {
                // Add the userInit$ method
                List<JCVariableDecl> receiverVarDeclList = List.of(makeReceiverParam(tree));
                ListBuffer<JCStatement> initStats = ListBuffer.lb();
                // call the superclasses userInit$
                Set<String> dupSet = new HashSet<String>();
                for (JCExpression parent : tree.getExtending()) {
                    if (! (parent instanceof JCIdent))
                        continue;
                    Symbol symbol = expressionSymbol(parent);
                    if (types.isJFXClass(symbol)) {
                        ClassSymbol cSym = (ClassSymbol) symbol;
                        String className = cSym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree(cSym.type, tree.pos(), true), make.Ident(defs.receiverName)));
                            initStats = initStats.append(callStatement(tree.pos(), ((JavafxTreeMaker)make).Identifier(className), initBuilder.userInitName, args1));
                        }
                    }
                }
                initStats.appendList(translatedInitBlocks);

                JCBlock userInitBlock = make.Block(0L, initStats.toList());
                translatedDefs.append(make.MethodDef(
                        make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC), 
                        initBuilder.userInitName, 
                        makeTypeTree(syms.voidType, null), 
                        List.<JCTypeParameter>nil(), 
                        receiverVarDeclList, 
                        List.<JCExpression>nil(), 
                        userInitBlock, 
                        null));
            }
            {
                // Add the userPostInit$ method
                List<JCVariableDecl> receiverVarDeclList = List.of(makeReceiverParam(tree));
                ListBuffer<JCStatement> initStats = ListBuffer.lb();
                // call the superclasses postInit$
                Set<String> dupSet = new HashSet<String>();
                for (JCExpression parent : tree.getExtending()) {
                    if (! (parent instanceof JCIdent))
                        continue;
                    Symbol symbol = expressionSymbol(parent);
                    if (types.isJFXClass(symbol)) {
                        ClassSymbol cSym = (ClassSymbol) symbol;
                        String className = cSym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree(cSym.type, tree.pos(), true), make.Ident(defs.receiverName)));
                            initStats = initStats.append(callStatement(tree.pos(), ((JavafxTreeMaker)make).Identifier(className), initBuilder.postInitName, args1));
                        }
                    }
                }
                initStats.appendList(translatedPostInitBlocks);

                JCBlock postInitBlock = make.Block(0L, initStats.toList());
                translatedDefs.append(make.MethodDef(
                        make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC),
                        initBuilder.postInitName,
                        makeTypeTree(syms.voidType, null),
                        List.<JCTypeParameter>nil(),
                        receiverVarDeclList,
                        List.<JCExpression>nil(),
                        postInitBlock,
                        null));
            }

            if (tree.isModuleClass) {
                // Add main method...
                translatedDefs.append(makeMainMethod(diagPos, tree.getName()));
            }

            // build the list of implemented interfaces
            List<JCExpression> implementing;
            if (classOnly) {
                implementing = model.interfaces;
            }
            else {
                implementing = List.of(make.Ident(model.interfaceName), makeIdentifier(fxObjectString));
            }
            
            long flags = tree.mods.flags & (Flags.PUBLIC | Flags.PRIVATE | Flags.PROTECTED | Flags.FINAL | Flags.ABSTRACT);
            if (tree.sym.owner.kind == Kinds.TYP) {
                flags |= Flags.STATIC;
            }
            
            // make the Java class corresponding to this FX class, and return it
            JCExpression jcExtending = null;
            Type superType;
            if (tree.type instanceof ClassType &&
                    (superType = ((ClassType)tree.type).supertype_field) != null &&
                    superType.tsym instanceof ClassSymbol &&
                    (superType.tsym.flags_field & JavafxFlags.COMPOUND_CLASS) == 0) {
                jcExtending = makeTypeTree(((ClassType)tree.type).supertype_field, null, false);
            }
            else if ((tree.mods.flags & Flags.FINAL) != 0L && tree.getExtending().nonEmpty()) {
                Symbol sym1 = TreeInfo.symbol(tree.getExtending().head);
                if (sym1 != null &&
                        (sym1.flags_field & JavafxFlags.COMPOUND_CLASS) == 0)
                    jcExtending = makeTypeTree(tree.getExtending().head.type, null, false);
            }
            
            JCClassDecl res = make.at(diagPos).ClassDef(
                    make.at(diagPos).Modifiers(flags),
                    tree.getName(),
                    tree.getEmptyTypeParameters(), 
                    jcExtending,
                    implementing, 
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
            result = res;
        }
        finally {
            attrEnv.enclClass = prevEnclClass;
            state = prevState;

            currentClass = prevClass;
            inOperationDef = prevInOper;
        }
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        result = null; // Just remove this tree...
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        result = null; // Just remove this tree...
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        Type type;

        ListBuffer<JCStatement> stats = ListBuffer.lb();
        for (JFXVar var : tree.getLocalvars()) {
            // force var to be a Location (so class members can see it)
            typeMorpher.varMorphInfo(var.sym).markBoundTo();
            // add the variable before the class definition or object litersl assignment
            stats.append(translate(var));
        }
        if (tree.getClassBody() == null) {
            type = tree.type;
        } else {
            JFXClassDeclaration cdef = tree.getClassBody();
            if (!inOperationDef) {
                prependToStatements.append( translate( cdef ) );  // prepend to the enclosing statements, class or top-level
            }
            else {
                stats.append(translate(cdef));
            }
            type = cdef.type;
        }
        JCExpression classTypeExpr = makeTypeTree(type, tree, false);

        List<JCExpression> newClassArgs;
        if (tree.constructor != null && tree.constructor.type != null) {
            List<Type> ptypes =
                    tree.constructor.type.asMethodType().getParameterTypes();
            newClassArgs = translate(tree.getArgs(), ptypes);
        }
        else
            newClassArgs = translate(tree.getArgs());
        if (tree.getClassBody() != null &&
            tree.getClassBody().sym != null && hasOuters.contains(tree.getClassBody().sym)) {
            JCIdent thisIdent = make.Ident(defs.receiverName);
            thisIdent.sym = tree.getClassBody().sym.owner;
            thisIdent.type = tree.getClassBody().sym.owner.type;
            
            newClassArgs = newClassArgs.prepend(thisIdent);
        }

        JCNewClass newClass = 
                make.NewClass(null, null, classTypeExpr,
                              newClassArgs,
                              null);
        
        {
            Symbol sym = TreeInfo.symbol(tree.getIdentifier());
        
            if (sym != null &&
                sym.kind == Kinds.TYP && (sym instanceof ClassSymbol) &&
                (types.isJFXClass((ClassSymbol)sym) ||
                tree.getClassBody() != null)) {
                // it is a JavaFX class, initializa it properly
                JCVariableDecl tmpVar = makeTmpVar(tree.pos(), "objlit", type, newClass);
                stats.append(tmpVar);
                for (JFXObjectLiteralPart olpart : tree.getParts()) {
                    DiagnosticPosition diagPos = olpart.pos();
                    JavafxBindStatus bindStatus = olpart.getBindStatus();
                    JCExpression init = olpart.getExpression();
                    VarSymbol vsym = (VarSymbol) olpart.sym;
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                    assert shouldMorph(vmi);

                    // Lift JFXObjectLiteralPart if needed
                    if (types.isSequence(olpart.type)) {
                        if (!types.isSequence(olpart.expr.type)) {
                             init = ((JavafxTreeMaker)make).ExplicitSequence(List.<JCExpression>of(olpart.expr));
                             WildcardType tpType = new WildcardType(olpart.expr.type, BoundKind.EXTENDS, olpart.expr.type.tsym);
                            init.type = new ClassType(((JavafxSymtab)syms).javafx_SequenceType, List.<Type>of(tpType), ((JavafxSymtab)syms).javafx_SequenceType.tsym);
                         }
                    }

                    JCIdent ident1 = make.at(diagPos).Ident(tmpVar.name);
                    stats.append( translateDefinitionalAssignmentToSet(diagPos, init, bindStatus, 
                            vsym, ident1, FROM_LITERAL_MILIEU) ); 
                }

                JCIdent ident3 = make.Ident(tmpVar.name);   
                JCStatement applyExec = callStatement(tree.pos(), ident3, defs.initializeName);
                stats.append(applyExec);

                JCIdent ident2 = make.Ident(tmpVar.name);
                result = makeBlockExpression(tree.pos(), stats, ident2);
            }
            else {
                // this is a Java class, just instanciate it
                result = newClass;
            }
       }
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        StringBuffer sb = new StringBuffer();
        List<JCExpression> parts = tree.getParts();
        ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();
        
        JCLiteral lit = (JCLiteral)(parts.head);            // "...{
        sb.append((String)lit.value);            
        parts = parts.tail;
        
        while (parts.nonEmpty()) {

            lit = (JCLiteral)(parts.head);                  // optional format (or null)
            String format = (String)lit.value;
            parts = parts.tail;
            JCExpression exp = parts.head;
            if (exp != null &&
                types.isSameType(exp.type, syms.javafx_TimeType)) {
                exp = make.Apply(null,
                                 make.Select(translate(exp), Name.fromString(names, "toDate")), 
                                 List.<JCExpression>nil());
                sb.append(format.length() == 0? "%tQms" : format);
            } else {
                exp = translate(exp);
                sb.append(format.length() == 0? "%s" : format);
            }
            values.append(exp);
            parts = parts.tail;
            
            lit = (JCLiteral)(parts.head);                  // }...{  or  }..."
            sb.append((String)lit.value);
            parts = parts.tail;
        }
        JCLiteral formatLiteral = make.at(tree.pos).Literal(TypeTags.CLASS, sb.toString());
        values.prepend(formatLiteral);
        JCExpression formatter;
        if (tree.translationKey != null) {
            formatter = make.Ident(Name.fromString(names, "com"));
            for (String s : new String[] {"sun", "javafx", "runtime", "util", "StringLocalization", "getLocalizedString"}) {
                formatter = make.Select(formatter, Name.fromString(names, s));
            }
            values.prepend(make.Literal(TypeTags.CLASS, tree.translationKey));
            String resourceName =
                attrEnv.enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
            values.prepend(make.Literal(TypeTags.CLASS, resourceName));
        } else {
            formatter = make.Ident(Name.fromString(names, "java"));
            for (String s : new String[] {"lang", "String", "format"}) {
                formatter = make.Select(formatter, Name.fromString(names, s));
            }
        }
        result = make.Apply(null, formatter, values.toList());
    }

    // Temporary hack to implement 'bind for' in a limited way.
    private JCExpression attemptBoundForExpressionHack(DiagnosticPosition diagPos, JCExpression init, VarMorphInfo vmi) {
        if (!(init instanceof JFXForExpression)) {
            return null; // cannot do hack, abort
        }
        JFXForExpression fexp = (JFXForExpression) init;
        List<JFXForExpressionInClause> fClauses = fexp.inClauses;
        JCExpression fbody = fexp.getBodyExpression();
        // Actually, bodyType is fbody.type.  But if the type
        // required by the context (vmi) is a super-type of the
        // fbody.type, when we get a type error, because the
        // various SequenceLocation types don't use the necessary
        // wildcare.  Also, using the context type avoids the
        // compiler having to create a bridge method.
        Type bodyType = vmi.getElementType();

        if (fClauses.size() != 1 || types.isSequence(bodyType) || fClauses.head.whereExpr != null) {
            return null;  // cannot do hack, abort
        }
        Type forType = elementType(fClauses.head.seqExpr.type);
        JCVariableDecl param1 = make.VarDef(make.Modifiers(Flags.FINAL), fClauses.head.var.name,
                makeTypeTree(forType, diagPos), null);
        JCVariableDecl param2 = make.VarDef(make.Modifiers(Flags.FINAL), indexVarName(fClauses.head),
                makeTypeTree(syms.javafx_IntegerType, diagPos), null);
        boolean dependsOnIndex = fClauses.head.getIndexUsed();

        JCStatement stmt = translateExpressionToStatement(fbody, Wrapped.InNothing, Convert.Normal, vmi.getRealType());
        JCMethodDecl computeElementMethod = make.at(diagPos).MethodDef(
                make.at(diagPos).Modifiers(Flags.PROTECTED),
                defs.computeElementName,
                makeTypeTree(bodyType, diagPos, true),
                List.<JCTypeParameter>nil(),
                List.<JCVariableDecl>of(param1, param2),
                List.<JCExpression>nil(),
                make.Block(0L, List.of(stmt)),
                null);
        JCExpression t = makeQualifiedTree(null,
                "com.sun.javafx.runtime.sequence.SimpleBoundComprehension");
        JCExpression clazz = make.TypeApply(t, List.<JCExpression>of(makeTypeTree(forType, diagPos),
                makeTypeTree(bodyType, diagPos)));
        JCClassDecl cl = make.AnonymousClassDef(make.Modifiers(0),
                List.<JCTree>of(computeElementMethod));
        List<JCExpression> args = List.<JCExpression>of(
                make.at(diagPos).Select(makeTypeTree(bodyType, diagPos, true), names._class),
                translate(fClauses.head.seqExpr, Wrapped.InLocation, Convert.ToBound),
                make.at(diagPos).Literal(TypeTags.BOOLEAN, dependsOnIndex ? 1 : 0));

        return make.NewClass(null/*encl*/, List.<JCExpression>nil(), clazz, args, cl);
    }
    
    private List<JCExpression> translateDefinitionalAssignmentToArgs(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarMorphInfo vmi) {
        if (bindStatus.isUnidiBind()) {
            assert (shouldMorph(vmi));

            {
                JCExpression forLocation = attemptBoundForExpressionHack(diagPos, init, vmi);
                if (forLocation != null) {
                    return List.of(forLocation);
                }
            }
            // normal case (no for expression binding hack)
            ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
            bindingExpressionDefs = ListBuffer.lb();

            JCStatement stmt = translateExpressionToStatement(init, Wrapped.InNothing, Convert.ToBound, vmi.getRealType());
            JCExpression binding = typeMorpher.buildExpression(diagPos, vmi, stmt);

            ListBuffer<JCExpression> argValues = ListBuffer.lb();
            argValues.append(make.Literal(TypeTags.BOOLEAN, bindStatus.isLazy() ? 1 : 0));
            argValues.append(binding);
            argValues.appendList( typeMorpher.buildDependencies(init) );

            assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
            bindingExpressionDefs = prevBindingExpressionDefs;
            return argValues.toList();
        } else if (bindStatus.isBidiBind()) {
            assert (shouldMorph(vmi));
            // Bi-directional bind translate so it stays in a Location
            return List.<JCExpression>of( translate(init, Wrapped.InLocation, Convert.ToBound) );
        } else {
            JCExpression initExpr;
            // normal init -- unbound -- but it is setting a Location
            if (init == null) {
                // no initializing expression determine a default value from the type
                initExpr = makeDefaultValue(diagPos, vmi);
            } else {
                // do a vanilla translation of the expression
                initExpr = translate(init, new State());
            }
            return List.<JCExpression>of( initExpr );
        }
    }

    private JCExpression translateDefinitionalAssignmentToValue(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym) {
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        List<JCExpression> args = translateDefinitionalAssignmentToArgs(diagPos, init, bindStatus, vmi);
        Name makeName = defs.makeMethodName;
        if (bindStatus.isBidiBind()) {
            makeName = defs.makeBijectiveMethodName;
        }
        if (shouldMorph(vmi)) {
            return typeMorpher.makeLocationVariable(vmi, diagPos, args, makeName);
        } else {
            assert (!bindStatus.isBound());
            return args.head;
        }
    }

    private JCStatement translateDefinitionalAssignmentToSet(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            JCExpression instance, int milieu) {
        return make.at(diagPos).Exec( translateDefinitionalAssignmentToSetExpression(diagPos,
            init, bindStatus, vsym,
             instance, milieu) );
    }

    private JCExpression translateDefinitionalAssignmentToSetExpression(DiagnosticPosition diagPos,
            JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym,
            JCExpression instance, int milieu) {
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        List<JCExpression> args = translateDefinitionalAssignmentToArgs(diagPos, init, bindStatus, vmi);
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

    JCExpression makeUnboundLocation(DiagnosticPosition diagPos, TypeMorphInfo tmi, JCExpression expr) {
        List<JCExpression> makeArgs = List.of(expr);
        return typeMorpher.makeLocationLocalVariable(tmi, diagPos, makeArgs);
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

        JCExpression binding = typeMorpher.buildExpression(diagPos, tmi, stmt);
        ListBuffer<JCExpression> argValues = ListBuffer.lb();
        argValues.append(make.Literal(TypeTags.BOOLEAN, isLazy ? 1 : 0));
        argValues.append(binding);
        argValues.appendList(staticDependencies);
        return typeMorpher.makeLocationLocalVariable(tmi, diagPos, argValues.toList());
    }

    JCExpression makeSequence(DiagnosticPosition diagPos, JCExpression expr, Type type) {
        List<JCExpression> makeArgs =
            List.of(expr).prepend(  makeSequenceClassArg(diagPos, type, false));
        JCExpression sequenceExpr = makeTypeTree(syms.javafx_SequencesType, diagPos, false);
        JCFieldAccess makeSelect = make.at(diagPos).Select(sequenceExpr, defs.makeMethodName);

        List<JCExpression> typeArgs = List.of(makeTypeTree(type, diagPos, false));
        return make.at(diagPos).Apply(typeArgs, makeSelect, makeArgs);
    }

    JCExpression makeSequenceClassArg(DiagnosticPosition diagPos, Type type, boolean makeInterface) {
        return make.Select(
                makeTypeTree(type, diagPos, makeInterface),
                names.fromString("class"));
    }

    JCExpression makeSequenceClassArg(DiagnosticPosition diagPos, TypeMorphInfo tmi) {
        return make.Select(
                makeTypeTree(tmi.getElementType(), diagPos, true),
                names.fromString("class"));
    }

    JCExpression makeDefaultValue(DiagnosticPosition diagPos, TypeMorphInfo tmi) {
        return tmi.getTypeKind() == TYPE_KIND_SEQUENCE ? 
                makeEmptySequenceCreator(diagPos, tmi.getElementType()) : 
                                tmi.getRealType() == syms.javafx_StringType? make.Literal("") :
                                typeMorpher.makeLit(tmi.getRealType(), tmi.getDefaultValue(), diagPos);
    }

    @Override
    public void visitVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();
        Type type = tree.type;
        JCModifiers mods = tree.getModifiers();
        long modFlags = mods == null ? 0L : mods.flags;
        VarSymbol vsym = tree.sym;
        boolean isClassVar = vsym.owner.kind == Kinds.TYP;
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        boolean isBound = state.isBound();
        boolean forceTypeMorph = isBound;

        if (!isClassVar && (vsym.flags_field & JavafxFlags.INNER_ACCESS) != 0) {
            if ((vsym.flags_field & JavafxFlags.ASSIGNED_TO) == 0) {
                modFlags |= Flags.FINAL;
            } else {
                vmi.markBoundTo();
                forceTypeMorph = true;
            }
        }
        if (shouldMorph(vmi) || forceTypeMorph) {
            // convert the type to the Location type
            type = vmi.getMorphedType();

            // Locations are never overwritten
            modFlags |= Flags.FINAL;
        }

        mods = make.at(diagPos).Modifiers(modFlags);
        JCExpression typeExpression = makeTypeTree(type, diagPos, true);

        // for class vars, initialization happens during class init, so set to
        // default Location.  For local vars translate as definitional
        JCExpression init;
        if (isClassVar) {
            // use default constructor to init attributes
            init = typeMorpher.makeLocationAttributeVariable(vmi, diagPos);
        } else if ((vsym.flags() & Flags.PARAMETER) != 0) {
            // parameters aren't initialized
            init = null;
        } else {
            if (isBound && locallyBound != null) {
                locallyBound.add(vsym);
            }
            init = translateDefinitionalAssignmentToValue(tree.pos(), tree.init,
                    tree.getBindStatus(), tree.sym);
        }

        //TODO: this is broken: init doesn't have type
        // Convert initializers returning Java arrays to sequences.
        if (type.tag == TypeTags.ARRAY) {
            JCExpression newTree = ((JCExpression)makeTypeTree(((ArrayType)type).elemtype, diagPos, types.isCompoundClass(((ArrayType)type).elemtype.tsym)));
            newTree.type = ((ArrayType)type).elemtype;
            WildcardType tpType = new WildcardType(newTree.type, BoundKind.EXTENDS, type.tsym);
            ClassType classType = new ClassType(((JavafxSymtab)syms).javafx_SequenceType, List.<Type>of(tpType), ((JavafxSymtab)syms).javafx_SequenceType.tsym);
            typeExpression = makeTypeTree(classType, diagPos, false);

            if (init.type != ((JavafxSymtab)syms).javafx_SequenceType) {
                init = makeSequence(diagPos, init, newTree.type);
            }
        }

        result = make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);
    }

    @Override
    public void visitOverrideAttribute(JFXOverrideAttribute tree) {
        // handled in visitClassDeclaration
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        boolean prev = inOperationDef;
        try {
            inOperationDef = true;
            result = ((JavafxTreeMaker)make).OnReplace(
                tree.getOldValue(), tree.getIndex(), tree.getLastIndex(),
                tree.getEndKind(), tree.getNewElements(),
                translate(tree.getBody()));
        }
        finally {
            inOperationDef = prev;
        }
    }
    
    @Override
    public void visitOnReplaceElement(JFXOnReplaceElement tree) {
        boolean prev = inOperationDef;
        try {
            inOperationDef = true;
            result = ((JavafxTreeMaker)make).OnReplaceElement(
                tree.getIndex(),
                tree.getOldValue(),
                translate(tree.getBody()));
        }
        finally {
            inOperationDef = prev;
        }
    }
    
    @Override
    public void visitOnInsertElement(JFXOnInsertElement tree) {
        boolean prev = inOperationDef;
        try {
            inOperationDef = true;
            result = ((JavafxTreeMaker)make).OnInsertElement(
                tree.getIndex(),
                tree.getOldValue(),  // new
                translate(tree.getBody()));
        }
        finally {
            inOperationDef = prev;
        }
    }
    
    @Override
    public void visitOnDeleteElement(JFXOnDeleteElement tree) {
        boolean prev = inOperationDef;
        try {
            inOperationDef = true;
            result = ((JavafxTreeMaker)make).OnDeleteElement(
                tree.getIndex(),
                tree.getOldValue(),
                translate(tree.getBody()));
        }
        finally {
            inOperationDef = prev;
        }
    }

    @Override
    public void visitOnDeleteAll(JFXOnDeleteAll tree) {
        assert false : "not yet implemented -- may not be";
    }

    @Override
    public void visitOperationValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }
    
   JCExpression makeFunctionValue (JCExpression meth, JFXFunctionDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        if (def != null)
            members.append(translate(def));
        JCExpression encl = null;
        List<JCExpression> args = List.nil();
        int nargs = mtype.argtypes.size();
        Type ftype = syms.javafx_FunctionTypes[nargs];
        JCExpression t = makeQualifiedTree(null, ftype.tsym.getQualifiedName().toString());
        ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
        Type rtype = syms.boxIfNeeded(mtype.restype);
        typeargs.append(makeTypeTree(rtype, diagPos));
        ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
        int i = 0;
        for (List<Type> l = mtype.argtypes;  l.nonEmpty();  l = l.tail) {
            Name pname = make.paramName(i++);
            Type ptype = syms.boxIfNeeded(l.head);
            JCVariableDecl param = make.VarDef(make.Modifiers(0), pname,
                    makeTypeTree(ptype, diagPos), null);
            params.append(param);
            JCExpression marg = make.Ident(pname);
            margs.append(marg);
            typeargs.append(makeTypeTree(ptype, diagPos));
        }

        // The backend's Attr skips SYNTHETIC methods when looking for a matching method.
        long flags = PUBLIC | BRIDGE; // | SYNTHETIC;

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
                makeTypeTree(rtype, diagPos),
                List.<JCTypeParameter>nil(),
                params.toList(),
                make.at(diagPos).Types(mtype.getThrownTypes()),
                make.Block(0, stats), 
                null);

        members.append(bridgeDef);
        JCClassDecl cl = make.AnonymousClassDef(make.Modifiers(0), members.toList());
        return make.NewClass(encl, args, make.TypeApply(t, typeargs.toList()), args, cl);
    }
    
   boolean isInnerFunction (MethodSymbol sym) {
       return sym.owner != null && sym.owner.kind != Kinds.TYP
                && (sym.flags() & Flags.SYNTHETIC) == 0;
   }
           
   /**
    * Translate JavaFX a class definition into a Java static implementation method.
    */
   @Override
    public void visitOperationDefinition(JFXFunctionDefinition tree) {
        if (isInnerFunction(tree.sym)) {
            // If tree's context is not a class, then translate:
            //   function foo(args) { body }
            // to: final var foo = function(args) { body };
            // A probably cleaner and possibly more efficient solution would
            // be to create an anonymous class containing the function as
            // a method; closed-over local variables become fields.
            // That would have the advantage that calling the function directly
            // translates into a direct method call, rather than going via
            // Function's invoke method, which implies extra casts, possibly
            // boxing, etc.  FIXME
            DiagnosticPosition diagPos = tree.pos();
            MethodType mtype = (MethodType) tree.type;
            JCExpression typeExpression = makeTypeTree(syms.makeFunctionType(mtype), diagPos, true);
            JFXFunctionDefinition def = new JFXFunctionDefinition(make.Modifiers(Flags.SYNTHETIC), tree.name, tree.operation);
            def.type = mtype;
            def.sym = new MethodSymbol(0, def.name, mtype, tree.sym.owner.owner);
            JCExpression init =
               makeFunctionValue(make.Ident(tree.name), def, tree.pos(), mtype);
            JCModifiers mods = make.at(diagPos).Modifiers(Flags.FINAL);
            result = make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);
            return;
        }
        boolean prevInOperDef = inOperationDef;
        inOperationDef = true;

        boolean isBound = state.isBound();
        State prevState = state;
        state = new State();

        try {
            boolean classOnly = currentClass.generateClassOnly();
            // Made all the operations public. Per Brian's spec.
            // If they are left package level it interfere with Multiple Inheritance
            // The interface methods cannot be package level and an error is reported.
            long flags = tree.mods.flags;
            long originalFlags = flags;
            flags &= ~(Flags.PROTECTED | Flags.PRIVATE);
            flags |=  Flags.PUBLIC;
            if (((flags & (Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) && !classOnly) {
                flags |= Flags.STATIC;
            }
            flags &= ~Flags.SYNTHETIC;
            JCModifiers mods = make.Modifiers(flags);     
            boolean isImplMethod = (originalFlags & (Flags.STATIC | Flags.ABSTRACT | Flags.SYNTHETIC)) == 0 && !classOnly;

            DiagnosticPosition diagPos = tree.pos();
            MethodType mtype = (MethodType)tree.type;
            
            if (isBound) {
                locallyBound = new HashSet<VarSymbol>();
                for (JFXVar fxVar : tree.getParameters()) {
                    locallyBound.add(fxVar.sym);
                }
            }

           // construct the body of the translated function
             JFXBlockExpression bexpr = tree.getBodyExpression();
            JCBlock body; 
            if (bexpr == null) {
                body = null; // null if no block expression
            } else {
                if (isBound) {
                    // bound function return value
                    body = boundMethodBody( diagPos, bexpr, tree );
                } else if (isRunMethod(tree.sym)) {
                    // it is a module level run method, do special translation
                    body = runMethodBody( bexpr );
                    isImplMethod = false;
                } else {
                    // the "normal" case
                    body = asBlock( translateExpressionToStatement(bexpr, mtype.getReturnType()) );
                }
            }

            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            if ((originalFlags & (Flags.STATIC | Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) {
                if (classOnly) {
                    // all implementation code is generated assuming a receiver parameter.
                    // in the final class case, there is no receiver param, so allow generated code
                    // to function by adding:   var receiver = this;
                    body.stats = body.stats.prepend(
                            make.at(diagPos).VarDef(
                                make.at(diagPos).Modifiers(Flags.FINAL), 
                                defs.receiverName, 
                                make.Ident(initBuilder.interfaceName(currentClass)), 
                                make.at(diagPos).Ident(names._this))
                            );
                } else {
                    // if we are converting a standard instance function (to a static method), the first parameter becomes a reference to the receiver
                    params.prepend(makeReceiverParam(currentClass));
                }
            }
            for (JFXVar fxVar : tree.getParameters()) {
                JCVariableDecl var = (JCVariableDecl)translate(fxVar);
                params.append(var);
            }
            
            mods = addAccessAnnotationModifiers(tree.mods.flags, mods, (JavafxTreeMaker)make);

            result = make.at(diagPos).MethodDef(
                    mods,
                    functionName(tree.sym, isImplMethod,  isBound), 
                    makeReturnTypeTree(diagPos, tree.sym, isBound), 
                    make.at(diagPos).TypeParams(mtype.getTypeArguments()), 
                    params.toList(),
                    make.at(diagPos).Types(mtype.getThrownTypes()),  // makeThrows(diagPos), //
                    body, 
                    null);
            ((JCMethodDecl)result).sym = tree.sym;
            result.type = tree.type;
        }
        finally {
            state = prevState;
            inOperationDef = prevInOperDef;
        }
    }
   
    public void visitBindExpression(JFXBindExpression tree) {
         throw new AssertionError(tree);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        DiagnosticPosition diagPos = tree.pos();
        ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
        prependToStatements = ListBuffer.lb();
        
        JCExpression value = tree.value;
        boolean valueFromReturn = (value == null) && (yield == Yield.ToReturnStatement);
        ListBuffer<JCStatement> translated = ListBuffer.lb();
        for(JCStatement stmt : tree.getStatements()) {
            if (valueFromReturn && stmt.getTag() == JavafxTag.RETURN) {
                value = ((JCReturn)stmt).getExpression();
            } else {
                translated.append( translate(stmt) );
            }
        }       
        List<JCStatement> localDefs = translated.toList();
        
        if (yield == Yield.ToExpression) {
            assert (tree.type != syms.voidType) : "void block expressions should be handled below";
            JCExpression tvalue = translate(value); // must be before prepend
            localDefs = prependToStatements.appendList(localDefs).toList();
            result = ((JavafxTreeMaker) make).at(tree.pos).BlockExpression(tree.flags, localDefs, tvalue);
        } else {
            prependToStatements.appendList(localDefs);
            if (value != null) {
                prependToStatements.append( translateExpressionToStatement(value) );
            }
            localDefs = prependToStatements.toList();
            result = localDefs.size() == 1? localDefs.head : make.at(diagPos).Block(0L, localDefs);
        }
        prependToStatements = prevPrependToStatements;
    }
 
    @Override
    public void visitBlock(JCBlock tree) {
        List<JCStatement> localDefs = translateStatements(tree.stats);
	result = make.at(tree.pos).Block(tree.flags, localDefs);
    }

    @Override
    public void visitAssign(JCAssign tree) {
        DiagnosticPosition diagPos = tree.pos();
        
        Symbol sym = expressionSymbol(tree.lhs);
        VarSymbol vsym = (sym != null && (sym instanceof VarSymbol))? (VarSymbol)sym : null;

        if (tree.rhs instanceof JFXBindExpression) {
            JFXBindExpression bind = (JFXBindExpression) tree.rhs;
            JCExpression lhs = translate(tree.lhs, Wrapped.InLocation);
            result = translateDefinitionalAssignmentToSetExpression(bind.pos(), 
                                    bind.getExpression(), bind.getBindStatus(), vsym,
                                    null /*for now*/, FROM_DEFAULT_MILIEU);
            return;
        }
        
        JCExpression rhs = translate(tree.rhs);
        if (tree.lhs.getTag() == JavafxTag.SEQUENCE_INDEXED) {
            // assignment of a sequence element --  s[i]=8, call the sequence set method
            JFXSequenceIndexed si = (JFXSequenceIndexed)tree.lhs;
            JCExpression seq = translate(si.getSequence(), Wrapped.InLocation); 
            JCExpression index = translate(si.getIndex());
            JCFieldAccess select = make.Select(seq, defs.setMethodName);
            List<JCExpression> args = List.of(index, rhs);
            result = make.at(diagPos).Apply(null, select, args);
        } else if (tree.lhs.getTag() == JavafxTag.SEQUENCE_SLICE) {
            // assignment of a sequence slice --  s[i..j]=8, call the sequence set method
            JFXSequenceSlice si = (JFXSequenceSlice)tree.lhs;
            JCExpression seq = translate(si.getSequence(), Wrapped.InLocation); 
            JCExpression firstIndex = translate(si.getFirstIndex());
            JCExpression lastIndex = makeSliceLastIndex(si);
            JCFieldAccess select = make.Select(seq, defs.replaceSliceMethodName);
            List<JCExpression> args = List.of(firstIndex, lastIndex, rhs);
            result = make.at(diagPos).Apply(null, select, args);
        } else if (shouldMorph(vsym)) {
            // we are setting a var Location, call the set method
            JCExpression lhs = translate(tree.lhs, Wrapped.InLocation);
            JCFieldAccess setSelect = make.Select(lhs, defs.locationSetMethodName[typeMorpher.typeMorphInfo(vsym.type).getTypeKind()]);
            List<JCExpression> setArgs = List.of(rhs);
            result = make.at(diagPos).Apply(null, setSelect, setArgs);
        } else {
            // We are setting a "normal" non-Location, use normal assign
            JCExpression lhs = translate(tree.lhs);
            result = make.at(diagPos).Assign(lhs, rhs); // make a new one so we are non-destructive
        }
    }

    @Override
    public void visitAssignop(JCAssignOp tree) {
        DiagnosticPosition diagPos = tree.pos();
        
        Symbol sym = expressionSymbol(tree.lhs);
        VarSymbol vsym = (sym != null && (sym instanceof VarSymbol))? (VarSymbol)sym : null;
        
        JCExpression rhs = translate(tree.rhs);
        JCExpression lhs = translate(tree.lhs);
        int binaryOp;
        switch (tree.getTag()) {
            case JCTree.PLUS_ASG:
                binaryOp = JCTree.PLUS;
                break;
            case JCTree.MINUS_ASG:
                binaryOp = JCTree.MINUS;
                break;
            case JCTree.MUL_ASG:
                binaryOp = JCTree.MUL;
                break;
            case JCTree.DIV_ASG:
                binaryOp = JCTree.DIV;
                break;
            case JCTree.MOD_ASG:
                binaryOp = JCTree.MOD;
                break;
            default:
                assert false : "unexpected assign op kind";
                binaryOp = JCTree.PLUS;
                break;
        }
        JCExpression combined = make.at(diagPos).Binary(binaryOp, lhs, rhs);

        if (tree.lhs.getTag() == JavafxTag.SEQUENCE_INDEXED) {
            // assignment of a sequence element --  s[i]+=8, call the sequence set method
            JFXSequenceIndexed si = (JFXSequenceIndexed)tree.lhs;
            JCExpression seq = translate(si.getSequence(), Wrapped.InLocation); 
            JCExpression index = translate(si.getIndex());
            JCFieldAccess select = make.Select(seq, defs.setMethodName);
            List<JCExpression> args = List.of(index, combined);
            result = make.at(diagPos).Apply(null, select, args);
        } else if (shouldMorph(vsym)) {
            // we are setting a var Location, call the set method
            JCExpression targetLHS = translate(tree.lhs, Wrapped.InLocation);
            JCFieldAccess setSelect = make.Select(targetLHS, defs.locationSetMethodName[typeMorpher.typeMorphInfo(vsym.type).getTypeKind()]);
            List<JCExpression> setArgs = List.of(combined);
            result = make.at(diagPos).Apply(null, setSelect, setArgs);
        } else {
            // We are setting a "normal" non-Location non-sequence-access, use normal assignop
            result = make.at(diagPos).Assignop(tree.getTag(), lhs, rhs);
        }
    }

    @Override
    public void visitSelect(JCFieldAccess tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression selected = tree.getExpression();
        Type selectedType = selected.type;

        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        JCExpression translatedSelected = translate(selected, Wrapped.InNothing);
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;            
            JCVariableDecl selectedTmpDecl = makeTmpVar(diagPos, "tg", selectedType, translatedSelected);
            JCExpression translated = make.at(diagPos).Select(make.Ident(selectedTmpDecl.name), tree.getIdentifier());
            translated = makeFunctionValue(translated, null, tree.pos(), mtype);
            //result = make.LetExpr(selectedTmp, translated);
            result = ((JavafxTreeMaker)make).BlockExpression(
                0L,
                List.<JCStatement>of(selectedTmpDecl), 
                translated);
           return;
        }

       if (selectedType != null && selectedType.isPrimitive()) { // selected.type is null for package symbols.
            translatedSelected = makeBox(diagPos, translatedSelected, selectedType);
        }
        // determine if this is a static reference, eg.   MyClass.myAttribute
        boolean staticReference = false;
        if (tree.sym != null && tree.sym.isStatic()) {
            if (selected.getTag() == JCTree.SELECT) {
                staticReference = ((JCFieldAccess) selected).sym instanceof ClassSymbol;
            } else if (selected.getTag() == JCTree.IDENT) {
                staticReference = ((JCIdent) selected).sym instanceof ClassSymbol;
            }
        }
        List<JCStatement> dummyReference = null;
        if (! staticReference && tree.sym.isStatic()) {  //TODO: something is goofy here, see above test
            // Translate x.staticRef to { x; XClass.staticRef }:
            dummyReference = List.<JCStatement>of(make.Exec(translatedSelected));
            translatedSelected = makeTypeTree(tree.sym.owner.type, tree, false);
            staticReference = true;
        }

        boolean testForNull = generateNullChecks && !staticReference
                                           && (tree.sym instanceof VarSymbol) 
                                           && types.isJFXClass(selectedType.tsym);
        boolean hasSideEffects = testForNull && hasSideEffects(selected);
        JCVariableDecl tmpVar = null;
        if (hasSideEffects) {
            tmpVar = makeTmpVar(diagPos, selectedType, translatedSelected);
            translatedSelected = make.at(diagPos).Ident(tmpVar.name);
        }
        
        JCFieldAccess translated = make.at(diagPos).Select(translatedSelected, tree.getIdentifier());

        //TODO: there is probably a cleaner way to do this -- e.g., state.isBound() is probably redundant
        // only make dynamic dependencies if we are in a binding expression (bindingExpressionDefs != null)
        boolean createDynamicDependencies = state.isBound() && !staticReference && bindingExpressionDefs != null;
        
        JCExpression ref = typeMorpher.convertVariableReference(
                diagPos,
                translated,
                tree.sym, 
                staticReference, 
                state.wantLocation(),
                createDynamicDependencies);
        if (dummyReference != null)
            ref = ((JavafxTreeMaker)make).BlockExpression(
                0L, dummyReference, ref);
        if (testForNull) {
            // we have a testable guard for null, wrap the attribute access  in it, return default value if null
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(tree.type);
            JCExpression defaultExpr = makeDefaultValue(diagPos, tmi);
            if (state.wantLocation()) {
                defaultExpr = makeUnboundLocation(diagPos, tmi, defaultExpr);
            }

            JCExpression checkedExpr;
            if (hasSideEffects) {
                // we don't recreate (we've stuck it in a var)
                checkedExpr = make.at(diagPos).Ident(tmpVar.name);
            } else {
                // re-translate, we need two of them
                checkedExpr = translate(selected, Wrapped.InNothing);
            }
            JCExpression cond = make.at(diagPos).Binary(
                    JCTree.EQ,
                    checkedExpr,
                    make.Literal(TypeTags.BOT, null));
            ref = make.at(diagPos).Conditional(cond, defaultExpr, ref);
            if (hasSideEffects) {
                // put the tmp var and the conditional in a block expression
                List<JCStatement> stmts = List.<JCStatement>of(tmpVar);
                ref = make.at(diagPos).BlockExpression(0L, stmts, ref);
            }
        }
        result = ref;
    }
    //where
    private boolean hasSideEffects(JCExpression expr) {
        final boolean[] hasSideEffectHolder = {false};
        new JavafxTreeScanner() {

            private void markSideEffects() {
                hasSideEffectHolder[0] = true;
            }

            @Override
            public void visitBlockExpression(JFXBlockExpression tree) {
                markSideEffects(); // maybe doesn't but covers all statements
            }

            @Override
            public void visitUnary(JCUnary tree) {
                markSideEffects();
            }

            @Override
            public void visitAssignop(JCAssignOp tree) {
                markSideEffects();
            }

            @Override
            public void visitInstanciate(JFXInstanciate tree) {
                markSideEffects();
            }

            @Override
            public void visitAssign(JCAssign tree) {
                markSideEffects();
            }

            @Override
            public void visitApply(JCMethodInvocation tree) {
                markSideEffects();
            }
        }.scan(expr);
        return hasSideEffectHolder[0];
    }
    
    @Override
    public void visitIdent(JCIdent tree)   {
       DiagnosticPosition diagPos = tree.pos();
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JFXFunctionDefinition def = null; // FIXME
            result = makeFunctionValue(make.Ident(functionName((MethodSymbol)tree.sym)), def, tree.pos(), mtype);
            return;
        }
        if (tree.name == names._this) {
            // in the static implementation method, "this" becomes "receiver$"
            result = make.at(diagPos).Ident(defs.receiverName);
            return;
        } else if (tree.name == names._super) {
            if (tree.type != null &&
                    types.isCompoundClass(tree.type.tsym)) {
                // "super" become just the class where the static implementation method is defined
                //  the rest of the implementation is in visitApply
                result = make.at(diagPos).Ident(tree.type.tsym.name);
            }
            else {
               JCFieldAccess superSelect = make.at(diagPos).Select(make.at(diagPos).Ident(defs.receiverName), tree.name);
                superSelect.type = tree.type;
                superSelect.sym = tree.sym;
                result = superSelect;
            }
            return;
        }
       
       // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
       // and possible outer access methods
        JCExpression convert;
        int kind = tree.sym.kind;
        if ((kind == Kinds.VAR || kind == Kinds.MTH) &&
                tree.sym.owner.kind == Kinds.TYP &&
                !tree.sym.isStatic()) {
            if (types.isCompoundClass(tree.sym.owner)) {
                convert = make.at(diagPos).Select(makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym), tree.name);
            }
            else {
                JCExpression mRec = makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym);
                convert = make.at(diagPos).Select(mRec, tree.name);
            }
        } else {
            if (tree.sym.isStatic()) {
                convert = make.at(diagPos).Select(makeTypeTree(tree.sym.owner.type, diagPos, false), tree.name);
            } else {
                convert = make.at(diagPos).Ident(tree.name);
            }
        }
        
        result = typeMorpher.convertVariableReference(diagPos, 
                convert, 
                tree.sym, 
                tree.sym.isStatic(),
                state.wantLocation(), 
                false);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) {
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
        args.append(make.at(diagPos).Select(makeTypeTree(elemType, diagPos), names._class));
        args.appendList( translate( tree.getItems() ) );
        result = make.at(diagPos).Apply(typeArgs, meth, args.toList());
        */
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = elementType(tree.type);
        UseSequenceBuilder builder = new UseSequenceBuilder(tree.pos(), elemType);
        stmts.append(builder.makeBuilderVar());
        for (JCExpression item : tree.getItems()) {
            stmts.append(builder.makeAdd( item ) );
        }
        result = makeBlockExpression(tree.pos(), stmts, builder.makeToSequence());
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression meth = makeQualifiedTree(
                diagPos, tree.isExclusive()? 
                    sequencesRangeExclusiveString : 
                    sequencesRangeString);
        ListBuffer<JCExpression> args = ListBuffer.lb();
        List<JCExpression> typeArgs = List.nil();
        args.append( translate( tree.getLower() ));
        args.append( translate( tree.getUpper() ));
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull() ));
        }
        result = make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        if (types.isSequence(tree.type)) {
            Type elemType = elementType(tree.type);
            result = makeEmptySequenceCreator(tree.pos(), elemType);
        }
        else
            result = make.at(tree.pos).Literal(TypeTags.BOT, null);
    }
        
    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seq = translate(tree.getSequence(), Wrapped.InLocation);  
        JCExpression index = translate(tree.getIndex());
        JCFieldAccess select = make.at(diagPos).Select(seq, defs.getMethodName);
        List<JCExpression> args = List.of(index);
        result = make.at(diagPos).Apply(null, select, args);
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seq = translate(tree.getSequence(), Wrapped.InLocation);  
        JCExpression firstIndex = translate(tree.getFirstIndex());
        JCExpression lastIndex = makeSliceLastIndex(tree);
        JCFieldAccess select = make.at(diagPos).Select(seq, defs.getSliceMethodName);
        List<JCExpression> args = List.of(firstIndex, lastIndex);
        result = make.at(diagPos).Apply(null, select, args);
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seqLoc = translate(tree.getSequence(), Wrapped.InLocation);
        JCExpression elem = translate( tree.getElement() );
        if (tree.getPosition() == null) {
            result = callStatement(diagPos, seqLoc, "insert", elem);
        } else {
            String meth = tree.shouldInsertAfter()? "insertAfter" : "insertBefore";
            result = callStatement(diagPos, seqLoc, meth, 
                    List.of(elem, translate(tree.getPosition())));
        }
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JCExpression seq = tree.getSequence();

        if (tree.getElement() != null) {
            JCExpression seqLoc = translate(seq, Wrapped.InLocation);
            result = callStatement(tree.pos(), seqLoc, "deleteValue", translate(tree.getElement()));
        } else {
            if (seq.getTag() == JavafxTag.SEQUENCE_INDEXED) {
                // deletion of a sequence element -- delete s[i]
                JFXSequenceIndexed si = (JFXSequenceIndexed) seq;
                JCExpression seqseq = si.getSequence();
                JCExpression seqLoc = translate(seqseq, Wrapped.InLocation);
                JCExpression index = translate(si.getIndex());
                result = callStatement(tree.pos(), seqLoc, "delete", index);
            } else if (seq.getTag() == JavafxTag.SEQUENCE_SLICE) {
                // deletion of a sequence slice --  delete s[i..j]=8
                JFXSequenceSlice slice = (JFXSequenceSlice) seq;
                JCExpression seqseq = slice.getSequence();
                JCExpression seqLoc = translate(seqseq, Wrapped.InLocation);
                JCExpression first = translate(slice.getFirstIndex());
                JCExpression last = makeSliceLastIndex(slice);
                result = callStatement(tree.pos(), seqLoc, "deleteSlice", List.of(first, last));
            } else if (types.isSequence(seq.type)) {
                JCExpression seqLoc = translate(seq, Wrapped.InLocation);
                result = callStatement(tree.pos(), seqLoc, "deleteAll");
            } else {
                result = make.at(tree.pos()).Exec(
                            make.Assign(
                                translate(seq), 
                                make.Literal(TypeTags.BOT, null)));
            }
        }
    }

    /**** utility methods ******/
    
    JCExpression makeSliceLastIndex(JFXSequenceSlice tree) {
        JCExpression lastIndex = tree.getLastIndex() == null ? 
                callExpression(tree, 
                    translate(tree.getSequence()),
                    defs.sizeMethodName) : 
                translate(tree.getLastIndex());
        int decr = 
                (tree.getEndKind() == SequenceSliceTree.END_EXCLUSIVE ? 1 : 0) +
                (tree.getLastIndex() == null ? 1 : 0);
        if (decr > 0) {
            lastIndex = make.at(tree).Binary(JavafxTag.MINUS,
                    lastIndex, make.Literal(TypeTags.INT, decr));
        }
        return lastIndex;
    }
    
    /**
     * For an attribute "attr" make an access to it via the receiver and getter  
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, String attribName) {
       return callExpression(diagPos, 
                make.Ident(defs.receiverName),
                attributeGetMethodNamePrefix + attribName);
   }
   
   /**
    * Make a receiver parameter. 
    * Its type is that of the corresponding interface and it is a final parameter.
    * */
    JCVariableDecl makeReceiverParam(JFXClassDeclaration cDecl) {
        return make.VarDef(
                make.Modifiers(Flags.FINAL | Flags.PARAMETER), 
                defs.receiverName, 
                make.Ident(initBuilder.interfaceName(cDecl)), 
                null);
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method) {
        return callStatement(diagPos, receiver, method, null);
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
             Name methodName) {
        return callStatement(diagPos, receiver, methodName, null);
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            Object args) {
        return make.at(diagPos).Exec(callExpression(diagPos, receiver, method, args));
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
             Name methodName, 
            Object args) {
        return make.at(diagPos).Exec(callExpression(diagPos, receiver, methodName, args));
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
        return Name.fromString(names, syntheticNamePrefix + syntheticNameCounter++ + kind);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, Type type, JCExpression value) {
        return makeTmpVar(diagPos, "tmp", type, value);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, String rootName, Type type, JCExpression value) {
            return make.at(diagPos).VarDef(
                          make.at(diagPos).Modifiers(Flags.FINAL),
                          getSyntheticName(rootName),
                          makeTypeTree(type, diagPos),
                          value);
    }
    
    JCMethodDecl makeMainMethod(DiagnosticPosition diagPos, Name className) {
            List<JCExpression> emptyExpressionList = List.nil();
            JCExpression classIdent = make.at(diagPos).Ident(className);
            JCExpression classConstant = make.at(diagPos).Select(classIdent, names._class);
            JCExpression startIdent = makeQualifiedTree(diagPos, JavafxDefs.startMethodString);
            ListBuffer<JCExpression>args = new ListBuffer<JCExpression>();
            args.append(classConstant);
            JCMethodInvocation runCall = make.at(diagPos).Apply(emptyExpressionList, startIdent, args.toList());
            List<JCStatement> mainStats = List.<JCStatement>of(make.at(diagPos).Exec(runCall));
            List<JCVariableDecl> paramList = List.nil();
            paramList = paramList.append(make.at(diagPos).VarDef(make.Modifiers(0), 
                    Name.fromString(names, "args"), 
                    make.at(diagPos).TypeArray(make.Ident(Name.fromString(names, "String"))), 
                    null));
            JCBlock body = make.Block(0, mainStats);
            return make.at(diagPos).MethodDef(make.Modifiers(Flags.PUBLIC | Flags.STATIC),
                    Name.fromString(names, "main"), 
                    make.at(diagPos).TypeIdent(TypeTags.VOID), 
                    List.<JCTypeParameter>nil(), 
                    paramList, 
                    makeThrows(diagPos), 
                    body, 
                    null);
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
    
    public List<JCExpression> makeThrows(DiagnosticPosition diagPos) {
        return List.of(makeQualifiedTree(diagPos, methodThrowsString));
    }
    
    public JCImport makeImport(String str, DiagnosticPosition diagPos) {
        JCExpression tree = makeQualifiedTree(diagPos, str);
        tree = make.at(diagPos).Select(tree, names.asterisk);
        return make.at(diagPos).Import(tree, false);
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
    
    JFXBlockExpression makeBlockExpression(DiagnosticPosition diagPos, 
            ListBuffer<JCStatement> stmts, JCExpression value) {
        return ((JavafxTreeMaker)make).at(diagPos).BlockExpression(0, stmts.toList(), value);
    }
    
    class UseSequenceBuilder {
        final DiagnosticPosition diagPos;
        Type elemType;
        
        Name sbName;
        
        UseSequenceBuilder(DiagnosticPosition diagPos, Type elemType) {
            this.diagPos = diagPos;
            this.elemType = elemType;
        }
        
        JCStatement makeBuilderVar() {
            JCExpression builderTypeExpr = makeQualifiedTree(diagPos, sequenceBuilderString);
            List<JCExpression> btargs = List.of(makeTypeTree(elemType, diagPos));
            builderTypeExpr = make.at(diagPos).TypeApply(builderTypeExpr, btargs);

            // Sequence builder temp var name "sb"
            sbName = getSyntheticName("sb");

            // Build "sb" initializing expression -- new SequenceBuilder<T>(clazz)
            List<JCExpression> args = List.<JCExpression>of( make.at(diagPos).Select(
                makeTypeTree(elemType, diagPos), 
                names._class));               

            JCExpression newExpr = make.at(diagPos).NewClass(
                null,                               // enclosing
                List.<JCExpression>nil(),           // type args
                make.at(diagPos).TypeApply(         // class name -- SequenceBuilder<elemType>
                     makeQualifiedTree(diagPos, sequenceBuilderString), 
                     List.<JCExpression>of(makeTypeTree(elemType, diagPos))),
                args,                               // args
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
         
        JCStatement makeAdd(JCExpression exprToAdd) {
            JCExpression expr = translate(exprToAdd);
            Type exprType = exprToAdd.type;
            if (exprType != elemType) {
                Type unboxedElemType = types.unboxedType(elemType);
                if (unboxedElemType != Type.noType) {
                    Type unboxedExprType = types.unboxedType(exprType);
                    if (unboxedExprType != Type.noType) {
                            expr = make.at(diagPos).TypeCast(unboxedExprType, expr);
                            exprType = unboxedExprType;
                    }
                    if (exprType.tag == TypeTags.INT && unboxedElemType.tag == TypeTags.DOUBLE) {
                        expr = make.at(diagPos).TypeCast(unboxedElemType, expr);
                    }
                } 
             }
            JCMethodInvocation addCall = make.Apply(
                    List.<JCExpression>nil(), 
                    make.at(diagPos).Select(
                        makeBuilderVarAccess(), 
                        Name.fromString(names, "add")), 
                    List.<JCExpression>of(expr));
            return make.at(diagPos).Exec(addCall);
        }

        JCExpression makeToSequence() {
            return make.Apply(
                List.<JCExpression>nil(), // type arguments
                make.at(diagPos).Select(
                    makeBuilderVarAccess(), 
                    Name.fromString(names, toSequenceString)),
                List.<JCExpression>nil() // arguments
                );
        }
    }

    JCExpression makeEmptySequenceCreator(DiagnosticPosition diagPos, Type elemType) {
        JCExpression meth = makeQualifiedTree(diagPos, sequencesEmptyString);
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append(make.at(diagPos).Select(makeTypeTree(elemType, diagPos, true), names._class));
        List<JCExpression> typeArgs = List.of(makeTypeTree(elemType, diagPos, true));
        return make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }
  
    /**
     * Build a Java AST representing the return type of a function.
     * Generate the return type as a Location if "isBound" is set.
     * */
    public JCExpression makeReturnTypeTree(DiagnosticPosition diagPos, MethodSymbol mth, boolean isBound) {
        Type returnType = mth.getReturnType();
        if (isBound) {
            VarMorphInfo vmi = typeMorpher.varMorphInfo(mth);
            returnType = vmi.getMorphedType();
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
        while (t instanceof CapturedType)
            t = ((CapturedType) t).wildcard;
        if (t.tag == TypeTags.CLASS) {
            JCExpression texp = null;

            if (makeIntf && t.tsym instanceof ClassSymbol &&
                    (t.tsym.flags_field & JavafxFlags.COMPOUND_CLASS) != 0) {
                 texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString() + interfaceSuffix);
            }
            else {
                if (t.isCompound())
                    t =syms.objectType;
                texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString());
            }

            // Type outer = t.getEnclosingType();
            if (!t.getTypeArguments().isEmpty()) {
                List<JCExpression> targs = List.nil();
                for (Type ta : t.getTypeArguments()) {
                    targs = targs.append(makeTypeTree(ta, diagPos, makeIntf));
                }
                texp = make.at(diagPos).TypeApply(texp, targs);
            }
            return texp;
        } else if (t.tag == TypeTags.WILDCARD) {
            WildcardType wtype = (WildcardType) t;
            return make.at(diagPos).Wildcard(make.TypeBoundKind(wtype.kind),
                    wtype.kind == BoundKind.UNBOUND ? null
                    : makeTypeTree(wtype.type, diagPos, makeIntf));
        } else if (t.tag == TypeTags.ARRAY) {
            return make.at(diagPos).TypeArray(makeTypeTree(types.elemtype(t), diagPos, makeIntf));
        } else {
            return make.at(diagPos).Type(t);
        }
    }

    Type elementType(Type seqType) {
        Type elemType = seqType.getTypeArguments().head;
        if (elemType instanceof CapturedType)
            elemType = ((CapturedType) elemType).wildcard;
        if (elemType instanceof WildcardType)
            elemType = ((WildcardType) elemType).type;
        return elemType;
    }
        
   JCExpression castFromObject (JCExpression arg, Type castType) {
        if (castType.isPrimitive())
            castType = types.boxedClass(castType).type;
         return make.TypeCast(makeTypeTree(castType, arg.pos()), arg);
    }

    /**
     * JCTrees which can just be copied and trees which sjould not occur 
     * */
    
    @Override
    public void visitAnnotation(JCAnnotation tree) {
        JCTree annotationType = translate(tree.annotationType);
        List<JCExpression> args = translate(tree.args);
        result = make.at(tree.pos).Annotation(annotationType, args);
    }

    @Override
    public void visitAssert(JCAssert tree) {
        JCExpression cond = translate(tree.cond);
        JCExpression detail = translate(tree.detail);
        result = make.at(tree.pos).Assert(cond, detail);
    }

    @Override
    public void visitBinary(final JCBinary tree) {
        result = (new Translator() {

            private final DiagnosticPosition diagPos = tree.pos();
            
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
                JCExpression defaultValue = typeMorpher.makeLit(tmi.getRealType(), tmi.getDefaultValue(), diagPos);
                return makeEqEq( arg, defaultValue);
            }
            
            /**
             * Check if a non-primitive has the default value for its type.
             */
            private JCExpression makeObjectNullCheck(Type argType, JCExpression arg) {
                TypeMorphInfo tmi = typeMorpher.typeMorphInfo(argType);
                if (tmi.getTypeKind() == TYPE_KIND_SEQUENCE || tmi.getRealType() == syms.javafx_StringType) {
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
                return make.at(diagPos).Binary(tag, arg1, arg2);
            }
            
            private JCExpression makeNull() {
                return make.at(diagPos).Literal(TypeTags.BOT, null);
            }
            
            private JCExpression callRuntime(String methNameString, List<JCExpression> args) {
                JCExpression meth = makeQualifiedTree(diagPos, methNameString);
                List<JCExpression> typeArgs = List.nil();
                return make.at(diagPos).Apply(typeArgs, meth, args);
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
                final JCExpression lhs = translate( tree.lhs );
                final JCExpression rhs = translate( tree.rhs );
                final Type lhsType = tree.lhs.type;
                final Type rhsType = tree.rhs.type;
                
                    // this is an x == y
                    if (lhsType.getKind() == TypeKind.NULL) {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // both are known to be null
                            return make.at(diagPos).Literal(TypeTags.BOOLEAN, 1);
                        } else if (rhsType.isPrimitive()) {
                            // lhs is null, rhs is primitive, do default check
                            return makePrimitiveNullCheck(rhsType, rhs);
                        } else {
                            // lhs is null, rhs is non-primitive, figure out what check to do
                            return makeObjectNullCheck(rhsType, rhs);
                        }
                    } else if (lhsType.isPrimitive()) {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // lhs is primitive, rhs is null, do default check on lhs
                            return makePrimitiveNullCheck(lhsType, lhs);
                        } else if (rhsType.isPrimitive()) {
                            // both are primitive, use ==
                            return makeEqEq(lhs, rhs);
                        } else {
                            // lhs is primitive, rhs is non-primitive, use equals(), but switch them
                            return makeFullCheck(rhs, lhs);
                        }
                    } else {
                        if (rhsType.getKind() == TypeKind.NULL) {
                            // lhs is non-primitive, rhs is null, figure out what check to do
                            return makeObjectNullCheck(lhsType, lhs);
                        } else {
                            //  lhs is non-primitive, use equals()
                            return makeFullCheck(lhs, rhs);
                        }
                    }
            }
           
            /**
             * Translate a binary expressions
             */
            public JCTree doit() {
                //TODO: handle <>
                if (tree.getTag() == JavafxTag.EQ) {
                    return translateEqualsEquals();
                } else if (tree.getTag() == JavafxTag.NE) {
                    return make.at(diagPos).Unary(JCTree.NOT, translateEqualsEquals());
                }  else {
                    // anything other than == or <>

                    // Time type operator overloading
                    if ((types.isSameType(tree.lhs.type, syms.javafx_TimeType) ||
                         types.isSameType(tree.rhs.type, syms.javafx_TimeType)) &&
                        tree.operator == null) { // operator check is to try to get a decent error message by falling through if the Time method isn't matched
                        JCExpression l = tree.lhs;
                        JCExpression r = tree.rhs;
                        switch (tree.getTag()) {
                        case JavafxTag.PLUS:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "add")), List.<JCExpression>of(translate(r)));
                            // lhs.add(rhs);
                        case JavafxTag.MINUS:
                            // lhs.sub(rhs);
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "sub")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.DIV:
                            // lhs.div(rhs);
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "div")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.MUL:
                            // lhs.mul(rhs);
                            if (!types.isSameType(l.type, syms.javafx_TimeType)) {
                                r = l;
                                l = tree.rhs;
                            }
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "mul")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.LT:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "lt")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.LE:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "le")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.GT:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "gt")), List.<JCExpression>of(translate(r)));
                        case JavafxTag.GE:
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(l), Name.fromString(names, "ge")), List.<JCExpression>of(translate(r)));
                        }
                    }
                    final JCExpression lhs = translate(tree.lhs);
                    final JCExpression rhs = translate(tree.rhs);
                    return makeBinary(tree.getTag(), lhs, rhs);
                }
            }

        }).doit();
    }

    @Override
    public void visitBreak(JCBreak tree) {
        result = make.at(tree.pos).Break(tree.label);
    }

    @Override
   public void visitCase(JCCase tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitCatch(JCCatch tree) {
        JCVariableDecl param = translate(tree.param);
        JCBlock body = translate(tree.body);
        result = make.at(tree.pos).Catch(param, body);
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        assert false : "should not be in JavaFX AST";
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
    @Override
    public void visitForExpression(JFXForExpression tree) {
        // sub-translation in done inline -- no super.visitForExpression(tree);
        if (yield == Yield.ToExecStatement) {
             result = wrapWithInClause(tree, translateExpressionToStatement(tree.getBodyExpression()));
        } else {
            // body has value (non-void)
            assert tree.type != syms.voidType : "should be handled above";
            DiagnosticPosition diagPos = tree.pos();
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            JCStatement stmt;
            JCExpression value;

            // Compute the element type from the sequence type
            assert tree.type.getTypeArguments().size() == 1;
            Type elemType = elementType(tree.type);

            UseSequenceBuilder builder = new UseSequenceBuilder(diagPos, elemType);
            stmts.append(builder.makeBuilderVar());

            // Build innermost loop body
            stmt = builder.makeAdd( tree.getBodyExpression() );

            // Build the result value
            value = builder.makeToSequence();
            stmt = wrapWithInClause(tree, stmt);
            stmts.append(stmt);

            if (yield == Yield.ToExpression) {
                    // Build the block expression -- which is what we translate to
                    result = makeBlockExpression(diagPos, stmts, value);
            } else {
                    stmts.append( make.at(tree).Return( value ) );
                    result = make.at(diagPos).Block(0L, stmts.toList());
            }
        }
    }

    //where
    private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
        JCStatement stmt = coreStmt;
        for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)tree.getInClauses().get(inx);
            if (clause.getWhereExpression() != null) {
                stmt = make.at(clause).If( translate( clause.getWhereExpression() ), stmt, null);
            }

            // Build the loop
            //TODO:  below is the simpler version of the loop. Ideally, this should be used in
            // cases where the loop variable does not need to be final.
            /*
            stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    boundTranslate(clause.getVar(), JavafxBindStatus.UNBOUND), 
                    translate(clause.getSequenceExpression()),
                    stmt);
             */
            JFXVar var = clause.getVar();
            Name tmpVarName = getSyntheticName(var.getName().toString());
            JCVariableDecl finalVar = make.VarDef(
                    make.Modifiers(Flags.FINAL), 
                    var.getName(), 
                    makeTypeTree(var.type, var), 
                    make.Ident(tmpVarName));
            Name tmpIndexVarName;
            if (clause.getIndexUsed()) {
                Name indexVarName = indexVarName(clause);
                tmpIndexVarName = getSyntheticName(indexVarName.toString());
                JCVariableDecl finalIndexVar = make.VarDef(
                    make.Modifiers(Flags.FINAL),
                    indexVarName, 
                    makeTypeTree(syms.javafx_IntegerType, var),
                    make.Unary(JCTree.POSTINC, make.Ident(tmpIndexVarName)));
                stmt = make.Block(0L, List.of(finalIndexVar, finalVar, stmt));
            }                
            else {
                tmpIndexVarName = null;
                stmt = make.Block(0L, List.of(finalVar, stmt));
            }
    
            DiagnosticPosition diagPos = clause.seqExpr;
            if (types.isSequence(clause.seqExpr.type)) {
                // It would be more efficient to move the Iterable.iterator call
                // to a static method, which can also check for null.
                // But that requires expanding the ForeachLoop by hand.  Later.
                JCExpression seq = callExpression(diagPos,
                    makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                    "forceNonNull",
                    List.of(make.at(diagPos).Select(makeTypeTree(syms.boxIfNeeded(var.type), diagPos, true), names._class),
                        translate(clause.seqExpr)));
                stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    make.VarDef(
                        make.Modifiers(0L), 
                        tmpVarName, 
                        makeTypeTree(var.type, var, true), 
                        null),
                    seq,
                    stmt);
            } else {
                // The "sequence" isn't a Sequence.
                // Compile: { var tmp = seq; if (tmp!=null) stmt; }
                if (! var.type.isPrimitive())
                    stmt = make.If(make.Binary(JCTree.NE, make.Ident(tmpVarName),
                                make.Literal(TypeTags.BOT, null)),
                            stmt, null);
                stmt = make.at(diagPos).Block(0,
                    List.of(make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        tmpVarName, 
                        makeTypeTree(var.type, var, true), 
                        translate(clause.seqExpr)),
                        stmt));
            }
            if (clause.getIndexUsed()) {
                JCVariableDecl tmpIndexVar =
                        make.VarDef(
                        make.Modifiers(0L), 
                        tmpIndexVarName, 
                        makeTypeTree(syms.javafx_IntegerType, var), 
                        make.Literal(Integer.valueOf(0)));
                stmt = make.Block(0L, List.of(tmpIndexVar, stmt));
            }
        }
        return stmt;
    }
    
    public Name indexVarName (JFXForExpressionInClause clause) {
        Name forVar = clause.getVar().getName();
        return names.fromString("$indexof$"+forVar.toString());
    }
    
    public void visitIndexof(JFXIndexof that) {
        result = make.at(that.pos()).Ident(indexVarName(that.clause));
    }

    @Override
    public void visitConditional(JCConditional tree) {
        final DiagnosticPosition diagPos = tree.pos();
        JCExpression cond = translate(tree.getCondition());
        JCExpression trueSide = tree.getTrueExpression();
        JCExpression falseSide = tree.getFalseExpression();
        if (yield == Yield.ToExpression) {
            JCExpression translatedFalseSide;
            if (falseSide == null) {
                Type trueSideType = tree.getTrueExpression().type;
                switch (trueSideType.tag) {
                    case TypeTags.BOOLEAN:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOOLEAN, 0);
                        break;
                    case TypeTags.INT:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.INT, 0);
                        break;
                    case TypeTags.DOUBLE:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.DOUBLE, 0.0);
                        break;
                    case TypeTags.BOT:
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                    case TypeTags.VOID:
                        assert false : "should have been translated";
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                    case TypeTags.CLASS:
                        if (trueSideType == syms.stringType) {
                            translatedFalseSide = make.at(diagPos).Literal(TypeTags.CLASS, "");
                        } else {
                            translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        }
                        break;
                    default:
                        assert false : "what is this type doing here? " + trueSideType;
                        translatedFalseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                        break;
                }
            } else {
                translatedFalseSide = translate(falseSide);
            }
            result = make.at(diagPos).Conditional(cond, translate(trueSide), translatedFalseSide);
        } else {
            result = make.at(diagPos).If(cond, 
                    translateExpressionToStatement(trueSide), 
                    falseSide == null ? null : translateExpressionToStatement(falseSide));
        }
    }

    @Override
    public void visitContinue(JCContinue tree) {
        result = make.at(tree.pos).Continue(tree.label);
    }

    @Override
    public void visitDoLoop(JCDoWhileLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitErroneous(JCErroneous tree) {
        List<? extends JCTree> errs = translate(tree.errs);
        result = make.at(tree.pos).Erroneous(errs);
    }
   
    @Override
    public void visitReturn(JCReturn tree) {
        JCExpression exp = tree.getExpression();
        if (exp == null) {
            result = make.at(tree).Return(null);
        } else {
            result = translateExpressionToStatement(exp, tree.type);
        }
    }

    @Override
    public void visitExec(JCExpressionStatement tree) {
        result = translateExpressionToStatement(tree.getExpression(), syms.voidType);
    }

    @Override
    public void visitParens(JCParens tree) {
        if (yield == Yield.ToExpression) {
            JCExpression expr = translate(tree.expr);
            result = make.at(tree.pos).Parens(expr);
        } else {
            result = translateExpressionToStatement(tree.expr);
        }
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
        JCTree qualid = translate(tree.qualid);
        result = make.at(tree.pos).Import(qualid, tree.staticImport);
    }

    @Override
    public void visitIndexed(JCArrayAccess tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeTest(JCInstanceOf tree) {
        JCTree clazz = this.makeTypeTree(tree.clazz.type, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeTest(expr, clazz);
    }

    @Override
    public void visitTypeCast(JCTypeCast tree) {
        Type clazztype = tree.clazz.type;
        if (clazztype.isPrimitive() && ! tree.expr.type.isPrimitive())
            clazztype = types.boxedClass(clazztype).type;
        JCTree clazz = this.makeTypeTree(clazztype, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeCast(clazz, expr);
    }
    
    @Override
    public void visitLabelled(JCLabeledStatement tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitLiteral(JCLiteral tree) {
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = elementType(tree.type);
            result = makeEmptySequenceCreator(tree.pos(), elemType);
        }
        else
            result = make.at(tree.pos).Literal(tree.typetag, tree.value);
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
         assert false : "should not be in JavaFX AST";
   }

    @Override
    public void visitApply(final JCMethodInvocation tree) {
        result = (new Translator() {

            private final DiagnosticPosition diagPos = tree.pos();
            private final JCExpression meth = tree.meth;
            private final JCFieldAccess fieldAccess = meth.getTag() == JavafxTag.SELECT?   
                                                                    (JCFieldAccess) meth :  
                                                                    null;
            private final JCExpression selector = fieldAccess != null? fieldAccess.getExpression() : null;
            private final Name name = fieldAccess != null? fieldAccess.name : null;
            private final Symbol sym = expressionSymbol(meth);
            private final MethodSymbol msym = (sym instanceof MethodSymbol)? (MethodSymbol)sym : null;
            private final Name selectorIdName = (selector != null && selector.getTag() == JavafxTag.IDENT)? ((JCIdent) selector).getName() : null;
            private final boolean thisCall = selectorIdName == names._this;
            private final boolean superCall = selectorIdName == names._super;
            private final boolean namedSuperCall =
                    selector != null && msym!= null && ! msym.isStatic() &&
                    expressionSymbol(selector) instanceof ClassSymbol &&
                    // FIXME should also allow other enclosing classes:
                    types.isSuperType(expressionSymbol(selector).type, currentClass.sym);
            private final boolean renameToSuper = namedSuperCall && ! types.isCompoundClass(currentClass.sym);
            private final boolean superToStatic = (superCall || namedSuperCall) && ! renameToSuper;
            private final List<Type> formals = meth.type.getParameterTypes();

            private final boolean useInvoke = meth.type instanceof FunctionType;
                                                      // The below was part of this test, but it doesn't make much sense,
                                                      // it was blocking other work and it is never used in runtime or tests
                                                      //              || (transMeth instanceof JCIdent
                                                      //                           && ((JCIdent) transMeth).sym instanceof MethodSymbol 
                                                      //                           && isInnerFunction((MethodSymbol) ((JCIdent) transMeth).sym)));

            private final boolean testForNull =  generateNullChecks && msym!=null  &&
                    !sym.isStatic() && selector!=null && !superCall && !namedSuperCall &&
                    !thisCall && !useInvoke && !selector.type.isPrimitive() && !renameToSuper;
            private final boolean hasSideEffects = testForNull && hasSideEffects(selector);
            private final boolean callBound = generateBoundFunctions && state.isBound() && msym != null                   && types.isJFXClass(msym.owner)
                    && (generateBoundVoidFunctions || msym.getReturnType() != syms.voidType )
                    && !useInvoke;

            public JCTree doit() {
                JCVariableDecl selectorVar = null;
                JCExpression transMeth;
                if (renameToSuper) {
                    transMeth = make.at(selector).Select(make.Select(makeTypeTree(currentClass.sym.type, selector, false), names._super), sym);
                } else {
                    transMeth = translate(meth);
                    if (hasSideEffects && transMeth.getTag() == JavafxTag.SELECT) {
                        // still a select and presumed to still have side effects -- hold the selector in a temp var
                        JCFieldAccess transMethFA = (JCFieldAccess) transMeth;
                        selectorVar = makeTmpVar(diagPos, "select", selector.type, transMethFA.getExpression());
                        transMeth = make.at(diagPos).Select(make.at(diagPos).Ident(selectorVar.name), transMethFA.name);
                    }  
                }

                // translate the method name -- e.g., foo  to foo$bound or foo$impl
                if (superToStatic || (callBound && ! renameToSuper)) {
                    Name name = functionName(msym, superToStatic, callBound);
                    if (transMeth.getTag() == JavafxTag.IDENT) {
                        transMeth = make.at(diagPos).Ident(name);
                    } else if (transMeth.getTag() == JavafxTag.SELECT) {
                        JCFieldAccess faccess = (JCFieldAccess) transMeth;
                        transMeth = make.at(diagPos).Select(faccess.getExpression(), name);
                    }
                }
                if (useInvoke) {
                    transMeth = make.Select(transMeth, defs.invokeName);
                }

                JCMethodInvocation app = make.at(diagPos).Apply( translate(tree.typeargs), transMeth, determineArgs());
                JCExpression fresult = app;
                if (callBound) {
                    fresult = makeBoundCall(app);
                }
                if (useInvoke) {
                    if (tree.type.tag != TypeTags.VOID) {
                        fresult = castFromObject(fresult, tree.type);
                    }
                } else if (transMeth instanceof JFXBlockExpression) {
                    // If visitSelect translated exp.staticMember to
                    // { exp; class.staticMember }:
                    JFXBlockExpression block = (JFXBlockExpression) transMeth;
                    app.meth = block.value;
                    block.value = app;
                    fresult = block;
                }
                // If we are to yield a Location, and this isn't going to happen as
                // a return of using a bound call (for example, if this is a Java call)
                // then convert into a Location
                if (state.wantLocation() && !callBound && msym != null) {
                    TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(msym.getReturnType());
                    fresult = makeUnboundLocation(diagPos, returnTypeInfo, fresult);
                }
                if (testForNull) {
                    JCExpression toTest = hasSideEffects? 
                                             make.at(diagPos).Ident(selectorVar.name) :
                                             translate(selector);
                    // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
                    JCExpression cond = make.at(diagPos).Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
                    if (msym.getReturnType() == syms.voidType) {
                        // if this is a void expression, check it with an If-statement
                        JCStatement stmt = make.If(cond, make.Exec(fresult), null);
                        assert yield == Yield.ToExecStatement : "Yield from a void call should always be a statement";
                        // a statement is the desired result of the translation, return the If-statement
                        if (hasSideEffects) {
                            // if the selector has side-effects, we created a temp var to hold it
                            // so we need to make a block to include the temp var
                            return make.at(diagPos).Block(0L, List.<JCStatement>of(selectorVar, stmt));
                        } else {
                            return stmt;
                        }
                    } else {
                        // it has a non-void return type, convert it to a conditional expression
                        // if it would dereference null, then instead give the default value
                        TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(msym.getReturnType());
                        JCExpression defaultExpr = makeDefaultValue(diagPos, returnTypeInfo);
                        if (state.wantLocation()) {
                            defaultExpr = makeUnboundLocation(diagPos, returnTypeInfo, defaultExpr);
                        }
                        fresult =  make.at(diagPos).Conditional(cond, fresult, defaultExpr);
                        if (hasSideEffects) {
                            // if the selector has side-effects, we created a temp var to hold it
                            // so we need to make a block-expression to include the temp var
                            fresult = make.at(diagPos).BlockExpression(0L, List.<JCStatement>of(selectorVar), fresult);
                        }
                    }
                }
                return fresult;
            }

            // compute the translated arguments.
            // if this is a bound call, use left-hand side references for arguments consisting
            // solely of a  var or attribute reference, or function call, otherwise, wrap it
            // in an expression location
            List<JCExpression> determineArgs() {
                List<JCExpression> args;
                if (callBound) {
                    ListBuffer<JCExpression> targs = ListBuffer.lb();
                    List<Type> formal = formals;
                    for (JCExpression arg : tree.args) {
                        switch (arg.getTag()) {
                            case JavafxTag.IDENT:
                            case JavafxTag.SELECT:
                            case JavafxTag.APPLY:
                                // This arg expression is one that will translate into a Location;
                                // since that is needed for a this into Location, do so.
                                // However, if the types need to by changed (subclass), this won't
                                // directly work.
                                // Also, if this is a mismatched sequence type, we will need
                                // to do some different
                                if (arg.type.equals(formal.head) || 
                                        types.isSequence(formal.head)) {
                                    targs.append(translate(arg, Wrapped.InLocation));
                                    break;
                                }
                                //TODO: handle sequence subclasses
                                //TODO: use more efficient mechanism (use currently apears rare)
                                //System.err.println("Not match: " + arg.type + " vs " + formal.head);
                                // Otherwise, fall-through, presumably a conversion will work.
                            default:
                                {
                                    ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
                                    bindingExpressionDefs = ListBuffer.lb();
                                    targs.append(makeBoundLocation(
                                                                    arg.pos(), 
                                                                    typeMorpher.typeMorphInfo(formal.head),
                                                                    translateExpressionToStatement(arg, arg.type), 
                                                                    false, 
                                                                    typeMorpher.buildDependencies(arg)));
                                    assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
                                    bindingExpressionDefs = prevBindingExpressionDefs;
                                    break;
                                }
                        }
                        formal = formal.tail;
                    }
                    args = targs.toList();
                } else {
                    boolean varargs = tree.args != null && msym != null &&
                            (msym.flags() & VARARGS) != 0 &&
                            (formals.size() != tree.args.size() ||
                             types.isConvertible(tree.args.last().type,
                                 types.elemtype(formals.last())));
                    ListBuffer<JCExpression> translated = ListBuffer.lb();
                    boolean handlingVarargs = false;
                    Type formal = null;
                    List<Type> t = formals;
	            for (List<JCExpression> l = tree.args; l.nonEmpty();  l = l.tail) {
                        if (! handlingVarargs) {
                            formal = t.head;
                            t = t.tail;
                            if (varargs && t.isEmpty()) {
                                formal = types.elemtype(formal);
                                handlingVarargs = true;
                            }
                        }
                        JCExpression tree = translate(l.head, formal);
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

            // now, generate the bound function call.  The bound function should be called no
            // more than once (per context), but it must be called in place, because of local
            // variables, so lazily set the functions bound location, which is stored as a field
            // on the binding expression.  Code looks like this:
            //     (loc == null)? addDependent( loc = boundCall ) : loc
            // if this is a right-hand side context, call get() on that to get the value.
            JCExpression makeBoundCall(JCExpression applyExpression) {
                Name tmpName = getSyntheticName("loc");
                JCExpression cond = make.at(diagPos).Binary(JCTree.EQ, make.at(diagPos).Ident(tmpName), make.Literal(TypeTags.BOT, null));
                JCExpression initLocation = callExpression(diagPos, 
                                                                    null,
                                                                    defs.addStaticDependentName,
                                                                    make.at(diagPos).Assign(make.at(diagPos).Ident(tmpName), applyExpression));
                JavafxTypeMorpher.TypeMorphInfo tmi = typeMorpher.typeMorphInfo(msym.getReturnType());
                Type morphedReturnType = tmi.getMorphedType();
                bindingExpressionDefs.append(make.VarDef(
                                                                    make.Modifiers(Flags.PRIVATE),
                                                                    tmpName,
                                                                    makeTypeTree(morphedReturnType, diagPos),
                                                                    make.Literal(TypeTags.BOT, null)));
                JCExpression funcLoc = make.at(diagPos).Conditional(cond, 
                                                                    initLocation, 
                                                                    make.at(diagPos).Ident(tmpName));
                return state.wantLocation() ? funcLoc : callExpression(diagPos, 
                                                                    make.at(diagPos).Parens(funcLoc),
                                                                    defs.locationGetMethodName[tmi.getTypeKind()]);
            }

        }).doit();
    }

    @Override
    public void visitModifiers(JCModifiers tree) {
        List<JCAnnotation> annotations = translate(tree.annotations);
        result = make.at(tree.pos).Modifiers(tree.flags, annotations);
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
        result = make.at(tree.pos).Skip();
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
        JCTree expr = translate(tree.expr);
        result = make.at(tree.pos).Throw(expr);
    }

    @Override
    public void visitTry(JCTry tree) {
        JCBlock body = translate(tree.body);
        List<JCCatch> catchers = translate(tree.catchers);
        JCBlock finalizer = translate(tree.finalizer);
        result = make.at(tree.pos).Try(body, catchers, finalizer);
    }

    @Override
    public void visitTypeApply(JCTypeApply tree) {
        JCExpression clazz = translate(tree.clazz);
        List<JCExpression> arguments = translate(tree.arguments);
        result = make.at(tree.pos).TypeApply(clazz, arguments);
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
    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        result = make.at(tree.pos).TypeIdent(tree.typetag);
    }

    @Override
    public void visitTypeParameter(JCTypeParameter tree) {
        List<JCExpression> bounds = translate(tree.bounds);
        result = make.at(tree.pos).TypeParameter(tree.name, bounds);
    }

    @Override
    public void visitUnary(final JCUnary tree) {
        result = (new Translator() {

            private final DiagnosticPosition diagPos = tree.pos();
            private final JCExpression expr = tree.getExpression();
            private final JCExpression transExpr = translate(expr);

            private JCExpression doVanilla() {
                return make.at(diagPos).Unary(tree.getTag(), transExpr);
            }
            
            private JCExpression callSetMethod(JCExpression target, List<JCExpression> args) {
                JCExpression transTarget = translate(target, Wrapped.InLocation); // must be Location
                JCFieldAccess setSelect = make.Select(transTarget,
                                                      args.size() == 1
                                                              ? defs.locationSetMethodName[typeMorpher.typeMorphInfo(target.type).getTypeKind()]
                                                              : defs.setMethodName);
                return make.at(diagPos).Apply(null, setSelect, args);
            }

            private JCExpression doIncDec(int binaryOp, boolean postfix) {
                final Symbol sym = expressionSymbol(expr);
                final VarSymbol vsym = (sym != null && (sym instanceof VarSymbol)) ? (VarSymbol) sym : null;
                final JCExpression one = make.at(diagPos).Literal(1);
                final JCExpression combined = make.at(diagPos).Binary(binaryOp, transExpr, one);
                JCExpression ret;

                if (expr.getTag() == JavafxTag.SEQUENCE_INDEXED) {
                    // assignment of a sequence element --  s[i]+=8, call the sequence set method
                    JFXSequenceIndexed si = (JFXSequenceIndexed) expr;
                    JCExpression index = translate(si.getIndex());
                    ret = callSetMethod( si.getSequence(), List.of(index, combined) );
                } else if (shouldMorph(vsym)) {
                    // we are setting a var Location, call the set method
                    ret = callSetMethod( expr, List.of(combined) );
                } else {
                    // We are setting a "normal" non-Location non-sequence-access, use normal operator
                    return doVanilla();
                }
                if (postfix) {
                    // this is a postfix operation, undo the value (not the variable) change
                    return make.at(diagPos).Binary(binaryOp, ret, make.at(diagPos).Literal(-1));
                } else {
                    // prefix operation
                    return ret;
                }
            }

            public JCTree doit() {
                switch (tree.getTag()) {
                    case JavafxTag.SIZEOF:
                        return callExpression(diagPos, 
                                makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"), 
                                defs.sizeMethodName, transExpr);
                    case JavafxTag.REVERSE:
                        return callExpression(diagPos, 
                                makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"), 
                                "reverse", transExpr);
                    case JCTree.PREINC:
                        return doIncDec(JCTree.PLUS, false);
                    case JCTree.PREDEC:
                        return doIncDec(JCTree.MINUS, false);
                    case JCTree.POSTINC:
                        return doIncDec(JCTree.PLUS, true);
                    case JCTree.POSTDEC:
                        return doIncDec(JCTree.MINUS, true);
                    case JCTree.NEG:
                        if (types.isSameType(tree.type, syms.javafx_TimeType)) {
                            return make.at(diagPos).Apply(null,
                                                          make.at(diagPos).Select(translate(tree.arg), Name.fromString(names, "negate")), List.<JCExpression>nil());
                        }
                    default:
                        return doVanilla();
                }
            }
        }).doit();
    }
  

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitWhileLoop(JCWhileLoop tree) {
        JCStatement body = translate(tree.body);
        JCExpression cond = translate(tree.cond);
        result = make.at(tree.pos).WhileLoop(cond, body);
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
        TypeBoundKind kind = make.at(tree.kind.pos).TypeBoundKind(tree.kind.kind);
        JCTree inner = translate(tree.inner);
        result = make.at(tree.pos).Wildcard(kind, inner);
    }

    @Override
    public void visitLetExpr(LetExpr tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTree(JCTree tree) {
        assert false : "should not be in JavaFX AST";
    }
    
    /******** goofy visitors, most of which should go away ******/

    public void visitDoLater(JFXDoLater that) {
        that.body = translate(that.body);
        result = that;
    }

    public void visitMemberSelector(JFXMemberSelector that) {
        result = that;
    }
    
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
        result = that;
    }
    
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.expr = translate(that.expr);
        result = that;
    }  
    
    public void visitTypeAny(JFXTypeAny that) {
        result = that;
    }
    
    public void visitTypeClass(JFXTypeClass that) {
        result = that;
    }
    
    public void visitTypeFunctional(JFXTypeFunctional that) {
        that.params = (List<JFXType>)translate((List<JFXType>)that.params);
        that.restype = translate(that.restype);
        result = that;
    }
    
    public void visitTypeUnknown(JFXTypeUnknown that) {
        result = that;
    }

    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }
    
    /***********************************************************************
     *
     * Utilities 
     *
     */
    
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
    
    /**
     * Build the body of a bound function.
     * There are two approaches, bind the body as a whole and re-executed it if any of its dependencies change,
     * or permeate the bind into the body.
     * The latter will be attempted when it is straight-line code with no assignments
     */
    private JCBlock boundMethodBody(DiagnosticPosition diagPos, JFXBlockExpression bexpr, JFXFunctionDefinition func) {
        JCStatement ret;
        
        ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
        bindingExpressionDefs = ListBuffer.lb();
        BindAnalysis analysis = typeMorpher.bindAnalysis(bexpr);
        // TODO: Remove entry in findbugs-exclude.xml if permeateBind is implemented
        if (permeateBind && analysis.isBindPermeable()) { //TODO: permeate bind
            State prevState = state;
            state = new State(state.wrap, Convert.Normal);
            ret = make.at(diagPos).Return( makeBoundExpression(bexpr, typeMorpher.varMorphInfo( func.sym ), false) );
            state = prevState;
        } else {
            ret =  make.at(diagPos).Return( makeBoundExpression(bexpr, typeMorpher.varMorphInfo( func.sym ), false) );
        }
        assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
        bindingExpressionDefs = prevBindingExpressionDefs;
        return asBlock( ret );
    }
    
    JCExpression makeBoundExpression(JCExpression expr, TypeMorphInfo tmi, boolean isLazy) {
        return makeBoundLocation(
                    expr.pos(), tmi, 
                    translateExpressionToStatement(expr, expr.type), 
                    isLazy, 
                    typeMorpher.buildDependencies(expr));
    }
    
    private JCBlock runMethodBody(JFXBlockExpression bexpr) {
        DiagnosticPosition diagPos = bexpr.pos();
        List<JCStatement> stats = translateStatements(bexpr.stats);
        boolean nullReturnNeeded = true;
        if (bexpr.value != null) {
            Type valueType = bexpr.value.type;
            if (valueType == syms.voidType) {
                // convert the void typed expression to a statement, still return null
                stats = stats.append( translateExpressionToStatement(bexpr.value, valueType) );
            } else {
                // the returned value will be the trailing expression, box if needed
                //TODO: handle cases of single legged if, etc
                if (valueType.isPrimitive()) {
                    JCExpression boxedExpr = makeBox(diagPos, translate(bexpr.value), valueType);
                    stats = stats.append( make.Return( boxedExpr ) );
                } else {
                    stats = stats.append( translateExpressionToStatement(bexpr.value, valueType) );
                }
                nullReturnNeeded = false;
            }
        }
        if (nullReturnNeeded) {
            stats = stats.append( make.Return(make.Literal(TypeTags.BOT, null)) );
        }
        return make.at(diagPos).Block(0L, stats);
    }
    
    boolean shouldMorph(VarSymbol vsym) {
        if (vsym == null) {
            return false;
        }
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        return shouldMorph(vmi);
    }
    
    boolean shouldMorph(VarMorphInfo vmi) {
        if ( vmi.mustMorph() )
            return true;
        else
            return locallyBound != null ? locallyBound.contains(vmi.getSymbol()) : false;
    }
    
    JCExpression makeIdentifier(String s) {
        return ((JavafxTreeMaker)make).Identifier(s);
    }
    
    JCExpression makeIdentifier(Name name) {
        return ((JavafxTreeMaker)make).Identifier(name);
    }
    
    boolean isRunMethod(MethodSymbol sym) {
        return sym.name == defs.runMethodName;
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
    
    /**
     * Build the AST for accessing the outer member. 
     * The accessors might be chained if the member accessed is more than one level up in the outer chain.
     * */
    private JCExpression makeReceiver(DiagnosticPosition pos, Symbol treeSym, Symbol siteOwner) {
        JCExpression ret = null;
        if (treeSym != null && siteOwner != null) {
            
            ret = make.Ident(defs.receiverName);
            ret.type = siteOwner.type;
            
            // check if it is in the chain
            if (siteOwner != treeSym.owner) {
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
                        if (types.isSameType(supType, treeSym.owner.type)) {
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
                            ret = callExpression(pos, ret, initBuilder.outerAccessorName);
                            ret.type = siteCursor.type;
                        }
                        
                        if (siteCursor.kind == Kinds.TYP) {
                            numOfOuters--;
                        }
                        siteCursor = siteCursor.owner;
                    }
                }
            }
        }
        return ret;
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
        return functionName( sym, sym.name.toString(), markAsImpl, isBound );
    }

    Name functionName(MethodSymbol sym, String full, boolean markAsImpl, boolean isBound) {
        if (isBound) {
            full = full  + JavafxDefs.boundFunctionDollarSuffix + getParameterTypeSuffix(sym);
        } else  if (markAsImpl) {
            full = full  + JavafxDefs.implFunctionSuffix;
        } 
        return names.fromString(full);
    }

    private String escapeTypeName(Type type) {
        return type.toString().replace(defs.typeCharToEscape, defs.escapeTypeChar);
    }
    
    private String getParameterTypeSuffix(MethodSymbol sym) {
        StringBuilder sb = new StringBuilder();
        if (sym != null && sym.type != null) {
            if (sym.type.tag == TypeTags.METHOD) {
                List<Type> argtypes = ((MethodType)sym.type).getParameterTypes();
                int argtypesCount = argtypes.length();
                int counter = 0;
                for (Type argtype : argtypes) {
                    sb.append(escapeTypeName(types.erasure(argtype)));
                    if (counter < argtypesCount - 1) { // Don't append type separator after the last type in the signature.
                        sb.append(defs.escapeTypeChar); // Double separator between type names.
                        sb.append(defs.escapeTypeChar);
                    }
                    counter ++;
                }
            }
        }
        return sb.toString();
    }

    // Fix up the owner of the ForeachInClause.var JFXVar symbol. When it is created it is set to be 
    // the outer ClassDeclaration and therefor is treated as an attribute instead of local var.
    static class ForEachInClauseOwnerFixer extends JavafxTreeScanner {
        Symbol currentSymbol;
        
        @Override
        public void visitVar(JFXVar tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitVar(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }
        
        @Override
        public void visitOverrideAttribute(JFXOverrideAttribute tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitOverrideAttribute(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }
        
        @Override
        public void visitOperationDefinition(JFXFunctionDefinition tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitOperationDefinition(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }
        
        @Override
        public void visitInitDefinition(JFXInitDefinition tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitInitDefinition(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }

        @Override
        public void visitPostInitDefinition(JFXPostInitDefinition tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitPostInitDefinition(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }

        @Override
        public void visitClassDeclaration(JFXClassDeclaration tree) {
            Symbol prevSymbol = currentSymbol;
            try {
                currentSymbol = tree.sym;
                super.visitClassDeclaration(tree);
            }
            finally {
                currentSymbol = prevSymbol;
            }
        }
        
        @Override
        public void visitVarDef(JCVariableDecl tree) {
            assert false : "should not be in JavaFX AST";
        }
        
        @Override
        public void visitMethodDef(JCMethodDecl tree) {
            assert false : "should not be in JavaFX AST";
        }
                        
        @Override
        public void visitClassDef(JCClassDecl tree) {
            assert false : "should not be in JavaFX AST";
        }
                
        @Override
        public void visitForExpressionInClause(JFXForExpressionInClause tree) {
            if (tree.var != null) {
                tree.var.sym.owner = currentSymbol;
            }
            super.visitForExpressionInClause(tree);
        }

    }

    private void fillClassesWithOuters(JCCompilationUnit tree) {
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
            public void visitIdent(JCIdent tree) {
                super.visitIdent(tree);
                if (currentClass != null && tree.sym.kind != Kinds.TYP) {
                    addOutersForOuterAccess(tree.sym, currentClass.sym);
                }
            }
            
            @Override
            public void visitSelect(JCFieldAccess tree) {
                super.visitSelect(tree);
                if (currentClass != null && tree.sym.kind != Kinds.TYP) {
                    addOutersForOuterAccess(tree.sym, currentClass.sym);
                }
            }

            @Override // Need this because JavafxTreeScanner is not visiting the args of the JFXInstanciate tree. Starting to visit them generate tons of errors.
            public void visitInstanciate(JFXInstanciate tree) {
                super.visitInstanciate(tree);
                if (tree.getArgs() != null) {
                    super.scan(tree.getArgs());
                }
            }

            private void addOutersForOuterAccess(Symbol sym, Symbol currentClass) {
                if (sym != null && sym.owner != null && sym.owner.type != null
                        && currentClass != null) {
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

    static JCModifiers addAccessAnnotationModifiers(long flags, JCModifiers mods, JavafxTreeMaker make) {
        JCModifiers ret = mods;
        ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
        if ((flags & Flags.PUBLIC) != 0) {
            annotations.append(make.Annotation(make.Identifier(publicAnnotationStr), List.<JCExpression>nil()));
        }
        else if ((flags & Flags.PRIVATE) != 0) {
            annotations.append(make.Annotation(make.Identifier(privateAnnotationStr), List.<JCExpression>nil()));
        }
        else if ((flags & Flags.PROTECTED) != 0) {        
            annotations.append(make.Annotation(make.Identifier(protectedAnnotationStr), List.<JCExpression>nil()));
        }                

        if ((flags & Flags.STATIC) != 0) {
            annotations.append(make.Annotation(make.Identifier(staticAnnotationStr), List.<JCExpression>nil()));
        }

        if (annotations.nonEmpty()) {
            ret = make.Modifiers(mods.flags, annotations.toList());
        }
        return ret;
    }

    public void visitTimeLiteral(JFXTimeLiteral tree) {
        // convert time literal to a javafx.lang.Time object literal
        JCFieldAccess clsname = (JCFieldAccess) makeQualifiedTree(tree.pos(), syms.javafx_TimeType.tsym.toString());
        clsname.type = syms.javafx_TimeType;
        clsname.sym = syms.javafx_TimeType.tsym;
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

    public void visitInterpolateExpression(JFXInterpolateExpression tree) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
