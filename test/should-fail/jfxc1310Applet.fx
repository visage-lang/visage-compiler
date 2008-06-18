/*
 * @test/compile-error
 * @compile jfxc1310Application.fx
 */

public class jfxc1310Applet {
   private attribute app:jfxc1310Application;

    private function do() {
        app.content;
    }
} 
