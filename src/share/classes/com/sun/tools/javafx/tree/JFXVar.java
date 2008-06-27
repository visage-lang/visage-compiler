/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Name;
import com.sun.javafx.api.JavafxBindStatus;
import java.util.Map;

/**
 * Variable declaration.
 *
 * @author Robert Field
 * @author Zhiqun Chen
 */
public class JFXVar extends JFXStatement implements VariableTree {
    public JFXModifiers mods;
    public Name name;
    public JFXExpression vartype;
    public JFXExpression init;
    public VarSymbol sym;
    private JFXType jfxtype;
    private final JavafxBindStatus bindStatus;
    private final boolean local;
    private final JFXOnReplace onReplace;

    protected JFXVar(Name name,
            JFXType jfxtype,
            JFXModifiers mods,
            boolean local,
            JFXExpression init,
            JavafxBindStatus bindStat,
            JFXOnReplace onReplace,
            VarSymbol sym) {
            this.mods = mods;
            this.name = name;
            this.vartype = jfxtype;
            this.init = init;
            this.sym = sym;
        this.local = local;
        this.jfxtype = jfxtype;
        this.bindStatus = bindStat == null ? JavafxBindStatus.UNBOUND : bindStat;
        this.onReplace = onReplace;
        this.sym = sym;
    }
    
    public Name getName() {
        return name;
    }

    public JFXTree getType() {
        return vartype;
    }

    public JFXExpression getInitializer() {
        return init;
    }

    public void accept(JavafxVisitor v) {
        v.visitVar(this);
    }

    public JFXType getJFXType() {
        return jfxtype;
    }

    public void setJFXType(JFXType type) {
        jfxtype = type;
    }
    
    public OnReplaceTree getOnReplaceTree() {
        return onReplace;        
    }
    
    public JFXOnReplace getOnReplace() {
        return onReplace;
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
    public JavafxTag getFXTag() {
        return JavafxTag.VAR_DEF;
    }
    
    public JFXModifiers getModifiers() {
        return mods;
    }
    
    public boolean isOverride() {
        return false;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.VARIABLE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitVariable(this, data);
     }
}
