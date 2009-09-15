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

import com.sun.tools.mjavac.code.*;
import com.sun.tools.mjavac.code.Scope.Entry;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.tree.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Build the representation(s) of a JavaFX class.  Includes class initialization, attribute and function proxies.
 * With support for mixins.
 *
 * @author Robert Field
 * @author Lubo Litchev
 * @author Per Bothner
 * @author Zhiqun Chen
 * @author Jim Laskey
 */
public class JavafxInitializationBuilder extends JavafxTranslationSupport {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private final JavafxToJava toJava;
    private final JavafxClassReader reader;
    private final JavafxOptimizationStatistics optStat;

    private static final int VFLAG_IS_INITIALIZED = 0;
    private static final int VFLAG_DEFAULTS_APPLIED = 1;
    private static final int VFLAG_BITS_PER_VAR = 2;

    private Name outerAccessorFieldName;
    private Name makeInitMap;

    private Name varNumName;
    private Name varLocalNumName;
    private Name varWordName;
    private Name varChangedName;
    private Name varOldValueName;
    private Name varNewValueName;

    final Type assignBindExceptionType;
    final Type assignDefExceptionType;

    void TODO() {
        throw new RuntimeException("Not yet implemented");
    }

    public static class LiteralInitVarMap {
        private int count = 1;
        public Map<VarSymbol, Integer> varMap = new HashMap<VarSymbol, Integer>();
        public ListBuffer<VarSymbol> varList = ListBuffer.lb();

        public int addVar(VarSymbol sym) {
            Integer value = varMap.get(sym);

            if (value == null) {
                value = new Integer(count++);
                varMap.put(sym, value);
                varList.append(sym);
            }

            return value.intValue();
        }

        public int size() {
            return varMap.size();
        }
    }

    public static class LiteralInitClassMap {
        public Map<ClassSymbol, LiteralInitVarMap> classMap = new HashMap<ClassSymbol, LiteralInitVarMap>();

        public LiteralInitVarMap getVarMap(ClassSymbol sym) {
            LiteralInitVarMap map = classMap.get(sym);

            if (map == null) {
                map = new LiteralInitVarMap();
                classMap.put(sym, map);
            }

            return map;
        }

        public int size() {
            return classMap.size();
        }
    }

    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        super(context);

        context.put(javafxInitializationBuilderKey, this);

        toJava = JavafxToJava.instance(context);
        reader = (JavafxClassReader) JavafxClassReader.instance(context);
        optStat = JavafxOptimizationStatistics.instance(context);

        outerAccessorFieldName = names.fromString("accessOuterField$");
        makeInitMap = names.fromString("makeInitMap$");

        varNumName = names.fromString("varNum$");
        varLocalNumName = names.fromString("varLocalNum$");
        varWordName = names.fromString("varWord$");
        varChangedName = names.fromString("varChanged$");
        varOldValueName =  names.fromString("varOldValue$");
        varNewValueName =  names.fromString("varNewValue$");

        {
            Name name = names.fromString(runtimePackageNameString + ".AssignToBoundException");
            ClassSymbol sym = reader.enterClass(name);
            assignBindExceptionType = types.erasure( sym.type );
        }
        
