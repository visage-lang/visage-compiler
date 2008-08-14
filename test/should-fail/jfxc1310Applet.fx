/*
 * @test/compile-error
 * @compile jfxc1310Application.fx
 */

public class jfxc1310Applet {
    attribute app:jfxc1310Application;

    function do() {
        app.content;
    }
}
