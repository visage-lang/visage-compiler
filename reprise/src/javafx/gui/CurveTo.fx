/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.awt.geom.GeneralPath;

// PENDING_DOC_REVIEW
/**
 * Creates a curved path element, defined by three new points, 
 * by drawing a B&eacute;zier curve that intersects both the current coordinates 
 * and the specified coordinates {@code (x3,y3)}, 
 * using the specified points {@code (x1,y1)} and {@code (x2,y2)} 
 * as B&eacute;zier control points. All coordinates are specified in double precision. 
 *
 * @profile common
 */  
public class CurveTo extends PathElement {

    private function u():Void { 
        if (path <> null) { path.updatePath2D(); }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the first B&eacute;zier control point.
     * 
     * @profile common
     */      
    public attribute x1:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the first B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute y1:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the second B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute x2:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the second B&eacute;zier control point.
     *
     * @profile common
     */      
    public attribute y2:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the final end point.
     *
     * @profile common
     */      
    public attribute x3:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the final end point.
     *
     * @profile common
     */      
    public attribute y3:Number on replace { u(); }

    // PENDING_DOC_REVIEW
    /**
     * Adds the curved path element to the specified path.
     *
     * @profile common
     */      
    function addTo(path2D:GeneralPath):Void {
        if (absolute) {
            path2D.curveTo(x1.floatValue(), y1.floatValue(), x2.floatValue(), 
            y2.floatValue(), x3.floatValue(), y3.floatValue());
        }
        else {
            var cp = path2D.getCurrentPoint();
            var dx = cp.getX();
            var dy = cp.getY();
            path2D.curveTo((x1+dx).floatValue(), (y1+dy).floatValue(), 
            (x2+dx).floatValue(), (y2+dy).floatValue(), 
            (x3+dx).floatValue(), (y3+dy).floatValue());
        }
    }

}
