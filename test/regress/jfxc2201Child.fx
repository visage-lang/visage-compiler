/**
 * @compilefirst jfxc2201Parent.java
 * @test
 * @run
 */
public class jfxc2201Child extends jfxc2201Parent {
    public function foo(): Void {
        firePropertyChange("data", 1, 2);
    }
}
function run() {
  var ch = jfxc2201Child {};
  ch.foo();
}
