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

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import javafx.ext.swing.SwingToggleGroup;
import javafx.ext.swing.SwingFrame;
import javafx.ext.swing.Menu;
import javafx.ext.swing.MenuItem;
import javafx.ext.swing.RadioButtonMenuItem;
import javafx.ext.swing.Canvas;
import javafx.ext.swing.BorderPanel;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.FlowPanel;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.geometry.Rectangle;
import javafx.scene.geometry.Circle;
import javafx.scene.geometry.Path;
import javafx.scene.geometry.MoveTo;
import javafx.scene.geometry.CurveTo;
import javafx.scene.geometry.ClosePath;
import javafx.scene.geometry.Ellipse;
import javafx.util.StringLocalizer;
import java.lang.System;
import java.lang.Math;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import com.sun.scenario.animation.FrameJob;
import com.sun.scenario.Settings;

Settings.set("com.sun.scenario.animation.pulse", "200");

class Ball extends BallBase {
    attribute view:Node;
}


class BallsTest {
    attribute _is_running: Boolean = true;
    attribute _frames:Number = 0;
    attribute _startTime:Number = System.currentTimeMillis();

    function colorRGBA(r:Number, g:Number, b:Number, a:Number):Color {
        Color {red: r/255, green: g/255, blue: b/255, opacity: a/255}
    }

    function create2DBall():Ball {
        var ball:Ball = Ball{};
        ball.view = Group {
           // transform: Translate{x: -80.0, y: -48.0}
           translateX: bind ball._x - 80.0
           translateY: bind ball._y - 48.0
           content:
               Group {
                   content: [
                       Circle {
                           centerX: 106.0
                           centerY: 74.0
                           fill: colorRGBA(0xcc, 0xff, 0x00, 0xff)
                           opacity: 0.9
                           radius: 25.0
                           stroke: colorRGBA(0xa7, 0xd1, 0x00, 0xff)
                           strokeWidth: 2.0
                       },
                       Path {
                           elements: [
                               MoveTo {
                                   x: 123.143
                                   y: 61.088
                                   absolute: true
                               },
                               CurveTo {
                                   controlX1: 130.602
                                   controlY1: 70.889
                                   controlX2: 129.01
                                   controlY2: 84.643
                                   x: 119.59
                                   y: 91.813
                                   // smooth: false
                                   absolute: true
                               },
                               CurveTo {
                                   controlX1: 110.171
                                   controlY1: 98.981
                                   controlX2: 96.489
                                   controlY2: 96.843
                                   x: 89.032
                                   y: 87.043
                                   // smooth: false
                                   absolute: true
                               },
                               CurveTo {
                                   controlX1: 81.573
                                   controlY1: 77.24
                                   controlX2: 83.165
                                   controlY2: 63.486
                                   x: 92.584
                                   y: 56.316
                                   // smooth: false
                                   absolute: true
                               },
                               CurveTo {
                                   controlX1: 102.004
                                   controlY1: 49.149
                                   controlX2: 115.686
                                   controlY2: 51.285
                                   x: 123.143
                                   y: 61.088
                                   // smooth: false
                                   absolute: true
                               },
                               ClosePath {}
                           ]
                           fill: XMLID_6_()
                           opacity: 0.73999999
                       },
                       Ellipse {
                           centerX: 96.5
                           centerY: 62.5
                           fill: colorRGBA(0xff, 0xff, 0xff, 0xff)
                           radiusX: 8.294
                           radiusY: 4.906
                           transform:
                               Transform.affine(0.7958, -0.6055, 0.6055, 0.7958, -18.1424, 71.1966)
                       }
                   ]
               }
        }
        ball.initialize();
        ball;
    }


    function XMLID_6_():RadialGradient {
        RadialGradient {
            centerX: 156.7178
            centerY: 129.2988
            // transform: Transform.matrix(1.19, 0.165, 0.165, 1.2813, -82.7876, -94.3076)
            //TODO: gradientUnits: GradientUnits.USER_SPACE_ON_USE
            radius: 53.625
            proportional: false
            stops: [
                Stop {
                    offset: 0.0
                    color: colorRGBA(0xFF, 0xFF, 0xFF, 0xff)
                },
                Stop {
                    offset: 0.2033
                    color: colorRGBA(0xFE, 0xFF, 0xFD, 0xff)
                },
                Stop {
                    offset: 0.2765
                    color: colorRGBA(0xFD, 0xFD, 0xF6, 0xff)
                },
                Stop {
                    offset: 0.3286
                    color: colorRGBA(0xF9, 0xFB, 0xEB, 0xff)
                },
                Stop {
                    offset: 0.3708
                    color: colorRGBA(0xF4, 0xF7, 0xDA, 0xff)
                },
                Stop {
                    offset: 0.4065
                    color: colorRGBA(0xEE, 0xF2, 0xC4, 0xff)
                },
                Stop {
                    offset: 0.4157
                    color: colorRGBA(0xEC, 0xF1, 0xBD, 0xff)
                },
                Stop {
                    offset: 1.0
                    color: colorRGBA(0xCC, 0xFF, 0x00, 0xff)
                }
            ]
        }
    }

