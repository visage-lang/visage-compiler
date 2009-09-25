/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.tools.mjavac.code.Flags;
import com.sun.tools.mjavac.code.Kinds;
import com.sun.tools.mjavac.code.Scope.Entry;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.ClassSymbol;
import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.tree.JCTree;
import com.sun.tools.mjavac.tree.JCTree.JCExpression;
import com.sun.tools.mjavac.tree.JCTree.JCStatement;
import com.sun.tools.mjavac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.ListBuffer;
import com.sun.tools.mjavac.util.Name;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.comp.JavafxAbstractTranslation.ExpressionResult;
import com.sun.tools.javafx.tree.*;

import static com.sun.tools.mjavac.code.Flags.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is used by JavafxInitializationBuilder to determine which inherited
 * attributes and methods have effect in the current javafx class.
 * @author Robert Field
 * @author Jim Laskey
 */
class JavafxAnalyzeClass {

    // Position in the current javafx class source.
    private final DiagnosticPosition diagPos;

    // Current class decl.
    private final JFXClassDeclaration currentClassDecl;

    // Current class symbol.
    private final ClassSymbol currentClassSym;

    // Null or symbol for the immediate super class (if a javafx class.)
    private ClassSymbol superClassSym;

    // Resulting list of all superclasses in top down order.
    private ListBuffer<ClassSymbol> superClasses = ListBuffer.lb();

    // Resulting list of immediate mixin classes in left to right order.
    private ListBuffer<ClassSymbol> immediateMixins = ListBuffer.lb();

    // Resulting list of all mixin classes in top down order.
    private ListBuffer<ClassSymbol> allMixins = ListBuffer.lb();

    // Number of vars in the current class (includes mixins.)
    private int classVarCount;

    // Number of vars in the current class (includes mixins.)
    private int scriptVarCount;
    
    // Resulting list of class vars.
    private final ListBuffer<VarInfo> classVarInfos = ListBuffer.lb();

    // Resulting list of script vars.
    private final ListBuffer<VarInfo> scriptVarInfos = ListBuffer.lb();

    // Resulting list of class vars.
    private final ListBuffer<FuncInfo> classFuncInfos = ListBuffer.lb();

    // Resulting list of script vars.
    private final ListBuffer<FuncInfo> scriptFuncInfos = ListBuffer.lb();

    // List of all attributes.  Used to track overridden and mixin attributes.
    private final Map<Name, VarInfo> visitedAttributes = new HashMap<Name, VarInfo>();

    // Resulting list of relevant methods.  A map is used to so that only the last occurrence is kept.
    private final Map<String, MethodSymbol> needDispatchMethods = new HashMap<String, MethodSymbol>();

    // List of all methods.  Used to track whether a mixin method should be included.
    private final Map<String, MethodSymbol> visitedMethods = new HashMap<String, MethodSymbol>();

    // List of classes encountered.  Used to prevent duplication.
    private final Set<Symbol> addedBaseClasses = new HashSet<Symbol>();

    // List of vars (attributes) found in the current javafx class (supplied by JavafxInitializationBuilder.)
    private final List<TranslatedVarInfo> translatedAttrInfo;

    // List of overriding vars (attributes) found in the current javafx class (supplied by JavafxInitializationBuilder.)
    private final List<TranslatedOverrideClassVarInfo> translatedOverrideAttrInfo;

    // List of functions (methods) found in the current javafx class (supplied by JavafxInitializationBuilder.)
    private final List<TranslatedFuncInfo> translatedFuncInfo;

    // Global names table (supplied by JavafxInitializationBuilder.)
    private final Name.Table names;

    // Global types table (supplied by JavafxInitializationBuilder.)
    private final JavafxTypes types;

    // Class reader used to fetch superclass .class files (supplied by JavafxInitializationBuilder.)
    private final JavafxClassReader reader;

    // Javafx to Java type translator (supplied by JavafxInitializationBuilder.)
    private final JavafxTypeMorpher typeMorpher;

    //
    // This class supers all classes used to hold var information. Consumed by
    // JavafxInitializationBuilder.
    //
    static abstract class VarInfo {
        // Position of the var declaration or current javafx class if read from superclass.
        private final DiagnosticPosition diagPos;

        // Var symbol (unique to symbol.)
        private final VarSymbol sym;

        // Translated type information.
        private final VarMorphInfo vmi;

        // Name of the var (is it the same as sym.name?)
        private final Name name;

        // Null or code for initializing the var.
        private final JCStatement initStmt;

        // The class local enumeration value for this var.
        private int enumeration;


