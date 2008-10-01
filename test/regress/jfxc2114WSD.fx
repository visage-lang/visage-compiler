/**
 * @subtest
 */

public class jfxc2114WSD {

    package var window : java.awt.Window = null;
    
    package function setResizable(resizable: Boolean): Void {
        println("WindowStageDelegate.setResizable: {resizable}");
    }

    public function setVisibility(visible: Boolean): Void {
        println("WindowStageDelegate.visible: visible {visible}");
        
        setResizable(visible);
    }
}
