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
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javafx.code.JavafxBindStatus;
import com.sun.tools.javafx.code.JavafxVarSymbol;
import com.sun.tools.javafx.tree.JFXBlockExpression;
import com.sun.tools.javafx.tree.JavafxJCAssign;
import com.sun.tools.javafx.tree.JavafxJCVarDecl;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Field
 */
public class JavafxTypeMorpher extends TreeTranslator {
    protected static final Context.Key<JavafxTypeMorpher> typeMorpherKey =
            new Context.Key<JavafxTypeMorpher>();
    
    private final Name.Table names;
    private final Resolve rs;
    private final ClassReader reader;
    private final TreeMaker make;
    private final Attr attr;
    private final Symtab syms;
    private final Types types;
    
    public static final int ARGS_IN_ARRAY = 0; // TODO: increase
    public static final int BIDI = ARGS_IN_ARRAY + 1;
    public static Name[][] locationName;
    public static ClassSymbol[][] locationSym;
    public static Type[][] locationType;
    
    private final Type[] realTypeByKind;
    
    private final Name contextName;
    private final ClassSymbol contextSym;
    private final Type contextType;
    
    private final Name baseLocationName;
    private final ClassSymbol baseLocationSym;
    private final Type baseLocationType;
    
    private final Name[] applyMethodNames;   // by group
    
    private final Name getMethodName;
    private final Name setMethodName;
    private final Name makeMethodName;
    
    private final Name[] valueAccessName;
    private final Name[] valueSetName;
    
    private Env<AttrContext> attrEnv;
    
    private static class PerClassInfo {

        JavafxBindStatus bindContext;
        boolean inLHS;
        ListBuffer<JCStatement> prependInFrontOfStatement = null;
        JCMethodDecl[] applyMethodDefs; // by group
        int[] exprNumNext;
        ListBuffer<ExprInfo>[] infoByGroup; // by group
 
        @SuppressWarnings("unchecked")
        PerClassInfo() {
            this.bindContext = JavafxBindStatus.UNBOUND;
            this.inLHS = false;
            this.prependInFrontOfStatement = null;
            this.applyMethodDefs = null;
            this.exprNumNext = new int[ARGS_IN_ARRAY + 1];
            this.infoByGroup = new ListBuffer[ARGS_IN_ARRAY + 1];
        }
        
        void setApplyMethod(JCMethodDecl meth, int i) {
            if (applyMethodDefs == null) {
                applyMethodDefs = new JCMethodDecl[ARGS_IN_ARRAY + 1 + 1];
            }
            assert applyMethodDefs[i] == null;
            applyMethodDefs[i] = meth;
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
        final int argsGroup;
        
        JCStatement updateStatement;  // ????
        
        ExprInfo(
                Map<VarSymbol,VarInfo> varMap,
                int exprNum,
                VarSymbol vsym,
                JCExpression expr,
                int argsGroup) {
            this.varMap = varMap;
            this.exprNum = exprNum;
            this.vsym = vsym;
            this.expr = expr;
            this.argsGroup = argsGroup;
        }
    }
    
    static class VarInfo {
        JCIdent origIdent;
        JCExpression replacement;
        VarInfo(JCIdent origIdent) {
            this.origIdent = origIdent;
        }
    }
    
    static class VarMorphInfo {
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

