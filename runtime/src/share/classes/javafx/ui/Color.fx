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
package javafx.ui;

import java.lang.Math;
import javafx.ui.UIElement;
import java.lang.Float;

public class Color extends AbstractColor, UIElement{
    public attribute red: Number;
    public attribute green: Number;
    public attribute blue: Number;
    public attribute opacity: Number = 1.0;


    public bound function interpolate(nextValue:Color, interp: Number): Color{
        return if (nextValue == null) {
            this;
        } else {
            var fromValue = this;
            var toValue = nextValue;
            var r1 = fromValue.red;
            var g1 = fromValue.green;
            var b1 = fromValue.blue;
            var a1 = fromValue.opacity;
            var r2 = toValue.red;
            var g2 = toValue.green;
            var b2 = toValue.blue;
            var a2 = toValue.opacity;
            var invInterp = 1.0 - interp;
            var red = r1*invInterp + r2*interp;
            var green = g1*invInterp + g2*interp ;
            var blue =   b1*invInterp + b2*interp;
            var opacity = a1*invInterp + a2*interp;
            if (red == this.red and green == this.green 
                and blue == this.blue and opacity == this.opacity) {
                this;
            } else {
                Color{red:red, green:green, blue:blue, opacity:opacity};
            }
        }
    }


    public attribute factor: Number = 0.70;
    
    public bound function darker():Color{
        return Color{red:Math.max((red*factor), 0.0), 
            green:Math.max((green*factor), 0.0), 
            blue:Math.max((blue*factor), 0.0), opacity:opacity};
    }

    public bound function brighter():Color{
        return Color{red:Math.min((red/factor), 1.0), 
            green:Math.min((green/factor), 1.0), 
            blue:Math.min((blue/factor), 1.0), opacity:opacity};
    }

    public bound function moreTransparent():Color {
        return Color{red:red, green:green, blue:blue, 
            opacity:Math.max((opacity*factor), 0.0)};
    }

    public bound function lessTransparent():Color{
        return Color{red:red, green:green, blue:blue,
            opacity:Math.min((opacity/factor), 1.0)};
    }

    public bound function htmlRef(): String{
        var alphaInt:Integer = (opacity * 255).intValue();
        var alphaStr = "{%02x alphaInt}";
        var redInt:Integer = (red * 255).intValue();
        var redStr = "{%02x redInt}";
        var greenInt:Integer = (green * 255).intValue();
        var greenStr = "{%02x greenInt}";
        var blueInt:Integer = (blue * 255).intValue();
        var blueStr = "{%02x blueInt}";
        return "#{redStr}{greenStr}{blueStr}{alphaStr}";
    }

    private function makeColor(red:Number, green:Number, blue:Number, opacity:Number):java.awt.Color {
	new java.awt.Color(red.floatValue(), green.floatValue(), blue.floatValue(), opacity.floatValue());
    }
    
    private attribute awtColor: java.awt.Color = 
        bind makeColor(red, green, blue, opacity);
    
    public bound function getPaint():java.awt.Paint {
        return awtColor;
    }

    public bound function getColor():java.awt.Color {
        return awtColor;
    }

    public static function color(r:Number, g:Number, b:Number, a:Number):Color {
        return Color { red: r, green: g, blue: b, opacity: a };
    }
    
    public static function rgb(r:Number, g:Number, b:Number):Color {
        return Color {red: r/255, green: g/255, blue: b/255, opacity: 1};
    }

    public static function rgba(r:Number, g:Number, b:Number, a:Number):Color {
        return Color {red: r/255, green: g/255, blue: b/255, opacity: a/255};
    }

