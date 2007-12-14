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
 * Draws a vertical line from the current point to y.
 */

public class VLine extends PathElement {
    public attribute y: Number on replace  {
        path.buildPath();
    };

    public function addTo(gp:GeneralPath):Void {
        if (absolute) {
            path.xCenter = path.currentX;
            path.currentY = y;
            path.yCenter = path.currentY;

            var x = gp.getCurrentPoint().getX();
            //TODO THis does not make sense as y is not an array
            //for (i in y) {
            //    gp.lineTo(x, i);
            //}
            gp.lineTo(x.floatValue(), y.floatValue());
        } else {
            path.xCenter = path.currentX;
            path.currentY = path.currentY +y;
            path.yCenter = path.currentY;

            var pt = gp.getCurrentPoint();
            var prev = pt.getY();
            var x = pt.getX();
            //TODO THis does not make sense as y is not an array
            //for (i in y) {
             //   gp.lineTo(x, prev + i);
             //   prev = i;
            //}
            gp.lineTo(x.floatValue(), (prev+y).floatValue());
        }
    }
}


