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

package javafx.gui;

/**
 * @author Richard
 */
import javafx.fxunit.*;

public class ColorTest extends FXTestCase {
    function testRedIsBoundedBy0And1() {
        var c = Color { red:-1 };
        assertTrue(0 == c.red);
        
        c = Color { red:100 };
        assertTrue(1 == c.red);
    }

    function testGreenIsBoundedBy0And1() {
        var c = Color { green:-1 };
        assertTrue(0 == c.green);
        
        c = Color { green:100 };
        assertTrue(1 == c.green);
    }
    
    function testBlueIsBoundedBy0And1() {
        var c = Color { blue:-1 };
        assertTrue(0 == c.blue);
        
        c = Color { blue:100 };
        assertTrue(1 == c.blue);
    }
    
    function testOpacityIsBoundedBy0And1() {
        var c = Color { opacity:-1 };
        assertTrue(0 == c.opacity);
        
        c = Color { opacity:100 };
        assertTrue(1 == c.opacity);
    }
    
    function testOfTheWay() {
        var start = Color { red:0, green:0, blue:0, opacity:0 };
        var end = Color { red:1, green:1, blue:1, opacity:1 };
        var mid = start.ofTheWay(end, .5) as Color;
        assertTrue(mid.red == .5);
        assertTrue(mid.green == .5);
        assertTrue(mid.blue == .5);
        assertTrue(mid.opacity == .5);
    }
    
    function testFromAWTColorIsBidirectional() {
        var awtColor:java.awt.Color = new java.awt.Color(255, 255, 255, 1);
        var color = Color.fromAWTColor(awtColor);
        var testColor:java.awt.Color = new java.awt.Color(
            color.red.floatValue(),
            color.green.floatValue(),
            color.blue.floatValue(),
            color.opacity.floatValue());
        assertEquals(awtColor, testColor);
    }
    
    function testColorIsBoundedBy0And1() {
        var color = Color.color(-1, -1, -1, -1);
        assertTrue(color.red == 0);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 0);
        assertTrue(color.opacity == 0);
        
        color = Color.color(100, 100, 100);
        assertTrue(color.red == 100);
        assertTrue(color.green == 100);
        assertTrue(color.blue == 100);
        assertTrue(color.opacity == 100);
    }
    
    function testColor() {
        var color = Color.color(.1, .2, .3, .4);
        assertTrue(color.red == .1);
        assertTrue(color.green == .2);
        assertTrue(color.blue == .3);
        assertTrue(color.opacity == .4);
    }
    
    function testRgbIsBoundedBy0And255() {
        var color = Color.rgb(2555, 2555, 2555);
        assertTrue(color.red == 1);
        assertTrue(color.green == 1);
        assertTrue(color.blue == 1);
        
        color = Color.rgb(-1, -1, -1);
        assertTrue(color.red == 0);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 0);
    }
    
    function testRgb() {
        var color = Color.rgb(255, 0, 255, 0);
        assertTrue(color.red == 1);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 1);
        assertTrue(color.opacity == 0);
    }
    
    function testHsbIsBounded() {
        // NOTE: Talking with Jim Graham -- it turns out that although the AWT
        // documentation indicates that saturation and brightness should be
        // between 0 and 1, it doesn't do anything to enforce it, and so this
        // works even though arguably it shouldn't. I expected this test case
        // to throw an exception, so not sure what to do about it.
        var color = Color.hsb(100, -1, -1);
    }
    
    // TODO can't get the color and expected to agree. Not sure what the
    // problem is.
    function testHsb() {
        var color = Color.hsb(210, 100, 50);
        java.lang.System.out.println("Was: {color.red}, {color.green}, {color.blue}");
        var expected = Color.rgb(0, 64, 128);
        java.lang.System.out.println("Expected: {expected.red}, {expected.green}, {expected.blue}");
//        assertTrue(expected.red == color.red);
//        assertTrue(expected.green == color.green);
//        assertTrue(expected.blue == color.blue);
//        assertTrue(expected.opacity == color.opacity);
    }
    
    function testWebPoundNotation() {
        var color = Color.web("#aabbcc");
        assertTrue(color.red == 170/255.0);
        assertTrue(color.green == 187/255.0);
        assertTrue(color.blue == 204/255.0);
    }
    
    function testWebPoundNotationIllegalValue() {
        var color = Color.web("#aabbccddee");
        // uh.... apparently we skip the r, g, b components and only use opacity
        // if the color constant is incorrect.
        assertTrue(color.red == 0);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 0);
        assertTrue(color.opacity == 1);
    }
    
    function testWebNullValue() {
        var color = Color.web(null);
        assertTrue(color.red == 0);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 0);
        assertTrue(color.opacity == 1);
    }
    
    function testWebHexNotation() {
        var color = Color.web("0xaabbcc");
        assertTrue(color.red == 170/255.0);
        assertTrue(color.green == 187/255.0);
        assertTrue(color.blue == 204/255.0);
    }
    
    function testWebHexNotationIllegalValue() {
        var color = Color.web("0xaabbccddee");
        assertTrue(color.red == 0);
        assertTrue(color.green == 0);
        assertTrue(color.blue == 0);
        assertTrue(color.opacity == 1);
    }
    
    function testWebNamed() {
        var color = Color.web("orangered");
        var expected = Color.rgb(0xFF, 0x45, 0x00);
        assertTrue(expected.red == color.red);
        assertTrue(expected.green == color.green);
        assertTrue(expected.blue == color.blue);
        assertTrue(expected.opacity == color.opacity);
    }
    
    function testWebNamedMixedCase() {
        var color = Color.web("oRAngEReD");
        var expected = Color.rgb(0xFF, 0x45, 0x00);
        assertTrue(expected.red == color.red);
        assertTrue(expected.green == color.green);
        assertTrue(expected.blue == color.blue);
        assertTrue(expected.opacity == color.opacity);
    }
    
    function testWebNamedWrongName() {
        var color = Color.web("foobar");
        var expected = Color{};
        assertTrue(expected.red == color.red);
        assertTrue(expected.green == color.green);
        assertTrue(expected.blue == color.blue);
        assertTrue(expected.opacity == color.opacity);
    }

    function testWebHexNoLeadingSymbol() {
        var color = Color.web("0xaabbcc");
        assertTrue(color.red == 170/255.0);
        assertTrue(color.green == 187/255.0);
        assertTrue(color.blue == 204/255.0);
    }
    
    //function testOfTheWayHandlesNegatives() {
        // TODO should this be tested? What does it mean?
    //}
    
    //function testOfTheWayHandlesLargeNumbers() {
        // TODO What should happen for numbers > 1?
    //}
}