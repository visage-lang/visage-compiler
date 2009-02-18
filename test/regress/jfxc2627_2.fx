/*
 * Regress test (2) for JFXC-2627
 *
 *  No rerender of custom component when
 *  bound to external variable
 *
 * This is slightly simplified test compared
 * to jfxc2627.fx.
 *
 * @test
 * @run
 */

var x = [ "venus", "mercury" ];
var y = bind x;

var z = bind [
	for (i in [0..<(sizeof y)]) {
	    y[i].toUpperCase()
	}
]; 

/*
 * "y" and "z" should differ only in case.
 * Size and content of y and z should be
 * same otherwise.
 */

println(y);
println(z);

insert "earth" into x;

println(y);
println(z);

insert ["mars", "jupiter"] into x;

println(y);
println(z);
