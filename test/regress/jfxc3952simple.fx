/**
 * JFXC-3952 : Samples: Mosaic fails with an NPE in initVars$
 *
 * Simple direct access
 *
 * @compilefirst jfxc3952mix.fx
 * @compilefirst jfxc3952sub.fx
 * @test
 * @run
 */

def fiv = jfxc3952sub{};
var sb = bind fiv.furb;

println(sb)
