/**
 * Test general private attributes.
 * @test
 * @compilefirst jfxc728One.fx
 * @run
 */

public class Two {
    private attribute attr : Number = 2.5;
    public function getAttr2() :Number { attr; }
    public function setAttr2(val :Number) :Void  { attr = val }
}

public class Three extends jfxc728One.One, Two {
    private attribute attr : String;
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

