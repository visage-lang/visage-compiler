/**
 * JFXC-1496 - Error message with postincrement/decrement 
 * operators used in bind context.
 *
 *
 * @test/compile-error
 */
var v = 2;

// post/pre increment/decrement operators not 
// allowed in bind context.

var v1 = bind v++;
var v2 = bind ++v;
var v3 = bind v--;
var v4 = bind --v;

// assignment operators are not allowed in bind context

var v5 = bind v += 2;
var v6 = bind v -= 2;
var v7 = bind v *= 2;
var v8 = bind v /= 2;

