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
 * The <code>Area</code> class is a device-independent specification of an
 * arbitrarily-shaped area.  The <code>Area</code> object is defined as an
 * object that performs certain binary CAG (Constructive Area Geometry)
 * functions on other area-enclosing geometries, such as rectangles,
 * ellipses, and polygons. The CAG functions are Add(union), Subtract,
 * Intersect, and ExclusiveOR. For example, an <code>Area</code> can be
 * made up of the area of a rectangle minus the area of an ellipse.
 *
 * This is an abstract class. The specific functions are provided by
 * subclasses of Area: <code>Add</code>, <code>Subtract</code>, 
 * <code>Intersect</code>, and <code>XOR</code>.
 */
public abstract class Area extends Shape {
    protected abstract function performOp(area1:java.awt.geom.Area, area2:java.awt.geom.Area):Void;
    private function getArea() {
        if (sgshape <> null) {
            var s1 = shape1.getTransformedShape();
            var s2 = shape2.getTransformedShape();
            if (s1 == null) {
                s1 = new java.awt.geom.Rectangle2D.Double();
            }
            if (s2 == null) {
                s2 = new java.awt.geom.Rectangle2D.Double();
            }
            var area1 = new java.awt.geom.Area(s1);
            var area2 = new java.awt.geom.Area(s2);
            this.performOp(area1, area2);
            sgshape.setShape(area1);
        }
    }
    /** the shape which is the first operand to this area function */
    public attribute shape1: Shape;
    /** the shape which is the second operand to this area function */
    public attribute shape2: Shape;

    private attribute awtShape1: java.awt.Shape = bind shape1.getTransformedShape()
        on replace {
            this.getArea();
        };
    private attribute awtShape2: java.awt.Shape = bind shape2.getTransformedShape()
        on replace {
            this.getArea();
        };
    public function createShape(): SGShape {
        sgshape = new SGShape();
        this.getArea();
        return sgshape;
    }
}


