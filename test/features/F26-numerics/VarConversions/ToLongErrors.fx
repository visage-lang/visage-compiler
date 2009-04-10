/*
 * Assigning incompatible values to a variable of the type Long.
 * A compilation error is expected.
 *
 * @compilefirst Values.fx
 * @test/compile-error
 *
 */

var l : Long;

l = Values.dduu;
l = Values.bboo;
l = Values.sstt;
l = Values.nul;
l = Values.iSeq;

