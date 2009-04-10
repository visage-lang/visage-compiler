/*
 * Assigning incompatible values to a variable of the type Byte.
 * A compilation error is expected.
 *
 * @compilefirst Values.fx
 * @test/compile-error
 */

var b : Byte;

b = Values.dduu;
b = Values.bboo;
b = Values.sstt;
b = Values.nul;
b = Values.iSeq;
b = Values.fSeq;
