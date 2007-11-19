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
import javafx.ui.canvas.SizeableCanvasElement;
import com.sun.scenario.scenegraph.SGShape;

/**
 * The <code>Rect</code> class defines a rectangle with
 * possibly rounded corners defined by a location (x,&nbsp;y), a
 * dimension (width&nbsp;&nbsp;height), and the width and height of an arc 
 * (arcWidth, arcHeight) with which to round the corners.
 */
public class Rect extends Shape, SizeableCanvasElement {
    private attribute awtrect: java.awt.geom.RoundRectangle2D.Double;

    /** The x coordinate of this rectangle's location. */
    public attribute x: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };
    /** The y coordinate of this rectangle's location. */
    public attribute y: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };
    /** The width of this rectangle. */
    public attribute width: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };
    /** The height of this rectangle. */
    public attribute height: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };
    /** The width of the corner arc of this rectangle. */
    public attribute arcWidth: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };
    /** The height of the corner arc of this rectangle. */
    public attribute arcHeight: Number on replace {
        awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        sgshape.setShape(awtrect);
    };

    public function setSize(w:Number, h:Number):Void {
        width = w;
        height = h;
    }

    protected function onSizeToFitCanvas(value:Boolean):Void {
//TODO JFXC-272:
//        if (parentCanvasElement <> null) {
//            var canvas = this.getCanvas();
//            if (value) {
//                insert this into canvas.sizeToFitList;
//           } else {
//                delete this from canvas.sizeToFitList;
//            }
//        }
    }
    public function createShape(): SGShape {
        awtrect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
        var sgshape = new SGShape();
        sgshape.setShape(awtrect);
        return sgshape;
    }

// TODO:  this method should be deleted, as getCanvas() should be resolved by Node superclass
    public function getCanvas(): Canvas {
        var n = this.parentCanvasElement;
        while (n <> null) {
            n = n.parentCanvasElement;
        }
        return cachedCanvas;
    }
}



