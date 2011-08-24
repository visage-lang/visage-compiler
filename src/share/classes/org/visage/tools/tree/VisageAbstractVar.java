/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import org.visage.api.VisageBindStatus;
import org.visage.api.tree.*;
import org.visage.api.tree.OnReplaceTree;

import org.visage.tools.code.VisageVarSymbol;
import com.sun.tools.mjavac.util.Name;

/**
 *
 * @author Robert Field
 */
public abstract class VisageAbstractVar extends VisageExpression implements VariableTree {
    public final Name name;
    private VisageType jfxtype;
    public final VisageModifiers mods;
    private final VisageExpression init;
    private final VisageOnReplace[] triggers;
    
    public VisageVarSymbol sym;

    protected VisageAbstractVar(
            Name name,
            VisageType jfxtype,
            VisageModifiers mods,
            VisageExpression init,
            VisageBindStatus bindStatus,
            VisageOnReplace onReplace,
            VisageOnReplace onInvalidate,
            VisageVarSymbol sym) {
        super(bindStatus);
        this.name = name;
        this.jfxtype = jfxtype;
        this.mods = mods;
        this.init = init;
        this.triggers = new VisageOnReplace[VisageOnReplace.Kind.values().length];
        this.triggers[VisageOnReplace.Kind.ONREPLACE.ordinal()] = onReplace;
        this.triggers[VisageOnReplace.Kind.ONINVALIDATE.ordinal()] = onInvalidate;
        this.sym = sym;
    }

    public abstract boolean isOverride();

    public boolean isStatic() {
        return sym.isStatic();
    }

    public VisageExpression getInitializer() {
        return init;
    }

    public VisageOnReplace getOnInvalidate() {
        return triggers[VisageOnReplace.Kind.ONINVALIDATE.ordinal()];
    }

    public OnReplaceTree getOnInvalidateTree() {
        return triggers[VisageOnReplace.Kind.ONINVALIDATE.ordinal()];
    }

    public VisageOnReplace getOnReplace() {
        return triggers[VisageOnReplace.Kind.ONREPLACE.ordinal()];
    }

    public OnReplaceTree getOnReplaceTree() {
        return triggers[VisageOnReplace.Kind.ONREPLACE.ordinal()];
    }

    public VisageOnReplace getTrigger(VisageOnReplace.Kind triggerKind) {
        return triggers[triggerKind.ordinal()];
    }

    public OnReplaceTree getTriggerTree(VisageOnReplace.Kind triggerKind) {
        return triggers[triggerKind.ordinal()];
    }

    public VisageVarSymbol getSymbol() {
        return sym;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitVariable(this, data);
    }

    public VisageKind getVisageKind() {
        return VisageKind.VARIABLE;
    }

    public Name getName() {
        return name;
    }

    public VisageTree getType() {
        return jfxtype;
    }

    public VisageType getJFXType() {
        return jfxtype;
    }

    public void setJFXType(VisageType type) {
        jfxtype = type;
    }

    public VisageModifiers getModifiers() {
        return mods;
    }
    
    public boolean isLiteralInit() {
        return init != null && init instanceof VisageLiteral;
    }
}
