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
 * Draws a closed polygon defined by a sequence of points [x1, y1, x2, y2,..., xN, yN].
 * The figure is automatically closed by drawing a line connecting
 * the final point to the first point, if those points are different.
 */
public class Polygon extends Shape {
    
    // TODO MARK AS FINAL
    private attribute awtPath: java.awt.geom.GeneralPath;

    /** The list of points that define this polygon */
    public attribute points: Number[] on replace {
        updatePolygon();
    };

    private function updatePolygon() {
        //fix me: OPT
        if (awtPath <> null and (sizeof points % 2 == 0)) {
            awtPath.reset();
            for (i in [0..((sizeof points)/2-1)]) {
                var px = points[i*2+0];
                var py = points[i*2+1];
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


    public function createShape(): SGShape {
        awtPath = new java.awt.geom.GeneralPath();
        updatePolygon();
        var sgshape = new SGShape();
        sgshape.setShape(awtPath);
        return sgshape;
    }
}






