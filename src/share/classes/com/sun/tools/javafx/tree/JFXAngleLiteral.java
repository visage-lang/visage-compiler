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
import javafx.lang.AngleUnit;

/**
 * Tree node for angle literals, such as "100deg" or "3rad".
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class JFXAngleLiteral extends JFXExpression implements AngleLiteralTree {
    public JFXLiteral value;
    public AngleUnit units;
    
   protected JFXAngleLiteral(){
        this.value = null;
        this.units = null;
    }

    protected JFXAngleLiteral(JFXLiteral value, AngleUnit units) {
        this.value = value;
        this.units = units;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.ANGLE_LITERAL;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitAngleLiteral(this);
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.ANGLE_LITERAL;
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitAngleLiteral(this, data);
    }

    @Override
    public AngleUnit getUnits() {
        return units;
    }

    @Override
    public JFXLiteral getValue() {
        return value;
    }
}
