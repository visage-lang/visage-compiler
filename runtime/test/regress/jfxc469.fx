/*
 * @test
 * @run
 */

import javafx.ui.*;
import javafx.ui.canvas.*;

class Foo {
    attribute strokeAlpha : Number = 0.3;
    public attribute content: Node[] = [Rect {
        stroke: Color{red:1,green:1,blue:1,opacity: bind strokeAlpha}
    }]
}

var f = Foo{};
