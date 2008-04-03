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

import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.FunctionDefinitionTree;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javafx.code.JavafxFlags;

/**
 * A function definition.
 */
public class JFXFunctionDefinition extends JFXStatement implements FunctionDefinitionTree {
    public final JCModifiers mods;
    public final Name name;
    public final JFXFunctionValue operation;
    public MethodSymbol sym;

    public JFXFunctionDefinition(
            JCModifiers mods, 
            Name name,
            JFXFunctionValue operation) {
        this.mods = mods;
        this.name = name;
        this.operation = operation;
    }

    protected JFXFunctionDefinition(
            JCModifiers mods, 
            Name name, 
            JFXType rettype, 
            List<JFXVar> funParams, 
            JFXBlockExpression bodyExpression) {
        this.mods = mods;
        this.name = name;
        this.operation = new JFXFunctionValue(rettype, funParams, bodyExpression);
    }
    
    public JFXBlockExpression getBodyExpression() {
        return operation.getBodyExpression();
    }
    public JCModifiers getModifiers() { return mods; }
    public boolean isBound() { return (mods.flags & JavafxFlags.BOUND) != 0; }
    public Name getName() { return name; }
    public JFXType getJFXReturnType() { return operation.rettype; }
    public List<JFXVar> getParameters() { return operation.funParams; }
    public JFXFunctionValue getFunctionValue() {
        return operation;
    }

    public void accept(JavafxVisitor v) {
        v.visitFunctionDefinition(this);
    }

    @Override
    public void accept(Visitor v) {
        if (v instanceof JavafxVisitor) {
            this.accept((JavafxVisitor)v);
        } else if (v instanceof Pretty) {
            JavafxPretty.visitFunctionDefinition((Pretty) v, this);
        } else {
            assert false;
        }
    }

    @Override
    public int getTag() {
        return JavafxTag.FUNCTION_DEF;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.FUNCTION_DEFINITION;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitFunctionDefinition(this, data);
    }
}
