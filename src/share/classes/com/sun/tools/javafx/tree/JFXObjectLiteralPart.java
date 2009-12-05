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

import com.sun.tools.mjavac.util.Name;
import com.sun.tools.mjavac.code.Symbol;
import com.sun.javafx.api.JavafxBindStatus;

/**
 * In object literal  "Identifier ':' [ 'bind'] expression"
 */
public class JFXObjectLiteralPart extends JFXExpression implements ObjectLiteralPartTree {
    public final Name name;
    private final JFXExpression expr;
    private final JavafxBindStatus explicitBindStatus;
    public Symbol sym;
   /*
    * @param selector member name and class name of member
    * @param init type of attribute
    * @param sym attribute symbol
    */
    protected JFXObjectLiteralPart(
            Name name,
            JFXExpression expr,
            JavafxBindStatus bindStatus,
            Symbol sym) {
        super(bindStatus);
        this.explicitBindStatus = bindStatus;
        this.name = name;
        this.expr = expr;
        this.sym = sym;
    }

    public void accept(JavafxVisitor v) {
        v.visitObjectLiteralPart(this);
    }

    public javax.lang.model.element.Name getName() {
        return name;
    }

    public JFXExpression getExpression() {
        return expr;
    }

    public JavafxBindStatus getExplicitBindStatus() {
        return explicitBindStatus;
    }

    public boolean isExplicitlyBound() {
        return explicitBindStatus.isBound();
    }
    
    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OBJECT_LITERAL_PART;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.OBJECT_LITERAL_PART;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitObjectLiteralPart(this, data);
    }
}
