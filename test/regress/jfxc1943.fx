/**
 * Regression test JFXC-1943 : Overriding variable declaration in subclass fails
 *
 * @compilefirst jfxc1943Base.fx
 * @test
 * @run
 */

public class jfxc1943Sub extends jfxc1943Base {
    public override var content = ["Hi", "Shannon"];
}

function run() {
    println(jfxc1943Sub{}.content[0]);
}
