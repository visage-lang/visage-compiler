/**
 * regression test: JFXC-2958 : NPE when binding a method from inherited JFX class
 * @test/fail
 */

mixin class Runnable {
public function run(): Void {
   println("I am a runnable");
}
}

class jfxc2958 extends Runnable {
}

jfxc2958 {}.run();
