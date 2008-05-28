/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
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
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.Visitor;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.javafx.api.tree.IndexofTree;

/**
 * Indexof expression
 *
 * @author Per Bothner
 */
public class JFXIndexof extends JFXExpression implements IndexofTree {
    /** Name of corresponding 'for'/'where' variable. */
    public Name fname;
    public JFXForExpressionInClause clause;
    
    protected JFXIndexof (Name fname) {
        this.fname = fname;
    }
    public Name getForVarName() { return fname; }

    public void accept(JavafxVisitor v) { v.visitIndexof(this); }

    @Override
    public int getTag() {
        return JavafxTag.INDEXOF;
    }

    public JavaFXKind getJavaFXKind() {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " support not implemented");
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitIndexof(this, data);
    }
}
