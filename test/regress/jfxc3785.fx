/**
 * Regression test JFXC-3785 : compiled-bind: 5 errors when compiling liveconnect
 *
 * @compilefirst jfxc3785Sub.fx
 * @test
 */

class Test {
   var mn = jfxc3785Sub.MapNode{function f() {}}
}
