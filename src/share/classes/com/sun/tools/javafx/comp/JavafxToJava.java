/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeTranslator;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.javafx.api.JavafxBindStatus;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.TranslatedAttributeInfo;
import com.sun.tools.javafx.comp.JavafxInitializationBuilder.JavafxClassModel;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.JavafxTreeMaker; // only for BlockExpression
import static com.sun.tools.javac.code.Flags.*;
import java.util.HashSet;
import java.util.Set;
import java.io.OutputStreamWriter;

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
    private final Types types;
    private final JavafxSymtab syms;
    private final JavafxInitializationBuilder initBuilder;
    final JavafxTypeMorpher typeMorpher;
    final private JavafxModuleBuilder moduleBuilder;

    /*
     * other instance information
     */
    private int syntheticNameCounter = 0;
    private ListBuffer<JCTree> prependInFrontOfDef = null;
    
    // for type morphing
    private JavafxBindStatus bindContext = JavafxBindStatus.UNBOUND;
    private boolean inLHS = false;
    private JavafxEnv<JavafxAttrContext> attrEnv;
    private Target target;
    private JavafxResolve rs;

    /*
     * static information
     */
    private static final String sequencesMakeString = "com.sun.javafx.runtime.sequence.Sequences.make";
    private static final String sequencesRangeString = "com.sun.javafx.runtime.sequence.Sequences.range";
    private static final String sequencesRangeExclusiveString = "com.sun.javafx.runtime.sequence.Sequences.rangeExclusive";
    private static final String sequencesEmptyString = "com.sun.javafx.runtime.sequence.Sequences.emptySequence";
    private static final String sequenceBuilderString = "com.sun.javafx.runtime.sequence.SequenceBuilder";
    private static final String toSequenceString = "toSequence";
    private static final String methodThrowsString = "java.lang.Throwable";
    private static final String syntheticNamePrefix = "jfx$$";
    private static final String staticFunctionSuffix = "$static";
    private static final String boundFunctionSuffix = "$bound";
    private JFXClassDeclaration currentClass;  //TODO: this is redundant with attrEnv.enclClass
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();

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
        types = Types.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);
        target = Target.instance(context);
        rs = JavafxResolve.instance(context);
        moduleBuilder = JavafxModuleBuilder.instance(context);
    }
    
    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends JCTree> T translate(T tree) {
	if (tree == null) {
	    return null;
	} else {
	    tree.accept(this);
	    JCTree result = this.result;
	    this.result = null;
	    return (T)result; // XXX cast
	}
    }

    /** Visitor method: translate a list of nodes.
     */
    public <T extends JCTree> List<T> translate(List<T> trees) {
        ListBuffer<T> translated = ListBuffer.<T>lb();
	if (trees == null) return null;
	for (List<T> l = trees; l.nonEmpty();  l = l.tail) {
            T tree = translate(l.head);
            if (tree != null) {
                translated.append( tree );
            }
        }
	return translated.toList();
    }

    private  <T extends JCTree> T boundTranslate(T tree, JavafxBindStatus bind) {
        JavafxBindStatus prevBindContext = bindContext;
        
        bindContext = bind;
        T result = translate(tree);
        bindContext = prevBindContext;
        return result;
    }
    
    <T extends JCTree> T translateLHS(T tree) {
        return translateLHS(tree, true);
    }
    
    <T extends JCTree> T translateLHS(T tree, boolean isImmediateLHS) {
        boolean wasInLHS = inLHS;
        inLHS = isImmediateLHS;
        T result = translate(tree);
        inLHS = wasInLHS;
        return result;
    }
    
    public void toJava(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;
        syntheticNameCounter = 0;
        
        attrEnv.toplevel = translate(attrEnv.toplevel);
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        // add to the hasOuters set the clas symbols for classes that need a reference to the outer class
        fillClassesWithOuters(tree);
        
       ListBuffer<JCTree> tdefs= ListBuffer.<JCTree>lb();
       ListBuffer<JCTree> imports= ListBuffer.<JCTree>lb();
       prependInFrontOfDef = ListBuffer.lb();
       for (JCTree def : tree.defs) {
            if (def.getTag() == JCTree.IMPORT) {
                imports.append(def);
                 if (!((JCImport)def).isStatic()) {
                    if (((JCImport)def).getQualifiedIdentifier().getTag() == JCTree.SELECT) {
                        JCFieldAccess select = (JCFieldAccess)((JCImport)def).getQualifiedIdentifier();
                        if (select.name != names.asterisk && 
                                ((select.sym) instanceof ClassSymbol) &&
                                initBuilder.isJFXClass((ClassSymbol)(select.sym))) {
                           imports.append(make.Import(make.Select(select.selected, names.fromString(select.name.toString() + initBuilder.interfaceNameSuffix)), false));
                        }
                    }  else if (((JCImport)def).getQualifiedIdentifier().getTag() == JCTree.IDENT) {
                        JCIdent ident = (JCIdent)((JCImport)def).getQualifiedIdentifier();
                        if (ident.name != names.asterisk && 
                                ((ident.sym) instanceof ClassSymbol) &&
                                initBuilder.isJFXClass((ClassSymbol)(ident.sym))) {
                            imports.append(make.Import(make.Ident(names.fromString(ident.name.toString() + initBuilder.interfaceNameSuffix)), false));
                        }
                    }
                }
            } else {
                // anything but an import, just translate it
                tdefs.append( translate(def) );
            }
        }
       // order is imports, any prepends, then the translated definitions
       tdefs = imports.appendList(prependInFrontOfDef).appendList(tdefs);
        prependInFrontOfDef = null; // shouldn't be used again until the next top level

 	JCExpression pid = tree.pid;  //translate(tree.pid);
        JCCompilationUnit translated = make.at(tree.pos).TopLevel(List.<JCAnnotation>nil(), pid, tdefs.toList());
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
        JavafxBindStatus prevBindContext = bindContext;
        boolean prevInLHS = inLHS;
        currentClass = tree;

        new ForEachInClauseOwnerFixer().scan(tree);

        try {
            DiagnosticPosition diagPos = tree.pos();

            attrEnv.enclClass = tree;
            bindContext = JavafxBindStatus.UNBOUND;
            inLHS = false;

            ListBuffer<JCStatement> translatedInitBlocks = ListBuffer.<JCStatement>lb();
            ListBuffer<JCTree> translatedDefs = ListBuffer.<JCTree>lb();
            ListBuffer<TranslatedAttributeInfo> attrInfo = ListBuffer.<TranslatedAttributeInfo>lb();

           // translate all the definitions that make up the class.
           // collect any prepended definitions, and prepend then to the tranlations
            ListBuffer<JCTree> prevPrependInFrontOfDef = prependInFrontOfDef;
            prependInFrontOfDef = ListBuffer.lb();
            {
                for (JCTree def : tree.getMembers()) {
                    switch(def.getTag()) {
                        case JavafxTag.INIT_DEF: {
                            JFXInitDefinition initDef = (JFXInitDefinition) def;
                            translatedInitBlocks.append(translate((JCBlock)initDef.getBody()));
                            break;
                        }
                        case JavafxTag.VAR_DEF: {
                            JFXVar attrDef = (JFXVar) def;
                            JCTree trans = translate(attrDef);
                            JCExpressionTupple exprTupple = translateVarInit(attrDef, true);
                            attrInfo.append(new TranslatedAttributeInfo(
                                    attrDef, 
                                    exprTupple.first,
                                    exprTupple.args,
                                    translate(attrDef.getOnChanges())));
                            translatedDefs.append(trans);
                            break;
                        }
                       case JavafxTag.FUNCTION_DEF : {
                           JFXOperationDefinition funcDef = (JFXOperationDefinition)def;
                            translatedDefs.append(  translate(funcDef) );
//                            if ((funcDef.sym.flags() & Flags.SYNTHETIC) == 0) {
//                                translatedDefs.append(  boundTranslate(funcDef, JavafxBindStatus.UNIDIBIND) );
//                            }
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
            translatedDefs = prependInFrontOfDef.appendList(translatedDefs);
            prependInFrontOfDef = prevPrependInFrontOfDef;

            JavafxClassModel model = initBuilder.createJFXClassModel(tree, attrInfo.toList());
            prependInFrontOfDef(model.correspondingInterface);
            translatedDefs.appendList(model.additionalClassMembers);

            {
                // Add the userInit$ method
                List<JCVariableDecl> receiverVarDeclList = List.<JCVariableDecl>of(make.VarDef(
                        make.Modifiers(Flags.FINAL), 
                        initBuilder.receiverName, 
                        make.Ident(names.fromString(tree.getName().toString() + initBuilder.interfaceNameSuffix.toString())), null));
                JCBlock userInitBlock = make.Block(0L, translatedInitBlocks.toList());
                translatedDefs.append(make.MethodDef(
                        make.Modifiers(Flags.PUBLIC | Flags.STATIC), 
                        initBuilder.userInitName, 
                        makeTypeTree(syms.voidType, null), 
                        List.<JCTypeParameter>nil(), 
                        receiverVarDeclList, 
                        List.<JCExpression>nil(), 
                        userInitBlock, 
                        null));
            }
 
            if (tree.isModuleClass) {
                // Add main method...
                translatedDefs.append(makeMainMethod(diagPos));
            }

            // build the list of implemented interfaces
           ListBuffer<JCExpression> implementing =  ListBuffer.<JCExpression>lb();
            implementing.appendList(tree.getImplementing());
            implementing.append(make.Ident(model.interfaceName));
            implementing.append(makeIdentifier(initBuilder.fxObjectName));
            
            // make the Java class corresponding to this FX class, and return it
            JCClassDecl res = make.at(diagPos).ClassDef(
                    tree.mods,
                    tree.getName(),
                    tree.getEmptyTypeParameters(), 
                    null,  // no classes are extended, they have become interfaces -- change if we implement single Java class extension
                    implementing.toList(), 
                    translatedDefs.toList());
            res.sym = tree.sym;
            res.type = tree.type;
            result = res;

            addBaseAttributes(tree.sym, res);  //TODO: remove this
        }
        finally {
            attrEnv.enclClass = prevEnclClass;
            bindContext = prevBindContext;
            inLHS = prevInLHS;

            currentClass = prevClass;
        }
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        result = null; // Just remove this tree...
    }

    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        Name tmpName = getSyntheticName("objlit");
        JCExpression clazz;

        ListBuffer<JCStatement> stats = ListBuffer.<JCStatement>lb();
        if (tree.getClassBody() == null) {
            clazz = makeTypeTree(tree.type, tree, false);
        } else {
            JFXClassDeclaration cdef = tree.getClassBody();
            prependInFrontOfDef( translate( cdef ) );
            clazz = makeTypeTree(cdef.type, tree, false);
         }

        List<JCExpression> newClassArgs = translate( tree.getArgs());
        if (tree.getClassBody() != null &&
            tree.getClassBody().sym != null && hasOuters.contains(tree.getClassBody().sym)) {
            JCIdent thisIdent = make.Ident(initBuilder.receiverName);
            thisIdent.sym = tree.getClassBody().sym.owner;
            thisIdent.type = tree.getClassBody().sym.owner.type;
            
            newClassArgs = newClassArgs.prepend(thisIdent);
        }

        JCNewClass newClass = 
                make.NewClass(null, null, clazz,
                              newClassArgs,
                              null);
        
        if (tree != null && tree.getIdentifier() != null) {
            Symbol sym = TreeInfo.symbol(tree.getIdentifier());
        
            if (sym != null &&
                sym.kind == Kinds.TYP && (sym instanceof ClassSymbol) &&
                initBuilder.isJFXClass((ClassSymbol)sym)) {
                // it is a JavaFX class, initializa it properly
                JCVariableDecl tmpVar = make.VarDef(make.Modifiers(0), tmpName, clazz, newClass);
                stats.append(tmpVar);
                for (JFXObjectLiteralPart olpart : tree.getParts()) {
                    DiagnosticPosition diagPos = olpart.pos();
                    JavafxBindStatus bindStatus = olpart.getBindStatus();
                    JCExpression init = olpart.getExpression();
                    VarSymbol vsym = (VarSymbol) olpart.sym;
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                    if (shouldMorph(vmi)) {
                        init = translateDefinitionalAssignment(diagPos, init, bindStatus, vsym, false).first;
                    } else {
                        init = boundTranslate(init, bindStatus);
                    }

                    JCIdent ident1 = make.at(diagPos).Ident(tmpName);
                    JCStatement lastStatement = callStatement(diagPos, ident1, initBuilder.attributeInitMethodNamePrefix + olpart.getName().toString(), init);
                    stats.append(lastStatement); 

                }

                JCIdent ident3 = make.Ident(tmpName);   
                JCStatement applyExec = callStatement(tree.pos(), ident3, initBuilder.initializeName.toString());
                stats.append(applyExec);

                JCIdent ident2 = make.Ident(tmpName);
                JFXBlockExpression blockExpr1 = makeBlockExpression(tree.pos(), stats, ident2);
                result = blockExpr1; 
            }
            else {
                // this is a Java class, just instanciate it
                result = newClass;
            }
       } else {
            // this is a Java class, just instanciate it
            result = newClass;
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

    private JCExpressionTupple translateDefinitionalAssignment(DiagnosticPosition diagPos,
                    JCExpression init, JavafxBindStatus bindStatus, VarSymbol vsym, boolean isAttribute) {
        JCExpression translatedInit = boundTranslate(init, bindStatus);
        JCExpressionTupple translatedInitTupple = new JCExpressionTupple(translatedInit, null);
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        if (shouldMorph(vmi)) {
            translatedInitTupple = typeMorpher.buildDefinitionalAssignment(diagPos, vmi, 
                    init, translatedInit, bindStatus, isAttribute);
        }
        
        return translatedInitTupple;
    }

    public JCExpressionTupple translateVarInit(JFXVar tree, boolean isAttribute) {
        return translateDefinitionalAssignment(tree.pos(), tree.init, 
                                                tree.getBindStatus(), tree.sym, isAttribute);
    }

    @Override
    public void visitVar(JFXVar tree) {
        // Fix for JFXC-69
        if (tree.type == syms.javafx_StringType && tree.init == null) {
            tree.init = make.Literal("");
        }

        DiagnosticPosition diagPos = tree.pos();
        Type type = tree.type;
        JCModifiers mods = tree.getModifiers();
        long modFlags = mods==null? 0L : mods.flags;
        VarSymbol vsym = tree.sym;
        boolean isClassVar = vsym.owner.kind == Kinds.TYP;
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
        boolean forceTypeMorph = false;;

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
        JCExpression init = (isClassVar || ((vsym.flags() & Flags.PARAMETER) != 0))? null : translateVarInit(tree, false).first;
        
        result = make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);         
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        result = ((JavafxTreeMaker)make).OnReplace(
                tree.getOldValue(),
                translate(tree.getBody()));
    }
    
    @Override
    public void visitOnReplaceElement(JFXOnReplaceElement tree) {
        result = ((JavafxTreeMaker)make).OnReplaceElement(
                tree.getIndex(),
                tree.getOldValue(),
                translate(tree.getBody()));
    }
    
    @Override
    public void visitOnInsertElement(JFXOnInsertElement tree) {
        result = ((JavafxTreeMaker)make).OnInsertElement(
                tree.getIndex(),
                tree.getOldValue(),  // new
                translate(tree.getBody()));
    }
    
    @Override
    public void visitOnDeleteElement(JFXOnDeleteElement tree) {
        result = ((JavafxTreeMaker)make).OnDeleteElement(
                tree.getIndex(),
                tree.getOldValue(),
                translate(tree.getBody()));
    }

    @Override
    public void visitOnDeleteAll(JFXOnDeleteAll tree) {
        assert false : "not yet implemented -- may not be";
    }

    @Override
    public void visitOperationValue(JFXOperationValue tree) {
        JFXOperationDefinition def = tree.definition;
        result = makeFunctionValue(make.Ident(((JavafxTreeMaker)make).lambdaName), tree.definition, tree.pos(), (MethodType) def.type);
        result.type = tree.type;
    }
    
    private void prependInFrontOfDef(JCStatement toPrepend) {
        prependInFrontOfDef.append(toPrepend);
    }
    
   JCExpression makeFunctionValue (JCExpression meth, JFXOperationDefinition def, DiagnosticPosition diagPos, MethodType mtype) {
        ListBuffer<JCTree> members = new ListBuffer<JCTree>();
        if (def != null)
            members.append(translate(def));
        JCExpression encl = null;
        List<JCExpression> args = List.<JCExpression>nil();
        int nargs = mtype.argtypes.size();
        Type ftype = syms.javafx_FunctionTypes[nargs];
        JCExpression t = makeQualifiedTree(null, ftype.tsym.getQualifiedName().toString());
        ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
        typeargs.append(makeTypeTree(syms.objectType, diagPos));
        ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        ListBuffer<JCExpression> margs = new ListBuffer<JCExpression>();
        int i = 0;
        for (List<Type> l = mtype.argtypes;  l.nonEmpty();  l = l.tail) {
            Name pname = make.paramName(i++);
            JCVariableDecl param = make.VarDef(make.Modifiers(0), pname,
                    makeTypeTree(syms.objectType, diagPos), null);
            params.append(param);
            JCExpression marg = make.Ident(pname);
            margs.append(castFromObject(marg, l.head));
            typeargs.append(makeTypeTree(syms.objectType, diagPos));
        }
        // The backend's Attr skips SYNTHETIC methods when looking for a matching method.
        long flags = PUBLIC | BRIDGE; // | SYNTHETIC;

        JCExpression call = make.Apply(null, meth, margs.toList());

        List<JCStatement> stats;
        if (mtype.restype == syms.voidType)
            stats = List.<JCStatement>of(make.Exec(call), make.Return(make.Literal(TypeTags.BOT, null)));
        else {
            if (mtype.restype.isPrimitive())
                call = makeBox(diagPos, call, mtype.restype);
            stats = List.<JCStatement>of(make.Return(call));
        }
       JCMethodDecl bridgeDef = make.at(diagPos).MethodDef(
                make.Modifiers(flags),
                ((JavafxTreeMaker)make).invokeName, 
                makeTypeTree(syms.objectType, diagPos), 
                List.<JCTypeParameter>nil(), 
                params.toList(),
                make.at(diagPos).Types(mtype.getThrownTypes()),
                make.Block(0, stats), 
                null);

        members.append(bridgeDef);
        JCClassDecl cl = make.AnonymousClassDef(make.Modifiers(0), members.toList());
        return make.NewClass(encl, args, make.TypeApply(t, typeargs.toList()), args, cl);
    }
    
   /**
    * Translate JavaFX a class definition into a Java static implementation method.
    * */
   @Override
    public void visitOperationDefinition(JFXOperationDefinition tree) {
        // Made all the operations public. Per Brian's spec.
        // If they are left package level it interfere with Multiple Inheritance
        // The interface methods cannot be package level and an error is reported.
        long flags = tree.mods.flags;
        long originalFlags = flags;
        flags &= ~(Flags.PROTECTED | Flags.PRIVATE);
        flags |=  Flags.PUBLIC;
        if ((flags & (Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) {
            flags |= Flags.STATIC;
        }
        flags &= ~Flags.SYNTHETIC;
        JCModifiers mods = make.Modifiers(flags);     
        
        DiagnosticPosition diagPos = tree.pos();
        boolean isBound = bindContext.isBound();
        MethodType mtype = (MethodType)tree.type;
        
        JFXBlockExpression bexpr = tree.getBodyExpression();
        JCBlock body = null; // stays null if no block expression
        if (bexpr != null) {
            body = blockExpressionToBlock(bexpr, mtype.getReturnType() != syms.voidType);
        }

        // if this is the synthetic run method, make sure it ends with a return
        boolean isRunMethod = isRunMethod(tree.sym);
        if (isRunMethod) {
            JCStatement lastStat = body.stats.last();
            if (lastStat == null || lastStat.getTag() != JCTree.RETURN) {
                body.stats = body.stats.append(make.Return(make.Literal(TypeTags.BOT, null)));
            }
        }
             
        ListBuffer<JCVariableDecl> params = ListBuffer.<JCVariableDecl>lb();
        if ((originalFlags & (Flags.STATIC | Flags.ABSTRACT | Flags.SYNTHETIC)) == 0) {
            // if we are converting a standard instance function (to a static method), the first parameter becomes a reference to the receiver
            params.prepend(makeReceiverParam(currentClass));
        }
        for (JFXVar fxVar : tree.getParameters()) {
            params.append(translate(fxVar));
        }
        result = make.at(diagPos).MethodDef(
                mods,
                functionName(tree.sym, isBound), 
                makeTypeTree(mtype.getReturnType(), diagPos), 
                make.at(diagPos).TypeParams(mtype.getTypeArguments()), 
                params.toList(),
                make.at(diagPos).Types(mtype.getThrownTypes()),  // makeThrows(diagPos), //
                body, 
                null);
        ((JCMethodDecl)result).sym = tree.sym;
        result.type = tree.type;
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        assert (tree.type != syms.voidType) : "void block expressions should all have been folded away";
        List<JCStatement> defs = translate(tree.stats);
	result = ((JavafxTreeMaker)make).at(tree.pos).BlockExpression(
                tree.flags, 
                defs, 
                translate(tree.value));
    }
 
    @Override
    public void visitBlock(JCBlock tree) {
        List<JCStatement> defs = translate(tree.stats);
	result = make.at(tree.pos).Block(tree.flags, defs);
    }

    @Override
    public void visitAssign(JCAssign tree) {
        DiagnosticPosition diagPos = tree.pos();
        
        Symbol sym = null;
        // TODO other cases
        if (tree.lhs instanceof JCIdent) {
            JCIdent varid = (JCIdent)tree.lhs;
            sym = varid.sym;
        } else if (tree.lhs instanceof JCFieldAccess) {
            JCFieldAccess varaccess = (JCFieldAccess)tree.lhs;
            sym = varaccess.sym;
        }
        VarSymbol vsym = null;
        if (sym != null && (sym instanceof VarSymbol)) {
            vsym = (VarSymbol)sym;
        }
        
        JCExpression rhs = translate(tree.rhs);
        if (tree.lhs.getTag() == JavafxTag.SEQUENCE_INDEXED) {
            // assignment of a sequence element --  s[i]=8
            JFXSequenceIndexed si = (JFXSequenceIndexed)tree.lhs;
            JCExpression seq = translateLHS(si.getSequence(), true);  // LHS?
            JCExpression index = translate(si.getIndex());
            result = typeMorpher.morphSequenceIndexedAssign(diagPos, seq, index, rhs);
        } else {
            JCExpression lhs = translateLHS(tree.lhs, true);
            result = typeMorpher.morphAssign(diagPos, vsym, lhs, rhs);
        }
    }

    @Override
    public void visitSelect(JCFieldAccess tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression selected = tree.getExpression();
        Type selectedType = selected.type;
        
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        JCExpression translatedSelected = translateLHS(selected, false);
     
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JCVariableDecl selectedTmp = null;
            
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

       if (selectedType != null && selectedType.isPrimitive()) { // selecetd.type is null for package symbols.
            translatedSelected = makeBox(diagPos, translatedSelected, selectedType);
        }
        JCFieldAccess translated = make.at(diagPos).Select(translatedSelected, tree.getIdentifier());
        // since this tree will be morphed, we need to copy the sym info
        translated.sym = tree.sym;
        translated.type = tree.type;
        
        // determine if this is a static reference, eg.   MyClass.myAttribute
        boolean staticReference = false;
        if (tree.sym != null && tree.sym.isStatic()) {
            if (selected.getTag() == JCTree.SELECT) {
                staticReference = ((JCFieldAccess) selected).sym instanceof ClassSymbol;
            } else if (selected.getTag() == JCTree.IDENT) {
                staticReference = ((JCIdent) selected).sym instanceof ClassSymbol;
            }
        }
        
        result = typeMorpher.convertVariableReference(
                translated,
                tree.sym, 
                staticReference , 
                bindContext, 
                inLHS);
    }
    
    @Override
    public void visitIdent(JCIdent tree)   {
       DiagnosticPosition diagPos = tree.pos();
        if (tree.type instanceof FunctionType && tree.sym.type instanceof MethodType) {
            MethodType mtype = (MethodType) tree.sym.type;
            JCExpression meth = tree;
            JFXOperationDefinition def = null; // FIXME
            result = makeFunctionValue(make.Ident(functionName((MethodSymbol)tree.sym)), def, tree.pos(), mtype);
            return;
        }
        if (tree.name == names._this) {
            // in the static implementation method, "this" becomes "receiver$"
            result = make.Ident(initBuilder.receiverName);
            return;
        } else if (tree.name == names._super) {
            // "super" become just the class where the static implementation method is defined
            //  the rest of the implementation is in visitApply
            result = make.Ident(tree.type.tsym.name);
            return;
        }
       
       // if this is an instance reference to an attribute or function, it needs to go the the "receiver$" arg,
       // and possible outer access methods
        JCExpression convert = tree;
        int kind = tree.sym.kind;
        if ((kind == Kinds.VAR || kind == Kinds.MTH) && tree.sym.owner.kind == Kinds.TYP && !tree.sym.isStatic()) {
            convert = make.Select(makeReceiver(diagPos, tree.sym, attrEnv.enclClass.sym), tree.name);
        }
        
        result = typeMorpher.convertVariableReference(convert, tree.sym, tree.sym.isStatic(), bindContext, inLHS);
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
         * */
        ListBuffer<JCStatement> stmts = ListBuffer.<JCStatement>lb();
        Type elemType = tree.type.getTypeArguments().head;
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
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        List<JCExpression> typeArgs = List.<JCExpression>nil();
        args.append( translate( tree.getLower() ));
        args.append( translate( tree.getUpper() ));
        if (tree.getStepOrNull() != null) {
            args.append( translate( tree.getStepOrNull() ));
        }
        result = make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) {
        Type elemType = tree.type.getTypeArguments().get(0);
        result = makeEmptySeuenceCreator(tree.pos(), elemType);
    }
        
    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression seq = translateLHS(tree.getSequence(), true);  // LHS?
        JCExpression index = translate(tree.getIndex());
        result = typeMorpher.morphSequenceIndexedAccess(diagPos, seq, index);
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        result = callStatement(tree, 
                translateLHS( tree.getSequence() ), 
                "insert", 
                translate( tree.getElement() ));
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        if (tree.getIndex() != null) { 
            result = callStatement(tree.pos(), 
                translateLHS( tree.getSequence() ), 
                "delete", 
                translate( tree.getIndex() ));
        } else if (tree.getElement() != null) { 
            result = callStatement(tree.pos(), 
                translateLHS( tree.getSequence() ), 
                "deleteValue", 
                translate( tree.getElement() ));
        } else { 
            result = callStatement(tree.pos(), 
                translateLHS( tree.getSequence() ), 
                "deleteAll");
        }
    }

    /**** utility methods ******/
    
    /**
     * For an attribute "attr" make an access to it via the receiver and getter  
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, JFXVar attrib) {
       return callExpression(diagPos, 
                make.Ident(initBuilder.receiverName),
                initBuilder.attributeGetMethodNamePrefix + attrib.getName().toString(), 
                List.<JCExpression>nil());
   }
   
   /**
    * Make a receiver parameter. 
    * Its type is that of the corresponding interface and it is a final parameter.
    * */
    JCVariableDecl makeReceiverParam(JFXClassDeclaration cDecl) {
        return make.VarDef(
                make.Modifiers(Flags.FINAL | Flags.PARAMETER), 
                initBuilder.receiverName, 
                make.Ident(initBuilder.interfaceName(cDecl)), 
                null);
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method) {
        return callStatement(diagPos, receiver, method, List.<JCExpression>nil());
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            JCExpression arg) {
        return callStatement(diagPos, receiver, method, List.<JCExpression>of(arg));
    }
    
    JCStatement callStatement(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            List<JCExpression> args) {
        return make.at(diagPos).Exec(callExpression(diagPos, receiver, method, args));
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            List<JCExpression> args) {
        return callExpression(diagPos, receiver, names.fromString(method), args);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
             Name methodName, 
            List<JCExpression> args) {
        JCExpression expr = null;
        if (receiver == null) {
            expr = make.at(diagPos).Ident(methodName);
        } else {
            expr = make.at(diagPos).Select(receiver, methodName);
        }
        return make.at(diagPos).Apply(List.<JCExpression>nil(), expr, args);
    }

    private Name getSyntheticName(String kind) {
        return Name.fromString(names, syntheticNamePrefix + syntheticNameCounter++ + kind);
    }

    JCMethodDecl makeMainMethod(DiagnosticPosition diagPos) {
            List<JCExpression> emptyExpressionList = List.nil();
            JCIdent runIdent = make.at(diagPos).Ident(Name.fromString(names, JavafxModuleBuilder.runMethodString));
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
            JCExpression meth =makeIdentifier(valueOfSym.owner.type.toString() + "." + valueOfSym.name.toString());
            TreeInfo.setSymbol(meth, valueOfSym);
            meth.type = valueOfSym.type;
            return make.App(meth, List.of(translatedExpr));
        }
    }
    
    public List<JCExpression> makeThrows(DiagnosticPosition diagPos) {
        return List.<JCExpression>of(makeQualifiedTree(diagPos, methodThrowsString));
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
                    initBuilder.isJFXClass((ClassSymbol)elemType.tsym)) {
                String str = elemType.tsym.flatName().toString().replace("$", ".");
                String strLookFor = str + initBuilder.interfaceNameSuffix.toString();
                elemType = typeMorpher.reader.enterClass(names.fromString(strLookFor)).type;

            }

            JCExpression builderTypeExpr = makeQualifiedTree(diagPos, sequenceBuilderString);
            List<JCExpression> btargs = List.<JCExpression>of(makeTypeTree(elemType, diagPos));
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

    JCExpression makeEmptySeuenceCreator(DiagnosticPosition diagPos, Type elemType) {
        JCExpression meth = makeQualifiedTree(diagPos, sequencesEmptyString);
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        args.append(make.at(diagPos).Select(makeTypeTree(elemType, diagPos, true), names._class));
        List<JCExpression> typeArgs = List.<JCExpression>of(makeTypeTree(elemType, diagPos, true));
        return make.at(diagPos).Apply(typeArgs, meth, args.toList());
    }
        
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos) {
        return makeTypeTree(t, diagPos, true);
    }
    
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos, boolean makeIntf) {
        if (t.tag == TypeTags.CLASS) {
            JCExpression texp = null;

            if (makeIntf && t.tsym instanceof ClassSymbol && initBuilder.isJFXClass((ClassSymbol)t.tsym) &&
                    !t.tsym.getQualifiedName().toString().endsWith(initBuilder.interfaceNameSuffix.toString())) {
                 texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString() + initBuilder.interfaceNameSuffix.toString());
            }
            else {
                texp = makeQualifiedTree(diagPos, t.tsym.getQualifiedName().toString());
            }

            // Type outer = t.getEnclosingType();
            if (!t.getTypeArguments().isEmpty()) {
                List<JCExpression> targs = List.<JCExpression>nil();
                for (Type ta : t.getTypeArguments()) {
                    // Kludge - we convert FunctionType generic parameters to Object,
                    // since otherwise the backend complains about our bridge methods.
                    targs = targs.append(makeTypeTree(t instanceof FunctionType ? syms.objectType : ta,
                            diagPos, makeIntf ? true: false));
                }
                texp = make.at(diagPos).TypeApply(texp, targs);
            }
            return texp;
        } else {
            return make.at(diagPos).Type(t);
        }
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

    public void visitAssignop(JCAssignOp tree) {
        JCTree lhs = translate(tree.lhs);
        JCTree rhs = translate(tree.rhs);
        result = make.at(tree.pos).Assignop(tree.getTag(), lhs, rhs);
    }

    public void visitBinary(JCBinary tree) {
        JCExpression lhs = translate(tree.lhs);
        JCExpression rhs = translate(tree.rhs);
        result = make.at(tree.pos).Binary(tree.getTag(), lhs, rhs);
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
        if (tree.type != syms.voidType) {
            // body has value (non-void)
            DiagnosticPosition diagPos = tree.pos();
            ListBuffer<JCStatement> stmts = ListBuffer.<JCStatement>lb();
            JCStatement stmt;
            JCExpression value;

            // Compute the element type from the sequence type
            assert tree.type.getTypeArguments().size() == 1;
            Type elemType = tree.type.getTypeArguments().head;

            UseSequenceBuilder builder = new UseSequenceBuilder(diagPos, elemType);
            stmts.append(builder.makeTmpVar());

            // Build innermost loop body
            stmt = builder.makeAdd( tree.getBodyExpression() );

            // Build the result value
            value = builder.makeToSequence();
            stmt = wrapWithInClause(tree, stmt);
            stmts.append(stmt);

            // Build the block expression -- which is what we translate to
            result = makeBlockExpression(diagPos, stmts, value);
        } else {
            assert false : "should always be folded away";
            result = exprToTranslatedStatement(tree, false);
        }
    }

    //where
    private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
        JCStatement stmt = coreStmt;
        for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)tree.getInClauses().get(inx);
            if (clause.getWhereExpression() != null) {
                stmt = make.at(clause).If(clause.getWhereExpression(), stmt, null);
            }

            // Build the loop
            stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    boundTranslate(clause.getVar(), JavafxBindStatus.UNBOUND), 
                    translate(clause.getSequenceExpression()),
                    stmt);
        }
        return stmt;
    }
    
    public void visitConditional(JCConditional tree) {
        final DiagnosticPosition diagPos = tree.pos();
        JCExpression cond = translate( tree.getCondition() );
        JCExpression trueSide = translate( tree.getTrueExpression() );
        JCExpression falseSide;
        if (tree.getFalseExpression() == null) {
            Type trueSideType = tree.getTrueExpression().type;
            switch (trueSideType.tag) {
            case TypeTags.BOOLEAN:
                falseSide = make.at(diagPos).Literal(TypeTags.BOOLEAN, 0);
                break;
            case TypeTags.INT:
                falseSide = make.at(diagPos).Literal(TypeTags.INT, 0);
                break;
            case TypeTags.DOUBLE:
                falseSide = make.at(diagPos).Literal(TypeTags.DOUBLE, 0.0);
                break;
            case TypeTags.BOT:
                falseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                break;
            case TypeTags.VOID:
                assert false : "should have been translated";
                falseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                break;
            case TypeTags.CLASS:
                if (trueSideType == syms.stringType) {
                    falseSide = make.at(diagPos).Literal(TypeTags.CLASS, "");
                } else {
                    falseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                }
                break;
            default:
                assert false : "what is this type doing here? " + trueSideType;
                falseSide = make.at(diagPos).Literal(TypeTags.BOT, null);
                break;
            }
        } else {
            falseSide = translate( tree.getFalseExpression() );
        }
        result = make.at(diagPos).Conditional(cond, trueSide, falseSide);
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

    /** Convert an expression to a statement.
     * In general, just creates an JCExpressionStatement.
     * However, JCConditional is converted to a JCIf.
     */
    private JCStatement exprToTranslatedStatement(JCExpression expr, boolean asReturn) {
        DiagnosticPosition diagPos = expr.pos();
        // Gen's visitConditional doesn't like void expressions.
        // This is easy to fix (use isVoidItem on the item before load),
        // but there are other restrictions in javac.  We'll deal with
        // those later, but for now here is a simple rewrite that
        // handles most of the useful cases.
        if (expr instanceof JCConditional) {
            JCConditional cond = (JCConditional) expr;
            return make.at(diagPos).If( translate( cond.getCondition() ), 
                    exprToTranslatedStatement(cond.truepart, asReturn), 
                    cond.falsepart == null ? 
                        null : 
                        exprToTranslatedStatement(cond.falsepart, asReturn));
        } else if (expr instanceof JFXBlockExpression) {
            return blockExpressionToBlock((JFXBlockExpression) expr, asReturn);
        } else if (expr instanceof JFXForExpression) {
            JFXForExpression forexpr = (JFXForExpression) expr;
            return wrapWithInClause(forexpr, 
                    exprToTranslatedStatement(
                        forexpr.getBodyExpression(), asReturn));
        } else if (expr instanceof JCParens) {
            return exprToTranslatedStatement(((JCParens) expr).getExpression(), asReturn);
       } else {
            JCExpression texpr = translate(expr);
            if (asReturn) {
                return make.at(diagPos).Return(texpr);
            } else {
                return make.at(diagPos).Exec(texpr);
            }
        }
    }
    
   JCBlock blockExpressionToBlock(JFXBlockExpression bexpr, boolean asReturn) {
        DiagnosticPosition diagPos = bexpr.pos();
        List<JCStatement> stats = translate(bexpr.stats);
        if (bexpr.value != null) {
            stats = stats.append( exprToTranslatedStatement(bexpr.value, asReturn) );
        }
        return make.at(diagPos).Block(0, stats);
    }

    public void visitReturn(JCReturn tree) {
        JCExpression exp = tree.getExpression();
        if (exp == null)
            result = null;
        else
            result = exprToTranslatedStatement(exp, true);
    }

    public void visitExec(JCExpressionStatement tree) {
        result = exprToTranslatedStatement(tree.getExpression(), false);
    }

    public void visitParens(JCParens tree) {
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).Parens(expr);
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitForLoop(JCForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitIf(JCIf tree) {
        assert false : "should not be in JavaFX AST";
/***
        JCExpression cond = translate(tree.cond);
        JCStatement thenpart = translate(tree.thenpart);
        JCStatement elsepart = translate(tree.elsepart);
        result = make.at(tree.pos).If(cond, thenpart, elsepart);
****/
    }

    public void visitImport(JCImport tree) {
        JCTree qualid = translate(tree.qualid);
        result = make.at(tree.pos).Import(qualid, tree.staticImport);
    }

    public void visitIndexed(JCArrayAccess tree) {
        assert false : "should not be in JavaFX AST";
    }

    public void visitTypeTest(JCInstanceOf tree) {
        JCExpression expr = translate(tree.expr);
        JCTree clazz = translate(tree.clazz);
        if (clazz != null && clazz.type.tsym != null &&
                clazz.type.tsym instanceof ClassSymbol && initBuilder.isJFXClass((ClassSymbol)clazz.type.tsym)) {
            clazz = makeIdentifier(clazz.type.tsym.name.toString() + initBuilder.interfaceNameSuffix.toString());
        }
        result = make.at(tree.pos).TypeTest(expr, clazz);
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

    public void visitApply(JCMethodInvocation tree) {
        List<JCExpression> typeargs = translate(tree.typeargs);
        JCExpression meth = tree.meth;
        Type mtype = meth.type;
        meth = meth==null? make.Ident(initBuilder.receiverName) :  translate(meth);
        boolean useInvoke = mtype instanceof FunctionType;
        if (useInvoke) {
            Scope.Entry e = mtype.tsym.members().lookup(((JavafxTreeMaker)make).invokeName);
            meth = make.Select(meth, e.sym);
        }
        
        List<JCExpression> args = translate(tree.args);
        
        // if this is a super.foo(x) call, "super" will be translated to referenced class, 
        // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
        if (tree.meth instanceof JCFieldAccess) {
             JCExpression selected =  ((JCFieldAccess)(tree.meth)).getExpression();
             if (selected instanceof JCIdent) {
                 JCIdent id = (JCIdent)selected;
                 if (id.getName() == names._super) {
                     args = args.prepend(make.Ident(initBuilder.receiverName));
                 }
             }
        }
        JCExpression fresult = make.at(tree.pos).Apply(typeargs, meth, args);
       if (useInvoke && tree.type.tag != TypeTags.VOID)
            fresult = castFromObject(fresult, tree.type);
        result = fresult;
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

    public void visitTypeCast(JCTypeCast tree) {
        JCTree clazz = translate(tree.clazz);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeCast(clazz, expr);
    }

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        result = make.at(tree.pos).TypeIdent(tree.typetag);
    }

    public void visitTypeParameter(JCTypeParameter tree) {
        List<JCExpression> bounds = translate(tree.bounds);
        result = make.at(tree.pos).TypeParameter(tree.name, tree.bounds);
    }

    public void visitUnary(JCUnary tree) {
        if (tree.getTag() == JavafxTag.SIZEOF) {
            if (isSequence(tree.arg.type)) {
                result = callExpression(tree, translate(tree.arg), "size", List.<JCExpression>nil());
            } else {
                // not a sequence, virtually promote to a sequence of length one
                result = make.at(tree).Literal(TypeTags.INT, 1);
            }
        } else {
            result = make.at(tree.pos).Unary(tree.getTag(), translate(tree.arg));
        }
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
    
    boolean shouldMorph(VarMorphInfo vmi) {
        if ( vmi.mustMorph() ) {
            return true;
        } else if (bindContext.isBound()) {
            Symbol owner = vmi.varSymbol.owner;
            if (owner.kind == Kinds.TYP) {
                ClassSymbol owningClass = (ClassSymbol)owner;
                return initBuilder.isJFXClass(owningClass);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    JCExpression makeIdentifier(String s) {
        return ((JavafxTreeMaker)make).Identifier(s);
    }
    
    JCExpression makeIdentifier(Name name) {
        return ((JavafxTreeMaker)make).Identifier(name);
    }
    
    boolean isRunMethod(MethodSymbol sym) {
        return sym.name.toString().equals(moduleBuilder.runMethodString);
    }
    
    boolean isSequence(Type type) {
        return type != Type.noType && type != null 
                && type.tag != TypeTags.ERROR 
                && type.tag != TypeTags.METHOD && type.tag != TypeTags.FORALL
                && types.erasure(type) == syms.javafx_SequenceTypeErasure;
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
            List<JCExpression> emptyArgs = List.<JCExpression>nil();
            
            ret = make.Ident(initBuilder.receiverName);
            ret.type = siteOwner.type;
            
            // check if it is in the chain
            Symbol siteCursor = siteOwner;
            while (treeSym.owner != siteCursor && siteCursor.kind != Kinds.PCK) {
                siteCursor = siteCursor.owner;
            }
            
            if (treeSym.owner == siteCursor) {
                // site was found up the outer class chain, add the chaining accessors
                siteCursor = siteOwner;
                while (treeSym.owner != siteCursor) {
                    if (siteCursor.kind == Kinds.TYP) {
                        ret = callExpression(pos, ret, initBuilder.outerAccessorName, emptyArgs);
                        ret.type = siteCursor.type;
                    }
                    siteCursor = siteCursor.owner;
                }
            }
        }
        return ret;
    }

    Name functionName(MethodSymbol sym) {
        return functionName(sym, false);
    }

    Name functionName(MethodSymbol sym, boolean bound) {
        String full = sym.name.toString();
        if (sym.isStatic() && (sym.owner.kind != Kinds.TYP || initBuilder.isJFXClass((ClassSymbol) sym.owner)) && !isRunMethod(sym)) {
            full = full  + staticFunctionSuffix;
        }
        if (bound) {
            full = full  + boundFunctionSuffix;
        }    
        return names.fromString(full);
    }

// Is the referenced symbol an outer member.
    static boolean isOuterMember(Symbol sym, Symbol ownerSym) {
        if (sym != null && ownerSym != null) {
            Symbol symOwner = sym.owner;
            Symbol ownerSymOwner = ownerSym.owner;
            while (ownerSymOwner != null && ownerSymOwner.kind != Kinds.PCK) {
                if (ownerSymOwner == symOwner) {
                    return true;
                }

                ownerSymOwner = ownerSymOwner.owner;
            }
        }

        return false;
    }

    //TODO: destructive -- remove
    private void addBaseAttributes(ClassSymbol sym, JCClassDecl result) {
        java.util.List<Symbol> attrSyms = initBuilder.fxClassAttributes.get(sym);
        if (attrSyms != null) {
            for (Symbol attrSym : attrSyms) {
                if (attrSym.kind == Kinds.MTH) {
                    VarSymbol varSym = new VarSymbol(0L, attrSym.name, ((MethodType)attrSym.type).restype, attrSym.owner);
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(varSym);
                    result.defs = result.defs.append(make.VarDef(make.Modifiers(0L), 
                            names.fromString(attrSym.name.toString().substring(initBuilder.attributeGetMethodNamePrefix.length())),
                           makeIdentifier(vmi.getMorphedType().toString()), null));
                }
                else if (attrSym.kind == Kinds.VAR && attrSym.owner != sym) {
                    JCVariableDecl var = make.VarDef((VarSymbol)attrSym, null);
                    // Made all the operations public. Per Brian's spec.
                    // If they are left package level it interfere with Multiple Inheritance
                    // The interface methods cannot be package level and an error is reported.
                    {
                        var.mods.flags &= ~Flags.PROTECTED;
                        var.mods.flags &= ~Flags.PUBLIC;
                        var.mods.flags |= Flags.PRIVATE;
                    }
                    VarMorphInfo vmi = typeMorpher.varMorphInfo((VarSymbol)attrSym);
                    var.vartype = makeIdentifier(vmi.getMorphedType().toString());
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
                if (currentClass != null && tree.sym.kind != Kinds.TYP && isOuterMember(tree.sym, currentClass.sym)) {
                    addOutersForOuterAccess(tree.sym, currentClass.sym);
                }
            }
            
            @Override
            public void visitSelect(JCFieldAccess tree) {
                super.visitSelect(tree);
                if (currentClass != null && tree.sym.kind != Kinds.TYP && isOuterMember(tree.sym, currentClass.sym)) {
                    addOutersForOuterAccess(tree.sym, currentClass.sym);
                }
            }

            private void addOutersForOuterAccess(Symbol sym, Symbol ownerSymbol) {
                if (sym != null && ownerSymbol != null) {
                    Symbol symOwner = sym.owner;
                    Symbol ownerSymOwner = ownerSymbol;
                    ListBuffer<ClassSymbol> potentialOuters = new ListBuffer<ClassSymbol>();
                    while (symOwner != null &&  
                            ownerSymOwner != symOwner) {
                        if (ownerSymOwner.kind == Kinds.TYP) {
                            potentialOuters.append((ClassSymbol)ownerSymOwner);
                        }

                        ownerSymOwner = ownerSymOwner.owner;
                    }
                    
                    if (ownerSymOwner == symOwner) {
                        for (ClassSymbol cs : potentialOuters) {
                            hasOuters.add(cs);
                        }
                    }
                }
            }
        }
        
        new FillClassesWithOuters().scan(tree);
    }
}
