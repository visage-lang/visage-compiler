/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file tree accompanied this code.
 *
 * This code is distributed in the hope tree it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file tree
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
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javafx.code.JavafxBindStatus;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.*;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Field
 */
public class JavafxTypeMorpher extends JavafxTreeTranslator {
    protected static final Context.Key<JavafxTypeMorpher> typeMorpherKey =
            new Context.Key<JavafxTypeMorpher>();
    
    private final Name.Table names;
    private final JavafxResolve rs;
    private final ClassReader reader;
    private final JavafxTreeMaker make;
    private final Symtab syms;
    private final Log log;
    private final JavafxToJava toJava;
    
    public static final String locationPackageName = "com.sun.javafx.runtime.location.";
    
    public LocationNameSymType[] varLocation;
    public LocationNameSymType[] exprLocation;
    public LocationNameSymType[] declLocation;
    public LocationNameSymType[] bindingExpression;
    public LocationNameSymType   baseLocation;
    
    private final Type[] realTypeByKind;
    private final Object[] defaultValueByKind;
    
    private final Name getMethodName;
    private final Name setMethodName;
    private final Name makeMethodName;
    private final Name makeLazyMethodName;
    
    private JavafxBindStatus bindContext = JavafxBindStatus.UNBOUND;
    private boolean inLHS = false;
    
    private JavafxEnv<JavafxAttrContext> attrEnv;
    
    public class LocationNameSymType {
        public final Name name;
        public final ClassSymbol sym;
        public final Type type;
        LocationNameSymType(String which) {
            name = Name.fromString(names, locationPackageName + which);
            sym = reader.enterClass(name);
            type = sym.type;      
        }
    }
    
    private Map<VarSymbol, VarMorphInfo> vmiMap = new HashMap<VarSymbol, VarMorphInfo>();
    
    static class VarInfo {
        JCIdent origIdent;
        JCExpression replacement;
        VarInfo(JCIdent origIdent) {
            this.origIdent = origIdent;
        }
    }
    
    class VarMorphInfo {
        VarSymbol varSymbol;
        Type realType;
        Type varType;
        Type expressionType;
        Type bindingExpressionType;
        int typeKind;
        boolean shouldMorph = false;
        boolean haveDeterminedMorphability = false;
        boolean isBoundTo = false;
        boolean isAssignedTo = false;
        
        VarMorphInfo(VarSymbol varSymbol) {
            this.varSymbol = varSymbol;
        }
        
        private boolean isAttribute() {
            Symbol owner = varSymbol.owner;
            if (owner.kind == Kinds.MTH) {
                return false; // local var
            } else {
                if (varSymbol instanceof JavafxVarSymbol) {
                    return true; // we made it, soassume it is from a JavaFX class
                } else {
                    return false;
                }
            }
        }

        private boolean shouldMorph() {
            if (!haveDeterminedMorphability()) {
                realType = varSymbol.type;
                TypeSymbol realTsym = realType.tsym;
                Type locationType = null;
                if (realTsym == declLocation[TYPE_KIND_OBJECT].sym) {
                    locationType = ((ClassType) realType).typarams_field.head;
                    typeKind = TYPE_KIND_OBJECT;
                } else if (realTsym == declLocation[TYPE_KIND_BOOLEAN].sym) {
                    locationType = Symtab.booleanType;
                    typeKind = TYPE_KIND_BOOLEAN;
                } else if (realTsym == declLocation[TYPE_KIND_DOUBLE].sym) {
                    locationType = Symtab.doubleType;
                    typeKind = TYPE_KIND_DOUBLE;
                } else if (realTsym == declLocation[TYPE_KIND_INT].sym) {
                    locationType = Symtab.intType;
                    typeKind = TYPE_KIND_INT;
                }
                if (locationType != null) {
                    // External module with a Location type
                    setUsedType(realType);
                    realType = locationType;
                    markShouldMorph();
                } else if (isBoundTo() || isAttribute()) {
                    if (realType.isPrimitive()) {
                        if (realTsym == Symtab.doubleType.tsym) {
                            typeKind = TYPE_KIND_DOUBLE;
                        } else if (realTsym == Symtab.intType.tsym) {
                            typeKind = TYPE_KIND_INT;
                        } else if (realTsym == Symtab.booleanType.tsym) {
                            typeKind = TYPE_KIND_BOOLEAN;
                        } else {
                            assert false : "should not reach here";
                            typeKind = TYPE_KIND_OBJECT;
                        }
                    } else {
                        typeKind = TYPE_KIND_OBJECT;
                    }
                    if (realType.constValue() != null) {
                        realType = realType.baseType();
                    }
                    // must be called AFTER typeKind and realType are set in vsym
                    setUsedType(generifyIfNeeded(declLocationType(typeKind), this));
                    setVarType(generifyIfNeeded(varLocationType(typeKind), this));
                    setExpressionType(generifyIfNeeded(expressionLocationType(typeKind), this));
                    setBindingExpressionType(generifyIfNeeded(bindingExpressionType(typeKind), this));
                    markShouldMorph();
                }
                markDeterminedMorphability();
            }
            return shouldMorph;
        }

