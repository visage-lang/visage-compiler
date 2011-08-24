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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.util.List;

/**
 * A "try { } catch ( ) { } finally { }" block.
 */
public class VisageTry extends VisageExpression implements TryTree {

    public VisageBlock body;
    public List<VisageCatch> catchers;
    public VisageBlock finalizer;

    protected VisageTry(VisageBlock body, List<VisageCatch> catchers, VisageBlock finalizer) {
        this.body = body;
        this.catchers = catchers;
        this.finalizer = finalizer;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitTry(this);
    }

    public VisageKind getJavaFXKind() {
        return VisageKind.TRY;
    }

    public VisageBlock getBlock() {
        return body;
    }

    public java.util.List<CatchTree> getCatches() {
        return convertList(CatchTree.class, catchers);
    }

    public VisageBlock getFinallyBlock() {
        return finalizer;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitTry(this, d);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.TRY;
    }
}
