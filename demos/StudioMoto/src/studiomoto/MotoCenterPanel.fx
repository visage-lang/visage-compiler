package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

public class MotoCenterPanel extends CompositeNode {
    attribute height: Number = 300;
    attribute width: Number = 500;

    function composeNode() {
        Clip {
            
            content:
            Group {
                
                function makeGlowAnim(filt:Filter) = KeyFrameAnimation {
                    
                    keyFrames:
                    [at (0s) {
                        filt => Glow;
                    },
                    at (.4s) {
                        filt => null;
                    }]
                };
                content:
                [ImageView {
                    transform: bind translate(width/2, height/2)
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
                        var: box
                        var filt : Filter = null
                        var glowAnim = bind makeGlowAnim(filt)
                        trigger on (h = box.hover) {
                            if (h) { glowAnim.start(); } else { glowAnim.stop(); filt = null;}
                        }
                        filter: bind filt
                        var: g
                        cursor: HAND
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
                            transform: translate(56/2, 35/2)
                            valign: VerticalAlignment.MIDDLE
                            halign: HorizontalAlignment.CENTER
                            fill: bind if (g.hover) black else white
                            content: "GO"
                            font: new Font("ARIAL", "BOLD", 11)
                        }]
                    }]
                },
                VBox {
                    transform: translate(width/2+30, height)
                    valign: VerticalAlignment.BOTTOM
                    halign: HorizontalAlignment.LEADING
                    content:
                    [Group {
                        transform: translate(120, 0)
                        
                        content:
                        [Group {
                            var: g
                            var: box
                            var filt = (Filter)null
                            var glowAnim = bind makeGlowAnim(filt)
                            trigger on (h = box.hover) {
                                if (h) { glowAnim.start(); }
                            }
                            filter: bind filt
                            cursor: HAND
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
                                transform: translate(56/2, 35/2)
                                valign: VerticalAlignment.MIDDLE
                                halign: HorizontalAlignment.CENTER
                                fill: bind if (g.hover) black else white
                                content: "GO"
                                font: new Font("ARIAL", "BOLD", 11)
                            }]
                        },
                        Rect {visible: false
                            selectable: true
                            height: 135,
                            width: 156
                            var alpha = .5
                            var: rect
                            var fade = KeyFrameAnimation {
                                toggle: true
                                keyFrames:
                                [at (0s) {
                                    alpha => .5;
                                },
                                at (100ms) {
                                    alpha => 1 tween EASEBOTH;
                                }]
                            }
                            var fill1 = RadialGradient {radius: 50, cx: 156/2, cy: 135/2, 
                                stops: [{offset: 0.3, color: new Color(1, 1, 1, .6)}, {offset: .7, color: new Color(1, 1, 1, 0)}]
                            }
                            trigger on (h = rect.hover) { fade.start(); }
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
    background: brown
    content:
    MotoCenterPanel {
        height: 300
        width: 500
    }
}