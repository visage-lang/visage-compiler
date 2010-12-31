/*
 * Copyright 2010 Visage Project.  All Rights Reserved.
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
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import javafx.lang.LengthUnit;

/**
 * Tree node for length literals, such as "100cm" or "3mm".
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class JFXLengthLiteral extends JFXExpression implements LengthLiteralTree {
    public JFXLiteral value;
    public LengthUnit units;
    
    protected JFXLengthLiteral(){
        this.value = null;
        this.units = null;
    }

    protected JFXLengthLiteral(JFXLiteral value, LengthUnit units) {
        this.value = value;
        this.units = units;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.LENGTH_LITERAL;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitLengthLiteral(this);
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.LENGTH_LITERAL;
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitLengthLiteral(this, data);
    }

    @Override
    public LengthUnit getUnits() {
        return units;
    }

    @Override
    public JFXLiteral getValue() {
        return value;
    }
}