        private VarInfo(DiagnosticPosition diagPos, Name name, VarSymbol attrSym, VarMorphInfo vmi,
                JCStatement initStmt) {
            this.diagPos = diagPos;
            this.name = name;
            this.sym = attrSym;
            this.vmi = vmi;
            this.initStmt = initStmt;
            this.enumeration = -1;
        }

        // Return the var symbol.
        public VarSymbol getSymbol() { return sym; }

        // Return the var position.
        public DiagnosticPosition pos() { return diagPos; }

        // Return type information from type translation.
        public VarMorphInfo getVMI()  { return vmi; }
        public Type getRealType()     { return vmi.getRealType(); }
        public Type getElementType()  { return vmi.getElementType(); }
        public boolean useAccessors() { return vmi.useAccessors(); }

        // Return var name.
        public Name getName() { return name; }

        // Return var name as string.
        public String getNameString() { return name.toString(); }

        // Return modifier flags from the symbol.
        public long getFlags() { return sym.flags(); }
        
        // Return true if the var/override has an initializing expression
        public boolean hasInitializer() { throw new RuntimeException("no initializer info"); }

        // Return true if the var has a bound definition.
        public boolean hasBoundDefinition() { throw new RuntimeException("no accurate bind info"); }
        
        // Return true if the var has a bidirectional bind.
        public boolean hasBiDiBoundDefinition() { throw new RuntimeException("no accurate bind info"); }
        
        // Return true if the var is an inline bind.
        public boolean isInlinedBind() { return hasBoundDefinition(); }

        // Generally means that the var needs to be included in the current method.
        public boolean needsCloning() { return false; }
        
        // Return true if the var is an override.
        public boolean isOverride() { return false; }

        // A proxy var serves several roles, but generally means that the proxy's
        // qualified name should be used in place of the current var's qualified name.
        public VarInfo proxyVar() { return null; }
        public boolean hasProxyVar() { return proxyVar() != null; }

        // Convenience method to return the current symbol to be used for qualified name.
        public VarSymbol proxyVarSym() { return hasProxyVar() ? proxyVar().sym : sym; }

        // Predicate for static var test.
        public boolean isStatic() { return (getFlags() & Flags.STATIC) != 0; }

        // Predicate for private var test.
        public boolean isPrivateAccess() { return (getFlags() & Flags.PRIVATE) != 0L; }

        // Predicate for def (constant) var.
        public boolean isDef() { return (getFlags() & JavafxFlags.IS_DEF) != 0; }

        // Predicate whether the var came from a mixin.
        public boolean isMixinVar() { return isMixinClass(sym.owner); }

        // Predicate whether the var came from the current javafx class.
        public boolean isDirectOwner() { return false; }

        // Predicate to test for sequence.
        public boolean isSequence() {
            return vmi.getTypeKind() == JavafxVarSymbol.TYPE_KIND_SEQUENCE;
        }
        // Returns null or the code for var initialization.
        public JCStatement getDefaultInitStatement() { return initStmt; }

        // Class local enumeration accessors.
        public int  getEnumeration()                { return enumeration; }
        public void setEnumeration(int enumeration) { this.enumeration = enumeration; }

        // null or javafx tree for the var's 'on replace'.
        public JFXOnReplace onReplace() { return null; }

        // null or Java tree for var's on-replace for use in a setter.
        public JCStatement onReplaceAsInline() { return null; }

        // null or Java tree for var's on-replace for use in change listeber.
        public JCStatement onReplaceAsListenerInstanciation() { return null; }

        // Null or Java code for getter expression of bound variable
        public JCExpression boundInit() { return null; }

        // Null or Java preface code for getter of bound variable
        public List<JCStatement> boundPreface() { return List.<JCStatement>nil(); }

        // Null or variable symbols on which this variable depends
        public List<VarSymbol> boundBindees() { return List.<VarSymbol>nil(); }

        @Override
        public String toString() { return getNameString(); }

        // Useful diagnostic tool.
        public void printInfo() {
            System.out.println("    " + getSymbol() +
                               ", type=" + getRealType() +
                               ", owner=" + getSymbol().owner +
                               ", static=" + isStatic() +
                               ", enum=" + getEnumeration() +
                               ", isPrivateAccess=" + isPrivateAccess() +
                               ", needsCloning=" + needsCloning() +
                               ", isDef=" + isDef() +
                               ", getDefaultInitStatement=" + (getDefaultInitStatement() != null) +
                               ", proxy=" + ((proxyVar() == null) ? "" : proxyVar().getSymbol().owner) +
                               ", class=" + getClass().getName());
        }
    }

