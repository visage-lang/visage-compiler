/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.List;

import com.sun.tools.javafx.tree.*;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JavafxInitializationBuilder {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private final JavafxTreeMaker make;
    private final Name.Table names;
    private final JavafxToJava toJava;
    private final JavafxSymtab syms;
    
    private final Name addChangeListenerName;
    private final Name changeListenerInterfaceName;
    private final Name sequenceChangeListenerInterfaceName;
    final Name initializerName;
    private final Name valueChangedName;
    private final Name classNameSuffix;
    private final Name interfaceNameSuffix;
    private final String attributeGetMethodNamePrefix = "get$";
    private final String attributeInitMethodNamePrefix = "init$";
    private final Name locationName;
    private final Name setDefaultsName;
    private final Name userInitName;
    private final Name receiverName;
    private final Name initializeName;
    
    private Map<ClassSymbol, JFXClassDeclaration> fxClasses;

    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        context.put(javafxInitializationBuilderKey, this);

        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        names = Name.Table.instance(context);
        toJava = JavafxToJava.instance(context);
        syms = (JavafxSymtab)(JavafxSymtab.instance(context));
        
        addChangeListenerName = names.fromString("addChangeListener");
        changeListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "ChangeListener");
        sequenceChangeListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "SequenceChangeListener");
        initializerName = names.fromString(JavafxModuleBuilder.initMethodString);
        valueChangedName = names.fromString("valueChanged");
        classNameSuffix = names.fromString("$Impl");
        interfaceNameSuffix = names.fromString("$Intf");
        locationName = names.fromString("location");
        setDefaultsName = names.fromString("setDefaults$");
        userInitName = names.fromString("userInit$");
        receiverName = names.fromString("receiver");
        initializeName = names.fromString("initialize$");
    }
    
    static class TranslatedAttributeInfo {
        private final JFXVar attribute;
        private final Type elemType;
        final JCExpression initExpr;
        final List<JFXAbstractOnChange> onChanges;
        TranslatedAttributeInfo(JFXVar attribute, JCExpression initExpr, List<JFXAbstractOnChange> onChanges) {
            this.attribute = attribute;
            this.initExpr = initExpr;
            this.onChanges = onChanges;
            this.elemType = attribute.type.getTypeArguments().head;
        }
        Name name() { return attribute.getName(); }
        Type type() { return attribute.type; }
        Type elemType() { return elemType; }
        DiagnosticPosition diagPos() { return attribute.pos(); }
    }
  
    /**
     * Non-destructively build the statements to fill-in the 
     * body of a translated class initializer method.
     * Incoming info MUST be translated into Java ASTs already
     */
    List<JCStatement> initializerMethodBody(
            JFXClassDeclaration classDecl,
            List<TranslatedAttributeInfo> attrInfo, 
            List<JCBlock> initBlocks) {
        ListBuffer<JCStatement> stmts = ListBuffer.<JCStatement>lb();
 
        // Initialize the default values for the atttributes.
        for (TranslatedAttributeInfo info : attrInfo) {
            if (info.initExpr != null) { // if there is an attribute initializer
                stmts.append(makeAttributeInitialization(info));
            }
        }

        for (JCBlock init : initBlocks) {
            stmts.append(init);   // insert the init blocks directly
        }

        // Now do the on change blocks
        for (TranslatedAttributeInfo info : attrInfo) {
            if (info.onChanges.length() > 0) { // if there is an on change clause
               stmts.append(makeChangeListenerCall(info));
            }
        }
        
        // Now add a call to notify changes for all attribute, so dependents 
        // initial values will be set
        makeOnChangedCall(classDecl, stmts);

        return stmts.toList();
    }  
    
    /**
     * Non-destructive creation of initialization code for an attribute
     */
    private JCStatement makeAttributeInitialization(TranslatedAttributeInfo info) {
        JCLiteral nullValue = make.Literal(TypeTags.BOT, null);
        JCIdent lhsIdent = make.Ident(info.name());
        JCBinary cond = make.Binary(JCTree.EQ, lhsIdent, nullValue);

        JCIdent lhsAssignIdent = make.Ident(info.name());
        JCAssign defValAssign = make.Assign(lhsAssignIdent, info.initExpr);
        JCExpressionStatement defAttrValue = make.Exec(defValAssign);
        return make.If(cond, defAttrValue, null);
    }
    
    /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    private JCStatement makeChangeListenerCall(TranslatedAttributeInfo info) {
        JFXOnReplace onReplace = null;
        JFXOnReplaceElement onReplaceElement = null;
        JFXOnInsertElement onInsertElement = null;
        JFXOnDeleteElement onDeleteElement = null;
        DiagnosticPosition diagPos = info.diagPos();
        ListBuffer<JCTree> defs = ListBuffer.<JCTree>lb();
        
        for (JFXAbstractOnChange onc : info.onChanges) {
            switch (onc.getTag()) {
                case JavafxTag.ON_REPLACE:
                    onReplace = (JFXOnReplace)onc;
                    break;
                case JavafxTag.ON_REPLACE_ELEMENT:
                    onReplaceElement = (JFXOnReplaceElement)onc;
                    break;
                case JavafxTag.ON_INSERT_ELEMENT:
                    onInsertElement = (JFXOnInsertElement)onc;
                    break;
                case JavafxTag.ON_DELETE_ELEMENT:
                    onDeleteElement = (JFXOnDeleteElement)onc;
                    break;
            }
        }
       
        // If there are any listenere, we need to build this, even if empty
        defs.append(makeChangeListenerMethod(
                        diagPos,
                        onReplace, 
                        "onChange",
                        List.<JCVariableDecl>nil(),
                        TypeTags.BOOLEAN));
        
        JCExpression changeListener = make.at(diagPos).Identifier(changeListenerInterfaceName);
        List<JCExpression> emptyTypeArgs = List.nil();
        if (onReplaceElement != null || onInsertElement != null || onDeleteElement != null) {
            changeListener = make.at(diagPos).Identifier(sequenceChangeListenerInterfaceName);
            changeListener = make.at(diagPos).TypeApply(changeListener, 
                    List.<JCExpression>of(toJava.makeTypeTree(info.elemType(), diagPos)));
            defs.append(makeChangeListenerMethod(
                    diagPos, 
                    onReplaceElement, 
                    "onReplace", 
                    List.<JCVariableDecl>of(
                        makeParam(diagPos, syms.intType, onReplaceElement.getIndex(), "$index$"), 
                        makeParam(diagPos, info.elemType(), onReplaceElement.getOldValue(), "$oldVallue$"), 
                        makeParam(diagPos, info.elemType(), null, "$newValue$")), 
                    TypeTags.VOID));
            defs.append(makeChangeListenerMethod(
                    diagPos, 
                    onInsertElement, 
                    "onInsert", 
                    List.<JCVariableDecl>of(
                        makeParam(diagPos, syms.intType, onInsertElement.getIndex(), "$index$"), 
                        makeParam(diagPos, info.elemType(), onInsertElement.getOldValue(), "$newValue$")), 
                    TypeTags.VOID));
            defs.append(makeChangeListenerMethod(
                    diagPos, 
                    onDeleteElement, 
                    "onDelete", 
                    List.<JCVariableDecl>of(
                        makeParam(diagPos, syms.intType, onDeleteElement.getIndex(), "$index$"), 
                        makeParam(diagPos, info.elemType(), onDeleteElement.getOldValue(), "$oldVallue$")), 
                    TypeTags.VOID));
        }
        JCNewClass anonymousChangeListener = make.NewClass(
                null, 
                emptyTypeArgs, 
                changeListener, 
                List.<JCExpression>nil(), 
                make.at(diagPos).AnonymousClassDef(make.Modifiers(0L), defs.toList()));

        JCIdent varIdent = make.at(diagPos).Ident(info.name());
        JCFieldAccess tmpSelect = make.at(diagPos).Select(varIdent, addChangeListenerName);

        List<JCExpression> args = List.<JCExpression>of(anonymousChangeListener);
        return make.at(diagPos).Exec(make.at(diagPos).Apply(emptyTypeArgs, tmpSelect, args));
    }
    
    private JCVariableDecl makeParam(DiagnosticPosition diagPos, Type type, JFXVar var, String nameDefault) {
        Name name;
        if (var != null) {
            name = var.getName();
            diagPos = var.pos();
        } else {
            name = names.fromString(nameDefault);
        }
        return make.at(diagPos).VarDef(
                make.Modifiers(Flags.PARAMETER),
                name,
                toJava.makeTypeTree(type, diagPos),
                null);
        
    }
    
    /**
     * construct a change listener method for insertion in a listener anon class.
     *   boolean onChange();
     *   void onInsert(...);
     *   void on Delete(...); ...
     */
    private JCMethodDecl makeChangeListenerMethod(
            DiagnosticPosition diagPos,
            JFXAbstractOnChange onChange, 
            String methodName, 
            List<JCVariableDecl> args, 
            int returnTypeTag) {
        ListBuffer<JCStatement> ocMethStmts = ListBuffer.<JCStatement>lb();
        if (onChange != null) {
            diagPos = onChange.pos();
            ocMethStmts.appendList(onChange.getBody().getStatements());
        }
        if (returnTypeTag == TypeTags.BOOLEAN) {
            ocMethStmts.append(make.at(diagPos).Return(make.at(diagPos).Literal(TypeTags.BOOLEAN, 1)));
        }

        return make.at(diagPos).MethodDef(
                make.at(diagPos).Modifiers(Flags.PUBLIC), 
                names.fromString(methodName), 
                make.at(diagPos).TypeIdent(returnTypeTag), 
                List.<JCTypeParameter>nil(), 
                args,
                List.<JCExpression>nil(), 
                make.at(diagPos).Block(0L, ocMethStmts.toList()), 
                null);
    }

    private void makeOnChangedCall(JFXClassDeclaration classDecl,
                                    ListBuffer<JCStatement> stmts) {
        for (JCTree tree : classDecl.defs) {
            if (tree.getTag() == JavafxTag.VAR_DEF) {
                JFXVar attrDef = (JFXVar)tree;
                DiagnosticPosition diagPos = attrDef.pos();
                JCIdent varIdent = make.at(diagPos).Ident(attrDef.name);
                JCFieldAccess tmpSelect = make.at(diagPos).Select(varIdent, valueChangedName);

                List<JCExpression> typeargs = List.nil();
                List<JCExpression> args = List.<JCExpression>nil();
                stmts = stmts.append(make.at(diagPos).Exec(make.at(diagPos).Apply(typeargs, tmpSelect, args)));
            }
        }
    }
    
    List<JCStatement> createJFXClassModel(JFXClassDeclaration cDecl, JavafxTypeMorpher typeMorpher) {
        Set<String> visitedClasses = new HashSet<String>();
        Map<String, VarSymbol> collectedAttributes = new HashMap<String, VarSymbol>();
        Map<String, MethodSymbol> collectedMethods = new HashMap<String, MethodSymbol>();
        java.util.List<VarSymbol> attributes = new java.util.ArrayList<VarSymbol>();
        java.util.List<MethodSymbol> methods = new java.util.ArrayList<MethodSymbol>();
        java.util.List<ClassSymbol> baseClasses = new java.util.ArrayList<ClassSymbol>();
        java.util.List<ClassSymbol> classesToVisit = new java.util.ArrayList<ClassSymbol>();
        
        classesToVisit.add(cDecl.sym);
        
        collectAttributesAndMethods(visitedClasses,
                                    collectedAttributes,
                                    collectedMethods,
                                    attributes,
                                    methods,
                                    baseClasses,
                                    classesToVisit);
        
        ListBuffer<JCStatement> ret = new ListBuffer<JCStatement>();
        
        ListBuffer<JCExpression> implementing = new ListBuffer<JCExpression>();
        implementing.append(make.Identifier("com.sun.javafx.runtime.FXObject"));
        ListBuffer<JCTree> iDefinitions = new ListBuffer<JCTree>();
        Map<JFXClassDeclaration, ListBuffer<VarMorphInfo>> attrsInfoMap = new HashMap<JFXClassDeclaration, ListBuffer<VarMorphInfo>>();
        ListBuffer<VarMorphInfo> attrInfos = new ListBuffer<VarMorphInfo>();
        attrsInfoMap.put(cDecl, attrInfos);
        for (JCTree def : cDecl.defs) {
            if (def.getTag() == JavafxTag.VAR_DEF) {
                if (((JFXVar)def).sym.owner.kind == Kinds.TYP) {
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(((JFXVar)def).sym);
                    vmi.shouldMorph();
                    attrInfos.append(vmi);
                }
            }
        }
        
        addInterfaceAttributeMethods(iDefinitions, attrInfos);
        Name interfaceName = names.fromString(cDecl.name.toString() + interfaceNameSuffix);
        JCClassDecl cInterface = make.ClassDef(make.Modifiers(cDecl.mods.flags | Flags.INTERFACE | Flags.ABSTRACT),
                interfaceName, 
                List.<JCTypeParameter>nil(), null, implementing.toList(), iDefinitions.toList());
        
        cDecl.implementing = cDecl.implementing.append(make.Ident(interfaceName));
        
        addClassAttributeMethods(cDecl, attrInfos);
        
        ret.append(cInterface);
        
        fxClasses = null;
        
        return ret.toList();
    }
    
    private void addInterfaceAttributeMethods(ListBuffer<JCTree> idefs, ListBuffer<VarMorphInfo> attrInfos) {
        for (VarMorphInfo attrInfo : attrInfos) {            
            idefs.append(make.MethodDef(
                    make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT),
                    names.fromString(attributeGetMethodNamePrefix + attrInfo.varSymbol.name.toString()),
                    toJava.makeTypeTree(attrInfo.getUsedType(), null),
                    List.<JCTypeParameter>nil(), 
                    List.<JCVariableDecl>nil(), 
                    List.<JCExpression>nil(), 
                    null, null));

            List<JCVariableDecl> locationVarDeclList = List.<JCVariableDecl>nil();
                locationVarDeclList = locationVarDeclList.append(make.VarDef(make.Modifiers(0L),
                    locationName, toJava.makeTypeTree(attrInfo.getUsedType(), null), null));
            idefs.append(make.MethodDef(
                    make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT),
                    names.fromString(attributeInitMethodNamePrefix + attrInfo.varSymbol.name.toString()),
                    toJava.makeTypeTree(syms.voidType, null),
                    List.<JCTypeParameter>nil(), 
                    locationVarDeclList, 
                    List.<JCExpression>nil(), 
                    null, null));
        }
    }

    private void addClassAttributeMethods(JFXClassDeclaration cdef, ListBuffer<VarMorphInfo> attrInfos) {
        for (VarMorphInfo attrInfo : attrInfos) { 
            List<JCStatement> stats = List.<JCStatement>nil();
            
            // Add the return stastement for the attribute
            JCBlock statBlock = make.Block(0L, stats);
            
            JCReturn returnStat = make.Return(make.Ident(attrInfo.varSymbol.name));
            stats = stats.append(returnStat);
            statBlock.stats = stats;
            
            // Add the method for this class' attributes
            cdef.defs = cdef.defs.append(make.MethodDef(
                    make.Modifiers(Flags.PUBLIC),
                    names.fromString(attributeGetMethodNamePrefix + attrInfo.varSymbol.name.toString()),
                    toJava.makeTypeTree(attrInfo.getUsedType(), null),
                    List.<JCTypeParameter>nil(), 
                    List.<JCVariableDecl>nil(), 
                    List.<JCExpression>nil(), 
                    statBlock, null));

            // Add the init$ method
            JCBlock initBlock = make.Block(0L, List.<JCStatement>nil());
            List<JCVariableDecl> locationVarDeclList = List.<JCVariableDecl>nil();
                locationVarDeclList = locationVarDeclList.append(make.VarDef(make.Modifiers(0L),
                    locationName, toJava.makeTypeTree(attrInfo.getUsedType(), null), null));
            cdef.defs = cdef.defs.append(make.MethodDef(
                    make.Modifiers(Flags.PUBLIC),
                    names.fromString(attributeInitMethodNamePrefix + attrInfo.varSymbol.name.toString()),
                    toJava.makeTypeTree(syms.voidType, null),
                    List.<JCTypeParameter>nil(), 
                    locationVarDeclList, 
                    List.<JCExpression>nil(), 
                    initBlock, null));
        }
        
        // Add the setDefaults$ method
        List<JCVariableDecl> receiverVarDeclList = List.<JCVariableDecl>nil();
        receiverVarDeclList = receiverVarDeclList.append(make.VarDef(make.Modifiers(Flags.FINAL),
                receiverName, make.Ident(names.fromString(cdef.name.toString() + interfaceNameSuffix.toString())), null));

        JCBlock setDefBlock = make.Block(0L, List.<JCStatement>nil());
        cdef.defs = cdef.defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC | Flags.STATIC),
                setDefaultsName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(), 
                receiverVarDeclList, 
                List.<JCExpression>nil(), 
                setDefBlock, null));

        // Add the userInit$ method
        receiverVarDeclList = List.<JCVariableDecl>nil();
        receiverVarDeclList = receiverVarDeclList.append(make.VarDef(make.Modifiers(Flags.FINAL),
                receiverName, make.Ident(names.fromString(cdef.name.toString() + interfaceNameSuffix.toString())), null));

        JCBlock userInitBlock = make.Block(0L, List.<JCStatement>nil());
        cdef.defs = cdef.defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC | Flags.STATIC),
                userInitName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(), 
                receiverVarDeclList, 
                List.<JCExpression>nil(), 
                userInitBlock, null));

        // Add the initialize$ method
        List<JCStatement> initializeStats = List.<JCStatement>nil();
