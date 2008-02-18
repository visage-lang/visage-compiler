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

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

public class JavafxInitializationBuilder {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private final JavafxDefs defs;
    private final JavafxTreeMaker make;
    private final Log log;
    public final Name.Table names;
    private final JavafxToJava toJava;
    private final JavafxSymtab syms;
    private final JavafxTypes types;
    private final JavafxTypeMorpher typeMorpher;
    private final ClassReader reader;
    
    private final Name addChangeListenerName;
    private final Name[] changeListenerInterfaceName;
    private final Name sequenceReplaceListenerInterfaceName;
    private final Name sequenceChangeListenerInterfaceName;
    private static final String initHelperClassName = "com.sun.javafx.runtime.InitHelper";
    private final Name initAttributesName;
    private final Name applyDefaultName;
    private final Name addTriggersName;
    final Name userInitName;
    final Name postInitName;
    private final Name onChangeArgName1, onChangeArgName2;
    Name outerAccessorName;
    Name outerAccessorFieldName;
    
    final Type initHelperType;
    final Type abstractVariableType;
    
    public static JavafxInitializationBuilder instance(Context context) {
        JavafxInitializationBuilder instance = context.get(javafxInitializationBuilderKey);
        if (instance == null)
            instance = new JavafxInitializationBuilder(context);
        return instance;
    }

    protected JavafxInitializationBuilder(Context context) {
        context.put(javafxInitializationBuilderKey, this);

        defs = JavafxDefs.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        names = Name.Table.instance(context);
        toJava = JavafxToJava.instance(context);
        syms = (JavafxSymtab)(JavafxSymtab.instance(context));
        types = JavafxTypes.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        reader = ClassReader.instance(context);
        
        addChangeListenerName = names.fromString("addChangeListener");
        changeListenerInterfaceName = new Name[JavafxVarSymbol.TYPE_KIND_COUNT];
        for (int i=0; i< JavafxVarSymbol.TYPE_KIND_COUNT; i++)
            changeListenerInterfaceName[i]
                    = names.fromString(JavafxTypeMorpher.locationPackageName + JavafxVarSymbol.getTypePrefix(i) + "ChangeListener");
        sequenceReplaceListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "SequenceReplaceListener");
        sequenceChangeListenerInterfaceName = names.fromString(JavafxTypeMorpher.locationPackageName + "SequenceChangeListener");
        initAttributesName = names.fromString("initAttributes$");
        applyDefaultName = names.fromString("applyDefault$");
        addTriggersName = names.fromString("addTriggers$");
        userInitName = names.fromString("userInit$");
        postInitName = names.fromString("postInit$");
        onChangeArgName1 = names.fromString("$oldValue");
        onChangeArgName2 = names.fromString("$newValue");
        outerAccessorName = names.fromString("accessOuter$");
        outerAccessorFieldName = names.fromString("accessOuterField$");
        
