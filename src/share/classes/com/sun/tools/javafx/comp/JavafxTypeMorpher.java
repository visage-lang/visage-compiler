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
import com.sun.tools.javafx.code.JavafxSymtab;
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
import com.sun.tools.javafx.comp.JavafxToJava.JCExpressionTupple;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Robert Field
 */
public class JavafxTypeMorpher {
    protected static final Context.Key<JavafxTypeMorpher> typeMorpherKey =
            new Context.Key<JavafxTypeMorpher>();
    
    private final Name.Table names;
    private final ClassReader reader;
    private final TreeMaker make;
    private final JavafxSymtab syms;
    private final Log log;
    private final JavafxToJava toJava;
    private final Types types;
    
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
        Type elementType = null;
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
            } else if (owner.kind == Kinds.TYP) {
                if (varSymbol instanceof JavafxVarSymbol) {
                    return true; // we made it, soassume it is from a JavaFX class
                } else if (!varSymbol.toString().equals("super") && !varSymbol.toString().equals("this")) {
                    //TODO: temp hack until the MI init code is in place
                    ClassSymbol klass = (ClassSymbol)owner;
                    String source = klass.sourcefile.getName();
                    String extension = source.substring(source.length()-3);
                    return extension.equals(".fx");
                }
            }
            // what is this?
            return false;
        }

        private boolean isSequence() {
            return types.erasure(realType) == syms.javafx_SequenceTypeErasure;
        }

        public boolean shouldMorph() {
            if (!haveDeterminedMorphability()) {
                realType = varSymbol.type;
                TypeSymbol realTsym = realType.tsym;
                //check if symbol is already a Location, needed for source class
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
                } else if (((varSymbol.flags() & Flags.PARAMETER) == 0) && 
                        (isBoundTo() || isAttribute() || isSequence())) {
                    // Must be a Location is bound, or a sequence,
                    // and, at least for now if it is an attribute.
                    // However, if this is a parameter it is either synthetic
                    // (like a loop or formal var) so we don't want to morph
                    // or it is a function param.  If we do bound param we
                    // will need to change this.
                    //TODO: should be a Location if there is a trigger on it
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
                        if (isSequence()) {
                            typeKind = TYPE_KIND_SEQUENCE;
                            elementType = realType.getTypeArguments().head;
                        } else {
                            typeKind = TYPE_KIND_OBJECT;
                        }
                    }
                    if (realType.constValue() != null) {
                        realType = realType.baseType();
                    }
                    // must be called AFTER typeKind and realType are set in vsym
                    setUsedType(generifyIfNeeded(declLocationType(typeKind), this));
                    markShouldMorph();
                    //System.err.println("Will morph: " + varSymbol);
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
        public Type getElementType() { return elementType; }
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
        
        syms = (JavafxSymtab)(JavafxSymtab.instance(context));
        types = Types.instance(context);
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
        locClass[TYPE_KIND_SEQUENCE] = "Sequence";

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
        realTypeByKind[TYPE_KIND_SEQUENCE] = syms.javafx_SequenceType;
        
        defaultValueByKind = new Object[TYPE_KIND_COUNT];
        defaultValueByKind[TYPE_KIND_OBJECT] = null;
        defaultValueByKind[TYPE_KIND_DOUBLE] = new Double(0.0);
        defaultValueByKind[TYPE_KIND_BOOLEAN] = new Integer(0);
        defaultValueByKind[TYPE_KIND_INT] = new Integer(0);
        defaultValueByKind[TYPE_KIND_SEQUENCE] = null; //TODO: empty sequence
        
        getMethodName = Name.fromString(names, "get");
        setMethodName = Name.fromString(names, "set");
        makeMethodName = Name.fromString(names, "make");
        makeLazyMethodName = Name.fromString(names, "makeLazy");
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
        return ((JavafxTreeMaker)make).at(diagPos).Identifier(lnst.name);
    }
    
    JCExpression castToReal(Type realType, JCExpression expr) {
        // cast the expression so that boxing works
        JCExpression typeExpr = make.Type(realType);
        JCExpression result = make.TypeCast(typeExpr, expr);     
        return result;
    }
    
    private Type generifyIfNeeded(Type aLocationType, VarMorphInfo vmi) {
        Type newType;
        if (vmi.getTypeKind() == TYPE_KIND_OBJECT || 
                vmi.getTypeKind() == TYPE_KIND_SEQUENCE) {
            List<Type> actuals = vmi.getTypeKind() == TYPE_KIND_SEQUENCE?
                  vmi.getRealType().getTypeArguments()
                : List.of(vmi.getRealType());
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
    
    public JCExpressionTupple buildDefinitionalAssignment(DiagnosticPosition diagPos,
                    VarMorphInfo vmi, JCExpression fxInit, JCExpression translatedInit,
                    JavafxBindStatus bindStatus, boolean isAttribute) {
        JCExpressionTupple ret;
        JCExpression initExpr = translatedInit != null? 
                translatedInit : 
                vmi.getTypeKind() == TYPE_KIND_SEQUENCE? 
                      toJava.makeEmptySeuenceCreator(diagPos, vmi.getElementType())
                    : makeLit(vmi.getRealType(), vmi.getDefaultValue(), diagPos);
        ret = new JCExpressionTupple(initExpr, null);
        if (bindStatus.isUnidiBind()) {
            ret = buildExpression(vmi.varSymbol, fxInit, initExpr, bindStatus, isAttribute);
        } else if (!bindStatus.isBidiBind()) {
            initExpr = makeCall(vmi, diagPos, List.of(initExpr), varLocation, makeMethodName);
            ret = new JCExpressionTupple(initExpr, null);
        }
        return ret;
    }
    
    public JCExpression morphAssign(DiagnosticPosition diagPos, JCExpression lhs, JCExpression rhs) {
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
                return make.at(diagPos).Apply(null, setSelect, setArgs);
            }
        }
        return make.at(diagPos).Assign(lhs, rhs); // make a new one so we are non-destructive
    }        
    
    /**
     * assignment of a sequence element --  s[i]=8
     */
    public JCExpression morphSequenceIndexedAssign(
            DiagnosticPosition diagPos, 
            JCExpression seq, 
            JCExpression index, 
            JCExpression value) {
        JCFieldAccess select = make.Select(seq, setMethodName);
        List<JCExpression> args = List.of(index, value);
        return make.at(diagPos).Apply(null, select, args);
    }        

    /**
     * access of a sequence element (RHS) --  s[i]
     */
    public JCExpression morphSequenceIndexedAccess(
            DiagnosticPosition diagPos, 
            JCExpression seq, 
            JCExpression index) {
        JCFieldAccess select = make.at(diagPos).Select(seq, getMethodName);
        List<JCExpression> args = List.of(index);
        return make.at(diagPos).Apply(null, select, args);
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
    
    /**
     * Build a list of dependencies for an expression.  It should include
     * any references to variables (which are Locations) but exclude
     * any that are defined internal to the expression.
     * The resulting list is translated.
     */
    private List<JCExpression> buildDependencies(JCExpression expr) {
        final Map<VarSymbol, JCExpression> refMap = new HashMap<VarSymbol, JCExpression>();
        final Set<VarSymbol> internalSet = new HashSet<VarSymbol>();
        
        JavafxTreeScanner ts = new JavafxTreeScanner() {
            @Override
            public void visitIdent(JCIdent tree)   {
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol ivsym = (VarSymbol)tree.sym;
                    VarMorphInfo vmi = varMorphInfo(ivsym);
                    if (vmi.shouldMorph()) {
                        refMap.put(ivsym, tree);
                    }
                }
            }
            @Override
            public void visitSelect(JCFieldAccess tree)   {
                super.visitSelect(tree);
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol ivsym = (VarSymbol)tree.sym;
                    VarMorphInfo vmi = varMorphInfo(ivsym);
                    if (vmi.shouldMorph()) {
                        refMap.put(ivsym, tree);
                    }
                }
            }
            @Override
            public void visitVar(JFXVar tree)   {
                if (tree.sym instanceof VarSymbol) {
                    VarSymbol vvsym = tree.sym;
                    internalSet.add(vvsym);
                }
            }
        };
        ts.scan(expr);
        
        ListBuffer<JCExpression> depend = ListBuffer.<JCExpression>lb();
        for (Map.Entry<VarSymbol, JCExpression> ref : refMap.entrySet()) {
            if (!internalSet.contains(ref.getKey())) {
                depend.append( toJava.translateLHS( ref.getValue() ) );
            }
        }
        
        return depend.toList();
    }
    
    private JCExpressionTupple buildExpression(VarSymbol vsym, 
            JCExpression fxInit, JCExpression translatedInit, JavafxBindStatus bindStatus, boolean isAttribute) {
        DiagnosticPosition diagPos = fxInit.pos();
        VarMorphInfo vmi = varMorphInfo(vsym);
        
        ListBuffer<JCExpression> argValues = ListBuffer.lb();

        JCBlock body = make.at(diagPos).Block(0, List.<JCStatement>of(
                            make.at(diagPos).Return(translatedInit)));
        JCMethodDecl getMethod = make.at(diagPos).MethodDef(
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
        List<JCExpression> dependencies = buildDependencies(fxInit);
        if (!isAttribute) {
            argValues.appendList(dependencies);
        }
        
        Name makeName = bindStatus.isLazy()? makeLazyMethodName : makeMethodName;
        return new JCExpressionTupple(makeCall(vmi, diagPos, argValues.toList(), exprLocation, makeName), isAttribute ? dependencies : null);
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
