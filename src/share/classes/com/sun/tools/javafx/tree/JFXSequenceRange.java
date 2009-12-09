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

/**
 *
 * @author Robert Field
 */
public class JFXSequenceRange extends JFXAbstractSequenceCreator implements SequenceRangeTree {
    private final JFXExpression lower;
    private final JFXExpression upper;
    private final JFXExpression stepOrNull;
    private final boolean exclusive;

    public JFXVar boundSizeVar;

    public JFXSequenceRange(JFXExpression lower, JFXExpression upper, JFXExpression stepOrNull, boolean exclusive) {
        this.lower = lower;
        this.upper = upper;
        this.stepOrNull = stepOrNull;
        this.exclusive = exclusive;
    }

    public void accept(JavafxVisitor v) {
        v.visitSequenceRange(this);
    }

    public JFXExpression getLower() {
        return lower;
    }
    
    public JFXExpression getUpper() {
        return upper;
    }
    
    public JFXExpression getStepOrNull() {
        return stepOrNull;
    }
    
    public boolean isExclusive() {
        return exclusive;
    }
    
    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.SEQUENCE_RANGE;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.SEQUENCE_RANGE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitSequenceRange(this, data);
    }
}
