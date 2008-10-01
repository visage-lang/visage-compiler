/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.code;

import static com.sun.tools.javac.code.Kinds.TYP;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Name;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents JavaFX package symbol
 */
public class JavafxPackageSymbol extends PackageSymbol {
    // all sub-packages of this package -- not just immediate ones
    private Set<PackageSymbol> sub_packages_field = new HashSet<PackageSymbol>();
    public JavafxPackageSymbol(Name name, Type type, Symbol owner) {
        super(name, type, owner);
    }

    public JavafxPackageSymbol(Name name, Symbol owner) {
        super(name, owner);
    }

    public Set<PackageSymbol> getSubpackages() {
        return sub_packages_field;
    }
    
    public void setSubpackages(Set<PackageSymbol> subpackages) {
        this.sub_packages_field = subpackages;
    }
    
    public void addSubpackage(PackageSymbol psym) {
        sub_packages_field.add(psym);
    }
    
    public void enterSubpackageMembers(Scope toScope) {
        members();
        if (sub_packages_field != null) {
            for (PackageSymbol ps : sub_packages_field) {
                final Scope subPkgScope = ps.members();
                for (Scope.Entry e = subPkgScope.elems; e != null; e = e.sibling) {
                    if (e.sym.kind == TYP && !toScope.includes(e.sym)) {
                        toScope.enter(e.sym, subPkgScope);
                    }
                }
            }
        }
    }
}