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

import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import com.sun.tools.mjavac.code.Symbol;


/**
 * An identifier
 * @param idname the name
 * @param sym the symbol
 */
public class JFXVarRef extends JFXExpression {

    private Symbol sym;
    private JFXExpression expr;
    private RefKind kind;
    private JFXExpression receiver;

    protected JFXVarRef(JFXExpression expr, RefKind kind) {
        this.kind = kind;
        this.sym = JavafxTreeInfo.symbolFor(expr);
        this.expr = expr;
        if (!sym.isStatic() && expr.getFXTag() == JavafxTag.SELECT) {
            receiver = ((JFXSelect)expr).selected;
        }
    }

    public JFXExpression getReceiver() {
        return receiver;
    }

    public void setReceiver(JFXExpression receiver) {
        this.receiver = receiver;
    }

    public JFXExpression getExpression() {
        return expr;
    }

    public Symbol getVarSymbol() {
        return sym;
    }

    public RefKind getVarRefKind() {
        return kind;
    }

    public enum RefKind {
        VARNUM,
        INST;
    }

    public void accept(JavafxVisitor v) {
        v.visitVarRef(this);
    }

    public JavafxTag getFXTag() {
        return JavafxTag.VAR_REF;
    }

    public JavaFXKind getJavaFXKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}