/*
 * Copyright 2003-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

import com.sun.javafx.api.tree.InterpolateValueTree;
import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.Name;

/**
 *
 * @author tball
 */
public class JFXInterpolateValue extends JFXTree implements InterpolateValueTree {
    public JCIdent attribute;
    public JCExpression value;
    public Name interpolation;
    
    public JFXInterpolateValue(JCIdent attr, JCExpression v, Name interp) {
        attribute = attr;
        value = v;
        interpolation = interp;
    }

    public JCIdent getAttribute() {
        return attribute;
    }

    public String getInterpolation() {
        return interpolation != null ? interpolation.toString() : null;
    }

    public JCExpression getValue() {
        return value;
    }

    public JavaFXKind getJavaFXKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitInterpolateValue(this);
    }

    @Override
    public int getTag() {
        return JavafxTag.INTERPOLATION_VALUE;
    }
}
