/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.code.Flags;

 /**
 * A statement block.
 * @param stats statements
 * @param flags flags
 */
public class JFXBlock extends JFXStatement implements BlockTree {

    public long flags;
    public List<JFXStatement> stats;
    /** Position of closing brace, optional. */
    public int endpos = Position.NOPOS;

    protected JFXBlock(long flags, List<JFXStatement> stats) {
        this.stats = stats;
        this.flags = flags;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitBlock(this);
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.BLOCK;
    }

    public java.util.List<StatementTree> getStatements() {
        return convertList(StatementTree.class, stats);
    }

    public List<JFXStatement> getStmts() {
        return stats;
    }

    public boolean isStatic() {
        return (flags & Flags.STATIC) != 0;
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitBlock(this, d);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.BLOCK;
    }
}
