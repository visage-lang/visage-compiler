/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import javax.lang.model.type.TypeKind;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.SequenceSliceTree;
import com.sun.tools.javac.code.*;
import static com.sun.tools.javac.code.Flags.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type.*;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javafx.code.FunctionType;
import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javafx.code.JavafxSymtab;
import com.sun.tools.javafx.code.JavafxTypes;
import com.sun.tools.javafx.comp.JavafxToJava.UseSequenceBuilder;
import com.sun.tools.javafx.comp.JavafxToJava.Wrapped;
import static com.sun.tools.javafx.code.JavafxVarSymbol.*;
import static com.sun.tools.javafx.comp.JavafxDefs.*;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.BindAnalysis;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.TypeMorphInfo;
import com.sun.tools.javafx.comp.JavafxTypeMorpher.VarMorphInfo;
import com.sun.tools.javafx.tree.*;

public class JavafxToBound extends JCTree.Visitor implements JavafxVisitor {
    protected static final Context.Key<JavafxToBound> jfxToJavaKey =
        new Context.Key<JavafxToBound>();

    /*
     * the result of translating a tree by a visit method
     */
    JCTree result;

    /*
     * modules imported by context
     */
    private final JavafxToJava toJava;
    private final JavafxTreeMaker make;  // should be generating Java AST, explicitly cast when not
    private final Log log;
    private final Name.Table names;
    private final JavafxTypes types;
    private final JavafxSymtab syms;
    private final JavafxInitializationBuilder initBuilder;
    private final JavafxTypeMorpher typeMorpher;
    private final JavafxDefs defs;
    private final JavafxResolve rs;
    
    /*
     * other instance information
     */
    private final Symbol doubleObjectTypeSymbol;
    private final Symbol intObjectTypeSymbol;
    private final Symbol booleanObjectTypeSymbol;

    /*
     * Buffers holding definitions waiting to be prepended to the current list of definitions.
     * At class or top-level these are the same.  
     * Within a method (or block) prependToStatements is split off.
     * They need to be different because anonymous classes need to be declared in the scope of the method,
     * but interfaces can't be declared here.
     */
    //ListBuffer<JCTree> bindingExpressionDefs = null;
    
    private JavafxEnv<JavafxAttrContext> attrEnv;
    private Target target;

    /*
     * static information
     */

    static final boolean generateBoundFunctions = true;
    static final boolean generateBoundVoidFunctions = false;
    static final boolean permeateBind = false;
    static final boolean generateNullChecks = true;
    
    private static final String cBoundSequences = "com.sun.javafx.runtime.sequence.BoundSequences";
    private static final String cBoundOperators = "com.sun.javafx.runtime.;pcation.BoundOperators";
    
    /** Class symbols for classes that need a reference to the outer class. */
    Set<ClassSymbol> hasOuters = new HashSet<ClassSymbol>();
        
    public static JavafxToBound instance(Context context) {
        JavafxToBound instance = context.get(jfxToJavaKey);
        if (instance == null)
            instance = new JavafxToBound(context);
        return instance;
    }

    protected JavafxToBound(Context context) {
        context.put(jfxToJavaKey, this);

        toJava = JavafxToJava.instance(context);
        make = JavafxTreeMaker.instance(context);
        log = Log.instance(context);
        names = Name.Table.instance(context);
        types = JavafxTypes.instance(context);
        syms = (JavafxSymtab)JavafxSymtab.instance(context);
        typeMorpher = JavafxTypeMorpher.instance(context);
        initBuilder = JavafxInitializationBuilder.instance(context);
        target = Target.instance(context);
        rs = JavafxResolve.instance(context);
        defs = JavafxDefs.instance(context);
        
        doubleObjectTypeSymbol = types.boxedClass(syms.doubleType).type.tsym;
        intObjectTypeSymbol = types.boxedClass(syms.intType).type.tsym;
        booleanObjectTypeSymbol = types.boxedClass(syms.booleanType).type.tsym;
    }
    
    /** Visitor method: Translate a single node.
     */
    @SuppressWarnings("unchecked")
    public <T extends JCTree> T translate(T tree) {
        T ret;
	if (tree == null) {
	    ret = null;
	} else {
	    tree.accept(this);
	    ret = (T)this.result;
	    this.result = null;
	}
        return ret;
    }
    
    public JCExpression translate(JCExpression tree, Type type) {
        if (tree == null)
            return null;
        if (types.isSequence(tree.type) && types.isArray(type)) {
             ListBuffer<JCStatement> stats = ListBuffer.lb();
            DiagnosticPosition diagPos = tree.pos();
            JCExpression init = (JCExpression) translate(tree);
            Type elemType = types.elemtype(type);
            if (elemType.isPrimitive()) {
                String mname = "toArray";
                if (elemType == syms.floatType)
                    mname = "toFloatArray";
                return callExpression(diagPos, makeTypeTree(syms.javafx_SequencesType, diagPos, false),
                       mname, init);
            }
            JCVariableDecl tmpVar = makeTmpVar(diagPos, tree.type, init);
            stats.append(tmpVar);
            JCVariableDecl arrVar = makeTmpVar(diagPos, "arr", type, 
                    make.NewArray(makeTypeTree(elemType, diagPos, true),
                        List.<JCExpression>of(callExpression(diagPos, make.Ident(tmpVar.name), "size")), null));
            stats.append(arrVar);
            stats.append(toJava.callStatement(diagPos, make.Ident(tmpVar.name), "toArray",
                            List.of(make.Ident(arrVar.name), make.Literal(TypeTags.INT, 0))));
            JCIdent ident2 = make.Ident(arrVar.name);
            return makeBlockExpression(diagPos, stats, ident2);
            
        }
        
        JCExpression ret = translate(tree);

        Type paramType = tree.type;
        if (paramType == syms.javafx_IntegerType ||
                type == syms.javafx_IntegerType) {
            if (paramType != type && type.isPrimitive()) {
                ret = make.at(tree).TypeCast(type, ret);
            }
        }

        return ret;
    }

    /** Visitor method: translate a list of nodes.
     */
    public <T extends JCTree> List<T> translate(List<T> trees) {
        ListBuffer<T> translated = ListBuffer.lb();
	if (trees == null) return null;
	for (List<T> l = trees; l.nonEmpty();  l = l.tail) {
            T tree = translate(l.head);
            if (tree != null) {
                translated.append( tree );
            }
        }
	return translated.toList();
    }
    
