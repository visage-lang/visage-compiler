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

package javafx.ui.canvas;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import java.lang.Exception;
import javafx.ui.canvas.CanvasElement;

/**
 * Abstract base class for user-defined Node's. You can create your
 * own node classes by extending this class and implementing its
 * <code>composeNode()</code> method.
 */
public abstract class CompositeNode extends Node {

    // TODO MARK AS FINAL
    private attribute __composite__: Node;

    /** Abstract method which returns the content of this user-defined node */
    protected abstract function composeNode(): Node;

    public function createNode(): SGNode {
        __composite__ = this.composeNode();
        __composite__.parentCanvasElement = this;
        var n = __composite__.getNode();
        if (n == null) {
            throw new Exception("CompositeNode class {this.getClass().getName()} returned null from composeNode()");
        }
        var sggroup = new SGGroup();
        sggroup.add(n);
        //sggroup.putAttribute("f3", this);
        return sggroup; // TODO: couldn't we simply return "n" itself?
    }
}
