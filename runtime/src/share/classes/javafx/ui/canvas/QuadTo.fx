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
 * Adds a curved segment, defined by two new points, to the path by
 * drawing a Quadratic curve that intersects both the current
 * coordinates and the coordinates (x2,&nbsp;y2), using the 
 * specified point (x1,&nbsp;y1) as a quadratic parametric control
 * point.
 */

class QuadTo extends PathElement {
    public attribute smooth: Boolean;

    /** the x coordinate of the first quadratic control point */
    attribute x1: Number on replace {
        path.buildPath();
    };
    /** the y coordinate of the first quadratic control point */
    attribute y1: Number on replace {
        path.buildPath();
    };
    /** the x coordinate of the second quadratic control point */
    attribute x2: Number on replace {
        path.buildPath();
    };
    /** the y coordinate of the second quadratic control point */
    attribute y2: Number on replace {
        path.buildPath();
    };

    public function addTo(gp:GeneralPath):Void {
            if (smooth) {
                if (absolute) {
                    path.xCenter = path.currentX * 2 - path.xCenter;
                    path.yCenter = path.currentY * 2 - path.yCenter;

                    path.currentX = x2;
                    path.currentY = y2;

                    gp.quadTo(path.xCenter.floatValue(), path.yCenter.floatValue(), path.currentX.floatValue(), path.currentY.floatValue());
                } else {
                    path.xCenter = path.currentX * 2 - path.xCenter;
                    path.yCenter = path.currentY * 2 - path.yCenter;

                    path.currentX += x2;
                    path.currentY += y2;

                    gp.quadTo(path.xCenter.floatValue(), path.yCenter.floatValue(), path.currentX.floatValue(), path.currentY.floatValue());
                }

                return;
            }

            if (absolute)  {
                    path.xCenter = x1;
                    path.yCenter = y1;
                    path.currentX = x2;
                    path.currentY = y2;
            } else {
                    path.xCenter = path.currentX + x1;
                    path.yCenter = path.currentY + y1;
                    path.currentX += x2;
                    path.currentY += y2;
            }

        gp.quadTo(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue());
    }
}



