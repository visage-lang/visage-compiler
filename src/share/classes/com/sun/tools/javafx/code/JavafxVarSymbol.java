/*
 * Copyright 1999-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.code;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.Name;

/**
 *
 * @author llitchev
 */
public class JavafxVarSymbol extends VarSymbol {
    private final int javafxVarType;
    private final boolean isBound;
    private final boolean isLazy;
    private Type realType;
    private boolean shouldMorph = false;
    private boolean haveDeterminedMorphability = false;
    private boolean isBoundTo = false;
    private boolean isAssignedTo = false;
    
    public static final int TYPE_KIND_OBJECT = 0;
    public static final int TYPE_KIND_DOUBLE = 1;
    public static final int TYPE_KIND_BOOLEAN = 2;
    public static final int TYPE_KIND_INT = 3;
    public static final int TYPE_KIND_COUNT = 4;
    
    private int typeKind;
    
    /** Creates a new instance of JavafxVarSymbol */
    public JavafxVarSymbol(long flags,
            Name name,
            int javafxVarType,
            Type type,
            boolean isBound,
            boolean isLazy,
            Symbol owner) {
        super(flags, name, type, owner);
        this.javafxVarType = javafxVarType;
        this.isBound = isBound;
        this.isLazy = isLazy;
        this.realType = type;
    }
    
    public int getJavafxVarType() {
        return javafxVarType;
    }
    public boolean isBound() { return isBound; }
    public boolean isLazy() { return isLazy; }
    public void markShouldMorph() { shouldMorph = true; }
    public void markDeterminedMorphability() { haveDeterminedMorphability = true; }
    public boolean shouldMorph() { return shouldMorph; }
    public boolean haveDeterminedMorphability() { return haveDeterminedMorphability; }
    public Type getRealType() { return realType; }
    public void setRealType(Type t) { realType = t; }
    public Type getUsedType() { return type; }
    public void setUsedType(Type usedType) { type = usedType; }
    
    public boolean isBoundTo() { return isBoundTo; }
    public boolean isAssignedTo() { return isAssignedTo; }
    public void markBoundTo() { this.isBoundTo = true; }
    public void markAssignedTo() { this.isAssignedTo = true; }

    public int getTypeKind() { return typeKind; }
    public void setTypeKind(int typeKind) { this.typeKind = typeKind; }
    
}
