/**
 * Regression test JFXC-2114 : NPE when running RT_423Test -- should be caught
 *
 * @compilefirst jfxc2114WSD.fx
 * @test
 * @run
 */

public class jfxc2114FSD extends jfxc2114WSD {
  
    package override function setResizable(resizable: Boolean): Void {
        println("FrameStageDelegate.setResizable: {resizable}");
        (window as javax.swing.JFrame).setResizable(resizable);
        println("FrameStageDelegate.setResizable: DONE");
    }

}

function run() {
    var fsd = jfxc2114FSD{};
    fsd.setVisibility(true);
}
