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

import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javafx.code.JavafxBindStatus;

/**
 * An attribute declaration.
 */
public class JFXAttributeDefinition extends JFXAbstractAttribute {

    public JCExpression init;
    public JavafxBindStatus bindStatus;
    public JCBlock onChange;

    protected JFXAttributeDefinition(
            JCModifiers modifiers, 
            Name name, 
            JFXType type, 
            JFXMemberSelector inverseOrNull, 
            JCExpression orderingOrNull, 
            JavafxBindStatus bindStatus, 
            JCExpression init,
            JCBlock onChange) {
        super(modifiers, name, type, inverseOrNull, orderingOrNull);
        this.bindStatus = bindStatus;
        this.init = init;
        this.onChange = onChange;
    }

    public void accept(JavafxVisitor v) {
        v.visitAttributeDefinition(this);
    }

    public JCExpression getInitializer() {
        return init;
    }

    public JavafxBindStatus getBindStatus() {
        return bindStatus;
    }

    public JCBlock getOnChangeBlock() {
        return onChange;
    }

    public boolean isBound() {
        return bindStatus.isBound;
    }

    public boolean isUnidiBind() {
        return bindStatus.isUnidiBind;
    }

    public boolean isBidiBind() {
        return bindStatus.isBidiBind;
    }

    public boolean isLazy() {
        return bindStatus.isLazy;
    }

    @Override
    public int getTag() {
        return JavafxTag.ATTRIBUTEDEF;
    }
}
