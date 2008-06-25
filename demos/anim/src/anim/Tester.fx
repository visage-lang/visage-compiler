package anim;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.transform.*;
import javafx.scene.geometry.*;
import javafx.scene.text.*;
import javafx.ext.swing.*;
import javafx.animation.*;
import javafx.input.*;
import java.lang.System;
import java.lang.Math;

var x = 0;
var interpolate = Interpolator.LINEAR;
var t = bind Timeline {
    keyFrames: [
        KeyFrame {
            time: 0s;
            values: x => 0.0
        },
        KeyFrame {
            time: 2s;
            values: x => 400.0 tween interpolate
        }
    ]
};

var x1 = 0.25;
var y1 = 0.25;
var x2 = 0.75;
var y2 = 0.75;
var canvas:Canvas;

var origCX = 0.0;
var origCY = 0.0;
var circle1:Circle;
var circle2:Circle;

Frame {
    height: 600, width: 500;
    visible: true
    content:
    BorderPanel {
        center: Canvas {
            background: Color.WHITE
            content:
            [ComponentView {
                transform: Transform.translate(75, 75);
                component: Label {
                    font: Font { name: "ARIAL", size: 30 };
                    foreground: Color.BLUE;
                    text: "<html><div width='400'>This simple example demostrates the various built-in interpolation functions. Click one of the buttons to animate the green Rect's x coordinate from 0 to 400 over 2 seconds</div></html>";
                }
            },
            Rectangle {
                x: bind x;
                height: 50;
                width: 50;
                fill: Color.GREEN
            }]
        }
        bottom: BorderPanel {
            top: GridPanel {
                rows: 0
                columns: 3        
                content:
                [Button {
                    text: "EaseIn";
                    action: function():Void { interpolate = Interpolator.EASEIN; t.start(); }
                },
                Button {
                    text: "EaseOut";
                    action: function():Void { interpolate = Interpolator.EASEOUT; t.start(); }
                },
                Button {
                    text: "EaseBoth";
                    action: function():Void { interpolate = Interpolator.EASEOUT; t.start(); }
                },
                Button {
                    text: "LINEAR";
                    action: function():Void { interpolate = Interpolator.LINEAR; t.start(); }

                },
                Button {
                    text: "DISCRETE";
                    action: function():Void { interpolate = Interpolator.DISCRETE; t.start(); }

                },
                Button {
                    text: "SPLINE";
                    action: function():Void { 
                        interpolate = Interpolator.SPLINE(x1, y1, x2, y2); 
                        t.start(); 
                    }

                }            
                ]
            }
            center: canvas = Canvas {
                content: [
                    Text {
                        x: 5
                        y: 10
                        content: "Spline Parameters"
                    },
                    Text {
                        x: 5
                        y: 20
                        content: "(Drag points to set)"
                        font: Font { name:"Courier" style: FontStyle.ITALIC size:10}
                    },
                    Group {
                        translateX: bind canvas.width/2 - 50
                        translateY: 10
                        content: [
                            Rectangle {
                                width: 100
                                height: 100
                                stroke: Color.BLACK
                            },
                            Text {
                                x: -20
                                y: 115
                                content: "(0.0,0.0)"
                            },
                            Text {
                                x: 105
                                y: 5
                                content: "(1.0,1.0)"
                            }, 
                            Text {
                                x: -120
                                y: 55
                                content: bind "x1: {%3.2f x1} y1: {%3.2f y1}"
                                stroke: Color.DODGERBLUE
                                font: Font { name:"Courier" size:10}
                            },
                            Text {
                                x: 110
                                y: 55
                                content: bind "x2: {%3.2f x2} y2: {%3.2f y2}"
                                stroke: Color.BLUE
                                font: Font { name:"Courier" size:10}
                            },                        
                            circle1 = Circle {
                                centerX: bind x1 * 100
                                centerY: bind 100.0 - y1*100
                                radius: 5
                                fill: Color.DODGERBLUE
                                onMousePressed: function(e:MouseEvent):Void {
                                    origCX = circle1.centerX;
                                    origCY = circle1.centerY;

                                }
                                onMouseReleased: function(e:MouseEvent):Void {
                                    var nx = e.getDragX() + origCX;
                                    var ny = e.getDragY() + origCY;
                                    x1 = nx/ 100.0;
                                    if(x1 < 0) {
                                        x1 = 0
                                    }else if(x1 > 1.0) {
                                        x1 = 1.0;
                                    }
                                    y1 = 1.0 - ny/100.0;
                                    if(y1 < 0) {
                                        y1 = 0
                                    }else if(y1 > 1.0) {
                                        y1 = 1.0;
                                    }                                    
                                }
                                onMouseDragged: function(e:MouseEvent):Void {
                                    var nx = e.getDragX() + origCX;
                                    var ny = e.getDragY() + origCY;
                                    x1 = nx/ 100.0;
                                    if(x1 < 0) {
                                        x1 = 0
                                    }else if(x1 > 1.0) {
                                        x1 = 1.0;
                                    }
                                    y1 = 1.0 - ny/100.0;
                                    if(y1 < 0) {
                                        y1 = 0
                                    }else if(y1 > 1.0) {
                                        y1 = 1.0;
                                    } 
                                }
                            },
                            circle2 = Circle {
                                centerX: bind x2 * 100.0
                                centerY: bind 100 - y2 * 100.0
                                radius: 5
                                fill: Color.BLUE
                                onMousePressed: function(e:MouseEvent):Void {
                                    origCX = circle2.centerX;
                                    origCY = circle2.centerY;

                                }
                                onMouseReleased: function(e:MouseEvent):Void {
                                    var nx = e.getDragX() + origCX;
                                    var ny = e.getDragY() + origCY;
                                    x2 = nx/ 100.0;
                                    if(x2 < 0) {
                                        x2 = 0
                                    }else if(x2 > 1.0) {
                                        x2 = 1.0;
                                    }
                                    y2 = 1.0 - ny/100.0;
                                    if(y2 < 0) {
                                        y2 = 0
                                    }else if(y2 > 1.0) {
                                        y2 = 1.0;
                                    } 
                                }
                                onMouseDragged: function(e:MouseEvent):Void {
                                    var nx = e.getDragX() + origCX;
                                    var ny = e.getDragY() + origCY;
                                    x2 = nx/ 100.0;
                                    if(x2 < 0) {
                                        x2 = 0
                                    }else if(x2 > 1.0) {
                                        x2 = 1.0;
                                    }
                                    y2 = 1.0 - ny/100.0;
                                    if(y2 < 0) {
                                        y2 = 0
                                    }else if(y2 > 1.0) {
                                        y2 = 1.0;
                                    } 
                                }                            
                            },                        
                            CubicCurve {
                                startX: 0
                                startY: 100
                                controlX1: bind x1 * 100.0
                                controlY1: bind 100 - y1 * 100.0
                                controlX2: bind x2 * 100.0
                                controlY2: bind 100 - y2 * 100.0
                                endX: 100
                                endY: 0
                                stroke: Color.RED
                            }
                        ]
                    }
                ]
            }
        }
    }
}
