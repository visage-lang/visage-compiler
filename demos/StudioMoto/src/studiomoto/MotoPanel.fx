package studiomoto;
import javafx.scene.*;
import javafx.scene.geometry.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.transform.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.ext.swing.*;

import java.lang.System;

public class MotoPanel extends Intro {
    var width: Number;
    var height: Number;
    var titleX: Number;
    var contentY: Number;
    var title: Node;
    var content: Node;
    var alpha1: Number;
    
    
    var intro: Timeline = Timeline {

        keyFrames:
           [ KeyFrame {
                time: 0s
                values: [ titleX => width, contentY => height, alpha1 => 0 ]
            },
            KeyFrame {
                time: 250ms
                values: [ alpha1 => 1 tween Interpolator.LINEAR ]
            },
            KeyFrame {
                time: 1s
                values: [ 
                    titleX => 0 tween Interpolator.EASEBOTH, 
                    contentY => 0 tween Interpolator.EASEBOTH, 
                    alpha1 => 0 tween Interpolator.LINEAR 
                ]
            }
         ]
    };
    override function doIntro():Void {
       intro.start();
    }
    var level:Number;
    var glow:Glow = Glow{level:bind level};
    var glowAnimation = Timeline {
        keyFrames:
        [KeyFrame {
            time: 0s
            action: function() {
                glow = Glow{};
            }            
        },
        KeyFrame {
            time: 300ms
            action: function() {
                glow = null;
            }              
        }]
    }; 
    
    var hover = bind isMouseOver() on replace {
        if(hover) {
            glowAnimation.start();
        } else {
            glowAnimation.stop();
            glow = null;
        }
    }

    override function create():Node {
        Group {
            clip: Rectangle {height: bind height, width: bind width}
            onMouseClicked: function(e) {doIntro();}
            content: Group {
            effect: bind glow
            content:
            [ImageView {
                transform: Transform.translate(0, 2)
                image: Image {url: "{__DIR__}Image/77.png"}
            },
            Circle {
                opacity: bind alpha1
                centerX: 10.5
                centerY: 12.5
                                                                                                radius: 10
                //fill: Color.WHITE
                fill:RadialGradient {
                    centerX: 10
                    centerY: 10
                    radius: 1
                    stops:
                    [Stop {
                        offset: 0
                        color: Color.WHITE
                    },
                    Stop {
                        offset: 1
                        color: Color.color(1, 1, 1, 0)
                    }]
                }
            },
            Group {
                clip: Rectangle {height: bind title.getHeight(), width: bind width}
                transform: bind Transform.translate(25+titleX, 18)
                content: bind title
                verticalAlignment: VerticalAlignment.BOTTOM
            },
            Group {
                transform: Transform.translate(20, 20)
                clip: Rectangle {x: -50, height: bind height-20, width: bind width+50}
                content:
                Group {
                    transform: bind Transform.translate(0, contentY)
                    content:
                    [Group {

                        content:
                        ImageView {
                            clip: bind Rectangle {height: 5, width: width}
                            image: Image {url: "{__DIR__}Image/95.png"}
                        }
                    },
                    Group {
                        transform: Transform.translate(5, 10)
                        content: bind content
                    }]
                }
            }]
        }
        };
    };
};

Canvas {
    background: Color.BLACK
    content:
    MotoPanel {
        width: 200
        height: 200
        title: Text {
            content: "Promotions", 
            textOrigin: TextOrigin.TOP
            fill: Color.WHITE, 
            font: Font{name: "ARIAL", size: 14}
        }
        content: Text {
            content: "Promotions", 
            fill: Color.WHITE, 
            font: Font{name: "ARIAL", size: 14}
        }
        
    }
}




