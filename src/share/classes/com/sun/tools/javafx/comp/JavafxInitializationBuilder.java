/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Scope.Entry;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.VarInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedVarInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedOverrideClassVarInfo;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javac.code.Flags.PRIVATE;

/**
 * Build the representation(s) of a JavaFX class.  Includes class initialization, attribute and function proxies.
 * With support for multiple inheritent.
 * 
 * @author Robert Field
 * @author Lubo Litchev
 * @author Per Bothner
 * @author Zhiqun Chen
 */
public class JavafxInitializationBuilder extends JavafxTranslationSupport {
    protected static final Context.Key<JavafxInitializationBuilder> javafxInitializationBuilderKey =
        new Context.Key<JavafxInitializationBuilder>();

    private final JavafxToJava toJava;
    private final JavafxClassReader reader;
    
    private final Name addChangeListenerName;
    private final Name locationInitializeName;;
    private final Name[] changeListenerInterfaceName;
//    private final Name sequenceReplaceListenerInterfaceName;
    private final Name sequenceChangeListenerInterfaceName;
    private static final String initHelperClassName = "com.sun.javafx.runtime.InitHelper";
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
        super(context);
        
        context.put(javafxInitializationBuilderKey, this);

        toJava = JavafxToJava.instance(context);
        reader = (JavafxClassReader) JavafxClassReader.instance(context);
        
        addChangeListenerName = names.fromString("addChangeListener");
        locationInitializeName = names.fromString("initialize");
        changeListenerInterfaceName = new Name[JavafxVarSymbol.TYPE_KIND_COUNT];
        for (int i=0; i< JavafxVarSymbol.TYPE_KIND_COUNT; i++)
            changeListenerInterfaceName[i]
                    = names.fromString(locationPackageNameString + "." + JavafxVarSymbol.getTypePrefix(i) + "ChangeListener");
 //       sequenceReplaceListenerInterfaceName = names.fromString(locationPackageName + "SequenceReplaceListener");
        sequenceChangeListenerInterfaceName = names.fromString(locationPackageNameString + ".SequenceChangeListener");
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
            Name name = Name.fromString(names, locationPackageNameString + ".AbstractVariable");
            ClassSymbol sym = reader.enterClass(name);
            abstractVariableType = types.erasure( sym.type );
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

        JavafxClassModel(
                Name interfaceName,
                List<JCExpression> interfaces,
                List<JCTree> iDefinitions,
                List<JCTree> addedClassMembers,
                List<JCExpression> additionalImports,
                Type superType) {
            this.interfaceName = interfaceName;
            this.interfaces = interfaces;
            this.iDefinitions = iDefinitions;
            this.additionalClassMembers = addedClassMembers;
            this.additionalImports = additionalImports;
            this.superType = superType;
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
           List<TranslatedOverrideClassVarInfo> translatedOverrideAttrInfo) {
        boolean classOnly = cDecl.generateClassOnly();
        DiagnosticPosition diagPos = cDecl.pos();
        Type superType = superType(cDecl);
        ClassSymbol outerTypeSym = outerTypeSymbol(cDecl); // null unless inner class with outer reference

        JavafxAnalyzeClass analysis = new JavafxAnalyzeClass(diagPos,
                cDecl.sym, translatedAttrInfo, translatedOverrideAttrInfo,
                log, names, types, reader, typeMorpher);
        List<VarInfo> instanceAttributeInfos = analysis.instanceAttributeInfos();
        List<ClassSymbol> javaInterfaces = immediateJavaInterfaceNames(cDecl);
        List<ClassSymbol> immediateFxSupertypeNames = immediateJavafxSupertypes(cDecl);

        ListBuffer<JCTree> cDefinitions = ListBuffer.lb();  // additional class members needed
        cDefinitions.appendList(makeAttributeFields(cDecl.sym, instanceAttributeInfos));
        cDefinitions.appendList(makeAttributeFields(cDecl.sym, analysis.staticAttributeInfos()));
        cDefinitions.appendList(makeClassAttributeGetterMethods(cDecl, instanceAttributeInfos));
        cDefinitions.appendList(makeClassAttributeApplyDefaultsMethods(diagPos, cDecl, instanceAttributeInfos));
        cDefinitions.append(makeInitStaticAttributesBlock(cDecl, translatedAttrInfo));
        cDefinitions.append(makeInitializeMethod(diagPos, instanceAttributeInfos, cDecl));
        if (outerTypeSym != null) {
            cDefinitions.append(makeClassOuterAccessorField(diagPos, cDecl, outerTypeSym));
            cDefinitions.append(makeClassOuterAccessorMethod(diagPos, cDecl, outerTypeSym));
        }
        cDefinitions.append(makeAddTriggersMethod(diagPos, cDecl, immediateFxSupertypeNames, translatedAttrInfo, translatedOverrideAttrInfo));
        cDefinitions.appendList(makeClassFunctionProxyMethods(cDecl, analysis.needDispatch()));
        if (outerTypeSym == null) {
            cDefinitions.append(makeJavaEntryConstructor(diagPos));
        }
        cDefinitions.append(makeFXEntryConstructor(diagPos, outerTypeSym, superType != null && types.isJFXClass(superType.tsym)));

        ListBuffer<JCTree> iDefinitions = ListBuffer.lb();
        if (!classOnly) {
            iDefinitions.appendList(makeInterfaceAttributeGetterMethods(diagPos, translatedAttrInfo));
            iDefinitions.appendList(makeInterfaceFunctionMethods(cDecl));
            iDefinitions.appendList(makeInterfaceOuterAccessorMembers(cDecl));
        }

        Name interfaceName = classOnly ? null : interfaceName(cDecl);

        return new JavafxClassModel(
                interfaceName,
                makeImplementingInterfaces(diagPos, cDecl, javaInterfaces),
                iDefinitions.toList(),
                cDefinitions.toList(),
                makeAdditionalImports(diagPos, cDecl, javaInterfaces),
                superType);
    }
   
