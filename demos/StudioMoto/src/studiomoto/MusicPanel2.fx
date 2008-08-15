package studiomoto;

import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.ext.swing.*;

import javafx.animation.*;
import javafx.lang.Duration;

public class MusicPanel2 extends CustomNode {
    var softY: Number;
    override function create():Node {
        Group {
            clip: Rectangle {height: 160, width: 220}
            content:
            Group {
                transform: Transform.translate(0, 0)
                content:
                [ImageView {
                    transform: Transform.translate(-80, 150) 
                    verticalAlignment: VerticalAlignment.BOTTOM
                    image: Image {url: "{__DIR__}Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(260, 150)
                    verticalAlignment: VerticalAlignment.BOTTOM, 
                    horizontalAlignment: HorizontalAlignment.TRAILING
                    image: Image {url: "{__DIR__}Image/83.png"}
                },
                ImageView {
                    transform: Transform.translate(0, 50)
                    image: Image {url: "{__DIR__}Image/84.png"}
                },
                ImageView {
                    transform: Transform.translate(75, 30+softY)
                    image: Image {url: "{__DIR__}Image/85.png"}
                },

                HBox {
                    transform: Transform.translate(0, 0)
                    content:
                    [ComponentView {
                        transform: Transform.translate(0, 8)
                        component:
                        Label {
                            text: "<html><div style='color:yellow;font-size:9pt;width:120;font-face:arial;'>Click here for more information on Soft!</div></html>"
                        }
                    },
                    MoreInfoButton {
                        transform: Transform.translate(-20, 0)
                    } as Node]
                }]
            }
        };

    }
    var hoverClip:Timeline;
    public var lhover: Boolean = bind isMouseOver() on replace {
        if(hoverClip != null) {
            // stop old clip, this assumes if old clip is already stopped
            // then nothing bad will happen.
            hoverClip.stop();
        }
        var d = 500;
        
        if (lhover) {
            var ys = [[10..-2],[-2..0]]; 
            var interval = (d*1)/sizeof ys;
            hoverClip = Timeline {
                var t = 0;
                keyFrames: for(s in reverse ys) {
                    var k = KeyFrame {
                        time: Duration.valueOf(t)
                        values: softY => s tween Interpolator.EASEBOTH
                    };
                    t += interval;
                    k;
                }
             };
        } else {
            var ys = [[0..15],[15..10 step -1]];
            var interval = (d*1)/sizeof ys;
            hoverClip = Timeline {
                var t = 0;
                keyFrames: for(s in ys) {
                    var k = KeyFrame {
                        time: Duration.valueOf(t)
                        values: softY => s tween Interpolator.EASEBOTH
                    };
                    t += interval;  
                    k;
                }
             };            
        }
        hoverClip.start();
    };
}

function run( ) {
    MusicPanel2 {}
}

