/* For JFXC-344 - Cannot cannot find symbol "javax" within on replace block
 * Regression test: assign op, e.g., +=
 *
 * @test
 * @run
 */

public class jfxc344 { 
    var jdesk: javax.swing.JDesktopPane; 
    public var foo :String on replace { 
	var bar : java.lang.Object[] = [new jfxc344];
        var frames = jdesk.getAllFrames(); 
        var f = frames[0];
    } 
} 
