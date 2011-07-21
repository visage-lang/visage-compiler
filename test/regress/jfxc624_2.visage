/**
 * JFXC-624 : Compiler can't find public method from its anonymous inner class.
 *
 * @test
 * @run
 */

class jfxc624_2 extends java.lang.Object {
    var runnable = java.lang.Runnable {
        public override function run() {
            println("Runnable.run started");
            outerCall();
        }
    }    
    
    public function outerCall(): Void {
        println("jfxc624_2.outerCall method called");
    }
}

jfxc624_2 {}.runnable.run();