        private void markShouldMorph() { shouldMorph = true; }
        private void markDeterminedMorphability() { haveDeterminedMorphability = true; }
        private boolean haveDeterminedMorphability() { return haveDeterminedMorphability; }
        public Type getRealType() { return realType; }
        public Type getUsedType() { return varSymbol.type; }
        private void setUsedType(Type usedType) { varSymbol.type = usedType; }
        public Type getVarType() { return varType; }
        private void setVarType(Type t) { varType = t; }
        public Type getExpressionType() { return expressionType; }
        private void setExpressionType(Type t) { expressionType = t; }
        public Type getBindingExpressionType() { return bindingExpressionType; }
        private void setBindingExpressionType(Type t) { bindingExpressionType = t; }
        public Object getDefaultValue() { return defaultValueByKind[typeKind]; }
    
        public boolean isBoundTo() { return isBoundTo; }
        public boolean isAssignedTo() { return isAssignedTo; }
        public void markBoundTo() { this.isBoundTo = true; }
        public void markAssignedTo() { this.isAssignedTo = true; }

        public int getTypeKind() { return typeKind; }
    }
    
    VarMorphInfo varMorphInfo(VarSymbol sym) {
        VarMorphInfo vmi = vmiMap.get(sym);
        if (vmi == null) {
            vmi = new VarMorphInfo(sym);
            vmiMap.put(sym, vmi);
        }
        return vmi;
    }
    
    public static JavafxTypeMorpher instance(Context context) {
        JavafxTypeMorpher instance = context.get(typeMorpherKey);
        if (instance == null)
            instance = new JavafxTypeMorpher(context);
        return instance;
    }
    
    protected JavafxTypeMorpher(Context context) {
        context.put(typeMorpherKey, this);
        
        syms = Symtab.instance(context);
        names = Name.Table.instance(context);
        rs = JavafxResolve.instance(context);
        reader = ClassReader.instance(context);
        make = (JavafxTreeMaker)JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        toJava = JavafxToJava.instance(context);

        String[] locClass = new String[TYPE_KIND_COUNT];
        locClass[TYPE_KIND_OBJECT] = "Object";
        locClass[TYPE_KIND_DOUBLE] = "Double";
        locClass[TYPE_KIND_BOOLEAN] = "Boolean";
        locClass[TYPE_KIND_INT] = "Int";

        varLocation = new LocationNameSymType[TYPE_KIND_COUNT];
        exprLocation = new LocationNameSymType[TYPE_KIND_COUNT];
        declLocation = new LocationNameSymType[TYPE_KIND_COUNT];
        bindingExpression = new LocationNameSymType[TYPE_KIND_COUNT];

        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            varLocation[kind]  = new LocationNameSymType(locClass[kind] + "Var");
            exprLocation[kind] = new LocationNameSymType(locClass[kind] + "Expression");
            declLocation[kind] = new LocationNameSymType(locClass[kind] + "Location");
            bindingExpression[kind] = new LocationNameSymType(locClass[kind] + "BindingExpression");
        }
        
