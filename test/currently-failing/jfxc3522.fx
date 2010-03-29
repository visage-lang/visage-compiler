/*
 * Bug JFXC-3522 PhotoFlip sample image does not flip
 * This test is currently-failing.  It runs correctly when run by hand but fails when run by junit.
 * 
 * @test
 * @compilefirst jfxc3522a.fx
 * @run/fail
 */

import javafx.animation.*;


class kcr extends jfxc3522a {
    var count = 0;
    var time:Number = 5.0 on replace { if (++count == 10) println("pass"); }
    public var anim = Timeline {
        keyFrames: [
            at(1s) { time=> 10.0 tween Interpolator.LINEAR},
        ]
    }
}

var v1 = kcr { };
v1.anim.play();