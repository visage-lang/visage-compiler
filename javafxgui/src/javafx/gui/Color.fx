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

package javafx.gui;

import java.lang.Object;
import javafx.animation.Interpolatable;
import javax.swing.plaf.UIResource;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.HashMap;
import java.lang.NumberFormatException;

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
 * static utility methods.  The following lines of code all create the same
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

    private static attribute namedColors: Map;

    private function getAWTColor0() {
        if (awtColor == null) {
            awtColor = new java.awt.Color(red.floatValue(),
                                          green.floatValue(),
                                          blue.floatValue(),
                                          opacity.floatValue());
        }

        awtColor;
    }

    private static function getNamedColor(name: String): Color {
        if (namedColors == null) {
            createNamedColors();
        }

        namedColors.get(name) as Color;
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
    public function getAWTPaint(): java.awt.Paint {
        getAWTColor0();
    }

    /* //PENDING_DOC_REVIEW
     * Support for interpolation?
     * <p class="editor">This method should not be public. It is an implementation detail.</p>
     * @needsreview
     */
    public function ofTheWay(endVal:Object, t:Number): Object {
        var v2 = endVal as Color;
        Color {
            red:     red     + (v2.red     - red)     * t;
            green:   green   + (v2.green   - green)   * t;
            blue:    blue    + (v2.blue    - blue)    * t;
            opacity: opacity + (v2.opacity - opacity) * t;
        }
    }

    // PENDING_DOC_REVIEW
    /**
     * Creates a Color instance derived from the provided {@code java.awt.Color}. 
     * @needsreview
     */
    public static function fromAWTColor(c: java.awt.Color): Color {
        if (c == null) {
            return null;
        }

        var rgbo = c.getRGBComponents(null);
        var r = rgbo[0];
        var g = rgbo[1];
        var b = rgbo[2];
        var o = rgbo[3];

        // PENDING(shannonh) - the commented code below is good, but it requires
        // us to fix the sync pattern of colors/fonts/etc, which requires
        // compiler support.
        // if (c instanceof UIResource) {
        //     color(r, g, b, o);
        // } else {
            Color {red: r, green: g, blue: b, opacity: o, awtColor: c};
        // }
    }

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
    public static function color(red: Number, green: Number, blue: Number, opacity: Number): Color {
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
    public static function color(red: Number, green: Number, blue: Number): Color {
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
    public static function rgb(red: Integer, green: Integer, blue: Integer, opacity: Number): Color {
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
    public static function rgb(red: Integer, green: Integer, blue: Integer): Color {
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
    public static function hsb(hue: Number, saturation: Number, brightness: Number, opacity: Number) {
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
    public static function hsb(hue: Number, saturation: Number, brightness: Number) {
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
    public static function web(color: String, opacity: Number): Color {
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
    public static function web(color: String): Color {
        web(color, 1.0);
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
    public function toString(): String {
        "{getClass().getName()}[red={red},green={green},blue={blue},opacity={opacity}]";
    }

    // PENDING_DOC_REVIEW
    /**
     * Predefined {@code Color} constants. 
     *
     * @profile common
     */        
    public static /* constant */ attribute TRANSPARENT          = color(0.0, 0.0, 0.0, 0.0);

    public static /* constant */ attribute ALICEBLUE            = rgb(0xF0, 0xF8, 0xFF);
    public static /* constant */ attribute ANTIQUEWHITE         = rgb(0xFA, 0xEB, 0xD7);
    public static /* constant */ attribute AQUA                 = rgb(0x00, 0xFF, 0xFF);
    public static /* constant */ attribute AQUAMARINE           = rgb(0x7F, 0xFF, 0xD4);
    public static /* constant */ attribute AZURE                = rgb(0xF0, 0xFF, 0xFF);
    public static /* constant */ attribute BEIGE                = rgb(0xF5, 0xF5, 0xDC);
    public static /* constant */ attribute BISQUE               = rgb(0xFF, 0xE4, 0xC4);
    public static /* constant */ attribute BLACK                = rgb(0x00, 0x00, 0x00);
    public static /* constant */ attribute BLANCHEDALMOND       = rgb(0xFF, 0xEB, 0xCD);
    public static /* constant */ attribute BLUE                 = rgb(0x00, 0x00, 0xFF);
    public static /* constant */ attribute BLUEVIOLET           = rgb(0x8A, 0x2B, 0xE2);
    public static /* constant */ attribute BROWN                = rgb(0xA5, 0x2A, 0x2A);
    public static /* constant */ attribute BURLYWOOD            = rgb(0xDE, 0xB8, 0x87);
    public static /* constant */ attribute CADETBLUE            = rgb(0x5F, 0x9E, 0xA0);
    public static /* constant */ attribute CHARTREUSE           = rgb(0x7F, 0xFF, 0x00);
    public static /* constant */ attribute CHOCOLATE            = rgb(0xD2, 0x69, 0x1E);
    public static /* constant */ attribute CORAL                = rgb(0xFF, 0x7F, 0x50);
    public static /* constant */ attribute CORNFLOWERBLUE       = rgb(0x64, 0x95, 0xED);
    public static /* constant */ attribute CORNSILK             = rgb(0xFF, 0xF8, 0xDC);
    public static /* constant */ attribute CRIMSON              = rgb(0xDC, 0x14, 0x3C);
    public static /* constant */ attribute CYAN                 = rgb(0x00, 0xFF, 0xFF);
    public static /* constant */ attribute DARKBLUE             = rgb(0x00, 0x00, 0x8B);
    public static /* constant */ attribute DARKCYAN             = rgb(0x00, 0x8B, 0x8B);
    public static /* constant */ attribute DARKGOLDENROD        = rgb(0xB8, 0x86, 0x0B);
    public static /* constant */ attribute DARKGRAY             = rgb(0xA9, 0xA9, 0xA9);
    public static /* constant */ attribute DARKGREEN            = rgb(0x00, 0x64, 0x00);
    public static /* constant */ attribute DARKGREY             = DARKGRAY;
    public static /* constant */ attribute DARKKHAKI            = rgb(0xBD, 0xB7, 0x6B);
    public static /* constant */ attribute DARKMAGENTA          = rgb(0x8B, 0x00, 0x8B);
    public static /* constant */ attribute DARKOLIVEGREEN       = rgb(0x55, 0x6B, 0x2F);
    public static /* constant */ attribute DARKORANGE           = rgb(0xFF, 0x8C, 0x00);
    public static /* constant */ attribute DARKORCHID           = rgb(0x99, 0x32, 0xCC);
    public static /* constant */ attribute DARKRED              = rgb(0x8B, 0x00, 0x00);
    public static /* constant */ attribute DARKSALMON           = rgb(0xE9, 0x96, 0x7A);
    public static /* constant */ attribute DARKSEAGREEN         = rgb(0x8F, 0xBC, 0x8F);
    public static /* constant */ attribute DARKSLATEBLUE        = rgb(0x48, 0x3D, 0x8B);
    public static /* constant */ attribute DARKSLATEGRAY        = rgb(0x2F, 0x4F, 0x4F);
    public static /* constant */ attribute DARKSLATEGREY        = DARKSLATEGRAY;
    public static /* constant */ attribute DARKTURQUOISE        = rgb(0x00, 0xCE, 0xD1);
    public static /* constant */ attribute DARKVIOLET           = rgb(0x94, 0x00, 0xD3);
    public static /* constant */ attribute DEEPPINK             = rgb(0xFF, 0x14, 0x93);
    public static /* constant */ attribute DEEPSKYPBLUE         = rgb(0x00, 0xBF, 0xFF);
    public static /* constant */ attribute DIMGRAY              = rgb(0x69, 0x69, 0x69);
    public static /* constant */ attribute DIMGREY              = DIMGRAY;
    public static /* constant */ attribute DODGERBLUE           = rgb(0x1E, 0x90, 0xFF);
    public static /* constant */ attribute FIREBRICK            = rgb(0xB2, 0x22, 0x22);
    public static /* constant */ attribute FLORALWHITE          = rgb(0xFF, 0xFA, 0xF0);
    public static /* constant */ attribute FORESTGREEN          = rgb(0x22, 0x8B, 0x22);
    public static /* constant */ attribute FUCHSIA              = rgb(0xFF, 0x00, 0xFF);
    public static /* constant */ attribute GAINSBORO            = rgb(0xDC, 0xDC, 0xDC);
    public static /* constant */ attribute GHOSTWHITE           = rgb(0xF8, 0xF8, 0xFF);
    public static /* constant */ attribute GOLD                 = rgb(0xFF, 0xD7, 0x00);
    public static /* constant */ attribute GOLDENROD            = rgb(0xDA, 0xA5, 0x20);
    public static /* constant */ attribute GRAY                 = rgb(0x80, 0x80, 0x80);
    public static /* constant */ attribute GREEN                = rgb(0x00, 0x80, 0x00);
    public static /* constant */ attribute GREENYELLOW          = rgb(0xAD, 0xFF, 0x2F);
    public static /* constant */ attribute GREY                 = GRAY;
    public static /* constant */ attribute HONEYDEW             = rgb(0xF0, 0xFF, 0xF0);
    public static /* constant */ attribute HOTPINK              = rgb(0xFF, 0x69, 0xB4);
    public static /* constant */ attribute INDIANRED            = rgb(0xCD, 0x5C, 0x5C);
    public static /* constant */ attribute INDIGO               = rgb(0x4B, 0x00, 0x82);
    public static /* constant */ attribute IVORY                = rgb(0xFF, 0xFF, 0xF0);
    public static /* constant */ attribute KHAKI                = rgb(0xF0, 0xE6, 0x8C);
    public static /* constant */ attribute LAVENDER             = rgb(0xE6, 0xE6, 0xFA);
    public static /* constant */ attribute LAVENDERBLUSH        = rgb(0xFF, 0xF0, 0xF5);
    public static /* constant */ attribute LAWNGREEN            = rgb(0x7C, 0xFC, 0x00);
    public static /* constant */ attribute LEMONCHIFFON         = rgb(0xFF, 0xFA, 0xCD);
    public static /* constant */ attribute LIGHTBLUE            = rgb(0xAD, 0xD8, 0xE6);
    public static /* constant */ attribute LIGHTCORAL           = rgb(0xF0, 0x80, 0x80);
    public static /* constant */ attribute LIGHTCYAN            = rgb(0xE0, 0xFF, 0xFF);
    public static /* constant */ attribute LIGHTGOLDENRODYELLOW = rgb(0xFA, 0xFA, 0xD2);
    public static /* constant */ attribute LIGHTGRAY            = rgb(0xD3, 0xD3, 0xD3);
    public static /* constant */ attribute LIGHTGREEN           = rgb(0x90, 0xEE, 0x90);
    public static /* constant */ attribute LIGHTGREY            = LIGHTGRAY;
    public static /* constant */ attribute LIGHTPINK            = rgb(0xFF, 0xB6, 0xC1);
    public static /* constant */ attribute LIGHTSALMON          = rgb(0xFF, 0xA0, 0x7A);
    public static /* constant */ attribute LIGHTSEAGREEN        = rgb(0x20, 0xB2, 0xAA);
    public static /* constant */ attribute LIGHTSKYBLUE         = rgb(0x87, 0xCE, 0xFA);
    public static /* constant */ attribute LIGHTSLATEGRAY       = rgb(0x77, 0x88, 0x99);
    public static /* constant */ attribute LIGHTSLATEGREY       = LIGHTSLATEGRAY;
    public static /* constant */ attribute LIGHTSTEELBLUE       = rgb(0xB0, 0xC4, 0xDE);
    public static /* constant */ attribute LIGHTYELLOW          = rgb(0xFF, 0xFF, 0xE0);
    public static /* constant */ attribute LIME                 = rgb(0x00, 0xFF, 0x00);
    public static /* constant */ attribute LIMEGREEN            = rgb(0x32, 0xCD, 0x32);
    public static /* constant */ attribute LINEN                = rgb(0xFA, 0xF0, 0xE6);
    public static /* constant */ attribute MAGENTA              = rgb(0xFF, 0x00, 0xFF);
    public static /* constant */ attribute MAROON               = rgb(0x80, 0x00, 0x00);
    public static /* constant */ attribute MEDIUMAQUAMARINE     = rgb(0x66, 0xCD, 0xAA);
    public static /* constant */ attribute MEDIUMBLUE           = rgb(0x00, 0x00, 0xCD);
    public static /* constant */ attribute MEDIUMORCHID         = rgb(0xBA, 0x55, 0xD3);
    public static /* constant */ attribute MEDIUMPURPLE         = rgb(0x93, 0x70, 0xDB);
    public static /* constant */ attribute MEDIUMSEAGREEN       = rgb(0x3C, 0xB3, 0x71);
    public static /* constant */ attribute MEDIUMSLATEBLUE      = rgb(0x7B, 0x68, 0xEE);
    public static /* constant */ attribute MEDIUMSPRINGGREEN    = rgb(0x00, 0xFA, 0x9A);
    public static /* constant */ attribute MEDIUMTURQUOISE      = rgb(0x48, 0xD1, 0xCC);
    public static /* constant */ attribute MEDIUMVIOLETRED      = rgb(0xC7, 0x15, 0x85);
    public static /* constant */ attribute MIDNIGHTBLUE         = rgb(0x19, 0x19, 0x70);
    public static /* constant */ attribute MINTCREAM            = rgb(0xF5, 0xFF, 0xFA);
    public static /* constant */ attribute MISTYROSE            = rgb(0xFF, 0xE4, 0xE1);
    public static /* constant */ attribute MOCCASIN             = rgb(0xFF, 0xE4, 0xB5);
    public static /* constant */ attribute NAVAJOWHITE          = rgb(0xFF, 0xDE, 0xAD);
    public static /* constant */ attribute NAVY                 = rgb(0x00, 0x00, 0x80);
    public static /* constant */ attribute OLDLACE              = rgb(0xFD, 0xF5, 0xE6);
    public static /* constant */ attribute OLIVE                = rgb(0x80, 0x80, 0x00);
    public static /* constant */ attribute OLIVEDRAB            = rgb(0x6B, 0x8E, 0x23);
    public static /* constant */ attribute ORANGE               = rgb(0xFF, 0xA5, 0x00);
    public static /* constant */ attribute ORANGERED            = rgb(0xFF, 0x45, 0x00);
    public static /* constant */ attribute ORCHID               = rgb(0xDA, 0x70, 0xD6);
    public static /* constant */ attribute PALEGOLDENROD        = rgb(0xEE, 0xE8, 0xAA);
    public static /* constant */ attribute PALEGREEN            = rgb(0x98, 0xFB, 0x98);
    public static /* constant */ attribute PALETURQUOISE        = rgb(0xAF, 0xEE, 0xEE);
    public static /* constant */ attribute PALEVIOLETRED        = rgb(0xDB, 0x70, 0x93);
    public static /* constant */ attribute PAPAYAWHIP           = rgb(0xFF, 0xEF, 0xD5);
    public static /* constant */ attribute PEACHPUFF            = rgb(0xFF, 0xDA, 0xB9);
    public static /* constant */ attribute PERU                 = rgb(0xCD, 0x85, 0x3F);
    public static /* constant */ attribute PINK                 = rgb(0xFF, 0xC0, 0xCB);
    public static /* constant */ attribute PLUM                 = rgb(0xDD, 0xA0, 0xDD);
    public static /* constant */ attribute POWDERBLUE           = rgb(0xB0, 0xE0, 0xE6);
    public static /* constant */ attribute PURPLE               = rgb(0x80, 0x00, 0x80);
    public static /* constant */ attribute RED                  = rgb(0xFF, 0x00, 0x00);
    public static /* constant */ attribute ROSYBROWN            = rgb(0xBC, 0x8F, 0x8F);
    public static /* constant */ attribute ROYALBLUE            = rgb(0x41, 0x69, 0xE1);
    public static /* constant */ attribute SADDLEBROWN          = rgb(0x8B, 0x45, 0x13);
    public static /* constant */ attribute SALMON               = rgb(0xFA, 0x80, 0x72);
    public static /* constant */ attribute SANDYBROWN           = rgb(0xF4, 0xA4, 0x60);
    public static /* constant */ attribute SEAGREEN             = rgb(0x2E, 0x8B, 0x57);
    public static /* constant */ attribute SEASHELL             = rgb(0xFF, 0xF5, 0xEE);
    public static /* constant */ attribute SIENNA               = rgb(0xA0, 0x52, 0x2D);
    public static /* constant */ attribute SILVER               = rgb(0xC0, 0xC0, 0xC0);
    public static /* constant */ attribute SKYBLUE              = rgb(0x87, 0xCE, 0xEB);
    public static /* constant */ attribute SLATEBLUE            = rgb(0x6A, 0x5A, 0xCD);
    public static /* constant */ attribute SLATEGRAY            = rgb(0x70, 0x80, 0x90);
    public static /* constant */ attribute SLATEGREY            = SLATEGRAY;
    public static /* constant */ attribute SNOW                 = rgb(0xFF, 0xFA, 0xFA);
    public static /* constant */ attribute SPRINGGREEN          = rgb(0x00, 0xFF, 0x7F);
    public static /* constant */ attribute STEELBLUE            = rgb(0x46, 0x82, 0xB4);
    public static /* constant */ attribute TAN                  = rgb(0xD2, 0xB4, 0x8C);
    public static /* constant */ attribute TEAL                 = rgb(0x00, 0x80, 0x80);
    public static /* constant */ attribute THISTLE              = rgb(0xD8, 0xBF, 0xD8);
    public static /* constant */ attribute TOMATO               = rgb(0xFF, 0x63, 0x47);
    public static /* constant */ attribute TURQUOISE            = rgb(0x40, 0xE0, 0xD0);
    public static /* constant */ attribute VIOLET               = rgb(0xEE, 0x82, 0xEE);
    public static /* constant */ attribute WHEAT                = rgb(0xF5, 0xDE, 0xB3);
    public static /* constant */ attribute WHITE                = rgb(0xFF, 0xFF, 0xFF);
    public static /* constant */ attribute WHITESMOKE           = rgb(0xF5, 0xF5, 0xF5);
    public static /* constant */ attribute YELLOW               = rgb(0xFF, 0xFF, 0x00);
    public static /* constant */ attribute YELLOWGREEN          = rgb(0x9A, 0xCD, 0x32);

    private static function createNamedColors(): Void {
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
        colors.put("deepskypblue",         DEEPSKYPBLUE);
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

}
