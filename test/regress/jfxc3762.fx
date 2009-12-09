/**
 * Regression test JFXC-3762 : Compiled bind: runtime IllegalArgumentException: no such variable: 0 on Button{}
 *
 * @compilefirst jfxc3762M.fx
 * @compilefirst jfxc3762S.fx
 * @test
 * @run
 */

var xx = jfxc3762S{}; 
