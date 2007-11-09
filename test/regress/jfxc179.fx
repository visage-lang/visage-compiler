/**
 * regression test: fix for JFXC-179.
 * @test
 * @run
 */

class Bar {
}

class BarSuper extends Bar {
}

class BarUser {
	function fooBar() : Void {
		BarSuper {
		} as Bar;
	}
}