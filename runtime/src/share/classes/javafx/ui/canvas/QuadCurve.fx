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
 * The <code>QuadCurve</code> class defines a quadratic parametric curve
 * segment by means of a start point (x1, y1), a control point (ctrlx, ctrly), 
 * and an end point (x2, y2).
 */
public class QuadCurve extends Shape {
    private attribute awtQuadCurve: java.awt.geom.QuadCurve2D.Double;

    /** The x coordinate of the start point of this quad curve. */
    public attribute x1: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };
    /** The y coordinate of the start point of this quad curve. */
    public attribute y1: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };

    /** The x coordinate of the control point of this quad curve. */
    public attribute ctrlx: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };
    /** The y coordinate of the control point of this quad curve. */
    public attribute ctrly: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };

    /** The x coordinate of the end point of this quad curve. */
    public attribute x2: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };
    /** The y coordinate of the end point of this quad curve. */
    public attribute y2: Number on replace {
        awtQuadCurve.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
        sgshape.setShape(awtQuadCurve);
    };

    public function createShape(): SGShape {
        awtQuadCurve = new java.awt.geom.QuadCurve2D.Double(x1, y1, ctrlx, ctrly, x2, y2);
        var sgshape = new SGShape();
        sgshape.setShape(awtQuadCurve);
        return sgshape;
    }
}

