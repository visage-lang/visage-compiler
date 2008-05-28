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

package javafx.ui.canvas;
import java.awt.geom.AffineTransform;
import java.lang.Math;

/**
 * Transformation function which skews an object.  
 */
public class Skew extends Transform {
    /** The skew angle along the x axis in degrees */
    public attribute x: Number on replace {
        transform = AffineTransform.getShearInstance(Math.tan(Math.toRadians(x)),
                         Math.tan(Math.toRadians(y)));
    };
    /** The skew angle along the y axis in degrees*/
    public attribute y: Number on replace {
        transform = AffineTransform.getShearInstance(Math.tan(Math.toRadians(x)),
                        Math.tan(Math.toRadians(y)));
    };

    public static function skew(x:Number,y:Number):Skew {
        Skew {x: x, y: y};
    }

    public static function skewX(x:Number):Skew {
        Skew {x: x, y: 0};
    }

    public static function skewY(y:Number):Skew {
        Skew {x: 0, y: y};
    }
}


