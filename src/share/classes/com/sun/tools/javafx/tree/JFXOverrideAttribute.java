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
import com.sun.javafx.api.JavafxBindStatus;

/**
 * Wrapper for loose triggers
 *
 * @author Robert Field
 */
public class JFXOverrideAttribute extends JFXStatement implements TriggerTree {
    private final JFXIdent expr;
    private final JFXExpression init;
    private final JavafxBindStatus bindStatus;
    private final JFXOnReplace onReplace;
    
    public VarSymbol sym;
    
    protected JFXOverrideAttribute(JFXIdent expr,
            JFXExpression init,
            JavafxBindStatus bindStat,
            JFXOnReplace onReplace,
            VarSymbol sym) {
        this.expr = expr;
        this.init = init;
        this.bindStatus = bindStat == null ? JavafxBindStatus.UNBOUND : bindStat;
        this.onReplace = onReplace;
        this.sym = sym;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitOverrideAttribute(this);
    }
    
    public JFXIdent getId() {
        return expr;
    }

    public JFXExpression getInitializer() {
        return init;
    }

    public ExpressionTree getExpressionTree() {
        return (ExpressionTree)expr;
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

    public OnReplaceTree getOnReplaceTree() {
        return (OnReplaceTree) onReplace;
    }

    public JFXOnReplace getOnReplace() {
        return onReplace;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.TRIGGER_WRAPPER;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OVERRIDE_ATTRIBUTE_DEF;
    }
    
    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitTrigger(this, data);
    }
}
