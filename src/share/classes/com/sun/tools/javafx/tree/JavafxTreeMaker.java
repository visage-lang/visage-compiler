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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.TimeLiteralTree.Duration;
import com.sun.javafx.api.tree.TypeTree.Cardinality;
import com.sun.source.tree.LiteralTree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

/* JavaFX version of tree maker
 */
public class JavafxTreeMaker extends TreeMaker implements JavafxTreeFactory {
     /** Get the JavafxTreeMaker instance.
     */
    public static JavafxTreeMaker instance(Context context) {
        JavafxTreeMaker instance = (JavafxTreeMaker) context.get(treeMakerKey);
        if (instance == null)
            instance = new JavafxTreeMaker(context);
        return instance;
    }
    
    public static void preRegister(final Context context) {
        context.put(treeMakerKey, new Context.Factory<TreeMaker>() {
            @Override
            public TreeMaker make() {
                return new JavafxTreeMaker(context);
            }
        });
    }
    
    /** Create a tree maker with null toplevel and NOPOS as initial position.
     */
    protected JavafxTreeMaker(Context context) {
        this(null,
                Name.Table.instance(context),
                Types.instance(context),
                Symtab.instance(context)
                );
        context.put(treeMakerKey, this);
        this.pos = Position.NOPOS;
    }
    
    /** Create a tree maker with a given toplevel and FIRSTPOS as initial position.
     */
    JavafxTreeMaker(JCCompilationUnit toplevel, Name.Table names, Types types, Symtab syms) {
        super(toplevel, names, types, syms);
    }
    
    /** Create a new tree maker for a given toplevel.
     */
    @Override
    public JavafxTreeMaker forToplevel(JCCompilationUnit toplevel) {
        return new JavafxTreeMaker(toplevel, names, types, syms);
    }

    /** Reassign current position.
     */
    @Override
    public JavafxTreeMaker at(int pos) {
        this.pos = pos;
        return this;
    }
    
    /** Reassign current position.
     */
    @Override
    public JavafxTreeMaker at(DiagnosticPosition pos) {
        this.pos = (pos == null ? Position.NOPOS : pos.getStartPosition());
        return this;
    }
    
