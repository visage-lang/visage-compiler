/**
 * regression test: JFXC-1220 : Compilation error while trying to index a sequence expression with predicates
 *
 * @test
 * @run
 */

import java.lang.System;

var nums =[20,14,15,55,33];
var firstNumberDivsiblebyThree=nums[k| k%3 ==0] [0]; // Indexing not allowed compilation error
System.out.println("{firstNumberDivsiblebyThree}");