    public static attribute ALICEBLUE:Color= Color.rgb(0xF0, 0xF8, 0xFF);
    public static attribute ANTIQUEWHITE:Color= rgb(0xFA, 0xEB, 0xD7);
    public static attribute AQUA:Color= rgb(0x00, 0xFF, 0xFF);
    public static attribute AQUAMARINE:Color= rgb(0x7F, 0xFF, 0xD4);
    public static attribute AZURE:Color= rgb(0xF0, 0xFF, 0xFF);
    public static attribute BEIGE:Color= rgb(0xF5, 0xF5, 0xDC);
    public static attribute BISQUE:Color= rgb(0xFF, 0xE4, 0xC4);
    public static attribute BLACK:Color= rgb(0x00, 0x00, 0x00);
    public static attribute BLANCHEDALMOND:Color= rgb(0xFF, 0xEB, 0xCD);
    public static attribute BLUE:Color= rgb(0x00, 0x00, 0xFF);
    public static attribute BLUEVIOLET:Color= rgb(0x8A, 0x2B, 0xE2);
    public static attribute BROWN:Color= rgb(0xA5, 0x2A, 0x2A);
    public static attribute BURLYWOOD:Color= rgb(0xDE, 0xB8, 0x87);
    public static attribute CADETBLUE:Color= rgb(0x5F, 0x9E, 0xA0);
    public static attribute CHARTREUSE:Color= rgb(0x7F, 0xFF, 0x00);
    public static attribute CHOCOLATE:Color= rgb(0xD2, 0x69, 0x1E);
    public static attribute CORAL:Color= rgb(0xFF, 0x7F, 0x50);
    public static attribute CORNFLOWERBLUE:Color= rgb(0x64, 0x95, 0xED);
    public static attribute CORNSILK:Color= rgb(0xFF, 0xF8, 0xDC);
    public static attribute CRIMSON:Color= rgb(0xDC, 0x14, 0x3C);
    public static attribute CYAN:Color= rgb(0x00, 0xFF, 0xFF);
    public static attribute DARKBLUE:Color= rgb(0x00, 0x00, 0x8B);
    public static attribute DARKCYAN:Color= rgb(0x00, 0x8B, 0x8B);
    public static attribute DARKGOLDENROD:Color= rgb(0xB8, 0x86, 0x0B);
    public static attribute DARKGRAY:Color= rgb(0xA9, 0xA9, 0xA9);
    public static attribute DARKGREEN:Color= rgb(0x00, 0x64, 0x00);
    public static attribute DARKKHAKI:Color= rgb(0xBD, 0xB7, 0x6B);
    public static attribute DARKMAGENTA:Color= rgb(0x8B, 0x00, 0x8B);
    public static attribute DARKOLIVEGREEN:Color= rgb(0x55, 0x6B, 0x2F);
    public static attribute DARKORANGE:Color= rgb(0xFF, 0x8C, 0x00);
    public static attribute DARKORCHID:Color= rgb(0x99, 0x32, 0xCC);
    public static attribute DARKRED:Color= rgb(0x8B, 0x00, 0x00);
    public static attribute DARKSALMON:Color= rgb(0xE9, 0x96, 0x7A);
    public static attribute DARKSEAGREEN:Color= rgb(0x8F, 0xBC, 0x8B);
    public static attribute DARKSLATEBLUE:Color= rgb(0x48, 0x3D, 0x8B);
    public static attribute DARKSLATEGRAY:Color= rgb(0x2F, 0x4F, 0x4F);
    public static attribute DARKTURQUOISE:Color= rgb(0x00, 0xCE, 0xD1);
    public static attribute DARKVIOLET:Color= rgb(0x94, 0x00, 0xD3);
    public static attribute DEEPPINK:Color= rgb(0xFF, 0x14, 0x93);
    public static attribute DEEPSKYBLUE:Color= rgb(0x00, 0xBF, 0xFF);
    public static attribute DIMGRAY:Color= rgb(0x69, 0x69, 0x69);
    public static attribute DODGERBLUE:Color= rgb(0x1E, 0x90, 0xFF);
    public static attribute FIREBRICK:Color= rgb(0xB2, 0x22, 0x22);
    public static attribute FLORALWHITE:Color= rgb(0xFF, 0xFA, 0xF0);
    public static attribute FORESTGREEN:Color= rgb(0x22, 0x8B, 0x22);
    public static attribute FUCHSIA:Color= rgb(0xFF, 0x00, 0xFF);
    public static attribute GAINSBORO:Color= rgb(0xDC, 0xDC, 0xDC);
    public static attribute GHOSTWHITE:Color= rgb(0xF8, 0xF8, 0xFF);
    public static attribute GOLD:Color= rgb(0xFF, 0xD7, 0x00);
    public static attribute GOLDENROD:Color= rgb(0xDA, 0xA5, 0x20);
    public static attribute GRAY:Color= rgb(0x80, 0x80, 0x80);
    public static attribute GREEN:Color= rgb(0x00, 0x80, 0x00);
    public static attribute GREENYELLOW:Color= rgb(0xAD, 0xFF, 0x2F);
    public static attribute HONEYDEW:Color= rgb(0xF0, 0xFF, 0xF0);
    public static attribute HOTPINK:Color= rgb(0xFF, 0x69, 0xB4);
    public static attribute INDIANRED:Color= rgb(0xCD, 0x5C, 0x5C);
    public static attribute INDIGO:Color= rgb(0x4B, 0x00, 0x82);
    public static attribute IVORY:Color= rgb(0xFF, 0xFF, 0xF0);
    public static attribute KHAKI:Color= rgb(0xF0, 0xE6, 0x8C);
    public static attribute LAVENDER:Color= rgb(0xE6, 0xE6, 0xFA);
    public static attribute LAVENDERBLUSH:Color= rgb(0xFF, 0xF0, 0xF5);
    public static attribute LAWNGREEN:Color= rgb(0x7C, 0xFC, 0x00);
    public static attribute LEMONCHIFFON:Color= rgb(0xFF, 0xFA, 0xCD);
    public static attribute LIGHTBLUE:Color= rgb(0xAD, 0xD8, 0xE6);
    public static attribute LIGHTCORAL:Color= rgb(0xF0, 0x80, 0x80);
    public static attribute LIGHTCYAN:Color= rgb(0xE0, 0xFF, 0xFF);
    public static attribute LIGHTGOLDENRODYELLOW:Color= rgb(0xFA, 0xFA, 0xD2);
    public static attribute LIGHTGREEN:Color= rgb(0x90, 0xEE, 0x90);
    public static attribute GREY:Color= rgb(0x80, 0x80, 0x80);
    public static attribute LIGHTGREY:Color= rgb(0xD3, 0xD3, 0xD3);
    public static attribute LIGHTPINK:Color= rgb(0xFF, 0xB6, 0xC1);
    public static attribute LIGHTSALMON:Color= rgb(0xFF, 0xA0, 0x7A);
    public static attribute LIGHTSEAGREEN:Color= rgb(0x20, 0xB2, 0xAA);
    public static attribute LIGHTSKYBLUE:Color= rgb(0x87, 0xCE, 0xFA);
    public static attribute LIGHTSLATEGRAY:Color= rgb(0x77, 0x88, 0x99);
    public static attribute LIGHTSTEELBLUE:Color= rgb(0xB0, 0xC4, 0xDE);
    public static attribute LIGHTYELLOW:Color= rgb(0xFF, 0xFF, 0xE0);
    public static attribute LIME:Color= rgb(0x00, 0xFF, 0x00);
    public static attribute LIMEGREEN:Color= rgb(0x32, 0xCD, 0x32);
    public static attribute LINEN:Color= rgb(0xFA, 0xF0, 0xE6);
    public static attribute MAGENTA:Color= rgb(0xFF, 0x00, 0xFF);
    public static attribute MAROON:Color= rgb(0x80, 0x00, 0x00);
    public static attribute MEDIUMAQUAMARINE:Color= rgb(0x66, 0xCD, 0xAA);
    public static attribute MEDIUMBLUE:Color= rgb(0x00, 0x00, 0xCD);
    public static attribute MEDIUMORCHID:Color= rgb(0xBA, 0x55, 0xD3);
    public static attribute MEDIUMPURPLE:Color= rgb(0x93, 0x70, 0xDB);
    public static attribute MEDIUMSEAGREEN:Color= rgb(0x3C, 0xB3, 0x71);
    public static attribute MEDIUMSLATEBLUE:Color= rgb(0x7B, 0x68, 0xEE);
    public static attribute MEDIUMSPRINGGREEN:Color= rgb(0x00, 0xFA, 0x9A);
    public static attribute MEDIUMTURQUOISE:Color= rgb(0x48, 0xD1, 0xCC);
    public static attribute MEDIUMVIOLETRED:Color= rgb(0xC7, 0x15, 0x85);
    public static attribute MIDNIGHTBLUE:Color= rgb(0x19, 0x19, 0x70);
    public static attribute MINTCREAM:Color= rgb(0xF5, 0xFF, 0xFA);
    public static attribute MISTYROSE:Color= rgb(0xFF, 0xE4, 0xE1);
    public static attribute MOCCASIN:Color= rgb(0xFF, 0xE4, 0xB5);
    public static attribute NAVAJOWHITE:Color= rgb(0xFF, 0xDE, 0xAD);
    public static attribute NAVY:Color= rgb(0x00, 0x00, 0x80);
    public static attribute OLDLACE:Color= rgb(0xFD, 0xF5, 0xE6);
    public static attribute OLIVE:Color= rgb(0x80, 0x80, 0x00);
    public static attribute OLIVEDRAB:Color= rgb(0x6B, 0x8E, 0x23);
    public static attribute ORANGE:Color= rgb(0xFF, 0xA5, 0x00);
    public static attribute ORANGERED:Color= rgb(0xFF, 0x45, 0x00);
    public static attribute ORCHID:Color= rgb(0xDA, 0x70, 0xD6);
    public static attribute PALEGOLDENROD:Color= rgb(0xEE, 0xE8, 0xAA);
    public static attribute PALEGREEN:Color= rgb(0x98, 0xFB, 0x98);
    public static attribute PALETURQUOISE:Color= rgb(0xAF, 0xEE, 0xEE);
    public static attribute PALEVIOLETRED:Color= rgb(0xDB, 0x70, 0x93);
    public static attribute PAPAYAWHIP:Color= rgb(0xFF, 0xEF, 0xD5);
    public static attribute PEACHPUFF:Color= rgb(0xFF, 0xDA, 0xB9);
    public static attribute PERU:Color= rgb(0xCD, 0x85, 0x3F);
    public static attribute PINK:Color= rgb(0xFF, 0xC0, 0xCB);
    public static attribute PLUM:Color= rgb(0xDD, 0xA0, 0xDD);
    public static attribute POWDERBLUE:Color= rgb(0xB0, 0xE0, 0xE6);
    public static attribute PURPLE:Color= rgb(0x80, 0x00, 0x80);
    public static attribute RED:Color= rgb(0xFF, 0x00, 0x00);
    public static attribute ROSYBROWN:Color= rgb(0xBC, 0x8F, 0x8F);
    public static attribute ROYALBLUE:Color= rgb(0x41, 0x69, 0xE1);
    public static attribute SADDLEBROWN:Color= rgb(0x8B, 0x45, 0x13);
    public static attribute SALMON:Color= rgb(0xFA, 0x80, 0x72);
    public static attribute SANDYBROWN:Color= rgb(0xF4, 0xA4, 0x60);
    public static attribute SEAGREEN:Color= rgb(0x2E, 0x8B, 0x57);
    public static attribute SEASHELL:Color= rgb(0xFF, 0xF5, 0xEE);
    public static attribute SIENNA:Color= rgb(0xA0, 0x52, 0x2D);
    public static attribute SILVER:Color= rgb(0xC0, 0xC0, 0xC0);
    public static attribute SKYBLUE:Color= rgb(0x87, 0xCE, 0xEB);
    public static attribute SLATEBLUE:Color= rgb(0x6A, 0x5A, 0xCD);
    public static attribute SLATEGRAY:Color= rgb(0x70, 0x80, 0x90);
    public static attribute SNOW:Color= rgb(0xFF, 0xFA, 0xFA);
    public static attribute SPRINGGREEN:Color= rgb(0x00, 0xFF, 0x7F);
    public static attribute STEELBLUE:Color= rgb(0x46, 0x82, 0xB4);
    public static attribute TAN:Color= rgb(0xD2, 0xB4, 0x8C);
    public static attribute TEAL:Color= rgb(0x00, 0x80, 0x80);
    public static attribute THISTLE:Color= rgb(0xD8, 0xBF, 0xD8);
    public static attribute TOMATO:Color= rgb(0xFF, 0x63, 0x47);
    public static attribute TURQUOISE:Color= rgb(0x40, 0xE0, 0xD0);
    public static attribute VIOLET:Color= rgb(0xEE, 0x82, 0xEE);
    public static attribute WHEAT:Color= rgb(0xF5, 0xDE, 0xB3);
    public static attribute WHITE:Color= rgb(0xFF, 0xFF, 0xFF);
    public static attribute WHITESMOKE:Color= rgb(0xF5, 0xF5, 0xF5);
    public static attribute YELLOW:Color= rgb(0xFF, 0xFF, 0x00);
    public static attribute YELLOWGREEN:Color= rgb(0x9A, 0xCD, 0x32);
    public static attribute lightGray:Color = fromAWTColor(java.awt.Color.lightGray);

