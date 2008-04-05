/**
 * Regression test JFXC-878 : Cannot handle DoubleVar or IntVar in ranges or Java methods.
 *
 * @test
 */

import java.lang.Math;
import javafx.ui.*;
import javafx.ui.canvas.*;
class Foo {
    attribute mouseX:Number = 100;
   
    attribute content:Node =Polygon {
            var w = 7;
            var h = 5; 
            transform: bind Transform.translate(Math.max(mouseX - w/2, -w/2), 10-h)
            fill: Color.BLACK
            points: [0, 0, 7, 0, 7/2, 5]
        } ;
}
