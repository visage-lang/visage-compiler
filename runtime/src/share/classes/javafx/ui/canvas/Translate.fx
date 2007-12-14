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
import java.awt.geom.AffineTransform;
import java.lang.Math;
/**
 * Transformation function which moves an object.  
 */

public class Translate extends Transform {
    /** The distance to move this object along the x axis */
    public attribute x: Number on replace {
        transform = AffineTransform.getTranslateInstance(x, y);
    };
    /** The distance to move this object along the y axis */
    public attribute y: Number on replace {
        transform = AffineTransform.getTranslateInstance(x, y);
    };

    public static function translate(x:Number, y:Number):Translate {
       //TODO: JXFC-107
        //Translate {x: bind x, y: bind y};
        Translate {x: x, y: y};
    }

    public static function translateX(x:Number):Translate {
        //TODO: JXFC-107
        //Translate {x: bind x, y: 0};
       Translate {x: x, y: 0};
    }

    public static function translateY(y:Number):Translate {
        //TODO: JXFC-107
        //Translate {x: 0, y: bind y};
        Translate {x: 0, y: y};
    }    
}




