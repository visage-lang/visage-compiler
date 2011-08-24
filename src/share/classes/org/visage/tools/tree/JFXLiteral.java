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

import com.sun.tools.mjavac.code.Type;
import com.sun.tools.mjavac.code.TypeTags;

/**
 * A constant value given literally.
 * @param value value representation
 */
public class VisageLiteral extends VisageExpression implements LiteralTree {

    public int typetag;
    public Object value;

    protected VisageLiteral(int typetag, Object value) {
        this.typetag = typetag;
        this.value = value;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitLiteral(this);
    }

    @Override
    public VisageKind getJavaFXKind() {
        switch (typetag) {
            case TypeTags.INT:
            case TypeTags.SHORT:
            case TypeTags.BYTE:
            case TypeTags.CHAR:
                return VisageKind.INT_LITERAL;
            case TypeTags.LONG:
                return VisageKind.LONG_LITERAL;
            case TypeTags.FLOAT:
                return VisageKind.FLOAT_LITERAL;
            case TypeTags.DOUBLE:
                return VisageKind.DOUBLE_LITERAL;
            case TypeTags.BOOLEAN:
                return VisageKind.BOOLEAN_LITERAL;
            case TypeTags.CLASS:
                return VisageKind.STRING_LITERAL;
            case TypeTags.BOT:
                return VisageKind.NULL_LITERAL;
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
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitLiteral(this, d);
    }

    @Override
    public VisageLiteral setType(Type type) {
        super.setType(type);
        return this;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.LITERAL;
    }
}

