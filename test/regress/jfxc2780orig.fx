/**
 * JFXC-2780 : Can't compile when "bind rect.boundsInLocal.minX" in a function
 *
 * approx original test case
 *
 * @compilefirst jfxc2780Rect.fx
 * @test
 */

public function test(rect : jfxc2780Rect) : jfxc2780Rect {
    jfxc2780Rect {
	x:  bind rect.boundsInLocal.minX;
    }
}
