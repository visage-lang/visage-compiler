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
import com.sun.tools.mjavac.code.Type.*;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.*;
import com.sun.tools.mjavac.util.*;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.*;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.tree.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

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

    static final boolean SCRIPT_LEVEL_AT_TOP = true;

    private final JavafxToJava toJava;
    private final JavafxClassReader reader;
    private final JavafxOptimizationStatistics optStat;

    private static final int VFLAG_IS_INITIALIZED = 0;
    private static final int VFLAG_DEFAULTS_APPLIED = 1;
    private static final int VFLAG_BITS_PER_VAR = 2;

    private Name outerAccessorFieldName;
    private Name makeInitMap;

    private Name updateInstanceName;
    private Name objName;
    private Name varNumName;
    private Name varLocalNumName;
    private Name varWordName;
    private Name varChangedName;
    private Name functionClassPrefixName;
    private Name phaseName;

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

        updateInstanceName = names.fromString("instance$");
        objName = names.fromString("object$");
        varNumName = names.fromString("varNum$");
        varLocalNumName = names.fromString("varLocalNum$");
        varWordName = names.fromString("varWord$");
        varChangedName = names.fromString("varChanged$");
        phaseName = names.fromString("phase$");

        functionClassPrefixName = names.fromString(JavafxSymtab.functionClassPrefix);

        {
            Name name = names.fromString(runtimePackageNameString + ".AssignToBoundException");
            ClassSymbol sym = reader.jreader.enterClass(name);
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
           List<TranslatedFuncInfo> translatedFuncInfo,
           LiteralInitClassMap initClassMap,
           ListBuffer<JCStatement> translatedInitBlocks, ListBuffer<JCStatement> translatedPostInitBlocks) {

        DiagnosticPosition diagPos = cDecl.pos();
        Type superType = types.superType(cDecl);
        ClassSymbol outerTypeSym = outerTypeSymbol(cDecl); // null unless inner class with outer reference
        boolean isLibrary = toJava.getAttrEnv().toplevel.isLibrary;
        boolean isRunnable = toJava.getAttrEnv().toplevel.isRunnable;

        JavafxAnalyzeClass analysis = new JavafxAnalyzeClass(diagPos,
                cDecl.sym, translatedAttrInfo, translatedOverrideAttrInfo, translatedFuncInfo,
                names, types, reader, typeMorpher);
                
        List<VarInfo> classVarInfos = analysis.classVarInfos();
        List<VarInfo> scriptVarInfos = analysis.scriptVarInfos();
        List<FuncInfo> classFuncInfos = analysis.classFuncInfos();
        List<FuncInfo> scriptFuncInfos = analysis.scriptFuncInfos();
        
        boolean hasStatics = !scriptVarInfos.isEmpty() || !scriptFuncInfos.isEmpty();
        
        int classVarCount = analysis.getClassVarCount();
        int scriptVarCount = analysis.getScriptVarCount();
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

        JavaCodeMaker javaCodeMaker = new JavaCodeMaker(analysis, cDefinitions);
        
        if (!isMixinClass) {
            javaCodeMaker.makeAttributeNumbers(classVarInfos, classVarCount, varMap);
            javaCodeMaker.makeAttributeFields(classVarInfos);
            javaCodeMaker.makeAttributeAccessorMethods(classVarInfos);
            javaCodeMaker.makeApplyDefaultsMethod();
            javaCodeMaker.makeUpdateMethod(analysis.getClassUpdateMap());
            javaCodeMaker.makeGetMethod(classVarInfos, classVarCount);
            javaCodeMaker.makeSetMethod(classVarInfos, classVarCount);
            javaCodeMaker.makeTypeMethod(classVarInfos, classVarCount);

            JCStatement initMap = isAnonClass ? javaCodeMaker.makeInitVarMapInit(varMap) : null;

            if (outerTypeSym == null) {
                javaCodeMaker.makeJavaEntryConstructor();
            } else {
                javaCodeMaker.makeOuterAccessorField(outerTypeSym);
                javaCodeMaker.makeOuterAccessorMethod(outerTypeSym);
            }

            javaCodeMaker.makeFunctionProxyMethods(needDispatch);
            javaCodeMaker.makeFXEntryConstructor(outerTypeSym);
            javaCodeMaker.makeInitMethod(defs.userInitName, translatedInitBlocks, immediateMixinClasses);
            javaCodeMaker.makeInitMethod(defs.postInitName, translatedPostInitBlocks, immediateMixinClasses);
            javaCodeMaker.gatherFunctions(classFuncInfos);

            if (isScriptClass && hasStatics) {
                Name scriptName = cDecl.getName().append(defs.scriptClassSuffixName);
                ListBuffer<JCTree> sDefinitions = ListBuffer.lb();
                 
                if (SCRIPT_LEVEL_AT_TOP) {
                    // With this approach script-level attribute fields and methods, functions, and class maps are top-level statics

                    // script-level into class X
                    javaCodeMaker.makeAttributeFields(scriptVarInfos);
                    javaCodeMaker.makeAttributeAccessorMethods(scriptVarInfos);
                    javaCodeMaker.gatherFunctions(scriptFuncInfos);
                    javaCodeMaker.makeInitClassMaps(initClassMap);

                    // script-level into class X.X$Script
                    javaCodeMaker.setContext(true, sDefinitions);
                    javaCodeMaker.makeAttributeNumbers(scriptVarInfos, scriptVarCount, null);
                    javaCodeMaker.makeUpdateMethod(analysis.getScriptUpdateMap());
                    javaCodeMaker.makeGetMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.makeSetMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.makeTypeMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.makeScriptLevelAccess(scriptName, true, isRunnable);
                    //needed: javaCodeMaker.makeApplyDefaultsMethod();
                    javaCodeMaker.setContext(false, cDefinitions);
                } else {
                    // With this approach all script-level members and support statics are in script-class

                    javaCodeMaker.setContext(true, sDefinitions);
                    javaCodeMaker.makeAttributeNumbers(scriptVarInfos, scriptVarCount, null);
                    javaCodeMaker.makeAttributeFields(scriptVarInfos);
                    javaCodeMaker.makeAttributeAccessorMethods(scriptVarInfos);
                    javaCodeMaker.makeInitClassMaps(initClassMap);
                    javaCodeMaker.makeUpdateMethod(analysis.getScriptUpdateMap());
                    javaCodeMaker.makeGetMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.makeSetMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.makeTypeMethod(scriptVarInfos, scriptVarCount);
                    javaCodeMaker.gatherFunctions(scriptFuncInfos);

                    javaCodeMaker.makeScriptLevelAccess(scriptName, true, isRunnable);
                    javaCodeMaker.setContext(false, cDefinitions);
                }

                javaCodeMaker.makeScriptLevelAccess(scriptName, false, isRunnable);
                javaCodeMaker.makeInitStaticAttributesBlock(scriptName, scriptVarInfos, initMap);
                javaCodeMaker.makeScript(scriptName, sDefinitions.toList());
            } else {
                javaCodeMaker.makeInitStaticAttributesBlock(null, null, initMap);
            }

            if (!hasFxSuper) {
                // Has a non-JavaFX super, so we can't use FXBase, therefore we need
                // to clone the necessary vars and methods.
                // This code must be after all methods have been added to cDefinitions,

                // A set of methods to exclude from cloning.
                HashSet<String> excludes = new HashSet<String>();

                // Exclude any methods generated by the init builder.
                for (JCTree member : cDefinitions) {
                    if (member.getTag() == JCTree.METHODDEF) {
                        JCMethodDecl jcmeth = (JCMethodDecl)member;
                        excludes.add(jcMethodDeclStr(jcmeth));
                     }
                }

                // Clone what is needed from FXBase/FXObject.
                javaCodeMaker.cloneFXBase(excludes);
            }

        } else {
            // Mixin class

            javaCodeMaker.makeAttributeFields(classVarInfos);

            // Static(script) vars are exposed in class
            javaCodeMaker.makeAttributeFields(scriptVarInfos);
            javaCodeMaker.makeAttributeAccessorMethods(scriptVarInfos);
            javaCodeMaker.makeUpdateMethod(analysis.getClassUpdateMap());
            javaCodeMaker.makeInitStaticAttributesBlock(null, null, null);

            javaCodeMaker.makeMixinAccessorMethods(classVarInfos);
        
            javaCodeMaker.makeInitMethod(defs.userInitName, translatedInitBlocks, immediateMixinClasses);
            javaCodeMaker.makeInitMethod(defs.postInitName, translatedPostInitBlocks, immediateMixinClasses);
            javaCodeMaker.gatherFunctions(classFuncInfos);
            
            javaCodeMaker.setContext(false, iDefinitions);
            javaCodeMaker.makeMemberVariableAccessorInterfaceMethods();
            javaCodeMaker.makeFunctionInterfaceMethods();
            javaCodeMaker.makeOuterAccessorInterfaceMembers();
            javaCodeMaker.setContext(false, cDefinitions);
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

    //
    // Build a string that can be compared against MethodSymbol.toString()
    //
    private static String jcMethodDeclStr(JCMethodDecl meth) {
        String str = meth.name.toString() + "(";
        boolean needsComma = false;
        for (JCVariableDecl varDecl : meth.getParameters()) {
            if (needsComma) str += ",";
            str += varDecl.vartype.toString();
            needsComma = true;
        }
        str += ")";
        return str;
    }

    private List<JCExpression> makeImplementingInterfaces(DiagnosticPosition diagPos,
            JFXClassDeclaration cDecl,
            List<ClassSymbol> baseInterfaces) {
        ListBuffer<JCExpression> implementing = ListBuffer.lb();

        if (cDecl.isMixinClass()) {
            implementing.append(makeIdentifier(diagPos, fxObjectString));
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

    // Add the methods and field for accessing the outer members. Also add a constructor with an extra parameter
    // to handle the instantiation of the classes that access outer members
    private ClassSymbol outerTypeSymbol(JFXClassDeclaration cdecl) {
        if (cdecl.sym != null && toJava.getHasOuters().contains(cdecl.sym)) {
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
        private boolean isScript;
        private ListBuffer<JCTree> definitions = null;

        JavaCodeMaker(JavafxAnalyzeClass analysis, ListBuffer<JCTree> definitions) {
            super(analysis.getCurrentClassPos());
            this.analysis = analysis;
            this.definitions = definitions;
        }
        
        //
        // Method for changing the current definition list.
        //
        public void setContext(boolean isScript, ListBuffer<JCTree> definitions) {
            this.isScript = isScript;
            this.definitions = definitions;
        }
        
        //
        // Methods for adding a new definitions.
        //
        private void addDefinition(JCTree member) {
            if (member != null) {
                definitions.append(member);
            }
        }
        private void addDefinitions(List<JCTree> members) {
            if (members != null) {
                definitions.appendList(members);
            }
        }

        //
        // Methods for managing the current diagnostic position.
        //
        private void setDiagPos(VarInfo ai) { setDiagPos(ai.pos()); }
        private void resetDiagPos() { setDiagPos(analysis.getCurrentClassPos()); }
        
        //
        // Returns the current class decl.
        //
        public JFXClassDeclaration getCurrentClassDecl() { return analysis.getCurrentClassDecl(); }

        //
        // Returns the current class symbol.
        //
        public ClassSymbol getCurrentClassSymbol() {
            return analysis.getCurrentClassSymbol();
        }

        //
        // Returns true if the sym is the current class symbol.
        //
        public boolean isCurrentClassSymbol(Symbol sym) {
            return analysis.isCurrentClassSymbol(sym);
        }

        //
        // Returns true if the current class is a mixin.
        //
        public boolean isMixinClass() {
            return analysis.isMixinClass();
        }

        //
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
        public void makeAttributeFields(List<? extends VarInfo> attrInfos) {
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
                    if (isCurrentClassSymbol(varSym.owner)) {
                        List<JCAnnotation> annotations = List.<JCAnnotation>of(m().Annotation(
                                makeIdentifier(diagPos, JavafxSymtab.sourceNameAnnotationClassNameString),
                                List.<JCExpression>of(m().Literal(varSym.name.toString()))));
                        mods = addAccessAnnotationModifiers(diagPos, varSym.flags(), mods, annotations);
                    } else {
                        mods = addInheritedAnnotationModifiers(diagPos, varSym.flags(), mods);
                    }

                    // Construct the value field
                    addDefinition(makeVariableField(ai, mods, ai.getRealType(), attributeValueName(varSym),
                                                    needsDefaultValue(ai.getVMI()) ? makeDefaultValue(diagPos, ai.getVMI()) : null));
                }
            }
        }

        //
        // This method constructs modifiers for getters/setters and proxies.
        //
        private JCModifiers proxyModifiers(VarInfo ai, boolean isAbstract) {
            // Copy old flags from VarInfo.
            long oldFlags = ai.getFlags();
            
            // Determine new flags.
            long newFlags = (oldFlags & Flags.STATIC) | Flags.PUBLIC;
            if (isAbstract) {
                newFlags|= Flags.ABSTRACT;
            } else if (isMixinClass()) {
                newFlags|= Flags.STATIC;
            }

            // Set up basic flags.
            JCModifiers mods = m().Modifiers(newFlags);

            // If var is in current class.
            if (isCurrentClassSymbol(ai.getSymbol().owner)) {
                // Use local access modifiers.
                mods = addAccessAnnotationModifiers(ai.pos(), oldFlags, mods);
            } else {
                // Use inherited modifiers.
                mods = addInheritedAnnotationModifiers(ai.pos(), oldFlags, mods);
            }

            return mods;
        }

        //
        // These methods return an expression for testing/setting/clearing a var flag.
        //
        JCExpression makeFlagExpression(VarSymbol varSym, Name action, Name flag) {
            return call(getReceiver(varSym), action, makeVarOffset(varSym, /*used in current context*/null), id(flag));
        }
        JCExpression makeFlagExpression(VarSymbol varSym, Name action, Name flag, JCExpression phase) {
            return call(getReceiver(varSym), action, makeVarOffset(varSym, /*used in current context*/null), makeBinary(JCTree.PLUS, id(flag), phase));
        }

        //
        // These methods returns a statement for setting/clearing a var flag.
        //
        JCStatement makeFlagStatement(VarSymbol varSym, Name action, Name flag) {
            return makeExec(makeFlagExpression(varSym, action, flag));
        }
        JCStatement makeFlagStatement(VarSymbol varSym, Name action, Name flag, JCExpression phase) {
            return makeExec(makeFlagExpression(varSym, action, flag, phase));
        }

        //
        // Return a receiver$ ident if is a mixin otherwise null.
        //
        private JCExpression getReceiver() {
            if (isMixinClass()) {
                return id(defs.receiverName);
            }
            
            return null;
        }
        private JCExpression getReceiver(VarSymbol varSym) {
            if (varSym.isStatic()) {
                return isScript ? null : call(defs.scriptLevelAccessMethod);
            }
            
            return getReceiver();
        }
        private JCExpression getReceiver(VarInfo varInfo) {
            return getReceiver(varInfo.getSymbol());
        }
        
        //
        // This method gathers all the translated functions in funcInfos.
        //
        public void gatherFunctions(List<FuncInfo> funcInfos) {
            for (FuncInfo func : funcInfos) {
                if (func instanceof TranslatedFuncInfo) {
                    addDefinitions(((TranslatedFuncInfo)func).jcFunction());
                }
            }
        }
        
        //
        // This class is designed to reduce the repetitiveness of constructing methods.
        //
        public class MethodBuilder {
            // Name of method to generate.
            protected Name methodName;
            // Method return type.
            protected JCExpression returnType;
            // True if the return type is void.
            protected boolean isVoidReturnType;
            
            // Grab the super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
            // Grab the mixin classes.
            public List<ClassSymbol> immediateMixinClasses = analysis.getImmediateMixins();
            
            // Stack of nested statements.
            protected Stack<ListBuffer<JCStatement>> stmtsStack = new Stack<ListBuffer<JCStatement>>();
            // Current set of statements.
            protected ListBuffer<JCStatement> stmts = ListBuffer.lb();
            
            // List of parameter types.
            ListBuffer<Type> paramTypes = ListBuffer.lb();
            // List of parameter names.
            ListBuffer<Name> paramNames = ListBuffer.lb();
            
            MethodBuilder(Name methodName, Type returnType) {
                this.methodName = methodName;
                this.returnType = makeType(returnType);
                this.isVoidReturnType = returnType == syms.voidType;
            }
            
            MethodBuilder(Name methodName, JCExpression returnType) {
                this.methodName = methodName;
                this.returnType = returnType;
                this.isVoidReturnType = false;
            }
            
            // This method saves the current list of statements and starts a new one.
            public void beginBlock() {
                stmtsStack.push(stmts);
                stmts = ListBuffer.lb();
            }
            
            // This method restores the previous list of statements and returns the current
            // list of statements in a block.
            public JCBlock endBlock() {
                return m().Block(0L, endBlockAsStmts());
            }
            
            // This method restores the previous list of statements and returns the current
            // list of statements.
            public List<JCStatement> endBlockAsStmts() {
                assert !stmtsStack.empty() : "MethodBuilder: mismatched blocks";
                List<JCStatement> result = stmts.toList();
                stmts = stmtsStack.pop();
                return result;
            }
            
            // This method adds a new statement to the current lists of statements.
            public void addStmt(JCStatement stmt) {
                if (stmt != null) {
                    stmts.append(stmt);
                }
            }
            
            // This method adds several statements to the current lists of statements.
            public void addStmts(List<JCStatement> list) {
                stmts.appendList(list);
            }
            public void addStmts(ListBuffer<JCStatement> list) {
                stmts.appendList(list.toList());
            }
            
            // This method adds a new parameter type and name to the current method.
            public void addParam(Type type, Name name) {
                paramTypes.append(type);
                paramNames.append(name);
            }
            
            // This method returns all the parameters for the current method as a
            // list of JCVariableDecl.
            protected List<JCVariableDecl> paramList() {
                return paramList(false);
            }
            protected List<JCVariableDecl> paramList(boolean isAbstract) {
                Iterator<Type> typeIter = paramTypes.iterator();
                Iterator<Name> nameIter = paramNames.iterator();
                ListBuffer<JCVariableDecl> params = ListBuffer.lb();
                
                if (isMixinClass() && !isAbstract) {
                    params.append(makeReceiverParam(getCurrentClassDecl()));
                }
     
                while (typeIter.hasNext() && nameIter.hasNext()) {
                    params.append(makeParam(typeIter.next(), nameIter.next()));
                }
                
                return params.toList();
            }
            
            
            // This method returns all the parameters for the current method as a
            // list of JCExpression.
            protected List<JCExpression> argList() {
                ListBuffer<JCExpression> args = ListBuffer.lb();
                
                for (Name name : paramNames) {
                    args.append(id(name));
                }
                
                return args.toList();
            }

            // This method generates a call to the mixin symbol.
            public void callMixin(ClassSymbol mixin) {
                JCExpression receiver = id(isMixinClass() ? defs.receiverName : names._this);
                List<JCExpression> mixinArgs = List.<JCExpression>of(receiver).appendList(argList());
                JCExpression selector = makeType(mixin.type, false);
 
                if (isVoidReturnType) {
                    addStmt(callStmt(selector, methodName, mixinArgs));
                } else {
                    addStmt(m().Return(call(selector, methodName, mixinArgs)));
                }
            }

            // This method generates all the calls for immediate mixins.
            public void callMixins() {
                for (ClassSymbol mixin : immediateMixinClasses) {
                    callMixin(mixin);
                }
            }
            
            // This method generates the call for the super class.
            public void callSuper() {
                if (superClassSym != null && !analysis.isMixinClass()) {
                    List<JCExpression> superArgs = argList();
                    
                    if (isVoidReturnType) {
                        addStmt(callStmt(id(names._super), methodName, superArgs));
                    } else {
                        addStmt(m().Return(call(id(names._super), methodName, superArgs)));
                    }
                 }
            }
            
            // Driver method to construct the current method.
            public void build() {
                // Initialize for method.
                initialize();
                
                // Generate the code.
                generate();
                
                // Record method.
                optStat.recordProxyMethod();

                // Construct method.
                addDefinition(makeMethod(isMixinClass() ? (Flags.STATIC | Flags.PUBLIC) : Flags.PUBLIC,
                                         returnType,
                                         methodName,
                                         paramList(),
                                         stmts.toList()));
            }

            // This method generates the statements for the method.
            public void generate() {
                // Reset diagnostic position to current class.
                resetDiagPos();
                // Emit prologue statements.
                prologue();
                
                // Reset diagnostic position to current class.
                resetDiagPos();
                // Emit method body.
                body();
                
                // Reset diagnostic position to current class.
                resetDiagPos();
                // Emit epilog statements.
                epilogue();
            }

            
            // This method contains any code to initialize the builder.
            public void initialize() {
            }
            
            // This method generates any code needed before the body of the method.
            public void prologue() {
            }
             
            // This method generates any code needed after the body of the method.
            public void epilogue() {
            }
            
            // This method generates the body of the method.
            public void body() {
                statements();
            }
            
            // This method generates specialized code for the body.
            public void statements() {
            }            
        }
        
        //
        // This class is designed to generate a method whose body is a static
        // utility.
        //
        public class StaticMethodBuilder extends MethodBuilder {
            StaticMethodBuilder(Name methodName, Type returnType) {
                super(methodName, returnType);
            }
            
            // Driver method to construct the current method.
            @Override
            public void build() {
                // Initialize for method.
                initialize();
                
                // Generate the code.
                generate();
                
                // Record method.
                optStat.recordProxyMethod();

                // Construct method.
                addDefinition(makeMethod(Flags.STATIC | Flags.PUBLIC,
                                         returnType,
                                         methodName,
                                         paramList(),
                                         stmts.toList()));
            }
        }
      
        //
        // This class is designed to generate a method whose body is a var
        // accessor.
        //
        public class VarAccessorMethodBuilder extends MethodBuilder {
            // Current var info.
            protected VarInfo varInfo;
            // Whether the accessor needs a body or not.
            protected boolean needsBody;
            // Symbol used on the method.
            protected VarSymbol varSym;
            // Symbol used when accessing the variable.
            protected VarSymbol proxyVarSym;
            // Name of var field.
            protected Name varName;
            // Real type of the var.
            protected Type type;

            
            VarAccessorMethodBuilder(Name methodName, Type returnType, VarInfo varInfo, boolean needsBody) {
                super(methodName, returnType);
                this.varInfo = varInfo;
                this.needsBody = needsBody;
                this.varSym = varInfo.getSymbol();
                this.proxyVarSym = varInfo.proxyVarSym();
                this.varName = attributeValueName(proxyVarSym);
                this.type = varInfo.getRealType();
            }
                
            // Driver method to construct the current method.
            @Override
            public void build() {
                // Initialize for method.
                initialize();
                
                // Generate the code.
                if (needsBody) generate();
                
                // Record method.
                optStat.recordProxyMethod();

                // Construct method.
                addDefinition(makeMethod(proxyModifiers(varInfo, !needsBody),
                                         returnType,
                                         methodName,
                                         paramList(!needsBody),
                                         needsBody ? stmts.toList() : null));
            }
        }
        
        //
        // This class is designed to generate a method whose body is a mixin var
        // accessor.
        //
        public class MixinMethodBuilder extends MethodBuilder {
            // Current var info.
            protected VarInfo varInfo;
            // Symbol used on the method.
            protected VarSymbol varSym;
            // Symbol used when accessing the variable.
            protected VarSymbol proxyVarSym;
            // Name of var field.
            protected Name varName;
            // Real type of the var.
            protected Type type;
            
            MixinMethodBuilder(Name methodName, Type returnType, VarInfo varInfo) {
                super(methodName, returnType);
                this.varInfo = varInfo;
                this.varSym = varInfo.getSymbol();
                this.proxyVarSym = varInfo.proxyVarSym();
                this.varName = attributeValueName(proxyVarSym);
                this.type = varInfo.getRealType();
            }
            
            // Driver method to construct the current method.
            @Override
            public void build() {
                // Build only if a member of this mixin.
                if (constrain()) {
                    super.build();
                }
            }
            
            // Additional constraint to force the method.
            public boolean constrain() {
                return false;
            }
        }
        
        //
        // This class is designed to generate a method whose body is switched
        // on var offsets.
        //
        public class VarCaseMethodBuilder extends MethodBuilder {
            protected List<VarInfo> attrInfos =  List.<VarInfo>nil();
            protected int varCount = 0;
            protected VarInfo varInfo;
        
            VarCaseMethodBuilder(Name methodName, Type returnType, List<VarInfo> attrInfos, int varCount) {
                super(methodName, returnType);
                this.attrInfos = attrInfos;
                this.varCount = varCount;
                addParam(syms.intType, varNumName);
            }
            VarCaseMethodBuilder(Name methodName, JCExpression returnType, List<VarInfo> attrInfos, int varCount) {
                super(methodName, returnType);
                this.attrInfos = attrInfos;
                this.varCount = varCount;
                addParam(syms.intType, varNumName);
            }
            
            // Driver method to construct the current method.
            @Override
            public void build() {
                // Generate the code.
                // Initialize for method.
                initialize();
                
                // Generate the body.
                generate();
                
                if (stmts.nonEmpty() || superClassSym == null) {
                    // Record method.
                    optStat.recordProxyMethod();
                    
                    // Construct method.
                    addDefinition(makeMethod(Flags.PUBLIC, returnType, methodName, paramList(), stmts.toList()));
                }
            }
            
            // Specialized body the handles offset cases.
            @Override
            public void body() {
                if (varCount != 0) {
                    // Prepare to accumulate cases.
                    ListBuffer<JCCase> cases = ListBuffer.lb();
                    // Iterate thru each var.
                    for (VarInfo ai : attrInfos) {
                        // Set to the var position.
                        setDiagPos(ai.pos());
                         
                        // Constrain the var.
                        if (ai.needsCloning() && !ai.isOverride()) {
                            // Construct the case.
                            beginBlock();
                            
                            // Generate statements.
                            varInfo = ai;
                            statements();
    
                            // case tag number
                            JCExpression tag = makeInt(ai.getEnumeration() - varCount);
    
                            // Add the case, something like:
                            // case i: return get$var();
                            cases.append(m().Case(tag, endBlockAsStmts()));
                        }
                    }
                    
                    // Add statement if there were some cases.
                    if (cases.nonEmpty()) {
                        // varNum - VCNT$
                        JCExpression tagExpr = makeBinary(JCTree.MINUS, id(varNumName), id(defs.varCountName));
                        // Construct and add: switch(varNum - VCNT$) { ... }
                        addStmt(m().Switch(tagExpr, cases.toList()));
                    }
                    
                    // Call the super version.
                    callSuper();
                }
            }
        }

        //
        // This method constructs the getter method for the specified attribute.
        //
        private void makeGetterAccessorMethod(VarInfo varInfo, boolean needsBody) {
            VarAccessorMethodBuilder vamb = new VarAccessorMethodBuilder(attributeGetterName(varInfo.getSymbol()),
                                                                         varInfo.getRealType(),
                                                                         varInfo, needsBody) {
                @Override
                public void statements() {
                    if (varInfo.hasBoundDefinition() || varInfo.isMixinVar()) {
                        // !isValidValue$(VOFF$var)
                        JCExpression condition = makeNot(makeFlagExpression(proxyVarSym, defs.varFlagActionTest, defs.varFlagValid, id(defs.vflgInvalPhaseName)));
                        
                        // Prepare to accumulate body of if.
                        beginBlock();
                        
                        // Set to new value.
                        if (varInfo.isMixinVar()) {
                            // Mixin.evaluate$var(this);
                            addStmt(makeSuperCall((ClassSymbol)varSym.owner, attributeEvaluateName(varSym), id(names._this)));
                            // setIsValidValue(VOFF$var);
                            addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgInvalPhaseName)));
                            addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgTriggerPhaseName)));
                        } else {
                            assert varInfo.boundInit() != null : "Oops! No boundInit.  varInfo = " + varInfo + ", preface = " + varInfo.boundPreface();
    
                            // set$var(init/bound expression)
                            addStmts(varInfo.boundPreface());
                            addStmt(callStmt(getReceiver(), attributeBeName(varSym), varInfo.boundInit()));
                        }
                      
                        // if (!isValidValue$(VOFF$var)) { set$var(init/bound expression); }
                        addStmt(m().If(condition, endBlock(), null));
                    } 
    
                    // Construct and add: return $var;
                    addStmt(m().Return(id(varName)));
                }
            };

            vamb.build();
        }

        //
        // This method constructs the setter method for the specified attribute.
        //
        private void makeSetterAccessorMethod(VarInfo varInfo, boolean needsBody) {
            VarAccessorMethodBuilder vamb = new VarAccessorMethodBuilder(attributeSetterName(varInfo.getSymbol()),
                                                                         varInfo.getRealType(),
                                                                         varInfo, needsBody) {
                @Override
                public void initialize() {
                    addParam(type, defs.attributeNewValueName);
                }

                @Override
                public void statements() {
                    if (varInfo.hasBoundDefinition()) {
                        if (varInfo.hasBiDiBoundDefinition()) {
                            // Preface to setter.
                            addStmts(varInfo.boundInvSetterPreface());
                            // set$var(set$invvar(value))
                            addStmt(callStmt(getReceiver(), attributeBeName(varSym), varInfo.boundInvSetter()));
                            // return $var;
                            addStmt(m().Return(id(varName)));
                        } else {
                            addStmt(makeThrow(assignBindExceptionType));
                        }
                    } else if (varInfo.isDef()) {
                        addStmt(makeThrow(assignDefExceptionType));
                    } else {
                        // set$var(value)
                        addStmt(callStmt(getReceiver(), attributeBeName(varSym), id(defs.attributeNewValueName)));
                        // return $var;
                        addStmt(m().Return(id(varName)));
                    }
                }
            };

            vamb.build();
        }

        //
        // This method constructs the be method for the specified attribute.
        //
        private void makeBeAccessorMethod(VarInfo varInfo, boolean needsBody) {
            VarAccessorMethodBuilder vamb = new VarAccessorMethodBuilder(attributeBeName(varInfo.getSymbol()),
                                                                         varInfo.getRealType(),
                                                                         varInfo, needsBody) {
                @Override
                public void initialize() {
                    addParam(type, defs.attributeNewValueName);
                }
                
                @Override
                public void statements() {
                    // T varOldValue$ = $var;
                    addStmt(makeVar(Flags.FINAL, type, defs.attributeOldValueName, id(varName)));
    
                    // Prepare to accumulate then statements.
                    beginBlock();
    
                    // invalidate$(VFLGS$INVAL_PHASE)
                    addStmt(callStmt(getReceiver(), attributeInvalidateName(varSym), id(defs.vflgInvalPhaseName)));
    
                    // $var = value
                    addStmt(makeExec(m().Assign(id(varName), id(defs.attributeNewValueName))));
    
                    // invalidate$(VFLGS$TRIGGER_PHASE)
                    addStmt(callStmt(getReceiver(), attributeInvalidateName(varSym), id(defs.vflgTriggerPhaseName)));
    
                    // setIsValidValue(VOFF$var);
                    addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgInvalPhaseName)));
                    addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgTriggerPhaseName)));
    
                    // onReplace$(varOldValue$, varNewValue$)
                    addStmt(callStmt(getReceiver(), attributeOnReplaceName(varSym), id(defs.attributeOldValueName), id(defs.attributeNewValueName)));
    
                    // varOldValue$ != varNewValue$
                    // or !varOldValue$.isEquals(varNewValue$) test for Objects and Sequences
                    JCExpression testExpr = type.isPrimitive() ?
                        makeNotEqual(id(defs.attributeOldValueName), id(defs.attributeNewValueName))
                      : makeNot(runtime(diagPos, defs.Util_isEqual, List.<JCExpression>of(id(defs.attributeOldValueName), id(defs.attributeNewValueName))));
                    
                    // End of then block.
                    JCBlock thenBlock = endBlock();
    
                    // Prepare to accumulate else statements.
                    beginBlock();
    
                    // setIsValidValue(VOFF$var);
                    addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgInvalPhaseName)));
                    addStmt(makeFlagStatement(proxyVarSym, defs.varFlagActionSet, defs.varFlagValid, id(defs.vflgTriggerPhaseName)));
                    
                    // End of else block.
                    JCBlock elseBlock = endBlock();
    
                    // if (varOldValue$ != varNewValue$) { handle change }
                    addStmt(m().If(testExpr, thenBlock, elseBlock));
   
                    // return $var;
                    addStmt(m().Return(id(varName)));
                }
            };

            vamb.build();
        }

        //
        // Determine if this override needs an invalidate method
        // Must be in sync with makeInvalidateAccessorMethod
        //
        private boolean needOverrideInvalidateAccessorMethod(VarInfo varInfo) {
            if (varInfo.hasBoundDefinition()) {
                return false;
            }
            if (varInfo.isMixinVar() || varInfo.onReplace() != null) {
                // based on makeInvalidateAccessorMethod
                return true;
            } else {
                if (varInfo instanceof TranslatedVarInfoBase) {
                    return ((TranslatedVarInfoBase) varInfo).boundBinders().size() != 0;
                } else {
                    return false;
                }
            }
        }

        //
        // This method constructs the invalidate method for the specified attribute.
        //
        private void makeInvalidateAccessorMethod(VarInfo varInfo, boolean needsBody) {
            VarAccessorMethodBuilder vamb = new VarAccessorMethodBuilder(attributeInvalidateName(varInfo.getSymbol()),
                                                                         syms.voidType,
                                                                         varInfo, needsBody) {
                @Override
                public void initialize() {
                    addParam(syms.intType, phaseName);
                }
                                                                         
                @Override
                public void statements() {
                    // Debug tracing
                    addStmts(makeDebugTrace(attributeInvalidateName(varSym) + " called"));

                    // Prepare to accumulate if statements.
                    beginBlock();
    
                    // Debug tracing
                    addStmts(makeDebugTrace(attributeInvalidateName(varSym) + " entered"));
    
                    if (varInfo.isOverride()) {
                        // Call super first.
                        callSuper();
                    }

                    // Mixin invalidate$
                    if (!isMixinClass() && varInfo.isMixinVar()) {
                        // Mixin.invalidate$var(this, phase$);
                        callMixin((ClassSymbol)varSym.owner);
                    }
                    
                    // Add on-invalidate trigger if any
                    if (varInfo.onInvalidate() != null) {
                        addStmt(varInfo.onInvalidateAsInline());
                    }
                    
                    for (VarSymbol otherVarSym : varInfo.boundBinders()) {
                        // invalidate$var(phase$);
                        addStmt(callStmt(getReceiver(), attributeInvalidateName(otherVarSym), id(phaseName)));
                    }
                    
                    // Invalidate back to inverse.
                    if (varInfo.hasBoundDefinition() && varInfo.hasBiDiBoundDefinition()) {
                        for (VarSymbol bindeeSym : varInfo.boundBindees()) {
                            addStmt(callStmt(getReceiver(), attributeInvalidateName(bindeeSym), id(phaseName)));
                            
                            // rest are duplicates.
                            break;
                        }
                        
                        for (DependentPair pair : varInfo.boundBoundSelects()) {
                            VarSymbol instanceSymbol = (VarSymbol)pair.instanceSym;
                            VarSymbol referenceSymbol = (VarSymbol)pair.referencedSym;
                            JCVariableDecl selector = makeTmpVar(instanceSymbol.type, call(getReceiver(), attributeGetterName(instanceSymbol)));
                            addStmt(selector);
                            beginBlock();
                            addStmt(callStmt(id(selector), attributeInvalidateName(referenceSymbol), id(phaseName)));
                            JCExpression conditionExpr = makeBinary(JCTree.NE, id(selector), makeNull());
                            addStmt(m().If(conditionExpr, endBlock(), null));
        
                            // rest are duplicates.
                            break;
                        }
                    }
                    
                    boolean isSuperVarInfo = varInfo instanceof SuperClassVarInfo;

                    if (isSuperVarInfo) {
                        callSuper();
                    } else if (!varInfo.isOverride()) {
                        // notifyDependents(VOFF$var, phase$);
                        addStmt(callStmt(getReceiver(varInfo), defs.attributeNotifyDependentsName, makeVarOffset(proxyVarSym, null), id(phaseName)));
                    } 
                    
                    if (varInfo.onReplace() != null) {
                        // Begin the get$ block.
                        beginBlock();
                        
                        // Call the get$var to force evaluation.
                        addStmt(callStmt(getReceiver(), attributeGetterName(proxyVarSym)));
                        
                         // phase$ == VFLGS$TRIGGER_PHASE
                        JCExpression ifTriggerPhase = makeBinary(JCTree.EQ, id(phaseName), id(defs.vflgTriggerPhaseName));
                       
                        // if (phase$ == VFLGS$TRIGGER_PHASE) { get$var(); }
                        addStmt(m().If(ifTriggerPhase, endBlock(), null));
                    }
                    
                    // isValid
                    Name action = isSuperVarInfo || varInfo.isOverride() ? defs.varFlagActionTest : defs.varFlagActionClear;
                    JCExpression ifValidTest = makeFlagExpression(proxyVarSym, action, defs.varFlagValid, id(phaseName));
                    
                    // if (!isValidValue$(VOFF$var)) { ... invalidate  code ... }
                    addStmt(m().If(ifValidTest, endBlock(), null));
                }
            };

            vamb.build();
        }

        //
        // This method constructs the onreplace$ method for the specified attribute.
        //
        private void makeOnReplaceAccessorMethod(VarInfo varInfo, boolean needsBody) {
            VarAccessorMethodBuilder vamb = new VarAccessorMethodBuilder(attributeOnReplaceName(varInfo.getSymbol()),
                                                                         syms.voidType,
                                                                         varInfo, needsBody) {
                Name oldValueName = defs.attributeOldValueName;
                Name newValueName = defs.attributeNewValueName;
                
                @Override
                public void initialize() {
                    if (needsBody) {
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
                                // Change the onReplace arg name. 
                                newValueName = newVar.getName();
                            }
                        }
                    }
                    
                    addParam(type, oldValueName);
                    addParam(type, newValueName);
                }
                
                @Override
                public void statements() {
                    // Forward to the mixin.
                    // Call super first.
                    if (varInfo.isOverride()) {
                        callSuper();
                    }

                    // Fetch the on replace statement or null.
                    JCStatement onReplace = varInfo.onReplaceAsInline();
    
                    if (!isMixinClass() && varInfo.isMixinVar()) {
                        // Mixin.onReplace$var(this, oldValue, newValue);
                        callMixin((ClassSymbol)varSym.owner);
                        
                        if (onReplace != null) {
                            addStmt(makeVar(Flags.FINAL, id(interfaceName(getCurrentClassDecl())), defs.receiverName, id(names._this)));
                        }
                    }
                    
                    // Need to capture init state if has trigger.
                    if (onReplace != null) {
                        // Insert the trigger.
                        addStmt(onReplace);
                    }
                }
            };

            vamb.build();
        }
        
        //
        // This method constructs a mixin applyDefault method.
        //
        private void makeMixinApplyDefaultsMethod(VarInfo varInfo) {
            MixinMethodBuilder mmb = new MixinMethodBuilder(attributeApplyDefaultsName(varInfo.getSymbol()), syms.voidType, varInfo) {
                @Override
                public void statements() {
                    // Get body of applyDefaults$.
                    addStmt(makeApplyDefaultsStatement(varInfo, true));
                }
                
                @Override
                public boolean constrain() {
                    return isCurrentClassSymbol(varSym.owner) || varInfo.getDefaultInitStatement() != null;
                }
            };
             
            mmb.build();
        }

        //
        // This method constructs a mixin evaluate method.
        //
        private void makeMixinEvaluateAccessorMethod(VarInfo varInfo) {
            MixinMethodBuilder mmb = new MixinMethodBuilder(attributeEvaluateName(varInfo.getSymbol()), syms.voidType, varInfo) {
                @Override
                public void statements() {
                    if (varInfo.hasBoundDefinition()) {
                        // set$var(init/bound expression)
                        addStmts(varInfo.boundPreface());
                        addStmt(callStmt(getReceiver(), attributeBeName(varSym), varInfo.boundInit()));
                    } 
                }
                
                @Override
                public boolean constrain() {
                    return isCurrentClassSymbol(varSym.owner) || varInfo.hasBoundDefinition();
                }
            };
             
            mmb.build();
        }

        //
        // This method constructs a mixin invalidate method.
        //
        private void makeMixinInvalidateAccessorMethod(VarInfo varInfo) {
            MixinMethodBuilder mmb = new MixinMethodBuilder(attributeInvalidateName(varInfo.getSymbol()), syms.voidType, varInfo) {
                @Override
                public void initialize() {
                    addParam(syms.intType, phaseName);
                }
                                                                         
                @Override
                public void statements() {
                    // Call super first.
                    if (varInfo.isOverride()) {
                       callSuper();
                    }
                    
                    // Invalidate other mixin vars.
                    for (VarSymbol otherVarSym : ((TranslatedVarInfoBase)varInfo).boundBinders()) {
                         addStmt(callStmt(getReceiver(), attributeInvalidateName(otherVarSym), id(phaseName)));
                    }
      
                     // Add on-invalidate trigger if any
                    if (varInfo.onInvalidate() != null) {
                        addStmt(varInfo.onInvalidateAsInline());
                    }
                }
                
                @Override
                public boolean constrain() {
                    return varInfo instanceof TranslatedVarInfoBase;
                }
            };
             
            mmb.build();
        
         }

        //
        // This method constructs the accessor methods for an attribute.
        //
        public void makeAnAttributeAccessorMethods(VarInfo ai, boolean needsBody) {
            setDiagPos(ai.pos());

            if (ai.useAccessors()) {
                if (!ai.isOverride()) {
                    makeGetterAccessorMethod(ai, needsBody);
                    makeSetterAccessorMethod(ai, needsBody);
                    makeBeAccessorMethod(ai, needsBody);
                    makeInvalidateAccessorMethod(ai, needsBody);
                    makeOnReplaceAccessorMethod(ai, needsBody);
                } else if (needsBody) {
                    if (ai.hasInitializer()) {
                        // Bound or not, we need getter & setter on override since we
                        // may be switching between bound and non-bound or visa versa
                        makeGetterAccessorMethod(ai, needsBody);
                        makeSetterAccessorMethod(ai, needsBody);
                    }
                    if (needOverrideInvalidateAccessorMethod(ai)) {
                        makeInvalidateAccessorMethod(ai, needsBody);
                    }
                    if (ai.onReplace() != null || ai.isMixinVar()) {
                        makeOnReplaceAccessorMethod(ai, needsBody);
                    }
                }
            }
        }
        
        //
        // This method constructs the accessor methods for each attribute.
        //
        public void makeAttributeAccessorMethods(List<VarInfo> attrInfos) {
            for (VarInfo ai : attrInfos) {
                // Only create accessors for declared and proxied vars.
                if (ai.needsCloning()) {
                    makeAnAttributeAccessorMethods(ai, true);
                } else {
                    // If a super has binders we need to emit an overriding invalidate$.
                    if (ai.boundBinders().size() != 0) {
                        makeInvalidateAccessorMethod(ai, true);
                    }
                }
            }
        }

        //
        // This method constructs the abstract interfaces for the accessors in
        // a mixin class.
        //
        public void makeMemberVariableAccessorInterfaceMethods() {
            // TranslatedVarInfo for the current class.
            List<TranslatedVarInfo> translatedAttrInfo = analysis.getTranslatedAttrInfo();

            // Only for vars within the class.
            for (VarInfo ai : translatedAttrInfo) {
                if (!ai.isStatic()) {
                    makeAnAttributeAccessorMethods(ai, false);
                }
            }
        }

        //
        // This method constructs mixin accessor methods.
        //
        private void makeMixinAccessorMethods(List<? extends VarInfo> attrInfos) {
            for (VarInfo ai : attrInfos) {
                // Set diagnostic position for attribute.
                setDiagPos(ai.pos());
                
                if (ai.needsCloning()) {
                    makeMixinApplyDefaultsMethod(ai);
                    makeMixinEvaluateAccessorMethod(ai);
                    makeMixinInvalidateAccessorMethod(ai);
                    makeOnReplaceAccessorMethod(ai, true);
                }
            }
        }

        //
        // This method generates an enumeration for each of the instance attributes
        // of the class.
        //
        public void makeAttributeNumbers(List<VarInfo> attrInfos, int varCount, LiteralInitVarMap varMap) {
            // Reset diagnostic position to current class.
            resetDiagPos();

            // Construct a static count variable (VCNT$), -1 indicates count has not been initialized.
            addDefinition(addSimpleIntVariable(Flags.STATIC | Flags.PUBLIC, defs.varCountName, -1));

            // Construct a static count accessor method (VCNT$)
            makeVCNT$(attrInfos, varCount);

            // Construct a virtual count accessor method (count$)
            makecount$();

            // Accumulate variable numbering.
            for (VarInfo ai : attrInfos) {
                // Only variables actually declared.
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Set diagnostic position for attribute.
                    setDiagPos(ai.pos());

                    // Construct offset var.
                    Name name = attributeOffsetName(ai.getSymbol());
                    // Construct and add: public static int VOFF$name = n;
                    addDefinition(makeVar(Flags.STATIC | Flags.PUBLIC, syms.intType, name, null));
                }

                // Add to var map if an anon class.
                // Exclude the bogus $internal$ fields of FXBase/FXObject
                if (varMap != null && !ai.getSymbol().name.endsWith(defs.internalSuffixName)) {
                    varMap.addVar(ai.getSymbol());
                }
            }
        }

        //
        // The method constructs the VCNT$ method for the current class.
        //
        public void makeVCNT$(final List<VarInfo> attrInfos, final int varCount) {
            StaticMethodBuilder smb = new StaticMethodBuilder(defs.varCountName, syms.intType) {
                @Override
                public void statements() {
                    // Start if block.
                    beginBlock();
        
                    // VCNT$ = super.VCNT$() + n  or VCNT$ = n;
                    JCExpression setVCNT$Expr = superClassSym == null ?  makeInt(varCount) :
                                                                         makeBinary(JCTree.PLUS,
                                                                                    call(superClassSym.type, defs.varCountName),
                                                                                    makeInt(varCount));
                    Name countName = names.fromString("$count");
                    // final int $count = VCNT$ = super.VCNT$() + n;
                    addStmt(makeVar(Flags.FINAL, syms.intType, countName, m().Assign(id(defs.varCountName), setVCNT$Expr)));
        
                    for (VarInfo ai : attrInfos) {
                        // Only variables actually declared.
                        if (ai.needsCloning() && !ai.isOverride()) {
                            // Set diagnostic position for attribute.
                            setDiagPos(ai.pos());
                            // Offset var name.
                            Name name = attributeOffsetName(ai.getSymbol());
                            // VCNT$ - n + i;
                            JCExpression setVOFF$Expr = makeBinary(JCTree.PLUS, id(countName), makeInt(ai.getEnumeration() - varCount));
                            // VOFF$var = VCNT$ - n + i;
                            addStmt(makeExec(m().Assign(id(name), setVOFF$Expr)));
                        }
                    }
        
                    // VCNT$ == -1
                    JCExpression condition = makeEqual(id(defs.varCountName), makeInt(-1));
                    // if (VCNT$ == -1) { ...
                    addStmt(m().If(condition, endBlock(), null));
                    // return VCNT$;
                    addStmt(m().Return(id(defs.varCountName)));
                }
            };
            
            smb.build();
        }

        //
        // The method constructs the count$ method for the current class.
        //
        public void makecount$() {
            MethodBuilder smb = new MethodBuilder(defs.attributeCountMethodName, syms.intType) {
                @Override
                public void statements() {
                    // Construct and add: return VCNT$();
                    addStmt(m().Return(call(defs.varCountName)));
                }
            };
            
            smb.build();
        }
        
        //
        // Clones a field declared in FXBase as an non-static field.  It also creates
        // FXObject accessor method.
        //
        private void cloneFXBaseVar(VarSymbol var, HashSet<String> excludes) {
            // Var name as a string.
            String str = var.name.toString();
            // Var modifier flags.
            long flags = var.flags();
            
            // If it's an excluded name or a static then skip it.
            if (excludes.contains(str) ||
                (flags & (Flags.SYNTHETIC | Flags.STATIC)) != 0) {
                return;
            }
            
            // Var FX type.
            Type type = var.asType();
            
            // Clone the var.
            addDefinition(makeVar(flags, type, str, null));
            
            // Construct the getter.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            Name name = names.fromString("get" + str);
            stmts.append(m().Return(id(var)));
            // public int getVar { return Var; }
            JCMethodDecl getMeth = makeMethod(flags, type, name, List.<JCVariableDecl>nil(), stmts);
            // Add to definitions.
            addDefinition(getMeth);
            // Add to the exclusion set.
            excludes.add(jcMethodDeclStr(getMeth));

            // Construct the setter.
            stmts = ListBuffer.lb();
            name = names.fromString("set" + str);
            Name argName = names.fromString("value");
            JCVariableDecl arg = makeParam(type, argName);
            stmts.append(m().Exec(m().Assign(id(var), id(argName))));
            // public void setVar(final int value) { Var = value; }
            JCMethodDecl setMeth = makeMethod(flags, syms.voidType, name, List.<JCVariableDecl>of(arg), stmts);
            // Add to definitions.
            addDefinition(setMeth);
            // Add to the exclusion set.
            excludes.add(jcMethodDeclStr(setMeth));
        }

        //
        // Clones a method declared as an FXObject interface to call the static 
        // equivalent in FXBase. 
        //
        private void cloneFXBaseMethod(MethodSymbol method, HashSet<String> excludes) {
            // Method modifier flags.
            long flags = method.flags();
            
            // If it's an excluded name or a static then skip it.
            if (excludes.contains(method.toString()) ||
                (flags & (Flags.SYNTHETIC | Flags.STATIC)) != 0) {
                return;
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
                args.append(makeParam(argSym.asType(), argSym.name));
                callArgs.append(id(argSym));
            }

            // Buffer for statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Method return type.
            Type returnType = method.getReturnType();
            // Basic call to supporting FXBase method.
            JCExpression fxBaseCall = call(makeType(syms.javafx_FXBaseType), method.name, callArgs);
           
            // Exec or return based on return type.
            if (returnType == syms.voidType) {
                stmts.append(makeExec(fxBaseCall));
            } else {
                stmts.append(m().Return(fxBaseCall));
            }
    
            //  public type meth$(t0 arg0, ...) { return FXBase.meth$(this, arg0, ...); }
            addDefinition(makeMethod(Flags.PUBLIC, returnType, method.name, args.toList(), stmts));
        }

        //
        // This method clones the contents of FXBase and FXObject when inheriting
        // from a java class.
        //
        public void cloneFXBase(HashSet<String> excludes) {
            // Reset diagnostic position to current class.
            resetDiagPos();

            // Retrieve FXBase and FXObject.
            ClassSymbol fxBaseSym = (ClassSymbol)syms.javafx_FXBaseType.tsym;
            ClassSymbol fxObjectSym = (ClassSymbol)syms.javafx_FXObjectType.tsym;
            Entry e;

            // Clone the vars in FXBase.
            for (e = fxBaseSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                if (e.sym instanceof VarSymbol) {
                     cloneFXBaseVar((VarSymbol)e.sym, excludes);
                }
            }

            // Clone the interfaces in FXObject.
            for (e = fxObjectSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                if (e.sym instanceof MethodSymbol) {
                     cloneFXBaseMethod((MethodSymbol)e.sym, excludes);
                }
            }
        }

        //
        // This method constructs the statements needed to apply defaults to a given var.
        //
        private JCStatement makeApplyDefaultsStatement(VarInfo ai, boolean isMixinClass) {
            if (ai.hasBoundDefinition()) {
                // bind, don't set in applyDefaults$
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
                    stmt = makeSuperCall((ClassSymbol)varSym.owner, methodName, id(names._this));
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
        // This method constructs the current class's applyDefaults$ method.
        //
        public void makeApplyDefaultsMethod() {
            // Number of variables in current class.
            int count = analysis.getClassVarCount();

            // Grab the super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();

            // Prepare to accumulate statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Reset diagnostic position to current class.
            resetDiagPos();

            // Prepare to accumulate cases.
            ListBuffer<JCCase> cases = ListBuffer.lb();
             // Prepare to accumulate overrides.
            ListBuffer<JCStatement> overrides = ListBuffer.lb();

            // Gather the instance attributes.
            List<VarInfo> attrInfos = analysis.classVarInfos();
            for (VarInfo ai : attrInfos) {
                // Only declared attributes with default expressions.
                if (ai.needsCloning() && !ai.isOverride()) {
                    // Prepare to accumulate case statements.
                    ListBuffer<JCStatement> caseStmts = ListBuffer.lb();

                    // Get body of applyDefaults$.
                    JCStatement deflt = makeApplyDefaultsStatement(ai, isMixinClass());

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
                    stmts.append(callStmt(selector, defs.attributeApplyDefaultsPrefixMethodName, args));
                }

                // Construct method.
                JCMethodDecl method = makeMethod(Flags.PUBLIC,
                                                 syms.voidType,
                                                 defs.attributeApplyDefaultsPrefixMethodName,
                                                 List.<JCVariableDecl>of(makeParam(syms.intType, varNumName)),
                                                 stmts);
                addDefinition(method);
            }
        }
        
        //
        // This method constructs the current class's update$ method.
        //
        public void makeUpdateMethod(HashMap<VarSymbol, HashMap<VarSymbol, HashSet<VarInfo>>> updateMap) {
            // Prepare to accumulate statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            // Grab the super class.
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
            // Reset diagnostic position to current class.
            resetDiagPos();
           
            // generate method if it is worthwhile or we have to.
            if (!updateMap.isEmpty() || superClassSym == null) {
                // Loop for instance symbol.
                for (VarSymbol instanceVar : updateMap.keySet()) {
                    HashMap<VarSymbol, HashSet<VarInfo>> instanceMap = updateMap.get(instanceVar);
                    
                    // Loop for reference symbol.
                    JCStatement ifReferenceStmt = null;
                    for (VarSymbol referenceVar : instanceMap.keySet()) {
                        HashSet<VarInfo> referenceSet = instanceMap.get(referenceVar);
                        ListBuffer<JCStatement> invalidateStmts = ListBuffer.lb();
                        
                        // Loop for local vars.
                        for (VarInfo varInfo : referenceSet) {
                            VarSymbol proxyVarSym = varInfo.proxyVarSym();
                            invalidateStmts.append(callStmt(getReceiver(proxyVarSym), attributeInvalidateName(proxyVarSym), id(phaseName)));
                        }

                        // Reference the class with the instance, if it is script-level append the suffix
                        JCExpression referenceSelect = makeVarOffset(referenceVar, instanceVar);
                        JCExpression ifReferenceCond = makeBinary(JCTree.EQ, id(varNumName), referenceSelect);
                        ifReferenceStmt = m().If(ifReferenceCond, m().Block(0L, invalidateStmts.toList()), ifReferenceStmt);
                    }
                    
                    JCExpression ifInstanceCond = makeBinary(JCTree.EQ, id(updateInstanceName), id(attributeValueName(instanceVar)));
                    JCStatement ifInstanceStmt = m().If(ifInstanceCond, m().Block(0L, List.<JCStatement>of(ifReferenceStmt)), null);
                    stmts.append(ifInstanceStmt);
                }
            
                // If there is a super class.
                if (superClassSym != null) {
                    // super
                    JCExpression selector = id(names._super);
                    // (varNum)
                    List<JCExpression> args = List.<JCExpression>of(id(updateInstanceName), id(varNumName), id(phaseName));
                    // Construct and add: return super.applyDefaults$(varNum);
                    stmts.append(callStmt(selector, defs.attributeUpdatePrefixMethodName, args));
                }

                // Construct method.
                JCMethodDecl method = makeMethod(Flags.PUBLIC,
                                                 syms.voidType,
                                                 defs.attributeUpdatePrefixMethodName,
                                                 List.<JCVariableDecl>of(makeParam(syms.javafx_FXObjectType, updateInstanceName),
                                                                         makeParam(syms.intType, varNumName),
                                                                         makeParam(syms.intType, phaseName)),
                                                 stmts);
                // Add to the methods list.
                addDefinition(method);
            }
        }
        
        //
        // This method constructs the current class's get$ method.
        //
        public void makeGetMethod(List<VarInfo> attrInfos, int varCount) {
            VarCaseMethodBuilder vcmb = new VarCaseMethodBuilder(defs.attributeGetPrefixName, syms.objectType,
                                                                 attrInfos, varCount) {
                @Override
                public void statements() {
                    // get$var()
                    JCExpression getterExp = call(attributeGetterName(varInfo.getSymbol()));
                    // return get$var()
                    addStmt(m().Return(getterExp));
                }
            };
            
            vcmb.build();
        }
        
        //
        // This method constructs the current class's set$ method.
        //
        public void makeSetMethod(List<VarInfo> attrInfos, int varCount) {
            VarCaseMethodBuilder vcmb = new VarCaseMethodBuilder(defs.attributeSetPrefixName, syms.voidType,
                                                                 attrInfos, varCount) {
                @Override
                public void initialize() {
                    addParam(syms.objectType, objName);
                }
                
                @Override
                public void statements() {
                    // (type)object$
                    JCExpression objCast = typeCast(diagPos, varInfo.getRealType(), syms.objectType, id(objName));
                    // set$var((type)object$)
                    addStmt(callStmt(attributeSetterName(varInfo.getSymbol()), objCast));
                    // return
                    addStmt(m().Return(null));
                }
            };
            
            vcmb.build();
        }
        
        //
        // This method constructs the current class's getType$ method.
        //
        public void makeTypeMethod(List<VarInfo> attrInfos, int varCount) {
            VarCaseMethodBuilder vcmb = new VarCaseMethodBuilder(defs.attributeTypePrefixName,
                                                                 makeQualifiedTree(diagPos, "java.lang.Class"),
                                                                 attrInfos, varCount) {
                @Override
                public void statements() {
                    Type type = types.erasure(varInfo.getRealType());
                    JCExpression expr = m().ClassLiteral(type);
                    addStmt(m().Return(expr));
                }
            };
            
            vcmb.build();
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
            args.append(call(cSym.type, defs.varCountName));

            // For each var declared in order (to make the switch tags align to the vars.)
            for (VarSymbol vSym : varMap.varList.toList()) {
                // ..., X.VOFF$x, ...

                args.append(select(makeType(cSym.type), attributeOffsetName(vSym)));
            }

            // FXBase.makeInitMap$(X.VCNT$(), X.VOFF$a, ...)
            return call(syms.javafx_FXBaseType, makeInitMap, args);
        }

        //
        // This method constructs a single var map declaration.
        //
        private JCVariableDecl makeInitVarMapDecl(ClassSymbol cSym, LiteralInitVarMap varMap) {
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
        public JCStatement makeInitVarMapInit(LiteralInitVarMap varMap) {
            // Get current class symbol.
            ClassSymbol cSym = getCurrentClassSymbol();
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
        public void makeInitClassMaps(LiteralInitClassMap initClassMap) {
            // Reset diagnostic position to current class.
            resetDiagPos();

            // For each class initialized in the current class.
            for (ClassSymbol cSym : initClassMap.classMap.keySet()) {
                // Get the var map for the referencing class.
                LiteralInitVarMap varMap = initClassMap.classMap.get(cSym);
                // Add to var.
                addDefinition(makeInitVarMapDecl(cSym, varMap));

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
                // Add method.
                addDefinition(method);
            }
        }


        //
        // This method constructs a super call with appropriate arguments.
        //
        private JCStatement makeSuperCall(ClassSymbol cSym, Name name, JCExpression... args) {
            ListBuffer<JCExpression> buffer = ListBuffer.lb();
            
            for (JCExpression arg : args) {
                buffer.append(arg);
            }
            
            return makeSuperCall(cSym, name, buffer.toList());
        }
        private JCStatement makeSuperCall(ClassSymbol cSym, Name name, List<JCExpression> args) {
            // If this is from a mixin class then we need to use receiver$ otherwise this.
            boolean fromMixinClass = isMixinClass();
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
            return callStmt(selector, name, args);
        }

        //
        // Construct the static block for setting defaults
        //
        public void makeInitStaticAttributesBlock(Name scriptName, List<VarInfo> attrInfo, JCStatement initMap) {
            // Buffer for init statements.
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
    
            // Initialize the var map for anon class.
            if (initMap != null) {
                stmts.append(initMap);
            }

            if (scriptName != null) {
                stmts.append(callStmt(defs.scriptLevelAccessMethod));
            }
            
            if (attrInfo != null) {
                for (VarInfo ai : attrInfo) {
                    JCStatement init = ai.getDefaultInitStatement();
                    
                    if (init != null) {
                        stmts.append(init);
                    }
                }
            }
             
            addDefinition(m().Block(Flags.STATIC, stmts.toList()));
        }

        //
        // This method generates the code for a userInit or postInit method.
        public void makeInitMethod(Name methName, ListBuffer<JCStatement> translatedInitBlocks, List<ClassSymbol> immediateMixinClasses) {
            ClassSymbol superClassSym = analysis.getFXSuperClassSym();
           
            // Only create method if necessary (rely on FXBase.)
            if (translatedInitBlocks.nonEmpty() || immediateMixinClasses.nonEmpty() || isMixinClass() || superClassSym == null) {
                List<JCVariableDecl> receiverVarDeclList;
                ListBuffer<JCStatement> stmts = ListBuffer.lb();
    
                // Mixin super calls will be handled when inserted into real classes.
                if (!isMixinClass()) {
                    //TODO:
                    // Some implementation code is still generated assuming a receiver parameter.  Until this is fixed
                    //    var receiver = this;
                    
                    stmts.append(makeVar(Flags.FINAL, id(interfaceName(getCurrentClassDecl())), defs.receiverName, id(names._this)));
                    receiverVarDeclList = List.<JCVariableDecl>nil();
    
                    if (superClassSym != null) {
                        stmts.append(callStmt(id(names._super), methName));
                    }
    
                    for (ClassSymbol mixinClassSym : immediateMixinClasses) {
                        JCExpression selector = makeType(mixinClassSym.type, false);
                        stmts.append(callStmt(selector, methName,  m().TypeCast(makeType(mixinClassSym), id(names._this))));
                    }
                } else {
                    receiverVarDeclList = List.<JCVariableDecl>of(makeReceiverParam(getCurrentClassDecl()));
                }
    
                stmts.appendList(translatedInitBlocks);

                addDefinition(makeMethod(!isMixinClass() ? Flags.PUBLIC : (Flags.PUBLIC | Flags.STATIC),
                                         syms.voidType,
                                         methName,
                                         receiverVarDeclList,
                                         stmts.toList()));
            }
        }

        private void makeConstructor(List<JCVariableDecl> params, List<JCStatement> cStats) {
            resetDiagPos();
            addDefinition(m().MethodDef(m().Modifiers(Flags.PUBLIC),
                          names.init,
                          makeType(syms.voidType),
                          List.<JCTypeParameter>nil(),
                          params,
                          List.<JCExpression>nil(),
                          m().Block(0L, cStats),
                          null));
    
        }
    
        //
        // Make a constructor to be called by Java code.
        // Simply pass up to super, unless this is the last JavaFX class, in which case add object initialization
        //
        public void makeJavaEntryConstructor() {
            //    public Foo() {
            //        this(false);
            //        initialize$();
            //    }
            makeConstructor(List.<JCVariableDecl>nil(), List.of(
                            callStmt(names._this, makeBoolean(false)),
                            callStmt(defs.initializeName)));
        }

        //
        // Make a constructor to be called by JavaFX code.
        //
        public void makeFXEntryConstructor(ClassSymbol outerTypeSym) {
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            Name dummyParamName = names.fromString("dummy");
    
            // call the FX version of the constructor in the superclass
            //    public Foo(boolean dummy) {
            //        super(dummy);
            //    }
            if (analysis.getFXSuperClassSym() != null) {
                stmts.append(callStmt(names._super, id(dummyParamName)));
            } else {
                stmts.append(callStmt(defs.initFXBaseName));
            }
    
            // Construct the parameters
            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            if (outerTypeSym != null) {
                // add a parameter and a statement to constructor for the outer instance reference
                params.append(makeParam(outerTypeSym.type, outerAccessorFieldName) );
                JCFieldAccess cSelect = m().Select(id(names._this), outerAccessorFieldName);
                stmts.append(makeExec(m().Assign(cSelect, id(outerAccessorFieldName))));
            }
            params.append(makeParam(syms.booleanType, dummyParamName));
    
            makeConstructor(params.toList(), stmts.toList());
        }
    

        //
        // Make the field for accessing the outer members
        //
        public void makeOuterAccessorField(ClassSymbol outerTypeSym) {
            resetDiagPos();
            // Create the field to store the outer instance reference
            addDefinition(m().VarDef(m().Modifiers(Flags.PUBLIC), outerAccessorFieldName, id(outerTypeSym), null));
        }
    
        //
        // Make the method for accessing the outer members
        //
        public void makeOuterAccessorMethod(ClassSymbol outerTypeSym) {
            resetDiagPos();
            ListBuffer<JCStatement> stmts = ListBuffer.lb();

            VarSymbol vs = new VarSymbol(Flags.PUBLIC, outerAccessorFieldName, outerTypeSym.type, getCurrentClassSymbol());
            stmts.append(m().Return(id(vs)));
            addDefinition(makeMethod(Flags.PUBLIC, outerTypeSym.type, defs.outerAccessorName, List.<JCVariableDecl>nil(), stmts));
        }
        
        //
        // Make a method body which redirects to the actual implementation in a static method of the defining class.
        //
        private JCBlock makeDispatchBody(MethodSymbol sym, boolean isBound, boolean isStatic) {
            ClassSymbol cSym = analysis.getCurrentClassSymbol();
            ListBuffer<JCExpression> args = ListBuffer.lb();
            if (!isStatic) {
                // Add the this argument, so the static implementation method is invoked
                args.append(m().TypeCast(id(interfaceName(getCurrentClassDecl())), id(names._this)));
            }
            for (VarSymbol var : sym.params) {
                args.append(id(var));
            }
            JCExpression receiver = isCurrentClassSymbol(sym.owner) ? null : makeType(sym.owner.type, false);
            JCExpression expr = call(receiver, functionName(sym, !isStatic, isBound), args);
            JCStatement stmt = (sym.getReturnType() == syms.voidType) ? m().Exec(expr) : m().Return(expr);
            return m().Block(0L, List.<JCStatement>of(stmt));
        }

        //
        // Make a method from a MethodSymbol and an optional method body.
        // Make a bound version if "isBound" is set.
        //
        private void appendMethodClones(MethodSymbol sym, boolean needsBody) {
            resetDiagPos();
            //TODO: static test is broken
            boolean isBound = (sym.flags() & JavafxFlags.BOUND) != 0;
            JCBlock body = needsBody ? makeDispatchBody(sym, isBound, (sym.flags() & Flags.STATIC) != 0) : null;
            // build the parameter list
            
            ListBuffer<JCVariableDecl> params = ListBuffer.lb();
            for (VarSymbol vsym : sym.getParameters()) {
                if (isBound) {
                    TODO();
                }
                params.append(m().VarDef(m().Modifiers(0L), vsym.name, makeType(vsym.asType()), null));
            }
    
            // make the method
            JCModifiers mods = m().Modifiers(Flags.PUBLIC | (body == null? Flags.ABSTRACT : 0L));
            
            if (isCurrentClassSymbol(sym.owner))
                mods = addAccessAnnotationModifiers(diagPos, sym.flags(), mods);
            else
                mods = addInheritedAnnotationModifiers(diagPos, sym.flags(), mods);
                
            addDefinition(m().MethodDef(
                          mods,
                          functionName(sym, false, isBound),
                          makeReturnTypeTree(diagPos, sym, isBound),
                          List.<JCTypeParameter>nil(),
                          params.toList(),
                          List.<JCExpression>nil(),
                          body,
                          null));
            if (needsBody) {
                optStat.recordProxyMethod();
            }
        }
    
    
        //
        // Add proxies which redirect to the static implementation for every concrete method
        //
        public void makeFunctionProxyMethods(List<MethodSymbol> needDispatch) {
            for (MethodSymbol sym : needDispatch) {
                appendMethodClones(sym, true);
            }
        }

        //
        // Add interface declarations for declared methods.
        //
        public void makeFunctionInterfaceMethods() {
            for (JFXTree def : getCurrentClassDecl().getMembers()) {
                if (def.getFXTag() == JavafxTag.FUNCTION_DEF) {
                    JFXFunctionDefinition func = (JFXFunctionDefinition) def;
                    MethodSymbol sym = func.sym;
                    
                    if ((sym.flags() & (Flags.SYNTHETIC | Flags.STATIC | Flags.PRIVATE)) == 0) {
                        appendMethodClones(sym, false);
                    }
                }
            }
        }

        //
        // This method constructs a script class.
        //
        public void makeScript(Name scriptName, List<JCTree> definitions) {
            JCModifiers classMods = m().Modifiers(Flags.PUBLIC | Flags.STATIC);
            classMods = addAccessAnnotationModifiers(diagPos, 0, classMods);
            JCClassDecl script =m().ClassDef(
                    classMods,
                    scriptName,
                    List.<JCTypeParameter>nil(),
                    makeType(syms.javafx_FXBaseType),
                    List.<JCExpression>of(makeType(syms.javafx_FXObjectType)),
                    definitions);
                        
            addDefinition(script);
        }
                
        //
        // Methods for accessing the outer members.
        //
        public void makeOuterAccessorInterfaceMembers() {
            ClassSymbol cSym = getCurrentClassSymbol();
            
            if (cSym != null && toJava.getHasOuters().contains(cSym)) {
                Symbol typeOwner = cSym.owner;
                
                while (typeOwner != null && typeOwner.kind != Kinds.TYP) {
                    typeOwner = typeOwner.owner;
                }
    
                if (typeOwner != null) {
                    ClassSymbol returnSym = reader.enterClass(names.fromString(typeOwner.type.toString() + mixinSuffix));
                    JCMethodDecl accessorMethod = m().MethodDef(
                            m().Modifiers(Flags.PUBLIC),
                            defs.outerAccessorName,
                            id(returnSym),
                            List.<JCTypeParameter>nil(),
                            List.<JCVariableDecl>nil(),
                            List.<JCExpression>nil(), null, null);
                    addDefinition(accessorMethod);
                    optStat.recordProxyMethod();
                }
            }
        }

        //
        // Add definitions to class to access the script-level sole instance.
        //
        public void makeScriptLevelAccess(Name scriptName, boolean scriptLevel, boolean isRunnable) {
            if (!scriptLevel) {
                // sole instance field
                addDefinition(makeVar(Flags.PRIVATE | Flags.STATIC, id(scriptName), defs.scriptLevelAccessField, null));

                // sole instance lazy creation method
                JCExpression condition = makeNullCheck(id(defs.scriptLevelAccessField));

                JCExpression assignExpr = m().Assign(
                        id(defs.scriptLevelAccessField),
                        m().NewClass(null, null, id(scriptName), List.<JCExpression>nil(), null));

                ListBuffer<JCStatement> stmts = ListBuffer.lb();

                stmts.append(m().If(condition, makeExec(assignExpr), null));
                stmts.append(m().Return(id(defs.scriptLevelAccessField)));
                addDefinition(makeMethod(Flags.PUBLIC | Flags.STATIC, id(scriptName), defs.scriptLevelAccessMethod, null, stmts.toList()));
            }

            // If module is runnable, create a run method that redirects to the sole instance version
            if (!SCRIPT_LEVEL_AT_TOP && !scriptLevel && isRunnable) {
                addDefinition(makeMethod(Flags.PUBLIC | Flags.STATIC,
                                         syms.objectType,
                                         defs.internalRunFunctionName,
                                         List.<JCVariableDecl>of(m().Param(defs.arg0Name, types.sequenceType(syms.stringType), null)),
                                         List.<JCStatement>of(m().Return(call(
                                             call(defs.scriptLevelAccessMethod),
                                             defs.internalRunFunctionName,
                                             id(defs.arg0Name))))));
            }
        }
    }
}
