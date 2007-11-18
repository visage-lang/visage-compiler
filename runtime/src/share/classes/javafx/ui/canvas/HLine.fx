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
 * Draws a horizontal line from the current point to x.
 */

public class HLine extends PathElement {
    public attribute x: Number on replace {
        path.buildPath();
    };

    public function addTo(gp:GeneralPath):Void {
        if (absolute) {
            path.currentX = x;
            path.yCenter = path.currentY;

            var y = gp.getCurrentPoint().getY();
            //TODO this makes no sense as x is not an array
            //foreach (i in x) {
           //     gp.lineTo(i, y);
            //}
            gp.lineTo(x, y);
        } else {
            path.currentX = path.currentX + x;
            path.xCenter = path.currentX;
            path.yCenter = path.currentY;

            var pt = gp.getCurrentPoint();
            var prev = pt.getX();
            var y = pt.getY();
            //TODO this makes no sense
            //foreach (i in x) {
            //    gp.lineTo(prev+i, y);
            //    prev = i;
            //}
            gp.lineTo(prev, y);
        }
    }

}

