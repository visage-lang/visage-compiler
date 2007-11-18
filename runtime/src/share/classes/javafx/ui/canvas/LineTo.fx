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

import java.awt.geom.GeneralPath;

/**
 * Adds a point to the path by drawing a straight line from the
 * current coordinates to the new specified coordinates (x, y).
 */

public class LineTo extends PathElement {
    /** the x coordinate of the point */
    public attribute x: Number on replace {
        path.buildPath();
    };
    /** the y coordinate of the point */
    public attribute y: Number on replace {
        path.buildPath();
    };

    public function addTo(gp:GeneralPath):Void {
        if (absolute) {
            path.currentX = x;
            path.xCenter = path.currentX;

            path.currentY = y;
            path.yCenter = path.currentY;

            gp.lineTo(x, y);
        } else {
            path.currentX = path.currentX +x;
            path.xCenter = path.currentX;

            path.currentY = path.currentY + y;
            path.yCenter = path.currentY;

            var pt = gp.getCurrentPoint();
            gp.lineTo(pt.getX() + x, pt.getY() + y);
        }
    }
}


