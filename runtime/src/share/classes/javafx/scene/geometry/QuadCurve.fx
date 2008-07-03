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
import com.sun.scenario.scenegraph.SGQuadCurve;


// PENDING_DOC_REVIEW
/**
 * The {@code Quadcurve} class defines a quadratic parametric curve segment 
 * in (x,y) coordinate space.

@example
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
QuadCurve {
    startX: 0.0
    startY: 50.0
    endX: 50.0
    endY: 50.0
    controlX: 25.0
    controlY: 0.0
    stroke: Color.BLACK
}
@endexample

 *
 * @profile common
 */
public class QuadCurve extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGQuadCurve() }

    function getSGQuadCurve():SGQuadCurve { impl_getSGNode() as SGQuadCurve }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the start point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */    
    public attribute startX:Number on replace {
        getSGQuadCurve().setX1(startX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the start point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */
    public attribute startY:Number on replace { 
        getSGQuadCurve().setY1(startY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the control point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */
    public attribute controlX:Number on replace { 
        getSGQuadCurve().setCtrlX(controlX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the control point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */
    public attribute controlY:Number on replace { 
        getSGQuadCurve().setCtrlY(controlY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the end point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */
    public attribute endX:Number on replace { 
        getSGQuadCurve().setX2(endX.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the end point 
     * of the quadratic curve segment.
     * 
     * @profile common
     */
    public attribute endY:Number on replace { 
        getSGQuadCurve().setY2(endY.floatValue());
    }

}