        private boolean shouldMorph() {
            if (!haveDeterminedMorphability()) {
                realType = varSymbol.type;
                TypeSymbol realTsym = realType.tsym;
                Type locationType = null;
                if (realTsym == locationSym[TYPE_KIND_OBJECT][BIDI]) {
                    locationType = ((ClassType) realType).typarams_field.head;
                    typeKind = TYPE_KIND_OBJECT;
                } else if (realTsym == locationSym[TYPE_KIND_BOOLEAN][BIDI]) {
                    locationType = Symtab.booleanType;
                    typeKind = TYPE_KIND_BOOLEAN;
                } else if (realTsym == locationSym[TYPE_KIND_DOUBLE][BIDI]) {
                    locationType = Symtab.doubleType;
                    typeKind = TYPE_KIND_DOUBLE;
                } else if (realTsym == locationSym[TYPE_KIND_INT][BIDI]) {
                    locationType = Symtab.intType;
                    typeKind = TYPE_KIND_INT;
                }
                if (locationType != null) {
                    // External module with a Location type
                    setUsedType(realType);
                    realType = locationType;
                    markShouldMorph();
                } else if (isBoundTo() || ((varSymbol instanceof JavafxVarSymbol) && 
                                        (((JavafxVarSymbol) varSymbol).isBound() || 
                                          varSymbol.owner.kind != Kinds.MTH))) {
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
                    setUsedType(generifyIfNeeded(biDiLocationType(typeKind), this));
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
        rs = Resolve.instance(context);
        types = Types.instance(context);
        reader = ClassReader.instance(context);
        attr = Attr.instance(context);
        make = TreeMaker.instance(context);
        
        locationName = new Name[TYPE_KIND_COUNT][BIDI + 1 + 1];
        locationSym = new ClassSymbol[TYPE_KIND_COUNT][BIDI + 1 + 1];
        locationType = new Type[TYPE_KIND_COUNT][BIDI + 1 + 1];
        
        String[] locClass = new String[TYPE_KIND_COUNT];
        locClass[TYPE_KIND_OBJECT] = "ObjectLocation";
        locClass[TYPE_KIND_DOUBLE] = "DoubleLocation";
        locClass[TYPE_KIND_BOOLEAN] = "BooleanLocation";
        locClass[TYPE_KIND_INT] = "IntegerLocation";
        String pkg = "com.sun.javafx.runtime.";
        
        for (int kind = 0; kind < TYPE_KIND_COUNT; ++kind) {
            for (int i = 0; i <= BIDI; ++i) {
                String className = pkg + (i==BIDI? "" : "Expression" + ((i==ARGS_IN_ARRAY)? "N" : i)) + locClass[kind];
                locationName[kind][i] = Name.fromString(names, className);
                locationSym[kind][i] = reader.enterClass(locationName[kind][i]);
                locationType[kind][i] = locationSym[kind][i].type;
            }
        }
        applyMethodNames = new Name[ARGS_IN_ARRAY + 1 + 1];
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            String applyName = "apply" + ((i==ARGS_IN_ARRAY)? "N" : i);
            applyMethodNames[i] = Name.fromString(names, applyName);
        }
        
        contextName = Name.fromString(names, pkg + "Context");
        contextSym = reader.enterClass(contextName);
        contextType = contextSym.type;
        
        baseLocationName = Name.fromString(names, pkg + "Location");
        baseLocationSym = reader.enterClass(baseLocationName);
        baseLocationType = baseLocationSym.type;
        
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

    
    public void morph(Env<AttrContext> attrEnv) {
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
    
    static Type biDiLocationType(int typeKind) {
        return locationType[typeKind][BIDI];
    }
    
    JCIdent biDiLocationId(int pos, int typeKind) {
        return sharedLocationId(pos, typeKind, BIDI);
    }
    
    JCIdent expressionLocationId(int pos, int typeKind, int argCount) {
        return sharedLocationId(pos, typeKind, argCount > ARGS_IN_ARRAY? ARGS_IN_ARRAY : argCount);
    }
    
    JCIdent sharedLocationId(int pos, int typeKind, int argCount) {
        JCIdent locationId = make.at(pos).Ident(locationName[typeKind][argCount]);
        locationId.sym = locationSym[typeKind][argCount];
        locationId.type = locationType[typeKind][argCount];
        return locationId;
    }
    
    JCExpression castToReal(Type realType, JCExpression expr) {
        // cast the expression so that boxing works
        ClassSymbol realTypeSymbol = (ClassSymbol)(realType.tsym);
        Name realTypeName = realTypeSymbol.flatName();
        
        JCIdent realIdent = make.Ident(realTypeName);
        realIdent.sym = realType.tsym;
        realIdent.setType(realType);
        
        JCExpression result = make.TypeCast(realIdent, expr);
        result.setType(realType);
        
        return result;
    }
    
    private static Type generifyIfNeeded(Type aLocationType, VarMorphInfo vmi) {
        Type newType;
        if (vmi.getTypeKind() == TYPE_KIND_OBJECT) {
            List<Type> actuals = List.of(vmi.getRealType());
            List<Type> formals = aLocationType.tsym.type.getTypeArguments();
            actuals.head = actuals.head.withTypeVar(formals.head);
            Type clazzOuter = biDiLocationType(vmi.getTypeKind()).getEnclosingType();
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
        Type realType = vmi.getRealType();
        TypeSymbol realTsym = realType.tsym;
        Type usedType = vmi.getUsedType();
        TypeSymbol usedTsym = usedType.tsym;
        JCExpression typeExp;

        if (realType.isPrimitive()) {
            typeExp = make.at(pos).Ident(usedTsym);
        } else {
            JCExpression objLoc = biDiLocationId(pos, TYPE_KIND_OBJECT);
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            args.append(make.Ident(realTsym));
            typeExp = make.at(pos).TypeApply(objLoc, args.toList());
        }
        typeExp.setType(usedType);
        return typeExp;
    }
    
    private JCExpression translateDefinitionalAssignment(JCExpression init, VarMorphInfo vmi, 
                                                        DiagnosticPosition diagPos,
                                                        JCExpression typeExp, JavafxBindStatus bindStatus) {
        JCExpression result;
        if (bindStatus.isUnidiBind()) {
            result = registerExpression(vmi.varSymbol, init);
        } else {
            result = boundTranslate(init, bindStatus);

            if (result.type.tsym != vmi.getUsedType().tsym) {
                Type realType = vmi.getRealType();
                JCFieldAccess makeSelect = make.at(diagPos).Select(typeExp, makeMethodName);
                makeSelect.sym = rs.resolveInternalMethod(diagPos, attrEnv, vmi.getUsedType(), makeMethodName, List.<Type>of(realType), realType.isPrimitive() ? List.<Type>nil() : List.<Type>of(realType)); // typeargs
                makeSelect.setType(makeSelect.sym.type);

                List<JCExpression> makeArgs = List.of(result);
                JCExpression makeApply = make.at(diagPos).Apply(null, makeSelect, makeArgs);
                makeApply.setType(vmi.getUsedType());

                result = makeApply;
            }
        }
        return result;
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        tree.defs = translate(tree.defs);
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

        if (classInfo.applyMethodDefs != null) {
            fillInApplyMethods(classInfo.applyMethodDefs, this);
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
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            if (tree.name == applyMethodNames[i]) {
                classInfo.setApplyMethod(tree, i);
                break;
            }
        }
        
        result = tree;
    }
    
    @Override
    public void visitVarDef(JCVariableDecl tree) {
        JavafxBindStatus bindStatus = JavafxBindStatus.UNBOUND;
        boolean isBound = false;
        if (tree instanceof JavafxJCVarDecl) {
            JavafxJCVarDecl var = (JavafxJCVarDecl)tree;
            
            var.anonymousChangeListener = translate(var.anonymousChangeListener);
            bindStatus = var.getBindStatus();
            isBound = var.isBound();
        }
        VarSymbol vsym = tree.sym;
        VarMorphInfo vmi = varMorphInfo(vsym);
        if (vmi.shouldMorph()) {
            JCExpression typeExp = makeUsedTypeExpression(vmi, tree.pos);

            if (tree.init != null) {
                tree.init = translateDefinitionalAssignment(tree.init, vmi, tree.pos(), typeExp, bindStatus);
            } else {
                // no initializing expression, do default
                Type realType = vmi.getRealType();
                JCFieldAccess makeSelect = make.at(tree.pos).Select(typeExp, makeMethodName);
                makeSelect.sym = rs.resolveInternalMethod(tree.pos(), 
                        attrEnv, vmi.getUsedType(), 
                        makeMethodName, 
                        List.<Type>nil(), 
                        realType.isPrimitive() ? List.<Type>nil() : List.<Type>of(realType)); // typeargs
                makeSelect.setType(makeSelect.sym.type);

                List<JCExpression> makeArgs = List.nil();
                JCExpression makeApply = make.at(tree.pos).Apply(null, makeSelect, makeArgs);
                makeApply.setType(vmi.getUsedType());

                tree.init = makeApply;
            }

            tree.type = typeExp.type;
            tree.vartype = typeExp;

            if (isBound) {
                tree.mods.flags |= Flags.FINAL;
            }
            result = tree;
            return;
        }
        tree.init = translate(tree.init);
        if (tree.init != null && tree.init.type.constValue() != null) {
            tree.init.type = tree.init.type.baseType();
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
                lhs.setType(vmi.getUsedType());
                if (tree instanceof JavafxJCAssign) {
                    // This is a definitional assignment
                    JavafxJCAssign assign = (JavafxJCAssign)tree;
                    JCExpression typeExp = makeUsedTypeExpression(vmi, tree.pos);
                    tree.rhs = translateDefinitionalAssignment(tree.rhs, 
                                vmi, 
                                tree.pos(), 
                                typeExp, 
                                assign.getBindStatus());
                    tree.setType(vmi.getUsedType());
                    result = tree;
                } else {
                    // normal assignment
                    JCExpression rhs = translate(tree.rhs);
                    Type realType = vmi.getRealType();
                    //if (realType.isPrimitive()) {
                    JCFieldAccess setSelect = make.Select(lhs, setMethodName);
                    setSelect.sym = rs.resolveInternalMethod(tree.pos(), attrEnv, vmi.getUsedType(), setMethodName, List.<Type>of(realType), List.<Type>nil()); // typeargs
                    setSelect.setType(setSelect.sym.type);

                    List<JCExpression> setArgs = List.of(rhs);
                    result = make.Apply(null, setSelect, setArgs);
                    result.setType(Symtab.voidType);
                }
            } else {
                tree.lhs = lhs;
                tree.rhs = translate(tree.rhs);
                result = tree;
            }
        }
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
                            baseLocationType,
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
    public void visitBinary(JCBinary tree) {
        super.visitBinary(tree);
        if (tree.type.constValue() != null) {
            tree.setType(tree.type.baseType());
        }
    }
    
    @Override
    public void visitUnary(JCUnary tree) {
        super.visitUnary(tree);
        if (tree.type.constValue() != null) {
            tree.setType(tree.type.baseType());
        }
    }
    
    
    //========================================================================================================================
    // Bound Expression support
    //========================================================================================================================
    
    void resetExpressionMapper() {
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            classInfo.exprNumNext[i]= 0;
            classInfo.infoByGroup[i] = ListBuffer.<ExprInfo>lb();
        }
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
    
    void fillInApplyMethods(JCMethodDecl[] applyMethodDefs, JavafxTypeMorpher morpher) {
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            ListBuffer<ExprInfo> infos = classInfo.infoByGroup[i];
            if (infos.length() >= 0) {      // TODO --- TEST!!!!!!!!!!!!!!!!!!
                JCMethodDecl meth = classInfo.applyMethodDefs[i];
                List<JCVariableDecl> params = meth.getParameters();
                //JCVariableDecl boundVar = params.head;
                JCVariableDecl numVar = params.tail.head;
                ListBuffer<JCCase> cases = ListBuffer.lb();
                ListBuffer<JCStatement> stmts;
                
                for (ExprInfo info : classInfo.infoByGroup[i]) {
                    stmts = ListBuffer.<JCStatement>lb();
                    stmts.append(buildExpression(info, meth));
                    stmts.append(make.Return(makeLit(Symtab.booleanType, 1)));
                    cases.append(make.Case(makeLit(Symtab.intType, info.exprNum), stmts.toList()));
                }
                JCExpression selector = make.Ident(numVar.sym);
                meth.body.stats = meth.body.stats.prepend( make.Switch(selector, cases.toList()) );
            }
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
        
        int argsGroup = varMap.size();
        if (argsGroup >= ARGS_IN_ARRAY) {
            argsGroup = ARGS_IN_ARRAY;
        }
        int exprNum = classInfo.exprNumNext[argsGroup]++;
        ExprInfo info = new ExprInfo(varMap, exprNum, vsym, tree, argsGroup);
        classInfo.infoByGroup[argsGroup].append(info);
        
        VarMorphInfo vmi = varMorphInfo(vsym);
        Type realType = vmi.getRealType();
        TypeSymbol realTsym = realType.tsym;
        Type usedType = vmi.getUsedType();
        TypeSymbol usedTsym = usedType.tsym;
        int typeKind = vmi.getTypeKind();
        
        JCExpression exprLocId = expressionLocationId(tree.pos, typeKind, argsGroup);
        if (typeKind == TYPE_KIND_OBJECT) {
            ListBuffer<JCExpression> args = ListBuffer.lb();
            args.append(make.Ident(realType.tsym));
            Type exprLocType = exprLocId.type;
            exprLocId = make.TypeApply(exprLocId, args.toList());
            exprLocId.setType(generifyIfNeeded(exprLocType, vmi));
        }
        
        ListBuffer<Type> argTypes = ListBuffer.lb();
        ListBuffer<JCExpression> argValues = ListBuffer.lb();
        argTypes.append(contextType);
        JCIdent thisId = make.Ident(new VarSymbol(Flags.FINAL, names._this, attrEnv.enclClass.type, attrEnv.enclClass.type.tsym));
        thisId.setType(thisId.type);
        argValues.append(thisId);
        argTypes.append(Symtab.intType);
        argValues.append(makeLit(Symtab.intType, exprNum));
        if (argsGroup == ARGS_IN_ARRAY) {
            Type argArrayType = new ArrayType(baseLocationType, syms.arrayClass);
            argTypes.append(argArrayType);
            ListBuffer<JCExpression> argContents = ListBuffer.lb();
            for (VarInfo vi : varMap.values()) {
                argContents.append(vi.origIdent);
            }
            JCNewArray argArgValue = make.NewArray(
                    make.Ident(baseLocationSym),
                    List.<JCExpression>nil(),
                    argContents.toList());
            argArgValue.setType(argArrayType);
            argValues.append(argArgValue);
        }
        
        JCFieldAccess makeSelect = make.Select(exprLocId, makeMethodName);
        makeSelect.sym = rs.resolveInternalMethod(tree.pos(),
                attrEnv,
                exprLocId.type,
                makeMethodName,
                argTypes.toList(),                                      // argtypes
                (typeKind == TYPE_KIND_OBJECT)? List.<Type>of(realType) : List.<Type>nil());   // typeargs
        makeSelect.setType(makeSelect.sym.type);
        
        JCExpression makeApply = make.Apply(null, makeSelect, argValues.toList());
        makeApply.setType(exprLocId.type);
        
        return makeApply;
    }
    
    public JCStatement buildExpression(ExprInfo info, JCMethodDecl applyMethod) {
        VarSymbol vsym = info.vsym;
        JCExpression tree = info.expr;
        int argsGroup = info.argsGroup;   
        varMap = info.varMap;
        
        List<JCVariableDecl> params = applyMethod.getParameters();
        JCVariableDecl boundVar = params.head;
        List<JCVariableDecl> args = params.tail.tail;
        
        int i = 0;
        for (VarSymbol orig : varMap.keySet()) {
            JCExpression argsExp = make.at(tree.pos).Ident(args.head.sym);
            if (argsGroup == ARGS_IN_ARRAY) {
                argsExp.setType(new ArrayType(baseLocationType, syms.arrayClass));
                argsExp = make.Indexed(argsExp, makeLit(Symtab.intType, i++));
            } else {
                args = args.tail;  // advance to next arg if explicit
            }
            argsExp.setType(baseLocationType);
            
            /***
             * // select the asBoolean() etc. method
             * Name asTypeMethodName = valueAccessName[orig.getTypeKind()];
             * JCFieldAccess methSelect = make.Select(argsExp, asTypeMethodName);
             * methSelect.sym = rs.resolveInternalMethod(tree.pos(),
             * attrEnv,
             * vsym.getUsedType(),
             * asTypeMethodName,
             * List.<Type>nil(),   // argtypes
             * List.<Type>nil());   // typeargs
             * methSelect.setType(methSelect.sym.type);
             * 
             * // apply the asBoolean() etc. method
             * JCExpression asTypeApply = make.Apply(null, methSelect, List.<JCExpression>nil());
             * asTypeApply.setType(realTypeByKind[orig.getTypeKind()]);
             * ***/
            
            varMap.get(orig).replacement = argsExp;
        }
        
        JCExpression updateExpr = boundTranslate(tree, JavafxBindStatus.UNIDIBIND);
        
        JCExpression lhs = make.Ident(boundVar.sym);
        VarMorphInfo vmi = varMorphInfo(vsym);
        Name valueSetMethod = valueSetName[vmi.getTypeKind()];
        JCFieldAccess setSelect = make.Select(lhs, valueSetMethod);
        setSelect.sym = rs.resolveInternalMethod(tree.pos(),
                attrEnv,
                vmi.getUsedType(),
                valueSetMethod,
                List.<Type>of(vmi.getRealType()),    // argtypes
                List.<Type>nil());   // typeargs
        setSelect.setType(setSelect.sym.type);
        
        List<JCExpression> setArgs = List.of(updateExpr);
        JCExpression updateApply = make.Apply(null, setSelect, setArgs);
        updateApply.setType(Symtab.voidType);
        return make.Exec(updateApply);
    }
}
