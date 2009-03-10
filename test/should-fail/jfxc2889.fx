/**
 * Should fail test JFXC-2889 : ClassCastException JCIf to JCExpression in JavafxToJava.translateToExpression on member select in KeyFrame literal
 *
 * @test/compile-error
 */

import javafx.animation.Timeline;

var value = 1.0;

var timeline = Timeline {
   keyFrames: [
      at (0s) {
         value => 2.0;
      }
   ]
}

var timeline2 = Timeline {
   keyFrames: [
      at (0s) { timeline.play() }
   ]
}

