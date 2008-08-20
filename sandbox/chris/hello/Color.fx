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
package hello;

public class Color extends Paint {
    public var red: Number;
    public var green: Number;
    public var blue: Number;
    public var opacity: Number = 1.0;

    var awtColor: java.awt.Color = 
        bind lazy makeColor(red, green, blue, opacity);

    function makeColor(red:Number, green:Number, blue:Number, opacity:Number):java.awt.Color {
	return new java.awt.Color(red.floatValue(),
				  green.floatValue(),
				  blue.floatValue(),
				  opacity.floatValue());
    }
    
    public function getPaint():java.awt.Paint {
        return awtColor;
    }

    public function getColor():java.awt.Color {
        return awtColor;
    }

    public static function fromAWTColor(c:java.awt.Color): Color {
        Color {red: c.getRed()/255, green: c.getGreen()/255, 
            blue: c.getBlue()/ 255, opacity: c.getAlpha()/255};
    }


}
