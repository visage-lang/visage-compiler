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
import com.sun.scenario.scenegraph.SGCircle;


// PENDING_DOC_REVIEW
/**
 * The {@code Circle} class creates a new circle 
 * with the specified size and location.
 *
 * @profile common 
 */ 
public class Circle extends Shape {

    function createSGNode():SGNode { new SGCircle() }

    function getSGCircle():SGCircle { getSGNode() as SGCircle }

    // PENDING_DOC_REVIEW
    /**
    * Defines the horizontal position of the center of the circle.
    * The default value is {@code 0.0}
    *
    * @profile common
    */     
    public attribute centerX:Number on replace { 
        getSGCircle().setCenterX(centerX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
    * Defines the vertical position of the center of the circle.
    * The default value is {@code 0.0}
    *
    * @profile common
    */        
    public attribute centerY:Number on replace { 
        getSGCircle().setCenterY(centerY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
    * Defines the radius of the circle.
    * The default value is {@code 0.0}
    *
    * @profile common
    */        
    public attribute radius:Number on replace { 
        getSGCircle().setRadius(radius.floatValue());
    }

}
