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

import javafx.lang.FX;
import java.lang.Object;
import javafx.animation.Interpolatable;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.HashMap;
import java.lang.NumberFormatException;

// PENDING_DOC_REVIEW_2
/**
 * Creates an sRGB color with the specified red, green, blue,
 * and opacity values in the range (0.0 - 1.0). 
 * The actual color used in rendering depends on finding the best match 
 * given the color space available for a particular output device.
 *
 * @param red the red component, 0->1.0
 * @param green the green component, 0 -> 1.0
 * @param blue  the blue component, 0 -> 1.0
 * @param opacity the opacity component, 0 -> 1.0
 *
 * @profile common
 * @needsreview
 */        
public function color(red: Number, green: Number, blue: Number, opacity: Number): Color {
    Color {red: red, green: green, blue: blue, opacity: opacity}
}

// PENDING_DOC_REVIEW_2
/**
 * Creates an opaque sRGB color with the specified red, green, 
 * and blue values in the range (0.0 - 1.0). Opacity is defaulted to {@code 1.0}. 
 * The actual color used in rendering depends on finding the best match 
 * given the color space available for a particular output device.
 *
 * @param red the red component, 0->1.0
 * @param green the green component, 0 -> 1.0
 * @param blue  the blue component, 0 -> 1.0
 *
 * @profile common
 * @needsreview
 */        
public function color(red: Number, green: Number, blue: Number): Color {
    Color {red: red, green: green, blue: blue};
}

// PENDING_DOC_REVIEW_2
/**
 * Creates an sRGB color with the specified red, green, blue 
 * values in the range (0 - 255) and opacity value in the range (0.0 - 1.0).
 *
 * @param red the red component, 0->255
 * @param green the green component, 0->255
 * @param blue the blue component, 0->255
 * @param opacity the opacity component, 0->1.0
 *
 * @profile common
 * @cssclass needsreview
 */        
public function rgb(red: Integer, green: Integer, blue: Integer, opacity: Number): Color {
    Color {red: red / 255.0, green: green / 255.0, blue: blue / 255.0, opacity: opacity};
}

// PENDING_DOC_REVIEW_2
/**
 * Creates an opaque sRGB color with the specified red, green, 
 * and blue values in the range (0 - 255). 
 * The actual color used in rendering depends on finding the best match 
 * given the color space available for a given output device. 
 * Opacity is defaulted to 255.
 *
 * @param red the red component, 0->255
 * @param green the green component, 0->255
 * @param blue the blue component, 0->255
 *
 * @profile common
 * @needsreview
 */        
public function rgb(red: Integer, green: Integer, blue: Integer): Color {
    Color {red: red / 255.0, green: green / 255.0, blue: blue / 255.0};
}

// PENDING_DOC_REVIEW_2
/**
 * <p class="editor">
 * NOTE, the description of the H component is confusing. Why can't
 * the hue simply be from 0->360 (with wrapping. The devleoper shouldn't have to understand
 * how it is calculated.</p>
 * 
 * <p>Creates a {@code Color} object based on the specified values for 
 * the HSB color model. The {@code saturation}, {@code brightness}, 
 * and {@code opacity}  components should be floating-point values 
 * between zero and one (numbers in the range {@code 0.0 - 1.0)}. 
 * The {@code hue} component can be any floating-point number. 
 * The floor of this number is subtracted from it to create a fraction 
 * between 0 and 1. This fractional number is then multiplied by 360 
 * to produce the hue angle in the HSB color model.</p>
 * 
 * @param hue the hue component, 0->1.0 (wraps)
 * @param saturation the saturation of the color, 0->1.0
 * @param brightness the brightness of the color, 0->1.0
 * @param opacity the opacity component, 0->1.0
 *
 * @profile common
 * @needsreview
 */        
public function hsb(hue: Number, saturation: Number, brightness: Number, opacity: Number) {
    var base = java.awt.Color.getHSBColor(hue.floatValue(), saturation.floatValue(), brightness.floatValue());
    var rgb = base.getRGBColorComponents(null);
    Color {red: rgb[0], green: rgb[1], blue: rgb[2], opacity: opacity};
}

// PENDING_DOC_REVIEW_2
/**
 * Creates a {@code Color} object based on the specified values 
 * for the HSB color model. The {@code saturation} and {@code brightness} 
 * components should be floating-point values between zero and one 
 * (numbers in the range 0.0-1.0). The {@code hue} component can be 
 * any floating-point number. The floor of this number is subtracted 
 * from it to create a fraction between 0 and 1. 
 * This fractional number is then multiplied by 360 to produce the hue angle 
 * in the HSB color model. 
 *
 * @param hue the hue component, 0->1.0 (wraps)
 * @param saturation the saturation of the color, 0->1.0
 * @param brightness the brightness of the color, 0->1.0
 * 
 * @profile common
 * @needsreview
 */        