// TODO: Disable for now..
// Ambiguity for the setDefault$ method if a class extends another class and there is a setDefasults$ in both classes        
//        initializeStats = initializeStats.append(toJava.callStatement(cdef.pos(), make.Ident(cdef.name)/*TODO: Add the class suffix*/, 
//            setDefaultsName.toString(), make.Ident(names._this)));
//        initializeStats = initializeStats.append(toJava.callStatement(cdef.pos(), make.Ident(cdef.name)/*TODO: Add the class suffix*/, 
//            userInitName.toString(), make.Ident(names._this)));
        // TODO: Add init helper calls...
        JCBlock initializeBlock = make.Block(0L, initializeStats);
        cdef.defs = cdef.defs.append(make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                initializeName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>nil(), 
                List.<JCExpression>nil(), 
                initializeBlock, null));
    }

    private void collectAttributesAndMethods(Set<String> visitedClasses,
                                             Map<String, VarSymbol> collectedAttributes,
                                             Map<String, MethodSymbol> collectedMethods,
                                             java.util.List<VarSymbol> attributes,
                                             java.util.List<MethodSymbol> methods,
                                             java.util.List<ClassSymbol> baseClasses,
                                             java.util.List<ClassSymbol> classesToVisit) {
        while(!classesToVisit.isEmpty()) {
            ClassSymbol cSym = classesToVisit.get(0);
            classesToVisit.remove(0);
            
            if (visitedClasses.contains(cSym.fullname.toString())) {
                if (isJFXClass(cSym)) {
                    if ((cSym.flags_field & Flags.INTERFACE) != 0) {
                        // TODO: Add the fields/methods based on XXLocation get$<fieldName>() method
                        // TODO: Add all the base interfaces to the classesToVisit List
                        visitedClasses.add(cSym.fullname.toString());
                    }
                    else {
                        JFXClassDeclaration cDecl = fxClasses.get(cSym);
                        if (cDecl != null) {
                            // TODO: Add the fields/methods of this class to the lists/maps
                            // TODO: Add all the base classes/interfaces to the classesToVisit List
                            visitedClasses.add(cSym.fullname.toString());
                        }
                    }
                }
            }
        }
    }
    
    private boolean isJFXClass(ClassSymbol cSym) {
        if ((cSym.flags_field & Flags.INTERFACE) != 0) {
            for (List<Type> intfs = cSym.getInterfaces(); intfs.nonEmpty(); intfs = intfs.tail) {
                if (intfs.head.tsym.type == syms.javafx_FXObjectType) {
                    return true;
                }
            }
        }
        else {
            if (fxClasses != null) {
                if (fxClasses.containsKey(cSym)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    void addFxClass(ClassSymbol csym, JFXClassDeclaration cdecl) {
        if (fxClasses == null) {
            fxClasses = new HashMap<ClassSymbol, JFXClassDeclaration>();
        }
        
        fxClasses.put(csym, cdecl);
    }
}

