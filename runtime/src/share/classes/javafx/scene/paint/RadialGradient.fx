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

package javafx.scene.paint;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.lang.Class;
import java.lang.Double;
import java.lang.reflect.Array;
import com.sun.scenario.scenegraph.ProportionalPaint;

// PENDING_DOC_REVIEW
/**
 * The {@code RadialGradient} class provides a way to fill a shape 
 * with a circular radial color gradient pattern. 
 * The user may specify 2 or more gradient colors, 
 * and this paint will provide an interpolation between each color. 
 * <p/>
 * The user must specify the circle controlling the gradient pattern, 
 * which is defined by a center point and a radius. 
 * The user can also specify a separate focus point within that circle, 
 * which controls the location of the first color of the gradient. 
 * By default the focus is set to be the center of the circle. 
 * <p/>
 * The center, radius, and focus point should be specified 
 * relative to a unit square, unless the <code>proportional</code> 
 * attribute is false.  By default proportional is true, and the 
 * gradient will be scaled to fill whatever shape it is applied to.
 * <p/>
 * This paint will map the first color of the gradient to the focus point, 
 * and the last color to the perimeter of the circle, 
 * interpolating smoothly for any in-between colors specified by the user. 
 * Any line drawn from the focus point to the circumference will 
 * thus span all the gradient colors. 
 * <p/>
 * Specifying a focus point outside of the circle's radius will result 
 * in the focus being set to the intersection point of the focus-center line 
 * and the perimeter of the circle. 
 * <p/>
 * The user must provide an array of floats specifying how to distribute 
 * the colors along the gradient. These values should range from 0.0 to 1.0 
 * and act like keyframes along the gradient 
 * (they mark where the gradient should be exactly a particular color). 
 *
 * @profile common
 */
public class RadialGradient extends Paint {

    // PENDING_DOC_REVIEW
    /**
     * Returns the {java.awt.Paint} delegate for this {@code RadialGradient}.  
     */
    override function getAWTPaint(): java.awt.Paint { 
        if (sizeof stops > 1) createPaint() else null
    }

    private function createPaint(): java.awt.Paint { 
        var center = new java.awt.geom.Point2D.Double(centerX, centerY);
        var fx = if (focusX == Double.NEGATIVE_INFINITY) centerX else focusX;
        var fy = if (focusY == Double.NEGATIVE_INFINITY) centerY else focusY;
        var focus = new java.awt.geom.Point2D.Double(fx, fy);
        var colors = for(stop in stops) { stop.color.getAWTColor() }
        var fractions = for(stop in stops) { stop.offset.floatValue() }
        var cmi = cycleMethod.toolkitValue;
        var rgp = Gradients.createRadialGradientPaint(center, radius.floatValue(), focus, fractions, colors, cmi);
        if (proportional) { new ProportionalPaint(rgp) } else { rgp }
    }
            
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the point to which the first color is mapped.
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     *
     * @profile common
     */
    public attribute focusX:Number = Double.NEGATIVE_INFINITY;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the Y coordinate of the point to which the first color is mapped.
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * 
     * @profile common
     */
    public attribute focusY:Number = Double.NEGATIVE_INFINITY;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the center point of the circle defining the gradient. 
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * The last color of the gradient is mapped to the perimeter of this circle.
     *
     * @profile common
     */
    public attribute centerX:Number = 0.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines the X coordinate of the center point of the circle defining the gradient. 
     * If proportional is true (the default), this value specifies a 
     * point on a unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     * The last color of the gradient is mapped to the perimeter of this circle.
     * 
     * @profile common
     */
    public attribute centerY:Number = 0.0;
    
    // PENDING_DOC_REVIEW
    /**
     * Specifies the radius of the circle defining the extents of the color gradient.
     * If proportional is true (the default), this value specifies a 
     * size relative to  unit square that will be scaled to match the size of the 
     * the shape that the gradient fills.
     *
     * @profile common
     */
    public attribute radius:Number = 1.0;
    
    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */
    public attribute proportional:Boolean = true;
    
    // PENDING_DOC_REVIEW
    /**
     * Defines which of the follwing cycle method is applied 
     * to the {@code LinearGradient}: {@code CycleMethod.NO_CYCLE}, 
     * {@code CycleMethod.REFLECT}, or {@code CycleMethod.REPEAT}.
     *
     * @profile common conditional multigradient
     */
    public attribute cycleMethod:CycleMethod= CycleMethod.NO_CYCLE;

    // PENDING_DOC_REVIEW
    /**
     * @profile common
     */
    public attribute stops:Stop[];

}
