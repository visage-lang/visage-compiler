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

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javafx.code.JavafxBindStatus;

/* JavaFX version of tree maker
 */
public class JavafxTreeMaker extends TreeMaker implements JavafxTreeFactory {
    /** Get the JavafxTreeMaker instance.
     */
    public static TreeMaker instance(Context context) {
        TreeMaker instance = context.get(treeMakerKey);
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
                List.<JCExpression>nil(),
                declarations,
                null);
        tree.pos = pos;
        return tree;
    }

    public JFXClassDeclaration ClassDeclaration(JCModifiers mods,
            Name name,
            List<JCExpression> supertypes,
            List<JCExpression> implementedInterfaces,
            List<JCTree> declarations) {
        JFXClassDeclaration tree = new JFXClassDeclaration(mods,
                name,
                supertypes,
                implementedInterfaces,
                declarations,
                null);
        tree.pos = pos;
        return tree;
    }

    public JFXBlockExpression BlockExpression(long flags, List<JCStatement> stats, JCExpression value) {
        JFXBlockExpression tree = new JFXBlockExpression(flags, stats, value);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOperationDefinition OperationDefinition(
            JCModifiers modifiers,
            Name name,
            JFXType restype,
            List<JFXVar> params, 
            JFXBlockExpression bodyExpression) {
        JFXOperationDefinition tree = new JFXOperationDefinition(
                modifiers,
                name,
                restype,
                params,
                bodyExpression);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOperationValue OperationValue(
            JFXType restype,
             List<JFXVar> params, 
            JFXBlockExpression bodyExpression) {
        JFXOperationValue tree = new JFXOperationValue(
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

    public JFXSequenceRange RangeSequence(JCExpression lower, JCExpression upper) {
        JFXSequenceRange tree = new JFXSequenceRange(lower, upper);
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

    public JFXSequenceInsert SequenceInsert(JCExpression sequence, JCExpression element) {
        JFXSequenceInsert tree = new JFXSequenceInsert(sequence, element);
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

    public JFXStringExpression StringExpression(List<JCExpression> parts) {
        JFXStringExpression tree = new JFXStringExpression(parts);
        tree.pos = pos;
        return tree;
    }
    
    public JFXPureObjectLiteral PureObjectLiteral(JCExpression ident,
            List<JCStatement> parts) {
        JFXPureObjectLiteral tree = new JFXPureObjectLiteral(ident, parts, null);
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
    
    public JFXType  TypeAny(int cardinality) {
        JFXType tree = new JFXTypeAny(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeUnknown() {
        JFXType tree = new JFXTypeUnknown();
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeClass(JCExpression className,int cardinality) {
        JFXType tree = new JFXTypeClass(className, cardinality, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType TypeFunctional(List<JFXType> params,
            JFXType restype,
            int cardinality) {
        JFXType tree = new JFXTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }
    
    
    public JFXOnReplace OnReplace(JFXVar oldValue, JCBlock body) {
         JFXOnReplace tree = new JFXOnReplace(oldValue, body);
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
            JCExpression initializer,
            JavafxBindStatus bindStatus,
            List<JFXAbstractOnChange> onChanges) {
        JFXVar tree = new JFXVar(name, type, 
                mods, initializer, bindStatus, onChanges, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVar Param(Name name,
            JFXType type) {
        JFXVar tree = new JFXVar(name, type, 
                Modifiers(Flags.PARAMETER), null, JavafxBindStatus.UNBOUND, 
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
    
    public JFXInstanciate Instanciate(JCExpression encl,
                             List<JCExpression> typeargs,
                             JCExpression clazz,
                             List<JCExpression> args,
                             JCClassDecl def)
    {
        JFXInstanciate tree = new JFXInstanciate(encl, typeargs, clazz, args, def);
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
        JCExpression tree = null;
        int inx;
        int lastInx = 0;
        do {
            inx = str.indexOf('.', lastInx);
            int endInx;
            if (inx < 0) {
                endInx = str.length();
                int ltInx = str.indexOf('<', lastInx);
                if (ltInx >= 0) {
                    // proof of concept only
                    String part = str.substring(lastInx, ltInx);
                    Name partName = Name.fromString(names, part);
                    tree = tree==null? Ident(partName) : Select(tree, partName);
                    tree.pos = pos;
                    ListBuffer<JCExpression> generic = ListBuffer.lb();
                    int gtInx = str.indexOf('>', ltInx);
                    String tpart = str.substring(ltInx+1, gtInx);
                    Name tpartName = Name.fromString(names, tpart);
                    JCExpression texp = Ident(tpartName);
                    generic.append(texp);
                    tree = TypeApply(tree, generic.toList());
                    tree.pos = pos;
                    break;
                }
            } else {
                endInx = inx;
            }
            String part = str.substring(lastInx, endInx);
            Name partName = Name.fromString(names, part);
            tree = tree==null? Ident(partName) : Select(tree, partName);
            tree.pos = pos;
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
}