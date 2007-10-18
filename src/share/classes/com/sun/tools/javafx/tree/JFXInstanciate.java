/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * A class declaration
 */
public class JFXInstanciate extends JFXExpression {

    private final JCExpression clazz;
    private final JFXClassDeclaration def;
    private final List<JCExpression> args;
    private final List<JFXObjectLiteralPart> parts;
    public ClassSymbol sym;
    public Symbol constructor;

    protected JFXInstanciate(JCExpression clazz, JFXClassDeclaration def, List<JCExpression> args, List<JFXObjectLiteralPart> parts, ClassSymbol sym) {
        this.clazz = clazz;
        this.def = def;
        this.args = args;
        this.parts = parts;
        this.sym = sym;
    }

    public void accept(JavafxVisitor v) {
        v.visitInstanciate(this);
    }

    public JCExpression getIdentifier() {
        return clazz;
    }

    public List<JCExpression> getArguments() {
        return args;
    }

    public Symbol getIdentifierSym() {
        switch (clazz.getTag()) {
            case JCTree.IDENT:
                return ((JCIdent) clazz).sym;
            case JCTree.SELECT:
                return ((JCFieldAccess) clazz).sym;
        }
        assert false;
        return null;
    }

    public List<JFXObjectLiteralPart> getParts() {
        return parts;
    }

    public JFXClassDeclaration getClassBody() {
        return def;
    }

    @Override
    public int getTag() {
        return JavafxTag.OBJECT_LITERAL;
    }
}