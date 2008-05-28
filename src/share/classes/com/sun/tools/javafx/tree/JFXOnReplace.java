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


/**
 *
 * @author Robert Field
 * @author Zhiqun Chen
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.JavaFXTree.JavaFXKind;
import com.sun.javafx.api.tree.JavaFXTreeVisitor;
import com.sun.javafx.api.tree.OnReplaceTree;

public class JFXOnReplace extends JFXStatement implements OnReplaceTree {
    
    private final JFXVar firstIndex;
    private final JFXVar oldValue;
    private final JCBlock body;
    private int endKind;

    private JFXVar lastIndex;
    private JFXVar newElements;

    
    public JFXOnReplace( JFXVar oldValue, JCBlock body) {
        this(oldValue, null, null, 0, null, body);
    }
    
    
    public JFXOnReplace(JFXVar oldValue, JFXVar firstIndex, JFXVar lastIndex,
            int endKind, JFXVar newElements, JCBlock body) {
        this.oldValue = oldValue;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.endKind = endKind;
        this.newElements = newElements;
        this.body = body;   
    }
    
    public void accept(JavafxVisitor v) {
        v.visitOnReplace(this);
    }

    
    public int getTag() {
        return JavafxTag.ON_REPLACE;
    }
    
    public JFXVar getOldValue() {
        return oldValue;
    }
    
    public JCBlock getBody() {
        return body;
    }
    
    public JFXVar getFirstIndex () {
        return firstIndex;
    }

    public JFXVar getLastIndex () {
        return lastIndex;
    }

    public JFXVar getNewElements () {
        return newElements;
    }

    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.ON_REPLACE;
    }

    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitOnReplace(this, data);
    }

    public int getEndKind () {
        return endKind;
    }
}
