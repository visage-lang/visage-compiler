/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.tree.JavaFXExpressionTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.Visitor;

import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.tree.Pretty;

/**
 * Statements.
 * Should be a subclass of JFXTree (but can't for now)
 * @see JFXTree
 */
public abstract class JFXExpression extends JCExpression implements JavaFXExpressionTree {
    
    /** Initialize tree with given tag.
     */
    protected JFXExpression() {
    }
    
    public abstract void accept(JavafxVisitor v);
    
    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else if (v instanceof Pretty) {
            try {
                Pretty pretty = (Pretty) v;
                pretty.print('[');
                pretty.print(getClass().getName());
                //pretty.print(' ');
                //v.visitTree(this); visit children?
                pretty.print(']');
            } catch (java.io.IOException ex) { throw new RuntimeException(ex); }
        } else {
            assert false : "should be seen by a non-JavaFX visitor: " + this.getClass();
            v.visitTree(this);
        }
    }

    @Override
    public final Kind getKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final <R, D> R accept(TreeVisitor<R, D> v, D d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
