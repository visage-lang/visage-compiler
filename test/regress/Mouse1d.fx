/*
 * Make sure inheritance resolution works for more than 1 level deep inheritance hierarchy.
 * @test
 */

import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Object;

class Mouse1d extends CompositeNode {
    public attribute width: Number;
    public attribute height: Number;
    attribute gx: Number = 15;
    attribute gy: Number = 35;
    attribute leftColor: Number = 0.0;
    attribute rightColor: Number = 0.0;
    function update(x:Number):Void {
        leftColor = -0.002 * x/2 + 0.06;
        rightColor =  0.002 * x/2 + 0.06;

        gx = x/2;
        gy = 100-x/2;

        if (gx < 10) {
            gx = 10;
        } else if (gx > 90) {
            gx = 90;
        }

        if (gy > 90) {
            gy = 90;
        } else if (gy < 10) {
            gy = 10;
        }
    }
    public function composeNode():Node {
      return Clip {
            shape: Rect {width: bind width, height: bind height}
            content:
            [Rect {
                height: bind height
                width: bind width
                fill: Color.BLACK
                selectable: true
                onMouseMoved: function(e:CanvasMouseEvent) {
                    update(e.localX);
                }
            },
            Rect {
                x: bind width/4-gx,
                y: bind width/2-gx
                height: bind gx*2
                width: bind gx*2
                fill: Color { red: 0, green: bind leftColor + 0.4, blue: bind leftColor + 0.6}
            },
            Rect {
                x: bind width/1.33-gy,
                y: bind width/2-gy
                height: bind gy*2
                width: bind gy*2

                fill: Color { green: bind rightColor + 0.2, blue: bind rightColor + 0.4}
            }]
        };
    }
}

Frame {
    visible: true
    width: 200
    height: 200
    content: Canvas {
        content: [Mouse1d {
            width: 200
            height: 200
        }]
    }
}