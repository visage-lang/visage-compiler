package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;

public class MotoCenterPanel extends CompositeNode {
    attribute height: Number = 300;
    attribute width: Number = 500;
    
    attribute pf: PointerFactory = PointerFactory{};
    
    attribute level1:Number;
    attribute glow1:Glow = Glow{level:bind level1};
    private attribute __level1 = bind pf.make(level1);
    private attribute _level1 = __level1.unwrap();
    attribute glow1Animation = Timeline {
        keyFrames:
        [KeyFrame {
            keyTime: 0s
            /****
            keyValues: NumberValue {
                target: _level1;
                value: .8
            } 
             * ****/
            action: function() {
                glow1 = Glow{level:0.8};
            }
        },
        KeyFrame {
            keyTime: 400ms
            /*****
            keyValues: NumberValue {
                target: _level1;
                value: 0
            } 
             * ****/
            action: function() {
                glow1 = null;
            }            
        }]
    };
    attribute level2:Number;
    attribute glow2:Glow = Glow{level:bind level2};
    private attribute __level2 = bind pf.make(level2);
    private attribute _level2 = __level2.unwrap();
    
    attribute glow2Animation = Timeline {
        keyFrames:
        [KeyFrame {
            keyTime: 0s
            /******
            keyValues: NumberValue {
                target: _level2;
                value: .8
            } 
             * ****/
            action: function() {
                glow2 = Glow{level:0.8};
            }            
        },
        KeyFrame {
            keyTime: 400ms
            /***
            keyValues: NumberValue {
                target: _level2;
                value: 0
            } 
             * ****/
            action: function() {
                glow2 = null;
            }            
        }]
    };    
    
    private attribute rect:Rect;
    private attribute lhover:Boolean = bind rect.hover on replace { fade.start(); }
    private attribute alpha = .5;
    private attribute _alpha = bind pf.make(alpha).unwrap();
    private attribute fade = Timeline {
        toggle: true
        
        keyFrames: [
            KeyFrame {
                keyTime: 0s
                keyValues: NumberValue {
                    target: _alpha;
                    value: .5
                }                                        
            },
            KeyFrame {
                keyTime: 100ms
                keyValues: NumberValue {
                    target: _alpha;
                    value: 1
                    interpolate: NumberValue.EASEBOTH
                }                                          
            }
        ]
    };    
    attribute go1:Group;
    attribute go1Hover = bind go1.hover on replace  {
        if(go1Hover) {
            glow1Animation.start();
        }else {
            glow1Animation.stop();
            glow1 = null;
        }
    };
    attribute go2:Group;
      attribute go2Hover = bind go2.hover on replace  {
        if(go2Hover) {
            glow2Animation.start();
        }else {
            glow2Animation.stop();
            glow2 = null;
        }
    };  

    function composeNode():Node {

        Clip {
            content:
            Group {
                content:
                [ImageView {
                    transform: bind Transform.translate(width/2, height/2)
                    valign: VerticalAlignment.MIDDLE
                    halign: HorizontalAlignment.CENTER
                    image: Image {url: "{__DIR__}Image/73.png"}
                },
                VBox {
                    content:
                    [View {
                        content:
                        Label {
                            text: "<html><div style='font-face:verdana;font-size:28;'><span style='color:white;'>moto</span><span style='color:yellow;font-style:italic;font-weight:bold;'>remix</span></div><div style='width:200;color:white;font-size:10;font-weight:bold;'>Mix a music video. Then show it off, or save it to your mobile phone.</html>"
                        }
                    },
                    go1 = Group {
                        cursor: Cursor.HAND
                        //filter: bind glow1
                        content:
                        [ImageView {
                            visible: bind not go1.hover
                            // 56x35
                            image: Image {url: "{__DIR__}Image/74.png"}
                        },
                        ImageView {
                            visible: bind go1.hover
                            image: Image {url: "{__DIR__}Image/75.png"}
                        },
                        Text {
                            transform: Transform.translate(56/2, 35/2)
                            valign: VerticalAlignment.MIDDLE
                            halign: HorizontalAlignment.CENTER
                            fill: bind if (go1.hover) Color.BLACK else Color.WHITE
                            content: "GO"
                            font: Font.Font("ARIAL", ["BOLD"], 11)
                        }] 
                    }]
                },
                VBox {
                    transform: Transform.translate(width/2+30, height)
                    valign: VerticalAlignment.BOTTOM
                    halign: HorizontalAlignment.LEADING
                    content:
                    [Group {
                        transform: Transform.translate(120, 0)
                        
                        content:
                        [go2 = Group {
                            //filter: bind glow2
                            cursor: Cursor.HAND
                            content:
                            [ImageView {
                                visible: bind not go2.hover
                                // 56x35
                                image: Image {url: "{__DIR__}Image/74.png"}
                            },
                            ImageView {
                                visible: bind go2.hover
                                image: Image {url: "{__DIR__}Image/75.png"}
                            },
                            Text {
                                transform: Transform.translate(56/2, 35/2)
                                valign: VerticalAlignment.MIDDLE
                                halign: HorizontalAlignment.CENTER
                                fill: bind if (go2.hover) Color.BLACK else Color.WHITE
                                content: "GO"
                                font: Font.Font("ARIAL", ["BOLD"], 11)
                            }]
                        },
                        rect = Rect {visible: false
                            selectable: true
                            height: 135,
                            width: 156
                            

                            var fill1 = RadialGradient {radius: 50, cx: 156/2, cy: 135/2, 
                                stops: [Stop{offset: 0.3, color: Color.color(1, 1, 1, .6)}, Stop{offset: .7, color: Color.color(1, 1, 1, 0)}]
                            }
                            opacity: bind alpha
                            fill: fill1
                            arcHeight: 20
                            arcWidth: 20
                        }]
                    },
                    View {
                        content:
                        Label {
                            text: "<html><div style='width:200;color:white;font-size:10;font-weight:bold;'>Create custom animations and sounds for your mobile phone.</div><div style='font-face:verdana;font-size:28;'><span style='color:white;'>moto</span><span style='color:yellow;font-style:italic;font-weight:bold;'>graph</span></div></html>"
                        }
                    }
                    ]
            }]
        }}
    }
}
    
Canvas {
    background: Color.BROWN
    content: MotoCenterPanel {
        height: 300
        width: 500
    } as Node
}