    attribute imageURL = this.getClass().getResource("resources/ball.png").toString();
    attribute image = Image { url: imageURL };

    function createImageBall():Ball {
        var ball:Ball = Ball{};
        ball.view = ImageView {
           translateX: bind ball._x
           translateY: bind ball._y
                image: image };
        ball.initialize();
        ball;
    }

    attribute balls:Ball[];

    function resetBalls():Void {
        balls = for (i in [1.._N]) { if (imageBalls) createImageBall() else create2DBall() }
    }

    attribute imageBalls:Boolean = true on replace { resetBalls() }
    attribute _N:Number = 16 on replace { resetBalls() }

    attribute timeline:Timeline = Timeline {
        repeatCount: Timeline.INDEFINITE
        autoReverse:false
        keyFrames: KeyFrame {
            time: 5ms
            action: function() {
                if (_is_running) {
                    update();
                } else {
                    stop();
                }            
            }
            canSkip: true
        }
    }  
    
    attribute fpsListener:ActionListener = ActionListener {
        override function actionPerformed(evt:ActionEvent): Void {
            if (_is_running) {
                fps = ##"{Math.round( 1000*_frames/(System.currentTimeMillis() - _startTime) )} fps";
                _frames = 0;
                _startTime = System.currentTimeMillis();
            } else {
                stop();
            }
        }
    }

    attribute fpsTimer:Timer = new Timer(3000, fpsListener);
    
    function update() {
        for (i in [0.._N-1]) {
             balls[i.intValue()].move();
        }
        for (i in [0.._N-1]) {
             for (j in [i+1.._N-1]) {
                balls[i.intValue()].doCollide(balls[j.intValue()]);
             }
        }
        _frames = _frames + 1;
    };

    attribute fps:String = ##"-- fps";
    
    function start(): Void {
        timeline.start();
        fpsTimer.start();
    }
    
    function stop(): Void {
        if (_is_running) {
            _is_running = false;
            timeline.stop();
            fpsTimer.stop();
            fps = ##"-- fps";
        }
    }
}

StringLocalizer.associate("javafxballs.resources.JavaFXBalls", "javafxballs");

var test = new BallsTest();

var tg = SwingToggleGroup {}
var tg2 = SwingToggleGroup {}

var win = SwingFrame {
            closeAction: function() {System.exit(0);}
            title: ##"JavaFX Balls"
          visible: true
        resizable: false
        menus: [
            Menu {text: ##"File"
                  items: [MenuItem {text: ##"Exit" action: function() {System.exit(0);}}]
                 },
            Menu {text: ##"Options"
                  items: [
                             Menu {text: ##"Ball Count" items: [
                                                    RadioButtonMenuItem {text: ##"1 ball" toggleGroup: tg action: function() {test._N = 1;}},
                                                    RadioButtonMenuItem {text: ##"10 balls" toggleGroup: tg action: function() {test._N = 10;}},
                                                    RadioButtonMenuItem {text: ##"16 balls" selected: true toggleGroup: tg action: function() {test._N = 16;}},
                                                    RadioButtonMenuItem {text: ##"32 balls" toggleGroup: tg action: function() {test._N = 32;}},
                                                    RadioButtonMenuItem {text: ##"100 balls" toggleGroup: tg action: function() {test._N = 100;}}]},
                             Menu {text: ##"Ball Painting" items: [
                                                    RadioButtonMenuItem {text: ##"Image Rendering" selected: true toggleGroup: tg2 action: function() {test.imageBalls = true;}},
                                                    RadioButtonMenuItem {text: ##"2D Rendering" toggleGroup: tg2 action: function() {test.imageBalls = false;}}]}

                         ]
                 }
            ]
          content:
            BorderPanel{
            center:
             Canvas { content: [
                Rectangle {
                    width: 500
                    height: 300
                    fill: Color {red: 1.0 green: 1.0 blue: 1.0 } // Color.WHITE
                },
                Group {
                    content: bind for (b in test.balls) { b.view }
                },
                Text {
                    x: 4
                    y: 4
                    textOrigin: TextOrigin.TOP
                    content: bind test.fps
                    fill: Color {red: 0.5 green: 0.5 blue: 0.5}
                    font: Font {name: ##"Arial", style: FontStyle.BOLD size: 14}
                }]}
              bottom: FlowPanel {content: SwingButton{
                        text: bind if (not test._is_running) then ##"Start" else ##"Stop"
                        action: function() {
                            test._is_running = not test._is_running;
                        }
                    }}
             }
};


test.start();
