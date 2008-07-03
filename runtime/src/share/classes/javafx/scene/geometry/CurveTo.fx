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
 * Creates a curved path element, defined by three new points, 
 * by drawing a B&eacute;zier curve that intersects both the current coordinates 
 * and the specified coordinates {@code (x,y)}, 
 * using the specified points {@code (controlX1,controlY1)} and {@code (controlX2,controlY2)} 
 * as B&eacute;zier control points. All coordinates are specified in double precision. 
 *
@example
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
Path {
    fill:Color.BLACK
    elements: [
        MoveTo { x: 0 y: 50 },
        CurveTo {
            controlX1:   0     controlY1:  0
            controlX2: 100    controlY2: 100
                    x: 100            y:  50
        }
    ]
}
@endexample

 * @profile common
 * @needsreview
 */  
public class CurveTo extends PathElement {

    private function u():Void { 
        if (path != null) { path.updatePath2D(); }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the first B&eacute;zier control point.
     * 
     * @profile common
     */      
    public attribute controlX1:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the first B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute controlY1:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the second B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute controlX2:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the second B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute controlY2:Number on replace { u(); }

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

    // PENDING_DOC_REVIEW
    /**
     * Adds the curved path element to the specified path.
     *
     * @profile common
     */      
    function addTo(path2D:GeneralPath):Void {
        if (absolute) {
            path2D.curveTo(controlX1.floatValue(), controlY1.floatValue(), controlX2.floatValue(), 
            controlY2.floatValue(), x.floatValue(), y.floatValue());
        }
        else {
            var cp = path2D.getCurrentPoint();
            var dx = cp.getX();
            var dy = cp.getY();
            path2D.curveTo((controlX1+dx).floatValue(), (controlY1+dy).floatValue(), 
            (controlX2+dx).floatValue(), (controlY2+dy).floatValue(), 
            (x+dx).floatValue(), (y+dy).floatValue());
        }
    }

}
