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
import com.sun.tools.javafx.code.JavafxMethodSymbol;
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
    public JavafxTreeMaker forToplevel(JCCompilationUnit toplevel) {
        return new JavafxTreeMaker(toplevel, names, types, syms);
    }
    
    /** Reassign current position.
     */
    public JavafxTreeMaker at(int pos) {
        this.pos = pos;
        return this;
    }
    
    /** Reassign current position.
     */
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
                null,
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

    public JavafxJCClassDecl JavafxJCClassDef(JCModifiers mods,
                                              Name name,
                                              JCTree extending,
                                              List<JCExpression> implementing,
                                              List<JCTree> defs, JavafxJCMethodDecl initializer) {
        List<JCTypeParameter> typarams = List.nil();
        JavafxJCClassDecl tree = new JavafxJCClassDecl(mods,
                name,
                typarams,
                extending,
                implementing,
                defs, initializer);
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
    
    public JFXFunctionDefinition FunctionDefinition(
            JCModifiers modifiers,
            Name name,
            JFXType restype,
            List<JCTree> params, 
            JFXBlockExpression bodyExpression) {
        JFXFunctionDefinition tree = new JFXFunctionDefinition(
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
    
    public JFXRetroAttributeDeclaration RetroAttributeDeclaration(JCModifiers mods,
            Name name,
            JFXType type,
            JFXMemberSelector inverseOrNull,
            JCExpression orderingOrNull) {
        JFXRetroAttributeDeclaration tree = new JFXRetroAttributeDeclaration(mods,
                name,
                type,
                inverseOrNull,
                orderingOrNull);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroFunctionMemberDeclaration RetroFunctionDeclaration(JCModifiers mods,
            Name name,
            JFXType restype,
            List<JCTree> params) {
        JFXRetroFunctionMemberDeclaration tree = new JFXRetroFunctionMemberDeclaration(mods,
                name,
                restype,
                params);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroOperationMemberDeclaration RetroOperationDeclaration(JCModifiers mods,
            Name name,
            JFXType restype,
            List<JCTree> params) {
        JFXRetroOperationMemberDeclaration tree = new JFXRetroOperationMemberDeclaration(mods,
                name,
                restype,
                params);
        tree.pos = pos;
        return tree;
    }
    
    
    
    public JFXRetroAttributeDefinition RetroAttributeDefinition(
            JFXMemberSelector selector,
            JCExpression init,
            JavafxBindStatus bindStatus) {
        JFXRetroAttributeDefinition tree = new JFXRetroAttributeDefinition(
                selector,
                init,
                bindStatus);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroFunctionMemberDefinition RetroFunctionDefinition(
            JFXMemberSelector selector,
            JFXType restype,
            List<JCTree> params,
            JCBlock body) {
        JFXRetroFunctionMemberDefinition tree = new JFXRetroFunctionMemberDefinition(
                selector,
                restype,
                params,
                body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroOperationMemberDefinition RetroOperationDefinition(
            JFXMemberSelector selector,
            JFXType restype,
            List<JCTree> params,
            JCBlock body) {
        JFXRetroOperationMemberDefinition tree = new JFXRetroOperationMemberDefinition(
                selector,
                restype,
                params,
                body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroFunctionLocalDefinition RetroFunctionLocalDefinition(
            Name name,
            JFXType restype,
            List<JCTree> params,
            JCBlock body) {
        JFXRetroFunctionLocalDefinition tree = new JFXRetroFunctionLocalDefinition(
                name,
                restype,
                params,
                body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXRetroOperationLocalDefinition RetroOperationLocalDefinition(
            Name name,
            JFXType restype,
            List<JCTree> params,
            JCBlock body) {
        JFXRetroOperationLocalDefinition tree = new JFXRetroOperationLocalDefinition(
                name,
                restype,
                params,
                body);
        tree.pos = pos;
        return tree;
    }
    
    public JFXTriggerOnNew TriggerOnNew(
            JCExpression typeIdentifier,
            JCExpression identifier,
            JCBlock block) {
        JFXTriggerOnNew tree = new JFXTriggerOnNew(typeIdentifier,
                identifier, block);
        tree.pos = pos;
        return tree;
    }
    
    public JFXTriggerOnReplace TriggerOnReplace(
            JFXMemberSelector selector,
            JCExpression identifier,
            JCBlock block) {
        JFXTriggerOnReplace tree = new JFXTriggerOnReplace(selector, identifier, block);
        tree.pos = pos;
        return tree;
    }
    
    public JFXTriggerOnReplaceElement TriggerOnReplaceElement(
            JFXMemberSelector selector,
            JCExpression elementIdentifier,
            JCExpression identifier,
            JCBlock block) {
        JFXTriggerOnReplaceElement tree = new JFXTriggerOnReplaceElement(selector, elementIdentifier, identifier, block);
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
            List<JFXStatement> parts) {
        JFXPureObjectLiteral tree = new JFXPureObjectLiteral(ident, parts, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVarIsObjectBeingInitialized VarIsObjectBeingInitialized(Name name) {
        JFXVarIsObjectBeingInitialized tree = new JFXVarIsObjectBeingInitialized(name, null);
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
    
    public JFXType  TypeClass(Name className,int cardinality) {
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
    
    public JFXVar  Var(Name name,
                        JFXType type) {
        return Var(name, type, null);
    }
    
    public JFXVar  Var(Name name,
                        JFXType type,
                        JCModifiers mods) {
        JFXVar tree = new JFXVar(name, type, mods, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVarStatement  VarStatement(Name name,
            JFXType type) {
        JFXVarStatement tree = new JFXVarStatement(name, type, null);
        tree.pos = pos;
        return tree;
    }
    
    public JFXVarInit  VarInit(Name name,
            JFXType type,
            JCExpression initializer,
            JavafxBindStatus bindStatus) {
        JFXVarInit tree = new JFXVarInit(name, type, 
                initializer, bindStatus, null);
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
    
    // Enable for debugging. To make sure noone is calling thiese methods form the different translators before javafxAttr.
    //    public JCMethodDecl MethodDef(JCModifiers mods,
    //                               Name name,
    //                               JCExpression restype,
    //                               List<JCTypeParameter> typarams,
    //                               List<JCVariableDecl> params,
    //                               List<JCExpression> thrown,
    //                               JCBlock body,
    //                               JCExpression defaultValue)
    //    {
    //        throw new Error("Illegal call! Try JavafxMethodDef instead!");
    //    }
    //
    //    public JCVariableDecl VarDef(JCModifiers mods, Name name, JCExpression vartype, JCExpression init) {
    //        throw new Error("Illegal call! Try JavafxVarDef instead!");
    //    }
    
    public JavafxJCMethodDecl JavafxMethodDef(JCModifiers mods,
            int javafxMethodType,
            Name name,
            JCExpression restype,
            List<JCVariableDecl> params,
            JCBlock body,
            JFXStatement definition,
            JFXStatement declaration) {
        List<JCTypeParameter> typarams = List.nil();
        List<JCExpression> thrown = List.nil();
        return new JavafxJCMethodDecl(mods, javafxMethodType, name, restype, typarams,
                params, thrown, body, null, definition, declaration);
    }
    
    public JavafxJCMethodDecl JavafxMethodDef(JCModifiers mods,
            int javafxMethodType,
            Name name,
            JCExpression restype,
            List<JCVariableDecl> params,
            JCBlock body) {
        List<JCTypeParameter> typarams = List.nil();
        List<JCExpression> thrown = List.nil();
        return new JavafxJCMethodDecl(mods, javafxMethodType, name, restype, typarams,
                params, thrown, body, null, null, null);
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
    
    public JavafxJCAssign JavafxAssign(JCExpression lhs, 
            JCExpression rhs,
            JavafxBindStatus bindStatus) {
        JavafxJCAssign tree = new JavafxJCAssign(lhs, rhs, bindStatus);
        tree.pos = pos;
        return tree;
    }

    public JCExpression Identifier(Name name) {
        String str = name.toString();
        if (str.indexOf('.') < 0 && str.indexOf('<') < 0) {
            return Ident(name);
        }
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
                    Name partName = Name.fromString(name.table, part);
                    tree = tree==null? Ident(partName) : Select(tree, partName);
                    tree.pos = pos;
                    ListBuffer<JCExpression> generic = ListBuffer.lb();
                    int gtInx = str.indexOf('>', ltInx);
                    String tpart = str.substring(ltInx+1, gtInx);
                    Name tpartName = Name.fromString(name.table, tpart);
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
            Name partName = Name.fromString(name.table, part);
            tree = tree==null? Ident(partName) : Select(tree, partName);
            tree.pos = pos;
            lastInx = endInx + 1;
        } while (inx >= 0);
        return tree;
    }
}
