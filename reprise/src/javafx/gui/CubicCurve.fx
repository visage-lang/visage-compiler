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
package javafx.gui;

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGCubicCurve;


// PENDING_DOC_REVIEW
/**
 * The {@code CubiCurve} class defines a cubic parametric curve segment 
 * in (x,y) coordinate space.
 * 
 * @profile common
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
    public attribute x1:Number on replace { 
        getSGCubicCurve().setX1(x1.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the start point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute y1: Number on replace { 
        getSGCubicCurve().setY1(y1.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the first control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */          
    public attribute ctrlX1: Number on replace { 
        getSGCubicCurve().setCtrlX1(ctrlX1.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the first control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute ctrlY1: Number on replace {
        getSGCubicCurve().setCtrlY1(ctrlY1.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the second control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute ctrlX2: Number on replace { 
        getSGCubicCurve().setCtrlX2(ctrlX2.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the second control point 
     * of the cubic curve segment.
     * 
     * @profile common
     */      
    public attribute ctrlY2: Number on replace { 
        getSGCubicCurve().setCtrlY2(ctrlY2.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the end point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute x2: Number on replace { 
        getSGCubicCurve().setX2(x2.floatValue());
    }


    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the end point of the cubic curve segment. 
     *
     * @profile common
     */      
    public attribute y2: Number on replace { 
        getSGCubicCurve().setY2(y2.floatValue());
    }
}
