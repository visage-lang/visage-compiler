/**
 * JFXC-2988 :  var xx = 1.1e3s; causes NullPointerException
 *
 * @test
 * @run
 */

var x = 1.1e3s; // used to throw NPE from parser

println(x);

var y = 1.1E3s; // used to throw NPE from parser

println(y);
