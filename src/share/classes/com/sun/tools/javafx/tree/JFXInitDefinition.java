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

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.InitDefinitionTree;
import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.source.tree.BlockTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.code.Symbol.MethodSymbol;

/**
 *
 * @author Robert Field
 */
public class JFXInitDefinition extends JFXTree implements InitDefinitionTree{
    public JCBlock body;
    public MethodSymbol sym;

    protected JFXInitDefinition(JCBlock body) {
        this.body = body;
    }
    
    public BlockTree getBody() {
        return body;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitInitDefinition(this);
    }

    @Override
    public int getTag() {
        return JavafxTag.INIT_DEF;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.INIT_DEFINITION;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitInitDefinition(this, data);
    }
}
