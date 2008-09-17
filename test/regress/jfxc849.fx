/* regression test for the bug 849
 *
 * @test
 * @run
 */

import java.lang.System; 

class Frame {
	function getWindow(): java.awt.Window { return null; }
}

var frame = new Frame;
var listeners = frame.getWindow().getContainerListeners(); 
for (l in listeners) {
  var cl : java.awt.event.ContainerListener = l;
  println(cl);
};


