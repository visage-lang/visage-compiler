/**
 * JFXC-3942 : missing updates on bind to external script-level var
 *
 * Static reference via instance
 *
 * @compilefirst jfxc3942viaInstSub.fx
 * @test
 * @run
 */

var refv = jfxc3942viaInstSub{}.ref;
def bt = bind refv.target;
println(bt);
refv.target = "set";
println(bt);

