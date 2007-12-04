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


import com.sun.scenario.scenegraph.SGShape;

/**
 * The <code>Ellipse</code> class describes an ellipse that is defined
 * by a center point (cx, cy), a horizontal radius (radiusX), and a vertical
 * radius (radiusY).
 */
public class Ellipse extends Shape {
    private attribute awtEllipse: java.awt.geom.Ellipse2D.Double;

    /** the x coordinate of the center point of this ellipse.*/
    public attribute cx: Number on replace {
        var x = cx - radiusX;
        var y = cy - radiusY;
        var width = 2*radiusX;
        var height = 2*radiusY;
        if (awtEllipse <> null)
            awtEllipse.setFrame(x, y, width, height);
        if (sgshape <> null)
            sgshape.setShape(awtEllipse);
    };
    /** the y coordinate of the center point of this ellipse.*/
    public attribute cy: Number on replace {
        var x = cx - radiusX;
        var y = cy - radiusY;
        var width = 2*radiusX;
        var height = 2*radiusY;
        if (awtEllipse <> null)
            awtEllipse.setFrame(x, y, width, height);
        if (sgshape <> null)
            sgshape.setShape(awtEllipse);
    };
    /** the horizontal radius of this ellipse.*/
    public attribute radiusX: Number on replace {
        var x = cx - radiusX;
        var y = cy - radiusY;
        var width = 2*radiusX;
        var height = 2*radiusY;
        if (awtEllipse <> null)
            awtEllipse.setFrame(x, y, width, height);
        if (sgshape <> null)
            sgshape.setShape(awtEllipse);
    };
    /** the vertical radius of this ellipse.*/
    public attribute radiusY: Number on replace {
        var x = cx - radiusX;
        var y = cy - radiusY;
        var width = 2*radiusX;
        var height = 2*radiusY;
        if (awtEllipse <> null)
            awtEllipse.setFrame(x, y, width, height);
        if (sgshape <> null)
            sgshape.setShape(awtEllipse);
    };

    public function createShape(): SGShape {
        var x = cx - radiusX;
        var y = cy - radiusY;
        var width = 2*radiusX;
        var height = 2*radiusY;
        awtEllipse = new java.awt.geom.Ellipse2D.Double(x, y, width, height);
        var sgshape = new SGShape();
        sgshape.setShape(awtEllipse);
        return sgshape;
    }
}


