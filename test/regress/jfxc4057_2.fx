/**
 * JFXC-4057 :  post increment bug.
 *
 * @test
 * @run
 */

var j = 0; 
var t="test"; 
var s=t; 
var b = s.charAt(j).equals(t.charAt({var x = j; j = j + 1;x})); 
println(b); 
