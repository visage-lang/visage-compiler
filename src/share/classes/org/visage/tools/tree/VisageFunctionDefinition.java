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

import com.sun.tools.mjavac.code.Symbol.MethodSymbol;
import com.sun.tools.mjavac.util.List;
import com.sun.tools.mjavac.util.Name;
import org.visage.tools.code.VisageFlags;

/**
 * A function definition.
 */
public class VisageFunctionDefinition extends VisageExpression implements FunctionDefinitionTree {

    public final VisageModifiers mods;
    public final Name name;
    public final VisageFunctionValue operation;
    public MethodSymbol sym;

    public VisageFunctionDefinition(
            VisageModifiers mods,
            Name name,
            VisageFunctionValue operation) {
        this.mods = mods;
        this.name = name;
        this.operation = operation;
    }

    protected VisageFunctionDefinition(
            VisageModifiers mods,
            Name name,
            VisageType rettype,
            List<VisageVar> funParams,
            VisageBlock bodyExpression) {
        this.mods = mods;
        this.name = name;
        this.operation = new VisageFunctionValue(rettype, funParams, bodyExpression);
    }

    public boolean isStatic() {
        return sym.isStatic();
    }

    public VisageBlock getBodyExpression() {
        return operation.getBodyExpression();
    }

    public VisageModifiers getModifiers() {
        return mods;
    }

    public boolean isBound() {
        return (mods.flags & VisageFlags.BOUND) != 0;
    }

    public Name getName() {
        return name;
    }

    public VisageType getJFXReturnType() {
        return operation.rettype;
    }

    public List<VisageVar> getParams() {
        return operation.funParams;
    }

    public VisageFunctionValue getFunctionValue() {
        return operation;
    }

    public void accept(VisageVisitor v) {
        v.visitFunctionDefinition(this);
    }

    @Override
    public VisageTag getFXTag() {
        return VisageTag.FUNCTION_DEF;
    }

    public VisageKind getVisageKind() {
        return VisageKind.FUNCTION_DEFINITION;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitFunctionDefinition(this, data);
    }
}
