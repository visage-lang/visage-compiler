/*
 * Compile-time error:
 *
 *   setAsShort(short) in com.sun.javafx.runtime.location.ShortVariable
 *   cannot be applied to (int)
 *             s++;
 *             ^
 * @test/fail
 */


// doesn't happen with a script var
var ss : Short = 99;

class jfxc2578 {
    // only happens with an instance variable
    public var s : Short = 99;

    function test() {
        // doesn't happen with a local var
        var sss : Short = 99;
        // Each of the following 2 lines causes a compilation failure
        s++;
        s--;
    }
}