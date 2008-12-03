/**
 * Regression test for JFXC-2410 : Predefined type names for {Integer,Double,Float...}
 *
 * @test
 * @run
 */
var ch0 : Character;
var by0 : Byte;
var sh0 : Short;
var ln0 : Long;
var fl0 : Float;
var db0 : Double;

println(ch0);
println(by0);
println(sh0);
println(ln0);
println(fl0);
println(db0);

{
	var prev : Byte = 0;
	var t : Byte = prev + 1;
	while (t > prev) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}

{
	var prev : Short = 0;
	var t : Short = prev + 1;
	while (t > prev) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}

{
	var prev : Long = 0;
	var t : Long = prev + 1;
	while (t > prev) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}

{
	var prev : Float = 0;
	var t : Float = prev + 1;
	while (t != java.lang.Float.POSITIVE_INFINITY) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}

{
	var prev : Double = 0;
	var t : Double = prev + 1;
	while (t != java.lang.Double.POSITIVE_INFINITY) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}

{
	var prev : Number = 0;
	var t : Number = prev + 1;
	while (t != java.lang.Double.POSITIVE_INFINITY) {
		prev = t;
		t = t * 2;
	}
	println(prev);
}
