/*
 * @test/compile-error
 * @compile jfxc1310Application.fx
 */

public class jfxc1310Applet {
    var app:jfxc1310Application;

    function do() {
        app.content;
    }
}
