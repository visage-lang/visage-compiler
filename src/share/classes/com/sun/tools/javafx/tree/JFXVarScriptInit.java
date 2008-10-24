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

import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.util.Name;
import com.sun.javafx.api.JavafxBindStatus;

/**
 * Initialization of a var inline in a script body
 *
 * @author Robert Field
 */
public class JFXVarScriptInit extends JFXExpression implements VariableTree {
    private final JFXVar var;

    protected JFXVarScriptInit(JFXVar var) {
            this.var = var;
    }
    
    public JFXVar getVar() {
        return var;
    }

    public VarSymbol getSymbol() {
        return var.getSymbol();
    }

    public Name getName() {
        return var.getName();
    }

    // for VariableTree
    public JFXTree getType() {
        return var.getType();
    }

    public JFXExpression getInitializer() {
        return var.getInitializer();
    }

    public void accept(JavafxVisitor v) {
        v.visitVarScriptInit(this);
    }

    public JFXType getJFXType() {
        return var.getJFXType();
    }

    public OnReplaceTree getOnReplaceTree() {
        return var.getOnReplaceTree();
    }
    
    public JFXOnReplace getOnReplace() {
        return var.getOnReplace();
    }

    public JavafxBindStatus getBindStatus() {
        return var.getBindStatus();
    }

    public boolean isBound() {
        return var.isBound();
    }

    public boolean isUnidiBind() {
        return var.isUnidiBind();
    }

    public boolean isBidiBind() {
        return var.isBidiBind();
    }

    public boolean isLazy() {
        return var.isLazy();
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.VAR_SCRIPT_INIT;
    }
    
    public JFXModifiers getModifiers() {
        return var.getModifiers();
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
