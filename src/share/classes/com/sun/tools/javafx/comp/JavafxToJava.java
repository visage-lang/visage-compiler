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
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.TranslatedAttributeInfo;
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
    private final TreeMaker make;  // should be generating Java AST, explicitly cast when not
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
    
    enum Wrapped {
        InLocation,
        InNothing
    }

    enum Convert {
        ToBound,
        Normal
    }

    class State {

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
    Type targetType = null;
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
            Name tmpName = getSyntheticName("seq");
            Name arrName = getSyntheticName("arr");
            ListBuffer<JCStatement> stats = ListBuffer.lb();
            DiagnosticPosition diagPos = tree.pos();
            JCExpression vartype = null;
            JCExpression init = (JCExpression) translate(tree);
            Type elemType = types.elemtype(type);
            if (elemType.isPrimitive()) {
                String mname = "toArray";
                if (elemType == syms.floatType)
                    mname = "toFloatArray";
                return callExpression(diagPos, makeTypeTree(syms.javafx_SequencesType, diagPos, false),
                       mname, init);
            }
            JCVariableDecl tmpVar = make.VarDef(make.Modifiers(0), tmpName,
                    makeTypeTree(tree.type, diagPos, true), init);
            stats.append(tmpVar);
            JCVariableDecl arrVar = make.VarDef(make.Modifiers(0), arrName,
                    makeTypeTree(type, diagPos, true),
                    make.NewArray(makeTypeTree(elemType, diagPos, true),
                        List.<JCExpression>of(callExpression(diagPos, make.Ident(tmpName), "size")), null));
            stats.append(arrVar);
            stats.append(callStatement(diagPos, make.Ident(tmpName), "toArray",
                            List.of(make.Ident(arrName), make.Literal(TypeTags.INT, 0))));
            JCIdent ident2 = make.Ident(arrName);
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
        this.targetType = targetType;
        state = newState;
       JCStatement ret = translateExpressionToStatement(expr);
        yield = prevYield;
        state = prevState;
        return ret;
    }

    JCStatement translateExpressionToStatement(JCExpression expr, Type targetType) {
        return translateExpressionToStatement(expr, state,  targetType);
    }

     private List<JCStatement> translateStatements(List<JCStatement> stmts, State newState) {
        State prevState = state;
        state = newState;
        List<JCStatement> ret = translateStatements(stmts);
        state = prevState;
        return ret;
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
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
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
                                ((select.sym) instanceof ClassSymbol) &&
                                types.isJFXClass((ClassSymbol)(select.sym))) {
                           imports.append(make.Import(make.Select(
                                        translate( select.selected ), 
                                        names.fromString(select.name.toString() + interfaceSuffix)), 
                                   false));
                        }
                    }  else if (((JCImport)def).getQualifiedIdentifier().getTag() == JCTree.IDENT) {
                        JCIdent ident = (JCIdent)((JCImport)def).getQualifiedIdentifier();
                        if (ident.name != names.asterisk && 
                                ((ident.sym) instanceof ClassSymbol) &&
                                types.isJFXClass((ClassSymbol)(ident.sym))) {
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

           // translate all the definitions that make up the class.
           // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCStatement> prevPrependToDefinitions = prependToDefinitions;
            ListBuffer<JCStatement> prevPrependToStatements = prependToStatements;
            prependToStatements = prependToDefinitions = ListBuffer.lb();
            {
                for (JCTree def : tree.getMembers()) {
                    switch(def.getTag()) {
                        case JavafxTag.INIT_DEF: {
                            translatedInitBlocks.append(translate((JCBlock) ((JFXInitDefinition) def).getBody()));
                            break;
                        }
                        case JavafxTag.POSTINIT_DEF: {
                            translatedPostInitBlocks.append(translate((JCBlock) ((JFXPostInitDefinition) def).getBody()));
                            break;
                        }
                        case JavafxTag.VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            JCTree trans = translate(attrDef);
                            JCExpressionTupple exprTupple = translateVarInit(attrDef, true);
                            attrInfo.append(new TranslatedAttributeInfo(
                                    attrDef,
                                    elementType(attrDef.type),
                                    exprTupple.first,
                                    exprTupple.args,
                                    translate(attrDef.getOnChanges())));
                            translatedDefs.append(trans);
                            break;
                        }
                       case JavafxTag.FUNCTION_DEF : {
                           JFXOperationDefinition funcDef = (JFXOperationDefinition)def;
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
            JavafxClassModel model = initBuilder.createJFXClassModel(tree, attrInfo.toList());
            additionalImports.appendList(model.additionalImports);
       
            boolean classIsFinal = (tree.getModifiers().flags & Flags.FINAL) != 0;
            
            // include the interface only once
            if (!tree.hasBeenTranslated) {
                if (! classOnly) {
                    JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.INTERFACE);
                    mods = addAccessAnnotationModifiers(tree.mods.flags, mods, (JavafxTreeMaker)make);

                    JCClassDecl cInterface = make.ClassDef(mods,
                            model.interfaceName, List.<JCTypeParameter>nil(), null,
                            model.interfaces.toList(), model.iDefinitions);

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
                for (ClassSymbol csym : model.baseClasses) {
                    if (types.isJFXClass(csym)) {
                        String className = csym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree(csym.type, tree.pos(), true), make.Ident(defs.receiverName)));
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
                for (ClassSymbol csym : model.baseClasses) {
                    if (types.isJFXClass(csym)) {
                        String className = csym.fullname.toString();
                        if (className.endsWith(interfaceSuffix)) {
                            className = className.substring(0, className.length() - interfaceSuffix.length());
                        }

                        if (!dupSet.contains(className)) {
                            dupSet.add(className);
                            List<JCExpression> args1 = List.nil();
                            args1 = args1.append(make.TypeCast(makeTypeTree(csym.type, tree.pos(), true), make.Ident(defs.receiverName)));
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
                translatedDefs.append(makeMainMethod(diagPos));
            }

            // build the list of implemented interfaces
            ListBuffer<JCExpression> implementing;
            if (classOnly) {
                implementing = model.interfaces;
            }
            else {
                implementing = ListBuffer.lb();
                implementing.append(make.Ident(model.interfaceName));
                implementing.append(makeIdentifier(fxObjectString));
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
                    implementing.toList(), 
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
            result = res;
            addBaseAttributes(tree.sym, res, model.attributes);  //TODO: remove this
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
        Name tmpName = getSyntheticName("objlit");
        JCExpression classTypeExpr;
        JCExpression tmpTypeExpr; // cloned so we don't use the same tree

        ListBuffer<JCStatement> stats = ListBuffer.lb();
        for (JFXVar var : tree.getLocalvars()) {
            // force var to be a Location (so class members can see it)
            typeMorpher.varMorphInfo(var.sym).markBoundTo();
            // add the variable before the class definition or object litersl assignment
            stats.append(translate(var));
        }
        if (tree.getClassBody() == null) {
            classTypeExpr = makeTypeTree(tree.type, tree, false);
            tmpTypeExpr = makeTypeTree(tree.type, tree, false);
        } else {
            JFXClassDeclaration cdef = tree.getClassBody();
            if (!inOperationDef) {
                prependToStatements.append( translate( cdef ) );  // prepend to the enclosing statements, class or top-level
            }
            else {
                stats.append(translate(cdef));
            }

            classTypeExpr = makeTypeTree(cdef.type, tree, false);
            tmpTypeExpr = makeTypeTree(cdef.type, tree, false);
         }

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
                JCVariableDecl tmpVar = make.VarDef(make.Modifiers(0), tmpName, tmpTypeExpr, newClass);
                stats.append(tmpVar);
                for (JFXObjectLiteralPart olpart : tree.getParts()) {
                    DiagnosticPosition diagPos = olpart.pos();
                    JavafxBindStatus bindStatus = olpart.getBindStatus();
                    JCExpression init = olpart.getExpression();
                    VarSymbol vsym = (VarSymbol) olpart.sym;
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);

                    // Lift JFXObjectLiteralPart if needed
                    if (types.isSequence(olpart.type)) {
                        if (!types.isSequence(olpart.expr.type)) {
                             init = ((JavafxTreeMaker)make).ExplicitSequence(List.<JCExpression>of(olpart.expr));
                             WildcardType tpType = new WildcardType(olpart.expr.type, BoundKind.EXTENDS, olpart.expr.type.tsym);
                            init.type = new ClassType(((JavafxSymtab)syms).javafx_SequenceType, List.<Type>of(tpType), ((JavafxSymtab)syms).javafx_SequenceType.tsym);
                         }
                    }

                    if (shouldMorph(vmi)) {
                        init = translateDefinitionalAssignment(diagPos, init, bindStatus, vsym, false).first;
                    }

                    JCIdent ident1 = make.at(diagPos).Ident(tmpName);
                    JCStatement lastStatement = callStatement(diagPos, ident1, attributeInitMethodNamePrefix + olpart.getName().toString(), init);
                    stats.append(lastStatement); 

                }

                JCIdent ident3 = make.Ident(tmpName);   
                JCStatement applyExec = callStatement(tree.pos(), ident3, defs.initializeName);
                stats.append(applyExec);

                JCIdent ident2 = make.Ident(tmpName);
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
            sb.append(format.length() == 0? "%s" : format);
            parts = parts.tail;
            
            values.append(translate(parts.head));           // expression
            parts = parts.tail;
            
            lit = (JCLiteral)(parts.head);                  // }...{  or  }..."
            sb.append((String)lit.value);
            parts = parts.tail;
        }
        JCLiteral formatLiteral = make.at(tree.pos).Literal(TypeTags.CLASS, sb.toString());
        JCExpression formatter = make.Ident(Name.fromString(names, "java"));
        for (String s : new String[] {"lang", "String", "format"}) {
            formatter = make.Select(formatter, Name.fromString(names, s));
        }
        values.prepend(formatLiteral);
        result = make.Apply(null, formatter, values.toList());
    }

    private JCExpressionTupple translateDefinitionalAssignment(DiagnosticPosition diagPos, JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, boolean isAttribute) {
        List<JCExpression> returnedDependencies = null;
        JCExpression initExpr;

        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        if (shouldMorph(vmi)) {

            if (bindStatus.isUnidiBind()) {
                List<JCExpression> dependencies = typeMorpher.buildDependencies(init);
                List<JCExpression> initDependencies;

                if (isAttribute) {
                    initDependencies = List.nil();
                    returnedDependencies = dependencies;
                } else {
                    initDependencies = dependencies;
                    returnedDependencies = null;
                }
                ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
                bindingExpressionDefs = ListBuffer.lb();

                JCStatement stmt = translateExpressionToStatement(init, Wrapped.InNothing, Convert.ToBound, vmi.getRealType());
                initExpr = typeMorpher.buildExpression(diagPos, vmi, stmt, bindStatus.isLazy(), initDependencies);

                assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
                bindingExpressionDefs = prevBindingExpressionDefs;
            } else if (bindStatus.isBidiBind()) {
                // Bi-directional bind translate so it stays in a Location
                initExpr = translate(init, Wrapped.InLocation, Convert.ToBound);
            } else {
                // normal init -- unbound -- but it is setting a Location
                if (init == null) {
                    // no initializing expression determine a default value from the type
                    initExpr = makeDefaultValue(diagPos, vmi);
                } else {
                    // do a vanilla translation of the expression
                    initExpr = translate(init, new State());
                }
                // convert the expression to a Location
                initExpr = makeIntoLocation(diagPos, vmi, initExpr);
            }
        } else {
            // normal init -- unbound -- setting a non-Location
            assert !state.isBound();
            assert !bindStatus.isBound();
            if (init == null) {
                // no initializing expression determine a default value from the type
                initExpr = makeDefaultValue(diagPos, vmi);
            } else {
                // do a vanilla translation of the expression
                initExpr = translate(init, new State());
            }
        }

        return new JCExpressionTupple(initExpr, returnedDependencies);
    }

    public JCExpressionTupple translateVarInit(JFXVar tree, boolean isAttribute) {
        return translateDefinitionalAssignment(tree.pos(), tree.init, 
                                                tree.getBindStatus(), tree.sym, isAttribute);
    }

    JCExpression makeIntoLocation(DiagnosticPosition diagPos, TypeMorphInfo tmi, JCExpression expr) {
        List<JCExpression> makeArgs = List.of(expr);
        if (tmi.typeKind == TYPE_KIND_SEQUENCE) {
            makeArgs = makeArgs.prepend(  makeSequenceClassArg(diagPos, tmi) );
        }
        return  typeMorpher.makeCall(tmi, diagPos, makeArgs, typeMorpher.varLocation, defs.makeMethodName);
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
        long modFlags = mods==null? 0L : mods.flags;
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
            type = vmi.getMorphedType();

            // local variables need to be final so they can be referenced
            // attributes cannot be final since they are initialized outside of the constructor
            if (isClassVar) {
                modFlags &= ~Flags.FINAL;
            } else {
                modFlags |= Flags.FINAL;
            }
        }
        
        mods = make.at(diagPos).Modifiers(modFlags);
        JCExpression typeExpression = makeTypeTree(type, diagPos, true);

        // for class vars, initialization happens during class init, so remove
        // from here.  For local vars translate as definitional
        JCExpression init = null;
        if (!isClassVar && ((vsym.flags() & Flags.PARAMETER) == 0)) {
            if (isBound && locallyBound != null) {
                locallyBound.add(vsym);
            }
            init = translateVarInit(tree, false).first;
        }

        // Convert initializers returning Java arrays to sequences.
        if (type.tag == TypeTags.ARRAY) {
            JCExpression newTree = ((JCExpression)makeTypeTree(((ArrayType)type).elemtype, diagPos, types.isJFXClass(((ArrayType)type).elemtype.tsym)));
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
    public void visitOnReplace(JFXOnReplace tree) {
        boolean prev = inOperationDef;
        try {
            inOperationDef = true;
            JFXVar oldVal = tree.getOldValue();
            result = ((JavafxTreeMaker)make).OnReplace(
                oldVal,
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
    public void visitOperationValue(JFXOperationValue tree) {
        JFXOperationDefinition def = tree.definition;
        result = makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }
    
   JCExpression makeFunctionValue (JCExpression meth, JFXOperationDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
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
    public void visitOperationDefinition(JFXOperationDefinition tree) {
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
            JFXOperationDefinition def = new JFXOperationDefinition(make.Modifiers(Flags.SYNTHETIC), tree.name, tree.operation);
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
        List<JCStatement> defs = translated.toList();
        
        if (yield == Yield.ToExpression) {
            assert (tree.type != syms.voidType) : "void block expressions should be handled below";
            JCExpression tvalue = translate(value); // must be before prepend
            defs = prependToStatements.appendList(defs).toList();
            result = ((JavafxTreeMaker) make).at(tree.pos).BlockExpression(tree.flags, defs, tvalue);
        } else {
            prependToStatements.appendList(defs);
            if (value != null) {
                prependToStatements.append( translateExpressionToStatement(value) );
            }
            defs = prependToStatements.toList();
            result = defs.size() == 1? defs.head : make.at(diagPos).Block(0L, defs);
        }
        prependToStatements = prevPrependToStatements;
    }
 
    @Override
    public void visitBlock(JCBlock tree) {
        List<JCStatement> defs = translateStatements(tree.stats);
	result = make.at(tree.pos).Block(tree.flags, defs);
    }

    @Override
    public void visitAssign(JCAssign tree) {
        DiagnosticPosition diagPos = tree.pos();
        
        Symbol sym = expressionSymbol(tree.lhs);
        VarSymbol vsym = (sym != null && (sym instanceof VarSymbol))? (VarSymbol)sym : null;
        
        JCExpression rhs = translate(tree.rhs);
        if (tree.lhs.getTag() == JavafxTag.SEQUENCE_INDEXED) {
            // assignment of a sequence element --  s[i]=8, call the sequence set method
            JFXSequenceIndexed si = (JFXSequenceIndexed)tree.lhs;
            JCExpression seq = translate(si.getSequence(), Wrapped.InLocation); 
            JCExpression index = translate(si.getIndex());
            JCFieldAccess select = make.Select(seq, defs.setMethodName);
            List<JCExpression> args = List.of(index, rhs);
            result = make.at(diagPos).Apply(null, select, args);
        } else if (shouldMorph(vsym)) {
            // we are setting a var Location, call the set method
            JCExpression lhs = translate(tree.lhs, Wrapped.InLocation);
            JCFieldAccess setSelect = make.Select(lhs, defs.setMethodName);
            List<JCExpression> setArgs = List.of(rhs);
            result = make.at(diagPos).Apply(null, setSelect, setArgs);
        } else {
            // We are setting a "normal" non-Location, use normal assign
            JCExpression lhs = translate(tree.lhs);
            result = make.at(diagPos).Assign(lhs, rhs); // make a new one so we are non-destructive
        }
    }

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
            JCFieldAccess setSelect = make.Select(targetLHS, defs.setMethodName);
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
            Name selectedTmpName = getSyntheticName("tg");
            JCVariableDecl selectedTmpDecl =
                make.VarDef(make.Modifiers(FINAL/*|SYNTHETIC*/), selectedTmpName,
                    makeTypeTree(selectedType, diagPos, true),
                    translatedSelected);
            JCExpression translated = make.at(diagPos).Select(make.Ident(selectedTmpName), tree.getIdentifier());
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
            translatedSelected = makeTypeTree(tree.selected.type, tree, false);
            staticReference = true;
        }

        JCFieldAccess translated = make.at(diagPos).Select(translatedSelected, tree.getIdentifier());
        //TODO: the below is curently untrue -- remove these
        // since this tree will be morphed, we need to copy the sym info
        translated.sym = tree.sym;
        translated.type = tree.type;

        //TODO: there is probably a cleaner way to do this -- e.g., state.isBound() is probably redundant
        // only make dynamic dependencies if we are in a binding expression (bindingExpressionDefs != null)
        boolean createDynamicDependencies = state.isBound() && !staticReference && bindingExpressionDefs != null;
        
        JCExpression ref = typeMorpher.convertVariableReference(
                diagPos,
                translated,
                tree.sym, 
                staticReference , 
                state.wantLocation(),
                createDynamicDependencies);
        if (dummyReference != null)
            ref = ((JavafxTreeMaker)make).BlockExpression(
                0L, dummyReference, ref);
        if (generateNullChecks && !staticReference
                                           && (tree.sym instanceof VarSymbol) 
                                           && types.isJFXClass(selectedType.tsym)) {
            // we have a testable guard for null, wrap the attribute access  in it, return default value if null
            TypeMorphInfo tmi = typeMorpher.typeMorphInfo(tree.type);
            JCExpression defaultExpr = makeDefaultValue(diagPos, tmi);
            if (state.wantLocation()) {
                defaultExpr = makeIntoLocation(diagPos, tmi, defaultExpr);
            }
            JCExpression cond = make.at(diagPos).Binary(
                JCTree.EQ, 
                translate(selected, Wrapped.InNothing), 
                make.Literal(TypeTags.BOT, null));
            ref = make.at(diagPos).Conditional(cond, defaultExpr, ref);
        }
        result = ref;
    }
    
    @Override
    public void visitIdent(JCIdent tree)   {
       DiagnosticPosition diagPos = tree.pos();
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JFXOperationDefinition def = null; // FIXME
            result = makeFunctionValue(make.Ident(functionName((MethodSymbol)tree.sym)), def, tree.pos(), mtype);
            return;
        }
        if (tree.name == names._this) {
            // in the static implementation method, "this" becomes "receiver$"
            result = make.at(diagPos).Ident(defs.receiverName);
            return;
        } else if (tree.name == names._super) {
            if (tree.type != null &&
                    types.isJFXClass(tree.type.tsym)) {
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
            if (types.isJFXClass(tree.sym.owner)) {
                convert = make.at(diagPos).Select(makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym), tree.name);
            }
            else {
                JCExpression mRec = makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym);
                convert = make.at(diagPos).Select(make.at(diagPos).TypeCast(makeTypeTree(tree.sym.owner.type, diagPos), mRec), tree.name);
            }
        } else {
            convert = make.at(diagPos).Ident(tree.name);
        }
        
        result = typeMorpher.convertVariableReference(diagPos, convert, tree.sym, tree.sym.isStatic(), state.wantLocation(), false);
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
        stmts.append(builder.makeTmpVar());
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
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        result = callStatement(tree, 
                translate( tree.getSequence(), Wrapped.InLocation ), 
                "insert", 
                translate( tree.getElement() ));
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        JCExpression seq = tree.getSequence();
        JCExpression seqLoc = translate(seq, Wrapped.InLocation);
        if (tree.getIndex() != null) { 
            result = callStatement(tree.pos(),  seqLoc,  "delete",   translate( tree.getIndex() ));
        } else if (tree.getElement() != null) { 
            result = callStatement(tree.pos(),  seqLoc,  "deleteValue",  translate( tree.getElement() ));
        } else {
            if (types.isSequence(seq.type))
                result = callStatement(tree.pos(), seqLoc,  "deleteAll");
            else {
                make.at(tree.pos());
                // Bit of a KLUDGE ...
                visitAssign(make.Assign(seq, make.Literal(TypeTags.BOT, null)));
                result = make.Exec((JCExpression) result);
            }
        }
    }

    /**** utility methods ******/
    
    /**
     * For an attribute "attr" make an access to it via the receiver and getter  
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, JFXVar attrib) {
       return callExpression(diagPos, 
                make.Ident(defs.receiverName),
                attributeGetMethodNamePrefix + attrib.getName());
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

    JCMethodDecl makeMainMethod(DiagnosticPosition diagPos) {
            List<JCExpression> emptyExpressionList = List.nil();
            JCIdent runIdent = make.at(diagPos).Ident(defs.runMethodName);
            JCMethodInvocation runCall = make.at(diagPos).Apply(emptyExpressionList, runIdent, emptyExpressionList);
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
        
        JCStatement makeTmpVar() {
            // Build the type declaration expression for the sequence builder
            if (elemType.tsym != null &&
                    elemType.tsym instanceof ClassSymbol &&
                    types.isJFXClass((ClassSymbol)elemType.tsym)) {
                String str = elemType.tsym.flatName().toString().replace("$", ".");
                String strLookFor = str + interfaceSuffix;
                elemType = typeMorpher.reader.enterClass(names.fromString(strLookFor)).type;

            }

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
        
        JCIdent makeTmpVarAccess() {
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
                        makeTmpVarAccess(), 
                        Name.fromString(names, "add")), 
                    List.<JCExpression>of(expr));
            return make.at(diagPos).Exec(addCall);
        }

        JCExpression makeToSequence() {
            return make.Apply(
                List.<JCExpression>nil(), // type arguments
                make.at(diagPos).Select(
                    makeTmpVarAccess(), 
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
                    makeTypeTree(wtype.type, diagPos, makeIntf));
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
    
    public void visitAnnotation(JCAnnotation tree) {
        JCTree annotationType = translate(tree.annotationType);
        List<JCExpression> args = translate(tree.args);
        result = make.at(tree.pos).Annotation(annotationType, args);
    }

    public void visitAssert(JCAssert tree) {
        JCExpression cond = translate(tree.cond);
        JCExpression detail = translate(tree.detail);
        result = make.at(tree.pos).Assert(cond, detail);
    }

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
                            return make.at(diagPos).Literal(true);
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
                } else {
                    // anything other than == or <>
                    final JCExpression lhs = translate(tree.lhs);
                    final JCExpression rhs = translate(tree.rhs);
                    return makeBinary(tree.getTag(), lhs, rhs);
                }
            }

        }).doit();
    }

    public void visitBreak(JCBreak tree) {
        result = make.at(tree.pos).Break(tree.label);
    }

   public void visitCase(JCCase tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitCatch(JCCatch tree) {
        JCVariableDecl param = translate(tree.param);
        JCBlock body = translate(tree.body);
        result = make.at(tree.pos).Catch(param, body);
    }

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
            stmts.append(builder.makeTmpVar());

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
                    makeTypeTree(var.type, var, true), 
                    make.Ident(tmpVarName));
            stmt = make.Block(0L, List.of(finalVar, stmt));
            stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    make.VarDef(
                        make.Modifiers(0L), 
                        tmpVarName, 
                        makeTypeTree(var.type, var, true), 
                        null),
                    translate(clause.getSequenceExpression()),
                    stmt);
        }
        return stmt;
    }
    
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

    public void visitContinue(JCContinue tree) {
        result = make.at(tree.pos).Continue(tree.label);
    }

    public void visitDoLoop(JCDoWhileLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitErroneous(JCErroneous tree) {
        List<? extends JCTree> errs = translate(tree.errs);
        result = make.at(tree.pos).Erroneous(errs);
    }
   
    public void visitReturn(JCReturn tree) {
        JCExpression exp = tree.getExpression();
        if (exp == null) {
            result = make.at(tree).Return(null);
        } else {
            result = translateExpressionToStatement(exp, tree.type);
        }
    }

    public void visitExec(JCExpressionStatement tree) {
        result = translateExpressionToStatement(tree.getExpression(), syms.voidType);
    }

    public void visitParens(JCParens tree) {
        if (yield == Yield.ToExpression) {
            JCExpression expr = translate(tree.expr);
            result = make.at(tree.pos).Parens(expr);
        } else {
            result = translateExpressionToStatement(tree.expr);
        }
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitForLoop(JCForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitIf(JCIf tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitImport(JCImport tree) {
        JCTree qualid = translate(tree.qualid);
        result = make.at(tree.pos).Import(qualid, tree.staticImport);
    }

    public void visitIndexed(JCArrayAccess tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitTypeTest(JCInstanceOf tree) {
        JCTree clazz = this.makeTypeTree(tree.clazz.type, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeTest(expr, clazz);
    }

    @Override
    public void visitTypeCast(JCTypeCast tree) {
        JCTree clazz = this.makeTypeTree(tree.clazz.type, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeCast(clazz, expr);
    }
    
    public void visitLabelled(JCLabeledStatement tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitLiteral(JCLiteral tree) {
        result = make.at(tree.pos).Literal(tree.typetag, tree.value);
    }

    public void visitMethodDef(JCMethodDecl tree) {
         assert false : "should not be in JavaFX AST";
   }

    public void visitApply(final JCMethodInvocation tree) {
        result = (new Translator() {

            private final DiagnosticPosition diagPos = tree.pos();
            private final JCExpression meth = tree.meth;
            private final JCExpression selector = meth.getTag() == JavafxTag.SELECT?   
                                                                    ((JCFieldAccess) meth).getExpression() :  
                                                                    null;
            private final Symbol sym = expressionSymbol(meth);
            private final MethodSymbol msym = (sym instanceof MethodSymbol)? (MethodSymbol)sym : null;
            private final Name selectorIdName = (selector != null && selector.getTag() == JavafxTag.IDENT)? ((JCIdent) selector).getName() : null;
            private final boolean superCall = selectorIdName == names._super;
            private final boolean thisCall = selectorIdName == names._this;
            private final List<Type> formals = meth.type.getParameterTypes();

            private JCExpression transMeth = translate(meth);

            private final boolean useInvoke = meth.type instanceof FunctionType 
                                                                    || (transMeth instanceof JCIdent
                                                                                 && ((JCIdent) transMeth).sym instanceof MethodSymbol 
                                                                                 && isInnerFunction((MethodSymbol) ((JCIdent) transMeth).sym));

            private final boolean testForNull =  generateNullChecks && msym!=null  && !sym.isStatic() && selector!=null && !superCall && !thisCall && !useInvoke;

            private final boolean callBound = generateBoundFunctions
                    && state.isBound()
                    && msym != null
                    && types.isJFXClass(msym.owner)
                    && (generateBoundVoidFunctions || msym.getReturnType() != syms.voidType )
                    && !useInvoke;

            public JCTree doit() {
                 // translate the method name -- e.g., foo  to foo$bound or foo$impl
                if (superCall || callBound) {
                    Name name = functionName(msym, superCall, callBound);
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
                if (testForNull && !selector.type.isPrimitive()) {
                    // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
                    JCExpression cond = make.at(diagPos).Binary(JCTree.NE, translate(selector), make.Literal(TypeTags.BOT, null));
                    if (msym.getReturnType() == syms.voidType) {
                        // if this is a void expression, check it with an If-statement
                        JCStatement stmt = make.If(cond, make.Exec(fresult), null);
                        if (yield == Yield.ToExecStatement) {
                            // if a statement is the desired result of the translation, return the If-statement
                            return stmt;
                        } else {
                            // otherwise wrap it in a block expression to make it into an expression
                            //TODO: should this case ever happen?
                            return makeBlockExpression(diagPos, ListBuffer.<JCStatement>lb().append(stmt), null);
                        }
                    } else {
                        // it has a non-void return type, convert it to a conditional expression
                        // if it would dereference null, then instead give the default value
                        TypeMorphInfo tmi = typeMorpher.typeMorphInfo(msym.getReturnType());
                        JCExpression defaultExpr = makeDefaultValue(diagPos, tmi);
                        if (state.wantLocation()) {
                            defaultExpr = makeIntoLocation(diagPos, tmi, defaultExpr);
                        }
                        return make.at(diagPos).Conditional(cond, fresult, defaultExpr);
                    }
                } else {
                    return fresult;
                }
            }

            // compute the translated arguments.
            // if this is a bound call, use left-hand side references for arguments consisting
            // solely of a  var or attribute reference, or function call, otherwise, wrap it
            // in an expression location
            List<JCExpression> determineArgs() {
                List<JCExpression> args;
                if (callBound) {
                    ListBuffer<JCExpression> targs = ListBuffer.lb();
                    for (JCExpression arg : tree.args) {
                        switch (arg.getTag()) {
                            case JavafxTag.IDENT:
                            case JavafxTag.SELECT:
                            case JavafxTag.APPLY:
                                targs.append(translate(arg, Wrapped.InLocation));
                                break;
                            default:
                                {
                                    ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
                                    bindingExpressionDefs = ListBuffer.lb();
                                    targs.append(typeMorpher.buildExpression(
                                                                    arg.pos(), 
                                                                    typeMorpher.typeMorphInfo(arg.type), 
                                                                    translateExpressionToStatement(arg, arg.type), 
                                                                    false, 
                                                                    typeMorpher.buildDependencies(arg)));
                                    assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
                                    bindingExpressionDefs = prevBindingExpressionDefs;
                                    break;
                                }
                        }
                    }
                    args = targs.toList();
                } else {
                    args = translate(tree.args, formals);
                }

               // if this is a super.foo(x) call, "super" will be translated to referenced class,
                // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
                if (superCall) {
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
                Type morphedReturnType = typeMorpher.typeMorphInfo(msym.getReturnType()).getMorphedType();
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
                                                                    defs.getMethodName);
            }

        }).doit();
    }

    public void visitModifiers(JCModifiers tree) {
        List<JCAnnotation> annotations = translate(tree.annotations);
        result = make.at(tree.pos).Modifiers(tree.flags, annotations);
    }

    public void visitNewArray(JCNewArray tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitNewClass(JCNewClass tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitSkip(JCSkip tree) {
        result = make.at(tree.pos).Skip();
    }

    public void visitSwitch(JCSwitch tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitSynchronized(JCSynchronized tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitThrow(JCThrow tree) {
        JCTree expr = translate(tree.expr);
        result = make.at(tree.pos).Throw(expr);
    }

    public void visitTry(JCTry tree) {
        JCBlock body = translate(tree.body);
        List<JCCatch> catchers = translate(tree.catchers);
        JCBlock finalizer = translate(tree.finalizer);
        result = make.at(tree.pos).Try(body, catchers, finalizer);
    }

    public void visitTypeApply(JCTypeApply tree) {
        JCExpression clazz = translate(tree.clazz);
        List<JCExpression> arguments = translate(tree.arguments);
        result = make.at(tree.pos).TypeApply(clazz, arguments);
    }

    public void visitTypeArray(JCArrayTypeTree tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        result = make.at(tree.pos).TypeIdent(tree.typetag);
    }

    public void visitTypeParameter(JCTypeParameter tree) {
        List<JCExpression> bounds = translate(tree.bounds);
        result = make.at(tree.pos).TypeParameter(tree.name, bounds);
    }

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
                JCFieldAccess setSelect = make.Select(transTarget, defs.setMethodName);
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
                        if (types.isSequence(expr.type)) {
                            return callExpression(diagPos, transExpr, "size");
                        } else {
                            return callExpression(diagPos, makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"), "size", transExpr);
                        }
                    case JCTree.PREINC:
                        return doIncDec(JCTree.PLUS, false);
                    case JCTree.PREDEC:
                        return doIncDec(JCTree.MINUS, false);
                    case JCTree.POSTINC:
                        return doIncDec(JCTree.PLUS, true);
                    case JCTree.POSTDEC:
                        return doIncDec(JCTree.MINUS, true);
                    default:
                        return doVanilla();
                }
            }
        }).doit();
    }
  

    public void visitVarDef(JCVariableDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitWhileLoop(JCWhileLoop tree) {
        JCStatement body = translate(tree.body);
        JCExpression cond = translate(tree.cond);
        result = make.at(tree.pos).WhileLoop(cond, body);
    }

    public void visitWildcard(JCWildcard tree) {
        TypeBoundKind kind = make.at(tree.kind.pos).TypeBoundKind(tree.kind.kind);
        JCTree inner = translate(tree.inner);
        result = make.at(tree.pos).Wildcard(kind, inner);
    }

    public void visitLetExpr(LetExpr tree) {
        assert false : "should not be in JavaFX AST";
    }

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
    private JCBlock boundMethodBody(DiagnosticPosition diagPos, JFXBlockExpression bexpr, JFXOperationDefinition func) {
        JCStatement ret;
        
        ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
        bindingExpressionDefs = ListBuffer.lb();
        BindAnalysis analysis = typeMorpher.bindAnalysis(bexpr);
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
        return typeMorpher.buildExpression(
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
                while (siteCursor.kind != Kinds.PCK) {            
                    ListBuffer<Type> supertypes = ListBuffer.lb();
                    Set<Type> superSet = new HashSet<Type>();
                    if (siteCursor.type != null) {
                        supertypes.append(siteCursor.type);
                        superSet.add(siteCursor.type);
                    }

                    if (siteCursor.kind == Kinds.TYP) {
                        rs.getSupertypes(siteCursor, types, supertypes, superSet);
                    }

                    if (superSet.contains(treeSym.owner.type)) {
                        foundOwner = true;
                        break;
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

    //TODO: destructive -- remove
    private void addBaseAttributes(ClassSymbol sym, JCClassDecl result, java.util.List<Symbol> attrSyms) {
        if (attrSyms != null) {
            for (Symbol attrSym : attrSyms) {
                if (attrSym.kind == Kinds.MTH) {
                    VarSymbol varSym = new VarSymbol(0L, attrSym.name, ((MethodType)attrSym.type).restype, attrSym.owner);
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(varSym);
                    result.defs = result.defs.append(make.VarDef(make.Modifiers(0L), 
                            names.fromString(attrSym.name.toString().substring(attributeGetMethodNamePrefix.length())),
                           makeTypeTree(vmi.getMorphedType(), result), null));
                }
                else if (attrSym.kind == Kinds.VAR && attrSym.owner != sym) {
                    JCVariableDecl var = make.VarDef((VarSymbol)attrSym, null);
                    // Made all the operations public. Per Brian's spec.
                    // If they are left package level it interfere with Multiple Inheritance
                    // The interface methods cannot be package level and an error is reported.
                    {
                        var.mods.flags &= ~Flags.PROTECTED;
                        var.mods.flags &= ~Flags.PRIVATE;
                        var.mods.flags |= Flags.PUBLIC;
                    }
                    VarMorphInfo vmi = typeMorpher.varMorphInfo((VarSymbol)attrSym);
                    var.vartype = makeTypeTree(vmi.getMorphedType(), result);
                    result.defs = result.defs.append(var);
                }
            }
        }
    }
    
    static class JCExpressionTupple {
        JCExpression first;
        List<JCExpression> args;
        
        JCExpressionTupple(JCExpression first, List<JCExpression> args) {
            this.first = first;
            this.args = args;
        }
    }

    // Fix up the owner of the ForeachInClause.var JFXVar symbol. When it is created it is set to be 
    // the outer ClassDeclaration and therefor is treated as an attribute instead of local var.
    class ForEachInClauseOwnerFixer extends JavafxTreeScanner {
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
        public void visitOperationDefinition(JFXOperationDefinition tree) {
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
                            ListBuffer<Type> supertypes = ListBuffer.lb();
                            Set<Type> superSet = new HashSet<Type>();
                            supertypes.append(outerSym.type);
                            superSet.add(outerSym.type);
                            rs.getSupertypes(outerSym, types, supertypes, superSet);

                            if (superSet.contains(sym.owner.type)) {
                                foundOuterOwner = true;
                                break;
                             }
                            potentialOuters.append((ClassSymbol)outerSym);
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
        ListBuffer<JCAnnotation> annotations;
        if ((flags & Flags.PUBLIC) != 0) {
            annotations = ListBuffer.<JCAnnotation>lb();
            annotations.append(make.Annotation(make.Identifier(publicAnnotationStr), List.<JCExpression>nil()));
            ret = make.Modifiers(mods.flags, annotations.toList());
        }
        else if ((flags & Flags.PRIVATE) != 0) {
            annotations = ListBuffer.<JCAnnotation>lb();
            annotations.append(make.Annotation(make.Identifier(privateAnnotationStr), List.<JCExpression>nil()));
            ret = make.Modifiers(mods.flags, annotations.toList());
        }
        else if ((flags & Flags.PROTECTED) != 0) {        
            annotations = ListBuffer.<JCAnnotation>lb();
            annotations.append(make.Annotation(make.Identifier(protectedAnnotationStr), List.<JCExpression>nil()));
            ret = make.Modifiers(mods.flags, annotations.toList());
        }                
        
        return ret;
    }
}
