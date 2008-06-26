/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.scene.geometry;

import java.lang.Math;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D.Double;

/**
* <p>A path element that forms an arc from the previous coordinates
* to the specified x and y coordinates using the specified radius. Example:</p>
* <pre><code>Path {
*   fill: Color.RED
    elements: [
        MoveTo { x: 0 y: 0 },
        ArcTo { x: 50 y: 50 radiusX: 50 radiusY: 50},
    ]
}</code></pre>
*
* <p>produces an arc like this<br/><img src="doc-files/ArcTo01.png"/></p>
* 
* <p>For more information on path elements see the {@code Path} and {@code PathElement} classes.
*
* @needsreview
*/

public class ArcTo extends PathElement {
    
    /** The horizontal radius to use for the arc. */
    public attribute radiusX: Number;
    
    /** The vertical radius to use for the arc. */
    public attribute radiusY: Number;
    
    public attribute xAxisRotation: Number;
    public attribute largeArcFlag: Boolean;
    public attribute sweepFlag: Boolean;
    
    /** The x coordinate to arc to. */
    public attribute x: Number;
    /** The y coordinate to arc to. */
    public attribute y: Number;

    // This function is nearly identical to the one written for the
    // original port of the F3 graphics/UI library: 
    // javafx.ui.canvas.ArcTo#addTo

    function addTo(gp:GeneralPath):Void { 

        // Determine current start position
        var currentPoint = gp.getCurrentPoint();
        var x0 = currentPoint.getX();
        var y0 = currentPoint.getY();
        var x = if (absolute) then this.x.doubleValue() else this.x.doubleValue() + x0;
        var y = if (absolute) then this.y else this.y + y0;
        // Compute the half distance between the current and the final point
        var dx2 = (x0 - x) / 2.0;
        var dy2 = (y0 - y) / 2.0;
        // Convert angle from degrees to radians
        var xAxisRotation = Math.toRadians(this.xAxisRotation mod 360.0);
        var cosAngle = Math.cos(xAxisRotation);
        var sinAngle = Math.sin(xAxisRotation);

        //
        // Step 1 : Compute (x1, y1)
        //
        var x1 = (cosAngle * dx2 + sinAngle * dy2);
        var y1 = (-sinAngle * dx2 + cosAngle * dy2);
        // Ensure radii are large enough
        var rx = Math.abs(this.radiusX);
        var ry = Math.abs(this.radiusY);
        var Prx = rx * rx;
        var Pry = ry * ry;
        var Px1 = x1 * x1;
        var Py1 = y1 * y1;
        // check that radii are large enough
        var radiiCheck = Px1/Prx + Py1/Pry;
        if (radiiCheck > 1) {
            rx = Math.sqrt(radiiCheck) * rx;
            ry = Math.sqrt(radiiCheck) * ry;
            Prx = rx * rx;
            Pry = ry * ry;
        }

        //
        // Step 2 : Compute (cx1, cy1)
        //
        var sign = if (largeArcFlag == sweepFlag) then -1 else 1;
        var sq = ((Prx*Pry)-(Prx*Py1)-(Pry*Px1)) / ((Prx*Py1)+(Pry*Px1));
        sq = if (sq < 0) then 0 else sq;
        var coef = (sign * Math.sqrt(sq));
        var cx1 = coef * ((rx * y1) / ry);
        var cy1 = coef * -((ry * x1) / rx);

        //
        // Step 3 : Compute (cx, cy) from (cx1, cy1)
        //
        var sx2 = (x0 + x) / 2.0;
        var sy2 = (y0 + y) / 2.0;
        var cx = sx2 + (cosAngle * cx1 - sinAngle * cy1);
        var cy = sy2 + (sinAngle * cx1 + cosAngle * cy1);

        //
        // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
        //
        var ux = (x1 - cx1) / rx;
        var uy = (y1 - cy1) / ry;
        var vx = (-x1 - cx1) / rx;
        var vy = (-y1 - cy1) / ry;
        // Compute the angle start
        var n = Math.sqrt((ux * ux) + (uy * uy));
        var p = ux; // (1 * ux) + (0 * uy)
        sign = if (uy < 0) then -1 else 1;
        var angleStart = Math.toDegrees(sign * Math.acos(p / n));

        // Compute the angle extent
        n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
        p = ux * vx + uy * vy;
        sign = if (ux * vy - uy * vx < 0) then -1 else 1;
        var angleExtent = Math.toDegrees(sign * Math.acos(p / n));
        if(not sweepFlag and angleExtent > 0) {
            angleExtent -= 360;
        } else if (sweepFlag and angleExtent < 0) {
            angleExtent += 360;
        }
        angleExtent = angleExtent mod 360;
        angleStart = angleStart mod 360;

        //
        // We can now build the resulting Arc2D in double precision
        //
        var arc = new java.awt.geom.Arc2D.Double();
        arc.x = cx - rx;
        arc.y = cy - ry;
        arc.width = rx * 2.0;
        arc.height = ry * 2.0;
        arc.start = -angleStart;
        arc.extent = -angleExtent;

        var t = AffineTransform.getRotateInstance(xAxisRotation,
                                                  arc.getCenterX(),
                                                  arc.getCenterY());
        var shape = t.createTransformedShape(arc);
        gp.append(shape, true);
    }
}
