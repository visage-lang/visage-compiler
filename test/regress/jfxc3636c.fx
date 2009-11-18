/**
 * Regression test: JFXC-3670 : Compiled bind: bind permeates object literal initializers
 *
 * @test
 */

import javafx.lang.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

var interpolator:Interpolator = null;

class Node {
var translateX:Number
}

var cond = true;
var myNode = Node{};

function rebuildKeyFrames():KeyFrame[] {
    var tx = if (cond) myNode.translateX else 5.0;
    [KeyFrame{}, KeyFrame {
        time: 10ms
        values: [
            myNode.translateX => tx tween interpolator,
        ]
        action: function() {action()}
    }]    
}

function action():Object { return null; }
