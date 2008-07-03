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
import com.sun.scenario.scenegraph.SGArc;


// PENDING_DOC_REVIEW_2
/**
 * The {@code Arc} class represents a 2D arc object, defined by a center point,
 * start angle (in degrees), angular extent (length of the arc in degrees), 
 * and an arc type ({@link ArcType#OPEN}, {@link ArcType#CHORD},
 * or {@link ArcType#ROUND}).
 * 
 * Example usage: the following code creates an Arc which is centered around
 * 50,50, has a radius of 25 and extends from the angle 45 to the angle 315
 * (270 degrees long), and is round.
 *
 * @example
 * import javafx.scene.paint.*;
 * import javafx.scene.geometry.*;
 * Arc { 
 *      centerX: 50
 *      centerY: 50
 *      radiusX: 25,
 *      radiusY: 25,
 *      startAngle: 45
 *      length: 270
 *      type: ArcType.ROUND
 *      fill: Color.BLACK
 *  }
 *
 * @profile common
 * @needsreview
 */ 
public class Arc extends Shape {

    /**
     * @treatasprivate implementation detail
     */
    public function impl_createSGNode():SGNode { new SGArc() }

    function getSGArc():SGArc { impl_getSGNode() as SGArc }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the center point of the arc. 
     *
     * @profile common
     */     
    public attribute centerX: Number on replace { 
        getSGArc().setCenterX(centerX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the center point of the arc. 
     *
     * @profile common
     */     
    public attribute centerY: Number on replace { 
        getSGArc().setCenterY(centerY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the overall width (horizontal radius) of the full ellipse 
     * of which this arc is a partial section.
     *
     * @profile common
     */     
    public attribute radiusX: Number on replace { 
        getSGArc().setRadiusX(radiusX.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the overall height (veritcal radius) of the full ellipse 
     * of which this arc is a partial section.
     * 
     * @profile common
     */     
    public attribute radiusY: Number on replace { 
        getSGArc().setRadiusY(radiusY.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the starting angle of the arc in degrees. 
     *
     * @profile common
     */     
    public attribute startAngle: Number on replace { 
        getSGArc().setAngleStart(startAngle.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the angular extent of the arc in degrees. 
     *
     * @profile common
     */     
    public attribute length: Number on replace { 
        getSGArc().setAngleExtent(length.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the closure type for the arc:
     * {@link ArcType#OPEN}, {@link ArcType#CHORD},or {@link ArcType#ROUND}.
     *
     * @profile common
     */     
    public attribute type:ArcType = ArcType.OPEN on replace { 
        getSGArc().setArcType(type.getToolkitValue());
    }

}
