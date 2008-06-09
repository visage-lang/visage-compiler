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

import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGEllipse;


// PENDING_DOC_REVIEW
/**
 * The {@code Ellipse} class creates a new ellipse 
 * with the specified size and location in pixels
 *
 * @profile common 
 * @needsreview
 */      
public class Ellipse extends Shape {

    //non-public
    function createSGNode():SGNode { new SGEllipse() }

    //non-public
    function getSGEllipse():SGEllipse { getSGNode() as SGEllipse }


    // PENDING_DOC_REVIEW
    /**
    * Defines the horizontal position of the center of the ellipse in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    */          
    public attribute centerX:Number on replace {
        getSGEllipse().setCenterX(centerX.floatValue()); 
    }
    
    // PENDING_DOC_REVIEW
    /**
    * Defines the vertical position of the center of the ellipse in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    */          
    public attribute centerY:Number on replace { 
        getSGEllipse().setCenterY(centerY.floatValue()); 
    }
    
    // PENDING_DOC_REVIEW
    /**
    * Defines the width of the ellipse in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    */          
    public attribute radiusX:Number on replace { 
        getSGEllipse().setRadiusX(radiusX.floatValue()); 
    }
    
    // PENDING_DOC_REVIEW
    /**
    * Defines the height of the ellipse in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    */          
    public attribute radiusY:Number on replace { 
        getSGEllipse().setRadiusY(radiusY.floatValue()); 
    }

}
