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

import javafx.ui.Canvas;
import java.lang.System;
/** Abstract interface of objects that appear in a canvas. */

public abstract class CanvasElement {
    /** The containing element of this element. */
    public attribute parentCanvasElement: CanvasElement;
    /** Convenience method to obtain the containing canvas. */
    public bound function getCanvas(): Canvas {
        var n = this.parentCanvasElement;
        if (n == null) {
            null
        } else {
            n.getCanvas()
        }
    }
    
   /** Returns the canvas element that contains this element */
   public bound function getContainer(): Container{
       return getParentContainer(parentCanvasElement);
   }

   /** Returns the closest parent which is a container */
   private static function getParentContainer(p: CanvasElement): Container{
       while (p <> null) {
           if (p instanceof Container) {
               return p as Container;
           }
           p = p.parentCanvasElement;
       }
       return null;
   }

   protected function onSetCanvas(canvas:Canvas):Void {}
    /** raise this element above its next sibling */
    public function raise() {
        if (this instanceof Node) {
           getContainer().raiseNode(this as Node);
        }
    }
    /** raise this element below its previous sibling */
    public function lower() {
        if (this instanceof Node) {
           getContainer().lowerNode(this as Node);
        }
    }
    /** move this element after all of its following siblings */
    public function toFront() {
        if (this instanceof Node) {
           getContainer().moveNodeToFront(this as Node);
        }
    }
    /** move this element before all of its previous siblings */
    public function toBack() {
        if (this instanceof Node) {
           getContainer().moveNodeToBack(this as Node);
        }
    }
    public bound function hasParent(): Boolean {
        parentCanvasElement <> null;
    }
}

