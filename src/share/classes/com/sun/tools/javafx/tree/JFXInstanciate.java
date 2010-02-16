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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

import com.sun.tools.mjavac.code.Symbol;
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.util.List;

/**
 * A class declaration
 */
public class JFXInstanciate extends JFXExpression implements InstantiateTree {

    private final JavaFXKind fxKind;
    private final JFXExpression clazz;
    private final JFXClassDeclaration def;
    private final List<JFXExpression> args;
    private final List<JFXObjectLiteralPart> parts;
    private final List<JFXVar> localVars;
    public ClassSymbol sym;
    public Symbol constructor;
    public Symbol varDefinedByThis;

    protected JFXInstanciate(JavaFXKind fxKind, JFXExpression clazz, JFXClassDeclaration def, List<JFXExpression> args,
            List<JFXObjectLiteralPart> parts, List<JFXVar> localVars, ClassSymbol sym) {
        this.fxKind = fxKind;
        this.clazz = clazz;
        this.def = def;
        this.args = args;
        this.parts = parts;
        this.localVars = localVars;
        this.sym = sym;
    }

    public void accept(JavafxVisitor v) {
        v.visitInstanciate(this);
    }

    public JFXExpression getIdentifier() {
        return clazz;
    }
    
    public List<JFXExpression> getArgs() {
        return args;
    }

    public java.util.List<ExpressionTree> getArguments() {
        return JFXTree.convertList(ExpressionTree.class, args);
    }

    public Symbol getIdentifierSym() {
        switch (clazz.getFXTag()) {
            case IDENT:
                return ((JFXIdent) clazz).sym;
            case SELECT:
                return ((JFXSelect) clazz).sym;
        }
        assert false;
        return null;
    }

    public java.util.List<VariableTree> getLocalVariables() {
        return convertList(VariableTree.class, localVars);
    }

    /**
     *  For API uses only - object literals locals are desugared in a block
     *  surrounding the object literal. This is done in JavafxLower. After lowering,
     *  the compiler doesn't have to deal with them explicitly. Note that we still
     *  need to maintain access for IDE.
     */
    public List<JFXVar> getLocalvars() {
        return localVars;
    }

    public List<JFXObjectLiteralPart> getParts() {
        return parts;
    }

    public java.util.List<ObjectLiteralPartTree> getLiteralParts() {
        return JFXTree.convertList(ObjectLiteralPartTree.class, parts);
    }

    public JFXClassDeclaration getClassBody() {
        return def;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OBJECT_LITERAL;
    }

    public JavaFXKind getJavaFXKind() {
        return fxKind;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitInstantiate(this, data);
    }
}
