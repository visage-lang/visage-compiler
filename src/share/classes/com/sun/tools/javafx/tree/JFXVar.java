/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.mjavac.code.Symbol.VarSymbol;
import com.sun.tools.mjavac.util.Name;
import com.sun.javafx.api.JavafxBindStatus;

/**
 * Variable declaration.
 *
 * @author Robert Field
 * @author Zhiqun Chen
 */
public class JFXVar extends JFXAbstractVar implements VariableTree {
    public final JFXModifiers mods;
    public final Name name;
    private JFXType jfxtype;
    private JFXVarScriptInit varInit;

    protected JFXVar() {
        this(null, null, null, null, null, null, null, null);
    }

    protected JFXVar(Name name,
            JFXType jfxtype,
            JFXModifiers mods,
            JFXExpression init,
            JavafxBindStatus bindStat,
            JFXOnReplace onReplace,
            JFXOnReplace onInvalidate,
            VarSymbol sym) {
        super(init, bindStat, onReplace, onInvalidate, sym);
        this.mods = mods;
        this.name = name;
        this.jfxtype = jfxtype;
    }
    
    public Name getName() {
        return name;
    }

    /**
     * @return the varInit
     */
    public JFXVarScriptInit getVarInit() {
        return varInit;
    }

    /**
     * @param varInit the varInit to set
     */
    public void setVarInit(JFXVarScriptInit varInit) {
        this.varInit = varInit;
    }

    /**
     * Is init done in a var init
     */
    public boolean deferInit() {
        return this.varInit != null;
    }

    // for VariableTree
    public JFXTree getType() {
        return jfxtype;
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
