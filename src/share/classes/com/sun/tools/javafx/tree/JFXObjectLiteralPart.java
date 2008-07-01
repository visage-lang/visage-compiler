/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.code.Symbol;
import com.sun.javafx.api.JavafxBindStatus;

/**
 * In object literal  "Identifier ':' [ 'bind' 'lazy'?] expression"
 */
public class JFXObjectLiteralPart extends JFXExpression implements ObjectLiteralPartTree {
    private JFXExpression expr;
    public Name name; // Make this an Ident. Tools might need position information.
    private JFXExpression translationInit = null;
    public Symbol sym;
   /*
    * @param selector member name and class name of member
    * @param init type of attribute
    * @param sym attribute symbol
    */
    protected JFXObjectLiteralPart(
            Name name,
            JFXExpression expr,
            JavafxBindStatus bindStatus,
            Symbol sym) {
        this.name = name;
        this.expr = bindStatus == JavafxBindStatus.UNBOUND ? expr :
            new JFXBindExpression(expr, bindStatus);
        this.sym = sym;
    }

   /*
    * @param selector member name and class name of member
    * @param init type of attribute
    * @param sym attribute symbol
    */
    protected JFXObjectLiteralPart(
            Name name,
            JFXExpression expr,
            Symbol sym) {
        this.name = name;
        this.expr = expr;
        this.sym = sym;
    }
    public void accept(JavafxVisitor v) { v.visitObjectLiteralPart(this); }
    
    public javax.lang.model.element.Name getName() { return name; }
    public JFXExpression getExpression() {
        return expr instanceof JFXBindExpression ?
            ((JFXBindExpression) expr).getExpression() :
            expr; }
    public JFXExpression getMaybeBindExpression() { return expr; }
    public void setTranslationInit(JFXExpression tra) { translationInit = tra; }
    public JFXExpression getTranslationInit() { assert false : "currently not being used"; return translationInit; }
    public JavafxBindStatus getBindStatus() {
        return expr instanceof JFXBindExpression ?
            ((JFXBindExpression) expr).getBindStatus() :
            JavafxBindStatus.UNBOUND; }
    public boolean isBound()     { return getBindStatus().isBound(); }
    public boolean isUnidiBind() { return getBindStatus().isUnidiBind(); }
    public boolean isBidiBind()  { return getBindStatus().isBidiBind(); }
    public boolean isLazy()      { return getBindStatus().isLazy(); }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.OBJECT_LITERAL_PART;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.OBJECT_LITERAL_PART;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitObjectLiteralPart(this, data);
    }
}
