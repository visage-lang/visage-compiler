/**
 * JFXC-3335 : Bound conversion of primitive to Object causes ClassCastException
 *
 * @test
 * @run
 */

def bb0 = bind String.format("%5d", 1 + 2);

function zup(x : Object) {x}
def bb1 = bind zup(1 + 2);

bound function bzup(x : Object) {x}
def bb2 = bind zup(1 + 2);

class cc {
   function zup(x : Object) {2}
}
var cx = cc{}
def bb3 = bind cx.zup(cc{});

def bb4 : Object = bind 1;

println(bb0);
println(bb1);
println(bb2);
println(bb3);
println(bb4);
