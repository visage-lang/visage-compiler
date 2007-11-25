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

package javafx.ui.canvas;

import javafx.ui.canvas.Node;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;

/**
 * Non-visual node that defines a new coordinate space for its child nodes.
 */
public class Group extends Node, Container {
    /** The child members of this group */
    protected attribute sggroup: SGGroup;

    public attribute content: Node[] on insert [indx] (newValue) {
        newValue.parentCanvasElement = this;
        if (sggroup <> null) {
            sggroup.add(indx, newValue.getNode());
        }
    } on replace [indx] (oldValue) {
        var newValue = content[indx];
        if (newValue <> null) {
            newValue.parentCanvasElement = this;
            if (sggroup <> null) {
                if (oldValue <> null) {
                   try {
                       sggroup.remove(indx);
                   } catch (e) {
                       //println("remove: {e} old={old}");
                   }
                }
                sggroup.add(indx, newValue.getNode());
            }
            if (oldValue.parentCanvasElement == this) {
                oldValue.parentCanvasElement = null;
            }
        }
    }
    on delete [indx] (oldValue) {
        if (sggroup <> null and oldValue <> null) {
            try {
               sggroup.remove(indx);
            } catch (e) {
                 //println("e={e}");
            }
        }
        if (oldValue.parentCanvasElement == this) {
            oldValue.parentCanvasElement = null;
        }
    };

    public function createNode(): SGNode {
        sggroup  = new SGGroup();
        foreach (i in content) {
            sggroup.add(i.getNode());
        }
        return sggroup;
    }

    function raiseNode(n:Node) {
        /*TODO: need select, index, delete
        var i = select indexof x from x in content where x == n;
        if (i == sizeof content -1) {
            return;
        }
        delete content[i];
        insert n after content[i];
         */
    }
    function lowerNode(n:Node) {
        /*TODO: need select, index, delete
        var i = select first indexof x from x in content where x == n;
        if (i == 0) {
            return;
        }
        delete content[i];
        insert n before content[i-1];
         */
    }
    
    function moveNodeToFront(n:Node) {
        /*TODO: need select, index, delete
        if (content[sizeof content-1] == n) {
            return;
        }
        var i = 0;
        for (c in content) {
           if (c == n) {
               i = indexof c;
               break;
           }
        }
        delete content[i];
        insert n as last into content;
         */
    }

    function moveNodeToBack(n:Node) {
        /*TODO: need select, index, delete
        if (content[0] == n) {
            return;
        }
        var i = 0;
        for (c in content) {
           if (c == n) {
               i = indexof c;
               break;
           }
        }
        delete content[i];
        insert n as first into content;
         */
    }
}
