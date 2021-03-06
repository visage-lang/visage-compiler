/*
 * Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.util.List;

/**
 * Specialized tree that can indicate to the walker that it was manufactured
 * in place of a time value that should have been there in the source code but
 * was erroneously not there (or perhaps the IDE is using this tree and the user
 * has not typed that in yet).
 *
 * @author jimi
 */
public class VisageErroneousTimeLiteral extends VisageTimeLiteral  {

    /**
     * This class is just an Erroneous node masquerading as
     * a Block so that we can create it in the tree. So it
     * stores a local erroneous block and uses this for the
     * visitor pattern etc.
     */
    private VisageErroneous errNode;

    protected VisageErroneousTimeLiteral(List<? extends VisageTree> errs) {
        errNode = new VisageErroneous(errs);
    }

    @Override
    public VisageTag getVisageTag() {
        return VisageTag.ERRONEOUS;
    }

    @Override
    public void accept(VisageVisitor v) {
        v.visitErroneous(errNode);
    }

    @Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitErroneous(errNode, d);
    }

    public List<? extends VisageTree> getErrorTrees() {
        return errNode.getErrorTrees();
    }
    
    @Override
    public VisageKind getVisageKind() {
        return VisageKind.ERRONEOUS;
    }
    
}