    //
    // This base class is used for vars declared in the current javafx class..
    //
    static abstract class TranslatedVarInfoBase extends VarInfo {

        // Null or javafx code for the var's on replace.
        private final JFXOnReplace onReplace;

        // The bind status for the var/override
        private final JavafxBindStatus bindStatus;

        // The does this var have an initializing expression
        private final boolean hasInitializer;

        // Null or java code for the var's on replace inlined in setter.
        private final JCStatement onReplaceAsInline;

        // Result of bind translation
        private final ExpressionResult bindOrNull;
        
        // Inversion of boundBindees.
        private ListBuffer<VarSymbol> bindersOrNull;

        TranslatedVarInfoBase(DiagnosticPosition diagPos, Name name, VarSymbol attrSym, JavafxBindStatus bindStatus, boolean hasInitializer, VarMorphInfo vmi,
                JCStatement initStmt, ExpressionResult bindOrNull, JFXOnReplace onReplace, JCStatement onReplaceAsInline) {
            super(diagPos, name, attrSym, vmi, initStmt);
            this.hasInitializer = hasInitializer;
            this.bindStatus = bindStatus;
            this.bindOrNull = bindOrNull;
            this.onReplace = onReplace;
            this.onReplaceAsInline = onReplaceAsInline;
        }

        // Return true if the var/override has an initializing expression
        @Override
        public boolean hasInitializer() { return hasInitializer; }

        // Return true if the var has a bound definition.
        @Override
        public boolean hasBoundDefinition() { return bindStatus.isBound(); }

        // Return true if the var has a bidirectional bind.
        @Override
        public boolean hasBiDiBoundDefinition() { return bindStatus.isBidiBind(); }

        // Null or Java code for getter expression of bound variable
        @Override
        public JCExpression boundInit() { return bindOrNull==null? null : bindOrNull.expr(); }

        // Null or Java preface code for getter of bound variable
        @Override
        public List<JCStatement> boundPreface() { return bindOrNull==null? List.<JCStatement>nil() : bindOrNull.statements(); }

        // Variable symbols on which this variable depends
        @Override
        public List<VarSymbol> boundBindees() { return bindOrNull==null? List.<VarSymbol>nil() : bindOrNull.bindees(); }

        // Bound variable symbols on which this variable is used.
        public List<VarSymbol> boundBinders() { return bindersOrNull==null? List.<VarSymbol>nil() : bindersOrNull.toList(); }

        // Possible javafx code for the var's 'on replace'.
        @Override
        public JFXOnReplace onReplace() { return onReplace; }

        // Possible java code for the var's 'on replace' in setter.
        @Override
        public JCStatement onReplaceAsInline() { return onReplaceAsInline; }

        // This var is in the current javafx class so it has to be cloned into the java class.
        @Override
        public boolean needsCloning() { return true; }

        // Has to be the current javafx class.
        @Override
        public boolean isDirectOwner() { return true; }
    }

    //
    // This class is used for basic vars declared in the current javafx class.
    //
    static class TranslatedVarInfo extends TranslatedVarInfoBase {
        // Tree for the javafx var.
        private final JFXVar var;

        TranslatedVarInfo(JFXVar var, VarMorphInfo vmi,
                JCStatement initStmt, ExpressionResult bindOrNull, JFXOnReplace onReplace, JCStatement onReplaceAsInline) {
            super(var.pos(), var.sym.name, var.sym, var.getBindStatus(), var.getInitializer()!=null, vmi, initStmt, bindOrNull, onReplace, onReplaceAsInline);
            this.var = var;
        }

        // Returns the tree for the javafx var.
        public JFXVar jfxVar() { return var; }
    }

    //
    // This class represents a var override declared in the current javafx class.
    //
    static class TranslatedOverrideClassVarInfo extends TranslatedVarInfoBase {
        // Reference to the var information the override overshadows.
        private VarInfo proxyVar;

        TranslatedOverrideClassVarInfo(JFXOverrideClassVar override,
                 VarMorphInfo vmi,
                JCStatement initStmt, ExpressionResult bindOrNull, JFXOnReplace onReplace, JCStatement onReplaceAsInline) {
            super(override.pos(), override.sym.name, override.sym, override.getBindStatus(), override.getInitializer()!=null, vmi, initStmt, bindOrNull, onReplace, onReplaceAsInline);
        }
        
        // Return true if the var is an override.
        @Override
        public boolean isOverride() { return true; }