        {
            Name name = names.fromString(runtimePackageNameString + ".AssignToDefException");
            ClassSymbol sym = reader.enterClass(name);
            assignDefExceptionType = types.erasure( sym.type );
        }
    }

    /**
     * Hold the result of analyzing the class.
     * */
    static class JavafxClassModel {
        final Name interfaceName;
        final List<JCExpression> interfaces;
        final List<JCTree> iDefinitions;
        final List<JCTree> additionalClassMembers;
        final List<JCExpression> additionalImports;
        final Type superType;
        final ClassSymbol superClassSym;
        final List<ClassSymbol> superClasses;
        final List<ClassSymbol> immediateMixins;
        final List<ClassSymbol> allMixins;

        JavafxClassModel(
                Name interfaceName,
                List<JCExpression> interfaces,
                List<JCTree> iDefinitions,
                List<JCTree> addedClassMembers,
                List<JCExpression> additionalImports,
                Type superType,
                ClassSymbol superClassSym,
                List<ClassSymbol> superClasses,
                List<ClassSymbol> immediateMixins,
                List<ClassSymbol> allMixins) {
            this.interfaceName = interfaceName;
            this.interfaces = interfaces;
            this.iDefinitions = iDefinitions;
            this.additionalClassMembers = addedClassMembers;
            this.additionalImports = additionalImports;
            this.superType = superType;
            this.superClassSym = superClassSym;
            this.superClasses = superClasses;
            this.immediateMixins = immediateMixins;
            this.allMixins = allMixins;
        }
    }

    /**
     * Analyze the class.
     *
     * Determine what methods will be needed to access attributes.
     * Determine what methods will be needed to proxy to the static implementations of functions.
     * Determine what other misc fields and methods will be needed.
     * Create the corresponding interface.
     *
     * Return all this as a JavafxClassModel for use in translation.
     * */
   JavafxClassModel createJFXClassModel(JFXClassDeclaration cDecl,
           List<TranslatedVarInfo> translatedAttrInfo,
           List<TranslatedOverrideClassVarInfo> translatedOverrideAttrInfo,
           LiteralInitClassMap initClassMap) {

        DiagnosticPosition diagPos = cDecl.pos();
        Type superType = types.superType(cDecl);
        ClassSymbol outerTypeSym = outerTypeSymbol(cDecl); // null unless inner class with outer reference

        JavafxAnalyzeClass analysis = new JavafxAnalyzeClass(diagPos,
                cDecl.sym, translatedAttrInfo, translatedOverrideAttrInfo,
                names, types, reader, typeMorpher);
        JavaCodeMaker javaCodeMaker = new JavaCodeMaker(analysis);
        List<VarInfo> instanceAttributeInfos = analysis.instanceAttributeInfos();
        List<VarInfo> staticAttributeInfos = analysis.staticAttributeInfos();
        List<MethodSymbol> needDispatch = analysis.needDispatch();
        ClassSymbol fxSuperClassSym = analysis.getFXSuperClassSym();
        List<ClassSymbol> superClasses = analysis.getSuperClasses();
        List<ClassSymbol> immediateMixinClasses = analysis.getImmediateMixins();
        List<ClassSymbol> allMixinClasses = analysis.getAllMixins();

        boolean isMixinClass = cDecl.isMixinClass();
        boolean isScriptClass = cDecl.isScriptClass();
        boolean isAnonClass = analysis.isAnonClass();
        boolean hasFxSuper = fxSuperClassSym != null;

        // Have to populate the var map for anon classes.
        // TODO: figure away to avoid this if not used (needs global knowledge.)
        LiteralInitVarMap varMap = isAnonClass ? initClassMap.getVarMap(analysis.getCurrentClassSymbol()) : null;

        ListBuffer<JCTree> cDefinitions = ListBuffer.lb();  // additional class members needed
        ListBuffer<JCTree> iDefinitions = ListBuffer.lb();

        if (!isMixinClass) {
            cDefinitions.appendList(javaCodeMaker.makeAttributeNumbers(varMap));
            cDefinitions.appendList(javaCodeMaker.makeAttributeFields(instanceAttributeInfos));
            cDefinitions.appendList(javaCodeMaker.makeAttributeFields(staticAttributeInfos));
            cDefinitions.appendList(javaCodeMaker.makeAttributeAccessorMethods(instanceAttributeInfos));
            cDefinitions.appendList(javaCodeMaker.makeAttributeAccessorMethods(staticAttributeInfos));
            cDefinitions.appendList(javaCodeMaker.makeApplyDefaultsMethod());

            if (isScriptClass) {
                cDefinitions.appendList(javaCodeMaker.makeInitClassMaps(initClassMap));
                cDefinitions.appendList(javaCodeMaker.makeScriptLevelAccess(cDecl));
            }

            JCStatement initMap = isAnonClass ? javaCodeMaker.makeInitVarMapInit(analysis.getCurrentClassSymbol(), varMap) : null;

            cDefinitions.append    (makeInitStaticAttributesBlock(cDecl, translatedAttrInfo, initMap));

            if (outerTypeSym == null) {
                cDefinitions.append(makeJavaEntryConstructor(diagPos));
            } else {
                cDefinitions.append(makeOuterAccessorField(diagPos, cDecl, outerTypeSym));
                cDefinitions.append(makeOuterAccessorMethod(diagPos, cDecl, outerTypeSym));
            }

            cDefinitions.appendList(makeFunctionProxyMethods(cDecl, needDispatch));
            cDefinitions.append(makeFXEntryConstructor(diagPos, outerTypeSym, hasFxSuper));
            
            if (!hasFxSuper) {
                // Has a non-JavaFX super, so we can't use FXBase, therefore we need 
                // to clone the necessary vars and methods.
                
                // A set of methods to exclude from cloning.
                HashSet<String> excludes = new HashSet<String>();
                
                // The methods are created in the "To Java" code.
                excludes.add("userInit$()");
                excludes.add("postInit$()");
                
                // Exclude any methods generated by the init builder.
                for (JCTree member : cDefinitions) {
                    if (member.getTag() == JCTree.METHODDEF) {
                        JCMethodDecl jcmeth = (JCMethodDecl)member;
                        excludes.add(jcMethodDeclStr(jcmeth));
                     }
                }
                
                // Clone what is needed from FXBase/FXObject.
                cDefinitions.appendList(javaCodeMaker.cloneFXBase(excludes));
            }
        } else {
            cDefinitions.appendList(javaCodeMaker.makeAttributeFields(instanceAttributeInfos));
            iDefinitions.appendList(javaCodeMaker.makeMemberVariableAccessorInterfaceMethods());

            if (isScriptClass) {
                cDefinitions.appendList(javaCodeMaker.makeInitClassMaps(initClassMap));
            }

            // Static(script) vars are exposed in class
            cDefinitions.appendList(javaCodeMaker.makeAttributeFields(staticAttributeInfos));
            cDefinitions.appendList(javaCodeMaker.makeAttributeAccessorMethods(staticAttributeInfos));
            cDefinitions.append    (makeInitStaticAttributesBlock(cDecl, translatedAttrInfo, null));

            cDefinitions.appendList(javaCodeMaker.makeMixinApplyDefaultsMethods(instanceAttributeInfos));
            iDefinitions.appendList(makeFunctionInterfaceMethods(cDecl));
            iDefinitions.appendList(makeOuterAccessorInterfaceMembers(cDecl));
        }

        Name interfaceName = isMixinClass ? interfaceName(cDecl) : null;

        return new JavafxClassModel(
                interfaceName,
                makeImplementingInterfaces(diagPos, cDecl, immediateMixinClasses),
                iDefinitions.toList(),
                cDefinitions.toList(),
                makeAdditionalImports(diagPos, cDecl, immediateMixinClasses),
                superType,
                fxSuperClassSym,
                superClasses,
                immediateMixinClasses,
                allMixinClasses);
    }

    private String jcMethodDeclStr(JCMethodDecl meth) {
        String str = meth.name.toString() + "(";
        boolean needsSpace = false;
        for (JCVariableDecl varDecl : meth.getParameters()) {
            if (needsSpace) str += " ";
            str += varDecl.vartype.toString();
            needsSpace = true;
        }
        str += ")";
        return str;
    }

    private List<JCTree> makeFunctionInterfaceMethods(JFXClassDeclaration cDecl) {
        ListBuffer<JCTree> methods = ListBuffer.lb();
        for (JFXTree def : cDecl.getMembers()) {
            if (def.getFXTag() == JavafxTag.FUNCTION_DEF) {
                JFXFunctionDefinition func = (JFXFunctionDefinition) def;
                MethodSymbol sym = func.sym;
                if ((sym.flags() & (Flags.SYNTHETIC | Flags.STATIC | Flags.PRIVATE)) == 0) {
                    appendMethodClones(methods, cDecl, sym, false);
                }
            }
        }
        return methods.toList();
    }

    /** add proxies which redirect to the static implementation for every concrete method
     *
     * @param cDecl
     * @param needDispatch
     * @return
     */
    private List<JCTree> makeFunctionProxyMethods(JFXClassDeclaration cDecl, List<MethodSymbol> needDispatch) {
        ListBuffer<JCTree> methods = ListBuffer.lb();
        for (MethodSymbol sym : needDispatch) {
            if ((sym.flags() & Flags.PRIVATE) == 0) {
                appendMethodClones(methods, cDecl, sym, true);
            }
        }
        return methods.toList();
    }

   /**
     * Make a method from a MethodSymbol and an optional method body.
     * Make a bound version if "isBound" is set.
     */
    private void appendMethodClones(ListBuffer<JCTree> methods, JFXClassDeclaration cDecl, MethodSymbol sym, boolean withDispatch) {
        //TODO: static test is broken
        boolean isBound = (sym.flags() & JavafxFlags.BOUND) != 0;
        JCBlock mthBody = withDispatch ? makeDispatchBody(cDecl, sym, isBound, (sym.flags() & Flags.STATIC) != 0) : null;
        DiagnosticPosition diagPos = cDecl;
        // build the parameter list
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        for (VarSymbol vsym : sym.getParameters()) {
            Type vtype = vsym.asType();
            if (isBound) {
                TODO();
            }
            params.append(make.VarDef(
                    make.Modifiers(0L),
                    vsym.name,
                    makeType(diagPos, vtype),
                    null // no initial value
                     // no initial value
                    ));
        }

        // make the method
        JCModifiers mods = make.Modifiers(Flags.PUBLIC | (mthBody==null? Flags.ABSTRACT : 0L));
        if (sym.owner == cDecl.sym)
            mods = addAccessAnnotationModifiers(diagPos, sym.flags(), mods);
        else
            mods = addInheritedAnnotationModifiers(diagPos, sym.flags(), mods);
        methods.append(make.at(diagPos).MethodDef(
                        mods,
                        functionName(sym, false, isBound),
                        makeReturnTypeTree(diagPos, sym, isBound),
                        List.<JCTypeParameter>nil(),
                        params.toList(),
                        List.<JCExpression>nil(),
                        mthBody,
                        null));
        if (withDispatch) {
            optStat.recordProxyMethod();
        }
    }


    private List<JCExpression> makeImplementingInterfaces(DiagnosticPosition diagPos,
            JFXClassDeclaration cDecl,
            List<ClassSymbol> baseInterfaces) {
        ListBuffer<JCExpression> implementing = ListBuffer.lb();

        if (cDecl.isMixinClass()) {
            implementing.append(makeIdentifier(diagPos, fxMixinString));
        } else {
            implementing.append(makeIdentifier(diagPos, fxObjectString));
        }

        for (JFXExpression intf : cDecl.getImplementing()) {
            implementing.append(makeType(diagPos, intf.type, false));
        }

        for (ClassSymbol baseClass : baseInterfaces) {
            implementing.append(makeType(diagPos, baseClass.type, true));
        }
        return implementing.toList();
    }

    private List<JCExpression> makeAdditionalImports(DiagnosticPosition diagPos, JFXClassDeclaration cDecl, List<ClassSymbol> baseInterfaces) {
        // Add import statements for all the base classes and basClass $Mixin(s).
        // There might be references to them when the methods/attributes are rolled up.
        ListBuffer<JCExpression> additionalImports = new ListBuffer<JCExpression>();
        for (ClassSymbol baseClass : baseInterfaces) {
            if (baseClass.type != null && baseClass.type.tsym != null &&
                    baseClass.type.tsym.packge() != cDecl.sym.packge() &&     // Work around javac bug (CR 6695838)
                    baseClass.type.tsym.packge() != syms.unnamedPackage) {    // Work around javac bug. the visitImport of Attr
                // is casting to JCFieldAcces, but if you have imported an
                // JCIdent only a ClassCastException is thrown.
                additionalImports.append(makeType( diagPos,baseClass.type, false));
                additionalImports.append(makeType( diagPos,baseClass.type, true));
            }
        }
        return additionalImports.toList();
    }

    /**
     * Make a constructor to be called by Java code.
     * Simply pass up to super, unless this is the last JavaFX class, in which case add object initialization
     * @param diagPos
     * @param superIsFX true if there is a super class (in the generated code) and it is a JavaFX class
     * @return the constructor
     */
    private JCMethodDecl makeJavaEntryConstructor(DiagnosticPosition diagPos) {
        make.at(diagPos);

        //    public Foo() {
        //        this(false);
        //        initialize$();
        //    }
        return makeConstructor(diagPos, List.<JCVariableDecl>nil(), List.of(
                callStatement(diagPos, names._this, make.Literal(TypeTags.BOOLEAN, 0)),
                callStatement(diagPos, defs.initializeName, List.<JCExpression>nil())));
    }

    /**
     * Make a constructor to be called by JavaFX code.
     * @param diagPos
     * @param superIsFX true if there is a super class (in the generated code) and it is a JavaFX class
     * @return the constructor
     */
    private JCMethodDecl makeFXEntryConstructor(DiagnosticPosition diagPos, ClassSymbol outerTypeSym, boolean superIsFX) {
        make.at(diagPos);
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Name dummyParamName = names.fromString("dummy");

        // call the FX version of the constructor in the superclass
        //    public Foo(boolean dummy) {
        //        super(dummy);
        //    }
        if (superIsFX) {
            stmts.append(callStatement(diagPos,
                names._super,
                List.<JCExpression>of(make.Ident(dummyParamName))));
        } else {
            stmts.append(callStatement(diagPos,
                defs.initFXBaseName,
                List.<JCExpression>nil()));
        }

        // Construct the parameters
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        if (outerTypeSym != null) {
               // add a parameter and a statement to constructor for the outer instance reference
                params.append( makeParam(diagPos, outerAccessorFieldName, make.Ident(outerTypeSym)) );
                JCFieldAccess cSelect = make.Select(make.Ident(names._this), outerAccessorFieldName);
                JCAssign assignStat = make.Assign(cSelect, make.Ident(outerAccessorFieldName));
                stmts.append(make.Exec(assignStat));
        }
        params.append( makeParam(diagPos, dummyParamName, syms.booleanType) );

        return makeConstructor(diagPos, params.toList(), stmts.toList());
    }

   private JCMethodDecl makeConstructor(DiagnosticPosition diagPos, List<JCVariableDecl> params, List<JCStatement> cStats) {
       return make.MethodDef(make.Modifiers(Flags.PUBLIC),
               names.init,
               make.TypeIdent(TypeTags.VOID),
               List.<JCTypeParameter>nil(),
               params,
               List.<JCExpression>nil(),
               make.Block(0L, cStats),
               null);

   }

    // Add the methods and field for accessing the outer members. Also add a constructor with an extra parameter to handle the instantiation of the classes that access outer members
    private ClassSymbol outerTypeSymbol(JFXClassDeclaration cdecl) {
        if (cdecl.sym != null && toJava.hasOuters.contains(cdecl.sym)) {
            Symbol typeOwner = cdecl.sym.owner;
            while (typeOwner != null && typeOwner.kind != Kinds.TYP) {
                typeOwner = typeOwner.owner;
            }

            if (typeOwner != null) {
                // Only return an interface class if it's a mixin.
                return !isMixinClass((ClassSymbol)typeOwner) ? (ClassSymbol)typeOwner.type.tsym :
                        reader.jreader.enterClass(names.fromString(typeOwner.type.toString() + mixinSuffix));
            }
        }
        return null;
   }

    // Make the field for accessing the outer members
    private JCTree makeOuterAccessorField(DiagnosticPosition diagPos, JFXClassDeclaration cdecl, ClassSymbol outerTypeSym) {
        // Create the field to store the outer instance reference
        return make.at(diagPos).VarDef(make.at(diagPos).Modifiers(Flags.PUBLIC), outerAccessorFieldName, make.Ident(outerTypeSym), null);
    }

    // Make the method for accessing the outer members
    private JCTree makeOuterAccessorMethod(DiagnosticPosition diagPos, JFXClassDeclaration cdecl, ClassSymbol outerTypeSym) {
        make.at(diagPos);
        VarSymbol vs = new VarSymbol(Flags.PUBLIC, outerAccessorFieldName, outerTypeSym.type, cdecl.sym);
        JCIdent retIdent = make.Ident(vs);
        JCStatement retRet = make.Return(retIdent);
        List<JCStatement> mStats = List.of(retRet);
        return make.MethodDef(make.Modifiers(Flags.PUBLIC), defs.outerAccessorName, make.Ident(outerTypeSym), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
                List.<JCExpression>nil(), make.Block(0L, mStats), null);
    }

    // methods for accessing the outer members.
    private List<JCTree> makeOuterAccessorInterfaceMembers(JFXClassDeclaration cdecl) {
        ListBuffer<JCTree> members = ListBuffer.lb();
        if (cdecl.sym != null && toJava.hasOuters.contains(cdecl.sym)) {
            Symbol typeOwner = cdecl.sym.owner;
            while (typeOwner != null && typeOwner.kind != Kinds.TYP) {
                typeOwner = typeOwner.owner;
            }

            if (typeOwner != null) {
                ClassSymbol returnSym = reader.enterClass(names.fromString(typeOwner.type.toString() + mixinSuffix));
                JCMethodDecl accessorMethod = make.MethodDef(
                        make.Modifiers(Flags.PUBLIC),
                        defs.outerAccessorName,
                        make.Ident(returnSym),
                        List.<JCTypeParameter>nil(),
                        List.<JCVariableDecl>nil(),
                        List.<JCExpression>nil(), null, null);
                members.append(accessorMethod);
                optStat.recordProxyMethod();
            }
        }
        return members.toList();
    }

    private JCStatement makeSuperCall(DiagnosticPosition diagPos, ClassSymbol cSym, Name methodName, boolean fromMixin) {
        if ((cSym.flags() & JavafxFlags.MIXIN) != 0) {
            // call to a mixin super, use local static reference
            Name rcvr = fromMixin? defs.receiverName : names._this;
            return callStatement(diagPos,
                    makeType(diagPos, cSym.type, false),
                    methodName, make.at(diagPos).Ident(rcvr));
        } else {
            // call to a non-mixin super, use "super"
            return callStatement(diagPos, make.at(diagPos).Ident(names._super), methodName);
        }
    }

    private JCMethodDecl makeInitMethod(DiagnosticPosition diagPos, Name name, Type retType, List<JCStatement> stmts) {
        make.at(diagPos);
        JCBlock initializeBlock = make.Block(0L, stmts);
        return make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                name,
                makeType(diagPos, retType),
                List.<JCTypeParameter>nil(),
                List.<JCVariableDecl>nil(),
                List.<JCExpression>nil(),
                initializeBlock,
                null);
    }

    /**
     * Construct the static block for setting defaults
     * */
    private JCBlock makeInitStaticAttributesBlock(JFXClassDeclaration cDecl,
            List<TranslatedVarInfo> translatedAttrInfo,
            JCStatement initMap) {
        // Add the initialization of this class' attributesa
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // Initialize the var map for anon class.
        if (initMap != null) {
            stmts.append(initMap);
        }

        boolean isLibrary = toJava.getAttrEnv().toplevel.isLibrary;
        for (TranslatedVarInfo tai : translatedAttrInfo) {
            assert tai.jfxVar() != null;
            assert tai.jfxVar().getFXTag() == JavafxTag.VAR_DEF;
            assert tai.jfxVar().pos != Position.NOPOS;
            if (tai.isStatic()) {
                DiagnosticPosition diagPos = tai.pos();
                // don't put variable initialization in the static initializer if this is a simple-form
                // script (where variable initialization is done in the run method).
                if (tai.isDirectOwner() && isLibrary) {
                    if (tai.getDefaultInitStatement() != null) {
                        stmts.append(tai.getDefaultInitStatement());
                    }
                }
            }
        }
        return make.at(cDecl.pos()).Block(Flags.STATIC, stmts.toList());
    }

    /**
     * Make a method body which redirects to the actual implementation in a static method of the defining class.
     */
    private JCBlock makeDispatchBody(JFXClassDeclaration cDecl, MethodSymbol mth, boolean isBound, boolean isStatic) {
        ListBuffer<JCExpression> args = ListBuffer.lb();
        if (!isStatic) {
            // Add the this argument, so the static implementation method is invoked
            args.append(make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this)));
        }
        for (VarSymbol var : mth.params) {
            args.append(make.Ident(var.name));
        }
        JCExpression receiver = mth.owner == cDecl.sym ? null : makeType(cDecl.pos(), mth.owner.type, false);
        JCExpression expr = callExpression(cDecl.pos(), receiver, functionName(mth, !isStatic, isBound), args);
        JCStatement statement = (mth.getReturnType() == syms.voidType) ? make.Exec(expr) : make.Return(expr);
        return make.at(cDecl.pos()).Block(0L, List.<JCStatement>of(statement));
    }

    protected String getSyntheticPrefix() {
        return "ifx$";
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    //
    // This class is used to simplify the construction of java code in the
    // initialization builder.
    //
    class JavaCodeMaker extends JavaTreeBuilder {
        // The current class analysis/
        private final JavafxAnalyzeClass analysis;

        JavaCodeMaker(JavafxAnalyzeClass analysis) {
            super(analysis.getCurrentClassPos());
            this.analysis = analysis;
        }

        //
        // Methods for managing the current diagnostic position.
        //
        private void setDiagPos(VarInfo ai) { setDiagPos(ai.pos()); }
        private void resetDiagPos() { setDiagPos(analysis.getCurrentClassPos()); }

        //
        // This method simplifies throw statements.
        //
        private JCStatement Throw(Type type, String message) {
            if (message != null) {
                return m().Throw(m().NewClass(null, null, makeType(type), List.<JCExpression>of(makeString(message)), null));
            } else {
                return m().Throw(m().NewClass(null, null, makeType(type), List.<JCExpression>nil(), null));
            }
        }
        private JCStatement Throw(Type type) {
            return Throw(type, null);
        }

        //
        // This method generates a simple java integer field then adds to the buffer.
        //
        private JCVariableDecl addSimpleIntVariable(long modifiers, Name name, int value) {
            // Construct the variable itself.
            return makeVar(modifiers, syms.intType, name, makeInt(value));
        }

        //
        // This method generates a java field for a varInfo.
        //
        private JCVariableDecl makeVariableField(VarInfo varInfo, JCModifiers mods, Type varType, Name name, JCExpression varInit) {
            setDiagPos(varInfo);
            // Define the type.
            JCExpression type = makeType(varType);
            // Construct the variable itself.
            JCVariableDecl var = m().VarDef(mods, name, type, varInit);
             // Update the statistics.
            optStat.recordClassVar(varInfo.getSymbol());
            optStat.recordConcreteField();

            return var;
        }

        //
        // Build the location and value field for each attribute.
        //
        public List<JCTree> makeAttributeFields(List<? extends VarInfo> attrInfos) {
            // Buffer for new vars.
            ListBuffer<JCTree> vars = ListBuffer.lb();

            for (VarInfo ai : attrInfos) {
                // Only process attributes declared in this class (includes mixins.)
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Set the current diagnostic position.
                    setDiagPos(ai);
                    // Grab the variable symbol.
                    VarSymbol varSym = ai.getSymbol();
                    // The fields need to be available to reflection.
                    // TODO deal with defs.
                    JCModifiers mods = m().Modifiers(Flags.PUBLIC | (ai.getFlags() & Flags.STATIC));

                    // Apply annotations, if current class then add source annotations.
                    if (varSym.owner == analysis.getCurrentClassSymbol()) {
                        List<JCAnnotation> annotations = List.<JCAnnotation>of(make.Annotation(
                                makeIdentifier(diagPos, JavafxSymtab.sourceNameAnnotationClassNameString),
                                List.<JCExpression>of(m().Literal(varSym.name.toString()))));
                        mods = addAccessAnnotationModifiers(diagPos, varSym.flags(), mods, annotations);
                    } else {
                        mods = addInheritedAnnotationModifiers(diagPos, varSym.flags(), mods);
                    }

                    // Construct the value field 
                    vars.append(makeVariableField(ai, mods, ai.getRealType(), attributeValueName(varSym),
                                needsDefaultValue(ai.getVMI()) ? makeDefaultValue(diagPos, ai.getVMI()) : null));
                }
            }

            return vars.toList();
        }

        //
        // This method constructs modifiers for getters/setters and proxies.
        //
        private JCModifiers proxyModifiers(VarInfo ai, boolean isAbstract) {
            // Copy flags from VarInfo.
            long flags = ai.getFlags();

            // Set up basic flags.
            JCModifiers mods = make.Modifiers((flags & Flags.STATIC) | (isAbstract ? (Flags.PUBLIC | Flags.ABSTRACT) : Flags.PUBLIC));

            // If var is in current class.
            if (ai.getSymbol().owner == analysis.getCurrentClassSymbol()) {
                // Use local access modifiers.
                mods = addAccessAnnotationModifiers(ai.pos(), flags, mods);
            } else {
                // Use inherited modifiers.
                mods = addInheritedAnnotationModifiers(ai.pos(), flags, mods);
            }

            return mods;
        }

        //
        // These methods return an expression for testing/setting/clearing a var flag.
        //
        JCExpression makeFlagExpression(VarInfo varInfo, String action, String flag) {
            return makeFlagExpression(attributeOffsetName(varInfo.getSymbol()), action, flag);
        }
        JCExpression makeFlagExpression(Name varOffsetName, String action, String flag) {
            return callExpression(names.fromString(action + flag), id(varOffsetName));
        }

        //
        // These methods returns a statement for setting/clearing a var flag.
        //
        JCStatement makeFlagStatement(VarInfo varInfo, String action, String flag) {
            return makeFlagStatement(attributeOffsetName(varInfo.getSymbol()), action, flag);
        }
        JCStatement makeFlagStatement(Name varOffsetName, String action, String flag) {
            return callStatement(names.fromString(action + flag), id(varOffsetName));
        }

        //
        // This method constructs the getter method for the specified attribute.
        //
        private JCTree makeGetterAccessorMethod(VarInfo varInfo, boolean needsBody) {
            // Symbol used on the method.
            VarSymbol varSym = varInfo.getSymbol();
            // Real type for var.
            Type type = varInfo.getRealType();
            // Assume no body.
            ListBuffer<JCStatement> stmts = null;

            if (needsBody) {
                // Prepare to accumulate statements.
                stmts = ListBuffer.lb();

                // Symbol used when accessing the variable.
                VarSymbol proxyVarSym = varInfo.proxyVarSym();
                // Name of variable.
                Name name = attributeValueName(proxyVarSym);

                if (varInfo.hasBoundDefinition()) {
                    assert varInfo.boundInit() != null;

                    // !isValidValue$(VOFF$var)
                    JCExpression condition = makeNot(makeFlagExpression(varInfo, varFlagActionTest, varFlagValid));
                    
                    // Prepare to accumulate body of if.
                    ListBuffer<JCStatement> ifStmts = ListBuffer.lb();
                    
                    // set$var(init/bound expression)
                    ifStmts.appendList(varInfo.boundPreface());
                    ifStmts.append(callStatement(attributeBeName(varSym), varInfo.boundInit()));
                  
                    // if (!isValidValue$(VOFF$var)) { set$var(init/bound expression); }
                    stmts.append(m().If(condition, m().Block(0L, ifStmts.toList()), null));
                } 

                // Construct and add: return $var;
                stmts.append(m().Return(id(name)));
            }

            // Construct method.
            JCMethodDecl method = makeMethod(proxyModifiers(varInfo, !needsBody),
                                             type,
                                             attributeGetterName(varSym),
                                             List.<JCVariableDecl>nil(),
                                             stmts);
            optStat.recordProxyMethod();

            return method;
        }

        //
        // This method constructs the setter method for the specified attribute.
        //
        private JCTree makeSetterAccessorMethod(VarInfo varInfo, boolean needsBody) {
            // Symbol used on the method.
            VarSymbol varSym = varInfo.getSymbol();
            // Real type for var.
            Type type = varInfo.getRealType();
            // Assume no body.
            ListBuffer<JCStatement> stmts = null;

            if (needsBody) {
                // Prepare to accumulate statements.
                stmts = ListBuffer.lb();
                
                if (varInfo.hasBoundDefinition() && !varInfo.hasBiDiBoundDefinition()) {
                    stmts.append(Throw(assignBindExceptionType));
                } else if (varInfo.isDef()) {
                    stmts.append(Throw(assignDefExceptionType));
                } else {
                    // Symbol used when accessing the variable.
                    VarSymbol proxyVarSym = varInfo.proxyVarSym();
                    // $var
                    Name varName = attributeValueName(proxyVarSym);
                    // set$var(value)
                    stmts.append(callStatement(attributeBeName(varSym), id(varNewValueName)));
                    // return $var;
                    stmts.append(m().Return(id(varName)));
                }
            }

            // Set up value arg.
            JCVariableDecl arg = m().VarDef(m().Modifiers(Flags.PARAMETER),
                                                          varNewValueName,
                                                          makeType(type),
                                                          null);
            // Construct method.
            JCMethodDecl method = makeMethod(proxyModifiers(varInfo, !needsBody),
                                             type,
                                             attributeSetterName(varSym),
                                             List.<JCVariableDecl>of(arg),
                                             stmts);
            optStat.recordProxyMethod();

            return method;
        }

        //
        // This method constructs the be method for the specified attribute.
        //
        private JCTree makeBeAccessorMethod(VarInfo varInfo, boolean needsBody) {
            // Symbol used on the method.
            VarSymbol varSym = varInfo.getSymbol();
            // Real type for var.
            Type type = varInfo.getRealType();
            // Assume no body.
            ListBuffer<JCStatement> stmts = null;

            if (needsBody) {
                // Prepare to accumulate statements.
                stmts = ListBuffer.lb();

                // Symbol used when accessing the variable.
                VarSymbol proxyVarSym = varInfo.proxyVarSym();
    
                // $var
                Name varName = attributeValueName(proxyVarSym);
    
                // T varOldValue$ = $var;
                stmts.append(makeVar(Flags.FINAL, type, varOldValueName, id(varName)));

                // Prepare to accumulate trigger statements.
                ListBuffer<JCStatement> ifStmts = ListBuffer.lb();

                // $var = value
                ifStmts.append(makeExec(m().Assign(id(varName), id(varNewValueName))));

                // invalidate$()
                ifStmts.append(callStatement(attributeInvalidateName(varSym)));

                // setIsValidValue(VOFF$var);
                ifStmts.append(makeFlagStatement(varInfo, varFlagActionSet, varFlagValid));

                // onReplace$(varOldValue$)
                ifStmts.append(callStatement(attributeOnReplaceName(varSym), id(varOldValueName)));

                // varOldValue$ != varNewValue$
                // or !varOldValue$.isEquals(varNewValue$) test for Objects and Sequences
                JCExpression testExpr = type.isPrimitive() ?
                    makeNotEqual(id(varOldValueName), id(varNewValueName))
                  : makeNot(runtime(diagPos, defs.Util_isEqual, List.<JCExpression>of(id(varOldValueName), id(varNewValueName))));

               // if (varOldValue$ != varNewValue$) { handle change }
                stmts.append(m().If(testExpr, m().Block(0L, ifStmts.toList()), null));
            }

            // Set up value arg.
            JCVariableDecl arg = m().VarDef(m().Modifiers(Flags.PARAMETER),
                                                          varNewValueName,
                                                          makeType(type),
                                                          null);
            // Construct method.
            JCMethodDecl method = makeMethod(proxyModifiers(varInfo, !needsBody),
                                             syms.voidType,
                                             attributeBeName(varSym),
                                             List.<JCVariableDecl>of(arg),
                                             stmts);
            optStat.recordProxyMethod();

            return method;
        }
        
        //
        // This method constructs the invalidate method for the specified attribute.
        //
        private JCTree makeInvalidateAccessorMethod(VarInfo varInfo, boolean needsBody) {
            // Symbol used on the method.
            VarSymbol varSym = varInfo.getSymbol();
            // Real type for var.
            Type type = varInfo.getRealType();
            // Assume no body.
            ListBuffer<JCStatement> stmts = null;

            if (needsBody) {
                // Prepare to accumulate statements.
                stmts = ListBuffer.lb();

                // Symbol used when accessing the variable.
                VarSymbol proxyVarSym = varInfo.proxyVarSym();
    
                // $var
                Name varName = attributeValueName(proxyVarSym);
                
                // Prepare to accumulate if statements.
                ListBuffer<JCStatement> ifStmts = ListBuffer.lb();
                
                // Call super first.
                ClassSymbol superClassSym = analysis.getFXSuperClassSym();
                if (varInfo.isOverride() && superClassSym != null) {
                    ifStmts.append(makeSuperCall(superClassSym, attributeInvalidateName(varSym)));
                }
                
                // clearValidValue$(VOFF$var);
                ifStmts.append(makeFlagStatement(varInfo, varFlagActionClear, varFlagValid));
                
                // Handle binders.
                if (varInfo instanceof TranslatedVarInfoBase) {
                    for (VarSymbol otherVarSym : ((TranslatedVarInfoBase)varInfo).boundBinders()) {
                        // invalidate$var();
                        ifStmts.append(callStatement(attributeInvalidateName(otherVarSym)));
                    }
                }
                
                // notifyDependents(VOFF$var););
                ifStmts.append(callStatement(defs.attributeNotifyDependentsName, id(attributeOffsetName(varSym))));
                
                // isValid
                JCExpression test = makeFlagExpression(varInfo, varFlagActionTest, varFlagValid);
                
                // if (!isValidValue$(VOFF$var)) { ... invalidate  code ... }
                stmts.append(m().If(test, m().Block(0L, ifStmts.toList()), null));
            }

            // Construct method.
            JCMethodDecl method = makeMethod(proxyModifiers(varInfo, !needsBody),
                                             syms.voidType,
                                             attributeInvalidateName(varSym),
                                             List.<JCVariableDecl>nil(),
                                             stmts);
            optStat.recordProxyMethod();

            return method;
        }

        //
        // This method constructs the onreplace$ method for the specified attribute.
        //
        private JCTree makeOnReplaceAccessorMethod(VarInfo varInfo, boolean needsBody) {
            // Symbol used on the method.
            VarSymbol varSym = varInfo.getSymbol();
            // Real type for var.
            Type type = varInfo.getRealType();
            // Assume no body.
            ListBuffer<JCStatement> stmts = null;
            // Assume the onReplace arg name.
            Name oldValueName = varOldValueName;

            if (needsBody) {
                // Prepare to accumulate statements.
                stmts = ListBuffer.lb();

                // Symbol used when accessing the variable.
                VarSymbol proxyVarSym = varInfo.proxyVarSym();
    
                // $var
                Name varName = attributeValueName(proxyVarSym);
                
                // Fetch the on replace statement or null.
                JCStatement onReplace = varInfo.onReplaceAsInline();

                // Need to capture init state if has trigger.
                if (onReplace != null) {
                    // Gather specified var info.
                    JFXVar oldVar = varInfo.onReplace().getOldValue();
                    JFXVar newVar = varInfo.onReplace().getNewElements();
    
                     // Check to see if the on replace has an old value.
                    if (oldVar != null) {
                        // Change the onReplace arg name. 
                        oldValueName = oldVar.getName();
                    }
    
                     // Check to see if the on replace has a new value.
                    if (newVar != null) {
                        // T newValue = value
                        stmts.append(makeVar(Flags.FINAL, type, newVar.getName(), id(varName)));
                    }
    
                    // Need a receiver under some circumstances.
                    if (!varInfo.isStatic()) {
                        // T receiver$ = this.
                        stmts.append(makeVar(Flags.FINAL, analysis.getCurrentClassSymbol().type, defs.receiverName, id(names._this)));
                    }
    
                    // Insert the trigger.
                    stmts.append(onReplace);
                }
                
                // Call super first.
                ClassSymbol superClassSym = analysis.getFXSuperClassSym();
                if (varInfo.isOverride() && superClassSym != null) {
                    stmts.prepend(makeSuperCall(superClassSym, attributeOnReplaceName(varSym), List.<JCExpression>of(id(oldValueName))));
                }
            }

            // Set up value arg.
            JCVariableDecl arg = m().VarDef(m().Modifiers(Flags.PARAMETER),
                                                          oldValueName,
                                                          makeType(type),
                                                          null);
            // Construct method.
            JCMethodDecl method = makeMethod(proxyModifiers(varInfo, !needsBody),
                                             syms.voidType,
                                             attributeOnReplaceName(varSym),
                                             List.<JCVariableDecl>of(arg),
                                             stmts);
            optStat.recordProxyMethod();

            return method;
        }
        
        
        //
        // This method constructs the accessor methods for an attribute.
        //
        public  List<JCTree> makeAnAttributeAccessorMethods(VarInfo ai, boolean needsBody) {
            ListBuffer<JCTree> accessors = ListBuffer.lb();
            setDiagPos(ai.pos());

            if (ai.useAccessors()) {
                if (!ai.isOverride()) {
                    accessors.append(makeGetterAccessorMethod(ai, needsBody));
                    accessors.append(makeSetterAccessorMethod(ai, needsBody));
                    accessors.append(makeBeAccessorMethod(ai, needsBody));
                    accessors.append(makeInvalidateAccessorMethod(ai, needsBody));
                    accessors.append(makeOnReplaceAccessorMethod(ai, needsBody));
                } else if (needsBody) {
                    if (ai.hasBoundDefinition()) {
                        accessors.append(makeGetterAccessorMethod(ai, needsBody));
                        accessors.append(makeInvalidateAccessorMethod(ai, needsBody));
                    }
                    if (ai.onReplace() != null) {
                        accessors.append(makeOnReplaceAccessorMethod(ai, needsBody));
                    }
                }
            }
            
            return accessors.toList();
        }
        

        //
        // This method constructs the accessor methods for each attribute.
        //
        public List<JCTree> makeAttributeAccessorMethods(List<VarInfo> attrInfos) {
            ListBuffer<JCTree> accessors = ListBuffer.lb();

            for (VarInfo ai : attrInfos) {
                // Only create accessors for declared and proxied vars.
                if (ai.needsCloning()) {
                    accessors.appendList(makeAnAttributeAccessorMethods(ai, true));
                }
            }

            return accessors.toList();
        }

        //
        // This method constructs the abstract interfaces for the accessors in
        // a mixin class.
        //
        public List<JCTree> makeMemberVariableAccessorInterfaceMethods() {
            // Buffer for new decls.
            ListBuffer<JCTree> accessors = ListBuffer.lb();
            // TranslatedVarInfo for the current class.
            List<TranslatedVarInfo> translatedAttrInfo = analysis.getTranslatedAttrInfo();

            // Only for vars within the class.
            for (VarInfo ai : translatedAttrInfo) {
                if (!ai.isStatic()) {
                    accessors.appendList(makeAnAttributeAccessorMethods(ai, false));
                }
            }
            return accessors.toList();
        }

        //
        // This method generates an enumeration for each of the instance attributes
        // of the class.
        //
        public List<JCTree> makeAttributeNumbers(LiteralInitVarMap varMap) {
            // Buffer for new members.
            ListBuffer<JCTree> members = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();
            // Get the list of instance attributes.
            List<VarInfo> attrInfos = analysis.instanceAttributeInfos();

            // Construct a static count variable (VCNT$), -1 indicates count has not been initialized.
            members.append(addSimpleIntVariable(Flags.STATIC | Flags.PUBLIC, defs.varCountName, -1));

            // Construct a static count accessor method (VCNT$)
            members.append(makeVCNT$());

            // Construct a virtual count accessor method (count$)
            members.append(makecount$());

            // Accumulate variable numbering.
            for (VarInfo ai : attrInfos) {
                // Only variables actually declared.
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Set diagnostic position for attribute.
                    setDiagPos(ai.pos());

                    // Construct offset var.
                    Name name = attributeOffsetName(ai.getSymbol());
                    // Construct and add: public static int VOFF$name = n;
                    members.append(makeVar(Flags.STATIC | Flags.PUBLIC, syms.intType, name, null));
                }

                // Add to var map if an anon class.
                if (varMap != null) varMap.addVar(ai.getSymbol());
            }

            return members.toList();
        }

        //
        // The method constructs the VCNT$ method for the current class.
        //
        public JCTree makeVCNT$() {
            // Prepare to accumulate statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();
            // Grab the super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
            // Get the list of instance attributes.
            List<VarInfo> attrInfos = analysis.instanceAttributeInfos();
            // Number of variables in current class.
            int count = analysis.getVarCount();

            // Prepare to accumulate statements in the if.
            ListBuffer<JCStatement> ifStmts = ListBuffer.lb();

            // VCNT$ = super.VCNT$() + n  or VCNT$ = n;
            JCExpression setVCNT$Expr;

            // If has a javafx superclass.
            if (superClassSym == null) {
                // n
                setVCNT$Expr = makeInt(count);
            } else {
                // super.VCNT$() + n
                setVCNT$Expr = makeBinary(JCTree.PLUS, callExpression(superClassSym.type, defs.varCountName), makeInt(count));
            }

            Name countName = names.fromString("$count");
            // final int $count = VCNT$ = super.VCNT$() + n;
            ifStmts.append(makeVar(Flags.FINAL, syms.intType, countName, m().Assign(id(defs.varCountName), setVCNT$Expr)));

            for (VarInfo ai : attrInfos) {
                // Only variables actually declared.
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Set diagnostic position for attribute.
                    setDiagPos(ai.pos());
                    // Offset var name.
                    Name name = attributeOffsetName(ai.getSymbol());
                    // VCNT$ - n + i;
                    JCExpression setVOFF$Expr = makeBinary(JCTree.PLUS, id(countName), makeInt(ai.getEnumeration() - count));
                    // VOFF$var = VCNT$ - n + i;
                    ifStmts.append(makeExec(m().Assign(id(name), setVOFF$Expr)));
                }
            }

            // VCNT$ == -1
            JCExpression condition = makeEqual(id(defs.varCountName), makeInt(-1));
            // if (VCNT$ == -1) { ...
            stmts.append(m().If(condition, m().Block(0, ifStmts.toList()), null));
            // return VCNT$;
            stmts.append(m().Return(id(defs.varCountName)));

            // Construct method.
            JCMethodDecl method = makeMethod(Flags.PUBLIC | Flags.STATIC,
                                             syms.intType,
                                             defs.varCountName,
                                             List.<JCVariableDecl>nil(),
                                             stmts);
            return method;
        }

        //
        // The method constructs the count$ method for the current class.
        //
        public JCTree makecount$() {
            // Prepare to accumulate statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();

            // VCNT$()
            JCExpression countExpr = callExpression(defs.varCountName);
            // Construct and add: return VCNT$();
            stmts.append(m().Return(countExpr));

            // Construct method.
            JCMethodDecl method = makeMethod(Flags.PUBLIC,
                                             syms.intType,
                                             defs.attributeCountMethodName,
                                             List.<JCVariableDecl>nil(),
                                             stmts);
            return method;
        }
        
        //
        // Clones a field declared in FXBase as an non-static field.  It also creates
        // FXObject accessor method.
        //
        private List<JCTree> cloneFXBaseVar(VarSymbol var, HashSet<String> excludes) {
            // Buffer for cloned members.
            ListBuffer<JCTree> members = ListBuffer.lb();
            // Var name as a string.
            String str = var.name.toString();
            // Var modifier flags.
            long flags = var.flags();
            
            // If it's an excluded name or a static then skip it.
            if (excludes.contains(str) ||
                (flags & (Flags.SYNTHETIC | Flags.STATIC)) != 0) {
                return members.toList();
            }
            
            // Var FX type.
            Type type = var.asType();
            
            // Clone the var.
            members.append(makeVar(flags, type, str, null));
            
            // Construct the getter.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            Name name = names.fromString("get" + str);
            stmts.append(m().Return(id(var.name)));
            // public int getVar { return Var; }
            JCMethodDecl getMeth = makeMethod(flags, type, name, List.<JCVariableDecl>nil(), stmts);
            // Add to members.
            members.append(getMeth);
            // Add to the exclusion set.
            excludes.add(jcMethodDeclStr(getMeth));

            // Construct the setter.
            stmts = ListBuffer.lb();
            name = names.fromString("set" + str);
            Name argName = names.fromString("value");
            JCVariableDecl arg = makeVar(Flags.PARAMETER | Flags.FINAL, type, argName, null);
            stmts.append(make.Exec(make.Assign(id(var.name), id(argName))));
            // public void setVar(final int value) { Var = value; }
            JCMethodDecl setMeth = makeMethod(flags, syms.voidType, name, List.<JCVariableDecl>of(arg), stmts);
            // Add to members.
            members.append(setMeth);
            // Add to the exclusion set.
            excludes.add(jcMethodDeclStr(setMeth));

            // Return the new members.
            return members.toList();
        }

        //
        // Clones a method declared as an FXObject interface to call the static 
        // equivalent in FXBase. 
        //
        private List<JCTree> cloneFXBaseMethod(MethodSymbol method, HashSet<String> excludes) {
            // Buffer for cloned members.
            ListBuffer<JCTree> members = ListBuffer.lb();
            // Method modifier flags.
            long flags = method.flags();
            
            // If it's an excluded name or a static then skip it.
            if (excludes.contains(method.toString()) ||
                (flags & (Flags.SYNTHETIC | Flags.STATIC)) != 0) {
                return members.toList();
            }

            // List of arguments to new method.
            ListBuffer<JCVariableDecl> args = ListBuffer.lb();
            // List of arguments to call supporting FXBase method.
            ListBuffer<JCExpression> callArgs = ListBuffer.lb();
            // Add this to to the call.
            callArgs.append(id(names._this));
            
            // Add arguments to both lists.
            for (VarSymbol argSym : method.getParameters()) {
                Type argType = argSym.asType();
                args.append(makeVar(Flags.PARAMETER, argSym.asType(), argSym.name, null));
                callArgs.append(id(argSym.name));
            }

            // Buffer for statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Method return type.
            Type returnType = method.getReturnType();
            // Basic call to supporting FXBase method.
            JCExpression call = callExpression(makeType(syms.javafx_FXBaseType), method.name, callArgs);
           
            // Exec or return based on return type.
            if (returnType == syms.voidType) {
                stmts.append(makeExec(call));
            } else {
                stmts.append(m().Return(call));
            }
    
            //  public type meth$(t0 arg0, ...) { return FXBase.meth$(this, arg0, ...); }
            members.append(makeMethod(Flags.PUBLIC, returnType, method.name, args.toList(), stmts));
            
            return members.toList();
        }

        //
        // This method clones the contents of FXBase and FXObject when inheriting
        // from a java class.
        //
        public List<JCTree> cloneFXBase(HashSet<String> excludes) {
            // Buffer for cloned members.
            ListBuffer<JCTree> members = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();

            // Retrieve FXBase and FXObject.
            ClassSymbol fxBaseSym = (ClassSymbol)syms.javafx_FXBaseType.tsym;
            ClassSymbol fxObjectSym = (ClassSymbol)syms.javafx_FXObjectType.tsym;
            Entry e;

            // Clone the vars in FXBase.
            for (e = fxBaseSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                if (e.sym instanceof VarSymbol) {
                     members.appendList(cloneFXBaseVar((VarSymbol)e.sym, excludes));
                }
            }

            // Clone the interfaces in FXObject.
            for (e = fxObjectSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                if (e.sym instanceof MethodSymbol) {
                     members.appendList(cloneFXBaseMethod((MethodSymbol)e.sym, excludes));
            }
            }

            return members.toList();
        }

        //
        // This method constructs the statements needed to apply defaults to a given var.
        //
        private JCStatement makeApplyDefaultsStatement(VarInfo ai, boolean isMixinClass) {
            if (ai.isInlinedBind()) { //TODO: Lombard
                // Inlined bind, don't set in applyDefaults$
                return null;
            }

            // Assume the worst.
            JCStatement stmt = null;
            // Get init statement.
            JCStatement init = ai.getDefaultInitStatement();

            if (init != null) {
                // a default exists, either on the direct attribute or on an override
                stmt = init;
            } else if (!isMixinClass) {
                if (ai.isMixinVar()) {
                    // Fetch the attribute symbol.
                    VarSymbol varSym = ai.getSymbol();
                    // Construct the name of the method.
                    Name methodName = attributeApplyDefaultsName(varSym);
                    // Include defaults for mixins into real classes.
                    stmt = makeSuperCall((ClassSymbol)varSym.owner, methodName, List.<JCExpression>of(id(names._this)));
               } else if (ai instanceof TranslatedVarInfo) {
                    //TODO: see SequenceVariable.setDefault() and JFXC-885
                    // setDefault() isn't really done for sequences
                   /**
                    if (!ai.isSequence()) {
                        // Make .setDefault() if Location (without clearing initialize bit) to fire trigger.
                        JCStatement setter = makeSetDefaultStatement(ai);
                        if (setter != null) {
                            stmt = setter;
                        }
                    }
                    * ***/
                }
            }

            return stmt;
        }

        //
        // This method constructs an applyDefaults method for each attribute in a mixin.
        //
        private List<JCTree> makeMixinApplyDefaultsMethods(List<? extends VarInfo> attrInfos) {
            // Prepare to accumulate methods.
            ListBuffer<JCTree> methods = ListBuffer.lb();

            for (VarInfo ai : attrInfos) {
                // True if the the user specified a default.
                boolean hasDefault = ai.getDefaultInitStatement() != null;

                // If the var is defined in the current class or it has a (override) default.
                if (ai.needsCloning() || hasDefault) {
                    // Set diagnostic position for attribute.
                    setDiagPos(ai.pos());
                    // Fetch the attribute symbol.
                    VarSymbol varSym = ai.getSymbol();

                    // Don't override someone elses default.
                    if (analysis.getCurrentClassSymbol() != varSym.owner && !hasDefault) {
                        continue;
                    }

                    // Prepare to accumulate statements.
                    ListBuffer<JCStatement> stmts = ListBuffer.lb();

                    // Get body of applyDefaults$.
                    JCStatement deflt = makeApplyDefaultsStatement(ai, true);
                    if (deflt != null) {
                        stmts.append(deflt);
                    }

                    // Mixins need a receiver arg.
                    List<JCVariableDecl> args = List.<JCVariableDecl>of(makeReceiverParam(analysis.getCurrentClassDecl()));

                    // Construct method.
                    JCMethodDecl method = makeMethod(Flags.PUBLIC | Flags.STATIC,
                                                     syms.voidType,
                                                     attributeApplyDefaultsName(varSym),
                                                     args,
                                                     stmts);
                    methods.append(method);
                }
            }
            return methods.toList();
        }

        //
        // This method constructs the current class's applyDefaults$ method.
        //
        public List<JCTree> makeApplyDefaultsMethod() {
            // Buffer for new methods.
            ListBuffer<JCTree> methods = ListBuffer.lb();

            // Number of variables in current class.
            int count = analysis.getVarCount();

            // Grab the super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
            // Mixin vars always have applyDefaults.
            boolean isMixinClass = analysis.isMixinClass();

            // Prepare to accumulate statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();

            // Prepare to accumulate cases.
            ListBuffer<JCCase> cases = ListBuffer.lb();
             // Prepare to accumulate overrides.
            ListBuffer<JCStatement> overrides = ListBuffer.lb();

            // Gather the instance attributes.
            List<VarInfo> attrInfos = analysis.instanceAttributeInfos();
            for (VarInfo ai : attrInfos) {
                // Only declared attributes with default expressions.
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Prepare to accumulate case statements.
                    ListBuffer<JCStatement> caseStmts = ListBuffer.lb();

                    // Get body of applyDefaults$.
                    JCStatement deflt = makeApplyDefaultsStatement(ai, isMixinClass);

                    // Something to set when we have a default.
                    if (deflt != null) {
                        // applyDefaults$var();
                        caseStmts.append(deflt);
                    }

                    // Build the case
                    {
                        // return
                        JCStatement returnExpr = m().Return(null);
                        caseStmts.append(returnExpr);

                        // case tag number
                        JCExpression tag = makeInt(ai.getEnumeration() - count);

                        // Add the case, something like:
                        // case i: applyDefaults$var(); return;
                        cases.append(m().Case(tag, caseStmts.toList()));
                    }
                } else {
                    // Get init statement.
                    JCStatement init = ai.getDefaultInitStatement();

                    if (init != null) {
                        // varNum == VOFF$var
                        JCExpression isRightVarExpr = makeEqual(id(varNumName), id(attributeOffsetName(ai.getSymbol())));
                        // { init; return; }
                        JCBlock block = m().Block(0, List.<JCStatement>of(init, m().Return(null)));
                        // if (varNum == VOFF$var) { init; return; }
                        overrides.append(m().If(isRightVarExpr, block, null));
                    }
                }
            }

            // Reset diagnostic position to current class.
            resetDiagPos();

            // Has some defaults.
            boolean hasDefaults = cases.nonEmpty() || overrides.nonEmpty();

            // If there were some location vars.
            if (cases.nonEmpty()) {
                // varNum - VCNT$
                JCExpression tagExpr = makeBinary(JCTree.MINUS, id(varNumName), id(defs.varCountName));
                // Construct and add: switch(varNum - VCNT$) { ... }
                stmts.append(m().Switch(tagExpr, cases.toList()));
            }

            // Add overrides.
            stmts.appendList(overrides);

            // generate method if it is worthwhile or we have to.
            if (hasDefaults || superClassSym == null) {
               // If there is a super class.
                if (superClassSym != null) {
                    // super
                    JCExpression selector = id(names._super);
                    // (varNum)
                    List<JCExpression> args = List.<JCExpression>of(id(varNumName));
                    // Construct and add: return super.applyDefaults$(varNum);
                    stmts.append(callStatement(selector, defs.applyDefaultsPrefixName, args));
                }

                // varNum ARG
                JCVariableDecl arg = makeParam( syms.intType,varNumName);
                // Construct method.
                JCMethodDecl method = makeMethod(Flags.PUBLIC,
                                                 syms.voidType,
                                                 defs.applyDefaultsPrefixName,
                                                 List.<JCVariableDecl>of(arg),
                                                 stmts);
                // Add to the methods list.
                methods.append(method);
            }

            return methods.toList();
        }

        //
        // This method constructs the initializer for a var map.
        //
        public JCExpression makeInitVarMapExpression(ClassSymbol cSym, LiteralInitVarMap varMap) {
            // Reset diagnostic position to current class.
            resetDiagPos();

             // Build up the argument list for the call.
            ListBuffer<JCExpression> args = ListBuffer.lb();
            // X.VCNT$()
            args.append(callExpression(cSym.type, defs.varCountName));

            // For each var declared in order (to make the switch tags align to the vars.)
            for (VarSymbol vSym : varMap.varList.toList()) {
                // ..., X.VOFF$x, ...

                args.append(select(makeType(cSym.type), attributeOffsetName(vSym)));
            }

            // FXBase.makeInitMap$(X.VCNT$(), X.VOFF$a, ...)
            return callExpression(syms.javafx_FXBaseType, makeInitMap, args);
        }

        //
        // This method constructs a single var map declaration.
        //
        public JCVariableDecl makeInitVarMapDecl(ClassSymbol cSym, LiteralInitVarMap varMap) {
            // Reset diagnostic position to current class.
            resetDiagPos();
            // Fetch name of map.
            Name mapName = varMapName(cSym);
            // static short[] Map$X;
            return makeVar(Flags.STATIC, syms.javafx_ShortArray, mapName, null);
        }

        //
        // This method constructs a single var map initial value.
        //
        public JCStatement makeInitVarMapInit(ClassSymbol cSym, LiteralInitVarMap varMap) {
            // Reset diagnostic position to current class.
            resetDiagPos();
            // Fetch name of map.
            Name mapName = varMapName(cSym);
            // Map$X = FXBase.makeInitMap$(X.VCNT$(), X.VOFF$a, ...);
            return makeExec(m().Assign(id(mapName), makeInitVarMapExpression(cSym, varMap)));
        }

        //
        // This method constructs declarations for var maps used by literal initializers.
        //
        public List<JCTree> makeInitClassMaps(LiteralInitClassMap initClassMap) {
            // Buffer for new vars and methods.
            ListBuffer<JCTree> members = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();

            // For each class initialized in the current class.
            for (ClassSymbol cSym : initClassMap.classMap.keySet()) {
                // Get the var map for the referencing class.
                LiteralInitVarMap varMap = initClassMap.classMap.get(cSym);
                // Add to var to list.
                members.append(makeInitVarMapDecl(cSym, varMap));

                // Fetch name of map.
                Name mapName = varMapName(cSym);
                // Prepare to accumulate statements.
                ListBuffer<JCStatement> stmts = ListBuffer.lb();

                if (analysis.isAnonClass(cSym)) {
                    // Construct and add: return MAP$X;
                    stmts.append(m().Return(id(mapName)));
                } else {
                    // MAP$X == null
                    JCExpression condition = makeNullCheck(id(mapName));
                    // MAP$X = FXBase.makeInitMap$(X.VCNT$, X.VOFF$a, ...)
                    JCExpression assignExpr = m().Assign(id(mapName), makeInitVarMapExpression(cSym, varMap));
                    // Construct and add: return MAP$X == null ? (MAP$X = FXBase.makeInitMap$(X.VCNT$, X.VOFF$a, ...)) : MAP$X;
                    stmts.append(m().Return(m().Conditional(condition, assignExpr, id(mapName))));
                }

                // Construct lazy accessor method.
                JCMethodDecl method = makeMethod(Flags.PUBLIC | Flags.STATIC,
                                                 syms.javafx_ShortArray,
                                                 varGetMapName(cSym),
                                                 List.<JCVariableDecl>nil(),
                                                 stmts);
                // Add method to list.
                members.append(method);
            }

            return members.toList();
        }


        //
        // This method constructs a super call with appropriate arguments.
        //
        private JCStatement makeSuperCall(ClassSymbol cSym, Name name) {
            return makeSuperCall(cSym, name, List.<JCExpression>nil());
        }
        private JCStatement makeSuperCall(ClassSymbol cSym, Name name, List<JCExpression> args) {
            // If this is from a mixin class then we need to use receiver$ otherwise this.
            boolean fromMixinClass = analysis.isMixinClass();
            // If this is to a mixin class then we need to use receiver$ otherwise this.
            boolean toMixinClass = JavafxAnalyzeClass.isMixinClass(cSym);
            // If this class doesn't have a javafx super then punt to FXBase.
            boolean toFXBase = cSym == null;

            // Add in the receiver if necessary.
            if (toMixinClass || toFXBase) {
                // Determine the receiver name.
                Name receiver = fromMixinClass ? defs.receiverName : names._this;
                args.prepend(id(receiver));
            }

            // Determine the selector.
            JCExpression selector;
            if (toMixinClass) {
                selector = makeType(cSym.type, false);
            } else if (toFXBase) {
                selector = makeType(syms.javafx_FXBaseType, false);
            } else {
                selector = id(names._super);
            }

            // Construct the call.

            JCStatement call = callStatement(selector, name, args);

            return call;
        }

        //
        // This method adds the cascading calls to the super classes and mixins.  The topdown flag indicates
        // whether the calls should be made in top down order or bottom up order.  The analysis is used to
        // determine whether the method is static (mixin) or an instance (normal.)  The analysis also
        // indicates whether the inheritance goes back to the FXBase class or whether it inherits from a
        // java class.
        //
        private ListBuffer<JCStatement> addSuperCalls(Name name, ListBuffer<JCStatement> stmts, boolean topdown) {
            // Get the current class's super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
            // Get the immediate mixin classes.
            List<ClassSymbol> immediateMixinClasses = analysis.getImmediateMixins();
            // Construct a list to hold the super calls in the correct order.
            ListBuffer<JCStatement> superCalls = ListBuffer.lb();

            // Order calls appropriately.
            if (topdown) {
                // Call the super.
                if (superClassSym != null) {
                    superCalls.append(makeSuperCall(superClassSym, name));
                } else {
                    // TODO - call FXBase.name();
                }

                // Call the immediate mixins.
                for (ClassSymbol cSym : immediateMixinClasses) {
                    superCalls.append(makeSuperCall(cSym, name));
                }

                stmts = superCalls.appendList(stmts);
            } else {
                // Call the super.
                if (superClassSym != null) {
                    superCalls.prepend(makeSuperCall(superClassSym, name));
                } else {
                    // TODO - call FXBase.name();
                }

                // Call the immediate mixins.
                for (ClassSymbol cSym : immediateMixinClasses) {
                    superCalls.prepend(makeSuperCall(cSym, name));
                }

                stmts = stmts.appendList(superCalls);
            }


            return stmts;
        }

        // Add definitions to class to access the script-level sole instance
        private List<JCTree> makeScriptLevelAccess(JFXClassDeclaration cDecl) {
            ListBuffer<JCTree> mems = ListBuffer.lb();
            final JFXScript module = cDecl.scriptClassModule;
            final JFXClassDeclaration scriptLevel = module.scriptLevelClass;
            if (scriptLevel != null) {
                // sole instance field
                mems.append(makeVar(Flags.PRIVATE | Flags.STATIC, scriptLevel.type, defs.scriptLevelAccessField, null));
                //sole instance lazy creation method
                JCExpression condition = makeNullCheck(id(defs.scriptLevelAccessField));
                JCExpression assignExpr = m().Assign(
                        id(defs.scriptLevelAccessField),
                        m().NewClass(null, null, m().Type(scriptLevel.type), List.<JCExpression>nil(), null));
                List<JCStatement> stmts = List.<JCStatement>of(
                        m().If(condition, makeExec(assignExpr), null),
                        m().Return(id(defs.scriptLevelAccessField)));
                mems.append(makeMethod(Flags.PUBLIC | Flags.STATIC, scriptLevel.type, defs.scriptLevelAccessMethod, null, stmts));
                // If module is runnable, create a run method that redirects to the sole instance version
                if (module.isRunnable) {
                    mems.append(makeMethod(
                            Flags.PUBLIC | Flags.STATIC,
                            syms.objectType,
                            defs.internalRunFunctionName,
                            List.<JCVariableDecl>of(m().Param(defs.arg0Name, types.sequenceType(syms.stringType), null)),
                            List.<JCStatement>of(m().Return(callExpression(
                                callExpression(defs.scriptLevelAccessMethod),
                                defs.internalRunFunctionName,
                                id(defs.arg0Name))))));
                }
            }
            return mems.toList();
        }
    }
}