        baseLocation = new LocationNameSymType("Location");
                
        realTypeByKind = new Type[TYPE_KIND_COUNT];
        realTypeByKind[TYPE_KIND_OBJECT] = syms.objectType;
        realTypeByKind[TYPE_KIND_DOUBLE] = Symtab.doubleType;
        realTypeByKind[TYPE_KIND_BOOLEAN] = Symtab.booleanType;
        realTypeByKind[TYPE_KIND_INT] = Symtab.intType;
        
        defaultValueByKind = new Object[TYPE_KIND_COUNT];
        defaultValueByKind[TYPE_KIND_OBJECT] = null;
        defaultValueByKind[TYPE_KIND_DOUBLE] = new Double(0.0);
        defaultValueByKind[TYPE_KIND_BOOLEAN] = new Boolean(false);
        defaultValueByKind[TYPE_KIND_INT] = new Integer(0);
        
        getMethodName = Name.fromString(names, "get");
        setMethodName = Name.fromString(names, "set");
        makeMethodName = Name.fromString(names, "make");
        makeLazyMethodName = Name.fromString(names, "makeLazy");
    }
       
    public void morph(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;

        // Determine variable usage
        JavafxVarUsageAnalysis analyzer = new JavafxVarUsageAnalysis(this);
        analyzer.scan(attrEnv.tree);
        
        // Now translate the tree
        translate(attrEnv.tree);
    }
    
    private  <T extends JCTree> T boundTranslate(T tree, JavafxBindStatus bind) {
        JavafxBindStatus prevBindContext = bindContext;
        if (bindContext == JavafxBindStatus.UNBOUND) {
            bindContext = bind;
        } else { // TODO
            assert bind == JavafxBindStatus.UNBOUND || bind == bindContext : "how do we handle this?";
        }
        T result = translate(tree);
        bindContext = prevBindContext;
        return result;
    }
    
    private  <T extends JCTree> T translateLHS(T tree, boolean isImmediateLHS) {
        boolean wasInLHS = inLHS;
        inLHS = isImmediateLHS;
        T result = translate(tree);
        inLHS = wasInLHS;
        return result;
    }
    
    Type declLocationType(int typeKind) {
        return declLocation[typeKind].type;
    }
    
    Type varLocationType(int typeKind) {
        return varLocation[typeKind].type;
    }
    
    Type expressionLocationType(int typeKind) {
        return exprLocation[typeKind].type;
    }

    Type bindingExpressionType(int typeKind) {
        return bindingExpression[typeKind].type;
    }

    Type sharedLocationType(int typeKind, LocationNameSymType[] lnsta) {
        LocationNameSymType lnst = lnsta[typeKind];
        return lnst.type;
    }

    JCExpression declLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, declLocation);
    }
    
    JCExpression varLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, varLocation);
    }
    
    JCExpression expressionLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, exprLocation);
    }
    
    JCExpression bindingExpressionId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, bindingExpression);
    }
    
    JCExpression sharedLocationId(int pos, int typeKind, LocationNameSymType[] lnsta) {
        LocationNameSymType lnst = lnsta[typeKind];
        return make.at(pos).Identifier(lnst.name);
    }
    
    JCExpression sharedLocationId(DiagnosticPosition diagPos, int typeKind, LocationNameSymType[] lnsta) {
        LocationNameSymType lnst = lnsta[typeKind];
        return make.at(diagPos).Identifier(lnst.name);
    }
    
    JCExpression castToReal(Type realType, JCExpression expr) {
        // cast the expression so that boxing works

        JCExpression typeExpr = make.Type(realType);
        JCExpression result = make.TypeCast(typeExpr, expr);
        
        return result;
    }
    
    private Type generifyIfNeeded(Type aLocationType, VarMorphInfo vmi) {
        Type newType;
        if (vmi.getTypeKind() == TYPE_KIND_OBJECT) {
            List<Type> actuals = List.of(vmi.getRealType());
            List<Type> formals = aLocationType.tsym.type.getTypeArguments();
            //actuals.head = actuals.head.withTypeVar(formals.head);
            Type clazzOuter = declLocationType(vmi.getTypeKind()).getEnclosingType();
            newType = new ClassType(clazzOuter, actuals, aLocationType.tsym);
        } else {
            newType = aLocationType;
        }
        return newType;
    }
        
    JCExpression convertVariableReference(JCExpression varRef, VarMorphInfo vmi) {
        JCExpression expr;
        
        varRef.setType(vmi.getUsedType());
        if (bindContext.isBidiBind()) {
            // already in correct form-- leave it
            expr = varRef;
        } else if (inLHS) {
            // immediate left-hand side -- leave it
            expr = varRef;
        } else {
            // non-bind context -- want v1.get()
            JCFieldAccess getSelect = make.Select(varRef, getMethodName);
            /***
            getSelect.sym = rs.resolveInternalMethod(varRef.pos(),
                    attrEnv,
                    vmi.getUsedType(),
                    getMethodName,
                    List.<Type>nil(),    // argtypes
                    List.<Type>nil());   // typeargs
            getSelect.setType(getSelect.sym.type);
            ***/
            
            List<JCExpression> getArgs = List.nil();
            expr = make.Apply(null, getSelect, getArgs);
            //expr.setType(vmi.getRealType());
        }
        return expr;
    }
    
    private void pretty(JCTree tree) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        Pretty pretty = new Pretty(osw, false);
        try {
            pretty.println();
            pretty.print("+++++++++++++++++++++++++++++++++");
            pretty.println();
            pretty.printExpr(tree);
            pretty.println();
            pretty.print("---------------------------------");
            pretty.println();
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Pretty print got: " + ex);
        }
    }
    
    private JCExpression translateDefinitionalAssignment(JCExpression init, 
                                                        VarMorphInfo vmi, 
                                                        DiagnosticPosition diagPos,
                                                        JavafxBindStatus bindStatus) {
        JCExpression initExpr = boundTranslate(init, bindStatus);
        if (bindStatus.isUnidiBind()) {
            initExpr = buildExpression(vmi.varSymbol, initExpr, bindStatus);
        } else {
            if (initExpr.type.tsym != vmi.getUsedType().tsym) {
                initExpr = makeCall(vmi, diagPos, List.of(initExpr), varLocation, makeMethodName);
            }
        }
        return initExpr;
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        tree.defs = translate(tree.defs);
        result = tree;
    }
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
            
            //TODO: remove this
            if (tree.supertypes.length() > 1) {
                log.error(tree.pos, "compiler.err.javafx.not.yet.implemented",
                              "multiple inheritance");
            }

            visitClassDef(tree);
            
            result = tree;
    }
    
    @Override
    public void visitClassDef(JCClassDecl tree) {
        JCClassDecl prevEnclClass = attrEnv.enclClass;
        JavafxBindStatus prevBindContext = bindContext;
        boolean prevInLHS = inLHS;
        
        attrEnv.enclClass = tree;
        bindContext = JavafxBindStatus.UNBOUND;
        inLHS = false;
        
        tree.defs = translate(tree.defs);

        attrEnv.enclClass = prevEnclClass;
        bindContext = prevBindContext;
        inLHS = prevInLHS;
        
        result = tree;
    }
    
    @Override
    public void visitMethodDef(JCMethodDecl tree) {
        // At least for now, prevent parameters and return from being translated
        //tree.params = translateVarDefs(tree.params);
        //tree.restype = translate(tree.restype);
        tree.body = translate(tree.body);
        
        result = tree;
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        tree.onChange = translate(tree.onChange);
        visitVar(tree);
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();
        JavafxBindStatus bindStatus = tree.getBindStatus();
        VarSymbol vsym = tree.sym;
        VarMorphInfo vmi = varMorphInfo(vsym);
        if (vmi.shouldMorph()) {
            JCExpression init = tree.init != null? tree.init : makeLit(vmi.getRealType(), vmi.getDefaultValue());
            tree.init = translateDefinitionalAssignment(init, vmi, tree.pos(), bindStatus);
            tree.type = vmi.getUsedType();

            // local variables need to be final so they can be referenced
            // attributes cannot be final since they are initialized outside of the constructor
            if (!(tree instanceof JFXAttributeDefinition)) {
                tree.mods.flags |= Flags.FINAL;  
            }
        } else {
            tree.init = translate(tree.init);
        }
        result = tree;
    }
    
    @Override
    public void visitAssign(JCAssign tree) {
        JCExpression lhs = translateLHS(tree.lhs, true);
        Symbol sym = null;
        // TODO other cases
        if (lhs instanceof JCIdent) {
            JCIdent varid = (JCIdent)lhs;
            sym = varid.sym;
        } else if (lhs instanceof JCFieldAccess) {
            JCFieldAccess varaccess = (JCFieldAccess)lhs;
            sym = varaccess.sym;
        }
        VarSymbol vsym = null;
        if (sym != null && (sym instanceof VarSymbol)) {
            vsym = (VarSymbol)sym;
        }
        
        if (vsym != null) {
            VarMorphInfo vmi = varMorphInfo(vsym);
            if (vmi.shouldMorph()) {
                JCExpression rhs = translate(tree.rhs);
                JCFieldAccess setSelect = make.Select(lhs, setMethodName);
                List<JCExpression> setArgs = List.of(rhs);
                result = make.Apply(null, setSelect, setArgs);
            } else {
                tree.lhs = lhs;
                tree.rhs = translate(tree.rhs);
                result = tree;
            }
        }
    }
    
    @Override
    public void visitOperationDefinition(JFXOperationDefinition tree) {
        visitMethodDef(tree);
    }
    @Override
    public void visitFunctionDefinitionStatement(JFXFunctionDefinitionStatement tree) {
        visitOperationDefinition(tree.funcDef);
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart tree) {
        VarSymbol vsym = (VarSymbol) tree.sym;
        VarMorphInfo vmi = varMorphInfo(vsym);
        if (vmi.shouldMorph()) {
            tree.setTranslationInit(
                  translateDefinitionalAssignment(tree.expr, 
                    vmi, 
                    tree.pos(), 
                    tree.getBindStatus()));
        } else {
            tree.expr = translate(tree.expr);   
        }
        result = tree;
    }
    
    @Override
    public void visitSelect(JCFieldAccess tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        tree.selected = translateLHS(tree.selected, false);
        
        if (tree.sym instanceof VarSymbol) {
            VarSymbol vsym = (VarSymbol)tree.sym;
            
            VarMorphInfo vmi = varMorphInfo(vsym);
            if (vmi.shouldMorph()) {
                result = convertVariableReference(tree, vmi);
                return;
            }
        }
        result = tree;
    }
    
    /***
     * public void visitAssignop(JCAssignOp tree) {
     * visitTree(tree);
     * }
     *
     * public void visitTypeCast(JCTypeCast tree) {
     * visitTree(tree);
     * }
     *
     *
     * public void visitTypeTest(JCInstanceOf tree) {
     * visitTree(tree);
     * }
     ***/
    
    @Override
    public void visitIdent(JCIdent tree)   {
        if (tree.sym instanceof VarSymbol) {
            VarSymbol vsym = (VarSymbol)tree.sym;
            VarMorphInfo vmi = varMorphInfo(vsym);
            if (vmi.shouldMorph()) {
                result = convertVariableReference(tree, vmi);
                return;
            }
        }
        super.visitIdent(tree);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        tree.stats = translate(tree.stats);
        tree.value = translate(tree.value);
	result = tree;
    }
 
    @Override
    public void visitBlock(JCBlock tree) {
        List<JCStatement> stats = tree.stats;
        if (stats != null)  {
            List<JCStatement> prev = null;
            for (List<JCStatement> l = stats; l.nonEmpty(); l = l.tail) {
                // translate must occur immediately before prependInFrontOfStatement check
                JCStatement trans = translate(l.head);
                if (trans == null) {
                    // This statement has translated to nothing, remove it from the list
                    prev.tail = l.tail;
                    l = prev;
                    continue;
                }
                l.head = trans;
                prev = l;
            }
            tree.stats = stats;
        }
        result = tree;
    }
        
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        tree.body = translate(tree.body);
        
        result = tree;
    }

    //========================================================================================================================
    // Bound Expression support
    //========================================================================================================================
    
    /** Make an attributed tree representing a literal. This will be an
     *  Ident node in the case of boolean literals, a Literal node in all
     *  other cases.
     *  @param type       The literal's type.
     *  @param value      The literal's value.
     */
    JCExpression makeLit(Type type, Object value) {
        int tag = value==null? TypeTags.BOT : type.tag;
        return make.Literal(tag, value).setType(type.constType(value));
    }
    
    public JCExpression buildExpression(VarSymbol vsym, JCExpression tree, JavafxBindStatus bindStatus) {
        DiagnosticPosition diagPos = tree.pos();
        final HashMap<VarSymbol,VarInfo> varMap = new HashMap<VarSymbol,VarInfo>();
        
        TreeScanner ts = new TreeScanner() {
            @Override
            public void visitIdent(JCIdent tree)   {
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol vsym = (VarSymbol)tree.sym;
                    VarMorphInfo vmi = varMorphInfo(vsym);
                    if (vmi.shouldMorph()) {
                        tree.setType(vmi.getUsedType());
                        
                        varMap.put(vsym, new VarInfo(tree));
                    }
                }
            }
        };
        ts.scan(tree);
        
        VarMorphInfo vmi = varMorphInfo(vsym);
        int typeKind = vmi.getTypeKind();
        
        ListBuffer<JCExpression> argValues = ListBuffer.lb();
        //TODO: this should be either JavaFX ASTs of hidden in translation field
        JCBlock body = make.at(diagPos).Block(0, List.<JCStatement>of(make.at(diagPos).Return(tree)));
        JCMethodDecl getMethod = make.at(tree.pos).MethodDef(
                make.at(diagPos).Modifiers(Flags.PUBLIC), 
                getMethodName, 
                toJava.makeTypeTree(vmi.getRealType(), diagPos), 
                List.<JCTypeParameter>nil(), 
                List.<JCVariableDecl>nil(), 
                List.<JCExpression>nil(), 
                body, 
                null); 
        ;
        JCExpression newExpr = make.at(diagPos).NewClass(
                null,                       // enclosing
                List.<JCExpression>nil(),   // type args
                // class name
                toJava.makeTypeTree(vmi.getBindingExpressionType(), diagPos),
                List.<JCExpression>nil(),   // args
                make.at(diagPos).AnonymousClassDef(
                    make.at(diagPos).Modifiers(0), 
                    List.<JCTree>of(getMethod)));
        argValues.append(newExpr);
        for (VarInfo vi : varMap.values()) {
            argValues.append(vi.origIdent);
        }
        
        Name makeName = bindStatus.isLazy()? makeLazyMethodName : makeMethodName;
        return makeCall(vmi, diagPos, argValues.toList(), exprLocation, makeName);
    }

    private JCExpression makeCall(VarMorphInfo vmi, 
                                  DiagnosticPosition diagPos,
                                  List<JCExpression> makeArgs,
                                  LocationNameSymType[] lnsta,
                                  Name makeName) {
        JCExpression locationTypeExp = sharedLocationId(diagPos, vmi.getTypeKind(), lnsta);
        JCFieldAccess makeSelect = make.at(diagPos).Select(locationTypeExp, makeName);
        List<JCExpression> typeArgs = null;
        if (vmi.getTypeKind() == TYPE_KIND_OBJECT) {
            typeArgs = List.of(toJava.makeTypeTree(vmi.getRealType(), diagPos));
        }
        return make.at(diagPos).Apply(typeArgs, makeSelect, makeArgs);
    }
}