        // Returns the var information the override overshadows.
        @Override
        public VarInfo proxyVar() { return proxyVar; }

        // Setter for the proxy var information.
        public void setProxyVar(VarInfo proxyVar) { this.proxyVar = proxyVar; }
    }

    //
    // This class represents a var that is declared in a superclass.  This var may be
    // declared in the same compile unit or read in from a .class file.
    //
    static class SuperClassVarInfo extends VarInfo {
        SuperClassVarInfo(DiagnosticPosition diagPos, VarSymbol var, VarMorphInfo vmi) {
            super(diagPos, var.name, var, vmi, null);
        }

        // Superclass vars are never cloned.
        @Override
        public boolean needsCloning() { return false; }
    }

    //
    // This class represents a var that is declared in a mixin superclass.  This var may be
    // declared in the same compile unit or read in from a .class file.
    //
    static class MixinClassVarInfo extends VarInfo {
        MixinClassVarInfo(DiagnosticPosition diagPos, VarSymbol var, VarMorphInfo vmi) {
            super(diagPos, var.name, var, vmi, null);
        }

        // Return true if the var has a bound definition.
        @Override
        public boolean hasBoundDefinition() { return (getFlags() & JavafxFlags.VARUSE_BOUND_DEFINITION) != 0L; }

        // Return true if the var has a bidirectional bind.
        @Override
        public boolean hasBiDiBoundDefinition() { return (getFlags() & JavafxFlags.VARUSE_BOUND_BIDIRECTIONAL) != 0L; }

        // Mixin vars are always cloned.
        @Override
        public boolean needsCloning() { return true; }
    }

    //
    // Set up the analysis.
    //
    JavafxAnalyzeClass(DiagnosticPosition diagPos,
            ClassSymbol currentClassSym,
            List<TranslatedVarInfo> translatedAttrInfo,
            List<TranslatedOverrideClassVarInfo> translatedOverrideAttrInfo,
            List<TranslatedFuncInfo> translatedFuncInfo,
            Name.Table names,
            JavafxTypes types,
            JavafxClassReader reader,
            JavafxTypeMorpher typeMorpher) {
        this.names = names;
        this.types = types;
        this.reader = reader;
        this.typeMorpher = typeMorpher;
        this.diagPos = diagPos;
        this.currentClassDecl = types.getFxClass(currentClassSym);
        this.currentClassSym = currentClassSym;
        this.translatedAttrInfo = translatedAttrInfo;
        this.translatedOverrideAttrInfo = translatedOverrideAttrInfo;
        this.translatedFuncInfo = translatedFuncInfo;
        this.classVarCount = 0;
        this.scriptVarCount = 0;

        // Start by analyzing the current class.
        analyzeCurrentClass();

        // Assign var enumeration and binders.
        for (VarInfo ai : classVarInfos) {
           if (ai.needsCloning() && !ai.isOverride()) {
               ai.setEnumeration(classVarCount++);
           }
           
           if (ai instanceof TranslatedVarInfoBase) {
              addBinders((TranslatedVarInfoBase)ai);
           }
        }
        for (VarInfo ai : scriptVarInfos) {
           if (ai.needsCloning() && !ai.isOverride()) {
               ai.setEnumeration(scriptVarCount++);
           }
           
           if (ai instanceof TranslatedVarInfoBase) {
              addBinders((TranslatedVarInfoBase)ai);
           }
        }

        // Useful debugging tool.
        // printAnalysis(false);
    }
    
    
    private void addBinders(TranslatedVarInfoBase tai) {
        // If the var has a bind 
        if (tai.boundInit() != null) {
            // Check each of the bindees.
            for (VarSymbol bindeeSym : tai.boundBindees()) {
                // Find the varInfo
                VarInfo bindee = visitedAttributes.get(bindeeSym.name);
                
                // If an interesting var.
                if (bindee instanceof TranslatedVarInfoBase) {
                    TranslatedVarInfoBase bindeeTAI = (TranslatedVarInfoBase)bindee;
                    
                    // Add a symbol buffer if necessary.
                    if (bindeeTAI.bindersOrNull == null) {
                        bindeeTAI.bindersOrNull = ListBuffer.lb();
                    }
                    
                    // Add bunder.
                    bindeeTAI.bindersOrNull.append(tai.getSymbol());
                }
            }
        }
    }

    
    //
    // This class supers all classes used to hold function information. Consumed by
    // JavafxInitializationBuilder.
    //
    static abstract class FuncInfo {
        // Position of the function declaration or current javafx class if read from superclass.
        private final DiagnosticPosition diagPos;

