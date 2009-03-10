/**
 * Should fail test JFXC-2887 : Compiler crash: at(0s) { println(...); scale=>0.3; ...}
 *
 * @test/compile-error
 */

import javafx.animation.Timeline;

Timeline {
    keyFrames: [
        at (0s) { println("hi") }
    ]
}