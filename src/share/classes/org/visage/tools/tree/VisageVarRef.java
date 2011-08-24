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

package org.visage.tools.tree;

import org.visage.api.tree.VisageTreeVisitor;
import org.visage.api.tree.Tree.VisageKind;
import com.sun.tools.mjavac.code.Symbol;


/**
 * An identifier
 * @param idname the name
 * @param sym the symbol
 */
public class VisageVarRef extends VisageExpression {

    private Symbol sym;
    private VisageExpression expr;
    private RefKind kind;
    private VisageExpression receiver;

    protected VisageVarRef(VisageExpression expr, RefKind kind) {
        this.kind = kind;
        this.sym = VisageTreeInfo.symbolFor(expr);
        this.expr = expr;
        if (!sym.isStatic() && expr.getFXTag() == VisageTag.SELECT) {
            receiver = ((VisageSelect)expr).selected;
        }
    }

    public VisageExpression getReceiver() {
        return receiver;
    }

    public void setReceiver(VisageExpression receiver) {
        this.receiver = receiver;
    }

    public VisageExpression getExpression() {
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

    public void accept(VisageVisitor v) {
        v.visitVarRef(this);
    }

    public VisageTag getFXTag() {
        return VisageTag.VAR_REF;
    }

    public VisageKind getVisageKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}