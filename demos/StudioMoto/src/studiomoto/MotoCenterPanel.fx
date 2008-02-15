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
    function makeGlowAnim(filt:Filter):Timeline{
        Timeline {
            keyFrames:
            [KeyFrame {
                keyTime: 0s
                action: function() {
                    //TODO GLOW FILTER
                    //filt = Glow;
                }

            },
            KeyFrame {
                keyTime: 400ms
                action: function() {
                    filt = null;
                }
            }]
         }
    }  
    attribute pf: PointerFactory = PointerFactory{};
    function composeNode():Node {
        Clip {
            
            content:
            Group {
                

                content:
                [ImageView {
                    transform: bind Transform.translate(width/2, height/2)
                    valign: VerticalAlignment.MIDDLE
                    halign: HorizontalAlignment.CENTER
                    image: Image {url: "{__DIR__}/Image/73.png"}
                },
                VBox {
                    content:
                    [View {
                        content:
                        Label {
                            text: "<html><div style='font-face:verdana;font-size:28;'><span style='color:white;'>moto</span><span style='color:yellow;font-style:italic;font-weight:bold;'>remix</span></div><div style='width:200;color:white;font-size:10;font-weight:bold;'>Mix a music video. Then show it off, or save it to your mobile phone.</html>"
                        }
                    },
                    Group {
                        var box = this
                        var filt = null as Filter;
                        var glowAnim = bind makeGlowAnim(filt)
                        //TODO TRIGGER
                        /************
                        trigger on (h = box.hover) {
                            if (h) { glowAnim.start(); } else { glowAnim.stop(); filt = null;}
                        }
                        **********/
                        filter: bind filt
                        var g = this;
                        cursor: Cursor.HAND
                        content:
                        [ImageView {
                            //visible: bind not g.hover
                            // 56x35
                            image: Image {url: "{__DIR__}/Image/74.png"}
                        },
                        ImageView {
                            visible: bind g.hover
                            image: Image {url: "{__DIR__}/Image/75.png"}
                        },
                        Text {
                            transform: Transform.translate(56/2, 35/2)
                            valign: VerticalAlignment.MIDDLE
                            halign: HorizontalAlignment.CENTER
                            fill: bind if (g.hover) Color.BLACK else Color.WHITE
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
                        [Group {
                            var g = this
                            var box = this
                            var filt = null as Filter;
                            var glowAnim = bind makeGlowAnim(filt);
                            //TODO trigger
                            /**************
                            trigger on (h = box.hover) {
                                if (h) { glowAnim.start(); }
                            }
                            ********************/
                            filter: bind filt
                            cursor: Cursor.HAND
                            content:
                            [ImageView {
                                //visible: bind not g.hover
                                // 56x35
                                image: Image {url: "{__DIR__}/Image/74.png"}
                            },
                            ImageView {
                                visible: bind g.hover
                                image: Image {url: "{__DIR__}/Image/75.png"}
                            },
                            Text {
                                transform: Transform.translate(56/2, 35/2)
                                valign: VerticalAlignment.MIDDLE
                                halign: HorizontalAlignment.CENTER
                                fill: bind if (g.hover) Color.BLACK else Color.WHITE
                                content: "GO"
                                font: new Font("ARIAL", "BOLD", 11)
                            }]
                        },
                        Rect {visible: false
                            selectable: true
                            height: 135,
                            width: 156
                            var alpha = .5
                            var rect = this;
                            var fade = Timeline {
                                toggle: true
                                var _alpha = pf.make(alpha).unwrap()
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
                            var fill1 = RadialGradient {radius: 50, cx: 156/2, cy: 135/2, 
                                stops: [Stop{offset: 0.3, color: Color.rgba(1, 1, 1, .6)}, Stop{offset: .7, color: Color.rgba(1, 1, 1, 0)}]
                            }
                            //TODO Trigger
                            //trigger on (h = rect.hover) { fade.start(); }
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
    content:
    MotoCenterPanel {
        height: 300
        width: 500
    }
}