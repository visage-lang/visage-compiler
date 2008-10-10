/*
 * toggle test
 *
 * @test/nocompare
 * @run
 */

import javafx.animation.*;
import java.lang.System;
import java.lang.AssertionError;

var ids: Integer[] = [0..15];
var golden: Integer[] = [0,1,2,3,4,5, 4,3,2,1,0];
var out: Integer[];

function runLater(ms: Duration, f: function(): Void): Void {
    Timeline {
        keyFrames: KeyFrame {
            time: ms
            action: f
        }
    }.play();
}

var t : Timeline = Timeline {
    repeatCount: 1
    keyFrames: [for (id in ids)
        KeyFrame {
            time: 100ms * indexof id
            action: function() {
                //System.out.println("id: {id}");
                insert id into out;
                if(id == 5) {
                    t.rate *= -1;
                }
            }
        },
    ]
};
t.play();

runLater(2000ms, check);
function check() {
    if(t.running) {
        t.stop();
        throw new AssertionError("test failed");
    }
    if(out != golden) {
        throw new AssertionError("test failed");
    }
    System.out.println("pass");
}

