/*
 * Regression test: for JFXC-3807: on invalidate is called the first time a binder is referenced after its bindee is modified
 *
 * @test
 * @run
 */

var bindee: Integer;

var binder = bind bindee
                on invalidate { println("--invalid");}
// on replace { println("--replace");}
;

println("ref binder");
binder; // doesn't call on invalidate
println("set bindee");
bindee = 89; // calls on invalidate
println("ref binder");
binder; // doesn't call on invalidate
binder; // doesn't call on invalidate
println("end");
