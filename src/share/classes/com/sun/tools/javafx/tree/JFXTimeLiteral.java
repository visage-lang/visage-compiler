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
 * Tree node for time literals, such as "100ms" or "3m".
 * @author tball
 */
public class JFXTimeLiteral extends JFXExpression implements TimeLiteralTree {
    public JFXLiteral value;
    public Duration duration;
    
   protected JFXTimeLiteral(){
        this.value = null;
        this.duration = null;
    }

    protected JFXTimeLiteral(JFXLiteral value, Duration duration) {
        this.value = value;
        this.duration = duration;
    }

    public JavafxTag getFXTag() {
        return JavafxTag.TIME_LITERAL;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitTimeLiteral(this);
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.TIME_LITERAL;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitTimeLiteral(this, data);
    }

    public Duration getDuration() {
        return duration;
    }

    public JFXLiteral getValue() {
        return value;
    }
}
