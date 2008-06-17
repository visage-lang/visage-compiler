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
 * A Shape that is the result of the union of all shapes in sequence <code>a</code> 
 * intersected with each of the shapes in sequence <code>b</code>.
 */
public class ShapeIntersect extends Shape {

    function createSGNode():SGNode { new SGShape() }

    function getSGShape():SGShape { getSGNode() as SGShape }

    private attribute area:java.awt.geom.Area = new java.awt.geom.Area();

    public attribute a:Shape[] on replace { u(); }

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
                area.intersect(new java.awt.geom.Area(shape.getSGAbstractShape().getShape()));
            }
        }
        getSGShape().setShape(area);
    }

}