        // Function symbol (unique to symbol.)
        private final MethodSymbol funcSym;
        
        FuncInfo(DiagnosticPosition diagPos, MethodSymbol funcSym) {
            this.diagPos = diagPos;
            this.funcSym = funcSym;
        }

        // Return the function position.
        public DiagnosticPosition pos() { return diagPos; }

        // Return the function symbol.
        public MethodSymbol getSymbol() { return funcSym; }

        // Return modifier flags from the symbol.
        public long getFlags() { return funcSym.flags(); }

        // Predicate for static func test.
        public boolean isStatic() { return (getFlags() & Flags.STATIC) != 0; }
        
        // Useful diagnostic tool.
        public void printInfo() {
            System.out.println("    " + getSymbol() +
                               ", static=" + isStatic());
        }
    }
    
    static class TranslatedFuncInfo extends FuncInfo {
        // Javafx definition of the function.
        private final JFXFunctionDefinition jfxFuncDef;
        
        // Java translation of the function.
        private final List<JCTree> jcFuncDef;
        
        TranslatedFuncInfo(JFXFunctionDefinition jfxFuncDef, List<JCTree> jcFuncDef) {
            super(jfxFuncDef, jfxFuncDef.sym);
            this.jfxFuncDef = jfxFuncDef;
            this.jcFuncDef = jcFuncDef;
        }

        // Return the javafx definition of the function.
        public JFXFunctionDefinition jfxFunction() { return jfxFuncDef; }

        // Return the java translation of the function.
        public List<JCTree> jcFunction() { return jcFuncDef; }

    }

    //
    // Returns the current class position.
    //
    public DiagnosticPosition getCurrentClassPos() { return diagPos; }

    //
    // Returns the current class decl.
    //
    public JFXClassDeclaration getCurrentClassDecl() { return currentClassDecl; }

    //
    // Returns the current class symbol.
    //
    public ClassSymbol getCurrentClassSymbol() { return currentClassSym; }

    //
    // Returns true if specified symbol is the current class symbol.
    //
    public boolean isCurrentClassSymbol(Symbol sym) { return sym == currentClassSym; }

    //
    // Returns the var count for the current class.
    //
    public int getClassVarCount() { return classVarCount; }

    //
    // Returns the var count for the current script.
    //
    public int getScriptVarCount() { return scriptVarCount; }

    //
    // Returns the translatedAttrInfo for the current class.
    //
    public List<TranslatedVarInfo> getTranslatedAttrInfo() { return translatedAttrInfo; }

    //
    // Returns the translatedOverrideAttrInfo for the current class.
    //
    public List<TranslatedOverrideClassVarInfo> getTranslatedOverrideAttrInfo() {
        return translatedOverrideAttrInfo;
    }

    //
    // Returns the translatedFuncInfo for the current class.
    //
    public List<TranslatedFuncInfo> getTranslatedFuncVarInfo() {
        return translatedFuncInfo;
    }

    //
    // Returns the resulting list of class vars.
    //
    public List<VarInfo> classVarInfos() {
        return classVarInfos.toList();
    }

    //
    // Returns the resulting list of script vars.
    //
    public List<VarInfo> scriptVarInfos() {
        return scriptVarInfos.toList();
    }

    //
    // Returns the resulting list of class funcs.
    //
    public List<FuncInfo> classFuncInfos() {
        return classFuncInfos.toList();
    }

    //
    // Returns the resulting list of script func.
    //
    public List<FuncInfo> scriptFuncInfos() {
        return scriptFuncInfos.toList();
    }

    //
    // Returns the resulting list of methods needing $impl dispatching.
    //
    public List<MethodSymbol> needDispatch() {
        ListBuffer<MethodSymbol> meths = ListBuffer.lb();

        for (MethodSymbol mSym : needDispatchMethods.values()) {
            meths.append( mSym );
        }

        return meths.toList();
    }

    //
    // Returns true if the type is a valid class worthy of analysis.
    //
    private boolean isValidClass(Type type) {
        return type != null && type.tsym != null && type.tsym.kind == Kinds.TYP;
    }

    //
    // Returns true if the class (or current class) is a mixin.
    //
    public boolean isMixinClass() {
        return isMixinClass(currentClassSym);
    }
    public static boolean isMixinClass(Symbol cSym) {
        return (cSym.flags() & JavafxFlags.MIXIN) != 0;
    }

    //
    // Returns true if the class is a Interface.
    //
    public boolean isInterface(Symbol cSym) {
        return (cSym.flags() & Flags.INTERFACE) != 0;
    }

