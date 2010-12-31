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

import com.sun.tools.mjavac.util.List;

/**
 * Specialized tree that can indicate to the walker that it was manufactured
 * in place of an angle value that should have been there in the source code but
 * was erroneously not there (or perhaps the IDE is using this tree and the user
 * has not typed that in yet).
 *
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class JFXErroneousAngleLiteral extends JFXAngleLiteral  {

    /**
     * This class is just an Erroneous node masquerading as
     * a Block so that we can create it in the tree. So it
     * stores a local erroneous block and uses this for the
     * visitor pattern etc.
     */
    private JFXErroneous errNode;

    protected JFXErroneousAngleLiteral(List<? extends JFXTree> errs) {
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
