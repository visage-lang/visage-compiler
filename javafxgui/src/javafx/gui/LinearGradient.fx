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

import java.awt.geom.Rectangle2D;
import java.lang.Class;
import java.lang.reflect.Array;
import com.sun.scenario.scenegraph.ProportionalPaint;

// PENDING_DOC_REVIEW
/**
 * <p>The {@code LinearGradient} class fills a shape
 * with a linear color gradient pattern. The user may specify two or
 * more gradient colors, and this Paint will provide an interpolation
 * between each color.</p>
 *
 * <p>
 * The user should provide an array of {@code Stop}s specifying how to distribute 
 * the colors along the gradient. The {@code Stop#offset} attribute should range 
 * from 0.0 to 1.0 and act like keyframes along the gradient 
 * (they mark where the gradient should be exactly a particular color). </p>
 * 
 * <p>If the proportional attribute is set to true (the default)
 * then the start and end points of the gradient
 * should be specified relative to the unit square (0.0->1.0) and will
 * be stretched across the shape. If proportional attribute is set
 * to false, then the start and end points should be specified
 * as absolute pixel values and the gradient will not be stretched at all.</p>
 *
 * <p>
 * The two filled rectangles in the example below will render the same.
 * The first uses proportional coordinates (the default) to specify
 * the gradient's end points.  The second uses absolute 
 * coordinates.  Both of them fill the specified rectangle with a 
 * horizontal gradient that varies from black to red</p>
 * 
 * <pre><code>
 * // object bounding box relative (proportional:true, default)
 * Rectangle {
 *     x: 0 y: 0 width: 100 height: 100
 *     fill: LinearGradient {
 *         startX: 0.0
 *         startY: 0.0
 *         endX: 1.0
 *         endY: 0.0
 *         proportional: true
 *         stops: [
 *            Stop { offset: 0.0 color: Color.BLACK },
 *            Stop { offset: 1.0 color: Color.RED }
 *         ]
 *     }
 * }
 * 
 * // user space relative (proportional:false)
 * Rectangle {
 *     x: 0 y: 0 width: 100 height: 100
 *     fill: LinearGradient {
 *         startX: 0.0
 *         startY: 0.0
 *         endX: 100.0
 *         endY:   0.0
 *         proportional: false
 *         stops: [
 *            Stop { offset: 0.0 color: Color.BLACK },
 *            Stop { offset: 1.0 color: Color.RED }
 *         ]
 *     }
 * } 
 * </code></pre>
 *
 * @profile common
 * @needsreview
 */ 
public class LinearGradient extends Paint {

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code java.awt.Paint} delegate for this {@code LinearGradient}.  
     */
    public function getAWTPaint(): java.awt.Paint { 
        if (sizeof stops > 1) createPaint() else null
    }

    private function createPaint(): java.awt.Paint { 
        var start = new java.awt.geom.Point2D.Double(startX, startY);
        var end = new java.awt.geom.Point2D.Double(endX, endY);
        var colors = for(stop in stops) { stop.color.getAWTColor() }
        var fractions = for(stop in stops) { stop.offset.floatValue() }
        var cmi = cycleMethod.getToolkitValue();
        var lgp = Gradients.createLinearGradientPaint(start, end, fractions, colors, cmi);
        if (proportional) { new ProportionalPaint(lgp) } else { lgp }
    }

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the gradient axis start point.  
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * 
     * @profile common
     */ 
    public attribute startX:Number = 0.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the gradient axis start point.
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * 
     * @profile common
     */ 
    public attribute startY:Number = 0.0;

    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the gradient axis end point.
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     *
     * @profile common
     */ 
    public attribute endX:Number = 1.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the gradient axis end point.
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * 
     * @profile common
     */ 
    public attribute endY:Number = 1.0;
    
    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */ 
    public attribute proportional:Boolean = true;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines which of the follwing cycle method is applied 
     * to the {@code LinearGradient}: {@code CycleMethod.NO}, 
     * {@code CycleMethod.REFLECT}, or {@code CycleMethod.REPEAT}.
     *
     * @profile common
     */ 
    public attribute cycleMethod:CycleMethod;

    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */ 
    public attribute stops:Stop[];

}
