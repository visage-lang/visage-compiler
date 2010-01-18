/*
 * @test
 * @run
 * From the JFXC-3904 bug reported by Eamonn McManus.
 */

import javafx.reflect.*;

public class SequenceListenerTest {
    public var x: Number;
    public var seq: Object[];
}

class DummyListener extends FXChangeListener {
    var id: String;

    override function onChange() {
        println('{id} changed!');
    }
}

public function run() {
    def fxContext = FXLocal.getContext();
    def seqListenerClass = fxContext.makeClassRef(SequenceListenerTest.class);
    def xVar = seqListenerClass.getVariable('x');
    def seqVar = seqListenerClass.getVariable('seq');
    def instance = SequenceListenerTest{};
    def mirror = fxContext.mirrorOf(instance);
    def xListener = DummyListener{id: "x"};
    def seqListener = DummyListener{id: "Sequence"};
    def xId = xVar.addChangeListener(mirror, xListener);
    def seqId = seqVar.addChangeListener(mirror, seqListener);
    instance.x = 1;
    instance.seq = [1];
    println('Removing listeners');
    xVar.removeChangeListener(mirror, xId);
    seqVar.removeChangeListener(mirror, seqId);
    println('Listeners should no longer be triggered');
    instance.x = 2;
    instance.seq = [2];
    println("Done.")
}
