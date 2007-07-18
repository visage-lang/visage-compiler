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

package com.sun.tools.javafx.tree;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.util.Name;

/**
 * Type referencing a class
 *
 * @author Robert Field
 */
public class JFXTypeClass extends JFXType {
    private final Name className; // TODO: Make this an indent.
    ClassSymbol sym;
    
    /*
     * @param cardinality one of the cardinality constants
     */
    protected JFXTypeClass(Name className,
            int cardinality,
            ClassSymbol sym) {
        super(JavafxTag.TYPECLASS, cardinality);
        this.className = className;
        this.sym = sym;
    }
    public void accept(JavafxVisitor v) { v.visitTypeClass(this); }
    
    public Name getClassName() { return className; }
}
