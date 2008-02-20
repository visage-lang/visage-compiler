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
    //TODO GLOW
    /***************************************************
    function makeGlowAnim(filt:Pointer):Timeline{
        Timeline {
            keyFrames:
            [KeyFrame {
                keyTime: 0s
                action: function() {
                    //TODO GLOW FILTER
                    //filt.set(Glow{});
                }

            },
            KeyFrame {
                keyTime: 400ms
                action: function() {
                    filt.set(null);
                }
            }]
         }
    }
    ***************************************************/  
    attribute pf: PointerFactory = PointerFactory{};
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
    function composeNode():Node {
        var go1:Group;
        var go2:Group;
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
                        
                        
                        //TODO TRIGGER - GLOW 
                        /************
                         * var filt = null as Filter;
                        var glowAnim = bind makeGlowAnim(pf.make(filt).unwrap());
                        trigger on (h = box.hover) {
                            if (h) { glowAnim.start(); } else { glowAnim.stop(); filt = null;}
                        }
                        filter: bind filt
                        **********/
                        
                        cursor: Cursor.HAND
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
                            //TODO trigger - GLOW
                            /**************
                            var filt = null as Filter;
                            var glowAnim = bind makeGlowAnim(pf.make(filt).unwrap());
                            trigger on (h = box.hover) {
                                if (h) { glowAnim.start(); }
                            }
                            filter: bind filt
                            ********************/
                            
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
                                stops: [Stop{offset: 0.3, color: Color.rgba(1, 1, 1, .6)}, Stop{offset: .7, color: Color.rgba(1, 1, 1, 0)}]
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
