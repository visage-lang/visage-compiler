/**
 * Regression test JFXC-412 : Functional binding does not work as expected
 *
 * @test
 * @run
 */

import java.lang.System;
import javafx.ui.*;
class Foo {
    attribute c: Color;
    attribute awtColor = bind c.getColor() on replace {newAwtColor = this.awtColor;}
    attribute newAwtColor: java.awt.Color;
}
var foo = Foo { c: Color { red: 1 } };
foo.c.red = 0.5;
System.out.println(foo.newAwtColor == foo.awtColor); // true
System.out.println(foo.awtColor.getRed()); // 255 - which is wrong - should be 128
