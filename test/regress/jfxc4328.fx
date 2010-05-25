/*
 * Regression: JFXC-4328 - NPE at JavafxDecompose.java:1110.
 *
 * @test
 * @run
 *
 */

var x = [1, 2, 3]; 
var z = bind for (i in x[y | y != 2]) { i } 
println("{z}");
