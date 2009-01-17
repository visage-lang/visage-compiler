/*
 * Compile-time error:
 *
 *   setAsShort(short) in com.sun.javafx.runtime.location.ShortVariable
 *   cannot be applied to (int)
 *             s++;
 *             ^
 * @test
 * @run
 */


// doesn't happen with a script var
var ss : Short = 99;

class jfxc2578 {
    // only happens with an instance variable
    public var s : Short = 99;

    function test() {
        // doesn't happen with a local var
        var sss : Short = 99;
        sss++;
        println(sss);
        sss--;
        println(sss);
        // Each of the following 2 lines caused a compilation failure
        s++;
        println(s);
        s--;
        println(s);
    }
}

jfxc2578{}.test();