    public static function fromAWTColor(c:java.awt.Color):Color {
        Color {red: c.getRed()/255, green: c.getGreen()/255, 
            blue: c.getBlue()/ 255, opacity: c.getAlpha()/255};
    }

    public static function transparent (c:Color, opacity:Number):Color {
        Color { red:c.red, green:c.green, blue:c.blue, opacity: opacity};
    }

    public static function __INTERPOLATE_COLOR(color1:Color, color2:Color, t:Number):Color {
        return color1.interpolate(color2, t);
    }
    
    public static function EASEBOTH(a:Color[], t:Number):Color {
         //TODO JXFC-195
        //return __EASE(a, t, __EASEBOTH, __INTERPOLATE_COLOR);
        t = UIElement.__EASEBOTH(t);
        var off = t * (sizeof a-1);
        var i = off.intValue();
        var value1 = a[i];
        if (i + 1 == sizeof a) then {
            return value1;
        };
        var value2 = a[i+1];
        //return __INTERPOLATE_COLOR(value1, value2, off-i);
        return value1.interpolate(value2, off-i);
    };

    public static function EASEIN(a:Color[], t:Number):Color {
         //TODO JXFC-195
        //return __EASE(a, t, __EASEIN, __INTERPOLATE_COLOR);
        t = UIElement.__EASEIN(t);
        var off = t * (sizeof a-1);
        var i = off.intValue();
        var value1 = a[i];
        if (i + 1 == sizeof a) then {
            return value1;
        };
        var value2 = a[i+1];
        //return __INTERPOLATE_COLOR(value1, value2, off-i);
        return value1.interpolate(value2, off-i);
    };

    public static function EASEOUT(a:Color[], t:Number):Color {
        //TODO JXFC-195
        //return __EASE(a, t, __EASEOUT, __INTERPOLATE_COLOR);
        t = UIElement.__EASEOUT(t);
        var off = t * (sizeof a-1);
        var i = off.intValue();
        var value1 = a[i];
        if (i + 1 == sizeof a) then {
            return value1;
        };
        var value2 = a[i+1];
        //return __INTERPOLATE_COLOR(value1, value2, off-i);
        return value1.interpolate(value2, off-i);
    };

    public static function LINEAR(a:Color[], t:Number):Color {
        //TODO JXFC-195
        //return __EASE(a, t, function(t:Number) {return t;}, __INTERPOLATE_COLOR);
        var off = t * (sizeof a-1);
        var i = off.intValue();
        var value1 = a[i];
        if (i + 1 == sizeof a) then {
            return value1;
        };
        var value2 = a[i+1];
        //return __INTERPOLATE_COLOR(value1, value2, off-i);
        return value1.interpolate(value2, off-i);
    };
     
    public static function DISCRETE(a:Color[], t:Number):Color {
        return a[t.intValue()*(sizeof a -1)];
    }

}
