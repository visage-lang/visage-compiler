/**
*   JavaFXBalls.fx
*
*   JavaFX Bubblemark demo 
*   www.bubblemark.com
*
*   License: The code is released under Creative Commons Attribution 2.5 License
*   (http://creativecommons.org/licenses/by/2.5/)
*/

package javafxballs;

import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;
import java.lang.Math;

class BallsTest {
    attribute _is_running: Boolean = true;
    attribute _frames:Number = 0;
    attribute _startTime:Number = System.currentTimeMillis();

    attribute _N:Integer = 16 on replace {
        delete balls;
        foreach (i in [1.._N]) {
            insert JavaFxBall{img:ball{}} into balls; 
        }

        System.out.println("New number of balls is {_N}");
        System.out.println("sizeof balls is {sizeof balls}");
    };
    
    attribute timer:Number;
    attribute fpsTimer:Number;
    
    attribute balls:JavaFxBall[];

    function update() {
        foreach (i in [0.._N]) {
             balls[i].move();
        }
        foreach (i in [0.._N]) {
             foreach (j in [i+1.._N]) {
                balls[i].doCollide(balls[j]);
             }
        }
        _frames = _frames + 1;
    };

    attribute <<fps>>:String = "-- fps";
}

/*
attribute BallsTest.timer = bind [1..1000] dur 1000 linear
while _is_running
continue if _is_running;

attribute BallsTest.fpsTimer = bind [1..10] dur 30000 linear
while _is_running
continue if _is_running;

trigger on BallsTest.timer = value{
    update();
}

trigger on BallsTest.fpsTimer = value{
    <<fps>> = "{Math.round( 1000*_frames/(System.currentTimeMillis() - _startTime) )} fps";
    _frames = 0;
    _startTime = System.currentTimeMillis();
}
*/

var test = new BallsTest();

var win = Frame {
        title: "JavaFX Balls"
        width: 510
        height: 366
        content: BorderPanel {
            center: Canvas {
                    content: [
                        Rect {
                            width: 500
                            height: 300
                            fill: Color.WHITE
                        },
                        Text {
                            x: 4
                            y: 4
                            content: bind test.<<fps>>
                            font: Font {faceName: "Arial", size: 14}
                        },
                        Group {
                            content: bind foreach (b in test.balls) (b.img as java.lang.Object) as Node
                        }
                        //bind test._ballImg
                    ]
                },
            bottom: FlowPanel{
                alignment: Alignment.LEADING
                content: [
                    Button{
                        text: bind if (not test._is_running) then "Start" else "Stop"
                        action: function() {
                            test._is_running = not test._is_running;
                        }
                    },
                    Label {
                        text: "# of balls:"
                    }, 
                    Spinner {
                      min: 2
                      max: 1024
                      value: bind test._N
                    }
                ]
            }
        }      
        visible: true
        resizable: false
        onClose: function() {
            test._is_running = false;
            test.timer = 0;
            test.fpsTimer = 0;
	    delete test;
        }
};
