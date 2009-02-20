/* regression test for the bug 849
 *
 * @test
 * @run/fail
 * was @test
 * was @run
 * NOTE this test is BOGUS - it assumes null is converted to an empty array.
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


