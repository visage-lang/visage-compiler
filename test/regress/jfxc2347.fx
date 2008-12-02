/**
 * Regression test JFXC-2347 : Regression: NPE on dereference
 *
 * @test
 * @run
 */

class Node {
       public var focused: Boolean;
}
var focusPath: Node[];

focusPath[0].focused = false;
println(focusPath[0].focused);