public function hsb(hue: Number, saturation: Number, brightness: Number) {
    var base = java.awt.Color.getHSBColor(hue.floatValue(), saturation.floatValue(), brightness.floatValue());
    var rgb = base.getRGBComponents(null);
    Color {red: rgb[0], green: rgb[1], blue: rgb[2]};
}

// PENDING_DOC_REVIEW_2
/**
 * Creates an RGB color specified with the hexadecimal notation. 
 * Opacity is in the range 0.0-1.0. ex:
 * 
 * <pre><code>
 * var c = Color.web("0xff6688",1.0);
 * var c = Color.web("#ff6688",1.0);
 * var c = Color.web("ff6688",1.0);
 * </code></pre>
 *
 * @param color the hexadecimal string to identify the RGB color
 * @param opacity the opacity component
 *
 * @profile common
 * @needsreview
 */        
public function web(color: String, opacity: Number): Color {
    color = color.toLowerCase();
    if (color.startsWith("#") or color.startsWith("0x")) {
        color = if (color.startsWith("#")) color.substring(1) else color.substring(2);

        var len = color.length();

        try {
            var r;
            var g;
            var b;

            if (len == 3) {
                r = java.lang.Integer.parseInt(color.substring(0, 1), 16);
                g = java.lang.Integer.parseInt(color.substring(1, 2), 16);
                b = java.lang.Integer.parseInt(color.substring(2, 3), 16);
                return Color.color(r / 15.0, g / 15.0, b / 15.0, opacity);
            } else if (len == 6) {
                r = java.lang.Integer.parseInt(color.substring(0, 2), 16);
                g = java.lang.Integer.parseInt(color.substring(2, 4), 16);
                b = java.lang.Integer.parseInt(color.substring(4, 6), 16);
                return Color.rgb(r, g, b, opacity);
            }
        } catch (nfe: NumberFormatException) {}
        
        return Color{opacity: opacity};
    } else {
        var col = getNamedColor(color);
        return if (col == null) {
                   Color{opacity: opacity};
               } else if (opacity == 1.0) {
                   col;
               } else {
                   Color.color(col.red, col.green, col.blue, opacity);
               }
    }
}

// PENDING_DOC_REVIEW_2
/**
 * Creates an RGB color specified with the hexadecimal notation. 
 * Opacity is set to {@code 1.0}. ex:
 * <pre><code>
 * var c = Color.web("0xff6688");
 * var c = Color.web("#ff6688");
 * var c = Color.web("ff6688");
 * </code></pre>
 *
 * @param color the hexadecimal string to identify the RGB color
 *
 * @profile common
 * @needsreview
 */        
public function web(color: String): Color {
    web(color, 1.0);
}

// PENDING_DOC_REVIEW
/**
 * Predefined {@code Color} constants. 
 *
 * @profile common
 */        
public /* const */ def TRANSPARENT          = color(0.0, 0.0, 0.0, 0.0);