    public JFXClassDeclaration ClassDeclaration(JCModifiers mods,
            Name name,
            List<JCExpression> supertypes,
            List<JCTree> declarations) {
        JFXClassDeclaration tree = new JFXClassDeclaration(mods,
                name,
                supertypes,
                declarations,
                null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXBindExpression BindExpression(JCExpression expr, JavafxBindStatus bindStatus) {
        if (bindStatus == null)
            bindStatus = JavafxBindStatus.UNBOUND;
        JFXBindExpression tree = new JFXBindExpression(expr, bindStatus);
        tree.pos = pos;
        return tree;
    }

    public JFXBlockExpression BlockExpression(long flags, List<JCStatement> stats, JCExpression value) {
        JFXBlockExpression tree = new JFXBlockExpression(flags, stats, value);
        tree.pos = pos;
        return tree;
    }
    
    public JFXFunctionDefinition OperationDefinition(
            JCModifiers modifiers,
            Name name,
            JFXType restype,
            List<JFXVar> params, 
            JFXBlockExpression bodyExpression) {
        JFXFunctionDefinition tree = new JFXFunctionDefinition(
                modifiers,
                name,
                restype,
                params,
                bodyExpression);
        tree.operation.definition = tree;
        tree.pos = pos;
        return tree;
    }

    public JFXFunctionValue OperationValue(
            JFXType restype,
             List<JFXVar> params, 
            JFXBlockExpression bodyExpression) {
        JFXFunctionValue tree = new JFXFunctionValue(
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }

    public JFXInitDefinition InitDefinition(
            JCBlock body) {
        JFXInitDefinition tree = new JFXInitDefinition(
                body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXPostInitDefinition PostInitDefinition(JCBlock body) {
        JFXPostInitDefinition tree = new JFXPostInitDefinition(body);
        tree.pos = pos;
        return tree;
    }

    public JFXDoLater  DoLater(JCBlock body) {
        JFXDoLater tree = new JFXDoLater(body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXMemberSelector  MemberSelector(Name className,
            Name name) {
        JFXMemberSelector tree = new JFXMemberSelector(className, name);
        tree.pos = pos;
        return tree;
    }
    
    public JFXSequenceEmpty EmptySequence() {
        JFXSequenceEmpty tree = new JFXSequenceEmpty();
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceRange RangeSequence(JCExpression lower, JCExpression upper, JCExpression stepOrNull, boolean exclusive) {
        JFXSequenceRange tree = new JFXSequenceRange(lower, upper,  stepOrNull,  exclusive);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceExplicit ExplicitSequence(List<JCExpression> items) {
        JFXSequenceExplicit tree = new JFXSequenceExplicit(items);
        tree.pos = pos;
        return tree;
    }
    
    public JFXSequenceIndexed SequenceIndexed(JCExpression sequence, JCExpression index) {
        JFXSequenceIndexed tree = new JFXSequenceIndexed(sequence, index);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceSlice SequenceSlice(JCExpression sequence, JCExpression firstIndex, JCExpression lastIndex, int endKind) {
        JFXSequenceSlice tree = new JFXSequenceSlice(sequence, firstIndex,
                lastIndex, endKind);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceInsert SequenceInsert(JCExpression sequence, JCExpression element, JCExpression position, boolean after) {
        JFXSequenceInsert tree = new JFXSequenceInsert(sequence, element, position, after);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceDelete SequenceDelete(JCExpression sequence) {
        JFXSequenceDelete tree = new JFXSequenceDelete(sequence, null);
        tree.pos = pos;
        return tree;
    }

    public JFXSequenceDelete SequenceDelete(JCExpression sequence, JCExpression element) {
        JFXSequenceDelete tree = new JFXSequenceDelete(sequence, element);
        tree.pos = pos;
        return tree;
    }

    public JFXStringExpression StringExpression(List<JCExpression> parts,
                                        String translationKey) {
        JFXStringExpression tree = new JFXStringExpression(parts, translationKey);
        tree.pos = pos;
        return tree;
    }
    
    public JFXInstanciate Instanciate(JCExpression ident,
            List<JCExpression> args,
            List<JCTree> defs) {
        ListBuffer<JFXObjectLiteralPart> partsBuffer = ListBuffer.lb();
        ListBuffer<JCTree> defsBuffer = ListBuffer.lb();
        ListBuffer<JFXVar> varsBuffer = ListBuffer.lb();
        if (defs != null) {
            for (JCTree def : defs) {
                if (def instanceof JFXObjectLiteralPart) {
                    partsBuffer.append((JFXObjectLiteralPart) def);
                } else if (def instanceof JFXVar && ((JFXVar)def).isLocal()) {
                    varsBuffer.append((JFXVar) def);
                } else {
                    defsBuffer.append(def);
                }
            }
        }
        JFXClassDeclaration klass = null;
        if (defsBuffer.size() > 0) {
            JCExpression id = ident;
            while (id instanceof JCFieldAccess) id = ((JCFieldAccess)id).getExpression();
            Name cname = syntheticClassName(((JCIdent)id).getName());
            long innerClassFlags = Flags.SYNTHETIC | Flags.FINAL; // to enable, change to Flags.FINAL
            klass = this.ClassDeclaration(this.Modifiers(innerClassFlags), cname, List.<JCExpression>of(ident), defsBuffer.toList());
        }
        
        JFXInstanciate tree = new JFXInstanciate(ident, 
                klass, 
                args==null? List.<JCExpression>nil() : args, 
                partsBuffer.toList(), 
                varsBuffer.toList(), 
                null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXSetAttributeToObjectBeingInitialized SetAttributeToObjectBeingInitialized(Name name) {
        JFXSetAttributeToObjectBeingInitialized tree = new JFXSetAttributeToObjectBeingInitialized(name, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXObjectLiteralPart ObjectLiteralPart(
            Name attrName,
            JCExpression expr,
            JavafxBindStatus bindStatus) {
        JFXObjectLiteralPart tree = new JFXObjectLiteralPart(attrName, expr,
                bindStatus, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeAny(Cardinality cardinality) {
        JFXType tree = new JFXTypeAny(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeUnknown() {
        JFXType tree = new JFXTypeUnknown();
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeClass(JCExpression className,Cardinality cardinality) {
        JFXType tree = new JFXTypeClass(className, cardinality, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType TypeFunctional(List<JFXType> params,
            JFXType restype,
            Cardinality cardinality) {
        JFXType tree = new JFXTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }
    
    
    public JFXOverrideAttribute TriggerWrapper(JCIdent expr, JFXOnReplace onr) {
        JFXOverrideAttribute tree = new JFXOverrideAttribute(expr, null, null, onr, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOnReplace OnReplace(JFXVar oldValue, JCBlock body) {
        JFXOnReplace tree = new JFXOnReplace(oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
     public JFXOnReplace OnReplace(JFXVar oldValue, JFXVar firstIndex,
             JFXVar lastIndex, JFXVar newElements, JCBlock body) {
         return OnReplace(oldValue, firstIndex, lastIndex,
                 JFXSequenceSlice.END_INCLUSIVE, newElements, body);
    }

     public JFXOnReplace OnReplace(JFXVar oldValue, JFXVar firstIndex,
             JFXVar lastIndex, int endKind, JFXVar newElements, JCBlock body) {
         JFXOnReplace tree = new JFXOnReplace(oldValue, firstIndex, lastIndex,
                 endKind, newElements, body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOnReplaceElement OnReplaceElement(JFXVar index, JFXVar oldValue, JCBlock body) {
         JFXOnReplaceElement tree = new JFXOnReplaceElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOnInsertElement OnInsertElement(JFXVar index, JFXVar oldValue, JCBlock body) {
         JFXOnInsertElement tree = new JFXOnInsertElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOnDeleteElement OnDeleteElement(JFXVar index, JFXVar oldValue, JCBlock body) {
         JFXOnDeleteElement tree = new JFXOnDeleteElement(index, oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOnDeleteAll OnDeleteAll(JFXVar oldValue, JCBlock body) {
         JFXOnDeleteAll tree = new JFXOnDeleteAll(oldValue, body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVar Var(Name name,
            JFXType type,
            JCModifiers mods,
            boolean isLocal,
            JCExpression initializer,
            JavafxBindStatus bindStatus,
            List<JFXAbstractOnChange> onChanges) {
        JFXVar tree = new JFXVar(name, type, 
                mods, isLocal, initializer, bindStatus, onChanges, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOverrideAttribute OverrideAttribute(JCIdent expr, 
            JCExpression initializer,
            JavafxBindStatus bindStatus,
            JFXOnReplace onr) {
        JFXOverrideAttribute tree = new JFXOverrideAttribute(expr, initializer, 
                bindStatus, onr, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVar Param(Name name,
            JFXType type) {
        JFXVar tree = new JFXVar(name, type, 
                Modifiers(Flags.PARAMETER), true, null, JavafxBindStatus.UNBOUND, 
                List.<JFXAbstractOnChange>nil(), null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXForExpression ForExpression(
            List<JFXForExpressionInClause> inClauses,
            JCExpression bodyExpr) {
        JFXForExpression tree = new JFXForExpression(inClauses, bodyExpr);       
        tree.pos = pos;
        return tree;
    }
    
    public JFXForExpressionInClause InClause(
            JFXVar var, 
            JCExpression seqExpr,
            JCExpression whereExpr) {
        JFXForExpressionInClause tree = new JFXForExpressionInClause(var, seqExpr, whereExpr);       
        tree.pos = pos;
        return tree;
    }
    
    public JCExpression Identifier(Name name) {
        String str = name.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return Ident(name);
        }
        return Identifier(str);
    }
    
    public JCExpression Identifier(String str) {
        assert str.indexOf('<') < 0 : "attempt to parse a type with 'Identifier'.  Use TypeTree";
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
                Ident(partName) : 
                Select(tree, partName);
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
    
    public JFXInterpolateExpression InterpolateExpression(JCExpression var, List<JFXInterpolateValue> values) {
        return new JFXInterpolateExpression(var, values);
    }
    
    public JFXInterpolateValue InterpolateValue(JCExpression attr, JCExpression v, Name interp) {
        return new JFXInterpolateValue(attr, v, interp);
    }
    
    public JFXIndexof Indexof (Name name) {
        JFXIndexof tree = new JFXIndexof(name);
        tree.pos = pos;
        return tree;
    }
    
    public JFXTimeLiteral TimeLiteral(String str) {
        int i = 0;
        char[] buf = str.toCharArray();
        while (i < buf.length && (Character.isDigit(buf[i]) || buf[i] == '.'))
            i++;
        assert i > 0 && buf.length - i > 0; // lexer should only pass valid time strings
        String dur = str.substring(i);
        Duration duration = 
                dur.equals("ms") ? Duration.MILLIS :
                dur.equals("s") ? Duration.SECONDS :
                dur.equals("m") ? Duration.MINUTES :
                dur.equals("h") ? Duration.HOURS : null;
        assert duration != null;
        Object value;
        try {
            String s = str.substring(0, i);
            if (s.indexOf('.') >= 0)
                value = Double.valueOf(s) * duration.getMultiplier();
            else 
                value = Integer.valueOf(s) * duration.getMultiplier();
        } catch (NumberFormatException ex) {
            // error already reported in scanner
            value = Double.NaN;
        }
        JCLiteral literal = Literal(value);
        JFXTimeLiteral tree = new JFXTimeLiteral(literal, duration);
        tree.pos = pos;
        return tree;
    }

   
    private int syntheticClassNumber = 0;
    
    Name syntheticClassName(Name superclass) {
        return Name.fromString(names, superclass.toString() + "$anon" + ++syntheticClassNumber);
    }
}
