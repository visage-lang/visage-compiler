/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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
package javafx.gui;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;


/**
 * <p>A Shape that is the result of the union of all shapes in sequence <code>a</code> with
 * all shapes in <code>b</code> subtracted from it. This class implements
 * the Constructive Area Geometry (CAG) <b>Subtract</b> operation.</p>
 *
 * <p>example:</p>
 *
 * <pre><code>ShapeSubtract {
 *    fill: Color.RED
 *    a: Rectangle { width: 100 height: 50 }
 *    b: Ellipse { centerX: 50 centerY: 25 radiusX: 50 radiusY: 25}
 * }</code></pre>
 * 
 * <p>looks like this</p>
 *
 * <p><img src="doc-files/ShapeSubtract01.png"/></p>
 */
public class ShapeSubtract extends Shape {

    function createSGNode():SGNode { new SGShape() }

    function getSGShape():SGShape { getSGNode() as SGShape }

    private attribute area:java.awt.geom.Area = new java.awt.geom.Area();

    /** The outer shape that shape (@code b) will be subtracted from.
    */
    public attribute a:Shape[] on replace { u(); }

    /** The inner shape that will be subtracted from shape {@code a}
    */
    public attribute b:Shape[] on replace { u(); }

    private function u():Void {
        area.reset();
        if (a <> null) {
            for (shape in a) {
                area.add(new java.awt.geom.Area(shape.getSGAbstractShape().getShape()));
            }
        }
        if (b <> null) {
            for (shape in b){
                area.subtract(new java.awt.geom.Area(shape.getSGAbstractShape().getShape()));
            }
        }
        getSGShape().setShape(area);
    }

}