        {
            Name name = Name.fromString(names, initHelperClassName);
            ClassSymbol sym = reader.enterClass(name);
            initHelperType = sym.type;
        }
        {
            Name name = Name.fromString(names, typeMorpher.locationPackageName + "AbstractVariable");
            ClassSymbol sym = reader.enterClass(name);
            abstractVariableType = types.erasure( sym.type );
        }
    }
    
    class AttributeInfo {
        private final DiagnosticPosition diagPos;
        private final Symbol sym;
        private final VarMorphInfo vmi;
        private final Name name;
        private final boolean isDirectOwner;
        
        AttributeInfo(DiagnosticPosition diagPos, Symbol attrSym, boolean isDirectOwner) {
            this.diagPos = diagPos;
            this.sym = attrSym;
            this.vmi = typeMorpher.varMorphInfo(attrSym);
            this.isDirectOwner = isDirectOwner;
            if (attrSym.kind == Kinds.VAR) {
                this.name = sym.name;
            } else {
                assert attrSym.kind == Kinds.MTH : "Invalid attribute type collected";
                this.name = names.fromString(attrSym.name.toString().substring(attributeGetMethodNamePrefix.length()));
            }
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

        public Type getMorphedType() {
            return vmi.getMorphedType();
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
        
        public boolean isStatic() {
            return (getFlags() & Flags.STATIC) != 0;
        }

        public VarMorphInfo getVMI() {
            return vmi;
        }

        public boolean isDirectOwner() {
            return isDirectOwner;
        }
        
        public JCExpression makeMorphedTypeTree(DiagnosticPosition diagPos) {
            return toJava.makeTypeTree(getMorphedType(), diagPos);
        }
    }
    
    TranslatedAttributeInfo translatedAttributeInfo(JFXVar attribute, 
                JCStatement initStmt, List<JFXAbstractOnChange> onChanges) {
        return new TranslatedAttributeInfo(attribute, initStmt, onChanges);
    }
    
    class TranslatedAttributeInfo extends AttributeInfo {
        final JFXVar attribute;
        final JCStatement initStmt;
        final List<JFXAbstractOnChange> onChanges;
        TranslatedAttributeInfo(JFXVar attribute, 
                JCStatement initStmt, List<JFXAbstractOnChange> onChanges) {
            super(attribute.pos(), attribute.sym, true);
            this.attribute = attribute;
            this.initStmt = initStmt;
            this.onChanges = onChanges;
        }
    }  
  
    TranslatedOverrideAttributeInfo translatedOverrideAttributeInfo(JFXOverrideAttribute override, JFXOnReplace onReplace) {
        return new TranslatedOverrideAttributeInfo(override, onReplace);
    }
    
    class TranslatedOverrideAttributeInfo extends AttributeInfo {
        private final JFXOnReplace onReplace;
        TranslatedOverrideAttributeInfo(JFXOverrideAttribute override, JFXOnReplace onReplace) {
            super(override.pos(), override.sym, true);
            this.onReplace = onReplace;
        }
        
        JFXOnReplace onReplace() { return onReplace; }
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

        JavafxClassModel(
                Name interfaceName,
                List<JCExpression> interfaces,
                List<JCTree> iDefinitions,
                List<JCTree> addedClassMembers,
                List<JCExpression> additionalImports) {
            this.interfaceName = interfaceName;
            this.interfaces = interfaces;
            this.iDefinitions = iDefinitions;
            this.additionalClassMembers = addedClassMembers;
            this.additionalImports = additionalImports;
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
           List<TranslatedAttributeInfo> translatedAttrInfo, 
           List<TranslatedOverrideAttributeInfo> translatedTriggerInfo) {
        boolean classOnly = cDecl.generateClassOnly();
        DiagnosticPosition diagPos = cDecl.pos();
        
        CollectAttributeAndMethods collection = new CollectAttributeAndMethods(diagPos, cDecl.sym);        
        List<ClassSymbol> javaInterfaces = collection.javaInterfaces;
        List<AttributeInfo> attributeInfos = collection.attributeInfos.toList();
        List<String> javafxClassNames = collection.javafxClassNames;
        
        ListBuffer<JCTree> cDefinitions = ListBuffer.lb();  // additional class members needed
        cDefinitions.append( makeConstructor(diagPos) );
        cDefinitions.appendList( makeAttributeFields(diagPos, attributeInfos) );
        cDefinitions.append( makeAttributeListField(diagPos, attributeInfos) );
        cDefinitions.append( makeTriggersMethod(diagPos, cDecl, javafxClassNames, translatedAttrInfo, translatedTriggerInfo) );
        cDefinitions.append( makeInitAttributesMethod(diagPos, cDecl, javafxClassNames, translatedAttrInfo) );
        cDefinitions.append( makeInitAttributesBlock(diagPos, cDecl, translatedAttrInfo) );
        cDefinitions.append( makeInitializeMethod(diagPos, cDecl) );
        cDefinitions.appendList( makeClassAttributeGetterMethods(diagPos, attributeInfos) );
        cDefinitions.appendList( makeClassOuterAccessorMembers(cDecl) );

        ListBuffer<JCTree> iDefinitions = ListBuffer.lb();
        iDefinitions.appendList( makeInterfaceAttributeGetterMethods(diagPos, attributeInfos) );
        iDefinitions.appendList( makeInterfaceOuterAccessorMembers(cDecl) );

        Name interfaceName = classOnly ? null : interfaceName(cDecl);

        //TODO: this needs to be cleanup, but that should wait for the "bound function" overhaul
        for (boolean bound = JavafxToJava.generateBoundFunctions;  ; bound = false) {

            // add interface methods for each method defined in this class
            for (MethodSymbol mth : collection.needInterfaceMethods) {
                    if (mth.owner == cDecl.sym) {
                        if (!bound || JavafxToJava.generateBoundVoidFunctions || mth.getReturnType() != syms.voidType) {
                            iDefinitions.append(makeMethod(cDecl, mth, null, bound));
                        }
                    }
            }

            // add proxies which redirect to the static implementation for every concrete method
            for (MethodSymbol mth : collection.needDispatchMethods) {
                    if ((!classOnly && (mth.flags() & (Flags.STATIC)) == 0L) || mth.owner != cDecl.sym) { //TODO: this test is wrong
                        if (!bound || JavafxToJava.generateBoundVoidFunctions || mth.getReturnType() != syms.voidType) {
                            cDefinitions.append(makeMethod(cDecl, mth, makeDispatchBody(cDecl, mth, bound, false), bound));
                        }
                    }
            }
            
            for (MethodSymbol mth : collection.needStaticDispatchMethods) {
                    if ((!classOnly && (mth.flags() & (Flags.STATIC)) == 0L) || mth.owner != cDecl.sym) {  //TODO: this test is wrong
                        if (!bound || JavafxToJava.generateBoundVoidFunctions || mth.getReturnType() != syms.voidType) {
                            cDefinitions.append(makeMethod(cDecl, mth, makeDispatchBody(cDecl, mth, bound, true), bound));
                        }
                    }
            }
            
            if (!bound) {
                break;
            }
        }


        return new JavafxClassModel(
                interfaceName, 
                makeImplementingInterfaces(diagPos, cDecl, javaInterfaces), 
                iDefinitions.toList(), 
                cDefinitions.toList(), 
                makeAdditionalImports(diagPos, javaInterfaces) );
    }
 
    private List<JCExpression> makeImplementingInterfaces(DiagnosticPosition diagPos, 
            JFXClassDeclaration cDecl, 
            List<ClassSymbol> baseInterfaces) {
        ListBuffer<JCExpression> implementing = ListBuffer.lb();
        implementing.append(make.at(diagPos).Identifier(fxObjectString));
        for (List<JCExpression> l = cDecl.getImplementing(); l.nonEmpty(); l = l.tail) {
            implementing.append(toJava.makeTypeTree(l.head.type, null));
        }

        for (ClassSymbol baseClass : baseInterfaces) {
                implementing.append(toJava.makeTypeTree(baseClass.type, diagPos, true));
        }
        return implementing.toList();
    }

    private List<JCExpression> makeAdditionalImports(DiagnosticPosition diagPos, List<ClassSymbol> baseInterfaces) {
        // Add import statements for all the base classes and basClass $Intf(s).
        // There might be references to them when the methods/attributes are rolled up.
        ListBuffer<JCExpression> additionalImports = new ListBuffer<JCExpression>();
        for (ClassSymbol baseClass : baseInterfaces) {
            if (baseClass.type != null && baseClass.type.tsym != null &&
                    baseClass.type.tsym.packge() != syms.unnamedPackage) {    // Work around javac bug. the visitImport of Attr 
                // is casting to JCFieldAcces, but if you have imported an
                // JCIdent only a ClastCastException is thrown.
                additionalImports.append(toJava.makeTypeTree(baseClass.type, diagPos, false));
                additionalImports.append(toJava.makeTypeTree(baseClass.type, diagPos, true));
            }
        }
        return additionalImports.toList();
    }
   
   /****
    *         initializeStats.append(toJava.callStatement(
                cDecl.pos(),
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                addTriggersName,
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));
    * ****/
   private JCMethodDecl makeConstructor(DiagnosticPosition diagPos) {
       ListBuffer<JCStatement> cStats = new ListBuffer<JCStatement>();
       cStats.append(toJava.callStatement(diagPos, null, addTriggersName, make.at(diagPos).Ident(names._this)));
       return make.MethodDef(make.Modifiers(Flags.PUBLIC), 
               names.init, 
               make.TypeIdent(TypeTags.VOID), 
               List.<JCTypeParameter>nil(), 
               List.<JCVariableDecl>nil(),
               List.<JCExpression>nil(), 
               make.Block(0L, cStats.toList()), 
               null);

   }
    
    // Add the methods and field for accessing the outer members. Also add a constructor with an extra parameter to handle the instantiation of the classes that access outer members
    private List<JCTree> makeClassOuterAccessorMembers(JFXClassDeclaration cdecl) {
        ListBuffer<JCTree> members = ListBuffer.lb();
        if (cdecl.sym != null && toJava.hasOuters.contains(cdecl.sym)) {
            Symbol typeOwner = cdecl.sym.owner;
            while (typeOwner != null && typeOwner.kind != Kinds.TYP) {
                typeOwner = typeOwner.owner;
            }
            
            if (typeOwner != null) {
                // If FINAL class, it is an anonymous class. There is no interface for it and we need to have the type of the
                // type, not it's interface.
                ClassSymbol returnSym = (typeOwner.flags_field & Flags.FINAL) != 0 ? (ClassSymbol)typeOwner.type.tsym :
                        typeMorpher.reader.enterClass(names.fromString(typeOwner.type.toString() + interfaceSuffix));
                // Create the field to store the outer instance reference
                JCVariableDecl accessorField = make.VarDef(make.Modifiers(Flags.PUBLIC), outerAccessorFieldName, make.Ident(returnSym), null);
                VarSymbol vs = new VarSymbol(Flags.PUBLIC, outerAccessorFieldName, returnSym.type, cdecl.sym);
                accessorField.type = returnSym.type;
                accessorField.sym = vs;

                // Create the interface method with it's type(s) and symbol(s)
                ListBuffer<JCStatement> mStats = new ListBuffer<JCStatement>();
                JCIdent retIdent = make.Ident(vs);
                JCReturn retRet = make.Return(retIdent);
                retRet.type = vs.type;
                mStats.append(retRet);

                JCMethodDecl accessorMethod = make.MethodDef(make.Modifiers(Flags.PUBLIC), outerAccessorName, make.Ident(returnSym), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
                        List.<JCExpression>nil(), make.Block(0L, mStats.toList()), null);

                accessorMethod.type = new MethodType(List.<Type>nil(), returnSym.type, List.<Type>nil(), returnSym);
                accessorMethod.sym = new MethodSymbol(Flags.PUBLIC, outerAccessorName, returnSym.type, returnSym);
                members.append(accessorMethod);
                members.append(accessorField);

                // Now add the constructor taking the outer instance reference
                JCVariableDecl accessorParam = make.VarDef(make.Modifiers(0L), outerAccessorFieldName, make.Ident(returnSym), null);
                VarSymbol vs1 = new VarSymbol(0L, outerAccessorFieldName, returnSym.type, cdecl.sym);
                accessorParam.type = returnSym.type;
                accessorParam.sym = vs1;

                ListBuffer<JCStatement> cStats = new ListBuffer<JCStatement>();
                JCIdent cSelected = make.Ident(names._this);
                cSelected.type = returnSym.type;
                cSelected.sym = returnSym;

                JCFieldAccess cSelect = make.Select(cSelected, outerAccessorFieldName);
                cSelect.sym = accessorField.sym;
                cSelect.type = accessorField.type;

                JCIdent paramIdent = make.Ident(outerAccessorFieldName);
                paramIdent.type = accessorParam.type;
                paramIdent.sym = accessorParam.sym;

                JCAssign assignStat = make.Assign(cSelect, paramIdent);
                assignStat.type = returnSym.type;

                JCStatement assignWrapper = make.Exec(assignStat);
                assignWrapper.type = assignStat.type;

                cStats.append(assignWrapper);

                JCMethodDecl ctor = make.MethodDef(make.Modifiers(Flags.PUBLIC), names.init, make.TypeIdent(TypeTags.VOID), List.<JCTypeParameter>nil(), List.<JCVariableDecl>of(accessorParam),
                        List.<JCExpression>nil(), make.Block(0L, cStats.toList()), null);

                accessorMethod.type = new MethodType(List.<Type>of(accessorParam.type), returnSym.type, List.<Type>nil(), returnSym);
                accessorMethod.sym = new MethodSymbol(Flags.PUBLIC, outerAccessorName, returnSym.type, returnSym);
                members.append(ctor);
            }
        }
        return members.toList();
    }

    // methods for accessing the outer members.
    private List<JCTree> makeInterfaceOuterAccessorMembers(JFXClassDeclaration cdecl) {
        ListBuffer<JCTree> members = ListBuffer.lb();
        if (cdecl.sym != null && toJava.hasOuters.contains(cdecl.sym)) {
            Symbol typeOwner = cdecl.sym.owner;
            while (typeOwner != null && typeOwner.kind != Kinds.TYP) {
                typeOwner = typeOwner.owner;
            }

            if (typeOwner != null) {
                ClassSymbol returnSym = typeMorpher.reader.enterClass(names.fromString(typeOwner.type.toString() + interfaceSuffix));
                JCMethodDecl accessorMethod = make.MethodDef(make.Modifiers(Flags.PUBLIC), outerAccessorName, make.Ident(returnSym), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
                        List.<JCExpression>nil(), null, null);

                accessorMethod.type = new MethodType(List.<Type>nil(), returnSym.type, List.<Type>nil(), returnSym);
                accessorMethod.sym = new MethodSymbol(Flags.PUBLIC, outerAccessorName, returnSym.type, returnSym);

                members.append(accessorMethod);
            }
        }
        return members.toList();
    }

    private List<JCTree> makeInterfaceAttributeGetterMethods(DiagnosticPosition diagPos, List<AttributeInfo> attrInfos) {
        ListBuffer<JCTree> getters = ListBuffer.lb();
        for (AttributeInfo attrInfo : attrInfos) { 
            if ((attrInfo.getFlags() & Flags.PRIVATE) != 0 && !attrInfo.isDirectOwner()) {
                continue;
            }
  
            JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT);
            mods = JavafxToJava.addAccessAnnotationModifiers(attrInfo.getFlags(), mods, (JavafxTreeMaker)make);
            getters.append(make.MethodDef(
                    mods,
                    names.fromString(attributeGetMethodNamePrefix + attrInfo.getName().toString()),
                    toJava.makeTypeTree(attrInfo.getMorphedType(), null),
                    List.<JCTypeParameter>nil(), 
                    List.<JCVariableDecl>nil(), 
                    List.<JCExpression>nil(), 
                    null, null));
        }
        return getters.toList();
    }

    private List<JCTree> makeClassAttributeGetterMethods(DiagnosticPosition diagPos, List<AttributeInfo> attrInfos) {
        ListBuffer<JCTree> getters = ListBuffer.lb();
        for (AttributeInfo attrInfo : attrInfos) { 
            // Add the return statement for the attribute
            JCStatement returnStat = make.Return(make.Ident(attrInfo.getName()));
            JCBlock statBlock = make.Block(0L, List.of(returnStat) );
            
            // Add the method for this class' attributes
            JCModifiers mods = make.Modifiers(Flags.PUBLIC);
            mods = JavafxToJava.addAccessAnnotationModifiers(attrInfo.getFlags(), mods, (JavafxTreeMaker)make);
            getters.append(make.MethodDef(
                    mods,
                    names.fromString(attributeGetMethodNamePrefix + attrInfo.getNameString()),
                    toJava.makeTypeTree(attrInfo.getMorphedType(), null),
                    List.<JCTypeParameter>nil(), 
                    List.<JCVariableDecl>nil(), 
                    List.<JCExpression>nil(), 
                    statBlock, 
                    null));
        }
        return getters.toList();
    }
        
    private List<JCTree> makeClassAttributeApplyDefaultsMethods(DiagnosticPosition diagPos,
            List<String> javafxClassNames,
            List<AttributeInfo> attrInfos) {
        ListBuffer<JCTree> getters = ListBuffer.lb();
        for (AttributeInfo attrInfo : attrInfos) {
            Name methodName = names.fromString(attributeApplyDefaultsMethodNamePrefix + attrInfo.getNameString());
            ListBuffer<JCStatement> stmts = ListBuffer.lb();

            for (String className : javafxClassNames) {
                List<JCExpression> arg = List.<JCExpression>of(make.at(diagPos).Ident(defs.receiverName));
                stmts.append(toJava.callStatement(diagPos, make.at(diagPos).Identifier(className), methodName, arg));
            }

            JCBlock statBlock = make.Block(0L, stmts.toList());

            // Add the method for this class' attributes
            JCModifiers mods = make.Modifiers(Flags.PUBLIC);
            mods = JavafxToJava.addAccessAnnotationModifiers(attrInfo.getFlags(), mods, (JavafxTreeMaker) make);
            getters.append(make.MethodDef(
                    mods,
                    methodName,
                    toJava.makeTypeTree(attrInfo.getMorphedType(), null),
                    List.<JCTypeParameter>nil(),
                    List.<JCVariableDecl>nil(),
                    List.<JCExpression>nil(),
                    statBlock,
                    null));
        }
        return getters.toList();
    }
        

    private JCMethodDecl makeInitializeMethod(DiagnosticPosition diagPos, 
            JFXClassDeclaration cDecl) {
        boolean classIsFinal = (cDecl.getModifiers().flags & Flags.FINAL) != 0;
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // Add calls to do the the default value initialization and user init code (validation for example.)
        
        // "initAttributes$(this);"
        stmts.append(toJava.callStatement(
                cDecl.pos(), 
                make.at(diagPos).Ident(classIsFinal? names._this : cDecl.getName()),
                 initAttributesName, 
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));
        
        // "userInit$(this);"
        stmts.append(toJava.callStatement(
                cDecl.pos(), 
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                userInitName, 
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));
        
        // "postInit$(this);"
        stmts.append(toJava.callStatement(
                cDecl.pos(),
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                postInitName,
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));

        // "InitHelper.finish(attributes);"
        stmts.append( toJava.callStatement(diagPos, 
                toJava.makeTypeTree(initHelperType, diagPos), 
                "finish", 
                make.at(diagPos).Ident(defs.attributesFieldName)) );
        
        // "attributes = null;"
        stmts.append(make.Exec(make.Assign(make.Ident(defs.attributesFieldName), make.Literal(TypeTags.BOT, null))));

        /***
        // Call the postInit$ method
        stmts.append(toJava.callStatement(
                cDecl.pos(),
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                postInitName, 
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));
         * ***/

        JCBlock initializeBlock = make.Block(0L, stmts.toList());
        return make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                defs.initializeName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>nil(), 
                List.<JCExpression>nil(), 
                initializeBlock, null);
    }
    
    /**
     * Construct the SetDefaults method
     * */
    private JCMethodDecl makeInitAttributesMethod(DiagnosticPosition diagPos, 
                                               JFXClassDeclaration cDecl,
                                               List<String> javafxClassNames,
                                               List<TranslatedAttributeInfo> translatedAttrInfo) {
        boolean classIsFinal =(cDecl.getModifiers().flags & Flags.FINAL) != 0;
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // call the superclasses SetDefaults
        stmts.appendList( makeRecursiveSupertypeCalls(diagPos, javafxClassNames, initAttributesName) );

        // Add the initialization of this class' attributes
        stmts.appendList(makeInitAttributesCode(translatedAttrInfo, cDecl));

        return make.at(diagPos).MethodDef(
                make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC),
                initAttributesName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>of( toJava.makeReceiverParam(cDecl) ), 
                List.<JCExpression>nil(), 
                make.Block(0L, stmts.toList()), 
                null);
    }

    // Add the initialization of this class' attributes
    private List<JCStatement> makeInitAttributesCode(List<TranslatedAttributeInfo> translatedAttrInfo,
                                                                   JFXClassDeclaration cdef) {
        List<JCStatement> ret = List.nil();
        for (TranslatedAttributeInfo tai : translatedAttrInfo) {
            DiagnosticPosition diagPos = tai.pos();
            if (tai.attribute != null && tai.attribute.getTag() == JavafxTag.VAR_DEF && tai.attribute.pos != Position.NOPOS) {
		if ((tai.attribute.getModifiers().flags & Flags.STATIC) != 0) {
		    continue;
		}
                if (tai.attribute.sym != null && tai.attribute.sym.owner == cdef.sym) {
                    VarMorphInfo vmi = typeMorpher.varMorphInfo(tai.attribute.sym);
                    JCExpression needsDefaultCond = toJava.callExpression(diagPos, 
                            toJava.makeAttributeAccess(cdef, tai.getNameString()), 
                            defs.needDefaultsMethodName);
                    JCIf defInitIf = make.If(needsDefaultCond, tai.initStmt, null);
                    ret = ret.append(defInitIf);
                }
            }
        }
        return ret;
    }
    /**
     * Construct the addTriggers method
     * */
    private JCMethodDecl makeTriggersMethod(DiagnosticPosition diagPos, 
                                               JFXClassDeclaration cDecl,
                                               List<String> javafxClassNames,
                                               List<TranslatedAttributeInfo> translatedAttrInfo,
                                               List<TranslatedOverrideAttributeInfo> translatedTriggerInfo) {
        boolean classIsFinal =(cDecl.getModifiers().flags & Flags.FINAL) != 0;
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // call the superclasses addTriggers
        stmts.appendList( makeRecursiveSupertypeCalls(diagPos, javafxClassNames, addTriggersName) );

        // add change listeners for triggers on attribute definitions
        for (TranslatedAttributeInfo info : translatedAttrInfo) {
            JCStatement stat = makeChangeListenerCall(info);
            if (stat != null)
                stmts.append(stat);
        }

        // add change listeners for "with" triggers
        for (TranslatedOverrideAttributeInfo info : translatedTriggerInfo) {
            JCStatement stat = makeChangeListenerCall(info);
            if (stat != null)
                stmts.append(stat);
        }

        return make.at(diagPos).MethodDef(
                make.Modifiers(classIsFinal? Flags.PUBLIC  : Flags.PUBLIC | Flags.STATIC),
                addTriggersName,
                toJava.makeTypeTree(syms.voidType, null),
                List.<JCTypeParameter>nil(),
                List.<JCVariableDecl>of( toJava.makeReceiverParam(cDecl) ),
                List.<JCExpression>nil(),
                make.Block(0L, stmts.toList()),
                null);
    }

    /**
     * Construct the static block for setting defaults
     * */
    private JCBlock makeInitAttributesBlock(DiagnosticPosition diagPos, 
            JFXClassDeclaration cDecl,
            List<TranslatedAttributeInfo> translatedAttrInfo) {
        // Add the initialization of this class' attributesa
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        for (TranslatedAttributeInfo tai : translatedAttrInfo) {
            assert tai.attribute != null && tai.attribute.getTag() == JavafxTag.VAR_DEF && tai.attribute.pos != Position.NOPOS;
            if ((tai.attribute.getModifiers().flags & Flags.STATIC) != 0) {
                if (tai.attribute.sym != null && tai.attribute.sym.owner == cDecl.sym) {
                    stmts.append(tai.initStmt);
                }
                JCStatement stat = makeStaticChangeListenerCall(tai);
                if (stat != null) {
                    stmts.append(stat);
                }
            }
        }
        return make.Block(Flags.STATIC, stmts.toList());
    }

    // build a field for each non-static attribute (including inherited).
    // and for static attributes of this class
    private List<JCTree> makeAttributeFields(DiagnosticPosition diagPos, List<AttributeInfo> attrInfos) {
        ListBuffer<JCTree> fields = ListBuffer.lb();
        for (AttributeInfo attrInfo : attrInfos) {
            if (!attrInfo.isStatic() || attrInfo.isDirectOwner()) {
                JCVariableDecl var = make.at(diagPos).VarDef(
                        make.Modifiers(Flags.PUBLIC | Flags.FINAL | (attrInfo.getFlags() & Flags.STATIC)),
                        attrInfo.getName(),
                        attrInfo.makeMorphedTypeTree(diagPos),
                        typeMorpher.makeLocationAttributeVariable(attrInfo.getVMI(), diagPos));
                fields.append(var);
            }
        }
        return fields.toList();
    }

    // Build a field that has an array of all the non-static attributes (including inherited).
    private JCVariableDecl makeAttributeListField(DiagnosticPosition diagPos, List<AttributeInfo> attrInfos) {
        ListBuffer<JCExpression> attrs = ListBuffer.lb();

        for (AttributeInfo attrInfo : attrInfos) {
            if (!attrInfo.isStatic()) {
                attrs.append(make.at(diagPos).Ident(attrInfo.getName()));
            }
        }
        
        return make.at(diagPos).VarDef(make.Modifiers(Flags.PUBLIC), 
                defs.attributesFieldName, 
                make.TypeArray( toJava.makeTypeTree(abstractVariableType, diagPos) ), 
                make.NewArray(toJava.makeTypeTree(abstractVariableType, diagPos), 
                                List.<JCExpression>nil(), attrs.toList()));
    }

