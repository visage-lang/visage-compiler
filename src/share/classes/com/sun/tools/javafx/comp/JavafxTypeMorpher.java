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

import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.jvm.ClassReader;

import com.sun.tools.javac.comp.*;

import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Flags;

import com.sun.tools.javafx.code.*;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import com.sun.tools.javafx.tree.*;

import java.io.OutputStreamWriter;
import com.sun.tools.javac.tree.Pretty;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

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
    private final Name[][] locationName;
    private final ClassSymbol[][] locationSym;
    private final Type[][] locationType;
    
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
 
        PerClassInfo() {
            this.bindContext = JavafxBindStatus.UNBOUND;
            this.inLHS = false;
            this.prependInFrontOfStatement = null;
            this.applyMethodDefs = new JCMethodDecl[ARGS_IN_ARRAY + 1 + 1];
            this.exprNumNext = new int[ARGS_IN_ARRAY + 1];
            this.infoByGroup = new ListBuffer[ARGS_IN_ARRAY + 1];
        }
    }
    
    private Map<JavafxVarSymbol, VarInfo> varMap;
    
    private PerClassInfo classInfo;
    
    private static class ExprInfo {
        final int exprNum;
        final JCExpression expr;
        final Map<JavafxVarSymbol,VarInfo> varMap;
        final JavafxVarSymbol vsym;
        final int argsGroup;
        
        JCStatement updateStatement;  // ????
        
        ExprInfo(
                Map<JavafxVarSymbol,VarInfo> varMap,
                int exprNum,
                JavafxVarSymbol vsym,
                JCExpression expr,
                int argsGroup) {
            this.varMap = varMap;
            this.exprNum = exprNum;
            this.vsym = vsym;
            this.expr = expr;
            this.argsGroup = argsGroup;
        }
    }
    
    private static class VarInfo {
        final JCIdent origIdent;
        JCExpression replacement;
        VarInfo(JCIdent origIdent) {
            this.origIdent = origIdent;
        }
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
        realTypeByKind[TYPE_KIND_DOUBLE] = syms.doubleType;
        realTypeByKind[TYPE_KIND_BOOLEAN] = syms.booleanType;
        realTypeByKind[TYPE_KIND_INT] = syms.intType;
        
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
            }
        };
    }

    
    public void morph(Env<AttrContext> attrEnv) {
        this.attrEnv = attrEnv;

        unconstifyHack().scan(attrEnv.tree);   //TODO: shouldn't need this

        // Determine variable usage
        JavafxVarUsageAnalysis analyzer = new JavafxVarUsageAnalysis();
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
    
    Type biDiLocationType(int typeKind) {
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
    
    private Type generifyIfNeeded(Type aLocationType, JavafxVarSymbol vsym) {
        Type newType;
        if (vsym.getTypeKind() == TYPE_KIND_OBJECT) {
            List<Type> actuals = List.<Type>of(vsym.getRealType());
            List<Type> formals = aLocationType.tsym.type.getTypeArguments();
            actuals.head = actuals.head.withTypeVar(formals.head);
            Type clazzOuter = biDiLocationType(vsym.getTypeKind()).getEnclosingType();
            newType = new ClassType(clazzOuter, actuals, aLocationType.tsym);
        } else {
            newType = aLocationType;
        }
        return newType;
    }
    
    private boolean shouldMorph(JavafxVarSymbol vsym) {
        if (!vsym.haveDeterminedMorphability()) {
            Type realType = vsym.type;
            if (vsym.isBound() || vsym.isBoundTo() || vsym.owner.kind != Kinds.MTH) {
                Type newType;
                int typeKind;
                if (realType.isPrimitive()) {
                    Symbol realTsym = realType.tsym;
                    if (realTsym == syms.doubleType.tsym) {
                        typeKind = TYPE_KIND_DOUBLE;
                    } else if (realTsym == syms.intType.tsym) {
                        typeKind = TYPE_KIND_INT;
                    } else if (realTsym == syms.booleanType.tsym) {
                        typeKind = TYPE_KIND_BOOLEAN;
                    } else {
                        assert false : "should not reach here";
                        typeKind = TYPE_KIND_OBJECT;
                    }
                } else {
                    typeKind = TYPE_KIND_OBJECT;
                }
                vsym.setTypeKind(typeKind);
                if (realType.constValue() != null) {
                    realType = realType.baseType();
                }
                vsym.setRealType(realType);
                // must be called AFTER typeKind and realType are set in vsym
                vsym.setUsedType(generifyIfNeeded(biDiLocationType(typeKind), vsym));
                vsym.markShouldMorph();
            } else {
                
            }
            vsym.markDeterminedMorphability();
        }
        return vsym.shouldMorph();
    }
    
    JCExpression convertVariableReference(JCExpression varRef, JavafxVarSymbol vsym) {
        JCExpression expr;
        
        varRef.setType(vsym.getUsedType());
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
                    vsym.getUsedType(),
                    getMethodName,
                    List.<Type>nil(),    // argtypes
                    List.<Type>nil());   // typeargs
            getSelect.setType(getSelect.sym.type);
            
            List<JCExpression> getArgs = List.<JCExpression>nil();
            expr = make.Apply(null, getSelect, getArgs);
            expr.setType(vsym.getRealType());
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
    
    public void visitTopLevel(JCCompilationUnit tree) {
        tree.defs = translate(tree.defs);
        result = tree;
    }
    
    public void visitClassDef(JCClassDecl tree) {
        JCClassDecl prevEnclClass = attrEnv.enclClass;
        PerClassInfo prevClassInfo = classInfo;
        attrEnv.enclClass = tree;
        classInfo = new PerClassInfo();
        
        resetExpressionMapper();
         
        tree.defs = translate(tree.defs);
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            assert applyMethodNames[i] != null;
        }
        fillInApplyMethods(classInfo.applyMethodDefs, this);
        
        attrEnv.enclClass = prevEnclClass;
        classInfo = prevClassInfo;
        
        result = tree;
    }
    
    public void visitMethodDef(JCMethodDecl tree) {
        // At least for now, prevent parameters and return from being translated
        //tree.params = translateVarDefs(tree.params);
        //tree.restype = translate(tree.restype);
        tree.body = translate(tree.body);
        
        int argGroup = -1;
        for (int i = 0; i <= ARGS_IN_ARRAY; ++i) {
            if (tree.name == applyMethodNames[i]) {
                assert classInfo.applyMethodDefs[i] == null;
                classInfo.applyMethodDefs[i] = tree;
                break;
            }
        }
        
        result = tree;
    }
    
    public void visitVarDef(JCVariableDecl tree) {
        if (tree instanceof JavafxJCVarDecl) {
            JavafxJCVarDecl var = (JavafxJCVarDecl)tree;
            
            if (var.sym instanceof JavafxVarSymbol) {
                JavafxVarSymbol vsym = (JavafxVarSymbol)(var.sym);
                if (shouldMorph(vsym)) {
                    
                    Type realType = vsym.getRealType();
                    TypeSymbol realTsym = realType.tsym;
                    Type usedType = vsym.getUsedType();
                    TypeSymbol usedTsym = usedType.tsym;
                    JCExpression typeExp;
                    
                    if (realType.isPrimitive()) {
                        typeExp = make.at(tree.pos).Ident(usedTsym);
                    } else {
                        JCExpression objLoc = biDiLocationId(tree.pos, TYPE_KIND_OBJECT);
                        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
                        args.append(make.Ident(realType.tsym));
                        JCExpression newTypeExp = make.TypeApply(objLoc, args.toList());
                        typeExp = make.at(tree.pos).Ident(usedTsym);
                    }
                    typeExp.setType(usedType);
                    var.type = usedType;
                    var.vartype = typeExp;
                    
                    // TODO: test will need to handle subtypes
                    if (tree.init != null) {
                        if (var.isUnidiBind()) {
                            tree.init = registerExpression(vsym, tree.init);;
                        } else {
                            tree.init = boundTranslate(tree.init, var.getBindStatus());
                            
                            if (tree.init.type.tsym != usedTsym) {
                                JCFieldAccess makeSelect = make.Select(typeExp, makeMethodName);
                                makeSelect.sym = rs.resolveInternalMethod(tree.pos(),
                                        attrEnv,
                                        usedType,
                                        makeMethodName,
                                        (tree.init == null)? List.<Type>nil() : List.<Type>of(realType),    // argtypes
                                        realType.isPrimitive()? List.<Type>nil() : List.<Type>of(realType));   // typeargs
                                makeSelect.setType(makeSelect.sym.type);
                                
                                List<JCExpression> makeArgs = (tree.init == null)? List.<JCExpression>nil() : List.<JCExpression>of(tree.init);
                                JCExpression makeApply = make.Apply(null, makeSelect, makeArgs);
                                makeApply.setType(usedType);
                                
                                tree.init = makeApply;
                            }
                        }
                    } else {
                        // no initializing expression, do default
                        JCFieldAccess makeSelect = make.Select(typeExp, makeMethodName);
                        makeSelect.sym = rs.resolveInternalMethod(tree.pos(), attrEnv, usedType, makeMethodName, List.<Type>nil(), realType.isPrimitive() ? List.<Type>nil() : List.<Type>of(realType)); // typeargs
                        makeSelect.setType(makeSelect.sym.type);

                        List<JCExpression> makeArgs = List.<JCExpression>nil();
                        JCExpression makeApply = make.Apply(null, makeSelect, makeArgs);
                        makeApply.setType(usedType);

                        tree.init = makeApply;
                    }
                    if (var.isBound()) {
                        var.mods.flags |= Flags.FINAL;
                    }
                    result = tree;
                    return;
                }
            }
        }
        tree.init = translate(tree.init);
        if (tree.init != null && tree.init.type.constValue() != null) {
            tree.init.type = tree.init.type.baseType();
        }
        result = tree;
    }
    
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
        JavafxVarSymbol vsym = null;
        if (sym != null && (sym instanceof JavafxVarSymbol)) {
            vsym = (JavafxVarSymbol)sym;
        }
        if (vsym != null && shouldMorph(vsym)) {
            lhs.setType(vsym.getUsedType());
            JavafxBindStatus bind = JavafxBindStatus.UNBOUND;
            if (tree instanceof JavafxJCAssign) {
                JavafxJCAssign assign = (JavafxJCAssign)tree;
                bind = assign.getBindStatus();
            }
            
            if (bind.isBidiBind()) {  // TODO: now
                // It is bound, just do the assign
                tree.lhs = lhs;
                tree.rhs = boundTranslate(tree.rhs, bind);
                tree.rhs.setType(vsym.getUsedType());
                tree.setType(vsym.getUsedType());
                result = tree;
            } else {
                JCExpression rhs = translate(tree.rhs);
                Type realType = vsym.getRealType();
                //if (realType.isPrimitive()) {
                JCFieldAccess setSelect = make.Select(lhs, setMethodName);
                setSelect.sym = rs.resolveInternalMethod(tree.pos(),
                        attrEnv,
                        vsym.getUsedType(),
                        setMethodName,
                        List.<Type>of(realType),    // argtypes
                        List.<Type>nil());   // typeargs
                setSelect.setType(setSelect.sym.type);
                
                List<JCExpression> setArgs = List.<JCExpression>of(rhs);
                result = make.Apply(null, setSelect, setArgs);
                result.setType(syms.voidType);
            }
        } else {
            tree.lhs = lhs;
            tree.rhs = translate(tree.rhs);
            result = tree;
        }
    }
    
    public void visitSelect(JCFieldAccess tree) {
        // this may or may not be in a LHS but in either
        // event the selector is a value expression
        tree.selected = translateLHS(tree.selected, false);
        
        if (tree.sym instanceof JavafxVarSymbol) {
            JavafxVarSymbol vsym = (JavafxVarSymbol)tree.sym;
            if (shouldMorph(vsym)) {
                result = convertVariableReference(tree, vsym);
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
    
    public void visitIdent(JCIdent tree)   {
        if (tree.sym instanceof JavafxVarSymbol) {
            JavafxVarSymbol vsym = (JavafxVarSymbol)tree.sym;
            if (shouldMorph(vsym)) {
                if (classInfo.bindContext.isUnidiBind()) {
                    Type realType = vsym.getRealType();
                    JCExpression locAccess = varMap.get(vsym).replacement;
                    
                    Name methName = valueAccessName[vsym.getTypeKind()];
                    JCFieldAccess getSelect = make.Select(locAccess, methName);
                    getSelect.sym = rs.resolveInternalMethod(locAccess.pos(),
                            attrEnv,
                            baseLocationType,
                            methName,
                            List.<Type>nil(),    // argtypes
                            List.<Type>nil());   // typeargs
                    getSelect.setType(getSelect.sym.type);
                    
                    List<JCExpression> getArgs = List.<JCExpression>nil();
                    JCExpression expr = make.Apply(null, getSelect, getArgs);
                    expr.setType(realType);
                    
                    if (vsym.getTypeKind() == TYPE_KIND_OBJECT) {
                        expr = castToReal(realType, expr);
                    }
                    
                    result = expr;
                    return;
                } else {
                    result = convertVariableReference(tree, vsym);
                    return;
                }
            }
        }
        super.visitIdent(tree);
        if (tree.type.constValue() != null) {
            tree.setType(tree.type.baseType());
        }
    }

    public void visitBlockExpression(JFXBlockExpression tree) {
        tree.stats = translate(tree.stats);
        tree.value = translate(tree.value);
	result = tree;
    }
 
    /**
     * Allow prepending of statements and/or deletin by translation to null
     */
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
    
    public void visitBinary(JCBinary tree) {
        super.visitBinary(tree);
        if (tree.type.constValue() != null) {
            tree.setType(tree.type.baseType());
        }
    }
    
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
                ListBuffer<JCCase> cases = ListBuffer.<JCCase>lb();
                ListBuffer<JCStatement> stmts;
                
                for (ExprInfo info : classInfo.infoByGroup[i]) {
                    stmts = ListBuffer.<JCStatement>lb();
                    stmts.append(buildExpression(info, meth));
                    stmts.append(make.Return(makeLit(syms.booleanType, 1)));
                    cases.append(make.Case(makeLit(syms.intType, info.exprNum), stmts.toList()));
                }
                JCExpression selector = make.Ident(numVar.sym);
                meth.body.stats = meth.body.stats.prepend( make.Switch(selector, cases.toList()) );
            }
        }
    }
    
    public JCExpression registerExpression(JavafxVarSymbol vsym, JCExpression tree) {
        final HashMap<JavafxVarSymbol,VarInfo> varMap = new HashMap<JavafxVarSymbol,VarInfo>();
        
        TreeScanner ts = new TreeScanner() {
            public void visitIdent(JCIdent tree)   {
                if (tree.sym instanceof JavafxVarSymbol) {
                    JavafxVarSymbol vsym = (JavafxVarSymbol)tree.sym;
                    if (vsym.shouldMorph()) {
                        tree.setType(vsym.getUsedType());
                        
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
        
        
        Type realType = vsym.getRealType();
        TypeSymbol realTsym = realType.tsym;
        Type usedType = vsym.getUsedType();
        TypeSymbol usedTsym = usedType.tsym;
        int typeKind = vsym.getTypeKind();
        
        JCExpression exprLocId = expressionLocationId(tree.pos, typeKind, argsGroup);
        if (typeKind == TYPE_KIND_OBJECT) {
            ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
            args.append(make.Ident(realType.tsym));
            Type exprLocType = exprLocId.type;
            exprLocId = make.TypeApply(exprLocId, args.toList());
            exprLocId.setType(generifyIfNeeded(exprLocType, vsym));
        }
        
        ListBuffer<Type> argTypes = ListBuffer.<Type>lb();
        ListBuffer<JCExpression> argValues = ListBuffer.<JCExpression>lb();
        argTypes.append(contextType);
        JCIdent thisId = make.Ident(new VarSymbol(Flags.FINAL, names._this, attrEnv.enclClass.type, attrEnv.enclClass.type.tsym));
        thisId.setType(thisId.type);
        argValues.append(thisId);
        argTypes.append(syms.intType);
        argValues.append(makeLit(syms.intType, exprNum));
        if (argsGroup == ARGS_IN_ARRAY) {
            Type argArrayType = new ArrayType(baseLocationType, syms.arrayClass);
            argTypes.append(argArrayType);
            ListBuffer<JCExpression> argContents = ListBuffer.<JCExpression>lb();
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
        JavafxVarSymbol vsym = info.vsym;
        JCExpression tree = info.expr;
        int argsGroup = info.argsGroup;
        varMap = info.varMap;
        
        
        List<JCVariableDecl> params = applyMethod.getParameters();
        JCVariableDecl boundVar = params.head;
        List<JCVariableDecl> args = params.tail.tail;
        
        int i = 0;
        for (JavafxVarSymbol orig : info.varMap.keySet()) {
            JCExpression argsExp = make.at(tree.pos).Ident(args.head.sym);
            if (argsGroup == ARGS_IN_ARRAY) {
                argsExp.setType(new ArrayType(baseLocationType, syms.arrayClass));
                argsExp = make.Indexed(argsExp, makeLit(syms.intType, i++));
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
        Name valueSetMethod = valueSetName[vsym.getTypeKind()];
        JCFieldAccess setSelect = make.Select(lhs, valueSetMethod);
        setSelect.sym = rs.resolveInternalMethod(tree.pos(),
                attrEnv,
                vsym.getUsedType(),
                valueSetMethod,
                List.<Type>of(vsym.getRealType()),    // argtypes
                List.<Type>nil());   // typeargs
        setSelect.setType(setSelect.sym.type);
        
        List<JCExpression> setArgs = List.<JCExpression>of(updateExpr);
        JCExpression updateApply = make.Apply(null, setSelect, setArgs);
        updateApply.setType(syms.voidType);
        return make.Exec(updateApply);
    }
}