    public void toBound(JavafxEnv<JavafxAttrContext> attrEnv) {
        this.attrEnv = attrEnv;
        
        attrEnv.toplevel = translate(attrEnv.toplevel);
    }

    interface Translator {
        JCTree doit();
    }


    @Override
    public void visitInstanciate(JFXInstanciate tree) {
        // for now do the F3 definition where bind doesn't permiate
        result = makeUnboundLocation( tree.pos(), tree.type, toJava.translate(tree) );
    }

    @Override
    public void visitStringExpression(JFXStringExpression tree) {
        StringBuffer sb = new StringBuffer();
        List<JCExpression> parts = tree.getParts();
        ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();
        
        JCLiteral lit = (JCLiteral)(parts.head);            // "...{
        sb.append((String)lit.value);            
        parts = parts.tail;
        
        while (parts.nonEmpty()) {

            lit = (JCLiteral)(parts.head);                  // optional format (or null)
            String format = (String)lit.value;
            parts = parts.tail;
            JCExpression exp = parts.head;
            if (exp != null &&
                types.isSameType(exp.type, syms.javafx_TimeType)) {
                exp = make.Apply(null,
                                 make.Select(translate(exp), Name.fromString(names, "toDate")), 
                                 List.<JCExpression>nil());
                sb.append(format.length() == 0? "%tQms" : format);
            } else {
                exp = translate(exp);
                sb.append(format.length() == 0? "%s" : format);
            }
            values.append(exp);
            parts = parts.tail;
            
            lit = (JCLiteral)(parts.head);                  // }...{  or  }..."
            sb.append((String)lit.value);
            parts = parts.tail;
        }
        JCLiteral formatLiteral = make.at(tree.pos).Literal(TypeTags.CLASS, sb.toString());
        values.prepend(formatLiteral);
        JCExpression formatter;
        if (tree.translationKey != null) {
            formatter = make.Ident(Name.fromString(names, "com"));
            for (String s : new String[] {"sun", "javafx", "runtime", "util", "StringLocalization", "getLocalizedString"}) {
                formatter = make.Select(formatter, Name.fromString(names, s));
            }
            values.prepend(make.Literal(TypeTags.CLASS, tree.translationKey));
            String resourceName =
                attrEnv.enclClass.sym.flatname.toString().replace('.', '/').replaceAll("\\$.*", "");
            values.prepend(make.Literal(TypeTags.CLASS, resourceName));
        } else {
            formatter = make.Ident(Name.fromString(names, "java"));
            for (String s : new String[] {"lang", "String", "format"}) {
                formatter = make.Select(formatter, Name.fromString(names, s));
            }
        }
        result = make.Apply(null, formatter, values.toList());
    }

    @Override
    public void visitVar(JFXVar tree) {
        DiagnosticPosition diagPos = tree.pos();
        Type type = tree.type;
        JCModifiers mods = tree.getModifiers();
        long modFlags = mods == null ? 0L : mods.flags;
        VarSymbol vsym = tree.sym;
        VarMorphInfo vmi = typeMorpher.varMorphInfo(vsym);

        // convert the type to the Location type
        type = vmi.getMorphedType();

        // Locations are never overwritten
        modFlags |= Flags.FINAL;

        mods = make.at(diagPos).Modifiers(modFlags);
        JCExpression typeExpression = toJava.makeTypeTree(type, diagPos, true);

        JCExpression init = translate(tree.init);

        //TODO: this is broken: init doesn't have type
        // Convert initializers returning Java arrays to sequences.
        if (type.tag == TypeTags.ARRAY) {
            JCExpression newTree = ((JCExpression)makeTypeTree(((ArrayType)type).elemtype, diagPos, types.isCompoundClass(((ArrayType)type).elemtype.tsym)));
            newTree.type = ((ArrayType)type).elemtype;
            WildcardType tpType = new WildcardType(newTree.type, BoundKind.EXTENDS, type.tsym);
            ClassType classType = new ClassType(((JavafxSymtab)syms).javafx_SequenceType, List.<Type>of(tpType), ((JavafxSymtab)syms).javafx_SequenceType.tsym);
            typeExpression = makeTypeTree(classType, diagPos, false);

            if (init.type != ((JavafxSymtab)syms).javafx_SequenceType) {
                init = toJava.makeSequence(diagPos, init, newTree.type);
            }
        }

        result = make.at(diagPos).VarDef(mods, tree.name, typeExpression, init);
    }

    @Override
    public void visitOperationValue(JFXFunctionValue tree) {
        JFXFunctionDefinition def = tree.definition;
        result = toJava.makeFunctionValue(make.Ident(defs.lambdaName), def, tree.pos(), (MethodType) def.type);
    }
        
    public void visitBlockExpression(JFXBlockExpression tree) {   //done
        DiagnosticPosition diagPos = tree.pos();

        JCExpression value = tree.value;
        boolean valueFromReturn = (value == null);
        ListBuffer<JCStatement> translated = ListBuffer.lb();
        for (JCStatement stmt : tree.getStatements()) {
            switch (stmt.getTag()) {
                case JavafxTag.RETURN:
                    value = ((JCReturn) stmt).getExpression();
                    break;
                case JavafxTag.VAR_DEF:
                    translated.append(translate(stmt));
                    break;
                default:
                    log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "statement");
                    break;
            }
        }

