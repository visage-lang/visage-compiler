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

import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * A function definition.
 */
public class MTOperationDefinition extends MTStatement {
    public MTModifiers mods;
    public Name name;
    public MTOperationValue operation;
    public MethodSymbol sym;

    protected MTOperationDefinition(
            MTModifiers mods, 
            Name name, 
            MTType rettype, 
            List<MTVar> funParams, 
            MTBlockExpression bodyExpression) {
        this.mods = mods;
        this.name = name;
        this.operation = new MTOperationValue(rettype, funParams, bodyExpression);
    }
    
    public MTBlockExpression getBodyExpression() {
        return operation.getBodyExpression();
    }
    public MTModifiers getModifiers() { return mods; }
    public Name getName() { return name; }
    public MTType getJFXReturnType() { return operation.rettype; }
    public List<MTVar> getParameters() { return operation.funParams; }

    public void accept(MTVisitor v) {
        v.visitFunctionDefinition(this);
    }

    @Override
    public int getTag() {
        return MTTag.FUNCTION_DEF;
    }
}
