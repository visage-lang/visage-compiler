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

/**
 * Transformation function which scales an object.  
 */
public class Scale extends Transform {
    /** The factor to scale along the x axis */
    public attribute x: Number on replace {
        if (x != 0 and y != 0) {
           transform = AffineTransform.getScaleInstance(x, y);
        }
    };
    /** The factor to scale along the y axis */
    public attribute y: Number on replace {
        if (x != 0 and y != 0) {
           transform = AffineTransform.getScaleInstance(x, y);
        }
    };
    
    public static function scale(x:Number, y:Number):Scale {
        //TODO: JXFC-107
        //return Scale {x: bind x, y: bind y};
        return Scale {x: x, y: y};
    };

    public static function scaleX(x:Number):Scale {
        return Scale {x: x, y: 1};
    };

    public static function scaleY(y:Number):Scale {
        return Scale {x: 1, y: y};
    };

    public static function scaleXY(s:Number):Scale {
        Scale {x: s, y: s};
    };    
}




