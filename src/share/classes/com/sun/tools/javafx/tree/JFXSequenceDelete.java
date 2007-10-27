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
import com.sun.javafx.api.tree.SequenceDeleteTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 *
 * @author Robert Field
 */
public class JFXSequenceDelete extends JFXStatement implements SequenceDeleteTree {
    private JCExpression sequence;
    private final JCExpression element;
    
    private JCExpression index;   // may be filled in by Attr

    public JFXSequenceDelete(JCExpression sequence, JCExpression element) {
        this.sequence = sequence;
        this.element = element;
    }

    public void accept(JavafxVisitor v) {
        v.visitSequenceDelete(this);
    }

    public JCExpression getSequence() {
        return sequence;
    }
    
    public JCExpression getElement() {
        return element;
    }
    
    public JCExpression getIndex() {
        return index;
    }
    
    public void resetSequenceAndIndex(JCExpression sequence, JCExpression index) {
        assert element == null;
        this.sequence = sequence;
        this.index = index;
    }
    
    @Override
    public int getTag() {
        return JavafxTag.DELETE;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.SEQUENCE_DELETE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceDelete(this, data);
    }
}
