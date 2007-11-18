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
  * A shape consisting of multiple attached line segments defined by a
  * sequence of points (points) <br></br>[x1, y1, x2, y2, ..., xN, yN].
  */
public class Polyline extends Shape {
    private attribute awtPath: java.awt.geom.Path2D.Double;

    /**
     * The list of points that make up this polyline. These are alternating
     * x and y coordinates.
     */
    public attribute points: Number[]
        on insert [ndx] (pt) {
            updatePolyline();
        }
        on replace [ndx] (oldValue) {
            updatePolyline();
        }
        on delete [ndx] (pt) {
            updatePolyline();
        };

    private function updatePolyline() {
        //fix me: OPT
        if (awtPath <> null and (sizeof points % 2 == 0)) {
            awtPath.reset();
            foreach (i in [0..((sizeof points)/2-1)]) {
                var px = points[i*2+0];
                var py = points[i*2+1];
                if (i == 0) {
                    awtPath.moveTo(px, py);
                } else {
                    awtPath.lineTo(px, py);
                }
            }
            if (sgshape <> null) {
                sgshape.setShape(awtPath);
            }
        }
    }

    public function createShape(): SGShape {
        awtPath = new java.awt.geom.Path2D.Double();
        updatePolyline();
        var sgshape = new SGShape();
        sgshape.setShape(awtPath);
        return sgshape;
    }
}




