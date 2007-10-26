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

import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import com.sun.tools.javac.code.Symbol.*;

/**
 * A class declaration
 */
public class JFXClassDeclaration extends JFXStatement {
    public JCModifiers mods;
    private final Name name;
    private List<JCExpression> extending = null;
    private List<JCExpression> implementing = null;
    private List<JCTree> defs;
    private final List<JCExpression> supertypes;

    public ClassSymbol sym;   

    public boolean isModuleClass = false;
    public List<JCTree> translatedPrepends = List.<JCTree>nil();
    public List<JCExpression> translatedAdditionalImplementing = List.<JCExpression>nil();
    
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
    
    public void hackAppendToMembers(JCTree member) {
        defs = defs.append(member);
    }

    public void appendToMembers(ListBuffer<JCTree> members) {
        defs = defs.appendList(members);
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
        } else {
            v.visitTree(this);
        }
    }
    
    // stuff to ignore
    
    public Kind getKind()  {
        throw new InternalError("not implemented");
    }
    
    @Override
    public <R,D> R accept(TreeVisitor<R,D> v, D d) {
        throw new InternalError("not implemented");
    }
}