    private Type superType(JFXClassDeclaration cDecl) {
        //TODO: this is in drastic need of cleaning up
        Type superType = null;
        if (cDecl.type instanceof ClassType &&
                (superType = ((ClassType) cDecl.type).supertype_field) != null &&
                superType.tsym instanceof ClassSymbol &&
                (superType.tsym.flags_field & JavafxFlags.COMPOUND_CLASS) == 0) {
        } else if ((cDecl.mods.flags & Flags.FINAL) != 0L && cDecl.getExtending().nonEmpty()) {
            Symbol sym1 = JavafxTreeInfo.symbol(cDecl.getExtending().head);
            if (sym1 != null &&
                    (sym1.flags_field & JavafxFlags.COMPOUND_CLASS) == 0) {
                superType = cDecl.getExtending().head.type;
            }
        }
        return superType;
    }
   
    private List<ClassSymbol> immediateJavafxSupertypes(JFXClassDeclaration cDecl) {
        ListBuffer<ClassSymbol> javafxClassNamesBuff = ListBuffer.lb();
        for (JFXExpression stype : cDecl.getSupertypes()) {
            Symbol sym = expressionSymbol(stype);
            if (types.isJFXClass(sym)) {
                ClassSymbol cSym = (ClassSymbol) sym;
                javafxClassNamesBuff.append(cSym);
            }
        }
        return javafxClassNamesBuff.toList();
    }

   
    private List<ClassSymbol> immediateJavaInterfaceNames(JFXClassDeclaration cDecl) {
        ListBuffer<ClassSymbol> javaInterfacesBuff = ListBuffer.lb();
        for (JFXExpression sup : cDecl.getSupertypes()) {
            ClassSymbol cSym = (ClassSymbol) expressionSymbol(sup);
            if (cSym != null) {
                String className = cSym.fullname.toString();
                boolean isFXInterface = className.endsWith(interfaceSuffix);

                if (!isFXInterface &&
                        cSym.fullname != names.fromString(fxObjectString) &&
                        (cSym.flags_field & JavafxFlags.COMPOUND_CLASS) != 0 &&
                        cSym.type != null) {
                    javaInterfacesBuff.append(cSym);
                }
            }
        }
        return javaInterfacesBuff.toList();
    }

