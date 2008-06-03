/*
 * ColorTest.fx
 *
 * Created on Jun 2, 2008, 11:57:14 AM
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
    
    //function testOfTheWayHandlesNegatives() {
        // TODO should this be tested? What does it mean?
    //}
    
    //function testOfTheWayHandlesLargeNumbers() {
        // TODO What should happen for numbers > 1?
    //}
}