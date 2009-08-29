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

import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.TypeTags;

/**
 * A constant value given literally.
 * @param value value representation
 */
public class JFXLiteral extends JFXExpression implements LiteralTree {

    public int typetag;
    public Object value;

    protected JFXLiteral(int typetag, Object value) {
        this.typetag = typetag;
        this.value = value;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitLiteral(this);
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        switch (typetag) {
            case TypeTags.INT:
            case TypeTags.SHORT:
            case TypeTags.BYTE:
            case TypeTags.CHAR:
                return JavaFXKind.INT_LITERAL;
            case TypeTags.LONG:
                return JavaFXKind.LONG_LITERAL;
            case TypeTags.FLOAT:
                return JavaFXKind.FLOAT_LITERAL;
            case TypeTags.DOUBLE:
                return JavaFXKind.DOUBLE_LITERAL;
            case TypeTags.BOOLEAN:
                return JavaFXKind.BOOLEAN_LITERAL;
            case TypeTags.CLASS:
                return JavaFXKind.STRING_LITERAL;
            case TypeTags.BOT:
                return JavaFXKind.NULL_LITERAL;
            default:
                throw new AssertionError("unknown literal kind " + this);
        }
    }

    public Object getValue() {
        switch (typetag) {
            case TypeTags.BOOLEAN:
                int bi = (Integer) value;
                return (bi != 0);
            case TypeTags.CHAR:
                int ci = (Integer) value;
                char c = (char) ci;
                if (c != ci) {
                    throw new AssertionError("bad value for char literal");
                }
                return c;
            default:
                return value;
        }
    }

    //@Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> v, D d) {
        return v.visitLiteral(this, d);
    }

    @Override
    public JFXLiteral setType(Type type) {
        super.setType(type);
        return this;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.LITERAL;
    }
}

