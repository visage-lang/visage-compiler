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

import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.util.Name;

 /**
 * Selects through packages and classes
 * @param selected selected Tree hierarchy
 * @param selector name of field to select thru
 * @param sym symbol of the selected class
 */
public class VisageSelect extends VisageExpression implements MemberSelectTree {

    public VisageExpression selected;
    public Name name;
    public Symbol sym;
    public boolean nullCheck;

    public VisageVar boundSize;

    protected VisageSelect(VisageExpression selected, Name name, Symbol sym, boolean nullCheck) {
        this.selected = selected;
        this.name = name;
        this.sym = sym;
        this.nullCheck = nullCheck;
    }

    @Override
    public void accept(VisageVisitor v) {
        v.visitSelect(this);
    }

    @Override
    public VisageKind getVisageKind() {
        return VisageKind.MEMBER_SELECT;
    }

    public VisageExpression getExpression() {
        return selected;
    }

    //@Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitMemberSelect(this, d);
    }

    public Name getIdentifier() {
        return name;
    }

    @Override
    public VisageTag getVisageTag() {
        return VisageTag.SELECT;
    }
}

