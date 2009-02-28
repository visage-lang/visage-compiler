/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.tools.javafx.code.JavafxFlags;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Scope;

/**
 * A class declaration
 */
public class JFXClassDeclaration extends JFXExpression implements ClassDeclarationTree {
    public final JFXModifiers mods;
    private final Name name;
    private List<JFXExpression> extending    = null;
    private List<JFXExpression> implementing = null;
    private List<JFXExpression> mixing       = null;
    private List<JFXTree> defs;
    private List<JFXExpression> supertypes;

    public ClassSymbol sym;
    
    public JFXFunctionDefinition runMethod;
    public Scope runBodyScope;
    public boolean isScriptClass = false;

    public boolean hasBeenTranslated = false; // prevent multiple translations
    
    protected JFXClassDeclaration()
    {
        this.mods       = null;
        this.name       = null;
        this.supertypes = null;
        this.defs       = null;
        this.sym        = null;
    }
    protected JFXClassDeclaration(JFXModifiers mods,
            Name name,
            List<JFXExpression> supertypes,
            List<JFXTree> declarations,
            ClassSymbol sym) {
        this.mods = mods;
        this.name = name;           
        this.supertypes = supertypes;
        this.defs = declarations;
        this.sym = sym;
    }

    public java.util.List<ExpressionTree> getSupertypeList() {
        return convertList(ExpressionTree.class, supertypes);
    }

    public JFXModifiers getModifiers() {
        return mods;
    }

    public Name getName() {
        return name;
    }

    public List<JFXExpression> getSupertypes() {
        return supertypes;
    }

    public List<JFXTree> getMembers() {
        return defs;
    }

    public void setMembers(List<JFXTree> members) {
        defs = members;
    }

    public List<JFXExpression> getImplementing() {
        return implementing;
    }

    public List<JFXExpression> getExtending() {
        return extending;
    }

    public List<JFXExpression> getMixing() {
        return mixing;
    }

    public void setDifferentiatedExtendingImplementing(List<JFXExpression> extending,
                                                       List<JFXExpression> implementing,
                                                       List<JFXExpression> mixing) {
        this.extending    = extending;
        this.implementing = implementing;
        this.mixing       = mixing;
        
        // JFXC-2663 - Implement mixin linerization.
        ListBuffer<JFXExpression> orderedSuperTypes = new ListBuffer<JFXExpression>();
        
        // Add supers according to declaration and normal, mixin and interface constraints.
        for (JFXExpression extend    : extending)    orderedSuperTypes.append(extend);
        for (JFXExpression mixin     : mixing)       orderedSuperTypes.append(mixin);
        for (JFXExpression implement : implementing) orderedSuperTypes.append(implement);
        
        // Replace supertypes so that all references use the correct ordering.
        supertypes = orderedSuperTypes.toList();
    }
    
    public boolean isMixinClass() {
        return (sym.flags_field & JavafxFlags.MIXIN) != 0;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.CLASS_DEF;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitClassDeclaration(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.CLASS_DECLARATION;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitClassDeclaration(this, data);
    }

    public javax.lang.model.element.Name getSimpleName() {
        return (javax.lang.model.element.Name)name;
    }

    public java.util.List<ExpressionTree> getImplements() {
        return JFXTree.convertList(ExpressionTree.class, implementing);
    }

    public java.util.List<Tree> getClassMembers() {
        return convertList(Tree.class, defs);
    }

    public java.util.List<ExpressionTree> getExtends() {
        return convertList(ExpressionTree.class, extending);
    }

    public java.util.List<ExpressionTree> getMixins() {
        return convertList(ExpressionTree.class, mixing);
    }
}
