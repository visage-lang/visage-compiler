/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import com.sun.tools.javac.code.Symbol.*;

/**
 * A class declaration
 */
public class MTClassDeclaration extends MTStatement {

    public List<MTExpression> supertypes;
    public boolean isModuleClass = false;
    public MTModifiers mods;
    public Name name;
    public List<MTTree> defs;
    public ClassSymbol sym;

    protected MTClassDeclaration(MTModifiers mods, Name name, List<MTExpression> supertypes, List<MTTree> defs, ClassSymbol sym) {
        this.mods = mods;
        this.name = name;
        this.defs = defs;
        this.sym = sym;
        this.supertypes = supertypes;
    }

    public void accept(MTVisitor v) {
        v.visitClassDeclaration(this);
    }

    public List<MTExpression> getSupertypes() {
        return supertypes;
    }

    @Override
    public int getTag() {
        return MTTag.CLASS_DEF;
    }

    public Kind getKind() {
        return Kind.CLASS;
    }

    public MTModifiers getModifiers() {
        return mods;
    }

    public Name getSimpleName() {
        return name;
    }

    public List<MTTree> getMembers() {
        return defs;
    }
}
