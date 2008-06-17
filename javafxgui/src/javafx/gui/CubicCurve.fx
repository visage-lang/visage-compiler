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
import com.sun.scenario.scenegraph.SGCubicCurve;


// PENDING_DOC_REVIEW
/**
 * <p>The {@code CubiCurve} class defines a cubic parametric curve segment 
 * in (x,y) coordinate space. Example:</p>
 * 
 *<pre><code>CubicCurve {
       startX:   0     startY:  50
    controlX1:  25  controlY1:   0
    controlX2:  75  controlY2: 100
         endX: 100       endY:  50
         fill: Color.RED
}</code></pre>

* <p><img src="doc-files/CubicCurve01.png"/></p>
 * 
 * @profile common
 * @needsreview josh
 */  
public class CubicCurve extends Shape {

    function createSGNode():SGNode { new SGCubicCurve() }

    function getSGCubicCurve():SGCubicCurve { getSGNode() as SGCubicCurve }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the start point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute startX:Number on replace { 
        getSGCubicCurve().setX1(startX.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the start point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute startY: Number on replace { 
        getSGCubicCurve().setY1(startY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the first control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */          
    public attribute controlX1: Number on replace { 
        getSGCubicCurve().setCtrlX1(controlX1.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the first control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute controlY1: Number on replace {
        getSGCubicCurve().setCtrlY1(controlY1.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the second control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute controlX2: Number on replace { 
        getSGCubicCurve().setCtrlX2(controlX2.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the second control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute controlY2: Number on replace { 
        getSGCubicCurve().setCtrlY2(controlY2.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the end point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute endX: Number on replace { 
        getSGCubicCurve().setX2(endX.floatValue());
    }


    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the end point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute endY: Number on replace { 
        getSGCubicCurve().setY2(endY.floatValue());
    }
}