    //
    // Returns true if the class is anon (synthetic.)
    //
    public boolean isAnonClass() {
        return isAnonClass(currentClassSym);
    }
    public boolean isAnonClass(Symbol cSym) {
        final long flags = (Flags.SYNTHETIC | Flags.FINAL);
        return (cSym.flags() & flags) == flags;
    }

    //
    // Returns null or the superclass symbol if it is a javafx class.
    //
    public ClassSymbol getFXSuperClassSym() { return superClassSym; }

    //
    // Returns resulting list of all superclasses in top down order.
    //
    public List<ClassSymbol> getSuperClasses() { return superClasses.toList(); }

    //
    // Returns resulting list of immediate mixin classes in left to right order.
    //
    public List<ClassSymbol> getImmediateMixins() { return immediateMixins.toList(); }

    //
    // Returns resulting list of all mixin classes in top down order.
    //
    public List<ClassSymbol> getAllMixins() { return allMixins.toList(); }

    //
    // This method analyzes the current javafx class.
    //
    private void analyzeCurrentClass() {
        // Make sure we don't recursively redo this class.
        addedBaseClasses.add(currentClassSym);

        // Process up the super class chain first.
        Type superType = currentClassSym.getSuperclass();

        // Make sure the super is a valid java class (is this always true?)
        if (isValidClass(superType)) {
            // Analyze the super class, but note that we don't want to clone
            // anything in the super chain.
            analyzeClass(superType.tsym, true, false);
        }

        // Track the current vars to the instance attribute results.
        for (TranslatedVarInfo tai : translatedAttrInfo) {
            // Track the var for overrides and mixin duplication.
            visitedAttributes.put(tai.getName(), tai);
        }

        // Track the override vars to the instance attribute results.
        for (TranslatedOverrideClassVarInfo tai : translatedOverrideAttrInfo) {
            // Find the overridden var.
            VarInfo oldVarInfo = visitedAttributes.get(tai.getName());

            // Test because it's possible to find the override before the mixin.
            if (oldVarInfo != null) {
                // Proxy to the overridden var.
                tai.setProxyVar(oldVarInfo);
            }

            // Track the var for overrides and mixin duplication.
            visitedAttributes.put(tai.getName(), tai);
        }

        // Map the current methods so they are filtered out of the results.
        for (TranslatedFuncInfo func : translatedFuncInfo) {
            MethodSymbol method = func.getSymbol();
            visitedMethods.put(methodSignature(method), method);
        }

        // Lastly, insert any mixin vars and methods from the interfaces.
        for (JFXExpression supertype : currentClassDecl.getSupertypes()) {
            // This will technically only analyze mixin classes.
            // We also want to clone all mixin vars amnd methods.
            analyzeClass(supertype.type.tsym, true, true);
        }

        // Add the current vars to the var results.
        // JFXC-3043 - This needs to be done after mixins.
        for (TranslatedVarInfo tai : translatedAttrInfo) {
            if (tai.isStatic()) {
                scriptVarInfos.append(tai);
            } else {
                classVarInfos.append(tai);
            }
        }

        // Add the override vars to the var results.
        // JFXC-3043 - This needs to be done after mixins.
        for (TranslatedOverrideClassVarInfo tai : translatedOverrideAttrInfo) {
            if (tai.isStatic()) {
                scriptVarInfos.append(tai);
            } else {
                classVarInfos.append(tai);
            }
        }

        // Add the functions to the function results.
        // JFXC-3043 - This needs to be done after mixins.
        for (TranslatedFuncInfo tfi : translatedFuncInfo) {
            if (tfi.isStatic()) {
                scriptFuncInfos.append(tfi);
            } else {
                classFuncInfos.append(tfi);
            }
        }
    }

