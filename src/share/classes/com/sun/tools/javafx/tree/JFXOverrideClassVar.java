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
import com.sun.javafx.api.JavafxBindStatus;

/**
 * The override of an instance variable
 *
 * @author Robert Field
 */
public class JFXOverrideClassVar extends JFXAbstractVar implements TriggerTree {
    private final JFXIdent expr;
    
    protected JFXOverrideClassVar(JFXIdent expr,
            JFXExpression init,
            JavafxBindStatus bindStat,
            JFXOnReplace onReplace,
            JFXOnReplace onInvalidate,
            VarSymbol sym) {
        super(init, bindStat, onReplace, onInvalidate, sym);
        this.expr = expr;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitOverrideClassVar(this);
    }
    
    public JFXIdent getId() {
        return expr;
    }

    public ExpressionTree getExpressionTree() {
        return (ExpressionTree) expr;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.TRIGGER_WRAPPER;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OVERRIDE_ATTRIBUTE_DEF;
    }
    
    @Override
    public boolean deferInit() {
        return false;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitTrigger(this, data);
    }
}
