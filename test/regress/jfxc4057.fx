/**
 * JFXC-4057 :  post increment bug.
 *
 * @test
 * @run
 */

var j = 0;
var t = "test"; 
var s = t; 
println("{s.charAt(j).equals(t.charAt(j++))}"); 

