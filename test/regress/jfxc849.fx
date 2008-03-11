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
var array = frame.getWindow().getContainerListeners(); 
System.out.println("array.class={array.getClass()}");
