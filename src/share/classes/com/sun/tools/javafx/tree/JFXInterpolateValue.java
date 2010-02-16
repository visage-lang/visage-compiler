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
import com.sun.tools.mjavac.code.Symbol;

/**
 *
 * @author tball
 */
public class JFXInterpolateValue extends JFXExpression implements InterpolateValueTree {
    public final JFXExpression attribute;
    public JFXExpression value;
    public JFXExpression funcValue;
    public final JFXExpression interpolation;
    public Symbol sym;
    
    public JFXInterpolateValue(JFXExpression attr, JFXExpression v, JFXExpression interp) {
        attribute = attr;
        value = v;
        funcValue = null;
        interpolation = interp;
        sym = null;
    }

    public JFXExpression getAttribute() {
        return attribute;
    }

    public JFXExpression getInterpolation() {
        return interpolation;
    }

    public JFXExpression getValue() {
        return value;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.INTERPOLATE_VALUE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitInterpolateValue(this, data);
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitInterpolateValue(this);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.INTERPOLATION_VALUE;
    }
}
