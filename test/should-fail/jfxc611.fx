/*
 * Regression test: JFXC-611: Underlying generics implementation leaking through in error message
 *
 * @test/compile-error
 *
 * This should produce:
 *
 *     jfxc611.fx:29: unexpected type
 *     found   : Object
 *     required: Object[]
 *     function f(llist) {return llist[0];}
 *                               ^
 *     1 error
 *
 * instead of
 *
 *     jfxc611.fx:29: unexpected type
 *     found   : java.lang.Object
 *     required: com.sun.javafx.runtime.sequence.Sequence<? extends <any?>>
 *     function f(llist) {return llist[0];}
 *                               ^
 *     1 error
 *
 * There doesn't seem to be a way to provide .EXPECTED files 
 * for tests that are expected to fail compilation.
 *
 */

function f(llist) {return llist[0];}
var list = [1..10];
f(list); // yields 1
