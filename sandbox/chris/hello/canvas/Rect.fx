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
 
package hello.canvas; 
import com.sun.scenario.scenegraph.SGShape;

/**
 * The <code>Rect</code> class defines a rectangle with
 * possibly rounded corners defined by a location (x,&nbsp;y), a
 * dimension (width&nbsp;&nbsp;height), and the width and height of an arc 
 * (arcWidth, arcHeight) with which to round the corners.
 */
public class Rect extends Shape {

    attribute sgrect: SGShape;

    attribute awtrect: java.awt.geom.RoundRectangle2D.Double;

    /** The x coordinate of this rectangle's location. */
    public attribute x: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };
    /** The y coordinate of this rectangle's location. */
    public attribute y: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };
    /** The width of this rectangle. */
    public attribute width: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };
    /** The height of this rectangle. */
    public attribute height: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };
    /** The width of the corner arc of this rectangle. */
    public attribute arcWidth: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };
    /** The height of the corner arc of this rectangle. */
    public attribute arcHeight: Number on replace {
	if (sgrect != null) {
	    awtrect.setRoundRect(x, y, width, height, arcWidth, arcHeight);
	    sgrect.setShape(awtrect);
	}
    };

    public function setSize(w:Number, h:Number):Void {
        width = w;
        height = h;
    }

    public function createShape(): SGShape {
        awtrect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
        sgrect = new SGShape();
        sgrect.setShape(awtrect);
        return sgrect;
    }

}



