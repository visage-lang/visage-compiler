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

import javafx.ui.canvas.PathElement;
import java.awt.geom.GeneralPath;

/**
 * Adds a curved segment, defined by three new points, to the path by
 * drawing a B&eacute;zier curve that intersects both the current
 * coordinates and the coordinates (x3,&nbsp;y3), using the    
 * specified points (x1,&nbsp;y1) and (x2,&nbsp;y2) as
 * B&eacute;zier control points.
 */

public class CurveTo extends PathElement {
    public attribute smooth: Boolean;

    /** 
     * the x coordinate of the first B&eacute;ezier
     * control point
     */
    public attribute x1: Number on replace {
        path.buildPath();
    };

    /** 
     * the y coordinate of the first B&eacute;ezier
     * control point
     */
    public attribute y1: Number on replace {
        path.buildPath();
    };

    /** 
     * the x coordinate of the second B&eacute;ezier
     * control point
     */
    public attribute x2: Number on replace {
        path.buildPath();
    };

    /** 
     * the y coordinate of the second B&eacute;ezier
     * control point
     */
    public attribute y2: Number on replace {
        path.buildPath();
    };

    /** 
     * the x coordinate of the final endpoint
     */
    public attribute x3: Number on replace {
        path.buildPath();
    };

    /** 
     * the y coordinate of the final endpoint
     */
    public attribute y3: Number on replace {
        path.buildPath();
    };

    public function addTo(gp:GeneralPath):Void {
        if (smooth) {
            if (absolute) {
                gp.curveTo((path.currentX * 2 - path.xCenter).floatValue(),
                           (path.currentY * 2 - path.yCenter).floatValue(),
                           x2.floatValue(), y2.floatValue(), x3.floatValue(), y3.floatValue());

                path.xCenter = x2;
                path.yCenter = y2;
                path.currentX = x3;
                path.currentY = y3;
            } else {
                gp.curveTo((path.currentX * 2 - path.xCenter).floatValue(),
                           (path.currentY * 2 - path.yCenter).floatValue(),
                           (path.currentX + x2).floatValue(), 
                           (path.currentY + y2).floatValue(), 
                           (path.currentX + x3).floatValue(), 
                           (path.currentY + y3).floatValue());

                path.xCenter = path.currentX + x2;
                path.yCenter = path.currentY + y2;

                path.currentX = path.currentX + x3;
                path.currentY = path.currentY + y3;
            }

            return;
        }

        // Not smooth
        if (absolute) {
            path.xCenter = x2;
            path.yCenter = y2;

            path.currentX = x3;
            path.currentY = y3;

            gp.curveTo(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue(), x3.floatValue(), y3.floatValue());
        } else {
            path.xCenter = path.currentX + x2;
            path.yCenter = path.currentY + y2;

            path.currentX = path.currentX + x3;
            path.currentY = path.currentY + y3;

            var pt = gp.getCurrentPoint();
            gp.curveTo((x1 + pt.getX()).floatValue(), 
                       (y1 + pt.getY()).floatValue(),
                       (x2 + pt.getX()).floatValue(),
                       (y2 + pt.getY()).floatValue(),
                       (x3 + pt.getX()).floatValue(),
                       (y3 + pt.getY()).floatValue());
        }
    }
}



