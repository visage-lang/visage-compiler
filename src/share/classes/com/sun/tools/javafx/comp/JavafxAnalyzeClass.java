package com.sun.tools.javafx.comp;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Position;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.util.MsgSym;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class JavafxAnalyzeClass {

    private final DiagnosticPosition diagPos;
    private final ClassSymbol currentClassSym;
    private final ListBuffer<AttributeInfo> attributeInfos = ListBuffer.lb();
    private final Map<String,MethodSymbol> needDispatchMethods = new HashMap<String, MethodSymbol>();
    private final Map<Name, AttributeInfo> translatedAttributes = new HashMap<Name, AttributeInfo>();
    private final Map<Name, AttributeInfo> visitedAttributes = new HashMap<Name, AttributeInfo>();
    private final Set<Symbol> addedBaseClasses = new HashSet<Symbol>();
    private final List<TranslatedAttributeInfo> translatedAttrInfo;
    private final Log log;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxClassReader reader;
    private final JavafxTypeMorpher typeMorpher;

    static class AttributeInfo {
        private final DiagnosticPosition diagPos;
        private final Symbol sym;
        private final VarMorphInfo vmi;
        private final Name name;
        private final JCStatement initStmt;
        private final boolean isDirectOwner;
        private boolean needsCloning;
        
        private AttributeInfo(DiagnosticPosition diagPos, Name name, Symbol attrSym, VarMorphInfo vmi,
                JCStatement initStmt, boolean isDirectOwner) {
            this.diagPos = diagPos;
            this.name = name;
            this.sym = attrSym;
            this.vmi = vmi;
            this.initStmt = initStmt;
            this.isDirectOwner = isDirectOwner;
        }

        public Symbol getSymbol() {
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
        
        // possibly confusing.  "needsCloning()" should be used for all known cases
        //public boolean isInCompoundClass() {
        //    return (sym.owner.flags() & JavafxFlags.COMPOUND_CLASS) != 0;
        //}
        
        private void setNeedsCloning(boolean needs) {
            needsCloning = needs;
        }
        
        public boolean needsCloning() {
            return needsCloning;
        }
        
        public boolean isStatic() {
            return (getFlags() & Flags.STATIC) != 0;
        }

        public VarMorphInfo getVMI() {
            return vmi;
        }

        public boolean isDirectOwner() {
            return isDirectOwner;
        }
        
        public JCStatement getDefaultInitializtionStatement() {
            return initStmt;
        }
        
        JFXOnReplace onReplace() { return null; }
        
        JCBlock onReplaceTranslatedBody() { return null; }

        @Override
        public String toString() {
            return getNameString();
        }
    }
    
    /*
    static class TranslatedAttributeInfo extends AttributeInfo {
        final JFXVar attribute;
        final List<JFXAbstractOnChange> onChanges;
        TranslatedAttributeInfo(JFXVar attribute, VarMorphInfo vmi,
                JCStatement initStmt, List<JFXAbstractOnChange> onChanges) {
            super(attribute.pos(), attribute.sym.name, attribute.sym, vmi, initStmt, true);
            this.attribute = attribute;
            this.onChanges = onChanges;
        }
        
        private void setNeedsCloning(boolean needs) {
            assert needs;
        }
        
        public boolean needsCloning() {
            return true; // these are from current class, so always need cloning
        }
        
    } */
    
    static class TranslatedAttributeInfo extends AttributeInfo {
        final JFXVar attribute;
        private final JFXOnReplace onReplace;
        private final JCBlock onReplaceTranslatedBody;
        TranslatedAttributeInfo(JFXVar attribute, VarMorphInfo vmi,
                JCStatement initStmt, JFXOnReplace onReplace, JCBlock onReplaceTranslatedBody) {
            super(attribute.pos(), attribute.sym.name, attribute.sym, vmi, initStmt, true);
            this.attribute = attribute;
            this.onReplace = onReplace;
            this.onReplaceTranslatedBody = onReplaceTranslatedBody;
        }
        
        private void setNeedsCloning(boolean needs) {
            assert needs;
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
    
    
    
  
    static class TranslatedOverrideAttributeInfo extends AttributeInfo {
        private final JFXOnReplace onReplace;
        private final JCBlock onReplaceTranslatedBody;
        TranslatedOverrideAttributeInfo(JFXOverrideClassVar override,
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
            List<TranslatedAttributeInfo> translatedAttrInfo,
            List<TranslatedOverrideAttributeInfo> translatedOverrideAttrInfo,
            Log log,
            Name.Table names,
            JavafxTypes types,
            JavafxClassReader reader,
            JavafxTypeMorpher typeMorpher) {
        this.log = log;
        this.names = names;
        this.types = types;
        this.reader = reader;
        this.typeMorpher = typeMorpher;
        this.diagPos = diagPos;
        this.currentClassSym = currentClassSym;
        
        this.translatedAttrInfo = translatedAttrInfo;
        for (TranslatedAttributeInfo tai : translatedAttrInfo) {
            translatedAttributes.put(tai.getName(), tai);
        }
        for (TranslatedOverrideAttributeInfo tai : translatedOverrideAttrInfo) {
            translatedAttributes.put(tai.getName(), tai);
        }

        // do the analysis
        process(currentClassSym, true);
        types.isCompoundClass(currentClassSym);
    }

    public List<AttributeInfo> instanceAttributeInfos() {
        return attributeInfos.toList();
    }

    public List<AttributeInfo> staticAttributeInfos() {
        ListBuffer<AttributeInfo> ais = ListBuffer.lb();
        for (AttributeInfo ai : translatedAttrInfo) {
            if (ai.isStatic()) {
                ais.append( ai );
            }
        }
        return ais.toList();
    }

    public List<MethodSymbol> needDispatch() {
        ListBuffer<MethodSymbol> meths = ListBuffer.lb();
        for (MethodSymbol mSym : needDispatchMethods.values()) {
            meths.append( mSym );
        }
        return meths.toList();
    }

    private void process(Symbol sym, boolean cloneVisible) {
        if (!addedBaseClasses.contains(sym) && types.isJFXClass(sym)) {
            ClassSymbol cSym = (ClassSymbol) sym;
            addedBaseClasses.add(cSym);
            JFXClassDeclaration cDecl = types.getFxClass(cSym);
            if (cSym != currentClassSym && (cSym.flags() & (JavafxFlags.COMPOUND_CLASS|Flags.INTERFACE)) == 0) {
                // this class is non-compound AND not the current class
                // needs to be recursively applied, non-compound in the chain blocks clonability
                cloneVisible = false; 
            }

            // get the corresponding AST, null if from class file
            if (cDecl == null) {
                for (Type supertype : cSym.getInterfaces()) {
                    ClassSymbol iSym = (ClassSymbol) supertype.tsym;
                    process(iSym, cloneVisible);
                    String iName = iSym.fullname.toString();
                    if (iName.endsWith(JavafxDefs.interfaceSuffix)) {
                        String sName = iName.substring(0, iName.length() - JavafxDefs.interfaceSuffix.length());
                        ClassSymbol sSym = reader.enterClass(names.fromString(sName));
                        process(sSym, cloneVisible);
                    }
                }
                if ((cSym.flags_field & Flags.INTERFACE) == 0 && cSym.members() != null) {
                    /***
                    for (Entry e = cSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                        if (e.sym.kind == Kinds.MTH) {
                            processMethodFromClassFile((MethodSymbol) e.sym, cSym, cloneVisible);
                        }
                    }
                     * ***/
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
            } else {
                for (JFXExpression supertype : cDecl.getSupertypes()) {
                    process(supertype.type.tsym, cloneVisible);
                }
                for (JFXTree def : cDecl.getMembers()) {
                    if (def.getFXTag() == JavafxTag.VAR_DEF) {
                        processAttribute(((JFXVar) def).sym, cDecl.sym, cloneVisible);
                    } else if (cloneVisible && def.getFXTag() == JavafxTag.FUNCTION_DEF) {
                        processMethod(((JFXFunctionDefinition) def).sym);
                    }
                }
            }
        }
    }

    private AttributeInfo addAttribute(Name attrName, Symbol sym, boolean needsCloning) {
        AttributeInfo attrInfo = translatedAttributes.get(attrName);
        if (attrInfo == null || attrInfo.getSymbol() != sym) {
            attrInfo = new AttributeInfo(diagPos, 
                    attrName,
                    sym, typeMorpher.varMorphInfo(sym), null, sym.owner == currentClassSym);
        }
        attrInfo.setNeedsCloning(needsCloning || attrInfo.isDirectOwner());
        attributeInfos.append(attrInfo);
        return attrInfo;
    }

    private void processMethodFromClassFile(MethodSymbol meth, ClassSymbol cSym, boolean cloneVisible) {
        if (cloneVisible && meth.name != names.init) {
            processMethod(meth);
        }
    }

     private void processMethod(MethodSymbol meth) {
        // no dispatch methods for abstract or static functions,
        // and none for methods from non-compound classes (this test is not redundant 
        // since the current class is allowed through if non-compound
        if ((meth.flags() & (Flags.SYNTHETIC | Flags.ABSTRACT | Flags.STATIC)) == 0  &&
                (meth.owner.flags() & JavafxFlags.COMPOUND_CLASS) != 0) {
            String nameSig = methodSignature(meth);
            needDispatchMethods.put(nameSig, meth);  // because we traverse super-to-sub class, last one wins
        }
    }

    private void processAttribute(VarSymbol var, ClassSymbol cSym, boolean cloneVisible) {
        if (var.owner.kind == Kinds.TYP && (var.flags() & Flags.STATIC) == 0) {
            Name attrName = var.name;
            if (visitedAttributes.containsKey(attrName)) {
                log.error(MsgSym.MESSAGE_JAVAFX_CANNOT_OVERRIDE_DEFAULT_INITIALIZER, attrName, cSym.name, visitedAttributes.get(attrName));
            } else {
                AttributeInfo ai = addAttribute(attrName, var, cloneVisible);
                if ((var.flags() & PRIVATE) == 0)
                    visitedAttributes.put(attrName, ai);
            }
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
