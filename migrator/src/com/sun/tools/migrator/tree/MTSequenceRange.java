/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import  com.sun.tools.migrator.tree.MTTree.*;

/**
 *
 * @author Robert Field
 */
public class MTSequenceRange extends MTAbstractSequenceCreator {
    private final MTExpression lower;
    private final MTExpression upper;

    public MTSequenceRange(MTExpression lower, MTExpression upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public void accept(MTVisitor v) {
        v.visitSequenceRange(this);
    }

    public MTExpression getLower() {
        return lower;
    }
    
    public MTExpression getUpper() {
        return upper;
    }
    
    @Override
    public int getTag() {
        return MTTag.SEQUENCE_RANGE;
    }
}
