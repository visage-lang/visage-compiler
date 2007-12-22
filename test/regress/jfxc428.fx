/*
 * Regression test: Infer parameter type from context.
 *
 * @test
 * @run
 */

import java.lang.System;

public class CanvasMouseEvent {
    public attribute x: Number;
    public attribute y: Number;
}
public class Circle {
  public attribute onMouseClicked: function(e:CanvasMouseEvent):Void;
};
var c = Circle {
    onMouseClicked: function(mEvt) {
        System.out.println("mouse click: x={mEvt.x} y={mEvt.y}");
    }
} 
c.onMouseClicked(CanvasMouseEvent{x: 2; y: 3} );