    private List<JCTree> makeInterfaceFunctionMethods(JFXClassDeclaration cDecl) {
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
    private List<JCTree> makeClassFunctionProxyMethods(JFXClassDeclaration cDecl, List<MethodSymbol> needDispatch) {
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
        JCBlock mthBody = withDispatch ? makeDispatchBody(cDecl, sym, isBound, sym.flags() == Flags.STATIC) : null;

        DiagnosticPosition diagPos = cDecl;
        // build the parameter list
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        for (VarSymbol vsym : sym.getParameters()) {
            Type vtype = vsym.asType();
            if (isBound) {
                VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);
                vtype = vmi.getLocationType();
            }
            params.append(make.VarDef(
                    make.Modifiers(0L), 
                    vsym.name, 
                    makeTypeTree(diagPos, vtype), 
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
    }

    
    private List<JCExpression> makeImplementingInterfaces(DiagnosticPosition diagPos,
            JFXClassDeclaration cDecl, 
            List<ClassSymbol> baseInterfaces) {
        ListBuffer<JCExpression> implementing = ListBuffer.lb();
        implementing.append(makeIdentifier(diagPos, fxObjectString));
        for (List<JFXExpression> l = cDecl.getImplementing(); l.nonEmpty(); l = l.tail) {
            implementing.append(makeTypeTree( null,l.head.type));
        }

        for (ClassSymbol baseClass : baseInterfaces) {
                implementing.append(makeTypeTree( diagPos,baseClass.type, true));
        }
        return implementing.toList();
    }

    private List<JCExpression> makeAdditionalImports(DiagnosticPosition diagPos, JFXClassDeclaration cDecl, List<ClassSymbol> baseInterfaces) {
        // Add import statements for all the base classes and basClass $Intf(s).
        // There might be references to them when the methods/attributes are rolled up.
        ListBuffer<JCExpression> additionalImports = new ListBuffer<JCExpression>();
        for (ClassSymbol baseClass : baseInterfaces) {
            if (baseClass.type != null && baseClass.type.tsym != null &&
                    baseClass.type.tsym.packge() != cDecl.sym.packge() &&     // Work around javac bug (CR 6695838)
                    baseClass.type.tsym.packge() != syms.unnamedPackage) {    // Work around javac bug. the visitImport of Attr 
                // is casting to JCFieldAcces, but if you have imported an
                // JCIdent only a ClastCastException is thrown.
                additionalImports.append(makeTypeTree( diagPos,baseClass.type, false));
                additionalImports.append(makeTypeTree( diagPos,baseClass.type, true));
            }
        }
        return additionalImports.toList();
    }
   
    /**
     * Make a constructor to be called by Java code.
     * It differs by calling the initialize$ method (that is explicitly called
     * by FX code).
     * @param diagPos
     * @return the constructor
     */
    private JCMethodDecl makeJavaEntryConstructor(DiagnosticPosition diagPos) {
        // call the FX (basic) version of the constructor
        JCStatement thisCall = make.at(diagPos).Exec(make.at(diagPos).Apply(null,
                make.at(diagPos).Ident(names._this),
                List.<JCExpression>of(make.at(diagPos).Literal(TypeTags.BOOLEAN, 0))));
        // then call the initialize$ method
        JCStatement initCall = make.at(diagPos).Exec(make.at(diagPos).Apply(null,
                make.at(diagPos).Ident(defs.initializeName),
                List.<JCExpression>nil()));
        return makeConstructor(diagPos, List.<JCVariableDecl>nil(), List.of(thisCall, initCall));
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
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        if (outerTypeSym != null) {
               // add a parameter and a statement to constructor for the outer instance reference
                params.append( make.VarDef(make.Modifiers(0L), outerAccessorFieldName, make.Ident(outerTypeSym), null) );
                JCFieldAccess cSelect = make.Select(make.Ident(names._this), outerAccessorFieldName);
                JCAssign assignStat = make.Assign(cSelect, make.Ident(outerAccessorFieldName));
                stmts.append(make.Exec(assignStat));            
        }
        Name dummyParamName = names.fromString("dummy");
        params.append( make.at(diagPos).VarDef(
                make.Modifiers(Flags.PARAMETER),
                dummyParamName,
                makeTypeTree( diagPos,syms.booleanType),
                null) );
        if (superIsFX) {
            // call the FX version of the constructor
            stmts.append(make.Exec(make.Apply(null,
                    make.Ident(names._super),
                    List.<JCExpression>of(make.Ident(dummyParamName)))));
        }
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
                // If FINAL class, it is an anonymous class. There is no interface for it and we need to have the type of the
                // type, not it's interface.
                return (typeOwner.flags_field & Flags.FINAL) != 0 ? (ClassSymbol)typeOwner.type.tsym :
                        reader.jreader.enterClass(names.fromString(typeOwner.type.toString() + interfaceSuffix));
            }
        }
        return null;
   }
    
    // Make the field for accessing the outer members
    private JCTree makeClassOuterAccessorField(DiagnosticPosition diagPos, JFXClassDeclaration cdecl, ClassSymbol outerTypeSym) {
        // Create the field to store the outer instance reference
        return make.at(diagPos).VarDef(make.at(diagPos).Modifiers(Flags.PUBLIC), outerAccessorFieldName, make.Ident(outerTypeSym), null);
    }

    // Make the method for accessing the outer members
    private JCTree makeClassOuterAccessorMethod(DiagnosticPosition diagPos, JFXClassDeclaration cdecl, ClassSymbol outerTypeSym) {
        make.at(diagPos);
        VarSymbol vs = new VarSymbol(Flags.PUBLIC, outerAccessorFieldName, outerTypeSym.type, cdecl.sym);
        JCIdent retIdent = make.Ident(vs);
        JCStatement retRet = make.Return(retIdent);
        List<JCStatement> mStats = List.of(retRet);
        return make.MethodDef(make.Modifiers(Flags.PUBLIC), outerAccessorName, make.Ident(outerTypeSym), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
                List.<JCExpression>nil(), make.Block(0L, mStats), null);
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

    private List<JCTree> makeInterfaceAttributeGetterMethods(DiagnosticPosition diagPos, List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> getters = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (!ai.isStatic()) {
                JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT);
                getters.append(make.MethodDef(
                        mods,
                        attributeGetterName(ai.getSymbol()),
                        makeTypeTree( null,ai.getVariableType()),
                        List.<JCTypeParameter>nil(),
                        List.<JCVariableDecl>nil(),
                        List.<JCExpression>nil(),
                        null, null));
            }
        }
        return getters.toList();
    }

