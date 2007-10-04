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

import com.sun.tools.migrator.tree.MTTree.*;

/**
 * Abstract base for types
 *
 * @author Robert Field
 */
public abstract class MTType extends MTExpression {
//    public static final int CARDINALITY_OPTIONAL  = 0;  // default: zero or one
    public static final int CARDINALITY_SINGLETON = 1;  // one
//    public static final int CARDINALITY_SOME      = 2;  // '+' one or more
    public static final int CARDINALITY_ANY       = 3;  // '[]' zero or more
    public static final int CARDINALITY_UNKNOWN   = 4;  // user didn't specify type info
    
    private final int cardinality;
    
    /*
     * @param cardinality one of the cardinality constants
     */
    protected MTType(int cardinality) {
        this.cardinality = cardinality;
    }
    
    public int getCardinality() { return cardinality; }
}
