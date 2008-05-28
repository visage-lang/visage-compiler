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

import com.sun.tools.migrator.tree.MTTree.*;

import com.sun.tools.javac.code.Type;

/**
 *
 * @author Robert Field
 */
public abstract class MTAbstractOnChange extends MTStatement {
    private final MTVar index;
    private final MTVar oldValue;
    private final MTBlock body;
    
    public Type elementType = null;

    public MTAbstractOnChange(MTVar index, MTVar oldValue, MTBlock body) {
        this.index = index;
        this.oldValue = oldValue;
        this.body = body;
    }

    public MTVar getIndex() {
        return index;
    }
    
    public MTVar getOldValue() {
        return oldValue;
    }
    
    public MTBlock getBody() {
        return body;
    }
}
