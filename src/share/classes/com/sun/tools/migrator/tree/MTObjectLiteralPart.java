/*
 * Copyright 1999-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.migrator.tree;

import com.sun.tools.migrator.tree.MTTree.*;

import com.sun.tools.javac.util.Name;

import com.sun.tools.javac.code.Symbol;

import com.sun.tools.javafx.code.JavafxBindStatus;

/**
 * In object literal  "Identifier ':' [ 'bind' 'lazy'?] expression"
 */
public class MTObjectLiteralPart extends MTStatement {
    public MTExpression expr;
    public Name name; // Make this an Ident. Tools might need position information.
    private JavafxBindStatus bindStatus;
    private MTExpression translationInit = null;
    public Symbol sym;
   /*
    * @param selector member name and class name of member
    * @param init type of attribute
    * @param sym attribute symbol
    */
    protected MTObjectLiteralPart(
            Name name,
            MTExpression expr,
            JavafxBindStatus bindStatus,
            Symbol sym) {
        this.name = name;
        this.expr = expr;
        this.bindStatus = bindStatus;
        this.sym = sym;
    }
    public void accept(MTVisitor v) { v.visitObjectLiteralPart(this); }
    
    public Name getName() { return name; }
    public MTExpression getExpression() { return expr; }
    public void setTranslationInit(MTExpression tra) { translationInit = tra; }
    public MTExpression getTranslationInit() { assert false : "currently not being used"; return translationInit; }
    public JavafxBindStatus getBindStatus() { return bindStatus; }
    public boolean isBound()     { return bindStatus.isBound; }
    public boolean isUnidiBind() { return bindStatus.isUnidiBind; }
    public boolean isBidiBind()  { return bindStatus.isBidiBind; }
    public boolean isLazy()      { return bindStatus.isLazy; }

    @Override
    public int getTag() {
        return MTTag.OBJECT_LITERAL_PART;
    }
}
