/**
 * Test general private attributes.
 *
 * @compilefirst jfxc728One.fx
 * @test/fail
 * *run
 */

public class Three extends jfxc728One.One, jfxc728One.Two {
    var attr : String;
    public function getAttr3() :String { attr; }
    public function setAttr3(val :String) :Void  { attr = val }
}

function run( ) {
    var xx = Three {};
    xx.setAttr1(12);
    xx.setAttr3("xx3");
    //java.lang.System.out.println("xx");
    java.lang.System.out.println("xx.attr1: {xx.getAttr1()} .attr2: {xx.getAttr2()} .attr3: {xx.getAttr3()}.");
}

