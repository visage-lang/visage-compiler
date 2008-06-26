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

import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * Creates a curved path element, defined by two new points, 
 * by drawing a Quadratic curve that intersects both the current coordinates 
 * and the specified coordinates {@code (x, y)},
 * using the specified point {@code (controlX, controlY)}
 * as a quadratic parametric control point. 
 * All coordinates are specified in double precision.
 * 
 * @profile common
 */
public class QuadTo extends PathElement {

    private function u():Void { 
        if (path != null) { path.updatePath2D(); }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the quadratic control point.
     *
     * @profile common
     */
    public attribute controlX:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the quadratic control point.
     *
     * @profile common
     */
    public attribute controlY:Number on replace { u(); }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the final end point.
     *
     * @profile common
     */
    public attribute x:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the final end point.
     *
     * @profile common
     */
    public attribute y:Number on replace { u(); }

    function addTo(path2D:GeneralPath):Void {
        if (absolute) {
            path2D.quadTo(controlX.floatValue(), controlY.floatValue(), 
            x.floatValue(), y.floatValue());
        }
        else {
            var cp = path2D.getCurrentPoint();
            var dx = cp.getX();
            var dy = cp.getY();
            path2D.quadTo((controlX+dx).floatValue(), (controlY+dy).floatValue(), 
            (x+dx).floatValue(), (y+dy).floatValue());
        }
    }

}
