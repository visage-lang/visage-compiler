/* For JFXC-344 - Cannot cannot find symbol "javax" within on replace block
 * Regression test: assign op, e.g., +=
 *
 * @test
 * @run
 */

public class jfxc344 { 
    private attribute jdesk: javax.swing.JDesktopPane; 
    public attribute foo :String on replace { 
	var bar : java.lang.Object[] = [new jfxc344];
        var frames = jdesk.getAllFrames(); 
        var f = frames[0];
    } 
} 