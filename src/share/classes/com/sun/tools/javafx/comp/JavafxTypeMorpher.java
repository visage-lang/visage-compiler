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
import com.sun.tools.javac.code.Type.ArrayType;
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
    
    private static final String locationPackageName = "com.sun.javafx.runtime.";
    
    public LocationNameSymType[] varLocation;
    public LocationNameSymType[] exprLocation;
    public LocationNameSymType[] declLocation;
    public LocationNameSymType   baseLocation;
    public LocationNameSymType   contextLocation;
    
    private final Type[] realTypeByKind;
    
    private final Name applyMethodName;
    
    private final Name getMethodName;
    private final Name setMethodName;
    private final Name makeMethodName;
    
    private final Name[] valueAccessName;
    private final Name[] valueSetName;
    
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
    
    private static class PerClassInfo {

        JavafxBindStatus bindContext;
        boolean inLHS;
        ListBuffer<JCStatement> prependInFrontOfStatement = null;
        JCMethodDecl applyMethodDef; 
        int exprNumNext;
        ListBuffer<ExprInfo> exprInfo; 
 
        @SuppressWarnings("unchecked")
        PerClassInfo() {
            this.bindContext = JavafxBindStatus.UNBOUND;
            this.inLHS = false;
            this.prependInFrontOfStatement = null;
            this.applyMethodDef = null;
            this.exprNumNext = 0;
            this.exprInfo = null;
        }
        
        void setApplyMethod(JCMethodDecl meth) {
            assert applyMethodDef == null;
            applyMethodDef = meth;
        }
    }
    
    private Map<VarSymbol, VarInfo> varMap;
    
    private Map<VarSymbol, VarMorphInfo> vmiMap = new HashMap<VarSymbol, VarMorphInfo>();
    
    private PerClassInfo classInfo;
    
    private static class ExprInfo {
        final int exprNum;
        final JCExpression expr;
        final Map<VarSymbol,VarInfo> varMap;
        final VarSymbol vsym;
        
        JCStatement updateStatement;  // ????
        
        ExprInfo(
                Map<VarSymbol,VarInfo> varMap,
                int exprNum,
                VarSymbol vsym,
                JCExpression expr) {
            this.varMap = varMap;
            this.exprNum = exprNum;
            this.vsym = vsym;
            this.expr = expr;
        }
    }
    
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
        locClass[TYPE_KIND_INT] = "Integer";

        varLocation = new LocationNameSymType[TYPE_KIND_COUNT];
        exprLocation = new LocationNameSymType[TYPE_KIND_COUNT];
        declLocation = new LocationNameSymType[TYPE_KIND_COUNT];

        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            varLocation[kind]  = new LocationNameSymType(locClass[kind] + "Location");
            exprLocation[kind] = new LocationNameSymType("ExpressionN" + locClass[kind] + "Location");
            declLocation[kind] = new LocationNameSymType(locClass[kind] + "Location");
        }
        
        baseLocation = new LocationNameSymType("Location");
        contextLocation = new LocationNameSymType("Context");

        applyMethodName = Name.fromString(names, "applyN");
                
        realTypeByKind = new Type[TYPE_KIND_COUNT];
        realTypeByKind[TYPE_KIND_OBJECT] = syms.objectType;
        realTypeByKind[TYPE_KIND_DOUBLE] = Symtab.doubleType;
        realTypeByKind[TYPE_KIND_BOOLEAN] = Symtab.booleanType;
        realTypeByKind[TYPE_KIND_INT] = Symtab.intType;
        
        getMethodName = Name.fromString(names, "get");
        setMethodName = Name.fromString(names, "set");
        makeMethodName = Name.fromString(names, "make");
        
        valueAccessName = new Name[TYPE_KIND_COUNT];
        valueAccessName[TYPE_KIND_OBJECT]  = Name.fromString(names, "asObject");
        valueAccessName[TYPE_KIND_DOUBLE]  = Name.fromString(names, "asDouble");
        valueAccessName[TYPE_KIND_BOOLEAN] = Name.fromString(names, "asBoolean");
        valueAccessName[TYPE_KIND_INT]     = Name.fromString(names, "asInt");
        
        valueSetName = new Name[TYPE_KIND_COUNT];
        valueSetName[TYPE_KIND_OBJECT]  = Name.fromString(names, "setObject");
        valueSetName[TYPE_KIND_DOUBLE]  = Name.fromString(names, "setDouble");
        valueSetName[TYPE_KIND_BOOLEAN] = Name.fromString(names, "setBoolean");
        valueSetName[TYPE_KIND_INT]     = Name.fromString(names, "setInt");
        
        classInfo = null;
    }
   
    private TreeScanner unconstifyHack() {
         return new TreeScanner() {

            private void deconstant(JCTree tree) {
                if (tree.type.constValue() != null) {
                    tree.type = tree.type.baseType();
                }
            }

            @Override
            public void visitApply(JCMethodInvocation tree) {
                super.visitApply(tree);
                deconstant(tree);
            }

            @Override
            public void visitParens(JCParens tree) {
                super.visitParens(tree);
                deconstant(tree);
            }

            @Override
            public void visitAssign(JCAssign tree) {
                super.visitAssign(tree);
                deconstant(tree);
            }

            @Override
            public void visitAssignop(JCAssignOp tree) {
                super.visitAssignop(tree);
                deconstant(tree);
            }

            @Override
            public void visitUnary(JCUnary tree) {
                super.visitUnary(tree);
                deconstant(tree);
            }

            @Override
            public void visitBinary(JCBinary tree) {
                super.visitBinary(tree);
                deconstant(tree);
            }

            @Override
            public void visitTypeCast(JCTypeCast tree) {
                super.visitTypeCast(tree);
                deconstant(tree);
            }

            @Override
            public void visitTypeTest(JCInstanceOf tree) {
                super.visitTypeTest(tree);
                deconstant(tree);
            }

            @Override
            public void visitIndexed(JCArrayAccess tree) {
                super.visitIndexed(tree);
                deconstant(tree);
            }

            @Override
            public void visitSelect(JCFieldAccess tree) {
                super.visitSelect(tree);
                deconstant(tree);
            }

            @Override
            public void visitConditional(JCConditional tree) {
                super.visitConditional(tree);
                deconstant(tree);
            }

            @Override
            public void visitIdent(JCIdent tree) {
                super.visitIdent(tree);
                deconstant(tree);
                if (tree.sym.type.constValue() != null) {
                    tree.sym.type = tree.sym.type.baseType();
                }
            }

            @Override
            public void visitVarDef(JCVariableDecl tree) {
                super.visitVarDef(tree);
                deconstant(tree);
                if (tree.sym.type.constValue() != null) {
                    tree.sym.type = tree.sym.type.baseType();
                }
                if (tree instanceof JavafxJCVarDecl) {
                    JCTree listener = ((JavafxJCVarDecl) tree).getChangeListener();
                    if (listener != null) {
                        listener.accept(this);
                    }
                }
            }
        };
    }

    
    public void morph(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;

        unconstifyHack().scan(attrEnv.tree);   //TODO: shouldn't need this

        // Determine variable usage
        JavafxVarUsageAnalysis analyzer = new JavafxVarUsageAnalysis(this);
        analyzer.scan(attrEnv.tree);
        
        // Now translate the tree
        translate(attrEnv.tree);
        
        /***
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        Pretty pretty = new Pretty(osw, false);
        try {
            pretty.printExpr(attrEnv.tree);
            osw.flush();
        }catch(Exception ex) {
            System.err.println("Type Morpher Pretty print got: " + ex);
        }
        ***/
        
    }
    
    private  <T extends JCTree> T boundTranslate(T tree, JavafxBindStatus bind) {
        JavafxBindStatus prevBindContext = classInfo.bindContext;
        if (classInfo.bindContext == JavafxBindStatus.UNBOUND) {
            classInfo.bindContext = bind;
        } else { // TODO
            assert bind == JavafxBindStatus.UNBOUND || bind == classInfo.bindContext : "how do we handle this?";
        }
        T result = translate(tree);
        classInfo.bindContext = prevBindContext;
        return result;
    }
    
    private  <T extends JCTree> T translateLHS(T tree, boolean isImmediateLHS) {
        boolean wasInLHS = classInfo.inLHS;
        classInfo.inLHS = isImmediateLHS;
        T result = translate(tree);
        classInfo.inLHS = wasInLHS;
        return result;
    }
    
    Type declLocationType(int typeKind) {
        return declLocation[typeKind].type;
    }
    
    JCExpression declLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, varLocation);
    }
    
    JCExpression expressionLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, exprLocation);
    }
    
    JCExpression sharedLocationId(int pos, int typeKind, LocationNameSymType[] lnsta) {
        LocationNameSymType lnst = lnsta[typeKind];
        return make.at(pos).Identifier(lnst.name);
    }
    
    JCExpression castToReal(Type realType, JCExpression expr) {
        // cast the expression so that boxing works
        /****
        ClassSymbol realTypeSymbol = (ClassSymbol)(realType.tsym);
        Name realTypeName = realTypeSymbol.flatName();
        
        JCIdent realIdent = make.Ident(realTypeName);
        realIdent.sym = realType.tsym;
        realIdent.setType(realType);
        
        JCExpression result = make.TypeCast(realIdent, expr);
        result.setType(realType);
         * ****/
        
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
        if (classInfo.bindContext.isBidiBind()) {
            // already in correct form-- leave it
            expr = varRef;
        } else if (classInfo.inLHS) {
            // immediate left-hand side -- leave it
            expr = varRef;
        } else {
            // non-bind context -- want v1.get()
            JCFieldAccess getSelect = make.Select(varRef, getMethodName);
            getSelect.sym = rs.resolveInternalMethod(varRef.pos(),
                    attrEnv,
                    vmi.getUsedType(),
                    getMethodName,
                    List.<Type>nil(),    // argtypes
                    List.<Type>nil());   // typeargs
            getSelect.setType(getSelect.sym.type);
            
            List<JCExpression> getArgs = List.nil();
            expr = make.Apply(null, getSelect, getArgs);
            expr.setType(vmi.getRealType());
        }
        return expr;
    }
    
    private JCMethodDecl makeEmptyContextMethod() {
        List<JCVariableDecl> params;
        params = List.of(            
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("bound"), make.Identifier(baseLocation.name), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("exprNum"), make.TypeIdent(TypeTags.INT), null),
                make.VarDef(make.Modifiers(Flags.PARAMETER), names.fromString("args"), make.TypeArray(make.Identifier(baseLocation.name)), null)
                );
        return make.MethodDef(
                make.Modifiers(Flags.PUBLIC),
                names.fromString("applyN"),
                make.TypeIdent(TypeTags.BOOLEAN),
                List.<JCTypeParameter>nil(),
                params,
                List.<JCExpression>nil(),
                make.Block(0, List.<JCStatement>of(make.Return(make.Literal(TypeTags.BOOLEAN, 0)))),
                null);
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
    
    private JCExpression makeUsedTypeExpression(VarMorphInfo vmi, int pos) {
        Type usedType = vmi.getUsedType();
        return toJava.makeTypeTree(usedType);
    }
    
    private JCExpression translateDefinitionalAssignment(JCExpression init, 
                                                        VarMorphInfo vmi, 
                                                        DiagnosticPosition diagPos,
                                                        JCExpression locationTypeExp, 
                                                        JavafxBindStatus bindStatus) {
        JCExpression initExpr;
        if (bindStatus.isUnidiBind()) {
            initExpr = registerExpression(vmi.varSymbol, init);
        } else {
            initExpr = boundTranslate(init, bindStatus);

            if (initExpr.type.tsym != vmi.getUsedType().tsym) {
                JCFieldAccess makeSelect = make.at(diagPos).Select(locationTypeExp, makeMethodName);
                List<JCExpression> makeArgs = List.of(initExpr);
                JCExpression makeApply = make.at(diagPos).Apply(null, makeSelect, makeArgs);
                initExpr = makeApply;
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
            // TODO: Need resolved types so I can verify tree one Java class is extended only... Move the rest to interfaces...
            List<JCExpression> interfaces = List.of(make.Identifier(contextLocation.name));
            tree.implementing = interfaces.appendList(tree.implementing);
            
            //TODO: remove this
            if (tree.supertypes.length() > 1) {
                log.error(tree.pos, "compiler.err.javafx.not.yet.implemented",
                              "multiple inheritance");
            }
            tree.defs = tree.defs.append(makeEmptyContextMethod());        
            
            visitClassDef(tree);
            
            result = tree;
    }
    
    @Override
    public void visitClassDef(JCClassDecl tree) {
        JCClassDecl prevEnclClass = attrEnv.enclClass;
        PerClassInfo prevClassInfo = classInfo;
        attrEnv.enclClass = tree;
        classInfo = new PerClassInfo();
        
        resetExpressionMapper();
         
        tree.defs = translate(tree.defs);

        if (classInfo.applyMethodDef != null) {
            fillInApplyMethod();
        }
        
        attrEnv.enclClass = prevEnclClass;
        classInfo = prevClassInfo;
        
        result = tree;
    }
    
    @Override
    public void visitMethodDef(JCMethodDecl tree) {
        // At least for now, prevent parameters and return from being translated
        //tree.params = translateVarDefs(tree.params);
        //tree.restype = translate(tree.restype);
        tree.body = translate(tree.body);
        
        int argGroup = -1;
        if (tree.name == applyMethodName) {
            classInfo.setApplyMethod(tree);
        }
        
        result = tree;
    }
    
    @Override
    public void visitAttributeDefinition(JFXAttributeDefinition tree) {
        tree.onChange = translate(tree.onChange);
        visitVar(tree);
    }
    
    @Override
    public void visitVar(JFXVar tree) {
        JavafxBindStatus bindStatus = tree.getBindStatus();
        boolean isBound = bindStatus.isBound();
        VarSymbol vsym = tree.sym;
        VarMorphInfo vmi = varMorphInfo(vsym);
        if (vmi.shouldMorph()) {
            JCExpression typeExp = makeUsedTypeExpression(vmi, tree.pos);

            if (tree.init != null) {
                tree.init = translateDefinitionalAssignment(tree.init, vmi, tree.pos(), typeExp, bindStatus);
            } else {
                // no initializing expression, do default
                JCFieldAccess makeSelect = make.at(tree.pos).Select(typeExp, makeMethodName);

                List<JCExpression> makeArgs = List.nil();
                JCExpression makeApply = make.at(tree.pos).Apply(null, makeSelect, makeArgs);

                tree.init = makeApply;
            }

            tree.type = vmi.getUsedType();

            if (isBound) {
//                tree.mods.flags |= Flags.FINAL;   // can't do this, because of initialization
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
            JCExpression typeExp = makeUsedTypeExpression(vmi, tree.pos);
            tree.setTranslationInit(
                  translateDefinitionalAssignment(tree.expr, 
                    vmi, 
                    tree.pos(), 
                    typeExp, 
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
                if (classInfo.bindContext.isUnidiBind()) {
                    Type realType = vmi.getRealType();
                    JCExpression locAccess = varMap.get(vsym).replacement;
                    
                    Name methName = valueAccessName[vmi.getTypeKind()];
                    JCFieldAccess getSelect = make.Select(locAccess, methName);
                    getSelect.sym = rs.resolveInternalMethod(locAccess.pos(),
                            attrEnv,
                            baseLocation.type,
                            methName,
                            List.<Type>nil(),    // argtypes
                            List.<Type>nil());   // typeargs
                    getSelect.setType(getSelect.sym.type);
                    
                    List<JCExpression> getArgs = List.nil();
                    JCExpression expr = make.Apply(null, getSelect, getArgs);
                    expr.setType(realType);
                    
                    if (vmi.getTypeKind() == TYPE_KIND_OBJECT) {
                        expr = castToReal(realType, expr);
                    }
                    
                    result = expr;
                    return;
                } else {
                    result = convertVariableReference(tree, vmi);
                    return;
                }
            }
        }
        super.visitIdent(tree);
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        tree.stats = translate(tree.stats);
        tree.value = translate(tree.value);
	result = tree;
    }
 
    /**
     * Allow prepending of statements and/or deletin by translation to null
     */
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
                if (classInfo.prependInFrontOfStatement != null) {
                    List<JCStatement> pl = classInfo.prependInFrontOfStatement.toList();
                    List<JCStatement> last = classInfo.prependInFrontOfStatement.last;
                    // attach remainder of list to the prepended statements
                    for (List<JCStatement> al = pl; ; al = al.tail) {
                        if (al.tail == last) {
                            al.tail = l;
                            break;
                        }
                    }
                    // attach prepended statement to previous part of list
                    if (prev == null) {
                        stats = pl;
                    } else {
                        prev.tail = pl;
                    }
                    classInfo.prependInFrontOfStatement = null;
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
    
    void resetExpressionMapper() {
        classInfo.exprNumNext= 0;
        classInfo.exprInfo = ListBuffer.<ExprInfo>lb();
    }
    
    /** Make an attributed tree representing a literal. This will be an
     *  Ident node in the case of boolean literals, a Literal node in all
     *  other cases.
     *  @param type       The literal's type.
     *  @param value      The literal's value.
     */
    JCExpression makeLit(Type type, Object value) {
        return make.Literal(type.tag, value).setType(type.constType(value));
    }
    
    void fillInApplyMethod() {
        ListBuffer<ExprInfo> infos = classInfo.exprInfo;
        if (infos.length() >= 0) {
            JCMethodDecl meth = classInfo.applyMethodDef;
            List<JCVariableDecl> params = meth.getParameters();
            JCVariableDecl numVar = params.tail.head;
            ListBuffer<JCCase> cases = ListBuffer.lb();
            ListBuffer<JCStatement> stmts;

            for (ExprInfo info : classInfo.exprInfo) {
                stmts = ListBuffer.<JCStatement>lb();
                stmts.append(buildExpression(info, meth));
                stmts.append(make.Return(makeLit(Symtab.booleanType, 1)));
                cases.append(make.Case(makeLit(Symtab.intType, info.exprNum), stmts.toList()));
            }

            JCExpression selector = make.Ident(numVar.name);
            meth.body.stats = meth.body.stats.prepend( make.Switch(selector, cases.toList()) );
        }
    }
    
    public JCExpression registerExpression(VarSymbol vsym, JCExpression tree) {
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
        
        int exprNum = classInfo.exprNumNext++;
        ExprInfo info = new ExprInfo(varMap, exprNum, vsym, tree);
        classInfo.exprInfo.append(info);
        
        VarMorphInfo vmi = varMorphInfo(vsym);
        Type realType = vmi.getRealType();
        Type usedType = vmi.getUsedType();
        int typeKind = vmi.getTypeKind();
        
        JCExpression exprLocId = expressionLocationId(tree.pos, typeKind);
        if (typeKind == TYPE_KIND_OBJECT) {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            args.append(make.Ident(realType.tsym));
            Type exprLocType = exprLocId.type;
            exprLocId = make.TypeApply(exprLocId, args.toList());
//            exprLocId.setType(generifyIfNeeded(exprLocType, vmi));
        }
        
        ListBuffer<Type> argTypes = ListBuffer.lb();
        ListBuffer<JCExpression> argValues = ListBuffer.lb();
        argTypes.append(contextLocation.type);
//        JCIdent thisId = make.Ident(new VarSymbol(Flags.FINAL, names._this, attrEnv.enclClass.type, attrEnv.enclClass.type.tsym));
        JCIdent thisId = make.Ident(names._this);
        thisId.setType(thisId.type);
        argValues.append(thisId);
        argTypes.append(Symtab.intType);
        argValues.append(makeLit(Symtab.intType, exprNum));
        {
            Type argArrayType = new ArrayType(baseLocation.type, syms.arrayClass);
            argTypes.append(argArrayType);
            ListBuffer<JCExpression> argContents = ListBuffer.lb();
            for (VarInfo vi : varMap.values()) {
                argContents.append(vi.origIdent);
            }
            JCNewArray argArgValue = make.NewArray(
                    make.Ident(baseLocation.sym),
                    List.<JCExpression>nil(),
                    argContents.toList());
            argArgValue.setType(argArrayType);
            argValues.append(argArgValue);
        }
        
        JCFieldAccess makeSelect = make.Select(exprLocId, makeMethodName);        
        JCExpression makeApply = make.Apply(null, makeSelect, argValues.toList());
        return makeApply;
    }
    
    public JCStatement buildExpression(ExprInfo info, JCMethodDecl applyMethod) {
        VarSymbol vsym = info.vsym;
        JCExpression tree = info.expr;
        varMap = info.varMap;
        
        List<JCVariableDecl> params = applyMethod.getParameters();
        JCVariableDecl boundVar = params.head;
        List<JCVariableDecl> args = params.tail.tail;
        
        int i = 0;
        for (VarSymbol orig : varMap.keySet()) {
            JCExpression argsExp = make.at(tree.pos).Ident(args.head.name);
            argsExp = make.Indexed(argsExp, makeLit(Symtab.intType, i++));
            
            varMap.get(orig).replacement = argsExp;
        }
        
        JCExpression updateExpr = boundTranslate(tree, JavafxBindStatus.UNIDIBIND);
        
        JCExpression lhs = make.Ident(boundVar.name);
        VarMorphInfo vmi = varMorphInfo(vsym);
        Name valueSetMethod = valueSetName[vmi.getTypeKind()];
        JCFieldAccess setSelect = make.Select(lhs, valueSetMethod);
        
        List<JCExpression> setArgs = List.of(updateExpr);
        JCExpression updateApply = make.Apply(null, setSelect, setArgs);
        return make.Exec(updateApply);
    }
}
