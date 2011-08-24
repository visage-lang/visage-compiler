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

package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

public class VisageOnReplace extends VisageTree implements OnReplaceTree {
    
    private final VisageVar firstIndex;
    private final VisageVar oldValue;
    private final VisageBlock body;
    private int endKind;
    private Kind triggerKind;
    private VisageVar lastIndex;
    private VisageVar newElements;
    private VisageVar saveVar;

    
    public VisageOnReplace(Kind triggerKind) {
        this(null, null, null, 0, null, null, null, triggerKind);
    }

    public VisageOnReplace( VisageVar oldValue, VisageBlock body, Kind triggerKind) {
        this(oldValue, null, null, 0, null, null, body, triggerKind);
    }
    
    
    public VisageOnReplace(VisageVar oldValue, VisageVar firstIndex, VisageVar lastIndex,
            int endKind, VisageVar newElements, VisageVar saveVar, VisageBlock body, Kind triggerKind) {
        this.oldValue = oldValue;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.endKind = endKind;
        this.newElements = newElements;
        this.body = body;
        this.triggerKind = triggerKind;
        this.saveVar = saveVar;
    }
    
    public void accept(VisageVisitor v) {
        v.visitOnReplace(this);
    }
    
    public VisageTag getFXTag() {
        return VisageTag.ON_REPLACE;
    }
    
    public VisageVar getOldValue() {
        return oldValue;
    }
    
    public VisageBlock getBody() {
        return body;
    }
    
    public VisageVar getFirstIndex () {
        return firstIndex;
    }

    public VisageVar getLastIndex () {
        return lastIndex;
    }

    public VisageVar getNewElements () {
        return newElements;
    }

    public VisageVar getSaveVar () {
        return saveVar;
    }

    public VisageKind getJavaFXKind() {
        return VisageKind.ON_REPLACE;
    }

    public <R, D> R accept(VisageTreeVisitor<R, D> visitor, D data) {
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
