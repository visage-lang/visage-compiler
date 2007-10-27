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
import com.sun.javafx.api.tree.SequenceIndexedTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 *
 * @author Robert Field
 */
public class JFXSequenceIndexed extends JFXExpression implements SequenceIndexedTree {
    private final JCExpression sequence;
    private final JCExpression index;

    public JFXSequenceIndexed(JCExpression sequence, JCExpression index) {
        this.sequence = sequence;
        this.index = index;
    }

    public void accept(JavafxVisitor v) {
        v.visitSequenceIndexed(this);
    }

    public JCExpression getSequence() {
        return sequence;
    }
    
    public JCExpression getIndex() {
        return index;
    }
    
    @Override
    public int getTag() {
        return JavafxTag.SEQUENCE_INDEXED;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.SEQUENCE_INDEXED;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceIndexed(this, data);
    }
}
