/**
 * Regression test JFXC-1450 : object literal local in bind
 *
 * @test
 * @run
 */

class DragNode { 
  var onMouseDragged : function(Integer) : Void;
  init {
    println("Creating new DragNode");
  }
}

var dragNode = bind DragNode {
  var dragX : Integer;
  onMouseDragged:
    function(newX : Integer):Void {
      dragX = newX;
    }
}

for (i in [1..10]) {
  dragNode.onMouseDragged(i)
}

