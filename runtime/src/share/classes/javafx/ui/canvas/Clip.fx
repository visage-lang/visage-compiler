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
import javafx.ui.canvas.Shape;
import com.sun.scenario.scenegraph.SGClip;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

/** 
 * A group node that provides an arbitrary clipping region.  Only the subset
 * of its content that intersects its <code>shape</code> attribute 
 * will be painted.
 */
public class Clip extends Node, Container {
    private attribute childGroup: SGGroup;

    /** Sets the shape that defines the visible region of the content*/
    public attribute shape: VisualNode on replace {
        if(shape <> null) {
            shape.parentCanvasElement = this;
            if (clipFilter <> null) {
                clipFilter.setShape(shape.getVisualNode().getShape());
            }
        }
    };
    /** The content of this node */
    public attribute content: Node[] on replace oldValue[lo..hi]=newVals {
        if (childGroup <> null) {
            for(k in [lo..hi]) { 
                childGroup.remove(lo);
            }
        }
        var ndx = lo;
        for(c in newVals) {
            c.parentCanvasElement = this;
            if (childGroup <> null) {
                childGroup.add(ndx, c.getNode());
            }
            ndx++
        }
    };
    /** If true then clipping will be antialiased. Defaults to false */
    public attribute antialias: Boolean = false on replace {
        if (clipFilter <> null) {
            clipFilter.setAntialiased(antialias);
        }
    };

    protected function createNode(): SGNode {
        clipFilter = SGClip {
                public function getBounds(transform:AffineTransform):Rectangle2D {
                    var b:Rectangle2D; 
                    if (getChild() <> null) {
                        b = getChild().getBounds(transform);
                    } else {
                        return new Rectangle2D.Double();
                    }
                    var s = getShape();
                    if (s <> null) {
                        var clipBounds = s.getBounds2D();
                        if (transform <> null) {
                            clipBounds = transform.createTransformedShape(clipBounds).getBounds2D();
                        }
                        b = clipBounds.createIntersection(b);
                    }
                    return b;
                }
                public function contains(point:Point2D):Boolean {
                      var s = getShape();
                      if (s <> null) {
                           if (not s.contains(point)) {
                               return false;
                           }
                      }
                      if (getChild() <> null) {
                          return getChild().contains(point);
                      }
                      return false;
                }
            };
        clipFilter.setShape(shape.getVisualNode().getShape());
        clipFilter.setAntialiased(antialias);
        childGroup = new SGGroup();
        clipFilter.setChild(childGroup);
        for (i in content) {
            childGroup.add(i.getNode());
        }
        return clipFilter;
    }
    private function selectIndex(n:Node):Integer {
        var a = -1;
        for (i in [0..<sizeof content]) {
            if(n == content[i]){
                a = i;
                break;
            }
        }
        return a;
    }
    public function raiseNode(n:Node):Void {
        var i = selectIndex( n );
        if (i == sizeof content -1) {
            return;
        }
        var tmp = content[i];
        content[i] = content[i+1];
        content[i+1] = tmp;
    }

    public function lowerNode(n:Node):Void {
        var i = selectIndex( n );
        if (i == 0) {
            return;
        }
        var tmp = content[i];
        content[i] = content[i-1];
        content[i-1] = tmp;
    }

    public function moveNodeToFront(n:Node):Void {
        var i = selectIndex( n );
        delete content[i];
        insert n  into content;
    }

    public function moveNodeToBack(n:Node):Void {
        var i = selectIndex( n );
        delete content[i];
        content = [n, content];
    }

}



