/**
 * Regress test for JFXC-3226: Error when attaching a trigger to a nativearray type
 *
 * @test/compile-error
 */

var text = "Hello, world";
var chars:nativearray of Character = text.toCharArray() on replace {
    println("Fired")
};
var chars_2:nativearray of Character = bind chars;
var chars_3:Character[] = bind chars;
