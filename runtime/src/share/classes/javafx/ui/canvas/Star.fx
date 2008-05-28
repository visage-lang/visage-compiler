/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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


import java.lang.Math;
import com.sun.scenario.scenegraph.SGShape;

/**
 * A shape which describes a star by means of a center point (cx, cy),
 * an inner radius (rin), an outer radius (rout), a count of points
 * (points), and an angle at which to draw the first point (startAngle).
 */
public class Star extends Shape {

    // TODO MARK AS FINAL
    private attribute awtPath: java.awt.geom.GeneralPath;
    
    private function updatePolygon() {
        if (awtPath <> null) {
            awtPath.reset();
            var angle = Math.toRadians(startAngle);
            var dth = Math.PI/points;
            var n = (points*2-1).intValue();
            for (i in [0..n] ) {
                var px = cx + (if (i %2 == 0) then rin else rout)*Math.cos(angle+dth* i);
                var py = cy + (if (i %2 == 0) then rin else rout)*Math.sin(angle+dth* i);
                if (i == 0) {
                    awtPath.moveTo(px.floatValue(), py.floatValue());
                } else {
                    awtPath.lineTo(px.floatValue(), py.floatValue());
                }
            }
            awtPath.closePath();
            if (sgshape <> null) {
                sgshape.setShape(awtPath);
            }
        }
    }

    /** The x coordinate of the center point of this star. */
    public attribute cx: Number on replace {
        this.updatePolygon();
    };
    /** The y coordinate of the center point of this star. */
    public attribute cy: Number on replace {
        this.updatePolygon();
    };
    /** The inner radius of this star. */
    public attribute rin: Number on replace {
        this.updatePolygon();
    };
    /** The outer radius of this star. */
    public attribute rout: Number on replace {
        this.updatePolygon();
    };
    /** The number of points this star has.*/
    public attribute points: Number on replace {
        this.updatePolygon();
    };
    /** The angle, in degress,  at which to draw the first point.  */
    public attribute startAngle: Number on replace { // in degrees
        this.updatePolygon();
    }; 


    public function createShape(): SGShape {
        awtPath = new java.awt.geom.GeneralPath();
        this.updatePolygon();
        var sgshape = new SGShape();
        sgshape.setShape(awtPath);
        return sgshape;
    }
}

