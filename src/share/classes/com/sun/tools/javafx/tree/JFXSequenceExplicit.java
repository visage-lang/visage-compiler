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
import com.sun.tools.javafx.code.JavafxVarSymbol;

import com.sun.tools.mjavac.util.List;

/**
 *
 * @author Robert Field
 */
public class JFXSequenceExplicit extends JFXAbstractSequenceCreator implements SequenceExplicitTree {
    private final List<JFXExpression> items;

    public List<JavafxVarSymbol> boundItemsSyms;
    public List<JavafxVarSymbol> boundItemLengthSyms;
    public JavafxVarSymbol boundLowestInvalidPartSym;
    public JavafxVarSymbol boundHighestInvalidPartSym;
    public JavafxVarSymbol boundPendingTriggersSym;
    public JavafxVarSymbol boundDeltaSym;
    public JavafxVarSymbol boundChangeStartPosSym;
    public JavafxVarSymbol boundChangeEndPosSym;
    public JavafxVarSymbol boundIgnoreInvalidationsSym;
    public JavafxVarSymbol boundSizeSym;

    public JFXSequenceExplicit(List<JFXExpression> items) {
        this.items = items;
    }

    public void accept(JavafxVisitor v) {
        v.visitSequenceExplicit(this);
    }

    public List<JFXExpression> getItems() {
        return items;
    }
    
    public java.util.List<ExpressionTree> getItemList() {
        return convertList(ExpressionTree.class, items);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.SEQUENCE_EXPLICIT;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.SEQUENCE_EXPLICIT;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceExplicit(this, data);
    }
}
