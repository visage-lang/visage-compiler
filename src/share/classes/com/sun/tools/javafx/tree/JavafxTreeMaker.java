/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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
    
    public JFXAttributeDefinition AttributeDefinition(
            JCModifiers modifiers,
            Name name,
            JFXType type,
            JFXMemberSelector inverseOrNull,
            JCExpression orderingOrNull, 
            JavafxBindStatus bindStatus, 
            JCExpression init,
            JCBlock onChange) {
        JFXAttributeDefinition tree = new JFXAttributeDefinition(
                modifiers,
                name,
                type,
                inverseOrNull,
                orderingOrNull, 
                bindStatus, 
                init,
                onChange);
        tree.pos = pos;
        return tree;
    }
    
    public JFXOperationDefinition OperationDefinition(
            JCModifiers modifiers,
            Name name,
            JFXType restype,
            List<JCTree> params, 
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
    
    public JFXFunctionDefinitionStatement FunctionDefinitionStatement(
            JCModifiers modifiers,
            Name name,
            JFXType restype,
            List<JCTree> params, 
            JFXBlockExpression bodyExpression) {
        JFXFunctionDefinitionStatement tree = new JFXFunctionDefinitionStatement(
                modifiers,
                name,
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
    
    public JFXVarIsObjectBeingInitialized VarIsObjectBeingInitialized(Name name) {
        JFXVarIsObjectBeingInitialized tree = new JFXVarIsObjectBeingInitialized(name, Modifiers(0L), null);
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
    
    public JFXType  TypeUnknown(int cardinality) {
        JFXType tree = new JFXTypeUnknown(cardinality);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeClass(JCExpression className,int cardinality) {
        JFXType tree = new JFXTypeClass(className, cardinality, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXType  TypeFunctional(List<JCTree> params,
            JFXType restype,
            int cardinality) {
        JFXType tree = new JFXTypeFunctional(params,
                restype,
                cardinality);
        tree.pos = pos;
        return tree;
    }
    
    
    public JFXVar Var(Name name,
            JFXType type,
            JCModifiers mods,
            JCExpression initializer,
            JavafxBindStatus bindStatus) {
        JFXVar tree = new JFXVar(name, type, 
                mods, initializer, bindStatus, null);
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

    public JFXPseudoRemoveExpression RemoveExpression(JCExpression removed) {
        JFXPseudoRemoveExpression tree = new JFXPseudoRemoveExpression(removed);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoReplaceExpression ReplaceExpression(JCExpression removed, JCExpression synthetic) {
        JFXPseudoReplaceExpression tree = new JFXPseudoReplaceExpression(removed, synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoSyntheticExpression SyntheticExpression(JCExpression synthetic) {
        JFXPseudoSyntheticExpression tree = new JFXPseudoSyntheticExpression(synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoRemoveStatement RemoveStatement(JCStatement removed) {
        JFXPseudoRemoveStatement tree = new JFXPseudoRemoveStatement(removed);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoReplaceStatement ReplaceStatement(JCStatement removed, JCStatement synthetic) {
        JFXPseudoReplaceStatement tree = new JFXPseudoReplaceStatement(removed, synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoSyntheticStatement SyntheticStatement(JCStatement synthetic) {
        JFXPseudoSyntheticStatement tree = new JFXPseudoSyntheticStatement(synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoRemoveTree RemoveTree(JCTree removed) {
        JFXPseudoRemoveTree tree = new JFXPseudoRemoveTree(removed);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoReplaceTree ReplaceTree(JCTree removed, JCTree synthetic) {
        JFXPseudoReplaceTree tree = new JFXPseudoReplaceTree(removed, synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    
    public JFXPseudoSyntheticTree SyntheticTree(JCTree synthetic) {
        JFXPseudoSyntheticTree tree = new JFXPseudoSyntheticTree(synthetic);
        tree.pos = Position.NOPOS;
        return tree;
    }
    

    
    public JavafxJCVarDecl JavafxVarDef(JCModifiers mods,
			 Name name,
                         int javafxVarType,
			 JCExpression vartype,
			 JCExpression init,
                         JavafxBindStatus bindStatus) {
        return new JavafxJCVarDecl(mods, name, javafxVarType, vartype, 
                init, null, bindStatus);
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