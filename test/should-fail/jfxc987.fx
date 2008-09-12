/**
 * Misleading error message in sequence declaration
 * @test/compile-error
 */

import java.lang.Object;

var a = ["a","b"];
var b:Integer[] = [];
var seq:Object = [a,b];
