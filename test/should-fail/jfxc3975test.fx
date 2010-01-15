/*
 * Regression test
 * JFXC-3975 : Compiled-bind: protected access error should be detected during attribution
 *
 * @compilefirst jfxc3975/jfxc3975b.fx
 * @test/compile-error
 *
 */

import jfxc3975.jfxc3975b;

class jfxc3975test extends jfxc3975b {
    function startup():Void {
        this.parent = null; // this works ok
        (this as jfxc3975b).parent = null; // error - parent is protected in jfxc3975b
    }
}
