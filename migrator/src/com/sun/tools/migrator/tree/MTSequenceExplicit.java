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

package com.sun.tools.migrator.tree;

import com.sun.tools.javac.util.List;

import  com.sun.tools.migrator.tree.MTTree.*;

/**
 *
 * @author Robert Field
 */
public class MTSequenceExplicit extends MTAbstractSequenceCreator {
    private final List<MTExpression> items;

    public MTSequenceExplicit(List<MTExpression> items) {
        this.items = items;
    }

    public void accept(MTVisitor v) {
        v.visitSequenceExplicit(this);
    }

    public List<MTExpression> getItems() {
        return items;
    }

    @Override
    public int getTag() {
        return MTTag.SEQUENCE_EXPLICIT;
    }
}
