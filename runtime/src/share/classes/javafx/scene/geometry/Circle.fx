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
import com.sun.scenario.scenegraph.SGCircle;


// PENDING_DOC_REVIEW_2
/**
 * The {@code Circle} class creates a new circle 
 * with the specified radius and center location measured in pixels 
 *
 * Example usage. The following code creates a circle with radius 50px centered
 * at (100,100)px.
 * 
@example
import javafx.scene.geometry.*;
import javafx.scene.paint.*;

Circle { 
  centerX: 100
  centerY: 100
  radius: 50
  fill:Color.BLACK
  }
 

 * @profile common 
 * @needsreview
 */ 
public class Circle extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGCircle() }

    function getSGCircle():SGCircle { impl_getSGNode() as SGCircle }

    // PENDING_DOC_REVIEW_2
    /**
    * Defines the horizontal position of the center of the circle in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    * @defaultvalue 0.0
    */     
    public attribute centerX:Number on replace { 
        getSGCircle().setCenterX(centerX.floatValue());
    }
    
    // PENDING_DOC_REVIEW_2
    /**
    * Defines the vertical position of the center of the circle in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    * @defaultvalue 0.0
    */        
    public attribute centerY:Number on replace { 
        getSGCircle().setCenterY(centerY.floatValue());
    }
    
    // PENDING_DOC_REVIEW_2
    /**
    * Defines the radius of the circle in pixels.
    * The default value is {@code 0.0}px
    *
    * @profile common
    * @needsreview
    * @defaultvalue 0.0
    */        
    public attribute radius:Number on replace { 
        getSGCircle().setRadius(radius.floatValue());
    }

}
