/**
 * JFXC-3490 :  -Xjcov option causes NPE and assertion failure during compile.
 *
 * @compilearg -Xjcov
 * @test
 */

class jfxc3490 {
    var title : String;
}

jfxc3490 {
    title: "Hello";
}
