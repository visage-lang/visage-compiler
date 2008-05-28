/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.javafx.api.tree.JavaFXStatementTree;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.Visitor;

import com.sun.source.tree.TreeVisitor;
import com.sun.tools.javac.tree.JCTree;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Statements.
 * Should be a subclass of JFXTree (but can't for now)
 * @see JFXTree
 */
public abstract class JFXStatement extends JCStatement implements JavaFXStatementTree {
    
    /** Initialize tree with given tag.
     */
    protected JFXStatement() {
    }
    
    public abstract void accept(JavafxVisitor v);
    
    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else {
            assert false : "FX tree should not reach here";
            v.visitTree(this);
        }
    }

    @Override
    public final Kind getKind() {
        return Kind.OTHER;
    }

    @Override
    public final <R, D> R accept(TreeVisitor<R, D> v, D d) {
        if (v instanceof JavaFXTreeVisitor) {
            return (R)this.accept((JavaFXTreeVisitor)v, d);
        } else {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " support not implemented");
        }
    }

    /** Convert a tree to a pretty-printed string. */
    @Override
    public String toString() {
        StringWriter s = new StringWriter();
        try {
            new JavafxPretty(s, false).printExpr(this);
        }
        catch (IOException e) {
            // should never happen, because StringWriter is defined
            // never to throw any IOExceptions
            throw new AssertionError(e);
        }
        return s.toString();
    }
    
    @Override
    public int getStartPosition() {
        return JavafxTreeInfo.getStartPos(this);
    }
    
    @Override
    public int getEndPosition(Map<JCTree, Integer> endPosTable) {
        return JavafxTreeInfo.getEndPos(this, endPosTable);
    }
}
