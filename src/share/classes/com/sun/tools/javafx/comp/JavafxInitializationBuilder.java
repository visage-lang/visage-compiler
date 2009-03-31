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

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedOverrideClassVarInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.TranslatedVarInfo;
import com.sun.tools.javafx.comp.JavafxAnalyzeClass.VarInfo;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

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
    private final JavafxOptimizationStatistics optStat;
    
    private final Name addChangeListenerName;
    private final Name addSequenceChangeListenerName;
    private final Name locationInitializeName;
    private final Name changeListenerInterfaceName;
    private static final String initHelperClassName = "com.sun.javafx.runtime.InitHelper";
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
        optStat = JavafxOptimizationStatistics.instance(context);
        
        addChangeListenerName = names.fromString("addChangeListener");
        addSequenceChangeListenerName = names.fromString("addSequenceChangeListener");
        locationInitializeName = names.fromString("initialize");
        changeListenerInterfaceName = names.fromString(locationPackageNameString + ".ChangeListener");
        outerAccessorName = names.fromString("accessOuter$");
        outerAccessorFieldName = names.fromString("accessOuterField$");
        
        {
            Name name = names.fromString(initHelperClassName);
            ClassSymbol sym = reader.enterClass(name);
            initHelperType = sym.type;
        }
        {
            Name name = names.fromString(locationPackageNameString + ".AbstractVariable");
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
        boolean isMixinClass = cDecl.isMixinClass();
        DiagnosticPosition diagPos = cDecl.pos();
        Type superType = types.superType(cDecl);
        ClassSymbol outerTypeSym = outerTypeSymbol(cDecl); // null unless inner class with outer reference

        JavafxAnalyzeClass analysis = new JavafxAnalyzeClass(diagPos,
                cDecl.sym, translatedAttrInfo, translatedOverrideAttrInfo,
                names, types, reader, typeMorpher);
        List<VarInfo> instanceAttributeInfos = analysis.instanceAttributeInfos();
        List<VarInfo> staticAttributeInfos = analysis.staticAttributeInfos();
        List<MethodSymbol> needDispatch = analysis.needDispatch();
        List<ClassSymbol> mixinClasses = immediateMixinNames(cDecl);
        List<ClassSymbol> supertypeClasses = immediateJavafxSupertypes(cDecl);

        ListBuffer<JCTree> cDefinitions = ListBuffer.lb();  // additional class members needed
        ListBuffer<JCTree> iDefinitions = ListBuffer.lb();
         
        if (!isMixinClass) {
            cDefinitions.appendList(makeAttributeFields(cDecl.sym, instanceAttributeInfos));
            cDefinitions.appendList(makeAttributeFields(cDecl.sym, staticAttributeInfos));
            cDefinitions.appendList(makeNeedsDefaultFields(instanceAttributeInfos));
            cDefinitions.appendList(makeApplyDefaultsMethods(diagPos, cDecl, instanceAttributeInfos));
            cDefinitions.appendList(makeMemberVariableAccessorMethods(cDecl, instanceAttributeInfos));
            cDefinitions.append(makeInitStaticAttributesBlock(cDecl, translatedAttrInfo));
            cDefinitions.append(makeInitializeMethod(diagPos, instanceAttributeInfos, cDecl));

            if (outerTypeSym == null) {
                cDefinitions.append(makeJavaEntryConstructor(diagPos));
            } else {
                cDefinitions.append(makeOuterAccessorField(diagPos, cDecl, outerTypeSym));
                cDefinitions.append(makeOuterAccessorMethod(diagPos, cDecl, outerTypeSym));
            }
            
            cDefinitions.append(makeAddTriggersMethod(diagPos, cDecl, supertypeClasses, translatedAttrInfo, translatedOverrideAttrInfo));
            cDefinitions.appendList(makeFunctionProxyMethods(cDecl, needDispatch));
            cDefinitions.append(makeFXEntryConstructor(diagPos, outerTypeSym, superType != null && types.isJFXClass(superType.tsym)));
        } else {
            cDefinitions.appendList(makeAttributeFields(cDecl.sym, instanceAttributeInfos));
            cDefinitions.appendList(makeAttributeFields(cDecl.sym, staticAttributeInfos));
            cDefinitions.appendList(makeApplyDefaultsMethods(diagPos, cDecl, instanceAttributeInfos));
            iDefinitions.appendList(makeMemberVariableAccessorInterfaceMethods(diagPos, translatedAttrInfo));
            iDefinitions.appendList(makeFunctionInterfaceMethods(cDecl));
            iDefinitions.appendList(makeOuterAccessorInterfaceMembers(cDecl));
            cDefinitions.append(makeAddTriggersMethod(diagPos, cDecl, supertypeClasses, translatedAttrInfo, translatedOverrideAttrInfo));
         }

        Name interfaceName = isMixinClass ? interfaceName(cDecl) : null;

        return new JavafxClassModel(
                interfaceName,
                makeImplementingInterfaces(diagPos, cDecl, mixinClasses),
                iDefinitions.toList(),
                cDefinitions.toList(),
                makeAdditionalImports(diagPos, cDecl, mixinClasses),
                superType);
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

   
    private List<ClassSymbol> immediateMixinNames(JFXClassDeclaration cDecl) {
        ListBuffer<ClassSymbol> javaInterfacesBuff = ListBuffer.lb();
        for (JFXExpression sup : cDecl.getSupertypes()) {
            ClassSymbol cSym = (ClassSymbol) expressionSymbol(sup);
            if (cSym != null) {
                String className = cSym.fullname.toString();
                boolean isFXInterface = className.endsWith(mixinSuffix);

                if (!isFXInterface &&
                        cSym.fullname != defs.fxObjectName &&
                        isMixinClass(cSym) &&
                        cSym.type != null) {
                    javaInterfacesBuff.append(cSym);
                }
            }
        }
        return javaInterfacesBuff.toList();
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
            implementing.append(makeTypeTree(diagPos, intf.type, false));
        }

        for (ClassSymbol baseClass : baseInterfaces) {
            implementing.append(makeTypeTree(diagPos, baseClass.type, true));
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
        Name dummyParamName = names.fromString("dummy");
        make.at(diagPos);     
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        if (superIsFX) {
            // call the FX version of the constructor
            stmts.append(make.Exec(make.Apply(null,
                    make.Ident(names._super),
                    List.<JCExpression>of(make.Ident(dummyParamName)))));
        }
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        if (outerTypeSym != null) {
               // add a parameter and a statement to constructor for the outer instance reference
                params.append( make.VarDef(make.Modifiers(0L), outerAccessorFieldName, make.Ident(outerTypeSym), null) );
                JCFieldAccess cSelect = make.Select(make.Ident(names._this), outerAccessorFieldName);
                JCAssign assignStat = make.Assign(cSelect, make.Ident(outerAccessorFieldName));
                stmts.append(make.Exec(assignStat));            
        }
        params.append( make.at(diagPos).VarDef(
                make.Modifiers(Flags.PARAMETER),
                dummyParamName,
                makeTypeTree( diagPos,syms.booleanType),
                null) );
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
        return make.MethodDef(make.Modifiers(Flags.PUBLIC), outerAccessorName, make.Ident(outerTypeSym), List.<JCTypeParameter>nil(), List.<JCVariableDecl>nil(),
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
                ClassSymbol returnSym = typeMorpher.reader.enterClass(names.fromString(typeOwner.type.toString() + mixinSuffix));
                JCMethodDecl accessorMethod = make.MethodDef(
                        make.Modifiers(Flags.PUBLIC), 
                        outerAccessorName, 
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

    private Type morphedType(VarInfo ai) {
        return requiresLocation(ai) ? ai.getVariableType() : ai.getRealType();
    }

    private JCMethodDecl makeGetterMethod(DiagnosticPosition diagPos, VarInfo ai, JCModifiers mods, JCBlock statBlock) {
        final VarSymbol vsym = ai.getSymbol();
        return make.at(diagPos).MethodDef(
                mods,
                attributeGetterName(vsym),
                makeTypeTree(diagPos, morphedType(ai)),
                List.<JCTypeParameter>nil(),
                List.<JCVariableDecl>nil(),
                List.<JCExpression>nil(),
                statBlock,
                null);
    }

    private JCMethodDecl makeSetterMethod(DiagnosticPosition diagPos, VarInfo ai, JCModifiers mods, JCBlock block) {
        final VarSymbol vsym = ai.getSymbol();
        return make.at(diagPos).MethodDef(
                mods,
                attributeSetterName(vsym),
                makeTypeTree(diagPos, ai.getRealType()),
                List.<JCTypeParameter>nil(),
                List.of(makeParam(diagPos, ai.getRealType(), null, defs.attributeSetMethodParamName)),
                List.<JCExpression>nil(),
                block,
                null);
    }

    private List<JCTree> makeMemberVariableAccessorInterfaceMethods(DiagnosticPosition diagPos, List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> accessors = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (!ai.isStatic()) {
                accessors.append(makeGetterMethod(diagPos, ai, make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT), null));
                if (!requiresLocation(ai)) {
                    accessors.append(makeSetterMethod(diagPos, ai, make.Modifiers(Flags.PUBLIC | Flags.ABSTRACT), null));
                }
            }
        }
        return accessors.toList();
    }

    private JCModifiers proxyModifiers(VarInfo ai, JFXClassDeclaration cDecl) {
        long flags = ai.getFlags();
        JCModifiers mods = make.Modifiers(Flags.PUBLIC);
        if (ai.getSymbol().owner == cDecl.sym) {
            mods = addAccessAnnotationModifiers(ai.pos(), flags, mods);
        } else {
            mods = addInheritedAnnotationModifiers(ai.pos(), flags, mods);
        }
        return mods;
    }

    private List<JCTree> makeMemberVariableAccessorMethods(JFXClassDeclaration cDecl, List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> accessors = ListBuffer.lb();
        boolean isMixinClass = cDecl.isMixinClass();
        
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning()) {
                final DiagnosticPosition diagPos = ai.pos();
                final VarSymbol vsym = ai.getSymbol();
                final VarSymbol proxyVarSym = ai.proxyVarSym();

                {
                    // Add the return statement for the attribute
                    JCExpression value = make.Ident(attributeFieldName(proxyVarSym));
                    JCStatement returnStat = make.at(diagPos).Return(value);

                    // Add the method for this class' attributes
                    JCBlock block = make.at(diagPos).Block(0L, List.of(returnStat));
                    accessors.append(makeGetterMethod(diagPos, ai, proxyModifiers(ai, cDecl), block));
                }
                if (!requiresLocation(ai)) {
                    // Add setter method
                    ListBuffer<JCStatement> stmts = ListBuffer.lb();

                    if (!ai.isDef() && !isMixinClass) {
                        stmts.append(clearNeedsDefault(diagPos, proxyVarSym));
                    }

                    // Set value
                    JCExpression attr = make.at(diagPos).Ident(attributeFieldName(proxyVarSym));
                    JCExpression value = make.at(diagPos).Ident(defs.attributeSetMethodParamName);
                    JCExpression assign = make.at(diagPos).Assign(attr, value);
                    stmts.append(make.at(diagPos).Return(assign));

                    // Add setter method
                    JCBlock block = make.at(diagPos).Block(0L, stmts.toList());
                    accessors.append(makeSetterMethod(diagPos, ai, proxyModifiers(ai, cDecl), block));
                }

                optStat.recordProxyMethod();
            }
        }
        return accessors.toList();
    }
        
    /**
     * Construct a var$needs_default$ = false; statement.
     */
    private JCStatement clearNeedsDefault(DiagnosticPosition diagPos, VarSymbol sym) {
      JCExpression needsDefaultField = make.at(diagPos).Ident(attributeNeedsDefaultFieldName(sym));
      JCExpression mark = make.at(diagPos).Assign(needsDefaultField, make.at(diagPos).Literal(TypeTags.BOOLEAN, 0));
      return make.at(diagPos).Exec(mark);
    }
        
    /**
     * Construct the applyDefaults methods
     */
    private List<JCTree> makeApplyDefaultsMethods(DiagnosticPosition diagPos,
            JFXClassDeclaration cDecl,
            List<? extends VarInfo> attrInfos) {
        boolean isMixinClass = cDecl.isMixinClass();
        ListBuffer<JCTree> methods = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning() && !ai.hasProxyVar()) {
                Name methodName = attributeApplyDefaultsName(ai.getSymbol());
                ListBuffer<JCStatement> stmts = ListBuffer.lb();

                if (ai.getDefaultInitStatement() != null) {
                    /* TODO JFXC-2836
                    if (!ai.isDef() && !isMixinClass && !requiresLocation(ai)) {
                        stmts.append(clearNeedsDefault(diagPos, ai.proxyVarSym()));
                    }
                    */
                    
                    // a default exists, either on the direct attribute or on an override
                    stmts.append(ai.getDefaultInitStatement());
                } else if (ai.isMixinVar()) {
                    /* TODO JFXC-2836
                    if (ai.isMixinVar() && !ai.isDef() && !isMixinClass && !requiresLocation(ai)) {
                        stmts.append(clearNeedsDefault(diagPos, ai.proxyVarSym()));
                    }
                    */
                    
                    ClassSymbol attrParent = (ClassSymbol)ai.getSymbol().owner;
                    assert attrParent != null : "Parent supertype for attribute " + ai.getNameString() + " not found";
                    if (attrParent != null) {
                        stmts.append(makeSuperCall(diagPos, attrParent, methodName, false));
                    }
                }
                
                JCBlock statBlock = make.at(diagPos).Block(0L, stmts.toList());

                // Add the method for this class' attributes
                JCModifiers mods = make.Modifiers(Flags.PUBLIC | (isMixinClass ? Flags.STATIC : 0L) );
                methods.append(make.at(diagPos).MethodDef(
                        mods,
                        methodName,
                        makeTypeTree( null,syms.voidType),
                        List.<JCTypeParameter>nil(),
                        List.<JCVariableDecl>of(makeReceiverParam(cDecl)),
                        List.<JCExpression>nil(),
                        statBlock,
                        null));
                optStat.recordProxyMethod();
            }
        }
        return methods.toList();
    }
    
    private JCStatement makeSuperCall(DiagnosticPosition diagPos, ClassSymbol cSym, Name methodName, boolean isStatic) {
        JCExpression arg = make.at(diagPos).Ident(defs.receiverName);
        JCExpression receiver;
        
        if (isStatic || (cSym.flags() & JavafxFlags.MIXIN) != 0) {
            // call to a mixin super, use local static reference
            receiver = makeTypeTree(diagPos, cSym.type, false);
        } else {
            // cast the arg to the right type
            // TODO JFXC-2836
            // arg = make.at(diagPos).TypeCast(make.Ident(cSym.fullname), arg);
            // call to a non-mixin super, use "super"
            receiver = make.at(diagPos).Ident(names._super);
        }
        
        List<JCExpression> args = List.<JCExpression>of(arg);
        return callStatement(diagPos, receiver, methodName, args);
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
                null,
                defs.userInitName, 
                make.at(diagPos).Ident(names._this)));
        
        // "postInit$(this);"
        stmts.append(callStatement(
                diagPos,
                null,
                defs.postInitName,
                make.at(diagPos).Ident(names._this)));

        // "InitHelper.finish(new[] { attribute, ... });
        ListBuffer<JCExpression> finishAttrs = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning() && requiresLocation(ai) && !ai.hasProxyVar()) {
                final VarSymbol vsym = ai.getSymbol();
                finishAttrs.append(make.at(diagPos).Ident(attributeFieldName(vsym)));
            }
        }                

        stmts.append( callStatement(diagPos, 
                makeTypeTree(diagPos, initHelperType), 
                "finish",
                make.NewArray(makeTypeTree(diagPos, abstractVariableType), 
                                List.<JCExpression>nil(), finishAttrs.toList())));

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
    
    // Add the initialization of instance variables
    private List<JCStatement> makeInitAttributesCode(List<? extends VarInfo> attrInfos,
            JFXClassDeclaration cDecl) {
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (!ai.hasProxyVar() && !ai.isStatic()) {
                DiagnosticPosition diagPos = ai.pos();
                VarSymbol vsym = ai.getSymbol();
                Name methodName = attributeApplyDefaultsName(vsym);

                List<JCExpression> applyDefaultsArg = List.<JCExpression>of(make.at(diagPos).Ident(names._this));
                JCStatement applyDefaultsCall = callStatement(diagPos, null, methodName, applyDefaultsArg);
                if (!ai.isDef()) {
                    JCExpression cond;
                    if (requiresLocation(ai)) {
                        Name fieldName = attributeFieldName(vsym);
                        cond = callExpression(diagPos,
                                make.at(diagPos).Ident(fieldName),
                                defs.needDefaultsMethodName);
                    } else {
                        Name fieldName = attributeNeedsDefaultFieldName(vsym);
                        cond = make.at(diagPos).Ident(fieldName);
                    }
                    applyDefaultsCall = make.If(cond, applyDefaultsCall, null);
                }
                stmts.append(applyDefaultsCall);
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
                if (tai.isDirectOwner() && isLibrary) {
                    if (tai.getDefaultInitStatement() != null) {
                        stmts.append(tai.getDefaultInitStatement());
                    }
                    if (requiresLocation(tai)) {
                        // If the static variable is represented with a Location, initialize it
                        stmts.append(callStatement(diagPos, make.at(diagPos).Ident(attributeFieldName(tai.getSymbol())), locationInitializeName));
                    }
                }
                JCStatement stat = makeChangeListenerCall(tai);
                if (stat != null) {
                    stmts.append(stat);
                }
            }
        }
        return make.at(cDecl.pos()).Block(Flags.STATIC, stmts.toList());
    }
     
    
    // 
    // Return the super class symbol if it is a FX class.
    // 
    private ClassSymbol getSuperSymbol(JFXClassDeclaration cDecl) {
        List<JFXExpression> extending = cDecl.getExtending();
        if (!extending.isEmpty()) {
            JFXExpression parent = extending.get(0);
            if (parent instanceof JFXIdent) {
                Symbol symbol = expressionSymbol(parent);
                if (types.isJFXClass(symbol)) {
                    return (ClassSymbol)symbol;
                }
            }
        }
        
        return null;
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
        boolean isMixinClass = cDecl.isMixinClass();

        // call the super addTriggers
        ClassSymbol superClassSym = getSuperSymbol(cDecl);
        if (superClassSym != null) {
            stmts.append(makeSuperCall(diagPos, superClassSym, defs.addTriggersName, true));
        }
        
        // JFXC-2822 - Triggers need to work from mixins.
        List<ClassSymbol> mixinClasses = immediateMixinNames(cDecl);
        for (ClassSymbol cSym : mixinClasses) {
            stmts.append(makeSuperCall(diagPos, cSym, defs.addTriggersName, true));
        }

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
                make.Modifiers(Flags.PUBLIC | Flags.STATIC),
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
            if (ai.needsCloning() && !ai.hasProxyVar()) {
                final DiagnosticPosition diagPos = ai.pos();
                final VarSymbol sym = ai.getSymbol();
                final boolean requiresLocation = requiresLocation(ai);
                final long modFlags = Flags.PUBLIC | (requiresLocation? Flags.FINAL : 0L) | (ai.getFlags() & Flags.STATIC);

                JCModifiers mods = make.Modifiers(modFlags);
                if (sym.owner == csym) {
                    List<JCAnnotation> annotations = List.<JCAnnotation>of(make.Annotation(
                            makeIdentifier(diagPos, JavafxSymtab.sourceNameAnnotationClassNameString),
                            List.<JCExpression>of(make.Literal(sym.name.toString()))));
                    mods = addAccessAnnotationModifiers(diagPos, sym.flags(), mods, annotations);
                } else {
                    mods = addInheritedAnnotationModifiers(diagPos, sym.flags(), mods);
                }

                Type varType;
                JCExpression varInit;
                VarMorphInfo vmi = ai.getVMI();
                if (requiresLocation) {
                    varType = ai.getVariableType();
                    varInit = makeLocationAttributeVariable(vmi, diagPos);
                } else {
                    varType = ai.getRealType();
                    varInit = makeDefaultValue(diagPos, vmi);
                }
                optStat.recordClassVar(sym, requiresLocation);
                JCVariableDecl var = make.at(diagPos).VarDef(
                        mods,
                        attributeFieldName(sym),
                        makeTypeTree(diagPos, varType),
                        varInit);
                fields.append(var);
                optStat.recordConcreteField();
            }
        }
        return fields.toList();
    }

    private Name attributeNeedsDefaultFieldName(VarSymbol vsym) {
        Name aName = attributeFieldName(vsym);
        String nameStr = aName.toString() + needsDefaultSuffix;
        return names.fromString(nameStr);
    }

    // build a needs-default boolean field for each non-Location non-def instance member var (including inherited).
    private List<JCTree> makeNeedsDefaultFields(List<? extends VarInfo> attrInfos) {
        ListBuffer<JCTree> members = ListBuffer.lb();
        for (VarInfo ai : attrInfos) {
            if (ai.needsCloning() && !ai.hasProxyVar() && !ai.isStatic() && !ai.isDef() && !requiresLocation(ai)) {
                final DiagnosticPosition diagPos = ai.pos();
                final VarSymbol vsym = ai.getSymbol();

                // Make field
                 JCVariableDecl var = make.at(diagPos).VarDef(
                        make.Modifiers(Flags.PUBLIC),
                        attributeNeedsDefaultFieldName(vsym),
                        makeTypeTree(diagPos, syms.booleanType),
                        make.at(diagPos).Literal(TypeTags.BOOLEAN, 1));
                members.append(var);
                optStat.recordConcreteField();
            }
        }
        return members.toList();
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

        List<JCExpression> emptyTypeArgs = List.nil();
        ListBuffer<JCStatement> setUpStmts = ListBuffer.lb();
        List<JCVariableDecl> onChangeArgs;
        Name addListenerName;
        Type valueType;
        
        if (types.isSequence(info.getRealType())) {
            addListenerName = addSequenceChangeListenerName;
            valueType = info.getElementType();
            Type seqValType = types.sequenceType(valueType, false);
            onChangeArgs = List.of(
                    makeParam(diagPos, syms.intType, onReplace.getFirstIndex(), defs.onReplaceArgNameFirstIndex),
                    makeParam(diagPos, syms.intType, onReplace.getLastIndex(), defs.onReplaceArgNameLastIndex),
                    makeParam(diagPos, info.getRealType(), onReplace.getNewElements(), defs.onReplaceArgNameNewElements),
                    makeParam(diagPos, seqValType, onReplace.getOldValue(), defs.onReplaceArgNameOld),
                    makeParam(diagPos, seqValType, null, defs.onReplaceArgNameNew));
        } else {
            addListenerName = addChangeListenerName;
            valueType = info.getRealType();
            onChangeArgs = List.of(
                    makeParam(diagPos, valueType, onReplace.getOldValue(), defs.onReplaceArgNameOld),
                    makeParam(diagPos, valueType, onReplace.getNewElements(), defs.onReplaceArgNameNew));
        }

        JCTree clMethod = makeChangeListenerMethod(diagPos, onReplace, info.onReplaceTranslatedBody(), setUpStmts, onChangeArgs, TypeTags.VOID);
        JCExpression changeListener = make.at(diagPos).TypeApply(
                makeIdentifier(diagPos, changeListenerInterfaceName),
                List.of(makeTypeTree(diagPos, types.boxedTypeOrType(valueType))));
        JCNewClass anonymousChangeListener = make.NewClass(
                null, 
                emptyTypeArgs,
                changeListener, 
                List.<JCExpression>nil(), 
                make.at(diagPos).AnonymousClassDef(make.Modifiers(0L), List.of(clMethod)
                ));

        JCExpression varRef;
        if (info.getSymbol().owner.kind == Kinds.TYP) {
            // on replace is on class variable
            varRef = makeAttributeAccess(diagPos, info.getSymbol(),
                    info.getSymbol().isStatic()? null : defs.receiverName);
        } else {
            // on replace is on local variable
            varRef = make.at(diagPos).Ident(info.getName());
        }
        JCFieldAccess tmpSelect = make.at(diagPos).Select(varRef, addListenerName);

        List<JCExpression> args = List.<JCExpression>of(anonymousChangeListener);
        return make.at(diagPos).Exec(make.at(diagPos).Apply(emptyTypeArgs, tmpSelect, args));
    }
    
    private JCVariableDecl makeParam(DiagnosticPosition diagPos, Type type, JFXVar var, Name nameDefault) {
        Name name;
        if (var != null) {
            name = var.getName();
            diagPos = var.pos();
        } else {
            name = nameDefault;
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
                names.fromString("onChange"),
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
    private JCBlock makeDispatchBody(JFXClassDeclaration cDecl, MethodSymbol mth, boolean isBound, boolean isStatic) {
        ListBuffer<JCExpression> args = ListBuffer.lb();
        if (!isStatic) {
            // Add the this argument, so the static implementation method is invoked
            args.append(make.TypeCast(make.Ident(interfaceName(cDecl)), make.Ident(names._this)));
        }
        for (VarSymbol var : mth.params) {
            args.append(make.Ident(var.name));
        }
        JCExpression receiver = mth.owner == cDecl.sym ? null : makeTypeTree(cDecl.pos(), mth.owner.type, false);
        JCExpression expr = callExpression(cDecl.pos(), receiver, functionName(mth, !isStatic, isBound), args);
        JCStatement statement = (mth.getReturnType() == syms.voidType) ? make.Exec(expr) : make.Return(expr);
        return make.at(cDecl.pos()).Block(0L, List.<JCStatement>of(statement));
    }

    private boolean requiresLocation(VarInfo ai) {
        return typeMorpher.requiresLocation(ai.getSymbol());
    }

    protected String getSyntheticPrefix() {
        return "ifx$";
    }
}
