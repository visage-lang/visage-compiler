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

import com.sun.javafx.api.tree.ClassDeclarationTree;
import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.Pretty;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class declaration
 */
public class JFXClassDeclaration extends JFXStatement implements ClassDeclarationTree {
    public JCModifiers mods;
    private final Name name;
    private List<JCExpression> extending = null;
    private List<JCExpression> implementing = null;
    private List<JCTree> defs;
    private final List<JCExpression> supertypes;

    public ClassSymbol sym;   

    public boolean isModuleClass = false;
    public boolean hasBeenTranslated = false; // prevent multiple translations
    
    protected JFXClassDeclaration(JCModifiers mods,
            Name name,
            List<JCExpression> supertypes,
            List<JCTree> declarations,
            ClassSymbol sym) {
        this.mods = mods;
        this.name = name;
        this.defs = declarations;
        this.sym = sym;
            
        this.supertypes = supertypes;
    }

    public java.util.List<ExpressionTree> getSupertypeList() {
        return JFXTree.convertList(ExpressionTree.class, supertypes);
    }

    public JCModifiers getModifiers() {
        return mods;
    }

    public Name getName() {
        return name;
    }

    public List<JCExpression> getSupertypes() {
        return supertypes;
    }

    public List<JCTree> getMembers() {
        return defs;
    }

    public List<JCExpression> getImplementing() {
        return implementing;
    }

    public List<JCExpression> getExtending() {
        return extending;
    }

    //TODO: remove this method and all references to it.
    /**
     * Because of multiple inheritance, we may extend many classes.
     * This is a hack to work around assumptions that we only extend a single class.
     * */
    public JCTree getFirstExtendingHack() {
        return extending.size() == 0? null : extending.head;
    }

    public void setDifferentiatedExtendingImplementing(List<JCExpression> extending, List<JCExpression> implementing) {
        this.extending = extending;
        this.implementing = implementing;
    }
    
    public List<JCTypeParameter> getEmptyTypeParameters() {
        return List.<JCTypeParameter>nil();
    }
    
    public void appendToMembers(ListBuffer<JCTree> members) {
        defs = defs.appendList(members.toList());
    }

    public boolean generateClassOnly () {
        // FIXME also return true if extending a Java class.
        return (getModifiers().flags & Flags.FINAL) != 0;
    }

    @Override
    public int getTag() {
        return JavafxTag.CLASS_DEF;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitClassDeclaration(this);
    }

    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else if (v instanceof Pretty) {
            try {
                StringWriter out = new StringWriter();
                new JavafxPretty(out, true).visitClassDeclaration(this);
                ((Pretty) v).print(out.toString());
            } catch (IOException ex) {
                Logger.getLogger(JFXClassDeclaration.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            v.visitTree(this);
        }
    }
    
    public java.util.List<Tree> getMemberTrees() {
        return JFXTree.convertList(Tree.class, defs);
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

    public java.util.List<ExpressionTree> getSupers() {
        return JFXTree.convertList(ExpressionTree.class, supertypes);
    }

    public java.util.List<ExpressionTree> getImplements() {
        return JFXTree.convertList(ExpressionTree.class, implementing);
    }

    public java.util.List<Tree> getClassMembers() {
        return JFXTree.convertList(Tree.class, defs);
    }

    public java.util.List<ExpressionTree> getExtends() {
        return JFXTree.convertList(ExpressionTree.class, extending);
    }
}
