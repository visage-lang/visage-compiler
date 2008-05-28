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

import javafx.ui.canvas.PathElement;
import java.awt.geom.GeneralPath;

/**
 * Adds a point to the path by moving to the specified
 * coordinates (x, y).
 */

public class MoveTo extends PathElement {
    /** the x coordinate of the point */
    public attribute x: Number on replace  {
        path.buildPath();
    };
    /** the y coordinate of the point */
    public attribute y: Number on replace  {
        path.buildPath();
    };   

    public function addTo(gp:GeneralPath):Void {
        if (path == null) {
            return;
        }
        if (absolute) {
            path.currentX = x;
            path.xCenter = path.currentX;

            path.currentY = y;
            path.yCenter = path.currentY;
            gp.moveTo(x.floatValue(), y.floatValue());
            path.lastMoveToX = x;
            path.lastMoveToY = y;
        } else {
            path.currentX +=  x;
            path.xCenter = path.currentX;

            path.currentY += y;
            path.yCenter = path.currentY;

            var pt = gp.getCurrentPoint();
            gp.moveTo((pt.getX() + x).floatValue(), (pt.getY() + y).floatValue());
            path.lastMoveToX = (pt.getX() + x).floatValue();
            path.lastMoveToY = (pt.getY() + y).floatValue();
        }
    }

}