public /* const */ def ALICEBLUE            = rgb(0xF0, 0xF8, 0xFF);
public /* const */ def ANTIQUEWHITE         = rgb(0xFA, 0xEB, 0xD7);
public /* const */ def AQUA                 = rgb(0x00, 0xFF, 0xFF);
public /* const */ def AQUAMARINE           = rgb(0x7F, 0xFF, 0xD4);
public /* const */ def AZURE                = rgb(0xF0, 0xFF, 0xFF);
public /* const */ def BEIGE                = rgb(0xF5, 0xF5, 0xDC);
public /* const */ def BISQUE               = rgb(0xFF, 0xE4, 0xC4);
public /* const */ def BLACK                = rgb(0x00, 0x00, 0x00);
public /* const */ def BLANCHEDALMOND       = rgb(0xFF, 0xEB, 0xCD);
public /* const */ def BLUE                 = rgb(0x00, 0x00, 0xFF);
public /* const */ def BLUEVIOLET           = rgb(0x8A, 0x2B, 0xE2);
public /* const */ def BROWN                = rgb(0xA5, 0x2A, 0x2A);
public /* const */ def BURLYWOOD            = rgb(0xDE, 0xB8, 0x87);
public /* const */ def CADETBLUE            = rgb(0x5F, 0x9E, 0xA0);
public /* const */ def CHARTREUSE           = rgb(0x7F, 0xFF, 0x00);
public /* const */ def CHOCOLATE            = rgb(0xD2, 0x69, 0x1E);
public /* const */ def CORAL                = rgb(0xFF, 0x7F, 0x50);
public /* const */ def CORNFLOWERBLUE       = rgb(0x64, 0x95, 0xED);
public /* const */ def CORNSILK             = rgb(0xFF, 0xF8, 0xDC);
public /* const */ def CRIMSON              = rgb(0xDC, 0x14, 0x3C);
public /* const */ def CYAN                 = rgb(0x00, 0xFF, 0xFF);
public /* const */ def DARKBLUE             = rgb(0x00, 0x00, 0x8B);
public /* const */ def DARKCYAN             = rgb(0x00, 0x8B, 0x8B);
public /* const */ def DARKGOLDENROD        = rgb(0xB8, 0x86, 0x0B);
public /* const */ def DARKGRAY             = rgb(0xA9, 0xA9, 0xA9);
public /* const */ def DARKGREEN            = rgb(0x00, 0x64, 0x00);
public /* const */ def DARKGREY             = DARKGRAY;
public /* const */ def DARKKHAKI            = rgb(0xBD, 0xB7, 0x6B);
public /* const */ def DARKMAGENTA          = rgb(0x8B, 0x00, 0x8B);
public /* const */ def DARKOLIVEGREEN       = rgb(0x55, 0x6B, 0x2F);
public /* const */ def DARKORANGE           = rgb(0xFF, 0x8C, 0x00);
public /* const */ def DARKORCHID           = rgb(0x99, 0x32, 0xCC);
public /* const */ def DARKRED              = rgb(0x8B, 0x00, 0x00);
public /* const */ def DARKSALMON           = rgb(0xE9, 0x96, 0x7A);
public /* const */ def DARKSEAGREEN         = rgb(0x8F, 0xBC, 0x8F);
public /* const */ def DARKSLATEBLUE        = rgb(0x48, 0x3D, 0x8B);
public /* const */ def DARKSLATEGRAY        = rgb(0x2F, 0x4F, 0x4F);
public /* const */ def DARKSLATEGREY        = DARKSLATEGRAY;
public /* const */ def DARKTURQUOISE        = rgb(0x00, 0xCE, 0xD1);
public /* const */ def DARKVIOLET           = rgb(0x94, 0x00, 0xD3);
public /* const */ def DEEPPINK             = rgb(0xFF, 0x14, 0x93);
public /* const */ def DEEPSKYBLUE          = rgb(0x00, 0xBF, 0xFF);
public /* const */ def DIMGRAY              = rgb(0x69, 0x69, 0x69);
public /* const */ def DIMGREY              = DIMGRAY;
public /* const */ def DODGERBLUE           = rgb(0x1E, 0x90, 0xFF);
public /* const */ def FIREBRICK            = rgb(0xB2, 0x22, 0x22);
public /* const */ def FLORALWHITE          = rgb(0xFF, 0xFA, 0xF0);
public /* const */ def FORESTGREEN          = rgb(0x22, 0x8B, 0x22);
public /* const */ def FUCHSIA              = rgb(0xFF, 0x00, 0xFF);
public /* const */ def GAINSBORO            = rgb(0xDC, 0xDC, 0xDC);
public /* const */ def GHOSTWHITE           = rgb(0xF8, 0xF8, 0xFF);
public /* const */ def GOLD                 = rgb(0xFF, 0xD7, 0x00);
public /* const */ def GOLDENROD            = rgb(0xDA, 0xA5, 0x20);
public /* const */ def GRAY                 = rgb(0x80, 0x80, 0x80);
public /* const */ def GREEN                = rgb(0x00, 0x80, 0x00);
public /* const */ def GREENYELLOW          = rgb(0xAD, 0xFF, 0x2F);
public /* const */ def GREY                 = GRAY;
public /* const */ def HONEYDEW             = rgb(0xF0, 0xFF, 0xF0);
public /* const */ def HOTPINK              = rgb(0xFF, 0x69, 0xB4);
public /* const */ def INDIANRED            = rgb(0xCD, 0x5C, 0x5C);
public /* const */ def INDIGO               = rgb(0x4B, 0x00, 0x82);
public /* const */ def IVORY                = rgb(0xFF, 0xFF, 0xF0);
public /* const */ def KHAKI                = rgb(0xF0, 0xE6, 0x8C);
public /* const */ def LAVENDER             = rgb(0xE6, 0xE6, 0xFA);
public /* const */ def LAVENDERBLUSH        = rgb(0xFF, 0xF0, 0xF5);
public /* const */ def LAWNGREEN            = rgb(0x7C, 0xFC, 0x00);
public /* const */ def LEMONCHIFFON         = rgb(0xFF, 0xFA, 0xCD);
public /* const */ def LIGHTBLUE            = rgb(0xAD, 0xD8, 0xE6);
public /* const */ def LIGHTCORAL           = rgb(0xF0, 0x80, 0x80);
public /* const */ def LIGHTCYAN            = rgb(0xE0, 0xFF, 0xFF);
public /* const */ def LIGHTGOLDENRODYELLOW = rgb(0xFA, 0xFA, 0xD2);
public /* const */ def LIGHTGRAY            = rgb(0xD3, 0xD3, 0xD3);
public /* const */ def LIGHTGREEN           = rgb(0x90, 0xEE, 0x90);
public /* const */ def LIGHTGREY            = LIGHTGRAY;
public /* const */ def LIGHTPINK            = rgb(0xFF, 0xB6, 0xC1);
public /* const */ def LIGHTSALMON          = rgb(0xFF, 0xA0, 0x7A);
public /* const */ def LIGHTSEAGREEN        = rgb(0x20, 0xB2, 0xAA);
public /* const */ def LIGHTSKYBLUE         = rgb(0x87, 0xCE, 0xFA);
public /* const */ def LIGHTSLATEGRAY       = rgb(0x77, 0x88, 0x99);
public /* const */ def LIGHTSLATEGREY       = LIGHTSLATEGRAY;
public /* const */ def LIGHTSTEELBLUE       = rgb(0xB0, 0xC4, 0xDE);
public /* const */ def LIGHTYELLOW          = rgb(0xFF, 0xFF, 0xE0);
public /* const */ def LIME                 = rgb(0x00, 0xFF, 0x00);
public /* const */ def LIMEGREEN            = rgb(0x32, 0xCD, 0x32);
public /* const */ def LINEN                = rgb(0xFA, 0xF0, 0xE6);
public /* const */ def MAGENTA              = rgb(0xFF, 0x00, 0xFF);
public /* const */ def MAROON               = rgb(0x80, 0x00, 0x00);
public /* const */ def MEDIUMAQUAMARINE     = rgb(0x66, 0xCD, 0xAA);
public /* const */ def MEDIUMBLUE           = rgb(0x00, 0x00, 0xCD);
public /* const */ def MEDIUMORCHID         = rgb(0xBA, 0x55, 0xD3);
public /* const */ def MEDIUMPURPLE         = rgb(0x93, 0x70, 0xDB);
public /* const */ def MEDIUMSEAGREEN       = rgb(0x3C, 0xB3, 0x71);
public /* const */ def MEDIUMSLATEBLUE      = rgb(0x7B, 0x68, 0xEE);
public /* const */ def MEDIUMSPRINGGREEN    = rgb(0x00, 0xFA, 0x9A);
public /* const */ def MEDIUMTURQUOISE      = rgb(0x48, 0xD1, 0xCC);
public /* const */ def MEDIUMVIOLETRED      = rgb(0xC7, 0x15, 0x85);
public /* const */ def MIDNIGHTBLUE         = rgb(0x19, 0x19, 0x70);
public /* const */ def MINTCREAM            = rgb(0xF5, 0xFF, 0xFA);
public /* const */ def MISTYROSE            = rgb(0xFF, 0xE4, 0xE1);
public /* const */ def MOCCASIN             = rgb(0xFF, 0xE4, 0xB5);
public /* const */ def NAVAJOWHITE          = rgb(0xFF, 0xDE, 0xAD);
public /* const */ def NAVY                 = rgb(0x00, 0x00, 0x80);
public /* const */ def OLDLACE              = rgb(0xFD, 0xF5, 0xE6);
public /* const */ def OLIVE                = rgb(0x80, 0x80, 0x00);
public /* const */ def OLIVEDRAB            = rgb(0x6B, 0x8E, 0x23);
public /* const */ def ORANGE               = rgb(0xFF, 0xA5, 0x00);
public /* const */ def ORANGERED            = rgb(0xFF, 0x45, 0x00);
public /* const */ def ORCHID               = rgb(0xDA, 0x70, 0xD6);
public /* const */ def PALEGOLDENROD        = rgb(0xEE, 0xE8, 0xAA);
public /* const */ def PALEGREEN            = rgb(0x98, 0xFB, 0x98);
public /* const */ def PALETURQUOISE        = rgb(0xAF, 0xEE, 0xEE);
public /* const */ def PALEVIOLETRED        = rgb(0xDB, 0x70, 0x93);
public /* const */ def PAPAYAWHIP           = rgb(0xFF, 0xEF, 0xD5);
public /* const */ def PEACHPUFF            = rgb(0xFF, 0xDA, 0xB9);
public /* const */ def PERU                 = rgb(0xCD, 0x85, 0x3F);
public /* const */ def PINK                 = rgb(0xFF, 0xC0, 0xCB);
public /* const */ def PLUM                 = rgb(0xDD, 0xA0, 0xDD);
public /* const */ def POWDERBLUE           = rgb(0xB0, 0xE0, 0xE6);
public /* const */ def PURPLE               = rgb(0x80, 0x00, 0x80);
public /* const */ def RED                  = rgb(0xFF, 0x00, 0x00);
public /* const */ def ROSYBROWN            = rgb(0xBC, 0x8F, 0x8F);
public /* const */ def ROYALBLUE            = rgb(0x41, 0x69, 0xE1);
public /* const */ def SADDLEBROWN          = rgb(0x8B, 0x45, 0x13);
public /* const */ def SALMON               = rgb(0xFA, 0x80, 0x72);
public /* const */ def SANDYBROWN           = rgb(0xF4, 0xA4, 0x60);
public /* const */ def SEAGREEN             = rgb(0x2E, 0x8B, 0x57);
public /* const */ def SEASHELL             = rgb(0xFF, 0xF5, 0xEE);
public /* const */ def SIENNA               = rgb(0xA0, 0x52, 0x2D);
public /* const */ def SILVER               = rgb(0xC0, 0xC0, 0xC0);
public /* const */ def SKYBLUE              = rgb(0x87, 0xCE, 0xEB);
public /* const */ def SLATEBLUE            = rgb(0x6A, 0x5A, 0xCD);
public /* const */ def SLATEGRAY            = rgb(0x70, 0x80, 0x90);
public /* const */ def SLATEGREY            = SLATEGRAY;
public /* const */ def SNOW                 = rgb(0xFF, 0xFA, 0xFA);
public /* const */ def SPRINGGREEN          = rgb(0x00, 0xFF, 0x7F);
public /* const */ def STEELBLUE            = rgb(0x46, 0x82, 0xB4);
public /* const */ def TAN                  = rgb(0xD2, 0xB4, 0x8C);
public /* const */ def TEAL                 = rgb(0x00, 0x80, 0x80);
public /* const */ def THISTLE              = rgb(0xD8, 0xBF, 0xD8);
public /* const */ def TOMATO               = rgb(0xFF, 0x63, 0x47);
public /* const */ def TURQUOISE            = rgb(0x40, 0xE0, 0xD0);
public /* const */ def VIOLET               = rgb(0xEE, 0x82, 0xEE);
public /* const */ def WHEAT                = rgb(0xF5, 0xDE, 0xB3);
public /* const */ def WHITE                = rgb(0xFF, 0xFF, 0xFF);
public /* const */ def WHITESMOKE           = rgb(0xF5, 0xF5, 0xF5);
public /* const */ def YELLOW               = rgb(0xFF, 0xFF, 0x00);
public /* const */ def YELLOWGREEN          = rgb(0x9A, 0xCD, 0x32);

