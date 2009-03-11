/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

import static com.sun.tools.javac.code.Flags.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class JavafxAnalyzeClass {

    private final DiagnosticPosition diagPos;
    private final ClassSymbol currentClassSym;
    private final ListBuffer<VarInfo> attributeInfos = ListBuffer.lb();
    private final Map<String,MethodSymbol> needDispatchMethods = new HashMap<String, MethodSymbol>();
    private final Map<Name, VarInfo> visitedAttributes = new HashMap<Name, VarInfo>();
    private final Set<Symbol> addedBaseClasses = new HashSet<Symbol>();
    private final List<TranslatedVarInfo> translatedAttrInfo;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxClassReader reader;
    private final JavafxTypeMorpher typeMorpher;

    static class VarInfo {
        private final DiagnosticPosition diagPos;
        private final VarSymbol sym;
        private final VarMorphInfo vmi;
        private final Name name;
        private final JCStatement initStmt;
        private final boolean isDirectOwner;
        private boolean needsCloning;
        private VarInfo proxyVar;
        
        private VarInfo(DiagnosticPosition diagPos, Name name, VarSymbol attrSym, VarMorphInfo vmi,
                JCStatement initStmt, boolean isDirectOwner) {
            this.diagPos = diagPos;
            this.name = name;
            this.sym = attrSym;
            this.vmi = vmi;
            this.initStmt = initStmt;
            this.isDirectOwner = isDirectOwner;
        }

        public VarSymbol getSymbol() {
            return sym;
        }

        public DiagnosticPosition pos() {
            return diagPos;
        }

        public Type getRealType() {
            return vmi.getRealType();
        }

        public Type getVariableType() {
            return vmi.getVariableType();
        }

        public Type getLocationType() {
            return vmi.getLocationType();
        }

        public Type getElementType() {
            return vmi.getElementType();
        }

        public Name getName() {
            return name;
        }

        public String getNameString() {
            return name.toString();
        }

        public long getFlags() {
            return sym.flags();
        }
        
        private void setNeedsCloning(boolean needs) {
            needsCloning = needs;
        }
        
        public boolean needsCloning() {
            return needsCloning;
        }
        
        public VarInfo proxyVar() {
            return proxyVar;
        }

        public boolean hasProxyVar() {
            return proxyVar != null;
        }
        
        public VarSymbol proxyVarSym() {
            return hasProxyVar() ? proxyVar.sym : sym;
        }

        public void setProxyVar(VarInfo proxyVar) {
            this.proxyVar = proxyVar;
        }

        public boolean isStatic() {
            return (getFlags() & Flags.STATIC) != 0;
        }

        public boolean isDef() {
            return (getFlags() & JavafxFlags.IS_DEF) != 0;
        }

        public boolean isMixinVar() {
            return (sym.owner.flags() & JavafxFlags.MIXIN) != 0;
        }

        public VarMorphInfo getVMI() {
            return vmi;
        }

        public boolean isDirectOwner() {
            return isDirectOwner;
        }
        
        public JCStatement getDefaultInitStatement() {
            return initStmt;
        }
        
        JFXOnReplace onReplace() { return null; }
        
        JCBlock onReplaceTranslatedBody() { return null; }

        @Override
        public String toString() {
            return getNameString();
        }
    }
        
    static class TranslatedVarInfo extends VarInfo {
        final JFXVar var;
        private final JFXOnReplace onReplace;
        private final JCBlock onReplaceTranslatedBody;
        TranslatedVarInfo(JFXVar var, VarMorphInfo vmi,
                JCStatement initStmt, JFXOnReplace onReplace, JCBlock onReplaceTranslatedBody) {
            super(var.pos(), var.sym.name, var.sym, vmi, initStmt, true);
            this.var = var;
            this.onReplace = onReplace;
            this.onReplaceTranslatedBody = onReplaceTranslatedBody;
        }
        
        @Override
        public boolean needsCloning() {
            return true; // these are from current class, so always need cloning
        }
        
        @Override
        JFXOnReplace onReplace() { return onReplace; }
        @Override
        JCBlock onReplaceTranslatedBody() { return onReplaceTranslatedBody; }       
    }  
    
  
    static class TranslatedOverrideClassVarInfo extends VarInfo {
        private final JFXOnReplace onReplace;
        private final JCBlock onReplaceTranslatedBody;
        TranslatedOverrideClassVarInfo(JFXOverrideClassVar override,
                 VarMorphInfo vmi,
                JCStatement initStmt, JFXOnReplace onReplace, JCBlock onReplaceTranslatedBody) {
            super(override.pos(), override.sym.name, override.sym, vmi, initStmt, true);
            this.onReplace = onReplace;
            this.onReplaceTranslatedBody = onReplaceTranslatedBody;
        }
        
        @Override
        JFXOnReplace onReplace() { return onReplace; }
        @Override
        JCBlock onReplaceTranslatedBody() { return onReplaceTranslatedBody; }       
    }
     
    JavafxAnalyzeClass(DiagnosticPosition diagPos,
            ClassSymbol currentClassSym,
            List<TranslatedVarInfo> translatedAttrInfo,
            List<TranslatedOverrideClassVarInfo> translatedOverrideAttrInfo,
            Name.Table names,
            JavafxTypes types,
            JavafxClassReader reader,
            JavafxTypeMorpher typeMorpher) {
        this.names = names;
        this.types = types;
        this.reader = reader;
        this.typeMorpher = typeMorpher;
        this.diagPos = diagPos;
        this.currentClassSym = currentClassSym;
        
        this.translatedAttrInfo = translatedAttrInfo;
        for (TranslatedVarInfo tai : translatedAttrInfo) {
            visitedAttributes.put(tai.getName(), tai);
        }
        for (TranslatedOverrideClassVarInfo tai : translatedOverrideAttrInfo) {
            visitedAttributes.put(tai.getName(), tai);
        }

        // do the analysis
        process(currentClassSym, true);
        
        if (false) {
            System.out.println("process : " + currentClassSym);
            
            System.out.println("  translatedAttrInfo");
            for (TranslatedVarInfo ai : translatedAttrInfo) {
                System.out.println("    " + ai.getSymbol() +
                                   ", owner=" + ai.getSymbol().owner +
                                   ", static=" + ai.isStatic() +
                                   ", needsCloning=" + ai.needsCloning() + 
                                   ", isDef=" + ai.isDef() +
                                   ", getDefaultInitStatement=" + (ai.getDefaultInitStatement() != null) +
                                   ", isDirectOwner=" + ai.isDirectOwner());
            }
            
            System.out.println("  translatedOverrideAttrInfo");
            for (TranslatedOverrideClassVarInfo ai : translatedOverrideAttrInfo) {
                System.out.println("    " + ai.getSymbol() +
                                   ", owner=" + ai.getSymbol().owner +
                                   ", static=" + ai.isStatic() +
                                   ", needsCloning=" + ai.needsCloning() + 
                                   ", isDef=" + ai.isDef() +
                                   ", getDefaultInitStatement=" + (ai.getDefaultInitStatement() != null) +
                                   ", isDirectOwner=" + ai.isDirectOwner());
            }
            
            System.out.println("  attributeInfos");
            for (VarInfo ai : attributeInfos) {
                System.out.println("    " + ai.getSymbol() +
                                   ", owner=" + ai.getSymbol().owner +
                                   ", static=" + ai.isStatic() +
                                   ", needsCloning=" + ai.needsCloning() + 
                                   ", isDef=" + ai.isDef() +
                                   ", getDefaultInitStatement=" + (ai.getDefaultInitStatement() != null) +
                                   ", isDirectOwner=" + ai.isDirectOwner());
            }
            
            System.out.println("  needDispatchMethods");
            for (MethodSymbol m : needDispatch()) {
                System.out.println("    " + m);
            }
             
            System.out.println();
            System.out.println();
        }
    }

    public List<VarInfo> instanceAttributeInfos() {
        return attributeInfos.toList();
    }

    public List<VarInfo> staticAttributeInfos() {
        ListBuffer<VarInfo> ais = ListBuffer.lb();
        for (VarInfo ai : translatedAttrInfo) {
            if (ai.isStatic()) {
                ais.append( ai );
            }
        }
        return ais.toList();
    }

    public List<MethodSymbol> needDispatch() {
        long flags = currentClassSym.flags();
        boolean isMixin = (flags & JavafxFlags.MIXIN) != 0;
        
        ListBuffer<MethodSymbol> meths = ListBuffer.lb();
        for (MethodSymbol mSym : needDispatchMethods.values()) {
            // Process only methods that are being mixed in or members
            // of a superclass.
            if (isMixin || mSym.owner != currentClassSym) {
                meths.append( mSym );
            }
        }
        
        return meths.toList();
    }

    private void process(Symbol sym, boolean cloneVisible) {
        if (!addedBaseClasses.contains(sym) && types.isJFXClass(sym)) {
            ClassSymbol cSym = (ClassSymbol) sym;
            JFXClassDeclaration cDecl = types.getFxClass(cSym);
            long flags = cSym.flags();
            boolean isMixin = (flags & JavafxFlags.MIXIN) != 0;
            boolean isCurrent = cSym == currentClassSym;
            
            addedBaseClasses.add(cSym);
           
            if (!isMixin && !isCurrent) {
                // Don't clone members of base classes or interfaces.
                cloneVisible = false; 
            }

            // Process the base class first.
            Type superType = cSym.getSuperclass();
            if (superType != null && superType.tsym != null && superType.tsym.kind == Kinds.TYP) {
                process(superType.tsym, cloneVisible);
            }

            // get the corresponding AST, null if from class file
            if (cDecl == null) {
                if ((cSym.flags_field & Flags.INTERFACE) == 0 && cSym.members() != null) {
                    //TODO: fiz this hack back to the above. for some reason the order of symbols within a scope is inverted
                    ListBuffer<Symbol> reversed = ListBuffer.lb();
                    for (Entry e = cSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                        reversed.prepend(e.sym);
                    }
                    for (Symbol mem : reversed) {
                        if (mem.kind == Kinds.MTH)
                            processMethodFromClassFile((MethodSymbol) mem, cSym, cloneVisible);
                        else if (mem instanceof VarSymbol)
                            processAttribute((VarSymbol) mem, cSym, cloneVisible);
                    }
                }
                
                for (Type supertype : cSym.getInterfaces()) {
                    ClassSymbol iSym = (ClassSymbol) supertype.tsym;
                    process(iSym, cloneVisible);
                }
            } else {
                for (JFXTree def : cDecl.getMembers()) {
                    if (def.getFXTag() == JavafxTag.VAR_DEF) {
                        processAttribute((VarSymbol)(((JFXVar) def).sym), cDecl.sym, cloneVisible);
                    } else if (cloneVisible && def.getFXTag() == JavafxTag.FUNCTION_DEF) {
                        processMethod(((JFXFunctionDefinition) def).sym);
                    }
                }
                
                for (JFXExpression supertype : cDecl.getSupertypes()) {
                    process(supertype.type.tsym, cloneVisible);
            }
        }
    }
    }

    private void processMethodFromClassFile(MethodSymbol meth, ClassSymbol cSym, boolean cloneVisible) {
        if (cloneVisible && meth.name != names.init) {
            processMethod(meth);
        }
    }

     private void processMethod(MethodSymbol meth) {
        // No dispatch methods for abstract or static functions,
        // and for methods from non-mixin classes (this test is not redundant 
        // since the current class is allowed through if non-mixin)
        if ((meth.flags() & (Flags.SYNTHETIC | Flags.ABSTRACT | Flags.STATIC)) == 0) {
            String nameSig = methodSignature(meth);
            boolean isMixin = (meth.owner.flags() & JavafxFlags.MIXIN) != 0;
            // Filter out non mixins and duplicates.
            if (!(isMixin && needDispatchMethods.containsKey(nameSig))) {
                // because we traverse super-to-sub class, last one wins
                needDispatchMethods.put(nameSig, meth);
            }
        }
    }

    private void processAttribute(VarSymbol var, ClassSymbol cSym, boolean cloneVisible) {
        if (var.owner.kind == Kinds.TYP && (var.flags() & Flags.STATIC) == 0) {
            Name attrName = var.name;
            VarInfo oldAttrInfo = visitedAttributes.get(attrName);
            
            // If attribute is a duplicate then select mixins with caution.
            boolean needsProxy = false;
            if (oldAttrInfo != null) {
                boolean oldIsMixin = oldAttrInfo.isMixinVar();
                boolean newIsMixin = (var.owner.flags() & JavafxFlags.MIXIN) != 0;
                needsProxy = !oldIsMixin && newIsMixin;
            }
            
            // normal override
            VarInfo newAttrInfo = oldAttrInfo;
            if (oldAttrInfo == null || oldAttrInfo.getSymbol() != var) {
                newAttrInfo = new VarInfo(diagPos, attrName, var, typeMorpher.varMorphInfo(var), null,
                                          var.owner == currentClassSym);
            }
            
            if (needsProxy) newAttrInfo.setProxyVar(oldAttrInfo);
            
            newAttrInfo.setNeedsCloning(cloneVisible || newAttrInfo.isDirectOwner());
            attributeInfos.append(newAttrInfo);
            visitedAttributes.put(attrName, newAttrInfo);
        }
    }

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
}