        assert (tree.type != syms.voidType) : "void block expressions should be handled below";
        JCExpression tvalue = translate(value); 
        result = ((JavafxTreeMaker) make).at(tree.pos).BlockExpression(tree.flags, translated.toList(), tvalue);
    }
 
    @Override
    public void visitAssign(JCAssign tree) {
        log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "=");
    }

    @Override
    public void visitAssignop(JCAssignOp tree) {
        log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "=");
    }

    @Override
    public void visitSelect(JCFieldAccess tree) {
        result = toJava.translate(tree, Wrapped.InLocation);  //TODO: not correct, but it will do for now
    }
    
    @Override
    public void visitIdent(JCIdent tree)   {
        result = toJava.translate(tree, Wrapped.InLocation);
    }
    
    @Override
    public void visitSequenceExplicit(JFXSequenceExplicit tree) { //done
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        Type elemType = types.elementType(tree.type);
        UseSequenceBuilder builder = toJava.useSequenceBuilder(tree.pos(), elemType, true);
        stmts.append(builder.makeBuilderVar());
        for (JCExpression item : tree.getItems()) {
            stmts.append(builder.makeAdd( item ) );
        }
        result = makeBlockExpression(tree.pos(), stmts, builder.makeToSequence());
    }
    
    @Override
    public void visitSequenceRange(JFXSequenceRange tree) { //done: except for step and exclusive
        DiagnosticPosition diagPos = tree.pos();
        ListBuffer<JCExpression> args = ListBuffer.lb();
        args.append( translate( tree.getLower() ));
        args.append( translate( tree.getUpper() ));
        //if (tree.getStepOrNull() != null) {
        //    args.append( translate( tree.getStepOrNull() ));
        //}
        result = runtime(diagPos, cBoundSequences, "range", toJava.elementType(tree.type), args);
    }
    
    @Override
    public void visitSequenceEmpty(JFXSequenceEmpty tree) { //done
        if (types.isSequence(tree.type)) {
            Type elemType = types.elementType(tree.type);
            result = runtime(tree.pos(), cBoundSequences, "empty", elemType);
        } else {
            result = make.at(tree.pos).Literal(TypeTags.BOT, null); //NOLOC?
        }
    }
        
    @Override
    public void visitSequenceIndexed(JFXSequenceIndexed tree) {   //done
        result = runtime(tree.pos(), cBoundSequences, "element", tree.type,
                List.of(translate(tree.getSequence()),
                translate(tree.getIndex())));
    }

    @Override
    public void visitSequenceSlice(JFXSequenceSlice tree) {    //done
        DiagnosticPosition diagPos = tree.pos();
        result = runtime(diagPos, cBoundSequences, "slice", tree.type,
                List.of(
                    toJava.makeTypeTree(types.elementType(tree.type), diagPos, true),
                    translate(tree.getSequence()),
                    translate(tree.getFirstIndex()),
                    translate(tree.getLastIndex())
                    ));
        //TODO:   SequenceSliceTree.END_EXCLUSIVE
    }

    /**** utility methods ******/
        
    /**
     * For an attribute "attr" make an access to it via the receiver and getter  
     *      "receiver$.get$attr()"
     * */
   JCExpression makeAttributeAccess(DiagnosticPosition diagPos, String attribName) {
       return callExpression(diagPos, 
                make.Ident(defs.receiverName),
                attributeGetMethodNamePrefix + attribName);
   }
   
    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            Type type) {
        return runtime(diagPos,
                cString,
                methString,
                type,
                List.<JCExpression>nil());
    }

    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            Type type,
            ListBuffer<JCExpression> args) {
        return runtime(diagPos,
                cString,
                methString,
                type,
                args.toList());
    }

    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            List<JCExpression> args) {
        return runtime(diagPos,
                cString,
                methString,
                null,
                args);
    }

    JCExpression runtime(DiagnosticPosition diagPos,
            String cString,
            String methString,
            Type type,
            List<JCExpression> args) {
        JCExpression meth = make.at(diagPos).Select(
                makeQualifiedTree(diagPos, cString),
                names.fromString(methString));
        List<JCExpression> typeArgs = type==null? List.<JCExpression>nil() : List.of(makeTypeTree(type, diagPos, true));
        return make.at(diagPos).Apply(typeArgs, meth, args);
    }

    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method) {
        return callExpression(diagPos, receiver, method, null);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            Name methodName) {
        return callExpression(diagPos, receiver, methodName, null);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
            String method, 
            Object args) {
        return callExpression(diagPos, receiver, names.fromString(method), args);
    }
    
    JCMethodInvocation callExpression(
            DiagnosticPosition diagPos,
            JCExpression receiver, 
             Name methodName, 
            Object args) {
        JCExpression expr = null;
        if (receiver == null) {
            expr = make.at(diagPos).Ident(methodName);
        } else {
            expr = make.at(diagPos).Select(receiver, methodName);
        }
        return make.at(diagPos).Apply(List.<JCExpression>nil(), expr, 
                (args == null)? List.<JCExpression>nil() :
                (args instanceof List)? (List<JCExpression>)args :
                (args instanceof ListBuffer)? ((ListBuffer<JCExpression>)args).toList() :
                (args instanceof JCExpression)?   List.<JCExpression>of((JCExpression)args) :
                    null /* error */
                );    
                    
    }

    private Name getSyntheticName(String kind) {
        return toJava.getSyntheticName(kind);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, Type type, JCExpression value) {
        return makeTmpVar(diagPos, "tmp", type, value);
    }

    private JCVariableDecl makeTmpVar(DiagnosticPosition diagPos, String rootName, Type type, JCExpression value) {
            return toJava.makeTmpVar(diagPos, rootName, type, value);
    }
    
    /** Equivalent to make.at(pos.getStartPosition()) with side effect of caching
     *  pos as make_pos, for use in diagnostics.
     **/
    TreeMaker make_at(DiagnosticPosition pos) {
        return make.at(pos);
    }

    /** Look up a method in a given scope.
     */
    private MethodSymbol lookupMethod(DiagnosticPosition pos, Name name, Type qual, List<Type> args) {
	return rs.resolveInternalMethod(pos, attrEnv, qual, name, args, null);
    }

    /** Look up a constructor.
     */
    private MethodSymbol lookupConstructor(DiagnosticPosition pos, Type qual, List<Type> args) {
	return rs.resolveInternalConstructor(pos, attrEnv, qual, args, null);
    }

    /** Box up a single primitive expression. */
    JCExpression makeBox(DiagnosticPosition diagPos, JCExpression translatedExpr, Type primitiveType) {
	make_at(translatedExpr.pos());
        if (target.boxWithConstructors()) {
            Symbol ctor = lookupConstructor(translatedExpr.pos(),
                                            types.boxedClass(primitiveType).type,
                                            List.<Type>nil()
                                            .prepend(primitiveType));
            return make.Create(ctor, List.of(translatedExpr));
        } else {
            Symbol valueOfSym = lookupMethod(translatedExpr.pos(),
                                             names.valueOf,
                                             types.boxedClass(primitiveType).type,
                                             List.<Type>nil()
                                             .prepend(primitiveType));
//            JCExpression meth =makeIdentifier(valueOfSym.owner.type.toString() + "." + valueOfSym.name.toString());
            JCExpression meth = make.Select(makeTypeTree(valueOfSym.owner.type, diagPos), valueOfSym.name);
            TreeInfo.setSymbol(meth, valueOfSym);
            meth.type = valueOfSym.type;
            return make.App(meth, List.of(translatedExpr));
        }
    }
    
    public JCExpression makeQualifiedTree(DiagnosticPosition diagPos, String str) {
        JCExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = Name.fromString(names, part);
            tree = tree == null? 
                make.at(diagPos).Ident(partName) : 
                make.at(diagPos).Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
    
    JFXBlockExpression makeBlockExpression(DiagnosticPosition diagPos, 
            ListBuffer<JCStatement> stmts, JCExpression value) {
        return ((JavafxTreeMaker)make).at(diagPos).BlockExpression(0, stmts.toList(), value);
    }
    
    /**
     * Build a Java AST representing the return type of a function.
     * Generate the return type as a Location if "isBound" is set.
     * */
    public JCExpression makeReturnTypeTree(DiagnosticPosition diagPos, MethodSymbol mth, boolean isBound) {
        Type returnType = mth.getReturnType();
        if (isBound) {
            VarMorphInfo vmi = typeMorpher.varMorphInfo(mth);
            returnType = vmi.getMorphedType();
        }
        return makeTypeTree(returnType, diagPos);
    }
 
    /**
     * Build a Java AST representing the specified type.
     * Convert JavaFX class references to interface references.
     * */
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos) {
        return makeTypeTree(t, diagPos, true);
    }
    
    /**
     * Build a Java AST representing the specified type.
     * If "makeIntf" is set, convert JavaFX class references to interface references.
     * */
    public JCExpression makeTypeTree(Type t, DiagnosticPosition diagPos, boolean makeIntf) {
        return makeTypeTree(t, diagPos, makeIntf);
    }

   JCExpression castFromObject (JCExpression arg, Type castType) {
        if (castType.isPrimitive())
            castType = types.boxedClass(castType).type;
         return make.TypeCast(makeTypeTree(castType, arg.pos()), arg);
    }

    
    /**
     * assume seq is a sequence of element type U
     * convert   for (x in seq where cond) { body }
     * into the following block expression
     * 
     *   {
     *     SequenceBuilder<T> sb = new SequenceBuilder<T>(clazz);
     *     for (U x : seq) {
     *       if (!cond)
     *         continue;
     *       sb.add( { body } );
     *     }
     *     sb.toSequence()
     *   }
     *
     * **/
    @Override
    public void visitForExpression(JFXForExpression tree) {
        // sub-translation in done inline -- no super.visitForExpression(tree);
            // body has value (non-void)
            assert tree.type != syms.voidType : "should be handled above";
            DiagnosticPosition diagPos = tree.pos();
            ListBuffer<JCStatement> stmts = ListBuffer.lb();
            JCStatement stmt;
            JCExpression value;

            // Compute the element type from the sequence type
            assert tree.type.getTypeArguments().size() == 1;
            Type elemType = types.elementType(tree.type);

            UseSequenceBuilder builder = toJava.useSequenceBuilder(diagPos, elemType, true);
            stmts.append(builder.makeBuilderVar());

            // Build innermost loop body
            stmt = builder.makeAdd( tree.getBodyExpression() );

            // Build the result value
            value = builder.makeToSequence();
            stmt = wrapWithInClause(tree, stmt);
            stmts.append(stmt);

            // Build the block expression -- which is what we translate to
            result = makeBlockExpression(diagPos, stmts, value);
    }

    //where
    private JCStatement wrapWithInClause(JFXForExpression tree, JCStatement coreStmt) {
        JCStatement stmt = coreStmt;
        for (int inx = tree.getInClauses().size() - 1; inx >= 0; --inx) {
            JFXForExpressionInClause clause = (JFXForExpressionInClause)tree.getInClauses().get(inx);
            if (clause.getWhereExpression() != null) {
                stmt = make.at(clause).If( translate( clause.getWhereExpression() ), stmt, null);
            }

            // Build the loop
            //TODO:  below is the simpler version of the loop. Ideally, this should be used in
            // cases where the loop variable does not need to be final.
            /*
            stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    boundTranslate(clause.getVar(), JavafxBindStatus.UNBOUND), 
                    translate(clause.getSequenceExpression()),
                    stmt);
             */
            JFXVar var = clause.getVar();
            Name tmpVarName = getSyntheticName(var.getName().toString());
            JCVariableDecl finalVar = make.VarDef(
                    make.Modifiers(Flags.FINAL), 
                    var.getName(), 
                    makeTypeTree(var.type, var), 
                    make.Ident(tmpVarName));
            Name tmpIndexVarName;
            if (clause.getIndexUsed()) {
                Name indexVarName = indexVarName(clause);
                tmpIndexVarName = getSyntheticName(indexVarName.toString());
                JCVariableDecl finalIndexVar = make.VarDef(
                    make.Modifiers(Flags.FINAL),
                    indexVarName, 
                    makeTypeTree(syms.javafx_IntegerType, var),
                    make.Unary(JCTree.POSTINC, make.Ident(tmpIndexVarName)));
                stmt = make.Block(0L, List.of(finalIndexVar, finalVar, stmt));
            }                
            else {
                tmpIndexVarName = null;
                stmt = make.Block(0L, List.of(finalVar, stmt));
            }
    
            DiagnosticPosition diagPos = clause.seqExpr;
            if (types.isSequence(clause.seqExpr.type)) {
                // It would be more efficient to move the Iterable.iterator call
                // to a static method, which can also check for null.
                // But that requires expanding the ForeachLoop by hand.  Later.
                JCExpression seq = callExpression(diagPos,
                    makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                    "forceNonNull",
                    List.of(make.at(diagPos).Select(makeTypeTree(syms.boxIfNeeded(var.type), diagPos, true), names._class),
                        translate(clause.seqExpr)));
                stmt = make.at(clause).ForeachLoop(
                    // loop variable is synthetic should not be bound
                    // even if we are in a bind context
                    make.VarDef(
                        make.Modifiers(0L), 
                        tmpVarName, 
                        makeTypeTree(var.type, var, true), 
                        null),
                    seq,
                    stmt);
            } else {
                // The "sequence" isn't a Sequence.
                // Compile: { var tmp = seq; if (tmp!=null) stmt; }
                if (! var.type.isPrimitive())
                    stmt = make.If(make.Binary(JCTree.NE, make.Ident(tmpVarName),
                                make.Literal(TypeTags.BOT, null)),
                            stmt, null);
                stmt = make.at(diagPos).Block(0,
                    List.of(make.at(diagPos).VarDef(
                        make.Modifiers(0L), 
                        tmpVarName, 
                        makeTypeTree(var.type, var, true), 
                        translate(clause.seqExpr)),
                        stmt));
            }
            if (clause.getIndexUsed()) {
                JCVariableDecl tmpIndexVar =
                        make.VarDef(
                        make.Modifiers(0L), 
                        tmpIndexVarName, 
                        makeTypeTree(syms.javafx_IntegerType, var), 
                        make.Literal(Integer.valueOf(0)));
                stmt = make.Block(0L, List.of(tmpIndexVar, stmt));
            }
        }
        return stmt;
    }
    
    public Name indexVarName (JFXForExpressionInClause clause) {
        Name forVar = clause.getVar().getName();
        return names.fromString("$indexof$"+forVar.toString());
    }
    
    public void visitIndexof(JFXIndexof that) {
        result = make.at(that.pos()).Ident(indexVarName(that.clause));
    }

    @Override
    public void visitConditional(JCConditional tree) {
        final DiagnosticPosition diagPos = tree.pos();
        JCExpression cond = translate(tree.getCondition());
        JCExpression trueSide = tree.getTrueExpression();
        JCExpression falseSide = tree.getFalseExpression();
            JCExpression translatedFalseSide;
            if (falseSide == null) {
                Type trueSideType = tree.getTrueExpression().type;
                translatedFalseSide = toJava.makeDefaultValue(diagPos, typeMorpher.typeMorphInfo(trueSideType));
            } else {
                translatedFalseSide = translate(falseSide);
            }
            result = make.at(diagPos).Conditional(cond, translate(trueSide), translatedFalseSide);
    }

    @Override
    public void visitErroneous(JCErroneous tree) {
        List<? extends JCTree> errs = translate(tree.errs);
        result = make.at(tree.pos).Erroneous(errs);
    }
   
    @Override
    public void visitParens(JCParens tree) { //done
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).Parens(expr);
    }

    @Override
    public void visitTypeTest(JCInstanceOf tree) {
        JCTree clazz = this.makeTypeTree(tree.clazz.type, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeTest(expr, clazz);
    }

    @Override
    public void visitTypeCast(JCTypeCast tree) {
        Type clazztype = tree.clazz.type;
        if (clazztype.isPrimitive() && ! tree.expr.type.isPrimitive())
            clazztype = types.boxedClass(clazztype).type;
        JCTree clazz = this.makeTypeTree(clazztype, tree);
        JCExpression expr = translate(tree.expr);
        result = make.at(tree.pos).TypeCast(clazz, expr);
    }
    
    @Override
    public void visitLiteral(JCLiteral tree) {
        JCExpression unbound;
        if (tree.typetag == TypeTags.BOT && types.isSequence(tree.type)) {
            Type elemType = types.elementType(tree.type);
            unbound = toJava.makeEmptySequenceCreator(tree.pos(), elemType);
        } else {
            unbound = make.at(tree.pos).Literal(tree.typetag, tree.value);
        }
        result = makeUnboundLocation(tree.pos(), tree.type, unbound);
    }

    @Override
    public void visitApply(final JCMethodInvocation tree) {
        /*********************
        result = (new Translator() {

            private final DiagnosticPosition diagPos = tree.pos();
            private final JCExpression meth = tree.meth;
            private final JCFieldAccess fieldAccess = meth.getTag() == JavafxTag.SELECT?   
                                                                    (JCFieldAccess) meth :  
                                                                    null;
            private final JCExpression selector = fieldAccess != null? fieldAccess.getExpression() : null;
            private final Name name = fieldAccess != null? fieldAccess.name : null;
            private final Symbol sym = expressionSymbol(meth);
            private final MethodSymbol msym = (sym instanceof MethodSymbol)? (MethodSymbol)sym : null;
            private final Name selectorIdName = (selector != null && selector.getTag() == JavafxTag.IDENT)? ((JCIdent) selector).getName() : null;
            private final boolean thisCall = selectorIdName == names._this;
            private final boolean superCall = selectorIdName == names._super;
            private final boolean namedSuperCall =
                    selector != null && msym!= null && ! msym.isStatic() &&
                    expressionSymbol(selector) instanceof ClassSymbol &&
                    // FIXME should also allow other enclosing classes:
                    types.isSuperType(expressionSymbol(selector).type, currentClass.sym);
            private final boolean renameToSuper = namedSuperCall && ! types.isCompoundClass(currentClass.sym);
            private final boolean superToStatic = (superCall || namedSuperCall) && ! renameToSuper;
            private final List<Type> formals = meth.type.getParameterTypes();

            private final boolean useInvoke = meth.type instanceof FunctionType;
                                                      // The below was part of this test, but it doesn't make much sense,
                                                      // it was blocking other work and it is never used in runtime or tests
                                                      //              || (transMeth instanceof JCIdent
                                                      //                           && ((JCIdent) transMeth).sym instanceof MethodSymbol 
                                                      //                           && isInnerFunction((MethodSymbol) ((JCIdent) transMeth).sym)));

            private final boolean testForNull =  generateNullChecks && msym!=null  &&
                    !sym.isStatic() && selector!=null && !superCall && !namedSuperCall &&
                    !thisCall && !useInvoke && !selector.type.isPrimitive() && !renameToSuper;
            private final boolean hasSideEffects = testForNull && hasSideEffects(selector);
            private final boolean callBound = generateBoundFunctions && msym != null
                    && types.isJFXClass(msym.owner)
                    && (generateBoundVoidFunctions || msym.getReturnType() != syms.voidType )
                    && !useInvoke;

            public JCTree doit() {
                JCVariableDecl selectorVar = null;
                JCExpression transMeth;
                if (renameToSuper) {
                    transMeth = make.at(selector).Select(make.Select(makeTypeTree(currentClass.sym.type, selector, false), names._super), sym);
                } else {
                    transMeth = translate(meth);
                    if (hasSideEffects && transMeth.getTag() == JavafxTag.SELECT) {
                        // still a select and presumed to still have side effects -- hold the selector in a temp var
                        JCFieldAccess transMethFA = (JCFieldAccess) transMeth;
                        selectorVar = makeTmpVar(diagPos, "select", selector.type, transMethFA.getExpression());
                        transMeth = make.at(diagPos).Select(make.at(diagPos).Ident(selectorVar.name), transMethFA.name);
                    }  
                }

                // translate the method name -- e.g., foo  to foo$bound or foo$impl
                if (superToStatic || (callBound && ! renameToSuper)) {
                    Name name = functionName(msym, superToStatic, callBound);
                    if (transMeth.getTag() == JavafxTag.IDENT) {
                        transMeth = make.at(diagPos).Ident(name);
                    } else if (transMeth.getTag() == JavafxTag.SELECT) {
                        JCFieldAccess faccess = (JCFieldAccess) transMeth;
                        transMeth = make.at(diagPos).Select(faccess.getExpression(), name);
                    }
                }
                if (useInvoke) {
                    transMeth = make.Select(transMeth, defs.invokeName);
                }

                JCMethodInvocation app = make.at(diagPos).Apply( translate(tree.typeargs), transMeth, determineArgs());
                JCExpression fresult = app;
                if (callBound) {
                    fresult = makeBoundCall(app);
                }
                if (useInvoke) {
                    if (tree.type.tag != TypeTags.VOID) {
                        fresult = castFromObject(fresult, tree.type);
                    }
                } else if (transMeth instanceof JFXBlockExpression) {
                    // If visitSelect translated exp.staticMember to
                    // { exp; class.staticMember }:
                    JFXBlockExpression block = (JFXBlockExpression) transMeth;
                    app.meth = block.value;
                    block.value = app;
                    fresult = block;
                }
                // If we are to yield a Location, and this isn't going to happen as
                // a return of using a bound call (for example, if this is a Java call)
                // then convert into a Location
                if (!callBound && msym != null) {
                    TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(msym.getReturnType());
                    fresult = toJava.makeUnboundLocation(diagPos, returnTypeInfo, fresult);
                }
                if (testForNull) {
                    JCExpression toTest = hasSideEffects? 
                                             make.at(diagPos).Ident(selectorVar.name) :
                                             translate(selector);
                    // we have a testable guard for null, test before the invoke (boxed conversions don't need a test)
                    JCExpression cond = make.at(diagPos).Binary(JCTree.NE, toTest, make.Literal(TypeTags.BOT, null));
                    {
                        // it has a non-void return type, convert it to a conditional expression
                        // if it would dereference null, then instead give the default value
                        TypeMorphInfo returnTypeInfo = typeMorpher.typeMorphInfo(msym.getReturnType());
                        JCExpression defaultExpr = toJava.makeDefaultValue(diagPos, returnTypeInfo);
                        defaultExpr = toJava.makeUnboundLocation(diagPos, returnTypeInfo, defaultExpr);
                        fresult =  make.at(diagPos).Conditional(cond, fresult, defaultExpr);
                        if (hasSideEffects) {
                            // if the selector has side-effects, we created a temp var to hold it
                            // so we need to make a block-expression to include the temp var
                            fresult = make.at(diagPos).BlockExpression(0L, List.<JCStatement>of(selectorVar), fresult);
                        }
                    }
                }
                return fresult;
            }

            // compute the translated arguments.
            // if this is a bound call, use left-hand side references for arguments consisting
            // solely of a  var or attribute reference, or function call, otherwise, wrap it
            // in an expression location
            List<JCExpression> determineArgs() {
                List<JCExpression> args;
                if (callBound) {
                    ListBuffer<JCExpression> targs = ListBuffer.lb();
                    List<Type> formal = formals;
                    for (JCExpression arg : tree.args) {
                        switch (arg.getTag()) {
                            case JavafxTag.IDENT:
                            case JavafxTag.SELECT:
                            case JavafxTag.APPLY:
                                // This arg expression is one that will translate into a Location;
                                // since that is needed for a this into Location, do so.
                                // However, if the types need to by changed (subclass), this won't
                                // directly work.
                                // Also, if this is a mismatched sequence type, we will need
                                // to do some different
                                if (arg.type.equals(formal.head) || 
                                        types.isSequence(formal.head)) {
                                    targs.append(translate(arg));
                                    break;
                                }
                                //TODO: handle sequence subclasses
                                //TODO: use more efficient mechanism (use currently apears rare)
                                //System.err.println("Not match: " + arg.type + " vs " + formal.head);
                                // Otherwise, fall-through, presumably a conversion will work.
                            default:
                                {
                                    ListBuffer<JCTree> prevBindingExpressionDefs = bindingExpressionDefs;
                                    bindingExpressionDefs = ListBuffer.lb();
                                    targs.append(toJava.makeBoundLocation(
                                                                    arg.pos(), 
                                                                    typeMorpher.typeMorphInfo(formal.head),
                                                                    toJava.translateExpressionToStatement(arg, arg.type), 
                                                                    false, 
                                                                    typeMorpher.buildDependencies(arg)));
                                    assert bindingExpressionDefs == null || bindingExpressionDefs.size() == 0 : "non-empty defs lost";
                                    bindingExpressionDefs = prevBindingExpressionDefs;
                                    break;
                                }
                        }
                        formal = formal.tail;
                    }
                    args = targs.toList();
                } else {
                    boolean varargs = tree.args != null && msym != null &&
                            (msym.flags() & VARARGS) != 0 &&
                            (formals.size() != tree.args.size() ||
                             types.isConvertible(tree.args.last().type,
                                 types.elemtype(formals.last())));
                    ListBuffer<JCExpression> translated = ListBuffer.lb();
                    boolean handlingVarargs = false;
                    Type formal = null;
                    List<Type> t = formals;
	            for (List<JCExpression> l = tree.args; l.nonEmpty();  l = l.tail) {
                        if (! handlingVarargs) {
                            formal = t.head;
                            t = t.tail;
                            if (varargs && t.isEmpty()) {
                                formal = types.elemtype(formal);
                                handlingVarargs = true;
                            }
                        }
                        JCExpression tree = translate(l.head, formal);
                        if (tree != null) {
                            translated.append( tree );
                        }
                    }
	            args = translated.toList();
                }

               // if this is a super.foo(x) call, "super" will be translated to referenced class,
                // so we add a receiver arg to make a direct call to the implementing method  MyClass.foo(receiver$, x)
                if (superToStatic) {
                    args = args.prepend(make.Ident(defs.receiverName));
                }

                return args;
            }

            // now, generate the bound function call.  The bound function should be called no
            // more than once (per context), but it must be called in place, because of local
            // variables, so lazily set the functions bound location, which is stored as a field
            // on the binding expression.  Code looks like this:
            //     (loc == null)? addDependent( loc = boundCall ) : loc
            // if this is a right-hand side context, call get() on that to get the value.
            JCExpression makeBoundCall(JCExpression applyExpression) {
                Name tmpName = getSyntheticName("loc");
                JCExpression cond = make.at(diagPos).Binary(JCTree.EQ, make.at(diagPos).Ident(tmpName), make.Literal(TypeTags.BOT, null));
                JCExpression initLocation = callExpression(diagPos, 
                                                                    null,
                                                                    defs.addStaticDependentName,
                                                                    make.at(diagPos).Assign(make.at(diagPos).Ident(tmpName), applyExpression));
                JavafxTypeMorpher.TypeMorphInfo tmi = typeMorpher.typeMorphInfo(msym.getReturnType());
                Type morphedReturnType = tmi.getMorphedType();
                bindingExpressionDefs.append(make.VarDef(
                                                                    make.Modifiers(Flags.PRIVATE),
                                                                    tmpName,
                                                                    makeTypeTree(morphedReturnType, diagPos),
                                                                    make.Literal(TypeTags.BOT, null)));
                return make.at(diagPos).Conditional(cond, 
                                                                    initLocation, 
                                                                    make.at(diagPos).Ident(tmpName));
            }

        }).doit();
         * ******/
    }

    @Override
    public void visitBinary(final JCBinary tree) {
        DiagnosticPosition diagPos = tree.pos();
        final JCExpression l = tree.lhs;
        final JCExpression r = tree.rhs;
        final JCExpression lhs = translate(l);
        final JCExpression rhs = translate(r);
        final String typeCode = typeCode(l.type) + typeCode(r.type);

        switch (tree.getTag()) {
            case JavafxTag.PLUS:
                result = runtime(diagPos, cBoundSequences, "plus_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.MINUS:
                result = runtime(diagPos, cBoundSequences, "minus_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.DIV:
                result = runtime(diagPos, cBoundSequences, "times_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.MUL:
                result = runtime(diagPos, cBoundSequences, "divide_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.EQ:
                result = runtime(diagPos, cBoundSequences, "eq_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.NE:
                result = runtime(diagPos, cBoundSequences, "ne_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.LT:
                result = runtime(diagPos, cBoundSequences, "lt_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.LE:
                result = runtime(diagPos, cBoundSequences, "le_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.GT:
                result = runtime(diagPos, cBoundSequences, "gt_" + typeCode, List.of(lhs, rhs));
                break;
            case JavafxTag.GE:
                result = runtime(diagPos, cBoundSequences, "ge_" + typeCode, List.of(lhs, rhs));
                break;
            default:
                assert false : "unhandled binary operator";
                result = lhs;
                break;
        }
    }

    @Override
    public void visitUnary(final JCUnary tree) {
        DiagnosticPosition diagPos = tree.pos();
        JCExpression expr = tree.getExpression();
        JCExpression transExpr = translate(expr);
        String typeCode = typeCode(expr.type);

        switch (tree.getTag()) {
            case JavafxTag.SIZEOF:
                result = callExpression(diagPos,
                        makeQualifiedTree(diagPos, "com.sun.javafx.runtime.sequence.Sequences"),
                        defs.sizeMethodName, transExpr);
                break;
            case JavafxTag.REVERSE:
                result = runtime(diagPos, cBoundSequences, "reverse", types.elementType(expr.type), List.of(transExpr) );
                break;
            case JCTree.NOT:
                result = runtime(diagPos, cBoundSequences, "not_"+typeCode, List.of(transExpr) );
                break;
            case JCTree.NEG:
                if (types.isSameType(tree.type, syms.javafx_TimeType)) {   //TODO
                    result = make.at(diagPos).Apply(null,
                            make.at(diagPos).Select(translate(tree.arg), Name.fromString(names, "negate")), List.<JCExpression>nil());
                } else {
                    result = runtime(diagPos, cBoundSequences, "negate_"+typeCode, List.of(transExpr));
                }
                break;
            case JCTree.PREINC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "++");
                result = transExpr;
                break;
            case JCTree.PREDEC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "--");
                result = transExpr;
                break;
            case JCTree.POSTINC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "++");
                result = transExpr;
                break;
            case JCTree.POSTDEC:
                log.error(tree.pos(), "javafx.not.allowed.in.bind.context", "--");
                result = transExpr;
                break;
            default:
                assert false : "unhandled unary operator";
                result = transExpr;
                break;
        }
    }
    
    public void visitTimeLiteral(JFXTimeLiteral tree) {
        // convert time literal to a javafx.lang.Time object literal
        JCFieldAccess clsname = (JCFieldAccess) makeQualifiedTree(tree.pos(), syms.javafx_TimeType.tsym.toString());
        clsname.type = syms.javafx_TimeType;
        clsname.sym = syms.javafx_TimeType.tsym;
        Name attribute = names.fromString("millis");
        Symbol symMillis = clsname.sym.members().lookup(attribute).sym;
        JavafxTreeMaker fxmake = (JavafxTreeMaker)make;
        JFXObjectLiteralPart objLiteral = fxmake.at(tree.pos()).ObjectLiteralPart(attribute, tree.value, JavafxBindStatus.UNBOUND);
        objLiteral.sym = symMillis;
        JFXInstanciate inst = fxmake.at(tree.pos).Instanciate(clsname, null, List.of((JCTree)objLiteral));
        inst.type = clsname.type;
        
        // now convert that object literal to Java
        visitInstanciate(inst); // sets result
    }

    @Override
    public void visitObjectLiteralPart(JFXObjectLiteralPart that) {
        that.expr = translate(that.expr);
        result = that;
    }  
        
    /***********************************************************************
     *
     * Utilities 
     *
     */
    
    private String typeCode(Type type) {
        Symbol tsym = type.tsym;
                if (type.isPrimitive()) {
                    if (tsym == syms.doubleType.tsym
                            || tsym == syms.floatType.tsym) {
                        return "d";
                    } else if (tsym == syms.intType.tsym
                            || tsym == syms.byteType.tsym
                            || tsym == syms.charType.tsym
                            || tsym == syms.longType.tsym
                            || tsym == syms.shortType.tsym) {
                        return "i";
                    } else if (tsym == syms.booleanType.tsym) {
                        return "b";
                    } else {
                        assert false : "should not reach here";
                        return "X";
                    }
                } else {
                    if (types.isSequence(type) ) {
                        return "";  //TODO: ?
                    } else if (tsym == this.booleanObjectTypeSymbol) {
                        return "B";
                    } else if (tsym == this.doubleObjectTypeSymbol) {
                        return "D";
                    } else if (tsym == this.intObjectTypeSymbol) {
                        return "I";
                    } else {
                        return "";
                    }
                }
    }
    
    Symbol expressionSymbol(JCExpression tree) {
        switch (tree.getTag()) {
            case JavafxTag.IDENT:
                return ((JCIdent) tree).sym;
            case JavafxTag.SELECT:
                return ((JCFieldAccess) tree).sym;
            default:
                return null;
        }
    }
    
    JCExpression makeUnboundLocation(DiagnosticPosition diagPos, Type type, JCExpression expr) {
        return toJava.makeUnboundLocation(diagPos, typeMorpher.typeMorphInfo(type), expr);
    }

    /**
     * Call Variable "make" to create a bound expression.
     * Use "stmt" which is the translation of the expression into
     * a statement.  The Variable is created with an anonymous binding expression
     * instance and the static dependencies.
     */
    JCExpression makeBoundLocation(DiagnosticPosition diagPos,
            TypeMorphInfo tmi,
            JCStatement stmt,
            boolean isLazy,
            List<JCExpression> staticDependencies) {

        return toJava.makeBoundLocation(diagPos,
            tmi,
            stmt,
            isLazy,
            staticDependencies);
    }

    JCExpression makeIdentifier(String s) {
        return ((JavafxTreeMaker)make).Identifier(s);
    }
    
    JCExpression makeIdentifier(Name name) {
        return ((JavafxTreeMaker)make).Identifier(name);
    }
    
    protected void prettyPrint(JCTree node) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        JavafxPretty pretty = new JavafxPretty(osw, false);
        try {
            pretty.printExpr(node);
            osw.flush();
        } catch (Exception ex) {
            System.err.println("Error in pretty-printing: " + ex);
        }
    }
    
    Name functionName(MethodSymbol sym) {
        return functionName(sym, false);
    }

    Name functionName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, false, isBound);
    }

    Name functionInterfaceName(MethodSymbol sym, boolean isBound) {
        return functionName(sym, isBound);
    }

    Name functionName(MethodSymbol sym, boolean markAsImpl, boolean isBound) {
        if (!markAsImpl && !isBound) {
            return sym.name;
        }
        return toJava.functionName( sym, sym.name.toString(), markAsImpl, isBound );
    }

    /***********************************************************************
     *
     * Moot visitors
     *
     */
    
    public void visitInterpolateExpression(JFXInterpolateExpression tree) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visitInterpolateValue(JFXInterpolateValue tree) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visitForExpressionInClause(JFXForExpressionInClause that) {
        assert false : "should be processed by parent tree";
    }
    
    @Override
    public void visitModifiers(JCModifiers tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitNewArray(JCNewArray tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitNewClass(JCNewClass tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitSkip(JCSkip tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSwitch(JCSwitch tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitSynchronized(JCSynchronized tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitThrow(JCThrow tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTry(JCTry tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitWhileLoop(JCWhileLoop tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitLetExpr(LetExpr tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTree(JCTree tree) {
        assert false : "should not be in JavaFX AST";
    }
    
    @Override
    public void visitDoLater(JFXDoLater that) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitMemberSelector(JFXMemberSelector that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitOverrideAttribute(JFXOverrideAttribute tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitOnReplace(JFXOnReplace tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitOnReplaceElement(JFXOnReplaceElement tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitOnInsertElement(JFXOnInsertElement tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitOnDeleteElement(JFXOnDeleteElement tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitOnDeleteAll(JFXOnDeleteAll tree) {
        assert false : "not yet implemented -- may not be";
    }

    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
        assert false : "should not be processed as part of a binding";
   }
    
    @Override
    public void visitClassDeclaration(JFXClassDeclaration tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitInitDefinition(JFXInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }

    public void visitPostInitDefinition(JFXPostInitDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }
    
   @Override
    public void visitOperationDefinition(JFXFunctionDefinition tree) {
        assert false : "should not be processed as part of a binding";
    }
   
    public void visitBindExpression(JFXBindExpression tree) {
         throw new AssertionError(tree);
    }

    @Override
    public void visitBlock(JCBlock tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSequenceInsert(JFXSequenceInsert tree) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitSequenceDelete(JFXSequenceDelete tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitContinue(JCContinue tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitDoLoop(JCDoWhileLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitReturn(JCReturn tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitExec(JCExpressionStatement tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitForeachLoop(JCEnhancedForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitForLoop(JCForLoop tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitIf(JCIf tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitImport(JCImport tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitIndexed(JCArrayAccess tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeArray(JCArrayTypeTree tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitLabelled(JCLabeledStatement tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
         assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitTypeApply(JCTypeApply tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitTypeParameter(JCTypeParameter tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitAnnotation(JCAnnotation tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitAssert(JCAssert tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitBreak(JCBreak tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
   public void visitCase(JCCase tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitCatch(JCCatch tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        assert false : "should not be in JavaFX AST";
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
        assert false : "should not be processed as part of a binding";
    }

    @Override
    public void visitSetAttributeToObjectBeingInitialized(JFXSetAttributeToObjectBeingInitialized that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeAny(JFXTypeAny that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeClass(JFXTypeClass that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeFunctional(JFXTypeFunctional that) {
        assert false : "should not be processed as part of a binding";
    }
    
    @Override
    public void visitTypeUnknown(JFXTypeUnknown that) {
        assert false : "should not be processed as part of a binding";
    }

}
