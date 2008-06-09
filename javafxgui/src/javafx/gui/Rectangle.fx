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
import com.sun.scenario.scenegraph.SGRectangle;

// PENDING_DOC_REVIEW_2

/**
 * <p>The {@code Rectangle} class defines a rectangle 
 * with the specified size and location. By default the rectangle
 * has sharp corners. Rounded corners can be specified using
 * the arcWidth and arcHeight attributes.</p>
 
 * <p>Example code: the following code creates a rectangle with width and height
 * of 200x100 with the upper left hand corner at 50,50 and with rounded corners
 * of radius 10px.</p>
 
 * <pre><code>Rectangle {
    x: 50
    y: 50
    width: 200
    height: 100
    arcWidth: 10
    arcHeight: 10
    fill: Color.RED
}</code></pre>
 *
 * @profile common
 * @needsreview
 */
public class Rectangle extends Shape {

    function createSGNode():SGNode { new SGRectangle() }

    function getSGRectangle():SGRectangle { getSGNode() as SGRectangle }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the upper-left corner of the rectangle.
     *
     * @profile common
     */    
    public attribute x:Number on replace { 
        getSGRectangle().setX(x.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the upper-left corner of the rectangle.
     *
     * @profile common
     */
    public attribute y:Number on replace { 
        getSGRectangle().setY(y.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the width of the rectangle.
     *
     * @profile common
     */
    public attribute width:Number on replace { 
        getSGRectangle().setWidth(width.floatValue());
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the height of the rectangle.
     *
     * @profile common
     */
    public attribute height:Number on replace { 
        getSGRectangle().setHeight(height.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the horizontal diameter of the arc 
     * at the four corners of the rectangle.
     *
     * @profile common
     */
    public attribute arcWidth:Number on replace { 
        getSGRectangle().setArcWidth(arcWidth.floatValue());
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the vertical diameter of the arc 
     * at the four corners of the rectangle.
     *
     * @profile common
     */
    public attribute arcHeight:Number on replace { 
        getSGRectangle().setArcHeight(arcHeight.floatValue());
    }
}