private var namedColors: Map;

private function getNamedColor(name: String): Color {
    if (namedColors == null) {
        createNamedColors();
    }

    namedColors.get(name) as Color;
}

private function createNamedColors(): Void {
    var colors = new HashMap(256);
    colors.put("aliceblue",            ALICEBLUE);
    colors.put("antiquewhite",         ANTIQUEWHITE);
    colors.put("aqua",                 AQUA);
    colors.put("aquamarine",           AQUAMARINE);
    colors.put("azure",                AZURE);
    colors.put("beige",                BEIGE);
    colors.put("bisque",               BISQUE);
    colors.put("black",                BLACK);
    colors.put("blanchedalmond",       BLANCHEDALMOND);
    colors.put("blue",                 BLUE);
    colors.put("blueviolet",           BLUEVIOLET);
    colors.put("brown",                BROWN);
    colors.put("burlywood",            BURLYWOOD);
    colors.put("cadetblue",            CADETBLUE);
    colors.put("chartreuse",           CHARTREUSE);
    colors.put("chocolate",            CHOCOLATE);
    colors.put("coral",                CORAL);
    colors.put("cornflowerblue",       CORNFLOWERBLUE);
    colors.put("cornsilk",             CORNSILK);
    colors.put("crimson",              CRIMSON);
    colors.put("cyan",                 CYAN);
    colors.put("darkblue",             DARKBLUE);
    colors.put("darkcyan",             DARKCYAN);
    colors.put("darkgoldenrod",        DARKGOLDENROD);
    colors.put("darkgray",             DARKGRAY);
    colors.put("darkgreen",            DARKGREEN);
    colors.put("darkgrey",             DARKGREY);
    colors.put("darkkhaki",            DARKKHAKI);
    colors.put("darkmagenta",          DARKMAGENTA);
    colors.put("darkolivegreen",       DARKOLIVEGREEN);
    colors.put("darkorange",           DARKORANGE);
    colors.put("darkorchid",           DARKORCHID);
    colors.put("darkred",              DARKRED);
    colors.put("darksalmon",           DARKSALMON);
    colors.put("darkseagreen",         DARKSEAGREEN);
    colors.put("darkslateblue",        DARKSLATEBLUE);
    colors.put("darkslategray",        DARKSLATEGRAY);
    colors.put("darkslategrey",        DARKSLATEGREY);
    colors.put("darkturquoise",        DARKTURQUOISE);
    colors.put("darkviolet",           DARKVIOLET);
    colors.put("deeppink",             DEEPPINK);
    colors.put("deepskyblue",          DEEPSKYBLUE);
    colors.put("dimgray",              DIMGRAY);
    colors.put("dimgrey",              DIMGREY);
    colors.put("dodgerblue",           DODGERBLUE);
    colors.put("firebrick",            FIREBRICK);
    colors.put("floralwhite",          FLORALWHITE);
    colors.put("forestgreen",          FORESTGREEN);
    colors.put("fuchsia",              FUCHSIA);
    colors.put("gainsboro",            GAINSBORO);
    colors.put("ghostwhite",           GHOSTWHITE);
    colors.put("gold",                 GOLD);
    colors.put("goldenrod",            GOLDENROD);
    colors.put("gray",                 GRAY);
    colors.put("green",                GREEN);
    colors.put("greenyellow",          GREENYELLOW);
    colors.put("grey",                 GREY);
    colors.put("honeydew",             HONEYDEW);
    colors.put("hotpink",              HOTPINK);
    colors.put("indianred",            INDIANRED);
    colors.put("indigo",               INDIGO);
    colors.put("ivory",                IVORY);
    colors.put("khaki",                KHAKI);
    colors.put("lavender",             LAVENDER);
    colors.put("lavenderblush",        LAVENDERBLUSH);
    colors.put("lawngreen",            LAWNGREEN);
    colors.put("lemonchiffon",         LEMONCHIFFON);
    colors.put("lightblue",            LIGHTBLUE);
    colors.put("lightcoral",           LIGHTCORAL);
    colors.put("lightcyan",            LIGHTCYAN);
    colors.put("lightgoldenrodyellow", LIGHTGOLDENRODYELLOW);
    colors.put("lightgray",            LIGHTGRAY);
    colors.put("lightgreen",           LIGHTGREEN);
    colors.put("lightgrey",            LIGHTGREY);
    colors.put("lightpink",            LIGHTPINK);
    colors.put("lightsalmon",          LIGHTSALMON);
    colors.put("lightseagreen",        LIGHTSEAGREEN);
    colors.put("lightskyblue",         LIGHTSKYBLUE);
    colors.put("lightslategray",       LIGHTSLATEGRAY);
    colors.put("lightslategrey",       LIGHTSLATEGREY);
    colors.put("lightsteelblue",       LIGHTSTEELBLUE);
    colors.put("lightyellow",          LIGHTYELLOW);
    colors.put("lime",                 LIME);
    colors.put("limegreen",            LIMEGREEN);
    colors.put("linen",                LINEN);
    colors.put("magenta",              MAGENTA);
    colors.put("maroon",               MAROON);
    colors.put("mediumaquamarine",     MEDIUMAQUAMARINE);
    colors.put("mediumblue",           MEDIUMBLUE);
    colors.put("mediumorchid",         MEDIUMORCHID);
    colors.put("mediumpurple",         MEDIUMPURPLE);
    colors.put("mediumseagreen",       MEDIUMSEAGREEN);
    colors.put("mediumslateblue",      MEDIUMSLATEBLUE);
    colors.put("mediumspringgreen",    MEDIUMSPRINGGREEN);
    colors.put("mediumturquoise",      MEDIUMTURQUOISE);
    colors.put("mediumvioletred",      MEDIUMVIOLETRED);
    colors.put("midnightblue",         MIDNIGHTBLUE);
    colors.put("mintcream",            MINTCREAM);
    colors.put("mistyrose",            MISTYROSE);
    colors.put("moccasin",             MOCCASIN);
    colors.put("navajowhite",          NAVAJOWHITE);
    colors.put("navy",                 NAVY);
    colors.put("oldlace",              OLDLACE);
    colors.put("olive",                OLIVE);
    colors.put("olivedrab",            OLIVEDRAB);
    colors.put("orange",               ORANGE);
    colors.put("orangered",            ORANGERED);
    colors.put("orchid",               ORCHID);
    colors.put("palegoldenrod",        PALEGOLDENROD);
    colors.put("palegreen",            PALEGREEN);
    colors.put("paleturquoise",        PALETURQUOISE);
    colors.put("palevioletred",        PALEVIOLETRED);
    colors.put("papayawhip",           PAPAYAWHIP);
    colors.put("peachpuff",            PEACHPUFF);
    colors.put("peru",                 PERU);
    colors.put("pink",                 PINK);
    colors.put("plum",                 PLUM);
    colors.put("powderblue",           POWDERBLUE);
    colors.put("purple",               PURPLE);
    colors.put("red",                  RED);
    colors.put("rosybrown",            ROSYBROWN);
    colors.put("royalblue",            ROYALBLUE);
    colors.put("saddlebrown",          SADDLEBROWN);
    colors.put("salmon",               SALMON);
    colors.put("sandybrown",           SANDYBROWN);
    colors.put("seagreen",             SEAGREEN);
    colors.put("seashell",             SEASHELL);
    colors.put("sienna",               SIENNA);
    colors.put("silver",               SILVER);
    colors.put("skyblue",              SKYBLUE);
    colors.put("slateblue",            SLATEBLUE);
    colors.put("slategray",            SLATEGRAY);
    colors.put("slategrey",            SLATEGREY);
    colors.put("snow",                 SNOW);
    colors.put("springgreen",          SPRINGGREEN);
    colors.put("steelblue",            STEELBLUE);
    colors.put("tan",                  TAN);
    colors.put("teal",                 TEAL);
    colors.put("thistle",              THISTLE);
    colors.put("tomato",               TOMATO);
    colors.put("turquoise",            TURQUOISE);
    colors.put("violet",               VIOLET);
    colors.put("wheat",                WHEAT);
    colors.put("white",                WHITE);
    colors.put("whitesmoke",           WHITESMOKE);
    colors.put("yellow",               YELLOW);
    colors.put("yellowgreen",          YELLOWGREEN);
    namedColors = colors;
}