    private void analyzeClass(Symbol sym, boolean isImmediateSuper, boolean needsCloning) {
        // Ignore pure java interfaces, classes we've visited before and non-javafx classes.
        if (!isInterface(sym) && !addedBaseClasses.contains(sym) && types.isJFXClass(sym)) {
            // Get the current class symbol and add it to the visited map.
            ClassSymbol cSym = (ClassSymbol) sym;
            addedBaseClasses.add(cSym);

            // Only continue cloning up the hierarchy if this is a mixin class.
            boolean isMixinClass = isMixinClass(cSym);
            needsCloning = needsCloning && isMixinClass;

            // Process up the super class chain first.
            Type superType = cSym.getSuperclass();
            if (isValidClass(superType)) {
                // Analyze the super class, but note that we don't want to clone
                // anything in the super chain.
                analyzeClass(superType.tsym, false, false);
            }

            // Get the class decl.  This will be null if read from a .class file.
            JFXClassDeclaration cDecl = types.getFxClass(cSym);

            // Scan for var and method members in the class.
            if (cDecl == null) {
                // Class loaded from .class file.
                if (cSym.members() != null) {
                    // Scope information is held in reverse order of declaration.
                    ListBuffer<Symbol> reversed = ListBuffer.lb();
                    for (Entry e = cSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                        reversed.prepend(e.sym);
                    }

                    // Scan attribute/method members.
                    for (Symbol mem : reversed) {
                        if (mem.kind == Kinds.MTH) {
                            // Method member.
                            MethodSymbol meth = (MethodSymbol) mem;

                            // Workaround for JFXC-3040 - Compile failure building
                            // runtime/javafx-ui-controls/javafx/scene/control/Button.fx
                            if (!needsCloning) continue;

                            // Filter out methods generated by the javac compiler.
                            if (filterMethods(meth)) {
                                processMethod(meth, needsCloning);
                            }
                        } else if (mem instanceof VarSymbol) {
                            // Attribute member.
                            VarSymbol var = (VarSymbol)mem;
                            processAttribute(var, cSym, needsCloning);
                        }
                    }
                }

                // Lastly, insert any mixin vars and methods from the interfaces.
                for (Type supertype : cSym.getInterfaces()) {
                    ClassSymbol iSym = (ClassSymbol) supertype.tsym;
                    analyzeClass(iSym, isImmediateSuper && isMixinClass, needsCloning);
                }
            } else {
                // Class is in current compile unit.

                // Scan attribute/method members.
                for (JFXTree def : cDecl.getMembers()) {
                    if (def.getFXTag() == JavafxTag.VAR_DEF) {
                        // Attribute member.
                        VarSymbol var = (VarSymbol)((JFXVar)def).sym;
                        processAttribute(var, cDecl.sym, needsCloning);
                    } else if (def.getFXTag() == JavafxTag.FUNCTION_DEF) {
                        // Method member.
                        MethodSymbol meth = (MethodSymbol)((JFXFunctionDefinition)def).sym;
                        processMethod(meth, needsCloning);
                    }
                }

                // Lastly, insert any mixin vars and methods from the interfaces.
                for (JFXExpression supertype : cDecl.getSupertypes()) {
                    analyzeClass(supertype.type.tsym, isImmediateSuper && isMixinClass, needsCloning);
                }
            }

            // Record the superclass in top down order.
            recordClass(cSym, isImmediateSuper);
        }
    }

    //
    // Predicate method indicates if the method should be include in processing.
    // Should filter out unrelated methods generated by the javac compiler.
    //
    private boolean filterMethods(MethodSymbol meth) {
        Name name = meth.name;
        return name != names.init;
    }

    //
    // Record the superclasses and mixins in top down order.
    //
    private void recordClass(ClassSymbol cSym, boolean isImmediateSuper) {
        // Make a distinction between superclasses and mixins.
        if (isMixinClass(cSym)) {
            // Record immediate mixin classes in left to right order.
            if (isImmediateSuper) {
                immediateMixins.append(cSym);
            }

            // Record all mixin classes in top down order.
            allMixins.append(cSym);
        } else {
            // Record the immediate superclass.
            if (isImmediateSuper) {
                superClassSym = cSym;
            }

            // Record all superclasses in top down order.
            superClasses.append(cSym);
        }
    }


    //
    // This method determines if a method should be added to the list of methods
    // needing $impl dispatch.  This method is only called for inherited methods.
    //
    private void processMethod(MethodSymbol newMethod, boolean needsCloning) {
        long flags = newMethod.flags();

        // Only look at real instance methods.
        if ((flags & (Flags.SYNTHETIC | Flags.ABSTRACT | Flags.STATIC)) == 0) {
            // Generate a name/signature string for uniqueness.
            String nameSig = methodSignature(newMethod);
            // Look to see if we've seen a method like this before.
            MethodSymbol oldMethod = visitedMethods.get(nameSig);
            // See if the current method is a mixin.
            boolean newIsMixin = isMixinClass(newMethod.owner);
            // See if the previous methods is a mixin.
            boolean oldIsMixin = oldMethod != null && isMixinClass(oldMethod.owner);

            // Are we are still cloning this far up the hierarchy?
            if (needsCloning) {
                // If the method didn't occur before or is a real method overshadowing a prior mixin.
                if (oldMethod == null || (oldIsMixin && !newIsMixin)) {
                    // Add to the methods needing $impl dispatch.
                    needDispatchMethods.put(nameSig, newMethod);
                }
            }

            // Map the fact we've seen this name/signature.
            visitedMethods.put(nameSig, newMethod);
        }
    }

