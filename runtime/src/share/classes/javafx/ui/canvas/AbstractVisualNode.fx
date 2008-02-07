
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

package javafx.ui.canvas;
import javafx.ui.*;

public abstract class AbstractVisualNode {

    protected abstract function updateStroke():Void;

    /** The <code>stroke</code> property paints along the outline of the given 
     * graphical element.
     */
    public attribute stroke: Paint;

    /**
     * The <code>fill</code> property paints the interior of the given 
     graphical element. The area to be painted consists of any areas inside 
     * the outline of the node's shape. 
     */
    public attribute fill: Paint;

    public attribute fillOpacity: Number = 1;

    /**
     * <code>strokeLineCap</code> specifies the shape to be used at the end 
     * of open subpaths when they are stroked. Defaults to <code>SQUARE</code>.
     */
    public attribute strokeLineCap: StrokeLineCap on replace {
        updateStroke();
    };
    /**
     * <code>strokeLineJoin</code> specifies the shape to be used at the 
     * corners of paths or basic shapes when they are stroked. Defaults to
     * <code>MITER</code>.
     */
    public attribute strokeLineJoin: StrokeLineJoin on replace {
        updateStroke();
    };
    /**
     * The width of the stroke on the current object. A zero value causes 
     * no stroke to be painted. A negative value is an error. Defaults to 1.0.
     */
    public attribute strokeWidth: Number on replace {
        updateStroke();
    };
    /**
     * <p>
     * When two line segments meet at a sharp angle and miter joins have
     * been specified for 'stroke-linejoin', it is possible for the miter to
     * extend far beyond the thickness of the line stroking the path. The 
     * 'strokeMiterLimit' imposes a limit on the ratio of the miter length to
     * the 'strokeWidth'. When the limit is exceeded, the join is converted
     * from a miter to a bevel.
     * </p>
     * <p>
     * <b>strokeMiterLimit</b></br>
     * The limit on the ratio of the miter length to the 'strokeWidth'. The
     * value of <miterlimit> must be a number greater than or equal to 1.
     * Any other value is an error. Defaults to 10.0.
     * </p>
     * <p>
     * The ratio of miter length (distance between the outer tip and the inner
     * corner of the miter) to 'strokeWidth' is directly related to the angle
     * (theta) between the segments in user space by the formula:
     * </p>
     * <p><code>
     * &nbsp;&nbsp;&nbsp;&nbsp;miterLength / strokeWidth = 1 / sin(theta/2)
     * </pre></code>
     * </p>
     * <p>
     * For example, a miter limit of 1.414 converts miters to bevels for 
     * theta less than 90 degrees, a limit of 4.0 converts them for theta less
     * than approximately 29 degrees, and a limit of 10.0 converts them for 
     * theta less than approximately 11.5 degrees.
     * </p>
     */
    public attribute strokeMiterLimit: Number on replace {
        updateStroke();
    };
    /**
     * <code>strokeDashArray</code> controls the pattern of dashes and gaps 
     * used to stroke paths. This attribute contains a list of lengths that 
     * specify the lengths of alternating dashes and gaps. If an odd number 
     * of values is provided, then the list of values is repeated to yield an 
     * even number of values. Thus, strokeDashArray: [5,3,2] is equivalent to
     * strokeDashArray: [5,3,2,5,3,2]. Defaults to [].
     */
    public attribute strokeDashArray: Number[] on replace {
        updateStroke();
    };
   
    /** 
     * <code>strokeDashOffset</code> specifies the distance into the dash 
     * pattern to start the dash. Defaults to 0.0.
     */
    public attribute strokeDashOffset: Number on replace {
        updateStroke();
    };

}
