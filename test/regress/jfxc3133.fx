/**
 * JFXC-3133 :  Cannot animate Color inside on-replace trigger
 *
 * @test
 * @compile jfxc3133.fx
 */

import javafx.animation.*;

var j : Integer;

var i : Integer on replace {
    var tl = Timeline {
        repeatCount: Timeline.INDEFINITE
        autoReverse: true
        keyFrames: [
            KeyFrame {
                time: 1s
                values: [ j => 10 tween Interpolator.LINEAR ]
            }
        ]
    };
    tl.playFromStart();
} 