// PENDING_DOC_REVIEW
/**
 * Creates a Color instance derived from the provided {@code java.awt.Color}. 
 * @needsreview
 */
public function fromAWTColor(c: java.awt.Color): Color {
    if (c == null) {
        return null;
    }

    var rgbo = c.getRGBComponents(null);
    var r = rgbo[0];
    var g = rgbo[1];
    var b = rgbo[2];
    var o = rgbo[3];

    Color {red: r, green: g, blue: b, opacity: o};
}

// PENDING_DOC_REVIEW_2
/**
 * <p class="editor">
 * NOTE: this definition, while correct, contains a lot of information which
 * is irrelevant to most developers. We should get to the basic definition and
 * usage patterns sooner.</p>
 *
 * <p>The Color class is used to encapsulate colors in the default sRGB color space. 
 * Every color has an implicit alpha value of 1.0 or an explicit one provided 
 * in the constructor. The alpha value defines the transparency of a color 
 * and can be  represented by a float value in the range 0.0-1.0 or 0-255. 
 * An alpha value of 1.0 or 255 means that the color is completely opaque 
 * and an alpha value of 0 or 0.0 means that the color is completely transparent. 
 * When constructing a {@code Color} with an explicit alpha or getting 
 * the color/alpha components of a Color, 
 * the color components are never premultiplied by the alpha component.
 * </p>
 * 
 * <p>{@code Color}s can be created with the constructor or with one of several
 * utility methods.  The following lines of code all create the same
 * blue color:</p>
 *
 * <pre><code>
 * var c = Color.BLUE;   //use the blue constant
 * var c = Color { red: 0 green: 0 blue: 1.0 }; // standard constructor
 
 * var c = Color.color(0,0,1.0); //use 0->1.0 values. implicit alpha of 1.0
 * var c = Color.color(0,0,1.0,1.0); //use 0->1.0 values, explicit alpha of 1.0
 
 * var c = Color.rgb(0,0,255); //use 0->255 integers, implict alpha of 1.0
 * var c = Color.rgb(0,0,255,1.0); //use 0->255 integers, explict alpha of 1.0
 * <b>//NOTE, shouldn't Color.rgb use 0->255 for the alpha?</b>
 
 * var c = Color.hsb(270,1.0,1.0); //hue = 270, saturation & value = 1.0. inplict alpha of 1.0
 * var c = Color.hsb(270,1.0,1.0,1.0); //hue = 270, saturation & value = 1.0, explict alpha of 1.0
 
 * var c = Color.web("0x0000FF",1.0);// blue as a hex web value, explict alpha
 * var c = Color.web("0x0000FF");// blue as a hex web value, implict alpha
 * var c = Color.web("#0000FF",1.0);// blue as a hex web value, explict alpha
 * var c = Color.web("#0000FF");// blue as a hex web value, implict alpha
 * var c = Color.web("0000FF",1.0);// blue as a hex web value, explict alpha
 * var c = Color.web("0000FF");// blue as a hex web value, implict alpha
 
 * var c = Color.fromAWTColor(java.awt.Color.BLUE }; //convert from an AWT color
 
 * 
 * 
 * </code></pre>
 *
 * Note. Once created the attributes of a color should never be modified.
 
 * @profile common
 * @needsreview
 */    