    private List<JCTree> makeClassAttributeGetterMethods(JFXClassDeclaration cDecl, List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> getters = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning()) {
                long flags = ai.getFlags();
                DiagnosticPosition diagPos = ai.pos();
                Name attribName = attributeFieldName(ai.getSymbol());
                Name methodName = attributeGetterName(ai.getSymbol());

                // Add the return statement for the attribute
                JCExpression value = make.Ident(attribName);
                JCStatement returnStat = make.at(diagPos).Return(value);
                JCBlock statBlock = make.at(diagPos).Block(0L, List.of(returnStat));

                // Add the method for this class' attributes
                JCModifiers mods = make.Modifiers(Flags.PUBLIC);
                if (ai.getSymbol().owner == cDecl.sym)
                    mods = addAccessAnnotationModifiers(diagPos, flags, mods);
                else
                    mods = addInheritedAnnotationModifiers(diagPos, flags, mods);
                getters.append(make.at(diagPos).MethodDef(
                        mods,
                        methodName,
                        makeTypeTree( null,ai.getVariableType()),
                        List.<JCTypeParameter>nil(),
                        List.<JCVariableDecl>nil(),
                        List.<JCExpression>nil(),
                        statBlock,
                        null));
            }
        }
        return getters.toList();
    }
        
    private boolean isAttributeOriginClass(Symbol csym, Symbol attr) {
        if (types.isJFXClass(csym)) {
            ClassSymbol supertypeSym = (ClassSymbol) csym;
            for (Entry e = supertypeSym.members().elems; e != null && e.sym != null; e = e.sibling) {
                if (attr.owner == csym)
                    return true;
                if ((attr.flags() & PRIVATE) != 0)
                    return false;
                if ((e.sym.kind == Kinds.VAR && e.sym.name == attr.name)) {
                    // Needed to handle override.
                    return true;
                }
            }
            // not directly in this class, try in the superclass
            for (Type supertype : supertypeSym.getInterfaces()) {
                if (isAttributeOriginClass(supertype.tsym, attr) ) {
                    return true;
                }
            }
        }
        return false;
    }
        
    /**
     * Construct the applyDefaults method
     */
    private List<JCTree> makeClassAttributeApplyDefaultsMethods(DiagnosticPosition diagPos,
            JFXClassDeclaration cDecl,
            List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> methods = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning()) {
                Name methodName = attributeApplyDefaultsName(ai.getSymbol());
                ListBuffer<JCStatement> stmts = ListBuffer.lb();

                if (ai.getDefaultInitializtionStatement() != null) {
                    // a default exists, either on the direct attribute or on an override
                    stmts.append(ai.getDefaultInitializtionStatement());
                } else {
                    // no default, look for the supertype which defines it, and defer to it
                    ClassSymbol attrParent = null;
                    //Name getterName = attributeName(ai.getSymbol(), attributeGetMethodNamePrefix);
                    for (JFXExpression supertype : cDecl.getSupertypes()) {
                        Symbol sym = supertype.type.tsym;
                        if (isAttributeOriginClass(sym, ai.getSymbol()) ) {
                            attrParent = (ClassSymbol) sym;
                            break;
                        }
                    }
                    assert attrParent != null : "Parent supertype for attribute " + ai.getNameString() + " not found";
                    if (attrParent != null) {
                        stmts.append( makeSuperCall(diagPos, attrParent, methodName) );
                    }
                }
                JCBlock statBlock = make.at(diagPos).Block(0L, stmts.toList());

                // Add the method for this class' attributes
                JCModifiers mods = make.Modifiers(Flags.PUBLIC | (cDecl.generateClassOnly()? 0L : Flags.STATIC) );
                methods.append(make.at(diagPos).MethodDef(
                        mods,
                        methodName,
                        makeTypeTree( null,syms.voidType),
                        List.<JCTypeParameter>nil(),
                        List.<JCVariableDecl>of(makeReceiverParam(cDecl)),
                        List.<JCExpression>nil(),
                        statBlock,
                        null));
            }
        }
        return methods.toList();
    }
    
    private JCStatement makeSuperCall(DiagnosticPosition diagPos, ClassSymbol cSym, Name methodName) {
        JCExpression receiver;
        List<JCExpression> arg = List.<JCExpression>of(make.at(diagPos).Ident(defs.receiverName));
        if ((cSym.flags() & JavafxFlags.COMPOUND_CLASS) != 0) {
            // call to a compound super, use static reference
            receiver = makeTypeTree( diagPos,cSym.type, false);
        } else {
            // call to a non-compound super, use "super"
            receiver = make.at(diagPos).Ident(names._super);
        }
        return callStatement(diagPos, receiver, methodName, arg);
    }

    private List<JCStatement> makeAllSuperCalls(DiagnosticPosition diagPos,
            List<ClassSymbol> javafxClasses, Name methodName) {
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        for (ClassSymbol cSym : javafxClasses) {
            stmts.append( makeSuperCall(diagPos, cSym, methodName) );
        }
        return stmts.toList();
    }
    
    private JCMethodDecl makeInitializeMethod(DiagnosticPosition diagPos,
            List<VarInfo> attrInfos,
            JFXClassDeclaration cDecl) {
        boolean classIsFinal = (cDecl.getModifiers().flags & Flags.FINAL) != 0;
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // Add calls to do the the default value initialization and user init code (validation for example.)
        
        // "addTriggers$(this);"
       stmts.append( callStatement(
               diagPos, 
               null, 
               defs.addTriggersName, 
               make.at(diagPos).Ident(names._this)));

       // "initAttributes$(this);"
        stmts.appendList( makeInitAttributesCode(attrInfos, cDecl) );
        
        // "userInit$(this);"
        stmts.append(callStatement(
                diagPos, 
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                defs.userInitName, 
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));
        
        // "postInit$(this);"
        stmts.append(callStatement(
                diagPos,
                make.Ident(classIsFinal? names._this : cDecl.getName()),
                defs.postInitName,
                make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this))));

        // "InitHelper.finish(new[] { attribute, ... });
        ListBuffer<JCExpression> attrs = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning()) {
                attrs.append(make.at(diagPos).Ident(attributeFieldName(ai.getSymbol())));
            }
        }                

        stmts.append( callStatement(diagPos, 
                makeTypeTree(diagPos, initHelperType), 
                "finish",
                make.NewArray(makeTypeTree(diagPos, abstractVariableType), 
                                List.<JCExpression>nil(), attrs.toList())));

        JCBlock initializeBlock = make.Block(0L, stmts.toList());
        return make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                defs.initializeName,
                makeTypeTree( null,syms.voidType),
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>nil(), 
                List.<JCExpression>nil(), 
                initializeBlock, null);
    }
    
    // Add the initialization of this class' attributes
    private List<JCStatement> makeInitAttributesCode(List<VarInfo> attrInfos,
            JFXClassDeclaration cDecl) {
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if ((ai.getFlags() & Flags.STATIC) == 0) {
                DiagnosticPosition diagPos = ai.pos();
                Name methodName = attributeApplyDefaultsName(ai.getSymbol());

                List<JCExpression> arg = List.<JCExpression>of(make.at(diagPos).Ident(names._this));
                JCStatement applyDefaultsCall = callStatement(diagPos, null, methodName, arg);
                JCExpression needsDefaultCond = callExpression(diagPos,
                        make.at(diagPos).Ident(attributeFieldName(ai.getSymbol())),
                        defs.needDefaultsMethodName);
                JCStatement protectedCall = make.If(needsDefaultCond, applyDefaultsCall, null);
                stmts.append( protectedCall );
            }
        }
        return stmts.toList();
    }
    
    /**
     * Construct the static block for setting defaults
     * */
    private JCBlock makeInitStaticAttributesBlock(JFXClassDeclaration cDecl,
            List<TranslatedVarInfo> translatedAttrInfo) {
        // Add the initialization of this class' attributesa
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        boolean isLibrary = toJava.attrEnv.toplevel.isLibrary;
        for (TranslatedVarInfo tai : translatedAttrInfo) {
            assert tai.var != null;
            assert tai.var.getFXTag() == JavafxTag.VAR_DEF;
            assert tai.var.pos != Position.NOPOS;
            if (tai.isStatic()) {
                DiagnosticPosition diagPos = tai.pos();
                // don't put variable initialization in the static initializer if this is a simple-form
                // script (where variable initialization is done in the run method).
                if (tai.isDirectOwner() && (isLibrary || (tai.getFlags() | JavafxFlags.SCRIPT_LEVEL_SYNTH_STATIC) == 0)) {
                    if (tai.getDefaultInitializtionStatement() != null) {
                        stmts.append(tai.getDefaultInitializtionStatement());
                    }
                    stmts.append( callStatement(diagPos, make.at(diagPos).Ident(attributeFieldName(tai.getSymbol())), locationInitializeName));
                }
                JCStatement stat = makeChangeListenerCall(tai);
                if (stat != null) {
                    stmts.append(stat);
                }
            }
        }
        return make.at(cDecl.pos()).Block(Flags.STATIC, stmts.toList());
    }

    /**
     * Construct the addTriggers method
     * */
    private JCMethodDecl makeAddTriggersMethod(DiagnosticPosition diagPos, 
                                               JFXClassDeclaration cDecl,
                                               List<ClassSymbol> javafxSupers,
                                               List<TranslatedVarInfo> translatedAttrInfo,
                                               List<TranslatedOverrideClassVarInfo> translatedTriggerInfo) {
        ListBuffer<JCStatement> stmts = ListBuffer.lb();

        // call the superclasses addTriggers
        stmts.appendList( makeAllSuperCalls(diagPos, javafxSupers, defs.addTriggersName) );

        // add change listeners for triggers on attribute definitions
        for (TranslatedVarInfo info : translatedAttrInfo) {
            if (!info.isStatic()) {
                JCStatement stat = makeChangeListenerCall(info);
                if (stat != null) {
                    stmts.append(stat);
                }
            }
        }

        // add change listeners for "with" triggers
        for (TranslatedOverrideClassVarInfo info : translatedTriggerInfo) {
            if (!info.isStatic()) {
                JCStatement stat = makeChangeListenerCall(info);
                if (stat != null) {
                    stmts.append(stat);
                }
            }
        }

        return make.at(diagPos).MethodDef(
                make.Modifiers(Flags.PUBLIC | (cDecl.generateClassOnly()? 0L : Flags.STATIC) ),
                defs.addTriggersName,
                makeTypeTree( null,syms.voidType),
                List.<JCTypeParameter>nil(),
                List.<JCVariableDecl>of( makeReceiverParam(cDecl) ),
                List.<JCExpression>nil(),
                make.Block(0L, stmts.toList()),
                null);
    }

    // build a field for each non-static attribute (including inherited).
    // and for static attributes of this class
    private List<JCTree> makeAttributeFields(Symbol csym, List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> fields = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning()) {
                DiagnosticPosition diagPos = ai.pos();
                JCModifiers mods = make.Modifiers(Flags.PUBLIC | Flags.FINAL | (ai.getFlags() & Flags.STATIC));
                Symbol sym = ai.getSymbol();
                Name fieldName = attributeFieldName(ai.getSymbol());
                if (sym.owner == csym) {
                    List<JCAnnotation> annotations;
                    if (fieldName != sym.name) {
                        annotations = List.<JCAnnotation>of(make.Annotation(makeIdentifier(diagPos, syms.sourceNameAnnotationClassNameString), List.<JCExpression>of(make.Literal(sym.name.toString()))));
                    }
                    else
                        annotations = List.<JCAnnotation>nil();
                    mods = addAccessAnnotationModifiers(diagPos, sym.flags(), mods, annotations);
                }
                else
                    mods = addInheritedAnnotationModifiers(diagPos, sym.flags(), mods);
                JCVariableDecl var = make.at(diagPos).VarDef(
                        mods,
                        fieldName,
                        makeTypeTree( diagPos,ai.getVariableType()),
                        makeLocationAttributeVariable(ai.getVMI(), diagPos));
                fields.append(var);
            }
        }
        return fields.toList();
    }
    
             
    /**
     * Non-destructive creation of "on change" change listener set-up call.
     */
    JCStatement makeChangeListenerCall(VarInfo info) {
        
        //TODO: TranslatedAttributeInfo should be simplified to hold onReplace attribute only
        //
        JFXOnReplace onReplace = info.onReplace();
        if (onReplace == null) return null;
        
        DiagnosticPosition diagPos = info.pos();
        ListBuffer<JCTree> members = ListBuffer.lb();

        JCExpression changeListener;
        List<JCExpression> emptyTypeArgs = List.nil();
        int attributeKind = typeMorpher.varMorphInfo(info.getSymbol()).getTypeKind();
        
        if (types.isSequence(info.getRealType())) {
            ListBuffer<JCStatement> setUpStmts = ListBuffer.lb();
//            changeListener = make.at(diagPos).Identifier(sequenceReplaceListenerInterfaceName);
            changeListener = makeIdentifier(diagPos, sequenceChangeListenerInterfaceName);
            changeListener = make.TypeApply(changeListener, List.of(makeTypeTree( diagPos,info.getElementType())));
            Type seqValType = types.sequenceType(info.getElementType(), false);
            List<JCVariableDecl> onChangeArgs = List.of(
                makeIndexParam(diagPos, onReplace),
                makeParam(diagPos, syms.intType, onReplace.getLastIndex(), "$lastIndex$"),
                makeParam(diagPos, info.getRealType(), onReplace.getNewElements(), "$newElements$"),
                makeParam(diagPos, seqValType, onReplace.getOldValue(), "$oldValue$"),
                makeParam(diagPos, seqValType, null, "$newValue$"));
   //         members.append(makeChangeListenerMethod(diagPos, onReplace, setUpStmts, "onReplace", onChangeArgs, TypeTags.VOID));
            members.append(makeChangeListenerMethod(diagPos, onReplace, info.onReplaceTranslatedBody(), setUpStmts, "onChange", onChangeArgs, TypeTags.VOID));
        }
        else {
            changeListener = makeIdentifier(diagPos, changeListenerInterfaceName[attributeKind]);
            members.append(makeOnReplaceChangeListenerMethod(diagPos, onReplace, info.onReplaceTranslatedBody(), info.getRealType()));
        }

        if (attributeKind == JavafxVarSymbol.TYPE_KIND_OBJECT)
            changeListener = make.at(diagPos).TypeApply(changeListener,
                                                        List.<JCExpression>of(makeTypeTree( diagPos,info.getRealType())));
        JCNewClass anonymousChangeListener = make.NewClass(
                null, 
                emptyTypeArgs,
                changeListener, 
                List.<JCExpression>nil(), 
                make.at(diagPos).AnonymousClassDef(make.Modifiers(0L), members.toList()));

        JCExpression varRef;
        if (info.getSymbol().owner.kind == Kinds.TYP) {
            // on replace is on class variable
            varRef = makeAttributeAccess(diagPos, info.getSymbol(),
                    info.getSymbol().isStatic()? null : defs.receiverName);
        } else {
            // on replace is on local variable
            varRef = make.at(diagPos).Ident(info.getName());
        }
        JCFieldAccess tmpSelect = make.at(diagPos).Select(varRef, addChangeListenerName);

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
                makeTypeTree(diagPos, type),
                null);
        
    }
    
    private JCVariableDecl makeIndexParam(DiagnosticPosition diagPos, JFXOnReplace onReplace) {
 //       return makeParam(diagPos, syms.intType, onReplace == null ? null : onReplace.getIndex(), "$index$");
        return makeParam(diagPos, syms.intType, onReplace == null ? null : onReplace.getFirstIndex(), "$index$");
    }
     
    
    /**
     * construct a change listener method for insertion in a listener anon class.
     *   void onReplace(...); ...
     */
    private JCMethodDecl makeOnReplaceChangeListenerMethod(DiagnosticPosition diagPos,
                                                           JFXOnReplace onReplace,
                                                           JCBlock onReplaceTranslatedBody,
                                                           Type attributeType) {
        List<JCVariableDecl> onChangeArgs = List.<JCVariableDecl>nil()
                .append(make.VarDef(make.Modifiers(0L), onChangeArgName1, makeTypeTree(diagPos, attributeType), null))
                .append(make.VarDef(make.Modifiers(0L), onChangeArgName2, makeTypeTree(diagPos, attributeType), null));
        ListBuffer<JCStatement> setUpStmts = ListBuffer.lb();
        if (onReplace != null && onReplace.getOldValue() != null) {
            // Create the variable for the old value, using the specified name
            JFXVar oldValue = onReplace.getOldValue();
            VarMorphInfo vmi = typeMorpher.varMorphInfo(oldValue.sym);

            setUpStmts.append( 
                    make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        oldValue.getName(), 
                        makeTypeTree( diagPos, vmi.getRealType(),types.isJFXClass(vmi.getRealType().tsym)),
                        makeIdentifier(diagPos, onChangeArgName1)));
        }
        if (onReplace != null && onReplace.getNewElements() != null) {
            // Create the variable for the new value, using the specified name
            JFXVar newValue = onReplace.getNewElements();
            VarMorphInfo vmi = typeMorpher.varMorphInfo(newValue.sym);

            setUpStmts.append( 
                    make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        newValue.getName(), 
                        makeTypeTree( diagPos, vmi.getRealType(),types.isJFXClass(vmi.getRealType().tsym)),
                        makeIdentifier(diagPos, onChangeArgName2)));
        }
        return makeChangeListenerMethod(diagPos, onReplace, onReplaceTranslatedBody, setUpStmts, "onChange", onChangeArgs, TypeTags.VOID);
    }

    /**
     * construct a change listener method for insertion in a listener anon class.
     *   boolean onChange();
     *   void onInsert(...);
     *   void on Delete(...); ...
     */
    private JCMethodDecl makeChangeListenerMethod(
            DiagnosticPosition diagPos,
            JFXOnReplace onReplace,
            JCBlock onReplaceTranslatedBody,
            ListBuffer<JCStatement> prefixStmts,
            String methodName,
            List<JCVariableDecl> args,
            int returnTypeTag) {
        ListBuffer<JCStatement> ocMethStmts = ListBuffer.lb();
        ocMethStmts.appendList(prefixStmts);
        
        if (onReplace != null) {
            diagPos = onReplace.pos();
            ocMethStmts.appendList(onReplaceTranslatedBody.getStatements());
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
        JCExpression receiver = makeTypeTree( diagPos,mth.owner.type, false);
        JCExpression expr = callExpression(diagPos, receiver, functionName(mth, !isStatic, isBound), args);
        JCStatement statement = (mth.getReturnType() == syms.voidType) ? make.Exec(expr) : make.Return(expr);
        return make.at(diagPos).Block(0L, List.<JCStatement>of(statement));
    }

    protected String getSyntheticPrefix() {
        return "ifx$";
    }
}
