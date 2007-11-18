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
 * Creates a line between the points <code>(x1,&nbsp;y1)</code> 
 * and <code>(x2,&nbsp;y2)</code>.
 */
public class Line extends Shape {
    private attribute awtLine: java.awt.geom.Line2D.Double;

    /** The first point's <i>x</i> coordinate. */
    public attribute x1: Number on replace {
        awtLine.setLine(x1, y1, x2, y2);
        sgshape.setShape(awtLine);
    };
    /** The first point's <i>y</i> coordinate. */
    public attribute y1: Number on replace {
        awtLine.setLine(x1, y1, x2, y2);
        sgshape.setShape(awtLine);
    };
    /** The second point's <i>x</i> coordinate. */
    public attribute x2: Number on replace {
        awtLine.setLine(x1, y1, x2, y2);
        sgshape.setShape(awtLine);
    };
    /** The second point's <i>y</i> coordinate. */
    public attribute y2: Number on replace {
        awtLine.setLine(x1, y1, x2, y2);
        sgshape.setShape(awtLine);
    };
    public function createShape(): SGShape {
        awtLine = new java.awt.geom.Line2D.Double(x1, y1, x2, y2);
        var sgshape = new SGShape();
        sgshape.setShape(awtLine);
        return sgshape;
    }
}



