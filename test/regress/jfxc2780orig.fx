/**
 * JFXC-2780 : Can't compile when "bind rect.boundsInLocal.minX" in a function
 *
 * approx original test case
 *
 * @compilerfirst jfxc2780Rect.fx
 * @test
 */

public function test(rect : Jfxc2780Rect) : Jfxc2780Rect {
    Jfxc2780Rect {
	x:  bind rect.boundsInLocal.minX;
    }
}
