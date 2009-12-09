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

/**
 *
 * @author Robert Field
 * @author Zhiqun Chen
 */

package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;

public class JFXOnReplace extends JFXTree implements OnReplaceTree {
    
    private final JFXVar firstIndex;
    private final JFXVar oldValue;
    private final JFXBlock body;
    private int endKind;
    private Kind triggerKind;
    private JFXVar lastIndex;
    private JFXVar newElements;
    private JFXVar saveVar;

    
    public JFXOnReplace(Kind triggerKind) {
        this(null, null, null, 0, null, null, null, triggerKind);
    }

    public JFXOnReplace( JFXVar oldValue, JFXBlock body, Kind triggerKind) {
        this(oldValue, null, null, 0, null, null, body, triggerKind);
    }
    
    
    public JFXOnReplace(JFXVar oldValue, JFXVar firstIndex, JFXVar lastIndex,
            int endKind, JFXVar newElements, JFXVar saveVar, JFXBlock body, Kind triggerKind) {
        this.oldValue = oldValue;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.endKind = endKind;
        this.newElements = newElements;
        this.body = body;
        this.triggerKind = triggerKind;
        this.saveVar = saveVar;
    }
    
    public void accept(JavafxVisitor v) {
        v.visitOnReplace(this);
    }
    
    public JavafxTag getFXTag() {
        return JavafxTag.ON_REPLACE;
    }
    
    public JFXVar getOldValue() {
        return oldValue;
    }
    
    public JFXBlock getBody() {
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

    public JFXVar getSaveVar () {
        return saveVar;
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

    public Kind getTriggerKind () {
        return triggerKind;
    }

    public enum Kind {
        ONREPLACE("on replace"),
        ONINVALIDATE("on invalidate");

        String displayName;

        Kind(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return displayName;
        }
    }
}
