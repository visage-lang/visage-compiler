/* regression test for the bug 849
 *
 * @test
 * @run
 */
import javafx.ui.Frame;
import java.lang.System; 


var frame = new Frame;
var window = frame.getWindow();
System.out.println("window.class={window.getClass()}");
var array = frame.getWindow().getContainerListeners(); 
System.out.println("array.class={array.getClass()}");
