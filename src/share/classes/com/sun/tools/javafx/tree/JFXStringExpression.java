/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.StringExpressionTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.tools.javac.util.List;

import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * 
 * @author Robert Field
 */
public class JFXStringExpression extends JFXExpression implements StringExpressionTree {
    public List<JCExpression> parts;

    JFXStringExpression(List<JCExpression> parts) {
        this.parts = parts;
    }
    
    public void accept(JavafxVisitor v) { v.visitStringExpression(this);}

    /**
     * Parts are:
     *    (StringPart FormatPartOrNull ExpressionPart)* StringPart
     */
    public List<JCExpression> getParts() {
        return parts;
    }

    public java.util.List<ExpressionTree> getPartList() {
        return JFXTree.convertList(ExpressionTree.class, parts);
    }

    @Override
    public int getTag() {
        return JavafxTag.STRING_EXPRESSION;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.STRING_EXPRESSION;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitStringExpression(this, data);
    }
}
