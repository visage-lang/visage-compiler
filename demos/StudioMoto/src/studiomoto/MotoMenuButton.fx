package studiomoto;
import java.lang.System;
import java.lang.Math;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.animation.*;

public class MotoMenuButton extends CustomNode {
    public attribute anim: MotoMenuAnimation;
    public attribute label1: String;
    public attribute label2: String;
    public attribute action: function();
    attribute hover: Boolean = bind rect.isMouseOver() on replace {
        a.start();
        if (hover) {
            anim.start();
        } else {
            anim.stop();
        }        
    };
    attribute a: Timeline = Timeline {
        toggle: true
        keyFrames: [
            KeyFrame {
                time: 0s
                values: y => 0
            },
            KeyFrame {
                time: 200ms
                values: y => -12 tween Interpolator.EASEBOTH
            },
            KeyFrame {
                time: 400ms
                values: y => -10 tween Interpolator.EASEBOTH
            }
        ]
    };

    attribute y: Number;
    private attribute group:Group;
    private attribute rect:Rectangle;
    
    function create():Node {
        Group {
            content:
            [group = Group {
                var w = 110;
                var h = 60;
                cursor: Cursor.HAND
                transform: bind Transform.translate(0, y)
                content: /** bind **/
                [rect = Rectangle {
                    width: w
                    height: h
                    fill: Color.BLACK
                    arcHeight: 20
                    arcWidth: 20
                    onMouseClicked: function(e) {if(action != null) action();}
                },
                Rectangle {
                    clip: Rectangle {height: h*.25, width: w}
                    width: w
                    height: h*.8
                    fill: LinearGradient {
                        endX: 0, endY: 1
                        stops:
                        [Stop {
                            offset: 0
                            color: Color.color(.7, .7, .7, 1)
                        },
                        Stop {
                            offset: 0.05
                            color: Color.color(.2, .2, .2, 1)
                        },
                        Stop {
                            offset: 0.5
                            color: Color.color(.1, .1, .1, 1)
                        }]
                    }
                    arcHeight: 20
                    arcWidth: 20
                },
                Group {
                    visible: bind not hover    
                    transform: Transform.translate(30, h*.3)
                    verticalAlignment: VerticalAlignment.CENTER
                    content:
                    VBox {
                        var textColor = Color.ORANGERED;
                        var font1 = Font {name: "ARIAL", style: FontStyle.PLAIN size: 12};
                        var font = Font {name: "ARIAL", style: FontStyle.BOLD size: 12};
                        content:
                        [Text {
                            textOrigin: TextOrigin.TOP
                            font: font1
                            fill: Color.ORANGE
                            content: bind label1
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            transform: Transform.translate(0, 3)
                            fill: Color.WHITE
                            font: font
                            content: bind label2
                        }]
                    }
                },
                Group {
                    var empty:Node[] = [];
                    visible: bind mouseOver
                    content: bind 
                    [Group {
                        transform: Transform.translate(w -5, h*.4)
                        verticalAlignment: VerticalAlignment.CENTER, 
                        horizontalAlignment: HorizontalAlignment.TRAILING
                        content: bind if (hover) then [anim] else empty
                    },
                    HBox {
                        var textColor = Color.ORANGERED;
                        var font = Font {name: "ARIAL", style: FontStyle.BOLD size: 8};
                        transform: Transform.translate(w-5, h*.7)
                        verticalAlignment: VerticalAlignment.CENTER, horizontalAlignment: HorizontalAlignment.TRAILING
                        content:
                        [Text {
                            textOrigin: TextOrigin.TOP
                            font: font
                            fill: Color.ORANGE
                            content: bind label1.toUpperCase()
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            transform: Transform.translate(2, 0)
                            fill: Color.WHITE
                            font: font
                            content: bind label2.toUpperCase()
                        }]
                    }]
                },
                Group {
                    transform: Transform.translate(10, 5)
                    content:
                    [
                    ImageView {
                        image: Image {url: "{__DIR__}Image/7.png"}
                    },
                    ImageView {
                        opacity: bind if (hover) then 0.5 else 0
                        transform: Transform.translate(-3, -3)
                        image: Image {url: bind "{__DIR__}Image/8.png"}
                    }]
                }]
            }]
    
        }
    }
}

MotoMenuButton { 
    label1: "Test", 
    label2: "Test 2" , 
    transform: Transform.translate(100, 100)
    anim: MotoMenuAnimation {active: true}
};

