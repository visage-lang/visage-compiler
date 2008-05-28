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

import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.SequenceRangeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 *
 * @author Robert Field
 */
public class JFXSequenceRange extends JFXAbstractSequenceCreator implements SequenceRangeTree {
    private final JCExpression lower;
    private final JCExpression upper;
    private final JCExpression stepOrNull;
    private final boolean exclusive;

    public JFXSequenceRange(JCExpression lower, JCExpression upper, JCExpression stepOrNull, boolean exclusive) {
        this.lower = lower;
        this.upper = upper;
        this.stepOrNull = stepOrNull;
        this.exclusive = exclusive;
    }

    public void accept(JavafxVisitor v) {
        v.visitSequenceRange(this);
    }

    public JCExpression getLower() {
        return lower;
    }
    
    public JCExpression getUpper() {
        return upper;
    }
    
    public JCExpression getStepOrNull() {
        return stepOrNull;
    }
    
    public boolean isExclusive() {
        return exclusive;
    }
    
    @Override
    public int getTag() {
        return JavafxTag.SEQUENCE_RANGE;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.SEQUENCE_RANGE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceRange(this, data);
    }
}
