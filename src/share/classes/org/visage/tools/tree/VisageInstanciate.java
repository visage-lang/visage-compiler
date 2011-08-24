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
import com.sun.tools.mjavac.code.Symbol.*;
import com.sun.tools.mjavac.util.List;

/**
 * A class declaration
 */
public class VisageInstanciate extends VisageExpression implements InstantiateTree {

    private final VisageKind fxKind;
    private final VisageExpression clazz;
    private final VisageClassDeclaration def;
    private final List<VisageExpression> args;
    private final List<VisageObjectLiteralPart> parts;
    private final List<VisageVar> localVars;
    public ClassSymbol sym;
    public Symbol constructor;
    public Symbol varDefinedByThis;

    protected VisageInstanciate(VisageKind fxKind, VisageExpression clazz, VisageClassDeclaration def, List<VisageExpression> args,
            List<VisageObjectLiteralPart> parts, List<VisageVar> localVars, ClassSymbol sym) {
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

    public VisageExpression getIdentifier() {
        return clazz;
    }
    
    public List<VisageExpression> getArgs() {
        return args;
    }

    public java.util.List<ExpressionTree> getArguments() {
        return VisageTree.convertList(ExpressionTree.class, args);
    }

    public Symbol getIdentifierSym() {
        switch (clazz.getFXTag()) {
            case IDENT:
                return ((VisageIdent) clazz).sym;
            case SELECT:
                return ((VisageSelect) clazz).sym;
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
    public List<VisageVar> getLocalvars() {
        return localVars;
    }

    public List<VisageObjectLiteralPart> getParts() {
        return parts;
    }

    public java.util.List<ObjectLiteralPartTree> getLiteralParts() {
        return VisageTree.convertList(ObjectLiteralPartTree.class, parts);
    }

    public VisageClassDeclaration getClassBody() {
        return def;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OBJECT_LITERAL;
    }

    public VisageKind getJavaFXKind() {
        return fxKind;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
        return visitor.visitInstantiate(this, data);
    }
}
