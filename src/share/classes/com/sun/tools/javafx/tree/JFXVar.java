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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.OnChangeTree;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.javafx.api.JavafxBindStatus;
import com.sun.javafx.api.tree.JavaFXVariableTree;

/**
 * Variable declaration.
 *
 * @author Robert Field
 */
public class JFXVar extends JCVariableDecl implements JavaFXVariableTree {
    private JFXType jfxtype;
    private final JavafxBindStatus bindStatus;
    private final List<JFXAbstractOnChange> onChanges;
    private final boolean local;
    
    protected JFXVar(Name name,
            JFXType jfxtype,
            JCModifiers mods,
            boolean local,
            JCExpression init,
            JavafxBindStatus bindStat,
            List<JFXAbstractOnChange> onChanges,
            VarSymbol sym) {
        super(mods, name, jfxtype, init, sym);
        this.local = local;
        this.jfxtype = jfxtype;
        this.bindStatus = bindStat == null ? JavafxBindStatus.UNBOUND : bindStat;
        this.onChanges = onChanges;
        this.sym = sym;
    }
    
    public void accept(JavafxVisitor v) { v.visitVar(this); }
    
    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else {
            v.visitVarDef(this);
        }
    }

    public JFXType getJFXType() {
        return jfxtype;
    }

    public void setJFXType(JFXType type) {
        jfxtype = type;
    }

    public java.util.List<OnChangeTree> getOnChangeTrees() {
        return JFXTree.convertList(OnChangeTree.class, onChanges);
    }

    public List<JFXAbstractOnChange> getOnChanges() {
        return onChanges;
    }

    public boolean isLocal() {
        return local;
    }

    public JavafxBindStatus getBindStatus() {
        return bindStatus;
    }

    public boolean isBound() {
        return bindStatus.isBound();
    }

    public boolean isUnidiBind() {
        return bindStatus.isUnidiBind();
    }

    public boolean isBidiBind() {
        return bindStatus.isBidiBind();
    }

    public boolean isLazy() {
        return bindStatus.isLazy();
    }

    @Override
    public int getTag() {
        return JavafxTag.VAR_DEF;
    }
    
    public JCModifiers getModifiers() {
        return mods;
    }

    public JavaFXKind getJavaFXKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