    //
    // This method determines if the var needs to be handled in the current class.
    // This method is only called for inherited attributes.
    //
    private void processAttribute(VarSymbol var, ClassSymbol cSym, boolean needsCloning) {
        boolean isStatic = (var.flags() & Flags.STATIC) != 0;

        // If the var is in a class and not a static (ie., an instance attribute.)
        if (var.owner.kind == Kinds.TYP && !isStatic) {
            // See if we've seen this class before.
            Name attrName = var.name;
            VarInfo oldVarInfo = visitedAttributes.get(attrName);

            // If we've seen this class before, it must be the same symbol and type,
            // otherwise in doesn't conflict.
            if (oldVarInfo != null &&
                (oldVarInfo.getSymbol() != var ||
                 oldVarInfo.getSymbol().type != var.type)) {
                oldVarInfo = null;
            }

            // Is the var in a mixin class and needs cloning.
            boolean newIsMixin = isMixinClass(var.owner);
            if (newIsMixin && needsCloning) {
                // Only process the mixin var if we've not seen it before.
                if (oldVarInfo == null) {
                    // Construct a new mixin VarInfo.
                    MixinClassVarInfo newVarInfo = new MixinClassVarInfo(diagPos, var, typeMorpher.varMorphInfo(var));
                    // Add the new mixin VarInfo to the result list.
                    classVarInfos.append(newVarInfo);
                    // Map the fact we've seen this var.
                    visitedAttributes.put(attrName, newVarInfo);
                }
            } else {
                // Construct a new superclass VarInfo.
                SuperClassVarInfo newVarInfo = new SuperClassVarInfo(diagPos, var, typeMorpher.varMorphInfo(var));
                // Add the new superclass VarInfo to the result list.
                classVarInfos.append(newVarInfo);
                // Map the fact we've seen this var.
                visitedAttributes.put(attrName, newVarInfo);
            }
        }
    }

    //
    // This method constructs a name/signature string for the supplied method
    // symbol.
    //
    private String methodSignature(MethodSymbol meth) {
        StringBuilder nameSigBld = new StringBuilder();
        nameSigBld.append(meth.name.toString());
        nameSigBld.append(":");
        nameSigBld.append(meth.getReturnType().toString());
        nameSigBld.append(":");
        for (VarSymbol param : meth.getParameters()) {
            nameSigBld.append(param.type.toString());
            nameSigBld.append(":");
        }
        return nameSigBld.toString();
    }

    //
    // This method can be used to dump the state of a VarInfo or subclass.
    // The before flag cam be used to dump the VarInfo supplied by the
    // JavafxInitializationBuilder.
    //
    private void printAnalysis(boolean before) {
        System.out.println("Analyzed : " + currentClassSym);

        if (before) {
            System.out.println("  translatedAttrInfo");
            for (TranslatedVarInfo ai : translatedAttrInfo) ai.printInfo();
            System.out.println("  translatedOverrideAttrInfo");
            for (TranslatedOverrideClassVarInfo ai : translatedOverrideAttrInfo) ai.printInfo();
        }

        System.out.println("  superClass");
        System.out.println("    " + superClassSym);
        System.out.println("  superClasses");
        for (ClassSymbol cs : superClasses) System.out.println("    " + cs);
        System.out.println("  immediate mixins");
        for (ClassSymbol cs : immediateMixins) System.out.println("    " + cs);
         System.out.println("  all mixins");
        for (ClassSymbol cs : allMixins) System.out.println("    " + cs);

        System.out.println("  classVarInfos");
        for (VarInfo ai : classVarInfos) ai.printInfo();
        System.out.println("  scriptVarInfos");
        for (VarInfo ai : scriptVarInfos) ai.printInfo();
        System.out.println("  classFuncInfos");
        for (FuncInfo fi : classFuncInfos) fi.printInfo();
        System.out.println("  scriptFuncInfos");
        for (FuncInfo fi : scriptFuncInfos) fi.printInfo();
        System.out.println("  needDispatchMethods");
        for (MethodSymbol m : needDispatch()) {
            System.out.println("    " + m + ", owner=" + m.owner);
        }
        System.out.println();
        System.out.println();
    }

}
