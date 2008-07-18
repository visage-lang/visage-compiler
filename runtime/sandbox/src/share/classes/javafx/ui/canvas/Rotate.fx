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
 * Transformation function which rotates an object.  
 */

public class Rotate extends Transform {
    /** The amount of rotation in degrees */
    public attribute angle: Number on replace {
        transform = 
            AffineTransform.getRotateInstance(Math.toRadians(angle), cx, cy);
    };
    
    /** The x coordinate of the center point about which this rotation will be performed. Defaults to zero. */
    public attribute cx: Number on replace {
        transform = 
            AffineTransform.getRotateInstance(Math.toRadians(angle), cx, cy);
    };

    /** The y coordinate of the center point about which this rotation will be performed. Defaults to zero. */
    public attribute cy: Number on replace {
        transform = 
            AffineTransform.getRotateInstance(Math.toRadians(angle), cx, cy);
    };
    
    public static function rotate(z:Number, cx:Number, cy:Number):Rotate {
        Rotate {cx: cx, cy: cy, angle: z};
    }
}



