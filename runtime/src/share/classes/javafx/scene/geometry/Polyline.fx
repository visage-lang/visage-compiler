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

package javafx.scene.geometry;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;


// PENDING_DOC_REVIEW
/**
 * Creates a polyline, defined by the array of the segment points. The Polyline
 * class is similar to the Polygon class, except that it is not automatically
 * closed.

@example
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
Polyline { 
    stroke:Color.BLACK
    points: [
        0.0,0.0, 
        20.0,10.0,
        10.0,20.0
    ]
}
@endexample

 *
 * @profile common
 */
public class Polyline extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    override function impl_createSGNode():SGNode { new SGShape() }

    function getSGShape():SGShape { impl_getSGNode() as SGShape }

    private attribute gPath = new java.awt.geom.GeneralPath;

    private attribute initializing:Boolean = true;

    init { initializing = false; u(); }

    private function u():Void { // update polyline
        if (initializing) { return; }
        if (sizeof points == 0 or sizeof points mod 2 != 0) { return; }
        gPath.reset();
        gPath.moveTo(points[0], points[1]);
        for (i in [1 .. ((sizeof points)/2-1)]) {
            var px = points[i*2+0];
            var py = points[i*2+1];
            gPath.lineTo(px.floatValue(), py.floatValue());
        }
        getSGShape().setShape(gPath);
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the coordinates of the polyline segments.
     *
     * @profile common
     */
    public attribute points: Number[] on replace oldElts[a..b] = newElts { u(); }

}