/**
* Walk the supertypes of this class, collecting the attributes and functions that need to be rolled up
* into the class representation.
* For source classes this means walking the explicit supertype in the AST.  For classes from class files,
* this means looking at the interfaces and the parallel class.
*/
   class CollectAttributeAndMethods {
       private final DiagnosticPosition diagPos;

       List<ClassSymbol> javaInterfaces;
       List<String> javafxClassNames;
       ListBuffer<AttributeInfo> attributeInfos = ListBuffer.lb();
       java.util.List<Symbol> allAttributeSymbols = new java.util.ArrayList<Symbol>();
       java.util.List<MethodSymbol> needInterfaceMethods = new java.util.ArrayList<MethodSymbol>();
       java.util.List<MethodSymbol> needDispatchMethods = new java.util.ArrayList<MethodSymbol>();
       java.util.List<MethodSymbol> needStaticDispatchMethods = new java.util.ArrayList<MethodSymbol>();
       
       private ListBuffer<ClassSymbol> baseClasses = ListBuffer.lb();
       private Map<String,String> visitedSourceAttributes = new HashMap<String,String>();
       private Map<String,String> visitedClassFileAttributes = new HashMap<String,String>();
       private Set<String> visitedFunctions = new HashSet<String>();
       private java.util.List<ClassSymbol> classesToVisit = new java.util.ArrayList<ClassSymbol>();
       private Set<ClassSymbol> addedBaseClasses = new HashSet<ClassSymbol>();
        
       CollectAttributeAndMethods(DiagnosticPosition diagPos, ClassSymbol csym) {
           this.diagPos = diagPos;
           classesToVisit.add(csym);
           while (!classesToVisit.isEmpty()) {
               ClassSymbol cSym = classesToVisit.get(0);
               classesToVisit.remove(0);
               boolean compound = (cSym.flags_field & JavafxFlags.COMPOUND_CLASS) != 0;
               if (types.isJFXClass(cSym)) {
                   JFXClassDeclaration cDecl = types.getFxClass(cSym); // get the corresponding AST, null if from class file
                   if (cDecl == null) { 
                       // this is a JavaFX class from a class file
                       if ((cSym.flags_field & Flags.INTERFACE) == 0 && cSym.members() != null) {
                           // we want to base this solely on (non-empty) implementation (not interfaces)
                           for (Entry e = cSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                               if (e.sym.kind == Kinds.MTH) {
                                   processMethodFromClassFile((MethodSymbol) e.sym, compound, cSym);
                               }
                           }
                       }
                       // now visit the super-types (both interface and class forms)
                       for (Type supertype : cSym.getInterfaces()) {
                           ClassSymbol iSym = (ClassSymbol) supertype.tsym;
                           addToVisitList(iSym);
                           String iName = iSym.fullname.toString();
                           if (iName.endsWith(JavafxDefs.interfaceSuffix)) {
                               // enter and visit the class parallel to this interface
                               String sName = iName.substring(0, iName.length() - JavafxDefs.interfaceSuffix.length());
                               ClassSymbol sSym = typeMorpher.reader.enterClass(names.fromString(sName));
                               addToVisitList(sSym);
                           }
                       }
                   } else {
                       // this is a JavaFX source file
                       for (JCTree def : cDecl.getMembers()) {
                           if (def.getTag() == JavafxTag.VAR_DEF) {
                               processAttributeFromSource((JFXVar) def, cDecl);
                           }
                           else if (compound && def.getTag() == JavafxTag.FUNCTION_DEF) {
                               processFunctionFromSource((JFXFunctionDefinition) def);
                           }
                       }
                       // now visit the super-types
                       for (JCExpression supertype : cDecl.getSupertypes()) {
                           addToVisitList(supertype.type.tsym);
                       }
                   }
               }
           }
           // finish up
           processClassLists();
       }

       private void addToVisitList(Symbol sSym) {
           if (sSym != null && sSym.kind == Kinds.TYP) {
               ClassSymbol cSym = (ClassSymbol) sSym;
               if (!addedBaseClasses.contains(cSym)) {

                   addedBaseClasses.add(cSym);
                   classesToVisit.add(cSym);
                   baseClasses.append(cSym);
               }
           }
       }
       
       private void processClassLists() {
           Set<String> classNamesSeen = new HashSet<String>();
           ListBuffer<ClassSymbol> javaInterfacesBuff = ListBuffer.lb();
           ListBuffer<String> javafxClassNamesBuff = ListBuffer.lb();
           for (ClassSymbol cSym : baseClasses) {
               String className = cSym.fullname.toString();
               boolean isFXInterface = className.endsWith(interfaceSuffix);
               if (types.isJFXClass(cSym)) {
                   if (isFXInterface) {
                       className = className.substring(0, className.length() - interfaceSuffix.length());
                   }

                   if (!classNamesSeen.contains(className)) {
                       classNamesSeen.add(className);
                       javafxClassNamesBuff.append(className);
                   }
               }

               if (!isFXInterface &&
                       cSym.fullname != names.fromString(fxObjectString) &&
                       (cSym.flags_field & JavafxFlags.COMPOUND_CLASS) != 0 &&
                       cSym.type != null) {
                   javaInterfacesBuff.append(cSym);
               }
           }
           javaInterfaces = javaInterfacesBuff.toList();
           javafxClassNames = javafxClassNamesBuff.toList();
       }
       
       private void processMethodFromClassFile(MethodSymbol meth, boolean compound, ClassSymbol cSym) {
           String methName = meth.name.toString();
           if (methName.startsWith(attributeGetMethodNamePrefix)) {
               // this is an attribute get method, collect it as an attribute
               String nameSig = methName.substring(attributeGetMethodNamePrefix.length());
               if (visitedSourceAttributes.containsKey(nameSig)) {
                   log.error("javafx.cannot.override.default.initializer", 
                           nameSig, cSym.className(), visitedSourceAttributes.get(nameSig));
               } else if(visitedClassFileAttributes.containsKey(nameSig)) {
                   // no error (attributes are duplicated in subclasses) but don't add again
               } else {
                   visitedClassFileAttributes.put(nameSig, cSym.className());
                   attributeInfos.append(new AttributeInfo(diagPos, meth, false));
               }
           } else if (compound && methName.endsWith(JavafxDefs.implFunctionSuffix)) {
               // implementation method
               methName = methName.substring(0, methName.length() - JavafxDefs.implFunctionSuffix.length());
               int cnt = 0;
               ListBuffer<VarSymbol> params = ListBuffer.lb();
               ListBuffer<Type> paramTypes = ListBuffer.lb();
               for (VarSymbol param : meth.getParameters()) {
                   cnt++;
                   if (cnt > 1) {
                       params.append(param);
                       paramTypes.append(param.type);
                   }
               }
               MethodType mtype = new MethodType(paramTypes.toList(), meth.type.getReturnType(), meth.type.getThrownTypes(), meth.type.tsym);
               MethodSymbol fixedMeth = new MethodSymbol(
                    meth.flags() & ~Flags.STATIC, 
                    names.fromString(methName),
                    mtype, 
                    meth.owner);
               fixedMeth.params = params.toList();
               String nameSig = methodSignature(fixedMeth);
               if (!visitedFunctions.contains(nameSig)) {
                   visitedFunctions.add(nameSig);
                   needDispatchMethods.add(fixedMeth);
               }
           }
       }
     
       private void processFunctionFromSource(JFXFunctionDefinition def) {
           MethodSymbol meth = def.sym;
           if (def.pos == Position.NOPOS) {
               return; //TODO: this looks REALLY dangerous, but this brabch is taken.  FIXME
           }
           String nameSig = methodSignature(meth);
           if (!visitedFunctions.contains(nameSig)) {
               visitedFunctions.add(nameSig);
               if ((meth.flags() & (Flags.SYNTHETIC|Flags.STATIC)) == 0) {
                   needInterfaceMethods.add(meth);
               }
               if ((meth.flags() & (Flags.SYNTHETIC | Flags.ABSTRACT)) == 0) {
                   if ((meth.flags() & (Flags.STATIC)) != 0) {
                       needStaticDispatchMethods.add(meth);
                   } else {
                       needDispatchMethods.add(meth);
                   }
               }
           }
       }
       
       private  void processAttributeFromSource(JFXVar def, JFXClassDeclaration cDecl) {
           VarSymbol var = def.sym;

           if (var.owner.kind == Kinds.TYP) {
               String attrName = var.name.toString();
               String className = cDecl.getName().toString();
               if (visitedSourceAttributes.containsKey(attrName)) {
                   log.error("javafx.cannot.override.default.initializer", 
                           attrName, className, visitedSourceAttributes.get(attrName));
               } else if(visitedClassFileAttributes.containsKey(attrName)) {
                   log.error("javafx.cannot.override.default.initializer", 
                           attrName, className, visitedClassFileAttributes.get(attrName));
               } else {
                   visitedSourceAttributes.put(attrName, className);
                   attributeInfos.append(new AttributeInfo(diagPos, var, var.owner==cDecl.sym));
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

   /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    JCStatement makeChangeListenerCall(TranslatedAttributeInfo info) {
        JFXOnReplace onReplace = null;
        JFXOnReplaceElement onReplaceElement = null;
        JFXOnInsertElement onInsertElement = null;
        JFXOnDeleteElement onDeleteElement = null;
        
        if (info.onChanges.isEmpty()) {
            return null;
        } 
        
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
        
        return makeChangeListenerCall(info,
             onReplace,
             onReplaceElement,
             onInsertElement,
             onDeleteElement);
    }
    
    /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    JCStatement makeChangeListenerCall(TranslatedOverrideAttributeInfo info) {
        return makeChangeListenerCall(info,
             info.onReplace(),
             null,
             null,
             null);
    }
    
    /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    JCStatement makeChangeListenerCall(AttributeInfo info,
            JFXOnReplace onReplace,
            JFXOnReplaceElement onReplaceElement,
            JFXOnInsertElement onInsertElement,
            JFXOnDeleteElement onDeleteElement) {
        DiagnosticPosition diagPos = info.pos();
        ListBuffer<JCTree> members = ListBuffer.lb();

        JCExpression changeListener;
        List<JCExpression> emptyTypeArgs = List.nil();
        int attributeKind = typeMorpher.varMorphInfo(info.getSymbol()).getTypeKind();
        if (onReplaceElement != null || onInsertElement != null || onDeleteElement != null) {
            changeListener = make.at(diagPos).Identifier(sequenceChangeListenerInterfaceName);
            changeListener = make.at(diagPos).TypeApply(changeListener, 
                    List.<JCExpression>of(toJava.makeTypeTree(info.getElementType(), diagPos)));
            members.append(makeSequenceChangeListenerMethod(
                    diagPos, 
                    onReplaceElement, 
                    "onReplace", 
                    List.<JCVariableDecl>of(
                        makeIndexParam(diagPos, onReplaceElement), 
                        makeParam(diagPos, info.getElementType(),
                                  onReplaceElement == null ? null : onReplaceElement.getOldValue(),
                                  "$oldValue$"),
                        makeParam(diagPos, info.getElementType(), null, "$newValue$")), 
                    TypeTags.VOID));
            members.append(makeSequenceChangeListenerMethod(
                    diagPos, 
                    onInsertElement, 
                    "onInsert", 
                    List.<JCVariableDecl>of(
                        makeIndexParam(diagPos, onInsertElement), 
                        makeParam(diagPos, info.getElementType(),
                                  onInsertElement == null ? null : onInsertElement.getOldValue(),
                                  "$newValue$")), 
                    TypeTags.VOID));
            members.append(makeSequenceChangeListenerMethod(
                    diagPos, 
                    onDeleteElement, 
                    "onDelete", 
                    List.<JCVariableDecl>of(
                        makeIndexParam(diagPos, onDeleteElement), 
                        makeParam(diagPos, info.getElementType(),
                                  onDeleteElement == null ? null : onDeleteElement.getOldValue(),
                                  "$oldValue$")), 
                    TypeTags.VOID));
        }
        else if (types.isSequence(info.getRealType())) {
            ListBuffer<JCStatement> setUpStmts = ListBuffer.lb();
            changeListener = make.at(diagPos).Identifier(sequenceReplaceListenerInterfaceName);
            changeListener = make.TypeApply(changeListener, List.of(toJava.makeTypeTree(info.getElementType(), diagPos)));
            Type seqValType = types.sequenceType(info.getElementType(), false);
            List<JCVariableDecl> onChangeArgs = List.of(
                makeIndexParam(diagPos, onReplace),
                makeParam(diagPos, syms.intType, onReplace.getLastIndex(), "$lastIndex$"),
                makeParam(diagPos, info.getRealType(), onReplace.getNewElements(), "$newElements$"),
                makeParam(diagPos, seqValType, onReplace.getOldValue(), "$oldValue$"),
                makeParam(diagPos, seqValType, null, "$newValue$"));
            members.append(makeChangeListenerMethod(diagPos, onReplace, setUpStmts, "onReplace", onChangeArgs, TypeTags.VOID));
        }
        else {
            changeListener = make.at(diagPos).Identifier(changeListenerInterfaceName[attributeKind]);
            members.append(makeOnReplaceChangeListenerMethod(diagPos, onReplace, info.getRealType()));
        }

        if (attributeKind == JavafxVarSymbol.TYPE_KIND_OBJECT)
            changeListener = make.at(diagPos).TypeApply(changeListener,
                                                        List.<JCExpression>of(toJava.makeTypeTree(info.getRealType(), diagPos)));
        JCNewClass anonymousChangeListener = make.NewClass(
                null, 
                emptyTypeArgs,
                changeListener, 
                List.<JCExpression>nil(), 
                make.at(diagPos).AnonymousClassDef(make.Modifiers(0L), members.toList()));

        JCExpression attrRef = toJava.makeAttributeAccess(diagPos, info.getNameString());
        JCFieldAccess tmpSelect = make.at(diagPos).Select(attrRef, addChangeListenerName);

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
        long flags = Flags.PARAMETER|Flags.FINAL;
        if (var != null && var.mods != null) {
            flags |= var.mods.flags;
        }
        return make.at(diagPos).VarDef(
                make.Modifiers(flags),
                name,
                toJava.makeTypeTree(type, diagPos),
                null);
        
    }

    private JCVariableDecl makeIndexParam(DiagnosticPosition diagPos, JFXAbstractOnChange onChange) {
        return makeParam(diagPos, syms.intType, onChange == null ? null : onChange.getIndex(), "$index$");
    }

    /**
     * construct a change listener method for sequence triggers.  Insert in a listener anon class.
     *   void onInsert(...);
     *   void on Delete(...); ...
     */
    private JCMethodDecl makeSequenceChangeListenerMethod(
            DiagnosticPosition diagPos,
            JFXAbstractOnChange onChange, 
            String methodName, 
            List<JCVariableDecl> args, 
            int returnTypeTag) {
        return makeChangeListenerMethod(
             diagPos,
             onChange, 
             ListBuffer.<JCStatement>lb(),
             methodName, 
             args, 
             returnTypeTag);
    }

    /**
     * construct a change listener method for insertion in a listener anon class.
     *   void onReplace(...); ...
     */
    private JCMethodDecl makeOnReplaceChangeListenerMethod(DiagnosticPosition diagPos,
                                                           JFXOnReplace onReplace,
                                                           Type attributeType) {
        List<JCVariableDecl> onChangeArgs = List.<JCVariableDecl>nil()
                .append(make.VarDef(make.Modifiers(0L), onChangeArgName1, toJava.makeTypeTree(attributeType, diagPos), null))
                .append(make.VarDef(make.Modifiers(0L), onChangeArgName2, toJava.makeTypeTree(attributeType, diagPos), null));
        ListBuffer<JCStatement> setUpStmts = ListBuffer.lb();
        if (onReplace != null && onReplace.getOldValue() != null) {
            // Create the variable for the old value, using the specified name
            JFXVar oldValue = onReplace.getOldValue();
            VarMorphInfo vmi = typeMorpher.varMorphInfo(oldValue.sym);

            setUpStmts.append( 
                    make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        oldValue.getName(), 
                        toJava.makeTypeTree(vmi.getRealType(), diagPos, types.isJFXClass(vmi.getRealType().tsym)),
                        make.Identifier(onChangeArgName1)));
        }
        if (onReplace != null && onReplace.getNewElements() != null) {
            // Create the variable for the new value, using the specified name
            JFXVar newValue = onReplace.getNewElements();
            VarMorphInfo vmi = typeMorpher.varMorphInfo(newValue.sym);

            setUpStmts.append( 
                    make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        newValue.getName(), 
                        toJava.makeTypeTree(vmi.getRealType(), diagPos, types.isJFXClass(vmi.getRealType().tsym)),
                        make.Identifier(onChangeArgName2)));
        }
        return makeChangeListenerMethod(diagPos, onReplace, setUpStmts, "onChange", onChangeArgs, TypeTags.VOID);
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
            ListBuffer<JCStatement> prefixStmts,
            String methodName,
            List<JCVariableDecl> args,
            int returnTypeTag) {
        ListBuffer<JCStatement> ocMethStmts = ListBuffer.lb();
        ocMethStmts.appendList(prefixStmts);
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

    JCStatement makeStaticChangeListenerCall(TranslatedAttributeInfo info) {
	// TBD static attribute triggers
	return null;
    }

    private List<JCStatement> makeRecursiveSupertypeCalls(DiagnosticPosition diagPos,
            List<String> javafxClassNames, Name methodName) {
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        for (String className : javafxClassNames) {
            List<JCExpression> arg = List.<JCExpression>of(make.at(diagPos).Ident(defs.receiverName));
            stmts.append(toJava.callStatement(diagPos, make.at(diagPos).Identifier(className), methodName, arg));
        }
        return stmts.toList();
    }
    
    /**
     * Make a method from a MethodSymbol and an optional method body.
     * Make a bound version if "isBound" is set.
     */
    private JCMethodDecl makeMethod(DiagnosticPosition diagPos, MethodSymbol mth, JCBlock mthBody, boolean isBound) {
        // build the parameter list
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        for (VarSymbol vsym : mth.getParameters()) {
            Type vtype = vsym.asType();
            if (isBound) {
                VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                vtype = vmi.getMorphedType();
            }
            params.append(make.VarDef(
                    make.Modifiers(0L), 
                    vsym.name, 
                    toJava.makeTypeTree(vtype, diagPos), 
                    null // no initial value
                    ));
        }
        
        // make the method
        JCModifiers mods = make.Modifiers(Flags.PUBLIC | (mthBody==null? Flags.ABSTRACT : 0L));
        mods = JavafxToJava.addAccessAnnotationModifiers(mth.flags(), mods, (JavafxTreeMaker)make);
        return make.at(diagPos).MethodDef(
                        mods, 
                        toJava.functionName(mth, false, isBound), 
                        toJava.makeReturnTypeTree(diagPos, mth, isBound), 
                        List.<JCTypeParameter>nil(), 
                        params.toList(), 
                        List.<JCExpression>nil(), 
                        mthBody, 
                        null);
    }
    
    /**
     * Make a method body which redirects to the actual implementation in a static method of the defining class.
     */
    private JCBlock makeDispatchBody(DiagnosticPosition diagPos, MethodSymbol mth, boolean isBound, boolean isStatic) {
        ListBuffer<JCExpression> args = ListBuffer.lb();
        if (!isStatic) {
            // Add the this argument, so the static implementation method is invoked
            args.append(make.Ident(names._this));
        }
        for (VarSymbol var : mth.params) {
            args.append(make.Ident(var.name));
        }
        String receiver = mth.owner.name.toString();
        JCExpression expr = toJava.callExpression(diagPos, make.Identifier(receiver), toJava.functionName(mth, !isStatic, isBound), args);
        JCStatement statement = (mth.getReturnType() == syms.voidType) ? make.Exec(expr) : make.Return(expr);
        return make.at(diagPos).Block(0L, List.<JCStatement>of(statement));
     }
    
    /**
     * Return the generated interface name corresponding to the class
     * */
    Name interfaceName(JFXClassDeclaration cDecl) {
        Name name = cDecl.getName();
        if (cDecl.generateClassOnly())
            return name;
        return names.fromString(name.toString() + interfaceSuffix);
    }
}