public /* final */ class Color extends Paint, Interpolatable {

    // PENDING_DOC_REVIEW
    /**
     * Defines the color red in the default sRGB space. 
     * This attribute should not be modifed once set in the constructor.
     *
     * @profile common
     * @setonce
     * @needsreview
     */    
    public /* set-once */ attribute red: Number;

    // PENDING_DOC_REVIEW
    /**
     * Defines the color green in the default sRGB space. 
     * This attribute should not be modifed once set in the constructor.
     *
     * @profile common
     * @setonce
     * @needsreview
     */        
    public /* set-once */ attribute green: Number;

    // PENDING_DOC_REVIEW
    /**
     * Defines the color blue in the default sRGB space. 
     * This attribute should not be modifed once set in the constructor.
     *
     * @profile common
     * @setonce
     * @needsreview
     */        
    public /* set-once */ attribute blue: Number;

    // PENDING_DOC_REVIEW
    /**
     * Defines the opacity for this color. The default value is 1.0.
     * This attribute should not be modifed once set in the constructor.
     *
     * @profile common
     * @setonce
     * @needsreview
     */        
    public /* set-once */ attribute opacity: Number = 1.0;

    private attribute awtColor: java.awt.Color;

    private function getAWTColor0() {
        if (awtColor == null) {
            awtColor = new java.awt.Color(red.floatValue(),
                                          green.floatValue(),
                                          blue.floatValue(),
                                          opacity.floatValue());
        }

        awtColor;
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code java.awt.Color} delegate for this Color.
     * <p class="editor">This method should not be public. It is an implementation detail.</p>
     * @needsreview
     */
    public function getAWTColor(): java.awt.Color {
        getAWTColor0();
    }

    // PENDING_DOC_REVIEW
    /**
     * Returns the {@code java.awt.Paint} delegate for this Color.
     * <p class="editor">This method should not be public. It is an implementation detail.</p>
     * @needsreview
     */
    override function getAWTPaint(): java.awt.Paint {
        getAWTColor0();
    }

    /**
     * @treatasprivate implementation detail
     */
    override function ofTheWay(endVal:Object, t:Number): Object {
        var v2 = endVal as Color;
        Color {
            red:     red     + (v2.red     - red)     * t;
            green:   green   + (v2.green   - green)   * t;
            blue:    blue    + (v2.blue    - blue)    * t;
            opacity: opacity + (v2.opacity - opacity) * t;
        }
    }

    /**
     * @inheritDoc
     */
    override function equals(obj: Object) : Boolean {
        if (FX.isSameObject(obj, this)) then return true;
        if (obj instanceof Color) {
            var other = obj as Color;
            return red == other.red 
                and blue == other.blue
                and green == other.green
                and opacity == other.opacity;
        } else false;
    }
    
    // PENDING_DOC_REVIEW
    /**
     * Returns a string representation of this {@code Color}. 
     * This method is intended to be used only for debugging purposes. 
     * The content and format of the returned string might vary between implementations. 
     * The returned string might be empty but cannot be {@code null}.
     *
     * @profile common
     */        
    override function toString(): String {
        "{getClass().getName()}[red={red},green={green},blue={blue},opacity={opacity}]";
    }

}
