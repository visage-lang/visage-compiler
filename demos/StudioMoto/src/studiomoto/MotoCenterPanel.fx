package studiomoto;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.effect.Glow;
import javafx.ext.swing.*;
import javafx.animation.*;

public class MotoCenterPanel extends CustomNode {
    var height: Number = 300;
    var width: Number = 500;
    
    var level1:Number;
    var glow1:Glow = Glow{level:bind level1};
    var glow1Animation = Timeline {
        keyFrames:
        [KeyFrame {
            time: 0s
            action: function() {
                glow1 = Glow{level:0.8};
            }
        },
        KeyFrame {
            time: 400ms
            action: function() {
                glow1 = null;
            }            
        }]
    };
    var level2:Number;
    var glow2:Glow = Glow{level:bind level2};
    
    var glow2Animation = Timeline {
        keyFrames:
        [KeyFrame {
            time: 0s
            action: function() {
                glow2 = Glow{level:0.8};
            }            
        },
        KeyFrame {
            time: 400ms
            action: function() {
                glow2 = null;
            }            
        }]
    };    
    
    var rect:Rectangle;
    var lhover:Boolean = bind rect.isMouseOver() on replace { fade.start(); }
    var alpha = .5;
    var fade = Timeline {
        toggle: true
        
        keyFrames: [
            KeyFrame {
                time: 0s
                values: alpha => .5
            },
            KeyFrame {
                time: 100ms
                values: alpha => 1.0 tween Interpolator.EASEBOTH
            }
        ]
    };    
    var go1:Group;
    var go1Hover = bind go1.isMouseOver() on replace  {
        if(go1Hover) {
            glow1Animation.start();
        }else {
            glow1Animation.stop();
            glow1 = null;
        }
    };
    var go2:Group;
      var go2Hover = bind go2.isMouseOver() on replace  {
        if(go2Hover) {
            glow2Animation.start();
        }else {
            glow2Animation.stop();
            glow2 = null;
        }
    };  

    override function create():Node {

            Group {
                content:
                [ImageView {
                    transform: bind Transform.translate(width/2, height/2)
                    verticalAlignment: VerticalAlignment.CENTER
                    horizontalAlignment: HorizontalAlignment.CENTER
                    image: Image {url: "{__DIR__}Image/73.png"}
                },
                VBox {
                    content:
                    [ComponentView {
                        component:
                        Label {
                            text: "<html><div style='font-face:verdana;font-size:28;'><span style='color:white;'>moto</span><span style='color:yellow;font-style:italic;font-weight:bold;'>remix</span></div><div style='width:200;color:white;font-size:10;font-weight:bold;'>Mix a music video. Then show it off, or save it to your mobile phone.</html>"
                        }
                    },
                    go1 = Group {
                        cursor: Cursor.HAND
                        //filter: bind glow1
                        content:
                        [ImageView {
                            visible: bind not go1.isMouseOver()
                            // 56x35
                            image: Image {url: "{__DIR__}Image/74.png"}
                        },
                        ImageView {
                            visible: bind go1.isMouseOver()
                            image: Image {url: "{__DIR__}Image/75.png"}
                        },
                        Text {
                            textOrigin: TextOrigin.TOP
                            transform: Transform.translate(56/2, 35/2)
                            verticalAlignment: VerticalAlignment.CENTER
                            horizontalAlignment: HorizontalAlignment.CENTER
                            fill: bind if (go1.isMouseOver()) Color.BLACK else Color.WHITE
                            content: "GO"
                            font: Font.font("ARIAL", FontStyle.BOLD, 11)
                        }] 
                    }]
                },
                VBox {
                    transform: Transform.translate(width/2+30, height)
                    verticalAlignment: VerticalAlignment.BOTTOM
                    horizontalAlignment: HorizontalAlignment.LEADING
                    content:
                    [Group {
                        transform: Transform.translate(120, 0)
                        
                        content:
                        [go2 = Group {
                            //filter: bind glow2
                            cursor: Cursor.HAND
                            content:
                            [ImageView {
                                visible: bind not go2.isMouseOver()
                                // 56x35
                                image: Image {url: "{__DIR__}Image/74.png"}
                            },
                            ImageView {
                                visible: bind go2.isMouseOver()
                                image: Image {url: "{__DIR__}Image/75.png"}
                            },
                            Text {
                                textOrigin: TextOrigin.TOP
                                transform: Transform.translate(56/2, 35/2)
                                verticalAlignment: VerticalAlignment.CENTER
                                horizontalAlignment: HorizontalAlignment.CENTER
                                fill: bind if (go2.isMouseOver()) Color.BLACK else Color.WHITE
                                content: "GO"
                                font: Font.font("ARIAL", FontStyle.BOLD, 11)
                            }]
                        },
                        rect = Rectangle {visible: false
                            height: 135,
                            width: 156;
                            var fill1 = RadialGradient {radius: 50, centerX: 156/2, centerY: 135/2, 
                                stops: [Stop{offset: 0.3, color: Color.color(1, 1, 1, .6)}, Stop{offset: .7, color: Color.color(1, 1, 1, 0)}]
                            };
                            opacity: bind alpha
                            fill: fill1
                            arcHeight: 20
                            arcWidth: 20
                        }]
                    },
                    ComponentView {
                        component:
                        Label {
                            text: "<html><div style='width:200;color:white;font-size:10;font-weight:bold;'>Create custom animations and sounds for your mobile phone.</div><div style='font-face:verdana;font-size:28;'><span style='color:white;'>moto</span><span style='color:yellow;font-style:italic;font-weight:bold;'>graph</span></div></html>"
                        }
                    }
                    ]
            }]
        }
    }
}
    
function run() {
    Canvas {
        background: Color.BROWN
        content: MotoCenterPanel {
            height: 300
            width: 500
        } as Node
    }
}
