/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 * 
 * This code is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 only, as 
 * published by the Free Software Foundation.  Sun designates this 
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
 
package hello.canvas; 
import java.awt.BasicStroke;
import hello.Paint;
import com.sun.scenario.scenegraph.SGAbstractShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGNode;

/**
 * Abstract base class for visual elements that appear in the canvas.
 */
public abstract class VisualNode extends Node {

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

    /**
     * <code>strokeLineCap</code> specifies the shape to be used at the end 
     * of open subpaths when they are stroked. Defaults to <code>SQUARE</code>.
     */
    public attribute strokeLineCap: StrokeLineCap = null /*StrokeLineCap.SQUARE*/ on replace {
        updateStroke();
    };
    /**
     * <code>strokeLineJoin</code> specifies the shape to be used at the 
     * corners of paths or basic shapes when they are stroked. Defaults to
     * <code>MITER</code>.
     */
    public attribute strokeLineJoin: StrokeLineJoin = /* StrokeLineJoin.MITER*/ null on replace {
        updateStroke();
    };
    /**
     * The width of the stroke on the current object. A zero value causes 
     * no stroke to be painted. A negative value is an error. Defaults to 1.0.
     */
    public attribute strokeWidth: Number = 1.0 on replace {
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
    public attribute strokeMiterLimit: Number = 10.0 on replace {
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
    public attribute strokeDashArray: Number[] 
        on replace [ndx] (oldValue) {
            updateStroke();
        }
        on insert [ndx] (newValue) {
            updateStroke();
        }
        on delete [ndx] (oldValue) {
            updateStroke();
        };
   
    /** 
     * <code>strokeDashOffset</code> specifies the distance into the dash 
     * pattern to start the dash. Defaults to 0.0.
     */
    public attribute strokeDashOffset: Number on replace {
        updateStroke();
    };
    private attribute sgvisualnode: SGAbstractShape;

    private attribute awtStroke: java.awt.Paint = bind if (this.stroke == null) then null else this.stroke.getPaint()
        on replace  {
            if (sgvisualnode <> null and awtStroke <> null) {
                sgvisualnode.setDrawPaint(awtStroke);
            }
            updateMode();
        };
    private attribute awtFill: java.awt.Paint = bind if (fill == null) null else fill.getPaint() 
        on replace {
            if (sgvisualnode <> null and awtFill <> null) {
                sgvisualnode.setFillPaint(awtFill);
            }
            updateMode();
        };

    public abstract function createVisualNode(): SGAbstractShape;
    public function getVisualNode(): SGAbstractShape{
        if (sgvisualnode == null) {
            this.createNode();
        }
        return sgvisualnode;
    }

   private  function updateMode() {
        if (sgvisualnode <> null) {
            if (awtFill <> null and awtStroke <> null) {
                sgvisualnode.setMode(SGAbstractShape.Mode.STROKE_FILL);
            } else if (awtFill <> null) {
                sgvisualnode.setMode(SGAbstractShape.Mode.FILL);
            } else {
                sgvisualnode.setMode(SGAbstractShape.Mode.STROKE);
            }
        }
    }

    public function updateStroke():Void {
        if (false and sgvisualnode <> null) {
            sgvisualnode.setDrawStroke(new BasicStroke(strokeWidth.floatValue(),
                                                       strokeLineCap.id.intValue(),
                                                       strokeLineJoin.id.intValue(),
                                                       strokeMiterLimit.floatValue(),
                                                       null, //strokeDashArray,
                                                       strokeDashOffset.floatValue()));
            updateMode();
        }
    }

    protected function createNode(): SGNode {
        sgvisualnode = this.createVisualNode();
        if (awtFill <> null) {
            sgvisualnode.setFillPaint(awtFill);
        }
        if (awtStroke <> null) {
            sgvisualnode.setDrawPaint(stroke.getPaint());
        }
        updateStroke(); // will also call updateMode()...
        return sgvisualnode;
    }

}


