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

import com.sun.tools.mjavac.util.List;

/**
 * A throw statement.
 */
public class JFXErroneousThrow extends JFXThrow {
    /**
     * This class is just an Erroneous node masquerading as
     * a Block so that we can create it in the tree. So it
     * stores a local erroneous block and uses this for the
     * vistor pattern etc.
     */
    private JFXErroneous errNode;

    protected JFXErroneousThrow() {
        errNode = new JFXErroneous(List.<JFXTree>nil());
    }
    protected JFXErroneousThrow(List<? extends JFXTree> errs) {
        errNode = new JFXErroneous(errs);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.ERRONEOUS;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitErroneous(errNode);
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitErroneous(errNode, d);
    }

    public List<? extends JFXTree> getErrorTrees() {
        return errNode.getErrorTrees();
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.ERRONEOUS;
    }

}

