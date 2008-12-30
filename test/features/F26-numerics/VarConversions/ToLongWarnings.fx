/*
 * Assigning compatible values to a variable of the type Long expecting
 * a warning about the presicion loss.
 *
 * @compilefirst Values.fx
 * @test/warning
 */

var l : Long;

l = Values.ff;
l = Values.dd;
l = Values.nn;
