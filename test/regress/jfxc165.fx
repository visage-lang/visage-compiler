/**
 * regression test:  Converting $Intf class references to a normal class.
 * @test
 * @compilefirst jfxc165a.fx
 * @compilefirst jfxc165b.fx 
 * @run
 */

class jfxc165 {
	var barS : jfxc165a = jfxc165b.BAR;
}
