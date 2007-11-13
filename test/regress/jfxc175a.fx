/**
 * regression test: fix for one of the bugs in JFXC-175. The isssue is that the synthetic
 * class of a local ObjLit was generated at the top of the enclosing method and there was 
 * no access to any subsequent locall var declarations.
 * @test
 * @run
 */
class Bar {
	attribute a : Integer;
	function getA() : Integer { return a; }
}

class FooBar {
	function x() : Void {
		var methodFooBar : Integer = 0;
		var bar1 : Bar = new Bar;
		var barFoo : FooBar = new FooBar;
		Bar {
			function fooBar() : Void {
				methodFooBar++;
				bar1.a++;
				bar1.getA();
			}
		};
		var aaa = 0;

		if (barFoo == this) {
			aaa++;
		}

		aaa++;
	}
}