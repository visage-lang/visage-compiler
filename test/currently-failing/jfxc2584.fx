/*
 * No warning for the lossy type casts { Short; Integer; Long } => Byte
 * Please update test/features/F26-numerics/VarConversions/ToByteWarnings.fx
 * when this issue is resolved.
 *
 * @test/warning
 */

var b : Byte;
var i : Integer;
var s : Short;
var l : Long;

b = i;
b = s;
b = l;


/*****************************
The following precision loss warnings are expected:

test/currently-failing/jfxc2584.fx:14: warning: possible loss of precision
found   : Integer
required: Byte
b = i;
    ^
test/currently-failing/jfxc2584.fx:15: warning: possible loss of precision
found   : Short
required: Byte
b = s;
    ^
test/currently-failing/jfxc2584.fx:16: warning: possible loss of precision
found   : Long
required: Byte
b = l;
    ^
3 warnings
******************************/
