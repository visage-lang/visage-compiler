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
import java.lang.Object;

/**
 * Non-visual node that defines a new coordinate space for its child nodes.
 */
public class Group extends Node, Container {
   /** The child members of this group */
    // TODO MARK AS FINAL
    protected attribute sggroup: SGGroup;

    public attribute content: Node[]  on replace oldValue[lo..hi]=newVals {
        for(n in oldValue[lo..hi]) { 
            if (sggroup <> null) {
                try {
                   sggroup.remove(lo);
                } catch (e) {
                     //println("e={e}");
                }
            }
            if (n.parentCanvasElement == (this as CanvasElement)) {
                n.parentCanvasElement = null;
            }
        }
        var ndx = lo;
        for(c in newVals) {
            c.parentCanvasElement = this as CanvasElement;
            if (sggroup <> null) {
                sggroup.add(ndx, c.getNode());
            }
            ndx++
        }
    };
    

    public function createNode(): SGNode {
        sggroup  = new SGGroup();
        for (i in content) {
            if (i <> null) {
                i.parentCanvasElement = this as CanvasElement;
                sggroup.add(i.getNode());
            }
        }
        return sggroup;
    }

    public function raiseNode(n:Node):Void {
        var seq = for(x in content where x == n) indexof x;
        if(sizeof seq > 0) {
            var i = seq[0];
            if(i < sizeof content - 1) {
                var tmp = content[i+1];
                content[i+1] = content[i];
                content[i] = tmp;  
            }
        }
    }
    public function lowerNode(n:Node):Void {
        var seq = for(x in content where x == n) indexof x;
        if(sizeof seq > 0) {
            var i = seq[0];
            if( i > 0) {
                var tmp = content[i-1];
                content[i-1] = content[i];
                content[i] = tmp;  
            }
        }
    }
    
    public function moveNodeToFront(n:Node):Void {
        var seq = for(x in content where x == n) indexof x;
        if(sizeof seq > 0) {
            delete content[seq[0]];
        }        
        insert n into content;
    }

    public function moveNodeToBack(n:Node):Void {
        var seq = for(x in content where x == n) indexof x;
        if(sizeof seq > 0) {
            delete content[seq[0]];
        }        
        insert n before content[0];       
    }
}
