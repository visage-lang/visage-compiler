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
import com.sun.tools.javac.code.Symtab;
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

        public boolean shouldMorph() {
            if (!haveDeterminedMorphability()) {
                realType = varSymbol.type;
                TypeSymbol realTsym = realType.tsym;
                Type locationType = null;
                if (realTsym == declLocation[TYPE_KIND_OBJECT].sym) {
                    locationType = ((ClassType) realType).typarams_field.head;
                    typeKind = TYPE_KIND_OBJECT;
                } else if (realTsym == declLocation[TYPE_KIND_BOOLEAN].sym) {
                    locationType = syms.booleanType;
                    typeKind = TYPE_KIND_BOOLEAN;
                } else if (realTsym == declLocation[TYPE_KIND_DOUBLE].sym) {
                    locationType = syms.doubleType;
                    typeKind = TYPE_KIND_DOUBLE;
                } else if (realTsym == declLocation[TYPE_KIND_INT].sym) {
                    locationType = syms.intType;
                    typeKind = TYPE_KIND_INT;
                }
                if (locationType != null) {
                    // External module with a Location type
                    setUsedType(realType);
                    realType = locationType;
                    markShouldMorph();
                } else if (isBoundTo() || isAttribute()) {
                    if (realType.isPrimitive()) {
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
        public Type getBindingExpressionType() { return generifyIfNeeded(bindingExpressionType(typeKind), this); }
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
        realTypeByKind[TYPE_KIND_DOUBLE] = syms.doubleType;
        realTypeByKind[TYPE_KIND_BOOLEAN] = syms.booleanType;
        realTypeByKind[TYPE_KIND_INT] = syms.intType;
        
        defaultValueByKind = new Object[TYPE_KIND_COUNT];
        defaultValueByKind[TYPE_KIND_OBJECT] = null;
        defaultValueByKind[TYPE_KIND_DOUBLE] = new Double(0.0);
        defaultValueByKind[TYPE_KIND_BOOLEAN] = new Integer(0);
        defaultValueByKind[TYPE_KIND_INT] = new Integer(0);
        
        getMethodName = Name.fromString(names, "get");
        setMethodName = Name.fromString(names, "set");
        makeMethodName = Name.fromString(names, "make");
        makeLazyMethodName = Name.fromString(names, "makeLazy");
    }
       
    public void morph(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;
        
        translate(attrEnv.tree);
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
            Type clazzOuter = declLocationType(vmi.getTypeKind()).getEnclosingType();
            newType = new ClassType(clazzOuter, actuals, aLocationType.tsym);
        } else {
            newType = aLocationType;
        }
        return newType;
    }
        
    public JCExpression convertVariableReference(JCExpression varRef, Symbol sym, JavafxBindStatus bindContext, boolean inLHS) {
        JCExpression expr = varRef;

        if (sym instanceof VarSymbol) {
            VarSymbol vsym = (VarSymbol) sym;

            VarMorphInfo vmi = varMorphInfo(vsym);
            if (vmi.shouldMorph()) {
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
                    List<JCExpression> getArgs = List.nil();
                    expr = make.Apply(null, getSelect, getArgs);
                }
            }
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
    
    public JCExpression translateDefinitionalAssignment(JCExpression init, 
                                                        VarMorphInfo vmi, 
                                                        DiagnosticPosition diagPos,
                                                        JavafxBindStatus bindStatus) {
        JCExpression initExpr = init != null? 
                init : 
                makeLit(vmi.getRealType(), vmi.getDefaultValue(), diagPos);
        if (bindStatus.isUnidiBind()) {
            initExpr = buildExpression(vmi.varSymbol, initExpr, bindStatus);
        } else {
            if (init == null || initExpr.type.tsym != vmi.getUsedType().tsym) {
                initExpr = makeCall(vmi, diagPos, List.of(initExpr), varLocation, makeMethodName);
            }
        }
        return initExpr;
    }
    
    public JCExpression morphAssign(JCAssign tree, JCExpression lhs, JCExpression rhs) {
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
                JCFieldAccess setSelect = make.Select(lhs, setMethodName);
                List<JCExpression> setArgs = List.of(rhs);
                return make.Apply(null, setSelect, setArgs);
            }
        }
        tree.lhs = lhs;
        tree.rhs = rhs;
        return tree;
    }        

    //========================================================================================================================
    // Bound Expression support
    //========================================================================================================================
    
    /** Make an attributed tree representing a literal. This will be 
     *  a Literal node.
     *  @param type       The literal's type.
     *  @param value      The literal's value.
     */
    JCExpression makeLit(Type type, Object value, DiagnosticPosition diagPos) {
        int tag = value==null? TypeTags.BOT : type.tag;
        return make.at(diagPos).Literal(tag, value).setType(type.constType(value));
    }
    
    public JCExpression buildExpression(VarSymbol vsym, JCExpression tree, JavafxBindStatus bindStatus) {
        DiagnosticPosition diagPos = tree.pos();
        final ListBuffer<JCExpression> varList = ListBuffer.<JCExpression>lb();
        
        TreeScanner ts = new TreeScanner() {
            @Override
            public void visitIdent(JCIdent tree)   {
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol vsym = (VarSymbol)tree.sym;
                    VarMorphInfo vmi = varMorphInfo(vsym);
                    assert vmi.shouldMorph();
                    
                    varList.append(tree);
                }
            }
        };
        ts.scan(tree);
        
        VarMorphInfo vmi = varMorphInfo(vsym);
        
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
        argValues.appendList(varList);
        
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
